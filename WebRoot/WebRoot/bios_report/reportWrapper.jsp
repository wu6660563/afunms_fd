<%@page language="java" contentType="text/html;charset=gb2312" %>

<!--  bi 杰报表页面标记库   -->
<%@ taglib prefix="bios" uri="http://www.bijetsoft.com/BiosReportTags" %>

<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map" %>
<%@page import="java.util.HashMap" %>
<%@page import="java.util.Set" %>
<%@page import="java.util.Iterator" %>

<%
  String rootPath = request.getContextPath();
 // String menuTable = (String)request.getAttribute("menuTable");
  
  String starttime = (String)request.getAttribute("starttime");
  String endtime = (String)request.getAttribute("endtime");

  String rpt= (String)request.getParameter("rpt");
  //	读取报表相关参数
	Map paramMap = new HashMap();
	paramMap = request.getParameterMap();
	Set<String> key = paramMap.keySet();
	String sql = " ";
	String value = "";
    for (Iterator it = key.iterator(); it.hasNext();) {
            String s = (String) it.next();
            if(s == "rpt"){
            	continue;
            }
			value = request.getParameter(s);
			sql += s +"="+value+";";
	}
	
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">


</head>
<body>

<bios:report rpt="<%=rpt %>" params="<%=sql %>" fitwidth="true" rptheight="1000"/>
</body>


</HTML>