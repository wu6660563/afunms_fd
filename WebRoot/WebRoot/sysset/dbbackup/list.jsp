<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@ include file="/include/globe.inc"%>
<%   
  List<String> tablesname = (List<String>)request.getAttribute("tablesname");
  String rootPath = request.getContextPath(); 
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script language="javascript">
 var number = 0;
 //全选
 function chkall(obj)
 {
 	var checkbox = document.getElementsByName("checkbox"); 
 	var selectobj = document.getElementById("selectednum");
 	if(obj.checked == true)
 	{	 
    	for( var i=0; i < checkbox.length; i++ )
    	{
        	checkbox[i].checked = true;
        }
        number = checkbox.length;
        selectobj.innerHTML = number;
    }
    else
    {
    	for( var i=0; i < checkbox.length; i++ )
    	{
        	checkbox[i].checked = false;
        }
        number = 0;
        selectobj.innerHTML = number;
    }       
 } 
 
 function checkChanged(obj)
 {
 	obj.checked ? number++ : number--;
 	if(number<0)
 	{
 		number=0;
 	}
 	var selectobj = document.getElementById("selectednum");
 	selectobj.innerHTML = number;
 }
 
 //开始备份
 function startbackup(obj)
 {
 	if( number==0 )
 	{
 		alert("请至少选择一个表！");
 		return false;
 	}
 	if( !confirm("确认要开始备份吗？") )
 	{
 		return false;
 	}
 	obj.disabled = true;
 	mainForm.action = "<%=rootPath%>/dbbackup.do?action=backup";
    mainForm.submit();
 }
 function startload(obj)
 {
 	if( !confirm("确认要开始导入数据文件吗？") )
 	{
 		return false;
 	}
 	obj.disabled = true;
 	mainForm.action = "<%=rootPath%>/dbbackup.do?action=load";
    mainForm.submit();
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

</script>
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa" onload="initmenu();if( ${result } ) alert('${msg }');">
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
                    <td class="layout_title">系统管理 >> 数据库维护 >> 数据导出</td>
                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                  </tr>
              </table>
			  </td>
			  </tr>

				<tr>           
								<tr>           
								<td>
								<table width="100%" cellpadding="0" cellspacing="1" >
								<tr>
								<td>
								<table width="100%" cellpadding="0" cellspacing="0" >
								<tr>
								<td bgcolor="#ECECEC" width="50%" align='left'>
						<input type="checkbox" class="noborder" name="all" onclick="chkall(this)"/>全选&nbsp;&nbsp;
						已选择的数目：<span id="selectednum" >0</span>
					</td>
					<td bgcolor="#ECECEC" width="50%" align='right'>
						备份类型：
						<input type="radio" name="radio" value="0" style="border: none;">只备份表结构
						<input type="radio" name="radio" value="1" style="border: none;">只备份表数据
						<input type="radio" name="radio" value="2" style="border: none;" checked="checked">表结构和数据	
					    <input type="button" value="开始备份" onclick="startbackup(this)"/>&nbsp;&nbsp;&nbsp;
						</td>
										</tr>
								</table>
		  						</td>
									</tr>
								</table>
		  						</td>                       
        						</tr>
				<tr >
			<td colspan="2">
				<table cellspacing="1" cellpadding="0" width="100%">
					<tr bgcolor="#FFFFFF" height=28>
       						<%
       							int j = 0;
  								for(int i=0;i<tablesname.size();i++)
  								{
  									j++;
  							%>
  								<td>
  									<input type="checkbox" class="noborder" name="checkbox" onclick="checkChanged(this)" value="<%=tablesname.get(i) %>"><%=tablesname.get(i) %>
  								</td>
  							<%
  									if(j==4 && i!=tablesname.size()-1 )
  									{
  										j=0;
  							%>
  							</tr>
  							<tr bgcolor="#FFFFFF" height=25>
  							<% 
  									}
  								}
  								for(;j<4;j++)
  								{
  							%> 
  							<td></td>
  							<%
  								}
  							 %>   			
  							</tr>	  				
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
