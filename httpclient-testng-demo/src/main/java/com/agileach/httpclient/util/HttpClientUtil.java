package com.agileach.httpclient.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

public class HttpClientUtil {
	/**
	 * 请求接口
	 * 
	 * @param method  请求方法
	 * @param url     请求地址
	 * @return
	 */
	public static ResponseEntity<String> proxyToDesURL(String method, String url) {
		Map<String, String> params = new HashMap<String, String>();
		return proxyToDesURL(method, url, null,params);			
	}	
	/**
	 * 请求接口
	 * 
	 * @param method  请求方法
	 * @param url     请求地址
	 * @param headers 请求头部
	 * @param params  请求参数
	 * @return
	 */
	public static ResponseEntity<String> proxyToDesURL(String method, String url, Map<String, String> headers,
			Map<String, String> params) {
		// TODO Auto-generated method stub
		try {
			SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
			RestTemplate restTemplate = new RestTemplate(requestFactory);
			// 处理请求头部
			HttpHeaders requestHeaders = new HttpHeaders();
			if (headers != null && !headers.isEmpty()) {
				Set<String> set = headers.keySet();
				for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
					String key = iterator.next();
					String value = headers.get(key);
					requestHeaders.add(key, value);
				}
			}
			// 处理请求参数
			MultiValueMap<String, String> paramList = new LinkedMultiValueMap<String, String>();
			if (params != null && !params.isEmpty()) {
				if (method.equalsIgnoreCase("GET")) {
					url += "?";
					Set<String> set = params.keySet();
					for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
						String key = iterator.next();
						String value = params.get(key);
						url += key + "=" + value + "&";
					}
					url = url.substring(0, url.length() - 1);
				} else {
					Set<String> set = params.keySet();
					for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
						String key = iterator.next();
						String value = params.get(key);
						paramList.add(key, value);
					}
				}
			}
			requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(
					paramList, requestHeaders);
			// 处理请求方法
			HttpMethod requestType = HttpMethod.GET;
			method = method.toUpperCase();
			switch (method) {
			case "GET":
				requestType = HttpMethod.GET;
				break;
			case "POST":
				requestType = HttpMethod.POST;
				break;
			case "PUT":
				requestType = HttpMethod.PUT;
				break;
			case "DELETE":
				requestType = HttpMethod.DELETE;
				break;
			case "HEAD":
				requestType = HttpMethod.HEAD;
				break;
			case "OPTIONS":
				requestType = HttpMethod.OPTIONS;
				break;
			default:
				requestType = HttpMethod.GET;
				break;
			}
			ResponseEntity<String> responseEntity = restTemplate.exchange(url, requestType, requestEntity, String.class,
					params);
			// 获取返回结果
			return responseEntity;
			//return responseEntity.getBody();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 主函数
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		// 请求地址设置
		String url = "https://api.apishop.net/communication/phone/getLocationByPhoneNum";
		// 请求方法设置
		String requestMethod = "POST";
		// 请求头部设置
		Map<String, String> headers = new HashMap<String, String>();
		// 请求参数设置
		Map<String, String> params = new HashMap<String, String>();
		params.put("apiKey", "IXuEAVG761353c0c8b926afff752c048fcaab888c9827e4");
		params.put("phoneNum", "18088822736");
		
		ResponseEntity<String> responseEntity = proxyToDesURL(requestMethod, url, headers, params);
		System.out.println("响应状态码是：" + responseEntity.getStatusCodeValue());		
		String result = responseEntity.getBody();
		if (result != null) {
			JSONObject jsonObject = JSONObject.parseObject(result);
			String status_code = jsonObject.getString("statusCode");
			if (status_code.equals("000000")) {
				// 状态码为000000, 说明请求成功
				System.out.println("请求成功：" + jsonObject.getString("result"));
			} else {
				// 状态码非000000, 说明请求失败
				System.out.println("请求失败：" + jsonObject.getString("desc"));
			}
		} else {
			// 返回内容异常，发送请求失败，以下可根据业务逻辑自行修改
			System.out.println("发送请求失败");
		}
	}
}
