<%@page language="java" contentType="text/html;charset=gb2312" pageEncoding="gb2312"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.config.dao.IpAliasDao"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.config.model.IpAlias"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Vector"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.afunms.polling.api.I_HostLastCollectData"%>
<%@page import="com.afunms.polling.impl.HostLastCollectDataManager"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.portscan.dao.PortScanDao"%>
<%@page import="com.afunms.common.util.CreateAmColumnPic"%>
<%@page import="java.util.Hashtable"%>


<%
	String rootPath = request.getParameter("rootPath")==null?"":request.getParameter("rootPath");
    String nodeid = request.getParameter("nodeid")==null?"":request.getParameter("nodeid");
    String alias = request.getParameter("alias")==null?"":request.getParameter("alias");
    String netMask = request.getParameter("netMask")==null?"":request.getParameter("netMask");
    String maxAlarmLevel = request.getParameter("maxAlarmLevel")==null?"":request.getParameter("maxAlarmLevel");
    String category = request.getParameter("category")==null?"":request.getParameter("category");
    String subtype = request.getParameter("subtype")==null?"":request.getParameter("subtype");
    String mac = request.getParameter("mac")==null?"":request.getParameter("mac");
    String supperName = request.getParameter("supperName")==null?"":request.getParameter("supperName");  // 类别
    String ipaddress = request.getParameter("ipaddress")==null?"":request.getParameter("ipaddress");  // 类别
    String curDayAmMemoryChartInfo = request.getParameter("curDayAmMemoryChartInfo")==null?"":request.getParameter("curDayAmMemoryChartInfo");  // 类别
    String curDayAmDiskChartInfo = request.getParameter("curDayAmDiskChartInfo")==null?"":request.getParameter("curDayAmDiskChartInfo");  // 类别
    String CSDVersion = request.getParameter("CSDVersion")==null?"":request.getParameter("CSDVersion");
    String processorNum = request.getParameter("processorNum")==null?"":request.getParameter("processorNum");
    String pageingUsed = request.getParameter("pageingUsed")==null?"":request.getParameter("pageingUsed");
    String totalPageing = request.getParameter("totalPageing")==null?"":request.getParameter("totalPageing");

    String sysDescr = request.getParameter("sysDescr")==null?"":request.getParameter("sysDescr");
    String sysObjectID = request.getParameter("sysObjectID")==null?"":request.getParameter("sysObjectID");
    String sysUpTime = request.getParameter("sysUpTime")==null?"":request.getParameter("sysUpTime");
    String sysName = request.getParameter("sysName")==null?"":request.getParameter("sysName");
    String sysLocation = request.getParameter("sysLocation")==null?"":request.getParameter("sysLocation"); 
    String curPingImage = request.getParameter("curPingImage")==null?"":request.getParameter("curPingImage");
    String curResponseTimeValue = request.getParameter("curResponseTimeValue")==null?"":request.getParameter("curResponseTimeValue");
    String curMemoryImage = request.getParameter("curPingImage")==null?"":request.getParameter("curMemoryImage");
    String curCPUImage = request.getParameter("curCPUImage")==null?"":request.getParameter("curCPUImage");

    IpAliasDao ipdao = new IpAliasDao();
    List iplist = ipdao.loadByIpaddress(ipaddress);
    ipdao.close();
    ipdao = new IpAliasDao();
    IpAlias ipalias = ipdao.getByIpAndUsedFlag(ipaddress,"1");
    ipdao.close();
    if(iplist == null) {
        iplist = new ArrayList();
    }
    String sysdescrforshow="";//用于显示设备信息简称
    if (sysDescr!="" && sysDescr!=null) {
        if(sysDescr.length() > 40){
            sysdescrforshow = sysDescr.substring(0,40)+"...";
        } else{
            sysdescrforshow= sysDescr;
        }
    }
    //内存最大、平均利用率 amcharts wxy add
    CreateAmColumnPic aixColumnPic=new CreateAmColumnPic();
    //最近一周CPU平均利用率 amcharts wxy add
    String cpuData=aixColumnPic.createCpuChartLastWeek(ipaddress);
%>
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<table id="detail-content" class="detail-content">
	<tr>
		<td>
			 <jsp:include page="/topology/includejsp/detail_content_top.jsp">
			 	<jsp:param name="contentTitle" value="设备详细信息"/> 
			 </jsp:include>
		</td>
	</tr>
	<tr>
		<td>
			<table id="detail-content-body" class="detail-content-body" align=center cellpadding=0 cellspacing="0" width=100%>
				<tr>
					<td> 
						<table>
							<tr> 
 								<td width="50%" align="left" valign="top">
									<table>
				 						<tr>
											<td  width="30%" height="29" align="left" nowrap class=txtGlobal>&nbsp;主机名:</td>
											<td  width="70%"><%=alias%> </td>
										</tr>                   										
										<tr bgcolor="#F1F1F1">
											<td height="29" class=txtGlobal align="left" nowrap >&nbsp;状态:</td>
											<td><img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(Integer.valueOf(maxAlarmLevel))%>">&nbsp;<%=NodeHelper.getStatusDescr(Integer.valueOf(maxAlarmLevel))%></td>
										</tr>
										<tr>
											<td height="29" align=left nowrap class=txtGlobal>&nbsp;系统名称:</td>
											<td ><%=sysName%></td>
										</tr>
			   							<tr bgcolor="#F1F1F1">
											<td height="29" class=txtGlobal align="left" nowrap>&nbsp;版本:</td>
											<td><%=CSDVersion%></td>
										</tr>
										<tr>
											<td height="29" align=left nowrap class=txtGlobal>&nbsp;类别:</td>
											<td ><%=NodeHelper.getNodeCategory(Integer.valueOf(category))%></td>
										</tr>
										<tr bgcolor="#F1F1F1">
			     							<td height="29" align=left nowrap  class=txtGlobal>&nbsp;类型:</td>
			     							<td><%=subtype%></td>
			   							</tr>
			   							<tr>
			     							<td height="29" align=left nowrap class=txtGlobal>&nbsp;CPU个数:</td>
			     							<td><%=processorNum%></td>
			   							</tr>
			   							<tr bgcolor="#F1F1F1">
			     							<td height="29" class=txtGlobal valign=middle nowrap  >&nbsp;设备启动时间:</td>
			     							<td>
												<jsp:include page="/topology/includejsp/td_acronym.jsp">
													<jsp:param name="wholeStr" value="<%= sysUpTime %>"/>
												</jsp:include> 
											</td>
			   						 	</tr>
			   							<tr>
											<td width="30%" height="26" align=left valign=middle nowrap class=txtGlobal>&nbsp;IP地址:&nbsp;</td>
											<TD nowrap width="40%">
												<select name="ipalias<%=ipaddress %>">
													<option selected><%=ipaddress %></option>
													<% 
													   for(int j = 0; j < iplist.size() ; j++){
																IpAlias voTemp = (IpAlias)iplist.get(j);
															%>
															<option <%if(ipalias != null && ipalias.getAliasip().equals(voTemp.getAliasip())){ %>selected<%} %>><%=voTemp.getAliasip() %></option>
													<%} %>
												</select>
												[
												<a href="#"  style="cursor:hand" onclick="modifyIpAliasajax('<%=ipaddress%>');">修改</a>
												]
											</TD>
										</tr>
			   							<tr>
			     							<td height="29" class=txtGlobal valign=middle nowrap>&nbsp;MAC地址:</td> 
			     							<td>
			     								<select name="mac" id="mac">
												<%
														String[] as = mac.split(",");
														int num=mac.length()/17;
														for(int i=0;i<num;i++){
													%>
														<option value="<%=as[i]%>"><%=as[i]%></option>
													<%		
														}
												 %> 
												 </select>
												 [<a href="#" style="cursor:hand" onclick="updateMac()">入库</a>]
												 <script type="text/javascript">
												 	function updateMac(){
												 		var mac="<%=mac%>";
												 		var id="<%=nodeid%>";
        												$.ajax({
															type:"GET",
															dataType:"json",
															url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=updatemac&mac="+mac+"&id="+id+"&flag="+(new Date()),
															success:function(data){
																window.alert("入库成功！");
															}
														});
												 	}
												 </script>
											</td>
			   							</tr>
			   							<tr  bgcolor="#F1F1F1">
											<td height="29" class=txtGlobal valign=middle nowrap  >&nbsp;系统OID:</td>
											<td><%=sysObjectID%></td>
										</tr>
			  							<tr>
                                            <td height="26" class=txtGlobal nowrap>
                                                &nbsp;设备供应商:
                                            </td>
                                            <td><%=supperName %></td>
                                        </tr> 
						         		<tr bgcolor="#F1F1F1">
										 	<td height="29">
											 	&nbsp;换页率:
										 	</td>
		            						<td>
												<table width="80%">
			             							<tr>
			              								<td >
			             									<table border=1 height=15  bgcolor=#ffffff id="WLTABLE">
			                									<tr>
				                									<td width="<%=pageingUsed%>%" bgcolor="green" align=center>&nbsp;&nbsp;<%=pageingUsed%>%</td>
				                  									<td width="<%=(100 - Double.valueOf(pageingUsed)) + ""%>%" bgcolor=#ffffff></td> 
			                  									</tr>
			              									</table>
			             								</td>
			             								<td>&nbsp;&nbsp;</td>
			             							</tr>
			            						</table>
											</td>
										</tr>
			   							<tr> 
			               					<td height="29">&nbsp;总换页空间:&nbsp;&nbsp;</td>
			               					<td align=left><%=totalPageing%></td>
										</tr>				
			      					</table>
			      				</td> 
			      				<td width="40%" align="right" valign="top">
                                    <jsp:include page="/topology/includejsp/systeminfo_graphic.jsp">
                                        <jsp:param name="rootPath" value="<%=rootPath %>"/>
                                        <jsp:param name="curPingImage" value="<%=curPingImage %>"/>
                                        <jsp:param name="curResponseTimeValue" value="<%=curResponseTimeValue %>"/> 
                                        <jsp:param name="curMemoryImage" value="<%=curMemoryImage %>"/>
                                        <jsp:param name="curCPUImage" value="<%=curCPUImage %>"/> 
                                    </jsp:include>
                                </td>
								</tr>
							<tr>
							    <td colspan=2 width="100%" align="left" valign="top">
							        <table>
							               <tr>
							                   <td width="30%" align="center" valign="top">
							                       <div id="aixCpu">
							                      
	                                            
	                                               <script type="text/javascript">
	                                              <% if(!cpuData.equals("0")&&cpuData!=null&&!cpuData.equals("")){%>
	                                          var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "amcolumn","270", "210", "8", "#FFFFFF");
				                                  so.addVariable("path", "<%=rootPath%>/amchart/");
				                                  so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/hostcpu_settings.xml"));
				                                  so.addVariable("chart_data","<%=cpuData%>");
				                                  so.write("aixCpu");
				                                  <%}else{%>
				                                  var _div=document.getElementById("aixCpu");
												  var img=document.createElement("img");
													  img.setAttribute("src","<%=rootPath%>/resource/image/nodata.gif");
													 _div.appendChild(img);
				                                  <%}%>
	                                          </script>
							                       </div> 
							                   </td>
							                   <td width="40%" align="center" valign="top">
							                       <div id="aixMemory">
							                       </div>
							                      <script type="text/javascript">
				
		                                           var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "amcolumn", "330", "210", "8", "#FFFFFF");
		                                               so.addVariable("path", "<%=rootPath%>/amchart/");
		                                               so.addVariable("settings_file",  escape("<%=rootPath%>/amcharts_settings/hostmemory_settings.xml"));
	                                                   so.addVariable("chart_data", "<%=curDayAmMemoryChartInfo%>");
		                                               so.addVariable("preloader_color", "#999999");
		                                               so.write("aixMemory");
	                                            </script>
							                   </td>
							                   <td width="30%" align="center" valign="top">
							                       <div id="aixDisk">
							                       </div>
							                         
	                                               <script type="text/javascript">
	                                              <% if(!curDayAmDiskChartInfo.equals("0")){%>
	                                          var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "amcolumn","270", "210", "8", "#FFFFFF");
				                                  so.addVariable("path", "<%=rootPath%>/amchart/");
				                                  so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/hostdisk_settings.xml"));
				                                  so.addVariable("chart_data","<%=curDayAmDiskChartInfo%>");
				                                  so.write("aixDisk");
				                                  <%}else{%>
				                                  var _div=document.getElementById("aixDisk");
												  var img=document.createElement("img");
													  img.setAttribute("src","<%=rootPath%>/resource/image/nodata.gif");
													 _div.appendChild(img);
				                                  <%}%>
	                                          </script>
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
	<tr>
		<td>
			 <jsp:include page="/topology/includejsp/detail_content_footer.jsp"/>
		</td>
	</tr>
</table>
