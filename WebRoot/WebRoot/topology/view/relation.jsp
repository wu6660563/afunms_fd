<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.topology.model.CustomXml"%>
<%@ include file="/include/globe.inc"%>
<%
  List list = (List)request.getAttribute("list");
  int rc = list.size();
  String rootPath = request.getContextPath();
  JspPage jp = (JspPage)request.getAttribute("page");
  String nodeId = (String)request.getAttribute("nodeId");
%>
<html>
<head>
<title>关联拓扑图</title>
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/javascript">
var map="";
function save()//关联拓扑图
{
    var nId=<%=nodeId%>;
    //alert("关联"+nId);
    var args = window.dialogArguments;
    var nodes = args.xmldoc.getElementsByTagName("node");
	for (var j = 0; j < nodes.length; j += 1)
	{
		var node = nodes[j];
		var id = node.getElementsByTagName("id")[0].text;
		if (nId == id)
		{
			node.getElementsByTagName("relationMap")[0].text = map;
		}
	}
	args.frmMap.hidXml.value = args.xmldoc.xml;
	args.relation();
	window.close();
}
  
function cancel()
{
    //alert("取消关联");
    var nId=<%=nodeId%>;
    var args = window.dialogArguments;
    var nodes = args.xmldoc.getElementsByTagName("node");
	for (var j = 0; j < nodes.length; j += 1)
	{
		var node = nodes[j];
		var id = node.getElementsByTagName("id")[0].text;
		if (nId == id)
		{
			node.getElementsByTagName("relationMap")[0].text = "";
		}
	}
	args.frmMap.hidXml.value = args.xmldoc.xml;
	
	//mainForm.action = "<%=rootPath%>/customxml.do?action=set_default";
    //mainForm.submit();
    
	args.relation();
	window.close();
}
  
  function setDefault()
  {
    var bExist = false;

    if ( mainForm.radio.length == null ) 
    {
      if( mainForm.radio.checked )  
         bExist = true;
    }
    else  
    {
       for( var i=0; i < mainForm.radio.length; i++ )
       {
         if(mainForm.radio[i].checked)
            bExist = true;
       }
    }
    if(bExist)
    {
        mainForm.action = "<%=rootPath%>/customxml.do?action=set_default";
        mainForm.submit();
    }
    else
    {
       alert("请选择要设置的记录");
       return false;
    }
  }

</script>
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#9FB0C4">
<form method="post" name="mainForm">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td bgcolor="#cedefa" align="center" valign="top">
			<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#397DBD cellPadding=0 rules=none align=center border=1 algin="center">
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
								    int startRow = jp.getStartRow();
								    for(int i=0;i<rc;i++)
								    {
								       CustomXml vo = (CustomXml)list.get(i);
								%>
                               <tr bgcolor="DEEBF7" <%=onmouseoverstyle%> class="microsoftLook">
    	                       <td  align='center'><INPUT type="radio" class=noborder name=radio value="<%=vo.getId()%>" class=noborder onclick="map='<%=vo.getXmlName()%>';"><font color='blue'><%=startRow + i%></font></td>    	 	
    	                       <td  align='center'><%=vo.getViewName()%></td>			
	                           </tr>
								<%
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
	        <!--  
			<input type="button" value="关联" style="width:50" class="formStylebutton" onclick="save()">
			<input type="button" value="取消" style="width:50" class="formStylebutton" onclick="cancel();">
			-->
			<input type="button" value="关闭" style="width:50" class="formStylebutton" onclick="window.close();">
			
       </td>
   </tr>
</table>
</form>		
</BODY>
</HTML>
