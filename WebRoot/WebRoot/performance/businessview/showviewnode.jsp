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
					alert("��ѡ����ͼ��");
				}
			}
		</script>
		
		<script language="JavaScript">

			//��������
			var node="";
			var ipaddress="";
			var operate="";
			/**
			*���ݴ����id��ʾ�Ҽ��˵�
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
			*��ʾ�����˵�
			*menuDiv:�Ҽ��˵�������
			*width:����ʾ�Ŀ��
			*rowControlString:�п����ַ�����0��ʾ����ʾ��1��ʾ��ʾ���硰101�������ʾ��1��3����ʾ����2�в���ʾ
			*/
			function popMenu(menuDiv,width,rowControlString)
			{
			    //���������˵�
			    var pop=window.createPopup();
			    //���õ����˵�������
			    pop.document.body.innerHTML=menuDiv.innerHTML;
			    var rowObjs=pop.document.body.all[0].rows;
			    //��õ����˵�������
			    var rowCount=rowObjs.length;
			    //alert("rowCount==>"+rowCount+",rowControlString==>"+rowControlString);
			    //ѭ������ÿ�е�����
			    for(var i=0;i<rowObjs.length;i++)
			    {
			        //������ø��в���ʾ����������һ
			        var hide=rowControlString.charAt(i)!='1';
			        if(hide){
			            rowCount--;
			        }
			        //�����Ƿ���ʾ����
			        rowObjs[i].style.display=(hide)?"none":"";
			        //������껬�����ʱ��Ч��
			        rowObjs[i].cells[0].onmouseover=function()
			        {
			            this.style.background="#397DBD";
			            this.style.color="white";
			        }
			        //������껬������ʱ��Ч��
			        rowObjs[i].cells[0].onmouseout=function(){
			            this.style.background="#F1F1F1";
			            this.style.color="black";
			        }
			    }
			    //���β˵��Ĳ˵�
			    pop.document.oncontextmenu=function()
			    {
			            return false; 
			    }
			    //ѡ���Ҽ��˵���һ��󣬲˵�����
			    pop.document.onclick=function()
			    {
			        pop.hide();
			    }
			    //��ʾ�˵�
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
		<!-- ��������������Ҫ��ʾ���Ҽ��˵� -->
		<div id="itemMenu" style="display: none";>
		<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
			style="border: thin;font-size: 12px" cellspacing="0">
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.detail()">�鿴����</td>
			</tr>
		</table>
		</div>
		<!-- �Ҽ��˵�����-->
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
									                	<td class="content-title"> &nbsp; ҵ����ͼ  &gt;&gt; <%=manageXml.getTopoName() %></td>
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
		        													<td class="body-data-title">����</td> 
		        													<td class="body-data-title">IP</td>
		        													<td class="body-data-title">����</td>
		        													<td class="body-data-title">״̬</td>  
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
