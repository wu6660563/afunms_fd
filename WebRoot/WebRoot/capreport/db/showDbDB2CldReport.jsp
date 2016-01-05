<%@ page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@ page import="com.afunms.application.model.DBVo"%>
<%@ page import=" java.util.*"%>
<%@ page import="com.afunms.application.model.DBVo"%> 
<%@ page import="com.afunms.application.model.DBTypeVo"%>
<%@ page import="com.afunms.event.model.EventList"%>

<html>
<head>
<%	

	Hashtable conn = (Hashtable)request.getAttribute("conn");//连接信息
	if(conn == null)conn = new Hashtable();	
	
	Hashtable poolInfo = (Hashtable)request.getAttribute("poolInfo");//缓存信息
	if(poolInfo == null)poolInfo = new Hashtable();	
	
	Hashtable log = (Hashtable)request.getAttribute("log");//日志
	if(log == null)log = new Hashtable();	
	
	Hashtable spaceInfo = (Hashtable)request.getAttribute("spaceInfo");//表空间
	if(spaceInfo == null)spaceInfo = new Hashtable();	
	
	
	String[] sysDbStatus={"活动","正在静默","静默模式","前滚"};
	
    Hashtable dbValue	= (Hashtable)request.getAttribute("dbValue");
    Hashtable sysValue = (Hashtable)request.getAttribute("sysValue");
    Hashtable mems = (Hashtable)request.getAttribute("mems");
    DBVo vo = (DBVo)request.getAttribute("vo");
    DBTypeVo typevo = (DBTypeVo)request.getAttribute("typevo");
    String pingmin = (String)request.getAttribute("pingmin");
    String pingmax = (String)request.getAttribute("pingmax");
    String avgpingcon = (String)request.getAttribute("avgpingcon"); 
    String pingnow = (String)request.getAttribute("pingnow"); 
    
	String conMemoy = (String)request.getAttribute("conMemory");//连接内存
	String totalMemory = (String)request.getAttribute("totalMemory");//total
	String lockMem = (String)request.getAttribute("lockMem");//  锁定内存
	String optMemory = (String)request.getAttribute("optMemory");// 内存优化
	String sqlMem = (String)request.getAttribute("sqlMem");// sql缓存
	Integer count = (Integer)request.getAttribute("count");//获得告警次数
	String maxping =(String)request.getAttribute("ConnectUtilizationmax");//
	String downnum= (String)request.getAttribute("downnum");//宕机数
	String IsSingleUser = (String)request.getAttribute("IsSingleUser");//单用户模式
	String SecurityOnly = (String)request.getAttribute("SecurityOnly");//集成安全性模式
	String buding = (String)request.getAttribute("buding");//补丁
	String IsClustered = (String)request.getAttribute("IsClustered");//在故障转移群集中配置服务器实例
	String grade =  (String)request.getAttribute("grade");
	String runstar = "";
	runstar = (String)request.getAttribute("runstr");
	
	String typeName = (String)request.getAttribute("SQLSERVER");
  	String rootPath = request.getContextPath();
  	String dbname = "afunms";
  	String newip = (String)request.getAttribute("newip");
  	String ip = (String)request.getAttribute("ipaddress");
  	String startdate = (String)request.getAttribute("startdate");
  	String todate = (String)request.getAttribute("todate");
  	Hashtable alldatabase = new Hashtable();
  	Vector names = new Vector();
  	
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
<!-- 综合报表 DB2-->
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
	mainForm.action = "<%=rootPath%>/dbreport.do?action=createDB2CldReport&ipaddress=<%=ip%>&str=2";
	mainForm.submit();
}
function ping_word_report(){
	mainForm.action = "<%=rootPath%>/dbreport.do?action=createDB2CldReport&ipaddress=<%=ip%>&str=0";
	mainForm.submit();
}
function ping_pdf_report(){
	mainForm.action = "<%=rootPath%>/dbreport.do?action=createDB2CldReport&ipaddress=<%=ip%>&str=1";
	mainForm.submit();
}
function ping_ok(){
	var starttime = document.getElementById("mystartdate").value;
	var endtime = document.getElementById("mytodate").value;
	var status = document.getElementById("status").value;
	var level1 = document.getElementById("level1").value;
	//mainForm.action = "<%=rootPath%>/hostreport.do?action=showPingReport&ipaddress= &startdate="+starttime+"&todate="+endtime;
	mainForm.action = "<%=rootPath%>/db2.do?action=db2ManagerCldReportQuery&ipaddress= &startdate="+starttime+"&todate="+endtime+"&status="+status+"&level1="+level1;
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
																				空闲比例 （%）
																			</td>
																		</tr>
																		<%
																		Iterator iterator = spaceInfo.keySet().iterator();
																		while(iterator.hasNext()){
																			String dbName = (String)iterator.next();
																			List toolsdb = (ArrayList)spaceInfo.get(dbName);
																			if(toolsdb != null && toolsdb.size()>0){
																				for(int i=0;i<toolsdb.size();i++){
																					Hashtable tempSpace = new Hashtable();
																					tempSpace = (Hashtable)toolsdb.get(i);
																					 String tablespace_name = (String)tempSpace.get("tablespace_name");
																				 	String totalspac = (String)tempSpace.get("totalspac");
																				 	String usablespac = (String)tempSpace.get("usablespac");
																				 	String usableper = (String)tempSpace.get("usableper");
																				 	
																				 	if(usableper == null || "0".equals(totalspac.trim())){
																				 		usableper = "100";
																				 	}
																				 	
																					%>
																					<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																						height="28">
																						<td class="application-detail-data-body-list"><%=i+1%></td>
																						<td align=center class="application-detail-data-body-list">
																							&nbsp;<%=tablespace_name%></td>
																						<td align=center class="application-detail-data-body-list">
																							&nbsp;<%=totalspac%></td>
																						<td align=center class="application-detail-data-body-list">
																							&nbsp;<%=usablespac%></td>
																						<td align=center class="application-detail-data-body-list">
																							&nbsp;<%=usableper%></td>
																					</tr>
																				<%}
																			}
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
																			<td align=center>连 接 信 息</td>
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
																			<td align=center>
																				数据库名称
																			</td>
																			<td align=center >
																				数据库路径
																			</td>
																			<td align=center >
																				数据库状态
																			</td>
																			<td align=center >
																				最后备份时间
																			</td>
																			<td align=center >
																				连接总数（个）
																			</td>
																			<td align=center >
																				最大连接数（个）
																			</td>
																			<td align=center >
																				当前连接数（个）
																			</td>
																			<td align=center>
																				失败的SQL数（个）
																			</td>
																			<td align=center>
																				成功的SQL数（个）
																			</td>
																		</tr>
																		<%
																		Enumeration dbs = conn.keys();
																		Hashtable<String,Object> allhash = new Hashtable();
																		List poolList = new ArrayList();
																		while(dbs.hasMoreElements()){								
																			String obj = (String)dbs.nextElement();
																			List connList = (List)conn.get(obj);
									
																		for(int i=0;i<connList.size();i++){
																			Hashtable ht = (Hashtable)connList.get(i);
																			String db_name = ht.get("db_name").toString();
																			String db_path = ht.get("db_path").toString();
																			String db_status = ht.get("db_status").toString();
																				String sqlm_elm_last_backup = ht.get("sqlm_elm_last_backup").toString();
																				String total_cons = ht.get("total_cons").toString();
																				String connections_top = ht.get("connections_top").toString();
																				String appls_cur_cons = ht.get("appls_cur_cons").toString();
																				String failedsql = ht.get("failedsql").toString();
																					String commitsql = ht.get("commitsql").toString();
																			
																			%>
																			<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																				height="28">
																				<td class="application-detail-data-body-list"><%=i+1%></td>
																				<td align=center class="application-detail-data-body-list">
																					&nbsp;<%=db_name%></td>
																				<td align=center class="application-detail-data-body-list">
																					&nbsp;<%=db_path%></td>
																				<td align=center class="application-detail-data-body-list">
																					&nbsp;<%=sysDbStatus[Integer.parseInt(db_status)]%></td>
																				<td align=center class="application-detail-data-body-list">
																					&nbsp;<%=sqlm_elm_last_backup%></td>
																				<td align=center class="application-detail-data-body-list">
																					&nbsp;<%=total_cons%></td>
																				<td align=center class="application-detail-data-body-list">
																					&nbsp;<%=connections_top%></td>
																				<td align=center class="application-detail-data-body-list">
																					&nbsp;<%=appls_cur_cons%></td>
																				<td align=center class="application-detail-data-body-list">
																					&nbsp;<%=failedsql%></td>
																				<td align=center class="application-detail-data-body-list">
																					&nbsp;<%=commitsql%></td>


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
																			<td align=center>缓 存 信 息</td>
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
																			<td align=center>
																				缓冲池名称
																			</td>
																			<td align=center>
																				数据命中率
																			</td>
																			<td align=center>
																				索引读取命中率
																			</td>
																			<td align=center>
																				缓存读取命中率
																			</td>
																			<td align=center>
																				异步读取比率
																			</td>
																			<td align=center>
																				直接读写数据库比率
																			</td>
																		</tr>
																		<%
																		Enumeration dbs2 = poolInfo.keys();
																		Hashtable<String,Object> allhash2 = new Hashtable();
																		List poolList2 = new ArrayList();
																		while(dbs2.hasMoreElements()){								
																			String obj = (String)dbs2.nextElement();
																			allhash = (Hashtable)poolInfo.get(obj);
																			poolList = (List)allhash.get("poolValue");
																			List lockList = (List)allhash.get("lockValue");
																			List readList = (List)allhash.get("readValue");
										   									List writeList = (List)allhash.get("writeValue");
																			for(int i=0;i<poolList.size();i++){
																				Hashtable ht = (Hashtable)poolList.get(i);
																				String bp_name = ht.get("bp_name").toString();
																				String data_hit_ratio = ht.get("data_hit_ratio").toString();
																				String index_hit_ratio = ht.get("index_hit_ratio").toString();
																				String BP_hit_ratio = ht.get("BP_hit_ratio").toString();
																				String Async_read_pct = ht.get("Async_read_pct").toString();
																				String Direct_RW_Ratio = ht.get("Direct_RW_Ratio").toString();
																		
																		%>
																		<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																			height="28">
																			<td class="application-detail-data-body-list"><%=i+1%></td>
																			<td align=center
																				class="application-detail-data-body-list">
																				&nbsp;<%=bp_name%></td>
																			<td align=center
																				class="application-detail-data-body-list">
																				&nbsp;<%=data_hit_ratio%></td>
																			<td align=center
																				class="application-detail-data-body-list">
																				&nbsp;<%=index_hit_ratio%></td>
																			<td align=center
																				class="application-detail-data-body-list">
																				&nbsp;<%=BP_hit_ratio%></td>
																			<td align=center
																				class="application-detail-data-body-list">
																				&nbsp;<%=Async_read_pct%></td>
																			<td align=center
																				class="application-detail-data-body-list">
																				&nbsp;<%=Direct_RW_Ratio%></td>
																		</tr>
																		<%} }//end of while(dbs.hasMoreElements())%>
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
																			<td align=center>锁 信 息</td>
																		</tr>
																	</table>
																</td>
															</tr>
															<tr>
																<td>
																	<table width="96%" align="center" border="1" >
																		<tr bgcolor="#ECECEC">
																			<td align=center>
																				&nbsp;
																			</td>
																			<td align=center>
																				名称
																			</td>
																			<td align=center>
																				读行数
																			</td>
																			<td align=center>
																				查询行数
																			</td>
																			<td align=center>
																				锁等待数
																			</td>
																			<td align=center>
																				锁等待时间
																			</td>
																			<td align=center>
																				死锁数
																			</td>
																			<td align=center>
																				升级锁
																			</td>
																			<td align=center>
																				总排序
																			</td>
																			<td align=center>
																				总排序时间
																			</td>
																		</tr>
																		<%
																			Enumeration dbs3 = poolInfo.keys();
																			Hashtable<String,Object> allhash3 = new Hashtable();
																			List poolList3 = new ArrayList();
																			while(dbs3.hasMoreElements()){								
																				String obj = (String)dbs3.nextElement();
																				allhash3 = (Hashtable)poolInfo.get(obj);
																				poolList3 = (List)allhash3.get("poolValue");
																				//System.out.println("poolList size === "+poolList.size());
																				List lockList = (List)allhash3.get("lockValue");
																				List readList = (List)allhash3.get("readValue");
											   									List writeList = (List)allhash3.get("writeValue");
																				for(int i=0;i<lockList.size();i++){
																					Hashtable ht = (Hashtable)lockList.get(i);
																					String db_name = ht.get("db_name").toString();
																					String rows_read = ht.get("rows_read").toString();
																					String rows_selected = ht.get("rows_selected").toString();
																					String lock_waits = ht.get("lock_waits").toString();
																					String lock_wait_time = ht.get("lock_wait_time").toString();
																					String deadlocks = ht.get("deadlocks").toString();
																					String lock_escals = ht.get("lock_escals").toString();
																					String total_sorts = ht.get("total_sorts").toString();
																					String total_sort_time = ht.get("total_sort_time").toString();
																		
																		%>
																		<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																			height="28">
																			<td class="application-detail-data-body-list"><%=i+1%></td>
																			<td align=center
																				class="application-detail-data-body-list">
																				&nbsp;<%=db_name%></td>
																			<td align=center
																				class="application-detail-data-body-list">
																				&nbsp;<%=rows_read%></td>
																			<td align=center
																				class="application-detail-data-body-list">
																				&nbsp;<%=rows_selected%></td>
																			<td align=center
																				class="application-detail-data-body-list">
																				&nbsp;<%=lock_waits%></td>
																			<td align=center
																				class="application-detail-data-body-list">
																				&nbsp;<%=lock_wait_time%></td>
																			<td align=center
																				class="application-detail-data-body-list">
																				&nbsp;<%=deadlocks%></td>
																			<td align=center
																				class="application-detail-data-body-list">
																				&nbsp;<%=lock_escals%></td>
																			<td align=center
																				class="application-detail-data-body-list">
																				&nbsp;<%=total_sorts%></td>
																			<td align=center
																				class="application-detail-data-body-list">
																				&nbsp;<%=total_sort_time%></td>


																		</tr>
																		<%}}%>
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
																			<td align=center> 日 志 信 息 </td>
																		</tr>
																	</table>
																</td>
															</tr>
															<tr>
																<td>
																	<table width="96%" align="center" border="1" >
																		<tr bgcolor="#ECECEC">
																			<td align=center >
																				&nbsp;
																			</td>
																			<td align=center >
																				数据库名称
																			</td>
																			<td align=center >
																				已使用大小(Mb)
																			</td>
																			<td align=center>
																				空闲大小(Mb)
																			</td>
																			<td align=center>
																				使用率(%)
																			</td>
																			<td align=center>
																				最大使用大小(Mb)
																			</td>
																			<td align=center>
																				最大使用大小/秒
																			</td>
																		</tr>
																		<%
																		Enumeration dbs4 =  log.keys();       
																		Hashtable<String,Object> allhash4 = new Hashtable();
																		List poolList4 = new ArrayList();
																		while(dbs4.hasMoreElements()){								
																			String obj = (String)dbs4.nextElement();
																			List logList = (List)log.get(obj);
																			for(int i=0;i<logList.size();i++){
																				Hashtable ht = (Hashtable)logList.get(i);
																				String logused = ht.get("logused").toString();
																				String logspacefree = ht.get("logspacefree").toString();
																				String pctused = ht.get("pctused").toString();
																				String maxlogused = ht.get("maxlogused").toString();
																				String maxsecused = ht.get("maxsecused").toString();
																		
																		%>
																		<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																			height="28">
																			<td class="application-detail-data-body-list"><%=i+1%></td>
																			<td align=center
																				class="application-detail-data-body-list">
																				&nbsp;<%=obj%></td>
																			<td align=center
																				class="application-detail-data-body-list">
																				&nbsp;<%=logused%></td>
																			<td align=center
																				class="application-detail-data-body-list">
																				&nbsp;<%=logspacefree%></td>
																			<td align=center
																				class="application-detail-data-body-list">
																				&nbsp;<%=pctused%></td>
																			<td align=center
																				class="application-detail-data-body-list">
																				&nbsp;<%=maxlogused%></td>
																			<td align=center
																				class="application-detail-data-body-list">
																				&nbsp;<%=maxsecused%></td>


																		</tr>
																		<%}}//end of while(dbs.hasMoreElements())%>
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
																			<td align=center> 表性能信息 </td>
																		</tr>
																	</table>
																</td>
															</tr>
															<tr>
																<td>
																	<table width="100%" border="1" cellspacing="0" cellpadding="0">
																	<%
																		Enumeration dbs5 = poolInfo.keys();
																		Hashtable<String,Object> allhash5 = new Hashtable();
																		List poolList5 = new ArrayList();
																		String obj = null;
																		Hashtable allhash6 = null;  
																		List lockList = null;
																		List readList = null;
																		List writeList = null;
																		while(dbs5.hasMoreElements()){								
																			obj = (String)dbs5.nextElement();
																			allhash6 = (Hashtable)poolInfo.get(obj); 
																			poolList = (List)allhash6.get("poolValue");
																			//System.out.println("poolList size === "+poolList.size());
																			lockList = (List)allhash6.get("lockValue");
																			readList = (List)allhash6.get("readValue");
										   								    writeList = (List)allhash6.get("writeValue");
										   								}
													   				 %>
																		<tr bgcolor="#ECECEC">
																			<td width="76%" height="28">
																				&nbsp;读频率最高的10张表
																			</td>
																		</tr>
																		<tr>
																			<td >
																				<table width="96%" align="center" border="1" >
																					<tr bgcolor="#ECECEC">
																						<td align=center>
																							&nbsp;
																						</td>
																						<td align=center>
																							模式
																						</td>
																						<td align=center>
																							表名称
																						</td>
																						<td align=center>
																							读行数
																						</td>
																						<td align=center>
																							写行数
																						</td>
																						<td align=center>
																							溢出访问
																						</td>
																						<td align=center>
																							页重组数
																						</td>
																					</tr>
																					<%
																						if(readList != null && readList.size()>0){
																						for(int i=0;i<readList.size();i++){
																							Hashtable ht = (Hashtable)readList.get(i);
																							String tbschema = ht.get("tbschema").toString();
																							String tbname = ht.get("tbname").toString();
																							String rows_read = ht.get("rows_read").toString();
																							String rows_written = ht.get("rows_written").toString();
																							String overflow_accesses = ht.get("overflow_accesses").toString();
																							String page_reorgs = ht.get("page_reorgs").toString();
																							
																					%>
																					<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>>
																						<td class="application-detail-data-body-list"><%=i+1%></td>
																						<td align=center
																							class="application-detail-data-body-list">
																							&nbsp;<%=tbschema%></td>
																						<td align=center
																							class="application-detail-data-body-list">
																							&nbsp;<%=tbname%></td>
																						<td align=center
																							class="application-detail-data-body-list">
																							&nbsp;<%=rows_read%></td>
																						<td align=center
																							class="application-detail-data-body-list">
																							&nbsp;<%=rows_written%></td>
																						<td align=center
																							class="application-detail-data-body-list">
																							&nbsp;<%=overflow_accesses%></td>
																						<td align=center
																							class="application-detail-data-body-list">
																							&nbsp;<%=page_reorgs%></td>

																					</tr>
																					<%}}%>
																				</table>
																			</td>
																		</tr>

																		<tr bgcolor="#ECECEC">
																			<td width="76%" height="28">
																				&nbsp;写频率最高的10张表
																			</td>
																		</tr>
																		<tr>
																			<td>
																				<table width="96%" align="center" border="1" >
																					<tr bgcolor="#ECECEC">
																						<td align=center>
																							&nbsp;
																						</td>
																						<td align=center>
																							模式
																						</td>
																						<td align=center>
																							表名称
																						</td>
																						<td align=center>
																							读行数
																						</td>
																						<td align=center>
																							写行数
																						</td>
																						<td align=center>
																							溢出访问
																						</td>
																						<td align=center>
																							页重组数
																						</td>
																					</tr>
																					<%
																					if(writeList != null && writeList.size()>0){
																						for(int i=0;i<writeList.size();i++){
																							Hashtable ht = (Hashtable)writeList.get(i);
																							String tbschema = ht.get("tbschema").toString();
																							String tbname = ht.get("tbname").toString();
																							String rows_read = ht.get("rows_read").toString();
																							String rows_written = ht.get("rows_written").toString();
																							String overflow_accesses = ht.get("overflow_accesses").toString();
																							String page_reorgs = ht.get("page_reorgs").toString();
																							
																					%>
																					<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>>
																						<td class="application-detail-data-body-list"><%=i+1%></td>
																						<td align=center
																							class="application-detail-data-body-list">
																							&nbsp;<%=tbschema%></td>
																						<td align=center
																							class="application-detail-data-body-list">
																							&nbsp;<%=tbname%></td>
																						<td align=center
																							class="application-detail-data-body-list">
																							&nbsp;<%=rows_read%></td>
																						<td align=center
																							class="application-detail-data-body-list">
																							&nbsp;<%=rows_written%></td>
																						<td align=center
																							class="application-detail-data-body-list">
																							&nbsp;<%=overflow_accesses%></td>
																						<td align=center
																							class="application-detail-data-body-list">
																							&nbsp;<%=page_reorgs%></td>

																					</tr>
																					<%} }//end of while(dbs.hasMoreElements())%>
																				</table>
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