<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.ibm.as400.access.Job"%>
<%@page import="com.afunms.topology.model.JobForAS400"%>
<%@page import="com.afunms.topology.util.JobConstantForAS400"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="com.afunms.common.util.*" %>
<%@page import="com.afunms.monitor.item.*"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.impl.*"%>
<%@page import="com.afunms.polling.api.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.monitor.item.base.MoidConstants"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@ page import="com.afunms.polling.api.I_Portconfig"%>
<%@ page import="com.afunms.polling.om.Portconfig"%>
<%@ page import="com.afunms.polling.om.*"%>
<%@ page import="com.afunms.polling.impl.PortconfigManager"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%
  	//String rootPath = request.getContextPath();
  	String menuTable = (String)request.getAttribute("menuTable");
  	
  	List jobList = (List)request.getAttribute("jobList");
  	
  	String jobName = (String)request.getAttribute("jobName");
  	if(jobName==null){
  		jobName = "";
  	}
  	
  	String jobUser = (String)request.getAttribute("jobUser");
  	if(jobUser==null){
  		jobUser = "";
  	}
  	
  	String jobType = (String)request.getAttribute("jobType");
  	
  	String jobSubtype = (String)request.getAttribute("jobSubtype");
  	
  	String jobActivestatus = (String)request.getAttribute("jobActivestatus");
  	
  	String jobSortField = (String)request.getAttribute("jobSortField");
  	
  	String jobSortType = (String)request.getAttribute("jobSortType");
  	
  	
  	String tmp = request.getParameter("id"); 
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		double cpuvalue = 0;
		String pingconavg ="0";
		String collecttime = "";
		String sysuptime = "";
		String sysservices = "";
		String sysdescr = "";
		
    	HostNodeDao hostdao = new HostNodeDao();
    	List hostlist = hostdao.loadHost();
    	
    	Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp)); 
    	String ipaddress = host.getIpAddress();
    	String orderflag = request.getParameter("orderflag");
    	if(orderflag == null || orderflag.trim().length()==0)
    		orderflag="index";
        String[] time = {"",""};
        DateE datemanager = new DateE();
        Calendar current = new GregorianCalendar();
        current.set(Calendar.MINUTE,59);
        current.set(Calendar.SECOND,59);
        time[1] = datemanager.getDateDetail(current);
        current.add(Calendar.HOUR_OF_DAY,-1);
        current.set(Calendar.MINUTE,0);
        current.set(Calendar.SECOND,0);
        time[0] = datemanager.getDateDetail(current);
        String starttime = time[0];
        String endtime = time[1];
        
        I_HostLastCollectData hostlastmanager=new HostLastCollectDataManager();
        
        Vector vector = new Vector();
        String[] netInterfaceItem={"index","ifDescr","ifSpeed","ifOperStatus","OutBandwidthUtilHdx","InBandwidthUtilHdx"};
        try{
        	vector = hostlastmanager.getInterface_share(host.getIpAddress(),netInterfaceItem,orderflag,starttime,endtime); 
        }catch(Exception e){
        	e.printStackTrace();
        }
        
		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
		if(ipAllData != null){
			Vector cpuV = (Vector)ipAllData.get("cpu");
			if(cpuV != null && cpuV.size()>0){
				
				CPUcollectdata cpu = (CPUcollectdata)cpuV.get(0);
				cpuvalue = new Double(cpu.getThevalue());
			}
			//得到系统启动时间
			Vector systemV = (Vector)ipAllData.get("system");
			if(systemV != null && systemV.size()>0){
				for(int i=0;i<systemV.size();i++){
					Systemcollectdata systemdata=(Systemcollectdata)systemV.get(i);
					if(systemdata.getSubentity().equalsIgnoreCase("sysUpTime")){
						sysuptime = systemdata.getThevalue();
					}
					if(systemdata.getSubentity().equalsIgnoreCase("sysServices")){
						sysservices = systemdata.getThevalue();
					}
					if(systemdata.getSubentity().equalsIgnoreCase("sysDescr")){
						sysdescr = systemdata.getThevalue();
					}
				}
			}
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String time1 = sdf.format(new Date());
								
	
		String starttime1 = time1 + " 00:00:00";
		String totime1 = time1 + " 23:59:59";
		
		Hashtable ConnectUtilizationhash = new Hashtable();
		I_HostCollectData hostmanager=new HostCollectDataManager();
		try{
			ConnectUtilizationhash = hostmanager.getCategory(host.getIpAddress(),"Ping","ConnectUtilization",starttime1,totime1);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		if (ConnectUtilizationhash.get("avgpingcon")!=null){
			pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			pingconavg = pingconavg.replace("%", "");
		}
		
		
		Vector pingData = (Vector)ShareData.getPingdata().get(host.getIpAddress());
		if(pingData != null && pingData.size()>0){
			Pingcollectdata pingdata = (Pingcollectdata)pingData.get(0);
			Calendar tempCal = (Calendar)pingdata.getCollecttime();							
			Date cc = tempCal.getTime();
			collecttime = sdf1.format(cc);
		}
		
        	request.setAttribute("vector", vector);
        	request.setAttribute("id", tmp);
        	request.setAttribute("ipaddress", host.getIpAddress());
		request.setAttribute("cpuvalue", cpuvalue);
		request.setAttribute("collecttime", collecttime);
		request.setAttribute("sysuptime", sysuptime);
		request.setAttribute("sysservices", sysservices);
		request.setAttribute("sysdescr", sysdescr);
		request.setAttribute("pingconavg", new Double(pingconavg));




  
String[] memoryItem={"AllSize","UsedSize","Utilization"};
String[] memoryItemch={"总容量","已用容量","当前利用率","最大利用率"};
String[] sysItem={"sysName","sysUpTime","sysContact","sysLocation","sysServices","sysDescr"};
String[] sysItemch={"设备名","设备启动时间","设备联系","设备位置","设备服务","设备描述"};
Hashtable hash = (Hashtable)request.getAttribute("hash");
Hashtable hash1 = (Hashtable)request.getAttribute("hash1");
Hashtable max = (Hashtable) request.getAttribute("max");
Hashtable imgurl = (Hashtable)request.getAttribute("imgurl");

double avgpingcon = (Double)request.getAttribute("pingconavg");
int percent1 = Double.valueOf(avgpingcon).intValue();
int percent2 = 100-percent1;
int cpuper = Double.valueOf(cpuvalue).intValue();
  String rootPath = request.getContextPath(); 
  
	Vector ipmacvector = (Vector)request.getAttribute("vector");
	if (ipmacvector == null)ipmacvector = new Vector();  
  
  //ResponseTimeItem item1 = (ResponseTimeItem)host.getMoidList().get(0); 
  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
  DefaultPieDataset dpd = new DefaultPieDataset();
  dpd.setValue("可用率",avgpingcon);
  dpd.setValue("不可用率",100 - avgpingcon);
  chart1 = ChartCreator.createPieChart(dpd,"",130,130);  
  
  //if(item1.getSingleResult()!=-1)
  //{
     //responseTime = item1.getSingleResult() + " ms";
  
     //SnmpItem item2 = (SnmpItem)host.getItemByMoid(MoidConstants.CISCO_CPU);
     //if(item2!=null&&item2.getSingleResult()!=-1)
         //chart2 = ChartCreator.createMeterChart(item2.getSingleResult(),"",150,150); 
         chart2 = ChartCreator.createMeterChart(cpuvalue,"",120,120);   
  
     //SnmpItem item3 = (SnmpItem)host.getItemByMoid(MoidConstants.CISCO_MEMORY);
     //if(item3!=null&&item3.getSingleResult()!=-1)
        chart3 = ChartCreator.createMeterChart(40.0,"",120,120);       
  //}
  //else
     //responseTime = "无响应"; 
     
     Vector ifvector = (Vector)request.getAttribute("vector"); 	
     SupperDao supperdao = new SupperDao();
    	Supper supper = null;
    	String suppername = "";
    	try{
    		supper = (Supper)supperdao.findByID(host.getSupperid()+"");
    		if(supper != null)suppername = supper.getSu_name()+"("+supper.getSu_dept()+")";
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		supperdao.close();
    	}		  	   
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 


<script language="javascript">	
  
  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
	
	
});

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
  
    function toGetConfigFile()
  {
        msg.style.display="block";
        mainForm.action = "<%=rootPath%>/cfgfile.do?action=getcfgfile&ipaddress=<%=host.getIpAddress()%>";
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
  
function changeOrder(para){
  	location.href="<%=rootPath%>/detail/dispatcher.jsp?id=net<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&orderflag="+para;
} 
function openwin3(operate,index,ifname) 
{	
        window.open ("<%=rootPath%>/monitor.do?action="+operate+"&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&ifindex="+index+"&ifname="+ifname, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes") 
} 

</script>
<script language="JavaScript">

	//公共变量
	var node="";
	var ipaddress="";
	var operate="";
	/**
	*根据传入的id显示右键菜单
	*/
	function showMenu(id,nodeid,ip)
	{	
		ipaddress=ip;
		node=nodeid;
		//operate=oper;
	    if("" == id)
	    {
	        popMenu(itemMenu,100,"100");
	    }
	    else
	    {
	        popMenu(itemMenu,100,"1111");
	    }
	    event.returnValue=false;
	    event.cancelBubble=true;
	    return false;
	}
	/**
	*显示弹出菜单
	*menuDiv:右键菜单的内容
	*width:行显示的宽度
	*rowControlString:行控制字符串，0表示不显示，1表示显示，如“101”，则表示第1、3行显示，第2行不显示
	*/
	function popMenu(menuDiv,width,rowControlString)
	{
	    //创建弹出菜单
	    var pop=window.createPopup();
	    //设置弹出菜单的内容
	    pop.document.body.innerHTML=menuDiv.innerHTML;
	    var rowObjs=pop.document.body.all[0].rows;
	    //获得弹出菜单的行数
	    var rowCount=rowObjs.length;
	    //alert("rowCount==>"+rowCount+",rowControlString==>"+rowControlString);
	    //循环设置每行的属性
	    for(var i=0;i<rowObjs.length;i++)
	    {
	        //如果设置该行不显示，则行数减一
	        var hide=rowControlString.charAt(i)!='1';
	        if(hide){
	            rowCount--;
	        }
	        //设置是否显示该行
	        rowObjs[i].style.display=(hide)?"none":"";
	        //设置鼠标滑入该行时的效果
	        rowObjs[i].cells[0].onmouseover=function()
	        {
	            this.style.background="#397DBD";
	            this.style.color="white";
	        }
	        //设置鼠标滑出该行时的效果
	        rowObjs[i].cells[0].onmouseout=function(){
	            this.style.background="#F1F1F1";
	            this.style.color="black";
	        }
	    }
	    //屏蔽菜单的菜单
	    pop.document.oncontextmenu=function()
	    {
	            return false; 
	    }
	    //选择右键菜单的一项后，菜单隐藏
	    pop.document.onclick=function()
	    {
	        pop.hide();
	    }
	    //显示菜单
	    pop.show(event.clientX-1,event.clientY,width,rowCount*25,document.body);
	    return true;
	}
	function detail()
	{
	    //location.href="<%=rootPath%>/detail/dispatcher.jsp?id="+node;
	    window.open ("<%=rootPath%>/monitor.do?action=show_hostutilhdx&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&ifindex="+node+"&ifname="+ipaddress, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes");
	}
	function portset()
	{
		window.open ("<%=rootPath%>/panel.do?action=show_portreset&ipaddress=<%=host.getIpAddress()%>&ifindex="+node, "newwindow", "height=200, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes");
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
	setClass();
	initSearchCondition();
}

function setClass(){
	document.getElementById('AS400DetailTitle-4').className='detail-data-title';
	document.getElementById('AS400DetailTitle-4').onmouseover="this.className='detail-data-title'";
	document.getElementById('AS400DetailTitle-4').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		document.getElementById("id").value="<%=tmp%>";
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}

function search(){
	document.getElementById("id").value="<%=tmp%>";
	mainForm.action = '<%=rootPath%>/monitor.do?action=AS400JobsDetail';
	mainForm.submit();
}
function sort(jobSortField , jobSortType){
	document.getElementById("id").value="<%=tmp%>";
	document.getElementById("jobSortField").value=jobSortField;
	if(jobSortType == 'asc'){
		jobSortType = 'desc';
	}else {
		jobSortType = 'asc';
	}
	document.getElementById("jobSortType").value = jobSortType;
	mainForm.action = '<%=rootPath%>/monitor.do?action=AS400JobsDetail';
	mainForm.submit();
}

function initSearchCondition(){
	
	<%
		String sortFiledimgsrc = "";
		if("desc".equals(jobSortType)){
	  	  	sortFiledimgsrc = "/afunms/resource/image/btn_up2.gif";
	  	}else if("asc".equals(jobSortType)){
	  	  	sortFiledimgsrc = "/afunms/resource/image/btn_up1.gif";
	  	}
	  	
	  	System.out.println(sortFiledimgsrc);
  	%>
  	
  	var sortFiledimg = document.getElementById("<%=jobSortField%>" + "-img");
  	if(sortFiledimg){
  		sortFiledimg.src = "<%=sortFiledimgsrc%>";
  		sortFiledimg.style.display = "inline";
  	}
	document.getElementById("jobName").value = "<%=jobName%>";
	document.getElementById("jobUser").value = "<%=jobUser%>";
	document.getElementById("jobType").value = "<%=jobType%>";
	document.getElementById("jobSubtype").value = "<%=jobSubtype%>";
	document.getElementById("jobActivestatus").value = "<%=jobActivestatus%>";
	document.getElementById("jobSortField").value = "<%=jobSortField%>";
	document.getElementById("jobSortType").value = "<%=jobSortType%>";
}

</script>


</head>
<body id="body" class="body" onload="initmenu();">

<div id="itemMenu" style="display: none";>
	<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
		style="border: thin;font-size: 12px" cellspacing="0">
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.detail()">查看状态</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.portset();">端口设置</td>
		</tr>		
	</table>
	</div>

	<form id="mainForm" method="post" name="mainForm">
		<input type=hidden name="orderflag">
		<input type=hidden name="id">
		<input type=hidden name="jobSortField" id="jobSortField">
		<input type=hidden name="jobSortType" id="jobSortType">
		
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
							<td class="td-container-main-detail">
								<table id="container-main-detail" class="container-main-detail">
									<tr>
										<td> 
											<jsp:include page="/topology/includejsp/systeminfo_hostas400.jsp">
												 <jsp:param name="rootPath" value="<%= rootPath %>"/>
												 <jsp:param name="tmp" value="<%= tmp %>"/>
												 <jsp:param name="collecttime" value="<%= collecttime %>"/>
												 <jsp:param name="sysuptime" value="<%= sysuptime %>"/>
												 <jsp:param name="sysdescr" value="<%= sysdescr %>"/>
												 <jsp:param name="percent1" value="<%= percent1 %>"/>
												 <jsp:param name="percent2" value="<%=percent2%>"/>
												 <jsp:param name="cpuper" value="<%= cpuper %>"/>  
										 	</jsp:include>	
										</td>
									</tr>
									<tr>
										<td>
											<table id="detail-data" class="detail-data">
												<tr>
											    	<td class="detail-data-header">
											    		<%=AS400DetailTitleTable%>
											    	</td>
											  </tr>
											  <tr>
											    	<td>
													<table class="detail-data-body">
														<tr style="height: 30">
															<td>
																<table height="30">
																	<tr>
											      						<td class="detail-data-body-title" colspan="7" style="text-align: left;">
											      							&nbsp;&nbsp;&nbsp;作业名：
											      							<input type="text" name="jobName" id="jobName">
											      							&nbsp;&nbsp;&nbsp;用户名：
											      							<input type="text" name="jobUser" id="jobUser">
										      							</td>
										      						</tr>
										      					</table>
										      				</td>
										      			</tr>
										      			<tr>
										      				<td>
										      					<table height="30">
											      					<tr>
											      						<td class="detail-data-body-title" colspan="7" style="text-align: left;">
											      							&nbsp;&nbsp;&nbsp;类型：
											      							<select name="jobType" id="jobType" style="width: 150px">
											      								<option value="-1">不限</option>
											      							<% 
												      							Enumeration<String> typeEnumeration = JobConstantForAS400.getTypeEnumeration();
												      							while(typeEnumeration.hasMoreElements()){
												      								String jobType_per = typeEnumeration.nextElement();
												      								String typeDescription_per = JobConstantForAS400.getTypeDescription(jobType_per);
												      								if("".equals(typeDescription_per)){
													      								typeDescription_per = "无类型";
													      							}
												      								%>
												      								<option value="<%=jobType_per%>" title="<%=typeDescription_per%>"><%=typeDescription_per%></option>
												      								<%
												      							}
											      							%>
											      							</select>
											      							&nbsp;&nbsp;&nbsp;子类型：
											      							<select name="jobSubtype" id="jobSubtype" style="width: 150px">
											      								<option value="-1">不限</option>
											      							<% 
												      							Enumeration<String> subtypeEnumeration = JobConstantForAS400.getSubtypeEnumeration();
												      							while(subtypeEnumeration.hasMoreElements()){
												      								String jobSubtype_per = subtypeEnumeration.nextElement();
												      								String subtypeDescription_per = JobConstantForAS400.getSubtypeDescription(jobSubtype_per);
												      								if("".equals(subtypeDescription_per)){
													      										subtypeDescription_per = "无子类型";
													      							}
												      								%>
												      								<option value="<%=jobSubtype_per%>" title="<%=subtypeDescription_per%>"><%=subtypeDescription_per%></option>
												      								<%
												      							}
											      							%>
											      							</select>
											      							&nbsp;&nbsp;&nbsp;状态：
											      							<select name="jobActivestatus" id="jobActivestatus" style="width: 150px">
											      								<option value="-1">不限</option>
												      							<% 
													      							Enumeration<String> statusEnumeration = JobConstantForAS400.getActiveStatusEnumeration();
													      							while(statusEnumeration.hasMoreElements()){
													      								String jobStatus_per = statusEnumeration.nextElement();
													      								String statusDescription_per = JobConstantForAS400.getActiveStatusDescription(jobStatus_per);
													      									
													      								%>
													      								<option value="<%=jobStatus_per%>" title="<%=statusDescription_per%>"><%=statusDescription_per%></option>
													      								<%
													      							}
												      							%>
											      							</select>
											      							<input type="button" value="查  询" onclick="search()">
											      						</td>
											      					</tr>
																</table>
															</td>
														</tr>
											      		<tr>
											      			<td>
											      				<table>
											      					<tr height="25">
											      						<td class="detail-data-body-title">序号</td>
											      						<td class="detail-data-body-title"><a href="#" onclick="sort('name' , '<%=jobSortType%>')">子系统/任务</a><img id="name-img" style="display: none;"></td>
											      						<td class="detail-data-body-title"><a href="#" onclick="sort('user' , '<%=jobSortType%>')">用户</a><img id="user-img" style="display: none;"></td>
																	 	<td class="detail-data-body-title"><a href="#" onclick="sort('type' , '<%=jobSortType%>')">类型</a><img id="type-img" style="display: none;"></td>
																	 	<td class="detail-data-body-title"><a href="#" onclick="sort('subtype' , '<%=jobSortType%>')">子类型</a><img id="subtype-img" style="display: none;"></td>
																		<td class="detail-data-body-title"><a href="#" onclick="sort('cpu_used_time' , '<%=jobSortType%>')">占用CPU时间(毫秒)</a><img id="cpu_used_time-img" style="display: none;"></td>
																		<td class="detail-data-body-title"><a href="#" onclick="sort('active_status' , '<%=jobSortType%>')">活动状态</a><img id="active_status-img" style="display: none;"></td>
											      					</tr>
											      					<%
											      						if(jobList!=null){
											      							for(int i = 0 ; i < jobList.size() ; i++){
												      							JobForAS400 jobForAS400 = (JobForAS400)jobList.get(i);
												      							
												      							String jobType_per = JobConstantForAS400.getTypeDescription(jobForAS400.getType());
												      							if(jobType_per == null){
												      								jobType_per = "";
												      							}
												      							String jobSubtype_per = JobConstantForAS400.getSubtypeDescription(jobForAS400.getSubtype());
												      							if(jobSubtype_per == null){
												      								jobSubtype_per = "";
												      							}
												      							
												      							String jobStatus_per = JobConstantForAS400.getActiveStatusDescription(jobForAS400.getActiveStatus().trim());
												      							if(jobStatus_per == null){
												      								jobStatus_per = jobForAS400.getActiveStatus();
												      							}
												      							
											      							%>
											      							<tr <%=onmouseoverstyle%>>
											      								<td class="detail-data-body-list"><%=i + 1%></td>
												      							<td class="detail-data-body-list"><%=jobForAS400.getName()%></td>
												      							<td class="detail-data-body-list"><%=jobForAS400.getUser()%></td>
																		 		<td class="detail-data-body-list"><%=jobType_per%></td>
																		 		<td class="detail-data-body-list"><%=jobSubtype_per%></td>
																				<td class="detail-data-body-list"><%=jobForAS400.getCPUUsedTime()%></td>
																				<td class="detail-data-body-list"><%=jobStatus_per%></td>
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
							<td class="td-container-main-tool">
								<jsp:include page="/include/toolbar.jsp">
									<jsp:param value="<%=host.getIpAddress()%>" name="ipaddress"/>
									<jsp:param value="<%=host.getSysOid()%>" name="sys_oid"/>
									<jsp:param value="<%=tmp%>" name="tmp"/>
									<jsp:param value="host" name="category"/>
									<jsp:param value="as400" name="subtype"/>
								</jsp:include>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		
	</form>
</BODY>
</HTML>