<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.model.PingData"%>
<%@ include file="/include/globe.inc"%>
<%  
   String rootPath = request.getContextPath();  
   List list = (List)request.getAttribute("list");     
   String startdate = (String)request.getAttribute("startdate");
   String todate = (String)request.getAttribute("todate");
   String id = (String)request.getAttribute("id");
%>
<html>
<head>
<base target="_self">
<meta http-equiv="Pragma" content="no-cache">
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 
<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
<script language="javascript">
function query()
{
    mainForm.action = "<%=rootPath%>/netPing.do?action=detail";
    mainForm.submit(); 
}

</script>
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#9FB0C4">
<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
<form name="mainForm" method="post">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=350px>
	<tr>
		<td bgcolor="#cedefa" align="center" valign=top>
		<table width="500px" border=0 cellpadding=0 cellspacing=0>
				<tr>
					<td height="28" align="left" bordercolor="#397DBD" bgcolor="#397DBD" class="txtGlobalBold">&nbsp;&nbsp;添加设备节点</td>
				</tr>
			<tr>
				<td bgcolor="#FFFFFF" valign="top">
				 <table border="1" id="table1" cellpadding="0" cellspacing="1" width="100%" align="center">
					<TBODY>
			        <tr>						
  						<td colspan=5 height="28" bgcolor="#ECECEC">
								开始日期
								<input type="text" name="startdate" value="<%=startdate%>" size="10">
							<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
							<img id=imageCalendar1 align=absmiddle width=34 height=21 src=<%=rootPath%>/include/calendar/button.gif border=0></a>
							
								截止日期
								<input type="text" name="todate" value="<%=todate%>" size="10"/>
							<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
							<img id=imageCalendar2 align=absmiddle width=34 height=21 src=<%=rootPath%>/include/calendar/button.gif border=0></a>
							<input type="button" name="submitss" value="查询" onclick="query()">
						</td>
			</tr>
			<tr>						
				<td class="detail-data-body-title">&nbsp;</td>
				<td width="10%" class="detail-data-body-title"><strong>类型</strong></td>
		        <td width="20%" class="detail-data-body-title"><strong>IP</strong></td>
		    	<td width="40%" class="detail-data-body-title"><strong>事件描述</strong></td>
				<td class="detail-data-body-title"><strong>告警时间</strong></td>
			</tr>
			<%
				int index = 0;
  				for(int i=0;i<list.size();i++){
 					index++;
 					PingData vo = (PingData)list.get(i);
				  	String ip = vo.getIp();
				  	String content = vo.getContent();
				  	String times = String.valueOf(vo.getTime());
  					String status = vo.getState();
%>

 <tr bgcolor="#FFFFFF"  <%=onmouseoverstyle%>>

    <td class="detail-data-body-list">&nbsp;<%=index%></td>
       <%
    	if("0".equals(status)){
       %>
      <td style="border-left:1px solid #EEEEEE;border-bottom:1px solid #EEEEEE;" bgcolor=red align=center>事件&nbsp;</td>
       <%
       } else {
       %>
       <td style="border-left:1px solid #EEEEEE;border-bottom:1px solid #EEEEEE;" bgcolor=green align=center>提示&nbsp;</td>
       <%
       }
       %>    
      <td class="detail-data-body-list">
      <%=ip%></td>
      <td class="detail-data-body-list">
      <%=content%></td>
       <td class="detail-data-body-list">
      <%=times%></td>
 </tr>
 <%}%>  
			<tr>
				<TD nowrap colspan="5" align=center>
					<br>
					<input type="hidden" name='node' value="<%=id%>"/>
					<input type="button" value="关闭" style="width:50" class="formStylebutton" onclick="window.close();">
				</TD>	
			</tr>
			</TBODY>
				</TABLE>				
				</td>
			</tr>			
		</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>