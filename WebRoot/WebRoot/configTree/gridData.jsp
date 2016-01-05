<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.topology.util.GridDS"%>
<% 
String start = request.getAttribute("start").toString();
String limit = request.getAttribute("limit").toString();
GridDS jsonTree=new GridDS(); 
request.setCharacterEncoding("gb2312");// 解决中文出现乱码的问题
response.setCharacterEncoding("gb2312");
String  gridjson="";
try {	
		int index = Integer.parseInt(start);
    	int pageSize = Integer.parseInt(limit);
		gridjson = jsonTree.getGridString(index,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println(gridjson);
%> 
