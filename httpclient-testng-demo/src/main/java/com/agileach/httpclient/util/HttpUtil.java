package com.agileach.httpclient.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtil {
	private static CloseableHttpClient client;
	private static RequestConfig config;
	private static CloseableHttpResponse response;
	// 初始化
	static {
		config = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(20000).build();
		client = HttpClients.custom().build();
	}

	public static String doGet(String url, Map<String, String> param, final Map<String, String> header) throws Exception {
		if (url.isEmpty()) {
			return null;
		}
		try {
			// 拼接url
			if (param.size() > 0) {
				List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> entry : param.entrySet()) {
					String value = entry.getValue();
					if (!value.isEmpty()) {
						pairs.add(new BasicNameValuePair(entry.getKey(), value));
					}
				}
				url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs), "UTF-8");
			}

			HttpGet httpGet = new HttpGet(url);

			// 添加请求头
			if (header.size() > 0) {
				header.forEach((key, value) -> httpGet.setHeader(key, value));
			}
			response = client.execute(httpGet);

			// 判断请求是否成功
			int code = response.getStatusLine().getStatusCode();
			if (code != 200) {
				httpGet.abort();
				throw new RuntimeException("HttpClient,error status code :" + code);
			}

			// 获取返回参数
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
			}

			// 释放请求，关闭连接
			EntityUtils.consume(entity);
			
			return result;

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			response.close();
		}

		return null;
	}

	public static String doPostByJson(String url, String json, final Map<String, String> header) throws Exception {
		if (url.isEmpty()) {
			return null;
		}
		try {
			// 设置请求体
			HttpPost httpPost = new HttpPost(url);
			StringEntity entity = new StringEntity(json, "utf-8");
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			httpPost.setEntity(entity);

			// 添加请求头
			if (header.size() > 0) {
				header.forEach((key, value) -> httpPost.setHeader(key, value));
			}

			response = client.execute(httpPost);
			// 判断请求是否成功
			int code = response.getStatusLine().getStatusCode();
			if (code != 200) {
				httpPost.abort();
				throw new RuntimeException("HttpClient,error status code :" + code);
			}

			// 获取返回参数
			HttpEntity responseEntity = response.getEntity();
			String result = null;
			if (responseEntity != null) {
				result = EntityUtils.toString(responseEntity, "UTF-8");
			}
			EntityUtils.consume(responseEntity);		
			return result;

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			response.close();
		}
		return null;
	}

	public static String doPostByFORM(String url, Map<String, String> form, final Map<String, String> header) throws Exception {
		if (url.isEmpty()) {
			return null;
		}	
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("content-Type", "application/x-www-form-urlencoded");

			if (header.size() > 0) {
				header.forEach((key, value) -> httpPost.setHeader(key, value));
			}
			// form表单
			if (form.size() > 0) {
				ArrayList<BasicNameValuePair> list = new ArrayList<>();
				form.forEach(
						(key, value) -> list.add(new BasicNameValuePair(String.valueOf(key), String.valueOf(value))));
				httpPost.setEntity(new UrlEncodedFormEntity(list, "utf-8"));
			}
			response = client.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpPost.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}

			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			response.close();
			return result;
		} catch (IOException e) {
			e.printStackTrace();	
		}finally {
			response.close();
		}		
		return null;
	}
}