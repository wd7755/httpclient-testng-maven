package com.agileach.httpclient.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.agileach.httpclient.util.*;

public class PostTest extends TestAPI {
	private String url = "https://api.apishop.net/communication/phone/getLocationByPhoneNum";
	private static ExecuteMethod em;

	@BeforeClass
	public void setUp() {
		em = new ExecuteMethod();
	}

	@Test(enabled = false)
	public void testPost() throws Exception {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("apiKey", "IXuEAVG761353c0c8b926afff752c048fcaab888c9827e4"));
		params.add(new BasicNameValuePair("phoneNum", "1861196136"));

		// 用哈希图准备请求头部信息
		HashMap<String, String> hashHead = new HashMap<String, String>();
		hashHead.put("Content-Type", "application/x-www-form-urlencoded");
		// 发送请求，获取反馈
		JSONObject jsonobj = em.sendPost(url, params, hashHead);
		int httpStatus = jsonobj.getInt(ExecuteMethod.HTTPSTATUS);
		if (httpStatus != HttpStatus.SC_OK) {
			Assert.fail("请求方式失败!状态码：" + httpStatus);
		}
		String expectedResult = "北京";
		JSONObject jo = jsonobj.getJSONObject("result");
		String city = JSONParser.getValue(jo, "city");
		Assert.assertEquals(expectedResult, city);

//		String count = OperateDB.getScalarValue(
//				"Select count(*) From member Where city=?", new String[] { city });
//		Assert.assertEquals("1", count);
	}

	private void doPost(String testname, String method, String address, String checkpoint, String expectedResult,
			String status, String keyvalue) throws Exception {
		url = host + address;
		// 用哈希图准备请求头部信息
		HashMap<String, String> hashHead = new HashMap<String, String>();
		hashHead.put("Content-Type", "application/x-www-form-urlencoded");

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// 取得POST提交的键值对数据
		JSONObject jo = new JSONObject("{" + keyvalue + "}");
		for (Iterator<String> iterator = jo.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			String value = String.valueOf(jo.get(key));
			params.add(new BasicNameValuePair(key, value));
		}
		// 发送请求，获取反馈
		JSONObject jsonobj = em.sendPost(url, params, hashHead);

		int httpStatus = jsonobj.getInt(ExecuteMethod.HTTPSTATUS);
		if (httpStatus != Integer.valueOf(status).intValue()) {
			Assert.fail("请求方式失败!状态码：" + httpStatus);
		}
		try {
			jo = jsonobj.getJSONObject("result");
		} catch (JSONException e) {
			Assert.fail("执行失败!" + e.getMessage());
			return;
		}
		String city = JSONParser.getValue(jo, "city");
		Assert.assertEquals(expectedResult, city);
	}

	@Test(dataProvider = "testData", enabled = false)
	public void testPhoneMember(String testname, String method, String address, String checkpoint,
			String expectedResult, String status, String keyvalue) throws Exception {
		doPost(testname, method, address, checkpoint, expectedResult, status, keyvalue);
	}

	@DataProvider
	public Object[][] testData() {
		Object[][] testData = new Object[][] {
				{ "验证手机号码归属地获取成功", "post", "communication/phone/getLocationByPhoneNum", "city", "北京", "200",
						"apiKey:IXuEAVG761353c0c8b926afff752c048fcaab888c9827e4,phoneNum:1861196136" },
				{ "验证apiKey为空归属地获取失败", "post", "communication/phone/getLocationByPhoneNum", "city", "北京", "200",
						"apiKey:1,phoneNum:18611961360" },
				{ "验证手机号码为空归属地获取失败", "post", "communication/phone/getLocationByPhoneNum", "city", "北京", "200",
						"apiKey:IXuEAVG761353c0c8b926afff752c048fcaab888c9827e4,phoneNum:1" },
				{ "验证手机号码格式错误归属地获取失败", "post", "communication/phone/getLocationByPhoneNum", "city", "北京", "200",
						"apiKey:IXuEAVG761353c0c8b926afff752c048fcaab888c9827e4,phoneNum:abcdefg" },
				{ "验证手机号码少于7位归属地获取失败", "post", "communication/phone/getLocationByPhoneNum", "city", "北京", "200",
						"apiKey:IXuEAVG761353c0c8b926afff752c048fcaab888c9827e4,phoneNum:186123" },
				{ "验证apiKey错误归属地获取失败", "post", "communication/phone/getLocationByPhoneNum", "city", "北京", "200",
						"apiKey:IXuEAVG761353777777fff752c048fcaab888c9827e4,phoneNum:186123" } };

		return testData;
	}

	@Test(dataProvider = "excelData")
	public void testPhoneMember2(String testname, String method, String address, String checkpoint,
			String expectedResult, String status, String keyvalue) throws Exception {
		doPost(testname, method, address, checkpoint, expectedResult, status, keyvalue);
	}

	@DataProvider
	public Object[][] excelData() throws IOException {
		Object[][] data = ExcelProcess.proessExcel(excelPath, 0);	
		return data;
	}
}