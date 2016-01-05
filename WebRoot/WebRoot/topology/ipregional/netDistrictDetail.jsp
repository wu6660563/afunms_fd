<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.topology.model.RemotePingHost"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.topology.model.IpDistrictDetail"%>
<%@page import="com.afunms.topology.model.NetDistrictDetail"%>
<%@page import="com.afunms.config.model.DistrictConfig"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>

<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  List list = (List)request.getAttribute("list");
  DistrictConfig districtConfig = (DistrictConfig)request.getAttribute("districtConfig");
  String districtName = "";
  if(districtConfig!=null){
      districtName = districtConfig.getName();
  }
 // JspPage jp = (JspPage)request.getAttribute("page");
%>


<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
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
			function netDistrictIpDetail(netDistrictId , districtId , ipDistrictId){
		        mainForm.action = "<%=rootPath%>/ipDistrictMatch.do?action=netDistrictIpDetails&netDistrictId="+ netDistrictId +"&districtId="+districtId + "&ipDistrictId=" + ipDistrictId;
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
									                	<td class="content-title"> 资源 >> ip 网段管理 >> <%=districtName%> 地区 网段列表 </td>
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
															<table class="body-data-title" style="text-align: left;">
											                	<tr>
											                		<td>&nbsp;&nbsp;&nbsp;<%=districtName%><td>
												                </tr>
												        	</table>
					        							</td>
					        						</tr>
		        									<tr>
		        										<td>
		        											<table>
		        												<tr>
		        													<td align="center" class="body-data-title"><INPUT type="checkbox" id="checkall" name="checkall" onclick="javascript:chkall()">序号</td>
		        													<td align="center" class="body-data-title">网段名</td>
		        													<td align="center" class="body-data-title">分配ip总数</td>
		        													<td align="center" class="body-data-title">已使用</td>
		        													<td align="center" class="body-data-title">未使用</td>
		        													<td align="center" class="body-data-title">在线总数</td>
		        													<td align="center" class="body-data-title">离线总数</td>
		        												</tr>
		        												<%
		        									    if(list!=null&& list.size()>0){
		        									        for(int i = 0 ; i < list.size() ; i++){
		        									            NetDistrictDetail netDistrictDetail = (NetDistrictDetail)list.get(i);
		        									            
		        									            %>
		        									            <tr <%=onmouseoverstyle%>>
		        													<td align="center" class="body-data-list"><INPUT type="checkbox" value="<%=netDistrictDetail.getId()%>" name="check" onclick="javascript:chkall()"><%=i+1%></td>
		        													<td align="center" class="body-data-list">
		        														<a href="#" title="点击查看详情" onclick="netDistrictIpDetail('<%=netDistrictDetail.getId()%>','<%=netDistrictDetail.getDistrictId()%>','<%=netDistrictDetail.getIpDistrictId()%>')">
		        													<%=netDistrictDetail.getNetDistrictName()%></a></td>
		        													<td align="center" class="body-data-list"><%=netDistrictDetail.getIpTotal()%></td>
		        													<td align="center" class="body-data-list"><%=netDistrictDetail.getUsedTotal()%></td>
		        													<td align="center" class="body-data-list"><%=netDistrictDetail.getUnusedTotal()%></td>
		        													<td align="center" class="body-data-list"><%=netDistrictDetail.getIsOnlineTotal()%></td>
		        													<td align="center" class="body-data-list"><%=netDistrictDetail.getUnOnlineToatal() %></td>
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
