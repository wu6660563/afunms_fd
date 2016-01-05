<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.model.ManageXml"%>
<%@page import="com.afunms.config.dao.BusinessDao"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.afunms.config.model.Business"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.alarm.service.NodeAlarmService"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>


<%
	String rootPath = request.getContextPath();
	//String menuTable = (String)request.getAttribute("menuTable");
	
	List list = (List)request.getAttribute("list");
	Hashtable nodeDependListHashtable = (Hashtable)request.getAttribute("nodeDependListHashtable");
		
	String flag = (String)request.getAttribute("flag");
	
	String bid = (String)request.getAttribute("bid");
	
	List allBidList = null;
	BusinessDao dao = new BusinessDao();
	try {
		allBidList = dao.loadAll();
	} catch (Exception e) {
	
	} finally {
		dao.close();
	}
	if(allBidList == null){
		allBidList = new ArrayList();
	}
  
%>
	
	


<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		
		<script language="JavaScript" type="text/javascript">
			function showViewNode(viewId){
				if(viewId || viewId == 0){
					mainForm.action = "<%=rootPath%>/businessview.do?action=showViewNode&bid=<%=bid%>&viewId="+viewId;
					mainForm.submit();
				} else {
					alert("请选择视图！");
				}
			}
		</script>
		
	</head>
	<body id="body" class="body" leftmargin="0" topmargin="0">
		<form id="mainForm" method="post" name="mainForm">
			<input type="hidden" id="flag" name="flag" value="<%=flag%>">
			<table id="body-container" class="body-container">
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
									                	<td class="content-title"> &nbsp; 业务视图  </td>
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
		        													<td class="body-data-title">名称</td> 
                                                                    <td class="body-data-title">状态</td> 
		        													<td class="body-data-title">资源数</td> 
		        												</tr>
		        												<%
		        													if(list != null) {
                                                                        NodeAlarmService nodeAlarmService = new NodeAlarmService();
		        														for(int i = 0 ; i < list.size(); i++){
		        														ManageXml manageXml = (ManageXml)list.get(i);
		        														String manageXmlBid = manageXml.getBid();
		        														
		        														List nodeDependList = (List)nodeDependListHashtable.get(manageXml);
		        														String size = "0";
		        														if(nodeDependList != null){
		        															size = nodeDependList.size() + "";
		        														}
                                                                        int maxAlarmLevel = nodeAlarmService.getMaxAlarmLevel(nodeDependList);
		        												%>
				        												<tr>
				        													<td class="body-data-list" align="center"><a href="#" onclick="showViewNode('<%=manageXml.getId()%>')"><%=manageXml.getTopoName()%></td> 
				        													<td class="body-data-list" align="center"><img src="<%=rootPath + "/resource/" + NodeHelper.getCurrentStatusImage(maxAlarmLevel)%>"></td>
                                                                            <td class="body-data-list" align="center"><%=size%></td> 
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
