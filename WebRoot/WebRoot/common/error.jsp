<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.base.ErrorMessage"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%
  String ec = request.getParameter("errorcode");
  int errorcode = 0;
  if(ec!=null)
     errorcode = Integer.parseInt(ec);

  String errorInfo = null;
  if(errorcode==-1) //直接取错误信息
     errorInfo = (String)request.getAttribute(SessionConstant.ERROR_INFO);
  else
     errorInfo = ErrorMessage.getErrorMessage(errorcode);
  String rootPath = request.getContextPath();     
%>
<html>
<head>
<title></title>
<script language="JavaScript" type="text/javascript">
  function toLogin()
  {
     window.location="<%=rootPath%>/index.jsp";
  }
</script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
</head>
<body>
<br><br><br>
<table width="590" border=1 cellspacing=0 cellpadding=0 bordercolorlight='#000000' bordercolordark='#FFFFFF'
align='center'>
  <tr>
    <td width="100%" align="center" height="30">
    <b>错误</b></td>
  </tr>
  <tr class="othertr">
    <td width="100%" align="center" height="70" background="<%=rootPath%>/resource/image/error.jpg">
      <font color="red"><%=errorInfo%></font>
    </td>
  </tr>
  <tr>
    <td width="100%" align="center" height="30">
    <%
    	if("对不起,用户名或密码不正确!".equals(errorInfo)){
    %>
      <input type="button" class="button" value="返回" name="B2" onclick="toLogin();">
      <%
      }else{
      %>
      <input type="button" class="button" value="返回" name="B2" onclick="javascript:history.back(1)">
      <%
      }
      %>
    </td>
  </tr>
</table>
</body>
</html>
