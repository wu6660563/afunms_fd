<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.topology.dao.HostNodeDao"%>
<%
  String rootPath = request.getContextPath();
  String tmp = (String)request.getParameter("nodeid");
  System.out.println("tmp==" + tmp);
  HostNodeDao hostNodeDao = new HostNodeDao();
  if(tmp == null)tmp = (String)request.getParameter("tmp");
  HostNode node = hostNodeDao.loadHost(Integer.valueOf(tmp));
  hostNodeDao.close();
  System.out.println("node==" + node);
  String ipaddress = node.getIpAddress();
  String subtype = (String)request.getParameter("subtype");
  String category = (String)request.getParameter("category");
  String sys_oid = node.getSysOid();
  String flag1 = request.getParameter("flag"); 
  System.out.println("subtype==" + subtype);
%>

<table class="container-main-tool">
	<tr>
		<td>
			<table class="tool-bar">
				<tr>
					<td>
						<table class="tool-bar-header">
							<tr>
								<td align="left" width="5">
									<img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5"
										height="29" />
								</td>
								<td class="tool-bar-title">
									����
								</td>
								<td align="right">
									<img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5"
										height="29" />
								</td>
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
													<li>
														<img src="<%=rootPath%>/resource/image/toolbar/ping.gif">
														<a href="javascript:void(null)"
															onClick='window.open("<%=rootPath%>/tool/ping.jsp?ipaddress=<%=ipaddress%>","oneping", "height=400, width= 500, top=300, left=100")'>Ping</a>
													</li>
												</ul>
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
                                                    <li><img src="<%=rootPath%>/resource/image/menu/dkpz.gif" border=0>&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/portconfig.do?action=nodeportlist&id=<%=tmp%>&ipaddress=<%=ipaddress%>","oneping", "height=400, width= 1000, top=300, left=100,scrollbars=yes")'>�˿�����</a></li>
                                                    <li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>
                                                    <a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/alarmport.do?action=list&ipaddress=<%=ipaddress%>&nodeid=<%=tmp%>&type=net&subtype=<%=subtype%>","oneping", "height=600, width= 1000, top=300, left=100,scrollbars=yes")'>�˿ڷ�ֵ����</a></li>
                                                    <%
												    if("host".equals(category)){
												    %>
												    <li><img src="<%=rootPath%>/resource/image/menu/zjjcjs.gif" border=0>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/processgroup.do?action=list&nodeid=<%=tmp%>&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")'>���̼�������</a></li>
                                                    <li><img src="<%=rootPath%>/resource/image/toolbar/service.gif" border=0 width=18>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/hostservicegroup.do?action=list&nodeid=<%=tmp%>&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")'>�����������</a></li>
                                                    <li><img src="<%=rootPath%>/resource/image/menu/cpfzylb.gif" border=0 width=18>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/disk.do?action=toolbarlist&nodeid=<%=tmp%>&ipaddress=<%=ipaddress%>","diskWindow","toolbar=no,width=1000,height=500,directories=no,status=no,scrollbars=yes,menubar=no")'>���̼�������</a></li>
												    <%
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
            </table>
        </td>
    </tr>
    <tr>
		<td>
			<table class="tool-bar">
				<tr>
					<td>
						<table class="tool-bar-header">
							<tr>
								<td align="left" width="5">
									<img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5"
										height="29" />
								</td>
								<td class="tool-bar-title">
									�˿�ɨ��
								</td>
								<td align="right">
									<img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5"
										height="29" />
								</td>
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
													<li>
														<img src="<%=rootPath%>/resource/image/toolbar/xgsbbq.gif"
															border=0>
														<a href="#"
															onclick='window.open("<%=rootPath%>/ipDistrictMatch.do?action=portscan&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")'>ɨ��˿�</a>
													</li>
													<li>
														<img
															src="<%=rootPath%>/resource/image/toolbar/xgxtzsx.gif"
															border=0>
														<a href="#"
															onclick='window.open("<%=rootPath%>/ipDistrictMatch.do?action=searchPortScanByIp&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")'>�鿴��ʷ����</a>
													</li>
													<li>
														<img src="<%=rootPath%>/resource/image/menu/dkfwjs.gif"
															width=16>
														<a href="#"
															onclick='window.open("<%=rootPath%>/ipDistrictMatch.do?action=ready_addPortScan&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")'>���ɨ��˿�</a>
													</li>
												</ul>
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
	<!-- 
    <% //if("network".equals(category)&&(node.getCollecttype()==1 || node.getCollecttype()==10 )){%>
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
                                                </ul>
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
                                                    <li><img src="<%=rootPath%>/resource/image/menu/dkpz.gif" border=0>&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/portconfig.do?action=nodeportlist&id=<%=tmp%>&ipaddress=<%=ipaddress%>","oneping", "height=400, width= 1000, top=300, left=100,scrollbars=yes")'>�˿�����</a></li>
                                                    <li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;
                                                    <a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/alarmport.do?action=list&ipaddress=<%=ipaddress%>&nodeid=<%=tmp%>&type=net&subtype=<%=subtype%>","oneping", "height=600, width= 1000, top=300, left=100,scrollbars=yes")'>�˿ڷ�ֵ����</a></li>
                                                </ul>
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
    
    <%//} else if("network".equals(category)&&(node.getCollecttype()==3)){%>
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
                                                </ul>
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
                                                    <li><img src="<%=rootPath%>/resource/image/menu/dkpz.gif" border=0>&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/portconfig.do?action=nodeportlist&id=<%=tmp%>&ipaddress=<%=ipaddress%>","oneping", "height=400, width= 1000, top=300, left=100,scrollbars=yes")'>�˿�����</a></li>
                                                </ul>
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
    <%//} else{ %>
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
                                                </ul>
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
    <%//}%>
    <%
        //if(("network".equals(category))&&(node.getCollecttype()==1)){
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
                                                </ul>
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
    <%
        //}
    %>
     -->
</table>
