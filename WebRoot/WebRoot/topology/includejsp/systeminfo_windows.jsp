<%@page language="java" contentType="text/html;charset=gb2312" pageEncoding="gb2312"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.config.dao.IpAliasDao"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.config.model.IpAlias"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.afunms.common.util.CreateAmColumnPic"%>


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
    String curVirtualMemoryValue = request.getParameter("curVirtualMemoryValue")==null?"":request.getParameter("curVirtualMemoryValue");  // 类别
    String curPhysicalMemoryValue = request.getParameter("curPhysicalMemoryValue")==null?"":request.getParameter("curPhysicalMemoryValue");  // 类别
    String curDayAmMemoryChartInfo = request.getParameter("curDayAmMemoryChartInfo")==null?"":request.getParameter("curDayAmMemoryChartInfo");  // 类别
    String curDayAmDiskChartInfo = request.getParameter("curDayAmDiskChartInfo")==null?"":request.getParameter("curDayAmDiskChartInfo");  // 类别
    
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
            <table id="detail-content-body" class="detail-content-body">
                <tr>
                    <td> 
                        <table cellpadding=0 width=100% align=center algin="center">
                            <tr> 
                                <td width="60%" align="left" valign="top">
                                    <table cellpadding="0" cellspacing="0" width=100% align=center>
                                        
                                        <tr>
                                            <td width="30%" height="26" align="left" nowrap  >&nbsp;设备标签:</td>
                                            <td width="70%">
                                               <span id="lable"><%=alias%></span>[<a href="#" onclick="openNewWindowForAlias()"><img src="<%=rootPath%>/resource/image/editicon.gif" border=0>修改</a>]
                                            </td>
                                        </tr>
                                        <tr bgcolor="#F1F1F1">
                                            <td width="30%" height="26" align="left" nowrap>&nbsp;系统名称:</td>
                                            <td width="70%">
                                            <span id="sysname"><%=sysName%></span>[<a href="#" onclick="openNewWindowForSysName()"><img src="<%=rootPath%>/resource/image/editicon.gif" border=0>修改</a>]
                                            </td>
                                        </tr>                                                           
                                        <tr>
                                            <td height="26" align="left"   >&nbsp;状态:</td>
                                            <td>
                                            <img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(Integer.valueOf(maxAlarmLevel))%>">
                                            &nbsp;
                                            <%=NodeHelper.getStatusDescr(Integer.valueOf(maxAlarmLevel))%>
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
                                            <td height="26" align="left"  >&nbsp;子网掩码:</td>
                                            <td><%=netMask%></td>
                                        </tr>
                                        <tr bgcolor="#F1F1F1">
                                            <td width="30%" height="26" align=left>
                                                &nbsp;类别:
                                            </td>
                                            <td width="70%">
                                                <%=NodeHelper.getNodeCategory(Integer.valueOf(category))%>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td width="30%" height="26" align=left >&nbsp;类型:</td>
                                            <td width="70%"><%=subtype%></td>
                                        </tr>
                                        <tr>
                                            <td width="30%" height="26" align=left valign=middle nowrap class=txtGlobal>&nbsp;系统描述:&nbsp;</td>
                                            <td width="70%"><acronym title="<%=sysDescr%>"><%=sysdescrforshow%></acronym></td>
                                        </tr>
                                        <tr>
                                            <td height="26" >&nbsp;设备启动时间:</td>
                                            <td><%=sysUpTime%></td>
                                        </tr>
                                        <tr>
                                            <td height="26" class=txtGlobal nowrap>
                                                &nbsp;设备供应商:
                                            </td>
                                            <td><%=supperName %></td>
                                        </tr>   
                                        <tr>
                                            <td width="30%" height="26">&nbsp;物理内存利用率</td>
                                            <td width="70%">
                                                <table> 
                                                    <tr>
                                                        <td width="80%">
                                                            <table border=1 height=15 width="100%" bgcolor=#ffffff id="WLTABLE">
                                                                <tr>
                                                                    <td width="<%=Integer.parseInt(curPhysicalMemoryValue)%>%" bgcolor="green" align=center>&nbsp;&nbsp;<%=curPhysicalMemoryValue%>%</td>
                                                                    <td width="<%=(100-Integer.parseInt(curPhysicalMemoryValue))%>%" bgcolor=#ffffff></td>
                                                                </tr>
                                                            </table>    
                                                        </td>
                                                        <!-- 
                                                        <td width="20%" align=left>
                                                            &nbsp;
                                                            <a onclick="fresh_WL()">
                                                                <img src="<//%=rootPath%>/resource/image/fresh.gif"></img>
                                                            </a>
                                                        </td>  -->
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <tr  bgcolor="#F1F1F1">
                                            <td width="30%" height="26">&nbsp;虚拟内存利用率</td>
                                            <td width="70%">
                                                <table> 
                                                    <tr>
                                                        <td width="80%">
                                                            <table border=1 height=15 width="100%" bgcolor=#ffffff id="XNTABLE">
                                                                <tr>
                                                                    <td width="<%=Integer.parseInt(curVirtualMemoryValue)%>%" bgcolor="green" align=center>&nbsp;&nbsp;<%=curVirtualMemoryValue%>%</td>
                                                                    <td width="<%=(100-Integer.parseInt(curVirtualMemoryValue))%>%" bgcolor=#ffffff></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <!--    
                                                        <td width="20%" align=left>
                                                            &nbsp;
                                                            <a onclick="fresh_XN()">
                                                                <img src="<//%=rootPath%>/resource/image/fresh.gif"/>
                                                            </a>
                                                        </td> -->
                                                    </tr>
                                                </table>
                                            </td>
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
                                         <td width="100%" align="center" valign="top" colspan=2>
                                                <table> 
                                                    <tr>
                                                        <td width="30%" align=center>
                                                            <table  height=15 width="100%" bgcolor=#ffffff id="XNTABLE">
                                                                <tr>
                                                                    <td>
                                                                    <div id="hostCpu">
                                                        
                                                                    </div>      
                                              <script type="text/javascript"
                                                    src="<%=rootPath%>/include/swfobject.js"></script>
                                                
                                                   <script type="text/javascript">
                                                  <% if(!cpuData.equals("0")){%>
                                              var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "amcolumn","270", "210", "8", "#FFFFFF");
                                                  so.addVariable("path", "<%=rootPath%>/amchart/");
                                                  so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/hostcpu_settings.xml"));
                                                  so.addVariable("chart_data","<%=cpuData%>");
                                                  so.write("hostCpu");
                                                  <%}else{%>
                                                  var _div=document.getElementById("hostCpu");
                                                  var img=document.createElement("img");
                                                      img.setAttribute("src","<%=rootPath%>/resource/image/nodata.gif");
                                                     _div.appendChild(img);
                                                  <%}%>
                                              </script>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>   
                                                        <td width="40%" align=center>
                                                        <div id="Infomemory">
                                                        <strong>You need to upgrade your Flash Player</strong>
                                                        </div>

                                                <script type="text/javascript">
                
                                                   var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "amcolumn", "330", "210", "8", "#FFFFFF");
                                                       so.addVariable("path", "<%=rootPath%>/amchart/");
                                                       so.addVariable("settings_file",  escape("<%=rootPath%>/amcharts_settings/hostmemory_settings.xml"));
                                                       so.addVariable("chart_data", "<%=curDayAmMemoryChartInfo%>");
                                                       so.addVariable("preloader_color", "#999999");
                                                       so.write("Infomemory");
                                                </script>
                                                            
                                                        </td>
                                                        <td width="30%" align=center>
                                                <div id="hostdisk">
                                                        
                                                        </div>      
                                              <script type="text/javascript"
                                                    src="<%=rootPath%>/include/swfobject.js"></script>
                                                
                                                   <script type="text/javascript">
                                                  <% if(!curDayAmDiskChartInfo.equals("0")){%>
                                              var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "amcolumn","270", "210", "8", "#FFFFFF");
                                                  so.addVariable("path", "<%=rootPath%>/amchart/");
                                                  so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/hostdisk_settings.xml"));
                                                  so.addVariable("chart_data","<%=curDayAmDiskChartInfo%>");
                                                  so.write("hostdisk");
                                                  <%}else{%>
                                                  var _div=document.getElementById("hostdisk");
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
<script type="text/javascript">
function openNewWindowForAlias() {
        window.open("<%=rootPath%>/network.do?action=ready_editalias&id=<%=nodeid%>&ipaddress=<%=ipaddress%>","_blank",
            "toolbar=no,titlebar=no, location=no,directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=yes,top=300,left=300 width=370, height=150")
    
}
function openNewWindowForSysName() {
        window.open("<%=rootPath%>/network.do?action=ready_editsysgroup&id=<%=nodeid%>&ipaddress=<%=ipaddress%>","_blank",
            "toolbar=no,titlebar=no, location=no,directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=yes,top=250,left=350 width=370, height=250")
    
}
</script>
