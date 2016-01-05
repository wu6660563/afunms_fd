<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.topology.util.GridDS"%>
<% 
String PID = "";
int pid;
request.setCharacterEncoding("gb2312");// 解决中文出现乱码的问题
response.setCharacterEncoding("gb2312");
if (request.getAttribute("DID")!=null) 
{ 
    PID = request.getAttribute("DID").toString(); 
}
pid=Integer.parseInt(PID);
GridDS jsonTree=new GridDS(); 
String  treeJson="";
try {
			treeJson = jsonTree.getJSONString(pid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println(treeJson);
%> 
