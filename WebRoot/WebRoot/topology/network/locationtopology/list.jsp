<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.config.model.Portconfig"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.topology.model.XmlNodeStandardVo"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>

<%
    String rootPath = request.getContextPath();
    String menuTable = (String)request.getAttribute("menuTable");
    List list = (List)request.getAttribute("list");
    List xmlNodeList = (List)request.getAttribute("xmlNodeList");
%>


<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/js/jquery.min.js"></script>
		<script type="text/javascript">
			var show = true;
			var hide = false;
			
			$(document).ready(function(){
				$("#checkall").click(function(){
					 $("input[name='checklist']").each(function () {  
                    	$(this).attr("checked", !$(this).attr("checked"));  
               		});  
 				});
			});
			
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
			
			function toAddMulti(){
				var nodeIds = "";
				var types = "";
				var subtypes = "";
				
				var arrChk = $("input[name='checklist']:checked");
				if(arrChk.length == 0){
					alert("未选择设备，请选择！");
					return;
				}
     			$(arrChk).each(function(){
     				var strs = this.value.split(",");
        			nodeIds += strs[0] + ",";
        			types += strs[1] + ",";
        			subtypes += strs[2] + ",";
     			});
     			window.self.location = "<%=rootPath%>/xmlnode.do?action=getTopoList&nodeIds="+nodeIds+"&types="+types+"&subtypes="+subtypes;
			}
			
			function toAdd(nodeId,type,subtype){
				window.self.location = "<%=rootPath%>/xmlnode.do?action=getTopoList&nodeIds="+nodeId+"&types="+type+"&subtypes="+subtype;
			}
			
			function query(){
				var ipaddress =  $("#ipaddress").val();
				window.self.location = "<%=rootPath%>/xmlnode.do?action=list&ipaddress="+ipaddress;
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
									                	<td class="content-title">资源 >> 设备维护 >> 设备默认拓扑图列表 </td>
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
                                                                        &nbsp;&nbsp;<B>查 询:</B>
                                                                        <input name="ipaddress" type="text" id="ipaddress" value="">
                                                                        &nbsp;<INPUT type="button" class="formStyle" value="查 询" onclick="query()">
                                                                    </td>
                                                                    <td class="body-data-title">
                                                                        <INPUT type="button" value="设 置" onclick="toAddMulti()">
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
		        									<tr>
       													<td align="center" class="body-data-title"><INPUT type="checkbox" id="checkall" name="checkall">序号</td>
       													<td align="center" class="body-data-title">设备名称</td>
       													<td align="center" class="body-data-title">IP地址</td>
       													<td align="center" class="body-data-title">类型</td>
       													<td align="center" class="body-data-title">子类型</td>
       													<td align="center" class="body-data-title">拓扑图名称</td>
                                                        <td align="center" class="body-data-title">设置拓扑图</td>
		        									</tr>
		        									<%
                                                    int startRow = 1;
                                                    for(int i = 0; i < list.size(); i++) {
                                                    	NodeDTO vo = (NodeDTO)list.get(i);
    									            %>
    									            <tr <%=onmouseoverstyle%>>
    													<td align="center" class="body-data-list"><INPUT type="checkbox" value="<%=vo.getNodeid()%>,<%=vo.getType()%>,<%=vo.getSubtype()%>" name="checklist"><%=startRow + i%></td>
    													<td align="center" class="body-data-list"><%=vo.getName()%></td>
    													<td align="center" class="body-data-list"><%=vo.getIpaddress()%></td>
    													<td align="center" class="body-data-list"><%=vo.getType()%></td>
    													<td align="center" class="body-data-list"><%=vo.getSubtype()%></td>
    													<%
    													String topoName = "";
    													for(int j = 0;j < xmlNodeList.size();j++){
    														XmlNodeStandardVo xmlNodeVo = (XmlNodeStandardVo) xmlNodeList.get(j);
    														if(xmlNodeVo.getNodeId().equals(vo.getNodeid())){
    															topoName = xmlNodeVo.getTopoName();
    															break;
    														}
    													}
    													%>
    													<td align="center" class="body-data-list"><%=topoName%></td>
    													<td align="center" class="body-data-list"><span style='cursor:hand' onClick='toAdd("<%=vo.getNodeid()%>","<%=vo.getType()%>","<%=vo.getSubtype()%>")'>
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
