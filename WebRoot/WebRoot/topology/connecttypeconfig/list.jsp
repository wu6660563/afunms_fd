<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.topology.model.ConnectTypeConfig"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>

<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  List list = (List)request.getAttribute("list");
  Hashtable hostNodeHashtable = (Hashtable)request.getAttribute("hostNodeHashtable");
%>


<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript">
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
			//��Ӳ˵�	
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
		<script type="text/javascript">
			function chkall(){
				var checkall = document.getElementById("checkall");
				var checkboxes = document.getElementsByName("checkbox");
				for(var i = 0 ; i < checkboxes.length; i++){
					var checkbox = checkboxes[i];
					checkbox.checked = checkall.checked;
				}
			}
		</script>
		<script type="text/javascript">
			//��������
			var node="";
			var ipaddress="";
			var operate="";
			/**
			*���ݴ����id��ʾ�Ҽ��˵�
			*/
			function showMenu(id,nodeid,ip,showItemMenu)
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
			        popMenu(itemMenu,100,showItemMenu);
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
			
			function updateHostCollectionAgreement()
			{
				location.href="<%=rootPath%>/connectConfig.do?action=readyConnectConfig&id="+node;
			}
			
			function showChildNode(){
				openWindow("<%=rootPath%>/remotePing.do?action=showChildNode&node="+node);
			}
			
			function setChildNode(){
				var url = "<%=rootPath%>/remotePing.do?action=setChildNode&node="+node;
				openWindow(url);
			}
			function deleteCollectionAgreement(endpoint)
	{
		location.href="<%=rootPath%>/remotePing.do?action=deleteCollectionAgreement&id="+node + "&endpoint="+endpoint;
	}
		</script>
		
		<script type="text/javascript">
			function openWindow(url){
				window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
			}

	
		</script>
	</head>
	<body id="body" class="body" onload="initmenu();">
	<div id="itemMenu" style="display: none";>
			<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
				style="border: thin;font-size: 12px" cellspacing="0">
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.updateHostCollectionAgreement()">
						�༭
					</td>
				</tr>
				<!--<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.showChildNode()">
						�鿴�ӽڵ�
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.setChildNode()">
						�����ӽڵ�
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.deleteCollectionAgreement(0)">
						ȡ������Э��
					</td>
				</tr>-->
			</table>
		</div>
		<form id="mainForm" method="post" name="mainForm">
			<table id="body-container" class="body-container">
				<tr>
					<td class="td-container-menu-bar">
						<table id="container-menu-bar" class="container-menu-bar">
							<tr>
								<td>
									<%=menuTable%>
								</td>	
							</tr>
						</table>
					</td>
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
									                	<td class="content-title"> ��Դ >> �豸ά�� >> �豸��ͨ�Լ������ >> �豸��ͨ�Լ�������б�</td>
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
		        											<table cellspacing="0" border="1" bordercolor="#ababab">
		        												<tr height=28 style="background:#ECECEC" align="center" class="content-title">
		        													<td><INPUT type="checkbox" id="checkall" name="checkall" onclick="javascript:chkall()">���</td>
		        													<td align="center">�豸����</td>
		        													<td align="center">IP��ַ</td>
		        													<td align="center">��ⷽʽ</td>
		        													<td align="center">�û���</td>
		        													<td align="center">��½��ʾ��</td>
		        													<td align="center">������ʾ��</td>
		        													<td align="center">shell��ʾ��</td>
		        													<td align="center">����</td>
		        												</tr>
		        												<%
		        												
		        												if(list!=null && list.size()>0 && hostNodeHashtable !=null && hostNodeHashtable.size()>0){
			        												for(int i = 0 ; i < list.size(); i ++){
			        													ConnectTypeConfig remotePingHost = (ConnectTypeConfig)list.get(i);
			        													HostNode hostNode = (HostNode)hostNodeHashtable.get(remotePingHost.getNode_id());
			        													String collectAgreement = remotePingHost.getConnecttype();;
			        													if(hostNode == null)continue;
			        													String showItemMenu = "";
																		if (hostNode.getEndpoint() == 0) {
																			//collectAgreement = "snmp";
																			showItemMenu = "0000";
																		}else if(hostNode.getEndpoint() == 1){
																			//collectAgreement = "Ping ������";	
																			showItemMenu = "1111";
																		}else if(hostNode.getEndpoint() == 2){
																			//collectAgreement = "Ping �ڵ�";	
																			showItemMenu = "0001";
																		}else if(hostNode.getEndpoint() == 3){
																			//collectAgreement = "Telnet";	
																			showItemMenu = "1001";
																		}else if(hostNode.getEndpoint() == 4){
																			//collectAgreement = "SSH";	
																			showItemMenu = "1001";
																		}
																		showItemMenu = "1111";
			        													%>
			        													<tr <%=onmouseoverstyle%>>
			        														<td>
			        															<input type="checkbox" id="<%=remotePingHost.getId()%>" name="checkbox" value="<%=remotePingHost.getId()%>">
			        															<%=i+1%>
			        														</td>
			        														<td>&nbsp;<%=hostNode.getAlias()%></td>
			        														<td>&nbsp;<%=hostNode.getIpAddress()%></td>
																		<td>&nbsp;<%=collectAgreement%></td>
			        														<td>&nbsp;<%=remotePingHost.getUsername()%></td>
			        														<td>&nbsp;<%=remotePingHost.getLoginPrompt()%></td>
			        														<td>&nbsp;<%=remotePingHost.getPasswordPrompt()%></td>
			        														<td>&nbsp;<%=remotePingHost.getShellPrompt()%></td>
			        														<td>
																				&nbsp;&nbsp;
																				<img src="<%=rootPath%>/resource/image/status.gif"
																					border="0" width=15 oncontextmenu=showMenu('2','<%=hostNode.getId()%>','<%=hostNode.getIpAddress()%>','<%=showItemMenu%>')>
					
					
																			</td>
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
