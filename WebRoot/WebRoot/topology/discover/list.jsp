<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="java.util.List"%>
<%
  String rootPath = request.getContextPath();
  List list=(List)request.getAttribute("list");
  JspPage jp = (JspPage)request.getAttribute("page"); 
%>
<html><head>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>  
<script language="javascript">
   var curpage= <%=jp.getCurrentPage()%>;
   var totalpages = <%=jp.getPageTotal()%>;
   var listAction = "<%=rootPath%>/discover.do?action=list";
	
  function doQuery()
  {  
     if(mainForm.key.value=="")
     {
     	alert("请输入查询条件");
     	return false;
     }
     mainForm.action = "<%=rootPath%>/discover.do?action=find";
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
				<td background="<%=rootPath%>/resource/image/main_frame_lbg.gif" width=12>　</td>
				<td height=300 bgcolor="#FFFFFF" valign="top">
<br>
	<table cellSpacing="1" cellPadding="0" width="100%" border="0">
					<tr>
				        <td align="left" nowrap>
    <jsp:include page="../../common/page.jsp">
     <jsp:param name="curpage" value="<%=jp.getCurrentPage()%>"/>
     <jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>"/>
   </jsp:include>
				        </td>
				    </tr>
        <TR>
        <TD width="15%"><B>查询:</B>
        <SELECT name="key" style="width=100"> 
          <OPTION value="alias" selected>设备名称</OPTION>
          <OPTION value="ip_address">IP地址</OPTION>
          <OPTION value="sys_oid">系统OID</OPTION>          
          <OPTION value="category">设备类型</OPTION>
          </SELECT>&nbsp;<b>=</b>&nbsp; 
          <INPUT type="text" name="value" width="15" class="formStyle">
          <INPUT type="button" class="formStyle" value="查询" onclick=" return doQuery()">
          </td>             
        </tr>
		<tr>
			<td colspan="2">
			<table class="microsoftLook" cellspacing="1" cellpadding="0" width="100%">
				<tr>
      <th width='5%'>序号</th>				
      <th width='15%'>名称</th>
      <th width='10%'>类型</th>
      <th width='15%'>型号</th>
      <th width='15%'>IP地址</th>
      <th width='20%'>系统OID</th>
      <th width='10%'>共同体</th>
</tr>
<%
  int startRow = jp.getStartRow();
  for(int i=0;i<list.size();i++)
  {
     HostNode vo = (HostNode)list.get(i);           
%>
<tr class="microsoftLook0" >    	
    	<td class="microsoftLook0"><font color='blue'><%=startRow + i%></font></td>
    	<a href="<%=rootPath%>/discover.do?action=read&id=<%=vo.getId()%>">
    	<td class="microsoftLook0" align='center' style="cursor:hand"><%=vo.getAlias()%></td></a>
    	<td class="microsoftLook0" align='center'><%=NodeHelper.getNodeCategory(vo.getCategory())%></td>
    	<td class="microsoftLook0" align='left'><%=vo.getType()%></td>
		<td class="microsoftLook0" align='center'><%=vo.getIpAddress()%></td>
		<td class="microsoftLook0" align='center'><%=vo.getSysOid()%></td>
		<td class="microsoftLook0" align='center'><%=vo.getCommunity()%></td>
  	</tr>			
<% }%>
			</table>
			</td>
		</tr>	
	</table>
</td>
		<td background="<%=rootPath%>/resource/image/main_frame_rbg.gif" width=13>　</td>
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
