<%@page language="java" contentType="text/html;charset=gb2312" %>



<%@page import="java.util.*"%>


<% 

	String rootPath = request.getContextPath(); 

%>

<%
String menuTable = (String)request.getAttribute("menuTable");
String common="<img src='/afunms/img/common.gif'>";				
String serious="<img src='/afunms/img/serious.gif'>";	
String urgency="<img src='/afunms/img/urgency.gif'>";
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js"
			charset="gb2312"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js"
			charset="utf-8"></script>
<script type="text/javascript">
function test(){
����$table=$("#advance_tab tr select");
����$table2=$("#advance_tab tr input:text");
alert($table2.get(0).value);
var arrayObj = new Array(); 
for(var i=0;i<$table2.length;i++){
arrayObj[i]=$table2.get(i).value;
}
mainForm.action = "<%=rootPath%>/configRule.do?action=save&arr="+arrayObj;
mainForm.submit();
}

function deltr(index)
{
����������$table=$("#advance_tab tr");
����������{
������������$("tr[id='advance"+index+"']").remove();��

      ��
����������}��
}
//append��ÿ��ƥ���Ԫ����׷������

$(document).ready(function()
{
������$("#add_but").click(function()
        {
������������var $table=$("#advance_tab tr");
������������var len=$table.length;
������������$("#advance_tab").append("<tr id=advance"+(len)+"><td align='center'><select   id='advance_relation'"+(len)+"  class='formStyle'><option value='0'>��</option><option value='1'>��</option></select>&nbsp;&nbsp;&nbsp;<select   id='advance_config'"+(len)+"  class='formStyle'><option value='1'>����</option><option value='0'>������</option></select>&nbsp;<input id='advance_value'"+(len)+" type='text' maxlength='200' size='50' class='formStyle'>&nbsp;<input id='advance_cut'"+(len)+"  type='button' maxlength='200' size='50' class='formStyle' value=' - 'onclick='deltr("+(len)+")'></td></tr>");����
           
����������})��
})


function customDeltr(index)
{
����������$table=$("#custom_tab tr");
����������{
������������$("tr[id='custom"+index+"']").remove();��

����������}��
}
//append��ÿ��ƥ���Ԫ����׷������

$(document).ready(function()
{
������$("#custom_add").click(function()
        {
������������var $table=$("#custom_tab tr");
������������var len=$table.length;
������������$("#custom_tab").append("<tr id=custom"+(len)+"><td align='center'><select   id='custom_relation'"+(len)+"  class='formStyle'><option value='0'>��</option><option value='1'>��</option></select>&nbsp;&nbsp;&nbsp;<select   id='custom_config'"+(len)+"  class='formStyle'><option value='1'>����</option><option value='0'>������</option></select>&nbsp;<input id='custom_value'"+(len)+" type='text' maxlength='200' size='50' class='formStyle'>&nbsp;<input id='custom_cut'"+(len)+"  type='button' maxlength='200' size='50' class='formStyle' value=' - 'onclick='customDeltr("+(len)+")'></td></tr>");����
           
����������})��
})
</script>


<script language="JavaScript" type="text/javascript">
function getSimpleValue(){
 var arrayObj = new Array();
 return arrayObj; 
 }
 
 function getAdacnceValue(){
  
 }
 
function save(){
  
   var arrayObj1= new Array(); 
   var arrayObj2 = new Array(); 
  
  var a=$("input[name='standard']:checked").val();
  if(a==1){
   $select=$("#advance_tab tr select");
����$text=$("#advance_tab tr input:text");
   for(var i=0;i<$select.length;i++){
   arrayObj1[i]=$select.get(i).value;
       }
   for(var i=0;i<$text.length;i++){
   arrayObj2[i]=$text.get(i).value;
   }
  
  }else if(a==2){
      $selectc=$("#custom_tab tr select");
����$textc=$("#custom_tab tr input:text");
  for(var i=0;i<$selectc.length;i++){
   arrayObj1[i]=$selectc.get(i).value;
       }
   for(var i=0;i<$textc.length;i++){
   arrayObj2[i]=$textc.get(i).value;
   }
  }
$('#selVal').val(arrayObj1);
 $('#textVal').val(arrayObj2);
 mainForm.action = "<%=rootPath%>/configRule.do?action=save";
 mainForm.submit();
 //window.close();
// parent.opener.location.href="<%=rootPath%>/configRule.do?action=ruleDetailList";
 
}

function CreateWindow(url)
{
	
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}    

function simpleStandard(){
document.getElementById('simple').style.display = "block";
document.getElementById('advance').style.display = "none";
document.getElementById('custom').style.display = "none";
}
function advanceStandard(){
document.getElementById('simple').style.display = "none";
document.getElementById('advance').style.display = "block";
document.getElementById('custom').style.display = "none";
}
function customStandard(){
document.getElementById('simple').style.display = "none";
document.getElementById('advance').style.display = "none";
document.getElementById('custom').style.display = "block";
}

</script>





</head>
<body id="body"  bgcolor="#cedefa">


	<!-- �Ҽ��˵�����-->
   <form name="mainForm" method="post">
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
										<td >
										<input type="hidden" name="selVal" id='selVal' value=""/>
                                         <input type="hidden" name="textVal" id='textVal' value=""/>
											<table id="add-content" class="add-content" border=1>
												<tr>
													<td>
														<table id="add-content-header" class="add-content-header" >
										                	<tr>
											                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
											                	<td class="add-content-title">&nbsp; �Զ���>> �����ļ�����>> ���Թ���>> ����>> �½�����
											                	</td>
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
																			<tr>
																			    <td nowrap align="left" height="24" width="30%">��������</td>
																			    <td width="70%" align="left">��&nbsp;&nbsp;<input type="text" name="rule_name" maxlength="100" size="40" class="formStyle"></td>
													                  		</tr>
																			<tr> 
													  						    <td nowrap align="left" height="24" width="30%">������</td>
																			    <td width="70%" align="left">��&nbsp;&nbsp;<input type="text" name="des" maxlength="300" size="80" class="formStyle"></td>
																			</tr>
																			<tr> 
													  						    <td nowrap align="left" height="24" width="30%">ѡ���׼��</td>
																			    <td width="70%" align="left">��&nbsp;&nbsp;
																			        <input type="radio" name="standard" onclick="simpleStandard()" value="0" checked>�򵥱�׼&nbsp;&nbsp;
																			        <input type="radio" name="standard" onclick="advanceStandard()" value="1">�߼���׼&nbsp;&nbsp;
																			        <input type="radio" name="standard" onclick="customStandard()"  value="2">�߼��Զ����׼&nbsp;&nbsp;
																			   </td>
																			</tr>
																			<tr>
																			    <td colspan="2">
																			     <hr/>
																			        <div id="simple" style="display:block;"> 
																			             <table>
																			            
																			                    <tr>
																			                         <td align=left ><font size="2" color="red">�򵥱�׼</font></td>
																			                         <td></td>
																			                    </tr>
																			                   <tr>
																			                   <td colspan=2>&nbsp;</td>
																			                   </tr>
																			                    <tr>
																			                         <td nowrap align="left" height="24" width="28%">�����ļ�</td>
																			                         <td width="72%" align="left">��&nbsp;&nbsp;
																			                             <select   name="simple_config"  class="formStyle">
																			                                     <option value="0">����������</option>
																			                                     <option value="1">�������κ���</option>
																			                                     <option value="2">Ӧ�ð�������</option>
																			                                     <option value="3">��Ӧ�ð�������</option>
																			                             </select>
                                                                                                     </td>
																			                        
																			                    </tr>
																			                    <tr>
																			                        <td nowrap align="left" height="24" width="28%">&nbsp;</td>
																			                        <td width="72%">&nbsp;&nbsp;&nbsp;&nbsp;<textarea name="content" id="content" rows="8" cols="77"></textarea></td>
																			                    </tr>
																			             </table>
																			        </div>
																			        <div id="advance" style="display:none;"> 
																			             <table>
																			                    <tr>
																			                         <td align=left ><font size="2" color="red">�߼���׼</font></td>
																			                         <td></td>
																			                    </tr>
																			                   <tr>
																			                         <td colspan=2>&nbsp;</td>
																			                   </tr>
																			                   <tr>
																			                         
																			                         <td width="100%" align="left" colspan=2>
																			                         <div style="height:100px;overFlow:auto;" align=center>�����ļ���&nbsp;&nbsp;
                                                                                                         <select   name="advance_config"  class="formStyle">
																			                                     <option value="1">����</option>
																			                                     <option value="0">������</option>
																			                             </select>
																			                             <input name="advance_value" type="text" maxlength="200" size="50" class="formStyle">
																			                             <input name="add_but" id="add_but" type="button" maxlength="200" size="50" class="formStyle" value=" + ">&nbsp;&nbsp;&nbsp;&nbsp;
                                                                                                      <table id="advance_tab"></table>
                                                                                                     </div>
                                                                                                    
                                                                                                     </td>
																			                        
																			                    </tr>
																			                    
																			             </table>
																			        </div>
																			        <div id="custom" style="display:none;"> 
																			             <table>
																			                    <tr>
																			                         <td align=left ><font size="2" color="red">�߼��Զ����׼</font></td>
																			                         <td>&nbsp;</td>
																			                    </tr>
																			                    <tr>
																			                         <td colspan=2>&nbsp;</td>
																			                    </tr>
																			                    <tr>
																			                        <td nowrap align="left" height="24" width="30%">������ʼ��</td>
																			                        <td width="70%" align="left">��&nbsp;&nbsp;<input type="text" name="begin" maxlength="100" size="40" class="formStyle"></td>
													                  		                    </tr>
																			                    <tr> 
													  						                        <td nowrap align="left" height="24" width="30%">���ý�����</td>
																			                        <td width="70%" align="left">��&nbsp;&nbsp;<input type="text" name="end" maxlength="100" size="40" class="formStyle"></td>
																			                    </tr>
																			                    <tr> 
													  						                        <td nowrap align="left" height="24" width="30%">���ӵı�׼��</td>
																			                        <td width="70%" align="left">��&nbsp;&nbsp;
																			                        <select   name="isExtraContain"  class="formStyle">
																			                                     <option value="-1">��</option>
																			                                     <option value="1">����</option>
																			                                     <option value="0">������</option>
																			                         </select>
																			                        <input type="text" name="extra" maxlength="100" size="50" class="formStyle">
																			                        </td>
																			                    </tr>
																			                    <tr>
																			                         
																			                         <td width="100%" align="left" colspan=2>
																			                             <div style="height:100px;overFlow:auto;" align=center>
                                                                                                             �����ļ���&nbsp;&nbsp;<select   name="custom_config"  class="formStyle">
																			                                     <option value="1">����</option>
																			                                     <option value="0">������</option>
																			                                 </select>
																			                                     <input type="text" name="custom_value" id="custom_value" maxlength="100" size="50" class="formStyle">
																			                                     <input name="custom_add" id="custom_add" type="button" maxlength="200" size="50" class="formStyle" value=" + ">&nbsp;&nbsp;
																			                                   <!--    <input name="custom_cut" id="custom_cut" type="button" maxlength="200" size="50" class="formStyle" value=" - ">--> 
                                                                                                                 <table id="custom_tab"></table>
                                                                                                        </div>
                                                                                                     </td>
																			                        
																			                    </tr>
																			                    
																			             </table>
																			        </div>
																			        <hr/>
																			    </td>
																			</tr>
																			<tr>
																			    <td nowrap align="left" height="24" width="30%">Υ��������</td>
																			    <td width="70%" align="left">��&nbsp;&nbsp;
																			        <input type="radio" name="level" value="0" checked><%=common %>��ͨ&nbsp;&nbsp;
																			        <input type="radio" name="level" value="1"><%=serious %>��Ҫ&nbsp;&nbsp;
																			        <input type="radio" name="level" value="2"><%=urgency %>����&nbsp;&nbsp;
																			   </td>
													                  		</tr>
																			<tr>
																			    <td nowrap align="left" height="24" width="30%">��������</td>
																			    <td width="70%" align="left">��&nbsp;&nbsp;<input type="text" name="add_des" maxlength="100" size="70" class="formStyle"></td>
													                  		</tr>
																			<tr>
																				<TD nowrap colspan="4" align=center colspan=2>
																				<br><input type="button" value="�� ��" style="width:50" id="process2" onclick="save()">&nbsp;&nbsp;
																					
																					<input type="reset" style="width:50" value="�� ��" onclick="javascript:history.back(1)">
																				</TD>	
																			</tr>	
																		</TABLE>
										 							
										 							
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