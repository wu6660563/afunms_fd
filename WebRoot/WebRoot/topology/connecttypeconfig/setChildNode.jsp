<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.topology.model.RemotePingNode"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>

<%
  String rootPath = request.getContextPath();
  List list = (List)request.getAttribute("list");
  List remotePingNodeList = (List)request.getAttribute("remotePingNodeList");
  String nodeId = (String)request.getAttribute("nodeId");
%>


<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
		<script type="text/javascript">
			function addChildNode(){
				mainForm.action = "<%=rootPath%>/remotePing.do?action=addChildNode";
				mainForm.submit();
				window.close();
			}
		
			function chkall(){
				var checkall = document.getElementById("checkall");
				var checkboxes = document.getElementsByName("checkbox");
				for(var i = 0 ; i < checkboxes.length; i++){
					var checkbox = checkboxes[i];
					checkbox.checked = checkall.checked;
				}
			}
			
			function closed(){
				window.close();
			}
		</script>
	</head>
	<body id="body" class="body" onload="initmenu();">
		<form id="mainForm" method="post" name="mainForm">
			<input type="hidden" id="nodeId" name="nodeId" value="<%=nodeId%>">
			<table id="body-container" class="body-container">
				<tr>
					<td class="td-container-main">
						<table id="container-main" class="container-main">
							<tr>
								<td class="td-container-main-add">
									<table id="container-main-content" class="container-main-content">
										<tr>
											<td>
												<table id="content-header" class="content-header">
								                	<tr>
									                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
									                	<td class="content-title">应用 >> 设置为远程Ping服务器 >> 添加</td>
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
		        											<table >
		        												<tr>
		        													<td>
		        														<table cellspacing="0" border="1" bordercolor="#ababab">
					        												<tr height=28 style="background:#ECECEC" align="center" class="content-title">
					        													<td><INPUT type="checkbox" id="checkall" name="checkall" onclick="javascript:chkall()">序号</td>
					        													<td>名称</td>
					        													<td>IP地址</td>
					        												</tr>
					        												<%
					        												if(list != null && list.size() > 0 ){
						        												for(int i=0; i < list.size(); i ++){
						        													String check = "";
						        													HostNode hostNode = (HostNode)list.get(i);
						        													if(remotePingNodeList!=null&&remotePingNodeList.size()>0){
						        														for(int j =0 ; j < remotePingNodeList.size(); j++){
						        															RemotePingNode remotePingNode = (RemotePingNode)remotePingNodeList.get(j);
						        															if(String.valueOf(hostNode.getId()).equals(remotePingNode.getChildNodeId())){
						        																check = "checked=\"checked\"";
						        															}
						        														}
						        													}
						        													%>
						        													<tr <%=onmouseoverstyle%>>
						        														<td>
						        															<input type="checkbox" id="<%=hostNode.getId()%>" <%=check%> name="checkbox" value="<%=hostNode.getId()%>">
						        															<%=i+1%>
						        														</td>
						        														<td><%=hostNode.getAlias()%></td>
						        														<td><%=hostNode.getIpAddress()%></td>
						        													</tr>
						        												<%
						        												}
					        												}
					        												%>
					        											</table>
					        										</td>
					        									</tr>
		        												<tr>
																	<TD nowrap colspan="4" align=center>
																	<br><input type="button" value="保 存" style="width:50" onclick="addChildNode()">&nbsp;&nbsp;
																		<input type="reset" style="width:50" value="重 置">&nbsp;&nbsp;
																		<input type="button" style="width:50" value="关 闭" onclick="closed()">
																	</TD>	
																</tr>
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
