<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.polling.*"%>
<%@ page import="com.afunms.polling.om.*"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.om.IpMacBase"%>
<%@ page import="com.afunms.system.model.Department"%>
<%@ page import="com.afunms.system.dao.DepartmentDao"%>
<%@ page import="com.afunms.config.model.Employee"%>
<%@ page import="com.afunms.config.dao.EmployeeDao"%>
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
  var delAction = "<%=rootPath%>/ipmacbase.do?action=delete";
  var listAction = "<%=rootPath%>/ipmacbase.do?action=list";
  
  function doQuery()
  {  
     if(mainForm.key.value=="")
     {
     	alert("请输入查询条件");
     	return false;
     }
     mainForm.action = "<%=rootPath%>/ipmacbase.do?action=find";
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
      mainForm.action = "<%=rootPath%>/ipmacbase.do?action=deleteall";
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
<script language="JavaScript">

	//公共变量
	var node="";
	var ipaddress="";
	var operate="";
	/**
	*根据传入的id显示右键菜单
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
	        popMenu(itemMenu,100,"111111");
	    }
	    event.returnValue=false;
	    event.cancelBubble=true;
	    return false;
	}
	/**
	*显示弹出菜单
	*menuDiv:右键菜单的内容
	*width:行显示的宽度
	*rowControlString:行控制字符串，0表示不显示，1表示显示，如“101”，则表示第1、3行显示，第2行不显示
	*/
	function popMenu(menuDiv,width,rowControlString)
	{
	    //创建弹出菜单
	    var pop=window.createPopup();
	    //设置弹出菜单的内容
	    pop.document.body.innerHTML=menuDiv.innerHTML;
	    var rowObjs=pop.document.body.all[0].rows;
	    //获得弹出菜单的行数
	    var rowCount=rowObjs.length;
	    //alert("rowCount==>"+rowCount+",rowControlString==>"+rowControlString);
	    //循环设置每行的属性
	    for(var i=0;i<rowObjs.length;i++)
	    {
	        //如果设置该行不显示，则行数减一
	        var hide=rowControlString.charAt(i)!='1';
	        if(hide){
	            rowCount--;
	        }
	        //设置是否显示该行
	        rowObjs[i].style.display=(hide)?"none":"";
	        //设置鼠标滑入该行时的效果
	        rowObjs[i].cells[0].onmouseover=function()
	        {
	            this.style.background="#397DBD";
	            this.style.color="white";
	        }
	        //设置鼠标滑出该行时的效果
	        rowObjs[i].cells[0].onmouseout=function(){
	            this.style.background="#F1F1F1";
	            this.style.color="black";
	        }
	    }
	    //屏蔽菜单的菜单
	    pop.document.oncontextmenu=function()
	    {
	            return false; 
	    }
	    //选择右键菜单的一项后，菜单隐藏
	    pop.document.onclick=function()
	    {
	        pop.hide();
	    }
	    //显示菜单
	    pop.show(event.clientX-1,event.clientY,width,rowCount*25,document.body);
	    return true;
	}
	function detail()
	{
	    location.href="<%=rootPath%>/detail/dispatcher.jsp?id="+node;
	}
	function edit()
	{
		location.href="<%=rootPath%>/ipmacbase.do?action=ready_edit&id="+node;
		//window.open('/nms/netutil/ping.jsp?ipaddress='+ipaddress);
	}
	function cancelmanage()
	{
		location.href="<%=rootPath%>/network.do?action=menucancelmanage&id="+node;
	}
	function addmanage()
	{
		location.href="<%=rootPath%>/network.do?action=menuaddmanage&id="+node;
	}
	function cancelendpoint()
	{
		location.href="<%=rootPath%>/network.do?action=menucancelendpoint&id="+node;
	}
	function addendpoint()
	{
		location.href="<%=rootPath%>/network.do?action=menuaddendpoint&id="+node;
	}		
	function clickMenu()
	{
	}
</script>
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa" onload="initmenu();">
<!-- 这里用来定义需要显示的右键菜单 -->
	<div id="itemMenu" style="display: none";>
	<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
		style="border: thin;font-size: 12px" cellspacing="0">
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.edit()">编辑</td>
		</tr>				
	</table>
	</div>
	<!-- 右键菜单结束-->
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
                    <td class="layout_title"><b>资源 >> IP/MAC资源 >> IP/MAC基线</b></td>
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
								<td colspan="2">
									<table cellspacing="1" cellpadding="0" width="100%">
	  									<tr class="microsoftLook0" height=25>
      											<th width='5%'><INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()">序号</th>				
      											<th width='10%'>网络设备(ip)</th>
      											<th width='5%'>端口</th>
      											<th width='10%'>IP地址</th>      
      											<th width='12%'>MAC</th>
      											<th width='6%'>基线</th>
      											<th width='6%'>IP-MAC</th>
      											<th width='6%'>端口-MAC</th>
      											<th width='8%'>IP-端口-MAC</th>
      											<th width='6%'>发送短信</th>
      											<th width='6%'>电话告警</th>
      											<th width='6%'>邮件告警</th>
      											<th width='10%'>用户</th>
      											<th width='4%'>操作</th>
      											
										</tr>
<%
    IpMacBase vo = null;
    int startRow = jp.getStartRow();
    for(int i=0;i<rc;i++)
    {
       vo = (IpMacBase)list.get(i);
       Host host = (Host)PollingEngine.getInstance().getNodeByIP(vo.getRelateipaddr());
       if(host == null)continue;

          
%>
   										<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>>  
    											<td ><INPUT type="checkbox" class=noborder name=checkbox value="<%=vo.getId()%>">
    												<font color='blue'><%=startRow + i%></font></td>
    											<td  align='center' style="cursor:hand"><a href="<%=rootPath%>/detail/dispatcher.jsp?id=<%=host.getId()%>"><%=vo.getRelateipaddr()%></a></td>
    											<td  align='center'><%=vo.getIfindex()%></td>
    											<td  align='center'><%=vo.getIpaddress()%></td>
    											<td  align='center'><%=vo.getMac()%></td>
    											<%
    												int employee_id = vo.getEmployee_id();
    												String namestr ="";
    												EmployeeDao emdao = new EmployeeDao();
    												Employee vo_ployee =(Employee)emdao.findByID(employee_id+"");
    												if(vo_ployee != null){
    													DepartmentDao deptdao = new DepartmentDao();
  													Department dept = (Department)deptdao.findByID(vo_ployee.getId()+"");
  													deptdao.close();
  													namestr = vo_ployee.getName()+"("+dept.getDept()+")";
  												}
    												int ifband = vo.getIfband();
    												//String macbase="<a href="+rootPath+"/ipmacbase.do?action=update&id="+vo.getId()+"&ifband=0>取消</a>";
    												//String ipmac="<a href="+rootPath+"/ipmacbase.do?action=update&id="+vo.getId()+"&ifband=0>取消</a>";
    												
    												
    												String sms = "<a href="+rootPath+"/ipmacbase.do?action=update&id="+vo.getId()+"&ifsms=0>取消发送</a>";
    												if(vo.getIfsms().equals("0")){
    													sms="<a href="+rootPath+"/ipmacbase.do?action=update&id="+vo.getId()+"&ifsms=1>发送</a>";
    												}
    												String tel="<a href="+rootPath+"/ipmacbase.do?action=update&id="+vo.getId()+"&iftel=0>是</a>";
    												if(vo.getIftel().equals("0")){
    													tel="<a href="+rootPath+"/ipmacbase.do?action=update&id="+vo.getId()+"&iftel=1>否</a>";
    												}
    												String email="<a href="+rootPath+"/ipmacbase.do?action=update&id="+vo.getId()+"&ifemail=0>取消发送</a>";
    												if(vo.getIfemail().equals("0")){
    													email="<a href="+rootPath+"/ipmacbase.do?action=update&id="+vo.getId()+"&ifemail=1>发送</a>";
    												}
    											%>
    											<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=cancelipmacbase&id=<%=vo.getId()%>&flag=-1">取消基线</a></td>
    											<% if(ifband ==0){%>
            												
            												<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=setipmacbase&id=<%=vo.getId()%>&flag=1">绑定</a></td>
            												<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=setipmacbase&id=<%=vo.getId()%>&flag=2">绑定</a></td>
            												<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=setipmacbase&id=<%=vo.getId()%>&flag=3">绑定</a></td>
            												<%
            												    	}else if(ifband == 1){
            												%>  
            												
            												<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=cancelipmacbase&id=<%=vo.getId()%>&flag=0">取消</a></td>
            												<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=setipmacbase&id=<%=vo.getId()%>&flag=2">绑定</a></td>
            												<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=setipmacbase&id=<%=vo.getId()%>&flag=3">绑定</a></td>
            												<%
            												    	}else if(ifband == 2){
            												%>  
            												
            												<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=setipmacbase&id=<%=vo.getId()%>&flag=1">绑定</a></td>
            												<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=cancelipmacbase&id=<%=vo.getId()%>&flag=0">取消</a></td>
            												<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=setipmacbase&id=<%=vo.getId()%>&flag=3">绑定</a></td>
            												<%
            												    	}else if(ifband == 3){
            												%>  
            												
            												<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=setipmacbase&id=<%=vo.getId()%>&flag=1">绑定</a></td>
            												<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=setipmacbase&id=<%=vo.getId()%>&flag=2">绑定</a></td>
            												<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=cancelipmacbase&id=<%=vo.getId()%>&flag=0">取消</a></td>
            												<%
            												    	}else if(ifband == -1){
            												%>  
            												
            												<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=setipmacbase&id=<%=vo.getId()%>&flag=1">绑定</a></td>
            												<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=setipmacbase&id=<%=vo.getId()%>&flag=2">绑定</a></td>
            												<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=setipmacbase&id=<%=vo.getId()%>&flag=0">绑定</a></td>
            												<%
            													}
            												%>
    											<td  align='left'><%=sms%></td>
    											<td  align='center'><%=tel%></td>
											<td  align='center'><%=email%></td>
											<td  align='center'><%=namestr%></td>
											<td >&nbsp;&nbsp;
        										<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('2','<%=vo.getId()%>','<%=vo.getMac()%>')>
        										<!--<a href="<%=rootPath%>/network.do?action=ready_edit&id=<%=vo.getId()%>">
												<img src="<%=rootPath%>/resource/image/editicon.gif" border="0"/></a>-->
												
												
											</td>
        										
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
