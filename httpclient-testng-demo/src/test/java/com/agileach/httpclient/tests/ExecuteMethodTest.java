package com.agileach.httpclient.tests;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.agileach.httpclient.util.ExcelProcess;
import com.agileach.httpclient.util.ExecuteMethod;
import com.agileach.httpclient.util.TestBase;
import com.agileach.httpclient.util.Users;
import com.agileach.httpclient.util.Util;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ExecuteMethodTest extends TestBase {
	protected Object[][] excelData;	
	private static ExecuteMethod em;
	@BeforeClass
	public void setUp() {
		em = new ExecuteMethod();
	}

	@Test
	public void testGet() throws Exception {
		String url = "https://api.apishop.net/communication/phone/getLocationByPhoneNum";
		url = url.concat("?apiKey=IXuEAVG761353c0c8b926afff752c048fcaab888c9827e4&phoneNum=1861195236");
		// 发送请求，获取反馈
		JSONObject jsonobj = em.sendGet(url);
		int httpStatus = jsonobj.getIntValue(em.HTTPSTATUS);
		if (httpStatus != HttpStatus.SC_OK) {
			Assert.fail("请求方式失败!状态码：" + httpStatus);
		}
		String expectedResult = "北京";	
		Assert.assertEquals(expectedResult, Util.getValueByJPath(jsonobj, "result/city"));
//		String count = OperateDB.getScalarValue(
//				"Select count(*) From member Where city=?", new String[] { city });
//		Assert.assertEquals("1", count);
	}

	@Test(enabled = true)
	public void testPostByForm() throws Exception {
		String url = "https://api.apishop.net/communication/phone/getLocationByPhoneNum";
		// 用哈希图准备参数信息
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("apiKey", "IXuEAVG761353c0c8b926afff752c048fcaab888c9827e4");
		params.put("phoneNum", "1861196136");
		// 用哈希图准备请求头部信息
		HashMap<String, String> hashHead = new HashMap<String, String>();
		hashHead.put("Content-Type", "application/x-www-form-urlencoded");
		// 发送请求，获取反馈
		JSONObject jsonobj = em.sendPostByForm(url, params, hashHead);
		int httpStatus = jsonobj.getIntValue(em.HTTPSTATUS);
		if (httpStatus != HttpStatus.SC_OK) {
			Assert.fail("请求方式失败!状态码：" + httpStatus);
		}
		String expectedResult = "北京";		
		Assert.assertEquals(expectedResult, Util.getValueByJPath(jsonobj, "result/city"));
//		String count = OperateDB.getScalarValue(
//				"Select count(*) From member Where city=?", new String[] { city });
//		Assert.assertEquals("1", count);
	}

	@Test(dataProvider = "testData", enabled = false)
	public void testPostDataFromArray(String testname, String method, String address, String checkpoint,
			String expectedResult, String status, String keyvalue) throws Exception {
		doPost(testname, method, address, checkpoint, expectedResult, status, keyvalue);
	}

	@Test(dataProvider = "excelData", enabled = true)
	public void testPostDataFromExcel(String testname, String method, String address, String checkpoint,
			String expectedResult, String status, String keyvalue) throws Exception {
		Log.info("数据驱动测试用例！");			
		doPost(testname, method, address, checkpoint, expectedResult, status, keyvalue);
	}

	@DataProvider
	public Object[][] testData() {
		Object[][] testData = new Object[][] {
				{ "验证手机号码归属地获取成功", "post", "communication/phone/getLocationByPhoneNum", "city", "北京", "200",
						"\"apiKey\":\"IXuEAVG761353c0c8b926afff752c048fcaab888c9827e4\",\"phoneNum\":1861196136" },
				{ "验证apiKey为空归属地获取失败", "post", "communication/phone/getLocationByPhoneNum", "city", "北京", "200",
						"\"apiKey\":1,\"phoneNum\":18611961360" },
				{ "验证手机号码为空归属地获取失败", "post", "communication/phone/getLocationByPhoneNum", "city", "北京", "200",
						"\"apiKey\":\"IXuEAVG761353c0c8b926afff752c048fcaab888c9827e4\",\"phoneNum\":1" },
				{ "验证手机号码格式错误归属地获取失败", "post", "communication/phone/getLocationByPhoneNum", "city", "北京", "200",
						"\"apiKey\":\"IXuEAVG761353c0c8b926afff752c048fcaab888c9827e4\",\"phoneNum\":\"abcdefg\"" },
				{ "验证手机号码少于7位归属地获取失败", "post", "communication/phone/getLocationByPhoneNum", "city", "北京", "200",
						"\"apiKey\":\"IXuEAVG761353c0c8b926afff752c048fcaab888c9827e4\",\"phoneNum\":186123" },
				{ "验证apiKey错误归属地获取失败", "post", "communication/phone/getLocationByPhoneNum", "city", "北京", "200",
						"\"apiKey\":\"IXuEAVG761353777777fff752c048fcaab888c9827e4\",\"phoneNum\":186123" } };

		return testData;
	}

	@DataProvider
	public Object[][] excelData() throws IOException {
		Object[][] data = ExcelProcess.proessExcelLessThan2010(excelPath, 0);
		Log.info("获取Excel中数据成功！");
		return data;
	}

	private void doPost(String testname, String method, String address, String checkpoint, String expectedResult,
			String status, String keyvalue) throws Exception {
		String url = "https://api.apishop.net/";
		url = url.concat(address);
		// 用哈希图准备请求头部信息
		HashMap<String, String> hashHead = new HashMap<String, String>();
		hashHead.put("Content-Type", "application/x-www-form-urlencoded");
		HashMap<String, String> params = new HashMap<String, String>();
		// 取得POST提交的键值对数据		
		String jsonString = "{" + keyvalue + "}";
		JSONObject jo = JSONObject.parseObject(jsonString);
		for (Iterator<String> iterator = jo.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			String value = String.valueOf(jo.get(key));
			params.put(key, value);
		}
		// 发送请求，获取反馈
		JSONObject jsonobj = em.sendPostByForm(url, params, hashHead);

		int httpStatus = jsonobj.getIntValue(em.HTTPSTATUS);
		if (httpStatus != Integer.valueOf(status).intValue()) {
			Assert.fail("请求方式失败!状态码：" + httpStatus);
		}			
		Assert.assertEquals(expectedResult, Util.getValueByJPath(jsonobj, "result/city"));
	}
	
	@Test
	public void testPostByJson() throws Exception {		
		String url = "https://reqres.in/api/users";		
		//准备请求头信息
		HashMap<String,String> headers = new HashMap<String,String>();
		headers.put("Content-Type", "application/json"); 		
		//对象转换成Json字符串
		Users user = new Users("Anthony","tester");
		String userJsonString = JSON.toJSONString(user);		
		//System.out.println(userJsonString);		
		// 发送请求，获取反馈
		JSONObject responseJson = em.sendPostByJson(url, userJsonString, headers);
		int httpStatus = responseJson.getIntValue(em.HTTPSTATUS);
		//验证状态码是不是201	
		if (httpStatus != 201) {
			Assert.fail("请求方式失败!状态码：" + HttpStatus.SC_CREATED);
		}			
		//System.out.println(responseString);
		String name = Util.getValueByJPath(responseJson, "name");
		String job = Util.getValueByJPath(responseJson, "job");
		Assert.assertEquals(name, "Anthony","name is not same");
		Assert.assertEquals(job, "tester","job is not same");		
	}		
	
	@Test
	public void putTest() throws ClientProtocolException, IOException{
		String url = "https://reqres.in/api/users/2";		
		HashMap<String,String> headers = new HashMap<String,String>();
		headers.put("Content-Type", "application/json"); //这个在postman中可以查询到		
		//对象转换成Json字符串
		Users user = new Users("Alex","automation tester");
		String userJsonString = JSON.toJSONString(user);			
		JSONObject responseJson = em.sendPut(url, userJsonString, headers);
		int httpStatus = responseJson.getIntValue(em.HTTPSTATUS);	
		Assert.assertEquals(httpStatus, HttpStatus.SC_OK,"response status code is not 200");		
		//验证名称为Alex的jon是不是 automation tester	
		String jobString = Util.getValueByJPath(responseJson, "job");				
		Assert.assertEquals(jobString, "automation tester","job is not same");
		Assert.assertEquals(Util.getValueByJPath(responseJson, "name"), "Alex","name is not same");
	} 
	
	@Test
	public void deleteTest() throws ClientProtocolException, IOException {		
		String url = "https://reqres.in/api/users/2";		
		int httpStatus = em.sendDelete(url);
		Log.info("测试响应状态码是否是204");			
		Assert.assertEquals(httpStatus, HttpStatus.SC_NO_CONTENT,"response status code is not 204");	
	}
}
