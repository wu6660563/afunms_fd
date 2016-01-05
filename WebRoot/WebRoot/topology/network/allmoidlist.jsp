<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>
<%@page import="java.util.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%@page import="com.afunms.topology.model.*"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.topology.dao.*"%>
<%
  String rootPath = request.getContextPath();
  List moidlist = (List)request.getAttribute("list");
  List nodelist = (List)request.getAttribute("nodelist");
  
  int rc = moidlist.size();

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
  var listAction = "<%=rootPath%>/moid.do?action=allmoidlist";
  
  function doQuery()
  {  
     mainForm.action = "<%=rootPath%>/moid.do?action=allmoidsearch";
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
<!-- 这里用来定义需要显示的右键菜单 -->
	<div id="itemMenu" style="display: none";>
	<table border="1" width="100%" height="100%" bgcolor="#cccccc"
		style="border: thin;font-size: 12px" cellspacing="0">
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.create()">查看状态</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.update();">ping</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.del()">traceroute</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.clickMenu()">关闭</td>
		</tr>
	</table>
	</div>
	<!-- 右键菜单结束-->
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
                    <td class="layout_title"><b>资源 >> 性能监视 >> 全局指标阀值一览</b></td>
                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                  </tr>
              </table>
			  </td>
			  </tr>
				<tr>
		
					<tr>           
								<td>
								<table width="100%" cellpadding="0" cellspacing="1" >
        			<tr>
					            <td bgcolor="#ECECEC" width="70%" align='left'>&nbsp;&nbsp;&nbsp;<B>查询:</B>
        								<SELECT name="ipaddress" style="width=200"> 
        								<%
        									if(nodelist != null && nodelist.size()>0){
        										for(int k=0;k<nodelist.size();k++){
        											HostNode _vo = (HostNode)nodelist.get(k);	
        								%>
          									<OPTION value="<%=_vo.getIpAddress()%>"><%=_vo.getAlias()%>(<%=_vo.getIpAddress()%>)</OPTION>
          								<%
          										}
          									}
          								%>
          								</SELECT>&nbsp;&nbsp; 
          								<INPUT type="button" class="formStyle" value="查询" onclick=" return doQuery()">
          							</td>
          							<td width=30% bgcolor="#ECECEC">
          							<a href="<%=rootPath%>/moid.do?action=downloadnetworklistfuck" target="_blank"><img name="selDay1" alt='导出EXCEL' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=18  border="0">导出EXCEL</a>&nbsp;&nbsp;&nbsp;&nbsp;
          							<a href="<%=rootPath%>/moid.do?action=downloadnetworklistdoc" target="_blank"><img name="selDay1" alt='导出EXCEL' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_word.gif" width=18  border="0">导出WORD</a>&nbsp;&nbsp;&nbsp;&nbsp;
          							<a href="<%=rootPath%>/moid.do?action=downloadnetworkpdf" target="_blank"><img name="selDay1" alt='导出EXCEL' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_pdf.gif" width=18  border="0">导出PDF</a>&nbsp;&nbsp;&nbsp;&nbsp;
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
									<table cellspacing="1" cellpadding="0" width="100%">
  						<tr align="center" height=28 class="microsoftLook0"> 
  							<td width="20%" align="center"><strong>IP</strong></td>
    							<td  width="10%" align="center"><strong>指标名称</strong></td>
    							<td  width="6%" align="center"><strong>监视</strong></td>
    							<!--<td  width="10%" align="left" bgcolor=#397dbd><font color=#ffffff>监视类型</font></td>-->
    							<td  width="10%" align="center"><strong>一级阀值(普通)</strong></td>
    							<td  width="5%" align="center"><strong>次数</strong></td>
    							<td  width="5%" align="center"><strong>告警</strong></td>
    							<td  width="10%" align="center"><strong>二级阀值(严重)</strong></td>
    							<td  width="5%" align="center"><strong>次数</strong></td>
    							<td  width="5%" align="center"><strong>告警</strong></td>
    							<td  width="10%" align="center"><strong>三级阀值(紧急)</strong></td>
    							<td  width="5%" align="center"><strong>次数</strong></td>
    							<td  width="5%" align="center"><strong>告警</strong></td>  
    							<td  width="5%" align="center"><strong>修改</strong></td>  							    							
  						</tr>
             <%
             if (moidlist != null){
             for(int i=0 ;i<moidlist.size(); i++){
             	NodeMonitor nm = (NodeMonitor)moidlist.get(i); 
             	HostNode vo = new HostNode();
		HostNodeDao dao = new HostNodeDao();
		vo = dao.loadHost(nm.getNodeID()); 
		String enable = "是";
		if(!nm.isEnabled())enable="否";
		if(vo == null)continue; 
		dao.close();
		String sms0="是";
		if(nm.getSms0()==0)sms0="否";
		String sms1="是";
		if(nm.getSms1()==0)sms1="否";
		String sms2="是";
		if(nm.getSms2()==0)sms2="否";
             %>
            					<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=25>           					
            						<td>&nbsp;<%=vo.getAlias()%>(<%=nm.getIp()%>)</td>         						
            						<td>&nbsp;<%=nm.getDescr()%></td>
            						<td>&nbsp;<%=enable%></td>
            						<td>&nbsp;<%=nm.getLimenvalue0()%>&nbsp;<%=nm.getUnit()%></td>
            						<td>&nbsp;<%=nm.getTime0()%></td>
            						<td>&nbsp;<%=sms0%></td>
            						<td>&nbsp;<%=nm.getLimenvalue1()%>&nbsp;<%=nm.getUnit()%></td>
            						<td>&nbsp;<%=nm.getTime1()%></td>
            						<td>&nbsp;<%=sms1%></td>
            						<td>&nbsp;<%=nm.getLimenvalue2()%>&nbsp;<%=nm.getUnit()%></td>
            						<td>&nbsp;<%=nm.getTime1()%></td>
            						<td>&nbsp;<%=sms2%></td>
            						<td>&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/moid.do?action=showeditmoids&moid=<%=nm.getId()%>","onetelnet", "height=400, width= 500, top=200, left= 200")'>
            						<img src="<%=rootPath%>/resource/image/editicon.gif" border=0></a></td>
            					</tr>
            <%}
            }
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
