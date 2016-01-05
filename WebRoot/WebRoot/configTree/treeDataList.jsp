<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.topology.util.GridDS"%>
<% 
GridDS jsonTree=new GridDS(); 
request.setCharacterEncoding("gb2312");// 解决中文出现乱码的问题
response.setCharacterEncoding("gb2312");
String  gridjson="";
try {	
		gridjson = jsonTree.getGridString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println(gridjson);
%> 