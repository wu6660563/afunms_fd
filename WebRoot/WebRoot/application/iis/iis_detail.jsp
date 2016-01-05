<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.system.model.TimeShareConfig"%>
<%@page import="com.afunms.detail.service.IISInfo.IISInfoService"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.monitor.item.*"%>
<%@page import="com.afunms.polling.node.*" %>
<%@page import="com.afunms.polling.om.*" %>
<%@page import="com.afunms.monitor.item.base.MonitorResult"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.monitor.item.base.MoidConstants"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@page import="com.afunms.application.util.*"%>
<%@page import="org.jdom.Element"%>
<%@page import="com.afunms.common.util.*"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.application.manage.IISManager"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.lang.*"%>
<%
	String runmodel = PollingEngine.getCollectwebflag(); 
   String rootPath = request.getContextPath(); 
   String timeFormat = "yyyy-MM-dd";
   String from_date1 = (String)request.getAttribute("starttime1");
   String to_date1 = (String)request.getAttribute("totime1");
   String flag = (String)request.getAttribute("flag");
  String tmp = request.getParameter("id");
  double jvm_memoryuiltillize=0;
  double iisping=0;

  String lasttime ;
  String nexttime;
	String totalBytesSentHighWord= "";
	String totalBytesSentLowWord= "";
	String totalBytesReceivedHighWord= "";
	String totalBytesReceivedLowWord = "";
	
	String totalFilesSent = "";
	String totalFilesReceived = "";
	String currentAnonymousUsers = "";
	String totalAnonymousUsers = "";
	
	String maxAnonymousUsers = "";
	String currentConnections = "";
	String maxConnections = "";
	String connectionAttempts = "";
	
	String logonAttempts = "";
	String totalGets = "";
	String totalPosts = "";
	String totalNotFoundErrors = "";

  List data_list=new ArrayList();
  
  Hashtable imgurlhash=new Hashtable();
  
  IISManager tm= new IISManager();

   
  IIS iis = (IIS)PollingEngine.getInstance().getIisByID(Integer.parseInt(tmp)); 
  Hashtable iisvalues = null; 
  String pingvalue ="0";
  	Hashtable hashPing=new Hashtable();
   if("0".equals(runmodel)){
   		//采集与访问是集成模式
  		iisvalues = ShareData.getIisdata();
 		if(iisvalues != null && iisvalues.size()>0){
	  		data_list = (List)iisvalues.get(iis.getIpAddress());
	  	}
	  	
	  	Hashtable allissdata = (Hashtable)ShareData.getIISPingdata();
		if(allissdata != null && allissdata.size()>0){
			Vector vec = (Vector)allissdata.get(iis.getIpAddress());
			if(vec != null && vec.size()>0){
				Pingcollectdata iispingdata = (Pingcollectdata)vec.get(0);
				if(iispingdata != null){
					pingvalue = iispingdata.getThevalue();
				}
			}
		}	
  }else{
  		//采集与访问是分离模式
  		IISInfoService iisInfoService = new IISInfoService();
  		data_list = iisInfoService.getIISData(iis.getId()+"");
  		pingvalue = iisInfoService.getIISPing(iis.getIpAddress());
  }
  
 
	imgurlhash =(Hashtable) request.getAttribute("imgurlhash");
	//ping连通率
	String image_ping =(String)imgurlhash.get("IISPing").toString();
   
	iisping=(double)tm.iisping(iis.getId());
	
	int percent1 = Double.valueOf(iisping).intValue();
	int percent2 = 100-percent1;
	
	 
	 Hashtable pollingtime_ht=tm.getCollecttime(iis.getIpAddress());
	 if(pollingtime_ht!=null){
	 lasttime=(String)pollingtime_ht.get("lasttime");
	 nexttime=(String)pollingtime_ht.get("nexttime");
	 
	 }else{
	 	lasttime=null;
	 	nexttime=null;	 
	 }
	 
	if(data_list!=null && data_list.size()>0){
		
		IISVo iisvo = (IISVo)data_list.get(0);
		totalBytesSentHighWord= iisvo.getTotalBytesSentHighWord();
		if(totalBytesSentHighWord == null)totalBytesSentHighWord="";
		totalBytesSentLowWord= iisvo.getTotalBytesSentLowWord();
		if(totalBytesSentLowWord == null)totalBytesSentLowWord="";
		totalBytesReceivedHighWord= iisvo.getTotalBytesReceivedHighWord();
		if(totalBytesReceivedHighWord == null)totalBytesReceivedHighWord="";
		totalBytesReceivedLowWord = iisvo.getTotalBytesReceivedLowWord();
		if(totalBytesReceivedLowWord == null)totalBytesReceivedLowWord="";
	
		totalFilesSent = iisvo.getTotalFilesSent();
		if(totalFilesSent == null)totalFilesSent="";
		totalFilesReceived = iisvo.getTotalFilesReceived();
		if(totalFilesReceived == null)totalFilesReceived = "";
		currentAnonymousUsers = iisvo.getCurrentAnonymousUsers();
		if(currentAnonymousUsers == null)currentAnonymousUsers = "";
		totalAnonymousUsers = iisvo.getTotalAnonymousUsers();
		if(totalAnonymousUsers == null)totalAnonymousUsers = "";
		
	
		maxAnonymousUsers = iisvo.getMaxAnonymousUsers();
		if(maxAnonymousUsers == null)maxAnonymousUsers = "";
		currentConnections = iisvo.getCurrentConnections();
		if(currentConnections == null)currentConnections = "";
		maxConnections = iisvo.getMaxConnections();
		if(maxConnections == null)maxConnections = "";
		connectionAttempts = iisvo.getConnectionAttempts();
		if(connectionAttempts == null)connectionAttempts = "";
	
		logonAttempts = iisvo.getLogonAttempts();
		if(logonAttempts == null)logonAttempts = "";
		totalGets = iisvo.getTotalGets();
		if(totalGets == null)totalGets = "";
		totalPosts = iisvo.getTotalPosts();
		if(totalPosts == null)totalPosts = "";
		totalNotFoundErrors = iisvo.getTotalNotFoundErrors();
		if(totalNotFoundErrors == null)totalNotFoundErrors = "";
	
	}	
	String chart1 = null,chart2 = null,chart3 = null,responseTime = null;  

	DefaultPieDataset dpd = new DefaultPieDataset();
	dpd.setValue("可用时间",iisping);
	dpd.setValue("不可用时间",100 - iisping);
	chart1 = ChartCreator.createPieChart(dpd,"",120,120); 
	int status=0;
	status = iis.getStatus();
	//amcharts 生成连通率图形
    StringBuffer dataStr1 = new StringBuffer();
	dataStr1.append("连通;").append(Math.round(percent1)).append(";false;7CFC00\\n");
	dataStr1.append("未连通;").append(100-Math.round(percent1)).append(";false;FF0000\\n");
	String realdata = dataStr1.toString();
	
	StringBuffer dataStr2 = new StringBuffer();
	dataStr2.append("连通;").append(Math.round(Float.parseFloat(pingvalue))).append(";false;7CFC00\\n");
	dataStr2.append("未连通;").append(100-Math.round(Float.parseFloat(pingvalue))).append(";false;FF0000\\n");
	String avgdata = dataStr2.toString();
%>
<% String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="gb2312" />
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="gb2312"></script>
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 

<script language="JavaScript" type="text/javascript">

	
function CreateWindow(url)
{
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}    
function setReceiver(eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/user.do?action=setReceiver&event='+event.id+'&value='+event.value);
}

function show_graph(){
      mainForm.action = "<%=rootPath%>/iis.do?action=detail";
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
	timeShareConfiginit(); // nielin add for time-sharing at 2010-01-04
}

</script>

<script>
//-- nielin add for timeShareConfig 2010-01-03 end-------------------------------------------------------------
	/*
	* 此方法用于短信分时详细信息
	* 需引入 /application/resource/js/timeShareConfigdiv.js 
	*/
	//接受用户的列表
	var action = "<%=rootPath%>/user.do?action=setReceiver";
	// 获取短信分时详细信息的div
	function timeShareConfiginit(){
		var rowNum = document.getElementById("rowNum");
		rowNum.value = "0";
		// 获取设备或服务的分时数据列表,
		var timeShareConfigs = new Array();
		var smsConfigs = new Array();
		var phoneConfigs = new Array();
		<%	
			List timeShareConfigList = (List) request.getAttribute("timeShareConfigList");
			if(timeShareConfigList!=null&&timeShareConfigList.size()>=0){
			for(int i = 0 ; i < timeShareConfigList.size(); i++){	        
	            TimeShareConfig timeShareConfig = (TimeShareConfig) timeShareConfigList.get(i);
	            int timeShareConfigId = timeShareConfig.getId();
	            String timeShareType = timeShareConfig.getTimeShareType();
	            String timeShareConfigbeginTime = timeShareConfig.getBeginTime();
	            String timeShareConfigendTime = timeShareConfig.getEndTime();
	            String timeShareConfiguserIds = timeShareConfig.getUserIds();
	            
	    %>
	            timeShareConfigs.push({
	                timeShareConfigId:"<%=timeShareConfigId%>",
	                timeShareType:"<%=timeShareType%>",
	                beginTime:"<%=timeShareConfigbeginTime%>",
	                endTime:"<%=timeShareConfigendTime%>",
	                userIds:"<%=timeShareConfiguserIds%>"
	            });
	    <%
	        }
	        }
	    %>   
	    for(var i = 0; i< timeShareConfigs.length; i++){
	    	var item = timeShareConfigs[i];
	    	if(item.timeShareType=="sms"){
	    		smsConfigs.push({
	                timeShareConfigId:item.timeShareConfigId,
	                timeShareType:item.timeShareType,
	                beginTime:item.beginTime,
	                endTime:item.endTime,
	                userIds:item.userIds
	            });
	    	}
	    	if(item.timeShareType=="phone"){
	    		phoneConfigs.push({
	                timeShareConfigId:item.timeShareConfigId,
	                timeShareType:item.timeShareType,
	                beginTime:item.beginTime,
	                endTime:item.endTime,
	                userIds:item.userIds
	            });
	    	}
	    }
		timeShareConfig("smsConfigTable",smsConfigs);
		timeShareConfig("phoneConfigTable",phoneConfigs);
	}
</script>


</head>
<body id="body" class="body" onload="initmenu();">
<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>

	<form id="mainForm" method="post" name="mainForm">
		<input type=hidden id="flag" name="flag" value="<%=flag%>">
		
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
							<td class="td-container-main-add">
								<table id="container-main-add" class="container-main-add">
									<tr>
										<td>
											<table id="add-content" class="add-content">
												<tr>
													<td>
														<table id="add-content-header" class="add-content-header">
										                	<tr>
											                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
											                	<td class="add-content-title">应用 >> 中间件管理 >> IIS服务监视</td>
											                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
											       			</tr>
											        	</table>
				        							</td>
				        						</tr>
				        						<tr>
				        							<td>
				        								<table id="detail-content-body" class="detail-content-body">
				        									<tr>
				        										<td>
				        										
	                                											<table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%">
																	<TBODY>
																 		<tr>
																 			<td colspan=2>
	
		服务名称
		<select name="id" >
		<%
			IISConfigDao dao = new IISConfigDao();	
  			List iis_list = dao.getAllIIS();
			if(iis_list != null && iis_list.size()>0){
				for(int i=0;i<iis_list.size();i++){
					IISConfig iisconfig = (IISConfig)iis_list.get(i);
		%>
		
		<option value="<%=iisconfig.getId()%>"> <%=iisconfig.getName()%></option>
		
		<%
				}
			}
		%>
		</select>
		显示从
		<input type="text" name="from_date1" value="<%=from_date1 %>" size="10"/>
	<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].from_date1,null,0,330)">
	<img id=imageCalendar1 align=absmiddle width=34 height=21 src=<%=rootPath%>/include/calendar/button.gif border=0></a>
			<select name="from_hour" >
		<%for(int i=0;i<=23;i++){
		  String id = new Integer(i).toString();%>
		<option value="<%=id%>"><%=i%></option>
		<%}%>
		</select>小时	
		到
		<input type="text" name="to_date1" value="<%=to_date1 %>" size="10"/>
	<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].to_date1,null,0,330)">
	<img id=imageCalendar2 align=absmiddle width=34 height=21 src=<%=rootPath%>/include/calendar/button.gif border=0></a>
	<select name="to_hour" >
		<%for(int i=0;i<=23;i++){
		  String id = new Integer(i).toString();%>
		<option value="<%=id%>"><%=i%></option>
		<%}%>
		</select>	
	小时内的数据图形
	<input type="button" onclick="show_graph()"  value="查询">
	</td>
	
	</tr>
														<tr>
                								<td width="80%"  align="left" valign="top" class=dashLeft>
                								<table>
                								<tr>
                								<td>
                									<table>
                   <tr bgcolor="#F1F1F1">
                      <td width="30%" height="26" valign=center align="left" nowrap  class=txtGlobal>&nbsp;名称:</td>
                      <td width="70%">
                      <%if(iis.getAlias()!=null){ %>
                      <%=iis.getAlias()%>
                      <%} %>
                      </td>
                    </tr>
                    <tr>
                      <td height="26" class=txtGlobal align="left" valign=center nowrap>&nbsp;状态:</td>
                      <td><img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(status)%>">&nbsp;<%=NodeHelper.getStatusDescr(status)%></td>
                    </tr>
                    <tr bgcolor="#F1F1F1">
                      <td width="30%" height="26" align=left valign=center nowrap   class=txtGlobal>&nbsp;IP地址:</td>
                      <td width="70%">
                      <%if(iis.getIpAddress()!=null)
                      { %>
                      <%=iis.getIpAddress()%>
                      <%} %>
                      </td>
                    </tr>
                    <tr>
                      <td width="30%" height="26" align=left valign=center nowrap class=txtGlobal>&nbsp;上一次轮询:</td>
                      <td width="70%">
                      <%if(lasttime!=null)
                      { %>
                      <%=lasttime%>
                      <%} %>
                      </td>
                    </tr>
                    <tr bgcolor="#F1F1F1">
                      <td height="26" class=txtGlobal valign=center nowrap>&nbsp;下一次轮询:</td>
                      <td>
                      <%if(nexttime!=null){ %>
                      <%=nexttime%>
                      <%} %>
                      </td>
                    </tr>
              </table>
                				             
										</td>	
										
										          									
            								</tr>	
										
													
									                                  					  
                  					
              <tr>
                    <td width="81%" align="left"  class=dashLeft>		
                  			<table style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa  cellpadding=0 rules=none width=100% align=center border=1 algin="center">
                    				<tr bgcolor="#F1F1F1">
                      					<td width="30%" height="26" align=right valign=right nowrap class=txtGlobal>&nbsp;发送的总字节数中的高32 位:</td>
                      					<td width="20%">&nbsp;&nbsp;
                      				     <%if(totalBytesSentHighWord!=""){ %>
                      					<%=totalBytesSentHighWord%>
                      					<%} %>
                      					</td>
                      					<td width="30%" height="26" class=txtGlobal align=right nowrap>&nbsp;发送的总字节数中的低32 位:</td>
                     		 			<td width="20%">&nbsp;&nbsp;
                     		 			<%if(totalBytesSentLowWord!=""){ %>
                     		 			<%=totalBytesSentLowWord%>
                     		 			<%} %>
                     		 			</td>
                    				</tr>
                    				<tr >
                      				
                      					<td width="30%" height="26" align=right valign=right nowrap class=txtGlobal>&nbsp;接收的总字节数中的高32 位:</td>
                      					<td width="20%">&nbsp;&nbsp;
                      					<%if(totalBytesReceivedHighWord!=""){ %>
                      					<%=totalBytesReceivedHighWord%>
                      					<%} %>
                      					</td>
                    				
                      					<td width="30%" height="26" class=txtGlobal align=right nowrap>&nbsp;接收的总字节数中的低32 位:</td>
                     		 			<td width="20%">&nbsp;&nbsp;
                     		 			<%if(totalBytesReceivedLowWord!=""){ %>
                     		 			<%=totalBytesReceivedLowWord%>
                     		 			<%} %>
                     		 			</td>
                    				</tr> 
                    				<tr bgcolor="#F1F1F1">
                      					<td width="30%" height="26" align=right valign=right nowrap class=txtGlobal>&nbsp;发送的文件总数:</td>
                      					<td width="20%">&nbsp;&nbsp;
                      					<%if(totalFilesSent!=""){ %>
                      					<%=totalFilesSent%>
                      					<%} %>
                      					</td>
                    				
                      					<td width="30%" height="26" class=txtGlobal align=right nowrap>&nbsp;接收的文件总数:</td>
                     		 			<td width="20%">&nbsp;&nbsp;
                     		 			<%if(totalFilesReceived!=""){ %>
                     		 			<%=totalFilesReceived%>
                     		 			<%} %>
                     		 			</td>
                    				</tr>
                    				<tr >
                      					<td width="30%" height="26" align=right valign=right nowrap class=txtGlobal>&nbsp;匿名连接的当前用户数:</td>
                      					<td width="20%">&nbsp;&nbsp;
                      					<%if(currentAnonymousUsers!=""){ %>
                      					<%=currentAnonymousUsers%>
                      					<%} %>
                      					</td>
                    				
                      					<td width="30%" height="26" class=txtGlobal align=right nowrap>&nbsp;匿名连接的用户总数:</td>
                     		 			<td width="20%">&nbsp;&nbsp;
                     		 			<%if(totalAnonymousUsers!=""){ %>
                     		 			<%=totalAnonymousUsers%>
                     		 			<%} %>
                     		 			</td>
                    				</tr>
                    				<tr bgcolor="#F1F1F1">
                      					<td width="30%" height="26" align=right valign=right nowrap class=txtGlobal>&nbsp;匿名连接的最大用户数:</td>
                      					<td width="20%">&nbsp;&nbsp;
                      					<%if(maxAnonymousUsers!=""){ %>
                      					<%=maxAnonymousUsers%></td>
                    				    <%} %>
                      					<td width="30%" height="26" class=txtGlobal align=right nowrap>&nbsp;当前连接数:</td>
                     		 			<td width="20%">&nbsp;&nbsp;
                     		 			<%if(currentConnections!=""){ %>
                     		 			<%=currentConnections%>
                     		 			<%} %>
                     		 			</td>
                    				</tr>
                    				<tr >
                      					<td width="30%" height="26" class=txtGlobal align=right nowrap>&nbsp;最大连接数:</td>
                     		 			<td width="20%">&nbsp;&nbsp;
                     		 			<%if(maxConnections!=""){ %>
                     		 			<%=maxConnections%></td>
                    				    <%} %>
                      					<td width="30%" height="26" align=right align=right nowrap class=txtGlobal>&nbsp;尝试连接数:</td>
                      					<td width="20%">&nbsp;&nbsp;
                      					<%if(connectionAttempts!=""){ %>
                      					<%=connectionAttempts%>
                      					<%} %>
                      					</td>
                    				</tr>
                    				<tr bgcolor="#F1F1F1">
                      					<td width="30%" height="26" class=txtGlobal align=right nowrap>&nbsp;尝试登录数:</td>
                     		 			<td width="20%">&nbsp;&nbsp;
                     		 			<%if(logonAttempts!=""){ %>
                     		 			<%=logonAttempts%></td>
                    				    <%} %>
                      					<td width="30%" height="26" align=right nowrap class=txtGlobal>&nbsp;GET方法请求数:</td>
                      					<td width="20%">&nbsp;&nbsp;
                      					<%if(totalGets!=""){ %>
                      					<%=totalGets%>
                      					 <%} %>
                      					</td>
                    				</tr>
                    				<tr >
                      					<td width="30%" height="26" class=txtGlobal align=right nowrap>&nbsp;POST方法请求数:</td>
                     		 			<td width="20%">&nbsp;&nbsp;
                     		 			<%if(totalPosts!=""){ %>
                     		 			<%=totalPosts%>
                     		 			<%} %>
                     		 			</td>
                    				
                      					<td width="30%" height="26" class=txtGlobal align=right nowrap>&nbsp;页面访问错误总数:</td>
                     		 			<td width="20%">&nbsp;&nbsp;
                     		 			<%if(totalNotFoundErrors!=""){ %>
                     		 			<%=totalNotFoundErrors%>
                     		 			<%} %>
                     		 			</td>
                    				</tr>
                    			</table>	
                    			<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none align=center border=1 algin="center">
                    				<tr>
                					<td colspan=7>
                						<table width="100%" border="0" cellpadding="0" cellspacing="1">
                    							<tr align="left" > 
                      								<td height="23" bgcolor="#D1DDF5"><b><font >&nbsp;连通率信息&nbsp;&nbsp;</font></b></td>
                    							</tr>
                  						</table>
                  					</td>
                  				<tr>
							<td align="left">
								<table style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none width=100% align="left" border=1 algin="center" height=100%>
                      							<tr>
                        							<td align="left" valign="top" height='30'>
											<%if(image_ping!=null){%>                        
                        									<img src="<%=rootPath%>/<%=image_ping%>">
                    										<br>
											<%} else out.print("无数据!");%>                        
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
              		<td width="18%" align="center" valign="top" class=dashLeft>

											<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none align=center border=1 algin="center">
                        								<tbody>
                        								<tr bgcolor=#F1F1F1 height="26">
														<td align="center">
															今日连通率
														</td>
													</tr>
                          									<tr class="topNameRight">
                      											<td height="70" align="center">
                      																		<!--  		<div id="flashcontent00">
																						<strong>You need to upgrade your Flash Player</strong>	
																							</div>
																							<script type="text/javascript">
																							var so = new SWFObject("<%=rootPath%>/flex/Pie_Component.swf?percent1=<%=percent1%>&percentStr1=可用&percent2=<%=percent2%>&percentStr2=不可用", "Pie_Component", "160", "160", "8", "#ffffff");
																							so.write("flashcontent00");
																							</script>
																							-->
														                                        <div id="realping">
							                                                                           <strong>You need to upgrade your Flash Player</strong>
						                                                                       </div>
						                                                                       <script type="text/javascript"
							                                                                         src="<%=rootPath%>/include/swfobject.js"></script>
						                                                                      <script type="text/javascript">
						                                                                             var so = new SWFObject("<%=rootPath%>/amchart/ampie.swf", "ampie","160", "155", "8", "#FFFFFF");
						                                                                                 so.addVariable("path", "<%=rootPath%>/amchart/");
						                                                                                 so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/pingStatepie.xml"));
						                                                                                 so.addVariable("chart_data","<%=realdata%>");
						                                                                                 so.write("realping");
						                                                                    </script>									
													</td>
                    										</tr>
                    										<tr>
										           		<td height="7" align=center><img src="<%=rootPath%>/resource/image/Loading_2.gif"><br><br></td>
										         	</tr>
										         	<tr bgcolor=#F1F1F1 height="26">
														<td align="center">
															当前连通率
														</td>
													</tr>
										         	<tr class="topNameRight">
                      											<td height="70" align="center">
                      																		<!--		<div id="flashcontent01">
																						<strong>You need to upgrade your Flash Player</strong>	
																							</div>
																							<script type="text/javascript">
																							var so = new SWFObject("<%=rootPath%>/flex/DHCCGauge.swf?percent=<%=pingvalue%>&title=当前连通率", "Pie_Component1", "160", "160", "8", "#ffffff");
																							so.write("flashcontent01");
																							</script>
																							 -->
														<div id="avgping">
							                                   <strong>You need to upgrade your Flash Player</strong>
						                                </div>
						                                <script type="text/javascript"
							                                    src="<%=rootPath%>/include/swfobject.js"></script>
						                                <script type="text/javascript">
						                                         var so = new SWFObject("<%=rootPath%>/amchart/ampie.swf", "ampie","160", "155", "8", "#FFFFFF");
						                                             so.addVariable("path", "<%=rootPath%>/amchart/");
						                                             so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/pingStatepie.xml"));
						                                             so.addVariable("chart_data","<%=avgdata%>");
						                                             so.write("avgping");
						                               </script>									 
													</td>
                    										</tr>
                    										<tr>
										           		<td height="7" align=center><img src="<%=rootPath%>/resource/image/Loading.gif"></td>
										         	</tr>
            										</tbody>
            										</table>
            										
            									</td> 
              		</tr>    								
							
		</TABLE>
										 							
										 							
				        										</td>
				        									</tr>
				        								</table>
				        							</td>
				        						</tr>
				        						<tr>
				        							<td>
				        								<table id="detail-content-footer" class="detail-content-footer">
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
</body>
</HTML>