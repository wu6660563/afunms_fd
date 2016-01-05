<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.dhcnms.discovery.*"%>
<% 
   DiscoverDataHelperInterface hepler = new DiscoverDataHelper();
   hepler.DB2ServerXml();
%>

