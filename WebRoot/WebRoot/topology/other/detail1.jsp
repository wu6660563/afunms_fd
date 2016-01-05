<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.topology.util.analysis.GroupData"%>
<%@page import="com.afunms.topology.util.analysis.IpData"%>
<%@page import="com.afunms.polling.node.OthersNode"%>
<%@page import="com.afunms.common.util.*"%>
<%@page import="java.util.*"%>
<%
  String rootPath = request.getContextPath(); 
  OthersNode others = (OthersNode)request.getAttribute("others");
  String keys = (String)request.getAttribute("keys");
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
<title>详细信息</title>
</head>
<body id="body" class="body" onload="initmenu();">
	<form id="mainForm" method="post" name="mainForm">
		<table id="body-container" class="body-container" width=100%>
			<tr>
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
																	               <table border="1" cellpadding="0" cellspacing="0" align=center>
																	                <tr>
																	                    <td align=center width=10%>&nbsp;</td>
																	                    <td align=center width=15%>流量</td>
																	                    <td align=center width=15%>PDP成功率</td>
																	                    <td align=center width=15%>业务成功率</td>
																	                    <td align=center width=15%>平均速率</td>
																	                    <td align=center width=15%>丢包率</td>
																	                    <td align=center width=15%>重传率</td>
																	                  </tr>  
																                  <%
																                   if(gsnhash!=null&&gsnhash.get(others.getAlias())!=null){
																                       GroupData groupData = (GroupData)gsnhash.get(others.getAlias());
																                   %>
																                       <tr <%=onmouseoverstyle%>>
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
																	               %>
																	               <%
																	                   if(gsnhash!=null&&gsnhash.get("ipList")!=null){
																	                       List<IpData> ipList = (List<IpData>)gsnhash.get("ipList");
																	                       if(ipList!=null&&ipList.size()>0){
																	                           for(int i=0;i<ipList.size();i++){
																	                               IpData ipData = (IpData)ipList.get(i);
																	               %>
																	                               <tr <%=onmouseoverstyle%>>
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
						</tr>
					</table>
				</td>
			</tr>
		</table>
		
	</form>
</BODY>
</HTML>