<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.Hashtable"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.system.util.*"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  List list = (List)request.getAttribute("list");
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
		
		<script type="text/javascript">
			var listAction = "<%=rootPath%>/vpntelnetconf.do?action=ready_multi_audit";
	  		var delAction = "<%=rootPath%>/vpntelnetconf.do?action=delete";
			var curpage= <%=jp.getCurrentPage()%>;
  			var totalpages = <%=jp.getPageTotal()%>;
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
			
			}
		</script>
		<script language="JavaScript">

			//��������
			var node="";
			var ipaddress="";
			var operate="";
			/**
			*���ݴ����id��ʾ�Ҽ��˵�
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
			        popMenu(itemMenu,100,"11111");
			    }
			    event.returnValue=false;
			    event.cancelBubble=true;
			    return false;
			}
			/**
			*��ʾ�����˵�
			*menuDiv:�Ҽ��˵�������
			*width:����ʾ�Ŀ��
			*rowControlString:�п����ַ�����0��ʾ����ʾ��1��ʾ��ʾ���硰101�������ʾ��1��3����ʾ����2�в���ʾ
			*/
			function popMenu(menuDiv,width,rowControlString)
			{
			    //���������˵�
			    var pop=window.createPopup();
			    //���õ����˵�������
			    pop.document.body.innerHTML=menuDiv.innerHTML;
			    var rowObjs=pop.document.body.all[0].rows;
			    //��õ����˵�������
			    var rowCount=rowObjs.length;
			    //alert("rowCount==>"+rowCount+",rowControlString==>"+rowControlString);
			    //ѭ������ÿ�е�����
			    for(var i=0;i<rowObjs.length;i++)
			    {
			        //������ø��в���ʾ����������һ
			        var hide=rowControlString.charAt(i)!='1';
			        if(hide){
			            rowCount--;
			        }
			        //�����Ƿ���ʾ����
			        rowObjs[i].style.display=(hide)?"none":"";
			        //������껬�����ʱ��Ч��
			        rowObjs[i].cells[0].onmouseover=function()
			        {
			            this.style.background="#397DBD";
			            this.style.color="white";
			        }
			        //������껬������ʱ��Ч��
			        rowObjs[i].cells[0].onmouseout=function(){
			            this.style.background="#F1F1F1";
			            this.style.color="black";
			        }
			    }
			    //���β˵��Ĳ˵�
			    pop.document.oncontextmenu=function()
			    {
			            return false; 
			    }
			    //ѡ���Ҽ��˵���һ��󣬲˵�����
			    pop.document.onclick=function()
			    {
			        pop.hide();
			    }
			    //��ʾ�˵�
			    pop.show(event.clientX-1,event.clientY,width,rowCount*25,document.body);
			    return true;
			}
			
			function edit()
			{
				//window.open("<%=rootPath%>/tool/ping.jsp?ipaddress="+ipaddress,"oneping", "height=400, width= 500, top=300, left=100");
				//window.open('/nms/netutil/ping.jsp?ipaddress='+ipaddress);
				mainForm.action="<%=rootPath%>/vpntelnetconf.do?action=ready_edit&id="+node;
				mainForm.submit();
			}
			
			function backup()
			{
				window.open("<%=rootPath%>/vpntelnetconf.do?action=readyBackupConfig&&id="+node+"&ipaddress="+ipaddress,"oneping", "height=400, width= 500, top=300, left=100,scrollbars=yes");
				//window.open('<%=rootPath%>/network.do?action=telnet&ipaddress='+ipaddress,'onetelnet', 'height=0, width= 0, top=0, left= 0');
				//window.open('/nms/netutil/tracerouter.jsp?ipaddress='+ipaddress);
			}	
			function setupcfg()
			{
				window.open("<%=rootPath%>/vpntelnetconf.do?action=readySetupConfig&&id="+node+"&ipaddress="+ipaddress,"oneping", "height=400, width= 800, top=300, left=100,scrollbars=yes");
				//window.open('<%=rootPath%>/network.do?action=telnet&ipaddress='+ipaddress,'onetelnet', 'height=0, width= 0, top=0, left= 0');
			}
			function telnetModify()
			{
				window.open("<%=rootPath%>/vpntelnetconf.do?action=readyTelnetModify&&id="+node+"&ipaddress="+ipaddress,"oneping", "height=400, width= 800, top=300, left=100,scrollbars=yes");
			}
			function uploadCfgFile()
			{
				window.open("<%=rootPath%>/vpntelnetconf.do?action=readyuploadCfgFile&&id="+node+"&ipaddress="+ipaddress,"oneping", "height=400, width= 800, top=300, left=100,scrollbars=yes");
			}
		</script>
		<script type="text/javascript">
		
			//var curpage= <%=jp.getCurrentPage()%>;
  			//var totalpages = <%=jp.getPageTotal()%>;
  			//var delAction = "<%=rootPath%>/network.do?action=delete";
  			//var listAction = "<%=rootPath%>/network.do?action=monitornodelist";
  			
			function toAdd(){
				mainForm.action = "<%=rootPath%>/vpntelnetconf.do?action=ready_add";
			    mainForm.submit();
			}
			function addForBatch()
			{
				mainForm.action = "<%=rootPath%>/vpntelnetconf.do?action=ready_addForBatch";
				mainForm.submit();
			}
			function modifypwForBatch()
			{
				mainForm.action = "<%=rootPath%>/vpntelnetconf.do?action=ready_multi_modify";
				mainForm.submit();
			}
			function backupForBatch()
			{
				mainForm.action = "<%=rootPath%>/vpntelnetconf.do?action=ready_backupForBatch";
				mainForm.submit();
			}
			function deployCfgForBatch()
			{
				mainForm.action = "<%=rootPath%>/vpntelnetconf.do?action=ready_deployCfgForBatch";
				mainForm.submit();
			}
			function modifyForBatch()
			{
				var check_obj = document.getElementsByName('checkbox');    
				var temp_check = false;    
				for(var i = 0; i < check_obj.length; i ++)
				{    
					if(check_obj[i].checked)
					{    
						temp_check = true;    
						break;
					}    
				}    
				if(temp_check == false)
				{    
					alert('���ٹ�ѡһ��');    
					return false;    
				}
				//window.open("<%=rootPath%>/vpntelnetconf.do?action=ready_multi_modify","portScanWindow","width=800,height=400,scrollbars=yes,resizable=yes")
				
				mainForm.action = "<%=rootPath%>/vpntelnetconf.do?action=ready_multi_modify";
				mainForm.submit();
			}
			function modifypwForAudit()
			{
				mainForm.action = "<%=rootPath%>/vpntelnetconf.do?action=ready_multi_audit";
				mainForm.submit();
			}
			function synchronizedData()
			{
				mainForm.action = "<%=rootPath%>/vpntelnetconf.do?action=synchronizeData";
				mainForm.submit();
			}
			function doCancelManage(){  
     				mainForm.action = "<%=rootPath%>/network.do?action=cancelmanage";
     				mainForm.submit();
	  		}
	  		
	  		function toDelete(){  
     				mainForm.action = "<%=rootPath%>/vpntelnetconf.do?action=delete";
     				mainForm.submit();
	  		}
	  		function toFindIp()
			{
			     mainForm.action = "<%=rootPath%>/vpntelnetconf.do?action=findip";
			     mainForm.submit();
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
			
  			function doFind(){  
			     if(mainForm.value.value=="")
			     {
			     	alert("�������ѯ����");
			     	return false;
			     }
			     mainForm.action = "<%=rootPath%>/vpntelnetconf.do?action=queryByCondition";
			     mainForm.submit();
			  }
  			
		</script>

	</head>
	<body id="body" class="body" onload="initmenu();">
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
								<td class="td-container-main-add">
									<table id="container-main-add" class="container-main-add">
										<tr>
											<td>
												<table id="add-content" class="add-content">
													<tr>
														<td>
															<table id="add-content-header"
																class="add-content-header">
																<tr>
																	<td align="left" width="5">
																		<img src="<%=rootPath%>/common/images/right_t_01.jpg"
																			width="5" height="29" />
																	</td>
																	<td class="add-content-title">
																		���������Ϣ
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
															<table width="100%" cellpadding="0" cellspacing="1">
																<tr>
																	<td bgcolor="#ECECEC" width="80%" align="center">
																		<jsp:include page="../../common/page.jsp">
																			<jsp:param name="curpage"
																				value="<%=jp.getCurrentPage()%>" />
																			<jsp:param name="pagetotal"
																				value="<%=jp.getPageTotal()%>" />
																		</jsp:include>
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td >
															<table cellspacing="0" border="1" bordercolor="#ababab">
																<tr>
																	<td bgcolor="#ECECEC" width="60%">
																		&nbsp;
																		<B>��&nbsp;&nbsp;ѯ:</B>
							        									<SELECT name="key" style="width=100">
																			<OPTION value="username" selected>
																				�û�
																			</OPTION>
																			<OPTION value="ip">
																				IP��ַ
																			</OPTION>
																		</SELECT>&nbsp;<b>=</b>&nbsp; 
								          								<INPUT type="text" name="value" width="15" class="formStyle">
								          								<INPUT type="button" class="formStyle" value="��ѯ" onclick=" return doFind()">
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
																		<table cellspacing="0" border="1" bordercolor="#ababab">
																			<tr height=28 style="background:#ECECEC" align="center" class="content-title">
																				<TD nowrap align="center">
																					���&nbsp;
																				</TD>
																				<TD nowrap align="center">
																					�û�&nbsp;
																				</TD>
																				<TD nowrap align="center">
																					IP&nbsp;
																				</TD>
																				<TD nowrap align="center">
																					������&nbsp;
																				</TD>
																				<td align="center">
																					������&nbsp;
																				</td>
																				<td align="center">
																					�޸�ʱ��&nbsp;
																				</td>
																			</tr>
																			<c:forEach items="${list}" var="had" varStatus="status">
																				<tr>
																					<td align='center'>
																						${status.index+1}
					        														</td>
																					<td align='center'>
																						${had.username}
																					</td>
																					<td align='center'>
																						${had.ip}
																					</td>
																					<td align='center'>
																						*
																					</td>
																					<td align='center'>
																						*
																					</td>
																					<td align='center'>
																						${had.dotime}
																					</td>
																				</tr>
																			</c:forEach>
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
																		<table width="100%" border="0" cellspacing="0"
																			cellpadding="0">
																			<tr>
																				<td align="left" valign="bottom">
																					<img
																						src="<%=rootPath%>/common/images/right_b_01.jpg"
																						width="5" height="12" />
																				</td>
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
		</form>
	</body>
</HTML>