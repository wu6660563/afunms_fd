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
	String rootPath = "";
	String picip = "";
	rootPath = request.getParameter("rootPath") == null ? "" : request.getParameter("rootPath");
	picip = request.getParameter("picip") == null ? "" : request.getParameter("picip");
	String avgresponse = request.getParameter("avgresponse") == null ? "": request.getParameter("avgresponse");
	//�ڴ�������
	String pvalue = request.getParameter("pvalue") == null ? "": request.getParameter("pvalue"); 
	String platvalue = request.getParameter("platvalue") == null ? "": request.getParameter("platvalue"); 
	//avgresponse="8889"; 
	int reslength = 3;//��Ӧʱ����ʾ��λ����
	if (avgresponse.indexOf(".") > 0) {
		avgresponse = avgresponse.substring(0, avgresponse.indexOf("."));//ɾ��С�����ֵ
	}
	if (avgresponse.length() > reslength) {
		avgresponse = avgresponse.substring(0, reslength);
	}
	CreateMetersPic cmp = new CreateMetersPic();
	String path = ResourceCenter.getInstance().getSysPath()+ "resource\\image\\dashBoard1.png";
	String dominoMemPath = ResourceCenter.getInstance().getSysPath()+ "resource\\image\\dashBoard1.png";
	cmp.createPic(picip, new Double(pvalue.replaceAll("%", "")), path,"�ڴ�������", "avgpmemory");
	cmp.createPic(picip, new Double(platvalue.replaceAll("%", "")), dominoMemPath,"ƽ̨�ڴ�������", "dominomem");
	String pingavg = request.getParameter("pingavg");
	
	//�����ｫ����ת��Ϊ���� ����ط�������ʡ�Ըù���
	pingavg = pingavg.replace("%", "");
	pingavg=String.valueOf(Math.round(Float.parseFloat(pingavg)));
	 
	StringBuffer dataStr = new StringBuffer();
	dataStr.append("��ͨ;").append(pingavg).append(";false;7CFC00\\n");
	dataStr.append("δ��ͨ;").append(100-Integer.parseInt(pingavg)).append(";false;FF0000\\n");
//	dataStr.append("��ͨ;").append(34).append(";false;7CFC00\\n");
//	dataStr.append("δ��ͨ;").append(66).append(";false;FF0000\\n");
	String pingdata = dataStr.toString();
	
	String pathPing = ResourceCenter.getInstance().getSysPath()+ "resource\\image\\dashBoardGray.png";
	cmp.createChartByParam(picip , pingavg , pathPing,"��ͨ��","pingdata"); 
	
%>
<table cellPadding=0 cellspacing="0" align="center" border="1" bordercolor="#D3D3D3">
	<tr>
	  <td width="50%" align="center" valign="top">
			<table  align=center  cellpadding=0 cellspacing="0" width=100%>
				<tr bgcolor=#F1F1F1 height="29">
					<td align="center">����ƽ����ͨ�� </td>
				</tr>
				<tr  height=160>
					<td align="center"> <img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>pingdata.png"> </td>
				</tr>
			</table>
		</td>
		<td width="50%" align="center" valign="top">
			<table    align=center  cellpadding=0 cellspacing="0" width=100%>
				<tr bgcolor=#F1F1F1 height="29">
					<td align="center">
						����ƽ̨�ڴ�ƽ��������
					</td>
				</tr>
				<tr  height=160>
					<td align="center"> <img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>dominomem.png"> </td>
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
					<td align="center">����ƽ��CPU������ </td>
				</tr>
				<tr  height=160>
					<td align="center"> <img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>cpu.png"> </td>
				</tr>
				<tr height="7">
					<td align=center> <img src="<%=rootPath%>/resource/image/Loading_2.gif"> </td>
				</tr>
			</table>
		</td>

		<td width="50%" align="center" valign="top">
			<table align=center  cellpadding=0 cellspacing="0" width=100%>
				<tr bgcolor=#F1F1F1 height="29">
					<td align="center">����ƽ���ڴ�������</td>
				</tr>
				<tr height=160>
					<td align="center"><img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>avgpmemory.png"></td>
				</tr>
				<tr height="7"> 
					<td align=center>&nbsp;<img src="<%=rootPath%>/resource/image/Loading.gif"></td>
				</tr>
			</table>
		</td>
	</tr>
</table>

