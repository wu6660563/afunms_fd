<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.event.model.EventList"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.topology.model.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="com.afunms.application.model.DBVo"%>
<%@page import="com.afunms.application.model.DBTypeVo"%>
<%@page import="com.afunms.application.dao.DBTypeDao"%>
<%@page import="com.afunms.application.model.Tomcat"%>
<%@page import="com.afunms.polling.base.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.application.model.IISConfig"%>
<%@page import="com.afunms.application.model.*"%>
<%
  	String rootPath = request.getContextPath();
   
    List tomcatlist = (List)request.getAttribute("tomcatlist");
   	List iislist = (List)request.getAttribute("iislist");
   	List weblogiclist = (List)request.getAttribute("weblogiclist");
	String startdate = (String)request.getAttribute("startdate");
	String todate = (String)request.getAttribute("todate");
	String menuTable = (String)request.getAttribute("menuTable");
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 

<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8"/>
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>



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
		alert("��ѡ���豸");
	}


	//mainForm.action="<%=rootPath%>/netreport.do?action=netevent";
	//mainForm.submit();
} 
  function doQuery()
  {  
     if(mainForm.key.value=="")
     {
     	alert("�������ѯ����");
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
     
       // Ext.MessageBox.wait('���ݼ����У����Ժ�.. '); 
        //msg.style.display="block";
	mainForm.action="<%=rootPath%>/midcapreport.do?action=midping";
	mainForm.submit();        
        //mainForm.action = "<%=rootPath%>/network.do?action=add";
        //mainForm.submit();
     //}  
       // mainForm.submit();
 });	
});

	

</script>
<script language="JavaScript" type="text/JavaScript">
function chkall(){
				var checkall = document.getElementById("checkall");
				var checkboxestomcat = document.getElementsByName("tomcatcheckbox");
				for(var i = 0 ; i < checkboxestomcat.length; i++){
					var checkbox = checkboxestomcat[i];
					checkbox.checked = checkall.checked;
				}
				
				var checkboxesiis = document.getElementsByName("iischeckbox");
				for(var i = 0 ; i < checkboxesiis.length; i++){
					var checkbox = checkboxesiis[i];
					checkbox.checked = checkall.checked;
				}
				
				var checkboxesweblogic = document.getElementsByName("weblogiccheckbox");
				for(var i = 0 ; i < checkboxesweblogic.length; i++){
					var checkbox = checkboxesweblogic[i];
					checkbox.checked = checkall.checked;
				}
			}

</script>



<script language="JavaScript" type="text/JavaScript">
var show = true;
var hide = false;
//�޸Ĳ˵������¼�ͷ����
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
//��Ӳ˵�	
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
	document.getElementById('middlewareReportTitle-0').className='detail-data-title';
	document.getElementById('middlewareReportTitle-0').onmouseover="this.className='detail-data-title'";
	document.getElementById('middlewareReportTitle-0').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}

function CreateWindow(url)
{
	
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=400,height=400,directories=no,status=no,scrollbars=no,menubar=no")
}  
function setBid(eventTextId , eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/business.do?action=setBidbyuser&event='+event.id+'&value='+event.value + '&eventText=' + eventTextId);
}
function query()
  {  
     mainForm.action = "<%=rootPath%>/midcapreport.do?action=find&dbflag=1";
     mainForm.submit();
  }
</script>


</head>
<body id="body" class="body" onload="initmenu();">
	<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
	<form id="mainForm" method="post" name="mainForm">
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
														    		<%=middlewareReportTitleSB%>
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
																						<td class="report-data-body-title" style="text-align: left">&nbsp;&nbsp;&nbsp;
																							��ʼ����
																								<input type="text" name="startdate" value="<%=startdate%>" size="10">
																								<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
																								<img id=imageCalendar1 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a>
																							
																								��ֹ����
																								<input type="text" name="todate" value="<%=todate%>" size="10"/>
																								<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
																								&nbsp;<img id=imageCalendar2  width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a> ����ҵ��<input type=text readonly="readonly" onclick='setBid("bidtext" , "bid");' id="bidtext" name="bidtext" size="25" maxlength="25" value="">
																								<input type="hidden" id="bid" name="bid" value="">
																								<input type="button" name="submitss" value="��  ѯ" onclick="query()">
																								&nbsp;&nbsp;<input type="button" name="process" value="���ɱ���" onclick="#">
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
													        			<tr height=28>
																	    	<td width="10%" class="report-data-body-title">&nbsp;&nbsp;<INPUT type="checkbox" class=noborder name="checkall" id = "checkall" onclick="javascript:chkall()">���</td>
																	        <td width="15%" class="report-data-body-title"><strong>����</strong></td>
																	        <td width="15%" class="report-data-body-title"><strong>IP��ַ</strong></td>
																	    	<td width="10%" class="report-data-body-title"><strong>�˿ں�</strong></td>
																		</tr>
																		<%
																			int rc = 0;
																			if(tomcatlist!=null && tomcatlist.size()!=0){
																			  	rc = tomcatlist.size();
																			}
									     									for(int i=0;i<rc;i++){
																		    	Tomcat vo = (Tomcat)tomcatlist.get(i);
																		       	Node node1 = PollingEngine.getInstance().getTomcatByID(vo.getId());
																		       	String alarmmessage = "";
																		       	int status = 0;
																		       	if(node1 != null){
																		       		status = node1.getStatus();
																		       		List alarmlist = node1.getAlarmMessage();
																		       	}       
									
																		%>
									 									<tr <%=onmouseoverstyle%> height=25>
									    									<td class="report-data-body-list">&nbsp;&nbsp;<INPUT type="checkbox" class=noborder name=tomcatcheckbox value="<%=vo.getId()%>"><%=i+1%></td>
									       									<td class="report-data-body-list"><%=vo.getAlias()%>&nbsp;</td>
									       									<td class="report-data-body-list"><%=vo.getIpAddress()%>&nbsp;</td>
																		    <td class="report-data-body-list"><%=vo.getPort()%></td>
																		</tr>
									 									<%
									 										}
									 										int iissize = 0;
																         	if(iislist!=null && iislist.size() > 0){
																         		iissize = iislist.size();
																         	}
																		    for(int i=0;i<iissize;i++){
																		       IISConfig voiis = (IISConfig)iislist.get(i);
																		 %> 
										  								<tr <%=onmouseoverstyle%> height=25>
										 									<td class="report-data-body-list">&nbsp;&nbsp;<INPUT type="checkbox" class=noborder name=iischeckbox value="<%=voiis.getId()%>"><%=i+1+rc%></td>
										   									<td class="report-data-body-list"><%=voiis.getName()%></td> 
																			<td class="report-data-body-list"><%=voiis.getIpaddress()%></td> 
										   									<td class="report-data-body-list"><%=""%></td> 
										    							</tr>
							 											<%  
																			}
							         										if(weblogiclist!=null && weblogiclist.size() > 0){
							         											for(int i=0;i<weblogiclist.size();i++){
																		       		WeblogicConfig weblogicvo = (WeblogicConfig)weblogiclist.get(i);
																		       		Node weblogicnode = PollingEngine.getInstance().getWeblogicByID(weblogicvo.getId());
																		 %>  
									  									<tr <%=onmouseoverstyle%> height=25>
										 									<td class="report-data-body-list">&nbsp;&nbsp;<INPUT type="checkbox" class=noborder name=weblogiccheckbox value="<%=weblogicvo.getId()%>"><%=i+1+rc+iissize%></td>
										   									<td class="report-data-body-list"><%=weblogicvo.getAlias()%></td> 
																			<td class="report-data-body-list"><%=weblogicvo.getIpAddress()%></td> 
										   									<td class="report-data-body-list"><%=""%></td> 
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
				</td>
			</tr>
		</table>
		
	</form>
</BODY>
</HTML>