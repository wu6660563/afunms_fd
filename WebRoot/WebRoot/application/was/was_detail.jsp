<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.common.util.*" %>
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

<%@page import="com.afunms.config.dao.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.application.wasmonitor.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%
System.out.println("1111======================================");
  Hashtable hs = null;
  Hashtable JDBCProviderHst = null;
  Hashtable SessionManagerHst = null;
  Hashtable SystemMetricsHst = null;
  Hashtable ThreadPoolHst = null;
  Hashtable DynaCacheHst = null;
  Hashtable TransactionServiceHst = null;
  Hashtable ORBHst = null;
  Hashtable JvmHst = null;
  String rootPath = request.getContextPath();
System.out.println("3333======================================");
  WasConfig vo  = (WasConfig)request.getAttribute("was");
  hs = (Hashtable)com.afunms.common.util.ShareData.getWasdata().get("was"+":"+vo.getIpaddress());
  JvmHst = (Hashtable)hs.get("JvmHst");
  JDBCProviderHst = (Hashtable)hs.get("JDBCProviderHst");
  SessionManagerHst = (Hashtable)hs.get("SessionManagerHst");
  SystemMetricsHst = (Hashtable)hs.get("SystemMetricsHst");
  ThreadPoolHst = (Hashtable)hs.get("ThreadPoolHst");
  DynaCacheHst = (Hashtable)hs.get("DynaCacheHst");
  TransactionServiceHst = (Hashtable)hs.get("TransactionServiceHst");
  ORBHst = (Hashtable)hs.get("ORBHst");
 double wasping=0;
WasManager wm = new WasManager();
        wasping=(double)wm.weblogicping(vo.getId());
		
		int percent1 = Double.valueOf(wasping).intValue();
		int percent2 = 100-percent1;
System.out.println("4444======================================");

%>
<% String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>

<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 



<script language="javascript">	

  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
 Ext.get("process").on("click",function(){
  
  Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');   
  //location.href="EventqueryMgr.do";
        //msg.style.display="block";
        mainForm.action = "<%=rootPath%>/cfgfile.do?action=getcfgfile&ipaddress=<%=vo.getIpaddress()%>";
        mainForm.submit();
 });	
	
});

  function doQuery()
  {  
     if(mainForm.key.value=="")
     {
     	alert("�������ѯ����");
     	return false;
     }
     mainForm.action = "<%=rootPath%>/network.do?action=find";
     mainForm.submit();
  }
  
  function doChange()
  {
     if(mainForm.view_type.value==1)
        window.location = "<%=rootPath%>/topology/network/index.jsp";
     else
        window.location = "<%=rootPath%>/topology/network/port.jsp";
  }

  function toAdd()
  {
      mainForm.action = "<%=rootPath%>/network.do?action=ready_add";
      mainForm.submit();
  } 
  

</script>
<script language="JavaScript" type="text/JavaScript">
var show = true;
var hide = false;
//�޸Ĳ˵������¼�ͷ����
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
//��Ӳ˵�	
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
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa" onload="initmenu();">


<form method="post" name="mainForm">
<input type=hidden name="orderflag">
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
							<td class="td-container-main-detail">
								<table id="container-main-detail" class="container-main-detail">
									<tr>
										<td>
											<table id="detail-content" class="detail-content">
												<tr>
													<td>
														<table id="detail-content-header" class="detail-content-header">
										                	<tr>
											                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29"/></td>
					                                        <td class="detail-content-title">Ӧ�� >>WAS���� >> <%=vo.getName()%> ��ϸ��Ϣ</td>
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
				        											<table>
										               					<tr>
										     								<td width="70%" align="left" valign="top">
										     									<table style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa  cellpadding=0 rules=none width=100% align=center border=1 algin="center">
                          						<tr>
                								<td width="80%" align="left" valign="top" class=dashLeft>
                									<table style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa  cellpadding=0 rules=none width=100% align=center border=1 algin="center">
													<td height="28" colspan="4" valign=center nowrap class=txtGlobal><div class="txtGlobalBigBold">WAS��Ϣ</div></td>
                    										</tr>      
                                       <tr bgcolor="#F1F1F1">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;����:</td>
                      											<td width="35%"><%=vo.getName()%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;�ڵ�����:</td>
                      											<td width="35%"><%=vo.getNodename()%> </td>
                    										</tr>
                    										<tr>
                    											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;IP��ַ:</td>
                      											<td width="35%"><%=vo.getIpaddress()%> </td>
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;��������:</td>
                      											<td width="35%"><%=vo.getServername()%> </td>
                    										</tr>   
                    											<tr bgcolor="#F1F1F1">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;�˿�:</td>
                      											<td width="35%"><%=vo.getPortnum()%> </td>
                    											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;����״̬:</td>
                      											<td width="35%"><%=vo.getMon_flag()%> </td>
                    										</tr>                 						
                    										     										     								
                									</table>
                									   <table width="100%" border="0" cellpadding="3" cellspacing="1" bgcolor="#FFFFFF">
                                                      <tr bgcolor="#D1DDF5">
                      											<td height="28" colspan="4" valign=center nowrap class=txtGlobal><div class="txtGlobalBigBold">JVM��Ϣ</div></td>
                    										</tr>
                    										<tr bgcolor="#F1F1F1">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>JVM�����е�ʱ����(��):</td>
                      											<td width="35%"><%=JvmHst.get("UpTime")%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>JVM����ʱ�е����ڴ棨ǧ�ֽڣ�:</td>
                      											<td width="35%"><%=JvmHst.get("HeapSize")%> </td>
                    										</tr>
                    										<tr>
                    											<td width="15%" height="26" align="left" nowrap class=txtGlobal>CPUʹ����:</td>
                      											<td width="35%"><%=JvmHst.get("ProcessCpuUsage")%> </td>
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>JVM����ʱ��ʹ�õ��ڴ�������ǧ�ֽڣ�:</td>
                      											<td width="35%"><%=JvmHst.get("UsedMemory")%> </td>
                    										</tr>   
                    											<tr bgcolor="#F1F1F1">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>JVM����ʱ�еĿ����ڴ棨ǧ�ֽڣ�:</td>
                      											<td width="35%"><%=JvmHst.get("FreeMemory")%> </td>
                    										</tr>                       						
                                                    </table>
                                                      <table width="100%" border="0" cellpadding="3" cellspacing="1" bgcolor="#FFFFFF">
                                                      <tr bgcolor="#D1DDF5">
                      											<td height="28" colspan="4" valign=center nowrap class=txtGlobal><div class="txtGlobalBigBold">JDBC��Ϣ</div></td>
                    										</tr>
                    										<tr bgcolor="#F1F1F1">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>���еĿ���������:</td>
                      											<td width="35%"><%=JDBCProviderHst.get("FreePoolSize")%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>ʹ�����ӵ�ƽ��ʱ�䣨���룩:</td>
                      											<td width="35%"><%=JDBCProviderHst.get("UseTime")%> </td>
                    										</tr>
                    										<tr>
                    											<td width="15%" height="26" align="left" nowrap class=txtGlobal>���ٻ��������������������:</td>
                      											<td width="35%"><%=JDBCProviderHst.get("PrepStmtCacheDiscardCount")%> </td>
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>���ص��ص����ӵ�����:</td>
                      											<td width="35%"><%=JDBCProviderHst.get("ReturnCount")%> </td>
                    										</tr>   
                    											<tr bgcolor="#F1F1F1">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>��������ӵ�����:</td>
                      											<td width="35%"><%=JDBCProviderHst.get("AllocateCount")%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>���е����ӳ�ʱ��:</td>
                      											<td width="35%"><%=JDBCProviderHst.get("FaultCount")%> </td>
                    										</tr>
                    										<tr>
                    											<td width="15%" height="26" align="left" nowrap class=txtGlobal>�ض����ӳ�ManagedConnection������:</td>
                      											<td width="35%"><%=JDBCProviderHst.get("ManagedConnectionCount")%> </td>
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>�ȴ����ӵ�ƽ�������߳���:</td>
                      											<td width="35%"><%=JDBCProviderHst.get("WaitingThreadCount")%> </td>
                    										</tr>   
															<tr bgcolor="#F1F1F1">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>����JDBCƽ������ʱ�䣨���룩:</td>
                      											<td width="35%"><%=JDBCProviderHst.get("JDBCTime")%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>�������ӵ�ƽ��ʹ��ʱ��ٷֱ�:</td>
                      											<td width="35%"><%=JDBCProviderHst.get("PercentMaxed")%> </td>
                    										</tr>
                    										<tr>
                    											<td width="15%" height="26" align="left" nowrap class=txtGlobal>��������֮ǰ��ƽ���ȴ�ʱ�䣨���룩:</td>
                      											<td width="35%"><%=JDBCProviderHst.get("WaitTime")%> </td>
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>�������ӵ�����:</td>
                      											<td width="35%"><%=JDBCProviderHst.get("CreateCount")%> </td>
                    										</tr>   
															<tr bgcolor="#F1F1F1">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>�����ض����ӳص�Connection������:</td>
                      											<td width="35%"><%=JDBCProviderHst.get("ConnectionHandleCount")%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>�ص�ƽ��ʹ����:</td>
                      											<td width="35%"><%=JDBCProviderHst.get("PercentUsed")%> </td>
                    										</tr>
                    										<tr>
                    											<td width="15%" height="26" align="left" nowrap class=txtGlobal>���ӳصĴ�С:</td>
                      											<td width="35%"><%=JDBCProviderHst.get("PoolSize")%> </td>
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>�ѹرյ����ӵ�����:</td>
                      											<td width="35%"><%=JDBCProviderHst.get("CloseCount")%> </td>
                    										</tr>   
															
                        </table>
						  <table width="100%" border="0" cellpadding="3" cellspacing="1" bgcolor="#FFFFFF">
                                                      <tr bgcolor="#D1DDF5">
                      											<td height="28" colspan="4" valign=center nowrap class=txtGlobal><div class="txtGlobalBigBold">SessionManager��Ϣ</div></td>
                    										</tr>

                    										<tr bgcolor="#F1F1F1">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>�Բ��ٴ��ڵĻỰ��������:</td>
                      											<td width="35%"><%=SessionManagerHst.get("ActivateNonExistSessionCount")%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>��ǿ��������ٻ���ĻỰ������:</td>
                      											<td width="35%"><%=SessionManagerHst.get("CacheDiscardCount")%> </td>
                    										</tr>
															<tr>
                    											<td width="15%" height="26" align="left" nowrap class=txtGlobal>��ǰ���ĻỰ����:</td>
                      											<td width="35%"><%=SessionManagerHst.get("LiveCount")%> </td>
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>ƽ���Ự�����ڣ����룩:</td>
                      											<td width="35%"><%=SessionManagerHst.get("LifeTime")%> </td>
                    										</tr>   
                    											<tr bgcolor="#F1F1F1">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>ǰһ���͵�ǰ����ʱ����ǵ�ʱ��֮����룩:</td>
                      											<td width="35%"><%=SessionManagerHst.get("TimeSinceLastActivated")%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>д���־��Դ洢�ĻỰ���ݴ�:</td>
                      											<td width="35%"><%=SessionManagerHst.get("ExternalWriteSize")%> </td>
                    										</tr>
                    										<tr>
                    											<td width="15%" height="26" align="left" nowrap class=txtGlobal>�����ĻỰ��:</td>
                      											<td width="35%"><%=SessionManagerHst.get("CreateCount")%> </td>
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>��������AllowOverflow=false���ڴ��еĻỰ:</td>
                      											<td width="35%"><%=SessionManagerHst.get("NoRoomForNewSessionCount")%> </td>
                    										</tr>   
															<tr bgcolor="#F1F1F1">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>Session���ݴ�С:</td>
                      											<td width="35%"><%=SessionManagerHst.get("SessionObjectSize")%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>�ӳ־��Դ洢д�Ự���ݻ��ѵ�ʱ�䣨���룩:</td>
                      											<td width="35%"><%=SessionManagerHst.get("ExternalWriteTime")%> </td>
                    										</tr>
                    										<tr>
                    											<td width="15%" height="26" align="left" nowrap class=txtGlobal>�ӳ־��Դ洢��ȡ�ĻỰ���ݴ�С:</td>
                      											<td width="35%"><%=SessionManagerHst.get("ExternalReadSize")%> </td>
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>�жϵ�HTTP�Ự��Ե��ϵ��:</td>
                      											<td width="35%"><%=SessionManagerHst.get("AffinityBreakCount")%> </td>
                    										</tr>   
															<tr bgcolor="#F1F1F1">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>ƽ���Ự�����ڣ����룩:</td>
                      											<td width="35%"><%=SessionManagerHst.get("InvalidateCount")%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>��ʱʧЧ�ĻỰ��:</td>
                      											<td width="35%"><%=SessionManagerHst.get("TimeoutInvalidationCount")%> </td>
                    										</tr>
                    										<tr>
                    											<td width="15%" height="26" align="left" nowrap class=txtGlobal>����ǰ���ʵĻỰ����:</td>
                      											<td width="35%"><%=SessionManagerHst.get("ActiveCount")%> </td>
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>�ӳ־��Դ洢���Ự���ݻ��ѵ�ʱ�䣨���룩:</td>
                      											<td width="35%"><%=SessionManagerHst.get("ExternalReadTime")%> </td>
                    										</tr>   
													</table>
													 <table width="100%" border="0" cellpadding="3" cellspacing="1" bgcolor="#FFFFFF">
                                                      <tr bgcolor="#D1DDF5">
                      											<td height="28" colspan="4" valign=center nowrap class=txtGlobal><div class="txtGlobalBigBold">SystemMetrics��Ϣ</div></td>
                    										</tr>
                    										<tr bgcolor="#F1F1F1">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>�����ڴ�Ŀ��գ�KB��:</td>
                      											<td width="35%"><%=SystemMetricsHst.get("FreeMemory")%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>�Է���������������CPUƽ��ʹ����:</td>
                      											<td width="35%"><%=SystemMetricsHst.get("CPUUsageSinceServerStarted")%> </td>
                    										</tr>
                    										<tr>
                    											<td width="15%" height="26" align="left" nowrap class=txtGlobal>���ϴβ�ѯ������ƽ��CPUʹ����:</td>
                      											<td width="35%"><%=SystemMetricsHst.get("CPUUsageSinceLastMeasurement")%> </td>
																
                    										</tr>                      						
                                                    </table>
													<table width="100%" border="0" cellpadding="3" cellspacing="1" bgcolor="#FFFFFF">
                                                      <tr bgcolor="#D1DDF5">
                      											<td height="28" colspan="4" valign=center nowrap class=txtGlobal><div class="txtGlobalBigBold">DynaCache��Ϣ</div></td>
                    										</tr>

                    										<tr bgcolor="#F1F1F1">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>�ɴ˸�������Эͬ�����ĸ��ٻ������ɵĿɸ��ٻ�������������Ŀ:</td>
                      											<td width="35%"><%=DynaCacheHst.get("DistributedRequestCount")%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>�ڴ��ڸ��ٻ�����Ŀ�ĵ�ǰ��:</td>
                      											<td width="35%"><%=DynaCacheHst.get("InMemoryCacheEntryCount")%> </td>
                    										</tr>
															<tr>
                    											<td width="15%" height="26" align="left" nowrap class=txtGlobal>�ڴ�ʹ����е�ǰʹ�õĸ��ٻ�����Ŀ��:</td>
                      											<td width="35%"><%=DynaCacheHst.get("InMemoryAndDiskCacheEntryCount")%> </td>
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>�ɴ�������Ŀɸ��ٻ�������������Ŀ:</td>
                      											<td width="35%"><%=DynaCacheHst.get("HitsOnDiskCount")%> </td>
                    										</tr>   
                    											<tr bgcolor="#F1F1F1">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>���´��ڴ��ȥ��Ŀ����ʽʧЧ��:</td>
                      											<td width="35%"><%=DynaCacheHst.get("ExplicitMemoryInvalidationCount")%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>��ʱ���ڴ��ڴ�ʹ��̳�ȥ�ĸ��ٻ�����Ŀ��:</td>
                      											<td width="35%"><%=DynaCacheHst.get("TimeoutInvalidationCount")%> </td>
                    										</tr>
                    										<tr>
                    											<td width="15%" height="26" align="left" nowrap class=txtGlobal>�ɴ�Ӧ�ó�������������е�Ӧ�ó��������ɵĿɸ��ٻ�������������Ŀ:</td>
                      											<td width="35%"><%=DynaCacheHst.get("ClientRequestCount")%> </td>
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>���ٻ���δ������Ŀɸ��ٻ�������������Ŀ:</td>
                      											<td width="35%"><%=DynaCacheHst.get("MissCount")%> </td>
                    										</tr>   
															<tr bgcolor="#F1F1F1">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>�������ʹ�õģ�LRU���㷨���ڴ��г�ȥ�ĸ��ٻ�����Ŀ��:</td>
                      											<td width="35%"><%=DynaCacheHst.get("LruInvalidationCount")%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>���򻯵ػ�ͨ�����ٻ�����Ա������ɵ���ʽʧЧ��:</td>
                      											<td width="35%"><%=DynaCacheHst.get("LocalExplicitInvalidationCount")%> </td>
                    										</tr>
                    										<tr>
                    											<td width="15%" height="26" align="left" nowrap class=txtGlobal>��ʽ��Ч��:</td>
                      											<td width="35%"><%=DynaCacheHst.get("ExplicitInvalidationCount")%> </td>
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>�ڴ��ڸ��ٻ�����Ŀ�������:</td>
                      											<td width="35%"><%=DynaCacheHst.get("MaxInMemoryCacheEntryCount")%> </td>
                    										</tr>   
															<tr bgcolor="#F1F1F1">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>���´Ӵ��̳�ȥ��Ŀ����ʽʧЧ��:</td>
                      											<td width="35%"><%=DynaCacheHst.get("ExplicitDiskInvalidationCount")%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>�ɴ˸�����������JVM����Ŀɸ��ٻ�������������Ŀ:</td>
                      											<td width="35%"><%=DynaCacheHst.get("RemoteHitCount")%> </td>
                    										</tr>
                    										<tr>
                    											<td width="15%" height="26" align="left" nowrap class=txtGlobal>���ڴ�����Ŀɸ��ٻ�������������Ŀ:</td>
                      											<td width="35%"><%=DynaCacheHst.get("HitsInMemoryCount")%> </td>
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>��Эͬ�����Ķ�̬���ٻ�����յ��ĸ��ٻ�����Ŀ��:</td>
                      											<td width="35%"><%=DynaCacheHst.get("RemoteCreationCount")%> </td>
                    										</tr> 
															<tr bgcolor="#F1F1F1">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>�Ӵ˸�������Эͬ������JVM���յ�����ʽʧЧ��:</td>
                      											<td width="35%"><%=DynaCacheHst.get("RemoteExplicitInvalidationCount")%> </td>	
                    										</tr>
                                                        </table>
                                                <table width="100%" border="0" cellpadding="3" cellspacing="1" bgcolor="#FFFFFF">
                                                      <tr bgcolor="#D1DDF5">
                      											<td height="28" colspan="4" valign=center nowrap class=txtGlobal><div class="txtGlobalBigBold">TransactionService��Ϣ</div></td>
                    										</tr>

                    										<tr bgcolor="#F1F1F1">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>ȫ�������ύ��ƽ������ʱ��:</td>
                      											<td width="35%"><%=TransactionServiceHst.get("GlobalCommitTime")%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>�ڷ��������ѿ�ʼ�ı���������:</td>
                      											<td width="35%"><%=TransactionServiceHst.get("LocalBegunCount")%> </td>
                    										</tr>
															<tr>
                    											<td width="15%" height="26" align="left" nowrap class=txtGlobal>Ϊ�Ż���ת���ɵ��׶ε�ȫ����������:</td>
                      											<td width="35%"><%=TransactionServiceHst.get("OptimizationCount")%> </td>
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>ȫ�������ƽ������ʱ��:</td>
                      											<td width="35%"><%=TransactionServiceHst.get("GlobalTranTime")%> </td>
                    										</tr>   
                    											<tr bgcolor="#F1F1F1">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>׼��ȫ�������ƽ������ʱ��:</td>
                      											<td width="35%"><%=TransactionServiceHst.get("GlobalPrepareTime")%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>ȫ�������ƽ��before_completion����ʱ��:</td>
                      											<td width="35%"><%=TransactionServiceHst.get("GlobalBeforeCompletionTime")%> </td>
                    										</tr>
                    										<tr>
                    											<td width="15%" height="26" align="left" nowrap class=txtGlobal>���������ύ��ƽ������ʱ��:</td>
                      											<td width="35%"><%=TransactionServiceHst.get("LocalCommitTime")%> </td>
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>�ع��ı���������:</td>
                      											<td width="35%"><%=TransactionServiceHst.get("LocalRolledbackCount")%> </td>
                    										</tr>   
															<tr bgcolor="#F1F1F1">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>���ύ��ȫ������ĸ���:</td>
                      											<td width="35%"><%=TransactionServiceHst.get("CommittedCount")%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>�������ȫ��������:</td>
                      											<td width="35%"><%=TransactionServiceHst.get("ActiveCount")%> </td>
                    										</tr>
                    										<tr>
                    											<td width="15%" height="26" align="left" nowrap class=txtGlobal>�ع���ȫ��������:</td>
                      											<td width="35%"><%=TransactionServiceHst.get("RolledbackCount")%> </td>
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>���ύ�ı�������ĸ���:</td>
                      											<td width="35%"><%=TransactionServiceHst.get("LocalCommittedCount")%> </td>
                    										</tr>   
															<tr bgcolor="#F1F1F1">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>���������漰��ȫ��������:</td>
                      											<td width="35%"><%=TransactionServiceHst.get("GlobalInvolvedCount")%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>ȫ�������ƽ������ʱ��:</td>
                      											<td width="35%"><%=TransactionServiceHst.get("LocalTranTime")%> </td>
                    										</tr>
                    										<tr>
                    											<td width="15%" height="26" align="left" nowrap class=txtGlobal>��ʱ��ȫ��������:</td>
                      											<td width="35%"><%=TransactionServiceHst.get("GlobalTimeoutCount")%> </td>
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>��ʱ�ı���������:</td>
                      											<td width="35%"><%=TransactionServiceHst.get("LocalTimeoutCount")%> </td>
                    										</tr> 
															<tr bgcolor="#F1F1F1">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>�ڷ������Ͽ�ʼ��ȫ��������:</td>
                      											<td width="35%"><%=TransactionServiceHst.get("GlobalBegunCount")%> </td>
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>������ı���������:</td>
                      											<td width="35%"><%=TransactionServiceHst.get("LocalActiveCount")%> </td>
                    										</tr>
															<tr bgcolor="#F1F1F1">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>���������before_completionƽ������ʱ��:</td>
                      											<td width="35%"><%=TransactionServiceHst.get("LocalBeforeCompletionTime")%> </td>
                    										</tr>
												</table>
												 <table width="100%" border="0" cellpadding="3" cellspacing="1" bgcolor="#FFFFFF">
                                                      <tr bgcolor="#D1DDF5">
                      											<td height="28" colspan="4" valign=center nowrap class=txtGlobal><div class="txtGlobalBigBold">ORB��Ϣ</div></td>
                    										</tr>
                    										<tr bgcolor="#F1F1F1">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>�ɶ������������򲢷������������:</td>
                      											<td width="35%"><%=ORBHst.get("ConcurrentRequestCount")%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal>���͵������������������������:</td>
                      											<td width="35%"><%=ORBHst.get("RequestCount")%> </td>
                    										</tr>
                    										<tr>
                    											<td width="15%" height="26" align="left" nowrap class=txtGlobal>���Ҷ������û��ѵ�ʱ���������룩:</td>
                      											<td width="35%"><%=ORBHst.get("LookupTime")%> </td>
																
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
				        								<table id="detail-content-footer" class="detail-content-footer">
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