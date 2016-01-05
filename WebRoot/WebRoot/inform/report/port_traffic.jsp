<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.report.abstraction.JspReport"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.node.*"%>
<%
    JspReport report1 = (JspReport)request.getAttribute("report1");
    JspReport report2 = (JspReport)request.getAttribute("report2");
    String rootPath = request.getContextPath();
    
    String day = (String)request.getAttribute("day");
    int nodeId = ((Integer)request.getAttribute("node_id")).intValue();
    String ifIndex = (String)request.getAttribute("index");
    Host host = (Host)PollingEngine.getInstance().getNodeByID(nodeId);
    IfEntity ifObj = (IfEntity)host.getInterfaceHash().get(ifIndex);     
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<script language="javascript">
  function selectDate(obj)
  {
     result = window.showModalDialog('<%=rootPath%>/common/calendar.htm',obj.value,'dialogWidth=185px;dialogHeight=210px;status=0;help=0');
     if (result!=null) obj.value = result;
  }

  function toReport()
  {
     FrmReport.action = "<%=rootPath%>/port.do?action=port_jsp&node_id=<%=nodeId%>&index=<%=ifIndex%>";
     FrmReport.submit();
  }
</script>
</HEAD>

<BODY leftmargin="0" topmargin="0" bgcolor="#9FB0C4">
<form method="post" name="FrmReport">
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
				<td height=500 bgcolor="#FFFFFF" valign="top" align="center"><br>				
<!--=============================table begin=====================-->
	<table width="75%">
		<tr nowrap>
		<td align="right" nowrap>
		日期<input name="day" type="text" class="formStyle" value="<%=day%>" size="10" readonly><img name="selDay1" style="CURSOR:hand" src="<%=rootPath%>/resource/image/cal_btn.gif" onclick="selectDate(FrmReport.day)">&nbsp;
		</td>
		<td align="left" nowrap>&nbsp;<input type="button" name="Submit" value="生成报表" class="button" onclick="toReport()"></td>
		<td aglin="right" nowrap><a href="<%=rootPath%>/topology/network/port.jsp">返回</a></td>
		</tr>
	</table>
	<table width="75%">
		<tr><td colspan="3" align="center" background="<%=rootPath%>/resource/image/td-bg.jpg"><b>端口信息</b></td></tr>
		<tr class="microsoftLook0"><td  align="left" width="10%">设备</td><td align="left"><%=host.getAlias()%>(<%=host.getIpAddress()%>)</td></tr>
		<tr class="microsoftLook0"><td  align="left" width="10%">端口号</td><td align="left"><%=ifObj.getPort()%></td></tr>
	    <tr class="microsoftLook0"><td  align="left" width="10%">接口索引</td><td align="left"><%=ifObj.getIndex() %></td></tr>
		<tr class="microsoftLook0"><td  align="left" width="10%">接口描述</td><td align="left"><%=ifObj.getDescr()%></td></tr>
		<tr class="microsoftLook"><td colspan="3" background="<%=rootPath%>/resource/image/td-bg.jpg"></td></tr>
	</table><br>
	<table border="0" cellspacing="0" cellpadding="0" align="center">
	   <tr><td align="right">
          <a href="<%=rootPath%>/port.do?action=port_traffic_pdf&day=<%=day%>&node_id=<%=nodeId%>&index=<%=ifIndex%>" target="_blank"><img name="selDay1" alt='导出PDF' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_pdf.gif" width=30 height=32 border="0"></a>
          &nbsp;&nbsp;&nbsp;
          <a href="<%=rootPath%>/port.do?action=port_traffic_excel&day=<%=day%>&node_id=<%=nodeId%>&index=<%=ifIndex%>" target="_blank"><img name="selDay1" alt='导出EXCEL' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=30 height=32 border="0"></a>
       </td></tr>      
		<tr><td height='30' align="center"><b><%=report1.getHead()%></b></td></tr>
		<tr><td align="center"><img src="<%=rootPath%>/artist?series_key=<%=report1.getChart()%>"></td></tr>
		<tr><td height='30'></td></tr>
		<tr><td align="center" width="400"><%=report1.getTable()%></td></tr>
		<tr><td height='30'></td></tr>
	</table>
	<hr>
	<table border="0" cellspacing="0" cellpadding="0" align="center">
	   <tr><td align="right">
          <a href="<%=rootPath%>/port.do?action=port_util_pdf&day=<%=day%>&node_id=<%=nodeId%>&index=<%=ifIndex%>" target="_blank"><img name="selDay1" alt='导出PDF' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_pdf.gif" width=30 height=32 border="0"></a>
          &nbsp;&nbsp;&nbsp;
          <a href="<%=rootPath%>/port.do?action=port_util_excel&day=<%=day%>&node_id=<%=nodeId%>&index=<%=ifIndex%>" target="_blank"><img name="selDay1" alt='导出EXCEL' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=30 height=32 border="0"></a>
       </td></tr>      
		<tr><td height='30' align="center"><b><%=report2.getHead()%></b></td></tr>
		<tr><td align="center"><img src="<%=rootPath%>/artist?series_key=<%=report2.getChart()%>"></td></tr>
		<tr><td height='30'></td></tr>
		<tr><td align="center" width="400"><%=report2.getTable()%></td></tr>
		<tr><td height='30'></td></tr>
	</table>	
<!--=============================table end=====================-->			
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