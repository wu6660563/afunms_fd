<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%
    String rootPath = request.getContextPath();
	session.setAttribute(SessionConstant.CURRENT_MENU,request.getParameter("menu"));
	//-----------�ж�ȫ����ʾ״̬----------------
	String fullScreen = request.getParameter("fullscreen");
	if (fullScreen == null || fullScreen.equals("0")) {
		fullScreen = "0";
		out.println("<script type=\"text/javascript\"> var fullscreen = 0; </script>");
	} else {
	// �����ȫ����ʾ���޸� viewWidth
		fullScreen = "1";
		out.println("<script type=\"text/javascript\"> var fullscreen = 1;");
		out.println("viewWidth = window.screen.width;</script>");
	}
%>	   
<html xmlns:v="urn:schemas-microsoft-com:vml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>��ͨ����ͼ</title>
<script type="text/javascript" src="js/profile.js"></script>
<script type="text/javascript">
	var fullscreen = <%=fullScreen%>;
	if(fullscreen == "1")
	  viewWidth = window.screen.width;      
	function changeFlags() 
	{
		if (fullscreen == 0)
		   window.parent.changeFlags();
		else
		   window.moveTo(0, 0);
	}			
</script>
</head>
<frameset id="parentFrame" rows="30,600" frameborder="no" border="0" framespacing="0">
<frame src="<%=rootPath%>/topology/submap/pingTopFrame.jsp?fullscreen=<%=fullScreen%>" name="topFrame" scrolling="No" noresize="noresize" id="topFrame" title="������" />
<frame src="<%=rootPath%>/topology/submap/pingShowMap.jsp?fullscreen=<%=fullScreen%>" name="mainFrame" scrolling="No" noresize="noresize" id="mainFrame" title="��ͨ����ͼ" />
</frameset>
</html>