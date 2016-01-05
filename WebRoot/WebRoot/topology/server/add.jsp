<%@page language="java" contentType="text/html;charset=GB2312"%>
<%  
   String rootPath = request.getContextPath();    
%>
<html>
<head>

<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
<title></title>
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script language="javascript">
  function toAdd()
  {
     var chk1 = checkinput("ip_address","ip","IP地址",15,false);
     var chk2 = checkinput("alias","string","机器名",30,false);
     var chk3 = checkinput("community","string","共同体",30,false);
     
     if(chk1&&chk2&&chk3)
     {
        msg.style.display="block";
        mainForm.action = "<%=rootPath%>/server.do?action=add";
        mainForm.submit();
     }
  }

</script>
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#9FB0C4">
<form name="mainForm" method="post">
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
				<td height=300 bgcolor="#FFFFFF" valign="top"><br>
				 <table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%">
					<TBODY>
						<tr>
							<td nowrap align="left" colspan="4" height="24">服务器--添加</td>
						</tr>
						<tr>
							<td nowrap colspan="4" height="3" bgcolor="#8EADD5"></td>
						</tr>
			<tr style="background-color: #ECECEC;">						
			<TD nowrap align="right" height="24" width="10%">IP地址&nbsp;</TD>				
			<TD nowrap width="40%">&nbsp;
			<input type="text" name="ip_address" maxlength="50" size="20" class="formStyle">
			<font color="red">&nbsp;*</font></TD>						
			<TD nowrap align="right" height="24" width="10%">机器名&nbsp;</TD>				
			<TD nowrap width="40%">&nbsp;
			<input type="text" name="alias" maxlength="50" size="20" class="formStyle">
			<font color="red">&nbsp;*</font></TD>															
			</tr>
			<tr>						
			<TD nowrap align="right" height="24" width="10%">共同体&nbsp;</TD>				
			<TD nowrap width="40%">&nbsp;
			<input type="text" name="community" maxlength="50" size="20" class="formStyle">
			<font color="red">&nbsp;*</font></TD>						
			<TD nowrap align="right" height="24" width="10%">&nbsp;</TD>				
			<TD nowrap width="40%">&nbsp;</TD>															
			</tr>
<tr><td colspan="4" align='center'><div id="msg" style="display:none"><font color="#FF0000">需要一点时间,请稍候...</font></div></td></tr>						
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
				<td background="<%=rootPath%>/resource/image/main_frame_rbg.gif" width=13 colspan="3"></td>
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