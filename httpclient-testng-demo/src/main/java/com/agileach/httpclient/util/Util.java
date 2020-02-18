package com.agileach.httpclient.util;

import java.util.Random;
import java.util.HashMap;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Util {
	// 1 json解析方法
	/**
	 *
	 * @param responseJson ,这个变量是拿到响应字符串通过json转换成json对象
	 * @param jpath,这个jpath指的是用户想要查询json对象的值的路径写法 jpath写法举例：
	 * 1) per_page
	 * 2)data[1]/first_name，data是一个json数组，[1]表示索引 
	 * /first_name表示data数组下某一个元素下的json对象的名称为first_name
	 * @return，返回first_name这个json对象名称对应的值
	 */
	public static String getValueByJPath(JSONObject responseJson, String jpath) {
		Object obj = responseJson;
		for (String s : jpath.split("/")) {
			if (!s.isEmpty()) {
				if (!(s.contains("[") || s.contains("]"))) {
					obj = ((JSONObject) obj).get(s);
				} else if (s.contains("[") || s.contains("]")) {
					obj = ((JSONArray) ((JSONObject) obj).get(s.split("\\[")[0]))
							.get(Integer.parseInt(s.split("\\[")[1].replaceAll("]", "")));
				}else if (s.contains("{") || s.contains("}")) {
					obj = ((JSONArray) ((JSONObject) obj).get(s.split("\\{")[0]))
							.get(Integer.parseInt(s.split("\\{")[1].replaceAll("}", "")));
				}
			}
		}
		return obj.toString();
	}
		
	// 获取返回的token ,使用JsonPath获取json路径
	public static HashMap<String, String> getToken(CloseableHttpResponse closeableHttpResponse, String jsonPath)
			throws Exception {
		HashMap<String, String> responseToken = new HashMap<String, String>();
		String responseString = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
		ReadContext ctx = JsonPath.parse(responseString);
		String Token = ctx.read(jsonPath); // "$.EFPV3AuthenticationResult.Token"
		if (null == Token || "".equals(Token)) {
			new Exception("token不存在");
		}

		responseToken.put("x-ba-token", Token);
		return responseToken;
	}

	// 获取状态码
	public static int getStatusCode(CloseableHttpResponse closeableHttpResponse) {
		int StatusCode = closeableHttpResponse.getStatusLine().getStatusCode();
		return StatusCode;
	}	

	/*
	 * 获取随机字符串
	 * 
	 * @length: 字符串的长度
	 */
	public static String getRandomString(int length) {
		// length表示生成字符串的长度
		String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		for (int i = 0; i < 3; i++) {
			System.out.println(Util.getRandomString(6));
		}
	}
}