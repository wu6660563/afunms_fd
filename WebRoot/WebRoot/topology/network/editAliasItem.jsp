<%@page language="java" contentType="text/html;charset=gb2312" %>

<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.topology.model.HostNode"%>

<% 

	String rootPath = request.getContextPath(); 
	HostNode vo = (HostNode)request.getAttribute("vo");
    String ip=request.getParameter("ipaddress");
	List allbuss = null;
	BusinessDao bussdao = new BusinessDao();
	 
	try{
	    allbuss = bussdao.loadAll();  
	}catch(Exception e){
	
	}finally{
		bussdao.close();
	}
	
	String bid = vo.getBid();
   	if(bid == null)bid="";
   	String id[] = bid.split(",");
   	List bidlist = new ArrayList();
   	if(id != null &&id.length>0){
		for(int i=0;i<id.length;i++){
		    bidlist.add(id[i]);
		}
  	} 
      
	String sendmobiles =vo.getSendmobiles();
	if( sendmobiles == null || "null".equals(sendmobiles) )	{
		sendmobiles = "";
	}			  	 
         
%>

<%String menuTable = (String)request.getAttribute("menuTable");%>
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
<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
<!--nielin add for timeShareConfig at 2010-01-04 end-->

<script language="JavaScript" type="text/javascript">


  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
 Ext.get("process").on("click",function(){
  
    var chk = checkinput("alias","string","机器名",30,false);
     if(chk)
     {
        mainForm.action = "<%=rootPath%>/network.do?action=updateAlias";
        mainForm.submit();
        data1=document.all.alias.value;
		
		var tree_id = parent.opener.document.getElementById("lable");
		tree_id.innerHTML = data1;
        window.close();
     }
       // mainForm.submit();
 });	
	
});

function CreateWindow(url)
{
	
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}    

function setReceiver(eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/user.do?action=setReceiver&event='+event.id+'&value='+event.value);
}
//-- nielin modify at 2010-01-04 end ----------------

function setBid(eventTextId , eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/business.do?action=setBid&event='+event.id+'&value='+event.value + '&eventText=' + eventTextId);
}


</script>

<script language="JavaScript" type="text/JavaScript">
function editAliasFun(){
  var	alias=document.all.alias.value;
  var	ipaddr=document.all.ip.value;
  var	bids=document.all.bid.value;
  var	idd=document.all.id.value;
 $.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=editAlias&id="+idd+"&alias="+alias+"&ip="+ipaddr+"&bid="+bids+"&nowtime="+(new Date()),
			success:function(data){
			if(data.flagStr==0){
			alert("修改失败！！！");
			}else{
			 var	data=document.all.alias.value;
		    var id = parent.opener.document.getElementById("lable");
		    id.innerHTML = data;
            alert("修改成功！！！");
        	window.close();
			}
			}
		});
		
}
	function getValue() {
		data1=document.all.alias.value;
		alert(data1);
		var tree_id = parent.opener.document.getElementById("lable");
		tree_id.innerHTML = data1;
         
         window.close();

	}

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
<body id="body" class="body" >


	<!-- 右键菜单结束-->
   <form name="mainForm" method="post">
      <input type=hidden name="id" value="<%=vo.getId()%>">
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
											                	<td class="add-content-title"><%=vo.getAlias()%>(<%=vo.getIpAddress()%>) >>&nbsp; 设备详细信息>>&nbsp;编辑设备标签</td>
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
																			<tr style="background-color: #ECECEC;">
													                  					<TD nowrap align="right" height="24" width="10%">机器名&nbsp;</TD>
													                  					<TD nowrap width="40%">&nbsp;
													                  					<input type="text" name="alias" maxlength="50" size="20" class="formStyle" value="<%=vo.getAlias()%>">
													                  					</TD>
													                				</tr>
																			<tr> 
													  							      
																				<td nowrap width="40%" colspan=2>&nbsp;
																				<input type="hidden" id="sendmobiles" name="sendmobiles" maxlength="32" size="50" value="<%=sendmobiles%>"  readonly="readonly">&nbsp;&nbsp; 
																				
																				<input type="hidden" id="sendemail" name="sendemail" maxlength="32" size="50" value="<%=vo.getSendemail()%>"  readonly="readonly">&nbsp;&nbsp;
																				<input type="hidden" id="ip" name="ip" maxlength="32" size="50" value="<%=vo.getIpAddress()%>"  readonly="readonly">&nbsp;&nbsp;
																				<input type="hidden" id="rowNum" name="rowNum">
																				
																				
																				<% 
																					String bussName = "";
																					if( allbuss.size()>0){
																						for(int i=0;i<allbuss.size();i++){
																							Business buss = (Business)allbuss.get(i);
																							
																							if(bidlist.contains(buss.getId()+"")){
																								bussName = bussName + ',' + buss.getName();
																							}
																							
																		 				}
																					}
																				%>   
																				<input type=hidden readonly="readonly" onclick='setBid("bidtext" , "bid");' id="bidtext" name="bidtext" size="50" maxlength="32" value="<%=bussName%>">&nbsp;&nbsp;
																				<input type="hidden" id="bid" name="bid" value="<%=vo.getBid()%>" >
																				
																			</tr>
																			<tr>
																				<TD nowrap colspan="4" align=center colspan=2>
																				<br><input type="button" value="保 存" style="width:50" id="process1" onclick="editAliasFun()">&nbsp;&nbsp;
																					
																					<input type="reset" style="width:50" value="关  闭" onclick="window.close();">
																				</TD>	
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