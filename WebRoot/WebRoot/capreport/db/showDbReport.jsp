<%@ page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@ page import="java.util.*"%>
<%@ page import="com.afunms.application.model.DBVo"%>
<html>
<head>
<%
	String newip = (String)request.getAttribute("newip");
	Double notTime =(Double)request.getAttribute("notpingcon");	
	Double avgpingcon = (Double) request.getAttribute("avgpingcon");
	String rootPath = request.getContextPath();
	String time1 =(String) session.getAttribute("Mytime1");
	String starttime =(String) session.getAttribute("Mystarttime1");
	String totime = (String)session.getAttribute("Mytotime1");
	DBVo vo = null;
	vo =(DBVo) request.getAttribute("db");
	String ipaddress = vo.getIpAddress();
	Vector tableinfo_v = (Vector)request.getAttribute("tableinfo_v");
	Hashtable dbio = (Hashtable)request.getAttribute("dbio");
	String id = (String)request.getAttribute("id");
  	String mysid = (String)request.getAttribute("sid");
	//HONGLI ADD START1
	String pingmin = (String)request.getAttribute("pingmin");//最小连通率
	String pingnow = (String)request.getAttribute("pingnow");//当前连通率
	//HONGLI ADD END1
 %>
<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
<title>数据库性能报表</title>
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
<script type="text/javascript" type="text/JavaScript"></script>
<script language="javaScript" type="text/javaScript">
function word_report()
{
	mainForm.action = "<%=rootPath%>/oracle.do?action=dboraReportdown&ipaddress=<%=ipaddress%>&str=1";
	mainForm.submit();
}
function ping_excel_report()
{
	mainForm.action = "<%=rootPath%>/oracle.do?action=dboraReportdown&ipaddress=<%=ipaddress%>&str=0";
	mainForm.submit();
}
function ping_pdf_report()
{
	mainForm.action = "<%=rootPath%>/oracle.do?action=dboraReportdown&ipaddress=<%=ipaddress%>&str=2";
	mainForm.submit();
}
function ping_ok()
{
	var starttime = document.getElementById("mystartdate").value;
	var endtime = document.getElementById("mytodate").value;
	//mainForm.action = "<%=rootPath%>/hostreport.do?action=showPingReport&ipaddress=<%=ipaddress%>&startdate="+starttime+"&todate="+endtime;
	mainForm.action = "<%=rootPath%>/hostreport.do?action=showOraclPerformaceReportByDate&ipaddress=<%=ipaddress%>&startdate="+starttime+"&todate="+endtime+"&sid="+<%=mysid%>;;
	mainForm.submit();
}
function ping_cancel()
{
window.close();
}
function query(){
  
 	window.open ("<%=rootPath%>/oracle.do?action=downloadmultiorareport&startdate="+startdate+"&todate="+todate+"&ids="+oids, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes")    	      
}

function openwin(str,operate,ip) 
{	
  var startdate = mainForm.startdate.value;
  var todate = mainForm.todate.value;
  window.open ("<%=rootPath%>/hostreport.do?action="+operate+"&startdate="+startdate+"&todate="+todate+"&ipaddress="+ip+"&str="+str+"&type=host", "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes") 
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
	<body id="body" class="body" >
	<iframe frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 189px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></iframe>
	<form id="mainForm" method="post" name="mainForm">
		<table id="container-main" class="container-main">
			<tr>
				<td>
					<table id="container-main-win" class="container-main-win">
						<tr>
							<td>
								<table id="win-content-header" class="win-content-header">
									<tr>
							        	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
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
														<%--<input type="text" id="mystartdate" name="startdate" value="<%=time1%>" size="10">
														--%>
														<input type="text" id="mystartdate" name="startdate" value="<%=starttime%>" size="10"><!-- HONGLI MODIFY -->
														<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
														<img id=imageCalendar1 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a>										
														截止日期
														<%--<input type="text" id="mytodate" name="todate" value="<%=time1%>" size="10"/>
														--%>
														<input type="text" id="mytodate" name="todate" value="<%=totime%>" size="10"/><!-- HONGLI MODIFY -->
														<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
														<img id=imageCalendar2 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a>
														<input type="button" name="doprocess" value="确定" onclick="ping_ok()">
													</td>
													<td height="28" align="left">
														<a href="javascript:word_report()"><img name="selDay1" alt='导出word' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_word.gif" width=18  border="0">导出WORLD</a>
													</td>
													<td height="28" align="left">
														<a href="javascript:ping_excel_report()"><img name="selDaword_reporty1" alt='导出EXCEL' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=18  border="0">导出EXCEL</a>
													</td>
													<td height="28" align="left">&nbsp;
														<a href="javascript:ping_pdf_report()"><img name="selDay1" alt='导出word' style="CURSOR: hand" src="<%=rootPath%>/resource/image/export_pdf.gif" width=18 border="0">导出PDF</a>
													</td>
												</tr>  
												
											</table>
						       			</td>
						       		</tr>  
							       	<tr>
							       		<td>
							       			<table class="application-detail-data-body" width="989" height="149">
												<tr>
													<td>
														<table width="100%" border="0" cellspacing="0"cellpadding="0">
															<tr height="28" bgcolor="#ECECEC">
																<td align=center>连通率</td>
																<td align=center>最小连通率</td>
																<td align=center>平均连通率</td>
															</tr>
															<tr>
																<%--
																<td class="application-detail-data-body-list"align=center>&nbsp;<%=avgpingcon%></td>
																<td class="application-detail-data-body-list"align=center>&nbsp;<%=notTime%></td>
																<td class="application-detail-data-body-list"align=center>&nbsp;<%=avgpingcon%></td>
																--%>
																<td class="application-detail-data-body-list"align=center>&nbsp;<%=pingnow%>%</td><!-- HONGLI MODIFY -->
																<td class="application-detail-data-body-list"align=center>&nbsp;<%=pingmin%>%</td><!-- HONGLI MODIFY -->
																<td class="application-detail-data-body-list"align=center>&nbsp;<%=avgpingcon%>%</td>
															</tr>
															
															<td colspan="4" align="center">
																<img src="<%=rootPath%>/resource/image/jfreechart/<%=newip %>ConnectUtilization.png"/>
															</td>
					
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
							       			<!--jhl add  -->
							       			<table class="application-detail-data-body" width="989" height="149">
												<tr>
													<td>
														<table width="100%" border="0" cellspacing="0"cellpadding="0">
															<tr height="28" bgcolor="#ECECEC">
																<td align=center>序号</td>
																<td align=center>文件名</td>
																<td align=center>表空间</td>
																<td align=center>空间大小(MB)</td>
																<td align=center>空闲大小(MB)</td>
																<td align=center>空闲比例</td>
																<td align=center>物理读</td>
																<td align=center>物理块读</td>
																<td align=center>物理写</td>
																<td align=center>物理块写</td>
																<td align=center>文件状态</td>
															</tr>
															<% 
																for(int i=0;i<tableinfo_v.size();i++){
																	Hashtable ht = (Hashtable)tableinfo_v.get(i);
																	String filename = ht.get("file_name").toString();
																	String tablespace = ht.get("tablespace").toString();
																	String size = ht.get("size_mb").toString();
																	String free = ht.get("free_mb").toString();
																	String percent = ht.get("percent_free").toString();
																	String status = ht.get("status").toString();
																	String pyr = "";String pbr = "";
																	String pyw = "";String pbw = "";
																	if(dbio.containsKey(filename)){
																		Hashtable iodetail = (Hashtable)dbio.get(filename);
																		if(iodetail != null && iodetail.size()>0){
																			pyr = (String)iodetail.get("pyr");
																			pbr = (String)iodetail.get("pbr");
																			pyw = (String)iodetail.get("pyw");
																			pbw = (String)iodetail.get("pbw");
																		}
																	}
															%>
															<tr height="28" bgcolor="#FFFFFF"<%=onmouseoverstyle%>>
																<td class="application-detail-data-body-list"align=center><%=i+1%></td>
																<td class="application-detail-data-body-list"align=center>&nbsp;<%=filename%></td>
																<td class="application-detail-data-body-list"align=center>&nbsp;<%=tablespace%></td>
																<td class="application-detail-data-body-list"align=center>&nbsp;<%=size%></td>
																<td class="application-detail-data-body-list"align=center>&nbsp;<%=free%></td>
																<td class="application-detail-data-body-list"align=center>&nbsp;<%=percent%></td>
																<td class="application-detail-data-body-list"align=center>&nbsp;<%=pyr%></td>
																<td class="application-detail-data-body-list"align=center>&nbsp;<%=pbr%></td>
																<td class="application-detail-data-body-list"align=center>&nbsp;<%=pyw%></td>
																<td class="application-detail-data-body-list"align=center>&nbsp;<%=pbw%></td>
																<td class="application-detail-data-body-list"align=center>&nbsp;<%=status%></td>
															</tr>
														<%} %>
														</table>
													</td>
												</tr>
											</table>
							       		</td><!-- jhl add -->
							       		
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