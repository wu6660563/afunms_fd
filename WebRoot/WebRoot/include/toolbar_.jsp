<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.config.dao.IpaddressPanelDao"%>
<%@page import="com.afunms.config.model.IpaddressPanel"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.config.dao.PanelModelDao"%>
<%@page import="com.afunms.config.model.PanelModel"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.topology.dao.HostNodeDao"%>
<%
  String rootPath = request.getContextPath();
  String tmp = (String)request.getParameter("tmp");
  HostNodeDao hostNodeDao = new HostNodeDao();
  HostNode node = hostNodeDao.loadHost(Integer.valueOf(tmp));
  hostNodeDao.close();
  String ipaddress = (String)request.getParameter("ipaddress");
  String subtype = (String)request.getParameter("subtype");
  String category = (String)request.getParameter("category");
  //String mypage = (String)request.getParameter("page");
  //System.out.println("toolbar.jsp#################################################"+category);
  String sys_oid = (String)request.getParameter("sys_oid");
  String mypage = (String)request.getParameter("page");
%>

<table class="container-main-tool">
	<tr>
		<td>
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
													<li><img src="<%=rootPath%>/resource/image/toolbar/qxgl.gif">&nbsp;<a href="<%=rootPath%>/network.do?action=menucancelmanage&id=<%=tmp%>">ȡ������</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/telnet.gif">&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/network.do?action=telnet&ipaddress=<%=ipaddress%>","onetelnet", "height=0, width= 0, top=0, left= 0")'>Telnet</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/ping.gif">&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/tool/ping.jsp?ipaddress=<%=ipaddress%>","oneping", "height=400, width= 500, top=300, left=100")'>Ping</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/lygz.gif">&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/tool/tracerouter.jsp?ipaddress=<%=ipaddress%>","newtracerouter", "height=400, width= 500, top=300, left=100")'>·�ɸ���</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/webssh.gif">&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/webutil/WebSSH.swf?MOID=<%=ipaddress%>","webssh", "height=555, width= 644, top=0, left=0")'>Web SSH</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/webtel.gif">&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/webutil/webtelnet.swf?MOID=<%=ipaddress%>","webtelnet", "height=555, width= 644, top=0, left=0")'>Web Telnet</a></li>
													<%
														if("network".equals(category)&&(node.getCollecttype()==1)){
	               											IpaddressPanelDao dao = new IpaddressPanelDao();
															IpaddressPanel panel = dao.loadIpaddressPanel(ipaddress);
															if(panel == null){
	               										%>
	               											<li><img src="<%=rootPath%>/resource/image/toolbar/snmpping.gif">&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/tool/remotePing.jsp?ipaddress=<%=ipaddress%>&community=<%=node.getWriteCommunity()%>","remotePing", "height=400, width=500, top=300, left=400")'>Snmp RemotePing</a></li><!-- snow add at 2010-5-28 -->
	               											<li><img src="<%=rootPath%>/resource/image/toolbar/sbmb.gif">&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/network.do?action=panelnodelist&jp=1&flag=1&id=<%=tmp%>&ipaddress=<%=ipaddress%>","panelfullScreenWindow", "toolbar=no,height=200,width=900" + ",scrollbars=no"+"screenX=0,screenY=0")'>�༭�豸���</a></li>
	               										<%
	               											}else{
	               												String filename = SysUtil.doip(ipaddress);
	               												String imageType = panel.getImageType();
	               												PanelModelDao panelModelDao = new PanelModelDao();
	               												PanelModel panelModel = panelModelDao.loadPanelModel(sys_oid,imageType);
	               												String pheight = panelModel.getHeight();
	               												String pwidth = panelModel.getWidth();
               											%>    
               												<li><img src="<%=rootPath%>/resource/image/toolbar/sbmb.gif">&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/panel/view/custom.jsp?filename=<%=filename%>&oid=<%=sys_oid%>&imageType=<%=imageType%>&ip=<%=ipaddress%>","panelfullScreenWindow", "toolbar=no,height=<%=pheight%>,width=<%=pwidth%>" + ",scrollbars=no"+"screenX=0,screenY=0")'>�豸���</a></li>
               										<%
               												}
               											}
         											%> 
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
		</td>
	</tr>
	<% if("network".equals(category)&&(node.getCollecttype()==1)){%>
	<tr>
		<td>
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
									<table id="tool-bar-body-list" class="tool-bar-body-list">
										<tr>
											<td>
												<ul>
													<li><img src="<%=rootPath%>/resource/image/toolbar/xgsbbq.gif" border=0>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/network.do?action=ready_editalias&id=<%=tmp%>&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")'>�޸��豸��ǩ</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/xgxtzsx.gif" border=0>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/network.do?action=ready_editsysgroup&id=<%=tmp%>&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")'>�޸�ϵͳ������</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/xg_snmp_cs.gif" width=16>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/network.do?action=ready_editsnmp&id=<%=tmp%>&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")'>�޸�SNMP����</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/ywfl.gif" width=16>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/vpntelnetconf.do?action=ipmenu&id=<%=tmp%>&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")'>Զ�̵�¼����</a></li>
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
		</td>
	</tr>
	<tr>
		<td>
			<table id="tool-bar" class="tool-bar">
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
						<table id="tool-bar-body" class="tool-bar-body">
							<tr>
								<td>
									<table id="tool-bar-body-list" class="tool-bar-body-list">
										<tr>
											<td>
												<ul>
													<li><img src="<%=rootPath%>/resource/image/menu/dkpz.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/portconfig.do?action=nodeportlist&&id=<%=tmp%>&ipaddress=<%=ipaddress%>","oneping", "height=400, width= 1000, top=300, left=100,scrollbars=yes")'>
													�˿�����</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/bfpzwj.gif" border=0>&nbsp;<a href="#" id="bkpCfg" onclick="">���������ļ�</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/dkpz.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/vpntelnetconf.do?action=readySetupConfig&&id=<%=tmp%>&ipaddress=<%=ipaddress%>&page=<%=mypage %>","oneping", "height=400, width= 500, top=300, left=100,scrollbars=yes")'>
													�·�����</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/pzwjlb.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/cfgfile.do?action=shownodelist&ipaddress=<%=ipaddress%>","oneping", "height=500, width= 800, top=300, left=100")'>
													�����ļ��б�</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/nodeGatherIndicators.do?action=list&nodeid=<%=tmp%>&type=net&subtype=<%=subtype%>","oneping", "height=600, width= 1000, top=300, left=100,scrollbars=yes")'>���ܼ���������</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/alarmIndicatorsNode.do?action=list&nodeid=<%=tmp%>&type=net&subtype=<%=subtype%>","oneping", "height=600, width= 1000, top=300, left=100,scrollbars=yes")'>����ָ�귧ֵ����</a></li>
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
						<table id="detail-content-footer" class="detail-content-footer">
							<tr>
								<td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
             					<td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<%} else{ %>
	<tr>
		<td>
			<table id="tool-bar" class="tool-bar">
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
						<table id="tool-bar-body" class="tool-bar-body">
							<tr>
								<td>
									<table id="tool-bar-body-list" class="tool-bar-body-list">
										<tr>
											<td>
												<ul>
													<li><img src="<%=rootPath%>/resource/image/menu/zjjcjs.gif" border=0>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/processgroup.do?action=list&nodeid=<%=tmp%>&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")'>���̼�������</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/service.gif" border=0 width=18>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/hostservicegroup.do?action=list&nodeid=<%=tmp%>&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")'>�����������</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/cpfzylb.gif" border=0 width=18>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/disk.do?action=toolbarlist&nodeid=<%=tmp%>&ipaddress=<%=ipaddress%>","diskWindow","toolbar=no,width=1000,height=500,directories=no,status=no,scrollbars=yes,menubar=no")'>���̼�������</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/glgz.gif" border=0 width=18>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/nodesyslogrule.do?action=toolbarfilter&nodeid=<%=tmp%>&ipaddress=<%=ipaddress%>","diskWindow","toolbar=no,width=1000,height=500,directories=no,status=no,scrollbars=yes,menubar=no")'>Syslog�澯����</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/nodeGatherIndicators.do?action=list&nodeid=<%=tmp%>&type=host&subtype=<%=subtype%>","oneping", "height=500, width= 1000, top=300, left=100,scrollbars=yes")'>���ܼ���������</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/alarmIndicatorsNode.do?action=list&nodeid=<%=tmp%>&type=host&subtype=<%=subtype%>","oneping", "height=500, width= 1000, top=300, left=100,scrollbars=yes")'>����ָ�귧ֵ����</a></li>
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
						<table id="detail-content-footer" class="detail-content-footer">
							<tr>
								<td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
             					<td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<%}%>
	<%
		if("network".equals(category)&&(node.getCollecttype()==1)&&1==2){
	%>
	<tr>
		<td>
			<table id="tool-bar" class="tool-bar">
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
						<table id="tool-bar-body" class="tool-bar-body">
							<tr>
								<td>
									<table id="tool-bar-body-list" class="tool-bar-body-list">
										<tr>
											<td>
												<ul>
													<li><img src="<%=rootPath%>/resource/image/menu/dkpz.gif" border=0>&nbsp;<a href="#" id="processport" onclick="#">�˿�����</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/bfpzwj.gif" border=0>&nbsp;<a href="#" id="process" onclick="#">���������ļ�</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/pzwjlb.gif" border=0>&nbsp;<a href="#" id="process1" onclick="#">�����ļ��б�</a></li>
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
						<table id="detail-content-footer" class="detail-content-footer">
							<tr>
								<td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
	            				<td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
							</tr>
							
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<%
		}
	%>
	
	<tr>
		<td>
			<table class="tool-bar">
				<tr>
					<td>
						<table class="tool-bar-header">
		                	<tr>
			                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
			                	<td class="tool-bar-title">�˿�ɨ��</td>
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
									<table id="tool-bar-body-list" class="tool-bar-body-list">
										<tr>
											<td>
												<ul>
													<li><img src="<%=rootPath%>/resource/image/toolbar/xgsbbq.gif" border=0>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/ipDistrictMatch.do?action=portscan&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")'>ɨ��˿�</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/xgxtzsx.gif" border=0>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/ipDistrictMatch.do?action=searchPortScanByIp&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")'>�鿴��ʷ����</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/dkfwjs.gif" width=16>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/ipDistrictMatch.do?action=ready_addPortScan&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")'>���ɨ��˿�</a></li>
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
		</td>
	</tr>
	<!-- ����-->
	<tr>
		<td>
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
									<table id="tool-bar-body-list" class="tool-bar-body-list">
										<tr>
											<td>
											<%
											if(category.equals("host"))
											{
											 %>
												<ul>
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif" border=0>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/hostreport.do?action=showPingReport&ipaddress=<%=ipaddress%>","portScanWindow","width=800,height=400,scrollbars=yes,resizable=yes")'>�����Ա���</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif" border=0>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/hostreport.do?action=showCapacityReport&ipaddress=<%=ipaddress%>","portScanWindow","width=800,height=400,scrollbars=yes,resizable=yes")'>���ܱ���</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif" width=16>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/hostreport.do?action=showDiskReport&ipaddress=<%=ipaddress%>","portScanWindow","width=800,height=400,scrollbars=yes,resizable=yes")'>���̱���</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif" width=16>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/hostreport.do?action=showHardwareReport&ipaddress=<%=ipaddress%>","portScanWindow","width=800,height=400,scrollbars=yes,resizable=yes")'>Ӳ������</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif" width=16>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/hostreport.do?action=showCompositeReport&ipaddress=<%=ipaddress%>","portScanWindow","width=800,height=400,scrollbars=yes,resizable=yes")'>�ۺϱ���</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif" width=16>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/hostreport.do?action=showAnalyseReport&ipaddress=<%=ipaddress%>","portScanWindow","width=800,height=400,scrollbars=yes,resizable=yes")'>���߱���</a></li>
												</ul>
											<%
											}
											else
											{
											 %>
											 	<ul>
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif" border=0>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/netreport.do?action=showPingReport&ipaddress=<%=ipaddress%>","portScanWindow","width=800,height=470,scrollbars=yes,resizable=yes")'>�����Ա���</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif" border=0>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/netreport.do?action=showEventReport&ipaddress=<%=ipaddress%>","portScanWindow","width=800,height=400,scrollbars=yes,resizable=yes")'>�¼�����</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif" width=16>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/netreport.do?action=showCompositeReport&ipaddress=<%=ipaddress%>","portScanWindow","width=800,height=400,scrollbars=yes,resizable=yes")'>�ۺϱ���</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif" width=16>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/netreport.do?action=showConfigReport&ipaddress=<%=ipaddress%>","portScanWindow","width=800,height=400,scrollbars=yes,resizable=yes")'>���ñ���</a></li>
												</ul>
											 <%
											 }
											 %>
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
		</td>
	</tr>
</table>
