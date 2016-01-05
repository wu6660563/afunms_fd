<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.topology.util.GridDS"%>
<% 
String id = "";
int pid;
request.setCharacterEncoding("gb2312");// 解决中文出现乱码的问题
response.setCharacterEncoding("gb2312");
if (request.getAttribute("DID")!=null) 
{ 
    id = request.getAttribute("DID").toString(); 
}
pid=Integer.parseInt(id);
GridDS json=new GridDS(); 
String  gridjson="";
try {	
		gridjson = json.getNodeString(pid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println(gridjson);
%> 