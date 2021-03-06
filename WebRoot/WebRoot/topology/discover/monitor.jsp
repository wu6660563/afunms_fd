<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.discovery.*"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%
  //------------这个jsp用于监视拓扑发现进程----------------
  DiscoverMonitor monitor = DiscoverMonitor.getInstance();
  monitor.setRefreshTimes();
    
  String rootPath = request.getContextPath();  
  User user = (User)session.getAttribute(SessionConstant.CURRENT_USER); //当前用户
  if(user==null)
  {
      out.print("系统超时,请重新登录!");
      return;
  }   
%>
<html>
<head>
<script language="javascript">
 
  function stopDiscover()
  {
  	//alert("====");
  	window.location = "/afunms/discover.do?action=stop"
  	window.close();
  	//monitorForm.action = "/afunms/discover.do?action=stop";
        //monitorForm.submit();
  } 
</script>
<title>Topo</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">    
<%
   if(monitor.isCompleted())
   {   
%>
    <script>
      alert("发现完成,可以使用系统了!您还可以重新发现"); 
    </script>
    <meta http-equiv="refresh" content="60"><!--1分钟刷新一次-->        
<% }else{%>
   <meta http-equiv="refresh" content="60"><!--1分钟刷新一次-->
<%
   }
%>  
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
</head>
<body bgcolor="#ECECEC">
<form method="post" name="monitorForm" >
<%
  if(monitor.getRefreshTimes()>1)
  { 
%>
<table width="500" align="center" class="microsoftLook">
  <tr><td align='center'><font color='blue'><b>发现进程监视</b></font><input type="button" class="button" value="停止发现" onclick="stopDiscover()" target=_self></td></tr>
  <tr>
    <td valign="top" align="center">
       <table width="100%" border=1 cellspacing=0 cellpadding=0 bordercolorlight='#000000' bordercolordark='#FFFFFF'>
         <tr class="microsoftLook0">
           <td>开始时间</td>
           <td><%=monitor.getStartTime()%></td>
         </tr>
         <tr class="microsoftLook0">
           <td>结束时间</td>
           <td><%=monitor.getEndTime()%></td>
         </tr>
         <tr class="microsoftLook0">
           <td>已经耗时</td>
           <td><%=monitor.getElapseTime()%></td>
         </tr>
       </table>
    </td></tr>   
    <tr><td>
       <table width="100%" border=1 cellspacing=0 cellpadding=0 bordercolorlight='#000000' bordercolordark='#FFFFFF'>
         <tr bgcolor='#D4E1D5'><td>&nbsp;</td><td><b>总数</b></td>
<%
 if(!monitor.isCompleted())
    out.print("<td><b>已发现</b></td><td><b>未发现</b></td>");
%>         
         </tr>
         <tr class='microsoftLook0'>
           <td>设备</td>
           <td><%=monitor.getHostTotal()%></td>
<%
 if(!monitor.isCompleted())
 {
    out.print("<td>");
    out.print(monitor.getDiscoveredNodeTotal());
    out.print("</td><td>");    
    out.print(monitor.getHostTotal() - monitor.getDiscoveredNodeTotal());    
    out.print("</td>");
 }  
%>         
         </tr>
         <tr class='microsoftLook0'>
           <td class="microsoftLook0">子网</td>
           <td><%=monitor.getSubNetTotal()%></td>
<%
 if(!monitor.isCompleted())
   out.print("<td>&nbsp;</td><td>&nbsp;</td>");
%>         
         </tr>
        </table> 
    </td></tr>
    <tr><td align='center'><font color='blue'><b><br>详细</b></font></td></tr>    
    <tr class='microsoftLook0'>
       <td><%=monitor.getResultTable()%></td>
    </tr>
</table> 
<%}%>
</form>
</body>
</html>