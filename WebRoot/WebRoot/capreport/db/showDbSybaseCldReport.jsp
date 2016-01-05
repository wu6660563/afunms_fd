<%@ page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@ page import="com.afunms.application.model.DBVo"%>
<%@ page import="com.afunms.application.model.TablesVO"%>
<%@ page import="com.afunms.application.model.SybaseVO" %>
<%@ page import="java.util.*" %>
<%@ page import="com.afunms.application.model.DBTypeVo"%>
<%@ page import="com.afunms.event.model.EventList"%>

<html>
<head>
<%	
	SybaseVO sysbaseVO = (SybaseVO)request.getAttribute("sysbaseVO");//表空间
	if(sysbaseVO == null)sysbaseVO = new SybaseVO();	
    DBVo vo = (DBVo)request.getAttribute("vo");
    DBTypeVo typevo = (DBTypeVo)request.getAttribute("typevo");
    String pingmin = (String)request.getAttribute("pingmin");
    String avgpingcon = (String)request.getAttribute("avgpingcon"); 
    String pingnow = (String)request.getAttribute("pingnow"); 
    
	Integer count = (Integer)request.getAttribute("count");//获得告警次数
	String downnum= (String)request.getAttribute("downnum");//宕机数
	String grade =  (String)request.getAttribute("grade");
	String runstar = "";
	runstar = (String)request.getAttribute("runstr");
	
  	String rootPath = request.getContextPath();
  	String newip = (String)request.getAttribute("newip");
  	String ip = (String)request.getAttribute("ipaddress");
  	String startdate = (String)request.getAttribute("startdate");
  	String todate = (String)request.getAttribute("todate");
  	
  	int level1 = Integer.parseInt(request.getAttribute("level1")+"");
	int _status = Integer.parseInt(request.getAttribute("status")+"");
	String level1str="";
	String level2str="";
	String level3str="";
	if(level1 == 1){
		level1str = "selected";
	}else if(level1 == 2){
		level2str = "selected";
	}else if(level1 == 3){
		level3str = "selected";	
	}
    String status0str="";
	String status1str="";
	String status2str="";
	if(_status == 0){
		status0str = "selected";
	}else if(_status == 1){
		status1str = "selected";
	}else if(_status == 2){
		status2str = "selected";	
	}	
%>
<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
<!-- 综合报表 sybase-->
<title>综合报表</title>
<script type='text/javascript' src='/afunms/dwr/interface/DWRUtil.js'></script>
<script type='text/javascript' src='/afunms/dwr/engine.js'></script>
<script type='text/javascript' src='/afunms/dwr/util.js'></script>
<script language="javascript" src="/afunms/js/tool.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
<script language="JavaScript" type="text/JavaScript">
function ping_excel_report(){
	mainForm.action = "<%=rootPath%>/dbreport.do?action=createSybaseCldReport&ipaddress=<%=ip%>&str=2";
	mainForm.submit();
}
function ping_word_report(){
	mainForm.action = "<%=rootPath%>/dbreport.do?action=createSybaseCldReport&ipaddress=<%=ip%>&str=0";
	mainForm.submit();
}
function ping_pdf_report(){
	mainForm.action = "<%=rootPath%>/dbreport.do?action=createSybaseCldReport&ipaddress=<%=ip%>&str=1";
	mainForm.submit();
}
function ping_ok(){
	var starttime = document.getElementById("mystartdate").value;
	var endtime = document.getElementById("mytodate").value;
	var status = document.getElementById("status").value;
	var level1 = document.getElementById("level1").value;
	//mainForm.action = "<%=rootPath%>/hostreport.do?action=showPingReport&ipaddress= &startdate="+starttime+"&todate="+endtime;
	mainForm.action = "<%=rootPath%>/sybase.do?action=sybaseManagerCldReportQuery&ipaddress= &startdate="+starttime+"&todate="+endtime+"&status="+status+"&level1="+level1;
	mainForm.submit();
}
function ping_cancel(){
window.close();
}
function query(){
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
 	window.open ("<%=rootPath%>/hostreport.do?action=downloadmultihostreport&startdate="+startdate+"&todate="+todate+"&ids="+oids, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes")    	      
}
function openwin(str,operate,ip) {	
  var startdate = mainForm.startdate.value;
  var todate = mainForm.todate.value;
  window.open ("<%=rootPath%>/hostreport.do?action="+operate+"&startdate="+startdate+"&todate="+todate+"&ipaddress="+ip+"&str="+str+"&type=host", "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes") 
}
function changeOrder(para){
	mainForm.orderflag.value = para;
	mainForm.action="<%=rootPath%>/netreport.do?action=netping"
  	mainForm.submit();
}
 Ext.onReady(function(){  
		setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
 		
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
	document.getElementById('hostReportTitle-5').className='detail-data-title';
	document.getElementById('hostReportTitle-5').onmouseover="this.className='detail-data-title'";
	document.getElementById('hostReportTitle-5').onmouseout="this.className='detail-data-title'";
}
function refer(action){
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}
</script>
<!-- snow add 2010-5-28 -->
<style>
<!--
body{
background-image: url(${pageContext.request.contextPath}/common/images/menubg_report.jpg);
TEXT-ALIGN: center; 
}
-->
</style>
</head>
<body id="body" class="body" onload="init();">
	<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 189px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
	<form id="mainForm" method="post" name="mainForm">
	<input type=hidden id="ipaddress" name="ipaddress" value=<%=request.getParameter("ipaddress") %>>
		<table id="container-main" class="container-main">
			<tr>
				<td>
					<table id="container-main-win" class="container-main-win">
						<tr>
							<td>
								<table id="win-content" class="win-content">
									<tr>
										<td>
											<table id="win-content-header" class="win-content-header">
					                			<tr>
								                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
								                	<td class="win-content-title" style="align:center">&nbsp;综合报表</td>
								                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>											   
												</tr>
						       				</table>
					       				</td>
					       			</tr>
							       	<tr>
							       		<td>
							       			<table id="win-content-body" class="win-content-body">
												<tr>
							       					<td>
														<table bgcolor="#ECECEC">
															<tr align="left" valign="center"> 
															<td height="28" align="left">
																开始日期
																					<input type="text" id="mystartdate" name="startdate" value="<%=startdate%>" size="10">
																					<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
																					<img id=imageCalendar1 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a>
																													
																					截止日期
																					<input type="text" id="mytodate" name="todate" value=" <%=todate%>" size="10"/>
																					<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
																					<img id=imageCalendar2 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a>
																					
																					
																					 事件等级
																					<select name="level1">
																						<option value="99">
																							不限
																						</option>
																						<option value="1" <%=level1str%>>
																							普通事件
																						</option>
																						<option value="2" <%=level2str%>>
																							严重事件
																						</option>
																						<option value="3" <%=level3str%>>
																							紧急事件
																						</option>
																					</select>
			
																					处理状态
																					<select id="status" name="status">
																						<option value="99">
																							不限
																						</option>
																						<option value="0" <%=status0str%>>
																							未处理
																						</option>
																						<option value="1" <%=status1str%>>
																							正在处理
																						</option>
																						<option value="2" <%=status2str%>>
																							已处理
																						</option>
																					</select>
																					<input type="button" id="process" name="process"
																						value="查 询" onclick="ping_ok();">
															</td>
															<td height="28" align="left">
																<a href="javascript:ping_word_report()"><img name="selDay1" alt='导出word' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_word.gif" width=18  border="0">导出WORLD</a>
															</td>
															<td height="28" align="left">
																<a href="javascript:ping_excel_report()"><img name="selDay1" alt='导出EXCEL' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=18  border="0">导出EXCEL</a>
															</td>
															<td height="28" align="left">&nbsp;
																<a href="javascript:ping_pdf_report()"><img name="selDay1" alt='导出word' style="CURSOR: hand" src="<%=rootPath%>/resource/image/export_pdf.gif" width=18 border="0">导出PDF</a>
															</td>
															</tr>  
														</table>
						       						</td>
						       					</tr>
												<tr>
							                		<td class="win-data-title" style="height: 29px;" ></td>
							       				</tr>
							       				<tr align="left" valign="center"> 
			             							<td height="28" align="left" border="0">
														<input type=hidden name="eventid">
														<div id="loading">
														<div class="loading-indicator">
															<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif" width="32" height="32" style="margin-right: 8px;" align="middle" />Loading...</div>
														</div>
														<table border="1" width="90%">
															<tr bgcolor="#ECECEC">
																<td>数据库名称</td><td>IP</td><td>类型</td><td>当前状态</td><td>运行评价</td><td>当前连通率</td><td>平均连通率</td><td>最小连通率</td>
															</tr>
															<tr>
																<td><%=vo.getDbName() %></td><td><%=vo.getIpAddress() %></td><td><%=typevo.getDbtype() %></td><td><%=runstar %></td><td><%=grade %></td><td><%=pingnow %>%</td><td><%=avgpingcon %>%</td> <td><%=pingmin %>%</td>
															</tr>
															<tr>
																<td colspan="8" align="center">
																	<img src="<%=rootPath%>/resource/image/jfreechart/<%=newip%>ConnectUtilization.png"/>
																</td>
															</tr>
														</table>
			             							</td>
												</tr> 
												<tr>
							                		<td class="win-data-title" style="height: 29px;" ></td>
							       				</tr> 
							       				<tr>
							       					<td>
							       						<table class="application-detail-data-body" width="989" height="149">
							       						<tr>
																<td>
																	<table width="100%" border="0" cellspacing="0"cellpadding="0">
																		<tr height="28" bgcolor="#ECECEC">
																			<td align=center>表 空 间</td>
																		</tr>
																	</table>
																</td>
															</tr>
															<tr>
																<td>
																	<table border="1" width="90%">
																		<tr bgcolor="#ECECEC">
																			<td align=center>
																				&nbsp;
																			</td>
																			<td align=center >
																				表空间
																			</td>
																			<td align=center >
																				空间大小（MB）
																			</td>
																			<td align=center >
																				空闲大小（MB）
																			</td>
																			<td align=center >
																				利用率(%)
																			</td>
																		</tr>
																		<%
																		List dbInfo = (ArrayList)sysbaseVO.getDbInfo();
																		if(dbInfo != null && dbInfo.size()>0){
									
																		for(int i=0;i<dbInfo.size();i++){
																		TablesVO tempSpace = (TablesVO)dbInfo.get(i);
																		 String db_name = (String)tempSpace.getDb_name();
																	 	String db_size = (String)tempSpace.getDb_size();
																	 	String db_freesize = (String)tempSpace.getDb_freesize();
																	 	String db_usedperc = (String)tempSpace.getDb_usedperc();
																	 	
																			%>
																			<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																				height="28">
																				<td class="application-detail-data-body-list"><%=i+1%></td>
																				<td align=center class="application-detail-data-body-list">
																					&nbsp;<%=db_name%></td>
																				<td align=center class="application-detail-data-body-list">
																					&nbsp;<%=db_size%></td>
																				<td align=center class="application-detail-data-body-list">
																					&nbsp;<%=db_freesize%></td>
																				<td align=center class="application-detail-data-body-list">
																					&nbsp;<%=db_usedperc%></td>
																			</tr>
																			<%}
																		}%>
																		</table>
																	
																</td>
															</tr>
															<tr>
							                					<td class="win-data-title" style="height: 29px;" ></td>
							       							</tr>
															<tr>
																<td>
																	<table width="100%" border="0" cellspacing="0"cellpadding="0">
																		<tr height="28" bgcolor="#ECECEC">
																			<td align=center >存储信息 </td>
																		</tr>
																	</table>
																</td>
															</tr>
							       							<tr align="left" valign="center"> 
			             										<td height="28" align="left" border="0">
																	<table border="1" width="90%">
																		<tr bgcolor="#ECECEC">
																			<td>总数据高速缓存大小(MB)</td><td>总物理内存大小(MB) </td><td>Metadata缓存(MB)</td><td>存储过程缓存大小(MB) </td><td>总逻辑内存大小(MB) </td><td>数据缓存匹配度(%)  </td>
																		</tr>
																		<tr>
																			<td><%=sysbaseVO.getTotal_dataCache() %></td>
																			<td><%=sysbaseVO.getTotal_physicalMemory() %></td>
																			<td><%=sysbaseVO.getMetadata_cache()%></td>
																			<td><%=sysbaseVO.getProcedure_cache()%></td>
																			<td><%=sysbaseVO.getTotal_logicalMemory()%></td>
																			<td><%=sysbaseVO.getData_hitrate()%></td>
																		</tr>
																	</table>
			             										</td>
															</tr>  
															<tr>
							                					<td class="win-data-title" style="height: 29px;" ></td>
							       							</tr>
							       							<tr align="left" bgcolor="#ECECEC">
																<td height="28" align=center>
																	<b>&nbsp;Sysbase 远程服务器信息</b>
																</td>
															</tr>
															<tr bgcolor="#ECECEC">
																<td align=center>

																	<table width="80%" border="0" cellpadding="0"
																		cellspacing="1" bgcolor="#ECECEC">
																		<tr bgcolor="#ECECEC" height=28>
																			<td width="25%"
																				class="application-detail-data-body-title">
																				<strong>服务器名称</strong>
																			</td>
																			<td width="25%"
																				class="application-detail-data-body-title">
																				<strong>服务器网络名称</strong>
																			</td>
																			<td width="15%"
																				class="application-detail-data-body-title">
																				<strong>所属类别</strong>
																			</td>
																			<td width="35%"
																				class="application-detail-data-body-title">
																				<strong>服务器状态</strong>
																			</td>
																		</tr>
																		<%
														                    List serverlist = (List)sysbaseVO.getServersInfo();
														                    if (serverlist != null && serverlist.size()>0){
														                    	for(int k=0;k<serverlist.size();k++){
														                    		TablesVO tVO = (TablesVO)serverlist.get(k);
														                   %>
																		<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																			height="28">
																			<td class="application-detail-data-body-list"><%=tVO.getServer_name()%></td>
																			<td class="application-detail-data-body-list"><%=tVO.getServer_network_name()%></td>
																			<td class="application-detail-data-body-list"><%=tVO.getServer_class()%></td>
																			<td class="application-detail-data-body-list"><%=tVO.getServer_status()%></td>
																		</tr>
																		<%
														                   		}
													                   	}
													                   %>
																	</table>
																</td>
															</tr>
															<tr>
							                					<td class="win-data-title" style="height: 29px;" ></td>
							       							</tr>

															<tr align="left" bgcolor="#ECECEC">
																<td height="28" align=center>
																	<b>&nbsp;Sysbase 数据库用户信息</b>
																</td>
															</tr>

															<tr bgcolor="#ECECEC">
																<td align=center>

																	<table width="80%" border="0" cellpadding="0"
																		cellspacing="1" bgcolor="#ECECEC">
																		<tr bgcolor="#ECECEC" height=28>
																			<td width="25%"
																				class="application-detail-data-body-title">
																				<strong>用户名称</strong>
																			</td>
																			<td width="25%"
																				class="application-detail-data-body-title">
																				<strong>数据库中的id</strong>
																			</td>
																			<td width="25%"
																				class="application-detail-data-body-title">
																				<strong>所属组组名</strong>
																			</td>
																			<td width="25%"
																				class="application-detail-data-body-title">
																				<strong>登陆名称</strong>
																			</td>
																		</tr>
																		<%
														                    List userlist = (List)sysbaseVO.getUserInfo();
														                    if (userlist != null && userlist.size()>0){
														                    	for(int k=0;k<userlist.size();k++){
														                    		TablesVO tVO = (TablesVO)userlist.get(k);
														                   %>
																		<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																			height=28>
																			<td class="application-detail-data-body-list"><%=tVO.getUsers_name()%></td>
																			<td class="application-detail-data-body-list"><%=tVO.getID_in_db()%></td>
																			<td class="application-detail-data-body-list"><%=tVO.getGroup_name()%></td>
																			<td class="application-detail-data-body-list"><%=tVO.getLogin_name()%></td>
																		</tr>
																		<%
													                   		}
													                   	}
													                   %>
																	</table>
																	<br>
																</td>
															</tr>
															<tr>
							                					<td class="win-data-title" style="height: 29px;" ></td>
							       							</tr>
							       							<tr align="left" bgcolor="#ECECEC">
																<td height="28" align=center>
																	<b>&nbsp;Sysbase 数据库设备信息</b>
																</td>
															</tr>
															<tr bgcolor="#FFFFFF">
																<td align=center>

																	<table width="80%" border="0" cellpadding="3"
																		cellspacing="1" bgcolor="#ECECEC">
																		<tr bgcolor="#ECECEC" height=28>
																			<td width="25%"
																				class="application-detail-data-body-title">
																				<strong>设备名称</strong>
																			</td>
																			<td width="25%"
																				class="application-detail-data-body-title">
																				<strong>设备物理名称</strong>
																			</td>
																			<td width="50%"
																				class="application-detail-data-body-title">
																				<strong>设备描述</strong>
																			</td>
																		</tr>
																		<%
													                        List devicelist = (List)sysbaseVO.getDeviceInfo();
													                        if (devicelist != null && devicelist.size()>0){
													                        	for(int k=0;k<devicelist.size();k++){
													                        		TablesVO tVO = (TablesVO)devicelist.get(k);
													                       %>
																		<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																			height=28>
																			<td class="application-detail-data-body-list"><%=tVO.getDevice_name()%></td>
																			<td class="application-detail-data-body-list"><%=tVO.getDevice_physical_name()%></td>
																			<td class="application-detail-data-body-list"><%=tVO.getDevice_description()%></td>
																		</tr>
																		<%
												                       		}
												                       	}
												                       %>
																	</table>
																</td>
															</tr>

															<tr>
							                					<td class="win-data-title" style="height: 29px;" ></td>
							       							</tr>
															<tr>
																<td>
																	<table width="100%" border="0" cellspacing="0"cellpadding="0">
																		<tr height="28" bgcolor="#ECECEC">
																			<td align=center> 事 件 汇 报 </td>
																		</tr>
																	</table>
																</td>
															</tr>
															<tr align="left" valign="center"> 
			             										<td height="28" align="left" border="0">
																	<table border="1" width="90%">
																		<tr bgcolor="#ECECEC">
																			<td>发生连通率事件（次）</td>
																			<td>库空间超过阀值事件（次）
																			</td>
																		</tr>
																		<tr>
																		<td><%=downnum %></td><td><%=count %></td>
																		</tr>
																	</table>
			             										</td>
															</tr>
															<tr>
							                					<td class="win-data-title" style="height: 29px;" ></td>
							       							</tr>
															<tr>
																	<td>
																		<table width="100%" border="1" cellspacing="0"cellpadding="0">
																			<tr height="28" bgcolor="#ECECEC">
																				<td align=center >事件列表</td>
																			</tr>
																		</table>
																	</td>
																</tr>
																<tr bgcolor="DEEBF7">
																	<td>
																		<table width="100%" border="0" cellpadding="3"
																			cellspacing="1" bgcolor="#FFFFFF">

																			<tr height="28" bgcolor="#ECECEC">
																				<td class="application-detail-data-body-title">
																					&nbsp;
																				</td>
																				<td class="application-detail-data-body-title"
																					width="10%">
																					<strong>事件等级</strong>
																				</td>
																				<td class="application-detail-data-body-title"
																					width="40%">
																					<strong>事件描述</strong>
																				</td>
																				<td class="application-detail-data-body-title">
																					<strong>登记日期</strong>
																				</td>
																				<td class="application-detail-data-body-title">
																					<strong>登记人</strong>
																				</td>
																				<td class="application-detail-data-body-title">
																					<strong>处理状态</strong>
																				</td>
																			</tr>  
																			<%
																				int index = 0;
																			  	java.text.SimpleDateFormat _sdf = new java.text.SimpleDateFormat("MM-dd HH:mm");
																			  	List list = (ArrayList)request.getAttribute("list");
																			  	if(list == null){
																			  	System.out.println("list---null");
																			  	}
																			  	if(list != null && list.size()>0){
																				  	for(int i=0;i<list.size();i++){
																				 	index++;
																				  	EventList eventlist = (EventList)list.get(i);
																				  	Date cc = eventlist.getRecordtime().getTime();
																				  	Integer eventid = eventlist.getId();
																				  	String eventlocation = eventlist.getEventlocation();
																				  	String content = eventlist.getContent();
																				  	String level = String.valueOf(eventlist.getLevel1());
																				  	String status = String.valueOf(eventlist.getManagesign());
																				  	String s = status;
																					String showlevel = null;
																				  	String act="处理报告";
																				  	if("1".equals(level)){
																				  		showlevel="普通事件";
																				  	}else if("2".equals(level)){
																				  		showlevel="严重事件";
																				  	}else{
																					    showlevel="紧急告警";
																					}
																				   	  	if("0".equals(status)){
																				  		status = "未处理";
																				  	}
																				  	if("1".equals(status)){
																				  		status = "处理中";  	
																				  	}
																				  	if("2".equals(status)){
																				  	  	status = "处理完成";
																				  	}
																				  	String rptman = eventlist.getReportman();
																				  	String rtime1 = _sdf.format(cc);
																			%>

																			<tr bgcolor="#FFFFFF" >

																				<td class="application-detail-data-body-list">
																					&nbsp;<%=index%></td>
																				<%
																			    	if("3".equals(level)){
																			    %>
																				<td
																					style="border-left: 1px solid #EEEEEE; border-bottom: 1px solid #EEEEEE;"
																					bgcolor=red align=center><%=showlevel%>&nbsp;
																				</td>
																				<%
																			       }else if("2".equals(level)){
																			       %>
																				<td
																					style="border-left: 1px solid #EEEEEE; border-bottom: 1px solid #EEEEEE;"
																					bgcolor=orange align=center><%=showlevel%>&nbsp;
																				</td>
																				<%
																			       }else{
																			       %>
																				<td
																					style="border-left: 1px solid #EEEEEE; border-bottom: 1px solid #EEEEEE;"
																					bgcolor=yellow align=center><%=showlevel%>&nbsp;
																				</td>
																				<%
																			       }
																			       %>
																				<td class="application-detail-data-body-list">
																					<%=content%></td>
																				<td class="application-detail-data-body-list">
																					<%=rtime1%></td>
																				<td class="application-detail-data-body-list">
																					<%=rptman%></td>
																				<td class="application-detail-data-body-list">
																					<%=status%></td>
																			</tr>
																			<%}
																			}//HONGLI ADD
 																			%>
																		</table>
																	</td>
																</tr>
															<tr>
							                					<td class="win-data-title" style="height:29px;"></td>
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
	            	<div align=center>
	            		<input type=button value="关闭窗口" onclick="window.close()">
	            	</div>  
				<br>
		</form>  
	</body>
</html>