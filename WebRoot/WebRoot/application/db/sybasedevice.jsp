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
<%
  String rootPath = request.getContextPath();;
  DBVo vo  = (DBVo)request.getAttribute("db");	
  String id = (String)request.getAttribute("id");
  String myip = vo.getIpAddress();
  String myport = vo.getPort();
  String myUser = vo.getUser();
  String myPassword = EncryptUtil.decode(vo.getPassword());
  String mysid = "";
  String dbPage = "sybasedevice";
  String dbType = "sybase";

Hashtable max = (Hashtable) request.getAttribute("max");
Hashtable imgurl = (Hashtable)request.getAttribute("imgurl");
String chart1 = (String)request.getAttribute("chart1");
String dbtye = (String)request.getAttribute("dbtye");
String managed = (String)request.getAttribute("managed");
String runstr = (String)request.getAttribute("runstr");

SybaseVO sysbaseVO = (SybaseVO)request.getAttribute("sysbaseVO");
if (sysbaseVO == null)sysbaseVO = new SybaseVO();     
double avgpingcon = (Double)request.getAttribute("avgpingcon");
int percent1 = Double.valueOf(avgpingcon).intValue();
int percent2 = 100-percent1;

StringBuffer dataStr2 = new StringBuffer();
	dataStr2.append("��ͨ;").append(Math.round(avgpingcon)).append(
			";false;7CFC00\\n");
	dataStr2.append("δ��ͨ;").append(100 - Math.round(avgpingcon))
			.append(";false;FF0000\\n");
	String avgdata = dataStr2.toString();			  	   
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
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
			url:"<%=rootPath%>/sybaseAjaxManager.ajax?action=ajaxUpdate_availability&id=<%=id%>&nowtime="+(new Date()),
			success:function(data){
				//$("#flashcontent00gzm").html(data.percent1+":"+data.percent2+":"+data.cpuper);
				var so = new SWFObject("<%=rootPath%>/flex/Pie_Component.swf?percent1="+data.percent1+"&percentStr1=����&percent2="+data.percent2+"&percentStr2=������", "Pie_Component", "160", "160", "8", "#ffffff");
				so.write("flashcontent00");
				
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
	document.getElementById('sybDetailTitle-2').className='detail-data-title';
	document.getElementById('sybDetailTitle-2').onmouseover="this.className='detail-data-title'";
	document.getElementById('sybDetailTitle-2').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		document.getElementById("id").value="<%=vo.getId()%>";
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}
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
															<jsp:include page="/topology/includejsp/db_sysbase.jsp">
																<jsp:param name="dbtye" value="<%= dbtye%>"/> 
																<jsp:param name="IpAddress" value="<%= vo.getIpAddress()%>"/> 
																<jsp:param name="managed" value="<%= managed%>"/> 
																<jsp:param name="runstr" value="<%= runstr%>"/> 
																<jsp:param name="ServerName" value="<%= sysbaseVO.getServerName()%>"/> 
																<jsp:param name="Version" value="<%= sysbaseVO.getVersion()%>"/> 
																<jsp:param name="avgdata" value="<%= avgdata%>"/> 
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
															<%=sybDetailTitleTable%>
														</td>
													</tr>
													<tr>
														<td>
															<table class="application-detail-data-body">
																<tr>
																	<td>
																		<table width="100%" border="0" cellpadding="0"
																			cellspacing="1">
																			<tr align="left" bgcolor="#ECECEC">
																				<td height="28">
																					<b>&nbsp;Sysbase ���ݿ��豸��Ϣ</b>
																				</td>
																			</tr>

																			<tr bgcolor="#FFFFFF">
																				<td align=center>

																					<table width="80%" border="0" cellpadding="3"
																						cellspacing="1" bgcolor="#ECECEC">
																						<tr bgcolor="#ECECEC" height=28>
																							<td width="25%"
																								class="application-detail-data-body-title">
																								<strong>�豸����</strong>
																							</td>
																							<td width="25%"
																								class="application-detail-data-body-title">
																								<strong>�豸��������</strong>
																							</td>
																							<td width="50%"
																								class="application-detail-data-body-title">
																								<strong>�豸����</strong>
																							</td>
																						</tr>
																						<%
                        List devicelist = (List)sysbaseVO.getDeviceInfo();
                        if (devicelist != null && devicelist.size()>0){
                        	for(int k=0;k<devicelist.size();k++){
                        		TablesVO tVO = (TablesVO)devicelist.get(k);
                       %>
																						<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																							height=28>
																							<td class="application-detail-data-body-list"><%=tVO.getDevice_name()%></td>
																							<td class="application-detail-data-body-list"><%=tVO.getDevice_physical_name()%></td>
																							<td class="application-detail-data-body-list"><%=tVO.getDevice_description()%></td>
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
					<td valign=top width=15%>
															<jsp:include page="/include/dbtoolbar.jsp">
																<jsp:param value="<%=myip %>" name="myip" />
																<jsp:param value="<%=myport %>" name="myport" />
																<jsp:param value="<%=myUser %>" name="myUser" />
																<jsp:param value="<%=myPassword %>" name="myPassword" />
																<jsp:param value="<%=mysid  %>" name="sid" />
																<jsp:param value="<%=id %>" name="id" />
																<jsp:param value="<%=dbPage %>" name="dbPage" />
																<jsp:param value="<%=dbType %>" name="dbType" />
																<jsp:param value="sybase" name="subtype"/>
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
  mainForm.action = "<%=rootPath%>/sybase.do?action=sychronizeData&dbPage=<%=dbPage %>&id=<%=id %>&myip=<%=myip %>&myport=<%=myport %>&myUser=<%=myUser %>&myPassword=<%=myPassword %>&sid=<%=mysid %>&flag=1";
  mainForm.submit();
 });    
});
</script>
	</BODY>
</HTML>