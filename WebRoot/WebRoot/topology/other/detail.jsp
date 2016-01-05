<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.topology.util.analysis.GroupData"%>
<%@page import="com.afunms.topology.util.analysis.IpData"%>
<%@page import="com.afunms.topology.util.analysis.LACData"%>
<%@page import="com.afunms.polling.node.OthersNode"%>
<%@page import="com.afunms.common.util.*"%>
<%@page import="java.util.*"%>
<%
  String rootPath = request.getContextPath(); 
  String menuTable = (String)request.getAttribute("menuTable");
  OthersNode others = (OthersNode)request.getAttribute("others");
  String keys = (String)request.getAttribute("keys");
  String type = (String)request.getAttribute("type");
  Hashtable gsnhash = new Hashtable();
  Hashtable hash = ShareData.getGSNdata();
  if(keys!=null&&hash!=null){
      gsnhash = (Hashtable)hash.get(keys);
  }
%>
<html>
<head>
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>


<script language="JavaScript" type="text/JavaScript">
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


</head>
<body id="body" class="body" onload="initmenu();">
	<form id="mainForm" method="post" name="mainForm">
		<input type=hidden name="id">
		
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
							<td class="td-container-main-service-detail">
								<table id="container-main-service-detail" class="container-main-service-detail">
									<tr>
										<td>
											<table id="service-detail-content" class="service-detail-content">
												<tr>
													<td>
														<table id="service-detail-content-header" class="service-detail-content-header">
										                	<tr>
											                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
											                	<td class="application-detail-content-title">设备详细信息</td>
											                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
											       			</tr>
											        	</table>
				        							</td>
				        						</tr>
				        						<tr>
				        							<td>
				        								<table id="service-detail-content-body" class="service-detail-content-body">
				        									<tr>
				        										<td>
				        										
				        											<table>
										               					<tr>
										     								<td width="80%" align="left" valign="top">
			        															<table>
						                    										<tr>
																                      <td width="30%" height="26" align="left" nowrap class=txtGlobal>&nbsp;名称:</td>
																                      <td width="70%"><%=others.getAlias()%></td>
																                    </tr>
																                    <tr bgcolor="#F1F1F1">
																                      <td height="26" class=txtGlobal align="left" nowrap>&nbsp;状态:</td>
																                      <td><img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(others.getStatus())%>">&nbsp;<%=NodeHelper.getStatusDescr(others.getStatus())%></td>
																                    </tr>
																                    <tr>
																                      <td width="30%" height="26" 
																                    align=left nowrap  class=txtGlobal>&nbsp;IP地址:</td>
																                      <td width="70%"><%=others.getIpAddress()%></td>
																                    </tr>
																                    <tr bgcolor="#F1F1F1">
																                      <td height="26" class=txtGlobal nowrap>&nbsp;类型:</td>
																                      <td><%=type%></td>
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
				        						<tr>
				        							<td>
				        								<table id="application-detail-content-footer" class="application-detail-content-footer">
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
									<tr>
										<td>
											<table id="service-detail-content" class="service-detail-content">
												<tr>
													<td>
														<table id="service-detail-content-header" class="service-detail-content-header">
										                	<tr>
											                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
											                	<td class="application-detail-content-title">性能信息</td>
											                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
											       			</tr>
											        	</table>
				        							</td>
				        						</tr>
				        						<tr>
				        							<td>
				        								<table id="service-detail-content-body" class="service-detail-content-body">
				        									<tr>
				        										<td>
				        											<table>
									                        			<tr>
																			<td>
																	               <table border="0" cellpadding="0" cellspacing="0" align=center>
																	                <tr>
																	                    <td align=center></td>
																	                    <td align=center>流量</td>
																	                    <td align=center>PDP成功率</td>
																	                    <td align=center>业务成功率</td>
																	                    <td align=center>平均速率</td>
																	                    <td align=center>丢包率</td>
																	                    <td align=center>重传率</td>
																	                  </tr>  
																                  <%
																                   if(gsnhash!=null&&gsnhash.get(others.getAlias())!=null){
																                       GroupData groupData = (GroupData)gsnhash.get(others.getAlias());
																                   %>
																                       <tr>
																                        <td align=center ><b>Total</b></td>
																	                    <td align=center ><%=groupData.getFlow()+"B"%></td>
																	                    <td align=center ><%=groupData.getPDPRate()+"%"%></td>
																	                    <td align=center><%=groupData.getBusinessRate()+"%"%></td>
																	                    <td align=center><%=groupData.getAverageRate()+"bps"%></td>
																	                    <td align=center><%=groupData.getPacketLossRate()+"%"%></td>
																	                    <td align=center><%=groupData.getRetransmission()+"%"%></td>
																	                  </tr> 
																                   <% 
																                   } 
																                   if(gsnhash!=null&&gsnhash.get("ipList")!=null){
																                       List<IpData> ipList = (List<IpData>)gsnhash.get("ipList");
																                       if(ipList!=null&&ipList.size()>0){
																	                           for(int i=0;i<ipList.size();i++){
																                                   IpData ipData = (IpData)ipList.get(i);
																	                %>
																                       <tr>
																                        <td align=center ><b><%=ipData.getAddr()%></b></td>
																	                    <td align=center ><%=ipData.getFlow()+"B"%></td>
																	                    <td align=center ><%=ipData.getPDPRate()+"%"%></td>
																	                    <td align=center><%=ipData.getBusinessRate()+"%"%></td>
																	                    <td align=center><%=ipData.getAverageRate()+"bps"%></td>
																	                    <td align=center><%=ipData.getPacketLossRate()+"%"%></td>
																	                    <td align=center><%=ipData.getRetransmission()+"%"%></td>
																	                  </tr> 
																                   <% 
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
				        								</table>
				        							</td>
				        						</tr>
				        						<tr>
				        							<td>
				        								<table id="application-detail-content-footer" class="application-detail-content-footer">
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
							<td class="td-container-main-service-detail-tool">
								<table class="container-main-service-detail-tool">
									<tr>
										<td>
											<table class="service-detail-tool-bar" style="bgcolor:#ababab">
												
												<tr>
													<td align=center>
														<div id="flashcontent00">
																												<strong>You need to upgrade your Flash Player</strong>	
																											</div>
																											<script type="text/javascript">
																												var so = new SWFObject("<%=rootPath%>/flex/Pie_Component.swf?percent1=100&percentStr1=可用&percent2=0&percentStr2=不可用", "Pie_Component", "160", "160", "8", "#ffffff");
																												so.write("flashcontent00");
																											</script>
													</td>
												</tr>
												<tr>
													<td height="7" align=center><img src="<%=rootPath%>/resource/image/Loading_2.gif"></td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td>
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
</BODY>
</HTML>