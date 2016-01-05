<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.business.dao.*" %>
<%@page import="com.afunms.business.model.*"%>
<%@page import="com.afunms.topology.model.*"%>
<%@page import="java.util.*"%>
<%@ include file="/include/globe.inc"%>

<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  String _flag = (String)request.getAttribute("flag");
  if(_flag == null){
  	_flag = "0";
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
  
     var chk1 = checkinput("collecttype","integer","采集类型",50,false);
     var chk2 = checkinput("method","string","方法",100,false);
     var chk3 = checkinput("desc","string","描述",50,false);
     var chk4 = checkinput("name","string","名称",50,false);
      if(chk1&&chk2&&chk3)
     {      
            Ext.MessageBox.wait('数据加载中，请稍后.. ');
        	mainForm.action = "<%=rootPath%>/businessNode.do?action=add&flag=<%=_flag%>";
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


</head>
<body id="body" class="body" onload="initmenu();">



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
											                	<td class="add-content-title">资源 >> 视图管理 >> 业务属性节点列表添加</td>
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
				        										
				        												<table border="0" id="table1" cellpadding="0" cellspacing="0"
																	width="100%">
																
																	<tr style="background-color: #ECECEC;">
																	<TD nowrap align="center" height="28" width="6%">名称&nbsp;</TD>
                            <TD width="30%">&nbsp; <input type="text" id="name" name="name" size="15"  class="formStyle"></TD>
																	<TD nowrap align="center" height="28">业务视图&nbsp;</TD>	
								
							<TD nowrap>
								&nbsp;<select size=1 id='managexmlid' name='managexmlid' style='width:108px;'>
								<option value="">--请选择--</option>
								<% 
								
						   		List xmllist = (List)request.getAttribute("xmllist");
						   		if(xmllist == null)xmllist = new ArrayList();
						   for(int i=0;i<xmllist.size();i++)
						   { 
						   ManageXml vo=(ManageXml)xmllist.get(i);
						%>	
								
								<option value="<%=vo.getId() %>"><%=vo.getTopoName() %></option>
								<%} %>
									</select>
								<font color="red">&nbsp;*</font>
							</TD>										
							</TD>	
							<TD nowrap align="center" height="28">采集类型&nbsp;</TD>	
								
							<TD nowrap>
								&nbsp;<select size=1 id='collecttype' name='collecttype' style='width:108px;'>
								<option value="">--请选择--</option>
								<% BusCollectTypeDao dao = new BusCollectTypeDao(); 
						   List arr=dao.loadAll();
						   for(int i=0;i<arr.size();i++)
						   { 
						   BusCollectType vo=(BusCollectType)arr.get(i);
						%>	
								
								<option value="<%=vo.getId() %>"><%=vo.getCollecttype() %></option>
								<%} %>
									</select>
								<font color="red">&nbsp;*</font>
							</TD>	
						   
							</tr>
							<tr bgcolor="#FFFFFF">
							 <TD nowrap align="center" height="28" width="6%">方法&nbsp;</TD>				
							<TD nowrap width="30%">&nbsp;&nbsp;<input type="text" id="method" name="method" size="30" value="" class="formStyle"><font color="red">&nbsp;*</font></TD>
							
						<TD nowrap align="center" height="28" width="6%">是否监视&nbsp;</TD>
							<TD width="20%" nowrap>
								&nbsp;
								<select size=1 name="flag"  id="flag" style='width:54px;'>
								<option value="1" selected>是</option>
			                    <option value="0">否</option>
								</select>
							</TD>
							</tr>
							<tr bgcolor="#ECECEC">
					
							<TD nowrap align="center" height="24">描述&nbsp;</TD>				
							<TD nowrap  colspan="5">&nbsp;<textarea id="desc" name="desc" cols="80" rows="5"></textarea></TD>
						</tr>   										                 										                      								 </TABLE>
						 <TABLE>

									            							
															<tr>
																<TD nowrap colspan="4" align=center bgcolor="#FFFFFF">
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
	</form>
</body>
</HTML>