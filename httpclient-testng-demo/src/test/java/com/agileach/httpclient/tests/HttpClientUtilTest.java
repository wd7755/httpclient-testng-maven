package com.agileach.httpclient.tests;

import java.util.HashMap;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.*;
import com.agileach.httpclient.util.HttpClientUtil;
import com.alibaba.fastjson.JSONObject;

public class HttpClientUtilTest {
	// 请求地址设置
	private String url = "https://api.apishop.net/communication/phone/getLocationByPhoneNum";
	@Test
	public void testGetMethod() {
		url = url.concat("?apiKey=IXuEAVG761353c0c8b926afff752c048fcaab888c9827e4&phoneNum=18666956958");
		// 请求方法设置
		String requestMethod = "get";
		// 请求头部设置
		Map<String, String> headers = new HashMap<String, String>();
		// 请求参数设置
		Map<String, String> params = new HashMap<String, String>();
		String result = HttpClientUtil.proxyToDesURL(requestMethod, url, headers, params);
		if (result != null) {
			JSONObject jsonObject = JSONObject.parseObject(result);	
			String status_code = jsonObject.getString("statusCode");
			if (status_code.equals("000000")) {
				// 状态码为000000, 说明请求成功
				Assert.assertEquals(status_code, "000000", "请求成功！");
				String city = "珠海";		
				JSONObject jo = jsonObject.getJSONObject("result");				
				Assert.assertEquals(city, jo.getString("city"));
			} else {
				// 状态码非000000, 说明请求失败
				Assert.fail("测试不通过：状态码是：" + status_code + ",");
			}
		} else {
			// 返回内容异常，发送请求失败，以下可根据业务逻辑自行修改
			Assert.fail("发送请求失败！");
		}
	}
	@Test//(expectedExceptions = AssertionError.class)
	public void testGetMethodWrong() {
		url = url.concat("?apiKey=IXuEAVG761353c0c8b926afff75******b888c9827e4&phoneNum=18666956958");
		// 请求方法设置
		String requestMethod = "get";
		// 请求头部设置
		Map<String, String> headers = new HashMap<String, String>();
		// 请求参数设置
		Map<String, String> params = new HashMap<String, String>();
		String result = HttpClientUtil.proxyToDesURL(requestMethod, url, headers, params);
		if (result != null) {
			JSONObject jsonObject = JSONObject.parseObject(result);	
			String status_code = jsonObject.getString("statusCode");
			if (status_code.equals("000000")) {
				// 状态码为000000, 说明请求成功
				Assert.assertEquals(status_code, "000000", "请求成功！");
				String city = "珠海";		
				JSONObject jo = jsonObject.getJSONObject("result");				
				Assert.assertEquals(city, jo.getString("city"));
			} else {
				// 状态码非000000, 说明请求失败
				Assert.fail("测试不通过：状态码是：" + status_code);
			}
		} else {
			// 返回内容异常，发送请求失败，以下可根据业务逻辑自行修改
			Assert.fail("发送请求失败！");
		}
	}

	@Test
	public void testPostMethod() {
		// 请求方法设置
		String requestMethod = "post";
		// 请求头部设置
		Map<String, String> headers = new HashMap<String, String>();
		// 请求参数设置
		Map<String, String> params = new HashMap<String, String>();
		params.put("apiKey", "IXuEAVG761353c0c8b926afff752c048fcaab888c9827e4");
		params.put("phoneNum", "18088822736");
		String result = HttpClientUtil.proxyToDesURL(requestMethod, url, headers, params);
		if (result != null) {
			JSONObject jsonObject = JSONObject.parseObject(result);	
			String status_code = jsonObject.getString("statusCode");
			if (status_code.equals("000000")) {
				// 状态码为000000, 说明请求成功
				Assert.assertEquals(status_code, "000000", "请求成功！");
				String city = "珠海";		
				JSONObject jo = jsonObject.getJSONObject("result");				
				Assert.assertEquals(city, jo.getString("city"));
			} else {
				// 状态码非000000, 说明请求失败
				Assert.fail("测试不通过：状态码是：" + status_code + ",");
			}
		} else {
			// 返回内容异常，发送请求失败，以下可根据业务逻辑自行修改
			Assert.fail("发送请求失败！");
		}
	}
	
	@Test(expectedExceptions = AssertionError.class)
	public void testPostMethodWrong() {
		// 请求方法设置
		String requestMethod = "post";
		// 请求头部设置
		Map<String, String> headers = new HashMap<String, String>();
		// 请求参数设置
		Map<String, String> params = new HashMap<String, String>();
		params.put("apiKey", "IXuEAVG761353c0c8b926afff752c048fcaab888c9827e4");
		params.put("phoneNum", "abcedfg");
		String result = HttpClientUtil.proxyToDesURL(requestMethod, url, headers, params);
		if (result != null) {
			JSONObject jsonObject = JSONObject.parseObject(result);	
			String status_code = jsonObject.getString("statusCode");
			if (status_code.equals("000000")) {
				// 状态码为000000, 说明请求成功
				Assert.assertEquals(status_code, "000000", "请求成功！");
				String city = "珠海";		
				JSONObject jo = jsonObject.getJSONObject("result");				
				Assert.assertEquals(city, jo.getString("city"));
			} else {
				// 状态码非000000, 说明请求失败
				Assert.fail("测试不通过：状态码是：" + status_code + ",");
			}
		} else {
			// 返回内容异常，发送请求失败，以下可根据业务逻辑自行修改
			Assert.fail("发送请求失败！");
		}
	}
	
}
