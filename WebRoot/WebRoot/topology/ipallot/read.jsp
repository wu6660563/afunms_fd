<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.common.util.ShareData"%>
<%@page import="java.util.*"%>

<%
	String[] value = (String[])request.getAttribute("value");
	String key = (String) request.getAttribute("key");
	
	String rootPath = request.getContextPath();
%>
<html>
	<head>
		<title>ip分配信息</title>
		<script>
		function toWord(){
		window.location.href = "<%=rootPath%>/ipallot.do?action=readword&key=<%=key%>"
		}
</script>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="Pragma" content="no-cache">
		<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
	</head>
	<body id="body" class="body"
		background="<%=rootPath%>/resource/image/bg6.jpg">
		<form id="mainForm" method="post" name="mainForm">
			<table>
				<tr>
					<td width="16"></td>
					<td align="center">
						<br>
						<table width="100%" border=0 cellpadding=0 cellspacing=0>
							<tr>
								<td>
									<table id="content-header" class="content-header">
										<tr>
											<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
											<td class="content-title">资源 >> IP/MAC资源 >> IP分配统计 </td>
											<td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
									<table>
										<tr>
											<td class="body-data-title" style="text-align: left;">&nbsp;&nbsp;<b>[<font color="#397DBD">列表展示</font>]</b></td>
											<td class="body-data-title" style="text-align: right;">
												<a href="<%=rootPath%>/ipallot.do?action=downloadnetworklistfuck&key=<%=key%>" target="_blank"><img name="selDay1" alt='导出EXCEL' style="CURSOR: hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=18 border="0">导出EXCEL</a>&nbsp;&nbsp;&nbsp;&nbsp;
												<a href="#" onclick="toWord()"><img name="selDay1" alt='导出word' style="CURSOR: hand" src="<%=rootPath%>/resource/image/export_word.gif" width=18 border="0">导出WORD</a>&nbsp;&nbsp;&nbsp;&nbsp;
												<a href="<%=rootPath%>/ipallot.do?action=reportlist&key=<%=key%>">生成报表</a>&nbsp;&nbsp;&nbsp;&nbsp;
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<table cellpadding="0" cellspacing="1" width="100%" id="detail-content-body" class="detail-content-body">
									<tr style="background-color: #FFFFFF;">
										<td align="center" class="body-data-title" width="15%">序号</td>
										<td align="center" class="body-data-title">${key }.*网段已分配IP</td>
									</tr>
									<%
									
									
										for (int i = 0; i < value.length; i++) {
									%>
									<tr style="background-color: #FFFFFF;">
										<td align="center" class="body-data-list" value="<%=value[i]%>"><font color='blue'><%=i + 1%></font></td>
										<td align="center" class="body-data-list"><%=value[i]%></td>
									</tr>
									<%
										}
									%>
								</table>
							</tr>
							<tr>
								<td width="16"></td>
								<td align="center">
									<table>
										<tr>
											<td></td>
										<br>
										</tr>
									</table>
								</td>
							</tr>
							<table border=4 bordercolor="#000000">
								<tr>
									<td width="16" bordercolor="#FFFFFF"></td>
									<td align="center">
										<table cellspacing=5 cellpadding=1>
											<tr>
												<td align="center" ><b>${key }.*网段已分配IP</b></td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td width="16" bordercolor="#FFFFFF"></td>
									<td align="center">
										<table cellspacing=5 cellpadding=1>
											<tr>
												<td width=20 align="center" class="detail-content-body" style="background-color: #00FF00;"></td>
												<td ><font color='blue'>未分配</font></td>
												<!-- 
												<td width=20 align="center" class="detail-content-body" style="background-color: #0000EE;"></td>
												<td><font color='blue'>已分配在线</font></td> -->
												<td width=20 align="center" class="detail-content-body" style="background-color: #808069;"></td>
												<td><font color='blue'>已分配</font></td>
											</tr>
										</table>
										<table width=400 cellspacing=4 cellpadding=1 border=1>
											<tr style="background-color: #FFFFFF;">
												<td align="center" class="body-data-list"></td>
											</tr>
											<tr style="background-color: #FFFFFF;">
												<%
													String str = null;
													String aa = null;//最后一位ip值
													int k = 0;
													int tmp = 20;
													for (int i = 0; i < 255; i++) {
														String ys = "#00FF00  ";
														String ip = key+"."+i;
														for (int j = 0; j < value.length; j++) {
															str = value[j];
															String pingvalue = "0";
															k = str.lastIndexOf(".");
															aa = str.substring(k + 1, str.length());
															if (aa.equals(i + "")) {
															    if((pingvalue!=null&&"0".equals(pingvalue))||pingvalue==null||"null".equalsIgnoreCase(pingvalue)){
															        ys = "#808069";
															    } else {
															        ys = "#0000EE";
															    }
																break;
															}
														}
														if (i % tmp != 0) {
												%>
												<td align="center" class="detail-content-body" style="background-color: <%=ys%>;cursor:hand;" onClick='window.open("<%=rootPath%>/topology/ipallot/ping.jsp?ipaddress=<%=ip%>","oneping", "height=400, width= 500, top=300, left=100")'><%=i%></td>
												<%
													} else {
												%>
											<tr style="background-color: #FFFFFF;">
												<td align="center" class="detail-content-body" style="background-color: <%=ys%>;cursor:hand;" onClick='window.open("<%=rootPath%>/topology/ipallot/ping.jsp?ipaddress=<%=ip%>","oneping", "height=400, width= 500, top=300, left=100")'><%=i%></td>
												<%
													}
													}
												%>
											</tr>
										</table>
								</tr>
							</table>
							<tr style="background-color: #FFFFFF;">
								<td width="16"></td>
								<table cellspacing=5 cellpadding=1>
									<td align="center">
									<td nowrap colspan="4" align="center">
										<br><br><br><br><br>
										<input type="reset" style="width: 50" value="关闭" onclick="javascript:window.close()">
									</td>
								</table>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>


