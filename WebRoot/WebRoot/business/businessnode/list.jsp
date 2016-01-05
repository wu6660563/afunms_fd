<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.business.model.BusinessNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.business.dao.*" %>
<%@page import="com.afunms.business.model.BusCollectType"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.topology.model.*"%>
<%@page import="java.util.*"%>
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
  var listAction = "<%=rootPath%>/businessNode.do?action=list";
  var delAction = "<%=rootPath%>/businessNode.do?action=delete";
  var curpage= <%=jp.getCurrentPage()%>;
  var totalpages = <%=jp.getPageTotal()%>;
  var xx = "";
  function toedit(){
      window.location="<%=rootPath%>/businessNode.do?action=ready_edit&id="+xx;
  }
  
  function toAdd()
  {
     mainForm.action = "<%=rootPath%>/businessNode.do?action=ready_add";
     mainForm.submit();
  }
   
   function search()
  {
	 
       mainForm.action = "<%=rootPath%>/businessNode.do?action=search";
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



	//公共变量
	var node="";
	var ipaddress="";
	var operate="";
	/**
	*根据传入的id显示右键菜单
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
	*显示弹出菜单
	*menuDiv:右键菜单的内容
	*width:行显示的宽度
	*rowControlString:行控制字符串，0表示不显示，1表示显示，如“101”，则表示第1、3行显示，第2行不显示
	*/
	function popMenu(menuDiv,width,rowControlString)
	{
	    //创建弹出菜单
	    var pop=window.createPopup();
	    //设置弹出菜单的内容
	    pop.document.body.innerHTML=menuDiv.innerHTML;
	    var rowObjs=pop.document.body.all[0].rows;
	    //获得弹出菜单的行数
	    var rowCount=rowObjs.length;
	    //alert("rowCount==>"+rowCount+",rowControlString==>"+rowControlString);
	    //循环设置每行的属性
	    for(var i=0;i<rowObjs.length;i++)
	    {
	        //如果设置该行不显示，则行数减一
	        var hide=rowControlString.charAt(i)!='1';
	        if(hide){
	            rowCount--;
	        }
	        //设置是否显示该行
	        rowObjs[i].style.display=(hide)?"none":"";
	        //设置鼠标滑入该行时的效果
	        rowObjs[i].cells[0].onmouseover=function()
	        {
	            this.style.background="#397DBD";
	            this.style.color="white";
	        }
	        //设置鼠标滑出该行时的效果
	        rowObjs[i].cells[0].onmouseout=function(){
	            this.style.background="#F1F1F1";
	            this.style.color="black";
	        }
	    }
	    //屏蔽菜单的菜单
	    pop.document.oncontextmenu=function()
	    {
	            return false; 
	    }
	    //选择右键菜单的一项后，菜单隐藏
	    pop.document.onclick=function()
	    {
	        pop.hide();
	    }
	    //显示菜单
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
<!-- 这里用来定义需要显示的右键菜单 -->
	<div id="itemMenu" style="display: none";>
	<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
		style="border: thin;font-size: 12px" cellspacing="0">
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.edit()">修改信息</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.cancelmanage()">取消监视</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.addmanage()">添加监视</td>
		</tr>		
	</table>
	</div>
	<!-- 右键菜单结束-->
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
                    <td class="layout_title"><b>资源 >> 视图管理 >> 业务属性节点列表</b></td>
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
				&nbsp;&nbsp;&nbsp;&nbsp;查询：所属业务
		    	<select size=1 id='managexmlid' name='managexmlid' style='width:108px;'>
								
								<% 
								
						   		List xmllist = (List)request.getAttribute("xmllist");
						   		if(xmllist == null)xmllist = new ArrayList();
						   for(int i=0;i<xmllist.size();i++)
						   { 
						   ManageXml vo=(ManageXml)xmllist.get(i);
						%>	
								
								<option value="<%=vo.getId() %>"><%=vo.getTopoName() %></option>
								<%} %>
									</select>  
				业务节点名称：<INPUT type="text" name="value" width="15" class="formStyle">
		    	&nbsp;&nbsp;<input type="submit" value="搜索" onclick=" return search()" class="button">     							
          							</td>   


		        		
					            <td bgcolor="#ECECEC" width="100%" align='right'>
			<a href="#" onclick="toAdd()">添加</a>
			<a href="#" onclick="toDelete()">删除</a>&nbsp;&nbsp;&nbsp;
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
					<tr class="microsoftLook0" height=28>
						<th width='5%'><INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()"></th>
   						<th width='5%'>序号</th>
						<th width='10%'>名称</th>
                        <th width='10%'>所属业务视图</th>
   						<th width='20%'>描述</th>
   						<th width='10%'>采集类型</th>    
   						<th width="15%">方法</th>
						<th width='5%'>是否监视</th>
   						<th width='10%'>编辑</th>
					</tr>
<%
    String sex = "";
    BusinessNode vo = null;
    int startRow = jp.getStartRow();
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
    	<td ><font color='blue'><%=startRow + i%></font></td>
        <td align='center'><%=vo.getName()%></td>
		
		<td align='center'><%=veiwname%></td>
    	<td align='center'><%=vo.getDesc()%></td>
    	<td align='center'>
    	&nbsp;<%=ht.get(vo.getCollecttype()) %></td>
    	<td align='center'><%=vo.getMethod()%></td>
        <td align='center'><%
				if(vo.getFlag() == 0){
			%>
			&nbsp;未监视
			<%
				}else{
			%>	
			&nbsp;已监视
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