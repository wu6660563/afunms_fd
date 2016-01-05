<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.event.model.EventList"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.topology.model.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="com.afunms.application.model.DBVo"%>
<%@page import="com.afunms.application.model.DBTypeVo"%>
<%@page import="com.afunms.application.dao.DBTypeDao"%>
<%@page import="com.afunms.application.model.Tomcat"%>
<%@page import="com.afunms.polling.base.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.application.model.IISConfig"%>
<%@page import="com.afunms.application.model.*"%>
<%
  	String rootPath = request.getContextPath();
  	//System.out.println(rootPath);
  	
  	List list = (List)request.getAttribute("list");
  	String menuTable = (String)request.getAttribute("menuTable");
	String startdate = (String)request.getAttribute("startdate");
	String todate = (String)request.getAttribute("todate");
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



<script language="javascript">	

function accEvent(eventid){
	mainForm.eventid.value=eventid;
	mainForm.action="<%=rootPath%>/event.do?action=accit";	
	mainForm.submit();
}

function query(){
	//subforms = document.forms[0];
	mainForm.action="<%=rootPath%>/netreport.do?action=netping";
	subforms.submit();
}
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
  
    function doDelete()
  {  
     mainForm.action = "<%=rootPath%>/event.do?action=dodelete";
     mainForm.submit();
  }  
  
function changeOrder(para){
	mainForm.orderflag.value = para;
	mainForm.action="<%=rootPath%>/dbreport.do?action=dbevent"
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
	document.getElementById('middlewareReportTitle-4').className='detail-data-title';
	document.getElementById('middlewareReportTitle-4').onmouseover="this.className='detail-data-title'";
	document.getElementById('middlewareReportTitle-4').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}
</script>


</head>
<body id="body" class="body" onload="initmenu();">
	<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
	<form id="mainForm" method="post" name="mainForm">
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
														<table id="report-content-header" class="report-content-header">
										                	<tr>
											    				<td>
														    		<%=middlewareReportTitleSB%>
														    	</td>
														  	</tr>
											        	</table>
				        							</td>
				        						</tr>
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
																						<td class="report-data-body-title" style="text-align: left">&nbsp;&nbsp;&nbsp;
																							开始日期
																								<input type="text" name="startdate" value="<%=startdate%>" size="10">
																								<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
																								<img id=imageCalendar1 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a>
																							
																								截止日期
																								<input type="text" name="todate" value="<%=todate%>" size="10"/>
																								<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
																								<img id=imageCalendar2 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a>
																								&nbsp;&nbsp;<input type="button" name="process" value="生成报表" onclick="#">
																						</td>
																					</tr>
																				</table>
																			</td>
																		</tr>
																		<tr>
                      														<td class="report-data-body-title" style="text-align: right;">
																				<a href="<%=rootPath%>/midcapreport.do?action=createdoc"><img name="selDay1" alt='导出word' style="CURSOR: hand" src="<%=rootPath%>/resource/image/export_word.gif" width=18 border="0">导出WORLD</a>&nbsp;&nbsp;&nbsp;&nbsp;
																				<a href="<%=rootPath%>/midcapreport.do?action=downloadselfpingexcel&startdate=<%=startdate%>&todate=<%=todate%>" target="_blank"><img name="selDay1" alt='导出EXCEL' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=18  border="0">导出EXCEL</a>&nbsp;&nbsp;&nbsp;&nbsp;
																				<a href="<%=rootPath%>/midcapreport.do?action=createPDF"><img name="selDay1" alt='导出word' style="CURSOR: hand" src="<%=rootPath%>/resource/image/export_pdf.gif" width=18 border="0">导出PDF</a>&nbsp;&nbsp;&nbsp;&nbsp;
						                      								</td> 
						                      							</tr>
																	</table>
																</td>
															</tr>
															<tr>
				        										<td>
				        											<table id="report-data-body" class="report-data-body">
													        			<tr height="29">
						    												<td class="report-data-body-title">序号</td>
								  											<td class="report-data-body-title">名称</td>
								  											<td class="report-data-body-title">IP地址</td>
								  											<td class="report-data-body-title">
								  												<input type="button"  name="button3" onclick="changeOrder('ping')" value="服务器不可用次数">
								  											</td>
						   												</tr>
																		<%
																			int index = 0;
																			//I_MonitorIpList monitorManager=new MonitoriplistManager();
																			List pinglist = (List)request.getAttribute("eventlist");
																			System.out.println(pinglist+"==================tomcat=pinglist");
																			if(pinglist != null && pinglist.size()>0){
																				for(int i=0;i<pinglist.size();i++){
																						List _pinglist = (List)pinglist.get(i);
																						System.out.println(_pinglist+"=============_pinglist=============");
																						String ip = (String)_pinglist.get(0);
																						String equname = (String)_pinglist.get(1);
																						String downnum = (String)_pinglist.get(2);
																		%>
									 									<tr <%=onmouseoverstyle%> height="25">
    																		<td class="report-data-body-list"><%=i+1%></td>
    																		<td class="report-data-body-list"><%=equname%></td>
       																		<td class="report-data-body-list"><%=ip%></td>
       																		<td class="report-data-body-list"><%=downnum%></td>
 																		</tr>
									 									<%
																	 			}
																			}
																			List pinglistiis = (List)request.getAttribute("eventlistiis");
																			System.out.println(pinglistiis+"===================iispinglist");
																			if(pinglistiis != null && pinglistiis.size()>0){
																				for(int i=0;i<pinglistiis.size();i++){
																						List _pinglist = (List)pinglistiis.get(i);
																						String ip = (String)_pinglist.get(0);
																						String equname = (String)_pinglist.get(1);
																						String downnum = (String)_pinglist.get(2);
																		%>
										  								<tr <%=onmouseoverstyle%> height=25>
					    													<td class="report-data-body-title"><%=i+1+pinglist.size()%></td>
					    													<td class="report-data-body-title"><%=equname%></td>
					       													<td class="report-data-body-title"><%=ip%></td>
					       													<td class="report-data-body-title"><%=downnum%></td>
					 													</tr>
							 											<%
																				}
																			}
																			List pinglistweblogic = (List)request.getAttribute("eventlistweblogic");
																			System.out.println(pinglistweblogic+"==================pinglistweblogic");
																			if(pinglistweblogic != null && pinglistweblogic.size()>0){
																				for(int i=0;i<pinglistweblogic.size();i++){
																						List _pinglist = (List)pinglistweblogic.get(i);
																						String ip = (String)_pinglist.get(0);
																						String equname = (String)_pinglist.get(1);
																						String downnum = (String)_pinglist.get(2);
																		%>
									  									<tr <%=onmouseoverstyle%> height=25>
    																		<td class="report-data-body-title"><%=i+1+pinglist.size()+pinglistiis.size()%></td>
					       													<td class="report-data-body-title"><%=equname%></td>
					       													<td class="report-data-body-title"><%=ip%></td>
					       													<td class="report-data-body-title"><%=downnum%></td>
 																		</tr>
																		<%
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
		
	</form>
</BODY>
</HTML>