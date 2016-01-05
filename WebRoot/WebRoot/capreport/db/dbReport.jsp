<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.afunms.application.model.DBVo"%>

<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  
  List list = (List)request.getAttribute("list");

  String[] items={"��ͨ��","����"};
   String[] itemId={"ping","val"};
    String[] oraItems={"��ͨ��","��ռ�"};
   String[] oraItemsId={"ping","tablespace"};
   String[] items1={"��ͨ��"};
   String[] itemId1={"ping"};
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
	
	
	ddtree.insertNewItem("","root","���ݿ�", 0, "", "","", "");
	
	<% 
	
	DBVo vo = new DBVo();
	if(list!=null&&list.size()>0)
	{
	String temp="";
	for(int i=0;i<list.size();i++){
	vo=(DBVo)list.get(i);
	if(temp.equals(vo.getIpAddress()))
	   continue;
	temp=vo.getIpAddress();
	%>
	ddtree.insertNewItem("root","<%=vo.getIpAddress()%>","<%=vo.getIpAddress()%>", 0, "", "","", "");
	<%
	
	}
	for(int j=0;j<list.size();j++){
	vo=(DBVo)list.get(j);
	if(vo.getDbtype()==4){
	%>
	ddtree.insertNewItem("<%=vo.getIpAddress()%>","<%=vo.getId()%>","<%=vo.getAlias()%>", 0, "", "mysql.gif","mysql.gif", "");
	<%
	}else if(vo.getDbtype()==1){
	%>
	ddtree.insertNewItem("<%=vo.getIpAddress()%>","<%=vo.getId()%>","<%=vo.getAlias()%>", 0, "", "oracle.gif","oracle.gif", "");
	<%
	}else if(vo.getDbtype()==2){
	%>
	ddtree.insertNewItem("<%=vo.getIpAddress()%>","<%=vo.getId()%>","<%=vo.getAlias()%>", 0, "", "sqlserver.gif","sqlserver.gif", "");
	<%
	}else if(vo.getDbtype()==3){
	%>
	ddtree.insertNewItem("<%=vo.getIpAddress()%>","<%=vo.getId()%>","<%=vo.getAlias()%>", 0, "", "oracle.gif","oracle.gif", "");
	<%
	}else if(vo.getDbtype()==5){
	%>
	ddtree.insertNewItem("<%=vo.getIpAddress()%>","<%=vo.getId()%>","<%=vo.getAlias()%>", 0, "", "db2.gif","db2.gif", "");
	<%
	}else if(vo.getDbtype()==6){
	%>
	ddtree.insertNewItem("<%=vo.getIpAddress()%>","<%=vo.getId()%>","<%=vo.getAlias()%>", 0, "", "sybase.gif","sybase.gif", "");
	<%
	}else if(vo.getDbtype()==7){
	%>
	ddtree.insertNewItem("<%=vo.getIpAddress()%>","<%=vo.getId()%>","<%=vo.getAlias()%>", 0, "", "informix.gif","informix.gif", "");
	<%
	}
	if(vo.getDbtype()==1){
	for(int i=0;i<oraItems.length;i++){
	%>
	ddtree.insertNewItem("<%=vo.getId()%>","<%=oraItemsId[i]%>"+"*"+"<%=vo.getDbtype()%>"+"*"+"<%=vo.getId()%>"+"*"+"<%=vo.getIpAddress()%>","<%=oraItems[i]%>", 0, "", "","", "");
	
	<%
	}
	}else if(vo.getDbtype()==4){
	for(int i=0;i<items.length;i++){
	%>
	ddtree.insertNewItem("<%=vo.getId()%>","<%=itemId[i]%>"+"*"+"<%=vo.getDbtype()%>"+"*"+"<%=vo.getId()%>"+"*"+"<%=vo.getIpAddress()%>","<%=items[i]%>", 0, "", "","", "");
	
	<%
	}
	}else {
	for(int i=0;i<items1.length;i++){
	%>
	ddtree.insertNewItem("<%=vo.getId()%>","<%=itemId1[i]%>"+"*"+"<%=vo.getDbtype()%>"+"*"+"<%=vo.getId()%>"+"*"+"<%=vo.getIpAddress()%>","<%=items1[i]%>", 0, "", "","", "");
	
	<%
	}
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
			type:"POST",
			dataType:"json",
			data:"id="+id+"&ids="+ids+"&startdate="+startdate+"&todate="+todate+"&nowtime="+(new Date()),
			url:"<%=rootPath%>/oracleAjaxManager.ajax?action=dbReport",
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
				if(data.tabledata==0){ 
				  var tableDiv =document.getElementById("tableDiv");
				  tableDiv.innerHTML="";
				  var tableReport =document.getElementById("tableReport");
				  tableReport.innerHTML="";
			     }else{
			     var so = new SWFObject("<%=rootPath%>/amchart/amline.swf", "ampie","885", "338", "8", "#FFFFFF");
		         so.addVariable("path", "<%=rootPath%>/amchart/");
		         so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/tablespaceReport_settings.xml"));
			     so.addVariable("chart_data",data.tabledata);
				 so.write("tableReport");
				var tableDiv =document.getElementById("tableDiv");
				
				 tableDiv.innerHTML=data.tableHtml;
			      
				}
			      var valDiv =document.getElementById("valDiv");
				
				 valDiv.innerHTML=data.valHtml;
			      }
			
		});
  }
function query_ok(){

  
   var	startdate=document.all.startdate.value;
   var	todate=document.all.todate.value;
	var	ids = ddtree.getAllChecked().toString();
	var id=null;
	if(ids.length<=0|| ids.length == "")
	{
	alert("��ѡ���豸������ѡ�����");
	return;
	}
	document.all.ids_perform.value=ids;
	document.all.id_perform.value=id;
  loadIds(id,ids,startdate,todate);
}


</script>

	<script language="JavaScript" type="text/JavaScript">
//Tabҳ
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
            {contentEl:'script', title: '��������'},
           
            {contentEl:'model', title: '����ģ���б�',listeners: {activate: handleActivate}}
        ]
    });
     function handleActivate(tab){
   
   $.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=loadPeforIndex&type=db&nowtime="+(new Date()),
			success:function(data){
			
			var modelDiv =document.getElementById("model");
				 modelDiv.innerHTML=data.dataStr;
			}
		});
    }
});

</script>
		<script>
//ģ�崴��
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
	   alert("��ѡ���豸������ѡ�����");
	   return;
	}
        $.ajax({
			type:"POST",
			dataType:"json",
			data:"type=db&ids="+ids+"&tile="+tile+"&desc="+desc+"&bid="+bid+"&bidtext="+bidtext+"&exporttype="+exporttype+"&reporttype="+reporttype+"&report_name="+report_name+"&recievers_name="+name+"&transmitfrequency="+frequency+"&sendtimemonth="+month+"&sendtimeweek="+week+"&sendtimeday="+day+"&sendtimehou="+hou+"&recievers_id="+re_id+"&nowtime="+(new Date()),
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=savePerforIndex",
			success:function(data){
			
			alert(data.dataStr);
			 var tab = Ext.getCmp('ext-tab-report');
           tab.setActiveTab(1);	
			}
		});
        
        
		});
	});
	//ɾ��ģ��ѡ��
function deleteItem(id){
 if(window.confirm("��ȷ��Ҫɾ����")){
  $.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=loadPeforIndex&type=db&id="+id+"&nowtime="+(new Date()),
			success:function(data){
			
			var modelDiv =document.getElementById("model");
				 modelDiv.innerHTML=data.dataStr;
			}
		});
   }
} 
//����ģ��ѡ�����ϸ��Ϣ
function createWin(id){

return CreateDeviceWindow("<%=rootPath%>/netreport.do?action=networkReportConfig&id="+id);

}
//����ģ��ѡ��Ԥ��
function preview(id){
  var	startdate=document.all.startdate.value;
   var	todate=document.all.todate.value;
   var tab = Ext.getCmp('ext-tab-report');
 tab.setActiveTab(0);
document.getElementById('editmodel').style.display='none';
	var ids=null;
	document.all.ids_perform.value=ids;
	document.all.id_perform.value=id;
  loadIds(id,ids,startdate,todate);
}
//�༭ģ��
function editModel(){

document.getElementById('editmodel').style.display='block';

}
//����ģ��
function hiddenModel(){

document.getElementById('editmodel').style.display='none';

}
//�༭ģ����Ϣ
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
//��������
function exportReport(exportType){
 var	startdate=document.all.startdate.value;
 var	todate=document.all.todate.value;
 var ids=document.all.ids_perform.value;
 var id=document.all.id_perform.value;

if(ids.length<=0|| ids.length == ""){
 ids = ddtree.getAllChecked().toString();
if((ids.length<=0|| ids.length == "")&&(id.length<=0|| id.length == "")){
	alert("��ѡ���豸������ѡ���ģ�壡����");
	return;
	}
	}
 window.open('<%=rootPath%>/netreport.do?action=downloadReport&type=db&exportType='+exportType+'&id='+id+'&ids='+ids+'&startdate='+startdate+'&todate='+todate+'&nowtime='+(new Date()),"_blank","toolbar=no,width=1,height=1,top=2000,left=3000,directories=no,status=no,menubar=no,alwaysLowered=yes");
 
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
										���ݿⱨ��
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
																					&nbsp;&nbsp;&nbsp;��ʼ����
																					<input type="text" id="mystartdate"
																						name="startdate" value="<%=startdate%>" size="10">
																					<a onClick="event.cancelBubble=true;"
																						href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
																						<img id=imageCalendar1 width=34 height=21
																							src="<%=rootPath%>/include/calendar/button.gif"
																							border=0> </a> ѡ������
																					<input type="text" id="mytodate" name="todate"
																						value="<%=todate%>" size="10" />
																					<a onClick="event.cancelBubble=true;"
																						href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
																						<img id=imageCalendar2 width=34 height=21
																							src="<%=rootPath%>/include/calendar/button.gif"
																							border=0> </a> &nbsp;&nbsp;
																					<input type="button" name="doprocess" value="Ԥ  ��"
																						onClick="query_ok()">
																					<!-- 	&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" name="save" value="����ģ��" onClick="savePerforIndex()">
																								&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" name="create" value="�½�ģ��" onClick="createModel()"> -->
																					&nbsp;&nbsp;&nbsp;&nbsp;
																					<input type="button" name="edit" value="�༭ģ��"
																						onClick="editModel()">
                                                                                       <input type="hidden" name="ids_perform" id="ids_perform" value=""/>
																						<input type="hidden" name="id_perform" id="id_perform" value=""/>
                                                                                    <span style="CURSOR:hand" onclick="exportReport('doc')" ><img name="doc" alt='����WORD'  src="<%=rootPath%>/resource/image/export_word.gif" width=18  border="0" >����WORD</span>&nbsp;&nbsp;&nbsp;&nbsp;
                                                                                     <span style="CURSOR:hand" onclick="exportReport('xls')" ><img name="xls" alt='����EXCEL'  src="<%=rootPath%>/resource/image/export_excel.gif" width=18  border="0" >����EXCEL</span>&nbsp;&nbsp;&nbsp;&nbsp;
                                                                                     <span style="CURSOR:hand" onclick="exportReport('pdf')" ><img name="pdf" alt='����PDF'  src="<%=rootPath%>/resource/image/export_pdf.gif" width=18  border="0" >����PDF</span>&nbsp;&nbsp;&nbsp;&nbsp;
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
																				       <div id="valDiv">
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
																				       <div id="tableReport">
							                                                           </div>
						                                                            </td>
																			    </tr>
																		</table>
                                                                      </td>
                                                                      
                                                                 </tr>
                                                                  <tr>
                                                                  
                                                                      <td width=94%>
                                                                          <div id="tableDiv">
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