<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.inform.model.Alarm"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.node.*"%>
<%
  List list = (List)request.getAttribute("list");
  int rc = list.size();

  JspPage jp = (JspPage)request.getAttribute("page");  
  String rootPath = request.getContextPath();  
%>
<html>
<head>

<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
<script language="JavaScript" type="text/javascript">
  var curpage= <%=jp.getCurrentPage()%>;
  var totalpages = <%=jp.getPageTotal()%>;
  var delAction = "<%=rootPath%>/alarm.do?action=delete";
  var listAction = "<%=rootPath%>/alarm.do?action=list";  
  
  function detail(id)
  {
     window.open("/afunms/detail/dispatcher.jsp?id=" + id,"window","toolbar=no,height=650,width=820,scrollbars=yes,center=yes,screenY=0");
  }
  
</script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#9FB0C4">
<form method="post" name="mainForm">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td width="16"></td>
		<td bgcolor="#9FB0C4" align="center">
		<br>
		<table width="95%" border=0 cellpadding=0 cellspacing=0>
			<tr>
				<td><img src="<%=rootPath%>/resource/image/main_frame_ltcorner.gif" width=12 height=34></td>
				<td background="<%=rootPath%>/resource/image/main_frame_tbg.gif" align="right">
				<img border="0" src="<%=rootPath%>/resource/image/main_frame_tflag.gif" width="66" height="34"></td>
				<td><img src="<%=rootPath%>/resource/image/main_frame_rtcorner.gif" width=13 height=34></td>
			</tr>
			<tr>
				<td background="<%=rootPath%>/resource/image/main_frame_lbg.gif" width=12></td>
				<td height=300 bgcolor="#FFFFFF" valign="top" align='center'>				
<br>
	<table cellSpacing="1" cellPadding="0" width="90%" border="0">
    <tr><td colspan="2" width="80%" align="center">
    <jsp:include page="../../common/page.jsp">
     <jsp:param name="curpage" value="<%=jp.getCurrentPage()%>"/>
     <jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>"/>
   </jsp:include>
        </td></tr> 
		<tr>
			<td colspan="2">
			<table class="microsoftLook" cellspacing="1" cellpadding="0" width="100%">
				<tr><th width='22'><INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()"></th>
    <th width='5%'>���</th>				
    <th width='15%'>�豸IP</th>
    <th width='40%'>�澯��Ϣ</th>
    <th width='10%'>����</th>    
    <th width='10%'>��Դ</th>
    <th width='20%'>ʱ��</th>
</tr>
<%
    int startRow = jp.getStartRow();
    for(int i=0;i<rc;i++)
    {
       Alarm vo = (Alarm)list.get(i);       
       Host host = (Host)PollingEngine.getInstance().getNodeByIP(vo.getIpAddress());
%>
     <tr class="microsoftLook0">
        <td class="microsoftLook0"><INPUT type="checkbox" class=noborder name=checkbox value="<%=vo.getId()%>"></td>
    	<td class="microsoftLook0"><font color='blue'><%=startRow + i%></font></td>
    	<a href="#" onclick='detail(<%=host.getId()%>)'>
    	<td class="microsoftLook0" style="cursor:hand"><%=vo.getIpAddress()%></td></a>
    	<td class="microsoftLook0"><%=vo.getMessage()%></td>
    	<td class="microsoftLook0" align='center'>
    	<img src="<%=rootPath%>/resource/<%=NodeHelper.getAlarmLevelImage(vo.getLevel())%>" alt='<%=NodeHelper.getAlarmLevelDescr(vo.getLevel())%>'></td>
    	<td class="microsoftLook0" align='center'><%=NodeHelper.getNodeCategory(vo.getCategory())%></td>
		<td class="microsoftLook0"><%=vo.getLogTime()%></td>
  	</tr>
<%
    }
%> 				
			</table>
			</td>
		</tr>	
	</table>
	<table cellspacing="5" border="0" width="90%">
	    <tr>
			<td align='left'><a href="#" onclick="toDelete()">ɾ��</a></td>
			<td></td>
		</tr>
	 </table>
</td>
		<td background="<%=rootPath%>/resource/image/main_frame_rbg.gif" width=13></td>
	</tr>
	<tr>
		<td valign="top"><img src="<%=rootPath%>/resource/image/main_frame_lbcorner.gif" width=12 height=15></td>
		<td background="<%=rootPath%>/resource/image/main_frame_bbg.gif" height=15></td>
		<td valign="top"><img src="<%=rootPath%>/resource/image/main_frame_rbcorner.gif" width=13 height=15></td>
	</tr>
</table>
</form>		
</BODY>
</HTML>
