<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.security.model.MgeUps"%>
<%@page import="com.afunms.security.util.MachineProtectHelper"%>
<%
  List list = (List)request.getAttribute("list");
  int rc = list.size();
 
  String rootPath = request.getContextPath();  
%>
<HTML>
<HEAD>
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
<script language="javascript">
  var delAction = "<%=rootPath%>/ups.do?action=delete";
  var listAction = "<%=rootPath%>/ups.do?action=list";
  
  function toAdd()
  {
     mainForm.action = "<%=rootPath%>/ups.do?action=ready_add";
     mainForm.submit();
  }
  
  function detail(id)
  {
     window.open("/afunms/detail/ups_detail.jsp?id=" + id,"window","toolbar=no,height=650,width=820,scrollbars=yes,center=yes,screenY=0");
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
		<table width="90%" border=0 cellpadding=0 cellspacing=0>
			<tr>
				<td><img src="<%=rootPath%>/resource/image/main_frame_ltcorner.gif" width=12 height=34></td>
				<td background = "<%=rootPath%>/resource/image/main_frame_tbg.gif" align="right">
				<img border="0" src="<%=rootPath%>/resource/image/main_frame_tflag.gif" width="66" height="34"></td>
				<td><img src="<%=rootPath%>/resource/image/main_frame_rtcorner.gif" width=13 height=34></td>
			</tr>
			<tr>
				<td background="<%=rootPath%>/resource/image/main_frame_lbg.gif" width=12></td>
				<td height=300 bgcolor="#FFFFFF" valign="top"><br>				
					<table cellSpacing="1" cellPadding="0" width="100%" border="0">						    
				<td colspan="2">
					<table class="microsoftLook" cellspacing="1" cellpadding="0" width="100%">
					<tr><th width='22'></th>
    <th width='30'>ÐòºÅ</th>
    <th width='100'>Ãû³Æ</th>
    <th width='100'>IPµØÖ·</th>
    <th width='100'>Î»ÖÃ</th>
    <th width='80'>µ±Ç°×´Ì¬</th>
    <th width='80'>ÏêÏ¸</th>
    <th width='30'>±à¼­</th>
</tr>
<%
    for(int i=0;i<rc;i++)
    {
       MgeUps vo = (MgeUps)list.get(i);
%>
       <tr class="microsoftLook0" >
    	<td class="microsoftLook0"><INPUT type="radio" class=noborder name=radio value="<%=vo.getId()%>"></td>
    	<td class="microsoftLook0"><font color='blue'><%=1 + i%></font></td>
		<td class="microsoftLook0"><%=vo.getAlias()%></td> 
		<td class="microsoftLook0"><%=vo.getIpAddress()%></td> 
		<td class="microsoftLook0"><%=vo.getLocation()%></td>		
		<td class="microsoftLook0" align='center'><img src="<%=rootPath%>/resource/<%=MachineProtectHelper.getStatusImage(vo.getStatus())%>" 
		 alt="<%=MachineProtectHelper.getUPSStatusDescr(vo.getStatus())%>" border="0"/></td>		
		<td class="microsoftLook0" align='center'><a href="#" onclick='detail(<%=vo.getId()%>)'>
		<img src="<%=rootPath%>/resource/image/detail.jpg" border="0"/></a></td>		
		<td class="microsoftLook0"><a href="<%=rootPath%>/ups.do?action=ready_edit&id=<%=vo.getId()%>">
		<img src="<%=rootPath%>/resource/image/editicon.gif" border="0"/></a></td>
  	</tr>
<%}%>  	  				
							</table>
							</td>
						</tr>	
					</table>
					<table cellspacing="5" border="0"><tr>
			<td><a href="#" onclick="toAdd()">Ìí¼Ó</a></td>
			<td><a href="#" onclick="toDelete2()">É¾³ý</a></td>
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
