<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.dhcnms.application.model.Symantec"%>
<%@page import="com.dhcnms.application.dao.SymantecDao"%>
<%@page import="java.util.List"%>
<%
 String onMouse =" onmouseout=\"this.style.background='FFFFFF'\" onmouseover=\"this.style.background='#F0E288'\"";

 String beginDate = request.getParameter("bd");
 String virus = request.getParameter("virus");
 String machine = request.getParameter("machine");

 Symantec vo = new Symantec();
 vo.setBegintime(beginDate);
 vo.setVirus(virus);
 vo.setMachineIp(machine);

 SymantecDao dao = new SymantecDao();
 List vov = dao.selectByContent(vo);
 vo = null;
 String content = null;
 String rootPath = request.getContextPath();
%>
<html>
<head>
<title></title>
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<script language="JavaScript" type="text/javascript">

  function toFind()
  {
     FrmList.action = "list.jsp";
     FrmList.submit();
  }

  function toReport()
  {
     FrmList.action = "report.jsp";
     FrmList.submit();
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
<%
if(vov.size()==0)
{
%>
<table width="60%" border="0" align="center">
 <tr class="othertr">
   <td align="left">没有相关的日志记录</td>
   <td align="right"><a href="#" onclick="javascript:history.back(1)">返回</a></td>
</tr>
</table>
<%
  return;
}  
%>
<table width="60%" border="0" align="center">
 <tr class="othertr"><b>
<%
 if(machine!=null)
 {
%>
   <td align="left"><b><%=beginDate%>,<font color="red"><%=machine%></font>感染的病毒:</b></td>
<%
 }else{
%>
   <td align="left"><b><%=beginDate%>,感染 <font color="red"><%=virus%></font>的机器:</b></td>
<%
 }
%>
<td align="right"><a href="#" onclick="javascript:history.back(1)">返回</a></td></b>
</tr>
</table>
<table class="microsoftLook" cellspacing="1" cellpadding="0" width="600" align='center'>
<tr>
    <th width="20%" align="center">
<%
  if(machine!=null)
    out.print("病毒");
  else
    out.print("机器IP");
%></th>
    <th width="60%" align="center">被感染文件数</th>
  </tr>
<%
   for(int i=0;i<vov.size();i++)
   {
     vo = (Symantec)vov.get(i);
     if(machine!=null)
        content = vo.getVirus();
     else
        content = vo.getMachineIp();
%>
  <tr class="othertr" <%=onMouse%>>
    <td align="center" class="microsoftLook0"><%=content%></td>
    <td align="left" class="microsoftLook0"><%=vo.getVirusFile()%></td>
  </tr>
<%
   } //end_while
%>
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