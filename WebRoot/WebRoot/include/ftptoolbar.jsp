<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.config.dao.IpaddressPanelDao"%>
<%@page import="com.afunms.config.model.IpaddressPanel"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.config.dao.PanelModelDao"%>
<%@page import="com.afunms.config.model.PanelModel"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.topology.dao.HostNodeDao"%>
<%@page import="com.afunms.application.model.FTPConfig"%>
<%
  String rootPath = request.getContextPath();
  String id = request.getParameter("id");
  String dbPage = request.getParameter("dbPage");
  String subtype = request.getParameter("subtype");
  String toolsubtype = "";
  String nodeid = id;
  FTPConfig queryconf = (FTPConfig)request.getAttribute("initconf");
  String ipaddress=queryconf.getIpaddress();
%>

<script type="text/javascript">

</script>

														<table class="tool-bar">
				<tr>
					<td>
						<table class="tool-bar-header">
		                	<tr>
			                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
			                	<td class="tool-bar-title">����</td>
			                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
			       			</tr>
			        	</table>
					</td>
				</tr>
				<tr>
					<td>
						<table class="tool-bar-body">
							<tr>
								<td>
									<table class="tool-bar-body-list">
										<tr>
											<td>
												<ul>
													<li><img src="<%=rootPath%>/resource/image/toolbar/ping.gif">&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/FTP.do?action=isOK&id=<%=id%>","oneping", "height=200, width= 500, top=300, left=100")'>�����Լ��</a></li>
													<li><img src="<%=rootPath%>/resource/image/topo/button_refresh_bg.gif">&nbsp;<a href="#"  id="process" >����ͬ��</a></li>
													
												</ul>
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
						<table class="detail-content-footer">
							<tr>
								<td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
             										<td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
							</tr>
							
						</table>
					</td>
				</tr>
			</table>
			
			<table class="tool-bar">
				<tr>
					<td>
						<table class="tool-bar-header">
		                	<tr>
			                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
			                	<td class="tool-bar-title">���ܼ�������</td>
			                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
			       			</tr>
			        	</table>
					</td>
				</tr>
				<tr>
					<td>
						<table class="tool-bar-body">
							<tr>
								<td>
									<table class="tool-bar-body-list">
										<tr>
											<td>
												<ul>
													<li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/nodeGatherIndicators.do?action=list&nodeid=<%=nodeid%>&type=service&subtype=ftp","oneping", "height=600, width= 1000, top=300, left=100,scrollbars=yes")'>���ܼ���������</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif">&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/alarmIndicatorsNode.do?action=list&nodeid=<%=nodeid%>&type=service&subtype=ftp","oneping", "height=600, width= 1000, top=300, left=100,scrollbars=yes")'>ָ�귧ֵ����</a></li>
												</ul>
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
						<table class="detail-content-footer">
							<tr>
								<td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
             										<td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
							</tr>
							
						</table>
					</td>
				</tr>
			</table>
			
			<table class="tool-bar">
				<tr>
					<td>
						<table class="tool-bar-header">
		                	<tr>
			                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
			                	<td class="tool-bar-title">�������</td>
			                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
			       			</tr>
			        	</table>
					</td>
				</tr>
				<tr>
					<td>
						<table class="tool-bar-body">
							<tr>
								<td>
									<table class="tool-bar-body-list">
										<tr>
											<td>
												<ul>
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif">&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/FTP.do?action=showPingReport&ipaddress=<%=ipaddress%>&id=<%=nodeid%>" ,"portScanWindow","width=850,height=400,left=120,top=100,scrollbars=yes,resizable=yes")'>�����Ա���</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif">&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/FTP.do?action=showCompositeReport&ipaddress=<%=ipaddress%>&id=<%=nodeid%>","portScanWindow","width=850,height=400,left=120,top=100,scrollbars=yes,resizable=yes")'>�ۺϱ���</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif">&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/FTP.do?action=showServiceEventReport&ipaddress=<%=ipaddress%>&id=<%=nodeid%>","portScanWindow","width=950,height=450,left=120,top=100,scrollbars=yes,resizable=yes")'>�¼�����</a></li>
												</ul>
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
						<table class="detail-content-footer">
							<tr>
								<td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
             										<td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
							</tr>
							
						</table>
					</td>
				</tr>
			</table>