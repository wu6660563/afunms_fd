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
     	alert("�������ѯ����");
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
<script language="JavaScript">

	//��������
	var node="";
	var ipaddress="";
	var operate="";
	/**
	*���ݴ����id��ʾ�Ҽ��˵�
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
<!-- ��������������Ҫ��ʾ���Ҽ��˵� -->
	<div id="itemMenu" style="display: none";>
	<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
		style="border: thin;font-size: 12px" cellspacing="0">
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.edit()">�༭</td>
		</tr>				
	</table>
	</div>
	<!-- �Ҽ��˵�����-->
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
                    <td class="layout_title"><b>��Դ >> IP/MAC��Դ >> IP/MAC����</b></td>
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
				&nbsp;&nbsp;&nbsp;&nbsp;<B>��ѯ:</B>
        								<SELECT name="key" style="width=100"> 
          									<OPTION value="relateipaddr" selected>�����豸IP</OPTION>
          									<OPTION value="ipaddress">IP��ַ</OPTION>
          									<OPTION value="mac">MAC</OPTION>          
          									
          								</SELECT>&nbsp;<b>=</b>&nbsp; 
          								<INPUT type="text" name="value" width="15" class="formStyle">
          								<INPUT type="button" class="formStyle" value="��ѯ" onclick=" return doQuery()">
          							</td> 
          							<td bgcolor="#ECECEC" width="50%" align='right'>
									
									<a href="#" onclick="toDelete()">ɾ��</a>&nbsp;&nbsp;&nbsp;
									<a href="#" onclick="toDeleteAll()">ɾ��ȫ��</a>&nbsp;&nbsp;&nbsp;
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
      											<th width='5%'><INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()">���</th>				
      											<th width='10%'>�����豸(ip)</th>
      											<th width='5%'>�˿�</th>
      											<th width='10%'>IP��ַ</th>      
      											<th width='12%'>MAC</th>
      											<th width='6%'>����</th>
      											<th width='6%'>IP-MAC</th>
      											<th width='6%'>�˿�-MAC</th>
      											<th width='8%'>IP-�˿�-MAC</th>
      											<th width='6%'>���Ͷ���</th>
      											<th width='6%'>�绰�澯</th>
      											<th width='6%'>�ʼ��澯</th>
      											<th width='10%'>�û�</th>
      											<th width='4%'>����</th>
      											
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
    												//String macbase="<a href="+rootPath+"/ipmacbase.do?action=update&id="+vo.getId()+"&ifband=0>ȡ��</a>";
    												//String ipmac="<a href="+rootPath+"/ipmacbase.do?action=update&id="+vo.getId()+"&ifband=0>ȡ��</a>";
    												
    												
    												String sms = "<a href="+rootPath+"/ipmacbase.do?action=update&id="+vo.getId()+"&ifsms=0>ȡ������</a>";
    												if(vo.getIfsms().equals("0")){
    													sms="<a href="+rootPath+"/ipmacbase.do?action=update&id="+vo.getId()+"&ifsms=1>����</a>";
    												}
    												String tel="<a href="+rootPath+"/ipmacbase.do?action=update&id="+vo.getId()+"&iftel=0>��</a>";
    												if(vo.getIftel().equals("0")){
    													tel="<a href="+rootPath+"/ipmacbase.do?action=update&id="+vo.getId()+"&iftel=1>��</a>";
    												}
    												String email="<a href="+rootPath+"/ipmacbase.do?action=update&id="+vo.getId()+"&ifemail=0>ȡ������</a>";
    												if(vo.getIfemail().equals("0")){
    													email="<a href="+rootPath+"/ipmacbase.do?action=update&id="+vo.getId()+"&ifemail=1>����</a>";
    												}
    											%>
    											<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=cancelipmacbase&id=<%=vo.getId()%>&flag=-1">ȡ������</a></td>
    											<% if(ifband ==0){%>
            												
            												<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=setipmacbase&id=<%=vo.getId()%>&flag=1">��</a></td>
            												<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=setipmacbase&id=<%=vo.getId()%>&flag=2">��</a></td>
            												<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=setipmacbase&id=<%=vo.getId()%>&flag=3">��</a></td>
            												<%
            												    	}else if(ifband == 1){
            												%>  
            												
            												<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=cancelipmacbase&id=<%=vo.getId()%>&flag=0">ȡ��</a></td>
            												<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=setipmacbase&id=<%=vo.getId()%>&flag=2">��</a></td>
            												<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=setipmacbase&id=<%=vo.getId()%>&flag=3">��</a></td>
            												<%
            												    	}else if(ifband == 2){
            												%>  
            												
            												<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=setipmacbase&id=<%=vo.getId()%>&flag=1">��</a></td>
            												<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=cancelipmacbase&id=<%=vo.getId()%>&flag=0">ȡ��</a></td>
            												<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=setipmacbase&id=<%=vo.getId()%>&flag=3">��</a></td>
            												<%
            												    	}else if(ifband == 3){
            												%>  
            												
            												<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=setipmacbase&id=<%=vo.getId()%>&flag=1">��</a></td>
            												<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=setipmacbase&id=<%=vo.getId()%>&flag=2">��</a></td>
            												<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=cancelipmacbase&id=<%=vo.getId()%>&flag=0">ȡ��</a></td>
            												<%
            												    	}else if(ifband == -1){
            												%>  
            												
            												<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=setipmacbase&id=<%=vo.getId()%>&flag=1">��</a></td>
            												<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=setipmacbase&id=<%=vo.getId()%>&flag=2">��</a></td>
            												<td height=28><a href="<%=rootPath%>/ipmacbase.do?action=setipmacbase&id=<%=vo.getId()%>&flag=0">��</a></td>
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
