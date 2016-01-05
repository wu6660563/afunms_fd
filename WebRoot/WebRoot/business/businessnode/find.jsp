<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.business.model.BusinessNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.business.dao.*" %>
<%@page import="com.afunms.business.manage.*" %>
<%@page import="com.afunms.business.model.BusCollectType"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.topology.model.*"%>
<%@page import="java.util.*"%>
<%@ include file="/include/globe.inc"%>
<%
  String rootPath = request.getContextPath();
  List list = (List)request.getAttribute("list");
 
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>

<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">



<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">

<script language="JavaScript" type="text/javascript">

   function search()
  {
     mainForm.action = "<%=rootPath%>/businessNode.do?action=search";
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



	//��������
	var node="";
	var ipaddress="";
	var operate="";
	/**
	*���ݴ����id��ʾ�Ҽ��˵�
	*/
	function showMenu(id,nodeid)
	{	
		node=nodeid;
		//operate=oper;
	    if("" == id)
	    {
	        popMenu(itemMenu,100,"100");
	    }
	    else
	    {
	        popMenu(itemMenu,100,"1111");
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
	function edit()
	{
		location.href="<%=rootPath%>/businessNode.do?action=ready_edit&id="+node;
	}
	function cancelmanage()
	{
		location.href="<%=rootPath%>/businessNode.do?action=cancelmanage&id="+node;
	}
	function addmanage()
	{
		location.href="<%=rootPath%>/businessNode.do?action=addmanage&id="+node;
	}	
	function clickMenu()
	{
	}



</script>
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa"  onload="initmenu();">
<!-- ��������������Ҫ��ʾ���Ҽ��˵� -->
	<div id="itemMenu" style="display: none";>
	<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
		style="border: thin;font-size: 12px" cellspacing="0">
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.edit()">�޸���Ϣ</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.cancelmanage()">ȡ������</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.addmanage()">��Ӽ���</td>
		</tr>		
	</table>
	</div>
	<!-- �Ҽ��˵�����-->
<form name="mainForm" method="post">
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
                    <td class="layout_title"><b>��Դ >> ��ͼ���� >> ��ѯҵ��ڵ��б�</b></td>
                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                  </tr>
              </table>
			  </td>
			  </tr>

	
		<tr>
			<td colspan="2">
				<table cellspacing="1" cellpadding="0" width="100%">
					<tr class="microsoftLook0" height=28>
						<th width='5%'><INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()"></th>
   						<th width='5%'>���</th>
						<th width='10%'>����</th>
                        <th width='10%'>����ҵ����ͼ</th>
   						<th width='20%'>����</th>
   						<th width='10%'>�ɼ�����</th>    
   						<th width="15%">����</th>
						<th width='5%'>�Ƿ����</th>
   						<th width='10%'>�༭</th>
					</tr>
<%
    BusinessNode vo = null;
    Hashtable ht=new Hashtable();
    BusCollectTypeDao dao = new BusCollectTypeDao();
    List arr=dao.loadAll();
    for(int j=0;j<arr.size();j++)
    {
     BusCollectType bct=(BusCollectType)arr.get(j);
     ht.put(bct.getId(),bct.getCollecttype());
    }
    
    for(int i=0;i<list.size();i++)
    {
       vo = (BusinessNode)list.get(i);
       ManageXmlDao viewdao = new ManageXmlDao();
	   String veiwname = null;
       try{
		  veiwname = viewdao.findNameById(vo.getBid());
	   }catch(Exception e){
	   }finally{
	       viewdao.close();
	   }
%>
<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> class="microsoftLook" height=25> 
    	<td align='center'><INPUT type="checkbox" class=noborder name=checkbox value="<%=vo.getId()%>"></td>
    	<td ><font color='blue'><%=1 + i%></font></td>
        <td align='center'><%=vo.getName()%></td>
		
		<td align='center'><%=veiwname%></td>
    	<td align='center'><%=vo.getDesc()%></td>
    	<td align='center'>
    	&nbsp;<%=ht.get(vo.getCollecttype()) %></td>
    	<td align='center'><%=vo.getMethod()%></td>
        <td align='center'><%
				if(vo.getFlag() == 0){
			%>
			&nbsp;δ����
			<%
				}else{
			%>	
			&nbsp;�Ѽ���
			<%
				}
			%>	</td>
    	<td align='center'>
		<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('2','<%=vo.getId()%>')>
		<!--<a href="<%=rootPath%>/businessNode.do?action=ready_edit&id=<%=vo.getId()%>"><img src="<%=rootPath%>/resource/image/editicon.gif" border="0"/></a>-->
		</td>
  	</tr>
<%
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
</BODY>
</HTML>