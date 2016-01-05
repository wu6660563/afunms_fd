<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.topology.model.RemotePingHost"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.topology.model.IpDistrictMatch"%>
<%@page import="com.afunms.topology.model.IpDistrictMatchConfig"%>
<%@page import="com.afunms.config.model.DistrictConfig"%>
<%@page import="com.afunms.config.dao.DistrictDao"%>
<%@page import="com.database.NMSMonitoring"%>
<%@page import="com.database.ConnectionStackTrace"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>

<%
    String rootPath = request.getContextPath();
    String menuTable = (String)request.getAttribute("menuTable");
    NMSMonitoring monitoring = NMSMonitoring.getInstance();
  
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
									    							<td bgcolor="#ECECEC" width="50%" align="center">当前打开所有的连接数</td>
                                                                    <td bgcolor="#ECECEC" width="50%" align="center"><%=monitoring.getOpenedConnectionNum()%></td>
			        											</tr>
                                                                <tr>
                                                                    <td bgcolor="#ECECEC" width="50%" align="center">当前关闭所有的连接数</td>
                                                                    <td bgcolor="#ECECEC" width="50%" align="center"><%=monitoring.getClosedConnectionNum()%></td>
                                                                </tr>
                                                                <tr>
                                                                   <td bgcolor="#ECECEC" width="50%" align="center">当前未关闭所有的连接数</td>
                                                                    <td bgcolor="#ECECEC" width="50%" align="center"><%=monitoring.getCurrUnclosedConnection()%></td>
                                                                </tr>
                                                                <tr>
                                                                    <td bgcolor="#ECECEC" width="50%" align="center">当前Map中所有的连接总数</td>
                                                                    <td bgcolor="#ECECEC" width="50%" align="center"><%=monitoring.getCurrMapSize()%></td>
                                                                </tr>
                                                                <tr>
                                                                    <td bgcolor="#ECECEC" width="50%" align="center">当前采集的线程总数</td>
                                                                    <td bgcolor="#ECECEC" width="50%" align="center"><%=monitoring.getGCCurrentThreadNum()%></td>
                                                                </tr>
                                                                <tr>
                                                                    <td bgcolor="#ECECEC" width="50%" align="center">当前采集的线程繁忙总数</td>
                                                                    <td bgcolor="#ECECEC" width="50%" align="center"><%=monitoring.getGCCurrentThreadsBusyNum()%></td>
                                                                </tr>
                                                                <tr>
                                                                    <td bgcolor="#ECECEC" width="50%" align="center">当前采集的待处理线程总数</td>
                                                                    <td bgcolor="#ECECEC" width="50%" align="center"><%=monitoring.getGCCurrentThreadsBufferNum()%></td>
                                                                </tr>
                                                                <tr>
                                                                    <td bgcolor="#ECECEC" width="50%" align="center">当前入库的线程总数</td>
                                                                    <td bgcolor="#ECECEC" width="50%" align="center"><%=monitoring.getDBCurrentThreadNum()%></td>
                                                                </tr>
                                                                <tr>
                                                                    <td bgcolor="#ECECEC" width="50%" align="center">当前入库的线程繁忙总数</td>
                                                                    <td bgcolor="#ECECEC" width="50%" align="center"><%=monitoring.getDBCurrentThreadsBusyNum()%></td>
                                                                </tr>
                                                                <tr>
                                                                    <td bgcolor="#ECECEC" width="50%" align="center">当前入库的待处理线程总数</td>
                                                                    <td bgcolor="#ECECEC" width="50%" align="center"><%=monitoring.getDBCurrentThreadsBufferNum()%></td>
                                                                </tr>
															</table>
														</td>
													</tr> 
		        									<tr>
       													<td align="center" class="body-data-title"><INPUT type="checkbox" id="checkall" name="checkall" onclick="javascript:chkall()">序号</td>
       													<td align="center" class="body-data-title">id</td>
       													<td align="center" class="body-data-title">线程栈追踪</td>
       													<td align="center" class="body-data-title">时间</td>
		        									</tr>
		        									<%
                                                        List<ConnectionStackTrace> list = monitoring.getList();
		        									    if(list!=null&& list.size()>0){
		        									        for(int i = 0 ; i < list.size() ; i++){
                                                                ConnectionStackTrace connectionStackTrace = list.get(i);
                                                                Thread thread = connectionStackTrace.getThread();
                                                                StackTraceElement[] stackTraceElements = thread.getStackTrace();
                                                                String statckTrace = "";
                                                                for (int j = 0 ; j < stackTraceElements.length; j++) {
                                                                    StackTraceElement stackTraceElement = stackTraceElements[j];
                                                                    statckTrace += stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() + ";";
                                                                }
                                                                
		        									            %>
		        									            <tr <%=onmouseoverstyle%>>
		        													<td align="center" class="body-data-list"><INPUT type="checkbox" value="<%=connectionStackTrace.getId()%>" name="check" onclick="javascript:chkall()"><%=i%></td>
		        													<td align="center" class="body-data-list"><%=connectionStackTrace.getId()%></td>
		        													<td align="center" class="body-data-list"><%=statckTrace%></td>
		        													<td align="center" class="body-data-list"><%=connectionStackTrace.getTime() %></td>
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
