<%@page language="java" contentType="text/html;charset=gb2312"%>
<% 
 	String wholeStr=request.getParameter("wholeStr");
	String abbStr="";//用于显示设备信息简称
	if(wholeStr!=""&&wholeStr!=null){
		if(wholeStr.length()>40){
			abbStr=wholeStr.substring(0,40)+"...";
	 	} else{
			abbStr=wholeStr;
		}
	}
%> 
 <acronym title="<%=wholeStr%>"><%=abbStr%></acronym>