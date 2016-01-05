<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.inform.model.Performance"%>
<%@page import="com.afunms.inform.dao.PerformanceDao"%>
<%@page import="com.afunms.inform.model.Alarm"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.inform.util.SystemSnap"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.event.model.*"%>
<%@page import="com.afunms.inform.dao.AlarmDao"%>
<%@ include file="/include/globe.inc"%>
<%@ page import="java.util.*"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.topology.model.*"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.model.CustomXml"%>
<%@page import="com.afunms.topology.dao.CustomXmlDao"%>
<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  List list = null;
  Date c1=new Date();
	String timeFormat = "MM-dd HH:mm:ss";
	java.text.SimpleDateFormat timeFormatter = new java.text.SimpleDateFormat(timeFormat);
	List networklist = (List)session.getAttribute("networklist");
	List hostlist = (List)session.getAttribute("hostlist");
	String hostsize = "0";
	if(hostlist != null && hostlist.size()>0)hostsize=hostlist.size()+"";
	String dbsize = (String)session.getAttribute("dbsize");
	if(dbsize == null)dbsize="0";
	String securesize = (String)session.getAttribute("securesize");
	if(securesize == null)securesize="0";
	String servicesize = (String)session.getAttribute("servicesize");
	if(servicesize == null)servicesize="0";
	String midsize = (String)session.getAttribute("midsize");
	if(midsize == null)midsize="0";
	String routesize = (String)session.getAttribute("routesize");
	if(routesize == null)routesize="0";
	String switchsize = (String)session.getAttribute("switchsize");
	if(switchsize == null)switchsize="0";
%>
<html>

<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">

<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/xml/flush/amcolumn/swfobject.js"></script>
<script type="text/javascript" src="<%=rootPath%>/js/jquery/jquery-1.3.2.js"></script>
<script type="text/javascript" src="<%=rootPath%>/js/jquery/jquery-1.2.6.js"></script>

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
}
// 获取Flex参数
function getFlexParam(param)
{
	//alert(param);
/*	
	$.ajax({
		type:"POST",
		url: "<%=rootPath%>/user.do?action=testJQury",
		data: "param=Test",
		cache: false,
		beforeSend:function loadImg(){
			$("#device_list").html("<img style='margin:10px;' src='<%=rootPath%>/img/loading.gif' />");
		},
		success: function(html){
			$("#device_list").html(html);
		}
	});
*/	
}

</script>
</head>
<BODY bgcolor="#ababab" onload="initmenu();">
<form name="mainForm" method="post">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td width="200" valign=top align=center>
			<%=menuTable%>
		</td>
		<td align="center" valign=top >
			<table width="98%" cellpadding="0" cellspacing="0" align="center">
				<tr>
					<td background="<%=rootPath%>/common/images/right_t_02.jpg" width="100%" colspan="3">
						<table width="100%" cellspacing="0" cellpadding="0" align="center">
		                	<tr>
			                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
			                	<td class="layout_title" >系统快照</td>
			                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
			       			</tr>
			        	</table>
	            	</td>
              	</tr>
				<tr>
					<td class="tborder" bgcolor="#ffffff">
						<table cellpadding="0" cellspacing="0">
							<tr>
								<td colspan="3" bgcolor="#ffffff">
			              			<table cellpadding="0" cellspacing="0" width="100%"  align='left'>
										<tr>
			  								<td align="center">
			   									<img src="<%=rootPath%>/resource/<%=NodeHelper.getSnapStatusImage(SystemSnap.getRouterStatus(),1)%>" ><br>
			   									<a href="<%=rootPath%>/network.do?action=monitorroutelist&jp=1">路由器(<%=routesize%>)</a>
			   								</td>
			  								<td align="center">
			   									<img src="<%=rootPath%>/resource/<%=NodeHelper.getSnapStatusImage(SystemSnap.getSwitchStatus(),2)%>" ><br>
			   									<a href="<%=rootPath%>/network.do?action=monitorswitchlist&jp=1">交换机(<%=switchsize%>)</a>
			   								</td>   
			  								<td align="center">
			   									<img src="<%=rootPath%>/resource/<%=NodeHelper.getSnapStatusImage(SystemSnap.getServerStatus(),3)%>"><br>
			   									<a href="<%=rootPath%>/network.do?action=monitorhostlist&jp=1">服务器(<%=hostsize%>)</a>
			   								</td>
			   								<td align="center">
			   									<img src="<%=rootPath%>/resource/<%=NodeHelper.getSnapStatusImage(SystemSnap.getDbStatus(),4)%>"><br>
			   									<a href="<%=rootPath%>/db.do?action=list&jp=1">数据库(<%=dbsize%>)</a>
			   								</td>
			  								<td align="center">
			  									<img src="<%=rootPath%>/resource/<%=NodeHelper.getSnapStatusImage(SystemSnap.getMiddleStatus(),5)%>"><br>
			  									<a href="<%=rootPath%>/FTP.do?action=midalllist&jp=1">中间件(<%=midsize%>)</a>
			  								</td>
			  								<td align="center">
			  									<img src="<%=rootPath%>/resource/<%=NodeHelper.getSnapStatusImage(SystemSnap.getServiceStatus(),6)%>"><br>
			  									<!--<img src="<%=rootPath%>/resource/image/service.gif" ><br>-->
			  									<a href="<%=rootPath%>/FTP.do?action=allservice&jp=1">服务(<%=servicesize%>)</a>
			  								</td>
			  								<td align="center">
			  									<img src="<%=rootPath%>/resource/<%=NodeHelper.getSnapStatusImage(SystemSnap.getFirewallStatus(),7)%>">
			  									<!--<img src="<%=rootPath%>/resource/image/topo/firewall/firewall.gif">-->
			  									<br>
			  									<a href="#">安全(<%=securesize%>)</a>
			  								</td>
			  								<td align="center" bgcolor=#ffffff>
			  									<img src="<%=rootPath%>/resource/image/ariconditioner.gif"><br>
			  									<a href="#">环境</a>
			  								</td>
										</tr>		
									</table>
								</td>
							</tr>
							<tr>
			          			<td width=33% align='center'>
				            		<table width=100% align='center'>
				              			<tr> 
				                			<td> 
				                				<div id="flashcontent1">
													<strong>You need to upgrade your Flash Player</strong>
												</div>
												<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Event_Pie.swf", "Event_Pie", "330", "290", "8", "#ffffff");
													so.write("flashcontent1");
												</script> 				
				                			</td>
				              			</tr>
				            		</table>
				            	</td>
	            				<td colspan="2" width="67%" style="background-color:#FFFFFF;">
									<table width="100%" cellpadding="0" cellspacing="0">
										<tr align="center" height="28" style="background-color:#EEEEEE;"> 
									 		<td width="7%" style="word-wrap:break-word;word-break: normal;border-left:1px solid #EEEEEE;border-bottom:1px solid #EEEEEE;"> 
									   			<b>等级</b>
									   		</td>
									 		<td width="18%" style="word-wrap:break-word;word-break: normal;border-left:1px solid #EEEEEE;border-bottom:1px solid #EEEEEE;"> 
									   			<b>来源</b>
									   		</td>                                           
									 		<td width="60%" style="word-wrap:break-word;border-left:1px solid #EEEEEE;border-bottom:1px solid #EEEEEE;"> 
									   			<b>事件描述</b>
									   		</td>
									 		<td width="15%" style="word-wrap:break-word;word-break: normal;border-bottom:1px solid #EEEEEE;"> 
									   			<b>时间</b>
									   		</td>
										</tr>
								  		<%
								  			int index3 = 0;
								  			List _rpceventlist = (List)session.getAttribute("rpceventlist");
								  			if(_rpceventlist != null && _rpceventlist.size()>0){
									  			for(int i=0;i<_rpceventlist.size();i++){
										  			String eventsrc = "";
										  			index3 ++;
										  			if(index3 == 9)break;	 
										  			EventList e2 = (EventList)_rpceventlist.get(i); 			  
										  			if(e2.getManagesign() != 0) continue;
								
											  		if(e2.getSubtype().equalsIgnoreCase("network") || e2.getSubtype().equalsIgnoreCase("host")){
											  			HostNode node = new HostNode();
														HostNodeDao _dao = new HostNodeDao();
														node = _dao.loadHost(e2.getNodeid()); 
														_dao.close();
														eventsrc = node.getAlias()+"("+node.getIpAddress()+")";
											  		}else{
											  			eventsrc = e2.getEventlocation();
											  		}  			  
								  			  
								  			  		Date d2 = e2.getRecordtime().getTime();
								  			  		String time2 = timeFormatter.format(d2);
								  					String l1 = String.valueOf(e2.getLevel1());
									  			   if("1".equals(l1)){
									  					l1="普通事件";
									  			   }
									  			   if("2".equals(l1)){
									  			  		l1="紧急事件";
									  			  }
									  			  if("3".equals(l1)){
									  			  		l1="严重事件";
									  			  }
								  		%>
				                   		<tr height="25">
			                     			<td style="border-left:1px solid #EEEEEE;border-bottom:1px solid #EEEEEE;"><img src="<%=rootPath%>/resource/<%=NodeHelper.getStatusImage(e2.getLevel1())%>" alt="<%=e2.getContent()%>" border="0"/></td>
			                     			<td title="<%=eventsrc%>" style="border-left:1px solid #EEEEEE;border-bottom:1px solid #EEEEEE;"><%=eventsrc%>&nbsp;</td>
			                     			<td title="<%=e2.getContent()%>" align="left" style="word-wrap:break-word;padding-left:2px; border-left:1px solid #EEEEEE;border-bottom:1px solid #EEEEEE;"><%=e2.getContent()%></td>
			                     			<td title="<%=time2%>" style="word-wrap:break-word;border-left:1px solid #EEEEEE;border-bottom:1px solid #EEEEEE;"><%=time2%></td>
				                   		</tr>
								        <%
								          	   }
										 %>
				                   		<tr> 
				                     		<td colspan="4" align="right" style="border-left:1px solid #EEEEEE;border-bottom:1px solid #EEEEEE;" nowrap>&nbsp;<a href="<%=rootPath%>/event.do?action=list&jp=1"><font color="397dbd"><em>>>查看更多扫描事件...</em></font></a>&nbsp;</td>
				                   		</tr>   
								        <%
								          	} else {
								        %>
								
										<!-- 没有设备显示时调用 -->
										<tr>
											<td colspan="4">
												<table width="100%" height="100%" style="background-color:#ffffff;">
													<tr>
														<td width="75%" valign="middle" align="center"><img  align="absmiddle" style="margin-right:3px;" src="<%=rootPath%>/img/talk.gif" />该视图中没有项目可显示</td>
														<td width="25%" valign="bottom" align="right"><img style="margin:30px;" src="<%=rootPath%>/img/bg.gif" /></td>
												</table>
											</td>
										</tr>
										<%
											}
										 %>				
									</table>
								</td>
							</tr>
							<!--<tr>
					          	<td width=100% colspan=3>
					          		<table width="100%" border="0" cellpadding="0" cellspacing="0">
					          	    	<tr style="background-color: #ECECEC;">
					              			<td align="center" height='28'>
					              				<b>业务视图</b>
					              			</td>
					              		</tr>
					              		<tr> 
	                						<td width="5%" > 
	                							<div id="flashcontent_yewu1">
											       <strong>You need to upgrade your Flash Player</strong>
										        </div>
					          	                <script type="text/javascript">
						                            var so = new SWFObject("<%=rootPath%>/flex/NetworkTopology.swf", "NetworkTopology", "100%", "280", "9", "#FFFFFF");
						                            so.write("flashcontent_yewu1");
					                            </script>
					                        </td>
					              		</tr>
				            			</table>
				          	    	</td>
				          		</tr>
				          		-->

				          	<% 
				          		if(networklist != null && networklist.size()>0){
				          	%>
			           		<tr>
			          			<td width="33%" align=center>
			            			<table width="100%" border="0" cellpadding="0" cellspacing="0">
			              				<tr> 
			                				<td align=center> 
                								<div id="flashcontent2">
													<strong>You need to upgrade your Flash Player</strong>
												</div>
												<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Column_TOP.swf?projectName=<%=rootPath%>&dataFileName=column_network_cpu_top10.xml&dir=network_cpu&title=网络设备CPU利用率TOP10", "Column_Network_Cpu_Top10", "326", "220", "8", "#ffffff");
													so.write("flashcontent2");
												</script>
											</td>
										</tr>
									</table>
								</td>
								<td width="33%" align=center>
									<table cellpadding="0" cellspacing="0" width=100%>
										<tr> 
											<td width="100%" > 
												<div id="flashcontent4">
													<strong>You need to upgrade your Flash Player</strong>
												</div>
												<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Column_TOP.swf?projectName=<%=rootPath%>&dataFileName=column_network_in_top10.xml&dir=network_in&title=网络设备入口流速TOP10", "Column_Network_In_TOP10", "326", "220", "8", "#ffffff");
													so.write("flashcontent4");
												</script>				
											</td>
										</tr>             
									</table> 
								</td>
								<td width="34%" align=center>
					          		<table cellpadding="0" cellspacing="0" width=100%>
					              		<tr> 
					                		<td width="100%" > 
					                			<div id="flashcontent5">
													<strong>You need to upgrade your Flash Player</strong>
												</div>
												<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Column_TOP.swf?projectName=<%=rootPath%>&dataFileName=column_network_out_top10.xml&dir=network_out&title=网络设备出口流速TOP10", "Column_Network_Out_TOP10", "336", "220", "8", "#ffffff");
													so.write("flashcontent5");
												</script>				
					                		</td>
					              		</tr>             
					            	</table> 
					          	</td>
          					</tr>
					 		<%
					 			}
					 			if(hostlist != null && hostlist.size()>0){
					 		%>
			 				<tr>
								<td width="33%" align=center>
									<table width="100%" cellpadding="0" cellspacing="0">
										<tr> 
											<td> 
												<div id="flashcontent6">
													<strong>You need to upgrade your Flash Player</strong>
												</div>
												<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Column_TOP.swf?projectName=<%=rootPath%>&dataFileName=column_host_cpu_top10.xml&dir=host_cpu&title=服务器CPU利用率TOP10", "Column_Host_CPU_TOP10", "326", "210", "8", "#ffffff");
													so.write("flashcontent6");
												</script>
											</td>
										</tr>
									</table>
								</td>
								<td width="33%" align=center>
									<table cellpadding="0" cellspacing="0" width=100% align='left'>
										<tr> 
											<td width="100%" > 
												<div id="flashcontent7">
													<strong>You need to upgrade your Flash Player</strong>
												</div>
												<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Column_TOP.swf?projectName=<%=rootPath%>&dataFileName=column_host_memory_top10.xml&dir=host_memory&title=服务器内存利用率TOP10", "Column_Host_Memory_TOP10", "326", "210", "8", "#ffffff");
													so.write("flashcontent7");
												</script>			
											</td>
										</tr>             
									</table> 
									<br>
								</td>
								<td width="34%" align=center>
									<table cellpadding="0" cellspacing="0" width=100% align='left'>
										<tr> 
											<td width="100%" > 
												<div id="flashcontent8">
													<strong>You need to upgrade your Flash Player</strong>
												</div>
												<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Column_TOP.swf?projectName=<%=rootPath%>&dataFileName=column_host_disk_top10.xml&dir=host_disk&title=服务器磁盘利用率TOP10", "Column_Host_Disk_TOP10", "326", "210", "8", "#ffffff");
													so.write("flashcontent8");
												</script>				
											</td>
										</tr>             
									</table> 
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
</form>	
</body>
</html>
