<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.report.jfree.ChartCreator"%>
<%@page import="com.afunms.common.util.SysUtil" %>
<%@page import="com.afunms.monitor.item.*"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.impl.HostLastCollectDataManager"%>
<%@page import="com.afunms.polling.api.I_HostLastCollectData"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.monitor.item.base.MoidConstants"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@ page import="com.afunms.polling.api.I_Portconfig"%>
<%@ page import="com.afunms.polling.om.Portconfig"%>
<%@ page import="com.afunms.polling.om.IpMac"%>
<%@ page import="com.afunms.polling.impl.PortconfigManager"%>
<%@page import="com.afunms.topology.dao.*"%>

<%@page import="java.util.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%@page import="com.afunms.topology.model.*"%>
<% 
 
String[] memoryItem={"AllSize","UsedSize","Utilization"};
String[] memoryItemch={"总容量","已用容量","当前利用率","最大利用率"};
String[] sysItem={"sysName","sysUpTime","sysContact","sysLocation","sysServices","sysDescr"};
String[] sysItemch={"设备名","设备启动时间","设备联系","设备位置","设备服务","设备描述"};

  String rootPath = request.getContextPath(); 
  List moidlist = (List)request.getAttribute("list");
  String id = (String)request.getAttribute("id");
  Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(id));
  String ipaddress = (String)request.getAttribute("ipaddress");
java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd HH:mm");					  	 
         
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="gb2312" />
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="gb2312"></script>
<!--nielin add for timeShareConfig at 2010-01-04 start-->
<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/timeShareConfigdiv.js" charset="gb2312"></script>
<!--nielin add for timeShareConfig at 2010-01-04 end-->

<script language="JavaScript" type="text/javascript">


  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
 Ext.get("process").on("click",function(){
  
  
       // mainForm.submit();
 });	
	
});
//-- nielin modify at 2010-01-04 start ----------------
function CreateWindow(url)
{
	
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}    

function setReceiver(eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/user.do?action=setReceiver&event='+event.id+'&value='+event.value);
}
//-- nielin modify at 2010-01-04 end ----------------


</script>

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
	timeShareConfiginit(); // nielin add for time-sharing at 2010-01-04
}

</script>

<script>
//-- nielin add for timeShareConfig 2010-01-03 end-------------------------------------------------------------
	/*
	* 此方法用于短信分时详细信息
	* 需引入 /application/resource/js/timeShareConfigdiv.js 
	*/
	//接受用户的列表
	var action = "<%=rootPath%>/user.do?action=setReceiver";
	// 获取短信分时详细信息的div
	function timeShareConfiginit(){
		var rowNum = document.getElementById("rowNum");
		rowNum.value = "0";
		// 获取设备或服务的分时数据列表,
		var timeShareConfigs = new Array();
		var smsConfigs = new Array();
		var phoneConfigs = new Array();
	    for(var i = 0; i< timeShareConfigs.length; i++){
	    	var item = timeShareConfigs[i];
	    	if(item.timeShareType=="sms"){
	    		smsConfigs.push({
	                timeShareConfigId:item.timeShareConfigId,
	                timeShareType:item.timeShareType,
	                beginTime:item.beginTime,
	                endTime:item.endTime,
	                userIds:item.userIds
	            });
	    	}
	    	if(item.timeShareType=="phone"){
	    		phoneConfigs.push({
	                timeShareConfigId:item.timeShareConfigId,
	                timeShareType:item.timeShareType,
	                beginTime:item.beginTime,
	                endTime:item.endTime,
	                userIds:item.userIds
	            });
	    	}
	    }
		timeShareConfig("smsConfigTable",smsConfigs);
		timeShareConfig("phoneConfigTable",phoneConfigs);
	}
//---- nielin add for timeShareConfig 2010-01-03 end-------------------------------------------------------------------------
</script>


</head>
<body id="body" class="body" onload="initmenu();">

	<!-- 右键菜单结束-->
   <form name="mainForm" method="post">
   <input type=hidden name="id" value="<%=id%>">
   <input type=hidden name="ipaddress" value="<%=ipaddress%>">
		<table id="body-container" class="body-container">
			<tr>
				<td class="td-container-main">
					<table id="container-main" class="container-main">
						<tr>
							<td class="td-container-main-add">
								<table id="container-main-add" class="container-main-add">
									<tr>
										<td>
											<table id="add-content" class="add-content">
												<tr>
													<td>
														<table id="add-content-header" class="add-content-header">
										                	<tr>
											                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
											                	<td class="add-content-title">设备维护 >> 设备监视项指标列表 >> <%=host.getAlias()%>(<%=ipaddress%>)</td>
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
				        										
				        												<table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%">
					<TBODY>
								<tr align="center"> 
    							<td class="report-data-body-title"  width="10%" align="left" bgcolor=#F1F1F1>指标名称</td>
    							<!--<td  width="10%" align="left" bgcolor=#397dbd><font color=#ffffff>监视类型</font></td>-->
    							<td class="report-data-body-title"  width="6%" align="center" bgcolor=#F1F1F1>监视</td>
    							<td class="report-data-body-title"  width="13%" align="left" bgcolor=#F1F1F1>一级阀值(普通)</td>
    							<td class="report-data-body-title"  width="5%" align="left" bgcolor=#F1F1F1>次数</td>
    							<td class="report-data-body-title"  width="5%" align="left" bgcolor=#F1F1F1>短信</td>
    							<td class="report-data-body-title"  width="13%" align="left" bgcolor=#F1F1F1>二级阀值(严重)</td>
    							<td class="report-data-body-title"  width="5%" align="left" bgcolor=#F1F1F1>次数</td>
    							<td class="report-data-body-title"  width="5%" align="left" bgcolor=#F1F1F1>短信</td>
    							<td class="report-data-body-title"  width="13%" align="left" bgcolor=#F1F1F1>三级阀值(紧急)</td>
    							<td class="report-data-body-title"  width="5%" align="left" bgcolor=#F1F1F1>次数</td>
    							<td class="report-data-body-title"  width="5%" align="left" bgcolor=#F1F1F1>短信</td>  
    							<td class="report-data-body-title"  width="5%" align="left" bgcolor=#F1F1F1>修改</td>  							    							
  						</tr>

             <%
             if (moidlist != null){
             for(int i=0 ;i<moidlist.size(); i++){
             	NodeMonitor nm = (NodeMonitor)moidlist.get(i);
             	//Date cc = ipmac.getCollecttime().getTime();
             	String url="";
             	if(nm.isEnabled()){
             		url="<a href="+rootPath+"/moid.do?action=stopmoid&id="+id+"&ipaddress="+ipaddress+"&moid="+nm.getId()+">停止监视</a>";
             	}else{
             		url="<a href="+rootPath+"/moid.do?action=startmoid&id="+id+"&ipaddress="+ipaddress+"&moid="+nm.getId()+">启动监视</a>";
             		//url="启动监视";
             	}  
             	HostNode vo = new HostNode();
		HostNodeDao dao = new HostNodeDao();
		vo = dao.loadHost(nm.getNodeID()); 
		String enable = "是";
		if(!nm.isEnabled())enable="否";
		if(vo == null)continue; 
		dao.close();
		String sms0="是";
		if(nm.getSms0()==0)sms0="否";
		String sms1="是";
		if(nm.getSms1()==0)sms1="否";
		String sms2="是";
		if(nm.getSms2()==0)sms2="否";  
             %>
            					<tr bgcolor="#ffffff" <%=onmouseoverstyle%>>
            						<td class="report-data-body-list" ><%=nm.getDescr()%></td>
            						<td class="report-data-body-list"><%=enable%></td>
            						<td class="report-data-body-list"><%=nm.getLimenvalue0()%>&nbsp;<%=nm.getUnit()%></td>
            						<td class="report-data-body-list"><%=nm.getTime0()%></td>
            						<td class="report-data-body-list"><%=sms0%></td>
            						<td class="report-data-body-list"><%=nm.getLimenvalue1()%>&nbsp;<%=nm.getUnit()%></td>
            						<td class="report-data-body-list"><%=nm.getTime1()%></td>
            						<td class="report-data-body-list"><%=sms1%></td>
            						<td class="report-data-body-list"><%=nm.getLimenvalue2()%>&nbsp;<%=nm.getUnit()%></td>
            						<td class="report-data-body-list"><%=nm.getTime1()%></td>
            						<td class="report-data-body-list"><%=sms2%></td>
            						<td class="report-data-body-list"><a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/moid.do?action=showeditmoids&id=<%=id%>&ipaddress=<%=host.getIpAddress()%>&moid=<%=nm.getId()%>&moid=<%=nm.getNodeID()%>","onetelnet", "height=400, width= 500, top=200, left= 200")'>
            						<img src="<%=rootPath%>/resource/image/editicon.gif" border=0></a></td>
            					</tr>
            <%}
            }
            %>            
                				<tr align=center>
                					<td colspan=11 align=center><br>
								<input type=reset class="formStylebutton" style="width:50" value="关 闭" onclick="javascript:window.close()">&nbsp;&nbsp; 
								              					
                  					</td>
                  				</tr>  
            					</tbody>
            					</table>
            					<br>
			</TD>																			
			</tr>			
															
															<tr>
																<!-- nielin add (for timeShareConfig) start 2010-01-03 -->
																<td><input type="hidden" id="rowNum" name="rowNum"></td>
																<!-- nielin add (for timeShareConfig) end 2010-01-03 -->
															
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
		
	</form>
</BODY>

<script>

function unionSelect(){
	var type = document.getElementById("type");
	var nameFont = document.getElementById("nameFont");
	var db_nameTD = document.getElementById("db_nameTD");
	var db_nameInput = document.getElementById("db_nameInput");
	var category = document.getElementById("category");
	var port  = document.getElementById("port");
	if(type.value == 2){
		nameFont.style.display="inline";
		db_nameTD.style.display="none";
		db_nameInput.style.display="none";
	}else{
		nameFont.style.display="none";
		db_nameTD.style.display="inline";
		db_nameInput.style.display="inline";
		
	}
	var categoryvalue = "";
	var portvalue = "";
	if(type.value == 1){
		categoryvalue = 53;
		portvalue = 1521;
	}else if(type.value == 2){
		categoryvalue = 54;
		portvalue = 1433;
	}else if(type.value == 4){
		categoryvalue = 52;
		portvalue = 3306;
	}else if(type.value == 5){
		categoryvalue = 59;
		portvalue = 50000;
	}else if(type.value == 6){
		categoryvalue = 55;
		portvalue = 2638;
	}else if(type.value == 7){
		categoryvalue = 60;
		portvalue = 9088;
	}
	port.value = portvalue;
	category.value = categoryvalue;
}

unionSelect();

</script>

</HTML>