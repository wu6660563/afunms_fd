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
<%@page import="com.afunms.application.model.DBVo"%>
<%@page import="com.afunms.application.model.DBTypeVo"%>
<%@page import="com.afunms.application.dao.DBTypeDao"%>
<%@page import="com.afunms.application.model.Tomcat"%>
<%@page import="com.afunms.polling.base.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.topology.dao.HostNodeDao"%>
<%@page import="com.afunms.application.model.IISConfig"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.application.model.*"%>
<%
  String rootPath = request.getContextPath();
  //System.out.println(rootPath);
  List list = (List)request.getAttribute("list");
   List listhost = (List)request.getAttribute("listhost");
   
   List dblist = (List)request.getAttribute("dblist");
    List tomcatlist = (List)request.getAttribute("tomcatlist");
   List iislist = (List)request.getAttribute("iislist");
   List weblogiclist = (List)request.getAttribute("weblogiclist");
String startdate = (String)request.getAttribute("startdate");
String todate = (String)request.getAttribute("todate");
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
  	var netoids ="";
  	var hostoids ="";
  	var dboids ="";
  	var tomcatoids ="";
  	var iisoids ="";
  	var weblogicoids ="";
  	var flag = false;
	var netcheckbox = document.getElementsByName("netcheckbox");
    for(var p = 0 ; p <netcheckbox.length ; p++){
		if(netcheckbox[p].checked ==true){
			if (netoids==""){
 					netoids=document.getElementsByName("netcheckbox")[p].value;
 			}else{
 					netoids=netoids+","+document.getElementsByName("netcheckbox")[p].value;
 			}
	 		flag = true;
		}
	}  	
	var hostcheckbox = document.getElementsByName("hostcheckbox");
    for(var p = 0 ; p <hostcheckbox.length ; p++){
		if(hostcheckbox[p].checked ==true){
			if (hostoids==""){
 					hostoids=document.getElementsByName("hostcheckbox")[p].value;
 			}else{
 					hostoids=hostoids+","+document.getElementsByName("hostcheckbox")[p].value;
 			}
	 		flag = true;
		}
	}  	
	
	var dbcheckbox = document.getElementsByName("dbcheckbox");
    for(var p = 0 ; p <dbcheckbox.length ; p++){
		if(dbcheckbox[p].checked ==true){
			if (dboids==""){
 					dboids=document.getElementsByName("dbcheckbox")[p].value;
 			}else{
 					dboids=dboids+","+document.getElementsByName("dbcheckbox")[p].value;
 			}
	 		flag = true;
		}
	}  	
	
	var tomcatcheckbox = document.getElementsByName("tomcatcheckbox");
    for(var p = 0 ; p <tomcatcheckbox.length ; p++){
		if(tomcatcheckbox[p].checked ==true){
			if (tomcatoids==""){
 					tomcatoids=document.getElementsByName("tomcatcheckbox")[p].value;
 			}else{
 					tomcatoids=tomcatoids+","+document.getElementsByName("tomcatcheckbox")[p].value;
 			}
	 		flag = true;
		}
	}  	
	
	var iischeckbox = document.getElementsByName("iischeckbox");
    for(var p = 0 ; p <iischeckbox.length ; p++){
		if(iischeckbox[p].checked ==true){
			if (iisoids==""){
 					iisoids=document.getElementsByName("iischeckbox")[p].value;
 			}else{
 					iisoids=iisoids+","+document.getElementsByName("iischeckbox")[p].value;
 			}
	 		flag = true;
		}
	}  	
	
	var weblogiccheckbox = document.getElementsByName("weblogiccheckbox");
    for(var p = 0 ; p <weblogiccheckbox.length ; p++){
		if(weblogiccheckbox[p].checked ==true){
			if (weblogicoids==""){
 					weblogicoids=document.getElementsByName("weblogiccheckbox")[p].value;
 			}else{
 					weblogicoids=weblogicoids+","+document.getElementsByName("weblogiccheckbox")[p].value;
 			}
	 		flag = true;
		}
	}  	

	if(flag)
	{
		//alert(weblogicoids);
	 	window.open ("<%=rootPath%>/servicecapreport.do?action=downloadselfservicechocereport&startdate="+startdate+"&todate="+todate+"&netoids="+netoids+"&hostoids="+hostoids+"&dboids="+dboids+"&tomcatoids="+tomcatoids+"&iisoids="+iisoids+"&weblogicoids="+weblogicoids, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes")    	      
	
	}
	else{
		alert("请选择设备");
	}


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
	mainForm.action="<%=rootPath%>/servicecapreport.do?action=downloadselfservicechocereport";
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

}
</script>
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa" onload="initmenu();">
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
	
			<%=menuTable %>
	</td>
		<td bgcolor="#cedefa" align="center" >
			<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#397DBD cellPadding=0 rules=none align=center border=1 >
				<tr>
					<td height="28" align="left" bordercolor="#397DBD" bgcolor="#397DBD" class="txtGlobalBold">&nbsp;&nbsp;<font color=#ffffff>报表 >> 网络设备报表</font></td>
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
	&nbsp;&nbsp;<input type="button" name="doprocess" value="决策报表" onclick="query()">
				</td>
				</tr>
											
						
				<tr>
		
					<td height=300 bgcolor="#FFFFFF" valign="top" align=center>
						<table cellSpacing="1" cellPadding="0" width="100%" border="0">						

        						
							<tr>
								<td >
								
									 <table cellspacing="1" cellpadding="0" width="100%" >
										<tr height=35>
										 <td  align=center colspan="5"><strong>网络设备</strong></td>
										</tr>
										
									  <tr class="microsoftLook0" height=28>
									      
									    	<td width="10%">&nbsp;<INPUT type="checkbox" class=noborder name="netcheckall" onclick="javascript:chkall()">序号</td>
									        <td width="30%"><strong>IP地址</strong></td>
									        <td width="30%"><strong>设备名称</strong></td>
									    	<td width="30%"><strong>操作系统</strong></td>
									   </tr>
									<%
									    	HostNode node = null;
									  	java.text.SimpleDateFormat _sdf = new java.text.SimpleDateFormat("MM-dd HH:mm");
									  	for(int i=0;i<list.size();i++){
									  	node = (HostNode)list.get(i);
									%>
									
									 <tr bgcolor="DEEBF7" <%=onmouseoverstyle%> class="microsoftLook" height=25>
									
									    <td>&nbsp;&nbsp;<INPUT type="checkbox" class=noborder name=netcheckbox value="<%=node.getId()%>"><%=i+1%></td>
									       <td><%=node.getIpAddress()%>&nbsp;</td>
									       <td><%=node.getAlias()%>&nbsp;</td>
									      <td><%=node.getType()%></td>
									 </tr>
									 
									 
									 <%}
									
									 %>  
									 <tr height=35>
									 <td  align=center colspan="5"><strong>服务器</strong></td>
									</tr>
									<tr class="microsoftLook0" height=28>
									      
									    	<td width="10%">&nbsp;<INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()">序号</td>
									        <td width="30%"><strong>IP地址</strong></td>
									        <td width="30%"><strong>设备名称</strong></td>
									    	<td width="30%"><strong>操作系统</strong></td>
									   </tr>
									<%  //HostNode node = null;
									  	for(int i=0;i<listhost.size();i++){
									  	node = (HostNode)listhost.get(i);
									
									%>
									
									 <tr bgcolor="DEEBF7" <%=onmouseoverstyle%> class="microsoftLook" height=25>
									
									    <td>&nbsp;&nbsp;<INPUT type="checkbox" class=noborder name=hostcheckbox value="<%=node.getId()%>"><%=i+1%></td>
									       <td><%=node.getIpAddress()%>&nbsp;</td>
									       <td><%=node.getAlias()%>&nbsp;</td>
									      <td><%=node.getType()%></td>
									      
									 </tr>
									 <%}
									
									 %>  
									  <tr height=35>
									 <td  align=center colspan="5"><strong>数据库</strong></td>
									</tr>
									  <tr class="microsoftLook0" height=28>
									    	<td width="10%">&nbsp;<INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()">序号</td>
									        <td width="20%"><strong>IP地址</strong></td>
									        <td width="20%"><strong>数据库类型</strong></td>
									    	<td width="20%"><strong>数据库名称</strong></td>
									   </tr>
									<%
									    DBVo dbnode = null;
									  	for(int i=0;i<dblist.size();i++){
									  	dbnode = (DBVo)dblist.get(i);
									
									       DBTypeDao typedao = new DBTypeDao();
									       int dbtype= dbnode.getDbtype();
									       DBTypeVo typevo = (DBTypeVo)typedao.findByID(dbtype+"");
									%>
									
									 <tr bgcolor="DEEBF7" <%=onmouseoverstyle%> class="microsoftLook" height=25>
									
									    <td>&nbsp;&nbsp;<INPUT type="checkbox" class=noborder name=dbcheckbox value="<%=dbnode.getId()%>"><%=i+1%></td>
									       <td><%=dbnode.getIpAddress()%>&nbsp;</td>
									       <td ><%=typevo.getDbtype()%>&nbsp;</td>
									      <td><%=dbnode.getDbName()%></td>
									 </tr>
									 <%}
									
									 %> 
									  <tr height=35>
									 <td  align=center colspan="5"><strong>中间件</strong></td>
									</tr> 
									  <tr class="microsoftLook0" height=28>
									    	<td width="10%" >&nbsp;&nbsp;<INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()">序号</td>
									        <td width="15%" ><strong>名称</strong></td>
									        <td width="15%" ><strong>IP地址</strong></td>
									    	<td width="10%" ><strong>端口号</strong></td>
									   </tr>
									  
										<%
									  int rc = 0;
									  if(tomcatlist.size()!=0)
									  {
									  	rc = tomcatlist.size();
									  }
									     for(int i=0;i<rc;i++)
									    {
									       Tomcat vo = (Tomcat)tomcatlist.get(i);
									       Node node1 = PollingEngine.getInstance().getTomcatByID(vo.getId());
									       String alarmmessage = "";
									       int status = 0;
									       if(node1 != null){
									       		status = node1.getStatus();
									       		List alarmlist = node1.getAlarmMessage();
									       }       
									
									%>
									
									 <tr bgcolor="DEEBF7" <%=onmouseoverstyle%> class="microsoftLook" height=25>
									
									    <td>&nbsp;&nbsp;<INPUT type="checkbox" class=noborder name=tomcatcheckbox value="<%=vo.getId()%>"><%=i+1%></td>
									       <td><%=vo.getAlias()%>&nbsp;</td>
									       <td><%=vo.getIpAddress()%>&nbsp;</td>
									      <td><%=vo.getPort()%></td>
									 </tr>
									 <%}
									 if(iislist.size() > 0){
										    for(int i=0;i<iislist.size();i++)
										    {
										       IISConfig voiis = (IISConfig)iislist.get(i);
										 
										 %> 
										  <tr bgcolor="DEEBF7" <%=onmouseoverstyle%> class="microsoftLook" height=25>
										 <td>&nbsp;&nbsp;<INPUT type="checkbox" class=noborder name=iischeckbox value="<%=voiis.getId()%>"><%=i+1+rc%></td>
										   <td ><%=voiis.getName()%></td> 
											<td ><%=voiis.getIpaddress()%></td> 
										   <td ><%=""%></td> 
										    </tr>
							 <%  
									}
							        
							         }
							         if(weblogiclist.size() > 0){
							         int iissize = 0;
							         if(iislist.size()>0)
							         {
							         	iissize = iislist.size();
							         }
							         
									    for(int i=0;i<weblogiclist.size();i++)
									    {
									       WeblogicConfig weblogicvo = (WeblogicConfig)weblogiclist.get(i);
									       Node weblogicnode = PollingEngine.getInstance().getWeblogicByID(weblogicvo.getId());
									 %>  
									  <tr bgcolor="DEEBF7" <%=onmouseoverstyle%> class="microsoftLook" height=25>
										 <td>&nbsp;&nbsp;<INPUT type="checkbox" class=noborder name=weblogiccheckbox value="<%=weblogicvo.getId()%>"><%=i+1+rc+iissize%></td>
										   <td ><%=weblogicvo.getAlias()%></td> 
											<td ><%=weblogicvo.getIpAddress()%></td> 
										   <td ><%=""%></td> 
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
</form>
</BODY>
</HTML>
