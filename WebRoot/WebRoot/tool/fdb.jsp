<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.util.*"%>
<%    
  //------------这个jsp用于显示设备上各接口的情况----------------
    String rootPath = request.getContextPath();
    String ip = request.getParameter("ip");
    String community = request.getParameter("community");
  
    String[][] fdb = null;
    if(ip!=null)
       fdb = SnmpUtil.getInstance().getFdbTable(ip,community);
%>
<html>
<head>
<title>topo</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
</head>
<body>
<form method="post" name="mainForm" action="fdb.jsp">
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
  <td>序号</td>
  <td>Mac</td>
  <td>Port</td>
  <td>Type</td>
</tr>
<%
   if(fdb!=null)
   {         
      for(int i=0;i<fdb.length;i++)
      {
%>
<tr class='othertr'>
  <td><font color='red'><%=i+1%></font></td>
  <td><%=fdb[i][0]%></td>
  <td><%=fdb[i][1]%></td>
  <td><%=fdb[i][2]%></td>
</tr>
<%
       }//end_for   
   }//end_if
%>  
</table>
</form>
</body>
</html>
