<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.system.util.UserView"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%
  String rootPath = request.getContextPath();
   BusinessDao bussdao = new BusinessDao();
   List allbuss = bussdao.loadAll();  
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />

<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="gb2312" />
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="gb2312"/>
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="gb2312"></script>
<script language="JavaScript" type="text/javascript">
  var delAction = "<%=rootPath%>/db.do?action=delete";
  var listAction = "<%=rootPath%>/db.do?action=list";

  function toAdd()
  {
     mainForm.action = "<%=rootPath%>/grapes.do?action=ready_add";
     mainForm.submit();
  }
  function toDelete()
  {
     mainForm.action = "<%=rootPath%>/grapes.do?action=delete";
     mainForm.submit();
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
        mainForm.action = "<%=rootPath%>/grapes.do?action=add";
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
<script language="JavaScript">

	//��������
	var node="";
	var ipaddress="";
	var operate="";
	/**
	*���ݴ����id��ʾ�Ҽ��˵�
	*/
	function showMenu(id,nodeid,ip)
	{	
		ipaddress=ip;
		node=nodeid;
		//operate=oper;
	    if("" == id)
	    {
	        popMenu(itemMenu,100,"100");
	    }
	    else
	    {
	        popMenu(itemMenu,100,"1111");
	    }
	    event.returnValue=false;
	    event.cancelBubble=true;
	    return false;
	}
	/**
	*��ʾ�����˵�
	*menuDiv:�Ҽ��˵�������
	*width:����ʾ�Ŀ��
	*rowControlString:�п����ַ�����0��ʾ����ʾ��1��ʾ��ʾ���硰101�������ʾ��1��3����ʾ����2�в���ʾ
	*/
	function popMenu(menuDiv,width,rowControlString)
	{
	    //���������˵�
	    var pop=window.createPopup();
	    //���õ����˵�������
	    pop.document.body.innerHTML=menuDiv.innerHTML;
	    var rowObjs=pop.document.body.all[0].rows;
	    //��õ����˵�������
	    var rowCount=rowObjs.length;
	    //alert("rowCount==>"+rowCount+",rowControlString==>"+rowControlString);
	    //ѭ������ÿ�е�����
	    for(var i=0;i<rowObjs.length;i++)
	    {
	        //������ø��в���ʾ����������һ
	        var hide=rowControlString.charAt(i)!='1';
	        if(hide){
	            rowCount--;
	        }
	        //�����Ƿ���ʾ����
	        rowObjs[i].style.display=(hide)?"none":"";
	        //������껬�����ʱ��Ч��
	        rowObjs[i].cells[0].onmouseover=function()
	        {
	            this.style.background="#397DBD";
	            this.style.color="white";
	        }
	        //������껬������ʱ��Ч��
	        rowObjs[i].cells[0].onmouseout=function(){
	            this.style.background="#F1F1F1";
	            this.style.color="black";
	        }
	    }
	    //���β˵��Ĳ˵�
	    pop.document.oncontextmenu=function()
	    {
	            return false; 
	    }
	    //ѡ���Ҽ��˵���һ��󣬲˵�����
	    pop.document.onclick=function()
	    {
	        pop.hide();
	    }
	    //��ʾ�˵�
	    pop.show(event.clientX-1,event.clientY,width,rowCount*25,document.body);
	    return true;
	}
	function detail()
	{
	    location.href="<%=rootPath%>/grapes.do?action=check&id="+node;
	}
	function edit()
	{
		location.href="<%=rootPath%>/grapes.do?action=ready_edit&id="+node;
	}
	function cancelmanage()
	{
		location.href="<%=rootPath%>/grapes.do?action=cancelmanage&id="+node;
	}
	function addmanage()
	{
		location.href="<%=rootPath%>/grapes.do?action=addmanage&id="+node;
	}	
	function clickMenu()
	{
	}
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

}

</script>
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa" onload="initmenu();">

<!-- ��������������Ҫ��ʾ���Ҽ��˵� -->
	<div id="itemMenu" style="display: none";>
	<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
		style="border: thin;font-size: 12px" cellspacing="0">
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.edit()">�޸���Ϣ</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.detail();">������Ϣ</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.cancelmanage()">ȡ������</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.addmanage()">��Ӽ���</td>
		</tr>		
	</table>
	</div>
	<!-- �Ҽ��˵�����-->
<form method="post" name="mainForm">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td width="200" valign=top align=center>
			<%=menuTable%>
		</td>
		<td bgcolor="#ffffff" align="center" valign=top>
				
				<table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%">
					<TBODY>
				<tr>
					<td height="28" align="left" bordercolor="#397DBD" bgcolor="#397DBD" class="txtGlobalBold" colspan=4>&nbsp;&nbsp;<font color=#ffffff>Ӧ�� >> ������� >> ���Grapes</font></td>
				</tr>
						<tr style="background-color: #ECECEC;">						
							<TD nowrap align="right" height="24" width="10%"><font color="red">*&nbsp;</font>����&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="name" maxlength="50" size="20" class="formStyle">
							</TD>
							<TD nowrap align="right" height="24" width="10%"><font color="red">*&nbsp;</font>IP��ַ&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="ipaddress" maxlength="50" size="20" class="formStyle" >
							</TD>
						</tr>
						<tr >						
							<TD nowrap align="right" height="24" width="10%"><font color="red">*&nbsp;</font>��·��&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="supperdir" maxlength="50" size="40" class="formStyle">
							</TD>
							<TD nowrap align="right" height="24" width="10%"><font color="red">*&nbsp;</font>��·��&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="subdir" maxlength="50" size="40" class="formStyle" >
							</TD>
						</tr>
						<tr style="background-color: #ECECEC;">						
							<TD nowrap align="right" height="24" width="10%"><font color="red">*&nbsp;</font>�ļ�����&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="subfilesum" maxlength="50" size="40" class="formStyle">
							</TD>
							<TD nowrap align="right" height="24" width="10%"><font color="red">*&nbsp;</font>�ļ���С��ֵ��KB��&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="filesize" maxlength="50" size="20" class="formStyle" >
							</TD>
						</tr>
						<tr >					
							<TD nowrap align="right" height="24" width="10%">�Ƿ���&nbsp;</TD>				
							<TD nowrap width="40%" colspan=3>&nbsp;
								<select   name="mon_flag"  class="formStyle">
									<option value=0>��</option>
									<option value=1>��</option>
								</select>
							</TD>														
						</tr>
						<tr style="background-color: #ECECEC;"> 
  							<TD nowrap align="right" height="24" width="10%">���Ž�����&nbsp;</TD>       
							<TD nowrap width="40%">&nbsp;
							<input type="text" name="sendmobiles" maxlength="32" size="50" value="">&nbsp;&nbsp; <input type=button value="���ö��Ž�����" onclick="javascript:return CreateWindow('<%=rootPath%>/user.do?action=setmqmobiles')"> 
							</td>
 						</tr>
    						<tr> 
  							<TD nowrap align="right" height="24" width="10%">�ʼ�������&nbsp;</TD>       
							<TD nowrap width="40%">&nbsp;
							<input type="text" name="sendemail" maxlength="32" size="50" value="">&nbsp;&nbsp;<input type=button value="�����ʼ�������" onclick="javascript:return CreateWindow('<%=rootPath%>/user.do?action=setmqemail')"> 
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
						<tr>
							<td nowrap colspan="4" height="1" bgcolor="#8EADD5"></td>
						</tr>
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
</BODY>
</HTML>
