<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.io.File"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.config.dao.*"%>
<%@page import="com.afunms.config.model.*"%>
<%
  String rootPath = request.getContextPath();
  List list = (List)request.getAttribute("list");
  int rc = list.size();
  JspPage jp = (JspPage)request.getAttribute("page");
  String path = rootPath+"/panel/view/image/";
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

<link rel="stylesheet" type="text/css" href="<%=rootPath%>/resource/css/examples.css" />
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/resource/css/chooser.css" />
<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8"/>
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>

<script language="javascript">	
Ext.onReady(function() { 
	Ext.QuickTips.init();
});
</script>

<script language="javascript">	
  var curpage= <%=jp.getCurrentPage()%>;
  var totalpages = <%=jp.getPageTotal()%>;
  var listAction = "<%=rootPath%>/panel.do?action=panelmodellist";
  

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
		<td align="center" valign=top bgcolor="#ababab">
			<table width="98%"  cellpadding="0" cellspacing="0" algin="center">
			<tr>
				<td background="<%=rootPath%>/common/images/right_t_02.jpg" width="100%"><table width="100%" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
                    <td class="layout_title"><b> 资源>> 设备面板配置管理 >> 面板模板编辑</b></td>
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
								<a href="<%=rootPath%>/panel.do?action=showaddpanel">编辑</a>&nbsp;&nbsp;</td>
				</td>
									</tr>
        								</table>
										</td>
        						</tr>				
	<tr>
							<td>
							<table width="100%" cellpadding="0" cellspacing="1" >
							<tr>
						    <td bgcolor="#ECECEC" width="80%" align="center">
    <jsp:include page="../../common/page.jsp">
     <jsp:param name="curpage" value="<%=jp.getCurrentPage()%>"/>
     <jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>"/>
   </jsp:include>
        </td>
        </tr>
		</table>
		</td>
		</tr>  						
							<tr>
								<td colspan="2">
									<table cellspacing="1" cellpadding="0" width="100%" >
	  									<tr class="microsoftLook0" height=28>
      											<td width='10%' align="center"><b><INPUT type="checkbox" class="noborder" name="checkall" onclick="javascript:chkall()">序号</b></td>			
      											<td width='20%' align="center"><b>模板oid</b></td>
      											<td width='20%' align="center"><b>模板类型</b></td> 
      											<td width='15%' align="center"><b>模板高度</b></td>     
      											<td width='15%' align="center"><b>模板宽度</b></td> 
      											<td width='20%' align="center"><b>模板图片</b></td>     											
										</tr>
										<%
											int startRow = jp.getStartRow();
										    for(int i=0;i<rc;i++)
										    {
										       PanelModel panelModel = (PanelModel)list.get(i);
										%>
   										<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> class="microsoftLook" height=28>  
    											<td width='5%'>&nbsp;&nbsp;<INPUT type="checkbox" class=noborder name=checkbox value="<%=panelModel.getId()%>" class=noborder>
    												<font color='blue'><%=startRow + i%></font></td>
    											<td  align='center'><font color="#397DBD"><%=panelModel.getOid()%></font></td>
    											<td  align='center'><%=panelModel.getImageType()%></td>
    											<td  align='center'><%=panelModel.getHeight()%></td>
    											<td  align='center'><font color="#397DBD"><%=panelModel.getWidth()%></font></td>
												<td  align='center' ext:qtitle="" ext:qtip='<img src="<%=path%><%=panelModel.getOid().replace(".","-")%>_<%=panelModel.getImageType()%>.jpg" width="250">'>模板图片</td>
  										</tr>	
  										<%} %>		
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
