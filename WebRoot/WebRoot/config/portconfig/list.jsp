<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.topology.model.IpDistrictMatchConfig"%>
<%@page import="com.afunms.config.model.DistrictConfig"%>
<%@page import="com.afunms.config.dao.DistrictDao"%>
<%@page import="com.afunms.config.model.Portconfig"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>

<%
    String rootPath = request.getContextPath();
    String menuTable = (String)request.getAttribute("menuTable");
    JspPage jp = (JspPage)request.getAttribute("page");
    List list = (List)request.getAttribute("list");
    List ips = (List)request.getAttribute("ips");
%>


<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script type="text/javascript">
            var curpage= <%=jp.getCurrentPage()%>;
            var totalpages = <%=jp.getPageTotal()%>;
            var delAction = "<%=rootPath%>/portconfig.do?action=delete";
            var listAction = "<%=rootPath%>/portconfig.do?action=list";
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
            function doQuery() {
                mainForm.action = "<%=rootPath%>/portconfig.do?action=list";
                mainForm.submit();
            }

            function toEmpty() {
                mainForm.action = "<%=rootPath%>/portconfig.do?action=empty";
                mainForm.submit();
            }
            
            function doFromlastoconfig() {
                mainForm.action = "<%=rootPath%>/portconfig.do?action=fromlasttoconfig";
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
									                	<td class="content-title">资源 >> 设备维护 >> 端口配置</td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									       			</tr>
									        	</table>
		        							</td>
		        						</tr>
		        						<tr>
		        							<td>
		        								<table id="content-body" class="content-body">
                                                    <tr>
                                                        <td colspan="8" align="left">
                                                            <table>
                                                                <tr align="left">
                                                                    <td align="left" class="body-data-title">
                                                                        &nbsp;&nbsp;<B>查询:</B>
                                                                        <SELECT name="ipaddress" style="width=100">
                                                                        	<OPTION value="-1">所有设备</OPTION>
                                                                            <%
                                                                                if(ips != null && ips.size()>0){
                                                                                    for(int k=0;k<ips.size();k++){
                                                                                        String ip = (String)ips.get(k);
                                                                            %> 
                                                                            <OPTION value="<%=ip%>"><%=ip%></OPTION>
                                                                            <%
                                                                                    }
                                                                                }
                                                                            %>        
                                                                        </SELECT>&nbsp;<INPUT type="button" class="formStyle" value="查询" onclick=" return doQuery()">
                                                                    </td>
                                                                    <td class="body-data-title">
                                                                        <INPUT type="button" value="清 空" onclick=" return toEmpty()">
                                                                        <INPUT type="button" value="刷 新" onclick=" return doFromlastoconfig()">&nbsp;&nbsp;&nbsp;
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
		        									<tr >
														<td colspan="8">
															<table width="100%" >
																<tr>
									    							<td bgcolor="#ECECEC" width="80%" align="center" class="body-data-title">
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
       													<td align="center" class="body-data-title"><INPUT type="checkbox" id="checkall" name="checkall" onclick="javascript:chkall()">序号</td>
       													<td align="center" class="body-data-title">设备名称(ip)</td>
       													<td align="center" class="body-data-title">端口索引</td>
       													<td align="center" class="body-data-title">端口名称</td>
       													<td align="center" class="body-data-title">端口应用</td>
       													<td align="center" class="body-data-title">端口Down是否告警</td>
       													<td align="center" class="body-data-title">关键端口</td>
                                                        <td align="center" class="body-data-title">操作</td>
		        									</tr>
		        									<%
                                                    int startRow = jp.getStartRow();
                                                    for(int i = 0; i < list.size(); i++) {
                                                        Portconfig vo = (Portconfig)list.get(i);
                                                        String isSMS = "是";
                                                        if( vo.getSms()==0) {
                                                            isSMS = "否";
                                                        }
                                                        String isImportant = "是";
                                                        if (vo.getImportant() == 0) {
                                                            isImportant = "否";
                                                        }
    									            %>
    									            <tr <%=onmouseoverstyle%>>
    													<td align="center" class="body-data-list"><INPUT type="checkbox" value="<%=vo.getId()%>" name="checkbox"><%=startRow + i%></td>
    													<td align="center" class="body-data-list"><%=vo.getIpaddress()%></td>
    													<td align="center" class="body-data-list"><%=vo.getPortindex()%></td>
    													<td align="center" class="body-data-list"><%=vo.getName()%></td>
    													<td align="center" class="body-data-list" id="linkUse<%=vo.getId()%>"><%=vo.getLinkuse()%></td>
    													<td align="center" class="body-data-list" id="isSMS<%=vo.getId()%>"><%=isSMS%></td>
                                                        <td align="center" class="body-data-list" id="isImportant<%=vo.getId()%>"><%=isImportant%></td>
    													<td align="center" class="body-data-list"><span style='cursor:hand' onClick='window.open("<%=rootPath%>/portconfig.do?action=showedit&id=<%=vo.getId()%>","_blank", "height=400, width= 500, top=200, left= 200")'>
											<img src="<%=rootPath%>/resource/image/editicon.gif" border="0" alt="编辑"/></td>
		        									</tr>
                                                    <% 
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
