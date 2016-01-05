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
<%@ page import="com.afunms.temp.model.NodeTemp"%>
<%@page import="com.afunms.detail.service.cpuInfo.CpuInfoService"%>
<%@page import="com.afunms.detail.service.diskInfo.DiskInfoService"%>
<%@page import="com.afunms.detail.service.OtherInfo.OtherInfoService"%>
<%@page import="com.afunms.detail.service.systemInfo.SystemInfoService"%>
<%@page import="com.afunms.detail.service.sysInfo.DiskPerfInfoService"%>
<%@page import="com.afunms.detail.service.sysInfo.CpuPerfInfoService"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.polling.manage.PollMonitorManager"%>
<%@page import="com.afunms.temp.dao.*"%>


<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%@ page import="org.apache.commons.beanutils.BeanUtils"%>
<%
	String runmodel = PollingEngine.getCollectwebflag(); 
	String rootPath = request.getContextPath();
  	String menuTable = (String)request.getAttribute("menuTable");
	String[] diskItem={"AllSize","UsedSize","Utilization"};
	String[] diskItemch={"总容量","已用容量","利用率"};
	String[] memoryItem={"Capability","Utilization"};
	String[] memoryItemch={"内存容量","当前利用率","最大利用率","平均利用率"};
	
	Hashtable max = (Hashtable) request.getAttribute("max");
	Hashtable memmaxhash = (Hashtable) request.getAttribute("memmaxhash");
	Hashtable memavghash = (Hashtable) request.getAttribute("memavghash");
	Hashtable diskhash = (Hashtable) request.getAttribute("diskhash");
	Hashtable memhash = (Hashtable) request.getAttribute("memhash");
	Hashtable diskHashtable = new Hashtable();
	String avgcpu = (String)max.get("cpuavg");
	String cpumax = (String)max.get("cpu");
  	String tmp = request.getParameter("id"); 
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	DecimalFormat df=new DecimalFormat("#.##");
	double cpuvalue = 0;
	String pingconavg ="0";
	String collecttime = null;
	String sysuptime = null;
	String sysservices = null;
	String sysdescr = null;
	
	String hostname = "";
	String porcessors = "";
	String sysname = "";
	String SerialNumber = "";
	String CSDVersion = "";
	String mac = "";
	String processornum = "";
	Supper supper = null;
   	String suppername = "";
	int pageingused = 0;
	String totalpageing = "";
	int percent1 = 0;
	int percent2 = 0;
	int cpuper = 0;
	
	Hashtable hostinfohash = new Hashtable();
    	List networkconfiglist = new ArrayList();
    	List cpuperflist = new ArrayList();
    	NodeTemp cpunode = new NodeTemp();
    	List alldiskperf = new ArrayList();
    	List alldiskio = new ArrayList();
    	Hashtable pagehash = new Hashtable();
    	Hashtable paginghash = new Hashtable();
    
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String _time1 = sdf.format(new Date());
									
		
	String starttime1 = _time1 + " 00:00:00";
	String totime1 = _time1 + " 23:59:59";
	
    	Host host = new Host();
    	host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp));
    	String newip=SysUtil.doip(host.getIpAddress());
   	if("0".equals(runmodel)){
       		//采集与访问是集成模式
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
        
		try {
			diskHashtable = hostlastmanager.getDisk_share(host.getIpAddress(), "Disk", starttime1,totime1);
		} catch (Exception e) {
			e.printStackTrace();
		}        
		
        
	        List cpulist = new ArrayList();
	        Nodeconfig nodeconfig = new Nodeconfig();
        
		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
		if(ipAllData != null){
			nodeconfig= (Nodeconfig)ipAllData.get("nodeconfig");
			if(nodeconfig != null){
				mac = nodeconfig.getMac();
				processornum = nodeconfig.getNumberOfProcessors();
				CSDVersion = nodeconfig.getCSDVersion();
				hostname = nodeconfig.getHostname();
			}
			
			cpuperflist = (List)ipAllData.get("cpuperflist");
			
			alldiskperf = (List)ipAllData.get("alldiskperf");
			if(alldiskperf == null)alldiskperf = new ArrayList();
			alldiskio = (List)ipAllData.get("alldiskio");
			if(alldiskio == null)alldiskio = new ArrayList();
			Vector cpuV = (Vector)ipAllData.get("cpu");
			if(cpuV != null && cpuV.size()>0){
				CPUcollectdata cpu = (CPUcollectdata)cpuV.get(0);
				if(cpu != null && cpu.getThevalue() != null){
					cpuvalue = new Double(cpu.getThevalue());
				}
			}

			//得到数据采集时间
			collecttime = (String)ipAllData.get("collecttime");
			
			pagehash = (Hashtable)ipAllData.get("pagehash");
			
			paginghash = (Hashtable)ipAllData.get("paginghash");
			if(paginghash == null)paginghash = new Hashtable();
			if(paginghash.get("Total_Paging_Space") != null){
				pageingused = Integer.parseInt(((String)paginghash.get("Percent_Used")).replaceAll("%",""));
				totalpageing = (String)paginghash.get("Total_Paging_Space");
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
					if(systemdata.getSubentity().equalsIgnoreCase("SysName")){
						sysname = systemdata.getThevalue();
					}
				}
			}
		}
		

			
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
        	request.setAttribute("id", tmp);
        	request.setAttribute("ipaddress", host.getIpAddress());
		request.setAttribute("cpuvalue", cpuvalue);
		request.setAttribute("collecttime", collecttime);
		request.setAttribute("sysuptime", sysuptime);
		request.setAttribute("sysservices", sysservices);
		request.setAttribute("sysdescr", sysdescr);
		request.setAttribute("pingconavg", new Double(pingconavg));
	
		String[] sysItem={"sysName","sysUpTime","sysContact","sysLocation","sysServices","sysDescr"};
		String[] sysItemch={"设备名","设备启动时间","设备联系","设备位置","设备服务","设备描述"};
	
		Hashtable hash = (Hashtable)request.getAttribute("hash");
		Hashtable hash1 = (Hashtable)request.getAttribute("hash1");
		Hashtable imgurl = (Hashtable)request.getAttribute("imgurl");
	
		double avgpingcon = (Double)request.getAttribute("pingconavg");
	
		percent1 = Double.valueOf(avgpingcon).intValue();
		percent2 = 100-percent1;
		
		cpuper = Double.valueOf(cpuvalue).intValue();	
     	
     	
     	
	      	SupperDao supperdao = new SupperDao();
	    	
	    	try{
	    		supper = (Supper)supperdao.findByID(host.getSupperid()+"");
	    		if(supper != null)suppername = supper.getSu_name()+"("+supper.getSu_dept()+")";
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}finally{
	    		supperdao.close();
	    	} 
	}else{
	    	//采集与访问是分离模式
	    	NodeUtil nodeUtil = new NodeUtil();
	        NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(host); 
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
        
	        List cpulist = new ArrayList();
	        Nodeconfig nodeconfig = new Nodeconfig();
		NodeconfigDao nodeconfigDao = new NodeconfigDao();
		try{
			nodeconfig = nodeconfigDao.getByNodeID(host.getId()+"");
		}catch(Exception e){
		}finally{
			nodeconfigDao.close();
		}
		if(nodeconfig != null){
			mac = nodeconfig.getMac();
			processornum = nodeconfig.getNumberOfProcessors();
			CSDVersion = nodeconfig.getCSDVersion();
			hostname = nodeconfig.getHostname();
		}
		
		CpuPerfInfoService cpuPerfInfoService = new CpuPerfInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype());
		cpuperflist = cpuPerfInfoService.getCpuPerListInfo();  
			
		DiskPerfInfoService diskInfoService = new DiskPerfInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype());
		alldiskperf = diskInfoService.getDiskperflistInfo(); 
		if(alldiskperf == null)alldiskperf = new ArrayList();
			
			
		//取出当前的硬盘信息 
		DiskInfoService _diskInfoService = new DiskInfoService(String.valueOf(nodedto.getId()),nodedto.getType(),nodedto.getSubtype());//1
		diskHashtable = _diskInfoService.getCurrDiskListInfo();			//1
			
		//alldiskio = (List)ipAllData.get("alldiskio");//无diskio信息
		if(alldiskio == null)alldiskio = new ArrayList();
		
		CpuInfoService cpuInfoService = new CpuInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype());
		Vector cpuV = cpuInfoService.getCpuInfo();        
		if(cpuV != null && cpuV.size()>0){
			CPUcollectdata cpu = (CPUcollectdata)cpuV.get(0);
			if(cpu != null && cpu.getThevalue() != null){
				cpuvalue = new Double(cpu.getThevalue());
			}
		}
		//得到数据采集时间
		OtherInfoService otherInfoService = new OtherInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype());
		collecttime = otherInfoService.getCollecttime();
		pagehash = (Hashtable)otherInfoService.getPagehash();
			
		paginghash = (Hashtable)otherInfoService.getPaginghash();
		if(paginghash == null)paginghash = new Hashtable();
		if(paginghash.get("Total_Paging_Space") != null){
			pageingused = Integer.parseInt(((String)paginghash.get("Percent_Used")).replaceAll("%",""));
			totalpageing = (String)paginghash.get("Total_Paging_Space");
		}
			
			//得到系统启动时间
			SystemInfoService systemInfoService = new SystemInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype());
			Vector systemV = systemInfoService.getSystemInfo();
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
        	request.setAttribute("id", tmp);
        	request.setAttribute("ipaddress", host.getIpAddress());
		request.setAttribute("cpuvalue", cpuvalue);
		request.setAttribute("collecttime", collecttime);
		request.setAttribute("sysuptime", sysuptime);
		request.setAttribute("sysservices", sysservices);
		request.setAttribute("sysdescr", sysdescr);
		request.setAttribute("pingconavg", new Double(pingconavg));
	
		PollMonitorManager pollMonitorManager = new PollMonitorManager(); 
		newip=SysUtil.doip(host.getIpAddress());
		//画图（磁盘是显示最新数据的柱状图）
		try{
			pollMonitorManager.draw_column(diskHashtable,"",newip+"disk",750,150);
		}catch(Exception e){
			e.printStackTrace();
		}		
	
		String[] sysItem={"sysName","sysUpTime","sysContact","sysLocation","sysServices","sysDescr"};
		String[] sysItemch={"设备名","设备启动时间","设备联系","设备位置","设备服务","设备描述"};
	
		Hashtable hash = (Hashtable)request.getAttribute("hash");
		Hashtable hash1 = (Hashtable)request.getAttribute("hash1");
		Hashtable imgurl = (Hashtable)request.getAttribute("imgurl");
	
		double avgpingcon = (Double)request.getAttribute("pingconavg");
	
		percent1 = Double.valueOf(avgpingcon).intValue();
		percent2 = 100-percent1;
		
		cpuper = Double.valueOf(cpuvalue).intValue();
		
	  	 
	  
		Vector ipmacvector = (Vector)request.getAttribute("vector");
		if (ipmacvector == null)ipmacvector = new Vector();  	   	
     	
	      	SupperDao supperdao = new SupperDao();	    	
	    	try{
	    		supper = (Supper)supperdao.findByID(host.getSupperid()+"");
	    		if(supper != null)suppername = supper.getSu_name()+"("+supper.getSu_dept()+")";
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}finally{
	    		supperdao.close();
	    	} 
	}
	Hashtable Disk = diskHashtable;	
	double avgpingcon = new Double(pingconavg);
    String ip = host.getIpAddress();
	String picip = CommonUtil.doip(ip);
    	
	   //生成CPU仪表盘
		CreateMetersPic cmp = new CreateMetersPic();
		MeterModel mm = new MeterModel();
		mm.setBgColor(0xffffff);
		mm.setInnerRoundColor(0xececec);
		mm.setOutRingColor(0x80ff80);
		mm.setTitle("CPU利用率");
		mm.setPicx(150);//
		mm.setPicy(150);//
		mm.setMeterX(80);//
		mm.setMeterY(80);//
		mm.setPicName(picip+"cpu");//
		mm.setValue(cpuper);//
		mm.setMeterSize(60);//设置仪表盘大小
		mm.setTitleY(79);//设置标题离左边距离
		mm.setTitleTop(122);//设置标题离顶部距离
		mm.setValueY(78);//设置值离左边距离
		mm.setValueTop(105);//设置值离顶部距离
		mm.setOutPointerColor(0x80ff80);//设置指针外部颜色
		mm.setInPointerColor(0x8080ff);//设置指针内部颜色
		mm.setFontSize(10);//设置字体大小
		List<StageColor> sm = new ArrayList<StageColor>();
		StageColor sc1 = new StageColor();
		sc1.setColor(0x99ff99);
		sc1.setStart(0);
		sc1.setEnd(60);
		StageColor sc2 = new StageColor();
		sc2.setColor(0xffff00);
		sc2.setStart(60);
		sc2.setEnd(80);
		StageColor sc3 = new StageColor();
		sc3.setColor(0xff3333);
		sc3.setStart(80);
		sc3.setEnd(100);
		sm.add(sc1);
		sm.add(sc2);
		sm.add(sc3);
		mm.setList(sm);
		cmp.createSimpleMeter(mm);	
		 //磁盘利用率 amcharts wxy add
		CreateAmColumnPic aixColumnPic=new CreateAmColumnPic();	
		String valueStr=aixColumnPic.createDiskChart(diskHashtable);		
			  	   
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>


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
function gzmajax(){
$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=ajaxUpdate_availability&tmp=<%=tmp%>&nowtime="+(new Date()),
			success:function(data){
				$("#flashcontent00gzm").html(data.percent1+":"+data.percent2+":"+data.cpuper);
				var so = new SWFObject("<%=rootPath%>/flex/Pie_Component.swf?percent1="+data.percent1+"&percentStr1=可用&percent2="+data.percent2+"&percentStr2=不可用", "Pie_Component", "160", "160", "8", "#ffffff");
				so.write("flashcontent00");
				var so1 = new SWFObject("<%=rootPath%>/flex/DHCCGauge.swf?percent="+data.cpuper, "Pie_Component1", "160", "160", "8", "#ffffff");
				so1.write("flashcontent01");
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
  	location.href="<%=rootPath%>/detail/host_linuxutilhdx.jsp?id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&orderflag="+para;
} 
function openwin3(operate,index,ifname) 
{	
        window.open ("<%=rootPath%>/monitor.do?action="+operate+"&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&ifindex="+index+"&ifname="+ifname, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes") 
} 

function reportByMinute(){
	window.open("<%=rootPath%>/monitor.do?action=hostping_report&id=<%=tmp%>&timeType=minute","newWindow","height=300, width=800, top=0, left= 200,screenX=0,screenY=0");
  }
  
 
  
// 全屏观看
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
}

function setClass(){
	document.getElementById('aixDetailTitle-2').className='detail-data-title';
	document.getElementById('aixDetailTitle-2').onmouseover="this.className='detail-data-title'";
	document.getElementById('aixDetailTitle-2').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		document.getElementById("id").value="<%=tmp%>";
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}

//网络设备的ip地址
function modifyIpAliasajax(ipaddress){
	var t = document.getElementById('ipalias'+ipaddress);
	var ipalias = t.options[t.selectedIndex].text;//获取下拉框的值
	$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=modifyIpAlias&ipaddress="+ipaddress+"&ipalias="+ipalias,
			success:function(data){
				window.alert("修改成功！");
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
	<div id="itemMenu" style="display: none";>
		<table border="1" width="100%" height="100%" bgcolor="#F1F1F1" style="border: thin;font-size: 12px" cellspacing="0">
			<tr>
				<td style="cursor: default; border: outset 1;" align="center" onclick="parent.detail()">查看状态</td>
			</tr>
			<tr>
				<td style="cursor: default; border: outset 1;" align="center" onclick="parent.portset();">端口设置</td>
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
							<td><%=menuTable%></td>	
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
											<jsp:include page="/topology/includejsp/systeminfo_hostaix.jsp">
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
												 <jsp:param name="pageingused" value="<%= pageingused %>"/> 
												 <jsp:param name="totalpageing" value="<%= totalpageing %>"/> 
											 </jsp:include>	
										</td>
									</tr>
									<tr>
										<td>
											<table id="detail-data" class="detail-data">
												<tr>
											    	<td class="detail-data-header"><%=aixDetailTitleTable%></td>
											  	</tr>
											  	<tr>
											    	<td>
														<table class="detail-data-body">
															<tr>
																<td align=center colspan=2 style="width:80%">
																	<br>
                   													<table style="width:98%" border=1>
														            	<tr>
															    			<td width=80%>
																				<table width="80%">
														                			<tr>
																	                	<td align="left" height=40 bgcolor="#ECECEC">&nbsp;<B>文件系统利用率</B></td>											   
																					</tr>
															       				</table>
														       				</td>
														       			</tr>
																     	<tr>
																       		<td width="100%">
																       			<table width="100%">
				                   													<tr>
																						<td align=center>
																						<div id='softDisk'></div>
																						<!--   	<img src="<%=rootPath%>/resource/image/jfreechart/<%=newip %>disk.png"/> -->
																						 <script type="text/javascript">
	                                                                                      <% if(!valueStr.equals("0")){%>
	                                                                                          var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "amcolumn","742", "290", "8", "#FFFFFF");
				                                                                                   so.addVariable("path", "<%=rootPath%>/amchart/");
				                                                                                   so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/dbpercent_settings.xml"));
				                                                                                   so.addVariable("chart_data","<%=valueStr%>");
				                                                                                   so.write("softDisk");
				                                                                                    <%}else{%>
				                                                                               var _div=document.getElementById("softDisk");
												                                               var img=document.createElement("img");
													                                               img.setAttribute("src","<%=rootPath%>/resource/image/nodata.png");
													                                               _div.appendChild(img);
				                                                                               <%}%>
	                                                                                          </script>
																						</td>
																					</tr> 
				                    												<tr>
				                  												  		<td align=center width=100%>
																            		    	<br>
																                	    	<table cellpadding="0" cellspacing="0">
												              									<tr> 
												                							    	<td align=center width=100%> 
												                								   		<div id="loading">
																										<div class="loading-indicator">
																											<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif" width="32" height="32" style="margin-right: 8px;" align="middle" />Loading...</div>
																										</div>
																	                                   	<table border="1" align=center>
																											<tr>
																												<td class="detail-data-body-title" style="height:29">文件系统名</td>
																												<td class="detail-data-body-title">总容量</td>
																												<td class="detail-data-body-title">已用容量</td>
																												<td class="detail-data-body-title">利用率</td>
																											</tr>
																										<%
																										if (Disk != null && Disk.size() > 0) {
																											// 写磁盘
																											for (int i = 0; i < Disk.size(); i++) {
																									    %>
																								            <tr>
																												<%
																												Hashtable _diskhash = (Hashtable) (Disk.get(new Integer(i)));
																												String name = (String) _diskhash.get("name");
																												%>
																									            <td class="detail-data-body-list" style="height:29">&nbsp;<%=name %></td>
																												<%
																												for (int j = 0; j < diskItem.length; j++) {
																													String value = "";
																													if (_diskhash.get(diskItem[j]) != null) {
																														value = (String) _diskhash.get(diskItem[j]);
																													}
																												%>
																									            <td class="detail-data-body-list">&nbsp;<%=value %></td>
																									            <%
																								                }
																								                %>
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
												            <tr>
													            <td align=center colspan=2>
													                <br>
													                <table cellpadding="0" cellspacing="0" width=92%>
													         		    <tr> 
													           			    <td width="100%"  align=center> 
													           				    <div id="flashcontent6">
																					<strong>You need to upgrade your Flash Player</strong>
																				</div>
																				<script type="text/javascript">
																					var so = new SWFObject("<%=rootPath%>/flex/Area_Disk_month.swf?ipadress=<%=host.getIpAddress()%>&id=2", "Area_Disk_month", "830", "440", "8", "#ffffff");
																					so.write("flashcontent6");
																				</script>				
															                </td>
																        </tr>             
																	</table> 
													            </td>
												            </tr>													
														    <tr>
														    	<td height=10>&nbsp;</td>
														    </tr>								
														    <tr>
                                                                <td align=center style="width:98%">
														            <table style="width:98%" border="0" align="center" cellpadding="0" cellspacing="0">
														              	<tr> 
														                	<td valign="top">
													                			<table width="100%" height="1" border="0" cellpadding="0" cellspacing="0">
																                    <tr align="left" bgcolor="#ECECEC"> 
																                  		<td width="98%" height="23">&nbsp;<b>磁盘性能</b></td>
																                    </tr>
																            	    <tr> 
																              			<td width="100%" align="center"> 
														                					<table width="90%" cellpadding="0" cellspacing="0">
																	                  			<tr align="center" height="28"> 
																                    				<td width="10%" class="detail-data-body-title" align="center">磁盘名</td>
																                    				<td width="10%" class="detail-data-body-title" align="center">繁忙(%)</td>
																                    				<td width="10%" class="detail-data-body-title" align="center">平均深度</td>
																                    				<td width="10%" class="detail-data-body-title" align="center">读写块数/秒</td>
																                    				<td width="15%" class="detail-data-body-title" align="center">读写字节（K）/秒</td>
																                    				<td width="20%" class="detail-data-body-title" align="center">平均等待时间(ms)</td>
																                    				<td width="20%" class="detail-data-body-title" align="center">平均执行时间(ms)</td>
																	                  			</tr>
																			                <%
																			                 	if(alldiskperf != null && alldiskperf.size()>0){ 
																			                 		
																			      					for(int i=0;i<alldiskperf.size();i++){
																			          					Hashtable diskperhash = (Hashtable)alldiskperf.get(i);
																			     		 	%>
																			                 	<tr height=28 <%=onmouseoverstyle%>> 
																				                    <td  width="10%" class="detail-data-body-list" align="center">&nbsp;<%=(String)diskperhash.get("disklebel")%></td>
																				                    <td  width="10%" class="detail-data-body-list" align="center">&nbsp;<%=(String)diskperhash.get("%busy")%></td>
																				                    <td  width="10%" class="detail-data-body-list" align="center">&nbsp;<%=(String)diskperhash.get("avque")%></td>
																				                    <td  width="10%" class="detail-data-body-list" align="center">&nbsp;<%=(String)diskperhash.get("r+w/s")%></td>
																				                    <td  width="15%" class="detail-data-body-list" align="center">&nbsp;<%=(String)diskperhash.get("Kbs/s")%></td>
																				                    <td  width="20%" class="detail-data-body-list" align="center">&nbsp;<%=(String)diskperhash.get("avwait")%></td>
																				                    <td  width="20%" class="detail-data-body-list" align="center">&nbsp;<%=(String)diskperhash.get("avserv")%></td>
																		                    <%
																	                    			}
																		                    %>
																		                 	 	</tr>
																			      			<% 	
																			      				} 
																			      			%>
																			                </table>
																		              	</td>
																		            </tr>  
																		            <tr>
																					    <td height=10>&nbsp;</td>
																				    </tr> 
																		            <tr> 
																	              		<td width="100%" align="center"> 
																	                		<table width="90%" cellpadding="0" cellspacing="0">
																	                			<tr align="center" height="28"> 
																                    				<td width="10%" class="detail-data-body-title" align="center">磁盘名</td>
																                    				<td width="10%" class="detail-data-body-title" align="center">tm_act(%)</td>
																                    				<td width="10%" class="detail-data-body-title" align="center">Kbps</td>
																                    				<td width="10%" class="detail-data-body-title" align="center">tps</td>
																                    				<td width="15%" class="detail-data-body-title" align="center">读字节（K）/秒</td>
																                    				<td width="20%" class="detail-data-body-title" align="center">写字节（K）/秒</td>
																	                  			</tr>
																		                    <%
																			                 	if(alldiskio != null && alldiskio.size()>0){
																			      				for(int i=0;i<alldiskio.size();i++){
																			          				Hashtable diskiohash = (Hashtable)alldiskio.get(i);
																		     		 	    %>
																			                  	<tr height=28 <%=onmouseoverstyle%>> 
																				                    <td  width="10%" class="detail-data-body-list" align="center">&nbsp;<%=(String)diskiohash.get("Disks")%></td>
																				                    <td  width="10%" class="detail-data-body-list" align="center">&nbsp;<%=(String)diskiohash.get("%tm_act")%></td>
																				                    <td  width="10%" class="detail-data-body-list" align="center">&nbsp;<%=(String)diskiohash.get("Kbps")%></td>
																				                    <td  width="10%" class="detail-data-body-list" align="center">&nbsp;<%=(String)diskiohash.get("tps")%></td>
																				                    <td  width="15%" class="detail-data-body-list" align="center">&nbsp;<%=(String)diskiohash.get("kb_read")%>kb/s</td>
																				                    <td  width="20%" class="detail-data-body-list" align="center">&nbsp;<%=(String)diskiohash.get("kb_wrtn")%>kb/s</td>
																		                    <%
																			                    }
																		                    %>
																			                  	</tr>
																			      			<% 
																			      				} 
																	      					%>
														                					</table>
														              					</td>
														            				</tr>
																		            <tr>
																				    	<td height=10>&nbsp;</td>
																				    </tr>
																		            <tr align="left" bgcolor="#ECECEC"> 
																		                <td width="90%" height="23">&nbsp;<b>页面性能</b></td>
																		            </tr>
																	           	 	<tr> 
																		              	<td width="100%" align="center"> 
																		              	    <table width="90%" cellpadding="0" cellspacing="0">
																		                        <tr lign="center" height="28" bgcolor="#ECECEC"> 
																		                  	        <td width="25%"  class="detail-data-body-title" align="center">页面调度程序输入/输出列表</td>
																		                    	    <td width="20%"  class="detail-data-body-title" align="center">内存页面调进数</td>
																		                    	    <td width="20%"  class="detail-data-body-title" align="center">内存页面调出数</td>
																		                    	    <td width="14%"  class="detail-data-body-title" align="center">释放的页数</td>
																		                    	    <td width="13%"  class="detail-data-body-title" align="center">扫描的页</td>
																		                    		<td width="13%"  class="detail-data-body-title" align="center">时钟周期</td>
																		                        </tr>
														                 					<%
																				           		if(pagehash != null && pagehash.size()>0){
																				      		%>
																			                  	<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=28> 
																				                    <td  class="detail-data-body-list" align="center"><%=(String)pagehash.get("re")%></td>
																				                    <td  class="detail-data-body-list" align="center"><%=(String)pagehash.get("pi")%></td>
																				                    <td  class="detail-data-body-list" align="center"><%=(String)pagehash.get("po")%></td>
																				                    <td  class="detail-data-body-list" align="center"><%=(String)pagehash.get("fr")%></td>
																				                    <td  class="detail-data-body-list" align="center"><%=(String)pagehash.get("sr")%></td>
																				                    <td  class="detail-data-body-list" align="center"><%=(String)pagehash.get("cy")%></td>
																			                  	</tr>      		
														      								<% 
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
							</td> 
							<td class="td-container-main-tool" width=15%>   
								<jsp:include page="/include/aixtoolbar.jsp">
									<jsp:param value="<%=host.getIpAddress()%>" name="ipaddress"/>
									<jsp:param value="<%=host.getSysOid()%>" name="sys_oid"/>
									<jsp:param value="<%=tmp%>" name="tmp"/>
									<jsp:param value="host" name="category"/>
									<jsp:param value="aix" name="subtype"/>
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