<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.*"%>
<%
	String rootPath = request.getContextPath();
	String[] value = (String[])request.getAttribute("value");
	String key = (String) request.getAttribute("key");
	String menuTable = (String) request.getAttribute("menuTable");
	JspPage jp = (JspPage) request.getAttribute("page");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<style type="text/css">
#divcenter {
	MARGIN-RIGHT: auto;
	MARGIN-LEFT: auto;
}
</style>
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
		<script type="text/javascript">
		</script>
	</head>
	<body style="background:url(resource\skin1\image\bg.gif)">
		<form id="mainForm" method="post" name="mainForm">
			<table id="body-container" class="body-container">
				<tr>
					
					<td class="td-container-main">
						<table align="center">
							<tr>
								<td class="td-container-main" width="100%" style="BORDER-right: #C0C0C0 1px solid; padding: 0px, 7px, 0px, 10px;">
									<table id="content-header" class="content-header">
										<tr>
											<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
											<td class="content-title">资源 &gt;&gt; IP/MAC资源 &gt;&gt; IP分配信息报表</td>
											<td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr style="align:center;width:100%">
								<td  style="align:center;" class="td-container-main" width="100%" align="center" style="padding: 0px, 7px, 0px, 10px;align:center;">
				            		<table id="content-body" class="content-body" style="align:center;width:100%">
										<tr style="align:center;">
											<td width="50%" style="padding: 23px, 0px, 80px, 0px;width:50%"  align="center">
												<table style="width:80%" width="50%"  align="center" border="1" bordercolor="#000000" cellspacing="0" cellpadding="0" bgcolor="#ffffff">
													<tr height=40>
														<td colspan="6" align="center" style="font-size: 16px;"><b>${key }.*段分配信息报表<b></td>
													</tr>
										<tr height=28>
											<td align="center"><B>序号</td>
											<td align="center"><B>IP</td>
										</tr>
										<%
											if (value != null && value.length > 0) {
												for (int i = 0; i < value.length; i++) {
												
										%>
										<tr style="background-color: <% if(i%2==0){%>#ECECEC;<%}else{%>#ffffff;<%} %>" height=28>
											<td align="center"><%=i + 1%></td>
											<td align="center"><%=value[i]%></td>
										</tr>
										<%
											}
											}
										%>
										<%
											if (( value.length -15)<0) {
												for (int i = value.length; i <(15- value.length); i++) {
													
										%>
										<tr  style="background-color: <% if(i%2==0){%>#ECECEC;<%}else{%>#ffffff;<%} %>" height=28>
											<td>&nbsp;</td>
											<td>&nbsp;</td>
										</tr>
										<%
											}
											}
										%>	
									</table>
								</td>
								</tr>
								</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
