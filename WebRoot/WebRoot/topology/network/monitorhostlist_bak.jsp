<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="java.util.List"%>
<%
  String rootPath = request.getContextPath();
  List list = (List)request.getAttribute("list");
  
  int rc = list.size();

  JspPage jp = (JspPage)request.getAttribute("page");
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
  var curpage= <%=jp.getCurrentPage()%>;
  var totalpages = <%=jp.getPageTotal()%>;
  var delAction = "<%=rootPath%>/network.do?action=delete";
  var listAction = "<%=rootPath%>/network.do?action=monitorhostlist";
  
  function doQuery()
  {  
	window.location = window.location
  }
  
    function doCancelManage()
  {  
     mainForm.action = "<%=rootPath%>/network.do?action=cancelmanage";
     mainForm.submit();
  }
  
  function doChange()
  {
     if(mainForm.view_type.value==1)
        window.location = "<%=rootPath%>/topology/network/index.jsp";
     else
        window.location = "<%=rootPath%>/topology/network/port.jsp";
  }

  function toAdd()
  {
      mainForm.action = "<%=rootPath%>/network.do?action=ready_add";
      mainForm.submit();
  }
  
// È«ÆÁ¹Û¿´
function gotoFullScreen() {
	parent.mainFrame.resetProcDlg();
	var status = "toolbar=no,height="+ window.screen.height + ",";
	status += "width=" + (window.screen.width-8) + ",scrollbars=no";
	status += "screenX=0,screenY=0";
	window.open("topology/network/index.jsp", "fullScreenWindow", status);
	parent.mainFrame.zoomProcDlg("out");
}

</script>
<script language="JavaScript">

	//¹«¹²±äÁ¿
	var node="";
	var ipaddress="";
	var operate="";
	/**
	*¸ù¾Ý´«ÈëµÄidÏÔÊ¾ÓÒ¼ü²Ëµ¥
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
	*ÏÔÊ¾µ¯³ö²Ëµ¥
	*menuDiv:ÓÒ¼ü²Ëµ¥µÄÄÚÈÝ
	*width:ÐÐÏÔÊ¾µÄ¿í¶È
	*rowControlString:ÐÐ¿ØÖÆ×Ö·û´®£¬0±íÊ¾²»ÏÔÊ¾£¬1±íÊ¾ÏÔÊ¾£¬Èç¡°101¡±£¬Ôò±íÊ¾µÚ1¡¢3ÐÐÏÔÊ¾£¬µÚ2ÐÐ²»ÏÔÊ¾
	*/
	function popMenu(menuDiv,width,rowControlString)
	{
	    //´´½¨µ¯³ö²Ëµ¥
	    var pop=window.createPopup();
	    //ÉèÖÃµ¯³ö²Ëµ¥µÄÄÚÈÝ
	    pop.document.body.innerHTML=menuDiv.innerHTML;
	    var rowObjs=pop.document.body.all[0].rows;
	    //»ñµÃµ¯³ö²Ëµ¥µÄÐÐÊý
	    var rowCount=rowObjs.length;
	    //alert("rowCount==>"+rowCount+",rowControlString==>"+rowControlString);
	    //Ñ­»·ÉèÖÃÃ¿ÐÐµÄÊôÐÔ
	    for(var i=0;i<rowObjs.length;i++)
	    {
	        //Èç¹ûÉèÖÃ¸ÃÐÐ²»ÏÔÊ¾£¬ÔòÐÐÊý¼õÒ»
	        var hide=rowControlString.charAt(i)!='1';
	        if(hide){
	            rowCount--;
	        }
	        //ÉèÖÃÊÇ·ñÏÔÊ¾¸ÃÐÐ
	        rowObjs[i].style.display=(hide)?"none":"";
	        //ÉèÖÃÊó±ê»¬Èë¸ÃÐÐÊ±µÄÐ§¹û
	        rowObjs[i].cells[0].onmouseover=function()
	        {
	            this.style.background="#397DBD";
	            this.style.color="white";
	        }
	        //ÉèÖÃÊó±ê»¬³ö¸ÃÐÐÊ±µÄÐ§¹û
	        rowObjs[i].cells[0].onmouseout=function(){
	            this.style.background="#F1F1F1";
	            this.style.color="black";
	        }
	    }
	    //ÆÁ±Î²Ëµ¥µÄ²Ëµ¥
	    pop.document.oncontextmenu=function()
	    {
	            return false; 
	    }
	    //Ñ¡ÔñÓÒ¼ü²Ëµ¥µÄÒ»Ïîºó£¬²Ëµ¥Òþ²Ø
	    pop.document.onclick=function()
	    {
	        pop.hide();
	    }
	    //ÏÔÊ¾²Ëµ¥
	    pop.show(event.clientX-1,event.clientY,width,rowCount*25,document.body);
	    return true;
	}
	function detail()
	{
	    location.href="<%=rootPath%>/detail/dispatcher.jsp?id=net"+node;
	}
	function ping()
	{
		window.open("<%=rootPath%>/tool/ping.jsp?ipaddress="+ipaddress,"oneping", "height=400, width= 500, top=300, left=100");
		//window.open('/nms/netutil/ping.jsp?ipaddress='+ipaddress);
	}
	function traceroute()
	{
		window.open("<%=rootPath%>/tool/tracerouter.jsp?ipaddress="+ipaddress,"newtracerouter", "height=400, width= 500, top=300, left=100");
		//window.open('/nms/netutil/tracerouter.jsp?ipaddress='+ipaddress);
	}
	function telnet()
	{
		window.open('<%=rootPath%>/network.do?action=telnet&ipaddress='+ipaddress,'onetelnet', 'height=0, width= 0, top=0, left= 0');
		//window.open('/nms/netutil/tracerouter.jsp?ipaddress='+ipaddress);
	}	
	function clickMenu()
	{
	}
</script>
<script language="JavaScript" type="text/JavaScript">
var show = true;
var hide = false;
//ÐÞ¸Ä²Ëµ¥µÄÉÏÏÂ¼ýÍ··ûºÅ
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
//Ìí¼Ó²Ëµ¥	
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
<!-- ÕâÀïÓÃÀ´¶¨ÒåÐèÒªÏÔÊ¾µÄÓÒ¼ü²Ëµ¥ -->
	<div id="itemMenu" style="display: none";>
	<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
		style="border: thin;font-size: 12px" cellspacing="0">
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.detail()">²é¿´×´Ì¬</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.ping();">ping</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.traceroute()">traceroute</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.telnet()">telnet</td>
		</tr>		
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.clickMenu()">¹Ø±Õ</td>
		</tr>
	</table>
	</div>
	<!-- ÓÒ¼ü²Ëµ¥½áÊø-->
<form method="post" name="mainForm">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td width="200" valign=top align=center>
			<%=menuTable%>		
		
		</td>
		<td bgcolor="#cedefa" align="center" valign=top>
			<table width="98%" style="BORDER-COLLAPSE: collapse" borderColor=#397DBD cellPadding=0 rules=none align=center border=1 algin="center">
				<tr>
					<td height="28" align="left" bordercolor="#397DBD" bgcolor="#397DBD" class="txtGlobalBold" colspan=3>&nbsp;&nbsp;Éè±¸Î¬»¤ >> Éè±¸ÁÐ±í</td>
				</tr>
				<tr bgcolor=#ffffff>
					<td align=3 height=30>
					&nbsp;&nbsp;<INPUT type="button" class="formStyle" value="È¡Ïû¹ÜÀí" onclick=" return doCancelManage()">
					</td>				
				</tr>
				<tr>

					
					<td height=300 bgcolor="#FFFFFF" valign="top" align=center>

						<table cellSpacing="1" cellPadding="0" width="100%" border="0">
						    <tr>
						    <td colspan="2" width="80%" align="center">
    <jsp:include page="../../common/page.jsp">
     <jsp:param name="curpage" value="<%=jp.getCurrentPage()%>"/>
     <jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>"/>
   </jsp:include>
        </td></tr> 						
        						<TR>
        							<TD width="50%"><B>²éÑ¯:</B>
        								<SELECT name="key" style="width=100"> 
          									<OPTION value="alias" selected>Ãû³Æ</OPTION>
          									<OPTION value="ip_address">IPµØÖ·</OPTION>
          									<OPTION value="sys_oid">ÏµÍ³OID</OPTION>          
          									<OPTION value="type">ÐÍºÅ</OPTION>
          								</SELECT>&nbsp;<b>=</b>&nbsp; 
          								<INPUT type="text" name="value" width="15" class="formStyle">
          								<INPUT type="button" class="formStyle" value="²éÑ¯" onclick=" return doQuery()">
          							</td>            
								<td width="50%" align='right'>
									<a href="#" onclick="toAdd()">Ìí¼Ó</a>
									<a href="#" onclick="toDelete()">É¾³ý°</a>&nbsp;&nbsp;&nbsp;
		  						</td>                       
        						</tr>
        						
							<tr>
								<td colspan="2">
									<table cellspacing="1" cellpadding="0" width="100%" >
	  									<tr class="microsoftLook0" height=28>
      											<th width='5%'><INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()">ÐòºÅ</th>				
      											<th width='5%'>×´Ì¬</th>
      											<th width='10%'>Ãû³Æ</th>
      											<th width='10%'>IPµØÖ·</th> 
      											<th width='20%'>ÐÍºÅ</th>     
      											<th width='10%'>½Ó¿ÚÁÐ±í</th>
      											
      											
      											<th width='5%'>²Ù×÷</th>
										</tr>
<%
    HostNode vo = null;
    int startRow = jp.getStartRow();
    for(int i=0;i<rc;i++)
    {
       vo = (HostNode)list.get(i);
       Host host = (Host)PollingEngine.getInstance().getNodeByID(vo.getId());

          
%>
   										<tr bgcolor="DEEBF7" <%=onmouseoverstyle%> class="microsoftLook">  
    											<td width='5%'>&nbsp;&nbsp;<INPUT type="checkbox" class=noborder name=checkbox value="<%=vo.getId()%>">
    												<font color='blue'><%=startRow + i%></font></td>
    												
    											<td  align='center' >
    											<%
    												if(host.isAlarm()){
    											%>
    												<img src="<%=rootPath%>/resource/image/topo/alert.gif" border="0" width=15/>&nbsp;¸æ¾¯
    											<%
    												}else{
    											%>
    												<img src="<%=rootPath%>/resource/image/topo/status_ok.gif" border="0"/>&nbsp;Õý³£
    											<%
    												}
    											%>
    											</td>
    											<td  align='center' style="cursor:hand"><a href="<%=rootPath%>/detail/dispatcher.jsp?id=net<%=vo.getId()%>"><%=vo.getAlias()%></a></td>
    											<td  align='center'><%=vo.getIpAddress()%></td>
    											<td  align='center'><%=vo.getType()%></td>
    											<td  align='left'><a href="<%=rootPath%>/detail/dispatcher.jsp?id=<%=vo.getId()%>">½Ó¿ÚÁÐ±í</a></td>
											
											
        										<td ><!--<a href="<%=rootPath%>/network.do?action=ready_edit&id=<%=vo.getId()%>">-->
												<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('2','<%=vo.getId()%>','<%=vo.getIpAddress()%>')></td>
  										</tr>			
<% }%>
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
