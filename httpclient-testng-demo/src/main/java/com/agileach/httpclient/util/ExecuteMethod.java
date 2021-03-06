package com.agileach.httpclient.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ExecuteMethod {
	private CloseableHttpClient httpClient;
	private CloseableHttpResponse response;
	private RequestConfig requestConfig;
	public final String HTTPSTATUS = "HttpStatus";
	private final static String DEFAULT_CHARSET = "UTF-8";
	private final static Logger Log = LoggerFactory.getLogger(ExecuteMethod.class);

	public ExecuteMethod() {
		requestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(1000)
				.setSocketTimeout(10000).build();
	}

	/**
	 * 
	 * @param connectTimeout           设置连接超时时间，单位毫秒。
	 * @param connectionRequestTimeout 设置从connect Manager(连接池)获取Connection
	 *                                 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
	 * @param socketTimeout            请求获取数据的超时时间(即响应时间)，单位毫秒。
	 *                                 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
	 */
	public ExecuteMethod(int connectTimeout, int connectionRequestTimeout, int socketTimeout) {
		requestConfig = RequestConfig.custom().setConnectTimeout(connectTimeout)
				.setConnectionRequestTimeout(connectionRequestTimeout).setSocketTimeout(socketTimeout).build();
	}

	private JSONObject getJSONObject(CloseableHttpResponse response) throws Exception {
		// 获取返回参数
		HttpEntity entity = response.getEntity();
		String result = null;
		if (entity != null) {
			try {
				result = EntityUtils.toString(entity, DEFAULT_CHARSET);
			} catch (Exception e) {
				// TODO Auto-generated catch block		
				throw new MyException(e);
			}
		}	
		try {
			// 释放请求，关闭连接
			EntityUtils.consume(entity);
		} catch (Exception e) {
			// TODO Auto-generated catch block		
			throw new MyException(e);
		}		
		JSONObject jsonobj = JSON.parseObject(result);
		jsonobj.put(HTTPSTATUS, response.getStatusLine().getStatusCode());
		Log.info("解析为JSON对象成功...");
		return jsonobj;
	}

	public JSONObject sendGet(String url, Map<String, String> params, Map<String, String> headers) throws Exception
			 {
		httpClient = HttpClients.createDefault();
		// 拼接url
		if (params != null) {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				String value = entry.getValue();
				if (!value.isEmpty()) {
					pairs.add(new BasicNameValuePair(entry.getKey(), value));
				}
			}
			try {
				url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs), DEFAULT_CHARSET);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new MyException(e);
			}
		}
		HttpGet httpGet = new HttpGet(url);
		try {
			httpGet.setConfig(requestConfig);
			// 加载请求头到httpget对象
			if (headers != null) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					httpGet.setHeader(entry.getKey(), entry.getValue());
				}
			}
			Log.info("开始发送get请求...");
			try {
				response = httpClient.execute(httpGet);
			} catch (Exception e) {
				// TODO Auto-generated catch block			
				throw new MyException(e);
			}
			Log.info("发送请求成功,得到响应对象...");
			return getJSONObject(response);
		} finally {
			httpClient.close();
			response.close();
		}
	}

	/*
	 * 执行Get方法并返回响应结果对象
	 */
	public JSONObject sendGet(String url, Map<String, String> params) throws Exception {
		return this.sendGet(url, params, null);
	}

	/*
	 * 执行Get方法并返回响应结果对象
	 */
	public JSONObject sendGet(String url) throws Exception {
		return this.sendGet(url, null, null);
	}

	public JSONObject sendPostByJson(String url, Object object, Map<String, String> headers) throws Exception {
		httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		try {
			httpPost.setConfig(requestConfig);
			String json = JSON.toJSONString(object);
			StringEntity entity = new StringEntity(json, DEFAULT_CHARSET);
			entity.setContentType("application/json");
			httpPost.setEntity(entity);
			if (headers != null) {
				// 加载请求头到httppost对象
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					httpPost.setHeader(entry.getKey(), entry.getValue());
				}
			}
			Log.info("开始发送post请求...");
			try {
				response = httpClient.execute(httpPost);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new MyException(e);
			}
			Log.info("发送请求成功,得到响应对象。");
			return getJSONObject(response);
		} finally {
			httpClient.close();
			response.close();
		}
	}

	/**
	 * 
	 * @param url
	 * @param parameters
	 * @param headers
	 * @return
	 * @throws Throwable
	 * @throws Exception
	 */
	public JSONObject sendPostByForm(String url, Map<String, String> form, Map<String, String> headers)
			throws Exception {
		httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		try {
			httpPost.setConfig(requestConfig);
			// 设置请求主体格式
			if (form.size() > 0) {
				ArrayList<BasicNameValuePair> list = new ArrayList<>();
				form.forEach((key, value) -> list.add(new BasicNameValuePair(key, value)));
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, DEFAULT_CHARSET);
				entity.setContentType("application/x-www-form-urlencoded");
				httpPost.setEntity(entity);
			}
			if (headers != null) {
				// 设置头部信息
				Set<String> set = headers.keySet();
				for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
					String key = iterator.next();
					String value = headers.get(key);
					httpPost.setHeader(key, value);
				}
			}
			Log.info("开始发送post请求...");
			try {
				response = httpClient.execute(httpPost);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new MyException(e);
			}
			Log.info("发送请求成功,得到响应对象。");
			return getJSONObject(response);
		} finally {
			httpClient.close();
			response.close();
		}
	}

	/**
	 * 执行Post方法并返回JSONObject对象
	 * 
	 * @param url
	 * @param parameters
	 * @return JSONObject
	 * @throws Exception
	 */
	public JSONObject sendPost(String url, Map<String, String> form) throws Exception {
		return sendPostByForm(url, form, null);
	}

	public JSONObject sendPostByJson(String url, Object object) throws Exception {
		return sendPostByJson(url, object, null);
	}

	/**
	 * 封装 put请求方法并返回JSONObject对象
	 * 
	 * @param url          接口url完整地址
	 * @param entityString 这个主要是设置payload,一般来说就是json串
	 * @param headers      带请求的头信息，格式是键值对，所以这里使用hashmap
	 * @return JSONObject
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public JSONObject sendPut(String url, String entityString, Map<String, String> headers) throws Exception {
		httpClient = HttpClients.createDefault();
		HttpPut httpPut = new HttpPut(url);
		try {
			httpPut.setConfig(requestConfig);
			httpPut.setEntity(new StringEntity(entityString, DEFAULT_CHARSET));
			if (headers != null) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					httpPut.setHeader(entry.getKey(), entry.getValue());
				}
			}
			// 发送put请求
			Log.info("开始发送put请求...");
			try {
				response = httpClient.execute(httpPut);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new MyException(e);
			}
			Log.info("发送请求成功,得到响应对象。");
			return getJSONObject(response);
		} finally {
			httpClient.close();
			response.close();
		}
	}

	/**
	 * 封装 delete请求方法，参数和get方法一样 *
	 * 
	 * @param url， 接口url完整地址
	 * @return，返回一个JSONObject对象，方便进行得到状态码和json解析动作
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public int sendDelete(String url) throws Exception {
		httpClient = HttpClients.createDefault();
		HttpDelete httpDel = new HttpDelete(url);
		try {
			httpDel.setConfig(requestConfig);
			// 发送delete请求
			Log.info("开始发送delete请求...");
			try {
				response = httpClient.execute(httpDel);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new MyException(e);
			}
			Log.info("发送请求成功,得到响应对象。");
			int statusCode = response.getStatusLine().getStatusCode();
			Log.info("得到StatusCode成功，StatusCode：" + statusCode);
			return statusCode;
		} finally {
			httpClient.close();
			response.close();
		}
	}
	

	/**
	 * 发送 http post 请求，支持文件上传
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	public JSONObject httpPostFormMultipart(String url, Map<String, String> params, List<File> files,
			Map<String, String> headers) throws Exception  {
		httpClient = HttpClients.createDefault();
		HttpPost httpost = new HttpPost(url);

		// 设置header
		if (headers != null && headers.size() > 0) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				httpost.setHeader(entry.getKey(), entry.getValue());
			}
		}
		try {
			MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();
			mEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			mEntityBuilder.setCharset(Charset.forName(DEFAULT_CHARSET));
	
			// 普通参数
			ContentType contentType = ContentType.create("text/plain", Charset.forName(DEFAULT_CHARSET));// 解决中文乱码
			if (params != null && params.size() > 0) {
				Set<String> keySet = params.keySet();
				for (String key : keySet) {
					mEntityBuilder.addTextBody(key, params.get(key), contentType);
				}
			}
			// 二进制参数
			if (files != null && files.size() > 0) {
				for (File file : files) {
					mEntityBuilder.addBinaryBody("file", file);
				}
			}
			httpost.setEntity(mEntityBuilder.build());		
			try {
				response = httpClient.execute(httpost);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new MyException(e);
			}
			return getJSONObject(response);
		} finally {
			httpClient.close();
			response.close();
		}
	}
}