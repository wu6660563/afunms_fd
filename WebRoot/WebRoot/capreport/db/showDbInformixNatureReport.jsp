<%@ page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%@ page import="com.afunms.application.model.DBVo"%>
<%@ page import="java.util.*" %>
<%@page import="java.text.*"%>
<html>
<head>
<%	
	DBVo vo  = (DBVo)request.getAttribute("db");
	ArrayList dbspaces = new ArrayList();
	Hashtable dbinfo = new Hashtable();
	dbinfo = (Hashtable)request.getAttribute("dbValue");
	if(dbinfo!=null){
	    dbspaces = (ArrayList)dbinfo.get("informixspaces");//数据库空间信息
	}
	ArrayList dblock = new ArrayList();
	ArrayList dbsession = new ArrayList();
	ArrayList dbdatabase = new ArrayList();
	String name="";
	String dbserver="";
	String createuser="";
	String createtime="";
	//数据库基本信息
	Hashtable databaseinfo = new Hashtable();
	if(dbinfo!=null){
	dbdatabase = (ArrayList)dbinfo.get("databaselist");
	if(dbdatabase!=null&&dbdatabase.size()>0){
	  databaseinfo = (Hashtable)dbdatabase.get(0);  
	  name=(String)databaseinfo.get("dbname");
	  dbserver=(String)databaseinfo.get("dbserver");
	  createuser=(String)databaseinfo.get("createuser");
	  createtime=(String)databaseinfo.get("createtime");
	}
	dbsession = (ArrayList)dbinfo.get("sessionList");//会话信息
	dblock = (ArrayList)dbinfo.get("lockList");//锁信息
	
	
			
	}
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
  	
%>
<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
<!-- 性能报表 infomix-->
<title>性能报表</title>
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
function ping_word_report()
{
	mainForm.action = "<%=rootPath%>/dbreport.do?action=createInformixSelfReport&ipaddress=<%=ip%>&str=1";
	mainForm.submit();
}
function ping_excel_report()
{
	mainForm.action = "<%=rootPath%>/dbreport.do?action=createInformixSelfReport&ipaddress=<%=ip%>&str=0";
	mainForm.submit();
}
function ping_pdf_report()
{
	mainForm.action = "<%=rootPath%>/dbreport.do?action=createInformixSelfReport&ipaddress=<%=ip%>&str=2";
	mainForm.submit();
}
function ping_ok()
{
	var starttime = document.getElementById("mystartdate").value;
	var endtime = document.getElementById("mytodate").value;
	//mainForm.action = "<%=rootPath%>/hostreport.do?action=showPingReport&ipaddress= &startdate="+starttime+"&todate="+endtime;
	mainForm.action = "<%=rootPath%>/informix.do?action=informixManagerNatureReportQuery&ipaddress= &startdate="+starttime+"&todate="+endtime;
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
	
 Ext.get("process").on("click",function(){
     
     //if(chk1&&chk2&&chk3)
     //{
     
        Ext.MessageBox.wait('数据加载中，请稍后.. '); 
        //msg.style.display="block";
	mainForm.action="<%=rootPath%>/hostreport.do?action=hostcpu";
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
								                	<td class="win-content-title" style="align:center">&nbsp;性能报表</td>
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
																<input type="text" id="mystartdate" name="startdate" value="<%=   startdate%>" size="10">
																<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
																<img id=imageCalendar1 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a>
																								
																截止日期
																<input type="text" id="mytodate" name="todate" value=" <%=   todate%>" size="10"/>
																<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
																<img id=imageCalendar2 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a>
																<!--   --><input type="button" name="doprocess" value="确定" onclick="ping_ok()"> 
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
																	<b>&nbsp;Informix数据库表空间信息&nbsp;&nbsp;</b>
																</td>
															</tr>
															<tr bgcolor="#FFFFFF">
																<td align=center colspan=2>

																	<table width="90%" border="0" cellpadding="3"
																		cellspacing="1" bgcolor="#FFFFFF">
																		<tr bgcolor="#ECECEC" height=28>
																			<td width="15%"
																				class="application-detail-data-body-title">
																				<strong>空间名称</strong>
																			</td>
																			<td width="15%"
																				class="application-detail-data-body-title">
																				<strong>空间所有者</strong>
																			</td>
																			<td width="15%"
																				class="application-detail-data-body-title">
																				<strong>该块文件的路径</strong>
																			</td>
																			<td width="15%"
																				class="application-detail-data-body-title">
																				<strong>空间大小</strong>
																			</td>
																			<td width="15%"
																				class="application-detail-data-body-title">
																				<strong>已使用的空间</strong>
																			</td>
																			<td width="13%"
																				class="application-detail-data-body-title">
																				<strong>空闲空间</strong>
																			</td>
																			<td width="15%"
																				class="application-detail-data-body-title">
																				<strong>空间使用率</strong>
																			</td>
																		</tr>
																		<% 
													                      if (dbspaces != null) {
																		      if (dbspaces.size()>0){
																		          DecimalFormat df=new DecimalFormat("#.###");
															            	      for(int i=0;i<dbspaces.size();i++){
															            	          Hashtable tablesVO = (Hashtable)dbspaces.get(i);
                  															%>
																		<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																			height=28>
																			<td class="application-detail-data-body-list"><%=tablesVO.get("dbspace")%></td>
																			<td class="application-detail-data-body-list"><%=tablesVO.get("owner")%></td>
																			<td class="application-detail-data-body-list"><%=tablesVO.get("fname")%></td>
																			<td class="application-detail-data-body-list"><%=df.format(Float.parseFloat(tablesVO.get("pages_size")+""))+"M"%></td>
																			<td class="application-detail-data-body-list"><%=df.format(Float.parseFloat(tablesVO.get("pages_used")+""))+"M"%></td>
																			<td class="application-detail-data-body-list"><%=df.format(Float.parseFloat(tablesVO.get("pages_free")+""))+"M"%></td>
																			<td class="application-detail-data-body-list"><%=df.format(100-Float.parseFloat(tablesVO.get("percent_free")+""))+"%"%></td>
																		</tr>
																						<% 
																                              }
																                          }
																                      }
																                  %>
																	</table>
																		<br>
																	</td>
																</tr>
																<tr align="left" bgcolor="#ECECEC">
																	<td height="28" colspan=2>
																		<b>&nbsp;Informix数据库会话信息&nbsp;&nbsp;</b>
																	</td>
																</tr>
																<tr bgcolor="#FFFFFF">
																	<td align=center colspan=2>

																		<table width="90%" border="0" cellpadding="3"
																			cellspacing="1" bgcolor="#FFFFFF">
																			<tr bgcolor="#ECECEC" height=28>
																				<td width="15%"
																					class="application-detail-data-body-title">
																					<strong>用户名</strong>
																				</td>
																				<td width="15%"
																					class="application-detail-data-body-title">
																					<strong>主机</strong>
																				</td>
																				<td width="15%"
																					class="application-detail-data-body-title">
																					<strong>命中次数</strong>
																				</td>
																				<td width="15%"
																					class="application-detail-data-body-title">
																					<strong>锁的数量</strong>
																				</td>
																				<td width="15%"
																					class="application-detail-data-body-title">
																					<strong>顺序扫描访问数据的次数</strong>
																				</td>
																				<td width="13%"
																					class="application-detail-data-body-title">
																					<strong>排序总数</strong>
																				</td>
																				<td width="15%"
																					class="application-detail-data-body-title">
																					<strong>不适合内存排序数</strong>
																				</td>
																			</tr>
																				<%
														                        if (dbsession != null && dbsession.size()>0){
														                        	for(int i=0;i<dbsession.size();i++){
														                        		Hashtable tablesVO = (Hashtable)dbsession.get(i);
														                        		String seqscans = String.valueOf(tablesVO.get("seqscans"));
														                        		if("null".equals(seqscans)){
														                        			seqscans = "";
														                        		}
														                        		String total_sorts = String.valueOf(tablesVO.get("total_sorts"));
														                        		if("null".equals(total_sorts)){
														                        			total_sorts = "";
														                        		}
														                        		String dsksorts = String.valueOf(tablesVO.get("dsksorts"));
														                        		if("null".equals(dsksorts)){
														                        			dsksorts = "";
														                        		}
														                        
														                       %>
																			<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																				height=28>
																				<td class="application-detail-data-body-list"><%=tablesVO.get("username")%></td>
																				<td class="application-detail-data-body-list"><%=tablesVO.get("hostname")%></td>
																				<td class="application-detail-data-body-list"><%=tablesVO.get("access")%></td>
																				<td class="application-detail-data-body-list"><%=tablesVO.get("locksheld")%></td>
																				<td class="application-detail-data-body-list"><%=seqscans%></td>
																				<td class="application-detail-data-body-list"><%=total_sorts%></td>
																				<td class="application-detail-data-body-list"><%=dsksorts%></td>
																			</tr>
																				<%
														                       		}
														                       	}
														                       %>
																		</table>
																		<br>
																	</td>
																</tr>

																<tr align="left" bgcolor="#ECECEC">
																	<td height="23" colspan=2>
																		<b>&nbsp;Informix数据库锁信息&nbsp;&nbsp;</b>
																	</td>
																</tr>
																<tr bgcolor="#ECECEC">
																	<td align=center colspan=2>
																		<br>

																		<table width="90%" border="0" cellpadding="3"
																			cellspacing="1" bgcolor="#ECECEC">
																			<tr bgcolor="397dbd" height=25>
																				<td width="6%"
																					class="application-detail-data-body-title">
																					<strong>用户名</strong>
																				</td>
																				<td width="12%"
																					class="application-detail-data-body-title">
																					<strong>主机</strong>
																				</td>
																				<td width="16%"
																					class="application-detail-data-body-title">
																					<strong>数据库名称</strong>
																				</td>
																				<td width="9%"
																					class="application-detail-data-body-title">
																					<strong>表名称</strong>
																				</td>
																				<td width="12%"
																					class="application-detail-data-body-title">
																					<strong>锁的类型</strong>
																				</td>
																			</tr>
																						<%
																                        if (dblock != null && dblock.size()>0){
																                        	for(int i=0;i<dblock.size();i++){
																                        		Hashtable tablesVO = (Hashtable)dblock.get(i);
																                        		String type = (String)tablesVO.get("type");
																                        		String desc = "";
																                        		if("B".equals(type)){
																                        			desc = "字节锁";
																                        		}else if("IS".equals(type)){
																                        			desc = "意向共享锁";
																                        		}else if("S".equals(type)){
																                        			desc = "共享锁";
																                        		}else if("XS".equals(type)){
																                        			desc = "由可重复阅读器持有的共享键值";
																                        		}else if("U".equals(type)){
																                        			desc = "更新锁";
																                        		}else if("IX".equals(type)){
																                        			desc = "意向互斥锁";
																                        		}else if("SIX".equals(type)){
																                        			desc = "共享的意向互斥锁";
																                        		}else if("X".equals(type)){
																                        			desc = "互斥锁";
																                        		}else if("XR".equals(type)){
																                        			desc = "由可重复阅读器持有的互斥键值";
																                        		}
																                        
																                       %>
																						<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																						height=28>
																							<td class="application-detail-data-body-list"><%=tablesVO.get("username")%></td>
																							<td class="application-detail-data-body-list"><%=tablesVO.get("hostname")%></td>
																							<td class="application-detail-data-body-list"><%=tablesVO.get("dbsname")%></td>
																							<td class="application-detail-data-body-list"><%=tablesVO.get("tabname")%></td>
																							<td class="application-detail-data-body-list"><%=desc%></td>
																						</tr>
																						<%
																	                      		}
																	                      	}
																	                      %>
																					</table>
																					<br>
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
            	<div align=center>
            		<input type=button value="关闭窗口" onclick="window.close()">
            	</div>  
				<br>
		</form>  
	</body>
</html>