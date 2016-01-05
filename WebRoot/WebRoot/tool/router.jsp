<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.dhcnms.common.util.SnmpUtil"%>
<%@page import="com.dhcnms.discovery.IpAddress"%>
<%@page import="java.util.List"%>
<%
  //------------这个jsp用于显示ip_router_table----------------
  String rootPath = request.getContextPath();
  String ip = request.getParameter("ip");
  String community = request.getParameter("community");
  if(community==null)  community = "qlsh";
  List ipRouterTable = null;
    
  if(community!=null&&ip!=null)
     ipRouterTable = SnmpUtil.getInstance().getIPRouterTable(ip,community,"","4");
%>
<html>
<head>
<title>topo</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
</head>
<body>
<form method="post" name="mainForm" action="router.jsp">
<table width="700" align="center">
    <tr><td valign="top" align="center">
<!--table begin-->
     <table width="50%" border=1 cellspacing=0 cellpadding=0 bordercolorlight='#000000' bordercolordark='#FFFFFF'>
        <tr>
          <td class="lefttd">设备IP</td>
          <td class="righttd"><input name="ip" type="text" style="width:150" class="formStyle" value="<%=ip%>"></td>
        </tr>
        <tr>
          <td class="lefttd">Community</td>
          <td class="righttd"><input name="community" type="text" style="width:150" class="formStyle"
           value="<%=community%>"></td>
        </tr>
      </table>
<!--table end-->
      </td>
    </tr>
    <tr><td width="100%" height="30"></td></tr>
    <tr>
      <td width="100%" align="center" height="30">
        <input type="submit" class="button_01" value="确定" name="B1">        
      </td>
    </tr>
</table>
<table width="700" align="center" border=1 cellspacing=0 cellpadding=0 bordercolorlight='#000000' bordercolordark='#FFFFFF'>
<tr class='othertr'>
  <td>index</td>
  <td>下一步路由</td>
  <td>它的MAC</td>
</tr>
<%
   if(ipRouterTable!=null&&ipRouterTable.size()!=0)
   {   
      IpAddress ipRouter = null;      
      for(int i=0;i<ipRouterTable.size();i++)
      {
         ipRouter = (IpAddress)ipRouterTable.get(i); 
%>
<tr class='othertr'>
  <td><%=ipRouter.getIfIndex() %></td>
  <td><%=ipRouter.getIpAddress() %></td>
  <td><%=ipRouter.getPhysAddress()%></td>
</tr>
<%
       }//end_for   
   }//end_if
%>  
</table>
</form>
</body>
</html>