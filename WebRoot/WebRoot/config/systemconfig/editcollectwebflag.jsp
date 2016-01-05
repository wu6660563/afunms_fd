<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="java.util.List"%>
<%
  String rootPath = request.getContextPath();
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
<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
<script language="javascript">	
  
  function doQuery()
  {  
     if(mainForm.key.value=="")
     {
     	alert("请输入查询条件");
     	return false;
     }
     mainForm.action = "<%=rootPath%>/network.do?action=find";
     mainForm.submit();
  }
  
    function doCancelManage()
  {  
     mainForm.action = "<%=rootPath%>/network.do?action=cancelmanage";
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


</script>
<script language="JavaScript" type="text/javascript">
  function toCheck()
  {
     if(FrmDeal.pwd.value=="")
     {
        alert("<密码>不能为空!");
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

	//网络设备的ip地址
	function modifySystemConfigflagAjax(flagkey){
		var t = document.getElementById(flagkey);
		var flagvalue = t.options[t.selectedIndex].value;//获取下拉框的值
		$.ajax({
				type:"GET",
				dataType:"json",
				url:"<%=rootPath%>/systemConfigAjaxManager.ajax?action=updateSystemconfigFlag&flagkey="+flagkey+"&flagvalue="+flagvalue,
				success:function(data){
					window.alert(data.message);
				},
				error:function(data){ 
					window.alert(data.message); 
				}
		});
	}
</script>
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa" onload="initmenu();">
<form method="post" name="FrmDeal">
	<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
		<tr>
			<td width="200" valign=top align=center>
				<%=menuTable%>
			</td>
			<td align="center" valign=top>
				<table width="98%"  cellpadding="0" cellspacing="0" algin="center">
					<tr>
						<td background="<%=rootPath%>/common/images/right_t_02.jpg" width="100%">
							<table width="100%" cellspacing="0" cellpadding="0">
		                 	 	<tr>
		                    		<td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
		                    		<td class="layout_title">系统管理 >> 系统配置 >> 系统运行模式配置</td>
		                   			 <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
		                 		 </tr>
	              			</table>
				  		</td>
	              </tr>
					<tr>					
						<td height=50  valign="top" align=center valign=center>
							<table width="100%"  border="0" cellpadding="1" cellspacing="1">
	  							<tr>
	  								<td valign="top" bgcolor="#FFFFFF" align="center" colspan="3">
										<!--table begin-->
										<table width="350px;"  cellpadding="0" bordercolorlight='#000000' bordercolordark='#FFFFFF' height="100px;">
		  									<tr class="othertr1">
		  										<td>系统运行模式:</td>
		  										<td>
		  											<select style="width:110px;" id="collectwebflag">
														<%
															String collectwebflag = PollingEngine.getCollectwebflag();
														 %>
														 <option <%if("0".equals(collectwebflag)){ %>selected<%} %> value='0'>采集与访问集成</option>
													 <option <%if(!"0".equals(collectwebflag)){ %>selected<%} %> value='1'>采集与访问分离</option>
													</select>
												</td>
												<td><input type="button" class="button" value="修 改" style="width:80"  onclick="modifySystemConfigflagAjax('collectwebflag');"></td>
											</tr>
											<tr class="othertr1">
												<td>资源树显示模式:</td>
												<td>
		  											<select style="width:110px;" id="treeshowflag">
														<%
															String treeshowflag = PollingEngine.getTreeshowflag();
														 %>
														 <option <%if("0".equals(treeshowflag)){ %>selected<%} %> value='0'>隐藏物设备节点</option>
														 <option <%if(!"0".equals(treeshowflag)){ %>selected<%} %> value='1'>显示无设备节点</option>
													</select>
												</td>
												<td><input type="button" class="button" value="修 改" style="width:80"  onclick="modifySystemConfigflagAjax('treeshowflag');"></td>
		  									</tr>
										</table>
									<!--table end-->
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
