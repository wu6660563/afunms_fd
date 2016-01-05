<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.application.model.URLConfig"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>

<%
    String rootPath = request.getContextPath();
    String menuTable = (String)request.getAttribute("menuTable");
    List<URLConfig> list = (List<URLConfig>)request.getAttribute("list");
  
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
  			var listAction = "<%=rootPath%>/url.do?action=list";
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
            function toAdd() {
                mainForm.action = "<%=rootPath%>/url.do?action=add";
                mainForm.submit();
            }
            function toEdit(id) {
                mainForm.action = "<%=rootPath%>/url.do?action=edit&id=" + id;
                mainForm.submit();
            }
            function toDelete() {
                mainForm.action = "<%=rootPath%>/url.do?action=delete";
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
								<td class="td-container-main-content">
									<table id="container-main-content" class="container-main-content">
										<tr>
											<td>
												<table id="content-header" class="content-header">
								                	<tr>
									                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
									                	<td class="content-title">应用 >> 服务管理 >> URL 服务列表</td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									       			</tr>
									        	</table>
		        							</td>
		        						</tr>
		        						<tr>
		        							<td>
		        								<table id="content-body" class="content-body">
                                                    <tr >
                                                        <td>
                                                            <table width="100%" >
                                                                <tr>
                                                                    <td class="body-data-title" width="80%" style="text-align: right;">
                                                                        <a href="#" onclick="toAdd()">添加</a>&nbsp;&nbsp;&nbsp;
									                                    <a href="#" onclick="toDelete()">删除</a>&nbsp;&nbsp;&nbsp;
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
		        									<tr >
														<td>
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
                                                            <table width="100%" >
                                                                <tr>
                   													<td align="center" class="body-data-title"><INPUT type="checkbox" id="checkall" name="checkall" onclick="javascript:chkall()">序号</td>
                   													<td align="center" class="body-data-title">名称</td>
                   													<td align="center" class="body-data-title">路径</td>
                   													<td align="center" class="body-data-title">状态</td>
                   													<td align="center" class="body-data-title">是否监控</td>
                   													<td align="center" class="body-data-title">操作</td>
		        									             </tr>
            		        									 <%
            		        									     if (list != null && list.size() > 0) {
                                                                         int i = -1;
            		        									         for (URLConfig url : list) {
            		        									             i++;
                                                                             String isMonitor = "是";
                                                                             if (url.getMonFlag() == 0) {
                                                                                 isMonitor = "否";
                                                                             }
                                                                             int status = 0;
            		        									             %>
            		        									             <tr <%=onmouseoverstyle%>>
            		        											         <td align="center" class="body-data-list"><INPUT type="checkbox" value="<%=url.getId()%>" name="checkbox"><%=i+jp.getStartRow()%></td>
            		        													 <td align="center" class="body-data-list"><%=url.getName()%></td>
            		        													 <td align="center" class="body-data-list"><%=url.getUrl()%></td>
            		        													 <td align="center" class="body-data-list"><img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(status)%>" border="0"></td>
            		        													 <td align="center" class="body-data-list"><%=isMonitor%></td>
            		        													 <td align="center" class="body-data-list"><img src="<%=rootPath%>/resource/image/editicon.gif" onclick="toEdit('<%=url.getId()%>')" title="编辑" alt="编辑" style="cursor: hand;"></td>
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
