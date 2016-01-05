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
	
	Vector val=(Vector)request.getAttribute("Val");
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
<!-- 性能报表 mysql-->
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
	mainForm.action = "<%=rootPath%>/dbreport.do?action=createMySQLSelfReport&ipaddress=<%=ip%>&str=1";
	mainForm.submit();
}
function ping_excel_report()
{
	mainForm.action = "<%=rootPath%>/dbreport.do?action=createMySQLSelfReport&ipaddress=<%=ip%>&str=0";
	mainForm.submit();
}
function ping_pdf_report()
{
	mainForm.action = "<%=rootPath%>/dbreport.do?action=createMySQLSelfReport&ipaddress=<%=ip%>&str=2";
	mainForm.submit();
}
function ping_ok()
{
	var starttime = document.getElementById("mystartdate").value;
	var endtime = document.getElementById("mytodate").value;
	//mainForm.action = "<%=rootPath%>/hostreport.do?action=showPingReport&ipaddress= &startdate="+starttime+"&todate="+endtime;
	mainForm.action = "<%=rootPath%>/mysql.do?action=mysqlManagerNatureReportQuery&ipaddress= &startdate="+starttime+"&todate="+endtime;
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