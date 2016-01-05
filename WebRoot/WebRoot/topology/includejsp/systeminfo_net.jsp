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

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String time1 = sdf.format(new Date());
    String starttime1 = time1 + " 00:00:00";
    String totime1 = time1 + " 23:59:59";

    String data = "";
    Vector vector = new Vector();
    Vector perVec = new Vector();
    StringBuffer sb = new StringBuffer();
    StringBuffer persb = new StringBuffer();
    String sbData = "", persbData = "";
    String[] colorStr = new String[] { "#D4B829", "#F57A29", "#B5DB2F",
            "#3189B5", "#AE3174", "#FFFF00", "#333399", "#0000FF",
            "#A52A2A", "#23f266" };
    String[] percolorStr = new String[] { "#0000FF", "#36DB43",
            "#3DA4D8", "#556B2F", "#8470F4", "#8A2BE2", "#23f266",
            "#F7FD31", "#8B4513", "FFD700" };
    I_HostLastCollectData hostlastmanager = new HostLastCollectDataManager();
    String[] netInterfaceItem1 = { "ifDescr", "InBandwidthUtilHdx" };
    String[] netInterfaceItem2 = { "ifDescr", "InBandwidthUtilHdxPerc" };
    try {
        vector = hostlastmanager.getInterface(ipaddress,
                netInterfaceItem1, "InBandwidthUtilHdx", starttime1,
                totime1);
        perVec = hostlastmanager.getInterface(ipaddress,
                netInterfaceItem2, "InBandwidthUtilHdxPerc",
                starttime1, totime1);
        //			
        String allipstr = SysUtil.doip(ipaddress);

        if (vector != null && vector.size() > 0) {
            sb.append("<chart><series>");
            persb.append("<chart><series>");
            for (int i = 0; i < 5; i++) {
                String[] strs = (String[]) vector.get(i);
                String[] perstrs = (String[]) perVec.get(i);
                String ifname = strs[0];
                String perifname = perstrs[0];

                sb.append("<value xid='" + i);
                sb.append("'>");
                sb.append(ifname + "</value>");
                persb.append("<value xid='" + i);
                persb.append("'>");
                persb.append(perifname + "</value>");
            }

            sb.append("</series><graphs><graph>");
            persb.append("</series><graphs><graph>");
            String speed = "";
            String perspeed = "";
            for (int i = 0; i < 5; i++) {
                String[] strs = (String[]) vector.get(i);
                String[] perstrs = (String[]) perVec.get(i);
                perspeed = perstrs[1].replaceAll("%", "");

                speed = strs[1].replaceAll("kb/s", "").replaceAll(
                        "kb/秒", "").replaceAll("KB/s", "");

                sb.append("<value xid='" + i).append("' color='")
                        .append(colorStr[i]);
                sb.append("'>");
                sb.append(speed + "</value>");
                persb.append("<value xid='" + i).append("' color='")
                        .append(percolorStr[i]);
                persb.append("'>");
                persb.append(perspeed + "</value>");
            }
            sb.append("</graph></graphs></chart>");
            persb.append("</graph></graphs></chart>");
            sbData = sb.toString();
            persbData = persb.toString();
        } else {
            sbData = "0";
            persbData = "0";
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    String value1 = "<chart><series><value xid='0'>down</value><value xid='1'>up</value><value xid='2'>down</value><value xid='3'>down</value></series><graphs><graph><value xid='0'>0</value><value xid='0'>0</value><value xid='1'>1</value><value xid='2'>0</value><value xid='0'>1</value></graph></graphs></chart>";
    Vector portVec = new Vector();
    PortScanDao dao = new PortScanDao();

    portVec = dao.getCurrentStatus(ipaddress);
    dao.close();
    String dataStr = "";
    String[] colors = { "#3DA4D8", "#FF0000" };
    StringBuffer xmlStr = new StringBuffer();
    if (portVec != null && portVec.size() > 0) {
        xmlStr.append("<chart><series>");
        for (int i = 0; i < portVec.size(); i++) {

            String[] strs = (String[]) portVec.get(i);
            xmlStr.append("<value xid='" + i);
            xmlStr.append("'>");
            xmlStr.append(strs[0] + "</value>");
        }
        String value = "";
        xmlStr.append("</series><graphs><graph>");
        for (int i = 0; i < portVec.size(); i++) {
            String[] strs = (String[]) portVec.get(i);
            value = strs[1];
            xmlStr.append("<value xid='" + i).append("' color='");
            if (value.equals("up")) {
                xmlStr.append(colors[0]).append("'>");
                xmlStr.append("1</value>");
            } else {
                xmlStr.append(colors[1]).append("'>");
                xmlStr.append("0</value>");
            }

        }
        xmlStr.append("</series><graphs><graph>");
        dataStr = xmlStr.toString();
    } else {
        dataStr = "0";
    }
%>
<table id="detail-content" class="detail-content">
    <tr>
		<td> 
			 <table width="100%" background="<%=rootPath%>/common/images/right_t_02.jpg" cellspacing="0" cellpadding="0">
				<tr>
					<td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
					<td class="layout_title"><b>设备详细信息</b></td>
					<td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table id="detail-content-body" class="detail-content-body">
				<tr>
					<td> 
						<table  cellpadding=0 width=100% align=center>
							<tr>
								<td width="60%" align="left" valign="top">
									<table >
										<tr>
											<td width="30%" height="26" align="left" nowrap  class=txtGlobal>&nbsp;设备别名:&nbsp;</td>
									        <td width="70%"><span id="lable"><%=alias%></span> [<a href="#" onclick="openNewWindow()"><img src="<%=rootPath%>/resource/image/editicon.gif" border=0>修改</a>]</td>
										</tr>
                                        <tr bgcolor="#F1F1F1">
                                            <td width="30%" height="26" align="left" nowrap class=txtGlobal>&nbsp;系统名称:&nbsp;</td>
                                            <td width="70%"><%=sysName%></td>
                                        </tr>
                                        <tr>
                                            <td height="26" class=txtGlobal align=left  valign=middle nowrap>&nbsp;系统OID:&nbsp;</td>
                                            <td><%=sysObjectID%></td>
                                        </tr> 
										<tr bgcolor="#F1F1F1">
											<td height="26" class=txtGlobal align="left" nowrap  >&nbsp;状态:&nbsp;</td>
											<td><img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(Integer.valueOf(maxAlarmLevel))%>">&nbsp;<%=NodeHelper.getStatusDescr(Integer.valueOf(maxAlarmLevel))%></td>
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
										<tr bgcolor="#F1F1F1">
											<td height="26" class=txtGlobal align="left" nowrap  >&nbsp;子网掩码:&nbsp;</td>
											<td><%=netMask %></td>
										</tr>
										<tr>
											<td width="30%" height="26" align=left valign=middle nowrap class=txtGlobal>&nbsp;类别:&nbsp;</td>
											<td width="70%"><%=NodeHelper.getNodeCategory(Integer.valueOf(category))%></td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td width="30%" height="26" align=left valign=middle nowrap  class=txtGlobal>&nbsp;类型:&nbsp;</td>
											<td width="70%"><%=subtype%></td>
										</tr>
										<tr>
                                            <td width="30%" height="26" align=left valign=middle nowrap class=txtGlobal>&nbsp;系统描述:&nbsp;</td>
                                            <td width="70%"><acronym title="<%=sysDescr%>"><%=sysdescrforshow%></acronym></td>
                                        </tr>
										<tr bgcolor="#F1F1F1">
											<td height="26" class=txtGlobal align=left  valign=middle nowrap  >&nbsp;设备启动时间:&nbsp;</td>
											<td><%=sysUpTime%></td>
										</tr>
										<!-- 
                                        <tr>
                                            <td width="30%" height="26" align=left valign=middle nowrap  class=txtGlobal>&nbsp;位置:&nbsp;</td>
                                            <td width="70%"><//%=sysLocation%></td>
                                        </tr>
                                         -->
										<tr bgcolor="#F1F1F1">
											<td height="26" class=txtGlobal align=left valign=middle nowrap  >&nbsp;设备MAC:&nbsp;</td>
											<td><%=mac %></td>
										</tr>
										<tr >
											<td height="26" class=txtGlobal align=left valign=middle nowrap  >&nbsp;供应商信息:&nbsp;</td>
											<td><%=supperName %></td>
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
                                <td colspan=2  width="100%" align=center valign="top">
                                    <table>
                                        <tr>
                                            <td width="50%"  align=center >
                                                <table>
                                                    <tr>
                                                        <td align=center style="height:25px">
                                                            <div>
                                                                &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" id="in" onclick="inSpeed()" checked> 入口流速</>
                                                                &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" id="out" onclick="outSpeed()" > 出口流速</>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td align=center>
                                                            <div id="utilhdx"></div>
                                                            <script type="text/javascript">
                                                                 <%if(!sbData.equals("0")){%>
                                                                    var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "ampie","288", "250", "8", "#FFFFFF");
                                                                             so.addVariable("path", "<%=rootPath%>/amchart/");
                                                                             so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/utilhdx_settings.xml"));
                                                                             so.addVariable("chart_data","<%=sbData%>");
                                                                             so.write("utilhdx");
                                                                             <%}else{%>
                                                                 var _div=document.getElementById("utilhdx");
                                                                 var img=document.createElement("img");
                                                                    img.setAttribute("src","<%=rootPath%>/resource/image/nodata.gif");
                                                                    _div.appendChild(img);
                                                                 <%}%>  
                                                            </script>                                                                                  
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                            
                                            <td width="50%" align=center >
                                                <table width=100%>
                                                    <tr>
                                                        <td align=center  style="height:25px">
                                                            <div style="height:20px">
                                                                &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" id="inband" onclick="inBandSpeed()" checked> 入口带宽</>
                                                                &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" id="outband" onclick="outBandSpeed()" > 出口带宽</>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td align=center>
                                                            <div id="bandwidth"></div>
                                                            <script type="text/javascript"
                                                             src="<%=rootPath%>/include/swfobject.js"></script>
                                                            
                                                            <script type="text/javascript">
                                                                <%if(!persbData.equals("0")){%>
                                                                  var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "ampie","288", "250", "8", "#FFFFFF");
                                                                      so.addVariable("path", "<%=rootPath%>/amchart/");
                                                                      so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/band_settings.xml"));
                                                                             so.addVariable("chart_data","<%=persbData%>");
                                                                             so.write("bandwidth");
                                                                             <%}else{%>
                                                                 var _div=document.getElementById("bandwidth");
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
function openNewWindow() {
		window.open("<%=rootPath%>/network.do?action=ready_editalias&id=<%=nodeid%>&ipaddress=<%=ipaddress%>","_blank",
			"toolbar=no,titlebar=no, location=no,directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=yes,top=300,left=300 width=370, height=150")
	
}
function openNewWindowForSysName() {
		window.open("<%=rootPath%>/network.do?action=ready_editsysgroup&id=<%=nodeid%>&ipaddress=<%=ipaddress%>","_blank",
			"toolbar=no,titlebar=no, location=no,directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=yes,top=250,left=350 width=370, height=250")
	
}
 
  function outSpeed()
  {
   
  document.getElementById('in').checked=false;
  document.getElementById('out').checked=true;
  this.utiludx("OutBandwidthUtilHdx");

  }
  function inSpeed(){
  document.getElementById('out').checked=false;
  document.getElementById('in').checked=true;
  this.utiludx("InBandwidthUtilHdx");
}
function inBandSpeed(){

 document.getElementById('outband').checked=false;
  document.getElementById('inband').checked=true;
  this.utilband("InBandwidthUtilHdxPerc");
}
function outBandSpeed(){
 document.getElementById('inband').checked=false;
  document.getElementById('outband').checked=true;
  this.utilband("OutBandwidthUtilHdxPerc");
}
function utilband(type){
 $.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=inSpeedTop5&tmp=<%=nodeid%>&ip=<%=ipaddress%>&type="+type+"&nowtime="+(new Date()),
			success:function(data){
			if(data.dataStr==0){
			var  _div=document.getElementById("bandwidth");
	        var  img=document.createElement("img");
		         img.setAttribute("src","<%=rootPath%>/resource/image/nodata.gif");
		        
			}else{
			
			var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "ampie","288", "250", "8", "#FFFFFF");
				 so.addVariable("path", "<%=rootPath%>/amchart/");
				 so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/band_settings.xml"));
				 so.addVariable("chart_data",data.dataStr);
				 so.write("bandwidth");
			}	
			}
		});
}
function utiludx(type){

 $.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=inSpeedTop5&tmp=<%=nodeid%>&ip=<%=ipaddress%>&type="+type+"&nowtime="+(new Date()),
			success:function(data){
			if(data.dataStr==0){
			var  _div=document.getElementById("bandwidth");
	        var  img=document.createElement("img");
		         img.setAttribute("src","<%=rootPath%>/resource/image/nodata.gif");
		        
			}else{
			var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "ampie","288", "250", "8", "#FFFFFF");
				 so.addVariable("path", "<%=rootPath%>/amchart/");
				 so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/utilhdx_settings.xml"));
				 so.addVariable("chart_data",data.dataStr);
				 so.write("utilhdx");
				}
			}
		});
}

</script>
