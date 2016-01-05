<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.report.abstraction.JspReport"%>
<%
    JspReport report = (JspReport)request.getAttribute("report");
    String rootPath = request.getContextPath();
    
    String day = (String)request.getAttribute("day");
    String field = (String)request.getAttribute("field");
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
     mainForm.action = "<%=rootPath%>/inform.do?action=network_jsp";
     mainForm.submit();
  }  
</script>
</HEAD>

<BODY leftmargin="0" topmargin="0" bgcolor="#9FB0C4">
<form method="post" name="mainForm">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr><td width="16"></td>
		<td bgcolor="#9FB0C4" align="center">
		<br>
		<table width="95%" border=0 cellpadding=0 cellspacing=0 >
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
<table width="85%" align="center" border="0" height="24">
    <tr>
       <td align="left" width="60%">
        日期<input name="day" type="text" class="formStyle" value="<%=report.getTimeStamp()%>" size="10" readonly><img name="selDay1" style="CURSOR:hand" src="<%=rootPath%>/resource/image/cal_btn.gif" onclick="selectDate(mainForm.day)">&nbsp;           
       &nbsp;
        按 <select size=1 name='field' style='width:100px;'>
           <option value='ip_long' <%if(field.equals("ip_long")) out.print("selected");%>>IP地址</option>
           <option value='cpu_value' <%if(field.equals("cpu_value")) out.print("selected");%>>CPU利用率</option>
           <option value='mem_value' <%if(field.equals("mem_value")) out.print("selected");%>>内存利用率</option>
           <option value='if_util' <%if(field.equals("if_util")) out.print("selected");%>>接口利用率</option>
          </select>排序&nbsp;
          <input type="button" name="Submit" value="查询" class="button" onclick="toReport()">
       </td>
       <td align="right" width="40%">
          <a href="<%=rootPath%>/inform.do?action=network_pdf&day=<%=day%>&field=<%=field%>" target="_blank"><img name="selDay1" alt='导出PDF' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_pdf.gif" width=30 height=32 border="0"></a>
          &nbsp;&nbsp;&nbsp;
          <a href="<%=rootPath%>/inform.do?action=network_excel&day=<%=day%>&field=<%=field%>" target="_blank"><img name="selDay1" alt='导出EXCEL' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=30 height=32 border="0"></a>
       </td>      
    </tr>
</table><br>
<!--**************************************服务器报表******************************************-->
<table width="90%" align="center" border="0" height="24">
  <tr><th align='center'><%=report.getHead()%></th></tr>
  <tr><td align='center'><%=report.getTable()%></td></tr>
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