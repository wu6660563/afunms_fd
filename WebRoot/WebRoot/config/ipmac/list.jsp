<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.polling.om.IpMac"%>
<%@ page import="com.afunms.system.model.Department"%>
<%@ page import="com.afunms.system.dao.DepartmentDao"%>
<%@ page import="com.afunms.config.model.Employee"%>
<%@ page import="com.afunms.polling.om.IpMacBase"%>
<%@ page import="com.afunms.config.dao.EmployeeDao"%>
<%@ page import="com.afunms.topology.dao.IpMacBaseDao"%>
<%
  String rootPath = request.getContextPath();
  List list = (List)request.getAttribute("list");
  
  int rc = list.size();

  JspPage jp = (JspPage)request.getAttribute("page");
  IpMacBaseDao dao = new IpMacBaseDao(); 
  List macbaselist = dao.loadAll();
  Hashtable macbaseHash = new Hashtable();
  if(macbaselist != null&& macbaselist.size()>0){
  	for(int i=0;i<macbaselist.size();i++){
  		IpMacBase ipmacbase = (IpMacBase)macbaselist.get(i);
  		macbaseHash.put(ipmacbase.getMac()+":"+ipmacbase.getRelateipaddr(),ipmacbase);
  	}
  }
  dao.close();
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
  var delAction = "<%=rootPath%>/ipmac.do?action=delete";
  var listAction = "<%=rootPath%>/ipmac.do?action=list";
  
  function doQuery()
  {  
     if(mainForm.key.value=="")
     {
     	alert("请输入查询条件");
     	return false;
     }
     mainForm.action = "<%=rootPath%>/ipmac.do?action=find";
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
  
  function toDeleteAll()
  {
      mainForm.action = "<%=rootPath%>/ipmac.do?action=deleteall";
      mainForm.submit();
  }
    function toSetBaseline()
  {
      mainForm.action = "<%=rootPath%>/ipmac.do?action=setlistbaseline";
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
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa"  onload="initmenu();">
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
                    <td class="layout_title"><b>资源 >> IP/MAC资源 >> IP/MAC</b></td>
                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                  </tr>
              </table>
			  </td>
			  </tr>
				
				<tr>           
								<td>
								<table width="100%" cellpadding="0" cellspacing="1" >
								<tr>
								<td>
								<table width="100%" cellpadding="0" cellspacing="0" >
								<tr>
								<td bgcolor="#ECECEC" width="50%" align='left'>
				&nbsp;&nbsp;&nbsp;&nbsp;<B>查询:</B>
        								<SELECT name="key" style="width=100"> 
          									<OPTION value="relateipaddr" selected>网络设备IP</OPTION>
          									<OPTION value="ipaddress">IP地址</OPTION>
          									<OPTION value="mac">MAC</OPTION>          
          									
          								</SELECT>&nbsp;<b>=</b>&nbsp; 
          								<INPUT type="text" name="value" width="15" class="formStyle">
          								<INPUT type="button" class="formStyle" value="查询" onclick=" return doQuery()">
          							</td> 
          							<td bgcolor="#ECECEC" width="50%" align='right'>
									<a href="#" onclick="toSetBaseline()">设为基线</a>&nbsp;&nbsp;&nbsp;
									<a href="#" onclick="toDelete()">删除</a>&nbsp;&nbsp;&nbsp;
									<a href="#" onclick="toDeleteAll()">删除全部</a>&nbsp;&nbsp;&nbsp;
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
		<td align=right bgcolor="#ECECEC">
		<a href="<%=rootPath%>/ipmac.do?action=downloadipmacreport" target="_blank"><img name="selDay1" alt='导出EXCEL' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=18  border="0">导出EXCEL</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="<%=rootPath%>/ipmac.do?action=downloadipmacreportall" target="_blank"><img name="selDay1" alt='导出EXCEL' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=18  border="0">导出全部EXCEL</a>&nbsp;&nbsp;&nbsp;&nbsp;
		
		</td>
		</tr>	
        						
							<tr>
								<td colspan="2">
									<table cellspacing="1" cellpadding="0" width="100%">
	  									<tr class="microsoftLook0" height=25>
      											<th width='5%'><INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()">序号</th>				
      											<th width='20%'>网络设备(ip)</th>
      											<th width='5%'>端口</th>
      											<th width='15%'>IP地址</th>      
      											<th width='20%'>MAC</th>
      											<th width='10%'>基线</th>
      											
										</tr>
<%
    IpMac vo = null;
    int startRow = jp.getStartRow();
    session.setAttribute("startRow",startRow);
    session.setAttribute("list",list);
    for(int i=0;i<rc;i++)
    {
       vo = (IpMac)list.get(i);

          
%>
   										<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>>  
    											<td ><INPUT type="checkbox" class=noborder name=checkbox value="<%=vo.getId()%>">
    												<font color='blue'><%=startRow + i%></font></td>
    											<td  align='center' style="cursor:hand"><%=vo.getRelateipaddr()%></td>
    											<td  align='center'><%=vo.getIfindex()%></td>
    											<td  align='center'><%=vo.getIpaddress()%></td>
    											<td  align='center'><%=vo.getMac()%></td>
    											<%
    												int baseflag = 0;
    												String band="<a href="+rootPath+"/ipmac.do?action=update&id="+vo.getId()+"&ifband=0>取消绑定</a>";
    												if(vo.getIfband().equals("0")){
    													band="<a href="+rootPath+"/ipmac.do?action=update&id="+vo.getId()+"&ifband=1>绑定</a>";
    												}
    												String sms = "<a href="+rootPath+"/ipmac.do?action=update&id="+vo.getId()+"&ifsms=0>取消发送</a>";
    												if(vo.getIfsms().equals("0")){
    													sms="<a href="+rootPath+"/ipmac.do?action=update&id="+vo.getId()+"&ifsms=1>发送</a>";
    												}
    												if(macbaseHash != null && macbaseHash.size()>0){
    													if(macbaseHash.containsKey(vo.getMac()+":"+vo.getRelateipaddr())){
    														baseflag = 1;
    													}
    												}
    												if(baseflag == 0){
    											%>
    											<td  align='left' bgcolor=#99CC00><a href="<%=rootPath%>/ipmac.do?action=setmacbase&relateip=<%=vo.getRelateipaddr()%>&ifindex=<%=vo.getIfindex()%>&macip=<%=vo.getIpaddress()%>&mac=<%=vo.getMac()%>"><font color=blue>设为基线</font></a></td>
    											<%
    												}else{
    											%>
    											<td  align='left'><a href="<%=rootPath%>/ipmac.do?action=cancelmacbase&relateip=<%=vo.getRelateipaddr()%>&index=<%=vo.getIfindex()%>&ip=<%=vo.getIpaddress()%>&mac=<%=vo.getMac()%>"><font color=blue>取消基线</font></a></td>
    											<%
    												}
    											%>
        										
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
