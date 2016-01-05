<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.topology.model.TreeNode"%>
<%@page import="com.afunms.polling.node.DBNode"%>
<%@page import="com.afunms.application.dao.OraclePartsDao"%>
<%@page import="com.afunms.application.model.OracleEntity"%>
<%@page import="com.afunms.application.dao.DBTypeDao"%>
<%@page import="com.afunms.application.model.DBTypeVo"%>
<%@page import="com.afunms.common.util.SystemConstant"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.model.ManageXml"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.polling.base.Node"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>


<%
	String rootPath = request.getContextPath();
	//String menuTable = (String)request.getAttribute("menuTable");
	
	List list = (List)request.getAttribute("list");
	List nodeList = (List)request.getAttribute("nodeList");
	Hashtable nodeTagHash = (Hashtable)request.getAttribute("nodeTagHash");
    Hashtable nodeAlarmLevelHash = (Hashtable)request.getAttribute("nodeAlarmLevelHash");
	Hashtable treeNodeHash = (Hashtable)request.getAttribute("treeNodeHash");
	ManageXml manageXml = (ManageXml)request.getAttribute("manageXml");
	
	String bid = (String)request.getAttribute("bid");
		
	String flag = (String)request.getAttribute("flag");
	
	
	DBTypeDao typedao = null;
    DBTypeVo oracleType = null;
    try{
    	typedao = new DBTypeDao();
     	oracleType = (DBTypeVo)typedao.findByDbtype("oracle");
    }catch(Exception e){
    	e.printStackTrace();
    }finally{
    	if(typedao != null){
    		typedao.close();
    	}
    }
%>
	
	


<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		
		<script language="JavaScript" type="text/javascript">
			function showViewNode(viewId){
				if(viewId || viewId == 0){
					mainForm.action = "<%=rootPath%>/businessview.do?action=showViewNode&bid=<%=bid%>&viewId="+viewId;
					mainForm.submit();
				} else {
					alert("请选择视图！");
				}
			}
		</script>
		
		<script language="JavaScript">

			//公共变量
			var node="";
			var ipaddress="";
			var operate="";
			/**
			*根据传入的id显示右键菜单
			*/
			function showMenu(id,nodeid,ip)
			{	
				ipaddress=ip;
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
			
			function detail(url){
				if(url){
					node = url;
				}
				mainForm.action = node;
				mainForm.submit();
			}
			
		</script>
	</head>
	<body id="body" class="body" leftmargin="0" topmargin="0">
		<!-- 这里用来定义需要显示的右键菜单 -->
		<div id="itemMenu" style="display: none";>
		<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
			style="border: thin;font-size: 12px" cellspacing="0">
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.detail()">查看详情</td>
			</tr>
		</table>
		</div>
		<!-- 右键菜单结束-->
		<form id="mainForm" method="post" name="mainForm">
			<input type="hidden" id="flag" name="flag" value="<%=flag%>">
			<table id="body-container" class="body-container">
				<tr>
					<td class="td-container-main">
						<table id="container-main" class="container-main">
							<tr>
								<td class="td-container-main-content">
									<table id="container-main-content" class="container-main-content">
										<tr>
											<td>
												<table id="content-header" class="content-header">
								                	<tr>
									                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
									                	<td class="content-title"> &nbsp; 业务视图  &gt;&gt; <%=manageXml.getTopoName() %></td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									       			</tr>
									        	</table>
		        							</td>
		        						</tr>
		        						<tr>
		        							<td>
		        								<table id="content-body" class="content-body">
		        									<tr>
		        										<td>
		        											<table>
		        												<tr>
		        													<td class="body-data-title">名称</td> 
		        													<td class="body-data-title">IP</td>
		        													<td class="body-data-title">类型</td>
		        													<td class="body-data-title">状态</td>  
		        												</tr>
		        												<%
		        													if(list != null) {
		        														for(int i = 0 ; i < list.size(); i++){
		        															NodeDTO nodeDTO = (NodeDTO)list.get(i);
		        															String id = nodeDTO.getId() + "";
		        															String sid = "";
		        															
		        															String nodeTag = (String)nodeTagHash.get(nodeDTO);
		        															String url = "/detail/dispatcher.jsp?id=" + nodeTag;
		        															
		        															url += "&flag=1";
                                                                            
                                                                            String nodeAlarmLevel = (String)nodeAlarmLevelHash.get(nodeDTO);
		        												%>
				        												<tr <%=onmouseoverstyle%>>
				        													<td align="center" class="body-data-list"><a href="#" onclick="detail('<%=rootPath + url%>')"><%=nodeDTO.getName()%></a></td> 
				        													<td align="center" class="body-data-list"><%=nodeDTO.getIpaddress()%></td> 
				        													<td align="center" class="body-data-list"><%=nodeDTO.getSubtype()%></td> 
				        													<td align="center" class="body-data-list"><img src="<%=rootPath + "/resource/" + NodeHelper.getCurrentStatusImage(Integer.valueOf(nodeAlarmLevel))%>"></td> 
				        												</tr>
		        												<%
		        														}
		        													
		        													}
		        												%>
		        											</table>
		        										</td>
		        									</tr>
		        								</table>
		        							</td>
		        						</tr>
		        						<tr>
		        							<td>
		        								<table id="content-footer" class="content-footer">
		        									<tr>
		        										<td>
		        											<table width="100%" border="0" cellspacing="0" cellpadding="0">
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
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
