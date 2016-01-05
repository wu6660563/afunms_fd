<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>
<%@ page import="com.afunms.event.model.EventList"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.topology.model.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%
  String rootPath = request.getContextPath();
  //System.out.println(rootPath);
  List list = (List)request.getAttribute("list");
  String menuTable = (String)request.getAttribute("menuTable");
String startdate = (String)session.getAttribute("startdate");
String todate = (String)request.getAttribute("todate");
%>

<html>
<head>
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<meta http-equiv="Page-Enter" content="revealTrans(duration=x, transition=y)">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script language="javascript">	

function accEvent(eventid){
	mainForm.eventid.value=eventid;
	mainForm.action="<%=rootPath%>/event.do?action=accit";	
	mainForm.submit();
}

function query(){
	//subforms = document.forms[0];
	mainForm.action="<%=rootPath%>/tomcatreport.do?action=tomcatping";
	subforms.submit();
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
  
function changeOrder(para){
	mainForm.orderflag.value = para;
	mainForm.action="<%=rootPath%>/tomcatreport.do?action=tomcatping"
  	mainForm.submit();
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
}
</script>

</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa" onload="initmenu();">
<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
<form method="post" name="mainForm">
<input type=hidden name="orderflag">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td width="200" valign=top align=center rowspan="2">
		
				<%=menuTable %>
		</td>
		<td bgcolor="#cedefa" align="center" valign=top>
			<table width="98%" style="BORDER-COLLAPSE: collapse" borderColor=#397DBD cellPadding=0 rules=none border=1 algin="center">
				<tr>
					<td height="28" align="left" bordercolor="#397DBD" bgcolor="#397DBD" class="txtGlobalBold" >&nbsp;&nbsp;报表 >> 网络设备报表</td>
				</tr>				
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
				&nbsp;&nbsp;<input type="button" name="submitss" value="生成报表" onclick="query()">
				</td>
				</tr>
				
				<tr>
		
					<td bgcolor="#FFFFFF" valign="top" align=center>
					<br>
						<table cellSpacing="1" cellPadding="0" width="100%" border="0">	
              					<tr> 
                <td width="10" height="22">&nbsp;</td>
                <td width="80" height="22" background="<%=rootPath%>/resource/image/anjian.gif">
<div align="center"><a href="<%=rootPath%>/hostreport.do?action=list"><font color="#FFFFFF">连通率报表</font></a></div></td>
                <td width="80" background="<%=rootPath%>/resource/image/anjian_1.gif">
<div align="center"><a href="<%=rootPath%>/hostreport.do?action=memorylist"><font color="#397dbd">内存报表</font></a></div></td>
                <td width="80" background="<%=rootPath%>/resource/image/anjian_1.gif">
<div align="center"><a href="<%=rootPath%>/hostreport.do?action=disklist"><font color="#397dbd">磁盘报表</font></a></div></td>
                <td width="80" background="<%=rootPath%>/resource/image/anjian_1.gif">
<div align="center"><a href="<%=rootPath%>/hostreport.do?action=cpulist"><font color="#397dbd">CPU报表</font></a></div></td>	
                <td width="80" background="<%=rootPath%>/resource/image/anjian_1.gif">
<div align="center"><a href="<%=rootPath%>/hostreport.do?action=eventlist"><font color="#397dbd">事件报表</font></a></div></td>		
                <td width="80" background="<%=rootPath%>/resource/image/anjian_1.gif">
<div align="center"><a href="<%=rootPath%>/hostreport.do?action=multilist"><font color="#397dbd">综合报表</font></a></div></td>			
                <td>&nbsp;</td> 
              					</tr>
              					</table>
              				</td>
              			</tr>
              			<tr>
              				<td>
              					<table width="100%" border="0" cellpadding="0" cellspacing="1">
              
						<tr> 
                					<td>
                						<table width="100%" border="0" cellpadding="0" cellspacing="1">
  									<tr>
  										<td>主机服务器连通率报表&nbsp;&nbsp;时间段:&nbsp;<%=(String)request.getAttribute("starttime")%>&nbsp;至&nbsp;<%=(String)request.getAttribute("totime")%>
  										</td>
  									</tr>
  									<tr>
                      								<td align=right valign=top>
                      								&nbsp;&nbsp;
															<a href="<%=rootPath%>/tomcatreport.do?action=createdoc"><img
																	name="selDay1" alt='导出word' style="CURSOR: hand"
																	src="<%=rootPath%>/resource/image/export_word.gif"
																	width=18 border="0">导出WORLD</a>&nbsp;&nbsp;&nbsp;&nbsp;
															<a href="<%=rootPath%>/tomcatreport.do?action=downloadpingreport&startdate=<%=startdate%>&todate=<%=todate%>" target="_blank"><img name="selDay1" alt='导出EXCEL' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=18  border="0">导出EXCEL</a>&nbsp;&nbsp;&nbsp;&nbsp;
															<a href="<%=rootPath%>/hostreport.do?action=createpdf"><img
																	name="selDay1" alt='导出word' style="CURSOR: hand"
																	src="<%=rootPath%>/resource/image/export_pdf.gif"
																	width=18 border="0">导出PDF</a>&nbsp;&nbsp;&nbsp;&nbsp;
                      										&nbsp;&nbsp;
                      								</td> 
                      							</tr>
                    							<tr bgcolor="DEEBF7"> 
                      								<td>
  											<table   cellSpacing="1"  cellPadding="0" border=0 bgcolor=black width=100%>

  												<tr  class="firsttr">
    													<td align=center bgcolor=white width=5%>&nbsp;</td>
    													<td align=center bgcolor=white width=25%>IP地址</td>
		  											<td align=center bgcolor=white width=25%>设备名称</td>
		  											<!-- <td align=center bgcolor=white width=25%>端口号</td> -->
		  											<td align=center bgcolor=white width=10%>
		  												<input type="button"  name="button3" styleClass="button" onclick="changeOrder('avgping')" value="平均连通率">
		  											</td>
		  											<td align=center bgcolor=white width=10%>
		  												<input type="button"  name="button3" styleClass="button" onclick="changeOrder('downnum')" value="宕机次数">
		  											</td>
   												</tr>

<%
			int index = 0;
			//I_MonitorIpList monitorManager=new MonitoriplistManager();
			List pinglist = (List)request.getAttribute("pinglist");
			if(pinglist != null && pinglist.size()>0){
				for(int i=0;i<pinglist.size();i++){
						List _pinglist = (List)pinglist.get(i);
						String ip = (String)_pinglist.get(0);
						String equname = (String)_pinglist.get(1);	
						String portname = (String)_pinglist.get(2);
						String avgping = (String)_pinglist.get(3);
            String downnum = (String)_pinglist.get(4);
%>

 												<tr  class="othertr" <%=onmouseoverstyle%>>

    													<td bgcolor=white align=center>&nbsp;<%=i+1%></td>
       													<td bgcolor=white align=center>
      														<%=ip%>&nbsp;</td>
      													<td bgcolor=white align=center>
      														<%=equname%></td>
       													<td bgcolor=white align=center>
      														<%=avgping%></td>
       													<td bgcolor=white align=center>
      														<%=downnum%></td>
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
</form>
</BODY>
</HTML>
