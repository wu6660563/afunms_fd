package com.afunms.application.wasmonitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.afunms.application.dao.WasConfigDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.util.SysLogger;


public class UrlConncetWas {
   public Hashtable ConncetWas(String ip,String port,String username,String password,String version){
	   Hashtable washst = new Hashtable();
	   if(!"".equalsIgnoreCase(username)){
			//�����perfServletApp�����˷���Ȩ�ޣ���Ҫ����Authenticator���������������Ҫ���û��������룬�����ȫ���û����ŷ���Ȩ�ޣ�����Ҫ�˲���
			Authenticator.setDefault(new MyAuthenticators(username,password));
		}
       StringBuffer sb = new StringBuffer();
       int responseTime = 0;//��������Ӧʱ��
       BufferedReader stdIn = null;
       InputStream input = null;
       boolean connectFlag = true;
       try {
		long timeB = System.currentTimeMillis();
		
		String urlstr = "http://"+ip+":"+port+"/wasPerfTool/servlet/perfservlet?refreshConfig=true";
		URL url = new URL(urlstr);
		
		HttpURLConnection urlCon = (HttpURLConnection)url.openConnection(); 
		
		urlCon.setConnectTimeout(30000);//���ó�ʱ
		
		// �򿪵��� URL �����Ӳ�����һ�����ڴӸ����Ӷ���� InputStream��
		//input = url.openStream();
		input = urlCon.getInputStream();
		
		stdIn = new BufferedReader(new InputStreamReader(input,"UTF-8"));

		long timeE = System.currentTimeMillis();
		responseTime = (int) (timeE - timeB);
		//System.out.println("���βɼ����ӷ�������Ӧʱ��Ϊ��" +responseTime+"����");
		
		String strLine;
		while ((strLine = stdIn.readLine()) != null) {
			if (strLine.indexOf("performancemonitor.dtd")!= -1)
		        		  continue;
			sb.append(strLine);
			
		}
		Document docRoot = getDocumentFromXML(sb.toString());
		if (version.indexOf("V5") != -1)
		{
			
			System.out.println("����V5�汾������");
			washst = getWebsphere5XML(ip,docRoot,version);
			return washst;
		}else if(version.indexOf("V6") != -1){
			System.out.println("����V6�汾������");
			washst = getWebsphere61XML(ip,docRoot,version);
			return washst;
		}else
		{
			
			System.out.println("��ʼ���WAS-7.0");
			washst = getWebsphere6XML(ip,docRoot,version);
			System.out.println("was-7 washst =====:"+washst.size());
			return washst;
		}
       }catch(Exception e){
    	   connectFlag = false;
       }finally {
			if (stdIn != null) {
				try {
					stdIn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if( input != null){
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
       
	return washst;
   }
   
   public Hashtable ConncetWas(String ip,String port,String username,String password,String version,Hashtable gatherhash){
	   Hashtable washst = new Hashtable();
	   if(!"".equalsIgnoreCase(username)){
			//�����perfServletApp�����˷���Ȩ�ޣ���Ҫ����Authenticator���������������Ҫ���û��������룬�����ȫ���û����ŷ���Ȩ�ޣ�����Ҫ�˲���
			Authenticator.setDefault(new MyAuthenticators(username,password));
		}
       StringBuffer sb = new StringBuffer();
       int responseTime = 0;//��������Ӧʱ��
       BufferedReader stdIn = null;
       InputStream input = null;
       boolean connectFlag = true;
       try {
			long timeB = System.currentTimeMillis();

			String urlstr = "http://" + ip + ":" + port
					+ "/wasPerfTool/servlet/perfservlet?refreshConfig=true";
			URL url = new URL(urlstr);

			HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();

			urlCon.setConnectTimeout(30000);// ���ó�ʱ

			// �򿪵��� URL �����Ӳ�����һ�����ڴӸ����Ӷ���� InputStream��
			// input = url.openStream();
			input = urlCon.getInputStream();

			stdIn = new BufferedReader(new InputStreamReader(input, "UTF-8"));

			long timeE = System.currentTimeMillis();
			responseTime = (int) (timeE - timeB);
			// System.out.println("���βɼ����ӷ�������Ӧʱ��Ϊ��" +responseTime+"����");

			String strLine;
			while ((strLine = stdIn.readLine()) != null) {
				if (strLine.indexOf("performancemonitor.dtd") != -1)
					continue;
				sb.append(strLine);
			}
			Document docRoot = getDocumentFromXML(sb.toString());

			if (version.indexOf("V5") != -1) {
				System.out.println("��ʼ���WAS-5");
				washst = getWebsphere5XML(ip, docRoot, version, gatherhash);
				return washst;
			} else {
				System.out.println("��ʼ���WAS-6 OR WAS-7");
				washst = getWebsphere6XML(ip, docRoot, version, gatherhash);
				return washst;
			}
		} catch (Exception e) {
			connectFlag = false;
		} finally {
			if (stdIn != null) {
				try {
					stdIn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if( input != null){
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return washst;
   }
   
   public void  parsexml(String ip,String xmlView,String version)
	{
		Document docRoot = getDocumentFromXML(xmlView);
		//1:����ȡ�ð汾��
		//����汾��5.x,�����5�汾��XML�ļ���������,���������ʱ����6�汾��XML�ļ���������

		if (version.indexOf("V5") != -1)
		{
			
			
			getWebsphere5XML(ip,docRoot,version);
			
		}
		else
		{
			
			//System.out.println("��ʼ���WAS-7.0");
			getWebsphere6XML(ip,docRoot,version);
		}
	}
   
   private String getResponseStatus(String xmlView,String version)
	{
		System.out.println("����getResponseStatus����");
		Document docRoot = getDocumentFromXML(xmlView);
		String responseStatus = "failed";
		String responseStatus_Temp = docRoot.getRootElement().attributeValue("responseStatus");
		if (null == responseStatus_Temp)
		{
			return "success";//���Ϊ�գ���5.1�汾��5.1�汾��û��������ԡ���ʱ�������������Ż�����ͨ���Ƿ���<Comments>��ǩ���ж�
		}
		
		if (responseStatus.equalsIgnoreCase(responseStatus_Temp))
		{
			return responseStatus;
		}
		else 
		{
			return "success";
		}
		
	}
   
   private Document getDocumentFromXML(String xmlView)
	{
		if (xmlView == null )
			return null;
		Document resultXMLDoc = null;
		SAXReader saxReader =  new SAXReader();
		try {
			resultXMLDoc = saxReader.read(new StringReader(xmlView));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return resultXMLDoc;
	}
   
   private Hashtable getWebsphere61XML(String ip,Document doc,String version){
	   Hashtable<String, Hashtable<?, ?>> was6hst = new Hashtable();
	   Hashtable jvm7hst = new Hashtable();
	   Hashtable jdbc7hst = new Hashtable();
	   Hashtable thread7hst = new Hashtable();
	   Hashtable servlet7hst = new Hashtable();
	   Hashtable system7hst = new Hashtable();
	   Hashtable trans7hst = new Hashtable();
	   Hashtable extension7hst = new Hashtable();
	   
		
		List<Node> listNodes = doc.selectNodes("//Node");
		
		if (listNodes == null || listNodes.size() == 0)
		{
			return null;
		}
		for (Node nodeTmp : listNodes) {
			String nodeName = nodeTmp.valueOf("@name");//��ȡ���ڵ������
			//1:��ȡnode
			
			//2:��ȡserver
			String xpathServer = "/PerformanceMonitor/"+"Node[@name='"+nodeName+"']/Server/following-sibling::*"+
								"|/PerformanceMonitor/"+"Node[@name='"+nodeName+"']/Server/self::*";
			
			List<Node> listServers = nodeTmp.getDocument().selectNodes(xpathServer);
			if (listServers == null || listServers.size() == 0)
			{
				//����õ���serverΪNull,���ߵõ��ĸ���Ϊ0,��Ҫֱ�ӷ���Null,����ѽڵ�����Ʒ��ع�ȥ�����Կ��Ƿ���null.
				return  null;
			}
			for(Node listServer : listServers)
			{
				String serverName = listServer.valueOf("@name");//�õ���serverName,�丸�ڵ�ΪNode;
				
				System.out.println("serverName----:"+serverName);
				
				//���кü������ָ������ͣ��˴���Ҫ��Ӽ�����go on......
				
				//3:��ȡCache
				
				Websphere6XMLParse xml6Parse = new Websphere6XMLParse();
//				xml6Parse.getCacheConfAndPerf(listServer,nodeName,serverName,version,
//						cacheList,cachePerfList,cacheInstanceList,cacheInstancePerfList);
//				//4:�õ�JVM������ָ��
				jvm7hst = xml6Parse.getJVMConfAndPerf(ip,listServer,nodeName,serverName,version);
//				5:�õ�webapplicationӦ�ã�����ָ�꣬ȡ����cache����		
//				xml6Parse.getWebapplicationConfAndPerf(listServer,nodeName,serverName,version,
//							webapplicationTotalList,webapplicationInstancelList,webapplicationTotalPerfList,
//							webapplicationInstancePerfList);
				//6���õ�JDBC���ӳأ�ȡ����Cache����
				jdbc7hst = xml6Parse.getJDBCConfAndPerf( ip,listServer, nodeName, serverName, version);
				//7:�õ�Thread���ӳأ�ȡ����cache����
				thread7hst = xml6Parse.getThreadConfAndPerf(ip, listServer, nodeName, serverName, version);
				//8:�õ�Servlet Session��ȡ��cache����
				servlet7hst = xml6Parse.getServletConfAndPerf(ip, listServer, nodeName, serverName, version);
				//9:�õ�System���ݣ���Ҫ��CPU���ݣ���JVMȡ��һ��
				system7hst = xml6Parse.getSystemDataConfAndPerf(ip, listServer, nodeName, serverName, version);
				//10:�õ�transcation���񣬺�JVMȡ��һ�£�����Ҫ��ȥ�ж���һ��
				trans7hst = xml6Parse.getTranscationConfAndPerf(ip, listServer, nodeName, serverName, version);
				//11:�õ���������ָ�꣬�磺������������������������������
				extension7hst = xml6Parse.getExtensionConfAndPerf(ip, listServer, nodeName, serverName, version);
				
			}
			was6hst.put("jvm7hst", jvm7hst);
			was6hst.put("jdbc7hst", jdbc7hst);
			was6hst.put("thread7hst", thread7hst);
			was6hst.put("servlet7hst", servlet7hst);
			was6hst.put("system7hst", system7hst);
			was6hst.put("trans7hst", trans7hst);
			was6hst.put("extension7hst", extension7hst);
		}
		return was6hst;
	   
   }
   
   private Hashtable getWebsphere5XML(String ip,Document doc,String version){
	    Hashtable was5hst = new Hashtable();
	    Hashtable cachehst = null;
	    Hashtable systemDatahst = null;
	    Hashtable servlethst = null;
	    Hashtable threadhst = null;
	    Hashtable transhst = null;
	    Hashtable jvmhst = null;
	    Hashtable jdbchst = null;
	    
	    List<Node> list = doc.selectNodes("//Node");

		if (list == null || list.size() == 0)
		return null;
		for (Node nodeTmp : list) {
			String nodeName = nodeTmp.valueOf("@name");//��ȡ���ڵ������
			
			//1:��ȡnode
			//2:��ȡserver
			String xpathServer = "/PerformanceMonitor/"+"Node[@name='"+nodeName+"']/Server/following-sibling::*"+
								"|/PerformanceMonitor/"+"Node[@name='"+nodeName+"']/Server/self::*";
			List<Node> listServers = nodeTmp.getDocument().selectNodes(xpathServer);
			if (listServers == null || listServers.size() == 0)
			{
				//����õ���serverΪNull,���ߵõ��ĸ���Ϊ0,��Ҫֱ�ӷ���Null,����ѽڵ�����Ʒ��ع�ȥ�����Կ��Ƿ���null.
				return null;
			}
			for(Node listServer : listServers)
			{
				String serverName = listServer.valueOf("@name");//�õ���serverName,�丸�ڵ�ΪNode;
				System.out.println("serverName.size()==================:"+serverName);

				Websphere5XMLParse webpshere5XML = new Websphere5XMLParse();
				systemDatahst = webpshere5XML.getSystemData5ConfAndPerf(ip,listServer, nodeName, serverName,  version);
				servlethst = webpshere5XML.getServlet5ConfAndPerf(ip,listServer, nodeName, serverName,  version);
				threadhst = webpshere5XML.getThread5ConfAndPerf(ip,listServer, nodeName, serverName, version);
				cachehst = webpshere5XML.getCache5ConfAndPerf(ip,listServer, nodeName, serverName, version);
				transhst = webpshere5XML.getTranscation5ConfAndPerf(ip,listServer, nodeName, serverName, version);
				jvmhst = webpshere5XML.getJVM5ConfAndPerf(ip,listServer, nodeName, serverName, version);
				jdbchst = webpshere5XML.getJDBC5ConfAndPerf(ip,listServer, nodeName, serverName, version);		
				
				}
			
			was5hst.put("cachehst", cachehst);
			was5hst.put("systemDatahst", systemDatahst);
			was5hst.put("servlethst", servlethst);
			was5hst.put("threadhst", threadhst);
			was5hst.put("transhst", transhst);
			was5hst.put("jvmhst", jvmhst);
			was5hst.put("jdbchst", jdbchst);
			}
		
		return was5hst;
		
	}
   
   private Hashtable getWebsphere5XML(String ip,Document doc,String version,Hashtable gatherhash){
	    Hashtable was5hst = new Hashtable();
	    Hashtable cachehst = null;
	    Hashtable systemDatahst = null;
	    Hashtable servlethst = null;
	    Hashtable threadhst = null;
	    Hashtable transhst = null;
	    Hashtable jvmhst = null;
	    Hashtable jdbchst = null;
	    
	    List<Node> list = doc.selectNodes("//Node");

		if (list == null || list.size() == 0)
		return null;
		for (Node nodeTmp : list) {
			String nodeName = nodeTmp.valueOf("@name");//��ȡ���ڵ������
			
			//1:��ȡnode
			//2:��ȡserver
			String xpathServer = "/PerformanceMonitor/"+"Node[@name='"+nodeName+"']/Server/following-sibling::*"+
								"|/PerformanceMonitor/"+"Node[@name='"+nodeName+"']/Server/self::*";
			List<Node> listServers = nodeTmp.getDocument().selectNodes(xpathServer);
			if (listServers == null || listServers.size() == 0)
			{
				//����õ���serverΪNull,���ߵõ��ĸ���Ϊ0,��Ҫֱ�ӷ���Null,����ѽڵ�����Ʒ��ع�ȥ�����Կ��Ƿ���null.
				return null;
			}
			for(Node listServer : listServers)
			{
				String serverName = listServer.valueOf("@name");//�õ���serverName,�丸�ڵ�ΪNode;
				Websphere5XMLParse webpshere5XML = new Websphere5XMLParse();
				if(gatherhash.containsKey("system")){
					systemDatahst = webpshere5XML.getSystemData5ConfAndPerf(ip,listServer, nodeName, serverName,  version);
				}
				if(gatherhash.containsKey("jdbc")){
					jdbchst = webpshere5XML.getJDBC5ConfAndPerf(ip,listServer, nodeName, serverName, version);
				}
				if(gatherhash.containsKey("session")){
					servlethst = webpshere5XML.getServlet5ConfAndPerf(ip,listServer, nodeName, serverName,  version);
				}
				if(gatherhash.containsKey("jvm")){
					jvmhst = webpshere5XML.getJVM5ConfAndPerf(ip,listServer, nodeName, serverName, version);
				}
				if(gatherhash.containsKey("cache")){
					cachehst = webpshere5XML.getCache5ConfAndPerf(ip,listServer, nodeName, serverName, version);
				}
				if(gatherhash.containsKey("thread")){
					threadhst = webpshere5XML.getThread5ConfAndPerf(ip,listServer, nodeName, serverName, version);
				}
				if(gatherhash.containsKey("orb")){
					transhst = webpshere5XML.getTranscation5ConfAndPerf(ip,listServer, nodeName, serverName, version);
				}
			}
			
			was5hst.put("cachehst", cachehst);
			was5hst.put("systemDatahst", systemDatahst);
			was5hst.put("servlethst", servlethst);
			was5hst.put("threadhst", threadhst);
			was5hst.put("transhst", transhst);
			was5hst.put("jvmhst", jvmhst);
			was5hst.put("jdbchst", jdbchst);
			}
		return was5hst;
		
	}
   
   
   
   private Hashtable getWebsphere6XML(String ip,Document doc,String version)
	{
	   Hashtable<String, Hashtable<?, ?>> was7hst = new Hashtable();
	   Hashtable jvm7hst = new Hashtable();
	   Hashtable jdbc7hst = new Hashtable();
	   Hashtable thread7hst = new Hashtable();
	   Hashtable servlet7hst = new Hashtable();
	   Hashtable system7hst = new Hashtable();
	   Hashtable trans7hst = new Hashtable();
	   Hashtable extension7hst = new Hashtable();
	   
		
		List<Node> listNodes = doc.selectNodes("//Node");
		
		if (listNodes == null || listNodes.size() == 0)
		{
			return null;
		}
		for (Node nodeTmp : listNodes) {
			String nodeName = nodeTmp.valueOf("@name");//��ȡ���ڵ������
			//1:��ȡnode
			
			//2:��ȡserver
			String xpathServer = "/PerformanceMonitor/"+"Node[@name='"+nodeName+"']/Server/following-sibling::*"+
								"|/PerformanceMonitor/"+"Node[@name='"+nodeName+"']/Server/self::*";
			
			List<Node> listServers = nodeTmp.getDocument().selectNodes(xpathServer);
			if (listServers == null || listServers.size() == 0)
			{
				//����õ���serverΪNull,���ߵõ��ĸ���Ϊ0,��Ҫֱ�ӷ���Null,����ѽڵ�����Ʒ��ع�ȥ�����Կ��Ƿ���null.
				return  null;
			}
			for(Node listServer : listServers)
			{
				String serverName = listServer.valueOf("@name");//�õ���serverName,�丸�ڵ�ΪNode;
				
				System.out.println("serverName----:"+serverName);
				
				//���кü������ָ������ͣ��˴���Ҫ��Ӽ�����go on......
				
				//3:��ȡCache
				
				Websphere6XMLParse xml6Parse = new Websphere6XMLParse();
//				xml6Parse.getCacheConfAndPerf(listServer,nodeName,serverName,version,
//						cacheList,cachePerfList,cacheInstanceList,cacheInstancePerfList);
//				//4:�õ�JVM������ָ��
				jvm7hst = xml6Parse.getJVMConfAndPerf(ip,listServer,nodeName,serverName,version);
//				5:�õ�webapplicationӦ�ã�����ָ�꣬ȡ����cache����		
//				xml6Parse.getWebapplicationConfAndPerf(listServer,nodeName,serverName,version,
//							webapplicationTotalList,webapplicationInstancelList,webapplicationTotalPerfList,
//							webapplicationInstancePerfList);
				//6���õ�JDBC���ӳأ�ȡ����Cache����
				jdbc7hst = xml6Parse.getJDBCConfAndPerf(ip, listServer, nodeName, serverName, version);
				//7:�õ�Thread���ӳأ�ȡ����cache����
				thread7hst = xml6Parse.getThreadConfAndPerf(ip, listServer, nodeName, serverName, version);
				//8:�õ�Servlet Session��ȡ��cache����
				servlet7hst = xml6Parse.getServletConfAndPerf(ip, listServer, nodeName, serverName, version);
				//9:�õ�System���ݣ���Ҫ��CPU���ݣ���JVMȡ��һ��
				system7hst = xml6Parse.getSystemDataConfAndPerf(ip, listServer, nodeName, serverName, version);
				//10:�õ�transcation���񣬺�JVMȡ��һ�£�����Ҫ��ȥ�ж���һ��
				trans7hst = xml6Parse.getTranscationConfAndPerf(ip, listServer, nodeName, serverName, version);
				//11:�õ���������ָ�꣬�磺������������������������������
				extension7hst = xml6Parse.getExtensionConfAndPerf(ip, listServer, nodeName, serverName, version);
				
			}
			   was7hst.put("jvm7hst", jvm7hst);
			   was7hst.put("jdbc7hst", jdbc7hst);
			   was7hst.put("thread7hst", thread7hst);
			   was7hst.put("servlet7hst", servlet7hst);
			   was7hst.put("system7hst", system7hst);
			   was7hst.put("trans7hst", trans7hst);
			   was7hst.put("extension7hst", extension7hst);
		}
		return was7hst;
	}
   
   private Hashtable getWebsphere6XML(String ip,Document doc,String version,Hashtable gatherhash)
	{
	   Hashtable<String, Hashtable<?, ?>> was7hst = new Hashtable();
	   Hashtable jvm7hst = new Hashtable();
	   Hashtable jdbc7hst = new Hashtable();
	   Hashtable thread7hst = new Hashtable();
	   Hashtable servlet7hst = new Hashtable();
	   Hashtable system7hst = new Hashtable();
	   Hashtable trans7hst = new Hashtable();
	   Hashtable extension7hst = new Hashtable();
		
		
		List<Node> listNodes = doc.selectNodes("//Node");
		
		if (listNodes == null || listNodes.size() == 0)
		{
			return null;
		}
		for (Node nodeTmp : listNodes) {
			String nodeName = nodeTmp.valueOf("@name");//��ȡ���ڵ������
			//1:��ȡnode
			
			//2:��ȡserver
			String xpathServer = "/PerformanceMonitor/"+"Node[@name='"+nodeName+"']/Server/following-sibling::*"+
								"|/PerformanceMonitor/"+"Node[@name='"+nodeName+"']/Server/self::*";
			
			List<Node> listServers = nodeTmp.getDocument().selectNodes(xpathServer);
			if (listServers == null || listServers.size() == 0)
			{
				//����õ���serverΪNull,���ߵõ��ĸ���Ϊ0,��Ҫֱ�ӷ���Null,����ѽڵ�����Ʒ��ع�ȥ�����Կ��Ƿ���null.
				return  null;
			}
			for(Node listServer : listServers)
			{
				String serverName = listServer.valueOf("@name");//�õ���serverName,�丸�ڵ�ΪNode;
				
				System.out.println("was7 serverName----:"+serverName);
				
				//���кü������ָ������ͣ��˴���Ҫ��Ӽ�����go on......
				
				//3:��ȡCache
				
				Websphere6XMLParse xml6Parse = new Websphere6XMLParse();
//				xml6Parse.getCacheConfAndPerf(listServer,nodeName,serverName,version,
//						cacheList,cachePerfList,cacheInstanceList,cacheInstancePerfList);
//				//4:�õ�JVM������ָ��
				if(gatherhash.containsKey("jvm"))
					jvm7hst = xml6Parse.getJVMConfAndPerf(ip,listServer,nodeName,serverName,version);
//				5:�õ�webapplicationӦ�ã�����ָ�꣬ȡ����cache����		
//				xml6Parse.getWebapplicationConfAndPerf(listServer,nodeName,serverName,version,
//							webapplicationTotalList,webapplicationInstancelList,webapplicationTotalPerfList,
//							webapplicationInstancePerfList);
				//6���õ�JDBC���ӳأ�ȡ����Cache����
				if(gatherhash.containsKey("jdbc"))
					jdbc7hst = xml6Parse.getJDBCConfAndPerf(ip, listServer, nodeName, serverName, version);
				//7:�õ�Thread���ӳأ�ȡ����cache����
				if(gatherhash.containsKey("thread"))
					thread7hst = xml6Parse.getThreadConfAndPerf(ip, listServer, nodeName, serverName, version);
				//8:�õ�Servlet Session��ȡ��cache����
				if(gatherhash.containsKey("session"))
					servlet7hst = xml6Parse.getServletConfAndPerf(ip, listServer, nodeName, serverName, version);
				//9:�õ�System���ݣ���Ҫ��CPU���ݣ���JVMȡ��һ��
				if(gatherhash.containsKey("system"))
					system7hst = xml6Parse.getSystemDataConfAndPerf(ip, listServer, nodeName, serverName, version);
				//10:�õ�transcation���񣬺�JVMȡ��һ�£�����Ҫ��ȥ�ж���һ��
				if(gatherhash.containsKey("orb"))
					trans7hst = xml6Parse.getTranscationConfAndPerf(ip, listServer, nodeName, serverName, version);
				//11:�õ���������ָ�꣬�磺������������������������������
				if(gatherhash.containsKey("cache"))
				
					extension7hst = xml6Parse.getExtensionConfAndPerf(ip, listServer, nodeName, serverName, version);
				
			}
			   was7hst.put("jvm7hst", jvm7hst);
			   was7hst.put("jdbc7hst", jdbc7hst);
			   was7hst.put("thread7hst", thread7hst);
			   was7hst.put("servlet7hst", servlet7hst);
			   was7hst.put("system7hst", system7hst);
			   was7hst.put("trans7hst", trans7hst);
			   was7hst.put("extension7hst", extension7hst);
		}
		return was7hst;
	}
   
   public boolean connectWasIsOK(String ip,int port){
	    String urlstr = "http://"+ip+":"+port+"/wasPerfTool/servlet/perfservlet?refreshConfig=true";
		
		try
		{
			URL url = new URL(urlstr);
			HttpURLConnection urlCon = (HttpURLConnection)url.openConnection(); 
			urlCon.setConnectTimeout(30000);
			urlCon.getInputStream();
			urlCon.disconnect();
	       return true;	  
		} catch (Exception e) {	
			e.printStackTrace();
			return false;
		}
		
	}
   
   
   public static void main(String args[]){
//	   UrlConncetWas ucw = new UrlConncetWas();
//	  // String xmlview = ucw.ConncetWas("10.10.151.166","9080","","","");
//	   ucw.ConncetWas("10.10.151.166","9080","","","V5");
	  
//		   WasConfigDao wasconf = new WasConfigDao();
//		   Was5system wassys = new Was5system();
//		   wassys = (Was5system) wasconf.findWasInfo("wassystem10101571");
//		   System.out.println("===============getFreeMemory=======================:"+wassys.getFreeMemory());
	
	
   }
}

