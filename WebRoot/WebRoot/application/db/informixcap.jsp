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
	try {
		String rootPath = request.getContextPath();
		DBVo vo = (DBVo) request.getAttribute("db");
		String menuTable = (String) request.getAttribute("menuTable");
		String id = (String) request.getAttribute("id");
		String myip = vo.getIpAddress();
		String myport = vo.getPort();
		String myUser = vo.getUser();
		String myPassword = EncryptUtil.decode(vo.getPassword());
		String mysid = "";
		String dbPage = "informixcap";
		String dbType = "informix";

		Hashtable max = (Hashtable) request.getAttribute("max");
		Hashtable imgurl = (Hashtable) request.getAttribute("imgurl");
		String chart1 = (String) request.getAttribute("chart1");
		String dbtye = (String) request.getAttribute("dbtye");
		String managed = (String) request.getAttribute("managed");
		String runstr = (String) request.getAttribute("runstr");
		Hashtable informixOtherData = (Hashtable)request.getAttribute("informixOtherData");

		Hashtable dbinfo = new Hashtable();
		dbinfo = (Hashtable) request.getAttribute("dbValue");
		ArrayList dbspaces = new ArrayList();
		if (dbinfo != null) {
			dbspaces = (ArrayList) dbinfo.get("informixspaces");//���ݿ�ռ���Ϣ
		}
		ArrayList dblock = new ArrayList();
		ArrayList dbsession = new ArrayList();
		ArrayList dbdatabase = new ArrayList();
		String name = "";
		String dbserver = "";
		String createuser = "";
		String createtime = "";
		//���ݿ������Ϣ
		Hashtable databaseinfo = new Hashtable();
		if (dbinfo != null) {
			dbdatabase = (ArrayList) dbinfo.get("databaselist");
			if (dbdatabase != null && dbdatabase.size() > 0) {
				databaseinfo = (Hashtable) dbdatabase.get(0);
				name = (String) databaseinfo.get("dbname");
				dbserver = (String) databaseinfo.get("dbserver");
				createuser = (String) databaseinfo.get("createuser");
				createtime = (String) databaseinfo.get("createtime");
			}
			dbsession = (ArrayList) dbinfo.get("sessionList");//�Ự��Ϣ
			dblock = (ArrayList) dbinfo.get("lockList");//����Ϣ

		}
		double avgpingcon = (Double) request.getAttribute("avgpingcon");
		int percent1 = Double.valueOf(avgpingcon).intValue();
		int percent2 = 100 - percent1;

		String picip = CommonUtil.doip(myip);

		//���ɵ���ƽ����ͨ��ͼ��
		CreatePiePicture _cpp = new CreatePiePicture();
		_cpp.createAvgPingPic(picip, avgpingcon);

		StringBuffer dataStr2 = new StringBuffer();
		dataStr2.append("��ͨ;").append(Math.round(avgpingcon)).append(
				";false;7CFC00\\n");
		dataStr2.append("δ��ͨ;").append(100 - Math.round(avgpingcon))
				.append(";false;FF0000\\n");
		String avgdata = dataStr2.toString();
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
		<script type="text/javascript"
			src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
		<script>
function createQueryString(){
		var user_name = encodeURI($("#user_name").val());
		var passwd = encodeURI($("#passwd").val());
		var queryString = {userName:user_name,passwd:passwd};
		return queryString;
	}

</script>
		<script language="JavaScript" type="text/JavaScript">


Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
 Ext.get("process").on("click",function(){
  
  Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');   
  //location.href="EventqueryMgr.do";
        //msg.style.display="block";
        mainForm.action = "<%=rootPath%>/cfgfile.do?action=getcfgfile&ipaddress=<%=vo.getIpAddress()%>";
        mainForm.submit();
 });	
	
});


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
	document.getElementById('inforDetailTitle-0').className='detail-data-title';
	document.getElementById('inforDetailTitle-0').onmouseover="this.className='detail-data-title'";
	document.getElementById('inforDetailTitle-0').onmouseout="this.className='detail-data-title'";
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
		<div id="loading">
			<div class="loading-indicator">
				<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif"
					width="32" height="32" style="margin-right: 8px;" align="middle" />
				Loading...
			</div>
		</div>
		<div id="loading-mask" style=""></div>
		<form id="mainForm" method="post" name="mainForm">
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
														<td valign="top">
															<jsp:include page="/topology/includejsp/db_informix.jsp" >
																<jsp:param name="dbtye" value="<%=dbtye %>"/>
																<jsp:param name="managed" value="<%=managed %>"/>
																<jsp:param name="runstr" value="<%=runstr %>"/>
																<jsp:param name="ipAddress" value="<%=vo.getIpAddress() %>"/>
																<jsp:param name="name" value="<%=name %>"/>
																<jsp:param name="dbserver" value="<%=dbserver %>"/>
																<jsp:param name="createuser" value="<%=createuser %>"/>
																<jsp:param name="createuser" value="<%=createuser %>"/>
																<jsp:param name="createtime" value="<%=createtime %>"/>
																<jsp:param name="picip" value="<%=picip %>"/>
																<jsp:param name="avgdata" value="<%=avgdata %>"/> 
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
															<%=inforDetailTitleTable%>
														</td>
													</tr>
													<tr>
														<td>
															<table class="application-detail-data-body">
																<tr>
																	<td colspan="6">
																		<table width="100%" border="0" cellpadding="0"
																			cellspacing="1">
																			<tr>
																				<td align=center width=40% valign=top>
																					<br>
																					<table cellpadding="0" cellspacing="0" width=98%>
																						<tr>
																							<td width="100%">
																								<div id="flashcontent1">
																									<strong>You need to upgrade your Flash
																										Player</strong>
																								</div>
																								<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Oracle_Ping.swf?ipadress=<%=vo.getIpAddress()%>&category=INFORMIXPing", "Oracle_Ping", "346", "330", "8", "#ffffff");
													so.write("flashcontent1");
												</script>
																							</td>
																						</tr>
																					</table>
																					<br>
																				</td>
																				<td align=center valign=top width=58%>
																					<br>
																					<table id="body-container" class="body-container"
																						style="background-color: #FFFFFF;" width="40%">
																						<tr>
																							<td class="td-container-main">
																								<table id="container-main"
																									class="container-main">
																									<tr>
																										<td class="td-container-main-add">
																											<table id="container-main-add"
																												class="container-main-add">
																												<tr>
																													<td>
																														<table id="add-content"
																															class="add-content">
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
																																				<b>Informix���ݿ��ռ�ʹ����</b>
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
																																		class="detail-content-body">
																																		<tr>
																																			<td>
																																				<table cellpadding="1"
																																					cellspacing="1" width=48%>

																																					<tr>
																																						<td valign=top>
																																							<table cellpadding="0"
																																								cellspacing="0" width=48%
																																								align=center>
																																								<tr>
																																									<td align=center colspan=3>
																																										<%
																																											String freeStr[] = null;
																																												String tsnames[] = null;
																																												StringBuffer tablespace = new StringBuffer();
																																												String tabledata = "";
																																												String[] colorStr = new String[] { "#33CCFF", "#003366",
																																														"#33FF33", "#FF0033", "#9900FF", "#FFFF00", "#333399",
																																														"#0000FF", "#A52A2A", "#23f266", "#9932CC", "#FF1493" };
																																												tablespace.append("<chart><series>");
																																												if (dbspaces != null) {
																																													if (dbspaces.size() > 0) {
																																														DecimalFormat df = new DecimalFormat("#.###");
																																														freeStr = new String[dbspaces.size()];
																																														tsnames = new String[dbspaces.size()];
																																														for (int i = 0; i < dbspaces.size(); i++) {

																																															Hashtable tablesVO = (Hashtable) dbspaces.get(i);
																																															freeStr[i] = df.format(100 - Float
																																																	.parseFloat(tablesVO.get("percent_free")
																																																			+ ""))
																																																	+ "";
																																															tsnames[i] = (String) tablesVO.get("dbspace");
																																														}
																																													}
																																												}
																																												if(tsnames!=null){
																																												for (int i = 0; i < tsnames.length; i++) {
																																													tablespace.append("<value xid='").append(i).append("'>")
																																															.append(tsnames[i]).append("</value>");
																																												}
																																												}
																																												tablespace.append("</series><graphs><graph gid='0'>");
																																												if(freeStr!=null)
																																												{
																																											 for (int i = 0,j=0; i < freeStr.length; i++,j++) {
																																											   if(i/colorStr.length==1)j=0;
																																													tablespace.append("<value xid='").append(i).append(
																																															"' color='").append(colorStr[j]).append(
																																															"'>" + freeStr[i]).append("</value>");

																																												}
																																												}
																																												tablespace.append("</graph></graphs></chart>");
																																												tabledata = tablespace.toString();
																																										%>
																																										<div id="tablespace">
																																											<strong>You need to
																																												upgrade your Flash Player</strong>
																																										</div>
																																										<script type="text/javascript"
																																											src="<%=rootPath%>/include/swfobject.js"></script>
																																										<script type="text/javascript">
						                                                                                                 var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "ampie","390", "278", "8", "#FFFFFF");
						                                                                                                     so.addVariable("path", "<%=rootPath%>/amchart/");
						                                                                                                     so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/dbpercent_settings.xml"));
						                                                                                                     so.addVariable("chart_data","<%=tabledata%>");
						                                                                                                     so.write("tablespace");
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
																															<tr>
																																<td>
																																	<table id="detail-content-footer"
																																		class="detail-content-footer">
																																		<tr>
																																			<td>
																																				<table width="100%" border="0"
																																					cellspacing="0" cellpadding="0">
																																					<tr>
																																						<td align="left" valign="bottom">
																																							<img
																																								src="<%=rootPath%>/common/images/right_b_01.jpg"
																																								width="5" height="12" />
																																						</td>
																																						<td></td>
																																						<td align="right" valign="bottom">
																																							<img
																																								src="<%=rootPath%>/common/images/right_b_03.jpg"
																																								width="5" height="12" />
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
																			<tr align="left" bgcolor="#ECECEC">
																				<td height="28">
																					<b>&nbsp;Informix���ݿ����ռ���Ϣ&nbsp;&nbsp;</b>
																				</td>
																				<td height="28" align=right>
																					<b>&nbsp;<a href="#"
																						onclick='window.open("<%=rootPath%>/hostreport.do?action=showDiskReport&ipaddress=<%=vo.getIpAddress()%>","portScanWindow","width=800,height=400,scrollbars=yes,resizable=yes")'>���ݿ����ڵķ������Ĵ�����Ϣ</a>&nbsp;&nbsp;&nbsp;&nbsp;</b>
																				</td>
																			</tr>
																			<tr bgcolor="#FFFFFF">
																				<td align=center colspan=2>

																					<table width="100%" border="0" cellpadding="3"
																						cellspacing="1" bgcolor="#FFFFFF">
																						<tr bgcolor="#ECECEC" height=28>
																							<td width="15%"
																								class="application-detail-data-body-title">
																								<strong>�ռ�����</strong>
																							</td>
																							<td width="15%"
																								class="application-detail-data-body-title">
																								<strong>�ռ�������</strong>
																							</td>
																							<td width="15%"
																								class="application-detail-data-body-title">
																								<strong>�ÿ��ļ���·��</strong>
																							</td>
																							<td width="15%"
																								class="application-detail-data-body-title">
																								<strong>�ռ��С</strong>
																							</td>
																							<td width="15%"
																								class="application-detail-data-body-title">
																								<strong>��ʹ�õĿռ�</strong>
																							</td>
																							<td width="13%"
																								class="application-detail-data-body-title">
																								<strong>���пռ�</strong>
																							</td>
																							<td width="15%"
																								class="application-detail-data-body-title">
																								<strong>�ռ�ʹ����</strong>
																							</td>
																						</tr>
																						<%
																						
																						Hashtable count = new Hashtable();
																						if (dbspaces != null) {
																									if (dbspaces.size() > 0) {
																										DecimalFormat df = new DecimalFormat("#.###");
																										int p = 0;
																										for (int i = 0; i < dbspaces.size(); i++) {
																											Hashtable tablesVO = (Hashtable) dbspaces.get(i);
																											if(!count.containsKey(tablesVO.get("dbspace"))){
																												p=1;
																												InformixCount infor = new InformixCount();
																												infor.setDbspace(tablesVO.get("dbspace").toString());
																												infor.setOwner(tablesVO.get("owner").toString());
																												infor.setPages_size(tablesVO.get("pages_size").toString());
																												infor.setPages_used(tablesVO.get("pages_used").toString());
																												infor.setPages_free(tablesVO.get("pages_free").toString());
																												infor.setPercent_free(tablesVO.get("percent_free").toString());
																												infor.setIntcount(p);
																												count.put(tablesVO.get("dbspace").toString(),infor);
																											}else{ 
																												InformixCount infor = new InformixCount();
																												infor = (InformixCount)count.get(tablesVO.get("dbspace"));
																												//System.out.println("---infor----"+infor.getPages_size());
																												//System.out.println("---tablesVO.get(pages_size)----"+tablesVO.get("pages_size"));
																												float pages_size = Float.parseFloat(infor.getPages_size()+"")+Float.parseFloat(tablesVO.get("pages_size")+"");
																												//System.out.println("---pages_size----"+pages_size);
																												float pages_used = Float.parseFloat(infor.getPages_used()+"")+Float.parseFloat(tablesVO.get("pages_used")+"");
																												float pages_free = Float.parseFloat(infor.getPages_free()+"")+Float.parseFloat(tablesVO.get("pages_free")+"");
																												float percent_free = Float.parseFloat(infor.getPercent_free()+"")+Float.parseFloat(tablesVO.get("percent_free")+"");
																												
																												
																												infor.setDbspace(tablesVO.get("dbspace").toString());
																												infor.setOwner(tablesVO.get("owner").toString());
																												infor.setPages_size(pages_size+"");
																												infor.setPages_used(pages_used+"");
																												infor.setPages_free(pages_free+"");
																												infor.setPercent_free(percent_free+"");	
																												infor.setIntcount(infor.getIntcount()+1);
																												count.put(tablesVO.get("dbspace"),infor);
																											}
																						%>
																						<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																							height=28>
																							<td class="application-detail-data-body-list"><%=tablesVO.get("dbspace")%></td>
																							<td class="application-detail-data-body-list"><%=tablesVO.get("owner")%></td>
																							<td class="application-detail-data-body-list"><%=tablesVO.get("fname")%></td>
																							<td class="application-detail-data-body-list"><%=df.format(Float.parseFloat(tablesVO.get("pages_size")+ "")) + "M"%></td>
																							<td class="application-detail-data-body-list"><%=df.format(Float.parseFloat(tablesVO.get("pages_used")+ "")) + "M"%></td>
																							<td class="application-detail-data-body-list"><%=df.format(Float.parseFloat(tablesVO.get("pages_free")+ "")) + "M"%></td>
																							<td class="application-detail-data-body-list"><%=df.format(100 - Float.parseFloat(tablesVO.get("percent_free")+ "")) + "%"%></td>
																						</tr>
																						<%
																							}
																									}
																								}
																						%>
																						
																			<tr align="left" bgcolor="#ECECEC">
																				<td height="28" colspan="7">
																					<b>&nbsp;Informix���ݿ��ܱ�ռ���Ϣ&nbsp;&nbsp;</b>
																				</td>
																			</tr>
																						<tr bgcolor="#ECECEC" height=28>
																							<td width="15%"
																								class="application-detail-data-body-title">
																								<strong>�ռ�����</strong>
																							</td>
																							<td width="15%"
																								class="application-detail-data-body-title">
																								<strong>�ռ�������</strong>
																							</td>
																							<td width="15%"
																								class="application-detail-data-body-title">
																								<strong>�ռ��ܴ�С</strong>
																							</td>
																							<td width="15%"
																								class="application-detail-data-body-title">
																								<strong>��ʹ�õ��ܿռ�</strong>
																							</td>
																							<td width="13%"
																								class="application-detail-data-body-title">
																								<strong>�����ܿռ�</strong>
																							</td>
																							<td width="15%" colspan="2"
																								class="application-detail-data-body-title">
																								<strong>�ռ���ʹ����</strong>
																							</td>
																						</tr>
																						
																						<%
																						System.out.println(count.size());
																						if (count != null) {
																									if (count.size() > 0) {
																										InformixCount infor = null;
																										Iterator it = count.keySet().iterator();
																										while(it.hasNext()){
																											String key = (String)it.next();
																											DecimalFormat df = new DecimalFormat("#.###");
																											System.out.println(key);
																											
																											infor = (InformixCount) count.get(key);
																											float percent_free = Float.parseFloat(infor.getPercent_free())/Float.parseFloat(infor.getIntcount()+"");
																											
																						%>
																						<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																							height=28>
																							<td class="application-detail-data-body-list"><%=infor.getDbspace()%></td>
																							<td class="application-detail-data-body-list"><%=infor.getOwner()%></td>
																							<td class="application-detail-data-body-list"><%=df.format(Float.parseFloat(infor.getPages_size())) + "M"%></td>
																							<td class="application-detail-data-body-list"><%=df.format(Float.parseFloat(infor.getPages_used())) + "M"%></td>
																							<td class="application-detail-data-body-list"><%=df.format(Float.parseFloat(infor.getPages_free())) + "M"%></td>
																							<td class="application-detail-data-body-list" colspan="2"><%=df.format(100 - percent_free) + "%"%></td>
																						</tr>
																						<%
																							}
																									}
																								}
																						%>
																						
																					</table>
																					<br>
																				</td>
																			</tr>
																			<tr align="left" bgcolor="#ECECEC">
																				<td height="28" colspan=2>
																					<b>&nbsp;Informix���ݿ�Ự��Ϣ&nbsp;&nbsp;</b>
																				</td>
																			</tr>
																			<tr bgcolor="#FFFFFF">
																				<td align=center colspan=2>

																					<table width="90%" border="0" cellpadding="3"
																						cellspacing="1" bgcolor="#FFFFFF">
																						<tr bgcolor="#ECECEC" height=28>
																							<td width="15%"
																								class="application-detail-data-body-title">
																								<strong>�û���</strong>
																							</td>
																							<td width="15%"
																								class="application-detail-data-body-title">
																								<strong>����</strong>
																							</td>
																							<td width="15%"
																								class="application-detail-data-body-title">
																								<strong>���д���</strong>
																							</td>
																							<td width="15%"
																								class="application-detail-data-body-title">
																								<strong>��������</strong>
																							</td>
																							<td width="15%"
																								class="application-detail-data-body-title">
																								<strong>˳��ɨ��������ݵĴ���</strong>
																							</td>
																							<td width="13%"
																								class="application-detail-data-body-title">
																								<strong>��������</strong>
																							</td>
																							<td width="15%"
																								class="application-detail-data-body-title">
																								<strong>���ʺ��ڴ�������</strong>
																							</td>
																						</tr>
																						<%
																							if (dbsession != null && dbsession.size() > 0) {
																									for (int i = 0; i < dbsession.size(); i++) {
																										Hashtable tablesVO = (Hashtable) dbsession.get(i);
																										String seqscans = String.valueOf(tablesVO
																												.get("seqscans"));
																										if ("null".equals(seqscans)) {
																											seqscans = "";
																										}
																										String total_sorts = String.valueOf(tablesVO
																												.get("total_sorts"));
																										if ("null".equals(total_sorts)) {
																											total_sorts = "";
																										}
																										String dsksorts = String.valueOf(tablesVO
																												.get("dsksorts"));
																										if ("null".equals(dsksorts)) {
																											dsksorts = "";
																										}
																						%>
																						<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																							height=28>
																							<td class="application-detail-data-body-list"><%=tablesVO.get("username")%></td>
																							<td class="application-detail-data-body-list"><%=tablesVO.get("hostname")%></td>
																							<td class="application-detail-data-body-list"><%=tablesVO.get("access")%></td>
																							<td class="application-detail-data-body-list"><%=tablesVO.get("locksheld")%></td>
																							<td class="application-detail-data-body-list"><%=seqscans%></td>
																							<td class="application-detail-data-body-list"><%=total_sorts%></td>
																							<td class="application-detail-data-body-list"><%=dsksorts%></td>
																						</tr>
																						<%
																							}
																								}
																						%>
																					</table>
																					<br>
																				</td>
																			</tr>

																			<tr align="left" bgcolor="#ECECEC">
																				<td height="23" colspan=2>
																					<b>&nbsp;Informix���ݿ�����Ϣ&nbsp;&nbsp;</b>
																				</td>
																			</tr>
																			<tr bgcolor="#ECECEC">
																				<td align=center colspan=2>
																					<br>

																					<table width="90%" border="0" cellpadding="3"
																						cellspacing="1" bgcolor="#ECECEC">
																						<tr bgcolor="397dbd" height=25>
																							<td width="6%"
																								class="application-detail-data-body-title">
																								<strong>�û���</strong>
																							</td>
																							<td width="12%"
																								class="application-detail-data-body-title">
																								<strong>����</strong>
																							</td>
																							<td width="16%"
																								class="application-detail-data-body-title">
																								<strong>���ݿ�����</strong>
																							</td>
																							<td width="9%"
																								class="application-detail-data-body-title">
																								<strong>������</strong>
																							</td>
																							<td width="12%"
																								class="application-detail-data-body-title">
																								<strong>��������</strong>
																							</td>
																						</tr>
																						<%
																							if (dblock != null && dblock.size() > 0) {
																									for (int i = 0; i < dblock.size(); i++) {
																										Hashtable tablesVO = (Hashtable) dblock.get(i);
																										String type = (String) tablesVO.get("type");
																										String desc = "";
																										if ("B".equals(type)) {
																											desc = "�ֽ���";
																										} else if ("IS".equals(type)) {
																											desc = "��������";
																										} else if ("S".equals(type)) {
																											desc = "������";
																										} else if ("XS".equals(type)) {
																											desc = "�ɿ��ظ��Ķ������еĹ����ֵ";
																										} else if ("U".equals(type)) {
																											desc = "������";
																										} else if ("IX".equals(type)) {
																											desc = "���򻥳���";
																										} else if ("SIX".equals(type)) {
																											desc = "��������򻥳���";
																										} else if ("X".equals(type)) {
																											desc = "������";
																										} else if ("XR".equals(type)) {
																											desc = "�ɿ��ظ��Ķ������еĻ����ֵ";
																										}
																						%>
																						<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																							height=28>
																							<td class="application-detail-data-body-list"><%=tablesVO.get("username")%></td>
																							<td class="application-detail-data-body-list"><%=tablesVO.get("hostname")%></td>
																							<td class="application-detail-data-body-list"><%=tablesVO.get("dbsname")%></td>
																							<td class="application-detail-data-body-list"><%=tablesVO.get("tabname")%></td>
																							<td class="application-detail-data-body-list"><%=desc%></td>
																						</tr>
																						<%
																							}
																								}
																						%>
																					</table>
																					<br>
																				</td>
																			</tr>
																			<!--Informix���ݿ�������������Ϣ(ͨ������127.0.0.1_informix.log���õ�һ��������)  -->
																			<tr align="left" bgcolor="#ECECEC">
																				<td height="23" colspan=2>
																					<b>&nbsp;Informix���ݿ�������������Ϣ&nbsp;&nbsp;</b>
																				</td>
																			</tr>
																			<%
																				String fgWrites = (String)informixOtherData.get("fgWrites");//���ݿ����ܼ��FOREGROUND WRITE���
																				String lokwaits = "";//�ȴ������߳�����
																				String ovbuff = "";//����������
																				String ovlock = "";//���ݿ������
																				String deadlks = "";//������
																				String bufwaits = "";//���ݿ⻺������Buffer�ȴ����
																				String bufreads_cached = "";//�����ڴ��������
																				String bufwrits_cached = "";//�����ڴ�д������
																				
																				if(informixOtherData.containsKey("lokwaits")){
																					lokwaits = (String)informixOtherData.get("lokwaits");
																				}
																				if(informixOtherData.containsKey("ovbuff")){
																					ovbuff = (String)informixOtherData.get("ovbuff");
																				}
																				if(informixOtherData.containsKey("ovlock")){
																					ovlock = (String)informixOtherData.get("ovlock");
																				}
																				if(informixOtherData.containsKey("deadlks")){
																					deadlks = (String)informixOtherData.get("deadlks");
																				}
																				if(informixOtherData.containsKey("bufwaits")){
																					bufwaits = (String)informixOtherData.get("bufwaits");
																				}
																				if(informixOtherData.containsKey("bufreads_cached")){
																					bufreads_cached = (String)informixOtherData.get("bufreads_cached");
																				}
																				if(informixOtherData.containsKey("bufwrits_cached")){
																					bufwrits_cached = (String)informixOtherData.get("bufwrits_cached");
																				}
																			 %>
																			<tr bgcolor="#ECECEC">
																				<td align=center colspan=2>
																					<table width="90%" border="0" cellpadding="3"
																						cellspacing="1" bgcolor="#ECECEC">
																						<tr bgcolor="397dbd" height=25>
																							<td width="12%"
																								class="application-detail-data-body-title">
																								<strong>����</strong>
																							</td>
																							<td width="12%"
																								class="application-detail-data-body-title">
																								<strong>ֵ</strong>
																							</td>
																						</tr>
																						<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																							height=28>
																							<td class="application-detail-data-body-list"  align="left"><pre>�ȴ������߳�����</pre></td>
																							<td class="application-detail-data-body-list" align="center"><pre><%=lokwaits%></pre></td>
																						</tr>
																						<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																							height=28>
																							<td class="application-detail-data-body-list"  align="left"><pre>����������</pre></td>
																							<td class="application-detail-data-body-list" align="center"><pre><%=ovbuff%></pre></td>
																						</tr>
																						<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																							height=28>
																							<td class="application-detail-data-body-list"  align="left"><pre>���ݿ��������</pre></td>
																							<td class="application-detail-data-body-list" align="center"><pre><%=ovlock%></pre></td>
																						</tr>
																						<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																							height=28>
																							<td class="application-detail-data-body-list"  align="left"><pre>������</pre></td>
																							<td class="application-detail-data-body-list" align="center"><pre><%=deadlks%></pre></td>
																						</tr>
																						<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																							height=28>
																							<td class="application-detail-data-body-list"  align="left"><pre>���ݿ⻺������Buffer�ȴ����</pre></td>
																							<td class="application-detail-data-body-list" align="center"><pre><%=bufwaits%></pre></td>
																						</tr>
																						<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																							height=28>
																							<td class="application-detail-data-body-list"  align="left"><pre>�����ڴ��������(%)</pre></td>
																							<td class="application-detail-data-body-list" align="center"><pre><%=bufreads_cached%></pre></td>
																						</tr>
																						<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																							height=28>
																							<td class="application-detail-data-body-list"  align="left"><pre>�����ڴ�д������(%)</pre></td>
																							<td class="application-detail-data-body-list" align="center"><pre><%=bufwrits_cached%></pre></td>
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
					<td valign="top" width="15%">
						<jsp:include page="/include/dbtoolbar.jsp">
							<jsp:param value="<%=myip%>" name="myip" />
							<jsp:param value="<%=myport%>" name="myport" />
							<jsp:param value="<%=myUser%>" name="myUser" />
							<jsp:param value="<%=myPassword%>" name="myPassword" />
							<jsp:param value="<%=mysid%>" name="sid" />
							<jsp:param value="<%=id%>" name="id" />
							<jsp:param value="<%=dbPage%>" name="dbPage" />
							<jsp:param value="<%=dbType%>" name="dbType" />
							<jsp:param value="informix" name="subtype" />
						</jsp:include>
					</td>
				</tr>
			</table>

		</form>
		<%
			} catch (Exception e) {
				e.printStackTrace();
			}
		%>
	</BODY>
</HTML>
