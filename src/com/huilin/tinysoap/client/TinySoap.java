package com.huilin.tinysoap.client;

import java.io.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 最简单的基于java的soap方案，只需要指定uri地址，以及soap body的内容即可，可谓居家旅行 杀人灭口之必备良药 使用起来更加简单：
 * 
 * TinySoap.send(uri, soapbody);
 * 
 * @author 张晓辉
 * 
 */
public class TinySoap {
	private static final String SOAP_HEAD = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><soapenv:Body>";
	private static final String SOAP_FOOT = "</soapenv:Body></soapenv:Envelope>";
	private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	private static final int TIMEOUT = 1 * 60 * 1000;

	/**
	 * 发送soap内容到指定的uri
	 * 
	 * @param uri
	 * @param soapbody
	 * @return
	 * @throws IOException
	 */
	protected static String send(String uri, String soapbody, String soapAction) {

		HttpURLConnection connection = null;
		StringBuffer sb = new StringBuffer();
		OutputStream os = null;
		InputStream is = null;
		try {
			URL u = new URL(uri);
			  //sun.net.client.defaultConnectTimeout：连接主机的超时时间（单位：毫秒）
			System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
			  //sun.net.client.defaultReadTimeout：从主机读取数据的超时时间（单位：毫秒） 
			System.setProperty("sun.net.client.defaultReadTimeout", "30000"); 
           
			connection = (HttpURLConnection) u.openConnection();
			
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"text/xml; charset=UTF-8");
			connection.setRequestProperty("SOAPAction", soapAction);
		
			// 输入
			connection.setDoOutput(true);
			os = connection.getOutputStream();

			sb.append(XML_HEADER);
			sb.append(SOAP_HEAD);
			sb.append(soapbody);
			sb.append(SOAP_FOOT);
			os.write(sb.toString().getBytes("UTF-8"));
			os.flush();

			// 读取输出
			sb = new StringBuffer();
			is = connection.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(is));

			String line;
			while (in.ready()) {
				line = in.readLine();
				sb.append(line);
			}
			
		} catch (IOException e) {
			System.out.println("连接异常" + e);
			appendErrorStream(connection, sb);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeStream(is);
			closeStream(os);
			
			if(connection != null) {
				connection.disconnect();
			}
		}

		return sb.toString();
	}

	private static void closeStream(InputStream s) {
		if (s != null) {
			try {
				s.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void closeStream(OutputStream s) {
		if (s != null) {
			try {
				s.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void appendErrorStream(HttpURLConnection connection,
			StringBuffer sb) {
		InputStream is=null;
		try {
			 is = connection.getErrorStream();
			if(is == null) {
				return;
			}
			BufferedReader ei = new BufferedReader(new InputStreamReader(
					is));
			String line;

			while (ei.ready()) {
				line = ei.readLine();
				sb.append(line);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			closeStream(is);
			if(connection != null) {
				connection.disconnect();
			}
		}
	}

}
