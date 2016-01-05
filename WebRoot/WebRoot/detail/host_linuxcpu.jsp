<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.polling.manage.PollMonitorManager"%>
<%@page import="javax.servlet.jsp.tagext.TryCatchFinally"%>

<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%@page import="com.afunms.detail.service.memoryInfo.MemoryInfoService"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>

<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.common.util.*"%>
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
<%@page import="com.afunms.detail.service.sysInfo.DiskPerfInfoService"%>
<%@page import="com.afunms.detail.service.pingInfo.PingInfoService"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.temp.dao.*"%>
<%@page import="com.afunms.detail.net.service.NetService"%>
<%@page import="com.afunms.temp.model.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%
	String runmodel = PollingEngine.getCollectwebflag();
	String rootPath = request.getContextPath();
	String menuTable = (String) request.getAttribute("menuTable");
	String tmp = request.getParameter("id");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	double cpuvalue = 0;
	String pingconavg = "0";
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
	String pingvalue = "0";
	String maxpingvalue = "0";
	String responsevalue = "0";
	String avgresponse = "0";
	String maxresponse = "0";
	Hashtable diskHashtable = new Hashtable();
	HostNodeDao hostdao = new HostNodeDao();
	List hostlist = hostdao.loadHost();
	List cpuperflist = null;
	Hashtable cpuperfhash = new Hashtable();
	Hashtable memhash = (Hashtable) request.getAttribute("memhash");
	Hashtable memmaxhash = (Hashtable) request
			.getAttribute("memmaxhash");
	Hashtable memavghash = (Hashtable) request
			.getAttribute("memavghash");

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String time1 = sdf.format(new Date());
	String starttime1 = time1 + " 00:00:00";
	String totime1 = time1 + " 23:59:59";

	Host host = (Host) PollingEngine.getInstance().getNodeByID(
			Integer.parseInt(tmp));
	String ipaddress = host.getIpAddress();
	String orderflag = request.getParameter("orderflag");
	if (orderflag == null || orderflag.trim().length() == 0)
		orderflag = "index";
	String[] time = { "", "" };
	DateE datemanager = new DateE();
	Calendar current = new GregorianCalendar();
	current.set(Calendar.MINUTE, 59);
	current.set(Calendar.SECOND, 59);
	time[1] = datemanager.getDateDetail(current);
	current.add(Calendar.HOUR_OF_DAY, -1);
	current.set(Calendar.MINUTE, 0);
	current.set(Calendar.SECOND, 0);
	time[0] = datemanager.getDateDetail(current);
	String starttime = time[0];
	String endtime = time[1];


	I_HostLastCollectData hostlastmanager = new HostLastCollectDataManager();
	Vector vector = new Vector();
	Vector deviceV = new Vector();
	if ("0".equals(runmodel)) {
		//采集与访问是集成模式

		String[] netInterfaceItem = { "index", "ifDescr", "ifSpeed",
				"ifOperStatus", "OutBandwidthUtilHdx",
				"InBandwidthUtilHdx" };
		try {
			vector = hostlastmanager.getInterface_share(host
					.getIpAddress(), netInterfaceItem, orderflag,
					starttime, endtime);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Hashtable hostinfohash = new Hashtable();
		List networkconfiglist = new ArrayList();
		List cpulist = new ArrayList();
		Nodeconfig nodeconfig = new Nodeconfig();
		Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(
				host.getIpAddress());
		if (ipAllData != null) {
			nodeconfig = (Nodeconfig) ipAllData.get("nodeconfig");
			
			if (nodeconfig != null) {
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

			Vector cpuV = (Vector) ipAllData.get("cpu");
			if (cpuV != null && cpuV.size() > 0) {
				CPUcollectdata cpu = (CPUcollectdata) cpuV.get(0);
				if(cpu != null && cpu.getThevalue() != null){
					cpuvalue = new Double(cpu.getThevalue());
				}
			}
			
			//得到系统启动时间
			Vector systemV = (Vector) ipAllData.get("system");
			if (systemV != null && systemV.size() > 0) {
				for (int i = 0; i < systemV.size(); i++) {
					Systemcollectdata systemdata = (Systemcollectdata) systemV
							.get(i);
					if (systemdata.getSubentity().equalsIgnoreCase(
							"sysUpTime")) {
						sysuptime = systemdata.getThevalue();
					}
					if (systemdata.getSubentity().equalsIgnoreCase(
							"sysServices")) {
						sysservices = systemdata.getThevalue();
					}
					if (systemdata.getSubentity().equalsIgnoreCase(
							"sysDescr")) {
						sysdescr = systemdata.getThevalue();
					}
					if (systemdata.getSubentity().equalsIgnoreCase(
							"SysName")) {
						sysname = systemdata.getThevalue();
					}

				}
			}
			
			cpuperflist = (List) ipAllData.get("cpuperflist");
			deviceV = (Vector) ipAllData.get("device");
			
		}
		//得到CPU详细信息
		if (cpuperflist != null)
			cpuperfhash = (Hashtable) cpuperflist.get(0);
		//文件系统
		try {
			diskHashtable = hostlastmanager.getDisk_share(host.getIpAddress(), "Disk", "",
					"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	} else {
		//采集与访问是分离模式
		NodeUtil nodeUtil = new NodeUtil();
		NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(host);

		//String[] netInterfaceItem={"index","ifDescr","ifSpeed","ifOperStatus","OutBandwidthUtilHdx","InBandwidthUtilHdx"};
		//try{
		//	vector = hostlastmanager.getInterface_share(host.getIpAddress(),netInterfaceItem,orderflag,starttime,endtime); 
		// }catch(Exception e){
		//	e.printStackTrace();
		//}
		Hashtable hostinfohash = new Hashtable();
		List networkconfiglist = new ArrayList();
		List cpulist = new ArrayList();
		Nodeconfig nodeconfig = new Nodeconfig();
		NodeconfigDao nodeconfigDao = new NodeconfigDao();
		List deviceList = new ArrayList();
		try {
			nodeconfig = nodeconfigDao.getByNodeID(host.getId() + "");
			deviceList = new NetService(host.getId() + "", nodedto
					.getType(), nodedto.getSubtype()).getDeviceInfo();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			nodeconfigDao.close();
		}
		if (nodeconfig != null) {
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

		if (deviceList != null && deviceList.size() > 0) {
			Devicecollectdata devicedata = null;
			for (int i = 0; i < deviceList.size(); i++) {
				DeviceNodeTemp nodetemp = (DeviceNodeTemp) deviceList
						.get(i);
				devicedata = new Devicecollectdata();
				devicedata.setIpaddress(host.getIpAddress());
				devicedata.setName(nodetemp.getName());
				devicedata.setStatus(nodetemp.getStatus());
				devicedata.setType(nodetemp.getDtype());
				deviceV.addElement(devicedata);
			}
		}
		CpuInfoService cpuInfoService = new CpuInfoService(nodedto
				.getId()
				+ "", nodedto.getType(), nodedto.getSubtype());
		//cpuperflist = cpuInfoService.getCpuPerListInfo();
		Vector cpuV = cpuInfoService.getCpuInfo();
		if (cpuV != null && cpuV.size() > 0) {
			CPUcollectdata cpu = (CPUcollectdata) cpuV.get(0);
			if(cpu != null && cpu.getThevalue() != null){
				cpuvalue = new Double(cpu.getThevalue());
			}
		}

		OthersTempDao tempdao = new OthersTempDao();
		List _cpuperflist = new ArrayList();
		try {
			_cpuperflist = tempdao.getCpuPerfInfoList(ipaddress);
		} catch (Exception e) {

		} finally {
			tempdao.close();
		}
		if (_cpuperflist != null && _cpuperflist.size() > 0) {
			for (int si = 0; si < _cpuperflist.size(); si++) {
				CPUcollectdata cpudata = (CPUcollectdata) _cpuperflist
						.get(si);
				cpuperfhash.put(cpudata.getSubentity(), cpudata
						.getThevalue());
				//name = name + cpudata.getSubentity().replaceAll("%", "") + ";";
			}
		}
		//得到系统启动时间
		SystemInfoService systemInfoService = new SystemInfoService(
				nodedto.getId() + "", nodedto.getType(), nodedto
						.getSubtype());
		Vector systemV = systemInfoService.getSystemInfo();
		if (systemV != null && systemV.size() > 0) {
			for (int i = 0; i < systemV.size(); i++) {
				Systemcollectdata systemdata = (Systemcollectdata) systemV
						.get(i);
				if (systemdata.getSubentity().equalsIgnoreCase(
						"sysUpTime")) {
					sysuptime = systemdata.getThevalue();
				}
				if (systemdata.getSubentity().equalsIgnoreCase(
						"sysServices")) {
					sysservices = systemdata.getThevalue();
				}
				if (systemdata.getSubentity().equalsIgnoreCase(
						"sysDescr")) {
					sysdescr = systemdata.getThevalue();
				}
				if (systemdata.getSubentity().equalsIgnoreCase(
						"SysName")) {
					sysname = systemdata.getThevalue();
				}

			}
		}
		OtherInfoService otherInfoService = new OtherInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype());
		collecttime = otherInfoService.getCollecttime();
		//pingdata
		Vector pingData = new PingInfoService(nodedto.getId() + "",
				nodedto.getType(), nodedto.getSubtype()).getPingInfo();
		if (pingData != null && pingData.size() > 0) {
			Pingcollectdata pingdata = (Pingcollectdata) pingData
					.get(0);
			Calendar tempCal = (Calendar) pingdata.getCollecttime();
			Date cc = tempCal.getTime();
			//collecttime = sdf1.format(cc);
			pingvalue = pingdata.getThevalue();//当前连通率
			pingdata = (Pingcollectdata) pingData.get(1);
			responsevalue = pingdata.getThevalue();//当前响应时间
		}

		//取出当前的硬盘信息 
		DiskInfoService diskInfoService = new DiskInfoService(String
				.valueOf(nodedto.getId()), nodedto.getType(), nodedto
				.getSubtype());
		diskHashtable = diskInfoService.getCurrDiskListInfo();
		//取出当前的内存信息 
		MemoryInfoService memoryInfoService = new MemoryInfoService(
				nodedto.getId() + "", nodedto.getType(), nodedto
						.getSubtype());
		memhash = memoryInfoService.getCurrMemoryListInfo();

	}
	Hashtable ConnectUtilizationhash = new Hashtable();
	Hashtable cpudetailhash = new Hashtable();
	
	 //获取当前连通率和当前时间
	Vector pingData = (Vector) ShareData.getPingdata().get(
				host.getIpAddress());
		if (pingData != null && pingData.size() > 0) {
			Pingcollectdata pingdata = (Pingcollectdata) pingData
					.get(0);
			Calendar tempCal = (Calendar) pingdata.getCollecttime();
			Date cc = tempCal.getTime();
			collecttime = sdf1.format(cc);
			pingvalue = pingdata.getThevalue();//当前连通率
			pingdata = (Pingcollectdata) pingData.get(1);
			responsevalue = pingdata.getThevalue();//当前响应时间
	}
	
	
	I_HostCollectData hostmanager = new HostCollectDataManager();
	
	 //获取平均连通率和最大连通率
	try {
		ConnectUtilizationhash = hostmanager.getCategory(host
				.getIpAddress(), "Ping", "ConnectUtilization",
				starttime1, totime1);
		cpudetailhash = hostmanager.getCpuDetail(host.getIpAddress(),
				starttime1, totime1);
		
	} catch (Exception ex) {
		ex.printStackTrace();
	}
	
	if (ConnectUtilizationhash.get("avgpingcon") != null) {
		pingconavg = (String) ConnectUtilizationhash.get("avgpingcon");
		pingconavg = pingconavg.replaceAll("%", "");
		maxpingvalue = (String) ConnectUtilizationhash.get("pingmax");
		maxpingvalue=maxpingvalue.replaceAll("%","");
	}
    //获取平均响应时间和最大响应时间
	try {
		ConnectUtilizationhash = hostmanager.getCategory(host
				.getIpAddress(), "Ping", "ResponseTime", starttime1,
				totime1);
	} catch (Exception ex) {
		ex.printStackTrace();
	}

	if (ConnectUtilizationhash.get("avgpingcon") != null) {
		avgresponse = (String) ConnectUtilizationhash.get("avgpingcon");
		avgresponse = avgresponse.replace("毫秒", "").replaceAll("%", "");
		maxresponse = (String) ConnectUtilizationhash.get("pingmax");
		maxresponse = maxresponse.replaceAll("%", "");
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

	//String[] memoryItem={"AllSize","UsedSize","Utilization"};
	String[] memoryItem = { "Capability", "Utilization" };
	String[] memoryItemch = { "总容量", "已用容量", "当前利用率", "最大利用率" };
	String[] diskItem = { "AllSize", "UsedSize", "Utilization" };
	String[] diskItemch = { "总容量", "已用容量", "利用率", "i-node已使用",
			"i-node利用率" };
	String[] sysItem = { "sysName", "sysUpTime", "sysContact",
			"sysLocation", "sysServices", "sysDescr" };
	String[] sysItemch = { "设备名", "设备启动时间", "设备联系", "设备位置", "设备服务",
			"设备描述" };
	PollMonitorManager pollMonitorManager = new PollMonitorManager();
	String newip = SysUtil.doip(host.getIpAddress());
	//画图（磁盘是显示最新数据的柱状图）
	try {
		pollMonitorManager.draw_column(diskHashtable, "", newip
				+ "disks", 340, 210);
	} catch (Exception e) {
		e.printStackTrace();
	}
	request.setAttribute("Disk", diskHashtable);

	Hashtable Disk = (Hashtable) request.getAttribute("Disk");

	Hashtable hash = (Hashtable) request.getAttribute("hash");
	Hashtable hash1 = (Hashtable) request.getAttribute("hash1");
	Hashtable max = (Hashtable) request.getAttribute("max");
	Hashtable imgurl = (Hashtable) request.getAttribute("imgurl");

	double avgpingcon = (Double) request.getAttribute("pingconavg");
	int percent1 = Double.valueOf(avgpingcon).intValue();
	int percent2 = 100 - percent1;

	int cpuper = Double.valueOf(cpuvalue).intValue();
	
	//注意初次添加的时候值为空时,将赋予0，保证图片的生成及页面的显示
	String avgcpu ="0";
	String cpumax = "0";
   if(max!=null){
    avgcpu = (String) max.get("cpuavg");
    cpumax = (String) max.get("cpu");
    if(avgcpu==null||avgcpu.equals(""))avgcpu ="0";
    if(cpumax==null||cpumax.equals(""))cpumax ="0";
   }
	Vector ipmacvector = (Vector) request.getAttribute("vector");
	if (ipmacvector == null)
		ipmacvector = new Vector();

	//ResponseTimeItem item1 = (ResponseTimeItem)host.getMoidList().get(0); 
	String chart1 = null, chart2 = null, chart3 = null, responseTime = null;
	DefaultPieDataset dpd = new DefaultPieDataset();
	dpd.setValue("可用率", avgpingcon);
	dpd.setValue("不可用率", 100 - avgpingcon);
	chart1 = ChartCreator.createPieChart(dpd, "", 130, 130);

	//if(item1.getSingleResult()!=-1)
	//{
	//responseTime = item1.getSingleResult() + " ms";

	//SnmpItem item2 = (SnmpItem)host.getItemByMoid(MoidConstants.CISCO_CPU);
	//if(item2!=null&&item2.getSingleResult()!=-1)
	//chart2 = ChartCreator.createMeterChart(item2.getSingleResult(),"",150,150); 
	chart2 = ChartCreator.createMeterChart(cpuvalue, "", 120, 120);

	//SnmpItem item3 = (SnmpItem)host.getItemByMoid(MoidConstants.CISCO_MEMORY);
	//if(item3!=null&&item3.getSingleResult()!=-1)
	chart3 = ChartCreator.createMeterChart(40.0, "", 120, 120);
	//}
	//else
	//responseTime = "无响应"; 

	Vector ifvector = (Vector) request.getAttribute("vector");

	SupperDao supperdao = new SupperDao();
	Supper supper = null;
	String suppername = "";
	try {
		supper = (Supper) supperdao.findByID(host.getSupperid() + "");
		if (supper != null)
			suppername = supper.getSu_name() + "("
					+ supper.getSu_dept() + ")";
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		supperdao.close();
	}

	DecimalFormat df = new DecimalFormat("#.##");
	String ip = host.getIpAddress();
	String picip = CommonUtil.doip(ip);
		 double[] cpu_data1 = null;
		 double[] cpu_data2 = null;
		 double[] cpu_data3 = null;
		 double[] cpu_data4 = null;
		 double[] cpu_data5 = null;
		 double[] cpu_data6 = null;
		 String[] cpu_labels =  {"当前","平均"};;
		String user="0";																					
		String nice="0";																						 
		String system="0";																						
		String iowait="0";																						 
		String steal="0";																						 
		String idle="0";
		 String maxuser = "0";
 		String maxnice = "0";
 		String maxsystem = "0";
 		String maxiowait = "0";
 		String maxsteal = "0";
 		String maxidle = "0";
 		String avguser = "0";
 		String avgnice = "0";
 		String avgsystem = "0";
 		String avgiowait = "0";
 		String avgsteal = "0";
 		String avgidle = "0";																						 
		if (cpuperfhash != null && cpuperfhash.size() > 0) {																						
		user = (String) cpuperfhash.get("%user");																						
		nice = (String) cpuperfhash.get("%nice");																						
		system = (String) cpuperfhash.get("%system");																						
		iowait = (String) cpuperfhash.get("%iowait");																						
		steal = (String) cpuperfhash.get("%steal");
		idle = (String) cpuperfhash.get("%idle");
 		 cpu_data1 = new double[2];
		 cpu_data2 = new double[2];
		 cpu_data3 = new double[2];
		 cpu_data4 = new double[2];																						
		 cpu_data5 = new double[2];																				
		 cpu_data6 = new double[2];	
		 
	
	     if(null !=idle)
		cpu_data1[0]=Double.parseDouble(idle);
		if(null !=steal)
		cpu_data2[0]=Double.parseDouble(steal);
		if(null !=iowait)
		cpu_data3[0]=Double.parseDouble(iowait);
		if(null !=system)
		cpu_data4[0]=Double.parseDouble(system);
		if(null !=nice)
		cpu_data5[0]=Double.parseDouble(nice);
		if(null !=user)
		cpu_data6[0]=Double.parseDouble(user);
		
		
		 if (cpudetailhash != null) {
		 
 			if (cpudetailhash.containsKey("user")) {
 				Hashtable usrhash = (Hashtable) cpudetailhash
 						.get("user");
 				if (usrhash != null) {
 					if (usrhash.containsKey("maxvalue"))
 						maxuser = (String) usrhash.get("maxvalue");
 					if (usrhash.containsKey("avgvalue"))
 						avguser = (String) usrhash.get("avgvalue");
 				}
 			}
 			if (cpudetailhash.containsKey("nice")) {
 				Hashtable nicehash = (Hashtable) cpudetailhash
 						.get("nice");
 				if (nicehash != null) {
 					if (nicehash.containsKey("maxvalue"))
 						maxnice = (String) nicehash.get("maxvalue");
 					if (nicehash.containsKey("avgvalue"))
 						avgnice = (String) nicehash.get("avgvalue");
 				}
 			}
 			if (cpudetailhash.containsKey("system")) {
 				Hashtable wiohash = (Hashtable) cpudetailhash
 						.get("system");
 				if (wiohash != null) {
 					if (wiohash.containsKey("maxvalue"))
 						maxsystem = (String) wiohash.get("maxvalue");
 					if (wiohash.containsKey("avgvalue"))
 						avgsystem = (String) wiohash.get("avgvalue");
 				}
 			}
 			if (cpudetailhash.containsKey("iowait")) {
 				Hashtable wiohash = (Hashtable) cpudetailhash
 						.get("iowait");
 				if (wiohash != null) {
 					if (wiohash.containsKey("maxvalue"))
 						maxiowait = (String) wiohash.get("maxvalue");
 					if (wiohash.containsKey("avgvalue"))
 						avgiowait = (String) wiohash.get("avgvalue");
 				}
 			}
 			if (cpudetailhash.containsKey("steal")) {
 				Hashtable stealhash = (Hashtable) cpudetailhash
 						.get("steal");
 				if (stealhash != null) {
 					if (stealhash.containsKey("maxvalue"))
 						maxsteal = (String) stealhash.get("maxvalue");
 					if (stealhash.containsKey("avgvalue"))
 						avgsteal = (String) stealhash.get("avgvalue");
 				}
 			}
 			if (cpudetailhash.containsKey("idle")) {
 				Hashtable idlehash = (Hashtable) cpudetailhash
 						.get("idle");
 				if (idlehash != null) {
 					if (idlehash.containsKey("maxvalue"))
 						maxidle = (String) idlehash.get("maxvalue");
 					if (idlehash.containsKey("avgvalue"))
 						avgidle = (String) idlehash.get("avgvalue");
 				}
 			}
 			
 			
 		}
 		
 		
 		 System.out.println("============jsp ============================8-1");
		cpu_data1[1]=Double.parseDouble(avgidle);
		System.out.println("============jsp ============================8");
		cpu_data2[1]=Double.parseDouble(avgsteal);
		System.out.println("============jsp ============================8");
		
		cpu_data3[1]=Double.parseDouble(avgiowait);
		System.out.println("============jsp ============================8");
		cpu_data4[1]=Double.parseDouble(avgsystem);
		System.out.println("============jsp ============================8");
		cpu_data5[1]=Double.parseDouble(avgnice);
		System.out.println("============jsp ============================8");
		cpu_data6[1]=Double.parseDouble(avguser);
		System.out.println("============jsp ============================8-6");
		 CreateBarPic cpu_cbp = new CreateBarPic();																					
    TitleModel cpu_tm = new TitleModel();
		 cpu_tm.setPicName(picip+"cpudetail");//
		 cpu_tm.setBgcolor(0x000000);
		 cpu_tm.setXpic(410);
		 cpu_tm.setYpic(170);
		 cpu_tm.setX1(70);
		 cpu_tm.setX2(20);
		 cpu_tm.setX3(320);
		 cpu_tm.setX4(100);
		 cpu_tm.setX5(25);
		 cpu_tm.setX6(130);
		 int cpu_color1 = 0x00FF00;
		 int cpu_color2 = 0x800000;
		 int cpu_color3= 0xFFFF00;
		 int cpu_color4= 0xFF00FF;
		 int cpu_color5= 0xFF1493;
		 int cpu_color6= 0x1E90FF;
		 
		 System.out.println("============jsp ============================8-7");
		
		 cpu_cbp.createCylindricalPiccc(cpu_data1,cpu_data2,cpu_data3,cpu_data4,cpu_data5,cpu_data6,cpu_labels,cpu_tm,"idle","steal","wio","sys","nice","usr",cpu_color1,cpu_color2,cpu_color3,cpu_color4,cpu_color5,cpu_color6);
		}	
	      System.out.println("============jsp ============================8-10");
				CreateAmColumnPic cpudetail=new CreateAmColumnPic();
		 System.out.println("============jsp ============================8-9");		
		String dataStr=cpudetail.createLinuxCpuDetailAmChart(cpuperfhash,cpudetailhash);
		
	System.out.println("============jsp ============================8-8");																			
	//生成连通率图形
	
	if(null !=pingvalue)
	{
	}else
	  {
	    pingvalue="0";
	  }
	Double realValue = Double.valueOf(pingvalue.replaceAll("%", ""));
	CreatePiePicture _cpp = new CreatePiePicture();
	_cpp.createPingPic(picip, realValue); //当前连通率
	_cpp.createAvgPingPic(picip, avgpingcon); //平均连通率
	_cpp.createMinPingPic(picip, maxpingvalue);//最小连通率
	
	 System.out.println("============jsp ============================9");	
	//amcharts 生成连通率图形
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
	
	//生成CPU仪表盘
	CreateMetersPic cmp = new CreateMetersPic();
	cmp.createCpuPic(picip, cpuper);
	cmp.createAvgCpuPic(picip, avgcpu.replaceAll("%", ""));
	cmp.createMaxCpuPic(picip, cpumax.replaceAll("%", ""));

	//生成响应时间图形
	CreateBarPic cbp = new CreateBarPic();
	cbp.createResponseTimePic(picip, responsevalue, maxresponse,
			avgresponse);
%>
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript"
			src="<%=rootPath%>/include/swfobject.js"></script>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>

		<link href="<%=rootPath%>/resource/css/global/global.css"
			rel="stylesheet" type="text/css" />


		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css"
			charset="utf-8" />
		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8" />
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js"
			charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js"
			charset="utf-8"></script>
		<script src="<%=rootPath%>/include/AC_OETags.js" language="javascript"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
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
	document.getElementById('linuxDetailTitle-0').className='detail-data-title';
	document.getElementById('linuxDetailTitle-0').onmouseover="this.className='detail-data-title'";
	document.getElementById('linuxDetailTitle-0').onmouseout="this.className='detail-data-title'";
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
				style="border: thin; font-size: 12px" cellspacing="0">
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.detail()">
						查看状态
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.portset();">
						端口设置
					</td>
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
												<jsp:include page="/topology/includejsp/systeminfo_hostlinux.jsp">
												 <jsp:param name="rootPath" value="<%= rootPath %>"/>
												 <jsp:param name="tmp" value="<%= tmp %>"/>
												 <jsp:param name="hostname" value="<%= hostname %>"/>
												 <jsp:param name="sysname" value="<%= sysname %>"/>
												 <jsp:param name="processornum" value="<%= processornum %>"/>
												 <jsp:param name="sysuptime" value="<%= sysuptime %>"/>
												 <jsp:param name="collecttime" value="<%= collecttime %>"/>
												 <jsp:param name="mac" value="<%= mac %>"/>   
										 		 <jsp:param name="CSDVersion" value="<%=CSDVersion%>"  />  
												 <jsp:param name="picip" value="<%= picip %>"/>	
												 <jsp:param name="pingavg" value="<%=Math.round(Float.parseFloat(pingconavg))%>"/>  
											 </jsp:include>	
											</td>
										</tr>
										<tr>
											<td>
												<table id="detail-data" class="detail-data">
													<tr>
														<td class="detail-data-header">
															<%=linuxDetailTitleTable%>
														</td>
													</tr>
													<tr>
														<td>
															<table class="detail-data-body">
																<tr>
																	<td align=center valign=top>
																		<br>
																		<table cellpadding="0" cellspacing="0" width=48%
																			align=center>
																			<tr>
																				<td width="100%" align=center>
																					<div id="flashcontent3">
																						<strong>You need to upgrade your Flash
																							Player</strong>
																					</div>
																					<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Ping.swf?ipadress=<%=host.getIpAddress()%>", "Ping", "346", "288", "8", "#ffffff");
													so.write("flashcontent3");
												</script>
																				</td>

																			</tr>

																		</table>
																	</td>
																	<td align=center>
																		<br>
																		<table id="body-container" class="body-container"
																			style="background-color: #FFFFFF;" width="40%">
																			<tr>
																				<td class="td-container-main">
																					<table id="container-main" class="container-main">
																						<tr>
																							<td class="td-container-main-add">
																								<table id="container-main-add"
																									class="container-main-add">
																									<tr>
																										<td style="background-color: #FFFFFF;">
																											<table id="add-content" class="add-content">
																												<tr>
																													<td>
																														<table id="add-content-header"
																															class="add-content-header">
																															<tr>
																																<td align="left" width="5">
																																	<img
																																		src="<%=rootPath%>/common/images/right_t_01.jpg"
																																		width="5" height="29" />
																																</td>
																																<td class="add-content-title">
																																	<b>连通率实时</b>
																																</td>
																																<td align="right">
																																	<img
																																		src="<%=rootPath%>/common/images/right_t_03.jpg"
																																		width="5" height="29" />
																																</td>
																															</tr>
																														</table>
																													</td>
																												</tr>
																												<tr>
																													<td>
																														<table id="detail-content-body"
																															class="detail-content-body" border='1'>
																															<tr>
																																<td>
																																	<table cellpadding="1" cellspacing="1"
																																		width=48%>
																																		<tr>
																																			<td valign=top>
																																				<table width="90%" cellpadding="0"
																																					cellspacing="0" border=0>
																																					<tr>
																																						<td valign=top>
																																						<!--  	<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>realping.png">
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
																																						<td valign=top>
																																							<!--<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>minping.png">
																																						-->
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
																																						<!--	<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>pingavg.png">
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
																																					<tr height=28>
																																						<td valign=top align=center>
																																							<b>当前</b>
																																						</td>
																																						<td valign=top align=center>
																																							<b>最小</b>
																																						</td>
																																						<td valign=top align=center>
																																							<b>平均</b>
																																						</td>
																																					</tr>
																																					<tr>
																																						<td width=100% align=center
																																							colspan=3>
																																							<table cellpadding="1"
																																								cellspacing="1"
																																								style="align: center; width: 100%"
																																								bgcolor="#FFFFFF">
																																								<tr bgcolor="#FFFFFF">
																																									<td
																																										class="detail-data-body-list"
																																										style="height: 29"
																																										align=center>
																																										&nbsp;&nbsp;名称&nbsp;&nbsp;
																																									</td>
																																									<td
																																										class="detail-data-body-list"
																																										style="height: 29"
																																										align=center>
																																										当前（%）
																																									</td>
																																									<td
																																										class="detail-data-body-list"
																																										style="height: 29"
																																										align=center>
																																										最小（%）
																																									</td>
																																									<td
																																										class="detail-data-body-list"
																																										style="height: 29"
																																										align=center>
																																										平均（%）
																																									</td>
																																								</tr>
																																								<tr>
																																									<td
																																										class="detail-data-body-list"
																																										height="29" align=center>
																																										连通率
																																									</td>
																																									<td
																																										class="detail-data-body-list"
																																										align=center><%=pingvalue%></td>
																																									<td
																																										class="detail-data-body-list"
																																										align=center><%=Math.round(Float.parseFloat(maxpingvalue.replaceAll("%",
							"")))%></td>
																																									<td
																																										class="detail-data-body-list"
																																										align=center><%=Math.round(Float.parseFloat(pingconavg))%></td>
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
																<tr>
																	<td align=center valign=top>
																		<br>
																		<table cellpadding="0" cellspacing="0" width=48%
																			align=center>
																			<tr>
																				<td width="100%" align=center>
																					<div id="flashcontent5">
																						<strong>You need to upgrade your Flash
																							Player</strong>
																					</div>
																					<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Response_time.swf?ipadress=<%=host.getIpAddress()%>", "Response_time", "346", "320", "8", "#ffffff");
													so.write("flashcontent5");
												</script>
																				</td>

																			</tr>

																		</table>
																	</td>
																	<td align=center>
																		<br>
																		<table id="body-container" class="body-container"
																			style="background-color: #FFFFFF;" width="40%">
																			<tr>
																				<td class="td-container-main">
																					<table id="container-main" class="container-main">
																						<tr>
																							<td class="td-container-main-add">
																								<table id="container-main-add"
																									class="container-main-add">
																									<tr>
																										<td style="background-color: #FFFFFF;">
																											<table id="add-content" class="add-content">
																												<tr>
																													<td>
																														<table id="add-content-header"
																															class="add-content-header">
																															<tr>
																																<td align="left" width="5">
																																	<img
																																		src="<%=rootPath%>/common/images/right_t_01.jpg"
																																		width="5" height="29" />
																																</td>
																																<td class="add-content-title">
																																	<b>响应时间详情</b>
																																</td>
																																<td align="right">
																																	<img
																																		src="<%=rootPath%>/common/images/right_t_03.jpg"
																																		width="5" height="29" />
																																</td>
																															</tr>
																														</table>
																													</td>
																												</tr>
																												<tr>
																													<td>
																														<table id="detail-content-body"
																															class="detail-content-body" border='1'>
																															<tr>
																																<td>
																																	<table cellpadding="1" cellspacing="1"
																																		width=48%>

																																		<tr>
																																			<td valign=top>
																																				<table cellpadding="0"
																																					cellspacing="0" width=48%
																																					align=center>
																																					<tr>
																																						<td align=center colspan=3>
																																							<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>response.png">
																																						</td>
																																					</tr>
																																					<tr height=25>
																																						<td valign=top align=center>
																																							<b>当前</b>
																																						</td>
																																						<td valign=top align=center>
																																							<b>最大</b>
																																						</td>
																																						<td valign=top align=center>
																																							<b>平均</b>
																																						</td>
																																					</tr>
																																				</table>

																																			</td>

																																		</tr>
																																		<tr>
																																			<td width=100% align=center colspan=3>
																																				<table cellpadding="1"
																																					cellspacing="1"
																																					style="align: center; width: 100%"
																																					bgcolor="#FFFFFF">
																																					<tr bgcolor="#FFFFFF">
																																						<td class="detail-data-body-list"
																																							style="height: 29" align=center>
																																							名称
																																						</td>
																																						<td class="detail-data-body-list"
																																							style="height: 29" align=center>
																																							当前（ms）
																																						</td>
																																						<td class="detail-data-body-list"
																																							style="height: 29" align=center>
																																							最大（ms）
																																						</td>
																																						<td class="detail-data-body-list"
																																							style="height: 29" align=center>
																																							平均（ms）
																																						</td>
																																					</tr>
																																					<tr>
																																						<td class="detail-data-body-list"
																																							height="29" align=center>
																																							响应时间
																																						</td>
																																						<td class="detail-data-body-list"
																																							align=center><%=responsevalue%></td>
																																						<td class="detail-data-body-list"
																																							align=center><%=maxresponse%></td>
																																						<td class="detail-data-body-list"
																																							align=center><%=avgresponse%></td>
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
																		<table cellpadding="0" cellspacing="0" width=48%
																			align=center>
																			<tr>
																				<td width="100%" align=center>
																					<div id="flashcontent1">
																						<strong>You need to upgrade your Flash
																							Player</strong>
																					</div>
																					<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Line_CPU.swf?ipadress=<%=host.getIpAddress()%>", "Line_CPU", "346", "320", "8", "#ffffff");
													so.write("flashcontent1");
												</script>
																				</td>
																			</tr>

																		</table>
																	</td>
																	<td align=center valign=top>
																		<br>
																		<table id="body-container" class="body-container"
																			style="background-color: #FFFFFF;" width="40%">
																			<tr>
																				<td class="td-container-main">
																					<table id="container-main" class="container-main">
																						<tr>
																							<td class="td-container-main-add">
																								<table id="container-main-add"
																									class="container-main-add">
																									<tr>
																										<td style="background-color: #FFFFFF;">
																											<table id="add-content" class="add-content">
																												<tr>
																													<td>
																														<table id="add-content-header"
																															class="add-content-header">
																															<tr>
																																<td align="left" width="5">
																																	<img
																																		src="<%=rootPath%>/common/images/right_t_01.jpg"
																																		width="5" height="29" />
																																</td>
																																<td class="add-content-title">
																																	<b>CPU利用率详情</b>
																																</td>
																																<td align="right">
																																	<img
																																		src="<%=rootPath%>/common/images/right_t_03.jpg"
																																		width="5" height="29" />
																																</td>
																															</tr>
																														</table>
																													</td>
																												</tr>
																												<tr>
																													<td>
																														<table id="detail-content-body"
																															class="detail-content-body" border='1'>
																															<tr>
																																<td>
																																	<table cellpadding="1" cellspacing="1"
																																		width=48%>

																																		<tr>
																																			<td valign=top>



																																				<table cellpadding="0"
																																					cellspacing="0" width=48%
																																					align=center>
																																					<tr>
																																						<td align=center>
																																							<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>cpu.png">
																																						</td>
																																						<td align=center>
																																							<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>cpumax.png">
																																						</td>
																																						<td align=center>
																																							<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>cpuavg.png">
																																						</td>
																																					</tr>
																																					<tr height=20>
																																						<td valign=top align=center>
																																							<b>当前</b>
																																						</td>
																																						<td valign=top align=center>
																																							<b>最大</b>
																																						</td>
																																						<td valign=top align=center>
																																							<b>平均</b>
																																						</td>
																																					</tr>
																																					<tr height=20>
																																						<td colspan=3>
																																							&nbsp;
																																						</td>
																																					</tr>
																																					<tr>
																																						<td width=100% align=center
																																							colspan=3>
																																							<table cellpadding="0"
																																								cellspacing="1"
																																								style="align: center; width: 100%"
																																								bgcolor="#FFFFFF">
																																								<tr bgcolor="#FFFFFF">
																																									<td height="29"
																																										class="detail-data-body-list"
																																										style="height: 29"
																																										align=center>
																																										名称
																																									</td>
																																									<td
																																										class="detail-data-body-list"
																																										style="height: 29"
																																										align=center>
																																										当前
																																									</td>
																																									<td
																																										class="detail-data-body-list"
																																										style="height: 29"
																																										align=center>
																																										最大
																																									</td>
																																									<td
																																										class="detail-data-body-list"
																																										style="height: 29"
																																										align=center>
																																										平均
																																									</td>
																																								</tr>
																																								<tr>
																																									<td
																																										class="detail-data-body-list"
																																										height="29" align=center>
																																										&nbsp;CPU利用率
																																									</td>
																																									<td
																																										class="detail-data-body-list"
																																										align=center>
																																										&nbsp;<%=cpuper%>%
																																									</td>
																																									<td
																																										class="detail-data-body-list"
																																										align=center>
																																										&nbsp;<%=cpumax%></td>
																																									<td
																																										class="detail-data-body-list"
																																										align=center>
																																										&nbsp;<%=avgcpu%></td>
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
																																	<table cellpadding="1" cellspacing="1"
																																		width=80%>
																																		<tr>
																																			<td valign=top width=80%>
																																				<table cellpadding="0"
																																					cellspacing="0" width=80%
																																					align=center>
																																					<%
																																						if (deviceV != null) {
																																							for (int m = 0; m < deviceV.size(); m++) {
																																								Devicecollectdata devicedata = (Devicecollectdata) deviceV
																																										.get(m);
																																								String name = devicedata.getName();
																																								String type = devicedata.getType();
																																								if (!"CPU".equals(type))
																																									continue;
																																								String status = devicedata.getStatus();
																																					%>
																																					<tr height=20>
																																						<td valign=top align=center>
																																							<b>类型</b>：<%=type%></td>
																																						<td valign=top align=center>
																																							<b>描述</b>：<%=name%></td>
																																						<td valign=top align=center>
																																							<b>状态</b>：<%=status%></td>
																																					</tr>
																																					<%
																																						}
																																						}
																																					%>
																																				</table>

																																			</td>

																																		</tr>
																																		<tr height=20>
																																			<td valign=center align=right
																																				colspan=3>
																																				>>
																																				<a href="#">实 时</a> &nbsp;&nbsp;

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
																		<table cellpadding="0" cellspacing="0" width=48%>
																			<tr>
																				<td width="100%" align=center>
																					<div id="flashcontent2">
																						<strong>You need to upgrade your Flash
																							Player</strong>
																					</div>
																					<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Area_Memory.swf?ipadress=<%=host.getIpAddress()%>", "Area_flux", "346", "207", "8", "#ffffff");
													so.write("flashcontent2");
												</script>
																				</td>
																			</tr>
																			<tr>
																				<td align=center>

																					<table width="90%" cellpadding="0" cellspacing="0"
																						style="align: center; width: 346">
																						<tr align="center" height="28"
																							style="background-color: #EEEEEE;">
																							<td width="21%" class="detail-data-body-title"
																								style="height: 29">
																								内存名
																							</td>
																							<%
																								String pmem = "";
																								String vmem = "";
																								String pcurmem = "";
																								String pmaxmem = "";
																								String pavgmem = "";
																								String vcurmem = "";
																								String vmaxmem = "";
																								String vavgmem = "";
																								for (int j = 0; j < memoryItemch.length; j++) {
																									String item = memoryItemch[j];
																							%>
																									<td class="detail-data-body-title"><%=item%></td>
																							<%
																								}
																							%>
																						</tr>
																						<%
																							for (int k = 0; k < memhash.size(); k++) {
																								Hashtable mhash = (Hashtable) (memhash.get(new Integer(k)));
																								String name = (String) mhash.get("name");
																						%>
																						<tr>
																							<td width="20%" class="detail-data-body-list"height="29">&nbsp;<%=name%></td>
																							<%
																								for (int j = 0; j < memoryItem.length; j++) {
																										String value = "";
																										if (mhash.get(memoryItem[j]) != null) {
																											value = (String) mhash.get(memoryItem[j]);
																											if (j == 0) {
																												if ("PhysicalMemory".equals(name))
																													pmem = value;
																												if ("SwapMemory".equals(name))
																													vmem = value;
																											} else {
																												if ("PhysicalMemory".equals(name))
																													pcurmem = value;
																												if ("SwapMemory".equals(name))
																													vcurmem = value;
																											}
																										}
																							%>
																							<td width="17%" class="detail-data-body-list">&nbsp;<%=value%></td>
																							<%
																								}
																									String value = "";
																									if (memmaxhash.get(name) != null) {
																										value = (String) memmaxhash.get(name);
																										if ("PhysicalMemory".equals(name))
																											pmaxmem = value;
																										if ("SwapMemory".equals(name))
																											vmaxmem = value;
																									}
																									String avgvalue = "";
																									if (memavghash.get(name) != null) {
																										avgvalue = (String) memavghash.get(name);
																										if ("PhysicalMemory".equals(name))
																											pavgmem = avgvalue;
																										if ("SwapMemory".equals(name))
																											vavgmem = avgvalue;
																									}
																							%>
																							<td width="20%" class="detail-data-body-list">
																								&nbsp;<%=value%></td>
																							<td width="20%" class="detail-data-body-list">
																								&nbsp;<%=avgvalue%></td>
																						</tr>
																						<%
																							}
																						%>
																					</table>

																				</td>
																			</tr>

																		</table>
																	</td>
																	<td align=center valign=top>
																		<br>
																		<table id="body-container" class="body-container"
																			style="background-color: #FFFFFF;" width="40%">
																			<tr>
																				<td class="td-container-main">
																					<table id="container-main" class="container-main">
																						<tr>
																							<td class="td-container-main-add">
																								<table id="container-main-add"
																									class="container-main-add">
																									<tr>
																										<td style="background-color: #FFFFFF;">
																											<table id="add-content" class="add-content">
																												<tr>
																													<td>
																														<table id="add-content-header"
																															class="add-content-header">
																															<tr>
																																<td align="left" width="5">
																																	<img
																																		src="<%=rootPath%>/common/images/right_t_01.jpg"
																																		width="5" height="29" />
																																</td>
																																<td class="add-content-title">
																																	<b>设备内存明细</b>
																																</td>
																																<td align="right">
																																	<img
																																		src="<%=rootPath%>/common/images/right_t_03.jpg"
																																		width="5" height="29" />
																																</td>
																															</tr>
																														</table>
																													</td>
																												</tr>
																												<tr>
																													<td valign=top>
																														<table id="detail-content-body"
																															class="detail-content-body" border='1'>
																															<tr>
																																<td valign=top>
																																	<table cellpadding="1" cellspacing="1"
																																		width=48%>

																																		<tr>
																																			<td valign=top>

																																				<%
																																					if (pcurmem == null || pcurmem.equals(""))
																																						pcurmem = "0";
																																					if (pmaxmem == null || pmaxmem.equals(""))
																																						pmaxmem = "0";
																																					if (pavgmem == null || pavgmem.equals(""))
																																						pavgmem = "0";
																																					if (vcurmem == null || vcurmem.equals(""))
																																						vcurmem = "0";
																																					if (vmaxmem == null || vmaxmem.equals(""))
																																						vmaxmem = "0";
																																					if (vavgmem == null || vavgmem.equals(""))
																																						vavgmem = "0";
																																					pcurmem = pcurmem.replaceAll("%", "");
																																					pmaxmem = pmaxmem.replaceAll("%", "");
																																					pavgmem = pavgmem.replaceAll("%", "");
																																					vcurmem = vcurmem.replaceAll("%", "");
																																					vmaxmem = vmaxmem.replaceAll("%", "");
																																					vavgmem = vavgmem.replaceAll("%", "");

																																					double dpcurmem = new Double(pcurmem);
																																					double dpmaxmem = new Double(pmaxmem);
																																					double dpavgmem = new Double(pavgmem);

																																					double dvcurmem = new Double(vcurmem);
																																					double dvmaxmem = new Double(vmaxmem);
																																					double dvavgmem = new Double(vavgmem);

																																					//生成双环
																																					//CreatePiePicture _cpp = new CreatePiePicture();
																																					TitleModel _titleModel = new TitleModel();
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
																																					_titleModel.setPicName(picip + "dmemory");
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

																																					double[] d_data1 = { dpcurmem, 100 - dpcurmem };
																																					double[] d_data2 = { dvcurmem, 100 - dvcurmem };
																																					String[] d_labels = { "已使用", "未使用" };
																																					int[] d_colors = { 0x66ff66, 0x8080ff };
																																					String d_title1 = "物理";
																																					String d_title2 = "虚拟";
																																					_cpp.createTwoConcentricDonutChart(d_data1, d_title1, d_data2,
																																							d_title2, d_labels, d_colors, _titleModel);

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
																																					_titleModel.setPicName(picip + "dmaxmemory");
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

																																					double[] dmax_data1 = { dpmaxmem, 100 - dpmaxmem };
																																					double[] dmax_data2 = { dvmaxmem, 100 - dvmaxmem };
																																					_cpp.createTwoConcentricDonutChart(dmax_data1, d_title1,
																																							dmax_data2, d_title2, d_labels, d_colors, _titleModel);

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
																																					_titleModel.setPicName(picip + "davgmemory");
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

																																					double[] davg_data1 = { dpavgmem, 100 - dpavgmem };
																																					double[] davg_data2 = { dvavgmem, 100 - dvavgmem };
																																					
																																					_cpp.createTwoConcentricDonutChart(davg_data1, d_title1,
																																							davg_data2, d_title2, d_labels, d_colors, _titleModel);
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
																																						long maxv = Math.round(dmax_data2[0]);
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
																																					String	temp = xmlStr.toString();
																																				%>

																																				<table width="100%" cellpadding="0"
																																					cellspacing="0" border=0>
																																					<tr>
																																						<td valign=top>
																																						<!-- 	<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>dmemory.png">
																																						-->
																																							<div id="flashcontent">
																																								<strong>You need to
																																									upgrade your Flash Player</strong>
																																							</div>

																																							<script type="text/javascript">
				
		                                                                                                                                                      var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "amcolumn", "470", "210", "8", "#FFFFFF");
		                                                                                                                                                       so.addVariable("path", "<%=rootPath%>/amchart/");
		                                                                                                                                                       so.addVariable("settings_file",  escape("<%=rootPath%>/amcharts_settings/memorypercent_settings.xml"));
	                        
	                                                                                                                                                            so.addVariable("chart_data", "<%=temp%>");
		                                                                                                                                                        so.addVariable("preloader_color", "#999999");
		                                                                                                                                                           so.write("flashcontent");
	                                                                                                                                                     </script>
																						
																																						</td>
																																					<!--	<td valign=top>
																																							<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>dmaxmemory.png">
																																					
																																						</td>
																																						<td valign=top>
																																							<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>davgmemory.png">
																																						
																																						</td>-->
																																					</tr>
																																					<!--<tr>
																																						<td valign=top align=center>
																																							<b>当前</b>
																																						</td>
																																						<td valign=top align=center>
																																							<b>最大</b>
																																						</td>
																																						<td valign=top align=center>
																																							<b>平均</b>
																																						</td>
																																					</tr>-->
																																					<tr height=22>
																																						<td  align=center
																																							colspan=3>

																																							<table cellpadding="0"
																																								cellspacing="0" width=48%
																																								align=center>
																																								<tr height=20>
																																									<td valign=top align=center>
																																										<b>物理内存总量</b>：<%=pmem%></td>
																																									<td valign=top align=center>
																																										<b>虚拟内存总量</b>：<%=vmem%></td>
																																								</tr>
																																							</table>

																																						</td>
																																					</tr>
																																					<tr height=15>
																																						<td  align=right
																																							colspan=3>
																																							>>
																																							<a
																																								href="javascript:void(window.open('<%=rootPath%>/monitor.do?action=memorydetail&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>','内存实时数据','top=200,left=300,height=350 ,width=720, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no'))">实
																																								时</a> &nbsp;&nbsp;

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
																		<table cellpadding="0" cellspacing="0" width=48%>
																			<tr>
																				<td width="100%" align=center>
																					<div id="flashcontent3_cpudtl">
																						<strong>You need to upgrade your Flash
																							Player</strong>
																					</div>
																					<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Cpu_detail.swf?ipadress=<%=host.getIpAddress()%>", "CPU详细", "346", "400", "8", "#ffffff");
													so.write("flashcontent3_cpudtl");
												</script>
																				</td>
																			</tr>
																			
																		</table>
																	</td>
                                                                  <td align=center valign=top>
																		<br>
																		<table id="body-container" class="body-container"
																			style="background-color: #FFFFFF;" width="40%">
																			<tr>
																				<td class="td-container-main">
																					<table id="container-main" class="container-main">
																						<tr>
																							<td class="td-container-main-add">
																								<table id="container-main-add"
																									class="container-main-add">
																									<tr>
																										<td style="background-color: #FFFFFF;">
																											<table id="add-content" class="add-content">
																												<tr>
																													<td>
																														<table id="add-content-header"
																															class="add-content-header">
																															<tr>
																																<td align="left" width="5">
																																	<img
																																		src="<%=rootPath%>/common/images/right_t_01.jpg"
																																		width="5" height="29" />
																																</td>
																																<td class="add-content-title">
																																	<b>CPU信息详情</b>
																																</td>
																																<td align="right">
																																	<img
																																		src="<%=rootPath%>/common/images/right_t_03.jpg"
																																		width="5" height="29" />
																																</td>
																															</tr>
																														</table>
																													</td>
																												</tr>
																												<tr>
																													<td valign=top>
																														<table id="detail-content-body"
																															class="detail-content-body" border='1'>
																															<tr>
																																<td valign=top>
																																	<table cellpadding="1" cellspacing="1"
																																		width=48%>

																																		<tr>
																																			<td valign=top>

																																				<table width="90%" cellpadding="0"
																																					cellspacing="0" border=0>
																																					<tr>
																																						
																																						<td valign=top>
																																						<!--  	<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>cpudetail.png">
																																						 -->
																																						 <div id="cpudetail">
								                                        <strong></strong>
							                                        </div>
							                                        <script type="text/javascript">
							                                         <%  if(!dataStr.equals("0")){%>
		                                                               var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "amcolumn", "470", "210", "8", "#FFFFFF");
		                                                                   so.addVariable("path", "<%=rootPath%>/amchart/");
		                                                                   so.addVariable("settings_file",  escape("<%=rootPath%>/amcharts_settings/cpuUtilPercent_settings.xml"));
	                                                                       so.addVariable("chart_data", "<%=dataStr%>");
		                                                                   so.addVariable("preloader_color", "#999999");
		                                                                   so.write("cpudetail");
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
																					<table cellpadding="1" cellspacing="1"
																						style="align: center; width: 98%"
																						bgcolor="#FFFFFF">
																						<tr bgcolor="#FFFFFF">
																							<td height="29" class="detail-data-body-list"
																								style="height: 29" align=center>
																								名称
																							</td>
																							<td class="detail-data-body-list"
																								style="height: 29" align=center>
																								当前（%）
																							</td>
																							<td class="detail-data-body-list"
																								style="height: 29" align=center>
																								最大（%）
																							</td>
																							<td class="detail-data-body-list"
																								style="height: 29" align=center>
																								平均（%）
																							</td>
																						</tr>
										
																						<tr>
																							<td class="detail-data-body-list" height="29" align=center>
																								%用户（user）
																							</td>
																							<td class="detail-data-body-list" align=center>
																								&nbsp;<%=df.format(Float.parseFloat(user))%></td>
																							<td class="detail-data-body-list" align=center>
																								&nbsp;<%=df.format(Float.parseFloat(maxuser))%></td>
																							<td class="detail-data-body-list" align=center>
																								&nbsp;<%=df.format(Float.parseFloat(avguser))%></td>
																						</tr>
																						<tr>
																							<td class="detail-data-body-list" height="29" align=center>
																								%低等级（nice）
																							</td>
																							<td class="detail-data-body-list" align=center>
																								&nbsp;<%=df.format(Float.parseFloat(nice))%></td>
																							<td class="detail-data-body-list" align=center>
																								&nbsp;<%=df.format(Float.parseFloat(maxnice))%></td>
																							<td class="detail-data-body-list" align=center>
																								&nbsp;<%=df.format(Float.parseFloat(avgnice))%></td>
																						</tr>
																						<tr>
																							<td class="detail-data-body-list" height="29" align=center>
																								%系统（system）
																							</td>
																							<td class="detail-data-body-list" align=center>
																								&nbsp;<%=df.format(Float.parseFloat(system))%></td>
																							<td class="detail-data-body-list" align=center>
																								&nbsp;<%=df.format(Float.parseFloat(maxsystem))%></td>
																							<td class="detail-data-body-list" align=center>
																								&nbsp;<%=df.format(Float.parseFloat(avgsystem))%></td>
																						</tr>
																						<tr>
																							<td class="detail-data-body-list" height="29" align=center>
																								%IO等待（iowait）
																							</td>
																							<td class="detail-data-body-list" align=center>
																								&nbsp;<%=df.format(Float.parseFloat(iowait))%></td>
																							<td class="detail-data-body-list" align=center>
																								&nbsp;<%=df.format(Float.parseFloat(maxiowait))%></td>
																							<td class="detail-data-body-list" align=center>
																								&nbsp;<%=df.format(Float.parseFloat(avgiowait))%></td>
																						</tr>
																						<tr>
																							<td class="detail-data-body-list" height="29" align=center>
																								%其他（steal）
																							</td>
																							<td class="detail-data-body-list" align=center>
																								&nbsp;<%=df.format(Float.parseFloat(steal))%></td>
																							<td class="detail-data-body-list" align=center>
																								&nbsp;<%=df.format(Float.parseFloat(maxsteal))%></td>
																							<td class="detail-data-body-list" align=center>
																								&nbsp;<%=df.format(Float.parseFloat(avgsteal))%></td>
																						</tr>
																						<tr>
																							<td class="detail-data-body-list" height="29" align=center>
																								%空闲（idle）
																							</td>
																							<td class="detail-data-body-list" align=center>
																								&nbsp;<%=df.format(Float.parseFloat(idle))%></td>
																							<td class="detail-data-body-list" align=center>
																								&nbsp;<%=df.format(Float.parseFloat(maxidle))%></td>
																							<td class="detail-data-body-list" align=center>
																								&nbsp;<%=df.format(Float.parseFloat(avgidle))%></td>
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
																<tr>
																	<td align=center  style="width: 95%" valign=top colspan=2>
																		<br>
																		<table style="width: 98%" border=1>
																			<tr>
																				<td width=98%>
																					<table width="98%">
																						<tr>
																							<td align="left" height=30 bgcolor="#ECECEC">
																								&nbsp;
																								<B>磁盘利用率</B>
																							</td>
																						</tr>
																					</table>
																				</td>
																			</tr>
																			<tr>
																																					<%
																																					String[] disknames=null;
																												String[] percents=null;
																						                          StringBuffer sbf=new StringBuffer();
																						                        String[] colorStrs=new String[] {"#33FF33","#FF0033","#9900FF","#FFFF00","#33CCFF","#003366","#33FF33","#FF0033","#9900FF","#FFFF00","#333399","#0000FF","#A52A2A","#23f266"};
																						                        sbf.append("<chart><series>");
																													if (Disk != null && Disk.size() > 0) {
																														// 写磁盘
																														disknames=new String[Disk.size()];
																														percents=new String[Disk.size()];
																														for (int i = 0; i < Disk.size(); i++) {
																												
																														Hashtable diskhash = (Hashtable) (Disk.get(new Integer(i)));
																																String name = (String) diskhash.get("name");
																																disknames[i]=name;
																													
																														for (int j = 0; j < diskItem.length; j++) {
																																	String value = "";
																																	if (diskhash.get(diskItem[j]) != null) {
																																		value = (String) diskhash.get(diskItem[j]);
																																	}
																													
																													percents[i]=value;
																														}
																													
																													}
																													for(int k=0;k<disknames.length;k++){
																													sbf.append("<value xid='").append(k).append("'>").append(disknames[k]).append("</value>");
																													
																													}
																													sbf.append("</series><graphs><graph gid='0'>");
																													for(int j=0;j<percents.length;j++){
																													int perInt=Math.round(Float.parseFloat(percents[j].replaceAll("%","")));
																													sbf.append("<value xid='").append(j).append("' color='").append(colorStrs[j]).append("'>"+perInt).append("</value>");
																													}
																													sbf.append("</graph></graphs></chart>");
																													}
																												%>
																																						<td align=center colspan=3>
																																						<!-- 	<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>response.png">
																																						 -->
																																						   <div id="netfalsh">
							                                                                                                                                    <strong>You need to upgrade your Flash Player</strong>
						                                                                                                                                       </div>
						                                                                                                                                       <script type="text/javascript"
							                                                                                                                                   src="<%=rootPath%>/include/swfobject.js"></script>
						                                                                                                                                       <script type="text/javascript">
						                                                                                                                                        var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "ampie","720", "288", "8", "#FFFFFF");
						                                                                                                                                             so.addVariable("path", "<%=rootPath%>/amchart/");
						                                                                                                                                             so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/dbpercent_settings.xml"));
						                                                                                                                                             so.addVariable("chart_data","<%=sbf.toString()%>");
						                                                                                                                                             so.write("netfalsh");
						                                                                                                                                          </script>	
																																						 </td>
																																					</tr>
																			<tr>
																				<td width="98%">
																					<table width="98%">
																					
																						<tr>
																							<td align=center width=98%>
																								
																								<table cellpadding="0" cellspacing="0">
																									<tr>
																										<td align=center width=98%>
																									 		
																											<table border="1" align=center>
																												<tr>
																													<td class="detail-data-body-list" height="40" align=center>
																														磁盘使用情况
																													</td>
																													<td class="detail-data-body-list" height="40" align=center>
																														磁盘名
																													</td >
																													<td class="detail-data-body-list" height="40" align=center>
																														总容量
																													</td>
																													<td class="detail-data-body-list" height="40" align=center>
																														已用容量
																													</td>
																													<td class="detail-data-body-list" height="40" align=center>
																														利用率
																													</td>
																												</tr>
																												<%
																											 
																													if (Disk != null && Disk.size() > 0) {
																												
																														for (int i = 0; i < Disk.size(); i++) {
																														
																												%>
																												<tr>
																													<td class="detail-data-body-list" height="45" align=center>
																														&nbsp;
																													</td>
																													<%
																														Hashtable diskhash = (Hashtable) (Disk.get(new Integer(i)));
																																String name = (String) diskhash.get("name");
																													
																													%>
																													<td class="detail-data-body-list" height="45" align=center><%=name%></td>
																													<%
																														for (int j = 0; j < diskItem.length; j++) {
																																	String value = "";
																																	if (diskhash.get(diskItem[j]) != null) {
																																		value = (String) diskhash.get(diskItem[j]);
																																	}
																													%>
																													<td class="detail-data-body-list" height="45" align=center><%=value%></td>
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
																		<table cellpadding="0" cellspacing="0" width=48%>
																			<tr>
																				<td width="100%" align=center>
																					<div id="flashcontent6">
																						<strong>You need to upgrade your Flash
																							Player</strong>
																					</div>
																					<script type="text/javascript">
								var so = new SWFObject("<%=rootPath%>/flex/Area_Disk_month.swf?ipadress=<%=host.getIpAddress()%>&id=2", "Area_Disk_month", "770", "350", "8", "#ffffff");
								so.write("flashcontent6");
							</script>
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
									<jsp:include page="/include/toolbar.jsp">
										<jsp:param value="<%=host.getIpAddress()%>" name="ipaddress" />
										<jsp:param value="<%=host.getSysOid()%>" name="sys_oid" />
										<jsp:param value="<%=tmp%>" name="tmp" />
										<jsp:param value="host" name="category" />
										<jsp:param value="linux" name="subtype" />
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
