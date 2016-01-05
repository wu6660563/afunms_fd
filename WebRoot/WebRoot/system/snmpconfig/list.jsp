<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.system.model.*"%>
<%
  String rootPath = request.getContextPath();
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
  
  function doQuery()
  {  
	window.location.reload();
  }
  
    function doAdd()
  {  
     mainForm.action = "<%=rootPath%>/snmp.do?action=ready_add";
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
  
// 全屏观看
function gotoFullScreen() {
	parent.mainFrame.resetProcDlg();
	var status = "toolbar=no,height="+ window.screen.height + ",";
	status += "width=" + (window.screen.width-8) + ",scrollbars=no";
	status += "screenX=0,screenY=0";
	window.open("topology/network/index.jsp", "fullScreenWindow", status);
	parent.mainFrame.zoomProcDlg("out");
}

</script>
<script language="JavaScript" type="text/JavaScript">
var show = true;
var hide = false;
//修改菜单的上下箭头符号
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
//添加菜单	
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
                    <td class="layout_title">系统管理 >> 资源管理 >> SNMP设置</td>
                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                  </tr>
              </table>
			  </td>
			  </tr>
        						<tr>           
								<td>
								<table width="100%" cellpadding="0" cellspacing="1" >
        			<tr>
					            <td bgcolor="#ECECEC" width="100%" align='right'>
					&nbsp;&nbsp;<INPUT type="button" class="formStyle" value="添 加" onclick=" return doAdd()">
					&nbsp;&nbsp;<INPUT type="button" class="formStyle" value="刷 新" onclick=" return doQuery()">
					</tr>
        								</table>
										</td>
        						</tr>						
				<tr>
					<td colspan="2">
						<table cellspacing="1" cellpadding="0" width="100%">
	  						<tr class="microsoftLook0" height=28>
      											<th width='15%'>名称</a></th>				
      											<th width='15%'>版本</th>
      											<th width='15%'>读团体</th>
      											<th width='15%'>写团体</th>  
      											<th width='10%'>超时时间(秒)</th> 
      											<th width='10%'>重试次数</th>  
      											<th width='10%'>修改</th>
      											
      											<th width='10%'>删除</th>
										</tr>
<%
    SnmpConfig vo = null;
    for(int i=0;i<list.size();i++)
    {
       vo = (SnmpConfig)list.get(i);
       String version = "";
       if(vo.getSnmpversion() == 0){
       		version = "SNMP V1";
       }else{
       		version = "SNMP V2c";
       }
       //Host host = (Host)PollingEngine.getInstance().getNodeByID(vo.getId());

          
%>
  										<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> class="microsoftLook" height=25>  
    												
    											<td  align='center'><%=vo.getName()%></td>
    											<td  align='center'><%=version%></a></td>
    											<td  align='center'><%=vo.getReadcommunity()%></td>
    											<td  align='center'><%=vo.getWritecommunity()%></td>
    											<td  align='center'><%=vo.getTimeout()%></td>
    											<td  align='center'><%=vo.getTrytime()%></td>
    											<td  align='center'><a href="<%=rootPath%>/snmp.do?action=ready_edit&id=<%=vo.getId()%>">修改</a></td>
    											<td  align='center'><a href="<%=rootPath%>/snmp.do?action=delete&id=<%=vo.getId()%>">删除</a></td>
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
</form>
</BODY>
</HTML>
