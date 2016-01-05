<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.dhcnms.application.util.SymantecReport"%>
<%
  SymantecReport report1 = (SymantecReport)request.getAttribute("report1");
  SymantecReport report2 = (SymantecReport)request.getAttribute("report2");
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
     FrmReport.action = "<%=rootPath%>/symantec.do?action=report";
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
				<td height=300 bgcolor="#FFFFFF" valign="top"><br>				
<!--=============================table begin=====================-->
<table width="750" border="0">
  <tr>
    <td align="right" height="10">
     <table width="80%" border="0" cellspacing="0" cellpadding="0">
       <tr class="othertr"><td align="right">
        日期<input name="begindate" type="text" class="formStyle" value="<%=report1.getBeginDate()%>" size="10" readonly><img name="selDay1" style="CURSOR:hand" src="<%=rootPath%>/resource/image/cal_btn.gif" onclick="selectDate(FrmReport.begindate)">&nbsp;
         前<%=report1.getTopnBox()%>位</td>
        <td>&nbsp;<input type="button" name="Submit" value="生成报表" class="button" onclick="toReport()"></td>
      </tr>
    </table>
   </td>
  </tr>
</table><br>
<!--**************************************感染病毒机器报表******************************************-->
<table width="700" border="0" cellspacing="0" cellpadding="0" align="center">
<tr class="headtr"><td align="center"><b><%=report1.getHead()%></b></td></tr>
<tr class="othertr"><td align="center"><%=report1.getTitle()%></td></tr>
<tr><td width="100%"><%=report1.getTable()%></td></tr>
<tr><td><br><%=report1.getChart()%></td></tr>
</table>
<br>
<hr>
<!--**************************************病毒报表*********************************************-->
<table width="700" border="0" cellspacing="0" cellpadding="0" align="center">
<tr class="headtr"><td align="center"><b><%=report2.getHead()%></b></td></tr>
<tr class="othertr"><td align="center"><%=report2.getTitle()%></td></tr>
<tr><td width="100%"><%=report2.getTable()%></td></tr>
<tr><td><br><%=report2.getChart()%></td></tr>
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