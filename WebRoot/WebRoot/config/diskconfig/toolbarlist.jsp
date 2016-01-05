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
		<!-- 这里用来定义需要显示的右键菜单 -->
		<div id="itemMenu" style="display: none";>
		<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
			style="border: thin;font-size: 12px" cellspacing="0">
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.edit()">编辑</td>
			</tr>
		</table>
		</div>
		<!-- 右键菜单结束-->
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
									                	<td class="content-title"> >> 磁盘阀值一览 </td>
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
							    										<a href="#" onclick="refresh()">刷新</a>&nbsp;&nbsp;&nbsp;
							    										<!--<a href="#" onclick="toDelete()">删除</a>&nbsp;&nbsp;&nbsp;
							    										<a href="#" onclick="toChooseNode()">批量应用</a>&nbsp;&nbsp;&nbsp;-->
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
					      											<td width="10%" align="center" class="body-data-title"><strong>IP地址</strong></td>  
					    											<td width="10%" align="center" class="body-data-title"><strong>磁盘名称</strong></td>    
					    											<td width="8%" align="center" class="body-data-title"><strong>启用</strong></td>
					    											<td width="8%" align="center" class="body-data-title"><strong>一级阀值</strong></td>
					    											<td width="5%" align="center" class="body-data-title"><strong>告警</strong></td>
					    											<td width="8%" align="center" class="body-data-title" ><strong>二级阀值</strong></td>
					    											<td width="5%" align="center" class="body-data-title"><strong>告警</strong></td>
					    											<td width="8%" align="center" class="body-data-title"><strong>三级阀值</strong></td>
					    											<td width="5%" align="center" class="body-data-title"><strong>告警</strong></td>
					      											<th width='8%' align="center" class="body-data-title">备注</th>
					      											<th width='5%' align="center" class="body-data-title">操作</th>
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
    												String monflag = "是";
    												if(vo.getMonflag() == 0)monflag="否";
    												String sms = "是";
    												if(vo.getSms() == 0)sms="否";
    												String sms1 = "是";
    												if(vo.getSms1() == 0)sms1="否";
    												String sms2 = "是";
    												if(vo.getSms2() == 0)sms2="否";
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
																			<input type="button" value="关 闭" style="width:50" onclick="window.close()">
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
