<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.detail.service.interfaceInfo.InterfaceInfoService"%>
<%@page import="com.afunms.detail.service.systemInfo.SystemInfoService"%>

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
<%@ page import="org.apache.commons.beanutils.BeanUtils"%>
<%@ page import="com.afunms.detail.service.OtherInfo.OtherInfoService"%> 
<%@page import="com.afunms.detail.service.cpuInfo.CpuInfoService"%>
<%@page import="com.afunms.detail.service.diskInfo.DiskInfoService"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%
	String runmodel = PollingEngine.getCollectwebflag(); 
	String rootPath = request.getContextPath(); 
  	String menuTable = (String)request.getAttribute("menuTable");
  	String tmp = request.getParameter("id"); 
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String _flag = (String)request.getAttribute("flag");
    String sorttype = request.getParameter("sorttype");
   	if(sorttype == null || sorttype.trim().length() == 0){
   		sorttype = "desc";
   	}
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
	
	Hashtable pagehash = new Hashtable();
	Hashtable paginghash = new Hashtable();
	int pageingused = 0;
	String totalpageing = "";
	Hashtable hostinfohash = new Hashtable();
    List networkconfiglist = new ArrayList();
    List cpulist = new ArrayList();
    Nodeconfig nodeconfig = new Nodeconfig();  
    String processornum = "";
    Supper supper = null;
	String suppername = "";
	int percent1 = 0;
	int percent2 = 0;
	int cpuper = 0;
	String ipaddress = "";
	Vector ifvector = new Vector(); 	
	
	Hashtable hash = new Hashtable();
	Hashtable hash1 = new Hashtable();
	Hashtable max = new Hashtable();
	Hashtable imgurl = new Hashtable();
	double avgpingcon = 0.0;
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
	
   	Host host = new Host();
   	host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp));
   	String[] netInterfaceItem={"index","ifDescr","ifSpeed","ifOperStatus","OutBandwidthUtilHdx","InBandwidthUtilHdx"};
   	if("0".equals(runmodel)){
       	//采集与访问是集成模式
   		ipaddress = host.getIpAddress();
   		String orderflag = request.getParameter("orderflag");
   		if(orderflag == null || orderflag.trim().length()==0){
   			orderflag="index";
   		}
		
        I_HostLastCollectData hostlastmanager=new HostLastCollectDataManager();
       
		Vector vector = new Vector();
		
		try{
			vector = hostlastmanager.getInterface_share(host.getIpAddress(),netInterfaceItem,orderflag,starttime,endtime); 
		}catch(Exception e){
			e.printStackTrace();
		}
		//排序 
		vector = CommonUtil.sortInterface(vector, orderflag, sorttype, netInterfaceItem);
		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
		if(ipAllData != null){
			nodeconfig= (Nodeconfig)ipAllData.get("nodeconfig");
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

		hash = (Hashtable)request.getAttribute("hash");
		hash1 = (Hashtable)request.getAttribute("hash1");
		max = (Hashtable) request.getAttribute("max");
		imgurl = (Hashtable)request.getAttribute("imgurl");
		avgpingcon = (Double)request.getAttribute("pingconavg");
		ifvector = (Vector)request.getAttribute("vector"); 
			
		percent1 = Double.valueOf(avgpingcon).intValue();
		percent2 = 100-percent1;
		
		cpuper = Double.valueOf(cpuvalue).intValue();
		
	  	
	  
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
     	SupperDao supperdao = new SupperDao();
    	try{
    		supper = (Supper)supperdao.findByID(host.getSupperid()+"");
    		if(supper != null)suppername = supper.getSu_name()+"("+supper.getSu_dept()+")";
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		supperdao.close();
    	} 
    }else{//采集与访问是分离模式
   		ipaddress = host.getIpAddress();
   		String orderflag = request.getParameter("orderflag");
   		if(orderflag == null || orderflag.trim().length()==0){
   			orderflag="index";
   		}
        I_HostLastCollectData hostlastmanager=new HostLastCollectDataManager();
        NodeUtil nodeUtil = new NodeUtil();
        NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(host); 
        InterfaceInfoService interfaceInfoService = new InterfaceInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype());
		Vector vector = new Vector();
		try{
			//vector = hostlastmanager.getInterface_share(host.getIpAddress(),netInterfaceItem,orderflag,starttime,endtime); 
			vector = interfaceInfoService.getInterfaceInfo(netInterfaceItem); 
		}catch(Exception e){  
			e.printStackTrace();  
		}
        
		//Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
		//if(ipAllData != null){
			//nodeconfig= (Nodeconfig)ipAllData.get("nodeconfig");
			NodeconfigDao nodeconfigDao = new NodeconfigDao();
			try{
				nodeconfig = nodeconfigDao.getByNodeID(host.getId()+"");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				nodeconfigDao.close();
			}
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
			CpuInfoService cpuInfoService = new CpuInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype()); 
			//cpuperflist = cpuInfoService.getCpuPerListInfo();
			Vector cpuV = cpuInfoService.getCpuInfo();  
			if(cpuV != null && cpuV.size()>0){
				CPUcollectdata cpu = (CPUcollectdata)cpuV.get(0);
				if(cpu != null && cpu.getThevalue() != null){
					cpuvalue = new Double(cpu.getThevalue());
				}
			}
			//得到数据采集时间
			//collecttime = (String)ipAllData.get("collecttime");
			OtherInfoService otherInfoService = new OtherInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype()); 
			collecttime = otherInfoService.getCollecttime(); 
			//pagehash = (Hashtable)ipAllData.get("pagehash");
			
			paginghash = otherInfoService.getPaginghash(); 
			
			//paginghash = (Hashtable)ipAllData.get("paginghash");
			if(paginghash == null)paginghash = new Hashtable();
			if(paginghash.get("Total_Paging_Space") != null){
				pageingused = Integer.parseInt(((String)paginghash.get("Percent_Used")).replaceAll("%",""));
				totalpageing = (String)paginghash.get("Total_Paging_Space");
			}
		
			//得到系统启动时间 
			//Vector systemV = (Vector)ipAllData.get("system");
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
		//}
		//排序
		vector = CommonUtil.sortInterface(vector, orderflag, sorttype, netInterfaceItem);	
		
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

		hash = (Hashtable)request.getAttribute("hash");
		hash1 = (Hashtable)request.getAttribute("hash1");
		max = (Hashtable) request.getAttribute("max");
		imgurl = (Hashtable)request.getAttribute("imgurl");
		avgpingcon = (Double)request.getAttribute("pingconavg");
		ifvector = (Vector)request.getAttribute("vector"); 
			
		percent1 = Double.valueOf(avgpingcon).intValue();
		percent2 = 100-percent1;
		
		cpuper = Double.valueOf(cpuvalue).intValue();
		
	  	
	  
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
    	
   String ip = host.getIpAddress();
    	//CommonUtil commutil = new CommonUtil();
	String picip = CommonUtil.doip(ip);
    	//生成当天平均连通率图形
		CreatePiePicture _cpp = new CreatePiePicture();
	        TitleModel _titleModel = new TitleModel();
	        _titleModel.setXpic(150);
	        _titleModel.setYpic(150);//160, 200, 150, 100
	        _titleModel.setX1(75);//外环向左的位置
	        _titleModel.setX2(60);//外环向上的位置
	        _titleModel.setX3(65);
	        _titleModel.setX4(30);
	        _titleModel.setX5(75);
	        _titleModel.setX6(70);
	        _titleModel.setX7(10);
	        _titleModel.setX8(115);
	        _titleModel.setBgcolor(0xffffff);
	        _titleModel.setPictype("png");
	        _titleModel.setPicName(picip+"pingavg");
	        _titleModel.setTopTitle("");
	        
	        double[] _data1 = {avgpingcon, 100-avgpingcon};
	        //double[] _data2 = {77, 87};
	        String[] p_labels = {"连通", "未连通"};
	        int[] _colors = {0x66ff66, 0xff0000}; 
	        String _title1="第一季度";
	        String _title2="第二季度";
	        _cpp.createOneRingChart(_data1,p_labels,_colors,_titleModel);
	        
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
				 $("#pingavg").attr({ src: "<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>pingavg.png?nowtime="+(new Date()), alt: "平均连通率" });
				$("#cpu").attr({ src: "<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>cpu.png?nowtime="+(new Date()), alt: "当前CPU利用率" }); 
			
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
  	location.href="<%=rootPath%>/detail/host_aixutilhdx.jsp?id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&flag=<%=_flag%>&orderflag="+para;
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
	document.getElementById('aixDetailTitle-1').className='detail-data-title';
	document.getElementById('aixDetailTitle-1').onmouseover="this.className='detail-data-title'";
	document.getElementById('aixDetailTitle-1').onmouseout="this.className='detail-data-title'";
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
			<table border="1" width="100%" height="100%" bgcolor="#F1F1F1" style="border: thin; font-size: 12px" cellspacing="0">
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
											<td valign=top>
												<jsp:include page="/topology/includejsp/systeminfo_hostaix.jsp">
													<jsp:param name="rootPath" value="<%=rootPath%>" />
													<jsp:param name="tmp" value="<%=tmp%>" />
													<jsp:param name="picip" value="<%=picip%>" />
													<jsp:param name="hostname" value="<%=hostname%>" />
													<jsp:param name="sysname" value="<%=sysname%>" />
													<jsp:param name="CSDVersion" value="<%=CSDVersion%>" />
													<jsp:param name="processornum" value="<%=processornum%>" />
													<jsp:param name="collecttime" value="<%=collecttime%>" />
													<jsp:param name="sysuptime" value="<%=sysuptime%>" />
													<jsp:param name="mac" value="<%=mac%>" />
													<jsp:param name="pageingused" value="<%=pageingused%>" />
													<jsp:param name="totalpageing" value="<%=totalpageing%>" />
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
																	<td align=center>
																		<table width="100%" border="0" cellpadding="3" cellspacing="1" bgcolor="#ECECEC">
																			<tr align="center">
																				<td rowspan="2" width="6%" align="center">
																					索引
																				</td>
																				<td rowspan="2" width="9%" align="center">
																					描述
																				</td>
																				<td rowspan="2" width="9%" align="center">
																					关联应用
																				</td>
																				<td rowspan="2" width="11%" align="center">
																					每秒字节数(M)
																				</td>
																				<td rowspan="2" width="9%" align="center">
																					当前状态
																				</td>
																				<td height="23" colspan="2" align="center">
																					流速
																				</td>
																				<td rowspan="2" align="center">
																					查看详情
																				</td>
																			</tr>
																			<tr>
																				<td width="15%" align="center">
																					<input type=button name="button3" styleClass="button" value="出口流速" onclick="changeOrder('OutBandwidthUtilHdx')">
																				</td>
																				<td width="15%" align="center">
																					<input type=button name="button4" styleClass="button" value="入口流速" onclick="changeOrder('InBandwidthUtilHdx')">
																				</td>
																			</tr>
																		<%
																			int[] width = { 6, 18, 11, 15, 15, 15, 15 };
																			for (int i = 0; i < ifvector.size(); i++) {
																				String[] strs = (String[]) ifvector.get(i);
																				String ifname = strs[1];
																				String index = strs[0];
																		%>
																			<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=28>
																		<%
																				for (int j = 0; j < strs.length; j++) {
																					if (j == 3) {
																						String status = strs[j];
																						String url = "";
																						if (status.equals("1")) {
																							url = rootPath + "/resource/image/topo/up.gif";
																						} else if (status.equals("2")) {
																							url = rootPath + "/resource/image/topo/down.gif";
																						} else {
																							url = rootPath + "/resource/image/topo/testing.gif";
																						}
																		%>
																				<td width="<%=width[j]%>%" align="center"
																					class="detail-data-body-list" align="center">
																					<img src="<%=url%>">
																				</td>
																		<%
																					} else {
																						if (j == 1) {
																							if (hash != null && hash.size() > 0) {
																								String linkuse = "";
																								if (hash.get(ipaddress + ":" + index) != null)
																									linkuse = (String) hash.get(ipaddress + ":"
																											+ index);
																		%>
																				<td width="<%=width[j] / 2%>%" class="detail-data-body-list" align="center"><%=strs[j]%></td>
																				<td width="<%=width[j] / 2%>%" class="detail-data-body-list" align="center"><%=linkuse%></td>
																		<%
																							} else {
																		%>
																				<td width="<%=width[j] / 2%>%" class="detail-data-body-list" align="center"><%=strs[j]%></td>
																				<td width="<%=width[j] / 2%>%" class="detail-data-body-list" align="center"></td>
																		<%
																							}
																						} else {
																		%>
																				<td width="<%=width[j]%>%" class="detail-data-body-list" align="center"><%=strs[j]%></td>
																		<%
																						}
																					}
																				}
																		%>
																				<td width="9%" align="center" class="detail-data-body-list" align="center">
																					&nbsp;
																					<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('2','<%=index%>','111')>
																				</td>
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
								<td class="td-container-main-tool" width=15%>
									<jsp:include page="/include/aixtoolbar.jsp">
										<jsp:param value="<%=host.getIpAddress()%>" name="ipaddress" />
										<jsp:param value="<%=host.getSysOid()%>" name="sys_oid" />
										<jsp:param value="<%=tmp%>" name="tmp" />
										<jsp:param value="host" name="category" />
										<jsp:param value="aix" name="subtype" />
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