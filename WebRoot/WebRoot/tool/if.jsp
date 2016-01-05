<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.util.SnmpUtil"%>
<%@page import="com.afunms.discovery.IfEntity"%>
<%@page import="java.util.List"%>
<%    
  //------------这个jsp用于显示设备上各接口的情况----------------
    String rootPath = request.getContextPath();
    String ip = request.getParameter("ip");
    String community = request.getParameter("community");
    String temp = request.getParameter("type");
    int type = 3;
    if(temp!=null)
       type = Integer.parseInt(temp);
    
    List list = null;
    if(ip!=null)    
       list = SnmpUtil.getInstance().getIfEntityList(ip,community,type);
%>
<html>
<head>
<title>topo</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
</head>
<body>
<form method="post" name="mainForm" action="if.jsp">
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
        <tr>
          <td class="lefttd">类型</td>
          <td class="righttd"><select name="type" style="width:100">
          <option value="1">路由</option><option value="2">交换</option>
          </select>
          </td>
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
  <td>Index</td>
  <td>Descr</td>
  <td>type</td>
  <td>IP</td>
  <td>MAC</td>
  <td>Port</td>
</tr>
<%
   if(list!=null&&list.size()!=0)
   {         
      for(int i=0;i<list.size();i++)
      {
         IfEntity obj = (IfEntity)list.get(i);
         String tempIp = null;
         if("".equals(obj.getIpList()))
            tempIp = "&nbsp;";
         else if(obj.getIpList().indexOf(",")!=-1)   
            tempIp = obj.getIpList().replace(",","<br>");
         else
            tempIp = obj.getIpList();   
%>
<tr class='othertr'>
  <td><font color='red'><%=i+1%></font></td>
  <td><%=obj.getIndex()%></td>
  <td><%=obj.getDescr()%></td>
  <td><%=obj.getType() + ""%></td>
  <td><%=tempIp%></td>
  <td><%=obj.getPhysAddress()%></td>
  <td><%=obj.getPort()%>&nbsp;</td>
</tr>
<%
       }//end_for   
   }//end_if
%>  
</table>
</form>
</body>
</html>
