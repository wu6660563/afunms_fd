<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%
  String rootPath = request.getContextPath();
  HostNode vo = (HostNode)request.getAttribute("vo");
%>
<html>
<head>
<title>dhcnms</title>

<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
<Script language="javascript" src="<%=rootPath%>/resource/js/calendar.js"></Script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<script language="javascript">
  function toRefresh()
  {
        mainForm.action = "<%=rootPath%>/network.do?action=refreshsysname";
        mainForm.submit();
  }

</script>
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#9FB0C4">
<form name="mainForm" method="post">
<input type=hidden name="id" value="<%=vo.getId()%>">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td width="16">　</td>
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
				<td height=300 bgcolor="#FFFFFF" valign="top"  align="center">
				  <table border="0" id="table1" cellpadding="0" cellspacing="1"
						width="95%">
					<TBODY>
						<tr>
							<TD align="left" colspan="4" height="24">设备--查看</TD>
						</tr>
						<tr>
							<td nowrap colspan="4" height="3" bgcolor="#8EADD5"></td>
						</tr>
					    <tr style="background-color: #ECECEC;">
						    <TD nowrap align="right" height="24" width="10%">名称&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;<%=vo.getAlias()%></TD>
							<TD nowrap align="right" height="24">IP地址&nbsp;</TD>				
							<TD nowrap>&nbsp;<%=vo.getIpAddress()%></TD>	
						</tr>
						<tr>								
							<TD nowrap align="right" height="24">子网掩码&nbsp;</TD>				
							<TD nowrap>&nbsp;<%=vo.getNetMask()%></TD>							
							<TD nowrap align="right" height="24">型号&nbsp;</TD>				
							<TD nowrap>&nbsp;<%=SysUtil.ifNull(vo.getType())%></TD>	
						</tr>
						<tr style="background-color: #ECECEC;">
							<TD nowrap align="right" height="24">系统OID&nbsp;</TD>				
							<TD nowrap>&nbsp;<%=vo.getSysOid()%></TD>					
						    <TD nowrap align="right" height="24">系统描述&nbsp;</TD>				
							<TD nowrap>&nbsp;<textarea name="sys_descr" cols="50" rows="8" class="formStyle" readonly><%=vo.getSysDescr()%></textarea></TD>							
						</tr>
						<tr>
							<td nowrap colspan="4" height="1" bgcolor="#8EADD5"></td>
						</tr>
						<tr>
							<TD nowrap colspan="4">
								<input type=reset class="formStylebutton" style="width:50" value="返回" onclick="javascript:history.back(1)">&nbsp;&nbsp;
								<input type=reset class="formStylebutton" style="width:80" value="刷新系统名称" onclick="toRefresh()">
							</TD>	
						</tr>						
					</TBODY>
				</TABLE>
				</td>
				<td background="<%=rootPath%>/resource/image/main_frame_rbg.gif" width=13 colspan="3">　</td>
			</tr>
			<tr>
				<td valign="top"><img src="<%=rootPath%>/resource/image/main_frame_lbcorner.gif" width=12 height=15></td>
				<td background="<%=rootPath%>/resource/image/main_frame_bbg.gif" height=15></td>
				<td valign="top"><img src="<%=rootPath%>/resource/image/main_frame_rbcorner.gif" width=13 height=15></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</form>	
</body>
</html>
