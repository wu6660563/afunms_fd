<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.ipresource.model.ChangeMessage"%>
<%
  List list = (List)request.getAttribute("list");
  int rc = list.size();

  JspPage jp = (JspPage)request.getAttribute("page");  
  String rootPath = request.getContextPath();    
%>
<HTML>
<HEAD>
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
<script language="javascript">
  var curpage= <%=jp.getCurrentPage()%>;
  var totalpages = <%=jp.getPageTotal()%>;
  var listAction = "<%=rootPath%>/ipres.do?action=list";
  
  function doChange()
  {
     mainForm.action = "<%=rootPath%>/ipres.do?action=detail&jp=1";
     mainForm.submit();
  } 
  
  function doFind()
  {
     mainForm.action = "<%=rootPath%>/ipres.do?action=find";
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
		<table width="95%" border=0 cellpadding=0 cellspacing=0>
			<tr>
				<td><img src="<%=rootPath%>/resource/image/main_frame_ltcorner.gif" width=12 height=34></td>
				<td background = "<%=rootPath%>/resource/image/main_frame_tbg.gif" align="right">
				<img border="0" src="<%=rootPath%>/resource/image/main_frame_tflag.gif" width="66" height="34"></td>
				<td><img src="<%=rootPath%>/resource/image/main_frame_rtcorner.gif" width=13 height=34></td>
			</tr>
			<tr>
				<td background="<%=rootPath%>/resource/image/main_frame_lbg.gif" width=12></td>
				<td height=300 bgcolor="#FFFFFF" valign="top"><br>				
					<table cellSpacing="1" cellPadding="0" width="80%" border="0" align="center">
					<tr>
						<td width="60%" align='left'><font color="blue">按网段查询:</font>&nbsp;
	                    	从<input name="start_value" type="text" style="width:110" class="formStyle" maxLength="20">
		                  	到<input name="end_value" type="text" style="width:110" class="formStyle" maxLength="20">
		                  &nbsp;<input type="button" class="button" value="查询" name="B1" onclick="doFind()"> 
		                  (以 10.110.1 形式输入)
	                	</td>
                	 </tr>
       <tr><td width='40%' align='left'></td></tr>
	 <tr><td height='20'>&nbsp;</td>&nbsp;<td></td></tr>
    <tr><td colspan="2" width="70%" align="center">
	    <jsp:include page="../common/page.jsp">
	     <jsp:param name="curpage" value="<%=jp.getCurrentPage()%>"/>
	     <jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>"/>
	   </jsp:include>
    </td></tr> 
						<tr>
							<td colspan="2">
								<table class="microsoftLook" cellspacing="1" cellpadding="0" width="100%" align='center'>
								<tr><th width='22'></th>
    <th width='10%'>序号</th>
    <th width='60%'>变更情况</th>
    <th width='30%'>时间</th>
</tr>
<%
    int startRow = jp.getStartRow();
    for(int i=0;i<rc;i++)
    {
       ChangeMessage vo = (ChangeMessage)list.get(i);
%>
       <tr class="microsoftLook0" >
    	<td class="microsoftLook0"></td>
    	<td class="microsoftLook0"><font color='blue'><%=startRow + i%></font></td>
		<td class="microsoftLook0" align='left'>
<%  if(vo.getTag()==1)
       out.print("新增地址:" + vo.getAddress() + "[" + vo.getMessage() + "]");
    else  
       out.print("<font color='blue'>地址变更:</font>" + vo.getAddress() + "[" + vo.getMessage() + "]"); 
%></td> 				
		<td class="microsoftLook0" align='left'><%=vo.getLogTime()%></td>			
  	</tr>
<%}%>  	  				
							</table>
							</td>
						</tr>	
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