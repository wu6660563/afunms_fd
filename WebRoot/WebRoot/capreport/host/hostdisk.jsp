<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%@page import="java.util.*"%>


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

function query(){
	//subforms = document.forms[0];
	mainForm.action="<%=rootPath%>/netreport.do?action=netping";
	subforms.submit();
}

function changeOrder(para){
	mainForm.orderflag.value = para;
	mainForm.action="<%=rootPath%>/hostreport.do?action=hostdisk"
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
	mainForm.action="<%=rootPath%>/hostreport.do?action=hostmemory";
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
	document.getElementById('hostReportTitle-2').className='detail-data-title';
	document.getElementById('hostReportTitle-2').onmouseover="this.className='detail-data-title'";
	document.getElementById('hostReportTitle-2').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}
</script>


</head>
<body id="body" class="body" onload="initmenu();">
	<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 189px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
	<form id="mainForm" method="post" name="mainForm">
	<input type=hidden name="orderflag">
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
														    		<%=hostReportTitleTable%>
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
																								<img id=imageCalendar2 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a>
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
				        												
				        												<tr> 
                					<td>
                						<table width="100%" border="0" cellpadding="0" cellspacing="1">
                						<table>
  									<table>
  									<tr>
  										<td>主机服务器磁盘利用率报表&nbsp;&nbsp;时间段:&nbsp;<%=(String)request.getAttribute("starttime")%>&nbsp;至&nbsp;<%=(String)request.getAttribute("totime")%>
  										</td>
  									
                      								<td align=right valign=top>
                      								&nbsp;&nbsp;
															
											<a href="<%=rootPath%>/hostreport.do?action=downloaddiskreport&startdate=<%=startdate%>&todate=<%=todate%>" target="_blank"><img name="selDay1" alt='导出EXCEL' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=18  border="0">导出EXCEL</a>&nbsp;&nbsp;&nbsp;&nbsp;
											<a href="<%=rootPath%>/hostreport.do?action=createDiskPDF"><img
																	name="selDay1" alt='导出word' style="CURSOR: hand"
																	src="<%=rootPath%>/resource/image/export_pdf.gif"
																	width=18 border="0">导出PDF</a>&nbsp;&nbsp;&nbsp;&nbsp;
											<a href="<%=rootPath%>/hostreport.do?action=createdocDisk"><img
																	name="selDay1" alt='导出word' style="CURSOR: hand"
																	src="<%=rootPath%>/resource/image/export_word.gif"
																	width=18 border="0">导出WORLD</a>&nbsp;&nbsp;&nbsp;&nbsp;
                      										&nbsp;&nbsp;
                      								</td> 
                      							</tr>
                      							</table>
                    							<tr bgcolor="DEEBF7"> 
                      								<td>
  											<table   cellSpacing="1"  cellPadding="0" border=0 bgcolor=black width=100%>

  												<tr  bgcolor=#ffffff height=28>
    	<td align=center bgcolor=white width=5%>&nbsp;</td>
    	<td align=center bgcolor=white width=15%>IP地址</td>
		  <td align=center bgcolor=white width=10%>设备名称</td>
		  <td align=center bgcolor=white width=10%>操作系统</td>
		  <td align=center bgcolor=white width=10%>磁盘名称</td>
		  <td align=center bgcolor=white width=10%>
		  	总大小
		  	</td>
		  <td align=center bgcolor=white width=10%>已用大小</td>
		  <td align=center bgcolor=white width=10%>
		  	<html:submit  name="button3" type="button" styleClass="button" onclick="changeOrder('utilization')">利用率</html:submit>
		  	</td>
   												</tr>

<%
			int index = 0;
			List disklist = (List)request.getAttribute("disklist");
			if(disklist != null && disklist.size()>0){
				for(int i=0;i<disklist.size();i++){
						List _disklist = (List)disklist.get(i);
						String ip = (String)_disklist.get(0);
						String equname = (String)_disklist.get(1);	
						String osname = (String)_disklist.get(2);
						String name = (String)_disklist.get(3);
            String allsizevalue = (String)_disklist.get(4);
            String usedsizevalue = (String)_disklist.get(5);
						String utilization = (String)_disklist.get(6);
%>

<tr  bgcolor=#ffffff height=25>

    <td bgcolor=white align=center>&nbsp;<%=i+1%></td>
       <td bgcolor=white align=center>
      <%=ip%>&nbsp;</td>
      <td bgcolor=white align=center>
      <%=equname%></td>
      <td bgcolor=white align=center>
      <%=osname%></td>      
       <td bgcolor=white align=center>
      <%=name%></td>
       <td bgcolor=white align=center>
      <%=allsizevalue%></td>
       <td bgcolor=white align=center>
      <%=usedsizevalue%></td>
       <td bgcolor=white align=center>
      <%=utilization%></td>
 												</tr>
 <%
 }
}
			%>														   
																	
															
				        								</table>
				        							</td>
				        						</tr>
				        						
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