<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.sysset.model.Middleware"%>
<%@page import="com.afunms.sysset.util.MiddlewareView"%>
<%  
   Middleware vo = (Middleware)request.getAttribute("vo");
   boolean faOrCh = vo.getFather_id()==0 ? false:true;
   String rootPath = request.getContextPath();   
   MiddlewareView view = null;
   if( faOrCh ){  view = new MiddlewareView(); }
   
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
<!--nielin add for timeShareConfig at 2010-01-04 end-->


<script language="JavaScript" type="text/javascript">


  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
 Ext.get("process").on("click",function(){
  
     //var chk = checkinput("name","string","����",30,false);
    //var chk1 = checkinput("readcommunity","string","������",30,false);
     //var chk2 = checkinput("writecommunity","string","д����",30,false);
     //var chk3 = checkinput("timeout","integer","��ʱʱ��",30,false);
     //var chk4 = checkinput("trytime","integer","���Դ���",30,false);
     
     //if(chk && chk1 && chk2 && chk3 && chk4 )
     {
     	    
        	mainForm.action = "<%=rootPath%>/middleware.do?action=update";
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
//���Ӳ˵�	
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

    <form method="post" name="mainForm">
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
											                	<td class="add-content-title">ϵͳ���� >> ��Դ���� >> �м���༭</td>
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
																
																	<tr style="background-color: #ECECEC;">						
			<TD nowrap align="right" height="24" width="10%">����&nbsp;</TD>				
			<TD nowrap width="40%">&nbsp;<input type="text" name="name" value="<%=vo.getName()%>" size="25" class="formStyle"><font color="red">&nbsp;*</font></TD>															
			<TD nowrap align="right" height="24">ע��&nbsp;</TD>				
			<TD nowrap width="40%">&nbsp;<input type="text" name="text" value="<%=vo.getText()%>" size="25" class="formStyle"><font color="red">&nbsp;*</font></TD>						
			</tr>
			<tr>
			<TD nowrap align="right" height="24">����&nbsp;</TD>				
            <TD nowrap width="40%">&nbsp;<input type="text" name="table_name" value="<%=vo.getTable_name()%>" size="25" class="formStyle"><font color="red">&nbsp;*</font></TD>
	       	<TD nowrap align="right" height="24" width="10%">category&nbsp;</TD>				
			<TD nowrap width="40%">&nbsp;<input type="text" value="<%=vo.getCategory()%>" name="category" size="25" class="formStyle"></TD>
	        </tr>
			<tr style="background-color: #ECECEC;">
			<TD nowrap align="right" height="24" width="10%">
			<% 
			if( faOrCh )
			{
			%>
			����&nbsp;
			<%
			}
			%>
			</TD>				
			<TD nowrap width="40%">
			<% 
			if( faOrCh )
			{
			%>
			&nbsp;<%=view.getMiddlewareBox(vo.getFather_id())%><font color="red">&nbsp;*</font>
			<%
			}
			%>
			</TD>	
		    <TD nowrap align="right" height="24" width="10%"></TD>				
			<TD nowrap width="40%"></TD>
			</tr>		                 										                      								            							
															<tr>
																<TD nowrap colspan="4" align=center>
																<br><input type="button" value="�޸�" style="width:50" id="process" onclick="#">&nbsp;&nbsp;
																	<input type="reset" style="width:50" value="����" onclick="javascript:history.back(1)">
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