<%@page language="java" contentType="text/html;charset=gb2312"%>
<%

	String rootPath = request.getContextPath();;  
	String  dbtye= request.getParameter("dbtye")==null?"":request.getParameter("dbtye"); // ����
	String  managed= request.getParameter("managed")==null?"":request.getParameter("managed"); // ����״̬
	String  runstr= request.getParameter("runstr")==null?"":request.getParameter("runstr"); // ��ǰ״̬
	String  ipAddress= request.getParameter("ipAddress")==null?"":request.getParameter("ipAddress"); // IP��ַvo.getIpAddress()
	String  name= request.getParameter("name")==null?"":request.getParameter("name"); // ���ݿ�����
	String  dbserver= request.getParameter("dbserver")==null?"":request.getParameter("dbserver"); // ��Ӧ�ķ�������
	String  createuser= request.getParameter("createuser")==null?"":request.getParameter("createuser"); // ������
	String  createtime= request.getParameter("createtime")==null?"":request.getParameter("createtime"); // ����ʱ��
	String  picip= request.getParameter("picip")==null?"":request.getParameter("picip"); //  
	String  avgdata= request.getParameter("avgdata")==null?"":request.getParameter("avgdata"); // 
 %>
<table id="application-detail-content"
	class="application-detail-content">
	<tr>
		<td>
			 <jsp:include page="/topology/includejsp/detail_content_top.jsp">
			 	<jsp:param name="contentTitle" value="������Ϣ"/> 
			 </jsp:include>
		</td>
	</tr>
	<tr>
		<td>
			<table id="application-detail-content-body"
				class="application-detail-content-body">
				<tr>
					<td>
						<table  align=center cellpadding=0 cellspacing="0" width=100%>
							<tr>
								<td width="80%" align="left" valign="top">
									<table>
										<tr>
											<td width="15%" height="29"  nowrap
												class=txtGlobal>
												&nbsp;����:
											</td>
											<td width="35%" >
												&nbsp;&nbsp;<%=dbtye%>
											</td>
											<td width="15%" height="29"  nowrap
												class=txtGlobal>
												&nbsp;�汾:
											</td>
											<td width="35%" >
											</td>

										</tr>
										<tr bgcolor="#F1F1F1">
											<td width="15%" height="29"  nowrap
												class=txtGlobal>
												&nbsp;����״̬:
											</td>
											<td width="35%" >
												&nbsp;&nbsp;<%=managed%>
											</td>
											<td width="15%" height="29"  nowrap
												class=txtGlobal>
												&nbsp;��ǰ״̬:
											</td>
											<td width="35%" >
												&nbsp;&nbsp;<%=runstr%>
											</td>
										</tr>
										<tr>
											<td width="15%" height="29"  nowrap
												class=txtGlobal>
												&nbsp;����������:
											</td>
											<td width="35%" >
											</td>
											<td width="15%" height="29"  nowrap
												class=txtGlobal>
												&nbsp;IP��ַ:
											</td>
											<td width="35%" >
												&nbsp;&nbsp;<%=ipAddress%>
											</td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td width="15%" height="29"  nowrap
												class=txtGlobal>
												&nbsp;���ݿ�����:
											</td>
											<td width="35%" >
												&nbsp;&nbsp;<%=name%>
											</td>
											<td width="15%" height="29"  nowrap
												class=txtGlobal>
												&nbsp;��Ӧ�ķ�������:
											</td>
											<td width="35%" >
												&nbsp;&nbsp;<%=dbserver%>
											</td>
										</tr>
										<tr>
											<td width="15%" height="29"  nowrap
												class=txtGlobal>
												&nbsp;������:
											</td>
											<td width="35%" >
												&nbsp;&nbsp;<%=createuser%>
											</td>
											<td width="15%" height="29"  nowrap
												class=txtGlobal>
												&nbsp;����ʱ��:
											</td>
											<td width="35%" >
												&nbsp;&nbsp;<%=createtime%>
											</td>
										</tr>
									</table>
								</td>

								<td valign="middle">
									<table cellPadding=0 cellspacing="0" align=center>
										<tr>
											<td width="100%"  valign="middle">
												<table width="100%" style="BORDER-COLLAPSE: collapse"
													borderColor=#cedefa cellPadding=0 rules=none align=center
													border=1>
													<tr>
														<td width="100%" >
															<!--      <img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>pingavg.png" id="pingavg">-->
															<div id="pie">
																<strong>You need to upgrade your Flash Player</strong>
															</div>
															<script type="text/javascript"
																src="<%=rootPath%>/include/swfobject.js"></script>
															<script type="text/javascript">
								                       		// <![CDATA[		
                                                              var so = new SWFObject("<%=rootPath%>/amchart/ampie.swf", "ampie","180", "155", "8", "#FFFFFF");
                                                                 so.addVariable("path", "<%=rootPath%>/amchart/");
                                                                 so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/pingStatepie.xml"));
                                                                 so.addVariable("chart_data","<%=avgdata%>");
                                                                 so.write("pie");
                                                               // ]]>
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