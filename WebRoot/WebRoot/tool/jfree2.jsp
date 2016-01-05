<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.common.util.*"%>
<%@page import="com.afunms.discovery.*"%>
<%@page import="java.util.*"%>
<%
  List routerList = SnmpUtil.getInstance().getRouterList("192.168.14.2","public"); 
  for(int i=0;i<routerList.size();i++)
  {
	  IpRouter ipr = (IpRouter)routerList.get(i);
	  System.out.println(ipr.getDest() + "_" + ipr.getNextHop() + "_" + ipr.getType() + "_" + ipr.getProto());
  }	  
%>
