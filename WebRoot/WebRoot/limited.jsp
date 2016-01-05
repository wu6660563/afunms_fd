<%@page language="java" contentType="text/html;charset=GB2312"%>
<%
  String rootPath = request.getContextPath();
%>
<HTML>
<head>
<TITLE>综合网络管理系统</TITLE>
<META http-equiv=Content-Type content="text/html; charset=GBK"/>
<LINK href="resource/css/login.css" type="text/css" rel=stylesheet>
<META content="MSHTML 6.00.2800.1106" name=GENERATOR/>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	background-image: url(<%=rootPath%>/resource/image/bg.jpg);
	font-size:12px;
	color:#000000;
}
.botton_login {	
	background-image: url(<%=rootPath%>/resource/image/login_button.gif);
	width: 53px;
	height:20px;
	border: none;
	padding-top: 2px;
	background-repeat: no-repeat;
}
.input_text {	background-color: #f2f1f1;
	height: 16px;
	width: 120px;
	padding-left: 2px;
	border: 1px solid #828485;
}
-->
</style>
<script language="javascript">
function doLogin()
{
    if(mainForm.userid.value=="")
    {
       alert("请输入用户名!");
       mainForm.userid.focus();
       return false;
    }
    else if(mainForm.password.value=="")
    {
       alert("请输入密码!");
       mainForm.password.focus();
       return false;
    } 
}
</script>
</head>

<body>
<table width="635" height="500" border="0"  align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td valign=middle ><img src="<%=rootPath%>/resource/image/limited.jpg">
    </td>
  </tr>
</table>
</body>
</html>
