<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%  
   String rootPath = request.getContextPath();  
   BusinessDao bussdao = new BusinessDao();
   List allbuss = bussdao.loadAll();  
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>

<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="gb2312" />
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="gb2312"/>
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="gb2312"></script>
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<!-- snow add for gatherTime at 2010-5-20 start -->
<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/addTimeConfig.js" charset="gb2312"></script>
<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/jquery-1.4.2.js" charset="gb2312"></script>
<script type="text/javascript">
	$(addTimeConfigRow);//addTimeConfigRow������application/resource/js/addTimeConfig.js�� 
</script>
<!-- snow add for gatherTime at 2010-5-20 end -->

<script language="javascript">
  function toAdd()
  {
     var chk1 = checkinput("ip_address","ip","IP��ַ",15,false);
     var chk2 = checkinput("alias","string","������",30,false);
     var chk3 = checkinput("community","string","��ͬ��",30,false);
     
     if(chk1&&chk2&&chk3)
     {
        msg.style.display="block";
        mainForm.action = "<%=rootPath%>/network.do?action=webadd";
        mainForm.submit();
     }
  }
  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
 Ext.get("process").on("click",function(){
  
     //var chk1 = checkinput("ip_address","ip","����",15,false);
     //var chk2 = checkinput("alias","string","����",30,false);
     //var chk3 = checkinput("community","string","�û���",30,false);
     
     //if(chk1&&chk2&&chk3)
     {
     
        Ext.MessageBox.wait('���ݼ����У����Ժ�.. '); 
        //msg.style.display="block";
        mainForm.action = "<%=rootPath%>/cics.do?action=add";
        mainForm.submit();
     }  
       // mainForm.submit();
 });	
	
});
function CreateWindow(url)
{
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}
</script>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa">
<div id="loading">
	<div class="loading-indicator">
	<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif" width="32" height="32" style="margin-right: 8px;" align="middle" />Loading...</div>
</div>
<div id="loading-mask" style=""></div>
<form name="mainForm" method="post">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td width="200" valign=top align=center >
			<%=menuTable%>
		
		</td>
		<td bgcolor="#cedefa" align="center" valign=top>
		<table width="98%" style="BORDER-COLLAPSE: collapse" borderColor=#397DBD cellPadding=0 rules=none border=1 algin="center">
				<tr>
					<td height="28" align="left" bordercolor="#397DBD" bgcolor="#397DBD" class="txtGlobalBold" colspan=3>&nbsp;&nbsp;Ӧ�� >> ���CICS����</td>
				</tr>
			<tr>
				<td height=300 bgcolor="#FFFFFF" valign="top"><br>
				<table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%">
					<TBODY>
						<tr style="background-color: #ECECEC;">						
							<TD nowrap align="right" height="24" width="10%">��������&nbsp;</TD>				
							<TD nowrap width="40%" colspan=3>&nbsp;
								<input type="text" name="region_name" maxlength="50" size="20" class="formStyle">
							</TD>
						</tr>
						<tr style="background-color: #ECECEC;">						
							<TD nowrap align="right" height="24" width="10%">����&nbsp;</TD>				
							<TD nowrap width="40%" colspan=3>&nbsp;
								<input type="text" name="alias" maxlength="50" size="20" class="formStyle">
							</TD>
						</tr>
						<tr>					
							<TD nowrap align="right" height="24" width="10%">������IP��ַ&nbsp;</TD>				
							<TD nowrap width="40%" colspan=3>&nbsp;
								<input type="text" name="ipaddress" maxlength="50" size="50" class="formStyle" value="">
							</TD>
						</tr>
						<tr style="background-color: #ECECEC;">						
							<TD nowrap align="right" height="24" width="10%">�����˿�&nbsp;</TD>				
							<TD nowrap width="40%" colspan=3>&nbsp;
								<input type="text" name="port_listener" maxlength="50" size="20" class="formStyle">
							</TD>		
						</tr>
						<tr style="background-color: #ECECEC;">						
							<TD nowrap align="right" height="24" width="10%">���ӳ�ʱ(��)&nbsp;</TD>				
							<TD nowrap width="40%" colspan=3>&nbsp;
								<input type="text" name="conn_timeout" maxlength="50" size="20" class="formStyle">
							</TD>		
						</tr>
						<tr>					
							<TD nowrap align="right" height="24" width="10%">����Э��&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<select   name="network_protocol"  class="formStyle">
									<option value="TCP/IP">TCP/IP</option>
									<option value="SNA">SNA</option>
								</select>
							</TD>															
						</tr>
						<tr>					
							<TD nowrap align="right" height="24" width="10%">�Ƿ���&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<select   name="flag"  class="formStyle">
									<option value=0>��</option>
									<option value=1>��</option>
								</select>
							</TD>															
						</tr>
						<tr style="background-color: #ECECEC;"> 
  							<TD nowrap align="right" height="24" width="10%">���Ž�����&nbsp;</TD>       
							<TD nowrap width="40%" colspan=3>&nbsp;
							<input type="text" name="sendmobiles" maxlength="32" size="50" value="">&nbsp;&nbsp;<input type=button value="���ö��Ž�����" onclick="javascript:return CreateWindow('<%=rootPath%>/user.do?action=setCicsMobiles')"> 
							</td>
 						</tr>
 						<!-- snow modify begin ��ӹ�Ӧ��  2010-05-20 -->
						<tr >												
							<TD nowrap align="right" height="24" width="10%">��Ӧ��&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<select size=1 name='supperid' style="width:260px;">
									<option value='0' selected></option>
									<c:forEach items="${allSupper}" var="al">
						      							<option value='${al.su_id }'>${al.su_name }��${al.su_dept}��</option>
						      						</c:forEach>
						      					</select>
						      				</TD>	
						      				<TD nowrap align="right" height="24" width="10%"></TD>				
							<TD nowrap width="40%">&nbsp;
						      				</TD>																					
						</tr>						
						<!-- snow modify end -->
    						<tr> 
  							<TD nowrap align="right" height="24" width="10%">�ʼ�������&nbsp;</TD>       
							<TD nowrap width="40%" colspan=3>&nbsp;
							<input type="text" name="sendemail" maxlength="32" size="50" value="">&nbsp;&nbsp;<input type=button value="�����ʼ�������" onclick="javascript:return CreateWindow('<%=rootPath%>/user.do?action=setCicsEmail&flag=db')">
							</td>
 						</tr>
 						<tr>						
							<TD nowrap align="right" height="24" width="10%">����ҵ��&nbsp;</TD>				
							<TD nowrap width="40%" colspan=3>&nbsp;
								<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none w align=center border=1 algin="center">
                        					<tbody>  
                        									<% if( allbuss.size()>0){
           												for(int i=0;i<allbuss.size();i++){
           													Business buss = (Business)allbuss.get(i);
                        									%>                  										                        								
                    										<tr align="left" valign="center"> 
                    											<td height="28" align="left">&nbsp;<INPUT type="checkbox" class=noborder name=checkbox value="<%=buss.getId()%>" >&nbsp;<%=buss.getName()%></td>
                    										</tr>  
                    										<%
                    											}
                    										}
                    										%>                  										                 										                      								
            							</tbody>
            							</table>
							</TD>																			
						</tr>  						 								
						<tr>
							<td colspan="4" align='center'>
								<div id="msg" style="display:none"><font color="#FF0000">��Ҫһ��ʱ��,���Ժ�...</font></div>
							</td>
						</tr>			

						<!-- snow modify begin (timeConfig div)*/ 2010-05-20 -->
						<tr >
						 	<td nowrap align="right" width="10%" style="height:35px;">��Ϣ�ɼ�ʱ��&nbsp;</td>		
						 	<td nowrap  colspan="3">
						        <div id="formDiv" style="">         
					                <table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none border=1 algin="center" >
				                        <tr>
				                            <td align="left">  
					                            <br>
				                                <table id="timeConfigTable" style="width:60%; padding:0;  background-color:#FFFFFF; position:relative; left:15px;" >
			                                        <tr>
			                                            <td colspan="7" height="50" align="center"> 
			                                                <span id="addTimeConfigRow" style="border: 1px solid black;margin:10px;padding:0px 3px 0px 3px;cursor: hand;">����һ��</span>
			                                            </td>
			                                        </tr>
				                                </table>
				                            </td>
				                        </tr>
					                </table>
					            </div> 
							</td>
						</tr>
						<!-- snow modify end */ 2010-05-20-->
						
						<tr>
							<TD nowrap colspan="4" align=center>
							<br><input type="button" value="�� ��" style="width:50" id="process" onclick="#">&nbsp;&nbsp;
								<input type="reset" style="width:50" value="����" onclick="javascript:history.back(1)">
							</TD>	
						</tr>
					</TBODY>
				</TABLE>				
				</td>
			</tr>			
			</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>