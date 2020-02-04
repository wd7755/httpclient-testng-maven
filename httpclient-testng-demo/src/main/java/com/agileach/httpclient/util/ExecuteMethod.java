package com.agileach.httpclient.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class ExecuteMethod {
	private static final String URL = "http://115.29.201.135/mobile/mobileapi.php";
	private static final String pattern = "yyyy-MM-dd HH:mm:ss:SSS";
	private CloseableHttpResponse response;
	private long startTime = 0L;
	private long endTime = 0L;
	private String resString = "";
	public static final String HTTPSTATUS = "HttpStatus";

	/*
	 * 执行Get方法并返回响应结果对象
	 */
	public JSONObject sendGet(String url) throws Exception {
//		RequestConfig globalConfig = RequestConfig.custom()
//				.setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
//		CloseableHttpClient httpclient = HttpClients.custom()
//				.setDefaultRequestConfig(globalConfig).build();
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		response = httpClient.execute(httpGet);
		try {
			HttpEntity myEntity = response.getEntity();
			// System.out.println(myEntity.getContentType());
			// System.out.println(myEntity.getContentLength());
			JSONObject jsonobj = new JSONObject(EntityUtils.toString(myEntity));
			jsonobj.put(HTTPSTATUS, response.getStatusLine().getStatusCode());
			System.out.println(jsonobj);
			return jsonobj;
		} finally {
			response.close();
		}
	}

	/*
	 * 执行Post方法并返回响应结果对象
	 */
	public JSONObject sendPost(String url, List<NameValuePair> parameters) throws Exception {
		return sendPost(url, parameters, null);
	}

	/*
	 * 执行Post方法并返回响应结果对象
	 */
	public JSONObject sendPost(String url, List<NameValuePair> parameters, HashMap<String, String> headers)
			throws Exception {
		// RequestConfig globalConfig = RequestConfig.custom()
		// .setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
		// CloseableHttpClient httpclient = HttpClients.custom()
		// .setDefaultRequestConfig(globalConfig).build();

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		startTime = System.currentTimeMillis();
		try {
			// 设置请求主体格式
			httpPost.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
			//httpPost.setHeader("Content-type","application/json; charset=utf-8");
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
			response = httpClient.execute(httpPost);
			JSONObject jsonobj = new JSONObject(EntityUtils.toString(response.getEntity()));
			jsonobj.put(HTTPSTATUS, response.getStatusLine().getStatusCode());
			//System.out.println(jsonobj);
			return jsonobj;
		} finally {
			response.close();
		}
	}

	/*
	 * 获取随机字符串
	 * 
	 * @length: 字符串的长度
	 */
	public static String getRandomString(int length) {
		// length表示生成字符串的长度
		String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		for (int i = 0; i < 3; i++) {
			System.out.println(ExecuteMethod.getRandomString(6));
		}
	}
}