<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.topology.model.RemotePingHost"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.topology.model.IpDistrictDetail"%>
<%@page import="com.afunms.config.model.DistrictConfig"%>
<%@page import="com.afunms.portscan.model.PortScanConfig"%>
<%@page import="com.afunms.portscan.model.PortConfig"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>

<%
  String rootPath = request.getContextPath();
  List list = (List)request.getAttribute("list");
  boolean status = (Boolean)request.getAttribute("status");
  String scanTotal = (String)request.getAttribute("scanTotal");
  String isScannedTotal = (String)request.getAttribute("isScannedTotal");
  String unScannedTotal = (String)request.getAttribute("unScannedTotal");
  
  String refresh = (String)request.getAttribute("refresh");
  String times = (String)request.getAttribute("times");
  
  String ipaddress = (String)request.getAttribute("ipaddress");
%>


<html>
	<head>
	
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">    
		<%
		
		   if(status)
		   {   
		%>
		    <script>
		      alert("扫描完成"); 
		    </script>
		<% }else{%>
		<script>
		<%
			if("0".equals(times)){
			%>
			window.location = window.location + "&refresh=refresh" ; 
			<%
			}
		%>
       		
    	</script>
		   <meta http-equiv="refresh" content="5"><!--1分钟刷新一次-->
		<%
		   }
		%>
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
			function savePortScan(){
			    mainForm.action = "<%=rootPath%>/ipDistrictMatch.do?action=savePortScan&refresh=refresh&ipaddress=" + "<%=ipaddress%>";
				mainForm.submit();
			}
			
			function refresh(){
			
			    mainForm.action = "<%=rootPath%>/ipDistrictMatch.do?action=portscan&ipaddress=" + "<%=ipaddress%>";
				mainForm.submit();
			}
			
			function CreateWindow(url){
				msgWindow=window.open(url,"popWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
			}
			
			function createReport_portscan(){
				CreateWindow( "<%=rootPath%>/ipDistrictMatch.do?action=createReport_portscan&refresh=refresh&ipaddress=" + "<%=ipaddress%>" );
			}
		
		</script>
<style>
<!--
body{
background-image: url(${pageContext.request.contextPath}/common/images/menubg_report.jpg);
TEXT-ALIGN: center; 
}
-->
</style>
	</head>

	<body id="body" class="body" onload="initmenu();">
		<form id="mainForm" method="post" name="mainForm">
			<table id="body-container" class="body-container">
				<tr>
					<td class="td-container-main">
						<table>
							<tr>
								<td>
									<table>
										<tr>
											<td>
												<table id="content-header" class="content-header">
								                	<tr>
									                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
									                	<td class="content-title"> 资源 >> IP/MAC 资源 >> 网段管理 >> 端口扫描结果 </td>
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
		        													<td align="center" class="body-data-title">总数:<%=scanTotal%></td>
		        													<td align="center" class="body-data-title">已扫描：<%=isScannedTotal%></td>
		        													<td align="center" class="body-data-title">未扫描：<%=unScannedTotal%></td>
		        													
		        												</tr>
		        												<% 
		        												if(status)
		   { 													%>
		        												<tr>
			        												<td align="center" class="body-data-title"><input type="button" onclick="refresh()" value="重新扫描"></td>
			        												<td align="center" class="body-data-title"></td>
			        												<td class="body-data-title" style="text-align: right;height: 25px">
												                			<a href="#" onclick="createReport_portscan()" >
												                				<img name="selDay1" alt='导出EXCEL' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=18  border="0">导出EXCEL
												                			</a>&nbsp;&nbsp;&nbsp;&nbsp;
											                		</td>
		        												</tr>
		        												<%
		        												}else{
		        												
		        												%>
		        												<tr>
			        												<td align="center" colspan="3" class="body-data-title">正在扫描 请稍后。。。</td>
			        												
		        												</tr>
		        												
		        												<% 
		        												}
		        												%>
		        											</table>
		        										</td>
		        									</tr>
		        									<tr>
		        										<td>
		        											<table>
		        												<tr>
		        													<td align="center" class="body-data-title"><INPUT type="checkbox" id="checkall" name="checkall" onclick="javascript:chkall()">序号</td>
		        													<td align="center" class="body-data-title">IP地址</td>
		        													<td align="center" class="body-data-title">端口号</td>
		        													<td align="center" class="body-data-title">端口名称</td>
		        													<td align="center" class="body-data-title">是否在用</td>
		        													<td align="center" class="body-data-title">扫描时间</td>
		        												</tr>
		        												<%
					        									    if(list!=null&& list.size()>0){
					        									    	int row = 0;
					        									        for(int i = 0 ; i < list.size() ; i++){
					        									            PortScanConfig portScanConfig = (PortScanConfig)list.get(i);
					        									            List isScannedList = portScanConfig.getIsScannedList();
					        									            if(isScannedList!=null&& isScannedList.size()>0){
					        									            	for(int j = 0 ; j < isScannedList.size() ; j++){
					        									            		PortConfig portConfig = (PortConfig)isScannedList.get(j);
					        									            		row = row + 1;
					        									            		
					        									            		String status_str = "否";
					        									            		String status_img = "";
					        									            		if("1".equals(portConfig.getStatus())){
					        									            			status_img = "resource/image/topo/status_ok_1.gif";
					        									            			status_str = "是";
					        									            		}
							        									            %>
							        									            <tr <%=onmouseoverstyle%>>
							        													<td align="center" class="body-data-list"><INPUT type="checkbox" value="<%=portConfig.getId()%>" name="check" onclick="javascript:chkall()"><%=row%></td>
							        													<td align="center" class="body-data-list"><%=portConfig.getIpaddress()%></td>
							        													<td align="center" class="body-data-list"><%=portConfig.getPort()%></td>
							        													<td align="center" class="body-data-list"><%=portConfig.getDescription()%></td>
							        													<td align="center" class="body-data-list"><%
							        													if("1".equals(portConfig.getStatus())){
							        														%>
							        														<img src="<%=rootPath%>/resource/image/topo/status_ok_1.gif"><%=status_str%>
							        														<%
					        									            			}else {
					        									            				%>
					        									            				<%=status_str%>
					        									            				<%
					        									            			}
							        													
							        													%></td>
							        													<td align="center" class="body-data-list"><%=portConfig.getScantime()%></td>
							        													
										        									</tr>
							        									            
							        									            <% 
					        									            	}
					        									            }
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
