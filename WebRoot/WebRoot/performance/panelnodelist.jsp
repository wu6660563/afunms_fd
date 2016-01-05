<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.config.dao.IpaddressPanelDao"%>
<%@page import="com.afunms.config.model.IpaddressPanel"%>
<%@page import="com.afunms.config.dao.PanelModelDao"%>
<%@page import="com.afunms.config.model.PanelModel"%>
<%@page import="com.afunms.polling.node.Host"%>
<%@page import="com.afunms.polling.PollingEngine"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/include/globe.inc"%>

<%
	String rootPath = request.getContextPath();
  	List hostNodeList = (List)request.getAttribute("list");
  	JspPage jp = (JspPage)request.getAttribute("page");
  	String rvalue = (String)request.getAttribute("rvalue");
  	String path = rootPath+"/panel/view/image/";
  	String menuTable = (String)request.getAttribute("menuTable");
  	
  	String flag = (String)request.getAttribute("flag");
  	String id = (String)request.getParameter("id");
  	
  	List list = new ArrayList();
  	
  	boolean isShowMessage = false;
  	
  	
  	if("1".equals(flag) && hostNodeList != null && id !=null && id.trim().length() > 0){
  		for(int i = 0 ; i < hostNodeList.size() ; i++){
  			HostNode hostNode = (HostNode)hostNodeList.get(i);
  			if(hostNode.getId() == Integer.valueOf(id)){
  				list.add(hostNode);
  			}
  		}
  		isShowMessage = true;
  	}else{
  		list = hostNodeList;
  	}
  	
  	int rc = 0;
  	if(list != null){
  		rc = list.size();
  	}
  	
  	
  	
%>


<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		
		<link rel="stylesheet" type="text/css" href="<%=rootPath%>/resource/css/examples.css" />
		<link rel="stylesheet" type="text/css" href="<%=rootPath%>/resource/css/chooser.css" />
		<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/application/environment/resource/ext3.1/resources/css/ext-all.css" />
		<script type="text/javascript" 	src="<%=rootPath%>/application/environment/resource/ext3.1/adapter/ext/ext-base.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/application/environment/resource/ext3.1/ext-all.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/application/environment/resource/ext3.1/ext-all-debug.js"></script>
		
		
		<script language="javascript">	
			Ext.onReady(function() { 
					
					<%
						 for(int i=0;i<rc;i++){
					       HostNode vo = (HostNode)list.get(i);
					       IpaddressPanelDao ipaddressPanelDao = new IpaddressPanelDao();
					       IpaddressPanel ipaddressPanel = ipaddressPanelDao.loadIpaddressPanel(vo.getIpAddress());
					       String imageType = "";
					       if(ipaddressPanel == null){
					       		continue;
					       }
					       String oid = vo.getSysOid().replace(".","-");
					       imageType = ipaddressPanel.getImageType();
					       String filename = oid + "_" + imageType+".jpg";
					       if(imageType==null||"".equals(imageType)){
					       		continue;
					       }
					    %>
					    var tooltip = new Ext.ToolTip({
							target: '<%=vo.getIpAddress()%>',
							title:"已选模板类型",
							html:'<img src="<%=path+filename%>" width="100" heght="150">'
						});
					    
					    <%   
						 }
					%>
						<%
						if(rvalue!=null||"".equals(rvalue)){
							%>
								
							Ext.Msg.alert("",'<%=rvalue%>')
							<%
						}
						
						%>
					
			        //msg.style.display="block";
			});
			
			function chooseModePanel(ip,oid){
			var ipaddress = ip;
			var sysoid = oid;
			var dummyData = new Array();
			<%
				PanelModelDao panelModelDao = new PanelModelDao();
				List all_panelMode = panelModelDao.loadAll();
				for(int i = 0 ; i < all_panelMode.size(); i++){
					PanelModel panelModel = (PanelModel)all_panelMode.get(i);
					String imageType = panelModel.getImageType();
					if(imageType == null || "".equals(imageType)){
						continue;
					}
					String oid = panelModel.getOid().replace(".","-");
					String url = path + oid + "_" + imageType + ".jpg";
					%>
					if(sysoid == "<%=panelModel.getOid()%>"){
						dummyData.push(
							['<%=panelModel.getId()%>',
							'<%=oid%>',
							'<%=panelModel.getImageType()%>',
							'<%=url%>']
						);
					}
					<%
				}
			%>
			
				var reader = new Ext.data.ArrayReader({id:0},[	
				   {name:'id',type: 'int'},
			       {name: 'oid', type: 'string'},  
			       {name:'imageType',type: 'string'},
			       {name:'url', type: 'string'}
			    ]);
			    var store= new Ext.data.Store({reader:reader,data:dummyData});
			   
			    var ImageChooser = function(config){
			    	this.config = config;
			    }
				ImageChooser.prototype={
					show : function(){
						if(!this.win){
							this.initTemplates();
						this.store  = store;
						//this.store.load();
						this.view = new Ext.DataView({
							tpl: this.thumbTemplate,
							singleSelect: true,
							overClass:'x-view-over',
							itemSelector: 'div.thumb-wrap',
							store: this.store,
							listeners: {
								//'selectionchange': {fn:this.showDetails, scope:this, buffer:100},
								'dblclick'       : {fn:this.doCallback, scope:this}
								//'loadexception'  : {fn:this.onLoadException, scope:this},
								/*
								'beforeselect'   : {fn:function(view){
							        //return view.store.getRange().length > 0;
							    }}
							    */
							}
							//prepareData: formatData.createDelegate(this)
						});
						
						var cfg = {
					    	title: 'Choose an Image',
					    	id: 'img-chooser-dlg',
					    	layout: 'border',
							modal: true,
							border: false,
							width:400,
			 				height:300,
			 				maximizable:true,
							items:[{
								id: 'img-chooser-view',
								region: 'center',
								autoScroll: true,
								items: this.view
							}],
							buttons: [{
								id: 'ok-btn',
								text: 'OK',
								handler: this.doCallback,
								scope: this
							},{
								text: 'Cancel',
								handler: function(){ this.win.close(); },
								scope: this
							}],
							keys: {
								key: 27, // Esc key
								handler: function(){ this.win.close(); },
								scope: this
							}
						};
						Ext.apply(cfg, this.config);
					    this.win = new Ext.Window(cfg);
					    }
					    this.win.show();
					    this.view.select(0);
					},
					initTemplates : function(){
						this.thumbTemplate = new Ext.XTemplate(
							'<tpl for=".">',
								'<div class="thumb-wrap" id="{id}">',
								'<div class="thumb"><img src="{url}" title="{name}"></div>',
								'</div>',
							'</tpl>'
						);
						this.thumbTemplate.compile();
					
						this.detailsTemplate = new Ext.XTemplate(
							'<div class="details">',
								'<tpl for=".">',
									'<img src="{url}"><div class="details-info">',
									'<b>Image Name:</b>',
									'<span>{name}</span>',
									'<b>Size:</b>',
									'<span>{sizeString}</span>',
									'<b>Last Modified:</b>',
									'<span>{dateString}</span></div>',
								'</tpl>',
							'</div>'
						);
						this.detailsTemplate.compile();
					},
					
					doCallback : function(){
			        var selNode = this.view.getSelectedRecords()[0];
					var callback = this.callback;
					var lookup = this.lookup;
					this.win.close();
					var id = selNode.data.id;
					var imageType = selNode.data.imageType;
					Ext.MessageBox.wait('请稍后.. '); 
			        mainForm.action = "<%=rootPath%>/panel.do?action=createpanel&ipaddress="+ipaddress 
			        				+ "&id=" + id + "&imageType=" + imageType;
			        mainForm.submit();
			    }
				};
				var chooser = new ImageChooser({});
				chooser.show();
			}
			</script>
			
			<script language="javascript">	
			  var curpage= <%=jp.getCurrentPage()%>;
			  var totalpages = <%=jp.getPageTotal()%>;
			  var delAction = "<%=rootPath%>/perform.do?action=delete";
			  var listAction = "<%=rootPath%>/perform.do?action=panelnodelist";
			  
			  function doQuery()
			  {  
			     if(mainForm.key.value=="")
			     {
			     	alert("请输入查询条件");
			     	return false;
			     }
			     mainForm.action = "<%=rootPath%>/perform.do?action=find";
			     mainForm.submit();
			  }
			  
			    function doCancelManage()
			  {  
			     mainForm.action = "<%=rootPath%>/perform.do?action=cancelmanage";
			     mainForm.submit();
			  }
			  
			  function doChange()
			  {
			     if(mainForm.view_type.value==1)
			        window.location = "<%=rootPath%>/topology/network/index.jsp";
			     else
			        window.location = "<%=rootPath%>/topology/network/port.jsp";
			  }
			
			  function toAdd()
			  {
			      mainForm.action = "<%=rootPath%>/perform.do?action=ready_add";
			      mainForm.submit();
			  }
			  
			// 全屏观看
			function gotoFullScreen() {
				parent.mainFrame.resetProcDlg();
				var status = "toolbar=no,height="+ window.screen.height + ",";
				status += "width=" + (window.screen.width-8) + ",scrollbars=no";
				status += "screenX=0,screenY=0";
				window.open("topology/network/index.jsp", "fullScreenWindow", status);
				parent.mainFrame.zoomProcDlg("out");
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
				function detail()
				{
				    location.href="<%=rootPath%>/detail/dispatcher.jsp?id="+node;
				}
				function ping()
				{
					window.open("<%=rootPath%>/tool/ping.jsp?ipaddress="+ipaddress,"oneping", "height=400, width= 500, top=300, left=100");
					//window.open('/nms/netutil/ping.jsp?ipaddress='+ipaddress);
				}
				function traceroute()
				{
					window.open("<%=rootPath%>/tool/tracerouter.jsp?ipaddress="+ipaddress,"newtracerouter", "height=400, width= 500, top=300, left=100");
					//window.open('/nms/netutil/tracerouter.jsp?ipaddress='+ipaddress);
				}
				function telnet()
				{
					window.open('<%=rootPath%>/perform.do?action=telnet&ipaddress='+ipaddress,'onetelnet', 'height=0, width= 0, top=0, left= 0');
					//window.open('/nms/netutil/tracerouter.jsp?ipaddress='+ipaddress);
				}	
				function clickMenu()
				{
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
	<body id="body" class="body" onload="initmenu();">
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
									                	<td class="content-title"> 资源>> 设备面板配置管理 >> 设备面板编辑 </td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									       			</tr>
									        	</table>
		        							</td>
		        						</tr>
		        						<tr>
		        							<td>
		        								<table id="content-body" class="content-body">
		        									<%if(!"1".equals(flag)){ %>
		        									<tr >
														<td colspan="7" >
															<table>
																<tr>
									    							<td class="body-data-title">
																		<jsp:include page="../../common/page.jsp">
																			<jsp:param name="curpage" value="<%=jp.getCurrentPage()%>" />
																			<jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>" />
																		</jsp:include>
														    		</td>
			        											</tr>
															</table>
														</td>
													</tr> 
													<%} %>
													<tr>
														<td>
															<table>
					        									<tr>
			       													<td class="body-data-title"><INPUT type="checkbox" id="checkall" name="checkall" onclick="javascript:chkall()">序号</td>
			       													<td class="body-data-title">状态</td>
			       													<td class="body-data-title">名称</td>
			       													<td class="body-data-title">IP地址</td>
			       													<td class="body-data-title">型号</td>
			       													<td class="body-data-title">面板操作</td>
					        									</tr>
					        									<%
																    HostNode vo = null;
																    int startRow = jp.getStartRow();
																    for(int i=0;i<rc;i++)
																    {
																       vo = (HostNode)list.get(i);
																       Host host = (Host)PollingEngine.getInstance().getNodeByID(vo.getId());
																       PanelModelDao modeldao = new PanelModelDao();
																       List panelModellist = (List)modeldao.loadPanelModel(host.getSysOid());
																
																          
																%>
																<tr <%=onmouseoverstyle%> >  
					    											<td class="body-data-list">&nbsp;&nbsp;<INPUT type="checkbox" name=checkbox value="<%=vo.getId()%>" class=noborder>
					    												<font color='blue'><%=startRow + i%></font></td>
					    												
					    											<td  align='center' class="body-data-list">
					    											<%
					    												if(host.isAlarm()){
					    											%>
					    												<img src="<%=rootPath%>/resource/image/topo/alert.gif" border="0" width=15/>&nbsp;告警
					    											<%
					    												}else{
					    											%>
					    												<img src="<%=rootPath%>/resource/image/topo/status_ok.gif" border="0"/>&nbsp;正常
					    											<%
					    												}
					    											%>
					    											</td>
					    											<td  style="cursor:hand" class="body-data-list"><%=vo.getAlias()%></td>
					    											<td  class="body-data-list"><%=vo.getIpAddress()%></td>
					    											<td  class="body-data-list"><%=vo.getType()%></td>
																	<%
																		if(panelModellist == null ||(panelModellist.size() == 0)){
																			isShowMessage = true;
																	%>
																	<td  align='center' class="body-data-list">未配置模板</td>
					        										<%
					        											}else{
					        												isShowMessage = false;
					        										%>
					        										<td  align='center' id="" class="body-data-list"><a href="javascript:void(null)" id="<%=vo.getIpAddress()%>" onclick='chooseModePanel("<%=vo.getIpAddress()%>","<%=vo.getSysOid()%>")' ><font color="#397DBD">生成面板</font></a></td>
					        										<%
					        											}
					        										%>
					  											</tr>			
																<%}
																
																if(isShowMessage){
																%>
																
																<tr>
																	<td colspan="6" class="body-data-list" style="text-align: center;">
																		请前往 资源>> 设备面板配置管理 >> 面板模板编辑 中 编辑模板
																	</td>
																<tr>
					        									<%} %>
		        										
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
