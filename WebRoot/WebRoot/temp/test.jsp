<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.ipresource.util.CollectIpMacDetail"%>
<%@page import="com.afunms.ipresource.util.CollectFdbDetail"%>
<% 
  CollectIpMacDetail task = new CollectIpMacDetail();
  task.collect();			
  
  //CollectFdbDetail cfd = new CollectFdbDetail();
//	cfd.collect();       	

%>