<%@ page contentType="text/html; charset=gb2312"%>
<%@ page import="com.afunms.common.util.NetworkUtil"%>
<%
   String ip = request.getParameter("ip");
   String result = NetworkUtil.pingReport(ip);
   if(result!=null)
      result = result.replaceAll("Reply","<br>Reply");
   else
      result = "";
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<link href="lib/css/style.css" rel="stylesheet" type="text/css" />
<%
out.println("<title>ping "+ ip +"</title>");
%>
</head>
<body topmargin="0" leftmargin="0" bgcolor="#000000">
<script type="text/javascript">
<!--
var parent;

function closeDlg() {
	window.dialogArguments.parent.mainFrame.closeProcDlg();
	window.close();
}

window.dialogArguments.parent.mainFrame.closeProcDlg();
//-->
</script>
<br/>
<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0" style="border:#C0C0C0 1px solid;padding:8px;">
  <tr>
    <td align="left">
<%
out.print("<div style=\"font-size:14px;\"><font color=\"#C0C0C0\" style=\"font-size=\">" + result + "</font></div>");
%>
	</td>
  </tr>
</table>
<br/>
<center>
<input type="button" value=" 确  定 " class="button" onClick="javascript:closeDlg();">
</center>
</body>
</html>
