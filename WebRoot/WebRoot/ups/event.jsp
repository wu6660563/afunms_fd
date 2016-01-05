<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.common.util.ShareData"%> 
<%@page import="com.afunms.security.model.MgeUps"%>
<%@page import="com.afunms.polling.node.UPSNode"%>
<%@page import="com.afunms.polling.om.Systemcollectdata"%>
<%@page import="java.util.*"%>   
<%@page import="com.afunms.inform.util.SystemSnap"%>
<%@page import="java.text.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.polling.impl.*"%>
<%@page import="com.afunms.polling.api.*"%>
<%@ page import="com.afunms.event.model.EventList"%>   
<%
	 String rootPath = request.getContextPath();
	 String menuTable = (String)request.getAttribute("menuTable");
     MgeUps vo  = (MgeUps)request.getAttribute("vo");
     String startdate = (String)request.getAttribute("startdate");
     String todate = (String)request.getAttribute("todate");
     UPSNode upsnode = (UPSNode)PollingEngine.getInstance().getUpsByID(vo.getId());
     Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(vo.getIpAddress());
     Vector batteryVector = (Vector)ipAllData.get("battery");
     String DCDY = "";
     String DCDL = "";
     String DCSYHBSJ = "";
     String DCWD = "";
     String HJWD = "";
     String sysName = "";
     String SBMS = "";
     if(batteryVector!=null){
         for(int i=0;i<batteryVector.size();i++){
             Systemcollectdata systemdata = (Systemcollectdata)batteryVector.get(i);
             if("DCDY".equalsIgnoreCase(systemdata.getEntity())){
                 DCDY = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("DCDL".equalsIgnoreCase(systemdata.getEntity())){
                 DCDL = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("DCSYHBSJ".equalsIgnoreCase(systemdata.getEntity())){
                 DCSYHBSJ = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("DCWD".equalsIgnoreCase(systemdata.getEntity())){
                 DCWD = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("HJWD".equalsIgnoreCase(systemdata.getEntity())){
                 HJWD = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("SBMC".equalsIgnoreCase(systemdata.getEntity())){
                 sysName = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("SBMS".equalsIgnoreCase(systemdata.getEntity())){
                 SBMS = systemdata.getThevalue()+systemdata.getUnit();
             }
         }
     }
     Vector systemVector = (Vector)ipAllData.get("system");
     String sysDescr = "";
     String sysUpTime = "";
     if(systemVector!=null){
         for(int i=0;i<systemVector.size();i++){
             Systemcollectdata systemdata = (Systemcollectdata)systemVector.get(i);
             if("sysDescr".equalsIgnoreCase(systemdata.getSubentity())){
                 sysDescr = systemdata.getThevalue();
             }
             if("sysUpTime".equalsIgnoreCase(systemdata.getSubentity())){
                 sysUpTime = systemdata.getThevalue();
             }
         }
     }
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String time1 = sdf.format(new Date());
								
	String pingconavg ="0";
	String starttime1 = time1 + " 00:00:00";
	String totime1 = time1 + " 23:59:59";
		
	Hashtable ConnectUtilizationhash = new Hashtable();
	I_HostCollectData hostmanager=new HostCollectDataManager();
	try{
		ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"Ping","ConnectUtilization",starttime1,totime1);
	}catch(Exception ex){
		ex.printStackTrace();
	}
		
	if (ConnectUtilizationhash.get("avgpingcon")!=null){
		pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
		pingconavg = pingconavg.replace("%", "");
	}
	request.setAttribute("pingconavg", new Double(pingconavg));
	double avgpingcon = (Double)request.getAttribute("pingconavg");
    int percent1 = Double.valueOf(avgpingcon).intValue();
	int percent2 = 100-percent1;
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
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>
<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="gb2312" />
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="gb2312"/>
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 

<script language="javascript">	
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
	document.getElementById('upsDetailTitle-2').className='detail-data-title';
	document.getElementById('upsDetailTitle-2').onmouseover="this.className='detail-data-title'";
	document.getElementById('upsDetailTitle-2').onmouseout="this.className='detail-data-title'";
}
function refer(action){
		document.getElementById("id").value="<%=vo.getId()%>";
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}
  function query()
  {  
     mainForm.action = "<%=rootPath%>/ups.do?action=event";
     mainForm.submit();
  }
</script>
</head>
<body id="body" class="body" onload="initmenu();">
<form method="post" name="mainForm">
<input type=hidden name="orderflag">
<input type=hidden name="id" value="<%=vo.getId()%>">
<div id="loading">
	<div class="loading-indicator">
	<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif" width="32" height="32" style="margin-right: 8px;" align="middle" />Loading...</div>
</div>
<div id="loading-mask" style=""></div>
<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 189px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=960>
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
							<td class="td-container-main-application-detail">
								<table id="container-main-application-detail" class="container-main-application-detail">
									<tr>
										<td>
											<table id="application-detail-content" class="application-detail-content">
												<tr>
													<td>
														<table id="application-detail-content-header" class="application-detail-content-header">
										                	<tr>
											                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
											                	<td><b>UPS设备监视 >> 详细信息</b></td>
											                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
											       			</tr>
											        	</table>
				        							</td>
				        						</tr>
				        						<tr>
				        							<td>
				        								<table id="application-detail-content-body" class="application-detail-content-body">
				        									<tr>
				        										<td>
				        										
				        											<table>
										               					<tr>
										     								<td width="70%" align="left" valign="top">
			        								  <table>
                    										<tr>
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;名称:</td>
                      											<td width="35%"><%=vo.getAlias()%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;IP地址:</td>
                      											<td width="35%"><%=vo.getIpAddress()%> </td>
                    										</tr>
                    										<tr bgcolor="#ECECEC">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;系统描述:</td>
                      											<td width="35%" colspan=3><%=sysDescr%> </td>	
                    										</tr>
                    										<tr>
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;运行时间:</td>
                      											<td width="35%"><%=sysUpTime%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;设备名称:</td>
                      											<td width="35%"><%=sysName%> </td>
                    										</tr>
                    										<tr bgcolor="#ECECEC">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;环境温度:</td>
                      											<td width="35%"><%=HJWD%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;设备描述:</td>
                      											<td width="35%"><%=SBMS%> </td>
                    										</tr>
                    										<tr >
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;当前状态:</td>
                      											<td width="35%"><img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(SystemSnap.getUpsStatus(upsnode.getId()+""))%>">&nbsp;<%=NodeHelper.getStatusDescr(SystemSnap.getUpsStatus(upsnode.getId()+""))%> </td>
                    											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;管理状态:</td>
																<%if("1".equals(vo.getIsmanaged())){%>
                      											<td width="35%">已监视</td>
																<%}else{%>
																<td width="35%">未监视</td>
																<%}%>
                    										</tr> 
                    										<tr bgcolor="#ECECEC">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;电池电压:</td>
                      											<td width="35%"><%=DCDY%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;电池电流:</td>
                      											<td width="35%"><%=DCDL%> </td>
                    										</tr>
                    										<tr>
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;电池剩余后备时间:</td>
                      											<td width="35%"><%=DCSYHBSJ%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;电池温度:</td>
                      											<td width="35%"><%=DCWD%> </td>
                    										</tr>
                									</table>
                      
										        </td>
												<td width="20%" align="center" valign="middle" class=dashLeft>

											<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none w align=center border=0 algin="center">
                        								
                          									<tr>
                											<td width="400" align="left" valign="middle" class=dashLeft>
                												<table  style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa  cellpadding=0 rules=none width=80% align=center border=0 algin="center" >
                    													<tr class="topNameRight">
                      														<td height="30" align="center">
                      														
                      															<div id="flashcontent">
																						<strong>You need to upgrade your Flash Player</strong>
																							</div>
																							<script type="text/javascript">
																							var so = new SWFObject("<%=rootPath%>/flex/Pie_Component.swf?percent1=<%=percent1%>&percentStr1=可用&percent2=<%=percent2%>&percentStr2=不可用", "Pie_Component", "160", "160", "8", "#ffffff");
																							so.write("flashcontent");
																							</script>
                      														</td>
                      														
                    													</tr>
                    													<tr>
										           														<td height="7" align=center><img src="<%=rootPath%>/resource/image/Loading_2.gif"></td>
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
                						
                				                <tr>
				        							<td>
				        								<table id="application-detail-content-footer" class="application-detail-content-footer">
				        									<tr>
				        										<td>
				        											<table width="100%" border="0" cellspacing="0" cellpadding="0">
											                  			<tr>
											                    			<td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
											                    			<td></td>
											                    			<td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
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
									<tr>
										<td>
											<table id="application-detail-data" class="application-detail-data">
											    <tr>
											    	<td class="detail-data-header">
											    		<%=upsDetailTitleTable%>
											    	</td>
											    </tr>
												<tr>
											    	<td>
													<table class="detail-data-body">
												      		  <tr>
                                                                <td align=left height="28" bgcolor="#ECECEC">
																
     开始日期
		<input type="text" name="startdate" value="<%=startdate%>" size="10">
	<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
	<img id=imageCalendar1 align=absmiddle width=34 height=21 src=<%=rootPath%>/include/calendar/button.gif border=0></a>
	
		截止日期
		<input type="text" name="todate" value="<%=todate%>" size="10"/>
	<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
	<img id=imageCalendar2 align=absmiddle width=34 height=21 src=<%=rootPath%>/include/calendar/button.gif border=0></a>
		事件等级
		<select name="level1">
		<option value="99">不限</option>
		<option value="1" <%=level1str%>>普通事件</option>
		<option value="2" <%=level2str%>>严重事件</option>
		<option value="3" <%=level3str%>>紧急事件</option>
		</select>
					
		处理状态
		<select name="status">
		<option value="99">不限</option>
		<option value="0" <%=status0str%>>未处理</option>
		<option value="1" <%=status1str%>>正在处理</option>
		<option value="2" <%=status2str%>>已处理</option>
		</select>	
	<input type="button" name="submitss" value="查询" onclick="query()">
						</td>
						</tr> 
                    <tr> 
                      <td>
  <table cellSpacing="0"  cellPadding="0" border=0 height=28>
	
  <tr bgcolor="#ECECEC" height=28>
    	<td class="detail-data-body-title" align="center">&nbsp;</td>
        <td width="10%" class="detail-data-body-title" align="center"><strong>事件等级</strong></td>
    	<td width="40%" class="detail-data-body-title" align="center"><strong>事件描述</strong></td>
		<td class="detail-data-body-title" align="center"><strong>登记日期</strong></td>
    	<td class="detail-data-body-title" align="center"><strong>登记人</strong></td>
    	<td class="detail-data-body-title" align="center"><strong>处理状态</strong></td>
    	<td class="detail-data-body-title" align="center"><strong>操作</strong></td>
   </tr>
<%
	int index = 0;
  	java.text.SimpleDateFormat _sdf = new java.text.SimpleDateFormat("MM-dd HH:mm");
  	List list = (List)request.getAttribute("list");
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
  	if("0".equals(level)){
  		showlevel="提示信息";
  	}
  	if("1".equals(level)){
  		showlevel="普通事件";
  	}
  	if("2".equals(level)){
  		showlevel="紧急事件";
  	}
  	if("3".equals(level)){
  		showlevel="严重事件";
  	}  	
   	  	if("0".equals(status)){
  		status = "未处理";
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

 <tr  bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=28>

    <td class="detail-data-body-list" align="center">&nbsp;<%=index%></td>
    <%
    	if("3".equals(level)){
    %>
      <td style="border-left:1px solid #EEEEEE;border-bottom:1px solid #EEEEEE;" bgcolor=red align=center><%=showlevel%>&nbsp;</td>
       <%
       }else if("2".equals(level)){
       %>
       <td style="border-left:1px solid #EEEEEE;border-bottom:1px solid #EEEEEE;" bgcolor=orange align=center><%=showlevel%>&nbsp;</td>
       <%
       }else if("0".equals(level)){
       %>
       <td style="border-left:1px solid #EEEEEE;border-bottom:1px solid #EEEEEE;" bgcolor=blue align=center><%=showlevel%>&nbsp;</td>
       <%
       }else{
       %>
       <td style="border-left:1px solid #EEEEEE;border-bottom:1px solid #EEEEEE;" bgcolor=yellow align=center><%=showlevel%>&nbsp;</td>
       <%
       }
       %>      
      <td class="detail-data-body-list" align="center">
      <%=content%></td>
       <td class="detail-data-body-list" align="center">
      <%=rtime1%></td>
       <td class="detail-data-body-list" align="center">
      <%=rptman%></td>
       <td class="detail-data-body-list" align="center">
      <%=status%></td>
       <td class="detail-data-body-list" align="center">
       <%
       		if("0".equals(s)){
       		%>
       			<input type ="button" value="接受处理" class="button" onclick='window.open("<%=rootPath%>/alarm/event/accitevent.jsp?eventid=<%=eventid%>","accEventWindow", "toolbar=no,height=400, width= 500, top=200, left= 200,scrollbars=no"+"screenX=0,screenY=0")'>
       		<%
       		}
       		if("1".equals(s)){
       		%>
       			<input type ="button" value="填写报告" class="button" onclick='window.open("<%=rootPath%>/alarm/event/accitevent.jsp?eventid=<%=eventid%>","accEventWindow", "toolbar=no,height=400, width= 700, top=200, left= 200,scrollbars=no"+"screenX=0,screenY=0")'>
       		<%
       		}
       		if("2".equals(s)){
       		%>
       			<input type ="button" value="查看报告" class="button" onclick='window.open("<%=rootPath%>/alarm/event/accitevent.jsp?eventid=<%=eventid%>","accEventWindow", "toolbar=no,height=400, width= 700, top=200, left= 200,scrollbars=no"+"screenX=0,screenY=0")'>
       		<%
       		}
       %>
       </td>
 </tr>
 <%}

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
</form>
<script>
			
Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	    Ext.get("process").on("click",function(){
        Ext.MessageBox.wait('数据加载中，请稍后.. ');   
        mainForm.action = "<%=rootPath%>/ups.do?action=toDetail";
        mainForm.submit();
 });    
});
</script>
</BODY>
</HTML>