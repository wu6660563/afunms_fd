<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.ipresource.model.IpMac"%>
<%@page import="com.afunms.initialize.ResourceCenter"%>
<%
  List list = (List)request.getAttribute("list");
  int rc = list.size();

  String rootPath = request.getContextPath();    
  int idelDays = ResourceCenter.getInstance().getIpIdleDays();
%>
<HTML>
<HEAD>
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
<script language="javascript">  
  function doFind()
  {
     mainForm.action = "<%=rootPath%>/ipmac.do?action=idle_ips";
     mainForm.submit();
  }
  
  function doList()
  {
     mainForm.action = "<%=rootPath%>/ipmac.do?action=list";
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
					<table cellSpacing="1" cellPadding="0" width="80%" border="0" align="center">
        <tr><td width="60%" align='left'><font color="blue">选择:</font>&nbsp;
                  <select size=1 name='idle_days' style='width:60px;' onchange="doFind()">
                           <option value="10" <%if(idelDays==10) out.print("selected");%>>10天</option>
                           <option value="20" <%if(idelDays==20) out.print("selected");%>>20天</option>
                           <option value="30" <%if(idelDays==30) out.print("selected");%>>30天</option>
						   <option value="60" <%if(idelDays==60) out.print("selected");%>>60天</option>
           </select>不用的IP</td><td align='right'>
           &nbsp;&nbsp;<input type="button" class="button" value="返回" name="B1" onclick="doList()">         
     </td></tr>
						<tr>
							<td colspan="2">
								<table class="microsoftLook" cellspacing="1" cellpadding="0" width="100%" align='center'>
								<tr><th width='22'></th>
    <th width='5%'>序号</th>
    <th width='15%'>IP地址</th>
    <th width='20%'>MAC地址</th>
    <th width='15%'>用户名</th>
    <th width='15%'>部门</th>
    <th width='15%'>房间号</th>
    <th width='10%'>电话</th>
    <th width='5%'>编辑</th>    
</tr>
<%
    for(int i=0;i<rc;i++)
    {
       IpMac  vo = (IpMac )list.get(i);
%>
       <tr class="microsoftLook0" >
    	<td class="microsoftLook0"><INPUT type="checkbox" class=noborder name="checkbox" value="<%=vo.getId()%>"/></td>
    	<td class="microsoftLook0"><font color='blue'><%=1 + i%></font></td>
		<td class="microsoftLook0" align='center'><font color='blue'><%=vo.getIpAddress()%></font></td> 				
		<td class="microsoftLook0" align='center'><%=vo.getMac()%></td>	
		<td class="microsoftLook0" align='center'><%=vo.getPerson()%></td>	
		<td class="microsoftLook0" align='center'><%=vo.getDept()%></td>	
		<td class="microsoftLook0" align='center'><%=vo.getRoom()%></td>	
		<td class="microsoftLook0" align='center'><%=vo.getTel()%></td>	
        <td class="microsoftLook0"><a href="<%=rootPath%>/ipmac.do?action=ready_edit&id=<%=vo.getId()%>">
		<img src="<%=rootPath%>/resource/image/editicon.gif" border="0"/></a></td>		
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