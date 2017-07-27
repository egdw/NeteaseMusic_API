package com.test;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import com.utils.AES;

public class MainClass {
	public static void main(String[] args) throws UnsupportedEncodingException, Exception {
		/*
		 * 加密用的Key 可以用26个字母和数字组成，最好不要用保留字符，虽然不会错，至于怎么裁决，个人看情况而定
		 * 此处使用AES-128-CBC加密模式，key需要为16位。
		 */
		Scanner scanner = new Scanner(System.in);
		System.out.print("请输入要解析的音乐ID：");
		// 获取音乐id
		String id = scanner.next();
		// 获取音质
		System.out.println("请输入音质:0:128kps 1:192kps 2:320kps");
		int rate = scanner.nextInt();
		String rateParam = null;
		if (rate == 0) {
			rateParam = "128000";
		} else if (rate == 1) {
			rateParam = "192000";
		} else {
			rateParam = "320000";
		}
		String first_param = "{\"ids\":\"[" + id + "]\",\"br\":" + rateParam + ",\"csrf_token\":\"\"}";
		testRequest("params=" + URLEncoder.encode(AES.get_params(first_param), "UTF-8") + "&encSecKey="
				+ AES.get_encSecKey());
	}

	public static void testRequest(String param) throws Exception {
		URL url = new URL("http://music.163.com/weapi/song/enhance/player/url?csrf_token=");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Referer", "http://music.163.com/");
		conn.setRequestProperty("Host", "music.163.com");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Charset", "UTF-8");
		conn.setRequestProperty("Accept-Language", "q=0.8,zh-CN;q=0.6,zh;q=0.2");
		conn.setRequestProperty("Cookie", "os=uwp;");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.connect();
		// param = "params=" + param;
		DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
		outputStream.write(param.getBytes());
		outputStream.flush();
		outputStream.close();
		BufferedInputStream inputStream = new BufferedInputStream(conn.getInputStream());
		byte[] bytes = new byte[512];
		int len = -1;
		StringBuilder sb = new StringBuilder();
		while ((len = inputStream.read(bytes)) != -1) {
			sb.append(new String(bytes, 0, len));
		}
		inputStream.close();
		System.out.println(sb.toString());
	}
}
