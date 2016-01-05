<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.indicators.model.GatherIndicators;"%>

<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  List list = (List)request.getAttribute("list");
  
  JspPage jp = (JspPage)request.getAttribute("page");
  String gatherfindselect=(String)session.getAttribute("gatherfindselect");
%>


<html>
	<head>
	
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		
		<!-- 二级级级联菜单 -->
		<script type="text/javascript">
			SPT="--请选择类型--";
			SCT="--请选择类别--";
			SAT="--请选择指标--";
			ShowT=1;		//提示文字 0:不显示 1:显示
			PCAD="<%=gatherfindselect%>";
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
			
			
			function add(){
				mainForm.action = "<%=rootPath%>/gatherIndicators.do?action=add";
				mainForm.submit();
			}
			
			function edit(){
				mainForm.action = "<%=rootPath%>/gatherIndicators.do?action=edit&id=" + node;
				mainForm.submit();
			}
			function toFind()
			  {
			     mainForm.action = "<%=rootPath%>/gatherIndicators.do?action=find";
			     mainForm.submit();
			  }
			
			
			  
			
		</script>
		<script type="text/javascript">
		
			var curpage= <%=jp.getCurrentPage()%>;
  			var totalpages = <%=jp.getPageTotal()%>;
  			var delAction = "<%=rootPath%>/gatherIndicators.do?action=delete";
  			var listAction = "<%=rootPath%>/gatherIndicators.do?action=list";
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
									                	<td class="content-title">&nbsp;资源 >> 性能监视 >> 监视指标一览表 </td>
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
													    			<td  class="body-data-title">
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
														<td>
															<table>
																<tr>
																<%
																	String select3="--请选择类型--";
																	String select4="--请选择类型--";
																	select3=(String)request.getAttribute("con3");
																	select4=(String)request.getAttribute("con4");
																 %>
																	<td class="body-data-title" style="text-align: right;">
																		类别:<select size="1" name="categorycon">
																			</select>
																	</td>
																	<td class="body-data-title" style="text-align: right;">
																		类型：<select size="1" name="entitycon">
																			</select>
																	</td>
																	<script language="javascript" defer>
																		new PCAS("categorycon","entitycon","<%=select3%>","<%=select4%>");
																	</script>
																	<td class="body-data-title" style="text-align: right;">
																	<input type="button" name="find" value="查询" onclick="toFind()">
																	</td>
													    			<td  class="body-data-title" style="text-align: right;">
													    				<a href="#" onclick="window.open('<%=rootPath%>/nodeGatherIndicators.do?action=list&nodeid=173&type=db&subtype=Oracle')">设备采集指标列表</a>&nbsp;&nbsp;&nbsp;
							    										<a href="#" onclick="add()">添加</a>&nbsp;&nbsp;&nbsp;
							    										<a href="#" onclick="toDelete()">删除</a>&nbsp;&nbsp;&nbsp;
							        								</td>
							        							</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<table>
																<tr>
													    			<td class="body-data-title"><INPUT type="checkbox" name="checkall" onclick="javascript:chkall()">序号</td>
													    			<td class="body-data-title">名称</td>
													    			<td class="body-data-title">别名</td>
													    			<td class="body-data-title">类型</td>
													    			<td class="body-data-title">子类型</td>
													    			<td class="body-data-title">是否采集</td>
													    			<td class="body-data-title">是否用于默认设置</td>
													    			<td class="body-data-title">采集间隔</td>
													    			<td class="body-data-title">操作</td>
							        							</tr>
							        							<%
							        							
							        								if(list != null && list.size() > 0) {
							        									for(int i = 0; i < list.size(); i++){
							        										GatherIndicators gatherIndicators = (GatherIndicators)list.get(i);
							        										
							        										String unit = "";
							        										if("m".equals(gatherIndicators.getInterval_unit())){
							        											unit = "分钟";
							        										}else if("h".equals(gatherIndicators.getInterval_unit())){
							        											unit = "小时";
							        										}else if("d".equals(gatherIndicators.getInterval_unit())){
							        											unit = "天";
							        										}else if("w".equals(gatherIndicators.getInterval_unit())){
							        											unit = "周";
							        										}else if("mt".equals(gatherIndicators.getInterval_unit())){
							        											unit = "月";
							        										}else if("y".equals(gatherIndicators.getInterval_unit())){
							        											unit = "年";
							        										}
							        										String isCollection = "否";
							        										if("1".equals(gatherIndicators.getIsCollection())){
							        											isCollection = "是";
							        										}
							        										String isDefault = "否";
							        										if("1".equals(gatherIndicators.getIsDefault())){
							        											isDefault = "是";
							        										}
							        										%>
							        											<tr <%=onmouseoverstyle%>>
																	    			<td class="body-data-list"><INPUT type="checkbox" name="checkbox" value="<%=gatherIndicators.getId()%>"><%=jp.getStartRow()+i%></td>
																	    			<td class="body-data-list"><%=gatherIndicators.getName()%></td>
																	    			<td class="body-data-list"><%=gatherIndicators.getAlias()%></td>
																	    			<td class="body-data-list"><%=gatherIndicators.getType()%></td>
																	    			<td class="body-data-list"><%=gatherIndicators.getSubtype()%></td>
																	    			<td class="body-data-list"><%=isCollection%></td>
																	    			<td class="body-data-list"><%=isDefault%></td>
																	    			<td class="body-data-list"><%=gatherIndicators.getPoll_interval() + " " + unit%></td>
																	    			<!-- 
																	    			<td class="body-data-list"><img src="<//%=rootPath%>/resource/image/status.gif"
																						border="0" width=15 oncontextmenu=showMenu('2','<//%=gatherIndicators.getId()%>','') alt="右键操作">
																					</td>
																					 -->
																					<td  align='center'  class="body-data-list"><a href="<%=rootPath%>/gatherIndicators.do?action=edit&id=<%=gatherIndicators.getId()%>">
							       														<img src="<%=rootPath%>/resource/image/editicon.gif" border="0" alt="编辑"/></a>
							       													</td>
											        							</tr>
							        										<%
							        									}
							        								}%>
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
