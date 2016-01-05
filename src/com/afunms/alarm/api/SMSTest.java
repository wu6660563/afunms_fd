/*
 * @(#)SMSTest.java     v1.01, Sep 2, 2014
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.alarm.api;

import java.io.IOException;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.PostMethod;

public class SMSTest {

	public static void test1() throws IOException {
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://gd.ums86.com:8899/sms/Api/Send.do");
		post.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=utf-8");// ��ͷ�ļ�������ת��
		String connect = "���齭ί��Ϣ���ġ�[��άƽ̨]�澯������{������ʪ�ȸ澯����}������ǰ�¶�{16}�棬������ֵ����ά�̣�{��������ɷݹ�˾}��ϵ�ˣ�{������}��ϵ�绰��{18802007610}";
		NameValuePair[] data = {new NameValuePair("SpCode", "211717"),
				new NameValuePair("LoginName", "admin"),
				new NameValuePair("Password", "Jszxmessage&7181"), 
				new NameValuePair("MessageContent", connect),
				new NameValuePair("UserNumber", "18802007610"),
				new NameValuePair("UserNumber", "99999999999999999999"),
				new NameValuePair("ScheduleTime", ""),
				new NameValuePair("ExtendAccessNum", ""),
				new NameValuePair("f", "1")};
		post.setRequestBody(data);

		client.executeMethod(post);
		Header[] headers = post.getResponseHeaders();
		for (Header h : headers) {
			System.out.println(h.toString());
		}
		String result = new String(post.getResponseBodyAsString().getBytes(
				"iso8859-1"));
		System.out.println("result:"+result);
		post.releaseConnection();
	}
	
	public static void main(String[] args) {
		try {
			test1();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
