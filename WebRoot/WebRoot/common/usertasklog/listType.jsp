<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.system.model.UserTaskLog"%>
<%@ include file="/include/globe.inc"%>
<%@ page import="com.afunms.event.model.EventList"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.topology.model.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%
  String rootPath = request.getContextPath();
  //System.out.println(rootPath);
  List<UserTaskLog> list = (List<UserTaskLog>)request.getAttribute("list");
  User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
  JspPage jp = (JspPage)request.getAttribute("page");
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>


<html>
	<head>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<meta http-equiv="Page-Enter"
			content="revealTrans(duration=x, transition=y)">
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		
		<script language="javascript">	
  var curpage= <%=jp.getCurrentPage()%>;
    var totalpages = <%=jp.getPageTotal()%>;
  	var listAction = "<%=rootPath%>/userTaskLog.do?action=listType";
  	
  	function onchangeView(){
  		mainForm.action = "<%=rootPath%>/userTaskLog.do?action=list";
  		mainForm.submit();
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
		location.href="<%=rootPath%>/network.do?action=ready_edit&id="+node;
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
                    <td class="layout_title"><b>��ҳ >> ����̨�� >> ��־�б�</b></td>
                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                  </tr>
              </table>
			  </td>


              <tr>           
								<td>
								<table width="100%" cellpadding="0" cellspacing="0">
        			<tr>
	              <td bgcolor="#ECECEC" width="100%" align='right'>
				  <input type="button" value="�л�������ͼ" onclick="onchangeView();">&nbsp;&nbsp;</td>
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
													<jsp:param name="curpage" value="<%=jp.getCurrentPage()%>" />
													<jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>" />
												</jsp:include>
											      </td>
        </tr>
		</table>
		</td>
		</tr>       <tr >
			<td colspan="2">
				<table cellspacing="1" cellpadding="0" width="100%" >
					<tr class="microsoftLook0" height=28>
								    	<td class="report-data-body-title" width="10%">&nbsp;���</td>
								        <td class="report-data-body-title" width="20%"><strong>�û���</strong></td>
								        <td class="report-data-body-title" width="50%"><strong>����</strong></td>
								    	<td class="report-data-body-title" width="20%"><strong>����</strong></td>
								 </tr>
								 <%for(int i = 0 ; i< list.size(); i++){
								 		String content = list.get(i).getContent();
								 		if(content.length()>=50){
								 			String contentNew = content.substring(0,50) + "...";
								 			content = contentNew;
								 		}
								 		%>
								 			 <tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> class="microsoftLook">
								 				<td class="report-data-body-list" width="10%">&nbsp;
								 				<INPUT type="checkbox" class=noborder name=checkbox value="<%=list.get(i).getId()%>">
								 				<%=i+1 %></td>
								        		<td class="report-data-body-list" width="20%"><%=user.getName()%></td>
								        		<td class="report-data-body-list" width="50%"><%=content %></td>
								    			<td class="report-data-body-list" width="20%"><%=list.get(i).getTime()%></td>
								 			</tr>
								 		<%
								 } %>
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
