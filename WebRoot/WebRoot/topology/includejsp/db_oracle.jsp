<%@page language="java" contentType="text/html;charset=gb2312"%> 
<%@page import="java.util.*"%>
<%@page import="com.afunms.application.manage.OracleManager"%>
<%@page import="com.afunms.common.util.CreateAmColumnPic"%>
<%@page import="com.afunms.application.dao.DBDao"%>
<%@page import="com.afunms.application.model.DBVo"%>
<%@page import="com.afunms.application.util.IpTranslation"%>
<%@page import="com.afunms.initialize.ResourceCenter"%> 
<%@page import="com.afunms.common.util.CreateMetersPic"%> 
<%
	String  rootPath = request.getContextPath();;  
	String  Id= request.getParameter("Id")==null?"":request.getParameter("Id"); //vo.getId
	String  IpAddress= request.getParameter("IpAddress")==null?"":request.getParameter("IpAddress"); //vo.getIpAddress()
	String  _flag= request.getParameter("_flag")==null?"":request.getParameter("_flag"); // 
	String  Alias= request.getParameter("Alias")==null?"":request.getParameter("Alias"); // vo.getAlias()
	String  Port= request.getParameter("Port")==null?"":request.getParameter("Port"); // vo.getPort()
	String  DbName= request.getParameter("DbName")==null?"":request.getParameter("DbName"); // vo.getDbName()
	String  dbtye= request.getParameter("dbtye")==null?"":request.getParameter("dbtye"); // 
	String  managed= request.getParameter("managed")==null?"":request.getParameter("managed"); // 
	String  runstr= request.getParameter("runstr")==null?"":request.getParameter("runstr"); // 
	String  created= request.getParameter("created")==null?"":request.getParameter("created"); //  
	String  buffercache= request.getParameter("buffercache")==null?"":request.getParameter("buffercache"); //  
	String  lstrnStatu= request.getParameter("lstrnStatu")==null?"":request.getParameter("lstrnStatu"); //  
	String  dictionarycache= request.getParameter("dictionarycache")==null?"":request.getParameter("dictionarycache"); // 
	String  unbuffercache= request.getParameter("unbuffercache")==null?"":request.getParameter("unbuffercache"); // 
	String  unpctmemorysorts= request.getParameter("unpctmemorysorts")==null?"":request.getParameter("unpctmemorysorts"); //  
	String  librarycache= request.getParameter("librarycache")==null?"":request.getParameter("librarycache"); //  
	String  unlibrarycache= request.getParameter("unlibrarycache")==null?"":request.getParameter("unlibrarycache"); //  
	String  pctmemorysorts= request.getParameter("pctmemorysorts")==null?"":request.getParameter("pctmemorysorts"); //  
	String  opencurstr= request.getParameter("opencurstr")==null?"":request.getParameter("opencurstr"); //    
	String  picip= request.getParameter("picip")==null?"":request.getParameter("picip"); //    
	String  avgdata= request.getParameter("avgdata")==null?"":request.getParameter("avgdata"); //   
	String  memPerfValue= request.getParameter("memPerfValue")==null?"":request.getParameter("memPerfValue"); //  
	String  sid= request.getParameter("sid")==null?"":request.getParameter("sid"); //  
	 if ("1".equals(managed)) {
        managed = "�ѹ���";
     } else {
         managed = "δ����";
     }
     if ("1".equals(lstrnStatu)) {
        lstrnStatu = "������";
     }
	String[] sysItem3={"HOST_NAME","DBNAME","VERSION","INSTANCE_NAME","STATUS","STARTUP_TIME","ARCHIVER"};
	String[] sysItemch3={"��������","DB ����","DB �汾","������","����״̬","���̿�ʼʱ��","�鵵��־ģʽ"}; 
	
	DBVo vo  = (DBVo)request.getAttribute("db");
  String myip = vo.getIpAddress();  
	OracleManager om=new OracleManager();
	Hashtable orasys = om.geHashtable(IpAddress,sid);
	 //sqlserver ����������ͼ
	 String[] title={"������������","�����ֵ�������","�⻺��������","�ڴ��е�����"};
		 String[] data={buffercache,dictionarycache,librarycache,pctmemorysorts};
		 CreateAmColumnPic amColumn=new CreateAmColumnPic();
		String dbdata=amColumn.createSqlUtilChart(data,title);
		
		//��ռ�ʹ����
		IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+sid;
		DBDao dao=null;
		Hashtable memValue = new Hashtable();
		Vector tableinfo = new Vector();
		try {
				dao = new DBDao();
		   memValue = dao.getOracle_nmsoramemvalue(serverip);
		   tableinfo = dao.getOracle_nmsoraspaces(serverip);
		} catch (Exception e) {
				e.printStackTrace();
			} finally{
				dao.close();
			}
		//Vector tableinfo = (Vector) request.getAttribute("tableinfo");
		String tabledata=amColumn.createOraTableSpaceUtilChart(tableinfo);
		String sgadata=amColumn.createSGAChart(memValue);
		//����ƽ����ͨ��
		double avgpingcon = (Double)request.getAttribute("avgpingcon");
		String pingavg=String.valueOf(Math.round(avgpingcon));
		CreateMetersPic cmp = new CreateMetersPic();
		String pathPing = ResourceCenter.getInstance().getSysPath()+ "resource\\image\\dashBoardGray.png";
	     cmp.createChartByParam(picip , pingavg , pathPing,"��ͨ��","pingdata"); 
%>
<script type="text/javascript"	src="<%=rootPath%>/include/swfobject.js"></script>
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
						<table align=center  cellpadding=0 cellspacing="0" width=100%>
							<tr>
								<td width="80%" align="left" valign="top">
									<table>
										<tr bgcolor="#F1F1F1">
											<td width="15%" height="29" align="left" nowrap
												class=txtGlobal>
												&nbsp;���ݿ����:
											</td>
											<td width="35%">
												<%=Alias%>[<a href="<%=rootPath%>/db.do?action=ready_edit&id=<%=Id%>&ipaddress=<%=IpAddress%>&flag=<%=_flag%>"><img src="<%=rootPath%>/resource/image/editicon.gif" border=0>�޸�</a>]
											</td>
											<td width="15%" height="29" align="left" nowrap
												class=txtGlobal>
												&nbsp;������:
											</td>
											<td width="35%">
												<%=DbName%>
											</td>
										</tr>
										<tr>
											<td width="15%" height="29" align="left" nowrap
												class=txtGlobal>
												&nbsp;����:
											</td>
											<td width="35%"><%=dbtye%>
											</td>
											<td width="15%" height="29" align="left" nowrap
												class=txtGlobal>
												&nbsp;IP��ַ:
											</td>
											<td width="35%"><%=IpAddress%>
											</td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td width="15%" height="29" align="left" nowrap
												class=txtGlobal>
												&nbsp;�˿�:
											</td>
											<td width="35%"><%=Port%>
											</td>
											<td width="15%" height="29" align="left" nowrap
												class=txtGlobal>
												&nbsp;����״̬:
											</td>
											<td width="35%"><%=lstrnStatu%>
											</td>
										</tr>
										<%
											if (orasys != null) {
												int k = 0;
												for (int i = 0; i < sysItem3.length; i++) {

													// System.out.println(sysItem3[i]+"-----------");
													String key = sysItemch3[i];
													String value = "";
													if (orasys.get(sysItem3[i]) != null) {
														value = (String) orasys.get(sysItem3[i]);
													}
													String key1 = "", value1 = "", key2 = "", value2 = "";
													if (i + 1 < sysItem3.length) {
														key1 = sysItemch3[i + 1];
														if (orasys.get(sysItem3[i + 1]) != null) {
															value1 = (String) orasys.get(sysItem3[i + 1]);
														}
													}
													if (k % 2 == 0) {
														if (!value.equals("") && !value1.equals("")) {
										%>
										<tr bgcolor="#FFFFFF">
											<td width="15%" height="29">
												&nbsp;<%=key%></td>
											<td width="35%"><%=value%></td>
											<td width="15%" height="29">
												&nbsp;<%=key1%></td>
											<td width="35%"><%=value1%></td>
										</tr>
										<%
											} else if (!value.equals("")) {
										%>
										<tr bgcolor="#FFFFFF">
											<td width="15%" height="29">
												&nbsp;<%=key%></td>
											<td width="35%"><%=value%></td>
											<td width="15%" height="29">
												&nbsp;���ݿⴴ������
											</td>
											<td width="35%">
												&nbsp;<%=created%></td>
										</tr>
										<%
											}
													} else {
														if (!value.equals("") && !value1.equals("")) {
										%>
										<tr bgcolor="#F1F1F1">
											<td width="15%" height="29">
												&nbsp;<%=key%></td>
											<td width="35%"><%=value%></td>
											<td width="15%" height="29">
												&nbsp;<%=key1%></td>
											<td width="35%"><%=value1%></td>
										</tr>
										<%
											} else if (!value.equals("")) {
										%>
										<tr bgcolor="#F1F1F1">
											<td width="15%" height="29">
												&nbsp;<%=key%></td>
											<td width="35%"><%=value%></td>
											<td width="15%" height="29">
												&nbsp;���ݿⴴ������
											</td>
											<td width="35%">
												&nbsp;<%=created%></td>
										</tr>

										<%
											}
													}
													k++;
													i = i + 1;
												}
											}
											Vector banners = (Vector) orasys.get("BANNER");
											if (banners != null && banners.size() > 0) {
										%>
										<tr>
											<td width="20%">
												&nbsp;���ݿⰲװ�Ĳ�Ʒ��Ϣ
											</td>
											<td width="80%" colspan="3">
												<%
													for (int i = 0; i < banners.size(); i++) {
												%>

												<%=banners.get(i).toString()%>

												<br>
												<%
													}
												%>
											</td>
										</tr>
										<%
											}
										%>


										<%
											if (memPerfValue != null) {
										%>
                                 <!--  
										<tr>
											<td width="15%" height="29">
												&nbsp;������������
											</td>
											<td width="35%">
												<table border=1 height=15 width=10 bgcolor=#ffffff
													id="huanchongtab">
													<tr>
														<td id="huanchong1" width="<%=buffercache%>%"
															bgcolor="skyblue" align=center><%=buffercache%>%
														</td>
														<td id="huanchong2" width="<%=unbuffercache%>%"
															bgcolor=#ECECEC></td>
													</tr>
												</table>
											</td>
											<td width="15%" height="29">
												&nbsp;�����ֵ�������
											</td>
											<td width="35%">
												<table border=1 height=15 width=10 bgcolor=#ffffff
													id="dictionaryTab">
													<tr>
														<td width="<%=dictionarycache%>%" bgcolor="#9ACD32"
															align=center><%=dictionarycache%>%
														</td>
														<td width="<%=unpctmemorysorts%>%" bgcolor=#ECECEC></td>
													</tr>
												</table>
											</td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td width="15%" height="29">
												&nbsp;�⻺��������
											</td>
											<td width="35%">
												<table border=1 height=15 width=10 bgcolor=#ffffff
													id="kuhuanchunTab">
													<tr>
														<td width="<%=librarycache%>%" bgcolor="0099AA"
															align=center><%=librarycache%>%
														</td>
														<td width="<%=unlibrarycache%>%" bgcolor=#ECECEC></td>
													</tr>
												</table>
											</td>
											<td width="15%" height="29">
												&nbsp;�ڴ��е�����
											</td>
											<td width="20%">
												<table border=1 height=15 width=10 bgcolor=#ffffff
													id="neicunpaixuTab">
													<tr>
														<td width="<%=pctmemorysorts%>%" bgcolor="#90EE90"
															align=center><%=pctmemorysorts%>%
														</td>
														<td width="<%=unpctmemorysorts%>%" bgcolor=#ECECEC></td>
													</tr>
												</table>
											</td>
										</tr>	-->
										<tr>
											<td height="29" colspan=4>
												<table height=15 width=100% bgcolor=#ffffff id="zuilangfeiTab">
													<tr>
														<td width="50%" nowrap>
															&nbsp;���˷��ڴ��ǰ10�����ռȫ���ڴ��ȡ���ı���
														</td>
														<td width="50%" nowrap align="left">
															<table >
																<tr>
																	<td width="100%" bgcolor="#ECECEC" align=center>
																		<table align="center" width=100%>
																			<tr>
																				<td width="100%" align=left colspan=2>
																					<div id="progressBar" style="visibility: hidden;">
																						<span id="block1" style=""></span>
																						<span id="block2" style=""></span>
																						<span id="block3" style=""></span>
																						<span id="block4" style=""></span>
																						<span id="block5" style=""></span>
																						<span id="block6" style=""></span>
																						<span id="block7" style=""></span>
																						<span id="block8" style=""></span>
																						<span id="block9" style=""></span>
																					</div>
																				</td>
																			</tr>
																			<tr>
																				<td align="center" id="complete"></td>
																				<td>
																					<input type="hidden" value="��ʼ" id="go" />
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
											}
										%>
										<tr>
											<td colspan=2 align=CENTER>
											</td>
											<TD colspan=2>
											</TD>
										</tr>
									</table>
								</td>
								<td valign="top">
									<table cellPadding=0 cellspacing="0" align=center  border=1>
				                     <tr bgcolor=#F1F1F1 height="29">
					                      <td align="center">����ƽ����ͨ�� </td>
				                      </tr>
				
										<tr>
											<td height="30" align="center">
												 <img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>pingdata.png" > 
											
											<!-- 	<div id="pie">
													<strong>You need to upgrade your Flash Player</strong>
												</div>
												<script type="text/javascript"
													src="<%=rootPath%>/include/swfobject.js"></script>
												<script type="text/javascript">
                                                	var so = new SWFObject("<%=rootPath%>/amchart/ampie.swf", "ampie","180", "155", "8", "#FFFFFF");
                                                   	so.addVariable("path", "<%=rootPath%>/amchart/");
                                                   	so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/pingStatepie.xml"));
                                                   	so.addVariable("chart_data","<%=avgdata%>");
                                                   	so.write("pie");
                                                </script>
                                                -->
											</td>
										</tr>
										<tr>
											<td height="7" align=center><img src="<%=rootPath%>/resource/image/Loading_2.gif"></td>
										</tr>
									<!--	<tr bgcolor=#F1F1F1 height="29">
					                      <td align="center">����ƽ����ͨ�� </td>
				                      </tr>
										<tr>
											<td height="30" align="center"><img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>public.png" id="public"></td>
										</tr>
										<tr>
											<td height="7" align=center><img src="<%=rootPath%>/resource/image/Loading.gif"></td>
										</tr>
										-->
									</table>
								</td>
							</tr>
							<tr>
							      <td width="100%" colspan=2>
												<table border=1 height=15 width=10 bgcolor=#ffffff
													id="neicunpaixuTab">
													<tr>
													<td width=33%>
														    <table>
														           <tr>
														               <td height=30 align=center>
														                <b>������</b>
														               </td>
														           </tr>
														           <tr>
														              <td>
														                <div id="bufferUtil"></div>
															                
															                 <script type="text/javascript">
						                                             var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "ampie","250", "278", "8", "#FFFFFF");
						                                             so.addVariable("path", "<%=rootPath%>/amchart/");
						                                             so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/shootingUtil_settings.xml"));
						                                             so.addVariable("chart_data","<%=dbdata%>");
						                                             so.write("bufferUtil");
						                                                   </script>
						                                            </td>
						                                            
						                                          </tr>
						                                    </table>  
														</td>
														<td width=33%>
														    <table>
														           <tr>
														           
														               <td height=30 align=center>
														                <b>  ��ռ�ʹ���� </b>
														               </td>
														           </tr>
														           <tr>
														              <td>
														               <div id="oracleSpace1">
																															
																		</div>
																	
																	<script type="text/javascript">
																	<% if(!tabledata.equals("0")){ %>	
						                                             var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "ampie","265", "278", "8", "#FFFFFF");
						                                                 so.addVariable("path", "<%=rootPath%>/amchart/");
						                                                 so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/dbpercent_settings.xml"));
						                                                 so.addVariable("chart_data","<%=tabledata%>");
						                                                 so.write("oracleSpace1");
						                                             <%}else{%>
																	var _div=document.getElementById("oracleSpace1");
																	var img=document.createElement("img");
																		img.setAttribute("src","<%=rootPath%>/resource/image/nodata.gif");
																		_div.appendChild(img);
																	 <%}%>
						                                             </script>
						                                            </td>
						                                            
													</tr>
												</table>
											</td>
								<td width=33%>
														    <table>
														           <tr>
														               <td height=30 align=center>
														                <b>SGA��Ϣ����</b>
														               </td>
														           </tr>
														           <tr>
														              <td>
														                <div id="sga"></div>
															                
															                 <script type="text/javascript">
						                                             var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "ampie","260", "278", "8", "#FFFFFF");
						                                             so.addVariable("path", "<%=rootPath%>/amchart/");
						                                             so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/dbmemory_settings.xml"));
						                                             so.addVariable("chart_data","<%=sgadata%>");
						                                             so.write("sga");
						                                                   </script>
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