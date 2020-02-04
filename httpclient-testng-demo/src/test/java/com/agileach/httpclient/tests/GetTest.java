package com.agileach.httpclient.tests;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.agileach.httpclient.util.*;

public class GetTest extends TestAPI{
	protected Object[][] excelData;
	private String url = "https://api.apishop.net/communication/phone/getLocationByPhoneNum?apiKey=IXuEAVG761353c0c8b926afff752c048fcaab888c9827e4&phoneNum=1861195236";

	private static ExecuteMethod em;
	@BeforeClass
	public void setUp() {
		em = new ExecuteMethod();
	}
	@Test
	public void testGet() throws Exception {
		// 发送请求，获取反馈
		JSONObject jsonobj = em.sendGet(url);
		int httpStatus = jsonobj.getInt(em.HTTPSTATUS);
		if (httpStatus != HttpStatus.SC_OK) {
			Assert.fail("请求方式失败!状态码：" + httpStatus);
		}
		String city = "北京";
		JSONObject jo = jsonobj.getJSONObject("result");
		JSONParser.getValue(jo, "city");
		Assert.assertEquals(city, JSONParser.getValue(jo, "city"));
//		String count = OperateDB.getScalarValue(
//				"Select count(*) From member Where city=?", new String[] { city });
//		Assert.assertEquals("1", count);
	}
}
