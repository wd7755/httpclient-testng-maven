package com.agileach.httpclient.util;

import org.json.JSONObject;

public class JSONParser {
	private static String value = "";
	public static String getValue(JSONObject jo, String key) {		
		try {		
			// 再根据键名查找键值ֵ
			value = jo.getString(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
}
