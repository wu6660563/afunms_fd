<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.service.RoleOperationPermissionService"%>
<%@page import="com.afunms.system.service.OperationPermissionService"%>
<%@ include file="/include/globe.inc"%>
<%
	String rootPath = request.getContextPath();
	List list = (List) request.getAttribute("list");
	JspPage jp = (JspPage) request.getAttribute("page");
	String findselect=(String)session.getAttribute("findselect");
    User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
    RoleOperationPermissionService roleOperationPermissionService = RoleOperationPermissionService.getInstance(user, OperationPermissionService.getKnowledgebaseOperationPermission());
    boolean isCreateOperation = roleOperationPermissionService.isCreateOperationPermission();
    boolean isUpdateOperation = roleOperationPermissionService.isUpdateOperationPermission();
    boolean isDeleteOperation = roleOperationPermissionService.isDeleteOperationPermission();
    String createOperationDisable = "inline";
    if (!isCreateOperation) {
        createOperationDisable = "none";
    }
    String updateOperationDisable = "inline";
    if (!isUpdateOperation) {
        updateOperationDisable = "none";
    }
    String deleteOperationDisable = "inline";
    if (!isDeleteOperation) {
        deleteOperationDisable = "none";
    }
%>
<%
	String menuTable = (String) request.getAttribute("menuTable");
%>
<html>
	<head>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet"
			type="text/css" />
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet"
			type="text/css" />
		<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css"
			rel="stylesheet">
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css"
			rel="stylesheet">
		<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet"
			type="text/css">
		<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css"
			type="text/css">
		<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet"
			type="text/css">
			
		<script language="JavaScript" type="text/javascript">
  var listAction = "<%=rootPath%>/knowledgebase.do?action=list";
  var delAction = "<%=rootPath%>/knowledgebase.do?action=delete";
  var curpage= <%=jp.getCurrentPage()%>;
  var totalpages = <%=jp.getPageTotal()%>;
  function toAdd()
  {
     mainForm.action = "<%=rootPath%>/knowledgebase.do?action=ready_add";
     mainForm.submit();
  }
  
  /**
	*查询方法
	*/
  function toFind()
  {
     mainForm.action = "<%=rootPath%>/knowledgebase.do?action=find";
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
<!-- 三级级联菜单 数据从表nms_alarm_indicators中查取 -->
		<script type="text/javascript">
			SPT="--请选择类型--";
			SCT="--请选择类别--";
			SAT="--请选择指标--";
			ShowT=1;		//提示文字 0:不显示 1:显示
			PCAD="<%=findselect%>";
			if(ShowT)PCAD=SPT+"$"+SCT+","+SAT+"#"+PCAD;
			PCAArea=[];
			PCAP=[];
			PCAC=[];
			PCAA=[];
			PCAN=PCAD.split("#");
			for(i=0;i<PCAN.length;i++)
			{
				PCAA[i]=[];TArea=PCAN[i].split("$")[1].split("|");
				for(j=0;j<TArea.length;j++)
				{
					PCAA[i][j]=TArea[j].split(",");
					if(PCAA[i][j].length==1)
					PCAA[i][j][1]=SAT;TArea[j]=TArea[j].split(",")[0]
				}
					PCAArea[i]=PCAN[i].split("$")[0]+","+TArea.join(",");
					PCAP[i]=PCAArea[i].split(",")[0];
					PCAC[i]=PCAArea[i].split(',')
			}
	
			function PCAS()
			{
				this.SelP=document.getElementsByName(arguments[0])[0];
				this.SelC=document.getElementsByName(arguments[1])[0];
				this.SelA=document.getElementsByName(arguments[2])[0];
				this.DefP=this.SelA?arguments[3]:arguments[2];
				this.DefC=this.SelA?arguments[4]:arguments[3];
				this.DefA=this.SelA?arguments[5]:arguments[4];
				this.SelP.PCA=this;
				this.SelC.PCA=this;
				this.SelP.onchange=function(){PCAS.SetC(this.PCA)};
				if(this.SelA)
				this.SelC.onchange=function(){PCAS.SetA(this.PCA)};
				PCAS.SetP(this)
			};
				
				PCAS.SetP=function(PCA){
				for(i=0;i<PCAP.length;i++){PCAPT=PCAPV=PCAP[i];
				if(PCAPT==SPT)PCAPV="";
				PCA.SelP.options.add(new Option(PCAPT,PCAPV));
				if(PCA.DefP==PCAPV)PCA.SelP[i].selected=true}PCAS.SetC(PCA)
				};
				
				PCAS.SetC=function(PCA){
				PI=PCA.SelP.selectedIndex;
				PCA.SelC.length=0;
				for(i=1;i<PCAC[PI].length;i++){PCACT=PCACV=PCAC[PI][i];
				if(PCACT==SCT)PCACV="";
				PCA.SelC.options.add(new Option(PCACT,PCACV));
				if(PCA.DefC==PCACV)
				PCA.SelC[i-1].selected=true}
				if(PCA.SelA)PCAS.SetA(PCA)
				};
				
				PCAS.SetA=function(PCA){
				PI=PCA.SelP.selectedIndex;
				CI=PCA.SelC.selectedIndex;
				PCA.SelA.length=0;
				for(i=1;i<PCAA[PI][CI].length;i++){
				PCAAT=PCAAV=PCAA[PI][CI][i];
				if(PCAAT==SAT)
				PCAAV="";
				PCA.SelA.options.add(new Option(PCAAT,PCAAV));
				if(PCA.DefA==PCAAV)PCA.SelA[i-1].selected=true}
				}
			</script>		

	</head>
	<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa"
		onload="initmenu();">
		<form method="post" name="mainForm">
			<table border="0" id="table1" cellpadding="0" cellspacing="0"
				width=100%>
				<tr>
					<td width="200" valign=top align=center>
						<%=menuTable%>
					</td>
					<td align="center" valign=top bgcolor="#ababab">
						<table width="98%" cellpadding="0" cellspacing="0" algin="center">
							<tr>
								<td background="<%=rootPath%>/common/images/right_t_02.jpg"
									width="100%">
									<table width="100%" cellspacing="0" cellpadding="0">
										<tr>
											<td align="left">
												<img src="<%=rootPath%>/common/images/right_t_01.jpg"
													width="5" height="29" />
											</td>
											<td class="layout_title">
												系统管理 >> 知识库管理 >> 知识列表
											</td>
											<td align="right">
												<img src="<%=rootPath%>/common/images/right_t_03.jpg"
													width="5" height="29" />
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
									<table width="100%" cellpadding="0" cellspacing="1">
										<tr>
											<td bgcolor="#ECECEC" width="8%" align="center">
												类别:<select size="1" name="categorycon">
													</select>
											</td>
											<td bgcolor="#ECECEC" width="14%" align="center">
												类型：<select size="1" name="entitycon">
													</select>
											</td>
											<td bgcolor="#ECECEC" width="12%" align="center">
												指标：<select size="1" name="subentitycon">
													</select>
											</td>
											<script language="javascript" defer>
												new PCAS("categorycon","entitycon","subentitycon","","","");
											</script>
											<td bgcolor="#ECECEC" width="16%" align="center">
												请输入查询关键字&nbsp;&nbsp;<input type="text" value="" name="wordkey" width="20">
												<input type="button" name="find" value="查询" onclick="toFind()">
											</td>
											<td bgcolor="#ECECEC" width="8%" align='right'>
												<a href="#" onclick="toAdd()" style="display: <%=createOperationDisable%>">添加</a>
												<a href="#" onclick="toDelete()" style="display: <%=deleteOperationDisable%>">删除</a>&nbsp;&nbsp;&nbsp;
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
									<table width="100%" cellpadding="0" cellspacing="1">
										<tr>
											<td bgcolor="#ECECEC" width="80%" align="center">
												<jsp:include page="../../common/page.jsp">
													<jsp:param name="curpage" value="<%=jp.getCurrentPage()%>" />
													<jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>" />
												</jsp:include>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<table cellspacing="1" cellpadding="0" width="100%" style="table-layout:auto;">
										<tr class="microsoftLook0" height=28>
											<th width='5%'>
												<INPUT type="checkbox" class=noborder name="checkall"
													onclick="javascript:chkall()">
											</th>
											<th width='5%'>
												序号
											</th>
											<th width='5%'>
												类别
											</th>
											<th width='5%'>
												类型
											</th>
											<th width="5%">
												指标
											</th>
											<th width="10%">
												标题
											</th>
											<th width="15%">
												内容
											</th>
											<th width='10%'>
												附件
											</th>
											<th width='10%'>
												上次修改时间
											</th>
											<th width='10%'>
												添加用户ID
											</th>
											<th width='5%'>编辑</th>
										</tr>
										<%
											User x=(User)session.getAttribute(SessionConstant.CURRENT_USER);
											int startRow = jp.getStartRow();
											Knowledgebase vo=null;
											for (int i = 0; i < list.size(); i++) {
												vo = (Knowledgebase) list.get(i);
												String attachfiles = vo.getAttachfiles();
												request.setAttribute("attachfiles",attachfiles);
										%>
										<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
											class="microsoftLook" height=25>
											<td align='center'>
												<INPUT type="checkbox" class=noborder name=checkbox
													value="<%=vo.getId()%>">
											</td>
											<td align='center'><a href="#"  style="cursor:hand" onclick="window.showModalDialog('<%=rootPath%>/knowledge.do?action=read&id=<%=vo.getId()%>',window,',dialogHeight:400px;dialogWidth:600px')">
												<font color='blue'><%=startRow + i%></font></a>
											</td>
											<td align='center'><%=vo.getCategory()%></td>
											<td align='center'><%=vo.getEntity()%></td>
											<td align='center'><%=vo.getSubentity()%></td>
											<td align='center'><%=vo.getTitles()%></td>
											<td align='center'><%=vo.getContents()%></td>
											<td align='center'><a href="#" onclick="window.open('<%=rootPath%>/config/knowledgebase/download.jsp?name=<%=attachfiles %>')"><%=vo.getAttachfiles()%></a></td>
											<td align='center'><%=vo.getKtime()%></td>
											<td align='center'><%=vo.getUserid()%></td>
											<td align='center'><a style="display: <%=updateOperationDisable%>" href="<%=rootPath%>/knowledgebase.do?action=ready_edit&id=<%=vo.getId()%>"><img src="<%=rootPath%>/resource/image/editicon.gif" border="0"/></a></td>
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
									</table>
								</td>
							</tr>
						</table>

					</td>
				</tr>
			</table>
	</BODY>
</HTML>
