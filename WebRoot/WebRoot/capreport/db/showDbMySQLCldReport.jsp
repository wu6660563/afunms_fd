<%@ page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@ page import="com.afunms.application.model.DBVo"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*"%>
<%@ page import="com.afunms.event.model.EventList"%>
<html>
<head>
<%	
	DBVo vo  = (DBVo)request.getAttribute("db");
	Vector val=(Vector)request.getAttribute("Val");
	List sessionlist=(List)request.getAttribute("sessionlist");
	String downnum= (String)request.getAttribute("downnum");//宕机数
    Integer count = (Integer)request.getAttribute("count");//获得告警次数 
    Hashtable tablesHash=(Hashtable)request.getAttribute("tablesHash");
    Vector tableinfo_v = (Vector)request.getAttribute("tableinfo_v");
	//double avgpingcon = (Double)request.getAttribute("avgpingcon");
	//int percent1 = Double.valueOf(avgpingcon).intValue();
	//int percent2 = 100-percent1;	
	
	String avgpingcon =(String) request.getAttribute("avgpingcon");
  	String rootPath = request.getContextPath();
  	String newip = (String)request.getAttribute("newIp");
  	String ip = (String)request.getAttribute("ipaddress");
  	String startdate = (String)request.getAttribute("startdate");
  	String todate = (String)request.getAttribute("todate");
  	Hashtable dbValue = new Hashtable();
  	Hashtable alldatabase = new Hashtable();
  	Vector names = new Vector();
    dbValue	= (Hashtable)request.getAttribute("dbValue");
    request.setAttribute("ipaddress",ip);
  	String pingmin = (String) request.getAttribute("pingmin");
  	String pingmax = (String) request.getAttribute("pingmax");
  	String pingnow = (String) request.getAttribute("pingnow");
    System.out.println("avgpingcon =  "+avgpingcon+"rootPath =  "+rootPath+"newip =  "+newip+"ip =  "+ip+"dbValue =  "+dbValue);
    
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
<!-- 综合报表 mysql-->
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
	mainForm.action = "<%=rootPath%>/dbreport.do?action=createMySQLCldReport&ipaddress=<%=ip%>&str=2";
	mainForm.submit();
}
function ping_word_report(){
	mainForm.action = "<%=rootPath%>/dbreport.do?action=createMySQLCldReport&ipaddress=<%=ip%>&str=0";
	mainForm.submit();
}
function ping_pdf_report(){
	mainForm.action = "<%=rootPath%>/dbreport.do?action=createMySQLCldReport&ipaddress=<%=ip%>&str=1";
	mainForm.submit();
}
function ping_ok(){
	var starttime = document.getElementById("mystartdate").value;
	var endtime = document.getElementById("mytodate").value;
	var status = document.getElementById("status").value;
	var level1 = document.getElementById("level1").value;
	//mainForm.action = "<%=rootPath%>/hostreport.do?action=showPingReport&ipaddress= &startdate="+starttime+"&todate="+endtime;
	mainForm.action = "<%=rootPath%>/mysql.do?action=mysqlManagerCldReportQuery&ipaddress= &startdate="+starttime+"&todate="+endtime+"&status="+status+"&level1="+level1;
	mainForm.submit();
}
function ping_cancel()
{
window.close();
}
//function query(){
  //	var startdate = mainForm.startdate.value;
  //	var todate = mainForm.todate.value;      
  //	var oids ="";
  //	var checkbox = document.getElementsByName("checkbox");
 	//	for (var i=0;i<checkbox.length;i++){
 			//if(checkbox[i].checked==true){
 			//	if (oids==""){
 			//		oids=checkbox[i].value;
 			//	}else{
 			//		oids=oids+","+checkbox[i].value;
 			//	}
 			//}
 	//	}
 	//if(oids==null||oids==""){
 	//	alert("请至少选择一个设备");
 	//	return;
 	//}
// 	window.open ("<%=rootPath%>/hostreport.do?action=downloadmultihostreport&startdate="+startdate+"&todate="+todate+"&ids="+oids, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes")    	      
//}

function openwin(str,operate,ip) 
{	
  var startdate = mainForm.startdate.value;
  var todate = mainForm.todate.value;
  window.open ("<%=rootPath%>/hostreport.do?action="+operate+"&startdate="+startdate+"&todate="+todate+"&ipaddress="+ip+"&str="+str+"&type=host", "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes") 
}
//function changeOrder(para){
//	mainForm.orderflag.value = para;
//	mainForm.action="<%=rootPath%>/netreport.do?action=netping"
 // 	mainForm.submit();
//}
  Ext.onReady(function()
{  

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

//function refer(action){
//		var mainForm = document.getElementById("mainForm");
//		mainForm.action = '<%=rootPath%>' + action;
//		mainForm.submit();
//}
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
	<input type="hidden"id="ipaddress" name="ipaddress" value=<%=request.getParameter("ipaddress") %>>
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
								                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" ></td>
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
															<tr>
																<td>连通率</td><td>当前连通率</td><td>最小连通率</td><td>平均连通率</td>
															</tr>
															<tr>
																<td>&nbsp;</td><td><%=pingnow %>%</td><td><%=pingmin %>%</td><td><%=avgpingcon %>%</td>
															</tr>
														</table>
														<tr>
																<td colspan="4" align="center">
																<img src="<%=rootPath%>/resource/image/jfreechart/<%=newip%>ConnectUtilization.png"/>
																</td>
															</tr>

												<tr>
							                		<td class="win-data-title" style="height: 29px;" ></td>
							       				</tr> 
							       				<tr>
							       					<td>
							       						<table class="application-detail-data-body" width="989" height="149">
							       						
							       							<tr align="left" bgcolor="#ECECEC">
																<td height="28">
																	<table cellspacing="1" cellpadding="0" width="100%">
																			<tr>
																				<td height="28" align="center" bgcolor="#ECECEC"
																					colspan=6>
																					&nbsp;&nbsp;
																					<b>数据库性能信息</b>
																				</td>
																			</tr>
																			<%   
																            	if(val != null && val.size()>0){
																            		for(int i=0;i<val.size();i++){
																            			Hashtable return_value = (Hashtable)val.get(i);
																            			if(return_value != null && return_value.size()>0){
																            				String name=return_value.get("variable_name").toString();
																            				String value=return_value.get("value").toString();
																            				if(name.equalsIgnoreCase("Max_used_connections"))
																					{
																					 name="服务器相应的最大连接数";
																					}
																					if(name.equalsIgnoreCase("Handler_read_first"))
																					{
																					 name="索引中第一条被读的次数";
																					}
																					if(name.equalsIgnoreCase("Handler_read_key"))
																					{
																					 name="根据键读一行的请求数";
																					}
																					if(name.equalsIgnoreCase("Handler_read_next"))
																					{
																					 name="按照键顺序读下一行的请求数";
																					}
																					if(name.equalsIgnoreCase("Handler_read_prev"))
																					{
																					 name="按照键顺序读前一行的请求数";
																					}
																					if(name.equalsIgnoreCase("Handler_read_rnd"))
																					{
																					 name="H根据固定位置读一行的请求数";
																					}
																					if(name.equalsIgnoreCase("Handler_read_rnd_next"))
																					{
																					 name="在数据文件中读下一行的请求数";
																					}
																					if(name.equalsIgnoreCase("Open_tables"))
																					{
																					 name="当前打开的表的数量";
																					}
																					if(name.equalsIgnoreCase("Opened_tables"))
																					{
																					 name="已经打开的表的数量";
																					}
																					if(name.equalsIgnoreCase("Threads_cached"))
																					{
																					 name="线程缓存内的线程的数量";
																					}
																					if(name.equalsIgnoreCase("Threads_connected"))
																					{
																					 name="当前打开的连接的数量";
																					}
																					if(name.equalsIgnoreCase("Threads_created"))
																					{
																					 name="创建用来处理连接的线程数";
																					}
																					if(name.equalsIgnoreCase("Threads_running"))
																					{
																					 name="激活的非睡眠状态的线程数";
																					}
																					if(name.equalsIgnoreCase("Table_locks_immediate"))
																					{
																					 name="立即获得的表的锁的次数";
																					}
																					if(name.equalsIgnoreCase("Table_locks_waited"))
																					{
																					 name="不能立即获得的表的锁的次数";
																					}
																					if(name.equalsIgnoreCase("Key_read_requests"))
																					{
																					 name="从缓存读键的数据块的请求数";
																					}
																					if(name.equalsIgnoreCase("Key_reads"))
																					{
																					 name="从硬盘读取键的数据块的次数";
																					}
																					if(name.equalsIgnoreCase("log_slow_queries"))
																					{
																					 name="是否记录慢查询";
																					}
																					if(name.equalsIgnoreCase("slow_launch_time"))
																					{
																					 name="创建线程的时间超过该秒数，服务器增加Slow_launch_threads状态变量";
																					}
																             %>
																			<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																						height=28>
																				<td width=10%
																					class="application-detail-data-body-list">
																					&nbsp;
																				</td>
																				<td align=left width=50%
																					class="application-detail-data-body-list">
																					&nbsp;<%=name%></td>
																				<td align=left
																					class="application-detail-data-body-list">
																					&nbsp;<%=value%></td>
																				<td width=10%
																					class="application-detail-data-body-list">
																					&nbsp;
																				</td>
																			</tr>
																				<%		}
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
																	<td align=center>
																		<table border="0" id="table1" cellpadding="0"
																			cellspacing="0" width="80%">
																			<tr border="1">
																				<td height="28" align="center" bgcolor="#ECECEC"
																					colspan=6 border="1">
																					&nbsp;&nbsp;
																					<b>连接信息</b>
																				</td>
																			</tr>
																			<TBODY>
																				<tr style="background-color: #ECECEC;" height=28>
																					<td align=center>
																						&nbsp;
																					</td>
																					<td align=center>
																						数据库
																					</td>
																					<td align=center>
																						用户名
																					</td>
																					<td align=center>
																						主机
																					</td>
																					<td align=center>
																						命令
																					</td>
																					<td align=center>
																						连接时间
																					</td>
																				</tr>
																				<% 
																                  if(sessionlist != null && sessionlist.size()>0){
																                  	for(int i=0;i<sessionlist.size();i++){
																                  		List ipsessionlist = (List)sessionlist.get(i);
																                  		if(ipsessionlist != null && ipsessionlist.size()>0){
																                  			for(int k=0;k<ipsessionlist.size();k++){
																                  				String[] sessions = (String[])ipsessionlist.get(k);
																                  				if(sessions != null && sessions.length ==5){
																                  %>
																				<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																					height="28">
																					<td align=center
																						class="application-detail-data-body-list"><%=k+1%></td>
																					<td align=center
																						class="application-detail-data-body-list">
																						&nbsp;<%=sessions[4]%></td>
																					<td align=center
																						class="application-detail-data-body-list">
																						&nbsp;<%=sessions[0]%></td>
																					<td align=center
																						class="application-detail-data-body-list">
																						&nbsp;<%=sessions[1]%></td>
																					<td align=center
																						class="application-detail-data-body-list">
																						&nbsp;<%=sessions[2]%></td>
																					<td align=center
																						class="application-detail-data-body-list">
																						&nbsp;<%=sessions[3]%></td>

																				</tr>
																				<%
																	          					}
																	          				}
																	          			}
																	          		}
																	          	}
																	          	%>
																			</TBODY>
																		</table>
																	</td>
															</tr>
															<tr>
							                					<td class="win-data-title" style="height: 29px;" ></td>
							       							</tr>
															<tr>
																<td align=center>
																	<table border="0" id="table1" cellpadding="0"
																		cellspacing="0" width="80%">
																		<tr border="1">
																				<td height="28" align="center" bgcolor="#ECECEC"
																					colspan=6 border="1">
																					&nbsp;&nbsp;
																					<b>表信息</b>
																				</td>
																		</tr>
																		<%
														                  String[] dbs = vo.getDbName().split(","); 
														                  if(dbs != null && dbs.length>0){
														                  	for(int i=0;i<dbs.length;i++){
														                  		String dbStr = dbs[i];
														                  		//System.out.println(dbStr+"====="+tablesHash.size());
														                  		if(tablesHash.containsKey(dbStr)){
														                  			List tableslist = (List)tablesHash.get(dbStr);
														                  %>
																		<TBODY>
																			<tr style="background-color: #ECECEC;" height=28>
																				<td align=center>
																					&nbsp;
																				</td>
																				<td align=center>
																					表名
																				</td>
																				<td align=center>
																					表行数
																				</td>
																				<td align=center>
																					表大小
																				</td>
																				<td align=center>
																					创建时间
																				</td>
																			</tr>
																			<%
              			
																              		if(tableslist != null && tableslist.size()>0){
																              			for(int k=0;k<tableslist.size();k++){
																              				String[] tables = (String[])tableslist.get(k);
																              				if(tables != null && tables.length ==4){
																              %>
																			<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																				height="28">
																				<td class="application-detail-data-body-list"
																					align="center"><%=k+1%></td>
																				<td class="application-detail-data-body-list"
																					align=center>
																					&nbsp;<%=tables[0]%></td>
																				<td class="application-detail-data-body-list"
																					align=center>
																					&nbsp;<%=tables[1]%></td>
																				<td class="application-detail-data-body-list"
																					align=center>
																					&nbsp;<%=tables[2]%></td>
																				<td class="application-detail-data-body-list"
																					align=center>
																					&nbsp;<%=tables[3]%></td>

																			</tr>
																			<%
																	      					}
																	      				}
																	      			}
																	
																	       		}
																	       	%>
																		</TBODY>
																		<tr>
																			<td heigh=10>
																				&nbsp;
																			</td>
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
																		<table cellspacing="1" cellpadding="0" width="100%">
																			<tr>
																				<td height="28" align="center" bgcolor="#ECECEC"
																					colspan=6>
																					<b> 数据库状态信息</b>
																				</td>
																			</tr>

																			<%
			if(tableinfo_v != null)
			{
				for(int i=0;i<tableinfo_v.size();i++){
					Hashtable ht = (Hashtable)tableinfo_v.get(i);
					String size = ht.get("variable_name").toString();
					String free = ht.get("value").toString();
					if(size.equalsIgnoreCase("auto_increment_increment"))
					{
					 size="控制列中的值的增量值";
					}
					if(size.equalsIgnoreCase("auto_increment_offset"))
					{
					 size="确定AUTO_INCREMENT列值的起点";
					}
					if(size.equalsIgnoreCase("automatic_sp_privileges"))
					{
					 size="automatic_sp_privileges";
					}
					if(size.equalsIgnoreCase("back_log"))
					{
					 size="连接请求的数量";
					}
					if(size.equalsIgnoreCase("basedir"))
					{
					 size="MySQL安装基准目录";
					}
					if(size.equalsIgnoreCase("binlog_cache_size"))
					{
					 size="容纳二进制日志SQL语句的缓存大小";
					}
					if(size.equalsIgnoreCase("bulk_insert_buffer_size"))
					{
					 size="每线程的字节数限制缓存树的大小";
					}
					if(size.equalsIgnoreCase("character_set_client"))
					{
					 size="来自客户端的语句的字符集";
					}
					if(size.equalsIgnoreCase("character_set_connection"))
					{
					 size="没有字符集导入符的字符串转换";
					}
					if(size.equalsIgnoreCase("character_set_database"))
					{
					 size="默认数据库使用的字符集";
					}
					if(size.equalsIgnoreCase("character_set_filesystem"))
					{
					 size="character_set_filesystem";
					}
					if(size.equalsIgnoreCase("character_set_results"))
					{
					 size="用于向客户端返回查询结果的字符集";
					}
					if(size.equalsIgnoreCase("character_set_server"))
					{
					 size="服务器的默认字符集";
					}
					if(size.equalsIgnoreCase("character_set_system"))
					{
					 size="服务器用来保存识别符的字符集";
					}
					if(size.equalsIgnoreCase("character_sets_dir"))
					{
					 size="字符集安装目录";
					}
					if(size.equalsIgnoreCase("collation_connection"))
					{
					 size="连接字符集的校对规则";
					}
					if(size.equalsIgnoreCase("collation_database"))
					{
					 size="默认数据库使用的校对规则";
					}
					if(size.equalsIgnoreCase("collation_server"))
					{
					 size="服务器的默认校对规则";
					}
					if(size.equalsIgnoreCase("completion_type"))
					{
					 size="事务结束类型";
					}
					if(size.equalsIgnoreCase("concurrent_insert"))
					{
					 size="存储值情况";
					}
					if(size.equalsIgnoreCase("connect_timeout"))
					{
					 size="服务器用Bad handshake响应前等待连接包的秒数";
					}
					if(size.equalsIgnoreCase("datadir"))
					{
					 size="MySQL数据目录";
					}
					if(size.equalsIgnoreCase("date_format"))
					{
					 size="date_format(为被使用)";
					}
					if(size.equalsIgnoreCase("datetime_format"))
					{
					 size="datetime_format(为被使用)";
					}
					if(size.equalsIgnoreCase("default_week_format"))
					{
					 size="WEEK() 函数使用的默认模式";
					}
					if(size.equalsIgnoreCase("delay_key_write"))
					{
					 size="使用的DELAY_KEY_WRITE表选项的处理";
					}
					if(size.equalsIgnoreCase("delayed_insert_limit"))
					{
					 size="INSERT DELAYED处理器线程检查是否有挂起的SELECT语句";
					}
					if(size.equalsIgnoreCase("delayed_insert_timeout"))
					{
					 size="INSERT DELAYED处理器线程终止前应等待INSERT语句的时间";
					}
					if(size.equalsIgnoreCase("delayed_queue_size"))
					{
					 size="处理INSERT DELAYED语句时队列中行的数量限制";
					}
					if(size.equalsIgnoreCase("div_precision_increment"))
					{
					 size="用/操作符执行除操作的结果可增加的精确度的位数";
					}
					if(size.equalsIgnoreCase("engine_condition_pushdown"))
					{
					 size="适用于NDB的检测";
					}
					if(size.equalsIgnoreCase("expire_logs_days"))
					{
					 size="二进制日志自动删除的天数";
					}
					if(size.equalsIgnoreCase("flush"))
					{
					 size="flush选项启动mysqld值";
					}
					if(size.equalsIgnoreCase("flush_time"))
					{
					 size="查看释放资源情况";
					}
					if(size.equalsIgnoreCase("ft_boolean_syntax"))
					{
					 size="使用IN BOOLEAN MODE执行的布尔全文搜索支持的操作符系列";
					}
					if(size.equalsIgnoreCase("ft_max_word_len"))
					{
					 size="FULLTEXT索引中所包含的字的最大长度";
					}
					if(size.equalsIgnoreCase("ft_min_word_len"))
					{
					 size="FULLTEXT索引中所包含的字的最小长度";
					}
					if(size.equalsIgnoreCase("ft_query_expansion_limit"))
					{
					 size="使用WITH QUERY EXPANSION进行全文搜索的最大匹配数";
					}
					if(size.equalsIgnoreCase("ft_stopword_file"))
					{
					 size="用于读取全文搜索的停止字清单的文件";
					}
					if(size.equalsIgnoreCase("group_concat_max_len"))
					{
					 size="允许的GROUP_CONCAT()函数结果的最大长度";
					}
					if(size.equalsIgnoreCase("have_archive"))
					{
					 size="mysqld支持ARCHIVE表支持表情况";
					}
					if(size.equalsIgnoreCase("have_bdb"))
					{
					 size="mysqld支持BDB表情况";
					}
					if(size.equalsIgnoreCase("have_blackhole_engine"))
					{
					 size="mysqld支持BLACKHOLE表情况";
					}
					if(size.equalsIgnoreCase("have_compress"))
					{
					 size="是否zlib压缩库适合该服务器";
					}
					if(size.equalsIgnoreCase("have_crypt"))
					{
					 size="是否crypt()系统调用适合该服务器";
					}
					if(size.equalsIgnoreCase("have_csv"))
					{
					 size="mysqld支持ARCHIVE表情况";
					}
					if(size.equalsIgnoreCase("have_example_engine"))
					{
					 size="mysqld支持EXAMPLE表情况";
					}
					if(size.equalsIgnoreCase("have_federated_engine"))
					{
					 size="mysqld支持FEDERATED表情况";
					}
					if(size.equalsIgnoreCase("have_geometry"))
					{
					 size="是否服务器支持空间数据类型";
					}
					if(size.equalsIgnoreCase("have_innodb"))
					{
					 size="mysqld支持InnoDB表情况";
					}
					if(size.equalsIgnoreCase("have_isam"))
					{
					 size="向后兼容";
					}
					if(size.equalsIgnoreCase("have_ndbcluster"))
					{
					 size="mysqld支持NDB CLUSTER表情况";
					}
					if(size.equalsIgnoreCase("have_openssl"))
					{
					 size="mysqld支持客户端/服务器协议的SSL(加密)情况";
					}
					if(size.equalsIgnoreCase("have_query_cache"))
					{
					 size="mysqld支持查询缓存情况";
					}
					if(size.equalsIgnoreCase("have_raid"))
					{
					 size="mysqld支持RAID选项情况";
					}
					if(size.equalsIgnoreCase("have_rtree_keys"))
					{
					 size="RTREE索引是否可用";
					}
					if(size.equalsIgnoreCase("have_symlink"))
					{
					 size="是否启用符号链接支持";
					}
					if(size.equalsIgnoreCase("init_connect"))
					{
					 size="字符串处理";
					}
					if(size.equalsIgnoreCase("init_file"))
					{
					 size="启动服务器时用--init-file选项指定的文件名";
					}
					if(size.equalsIgnoreCase("init_slave"))
					{
					 size="SQL线程启动时从服务器应执行该字符串";
					}
					if(size.equalsIgnoreCase("innodb_additional_mem_pool_size"))
					{
					 size="InnoDB用来存储数据内存大小情况";
					}
					if(size.equalsIgnoreCase("innodb_autoextend_increment"))
					{
					 size="表空间被填满之时扩展表空间的尺寸";
					}
					if(size.equalsIgnoreCase("innodb_buffer_pool_awe_mem_mb"))
					{
					 size="缓冲池被放在32位Windows的AWE内存里缓存池大小";
					}
					if(size.equalsIgnoreCase("innodb_buffer_pool_size"))
					{
					 size="InnoDB用来缓存它的数据和索引的内存缓冲区的大小";
					}
					if(size.equalsIgnoreCase("innodb_checksums"))
					{
					 size="InnoDB在所有对磁盘的页面读取上的状态";
					}
					if(size.equalsIgnoreCase("innodb_commit_concurrency"))
					{
					 size="innodb_commit_concurrency";
					}
					if(size.equalsIgnoreCase("innodb_concurrency_tickets"))
					{
					 size="innodb_concurrency_tickets";
					}
					if(size.equalsIgnoreCase("innodb_data_file_path"))
					{
					 size="单独数据文件和它们尺寸的路径";
					}
					if(size.equalsIgnoreCase("innodb_data_home_dir"))
					{
					 size="目录路径对所有InnoDB数据文件的共同部分";
					}
					if(size.equalsIgnoreCase("innodb_doublewrite"))
					{
					 size="InnoDB存储所有数据情况";
					}
					if(size.equalsIgnoreCase("innodb_fast_shutdown"))
					{
					 size="InnoDB在关闭情况的值选择";
					}
					if(size.equalsIgnoreCase("innodb_file_io_threads"))
					{
					 size="InnoDB中文件I/O线程的数";
					}
					if(size.equalsIgnoreCase("innodb_file_per_table"))
					{
					 size="确定是否InnoDB用自己的.ibd文件为存储数据和索引创建每一个新表";
					}
					if(size.equalsIgnoreCase("innodb_flush_log_at_trx_commit"))
					{
					 size="InnoDB对日志操作情况";
					}
					if(size.equalsIgnoreCase("innodb_flush_method"))
					{
					 size="InnoDB使用fsync()来刷新数据和日志文件";
					}
					if(size.equalsIgnoreCase("innodb_force_recovery"))
					{
					 size="损坏的数据库转储表的方案";
					}
					if(size.equalsIgnoreCase("innodb_lock_wait_timeout"))
					{
					 size="InnoDB事务在被回滚之前可以等待一个锁定的超时秒数";
					}
					if(size.equalsIgnoreCase("innodb_locks_unsafe_for_binlog"))
					{
					 size="InnoDB搜索和索引扫描中关闭下一键锁定";
					}
					if(size.equalsIgnoreCase("innodb_log_arch_dir"))
					{
					 size="使用日志档案 被完整写入的日志文件所在的目录的归档值";
					}
					if(size.equalsIgnoreCase("innodb_log_archive"))
					{
					 size="日志处理情况";
					}
					if(size.equalsIgnoreCase("innodb_log_buffer_size"))
					{
					 size="InnoDB用来往磁盘上的日志文件写操作的缓冲区的大小";
					}
					if(size.equalsIgnoreCase("innodb_log_file_size"))
					{
					 size="日志组里每个日志文件的大小";
					}
					if(size.equalsIgnoreCase("innodb_log_files_in_group"))
					{
					 size="日志组里日志文件的数目";
					}
					if(size.equalsIgnoreCase("innodb_log_group_home_dir"))
					{
					 size="InnoDB日志文件的目录路径";
					}
					if(size.equalsIgnoreCase("innodb_max_dirty_pages_pct"))
					{
					 size="InnoDB中处理脏页的情况";
					}
					if(size.equalsIgnoreCase("innodb_max_purge_lag"))
					{
					 size="净化操作被滞后之时，如何延迟INSERT,UPDATE和DELETE操作";
					}
					if(size.equalsIgnoreCase("innodb_mirrored_log_groups"))
					{
					 size="为数据库保持的日志组内同样拷贝的数量";
					}
					if(size.equalsIgnoreCase("innodb_open_files"))
					{
					 size="定InnoDB一次可以保持打开的.ibd文件的最大数";
					}
					if(size.equalsIgnoreCase("innodb_support_xa"))
					{
					 size="InnoDB支持在XA事务中的双向提交情况";
					}
					if(size.equalsIgnoreCase("innodb_sync_spin_loops"))
					{
					 size="innodb_sync_spin_loops";
					}
					if(size.equalsIgnoreCase("innodb_table_locks"))
					{
					 size="InnoDB对表的锁定情况";
					}
					if(size.equalsIgnoreCase("innodb_thread_concurrency"))
					{
					 size="InnoDB试着在InnoDB内保持操作系统线程的数量少于或等于这个参数给出的限制范围";
					}
					if(size.equalsIgnoreCase("innodb_thread_sleep_delay"))
					{
					 size="让InnoDB为周期的SHOW INNODB STATUS输出创建一个文件<datadir>/innodb_status";
					}
					if(size.equalsIgnoreCase("interactive_timeout"))
					{
					 size="服务器关闭交互式连接前等待活动的秒数";
					}
					if(size.equalsIgnoreCase("join_buffer_size"))
					{
					 size="用于完全联接的缓冲区的大小";
					}
					if(size.equalsIgnoreCase("key_buffer_size"))
					{
					 size="索引块缓冲区的大小";
					}
					if(size.equalsIgnoreCase("key_cache_age_threshold"))
					{
					 size="控制将缓冲区从键值缓存热子链(sub-chain)降级到温子链(sub-chain)的值";
					}
					if(size.equalsIgnoreCase("key_cache_block_size"))
					{
					 size="键值缓存内块的字节大小";
					}
					if(size.equalsIgnoreCase("key_cache_division_limit"))
					{
					 size="键值缓存缓冲区链热子链和温子链的划分点";
					}
					if(size.equalsIgnoreCase("language"))
					{
					 size="错误消息所用语言";
					}
					if(size.equalsIgnoreCase("large_files_support"))
					{
					 size="mysqld编译时是否使用了大文件支持选项";
					}
					if(size.equalsIgnoreCase("large_page_size"))
					{
					 size="large_page_size";
					}
					if(size.equalsIgnoreCase("large_pages"))
					{
					 size="是否启用了大页面支持";
					}
					if(size.equalsIgnoreCase("license"))
					{
					 size="服务器的许可类型";
					}
					if(size.equalsIgnoreCase("local_infile"))
					{
					 size="是否LOCAL支持LOAD DATA INFILE语句";
					}
					if(size.equalsIgnoreCase("log"))
					{
					 size="是否启用将所有查询记录到常规查询日志中";
					}
					if(size.equalsIgnoreCase("log_bin"))
					{
					 size="是否启用二进制日志";
					}
					if(size.equalsIgnoreCase("log_bin_trust_function_creators"))
					{
					 size="是否可以信任保存的程序的作者不会创建向二进制日志写入不安全事件的程序";
					}
					if(size.equalsIgnoreCase("log_error"))
					{
					 size="错误日志的位置";
					}
					if(size.equalsIgnoreCase("log_slave_updates"))
					{
					 size="是否从服务器从主服务器收到的更新应记入从服务器自己的二进制日志";
					}
					if(size.equalsIgnoreCase("log_slow_queries"))
					{
					 size="是否记录慢查询";
					}
					if(size.equalsIgnoreCase("log_warnings"))
					{
					 size="是否产生其它警告消息";
					}
					if(size.equalsIgnoreCase("long_query_time"))
					{
					 size="查询时间超过该值，则增加Slow_queries状态变量";
					}
					if(size.equalsIgnoreCase("low_priority_updates"))
					{
					 size="表示sql语句等待语句将等待直到受影响的表没有挂起的SELECT或LOCK TABLE READ";
					}
					if(size.equalsIgnoreCase("lower_case_file_system"))
					{
					 size="说明是否数据目录所在的文件系统对文件名的大小写敏感";
					}
					if(size.equalsIgnoreCase("lower_case_table_names"))
					{
					 size="为1表示表名用小写保存到硬盘上，并且表名比较时不对大小写敏感";
					}
					if(size.equalsIgnoreCase("max_allowed_packet"))
					{
					 size="包或任何生成的/中间字符串的最大大小";
					}
					if(size.equalsIgnoreCase("max_binlog_cache_size"))
					{
					 size="多语句事务需要更大的内存时出现的情况";
					}
					if(size.equalsIgnoreCase("max_binlog_size"))
					{
					 size="多语句事务需要更大的内存时出现的情况";
					}
					if(size.equalsIgnoreCase("max_connect_errors"))
					{
					 size="断的与主机的连接的最大限制数";
					}
					if(size.equalsIgnoreCase("max_connections"))
					{
					 size="允许的并行客户端连接数目";
					}
					if(size.equalsIgnoreCase("max_delayed_threads"))
					{
					 size="启动线程来处理INSERT DELAYED语句的限制数";
					}
					if(size.equalsIgnoreCase("max_error_count"))
					{
					 size="存由SHOW ERRORS或SHOW WARNINGS显示的错误、警告和注解的最大数目";
					}
					if(size.equalsIgnoreCase("max_heap_table_size"))
					{
					 size="设置MEMORY (HEAP)表可以增长到的最大空间大小";
					}
					if(size.equalsIgnoreCase("max_insert_delayed_threads"))
					{
					 size="启动线程来处理INSERT DELAYED语句的限制数(同max_delayed_threads)";
					}
					if(size.equalsIgnoreCase("max_join_size"))
					{
					 size="不允许可能需要检查多于max_join_size行的情况";
					}
					if(size.equalsIgnoreCase("max_length_for_sort_data"))
					{
					 size="确定使用的filesort算法的索引值大小的限值";
					}
					if(size.equalsIgnoreCase("max_prepared_stmt_count"))
					{
					 size="max_prepared_stmt_count";
					}
					if(size.equalsIgnoreCase("max_relay_log_size"))
					{
					 size="如果复制从服务器写入中继日志时超出给定值，则滚动中继日";
					}
					if(size.equalsIgnoreCase("max_seeks_for_key"))
					{
					 size="限制根据键值寻找行时的最大搜索数";
					}
					if(size.equalsIgnoreCase("max_sort_length"))
					{
					 size="排序BLOB或TEXT值时使用的字节数";
					}
					if(size.equalsIgnoreCase("max_sp_recursion_depth"))
					{
					 size="max_sp_recursion_depth";
					}
					if(size.equalsIgnoreCase("max_tmp_tables"))
					{
					 size="客户端可以同时打开的临时表的最大数";
					}
					if(size.equalsIgnoreCase("max_user_connections"))
					{
					 size="给定的MySQL账户允许的最大同时连接数";
					}
					if(size.equalsIgnoreCase("max_write_lock_count"))
					{
					 size="超过写锁定限制后，允许部分读锁定";
					}
					if(size.equalsIgnoreCase("multi_range_count"))
					{
					 size="multi_range_count";
					}
					if(size.equalsIgnoreCase("myisam_data_pointer_size"))
					{
					 size="默认指针大小的值";
					}
					if(size.equalsIgnoreCase("myisam_max_sort_file_size"))
					{
					 size="重建MyISAM索引时，允许MySQL使用的临时文件的最大空间大小";
					}
					if(size.equalsIgnoreCase("myisam_recover_options"))
					{
					 size="myisam-recover选项的值";
					}
					if(size.equalsIgnoreCase("myisam_repair_threads"))
					{
					 size="如果该值大于1，在Repair by sorting过程中并行创建MyISAM表索引";
					}
					if(size.equalsIgnoreCase("myisam_sort_buffer_size"))
					{
					 size="在REPAIR TABLE或用CREATE INDEX创建索引或ALTER TABLE过程中排序MyISAM索引分配的缓冲区";
					}
					if(size.equalsIgnoreCase("myisam_stats_method"))
					{
					 size="MyISAM表搜集关于索引值分发的统计信息时服务器如何处理NULL值";
					}
					if(size.equalsIgnoreCase("named_pipe"))
					{
					 size="明服务器是否支持命名管道连接";
					}
					if(size.equalsIgnoreCase("net_buffer_length"))
					{
					 size="在查询之间将通信缓冲区重设为该值";
					}
					if(size.equalsIgnoreCase("net_read_timeout"))
					{
					 size="中断读前等待连接的其它数据的秒数";
					}
					if(size.equalsIgnoreCase("net_retry_count"))
					{
					 size="表示某个通信端口的读操作中断了，在放弃前重试多次";
					}
					if(size.equalsIgnoreCase("net_write_timeout"))
					{
					 size="中断写之前等待块写入连接的秒数";
					}
					if(size.equalsIgnoreCase("new"))
					{
					 size="表示在MySQL 4.0中使用该变量来打开4.1中的一些行为，并用于向后兼容性";
					}
					if(size.equalsIgnoreCase("old_passwords"))
					{
					 size="是否服务器应为MySQL用户账户使用pre-4.1-style密码性";
					}
					if(size.equalsIgnoreCase("open_files_limit"))
					{
					 size="操作系统允许mysqld打开的文件的数量";
					}
					if(size.equalsIgnoreCase("optimizer_prune_level"))
					{
					 size="在查询优化从优化器搜索空间裁减低希望局部计划中使用的控制方法 0表示禁用方法";
					}
					if(size.equalsIgnoreCase("optimizer_search_depth"))
					{
					 size="查询优化器进行的搜索的最大深度";
					}
					if(size.equalsIgnoreCase("pid_file"))
					{
					 size="进程ID (PID)文件的路径名";
					}
					if(size.equalsIgnoreCase("prepared_stmt_count"))
					{
					 size="prepared_stmt_count";
					}
					if(size.equalsIgnoreCase("port"))
					{
					 size="服务器帧听TCP/IP连接所用端口";
					}
					if(size.equalsIgnoreCase("preload_buffer_size"))
					{
					 size="重载索引时分配的缓冲区大小";
					}
					if(size.equalsIgnoreCase("protocol_version"))
					{
					 size="MySQL服务器使用的客户端/服务器协议的版本";
					}
					if(size.equalsIgnoreCase("query_alloc_block_size"))
					{
					 size="为查询分析和执行过程中创建的对象分配的内存块大小";
					}
					if(size.equalsIgnoreCase("query_cache_limit"))
					{
					 size="不要缓存大于该值的结果";
					}
					if(size.equalsIgnoreCase("query_cache_min_res_unit"))
					{
					 size="查询缓存分配的最小块的大小(字节)";
					}
					if(size.equalsIgnoreCase("query_cache_size"))
					{
					 size="为缓存查询结果分配的内存的数量";
					}
					if(size.equalsIgnoreCase("query_cache_type"))
					{
					 size="设置查询缓存类型";
					}
					if(size.equalsIgnoreCase("query_cache_wlock_invalidate"))
					{
					 size="对表进行WRITE锁定的设置值";
					}
					if(size.equalsIgnoreCase("query_prealloc_size"))
					{
					 size="用于查询分析和执行的固定缓冲区的大小";
					}
					if(size.equalsIgnoreCase("range_alloc_block_size"))
					{
					 size="范围优化时分配的块的大小";
					}
					if(size.equalsIgnoreCase("read_buffer_size"))
					{
					 size="每个线程连续扫描时为扫描的每个表分配的缓冲区的大小(字节)";
					}
					if(size.equalsIgnoreCase("read_only"))
					{
					 size="变量对复制从服务器设置为ON时，服务器是否允许更新";
					}
					if(size.equalsIgnoreCase("read_only"))
					{
					 size="变量对复制从服务器设置为ON时，从服务器不允许更新";
					}
					if(size.equalsIgnoreCase("relay_log_purge"))
					{
					 size="当不再需要中继日志时禁用或启用自动清空中继日志";
					}
					if(size.equalsIgnoreCase("read_rnd_buffer_size"))
					{
					 size="当排序后按排序后的顺序读取行时，则通过该缓冲区读取行，避免搜索硬盘";
					}
					if(size.equalsIgnoreCase("secure_auth"))
					{
					 size="如果用--secure-auth选项启动了MySQL服务器，是否将阻塞有旧格式(4.1之前)密码的所有账户所发起的连接";
					}
					if(size.equalsIgnoreCase("shared_memory"))
					{
					 size="(只用于Windows)服务器是否允许共享内存连接";
					}
					if(size.equalsIgnoreCase("shared_memory_base_name"))
					{
					 size="(只用于Windows)说明服务器是否允许共享内存连接，并为共享内存设置识别符";
					}
					if(size.equalsIgnoreCase("server_id"))
					{
					 size="用于主复制服务器和从复制服务器";
					}
					if(size.equalsIgnoreCase("skip_external_locking"))
					{
					 size="mysqld是否使用外部锁定";
					}
					if(size.equalsIgnoreCase("skip_networking"))
					{
					 size="如果服务器只允许本地(非TCP/IP)连接";
					}
					if(size.equalsIgnoreCase("skip_show_database"))
					{
					 size="防止不具有SHOW DATABASES权限的人们使用SHOW DATABASES语句";
					}
					if(size.equalsIgnoreCase("slave_compressed_protocol"))
					{
					 size="如果主、从服务器均支持，确定是否使用从/主压缩协议";
					}
					if(size.equalsIgnoreCase("slave_load_tmpdir"))
					{
					 size="从服务器为复制LOAD DATA INFILE语句创建临时文件的目录名";
					}
					if(size.equalsIgnoreCase("slave_net_timeout"))
					{
					 size="放弃读操作前等待主/从连接的更多数据的等待秒数";
					}
					if(size.equalsIgnoreCase("slave_skip_errors"))
					{
					 size="从服务器应跳过(忽视)的复制错误";
					}
					if(size.equalsIgnoreCase("slave_transaction_retries"))
					{
					 size="复制从服务器SQL线程未能执行事务，在提示错误并停止前它自动重复slave_transaction_retries次";
					}
					if(size.equalsIgnoreCase("slow_launch_time"))
					{
					 size="如果创建线程的时间超过该秒数，服务器增加Slow_launch_threads状态变量";
					}
					if(size.equalsIgnoreCase("sort_buffer_size"))
					{
					 size="每个排序线程分配的缓冲区的大小";
					}
					if(size.equalsIgnoreCase("sql_mode"))
					{
					 size="当前的服务器SQL模式，可以动态设置";
					}
					if(size.equalsIgnoreCase("storage_engine"))
					{
					 size="该变量是table_typeis的同义词。在MySQL 5.1中,首选storage_engine";
					}
					if(size.equalsIgnoreCase("sync_binlog"))
					{
					 size="如果为正，当每个sync_binlog'th写入该二进制日志后，MySQL服务器将它的二进制日志同步到硬盘上";
					}
					if(size.equalsIgnoreCase("sync_frm"))
					{
					 size="如果该变量设为1,当创建非临时表时它的.frm文件是否被同步到硬盘上";
					}
					if(size.equalsIgnoreCase("system_time_zone"))
					{
					 size="服务器系统时区";
					}
					if(size.equalsIgnoreCase("table_cache"))
					{
					 size="所有线程打开的表的数目";
					}
					if(size.equalsIgnoreCase("table_type"))
					{
					 size="默认表类型(存储引擎)";
					}
					if(size.equalsIgnoreCase("thread_cache_size"))
					{
					 size="服务器应缓存多少线程以便重新使用";
					}
					if(size.equalsIgnoreCase("thread_stack"))
					{
					 size="每个线程的堆栈大小";
					}
					if(size.equalsIgnoreCase("time_format"))
					{
					 size="该变量为使用";
					}
					if(size.equalsIgnoreCase("time_zone"))
					{
					 size="当前的时区";
					}
					if(size.equalsIgnoreCase("tmp_table_size"))
					{
					 size="如果内存内的临时表超过该值，MySQL自动将它转换为硬盘上的MyISAM表";
					}
					if(size.equalsIgnoreCase("tmpdir"))
					{
					 size="保存临时文件和临时表的目录";
					}
					if(size.equalsIgnoreCase("transaction_alloc_block_size"))
					{
					 size="为保存将保存到二进制日志中的事务的查询而分配的内存块的大小(字节)";
					}
					if(size.equalsIgnoreCase("transaction_prealloc_size"))
					{
					 size="transaction_alloc_blocks分配的固定缓冲区的大小（字节），在两次查询之间不会释放";
					}
					if(size.equalsIgnoreCase("tx_isolation"))
					{
					 size="默认事务隔离级别";
					}
					if(size.equalsIgnoreCase("updatable_views_with_limit"))
					{
					 size="该变量控制如果更新包含LIMIT子句，是否可以在当前表中使用不包含主关键字的视图进行更新";
					}
					if(size.equalsIgnoreCase("version"))
					{
					 size="服务器版本号";
					}
					if(size.equalsIgnoreCase("version_bdb"))
					{
					 size="BDB存储引擎版本";
					}
					if(size.equalsIgnoreCase("version_comment"))
					{
					 size="configure脚本有一个--with-comment选项，当构建MySQL时可以进行注释";
					}
					if(size.equalsIgnoreCase("version_compile_machine"))
					{
					 size="MySQL构建的机器或架构的类型";
					}
					if(size.equalsIgnoreCase("version_compile_os"))
					{
					 size="MySQL构建的操作系统的类型";
					}
					if(size.equalsIgnoreCase("wait_timeout"))
					{
					 size="服务器关闭非交互连接之前等待活动的秒数";
					}
			%>
																			<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																				height="28">
																				<td class="application-detail-data-body-list"><%=i+1%></td>
																				<td align=left
																					class="application-detail-data-body-list">
																					&nbsp;<%=size%></td>
																				<td class="application-detail-data-body-list"
																					align=left width=20%>
																					&nbsp;<%=free%></td>
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