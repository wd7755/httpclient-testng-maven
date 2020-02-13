package com.agileach.httpclient.util;

import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

public class TestBase {
	private Properties prop;
	public String host;
	public final Logger Log = LoggerFactory.getLogger(TestBase.class);
	//protected Object[][] excelData;
	// 构造函数
	public TestBase() {
		try {
			// 数据流的形式读取配置文件
			prop = new Properties();
			InputStream fis = this.getClass().getClassLoader().getResourceAsStream("config.properties");					
			prop.load(fis);
		} catch (Exception e) {
			e.printStackTrace();
		}
		host = prop.getProperty("Host");		
	}	
	
	@BeforeMethod
	public void beforeMethod() {
		Log.info("开始执行用例...");
	}
	
	@AfterMethod
	public void AfterMethod() {
		 Log.info("用例执行结束");
	}
}