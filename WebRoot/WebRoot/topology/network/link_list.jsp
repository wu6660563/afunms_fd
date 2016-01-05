<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.Link"%>
<%@page import="java.util.List"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.topology.model.*"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%
  String rootPath = request.getContextPath();

  List list = (List)request.getAttribute("list");
  
  int rc = list.size();

  JspPage jp = (JspPage)request.getAttribute("page"); 
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>  
<script language="javascript">	
  var curpage= <%=jp.getCurrentPage()%>;
  var totalpages = <%=jp.getPageTotal()%>;
  var delAction = "<%=rootPath%>/link.do?action=delete";
  var listAction = "<%=rootPath%>/link.do?action=list";
    
  function toAdd()
  {
      mainForm.action = "<%=rootPath%>/link.do?action=ready_add";
      mainForm.submit();
  }
    function toEdit()
  {
      mainForm.action = "<%=rootPath%>/link.do?action=ready_edit";
      mainForm.submit();
  }
     function toDelete2()
  {
      mainForm.action = "<%=rootPath%>/link.do?action=delete";
      mainForm.submit();
  }
    function toEditAll()
  {
      mainForm.action = "<%=rootPath%>/link.do?action=editall";
      mainForm.submit();
  }
</script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">

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
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa"   onload="initmenu();">
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
                    <td class="layout_title"><b>资源 >> 设备维护 >> 链路信息</b></td>
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
					            		<a href="<%=rootPath%>/link.do?action=downloadlinklist" target="_blank"><img name="selDay1" alt='导出EXCEL' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=18  border="0">导出EXCEL</a>&nbsp;&nbsp;&nbsp;&nbsp;
								<a href="#" onclick="toAdd()">添加</a>
								<a href="#" onclick="toEdit()">修改</a>
								<a href="#" onclick="toDelete2()">删除</a>
								<a href="#" onclick="toEditAll()">批量设置阀值</a>&nbsp;&nbsp;&nbsp;&nbsp;
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
						<tr>
							<td colspan="2">
								<table cellspacing="1" cellpadding="0" width="100%">
	  								<tr class="microsoftLook0" height=28>

      										<th width='5%'><INPUT type="checkbox" class=noborder name="checkall"
																onclick="javascript:chkall()" class=noborder>序号</th>				
      										<th width='15%'>起点设备</th>
      										<th width='20%'>起点端口</th>      
      										<th width='15%'>终点设备</th>
      										<th width='20%'>终点端口</th>
											<th width='10%'>流量阀值（KB/S）</th>
											<th width='15%'>带宽利用率阀值(%)</th>
									</tr>									
<%
    Link vo = null;
    int startRow = jp.getStartRow();
  for(int i=0;i<list.size();i++)
  {
     vo = (Link)list.get(i); 
     		HostNode node = new HostNode();
		HostNodeDao dao = new HostNodeDao();
		
		node = dao.loadHost(vo.getStartId());
     		HostNode endnode = new HostNode();
		dao = new HostNodeDao();
		
		endnode = dao.loadHost(vo.getEndId());		
		         
%>
   <tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> class="microsoftLook" height=25> 
    	<td  align='center'><INPUT type="checkbox" class=noborder name=checkbox value="<%=vo.getId()%>" class=noborder>
    	<font color='blue'><%=startRow + i%></font></td>
    	<td  align='center'><%=node.getAlias()%></td></a>
    	<td  align='left'>
<%
    out.print("IP地址:");
    out.print(vo.getStartIp());
    out.print(" 索引:");
    out.print(vo.getStartIndex());
%></td>
    	<td  align='center'><%=endnode.getAlias()%></td>
		<td align='left'>
<%
    out.print("IP地址:");
    out.print(vo.getEndIp());
    out.print(" 索引:");
    out.print(vo.getEndIndex());
%>
</td>
<td  align='center'><%=vo.getMaxSpeed()%></td>
<td  align='center'><%=vo.getMaxPer()%></td>
</tr>	
<% }%>
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
