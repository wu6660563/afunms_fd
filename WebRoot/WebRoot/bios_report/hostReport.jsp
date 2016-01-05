<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="java.util.List"%>

<%
    String rootPath = request.getContextPath();
    String menuTable = (String) request.getAttribute("menuTable");
    List nodeList = (List) request.getAttribute("list");

    String startdate = (String) request.getAttribute("startdate");
    String todate = (String) request.getAttribute("todate");
%>
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript"
			src="<%=rootPath%>/include/swfobject.js"></script>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>

		<link href="<%=rootPath%>/resource/css/global/global.css"
			rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>

		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css"
			charset="utf-8" />
		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8" />
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js"
			charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js"
			charset="utf-8"></script>



		<script language="JavaScript" type="text/JavaScript">
  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);

 Ext.get("process").on("click",function(){
     
     //if(chk1&&chk2&&chk3)
     //{
     
        Ext.MessageBox.wait('数据加载中，请稍后.. '); 
        //msg.style.display="block";
	mainForm.action="<%=rootPath%>/netreport.do?action=netping";
	mainForm.submit();        
        //mainForm.action = "<%=rootPath%>/network.do?action=add";
        //mainForm.submit();
     //}  
       // mainForm.submit();
 });	
	
});

function CreateWindow(url)
{
	
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=400,height=400,directories=no,status=no,scrollbars=no,menubar=no")
}   

function setBid(eventTextId , eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/business.do?action=setBidbyuser&event='+event.id+'&value='+event.value + '&eventText=' + eventTextId);
}

function query()
  {  
     mainForm.action = "<%=rootPath%>/netreport.do?action=find&netflag=1";
     mainForm.submit();
  }


</script>



		<script language="JavaScript" type="text/JavaScript">
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
	document.getElementById('netReportTitle-0').className='detail-data-title';
	document.getElementById('netReportTitle-0').onmouseover="this.className='detail-data-title'";
	document.getElementById('netReportTitle-0').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}

// 根据用户选择设备生成报表
function popupReport(reportName){
	// 获取用户输入的查询时间
	 var startdate = document.getElementById("startdate").value;
	 var todate = document.getElementById("todate").value;
	 var starttime = startdate + " 00:00:00";
	 var endtime = todate + " 23:59:59";
	 
	// 获取用户选中的设备
	var checkList = document.getElementsByName("checkboxNode"); 
	var nodeId = new Array();
	var nodeType = new Array(); // 设备类型
	var nodeIp = new Array();	// 设备IP
	var nodeName = new Array();	// 设备名称
	var tableTitle = new Array();	// 表名（IP用下划线连接）
	var tableName = new Array();	
	var nodeSubtype = new Array();	// 设备子类型
	var sqlCondition = "";	// 查询SQL条件
	var result = false;
	var nodeNum = 0;
	var subentity ;
	if (checkList && checkList.length > 0) {
		for(var i=0; i<checkList.length; i++){
			var checkbox = checkList[i];
			if(checkbox.checked) {
				result = true;	
				var checkboxValue = checkbox.value;
			// 	nodeId[nodeNum] = parseInt(document.getElementById("nodeid_"+checkboxValue).value);
				nodeType[nodeNum] = document.getElementById("type_"+checkboxValue).value;
			// 	nodeSubtype[nodeNum] = document.getElementById("subtype_"+checkboxValue).value;
			//   	nodeName[nodeNum] = i;
				nodeIp[nodeNum] = document.getElementById("ipaddress_"+checkboxValue).value;
				var ip = nodeIp[nodeNum];
			    tableTitle[nodeNum] = i;
			    tableName[nodeNum] = nodeIp[nodeNum].replace(".","_");
			    tableName[nodeNum] = tableName[nodeNum].replace(".","_");
			    if(reportName == 'hostCpuReport'){
			    	tableName[nodeNum] = 'cpu'+tableName[nodeNum].replace(".","_");
			    }else if(reportName=='DiskReport'){
			    	
			    	tableName[nodeNum] = 'disk'+tableName[nodeNum].replace(".","_");
			    }else if(reportName == 'pingReport'){
			    	tableName[nodeNum] = 'ping'+tableName[nodeNum].replace(".","_");
			    }else if(reportName == 'BandwidthUtilHdxReport'){
			    	tableName[nodeNum] = 'allutilhdxday'+tableName[nodeNum].replace(".","_");
			    }else if(reportName == 'hostMemoryReport'){
			    	if(nodeType[nodeNum] == 'Windows'){
			    		subentity = 'VirtualMemory';
			    	}else if(nodeType[nodeNum] == 'aix'){
			    		subentity = 'SwapMemory';
			    	}
			    	tableName[nodeNum] = 'memory'+tableName[nodeNum].replace(".","_");
			    }
			  //   alert(tableName[nodeNum]);
				sqlCondition += " or (nodeid="+nodeId[nodeNum]+" and subtype='"+nodeType[nodeNum]+"')";
				nodeNum++;
			}
		}
		if (result) {
			sqlCondition = sqlCondition.substring(4);
			if(reportName == 'pingReport'){
				var condition = " entity='Utilization' ";
				window.open('<%=rootPath%>/bios_report/reportWrapper.jsp?rpt=pingReport.brt&starttime=' + starttime + ';endtime=' + endtime +';tableName='+ tableName +';condition='+condition, '设备CPU性能报表','height=650,width=800,toolbar=no,menubar=no,location=no,status=no,scrollbars=yes,resizable=no');
				
			}
			if(reportName == 'BandwidthUtilHdxReport'){
				window.open('<%=rootPath%>/bios_report/reportWrapper.jsp?rpt=BandwidthUtilHdxReport.brt&starttime=' + starttime + ';endtime=' + endtime +';tableName='+ tableName +';tableTitle='+tableTitle,'设备CPU性能报表','height=670,width=800,toolbar=no,menubar=no,location=no,status=no,scrollbars=yes,resizable=no');
			}
			if(reportName == 'DiskReport' ){
				window.open('<%=rootPath%>/bios_report/reportWrapper.jsp?rpt=DiskReport.brt&starttime=' + starttime + ';endtime=' + endtime +';tableName='+ tableName + ';ipaddress='+ ip + ';tableTitle='+tableTitle,'设备CPU性能报表','height=650,width=800,toolbar=no,menubar=no,location=no,status=no,scrollbars=yes,resizable=no');
			}
			if(reportName == 'hostCpuReport' ){
				window.open('<%=rootPath%>/bios_report/reportWrapper.jsp?rpt=netReport.brt&starttime=' + starttime + ';endtime=' + endtime +';tableName='+ tableName +';tableTitle='+tableTitle,'设备CPU性能报表','height=650,width=800,toolbar=no,menubar=no,location=no,status=no,scrollbars=yes,resizable=no');
			}
			else if(reportName == 'hostMemoryReport'){
				if(nodeType[nodeNum] == 'windows'){
			    		subentity = "'VirtualMemory'";
			    }else if(nodeType[nodeNum] == 'aix'){
			    		subentity = "'SwapMemory'";
			    }
			    
			    
				window.open('<%=rootPath%>/bios_report/reportWrapper.jsp?rpt=' + reportName + '.brt&starttime=' 
					+ starttime + ';endtime=' + endtime +';tableName=' + tableName +';subentity='+subentity ,'设备告警报表','height=650,width=800,toolbar=no,menubar=no,location=no,status=no,scrollbars=yes');		
			}
		} else {
			alert("请选择设备!!!");
		}
	} else {
		alert("请选择设备!!!");
	}
}
</script>


	</head>
	<body id="body" class="body" onload="initmenu();">
		<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0
			noResize scrolling=no src="<%=rootPath%>/include/calendar.htm"
			style="DISPLAY: none; HEIGHT: 189px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
		<form id="mainForm" method="post" name="mainForm">
			<div id="loading">
				<div class="loading-indicator">
					<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif"
						width="32" height="32" style="margin-right: 8px;" align="middle" />
					Loading...
				</div>
			</div>
			<div id="loading-mask" style=""></div>
			<table id="body-container" class="body-container">
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
								<td class="td-container-main-report">
									<table id="container-main-report" class="container-main-report">
										<tr>
											<td>
												<table id="report-content" class="report-content">
													<tr>
														<td>
															<table id="report-content-header"
																class="report-content-header">
																<tr>
																	<td>
																		<%
																		    //netReportTitleTable
																		%>
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<table id="report-content-body"
																class="report-content-body">
																<tr>
																	<td>

																		<table id="report-data-header"
																			class="report-data-header">
																			<tr>
																				<td>

																					<table id="report-data-header-title"
																						class="report-data-header-title">
																						<tr>
																							<td>
																								开始日期
																								<input type="text" name="startdate"
																									value="<%=startdate%>" size="10">
																								<a onclick="event.cancelBubble=true;"
																									href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
																									<img id=imageCalendar1 align=absmiddle width=34
																										height=21
																										src="<%=rootPath%>/include/calendar/button.gif"
																										border=0> </a> 截止日期
																								<input type="text" name="todate"
																									value="<%=todate%>" size="10" />
																								<a onclick="event.cancelBubble=true;"
																									href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
																									<img id=imageCalendar2 align=absmiddle width=34
																										height=21
																										src="<%=rootPath%>/include/calendar/button.gif"
																										border=0> </a> 

																								
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

																		<table id="report-data-body" class="report-data-body">
																			<tr height=50>
																				<td class="report-data-body-title">
																					<input type="button" name="hostCpuReport" value="设备CPU性能报表"
																									onclick="popupReport('hostCpuReport')">
																				<td class="report-data-body-title">
																					<input type="button" name="hostMemoryReport" value="设备内存性能报表"
																									onclick="popupReport('hostMemoryReport')">
																				</td>
																									
																				<td class="report-data-body-title">
																					<input type="button" name="pingReport" value="设备连通率报表"
																									onclick="popupReport('pingReport')">
																				</td>
																				<td class="report-data-body-title">
																					<input type="button" name="BandwidthUtilHdxReport" value="设备综合流速报表"
																									onclick="popupReport('BandwidthUtilHdxReport')">
																				</td>
															
																				<td class="report-data-body-title">
																					<input type="button" name="DiskReport" value="设备磁盘性能报表"
																									onclick="popupReport('DiskReport')">
																				</td>
																			</tr>		
																			<tr height=28>
																				<td class="report-data-body-title">
																					&nbsp;
																					<INPUT type="radio" name="checkall"
																						onclick="javascript:chkall()">
																					序号
																				</td>
																				<td class="report-data-body-title">
																					IP地址
																				</td>
																				<td class="report-data-body-title">
																					设备名称
																				</td>
																				<td class="report-data-body-title">
																					操作系统
																				</td>
																				<td class="report-data-body-title">
																					
																				</td>
																				
																			</tr>
																			<%
									    									HostNode node = null;
																			  	java.text.SimpleDateFormat _sdf = new java.text.SimpleDateFormat("MM-dd HH:mm");
																			  	for(int i=0;i<nodeList.size();i++){
																			  	node = (HostNode)nodeList.get(i);
																		
																			%>

																			<tr bgcolor="#ffffff" <%=onmouseoverstyle%> height=25>
																				<td class="report-data-body-list">&nbsp;&nbsp;
																					<INPUT type="radio" name="checkboxNode" id="checkboxNode" value="<%=i%>"><%=i + 1%></td>
																				<td class="report-data-body-list"><%=node.getIpAddress()%></td>
																				<td class="report-data-body-list"><%=node.getAlias()%></td>
																				<td class="report-data-body-list"><%=node.getType()%></td>
																			    <td style="display: none">
																					<input type="hide" id="ipaddress_<%=i%>" value=<%=node.getIpAddress()%> />
																					<input type="hide" id="alias_<%=i%>"  value=<%=node.getAlias()%> />
																					<input type="hide" id="type_<%=i%>"  value=<%=node.getType()%> />
																				</td>	
																				
																			</tr>
																			<%
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

		</form>
	</BODY>
</HTML>