<%@page language="java" contentType="text/html;charset=gb2312" %>
<!--  bi 杰报表页面标记库   -->
<%@ taglib prefix="bios" uri="http://www.bijetsoft.com/BiosReportTags" %>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.system.model.Department" %>

<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  
  String startdate = (String)request.getAttribute("startdate");
  String todate = (String)request.getAttribute("todate");
  String bid = (String)request.getAttribute("bid");
  String query = (String)request.getAttribute("query");
  List<Department> departmentList = (List<Department>) request.getAttribute("departmentList");
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
	var query ="<%=query%>";
	var departmentID = document.getElementById("department").value;
	
	var dep =" ";
	if(departmentID != -1){
		dep=" and d.id=" + departmentID;
	}
	
//	var bid = '<%=bid%>';
//	alert(bid);
//	alert(query);
	window.open('<%=rootPath%>/bios_report/reportWrapper.jsp?rpt=' + reportName + '.brt&start=' + starttime + ';end=' + endtime +';dep='+dep,'用户访问在校时长统计报表','top=100,left=200,height=600,width=780,toolbar=no, menubar=no,location=no,status=no,scrollbars=yes');
	
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
																							开始日期
																								<input type="text" name="startdate" id="startdate" value="<%=startdate%>" size="10">
																								<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
																								<img id=imageCalendar1 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a>
																							
																								截止日期
																								<input type="text" name="todate" id="todate" value="<%=todate%>" size="10"/>
																								<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
																								<img id=imageCalendar2 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a> 
																						
																								&nbsp;&nbsp;
																								部门
																								<select id="department" name="department" style="width:120px">
																									<option value="-1">不限</option>
																								</select>
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
																	    		name="alarmByLevelPie" value="用户在线时长报表" 
																	    		onclick="popupReport('onlinetime')" style="width:120px;" ></td>
												
																	        <td class="report-data-body-title">
																	        	<input type="hidden" />
																	        </td>
																	    		
																	    	<td class="report-data-body-title">
																	    		<input type="hidden" />
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
</BODY>

	<script type="text/javascript">
		
		var departmentArray = new Array();
		
		<%
			if(departmentList != null && departmentList.size() > 0){
				for(int i=0; i<departmentList.size(); i++){
					Department dp = departmentList.get(i);
		%>
					departmentArray.push({
							id:"<%=dp.getId()%>",
							deptname:"<%=dp.getDept()%>"
					});
		<%			
				}
			}
		%>
		
		function initDepartment() {
			var department = document.getElementById("department");
			department.length = 0;
			department.options[0] = new Option("不限","-1");
			var k=1;
			for(var i=0; i<departmentArray.length; i++){
				if(departmentArray[i].id != null || departmentArray[i].id > 0){
					department.options[k] = new Option(departmentArray[i].deptname,departmentArray[i].id);
					k++;
				}
			}
		}
		
		initDepartment();
	</script>

</HTML>