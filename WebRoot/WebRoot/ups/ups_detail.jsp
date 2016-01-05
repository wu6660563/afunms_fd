<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.common.util.ShareData"%> 
<%@page import="com.afunms.security.model.MgeUps"%>
<%@page import="com.afunms.polling.node.UPSNode"%>   
<%@page import="com.afunms.polling.om.Systemcollectdata"%>   
<%@page import="java.util.*"%>      
<%@page import="java.text.*"%>     
<%@page import="com.afunms.inform.util.SystemSnap"%>
<%@page import="com.afunms.polling.impl.*"%>
<%@page import="com.afunms.polling.api.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
  
<%
	 String rootPath = request.getContextPath();
	 String menuTable = (String)request.getAttribute("menuTable");
     MgeUps vo  = (MgeUps)request.getAttribute("vo");
     UPSNode upsnode = (UPSNode)PollingEngine.getInstance().getUpsByID(vo.getId());
     Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(vo.getIpAddress());
     Vector batteryVector = (Vector)ipAllData.get("battery");
     String DCDY = "";
     String DCDL = "";
     String DCSYHBSJ = "";
     String DCWD = "";
     String HJWD = "";
     String sysName = "";
     String SBMS = "";
     if(batteryVector!=null){
         for(int i=0;i<batteryVector.size();i++){
             Systemcollectdata systemdata = (Systemcollectdata)batteryVector.get(i);
             if("DCDY".equalsIgnoreCase(systemdata.getEntity())){
                 DCDY = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("DCDL".equalsIgnoreCase(systemdata.getEntity())){
                 DCDL = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("DCSYHBSJ".equalsIgnoreCase(systemdata.getEntity())){
                 DCSYHBSJ = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("DCWD".equalsIgnoreCase(systemdata.getEntity())){
                 DCWD = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("HJWD".equalsIgnoreCase(systemdata.getEntity())){
                 HJWD = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("SBMC".equalsIgnoreCase(systemdata.getEntity())){
                 sysName = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("SBMS".equalsIgnoreCase(systemdata.getEntity())){
                 SBMS = systemdata.getThevalue()+systemdata.getUnit();
             }
         }
     }
     Vector systemVector = (Vector)ipAllData.get("system");
     String sysDescr = "";
     String sysUpTime = "";
     if(systemVector!=null){
         for(int i=0;i<systemVector.size();i++){
             Systemcollectdata systemdata = (Systemcollectdata)systemVector.get(i);
             if("sysDescr".equalsIgnoreCase(systemdata.getSubentity())){
                 sysDescr = systemdata.getThevalue();
             }
             if("sysUpTime".equalsIgnoreCase(systemdata.getSubentity())){
                 sysUpTime = systemdata.getThevalue();
             }
         }
     }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String time1 = sdf.format(new Date());
								
	String pingconavg ="0";
	String starttime1 = time1 + " 00:00:00";
	String totime1 = time1 + " 23:59:59";
		
	Hashtable ConnectUtilizationhash = new Hashtable();
	I_HostCollectData hostmanager=new HostCollectDataManager();
	try{
		ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"Ping","ConnectUtilization",starttime1,totime1);
	}catch(Exception ex){
		ex.printStackTrace();
	}
		
	if (ConnectUtilizationhash.get("avgpingcon")!=null){
		pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
		pingconavg = pingconavg.replace("%", "");
	}
	request.setAttribute("pingconavg", new Double(pingconavg));
	double avgpingcon = (Double)request.getAttribute("pingconavg");
    int percent1 = Double.valueOf(avgpingcon).intValue();
	int percent2 = 100-percent1;
		
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>
<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="gb2312" />
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="gb2312"/>
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 

<script language="javascript">	
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
	document.getElementById('upsDetailTitle-0').className='detail-data-title';
	document.getElementById('upsDetailTitle-0').onmouseover="this.className='detail-data-title'";
	document.getElementById('upsDetailTitle-0').onmouseout="this.className='detail-data-title'";
}
function refer(action){
		document.getElementById("id").value="<%=vo.getId()%>";
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}

</script>
</head>
<body id="body" class="body" onload="initmenu();">
<form method="post" name="mainForm">
<input type=hidden name="orderflag">
<input type=hidden name="id" value="<%=vo.getId()%>">
<div id="loading">
	<div class="loading-indicator">
	<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif" width="32" height="32" style="margin-right: 8px;" align="middle" />Loading...</div>
</div>
<div id="loading-mask" style=""></div>
<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 189px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
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
											                	<td><b>UPS设备监视 >> 详细信息</b></td>
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
										     								<td width="70%" align="left" valign="top">
			        								  <table>
                    										<tr>
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;名称:</td>
                      											<td width="35%"><%=vo.getAlias()%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;IP地址:</td>
                      											<td width="35%"><%=vo.getIpAddress()%> </td>
                    										</tr>
                    										<tr bgcolor="#ECECEC">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;系统描述:</td>
                      											<td width="35%" colspan=3><%=sysDescr%> </td>	
                    										</tr>
                    										<tr>
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;运行时间:</td>
                      											<td width="35%"><%=sysUpTime%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;设备名称:</td>
                      											<td width="35%"><%=sysName%> </td>
                    										</tr>
                    										<tr bgcolor="#ECECEC">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;环境温度:</td>
                      											<td width="35%"><%=HJWD%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;设备描述:</td>
                      											<td width="35%"><%=SBMS%> </td>
                    										</tr>
                    										<tr >
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;当前状态:</td>
                      											<td width="35%"><img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(SystemSnap.getUpsStatus(upsnode.getId()+""))%>">&nbsp;<%=NodeHelper.getStatusDescr(SystemSnap.getUpsStatus(upsnode.getId()+""))%></td>
                    											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;管理状态:</td>
																<%if("1".equals(vo.getIsmanaged())){%>
                      											<td width="35%">已监视</td>
																<%}else{%>
																<td width="35%">未监视</td>
																<%}%>
                    										</tr> 
                    										<tr bgcolor="#ECECEC">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;电池电压:</td>
                      											<td width="35%"><%=DCDY%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;电池电流:</td>
                      											<td width="35%"><%=DCDL%> </td>
                    										</tr>
                    										<tr>
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;电池剩余后备时间:</td>
                      											<td width="35%"><%=DCSYHBSJ%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;电池温度:</td>
                      											<td width="35%"><%=DCWD%> </td>
                    										</tr>
                									</table>
                      
										        </td>
												<td width="20%" align="center" valign="middle" class=dashLeft>

											<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none w align=center border=0 algin="center">
                        								
                          									<tr>
                											<td width="400" align="left" valign="middle" class=dashLeft>
                												<table  style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa  cellpadding=0 rules=none width=80% align=center border=0 algin="center" >
                    													<tr class="topNameRight">
                      														<td height="30" align="center">
                      														
                      															<div id="flashcontent">
																						<strong>You need to upgrade your Flash Player</strong>
																							</div>
																							<script type="text/javascript">
																							var so = new SWFObject("<%=rootPath%>/flex/Pie_Component.swf?percent1=<%=percent1%>&percentStr1=可用&percent2=<%=percent2%>&percentStr2=不可用", "Pie_Component", "160", "160", "8", "#ffffff");
																							so.write("flashcontent");
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
											    	<td class="detail-data-header">
											    		<%=upsDetailTitleTable%>
											    	</td>
											    </tr>
												<tr>
													<td width=100%>
													    <table class="detail-data-body">
												      		  <tr>
											                    			<td align=center >
											                    				<br>
											                    				<table cellpadding="0" cellspacing="0" width=48% align=center>
								              									<tr> 
								                									<td width="100%" align=center> 
								                										<div id="flashcontent1">
																					<strong>You need to upgrade your Flash Player</strong>
																				</div>
																				<script type="text/javascript">
																					var so = new SWFObject("<%=rootPath%>/flex/Area_Input_V.swf?ipadress=<%=vo.getIpAddress()%>&table=input&obj1=SRXDYAB&obj2=SRXDYBC&obj3=SRXDYCA&tag=IV", "Area_Input_V", "430", "280", "8", "#ffffff");
																					so.write("flashcontent1");
																				</script>				
															                		</td>
																		</tr>             
																	</table> 
											                  			</td>
												                		<td align=center >
												                			<br>
												                			<table cellpadding="0" cellspacing="0" width=48% align=center>
								              									<tr> 
								                									<td width="100%"  align=center> 
								                										<div id="flashcontent2">
																					<strong>You need to upgrade your Flash Player</strong>
																				</div>
																				<script type="text/javascript">
																					var so = new SWFObject("<%=rootPath%>/flex/Area_Input_A.swf?ipadress=<%=vo.getIpAddress()%>&table=input&obj1=AXSRDL&obj2=BXSRDL&obj3=CXSRDL&tag=IA", "Area_Input_A", "430", "280", "8", "#ffffff");
																					so.write("flashcontent2");
																				</script>				
															                		</td>
																		</tr>             
																	</table> 
												           			</td>
                    													</tr>
                    													 
                    													<tr>
                  												<td align=center >
												            		<br>
												                	<table cellpadding="0" cellspacing="0" width=48%>
								              							<tr> 
								                							<td width="100%"  align=center> 
								                								<div id="flashcontent3">
																					<strong>You need to upgrade your Flash Player</strong>
																				</div>
																				<script type="text/javascript">
																					var so = new SWFObject("<%=rootPath%>/flex/Area_Output_V.swf?ipadress=<%=vo.getIpAddress()%>&table=output&obj1=SCXDYA&obj2=SCXDYB&obj3=SCXDYC&tag=OV", "Area_Output_V", "430", "280", "8", "#ffffff");
																					so.write("flashcontent3");
																				</script>				
															                </td>
																		</tr>             
																	</table> 
																	<br>
												          		</td>
												          		<td align=center >
																	<br>
																	<table cellpadding="0" cellspacing="0" width=48%>
								              							<tr> 
								                							<td width="100%"  align=center> 
								                								<div id="flashcontent4">
																					<strong>You need to upgrade your Flash Player</strong>
																				</div>
																				<script type="text/javascript">
																					var so = new SWFObject("<%=rootPath%>/flex/Area_Output_Kw.swf?ipadress=<%=vo.getIpAddress()%>&table=output&obj1=AXSCYGGL&obj2=BXSCYGGL&obj3=CXSCYGGL&tag=OP", "Area_Output_Kw", "430", "280", "8", "#ffffff");
																					so.write("flashcontent4");
																				</script>				
															                </td>
																		</tr>             
																	</table> 
																	<br>
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
<script>
			
Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	    Ext.get("process").on("click",function(){
        Ext.MessageBox.wait('数据加载中，请稍后.. ');   
        mainForm.action = "<%=rootPath%>/ups.do?action=toDetail";
        mainForm.submit();
 });    
});
</script>
</BODY>
</HTML>