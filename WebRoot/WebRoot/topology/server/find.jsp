<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="java.util.List"%>
<%
  String rootPath = request.getContextPath();
  List list=(List)request.getAttribute("list"); 
%>
<html>
<head>
<script language="javascript">
	
  function doQuery()
  {  
     if(mainForm.key.value=="")
     {
     	alert("�������ѯ����");
     	return false;
     }
     mainForm.action = "<%=rootPath%>/server.do?action=find";
     mainForm.submit();
  }

  function goBack()
  {
     mainForm.action = "<%=rootPath%>/server.do?action=list&jp=1";
     mainForm.submit();  
  }
</script>
<title>dhcnms</title>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
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
				<td background="<%=rootPath%>/resource/image/main_frame_lbg.gif" width=12>��</td>
				<td height=300 bgcolor="#FFFFFF" valign="top">
<br>
	<table cellSpacing="1" cellPadding="0" width="100%" border="0">
        <TR>
        <TD width="50%"><B>��ѯ:</B>
        <SELECT name="key" style="width=100"> 
          <OPTION value="alias" selected>����</OPTION>
          <OPTION value="ip_address">IP��ַ</OPTION>
          <OPTION value="sys_oid">ϵͳOID</OPTION>          
          <OPTION value="type">�ͺ�</OPTION>
          </SELECT>&nbsp;<b>=</b>&nbsp; 
          <INPUT type="text" name="value" width="15" class="formStyle">
          <INPUT type="button" class="formStyle" value="��ѯ" onclick="doQuery()">
          <INPUT type="button" class="formStyle" value="����" onclick="goBack()">
          </td>
        </tr>
		<tr>
			<td colspan="2">
			<table class="microsoftLook" cellspacing="1" cellpadding="0" width="100%">
	  <tr>
      <th width='5%'>���</th>				
      <th width='15%'>����</th>
      <th width='15%'>IP��ַ</th>      
      <th width='15%'>��������</th>
      <th width='20%'>�ͺ�</th>
      <th width='20%'>ϵͳOID</th>
      <th width='5%'>�༭</th>
</tr>
<%
  for(int i=0;i<list.size();i++)
  {
     HostNode vo = (HostNode)list.get(i);           
%>
   <tr class="microsoftLook0" >  
    	<td class="microsoftLook0"><INPUT type="checkbox" class=noborder name=checkbox value="<%=vo.getId()%>">
    	<font color='blue'><%=1 + i%></font></td>
    	<a href="<%=rootPath%>/server.do?action=read&id=<%=vo.getId()%>">
    	<td class="microsoftLook0" align='center' style="cursor:hand"><%=vo.getAlias()%></td></a>
    	<td class="microsoftLook0" align='center'><%=vo.getIpAddress()%></td>
    	<td class="microsoftLook0" align='left'><%=vo.getNetMask()%></td>
		<td class="microsoftLook0" align='center'><%=vo.getType()%></td>
		<td class="microsoftLook0" align='center'><%=vo.getSysOid()%></td>
        <td class="microsoftLook0"><a href="<%=rootPath%>/server.do?action=ready_edit&id=<%=vo.getId()%>">
		<img src="<%=rootPath%>/resource/image/editicon.gif" border="0"/></a></td>		
  	</tr>			
<% }%>
			</table>
			</td>
		</tr>	
	</table>
</td>
		<td background="<%=rootPath%>/resource/image/main_frame_rbg.gif" width=13>��</td>
	</tr>
	<tr>
		<td valign="top"><img src="<%=rootPath%>/resource/image/main_frame_lbcorner.gif" width=12 height=15></td>
		<td background="<%=rootPath%>/resource/image/main_frame_bbg.gif" height=15></td>
		<td valign="top"><img src="<%=rootPath%>/resource/image/main_frame_rbcorner.gif" width=13 height=15></td>
	</tr>
</table>
</BODY>
</HTML>
