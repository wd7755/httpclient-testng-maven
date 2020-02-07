package com.agileach.httpclient.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
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
	private CloseableHttpResponse response;
	public String HTTPSTATUS = "HttpStatus";
	final static Logger Log = Logger.getLogger(ExecuteMethod.class);

	/*
	 * 执行Get方法并返回响应结果对象
	 */
	public JSONObject sendGet(String url, HashMap<String, String> headers) throws Exception {
//		RequestConfig globalConfig = RequestConfig.custom()
//				.setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
//		CloseableHttpClient httpclient = HttpClients.custom()
//				.setDefaultRequestConfig(globalConfig).build();
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(url);
			// 加载请求头到httpget对象
			if (headers != null) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					httpGet.addHeader(entry.getKey(), entry.getValue());
				}
			}
			Log.info("开始发送get请求...");
			response = httpClient.execute(httpGet);
			Log.info("发送请求成功,得到响应对象。");
			HttpEntity myEntity = response.getEntity();
			// System.out.println(myEntity.getContentType());
			// System.out.println(myEntity.getContentLength());
			JSONObject jsonobj = JSON.parseObject(EntityUtils.toString(myEntity));					
			jsonobj.put(HTTPSTATUS, response.getStatusLine().getStatusCode());
			return jsonobj;
		} finally {
			response.close();
		}
	}

	/*
	 * 执行Get方法并返回响应结果对象
	 */
	public JSONObject sendGet(String url) throws Exception {
		return this.sendGet(url, null);
	}

	public JSONObject sendPost(String url, String json, HashMap<String, String> headers)
			throws Exception {
		// RequestConfig globalConfig = RequestConfig.custom()
		// .setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
		// CloseableHttpClient httpclient = HttpClients.custom()
		// .setDefaultRequestConfig(globalConfig).build();

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		try {
			httpPost.setEntity(new StringEntity(json));
			// 设置请求主体格式	
//			 httpPost.setHeader("Content-type","application/json; charset=utf-8");
//			 httpPost.setHeader("Accept", "application/json");
			if (headers != null) {
				//加载请求头到httppost对象
				for(Map.Entry<String, String> entry : headers.entrySet()) {
					httpPost.setHeader(entry.getKey(), entry.getValue());
				}
			}
			Log.info("开始发送post请求...");
			response = httpClient.execute(httpPost);
			Log.info("发送请求成功,得到响应对象。");		
			JSONObject jsonobj = JSON.parseObject(EntityUtils.toString(response.getEntity()));	
			jsonobj.put(HTTPSTATUS, response.getStatusLine().getStatusCode());
			return jsonobj;
		} finally {
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
	public JSONObject sendPost(String url, Map<String, String> form, HashMap<String, String> headers)
			throws Exception {
		// RequestConfig globalConfig = RequestConfig.custom()
		// .setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
		// CloseableHttpClient httpclient = HttpClients.custom()
		// .setDefaultRequestConfig(globalConfig).build();

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		try {
			// 设置请求主体格式
			if (form.size() > 0) {
				ArrayList<BasicNameValuePair> list = new ArrayList<>();
				form.forEach(
						(key, value) -> list.add(new BasicNameValuePair(key, value)));
				httpPost.setEntity(new UrlEncodedFormEntity(list, "utf-8"));
			}	
//			 httpPost.setHeader("Content-type","application/json; charset=utf-8");
			 //httpPost.setHeader("Accept", "application/json");
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
			HttpEntity myEntity = response.getEntity();
			JSONObject jsonobj = JSON.parseObject(EntityUtils.toString(myEntity));	
			jsonobj.put(HTTPSTATUS, response.getStatusLine().getStatusCode());
			return jsonobj;
		} finally {
			response.close();
		}
	}

	/**
	  * 执行Post方法并返回JSONObject对象
	 * @param url
	 * @param parameters
	 * @return JSONObject
	 * @throws Exception
	 */
	public JSONObject sendPost(String url, Map<String, String> form) throws Exception {
		return sendPost(url, form, null);
	}
	public JSONObject sendPost(String url, String json) throws Exception {
		return sendPost(url, json, null);
	}
	/**
	 * 封装 put请求方法并返回JSONObject对象 
	 * @param url 接口url完整地址
	 * @param entityString，这个主要是设置payload,一般来说就是json串
	 * @param headers，带请求的头信息，格式是键值对，所以这里使用hashmap
	 * @return JSONObject
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public JSONObject sendPut(String url, String entityString, HashMap<String, String> headers)
			throws ClientProtocolException, IOException {
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPut httpput = new HttpPut(url);
			httpput.setEntity(new StringEntity(entityString));
			if (headers != null) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					httpput.addHeader(entry.getKey(), entry.getValue());
				}
			}
			// 发送put请求
			Log.info("开始发送put请求...");
			response = httpclient.execute(httpput);
			Log.info("发送请求成功,得到响应对象。");
			HttpEntity myEntity = response.getEntity();
			JSONObject jsonobj = JSON.parseObject(EntityUtils.toString(myEntity));	
			jsonobj.put(HTTPSTATUS, response.getStatusLine().getStatusCode());		
			return jsonobj;
		} finally {
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
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpDelete httpdel = new HttpDelete(url);
			// 发送delete请求
			Log.info("开始发送delete请求...");
			response = httpclient.execute(httpdel);
			Log.info("发送请求成功,得到响应对象。");	
			int statusCode = response.getStatusLine().getStatusCode();	
			return statusCode;
		} finally {
			response.close();
		}
	}
}