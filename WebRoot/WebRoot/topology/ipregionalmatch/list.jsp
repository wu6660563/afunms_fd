<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.topology.model.RemotePingHost"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.topology.model.IpDistrictMatch"%>
<%@page import="com.afunms.topology.model.IpDistrictMatchConfig"%>
<%@page import="com.afunms.config.model.DistrictConfig"%>
<%@page import="com.afunms.config.dao.DistrictDao"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>

<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  List list = (List)request.getAttribute("list");
  
  List districtList = (List)request.getAttribute("districtList");
  JspPage jp = (JspPage)request.getAttribute("page");
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
			var curpage= "<%=jp.getCurrentPage()%>";
  			var totalpages = "<%=jp.getPageTotal()%>";
  			var listAction = "<%=rootPath%>/ipDistrictMatch.do?action=list";
		</script>
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
		<script>
			function refresh(){
				if(window.confirm("同步刷新所需的时间比较长! 并会将此现有信息全部清空! 是否继续?")){
					Ext.MessageBox.wait('数据加载中，请稍后.. '); 
					mainForm.action = "<%=rootPath%>/ipDistrictMatch.do?action=list&refresh=refresh"; 
					mainForm.submit();
				}
				
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
									                	<td class="content-title"> 资源 >> ip 区域匹配 >> ip 区域匹配列表</td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									       			</tr>
									        	</table>
		        							</td>
		        						</tr>
		        						<tr>
		        							<td>
		        								<table id="content-body" class="content-body">
		        									<tr >
														<td class="body-data-title" colspan="7">
															<table width="100%" >
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
														<td colspan="7" style="text-align: right;" class="body-data-title">
															<input  type="button" onclick="refresh()" value="同步刷新">&nbsp;&nbsp;&nbsp;
					        							</td>
					        						</tr>
					        						<tr>
														<td colspan="7" class="body-data-title">
															<table>
																<tr>
																	<td>&nbsp;&nbsp;&nbsp;</td>
																	<%
																	if(districtList!=null||districtList.size()>0){
																			for(int i=0; i < districtList.size() ; i++){
																				DistrictConfig districtConfig = (DistrictConfig)districtList.get(i);
																			%>
																			<td bgcolor="<%=districtConfig.getDescolor()%>">&nbsp;</td>
																			<td><%=districtConfig.getName()%></td>
																			<%
																			}
																	}
																 %>
																 	<td>&nbsp;&nbsp;&nbsp;</td>
																</tr>
															</table>
					        							</td>
					        						</tr>
		        									<tr>
       													<td align="center" class="body-data-title"><INPUT type="checkbox" id="checkall" name="checkall" onclick="javascript:chkall()">序号</td>
       													<td align="center" class="body-data-title">网络设备ip</td>
       													<td align="center" class="body-data-title">服务器ip</td>
       													<td align="center" class="body-data-title">在线状态</td>
       													<td align="center" class="body-data-title">设备所属区域</td>
       													<td align="center" class="body-data-title">当前ip所属区域</td>
       													<td align="center" class="body-data-title">区域是否匹配</td>
		        									</tr>
		        									<%
		        									    if(list!=null&& list.size()>0){
		        									    	
		        									        for(int i = 0 ; i < list.size() ; i++){
		        									        	String isOnline = "离线";
		        									        	String isOnline_img = "resource/image/topo/status6.png";
		        									            IpDistrictMatchConfig ipDistrictMatchConfig = (IpDistrictMatchConfig)list.get(i);
		        									            if("1".equals(ipDistrictMatchConfig.getIsOnline())){
		        									                isOnline = "在线";
		        									                isOnline_img = "resource/image/topo/status_ok_1.gif";
		        									            }
		        									            
		        									            String isMatch_img = "resource/image/topo/alert.gif";
		        									            String isMatch = "否";
		        									            if("1".equals(ipDistrictMatchConfig.getIsMatch())){
		        									            	isMatch = "是";
		        									            	isMatch_img = "resource/image/statusOK.gif";
		        									            }
		        									            
		        									            String orcBgColor ="";
		        									            String orcDistrictName = "";
		        									            
		        									            DistrictDao districtDao = new DistrictDao();
		        									            String oriDistrictId = ipDistrictMatchConfig.getOriginalDistrict();
		        									            if(oriDistrictId!=null&&oriDistrictId.trim().length()>0){
		        									            	DistrictConfig districtConfig1 = (DistrictConfig)districtDao.findByID(oriDistrictId);
		        									            	if(districtConfig1 != null){
		        									            		orcBgColor = districtConfig1.getDescolor();
		        									            		orcDistrictName = districtConfig1.getName();
		        									            	}
		        									            }
		        									            
		        									            String curBgColor = "";
		        									            String curDistrictName = "";
		        									            
		        									            String curDistrictId = ipDistrictMatchConfig.getCurrentDistrict();
		        									            if(curDistrictId!=null&&curDistrictId.trim().length()>0){
		        									            	DistrictConfig districtConfig2 = (DistrictConfig)districtDao.findByID(curDistrictId);
		        									            	if(districtConfig2 != null){
		        									            		curBgColor = districtConfig2.getDescolor();
		        									            		curDistrictName = districtConfig2.getName();
		        									            	}
		        									            }
		        									            
		        									           
		        									            districtDao.close();
		        									            
		        									            
		        									            %>
		        									            <tr <%=onmouseoverstyle%>>
		        													<td align="center" class="body-data-list"><INPUT type="checkbox" value="<%=ipDistrictMatchConfig.getId()%>" name="check" onclick="javascript:chkall()"><%=i+jp.getStartRow()%></td>
		        													<td align="center" class="body-data-list"><%=ipDistrictMatchConfig.getRelateipaddr()%></td>
		        													<td align="center" class="body-data-list"><%=ipDistrictMatchConfig.getNodeIp()%></td>
		        													<td align="center" class="body-data-list"><img src="<%=isOnline_img%>">&nbsp;<%=isOnline %></td>
		        													<td align="center" class="body-data-list" width="10%" bgcolor="<%=orcBgColor%>"><%=orcDistrictName%></td>
		        													<td align="center" class="body-data-list" width="10%" bgcolor="<%=curBgColor%>"><%=curDistrictName%></td>
		        													<td align="center" class="body-data-list"><img src="<%=isMatch_img%>">&nbsp;<%=isMatch%></td>
					        									</tr>
		        									            
		        									            <% 
		        									        }
		        									        
		        									    }
		        									 %>
		        									
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
