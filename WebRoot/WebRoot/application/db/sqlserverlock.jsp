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
  String dbPage = "sqlserverlock";
  String dbType = "sqlserver";
double avgpingcon = (Double)request.getAttribute("avgpingcon");
int percent1 = Double.valueOf(avgpingcon).intValue();
int percent2 = 100-percent1;

Hashtable max = (Hashtable) request.getAttribute("max");
Hashtable imgurl = (Hashtable)request.getAttribute("imgurl");
String chart1 = (String)request.getAttribute("chart1");
String dbtye = (String)request.getAttribute("dbtye");
String managed = (String)request.getAttribute("managed");
String runstr = (String)request.getAttribute("runstr");
  
 Hashtable sqlsys = (Hashtable)request.getAttribute("sqlsys"); 
 if(sqlsys == null)sqlsys = new Hashtable();
 String HostName = "";
 if(sqlsys.get("MACHINENAME")!=null){
 	HostName = (String)sqlsys.get("MACHINENAME");
  }
 String VERSION = "";
 if(sqlsys.get("VERSION")!=null){
 	VERSION = (String)sqlsys.get("VERSION");
  } 
 String productlevel = "";
 if(sqlsys.get("productlevel")!=null){
 	productlevel = (String)sqlsys.get("productlevel");
  }   
 String ProcessID = "";
 if(sqlsys.get("ProcessID")!=null){
 	ProcessID = (String)sqlsys.get("ProcessID");
  } 
 String IsSingleUser = "";
 if(sqlsys.get("IsSingleUser")!=null){
 	IsSingleUser = (String)sqlsys.get("IsSingleUser");
  } 
  
 String IsIntegratedSecurityOnly = "";
 if(sqlsys.get("IsIntegratedSecurityOnly")!=null){
 	IsIntegratedSecurityOnly = (String)sqlsys.get("IsIntegratedSecurityOnly");
  }
 String IsClustered = "";
 if(sqlsys.get("IsClustered")!=null){
 	IsClustered = (String)sqlsys.get("IsClustered");
  }        
  

Vector lockinfo_v = (Vector)request.getAttribute("lockinfo_v"); 
			Hashtable rsc_type_ht= (Hashtable)request.getAttribute("rsc_type_ht");
			Hashtable req_mode_ht= (Hashtable)request.getAttribute("req_mode_ht");
			Hashtable mode_ht= (Hashtable)request.getAttribute("mode_ht");
			Hashtable req_status_ht= (Hashtable)request.getAttribute("req_status_ht");
			Hashtable req_ownertype_ht= (Hashtable)request.getAttribute("req_ownertype_ht");
			Hashtable sqlValue = (Hashtable)request.getAttribute("sqlValue");
			if(sqlValue == null)sqlValue=new Hashtable();
			Hashtable locksValue = (Hashtable)sqlValue.get("locks");
			Hashtable cachesValue = (Hashtable)sqlValue.get("caches");			  	   
%>
<%
String myBufferCacheHitRatio = (String)request.getAttribute("bufferCacheHitRatio");
	double intMyBufferCacheHitRatio = 0;
	if(myBufferCacheHitRatio!= null && !myBufferCacheHitRatio.equals(""))
		intMyBufferCacheHitRatio = Double.valueOf(myBufferCacheHitRatio);
		
	String planCacheHitRatio = (String)request.getAttribute("planCacheHitRatio");
	double intPlanCacheHitRatio = 0;
	if(planCacheHitRatio!= null && !planCacheHitRatio.equals(""))
		intPlanCacheHitRatio = Double.valueOf(planCacheHitRatio);
		
	String cursorManagerByTypeHitRatio = (String)request.getAttribute("cursorManagerByTypeHitRatio");
	double intCursorManagerByTypeHitRatio = 0;
	if(cursorManagerByTypeHitRatio != null && !cursorManagerByTypeHitRatio.equals(""))
		intCursorManagerByTypeHitRatio = Double.valueOf(cursorManagerByTypeHitRatio);
		
	String catalogMetadataHitRatio = (String)request.getAttribute("catalogMetadataHitRatio");
	double intCatalogMetadataHitRatio = 0;
	if(catalogMetadataHitRatio != null && !catalogMetadataHitRatio.equals(""))
		intCatalogMetadataHitRatio = Double.valueOf(catalogMetadataHitRatio);
		
		
	String picip = CommonUtil.doip(myip);
	
    	//���ɵ���ƽ����ͨ��ͼ��
	CreatePiePicture _cpp = new CreatePiePicture();
	_cpp.createAvgPingPic(picip,avgpingcon); 
		
	
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
	document.getElementById('sqlDetailTitle-4').className='detail-data-title';
	document.getElementById('sqlDetailTitle-4').onmouseover="this.className='detail-data-title'";
	document.getElementById('sqlDetailTitle-4').onmouseout="this.className='detail-data-title'";
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
														<td> 
															<jsp:include page="/topology/includejsp/db_sqlserver.jsp">
																<jsp:param name="dbtye" value="<%=dbtye %>"/>
																<jsp:param name="ipAddress" value="<%=vo.getIpAddress() %>"/> 
																<jsp:param name="managed" value="<%=managed %>"/>
																<jsp:param name="runstr" value="<%=runstr %>"/>
																<jsp:param name="HostName" value="<%=HostName %>"/>
																<jsp:param name="VERSION" value="<%=VERSION %>"/>
																<jsp:param name="productlevel" value="<%=productlevel %>"/>
																<jsp:param name="ProcessID" value="<%=ProcessID %>"/>
																<jsp:param name="IsSingleUser" value="<%=IsSingleUser %>"/>
																<jsp:param name="IsIntegratedSecurityOnly" value="<%=IsIntegratedSecurityOnly %>"/>
																<jsp:param name="IsClustered" value="<%=IsClustered %>"/>
																<jsp:param name="intMyBufferCacheHitRatio" value="<%=intMyBufferCacheHitRatio %>"/>
																<jsp:param name="intPlanCacheHitRatio" value="<%=intPlanCacheHitRatio %>"/>
																<jsp:param name="intCatalogMetadataHitRatio" value="<%=intCatalogMetadataHitRatio %>"/>
																<jsp:param name="intCursorManagerByTypeHitRatio" value="<%=intCursorManagerByTypeHitRatio %>"/> 
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
															<%=sqlDetailTitleTable%>
														</td>
													</tr>
													<tr>
														<td>
															<table class="application-detail-data-body">
																<tr>
																	<td>
																		<% String str1 = "",str2="";
                  if(max.get("avgpingcon")!=null){
                          str2 = (String)max.get("avgpingcon");
                  }
                  %>
																		<table width="100%" border="0" cellpadding="0"
																			cellspacing="1">
																			<tr align="left" bgcolor="#ECECEC">
																				<td height="28" background="images/yemian_16.gif">
																					<b>&nbsp;���ݿ�����Ϣ</b>
																				</td>
																			</tr>
																			<tr bgcolor="DEEBF7">
																				<td>
																					<table width="100%" border="0" cellpadding="3"
																						cellspacing="1" bgcolor="#FFFFFF">
																						<%
                      	if(locksValue != null){
                      %>

																						<tr bgcolor="#FFFFFF">
																							<td class="application-detail-data-body-list"
																								width="20%" height="28">
																								��������
																							</td>
																							<td class="application-detail-data-body-list"
																								width="20%" height="28"><%=locksValue.get("lockRequests")%></td>
																							<td class="application-detail-data-body-list"
																								width="20%" height="28">
																								��������/��
																							</td>
																							<td class="application-detail-data-body-list"
																								width="40%" height="28"><%=locksValue.get("lockRequestsRate")%></td>
																						</tr>
																						<tr bgcolor="#FFFFFF">
																							<td class="application-detail-data-body-list"
																								width="20%" height="28">
																								�����ȴ�
																							</td>
																							<td class="application-detail-data-body-list"
																								width="20%" height="28"><%=locksValue.get("lockWaits")%></td>
																							<td class="application-detail-data-body-list"
																								width="20%" height="28">
																								�����ȴ�/��
																							</td>
																							<td class="application-detail-data-body-list"
																								width="40%" height="28"><%=locksValue.get("lockWaitsRate")%></td>
																						</tr>
																						<tr bgcolor="#FFFFFF">
																							<td class="application-detail-data-body-list"
																								width="20%" height="28">
																								������ʱ
																							</td>
																							<td class="application-detail-data-body-list"
																								width="20%" height="28"><%=locksValue.get("lockTimeouts")%></td>
																							<td class="application-detail-data-body-list"
																								width="20%" height="28">
																								������ʱ/��
																							</td>
																							<td class="application-detail-data-body-list"
																								width="40%" height="28"><%=locksValue.get("lockTimeoutsRate")%></td>
																						</tr>

																						<tr bgcolor="#FFFFFF">
																							<td class="application-detail-data-body-list"
																								width="20%" height="28">
																								������
																							</td>
																							<td class="application-detail-data-body-list"
																								width="20%" height="28"><%=locksValue.get("deadLocks")%></td>
																							<td class="application-detail-data-body-list"
																								width="20%" height="28">
																								������/��
																							</td>
																							<td class="application-detail-data-body-list"
																								width="40%" height="28"><%=locksValue.get("deadLocksRate")%></td>
																						</tr>
																						<tr bgcolor="#FFFFFF">
																							<td class="application-detail-data-body-list"
																								width="20%" height="28">
																								ƽ�������ȴ�ʱ��
																							</td>
																							<td class="application-detail-data-body-list"
																								width="20%" height="28"><%=locksValue.get("avgWaitTime")%></td>
																							<td class="application-detail-data-body-list"
																								width="20%" height="28">
																								ƽ�������ȴ�ʱ�����Ļ���
																							</td>
																							<td class="application-detail-data-body-list"
																								width="40%" height="28"><%=locksValue.get("avgWaitTimeBase")%></td>
																						</tr>
																						<tr bgcolor="#FFFFFF">
																							<td class="application-detail-data-body-list"
																								width="20%" height="28">
																								�����ȴ�
																							</td>
																							<td class="application-detail-data-body-list"
																								width="20%" height="28"><%=locksValue.get("latchWaits")%></td>
																							<td class="application-detail-data-body-list"
																								width="20%" height="28">
																								�����ȴ�/��
																							</td>
																							<td class="application-detail-data-body-list"
																								width="40%" height="28"><%=locksValue.get("latchWaitsRate")%></td>
																						</tr>
																						<tr bgcolor="#FFFFFF">
																							<td class="application-detail-data-body-list"
																								width="20%" height="28">
																								ƽ�������ȴ�ʱ��
																							</td>
																							<td class="application-detail-data-body-list"
																								width="20%" colspan=3 height="28"><%=locksValue.get("avgLatchWait")%></td>
																						</tr>
																						<%
                      }
                      %>

																					</table>
																				</td>
																			</tr>
																		</table>

																		<table width="100%" border="0" cellspacing="0"
																			cellpadding="0">
																			<tr>
																				<td>
																					<table width="96%" align="center" border=0
																						cellspacing="0" cellpadding="0">
																						<tr height="28" bgcolor="#ECECEC">
																							<td align=center>
																								&nbsp;
																							</td>
																							<td align=center>
																								����Դ
																							</td>
																							<td align=center>
																								���ݿ�
																							</td>
																							<td align=center>
																								��Դ����
																							</td>
																							<td align=center
																								title="�����������ߵ���ģʽ�����Ҵ�������Ȩģʽ�������ת����ȴ�ģʽ��">
																								������ģʽ
																							</td>
																							<td align=center>
																								������״̬
																							</td>
																							<td align=center
																								title="����ÿ���������ĳ����Դ�ϵ���ʱ�����ü��������ӡ�ֱ�����ü������� 0 ʱ�����ͷ�����">
																								�����ô���
																							</td>
																							<td align=center>
																								����������
																							</td>
																							<td align=center>
																								����ID
																							</td>
																							<td align=center>
																								��������
																							</td>
																						</tr>
																						<%
			if(lockinfo_v != null && lockinfo_v.size()>0){
				for(int i=0;i<lockinfo_v.size();i++){
					Hashtable ht = (Hashtable)lockinfo_v.get(i);
					String rsc_text = ht.get("rsc_text").toString().trim();
					//System.out.println("rsc_text:"+rsc_text);
					String dbname = ht.get("dbname").toString();
					String rsc_type = rsc_type_ht.get(ht.get("rsc_type").toString()).toString();
					String req_mode = req_mode_ht.get(ht.get("req_mode").toString()).toString();
					//System.out.println("req_mode:"+req_mode);
					String req_status = req_status_ht.get(ht.get("req_status").toString()).toString();
					//System.out.println("req_status:"+req_status);
					String req_refcnt = ht.get("req_refcnt").toString();
					//System.out.println("req_refcnt:"+req_refcnt);
					
					String req_lifetime = ht.get("req_lifetime").toString();
					//System.out.println("req_lifetime:"+req_lifetime);
					String req_spid = ht.get("req_spid").toString();
					//System.out.println("req_spid:"+req_spid);
					String req_ownertype = req_ownertype_ht.get(ht.get("req_ownertype").toString()).toString();
					//System.out.println("req_ownertype:"+req_ownertype);
					String title = mode_ht.get(ht.get("req_mode").toString()).toString();
			%>
																						<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																							height="28">
																							<td class="application-detail-data-body-list"><%=i+1%></td>
																							<td class="application-detail-data-body-list"
																								align=center>
																								&nbsp;<%=rsc_text%></td>
																							<td class="application-detail-data-body-list"
																								align=center>
																								&nbsp;<%=dbname%></td>
																							<td class="application-detail-data-body-list"
																								align=center>
																								&nbsp;<%=rsc_type%></td>
																							<td class="application-detail-data-body-list"
																								align=center title="<%=title%>">
																								&nbsp;<%=req_mode%></td>
																							<td class="application-detail-data-body-list"
																								align=center>
																								&nbsp;<%=req_status%></td>
																							<td class="application-detail-data-body-list"
																								align=center>
																								&nbsp;<%=req_refcnt%></td>
																							<td class="application-detail-data-body-list"
																								align=center>
																								&nbsp;<%=req_lifetime%></td>
																							<td class="application-detail-data-body-list"
																								align=center>
																								&nbsp;<%=req_spid%></td>
																							<td class="application-detail-data-body-list"
																								align=center>
																								&nbsp;<%=req_ownertype%></td>
																						</tr>
																						<%}
          }%>
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
																<jsp:param value="sqlserver" name="subtype" />
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
  mainForm.action = "<%=rootPath%>/sqlserver.do?action=sychronizeData&dbPage=<%=dbPage %>&id=<%=id %>&myip=<%=myip %>&myport=<%=myport %>&myUser=<%=myUser %>&myPassword=<%=myPassword %>&sid=<%=mysid %>&flag=1";
  mainForm.submit();
 });    
});
</script>
	</BODY>
</HTML>