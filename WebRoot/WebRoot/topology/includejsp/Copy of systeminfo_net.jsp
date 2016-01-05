<%@page language="java" contentType="text/html;charset=gb2312"
    pageEncoding="gb2312"%>
<%@page import="javax.servlet.jsp.tagext.TryCatchFinally"%>
<%@page import="com.afunms.portscan.dao.PortScanDao"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.common.util.*"%>
<%@page import="com.afunms.monitor.item.*"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.impl.*"%>
<%@page import="com.afunms.polling.api.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.monitor.item.base.MoidConstants"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@ page import="com.afunms.polling.api.I_Portconfig"%>
<%@ page import="com.afunms.polling.om.Portconfig"%>
<%@ page import="com.afunms.polling.om.*"%>
<%@ page import="com.afunms.polling.impl.PortconfigManager"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>

<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>

<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.detail.net.service.NetService"%>
<%@page import="com.afunms.temp.model.NodeTemp"%>
<%@page import="com.afunms.temp.model.*"%>
<%@page import="com.afunms.polling.loader.HostLoader"%>


<%
    String rootPath = request.getParameter("rootPath") == null ? ""
            : request.getParameter("rootPath");
    String tmp = request.getParameter("tmp") == null ? "" : request
            .getParameter("tmp");
    String sysdescr = request.getParameter("sysdescr") == null ? ""
            : request.getParameter("sysdescr");
    String sysuptime = request.getParameter("sysuptime") == null ? ""
            : request.getParameter("sysuptime");
    String collecttime = request.getParameter("collecttime") == null ? ""
            : request.getParameter("collecttime");
    String picip = request.getParameter("picip") == null ? "" : request
            .getParameter("picip");
    String syslocation = request.getParameter("syslocation") == null ? ""
            : request.getParameter("syslocation");
    String flag1 = request.getParameter("flag1") == null ? "" : request
            .getParameter("flag1");
    String avgresponse = request.getParameter("avgresponse") == null ? ""
            : request.getParameter("avgresponse");
    String memoryvalue = request.getParameter("memoryvalue") == null ? ""
            : request.getParameter("memoryvalue");
    Host host = (Host) PollingEngine.getInstance().getNodeByID(
            Integer.parseInt(tmp));
    SupperDao supperdao = new SupperDao();
    Supper supper = null;
    String suppername = "";
    try {
        supper = (Supper) supperdao.findByID(host.getSupperid() + "");
        if (supper != null)
            suppername = supper.getSu_name() + "("
                    + supper.getSu_dept() + ")";
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        supperdao.close();
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
        vector = hostlastmanager.getInterface(host.getIpAddress(),
                netInterfaceItem1, "InBandwidthUtilHdx", starttime1,
                totime1);
        perVec = hostlastmanager.getInterface(host.getIpAddress(),
                netInterfaceItem2, "InBandwidthUtilHdxPerc",
                starttime1, totime1);
        //			
        String allipstr = SysUtil.doip(host.getIpAddress());

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
                        "kb/秒", "");

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

    portVec = dao.getCurrentStatus(host.getIpAddress());

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

<script type="text/javascript">
function openNewWindow() {
		window.open("<%=rootPath%>/network.do?action=ready_editalias&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>","_blank",
			"toolbar=no,titlebar=no, location=no,directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=yes,top=300,left=300 width=370, height=150")
	
}
function openNewWindowForSysName() {
		window.open("<%=rootPath%>/network.do?action=ready_editsysgroup&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>","_blank",
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
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=inSpeedTop5&tmp=<%=tmp%>&ip=<%=host.getIpAddress()%>&type="+type+"&nowtime="+(new Date()),
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
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=inSpeedTop5&tmp=<%=tmp%>&ip=<%=host.getIpAddress()%>&type="+type+"&nowtime="+(new Date()),
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
<table id="detail-content" class="detail-content">
    <tr>
        <td>
            <table width="100%"
                background="<%=rootPath%>/common/images/right_t_02.jpg"
                cellspacing="0" cellpadding="0">
                <tr>
                    <td align="left">
                        <img
                            src="<%=rootPath%>/common/images/right_t_01.jpg"
                            width="5" height="29" />
                    </td>
                    <td class="layout_title">
                        <b>设备详细信息</b>
                    </td>
                    <td align="right">
                        <img
                            src="<%=rootPath%>/common/images/right_t_03.jpg"
                            width="5" height="29" />
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table id="detail-content-body" class="detail-content-body">
                <tr>
                    <td>
                        <table cellpadding=0 width=100% align=center
                            algin="center">
                            <tr>
                                <td width="60%" align="left"
                                    valign="top">
                                    <table>
                                        <tr>
                                            <td width="30%" height="26"
                                                align="left" nowrap
                                                class=txtGlobal>
                                                &nbsp;设备标签:
                                            </td>
                                            <!--  	<td width="70%"><%=host.getAlias()%> [<a href="<%=rootPath%>/network.do?action=ready_editalias&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><img src="<%=rootPath%>/resource/image/editicon.gif" border=0>修改</a>]</td>-->
                                            <td width="70%">
                                                <span id="lable"><%=host.getAlias()%></span>
                                                [
                                                <a href="#"
                                                    onclick="openNewWindow()"><img
                                                        src="<%=rootPath%>/resource/image/editicon.gif"
                                                        border=0>修改</a>]
                                            </td>
                                        </tr>
                                        <tr bgcolor="#F1F1F1">
                                            <td width="30%" height="26"
                                                align="left" nowrap
                                                class=txtGlobal>
                                                &nbsp;系统名称:
                                            </td>
                                            <td width="70%">
                                                <span id="sysname"><%=host.getSysName()%></span>
                                                [
                                                <a href="#"
                                                    onclick="openNewWindowForSysName()"><img
                                                        src="<%=rootPath%>/resource/image/editicon.gif"
                                                        border=0>修改</a>]
                                            </td>
                                        </tr>
                                        <tr>
                                            <td height="26"
                                                class=txtGlobal
                                                align="left" nowrap>
                                                &nbsp;状态:
                                            </td>
                                            <td>
                                                <img
                                                    src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(host.getStatus())%>">
                                                &nbsp;<%=NodeHelper.getStatusDescr(host.getStatus())%></td>
                                        </tr>
                                        <tr bgcolor="#F1F1F1">
                                            <td width="30%" height="26"
                                                align=left valign=middle
                                                nowrap class=txtGlobal>
                                                &nbsp;IP地址:
                                            </td>
                                            <%
                                                IpAliasDao ipdao = new IpAliasDao();
                                                List iplist = ipdao.loadByIpaddress(host.getIpAddress());
                                                ipdao.close();
                                                ipdao = new IpAliasDao();
                                                IpAlias ipalias = ipdao
                                                        .getByIpAndUsedFlag(host.getIpAddress(), "1");
                                                ipdao.close();
                                                if (iplist == null)
                                                    iplist = new ArrayList();
                                            %>
                                            <TD nowrap width="40%">
                                                <select
                                                    name="ipalias<%=host.getIpAddress()%>">
                                                    <option selected><%=host.getIpAddress()%></option>
                                                    <%
                                                        for (int j = 0; j < iplist.size(); j++) {
                                                            IpAlias voTemp = (IpAlias) iplist.get(j);
                                                    %>
                                                    <option
                                                        <%if(ipalias != null && ipalias.getAliasip().equals(voTemp.getAliasip())){ %>
                                                        selected <%} %>><%=voTemp.getAliasip()%></option>
                                                    <%
                                                        }
                                                    %>
                                                </select>
                                                [
                                                <a href="#"
                                                    style="cursor: hand"
                                                    onclick="modifyIpAliasajax('<%=host.getIpAddress()%>');">修改</a>
                                                ]
                                            </TD>
                                        </tr>
                                        <tr>
                                            <td height="26"
                                                class=txtGlobal
                                                align="left" nowrap>
                                                &nbsp;子网掩码:
                                            </td>
                                            <td><%=host.getNetMask()%></td>
                                        </tr>
                                        <tr bgcolor="#F1F1F1">
                                            <td width="30%" height="26"
                                                align=left valign=middle
                                                nowrap class=txtGlobal>
                                                &nbsp;类别:
                                            </td>
                                            <td width="70%"><%=NodeHelper.getNodeCategory(host.getCategory())%></td>
                                        </tr>
                                        <tr>
                                            <td width="30%" height="26"
                                                align=left valign=middle
                                                nowrap class=txtGlobal>
                                                &nbsp;类型:
                                            </td>
                                            <td width="70%"><%=host.getType()%></td>
                                        </tr>
                                        <tr bgcolor="#F1F1F1">
                                            <td width="30%" height="26"
                                                align=left valign=middle
                                                nowrap class=txtGlobal>
                                                &nbsp;位置:
                                            </td>
                                            <td width="70%"><%=syslocation%></td>
                                        </tr>
                                        <tr>
                                            <td width="30%" height="26"
                                                align=left valign=middle
                                                nowrap class=txtGlobal>
                                                &nbsp;系统描述:
                                            </td>
                                            <td width="70%">
                                                <%
                                                    String sysdescrforshow = "";//用于显示设备信息简称
                                                    if (sysdescr != "" && sysdescr != null) {
                                                        if (sysdescr.length() > 40) {
                                                            sysdescrforshow = sysdescr.substring(0, 40) + "...";
                                                        } else {
                                                            sysdescrforshow = sysdescr;
                                                        }
                                                    }
                                                %>
                                                <acronym
                                                    title="<%=sysdescr%>"><%=sysdescrforshow%></acronym>
                                            </td>
                                        </tr>
                                        <tr bgcolor="#F1F1F1">
                                            <td height="26"
                                                class=txtGlobal
                                                valign=middle nowrap>
                                                &nbsp;设备启动时间:
                                            </td>
                                            <td><%=sysuptime%></td>
                                        </tr>
                                        <tr>
                                            <td width="30%" height="26"
                                                align=left valign=middle
                                                nowrap class=txtGlobal>
                                                &nbsp;数据采集时间:
                                            </td>
                                            <td width="70%"><%=collecttime%></td>
                                        </tr>
                                        <tr bgcolor="#F1F1F1">
                                            <td height="26"
                                                class=txtGlobal
                                                valign=middle nowrap>
                                                &nbsp;设备MAC:
                                            </td>
                                            <td><%=host.getMac()%></td>
                                        </tr>
                                        <tr>
                                            <td width="30%" height="26"
                                                align="left" nowrap
                                                class=txtGlobal>
                                                &nbsp;设备资产编号:
                                            </td>
                                            <td width="70%">
                                                <span id="assetid"><%=host.getAssetid()%></span>
                                            </td>
                                        </tr>
                                        <tr bgcolor="#F1F1F1">
                                            <td width="30%" height="26"
                                                align="left" nowrap
                                                class=txtGlobal>
                                                &nbsp;机房位置:
                                            </td>
                                            <td width="70%">
                                                <span id="location"><%=host.getLocation()%></span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td height="26"
                                                class=txtGlobal
                                                valign=middle nowrap>
                                                &nbsp;系统OID:
                                            </td>
                                            <td><%=host.getSysOid()%></td>
                                        </tr>
                                        <tr bgcolor="#F1F1F1">
                                            <td height="26"
                                                class=txtGlobal
                                                valign=middle nowrap>
                                                &nbsp;供应商信息:
                                            </td>
                                            <td>
                                                <%
                                                    if (supper != null) {
                                                %>
                                                <a href="#"
                                                    style="cursor: hand"
                                                    onclick="window.showModalDialog('<%=rootPath%>/supper.do?action=read&id=<%=supper.getSu_id()%>',window,',dialogHeight:400px;dialogWidth:600px')"><%=suppername%></a>
                                                <%
                                                    }
                                                %>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td width="40%" align="right"
                                    valign="top">
                                    <jsp:include
                                        page="/topology/includejsp/systeminfo_graphic.jsp">
                                        <jsp:param name="rootPath"
                                            value="<%=rootPath%>" />
                                        <jsp:param name="picip"
                                            value="<%=picip%>" />
                                        <jsp:param name="avgresponse"
                                            value="<%=avgresponse%>" />
                                        <jsp:param name="pvalue"
                                            value="<%=memoryvalue%>" />
                                    </jsp:include>
                                </td>
                            </tr>
                            <tr>
                                <td colspan=2 width="100%" align=center
                                    valign="top">
                                    <table>
                                        <tr>
                                            <td width="30%" align=center>
                                                <table>
                                                    <tr>
                                                        <td
                                                            style="height: 15px; font-size: 14px"
                                                            align=center>
                                                            <%
                                                                if (dataStr.equals("0")) {
                                                            %>
                                                            <b>当前关键端口的状态</b>
                                                            <%
                                                                }
                                                            %>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td align=center>
                                                            <div
                                                                id="portPing"></div>
                                                            <script
                                                                type="text/javascript">
	 <%if(!dataStr.equals("0")){%>
		var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "ampie","262", "240", "8", "#FFFFFF");
				 so.addVariable("path", "<%=rootPath%>/amchart/");
				 so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/hostPing_settings.xml"));
				 so.addVariable("chart_data","<%=dataStr%>");
				 so.write("portPing");	
	 <%}else{%>
	 var _div=document.getElementById("portPing");
	 var img=document.createElement("img");
		img.setAttribute("src","<%=rootPath%>/resource/image/nodata.gif");
		_div.appendChild(img);
	 <%}%>				 
				 </script>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>

                                            <td width="35%" align=center>
                                                <table>
                                                    <tr>
                                                        <td align=center
                                                            style="height: 25px">
                                                            <div>
                                                                &nbsp;&nbsp;&nbsp;&nbsp;
                                                                <input
                                                                    type="radio"
                                                                    id="in"
                                                                    onclick="inSpeed()"
                                                                    checked>
                                                                入口流速</>
                                                                &nbsp;&nbsp;&nbsp;&nbsp;
                                                                <input
                                                                    type="radio"
                                                                    id="out"
                                                                    onclick="outSpeed()">
                                                                出口流速</>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td align=center>
                                                            <div
                                                                id="utilhdx"></div>
                                                            <script
                                                                type="text/javascript">
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

                                            <td width="35%" align=center>
                                                <table width=100%>
                                                    <tr>
                                                        <td align=center
                                                            style="height: 25px">
                                                            <div
                                                                style="height: 20px">
                                                                &nbsp;&nbsp;&nbsp;&nbsp;
                                                                <input
                                                                    type="radio"
                                                                    id="inband"
                                                                    onclick="inBandSpeed()"
                                                                    checked>
                                                                入口带宽</>
                                                                &nbsp;&nbsp;&nbsp;&nbsp;
                                                                <input
                                                                    type="radio"
                                                                    id="outband"
                                                                    onclick="outBandSpeed()">
                                                                出口带宽</>
                                                            </div>
                                                        </td>
                                                    </tr>

                                                    <tr>
                                                        <td align=center>
                                                            <div
                                                                id="bandwidth"></div>

                                                            <script
                                                                type="text/javascript"
                                                                src="<%=rootPath%>/include/swfobject.js"></script>

                                                            <script
                                                                type="text/javascript">
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
            <jsp:include
                page="/topology/includejsp/detail_content_footer.jsp" />
        </td>
    </tr>
</table>