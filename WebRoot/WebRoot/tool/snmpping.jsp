<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.model.DiscoverConfig"%>
<%@page import="com.afunms.discovery.*"%>

<%
 String rootPath = request.getContextPath();
String menuTable = (String)request.getAttribute("menuTable");%>
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
<script language="javascript">
 
    
  function doDelete(id)
  {
     if (window.confirm("确实要删除吗?"))
     {
        mainForm.id.value = id;
        mainForm.action = "<%=rootPath%>/discover.do?action=delete";
        mainForm.submit();      
     }   
  }
  
  function doDiscover()
  {
        mainForm.action = "<%=rootPath%>/snmpping.do?action=ping";
        mainForm.submit();        
      
  }     

  function fromCore()
  {
      mainForm.core_ip.disabled = false;
      mainForm.community.disabled = false;
      //mainForm.net_ip.disabled = true;
      //mainForm.netmask.disabled = true;
      initmenu();
  }
  
  function fromNet()
  {
      mainForm.core_ip.disabled = true;
      mainForm.community.disabled = true;
      //mainForm.net_ip.disabled = false;
      //mainForm.netmask.disabled = false;
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
	m1 =new Menu("menu1",'menu1_child','dtu','100',show,my_on,my_off);
	m1.init();
}
</script>
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa"  onload="initmenu();">
<div id="itemMenu" style="display: none";>
	</div>
<form name="mainForm" method="post">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
	
		<td align="center" valign=top bgcolor="#ababab">
			<table width="98%"  cellpadding="0" cellspacing="0" algin="center">
				<tr>
					<td background="<%=rootPath%>/common/images/right_t_02.jpg" width="100%">
						<table width="100%" cellspacing="0" cellpadding="0">
		                  <tr>
		                    <td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
		                    <td class="layout_title"><b>SNMP检测</b></td>
		                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
		                  </tr>
		              </table>
				  	</td>
				 </tr>				
	
				<tr>
					<td>
						<table width="100%" border=0 cellpadding=0 cellspacing=1>
			<tr>
				<td height=300 bgcolor="#FFFFFF" valign="top">				
					<table cellSpacing="0" cellPadding="0" width="100%" border="0" align='left'>
										
						<tr>
				        										<td>
				        										
				        												<table border="0" id="table1" cellpadding="0" cellspacing="1"
																	width="100%">
																<TBODY>
			<tr>						
			<TD nowrap align="right" height="24" width="20%">IP地址&nbsp;</TD>				
			<TD nowrap width="30%">&nbsp;<input type="text" id="ipaddress"  name="ipaddress" size="20" class="formStyle"><font color='red'>*</font></TD>															
			<TD nowrap align="right" height="24" width="20%">团体名&nbsp;</TD>				
			<TD nowrap width="30%">&nbsp;<input type="text" id="name" name="name" size="20" class="formStyle"><font color='red'>*</font></TD>						
			</tr>
			<tr style="background-color: #ECECEC;">
							<TD nowrap align="right" height="24" width="20%">SNMP版本&nbsp;</TD>				
							<TD nowrap width="30%" >&nbsp;
								<select   name="version"  class="formStyle">
									<option value=5>--请选择--</option>
									<option value=0>SNMP&nbsp;V1</option>
									<option value=1>SNMP&nbsp;V2c</option>
									
								</select>
							</TD>
							<TD nowrap align="right" height="24"></TD>	
							<TD nowrap width="40%">&nbsp;<input type="button" value="开始检测" style="width:100" class="formStylebutton" onclick="doDiscover()"></font></TD>						
						
			</tr>	
				<tr>
				<td ></td>				</tr>		
			</table>			
		</td>
	</tr>
	<tr align="center" valign="center"> 
                    			<td height="28" align="center">
						<textarea id="resultofping" name="showList" rows="15" cols="60" readonly="readonly"></textarea>                    			
                    			</td>
				</tr>  
				<tr>
				<TD nowrap colspan="4" align=center>
				      <div align=center>
            			<input type=button value="关闭窗口" onclick="window.close()">
            		</div> 
					</TD>
					<td></td>	
					</tr>
				            
</table>
				</tr>
        							
			    </table>
			  </td>
			</tr>
	      </table>
	  </form>

</BODY>
</HTML>