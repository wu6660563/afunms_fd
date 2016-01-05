<%@ page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@ page import="com.afunms.event.model.EventList"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.topology.model.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="com.afunms.polling.om.IpRouter"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.temp.model.*"%>
<html>
<head>
<%
	String runmodel = PollingEngine.getCollectwebflag(); 
  String rootPath = request.getContextPath();
  //System.out.println(rootPath);
  List pinglist = (List)session.getAttribute("pinglist");
  String ipaddress = (String)request.getAttribute("ipaddress");
String startdate = (String)request.getAttribute("startdate");
String todate = (String)request.getAttribute("todate");
Hashtable reportHash = (Hashtable)session.getAttribute("reporthash");
String newip = (String)request.getAttribute("newip");
Hashtable CPU = (Hashtable) reportHash.get("CPU");
		String Ping = (String) reportHash.get("Ping");
		String ip = (String) reportHash.get("ip");
		Vector netifVector = (Vector) reportHash.get("netifVector");
		List routerList = (ArrayList)reportHash.get("routerList");
		Hashtable portconfigHash = (Hashtable) reportHash.get("portconfigHash");
		List reportports = (List) reportHash.get("reportports");
		Vector iprouterVector = (Vector) reportHash.get("iprouterVector");

		Hashtable Memory = (Hashtable) reportHash.get("Memory");
		Hashtable Disk = (Hashtable) reportHash.get("Disk");
		String hostname = (String) reportHash.get("equipname");
		Hashtable memMaxHash = (Hashtable) reportHash.get("memmaxhash");
		Hashtable maxping = (Hashtable) reportHash.get("ping");
String[] netIfItemch = { "索引", "描述", "关联应用", "每秒字节数(M)", "当前状态",
				"出口流速", "入口流速" };
		String[] ipRouterItemch = { "端口索引", "目标地址", "下一跳", "路由类型", "路由协议",
				"子网掩码" };
		String[] memoryItem = { "Capability", "Utilization" };
		String[] diskItem = { "AllSize", "UsedSize", "Utilization",
				"INodeUsedSize", "INodeUtilization" };
		String[] diskItemch = { "总容量", "已用容量", "利用率", "i-node已使用", "i-node利用率" };
		String[] iproutertype = { "", "", "", "direct(3)", "indirect(4)" };
		String[] iprouterproto = { "", "other(1)", "local(2)", "netmgmt(3)",
				"icmp(4)", "egp(5)", "ggp(6)", "hello(7)", "rip(8)",
				"is-is(9)", "es-is(10)", "ciscoIgrp(11)", "bbnSpfIgp(12)",
				"ospf(13)", "bgp(14)" };
%>
<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>

<title>综合报表</title>
<!-- snow add 2010-5-28 -->
<style>
<!--
body{
background-image: url(${pageContext.request.contextPath}/common/images/menubg_report.jpg);
TEXT-ALIGN: center; 
}
-->
</style>
<!-- snow add end -->
<script type='text/javascript' src='/afunms/dwr/interface/DWRUtil.js'></script>
<script type='text/javascript' src='/afunms/dwr/engine.js'></script>
<script type='text/javascript' src='/afunms/dwr/util.js'></script>
<script language="javascript" src="/afunms/js/tool.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
<script language="JavaScript" type="text/JavaScript">
function network_composite_word_report()
{
	mainForm.action = "<%=rootPath%>/netreport.do?action=downloadNetworkCompositeReport&ipaddress=<%=ipaddress%>&str=1";
	mainForm.submit();
}
function network_composite_excel_report()
{
	mainForm.action = "<%=rootPath%>/netreport.do?action=downloadNetworkCompositeReport&ipaddress=<%=ipaddress%>&str=0";
	mainForm.submit();
}
function network_composite_pdf_report()
{
	mainForm.action = "<%=rootPath%>/netreport.do?action=downloadNetworkCompositeReport&ipaddress=<%=ipaddress%>&str=5";
	mainForm.submit();
}
function network_composite_ok()
{
	//var prewindow = window.open("<%=rootPath%>/netreport.do?action=showCompositeReport&ipaddress=<%=ipaddress%>","hello","width=600,height=400");
	//prewindow.opener.close();
	var starttime = document.getElementById("mystartdate").value;
	var endtime = document.getElementById("mytodate").value;
	mainForm.action = "<%=rootPath%>/netreport.do?action=showCompositeReport&ipaddress=<%=ipaddress%>&startdate="+starttime+"&todate="+endtime;
	mainForm.submit();
}
function network_composite_cancel()
{
window.close();
}
function query(){
  	var startdate = mainForm.startdate.value;
  	var todate = mainForm.todate.value;      
  	var oids ="";
  	var checkbox = document.getElementsByName("checkbox");
 		for (var i=0;i<checkbox.length;i++){
 			if(checkbox[i].checked==true){
 				if (oids==""){
 					oids=checkbox[i].value;
 				}else{
 					oids=oids+","+checkbox[i].value;
 				}
 			}
 		}
 	if(oids==null||oids==""){
 		alert("请至少选择一个设备");
 		return;
 	}
 	window.open ("<%=rootPath%>/hostreport.do?action=downloadmultihostreport&startdate="+startdate+"&todate="+todate+"&ids="+oids, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes")    	      
}

function openwin(str,operate,ip) 
{	
  var startdate = mainForm.startdate.value;
  var todate = mainForm.todate.value;
  window.open ("<%=rootPath%>/hostreport.do?action="+operate+"&startdate="+startdate+"&todate="+todate+"&ipaddress="+ip+"&str="+str+"&type=host", "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes") 
}
function changeOrder(para){
	mainForm.orderflag.value = para;
	mainForm.action="<%=rootPath%>/netreport.do?action=netping"
  	mainForm.submit();
}
  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
 Ext.get("process").on("click",function(){
     
     //if(chk1&&chk2&&chk3)
     //{
     
        Ext.MessageBox.wait('数据加载中，请稍后.. '); 
        //msg.style.display="block";
	mainForm.action="<%=rootPath%>/hostreport.do?action=hostcpu";
	mainForm.submit();        
        //mainForm.action = "<%=rootPath%>/network.do?action=add";
        //mainForm.submit();
     //}  
       // mainForm.submit();
 });	
	
});
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
	document.getElementById('hostReportTitle-5').className='detail-data-title';
	document.getElementById('hostReportTitle-5').onmouseover="this.className='detail-data-title'";
	document.getElementById('hostReportTitle-5').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}
</script>
</head>
<body id="body" class="body" onload="init();">
<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 189px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
<form id="mainForm" method="post" name="mainForm">
<input type=hidden id="ipaddress" name="ipaddress" value=<%=request.getParameter("ipaddress") %>>
	<table id="container-main" class="container-main">
		<tr>
			<td>
				<table id="container-main-win" class="container-main-win">
					<tr>
						<td>
							<table id="win-content" class="win-content">
								<tr>
									<td>
										<table id="win-content-header" class="win-content-header">
				                			<tr>
							                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
							                	<td class="win-content-title" style="align:center">&nbsp;综合报表</td>
							                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>											   
											</tr>
									    
					       				</table>
				       				</td>
				       			</tr>
						       	<tr>
						       		<td>
						       			<table id="win-content-body" class="win-content-body">
											<tr>
						       					<td>
													<table bgcolor="#ECECEC">
														<tr align="left" valign="center"> 
														<td height="28" align="left">
															开始日期
															<input type="text" id="mystartdate" name="startdate" value="<%=startdate%>" size="10">
															<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
															<img id=imageCalendar1 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a>
																							
															截止日期
															<input type="text" id="mytodate" name="todate" value="<%=todate%>" size="10"/>
															<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
															<img id=imageCalendar2 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a>
															<input type="button" name="doprocess" value="确定" onclick="network_composite_ok()">
														</td>
														<td height="28" align="left">
															<a href="javascript:network_composite_word_report()"><img name="selDay1" alt='导出word' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_word.gif" width=18  border="0">导出WORLD</a>
														</td>
														<td height="28" align="left">
															<a href="javascript:network_composite_excel_report()"><img name="selDay1" alt='导出EXCEL' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=18  border="0">导出EXCEL</a>
														</td>
														<td height="28" align="left">&nbsp;
															<a href="javascript:network_composite_pdf_report()"><img name="selDay1" alt='导出word' style="CURSOR: hand" src="<%=rootPath%>/resource/image/export_pdf.gif" width=18 border="0">导出PDF</a>
														</td>
														</tr>  
													</table>
						       					</td>
						       				</tr>
											<tr>
							                	<td class="win-data-title" style="height: 29px;" ></td>
							       			</tr>
							       			<tr align="left" valign="center"> 
			             						<td height="28" align="left" border="0">
													
													<input type=hidden name="eventid">
													<div id="loading">
													<div class="loading-indicator">
														<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif" width="32" height="32" style="margin-right: 8px;" align="middle" />Loading...</div>
													</div>
													<table width="70%">
						<tr>
						<%
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String contextString = "报表生成时间:" + new Date().toString() + " \n";// 换行+ "数据采集时间:" + sdf.format(cc);
						%>
							<td colspan="8" align="center"><%=hostname %>性能报表</td>
						</tr>
						<tr>
							<td align="center"><%=contextString %></td>
						</tr>
						<tr>
							<td align="center">
								<table border="1" width="90%">
									<tr>
										<td>连通率</td><td>当前连通率</td><td>最小连通率</td><td>平均连通率</td>
										<%
											String myPing = Ping + "%";
											String myPingmax = (String) maxping.get("pingmax");
											String avgpingcon = (String) maxping.get("avgpingcon");
						 				%>
									</tr>
									<tr>
						 				<td>&nbsp;</td><td><%=myPing %></td><td><%=myPingmax %></td><td><%=avgpingcon %></td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td align="center">
								<img src="<%=rootPath%>/resource/image/jfreechart/<%=newip %>ConnectUtilization.png"/>
							</td>
						</tr>
						<tr>
							<td  align="center">
								<table border="1" width="90%">
									<tr>
										<td>CPU利用率</td><td>当前利用率</td><td>最大利用率</td><td>平均利用率</td>
									</tr>
									<%
										String myCpu = (String) CPU.get("cpu") + "%";
										String myCpumax = (String) CPU.get("cpumax") + "";
										String myAvgcpu = (String) CPU.get("avgcpu") + "";
									%>
									<tr>
										<td>&nbsp;</td><td><%=myCpu %></td><td><%=myCpumax %></td><td><%=myAvgcpu %></td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td align="center"><img src="<%=rootPath%>/resource/image/jfreechart/<%=newip %>cpu.png"/></td>
						</tr>
						<%
						if (netifVector != null && netifVector.size() > 0) 
						{
						%>
						<tr>
							<td align="center">
								<table border="1">
									<tr>
										<td>端口使用情况</td><td>索引</td><td>描述</td><td>关联应用</td><td>每秒字节数(M)</td><td>当前状态</td><td>出口流速</td><td>入口流速</td>
									</tr>
						
									<%
									// 写端口
									for (int i = 0; i < netifVector.size(); i++) 
									{
									%>
									<tr>
										<td>&nbsp;</td>
										<%
										String[] strs = (String[]) netifVector.get(i);
										String ifname = strs[1];
										String index = strs[0];
										for (int j = 0; j < strs.length; j++) 
										{

											if (j == 1) 
											{

												String linkuse = "";
												if (portconfigHash != null && portconfigHash.size() > 0) 
												{
													if (portconfigHash.get(ip + ":" + index) != null)
														linkuse = (String) portconfigHash.get(ip + ":"+ index);
												}
										%>
										<td><%=strs[j] %></td>
										<td><%=linkuse %>&nbsp;</td>
										<%
											}
											else if (j > 1) 
											{
										%>
										<td><%=strs[j] %></td>
										<%
											} 
											else 
											{
										%>
											<td><%=strs[j] %></td>
										<%
											}
										}// end 写端口
										%>
									</tr>
									<%
									}
								}
								%>
								</table>
							</td>
						</tr>
						<%
						
						%>
						<tr>
							<td align="center">
								<table border="1">
									<tr>
										<td>路由表信息</td><td>端口索引</td><td>目标地址</td><td>下一跳</td><td>路由类型</td><td>路由协议</td><td>子网掩码</td>
									</tr>
								
									<%
									// 写路由表信息
 									if("0".equals(runmodel)){
 									//采集与访问是集成模式
	 									if (iprouterVector != null && iprouterVector.size() > 0) {
											for (int i = 0; i < iprouterVector.size(); i++) 
											{
												IpRouter iprouter = (IpRouter) iprouterVector.get(i);
												String ifIndexStr = iprouter.getIfindex();
												String destStr = iprouter.getDest();
												String nextHopStr = iprouter.getNexthop();
												String ipRouteTypeStr = iproutertype[Integer.parseInt(iprouter.getType().longValue()+ "")];
												String ipRouterProtoStr = iprouterproto[Integer.parseInt(iprouter.getProto().longValue()+ "")];
												String maskStr = iprouter.getMask();
											%>
											<tr>
												<td>&nbsp;</td>
												<td><%=ifIndexStr %></td>
												<td><%=destStr %></td>
												<td><%=nextHopStr %></td>
												<td><%=ipRouteTypeStr %></td>
												<td><%=ipRouterProtoStr %></td>
												<td><%=maskStr %></td>
											</tr>
											<%
											}
										}
									}else{
									//采集与访问是分离模式
							            %> 
							            <%
							            if (routerList != null){
							            	for(int i=0 ;i<routerList.size(); i++){
							             	RouterNodeTemp iprouter = (RouterNodeTemp)routerList.get(i);
							            %>
							            <tr style=bgcolor:#FFFFF" bgcolor="#FFFFFF" height=28>
							            <td>&nbsp;</td>
							            <td><%=iprouter.getIfindex()%></td>
							            <td><%=iprouter.getDest()%></td>
							            <td><%=iprouter.getNexthop()%></td>
							            <td><%=iprouter.getRtype()%></td>
							            <td><%=iprouter.getProto()%></td>
							            <td><%=iprouter.getMask()%></td>
							            </tr>            
							           <%
							           		}
							           	}
							        }
						%>
								</table>
							</td>
						</tr>
						<%
						// 路由表doc添加成功

						if (reportports != null && reportports.size() > 0) 
						{
						// 显示该端口的流速图形
						// aTable4.addCell("路由表信息");
							for (int i = 0; i < reportports.size(); i++) 
							{
								
								com.afunms.config.model.Portconfig portconfig = (com.afunms.config.model.Portconfig) reportports.get(i);
								String str1 = "第" + portconfig.getPortindex() + "("+ portconfig.getName() + ")端口";
								%>
						<tr>
							<td>
								<table>
									<tr>
										<td><%=str1 %></td>
									
								<%
								if (portconfig.getLinkuse() == null)
									portconfig.setLinkuse("");
								String str2 = "应用:" + portconfig.getLinkuse();
								String myPortIndexStr = portconfig.getPortindex()+"";

								%>
										<td><%=str2 %></td></tr>
									<tr><td><img src="<%=rootPath%>/resource/image/jfreechart/<%=newip %><%=myPortIndexStr%>ifspeed_day.png"/></td></tr>
								<%
								%>
								</table>
							</td>
						</tr>
						<%
							}
						}// 出口流速图片
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
			
            		<div align=center>
            			<input type=button value="关闭窗口" onclick="window.close()">
            		</div>  
					<br>
</form> 
</body>
</html>