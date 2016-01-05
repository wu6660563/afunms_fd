<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.topology.model.ConnectTypeConfig"%>
<%@page import="com.afunms.config.model.Huaweitelnetconf"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.system.util.*"%>
<%@page import="com.afunms.config.model.ConfiguringDevice"%>


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
			var listAction = "<%=rootPath%>/vpntelnetconf.do?action=passwdList";
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
					onclick="parent.telnetModify()">�޸�����</td>
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
									                	<td class="content-title"> ��Դ >> �豸ά�� >> Զ���豸�����޸��б�</td>
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
																<tr>
																	<td bgcolor="#ECECEC" width="60%">
																	&nbsp;&nbsp;&nbsp;
																		<!-- ip��ַ��<input type="text" size="30" name="ipfindaddress">
																		<input type="button" value="��ѯ" onclick="toFindIp()">-->
																	</td>
																	<td bgcolor="#ECECEC" width="40%" align='right'>
																		<a href="#" onclick="toAdd()">���</a>&nbsp;
																		<a href="#" onclick="toDelete()">ɾ��</a>&nbsp;
																		<a href="#" onclick="addForBatch()">�������</a>&nbsp;
																		<a href="#" onclick="modifypwForBatch()">�����޸�����</a>&nbsp;
																		
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
																			<jsp:param name="curpage" value="<%=jp.getCurrentPage()%>" />
																			<jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>" />
																		</jsp:include>
																	</td>
																</tr>
															</table>
														</td>
													</tr>
		        									<tr>
		        										<td>
		        											<table cellspacing="0" border="1" bordercolor="#ababab">
		        												<tr height=28 style="background:#ECECEC" align="center" class="content-title">
		        													<td align="center"><INPUT type="checkbox" id="checkall" name="checkall" onclick="javascript:chkall()" class=noborder></td>
		        													<td align="center">���</td>
		        													<td align="center" width="20%">IP��ַ</td>
		        													<td align="center">�豸����</td>
		        													<!--<td align="center">���һ�α���ʱ��</td>-->
		        													<!-- <td align="center">b</td> -->
		        													<!--<td align="center" width="15%">VPN�ɼ�״̬</td>-->
		        													<td align="center" width="10%">ͬ��״̬</td>
		        													<td align="center" width="20%">����</td>
		        												</tr>
		        												<%
		        													ConfiguringDevice vo=null;
		        													int startRow = jp.getStartRow();
			        												for (int i = 0; i < list.size(); i++) 
			        												{
			        													vo = (ConfiguringDevice)list.get(i);
																		vo = (ConfiguringDevice) list.get(i);
																		String lastUpdateTime = "û�б����ļ�";
																		if(vo.getLastUpdateTime() != null)
																		{
																			lastUpdateTime = vo.getLastUpdateTime().toString().substring(0,vo.getLastUpdateTime().toString().length()-5);
																		}
																		int vpn=vo.getEnablevpn();
																		String vpnflag;
																		if(vpn==1){
																			vpnflag="������";
																		}else{vpnflag="δ����";}
																		String isSynchronized = "δͬ��";
																		if(vo.getIsSynchronized() == 1)
																		{
																			isSynchronized = "";
																		}
			        											%>
			        													<tr <%=onmouseoverstyle%>>
											        						<td align='center'>
																			<INPUT type="checkbox" class=noborder name="checkbox"
																					value="<%=vo.getId() %>">
																			</td>
																			<td align='center'>
																				<font color='blue'><%=startRow + i%></font>
			        														</td>
			        														<td align='center'><%=vo.getIpaddress()%></td>
			        														<td align='center'><%=vo.getDeviceRender() %></td>
			        														<!-- <td align='center'><%=lastUpdateTime %></td>-->
			        														<!-- <td align='center'>b</td> -->
			        														<!--<td align='center'><%=vpnflag%></td>-->
			        														<td align='center'><%=isSynchronized %></td>
																			<!-- <td align='center'><a href="<%=rootPath%>/vpntelnetconf.do?action=ready_edit&id=<%=vo.getId()%>"><img src="<%=rootPath%>/resource/image/editicon.gif" border="0"/></a></td> -->
																			<td align='center'>
																					<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('2','<%=vo.getId() %>','<%=vo.getIpaddress() %>') alt="�Ҽ�����">
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
