<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.business.model.BusCollectType"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@ include file="/include/globe.inc"%>
<%
  String rootPath = request.getContextPath();
  List list = (List)request.getAttribute("list");
  JspPage jp = (JspPage)request.getAttribute("page");
  String type=null;
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<meta http-equiv="Page-Enter" content="revealTrans(duration=x, transition=y)">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/javascript">
  var listAction = "<%=rootPath%>/buscolltype.do?action=list";
  var delAction = "<%=rootPath%>/buscolltype.do?action=delete";
  var curpage= <%=jp.getCurrentPage()%>;
  var totalpages = <%=jp.getPageTotal()%>;
  var xx = "";
  function toedit(){
      window.location="<%=rootPath%>/buscolltype.do?action=ready_edit&id="+xx;
  }
  
  function toAdd()
  {
     mainForm.action = "<%=rootPath%>/buscolltype.do?action=ready_add";
     mainForm.submit();
  }
</script>
<script language="JavaScript" type="text/javascript">
  function toCheck()
  {
     if(FrmDeal.pwd.value=="")
     {
        alert("<����>����Ϊ��!");
        FrmDeal.pwd.focus();
        return false;
     }
     FrmDeal.action = "check.jsp";
     FrmDeal.submit();
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
//���Ӳ˵�	
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
                    <td class="layout_title">��Դ >> ��ͼ���� >> �ɼ�����</td>
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
			<a href="#" onclick="toAdd()">����</a>
			<a href="#" onclick="toDelete()">ɾ��</a>&nbsp;&nbsp;&nbsp;
		</td>
									</tr>
        								</table>
										</td>
        						</tr>	
		<tr>
			<td colspan="2">
				<table cellspacing="1" cellpadding="0" width="100%">
					<tr class="microsoftLook0" height=28>
						<th width='5%'><INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()"></th>
   						<th width='15%'>���</th>
   						<th width='25%'>�ɼ�����</th>
   						<th width='45%'>����</th>
                        <th width='10%'>�༭</th>
					</tr>
<%
    String sex = "";
    BusCollectType vo = null;
    int startRow = jp.getStartRow();
    for(int i=0;i<list.size();i++)
    {
       vo = (BusCollectType)list.get(i);
%>
<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> class="microsoftLook" height=25> 
    	<td align='center'><INPUT type="checkbox" class=noborder name=checkbox value="<%=vo.getId()%>"></td>
    	<td ><font color='blue'><%=startRow + i%></font></td>
    	<td align='center'><%=vo.getCollecttype()%></td>
    	<td align='center'><%=vo.getBct_desc()%></td>
    	<td align='center'><a href="<%=rootPath%>/buscolltype.do?action=ready_edit&id=<%=vo.getId()%>"><img src="<%=rootPath%>/resource/image/editicon.gif" border="0"/></a></td>
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