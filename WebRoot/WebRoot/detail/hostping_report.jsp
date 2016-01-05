<%@ page language="java" contentType="text/html; charset=GBK" %>
<%@ page import="java.util.*"%>
<%@ include file="/include/globe.inc"%>

<%
	
	Hashtable imgurl = (Hashtable)request.getAttribute("imgurl");
	String rootPath = request.getContextPath(); 
	String id = (String)request.getAttribute("id");
	String timeType = (String)request.getAttribute("timeType");
	String startdate = (String)request.getAttribute("startdate");
	String todate = (String)request.getAttribute("todate");
	
  	
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html> 
<head>
<title>dhcnms</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="-1">

<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="<%=rootPath%>/include/list.js"></script>
<script language="JavaScript">var tabImageBase = "<%=rootPath%>/chart/report/tabs";</script> 
<script language="JavaScript" src="<%=rootPath%>/chart/js/dhtml.js"></script> 
<script language="JavaScript" src="<%=rootPath%>/chart/js/graph.js"></script> 
<script language="JavaScript" src="<%=rootPath%>/include/print.js"></script>
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>


<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script language="JavaScript" src="<%=rootPath%>/include/validation.js"></script>
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 
<script language="JavaScript" fptype="dynamicoutline">


function query(){
	
	mainForm.action = "<%=rootPath%>/monitor.do?action=hostping_report&id=<%=id%>";
	mainForm.submit();
}
function checkdetail(){
	subform = document.forms[0];
	subform.operate.value = "show_utilhdx";
  subform.submit();
}
function closewin(){
	window.close();
}
function openwin(operate,category,entity,subentity) 
{	subform = document.forms[0];
  var ipaddress = subform.ipaddress.value;
  var equipname = subform.equipname.value;
  window.open ("MonitoriplistMgr.do?operate="+operate+"&category="+category+"&entity="+entity+"&subentity="+subentity+"&ipaddress="+ipaddress+"&equipname="+equipname, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes") 
}
</script>
</head>

<body class="WorkWin_Body">
<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
<form method="post" name="mainForm">
 

<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
 
  <tr>
    	<td width="100%" height="23">连通率
    	开始日期
		<input type="text" name="startdate" value="<%=startdate%>" size="10">
	<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
	<img id=imageCalendar1 align=absmiddle width=34 height=21 src=<%=rootPath%>/include/calendar/button.gif border=0></a>
	
		截止日期
		<input type="text" name="todate" value="<%=todate%>" size="10"/>
	<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
	<img id=imageCalendar2 align=absmiddle width=34 height=21 src=<%=rootPath%>/include/calendar/button.gif border=0></a>&nbsp;&nbsp;时间粒度
	<select id="timeType" name="timeType">
			<option  value="minute">分</option>
			<option  value="hour">时</option>
    		<option  value="day">天</option>
    		<option value="month">月</option>
    	</select>
    	<input type="button" value="确定" onclick="query()">
    	</td>
    	
    </tr>
  <tr> 
    <td><i>
	    <table width="100%" border="1" cellpadding="0" cellspacing="0" bordercolor="98C4F1" id="table2">
	   
	  	<tr>
			
			<td bgcolor="#FFFFFF" align=center><img src='<%=rootPath%>/<%=imgurl.get("ConnectUtilization").toString()%>?temp=<%=Math.random()%>'></td>
		
		</tr>
	        
	
	      </table>
      </i></td>
  </tr>
</table>
    </form>
</body>
<script>
	function unionSelect(){
		var timeType = document.getElementById("timeType");
		timeType.value = "<%=timeType%>";
	}
	unionSelect();
</script>
</html> 