<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.report.abstraction.JspReport"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%
  JspReport report = (JspReport)request.getAttribute("report");
    
  String day = (String)request.getAttribute("day");
  if(day==null) day = SysUtil.getCurrentDate();
    
  int nodeId = 0;
  Integer temp = (Integer)request.getAttribute("node_id");
  if(temp!=null) nodeId = temp.intValue();
  
  String rootPath = request.getContextPath();  
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
     mainForm.action = "<%=rootPath%>/tomcat.do?action=report";
     mainForm.submit();
  }
  
  function goBack()
  {
     mainForm.action = "<%=rootPath%>/detail/tomcat_detail.jsp?id=<%=nodeId%>";
     mainForm.submit();
  }
</script>
</HEAD>

<BODY leftmargin="0" topmargin="0" bgcolor="#9FB0C4">
<form method="post" name="mainForm">
<input type=hidden name="node_id" value="<%=nodeId%>">
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
<!--=============================table begin=====================-->
<table width="750" border="0" align='center'>
<tr>
    <td height="10">
     <table width="100%" border="0" cellspacing="0" cellpadding="0">
       <tr class="othertr"><td align="center" width="100%">
        日期<input name="day" type="text" class="formStyle" value="<%=day%>" size="10" readonly><img name="selDay1" style="CURSOR:hand" src="<%=rootPath%>/resource/image/cal_btn.gif" onclick="selectDate(mainForm.day)">&nbsp;
      <input type="button" name="report" value="生成报表" class="button" onclick="toReport()">
      <input type="button" name="goback" value="返回" class="button" onclick="goBack()">
      </td>
      </tr>
      <tr><td height="10"></td></tr>
    </table>
   </td>
  </tr>
</table><br>
<!--**************************************report begin******************************************-->
<table width="600" border="0" cellspacing="0" cellpadding="0" align="center">
<tr><td align="center" height='30' valign='middle'><b><%=report.getHead()%></b></td></tr>
<%
if((report.getChart()!=null)&&(!report.getChart().equals("")))
{
%>
<tr><td width="100%" align="center"><br><img src="<%=rootPath%>/artist?series_key=<%=report.getChart()%>"></td></tr>
<%}%>
</table>
<table width=300 align='center'><tr><td><%=report.getTable()%></td></tr></table>
<!--**************************************begin end******************************************-->
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