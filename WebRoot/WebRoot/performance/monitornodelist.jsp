<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.common.util.*" %>

<%@page import="com.afunms.topology.model.MonitorHostDTO"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.topology.model.MonitorNetDTO"%>
<%@page import="com.afunms.topology.model.MonitorNodeDTO"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.impl.*"%>
<%@page import="com.afunms.polling.api.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.topology.model.*"%>
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
<%@page import="java.util.ArrayList"%>
<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>

<%@page import="com.afunms.detail.net.service.NetService"%>
<%@page import="com.afunms.temp.model.NodeTemp"%>
<%@ page import="com.afunms.polling.loader.HostLoader"%>

<%
  String runmodel = PollingEngine.getCollectwebflag();
  String rootPath = request.getContextPath();
  List list = (List)request.getAttribute("list");
  
  JspPage jp = (JspPage)request.getAttribute("page");
  
  String field = (String)request.getAttribute("field");
  String sorttype = (String)request.getAttribute("sorttype");
  if(sorttype == null || sorttype.trim().length() == 0){
  	  sorttype = "";
  }
  String flag = (String)request.getAttribute("flag");
  String category = (String)request.getAttribute("category");
  
  String nameImg = "";
  
  String categoryImg = "";
  
  String ipaddressImg = "";
  
  String pingImg = "";
  
  String cpuImg = "";
  
  String memoryImg = "";
  
  String inutilhdxImg = "";
  
  String oututilhdxImg = "";
  
  String imgSrc = "";
  
  	String memcollecttime = "";
	String pingcollecttime = "";
	String responsevalue = "";
	String collecttime = "";
	String pingvalue= "0";
	String cpuUsed = "0";
																			String cpuUnUsed = "";
																			if(cpuUsed.equals("0"))cpuUnUsed="100";
																			String memeryUsed = "0";
																			
																			String memeryUnUsed = "";
																			if(memeryUsed.equals("0"))memeryUnUsed="100";
																			String pingValue = "0";
  
  if("desc".equals(sorttype)){
  	  imgSrc = "/afunms/resource/image/btn_up2.gif";
  }else if("asc".equals(sorttype)){
  	  imgSrc = "/afunms/resource/image/btn_up1.gif";
  }
  
  if("name".equals(field)){
  	  nameImg = "<img src='" + imgSrc + "'>";
  }
  if("category".equals(field)){
  	  categoryImg = "<img src='" + imgSrc + "'>";
  }
  if("ipaddress".equals(field)){
  	  ipaddressImg = "<img src='" + imgSrc + "'>";
  }
  if("ping".equals(field)){
  	  pingImg = "<img src='" + imgSrc + "'>";
  }
  if("cpu".equals(field)){
  	  cpuImg = "<img src='" + imgSrc + "'>";
  }
  if("memory".equals(field)){
  	  memoryImg = "<img src='" + imgSrc + "'>";
  }
  if("inutilhdx".equals(field)){
  	  inutilhdxImg = "<img src='" + imgSrc + "'>";
  }
  if("oututilhdx".equals(field)){
  	  oututilhdxImg = "<img src='" + imgSrc + "'>";
  }
  
  String treeBid = (String)request.getAttribute("treeBid");
%>


<html>
	<head>
	
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">    
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
		<script language="JavaScript">

			//公共变量
			var node="";
			var ipaddress="";
			var operate="";
			/**
			*根据传入的id显示右键菜单
			*/
			function showMenu(id,nodeid,ip)
			{	
				ipaddress=ip;
				node=nodeid;
				//operate=oper;
			    if("" == id)
			    {
			        popMenu(itemMenu,100,"100");
			    }
			    else
			    {
			        popMenu(itemMenu,100,"1111");
			    }
			    event.returnValue=false;
			    event.cancelBubble=true;
			    return false;
			}
			/**
			*显示弹出菜单
			*menuDiv:右键菜单的内容
			*width:行显示的宽度
			*rowControlString:行控制字符串，0表示不显示，1表示显示，如“101”，则表示第1、3行显示，第2行不显示
			*/
			function popMenu(menuDiv,width,rowControlString)
			{
			    //创建弹出菜单
			    var pop=window.createPopup();
			    //设置弹出菜单的内容
			    pop.document.body.innerHTML=menuDiv.innerHTML;
			    var rowObjs=pop.document.body.all[0].rows;
			    //获得弹出菜单的行数
			    var rowCount=rowObjs.length;
			    //alert("rowCount==>"+rowCount+",rowControlString==>"+rowControlString);
			    //循环设置每行的属性
			    for(var i=0;i<rowObjs.length;i++)
			    {
			        //如果设置该行不显示，则行数减一
			        var hide=rowControlString.charAt(i)!='1';
			        if(hide){
			            rowCount--;
			        }
			        //设置是否显示该行
			        rowObjs[i].style.display=(hide)?"none":"";
			        //设置鼠标滑入该行时的效果
			        rowObjs[i].cells[0].onmouseover=function()
			        {
			            this.style.background="#397DBD";
			            this.style.color="white";
			        }
			        //设置鼠标滑出该行时的效果
			        rowObjs[i].cells[0].onmouseout=function(){
			            this.style.background="#F1F1F1";
			            this.style.color="black";
			        }
			    }
			    //屏蔽菜单的菜单
			    pop.document.oncontextmenu=function()
			    {
			            return false; 
			    }
			    //选择右键菜单的一项后，菜单隐藏
			    pop.document.onclick=function()
			    {
			        pop.hide();
			    }
			    //显示菜单
			    pop.show(event.clientX-1,event.clientY,width,rowCount*25,document.body);
			    return true;
			}
			function detail()
			{
			    location.href="<%=rootPath%>/detail/dispatcher.jsp?id=net"+node+"&flag=1";
			}
			function ping()
			{
				window.open("<%=rootPath%>/tool/ping.jsp?ipaddress="+ipaddress,"oneping", "height=400, width= 500, top=300, left=100");
				//window.open('/nms/netutil/ping.jsp?ipaddress='+ipaddress);
			}
			function traceroute()
			{
				window.open("<%=rootPath%>/tool/tracerouter.jsp?ipaddress="+ipaddress,"newtracerouter", "height=400, width= 500, top=300, left=100");
				//window.open('/nms/netutil/tracerouter.jsp?ipaddress='+ipaddress);
			}
			function telnet()
			{
				window.open('<%=rootPath%>/perform.do?action=telnet&ipaddress='+ipaddress,'onetelnet', 'height=0, width= 0, top=0, left= 0');
				//window.open('/nms/netutil/tracerouter.jsp?ipaddress='+ipaddress);
			}	
			function clickMenu()
			{
			}
		</script>
		<script type="text/javascript">
			var curpage= <%=jp.getCurrentPage()%>;
  			var totalpages = <%=jp.getPageTotal()%>;
  			var delAction = "<%=rootPath%>/perform.do?action=delete";
  			var listAction = "<%=rootPath%>/perform.do?action=monitornodelist";
  			
			function toAdd(){
				mainForm.action = "<%=rootPath%>/perform.do?action=ready_add";
				mainForm.submit();
			}
			  
			function doCancelManage(){  
     			mainForm.action = "<%=rootPath%>/perform.do?action=cancelmanage";
     			mainForm.submit();
	  		}
	  		
	  		function doQuery(){ 
	  			window.location.reload(true); 
  			}
  			
  			function doSearch(){  
				mainForm.action = "<%=rootPath%>/perform.do?action=monitornodelist";
     			mainForm.submit();
  			}
  			
  			function showCpu(id , ip){  
				CreateWindow("<%=rootPath%>/detail/net_cpu_month.jsp?id=" + 2 + "&ip=" +ip);
  			}
  			
  			function showMemery(id , ip, type){
                if (type=='net') {
                    CreateWindow("<%=rootPath%>/detail/net_memory_month.jsp?id=" + 2 + "&ip=" +ip);
                } else {
                    CreateWindow("<%=rootPath%>/detail/host_memory_month.jsp?id=" + 2 + "&ip=" +ip);
                }  
				
  			}
  			
  			function showPing(id , ip){  
				CreateWindow("<%=rootPath%>/detail/net_ping_month.jsp?id=" + 2 + "&ip=" +ip);
  			}
  			
  			function showFlux(id , ip){  
				CreateWindow("<%=rootPath%>/detail/net_flux_month.jsp?id=" + 2 + "&ip=" +ip);
  			}
  			
  			function CreateWindow(url){
				msgWindow=window.open(url,"protypeWindow","toolbar=no,width=850,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
			}
			
			function showSort(fieldValue){  
				var field = document.getElementById('field');
				field.value = fieldValue;
				
				mainForm.action = "<%=rootPath%>/perform.do?action=monitornodelist";
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
	<%
	//System.out.println("======jp.getPageTotal()=====111======" + jp.getPageTotal()); 
	%>
	<body id="body" class="body" onload="initmenu();" leftmargin="0" topmargin="0">
		<!-- 这里用来定义需要显示的右键菜单 -->
		<div id="itemMenu" style="display: none";>
		<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
			style="border: thin;font-size: 12px" cellspacing="0">
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.detail();">查看状态</td>
			</tr>
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.ping();">ping</td>
			</tr>
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.traceroute()">traceroute</td>
			</tr>
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.telnet()">telnet</td>
			</tr>		
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.clickMenu()">关闭</td>
			</tr>
		</table>
		</div>
		<!-- 右键菜单结束-->
		<form id="mainForm" method="post" name="mainForm">
			<input type="hidden" id="flag" name="flag" value="<%=flag%>">
			<input type=hidden id="category" name="category" value="<%=category%>">
			<input type=hidden id="treeBid" name="treeBid" value="<%=treeBid%>">
			<table id="body-container" class="body-container" height="100%">
				<tr>
					<td class="td-container-main">
						<table id="container-main" class="container-main">
							<tr>
								<td class="td-container-main-content">
									<table id="container-main-content" class="container-main-content">
										<tr>
											<td>
												<table id="content-header" class="content-header">
								                	<tr>
									                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
									                	<td class="content-title">&nbsp;资源 >> 性能监视 >> 监视对象一览 >> 设备列表 </td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									       			</tr>
									        	</table>
		        							</td>
		        						</tr>
		        						<tr>
		        							<td>
		        								<table id="content-body" class="content-body">
		        									<tr>
														<td>
															<table>
																<tr>
													    			<td  class="body-data-title">
							    										<jsp:include page="../common/page.jsp">
																			<jsp:param name="curpage" value="<%=jp.getCurrentPage()%>"/>
																			<jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>"/>
							  										 	</jsp:include>
							        								</td>
							        							</tr>
															</table>
														</td>
													</tr> 
													<tr>
														<td >
															<table>
																<tr>
																	<td class="body-data-title" style="text-align: left;" >
																	
																		&nbsp;
																		<B>查&nbsp;&nbsp;询:</B>
							        									<SELECT name="key"> 
								          									<OPTION value="alias" selected>名称</OPTION>
								          									<OPTION value="ip_address">IP地址</OPTION>
								          									<OPTION value="sys_oid">系统OID</OPTION>          
								          									<OPTION value="type">型号</OPTION>
								          								</SELECT>&nbsp;<b>=</b>&nbsp; 
								          								<INPUT type="text" name="value" width="15" class="formStyle">
								          								<INPUT type="button" class="formStyle" value="查    询" onclick=" return doSearch()">
								          							</td>            
																</tr>
															</table>
											  			</td>
													</tr>
		        									<tr>
		        										<td>
		        											<table>
		        												<tr>
		        													<input type="hidden" id="field" name="field" value="<%=field%>">
		        													<input type="hidden" id="sorttype" name="sorttype" value="<%=sorttype%>">
		        													<td align="center" class="body-data-title" width="5%"><INPUT type="checkbox" id="checkall" name="checkall" onclick="javascript:chkall()">序号</td>
		        													<td align="center" class="body-data-title" width="11%"><a href="#" onclick="showSort('name')">名称</a><%=nameImg%></td>
		        													<td align="center" class="body-data-title" width="5%"><a href="#" onclick="showSort('category')">类型</a><%=categoryImg%></td>
		        													<td align="center" class="body-data-title" width="14%"><a href="#" onclick="showSort('ipaddress')">IP地址</a><%=ipaddressImg%></td>
		        													<td align="center" class="body-data-title" width="6%"><a href="#" onclick="showSort('ping')">可用性</a><%=pingImg%></td>
		        													<td align="center" class="body-data-title" width="10%"><a href="#" onclick="showSort('cpu')">CPU(%)</a><%=cpuImg%></td>
		        													<td align="center" class="body-data-title" width="10%"><a href="#" onclick="showSort('memory')">内存(%)</a><%=memoryImg%></td>
                                                                    <td align="center" class="body-data-title" width="12%"><a href="#" onclick="showSort('inutilhdx')">入口流速(KB/S)</a><%=inutilhdxImg%></td>
		        													<td align="center" class="body-data-title" width="12%"><a href="#" onclick="showSort('oututilhdx')">出口流速(KB/S)</a><%=oututilhdxImg%></td>
		        													<%--<td align="center" class="body-data-title" width="10%">告警</td> --%>
		        													<td align="center" class="body-data-title" width="6%">接口数量</td> 
		        													<td align="center" class="body-data-title" width="6%">采集方式</td>
		        												</tr>
		        												<%
					        									    if(list!=null&& list.size()>0){
					        									        for(int i = 0 ; i < list.size() ; i++){
					        									        	MonitorNodeDTO monitorNodeDTO = (MonitorNodeDTO)list.get(i);
					        									        	pingValue = monitorNodeDTO.getPingValue();
					        									        	cpuUsed = monitorNodeDTO.getCpuValue();
					        									        	cpuUnUsed = String.valueOf(100 - Double.valueOf(cpuUsed));
					        									        	memeryUsed = monitorNodeDTO.getMemoryValue();
					        									        	memeryUnUsed = String.valueOf(100 - Double.valueOf(memeryUsed));
					        									        	String cpuValueColor = monitorNodeDTO.getCpuValueColor();
					        									        	String memeryValueColor = monitorNodeDTO.getMemoryValueColor();
					        									        	String status = monitorNodeDTO.getStatus();
					        									        	String statusImg = "";
					        									        	if("1".equals(status)){
					        									        		statusImg = "alarm_level_1.gif";
					        									        	}else if("2".equals(status)){
					        									        		statusImg = "alarm_level_2.gif";
					        									        	}else if("3".equals(status)){
					        									        		statusImg = "alert.gif";
					        									        	}else{
					        									        		statusImg = "status_ok.gif";
					        									        	}
					        									        	//Hashtable eventListSummary = monitorNodeDTO.getEventListSummary();
					        									        	//String generalAlarm = (String)eventListSummary.get("generalAlarm");
					        									        	//String urgentAlarm = (String)eventListSummary.get("urgentAlarm");
					        									        	//String seriousAlarm = (String)eventListSummary.get("seriousAlarm");
					        									        	//System.out.println("==============1441");
					        									            %>
					        									            <tr <%=onmouseoverstyle%>>
						        									            <td align="center" class="body-data-list"><INPUT type="checkbox" id="checkbox" name="checkbox" value="<%=monitorNodeDTO.getId()%>"><%=jp.getStartRow()+i %></td>
					        													<td align="left" class="body-data-list"><img src="<%=rootPath%>/resource/image/topo/<%=statusImg%>">&nbsp;<a href="<%=rootPath%>/detail/dispatcher.jsp?id=net<%=monitorNodeDTO.getId()%>&flag=1"><%=monitorNodeDTO.getAlias()%></a></td>
					        													<td align="center" class="body-data-list"><%=monitorNodeDTO.getCategory()%></td>
					        													<td align="left" class="body-data-list">
					        														<table>
													                      				<tr>
													                      					<td>  
								        									               		<select style="width:115px;" name="ipalias<%=monitorNodeDTO.getIpAddress() %>">
														                  							<option selected><%=monitorNodeDTO.getIpAddress() %></option>
														                  							<%
																                  						IpAliasDao ipdao = new IpAliasDao();
																										 List iplist = ipdao.loadByIpaddress(monitorNodeDTO.getIpAddress());
																										 ipdao.close();
																										 ipdao = new IpAliasDao();
											                     											 IpAlias ipalias = ipdao.getByIpAndUsedFlag(monitorNodeDTO.getIpAddress(),"1"); 
											                     											 ipdao.close();
																										 if(iplist == null)iplist = new ArrayList(); 
									       															   for(int j=0 ;j<iplist.size() ; j++){
									      																					IpAlias voTemp = (IpAlias)iplist.get(j); 
									      																				%>
									      																				<option <%if(ipalias != null && ipalias.getAliasip().equals(voTemp.getAliasip())){ %>selected<%} %>><%=voTemp.getAliasip() %></option>
									       															<%} %>
									       														</select>
								                    											<img href="#" src="<%= rootPath%>/resource/image/menu/xgmm.gif" style="cursor:hand" onclick="modifyIpAliasajax('<%=monitorNodeDTO.getIpAddress()%>');"/>
                    																		</td>
                    																	</tr>
												                      				</table>
                    															</td>
					        													<td align="center" class="body-data-list">
					        														<table>
													                      				<tr>
													                      					<td align="center" width="50%"><%=pingValue%>%</td>
													                      					<td align="center" width="50%"><img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showPing("<%=monitorNodeDTO .getId()%>" , "<%=monitorNodeDTO.getIpAddress()%>")' width=15></td>
													                      				</tr>
												                      				</table>
					        													</td>
					        													<td align="center" class="body-data-list">
					        														<table>
													                      				<tr>
													                      					<td><%=cpuUsed%>%</td>
													                      					<td width=150>
													                      						<table border=1 height=15 width="100%" bgcolor=#ffffff>
																                      				<tr>
																                      					<td width="<%=cpuUsed%>%" bgcolor="<%=cpuValueColor%>"></td>
																                      					<td width="<%=cpuUnUsed%>%" bgcolor=#ffffff></td>
																                      				</tr>
															                      				</table>
													                      					</td>
													                      					<td><img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showCpu("<%=monitorNodeDTO .getId()%>" , "<%=monitorNodeDTO.getIpAddress()%>")' width=15></td>
													                      				</tr>
												                      				</table>
					        													</td>
					        													<td align="center" class="body-data-list" >
					        														<table>
													                      				<tr>
													                      					<td><%=memeryUsed%>%</td>
													                      					<td width=150>
													                      						<table border=1 height=15 width="100%" bgcolor=#ffffff>
																                      				<tr>
																                      					<td width="<%=memeryUsed%>%" bgcolor="<%=memeryValueColor%>"></td>
																                      					<td width="<%=memeryUnUsed%>%" bgcolor=#ffffff></td>
																                      				</tr>
															                      				</table>
													                      					</td>
													                      					<td><img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showMemery("<%=monitorNodeDTO .getId()%>" , "<%=monitorNodeDTO.getIpAddress()%>" , "<%=monitorNodeDTO.getType()%>")' width=15></td>
													                      				</tr>
												                      				</table>
					        													</td>
					        													<td align="center" class="body-data-list">
					        														<table>
													                      				<tr>
													                      					<td align="center" width="50%"><%=monitorNodeDTO.getInutilhdxValue()%></td>
													                      					<td align="center" width="50%"><img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showFlux("<%=monitorNodeDTO .getId()%>" , "<%=monitorNodeDTO.getIpAddress()%>")' width=15></td>
													                      				</tr>
												                      				</table>
					        													</td>
					        													<td align="center" class="body-data-list">
					        														<table>
													                      				<tr>
													                      					<td align="center" width="50%"><%=monitorNodeDTO.getOututilhdxValue()%></td>
													                      					<td align="center" width="50%"><img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showFlux("<%=monitorNodeDTO .getId()%>" , "<%=monitorNodeDTO.getIpAddress()%>")' width=15></td>
													                      				</tr>
												                      				</table></td>
					        													<%-- <td align="center" class="body-data-list">
					        														<table>
													                      				<tr>
													                      					<td align="center">
													                      						<table border=1 height=15 bgcolor=#ffffff>
																                      				<tr>
																                      					<td width="30%" bgcolor="#ffff00" align="center"><%=generalAlarm%></td>
																                      					<td width="30%" bgcolor="orange" align="center"><%=urgentAlarm%></td>
																                      					<td width="30%" bgcolor="#ff0000" align="center"><%=seriousAlarm%></td>
																                      				</tr>
															                      				</table>
													                      					</td>
													                      				</tr>
												                      				</table>
					        													</td>--%>
					        													<td align="center" class="body-data-list"><%=monitorNodeDTO.getEntityNumber()%></td>  
					        													<td align="center" class="body-data-list"><%=monitorNodeDTO.getCollectType()%></td>
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
		        							<td>
		        								<table id="content-footer" class="content-footer">
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
						</table>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
