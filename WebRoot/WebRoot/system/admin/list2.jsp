<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.system.model.Role"%>


<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.model.Function"%>
<%@page import="com.afunms.system.model.RoleFunction"%>
<%@page import="com.afunms.system.util.CreateRoleFunctionTable"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.system.service.RoleOperationPermissionService"%>
<%@page import="com.afunms.system.service.OperationPermissionService"%>
<%@include file="/include/globe.inc"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="java.util.List"%>
<%
  	String rootPath = request.getContextPath();
  	Role role = (Role)request.getAttribute("role");
  	List<Function> allFunction= (List<Function>)request.getAttribute("allFunction");
  	List<Function> role_Function_list= (List<Function>)request.getAttribute("roleFunction");
	String menuTable = (String)request.getAttribute("menuTable");
	CreateRoleFunctionTable crft = new CreateRoleFunctionTable();
	List<Function> allRootFunction = crft.getAllMenuRoot(allFunction);
	int length = 0;
	for(int i = 0 ; i < allRootFunction.size() ; i++){
		List<Function> secondFunction = crft.getFunctionChild(allRootFunction.get(i),allFunction);
		for(int j = 0 ; j < secondFunction.size() ; j++){
			List<Function> thirdFunction = crft.getFunctionChild(secondFunction.get(j),allFunction);
			if(length< thirdFunction.size()){
				length = thirdFunction.size();
			}
		}
	}
	
	User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
    RoleOperationPermissionService roleOperationPermissionService = RoleOperationPermissionService.getInstance(user, OperationPermissionService.getAdminOperationPermission());
    boolean isUpdateOperation = roleOperationPermissionService.isUpdateOperationPermission();
    String updateOperationDisable = "inline";
    if (!isUpdateOperation) {
        updateOperationDisable = "none";
    }
	
%>
<html>
<head>


<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
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

  function toUpdate()
  {
  	
     mainForm.action = "<%=rootPath%>/admin.do?action=admin_set";
     
     mainForm.submit();
  }

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
											                	<td class="add-content-title">ϵͳ���� >> Ȩ�޹��� >> Ȩ���б�</td>
											                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
											       			</tr>
											        	</table>
				        							</td>
				        						</tr>
				        						
				        						<tr>
				        							<td>
				        								<table id="detail-content-body" class="detail-content-body">
				        								<tr bgcolor="#ffffff">
							<td height="28" align="center" colspan=5><input type="hidden" name="RoleId" value=<%=role.getId()%>><%=role.getRole() %></td>
						      </tr>
				        									<tr>
				        										<td>
				        										
				        												<table width="100%" border="0" cellpadding="0" cellspacing="0">
									<%
										for(int i = 0 ; i < allRootFunction.size(); i++){
										List<Function> secondFunction = crft.getFunctionChild(allRootFunction.get(i),allFunction);
										if (secondFunction == null || secondFunction.size() == 0) {
											
											%>
											
											<tr id="<%=allRootFunction.get(i).getFunc_desc()%>&<%=i%>&tr">
											<td width=6%><input type="checkbox" id="cb<%=allRootFunction.get(i).getFunc_desc() %>" value="<%=allRootFunction.get(i).getId() %>" name="checkbox"><%=allRootFunction.get(i).getCh_desc() %></td>
											<%
											
											for(int m = 0 ; m < length ; m++){
										%>
										<td></td>
										
										<% 
											}
										}
										%>
									<%
										for(int j = 0 ; j < secondFunction.size(); j++){
										List<Function> thirdFunction = crft.getFunctionChild(secondFunction.get(j),allFunction);
									%>
									<tr id="<%=secondFunction.get(j).getFunc_desc()%>&<%=i %>&tr">
									<% 
										if(j==0){
									%>
										<td rowspan="<%=secondFunction.size() %>" width=6%><input type="checkbox" id="cb<%=allRootFunction.get(i).getFunc_desc() %>" value="<%=allRootFunction.get(i).getId() %>" name="checkbox"><%=allRootFunction.get(i).getCh_desc() %></td>
									
									<% 
										}
									%>
										<td width=8%><input type="checkbox" id="cb<%=secondFunction.get(j).getFunc_desc() %>" value="<%=secondFunction.get(j).getId() %>" name="checkbox"><%=secondFunction.get(j).getCh_desc() %></td>
										<%
											for(int k = 0 ; k <thirdFunction.size(); k++){
										%>
										<td><input type="checkbox" id="cb<%=thirdFunction.get(k).getFunc_desc() %>" value="<%=thirdFunction.get(k).getId() %>" name="checkbox"><%=thirdFunction.get(k).getCh_desc() %></td>
										<% 
											} 
											for(int m = 0 ; m < length-thirdFunction.size() ; m++){
										%>
										<td></td>
										
										<% 
											}
										%>
									</tr>
									<% 
										}	
									}
							 		%>
							 <!-- 
							<tr class="microsoftLook0">
										<td rowspan="5"><input type="checkbox" id="0A" value="0A" name="checkbox">��Դ</td>
										<td><input type="checkbox" id="0A0A" value="0A0A" name="checkbox">����</td>
										<td><input type="checkbox" id="0A0A01" value="0A0A01" name="checkbox">�������� </td>
										<td><input type="checkbox" id="0A0A02" value="0A0A02" name="checkbox">����������</td>
															
									</tr>
									<tr class="microsoftLook0">			
										<td><input type="checkbox" id="0A0B" value="0A0B" name="checkbox">�豸ά��</td>
										<td><input type="checkbox" id="0A0B01" value="0A0B01" name="checkbox">�����豸</td>
										<td><input type="checkbox" id="0A0B02" value="0A0B02" name="checkbox">�豸�б�</td>
										<td><input type="checkbox" id="0A0B03" value="0A0B03" name="checkbox">IP/MAC</td>
										<td><input type="checkbox" id="0A0B04" value="0A0B04" name="checkbox">�˿�����</td>
										<td><input type="checkbox" id="0A0B05" value="0A0B05" name="checkbox">��·��Ϣ</td>
										
									</tr>
				    				<tr class="microsoftLook0">  		
					    				<td><input type="checkbox" id="0A0C" value="0A0C" name="checkbox">���ܼ���</td>
					   				 	<td><input type="checkbox" id="0A0C01" value="0A0C01" name="checkbox">���Ӷ���һ����</td>
					    				<td><input type="checkbox" id="0A0C02" value="0A0C02" name="checkbox">ָ��ȫ�ַ�ֵһ����</td>
					    				<td></td>
										<td></td>
										<td></td>
										<td></td>
									</tr>
									<tr class="microsoftLook0">						
										<td><input type="checkbox" id="0A0D" value="0A0D" name="checkbox">��ͼ����</td>
										<td><input type="checkbox" id="0A0D01" value="0A0D01" name="checkbox">��ͼ�༭</td>
										<td><input type="checkbox" id="0A0D02" value="0A0D02" name="checkbox">��ͼչʾ</td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
									</tr>
									<tr class="microsoftLook0">					
										<td ><input type="checkbox" id="0A0E" value="0A0E" name="checkbox">�豸������ù���</td>
										<td><input type="checkbox" id="0A0E01" value="0A0E01" name="checkbox">���ģ��༭</td>
										<td><input type="checkbox" id="0A0E02" value="0A0E02" name="checkbox">�豸���༭</td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
							</tr>
							<tr bgcolor="DEEBF7">
												
										<td rowspan="4"><input type="checkbox" id="0B" value="0B" name="checkbox">�澯</td>
										<td><input type="checkbox" id="0B0A" value="0B0A" name="checkbox">�澯���</td>
										<td><input type="checkbox" id="0B0A01" value="0B0A01" name="checkbox">�澯�б�</td>
										<td><input type="checkbox" id="0B0A02" value="0B0A02" name="checkbox">���ڸ澯���豸</td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
									</tr>
									<tr bgcolor="DEEBF7"> 
										<td><input type="checkbox" id="0B0B" value="0B0B" name="checkbox">�澯ͳ��</td>
										<td><input type="checkbox" id="0B0B01" value="0B0B01" name="checkbox">��ҵ��ֲ�</td>
										<td><input type="checkbox" id="0B0B02" value="0B0B02" name="checkbox">���豸</td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
									</tr>
									<tr bgcolor="DEEBF7">
										<td><input type="checkbox" id="0B0C" value="0B0C" name="checkbox">Trap����</td>
										<td><input type="checkbox" id="0B0C01" value="0B0C01" name="checkbox">���Trap</td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
									</tr>
									<tr bgcolor="DEEBF7">
										<td bgcolor=><input type="checkbox" id="0B0D" value="0B0D" name="checkbox">Syslog����</td>
										<td><input type="checkbox" id="0B0D01" value="0B0D01" name="checkbox">���Syslog</td>
										<td><input type="checkbox" id="0B0D02" value="0B0D02" name="checkbox">���˹���</td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
							</tr>
							<tr class="microsoftLook0" >
										
										<td><input type="checkbox" id="0C" value="0C" name="checkbox">����</td>
										<td ><input type="checkbox" id="0C0A" value="0C0A" name="checkbox">�������</td>
										<td><input type="checkbox" id="0C0A01" value="0C0A01" name="checkbox">�����豸����</td>
										<td><input type="checkbox" id="0C0A02" value="0C0A02" name="checkbox">����������</td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
									</tr>
								
									<tr bgcolor="DEEBF7">						
										<td rowspan="3"><input type="checkbox" id="0D" value="0D" name="checkbox">Ӧ��</td>
										<td><input type="checkbox" id="0D0A" value="0D0A" name="checkbox">���ݿ����</td>
										<td><input type="checkbox" id="0D0A01" value="0D0A01" name="checkbox">���ݿ����͹���</td>
										<td><input type="checkbox" id="0D0A02" value="0D0A02" name="checkbox">���ݿ����</td>
										<td><input type="checkbox" id="0D0A03" value="0D0A03" name="checkbox">Oracle�澯����</td>
										<td><input type="checkbox" id="0D0A04" value="0D0A04" name="checkbox">SQLServer�澯����</td>
										<td><input type="checkbox" id="0D0A05" value="0D0A05" name="checkbox">DB2�澯����</td>
										<td><input type="checkbox" id="0D0A06" value="0D0A06" name="checkbox">Sybase�澯����</td>
									</tr>					
									<tr  bgcolor="DEEBF7">						
										<td><input type="checkbox" id="0D0B" value="0D0B" name="checkbox" >�������</td>
										<td><input type="checkbox" id="0D0B01" value="0D0B01" name="checkbox">FTP�������</td>
										<td><input type="checkbox" id="0D0B02" value="0D0B02" name="checkbox">Email�������</td>
										<td><input type="checkbox" id="0D0B03" value="0D0B03" name="checkbox">WEB���ʷ������</td>
										<td><input type="checkbox" id="0D0B04" value="0D0B04" name="checkbox">Grapes����</td>
										<td><input type="checkbox" id="0D0B05" value="0D0B05" name="checkbox">Radar����</td>
										<td><input type="checkbox" id="0D0B06" value="0D0B06" name="checkbox">Plot����</td>
									</tr>
									<tr bgcolor="DEEBF7">						
										<td><input type="checkbox" id="0D0C" value="0D0C" name="checkbox">�м������</td>
										<td><input type="checkbox" id="0D0C01" value="0D0C01" name="checkbox">MQ����</td>
										<td><input type="checkbox" id="0D0C02" value="0D0C02" name="checkbox">MQ�澯����</td>
										<td><input type="checkbox" id="0D0C03" value="0D0C03" name="checkbox">Domino����</td>
										<td><input type="checkbox" id="0D0C04" value="0D0C04" name="checkbox">WAS����</td>
										<td><input type="checkbox" id="0D0C05" value="0D0C05" name="checkbox">Weblogic����</td>
										<td><input type="checkbox" id="0D0C06" value="0D0C06" name="checkbox">Tomcat����</td>
									</tr>
								
									<tr class="microsoftLook0">
							
										<td rowspan="3" ><input type="checkbox" id="0E" value="0E" name="checkbox">ϵͳ����</td>
										<td><input type="checkbox" id="0E0A" value="0E0A" name="checkbox">��Դ����</td>
										<td><input type="checkbox" id="0E0A01" value="0E0A01" name="checkbox">SNMPģ��</td>
										<td><input type="checkbox" id="0E0A02" value="0E0A02" name="checkbox">�豸����</td>
										<td><input type="checkbox" id="0E0A03" value="0E0A03" name="checkbox">�豸�ͺ�</td>
										<td><input type="checkbox" id="0E0A04" value="0E0A04" name="checkbox">����</td>
										<td></td>
										<td></td>
									</tr>
									<tr class="microsoftLook0">						
										<td><input type="checkbox" id="0E0B" value="0E0B" name="checkbox">�û�����</td>
										<td><input type="checkbox" id="0E0B01" value="0E0B01" name="checkbox">�û�</td>
										<td><input type="checkbox" id="0E0B02" value="0E0B02" name="checkbox">��ɫ</td>
										<td><input type="checkbox" id="0E0B03" value="0E0B03" name="checkbox">����</td>
										<td><input type="checkbox" id="0E0B04" value="0E0B04" name="checkbox">ְλ</td>
										<td><input type="checkbox" id="0E0B05" value="0E0B05" name="checkbox">Ȩ������</td>
										<td><input type="checkbox" id="0E0B06" value="0E0B06" name="checkbox">�޸�����</td>
										
									</tr>
									<tr class="microsoftLook0">						
										<td><input type="checkbox" id="0E0C" value="0E0C" name="checkbox">ϵͳ����</td>
										<td><input type="checkbox" id="0E0C01" value="0E0C01" name="checkbox">ҵ�����</td>
										<td><input type="checkbox" id="0E0C02" value="0E0C02" name="checkbox">������־</td>
										<td><input type="checkbox" id="0E0C03" value="0E0C03" name="checkbox">�澯����</td>
										<td><input type="checkbox" id="0E0C04" value="0E0C04" name="checkbox">TFTP����</td>
										<td></td>
										<td></td>
									</tr>
								 -->
							
								</table>				
				        										</td>
				        									</tr>
				        									<tr>
							                   <td bgcolor="#ffffff" style="display: <%=updateOperationDisable%>" colspan="5" align=center><input id="write" type="button" class="button" value="ȷ��" name="B2" onclick="toUpdate()"></td>
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
<script>

/*
	�˷�����ҪΪʵ�ָ�ѡ�������
*/


function unionCheckbox(){
 var checkboxes = document.getElementsByName("checkbox");
 
 var allcheckbox="checkbox";
 
  var changeChecked=function(cbox,chk){
     cbox.checked = chk;
  };
 //�ù��ı丸�ڵ� �������ı��ӽڵ�
  var changeFather=function(checkboxFather,chk){
   
	if(checkboxFather == 0)return;
	var checkboxFatherId=checkboxFather.id;
  	var idpattern=new RegExp("^"+checkboxFatherId+"[0,1].$");
    for(var i=0,l=checkboxes.length; i<l; i++){   
    var checkboxChild = checkboxes[i];     
    var checkboxChildId=checkboxChild.id;
    if(idpattern.test(checkboxChildId)){
       changeChecked(checkboxChild,chk);
       changeFather(checkboxChild,chk);
     }     
   }
  };
 var boxonchangedown=function(checkbox){
   checkbox.onclick=function()
   {  
  	  var chk = checkbox.checked;
  	  changeFather(checkbox,chk);
  	  changeChild(checkbox,chk);
  	     
   }   
 }
 //ͨ���ӽڵ��������ı丸�ڵ�
 var changeChild=function(checkboxChild,chk){
   var checkboxChildId=checkboxChild.id;
   var checkboxChildIdlength=checkboxChildId.length;
   if(checkboxChildId.length<=0){
      return;
   }
   var FatherId=checkboxChildId.substring(0,checkboxChildIdlength-2);
   var FatherIdpattern=new RegExp("^"+FatherId+"[0,1].$");
   var checkboxFather=document.getElementById(FatherId);
   if(chk){
     changeChecked(checkboxFather,true);
   }else{
     var flagmark=false;
     for(var m=0,n=checkboxes.length; m<n; m++){   
     var checkbox = checkboxes[m];   
     var checkboxId=checkbox.id;
     if(FatherIdpattern.test(checkboxId)){
        if(checkbox.checked){
          flagmark=true;
          break;
        }
     }     
     }
     if(!flagmark){

       changeChecked(checkboxFather,false);
     }
   }
   changeChild(checkboxFather,checkboxFather.checked);
 }

 for(var j=0,k=checkboxes.length; j<k; j++){   
     var checkbox = checkboxes[j];
     	boxonchangedown(checkbox);
 }
   
}   
  
unionCheckbox();    

</script>

<script>
/*
	�˷�����ҪΪ����ɫ����Ȩ�޵ĸ�ѡ��Ĭ��Ϊѡ��״̬
*/

function checkedbox(){
	
	var allcheckbox = document.getElementsByName("checkbox");
	for(var i = 0 ,j = allcheckbox.length; i < j ; i++){
		checkbox = allcheckbox[i];
		<%for(int m = 0 ; m < role_Function_list.size(); m ++){
			%>
			if(checkbox.id=="cb<%=role_Function_list.get(m).getFunc_desc()%>"){
				
				checkbox.checked = true;
			}
			<%
		}%>
	}
}

checkedbox();


</script>

<script>
/*
	���ݲ�ͬ��rootFunction ��tr �趨class
*/
	function trClass(){
	
		var all_tr = document.getElementsByTagName("tr");
		var tr_pattern = new RegExp("&tr$")
		for(var i=0,length = all_tr.length;i<length;i++){
			tr = all_tr[i];
			
			if(tr_pattern.test(tr.id)){
				var tr_id_array = tr.id.split("&");
				if(tr_id_array[1]%2==0){
					tr.className ="microsoftLook0";
				}else{
					tr.bgColor="DEEBF7";
					
				}
			}
		}
	}
	
trClass();


</script>
</HTML>