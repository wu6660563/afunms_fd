<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.config.model.*"%>
<%
String rootPath = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>�����Ķ�</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script src="<%=rootPath%>/config/knowledges/AC_PDF.js" language="javascript"></script>

	<script language="javascript" type="text/javascript">
        check = isAcrobatPluginInstall();
		if(!check)
		{
		    if(window.confirm("���Ļ�����û�а�װAcrobat Reader���Ƿ����ڰ�װ��")) 
		    {
				window.open( "http://www.adobe.com/cn/products/reader/")
			} 
			else
			{
			    alert("���Ļ���û�а�װ Acrobat Reader �� ���ܾͲ���������ʾ������ĵ�");
			}
		}
    </script>

  </head>
  
  <body>
  <%
  	String name=(String)request.getParameter("attachfiles");
	String  result1 = new String(name.getBytes("ISO-8859-1"),"GBK");
  	String category=(String)request.getParameter("category");
  	String  result2 = new String(category.getBytes("ISO-8859-1"),"GBK");
  	String entity=(String)request.getParameter("entity");
  	String  result3 = new String(entity.getBytes("ISO-8859-1"),"GBK");
  	String subentity=(String)request.getParameter("subentity");
  	String  result4 = new String(subentity.getBytes("ISO-8859-1"),"GBK");
  	String  result5 = new String(name.getBytes("ISO-8859-1"),"GBK");
  %>
    <h3><%=result4%>&nbsp;>>&nbsp;<%=result3%>&nbsp;>>&nbsp;<%=result2%>&nbsp;>>&nbsp;<%=result1%></h3>
    <hr/>
     <br>
     
     <script type="text/javascript">
        var ua = navigator.userAgent.toLowerCase();
        var isIE  = (ua.indexOf("msie") != -1) ? true : false;
        if(!isIE)
        {
            document.write("pdf���ݲ���������ʾ����Ҫ��IE6.0��6.0���ϵ������<br>");
        }
     </script>
        <embed width="800" height="580" src="<%=rootPath%>/config/knowledges/pdf/<%=result5%>"  vmmode="transparent"></embed>
         <br>
     
     <br>
  </body>
</html>
