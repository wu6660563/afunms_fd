<%@page language="java" contentType="text/html;charset=GB2312"%>

<%  
   String rootPath = request.getContextPath();  

   String lineId = (String)request.getAttribute("lineId");  
%>
<html>
<head>
<base target="_self">

<meta http-equiv="Pragma" content="no-cache">
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
<script language="javascript">
  function toEdit()
  {
      var   args   =   window.dialogArguments;   
      mainForm.action = "<%=rootPath%>/submap.do?action=saveLinkProperty&xml="+args.fatherXML;
      mainForm.submit();
      window.close(); 
      args.location.reload(); 
  }

</script>
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#9FB0C4">
<form name="mainForm" method="post">
<input type=hidden name="lineId" value="<%=lineId%>">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=300px>
	<tr>
		<td bgcolor="#cedefa" align="center" valign=top>
		<table width="300px" border=0 cellpadding=0 cellspacing=0>
				<tr>
					<td height="28" align="left" bordercolor="#397DBD" bgcolor="#397DBD" class="txtGlobalBold">&nbsp;&nbsp;��·��ʽ</td>
				</tr>
			<tr>
				<td height=200 bgcolor="#FFFFFF" valign="top">
				 <table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%" align="center">
					<TBODY>
						<tr>
							<td nowrap colspan="2" height="3" bgcolor="#8EADD5"></td>
						</tr>
			
			<tr>						
			<TD style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">��·����&nbsp;</TD>				
			<TD nowrap width="80%">&nbsp; <input type="text" name="link_name" size="26" class="formStyle"></TD>	
            </tr>
			<tr>						
			<TD style="background-color: #ECECEC;" nowrap align="right" height="24" width="20%">��·���&nbsp;</TD>				
			<TD nowrap width="80%">&nbsp;
			   <select size=1 name='link_width' style='width:50px;' onchange="">
			       <option value="1" selected>1</option>
				   <option value="2">2</option>
				   <option value="3">3</option>
				   <option value="4">4</option>
				   <option value="5">5</option>
			   </select>
			</TD>
			</tr>
			
     
			<tr>
				<TD nowrap colspan="4" align=center>
					<br>
					<input type="button" value="�޸�" style="width:50" class="formStylebutton" onclick="toEdit()">
					<input type="button" value="�ر�" style="width:50" class="formStylebutton" onclick="window.close();">
				</TD>	
			</tr>
			</TBODY>
				</TABLE>				
				</td>
			</tr>			
		</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>