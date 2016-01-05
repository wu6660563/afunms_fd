<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

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
<%@page import="com.afunms.config.dao.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%@page import="com.afunms.application.manage.WeblogicManager"%>
<%@page import="com.afunms.application.dao.WeblogicConfigDao"%>
<%@page import="com.afunms.application.model.WeblogicConfig"%>
<%@page import="com.afunms.application.weblogicmonitor.*"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>

<%
	 String rootPath = request.getContextPath();
	 String menuTable = (String)request.getAttribute("menuTable");
	 Hashtable hash = (Hashtable)request.getAttribute("hash");
	 String id= (String)request.getAttribute("id");
	 String chart1=null;
	 double weblogicping=0;
	 Hashtable pollingtime_ht=new Hashtable();
	 String lasttime;
	 String nexttime;

	 WeblogicManager wm=new WeblogicManager();
	 WeblogicConfigDao weblogicconfigdao=new WeblogicConfigDao();
     WeblogicConfig weblogicconf=(WeblogicConfig) weblogicconfigdao.findByID(id);
	
	 pollingtime_ht=wm.getCollecttime(weblogicconf.getIpAddress());

	 if(pollingtime_ht!=null){
	 lasttime=(String)pollingtime_ht.get("lasttime");
	 nexttime=(String)pollingtime_ht.get("nexttime");
	 
	 }else{
	 lasttime=null;
	 nexttime=null;	 
	 }

//System.out.println("&&&&&&&&&&&&&&&&&&&&&&lasttime====================="+lasttime+"===================");
//System.out.println("&&&&&&&&&&&&&&&&&&&&&&nexttime====================="+nexttime+"===================");

		weblogicping=(double)wm.weblogicping(weblogicconf.getId());
		int percent1 = Double.valueOf(weblogicping).intValue();
		int percent2 = 100-percent1;
	
		DefaultPieDataset dpd = new DefaultPieDataset();
		dpd.setValue("可用时间",weblogicping);
		dpd.setValue("不可用时间",100 - weblogicping);
		chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
	
		WeblogicNormal normalvalue=null;
		List normaldatalist =new ArrayList();
		List serverdatalist =new ArrayList();
if(hash!=null){
	 normaldatalist =(List) hash.get("normalValue");
     serverdatalist = (List)hash.get("serverValue");
 if(normaldatalist!=null&&normaldatalist.size()>0){
	 normalvalue = (WeblogicNormal)normaldatalist.get(0);
	}else{
	 normalvalue = new WeblogicNormal();
	}
	 
    
}else{
}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>


<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="gb2312" />
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="gb2312"/>
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 

<script language="javascript">	

  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
 Ext.get("process").on("click",function(){
  
  Ext.MessageBox.wait('数据加载中，请稍后.. ');   
  //location.href="EventqueryMgr.do";
        //msg.style.display="block";
        mainForm.action = "<%=rootPath%>/cfgfile.do?action=getcfgfile&ipaddress=<%=weblogicconf.getIpAddress()%>";
        mainForm.submit();
 });	
	
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
  
// 全屏观看
function gotoFullScreen() {
	parent.mainFrame.resetProcDlg();
	var status = "toolbar=no,height="+ window.screen.height + ",";
	status += "width=" + (window.screen.width-8) + ",scrollbars=no";
	status += "screenX=0,screenY=0";
	window.open("topology/network/index.jsp", "fullScreenWindow", status);
	parent.mainFrame.zoomProcDlg("out");
}
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
	document.getElementById('weblogicDetailTitle-4').className='detail-data-title';
	document.getElementById('weblogicDetailTitle-4').onmouseover="this.className='detail-data-title'";
	document.getElementById('weblogicDetailTitle-4').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		document.getElementById("id").value="<%=weblogicconf.getId()%>";
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}

</script>

</head>
<body id="body" class="body" onload="initmenu();">
<form method="post" name="mainForm">
<input type=hidden name="orderflag">
<input type=hidden name="id">
<div id="loading">
	<div class="loading-indicator">
	<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif" width="32" height="32" style="margin-right: 8px;" align="middle" />Loading...</div>
</div>
<div id="loading-mask" style=""></div>
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=960>
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
								<table id="container-main-application-detail" class="container-main-application-detail">
									<tr>
										<td>
											<table id="application-detail-content" class="application-detail-content">
												<tr>
													<td>
														<table id="application-detail-content-header" class="application-detail-content-header">
										                	<tr>
											                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
											                	<td class="application-detail-content-title">WEBLOGIC详细信息</td>
											                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
											       			</tr>
											        	</table>
				        							</td>
				        						</tr>
				        						<tr>
				        							<td>
				        								<table id="application-detail-content-body" class="application-detail-content-body">
				        									<tr>
				        										<td>
				        										
				        											<table>
										               					<tr>
										     								<td width="70%" align="left" valign="top">
			        															<table>
                    										<tr>
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;IP地址:</td>
                      											<td width="35%"><%=weblogicconf.getIpAddress()%> </td>
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;监控端口:</td>
                      											<td width="35%"><%=weblogicconf.getPortnum()%> </td>                    										
                    										</tr>
                    										<tr bgcolor="#F1F1F1">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;管理域名:</td>
                      											<td width="35%"><%=normalvalue.getDomainName()%> </td>
                    											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;管理域状态:</td>
																<%if("1".equals(normalvalue.getDomainActive())){%>
																<td width="35%">活动
																</td>
																<%}else {%>
																<td width="35%">不活动
																</td>
																<%}%>
                    										</tr>   
                    											<tr>
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;管理端口:</td>
                      											<td width="35%"><%=normalvalue.getDomainAdministrationPort()%> </td>
                    											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;管理状态:</td>
																<%if(weblogicconf.getMon_flag()==1){%>
																<td width="35%">管理中
																</td>
																<%}else {%>
																<td width="35%">未管理
																</td>
																<%}%>
                      											<td width="35%"> </td>
                    										</tr>    
                    										<tr bgcolor="#F1F1F1">
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;版本:</td>
                      											<td width="85%" colspan=3><%=normalvalue.getDomainConfigurationVersion()%> </td>
                    										</tr>
															<tr>
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;上一次轮询:</td>
                      											<td width="35%"><%=lasttime%> </td>                  							
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;下一次轮询:</td>
                      											<td width="35%"><%=nexttime%> </td>
                    										</tr>  
                   																	
                									</table>
                      
										</td>																					
                						<td width="20%" align="center" valign="middle" class=dashLeft>

											<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none w align=center border=0 algin="center">
                        								
                          									<tr>
                											<td width="400" align="left" valign="middle" class=dashLeft>
                												<table  style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa  cellpadding=0 rules=none width=80% align=center border=0 algin="center" >
                    													
                    													<tr class="topNameRight">
                      														<td height="30" align="center">
                      														<div id="flashcontent00">
																							<strong>You need to upgrade your Flash Player</strong>
																							</div>
																							<script type="text/javascript">
																							var so = new SWFObject("<%=rootPath%>/flex/Pie_Component.swf?percent1=<%=percent1%>&percentStr1=可用&percent2=<%=percent2%>&percentStr2=不可用", "Pie_Component", "160", "160", "8", "#ffffff");
																							so.write("flashcontent00");
																							</script>
                      														</td>
                    													</tr>
                    													<tr class="topNameRight">
                      														<td height="7" align=center><img src="<%=rootPath%>/resource/image/Loading_2.gif"></td>
                    													</tr>
                												</table>				
					                																	</td>
																                        			</tr>				
					                															</table>
					                														</td>
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
				        								<table id="application-detail-content-footer" class="application-detail-content-footer">
				        									<tr>
				        										<td>
				        											<table width="100%" border="0" cellspacing="0" cellpadding="0">
											                  			<tr>
											                    			<td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
											                    			<td></td>
											                    			<td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
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
											<table id="application-detail-data" class="application-detail-data">
												<tr>
											    	<td class="detail-data-header">
											    		<%=weblogicDetailTitleTable%>
											    	</td>
											   </tr>
		                                       <tr>
											    	<td>
											    		<table class="application-detail-data-body">
														<tr>
														<td>
												      		 <table  width="96%" align="center">
                   <tr bgcolor="#ECECEC" height="28">
          			 <td align=center>&nbsp;</td>
					 <td align=center>服务名称</td>
					 <td align=center>服务监听地址</td>
					 <td align=center>服务监听端口</td>
					 <td align=center>当前Socket数</td>
					 <td align=center>服务当前运行状态</td>
                     <td align=center>服务器IP</td>
                </tr>
			<%
				for(int i=0;i<serverdatalist.size();i++){
					WeblogicServer vo=(WeblogicServer)serverdatalist.get(i);
										
			%>
          <tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> height="28">
			<td align=center class="application-detail-data-body-list"><%=i+1%></td>          
            <td align=center class="application-detail-data-body-list">&nbsp;<%=vo.getServerRuntimeName()%></td>
            <td align=center class="application-detail-data-body-list">&nbsp;<%=vo.getServerRuntimeListenAddress()%></td>
            <td align=center class="application-detail-data-body-list">&nbsp;<%=vo.getServerRuntimeListenPort()%></td>
            <td align=center class="application-detail-data-body-list">&nbsp;<%=vo.getServerRuntimeOpenSocketsCurrentCount()%></td>
			<td align=center class="application-detail-data-body-list">&nbsp;<%=vo.getServerRuntimeState()%></td>
			<td align=center class="application-detail-data-body-list">&nbsp;<%=weblogicconf.getIpAddress()%></td>
          </tr>
          <%}%>
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
</form>
</BODY>
</HTML>