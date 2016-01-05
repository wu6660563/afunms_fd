<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@ include file="/include/globe.inc"%>
<%@ page import="com.afunms.event.model.EventList"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.topology.model.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%
  String rootPath = request.getContextPath();
  //System.out.println(rootPath);
  List list = (List)request.getAttribute("list");
  
String startdate = (String)request.getAttribute("startdate");
String todate = (String)request.getAttribute("todate");
Hashtable allreporthash = (Hashtable)request.getAttribute("allreporthash");

String menuTable = (String)request.getAttribute("menuTable");
%>

<html>
<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8"/>
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<meta http-equiv="Page-Enter" content="revealTrans(duration=x, transition=y)">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 
<script language="javascript">	

function accEvent(eventid){
	mainForm.eventid.value=eventid;
	mainForm.action="<%=rootPath%>/event.do?action=accit";	
	mainForm.submit();
}

function query(){
	var startdate = mainForm.startdate.value;
  	var todate = mainForm.todate.value;    
  	//request.setAttribute("allreporthash", allreporthash);
 	window.open ("<%=rootPath%>/netreport.do?action=downloadselfnetchocereport&startdate="+startdate+"&todate="+todate)    	      

	//mainForm.action="<%=rootPath%>/netreport.do?action=netevent";
	//mainForm.submit();
} 
  function doQuery()
  {  
     if(mainForm.key.value=="")
     {
     	alert("请输入查询条件");
     	return false;
     }
     mainForm.action = "<%=rootPath%>/perform.do?action=find";
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
      mainForm.action = "<%=rootPath%>/perform.do?action=ready_add";
      mainForm.submit();
  }
  
    function doDelete()
  {  
     mainForm.action = "<%=rootPath%>/event.do?action=dodelete";
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
     
       // Ext.MessageBox.wait('数据加载中，请稍后.. '); 
        //msg.style.display="block";
	//mainForm.action="<%=rootPath%>/netreport.do?action=netchocereport";
	mainForm.submit();        
        //mainForm.action = "<%=rootPath%>/perform.do?action=add";
        //mainForm.submit();
     //}  
       // mainForm.submit();
 });	
	
});

function openwin(str,operate,ip) 
{	
  var startdate = mainForm.startdate.value;
  var todate = mainForm.todate.value;
  window.open ("<%=rootPath%>/hostreport.do?action="+operate+"&startdate="+startdate+"&todate="+todate+"&ipaddress="+ip+"&str="+str+"&type=host", "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes") 
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
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa">
<div id="loading">
	<div class="loading-indicator">
	<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif" width="32" height="32" style="margin-right: 8px;" align="middle" />Loading...</div>
</div>
<div id="loading-mask" style=""></div>
<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
<form method="post" name="mainForm">
<input type=hidden name="eventid">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
					<td width="200" valign=top align=center>

						<%=menuTable%>

					</td>
					<td align="center" valign=top>
			<table width="98%"  cellpadding="0" cellspacing="0" algin="center">
			<tr>
				<td background="<%=rootPath%>/common/images/right_t_02.jpg" width="100%"><table width="100%" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
                    <td class="layout_title"><b>资源 >> 指标对比 >> 网络设备</b></td>
                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                  </tr>
              </table>
			  </td>
			  </tr>
				<tr>
											<td colspan="2">
												<table cellspacing="1" cellpadding="0" width="100%">
													<tr align="center" class="microsoftLook0" height=25>
        <td width="10%" rowspan="2"><b>IP地址</b></td>
        <td width="7%" rowspan="2"><b>别名</b></td>
    	 <td width="7%" colspan="2"><b>CPU利用率</b></td>
    	<td width="7%" colspan="2"><b>出口流速</b></td>
    	 <td width="7%" colspan="2"><b>入口流速</b></td>
    	<td width="7%" colspan="3"><b>事件统计</b></td>
    	 
   </tr>
   <tr align="center" class="microsoftLook0" height=20>
    <td   width="9%" align="center"><b>平均值</b></td>
    <td   width="9%" align="center"><b>最大值</b></td>
       
    <td   width="9%" align="center"><b>平均值(KB/S)</b></td>
    <td   width="9%" align="center"><b>最大值(KB/S)</b></td>
	
	<td   width="9%" align="center"><b>平均值(KB/S)</b></td>
    <td   width="9%" align="center"><b>最大值(KB/S)</b></td>
    
    <td   width="10%" align="center"><b>普通事件</b></td>
    <td   width="10%" align="center"><b>严重事件</b></td>
	<td   width="10%" align="center"><b>紧急事件</b></td>
   </tr>
<%
  	java.text.SimpleDateFormat _sdf = new java.text.SimpleDateFormat("MM-dd HH:mm");
if (allreporthash != null && allreporthash.size() > 0) {
				Iterator keys = allreporthash.keySet().iterator();
				String ip = "";
				while (keys.hasNext()) {
					ip = keys.next().toString();
					Hashtable report_has = (Hashtable) allreporthash.get(ip);
					String hostname = (String) report_has.get("equipname");
					Hashtable CPU = (Hashtable) report_has.get("CPU");
					String Ping = (String) report_has.get("Ping");
					String avginput = (String) report_has.get("avginput");
					String avgoutput = (String) report_has.get("avgoutput");
					String maxinput = (String) report_has.get("maxinput");
					String maxoutput = (String) report_has.get("maxoutput");

					String levelone = (String) report_has.get("levelone");
					String levletwo = (String) report_has.get("levletwo");
					String levelthree = (String) report_has.get("levelthree");
%>

 <tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> class="microsoftLook" height=25 align="center">

       <td><%=ip%></td>
       <td><%=hostname%></td>
       <td><%=(String) CPU.get("avgcpu")%>&nbsp;</td>
      <td><%=(String) CPU.get("cpumax")%></td>
      <td><%=avgoutput.replace(".0", "")%></td>
       <td><%=maxoutput.replace(".0", "")%>&nbsp;</td>
       <td><%=avginput.replace(".0", "")%>&nbsp;</td>
      <td><%=maxinput.replace(".0", "")%></td>
       <td><%=levelone%>&nbsp;</td>
       <td><%=levletwo%>&nbsp;</td>
        <td><%=levelthree%></td>
      
 </tr>
 <%				}
 	}

 %>  
   
 </table>
								</td>
							</tr>	
				<tr>
					              <td background="<%=rootPath%>/common/images/right_b_02.jpg" ><table width="100%" border="0" cellspacing="0" cellpadding="0">
					                  <tr>
					                    <td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
					                    <td></td>
					                    <td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
					                  </tr>
					              </table></td>
					            </tr>
			</table>
		</td>
	</tr>
</table>
</form>
</BODY>
</HTML>
