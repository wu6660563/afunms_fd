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
  String dbPage = "sqlserverping";
  String dbType = "sqlserver";
double avgpingcon = (Double)request.getAttribute("avgpingcon");
int percent1 = Double.valueOf(avgpingcon).intValue();
int percent2 = 100-percent1;		
		

String[] sysItem3={"HOST_NAME","DBNAME","VERSION","INSTANCE_NAME","STATUS","STARTUP_TIME","ARCHIVER"};
String[] sysItemch3={"��������","DB ����","DB �汾","������","����״̬","���̿�ʼʱ��","�鵵��־ģʽ"};
String[] dbItems={"usedperc","usedsize","size","dbname"};

  
String[] memoryItem={"AllSize","UsedSize","Utilization"};
String[] memoryItemch={"������","��������","��ǰ������","���������"};
String[] sysItem={"sysName","sysUpTime","sysContact","sysLocation","sysServices","sysDescr"};
String[] sysItemch={"�豸��","�豸����ʱ��","�豸��ϵ","�豸λ��","�豸����","�豸����"};
//Hashtable max = (Hashtable) request.getAttribute("max");
//Hashtable imgurl = (Hashtable)request.getAttribute("imgurl");
//String chart1 = (String)request.getAttribute("chart1");
String dbtye = (String)request.getAttribute("dbtye");
String managed = (String)request.getAttribute("managed");
String runstr = (String)request.getAttribute("runstr");
  
 Hashtable sqlsys = (Hashtable)request.getAttribute("sqlsys");
 Hashtable dataValue = (Hashtable)request.getAttribute("dbValue");
 
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
	
    			  	   
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
	<head>

<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>

<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8"/>


<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />

<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>

<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
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
	go();
	setClass();
}

function setClass(){
	document.getElementById('sqlDetailTitle-0').className='detail-data-title';
	document.getElementById('sqlDetailTitle-0').onmouseover="this.className='detail-data-title'";
	document.getElementById('sqlDetailTitle-0').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		document.getElementById("id").value="<%=vo.getId()%>";
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}
</script>
<script type="text/javascript">
        var xmlHttp;
        var bar_color = 'skyblue';      /*��������ɫ*/
        var span_id = "block";
        var clear = "&nbsp;&nbsp;&nbsp;"

        function createXMLHttpRequest() {
            if (window.ActiveXObject) {
                xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
            } 
            else if (window.XMLHttpRequest) {
                xmlHttp = new XMLHttpRequest();                
            }
        }

        function go() {     /*����ProgresBarJsp.jsp*/
            createXMLHttpRequest();
            checkDiv();
            var url = "<%=rootPath%>/application/db/ProgressBarJsp.jsp?task=create";
            var button = document.getElementById("go");
            button.disabled = true;
            xmlHttp.open("GET", url, true);
            xmlHttp.onreadystatechange = goCallback;     //������Ӧ�󣬵��õ�goCallback����
            xmlHttp.send(null);
        }

        function goCallback() {     
            if (xmlHttp.readyState == 4) {
                if (xmlHttp.status == 200) {
                    setTimeout("pollServer()", 500);
                }
            }
        }
        
        function pollServer() {
            createXMLHttpRequest();
            var url = "<%=rootPath%>/application/db/ProgressBarJsp.jsp?task=poll";
            xmlHttp.open("GET", url, true);
            xmlHttp.onreadystatechange = pollCallback;
            xmlHttp.send(null);
        }
        
        function pollCallback() {
            if (xmlHttp.readyState == 4) {
                if (xmlHttp.status == 200) {
                    var percent_complete = xmlHttp.responseXML.getElementsByTagName("percent")[0].firstChild.data;                 
                    var index = processResult(percent_complete);                  
                    for (var i = 1; i <= index; i++) {
                        var elem = document.getElementById("block" + i);
                        if(i != 5){                        
                        elem.innerHTML = clear;
                        }
                        elem.style.backgroundColor = bar_color;
                        
                        //var next_cell = i + 1;
                        //if (next_cell > index && next_cell <= 9) {
                        //    document.getElementById("block" + next_cell).innerHTML = percent_complete + "%";
                        //}
                    }                    
                    if (index < 9) {
                        setTimeout("pollServer()", 500);   //ÿ������ִ��һ��pollServer����������ִֻ��һ��
                    } else {
                        setTimeout("go()", 500);            //��1����������һ��go�������ﵽѭ����ʾ��Ŀ��
                        //document.getElementById("complete").innerHTML = "Complete!";
                        //document.getElementById("go").disabled = false;
                    }
                }
            }
        }
        
        function processResult(percent_complete) {
            var ind;
            if (percent_complete.length == 2) {
                ind = percent_complete.substring(0, 1);   /*ȡ��λ���ĵ�һλ��*/
            } else {
                ind = 9;
            }
            return ind;
        }

        function checkDiv() {
            var progress_bar = document.getElementById("progressBar");
            //var rightbar = document.getElementById("rightBar");           
            if (progress_bar.style.visibility == "visible") {
                clearBar();
                document.getElementById("complete").innerHTML = "";
            } else {
                progress_bar.style.visibility = "visible";
                //rightbar.style.visibility = "visible";               
                //document.getElementById("rightb").style.backgroundColor = "#ECECEC";              
            }
        }
        
        function clearBar() {
            for (var i = 1; i < 10; i++) {
                var elem = document.getElementById("block" + i);
                elem.innerHTML = clear;
                elem.style.backgroundColor = "#ececec";                
                if(i == 5)
                {
                   elem.innerHTML = "<%=intMyBufferCacheHitRatio%>%";
                }
            }
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
				<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif" width="32" height="32" style="margin-right: 8px;" align="middle" />
				Loading...
			</div>
		</div>
		<div id="loading-mask" style=""></div>
		<form id="mainForm" method="post" name="mainForm">
			<input type=hidden name="id">

			<table id="body-container" class="body-container">
				<tr>
					<td class="td-container-menu-bar" width=14%>
						<table id="container-menu-bar" class="container-menu-bar">
							<tr>
								<td>
									<%=menuTable%>
								</td>
							</tr>
						</table>
					</td>
					<td class="td-container-main" width=76% align=center>
						<table id="container-main" class="container-main">
							<tr>
								<td class="td-container-main-application-detail">
									<table id="container-main-application-detail" class="container-main-application-detail">
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
												<table id="application-detail-data" class="application-detail-data">
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
																		<table width="100%" cellpadding="0" cellspacing="0">
																			<tr>
																				<td align=center>
																					<br>
																					<table cellpadding="0" cellspacing="0" width=98%>
																						<tr>
																							<td width="100%">
																								<div id="flashcontent1">
																									<strong>You need to upgrade your Flash
																										Player</strong>
																								</div>
																								<script type="text/javascript">
																									var so = new SWFObject("<%=rootPath%>/flex/Oracle_Ping.swf?ipadress=<%=vo.getIpAddress()%>&category=SQLPing", "Oracle_Ping", "346", "250", "8", "#ffffff");
																									so.write("flashcontent1");
																								</script>
																							</td>
																						</tr>
																					</table>
																				</td>
																				<td>
																					&nbsp;
																				</td>
																			</tr>
																			<tr>
																			<td align=center valign=top style="width:53%">
																    	    <br>
                   															<table style="width:95%" border=1 bordercolor= "#7599d7" style="border:1px solid #7599d7;border-collapse:collapse;">
																			<tr>
															                		<td width=95%>
																				<table width="95%">
														                					<tr>
																	                			<td align="left" height=30 bgcolor="#ECECEC">&nbsp;<B>��ռ�ʹ����</B></td>											   
																					</tr>
															       					</table>
														       					</td>
														       				</tr>
																			<tr>
																				<td align=center>
																					<br>
																					<table cellpadding="0" cellspacing="0" width=98%>
																						<tr>
																						<%
																						           
                                                                                                             String[] usedperc=null;
																							                  String[] usedsize=null;
																							                  String[] size=null;
																							                  String[] dbname=null;
																						                      String dataStr="";
																						                       Hashtable    database=null;
																						                      if(dataValue!=null&&dataValue.size()>0)
																						                     database=(Hashtable)dataValue.get("database");
				                   															                if (database != null && database.size() > 0) {
				                   															                 int i=0;
				                   															                 usedperc=new String[database.size()];
																													usedsize=new String[database.size()];
																													size=new String[database.size()];
																													dbname=new String[database.size()];
                                                                                                          for(Iterator itr = database.keySet().iterator(); itr.hasNext();){ 
                                                                                                             String key = (String) itr.next(); 
                                                                                                             Hashtable tablespace = (Hashtable) database.get(key); 
                                                                                                              if(tablespace!= null && tablespace.size() > 0){
                                                                                                               
																													usedperc[i]=(String)tablespace.get(dbItems[0]);
																												    usedsize[i]=(String)tablespace.get(dbItems[1]);
																												    size[i]=(String)tablespace.get(dbItems[2]);
																												    dbname[i]=(String)tablespace.get(dbItems[3]);
																												     i++;
																																 }
																											        }
																											        }
																											         StringBuffer sb=new StringBuffer();
																						                      
																						                        String[] colorStr=new String[] {"#33FF33","#FF0033","#9900FF","#FFFF00","#33CCFF","#003366","#33FF33","#FF0033","#9900FF","#FFFF00","#333399","#0000FF","#A52A2A","#800080"};
																						                        sb.append("<chart><series>");
																						                        if(dbname!=null){
																						                        for(int k=0;k<dbname.length;k++){
																													sb.append("<value xid='").append(k).append("'>").append(dbname[k]).append("</value>");
																													
																													}
																													}
																													sb.append("</series><graphs><graph gid='0'>");
																													if(usedperc!=null){
																													for(int i=0,j=0;i<usedperc.length;i++,j++){
																													if(i/colorStr.length==1)j=0;
																													int perInt=Math.round(Float.parseFloat(usedperc[i]));
																													sb.append("<value xid='").append(i).append("' color='").append(colorStr[j]).append("'>"+perInt).append("</value>");
																													}
																													}
																													sb.append("</graph></graphs></chart>");
																													dataStr=sb.toString();

																						 %>
																							<td width="100%">
																							<!-- 
																								<div id="flashcontent3">
																									<strong>You need to upgrade your Flash
																										Player</strong>
																								</div>
																								<script type="text/javascript">
													                                                    var so = new SWFObject("<%=rootPath%>/flex/Column_SqlDb.swf?ip=<%=vo.getIpAddress()%>&user=<%=vo.getUser()%>&pwd=<%=vo.getPassword()%>", "Column_SqlDb", "820", "390", "8", "#ffffff");
													                                                       so.write("flashcontent3");
												                                              </script>
												                                              -->
												                                              <div id="disksolaris">
							                                                                   <strong>You need to upgrade your Flash Player</strong>
						                                                                     </div>
						                                                                   <script type="text/javascript"
							                                                                    src="<%=rootPath%>/include/swfobject.js"></script>
						                                                                   <script type="text/javascript">
						                                                                         var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "ampie","390", "388", "8", "#FFFFFF");
						                                                                             so.addVariable("path", "<%=rootPath%>/amchart/");
						                                                                             so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/dbpercent_settings.xml"));
						                                                                             so.addVariable("chart_data","<%=dataStr%>");
						                                                                             so.write("disksolaris");
						                                                                  </script>	
																							</td>
																						</tr>
																						
																					</table>
																				</td>
																				
				                  												        			
																				   	
																				</tr>
																				</table>
																				</td>
																				<td align=center width=47% valign=top>
																				<br>
																                	        			<table cellpadding="0" cellspacing="0"border="1" >
												              							        			<tr> 
												                							        			<td align=center width=95%> 
												                								        			
																	                                    					<table border="1" align=center>
																											<tr>
																												<td class="detail-data-body-title" style="height:31">���ݿ�����</td>
																												<td class="detail-data-body-title" style="height:31">�ܴ�С</td>
																												<td class="detail-data-body-title" style="height:31">���ô�С</td>
																												<td class="detail-data-body-title" style="height:31">ʹ����</td>
																											</tr>
																														
																													           
																																	<%
																																	if (database != null && database.size() > 0) {
																																	for(int i=0;i<database.size();i++)
																																	{
																																	
																																	%>
																																	 <tr>
																														            <td class="detail-data-body-list" height="27">&nbsp;&nbsp;<%=dbname[i] %></td>
																																	
																														            <td class="detail-data-body-list" height="27" align=center><%=size[i] %>MB</td>
																														            <td class="detail-data-body-list" height="27" align=center><%=usedsize[i] %>MB</td>
																														            <td class="detail-data-body-list" height="27" align=center><%=usedperc[i] %>%</td>
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
																				<tr>
																			<td align=center valign=top style="width:53%">
																    	    <br>
                   															<table style="width:95%" border=1 bordercolor= "#7599d7" style="border:1px solid #7599d7;border-collapse:collapse;">
																			<tr>
															                		<td width=95%>
																				<table width="95%">
														                					<tr>
																	                			<td align="left" height=30 bgcolor="#ECECEC">&nbsp;<B>������־�ռ�ʹ����</B></td>											   
																					</tr>
															       					</table>
														       					</td>
														       				</tr>
																			<tr>
																				<td align=center>
																					<br>
																					<table cellpadding="0" cellspacing="0" width=98%>
																						<tr>
																						<%
																						           
                                                                                                           
																							                  String[] logname=null;
																						                      String logStr="";
																						                      Hashtable    logfile=null;
																						                      if(dataValue!=null&&dataValue.size()>0)
																						                     logfile=(Hashtable)dataValue.get("logfile");
				                   															                if (logfile != null && logfile.size() > 0) {
				                   															                 int i=0;
				                   															                 usedperc=new String[logfile.size()];
																													usedsize=new String[logfile.size()];
																													size=new String[logfile.size()];
																													logname=new String[logfile.size()];
                                                                                                          for(Iterator itr = logfile.keySet().iterator(); itr.hasNext();){ 
                                                                                                             String key = (String) itr.next(); 
                                                                                                             Hashtable tablespace = (Hashtable) logfile.get(key); 
                                                                                                              if(tablespace!= null && tablespace.size() > 0){
                                                                                                               
																													usedperc[i]=(String)tablespace.get(dbItems[0]);
																												    usedsize[i]=(String)tablespace.get(dbItems[1]);
																												    size[i]=(String)tablespace.get(dbItems[2]);
																												    logname[i]=(String)tablespace.get("logname");
																												     i++;
																																 }
																											        }
																											        }
																											         StringBuffer logBuffer=new StringBuffer();
																						                      
																						                        logBuffer.append("<chart><series>");
																						                        if(dbname!=null){
																						                        for(int k=0;k<logname.length;k++){
																													logBuffer.append("<value xid='").append(k).append("'>").append(logname[k]).append("</value>");
																													
																													}
																													}
																													logBuffer.append("</series><graphs><graph gid='0'>");
																													if(usedperc!=null){
																													for(int i=0,j=0;i<usedperc.length;i++,j++){
																													if(i/colorStr.length==1)j=0;
																													int perInt=Math.round(Float.parseFloat(usedperc[i]));
																													logBuffer.append("<value xid='").append(i).append("' color='").append(colorStr[j]).append("'>"+perInt).append("</value>");
																													}
																													}
																													logBuffer.append("</graph></graphs></chart>");
																													logStr=logBuffer.toString();

																						 %>
																							<td width="100%">
																							<!-- 
																								<div id="flashcontent3">
																									<strong>You need to upgrade your Flash
																										Player</strong>
																								</div>
																								<script type="text/javascript">
													                                                    var so = new SWFObject("<%=rootPath%>/flex/Column_SqlDb.swf?ip=<%=vo.getIpAddress()%>&user=<%=vo.getUser()%>&pwd=<%=vo.getPassword()%>", "Column_SqlDb", "820", "390", "8", "#ffffff");
													                                                       so.write("flashcontent3");
												                                              </script>
												                                              -->
												                                              <div id="sqlserverlog">
							                                                                   <strong>You need to upgrade your Flash Player</strong>
						                                                                     </div>
						                                                                   <script type="text/javascript"
							                                                                    src="<%=rootPath%>/include/swfobject.js"></script>
						                                                                   <script type="text/javascript">
						                                                                         var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "ampie","390", "388", "8", "#FFFFFF");
						                                                                             so.addVariable("path", "<%=rootPath%>/amchart/");
						                                                                             so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/dbpercent_settings.xml"));
						                                                                             so.addVariable("chart_data","<%=logStr%>");
						                                                                             so.write("sqlserverlog");
						                                                                  </script>	
																							</td>
																						</tr>
																						
																					</table>
																				</td>
																				</tr>
																				</table>
																				</td>
				                  												        			<td align=center width=47% valign=top>
				                  												        			<br>
																                	        			<table cellpadding="0" cellspacing="0"border="1" >
												              							        			<tr> 
												                							        			<td align=center width=95%> 
												                								        			
																	                                    					<table border="1" align=center>
																											<tr>
																												<td class="detail-data-body-title" style="height:31">���ݿ�����</td>
																												<td class="detail-data-body-title" style="height:31">�ܴ�С</td>
																												<td class="detail-data-body-title" style="height:31">���ô�С</td>
																												<td class="detail-data-body-title" style="height:31">ʹ����</td>
																											</tr>
																														
																													           
																																	<%
																																	 if (logfile != null && logfile.size() > 0) {
																																	for(int i=0;i<logfile.size();i++)
																																	{
																																	
																																	%>
																																	 <tr>
																														            <td class="detail-data-body-list" height="27">&nbsp;&nbsp;<%=logname[i] %></td>
																																	
																														            <td class="detail-data-body-list" height="27" align=center><%=size[i] %>MB</td>
																														            <td class="detail-data-body-list" height="27" align=center><%=usedsize[i] %>MB</td>
																														            <td class="detail-data-body-list" height="27" align=center><%=usedperc[i] %>%</td>
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
																			 <!-- 	<tr>
																				<td>
																					<br>
																					<table cellpadding="0" cellspacing="0" width=98%>
																						<tr>
																							<td width="100%">
																								<div id="flashcontent4">
																									<strong>You need to upgrade your Flash
																										Player</strong>
																								</div>
																								<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Column_SqlDb_Log.swf?ip=<%=vo.getIpAddress()%>&user=<%=vo.getUser()%>&pwd=<%=vo.getPassword()%>", "Column_SqlDb_Log", "820", "390", "8", "#ffffff");
													so.write("flashcontent4");
												</script>
																							</td>
																						</tr>
																					</table>
																				</td>
																			</tr> -->
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
					<td width=12% valign=top align=left>
															<jsp:include page="/include/dbtoolbar.jsp">
																<jsp:param value="<%=myip %>" name="myip" />
																<jsp:param value="<%=myport %>" name="myport" />
																<jsp:param value="<%=myUser %>" name="myUser" />
																<jsp:param value="<%=myPassword %>" name="myPassword" />
																<jsp:param value="<%=mysid  %>" name="sid" />
																<jsp:param value="<%=id %>" name="id" />
																<jsp:param value="<%=dbPage %>" name="dbPage" />
																<jsp:param value="<%=dbType %>" name="dbType" />
																<jsp:param value="SQLServer" name="subtype" />
															</jsp:include><!-- HONGLI MODIFY -->
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