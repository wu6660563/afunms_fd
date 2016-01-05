<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.indicators.model.NodeGatherIndicators"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.config.model.Diskconfig"%>

<%
	String rootPath = request.getContextPath();
  List list = (List)request.getAttribute("list");
  String nodeid = (String)request.getAttribute("nodeid");
  String ipaddress = (String)request.getAttribute("ipaddress");
%>


<html>
	<head>
	
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
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
			
			
			function refresh(){
				mainForm.action = "<%=rootPath%>/disk.do?action=toolbarrefresh&ipaddress=<%=ipaddress%>";
				mainForm.submit();
			}
			
			function edit(){
				mainForm.action = "<%=rootPath%>/nodeGatherIndicators.do?action=edit&id=" + node;
				mainForm.submit();
			}
			
			function toChooseNode(){
				mainForm.action = "<%=rootPath%>/nodeGatherIndicators.do?action=showtomultiaddlist";
				mainForm.submit();
			}
			
			
			  
			
		</script>
		<script type="text/javascript">
		
  			var delAction = "<%=rootPath%>/nodeGatherIndicators.do?action=delete";
  			var listAction = "<%=rootPath%>/nodeGatherIndicators.do?action=list";
		</script>
	</head>
	<body id="body" class="body" onload="initmenu();">
		<!-- ��������������Ҫ��ʾ���Ҽ��˵� -->
		<div id="itemMenu" style="display: none";>
		<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
			style="border: thin;font-size: 12px" cellspacing="0">
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.edit()">�༭</td>
			</tr>
		</table>
		</div>
		<!-- �Ҽ��˵�����-->
		<form id="mainForm" method="post" name="mainForm">
			<input type="hidden" name="nodeid" id="nodeid" value="<%=nodeid%>">
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
									                	<td class="content-title"> >> ���̷�ֵһ�� </td>
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
													    			<td  class="body-data-title" style="text-align: right;">
							    										<a href="#" onclick="refresh()">ˢ��</a>&nbsp;&nbsp;&nbsp;
							    										<!--<a href="#" onclick="toDelete()">ɾ��</a>&nbsp;&nbsp;&nbsp;
							    										<a href="#" onclick="toChooseNode()">����Ӧ��</a>&nbsp;&nbsp;&nbsp;-->
							        								</td>
							        							</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<table>
															<tr>
													    			<th width='5%' class="body-data-title"><INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()"></th>
					      											<td width="10%" align="center" class="body-data-title"><strong>IP��ַ</strong></td>  
					    											<td width="10%" align="center" class="body-data-title"><strong>��������</strong></td>    
					    											<td width="8%" align="center" class="body-data-title"><strong>����</strong></td>
					    											<td width="8%" align="center" class="body-data-title"><strong>һ����ֵ</strong></td>
					    											<td width="5%" align="center" class="body-data-title"><strong>�澯</strong></td>
					    											<td width="8%" align="center" class="body-data-title" ><strong>������ֵ</strong></td>
					    											<td width="5%" align="center" class="body-data-title"><strong>�澯</strong></td>
					    											<td width="8%" align="center" class="body-data-title"><strong>������ֵ</strong></td>
					    											<td width="5%" align="center" class="body-data-title"><strong>�澯</strong></td>
					      											<th width='8%' align="center" class="body-data-title">��ע</th>
					      											<th width='5%' align="center" class="body-data-title">����</th>
							        							</tr>
							        							<%
							        							
							        								    Diskconfig vo = null;
																    for(int i=0;i<list.size();i++)
																    {
																       vo = (Diskconfig)list.get(i);
							        									
							        										%>
							        											<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>>  
    											<td class="body-data-list">
    												<font color='blue'>&nbsp;<%=i+1%></font><INPUT type="checkbox" class=noborder name=checkbox value="<%=vo.getId()%>" class=noborder></td>
    											<td  class="body-data-list"><%=vo.getIpaddress()%></td>
    											<td  class="body-data-list"><%=vo.getName()%></td>
    											<%
    												String monflag = "��";
    												if(vo.getMonflag() == 0)monflag="��";
    												String sms = "��";
    												if(vo.getSms() == 0)sms="��";
    												String sms1 = "��";
    												if(vo.getSms1() == 0)sms1="��";
    												String sms2 = "��";
    												if(vo.getSms2() == 0)sms2="��";
    											%>
    											<td  class="body-data-list"><%=monflag%></td>
    											<td  class="body-data-list"><%=vo.getLimenvalue()%></td>
    											<td  class="body-data-list"><%=sms%></td>
    											<td  class="body-data-list"><%=vo.getLimenvalue1()%></td>
    											<td  class="body-data-list"><%=sms1%></td>
    											<td  class="body-data-list"><%=vo.getLimenvalue2()%></td>
    											<td  class="body-data-list"><%=sms2%></td>
    											<td  class="body-data-list"><%=vo.getBak()%></td>
											<td  class="body-data-list"><a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/disk.do?action=showedit&id=<%=vo.getId()%>","editdiskconfig", "height=400, width= 500, top=200, left= 200")'>
											<img src="<%=rootPath%>/resource/image/editicon.gif" border="0"/></a></td>
        										
  										</tr>
							        										<%
							        								
							        								}%>
															</table>
														</td>
													</tr>
													<tr>
																			<TD nowrap colspan="20" align=center><br>
																			<input type="button" value="�� ��" style="width:50" onclick="window.close()">
																			</TD>	
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
