<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.config.model.Diskconfig"%>
<%
  String rootPath = request.getContextPath();
  List list = (List)request.getAttribute("list");
  List ips = (List)request.getAttribute("ips");
  String ipaddress = (String)request.getAttribute("ipaddress");
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
  var delAction = "<%=rootPath%>/disk.do?action=delete";
  var listAction = "<%=rootPath%>/disk.do?action=find";
  
  function doQuery()
  {  
     mainForm.action = "<%=rootPath%>/disk.do?action=find";
     mainForm.submit();
  }
  function doFromlastoconfig()
  {  
     mainForm.action = "<%=rootPath%>/disk.do?action=fromlasttoconfig";
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
  
    function toEmpty()
  {
      mainForm.action = "<%=rootPath%>/disk.do?action=empty";
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
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa"   onload="initmenu();">
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
                    <td class="layout_title"><b>��Դ >> ���ܼ��� >> ���̷�ֵһ��</b></td>
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
        								<SELECT name="ipaddress" style="width=100">
        									<%
        										if(ips != null && ips.size()>0){
        											for(int k=0;k<ips.size();k++){
        												String ip = (String)ips.get(k);
        												if(ipaddress != null && ipaddress.trim().length()>0){
        													if(ip.equalsIgnoreCase(ipaddress)){
        									%> 			
        														<OPTION value="<%=ip%>" selected><%=ip%></OPTION>
        									<%
        													}else{
        									%>
          														<OPTION value="<%=ip%>"><%=ip%></OPTION>
          									<%
          													}
          												}else{
          									%>
          													<OPTION value="<%=ip%>"><%=ip%></OPTION>
          									<%
          												}
          											}
          											
          										}
          									%>        
          									
          								</SELECT>&nbsp;
          								<INPUT type="button" class="formStyle" value="��ѯ" onclick=" return doQuery()">
          							</td> 
          							<td bgcolor="#ECECEC" width="50%" align='right'>
									<INPUT type="button" class="formStyle" value="�� ��" onclick=" return toEmpty()">
			                        <INPUT type="button" class="formStyle" value="ˢ ��" onclick=" return doFromlastoconfig()">
									<INPUT type="button" class="formStyle" value="ɾ ��" onclick=" return toDelete()">&nbsp;&nbsp;&nbsp;
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
	  									<tr class="microsoftLook0">
	  										<th width='5%'><INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()"></th>
      											<td width="10%" align="center"><strong>IP��ַ</strong></td>  
    											<td width="10%" align="center"><strong>��������</strong></td>    
    											<td width="8%" align="center"><strong>�Ƿ�澯</strong></td>
    											<td width="8%" align="center"><strong>һ����ֵ</strong></td>
    											<td width="5%" align="center"><strong>�澯</strong></td>
    											<td width="8%" align="center"><strong>������ֵ</strong></td>
    											<td width="5%" align="center"><strong>�澯</strong></td>
    											<td width="8%" align="center"><strong>������ֵ</strong></td>
    											<td width="5%" align="center"><strong>�澯</strong></td>
      											<th width='8%' align="center">��ע</th>
      											<th width='5%' align="center">����</th>
      											
										</tr>
<%
    Diskconfig vo = null;
    int startRow = jp.getStartRow();
    for(int i=0;i<rc;i++)
    {
       vo = (Diskconfig)list.get(i);

          
%>
   										<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>>  
    											<td >
    												<font color='blue'>&nbsp;<%=startRow + i%></font><INPUT type="checkbox" class=noborder name=checkbox value="<%=vo.getId()%>" class=noborder></td>
    											<td  align='center' style="cursor:hand"><%=vo.getIpaddress()%></td>
    											<td  align='center'><%=vo.getName()%></td>
    											<%
    												String monflag = "��";
    												if(vo.getMonflag() == 0)monflag="��";
    												String sms = "��";
    												if(vo.getSms() == 0)sms="��";
    												String sms1 = "��";
    												if(vo.getSms1() == 0)sms1="��";
    												String sms2 = "��";
    												if(vo.getSms2() == 0)sms2="��";
    											%>
    											<td  align='center'><%=monflag%></td>
    											<td  align='center'><%=vo.getLimenvalue()%></td>
    											<td  align='center'><%=sms%></td>
    											<td  align='center'><%=vo.getLimenvalue1()%></td>
    											<td  align='center'><%=sms1%></td>
    											<td  align='center'><%=vo.getLimenvalue2()%></td>
    											<td  align='center'><%=sms2%></td>
    											<td  align='center'><%=vo.getBak()%></td>
											<td  align='center'><a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/disk.do?action=showedit&id=<%=vo.getId()%>","editdiskconfig", "height=400, width= 500, top=200, left= 200")'>
											<img src="<%=rootPath%>/resource/image/editicon.gif" border="0"/></a></td>
        										
  										</tr>			
<% }%>
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
