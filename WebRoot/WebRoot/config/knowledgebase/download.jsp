<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@ page import="com.jspsmart.upload.*"%>
<%
String rootPath = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+rootPath+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>����</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
��	<script language="JavaScript"> 
/**
	*�Զ��ر�
	*/
	����function closeit() 
	����{ 
	����setTimeout("self.close()",0) //���� 
	����} 
����</script>
  </head>
  
  <body onload="closeit()">
    <%
    	String dname_get=request.getParameter("name");//����ļ���
    	String  dname = new String(dname_get.getBytes("ISO-8859-1"),"GBK");//��ֹ����ת��
    	String path=".."+request.getContextPath()+"/config/knowledgebase/attachfiles/"+dname;
    	SmartUpload su=new SmartUpload();
    	su.initialize(pageContext);//��ʼ��
    	su.setContentDisposition(null);//��������ļ�
    	su.downloadFile(path);//����·��
    	out.clear();//����
    	out=pageContext.pushBody();//ˢ��
     %>
  </body>
</html>
