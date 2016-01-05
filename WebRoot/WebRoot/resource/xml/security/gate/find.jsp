<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.security.model.GateRecord"%>
<%
  List list = (List)request.getAttribute("list");
  int rc = list.size();
  String rootPath = request.getContextPath();
%>
<html>
<head>
<title></title>

<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
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
				<td background = "<%=rootPath%>/resource/image/main_frame_tbg.gif" align="right">
				<img border="0" src="<%=rootPath%>/resource/image/main_frame_tflag.gif" width="66" height="34"></td>
				<td><img src="<%=rootPath%>/resource/image/main_frame_rtcorner.gif" width=13 height=34></td>
			</tr>
			<tr>
				<td background="<%=rootPath%>/resource/image/main_frame_lbg.gif" width=12></td>
				<td height=300 bgcolor="#FFFFFF" valign="top">				
					<table cellSpacing="1" cellPadding="0" width="100%" border="0">
		<tr>
			<td colspan="2">&nbsp;
				<table class="microsoftLook" cellspacing="1" cellpadding="0" width="100%">
				<tr>	
    <th width='5%'>序号</th>
    <th width='20%'>人员</th>
    <th width='20%'>事件</th>
    <th width='30%'>发生时间</th>
    <th width='25%'>进/出</th>
</tr>
<%
    for(int i=0;i<rc;i++)
    {
       GateRecord vo = (GateRecord)list.get(i);
%>
       <tr class="microsoftLook0">
    	<td class="microsoftLook0"><font color='blue'><%=1 + i%></font></td>
		<td class="microsoftLook0"><%=vo.getPerson()%></td> 	
		<td class="microsoftLook0"><%=vo.getEvent()%></td>
		<td class="microsoftLook0"><%=vo.getLogTime()%></td>						
   		<td class="microsoftLook0"><%=vo.getIo()%></td>
  	</tr>
<%}%>  	  				
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
		</td>
	</tr>
  </form>
</table>
</BODY>
</HTML>
