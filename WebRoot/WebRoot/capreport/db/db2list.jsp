<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%@page import="com.afunms.application.model.DBVo"%>
<%@page import="com.afunms.application.model.DBTypeVo"%>
<%@page import="com.afunms.application.dao.DBTypeDao"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@ page import="com.afunms.event.model.EventList"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.topology.model.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>

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

<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8"/>
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>



<script language="JavaScript" type="text/JavaScript">

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
 	window.open ("<%=rootPath%>/dbreport.do?action=downloadmultidb2report2&startdate="+startdate+"&todate="+todate+"&ids="+oids, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes")    	      

}

function openwin(str,operate,ip) 
{
  var startdate = mainForm.startdate.value;
  var todate = mainForm.todate.value;
  window.open ("dbreport.do?action="+operate+"&startdate="+startdate+"&todate="+todate+"&ipaddress="+ip+"&str="+str, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes") 
}
function changeOrder(para){
	mainForm.orderflag.value = para;
	mainForm.action="<%=rootPath%>/netreport.do?action=netping"
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
     
        Ext.MessageBox.wait('数据加载中，请稍后.. '); 
        //msg.style.display="block";
	mainForm.action="<%=rootPath%>/dbreport.do?action=dbping";
	mainForm.submit();        
        //mainForm.action = "<%=rootPath%>/network.do?action=add";
        //mainForm.submit();
     //}  
       // mainForm.submit();
 });	
	
});
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
	document.getElementById('dbReportTitle-2').className='detail-data-title';
	document.getElementById('dbReportTitle-2').onmouseover="this.className='detail-data-title'";
	document.getElementById('dbReportTitle-2').onmouseout="this.className='detail-data-title'";
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
     mainForm.action = "<%=rootPath%>/dbreport.do?action=find&dbflag=5";
     mainForm.submit();
  }
</script>


</head>
<body id="body" class="body" onload="initmenu();">
	<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 189px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
	<form id="mainForm" method="post" name="mainForm">
	<input type=hidden name="eventid">
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
														    		<%=dbReportTitleTable%>
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
																						<td>
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
																								&nbsp;&nbsp;<input type="button" name="doprocess" value="生成综合报表" onclick="exeReport()">
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
				        												
				        												<tr> 
                					<td>
                						<table width="100%" border="0" cellpadding="0" cellspacing="1">
                						<table>
  									<tr class="microsoftLook0" height=28>
    	<td class="report-data-body-title" width="10%" rowspan=2 align=center>&nbsp;&nbsp;<INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()">序号</td>
        <td class="report-data-body-title" width="10%" rowspan=2 align=center><strong>IP地址</strong></td>
        <td class="report-data-body-title" width="10%" rowspan=2 align=center><strong>数据库类型</strong></td>
    	<td class="report-data-body-title" width="10%" rowspan=2 align=center><strong>数据库名称</strong></td>
    	<td class="report-data-body-title" width="10%" rowspan=2 align=center><strong>数据库应用</strong></td>
    	<td class="report-data-body-title" width="60%" align=center colspan="5"><strong>生成报表</strong></td>
   </tr>
  <tr class="microsoftLook0" height=28>
    <td class="report-data-body-title" align=center  width="10%">EXCEL综合</td>
    <td class="report-data-body-title" align=center  width="10%">WORD综合</td>
    <td class="report-data-body-title" align=center  width="10%">PDF综合</td>
    <td class="report-data-body-title" align=center  width="10%">运行分析（WORD）</td>
    <td class="report-data-body-title" align=center  width="20%">运行分析（PDF）</td>
   </tr>
<%
    DBVo node = null;
  	java.text.SimpleDateFormat _sdf = new java.text.SimpleDateFormat("MM-dd HH:mm");
  	for(int i=0;i<list.size();i++){
  	node = (DBVo)list.get(i);

       DBTypeDao typedao = new DBTypeDao();
       int dbtype= node.getDbtype();
       DBTypeVo typevo = null; 
	   try{
		typevo = (DBTypeVo)typedao.findByID(dbtype+"");
       }catch(Exception e){
       }finally{
       		typedao.close();
       }

%>

 <tr bgcolor="#ffffff" <%=onmouseoverstyle%> class="microsoftLook" height=25>

   
   
   
    <td class="report-data-body-list">&nbsp;&nbsp;<INPUT type="checkbox" class=noborder id="b<%=i+1%>" name=checkbox value="<%=node.getId()%>"><%=i+1%></td>
       <td class="report-data-body-list"><%=node.getIpAddress()%>&nbsp;</td>
       <td class="report-data-body-list"><%=typevo.getDbtype()%>&nbsp;</td>
      <td class="report-data-body-list"><%=node.getDbName()%></td>
	   <td class="report-data-body-list"><%=node.getDbuse()%></td>
	    <td class="report-data-body-list" align=center>      
	&nbsp;&nbsp;<img  name="submitss" src="<%=rootPath%>/resource/image/export_excel.gif" width=18 border="0" onclick="document.getElementById('b<%=i+1%>').checked = true;exeReport();" target="_blank">      
      </td>
        <td class="report-data-body-list" align=center>     
	&nbsp;&nbsp;<img  name="submitss" src="<%=rootPath%>/resource/image/export_word.gif" width=18 border="0" onclick=openwin(0,"createDB2CldReport","<%=node.getIpAddress()%>") target="_blank">      
      </td>
        <td class="report-data-body-list" align=center>       
	&nbsp;&nbsp;<img  name="submitss" src="<%=rootPath%>/resource/image/export_pdf.gif" width=18 border="0" onclick=openwin(1,"createDB2CldReport","<%=node.getIpAddress()%>") target="_blank">      
      </td>
        <td class="report-data-body-list" align=center>       
	&nbsp;&nbsp;<img  name="submitss" src="<%=rootPath%>/resource/image/export_word.gif" width=18 border="0" onclick=openwin(3,"downloaddb2selfreport","<%=node.getIpAddress()%>") target="_blank">      
      </td>
       <td class="report-data-body-list" align=center>      
	&nbsp;&nbsp;<img  name="submitss" src="<%=rootPath%>/resource/image/export_pdf.gif" width=18 border="0" onclick=openwin(4,"downloaddb2selfreport","<%=node.getIpAddress()%>") target="_blank">      
      </td>
	   
 </tr>
 <%}
   
 %>  
   
																	   
																	</table>
															
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
</BODY>
</HTML>