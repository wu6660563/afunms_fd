<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.topology.dao.TelnetDao"%>
<%@page import="com.afunms.topology.model.TelnetConfig"%>
<%
  String rootPath = request.getContextPath();

  int nodeId = 1;
  String temp = null;
 
  temp = request.getParameter("id");
  if(temp!=null) nodeId = Integer.parseInt(temp);
  
  Host host = (Host)PollingEngine.getInstance().getNodeByID(nodeId);    
  
  TelnetDao dao = new TelnetDao();
  TelnetConfig vo = dao.findByNodeID(nodeId);
%>
<html>
<head>
<title>dhcnms</title>

<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
<script language="JavaScript" type="text/javascript">
  function toAdd()
  {
     mainForm.action = "<%=rootPath%>/server.do?action=config";
     mainForm.submit();     
  }
</script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#9FB0C4">
<form name="mainForm" method="post">
<input type=hidden name="id" value="<%=nodeId%>">
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
				  <table border="0" id="table1" cellpadding="0" cellspacing="1" width="80%">
					<TBODY>
						<tr>
							<TD align="left" colspan="2" height="24">参数配置</TD>
						</tr>
						<tr>
							<td nowrap colspan="2" height="3" bgcolor="#8EADD5"></td>
						</tr>
						<tr>
						   <TD nowrap align="right" height="24" width="10%" style="background-color: #ECECEC;">设备名&nbsp;</TD>				
						   <TD nowrap width="40%"><%=host.getAlias()%></TD>
						</tr>
						<tr>
						   <TD nowrap align="right" height="24" width="10%" style="background-color: #ECECEC;">用户名&nbsp;</TD>				
						   <TD nowrap width="40%">&nbsp;<input type="text" name="user" size="10" class="formStyle" value="<%=vo.getUser()%>"></TD>
						</tr>
						<tr>
						   <TD nowrap align="right" height="24" width="10%" style="background-color: #ECECEC;">密码&nbsp;</TD>				
						   <TD nowrap width="40%">&nbsp;<input type="password" name="password" size="10" class="formStyle" value="<%=vo.getPassword()%>"></TD>
						</tr>
						<tr>
						   <TD nowrap align="right" height="24" width="10%" style="background-color: #ECECEC;">命令提示符&nbsp;</TD>				
						   <TD nowrap width="40%">&nbsp;<input type="text" name="prompt" size="10" class="formStyle" value="<%=vo.getPrompt()%>"></TD>
						</tr>
						<tr>
							<td nowrap colspan="2" height="1" bgcolor="#8EADD5"></td>
						</tr>
						<tr>
							<TD nowrap colspan="2">
								<br>
								<input type="button" value="保存" style="width:50" class="formStylebutton" onclick="toAdd()">&nbsp;&nbsp;
								<input type=reset class="formStylebutton" style="width:50" value="返回" onclick="javascript:history.back(1)">
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
