<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.initialize.ResourceCenter"%> 
<%@page import="com.afunms.common.util.CreateMetersPic"%> 
<%

	String rootPath = request.getContextPath();;  
	
	String  dbtye= request.getParameter("dbtye")==null?"":request.getParameter("dbtye"); //����
	String  ipAddress= request.getParameter("ipAddress")==null?"":request.getParameter("ipAddress"); // IP��ַvo.getIpAddress()
	String  managed= request.getParameter("managed")==null?"":request.getParameter("managed"); // ����״̬
	String  runstr= request.getParameter("runstr")==null?"":request.getParameter("runstr"); // ��ǰ״̬
	
	String  dbName= request.getParameter("dbName")==null?"":request.getParameter("dbName"); // ������vo.getDbName()
	String  port= request.getParameter("port")==null?"":request.getParameter("port"); //�˿�:vo.getPort()
	String  os_name= request.getParameter("os_name")==null?"":request.getParameter("os_name"); // ����ϵͳ����
	String  host_name= request.getParameter("host_name")==null?"":request.getParameter("host_name"); // ��������
	
	String  total_cpu= request.getParameter("total_cpu")==null?"":request.getParameter("total_cpu"); // CPU����
	String  configured_cpu= request.getParameter("configured_cpu")==null?"":request.getParameter("configured_cpu"); // �����õ�CPU
	String  total_memory= request.getParameter("total_memory")==null?"":request.getParameter("total_memory"); // �������ڴ�
	String  installed_prod= request.getParameter("installed_prod")==null?"":request.getParameter("installed_prod"); // DB2��Ʒ:
	
	String  prod_release= request.getParameter("prod_release")==null?"":request.getParameter("prod_release"); // DB2�ķ��а汾
	String  picip= request.getParameter("picip")==null?"":request.getParameter("picip");  
	String  pingAvg= request.getParameter("pingAvg")==null?"0":request.getParameter("pingAvg");  
    float avgPing=Float.parseFloat(pingAvg);
		 	String pingavg=String.valueOf(Math.round(avgPing));
		CreateMetersPic cmp = new CreateMetersPic();
		String pathPing = ResourceCenter.getInstance().getSysPath()+ "resource\\image\\dashBoardGray.png";
	     cmp.createChartByParam(picip , pingavg , pathPing,"��ͨ��","pingdata");  
															
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
						<table align=center cellpadding=0 cellspacing="0" width=100%>
							<tr>
								<td width="80%" align="left" valign="top">
									<table>
										<tr>
											<td width="15%" height="29"  nowrap
												class=txtGlobal>
												&nbsp;����:
											</td>
											<td width="35%" >
												&nbsp;<%=dbtye%>
											</td>
											<td width="15%" height="29"  nowrap
												class=txtGlobal>
												&nbsp;IP��ַ:
											</td>
											<td width="35%" >
												&nbsp;<%=ipAddress%>
											</td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td width="15%" height="29"  nowrap
												class=txtGlobal>
												&nbsp;����״̬:
											</td>
											<td width="35%" >
												&nbsp;<%=managed%>
											</td>
											<td width="15%" height="29"  nowrap
												class=txtGlobal>
												&nbsp;��ǰ״̬:
											</td>
											<td width="35%" >
												&nbsp;<%=runstr%>
											</td>
										</tr>
										<tr>
											<td width="15%" height="29"  nowrap
												class=txtGlobal>
												&nbsp;������:
											</td>
											<td width="35%" >
												&nbsp;<%=dbName%>
											</td>
											<td width="15%" height="29"  nowrap
												class=txtGlobal>
												&nbsp;�˿�:
											</td>
											<td width="35%" >
												&nbsp;<%=port%>
											</td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td width="15%" height="29"  nowrap
												class=txtGlobal>
												&nbsp;����ϵͳ����:
											</td>
											<td width="35%" >
												&nbsp;<%=os_name%>
											</td>
											<td width="15%" height="29"  nowrap
												class=txtGlobal>
												&nbsp;��������:
											</td>
											<td width="35%" >
												&nbsp;<%=host_name%>
											</td>
										</tr>
										<tr>
											<td width="15%" height="29"  nowrap
												class=txtGlobal>
												&nbsp;CPU����:
											</td>
											<td width="35%" >
												&nbsp;<%=total_cpu%>
											</td>
											<td width="15%" height="29"  nowrap
												class=txtGlobal>
												&nbsp;�����õ�CPU:
											</td>
											<td width="35%" >
												&nbsp;<%=configured_cpu%>
											</td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td width="15%" height="29"  nowrap
												class=txtGlobal>
												&nbsp;�������ڴ�:
											</td>
											<td width="35%" >
												&nbsp;<%=total_memory%>
											</td>
											<td width="15%" height="29"  nowrap
												class=txtGlobal>
												&nbsp;DB2��Ʒ:
											</td>
											<td width="35%" >
												&nbsp;<%=installed_prod%>
											</td>
										</tr>
										<tr>
											<td width="15%" height="29"  nowrap
												class=txtGlobal>
												&nbsp;DB2�ķ��а汾:
											</td>
											<td width="35%" align="center" colspan=3>
												&nbsp;<%=prod_release%>
											</td>
										</tr>  
									</table>
								</td>
								<td valign="middle">
									<table cellPadding=0 cellspacing="0" align=center>
										<tr>
											<td width="100%" align="left" valign="middle">
												<table style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa
													rules=none align=center border=1 cellpadding=0
													cellspacing="0" width=100%>
													<tr bgcolor=#F1F1F1 height="26">
														<td align="center">
															����ƽ����ͨ��
														</td>
													</tr>
													<tr>
														<td width="100%" >
														 
															<img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>pingdata.png" >
													
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