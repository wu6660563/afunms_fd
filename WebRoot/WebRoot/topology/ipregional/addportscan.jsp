<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.config.dao.DistrictDao"%>
<%@page import="com.afunms.config.model.DistrictConfig"%>



<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  String ipaddress = (String)request.getAttribute("ipaddress");
  
  
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
  
     var chk = checkinput("ipaddress","string","IP ��ַ����Ϊ��",17,false);
     var chk1 = checkinput("startport","string","��ʼ�˿ڲ���Ϊ��",500,false);
     var chk2 = checkinput("timeout","string","��ʱ����Ϊ��",500,false);
      if(chk && chk1 && chk2)
     {      
            Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');
        	mainForm.action = "<%=rootPath%>/ipDistrictMatch.do?action=addPortScan";
        	mainForm.submit();
        	window.close();
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
<style>
<!--
body{
background-image: url(${pageContext.request.contextPath}/common/images/menubg_report.jpg);
TEXT-ALIGN: center; 
}
-->
</style>

</head>
<body id="body" style="padding:5px;" class="body" onload="initmenu();">



	<form id="mainForm" method="post" name="mainForm">
		
		<table>
			<tr>
				<td class="td-container-main">
					<table>
						<tr>
							<td class="td-container-main-add">
								<table >
									<tr>
										<td>
											<table>
												<tr>
													<td>
														<table id="add-content-header" class="add-content-header">
										                	<tr>
											                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
											                	<td class="add-content-title">IP ���ι��� &gt;&gt; IP �˿�ɨ�� &gt;&gt; ���</td>
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
				        														<td>
				        															<table>
																						<tr style="background-color: #ECECEC;">						
																							<td align="right" height="24" width="10%">IP ��ַ:&nbsp;</td>				
																							<td width="40%">&nbsp;<input type="text" id="ipaddress" readonly="readonly" value="<%=ipaddress%>" name="ipaddress"><font color="red">&nbsp;�˿�����IP ��ַ*</font>
																							</td>
																						</tr>														
																						<tr style="background-color: #ECECEC;">						
																							<td align="right" height="24" width="10%">�˿�����:&nbsp;</td>				
																							<td width="40%">&nbsp;<input type="text" id="portName" name="portName"><font color="red">&nbsp;ѡ�� �粻�� ��Ϊ δ����</font>
																							</td>
																						</tr>
																						<tr style="background-color: #ECECEC;">						
																							<td align="right" height="24" width="10%">�˿�����:&nbsp;</td>				
																							<td width="40%">&nbsp;<input type="text" id="description" name="description"><font color="red">&nbsp;ѡ�� �粻�� ��Ϊ δ����</font>
																							</td>
																						</tr>
																						<tr style="background-color: #ECECEC;">						
																							<td align="right" height="24" width="10%">��ʼ�˿�:&nbsp;</td>				
																							<td width="40%">&nbsp;<input type="text" id="startport" name="startport"><font color="red">&nbsp;���� ��������ֹ�˿� ��ֻɨ��˶˿�*</font>
																							</td>
																						</tr>
																						<tr style="background-color: #ECECEC;">						
																							<td align="right" height="24" width="10%">��ֹ�˿�:&nbsp;</td>				
																							<td width="40%">&nbsp;<input type="text" id="endport" name="endport"><font color="red">&nbsp;ѡ�� ���������ʼ�˿� ���ɲ���</font>
																							</td>
																						</tr>
																						<tr style="background-color: #ECECEC;">						
																							<td align="right" height="24" width="10%">�˿�����:&nbsp;</td>				
																							<td width="40%">&nbsp;<input type="text" id="type" name="type"><font color="red">&nbsp;ѡ�� ���� UDP �� TCP �ɲ���</font>
																							</td>
																						</tr>
																						<tr style="background-color: #ECECEC;">						
																							<td align="right" height="24" width="10%">��ʱʱ��:&nbsp;</td>				
																							<td width="40%">&nbsp;<input type="text" id="timeout" name="timeout"><font color="red">&nbsp;���� ���Զ˿��ʱ�� ��λ������*</font>
																							</td>
																						</tr>
			                                           
									            							
															<tr>
																<TD nowrap colspan="4" align=center>
																<br><input type="button" value="�� ��" style="width:50" id="process" onclick="#">&nbsp;&nbsp;
																	<input type="reset" style="width:50" value="�� ��" onclick="javascript:window.close()">
																</TD>	
															</tr>	
							
						                        </TABLE>		
				        														</td>
				        													<tr>
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