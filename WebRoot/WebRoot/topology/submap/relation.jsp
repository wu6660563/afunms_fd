<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.topology.model.ManageXml"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@ include file="/include/globe.inc"%>
<%
   User current_user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
   System.out.println(current_user.getBusinessids());
   String bids[] = current_user.getBusinessids().split(",");
  List list = (List)request.getAttribute("list");
  int rc = list.size();
  String rootPath = request.getContextPath();
  //JspPage jp = (JspPage)request.getAttribute("page");
  String nodeId = (String)request.getAttribute("nodeId");
  String category = (String)request.getAttribute("category");
  String mapId = (String)request.getAttribute("mapId");
%>
<html>
<head>
<title>关联拓扑图</title>
<base target="_self">
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<meta http-equiv="Pragma" content="no-cache">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/javascript">
function save()//关联拓扑图
{
    var args = window.dialogArguments;
	mainForm.action = "<%=rootPath%>/submap.do?action=save_relation_node&xml="+args.fatherXML;
    mainForm.submit();
    alert("关联成功!");
	window.close();
	args.location.reload();
}
  
function cancel()
{
    var args = window.dialogArguments;
    mainForm.action = "<%=rootPath%>/submap.do?action=cancel_relation_node&xml="+args.fatherXML;
    mainForm.submit();
    alert("取消成功!");
	window.close();
	args.location.reload();
}

</script>
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#9FB0C4">
<form method="post" name="mainForm">
<input type=hidden name="nodeId" value="<%=nodeId%>">
<input type=hidden name="category" value="<%=category%>">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td bgcolor="#cedefa" align="center" valign="top">
			<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#397DBD cellPadding=0 rules=none align=center border=1>
				<tr>
					<td height="28" align="left" bordercolor="#397DBD" bgcolor="#397DBD" class="txtGlobalBold" colspan=3>&nbsp;&nbsp;关联拓扑图</td>
				</tr>
			    <tr>
				    <td bgcolor="#FFFFFF" valign="top">
                       <input type="hidden" name="intMultibox">	
	                   <table cellSpacing="1" cellPadding="0" width="100%" border="0">
		                   <tr>
			               <td>
			               <table cellspacing="1" cellpadding="0" width="100%" >
			                   <tr class="microsoftLook0" height=28>
	                           <th align='center' width='15%'>序号</th>				
	                           <th align='center' width='85%'>视图名</th>
	                           </tr>
								<%
								    //int startRow = jp.getStartRow();
								    
								    for(int i=0;i<rc;i++)
								    {
								       ManageXml vo = (ManageXml)list.get(i);
							        int tag = 0;
									if(bids!=null&&bids.length>0){
									    for(int j=0;j<bids.length;j++){
									        if(vo.getBid()!=null&&!"".equals(vo.getBid())&&vo.getBid().indexOf(bids[j])!=-1){
									            tag++;
									        }
									    }
									}
									if(tag>0){
								       if(vo.getId()==Integer.parseInt(mapId)){
							   %>
                               <tr bgcolor="DEEBF7" <%=onmouseoverstyle%> class="microsoftLook">
    	                       <td  align='center'><INPUT type="radio" class=noborder name=radio value="<%=vo.getId()%>" class=noborder checked><font color='blue'><%=i+1%></font></td>    	 	
    	                       <td  align='center'><%=vo.getTopoName()%></td>			
	                           </tr>
								<%
								       } else {
				
								%>
                               <tr bgcolor="DEEBF7" <%=onmouseoverstyle%> class="microsoftLook">
    	                       <td  align='center'><INPUT type="radio" class=noborder name=radio value="<%=vo.getId()%>" class=noborder ><font color='blue'><%=i+1%></font></td>    	 	
    	                       <td  align='center'><%=vo.getTopoName()%></td>			
	                           </tr>
								<%
								       }}
								    }
								%> 				
			               </table>
			               </td>
		                   </tr>	
	                   </table>
                    </td>
			    </tr>
		    </table>
        </td>
    </tr>		
    <tr>
	    <td nowrap align=center>
			<input type="button" value="确定" style="width:50" class="formStylebutton" onclick="save()">
			<input type="button" value="取消关联" style="width:80" class="formStylebutton" onclick="cancel();">
			<input type="button" value="关闭" style="width:50" class="formStylebutton" onclick="window.close();">
       </td>
   </tr>
</table>
</form>		
</BODY>
</HTML>
