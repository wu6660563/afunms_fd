<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>


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

<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>

<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.detail.net.service.NetService"%>
<%@page import="com.afunms.temp.model.NodeTemp"%>
<%@page import="com.afunms.temp.model.*"%>
<%@ page import="com.afunms.polling.loader.HostLoader"%>

<%
	String temp = "";
	String runmodel = PollingEngine.getCollectwebflag();
	String menuTable = (String) request.getAttribute("menuTable");
	String[] diskItem = { "AllSize", "UsedSize", "Utilization" };
	String[] diskItemch = { "总容量", "已用容量", "利用率" };

	String[] memoryItem = { "Capability", "Utilization" };
	String[] memoryItemch = { "容量", "当前", "最大", "平均" };

	List cpulist = new ArrayList();
	Vector deviceV = new Vector();

	Hashtable memmaxhash = (Hashtable) request
			.getAttribute("memmaxhash");
	Hashtable memavghash = (Hashtable) request
			.getAttribute("memavghash");
	Hashtable diskhash = new Hashtable();
	Hashtable memhash = new Hashtable();

	String memcollecttime = "";
	String pingcollecttime = "";
	String avgcpu = "";
	String cpumax = "";
	Hashtable max = (Hashtable) request.getAttribute("max");
	if (max != null) {
		avgcpu = (String) max.get("cpuavg");
		cpumax = (String) max.get("cpu");
	}
	if (avgcpu.equals("") || avgcpu == null)
		avgcpu = "0";
	if (cpumax.equals("") || cpumax == null)
		cpumax = "0";
	String begindate = "";
	String enddate = "";

	String tmp = request.getParameter("id");
	String _flag = (String) request.getAttribute("flag");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	double cpuvalue = 0;
	String pingconavg = "0";
	String collecttime = "";
	String sysuptime = "";
	String sysservices = "";
	String sysdescr = "";
	String syslocation = "";
	String vvalue = "0";
	String pvalue = "0";
	float capvalue = 0f;
	float fvvalue = 0f;
	String vused = "0";
	String maxpingvalue = "0";
	String responsevalue = "0";
	String pingvalue = "0";

	int cpuper = 0;

	Host host = (Host) PollingEngine.getInstance().getNodeByID(
			Integer.parseInt(tmp));
	if (host == null) {
		//从数据库里获取
		HostNodeDao hostdao = new HostNodeDao();
		HostNode node = null;
		try {
			node = (HostNode) hostdao.findByID(tmp);
		} catch (Exception e) {
		} finally {
			hostdao.close();
		}
		HostLoader loader = new HostLoader();
		loader.loadOne(node);
		loader.close();
		host = (Host) PollingEngine.getInstance().getNodeByID(
				Integer.parseInt(tmp));
	}
	NodeUtil nodeUtil = new NodeUtil();
	NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(host);

	String ipaddress = host.getIpAddress();
	//CommonUtil commutil = new CommonUtil();
	String picip = CommonUtil.doip(ipaddress);
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
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String time1 = sdf.format(new Date());
	String starttime1 = time1 + " 00:00:00";
	String totime1 = time1 + " 23:59:59";
	Vector memoryVector = new Vector();
	
	if ("0".equals(runmodel)) {
		//采集与访问是集成模式
		Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(
				host.getIpAddress());
		if (ipAllData != null) {
			Vector cpuV = (Vector) ipAllData.get("cpu");
			if (cpuV != null && cpuV.size() > 0) {
				CPUcollectdata cpu = (CPUcollectdata) cpuV.get(0);
				if (cpu != null && cpu.getThevalue() != null) {
					cpuvalue = new Double(cpu.getThevalue());
				}
			}
			memhash = (Hashtable) request.getAttribute("memhash");
			diskhash = (Hashtable) request.getAttribute("diskhash");
			//得到内存利用率
			memoryVector = (Vector) ipAllData.get("memory");
			if (memoryVector != null && memoryVector.size() > 0) {
				for (int i = 0; i < memoryVector.size(); i++) {
					Memorycollectdata memorydata = (Memorycollectdata) memoryVector
							.get(i);
					if ("VirtualMemory".equalsIgnoreCase(memorydata
							.getSubentity())
							&& "Utilization"
									.equalsIgnoreCase(memorydata
											.getEntity())) {
						vvalue = Math.round(Float.parseFloat(memorydata
								.getThevalue()))
								+ "";
						fvvalue = Float.parseFloat(memorydata
								.getThevalue());
					} else if ("PhysicalMemory"
							.equalsIgnoreCase(memorydata.getSubentity())
							&& "Utilization"
									.equalsIgnoreCase(memorydata
											.getEntity())) {
						pvalue = Math.round(Float.parseFloat(memorydata
								.getThevalue()))
								+ "";
					}
					if ("VirtualMemory".equalsIgnoreCase(memorydata
							.getSubentity())
							&& "Capability".equalsIgnoreCase(memorydata
									.getEntity())) {
						capvalue = Float.parseFloat(memorydata
								.getThevalue());
					}
					if (i == 0) {
						SimpleDateFormat _sdf1 = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						Calendar tempCal = (Calendar) memorydata
								.getCollecttime();
						Date cc = tempCal.getTime();
						//Date d = _sdf1.parse(nodetemp.getCollecttime());
						//Calendar c = Calendar.getInstance();
						//c.setTime(d);
						_sdf1 = new SimpleDateFormat("MM-dd HH:mm");
						memcollecttime = _sdf1.format(cc);
					}
				}
				DecimalFormat df = new DecimalFormat("#.##");
				vused = df.format(capvalue * fvvalue / 100) + "";
			}

			deviceV = (Vector) ipAllData.get("device");
    
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
				}
			}
			Vector pingData = (Vector) ShareData.getPingdata().get(
					host.getIpAddress());
			if (pingData != null && pingData.size() > 0) {
				Pingcollectdata pingdata = (Pingcollectdata) pingData
						.get(0);
				Calendar tempCal = (Calendar) pingdata.getCollecttime();
				Date cc = tempCal.getTime();
				collecttime = sdf1.format(cc);
				SimpleDateFormat _sdf1 = new SimpleDateFormat(
						"MM-dd HH:mm");
				pingcollecttime = _sdf1.format(cc);
				pingvalue = pingdata.getThevalue();
				pingdata = (Pingcollectdata) pingData.get(1);
				responsevalue = pingdata.getThevalue();
			}
			if (ipAllData != null) {
				//Vector cpuV = (Vector)ipAllData.get("cpu");
				if (cpuV != null && cpuV.size() > 0) {
					CPUcollectdata cpu = (CPUcollectdata) cpuV.get(0);
					if (cpu != null && cpu.getThevalue() != null) {
						cpuvalue = new Double(cpu.getThevalue());
						cpuper = Double.valueOf(cpuvalue).intValue();
					}
					//cpuvalue = cpuper+"";

				}
				cpulist = (List) ipAllData.get("cpulist");
				if (cpulist == null)
					cpulist = new ArrayList();
			}
		}

	} else {
	
		//采集与访问是分离模式
		List systemList = new ArrayList();
		List pingList = new ArrayList();
		List memoryList = new ArrayList();
		List flashList = new ArrayList();
		List deviceList = new ArrayList();
		try {
			systemList = new NetService(host.getId() + "", nodedto
					.getType(), nodedto.getSubtype()).getSystemInfo();
			pingList = new NetService(host.getId() + "", nodedto
					.getType(), nodedto.getSubtype()).getCurrPingInfo();
			cpuvalue = new Double(new NetService(host.getId() + "",
					nodedto.getType(), nodedto.getSubtype())
					.getCurrCpuAvgInfo());
			cpuper = Double.valueOf(cpuvalue).intValue();
			;
			memoryList = new NetService(host.getId() + "", nodedto
					.getType(), nodedto.getSubtype())
					.getCurrMemoryInfo();
			deviceList = new NetService(host.getId() + "", nodedto
					.getType(), nodedto.getSubtype()).getDeviceInfo();
			//flashList = new NetService(host.getId()+"", nodedto.getType(), nodedto.getSubtype()).getCurrFlashInfo();
			//pingconavg = new NetService(host.getId()+"", nodedto.getType(), nodedto.getSubtype()).getCurrDayPingAvgInfo();
		} catch (Exception e) {
		}
		if (memoryList != null && memoryList.size() > 0) {
			for (int i = 0; i < memoryList.size(); i++) {
				Memorycollectdata memorycollectdata = new Memorycollectdata();
				NodeTemp nodetemp = (NodeTemp) memoryList.get(i);
				if (i == 0) {
					SimpleDateFormat _sdf1 = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					Date d = _sdf1.parse(nodetemp.getCollecttime());
					//Calendar c = Calendar.getInstance();
					//c.setTime(d);
					_sdf1 = new SimpleDateFormat("MM-dd HH:mm");
					memcollecttime = _sdf1.format(d);
				}
				memorycollectdata.setSubentity(nodetemp.getSindex());
				memorycollectdata.setEntity(nodetemp.getSubentity());
				memorycollectdata.setThevalue(nodetemp.getThevalue());
				memoryVector.add(memorycollectdata);
			}
		}

		//得到内存利用率
		if (memoryVector != null && memoryVector.size() > 0) {
			for (int i = 0; i < memoryVector.size(); i++) {
				Memorycollectdata memorydata = (Memorycollectdata) memoryVector
						.get(i);
				if ("VirtualMemory".equalsIgnoreCase(memorydata
						.getSubentity())
						&& "Utilization".equalsIgnoreCase(memorydata
								.getEntity())) {
					vvalue = Math.round(Float.parseFloat(memorydata
							.getThevalue()))
							+ "";
					fvvalue = Float
							.parseFloat(memorydata.getThevalue());
				} else if ("PhysicalMemory".equalsIgnoreCase(memorydata
						.getSubentity())
						&& "Utilization".equalsIgnoreCase(memorydata
								.getEntity())) {
					pvalue = Math.round(Float.parseFloat(memorydata
							.getThevalue()))
							+ "";
				}
				if ("VirtualMemory".equalsIgnoreCase(memorydata
						.getSubentity())
						&& "Capability".equalsIgnoreCase(memorydata
								.getEntity())) {
					capvalue = Float.parseFloat(memorydata
							.getThevalue());
				}
			}
			DecimalFormat df = new DecimalFormat("#.##");
			vused = df.format(capvalue * fvvalue / 100) + "";
		}
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

		I_HostLastCollectData hostlastmanager = new HostLastCollectDataManager();
		try {
			memhash = hostlastmanager.getMemory(host.getIpAddress(),
					"Memory", starttime, endtime);
			diskhash = hostlastmanager.getDisk(host.getIpAddress(),
					"Disk", starttime, endtime);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (systemList != null && systemList.size() > 0) {
			for (int i = 0; i < systemList.size(); i++) {
				NodeTemp nodetemp = (NodeTemp) systemList.get(i);
				if ("sysUpTime".equals(nodetemp.getSindex()))
					sysuptime = nodetemp.getThevalue();
				if ("sysDescr".equals(nodetemp.getSindex()))
					sysdescr = nodetemp.getThevalue();
				if ("sysLocation".equals(nodetemp.getSindex()))
					syslocation = nodetemp.getThevalue();
			}
		}
		if (pingList != null && pingList.size() > 0) {
			for (int i = 0; i < pingList.size(); i++) {
				NodeTemp nodetemp = (NodeTemp) pingList.get(i);
				if ("ConnectUtilization".equals(nodetemp.getSindex())) {
					collecttime = nodetemp.getCollecttime();
					pingvalue = nodetemp.getThevalue();
					SimpleDateFormat _sdf1 = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					Date d = _sdf1.parse(nodetemp.getCollecttime());
					//Calendar c = Calendar.getInstance();
					//c.setTime(d);
					_sdf1 = new SimpleDateFormat("MM-dd HH:mm");
					pingcollecttime = _sdf1.format(d);
				}
				if ("ResponseTime".equals(nodetemp.getSindex())) {
					responsevalue = nodetemp.getThevalue();
				}
			}
		}
	}
	Hashtable ConnectUtilizationhash = new Hashtable();
	I_HostCollectData hostmanager = new HostCollectDataManager();
	try {
		ConnectUtilizationhash = hostmanager.getCategory(host
				.getIpAddress(), "Ping", "ConnectUtilization",
				starttime1, totime1);
	} catch (Exception ex) {
		ex.printStackTrace();
	}

	if (ConnectUtilizationhash.get("avgpingcon") != null) {
		pingconavg = (String) ConnectUtilizationhash.get("avgpingcon");
		pingconavg = pingconavg.replace("%", "");
		maxpingvalue = (String) ConnectUtilizationhash.get("pingmax");
		maxpingvalue = maxpingvalue.replaceAll("%", "");
	}

	try {
		ConnectUtilizationhash = hostmanager.getCategory(host
				.getIpAddress(), "Ping", "ResponseTime", starttime1,
				totime1);
	} catch (Exception ex) {
		ex.printStackTrace();
	}

	//----得到响应时间 
	String avgresponse = "0";//-----------
	String maxresponse = "0";//----------- 

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

	double avgpingcon = new Double(pingconavg);
	int percent1 = Double.valueOf(avgpingcon).intValue();
	int percent2 = 100 - percent1;

	String rootPath = request.getContextPath();

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

	//生成连通率饼图
	//CreatePiePicture cpp = new CreatePiePicture();
	//double[] data = {avgpingcon, 100-avgpingcon};
	//String[] _labels = {"连通", "未连通"};
	//int[] colors = {0x80ff80, 0xff0000};
	//TitleModel titleModel = new TitleModel();
	//titleModel.setXpic(150);
	//titleModel.setYpic(140);
	//titleModel.setX1(80);
	//titleModel.setX2(50);
	//titleModel.setX3(50);
	//titleModel.setBgcolor(0xffffff);
	//titleModel.setPictype("png");
	//titleModel.setPicName(picip+"ping");
	//titleModel.setTopTitle("");
	//cpp.createPieChartWithLegend(_labels,colors,data,titleModel);  

	CreateMetersPic cmp = new CreateMetersPic();
	cmp.createCpuPic(picip, cpuper); //生成CPU仪表盘
	cmp.createMaxCpuPic(picip, cpumax); //生成CPU最大值仪表盘
	cmp.createAvgCpuPic(picip, avgcpu); //生成CPU平均值仪表盘

	//生成内存利用率图   

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
	_titleModel.setPicName(picip + "pingavg");
	_titleModel.setTopTitle("");

	double[] _data1 = { avgpingcon, 100 - avgpingcon };
	//double[] _data2 = {77, 87};
	String[] p_labels = { "连通", "未连通" };
	int[] _colors = { 0x66ff66, 0xff0000 };
	String _title1 = "第一季度";
	String _title2 = "第二季度";
	_cpp.createOneRingChart(_data1, p_labels, _colors, _titleModel);

	//生成当前连通率图形
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
	ping_titleModel.setPicName(picip + "realping");
	ping_titleModel.setTopTitle("");

	double[] ping_data1 = { avgpingcon, 100 - avgpingcon };
	String[] ping_labels = { "连通", "未连通" };
	int[] ping_colors = { 0x66ff66, 0xff0000 };
	_cpp.createOneRingChart(ping_data1, ping_labels, ping_colors,
			ping_titleModel);
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
	ping_titleModel.setPicName(picip + "minping");
	ping_titleModel.setTopTitle("");
	double d_maxping = new Double(maxpingvalue);
	double[] minping_data1 = { d_maxping, 100 - d_maxping };
	//String[] ping_labels = {"连通", "未连通"};
	//int[] ping_colors = {0x66ff66, 0xff0000}; 
	_cpp.createOneRingChart(minping_data1, ping_labels, ping_colors,
			ping_titleModel);
	CreateBarPic cbp = new CreateBarPic();
	TitleModel tm = new TitleModel();

	cbp = new CreateBarPic();

	double[] r_data1 = { new Double(responsevalue),
			new Double(maxresponse), new Double("0.0") };
	String[] r_labels = { "当前响应时间(ms)", "最大响应时间(ms)", "平均响应时间(ms)" };
	tm = new TitleModel();
	tm.setPicName(picip + "response");//
	tm.setBgcolor(0xffffff);
	tm.setXpic(450);//图片长度
	tm.setYpic(180);//图片高度
	tm.setX1(30);//左面距离
	tm.setX2(20);//上面距离
	tm.setX3(400);//内图宽度
	tm.setX4(130);//内图高度
	tm.setX5(10);
	tm.setX6(115);
	cbp.createTimeBarPic(r_data1, r_labels, tm, 40);
	tm.setPicName(picip + "response1");//
	cbp.createRoundTimeBarPic(r_data1, r_labels, tm, 40);
/**
	CreateBarPic disk_cbp = new CreateBarPic();
	double[] disk_data1 = new double[diskhash.size()];
	double[] disk_data2 = new double[diskhash.size()];
	String[] disk_labels = new String[diskhash.size()];

    System.out.println("================1=================================================");



	for (int k = 0; k < diskhash.size(); k++) {
		Hashtable dhash = (Hashtable) (diskhash.get(new Integer(k)));

		String name = "";
		if (dhash.get("name") != null) {
			name = (String) dhash.get("name");
			// System.out.println("====="+name);
		}
		for (int j = 0; j < diskItem.length; j++) {
			String value = "";
			if (dhash.get(diskItem[j]) != null) {

				value = (String) dhash.get(diskItem[j]);
				disk_data1[k] = new Double(value.replaceAll("G", "")
						.replaceAll("%", "").replaceAll("M", ""));
				disk_data2[k] = 100 - disk_data1[k];
				disk_labels[k] = name;

			}

		}
	}

	TitleModel disk_tm = new TitleModel();
	disk_tm.setPicName(picip + "disk");//
	disk_tm.setBgcolor(0x000000);
	disk_tm.setXpic(450);
	disk_tm.setYpic(170);
	disk_tm.setX1(50);
	disk_tm.setX2(20);
	disk_tm.setX3(360);
	disk_tm.setX4(100);
	disk_tm.setX5(160);
	disk_tm.setX6(140);
	int disk_color1 = 0x80ff80;
	int disk_color2 = 0x8080ff;
	disk_cbp.createCylindricalPic(disk_data1, disk_data2, disk_labels,
			disk_tm, "已使用", "未使用", disk_color1, disk_color2);
	**/	
	  //磁盘的利用率	
	 CreateAmColumnPic amColumnPic=new CreateAmColumnPic();
	 String dataStr=amColumnPic.createWinDiskChart(diskhash);
			
    //amchar 连通率
   Double realValue = Double.valueOf(pingvalue.replaceAll("%", ""));
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
		<script type="text/javascript"
			src="<%=rootPath%>/include/swfobject.js"></script>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>

		<link href="<%=rootPath%>/resource/css/global/global.css"
			rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>

		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

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
		<script type="text/javascript"
			src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/resource/xml/flush/amcolumn/swfobject.js"></script>
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

		<script>
function createQueryString(){
		var user_name = encodeURI($("#user_name").val());
		var passwd = encodeURI($("#passwd").val());
		var queryString = {userName:user_name,passwd:passwd};
		return queryString;
	}
function gzmajax2(){
$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=ajaxUpdate_memory&tmp=<%=tmp%>&nowtime="+(new Date()),
			success:function(data){
			var vvalue=data.vvalue;
			var pvalue=data.pvalue;
			var pppvalue=100-pvalue;
			var vvvvalue=100-vvalue;
			var vused=data.vused;
			$('#PFTABLE').html('<tr><td width="'+vvalue+'%" bgcolor="green" align=center>&nbsp;&nbsp;'+vused+'GB</td><td width="'+pppvalue+'%" bgcolor=#ffffff></td></tr>');
			$('#WLTABLE').html('<tr><td width="'+pvalue+'%" bgcolor="green" align=center>&nbsp;&nbsp;'+pvalue+'%</td><td width="'+pppvalue+'%" bgcolor=#ffffff></td></tr>');
			$('#XNTABLE').html('<tr><td width="'+vvalue+'%" bgcolor="green" align=center>&nbsp;&nbsp;'+vvalue+'%</td><td width="'+vvvvalue+'%" bgcolor=#ffffff></td></tr>');
			}
		});
}
$(document).ready(function(){
	//$("#testbtn").bind("click",function(){
	//gzmajax();
	//});
setInterval(gzmajax2,60000);
});
</script>

		<script type="text/javascript">
	function closetime(){
		Ext.MessageBox.hide();
	}
	function gzmajax_PF(){
	Ext.MessageBox.wait('数据加载中，请稍后.. ');
	$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=ajaxMemory_fresh&tmp=<%=tmp%>&nowtime="+(new Date()),
			success:function(data){
			var vvalue=data.vvalue;
			var pvalue=data.pvalue;
			var pppvalue=100-pvalue;
			var vused=data.vused;
			$('#PFTABLE').html('<tr><td width="'+vvalue+'%" bgcolor="green" align=center>&nbsp;&nbsp;'+vused+'GB</td><td width="'+pppvalue+'%" bgcolor=#ffffff></td></tr>');
			}
		});
}
	function gzmajax_WL(){
	Ext.MessageBox.wait('数据加载中，请稍后.. ');
	$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=ajaxMemory_fresh&tmp=<%=tmp%>&nowtime="+(new Date()),
			success:function(data){
			var vvalue=data.vvalue;
			var pvalue=data.pvalue;
			var pppvalue=100-pvalue;
			var vused=data.vused;
			$('#WLTABLE').html('<tr><td width="'+pvalue+'%" bgcolor="green" align=center>&nbsp;&nbsp;'+pvalue+'%</td><td width="'+pppvalue+'%" bgcolor=#ffffff></td></tr>');
			}
		});
}
	function gzmajax_XN(){
	Ext.MessageBox.wait('数据加载中，请稍后.. ');
	$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=ajaxMemory_fresh&tmp=<%=tmp%>&nowtime="+(new Date()),
			success:function(data){
			var vvalue=data.vvalue;
			var pvalue=data.pvalue;
			var pppvalue=100-pvalue;
			var vvvvalue=100-vvalue;
			var vused=data.vused;
			$('#XNTABLE').html('<tr><td width="'+vvalue+'%" bgcolor="green" align=center>&nbsp;&nbsp;'+vvalue+'%</td><td width="'+vvvvalue+'%" bgcolor=#ffffff></td></tr>');
			}
		});
}
	function fresh_PF(){
		gzmajax_PF();
		setTimeout(closetime,2000);
	}
	function fresh_WL(){
		gzmajax_WL();
		setTimeout(closetime,2000);
	}
	function fresh_XN(){
		gzmajax_XN();
		setTimeout(closetime,2000);
	}
</script>

		<script language="javascript">	

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

function cpuReportByMinute(){
	window.open("<%=rootPath%>/monitor.do?action=hostcpu_report&id=<%=tmp%>&timeType=minute","newWindow","height=300, width=800, top=0, left= 200,screenX=0,screenY=0");
  }
  
  
  
  function memoryReportByMinute(){
	window.open("<%=rootPath%>/monitor.do?action=hostmemory_report&id=<%=tmp%>&timeType=minute","newWindow","height=300, width=800, top=0, left= 200,screenX=0,screenY=0");
   
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
	document.getElementById('hostDetailTitle-0').className='detail-data-title';
	document.getElementById('hostDetailTitle-0').onmouseover="this.className='detail-data-title'";
	document.getElementById('hostDetailTitle-0').onmouseout="this.className='detail-data-title'";
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

// zhubinhua  add   
//用于在服务器上生成对应Flex图片,,然后生成word文档  下载
function outputWord() {
	//alert("YES!");
	//先生成图片
	window["Area_flux"].createImage();
	window["Response_time"].createImage();
	window["Area_Disk"].createImage();
	window["Column_Disk"].createImage();
	window["Line_CPU"].createImage();
	window["Ping"].createImage();
	
	//window.open("<%=rootPath%>/monitor.do?action=createWord");
	var a = document.getElementById("outputWord");
	a.href="<%=rootPath%>/monitor.do?action=createWord&whattype=host&id=<%=tmp%>";
}
</script>


	</head>
	<body id="body" class="body" onload="initmenu();">
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
												<jsp:include page="/topology/includejsp/systeminfo_host.jsp">
													<jsp:param name="rootPath" value="<%=rootPath%>" />
													<jsp:param name="tmp" value="<%=tmp%>" />
													<jsp:param name="sysdescr" value="<%=sysdescr%>" />
													<jsp:param name="sysuptime" value="<%=sysuptime%>" />
													<jsp:param name="sysuptime" value="<%=sysuptime%>" />
													<jsp:param name="collecttime" value="<%=collecttime%>" />
													<jsp:param name="pvalue" value="<%=pvalue%>" />
													<jsp:param name="vvalue" value="<%=vvalue%>" />
													<jsp:param name="vused" value="<%=vused%>" />
													<jsp:param name="picip" value="<%=picip%>" />
													<jsp:param name="pingavg" value="<%=Math.round(Float.parseFloat(pingconavg))%>"/>
													<jsp:param name="avgresponse" value="<%=avgresponse%>" />
												</jsp:include>
											</td>
										</tr>
										<tr>
											<td>
												<table id="detail-data" class="detail-data">
													<tr>
														<td class="detail-data-header">
															<%=hostDetailTitleTable%>
														</td>
													</tr>
													<tr>
														<td>
															<table class="detail-data-body">
																<!--<tr>
																<td>
																	&nbsp;
																</td>
																<td align="right">
																	<a id="outputWord" onclick="outputWord()" href=""><img name="selDay1" alt='导出word' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_word.gif" width=18  border="0">导出word</a>&nbsp;&nbsp;
																</td>
														</tr>-->
																<!--<tr>
																<td>
																	&nbsp;
																</td>
																<td align="right">
																<table>
																<tr>
																<td align=right width=40%><img name="selDay1" src="<%=rootPath%>/resource/image/topo/server-B-24.gif" border="0"></td>
																<td align=center valign=top width=20% background="<%=rootPath%>/resource/image/topo/arrow/right.gif" style="text-align: center;background-repeat:no-repeat;valign:top;" ><%=pingvalue%>%</td>
																<td align=left width=40%><img name="selDay1" src="<%=rootPath%>/resource/image/topo/server/22.gif" border="0"></td>
																</tr>
																<tr>
																<td align=right>网管服务器</td>
																<td align=center valign=top><%=pingvalue%>%</td>
																<td align=left><%=host.getIpAddress()%></td>
																</tr>
																</table>
																	
																</td>
														</tr>-->
																<tr>

																	<td align=center width=43% valign=top>
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
																					var so = new SWFObject("<%=rootPath%>/flex/Ping.swf?ipadress=<%=host.getIpAddress()%>", "Ping", "346", "191", "8", "#ffffff");
																					so.write("flashcontent3");
																				</script>
																				</td>
																			</tr>
																			<tr>
																				<td width=100% align=center>
																					<table cellpadding="1" cellspacing="1"
																						style="align: center; width: 346"
																						bgcolor="#FFFFFF">
																						<tr bgcolor="#FFFFFF">
																							<td height="29" class="detail-data-body-title"
																								style="height: 29">
																								名称
																							</td>
																							<td class="detail-data-body-title"
																								style="height: 29">
																								当前（%）
																							</td>
																							<td class="detail-data-body-title"
																								style="height: 29">
																								最小（%）
																							</td>
																							<td class="detail-data-body-title"
																								style="height: 29">
																								平均（%）
																							</td>
																							<td class="detail-data-body-title"
																								style="height: 29">
																								采集时间
																							</td>
																						</tr>
																						<tr>
																							<td class="detail-data-body-list" height="29">
																								连通率
																							</td>
																							<td class="detail-data-body-list"><%=pingvalue%></td>
																							<td class="detail-data-body-list"><%=Math.round(Float.parseFloat(maxpingvalue))%></td>
																							<td class="detail-data-body-list"><%=percent1%></td>
																							<td class="detail-data-body-list"><%=pingcollecttime%></td>
																						</tr>
																					</table>
																				</td>
																			</tr>
																		</table>
																	</td>
																	<td align=left valign=top width=57%>

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
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>realping.png">-->
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
																																						<!--	<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>minping.png">-->
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
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>pingavg.png">-->
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
																					<div id="flashcontent_5">
																						<strong>You need to upgrade your Flash
																							Player</strong>
																					</div>
																					<script type="text/javascript">
																					var so = new SWFObject("<%=rootPath%>/flex/Response_time.swf?ipadress=<%=host.getIpAddress()%>", "Response_time", "346", "196", "8", "#ffffff");
																					so.write("flashcontent_5");
																				</script>
																				</td>
																			</tr>
																			<tr>
																				<td width=100% align=center>
																					<table cellpadding="1" cellspacing="1"
																						style="align: center; width: 346"
																						bgcolor="#FFFFFF">
																						<tr bgcolor="#FFFFFF">
																							<td class="detail-data-body-title"
																								style="height: 29" height="29">
																								名称
																							</td>
																							<td class="detail-data-body-title"
																								style="height: 29">
																								当前（ms）
																							</td>
																							<td class="detail-data-body-title"
																								style="height: 29">
																								最大（ms）
																							</td>
																							<td class="detail-data-body-title"
																								style="height: 29">
																								平均（ms）
																							</td>
																							<td class="detail-data-body-title"
																								style="height: 29">
																								采集时间
																							</td>
																						</tr>
																						<tr>
																							<td class="detail-data-body-list" height="29">
																								响应时间
																							</td>
																							<td class="detail-data-body-list"><%=responsevalue%></td>
																							<td class="detail-data-body-list"><%=maxresponse%></td>
																							<td class="detail-data-body-list"><%=avgresponse%></td>
																							<td class="detail-data-body-list"><%=pingcollecttime%></td>
																						</tr>
																					</table>
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
																																					<tr height=30>
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




																<%
																	if (host.getCollecttype() != 3 && host.getCollecttype() != 4) {
																%>
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
																					var so = new SWFObject("<%=rootPath%>/flex/Line_CPU.swf?ipadress=<%=host.getIpAddress()%>", "Line_CPU", "346", "230", "8", "#ffffff");
																					so.write("flashcontent1");
																				</script>
																				</td>
																			</tr>
																			<tr>
																				<td width=100% align=center>
																					<table cellpadding="0" cellspacing="1"
																						style="align: center; width: 346"
																						bgcolor="#FFFFFF">
																						<tr bgcolor="#FFFFFF">
																							<td height="29" class="detail-data-body-title"
																								style="height: 29">
																								名称
																							</td>
																							<td class="detail-data-body-title"
																								style="height: 29">
																								当前
																							</td>
																							<td class="detail-data-body-title"
																								style="height: 29">
																								最大
																							</td>
																							<td class="detail-data-body-title"
																								style="height: 29">
																								平均
																							</td>
																						</tr>
																						<tr>
																							<td class="detail-data-body-list" height="29">
																								&nbsp;CPU利用率
																							</td>
																							<td class="detail-data-body-list">
																								&nbsp;<%=cpuper%>%
																							</td>
																							<td class="detail-data-body-list">
																								&nbsp;<%=cpumax%></td>
																							<td class="detail-data-body-list">
																								&nbsp;<%=avgcpu%></td>
																						</tr>
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
																																					if(deviceV!=null&&deviceV.size()>0){
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
																																				<a
																																					href="javascript:void(window.open('<%=rootPath%>/monitor.do?action=cpudetail&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>','cpu利用率','top=200,left=300,height=490,width=660, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no'))">实
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

																<tr>
																	<td align=center valign=top>
																		<br>
																		<table cellpadding="0" cellspacing="0" width=48%
																			align=center valign=top>
																			<tr>
																				<td width="100%" align=center>
																					<div id="flashcontent_2">
																						<strong>You need to upgrade your Flash
																							Player</strong>
																					</div>
																					<script type="text/javascript">
																					var so = new SWFObject("<%=rootPath%>/flex/Area_Memory.swf?ipadress=<%=host.getIpAddress()%>", "Area_flux", "346", "200", "8", "#ffffff");
																					so.write("flashcontent_2");
																				</script>
																				</td>
																			</tr>
																			<tr>
																				<td align=center valign=top>

																					<table width="90%" cellpadding="0" cellspacing="0"
																						style="align: center; valign: top; width: 346">
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
																							<td width="20%" class="detail-data-body-list"
																								height="29">
																								&nbsp;<%=name%></td>
																							<%
																								for (int j = 0; j < memoryItem.length; j++) {
																											String value = "";
																											if (mhash.get(memoryItem[j]) != null) {
																												value = (String) mhash.get(memoryItem[j]);
																												if (j == 0) {
																													if ("PhysicalMemory".equals(name))
																														pmem = value;
																													if ("VirtualMemory".equals(name))
																														vmem = value;
																												} else {
																													if ("PhysicalMemory".equals(name))
																														pcurmem = value;
																													if ("VirtualMemory".equals(name))
																														vcurmem = value;
																												}
																											}
																							%>
																							<td width="17%" class="detail-data-body-list">
																								&nbsp;<%=value%></td>
																							<%
																								}
																										String value = "";
																										if (memmaxhash.get(name) != null) {
																											value = (String) memmaxhash.get(name);
																											if ("PhysicalMemory".equals(name))
																												pmaxmem = value;
																											if ("VirtualMemory".equals(name))
																												vmaxmem = value;
																										}
																										String avgvalue = "";
																										if (memavghash.get(name) != null) {
																											avgvalue = (String) memavghash.get(name);
																											if ("PhysicalMemory".equals(name))
																												pavgmem = avgvalue;
																											if ("VirtualMemory".equals(name))
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
																																						//_titleModel = new TitleModel();
																																						//_titleModel.setXpic(150);
																																						//_titleModel.setYpic(150);//160, 200, 150, 100
																																						//_titleModel.setX1(75);//外环向左的位置
																																						//_titleModel.setX2(60);//外环向上的位置
																																						//_titleModel.setX3(50);//环宽度
																																						//_titleModel.setX4(73);
																																						//_titleModel.setX5(90);//外环TEXT与左的位置
																																						//_titleModel.setX6(105);//外环TEXT与上的位置
																																						//_titleModel.setX7(10);
																																						//_titleModel.setX8(120);
																																						//_titleModel.setBgcolor(0xffffff);
																																						//_titleModel.setPictype("png");
																																						//_titleModel.setPicName(picip + "davgmemory");
																																						//_titleModel.setTopTitle("");

																																						//_titleModel.setR2x(100);
																																						//_titleModel.setR2y(100);
																																						//_titleModel.setR2x1(50);
																																						//_titleModel.setR2x2(30);
																																						//_titleModel.setR2x3(40);
																																						//_titleModel.setR2x4(30);
																																						//_titleModel.setR2textx(50);
																																						//_titleModel.setR2texty(20);
																																						//_titleModel.setInr2x(25);//挪小环向左的位置
																																						//_titleModel.setInr2y(30);//挪内环向上的距离	        

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
																																						temp = xmlStr.toString();
																																				%>

																																				<table width="90%" cellpadding="0"
																																					cellspacing="0" border=0>
																																					<!--  <tr>
			      <td valign=top>
					<img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>dmemory.png">
	  			</td>
	   			<td valign=top>
                     			<img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>dmaxmemory.png">
                   		</td>
                   		<td valign=top>
                     			<img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>davgmemory.png">
                   		</td>
	  		</tr> -->
																																					<tr>
																																						<td colspan=1>
																																							<div id="flashcontent">
																																								<strong>You need to
																																									upgrade your Flash Player</strong>
																																							</div>

																																							<script type="text/javascript">
				
		                        var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "amcolumn", "475", "210", "8", "#FFFFFF");
		                        so.addVariable("path", "<%=rootPath%>/amchart/");
		                        so.addVariable("settings_file",  escape("<%=rootPath%>/amcharts_settings/memorypercent_settings.xml"));
	                        //	so.addVariable("data_file",  escape("<%=rootPath%>/amcharts_data/memorypercent_data.xml"));
	                            so.addVariable("chart_data", "<%=temp%>");
		                        so.addVariable("preloader_color", "#999999");
		                        so.write("flashcontent");
	                             </script>
																																						</td>
																																					</tr>
																																					<!-- 
	  		<tr>
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
	  		 -->
																																					<tr height=20>
																																						<td valign=center align=center
																																							colspan=1>

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
																																						<td valign=center align=right colspan=3>
																																							>><a href="javascript:void(window.open('<%=rootPath%>/monitor.do?action=memorydetail&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>','内存实时数据','top=200,left=300,height=490,width=660, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no'))">实时</a> &nbsp;&nbsp;
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
																					<div id="flashcontent6">
																						<strong>You need to upgrade your Flash
																							Player</strong>
																					</div>
																					<script type="text/javascript">
																					var so = new SWFObject("<%=rootPath%>/flex/Area_Disk.swf?ipadress=<%=host.getIpAddress()%>", "Area_Disk", "346", "360", "8", "#ffffff");
																					so.write("flashcontent6");
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
																																	<b>磁盘利用率详情</b>
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
																																						<td align=center colspan=1>
																																						<!-- 	<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>disk.png">
																																						-->
																																						<div id="diskUtil">
																																								<strong></strong>
																																							</div>

																																							<script type="text/javascript">
				
		                                                                                                                                                           var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "amcolumn", "480", "210", "8", "#FFFFFF");
		                                                                                                                                                               so.addVariable("path", "<%=rootPath%>/amchart/");
		                                                                                                                                                               so.addVariable("settings_file",  escape("<%=rootPath%>/amcharts_settings/cpuUtilPercent_settings.xml"));
	                                                                                                                                                                 //	so.addVariable("data_file",  escape("<%=rootPath%>/amcharts_data/memorypercent_data.xml"));
	                                                                                                                                                                   so.addVariable("chart_data", "<%=dataStr%>");
		                                                                                                                                                               so.addVariable("preloader_color", "#999999");
		                                                                                                                                                               so.write("diskUtil");
	                                                                                                                                                        </script>
																																						</td>
																																					</tr>


																																					<tr>
																																						<td>
																																							<table cellpadding="0"
																																								cellspacing="0" width=30%
																																								align=center>
																																								<tr>
																																									<td valign=top align=center
																																										style="height: 29"
																																										class="detail-data-body-list">
																																										磁盘名
																																									</td>
																																									<td valign=top align=center
																																										style="height: 29"
																																										class="detail-data-body-list">
																																										总容量
																																									</td>
																																									<td valign=top align=center
																																										style="height: 29"
																																										class="detail-data-body-list">
																																										已用容量
																																									</td>
																																									<td valign=top align=center
																																										style="height: 29"
																																										class="detail-data-body-list">
																																										利用率
																																									</td>

																																								</tr>
																																								<%
																																									if (diskhash != null && diskhash.size() > 0) {
																																											// 写磁盘
																																											for (int i = 0; i < diskhash.size(); i++) {
																																								%>
																																								<tr>
																																									<%
																																										Hashtable diskhash1 = (Hashtable) (diskhash
																																															.get(new Integer(i)));
																																													String name = (String) diskhash1.get("name");
																																									%>
																																									<td
																																										class="detail-data-body-list"
																																										align=center
																																										height="29;width=10%"><%=name%></td>
																																									<%
																																										for (int j = 0; j < diskItem.length; j++) {
																																														String value = "";
																																														if (diskhash1.get(diskItem[j]) != null) {
																																															value = (String) diskhash1.get(diskItem[j]);
																																														}
																																									%>
																																									<td
																																										class="detail-data-body-list"
																																										align=center
																																										height="29;width=10%"><%=value%></td>
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
								<%
									String subtype = "windows";
									if (host.getSysOid().indexOf("1.3.6.1.4.1.2021") >= 0
											|| host.getSysOid().indexOf("1.3.6.1.4.1.8072") >= 0) {
										subtype = "linux";
									}
								%>
								<td class="td-container-main-tool" width=15%>
									<jsp:include page="/include/toolbar.jsp">
										<jsp:param value="<%=host.getIpAddress()%>" name="ipaddress" />
										<jsp:param value="<%=host.getSysOid()%>" name="sys_oid" />
										<jsp:param value="<%=tmp%>" name="tmp" />
										<jsp:param value="host" name="category" />
										<jsp:param value="<%=subtype%>" name="subtype" />
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
