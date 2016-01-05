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
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.polling.api.*"%>
  
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
     Vector inputVector = (Vector)ipAllData.get("input"); 
     String SRXDYAB="";
     String SRXDYBC="";
     String SRXDYCA=""; 
     String AXSRDY=""; 
     String BXSRDY=""; 
     String CXSRDY=""; 
     String AXSRDL=""; 
     String BXSRDL=""; 
     String CXSRDL="";
     String SRPL="";
     String SRGLYSA="";
     String SRGLYSB="";
     String SRGLYSC="";
     if(inputVector!=null){
         for(int i=0;i<inputVector.size();i++){
             Systemcollectdata systemdata = (Systemcollectdata)inputVector.get(i);
             if("SRXDYAB".equalsIgnoreCase(systemdata.getSubentity())){
                 SRXDYAB = systemdata.getThevalue();
             }
             if("SRXDYBC".equalsIgnoreCase(systemdata.getSubentity())){
                 SRXDYBC = systemdata.getThevalue();
             }
             if("SRXDYCA".equalsIgnoreCase(systemdata.getSubentity())){
                 SRXDYCA = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("AXSRDY".equalsIgnoreCase(systemdata.getSubentity())){
                 AXSRDY = systemdata.getThevalue();
             }
             if("BXSRDY".equalsIgnoreCase(systemdata.getSubentity())){
                 BXSRDY = systemdata.getThevalue();
             }
             if("CXSRDY".equalsIgnoreCase(systemdata.getSubentity())){
                 CXSRDY = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("AXSRDL".equalsIgnoreCase(systemdata.getSubentity())){
                 AXSRDL = systemdata.getThevalue();
             }
             if("BXSRDL".equalsIgnoreCase(systemdata.getSubentity())){
                 BXSRDL = systemdata.getThevalue();
             }
             if("CXSRDL".equalsIgnoreCase(systemdata.getSubentity())){
                 CXSRDL = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("SRPL".equalsIgnoreCase(systemdata.getSubentity())){
                 SRPL = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("SRGLYSA".equalsIgnoreCase(systemdata.getSubentity())){
                 SRGLYSA = systemdata.getThevalue();
             }
             if("SRGLYSB".equalsIgnoreCase(systemdata.getSubentity())){
                 SRGLYSB = systemdata.getThevalue();
             }
             if("SRGLYSC".equalsIgnoreCase(systemdata.getSubentity())){
                 SRGLYSC = systemdata.getThevalue()+systemdata.getUnit();
             }
         }
     }
     Vector passVector = (Vector)ipAllData.get("bypass"); 
     String AXPLDY="";
     String BXPLDY="";
     String CXPLDY="";
     String PLPL="";
     if(passVector!=null){
         for(int i=0;i<passVector.size();i++){
             Systemcollectdata systemdata = (Systemcollectdata)passVector.get(i);
             if("AXPLDY".equalsIgnoreCase(systemdata.getSubentity())){
                 AXPLDY = systemdata.getThevalue();
             }
             if("BXPLDY".equalsIgnoreCase(systemdata.getSubentity())){
                 BXPLDY = systemdata.getThevalue();
             }
             if("CXPLDY".equalsIgnoreCase(systemdata.getSubentity())){
                 CXPLDY = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("PLPL".equalsIgnoreCase(systemdata.getSubentity())){
                 PLPL = systemdata.getThevalue()+systemdata.getUnit();
             }
         }
     }
     Vector outputVector = (Vector)ipAllData.get("output");
     String SCXDYA=""; 
     String SCXDYB=""; 
     String SCXDYC=""; 
     String AXSCDL=""; 
     String BXSCDL=""; 
     String CXSCDL=""; 
     String SCPL=""; 
     String AXSCGLYS=""; 
     String BXSCGLYS=""; 
     String CXSCGLYS=""; 
     String AXSCYGGL=""; 
     String BXSCYGGL=""; 
     String CXSCYGGL=""; 
     String AXSCSZGL=""; 
     String BXSCSZGL=""; 
     String CXSCSZGL=""; 
     String AXSCWGGL=""; 
     String BXSCWGGL=""; 
     String CXSCWGGL=""; 
     String AXSCFZBFB=""; 
     String BXSCFZBFB=""; 
     String CXSCFZBFB=""; 
     String AXSCFZB=""; 
     String BXSCFZB=""; 
     String CXSCFZB=""; 
     if(outputVector!=null){
         for(int i=0;i<outputVector.size();i++){
             Systemcollectdata systemdata = (Systemcollectdata)outputVector.get(i);
             if("SCXDYA".equalsIgnoreCase(systemdata.getSubentity())){
                 SCXDYA = systemdata.getThevalue();
             }
             if("SCXDYB".equalsIgnoreCase(systemdata.getSubentity())){
                 SCXDYB = systemdata.getThevalue();
             }
             if("SCXDYC".equalsIgnoreCase(systemdata.getSubentity())){
                 SCXDYC = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("AXSCDL".equalsIgnoreCase(systemdata.getSubentity())){
                 AXSCDL = systemdata.getThevalue();
             }
             if("BXSCDL".equalsIgnoreCase(systemdata.getSubentity())){
                 BXSCDL = systemdata.getThevalue();
             }
             if("CXSCDL".equalsIgnoreCase(systemdata.getSubentity())){
                 CXSCDL = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("SCPL".equalsIgnoreCase(systemdata.getSubentity())){
                 SCPL = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("AXSCGLYS".equalsIgnoreCase(systemdata.getSubentity())){
                 AXSCGLYS = systemdata.getThevalue();
             }
             if("BXSCGLYS".equalsIgnoreCase(systemdata.getSubentity())){
                 BXSCGLYS = systemdata.getThevalue();
             }
             if("CXSCGLYS".equalsIgnoreCase(systemdata.getSubentity())){
                 CXSCGLYS = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("AXSCYGGL".equalsIgnoreCase(systemdata.getSubentity())){
                 AXSCYGGL = systemdata.getThevalue();
             }
             if("BXSCYGGL".equalsIgnoreCase(systemdata.getSubentity())){
                 BXSCYGGL = systemdata.getThevalue();
             }
             if("CXSCYGGL".equalsIgnoreCase(systemdata.getSubentity())){
                 CXSCYGGL = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("AXSCSZGL".equalsIgnoreCase(systemdata.getSubentity())){
                 AXSCSZGL = systemdata.getThevalue();
             }
             if("BXSCSZGL".equalsIgnoreCase(systemdata.getSubentity())){
                 BXSCSZGL = systemdata.getThevalue();
             }
             if("CXSCSZGL".equalsIgnoreCase(systemdata.getSubentity())){
                 CXSCSZGL = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("AXSCWGGL".equalsIgnoreCase(systemdata.getSubentity())){
                 AXSCWGGL = systemdata.getThevalue();
             }
             if("BXSCWGGL".equalsIgnoreCase(systemdata.getSubentity())){
                 BXSCWGGL = systemdata.getThevalue();
             }
             if("CXSCWGGL".equalsIgnoreCase(systemdata.getSubentity())){
                 CXSCWGGL = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("AXSCFZBFB".equalsIgnoreCase(systemdata.getSubentity())){
                 AXSCFZBFB = systemdata.getThevalue();
             }
             if("BXSCFZBFB".equalsIgnoreCase(systemdata.getSubentity())){
                 BXSCFZBFB = systemdata.getThevalue();
             }
             if("CXSCFZBFB".equalsIgnoreCase(systemdata.getSubentity())){
                 CXSCFZBFB = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("AXSCFZB".equalsIgnoreCase(systemdata.getSubentity())){
                 AXSCFZB = systemdata.getThevalue();
             }
             if("BXSCFZB".equalsIgnoreCase(systemdata.getSubentity())){
                 BXSCFZB = systemdata.getThevalue();
             }
             if("CXSCFZB".equalsIgnoreCase(systemdata.getSubentity())){
                 CXSCFZB = systemdata.getThevalue()+systemdata.getUnit();
             }
         }
     }
     Vector statuVector = (Vector)ipAllData.get("statue"); 
     String JXRL="";
     String BJTH="";
     String BJXTAXSCZYGGL="";
     String BJXTBXSCZYGGL="";
     String BJXTCXSCZYGGL="";
     String BJXTAXSCZSZGL="";
     String BJXTBXSCZSZGL="";
     String BJXTCXSCZSZGL="";
     String BJXTAXSCZWGGL="";
     String BJXTBXSCZWGGL="";
     String BJXTCXSCZWGGL="";
     if(statuVector!=null){
         for(int i=0;i<statuVector.size();i++){
             Systemcollectdata systemdata = (Systemcollectdata)statuVector.get(i);
             if("JXRL".equalsIgnoreCase(systemdata.getSubentity())){
                 JXRL = systemdata.getThevalue();
             }
             if("BJTH".equalsIgnoreCase(systemdata.getSubentity())){
                 BJTH = systemdata.getThevalue();
             }
             if("BJXTAXSCZYGGL".equalsIgnoreCase(systemdata.getSubentity())){
                 BJXTAXSCZYGGL = systemdata.getThevalue();
             }
             if("BJXTBXSCZYGGL".equalsIgnoreCase(systemdata.getSubentity())){
                 BJXTBXSCZYGGL = systemdata.getThevalue();
             }
             if("BJXTCXSCZYGGL".equalsIgnoreCase(systemdata.getSubentity())){
                 BJXTCXSCZYGGL = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("BJXTAXSCZSZGL".equalsIgnoreCase(systemdata.getSubentity())){
                 BJXTAXSCZSZGL = systemdata.getThevalue();
             }
             if("BJXTBXSCZSZGL".equalsIgnoreCase(systemdata.getSubentity())){
                 BJXTBXSCZSZGL = systemdata.getThevalue();
             }
             if("BJXTCXSCZSZGL".equalsIgnoreCase(systemdata.getSubentity())){
                 BJXTCXSCZSZGL = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("BJXTAXSCZWGGL".equalsIgnoreCase(systemdata.getSubentity())){
                 BJXTAXSCZWGGL = systemdata.getThevalue();
             }
             if("BJXTBXSCZWGGL".equalsIgnoreCase(systemdata.getSubentity())){
                 BJXTBXSCZWGGL = systemdata.getThevalue();
             }
             if("BJXTCXSCZWGGL".equalsIgnoreCase(systemdata.getSubentity())){
                 BJXTCXSCZWGGL = systemdata.getThevalue()+systemdata.getUnit();
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
	document.getElementById('upsDetailTitle-1').className='detail-data-title';
	document.getElementById('upsDetailTitle-1').onmouseover="this.className='detail-data-title'";
	document.getElementById('upsDetailTitle-1').onmouseout="this.className='detail-data-title'";
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
                      											<td width="35%"><img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(SystemSnap.getUpsStatus(upsnode.getId()+""))%>">&nbsp;<%=NodeHelper.getStatusDescr(SystemSnap.getUpsStatus(upsnode.getId()+""))%> </td>
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
													<td>
													    <table class="detail-data-body">
												      		  <tr>
                                                                <td align=center >
                                                                 <table width="100%" border="0" cellpadding="0" cellspacing="1" bgcolor="#FFFFFF">
                      		                                         <tr>
					                                                     <td height="28" align="left" bgcolor="#ECECEC" colspan=2>&nbsp;&nbsp;<b>输入信息</b></td>
				                                                     </tr>
														             <tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=28>
														             <td   class="detail-data-body-list" align="center">&nbsp;输入线电压</td>
														             <td class="detail-data-body-list" align="center">&nbsp;<%=SRXDYAB+"/"+SRXDYBC+"/"+SRXDYCA%></td>
														             </tr>
														             <tr  bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=28>
														             <td class="detail-data-body-list" align="center">&nbsp;输入相电压</td>
														             <td class="detail-data-body-list" align="center">&nbsp;<%=AXSRDY+"/"+BXSRDY+"/"+CXSRDY%></td>
														             </tr>  
														             <tr  bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=28>
														             <td class="detail-data-body-list" align="center">&nbsp;输入电流</td>
														             <td class="detail-data-body-list" align="center">&nbsp;<%=AXSRDL+"/"+BXSRDL+"/"+CXSRDL%></td>
														             </tr>  
														             <tr  bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=28>
														             <td class="detail-data-body-list" align="center">&nbsp;输入频率</td>
														             <td class="detail-data-body-list" align="center">&nbsp;<%=SRPL%></td>
														             </tr>  
														             <tr  bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=28>
														             <td class="detail-data-body-list" align="center">&nbsp;输入功率因数</td>
														             <td class="detail-data-body-list" align="center">&nbsp;<%=SRGLYSA+"/"+SRGLYSB+"/"+SRGLYSC%></td>
														             </tr>     
														             <tr>
					                                                     <td height="28" align="left" bgcolor="#ECECEC" colspan=2>&nbsp;&nbsp;<b>旁路信息</b></td>
				                                                     </tr>
														             <tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=28>
														             <td class="detail-data-body-list" align="center">&nbsp;旁路电压</td>
														             <td class="detail-data-body-list" align="center">&nbsp;<%=AXPLDY+"/"+BXPLDY+"/"+CXPLDY%></td>
														             </tr>
														             <tr  bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=28>
														             <td class="detail-data-body-list" align="center">&nbsp;旁路频率</td>
														             <td class="detail-data-body-list" align="center">&nbsp;<%=PLPL%></td>
														             </tr>        
														             <tr>
					                                                     <td height="28" align="left" bgcolor="#ECECEC" colspan=2>&nbsp;&nbsp;<b>输出信息</b></td>
				                                                     </tr>
														             <tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=28>
														             <td   class="detail-data-body-list" align="center">&nbsp;输出电压</td>
														             <td class="detail-data-body-list" align="center">&nbsp;<%=SCXDYA+"/"+SCXDYB+"/"+SCXDYC%></td>
														             </tr>
														             <tr  bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=28>
														             <td class="detail-data-body-list" align="center">&nbsp;输出电流</td>
														             <td class="detail-data-body-list" align="center">&nbsp;<%=AXSCDL+"/"+BXSCDL+"/"+CXSCDL%></td>
														             </tr> 
														              <tr  bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=28>
														             <td class="detail-data-body-list" align="center">&nbsp;输出频率</td>
														             <td class="detail-data-body-list" align="center">&nbsp;<%=SCPL%></td>
														             </tr>
														              <tr  bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=28>
														             <td class="detail-data-body-list" align="center">&nbsp;输出功率因数</td>
														             <td class="detail-data-body-list" align="center">&nbsp;<%=AXSCGLYS+"/"+BXSCGLYS+"/"+CXSCGLYS%></td>
														             </tr>
														              <tr  bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=28>
														             <td class="detail-data-body-list" align="center">&nbsp;输出有功功率</td>
														             <td class="detail-data-body-list" align="center">&nbsp;<%=AXSCYGGL+"/"+BXSCYGGL+"/"+CXSCYGGL%></td>
														             </tr>
														              <tr  bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=28>
														             <td class="detail-data-body-list" align="center">&nbsp;输出视在功率</td>
														             <td class="detail-data-body-list" align="center">&nbsp;<%=AXSCSZGL+"/"+BXSCSZGL+"/"+CXSCSZGL%></td>
														             </tr>
														             <tr  bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=28>
														             <td class="detail-data-body-list" align="center">&nbsp;输出无功功率</td>
														             <td class="detail-data-body-list" align="center">&nbsp;<%=AXSCWGGL+"/"+BXSCWGGL+"/"+CXSCWGGL%></td>
														             </tr>
														             <tr  bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=28>
														             <td class="detail-data-body-list" align="center">&nbsp;输出负载</td>
														             <td class="detail-data-body-list" align="center">&nbsp;<%=AXSCFZBFB+"/"+BXSCFZBFB+"/"+CXSCFZBFB%></td>
														             </tr>
														             <tr  bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=28>
														             <td class="detail-data-body-list" align="center">&nbsp;输出峰值比</td>
														             <td class="detail-data-body-list" align="center">&nbsp;<%=AXSCFZB+"/"+BXSCFZB+"/"+CXSCFZB%></td>
														             </tr>
														             <tr>
					                                                     <td height="28" align="left" bgcolor="#ECECEC" colspan=2>&nbsp;&nbsp;<b>并机系统</b></td>
				                                                     </tr>
														             <tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=28>
														             <td   class="detail-data-body-list" align="center">&nbsp;机型容量</td>
														             <td class="detail-data-body-list" align="center">&nbsp;<%=JXRL%></td>
														             </tr>
														             <tr  bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=28>
														             <td class="detail-data-body-list" align="center">&nbsp;本机台号</td>
														             <td class="detail-data-body-list" align="center">&nbsp;<%=BJTH%></td>
														             </tr> 
														             <tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=28>
														             <td   class="detail-data-body-list" align="center">&nbsp;并机系统输出总有功功率</td>
														             <td class="detail-data-body-list" align="center">&nbsp;<%=BJXTAXSCZYGGL+""+BJXTBXSCZYGGL+""+BJXTCXSCZYGGL%></td>
														             </tr>
														             <tr  bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=28>
														             <td class="detail-data-body-list" align="center">&nbsp;并机系统输出总视在功率</td>
														             <td class="detail-data-body-list" align="center">&nbsp;<%=BJXTAXSCZSZGL+"/"+BJXTBXSCZSZGL+"/"+BJXTCXSCZSZGL%></td>
														             </tr>
														             <tr  bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=28>
														             <td class="detail-data-body-list" align="center">&nbsp;并机系统输出总无功功率</td>
														             <td class="detail-data-body-list" align="center">&nbsp;<%=BJXTAXSCZWGGL+"/"+BJXTBXSCZWGGL+"/"+BJXTCXSCZWGGL%></td>
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