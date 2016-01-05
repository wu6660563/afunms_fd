<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.system.model.TimeShareConfig"%>
<%@page import="com.afunms.common.util.ShareData"%>
<%@page import="com.afunms.common.util.CommonUtil"%>
<%@page import="com.afunms.detail.service.dnsInfo.DnsInfoService"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="java.util.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.polling.*"%>
<%
	String runmodel = PollingEngine.getCollectwebflag(); 
  	String rootPath = request.getContextPath();
  
 	String timeFormat = "yyyy-MM-dd";
	String fdate = (String)request.getAttribute("from_date1");
	String tdate = (String)request.getAttribute("to_date1");
	String fhour = (String)request.getAttribute("from_hour");
	String thour = (String)request.getAttribute("to_hour");

	java.text.SimpleDateFormat timeFormatter = new java.text.SimpleDateFormat(timeFormat);
	String from_date1 = "";
	if (fdate == null ){
	 	from_date1=timeFormatter.format(new java.util.Date());
	}else{
		from_date1 = fdate;
	}
	String to_date1 = "";
	if (tdate == null ){
	 	to_date1=timeFormatter.format(new java.util.Date());
	}else{
		to_date1 = tdate;
	}

	String from_hour = "";
	if (fhour == null ){
	 	from_hour=new java.util.Date().getHours()+"";
	}else{
		from_hour = fhour;
	}
	String to_hour = "";
	if (thour == null ){
	 	to_hour=new java.util.Date().getHours()+"";
	}else{
		to_hour = thour;
	}
	String  dnsip = (String)request.getAttribute("dnsip");
	int  id = (Integer)request.getAttribute("id");
	String  fault = "";
	String hostip = "";
	String dns = "";
	String aaa = "";
	String primary = "";
	String responsible = "";
	String serial = "";
	String refresh = "";
	String retry = "";
	String expire = "";
	String dfault = "";
	String time = "";
	List mx = null;
	List ns = null;
	List cache = null;
	Hashtable dnsDataHash = null;
	
    if("0".equals(runmodel)){
   		//�ɼ�������Ǽ���ģʽ
 		dnsDataHash = (Hashtable)ShareData.getAllDnsData().get(id);
 	}else{
 		//�ɼ�������Ƿ���ģʽ
 		DnsInfoService dnsInfoService = new DnsInfoService();
 		dnsDataHash = dnsInfoService.getDnsDataHashtable(id+"");
 	}
 	fault = CommonUtil.getValue(dnsDataHash, "default", "--");
 	hostip =  CommonUtil.getValue(dnsDataHash, "hostip", "--");
 	dns =  CommonUtil.getValue(dnsDataHash, "dns", "--");
 	aaa =  CommonUtil.getValue(dnsDataHash, "aaa", "--");
 	primary =  CommonUtil.getValue(dnsDataHash, "primary", "--");
 	responsible =  CommonUtil.getValue(dnsDataHash, "responsible", "--");
 	serial =  CommonUtil.getValue(dnsDataHash, "serial", "--");
 	refresh =  CommonUtil.getValue(dnsDataHash, "refresh", "--");
 	retry =  CommonUtil.getValue(dnsDataHash, "retry", "--");
 	expire =  CommonUtil.getValue(dnsDataHash, "expire", "--");
 	dfault =  CommonUtil.getValue(dnsDataHash, "dfault", "--");
 	time =  CommonUtil.getValue(dnsDataHash, "time", "--");
	if(dnsDataHash.containsKey("mx")){
		mx = (ArrayList)dnsDataHash.get("mx");
	} 	
 	if(dnsDataHash.containsKey("ns")){
 		ns = (ArrayList)dnsDataHash.get("ns");
 	}
 	if(dnsDataHash.containsKey("cache")){
 		cache = (ArrayList)dnsDataHash.get("cache");
 	}
 	
	String chart1 = (String)request.getAttribute("chart1");
	String avgpingcon=(String)request.getAttribute("avgpingcon");
	String connrate = request.getAttribute("connrate").toString();
 	String conn_name = request.getAttribute("conn_name").toString();
 	String valid_name = request.getAttribute("valid_name").toString();
 	String fresh_name = request.getAttribute("fresh_name").toString();
 	String wave_name = request.getAttribute("wave_name").toString();
 	String delay_name = request.getAttribute("delay_name").toString();
 	String validrate = request.getAttribute("validrate").toString();
 	String freshrate = request.getAttribute("freshrate").toString();
 	String connsrc = rootPath+"/resource/image/jfreechart/"+conn_name+".png";
 	String validsrc =  rootPath+"/resource/image/jfreechart/"+valid_name+".png";
 	String freshsrc =  rootPath+"/resource/image/jfreechart/"+fresh_name+".png";
 	String wavesrc =  rootPath+"/resource/image/jfreechart/"+wave_name+".png";
 	String delaysrc =  rootPath+"/resource/image/jfreechart/"+delay_name+".png";
%>
<%  String menuTable = (String)request.getAttribute("menuTable");%>
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
  
     var chk1 = checkinput("alias","string","����",30,false);
     var chk2 = checkinput("ipaddress","ip","IP��ַ",30,false);
     var chk3 = checkinput("username","string","�û���",30,false);
     var chk4 = checkinput("password","string","����",30,false);
     var chk5 = checkinput("port","string","�˿ں�",30,false);
     
     if(chk1&&chk2&&chk3&&chk4&&chk5)
     {
     
        Ext.MessageBox.wait('���ݼ����У����Ժ�.. '); 
        //msg.style.display="block";
        mainForm.action = "<%=rootPath%>/jboss.do?action=add";
        mainForm.submit();
     }  
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
	timeShareConfiginit(); // nielin add for time-sharing at 2010-01-04
}

</script>

<script>
//-- nielin add for timeShareConfig 2010-01-03 end-------------------------------------------------------------
	/*
	* �˷������ڶ��ŷ�ʱ��ϸ��Ϣ
	* ������ /application/resource/js/timeShareConfigdiv.js 
	*/
	//�����û����б�
	var action = "<%=rootPath%>/user.do?action=setReceiver";
	// ��ȡ���ŷ�ʱ��ϸ��Ϣ��div
	function timeShareConfiginit(){
		var rowNum = document.getElementById("rowNum");
		rowNum.value = "0";
		// ��ȡ�豸�����ķ�ʱ�����б�,
		var timeShareConfigs = new Array();
		var smsConfigs = new Array();
		var phoneConfigs = new Array();
		<%	
			List timeShareConfigList = (List) request.getAttribute("timeShareConfigList");
			if(timeShareConfigList!=null&&timeShareConfigList.size()>=0){
			for(int i = 0 ; i < timeShareConfigList.size(); i++){	        
	            TimeShareConfig timeShareConfig = (TimeShareConfig) timeShareConfigList.get(i);
	            int timeShareConfigId = timeShareConfig.getId();
	            String timeShareType = timeShareConfig.getTimeShareType();
	            String timeShareConfigbeginTime = timeShareConfig.getBeginTime();
	            String timeShareConfigendTime = timeShareConfig.getEndTime();
	            String timeShareConfiguserIds = timeShareConfig.getUserIds();
	            
	    %>
	            timeShareConfigs.push({
	                timeShareConfigId:"<%=timeShareConfigId%>",
	                timeShareType:"<%=timeShareType%>",
	                beginTime:"<%=timeShareConfigbeginTime%>",
	                endTime:"<%=timeShareConfigendTime%>",
	                userIds:"<%=timeShareConfiguserIds%>"
	            });
	    <%
	        }
	        }
	    %>   
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

<!-- ��������������Ҫ��ʾ���Ҽ��˵� -->
	<div id="itemMenu" style="display: none";>
	<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
		style="border: thin" cellspacing="0">
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.edit()">�޸���Ϣ</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.detail();">������Ϣ</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.cancelmanage()">ȡ������</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.addmanage()">��Ӽ���</td>
		</tr>		
	</table>
	</div>
	<!-- �Ҽ��˵�����-->

	<form id="mainForm" method="post" name="mainForm">
		
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
											                	<td class="add-content-title">Ӧ�� >> ������� >> DNS�������</td>
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
				        										
	                                <table border="0" id="table1" cellpadding="0" cellspacing="1"
																	width="100%">
																<TBODY>
											<%if(fault!=null)
											   { %>
											  <tr bgcolor="#F1F1F1">
											  
											  <td colspan=2  height="26">
												
													��������:
													<%=fault %>
													
												</td>
												
												</tr>
												<%} %>
														<tr>
                								<td width="80%"  align="left" valign="top" class=dashLeft>
                									<table>
                    										
                    								<tr class="firsttr" > 
                    <td colspan="2"   height="26" class="tableheadingbborder">������Ϣ</td>
                  </tr>
                  <tr bgcolor="#F1F1F1"> 
                    <td  height="26" width="45%">����</td>
                    <td  height="26" width="55%">DNS�������  
                    </td>
                  </tr>
                  <tr> 
                    <td  height="26" width="45%">DNS������</td>
                    <%if(fault!=null)
                    {
                     %>
                    <td width="55%"><%=fault%></td>
                    <%} %>
                  </tr>
                   <tr bgcolor="#F1F1F1"> 
                    <td   height="26" width="45%">DNS������IP</td>
                    <td width="55%">
                     <%if(hostip!=null)
                    {
                     %>
                    <%=hostip%>
                    <%} %>
                    </td>
                  </tr>
                   <tr> 
                    <td  height="26" width="45%">״̬</td>
                    <td  align='left'height=25>
                    <%if(hostip!=null){
                     %>
			        <img src="/afunms/resource/image/topo/status_ok.gif" alt="" border="0"/>
		              <%} else{ %>
		            <img src="/afunms/resource/image/topo/status_busy.gif" alt="" border="0"/>
		            <% }%>  
		              </td>
                  </tr>
                  <tr bgcolor="#F1F1F1"> 
                    <td  height="26" width="45%">��ַ</td>
                     <td width="55%">
                     <%if(dns!=null)
                    {
                     %>
                   <%=dns%>
                        <%} %>
                        </td>
                  </tr>
                   <tr> 
                    <td  height="26" width="45%">��ַIP</td>
                    <%if(dnsip!=null)
                    {
                     %>
                    <td width="55%"><%=dnsip%></td>
                     <%} %>
                  </tr >
                   <tr bgcolor="#F1F1F1"> 
                    <td  height="26" width="45%">��ѯƵ��</td>
                    <td width="55%">5min</td>
                  </tr>
                  <tr>
                  <td  height="26" width="45%">�ϴ���ѯʱ��</td>
                    <td width="55%"><%=request.getAttribute("lasttime")%></td>
                  </tr>
                  <tr bgcolor="#F1F1F1">
                    <td  height="26" width="45%">�´���ѯʱ��</td>
                    <td width="55%"><%=request.getAttribute("nexttime")%></td>
                  </tr>							
                									</table>
                				             
										</td>	
										
										<td width="20%" align="center" valign="middle" class=dashLeft>

											<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none align=center border=1 algin="center">
                        								<tbody>
                          									<tr>
                											<td   align="center" valign="middle" class=dashLeft>
                														
			   <table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none  align=center border=1 >
                    <tr class="topNameRight">
                      <td width="3%" height="7" bgcolor="#FFFFFF">&nbsp;</td>
                      <td width="94%" background="OpManager.files/1spacer.gif" bgcolor="#FFFFFF"><div align="center"></div></td>
                      <td width="3%" bgcolor="#FFFFFF">&nbsp;</td>
                    </tr>
                    <tr class="topNameRight" >
                      <td height="70" rowspan="2">&nbsp;</td>
                      <td height="11" align="center" bordercolor="#9FB0C4" bgcolor="#D1DDF5" class="txtGlobalBold">DNS������ͨ��(��<%=from_date1%> <%=from_hour%>�� �� <%=to_date1%> <%=to_hour%>��)</td>
                      <td height="70" rowspan="2">&nbsp;</td>
                      
                    </tr>
                    <tr class="topNameRight">                   
                      <td height="70" align="center"><img src="<%=connsrc%>">
                      <br>DNS������ͨ��:<%=connrate %>
                      </td>    
                    </tr>
                </table>
				              																							  
                											</td>
              											</tr>
            										</tbody>
            										</table>
            									</td>           									
            								</tr>	
													
													
									                                  					  
                  					
              <tr>
                    <td width="80%" align="left"  class=dashLeft>		
                  			<table style="BORDER-COLLAPSE: collapse" bordercolor=#cedefa  cellpadding=0 rules=none width=100% align=center border=1 algin="center">
                    				<tr class="firsttr" > 
              <td colspan="2"  height="26" class="tableheadingbborder" >DNS��������ϸ��Ϣ</td>
            </tr>

  																	<tr  class="firsttr" bgcolor="#D1DDF5">
    																	<td  height="26" width="80%"><strong>A ��ַ��¼</strong></td>
    																
    																</tr>
    																<tr   >
    																<%if(aaa!=null)
    																{ %>
    																	<td ><%=aaa %></td>
    																<%} %>
    																</tr>
    																
    																<tr  class="firsttr" bgcolor="#D1DDF5">
    																	<td  height="26" ><strong>DNS������������Ϣ</strong></td>
    																
    																</tr>
    																<%if(primary!=null)
    																{%>
    																<tr  >
    																	<td ><%=primary %><br><%=responsible %><br><%=serial %><br><%=refresh %><br><%=retry %><br><%=expire %><br><%=dfault %><br><%=time %></td>
    																
    																</tr>
    																<%}%>
    																<tr  class="firsttr" bgcolor="#D1DDF5">
    																	<td  height="26" ><strong>MX�ʼ������¼</strong></td>
    																
    																</tr>
    																<%if(mx!=null)
    																{
    																  for(int i=0;i<mx.size();i++)
    																	
    																	{ %>
    																<tr    >
    																	
    																	<td ><%=mx.get(i)%></td>
    																  
    																
    																</tr>
     																	   <%} }%>
     																	<tr  class="firsttr" bgcolor="#D1DDF5">
    																	<td  height="26" ><strong>NS���ַ����¼(�������ַ�������¼)</strong></td>
    																</tr>
    																<% 
    																if(ns!=null)
    																{
    																for(int i=0;i<ns.size();i++)
    																	
    																	{ %>
    																<tr    >
    																
    																	<td ><%=ns.get(i)%></td>
    																   
    																
    																</tr>
    																  <%}} %>
    																<tr  class="firsttr" bgcolor="#D1DDF5">
    																	<td  height="26" ><strong>������Ϣ</strong></td>
    																</tr>
    																<% 
    																if(cache!=null)
    																{
    																for(int i=0;i<cache.size();i++)
    																	
    																	{ %>
    																<tr    >
    																	
    																	<td ><%=cache.get(i) %></td>    
    																  <%} }%>
    																</tr>
                    				</table>
                  	</td>
              </tr>    								
							
						</TABLE>
										 							
										 							
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
									<tr>
										<td>
											
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
</body>
</HTML>