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

<%@page import="com.afunms.config.dao.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.application.model.*"%>

<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%@page import="com.afunms.common.util.CreatePiePicture"%>
<%
  String rootPath = request.getContextPath();;
  DBVo vo  = (DBVo)request.getAttribute("db");	
  String id = (String)request.getAttribute("id");
  String myip = vo.getIpAddress();
  String myport = vo.getPort();
  String myUser = vo.getUser();
  String myPassword = EncryptUtil.decode(vo.getPassword());
  String mysid = "";
  String dbPage = "mysqlvariables";
  String dbType = "mysql";
        
	
Hashtable max = (Hashtable) request.getAttribute("max");
//Hashtable imgurl = (Hashtable)request.getAttribute("imgurl");
String chart1 = (String)request.getAttribute("chart1");
	double avgpingcon = (Double)request.getAttribute("avgpingcon");	
	int percent1 = Double.valueOf(avgpingcon).intValue();
	int percent2 = 100-percent1;
String dbtye = (String)request.getAttribute("dbtye");
String managed = (String)request.getAttribute("managed");
String runstr = (String)request.getAttribute("runstr");
Vector tableinfo_v = (Vector)request.getAttribute("tableinfo_v");
String versionvalue=(String)request.getAttribute("versionvalue");
String vercommaval=(String)request.getAttribute("vercommaval");
String vercomosval=(String)request.getAttribute("vercomosval");
String verwaittimeval=(String)request.getAttribute("verwaittimeval");
     
String basePath=(String)request.getAttribute("basePath");
String dataPath=(String)request.getAttribute("dataPath");
String logerrorPath=(String)request.getAttribute("logerrorPath");
String version=(String)request.getAttribute("version");
String hostOS=(String)request.getAttribute("hostOS");
   
   
   String picip = CommonUtil.doip(myip);
     			  	   
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
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


		<script language="javascript">	


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
	document.getElementById('mysqlDetailTitle-5').className='detail-data-title';
	document.getElementById('mysqlDetailTitle-5').onmouseover="this.className='detail-data-title'";
	document.getElementById('mysqlDetailTitle-5').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		document.getElementById("id").value="<%=vo.getId()%>";
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}
</script>


 <script>
function gzmajax(){
$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=ajaxUpdate_availability&tmp=<%=id%>&nowtime="+(new Date()),
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
								<td class="td-container-main-application-detail">
									<table id="container-main-application-detail"
										class="container-main-application-detail">
										<tr>
											<td> 
												<jsp:include page="/topology/includejsp/db_mysql.jsp">
													<jsp:param name="dbName" value="<%=vo.getDbName() %>"/>
													<jsp:param name="alias" value="<%=vo.getAlias() %>"/>
													<jsp:param name="ipAddress" value="<%=vo.getIpAddress() %>"/>
													<jsp:param name="port" value="<%=vo.getPort() %>"/>
													<jsp:param name="dbtye" value="<%=dbtye %>"/>
													<jsp:param name="managed" value="<%=managed %>"/>
													<jsp:param name="runstr" value="<%=runstr %>"/>
													<jsp:param name="version" value="<%=version %>"/>
													<jsp:param name="hostOS" value="<%=hostOS %>"/>
													<jsp:param name="basePath" value="<%=basePath %>"/>
													<jsp:param name="dataPath" value="<%=dataPath %>"/>
													<jsp:param name="logerrorPath" value="<%=logerrorPath %>"/>
													<jsp:param name="picip" value="<%=picip %>"/>
													<jsp:param name="pingavg" value="<%=avgpingcon %>"/>   
												</jsp:include>
											</td>
										</tr>
										<tr>
											<td>
												<table id="application-detail-data"
													class="application-detail-data">
													<tr>
														<td class="detail-data-header">
															<%=mysqlDetailTitleTable%>
														</td>
													</tr>
													<tr>
														<td>
															<table class="application-detail-data-body">
																<tr>
																	<td>
																		<table cellspacing="1" cellpadding="0">
																			<tr>
																				<td height="28" align="center" bgcolor="#ECECEC"
																					colspan=6>
																					<b> ���ݿ�״̬��Ϣ</b>
																				</td>
																			</tr>

																			<%
			if(tableinfo_v != null)
			{
				for(int i=0;i<tableinfo_v.size();i++){
					Hashtable ht = (Hashtable)tableinfo_v.get(i);
					String size = ht.get("variable_name").toString();
					String free = ht.get("value").toString();
					if(size.equalsIgnoreCase("auto_increment_increment"))
					{
					 size="�������е�ֵ������ֵ";
					}
					if(size.equalsIgnoreCase("auto_increment_offset"))
					{
					 size="ȷ��AUTO_INCREMENT��ֵ�����";
					}
					if(size.equalsIgnoreCase("automatic_sp_privileges"))
					{
					 size="automatic_sp_privileges";
					}
					if(size.equalsIgnoreCase("back_log"))
					{
					 size="�������������";
					}
					if(size.equalsIgnoreCase("basedir"))
					{
					 size="MySQL��װ��׼Ŀ¼";
					}
					if(size.equalsIgnoreCase("binlog_cache_size"))
					{
					 size="���ɶ�������־SQL���Ļ����С";
					}
					if(size.equalsIgnoreCase("bulk_insert_buffer_size"))
					{
					 size="ÿ�̵߳��ֽ������ƻ������Ĵ�С";
					}
					if(size.equalsIgnoreCase("character_set_client"))
					{
					 size="���Կͻ��˵������ַ���";
					}
					if(size.equalsIgnoreCase("character_set_connection"))
					{
					 size="û���ַ�����������ַ���ת��";
					}
					if(size.equalsIgnoreCase("character_set_database"))
					{
					 size="Ĭ�����ݿ�ʹ�õ��ַ���";
					}
					if(size.equalsIgnoreCase("character_set_filesystem"))
					{
					 size="character_set_filesystem";
					}
					if(size.equalsIgnoreCase("character_set_results"))
					{
					 size="������ͻ��˷��ز�ѯ������ַ���";
					}
					if(size.equalsIgnoreCase("character_set_server"))
					{
					 size="��������Ĭ���ַ���";
					}
					if(size.equalsIgnoreCase("character_set_system"))
					{
					 size="��������������ʶ������ַ���";
					}
					if(size.equalsIgnoreCase("character_sets_dir"))
					{
					 size="�ַ�����װĿ¼";
					}
					if(size.equalsIgnoreCase("collation_connection"))
					{
					 size="�����ַ�����У�Թ���";
					}
					if(size.equalsIgnoreCase("collation_database"))
					{
					 size="Ĭ�����ݿ�ʹ�õ�У�Թ���";
					}
					if(size.equalsIgnoreCase("collation_server"))
					{
					 size="��������Ĭ��У�Թ���";
					}
					if(size.equalsIgnoreCase("completion_type"))
					{
					 size="�����������";
					}
					if(size.equalsIgnoreCase("concurrent_insert"))
					{
					 size="�洢ֵ���";
					}
					if(size.equalsIgnoreCase("connect_timeout"))
					{
					 size="��������Bad handshake��Ӧǰ�ȴ����Ӱ�������";
					}
					if(size.equalsIgnoreCase("datadir"))
					{
					 size="MySQL����Ŀ¼";
					}
					if(size.equalsIgnoreCase("date_format"))
					{
					 size="date_format(Ϊ��ʹ��)";
					}
					if(size.equalsIgnoreCase("datetime_format"))
					{
					 size="datetime_format(Ϊ��ʹ��)";
					}
					if(size.equalsIgnoreCase("default_week_format"))
					{
					 size="WEEK() ����ʹ�õ�Ĭ��ģʽ";
					}
					if(size.equalsIgnoreCase("delay_key_write"))
					{
					 size="ʹ�õ�DELAY_KEY_WRITE��ѡ��Ĵ���";
					}
					if(size.equalsIgnoreCase("delayed_insert_limit"))
					{
					 size="INSERT DELAYED�������̼߳���Ƿ��й����SELECT���";
					}
					if(size.equalsIgnoreCase("delayed_insert_timeout"))
					{
					 size="INSERT DELAYED�������߳���ֹǰӦ�ȴ�INSERT����ʱ��";
					}
					if(size.equalsIgnoreCase("delayed_queue_size"))
					{
					 size="����INSERT DELAYED���ʱ�������е���������";
					}
					if(size.equalsIgnoreCase("div_precision_increment"))
					{
					 size="��/������ִ�г������Ľ�������ӵľ�ȷ�ȵ�λ��";
					}
					if(size.equalsIgnoreCase("engine_condition_pushdown"))
					{
					 size="������NDB�ļ��";
					}
					if(size.equalsIgnoreCase("expire_logs_days"))
					{
					 size="��������־�Զ�ɾ��������";
					}
					if(size.equalsIgnoreCase("flush"))
					{
					 size="flushѡ������mysqldֵ";
					}
					if(size.equalsIgnoreCase("flush_time"))
					{
					 size="�鿴�ͷ���Դ���";
					}
					if(size.equalsIgnoreCase("ft_boolean_syntax"))
					{
					 size="ʹ��IN BOOLEAN MODEִ�еĲ���ȫ������֧�ֵĲ�����ϵ��";
					}
					if(size.equalsIgnoreCase("ft_max_word_len"))
					{
					 size="FULLTEXT���������������ֵ���󳤶�";
					}
					if(size.equalsIgnoreCase("ft_min_word_len"))
					{
					 size="FULLTEXT���������������ֵ���С����";
					}
					if(size.equalsIgnoreCase("ft_query_expansion_limit"))
					{
					 size="ʹ��WITH QUERY EXPANSION����ȫ�����������ƥ����";
					}
					if(size.equalsIgnoreCase("ft_stopword_file"))
					{
					 size="���ڶ�ȡȫ��������ֹͣ���嵥���ļ�";
					}
					if(size.equalsIgnoreCase("group_concat_max_len"))
					{
					 size="�����GROUP_CONCAT()�����������󳤶�";
					}
					if(size.equalsIgnoreCase("have_archive"))
					{
					 size="mysqld֧��ARCHIVE��֧�ֱ����";
					}
					if(size.equalsIgnoreCase("have_bdb"))
					{
					 size="mysqld֧��BDB�����";
					}
					if(size.equalsIgnoreCase("have_blackhole_engine"))
					{
					 size="mysqld֧��BLACKHOLE�����";
					}
					if(size.equalsIgnoreCase("have_compress"))
					{
					 size="�Ƿ�zlibѹ�����ʺϸ÷�����";
					}
					if(size.equalsIgnoreCase("have_crypt"))
					{
					 size="�Ƿ�crypt()ϵͳ�����ʺϸ÷�����";
					}
					if(size.equalsIgnoreCase("have_csv"))
					{
					 size="mysqld֧��ARCHIVE�����";
					}
					if(size.equalsIgnoreCase("have_example_engine"))
					{
					 size="mysqld֧��EXAMPLE�����";
					}
					if(size.equalsIgnoreCase("have_federated_engine"))
					{
					 size="mysqld֧��FEDERATED�����";
					}
					if(size.equalsIgnoreCase("have_geometry"))
					{
					 size="�Ƿ������֧�ֿռ���������";
					}
					if(size.equalsIgnoreCase("have_innodb"))
					{
					 size="mysqld֧��InnoDB�����";
					}
					if(size.equalsIgnoreCase("have_isam"))
					{
					 size="������";
					}
					if(size.equalsIgnoreCase("have_ndbcluster"))
					{
					 size="mysqld֧��NDB CLUSTER�����";
					}
					if(size.equalsIgnoreCase("have_openssl"))
					{
					 size="mysqld֧�ֿͻ���/������Э���SSL(����)���";
					}
					if(size.equalsIgnoreCase("have_query_cache"))
					{
					 size="mysqld֧�ֲ�ѯ�������";
					}
					if(size.equalsIgnoreCase("have_raid"))
					{
					 size="mysqld֧��RAIDѡ�����";
					}
					if(size.equalsIgnoreCase("have_rtree_keys"))
					{
					 size="RTREE�����Ƿ����";
					}
					if(size.equalsIgnoreCase("have_symlink"))
					{
					 size="�Ƿ����÷�������֧��";
					}
					if(size.equalsIgnoreCase("init_connect"))
					{
					 size="�ַ�������";
					}
					if(size.equalsIgnoreCase("init_file"))
					{
					 size="����������ʱ��--init-fileѡ��ָ�����ļ���";
					}
					if(size.equalsIgnoreCase("init_slave"))
					{
					 size="SQL�߳�����ʱ�ӷ�����Ӧִ�и��ַ���";
					}
					if(size.equalsIgnoreCase("innodb_additional_mem_pool_size"))
					{
					 size="InnoDB�����洢�����ڴ��С���";
					}
					if(size.equalsIgnoreCase("innodb_autoextend_increment"))
					{
					 size="��ռ䱻����֮ʱ��չ��ռ�ĳߴ�";
					}
					if(size.equalsIgnoreCase("innodb_buffer_pool_awe_mem_mb"))
					{
					 size="����ر�����32λWindows��AWE�ڴ��ﻺ��ش�С";
					}
					if(size.equalsIgnoreCase("innodb_buffer_pool_size"))
					{
					 size="InnoDB���������������ݺ��������ڴ滺�����Ĵ�С";
					}
					if(size.equalsIgnoreCase("innodb_checksums"))
					{
					 size="InnoDB�����жԴ��̵�ҳ���ȡ�ϵ�״̬";
					}
					if(size.equalsIgnoreCase("innodb_commit_concurrency"))
					{
					 size="innodb_commit_concurrency";
					}
					if(size.equalsIgnoreCase("innodb_concurrency_tickets"))
					{
					 size="innodb_concurrency_tickets";
					}
					if(size.equalsIgnoreCase("innodb_data_file_path"))
					{
					 size="���������ļ������ǳߴ��·��";
					}
					if(size.equalsIgnoreCase("innodb_data_home_dir"))
					{
					 size="Ŀ¼·��������InnoDB�����ļ��Ĺ�ͬ����";
					}
					if(size.equalsIgnoreCase("innodb_doublewrite"))
					{
					 size="InnoDB�洢�����������";
					}
					if(size.equalsIgnoreCase("innodb_fast_shutdown"))
					{
					 size="InnoDB�ڹر������ֵѡ��";
					}
					if(size.equalsIgnoreCase("innodb_file_io_threads"))
					{
					 size="InnoDB���ļ�I/O�̵߳���";
					}
					if(size.equalsIgnoreCase("innodb_file_per_table"))
					{
					 size="ȷ���Ƿ�InnoDB���Լ���.ibd�ļ�Ϊ�洢���ݺ���������ÿһ���±�";
					}
					if(size.equalsIgnoreCase("innodb_flush_log_at_trx_commit"))
					{
					 size="InnoDB����־�������";
					}
					if(size.equalsIgnoreCase("innodb_flush_method"))
					{
					 size="InnoDBʹ��fsync()��ˢ�����ݺ���־�ļ�";
					}
					if(size.equalsIgnoreCase("innodb_force_recovery"))
					{
					 size="�𻵵����ݿ�ת����ķ���";
					}
					if(size.equalsIgnoreCase("innodb_lock_wait_timeout"))
					{
					 size="InnoDB�����ڱ��ع�֮ǰ���Եȴ�һ�������ĳ�ʱ����";
					}
					if(size.equalsIgnoreCase("innodb_locks_unsafe_for_binlog"))
					{
					 size="InnoDB����������ɨ���йر���һ������";
					}
					if(size.equalsIgnoreCase("innodb_log_arch_dir"))
					{
					 size="ʹ����־���� ������д�����־�ļ����ڵ�Ŀ¼�Ĺ鵵ֵ";
					}
					if(size.equalsIgnoreCase("innodb_log_archive"))
					{
					 size="��־�������";
					}
					if(size.equalsIgnoreCase("innodb_log_buffer_size"))
					{
					 size="InnoDB�����������ϵ���־�ļ�д�����Ļ������Ĵ�С";
					}
					if(size.equalsIgnoreCase("innodb_log_file_size"))
					{
					 size="��־����ÿ����־�ļ��Ĵ�С";
					}
					if(size.equalsIgnoreCase("innodb_log_files_in_group"))
					{
					 size="��־������־�ļ�����Ŀ";
					}
					if(size.equalsIgnoreCase("innodb_log_group_home_dir"))
					{
					 size="InnoDB��־�ļ���Ŀ¼·��";
					}
					if(size.equalsIgnoreCase("innodb_max_dirty_pages_pct"))
					{
					 size="InnoDB�д�����ҳ�����";
					}
					if(size.equalsIgnoreCase("innodb_max_purge_lag"))
					{
					 size="�����������ͺ�֮ʱ������ӳ�INSERT,UPDATE��DELETE����";
					}
					if(size.equalsIgnoreCase("innodb_mirrored_log_groups"))
					{
					 size="Ϊ���ݿⱣ�ֵ���־����ͬ������������";
					}
					if(size.equalsIgnoreCase("innodb_open_files"))
					{
					 size="��InnoDBһ�ο��Ա��ִ򿪵�.ibd�ļ��������";
					}
					if(size.equalsIgnoreCase("innodb_support_xa"))
					{
					 size="InnoDB֧����XA�����е�˫���ύ���";
					}
					if(size.equalsIgnoreCase("innodb_sync_spin_loops"))
					{
					 size="innodb_sync_spin_loops";
					}
					if(size.equalsIgnoreCase("innodb_table_locks"))
					{
					 size="InnoDB�Ա���������";
					}
					if(size.equalsIgnoreCase("innodb_thread_concurrency"))
					{
					 size="InnoDB������InnoDB�ڱ��ֲ���ϵͳ�̵߳��������ڻ��������������������Ʒ�Χ";
					}
					if(size.equalsIgnoreCase("innodb_thread_sleep_delay"))
					{
					 size="��InnoDBΪ���ڵ�SHOW INNODB STATUS�������һ���ļ�<datadir>/innodb_status";
					}
					if(size.equalsIgnoreCase("interactive_timeout"))
					{
					 size="�������رս���ʽ����ǰ�ȴ��������";
					}
					if(size.equalsIgnoreCase("join_buffer_size"))
					{
					 size="������ȫ���ӵĻ������Ĵ�С";
					}
					if(size.equalsIgnoreCase("key_buffer_size"))
					{
					 size="�����黺�����Ĵ�С";
					}
					if(size.equalsIgnoreCase("key_cache_age_threshold"))
					{
					 size="���ƽ��������Ӽ�ֵ����������(sub-chain)������������(sub-chain)��ֵ";
					}
					if(size.equalsIgnoreCase("key_cache_block_size"))
					{
					 size="��ֵ�����ڿ���ֽڴ�С";
					}
					if(size.equalsIgnoreCase("key_cache_division_limit"))
					{
					 size="��ֵ���滺���������������������Ļ��ֵ�";
					}
					if(size.equalsIgnoreCase("language"))
					{
					 size="������Ϣ��������";
					}
					if(size.equalsIgnoreCase("large_files_support"))
					{
					 size="mysqld����ʱ�Ƿ�ʹ���˴��ļ�֧��ѡ��";
					}
					if(size.equalsIgnoreCase("large_page_size"))
					{
					 size="large_page_size";
					}
					if(size.equalsIgnoreCase("large_pages"))
					{
					 size="�Ƿ������˴�ҳ��֧��";
					}
					if(size.equalsIgnoreCase("license"))
					{
					 size="���������������";
					}
					if(size.equalsIgnoreCase("local_infile"))
					{
					 size="�Ƿ�LOCAL֧��LOAD DATA INFILE���";
					}
					if(size.equalsIgnoreCase("log"))
					{
					 size="�Ƿ����ý����в�ѯ��¼�������ѯ��־��";
					}
					if(size.equalsIgnoreCase("log_bin"))
					{
					 size="�Ƿ����ö�������־";
					}
					if(size.equalsIgnoreCase("log_bin_trust_function_creators"))
					{
					 size="�Ƿ�������α���ĳ�������߲��ᴴ�����������־д�벻��ȫ�¼��ĳ���";
					}
					if(size.equalsIgnoreCase("log_error"))
					{
					 size="������־��λ��";
					}
					if(size.equalsIgnoreCase("log_slave_updates"))
					{
					 size="�Ƿ�ӷ����������������յ��ĸ���Ӧ����ӷ������Լ��Ķ�������־";
					}
					if(size.equalsIgnoreCase("log_slow_queries"))
					{
					 size="�Ƿ��¼����ѯ";
					}
					if(size.equalsIgnoreCase("log_warnings"))
					{
					 size="�Ƿ��������������Ϣ";
					}
					if(size.equalsIgnoreCase("long_query_time"))
					{
					 size="��ѯʱ�䳬����ֵ��������Slow_queries״̬����";
					}
					if(size.equalsIgnoreCase("low_priority_updates"))
					{
					 size="��ʾsql���ȴ���佫�ȴ�ֱ����Ӱ��ı�û�й����SELECT��LOCK TABLE READ";
					}
					if(size.equalsIgnoreCase("lower_case_file_system"))
					{
					 size="˵���Ƿ�����Ŀ¼���ڵ��ļ�ϵͳ���ļ����Ĵ�Сд����";
					}
					if(size.equalsIgnoreCase("lower_case_table_names"))
					{
					 size="Ϊ1��ʾ������Сд���浽Ӳ���ϣ����ұ����Ƚ�ʱ���Դ�Сд����";
					}
					if(size.equalsIgnoreCase("max_allowed_packet"))
					{
					 size="�����κ����ɵ�/�м��ַ���������С";
					}
					if(size.equalsIgnoreCase("max_binlog_cache_size"))
					{
					 size="�����������Ҫ������ڴ�ʱ���ֵ����";
					}
					if(size.equalsIgnoreCase("max_binlog_size"))
					{
					 size="�����������Ҫ������ڴ�ʱ���ֵ����";
					}
					if(size.equalsIgnoreCase("max_connect_errors"))
					{
					 size="�ϵ������������ӵ����������";
					}
					if(size.equalsIgnoreCase("max_connections"))
					{
					 size="����Ĳ��пͻ���������Ŀ";
					}
					if(size.equalsIgnoreCase("max_delayed_threads"))
					{
					 size="�����߳�������INSERT DELAYED����������";
					}
					if(size.equalsIgnoreCase("max_error_count"))
					{
					 size="����SHOW ERRORS��SHOW WARNINGS��ʾ�Ĵ��󡢾����ע��������Ŀ";
					}
					if(size.equalsIgnoreCase("max_heap_table_size"))
					{
					 size="����MEMORY (HEAP)����������������ռ��С";
					}
					if(size.equalsIgnoreCase("max_insert_delayed_threads"))
					{
					 size="�����߳�������INSERT DELAYED����������(ͬmax_delayed_threads)";
					}
					if(size.equalsIgnoreCase("max_join_size"))
					{
					 size="�����������Ҫ������max_join_size�е����";
					}
					if(size.equalsIgnoreCase("max_length_for_sort_data"))
					{
					 size="ȷ��ʹ�õ�filesort�㷨������ֵ��С����ֵ";
					}
					if(size.equalsIgnoreCase("max_prepared_stmt_count"))
					{
					 size="max_prepared_stmt_count";
					}
					if(size.equalsIgnoreCase("max_relay_log_size"))
					{
					 size="������ƴӷ�����д���м���־ʱ��������ֵ��������м���";
					}
					if(size.equalsIgnoreCase("max_seeks_for_key"))
					{
					 size="���Ƹ��ݼ�ֵѰ����ʱ�����������";
					}
					if(size.equalsIgnoreCase("max_sort_length"))
					{
					 size="����BLOB��TEXTֵʱʹ�õ��ֽ���";
					}
					if(size.equalsIgnoreCase("max_sp_recursion_depth"))
					{
					 size="max_sp_recursion_depth";
					}
					if(size.equalsIgnoreCase("max_tmp_tables"))
					{
					 size="�ͻ��˿���ͬʱ�򿪵���ʱ��������";
					}
					if(size.equalsIgnoreCase("max_user_connections"))
					{
					 size="������MySQL�˻���������ͬʱ������";
					}
					if(size.equalsIgnoreCase("max_write_lock_count"))
					{
					 size="����д�������ƺ������ֶ�����";
					}
					if(size.equalsIgnoreCase("multi_range_count"))
					{
					 size="multi_range_count";
					}
					if(size.equalsIgnoreCase("myisam_data_pointer_size"))
					{
					 size="Ĭ��ָ���С��ֵ";
					}
					if(size.equalsIgnoreCase("myisam_max_sort_file_size"))
					{
					 size="�ؽ�MyISAM����ʱ������MySQLʹ�õ���ʱ�ļ������ռ��С";
					}
					if(size.equalsIgnoreCase("myisam_recover_options"))
					{
					 size="myisam-recoverѡ���ֵ";
					}
					if(size.equalsIgnoreCase("myisam_repair_threads"))
					{
					 size="�����ֵ����1����Repair by sorting�����в��д���MyISAM������";
					}
					if(size.equalsIgnoreCase("myisam_sort_buffer_size"))
					{
					 size="��REPAIR TABLE����CREATE INDEX����������ALTER TABLE����������MyISAM��������Ļ�����";
					}
					if(size.equalsIgnoreCase("myisam_stats_method"))
					{
					 size="MyISAM���Ѽ���������ֵ�ַ���ͳ����Ϣʱ��������δ���NULLֵ";
					}
					if(size.equalsIgnoreCase("named_pipe"))
					{
					 size="���������Ƿ�֧�������ܵ�����";
					}
					if(size.equalsIgnoreCase("net_buffer_length"))
					{
					 size="�ڲ�ѯ֮�佫ͨ�Ż���������Ϊ��ֵ";
					}
					if(size.equalsIgnoreCase("net_read_timeout"))
					{
					 size="�ж϶�ǰ�ȴ����ӵ��������ݵ�����";
					}
					if(size.equalsIgnoreCase("net_retry_count"))
					{
					 size="��ʾĳ��ͨ�Ŷ˿ڵĶ������ж��ˣ��ڷ���ǰ���Զ��";
					}
					if(size.equalsIgnoreCase("net_write_timeout"))
					{
					 size="�ж�д֮ǰ�ȴ���д�����ӵ�����";
					}
					if(size.equalsIgnoreCase("new"))
					{
					 size="��ʾ��MySQL 4.0��ʹ�øñ�������4.1�е�һЩ��Ϊ����������������";
					}
					if(size.equalsIgnoreCase("old_passwords"))
					{
					 size="�Ƿ������ӦΪMySQL�û��˻�ʹ��pre-4.1-style������";
					}
					if(size.equalsIgnoreCase("open_files_limit"))
					{
					 size="����ϵͳ����mysqld�򿪵��ļ�������";
					}
					if(size.equalsIgnoreCase("optimizer_prune_level"))
					{
					 size="�ڲ�ѯ�Ż����Ż��������ռ�ü���ϣ���ֲ��ƻ���ʹ�õĿ��Ʒ��� 0��ʾ���÷���";
					}
					if(size.equalsIgnoreCase("optimizer_search_depth"))
					{
					 size="��ѯ�Ż������е�������������";
					}
					if(size.equalsIgnoreCase("pid_file"))
					{
					 size="����ID (PID)�ļ���·����";
					}
					if(size.equalsIgnoreCase("prepared_stmt_count"))
					{
					 size="prepared_stmt_count";
					}
					if(size.equalsIgnoreCase("port"))
					{
					 size="������֡��TCP/IP�������ö˿�";
					}
					if(size.equalsIgnoreCase("preload_buffer_size"))
					{
					 size="��������ʱ����Ļ�������С";
					}
					if(size.equalsIgnoreCase("protocol_version"))
					{
					 size="MySQL������ʹ�õĿͻ���/������Э��İ汾";
					}
					if(size.equalsIgnoreCase("query_alloc_block_size"))
					{
					 size="Ϊ��ѯ������ִ�й����д����Ķ��������ڴ���С";
					}
					if(size.equalsIgnoreCase("query_cache_limit"))
					{
					 size="��Ҫ������ڸ�ֵ�Ľ��";
					}
					if(size.equalsIgnoreCase("query_cache_min_res_unit"))
					{
					 size="��ѯ����������С��Ĵ�С(�ֽ�)";
					}
					if(size.equalsIgnoreCase("query_cache_size"))
					{
					 size="Ϊ�����ѯ���������ڴ������";
					}
					if(size.equalsIgnoreCase("query_cache_type"))
					{
					 size="���ò�ѯ��������";
					}
					if(size.equalsIgnoreCase("query_cache_wlock_invalidate"))
					{
					 size="�Ա����WRITE����������ֵ";
					}
					if(size.equalsIgnoreCase("query_prealloc_size"))
					{
					 size="���ڲ�ѯ������ִ�еĹ̶��������Ĵ�С";
					}
					if(size.equalsIgnoreCase("range_alloc_block_size"))
					{
					 size="��Χ�Ż�ʱ����Ŀ�Ĵ�С";
					}
					if(size.equalsIgnoreCase("read_buffer_size"))
					{
					 size="ÿ���߳�����ɨ��ʱΪɨ���ÿ�������Ļ������Ĵ�С(�ֽ�)";
					}
					if(size.equalsIgnoreCase("read_only"))
					{
					 size="�����Ը��ƴӷ���������ΪONʱ���������Ƿ��������";
					}
					if(size.equalsIgnoreCase("read_only"))
					{
					 size="�����Ը��ƴӷ���������ΪONʱ���ӷ��������������";
					}
					if(size.equalsIgnoreCase("relay_log_purge"))
					{
					 size="��������Ҫ�м���־ʱ���û������Զ�����м���־";
					}
					if(size.equalsIgnoreCase("read_rnd_buffer_size"))
					{
					 size="�������������˳���ȡ��ʱ����ͨ���û�������ȡ�У���������Ӳ��";
					}
					if(size.equalsIgnoreCase("secure_auth"))
					{
					 size="�����--secure-authѡ��������MySQL���������Ƿ������оɸ�ʽ(4.1֮ǰ)����������˻������������";
					}
					if(size.equalsIgnoreCase("shared_memory"))
					{
					 size="(ֻ����Windows)�������Ƿ��������ڴ�����";
					}
					if(size.equalsIgnoreCase("shared_memory_base_name"))
					{
					 size="(ֻ����Windows)˵���������Ƿ��������ڴ����ӣ���Ϊ�����ڴ�����ʶ���";
					}
					if(size.equalsIgnoreCase("server_id"))
					{
					 size="���������Ʒ������ʹӸ��Ʒ�����";
					}
					if(size.equalsIgnoreCase("skip_external_locking"))
					{
					 size="mysqld�Ƿ�ʹ���ⲿ����";
					}
					if(size.equalsIgnoreCase("skip_networking"))
					{
					 size="���������ֻ������(��TCP/IP)����";
					}
					if(size.equalsIgnoreCase("skip_show_database"))
					{
					 size="��ֹ������SHOW DATABASESȨ�޵�����ʹ��SHOW DATABASES���";
					}
					if(size.equalsIgnoreCase("slave_compressed_protocol"))
					{
					 size="��������ӷ�������֧�֣�ȷ���Ƿ�ʹ�ô�/��ѹ��Э��";
					}
					if(size.equalsIgnoreCase("slave_load_tmpdir"))
					{
					 size="�ӷ�����Ϊ����LOAD DATA INFILE��䴴����ʱ�ļ���Ŀ¼��";
					}
					if(size.equalsIgnoreCase("slave_net_timeout"))
					{
					 size="����������ǰ�ȴ���/�����ӵĸ������ݵĵȴ�����";
					}
					if(size.equalsIgnoreCase("slave_skip_errors"))
					{
					 size="�ӷ�����Ӧ����(����)�ĸ��ƴ���";
					}
					if(size.equalsIgnoreCase("slave_transaction_retries"))
					{
					 size="���ƴӷ�����SQL�߳�δ��ִ����������ʾ����ֹͣǰ���Զ��ظ�slave_transaction_retries��";
					}
					if(size.equalsIgnoreCase("slow_launch_time"))
					{
					 size="��������̵߳�ʱ�䳬��������������������Slow_launch_threads״̬����";
					}
					if(size.equalsIgnoreCase("sort_buffer_size"))
					{
					 size="ÿ�������̷߳���Ļ������Ĵ�С";
					}
					if(size.equalsIgnoreCase("sql_mode"))
					{
					 size="��ǰ�ķ�����SQLģʽ�����Զ�̬����";
					}
					if(size.equalsIgnoreCase("storage_engine"))
					{
					 size="�ñ�����table_typeis��ͬ��ʡ���MySQL 5.1��,��ѡstorage_engine";
					}
					if(size.equalsIgnoreCase("sync_binlog"))
					{
					 size="���Ϊ������ÿ��sync_binlog'thд��ö�������־��MySQL�����������Ķ�������־ͬ����Ӳ����";
					}
					if(size.equalsIgnoreCase("sync_frm"))
					{
					 size="����ñ�����Ϊ1,����������ʱ��ʱ����.frm�ļ��Ƿ�ͬ����Ӳ����";
					}
					if(size.equalsIgnoreCase("system_time_zone"))
					{
					 size="������ϵͳʱ��";
					}
					if(size.equalsIgnoreCase("table_cache"))
					{
					 size="�����̴߳򿪵ı����Ŀ";
					}
					if(size.equalsIgnoreCase("table_type"))
					{
					 size="Ĭ�ϱ�����(�洢����)";
					}
					if(size.equalsIgnoreCase("thread_cache_size"))
					{
					 size="������Ӧ��������߳��Ա�����ʹ��";
					}
					if(size.equalsIgnoreCase("thread_stack"))
					{
					 size="ÿ���̵߳Ķ�ջ��С";
					}
					if(size.equalsIgnoreCase("time_format"))
					{
					 size="�ñ���Ϊʹ��";
					}
					if(size.equalsIgnoreCase("time_zone"))
					{
					 size="��ǰ��ʱ��";
					}
					if(size.equalsIgnoreCase("tmp_table_size"))
					{
					 size="����ڴ��ڵ���ʱ������ֵ��MySQL�Զ�����ת��ΪӲ���ϵ�MyISAM��";
					}
					if(size.equalsIgnoreCase("tmpdir"))
					{
					 size="������ʱ�ļ�����ʱ���Ŀ¼";
					}
					if(size.equalsIgnoreCase("transaction_alloc_block_size"))
					{
					 size="Ϊ���潫���浽��������־�е�����Ĳ�ѯ��������ڴ��Ĵ�С(�ֽ�)";
					}
					if(size.equalsIgnoreCase("transaction_prealloc_size"))
					{
					 size="transaction_alloc_blocks����Ĺ̶��������Ĵ�С���ֽڣ��������β�ѯ֮�䲻���ͷ�";
					}
					if(size.equalsIgnoreCase("tx_isolation"))
					{
					 size="Ĭ��������뼶��";
					}
					if(size.equalsIgnoreCase("updatable_views_with_limit"))
					{
					 size="�ñ�������������°���LIMIT�Ӿ䣬�Ƿ�����ڵ�ǰ����ʹ�ò��������ؼ��ֵ���ͼ���и���";
					}
					if(size.equalsIgnoreCase("version"))
					{
					 size="�������汾��";
					}
					if(size.equalsIgnoreCase("version_bdb"))
					{
					 size="BDB�洢����汾";
					}
					if(size.equalsIgnoreCase("version_comment"))
					{
					 size="configure�ű���һ��--with-commentѡ�������MySQLʱ���Խ���ע��";
					}
					if(size.equalsIgnoreCase("version_compile_machine"))
					{
					 size="MySQL�����Ļ�����ܹ�������";
					}
					if(size.equalsIgnoreCase("version_compile_os"))
					{
					 size="MySQL�����Ĳ���ϵͳ������";
					}
					if(size.equalsIgnoreCase("wait_timeout"))
					{
					 size="�������رշǽ�������֮ǰ�ȴ��������";
					}
			%>
																			<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																				height="28">
																				<td class="application-detail-data-body-list"><%=i+1%></td>
																				<td align=left
																					class="application-detail-data-body-list">
																					&nbsp;<%=size%></td>
																				<td class="application-detail-data-body-list"
																					align=left width=20%>
																					&nbsp;<%=free%></td>
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
					<td width=15% valign=top>
															<jsp:include page="/include/dbtoolbar.jsp">
																<jsp:param value="<%=myip %>" name="myip" />
																<jsp:param value="<%=myport %>" name="myport" />
																<jsp:param value="<%=myUser %>" name="myUser" />
																<jsp:param value="<%=myPassword %>" name="myPassword" />
																<jsp:param value="<%=mysid  %>" name="sid" />
																<jsp:param value="<%=id %>" name="id" />
																<jsp:param value="<%=dbPage %>" name="dbPage" />
																<jsp:param value="<%=dbType %>" name="dbType" />
																<jsp:param value="mysql" name="subtype"/>
															</jsp:include>
														</td>
				</tr>
			</table>
		</form>
		<script>
			
			Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	    Ext.get("process").on("click",function(){
  
  Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');   
  mainForm.action = "<%=rootPath%>/mysql.do?action=sychronizeData&dbPage=<%=dbPage %>&id=<%=id %>&myip=<%=myip %>&myport=<%=myport %>&myUser=<%=myUser %>&myPassword=<%=myPassword %>&sid=<%=mysid %>&flag=1";
  mainForm.submit();
 });    
});
</script>
	</BODY>
</HTML>