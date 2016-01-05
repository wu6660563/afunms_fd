<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.application.model.SerialNode"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.application.model.TemperatureHumidityConfig"%>
<%@page import="com.afunms.application.model.TemperatureHumidityThresholdConfig"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>
<%@page import="org.jfree.data.category.CategoryDataset"%>
<%@page import="org.jfree.data.category.DefaultCategoryDataset"%>
<%@ include file="/include/globe.inc"%>

<%
	String rootPath = request.getContextPath();
	List list = (List) request.getAttribute("list");
	String selcetId = (String) request.getAttribute("selcetId");
	System.out.println(selcetId+"========================================");
	SerialNode selectSerialNode = (SerialNode) request
			.getAttribute("selectSerialNode");
	TemperatureHumidityThresholdConfig temperatureHumidityThresholdConfig = (TemperatureHumidityThresholdConfig) request
			.getAttribute("temperatureHumidityThresholdConfig");
	Hashtable temperatureHumidityHashtable = (Hashtable) request
			.getAttribute("temperatureHumidityHashtable");
	Hashtable<String, String> imageHashtable = (Hashtable<String, String>) request
			.getAttribute("imageHashtable");
	Hashtable temperatureHumidityStatisticsHashtable = (Hashtable) request
			.getAttribute("temperatureHumidityStatisticsHashtable");
			
%>
<%
	String menuTable = (String) request.getAttribute("menuTable");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet"
			type="text/css" />
		<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css"
			rel="stylesheet">
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css"
			rel="stylesheet">
		<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet"
			type="text/css">
		<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css"
			type="text/css">
		<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet"
			type="text/css">

		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/resource/css/examples.css" />
		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/resource/css/chooserSerial.css" />


		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/application/environment/resource/ext3.1/resources/css/ext-all.css" />
		<script type="text/javascript"
			src="<%=rootPath%>/application/environment/resource/ext3.1/adapter/ext/ext-base.js"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/application/environment/resource/ext3.1/ext-all.js"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/application/environment/resource/ext3.1/ext-all-debug.js"></script>
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
				java.text.NumberFormat   formate   =   java.text.NumberFormat.getNumberInstance();   
        		formate.setMaximumFractionDigits(1);//设定小数最大为数   ，那么显示的最后会四舍五入的   
        		String   temperature   =   formate.format(Double.valueOf(temperatureHumidityConfig.getTemperature()));  
        		String char1 = ChartCreator.createMeterChart(Double.valueOf(temperature),"温度",130,130);
        		String char2 = ChartCreator.createMeterChart(Double.valueOf(Double.valueOf(temperatureHumidityConfig.getHumidity())),"湿度",130,130);
        		DefaultCategoryDataset defaultCategoryDataset = new DefaultCategoryDataset();
        		String char3 =ChartCreator.createStackeBarChart(defaultCategoryDataset , "温湿度" , "值" ,"温湿度" , 150 ,150);
        		defaultCategoryDataset.addValue(Double.valueOf(temperatureHumidityConfig.getTemperature()) , "温度" , "温度");
        		defaultCategoryDataset.addValue(Double.valueOf(temperatureHumidityConfig.getHumidity()) , "湿度" , "湿度");
        		Hashtable statisticsHashtable= (Hashtable)temperatureHumidityStatisticsHashtable.get(String.valueOf(serialNode.getId()));
				%>
				serialNode[<%=i%>]=[
	                "<%=serialNode.getId()%>",
	                "<%=serialNode.getName()%>",
	                "<%=serialNode.getAddress()%>",
	                "<%=serialNode.getMonflag()%>",
	                "<%=serialNode.getDescription()%>",
	                "<%=serialNode.getSerialPortId()%>",
	                "<%=serialNode.getBaudRate()%>",
	                "<%=serialNode.getDatabits()%>",
	                "<%=serialNode.getStopbits()%>",
	                "<%=serialNode.getParity()%>",
	                "<%=temperatureHumidityConfig.getTemperature()%>",
	                "<%=temperatureHumidityConfig.getHumidity()%>",
	                "<%=temperatureHumidityConfig.getTime()%>",
	                "<%=char1%>",
	                "<%=char2%>",
	                "<%=statisticsHashtable.get("maxTemperature")%>",
	                "<%=statisticsHashtable.get("avgTemperature")%>",
	                "<%=statisticsHashtable.get("minTemperature")%>",
	                
	                "<%=statisticsHashtable.get("maxHumidity")%>",
	                "<%=statisticsHashtable.get("avgHumidity")%>",
	                "<%=statisticsHashtable.get("minHumidity")%>",
	                
	                "<%=char3%>"
	            ];
	           <%
			}
		}  
		%>
		
	 showPanel();
	
});
</script>

		<script type="text/javascript">
	function showPanel(){
		var data = serialNode;
		var monflag = false;
		if('1' == '<%=selectSerialNode.getMonflag()%>'){
			monflag = true;
		}
		
		var reader = new Ext.data.ArrayReader({id:0},[	
		   {name:'id',type: 'int'},
	       {name: 'name', type: 'string'},  
	       {name:'address',type: 'string'},
	       {name:'monflag',type: 'string'},
	       {name: 'description', type: 'string'},  
	       {name: 'serialPortId', type: 'string'},  
	       {name:'baudRate',type: 'string'},
	       {name: 'databits', type: 'string'},  
	       {name:'stopbits',type: 'string'},
	       {name: 'parity', type: 'string'},
	       {name: 'temperature', type: 'string'},  
	       {name:'humidity',type: 'string'},
	       {name: 'time', type: 'string'},
	       {name: 'artistTemperature', type: 'string'},
	       {name: 'artistHumidity', type: 'string'},
	       {name: 'maxTemperature', type: 'string'},
	       {name: 'avgTemperature', type: 'string'},
	       {name: 'minTemperature', type: 'string'},
	       
	       {name: 'maxHumidity', type: 'string'},
	       {name: 'avgHumidity', type: 'string'},
	       {name: 'minHumidity', type: 'string'},
	       
	       {name: 'char3', type: 'string'}
	        
	    ]);
	   
	    var store= new Ext.data.Store({reader:reader,data:data});
	    
	    var comboxdata = [['minute','分钟'],['hour','小时'],['day','天'],['month','月']];
	    
	    var comboxReader = new Ext.data.ArrayReader({id:0},[	
		   {name:	'value',	type	:	 'string'},
	       {name: 	'text', 	type	:	 'string'}
	    ]);
	    
	    var comboxStore = new Ext.data.Store({reader:comboxReader,data:comboxdata});
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
					store		: 	comboxStore,
					fieldLabel	:	'时间粒度',
					mode		:	'local',
					typeAhead	:	true,
					selectOnFocus : true,
					emptyText	:	'--请选择开始时间--',
					width		:	130,
					triggerAction	: 'all',
					forceSelection	:	true,
					valueField	:'value',   
    				displayField:'text',
    				hiddenName	:'value'
				},
				'-',
				{
					xtype		:	'button',
					text		:	'提交查看曲线图',
					handler		:	function(){
									Ext.Msg.wait('数据提交中..请稍后...');
									var view = Ext.getCmp('view');
									var selNode = view.getSelectedRecords()[0];
									var selectId = selNode.data.id;
									var startDate = Ext.getCmp('startDate').getValue().format('Y-m-d');
									var endDate = Ext.getCmp('endDate').getValue().format('Y-m-d');
									var timeType = Ext.getCmp('timeType').getValue();
									mainForm.action = '<%=rootPath%>/temperatureHumidity.do?action=listImage'
													+ '&selectId=' + selectId + '&timeType=' + timeType
													+ '&startDate=' + startDate + '&endDate=' + endDate;
									mainForm.submit();	
									}
				}]
		});
	    
		 var panel = new Ext.Panel({
		 	title :	'应用 >> 环境监控 >> 温湿度传感器列表',
            layout: 'border',
            collapsible: true,
            height:650,
            items: [
            // create instance immediately
             {
                // lazily created panel (xtype:'panel' is default)
                region: 'south',
                contentEl: 'south',
                layout:'fit',
                split: true,
                height: 280,
                minSize: 100,
                maxSize: 200,
                collapsible: true,
                title: '温湿度曲线图',
                margins: '0 0 0 0',
                html	:	'<div align="center"><image src="<%=rootPath%>/resource/image/jfreechart/<%=imageHashtable.get("imageName")%>" ><div>',
                tbar	:	tb,
                frame	:	true
                
                
            }, {
                region: 'east',
                title: '编 辑',
                collapsible: true,
                split: true,
                width: 225, // give east and west regions a width
                minSize: 175,
                maxSize: 400,
                layout: 'fit', // specify layout manager for items
                items:            // this TabPanel is wrapped by another Panel so the title will be applied
                new Ext.Panel({
                	layout:'fit',
                    items: [
                    new Ext.grid.PropertyGrid({
                        title: '传感器参数配置',
                        id:'grid',
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
                        source: {
                            name: '<%=selectSerialNode.getName()%>',
					        address: '<%=selectSerialNode.getAddress()%>',
					        monflag: monflag,
					        description: '<%=selectSerialNode.getDescription()%>',
					        serialPortId: '<%=selectSerialNode.getSerialPortId()%>',
					        minTemperature: '<%=temperatureHumidityThresholdConfig.getMinTemperature()%>',
					        maxTemperature: '<%=temperatureHumidityThresholdConfig.getMaxTemperature()%>',
					        minHumidity: '<%=temperatureHumidityThresholdConfig.getMinHumidity()%>',
					        maxHumidity: '<%=temperatureHumidityThresholdConfig.getMaxHumidity()%>'
                        }
                    })],
                    
                    buttons:[{text:"提交",
					handler:function(){
					var grid = Ext.getCmp('grid');
					document.getElementById('name').value = grid.source['name'];
					document.getElementById('address').value = grid.source['address'];
					document.getElementById('description').value = grid.source['description'];
					if(grid.source['monflag']){
						document.getElementById('monflag').value = '1';
					}else{
						document.getElementById('monflag').value = '0';
					}
					document.getElementById('serialPortId').value = grid.source['serialPortId'];
					document.getElementById('minTemperature').value = grid.source['minTemperature'];
					document.getElementById('maxTemperature').value = grid.source['maxTemperature'];
					document.getElementById('minHumidity').value = grid.source['minHumidity'];
					document.getElementById('maxHumidity').value = grid.source['maxHumidity'];
					mainForm.action = "<%=rootPath%>/temperatureHumidity.do?action=edit"
									+ "&baudRate=" + <%=selectSerialNode.getBaudRate()%>
									+ "&databits=" + <%=selectSerialNode.getDatabits()%> + "&stopbits=" + <%=selectSerialNode.getStopbits()%>
									+ "&parity=" + <%=selectSerialNode.getParity()%>
									+ "&serialNodeThresoldId=" + <%=temperatureHumidityThresholdConfig.getId()%>
									+ "&serialNodeId=" + <%=temperatureHumidityThresholdConfig.getNode_id()%>;
					mainForm.submit();
					}}]
                })
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
					//'selectionchange': {fn:this.showDetails, scope:this, buffer:100}
					'dblclick'       : {fn:this.doCallback, scope:this},
					//'loadexception'  : {fn:this.onLoadException, scope:this},
					/*
					'beforeselect'   : {fn:function(view){
				        //return view.store.getRange().length > 0;
				    }}
				    */
				    'contextmenu'	: {fn:this.edit, scope:this}
				}
				//prepareData: formatData.createDelegate(this)
			});
			
			var cfg = {
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
		    
		    this.view.select('<%=selcetId%>');
		   	centerPanel.add(cfg);
			},
			initTemplates : function(){
				this.thumbTemplate = new Ext.XTemplate(
					'<tpl for=".">',
						'<div class="thumb-wrap" id="{id}_serialNode">',
						'<div class="thumb" >{name}</div>',
						'<div class="thumb" ><image src="<%=rootPath%>/artist?series_key={char3}"></div>',
						'<div class="thumb" >温度</div>',
						'<div class="thumb" >最大值：{maxTemperature}</div>',
						'<div class="thumb" >最小值：{minTemperature}</div>',
						'<div class="thumb" >平均值：{avgTemperature}</div>',
						'<div class="thumb" >湿度</div>',
						'<div class="thumb" >最大值：{maxHumidity}</div>',
						'<div class="thumb" >最小值：{minHumidity}</div>',
						'<div class="thumb" >平均值：{avgHumidity}</div>',
						//'<div class="thumb" ><image src="<%=rootPath%>/artist?series_key={artistTemperature}">' +
						//'<image src="<%=rootPath%>/artist?series_key={artistHumidity}"></div>',
						//'<div class="thumb" id="{id}'+ '_temperature'+'">温度：</div>',
						//'<div class="thumb" id="{id}'+ '_humidity'+'">湿度：</div>',
						'</div>',
					'</tpl>'
				);
				this.thumbTemplate.compile();
			},
			showDetails: function(){
				
			},
			doCallback : function(){
				Ext.Msg.wait('数据提交中..请稍后...');
				var selNode = this.view.getSelectedRecords()[0];
				var selectId = selNode.data.id;
	        	mainForm.action = '<%=rootPath%>/temperatureHumidity.do?action=listImage&selectId='+selectId;
	        	mainForm.submit();
	    	},
	    	
	    	edit:function(exl, index, node, e ){
	    		e.preventDefault();//阻止浏览器默认行为处理事件
	    		var rightClick = new Ext.menu.Menu({ 
				    items: [ 
				        { 
				            //id: 'rMenu1', 
				            //handler: rMenu1Fn, 
				            text: '右键菜单1',
				            handler:function(){
				            	mainForm.action = "<%=rootPath%>/"
				            }
				        }
				        
				    ] 
				}); 
				rightClick.showAt(e.getXY()); 
			} 
	    	
		};
		
		var chooser = new ImageChooser({});
		chooser.show();  
        panel.render("panel");
        Ext.getCmp('view').select(<%=selcetId%>);
        Ext.getCmp('startDate').setValue('<%=imageHashtable.get("startDate")%>');
		Ext.getCmp('endDate').setValue('<%=imageHashtable.get("endDate")%>');
		Ext.getCmp('timeType').setValue('<%=imageHashtable.get("timeType")%>');
		
		/*
		for(var i = 0 ; i < serialNode.length ; i++){
		
			

		
		
			var progressBar1 = new Ext.ProgressBar({
					width	:	200,
					fieldLabel :'温度',
					animate	: {
							duration   : 1,
							easing     : 'bounceOut'	
							}
					});
			progressBar1.render(serialNode[i][0]+"_temperature");
			progressBar1.updateProgress(serialNode[i][10]/100 , serialNode[i][10]+"℃");
			var progressBar2 = new Ext.ProgressBar({
						width	:	200,
						fieldLabel :'湿度',
						animate	: {
								duration   : 1,
								easing     : 'bounceOut'	
								}
						});
			progressBar2.render(serialNode[i][0] +"_humidity");
			progressBar2.updateProgress(serialNode[i][11]/100 , serialNode[i][11]+"%");
			
		}
		
		*/
         
	}
</script>


	</head>
	<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa"
		onload="initmenu();">
		<form method="post" name="mainForm">
			<input type="hidden" id="name" name="name">
			<input type="hidden" id="address" name="address">
			<input type="hidden" id="description" name="description">
			<input type="hidden" id="monflag" name="monflag">
			<input type="hidden" id="serialPortId" name="serialPortId">
			<input type="hidden" id="minTemperature" name="minTemperature">
			<input type="hidden" id="maxTemperature" name="maxTemperature">
			<input type="hidden" id="minHumidity" name="minHumidity">
			<input type="hidden" id="maxHumidity" name="maxHumidity">
			<table border="0" id="table1" cellpadding="0" cellspacing="0"
				width=100%>
				<tr>
					<td width="200" valign=top align=center><%=menuTable%></td>
					<td bgcolor="#ffffff" align="center" valign=top>
						<table width="100%" style="BORDER-COLLAPSE: collapse"
							borderColor=#397DBD cellPadding=0 rules=none align=center
							border=1 algin="center">
							<tr>
								<td>
									<div id="panel">


										<div id="west" class="x-hide-display">

										</div>
										<div id="center2" class="x-hide-display">
										</div>
										<div id="center1" class="x-hide-display">

										</div>
										<div id="props-panel" class="x-hide-display"
											style="width: 200px; height: 200px; overflow: hidden;">
										</div>
										<div id="south" class="x-hide-display">

										</div>

									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</form>
	</BODY>
</HTML>
