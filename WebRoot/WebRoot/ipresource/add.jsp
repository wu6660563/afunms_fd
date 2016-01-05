<%@page language="java" contentType="text/html;charset=GB2312"%>
<%
   String rootPath = request.getContextPath();
%>
<html>
<head>
<title>dhcnms</title>

<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
<script language="JavaScript" type="text/javascript">
  function toAdd()
  {
     var chk1 = checkinput("ip","string","IP地址",15,false);
     var chk2 = checkinput("mac","string","MAC地址",17,false);
   
     if(chk1&&chk2)
     {
        mainForm.action = "<%=rootPath%>/ipmac.do?action=add";
        mainForm.submit();
     }   
  }
</script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#9FB0C4">
<form name="mainForm" method="post">
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
					<input type="hidden" name="id" value="">
					<input type="hidden" name="category" value="25">
					<input type="hidden" name="code" value="DATABASE">
				  <table border="0" id="table1" cellpadding="0" cellspacing="1"
						width="95%">
					<TBODY>
						<tr>
							<TD align="left" colspan="4" height="24">IP信息--添加</TD>
						</tr>
						<tr>
							<td nowrap colspan="4" height="3" bgcolor="#8EADD5"></td>
						</tr>
						<tr style="background-color: #ECECEC;">
						    <TD nowrap align="right" height="24" width="10%">IP地址&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;<input type="text" name="ip" size="16" value="" class="formStyle"><font color="red">&nbsp;*</font></TD>
							<TD nowrap align="right" height="24">MAC地址&nbsp;</TD>				
							<TD nowrap>
								&nbsp;<input type="text" name="mac" size="16" class="formStyle"><font color="red">&nbsp;*</font>
							</TD>	
						</tr>
						<tr>	
							<TD nowrap align="right" height="24">用户名&nbsp;</TD>				
							<TD nowrap>
								&nbsp;<input name="persone" type="text" size="16"  class="formStyle">
							</TD>						
							<TD nowrap align="right" height="24">部门&nbsp;</TD>				
							<TD nowrap>&nbsp;<input name="dept" type="text" size="16" class="formStyle">														
							</TD>	
						</tr>
						<tr style="background-color: #ECECEC;">
							<TD nowrap align="right" height="24">房间号&nbsp;</TD>				
							<TD nowrap>&nbsp;<input name="room" type="text" size="16" class="formStyle"></TD>								
							<td align="right" height="20">电话&nbsp;</td>
							<td colspan="3" align="left">&nbsp;<input name="tel" type="text" size="16" class="formStyle"></td>
						</tr>
						<tr>
							<td nowrap colspan="4" height="1" bgcolor="#8EADD5"></td>
						</tr>
						<tr>
							<TD nowrap colspan="4">
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
