<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Hashtable"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>

<%@page import="com.afunms.common.util.*"%>
<%@page import="com.afunms.polling.impl.*"%>
<%@page import="com.afunms.polling.api.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@ page import="com.afunms.polling.om.*"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>

<%@page import="java.text.*"%>
<%
	String unixflag = (String) request.getAttribute("unixflag");
	
	String errpttype = (String) request.getAttribute("errpttype");
	String errptclass = (String) request.getAttribute("errptclass");
	String _flag = (String)request.getAttribute("flag"); 
	if (unixflag == null)
		unixflag = "";
	String[] pingItem = { "ResponseTime", "LostPack" };
	String begindate = "";
	String enddate = "";

	String tmp = request.getParameter("id");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	double cpuvalue = 0;
	String pingconavg = "0";
	String collecttime = null;
	String sysuptime = null;
	String sysservices = null;
	String sysdescr = null;
	
	int pageingused = 0;
	String totalpageing = "";
	
	Hashtable pagehash = new Hashtable();
	Hashtable paginghash = new Hashtable();

	HostNodeDao hostdao = new HostNodeDao();
	List hostlist = hostdao.loadHost();

	Host host = (Host) PollingEngine.getInstance().getNodeByID(
			Integer.parseInt(tmp));
	String ipaddress = host.getIpAddress();
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

	Nodeconfig nodeconfig = new Nodeconfig();
	String processornum = "";
	String hostname = "";
	String porcessors = "";
	String sysname = "";
	String SerialNumber = "";
	String CSDVersion = "";
	String mac = "";

	try {

		I_HostLastCollectData hostlastmanager = new HostLastCollectDataManager();

		//Vector cpuV = (Vector) ipAllData.get("cpu");
		Vector cpuV  = (Vector)request.getAttribute("cpuV");
		if (cpuV != null && cpuV.size() > 0) {
			CPUcollectdata cpu = (CPUcollectdata) cpuV.get(0);
			if(cpu != null && cpu.getThevalue() != null){
				cpuvalue = new Double(cpu.getThevalue());
			}
		}
		//�õ����ݲɼ�ʱ��
		//collecttime = (String) ipAllData.get("collecttime");
		
		//pagehash = (Hashtable)ipAllData.get("pagehash");
		pagehash = (Hashtable)request.getAttribute("pagehash");
		
		//paginghash = (Hashtable)ipAllData.get("paginghash");
		paginghash = (Hashtable)request.getAttribute("paginghash");
		if(paginghash == null)paginghash = new Hashtable();
		if(paginghash.get("Total_Paging_Space") != null){
			pageingused = Integer.parseInt(((String)paginghash.get("Percent_Used")).replaceAll("%",""));
			totalpageing = (String)paginghash.get("Total_Paging_Space");
		}
	    //�õ�ϵͳ����ʱ��
		cpuvalue = (Double)request.getAttribute("cpuvalue");
		collecttime = (String)request.getAttribute("collecttime");
		sysuptime = (String)request.getAttribute("sysuptime");
		sysservices = (String)request.getAttribute("sysservices");
		sysdescr = (String)request.getAttribute("sysDescr");
		sysname = (String)request.getAttribute("SysName");
		pingconavg = String.valueOf((Double)request.getAttribute("pingconavg"));
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String time1 = sdf.format(new Date());

		String starttime1 = time1 + " 00:00:00";
		String totime1 = time1 + " 23:59:59";

		Hashtable ConnectUtilizationhash = new Hashtable();
		I_HostCollectData hostmanager = new HostCollectDataManager();
		try {
			ConnectUtilizationhash = (Hashtable)request.getAttribute("ConnectUtilizationhash");
			if (ConnectUtilizationhash.get("avgpingcon") != null) {
				pingconavg = (String) ConnectUtilizationhash
						.get("avgpingcon");
				pingconavg = pingconavg.replace("%", "");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		//nodeconfig = (Nodeconfig) ipAllData.get("nodeconfig");
		nodeconfig = (Nodeconfig)request.getAttribute("nodeconfig");
		if (nodeconfig != null) {
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

	} catch (Exception e) {
		e.printStackTrace();
	}

	String[] sysItem = { "sysName", "sysUpTime", "sysContact",
			"sysLocation", "sysServices", "sysDescr" };
	String[] sysItemch = { "�豸��", "�豸����ʱ��", "�豸��ϵ", "�豸λ��", "�豸����",
			"�豸����" };
	Hashtable hash = (Hashtable) request.getAttribute("hash");
	Hashtable hash1 = (Hashtable) request.getAttribute("hash1");

	double avgpingcon = (Double) request.getAttribute("pingconavg");

	int percent1 = Double.valueOf(avgpingcon).intValue();
	int percent2 = 100 - percent1;

	int cpuper = Double.valueOf(cpuvalue).intValue();

	String rootPath = request.getContextPath();

	String chart1 = null, chart2 = null, chart3 = null, responseTime = null;
	DefaultPieDataset dpd = new DefaultPieDataset();
	dpd.setValue("������", avgpingcon);
	dpd.setValue("��������", 100 - avgpingcon);
	chart1 = ChartCreator.createPieChart(dpd, "", 130, 130);
	chart2 = ChartCreator.createMeterChart(cpuvalue, "", 120, 120);
	chart3 = ChartCreator.createMeterChart(40.0, "", 120, 120);

	String startdate = (String) request.getAttribute("startdate");
	String todate = (String) request.getAttribute("todate");

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
	
	String ip = host.getIpAddress();
	String picip = CommonUtil.doip(ip);
    	//���ɵ���ƽ����ͨ��ͼ��
		CreatePiePicture _cpp = new CreatePiePicture();
	        TitleModel _titleModel = new TitleModel();
	        _titleModel.setXpic(150);
	        _titleModel.setYpic(150);//160, 200, 150, 100
	        _titleModel.setX1(75);//�⻷�����λ��
	        _titleModel.setX2(60);//�⻷���ϵ�λ��
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
	        String[] p_labels = {"��ͨ", "δ��ͨ"};
	        int[] _colors = {0x66ff66, 0xff0000}; 
	        String _title1="��һ����";
	        String _title2="�ڶ�����";
	        _cpp.createOneRingChart(_data1,p_labels,_colors,_titleModel);
	        
	   //����CPU�Ǳ���
		CreateMetersPic cmp = new CreateMetersPic();
		MeterModel mm = new MeterModel();
		mm.setBgColor(0xffffff);
		mm.setInnerRoundColor(0xececec);
		mm.setOutRingColor(0x80ff80);
		mm.setTitle("CPU������");
		mm.setPicx(150);//
		mm.setPicy(150);//
		mm.setMeterX(80);//
		mm.setMeterY(80);//
		mm.setPicName(picip+"cpu");//
		mm.setValue(cpuper);//
		mm.setMeterSize(60);//�����Ǳ��̴�С
		mm.setTitleY(79);//���ñ�������߾���
		mm.setTitleTop(122);//���ñ����붥������
		mm.setValueY(78);//����ֵ����߾���
		mm.setValueTop(105);//����ֵ�붥������
		mm.setOutPointerColor(0x80ff80);//����ָ���ⲿ��ɫ
		mm.setInPointerColor(0x8080ff);//����ָ���ڲ���ɫ
		mm.setFontSize(10);//���������С
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
<%
	String menuTable = (String) request.getAttribute("menuTable");
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
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>

		<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>
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
  				location.href="<%=rootPath%>/topology/network/networkview.jsp?id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&orderflag="+para;
			} 
			function openwin3(operate,index,ifname) 
			{	
			        mainForm.action = "<%=rootPath%>/cfgfile.do?action=getcfgfile&ipaddress=<%=host.getIpAddress()%>";
			        mainForm.submit();
			        window.open ("<%=rootPath%>/monitor.do?action="+operate+"&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&ifindex="+index+"&ifname="+ifname, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes") 
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
			  function query()
			  {  
			     mainForm.id.value = <%=tmp%>;
			     mainForm.action = "<%=rootPath%>/monitor.do?action=hosterrpt&flag=<%=_flag%>";
			     mainForm.submit();
			  }
			function CreateWindow(urlstr)
			{
			msgWindow=window.open(urlstr,"protypeWindow","toolbar=no,width=800,height=680,directories=no,status=no,scrollbars=yes,menubar=no,top=5")
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
			
			//���Ӳ˵�	
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
				initData();
			}
			
			function setClass(){
				document.getElementById('aixDetailTitle-7').className='detail-data-title';
				document.getElementById('aixDetailTitle-7').onmouseover="this.className='detail-data-title'";
				document.getElementById('aixDetailTitle-7').onmouseout="this.className='detail-data-title'";
			}
			
			function refer(action){
					document.getElementById("id").value="<%=tmp%>";
					var mainForm = document.getElementById("mainForm");
					mainForm.action = '<%=rootPath%>' + action;
					mainForm.submit();
			}
			
			function showHostErrptlogDetail(errptlogId){
				CreateWindow('<%=rootPath%>/monitor.do?action=hosterrptDetail&errptlogId=' + errptlogId);
			}
			
			function initData(){
					document.getElementById("errpttype").value="<%=errpttype%>";
					document.getElementById("errptclass").value="<%=errptclass%>";
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

		<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0
			noResize scrolling=no src="<%=rootPath%>/include/calendar.htm"
			style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>

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
																	<td>
																		<table width="100%" border="0" cellpadding="0" cellspacing="0">
																			<tr>
																				<td colspan=5 bgcolor="#ECECEC" height="28">
																					&nbsp;��ʼ����
																					<input type="text" name="startdate"
																						value="<%=startdate%>" size="10">
																					<a onclick="event.cancelBubble=true;"
																						href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
																						<img id=imageCalendar1 align=absmiddle width=34 height=21 src=<%=rootPath%>/include/calendar/button.gif border=0>
																					</a> ��ֹ����
																					<input type="text" name="todate" value="<%=todate%>"
																						size="10" />
																					<a onclick="event.cancelBubble=true;"
																						href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
																						<img id=imageCalendar2 align=absmiddle width=34 height=21 src=<%=rootPath%>/include/calendar/button.gif border=0>
																					</a> ���س̶�
																					<select name="errpttype" id="errpttype" style="width: 200px">
																						<option value="all">
																							ȫ��
																						</option>
																						<option value="pend" title="�豸����������ܶ�ʧ">
																							�豸����������ܶ�ʧ
																						</option>
																						<option value="perf" title="���������½�">
																							���������½�
																						</option>
																						<option value="perm" title="Ӳ���豸������ģ����">
																							Ӳ���豸������ģ����
																						</option>
																						<option value="temp" title="��ʱ�Դ��󣬾������Ժ��Ѿ��ָ�����">
																							��ʱ�Դ��󣬾������Ժ��Ѿ��ָ�����
																						</option>
																						<option value="info" title="һ����Ϣ�����Ǵ���">
																							һ����Ϣ�����Ǵ���
																						</option>
																						<option value="unkn" title="����ȷ�������������">
																							����ȷ�������������
																						</option>
																						<option value="none" title="δ֪">
																							δ֪
																						</option>
																					</select>
																					����Դ
																					<select name="errptclass" id="errptclass">
																						<option value="all" title="ȫ��">
																							ȫ��
																						</option>
																						<option value="h" title="Ӳ������ʹ���">
																							Ӳ������ʹ���
																						</option>
																						<option value="s" title="��������">
																							��������
																						</option>
																						<option value="o" title="��Ϊ����">
																							��Ϊ����
																						</option>
																						<option value="u" title="����ȷ��">
																							����ȷ��
																						</option>
																					</select>
																					<input type="button" name="submitss" value="��ѯ"
																						onclick="query()">
																					<%
																						if (unixflag.equals("1")) {
																					%>
																					<input type="button" name="submitss" value="��½�û���Ϣ"
																						onclick=openwin(
																						"showusersyslog",'<%=host.getIpAddress()%>') target="_blank">
																					<%
																						}
																					%>
																				</td>
																			</tr>
																		</table>
																	</td>
																</tr>
																<tr>
																	<td>
																			<table cellSpacing="0" cellPadding="0" border=0>
			
																				<tr class="firsttr">
																					<td class="detail-data-body-title">
																						&nbsp;
																					</td>
																					<td class="detail-data-body-title">
																						<strong>��ǩ</strong>
																					</td>
																					<td class="detail-data-body-title">
																						<strong>����</strong>
																					</td>
																					<td class="detail-data-body-title">
																						<strong>��������</strong>
																					</td>
																					<td class="detail-data-body-title">
																						<strong>��������</strong>
																					</td>
																					<td class="detail-data-body-title">
																						<strong>��Դ����</strong>
																					</td>
																					<td class="detail-data-body-title">
																						<strong>��Դ����</strong>
																					</td>
																					<td class="detail-data-body-title">
																						<strong>��Դ����</strong>
																					</td>
																					<td class="detail-data-body-title">
																						<strong>λ��</strong>
																					</td>
																					<td class="detail-data-body-title">
																						<strong>�鿴</strong>
																					</td>
																				</tr>
																				<%
																					Hashtable errpttypehashtable = new Hashtable();
																					errpttypehashtable.put("pend" , "�豸����������ܶ�ʧ");
																					errpttypehashtable.put("perf" , "���������½�");
																					errpttypehashtable.put("perm" , "Ӳ���豸������ģ����");
																					errpttypehashtable.put("temp" , "��ʱ�Դ��󣬾������Ժ��Ѿ��ָ�����");
																					errpttypehashtable.put("info" , "һ����Ϣ�����Ǵ���");
																					errpttypehashtable.put("unkn" , "����ȷ�������������");
																					errpttypehashtable.put("none" , "δ֪");
																					Hashtable errptclasshashtable = new Hashtable();
																					errptclasshashtable.put("h" , "Ӳ������ʹ���");
																					errptclasshashtable.put("s" , "��������");
																					errptclasshashtable.put("0" , "��Ϊ����");
																					errptclasshashtable.put("u" , "����ȷ��");
																					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
																					List list = (List) request.getAttribute("list");
																					if (list == null)
																						list = new ArrayList();
																					for (int i = 0; i < list.size(); i++) {
																						Errptlog errptlog = (Errptlog)list.get(i);
																						String errpttype_per = (String)errpttypehashtable.get(errptlog.getErrpttype().trim().toLowerCase());
																						String errptclass_per = (String)errptclasshashtable.get(errptlog.getErrptclass().trim().toLowerCase());
																				%>
			
																				<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>>
			
																					<td class="detail-data-body-list">
																						&nbsp;<%=i + 1%></td>
																					<td class="detail-data-body-list">
																						<%=errptlog.getLabels()%></td>
																					<td class="detail-data-body-list">
																						<%=simpleDateFormat.format(errptlog.getCollettime().getTime())%>&nbsp;
																					</td>
																					<td class="detail-data-body-list">
																						<%=errpttype_per%></td>
																					<td class="detail-data-body-list">
																						<%=errptclass_per%></td>
																					<td class="detail-data-body-list">
																						<%=errptlog.getResourcename()%></td>
																					<td class="detail-data-body-list">
																						<%=errptlog.getResourceclass()%></td>
																					<td class="detail-data-body-list">
																						<%=errptlog.getRescourcetype()%></td>
																					<td class="detail-data-body-list">
																						<%=errptlog.getLocations()%></td>
																					<td class="detail-data-body-list">
																						<input type="button" value="��ϸ��Ϣ" class="button"
																							onclick="showHostErrptlogDetail('<%=errptlog.getId()%>')">
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