<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="java.util.List"%>

<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  
  String startdate = (String)request.getAttribute("startdate");
  String todate = (String)request.getAttribute("todate");
	
  String nodeQuery = (String) request.getAttribute("query");	
  String winquery = (String) request.getAttribute("winquery");
  		
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 

<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8"/>
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>



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

// 弹出报表页面
// reportName为弹出的报表名称
function popupReport(reportName){
	var startdate = document.getElementById("startdate").value;
	var todate = document.getElementById("todate").value;
	
	
	var starttime = startdate + " 00:00:00";
	var endtime = todate + " 23:59:59";
	
	var queryCondition = "<%=nodeQuery%>";
	// var queryCondition = "nodeid in(158,159,160,161,165,167,168,169,170,171,172,173,174,175,176,177,246,247,249,250,256,259,260,263,276,303,305,306,307,308,309,311,312,313,338,340,341,342,472,475,476,491,492,493,494 )";
	//alert(queryCondition);


	var winquery = "<%=winquery%>";
	//alert(queryCondition);
	// var queryCondition = "nodeid in(123)";
	//alert('<%=rootPath%>/bios_report/reportWrapper.jsp?rpt=' + reportName + '.brt&starttime=' + starttime + ';endtime=' + endtime +';query=' + queryCondition ,'网络设备综合报表','top=100,left=200,height=600,width=780,toolbar=no, menubar=no,location=no,status=no,scrollbars=yes');
   // if("windows_server" == reportName){
	//	alert(winquery);
	//	window.open('<%=rootPath%>/bios_report/reportWrapper.jsp?rpt=' + reportName + '.brt&starttime=' + starttime + ';endtime=' + endtime +';query=' + winquery,'网络设备综合报表','top=100,left=200,height=600,width=780,toolbar=no, menubar=no,location=no,status=no,scrollbars=yes');
		    
    // }	
    window.open('<%=rootPath%>/bios_report/reportWrapper.jsp?rpt=' + reportName + '.brt&starttime=' + starttime + ';endtime=' + endtime +';query=' + queryCondition ,'网络设备综合报表','top=100,left=200,height=600,width=780,toolbar=no, menubar=no,location=no,status=no,scrollbars=yes');

}
</script>


</head>
<body id="body" class="body" onload="initmenu();">
	<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 189px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
	<form id="mainForm" method="post" name="mainForm">
		<div id="loading">
			<div class="loading-indicator">
				<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif" width="32" height="32" style="margin-right: 8px;" align="middle" />Loading...</div>
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
				        								<table id="report-content-body" class="report-content-body">
				        									<tr>
				        										<td>
				        											
				        											<table id="report-data-header" class="report-data-header">
				        												<tr>
															  				<td>
																
																				<table id="report-data-header-title" class="report-data-header-title">
																					<tr>
																						<td>
																							&nbsp;&nbsp;开始日期
																								<input type="text" name="startdate" id="startdate" value="<%=startdate%>" size="10">
																								<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
																								<img id=imageCalendar1 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a>
																							
																								截止日期
																								<input type="text" name="todate" id="todate" value="<%=todate%>" size="10"/>
																								<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
																								<img id=imageCalendar2 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a> 
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
																	    	<td class="report-data-body-title"><input type="button" 
																	    		name="netDeviceReport" value="网络设备综合报表" 
																	    		onclick="popupReport('netDeviceReport')"></td>
												
																	        <td class="report-data-body-title"><input type="button" 
																	    		name="windows_server" value="Windows服务器设备报表" 
																	    		onclick="popupReport('windows_server')"></td>
																	    	
																	    	<td class="report-data-body-title"><input type="button" 
																	    		name="aix_server" value="Aix 服务器设备报表" 
																	    		onclick="popupReport('aix_server')"></td>
																	    		
																	    		<td class="report-data-body-title"><input type="button" 
																	    		name="db_oracle" value="Oracle数据库综合报表" 
																	    		onclick="popupReport('db_oracle')"></td>
												
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
</BODY>
</HTML>