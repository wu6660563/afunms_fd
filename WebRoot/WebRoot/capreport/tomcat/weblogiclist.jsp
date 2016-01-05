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
  	String menuTable = (String)request.getAttribute("menuTable");
	String startdate = (String)request.getAttribute("startdate");
	String todate = (String)request.getAttribute("todate");
	List list = (List)request.getAttribute("list");
	int rc = 0;
	if(list!=null&&list.size()>0){
		rc = list.size();
	}
  	
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

function openwin(str,operate,ip) 
{
  var startdate = mainForm.startdate.value;
  var todate = mainForm.todate.value;
  window.open ("weblogiccapreport.do?action="+operate+"&startdate="+startdate+"&todate="+todate+"&ipaddress="+ip+"&str="+str, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes") 
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
  
  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
 Ext.get("process").on("click",function(){
     
     //if(chk1&&chk2&&chk3)
     //{
     
        //Ext.MessageBox.wait('数据加载中，请稍后.. '); 
        //msg.style.display="block";
	//mainForm.action="<%=rootPath%>/iiscatreport.do?action=downloadselfiisreport";
	//mainForm.submit();     
	exeReport();   
        //mainForm.action = "<%=rootPath%>/network.do?action=add";
        //mainForm.submit();
     //}  
       // mainForm.submit();
 });	
	
});

function exeReport(){
  	var startdate = mainForm.startdate.value;
  	var todate = mainForm.todate.value;      
  	var oids ="";
  	var checkbox = document.getElementsByName("checkbox");
 		for (var i=0;i<checkbox.length;i++){
 			if(checkbox[i].checked==true){
 				if (oids==""){
 					oids=checkbox[i].value;
 				}else{
 					oids=oids+","+checkbox[i].value;
 				}
 			}
 		}
 	if(oids==null||oids==""){
 		alert("请至少选择一个设备");
 		return;
 	}
 	window.open ("<%=rootPath%>/weblogiccapreport.do?action=downloadselfweblogicreportAll&startdate="+startdate+"&todate="+todate+"&ids="+oids, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes")    	      
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
	document.getElementById('middlewareReportTitle-3').className='detail-data-title';
	document.getElementById('middlewareReportTitle-3').onmouseover="this.className='detail-data-title'";
	document.getElementById('middlewareReportTitle-3').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}

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
     mainForm.action = "<%=rootPath%>/midcapreport.do?action=find&dbflag=4";
     mainForm.submit();
  }
</script>


</head>
<body id="body" class="body" onload="initmenu();">
	<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
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
																				<table id="report-data-header-title" class="report-data-body-title">
																					<tr>
																						<td class="report-data-body-title" style="text-align: left">&nbsp;&nbsp;&nbsp;
																								开始日期
																								<input type="text" name="startdate" value="<%=startdate%>" size="10">
																								<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
																								<img id=imageCalendar1 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a>
																							
																								截止日期
																								<input type="text" name="todate" value="<%=todate%>" size="10"/>
																								<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
																								&nbsp;<img id=imageCalendar2  width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a> 所属业务<input type=text readonly="readonly" onclick='setBid("bidtext" , "bid");' id="bidtext" name="bidtext" size="25" maxlength="25" value="">
																								<input type="hidden" id="bid" name="bid" value="">
																								<input type="button" name="submitss" value="查  询" onclick="query()">
																								&nbsp;&nbsp;<input type="button" name="process" value="生成报表" onclick="#">
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
													        			<tr height=28>
																	    	<td width="10%" rowspan=2 class="report-data-body-title">&nbsp;&nbsp;<INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()">序号</td>
																	        <td width="15%" rowspan=2 class="report-data-body-title">名称</td>
																	        <td width="15%" rowspan=2 class="report-data-body-title">IP地址</td>
																	    	<td width="10%" rowspan=2 class="report-data-body-title">团体名称</td>
																	    	<td width="50%" colspan="5" class="report-data-body-title">生成报表</td>
																	   	</tr>
																	  	<tr height=28>
																	    	<td align=center  width="10%" class="report-data-body-title">EXCEL综合</td>
																	    	<td align=center  width="10%" class="report-data-body-title">WORD综合</td>
																	    	<td align=center  width="10%" class="report-data-body-title">PDF综合</td>
																	    	<td align=center  width="10%" class="report-data-body-title">运行分析（WORD）</td>
																	    	<td align=center  width="10%" class="report-data-body-title">运行分析（PDF）</td>
																	  	</tr>
																		<%
																		if(list != null&&list.size() > 0){
																		     for(int i=0;i<rc;i++){
																		     	WeblogicConfig vo = (WeblogicConfig)list.get(i);
																		     	Node node = PollingEngine.getInstance().getWeblogicByID(vo.getId());
																		       	String alarmmessage = "";
																		       	int status = 0;
																		       	if(node != null){
																		       		status = node.getStatus();
																		       		List alarmlist = node.getAlarmMessage();
																		       		if(alarmlist!= null && alarmlist.size()>0){
																						for(int k=0;k<alarmlist.size();k++){
																							alarmmessage = alarmmessage+alarmlist.get(k).toString();
																						}
																					}
																				}  
																		%>
																		 <tr <%=onmouseoverstyle%> class="microsoftLook" height=25>
																		 	<td class="report-data-body-list">&nbsp;&nbsp;<INPUT type="checkbox" class=noborder name=checkbox value="<%=vo.getId()%>"><%=i+1%></td>
																			<td class="report-data-body-list"><%=vo.getAlias()%>&nbsp;</td>
																			<td class="report-data-body-list"><%=vo.getIpAddress()%>&nbsp;</td>
																			<td class="report-data-body-list"><%=vo.getCommunity()%></td>
																			<td align=center class="report-data-body-list">      
																				<img  name="submitss" src="<%=rootPath%>/resource/image/export_excel.gif" width=18 border="0" onclick=openwin(0,"downloadselfweblogicreport","<%=node.getIpAddress()%>") target="_blank">      
																		   	</td>
																		   	<td align=center class="report-data-body-list">     
																				<img  name="submitss" src="<%=rootPath%>/resource/image/export_word.gif" width=18 border="0" onclick=openwin(1,"downloadselfweblogicreport","<%=node.getIpAddress()%>") target="_blank">      
																		 	</td>
																		  	<td align=center class="report-data-body-list">       
																				<img  name="submitss" src="<%=rootPath%>/resource/image/export_pdf.gif" width=18 border="0" onclick=openwin(4,"downloadselfweblogicreport","<%=node.getIpAddress()%>") target="_blank">      
																		  	</td>
																		   	<td align=center class="report-data-body-list">       
																				<img  name="submitss" src="<%=rootPath%>/resource/image/export_word.gif" width=18 border="0" onclick=openwin(2,"downloadselfweblogicreport","<%=node.getIpAddress()%>") target="_blank">      
																		  	</td>
																		  	<td align=center class="report-data-body-list">      
																				<img  name="submitss" src="<%=rootPath%>/resource/image/export_pdf.gif" width=18 border="0" onclick=openwin(3,"downloadselfweblogicreport","<%=node.getIpAddress()%>") target="_blank">      
																		  	</td>
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