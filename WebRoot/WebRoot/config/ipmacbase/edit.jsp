<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>
<%@page import="com.afunms.common.util.SysUtil" %>
<%@page import="com.afunms.monitor.item.*"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.impl.HostLastCollectDataManager"%>
<%@page import="com.afunms.polling.api.I_HostLastCollectData"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.monitor.item.base.MoidConstants"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@ page import="com.afunms.polling.api.I_Portconfig"%>
<%@ page import="com.afunms.polling.om.Portconfig"%>
<%@ page import="com.afunms.polling.om.IpMacBase"%>
<%@ page import="com.afunms.polling.impl.PortconfigManager"%>

<%@ page import="com.afunms.system.model.Department"%>
<%@ page import="com.afunms.system.dao.DepartmentDao"%>
<%@ page import="com.afunms.config.model.Employee"%>
<%@ page import="com.afunms.config.dao.EmployeeDao"%>

<%@page import="java.util.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<% 

  String rootPath = request.getContextPath(); 
  IpMacBase vo = (IpMacBase)request.getAttribute("vo");
  String key = (String)request.getAttribute("key");
  String value = (String)request.getAttribute("value");
  System.out.println("key==="+key+"====value:"+value);
  EmployeeDao dao = new EmployeeDao();
  List employeelist = dao.loadAll();
  dao.close();
  if(employeelist != null && employeelist.size()>0){
  	for(int i=0;i<employeelist.size();i++){
  		Employee employee = (Employee)employeelist.get(i);
  	}
  }
 
    

java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd HH:mm");					  	 
         
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>
<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
<script language="javascript">
function showdiv(id)
{
	var obj=document.getElementsByName("tab");
    j=0;
	var tagname=document.getElementsByTagName("DIV");
	for(var i=0;i<tagname.length;i++)
	{
		if(tagname[i].id==id)
		{
		   obj[j].className="selectedtab";
		   tagname[i].style.display="";
		   j++;
		}
		else if(tagname[i].id.indexOf("contentData")==0)
		{
		   obj[j].className="1";
		   j++;
		   tagname[i].style.display="none";
		}
	}
}
function changeOrder(para){
      mainForm.orderflag.value = para;
      mainForm.id.value = <%=vo.getId()%>;
      mainForm.action = "<%=rootPath%>/monitor.do?action=netif";
      mainForm.submit();
}
function openwin3(operate,ipaddress,index,ifname) 
{	//var ipaddress = document.forms[0].ipaddress.value;
  window.open ("<%=rootPath%>/monitor.do?action="+operate+"&ipaddress="+ipaddress+"&ifindex="+index+"&ifname="+ifname, "newwindow", "height=400, width=850, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes") 
}

  function toAdd()
  {
     	if (confirm("ȷ��Ҫ�޸���?")){
     		<%
     			if(value != null && value.trim().length() > 0){
     		%>
        		mainForm.action = "<%=rootPath%>/ipmacbase.do?action=selupdateemployee";
        	<%
        		}else{
        	%>
        		mainForm.action = "<%=rootPath%>/ipmacbase.do?action=updateemployee";
        	<%
        		}
        	%>
        	mainForm.submit();
        }
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
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#9FB0C4"  onload="initmenu();">
<form method="post" name="mainForm">
<input type=hidden name="id" value="<%=vo.getId()%>">
<input type=hidden name="key" value="<%=key%>">
<input type=hidden name="value" value="<%=value%>">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td width="200" valign=top align=center>
	
	<%=menuTable%>
            				
	</td>
		<td bgcolor="#9FB0C4" align="center" valign=top>
		<table width="100%" border=0 cellpadding=0 cellspacing=0>
			<tr>
			  	<td height=380 bgcolor="#FFFFFF" valign="top">	            
					<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none align=center border=1 algin="center">
                  				<tr>
                    					<td width="619" height="25" bgcolor=#397DBD style="HEIGHT: 25px" colspan=2><div align="left">&nbsp;&nbsp;<font color=#ffffff><b>>> �豸ά�� >> IP/MAC >> �༭</b></font></div></td>
                  				</tr>
                  				<tr>
                  					<TD nowrap align="right" height="24" width="10%">�����豸����&nbsp;</TD>
                  					<TD nowrap width="40%">&nbsp;<%=vo.getRelateipaddr()%>
                  					</TD>
                				</tr>
                  				<tr bgcolor="#F1F1F1">
                  					<TD nowrap align="right" height="24" width="10%">�����豸IP��ַ&nbsp;</TD>
                  					<TD nowrap width="40%">&nbsp;<%=vo.getRelateipaddr()%>
                  					</TD>
                				</tr>
                				<tr>
                  					<TD nowrap align="right" height="24" width="10%">IP&nbsp;</TD>
                  					<TD nowrap width="40%">&nbsp;<%=vo.getIpaddress()%>
                  					</TD>
                				</tr>  
                				<tr bgcolor="#F1F1F1">
                  					<TD nowrap align="right" height="24" width="10%">MAC&nbsp;</TD>
                  					<TD nowrap width="40%">&nbsp;<%=vo.getMac()%>
                  					</TD>
                				</tr> 
                				<tr>
                  					<TD nowrap align="right" height="24" width="10%">�û�&nbsp;</TD>
                  					<TD nowrap width="40%">&nbsp;
                  					<select name=employee_id>
                  					<option value="0"> </option>
                  					<%
                  						int employee_id = vo.getEmployee_id();
                  						dao = new EmployeeDao();
                  						Employee vo_ployee =(Employee)dao.findByID(employee_id+"");
                  						if(employeelist != null && employeelist.size()>0){
                  							for(int i=0;i<employeelist.size();i++){
  										Employee employee = (Employee)employeelist.get(i);
  										DepartmentDao deptdao = new DepartmentDao();
  										Department dept = (Department)deptdao.findByID(employee.getId()+"");
  										deptdao.close();
  										String namestr = employee.getName()+"("+dept.getDept()+")";
  										String checkedflag = "";
  										if(vo_ployee != null){
  											if(employee.getId()==vo.getEmployee_id()) checkedflag="selected";
  										}
                  					%>
                  					<option value="<%=employee.getId()%>" <%=checkedflag%>><%=namestr%></option>
                  					<%
                  							}
                  						}
                  						if(vo_ployee == null){
                  					%>
                  					<option value="0" selected> </option>
                  					<%
                  						}
                  					%>
                  					
                  					</select>
                  					</TD>
                				</tr>               				                 				                				              				
                				<tr>
                					<td colspan=2 align=center><br>
								<input type="button" value="�� ��" style="width:50" class="formStylebutton" onclick="toAdd()">&nbsp;&nbsp;
								<input type=reset class="formStylebutton" style="width:50" value="�� ��" onclick="javascript:history.back(1)">                					
                  					</td>
                  				</tr>
                  			</table>
                  		</td>
                  	</tr>
		</table> 
	</td>
	
	</tr>
	</table>                	              
                
                                          
</form>
</BODY>
</HTML>
 