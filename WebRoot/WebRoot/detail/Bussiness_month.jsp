<%@ page language="java" contentType="text/html; charset=gb2312" %>
<%@ include file="/include/globe.inc"%>
<%
	String rootPath = request.getContextPath(); 
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html> 
<head>
<title>业务连通性/响应时间</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">

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
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script> 
</head>
<%
String bid = (String)request.getParameter("bid");
String id = (String)request.getParameter("id");
String name = (String)request.getParameter("name");
String sss = new String(name.getBytes("ISO8859-1"),"utf-8");
System.out.print(sss);
%>
<body class="WorkWin_Body" onload="window.resizeTo(850,530);">
<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
<form method="post" name="mainForm">

<table width="760" border="0" align="center" cellpadding="0" cellspacing="0">
  
  <tr> 
    <td colspan="10">
    <table width="100%" cellpadding="0" cellspacing="0" bordercolor="98C4F1" id="table2">
        <tr> 
          <td><div align="center">
              <table cellpadding="0" cellspacing="0" width=98%>
              							<tr> 
                							<td width="100%" > 
                								<div id="flashcontent1">
													<strong>You need to upgrade your Flash Player</strong>
												</div>
												<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Bus_Resp_Ping.swf?bid=<%=bid%>&id=<%=id%>&name=<%=sss%>", "Bus_Resp_Ping", "800", "250", "8", "#ffffff");
													so.write("flashcontent1");
												</script>				
							                </td>
										</tr>             
									</table>
            </div>
            </td>
        </tr>
      </table>
      </td>
  </tr>
</table>
    </form>
</body>
</html> 