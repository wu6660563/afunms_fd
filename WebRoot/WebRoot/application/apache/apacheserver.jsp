<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.system.model.TimeShareConfig"%>
<%@page import="com.afunms.detail.service.apacheInfo.ApacheInfoService"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="java.util.*"%>
<%@page import="java.lang.*"%>
<%
	String runmodel = PollingEngine.getCollectwebflag(); 
	String rootPath = request.getContextPath();
	double jvm_memoryuiltillize=0;
	ApacheConfig vo=(ApacheConfig)request.getAttribute("vo");
	Hashtable apache_ht = null;
  
	if("0".equals(runmodel)){
	  	//采集与访问是集成模式
		apache_ht = (Hashtable)com.afunms.common.util.ShareData.getApachedata().get("apache"+":"+vo.getIpaddress());
	}else{  
		//采集与访问是分离模式
		ApacheInfoService apacheInfoService = new ApacheInfoService();
		apache_ht = apacheInfoService.getApacheDataHashtable(vo.getId()+"");
	}
	if(apache_ht == null)apache_ht = new Hashtable();
		String status_str = (String)apache_ht.get("status");
		Integer status = 0;
		if(status_str!=null){
		status = Integer.valueOf(status_str);
	}
	//Integer status=(Integer)request.getAttribute("status");
	// Hashtable apache_ht=(Hashtable)request.getAttribute("apache_ht");
	String conn_name = request.getAttribute("conn_name").toString();
	String connsrc = rootPath+"/resource/image/jfreechart/"+conn_name+".png";
	String wave_name = request.getAttribute("wave_name").toString();
	String wavesrc =  rootPath+"/resource/image/jfreechart/"+wave_name+".png";
	String connrate = request.getAttribute("connrate").toString();
	connrate = connrate.replaceAll("%","");
	int percent1 = Double.valueOf(connrate).intValue();
	int percent2 = 100-percent1;
  	String timeFormat = "yyyy-MM-dd";
	String fdate = (String)request.getAttribute("from_date1");
	String tdate = (String)request.getAttribute("to_date1");
	String fhour = (String)request.getAttribute("from_hour");
	String thour = (String)request.getAttribute("to_hour");
	java.text.SimpleDateFormat timeFormatter = new java.text.SimpleDateFormat(timeFormat);
	String from_date1 = "";
	if (fdate == null ){
		from_date1=timeFormatter.format(new java.util.Date());
	}else{
		from_date1 = fdate;
	}
	String to_date1 = "";
	if (tdate == null ){
		to_date1=timeFormatter.format(new java.util.Date());
	}else{
		to_date1 = tdate;
	}
	
	String from_hour = "";
	if (fhour == null ){
		from_hour=new java.util.Date().getHours()+"";
	}else{
		from_hour = fhour;
	}
	String to_hour = "";
	if (thour == null ){
		to_hour=new java.util.Date().getHours()+"";
	}else{
		to_hour = thour;
	}
%>
<% String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>

<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>

<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="gb2312" />
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="gb2312"></script>

<!--nielin add for timeShareConfig at 2010-01-04 start-->
<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/timeShareConfigdiv.js" charset="gb2312"></script>
<!--nielin add for timeShareConfig at 2010-01-04 end-->
<script language="JavaScript" type="text/javascript">

//-- nielin modify at 2010-01-04 start ----------------
function CreateWindow(url)
{
	
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}    

function setReceiver(eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/user.do?action=setReceiver&event='+event.id+'&value='+event.value);
}
//-- nielin modify at 2010-01-04 end ----------------


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
	document.getElementById('apacheDetailTitle-0').className='detail-data-title';
	document.getElementById('apacheDetailTitle-0').onmouseover="this.className='detail-data-title'";
	document.getElementById('apacheDetailTitle-0').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		document.getElementById("id").value="<%=vo.getId()%>";
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}
</script>
</head>
<body id="body" class="body" onload="initmenu();">

<!-- 这里用来定义需要显示的右键菜单 -->
	<div id="itemMenu" style="display: none";>
	<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
		style="border: thin" cellspacing="0">
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.edit()">修改信息</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.detail();">监视信息</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.cancelmanage()">取消监视</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.addmanage()">添加监视</td>
		</tr>		
	</table>
	</div>
	<!-- 右键菜单结束-->

	<form id="mainForm" method="post" name="mainForm">
		<input type=hidden name="id">
		
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
										<%
										
											String apache_ht_version=(String)(apache_ht.get("version")!=null?" ":apache_ht.get("version"));
											String apache_ht_built=(String)(apache_ht.get("built")!=null?" ":apache_ht.get("built"));
											String apache_ht_current=(String)(apache_ht.get("current")!=null?" ":apache_ht.get("current"));
											String apache_ht_restart=(String)(apache_ht.get("restart")!=null?" ":apache_ht.get("restart")); 
											String apache_ht_parent=(String)(apache_ht.get("parent")!=null?" ":apache_ht.get("parent"));
											String apache_ht_uptime=(String)(apache_ht.get("uptime")!=null?" ":apache_ht.get("uptime"));
											String apache_ht_accesses=(String)(apache_ht.get("accesses")!=null?" ":apache_ht.get("accesses"));
											String apache_ht_traffic=(String)(apache_ht.get("traffic")!=null?" ":apache_ht.get("traffic"));
										 %>
											<jsp:include page="/topology/includejsp/middleware_apache.jsp">
												<jsp:param name="alias" value="<%= vo.getAlias()%>"/>
												<jsp:param name="ipaddress" value="<%=vo.getIpaddress() %>"/>
												<jsp:param name="status" value="<%=  NodeHelper.getCurrentStatusImage(status)%>"/>
												<jsp:param name="port" value="<%= vo.getPort()%>"/>
												
												<jsp:param name="version" value="<%= apache_ht_version %>"/>
												<jsp:param name="built" value="<%= apache_ht_built %>"/>
												<jsp:param name="current" value="<%= apache_ht_current %>"/>
												<jsp:param name="restart" value="<%= apache_ht_restart %>"/>
												
												<jsp:param name="parent" value="<%= apache_ht_parent %>"/>
												<jsp:param name="uptime" value="<%= apache_ht_uptime %>"/>
												<jsp:param name="accesses" value="<%= apache_ht_accesses %>"/>
												<jsp:param name="traffic" value="<%= apache_ht_traffic  %>"/>
												 
												 
												<jsp:param name="percent1" value="<%= percent1%>"/> 
												<jsp:param name="percent2" value="<%= percent2%>"/>  
											</jsp:include>
										</td>
									</tr>			
						            <tr>
										<td>
											<table id="detail-data" class="detail-data">
												<tr>
													<td class="detail-data-header">
														<%=apacheDetailTitleTable%>
													</td>
												</tr>
									
												<tr>
													<td>
														<table class="detail-data-body">
									
															<tr>
																<td width="80%" align="left" class=dashLeft>
																	<table style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa
																		cellpadding=0 rules=none width=100% align=center border=0
																		>
																		<tr>
																			<td>
																				<%
																					if (apache_ht.get("pre") != null) {
																				%>
																				<%=apache_ht.get("pre")%>
																				<%
																					}
																				%>
																			</td>
																		</tr>
																		<tr>
																			<td>
																				<font color="#ff0000">*</font>&nbsp;&nbsp;注释：&nbsp;&nbsp;_：等待连结中&nbsp;&nbsp;S：启动中&nbsp;&nbsp;R：正在读取要求&nbsp;&nbsp;W：正在送出回应&nbsp;&nbsp;K：处于保持联机的状态&nbsp;&nbsp;D：正在查找DNS&nbsp;&nbsp;C：正在关闭连结&nbsp;
																			</td>
																		</tr>
																		<tr>
																			<td>
																				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;L：正在写入记录文件&nbsp;&nbsp;G：进入正常结束程序中&nbsp;&nbsp;I：处理闲置&nbsp;&nbsp;.：尚无此程序
																			</td>
																		</tr>
																	</table>
																</td>
															</tr>
														</TABLE>
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
</body>
</HTML>