<%@page language="java" contentType="text/html;charset=gb2312" %>

<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>

<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.common.util.SysUtil"%>
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
<%@ page import="com.afunms.config.model.Nodeconfig"%>
<%@ page import="com.afunms.event.model.EventList"%>
<%@page import="com.afunms.detail.service.cpuInfo.CpuInfoService"%>
<%@page import="com.afunms.detail.service.diskInfo.DiskInfoService"%>
<%@page import="com.afunms.detail.service.OtherInfo.OtherInfoService"%>
<%@page import="com.afunms.detail.service.systemInfo.SystemInfoService"%>
<%@page import="com.afunms.detail.service.sysInfo.DiskPerfInfoService"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.detail.service.interfaceInfo.InterfaceInfoService"%>
<%@page import="com.afunms.detail.service.systemInfo.SystemInfoService"%>
<%@page import="com.afunms.detail.service.pingInfo.PingInfoService"%>
<%@page import="com.afunms.detail.service.sysInfo.CpuPerfInfoService"%>
<%@page import="com.afunms.detail.service.memoryInfo.MemoryInfoService"%>
<%@page import="com.afunms.temp.dao.UserTempDao"%>
<%@page import="com.afunms.detail.service.configInfo.NetmediaConfigInfoService"%>


<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%
	String runmodel = PollingEngine.getCollectwebflag(); 
  	String menuTable = (String)request.getAttribute("menuTable");
	String[] processItem={"pid","Name","Type","CpuTime","MemoryUtilization","Memory","Status"};
	String[] processItemch={"����ID","��������","��������","cpuʱ��","�ڴ�ռ����","�ڴ�ռ����","��ǰ״̬"};
	String[] pingItem={"ResponseTime","LostPack"}; 
	Hashtable userhash = (Hashtable)request.getAttribute("userhash");
	Hashtable processhash = (Hashtable) request.getAttribute("processhash"); 
	
	Hashtable max = (Hashtable) request.getAttribute("max");
	Hashtable memmaxhash = (Hashtable) request.getAttribute("memmaxhash");
	Hashtable memavghash = (Hashtable) request.getAttribute("memavghash");
	Hashtable diskhash = (Hashtable) request.getAttribute("diskhash");
	Hashtable memhash = (Hashtable) request.getAttribute("memhash");
	String avgcpu = (String)max.get("cpuavg");
	String cpumax = (String)max.get("cpu");
  	String tmp = request.getParameter("id"); 
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	double cpuvalue = 0;
	String pingconavg ="0";
	String collecttime = "";
	String sysuptime = "";
	String sysservices = "";
	String sysdescr = "";
	
	String hostname = "";
	String porcessors = "";
	String sysname = "";
	String SerialNumber = "";
	String CSDVersion = "";
	String mac = "";
		
   	HostNodeDao hostdao = new HostNodeDao();
   	List hostlist = hostdao.loadHost();
   	
   	Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp)); 
   	String ipaddress = host.getIpAddress();
   	String orderflag = request.getParameter("orderflag");
   	if(orderflag == null || orderflag.trim().length()==0){
   		orderflag="index";
   	}
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
    Vector cpuV = new Vector(); 
    Vector systemV = new Vector();
    Vector pingData = new Vector();
    Hashtable hostinfohash = new Hashtable();
    List networkconfiglist = new ArrayList();
    List cpulist = new ArrayList();
    Nodeconfig nodeconfig = new Nodeconfig();
    String processornum = "";
    String[] netInterfaceItem={"index","ifDescr","ifSpeed","ifOperStatus","OutBandwidthUtilHdx","InBandwidthUtilHdx"};
    try{
    	if("0".equals(runmodel)){
			//�ɼ�������Ǽ���ģʽ
    		vector = hostlastmanager.getInterface_share(host.getIpAddress(),netInterfaceItem,orderflag,starttime,endtime); 
    		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
    		//nodeconfig
			nodeconfig= (Nodeconfig)ipAllData.get("nodeconfig");
			cpuV = (Vector)ipAllData.get("cpu");
			systemV = (Vector)ipAllData.get("system");
			pingData = (Vector)ShareData.getPingdata().get(host.getIpAddress());
    	}else{
    		//�ɼ�������Ƿ���ģʽ
			NodeUtil nodeUtil = new NodeUtil();
			NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(host);
			//nodeconfig
			NodeconfigDao nodeconfigDao = new NodeconfigDao();
			try{
				nodeconfig = nodeconfigDao.getByNodeID(nodedto.getId()+"");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				nodeconfigDao.close();
			}
			//cpu
			CpuInfoService cpuInfoService = new CpuInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype()); 
			cpuV = cpuInfoService.getCpuInfo();
			//�õ�ϵͳ����ʱ��
			SystemInfoService systemInfoService = new SystemInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype());
			systemV = systemInfoService.getSystemInfo();
			//ping
			PingInfoService pingInfoService = new PingInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype());
			pingData = pingInfoService.getPingInfo(); 
    	}
    }catch(Exception e){
    	e.printStackTrace();
    }
	//Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
	//if(ipAllData != null){
		//nodeconfig= (Nodeconfig)ipAllData.get("nodeconfig");
	if(nodeconfig != null){
		mac = nodeconfig.getMac();
		processornum = nodeconfig.getNumberOfProcessors();
		CSDVersion = nodeconfig.getCSDVersion();
		hostname = nodeconfig.getHostname();
	}
	//hostinfohash = (Hashtable)ipAllData.get("hostinfo");
	//networkconfiglist = (List)ipAllData.get("networkconfig");
	//cpulist = (List)hostinfohash.get("CPUname");
	//hostname = (String)hostinfohash.get("Hostname");
	//porcessors = (String)hostinfohash.get("NumberOfProcessors");
	//sysname = (String)hostinfohash.get("Sysname");
	//SerialNumber = (String)hostinfohash.get("SerialNumber");
	//CSDVersion = (String)hostinfohash.get("CSDVersion");
	//Vector cpuV = (Vector)ipAllData.get("cpu");
	if(cpuV != null && cpuV.size()>0){
		CPUcollectdata cpu = (CPUcollectdata)cpuV.get(0);
		if(cpu != null && cpu.getThevalue() != null){
			cpuvalue = new Double(cpu.getThevalue());
		}
	}
	//�õ�ϵͳ����ʱ��
	//Vector systemV = (Vector)ipAllData.get("system");
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
			if(systemdata.getSubentity().equalsIgnoreCase("SysName")){
				sysname = systemdata.getThevalue();
			}
		}
	}
	//}
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
	//Vector pingData = (Vector)ShareData.getPingdata().get(host.getIpAddress());
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

	String[] sysItem={"sysName","sysUpTime","sysContact","sysLocation","sysServices","sysDescr"};
	String[] sysItemch={"�豸��","�豸����ʱ��","�豸��ϵ","�豸λ��","�豸����","�豸����"};

	Hashtable hash = (Hashtable)request.getAttribute("hash");
	Hashtable hash1 = (Hashtable)request.getAttribute("hash1");
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
  	dpd.setValue("������",avgpingcon);
  	dpd.setValue("��������",100 - avgpingcon);
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
     	//responseTime = "����Ӧ"; 
     
 	Vector ifvector = (Vector)request.getAttribute("vector"); 	
    String startdate = (String)request.getAttribute("startdate");
	String todate = (String)request.getAttribute("todate");  
	int level1 = Integer.parseInt(request.getAttribute("level1")+"");
	int _status = Integer.parseInt(request.getAttribute("status")+"");
	String level0str="";
	String level1str="";
	String level2str="";
	String level3str="";
	if(level1 == 0){
		level0str = "selected";
	}else if(level1 == 1){
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
  	
  	String ip = host.getIpAddress();
	String picip = CommonUtil.doip(ip);
	
    	//���ɵ���ƽ����ͨ��ͼ��
	CreatePiePicture _cpp = new CreatePiePicture();
	_cpp.createAvgPingPic(picip,avgpingcon); 
	
		 //����CPU�Ǳ���
	CreateMetersPic cmp = new CreateMetersPic();
	cmp.createCpuPic(picip,cpuper);	
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
<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
<script>
function createQueryString(){
		var user_name = encodeURI($("#user_name").val());
		var passwd = encodeURI($("#passwd").val());
		var queryString = {userName:user_name,passwd:passwd};
		return queryString;
	}
	
	function batchAccfiEvent(){
		var eventids = ''; 
		var formItem=document.forms["mainForm"];
		var formElms=formItem.elements;
		var l=formElms.length;
		while(l--){
			if(formElms[l].type=="checkbox"){
				var checkbox=formElms[l];
				if(checkbox.name == "checkbox" && checkbox.checked==true){
	 				if (eventids==""){
	 					eventids=checkbox.value;
	 				}else{
	 					eventids=eventids+","+checkbox.value;
	 				}
 				}
			}
		}
        if(eventids == ""){
        	alert("δѡ��");
        	return ;
        }
		window.open("<%=rootPath%>/alarm/event/batch_accitevent.jsp?eventids="+eventids,"accEventWindow", "toolbar=no,height=400, width= 800, top=200, left= 200,resizable=yes,scrollbars=yes,screenX=0,screenY=0");
	}
	
	//batchDoReport();
	function batchDoReport(){
		 var eventids = ''; 
		var formItem=document.forms["mainForm"];
		var formElms=formItem.elements;
		var l=formElms.length;
		while(l--){
			if(formElms[l].type=="checkbox"){
				var checkbox=formElms[l];
				if(checkbox.name == "checkbox" && checkbox.checked==true){
	 				if (eventids==""){
	 					eventids=checkbox.value;
	 				}else{
	 					eventids=eventids+","+checkbox.value;
	 				}
 				}
			}
		}
        if(eventids == ""){
        	alert("δѡ��");
        	return ;
        }
		window.open("<%=rootPath%>/alarm/event/batch_doreport.jsp?eventids="+eventids,"accEventWindow", "toolbar=no,height=400, width= 800, top=200, left= 200,resizable=yes,scrollbars=yes,screenX=0,screenY=0");
	}
	
	function batchEditAlarmLevel(){
		var eventids = ''; 
		var formItem=document.forms["mainForm"];
		var formElms=formItem.elements;
		var l=formElms.length;
		while(l--){
			if(formElms[l].type=="checkbox"){
				var checkbox=formElms[l];
				if(checkbox.name == "checkbox" && checkbox.checked==true){
	 				if (eventids==""){
	 					eventids=checkbox.value;
	 				}else{
	 					eventids=eventids+","+checkbox.value;
	 				}
 				}
			}
		}
        if(eventids == ""){
        	alert("δѡ��");
        	return ;
        }
		window.open("<%=rootPath%>/alarm/event/batch_editAlarmLevel.jsp?eventids="+eventids,"accEventWindow", "toolbar=no,height=400, width= 800, top=200, left= 200,resizable=yes,scrollbars=yes,screenX=0,screenY=0");
	}
	
function gzmajax(){
$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=ajaxUpdate_availability&tmp=<%=tmp%>&nowtime="+(new Date()),
			success:function(data){
				  $("#pingavg").attr({ src: "<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>pingavg.png?nowtime="+(new Date()), alt: "ƽ����ͨ��" });
				$("#cpu").attr({ src: "<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>cpu.png?nowtime="+(new Date()), alt: "��ǰCPU������" }); 
			
			}
		});
}
$(document).ready(function(){
	//$("#testbtn").bind("click",function(){
	//	gzmajax();
	//});
setInterval(gzmajax,60000);
});
</script>
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
     	alert("�������ѯ����");
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
  	location.href="<%=rootPath%>/detail/host_linuxutilhdx.jsp?id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&orderflag="+para;
} 
function openwin3(operate,index,ifname) 
{	
        window.open ("<%=rootPath%>/monitor.do?action="+operate+"&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&ifindex="+index+"&ifname="+ifname, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes") 
} 

function reportByMinute(){
	window.open("<%=rootPath%>/monitor.do?action=hostping_report&id=<%=tmp%>&timeType=minute","newWindow","height=300, width=800, top=0, left= 200,screenX=0,screenY=0");
  }
  
  function query()
  {  
  	mainForm.id.value = <%=tmp%>;
     mainForm.action = "<%=rootPath%>/monitor.do?action=hostevent";
     mainForm.submit();
  }
  
// ȫ���ۿ�
function gotoFullScreen() {
	parent.mainFrame.resetProcDlg();
	var status = "toolbar=no,height="+ window.screen.height + ",";
	status += "width=" + (window.screen.width-8) + ",scrollbars=no";
	status += "screenX=0,screenY=0";
	window.open("topology/network/index.jsp", "fullScreenWindow", status);
	parent.mainFrame.zoomProcDlg("out");
}

</script>
<script language="JavaScript">

	//��������
	var node="";
	var ipaddress="";
	var operate="";
	/**
	*���ݴ����id��ʾ�Ҽ��˵�
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
	*��ʾ�����˵�
	*menuDiv:�Ҽ��˵�������
	*width:����ʾ�Ŀ��
	*rowControlString:�п����ַ�����0��ʾ����ʾ��1��ʾ��ʾ���硰101�������ʾ��1��3����ʾ����2�в���ʾ
	*/
	function popMenu(menuDiv,width,rowControlString)
	{
	    //���������˵�
	    var pop=window.createPopup();
	    //���õ����˵�������
	    pop.document.body.innerHTML=menuDiv.innerHTML;
	    var rowObjs=pop.document.body.all[0].rows;
	    //��õ����˵�������
	    var rowCount=rowObjs.length;
	    //alert("rowCount==>"+rowCount+",rowControlString==>"+rowControlString);
	    //ѭ������ÿ�е�����
	    for(var i=0;i<rowObjs.length;i++)
	    {
	        //������ø��в���ʾ����������һ
	        var hide=rowControlString.charAt(i)!='1';
	        if(hide){
	            rowCount--;
	        }
	        //�����Ƿ���ʾ����
	        rowObjs[i].style.display=(hide)?"none":"";
	        //������껬�����ʱ��Ч��
	        rowObjs[i].cells[0].onmouseover=function()
	        {
	            this.style.background="#397DBD";
	            this.style.color="white";
	        }
	        //������껬������ʱ��Ч��
	        rowObjs[i].cells[0].onmouseout=function(){
	            this.style.background="#F1F1F1";
	            this.style.color="black";
	        }
	    }
	    //���β˵��Ĳ˵�
	    pop.document.oncontextmenu=function()
	    {
	            return false; 
	    }
	    //ѡ���Ҽ��˵���һ��󣬲˵�����
	    pop.document.onclick=function()
	    {
	        pop.hide();
	    }
	    //��ʾ�˵�
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
	document.getElementById('sunDetailTitle-6').className='detail-data-title';
	document.getElementById('sunDetailTitle-6').onmouseover="this.className='detail-data-title'";
	document.getElementById('sunDetailTitle-6').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		document.getElementById("id").value="<%=tmp%>";
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}

//�����豸��ip��ַ
function modifyIpAliasajax(ipaddress){
	var t = document.getElementById('ipalias'+ipaddress);
	var ipalias = t.options[t.selectedIndex].text;//��ȡ�������ֵ
	$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=modifyIpAlias&ipaddress="+ipaddress+"&ipalias="+ipalias,
			success:function(data){
				window.alert("�޸ĳɹ���");
			}
		});
}
$(document).ready(function(){
	//$("#testbtn").bind("click",function(){
	//	gzmajax();
	//});
//setInterval(modifyIpAliasajax,60000);
});
</script>


</head>
<body id="body" class="body" onload="initmenu();">
<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>

<div id="itemMenu" style="display: none";>
	<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
		style="border: thin;font-size: 12px" cellspacing="0">
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.detail()">�鿴״̬</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.portset();">�˿�����</td>
		</tr>		
	</table>
	</div>

	<form id="mainForm" method="post" name="mainForm">
		<input type=hidden name="orderflag">
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
							<td class="td-container-main-detail" width=85%>
								<table id="container-main-detail" class="container-main-detail">
									<tr>
										<td>
											<jsp:include page="/topology/includejsp/systeminfo_hostsolaris.jsp">
												 <jsp:param name="rootPath" value="<%= rootPath %>"/>
												 <jsp:param name="tmp" value="<%= tmp %>"/> 
												 <jsp:param name="picip" value="<%= picip %>"/> 
												 <jsp:param name="hostname" value="<%= hostname %>"/> 
												 <jsp:param name="sysname" value="<%= sysname %>"/> 
												 <jsp:param name="CSDVersion" value="<%= CSDVersion %>"/> 
												 <jsp:param name="processornum" value="<%= processornum %>"/> 
												 <jsp:param name="collecttime" value="<%= collecttime %>"/> 
												 <jsp:param name="sysuptime" value="<%= sysuptime %>"/> 
												 <jsp:param name="mac" value="<%= mac %>"/>
											 </jsp:include>
										</td>
									</tr>
									<tr>
										<td>
											<table id="detail-data" class="detail-data">
												<tr>
											    	<td class="detail-data-header">
											    		<%=sunDetailTitleTable%>
											    	</td>
											  </tr>
											  <tr>
											    	<td>
													<table class="detail-data-body">
												      		  <tr>
                                                                <td align=left height="28" bgcolor="#ECECEC">
																
     ��ʼ����
		<input type="text" name="startdate" value="<%=startdate%>" size="10">
	<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
	<img id=imageCalendar1 align=absmiddle width=34 height=21 src=<%=rootPath%>/include/calendar/button.gif border=0></a>
	
		��ֹ����
		<input type="text" name="todate" value="<%=todate%>" size="10"/>
	<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
	<img id=imageCalendar2 align=absmiddle width=34 height=21 src=<%=rootPath%>/include/calendar/button.gif border=0></a>
		�¼��ȼ�
		<select name="level1">
		<option value="99">����</option>
		<option value="0" <%=level0str%>>��ʾ��Ϣ</option>
		<option value="1" <%=level1str%>>��ͨ�¼�</option>
		<option value="2" <%=level2str%>>�����¼�</option>
		<option value="3" <%=level3str%>>�����¼�</option>
		</select>
					
		����״̬
		<select name="status">
		<option value="99">����</option>
		<option value="0" <%=status0str%>>δ����</option>
		<option value="1" <%=status1str%>>���ڴ���</option>
		<option value="2" <%=status2str%>>�Ѵ���</option>
		</select>	
	<input type="button" name="submitss" value="��ѯ" onclick="query()"><hr/>
						</td>
						</tr> 
						<tr align="right" bgcolor="#ECECEC">
							<td><table><tr>
							<td width="75%">&nbsp;</td>
							<td width="15" height=15 >&nbsp;&nbsp;</td>
							<td  height=15>&nbsp;&nbsp;<input type="button" name="" value="���ܴ���" onclick='batchAccfiEvent();'/></td>
							<td width="15" height=15>&nbsp;&nbsp;</td>
							<td  height=15>&nbsp;&nbsp;<input type="button" name="" value="��д����" onclick='batchDoReport();'/></td>
							<td width="15" height=15>&nbsp;&nbsp;</td>
							<td  height=15>&nbsp;&nbsp;<input type="button" name="submitss" value="�޸ĵȼ�" onclick="batchEditAlarmLevel();">&nbsp;&nbsp;</td>
							<td width="15" height=15>&nbsp;&nbsp;</td>
							</tr></table></td>
						</tr>
                    <tr> 
                      <td>
  <table cellSpacing="0"  cellPadding="0" border=0 height=28>
	
  <tr bgcolor="#ECECEC" height=28>
    	<td class="detail-data-body-title" align="center"><INPUT type="checkbox" name="checkall" onclick="javascript:chkall()"></td>
        <td width="10%" class="detail-data-body-title" align="center"><strong>�¼��ȼ�</strong></td>
    	<td width="40%" class="detail-data-body-title" align="center"><strong>�¼�����</strong></td>
		<td class="detail-data-body-title" align="center"><strong>�Ǽ�����</strong></td>
    	<td class="detail-data-body-title" align="center"><strong>�Ǽ���</strong></td>
    	<td class="detail-data-body-title" align="center"><strong>����״̬</strong></td>
    	<td class="detail-data-body-title" align="center"><strong>����</strong></td>
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
  	String bgcolor = "";
  	String act="������";
  	if("0".equals(level)){
  		level="��ʾ��Ϣ";
  		bgcolor = "bgcolor='blue'";
  	}
	if("1".equals(level)){
  		level="��ͨ�澯";
  		bgcolor = "bgcolor='yellow'";
  	}
  	if("2".equals(level)){
  		level="���ظ澯";
  		bgcolor = "bgcolor='orange'";
  	}
  	if("3".equals(level)){
  		level="�����澯";
  		bgcolor = "bgcolor='red'";
  	}
   	String bgcolorstr="";
  	if("0".equals(status)){
  		status = "δ����";
  		bgcolorstr="#9966FF";
  	}
  	if("1".equals(status)){
  		status = "������";  
  		bgcolorstr="#3399CC";	
  	}
  	if("2".equals(status)){
  	  	status = "�������";
  	  	bgcolorstr="#33CC33";
  	}
  	String rptman = eventlist.getReportman();
  	String rtime1 = _sdf.format(cc);
%>

 <tr  bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=28>

      <td class="detail-data-body-list"><INPUT type="checkbox" name="checkbox" value="<%=eventlist.getId()%>"><%=i+1%></td>
   	  <td class="detail-data-body-list" <%=bgcolor%>><%=level%></td>  
      <td class="detail-data-body-list" align="center">
      <%=content%></td>
       <td class="detail-data-body-list" align="center">
      <%=rtime1%></td>
       <td class="detail-data-body-list" align="center">
      <%=rptman%></td>
       <td class="detail-data-body-list" align="center"  bgcolor=<%=bgcolorstr%>>
      <%=status%></td>
       <td class="detail-data-body-list" align="center">
       <%
       		if("0".equals(s)){
       		%>
       			<input type ="button" value="���ܴ���" class="button" onclick='window.open("<%=rootPath%>/alarm/event/accitevent.jsp?eventid=<%=eventid%>","accEventWindow", "toolbar=no,height=400, width= 500, top=200, left= 200,scrollbars=no"+"screenX=0,screenY=0")'>
       		<%
       		}
       		if("1".equals(s)){
       		%>
       			<input type ="button" value="��д����" class="button" onclick='window.open("<%=rootPath%>/alarm/event/accitevent.jsp?eventid=<%=eventid%>","accEventWindow", "toolbar=no,height=400, width= 700, top=200, left= 200,scrollbars=no"+"screenX=0,screenY=0")'>
       		<%
       		}
       		if("2".equals(s)){
       		%>
       			<input type ="button" value="�鿴����" class="button" onclick='window.open("<%=rootPath%>/alarm/event/accitevent.jsp?eventid=<%=eventid%>","accEventWindow", "toolbar=no,height=400, width= 700, top=200, left= 200,scrollbars=no"+"screenX=0,screenY=0")'>
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
											    </td>
											  </tr>
											</table>
											
										</td>
									</tr>
								</table>
							</td>
							<td class="td-container-main-tool" width=15%>
							
							
								<jsp:include page="/include/toolbar.jsp">
									<jsp:param value="<%=host.getIpAddress()%>" name="ipaddress"/>
									<jsp:param value="<%=host.getSysOid()%>" name="sys_oid"/>
									<jsp:param value="<%=tmp%>" name="tmp"/>
									<jsp:param value="host" name="category"/>
									<jsp:param value="solaris" name="subtype"/>
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