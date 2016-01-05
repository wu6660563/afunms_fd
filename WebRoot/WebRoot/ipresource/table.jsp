<%@page language="java" contentType="text/html;charset=GB2312"%>
<%
   String outPutInfo = (String)request.getAttribute("out_put_info");
   String beginip = (String)request.getAttribute("beginip");
   String endip = (String)request.getAttribute("endip");
   String rootPath = request.getContextPath();    
%>
<HTML>
<HEAD>
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
<script language="JavaScript" type="text/javascript">
  function doLookup()
  {
     mainForm.action = "<%=rootPath%>/ipres.do?action=detail";
     mainForm.submit();
  }
    
</script>
</HEAD>

<BODY leftmargin="0" topmargin="0" bgcolor="#9FB0C4">
<form method="post" name="mainForm">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td width="16"></td>
		<td bgcolor="#9FB0C4" align="center">
		<br>
		<table width="95%" border=0 cellpadding=0 cellspacing=0 width='700'>
			<tr>
				<td><img src="<%=rootPath%>/resource/image/main_frame_ltcorner.gif" width=12 height=34></td>
				<td background = "<%=rootPath%>/resource/image/main_frame_tbg.gif" align="right">
				<img border="0" src="<%=rootPath%>/resource/image/main_frame_tflag.gif" width="66" height="34"></td>
				<td><img src="<%=rootPath%>/resource/image/main_frame_rtcorner.gif" width=13 height=34></td>
			</tr>			
			<tr>
				<td background="<%=rootPath%>/resource/image/main_frame_lbg.gif" width=12></td>
				<td height=300 bgcolor="#FFFFFF" valign="top"><br>				
				<table cellSpacing="1" cellPadding="0" width="60%" border="0" align="center">
				<tr></tr>  				
           <tr align='center'>
             <td width="70%" align='center'>输入IP地址范围&nbsp;从
             <input name="beginip" type="text" class="formStyle" size="15" value="<%=beginip%>">至
             <input name="endip" type="text" class="formStyle" size="15" value="<%=endip%>">
             &nbsp;<input type="button" value="查询" name="B3" class="button" onclick="doLookup()">
           </td></tr>			
           <tr><td height='30' align='center' valign='middle'>
           <img src="<%=rootPath%>/resource/image/ipused.gif" width=10 height=10>&nbsp;长时间不使用的IP</td></tr>	
		   <tr><td align='center'><%=outPutInfo%></td></tr>	
           <tr><td height='20'>&nbsp;</td></tr>
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
</table>
</form>
</BODY>
</HTML>