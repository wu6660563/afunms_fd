<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@ include file="/include/globe.inc"%>

<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.system.util.UserView"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.polling.base.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.config.model.Procs"%>
<%
  String rootPath = request.getContextPath();
  List<FTPConfig> userFTPConfigList = (List<FTPConfig>)request.getAttribute("ftplist");
  List<EmailMonitorConfig> userEmailMonitorConfigList = (List<EmailMonitorConfig>)request.getAttribute("emaillist");
  List prolist = (List)request.getAttribute("prolist");//����
  List weblist = (List)request.getAttribute("weblist");//web
  List portlist = (List)request.getAttribute("portlist");//�˿�
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<meta http-equiv="Page-Enter" content="revealTrans(duration=x, transition=y)">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script language="JavaScript">

	//��������
	var node="";
	var ipaddress="";
	var operate="";
	/**
	*���ݴ����id��ʾ�Ҽ��˵�
	*/
	function showMenu(ser,id,nodeid,ip)
	{	
		
		if(ser == "ftp"){
			document.getElementById('itemMenu').innerHTML="<table border='1' width='100%' height='100%' bgcolor='#F1F1F1'"+
		"style='border: thin;font-size: 12px' cellspacing='0'>"+
		"<tr>"+
			"<td style='cursor: default; border: outset 1;' align='center'"+
				"onclick='parent.ftpedit()'>�޸���Ϣ</td>"+
		"</tr>"+
		"<tr>"+
			"<td style='cursor: default; border: outset 1;' align='center'"+
				"onclick='parent.ftpdetail();'>������Ϣ</td>"+
		"</tr>"+
		"<tr>"+
			"<td style='cursor: default; border: outset 1;' align='center'"+
				"onclick='parent.ftpcancelmanage()'>ȡ������</td>"+
		"</tr>"+
		"<tr>"+
			"<td style='cursor: default; border: outset 1;' align='center'"+
				"onclick='parent.ftpaddmanage()'>��Ӽ���</td>"+
		"</tr>"+	
	"</table>";
		}else if(ser == "email"){
		document.getElementById('itemMenu').innerHTML="<table border='1' width='100%' height='100%' bgcolor='#F1F1F1'"+
			"style='border: thin;font-size: 12px' cellspacing='0'>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.emailedit()'>�޸���Ϣ</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.emaildetail();'>������Ϣ</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.emailcancelmanage()'>ȡ������</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.emailaddmanage()'>��Ӽ���</td>"+
			"</tr>"+
		"</table>";
		}else if(ser == "web"){
			document.getElementById('itemMenu').innerHTML="<table border='1' width='100%' height='100%' bgcolor='#F1F1F1'"+
			"style='border: thin;font-size: 12px' cellspacing='0'>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.webedit()'>�޸���Ϣ</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.webdetail();'>������Ϣ</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.webcancelmanage()'>ȡ������</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.webaddmanage()'>��Ӽ���</td>"+
			"</tr>"+
		"</table>";
		}else if(ser == "port"){
		document.getElementById('itemMenu').innerHTML="<table border='1' width='100%' height='100%' bgcolor='#F1F1F1'"+
			"style='border: thin;font-size: 12px' cellspacing='0'>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.portedit()'>�޸���Ϣ</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.portdetail();'>������Ϣ</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.portcancelmanage()'>ȡ������</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.portaddmanage()'>��Ӽ���</td>"+
			"</tr>"+
		"</table>";
		}
		
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
	function ftpdetail()
	{
	    location.href="<%=rootPath%>/FTP.do?action=detail&id="+node;
	}
	function ftpedit()
	{
		location.href="<%=rootPath%>/FTP.do?action=ready_edit&id="+node;
	}
	function ftpcancelmanage()
	{
		location.href="<%=rootPath%>/FTP.do?action=changeMonflag&value=0&id="+node;
	}
	function ftpaddmanage()
	{
		location.href="<%=rootPath%>/FTP.do?action=changeMonflag&value=1&id="+node;
	}	
	
	function toReady_edit(ids)
  	{
	 mainForm.action = "<%=rootPath%>/process.do?action=ready_edit&id="+ids;
     mainForm.submit();
  	}
  
  
  	function emaildetail()
	{
	    location.href="<%=rootPath%>/mail.do?action=detail&id="+node;
	}
	function emailedit()
	{
		location.href="<%=rootPath%>/mail.do?action=ready_edit&id="+node;
	} 
	function emailcancelmanage()
	{
		location.href="<%=rootPath%>/mail.do?action=changeMonflag&value=0&id="+node;
	}
	function emailaddmanage()
	{
		location.href="<%=rootPath%>/mail.do?action=changeMonflag&value=1&id="+node;
	}
	
	
	function webdetail()
	{
	    location.href="<%=rootPath%>/web.do?action=detail&id="+node;
	}
	function webedit()
	{
		location.href="<%=rootPath%>/web.do?action=ready_edit&id="+node;
	}
	function webcancelmanage()
	{
		location.href="<%=rootPath%>/web.do?action=cancelalert&id="+node;
	}
	function webaddmanage()
	{
		location.href="<%=rootPath%>/web.do?action=addalert&id="+node;
	}
	
	function portdetail()
	{
	    location.href="<%=rootPath%>/pstype.do?action=detail&id="+node;
	}
	function portedit()
	{
		location.href="<%=rootPath%>/pstype.do?action=ready_edit&id="+node;
	}
	function portcancelmanage()
	{
		location.href="<%=rootPath%>/pstype.do?action=changeMonflag&value=0&id="+node;
	}
    function portaddmanage()
	{
		location.href="<%=rootPath%>/pstype.do?action=changeMonflag&value=1&id="+node;
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
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa"  onload="initmenu();">
<div id="itemMenu" style="display: none";>
	</div>
<form name="mainForm" method="post">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td width="200" valign=top align=center>
			<%=menuTable%>
		
		</td>
		<td align="center" valign=top bgcolor="#ababab">
			<table width="98%"  cellpadding="0" cellspacing="0" algin="center">
				<tr>
					<td background="<%=rootPath%>/common/images/right_t_02.jpg" width="100%">
						<table width="100%" cellspacing="0" cellpadding="0">
		                  <tr>
		                    <td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
		                    <td class="layout_title"></td>
		                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
		                  </tr>
		              </table>
				  	</td>
				 </tr>				

		
		
		
		    <tr>
					<td>
						<table cellspacing="1" cellpadding="0" width="100%">
							<tr>
								<td height="28" align="left" bordercolor="#ffffff" bgcolor="#ffffff" class="txtGlobalBold" colspan="7">&nbsp;&nbsp;<font color="#397DBD">FTP����</font></td>
							</tr>
							
								<tr class="microsoftLook0" height=28 align="center">
			    					<th width='5%' align="center" class="report-data-body-title"><font color="#397DBD">���</font></th>
			      					<th width='20%' align="center" class="report-data-body-title"><font color="#397DBD">����</font></th>
			      					<th width='20%' align="center" class="report-data-body-title"><font color="#397DBD">IP��ַ</font></th>	
			      					<th width='20%' align="center" class="report-data-body-title"><font color="#397DBD">�ļ�����</font></th> 
			      					<th width='10%' align="center" class="report-data-body-title"><font color="#397DBD">��ǰ״̬</font></th>    
			      					<th width='10%' align="center" class="report-data-body-title"><font color="#397DBD">�Ƿ���</font></th>
			    					<th width='10%' align="center" class="report-data-body-title""><font color="#397DBD">����</font></th>
			</tr>
			<%
			if(userFTPConfigList.size() > 0){
			    for(int i=0;i<userFTPConfigList.size();i++)
			    {
			       FTPConfig vo = userFTPConfigList.get(i);
			       				int status = 0;
			       				Node node = (Node)PollingEngine.getInstance().getFtpByID(vo.getId());
			       				String alarmmessage = "";
			       				if(node != null){
			       					if(node.isAlarm())status = 3;
			       					List alarmlist = node.getAlarmMessage();
			       					if(alarmlist!= null && alarmlist.size()>0){
									for(int k=0;k<alarmlist.size();k++){
										alarmmessage = alarmmessage+alarmlist.get(k).toString();
									}
								}
			       				}       
			       
			%>
			       <tr bgcolor="FFFFFF" class="microsoftLook" height="25">
			    	<td height=25  class="report-data-body-list">&nbsp;<font color='blue'><%=1 + i%></font></td>
					<td height=25 class="report-data-body-list">&nbsp;<%=vo.getName()%></td> 
					<td height=25 class="report-data-body-list">&nbsp;<%=vo.getIpaddress()%></td> 
					<td height=25 class="report-data-body-list">&nbsp;<%=vo.getFilename()%></td> 
					<td align='center' class="report-data-body-list"><img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(status)%>" border="0" alt=<%=alarmmessage%>></td>
					<td height=25 class="report-data-body-list">
						<%
							if(vo.getMonflag() == 0){
						%>
						&nbsp;δ����
						<%
							}else{
						%>	
						&nbsp;�Ѽ���
						<%
							}
						%>	
					</td>	
					<td height=25 class="report-data-body-list">&nbsp;
					<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('ftp','2','<%=vo.getId()%>','<%=vo.getName()%>')>
					</td>
			  	</tr>
			<%	}
			}
			
			%>					  				
									
						</table>
					</td>
				</tr>
				
				
				
				
				<tr>
					<td>
						<table cellspacing="1" cellpadding="0" width="100%">
							<tr>
								<td height="28" align="left" bordercolor="#ffffff" bgcolor="#ffffff" class="txtGlobalBold" colspan="8">&nbsp;&nbsp;<font color="#397DBD">Email����</font></td>
							</tr>
								<tr class="microsoftLook0" height=28 align="center">
      								<th width='5%' align="center" class="report-data-body-title"><font color="#397DBD">���</font></th>				
      								<th width='20%' align="center" class="report-data-body-title"><font color="#397DBD">����</font></th>
      								<th width='15%' align="center" class="report-data-body-title"><font color="#397DBD">�ʼ���ַ</font></th>  
      								<th width='15%' align="center" class="report-data-body-title"><font color="#397DBD">���յ�ַ</font></th>
      								<th width='10%' align="center" class="report-data-body-title"><font color="#397DBD">��ʱ</font></th>
      								<th width='10%' align="center" class="report-data-body-title"><font color="#397DBD">��ǰ״̬</font></th>
      								<th width='10%' align="center" class="report-data-body-title"><font color="#397DBD">�Ƿ���</font></th>
      								<th width='10%' align="center" class="report-data-body-title"><font color="#397DBD">����</font></th>
							</tr>
			<%
    			EmailMonitorConfig emailvo = null;
   			for(int i=0;i<userEmailMonitorConfigList.size();i++)
    			{
       				emailvo = (EmailMonitorConfig)userEmailMonitorConfigList.get(i);
       				Node node = PollingEngine.getInstance().getMailByID(emailvo.getId());
       				String alarmmessage = "";
       				if(node != null){
       					if(node.isAlarm())node.setStatus(3);
       					List alarmlist = node.getAlarmMessage();
       					if(alarmlist!= null && alarmlist.size()>0){
						for(int k=0;k<alarmlist.size();k++){
							alarmmessage = alarmmessage+alarmlist.get(k).toString();
						}
					}
       				}       				
			%>
   										<tr bgcolor="FFFFFF" class="microsoftLook" height="25"> 
    											<td  height=25  class="report-data-body-list"><font color='blue'><%=1 + i%></font></td>
    											<td  height=25  class="report-data-body-list"><%=emailvo.getName()%></td>
    											<td  height=25  class="report-data-body-list"><font color="#397DBD"><%= emailvo.getAddress()%></font></td>
    											<td  height=25  class="report-data-body-list"><%=emailvo.getRecivemail()%></td>
    											<td  height=25  class="report-data-body-list"><%=emailvo.getTimeout()%></td>
    											<td height=25 align='center' class="report-data-body-list"><img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(node.getStatus())%>" border="0" alt="<%=alarmmessage%>" /></td>
											<%
												if(emailvo.getMonflag()==0){
											%>
											
											<td  align='center' height=25  class="report-data-body-list">��</td>
											<% 
												//if(vo.getFlag()==1);
												}else{
											%>
											 	<td  align='center' height=25  class="report-data-body-list">��</td>
											 <%
											 	}
											 %>
											<td height=25 height=25  class="report-data-body-list">&nbsp;
												<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('email','2','<%=emailvo.getId()%>','')>
											</td>											 
  										</tr>			
								<%;} %>	  				
									
						</table>
					</td>
				</tr>
        			
        		
        		
        		
        			<tr>
					<td>
						<table cellspacing="1" cellpadding="0" width="100%">
							<tr>
								<td height="28" align="left" bordercolor="#ffffff" bgcolor="#ffffff" class="txtGlobalBold" colspan="8">&nbsp;&nbsp;<font color="#397DBD">���̷���</font></td>
							</tr>
								<tr class="microsoftLook0" height=28 align="center">
      								<th width='5%' align="center" class="report-data-body-title"><font color="#397DBD">���</font></th>				
      								<th width='20%' align="center" class="report-data-body-title"><font color="#397DBD">IP��ַ</font></th>
      								<th width='30%' align="center" class="report-data-body-title"><font color="#397DBD">��������</font></th>
      								<th width='30%' align="center" class="report-data-body-title"><font color="#397DBD">��ע</font></th> 
									<th width='10%' align="center" class="report-data-body-title""><font color="#397DBD">����</font></th>
							</tr>
			<%
    			Procs provo = null;
   				 for(int i=0;i<prolist.size();i++)
    			{
       				provo = (Procs)prolist.get(i);
			%>
   										<tr bgcolor="FFFFFF" class="microsoftLook" height="25">
    											<td  class="report-data-body-list">
    												<font color='blue'><%=1 + i%></font></td>
    											<td  align='center'  height=25  class="report-data-body-list"><%=provo.getIpaddress()%></td>
    											<td  align='center' class="report-data-body-list"><%= provo.getProcname() %> </td>
    											<td  align='center' height=25><%=provo.getBak()%></td> 
												<td height=25 height=25>&nbsp;
												<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 onclick=toReady_edit(<%=provo.getId()%>)>
											</td>
  										</tr>			
								<%;} %>				
									
						</table>
					</td>
				</tr>	
        			
        				
        	    
        	    <tr>
					<td>
						<table cellspacing="1" cellpadding="0" width="100%">
							<tr>
								<td height="28" align="left" bordercolor="#ffffff" bgcolor="#ffffff" class="txtGlobalBold" colspan="8">&nbsp;&nbsp;<font color="#397DBD">WEB����</font></td>
							</tr>
								<tr class="microsoftLook0" height=28 align="center">
      								<th width='5%' align="center" class="report-data-body-title"><font color="#397DBD">���</font></th>				
      								<th width='20%' align="center" class="report-data-body-title"><font color="#397DBD">����</font></th>
      								<th width='30%' align="center" class="report-data-body-title"><font color="#397DBD">·��</font></th>
      								<th width='10%' align="center" class="report-data-body-title"><font color="#397DBD">��ʱ</font></th>
      								<th width='10%' align="center" class="report-data-body-title"><font color="#397DBD">��ǰ״̬</font></th>      
      								<th width='10%' align="center" class="report-data-body-title"><font color="#397DBD">�Ƿ���</font></th>
      								<th width='10%' align="center" class="report-data-body-title"><font color="#397DBD">����</font></th>
							</tr>
			<%
    			WebConfig webvo = null;
   				 for(int i=0;i<weblist.size();i++)
    			{
       				webvo = (WebConfig)weblist.get(i);
       				int status = 0;
       				Node node = (Node)PollingEngine.getInstance().getWebByID(webvo.getId());
       				String alarmmessage = "";
       				if(node != null){
       					if(node.isAlarm())status = 3;
       					List alarmlist = node.getAlarmMessage();
       					if(alarmlist!= null && alarmlist.size()>0){
						for(int k=0;k<alarmlist.size();k++){
							alarmmessage = alarmmessage+alarmlist.get(k).toString();
						}
					}
					
       				}
       				//System.out.println(NodeHelper.getCurrentStatusImage(status)+"====="+status);       				
			%>
   										<tr  bgcolor="FFFFFF" class="microsoftLook" height="25">
    											<td   height=25  class="report-data-body-list">
    												<font color='blue'><%=1 + i%></font></td>
    											<td  align='center'  height=25  class="report-data-body-list"" ><%=webvo.getAlias()%></td>
    											<td  align='center'  height=25  class="report-data-body-list"><a href=<%= webvo.getStr() %> target=_blank><font color="#397DBD"><%= webvo.getStr()%></font></a></td>
    											<td  align='center'  height=25  class="report-data-body-list"><%=webvo.getTimeout()%></td>
    											<td align='center'  height=25  class="report-data-body-list"><img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(status)%>" border="0" alt=<%=alarmmessage%>></td>
											<%
												if(webvo.getFlag()==0){
											%>
											<td  align='center'  height=25  class="report-data-body-list">��</td>
											<% 
												//if(vo.getFlag()==1);
												}else{
											%>
											 <td  align='center'  height=25  class="report-data-body-list">��</td>
											 <%
											 	}
											 %>
											<td height=25  height=25  class="report-data-body-list">&nbsp;
												<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('web','2','<%=webvo.getId()%>','')>
											</td>											 
  										</tr>			
								<%;} %>	
									
						</table>
					</td>
				</tr>	
        	    
        	    
        	    
        	    
        	     <tr>
					<td>
						<table cellspacing="1" cellpadding="0" width="100%">
							<tr>
								<td height="28" align="left" bordercolor="#ffffff" bgcolor="#ffffff" class="txtGlobalBold" colspan="8">&nbsp;&nbsp;<font color="#397DBD">�˿ڷ���</font></td>
							</tr>
								<tr class="microsoftLook0" height=28 align="center">
    					<th width='5%' align="center" class="report-data-body-title"><font color="#397DBD">���</font></th>
    					<th width='20%' align="center" class="report-data-body-title"><font color="#397DBD">IP</font></th>
    					<th width='10%' align="center" class="report-data-body-title"><font color="#397DBD">�˿�</font></th>
    					<th width='20%' align="center" class="report-data-body-title"><font color="#397DBD">��������</font></th>
    					<th width='20%' align="center" class="report-data-body-title"><font color="#397DBD">��ǰ״̬</font></th>
    					<th width='10%' align="center" class="report-data-body-title"><font color="#397DBD">�Ƿ����</font></th>
    					<th width='10%' align="center" class="report-data-body-title"><font color="#397DBD">����</font></th>
					</tr>
<%
System.out.println(portlist.size());
if(portlist.size() > 0){
    for(int i=0;i<portlist.size();i++)
    {
       PSTypeVo portvo = (PSTypeVo)portlist.get(i);
       Node node = PollingEngine.getInstance().getSocketByID(portvo.getId());
       String alarmmessage = "";
       
       if(node != null){
       		portvo.setStatus(0);
       		if(node.isAlarm())portvo.setStatus(3);
       		List alarmlist = node.getAlarmMessage();
       		if(alarmlist!= null && alarmlist.size()>0){
			for(int k=0;k<alarmlist.size();k++){
				alarmmessage = alarmmessage+alarmlist.get(k).toString();
			}
		}
       } 
%>
       <tr bgcolor="FFFFFF" class="microsoftLook" height="25">
    		<td height=25  class="report-data-body-list"><font color='blue'><%=1 + i%></font></td>
		<td height=25  class="report-data-body-list"><%=portvo.getIpaddress()%></td> 
		<td height=25  class="report-data-body-list"><%=portvo.getPort()%></td> 
		<td height=25  class="report-data-body-list"><%=portvo.getPortdesc()%></td> 
		<td align='center'  class="report-data-body-list"><img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(portvo.getStatus())%>" border="0"/></td>
<%
		if(portvo.getMonflag()==1){
	%>
	<td  align='center' class="report-data-body-list">��</td>
	<%
		}else{
	%>
	<td  align='center' class="report-data-body-list">��</td>
	<%
		}
	%>	
		<td height=25 class="report-data-body-list">&nbsp;
			<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('port','2','<%=portvo.getId()%>','')>
		</td>
  	</tr>
<%	}
}

%>				
									
						</table>
					</td>
				</tr>	
        	    
        	    
        				
        				
        							
				<tr>
		              <td background="<%=rootPath%>/common/images/right_b_02.jpg" >
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
			    </form>

</BODY>
</HTML>       