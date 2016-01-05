<%@page language="java" contentType="text/html;charset=gb2312" pageEncoding="gb2312"%>
<%@page import="javax.servlet.jsp.tagext.TryCatchFinally"%>
<%@page import="com.afunms.portscan.dao.PortScanDao"%>
<%@page import="com.afunms.application.model.DominoMem"%>
<%@page import="com.afunms.application.model.DominoServer"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.common.util.*"%>
<%@page import="com.afunms.monitor.item.*"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.impl.*"%>
<%@page import="com.afunms.polling.api.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.topology.dao.*"%>

<%@ page import="com.afunms.polling.om.*"%>


<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>

<%@page import="com.afunms.temp.model.*"%>



<%
	String rootPath = request.getParameter("rootPath")==null?"":request.getParameter("rootPath");
	String tmp = request.getParameter("tmp")==null?"":request.getParameter("tmp");
	
	String sysuptime = request.getParameter("sysuptime")==null?"":request.getParameter("sysuptime");
	String collecttime = request.getParameter("collecttime")==null?"":request.getParameter("collecttime");
	String picip = request.getParameter("picip")==null?"":request.getParameter("picip");
	String status = request.getParameter("status")==null?"":request.getParameter("status");
	String flag1 = request.getParameter("flag1")==null?"":request.getParameter("flag1");
	String avgresponse = request.getParameter("avgresponse")==null?"":request.getParameter("avgresponse"); 
	String memoryvalue = request.getParameter("memoryvalue")==null?"":request.getParameter("memoryvalue");  
	String platformMemvalue = request.getParameter("platformMemvalue")==null?"":request.getParameter("platformMemvalue");  
	 Domino domino = (Domino)PollingEngine.getInstance().getDominoByID(Integer.parseInt(tmp));	
	String ipaddress=domino.getIpAddress();
	Supper supper = null;
	String suppername = "";
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String time1 = sdf.format(new Date());	
	String starttime1 = time1 + " 00:00:00";
	String totime1 = time1 + " 23:59:59";
	DominoServer server =(DominoServer)request.getAttribute("server");
	String data="";
	Vector vector =new Vector();
	Vector perVec =new Vector();
		 StringBuffer sb=new StringBuffer();
		 StringBuffer persb=new StringBuffer();
		 String sbData="",persbData="";
		 I_HostLastCollectData hostlastmanager=new HostLastCollectDataManager();
       
			String allipstr = SysUtil.doip(ipaddress);
			int statusInt=-1; 
		if(status.equalsIgnoreCase("TRUE")){
		statusInt=0;
		}else if(status.equalsIgnoreCase("FALSE")){
		statusInt=3;
		}
  
%>

<script type="text/javascript">


</script>
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
						<table  cellpadding=0 width=100% align=center algin="center">
							<tr>
								<td width="60%" align="left" valign="top">
								<table cellpadding="0" cellspacing="0" width=100% align=center>
									<tr>
     										<td width="30%" height="26" align="left" nowrap >&nbsp;系统名称:</td>
     										<td width="70%">
     										   <span id="lable"><%=domino.getName()%></span>
     										</td>
   									</tr>
   									<tr bgcolor="#F1F1F1">
     										<td width="30%" height="26" align="left" nowrap>&nbsp;IP:</td>
     										<td width="70%">
      										<span id="sysname"><%=ipaddress%></span>[<a href="#" onclick="openNewWindowForSysName()"><img src="<%=rootPath%>/resource/image/editicon.gif" border=0>修改</a>]
    										</td>
   									</tr> 
   									<tr>
     										<td width="30%" height="26" align="left">&nbsp;状态:</td>
     										<td>
      										<img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(statusInt)%>">
      										&nbsp;
      										<%=NodeHelper.getStatusDescr(statusInt)%>
     										</td>
   									</tr> 
   									<tr bgcolor="#F1F1F1">
     										<td width="30%" height="26" align="left" nowrap >&nbsp;服务器名称:</td>
     										<td width="70%">
     										   <span id="lable"><%=server.getName()%></span>
     										</td>
   									</tr>
   									<tr >
     										<td width="30%" height="26" align="left" nowrap>&nbsp;服务器标题:</td>
     										<td width="70%">
      										<span id="title"><%=server.getTitle()%></span>
    										</td>
   									</tr> 
   									<tr bgcolor="#F1F1F1">
     										<td width="30%" height="26" align="left" nowrap >&nbsp;服务器版本:</td>
     										<td width="70%">
     										   <span id="lable"><%=server.getOs()%></span>
     										</td>
   									</tr>
   									<tr >
     										<td width="30%" height="26" align="left" nowrap>&nbsp;服务器位数:</td>
     										<td width="70%">
      										<span id="sysname"><%=server.getArchitecture()%></span>
    										</td>
   									</tr> 
   									<tr bgcolor="#F1F1F1">
     										<td width="30%" height="26" align="left" nowrap >&nbsp;监视启动时间:</td>
     										<td width="70%">
     										   <span id="lable"><%=server.getStarttime()%></span>
     										</td>
   									</tr>
   									<tr>
     										<td width="30%" height="26" align="left" nowrap>&nbsp;服务器CPU类型:</td>
     										<td width="70%">
      										<span id="sysname"><%=server.getCputype()%></span>
    										</td>
   									</tr> 
   									<tr bgcolor="#F1F1F1">
     										<td width="30%" height="26" align="left" nowrap >&nbsp;服务器CPU数量:</td>
     										<td width="70%">
     										   <span id="lable"><%=server.getCpucount()%></span>
     										</td>
   									</tr>
   									
   								</table>
								</td>						
								<td width="40%" align="right" valign="top">
									<jsp:include page="/topology/includejsp/domino_graphic.jsp">
										<jsp:param name="rootPath" value="<%=rootPath %>"/>
										<jsp:param name="picip" value="<%=picip %>"/> 
										<jsp:param name="avgresponse" value="<%=avgresponse %>"/>  
										<jsp:param name="pvalue" value="<%=memoryvalue %>"/>	 
										<jsp:param name="platvalue" value="<%=platformMemvalue %>"/>	 
									</jsp:include>
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