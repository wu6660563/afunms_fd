<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.sysset.model.DeviceType"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.sysset.util.DeviceTypeView"%>
<%@ include file="/include/globe.inc"%>
<%
  List list = (List)request.getAttribute("list");
  int rc = list.size();

  JspPage jp = (JspPage)request.getAttribute("page");  
  String rootPath = request.getContextPath();  
  
  DeviceTypeView view = new DeviceTypeView();
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
  var delAction = "<%=rootPath%>/devicetype.do?action=delete";
  var listAction = "<%=rootPath%>/devicetype.do?action=list";
  
  function toAdd()
  {
     mainForm.action = "<%=rootPath%>/devicetype.do?action=ready_add";
     mainForm.submit();
  }
  
  function doFind()
  {
     mainForm.action = "<%=rootPath%>/devicetype.do?action=find";
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
                    <td class="layout_title">系统管理 >> 资源管理 >> 设备型号</td>
                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                  </tr>
              </table>
        						<tr>           
								<td>
								<table width="100%" cellpadding="0" cellspacing="1" >
								<tr>
								<td>
								<table width="100%" cellpadding="0" cellspacing="0" >
								<tr>
								<td bgcolor="#ECECEC" width="50%" align='left'>
								&nbsp;&nbsp;&nbsp;&nbsp;按[<b>系统OID</b>]查询&nbsp;<input type="text" name="sys_oid" size="30" class="formStyle">
        							<input type="button" value="查询" style="width:60" class="button" onclick="doFind()">
        						</td>
        					<td bgcolor="#ECECEC" width="50%" align='right'>
			<a href="#" onclick="toAdd()">添加</a>
			<a href="#" onclick="toDelete()">删除</a>&nbsp;&nbsp;&nbsp;
					</td>
										</tr>
								</table>
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
								<tr class="microsoftLook0" height=28>
									<th width='22'><INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()"></th>
    									<th width='30'>序号</th>
    									<th width='80'>图例</th>
    									<th width='100'>描述</th>
    									<th width='100'>系统OID</th>
    									<th width='100'>生产商</th>
    									<th width='100'>分类</th>
    									<th width='30'>编辑</th>
								</tr>
<%
    DeviceType vo = null;
    int startRow = jp.getStartRow();
    for(int i=0;i<rc;i++)
    {
       vo = (DeviceType)list.get(i);
%>
       		<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> class="microsoftLook" height=25>
    		<td ><INPUT type="checkbox" class=noborder name=checkbox value="<%=vo.getId()%>"></td>
    		<td ><font color='blue'><%=startRow + i%></font></td>
		<td ><img src="<%=rootPath%>/resource/image/device/<%=vo.getImage()%>"></td>
		<td ><%=vo.getDescr()%></td> 				
		<td ><%=vo.getSysOid()%></td>			
   		<td ><%=view.getProducer(vo.getProducer())%></td>
		<td ><%=view.getCategory(vo.getCategory())%></td>
		<td ><a href="<%=rootPath%>/devicetype.do?action=ready_edit&id=<%=vo.getId()%>">
		<img src="<%=rootPath%>/resource/image/editicon.gif" border="0"/></a></td>
  	</tr>
<%}%>  	  				
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
