<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="java.util.List"%>
<%@ include file="/include/globe.inc"%>
<%
  String rootPath = request.getContextPath();
  List list=(List)request.getAttribute("list"); 
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>
<script language="javascript">
	
  function doQuery()
  {  
     if(mainForm.key.value=="")
     {
     	alert("�������ѯ����");
     	return false;
     }
     mainForm.action = "<%=rootPath%>/network.do?action=find";
     mainForm.submit();
  }

  function goBack()
  {
     mainForm.action = "<%=rootPath%>/network.do?action=list&jp=1";
     mainForm.submit();  
  }
  function toEditBid(){
     var bidnum = document.getElementsByName("checkbox").length;
     var k=0;
     for(var p = 0 ; p <bidnum ; p++){
		 if(document.getElementsByName("checkbox")[p].checked ==true){
			 k=k+1;
			 }
	}
     if(k==0){
		 alert ("��ѡ���豸��");
		 }
     else {
		 
		 //alert("ѡ�����豸��Ϊ"+k);
		 mainForm.target="findeditbid";
		 window.open("","findeditbid","toolbar=no,height=400, width= 500, top=200, left= 200,scrollbars=no"+"screenX=0,screenY=0")
		 mainForm.action='<%=rootPath%>/network.do?action=editall';
		 mainForm.submit();
		
		
		 }

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
	    location.href="<%=rootPath%>/detail/dispatcher.jsp?id="+node;
	}
	function edit()
	{
		location.href="<%=rootPath%>/network.do?action=ready_edit&id="+node;
		//window.open('/nms/netutil/ping.jsp?ipaddress='+ipaddress);
	}
	function cancelmanage()
	{
		location.href="<%=rootPath%>/network.do?action=menucancelmanage&id="+node;
		//window.open("<%=rootPath%>/tool/tracerouter.jsp?ipaddress="+ipaddress,"newtracerouter", "height=400, width= 500, top=300, left=100");
		//window.open('/nms/netutil/tracerouter.jsp?ipaddress='+ipaddress);
	}
	function addmanage()
	{
		location.href="<%=rootPath%>/network.do?action=menuaddmanage&id="+node;
		//window.open('<%=rootPath%>/network.do?action=telnet&ipaddress='+ipaddress,'onetelnet', 'height=0, width= 0, top=0, left= 0');
		//window.open('/nms/netutil/tracerouter.jsp?ipaddress='+ipaddress);
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
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#9FB0C4" onload="initmenu();">
<!-- ��������������Ҫ��ʾ���Ҽ��˵� -->
	<div id="itemMenu" style="display: none";>
	<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
		style="border: thin;font-size: 12px" cellspacing="0">
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.edit()">�༭</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.cancelmanage()">ȡ������</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.addmanage()">��ӹ���</td>
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
		<td align="center" valign=top>
			<table width="98%"  cellpadding="0" cellspacing="0" algin="center">
			<tr>
				<td background="<%=rootPath%>/common/images/right_t_02.jpg" width="100%"><table width="100%" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
                    <td class="layout_title"><b>��Դ >> �豸ά�� >> �豸�б�</b></td>
                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                  </tr>
              </table>
			  </td>
			  </tr>
				
				<tr>           
								<td>
								<table width="100%" cellpadding="0" cellspacing="1" >
								<tr>
								<td>
								<table width="100%" cellpadding="0" cellspacing="0" >
								<tr>
								<td bgcolor="#ECECEC" width="50%" align='left'>
				&nbsp;&nbsp;&nbsp;&nbsp;<B>��ѯ:</B>
        <SELECT name="key" style="width=100"> 
          <OPTION value="alias" selected>����</OPTION>
          <OPTION value="ip_address">IP��ַ</OPTION>
          <OPTION value="sys_oid">ϵͳOID</OPTION>          
          <OPTION value="type">�ͺ�</OPTION>
          </SELECT>&nbsp;<b>=</b>&nbsp; 
          <INPUT type="text" name="value" width="15" class="formStyle">
          <INPUT type="button" class="formStyle" value="��ѯ" onclick="doQuery()">
          <INPUT type="button" class="formStyle" value="����" onclick="goBack()">
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" onclick="toEditBid()">Ȩ������</a>
          </td>
										</tr>
								</table>
		  						</td>
									</tr>
								</table>
		  						</td>                       
        						</tr>
		<tr>
			<td colspan="2">
			<table cellspacing="1" cellpadding="0" width="100%" >
	  <tr class="microsoftLook0" height=28>
      <th width='5%'><INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()" class=noborder>���</th></th>				
      <th width='15%'>����</th>
      <th width='15%'>IP��ַ</th>      
      <th width='15%'>��������</th>
      <th width='20%'>�ͺ�</th>
      <th width='20%'>ϵͳOID</th>
      <th width='5%'>�༭</th>
</tr>
<%
  for(int i=0;i<list.size();i++)
  {
     HostNode vo = (HostNode)list.get(i);           
%>
   <tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> class="microsoftLook" height=25>  
    	<td><INPUT type="checkbox" class=noborder name=checkbox value="<%=vo.getId()%>" class=noborder><font color='blue'><%=1 + i%></font></td>
    	<a href="<%=rootPath%>/network.do?action=read&id=<%=vo.getId()%>">
    	<td align='center' style="cursor:hand"><%=vo.getAlias()%></td></a>
    	<td  align='center'><%=vo.getIpAddress()%></td>
    	<td  align='left'><%=vo.getNetMask()%></td>
		<td  align='center'><%=vo.getType()%></td>
		<td align='center'><%=vo.getSysOid()%></td>
        <td ><img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('2','<%=vo.getId()%>','<%=vo.getIpAddress()%>')>
        </td>		
  	</tr>			
<% }%>
			</table>
			</td>
		</tr>
		<tr>
              <td background="<%=rootPath%>/common/images/right_b_02.jpg" ><table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
                    <td></td>
                    <td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
                  </tr>
              </table></td>
            </tr>	
	</table>
</td>
	</tr>
</table>
</BODY>
</HTML>
