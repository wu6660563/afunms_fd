<%@page language="java" contentType="text/html;charset=gb2312" %>



<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.ArrayList"%>


<%@page import="java.util.Hashtable"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.afunms.config.model.Portconfig"%>

<%@page import="com.afunms.capreport.model.UtilReport"%>

<%@page import="com.afunms.capreport.dao.UtilReportDao"%>
<%@page import="com.afunms.capreport.model.SubscribeResources"%>
<%@page import="com.afunms.application.model.DBVo"%>
<%@page import="com.afunms.application.dao.DBDao"%>
<%@page import="com.afunms.application.dao.TomcatDao"%>
<%@page import="com.afunms.application.dao.IISConfigDao"%>
<%@page import="com.afunms.application.model.Tomcat"%>
<%@page import="com.afunms.application.model.IISConfig"%>
<%
  String rootPath = request.getContextPath();
  
  List list = (List)request.getAttribute("list");
  Vector vec=(Vector)request.getAttribute("vector");
  String sendDate=(String)request.getAttribute("sendDate");
 UtilReport report=(UtilReport)vec.get(0);
 SubscribeResources sub=(SubscribeResources)vec.get(1);
  Hashtable cpuhash = (Hashtable)request.getAttribute("cpuhash");
  String[] items={"连通率","CPU","内存","端口"};
   String[] itemId={"ping","cpu","mem","port"};
  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String	startdate = sdf.format(new Date());
			String	todate = sdf.format(new Date());
	String type="";
	if(report.getType().equals("net")){
	type="网络设备";
	}else if(report.getType().equals("host")){
	type="服务器";
	}else if(report.getType().equals("db")){
	type="数据库";
	}else if(report.getType().equals("midware")){
	type="中间件";
	}
	
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<style type="text/css">



</style>
<script type="text/javascript" src="<%=rootPath%>/js/tree/Tree.js"></script>
<script type="text/javascript" src="<%=rootPath%>/js/tree/common.js"></script>

<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 
<script type="text/javascript"
			src="<%=rootPath%>/application/environment/resource/ext3.1/adapter/ext/ext-base.js"></script>
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8"/>
<script type="text/javascript"
			src="<%=rootPath%>/application/environment/resource/ext3.1/ext-all.js"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/application/environment/resource/ext3.1/resources/css/ext-all.css" />

		
		
		

<script>


////////////////初始化树/////////////////////////
var ddtree = null;

function init()
{
	parseDataForTree();
}

 
function parseDataForTree()
{
	ddtree = new Tree("sorttree","100%","100%",0);
	ddtree.setImagePath("<%=rootPath%>/resource/image/tree/");
	ddtree.setDelimiter(",");
	ddtree.enableCheckBoxes(1);
	
	
	ddtree.insertNewItem("","root","<%=type%>", 0, "", "","", "checked");
	ddtree.setCheck("root",0);
	<% 
	String item="";
	Vector<String> vector=new Vector<String>();
	Vector<String> portvector=new Vector<String>();
	if(list!=null&&list.size()>0)
	{
	
	for(int i=0;i<list.size();i++){
	item=(String)list.get(i);
	if(report.getType().equals("net")||report.getType().equals("host"))
	{
	if(item.indexOf("ping")>-1){
	String ip=item.replace("ping","");
	if(!vector.contains(ip)){
	%>
	ddtree.insertNewItem("root","<%=ip%>","<%=ip%>", 0, "", "","", "");
	ddtree.insertNewItem("<%=ip%>","ping"+"<%=ip%>","连通率", 0, "", "","", "");
	<%
	vector.add(ip);
	}else{
	
	%>
	ddtree.insertNewItem("<%=ip%>","ping"+"<%=ip%>","连通率", 0, "", "","", "");
	<%
	}
	}else if(item.indexOf("cpu")>-1){
	String ip=item.replace("cpu","");
	if(!vector.contains(ip)){
	%>
	ddtree.insertNewItem("root","<%=ip%>","<%=ip%>", 0, "", "","", "");
	ddtree.insertNewItem("<%=ip%>","cpu"+"<%=ip%>","CPU", 0, "", "","", "");
	<%
	vector.add(ip);
	}else{
	
	%>
	ddtree.insertNewItem("<%=ip%>","cpu"+"<%=ip%>","CPU", 0, "", "","", "");
	<%
	}
	
	}else if(item.indexOf("mem")>-1){
	String ip=item.replace("mem","");
	
	if(!vector.contains(ip)){
	
	%>
	ddtree.insertNewItem("root","<%=ip%>","<%=ip%>", 0, "", "","", "");
	ddtree.insertNewItem("<%=ip%>","mem"+"<%=ip%>","内存", 0, "", "","", "");
	<%
	vector.add(ip);
	}else{
	
	%>
	ddtree.insertNewItem("<%=ip%>","mem"+"<%=ip%>","内存", 0, "", "","", "");
	<%
	}
	
	}else if(item.indexOf("disk")>-1){
	String ip=item.replace("disk","");
	
	if(!vector.contains(ip)){
	
	%>
	ddtree.insertNewItem("root","<%=ip%>","<%=ip%>", 0, "", "","", "");
	ddtree.insertNewItem("<%=ip%>","disk"+"<%=ip%>","磁盘", 0, "", "","", "");
	<%
	vector.add(ip);
	}else{
	
	%>
	ddtree.insertNewItem("<%=ip%>","disk"+"<%=ip%>","磁盘", 0, "", "","", "");
	<%
	}
	
	}else if(item.indexOf("port")>-1){
	String[] idRelValue=new String[item.split("\\*").length];
					idRelValue=item.split("\\*");
					if (idRelValue.length<4) {
						continue;
					}
	String ip=idRelValue[1];
	String portname=idRelValue[3];
	if(!vector.contains(ip)){
	
	%>
	ddtree.insertNewItem("root","<%=ip%>","<%=ip%>", 0, "", "","", "");
	
	ddtree.insertNewItem("<%=ip%>","port"+"<%=ip%>","端口", 0, "", "","", "");
	ddtree.insertNewItem("port"+"<%=ip%>","port"+"<%=portname%>","<%=portname%>", 0, "", "","", "");
	<%
	portvector.add("port"+portname);
	vector.add(ip);
	}else {
	if(!portvector.contains("port"+portname)){
	%>
	ddtree.insertNewItem("<%=ip%>","port"+"<%=ip%>","端口", 0, "", "","", "");
	ddtree.insertNewItem("port"+"<%=ip%>","port"+"<%=portname%>","<%=portname%>", 0, "", "","", "");
	<%
	portvector.add("port"+portname);
	}else{
	%>
	
	ddtree.insertNewItem("port"+"<%=ip%>","port"+"<%=portname%>","<%=portname%>", 0, "", "","", "");
	<%
	}
	
	}
	
	}
	}else if(report.getType().equals("db")){
	String[] idRelValue = new String[item.split("\\*").length];
				idRelValue = item.split("\\*");
				if(idRelValue.length<4)continue;
				String itemid = idRelValue[0].trim();
				String typeId = idRelValue[1].trim();
				String id = idRelValue[2].trim();
				String ip = idRelValue[3].trim();
				DBDao dao = new DBDao();
				DBVo vo = new DBVo();
				List voList=(List)dao.getDbByTypeAndIpaddress(Integer.parseInt(typeId),ip);
				if(voList!=null&&voList.size()>0)
				 vo=(DBVo)voList.get(0);
	 if(item.indexOf("ping")>-1){
	 String name="连通率";
	if(!vector.contains(ip)){
	
	%>
	ddtree.insertNewItem("root","<%=ip%>","<%=ip%>", 0, "", "","", "");
	
	ddtree.insertNewItem("<%=ip%>","<%=vo.getId()%>","<%=vo.getAlias()%>", 0, "", "","", "");
	ddtree.insertNewItem("<%=vo.getId()%>","<%=vo.getId()%>"+"*"+"<%=ip%>","<%=name%>", 0, "", "","", "");
	<%
	portvector.add(vo.getId()+"*"+ip);
	vector.add(ip);
	}else {
	if(!portvector.contains(vo.getId()+"*"+ip)){
	%>
	ddtree.insertNewItem("<%=ip%>","<%=vo.getId()%>","<%=vo.getAlias()%>", 0, "", "","", "");
	ddtree.insertNewItem("<%=vo.getId()%>","<%=vo.getId()%>"+"*"+"<%=ip%>","<%=name%>", 0, "", "","", "");
	<%
	portvector.add(vo.getId()+"*"+ip);
	}else{
	%>
	
	ddtree.insertNewItem("<%=vo.getId()%>","<%=vo.getId()%>"+"*"+"<%=ip%>","<%=name%>", 0, "", "","", "");
	<%
	}
	
	}
	 }else if(item.indexOf("val")>-1){
	 String name="性能";
	if(!vector.contains(ip)){
	
	%>
	ddtree.insertNewItem("root","<%=ip%>","<%=ip%>", 0, "", "","", "");
	
	ddtree.insertNewItem("<%=ip%>","<%=vo.getId()%>","<%=vo.getAlias()%>", 0, "", "","", "");
	ddtree.insertNewItem("<%=vo.getId()%>","<%=vo.getId()%>"+"*"+"<%=ip%>","<%=name%>", 0, "", "","", "");
	<%
	portvector.add(vo.getId()+"*"+ip);
	vector.add(ip);
	}else {
	if(!portvector.contains(vo.getId()+"*"+ip)){
	%>
	ddtree.insertNewItem("<%=ip%>","<%=vo.getId()%>","<%=vo.getAlias()%>", 0, "", "","", "");
	ddtree.insertNewItem("<%=vo.getId()%>","<%=vo.getId()%>"+"*"+"<%=ip%>","<%=name%>", 0, "", "","", "");
	<%
	portvector.add(vo.getId()+"*"+ip);
	}else{
	%>
	
	ddtree.insertNewItem("<%=vo.getId()%>","<%=vo.getId()%>"+"*"+"<%=ip%>","<%=name%>", 0, "", "","", "");
	<%
	}
	
	}
	 }else if(item.indexOf("tablespace")>-1){
	 String name="表空间";
	if(!vector.contains(ip)){
	
	%>
	ddtree.insertNewItem("root","<%=ip%>","<%=ip%>", 0, "", "","", "");
	
	ddtree.insertNewItem("<%=ip%>","<%=vo.getId()%>","<%=vo.getAlias()%>", 0, "", "","", "");
	ddtree.insertNewItem("<%=vo.getId()%>","<%=vo.getId()%>"+"*"+"<%=ip%>","<%=name%>", 0, "", "","", "");
	<%
	portvector.add(vo.getId()+"*"+ip);
	vector.add(ip);
	}else {
	if(!portvector.contains(vo.getId()+"*"+ip)){
	%>
	ddtree.insertNewItem("<%=ip%>","<%=vo.getId()%>","<%=vo.getAlias()%>", 0, "", "","", "");
	ddtree.insertNewItem("<%=vo.getId()%>","<%=vo.getId()%>"+"*"+"<%=ip%>","<%=name%>", 0, "", "","", "");
	<%
	portvector.add(vo.getId()+"*"+ip);
	}else{
	%>
	
	ddtree.insertNewItem("<%=vo.getId()%>","<%=vo.getId()%>"+"*"+"<%=ip%>","<%=name%>", 0, "", "","", "");
	<%
	}
	
	}
	 }
	}else if(report.getType().equals("midware")){
	////////////////////////////////
	
	String[] idRelValue = new String[item.split("\\*").length];
				idRelValue = item.split("\\*");
				if(idRelValue.length<4)continue;
				String itemid = idRelValue[0].trim();
				String typeId = idRelValue[1].trim();
				String id = idRelValue[2].trim();
				String ip = idRelValue[3].trim();
				if(typeId.equals("ping")||typeId.equals("jvm")){
				TomcatDao tomcatDao=null;
				Tomcat vo=null;
				IISConfigDao iisconfigdao = null;
				IISConfig iis=null;
				String name="";
				String alias="";
			if(itemid.equals("tomcat"))
	     {
	       tomcatDao=new TomcatDao();
           vo=(Tomcat)tomcatDao.findByID(id);
           if(typeId.equals("jvm")){
           name="虚拟利用率";
           }else{
	       name="连通率";
	       }
	       alias=vo.getAlias();
	     }else if(itemid.equals("iis")){
	     iisconfigdao = new IISConfigDao();
	     iis=(IISConfig)iisconfigdao.findByID(id);
	     name="连通率";
	     alias=iis.getName();
	    
	     }
	if(!vector.contains(ip)){
	
	%>
	ddtree.insertNewItem("root","<%=ip%>","<%=ip%>", 0, "", "","", "");
	
	ddtree.insertNewItem("<%=ip%>","<%=id%>"+"<%=itemid%>","<%=alias%>", 0, "", "","", "");
	ddtree.insertNewItem("<%=id%>"+"<%=itemid%>","<%=itemid%>"+"<%=id%>"+"*"+"<%=ip%>","<%=name%>", 0, "", "","", "");
	<%
	portvector.add(id+"*"+itemid);
	vector.add(ip);
	}else {
	if(!portvector.contains(id+"*"+itemid)){
	%>
	ddtree.insertNewItem("<%=ip%>","<%=id%>"+"<%=itemid%>","<%=alias%>", 0, "", "","", "");
	ddtree.insertNewItem("<%=id%>"+"<%=itemid%>","<%=itemid%>"+"<%=id%>"+"*"+"<%=ip%>","<%=name%>", 0, "", "","", "");
	<%
	portvector.add(vo.getId()+"*"+itemid);
	}else{
	%>
	
	ddtree.insertNewItem("<%=id%>"+"<%=itemid%>","<%=itemid%>"+"<%=id%>"+"*"+"<%=ip%>","<%=name%>", 0, "", "","", "");
	<%
	}
	
	}
	 
	          }
	    
	
	////////////////////////////////
	}
	}
	}
	%>
		

}

</script>

<script language="JavaScript" type="text/JavaScript">
  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
	
});

 
</script>


<script language="JavaScript" type="text/javascript">


function CreateDeviceWindow(url)
{
	
msgWindow=window.open(url,"_blank","toolbar=no,width=900,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}


function CreateWindow(url)
{
	
 msgWindow=window.open(url,"_blank","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}


</script>
</head>
<body id="body" class="body" onLoad="init();">
<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 189px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
	
	<form id="mainForm" method="post" name="mainForm">
		<div id="loading">
			<div class="loading-indicator">
				<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif" width="32" height="32" style="margin-right: 8px;" align="middle" />Loading...</div>
			</div>
		<div id="loading-mask" style=""></div>
		<table id="body-container" class="body-container">
			<tr>
				
				<td valign=top style="margin: 10px auto;position :relative;">
				<table >
                <tr>
                <td colspan="2">
     <div style=" height:36px; background:url('<%=rootPath%>/resource/image/tree/tit_bg.gif'); padding-left:30px; line-height:36px; font-size:20px; font-weight:bold; color:#fff;">报表模板详细信息</div></td>
</tr>
                    
                    
							<tr>
								<td  height="100%" align="left" valign="top">
				<div id="sorttree" style="margin-top: 0px; background-color: #FFFFFF;OVERFLOW-y:auto;OVERFLOW-x:auto;height:400;width:250;"></div>
							</td>
							<td width="80%" height="100%" valign="top">
							
                            <table border="0" cellpadding="0" cellspacing="0" class="win-content" id="win-content">
																
																<tr >
                                                                 
                                                                      <td width=100% >
                                                                      
                                                                         <table cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" border=1>
																			   <tr>
																				   <td width="100%" align=left>
																				       <div id="editmodel" >
																				      <table border="0" id="table1" cellpadding="0" width="100%">
																		<tr style="background-color: #FFFFFF;">
																				<TD nowrap align="right" height="24" width="10%">
																					报表名称&nbsp;
																				</TD>
																				<TD nowrap width="40%">
																					&nbsp;
																					<input type="text" name="report_name" id="report_name" size="50" class="formStyle" size="50" value="<%=report.getName() %>">
																					
																					
																				</TD>
																			</tr>
																			<tr style="background-color: #ECECEC;">
																				<TD nowrap align="right" height="24" width="10%">
																					报表接收人&nbsp;
																				</TD>
																				<TD nowrap width="40%">
																					&nbsp;
																					<input type="text" name="recievers_name" id="recievers_name" size="50" class="formStyle" size="50" value="<%=sub.getUsername() %>" readonly>
																					
																					<input type="hidden" id="recievers_id" name="recievers_id" value="">
																					
																					<font color='red'>*</font>
																				</TD>
																			</tr>
																			<tr>
																				<TD nowrap align="right" height="24" width="10%">
																					邮件标题:&nbsp;
																				</TD>
																				<TD nowrap width="40%">
																					&nbsp;
																					<input type="text" name="mailTile" id="mailTile"
																						size="50" class="formStyle" value="<%=sub.getEmailtitle() %>">
																				</TD>
																			</tr>
																			<tr style="background-color: #ECECEC;">
																				<TD nowrap align="right" height="24" width="10%">
																					邮件描述:&nbsp;
																				</TD>
																				<TD nowrap width="40%">
																					&nbsp;
																					<input type="text" name="devices_name"
																						id="devices_name" size="50" class="formStyle" value="<%=sub.getEmailcontent() %>">
																				</TD>
																			</tr>
																			<tr>
																				<TD nowrap align="right" height="24" width="10%">
																					所属业务&nbsp;
																				</TD>
																				<TD nowrap width="40%">
																					&nbsp;
																					<input type="text" name="bidtext" id="bidtext" size="50" class="formStyle" value="<%=sub.getBidtext() %>">
																					
																					<input type="hidden" id="bid" name="bid" value="">
																					<font color='red'>*</font>
																				</TD>
																			</tr>
																			<tr style="background-color: #ECECEC;">
																				<TD nowrap align="right" height="24" width="10%">
																					报表类型:&nbsp;
																				</TD>
																				<TD nowrap width="40%">
																					&nbsp;
																					<SELECT id="reporttype" name="reporttype">
																					<%if(sub.getReport_type().equals("day")){ %>
																						<OPTION value="day" selected>
																							日报
																						</OPTION>
																						<%}else if(sub.getReport_type().equals("week")){ %>
																						<OPTION value="week">
																							周报
																						</OPTION>
																						<%}else if(sub.getReport_type().equals("month")){ %>
																						<OPTION value="month">
																							月报
																						</OPTION>
																						<%}else if(sub.getReport_type().equals("season")){ %>
																						<OPTION value="season">
																							季报
																						</OPTION>
																						<%}else if(sub.getReport_type().equals("year")){ %>
																						<OPTION value="year">
																							年报
																						</OPTION>
																						<%}%>
																					</SELECT>
																				</TD>
																			</tr>
																			<tr>
																				<TD nowrap align="right" height="24">报表发送时间:&nbsp;</TD>
																				<td nowrap  colspan="3">
																			         <%=sendDate %>
																				</td>
																			</tr>
																			<tr>
																				<TD nowrap colspan="4" align=center>
																					<br>
																					<input type="button" value="关闭" style="width: 50" class="formStylebutton" id="close" onclick='window.close()'>
																					</TD>
																			</tr>
																		</TABLE>
							                                                           </div>
						                                                            </td>
																			    </tr>
																		</table>
																		
                                                                      </td>
                                                                      
                                                                  </tr>
																<tr>
                                                                 
                                                                      <td width=94%>
                                                                         <table cellpadding="0" cellspacing="0" >
																			   <tr>
																				   <td width="100%" align=center>
																				       <div id="netpingReport">
							                                                           </div>
						                                                            </td>
																			    </tr>
																		</table>
                                                                      </td>
                                                                      
                                                                  </tr>
                                                                  <tr>
                                                                  
                                                                      <td  align=right>
                                                                          <div id="pingDiv">
                                                                          </div>
                                                                      </td>
                                                                      
                                                                  </tr>
                                                                  <tr>
                                                                  
                                                                      <td width=94%>
                                                                         <table cellpadding="0" cellspacing="0" >
																			   <tr>
																				   <td width="100%" align=center>
																				       <div id="netcpuReport">
							                                                           </div>
						                                                            </td>
																			    </tr>
																		</table>
                                                                      </td>
                                                                      
                                                                  </tr>
                                                                  <tr>
                                                                  
                                                                      <td width=94%>
                                                                          <div id="cpuDiv">
                                                                          </div>
                                                                      </td>
                                                                     
                                                                  </tr>
                                                                  <tr>
                                                                  
                                                                      <td width=94%>
                                                                         <table cellpadding="0" cellspacing="0" >
																			   <tr>
																				   <td width="100%" align=center>
																				       <div id="netmemReport">
							                                                           </div>
						                                                            </td>
																			    </tr>
																		</table>
                                                                      </td>
                                                                      
                                                                  </tr>
                                                                  <tr>
                                                                  
                                                                      <td width=94%>
                                                                          <div id="memDiv">
                                                                          </div>
                                                                      </td>
                                                                     
                                                                  </tr>
                                                                   <tr>
                                                                  
                                                                      <td width=94%>
                                                                         <table cellpadding="0" cellspacing="0" >
																			   <tr>
																				   <td width="100%" align=center>
																				       <div id="netportReport">
							                                                           </div>
						                                                            </td>
																			    </tr>
																		</table>
                                                                      </td>
                                                                      
                                                                  </tr>
                                                                  <tr>
                                                                  
                                                                      <td width=94%>
                                                                          <div id="portDiv">
                                                                          </div>
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