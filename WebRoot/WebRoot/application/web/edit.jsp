<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.application.model.*"%>

<%
    String rootPath = request.getContextPath();
    String menuTable = (String)request.getAttribute("menuTable");
    URLConfig vo = (URLConfig) request.getAttribute("vo");
    String bidNames = (String) request.getAttribute("bidNames");
    int monFlag = vo.getMonFlag();
    String monFlagSelected = "selected=\"selected\"";
    if (monFlag == 0) {
        monFlagSelected = ""; 
    }
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
<!-- snow add for gatherTime at 2010-5-21 start -->
<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/addTimeConfig.js" charset="gb2312"></script>
<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/addWordConfig.js" charset="gb2312"></script>
<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/jquery-1.4.2.js" charset="gb2312"></script>
<script type="text/javascript">
	$(addTimeConfigRow);//addTimeConfigRow函数在application/resource/js/addTimeConfig.js中 
	$(addWordConfigRow);
</script>
<!-- snow add for gatherTime at 2010-5-21 end -->


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
  
    //var chk1 = checkinput("alias","string","名称",15,false);
     //var chk2 = checkinput("str","string","路径",30,false);
     //var chk3 = checkinput("ipaddress","ip","IP地址",30,false);
     //var chk4 = checkinput("timeout","number","超时",30,false);
     
     //if(chk1&&chk2&&chk3&&chk4)
     {
     
        Ext.MessageBox.wait('数据加载中，请稍后.. '); 
        //msg.style.display="block";
        mainForm.action = "<%=rootPath%>/url.do?action=update";
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


//-- nielin add at 2010-07-27 start ----------------
function setBid(eventTextId , eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/business.do?action=setBid&event='+event.id+'&value='+event.value + '&eventText=' + eventTextId);
}
//-- nielin add at 2010-07-27 end ----------------




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
	wordShareConfiginit();
}

</script>
</head>
<body id="body" class="body" onload="initmenu();">
	<form name="mainForm" method="post">
		<input type=hidden name="id" value="<%=vo.getId()%>">
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
											                	<td class="add-content-title">应用 >> 服务管理 >> URL 编辑 </td>
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
                                                                            <TD nowrap align="right" height="24" width="10%">名称&nbsp;</TD>              
                                                                            <TD nowrap width="40%" colspan=3>&nbsp;
                                                                                <input type="text" id="name" name="name" maxlength="50" size="20" class="formStyle" value="<%=vo.getName()%>">
                                                                            </TD>
                                                                        </tr>
                                                                        <tr>                 
                                                                            <TD nowrap align="right" height="24" width="10%">所在服务器IP&nbsp;</TD>              
                                                                            <TD nowrap width="40%" colspan=3>&nbsp;
                                                                                <input type="text" id="ipaddress" name="ipaddress" maxlength="50" size="20" class="formStyle" value="<%=vo.getIpaddress()%>">
                                                                            </TD>
                                                                        </tr>
                                                                        <tr style="background-color: #ECECEC;">                    
                                                                            <TD nowrap align="right" height="24" width="10%">URL 路径&nbsp;</TD>              
                                                                            <TD nowrap width="40%" colspan=3>&nbsp;
                                                                                <input type="text" id="url" name="url" maxlength="50" size="50" class="formStyle" value="<%=vo.getUrl()%>">
                                                                            </TD>
                                                                        </tr>
                                                                        <tr >                       
                                                                            <TD nowrap align="right" height="24" width="10%">超时(毫秒)&nbsp;</TD>              
                                                                            <TD nowrap width="40%" colspan=3>&nbsp;
                                                                                <input type="text" id="timeout" name="timeout" maxlength="50" size="20" class="formStyle" value="<%=vo.getTimeout()%>">
                                                                            </TD>       
                                                                        </tr>
                                                                        <tr style="background-color: #ECECEC;">                 
                                                                            <TD nowrap align="right" height="24" width="10%">页面大小&nbsp;</TD>              
                                                                            <TD nowrap width="40%" colspan=3>&nbsp;
                                                                                <input type="text" id="pageSize" name="pageSize" maxlength="50" size="20" class="formStyle" value="<%=vo.getPageSize()%>">KB
                                                                            </TD>                                                           
                                                                        </tr>
                                                                        <tr>                    
                                                                            <TD nowrap align="right" height="24" width="10%">是否监控&nbsp;</TD>                
                                                                            <TD nowrap width="40%">&nbsp;
                                                                                <select name="monFlag" class="formStyle">
                                                                                    <option value=0>否</option>
                                                                                    <option value=1 <%=monFlagSelected%>>是</option>
                                                                                </select>
                                                                            </TD>                                                           
                                                                        </tr>
                                                                        <tr style="background-color: #ECECEC;">    
                                                                            <TD nowrap align="right" height="24">所属网元组&nbsp;</TD>
                                                                            <td nowrap colspan="3" height="1">&nbsp;
                                                                                <input type=text readonly="readonly" onclick='setBid("bidtext" , "bid");' id="bidtext" name="bidtext" size="50" maxlength="32" value="<%=bidNames%>">&nbsp;&nbsp;<input type=button value="设置所属网元组" onclick='setBid("bidtext" , "bid");'>
                                                                                <input type="hidden" id="bid" name="bid" value="">
                                                                            </td>
                                                                        </tr>
                                                                        <tr>
                                                                            <TD nowrap colspan="4" align=center>
                                                                            <br><input type="button" value="保 存" style="width:50" id="process" onclick="#">&nbsp;&nbsp;
                                                                                <input type="reset" style="width:50" value="返回" onclick="javascript:history.back(1)">
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