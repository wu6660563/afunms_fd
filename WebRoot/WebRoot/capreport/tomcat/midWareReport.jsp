<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="java.util.List"%>
<%@page import="java.util.Vector"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.afunms.application.model.Tomcat"%>
<%@page import="com.afunms.application.model.WeblogicConfig"%>
<%@page import="com.afunms.application.model.WasConfig"%>
<%@page import="com.afunms.application.model.IISConfig"%>

<%
  String rootPath = request.getContextPath();
 
  List tomcatList = (List)request.getAttribute("tomcatList");
  List iisList = (List)request.getAttribute("iisList");
  List weblogicList = (List)request.getAttribute("weblogicList");
  List waslist = (List)request.getAttribute("waslist");

  String[] tomcatItems={"连通率","虚拟利用率"};
   String[] tomcatItemId={"ping","jvm"};
  String[] items={"连通率"};
   String[] itemId={"ping"};
  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String	startdate = sdf.format(new Date());
			String	todate = sdf.format(new Date());
			
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

<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8"/>
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>


<script>



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
	
	ddtree.insertNewItem("","root","中间件", 0, "", "","", "");
	
	<% 
	
	Tomcat vo = new Tomcat();
	Vector<String> vector=new Vector<String>();
	
	if(tomcatList!=null&&tomcatList.size()>0)
	{
	
	for(int i=0;i<tomcatList.size();i++){
	vo=(Tomcat)tomcatList.get(i);
	vector.add(vo.getIpAddress());
	%>
	ddtree.insertNewItem("root","<%=vo.getIpAddress()%>","<%=vo.getIpAddress()%>", 0, "", "","", "");
	ddtree.insertNewItem("<%=vo.getIpAddress()%>","<%=vo.getId()%>","<%=vo.getAlias()%>", 0, "", "","", "");
	
	<%
	for(int j=0;j<tomcatItems.length;j++){
	%>
	ddtree.insertNewItem("<%=vo.getId()%>","tomcat*"+"<%=tomcatItemId[j]%>"+"*"+"<%=vo.getId()%>"+"*"+"<%=vo.getIpAddress()%>","<%=tomcatItems[j]%>", 0, "", "","", "");
	
	<%
	}
	
	}
	}
	IISConfig iis=new IISConfig();
	if(iisList!=null&&iisList.size()>0)
	{
	
	for(int i=0;i<iisList.size();i++){
	iis=(IISConfig)iisList.get(i);
	if(!vector.contains(iis.getIpaddress())){
	
	vector.add(iis.getIpaddress());
	%>
	ddtree.insertNewItem("root","<%=iis.getIpaddress()%>","<%=iis.getIpaddress()%>", 0, "", "","", "");
	<%
	}
		%>
	ddtree.insertNewItem("<%=iis.getIpaddress()%>","<%=iis.getId()%>"+"<%=iis.getName()%>","<%=iis.getName()%>", 0, "", "iis.gif","iis.gif", "");
	
	<%
	for(int j=0;j<items.length;j++){
	%>
	ddtree.insertNewItem("<%=iis.getId()%>"+"<%=iis.getName()%>","iis*"+"<%=itemId[j]%>"+"*"+"<%=iis.getId()%>"+"*"+"<%=iis.getIpaddress()%>","<%=items[j]%>", 0, "", "","", "");
	
	<%
	}
	}
	}
	
	WasConfig was=new WasConfig();
	if(waslist!=null&&waslist.size()>0)
	{
	
	for(int i=0;i<waslist.size();i++){
	was=(WasConfig)waslist.get(i);
	if(!vector.contains(was.getIpaddress())){
	 
	vector.add(was.getIpaddress());
	%>
	ddtree.insertNewItem("root","<%=was.getIpaddress()%>","<%=was.getIpaddress()%>", 0, "", "","", "");
	<%
	}
	%>
	ddtree.insertNewItem("<%=was.getIpaddress()%>","<%=was.getId()%>","<%=was.getServername()%>", 0, "", "","", "");
	
	<%
	for(int j=0;j<items.length;j++){
	%>
	ddtree.insertNewItem("<%=was.getId()%>","was"+"*"+"<%=tomcatItemId[j]%>"+"*"+"<%=was.getId()%>"+"*"+"<%=was.getIpaddress()%>","<%=items[j]%>", 0, "", "","", "");
	
	<%
	}
	}
	}
	WeblogicConfig weblog=new WeblogicConfig();
	if(weblogicList!=null&&weblogicList.size()>0)
	{
	
	for(int i=0;i<weblogicList.size();i++){
	weblog=(WeblogicConfig)weblogicList.get(i);
	if(!vector.contains(weblog.getIpAddress())){
	 
	vector.add(weblog.getIpAddress());
	%>
	ddtree.insertNewItem("root","<%=weblog.getIpAddress()%>","<%=weblog.getIpAddress()%>", 0, "", "","", "");
	<%
	}
	%>
	ddtree.insertNewItem("<%=weblog.getIpAddress()%>","<%=weblog.getId()%>","<%=weblog.getAlias()%>", 0, "", "","", "");
	
	<%
	for(int j=0;j<tomcatItems.length;j++){
	%>
	ddtree.insertNewItem("<%=weblog.getId()%>","<%=tomcatItemId[j]%>"+"*"+"<%=weblog.getId()%>","<%=tomcatItems[j]%>", 0, "", "","", "");
	
	<%
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

function CreateWindow(url)
{
	
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=400,height=400,directories=no,status=no,scrollbars=no,menubar=no")
}   
function loadIds(id,ids,startdate,todate){
 $.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=execMidReport&id="+id+"&ids="+ids+"&startdate="+startdate+"&todate="+todate+"&nowtime="+(new Date()),
			success:function(data){
			
				 if(data.pingdata==0){ 
				  var pingDiv =document.getElementById("pingDiv");
				  pingDiv.innerHTML="";
				  var netpingReport =document.getElementById("netpingReport");
				  netpingReport.innerHTML="";
			     }else{
			     var so = new SWFObject("<%=rootPath%>/amchart/amline.swf", "ampie","885", "338", "8", "#FFFFFF");
		         so.addVariable("path", "<%=rootPath%>/amchart/");
		         so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/netPingReport_settings.xml"));
			     so.addVariable("chart_data",data.pingdata);
				 so.write("netpingReport");
				var pingDiv =document.getElementById("pingDiv");
				
				 pingDiv.innerHTML=data.pingHtml;
			      
				}
			       if(data.jvmdata==0){ 
				  var jvmDiv =document.getElementById("jvmDiv");
				  jvmDiv.innerHTML="";
				  var jvmReport =document.getElementById("jvmReport");
				  jvmReport.innerHTML="";
			     }else{
			     var so = new SWFObject("<%=rootPath%>/amchart/amline.swf", "ampie","885", "338", "8", "#FFFFFF");
		         so.addVariable("path", "<%=rootPath%>/amchart/");
		         so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/jvmReport_settings.xml"));
			     so.addVariable("chart_data",data.jvmdata);
				 so.write("jvmReport");
				var jvmDiv =document.getElementById("jvmDiv");
				
				 jvmDiv.innerHTML=data.jvmHtml;
			      
				}
			      }
			
		});
}
////根据模板选项预览
function preview(id){
var tab = Ext.getCmp('ext-tab-report');
 tab.setActiveTab(0);
document.getElementById('editmodel').style.display='none';
 var	startdate=document.all.startdate.value;
   var	todate=document.all.todate.value;
   var ids=null;
   document.all.ids_perform.value=ids;
	document.all.id_perform.value=id;
  loadIds(id,ids,startdate,todate)
}

function query_ok(){

  
   var	startdate=document.all.startdate.value;
   var	todate=document.all.todate.value;
	var	ids = ddtree.getAllChecked().toString();
	
	if(ids.length<=0|| ids.length == "")
	{
	alert("请选择设备的性能选项！！！");
	return;
	}
	var id=null;
	document.all.ids_perform.value=ids;
	document.all.id_perform.value=id;
  loadIds(id,ids,startdate,todate)
}
//保存中间件的性能指标
function savePerforIndex(){


   var	startdate=document.all.startdate.value;
   var	todate=document.all.todate.value;
	var	ids = ddtree.getAllChecked().toString();
	
	if(ids.length<=0|| ids.length == "")
	{
	alert("请选择设备的性能选项！！！");
	return;
	}
   $.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=savePerforIndex&ids="+ids+"&startdate="+startdate+"&todate="+todate+"&type=midware&nowtime="+(new Date()),
			success:function(data){
			alert(data.dataStr)
			}
		});
}
</script>


<!-- Tab页 -->
<script language="JavaScript" type="text/JavaScript">

Ext.onReady(function(){
var	ids = ddtree.getAllChecked().toString();

     var tabs = new Ext.TabPanel({
     	id: 'ext-tab-report',
        renderTo: 'tabs1',
        width:888,
        activeTab: 0,
        frame:true,
        defaults:{autoHeight: true},
        items:[
            {contentEl:'script', title: '报表设置'},
           
            {contentEl:'model', title: '报表模板列表',listeners: {activate: handleActivate}}
        ]
    });
     function handleActivate(tab){
   
   $.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=loadPeforIndex&type=midware&nowtime="+(new Date()),
			success:function(data){
			
			var modelDiv =document.getElementById("model");
				 modelDiv.innerHTML=data.dataStr;
			}
		});
    }
});

  </script>
  <!-- 模板 -->
<script>
//模板创建
	$(document).ready(function(){
		$('#saveBtn').bind('click',function(){
		var report_name=$('#report_name').val();
		var name=$('#recievers_name').val();
		var tile=$('#tile').val();
		var desc=$('#desc').val();
		var bid=$('#bid').val();
		var bidtext=$('#bidtext').val();
		var reporttype=$('#reporttype').val();
		var exporttype=$('#exporttype').val();
		var frequency=$('#transmitfrequency').val();
		var month=$('#sendtimemonth').val();
		var week=$('#sendtimeweek').val();
		var day=$('#sendtimeday').val();
		var hou=$('#sendtimehou').val();
		var re_id=$('#recievers_id').val();
		
			if($('#recievers_name').val()==null || $('#recievers_name').val()=='')
				return;
			if($('#bidtext').val()==null || $('#bidtext').val()=='')
				return;
	
			if($('#timeConfigTable tr').length<2)
				return;
		//	mainForm.action = "<%=rootPath%>/networkDeviceAjaxManager.ajax?action=savePerforIndex";
        //	mainForm.submit();
        var	ids = ddtree.getAllChecked().toString();
	
	if(ids.length<=0|| ids.length == "")
	{
	   alert("请选择设备的性能选项！！！");
	   return;
	}
        $.ajax({
			type:"POST",
			dataType:"json",
			data:"type=midware&ids="+ids+"&tile="+tile+"&desc="+desc+"&bid="+bid+"&bidtext="+bidtext+"&exporttype="+exporttype+"&reporttype="+reporttype+"&report_name="+report_name+"&recievers_name="+name+"&transmitfrequency="+frequency+"&sendtimemonth="+month+"&sendtimeweek="+week+"&sendtimeday="+day+"&sendtimehou="+hou+"&recievers_id="+re_id+"&nowtime="+(new Date()),
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=savePerforIndex",
			success:function(data){
			
			alert(data.dataStr);
			 var tab = Ext.getCmp('ext-tab-report');
           tab.setActiveTab(1);	
			}
		});
        
        
		});
	});
	
	//加载模板选项的详细信息
function createWin(id){

return CreateDeviceWindow("<%=rootPath%>/netreport.do?action=networkReportConfig&id="+id);

}
	//编辑模板
function editModel(){

document.getElementById('editmodel').style.display='block';

}
//隐藏模板
function hiddenModel(){

document.getElementById('editmodel').style.display='none';

}
//删除模板
function deleteItem(id){
 if(window.confirm("您确定要删除吗？")){
  $.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=loadPeforIndex&type=midware&id="+id+"&nowtime="+(new Date()),
			success:function(data){
			
			var modelDiv =document.getElementById("model");
				 modelDiv.innerHTML=data.dataStr;
			}
		});
  } 
} 

</script>
<script language="JavaScript" type="text/javascript">
  
//-- nielin add at 2010-07-27 start ----------------
function setBid(eventTextId , eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/business.do?action=setBid&event='+event.id+'&value='+event.value + '&eventText=' + eventTextId);
}
function CreateDeviceWindow(url)
{
	
msgWindow=window.open(url,"_blank","toolbar=no,width=900,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}

function setReciever(ctrlId,hideCtrlId)
{
	return CreateWindow("<%=rootPath%>/subscribeReportConfig.do?action=user_list&&ctrlId="+ctrlId+"&&hideCtrlId="+hideCtrlId);
}
function setDevices(ctrlId,hideCtrlId)
{
	return CreateDeviceWindow("<%=rootPath%>/subscribeReportConfig.do?action=device_list&&ctrlId="+ctrlId+"&&hideCtrlId="+hideCtrlId);
}
function timeType(obj){
	var type = obj.value;
	document.getElementById('td_sendtimehou').style.display='none';
	document.getElementById('td_sendtimeday').style.display='none';
	document.getElementById('td_sendtimeweek').style.display='none';
	document.getElementById('td_sendtimemonth').style.display='none';
	document.getElementById('sendtimehou').disabled="disabled";
	document.getElementById('sendtimeday').disabled="disabled";
	document.getElementById('sendtimeweek').disabled="disabled";
	document.getElementById('sendtimemonth').disabled="disabled";
	if(type==1){
		document.getElementById('td_sendtimehou').style.display='';
		document.getElementById('sendtimehou').disabled="";
	}else if(type==2){
		document.getElementById('td_sendtimehou').style.display='';
		document.getElementById('td_sendtimeweek').style.display='';
		document.getElementById('sendtimehou').disabled="";
		document.getElementById('sendtimeweek').disabled="";
	}else if(type==3){
		document.getElementById('td_sendtimehou').style.display='';
		document.getElementById('td_sendtimeday').style.display='';
		document.getElementById('sendtimehou').disabled="";
		document.getElementById('sendtimeday').disabled="";
	}else if(type==4){
		document.getElementById('td_sendtimehou').style.display='';
		document.getElementById('td_sendtimeday').style.display='';
		document.getElementById('td_sendtimemonth').style.display='';
		document.getElementById('sendtimehou').disabled="";
		document.getElementById('sendtimeday').disabled="";
		document.getElementById('sendtimemonth').disabled="";
	}else if(type==5){
		document.getElementById('td_sendtimehou').style.display='';
		document.getElementById('td_sendtimeday').style.display='';
		document.getElementById('td_sendtimemonth').style.display='';
		document.getElementById('sendtimehou').disabled="";
		document.getElementById('sendtimeday').disabled="";
		document.getElementById('sendtimemonth').disabled="";
	}
}
function CreateWindow(url)
{
	
 msgWindow=window.open(url,"_blank","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}
//导出报表
function exportReport(exportType){
 var	startdate=document.all.startdate.value;
 var	todate=document.all.todate.value;
 var ids=document.all.ids_perform.value;
 var id=document.all.id_perform.value;

if(ids.length<=0|| ids.length == ""){
 ids = ddtree.getAllChecked().toString();
if((ids.length<=0|| ids.length == "")&&(id.length<=0|| id.length == "")){
	alert("请选择设备的性能选项或模板！！！");
	return;
	}
	}
 window.open('<%=rootPath%>/netreport.do?action=downloadReport&type=midware&exportType='+exportType+'&id='+id+'&ids='+ids+'&startdate='+startdate+'&todate='+todate+'&nowtime='+(new Date()),"_blank","toolbar=no,width=1,height=1,top=2000,left=3000,directories=no,status=no,menubar=no,alwaysLowered=yes");
 
}

</script>
	</head>
	<body id="body" class="body" onLoad="init();">
		<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0
			noResize scrolling=no src="<%=rootPath%>/include/calendar.htm"
			style="DISPLAY: none; HEIGHT: 189px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>

		<form id="mainForm" method="post" name="mainForm">
			<div id="loading">
				<div class="loading-indicator">
					<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif"
						width="32" height="32" style="margin-right: 8px;" align="middle" />
					Loading...
				</div>
			</div>
			<div id="loading-mask" style=""></div>
			<table id="body-container" class="body-container">
				<tr>

					<td valign=top style="margin: 10px auto; position: relative;">
						<table>
							<tr>
								<td colspan="2">
									<div
										style="height:60px;margin-bottom:10px; background: url('<%=rootPath%>/resource/image/tree/tit_bg.gif'); padding-left:40px; line-height:60px; font-size: 24px; font-weight: bold; color: #fff;">
										中间件报表
									</div>
								</td>
							</tr>


							<tr>
								<td height="100%" align="left" valign="top">
									<div id="sorttree"
										style="margin:0 8px 0 0; padding:10px; background:#dce9f2; height:600px; width: 240px;"></div>
								</td>
								<td width="80%" height="100%" valign="top">
									<div id='tabs1'>
										<div id="script" class="x-hide-display">
											<table border="0" cellpadding="0" cellspacing="0"
												class="win-content" id="win-content">

												<tr>

													<td colspan="1" width="94%">
														<table id="win-content-body" class="win-content-body">
															<tr>

																<td>
																	<div>
																		<table bgcolor="#ECECEC">
																			<tr align="left" valign="middle">
																				<td height="21" align="left" valign=top>
																					&nbsp;&nbsp;&nbsp;开始日期
																					<input type="text" id="mystartdate"
																						name="startdate" value="<%=startdate%>" size="10">
																					<a onClick="event.cancelBubble=true;"
																						href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
																						<img id=imageCalendar1 width=34 height=21
																							src="<%=rootPath%>/include/calendar/button.gif"
																							border=0> </a> 选择日期
																					<input type="text" id="mytodate" name="todate"
																						value="<%=todate%>" size="10" />
																					<a onClick="event.cancelBubble=true;"
																						href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
																						<img id=imageCalendar2 width=34 height=21
																							src="<%=rootPath%>/include/calendar/button.gif"
																							border=0> </a> &nbsp;&nbsp;
																					<input type="button" name="doprocess" value="预  览"
																						onClick="query_ok()">
																					<!-- 	&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" name="save" value="保存模板" onClick="savePerforIndex()">
																								&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" name="create" value="新建模板" onClick="createModel()"> -->
																					&nbsp;&nbsp;&nbsp;&nbsp;
																					<input type="button" name="edit" value="编辑模板"
																						onClick="editModel()">
																						<input type="hidden" name="ids_perform" id="ids_perform" value=""/>
																						<input type="hidden" name="id_perform" id="id_perform" value=""/>
                                                                                   <span style="CURSOR:hand" onclick="exportReport('doc')" ><img name="doc" alt='导出WORD'  src="<%=rootPath%>/resource/image/export_word.gif" width=18  border="0" >导出WORD</span>&nbsp;&nbsp;&nbsp;&nbsp;
                                                                                    <span style="CURSOR:hand" onclick="exportReport('xls')" ><img name="xls" alt='导出EXCEL'  src="<%=rootPath%>/resource/image/export_excel.gif" width=18  border="0" >导出EXCEL</span>&nbsp;&nbsp;&nbsp;&nbsp;
                                                                                    <span style="CURSOR:hand" onclick="exportReport('pdf')" ><img name="pdf" alt='导出PDF'  src="<%=rootPath%>/resource/image/export_pdf.gif" width=18  border="0" >导出PDF</span>&nbsp;&nbsp;&nbsp;&nbsp;

																				</td>

																			</tr>
																		</table>
																	</div>
																</td>

															</tr>

														</table>
													</td>
												</tr>

												<tr>
													<td width=100%>
														<jsp:include page="../busireport/subscribeAdd.jsp"></jsp:include>
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
																				       <div id="jvmReport">
							                                                           </div>
						                                                            </td>
																			    </tr>
																		</table>
                                                                      </td>
                                                                      
                                                                  </tr>
                                                                  <tr>
                                                                  
                                                                      <td width=94%>
                                                                          <div id="jvmDiv">
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

										</div>

										<div id="model" class="x-hide-display">
										</div>
									</div>
								</td>
							</tr>
						</table>

					</td>
				</tr>
			</table>
		</form>

	</BODY>
</HTML>