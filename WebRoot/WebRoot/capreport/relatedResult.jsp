<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
Boolean flag = (Boolean) request.getAttribute("flag");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'test.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

  </head>
  
  <body>
    <%if(flag) {
    	//�����ɹ���������ϵ����nms_alarm_device_dependence�����Ҫ�Լ��趨��׼�豸
    	//���ظù��ܣ�Ŀ�ķ�ֹʧ���޸Ļ�׼�豸
    	//����ͼ������ϣ�����֮�󣬸ù���������ʹ��
    	out.print("�����ɹ���");
    } %>
  </body>
</html>
