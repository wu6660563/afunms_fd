package com.afunms.application.jbossmonitor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;


public class HttpClientJBoss {
	
//	���ConnectionManager��������ز���
	private static MultiThreadedHttpConnectionManager manager =
	new MultiThreadedHttpConnectionManager();

	private static int connectionTimeOut = 20000;
	private static int socketTimeOut = 10000;
	private static int maxConnectionPerHost = 5;
	private static int maxTotalConnections = 40;

//	��־��ʼ���Ƿ���ɵ�flag
	private static boolean initialed = false;

//	��ʼ��ConnectionManger�ķ���
	public static void SetPara() {
	manager.getParams().setConnectionTimeout(connectionTimeOut);

	manager.getParams().setSoTimeout(socketTimeOut);
	manager.getParams()
	.setDefaultMaxConnectionsPerHost(maxConnectionPerHost);

	manager.getParams().setMaxTotalConnections(maxTotalConnections);

	initialed = true;
	}

//	ͨ��get������ȡ��ҳ����
	public static String getGetResponseWithHttpClient(String url, String encode) {
	       
		   HttpClient client = new HttpClient(manager);

		   if (initialed) {
			   HttpClientJBoss.SetPara();
			}
	      GetMethod get = new GetMethod(url);
	          get.setFollowRedirects(true);

	      String result = null;

	      StringBuffer resultBuffer = new StringBuffer();

	      try {

	      client.executeMethod(get);

//	String strGetResponseBody = post.getResponseBodyAsString();
	     BufferedReader in = new BufferedReader(
	        new InputStreamReader(
	          get.getResponseBodyAsStream(),
	          get.getResponseCharSet()));

	     String inputLine = null;

	     while ((inputLine = in.readLine()) != null) {
	       resultBuffer.append(inputLine);
	       resultBuffer.append("\n");
	   }

	    in.close();

	    result = resultBuffer.toString();

	    result = HttpClientJBoss.ConverterStringCode(resultBuffer.toString(),
	     get.getResponseCharSet(),
	     encode);
	  } catch (Exception e) {
	    e.printStackTrace();

	     result = "";
	  } finally {
	   get.releaseConnection();

	    return result;
	   }
	}

	public static String getPostResponseWithHttpClient(String url,
	String encode) {
	   HttpClient client = new HttpClient(manager);

	   if (initialed) {

		   HttpClientJBoss.SetPara();
	 }

	  PostMethod post = new PostMethod(url);
	     post.setFollowRedirects(false);

	  StringBuffer resultBuffer = new StringBuffer();

	  String result = null;

	  try {
	    client.executeMethod(post);

	  BufferedReader in = new BufferedReader(
	      new InputStreamReader(
	    post.getResponseBodyAsStream(),
	    post.getResponseCharSet()));
	  String inputLine = null;

	  while ((inputLine = in.readLine()) != null) {
	     resultBuffer.append(inputLine);
	     resultBuffer.append("\n");
	 }

	 in.close();
	  result = HttpClientJBoss.ConverterStringCode(resultBuffer.toString(),
	     post.getResponseCharSet(),
	     encode);
	 } catch (Exception e) {
	  e.printStackTrace();

	   result = "";
	 } finally {
	  post.releaseConnection();

	 return result;
	   }
	}


	public static String getPostResponseWithHttpClient(String url,
	String encode,
	    NameValuePair[] nameValuePair) {
	    HttpClient client = new HttpClient(manager);

	    if (initialed) {
	    	HttpClientJBoss.SetPara();
	 }

	    PostMethod post = new PostMethod(url);

	      post.setRequestBody(nameValuePair);
	      post.setFollowRedirects(false);

	   String result = null;
	   StringBuffer resultBuffer = new StringBuffer();


	  try {
	       client.executeMethod(post);
	  BufferedReader in = new BufferedReader(
	       new InputStreamReader(
	      post.getResponseBodyAsStream(),
	      post.getResponseCharSet()));
	  String inputLine = null;

	 while ((inputLine = in.readLine()) != null) {
	      resultBuffer.append(inputLine);
	      resultBuffer.append("\n");
	 }

	   in.close();
	  result = HttpClientJBoss.ConverterStringCode(resultBuffer.toString(),
	     post.getResponseCharSet(),
	  encode);
	 } catch (Exception e) {
	  e.printStackTrace();

	  result = "";
	  } finally {
	     post.releaseConnection();


	  return result;
	   }
	}

	private static String ConverterStringCode(String source, String srcEncode, String destEncode) {
	     if (source != null) {
	     try {
	   return new String(source.getBytes(srcEncode), destEncode);
	     } catch (UnsupportedEncodingException e) {
	  e.printStackTrace();
	  return "";
	  }

	   } else {
	  return "";
	   }
	  }

}
