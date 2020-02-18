package com.agileach.httpclient.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyException extends Exception{		
	private static final long serialVersionUID = 1L;
	private final static Logger Log = LoggerFactory.getLogger(ExecuteMethod.class);		
	public MyException(Exception e) {
		e.printStackTrace();
		Log.error(e.getLocalizedMessage());		
	}
	
}
