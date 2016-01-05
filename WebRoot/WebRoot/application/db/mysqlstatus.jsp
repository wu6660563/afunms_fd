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
  System.out.println("########################id = "+id);
  String myip = vo.getIpAddress();
  String myport = vo.getPort();
  String myUser = vo.getUser();
  String myPassword = EncryptUtil.decode(vo.getPassword());
  String mysid = "";
  String dbPage = "mysqlstatus";
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
	document.getElementById('mysqlDetailTitle-4').className='detail-data-title';
	document.getElementById('mysqlDetailTitle-4').onmouseover="this.className='detail-data-title'";
	document.getElementById('mysqlDetailTitle-4').onmouseout="this.className='detail-data-title'";
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
												<table class="container-main-application-detail">
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
												</table>
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
																		<table cellspacing="1" cellpadding="0" width="100%">
																			<tr>
																				<td height="28" align="center" bgcolor="#ECECEC"
																					colspan=6>
																					<b>���ݿ�״̬��Ϣ</b>
																				</td>
																			</tr>
																			<%
			if(tableinfo_v != null)
			{
				for(int i=0;i<tableinfo_v.size();i++){
					Hashtable ht = (Hashtable)tableinfo_v.get(i);
					
					String size = ht.get("variable_name").toString();
					String free = ht.get("value").toString();
					if(size.equalsIgnoreCase("Aborted_clients"))
					{
					 size="���ڿͻ���û����ȷ�ر����ӵ��¿ͻ�����ֹ���жϵ�������";
					}
					if(size.equalsIgnoreCase("Aborted_connects"))
					{
					 size="��ͼ���ӵ�MySQL��������ʧ�ܵ�������";
					}
					if(size.equalsIgnoreCase("Binlog_cache_disk_use"))
					{
					 size="ʹ����ʱ��������־���浫����binlog_cache_sizeֵ��ʹ����ʱ�ļ������������е�������������";
					}
					if(size.equalsIgnoreCase("Binlog_cache_use"))
					{
					 size="ʹ����ʱ��������־�������������";
					}
					if(size.equalsIgnoreCase("Bytes_received"))
					{
					 size="�����пͻ��˽��յ����ֽ���";
					}
					if(size.equalsIgnoreCase("Bytes_sent"))
					{
					 size="���͸����пͻ��˵��ֽ���";
					}
					if(size.equalsIgnoreCase("Com_admin_commands"))
					{
					 size="admin_commands���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_alter_db"))
					{
					 size="alter_db���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_alter_table"))
					{
					 size="alter_table���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_analyze"))
					{
					 size="analyze���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_backup_table"))
					{
					 size="backup_table���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_begin"))
					{
					 size="begin���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_change_db"))
					{
					 size="change_db���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_change_master"))
					{
					 size="change_master���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_check"))
					{
					 size="check���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_checksum"))
					{
					 size="checksum���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_commit"))
					{
					 size="commit���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_create_db"))
					{
					 size="create_db���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_create_function"))
					{
					 size="create_function���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_create_index"))
					{
					 size="create_index���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_create_table"))
					{
					 size="create_table���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_dealloc_sql"))
					{
					 size="dealloc_sql���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_delete"))
					{
					 size="delete���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_delete_multi"))
					{
					 size="delete_multi���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_do"))
					{
					 size="do���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_drop_db"))
					{
					 size="drop_db���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_drop_function"))
					{
					 size="drop_function���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_drop_index"))
					{
					 size="drop_index���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_drop_table"))
					{
					 size="drop_table���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_drop_user"))
					{
					 size="drop_user���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_execute_sql"))
					{
					 size="execute_sql���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_flush"))
					{
					 size="flush���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_grant"))
					{
					 size="grant���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_ha_close"))
					{
					 size="ha_close���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_ha_open"))
					{
					 size="ha_open���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_ha_read"))
					{
					 size="ha_read���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_help"))
					{
					 size="help���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_insert"))
					{
					 size="insert���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_insert_select"))
					{
					 size="insert_select���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_kill"))
					{
					 size="kill���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_load"))
					{
					 size="load���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_load_master_data"))
					{
					 size="load_master_data���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_load_master_table"))
					{
					 size="load_master_table���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_lock_tables"))
					{
					 size="lock_tables���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_optimize"))
					{
					 size="optimize���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_preload_keys"))
					{
					 size="preload_keys���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_prepare_sql"))
					{
					 size="prepare_sql���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_purge"))
					{
					 size="purge���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_purge_before_date"))
					{
					 size="purge_before_date���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_rename_table"))
					{
					 size="rename_table���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_repair"))
					{
					 size="repair���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_replace"))
					{
					 size="replace���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_replace_select"))
					{
					 size="replace_select���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_reset"))
					{
					 size="reset���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_restore_table"))
					{
					 size="restore_table���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_revoke"))
					{
					 size="revoke���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_revoke_all"))
					{
					 size="revoke_all���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_rollback"))
					{
					 size="rollback���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_savepoint"))
					{
					 size="savepoint���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_select"))
					{
					 size="select���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_set_option"))
					{
					 size="set_option���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_show_binlog_events"))
					{
					 size="show_binlog_events���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_show_binlogs"))
					{
					 size="show_binlogs���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_show_charsets"))
					{
					 size="show_charsets���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_show_collations"))
					{
					 size="show_collations���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_show_column_types"))
					{
					 size="how_column_types���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_show_create_db"))
					{
					 size="show_create_db���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_show_create_table"))
					{
					 size="show_create_table���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_show_databases"))
					{
					 size="show_databases���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_show_errors"))
					{
					 size="show_errors���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_show_fields"))
					{
					 size="show_fields���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_show_grants"))
					{
					 size="show_grants���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_show_innodb_status"))
					{
					 size="show_innodb_status���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_show_keys"))
					{
					 size="show_keys���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_show_logs"))
					{
					 size="show_logs���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_show_master_status"))
					{
					 size="show_master_status���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_show_ndb_status"))
					{
					 size="show_ndb_status���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_show_new_master"))
					{
					 size="show_new_master���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_show_open_tables"))
					{
					 size="show_open_tables���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_show_privileges"))
					{
					 size="show_privileges���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_show_processlist"))
					{
					 size="show_processlist���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_show_slave_hosts"))
					{
					 size="show_slave_hosts���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_show_slave_status"))
					{
					 size="show_slave_status���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_show_status"))
					{
					 size="show_status���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_show_storage_engines"))
					{
					 size="show_storage_engines���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_show_tables"))
					{
					 size="show_tables���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_show_triggers"))
					{
					 size="show_triggers���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_show_variables"))
					{
					 size="show_variables���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_show_warnings"))
					{
					 size="show_warnings���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_slave_start"))
					{
					 size="slave_start���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_slave_stop"))
					{
					 size="slave_stop���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_truncate"))
					{
					 size="truncate���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_unlock_tables"))
					{
					 size="unlock_tables���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_update"))
					{
					 size="update���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_update_multi"))
					{
					 size="update_multi���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_xa_commit"))
					{
					 size="xa_commi���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_xa_end"))
					{
					 size="xa_end���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_xa_prepare"))
					{
					 size="xa_prepare���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_xa_recover"))
					{
					 size="xa_recover���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_xa_rollback"))
					{
					 size="xa_rollback���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Com_xa_start"))
					{
					 size="xa_start���ִ�еĴ���";
					}
					if(size.equalsIgnoreCase("Connections"))
					{
					 size="��ͼ���ӵ�(�����Ƿ�ɹ�)MySQL��������������";
					}
					if(size.equalsIgnoreCase("Created_tmp_disk_tables"))
					{
					 size="������ִ�����ʱ��Ӳ�����Զ���������ʱ�������";
					}
					if(size.equalsIgnoreCase("Created_tmp_files"))
					{
					 size="mysqld�Ѿ���������ʱ�ļ�������";
					}
					if(size.equalsIgnoreCase("Created_tmp_tables"))
					{
					 size="������ִ�����ʱ�Զ��������ڴ��е���ʱ�������";
					}
					if(size.equalsIgnoreCase("Delayed_errors"))
					{
					 size="��INSERT DELAYEDд�ĳ��ִ��������(����Ϊduplicate key)";
					}
					if(size.equalsIgnoreCase("Delayed_insert_threads"))
					{
					 size="��ʹ�õ�INSERT DELAYED�������߳���";
					}
					if(size.equalsIgnoreCase("Delayed_writes"))
					{
					 size="д���INSERT DELAYED����";
					}
					if(size.equalsIgnoreCase("Flush_commands"))
					{
					 size="ִ�е�FLUSH�����";
					}
					if(size.equalsIgnoreCase("Handler_commit"))
					{
					 size="�ڲ��ύ�����";
					}
					if(size.equalsIgnoreCase("Handler_delete"))
					{
					 size="�дӱ���ɾ���Ĵ���";
					}
					if(size.equalsIgnoreCase("Handler_discover"))
					{
					 size="MySQL������������NDB CLUSTER�洢�����Ƿ�֪��ĳһ���ֵı�";
					}
					if(size.equalsIgnoreCase("Handler_read_first"))
					{
					 size="�����е�һ�������Ĵ���";
					}
					if(size.equalsIgnoreCase("Handler_read_key"))
					{
					 size="���ݼ���һ�е�������";
					}
					if(size.equalsIgnoreCase("Handler_read_next"))
					{
					 size="���ռ�˳�����һ�е�������";
					}
					if(size.equalsIgnoreCase("Handler_read_prev"))
					{
					 size="���ռ�˳���ǰһ�е�������";
					}
					if(size.equalsIgnoreCase("Handler_read_rnd"))
					{
					 size="���ݹ̶�λ�ö�һ�е�������";
					}
					if(size.equalsIgnoreCase("Handler_read_rnd_next"))
					{
					 size="�������ļ��ж���һ�е�������";
					}
					if(size.equalsIgnoreCase("Handler_rollback"))
					{
					 size="�ڲ�ROLLBACK��������";
					}
					if(size.equalsIgnoreCase("Handler_update"))
					{
					 size="�ڱ��ڸ���һ�е�������";
					}
					if(size.equalsIgnoreCase("Handler_write"))
					{
					 size="�ڱ��ڲ���һ�е�������";
					}
					if(size.equalsIgnoreCase("Innodb_buffer_pool_pages_data"))
					{
					 size="�������ݵ�ҳ��(���ɾ�)";
					}
					if(size.equalsIgnoreCase("Innodb_buffer_pool_pages_dirty"))
					{
					 size="��ǰ����ҳ��";
					}
					if(size.equalsIgnoreCase("Innodb_buffer_pool_pages_flushed"))
					{
					 size="����յĻ����ҳ��";
					}
					if(size.equalsIgnoreCase("Innodb_buffer_pool_pages_free"))
					{
					 size="��ҳ��";
					}
					if(size.equalsIgnoreCase("Innodb_buffer_pool_pages_latched"))
					{
					 size="��InnoDB�������������ҳ��";
					}
					if(size.equalsIgnoreCase("Innodb_buffer_pool_pages_misc"))
					{
					 size="æ��ҳ��";
					}
					if(size.equalsIgnoreCase("Innodb_buffer_pool_pages_total"))
					{
					 size="������ܴ�С��ҳ����";
					}
					if(size.equalsIgnoreCase("Innodb_buffer_pool_read_ahead_rnd"))
					{
					 size="InnoDB��ʼ���ġ������read-aheads��";
					}
					if(size.equalsIgnoreCase("Innodb_buffer_pool_read_ahead_seq"))
					{
					 size="InnoDB��ʼ����˳��read-aheads��";
					}
					if(size.equalsIgnoreCase("Innodb_buffer_pool_read_requests"))
					{
					 size="InnoDB�Ѿ���ɵ��߼���������";
					}
					if(size.equalsIgnoreCase("Innodb_buffer_pool_reads"))
					{
					 size="��������InnoDB���뵥ҳ��ȡ�Ļ�����е��߼�������";
					}
					if(size.equalsIgnoreCase("Innodb_buffer_pool_wait_free"))
					{
					 size="û��������� ͨ����̨��InnoDB�����д";
					}
					if(size.equalsIgnoreCase("Innodb_buffer_pool_write_requests"))
					{
					 size="��InnoDB����ص�д����";
					}
					if(size.equalsIgnoreCase("Innodb_data_fsyncs"))
					{
					 size="fsync()������";
					}
					if(size.equalsIgnoreCase("Innodb_data_pending_fsyncs"))
					{
					 size="��ǰ�����fsync()������";
					}
					if(size.equalsIgnoreCase("Innodb_data_pending_reads"))
					{
					 size="��ǰ����Ķ���";
					}
					if(size.equalsIgnoreCase("Innodb_data_pending_writes"))
					{
					 size="��ǰ�����д��";
					}
					if(size.equalsIgnoreCase("Innodb_data_read"))
					{
					 size="�����Ѿ���ȡ�������������ֽڣ�";
					}
					if(size.equalsIgnoreCase("Innodb_data_reads"))
					{
					 size="���ݶ�������";
					}
					if(size.equalsIgnoreCase("IInnodb_data_writes"))
					{
					 size="����д������";
					}
					if(size.equalsIgnoreCase("Innodb_data_written"))
					{
					 size="�����Ѿ�д������������ֽڣ�";
					}
					if(size.equalsIgnoreCase("Innodb_dblwr_pages_written"))
					{
					 size="Ϊ�Ѿ�ִ�е�˫д��������д�õ�ҳ��";
					}
					if(size.equalsIgnoreCase("Innodb_dblwr_writes"))
					{
					 size="�Ѿ�ִ�е�˫д��������";
					}
					if(size.equalsIgnoreCase("Innodb_log_waits"))
					{
					 size="����ȴ���ʱ�䣬��Ϊ��־������̫С���ڼ���ǰ�����ȵȴ��������";
					}
					if(size.equalsIgnoreCase("Innodb_log_write_requests"))
					{
					 size="��־д������";
					}
					if(size.equalsIgnoreCase("Innodb_log_writes"))
					{
					 size="����־�ļ�������д����";
					}
					if(size.equalsIgnoreCase("Innodb_os_log_fsyncs"))
					{
					 size="����־�ļ���ɵ�fsync()д����";
					}
					if(size.equalsIgnoreCase("Innodb_os_log_pending_fsyncs"))
					{
					 size="�������־�ļ�fsync()��������";
					}
					if(size.equalsIgnoreCase("Innodb_os_log_pending_writes"))
					{
					 size="�������־�ļ�д����";
					}
					if(size.equalsIgnoreCase("Innodb_os_log_written"))
					{
					 size="д����־�ļ����ֽ���";
					}
					if(size.equalsIgnoreCase("Innodb_page_size"))
					{
					 size="�����InnoDBҳ��С(Ĭ��16KB) ���ֵ��ҳ������ ҳ�Ĵ�С������ת��Ϊ�ֽ�";
					}
					if(size.equalsIgnoreCase("Innodb_pages_created"))
					{
					 size="������ҳ��";
					}
					if(size.equalsIgnoreCase("Innodb_pages_read"))
					{
					 size="��ȡ��ҳ��";
					}
					if(size.equalsIgnoreCase("Innodb_pages_written"))
					{
					 size="д���ҳ��";
					}
					if(size.equalsIgnoreCase("Innodb_row_lock_current_waits"))
					{
					 size="��ǰ�ȴ��Ĵ�����������";
					}
					if(size.equalsIgnoreCase("Innodb_row_lock_time"))
					{
					 size="���������ѵ���ʱ�䣬��λ����";
					}
					if(size.equalsIgnoreCase("Innodb_row_lock_time_avg"))
					{
					 size="��������ƽ��ʱ�䣬��λ����";
					}
					if(size.equalsIgnoreCase("Innodb_row_lock_time_max"))
					{
					 size="���������ʱ�䣬��λ����";
					}
					if(size.equalsIgnoreCase("Innodb_row_lock_waits"))
					{
					 size="һ����������ȴ���ʱ����";
					}
					if(size.equalsIgnoreCase("Innodb_rows_deleted"))
					{
					 size="��InnoDB��ɾ��������";
					}
					if(size.equalsIgnoreCase("Innodb_rows_inserted"))
					{
					 size="���뵽InnoDB�������";
					}
					if(size.equalsIgnoreCase("Innodb_rows_read"))
					{
					 size="��InnoDB���ȡ������";
					}
					if(size.equalsIgnoreCase("Innodb_rows_updated"))
					{
					 size="InnoDB���ڸ��µ�����";
					}
					if(size.equalsIgnoreCase("Key_blocks_not_flushed"))
					{
					 size="���������Ѿ����ĵ���û����յ�Ӳ���ϵļ������ݿ�����";
					}
					if(size.equalsIgnoreCase("Key_blocks_unused"))
					{
					 size="��������δʹ�õĿ�����";
					}
					if(size.equalsIgnoreCase("Key_blocks_used"))
					{
					 size="��������ʹ�õĿ�����";
					}
					if(size.equalsIgnoreCase("Key_read_requests"))
					{
					 size="�ӻ�����������ݿ��������";
					}
					if(size.equalsIgnoreCase("Key_reads"))
					{
					 size="��Ӳ�̶�ȡ�������ݿ�Ĵ���";
					}
					if(size.equalsIgnoreCase("Key_write_requests"))
					{
					 size="���������ݿ�д�뻺���������";
					}
					if(size.equalsIgnoreCase("Key_writes"))
					{
					 size="��Ӳ��д�뽫�������ݿ������д�����Ĵ���";
					}
					if(size.equalsIgnoreCase("Last_query_cost"))
					{
					 size="�ò�ѯ�Ż��������������Ĳ�ѯ���ܳɱ�";
					}
					if(size.equalsIgnoreCase("Max_used_connections"))
					{
					 size="�������������Ѿ�ͬʱʹ�õ����ӵ��������";
					}
					if(size.equalsIgnoreCase("Not_flushed_delayed_rows"))
					{
					 size="�ȴ�д��INSERT DELAY���е�����";
					}
					if(size.equalsIgnoreCase("Open_files"))
					{
					 size="�򿪵��ļ�����Ŀ";
					}
					if(size.equalsIgnoreCase("Open_streams"))
					{
					 size="�򿪵���������(��Ҫ���ڼ�¼)";
					}
					if(size.equalsIgnoreCase("Open_tables"))
					{
					 size="��ǰ�򿪵ı������";
					}
					if(size.equalsIgnoreCase("Opened_tables"))
					{
					 size="�Ѿ��򿪵ı������";
					}
					if(size.equalsIgnoreCase("Qcache_free_blocks"))
					{
					 size="��ѯ�����������ڴ�������";
					}
					if(size.equalsIgnoreCase("Qcache_free_memory"))
					{
					 size="���ڲ�ѯ����������ڴ������";
					}
					if(size.equalsIgnoreCase("Qcache_hits"))
					{
					 size="��ѯ���汻���ʵĴ���";
					}
					if(size.equalsIgnoreCase("Qcache_inserts"))
					{
					 size="���뵽����Ĳ�ѯ����";
					}
					if(size.equalsIgnoreCase("Qcache_lowmem_prunes"))
					{
					 size="�����ڴ���ٴӻ���ɾ���Ĳ�ѯ����";
					}
					if(size.equalsIgnoreCase("Qcache_not_cached"))
					{
					 size="�ǻ����ѯ��(���ɻ��棬������query_cache_type�趨ֵδ����)";
					}
					if(size.equalsIgnoreCase("Qcache_queries_in_cache"))
					{
					 size="�Ǽǵ������ڵĲ�ѯ������";
					}
					if(size.equalsIgnoreCase("Qcache_total_blocks"))
					{
					 size="��ѯ�����ڵ��ܿ���";
					}
					if(size.equalsIgnoreCase("Questions"))
					{
					 size="�Ѿ����͸��������Ĳ�ѯ�ĸ���";
					}
					if(size.equalsIgnoreCase("Rpl_status"))
					{
					 size="ʧ�ܰ�ȫ����״̬(��δʹ��)";
					}
					if(size.equalsIgnoreCase("Select_full_join"))
					{
					 size="û��ʹ�����������ӵ������������ֵ��Ϊ0,��Ӧ��ϸ���������";
					}
					if(size.equalsIgnoreCase("Select_full_range_join"))
					{
					 size="�����õı���ʹ�÷�Χ���������ӵ�����";
					}
					if(size.equalsIgnoreCase("Select_range"))
					{
					 size="�ڵ�һ������ʹ�÷�Χ�����ӵ�����";
					}
					if(size.equalsIgnoreCase("Select_range_check"))
					{
					 size="��ÿһ�����ݺ�Լ�ֵ���м��Ĳ�����ֵ�����ӵ�����";
					}
					if(size.equalsIgnoreCase("Select_scan"))
					{
					 size="�Ե�һ���������ȫɨ������ӵ�����";
					}
					if(size.equalsIgnoreCase("Slave_open_temp_tables"))
					{
					 size="��ǰ�ɴ�SQL�̴߳򿪵���ʱ�������";
					}
					if(size.equalsIgnoreCase("Slave_retried_transactions"))
					{
					 size="�������ƴӷ�����SQL�̳߳���������ܴ���";
					}
					if(size.equalsIgnoreCase("Slave_running"))
					{
					 size="����÷����������ӵ����������Ĵӷ����������ֵΪON";
					}
					if(size.equalsIgnoreCase("Slow_launch_threads"))
					{
					 size="����ʱ�䳬��slow_launch_time����߳���";
					}
					if(size.equalsIgnoreCase("Slow_queries"))
					{
					 size="��ѯʱ�䳬��long_query_time��Ĳ�ѯ�ĸ���";
					}
					if(size.equalsIgnoreCase("Sort_merge_passes"))
					{
					 size="�����㷨�Ѿ�ִ�еĺϲ�������";
					}
					if(size.equalsIgnoreCase("Sort_range"))
					{
					 size="�ڷ�Χ��ִ�е����������";
					}
					if(size.equalsIgnoreCase("Sort_rows"))
					{
					 size="�Ѿ����������";
					}
					if(size.equalsIgnoreCase("Sort_scan"))
					{
					 size="ͨ��ɨ�����ɵ����������";
					}
					if(size.equalsIgnoreCase("Ssl_accept_renegotiates"))
					{
					 size="����SSL���ӵı���";
					}
					if(size.equalsIgnoreCase("Ssl_accept_renegotiates"))
					{
					 size="����SSL���ӵı���accept_renegotiates";
					}
					if(size.equalsIgnoreCase("Ssl_accepts"))
					{
					 size="����SSL���ӵı���accepts";
					}
					if(size.equalsIgnoreCase("Ssl_callback_cache_hits"))
					{
					 size="����SSL���ӵı���callback_cache_hits";
					}
					if(size.equalsIgnoreCase("Ssl_cipher"))
					{
					 size="����SSL���ӵı���cipher";
					}
					if(size.equalsIgnoreCase("Ssl_cipher_list"))
					{
					 size="����SSL���ӵı���cipher_list";
					}
					if(size.equalsIgnoreCase("Ssl_client_connects"))
					{
					 size="����SSL���ӵı���client_connects";
					}
					if(size.equalsIgnoreCase("Ssl_connect_renegotiates"))
					{
					 size="����SSL���ӵı���connect_renegotiates";
					}
					if(size.equalsIgnoreCase("Ssl_ctx_verify_depth"))
					{
					 size="����SSL���ӵı���ctx_verify_depth";
					}
					if(size.equalsIgnoreCase("Ssl_ctx_verify_mode"))
					{
					 size="����SSL���ӵı���ctx_verify_mode";
					}
					if(size.equalsIgnoreCase("Ssl_default_timeout"))
					{
					 size="����SSL���ӵı���default_timeout";
					}
					if(size.equalsIgnoreCase("Ssl_finished_accepts"))
					{
					 size="����SSL���ӵı���finished_accepts";
					}
					if(size.equalsIgnoreCase("Ssl_finished_connects"))
					{
					 size="����SSL���ӵı���finished_connects";
					}
					if(size.equalsIgnoreCase("Ssl_session_cache_hits"))
					{
					 size="����SSL���ӵı���session_cache_hits";
					}
					if(size.equalsIgnoreCase("Ssl_session_cache_misses"))
					{
					 size="����SSL���ӵı���session_cache_misses";
					}
					if(size.equalsIgnoreCase("Ssl_session_cache_mode"))
					{
					 size="����SSL���ӵı���session_cache_mode";
					}
					if(size.equalsIgnoreCase("Ssl_session_cache_overflows"))
					{
					 size="����SSL���ӵı���session_cache_overflows";
					}
					if(size.equalsIgnoreCase("Ssl_session_cache_size"))
					{
					 size="����SSL���ӵı���session_cache_size";
					}
					if(size.equalsIgnoreCase("Ssl_session_cache_timeouts"))
					{
					 size="����SSL���ӵı���session_cache_timeouts";
					}
					if(size.equalsIgnoreCase("Ssl_sessions_reused"))
					{
					 size="����SSL���ӵı���sessions_reused";
					}
					if(size.equalsIgnoreCase("Ssl_used_session_cache_entries"))
					{
					 size="����SSL���ӵı���used_session_cache_entries";
					}
					if(size.equalsIgnoreCase("Ssl_verify_depth"))
					{
					 size="����SSL���ӵı���verify_depth";
					}
					if(size.equalsIgnoreCase("Ssl_verify_mode"))
					{
					 size="����SSL���ӵı���verify_mode";
					}
					if(size.equalsIgnoreCase("Ssl_version"))
					{
					 size="����SSL���ӵı���version";
					}
					if(size.equalsIgnoreCase("Table_locks_immediate"))
					{
					 size="������õı�����Ĵ���";
					}
					if(size.equalsIgnoreCase("Table_locks_waited"))
					{
					 size="����������õı�����Ĵ���";
					}
					if(size.equalsIgnoreCase("Threads_cached"))
					{
					 size="�̻߳����ڵ��̵߳�����";
					}
					if(size.equalsIgnoreCase("Threads_connected"))
					{
					 size="��ǰ�򿪵����ӵ�����";
					}
					if(size.equalsIgnoreCase("Threads_created"))
					{
					 size="���������������ӵ��߳���";
					}
					if(size.equalsIgnoreCase("Threads_running"))
					{
					 size="����ģ���˯��״̬���߳���";
					}
						if(size.equalsIgnoreCase("Uptime"))
					{
					 size="�������Ѿ����е�ʱ�䣨����Ϊ��λ��";
					}
					
					
			%>
																			<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=28>
																				<td class="application-detail-data-body-list"><%=i+1%></td>
																				<td class="application-detail-data-body-list"
																					align=left>
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
					<td  width=15% valign=top>
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