<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%
	String rootPath = request.getContextPath();
	String menuTable = (String)request.getAttribute("menuTable");
	String nodeid = (String)request.getAttribute("nodeid");
	String type = (String)request.getAttribute("type");
	String subtype = (String)request.getAttribute("subtype");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
		
		<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
		<script type="text/javascript" src="<%=rootPath%>/js/dwr/engine.js"></script> 
		<script type="text/javascript" src="<%=rootPath%>/js/dwr/util.js"></script> 
		<script type="text/javascript" src="<%=rootPath%>/js/detail/storagedetail.js"></script> 
		<script type="text/javascript" src="<%=rootPath%>/dwr/interface/StorageRemoteService.js"></script>
		
		<script language="JavaScript" type="text/JavaScript">
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
			//���Ӳ˵�	
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
			/**
			 * ���ݴ��� div �� id ��ʾ�Ҽ��˵�
			 
			function showMenu(divid, )
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
			*/
			/**
			*��ʾ�����˵�
			*menuDiv:�Ҽ��˵�������
			*width:����ʾ�Ŀ���
			*rowControlString:�п����ַ�����0��ʾ����ʾ��1��ʾ��ʾ���硰101�������ʾ��1��3����ʾ����2�в���ʾ
			*/
			function popMenu(menuDivId,width,rowControlString)
			{
			    //���������˵�
			    //var pop = window.createPopup();
			    //���õ����˵�������
			    //pop.document.body.innerHTML= document.getElementById(menuDiv).innerHTML;
			    
			    var menuDiv = document.getElementById(menuDivId);
			    menuDiv.style.display = "inline";
			    var rowObjs = menuDiv.all[0].rows;
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
			         //���ø��еĸ߶�
			        rowObjs[i].style.height = "25";
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
			    menuDiv.document.oncontextmenu=function()
			    {
			            return false; 
			    }
			    //ѡ���Ҽ��˵���һ��󣬲˵�����
			    menuDiv.document.onclick=function()
			    {
			        menuDiv.style.display = "none";
			    }
			    //��ʾ�˵�
			    var elmt = event.srcElement; 
			    var offsetTop = elmt.offsetTop; 
				var offsetLeft = elmt.offsetLeft; 
			    var offsetWidth = elmt.offsetWidth; 
			    var offsetHeight = elmt.offsetHeight; 
			    while( elmt = elmt.offsetParent ) 
			    { 
			          // add this judge 
			        if ( elmt.style.position == 'absolute' || elmt.style.position == 'relative'  
			            || ( elmt.style.overflow != 'visible' && elmt.style.overflow != '' ) ) 
			        { 
			            break; 
			        }  
			        offsetTop += elmt.offsetTop; 
			        offsetLeft += elmt.offsetLeft; 
			    }
			    
			    menuDiv.style.posLeft = offsetLeft;
			    menuDiv.style.posTop = offsetTop;
			    return true;
			}
		
		</script>
	</head>
	<body id="body" class="body" onload="initmenu();">
		<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
		<form id="mainForm" method="post" name="mainForm">
			<input type="hidden" id="nodeid" name="nodeid" value="<%=nodeid%>">
			<input type="hidden" id="type" name="type" value="<%=type%>">
			<input type="hidden" id="subtype" name="subtype" value="<%=subtype%>">
			<input type="hidden" id="rootPath" name="rootPath" value="<%=rootPath%>">
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
								<td class="td-container-main-detail">
									<table id="container-main-detail" class="container-main-detail">
										<tr>
											<td>
												<table id="detail-content" class="detail-content">
													<tr>
														<td>
															<table id="detail-content-header" class="detail-content-header">
											                	<tr>
												                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
												                	<td class="detail-content-title">�豸��ϸ��Ϣ</td><td class="detail-content-title"><input type="button" id="freshSystemInfo" value="ˢ��"></td>
												                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
												       			</tr>
												        	</table>
					        							</td>
					        						</tr>
					        						<tr>
					        							<td>
					        								<table id="detail-content-body" class="detail-content-body">
					        									<tr>
					        										<td>
					        											<div id="systemInfo">
					        												<!-- �� div ������ʾ�豸ϵͳ��Ϣ -->
					        												<table>
												               					<tr>
												               						<td id="systemInfo_1" width="80%">
												               							<table>
								                    										<tr>
								                      											<td width="30%" height="29">&nbsp;����:</td>
								                      											<td width="70%" id="systemInfo_name"></td>
								                    										</tr>
								                    										<tr bgcolor="#F1F1F1">
								                      											<td width="30%" height="29">&nbsp;IP��ַ:</td>
								                      											<td width="70%" id="systemInfo_ipaddress"></td>
								                    										</tr>                    										
								                    										<tr >
								                    										
								                      											<td width="30%" height="29">&nbsp;����״̬:</td>
								                      											<td id="systemInfo_mon_flag"></td>
								                    										</tr>
								                    										<tr bgcolor="#F1F1F1">
								                      											<td width="30%" height="29">&nbsp;����״̬:</td>
								                      											<td width="70%" id="systemInfo_status"></td>
								                    										</tr>
								                    										<tr>
								                      											<td width="30%" height="29">&nbsp;�ͺ�:</td>
								                      											<td id="systemInfo_producerStr"></td>
								                    										</tr>
								                    										<tr bgcolor="#F1F1F1">
								                      											<td width="30%" height="29">&nbsp;���к�:</td>
								                      											<td width="70%" id="systemInfo_serialNumber"></td>
								                    										</tr>
				                    													</table>
				                    												</td>
				                    												<td id="systemInfo_2">
				                    													<table>
				                    														<tr>
				                    															<td id="currDayPingAvgInfo">
				                    															</td>
				                    														</tr>
				                    													</table>
				                    												</td>
				                    											</tr>                																	
				                											</table>
					        											</div>
					        										</td>
					        									</tr>
					        								</table>
					        							</td>
					        						</tr>
					        						<tr>
					        							<td>
					        								<table id="detail-content-footer" class="detail-content-footer">
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
										<tr>
											<td>
												<table id="detail-data" class="detail-data">
													<tr>
												    	<td class="detail-data-header">
												    		<div id="detailTabDIV"><!-- �� div ������ʾ��ǩҳ --></div>
												    	</td>
												  	</tr>
												  	<tr>
												    	<td>
												    		<table class="detail-data-body">
													      		<tr>
													      			<td>
													      				<div id="detailDataDIV">
													      					<!-- �� div ������ʾ��ǰ��ǩҳʱ��������ϸ��Ϣ -->
													      				</div>
															       	</td>
													      		</tr>
												    		</table>
												  		</td>
													</tr>
													<tr>
					        							<td>
					        								<table id="detail-content-footer" class="detail-content-footer">
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
					</td>
				</tr>
			</table>
		</form>
	</BODY>
</HTML>