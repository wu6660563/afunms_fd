<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.polling.base.Node"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="java.util.List"%>
<%
  String rootPath = request.getContextPath();
  List list = (List)request.getAttribute("list");  
  String address = (String)request.getAttribute("address"); 
  
  int perRowNum = 8; //每行的节点个数 
%>
<html>
<head>
<title>dhcnms</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
</head>
<BODY leftmargin="0" topmargin="0">
<br>
<form method="post" name="mainForm">
<table cellpadding="0" cellspacing="0" width=80% class="tborder" align='center'>
 <tr height="20"><td colspan='<%=perRowNum%>' background="<%=rootPath%>/resource/image/td-bg.jpg" align='center'> 
 <b><font color='white'><%=address%></b>&nbsp;&nbsp;(设备总数:<%=list.size()%>)</font></td></tr>
<%
   for(int i=0;i<list.size();i++)
   {
      if(((Node)list.get(i)).getCategory()>50) continue;
      
      Host host = (Host)list.get(i);
      String imgFile = null;
      if(host.getCategory()==1) imgFile = "router.png";
      else if(host.getCategory()==2) imgFile = "switch_router.gif";
      else if(host.getCategory()==3) imgFile = "switch.png";
      else if(host.getCategory()==5) imgFile = "printer.png";
      else
      {
         if(host.getSysOid().equals("1.3.6.1.4.1.311.1.1.3.1.1")) //windows xp   
            imgFile = "winxp.png";
         else if(host.getSysOid().equals("1.3.6.1.4.1.311.1.1.3.1.2")) //windows 2000     
            imgFile = "win2k.png";
         else if(host.getSysOid().equals("1.3.6.1.4.1.311.1.1.3.1.3")) //windows NT   
            imgFile = "winnt.png";
         else if(host.getSysOid().equals("1.3.6.1.4.1.2021.250.10")||
	          host.getSysOid().equals("1.3.6.1.4.1.8072.3.2.10")) //linux     
            imgFile = "linux.png";
         else if(host.getSysOid().startsWith("1.3.6.1.4.1.42.")) //solaris    
            imgFile = "solaris.png";
         else
            imgFile = "server.png";
      }                       

      if(i%perRowNum==0) out.print("<tr>");      
      out.print("<td width='100' height='100'>&nbsp;&nbsp;&nbsp;&nbsp;");
      out.print("<img height='48' width='48' border=0 src='");
      out.print(rootPath);
      out.print("/resource/image/device/");
      out.print(imgFile); 
      out.print("' alt='名&nbsp;&nbsp;&nbsp;称:");        
      out.print(host.getAlias());   
      out.print("\n");         
      out.print("IP地址:");         
      out.print(host.getIpAddress());               
      out.print("'></a><br>&nbsp;&nbsp;&nbsp;");
      out.print(host.getIpAddress());   
      out.print("</td>");         
      if(i%perRowNum==perRowNum-1) out.print("</tr>");         
   }
%>
<tr height="20"><td colspan='<%=perRowNum%>' align='right'>
<a href="#" onclick="javascript:history.back(1)">返回</a>&nbsp;&nbsp;</td></tr>
</table>
</form>
</BODY>
</HTML>
