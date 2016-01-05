<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.dhcnms.common.util.SnmpUtil"%>
<%    
  //------------确定设备类型----------------
    String rootPath = request.getContextPath();
    String ip = request.getParameter("ip");
    String community = request.getParameter("community");
    int type = 0;
    if(ip!=null)    
    {
       String sysOid = SnmpUtil.getInstance().getSysOid(ip,community);
       type = SnmpUtil.getInstance().checkDevice(ip,community,sysOid);
    }   
    if(community==null) community = "public";
%>
<html>
<head>
<title>topo</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
</head>
<body>
<form method="post" name="mainForm" action="check.jsp">
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
  <td>
<%
   if(type==1) out.print("路由器");
   else if(type==2) out.print("路由交换");
   else if(type==3) out.print("交换机");
   else if(type==4) out.print("打印机");
   else if(type==5) out.print("服务器");
   else out.print("其他");
%>
</td>
</tr>
</table>
</form>
</body>
</html>
