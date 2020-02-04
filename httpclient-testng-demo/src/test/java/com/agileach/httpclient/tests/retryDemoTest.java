package com.agileach.httpclient.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

public class retryDemoTest {
	@Test
	public void test1()
	{
		Assert.assertEquals(1, 1,">>>>>>>>>> test1");
	}
	@Test
	public void test2()
	{
		Assert.assertEquals(6, 6,">>>>>>>>>> test2");
	}
	@Test
	public void test3()
	{
		Assert.assertEquals(1, 1,">>>>>>>>>> test3");
	}
	
	@Test
	public void test4()
	{
		System.out.println("******************* 测试重跑是否实现 ***********************");
		Assert.assertEquals(1, 2,">>>>>>>>>> test4");
	}
	
	@Test
	public void test5()
	{
		Assert.assertEquals(1, 1,">>>>>>>>>> test5");
	}

}
