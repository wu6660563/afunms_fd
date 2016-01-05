<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.system.util.CreateMenu"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.system.model.Function"%>
<%
	List<Function> menuRoot = (List<Function>)request.getAttribute("menuRoot");
  User user = (User)session.getAttribute(SessionConstant.CURRENT_USER); //当前用户
  if(user==null)
     response.sendRedirect("/common/error.jsp?errorcode=3003");

  String rootPath = request.getContextPath();
  CreateMenu menu = new CreateMenu(rootPath);
%>
<html>
  <head>
<title>综合网络管理系统</title>
<LINK rel=stylesheet href="<%=rootPath%>/resource/css/login.css" type="text/css" >
<link rel="stylesheet" href="<%=rootPath%>/resource/css/xmenu.css" type="text/css">
<script type="text/javascript" src="<%=rootPath%>/resource/js/cssexpr.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/xmenu.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/custom_menu.js"></script>
<script language="JavaScript" type="text/javascript">

  function toHome()
  {
     parent.mainFrame.location = "<%=rootPath%>/common/home.jsp";
  }

  function doQuit()
  {
     if (confirm("你真的要退出吗?"))
     {
         parent.location = "<%=rootPath%>/user.do?action=logout";
     }
  }

  function setPerson()
  {
     parent.mainFrame.location = "<%=rootPath%>/system/user/inputpwd.jsp";     
  }
</script>

<script>
   function menu(){
   menuFrame.location.reload();
   }

</script>


</HEAD>
<body  leftmargin="0" topmargin="0" onload="window.open('test.jsp','声音控制窗口','height=50,width=100,top=0,left=0,toolbar=no, menubar=no ,scrollbars=no, resizable=no, location=no,status=no');">
<table border='0'>
  <tr>
  	<td><img src="<%=rootPath%>/resource/image/top11.jpg" width="540" height="75"></td>
  	<td width="36%" valign="top"><img src="<%=rootPath%>/resource/image/yemian_03.gif" width="331" height="75"></td>
  </tr>
</table>
<table border='0'>
  <tr valign=bottom>
    <td align='center' width=100% valign=bottom>
    
											<table cellspacing="1" cellpadding="0" bgcolor=#397DBD width=60%>
                        								                    										                        								
                    										<tr bgcolor="DEEBF7" height=25>                      														
                      											
                      											<%for(int i = 0 ; i < menuRoot.size() ; i++){ %>
                      											<td height="28" align="center" bordercolor="#397DBD" bgcolor="#D1DDF5" width="15%"><a href="<%=rootPath%>/<%=menuRoot.get(i).getUrl() %>" target=mainFrame><b><%=menuRoot.get(i).getCh_desc() %></b></a></td>
                      											<%} %>
                    										</tr>                     										                      								
            										
            										</table>
   </td>
    <td align='right'><input type="button" class="button" value="个人设置" onclick="setPerson()"><input type="button" class="button" value="系统首页" onclick="toHome()"><input type="button" class="button" value="退出系统" onclick="doQuit()"></td>
  </tr>
  <tr valign=top>
  <td bgcolor=#397DBD colspan=2 height=2 valign=top>
  </td>
  </tr>
  <!--<tr>
    <td align='center' width="80%"><%=menu.getMenus(user.getRole())%></td>
    <td align='right'><input type="button" class="button" value="个人设置" onclick="setPerson()"><input type="button" class="button" value="系统首页" onclick="toHome()"><input type="button" class="button" value="退出系统" onclick="doQuit()"></td>
  </tr>-->
</table>

<iframe id="mainFrame" name="mainFrame" width="100%" marginwidth="0" marginheight="0" frameborder="NO" frameborder="0" height="100%" src='<%=rootPath%>/common/home.jsp' onload="height=mainFrame.document.body.scrollHeight" ></iframe>

</BODY>

</html>
