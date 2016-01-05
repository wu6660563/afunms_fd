package com.afunms.common.util;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.text.*;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;

import com.afunms.discovery.DiscoverResource;
import com.afunms.sysset.model.Service;


public class SocketService {
	/**
	*���캯��,��ʼ��һ������
	*/
	public SocketService(){}
    
    /**
     * �������еķ��� 
     */
     public static boolean checkService(String ipAddress,int port,int timeout) 
     {	   
         boolean result = false;
            
         Socket socket = new Socket();          
  	     try{
                InetAddress addr = InetAddress.getByName(ipAddress);
                SocketAddress sockaddr = new InetSocketAddress(addr,port);                                  
                socket.connect(sockaddr, timeout);
      	      	result = true; 
  	      }catch(SocketTimeoutException ste){	 		  
  		  }catch(IOException ioe){	 			      
  		  }finally{
     	    	  try
     	    	  {
     	    	     socket.close();
     	    	  }
     	    	  catch(IOException ioe){}
     	  }	       	       
     	  return result;
     }

}