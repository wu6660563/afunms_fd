<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.inform.model.Alarm"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%
  List list = (List)request.getAttribute("list");
  int rc = list.size();
  
  String rootPath = request.getContextPath();  
%>
<html>
<head>

<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
<script language="JavaScript" type="text/javascript">
  var delAction = "<%=rootPath%>/alarm.do?action=delete";
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
				<td background="<%=rootPath%>/resource/image/main_frame_lbg.gif" width=12>　</td>
				<td height=300 bgcolor="#FFFFFF" valign="top"><br>
	<table cellSpacing="1" cellPadding="0" width="90%" border="0" align='center'>	  
	  <tr><td align='left' colspan="2"><a href="#" onclick="toDelete()">删除</a>
	   &nbsp;<a href="#" onclick="javascript:history.back(1)">返回</a></td></tr>
		<tr>
			<td colspan="2">
			<table class="microsoftLook" cellspacing="1" cellpadding="0" width="100%">
				<tr><th width='22'><INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()"></th>
    <th width='5%'>序号</th>				
    <th width='15%'>设备IP</th>
    <th width='40%'>告警信息</th>
    <th width='10%'>级别</th>    
    <th width='10%'>来源</th>
    <th width='20%'>时间</th>
</tr>
<%
    for(int i=0;i<rc;i++)
    {
       Alarm vo = (Alarm)list.get(i);
%>
<tr class="microsoftLook0">
        <td class="microsoftLook0"><INPUT type="checkbox" class=noborder name=checkbox value="<%=vo.getId()%>"></td>
    	<td class="microsoftLook0"><font color='blue'><%=i + 1%></font></td>
    	<td class="microsoftLook0"><%=vo.getIpAddress()%></td>
    	<td class="microsoftLook0"><%=vo.getMessage()%></td>
    	<td class="microsoftLook0" align='center'>
        <img src="<%=rootPath%>/resource/<%=NodeHelper.getAlarmLevelImage(vo.getLevel())%>" alt='<%=NodeHelper.getAlarmLevelDescr(vo.getLevel())%>'></td>
    	<td class="microsoftLook0" align='center'><%=NodeHelper.getNodeCategory(vo.getCategory())%></td>
		<td class="microsoftLook0" ><%=vo.getLogTime()%></td>
  	</tr>
<%
    }
%> 				
			</table>
			</td>
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
