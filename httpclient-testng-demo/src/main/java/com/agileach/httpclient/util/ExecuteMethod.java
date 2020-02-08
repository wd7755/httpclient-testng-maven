package com.agileach.httpclient.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ExecuteMethod {
	private CloseableHttpClient httpClient;
	private CloseableHttpResponse response;
	private RequestConfig requestConfig;
	public String HTTPSTATUS = "HttpStatus";
	private final static Logger Log = Logger.getLogger(ExecuteMethod.class);

	public ExecuteMethod() {		
		requestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(1000).setSocketTimeout(10000).build();
	}

	/**
	 * 
	 * @param connectTimeout 设置连接超时时间，单位毫秒。
	 * @param connectionRequestTimeout 设置从connect Manager(连接池)获取Connection 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
	 * @param socketTimeout 请求获取数据的超时时间(即响应时间)，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
	 */
	public ExecuteMethod(int connectTimeout, int connectionRequestTimeout, int socketTimeout) {			
		requestConfig = RequestConfig.custom().setConnectTimeout(connectTimeout).setConnectionRequestTimeout(connectionRequestTimeout).setSocketTimeout(socketTimeout).build();
	}	
	
	public JSONObject sendGet(String url, HashMap<String, String> params, HashMap<String, String> headers)
			throws Exception {
		httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		try {		
			httpGet.setConfig(requestConfig);
			// 加载请求头到httpget对象
			if (headers != null) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					httpGet.setHeader(entry.getKey(), entry.getValue());
				}
			}
			// 拼接url
			if (params != null) {
				List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> entry : params.entrySet()) {
					String value = entry.getValue();
					if (!value.isEmpty()) {
						pairs.add(new BasicNameValuePair(entry.getKey(), value));
					}
				}
				url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs), "UTF-8");
			}
			Log.info("开始发送get请求...");
			response = httpClient.execute(httpGet);
			Log.info("发送请求成功,得到响应对象...");
			// 获取返回参数
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
			}
			// 释放请求，关闭连接
			EntityUtils.consume(entity);
			JSONObject jsonobj = JSON.parseObject(result);
			jsonobj.put(HTTPSTATUS, response.getStatusLine().getStatusCode());
			Log.info("解析为JSON对象成功...");
			return jsonobj;
		} finally {
			httpClient.close();
			response.close();
		}
	}

	/*
	 * 执行Get方法并返回响应结果对象
	 */
	public JSONObject sendGet(String url, HashMap<String, String> params) throws Exception {
		return this.sendGet(url, params, null);
	}

	/*
	 * 执行Get方法并返回响应结果对象
	 */
	public JSONObject sendGet(String url) throws Exception {
		return this.sendGet(url, null, null);
	}

	public JSONObject sendPostByJson(String url, String json, HashMap<String, String> headers) throws Exception {
		httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);	
		try {
			httpPost.setConfig(requestConfig);
			StringEntity entity = new StringEntity(json, "utf-8");
			entity.setContentType("application/json");
			httpPost.setEntity(entity);
			if (headers != null) {
				// 加载请求头到httppost对象
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					httpPost.setHeader(entry.getKey(), entry.getValue());
				}
			}
			Log.info("开始发送post请求...");
			response = httpClient.execute(httpPost);
			Log.info("发送请求成功,得到响应对象。");
			HttpEntity responseEntity = response.getEntity();
			String result = null;
			if (responseEntity != null) {
				result = EntityUtils.toString(responseEntity, "UTF-8");
			}
			EntityUtils.consume(responseEntity);
			JSONObject jsonobj = JSON.parseObject(result);
			jsonobj.put(HTTPSTATUS, response.getStatusLine().getStatusCode());
			Log.info("解析为JSON对象成功...");
			return jsonobj;
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
	 * @throws Exception
	 */
	public JSONObject sendPostByForm(String url, Map<String, String> form, HashMap<String, String> headers)
			throws Exception {
		httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);	
		try {
			httpPost.setConfig(requestConfig);
			// 设置请求主体格式
			if (form.size() > 0) {
				ArrayList<BasicNameValuePair> list = new ArrayList<>();
				form.forEach((key, value) -> list.add(new BasicNameValuePair(key, value)));
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
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
			response = httpClient.execute(httpPost);
			Log.info("发送请求成功,得到响应对象。");
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			JSONObject jsonobj = JSON.parseObject(result);
			jsonobj.put(HTTPSTATUS, response.getStatusLine().getStatusCode());
			Log.info("解析为JSON对象成功...");
			return jsonobj;
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

	public JSONObject sendPostByJson(String url, String json) throws Exception {
		return sendPostByJson(url, json, null);
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
	public JSONObject sendPut(String url, String entityString, HashMap<String, String> headers)
			throws ClientProtocolException, IOException {
		httpClient = HttpClients.createDefault();
		HttpPut httpPut = new HttpPut(url);		
		try {
			httpPut.setConfig(requestConfig);
			httpPut.setEntity(new StringEntity(entityString, "utf-8"));
			if (headers != null) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					httpPut.setHeader(entry.getKey(), entry.getValue());
				}
			}
			// 发送put请求
			Log.info("开始发送put请求...");
			response = httpClient.execute(httpPut);
			Log.info("发送请求成功,得到响应对象。");
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			JSONObject jsonobj = JSON.parseObject(result);
			jsonobj.put(HTTPSTATUS, response.getStatusLine().getStatusCode());
			Log.info("解析为JSON对象成功...");
			return jsonobj;
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
	public int sendDelete(String url) throws ClientProtocolException, IOException {
		httpClient = HttpClients.createDefault();
		HttpDelete httpDel = new HttpDelete(url);
		try {
			httpDel.setConfig(requestConfig);
			// 发送delete请求
			Log.info("开始发送delete请求...");
			response = httpClient.execute(httpDel);
			Log.info("发送请求成功,得到响应对象。");
			int statusCode = response.getStatusLine().getStatusCode();
			Log.info("得到StatusCode成功，StatusCode：" + statusCode);
			return statusCode;
		} finally {
			httpClient.close();
			response.close();
		}
	}
}