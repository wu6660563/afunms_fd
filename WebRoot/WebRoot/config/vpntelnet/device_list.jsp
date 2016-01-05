<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.topology.model.ConnectTypeConfig"%>
<%@page import="com.afunms.config.model.ConfiguringDevice"%>
<%@page import="com.afunms.config.manage.HaweitelnetconfManager"%>
<%@page import="java.util.*" %>
<%@page import="com.afunms.config.model.PasswdTimingBackupTelnetConfig"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.system.util.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  List<PasswdTimingBackupTelnetConfig> PasswdTimingBackupTelnetConfigList = (ArrayList<PasswdTimingBackupTelnetConfig>)request.getAttribute("list");
  //List list = (List)request.getAttribute("passwdTimingBackupTelnetConfigList");
  JspPage jp = (JspPage) request.getAttribute("page");
  
%>


<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet"
			type="text/css" />
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet"
			type="text/css" />
		<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css"
			rel="stylesheet">
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css"
			rel="stylesheet">
		<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet"
			type="text/css">
		<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css"
			type="text/css">
		<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet"
			type="text/css">
		<link href="<%=rootPath%>/resource/css/global/global.css"
			rel="stylesheet" type="text/css" />
			
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
		</script>
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
				window.open("<%=rootPath%>/vpntelnetconf.do?action=readySetupConfig&ipaddress="+ipaddress,"oneping", "height=400, width= 800, top=300, left=100,scrollbars=yes");
				//window.open('<%=rootPath%>/network.do?action=telnet&ipaddress='+ipaddress,'onetelnet', 'height=0, width= 0, top=0, left= 0');
				//window.open('/nms/netutil/tracerouter.jsp?ipaddress='+ipaddress);
			}
			function setupcfg()
			{
				window.open("<%=rootPath%>/vpntelnetconf.do?action=readySetupConfig&&id="+node+"&ipaddress="+ipaddress,"oneping", "height=400, width= 500, top=300, left=100,scrollbars=yes");
				//window.open('<%=rootPath%>/network.do?action=telnet&ipaddress='+ipaddress,'onetelnet', 'height=0, width= 0, top=0, left= 0');
			}
		</script>
		<script type="text/javascript">
		
			function edit()
			{
				mainForm.action="<%=rootPath%>/vpntelnetconf.do?action=ready_editPasswdTimingBackupTelnetConfig&id="+node;
				mainForm.submit();
			}
	  		function addBackup()
			{
				mainForm.action="<%=rootPath%>/vpntelnetconf.do?action=addPasswdBackup&id="+node;
				mainForm.submit();
			}
	  		function disBackup()
			{
				mainForm.action="<%=rootPath%>/vpntelnetconf.do?action=disPasswdBackup&id="+node;
				mainForm.submit();
			}
			  			
			function toAdd(){
				mainForm.action = "<%=rootPath%>/vpntelnetconf.do?action=ready_addPasswd";
			    mainForm.submit();
			}
	  		function toDelete(){  
     				mainForm.action = "<%=rootPath%>/vpntelnetconf.do?action=deletePasswdTimingBackupTelnetConfig";
     				mainForm.submit();
	  		}
			  
			function doCancelManage(){  
     				mainForm.action = "<%=rootPath%>/network.do?action=cancelmanage";
     				mainForm.submit();
	  		}
	  		
	  		function toFindIp()
			{
			     mainForm.action = "<%=rootPath%>/vpntelnetconf.do?action=findip";
			     mainForm.submit();
			}
	  		function doQuery(){  
				window.location = window.location;
  			}
  			
  			function doSearch(){  
				mainForm.action = "<%=rootPath%>/network.do?action=monitornodelist";
     			mainForm.submit();
  			}
  			
  			function showCpu(id , ip){  
				CreateWindow("<%=rootPath%>/detail/net_cpu_month.jsp?id=" + 2 + "&ip=" +ip);
  			}
  			
  			function showMemery(id , ip){  
				CreateWindow("<%=rootPath%>/detail/host_memory_month.jsp?id=" + 2 + "&ip=" +ip);
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
				mainForm.action = "<%=rootPath%>/network.do?action=monitornodelist";
     			mainForm.submit();
  			}
		</script>
	</head>
	<body id="body" class="body" onload="initmenu();">
	<div id="itemMenu" style="display: none";>
		<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
			style="border: thin;font-size: 12px" cellspacing="0">
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.edit()">编辑</td>
			</tr>
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.addBackup()">启动</td>
			</tr>
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.disBackup()">取消启动</td>
			</tr>
		</table>
	</div>
		<form id="mainForm" method="post" name="mainForm">
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
								<td class="td-container-main-content">
									<table id="container-main-content" class="container-main-content">
										<tr>
											<td>
												<table id="content-header" class="content-header">
								                	<tr>
									                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
									                	<td class="content-title"> 自动化 >> 远程设备维护 >> 密码定时变更 </td>
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
															<table width="100%" cellpadding="0" cellspacing="1">
																<tr><!--  
																	<td bgcolor="#ECECEC" width="90%">
																	&nbsp;&nbsp;&nbsp;
																		ip地址：<input type="text" size="30" name="ipfindaddress">
																		<input type="button" value="查询" onclick="toFindIp()">
																	</td>
																	-->
																	<td bgcolor="#ECECEC" width="10%" align='right'>
																		<a href="#" onclick="toAdd()">添加</a>
																		<a href="#" onclick="toDelete()">删除</a>&nbsp;&nbsp;&nbsp;
																	</td>
																</tr>
															</table>
														</td>
													</tr>
		        									<tr>
		        										<td colspan="2">
		        											<table cellspacing="0" border="1" bordercolor="#ababab">
		        												<tr height=28 style="background:#ECECEC" align="center" class="content-title">
		        													<td align="center"><INPUT type="checkbox" id="checkall" name="checkall" onclick="javascript:chkall()" class=noborder></td>
		        													<td align="center">序号</td>
		        													<td align="center">IP地址</td>
		        												    <td align="center">任务创建时间</td>

		        													<td align="center">定时提醒时间</td>
		        													<td align="center">提醒方式</td>
		        													<td align="center">是否定时</td>
		        													<td align="center" width="20%">操作</td>
		        												</tr>
        												    <%					
															if(PasswdTimingBackupTelnetConfigList != null){
																//String[] frequencyName = { "每天", "每周", "每月", "每季", "每年" };
																String[] monthCh = { " 1月", " 2月", " 3月", " 4月", " 5月", " 6月", " 7月", " 8月",
																		" 9月", " 10月", " 11月", " 12月" };
																String[] weekCh = { " 星期日", " 星期一", " 星期二", " 星期三", " 星期四", " 星期五", " 星期六" };
																String[] dayCh = null;
																String[] hourCh =null;
																for(int i=0; i<PasswdTimingBackupTelnetConfigList.size(); i++){
																	PasswdTimingBackupTelnetConfig passWDBackupTelnetConfig = (PasswdTimingBackupTelnetConfig)PasswdTimingBackupTelnetConfigList.get(i);
																	StringBuffer sb = new StringBuffer();
																	String frequency = passWDBackupTelnetConfig.getBackup_sendfrequency();
																	String month = HaweitelnetconfManager.splitDate(passWDBackupTelnetConfig.getBackup_time_month(), monthCh, "month");
																	String week = HaweitelnetconfManager.splitDate(passWDBackupTelnetConfig.getBackup_time_week(), weekCh, "week"); 
																	String day = HaweitelnetconfManager.splitDate(passWDBackupTelnetConfig.getBackup_time_day(), dayCh, "day");
																	String hour = HaweitelnetconfManager.splitDate(passWDBackupTelnetConfig.getBackup_time_hou(), hourCh, "hour");
																	sb.append(frequency + " ");
														
																	if(frequency.equals("每天")){//每天 
																		sb.append(" 时间：(" + hour + ")");
																	}
																	if(frequency.equals("每周")){//每周
																		sb.append(" 星期:(" + week + ")");
																		sb.append(" 时间：(" + hour + ")");
																	}
																	if(frequency.equals("每月")){//每月
																		sb.append(" 日期：(" + day + ")");
																		sb.append(" 时间：(" + hour + ")");
																	}
																	if(frequency.equals("每季")){//每季
																		sb.append(" 月份：(" + month + ")");
																		sb.append(" 日期：(" + day + ")");
																		sb.append(" 时间：(" + hour + ")");
																	}
																	if(frequency.equals("每年")){//每年
																		sb.append(" 月份：(" + month + ")");
																		sb.append(" 日期：(" + day + ")");
																		sb.append(" 时间：(" + hour + ")");
																	}
																	
																	String status = passWDBackupTelnetConfig.getStatus();
																 %>
		        												  <tr <%=onmouseoverstyle%>>
										        						<td align='center'>
																		<INPUT type="checkbox" class=noborder name="checkbox"
																				value="<%=passWDBackupTelnetConfig.getId()%>">
																		</td>
																		<td align='center'>
																			<font color='blue'><%=i+1 %></font>
		        														<br></td> 
		        														<td align='center'><%=passWDBackupTelnetConfig.getTelnetconfigips()%><br></td>
		        														<td align='center'><%=passWDBackupTelnetConfig.getBackup_date() %></font></td>
		        														<td align='center'><%=sb.toString()+"" %></td>
		        														<td align='center'><%=passWDBackupTelnetConfig.getWarntype() %></td>
		        														<td align='center'><%="是".equals(status)?"启动":"未启动" %></td>
		        														<td align='center'>
																			<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('2','<%=passWDBackupTelnetConfig.getId() %>') alt="右键操作">
																		</td>
		        													</tr>
		        													<%
		        												}}
	        												%>
		        												
		        												<!--<c:forEach items="${list}" var="list" varStatus="sta">
			        												<tr>
			        													<td align='center'>
																			<INPUT type="checkbox" class=noborder name="checkbox"
																				value="${list.id}">
																		</td>
			        													<td align="center">${sta.index+1}</td>
			        													<td align="center">${list.telnetconfigips}</td>
			        													<td align="center">${list.backup_filename}</td>
			        													<td align="center">${list.backup_type}</td>
			        													<td align="center">${list.backup_time_hou}</td>
			        													<td align="center">${list.warntype}</td>
			        													<td align="center">${list.status}</td>
			        													<td align='center'>
																			<img src="/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('2','${list.id}') alt="右键操作">
																		</td>
			        												</tr>
		        												</c:forEach>-->
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
