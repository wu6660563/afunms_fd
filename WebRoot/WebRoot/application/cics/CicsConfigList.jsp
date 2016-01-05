<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.application.model.CicsConfig"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.base.Node"%>
<%
  String rootPath = request.getContextPath();
  String alertStr = (String)request.getAttribute("alertStr");
  List list = (List)request.getAttribute("list");

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
<script language="javascript">	
  
  var delAction = "<%=rootPath%>/network.do?action=webdelete";
  var listAction = "<%=rootPath%>/network.do?action=list";
  
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
  
  function doChange()
  {
     if(mainForm.view_type.value==1)
        window.location = "<%=rootPath%>/topology/network/index.jsp";
     else
        window.location = "<%=rootPath%>/topology/network/port.jsp";
  }

  function toFind()
  {
      mainForm.action = "<%=rootPath%>/cics.do?action=ready_find";
      mainForm.submit();
  }
  
  function toAdd()
  {
      mainForm.action = "<%=rootPath%>/cics.do?action=ready_add";
      mainForm.submit();
  }
  
  function toDelete()
  {
     mainForm.action = "<%=rootPath%>/cics.do?action=delete";
     mainForm.submit();
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
	    location.href="<%=rootPath%>/cics.do?action=detail&id="+node;
	}
	function edit()
	{
		location.href="<%=rootPath%>/cics.do?action=ready_edit&id="+node;
	}
	function cancelmanage()
	{
		location.href="<%=rootPath%>/cics.do?action=cancelalert&id="+node;
	}
	function addmanage()
	{
		location.href="<%=rootPath%>/cics.do?action=addalert&id="+node;
	}	
	function clickMenu()
	{
	}
</script>
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa">
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
		<td align="center" valign=top>
			<table width="98%"  cellpadding="0" cellspacing="0" algin="center">
			<tr>
				<td background="<%=rootPath%>/common/images/right_t_02.jpg" width="100%"><table width="100%" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
                    <td class="layout_title">Ӧ�� >> �м������ >> CICS�����б�</td>
                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                  </tr>
              </table>
			  </td>
        						<tr>           
								<td>
								<table width="100%" cellpadding="0" cellspacing="1" >
        			<tr>
					            <td bgcolor="#ECECEC" width="100%" align='right'>
								    <a href="#" onclick="toFind()">���ַ���</a>
									<a href="#" onclick="toDelete()">ɾ��</a>&nbsp;&nbsp;&nbsp;
		  						</td>
									</tr>
        								</table>
										</td>
        						</tr>		
				<tr>
					<td colspan="2">
						<table cellspacing="1" cellpadding="0" width="100%">
	  						<tr class="microsoftLook0" height=28>
      								<td width='10%' align="left"><INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()">���</td>				
      								<th width='15%'>��������</th>
      								<th width='15%'>��������IP��ַ</th>
      								<th width='10%'>����Э��</th>      
      								<th width='10%'>�����˿�</th>
      								<th width='10%'>�Ƿ���</th>
      								<th width='10%'>��ǰ״̬</th>
      								<th width='10%'>Gateway</th>
      								<th width='10%'>����</th>
							</tr>
			<%
    			CicsConfig vo = null;
   				 for(int i=0;i<list.size();i++)
    			{
       				vo = (CicsConfig)list.get(i);
       				Node node = PollingEngine.getInstance().getCicsByID(vo.getId());
       				String alarmmessage = "";
				       if(node != null){
				       		vo.setStatus(1);
				       		//System.out.println(vo.getIpaddress()+"====================="+node.isAlarm());
				       		if(node.isAlarm())vo.setStatus(3);
				       		List alarmlist = node.getAlarmMessage();
				       		if(alarmlist!= null && alarmlist.size()>0){
							for(int k=0;k<alarmlist.size();k++){
								alarmmessage = alarmmessage+alarmlist.get(k).toString();
							}
						}
				       } else {
				           //System.out.println("=====================");
				       }
			%>
   										<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=25>  
    											<td ><INPUT type="checkbox" name=checkbox class=noborder value="<%=vo.getId() %>">
    												<font color='blue'><%=1 + i%></font></td>
    											<td  align='center' ><%=vo.getRegion_name()%></td>
    											<td  align='center' style="cursor:hand"><font color="#397DBD"><%= vo.getIpaddress()%></font></td>
    											<td  align='center'><%=vo.getNetwork_protocol()%></td>
    											<td  align='center'><%=vo.getPort_listener()%></td>
    											<%
												if(vo.getFlag()==0){
											%>
											<td  align='center'>��</td>
											<% 
												//if(vo.getFlag()==1);
												}else{
											%>
											 <td  align='center'>��</td>
											 <%
											 	}
											 %>
											 <td  align='center' ><img src="<%=rootPath%>/resource/<%=NodeHelper.getStatusImage(vo.getStatus())%>" border="0" alt="<%=alarmmessage%>"/></td>
											 <td  align='center' ><%=vo.getGateway()%></td>
											<td height=25>&nbsp;
												<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('2','<%=vo.getId()%>','') alt="�Ҽ�����">
											</td>											 
  										</tr>			
								<%;} %>
									</table>
								</td>
							</tr>	
					 <tr>
              <td background="<%=rootPath%>/common/images/right_b_02.jpg" ><table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="11" /></td>
                    <td></td>
                    <td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="11" /></td>
                  </tr>
              </table></td>
            </tr>
			</table>
		</td>
	</tr>
</table>
</form>
</BODY>
</HTML>
