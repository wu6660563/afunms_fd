<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Vector"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.topology.model.MonitorHostDTO"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.topology.model.MonitorNetDTO"%>
<%@page import="com.afunms.topology.model.MonitorNodeDTO"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.common.util.ShareData" %>
<%@page import="com.afunms.config.dao.IpAliasDao" %>
<%@page import="com.afunms.config.model.IpAlias" %>

<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
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
  
  
  //System.out.println("======type===========" + sorttype);
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
		
		<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />
		<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
		<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
		<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
		<script src="<%=rootPath%>/include/AC_OETags.js" language="javascript"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>

		<script type="text/javascript">
		 	
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
			        popMenu(itemMenu,100,"1111");
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
			function detail()
			{
			    location.href="<%=rootPath%>/detail/dispatcher.jsp?id=net"+node;
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
				window.open('<%=rootPath%>/network.do?action=telnet&ipaddress='+ipaddress,'onetelnet', 'height=0, width= 0, top=0, left= 0');
				//window.open('/nms/netutil/tracerouter.jsp?ipaddress='+ipaddress);
			}	
			function clickMenu()
			{
			}
		</script>
		<script type="text/javascript">
		
			var curpage= <%=jp.getCurrentPage()%>;
  			var totalpages = <%=jp.getPageTotal()%>;
  			var delAction = "<%=rootPath%>/network.do?action=delete";
  			var listAction = "<%=rootPath%>/network.do?action=monitornodelist";
  			
			function toAdd(){
				mainForm.action = "<%=rootPath%>/network.do?action=ready_add";
				mainForm.submit();
			}
			  
			function doCancelManage(){  
     				mainForm.action = "<%=rootPath%>/network.do?action=cancelmanage";
     				mainForm.submit();
	  		}
	  		
	  		function doDelete(){  
     				mainForm.action = "<%=rootPath%>/network.do?action=delete";
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
  			
  			//�����豸��ip��ַ
			function modifyIpAliasajax(ipaddress){
				var t = document.getElementById('ipalias'+ipaddress);
				var ipalias = t.options[t.selectedIndex].text;//��ȡ�������ֵ
				$.ajax({
						type:"GET",
						dataType:"json",
						url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=modifyIpAlias&ipaddress="+ipaddress+"&ipalias="+ipalias,
						success:function(data){
							window.alert("�޸ĳɹ���");
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
	<body id="body" class="body" onload="initmenu();">
		<!-- ��������������Ҫ��ʾ���Ҽ��˵� -->
		<div id="itemMenu" style="display: none";>
		<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
			style="border: thin;font-size: 12px" cellspacing="0">
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.detail()">�鿴״̬</td>
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
					onclick="parent.clickMenu()">�ر�</td>
			</tr>
		</table>
		</div>
		<!-- �Ҽ��˵�����-->
		<form id="mainForm" method="post" name="mainForm">
			<input type=hidden id="flag" name="flag" value="<%=flag%>">
			<input type=hidden id="category" name="category" value="<%=category%>">
			<input type="hidden" id="field" name="field" value="<%=field%>">
		    <input type="hidden" id="sorttype" name="sorttype" value="<%=sorttype%>">
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
									                	<td class="content-title">&nbsp;��Դ >> ���ܼ��� >> ���Ӷ���һ�� >> �豸�б� </td>
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
							    										<jsp:include page="../../common/page.jsp">
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
																		<B>��&nbsp;&nbsp;ѯ:</B>
							        									<SELECT name="key"> 
								          									<OPTION value="alias" selected>����</OPTION>
								          									<OPTION value="ip_address">IP��ַ</OPTION>
								          									<OPTION value="sys_oid">ϵͳOID</OPTION>          
								          									<OPTION value="type">�ͺ�</OPTION>
								          								</SELECT>&nbsp;<b>=</b>&nbsp; 
								          								<INPUT type="text" name="value" width="15" class="formStyle">
								          								<INPUT type="button" class="formStyle" value="��    ѯ" onclick=" return doSearch()">
								          							</td>            
																	<td class="body-data-title" style="text-align: right;">
																		<INPUT type="button" value="��    ��" onclick=" return toAdd()">
																		<INPUT type="button" value="ɾ    ��" onclick=" return doDelete()">
																		<INPUT type="button" value="ȡ������" onclick=" return doCancelManage()">
														                <INPUT type="button" value="ˢ    ��" onclick=" return doQuery()">
														                &nbsp;&nbsp;&nbsp;
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
		        													<td align="center" class="body-data-title" width="5%"><INPUT type="checkbox" id="checkall" name="checkall" onclick="javascript:chkall()">���</td>
		        													<td align="center" class="body-data-title" width="11%"><a href="#" onclick="showSort('name')">����</a><%=nameImg%></td>
		        													<td align="center" class="body-data-title" width="5%"><a href="#" onclick="showSort('category')">����</a><%=categoryImg%></td>
		        													<td align="center" class="body-data-title" width="14%"><a href="#" onclick="showSort('ipaddress')">IP��ַ</a><%=ipaddressImg%></td>
		        													<td align="center" class="body-data-title" width="6%"><a href="#" onclick="showSort('ping')">������</a><%=pingImg%></td>
		        													<td align="center" class="body-data-title" width="10%"><a href="#" onclick="showSort('cpu')">CPU(%)</a><%=cpuImg%></td>
		        													<td align="center" class="body-data-title" width="10%"><a href="#" onclick="showSort('memory')">�ڴ�(%)</a><%=memoryImg%></td>
		        													<td align="center" class="body-data-title" width="12%"><a href="#" onclick="showSort('inutilhdx')">��������(KB/S)</a><%=inutilhdxImg%></td>
		        													<td align="center" class="body-data-title" width="12%"><a href="#" onclick="showSort('oututilhdx')">�������(KB/S)</a><%=oututilhdxImg%></td>
		        													<td align="center" class="body-data-title" width="8%">�ӿ�����</td>
		        													<td align="center" class="body-data-title" width="6%">�ɼ���ʽ</td>
		        													<td align="center" class="body-data-title" width="5%">����</td>
		        												</tr>
		        												<%
					        									    if(list!=null&& list.size()>0){
					        									        for(int i = 0 ; i < list.size() ; i++){
					        									        	MonitorNodeDTO monitorNodeDTO = (MonitorNodeDTO)list.get(i);
					        									        	
					        									        	String cpuUsed = monitorNodeDTO.getCpuValue();
					        									        	String cpuUnUsed = String.valueOf(100 - Double.valueOf(cpuUsed));

					        									        	String memeryUsed = monitorNodeDTO.getMemoryValue();
					        									        	String memeryUnUsed = String.valueOf(100 - Double.valueOf(memeryUsed));
					        									        	
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
					        									            %>
					        									            <tr <%=onmouseoverstyle%>>
						        									            <td align="center" class="body-data-list"><INPUT type="checkbox" id="checkbox" name="checkbox" value="<%=monitorNodeDTO.getId()%>"><%=jp.getStartRow()+i %></td>
					        													<td align="left" class="body-data-list"><img src="<%=rootPath%>/resource/image/topo/<%=statusImg%>">&nbsp;
					        													<!--<a href="<%=rootPath%>/detail/dispatcher.jsp?id=net<%=monitorNodeDTO.getId()%>">-->
					        													<a href="<%=rootPath%>/dispatcher.do?action=dispatcher&nodeid=<%=monitorNodeDTO.getId()%>&type=<%=monitorNodeDTO.getType()%>&subtype=<%=monitorNodeDTO.getSubtype()%>"><%=monitorNodeDTO.getAlias()%></a></td>
					        													<td align="center" class="body-data-list"><%=monitorNodeDTO.getCategory()%></td>
					        													<%
					        															//Host host = (Host)PollingEngine.getInstance().getNodeByID(monitorNodeDTO.getId());
					        															//Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
																						//if(ipAllData != null){
					        																//java.util.Vector vector = new java.util.Vector();
					        																//vector = (Vector)ipAllData.get("ipmac");
					        																//System.out.println(vector.size());
						        															IpAliasDao ipdao = new IpAliasDao();
    																						 List iplist = ipdao.loadByIpaddress(monitorNodeDTO.getIpAddress());
    																						 if(iplist == null)iplist = new ArrayList();
					        																ipdao.close();
					        														%>
					        													<td align="center" class="body-data-list">
					        														<table width="100%">
					        															<tr width="100%">
					        																<td align="left" width="40%">
						        																<select style="width:110px;" name="ipalias<%=monitorNodeDTO.getIpAddress() %>">
						        																	<option selected><%=monitorNodeDTO.getIpAddress() %></option>
								        															<%
								        															ipdao = new IpAliasDao();
									                      											 IpAlias ipalias = ipdao.getByIpAndUsedFlag(monitorNodeDTO.getIpAddress(),"1");
									                      											 ipdao.close();
								        															for(int j=0 ;j<iplist.size() ; j++){
			            																					IpAlias vo = (IpAlias)iplist.get(j); %>
			            																					
								        																	<option <%if(ipalias != null && ipalias.getAliasip().equals(vo.getAliasip())){ %>selected<%} %>><%=vo.getAliasip() %></option>
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
													                      					<td align="center" width="50%"><%=monitorNodeDTO.getPingValue()%>%</td>
													                      					<td align="center" width="50%"><img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showPing("<%=monitorNodeDTO .getId()%>" , "<%=monitorNodeDTO.getIpAddress()%>")' width=15></td>
													                      				</tr>
												                      				</table>
					        													</td>
					        													<td align="center" class="body-data-list">
					        														<table>
													                      				<tr>
													                      					<td><%=monitorNodeDTO.getCpuValue()%>%</td>
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
													                      					<td><%=monitorNodeDTO.getMemoryValue()%>%</td>
													                      					<td width=150>
													                      						<table border=1 height=15 width="100%" bgcolor=#ffffff>
																                      				<tr>
																                      					<td width="<%=memeryUsed%>%" bgcolor="<%=memeryValueColor%>"></td>
																                      					<td width="<%=memeryUnUsed%>%" bgcolor=#ffffff></td>
																                      				</tr>
															                      				</table>
													                      					</td>
													                      					<td><img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showMemery("<%=monitorNodeDTO .getId()%>" , "<%=monitorNodeDTO.getIpAddress()%>")' width=15></td>
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
					        													<td align="center" class="body-data-list">
																					<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('2','<%=monitorNodeDTO.getId()%>','<%=monitorNodeDTO.getIpAddress()%>') alt="�Ҽ�����">
																				</td>
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
