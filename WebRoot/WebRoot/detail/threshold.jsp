<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.monitor.item.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%
  String rootPath = request.getContextPath();

  int nodeId = 1;
  String temp = null;
 
  temp = request.getParameter("id");
  if(temp!=null) nodeId = Integer.parseInt(temp);
  
  String moid = request.getParameter("moid");
  Host host = (Host)PollingEngine.getInstance().getNodeByID(nodeId);
  
  MonitoredItem item = null;
  if(host.getItemByMoid(moid) instanceof SnmpItem)
     item = (SnmpItem)host.getItemByMoid(moid);
  else
	 item = (CommonItem)host.getItemByMoid(moid);

  String monitorObject = null;
  if("001001".equals(moid))
	 monitorObject = "Window CPU利用率";
  else if("001002".equals(moid))
	 monitorObject = "Window 内存利用率";
  else if("001003".equals(moid))
	 monitorObject = "Window 硬盘利用率";
  if("002001".equals(moid))
	 monitorObject = "Cisco CPU利用率";
  else if("002002".equals(moid))
	 monitorObject = "Cisco 内存利用率";  
  else if("004001".equals(moid))
     monitorObject = "Aix CPU利用率";  
  else if("004002".equals(moid))
	 monitorObject = "Aix 内存利用率";  
  else if("004003".equals(moid))
     monitorObject = "Aix 文件系统利用率";  

%>
<html>
<head>
<title>dhcnms</title>

<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
<script language="JavaScript" type="text/javascript">
  function toAdd()
  {
     if(mainForm.value.value=="")
     {
         alert("请输入阀值!");
         return;     
     }
     else if(isNaN(mainForm.value.value))
     {
         alert("阀值必须是数字");
         return;
     }
     
     var temp = parseInt(mainForm.value.value);
     if(temp>100 || temp <0 )
     {
         alert("阀值只能是0到100数字");
         return;     
     }
      mainForm.action = "<%=rootPath%>/moid.do?action=update";
      mainForm.submit();     
  }
</script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#9FB0C4">
<form name="mainForm" method="post">
<input type=hidden name="id" value="<%=nodeId%>">
<input type=hidden name="moid" value="<%=moid%>">
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
				  <table border="0" id="table1" cellpadding="0" cellspacing="1" width="80%">
					<TBODY>
						<tr>
							<TD align="left" colspan="2" height="24">阀值设置</TD>
						</tr>
						<tr>
							<td nowrap colspan="2" height="3" bgcolor="#8EADD5"></td>
						</tr>
						<tr>
						   <TD nowrap align="right" height="24" width="10%" style="background-color: #ECECEC;">设备名&nbsp;</TD>				
						   <TD nowrap width="40%"><%=host.getAlias()%></TD>
						</tr>
						<tr>
						   <TD nowrap align="right" height="24" width="10%" style="background-color: #ECECEC;">监视对象&nbsp;</TD>				
						   <TD nowrap width="40%"><%=monitorObject%></TD>
						</tr>
						<tr>
						   <TD nowrap align="right" height="24" width="10%" style="background-color: #ECECEC;">阀值&nbsp;</TD>				
						   <TD nowrap width="40%">>&nbsp;<input type="text" name="value" size="10" class="formStyle" value="<%=item.getThreshold()%>">
						   &nbsp;%</TD>
						</tr>
						<tr>
							<td nowrap colspan="2" height="1" bgcolor="#8EADD5"></td>
						</tr>
						<tr>
							<TD nowrap colspan="2">
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
