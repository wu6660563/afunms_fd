<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.discovery.*"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%
  //------------���jsp���ڼ������˷��ֽ���----------------
  DiscoverMonitor monitor = DiscoverMonitor.getInstance();
  monitor.setRefreshTimes();
    
  String rootPath = request.getContextPath();  
  User user = (User)session.getAttribute(SessionConstant.CURRENT_USER); //��ǰ�û�
  if(user==null)
  {
      out.print("ϵͳ��ʱ,�����µ�¼!");
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
      alert("�������,����ʹ��ϵͳ��!�����������·���"); 
    </script>
    <meta http-equiv="refresh" content="60"><!--1����ˢ��һ��-->        
<% }else{%>
   <meta http-equiv="refresh" content="60"><!--1����ˢ��һ��-->
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
  <tr><td align='center'><font color='blue'><b>���ֽ��̼���</b></font><input type="button" class="button" value="ֹͣ����" onclick="stopDiscover()" target=_self></td></tr>
  <tr>
    <td valign="top" align="center">
       <table width="100%" border=1 cellspacing=0 cellpadding=0 bordercolorlight='#000000' bordercolordark='#FFFFFF'>
         <tr class="microsoftLook0">
           <td>��ʼʱ��</td>
           <td><%=monitor.getStartTime()%></td>
         </tr>
         <tr class="microsoftLook0">
           <td>����ʱ��</td>
           <td><%=monitor.getEndTime()%></td>
         </tr>
         <tr class="microsoftLook0">
           <td>�Ѿ���ʱ</td>
           <td><%=monitor.getElapseTime()%></td>
         </tr>
       </table>
    </td></tr>   
    <tr><td>
       <table width="100%" border=1 cellspacing=0 cellpadding=0 bordercolorlight='#000000' bordercolordark='#FFFFFF'>
         <tr bgcolor='#D4E1D5'><td>&nbsp;</td><td><b>����</b></td>
<%
 if(!monitor.isCompleted())
    out.print("<td><b>�ѷ���</b></td><td><b>δ����</b></td>");
%>         
         </tr>
         <tr class='microsoftLook0'>
           <td>�豸</td>
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
           <td class="microsoftLook0">����</td>
           <td><%=monitor.getSubNetTotal()%></td>
<%
 if(!monitor.isCompleted())
   out.print("<td>&nbsp;</td><td>&nbsp;</td>");
%>         
         </tr>
        </table> 
    </td></tr>
    <tr><td align='center'><font color='blue'><b><br>��ϸ</b></font></td></tr>    
    <tr class='microsoftLook0'>
       <td><%=monitor.getResultTable()%></td>
    </tr>
</table> 
<%}%>
</form>
</body>
</html>