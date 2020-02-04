package com.agileach.httpclient.util;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Properties;

import org.testng.annotations.DataProvider;

public class TestAPI {
	public Properties prop;
	public String excelPath;
	public String host;
	//protected Object[][] excelData;
	// 构造函数
	public TestAPI() {
		try {
			// 数据流的形式读取配置文件
			prop = new Properties();
			FileInputStream fis = new FileInputStream(
					System.getProperty("user.dir") + "/src/main/resources/config.properties");
			prop.load(fis);
		} catch (Exception e) {
			e.printStackTrace();
		}
		host = prop.getProperty("Host");
		excelPath = prop.getProperty("testData");
	}	
}