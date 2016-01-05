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
<%@page import="com.afunms.event.model.Syslog"%>
<%@page import="com.afunms.detail.service.cpuInfo.CpuInfoService"%>
<%@page import="com.afunms.detail.service.diskInfo.DiskInfoService"%>
<%@page import="com.afunms.detail.service.OtherInfo.OtherInfoService"%>
<%@page import="com.afunms.detail.service.systemInfo.SystemInfoService"%>
<%@page import="com.afunms.detail.service.interfaceInfo.InterfaceInfoService"%>
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
	String unixflag = (String)request.getAttribute("unixflag");
	String startdate = (String)request.getAttribute("startdate");
	String todate = (String)request.getAttribute("todate");  
	if (unixflag == null)unixflag = "";
	String[] pingItem={"ResponseTime","LostPack"}; 
	String begindate="";
	String enddate="";

  	String tmp = request.getParameter("id"); 
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	double cpuvalue = 0;
	String pingconavg ="0";
	String collecttime = null;
	String sysuptime = null;
	String sysservices = null;
	String sysdescr = null;
		
	Hashtable pagehash = new Hashtable();
	Hashtable paginghash = new Hashtable();
	int pageingused = 0;
	String totalpageing = "";
	String processornum = "";
	String hostname = "";
	String porcessors = "";
	String sysname = "";
	String SerialNumber = "";
	String CSDVersion = "";
	String mac = "";
   	Supper supper = null;
   	String suppername = "";
    int percent1 = 0;
	int percent2 = 0;
	int cpuper = 0;
		
   	HostNodeDao hostdao = new HostNodeDao();
   	List hostlist = hostdao.loadHost();
   	
   	Host host = new Host();
   	host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp)); 
   	NodeUtil nodeUtil = new NodeUtil();
    NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(host); 
   	String ipaddress = host.getIpAddress();
   	if("0".equals(runmodel)){
       	//采集与访问是集成模式
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
	        
	    Nodeconfig nodeconfig = new Nodeconfig();
	      
		try{
	      
			I_HostLastCollectData hostlastmanager=new HostLastCollectDataManager();
	      
			Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
			if(ipAllData != null){
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
				if (ConnectUtilizationhash.get("avgpingcon")!=null){
					pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
					pingconavg = pingconavg.replace("%", "");
				}			
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
	
	    
		   if(ipAllData != null){
			nodeconfig= (Nodeconfig)ipAllData.get("nodeconfig");
			if(nodeconfig != null){
				mac = nodeconfig.getMac();
				processornum = nodeconfig.getNumberOfProcessors();
				CSDVersion = nodeconfig.getCSDVersion();
				hostname = nodeconfig.getHostname();
			}
			
			}
			
        	request.setAttribute("id", tmp);
        	request.setAttribute("ipaddress", host.getIpAddress());
			request.setAttribute("cpuvalue", cpuvalue);
			request.setAttribute("collecttime", collecttime);
			request.setAttribute("sysuptime", sysuptime);
			request.setAttribute("sysservices", sysservices);
			request.setAttribute("sysdescr", sysdescr);
			request.setAttribute("pingconavg", new Double(pingconavg));
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		String[] sysItem={"sysName","sysUpTime","sysContact","sysLocation","sysServices","sysDescr"};
		String[] sysItemch={"设备名","设备启动时间","设备联系","设备位置","设备服务","设备描述"};
		Hashtable hash = (Hashtable)request.getAttribute("hash");
		Hashtable hash1 = (Hashtable)request.getAttribute("hash1");
	
		double avgpingcon = (Double)request.getAttribute("pingconavg");
	
		percent1 = Double.valueOf(avgpingcon).intValue();
		percent2 = 100-percent1;
		
		cpuper = Double.valueOf(cpuvalue).intValue();
		
	  
	  	String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
	  	DefaultPieDataset dpd = new DefaultPieDataset();
	  	dpd.setValue("可用率",avgpingcon);
	  	dpd.setValue("不可用率",100 - avgpingcon);
	  	chart1 = ChartCreator.createPieChart(dpd,"",130,130);  
        chart2 = ChartCreator.createMeterChart(cpuvalue,"",120,120);   
        chart3 = ChartCreator.createMeterChart(40.0,"",120,120); 
        
         
	
	
	
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
	    Nodeconfig nodeconfig = new Nodeconfig();
		try{
			I_HostLastCollectData hostlastmanager=new HostLastCollectDataManager();
			//Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
			//if(ipAllData != null){
			CpuInfoService cpuInfoService = new CpuInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype()); 
			Vector cpuV = cpuInfoService.getCpuInfo();  
			if(cpuV != null && cpuV.size()>0){
				CPUcollectdata cpu = (CPUcollectdata)cpuV.get(0);
				if(cpu != null && cpu.getThevalue() != null){
					cpuvalue = new Double(cpu.getThevalue());
				}
			}
			//得到数据采集时间
			//collecttime = (String)ipAllData.get("collecttime");
			//pagehash = (Hashtable)ipAllData.get("pagehash");
			//paginghash = (Hashtable)ipAllData.get("paginghash");
			OtherInfoService otherInfoService = new OtherInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype());
			collecttime = otherInfoService.getCollecttime();
			paginghash = otherInfoService.getPaginghash();
	   		pagehash = otherInfoService.getPagehash();
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
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(host.getIpAddress(),"Ping","ConnectUtilization",starttime1,totime1);
				if (ConnectUtilizationhash.get("avgpingcon")!=null){
					pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
					pingconavg = pingconavg.replace("%", "");
				}			
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
			NodeconfigDao nodeconfigDao = new NodeconfigDao();
			try{
				nodeconfig = nodeconfigDao.getByNodeID(nodedto.getId()+"");
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
        	request.setAttribute("id", tmp);
        	request.setAttribute("ipaddress", host.getIpAddress());
			request.setAttribute("cpuvalue", cpuvalue);
			request.setAttribute("collecttime", collecttime);
			request.setAttribute("sysuptime", sysuptime);
			request.setAttribute("sysservices", sysservices);
			request.setAttribute("sysdescr", sysdescr);
			request.setAttribute("pingconavg", new Double(pingconavg));
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		String[] sysItem={"sysName","sysUpTime","sysContact","sysLocation","sysServices","sysDescr"};
		String[] sysItemch={"设备名","设备启动时间","设备联系","设备位置","设备服务","设备描述"};
		Hashtable hash = (Hashtable)request.getAttribute("hash");
		Hashtable hash1 = (Hashtable)request.getAttribute("hash1");
	
		double avgpingcon = (Double)request.getAttribute("pingconavg");
	
		percent1 = Double.valueOf(avgpingcon).intValue();
		percent2 = 100-percent1;
		
		cpuper = Double.valueOf(cpuvalue).intValue();
		
	  
	  	String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
	  	DefaultPieDataset dpd = new DefaultPieDataset();
	  	dpd.setValue("可用率",avgpingcon);
	  	dpd.setValue("不可用率",100 - avgpingcon);
	  	chart1 = ChartCreator.createPieChart(dpd,"",130,130);  
        chart2 = ChartCreator.createMeterChart(cpuvalue,"",120,120);   
        chart3 = ChartCreator.createMeterChart(40.0,"",120,120); 
        
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
    double avgpingcon = new Double(pingconavg);
	  String ip = host.getIpAddress();
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
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>


<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8"/>
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 

<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 
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
        mainForm.action = "<%=rootPath%>/cfgfile.do?action=getcfgfile&ipaddress=<%=host.getIpAddress()%>";
        mainForm.submit();
        window.open ("<%=rootPath%>/monitor.do?action="+operate+"&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&ifindex="+index+"&ifname="+ifname, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes") 
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
  function query()
  {  
     mainForm.id.value = <%=tmp%>;
     mainForm.action = "<%=rootPath%>/monitor.do?action=hostsyslog";
     mainForm.submit();
  }
function CreateWindow(urlstr)
{
msgWindow=window.open(urlstr,"protypeWindow","toolbar=no,width=500,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
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
	document.getElementById('aixDetailTitle-6').className='detail-data-title';
	document.getElementById('aixDetailTitle-6').onmouseover="this.className='detail-data-title'";
	document.getElementById('aixDetailTitle-6').onmouseout="this.className='detail-data-title'";
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

<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
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
							<td class="td-container-main-detail">
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
										<td width=85%>
											<table id="detail-data" class="detail-data">
												<tr>
											    	<td class="detail-data-header"><%=aixDetailTitleTable%></td>
											  	</tr>
											  	<tr>
											  		<td>
											    		<table class="detail-data-body">
												 			<tr>
												      			<td>
													      			<table width="100%" border="0" cellpadding="0" cellspacing="0">
  																		<tr>
  																			<td colspan=5 bgcolor="#ECECEC" height="28">
  																				&nbsp;&nbsp;&nbsp;&nbsp;开始日期
																				<input type="text" name="startdate" value="<%=startdate%>" size="10">
																				<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
																				<img id=imageCalendar1 align=absmiddle width=34 height=21 src=<%=rootPath%>/include/calendar/button.gif border=0></a>
																				截止日期
																				<input type="text" name="todate" value="<%=todate%>" size="10"/>
																				<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
																				<img id=imageCalendar2 align=absmiddle width=34 height=21 src=<%=rootPath%>/include/calendar/button.gif border=0></a>
																				日志等级
																				<select name="priorityname">
																					<option value="all">all</option>
																					<option value="panic">panic</option>
																					<option value="alert">alert</option>
																					<option value="critical">critical</option>		
																					<option value="error">error</option>
																					<option value="warning">warning</option>
																					<option value="notice">notice</option>
																					<option value="info">info</option>
																					<option value="debug">debug</option>
																				</select> 
																				<input type="button" name="submitss" value="查询" onclick="query()">&nbsp;&nbsp;
																				<%
																					if(unixflag.equals("1")){
																				%>
																				<input type="button" name="submitss" value="登陆用户信息" onclick=openwin("showusersyslog",'<%=host.getIpAddress()%>') target="_blank">
																				<%
																					}
																				%>
																			</td>
																		</tr>
  																	<table cellSpacing="0"  cellPadding="0" border=0>
																	  	<tr  class="firsttr">
																	    	<td class="detail-data-body-title">&nbsp;</td>
																	        <td width="10%" class="detail-data-body-title"><strong>类型</strong></td>
																	    	<td width="10%" class="detail-data-body-title"><strong>日期</strong></td>
																			<td class="detail-data-body-title"><strong>来源</strong></td>
																	    	<td class="detail-data-body-title"><strong>分类</strong></td>
																	    	<td class="detail-data-body-title"><strong>事件</strong></td>
																	    	<td class="detail-data-body-title"><strong>用户</strong></td>
																	    	<td class="detail-data-body-title"><strong>计算机</strong></td>
																	    	<td width="10%" class="detail-data-body-title"><strong>查看</strong></td>
																	   </tr>
																	<%
																		int index = 0;
																	  	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd HH:mm");
																	  	List list = (List)request.getAttribute("list");
																	  	if (list == null)list = new ArrayList();
																	  	for(int i=0;i<list.size();i++){
																	 		index++;
																	  		Syslog syslog = (Syslog)list.get(i);
																	  		Date cc = syslog.getRecordtime().getTime();
																	  		int priority = syslog.getPriority();
																	  		String processName = syslog.getProcessname();
																	  		String priorityName = syslog.getPriorityName();
																	  		int eventid = syslog.getEventid();  		
																	  		//String hostname = syslog.getHostname();  
																	  		String bak = syslog.getUsername();		  		  		
																	  		String ctime = sdf.format(cc);
																	%>
 																		<tr  bgcolor="#FFFFFF" <%=onmouseoverstyle%>>
    																		<td class="detail-data-body-list">&nbsp;<%=index%></td>
																			<td class="detail-data-body-list"><%=priorityName%></td>
																			<td class="detail-data-body-list"><%=ctime%>&nbsp;</td>
																			<td class="detail-data-body-list"><%=processName%></td>
																			<td class="detail-data-body-list"><%=processName%></td>
																			<td class="detail-data-body-list"><%=eventid%></td>
																			<td class="detail-data-body-list"><%=bak%></td>
																			<td class="detail-data-body-list"> <%=hostname%></td>
																			<td class="detail-data-body-list"><input type ="button" value="详细信息" class="button" onclick="javascript:return CreateWindow('<%=rootPath%>/monitor.do?action=hostsyslogdetail&id=<%=syslog.getId()%>&ipaddress=<%=syslog.getIpaddress()%>')"></td>
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
									<jsp:param value="<%=host.getIpAddress()%>" name="ipaddress"/>
									<jsp:param value="<%=host.getSysOid()%>" name="sys_oid"/>
									<jsp:param value="<%=tmp%>" name="tmp"/>
									<jsp:param value="host" name="category"/>
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