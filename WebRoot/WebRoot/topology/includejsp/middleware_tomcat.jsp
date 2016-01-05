<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@page import="com.afunms.polling.node.Tomcat"%>
<%@page import="com.afunms.common.util.*"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.application.manage.TomcatManager"%>
<%@ page import="com.afunms.event.model.EventList"%>
<%@ page import="com.afunms.detail.service.tomcatInfo.TomcatInfoService"%>
<%@page import="com.afunms.initialize.*"%>
<%
	String rootPath = request.getContextPath();
	String nodeid = request.getParameter("nodeid");
    String name = request.getParameter("name") == null ? ""
			: request.getParameter("name");
    String alarmLevel = request.getParameter("alarmLevel") == null ? "0"
			: request.getParameter("alarmLevel");
    String ipaddress = request.getParameter("ipaddress") == null ? "0"
            : request.getParameter("ipaddress");
    String port = request.getParameter("port") == null ? "0"
            : request.getParameter("port");
    String tomcatVersion = request.getParameter("tomcatVersion") == null ? "0"
            : request.getParameter("tomcatVersion");
    String JVMVersion = request.getParameter("JVMVersion") == null ? "0"
            : request.getParameter("JVMVersion");
    String JVMVender = request.getParameter("JVMVender") == null ? "0"
            : request.getParameter("JVMVender");
    String OSName = request.getParameter("OSName") == null ? "0"
            : request.getParameter("OSName");
    String OSVersion = request.getParameter("OSVersion") == null ? "0"
            : request.getParameter("OSVersion");
    String ping = request.getParameter("ping") == null ? "0"
            : request.getParameter("ping");
    String curJVMImg = request.getParameter("curJVMImg") == null ? "0"
            : request.getParameter("curJVMImg");
            
    StringBuffer dataStr = new StringBuffer();
	dataStr.append("连通;").append(Math.round(Double.valueOf(ping))).append(
			";false;7CFC00\\n");
	dataStr.append("未连通;").append(100 - Math.round(Double.valueOf(ping))).append(
			";false;FF0000\\n");        
    String avgdata = dataStr.toString();
            
%>
<table id="service-detail-content" class="service-detail-content">
	<tr>
		<td>
			 <jsp:include page="/topology/includejsp/detail_content_top.jsp">
			 	<jsp:param name="contentTitle" value="Tomcat 信息"/> 
			 </jsp:include>
		</td>
	</tr>
	<tr>
		<td>
			<table id="service-detail-content-body"
				class="service-detail-content-body">
				<tr>
					<td>
						<table style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa
							rules=none align=center border=1 cellpadding=0 cellspacing="0"
							width=100%>
							<tr>
								<td width="80%" align="left" valign="top">
									<table>
										<tr>
											<td width="30%" height="29" align="left" nowrap
												class=txtGlobal>
												&nbsp;名称:
											</td>
											<td width="70%"><%=name%></td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td height="29" class=txtGlobal align="left" nowrap>
												&nbsp;状态:
											</td>
											<td>
												<img
													src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(Integer.valueOf(alarmLevel))%>">
												&nbsp;<%=NodeHelper.getStatusDescr(Integer.valueOf(alarmLevel))%>
											</td>
										</tr>
										<tr>
											<td height="29" align=left nowrap class=txtGlobal>
												&nbsp;IP地址:
											</td>
											<td><%=ipaddress%></td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td height="29" class=txtGlobal align="left" nowrap>
												&nbsp;端口:
											</td>
											<td><%=port%></td>
										</tr>
										<tr>
											<td height="29" align=left nowrap class=txtGlobal>
												&nbsp;Tomcat版本:
											</td>
											<td><%=tomcatVersion%></td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td height="29" align=left nowrap class=txtGlobal>
												&nbsp;JVM版本:
											</td>
											<td><%=JVMVersion%></td>
										</tr>
										<tr>
											<td height="29" align=left nowrap class=txtGlobal>
												&nbsp;JVM供应商:
											</td>
											<td><%=JVMVender%></td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td height="29" align=left nowrap class=txtGlobal>
												&nbsp;服务器操作系统:
											</td>
											<td><%=OSName%></td>
										</tr>
										<tr>
											<td height="29" nowrap class=txtGlobal>
												&nbsp;操作系统版本:
											</td>
											<td><%=OSVersion%></td>
										</tr>
									</table>
								</td>
								<td width=20% align="center">
									<table class="container-main-service-detail-tool">
										<tr>
											<td>
												<table style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa
													rules=none align=center border=1 cellpadding=0
													cellspacing="0" width=100%>
													<tr bgcolor=#F1F1F1 height="26">
														<td align="center">
															今日连通率
														</td>
													</tr>
													<tr>
														<td align=center>
															<div id="avgping">
																<strong>You need to upgrade your Flash Player</strong>
															</div>
															<script type="text/javascript"
																src="<%=rootPath%>/include/swfobject.js"></script>
															<script type="text/javascript">
						                                       var so = new SWFObject("<%=rootPath%>/amchart/ampie.swf", "ampie","160", "155", "8", "#FFFFFF");
						                                           so.addVariable("path", "<%=rootPath%>/amchart/");
						                                           so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/pingStatepie.xml"));
						                                           so.addVariable("chart_data","<%=avgdata%>");
						                                           so.write("avgping");
					                                        </script>
														</td>
													</tr>
													<tr>
														<td height="7" align=center>
															<img src="<%=rootPath%>/resource/image/Loading_2.gif">
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>

											<td>
												<table style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa
													rules=none align=center border=1 cellpadding=0
													cellspacing="0" width=100%>
													<tr bgcolor=#F1F1F1 height="26">
														<td align="center">
															JVM当前利用率
														</td>
													</tr>
													<tr height=160>
														<td align="center">
															<img src="<%=curJVMImg %>">
														</td>
													</tr>
													<tr height="7">
														<td align=center>
															&nbsp;
															<img src="<%=rootPath%>/resource/image/Loading.gif">
														</td>
													</tr>
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
		</td>
	</tr>
	<tr>
		<td>
			 <jsp:include page="/topology/includejsp/detail_content_footer.jsp"/>
		</td>
	</tr>
</table>