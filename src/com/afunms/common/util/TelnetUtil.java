package com.afunms.common.util;

/*
 * �����ṩ����telnet�Ƿ����ӳɹ�
 * 
 * ���ܳɹ����� ���� 0 , ���򷵻� -1
 * 
 * ����������������� 
 * 		connect(String ip)   
 *    		�������� ip �� Ĭ�ϵĶ˿� 23 �Լ� Ĭ�ϵĳ�ʱʱ�� 3000 
 *    		�����Ƿ������ӳɹ�
 *    
 *    	connect(String ip , int timeout)   
 *    		�������� ip �� Ĭ�ϵĶ˿� 23 �Լ� ��ʱʱ�� timeout 
 *    		�����Ƿ������ӳɹ�
 * 		
 * 		connect(String ip , int port , int timeout)   
 *    		�������� ip �� �˿� port �Լ� ��ʱʱ�� timeout 
 *    		�����Ƿ������ӳɹ�
 */

import java.io.IOException;
import java.util.ArrayList;

import cn.org.xone.telnet.TelnetWrapper;

/**
 * 
 * This class is used to test whether it can successfully telnet to connect
 * 
 * @author nielin
 * @create on 2010-02-26
 *
 */
public class TelnetUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TelnetUtil telnetTest = new TelnetUtil();
		
		System.out.println(telnetTest.connect("10.10.1.1"));
	}
	
	/**
	 * 
	 * According to the host ip, the default port 23, the default timeout 3000 
	 * Test whether a successful connection
	 * 
	 * @param ip
	 *			the host ip
	 * @return
	 * 			If successful to return 0, failure to return -1
	 */
	public int connect(String ip){
		return connect(ip , 23 , 3000);
	}
	
	/**
	 * 
	 * According to the host ip, the default port 23, the connect timeout 
	 * Test whether a successful connection
	 * 
	 * @param ip
	 * 			the host ip
	 * @param timeout
	 * 			the connect timeout 
	 * @return
	 * 			If successful to return 0, failure to return -1
	 */
	public int connect(String ip , int timeout){
		return connect(ip , 23 , timeout);
	}
	
	/**
	 * 
	 * According to the host ip, the host port, the connect timeout 
	 * Test whether a successful connection
	 * 
	 * @param ip
	 * 			the host ip
	 * @param port
	 * 			the host port
	 * @param timeout
	 * 			the connect timeout 
	 * @return
	 * 			If successful to return 0, failure to return -1
	 */
	public int connect(String ip , int port , int timeout){
		TelnetWrapper telnet = new TelnetWrapper();
		try{
			telnet.connect(ip, port , timeout);
		}catch(Exception e){
			return -1;
		}finally{
			try {
				telnet.disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
	}

}
