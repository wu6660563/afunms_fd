<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.common.util.CreateAlarmMetersPic"%> 
<%@page import="com.afunms.common.util.CreateAmColumnPic"%> 
<%@page import="java.util.Vector"%> 
<%@page import="com.afunms.application.dao.DBDao"%>
<%@page import="java.util.Hashtable"%> 
<%@page import="java.util.Iterator"%> 
<%@page import="com.afunms.common.util.CreateMetersPic"%> 
<%@page import="com.afunms.application.util.IpTranslation"%>
<%@page import="com.afunms.initialize.ResourceCenter"%> 
<%@page import="com.afunms.application.model.DBVo"%>
<%
	String rootPath = request.getContextPath();
	String  dbtye= request.getParameter("dbtye")==null?"":request.getParameter("dbtye"); // 
	String  ipAddress= request.getParameter("ipAddress")==null?"":request.getParameter("ipAddress"); //vo.getIpAddress()	  
	String  managed= request.getParameter("managed")==null?"":request.getParameter("managed"); // 	  
	String  runstr= request.getParameter("runstr")==null?"":request.getParameter("runstr"); //   
	String  HostName= request.getParameter("HostName")==null?"":request.getParameter("HostName"); // 	  
	String  VERSION= request.getParameter("VERSION")==null?"":request.getParameter("VERSION"); // 	  
	String  productlevel= request.getParameter("productlevel")==null?"":request.getParameter("productlevel"); //   
	String  ProcessID= request.getParameter("ProcessID")==null?"":request.getParameter("ProcessID"); // 
	String  IsSingleUser= request.getParameter("IsSingleUser")==null?"":request.getParameter("IsSingleUser"); // 
	String  IsIntegratedSecurityOnly= request.getParameter("IsIntegratedSecurityOnly")==null?"":request.getParameter("IsIntegratedSecurityOnly"); // 
	String  IsClustered= request.getParameter("IsClustered")==null?"":request.getParameter("IsClustered"); // 
	double  intMyBufferCacheHitRatio=Double.parseDouble(request.getParameter("intMyBufferCacheHitRatio")==null?"":request.getParameter("intMyBufferCacheHitRatio")); // 
	double  intPlanCacheHitRatio= Double.parseDouble(request.getParameter("intPlanCacheHitRatio")==null?"":request.getParameter("intPlanCacheHitRatio")); // 
	double  intCatalogMetadataHitRatio= Double.parseDouble(request.getParameter("intCatalogMetadataHitRatio")==null?"":request.getParameter("intCatalogMetadataHitRatio")); // 
	double  intCursorManagerByTypeHitRatio= Double.parseDouble(request.getParameter("intCursorManagerByTypeHitRatio")==null?"":request.getParameter("intCursorManagerByTypeHitRatio")); // 
	String  picip= request.getParameter("picip")==null?"":request.getParameter("picip"); // 
	double  avgpingcon= Double.parseDouble(request.getParameter("pingavg")==null?"0":request.getParameter("pingavg")); // 平均连通率
	
	StringBuffer dataStr = new StringBuffer();
		 	dataStr.append("连通;").append(Math.round(avgpingcon)).append(";false;7CFC00\\n");
		 	dataStr.append("未连通;").append(100-Math.round(avgpingcon)).append(";false;FF0000\\n");
		 	String avgdata = dataStr.toString();

String pingavg=String.valueOf(Math.round(avgpingcon));
		CreateMetersPic cmp = new CreateMetersPic();
		String pathPing = ResourceCenter.getInstance().getSysPath()+ "resource\\image\\dashBoardGray.png";
	     cmp.createChartByParam(picip , pingavg , pathPing,"连通率","pingdata"); 
		 //sqlserver 各种命中率图
		 String[] items = { "缓冲区命中率 ","planCache命中率", "CursorManagerByType命中率","CatalogMetadata命中率" };
		 String[] data={intMyBufferCacheHitRatio+"",intPlanCacheHitRatio+"",intCatalogMetadataHitRatio+"",intCursorManagerByTypeHitRatio+""};
		CreateAmColumnPic amColumn=new CreateAmColumnPic();
		String dbdata=amColumn.createSqlUtilChart(data,items);
		
		//
		//Vector tableinfo = new Vector();
		DBVo vo  = (DBVo)request.getAttribute("db");
       String myip = vo.getIpAddress();  
		IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+vo.getAlias();
		DBDao dao=null;
		Hashtable dataValue = new Hashtable();
		Vector tableinfo = new Vector();
		try {
				dao = new DBDao();
		   dataValue = dao.getSqlserver_nmsdbvalue(serverip);
		  
		} catch (Exception e) {
				e.printStackTrace();
			} finally{
				dao.close();
			}
		//	 Hashtable dataValue = (Hashtable)request.getAttribute("dbValue");
			 String[] usedperc=null;
     String[] usedsize=null;
     String[] size=null;
     String[] dbname=null;
   String xmlStr="";
    String[] colorStr=new String[] {"#33FF33","#FF0033","#9900FF","#FFFF00","#33CCFF","#003366","#33FF33","#FF0033","#9900FF","#FFFF00","#333399","#0000FF","#A52A2A","#800080"};
     Hashtable database=new Hashtable();
     String[] dbItems={"usedperc","usedsize","size","dbname"};
     if(dataValue!=null&&dataValue.size()>0){
         database=(Hashtable)dataValue.get("database");
     }
    	 
    
	 if (database != null && database.size() > 0) {
		
            int n=0;
            usedperc=new String[database.size()];
				usedsize=new String[database.size()];
				size=new String[database.size()];
				dbname=new String[database.size()];
      for(Iterator itr = database.keySet().iterator(); itr.hasNext();){ 
         String key = (String) itr.next(); 
         Hashtable tablespace = (Hashtable) database.get(key); 
          if(tablespace!= null && tablespace.size() > 0){
           
				usedperc[n]=(String)tablespace.get(dbItems[0]);
			    usedsize[n]=(String)tablespace.get(dbItems[1]);
			    size[n]=(String)tablespace.get(dbItems[2]);
			    dbname[n]=(String)tablespace.get(dbItems[3]);
			     n++;
							 }
		        }
      StringBuffer sb=new StringBuffer();
      
     
      sb.append("<chart><series>");
      if(dbname!=null){
      for(int k=0;k<dbname.length;k++){
			sb.append("<value xid='").append(k).append("'>").append(dbname[k]).append("</value>");
			
			}
			}
			sb.append("</series><graphs><graph gid='0'>");
			if(usedperc!=null){
			for(int i=0,j=0;i<usedperc.length;i++,j++){
			if(((i+1)%colorStr.length)==0)j=0;
			int perInt=Math.round(Float.parseFloat(usedperc[i]));
			sb.append("<value xid='").append(i).append("' color='").append(colorStr[j]).append("'>"+perInt).append("</value>");
			}
			}
			sb.append("</graph></graphs></chart>");
			xmlStr=sb.toString();
		        }else {
				xmlStr="0";
				}
				
		Hashtable    logfile=null;
		String[] logname=null;
		String logStr="";
if(dataValue!=null&&dataValue.size()>0)
    logfile=(Hashtable)dataValue.get("logfile");
      if (logfile != null && logfile.size() > 0) {
    	 
       int m=0;
       usedperc=new String[logfile.size()];
			usedsize=new String[logfile.size()];
			size=new String[logfile.size()];
			logname=new String[logfile.size()];
 for(Iterator itr = logfile.keySet().iterator(); itr.hasNext();){ 
    String key = (String) itr.next(); 
    Hashtable tablespace = (Hashtable) logfile.get(key); 
     if(tablespace!= null && tablespace.size() > 0){
      
			usedperc[m]=(String)tablespace.get(dbItems[0]);
		    usedsize[m]=(String)tablespace.get(dbItems[1]);
		    size[m]=(String)tablespace.get(dbItems[2]);
		    logname[m]=(String)tablespace.get("logname");
		     m++;
						 }
	        }
	        StringBuffer logBuffer=new StringBuffer();
     
       logBuffer.append("<chart><series>");
       if(dbname!=null){
       for(int k=0;k<logname.length;k++){
			logBuffer.append("<value xid='").append(k).append("'>").append(logname[k]).append("</value>");
			
			}
			}
			logBuffer.append("</series><graphs><graph gid='0'>");
			if(usedperc!=null){
			for(int i=0,j=0;i<usedperc.length;i++,j++){
			if(((i+1)%colorStr.length)==0)j=0;
			int perInt=Math.round(Float.parseFloat(usedperc[i]));
			logBuffer.append("<value xid='").append(i).append("' color='").append(colorStr[j]).append("'>"+perInt).append("</value>");
			}
			}
			logBuffer.append("</graph></graphs></chart>");
			logStr=logBuffer.toString();
	        }else{
	        logStr="0";
	        }
	         
				
 %>
 <script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<table id="application-detail-content" class="application-detail-content">
	<tr>
		<td>
			 <jsp:include page="/topology/includejsp/detail_content_top.jsp">
			 	<jsp:param name="contentTitle" value="数据信息"/> 
			 </jsp:include>
		</td>
	</tr>
	<tr>
		<td>
			<table id="application-detail-content-body" class="application-detail-content-body">
				<tr>
					<td>
						<table align=center cellpadding=0 cellspacing="0" width=100%>	
							<tr>
								<td width="80%" align="left" valign="top">
									<table>
										<tr>
											<td width="15%" height="29" 
												nowrap class=txtGlobal>
												&nbsp;类型:
											</td>
											<td width="35%" >
												<%=dbtye%>
											</td>
											<td width="15%" height="29" 
												nowrap class=txtGlobal>
												&nbsp;IP地址:
											</td>
											<td width="35%" >
												<%=ipAddress%>
											</td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td width="15%" height="29" 
												nowrap class=txtGlobal>
												&nbsp;管理状态:
											</td>
											<td width="35%" >
												<%=managed%>
											</td>
											<td width="15%" height="29" 
												nowrap class=txtGlobal>
												&nbsp;当前状态:
											</td>
											<td width="35%" >
												<%=runstr%>
											</td>
										</tr>
										<tr>
											<td width="15%" height="29" 
												nowrap class=txtGlobal>
												&nbsp;主机名称:
											</td>
											<td width="35%" ><%=HostName%>
											</td>
											<td width="15%" height="29" 
												nowrap class=txtGlobal>
												&nbsp;版本:
											</td>
											<td width="35%" >
												<jsp:include page="/topology/includejsp/td_acronym.jsp">
													<jsp:param name="wholeStr" value="<%= VERSION %>"/>
												</jsp:include>
											</td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td width="15%" height="29" 
												nowrap class=txtGlobal>
												&nbsp;补丁包:
											</td>
											<td width="35%" ><%=productlevel%>
											</td>
											<td width="15%" height="29" 
												nowrap class=txtGlobal>
												&nbsp;进程ID:
											</td>
											<td width="35%" ><%=ProcessID%>
											</td>
										</tr>
										<tr>
											<td width="15%" height="29" 
												nowrap class=txtGlobal>
												&nbsp;单用户模式下:
											</td>
											<td width="35%" ><%=IsSingleUser%>
											</td>
											<td width="15%" height="29" 
												nowrap class=txtGlobal>
												&nbsp;集成安全性模式下:
											</td>
											<td width="35%" ><%=IsIntegratedSecurityOnly%>
											</td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td colspan="4">
												<table>
													<tr>	
														<td width="30%" height="29">
															&nbsp;在故障转移群集中配置服务器实例:
														</td>
														<td width="70%">
															<%=IsClustered%>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										
									<!--  <tr>
											<td colspan="4">
												<table>
													<tr>	
														<td width="30%" height="29">
															&nbsp;缓冲区命中率
														</td>
														<td width="70%">
															<table border=1 height=15 width=10 bgcolor=#ffffff id="huanchongtab">
																<tr>
																	<td id="huanchong1" width="<%=intMyBufferCacheHitRatio%>%" bgcolor="skyblue" align=center><%=intMyBufferCacheHitRatio%>%</td>
																	<td id="huanchong2" width="<%=100 - intMyBufferCacheHitRatio%>%" bgcolor=#ECECEC></td>
																	 
																</tr>
															</table>
														</td>
													</tr>
												</table>
											</td> 
										</tr>
										<tr bgcolor="#F1F1F1">
											<td colspan="4">
												<table>
													<tr>	
														<td width="30%" height="29">
															&nbsp;planCache命中率
														</td>
														<td width="70%">
															<table border=1 height=15 width=10 bgcolor=#ffffff id="planCacheTab">
																<tr>
																	<td width="<%=intPlanCacheHitRatio%>%" bgcolor="#9ACD32" align=center><%=intPlanCacheHitRatio%>%</td>
																	<td width="<%=100 - intPlanCacheHitRatio%>%" bgcolor=#ECECEC></td>
																</tr>
															</table>
														</td>
													</tr>
												</table>
											</td> 
										</tr>
										<tr>
											<td colspan="4">
												<table>
													<tr>	
														<td width="30%" height="29">
															&nbsp;CursorManagerByType命中率
														</td>
														<td width="70%">
															<table border=1 height=15 width=10 bgcolor=#ffffff id="cursorManagerTab">
																<tr>
																	<td width="<%=intCursorManagerByTypeHitRatio%>%" bgcolor="#0099AA" align=center><%=intCursorManagerByTypeHitRatio%>%</td>
																	<td width="<%=100 - intCursorManagerByTypeHitRatio%>%" bgcolor=#ECECEC></td>
																</tr>
															</table>
														</td> 
													</tr>
												</table>
											</td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td colspan="4">
												<table>
													<tr>	
														<td width="30%" height="29">
															&nbsp;CatalogMetadata命中率
														</td>
														<td width="70%">
															<table border=1 height=15 width=10 bgcolor=#ffffff id="catalogMetadataTab">
																<tr>
																	<td width="<%=intCatalogMetadataHitRatio%>%" bgcolor="#90EE90" align=center><%=intCatalogMetadataHitRatio%>%</td>
																	<td width="<%=100 - intCatalogMetadataHitRatio%>%" bgcolor=#ECECEC></td>
																	 
																</tr>
															</table>
														</td>
													</tr>
												</table>
											</td>
										</tr>-->
									</table>
								</td>

								<td valign="top">
									<table style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa
													rules=none align=center border=1 cellpadding=0
													cellspacing="0" width=100%>
													<tr bgcolor=#F1F1F1 height="26">
														<td align="center" >
															今日平均连通率
														</td>
													</tr>
										<tr>
											<td width="100%" align="left" valign="top">
												<table width="100%"
													style="BORDER-COLLAPSE: collapse"
													borderColor=#cedefa cellPadding=0 rules=none
													align=center border=1>
													<tr>
														<td width="100%" align="center">
															<img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>pingdata.png" >
													<!-- 	<div id="avgping">
				                                            <strong>You need to upgrade your Flash Player</strong>
				                                        </div>
				                                   <script type="text/javascript"
							                            src="<%=rootPath%>/include/swfobject.js"></script>
					                              <script type="text/javascript">
						                                var so = new SWFObject("<%=rootPath%>/amchart/ampie.swf", "ampie","165", "165", "8", "#FFFFFF");
						                                    so.addVariable("path", "<%=rootPath%>/amchart/");
						                                    so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/pingStatepie.xml"));
						                                    so.addVariable("chart_data","<%=avgdata%>");
						                                    so.write("avgping");
					                                </script>
					                               -->
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
						<tr>
											<td colspan="2">
												<table>
													<tr>	
														<td width=30%>
														    <table>
														           <tr>
														               <td height=30 align=center>
														                <b>命中率</b>
														               </td>
														           </tr>
														           <tr>
														              <td>
														                <div id="bufferUtil"></div>
															                
															                 <script type="text/javascript">
						                                             var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "ampie","285", "278", "8", "#FFFFFF");
						                                             so.addVariable("path", "<%=rootPath%>/amchart/");
						                                             so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/shootingUtil_settings.xml"));
						                                             so.addVariable("chart_data","<%=dbdata%>");
						                                             so.write("bufferUtil");
						                                                   </script>
						                                            </td>
						                                          </tr>
						                                    </table>  
														</td>
														<td width=35%>
														   <table>
														           <tr>
														               <td height=30 align=center>
														                <b>表空间使用率</b>
														               </td>
														           </tr>
														           <tr> 
														              <td>
														<div id="tableSpace"></div>
															
															<script
																 type="text/javascript">
						                                         <% if(!xmlStr.equals("0")){ %>	
						                                            var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "ampie","295", "278", "8", "#FFFFFF");
						                                                so.addVariable("path", "<%=rootPath%>/amchart/");
						                                                so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/dbpercent_settings.xml"));
						                                                so.addVariable("chart_data","<%=xmlStr%>");
						                                                 so.write("tablespace");
						                                             <%}else{%>
																	var _div=document.getElementById("tableSpace");
																	var img=document.createElement("img");
																		img.setAttribute("src","<%=rootPath%>/resource/image/nodata.gif");
																		_div.appendChild(img);
																	<%}%>
						                                                    </script>
						                                                    </td>
						                                               </tr>  
						                                              </table>  
														                   </td>
														        <td width=35%>
														             <table>
														                  <tr>
														              <td height=30 align=center>
														                <b>数据日志空间使用率</b>
														               </td>
														           </tr>
														           <tr> 
														              <td>
														<div id="logSpace"></div>
															<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
															<script
																 type="text/javascript">
																  <% if(!logStr.equals("0")){ %>
						                                         var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "ampie","295", "278", "8", "#FFFFFF");
						                                             so.addVariable("path", "<%=rootPath%>/amchart/");
						                                             so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/dbpercent_settings.xml"));
						                                             so.addVariable("chart_data","<%=logStr%>");
						                                             so.write("logSpace");
						                                              <%}else{%>
																	var _div=document.getElementById("logSpace");
																	var img=document.createElement("img");
																		img.setAttribute("src","<%=rootPath%>/resource/image/nodata.gif");
																		_div.appendChild(img);
																	<%}%>
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
			</table>
		</td>
	</tr>
	<tr>
		<td>
			 <jsp:include page="/topology/includejsp/detail_content_footer.jsp"/>
		</td>
	</tr>
</table>