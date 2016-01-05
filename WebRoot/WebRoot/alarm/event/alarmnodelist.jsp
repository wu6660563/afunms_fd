<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>
<%@ page import="com.afunms.event.model.EventList"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.topology.model.*"%>
<%@page import="com.afunms.topology.util.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.base.*"%>
<%@page import="com.afunms.polling.node.*"%>
<%
  String rootPath = request.getContextPath();
  JspPage jp = (JspPage)request.getAttribute("page");
  
 	    List pollnodes = PollingEngine.getInstance().getNodeList();
 	    List alarmlist = new ArrayList();
 	    if(pollnodes != null && pollnodes.size()>0){
 	    	for(int k=0;k<pollnodes.size();k++){
 	    	System.out.println(pollnodes.get(k).toString());
 	    		Node node = (Node)pollnodes.get(k);
 	    		if(node.isAlarm())alarmlist.add(node);
 	    	}
 	    }    
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
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 
<script language="javascript">	
  var delAction = "<%=rootPath%>/event.do?action=delete";
  var listAction = "<%=rootPath%>/event.do?action=list";

  function query()
  {  
     mainForm.action = "<%=rootPath%>/event.do?action=list&jp=1";
     mainForm.submit();
  }  
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

  function toAdd()
  {
      mainForm.action = "<%=rootPath%>/network.do?action=ready_add";
      mainForm.submit();
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
                    <td class="layout_title"><b>�澯 >> �澯��� >> ���ڸ澯���豸</b></td>
                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                  </tr>
              </table>
			  </td>
			  </tr>			
      <tr>
								<td colspan="2">
  <table cellSpacing="1"  cellPadding="0" border=0 width=100%>  						
							
<%
    	Host host = null;
    	Host host1 = null;
    	Host host2 = null;
    	Host host3 = null;
    	NodeHelper helper = new NodeHelper();
    	int size = alarmlist.size();
  	for(int i=0;i<alarmlist.size();i++){
  		String imgsrc = "";
  		String imgsrc1 = "";
  		String imgsrc2 = "";
  		String imgsrc3 = "";
  		String namestr = "";
  		String namestr1 = "";
  		String namestr2 = "";
  		String namestr3 = "";
  		if(alarmlist.get(i) != null){
  			host = (Host)alarmlist.get(i);
  			imgsrc = "<img src="+rootPath+"/resource/"+helper.getAlarmImage(host.getCategory())+">";
  			namestr = "<a href="+rootPath+"/detail/dispatcher.jsp?id=net"+host.getId()+">"+host.getAlias()+"("+host.getIpAddress()+")<a>";
  		}
  		if(i+1 < size){  		  		
  		if(alarmlist.get(i+1) != null){
  			host1 = (Host)alarmlist.get(i+1);
  			imgsrc1 = "<img src="+rootPath+"/resource/"+helper.getAlarmImage(host1.getCategory())+">";
  			namestr1 = "<a href="+rootPath+"/detail/dispatcher.jsp?id=net"+host1.getId()+">"+host1.getAlias()+"("+host1.getIpAddress()+")<a>";
  		}
  		}
  		if(i+2 < size){ 
  		if(alarmlist.get(i+2) != null){
  			host2 = (Host)alarmlist.get(i+2);
  			imgsrc2 = "<img src="+rootPath+"/resource/"+helper.getAlarmImage(host2.getCategory())+">";
  			namestr2 = "<a href="+rootPath+"/detail/dispatcher.jsp?id=net"+host2.getId()+">"+host2.getAlias()+"("+host2.getIpAddress()+")<a>";
  		} 
  		}
  		if(i+3 < size){  
  		if(alarmlist.get(i+3) != null){
  			host3 = (Host)alarmlist.get(i+3);
  			imgsrc3 = "<img src="+rootPath+"/resource/"+helper.getAlarmImage(host3.getCategory())+">";
  			namestr3 = "<a href="+rootPath+"/detail/dispatcher.jsp?id=net"+host3.getId()+">"+host3.getAlias()+"("+host3.getIpAddress()+")<a>";
  		}
  		}
  		i=i+3;   				
 		  			
%>

	
 <tr  class="microsoftLook0" height=25>

    	<td width="20%" height=80 align=center><%=imgsrc%><br><%=namestr%></td>
       	<td width="20%" height=80 align=center><%=imgsrc1%><br><%=namestr1%></td>
       	<td width="20%" height=80 align=center><%=imgsrc2%><br><%=namestr2%></td>
      	<td width="20%" height=80 align=center><%=imgsrc3%><br><%=namestr3%></td>
 </tr>
 <%}

 %>  
   
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
</form>
</BODY>
</HTML>
