<%@page language="java" contentType="text/html;charset=GB2312"%>
<%
   String result = (String)request.getAttribute("result");
   String address = (String)request.getAttribute("address");
      
   String rootPath = request.getContextPath();    
%>
<HTML>
<HEAD>
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
<script language="JavaScript" type="text/javascript">
  function doLookup()
  {
     if(mainForm.address.value=="")
     {
         alert("请输入地址!");
         mainForm.address.focus();
         return false;
     }     
     mainForm.action = "<%=rootPath%>/ipres.do?action=locate";
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
           <tr align='center'>
             <td width="100%" align='center'>输入IP或MAC地址:
             <input name="address" type="text" class="formStyle" size="20" value="<%=address%>">
             &nbsp;<input type="button" value="定位" name="B3" class="button" onclick="doLookup()">
             (MAC以 00:16:36:2a:17:cf 形式输入)
           </td></tr>			
           <tr><td height='30'>&nbsp;</td></tr>	
		   <tr><td align='center'><font color='blue'><%=result%></font></td></tr>	
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