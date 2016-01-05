<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@ include file="/include/globe.inc"%>

<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.polling.base.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="java.util.*"%>

<%
  List tomcatlist = (List)request.getAttribute("tomcatlist");
  int tomcatrc = tomcatlist.size();
 List mqlist = (List)request.getAttribute("mqlist");
  int mqtrc = mqlist.size();
 List dominolist = (List)request.getAttribute("dominolist");
List dnslist = (List)request.getAttribute("dnslist");
String state = (String)request.getAttribute("state");
List cicslist = (List)request.getAttribute("cicslist");
List iislist = (List)request.getAttribute("iislist");
List weblogiclist = (List)request.getAttribute("weblogiclist");
if(weblogiclist == null)weblogiclist = new ArrayList();
List waslist = (List)request.getAttribute("waslist");
 if(waslist == null)waslist = new ArrayList();


  String rootPath = request.getContextPath();  
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
		if(ser == "mq"){
			document.getElementById('itemMenu').innerHTML="<table border='1' width='100%' height='100%' bgcolor='#F1F1F1'"+
		"style='border: thin;font-size: 12px' cellspacing='0'>"+
		"<tr>"+
			"<td style='cursor: default; border: outset 1;' align='center'"+
				"onclick='parent.mqedit()'>�޸���Ϣ</td>"+
		"</tr>"+
		"<tr>"+
			"<td style='cursor: default; border: outset 1;' align='center'"+
				"onclick='parent.mqdetail();'>������Ϣ</td>"+
		"</tr>"+
		"<tr>"+
			"<td style='cursor: default; border: outset 1;' align='center'"+
				"onclick='parent.mqcancelmanage()'>ȡ������</td>"+
		"</tr>"+
		"<tr>"+
			"<td style='cursor: default; border: outset 1;' align='center'"+
				"onclick='parent.mqaddmanage()'>��Ӽ���</td>"+
		"</tr>"+	
	"</table>";
		}else if(ser == "domino"){
		document.getElementById('itemMenu').innerHTML="<table border='1' width='100%' height='100%' bgcolor='#F1F1F1'"+
			"style='border: thin;font-size: 12px' cellspacing='0'>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.dominoedit()'>�޸���Ϣ</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.dominodetail();'>������Ϣ</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.dominocancelmanage()'>ȡ������</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.dominoaddmanage()'>��Ӽ���</td>"+
			"</tr>"+
		"</table>";
		}else if(ser == "was"){
			document.getElementById('itemMenu').innerHTML="<table border='1' width='100%' height='100%' bgcolor='#F1F1F1'"+
			"style='border: thin;font-size: 12px' cellspacing='0'>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.wasedit()'>�޸���Ϣ</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.wasdetail();'>������Ϣ</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.wascancelalert()'>ȡ������</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.wasaddalert()'>��Ӽ���</td>"+
			"</tr>"+
		"</table>";
		}else if(ser == "weblogic"){
		document.getElementById('itemMenu').innerHTML="<table border='1' width='100%' height='100%' bgcolor='#F1F1F1'"+
			"style='border: thin;font-size: 12px' cellspacing='0'>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.weblogicedit()'>�޸���Ϣ</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.weblogicdetail();'>������Ϣ</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.weblogiccancelalert()'>ȡ������</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.weblogicaddalert()'>��Ӽ���</td>"+
			"</tr>"+
		"</table>";
		}else if(ser == "dns"){
		document.getElementById('itemMenu').innerHTML="<table border='1' width='100%' height='100%' bgcolor='#F1F1F1'"+
			"style='border: thin;font-size: 12px' cellspacing='0'>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.dnsedit()'>�޸���Ϣ</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.dnsdetail();'>������Ϣ</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.dnscancelmanage()'>ȡ������</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.dnsaddmanage()'>��Ӽ���</td>"+
			"</tr>"+
		"</table>";
		}else if(ser == "iis"){
		document.getElementById('itemMenu').innerHTML="<table border='1' width='100%' height='100%' bgcolor='#F1F1F1'"+
			"style='border: thin;font-size: 12px' cellspacing='0'>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.iisedit()'>�޸���Ϣ</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.iisdetail();'>������Ϣ</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.iiscancelmanage()'>ȡ������</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.iisaddmanage()'>��Ӽ���</td>"+
			"</tr>"+
		"</table>";
		}else if(ser == "cics"){
		document.getElementById('itemMenu').innerHTML="<table border='1' width='100%' height='100%' bgcolor='#F1F1F1'"+
			"style='border: thin;font-size: 12px' cellspacing='0'>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.cicsedit()'>�޸���Ϣ</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.cicsdetail();'>������Ϣ</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.cicscancelmanage()'>ȡ������</td>"+
			"</tr>"+
			"<tr>"+
				"<td style='cursor: default; border: outset 1;' align='center'"+
					"onclick='parent.cicsaddmanage()'>��Ӽ���</td>"+
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
	function cicsdetail()
	{
	    location.href="<%=rootPath%>/cics.do?action=detail&id="+node;
	}
	function cicsedit()
	{
		location.href="<%=rootPath%>/cics.do?action=ready_edit&id="+node;
	}
	function cicscancelmanage()
	{
		location.href="<%=rootPath%>/cics.do?action=cancelalert&id="+node;
	}
	function cicsaddmanage()
	{
		location.href="<%=rootPath%>/cics.do?action=addalert&id="+node;
	}	


	function dnsdetail()
	{
	    location.href="<%=rootPath%>/dns.do?action=detail&id="+node;
	}
	function dnsedit()
	{
		location.href="<%=rootPath%>/dns.do?action=ready_edit&id="+node;
	}
	function dnscancelmanage()
	{
		location.href="<%=rootPath%>/dns.do?action=cancelalert&id="+node;
	}
	function dnsaddmanage()
	{
		location.href="<%=rootPath%>/dns.do?action=addalert&id="+node;
	}	


    function dominodetail()
	{
	    location.href="<%=rootPath%>/domino.do?action=detail&id="+node;
	}
	function dominoedit()
	{
		location.href="<%=rootPath%>/domino.do?action=ready_edit&id="+node;
	}
	function dominocancelmanage()
	{
		location.href="<%=rootPath%>/domino.do?action=cancelalert&id="+node;
	}
	function dominoaddmanage()
	{
		location.href="<%=rootPath%>/domino.do?action=addalert&id="+node;
	}	

    function iisdetail()
	{
	    location.href="<%=rootPath%>/iis.do?action=detail&id="+node;
	}
	function iisedit()
	{
		location.href="<%=rootPath%>/iis.do?action=ready_edit&id="+node;
	}
	function iiscancelmanage()
	{
		location.href="<%=rootPath%>/iis.do?action=cancelalert&id="+node;
	}
	function iisaddmanage()
	{
		location.href="<%=rootPath%>/iis.do?action=addalert&id="+node;
	}	

    function mqdetail()
	{
	    location.href="<%=rootPath%>/mq.do?action=detail&id="+node;
	}
	function mqedit()
	{
		location.href="<%=rootPath%>/mq.do?action=ready_edit&id="+node;
	}
	function mqcancelmanage()
	{
		location.href="<%=rootPath%>/mq.do?action=cancelalert&id="+node;
	}
	function mqaddmanage()
	{
		location.href="<%=rootPath%>/mq.do?action=addalert&id="+node;
	}	

    function wasdetail()
	{
	    location.href="<%=rootPath%>/was.do?action=detail&id="+node;
	}
	function wasedit()
	{
		location.href="<%=rootPath%>/was.do?action=ready_edit&id="+node;
	}
	function wascancelalert()
	{
		location.href="<%=rootPath%>/was.do?action=cancelalert&id="+node;
	}
	function wasaddalert()
	{
		location.href="<%=rootPath%>/was.do?action=addalert&id="+node;
	}	

    function weblogicdetail()
	{
	    location.href="<%=rootPath%>/weblogic.do?action=detail&id="+node;
	}
	function weblogicedit()
	{
		location.href="<%=rootPath%>/weblogic.do?action=ready_edit&id="+node;
	}
	function weblogiccancelalert()
	{
		location.href="<%=rootPath%>/weblogic.do?action=cancelalert&id="+node;
	}
	function weblogicaddalert()
	{
		location.href="<%=rootPath%>/weblogic.do?action=addalert&id="+node;
	}	


	function cicsclickMenu()
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
								<td height="28" align="left" bordercolor="#ffffff" bgcolor="#ffffff" class="txtGlobalBold" colspan="7">&nbsp;&nbsp;<font color="#397DBD">Tomcat�����б�</font></td>
							</tr>
							
										<tr class="microsoftLook0" height=28 align="center">
					    					<th width='10%' align="center" class="report-data-body-title"><font color="#397DBD">���</font></th>
					    					<th width='15%' align="center" class="report-data-body-title"><font color="#397DBD">����</font></th>
					    					<th width='20%' align="center"  class="report-data-body-title"><font color="#397DBD">IP��ַ</font></th>
					    					<th width='10%' align="center" class="report-data-body-title"><font color="#397DBD">�˿�</font></th>
					    					<th width='15%' align="center" class="report-data-body-title"><font color="#397DBD">��ǰ״̬</font></th>
					    					<th width='20%' align="center" class="report-data-body-title"><font color="#397DBD">��ϸ</font></th>
					    					<th width='20%' align="center" class="report-data-body-title"><font color="#397DBD">�༭</font></th>
										</tr>
										<%
										    for(int i=0;i<tomcatrc;i++)
										    {
										       Tomcat tomcatvo = (Tomcat)tomcatlist.get(i);
										       Node node = PollingEngine.getInstance().getTomcatByID(tomcatvo.getId());
										       String alarmmessage = "";
										       if(node != null){
										       		tomcatvo.setStatus(1);
										       		//System.out.println(vo.getIpAddress()+"====================="+node.isAlarm());
										       		if(node.isAlarm())tomcatvo.setStatus(3);
										       		List alarmlist = node.getAlarmMessage();
										       		if(alarmlist!= null && alarmlist.size()>0){
													for(int k=0;k<alarmlist.size();k++){
														alarmmessage = alarmmessage+alarmlist.get(k).toString();
													}
												}
										       }       
										%>
								       	<tr bgcolor="FFFFFF" class="microsoftLook" height="25">
									    	<td  class="report-data-body-list"><font color='blue'><%=1 + i%></font></td>
											<td  class="report-data-body-list"><%=tomcatvo.getAlias()%></td> 
											<td  class="report-data-body-list"><%=tomcatvo.getIpAddress()%></td> 
											<td  class="report-data-body-list"><%=tomcatvo.getPort()%></td>		
											<td  class="report-data-body-list" align="center" ><img src="<%=rootPath%>/resource/<%=NodeHelper.getStatusImage(tomcatvo.getStatus())%>" border="0"/></td>		
											<td  class="report-data-body-list" align='center' ><a href="#" onclick='detail(<%=tomcatvo.getId()%>)'>
											<img src="<%=rootPath%>/resource/image/detail.jpg" border="0"/></a></td>		
											<td  class="report-data-body-list" ><a href="<%=rootPath%>/tomcat.do?action=ready_edit&id=<%=tomcatvo.getId()%>">
											<img src="<%=rootPath%>/resource/image/editicon.gif" border="0"/></a></td>
								  		</tr>
										<%}%>  	  				
									
						</table>
					</td>
				</tr>
			<!--MQ  -->		
			    
			    <tr>
					<td>
						<table cellspacing="1" cellpadding="0" width="100%">
							<tr>
								<td height="28" align="left" bordercolor="#ffffff" bgcolor="#ffffff" class="txtGlobalBold" colspan="8">&nbsp;&nbsp;<font color="#397DBD">MQ�����б�</font></td>
							</tr>
						
									<tr class="microsoftLook0" height=28>
				    					<th width='5%' class="report-data-body-title"><font color="#397DBD">���</font></th>
				      					<th width='10%' class="report-data-body-title"><font color="#397DBD">MQ����</font></th>
				      					<th width='20%' class="report-data-body-title"><font color="#397DBD">IP��ַ</font></th>
				      					<th width='20%' class="report-data-body-title"><font color="#397DBD">���й���������</font></th>
				      					<th width='10%' class="report-data-body-title"><font color="#397DBD">�˿�</font></th> 
				      					<th width='10%' class="report-data-body-title"><font color="#397DBD">��ǰ״̬</font></th>     
				      					<th width='10%' class="report-data-body-title"><font color="#397DBD">�Ƿ���</font></th>
				    					<th width='10%' class="report-data-body-title"><font color="#397DBD">����</font></th>
				          </tr>
				<%
				if(mqtrc > 0){
				    for(int i=0;i<mqtrc;i++)
				    {
				       MQConfig mqvo = (MQConfig)mqlist.get(i);
				       Node node = PollingEngine.getInstance().getMqByID(mqvo.getId());
				       String alarmmessage = "";
				       if(node != null){
				       		mqvo.setStatus(1);
				       		//System.out.println(mqvo.getIpaddress()+"====================="+node.isAlarm());
				       		if(node.isAlarm())mqvo.setStatus(3);
				       		List alarmlist = node.getAlarmMessage();
				       		if(alarmlist!= null && alarmlist.size()>0){
							for(int k=0;k<alarmlist.size();k++){
								alarmmessage = alarmmessage+alarmlist.get(k).toString();
							}
						}
				       }        
				       
				%>
				       <tr bgcolor="FFFFFF" class="microsoftLook">
				    		<td height=25 class="report-data-body-list">&nbsp;<font color='blue'><%=1 + i%></font></td>
						<td height=25 class="report-data-body-list">&nbsp;<%=mqvo.getName()%></td> 
						<td height=25 class="report-data-body-list">&nbsp;<%=mqvo.getIpaddress()%></td> 
						<td height=25 class="report-data-body-list">&nbsp;<%=mqvo.getManagername()%></td> 
						<td height=25 class="report-data-body-list">&nbsp;<%=mqvo.getPortnum()%></td> 
						<td align='center' class="report-data-body-list"><img src="<%=rootPath%>/resource/<%=NodeHelper.getStatusImage(mqvo.getStatus())%>" border="0" alt=<%=alarmmessage%>></td>		
						<td height=25 class="report-data-body-list">
							<%
								if(mqvo.getMon_flag() == 0){
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
						<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('mq','2','<%=mqvo.getId()%>','<%=mqvo.getIpaddress()%>')>
						</td>
				  	</tr>
				<%	}
				}
				
				%>				
							
						</table>
					</td>
				</tr>
        	  <!-- Domino�����б� -->	
        			
        			   
			    <tr>
					<td>
						<table cellspacing="1" cellpadding="0" width="100%">
							<tr>
								<td height="28" align="left" bordercolor="#ffffff" bgcolor="#ffffff" class="txtGlobalBold" colspan="6">&nbsp;&nbsp;<font color="#397DBD">Domino�����б�</font></td>
							</tr>
							
								<tr height=28 class="microsoftLook0">
			    					<th width='5%' class="report-data-body-title"><font color="#397DBD">���</font></th>
			      					<th width='20%' class="report-data-body-title"><font color="#397DBD">����</font></th>
			      					<th width='20%' class="report-data-body-title"><font color="#397DBD">IP��ַ</font></th>
			      					<th width='20%' class="report-data-body-title"><font color="#397DBD">��������</font></th>     
			      					<th width='10%' class="report-data-body-title"><font color="#397DBD">�Ƿ���</font></th>
			    					<th width='10%' class="report-data-body-title"><font color="#397DBD">����</font></th>
			</tr>
			<%
			if(dominolist.size() > 0){
			    for(int i=0;i<dominolist.size();i++)
			    {
			       DominoConfig dominovo = (DominoConfig)dominolist.get(i);
			       
			%>
			       <tr bgcolor="FFFFFF"  class="microsoftLook">
			    		
			    		<td height=25 class="report-data-body-list">&nbsp;<font color='blue'><%=1 + i%></font></td>
					<td height=25 class="report-data-body-list">&nbsp;<%=dominovo.getName()%></td> 
					<td height=25 class="report-data-body-list">&nbsp;<%=dominovo.getIpaddress()%></td> 
					<td height=25 class="report-data-body-list">&nbsp;<%=dominovo.getCommunity()%></td> 	
					<td height=25 class="report-data-body-list">
						<%
							if(dominovo.getMon_flag() == 0){
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
					<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('domino','2','<%=dominovo.getId()%>','<%=dominovo.getIpaddress()%>')>
					</td>
			  	</tr>
			<%	}
			}
			
			%>				
						
						</table>
					</td>
				</tr>
        
        <!-- Webphere�����б� -->	
        			
        	  <tr>
					<td>
						<table cellspacing="1" cellpadding="0" width="100%">
							<tr>
								<td height="28" align="left" bordercolor="#ffffff" bgcolor="#ffffff" class="txtGlobalBold" colspan="5">&nbsp;&nbsp;<font color="#397DBD">Webphere�����б�</font></td>
							</tr>
							
							<tr height=28 class="microsoftLook0">
		    					<th width='5%' class="report-data-body-title"><font color="#397DBD">���</font></th>
		      					<th width='30%' class="report-data-body-title"><font color="#397DBD">����</font></th>
		      					<th width='30%' class="report-data-body-title"><font color="#397DBD">IP��ַ</font></th>    
		      					<th width='15%' class="report-data-body-title"><font color="#397DBD">�Ƿ���</font></th>
		    					<th width='15%' class="report-data-body-title"><font color="#397DBD">����</font></th>
		</tr>
		<%
		if(waslist.size() > 0){
		    for(int i=0;i<waslist.size();i++)
		    {
		       WasConfig wasconfigvo = (WasConfig)waslist.get(i);
		       
		%>
		       <tr bgcolor="FFFFFF" class="microsoftLook">
		    		
		    	<td height=25 class="report-data-body-list">&nbsp;<font color='blue'><%=1 + i%></font></td>
				<td height=25 class="report-data-body-list">&nbsp;<%=wasconfigvo.getName()%></td> 
				<td height=25 class="report-data-body-list">&nbsp;<%=wasconfigvo.getIpaddress()%></td> 	
				<td height=25 class="report-data-body-list">
					<%
						if(wasconfigvo.getMon_flag() == 0){
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
				<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('was','2','<%=wasconfigvo.getId()%>','<%=wasconfigvo.getIpaddress()%>')>
				</td>
		  	</tr>
		<%	}
		}
		
		%>				
					
						</table>
					</td>
				</tr>
				
        		<!-- Weblogic�����б� -->
        		
        		<tr>
					<td>
						<table cellspacing="1" cellpadding="0" width="100%">
							<tr>
								<td height="28" align="left" bordercolor="#ffffff" bgcolor="#ffffff" class="txtGlobalBold" colspan="7">&nbsp;&nbsp;<font color="#397DBD">Weblogic�����б�</font></td>
							</tr>
							
							<tr height=28 class="microsoftLook0">
		    					<th width='5%'  class="report-data-body-title"><font color="#397DBD">���</font></th>
		      					<th width='20%' class="report-data-body-title"><font color="#397DBD">����</font></th>
		      					<th width='20%' class="report-data-body-title"><font color="#397DBD">IP��ַ</font></th>
		      					<th width='20%' class="report-data-body-title"><font color="#397DBD">��������</font></th> 
		      					<th width='10%' class="report-data-body-title"><font color="#397DBD">��ǰ״̬</font></th>    
		      					<th width='10%' class="report-data-body-title"><font color="#397DBD">�Ƿ���</font></th>
		    					<th width='10%' class="report-data-body-title"><font color="#397DBD">����</font></th>
							</tr>
					<%
					if(weblogiclist.size() > 0){
					    for(int i=0;i<weblogiclist.size();i++)
					    {
					       WeblogicConfig weblogicvo = (WeblogicConfig)weblogiclist.get(i);
					       Node node = PollingEngine.getInstance().getWeblogicByID(weblogicvo.getId());
					       String alarmmessage = "";
					       if(node != null){
					       		weblogicvo.setStatus(1);
					       		if(node.isAlarm())weblogicvo.setStatus(3);
					       		List alarmlist = node.getAlarmMessage();
					       		if(alarmlist!= null && alarmlist.size()>0){
								for(int k=0;k<alarmlist.size();k++){
									alarmmessage = alarmmessage+alarmlist.get(k).toString();
								}
							}
					       }
					       
					%>
		       <tr bgcolor="FFFFFF" class="microsoftLook">
		    		
		    		<td height=25 class="report-data-body-list">&nbsp;<font color='blue'><%=1 + i%></font></td>
				<td height=25 class="report-data-body-list">&nbsp;<%=weblogicvo.getAlias()%></td> 
				<td height=25 class="report-data-body-list">&nbsp;<%=weblogicvo.getIpAddress()%></td> 
				<td height=25 class="report-data-body-list">&nbsp;<%=weblogicvo.getCommunity()%></td> 
				<td align='center' class="report-data-body-list"><img src="<%=rootPath%>/resource/<%=NodeHelper.getStatusImage(weblogicvo.getStatus())%>" border="0"/></td>	
				<td height=25 class="report-data-body-list">
					<%
						if(weblogicvo.getMon_flag() == 0){
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
				<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('weblogic','2','<%=weblogicvo.getId()%>','<%=weblogicvo.getIpAddress()%>')>
				</td>
		  	</tr>
		<%	}
		}
		
		%>				
					
						</table>
					</td>
				</tr>	
				
				
	       <!-- IIS�����б� --> 		
        		
        		<tr>
					<td>
						<table cellspacing="1" cellpadding="0" width="100%">
							<tr>
								<td height="28" align="left" bordercolor="#ffffff" bgcolor="#ffffff" class="txtGlobalBold" colspan="6">&nbsp;&nbsp;<font color="#397DBD">IIS�����б�</font></td>
							</tr>
							
							<tr height=28 class="microsoftLook0">
		    					<th width='5%' class="report-data-body-title"><font color="#397DBD">���</font></th>
		      					<th width='20%' class="report-data-body-title"><font color="#397DBD">����</font></th>
		      					<th width='20%' class="report-data-body-title"><font color="#397DBD">IP��ַ</font></th>
		      					<th width='20%' class="report-data-body-title"><font color="#397DBD">��������</font></th>     
		      					<th width='10%' class="report-data-body-title"><font color="#397DBD">�Ƿ���</font></th>
		    					<th width='10%' class="report-data-body-title"><font color="#397DBD">����</font></th>
		</tr>
		<%
		if(iislist.size() > 0){
		    for(int i=0;i<iislist.size();i++)
		    {
		       IISConfig iisconfigvo = (IISConfig)iislist.get(i);
		       
		%>
		       <tr  bgcolor="FFFFFF" class="microsoftLook">
		    		<td height=25 class="report-data-body-list">&nbsp;<font color='blue'><%=1 + i%></font></td>
				<td height=25 class="report-data-body-list">&nbsp;<%=iisconfigvo.getName()%></td> 
				<td height=25 class="report-data-body-list">&nbsp;<%=iisconfigvo.getIpaddress()%></td> 
				<td height=25 class="report-data-body-list">&nbsp;<%=iisconfigvo.getCommunity()%></td> 	
				<td height=25 class="report-data-body-list">
					<%
						if(iisconfigvo.getMon_flag() == 0){
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
				<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('iis','2','<%=iisconfigvo.getId()%>','<%=iisconfigvo.getIpaddress()%>')>
				</td>
		  	</tr>
		<%	}
		}
		
		%>				
					
						</table>
					</td>
				</tr>		
        			
      <!-- CICS�����б� -->  			
        			
        		<tr>
					<td>
						<table cellspacing="1" cellpadding="0" width="100%">
							<tr>
								<td height="28" align="left" bordercolor="#ffffff" bgcolor="#ffffff" class="txtGlobalBold" colspan="9">&nbsp;&nbsp;<font color="#397DBD">CICS�����б�</font></td>
							</tr>
							
	  						<tr height=28 class="microsoftLook0">
      								<th width='10%' class="report-data-body-title"><font color="#397DBD">���</font></th>				
      								<th width='15%' class="report-data-body-title"><font color="#397DBD">��������</font></th>
      								<th width='15%' class="report-data-body-title"><font color="#397DBD">��������IP��ַ</font></th>
      								<th width='10%' class="report-data-body-title"><font color="#397DBD">����Э��</font></th>      
      								<th width='10%' class="report-data-body-title"><font color="#397DBD">�����˿�</font></th>
      								<th width='10%' class="report-data-body-title"><font color="#397DBD">�Ƿ���</font></th>
      								<th width='10%' class="report-data-body-title"><font color="#397DBD">��ǰ״̬</font></th>
      								<th width='10%' class="report-data-body-title"><font color="#397DBD">Gateway</font></th>
      								<th width='10%' class="report-data-body-title"><font color="#397DBD">����</font></th>
							</tr>
			<%
    			CicsConfig cicsconfigvo = null;
   				 for(int i=0;i<cicslist.size();i++)
    			{
       				cicsconfigvo = (CicsConfig)cicslist.get(i);
       				Node node = PollingEngine.getInstance().getCicsByID(cicsconfigvo.getId());
       				String alarmmessage = "";
				       if(node != null){
				       		cicsconfigvo.setStatus(1);
				       		//System.out.println(vo.getIpaddress()+"====================="+node.isAlarm());
				       		if(node.isAlarm())cicsconfigvo.setStatus(3);
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
   										<tr bgcolor="FFFFFF" class="microsoftLook">  		
    											<td><font color='blue' class="report-data-body-list"><font color="#397DBD"><%=1 + i%></font></td>
    											<td  align='center'  class="report-data-body-list"><%=cicsconfigvo.getRegion_name()%></td>
    											<td  align='center' class="report-data-body-list"><font color="#397DBD"><%= cicsconfigvo.getIpaddress()%></font></td>
    											<td  align='center' class="report-data-body-list"><%=cicsconfigvo.getNetwork_protocol()%></td>
    											<td  align='center' class="report-data-body-list"><%=cicsconfigvo.getPort_listener()%></td>
    											<%
												if(cicsconfigvo.getFlag()==0){
											%>
											<td  align='center' class="report-data-body-list">��</td>
											<% 
												//if(vo.getFlag()==1);
												}else{
											%>
											 <td  align='center' class="report-data-body-list">��</td>
											 <%
											 	}
											 %>
											 <td  align='center'  class="report-data-body-list"><img src="<%=rootPath%>/resource/<%=NodeHelper.getStatusImage(cicsconfigvo.getStatus())%>" border="0" alt="<%=alarmmessage%>"/></td>
											 <td  align='center'  class="report-data-body-list"><%=cicsconfigvo.getGateway()%></td>
											<td height=25 class="report-data-body-list">&nbsp;
												<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('cics','2','<%=cicsconfigvo.getId()%>','')>
											</td>											 
  										</tr>			
								<%;} %>
									
						</table>
					</td>
				</tr>	
				
        		<!--DNS�����б�  -->	
        			 <tr>
					<td>
						<table cellspacing="1" cellpadding="0" width="100%">
							<tr>
								<td height="28" align="left" bordercolor="#ffffff" bgcolor="#ffffff" class="txtGlobalBold" colspan="7">&nbsp;&nbsp;<font color="#397DBD">DNS�����б�</font></td>
							</tr>
							
	  						<tr height=28 class="microsoftLook0">
      								<th width='10%' class="report-data-body-title"><font color="#397DBD">���</font></th>				
      								<th width='25%' class="report-data-body-title"><font color="#397DBD">DNS����</font></th>
      								<th width='10%' class="report-data-body-title"><font color="#397DBD">DNS�ӿ�</font></th>
      								<th width='25%' class="report-data-body-title"><font color="#397DBD">����</font></th>
      								<th width='10%' class="report-data-body-title"><font color="#397DBD">״̬</font></th>      
      								<th width='10%' class="report-data-body-title"><font color="#397DBD">�Ƿ���</font></th>
      								<th width='10%' class="report-data-body-title""><font color="#397DBD">����</font></th>
							</tr>
						<%
			    			DnsConfig dnsvo = null;
			   				 for(int i=0;i<dnslist.size();i++)
			    			{
			       				dnsvo = (DnsConfig)dnslist.get(i);
						%>
   										<tr bgcolor="#ffffff"  height=25>  
    											
    											<td><font color='blue' class="report-data-body-list"><%=1 + i%></font></td>
    											<td  align='center'  class="report-data-body-list"><%=dnsvo.getHostip()%></td>
    											<td  align='center'  class="report-data-body-list"><%= dnsvo.getHostinter()%></td>
    											<td  align='center' class="report-data-body-list"><%=dnsvo.getDns()%></td>
    											<td  align='center' class="report-data-body-list">
    											<%if(state==null)
    											{ %>
    											<img src="/afunms/resource/image/topo/status_ok.gif" alt="" border="0"/>
    											<%} else {%>
    											<img src="/afunms/resource/image/topo/alert.gif" alt="" border="0"/>
    											<%} %>
                                               </td>
											<%
												if(dnsvo.getFlag()==0){
											%>
											<td  align='center' class="report-data-body-list">��</td>
											<% 
												//if(vo.getFlag()==1);
												}else{
											%>
											 <td  align='center' class="report-data-body-list">��</td>
											 <%
											 	}
											 %>
											<td height=25 class="report-data-body-list">&nbsp;
												<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('dns','2','<%=dnsvo.getId()%>','')>
											</td>											 
  										</tr>			
								<%;} %>
									
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