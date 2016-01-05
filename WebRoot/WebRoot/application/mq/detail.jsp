<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.common.util.*" %>
<%@page import="com.afunms.monitor.item.*"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.impl.*"%>
<%@page import="com.afunms.polling.api.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.monitor.item.base.MoidConstants"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@ page import="com.afunms.polling.api.I_Portconfig"%>
<%@ page import="com.afunms.polling.om.Portconfig"%>
<%@ page import="com.afunms.polling.om.*"%>
<%@ page import="com.afunms.polling.impl.PortconfigManager"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>

<%@page import="com.afunms.config.dao.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.application.manage.WasManager"%>
<%@page import="com.afunms.application.wasmonitor.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%@ page import="com.afunms.mq.DEServerConstants"%>
<%@ page import="com.ibm.mq.pcf.CMQXC"%>
<%@ page import="com.ibm.mq.pcf.CMQCFC"%>
<%@ page import="com.afunms.mq.*"%>

<%
	 String rootPath = request.getContextPath();
	 String menuTable = (String)request.getAttribute("menuTable");
	 Double mqAvgPing = (Double)request.getAttribute("mqAvgPing");
	int percent1 = Double.valueOf(mqAvgPing).intValue();
	int percent2 = 100-percent1;
	String dbPage = "detail";
	String flag_1 = (String)request.getAttribute("flag");
	MQConfig vo = (MQConfig)request.getAttribute("vo");
	if(vo == null){
		vo = new MQConfig();
	}
	StringBuffer dataStr = new StringBuffer();
		 	dataStr.append("连通;").append(Math.round(mqAvgPing)).append(";false;7CFC00\\n");
		 	dataStr.append("未连通;").append(100-Math.round(mqAvgPing)).append(";false;FF0000\\n");
		 	String avgdata = dataStr.toString();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>


<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="gb2312" />
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="gb2312"/>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 

<script language="javascript">	

  function doQuery()
  {  
     if(mainForm.key.value=="")
     {
     	alert("请输入查询条件");
     	return false;
     }
     mainForm.action = "<%=rootPath%>/network.do?action=find";
     mainForm.submit();
  }
  
  function doChange()
  {
     if(mainForm.view_type.value==1)
        window.location = "<%=rootPath%>/topology/network/index.jsp";
     else
        window.location = "<%=rootPath%>/topology/network/port.jsp";
  }

  function toAdd()
  {
      mainForm.action = "<%=rootPath%>/network.do?action=ready_add";
      mainForm.submit();
  } 
  
// 全屏观看
function gotoFullScreen() {
	parent.mainFrame.resetProcDlg();
	var status = "toolbar=no,height="+ window.screen.height + ",";
	status += "width=" + (window.screen.width-8) + ",scrollbars=no";
	status += "screenX=0,screenY=0";
	window.open("topology/network/index.jsp", "fullScreenWindow", status);
	parent.mainFrame.zoomProcDlg("out");
}
var show = true;
var hide = false;
//修改菜单的上下箭头符号
function my_on(head,body)
{
	var tag_a;
	for(var i=0;i<head.childNodes.length;i++)
	{
		if (head.childNodes[i].nodeName=="A")
		{
			tag_a=head.childNodes[i];
			break;
		}
	}
	tag_a.className="on";
}
function my_off(head,body)
{
	var tag_a;
	for(var i=0;i<head.childNodes.length;i++)
	{
		if (head.childNodes[i].nodeName=="A")
		{
			tag_a=head.childNodes[i];
			break;
		}
	}
	tag_a.className="off";
}

//添加菜单	
function initmenu()
{
	var idpattern=new RegExp("^menu");
	var menupattern=new RegExp("child$");
	var tds = document.getElementsByTagName("div");
	for(var i=0,j=tds.length;i<j;i++){
		var td = tds[i];
		if(idpattern.test(td.id)&&!menupattern.test(td.id)){					
			menu =new Menu(td.id,td.id+"child",'dtu','100',show,my_on,my_off);
			menu.init();		
		}
	}
	setClass();
}

function setClass(){
	document.getElementById('wasDetailTitle-0').className='detail-data-title';
	document.getElementById('wasDetailTitle-0').onmouseover="this.className='detail-data-title'";
	document.getElementById('wasDetailTitle-0').onmouseout="this.className='detail-data-title'";
}


</script>
</head>
<body id="body" class="body" onload="initmenu();">
<form method="post" name="mainForm">
<input type=hidden name="orderflag">
<input type=hidden name="id">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=960>
	<tr>
		<td class="td-container-menu-bar">
			<table id="container-menu-bar" class="container-menu-bar">
				<tr>
					<td>
						<%=menuTable%>
					</td>	
				</tr>
			</table>
		</td>
		<td class="td-container-main">
			<table id="container-main" class="container-main">
				<tr>
					<td class="td-container-main-application-detail">
						<table id="container-main-application-detail" class="container-main-application-detail">
							<tr>
								<td>
									<table id="application-detail-content" class="application-detail-content">
										<tr>
											<td>
												<table id="application-detail-content-header" class="application-detail-content-header">
								                	<tr>
									                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
									                	<td><b>应用 >>MQ监视 >> <%=vo.getName()%> 详细信息</b></td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									       			</tr>
									        	</table>
		        							</td>
		        						</tr>
		        						<tr>
		        							<td>
		        								<table id="application-detail-content-body" class="application-detail-content-body">
		        									<tr>
		        										<td>
		        											<table>
								               					<tr>
								     								<td width="82%" align="left" valign="top">
						        										<table>
				                    										<tr>
				                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;名称:</td>
				                      											<td width="35%"><%=vo.getName()%> </td>	
																				<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;消息管理器名称:</td>
				                      											<td width="35%"><%=vo.getManagername()%> </td>
				                    										</tr>
				                    										<tr bgcolor="#ECECEC">
				                    											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;IP地址:</td>
				                      											<td width="35%"><%=vo.getIpaddress()%> </td>
				                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;端口:</td>
				                      											<td width="35%"><%=vo.getPortnum()%> </td>
				                    										</tr>   
				                    										<tr>
				                    											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;管理状态:</td>
																				<%if(vo.getMon_flag()==1){%>
				                      											<td width="35%">已监视</td>
																				<%}else{%>
																				<td width="35%">未监视</td>
																				<%}%>
				                    										</tr> 
			                											</table>
								        							</td>
																	<td width="17%" align="center" valign="middle" class=dashLeft>
																		<table width="95%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none  align=center border=1 align="center">
                         													<tr>
					                											<td width="400" align="left" valign="middle" class=dashLeft>
					                												<table  style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa  cellpadding=0 rules=none width=80% align=center border=1 align="center" >
				                    													<tr bgcolor=#F1F1F1 height="26">
														                                    <td align="center" >
															                                    今日连通率
														                                    </td>
													                                   </tr>
				                    													<tr class="topNameRight">
				                      														<td height="30" align="center">
				                      														<!-- 
				                      															<div id="flashcontent00">
																									<strong>You need to upgrade your Flash Player</strong>
																								</div>
																								<script type="text/javascript">
																									var so = new SWFObject("<%=rootPath%>/flex/Pie_Component.swf?percent1=<%=percent1%>&percentStr1=可用&percent2=<%=percent2%>&percentStr2=不可用", "Pie_Component", "160", "160", "8", "#ffffff");
																									so.write("flashcontent00");
																								</script>
																							-->
																							<div id="avgping">
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
				                      														</td>
				                    													</tr>
				                    													<tr>
							           														<td height="7" align=center><img src="<%=rootPath%>/resource/image/Loading_2.gif"></td>
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
		        								<table id="application-detail-content-footer" class="application-detail-content-footer">
		        									<tr>
		        										<td>
		        											<table width="100%" border="0" cellspacing="0" cellpadding="0">
									                  			<tr>
									                    			<td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
									                    			<td></td>
									                    			<td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
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
									<table id="application-detail-data" class="application-detail-data">
                                       <tr>
									    	<td>
									    		<table class="application-detail-data-body">
													<tr>
														<td>
												      		<table width="96%" align="center">
		                                                       	<tr bgcolor="#F1F1F1">
																	<td>远程队列信息</td>
																</tr>
																<tr>
																	<td align=center>
																		<table width=80%>
		  																	<tr>
					                    										<td width=15%><strong>队列名称</strong></td>
																				<td width=15%><strong>队列类型</strong></td>
																				<td width=15%><strong>持久性</strong></td>
																				<td width=15%><strong>远程队列名称</strong></td>
																				<td width=15%><strong>管理器名称</strong></td>
																				<td width=15%><strong>传输队列名称</strong></td>
																			</tr>
					                  										<%
																			 	List remoteValue = (ArrayList)request.getAttribute("remote"); 	
																			 	if (remoteValue != null && remoteValue.size()>0){
																			 		for(int i=0;i<remoteValue.size();i++){
																			 			MqQueue qAttr = (MqQueue)remoteValue.get(i);
																			 			if (qAttr != null ){ 		
																			 %> 
			   
																					<tr  class="othertr" <%=onmouseoverstyle%> >
			  																			<td height="20">
			  																				&nbsp;
			  																			</td>
			  																			<td><%=qAttr.getQname()%></td>
			  																			<td><%=qAttr.getQtype()%></td>
			  																			<td><%=qAttr.getPersistent()%></td>
			  																			<td><%=qAttr.getRemoteQName()%></td>
			  																			<td><%=qAttr.getRemoteQM()%></td>
			  																			<td><%=qAttr.getXmitQName()%></td>
																					 </tr>
																			<%
																						}
																					}
																				}
																			%>
																			</table>
									   									</td>
									 								 </tr>				
															</table>
									   					</td>
									 				 </tr>
									 				 
									 				 <tr>
														<td>
												      		<table width="96%" align="center">
													      		<tr bgcolor="#F1F1F1">
																	<td>本地队列信息</td>
																</tr>
																<tr>
																	<td align=center>
																		<table width=80%>
		  																	<tr>
		    																	<td width=10%>&nbsp;</td>
		    																	<td width=20%><strong>队列名称</strong></td>
		    																	<td width=20%><strong>队列类型</strong></td>
		    																	<td width=20%><strong>持久性</strong></td>
		    																	<td width=20%><strong>深度</strong></td>
		    																	<td width=10%><strong>使用</strong></td>
		    																</tr>
										     								<%
																			 	List localValue = (List)request.getAttribute("local"); 	
																			 	if (localValue != null && localValue.size()>0){
																			 		for(int i=0;i<localValue.size();i++){
																			 			MqQueue qAttr = (MqQueue)localValue.get(i);
																			 			if (qAttr != null ){ 		
																			 %> 
		 																	<tr  class="othertr" <%=onmouseoverstyle%> >
		   																			<td height="20">
		   																				&nbsp;
		   																			</td>
		   																			<td><%=qAttr.getQname()%></td>
		   																			<td><%=qAttr.getQtype()%></td>
		   																			<td><%=qAttr.getPersistent()%></td>
		   																			<td><%=qAttr.getQdepth()%></td>
		   																			<td><%=qAttr.getUsage()%></td>
		 																	</tr>
																				<%
																							}
																						}
																					}
																				%>																	
																		</table>
																	</td>
																</tr>     
													      	</table>
									   					</td>
									 				 </tr>	
									 				 
									 				  <tr>
														<td>
												      		<table width="96%" align="center">
													      		<tr bgcolor="#F1F1F1">
																	<td>通道信息</td>
																</tr>
																<tr>
																	<td align=center>
																		<table width=80%>
		  																	<tr>
		    																	<td width=10%>&nbsp;</td>
																			    <td width=20%><strong>通道名称</strong></td>
																			    <td width=20%><strong>通道类型</strong></td>
																			    <td width=20%><strong>连接名</strong></td>
																			    <td width=20%><strong>传输队列名称</strong></td>
																			    <td width=10%><strong>通道状态</strong></td>
		    																</tr>
										     								<%
																			 	Vector mqValue = (Vector)request.getAttribute("mqValue"); 	
																			 	if (mqValue != null && mqValue.size()>0){
																			 		for(int i=0;i<mqValue.size();i++){
																			 			Hashtable cAttr = (Hashtable)mqValue.get(i);
																			 			if (cAttr != null && cAttr.size()>0){
																			 				Integer type = new Integer((String)cAttr.get("type"));
																			 				Integer status = new Integer((String)cAttr.get("status"));
																			 				String chaneltype= DEServerConstants.ChaneltypeStr[DEServerConstants.Chaneltypes[type]];
																			 				String chanelstatus=DEServerConstants.ChanelstatusStr[DEServerConstants.Chanelstatus[status]];
																			 		
																			 %>    
																			  <tr  class="othertr" <%=onmouseoverstyle%> >
																			    <td height="20">
																			    &nbsp;<%=i+1%>
																			    </td>
																			    <td><%=cAttr.get("name")%></td>
																			    <td><%=chaneltype%></td>
																			    <td><%=cAttr.get("connName")%></td>
																			    <td><%=cAttr.get("xmitQName")%></td>
																			    <td><%=chanelstatus%></td>
																			   </tr>
																			<%
																						}
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
</HTML>