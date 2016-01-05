<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.model.LinkPerformanceDTO"%>
<%@ include file="/include/globe.inc"%>

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
  	
  	String nameImg = "";
  
  	String startIpImg = "";
  
  	String endIpImg = "";
  
  	String uplinkSpeedImg = "";
  
  	String downlinkSpeedImg = "";
  
  	String pingImg = "";
  	
  	String allSpeedRateImg = "";
  	
  	String imgSrc = "";
  	
  
  if("desc".equals(sorttype)){
  	  imgSrc = "/afunms/resource/image/btn_up2.gif";
  }else if("asc".equals(sorttype)){
  	  imgSrc = "/afunms/resource/image/btn_up1.gif";
  }
  
  if("name".equals(field)){
  	  nameImg = "<img src='" + imgSrc + "'>";
  }
  if("startIp".equals(field)){
  	  startIpImg = "<img src='" + imgSrc + "'>";
  }
  
  if("endIp".equals(field)){
  	  endIpImg = "<img src='" + imgSrc + "'>";
  }
  
  if("uplinkSpeed".equals(field)){
  	  uplinkSpeedImg = "<img src='" + imgSrc + "'>";
  }
  
  if("downlinkSpeed".equals(field)){
  	  downlinkSpeedImg = "<img src='" + imgSrc + "'>";
  }
  
  if("ping".equals(field)){
  	  pingImg = "<img src='" + imgSrc + "'>";
  }
  
  if("allSpeedRate".equals(field)){
  	  allSpeedRateImg = "<img src='" + imgSrc + "'>";
  }
%>


<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		
		<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/application/environment/resource/ext3.1/resources/css/ext-all.css" />
		<script type="text/javascript" 	src="<%=rootPath%>/application/environment/resource/ext3.1/adapter/ext/ext-base.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/application/environment/resource/ext3.1/ext-all.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/application/environment/resource/ext3.1/ext-all-debug.js"></script>
		
		
		<script type="text/javascript">
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
		
		<script type="text/javascript">
		
			var curpage= <%=jp.getCurrentPage()%>;
  			var totalpages = <%=jp.getPageTotal()%>;
  			var delAction = "<%=rootPath%>/network.do?action=delete";
  			var listAction = "<%=rootPath%>/linkperformance.do?action=list";
		
			function showSort(fieldValue){  
				var field = document.getElementById('field');
				field.value = fieldValue;
				mainForm.action = "<%=rootPath%>/linkperformance.do?action=list";
     			mainForm.submit();
  			}
  			
  			function showDetail(id){  
				CreateWindow('<%=rootPath%>/topology/network/linkedline.jsp?line='+ id);
  			}
  			
  			function CreateWindow(url){
				msgWindow=window.open(url,'window', 'toolbar=no,height=800,width=850,scrollbars=yes,center=yes,screenY=0');
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
								<td class="td-container-main-content">
									<table id="container-main-content" class="container-main-content">
										<tr>
											<td>
												<table id="content-header" class="content-header">
								                	<tr>
									                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
									                	<td class="content-title"> 资源 >> 性能监控 >> 链路性能一览表 </td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									       			</tr>
									        	</table>
		        							</td>
		        						</tr>
		        						<tr>
		        							<td>
		        								<table id="content-body" class="content-body">
		        									<tr >
														<td >
															<table width="100%" >
																<tr>
									    							<td class="body-data-title" width="80%" align="center">
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
															<table>
					        									<tr>
					        										<input type="hidden" id="field" name="field" value="<%=field%>">
		        													<input type="hidden" id="sorttype" name="sorttype" value="<%=sorttype%>">
			       													<td align="center" class="body-data-title"><INPUT type="checkbox" id="checkall" name="checkall" onclick="javascript:chkall()">序号</td>
			       													<td align="center" class="body-data-title"><a href="#" onclick="showSort('name')">名称<%=nameImg%></td>
			       													<td align="center" class="body-data-title"><a href="#" onclick="showSort('startIp')">起始设备IP<%=startIpImg%></td>
			       													<td align="center" class="body-data-title">起始设备端口</td>
			       													<td align="center" class="body-data-title"><a href="#" onclick="showSort('endIp')">终止设备IP<%=endIpImg%></td>
			       													<td align="center" class="body-data-title">终止设备端口</td>
			       													<td align="center" class="body-data-title"><a href="#" onclick="showSort('uplinkSpeed')">上行流速(KB/秒)<%=uplinkSpeedImg%></td>
			       													<td align="center" class="body-data-title"><a href="#" onclick="showSort('downlinkSpeed')">下行流速(KB/秒)<%=downlinkSpeedImg%></td>
			       													<td align="center" class="body-data-title"><a href="#" onclick="showSort('ping')">可用性(%)<%=pingImg%></td>
			       													<td align="center" class="body-data-title"><a href="#" onclick="showSort('allSpeedRate')">带宽利用率(%)<%=allSpeedRateImg%></td>
					        									</tr>
					        									<%
					        										if(list != null && list.size() > 0){
					        											for(int i = 0; i < list.size() ; i++){
					        												LinkPerformanceDTO linkPerformanceDTO = (LinkPerformanceDTO)list.get(i);
					        												
					        												%>
					        												<tr <%=onmouseoverstyle%>>
						       													<td align="center" class="body-data-list"><INPUT type="checkbox" value="<%=linkPerformanceDTO.getId()%>" id="check" name="check" onclick="javascript:chkall()"><%=i+1%></td>
						       													<td align="center" class="body-data-list"><%=linkPerformanceDTO.getName()%></td>
						       													<td align="center" class="body-data-list"><%=linkPerformanceDTO.getStartNode()%></td>
						       													<td align="center" class="body-data-list"><%=linkPerformanceDTO.getStratIndex()%></td>
						       													<td align="center" class="body-data-list"><%=linkPerformanceDTO.getEndNode()%></td>
						       													<td align="center" class="body-data-list"><%=linkPerformanceDTO.getEndIndex()%></td>
						       													<td align="center" class="body-data-list">
						       														<table>
													                      				<tr>
													                      					<td align="center" width="50%"><%=linkPerformanceDTO.getUplinkSpeed()%></td>
													                      					<td align="center" width="50%"><img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showDetail("<%=linkPerformanceDTO.getId()%>")' width=15></td>
													                      				</tr>
												                      				</table>
												                      			</td>
						       													<td align="center" class="body-data-list">
						       														<table>
													                      				<tr>
													                      					<td align="center" width="50%"><%=linkPerformanceDTO.getDownlinkSpeed()%></td>
													                      					<td align="center" width="50%"><img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showDetail("<%=linkPerformanceDTO.getId()%>" )' width=15></td>
													                      				</tr>
												                      				</table>
												                      			</td>
						       													<td align="center" class="body-data-list">
						       														<table>
													                      				<tr>
													                      					<td align="center" width="50%"><%=linkPerformanceDTO.getPingValue()%></td>
													                      					<td align="center" width="50%"><img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showDetail("<%=linkPerformanceDTO.getId()%>")' width=15></td>
													                      				</tr>
												                      				</table>
												                      			</td>
						       													<td align="center" class="body-data-list">
						       														<table>
													                      				<tr>
													                      					<td align="center" width="50%"><%=linkPerformanceDTO.getAllSpeedRate()%></td>
													                      					<td align="center" width="50%"><img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showDetail("<%=linkPerformanceDTO.getId()%>")' width=15></td>
													                      				</tr>
												                      				</table>
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
