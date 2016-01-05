<%@ page language="java" contentType="text/html;charset=gb2312"%>

<html>
<head>
<%
String rootPath = request.getContextPath();
  String hostname = (String)request.getAttribute("hostname");
  String hostnameDoc = (String)request.getAttribute("hostnameDoc");
  String typename = (String)request.getAttribute("typename");
  String grade = (String)request.getAttribute("grade");
  String avgcpu = (String)request.getAttribute("avgcpu");
  String cpumax = (String)request.getAttribute("cpumax");
  String avgvalue = (String)request.getAttribute("avgvalue");
  String PhysicalMemory = (String)request.getAttribute("PhysicalMemory");
  int cpuvalue = (Integer)request.getAttribute("cpuvalue");
  int memvalue = (Integer)request.getAttribute("memvalue");
  int pingvalue = (Integer)request.getAttribute("pingvalue");
  int diskvalue = (Integer)request.getAttribute("diskvalue");
  String runningStatus = (String)request.getAttribute("runningStatus");
  String ipaddress = (String)request.getAttribute("ipaddress");
  String startdate = (String)request.getAttribute("startdate");
  String todate = (String)request.getAttribute("todate");
%>
<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>

<title>决策报表</title>
<!-- snow add 2010-5-28 -->
<style>
<!--
body{
background-image: url(${pageContext.request.contextPath}/common/images/menubg_report.jpg);
TEXT-ALIGN: center; 
}
-->
</style>
<!-- snow add end -->
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
function analyse_word_report()
{
	mainForm.action = "<%=rootPath%>/hostreport.do?action=downloadAnalyseReport&ipaddress=<%=ipaddress%>&str=2";
	mainForm.submit();
}
function analyse_excel_report()
{ 
	mainForm.action = "<%=rootPath%>/hostreport.do?action=downloadAnalyseReport&ipaddress=<%=ipaddress%>&str=5";
	mainForm.submit();
}
function analyse_pdf_report()
{
	mainForm.action = "<%=rootPath%>/hostreport.do?action=downloadAnalyseReport&ipaddress=<%=ipaddress%>&str=3";
	mainForm.submit();
}
function analyse_ok()
{
	var starttime = document.getElementById("mystartdate").value;
	var endtime = document.getElementById("mytodate").value;
	mainForm.action = "<%=rootPath%>/hostreport.do?action=showAnalyseReport&ipaddress=<%=ipaddress%>&startdate="+starttime+"&todate="+endtime;
	mainForm.submit();
}
function analyse_cancel()
{
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
							                	<td class="win-content-title" style="align:center">&nbsp;决策报表</td>
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
															<input type="text" id="mytodate" name="todate" value="<%=todate%>" size="10"/>
															<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
															<img id=imageCalendar2 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a>
															<input type="button" name="doprocess" value="确定" onclick="analyse_ok()">
														</td>
														<td height="28" align="left">
															<a href="javascript:analyse_word_report()"><img name="selDay1" alt='导出word' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_word.gif" width=18  border="0">导出WORLD</a>
														</td>
														<td height="28" align="left">
															<a href="javascript:analyse_excel_report()"><img name="selDay1" alt='导出EXCEL' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=18  border="0">导出EXCEL</a>
														</td>
														<td height="28" align="left">&nbsp;
															<a href="javascript:analyse_pdf_report()"><img name="selDay1" alt='导出word' style="CURSOR: hand" src="<%=rootPath%>/resource/image/export_pdf.gif" width=18 border="0">导出PDF</a>
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
													<table width="90%">
			<tr>
				<td align="center">
					<table width="680" height="570" border="1">
  <tr>
	<td height="29" colspan="6"><p align="center" ><b>设备<%=hostname %>运行状况分析报告</b> </p></td>
  </tr>
  <tr>
    <td width="106" height="25"><p align="center" >日期</p></td>
    <td colspan="5"><p align="center" ><%=startdate %> 00:00:00 至<%=todate %> 23:59:59</td>
  </tr>
  <tr>
    <td height="24"><p align="center" >设备名称 </p></td>
    <td width="106"><p align="center" ><%=hostnameDoc %></p></td>
    <td width="43"><p align="center" >IP</p></td>
    <td width="132"><p align="center" ><%=ipaddress %></p></td>
    <td width="143"><p align="center" >设备类型</p></td>
    <td width="110"><%=typename %>&nbsp;</td>
  </tr>
  <tr>
    <td height="23"><p align="center" >运行评价 </p></td>
    <td colspan="5"><p align="center"><%=grade %></p></td>
  </tr>
  <tr>
    <td height="167" rowspan="2"><p align="center" >参 </p>
      <p align="center" >数 </p>
      <p align="center" >指 </p>
    <p align="center" >标</p></td>
    <td height="51" colspan="2"><p align="center" >平均CPU </p></td>
    <td height="51"><p align="center" ><%=avgcpu %></p></td>
    <td height="51"><p align="center" >最大CPU</p></td>
    <td height="51"><p align="center" ><%=cpumax %></p></td>
  </tr>
  <tr>
    <td height="45" colspan="2"><p align="center" >平均内存利用率</p></td>
    <td><p align="center" ><%=avgvalue %></p></td>
    <td><p align="center" >最大内存利用率</p></td>
    <td><p align="center" ><%=PhysicalMemory %></p></td>
  </tr>
  
  <tr>
    <td rowspan="4"><p align="center" >事 </p>
      <p align="center" >件 </p>
      <p align="center" >汇 </p>
    <p align="center" >总</p></td>
    <td height="33" colspan="5"><p align="center" >CPU事件<%=cpuvalue %>次 </p></td>
  </tr>
  <tr>
    <td height="36" colspan="5"><p align="center" >内存事件<%=memvalue %>次 </p></td>
  </tr>
  <tr>
    <td height="36" colspan="5"><p align="center" >连通率事件<%=pingvalue %>次 </p></td>
  </tr>
  <tr>
    <td height="32" colspan="5"><p align="center" >磁盘事件<%=diskvalue%>次 </p></td>
  </tr>
  <tr>
    <td height="29" colspan="6"><p align="center" >业务运行状况分析 </p></td>
  </tr>
  <tr>
    <td colspan="6">
    	<%=runningStatus %>
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
			
            		<div align=center>
            			<input type=button value="关闭窗口" onclick="window.close()">
            		</div>  
					<br>
</form>
</body>
</html>