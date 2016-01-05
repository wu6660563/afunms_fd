<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.application.model.SerialNode"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.application.model.TemperatureHumidityConfig"%>
<%@page import="com.afunms.application.model.TemperatureHumidityThresholdConfig"%>
<%@ include file="/include/globe.inc"%>

<%
  String rootPath = request.getContextPath();
  JspPage jp = (JspPage)request.getAttribute("page");
  List list = (List)request.getAttribute("list");
  Hashtable temperatureHumidityHashtable = (Hashtable)request.getAttribute("temperatureHumidityHashtable");
  Hashtable temperatureHumidityThresholdHashtable = (Hashtable)request.getAttribute("temperatureHumidityThresholdHashtable");
  Hashtable queryImageHashtable = (Hashtable)request.getAttribute("queryImageHashtable");
  request.setCharacterEncoding("gb2312");
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">

<link rel="stylesheet" type="text/css" href="<%=rootPath%>/resource/css/examples.css" />
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/resource/css/chooser.css" />


<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/application/environment/resource/ext3.1/resources/css/ext-all.css" />
<script type="text/javascript" 	src="<%=rootPath%>/application/environment/resource/ext3.1/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=rootPath%>/application/environment/resource/ext3.1/ext-all.js"></script>
<script type="text/javascript" src="<%=rootPath%>/application/environment/resource/ext3.1/ext-all-debug.js"></script>
<!--  
<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8"/>
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>

<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">

-->

<script language="JavaScript" type="text/javascript">
  var listAction = "<%=rootPath%>/db.do?action=list";
</script>
<script language="JavaScript">
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
	
<script>
var serialNode = new Array();
Ext.onReady(function(){
	Ext.QuickTips.init();
	
	<%
		if(list != null && list.size() > 0){
			for(int i = 0 ; i < list.size(); i++ ){
				SerialNode serialNode = (SerialNode)list.get(i);
				TemperatureHumidityConfig temperatureHumidityConfig = (TemperatureHumidityConfig)temperatureHumidityHashtable.get(serialNode.getId());
				TemperatureHumidityThresholdConfig temperatureHumidityThresholdConfig =
					(TemperatureHumidityThresholdConfig)temperatureHumidityThresholdHashtable.get(serialNode.getId());
				%>
				serialNode.push({
	                serialNodeId:"<%=serialNode.getId()%>",
	                serialNodeName:"<%=serialNode.getName()%>",
	                serialNodeAddress:"<%=serialNode.getAddress()%>",
	                serialNodeDescription:"<%=serialNode.getDescription()%>",
	                serialNodeMonflag:"<%=serialNode.getMonflag()%>",
	                serialNodeThresoldId:"<%=temperatureHumidityThresholdConfig.getId()%>",
	                serialNodeMinTemperature:"<%=temperatureHumidityThresholdConfig.getMinTemperature()%>",
	                serialNodeMaxTemperature:"<%=temperatureHumidityThresholdConfig.getMaxTemperature()%>",
	                serialNodeMinHumidity:"<%=temperatureHumidityThresholdConfig.getMinHumidity()%>",
	                serialNodeMaxHumidity:"<%=temperatureHumidityThresholdConfig.getMaxHumidity()%>",
	                serialNodeSerialPortId:"<%=serialNode.getSerialPortId()%>",
	                serialNodeBaudRate:"<%=serialNode.getBaudRate()%>",
	                serialNodeDataBits:"<%=serialNode.getDatabits()%>",
	                serialNodeStopBits:"<%=serialNode.getStopbits()%>",
	                serialNodeParity:"<%=serialNode.getParity()%>"
	            });
				// create the progressBar
				var progressBar1 = new Ext.ProgressBar({
					id		:	'progressBar1',
					animate	: {
							duration   : 1,
							easing     : 'bounceOut'	
							}
					});
			    progressBar1.render('<%=serialNode.getId()%>_temperature');
				progressBar1.updateProgress(<%=temperatureHumidityConfig.getTemperature()%>/100 , "<%=temperatureHumidityConfig.getTemperature()%>℃");
				var progressBar2 = new Ext.ProgressBar({
					animate	: {
							duration   : 1,
							easing     : 'bounceOut'	
							}
					});
				progressBar2.render('<%=serialNode.getId()%>_humidity');
				progressBar2.updateProgress(<%=temperatureHumidityConfig.getHumidity()%>/100 , "<%=temperatureHumidityConfig.getHumidity()%>%");
				<%
			}
		}  
		
		if( queryImageHashtable != null){
			String imageName = (String)queryImageHashtable.get("imageName");
		  	String startDate = (String)queryImageHashtable.get("startDate");
			String endDate = (String)queryImageHashtable.get("endDate");
			String timeType = (String)queryImageHashtable.get("timeType");
			String serialNodeId = (String)queryImageHashtable.get("serialNodeId");
		%>
			var store = ['<%=serialNodeId%>' , '<%=timeType%>','<%=startDate%>','<%=endDate%>','<%=imageName%>'];
			
			showCurveWindow(store);
		<%
		}
	 %>
	 
	 
	
});


Ext.apply(Ext.form.VTypes, {
    daterange : function(val, field) {
        var date = field.parseDate(val);

        if(!date){
            return;
        }
        if (field.startDateField && (!this.dateRangeMax || (date.getTime() != this.dateRangeMax.getTime()))) {
            var start = Ext.getCmp(field.startDateField);
            start.setMaxValue(date);
            start.validate();
            this.dateRangeMax = date;
        } 
        else if (field.endDateField && (!this.dateRangeMin || (date.getTime() != this.dateRangeMin.getTime()))) {
            var end = Ext.getCmp(field.endDateField);
            end.setMinValue(date);
            end.validate();
            this.dateRangeMin = date;
        }
        /*
         * Always return true since we're only using this vtype to set the
         * min/max allowed values (these are tested for after the vtype test)
         */
        return true;
    }

});

</script>
<script>
	function checkLinkage(id){
		var checkall = document.getElementById(id);
		var checkboxes = document.getElementsByName("checkbox");
		if(checkall.checked){
			for(var i = 0 ; i < checkboxes.length ; i ++){
				var checkbox = checkboxes[i];
				checkbox.checked = true;
			}
		}else{
			for(var i = 0 ; i < checkboxes.length ; i ++){
				var checkbox = checkboxes[i];
				checkbox.checked = false;
			}
		}
		
	}
</script>

<script>
	function toShowCurve(serialNodeId){
		Ext.MessageBox.wait('数据加载中，请稍后.. '); 
		mainForm.action = '<%=rootPath%>/temperatureHumidity.do?action=showCurvGraph'
						+ '&serialNodeId=' + serialNodeId + "&jsp=list";
		mainForm.submit();
	}
	
	function showCurveWindow(store){
		var imageName = "<%=rootPath%>/resource/image/jfreechart/" + store[4];
		var tb = new Ext.Toolbar({
			items: [
				'开始时间',
		        {
		        	id			:	'startDate',
					name		:	'startDate',
		            xtype		:	'datefield', // default for Toolbars, same as 'tbbutton'
		            format		:	'Y-m-d',
		            width		:	150,
					endDateField:	'endDate'
		        },
		        '结束时间',
		        {
		        	id			:	'endDate',
					name		:	'endDate',
		            xtype		:	'datefield', // same as 'tbsplitbutton'
		            format		:	'Y-m-d',
		            width		:	150,
		            startDateField: 'startDate'
		        },
		        '时间粒度',
		        {
					name		:	'timeType',
					id			:	'timeType',
					xtype 		: 	'combo',
					store		: 	[['minute','分钟'],['hour','小时'],['day','天'],['month','月']],
					fieldLabel	:	'时间粒度',
					mode		:	'local',
					typeAhead	:	true,
					selectOnFocus : true,
					emptyText	:	'--请选择开始时间--',
					width		:	130,
					triggerAction	: 'all',
					forceSelection	:	true
				},
				'-',
				{
					xtype		:	'button',
					text		:	'提交查看曲线图',
					handler		:	function(){
										var startDate = Ext.getCmp('startDate').getValue().format('Y-m-d');
										var endDate = Ext.getCmp('endDate').getValue().format('Y-m-d');
										var timeType = Ext.getCmp('timeType').getValue();
										mainForm.action = '<%=rootPath%>/temperatureHumidity.do?action=showCurvGraph&jsp=list'
														+ '&serialNodeId=' + store[0] + '&timeType=' + timeType
														+ '&startDate=' + startDate + '&endDate=' + endDate;
										mainForm.submit();
									}
				}]
		});
		
		var win = new Ext.Window({
			tbar	:	tb,
			title	:	"温湿度曲线图",
			modal	:	true,
			width	:	750,
			html	:	'<div align="center"><image src=' +imageName + '></div>',
			layout	:	'fit',
			frame	:	true,
			maximizable:true
		});
		Ext.getCmp('startDate').setValue(store[2]);
		Ext.getCmp('endDate').setValue(store[3]);
		Ext.getCmp('timeType').setValue(store[1]);
		win.show();
	}
	
</script>

<script>

	function showSerailNodeDetail(serialNodeId){
		var items = new Array();
		for(var i = 0 ; i < serialNode.length ; i++){
			if(serialNodeId==serialNode[i].serialNodeId){
				items = serialNode[i];
			}
		}
		var grid = new Ext.grid.PropertyGrid({
		    title: 'Properties Grid',
		    autoHeight: true,
		    propertyNames: {
	            name:"名称",
		        address:"地址",
		        monflag:"监控",
		        description:"描述",
		        serialPortId:"串口号",
		        minTemperature:"温度最小阀值",
		        maxTemperature:"温度最大阀值",
		        minHumidity:"湿度最小阀值",
		        maxHumidity:"湿度最大阀值"
	        },
	        viewConfig : {
	            forceFit: true,
	            scrollOffset: 2 // the grid will never have scrollbars
	        },
		    source: {
		        name: items.serialNodeName,
		        address: items.serialNodeAddress,
		        monflag: items.serialNodeMonflag,
		        description: items.serialNodeDescription,
		        serialPortId: items.serialNodeSerialPortId,
		        minTemperature: items.serialNodeMinTemperature,
		        maxTemperature: items.serialNodeMaxTemperature,
		        minHumidity: items.serialNodeMinHumidity,
		        maxHumidity: items.serialNodeMaxHumidity
		    }
		});
		var win2 = new Ext.Window({
			title	:	"设备详细信息配置",
			modal	:	true,
			autoScroll : true,
			width	:	320,
			height	:	450,
			layout	:	'fit',
			maximizable:true,
			resize	:	function(){
							grid.reconfigure();
							grid.render();
							},
			 buttons:[{text:"提交",
					handler:function(){
					var name = grid.source['name'];
					var address = grid.source['address'];
					var description = grid.source['description'];
					var monflag = grid.source['monflag'];
					var serialPortId = grid.source['serialPortId'];
					var minTemperature = grid.source['minTemperature'];
					var maxTemperature = grid.source['maxTemperature'];
					var minHumidity = grid.source['minHumidity'];
					var maxHumidity = grid.source['maxHumidity'];
					mainForm.action = "<%=rootPath%>/temperatureHumidity.do?action=edit"
									+ "&name=" + name + "&address=" + address 
									+ "&description=" + description + "&monflag=" +monflag 
									+ "&serialPortId=" + serialPortId + "&baudRate=" + items.serialNodeBaudRate
									+ "&databits=" + items.serialNodeDataBits + "&stopbits=" + items.serialNodeStopBits 
									+ "&parity=" + items.serialNodeParity 
									+ "&minTemperature=" + minTemperature + "&maxTemperature=" + maxTemperature
									+ "&minHumidity=" + minHumidity + "&maxHumidity=" + maxHumidity
									+ "&serialNodeThresoldId=" + items.serialNodeThresoldId
									+ "&serialNodeId=" + items.serialNodeId;
					mainForm.submit();
						}},
							{text:"关闭",
							handler:function(){
								win2.destroy();		
								win2.hide();
							}
							}]
		});
		win2.add(grid);
		win2.show();
	}
</script>

<script type="text/javascript">
	function showPanel(){
	
	
		var dummyData = new Array();
		for(var i = 0 ; i < 4 ; i++){
		dummyData.push(
				[ i + '',
				'传感器0' + i ]
			);
		}
		
		var reader = new Ext.data.ArrayReader({id:0},[	
		   {name:'id',type: 'int'},
	       {name: 'name', type: 'string'},  
	    ]);
	    var store= new Ext.data.Store({reader:reader,data:dummyData});
		 var panel = new Ext.Panel({
            layout: 'border',
            collapsible: true,
            height:400,
            items: [
            // create instance immediately
             {
                // lazily created panel (xtype:'panel' is default)
                region: 'south',
                contentEl: 'south',
                split: true,
                height: 100,
                minSize: 100,
                maxSize: 200,
                collapsible: true,
                title: 'South',
                margins: '0 0 0 0'
            }, {
                region: 'east',
                title: 'East Side',
                collapsible: true,
                split: true,
                width: 225, // give east and west regions a width
                minSize: 175,
                maxSize: 400,
                margins: '0 5 0 0',
                layout: 'fit', // specify layout manager for items
                items:            // this TabPanel is wrapped by another Panel so the title will be applied
                new Ext.Panel({
                    border: false, // already wrapped so don't add another border
                    activeTab: 1, // second tab initially active
                    loyout:'fit',
                    items: [
                    new Ext.grid.PropertyGrid({
                        title: 'Property Grid',
                        closable: true,
                        source: {
                            "(name)": "Properties Grid",
                            "grouping": false,
                            "autoFitColumns": true,
                            "productionQuality": false,
                            "created": new Date(Date.parse('10/15/2006')),
                            "tested": false,
                            "version": 0.01,
                            "borderWidth": 1
                        }
                    })]
                })
            }, {
                region: 'west',
                id: 'west-panel', // see Ext.getCmp() below
                title: 'West',
                split: true,
                width: 200,
                minSize: 175,
                maxSize: 400,
                collapsible: true,
                margins: '0 0 0 5',
                layout: {
                    type: 'accordion',
                    animate: true
                },
                items: [{
                    contentEl: 'west',
                    title: 'Navigation',
                    border: false,
                    iconCls: 'nav' // see the HEAD section for style used
                }, {
                    title: 'Settings',
                    html: '<p>Some settings in here.</p>',
                    border: false,
                    iconCls: 'settings'
                }]
            },
            // in this instance the TabPanel is not wrapped by another panel
            // since no title is needed, this Panel is added directly
            // as a Container
             new Ext.Panel({
             	id:"centerPanel",
             	name:"centerPanel",
                region: 'center', // a center region is ALWAYS required for border layout
                deferredRender: false,
                activeTab: 0,     // first tab initially active
                layout:'fit'
                
            })]
        });
      	var centerPanel = Ext.getCmp("centerPanel");
	    var ImageChooser = function(config){
	    	this.config = config;
	    }
		ImageChooser.prototype={
			show : function(){
					this.initTemplates();
				this.store  = store;
				this.view = new Ext.DataView({
				id:'view',
				tpl: this.thumbTemplate,
				singleSelect: true,
				overClass:'x-view-over',
				itemSelector: 'div.thumb-wrap',
				store: this.store,
				listeners: {
					'selectionchange': {fn:this.showDetails, scope:this, buffer:100}
					//'dblclick'       : {fn:this.doCallback, scope:this}
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
				}]
				
			};
			
			
			Ext.apply(cfg, this.config);
		    centerPanel.add(cfg);
		    this.view.select(0);
		   
			},
			initTemplates : function(){
				this.thumbTemplate = new Ext.XTemplate(
					'<tpl for=".">',
						'<div class="thumb-wrap" id="{id}">',
						'<div class="thumb" id="{id}'+ '_temperature'+'">温度：</div>',
						'<div class="thumb" id="{id}'+ '_humidity'+'">湿度：</div>',
						'<div class="thumb" >{name}</div>',
						'</div>',
					'</tpl>'
				);
				this.thumbTemplate.compile();
			},
			showDetails: function(){
			},
			doCallback : function(){
	        
	    }
		};
		
		var chooser = new ImageChooser({});
		chooser.show();  
      	
      	
        
        panel.render("panel");
         var progressBar1 = new Ext.ProgressBar({
					id		:	'progressBar2',
					width	:	200,
					fieldLabel :'温度',
					animate	: {
							duration   : 1,
							easing     : 'bounceOut'	
							}
					});
			progressBar1.render("0_temperature");
			progressBar1.updateProgress(0.2 , "aaa");
		var progressBar2 = new Ext.ProgressBar({
					id		:	'progressBar2',
					width	:	200,
					fieldLabel :'湿度',
					animate	: {
							duration   : 1,
							easing     : 'bounceOut'	
							}
					});
			progressBar2.render("0_humidity");
			progressBar2.updateProgress(0.2 , "aaa");
        var view = Ext.getCmp('view');
        view.select(0);
	}
</script>

<script>
	function toAdd(){
		mainForm.action = "<%=rootPath%>/temperatureHumidity.do?action=readyAdd";
		mainForm.submit();
	}
	
	function toDelete(){
		var checkbox = document.getElementsByName("checkbox");
		for(var i = 0 ; i < checkbox.length ; i++){
			var value = checkbox[i].checked;
			if(value){
				mainForm.action = "<%=rootPath%>/temperatureHumidity.do?action=delete";
				mainForm.submit();
				return;
			}
		}
		alert("请最少选择一台设备");
		return;
	}
	
	function toEdit(serialNodeId){
		mainForm.action = "<%=rootPath%>/temperatureHumidity.do?action=readyEdit&serialNodeId=" + serialNodeId;
		mainForm.submit();
	}
</script>

</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa" onload="initmenu();">
	<form method="post" name="mainForm">
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
                    <td class="layout_title">应用 >> 环境监控 >> 温湿度监测参数配置</td>
                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                  </tr>
              </table>
			  </td>
		        		<tr>           
								<td>
								<table width="100%" cellpadding="0" cellspacing="1" >
        			<tr>
					            <td bgcolor="#ECECEC" width="100%" align='right'>
								<a href="#" onclick="toAdd()">添加</a>
								<a href="#" onclick="toDelete()">删除</a>&nbsp;&nbsp;&nbsp;
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
                              <jsp:include page="../../../common/page.jsp"><jsp:param name="curpage" value="<%=jp.getCurrentPage()%>"/>
									<jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>"/>
								</jsp:include>
		        			 </td>
        </tr>
		</table>
		</td>
		</tr>    
		        		<tr>
			<td colspan="2">
				<table cellspacing="1" cellpadding="0" width="100%" >
					<tr class="microsoftLook0" height=28>
										<td width='5%' align=left>
											&nbsp;<input type="checkbox" class=noborder name="checkall" id="checkall" onclick='checkLinkage("checkall")'>
										</td>
				    					<td width='5%'  align="center"><b>序号</b></td>
				      					<td width='10%' align="center"><b>名称</b></td>
				      					<td width='10%' align="center"><b>地址</b></td>	
				      					<td width='20%' align="center"><b>当前温度</b></td> 
				      					<td width='20%' align="center"><b>当前湿度</b></td>    
				      					<td width='20%' align="center"><b>监测时间</b></td>
				    					<td width='10%' align="center"><b>详情</b></td>
				    				</tr>
				    				<%
					    				if(list != null && list.size() > 0){
					    					for(int i = 0 ; i < list.size(); i++ ){
					    						SerialNode serialNode = (SerialNode)list.get(i);
					    						TemperatureHumidityConfig temperatureHumidityConfig = (TemperatureHumidityConfig)temperatureHumidityHashtable.get(serialNode.getId());
					    						%>
					    						<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=25>
					    							<td align="center" >
					    								<input type="checkbox" id="<%=serialNode.getId()%>" name="checkbox"  class=noborder value="<%=serialNode.getId()%>">
					    							</td >
					    							<td align="center"><%=i+1%></td>
					    							<td align="center"><input type="button" onclick='toEdit("<%=serialNode.getId()%>")' value="<%=serialNode.getName()%>"></td>
					    							<td align="center"><%=serialNode.getAddress()%></td>
					    							<td align="center" ><div id="<%=serialNode.getId()%>_temperature" ext:qtip="<%=temperatureHumidityConfig.getTemperature()%>"></div></td>
					    							<td align="center"><div id="<%=serialNode.getId()%>_humidity" ></div></td>
					    							<td align="center"><%=temperatureHumidityConfig.getTime()%></td>
					    							<td align="center"><input type="button" value="详细" onclick='toShowCurve("<%=serialNode.getId()%>")'></td>
					    						</tr>
					    						<%
					    					}
					    				}  
				    				 %>
				    			</table>
				    		</td>		
						</tr>
						<tr>
							<td><div id="panel">
							
							
							<div id="west" class="x-hide-display">
      
						    </div>
						    <div id="center2" class="x-hide-display">
						        </div>
						    <div id="center1" class="x-hide-display">
						
						    </div>
						    <div id="props-panel" class="x-hide-display" style="width:200px;height:200px;overflow:hidden;">
						    </div>
						    <div id="south" class="x-hide-display">
						       
						    </div>
							
							</div></td>
						</tr>
						 <tr>
              <td background="<%=rootPath%>/common/images/right_b_02.jpg" ><table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="11" /></td>
                    <td></td>
                    <td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="11" /></td>
                  </tr>
              </table></td>
			  </tr>
		        	</table>
		        </td>	
			</tr>
		</table>
	</form>
</BODY>
</HTML>
