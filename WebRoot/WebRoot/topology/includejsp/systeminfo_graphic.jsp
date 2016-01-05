<%@ page language="java" contentType="text/html;charset=gb2312"%>
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
<%@ page import="com.afunms.polling.loader.HostLoader"%>

<%@page import="com.afunms.initialize.*"%>
<%
	String rootPath = request.getParameter("rootPath") == null ? "" : request.getParameter("rootPath");
    String curPingImage = request.getParameter("curPingImage")==null?"":request.getParameter("curPingImage");
    String curResponseTimeValue = request.getParameter("curResponseTimeValue")==null?"":request.getParameter("curResponseTimeValue");
    String curMemoryImage = request.getParameter("curPingImage")==null?"":request.getParameter("curMemoryImage");
    String curCPUImage = request.getParameter("curCPUImage")==null?"":request.getParameter("curCPUImage");
    int reslength = 3;//响应时间显示的位数；
    if (curResponseTimeValue.indexOf(".") > 0) {
        curResponseTimeValue = curResponseTimeValue.substring(0, curResponseTimeValue.indexOf("."));//删除小数点后值
    }
    if (curResponseTimeValue.length() > reslength) {
        curResponseTimeValue = curResponseTimeValue.substring(0, reslength);
    }
%>
<table cellPadding=0 cellspacing="0" align="center" border="1" bordercolor="#D3D3D3">
	<tr>
	  <td width="50%" align="center" valign="top">
			<table  align=center  cellpadding=0 cellspacing="0" width=100%>
				<tr bgcolor=#F1F1F1 height="29">
					<td align="center">当前平均连通率 </td>
				</tr>
				<tr  height=160>
					<td align="center"> <img src="<%=rootPath%>/<%=curPingImage%>"> </td>
				</tr>
			</table>
		</td>
		<td width="50%" align="center" valign="top">
			<table    align=center  cellpadding=0 cellspacing="0" width=100%>
				<tr bgcolor=#F1F1F1 height="29">
					<td align="center">
						当前平均响应时间
					</td>
				</tr>
				<tr  height=160>
					<td align="center">
						<table style="align: center; width: 100%; background-image: url(<%=rootPath%>/resource/image/chartdirector/responsebg.png); background-repeat: no-repeat; background-position: center;">
							<tr height=160>
								<td align="center">
									<%
										if (curResponseTimeValue != null && curResponseTimeValue != "") {
											for (int i = 0; i < reslength - curResponseTimeValue.length(); i++) {
									%>
									<img src="<%=rootPath%>/resource/image/chartdirector/0.png" border="0">
									<%
										}
											for (int i = 0; i < curResponseTimeValue.length(); i++) {
									%>
									<img
										src="<%=rootPath%>/resource/image/chartdirector/<%=curResponseTimeValue.charAt(i)%>.png"
										border="0">
									<%
										}
										} else {
									%>
									<%
										for (int i = 0; i < reslength; i++) {
									%>
									<img src="<%=rootPath%>/resource/image/chartdirector/0.png" border="0">
									<%
										}
										}
									%>
									<img src="<%=rootPath%>/resource/image/chartdirector/ms.png" border="0">
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr height="7">
					<td align=center>
						&nbsp;
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td width="50%" align="center" valign="top">
			<table  align=center  cellpadding=0 cellspacing="0" width=100%>
				<tr bgcolor=#F1F1F1 height="29">
					<td align="center">当前平均CPU利用率 </td>
				</tr>
				<tr  height=160>
					<td align="center"> <img src="<%=rootPath%>/<%=curCPUImage%>"> </td>
				</tr>
				<tr height="7">
					<td align=center> <img src="<%=rootPath%>/resource/image/Loading_2.gif"> </td>
				</tr>
			</table>
		</td>

		<td width="50%" align="center" valign="top">
			<table align=center  cellpadding=0 cellspacing="0" width=100%>
				<tr bgcolor=#F1F1F1 height="29">
					<td align="center">当前平均内存利用率</td>
				</tr>
				<tr height=160>
					<td align="center"><img src="<%=rootPath%>/<%=curMemoryImage%>"></td>
				</tr>
				<tr height="7"> 
					<td align=center>&nbsp;<img src="<%=rootPath%>/resource/image/Loading.gif"></td>
				</tr>
			</table>
		</td>
	</tr>
</table>

