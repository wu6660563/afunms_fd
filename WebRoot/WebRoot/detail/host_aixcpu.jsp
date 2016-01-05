<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.detail.service.sysInfo.DiskPerfInfoService"%>
<%@page import="com.afunms.detail.service.pingInfo.PingInfoService"%>

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
<%@page import="com.afunms.detail.service.cpuInfo.CpuInfoService"%>
<%@page import="com.afunms.detail.service.diskInfo.DiskInfoService"%>
<%@page import="com.afunms.detail.service.OtherInfo.OtherInfoService"%>
<%@page import="com.afunms.detail.service.systemInfo.SystemInfoService"%>
<%@page import="com.afunms.detail.service.interfaceInfo.InterfaceInfoService"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.polling.manage.PollMonitorManager"%>
<%@page import="com.afunms.temp.dao.*"%>
<%@page import="com.afunms.common.util.*"%>


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

	double cpuvalue = 0;
	String pingconavg ="0";
	String collecttime = "";
	String sysuptime = "";
	String sysservices = "";
	String sysdescr = "";
	int pageingused = 0;
	String totalpageing = "0";
	
	String maxpingvalue = "0"; 
	String responsevalue = "0";
		
	String avgresponse = "0";
	String maxresponse = "0";
	
	String hostname = "";
	String porcessors = "0";
	String sysname = "";
	String SerialNumber = "";
	String CSDVersion = "";
	String mac = "";
		
   	HostNodeDao hostdao = new HostNodeDao();
   	List hostlist = hostdao.loadHost();
   	List cpuperflist = new ArrayList();
   	Hashtable cpuperfhash = new Hashtable();
	Hashtable diskHashtable = new Hashtable();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String time1 = sdf.format(new Date());
	String starttime1 = time1 + " 00:00:00";
	String totime1 = time1 + " 23:59:59";
   	Hashtable memhash = (Hashtable) request.getAttribute("memhash");
   	
   	Vector deviceV = new Vector();
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
    
    Hashtable hostinfohash = new Hashtable();
    List networkconfiglist = new ArrayList();
    List cpulist = new ArrayList();
    
    Hashtable paginghash = new Hashtable();
    
    Nodeconfig nodeconfig = new Nodeconfig();
    String processornum = "0";
    String pingvalue= "0";
       
     if("0".equals(runmodel)){
    		//采集与访问是集成模式
		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
		if(ipAllData != null){
			nodeconfig= (Nodeconfig)ipAllData.get("nodeconfig");
			if(nodeconfig != null){
				mac = nodeconfig.getMac();
				processornum = nodeconfig.getNumberOfProcessors();
				CSDVersion = nodeconfig.getCSDVersion();
				hostname = nodeconfig.getHostname();
			}
			Vector cpuV = (Vector)ipAllData.get("cpu");
			if(cpuV != null && cpuV.size()>0){
				CPUcollectdata cpu = (CPUcollectdata)cpuV.get(0);
				if(cpu != null && cpu.getThevalue() != null){
					cpuvalue = new Double(cpu.getThevalue());
				}
			}
			//得到CPU详细信息
			cpuperflist = (List)ipAllData.get("cpuperflist");
			if(cpuperflist != null)cpuperfhash = (Hashtable)cpuperflist.get(0);
			
			//得到数据采集时间
			collecttime = (String)ipAllData.get("collecttime");
			
			paginghash = (Hashtable)ipAllData.get("paginghash");
			if(paginghash == null)paginghash = new Hashtable();
			if(paginghash.get("Total_Paging_Space") != null){
				pageingused = Integer.parseInt(((String)paginghash.get("Percent_Used")).replaceAll("%",""));
				totalpageing = (String)paginghash.get("Total_Paging_Space");
			}
			
			deviceV = (Vector)ipAllData.get("device");
			
			//得到系统启动时间
			Vector systemV = (Vector)ipAllData.get("system");
			if(systemV != null && systemV.size()>0){
				for(int i=0;i<systemV.size();i++){
					Systemcollectdata systemdata=(Systemcollectdata)systemV.get(i);
					//System.out.println(systemdata.getSubentity()+"------------");
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
		try {
			diskHashtable = hostlastmanager.getDisk_share(host.getIpAddress(), "Disk", starttime1,
					totime1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Vector pingData = (Vector)ShareData.getPingdata().get(host.getIpAddress());
		if(pingData != null && pingData.size()>0){
			Pingcollectdata pingdata = (Pingcollectdata)pingData.get(0);
			Calendar tempCal = (Calendar)pingdata.getCollecttime();							
			Date cc = tempCal.getTime();
			collecttime = sdf1.format(cc);
			pingvalue = pingdata.getThevalue();//当前连通率
			pingdata = (Pingcollectdata)pingData.get(1);
			responsevalue = pingdata.getThevalue();//当前响应时间
		}
	}else{
		//采集与访问是分离模式
	    	NodeUtil nodeUtil = new NodeUtil();
	        NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(host); 
	        NodeconfigDao nodeconfigDao = new NodeconfigDao();
		nodeconfig = nodeconfigDao.getByNodeID(nodedto.getId()+"");
		if(nodeconfig != null){
			mac = nodeconfig.getMac();
			processornum = nodeconfig.getNumberOfProcessors();
			CSDVersion = nodeconfig.getCSDVersion();
			hostname = nodeconfig.getHostname();
		}
		CpuInfoService cpuInfoService = new CpuInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype()); 
		Vector cpuV = cpuInfoService.getCpuInfo(); 
		if(cpuV != null && cpuV.size()>0){
			CPUcollectdata cpu = (CPUcollectdata)cpuV.get(0);
			if(cpu != null && cpu.getThevalue() != null){
				cpuvalue = new Double(cpu.getThevalue());
			}
		}
		
		//if(cpuperflist != null)cpuperfhash = (Hashtable)cpuperflist.get(0);
		//Hashtable cpuperfhash = new Hashtable();
			OthersTempDao tempdao = new OthersTempDao();
			List _cpuperflist = new ArrayList();
			try{
				_cpuperflist = tempdao.getCpuPerfInfoList(ipaddress);
			}catch(Exception e){
				
			}finally{
				tempdao.close();
			}
			if(_cpuperflist != null && _cpuperflist.size()>0){
				for (int si = 0; si < _cpuperflist.size(); si++) {
					CPUcollectdata cpudata = (CPUcollectdata)_cpuperflist.get(si);
					cpuperfhash.put(cpudata.getSubentity(),cpudata.getThevalue());
					//name = name + cpudata.getSubentity().replaceAll("%", "") + ";";
				}
			}		
		
		//得到数据采集时间
		OtherInfoService otherInfoService = new OtherInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype());
		collecttime = otherInfoService.getCollecttime();
		paginghash = otherInfoService.getPaginghash();
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
				//System.out.println(systemdata.getSubentity()+"------------");
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
		 hostlastmanager=new HostLastCollectDataManager();
		try{
			memhash = hostlastmanager.getMemory(host.getIpAddress(),"Memory",starttime,endtime);
		}catch(Exception e){
			e.printStackTrace();
		}
		//取出当前的硬盘信息 
		DiskInfoService diskInfoService = new DiskInfoService(String.valueOf(nodedto.getId()),nodedto.getType(),nodedto.getSubtype());
		diskHashtable = diskInfoService.getCurrDiskListInfo();
		
		PingInfoService pingInfoService = new PingInfoService(String.valueOf(nodedto.getId()),nodedto.getType(),nodedto.getSubtype());
		Vector pingData  = pingInfoService.getPingInfo();  
		if(pingData != null && pingData.size()>0){
			Pingcollectdata pingdata = (Pingcollectdata)pingData.get(0);
			Calendar tempCal = (Calendar)pingdata.getCollecttime();							
			Date cc = tempCal.getTime();
			//collecttime = sdf1.format(cc);
			pingvalue = pingdata.getThevalue();//当前连通率
			pingdata = (Pingcollectdata)pingData.get(1);
			responsevalue = pingdata.getThevalue();//当前响应时间
		}
	}
	PollMonitorManager pollMonitorManager = new PollMonitorManager(); 
	String newip=SysUtil.doip(host.getIpAddress());
	//画图（磁盘是显示最新数据的柱状图）
	try{
		pollMonitorManager.draw_column(diskHashtable,"",newip+"disk",750,150);
	}catch(Exception e){
		e.printStackTrace();
	}
	request.setAttribute("Disk", diskHashtable);
	Hashtable ConnectUtilizationhash = new Hashtable();
	Hashtable cpudetailhash = new Hashtable();
	Hashtable pagedetailhash = new Hashtable();
	I_HostCollectData hostmanager=new HostCollectDataManager();
	try{
		ConnectUtilizationhash = hostmanager.getCategory(host.getIpAddress(),"Ping","ConnectUtilization",starttime1,totime1);
		cpudetailhash = hostmanager.getCpuDetail(host.getIpAddress(),starttime1,totime1);
		pagedetailhash = hostmanager.getPageingDetail(host.getIpAddress(),starttime1,totime1);
	}catch(Exception ex){
		ex.printStackTrace();
	}
		
	if (ConnectUtilizationhash.get("avgpingcon")!=null){
		pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
		pingconavg = pingconavg.replace("%", "");
		maxpingvalue = (String)ConnectUtilizationhash.get("pingmax");
	}
	
	try{
		ConnectUtilizationhash = hostmanager.getCategory(host.getIpAddress(),"Ping","ResponseTime",starttime1,totime1);
	}catch(Exception ex){
		ex.printStackTrace();
	}
	
	if (ConnectUtilizationhash.get("avgpingcon")!=null){
		avgresponse = (String)ConnectUtilizationhash.get("avgpingcon");
		avgresponse = avgresponse.replace("毫秒", "");
		maxresponse = (String)ConnectUtilizationhash.get("pingmax");
	}
    request.setAttribute("id", tmp);
    request.setAttribute("ipaddress", host.getIpAddress());
	request.setAttribute("cpuvalue", cpuvalue);
	request.setAttribute("collecttime", collecttime);
	request.setAttribute("sysuptime", sysuptime);
	request.setAttribute("sysservices", sysservices);
	request.setAttribute("sysdescr", sysdescr);
	request.setAttribute("pingconavg", new Double(pingconavg));
	
	//String[] memoryItem={"AllSize","UsedSize","Utilization"};
	//String[] memoryItemch={"总容量","已用容量","当前利用率","最大利用率"};
	String[] memoryItem={"Capability","Utilization"};
	String[] memoryItemch={"容量","当前","最大","平均"};
  	String[] diskItem={"AllSize","UsedSize","Utilization"};
	String[] diskItemch={"总容量","已用容量","利用率","i-node已使用","i-node利用率"};
	String[] sysItem={"sysName","sysUpTime","sysContact","sysLocation","sysServices","sysDescr"};
	String[] sysItemch={"设备名","设备启动时间","设备联系","设备位置","设备服务","设备描述"};

	Hashtable hash = (Hashtable)request.getAttribute("hash");
	Hashtable hash1 = (Hashtable)request.getAttribute("hash1");
	Hashtable max = (Hashtable) request.getAttribute("max");
	Hashtable imgurl = (Hashtable)request.getAttribute("imgurl");
	String avgcpu="0";
	String cpumax="0";
   if(max!=null){
	avgcpu= (String)max.get("cpuavg");
	cpumax= (String)max.get("cpu");
   }
    if(avgcpu.equals("")||avgcpu==null){
	 avgcpu ="0";
   }
	 if(cpumax.equals("")||cpumax==null){
	 cpumax="0";
	 }
	Hashtable Disk = (Hashtable)request.getAttribute("Disk");
	
	Hashtable memmaxhash = (Hashtable) request.getAttribute("memmaxhash");
	Hashtable memavghash = (Hashtable) request.getAttribute("memavghash");
	
	
	
	double avgpingcon = (Double)request.getAttribute("pingconavg");
	int percent1 = Double.valueOf(avgpingcon).intValue();
	int percent2 = 100-percent1;
	
	int cpuper = Double.valueOf(cpuvalue).intValue(); 
  
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
  	
  	DecimalFormat df=new DecimalFormat("#.##"); 
     	
     	String ip = host.getIpAddress();
	String picip = CommonUtil.doip(ip);
	
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
		
	        //生成CPU最大值仪表盘
		//CreateMetersPic cmp = new CreateMetersPic();
		//MeterModel mm = new MeterModel();
		mm = new MeterModel();
		mm.setBgColor(0xffffff);
		mm.setInnerRoundColor(0xececec);
		mm.setOutRingColor(0x80ff80);
		mm.setTitle("CPU利用率");
		mm.setPicx(150);//
		mm.setPicy(150);//
		mm.setMeterX(80);//
		mm.setMeterY(80);//
		mm.setPicName(picip+"cpumax");//
		mm.setValue(new Double(cpumax.replaceAll("%","")));//
		mm.setMeterSize(60);//设置仪表盘大小
		mm.setTitleY(79);//设置标题离左边距离
		mm.setTitleTop(122);//设置标题离顶部距离
		mm.setValueY(78);//设置值离左边距离
		mm.setValueTop(105);//设置值离顶部距离
		mm.setOutPointerColor(0x80ff80);//设置指针外部颜色
		mm.setInPointerColor(0x8080ff);//设置指针内部颜色
		mm.setFontSize(10);//设置字体大小
		sm = new ArrayList<StageColor>();
		sc1 = new StageColor();
		sc1.setColor(0x99ff99);
		sc1.setStart(0);
		sc1.setEnd(60);
		sc2 = new StageColor();
		sc2.setColor(0xffff00);
		sc2.setStart(60);
		sc2.setEnd(80);
		sc3 = new StageColor();
		sc3.setColor(0xff3333);
		sc3.setStart(80);
		sc3.setEnd(100);
		sm.add(sc1);
		sm.add(sc2);
		sm.add(sc3);
		mm.setList(sm);
		cmp.createSimpleMeter(mm);	
		
		//生成CPU平均值仪表盘
		//CreateMetersPic cmp = new CreateMetersPic();
		//MeterModel mm = new MeterModel();
		mm = new MeterModel();
		mm.setBgColor(0xffffff);
		mm.setInnerRoundColor(0xececec);
		mm.setOutRingColor(0x80ff80);
		mm.setTitle("CPU利用率");
		mm.setPicx(150);//
		mm.setPicy(150);//
		mm.setMeterX(80);//
		mm.setMeterY(80);//
		mm.setPicName(picip+"cpuavg");//
		mm.setValue(new Double(avgcpu.replaceAll("%","")));//
		mm.setMeterSize(60);//设置仪表盘大小
		mm.setTitleY(79);//设置标题离左边距离
		mm.setTitleTop(122);//设置标题离顶部距离
		mm.setValueY(78);//设置值离左边距离
		mm.setValueTop(105);//设置值离顶部距离
		mm.setOutPointerColor(0x80ff80);//设置指针外部颜色
		mm.setInPointerColor(0x8080ff);//设置指针内部颜色
		mm.setFontSize(10);//设置字体大小
		sm = new ArrayList<StageColor>();
		sc1 = new StageColor();
		sc1.setColor(0x99ff99);
		sc1.setStart(0);
		sc1.setEnd(60);
		sc2 = new StageColor();
		sc2.setColor(0xffff00);
		sc2.setStart(60);
		sc2.setEnd(80);
		sc3 = new StageColor();
		sc3.setColor(0xff3333);
		sc3.setStart(80);
		sc3.setEnd(100);
		sm.add(sc1);
		sm.add(sc2);
		sm.add(sc3);
		mm.setList(sm);
		cmp.createSimpleMeter(mm);	
		String maxpageoutused = "0";
		String avgpageoutused = "0";
		if(pagedetailhash != null){
			
		if(pagedetailhash.containsKey("maxvalue"))maxpageoutused = (String)pagedetailhash.get("maxvalue");
		if(pagedetailhash.containsKey("avgvalue"))avgpageoutused = (String)pagedetailhash.get("avgvalue");
			}
		if(maxpageoutused==null ||maxpageoutused.equals("")){
			maxpageoutused = "0";
			avgpageoutused = "0";
		}
			//当前换页率
	    cmp = new CreateMetersPic();
		mm = new MeterModel();
		mm.setBgColor(0xffffff);
		mm.setInnerRoundColor(0xececec);
		mm.setOutRingColor(0x80ff80);
		mm.setTitle("换页率");
		mm.setPicx(150);//
		mm.setPicy(150);//
		mm.setMeterX(80);//
		mm.setMeterY(80);//
		mm.setPicName(picip+"pageout");//
		mm.setValue(pageingused);//
		mm.setMeterSize(60);//设置仪表盘大小
		mm.setTitleY(79);//设置标题离左边距离
		mm.setTitleTop(122);//设置标题离顶部距离
		mm.setValueY(78);//设置值离左边距离
		mm.setValueTop(105);//设置值离顶部距离
		mm.setOutPointerColor(0x80ff80);//设置指针外部颜色
		mm.setInPointerColor(0x8080ff);//设置指针内部颜色
		mm.setFontSize(10);//设置字体大小
		 sm = new ArrayList<StageColor>();
		 sc1 = new StageColor();
		sc1.setColor(0x99ff99);
		sc1.setStart(0);
		sc1.setEnd(60);
		 sc2 = new StageColor();
		sc2.setColor(0xffff00);
		sc2.setStart(60);
		sc2.setEnd(80);
		 sc3 = new StageColor();
		sc3.setColor(0xff3333);
		sc3.setStart(80);
		sc3.setEnd(100);
		sm.add(sc1);
		sm.add(sc2);
		sm.add(sc3);
		mm.setList(sm);
		cmp.createSimpleMeter(mm);
		
	        //生成换页率最大值仪表盘
		mm = new MeterModel();
		mm.setBgColor(0xffffff);
		mm.setInnerRoundColor(0xececec);
		mm.setOutRingColor(0x80ff80);
		mm.setTitle("换页率");
		mm.setPicx(150);//
		mm.setPicy(150);//
		mm.setMeterX(80);//
		mm.setMeterY(80);//
		mm.setPicName(picip+"pageoutmax");//
		mm.setValue(new Double(maxpageoutused.replaceAll("%","")));//
		mm.setMeterSize(60);//设置仪表盘大小
		mm.setTitleY(79);//设置标题离左边距离
		mm.setTitleTop(122);//设置标题离顶部距离
		mm.setValueY(78);//设置值离左边距离
		mm.setValueTop(105);//设置值离顶部距离
		mm.setOutPointerColor(0x80ff80);//设置指针外部颜色
		mm.setInPointerColor(0x8080ff);//设置指针内部颜色
		mm.setFontSize(10);//设置字体大小
		sm = new ArrayList<StageColor>();
		sc1 = new StageColor();
		sc1.setColor(0x99ff99);
		sc1.setStart(0);
		sc1.setEnd(60);
		sc2 = new StageColor();
		sc2.setColor(0xffff00);
		sc2.setStart(60);
		sc2.setEnd(80);
		sc3 = new StageColor();
		sc3.setColor(0xff3333);
		sc3.setStart(80);
		sc3.setEnd(100);
		sm.add(sc1);
		sm.add(sc2);
		sm.add(sc3);
		mm.setList(sm);
		cmp.createSimpleMeter(mm);	
		
		//生成换页率平均值仪表盘
		mm = new MeterModel();
		mm.setBgColor(0xffffff);
		mm.setInnerRoundColor(0xececec);
		mm.setOutRingColor(0x80ff80);
		mm.setTitle("换页率");
		mm.setPicx(150);//
		mm.setPicy(150);//
		mm.setMeterX(80);//
		mm.setMeterY(80);//
		mm.setPicName(picip+"pageoutavg");//
		mm.setValue(new Double(avgpageoutused.replaceAll("%","")));//
		mm.setMeterSize(60);//设置仪表盘大小
		mm.setTitleY(79);//设置标题离左边距离
		mm.setTitleTop(122);//设置标题离顶部距离
		mm.setValueY(78);//设置值离左边距离
		mm.setValueTop(105);//设置值离顶部距离
		mm.setOutPointerColor(0x80ff80);//设置指针外部颜色
		mm.setInPointerColor(0x8080ff);//设置指针内部颜色
		mm.setFontSize(10);//设置字体大小
		sm = new ArrayList<StageColor>();
		sc1 = new StageColor();
		sc1.setColor(0x99ff99);
		sc1.setStart(0);
		sc1.setEnd(60);
		sc2 = new StageColor();
		sc2.setColor(0xffff00);
		sc2.setStart(60);
		sc2.setEnd(80);
		sc3 = new StageColor();
		sc3.setColor(0xff3333);
		sc3.setStart(80);
		sc3.setEnd(100);
		sm.add(sc1);
		sm.add(sc2);
		sm.add(sc3);
		mm.setList(sm);
		cmp.createSimpleMeter(mm);		
	CreateBarPic cbp = new CreateBarPic();
		double[] data1 = {40, 30};
		 double[] data2 = {60, 70};
		 String[] labels = {"物理内存", "虚拟内存"};		 
		 TitleModel tm = new TitleModel();
		 tm.setBgcolor(0xffffff);
		 tm.setXpic(150);
		 tm.setYpic(140);
		 tm.setX1(0);
		 tm.setX2(0);
		 tm.setX3(140);
		 tm.setX4(100);
		 tm.setX5(20);
		 tm.setX6(20);
		 int color1 = 0x80ff80;
		 int color2 = 0x8080ff;
		cbp.createCylindricalPic(data1,data2,labels,tm,"","",color1,color2);
		
 cbp = new CreateBarPic();
		double[] r_data1 = {new Double(responsevalue.replaceAll("%","")), new Double(maxresponse.replaceAll("%","")),new Double(avgresponse.replaceAll("%",""))};
		 String[] r_labels = {"当前响应时间(ms)", "最大响应时间(ms)","平均响应时间(ms)"};		 
		 tm = new TitleModel();
		 tm.setPicName(picip+"response");//
		 tm.setBgcolor(0xffffff);
		 tm.setXpic(450);//图片长度
		 tm.setYpic(180);//图片高度
		 tm.setX1(30);//左面距离
		 tm.setX2(20);//上面距离
		 tm.setX3(400);//内图宽度
		 tm.setX4(130);//内图高度
		 tm.setX5(10);
		 tm.setX6(115);
		 cbp.createTimeBarPic(r_data1,r_labels,tm,40);
		 
		 
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
	        _cpp.createOneRingChart(_data1,p_labels,_colors,_titleModel);
	        
	                
		//生成当前连通率图形
		 Double realValue=Double.valueOf(pingvalue.replaceAll("%",""));
		CreatePiePicture ping_cpp = new CreatePiePicture();
	        TitleModel ping_titleModel = new TitleModel();
	        ping_titleModel.setXpic(150);
	        ping_titleModel.setYpic(150);//160, 200, 150, 100
	        ping_titleModel.setX1(75);//外环向左的位置
	        ping_titleModel.setX2(60);//外环向上的位置
	        ping_titleModel.setX3(65);
	        ping_titleModel.setX4(30);
	        ping_titleModel.setX5(75);
	        ping_titleModel.setX6(70);
	        ping_titleModel.setX7(10);
	        ping_titleModel.setX8(115);
	        ping_titleModel.setBgcolor(0xffffff);
	        ping_titleModel.setPictype("png");
	        ping_titleModel.setPicName(picip+"realping");
	        ping_titleModel.setTopTitle("");
	        
	        double[] ping_data1 = {realValue, 100-realValue};
	        String[] ping_labels = {"连通", "未连通"};
	        int[] ping_colors = {0x66ff66, 0xff0000}; 
	        _cpp.createOneRingChart(ping_data1,ping_labels,ping_colors,ping_titleModel);	
	        
		//生成最小连通率图形
	        ping_titleModel = new TitleModel();
	        ping_titleModel.setXpic(150);
	        ping_titleModel.setYpic(150);//160, 200, 150, 100
	        ping_titleModel.setX1(75);//外环向左的位置
	        ping_titleModel.setX2(60);//外环向上的位置
	        ping_titleModel.setX3(65);
	        ping_titleModel.setX4(30);
	        ping_titleModel.setX5(75);
	        ping_titleModel.setX6(70);
	        ping_titleModel.setX7(10);
	        ping_titleModel.setX8(115);
	        ping_titleModel.setBgcolor(0xffffff);
	        ping_titleModel.setPictype("png");
	        ping_titleModel.setPicName(picip+"minping");
	        ping_titleModel.setTopTitle("");
	        double d_maxping = new Double(maxpingvalue.replace("%",""));
	        double[] minping_data1 = {d_maxping, 100-d_maxping};
	       
	        _cpp.createOneRingChart(minping_data1,ping_labels,ping_colors,ping_titleModel);	                
	        
	        
		String pmemvalue = "0";
                if(memmaxhash.get("PhysicalMemory")!=null){
                      	pmemvalue = (String)memmaxhash.get("PhysicalMemory");
                      	pmemvalue = pmemvalue.replaceAll("%","");
                }
                String vmemvalue = "0";
                if(memmaxhash.get("SwapMemory")!=null){
                      	vmemvalue = (String)memmaxhash.get("SwapMemory");
                      	vmemvalue = vmemvalue.replaceAll("%","");
                }
                double pmem_value = new Double(pmemvalue);
                double vmem_value = new Double(vmemvalue);	        
			
	   //生成CPU仪表盘
		 cmp = new CreateMetersPic();
		 mm = new MeterModel();
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
		List<StageColor> sm1 = new ArrayList<StageColor>();
		StageColor sc4 = new StageColor();
		sc1.setColor(0x99ff99);
		sc1.setStart(0);
		sc1.setEnd(60);
		StageColor sc5 = new StageColor();
		sc2.setColor(0xffff00);
		sc2.setStart(60);
		sc2.setEnd(80);
		StageColor sc6 = new StageColor();
		sc3.setColor(0xff3333);
		sc3.setStart(80);
		sc3.setEnd(100);
		sm.add(sc4);
		sm.add(sc5);
		sm.add(sc6);
		mm.setList(sm);
		cmp.createSimpleMeter(mm);
		
	
		CreateAmColumnPic cpudetail=new CreateAmColumnPic();
		String dataStr=cpudetail.createCpuDetailAmChart(cpuperfhash,cpudetailhash);
		//磁盘利用率
//   CreateBarPic cpu_cbp = new CreateBarPic();
//    TitleModel cpu_tm = new TitleModel();
//		 cpu_tm.setPicName(picip+"cpudetail");//
//		 cpu_tm.setBgcolor(0x000000);
//		 cpu_tm.setXpic(400);
//		 cpu_tm.setYpic(170);
//		 cpu_tm.setX1(50);
//		 cpu_tm.setX2(20);
//		 cpu_tm.setX3(320);
//		 cpu_tm.setX4(100);
//		 cpu_tm.setX5(95);
//		 cpu_tm.setX6(140);
//		 int cpu_color1 = 0x80ff80;
//		 int cpu_color2 = 0x8080ff;
//		 int cpu_color3= 0xFFFF00;
//		 int cpu_color4= 0xFF00FF;
//		 cpu_cbp.createCylindricalPicc(cpu_data1,cpu_data2,cpu_data3,cpu_data4,cpu_labels,cpu_tm,"idle","wio","sys","usr",cpu_color1,cpu_color2,cpu_color3,cpu_color4);   					  	   

		//amchar 连通率
		 StringBuffer dataStr1 = new StringBuffer();
		 	dataStr1.append("连通;").append(Math.round(realValue)).append(";false;7CFC00\\n");
		 	dataStr1.append("未连通;").append(100-Math.round(realValue)).append(";false;FF0000\\n");
		 	String realdata = dataStr1.toString();
		 			StringBuffer dataStr2 = new StringBuffer();
		 	dataStr2.append("连通;").append(Math.round(avgpingcon)).append(";false;7CFC00\\n");
		 	dataStr2.append("未连通;").append(100-Math.round(avgpingcon)).append(";false;FF0000\\n");
		 	String avgdata = dataStr2.toString();
		 			StringBuffer dataStr3 = new StringBuffer();
		 			String maxping=maxpingvalue.replaceAll("%", "");
		 	dataStr3.append("连通;").append(Math.round(Float.parseFloat(maxping))).append(";false;7CFC00\\n");
		 	dataStr3.append("未连通;").append(100-Math.round(Float.parseFloat(maxping))).append(";false;FF0000\\n");
		 	String maxdata = dataStr3.toString();
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
  	location.href="<%=rootPath%>/topology/network/networkview.jsp?id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&orderflag="+para;
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
	document.getElementById('aixDetailTitle-0').className='detail-data-title';
	document.getElementById('aixDetailTitle-0').onmouseover="this.className='detail-data-title'";
	document.getElementById('aixDetailTitle-0').onmouseout="this.className='detail-data-title'";
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
											    	<td class="detail-data-header">
											    		<%=aixDetailTitleTable%>
											    	</td>
											  </tr>
											  <tr>
											    	<td>
													<table class="detail-data-body">
<tr>
                    <td align=center >
                    <br>
                      <table cellpadding="0" cellspacing="0" width=48% align=center>
              							<tr> 
                							<td width="100%"  align=center> 
                										<div id="flashcontent3">
													<strong>You need to upgrade your Flash Player</strong>
												</div>
												<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Ping.swf?ipadress=<%=host.getIpAddress()%>", "Ping", "346", "180", "8", "#ffffff");
													so.write("flashcontent3");
												</script>				
							                </td>
								</tr> 
								<tr>
																	<td width=100% align=center>
																		<table cellpadding="1" cellspacing="1" style="align:center;width:346" bgcolor="#FFFFFF">
																			<tr bgcolor="#FFFFFF">
																				<td height="29" class="detail-data-body-title" style="height:29">名称</td>
																				<td class="detail-data-body-title" style="height:29">当前（%）</td>
																				<td class="detail-data-body-title" style="height:29">最小（%）</td>
																				<td class="detail-data-body-title" style="height:29">平均（%）</td>
																			</tr>
																			<tr>
																				<td class="detail-data-body-list" height="29">连通率</td>
																				<td class="detail-data-body-list"><%=pingvalue%></td>
																				<td class="detail-data-body-list"><%=Math.round(Float.parseFloat(maxpingvalue.replaceAll("%","")))%></td>
																				<td class="detail-data-body-list"><%=Math.round(Float.parseFloat(pingconavg))%></td>
																			</tr>
																		</table>
																	</td>
								</tr>            
									</table> 
                  </td>
                  <td align=left valign=top width=57%>
														            	
<br>
                      <table id="body-container" class="body-container" style="background-color:#FFFFFF;" width="40%">
			<tr>
				<td class="td-container-main">
					<table id="container-main" class="container-main">
						<tr>
							<td class="td-container-main-add">
								<table id="container-main-add" class="container-main-add">
									<tr>
										<td style="background-color:#FFFFFF;">
											<table id="add-content" class="add-content">
												<tr>
													<td>
														<table id="add-content-header" class="add-content-header">
										                	<tr>
											                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
											                	<td class="add-content-title"><b>连通率实时</b></td>
											                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
											       			</tr>
											        	</table>
				        							</td>
				        						</tr>
				        						<tr>
				        							<td>
				        								<table id="detail-content-body" class="detail-content-body" border=1>
				        									<tr>
				        										<td>
				        										<table  cellpadding="1" cellspacing="1" width=48% >
              							
								<tr>
									<td valign=top>
									   
										

			<table width="90%" cellpadding="0" cellspacing="0"  >
				<tr>
			      	<td valign=top>
			    <!--   	<img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>realping.png">  -->
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
				<td valign=top>
			<!--	<img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>minping.png"> -->
				<div id="maxping">
					 <strong>You need to upgrade your Flash Player</strong>
			    </div>
					<script type="text/javascript"
						 src="<%=rootPath%>/include/swfobject.js"></script>
					<script type="text/javascript">
						var so = new SWFObject("<%=rootPath%>/amchart/ampie.swf", "ampie","160", "155", "8", "#FFFFFF");
						    so.addVariable("path", "<%=rootPath%>/amchart/");
						    so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/pingStatepie.xml"));
						    so.addVariable("chart_data","<%=maxdata%>");
						    so.write("maxping");
					</script>
				</td>
				<td valign=top>
			<!--	<img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>pingavg.png"> -->
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
				<tr height=50>
			      	<td valign=top align=center><b>当前</b></td>
				<td valign=top align=center><b>最小</b></td>
				<td valign=top align=center><b>平均</b></td>
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
						</tr>
					</table>
				</td>
			</tr>
		</table>														            	
														            	
	</td>
	</tr>
	<tr>
                  <td align=center >
                  <br>
                      <table cellpadding="0" cellspacing="0" width=48% >
              							<tr> 
                							<td width="100%"  align=center> 
                								<div id="flashcontent4">
													<strong>You need to upgrade your Flash Player</strong>
												</div>
												<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Response_time.swf?ipadress=<%=host.getIpAddress()%>", "Response_time", "346", "180", "8", "#ffffff");
													so.write("flashcontent4");
												</script>				
							                </td>
										</tr>  
								<tr>
																	<td width=100% align=center>
																		<table cellpadding="1" cellspacing="1" style="align:center;width:346" bgcolor="#FFFFFF">
																			<tr bgcolor="#FFFFFF">
																				<td class="detail-data-body-title" style="height:29" height="29">名称</td>
																				<td class="detail-data-body-title" style="height:29">当前（ms）</td>
																				<td class="detail-data-body-title" style="height:29">最大（ms）</td>
																				<td class="detail-data-body-title" style="height:29">平均（ms）</td>
																			</tr>
																			<tr>
																				<td class="detail-data-body-list" height="29">响应时间</td>
																				<td class="detail-data-body-list"><%=responsevalue.replaceAll("%","")%></td>
																				<td class="detail-data-body-list"><%=maxresponse.replaceAll("%","")%></td>
																				<td class="detail-data-body-list"><%=avgresponse.replaceAll("%","")%></td>
																			</tr>
																		</table>
																	</td>
																	</tr>           
									</table> 
                      </td>
                      <td align=center>
														            	<br>
<table id="body-container" class="body-container" style="background-color:#FFFFFF;" width="40%" >
			<tr>
				<td class="td-container-main">
					<table id="container-main" class="container-main">
						<tr>
							<td class="td-container-main-add">
								<table id="container-main-add" class="container-main-add">
									<tr>
										<td style="background-color:#FFFFFF;">
											<table id="add-content" class="add-content">
												<tr>
													<td>
														<table id="add-content-header" class="add-content-header">
										                	<tr>
											                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
											                	<td class="add-content-title"><b>响应时间详情</b></td>
											                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
											       			</tr>
											        	</table>
				        							</td>
				        						</tr>
				        						<tr>
				        							<td>
				        								<table id="detail-content-body" class="detail-content-body" border='1'>
				        									<tr>
				        										<td>
				        										<table  cellpadding="1" cellspacing="1" width=48%>
              							
																<tr>
																	<td valign=top>
									   
										

																	<table cellpadding="0" cellspacing="0" width=48% align=center >
																	<tr>
																	<td align=center colspan=3>
																		<img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>response.png">	
																	</td>
																	</tr> 
																	<tr height=30>
																      	<td valign=top align=center><b>当前</b></td>
																	<td valign=top align=center><b>最大</b></td>
																	<td valign=top align=center><b>平均</b></td>
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
						</tr>
					</table>
				</td>
			</tr>
		</table>														            		
																</td>
                  </tr>													
												      		  <tr>
                    <td align=center valign=top>
                    
                    <br>
                      <table cellpadding="0" cellspacing="0" width=48% align=center>
                      
              							<tr> 
                							<td width="100%" align=center> 
                								<div id="flashcontent1">
													<strong>You need to upgrade your Flash Player</strong>
												</div>
												<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Line_CPU.swf?ipadress=<%=host.getIpAddress()%>", "Line_CPU", "346", "200", "8", "#ffffff");
													so.write("flashcontent1");
												</script>				
							                </td>
										</tr>  
								 <tr>
																	<td width=100% align=center>
																		<table cellpadding="0" cellspacing="1" style="align:center;width:346" bgcolor="#FFFFFF">
																			<tr bgcolor="#FFFFFF">
																				<td height="29" class="detail-data-body-title" style="height:29">名称</td>
																				<td class="detail-data-body-title" style="height:29">当前</td>
																				<td class="detail-data-body-title" style="height:29">最大</td>
																				<td class="detail-data-body-title" style="height:29">平均</td>
																			</tr>
																			<tr>
																				<td class="detail-data-body-list" height="29">&nbsp;CPU利用率</td>
																				<td class="detail-data-body-list">&nbsp;<%=cpuper%>%</td>
																				<td class="detail-data-body-list">&nbsp;<%=cpumax%></td>
																				<td class="detail-data-body-list">&nbsp;<%=avgcpu%></td>
																			</tr>
																		</table>
																	</td>
																	</tr>           
									</table> 
                  </td>
                  <td align=center valign=top>
<br>
                      <table id="body-container" class="body-container" style="background-color:#FFFFFF;" width="40%">
			<tr>
				<td class="td-container-main">
					<table id="container-main" class="container-main">
						<tr>
							<td class="td-container-main-add">
								<table id="container-main-add" class="container-main-add">
									<tr>
										<td style="background-color:#FFFFFF;">
											<table id="add-content" class="add-content">
												<tr>
													<td>
														<table id="add-content-header" class="add-content-header">
										                	<tr>
											                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
											                	<td class="add-content-title"><b>CPU利用率详情</b></td>
											                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
											       			</tr>
											        	</table>
				        							</td>
				        						</tr>
				        						<tr>
				        							<td>
				        								<table id="detail-content-body" class="detail-content-body" border='1'>
				        									<tr>
				        										<td>
				        										<table  cellpadding="1" cellspacing="1" width=48%>
              							
																<tr>
																	<td valign=top>
									   
										

																	<table cellpadding="0" cellspacing="0" width=48% align=center>
																	<tr>
																	<td align=center>
																		<img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>cpu.png">	
																	</td>
																	<td align=center>
																		<img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>cpumax.png">	
																	</td>
																	<td align=center>
																		<img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>cpuavg.png">	
																	</td>
																	</tr> 
																	<tr height=20>
																      	<td valign=top align=center><b>当前</b></td>
																	<td valign=top align=center><b>最大</b></td>
																	<td valign=top align=center><b>平均</b></td>
																	</tr>
																	<tr height=20>
																      	<td colspan=3>&nbsp;</td>
																	</tr>
																																		           
																	</table>

																	</td>
				   
																	</tr> 
								        
																</table> 
				        												
							                                   					</td>
							                                				</tr>
							                                				
							                                				<tr>
				        										<td>
				        										
				        										
				        										<table  cellpadding="1" cellspacing="1" width=80%>
              							
																<tr>
																	<td valign=top width=80%>
																	<table cellpadding="0" cellspacing="0" width=80% align=center>
<%
	if(deviceV != null){
    	      for(int m=0;m<deviceV.size();m++){
              		Devicecollectdata devicedata = (Devicecollectdata)deviceV.get(m);
			String name = devicedata.getName();
			String type = devicedata.getType();
			if(!"CPU".equals(type))continue;				
			String status = devicedata.getStatus();
		
%>																	
																	<tr height=20>
																      	<td valign=top align=center><b>类型</b>：<%=type%></td>
																	<td valign=top align=center><b>描述</b>：<%=name%></td>
																	<td valign=top align=center><b>状态</b>：<%=status%></td>
																	</tr>	
<%
		}
	}
%>																															           
																	</table>

																	</td>
				   
																	</tr> 
																	<tr height=20>
																	      <td valign=center align=right colspan=3>
																		>> <a href="#">实 时</a> &nbsp;&nbsp;
																		
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
			</tr>
		</table>									 
	</td>
</tr>
				<tr>
                  <td align=center valign=top>
                  <br>
                      <table cellpadding="0" cellspacing="0" width=48% align=center border='1'>
              							<tr> 
                							<td width="100%"  align=center> 
                								<div id="flashcontent2">
													<strong>You need to upgrade your Flash Player</strong>
												</div>
												<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Area_Memory.swf?ipadress=<%=host.getIpAddress()%>", "Area_flux", "346", "200", "8", "#ffffff");
													so.write("flashcontent2");
												</script>				
							                </td>
								</tr>
								<tr>
																	<td align=center>
																	
																	<table width="90%" cellpadding="0" cellspacing="0" style="align:center;width:346">
                  <tr align="center" height="28" style="background-color:#EEEEEE;"> 
                    <td width="21%" class="detail-data-body-title" style="height:29">内存名</td>
                    
                    <%
                    String pmem = "";
         String vmem = "";
         String pcurmem = "";
         String pmaxmem = "";
         String pavgmem = "";
         String vcurmem = "";
         String vmaxmem = "";
         String vavgmem = "";    
         for(int j=0;j<memoryItemch.length;j++){
         		String item=memoryItemch[j];
         %>
                    <td  class="detail-data-body-title"><%=item%></td>
                    <%}%>
                  </tr>
   <% for(int k=0; k<memhash.size(); k++)
      {
          Hashtable mhash = (Hashtable)(memhash.get(new Integer(k)));
          String name = (String)mhash.get("name");
      %>
                  <tr> 
                    <td width="20%" class="detail-data-body-list" height="29">&nbsp;<%=name%></td>
                   <%for(int j=0;j<memoryItem.length;j++){
                       String value = "";
                       if(mhash.get(memoryItem[j])!=null){
                           value = (String)mhash.get(memoryItem[j]);
                            if(j==0){
                           	if("PhysicalMemory".equals(name))pmem=value;
                           	if("SwapMemory".equals(name))vmem=value;
                           }else{
                           	if("PhysicalMemory".equals(name))pcurmem=value;
                           	if("SwapMemory".equals(name))vcurmem=value;
                           }
                       }
		                 %>
                     <td width="17%"  class="detail-data-body-list">&nbsp;<%=value%></td>
                    <%}
                     String value = "";
                      if(memmaxhash.get(name)!=null){
                      	value = (String)memmaxhash.get(name);
                      	if("PhysicalMemory".equals(name))pmaxmem=value;
                      	if("SwapMemory".equals(name))vmaxmem=value;
                      }
                      String avgvalue = "";
                      if(memavghash.get(name)!=null){
                      	avgvalue = (String)memavghash.get(name);
                      	if("PhysicalMemory".equals(name))pavgmem=avgvalue;
                      	if("SwapMemory".equals(name))vavgmem=avgvalue;
                      }                               
                    %>
                       <td width="20%"   class="detail-data-body-list">&nbsp;<%=value%></td>
                       <td width="20%"  class="detail-data-body-list">&nbsp;<%=avgvalue%></td>
                  </tr>
      <%}%>
                </table>
																		
																	</td>
																	</tr> 
								
								
								             
									</table> 
                      </td>
                      <td align=center valign=top>
<br>												                			
<table id="body-container" class="body-container" style="background-color:#FFFFFF;" width="40%">
			<tr>
				<td class="td-container-main">
					<table id="container-main" class="container-main">
						<tr>
							<td class="td-container-main-add">
								<table id="container-main-add" class="container-main-add">
									<tr>
										<td style="background-color:#FFFFFF;">
											<table id="add-content" class="add-content">
												<tr>
													<td>
														<table id="add-content-header" class="add-content-header">
										                	<tr>
											                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
											                	<td class="add-content-title"><b>设备内存明细</b></td>
											                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
											       			</tr>
											        	</table>
				        							</td>
				        						</tr>
				        						<tr>
				        							<td valign=top>
				        								<table id="detail-content-body" class="detail-content-body" border='1'>
				        									<tr>
				        										<td valign=top>
				        										<table  cellpadding="1" cellspacing="1" width=48%>
              							
								<tr>
									<td valign=top>
									   
<%
if(pcurmem==null||pcurmem.equals(""))pcurmem = "0";
if(pmaxmem==null||pmaxmem.equals(""))pmaxmem = "0";
if(pavgmem == null||pavgmem.equals(""))pavgmem = "0";
if(vcurmem == null||vcurmem.equals(""))vcurmem = "0";
if(vmaxmem == null||vmaxmem.equals(""))vmaxmem = "0";
if(vavgmem == null||vavgmem.equals(""))vavgmem = "0";
pcurmem = pcurmem.replaceAll("%","");
pmaxmem = pmaxmem.replaceAll("%","");
pavgmem = pavgmem.replaceAll("%","");
vcurmem = vcurmem.replaceAll("%","");
vmaxmem = vmaxmem.replaceAll("%","");
vavgmem = vavgmem.replaceAll("%","");

double dpcurmem = new Double(pcurmem);
double dpmaxmem = new Double(pmaxmem);
double dpavgmem = new Double(pavgmem);

double dvcurmem = new Double(vcurmem);
double dvmaxmem = new Double(vmaxmem);
double dvavgmem = new Double(vavgmem);

		//生成双环
		//CreatePiePicture _cpp = new CreatePiePicture();
	        _titleModel = new TitleModel();
	        _titleModel.setXpic(150);
	        _titleModel.setYpic(150);//160, 200, 150, 100
	        _titleModel.setX1(75);//外环向左的位置
	        _titleModel.setX2(60);//外环向上的位置
	        _titleModel.setX3(50);//环宽度
	        _titleModel.setX4(73);
	        _titleModel.setX5(90);//外环TEXT与左的位置
	        _titleModel.setX6(105);//外环TEXT与上的位置
	        _titleModel.setX7(10);
	        _titleModel.setX8(120);
	        _titleModel.setBgcolor(0xffffff);
	        _titleModel.setPictype("png");
	        _titleModel.setPicName(picip+"dmemory");
	        _titleModel.setTopTitle("");
	        
	        _titleModel.setR2x(100);
	        _titleModel.setR2y(100);
	        _titleModel.setR2x1(50);
	        _titleModel.setR2x2(30);
	        _titleModel.setR2x3(40);
	        _titleModel.setR2x4(30);
	        _titleModel.setR2textx(50);
	        _titleModel.setR2texty(20);
	        _titleModel.setInr2x(25);//挪小环向左的位置
	        _titleModel.setInr2y(30);//挪内环向上的距离	        
	        
	        double[] d_data1 = {dpcurmem, 100-dpcurmem};
	        double[] d_data2 = {dvcurmem, 100-dvcurmem};
	        String[] d_labels = {"已使用", "未使用"};
	        int[] d_colors = {0x66ff66, 0x8080ff}; 
	        String d_title1="物理";
	        String d_title2="虚拟";
	        _cpp.createTwoConcentricDonutChart(d_data1,d_title1,d_data2,d_title2,d_labels,d_colors,_titleModel);
	        
	        //最大值内存利用率图
	         _titleModel = new TitleModel();
	        _titleModel.setXpic(150);
	        _titleModel.setYpic(150);//160, 200, 150, 100
	        _titleModel.setX1(75);//外环向左的位置
	        _titleModel.setX2(60);//外环向上的位置
	        _titleModel.setX3(50);//环宽度
	        _titleModel.setX4(73);
	        _titleModel.setX5(90);//外环TEXT与左的位置
	        _titleModel.setX6(105);//外环TEXT与上的位置
	        _titleModel.setX7(10);
	        _titleModel.setX8(120);
	        _titleModel.setBgcolor(0xffffff);
	        _titleModel.setPictype("png");
	        _titleModel.setPicName(picip+"dmaxmemory");
	        _titleModel.setTopTitle("");
	        
	        _titleModel.setR2x(100);
	        _titleModel.setR2y(100);
	        _titleModel.setR2x1(50);
	        _titleModel.setR2x2(30);
	        _titleModel.setR2x3(40);
	        _titleModel.setR2x4(30);
	        _titleModel.setR2textx(50);
	        _titleModel.setR2texty(20);
	        _titleModel.setInr2x(25);//挪小环向左的位置
	        _titleModel.setInr2y(30);//挪内环向上的距离	        
	        
	        double[] dmax_data1 = {dpmaxmem, 100-dpmaxmem};
	        double[] dmax_data2 = {dvmaxmem, 100-dvmaxmem};
	        _cpp.createTwoConcentricDonutChart(dmax_data1,d_title1,dmax_data2,d_title2,d_labels,d_colors,_titleModel);
	        
	        //平均值内存利用率图
	         _titleModel = new TitleModel();
	        _titleModel.setXpic(150);
	        _titleModel.setYpic(150);//160, 200, 150, 100
	        _titleModel.setX1(75);//外环向左的位置
	        _titleModel.setX2(60);//外环向上的位置
	        _titleModel.setX3(50);//环宽度
	        _titleModel.setX4(73);
	        _titleModel.setX5(90);//外环TEXT与左的位置
	        _titleModel.setX6(105);//外环TEXT与上的位置
	        _titleModel.setX7(10);
	        _titleModel.setX8(120);
	        _titleModel.setBgcolor(0xffffff);
	        _titleModel.setPictype("png");
	        _titleModel.setPicName(picip+"davgmemory");
	        _titleModel.setTopTitle("");
	        
	        _titleModel.setR2x(100);
	        _titleModel.setR2y(100);
	        _titleModel.setR2x1(50);
	        _titleModel.setR2x2(30);
	        _titleModel.setR2x3(40);
	        _titleModel.setR2x4(30);
	        _titleModel.setR2textx(50);
	        _titleModel.setR2texty(20);
	        _titleModel.setInr2x(25);//挪小环向左的位置
	        _titleModel.setInr2y(30);//挪内环向上的距离	        
	        
	        double[] davg_data1 = {dpavgmem, 100-dpavgmem};
	        double[] davg_data2 = {dvavgmem, 100-dvavgmem};
	        _cpp.createTwoConcentricDonutChart(davg_data1,d_title1,davg_data2,d_title2,d_labels,d_colors,_titleModel);
	        StringBuffer xmlStr = new StringBuffer();
			xmlStr.append("<?xml version='1.0' encoding='gb2312'?>");
			xmlStr.append("<chart><series>");
			String[] titleStr = new String[] { "当前物理", "当前虚拟", "平均物理",
					"平均虚拟", "最大物理", "最大虚拟" };
			String[] title = new String[] { "当前已用", "当前未用", "平均已用", "平均未用",
					"最大已用", "最大未用" };

			for (int i = 0; i < 6; i++) {
				xmlStr.append("<value xid='").append(i).append("'>")
						.append(titleStr[i]).append("</value>");

			}
			xmlStr.append("</series><graphs>");
			long curp = Math.round(d_data1[0]);
			long curv = Math.round(d_data2[0]);
			long maxp = Math.round(dmax_data1[0]);
			long maxv = Math.round(+dmax_data2[0]);
			long avgp = Math.round(davg_data1[0]);
			long avgv = Math.round(davg_data2[0]);

			long[] data = { curp, curv, 100 - curp, 100 - curv, avgp, avgv,
					100 - avgp, 100 - avgv, maxp, maxv, 100 - maxp,
					100 - maxv };
			int tempInt = 0, tempId = 0;
			for (int i = 0; i < 6; i++) {
				if (i == 1)
					tempId = 0;
				if (i == 3)
					tempId = 2;
				if (i == 5)
					tempId = 4;
				xmlStr.append("<graph gid='").append(i).append("' title='")
						.append(title[i]).append("'>").append(
								"<value xid='" + tempId + "'> "
										+ data[tempInt]).append("</value>");
				xmlStr.append("<value xid='" + (++tempId) + "'>"
						+ data[++tempInt] + "</value>");
				xmlStr.append("</graph>");
				tempId++;
				tempInt++;
			}

			xmlStr.append("</graphs></chart>");
	String memStr=xmlStr.toString();		

%>										

			<table width="90%" cellpadding="0" cellspacing="0"  border=0>
			<tr>
			      <td colspan=1>
							<div id="memorypercent">
								<strong>You need to upgrade your Flash Player</strong>
							</div>
							<script type="text/javascript">
		                        var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "amcolumn", "470", "210", "8", "#FFFFFF");
		                        so.addVariable("path", "<%=rootPath%>/amchart/");
		                        so.addVariable("settings_file",  escape("<%=rootPath%>/amcharts_settings/memorypercent_settings.xml"));
	                            so.addVariable("chart_data", "<%=memStr%>");
		                        so.addVariable("preloader_color", "#999999");
		                        so.write("memorypercent");
	                        </script>
																							
			<!--  		<img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>dmemory.png">
	  			       </td>
	   			        <td valign=top>
                     			<img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>dmaxmemory.png">
                   		</td>
                   		<td valign=top>
                     			<img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>davgmemory.png">
                   		</td>-->
	  		</tr>
	  		
	  		<tr height=20>
			      <td valign=middle align=center colspan=3>
				
				<table cellpadding="0" cellspacing="0" width=48% align=center>
					<tr height=20>
						<td valign=top align=center><b>物理内存总量</b>：<%=pmem%></td>
						<td valign=top align=center><b>虚拟内存总量</b>：<%=vmem%></td>
					</tr>															           
				</table>
				
                   		</td>
	  		</tr>
	  		<tr height=15>
			      <td valign=center align=right colspan=3>
				>> <a href="#">实 时</a> &nbsp;&nbsp;
				
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
						</tr>
					</table>
				</td>
			</tr>
		</table>												                			
												                			
												                			
												           			</td>
                    </tr> 
                   <tr>
                    <td align=center valign=top>
                    <br>
                      <table cellpadding="0" cellspacing="0" width=48% align=center>
              							<tr> 
                							<td width="100%"  align=center> 
                										<div id="flashcontent3_cpudtl">
													<strong>You need to upgrade your Flash Player</strong>
												</div>
												<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Cpu_detail.swf?ipadress=<%=host.getIpAddress()%>", "CPU详细", "346", "370", "8", "#ffffff");
													so.write("flashcontent3_cpudtl");
												</script>				
							                </td>
								</tr> 
								            
									</table> 
                  </td>
                  
<td align=center>
														            	<br>
<table id="body-container" class="body-container" style="background-color:#FFFFFF;" width="40%">
			<tr>
				<td class="td-container-main">
					<table id="container-main" class="container-main">
						<tr>
							<td class="td-container-main-add">
								<table id="container-main-add" class="container-main-add">
									<tr>
										<td style="background-color:#FFFFFF;">
											<table id="add-content" class="add-content">
												<tr>
													<td>
														<table id="add-content-header" class="add-content-header">
										                	<tr>
											                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
											                	<td class="add-content-title"><b>CPU信息详情</b></td>
											                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
											       			</tr>
											        	</table>
				        							</td>
				        						</tr>
				        						<tr>
				        							<td>
				        								<table id="detail-content-body" class="detail-content-body" border='1'>
				        									<tr>
				        										<td>
				        										<table  cellpadding="1" cellspacing="1" width=48%>
              							
																<tr>
																	<td valign=top>
																	<table cellpadding="0" cellspacing="0" width=48% align=center>
																	<tr>
																	<td align=center colspan=1>
																	<!-- 	<img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>cpudetail.png">	 -->
																	<div id="aixCpuDetail">
								                                        <strong></strong>
							                                        </div>
							                                        <script type="text/javascript">
							                                      <%  if(!dataStr.equals("0")){%>
		                                                               var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "amcolumn", "485", "210", "8", "#FFFFFF");
		                                                                   so.addVariable("path", "<%=rootPath%>/amchart/");
		                                                                   so.addVariable("settings_file",  escape("<%=rootPath%>/amcharts_settings/cpuUtilPercent_settings.xml"));
	                                                                       so.addVariable("chart_data", "<%=dataStr%>");
		                                                                   so.addVariable("preloader_color", "#999999");
		                                                                   so.write("aixCpuDetail");
		                                                                   <%}else{%>
		                                                                   var _div=document.getElementById("aixCpuDetail");
																			var img=document.createElement("img");
																			img.setAttribute("src","<%=rootPath%>/resource/image/nodata.gif");
																			_div.appendChild(img);
																			<%}%>
	                                                                </script>
																	</td>
																	</tr> 
																	 <tr>
																	<td width=100% align=center valign=top>
																		<table cellpadding="1" cellspacing="1" style="align:center;width:100%" bgcolor="#FFFFFF">
																			<tr bgcolor="#FFFFFF">
																				<td align=center class="detail-data-body-list" style="align:center;height:29">名称</td>
																				<td align=center class="detail-data-body-list" style="align:center;height:29">当前（%）</td>
																				<td align=center class="detail-data-body-list" style="align:center;height:29">最大（%）</td>
																				<td align=center class="detail-data-body-list" style="align:center;height:29">平均（%）</td>
																			</tr>
																			<% if(cpuperfhash != null && cpuperfhash.size()>0){ 
																				String usr = (String)cpuperfhash.get("%usr");
																				
																				String sys = (String)cpuperfhash.get("%sys");
																				String wio = (String)cpuperfhash.get("%wio");
																				String idle = (String)cpuperfhash.get("%idle");
																				String maxusr = "0";
																				String maxsys = "0";
																				String maxwio = "0";
																				String maxidle = "0";
																				String avgusr = "0";
																				String avgsys = "0";
																				String avgwio = "0";
																				String avgidle = "0";
																				if(cpudetailhash != null){
																					if(cpudetailhash.containsKey("usr")){
																						Hashtable usrhash = (Hashtable)cpudetailhash.get("usr");
																						if(usrhash != null){
																							if(usrhash.containsKey("maxvalue"))maxusr = (String)usrhash.get("maxvalue");
																							if(usrhash.containsKey("avgvalue"))avgusr = (String)usrhash.get("avgvalue");
																						}
																					}
																					if(cpudetailhash.containsKey("sys")){
																						Hashtable syshash = (Hashtable)cpudetailhash.get("sys");
																						if(syshash != null){
																							if(syshash.containsKey("maxvalue"))maxsys = (String)syshash.get("maxvalue");
																							if(syshash.containsKey("avgvalue"))avgsys = (String)syshash.get("avgvalue");
																						}
																					}
																					if(cpudetailhash.containsKey("wio")){
																						Hashtable wiohash = (Hashtable)cpudetailhash.get("wio");
																						if(wiohash != null){
																							if(wiohash.containsKey("maxvalue"))maxwio = (String)wiohash.get("maxvalue");
																							if(wiohash.containsKey("avgvalue"))avgwio = (String)wiohash.get("avgvalue");
																						}
																					}
																					if(cpudetailhash.containsKey("idle")){
																						Hashtable idlehash = (Hashtable)cpudetailhash.get("idle");
																						if(idlehash != null){
																							if(idlehash.containsKey("maxvalue"))maxidle = (String)idlehash.get("maxvalue");
																							if(idlehash.containsKey("avgvalue"))avgidle = (String)idlehash.get("avgvalue");
																						}
																					}
																				}
																			%>
																			<tr>
																				<td align=center class="detail-data-body-list" style="height:29">%用户（usr）</td>
																				<td align=center class="detail-data-body-list"><%=usr%></td>
																				<td align=center class="detail-data-body-list"><%=df.format(Float.parseFloat(maxusr))%></td>
																				<td align=center class="detail-data-body-list"><%=df.format(Float.parseFloat(avgusr))%></td>
																			</tr>
																			<tr>
																				<td align=center class="detail-data-body-list" style="height:29">%系统（sys）</td>
																				<td align=center class="detail-data-body-list"><%=sys%></td>
																				<td align=center class="detail-data-body-list"><%=df.format(Float.parseFloat(maxsys))%></td>
																				<td align=center class="detail-data-body-list"><%=df.format(Float.parseFloat(avgsys))%></td>
																			</tr>
																			<tr>
																				<td align=center class="detail-data-body-list" style="height:29">%IO等待（wio）</td>
																				<td align=center class="detail-data-body-list"><%=wio%></td>
																				<td align=center class="detail-data-body-list"><%=df.format(Float.parseFloat(maxwio))%></td>
																				<td align=center class="detail-data-body-list"><%=df.format(Float.parseFloat(avgwio))%></td>
																			</tr>
																			<tr>
																				<td align=center class="detail-data-body-list" style="height:29">%空闲（idle）</td>
																				<td align=center class="detail-data-body-list"><%=idle%></td>
																				<td align=center class="detail-data-body-list"><%=df.format(Float.parseFloat(maxidle))%></td>
																				<td align=center class="detail-data-body-list"><%=df.format(Float.parseFloat(avgidle))%></td>
																			</tr>
																			<% } %>
																			
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
						</tr>
					</table>
				</td>
			</tr>
		</table>														            		
																</td>		
				<tr>
                  <td align=left valign=top>
                  <br>
                      <table cellpadding="0" cellspacing="0" width=48% >
              							<tr> 
                							<td width="100%"  align=center> 
                								<div id="flashcontent4_page">
													<strong>You need to upgrade your Flash Player</strong>
												</div>
												<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Line.swf?ipadress=<%=host.getIpAddress()%>", "PageSpace", "346", "300", "8", "#ffffff");
													so.write("flashcontent4_page");
												</script>				
							                </td>
										</tr>  
								           
																	</table> 
                      														</td>
                      											<td align=center valign=top>
<br>
                      <table id="body-container" class="body-container" style="background-color:#FFFFFF;" width="40%">
			<tr>
				<td class="td-container-main">
					<table id="container-main" class="container-main">
						<tr>
							<td class="td-container-main-add">
								<table id="container-main-add" class="container-main-add">
									<tr>
										<td style="background-color:#FFFFFF;" >
											<table id="add-content" class="add-content">
												<tr>
													<td>
														<table id="add-content-header" class="add-content-header">
										                	<tr>
											                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
											                	<td class="add-content-title"><b>换页率详情</b></td>
											                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
											       			</tr>
											        	</table>
				        							</td>
				        						</tr>
				        						<tr>
				        							<td>
				        								<table id="detail-content-body" class="detail-content-body" border='1'>
				        									<tr>
				        										<td>
				        										<table  cellpadding="1" cellspacing="1" width=48%>
              							
																<tr>
																	<td valign=top>

																	<table cellpadding="0" cellspacing="0" width=48% align=center>
																	<tr>
																	<td align=center>
																		<img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>pageout.png">	
																	</td>
																	<td align=center>
																		<img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>pageoutmax.png">	
																	</td>
																	<td align=center>
																		<img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>pageoutavg.png">	
																	</td>
																	</tr> 
																	<tr height=20>
																      	<td valign=top align=center><b>当前</b></td>
																	<td valign=top align=center><b>最大</b></td>
																	<td valign=top align=center><b>平均</b></td>
																	</tr>
																																		           
																	</table>

																	</td>
				   
																	</tr> 
								                                  <tr>
																	<td valign=top >
																		<table cellpadding="0" cellspacing="0" width=48% align=center>
																			<tr bgcolor="#FFFFFF">
																				<td align="center" class="detail-data-body-list" style="height:29" >名称</td>
																				<td align="center" class="detail-data-body-list" style="height:29">容量</td>
																				<td align="center" class="detail-data-body-list" style="height:29">当前（%）</td>
																				<td align="center" class="detail-data-body-list" style="height:29">最大（%）</td>
																				<td align="center" class="detail-data-body-list" style="height:29">平均（%）</td>
																			</tr>
																			<%
																				String maxpageused = "0";
																				String avgpageused = "0";
																				if(pagedetailhash != null){
																					if(pagedetailhash.containsKey("maxvalue"))maxpageused = (String)pagedetailhash.get("maxvalue");
																					if(pagedetailhash.containsKey("avgvalue"))avgpageused = (String)pagedetailhash.get("avgvalue");
																				}
																			%>
																			<tr>
																				<td align="center" class="detail-data-body-list" height="29">换页率</td>
																				<td align="center" class="detail-data-body-list">&nbsp;<%=totalpageing%></td>
																				<td align="center" class="detail-data-body-list">&nbsp;<%=pageingused%></td>
																				<td align="center" class="detail-data-body-list">&nbsp;<%=df.format(Float.parseFloat(maxpageused))%></td>
																				<td align="center" class="detail-data-body-list">&nbsp;<%=df.format(Float.parseFloat(avgpageused))%></td>
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