<%@ page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.model.MonitorNodeDTO"%>
<%@page import="com.afunms.application.model.MonitorDBDTO"%>
<%@page import="com.afunms.application.model.MonitorMiddlewareDTO"%>
<%@page import="com.afunms.polling.impl.HostLastCollectDataManager"%>
<%@page import="com.afunms.polling.api.I_HostLastCollectData"%>
<%@page import="com.afunms.polling.PollingEngine"%>
<%@page import="com.afunms.application.util.AssetHelper"%>
<%@page import="com.afunms.capreport.model.StatisNumer"%>
<%@page import="com.afunms.common.util.CEIString"%>
<html>
<head>
<%
 int count = 0;
 String rootPath = request.getContextPath();
 List networkList = (List)request.getAttribute("networkList");
 List serverList = (List)request.getAttribute("serverList");
 List dbList = (List)request.getAttribute("dbList");
 List midwareList = (List)request.getAttribute("midwareList");
 I_HostLastCollectData hostlastmanager = new HostLastCollectDataManager();
 String runmodel = PollingEngine.getCollectwebflag();
 session.setAttribute("networkList",networkList);
 session.setAttribute("serverList",serverList);
 session.setAttribute("dbList",dbList);
 session.setAttribute("midwareList",midwareList);
%>
<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>

<title>硬件报表</title>


<!-- snow add end -->
<script type='text/javascript' src='/afunms/dwr/interface/DWRUtil.js'></script>
<script type='text/javascript' src='/afunms/dwr/engine.js'></script>
<script type='text/javascript' src='/afunms/dwr/util.js'></script>
<script language="javascript" src="/afunms/js/tool.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>




<script language="JavaScript" type="text/JavaScript">
 Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
	
	
});

function hardware_report(str){
	mainForm.action = "<%=rootPath%>/hostreport.do?action=downloadHardwareReportNew&str="+str;
	mainForm.submit();
}

</script>
<style>
.detail-data-body-list {
	font-size: 12px;
	border-top: 1px solid #e8e9e9;
	border-left: 1px solid #e8e9e9;
	border-bottom: 1pt solid #e8e9e9;
	border-right: 1px solid #e8e9e9;
	}
.detail-data-body-table1 {
	font-size: 12px;
	border-top: none black;
	border-left: 0px solid #e8e9e9;
	border-bottom: 1pt solid #e8e9e9;
	border-right: 0px solid #e8e9e9;	
}
</style>
</head>
<body id="body" class="body" >
<form id="mainForm" method="post" name="mainForm">
<input type=hidden id="ipaddress" name="ipaddress" value=<%=request.getParameter("ipaddress") %>>
	<table id="container-main" class="container-main">
		<tr>
			<td>
				<table id="container-main-win" class="container-main-win">
					<tr>
						<td>
							<table id="win-content" class="win-content">
								<tr>
									<td>
										<table id="win-content-header" class="win-content-header">
				                			<tr>
							                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
							                	<td class="win-content-title" style="align:center">&nbsp;报表</td>
							                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>											   
											</tr>
									    
					       				</table>
				       				</td>
				       			</tr>
						       	<tr>
						       		<td>
						       			<table id="win-content-body" class="win-content-body">
											<tr>
						       					<td>
													<table bgcolor="#ECECEC">
														<tr align="left" valign="center"> 
														<td height="28" align="left" width="70%">
														</td>
														<td height="28" align="left" width="10%">
															<a href="javascript:hardware_report(1)"><img name="selDay1" alt='导出word' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_word.gif" width=18  border="0">导出WORLD</a>
														</td>
														<td height="28" align="left" width="10%">
															<a href="javascript:hardware_report(0)"><img name="selDay1" alt='导出EXCEL' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=18  border="0">导出EXCEL</a>
														</td>
														<td height="28" align="left" width="10%">&nbsp;
															<a href="javascript:hardware_report(4)"><img name="selDay1" alt='导出word' style="CURSOR: hand" src="<%=rootPath%>/resource/image/export_pdf.gif" width=18 border="0">导出PDF</a>
														</td>
														</tr>
														<tr>
															<td colspan="4">
			<table >
			<tr>
                 <td height="28"   class="detail-data-body-title"  width='95' bgcolor="#FFFFFF" >网络设备</td>
			     <td height="28"  align="center" >
			         <table >
			                <tr>
			                    <td height="28"  align="center" class="detail-data-body-title">名称</td>
			                    <td height="28"  align="center" class="detail-data-body-title">类别</td>
			                    <td height="28"  align="center" class="detail-data-body-title">类型</td>
			                    <td height="28"  align="center" class="detail-data-body-title">IP</td>
			                    <td height="28"  align="center" class="detail-data-body-title">端口数量</td>
			                </tr>
			               
			               <%
					        	if(networkList!=null&& networkList.size()>0){
					        		for(int i = 0 ; i < networkList.size() ; i++){
					        			MonitorNodeDTO monitorNodeDTO = (MonitorNodeDTO)networkList.get(i);
					       %>
					            <tr bgcolor="#FFFFFF">
					        	<td height="28"  align="center" class="detail-data-body-list"><%=monitorNodeDTO.getAlias()%></td>
					        	<td height="28"  align="center" class="detail-data-body-list"><%=monitorNodeDTO.getCategory()%></td>
					        	<td height="28"  align="center" class="detail-data-body-list"><%=monitorNodeDTO.getType()%></td>
					        	<td height="28"  align="center" class="detail-data-body-list"><%=monitorNodeDTO.getIpAddress()%></td>
					        	<td height="28"  align="center" class="detail-data-body-list"><%=monitorNodeDTO.getEntityNumber()%></td>
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
		<td colspan="4">
			<table >
			<tr>
                 <td height="28"   class="detail-data-body-title"  width='95' bgcolor="#FFFFFF" >服务器</td>
			     <td height="28"  align="center" >
			         <table >
			                <tr>
			                    <td height="28"  align="center" class="detail-data-body-title">名称</td>
			                    <td height="28"  align="center" class="detail-data-body-title">类别</td>
			                    <td height="28"  align="center" class="detail-data-body-title">类型</td>
			                    <td height="28"  align="center" class="detail-data-body-title">IP</td>
			                    <td height="28"  align="center" class="detail-data-body-title">磁盘</td>
			                    <td height="28"  align="center" class="detail-data-body-title">内存</td>
			                </tr>
			               
			               <%
					        	if(serverList!=null&& serverList.size()>0){
					        		for(int i = 0 ; i < serverList.size() ; i++){
					        			MonitorNodeDTO monitorNodeDTO = (MonitorNodeDTO)serverList.get(i);
					        			StringBuffer disksb=new StringBuffer();
					        			StringBuffer memsb=new StringBuffer();
					        			AssetHelper helper=new AssetHelper();
					        		List<StatisNumer> diskList=helper.getAssetList(monitorNodeDTO.getIpAddress(),"Disk");
					        		List<StatisNumer> memList=helper.getAssetList(monitorNodeDTO.getIpAddress(),"Memory");
					        		if(diskList!=null&&diskList.size()>0){
					        		for(int k=0;k<diskList.size();k++){
					        		StatisNumer numer=diskList.get(k);
					        		disksb.append(numer.getName()+"(");
					        		disksb.append(numer.getCurrent()+") ");
					        		}
					        		}
					        		if(memList!=null&&memList.size()>0){
					        		for(int k=0;k<memList.size();k++){
					        		StatisNumer numer=memList.get(k);
					        		memsb.append(numer.getName()+"(");
					        		memsb.append(numer.getCurrent()+") ");
					        		}
					        		}
					       %>
					            <tr bgcolor="#FFFFFF">
					        	<td height="28"  align="center" class="detail-data-body-list"><%=monitorNodeDTO.getAlias()%></td>
					        	<td height="28"  align="center" class="detail-data-body-list"><%=monitorNodeDTO.getCategory()%></td>
					        	<td height="28"  align="center" class="detail-data-body-list"><%=monitorNodeDTO.getType()%></td>
					        	<td height="28"  align="center" class="detail-data-body-list"><%=monitorNodeDTO.getIpAddress()%></td>
					        	<td height="28"  align="center" class="detail-data-body-list">
					        	<table>
					        	<tr>
					        		<td   align="left" class="detail-data-body-list" width=80%>磁盘名</td>
					        		<td  align="left" class="detail-data-body-list" width=20%>磁盘容量</td>
					        		</tr>
					        	<%
					        	double sum=0;
					        	if(diskList!=null&&diskList.size()>0){
					        		count += diskList.size()+1;
					        		for(int k=0;k<diskList.size();k++){
					        		StatisNumer numer=diskList.get(k);
					        		double temp=0;
					        		if(numer.getCurrent().indexOf("M")>0){
					        		temp=Double.parseDouble(numer.getCurrent().replace("M",""))/1024;
					        		}else{
					        		temp=Double.parseDouble(numer.getCurrent().replace("G","").replace("M",""));
					        		}
					        		 sum+=temp;
					        		%>
					        		<tr >
					        		<td height="28"  align="left" class="detail-data-body-list" width=80%><%=numer.getName() %></td>
					        		<td height="28"  align="left" class="detail-data-body-list" width=20%><%=numer.getCurrent() %></td>
					        		</tr>
					        		<%
					        		}
					        		%>
					        		<tr>
					        		<td   align="left" class="detail-data-body-list" width=80%>磁盘总量</td>
					        		<td  align="left" class="detail-data-body-list" width=20%><%=sum+"G" %></td>
					        		</tr>
					        		<% 
					        		}
					        	 %>
					        	</table>
					        	</td>
					        	<td   align="center" class="detail-data-body-list">
                                <table>
                                
					        	<%
					        	
					        	if(memList!=null&&memList.size()>0){
					        		for(int k=0;k<memList.size();k++){
					        		StatisNumer numer=memList.get(k);
					        		
					        		%>
					        		<tr>
					        		<td   align="left" class="detail-data-body-list" width=80%><%=numer.getName() %></td>
					        		<td  align="left" class="detail-data-body-list" width=20%><%=numer.getCurrent() %></td>
					        		</tr>
					        		<%
					        		}
					        		
					        		}
					        	 %>
					        	</table>
                                </td>
					           </tr>
					           <%
					        								           }
					        		%>
					        		<input type="hidden" name="diskcount" value="<%=count %>">
					        		<%
					        								}
					        %>
			         </table>
			     </td>
				 
            </tr>
			</table>
															</td>	
														</tr>
														<tr>
		<td colspan="4">
			<table >
			<tr>
                 <td height="28"   class="detail-data-body-title"  width='95' bgcolor="#FFFFFF" >数据库</td>
			     <td height="28"  align="center" >
			         <table >
			                <tr>
			                    <td height="28"  align="center" class="detail-data-body-title">名称</td>
			                    <td height="28"  align="center" class="detail-data-body-title">类型</td>
			                    <td height="28"  align="center" class="detail-data-body-title">IP</td>
			                    
			                </tr>
			               
			               <%
					        	if(dbList!=null&& dbList.size()>0){
					        	
					        		for(int i = 0 ; i < dbList.size() ; i++){
					        		
					        			MonitorDBDTO monitorNodeDTO = (MonitorDBDTO)dbList.get(i);
					       %>
					            <tr bgcolor="#FFFFFF">
					        	<td height="28"  align="center" class="detail-data-body-list"><%=monitorNodeDTO.getAlias()%></td>
					        	<td height="28"  align="center" class="detail-data-body-list"><%=monitorNodeDTO.getDbtype()%></td>
					        	<td height="28"  align="center" class="detail-data-body-list"><%=monitorNodeDTO.getIpAddress()%></td>
					        	
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
		<td colspan="4">
			<table >
			<tr>
                 <td height="28"   class="detail-data-body-title"  width='95' bgcolor="#FFFFFF" >中间件</td>
			     <td height="28"  align="center" >
			         <table >
			                <tr>
			                    <td height="28"  align="center" class="detail-data-body-title">名称</td>
			                    <td height="28"  align="center" class="detail-data-body-title">类型</td>
			                    <td height="28"  align="center" class="detail-data-body-title">IP</td>
			                    
			                </tr>
			               
			               <%
					        	if(midwareList!=null&& midwareList.size()>0){
					        	
					        		for(int i = 0 ; i < midwareList.size() ; i++){
					        		
					        			MonitorMiddlewareDTO monitorNodeDTO = (MonitorMiddlewareDTO)midwareList.get(i);
					       %>
					            <tr bgcolor="#FFFFFF">
					        	<td height="28"  align="center" class="detail-data-body-list"><%=monitorNodeDTO.getAlias()%></td>
					        	<td height="28"  align="center" class="detail-data-body-list"><%=monitorNodeDTO.getCategory()%></td>
					        	<td height="28"  align="center" class="detail-data-body-list"><%=monitorNodeDTO.getIpAddress()%></td>
					        	
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
													</table>
						       					</td>
						       				</tr>
											<tr>
							                	<td class="win-data-title" style="height: 29px;" ></td>
							       			</tr>
							       			<tr align="left" valign="center"> 
			             						<td height="28" align="left" border="0">
													
													<input type=hidden name="eventid">
													<div id="loading">
													<div class="loading-indicator">
														<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif" width="32" height="32" style="margin-right: 8px;" align="middle" />Loading...</div>
													</div>
													
													
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
			
            		<div align=center>
            			<input type=button value="关闭窗口" onclick="window.close()">
            		</div>  
					<br>
</form>
</body>
</html>