var tabpanel_var = function(rootpath){
			/**
			 * 1������grid1 ����  ������
			 * 		a��store
			 *      b��clolumn
			 *      c��new grid 
			 * 2������grid2 ��3����
			 * 3��3��grid����TabPanel
			 * 4�����������������
			 */
				var proxy1 = new Ext.data.HttpProxy({
					url:'serverAjaxManager.ajax?action=ajaxHostInfolist'
				});
				var reader1 = new Ext.data.JsonReader({
						root : 'monitorNodeList'
					},[
						{name : 'id',type:'int'},
						{name : 'ipAddress'},
						{name : 'alias'},
						{name : 'status'},
						{name : 'category'},
						{name : 'type'},
						{name : 'subtype'},
						{name : 'pingValue' ,type:'int'},
						{name : 'cpuValue' ,type:'int'},
						{name : 'memoryValue' ,type:'int'},
						{name : 'cpuValueColor'},
						{name : 'memoryValueColor'},
						{name : 'inutilhdxValue' ,type:'int'},
						{name : 'oututilhdxValue' ,type:'int'},
						{name : 'eventListCount' ,type:'int'},
						{name : 'collectType'},
						{name : 'eventListSummary'},
						{name : 'entityNumber'}
				]);
			    var dataStore1 = new Ext.data.Store({
			        autoLoad: false,
			        baseParams: {type: 'host'},
					proxy : proxy1,
					reader : reader1
				});
			    // manually load local data
			    // dataStore1load();
			    var grid1 = new Ext.grid.GridPanel({
			        store: dataStore1,
			        title:'������',
			        columns: [
			            {
			                id       :'id',
			                header   : '�豸ID', 
			                width :70,
			                sortable : true, 
			                dataIndex: 'id'
			            },
			            {
			                header   : 'ip��ַ', 
			                sortable : true, 
			                renderer : 'ipAddress', 
			                dataIndex: 'ipAddress'
			            },
			            {
			                header   : '����', 
			                sortable : true, 
			                renderer : function(alias,_this,_data){ 
			                	var status = _data.get('status');
			                	var statusImg = '';
					        	if("1" ==status){
					        		statusImg = "alarm_level_1.gif";
					        	}else if("2"==status){
					        		statusImg = "alarm_level_2.gif";
					        	}else if("3"==status){
					        		statusImg = "alert.gif";
					        	}else{
					        		statusImg = "status_ok.gif";
					        	};
					        	//alert(Ext.getCmp('rootpath').value);
					        	var nodeId = _data.get('id');
					        	var type = _data.get('type');
					        	var subtype = _data.get('subtype');
					        	var node_ip = _data.get('ipAddress');
			                	 var tabstr = "<img src='"+rootpath+"/resource/image/topo/"+statusImg+"'>  <a href='javascript:openTologyAndLocation(\""+rootpath+"\","+nodeId+",\""+type+"\",\""+subtype+"\",\""+node_ip+"\")'>"+alias+"</a>";
							     return tabstr;
							},    
			                dataIndex: 'alias'
			            },
			            {
			                header   : '������(%)', 
			                sortable : true, 
			                renderer : 'pingValue',
			                dataIndex: 'pingValue'
			            },
			            {
			                header   : 'CPU(%)', 
			                sortable : true, 
			                renderer : function(cpuValue,_this,_data){ 
			                	 var cpuUnUsed = 100-cpuValue;
			                	 //ȡ����ǰcpu����ɫ  ��ǰcolumn ��index������
			                	// var itemid = this.getItemId();
			                	var cpuValueColor = _data.get('cpuValueColor');
			                	 var tabstr =  '<table width="100%"><tr><td>'+cpuValue+'%</td>' +
			                	 									'<td>' +
			                	 										'<table border=1 height=15  bgcolor=#ffffff>' +
			                	 											'<tr><td width='+cpuValue+'% bgcolor='+cpuValueColor+'></td><td width='+cpuUnUsed+'% bgcolor=#ffffff></td></tr></table>' +
			                	 									'</td></tr></table>';
							     return tabstr;
							},   
			                dataIndex: 'cpuValue'
			            },
			            {
			                header   : '�ڴ�(%)', 
			                sortable : true, 
			                renderer : function(memoryValue,_this,_data){ 
			                	 var memoryUnUsed = 100-memoryValue;
			                	 //�ڴ����ɫ
			                	var memoryValueColor = _data.get('memoryValueColor');
			                	 var tabstr =  '<table width="100%"><tr><td>'+memoryValue+'%</td>' +
			                	 									'<td>' +
			                	 										'<table border=1 height=15  bgcolor=#ffffff>' +
			                	 											'<tr><td width='+memoryValue+'% bgcolor='+memoryValueColor+'></td><td width='+memoryUnUsed+'% bgcolor=#ffffff></td></tr></table>' +
			                	 									'</td></tr></table>';
							     return tabstr;
							},   
			                dataIndex: 'memoryValue'
			            },
			            {
			                header   : '�������(KB/S)', 
			                sortable : true, 
			                renderer : 'inutilhdxValue', 
			                dataIndex: 'inutilhdxValue'
			            },
			            {
			                header   : '��������(KB/S)', 
			                sortable : true, 
			                renderer : 'oututilhdxValue', 
			                dataIndex: 'oututilhdxValue'
			            }
			       	],
			        stripeRows: true,
			        autoExpandColumn: 'id',
			        autoScroll: true,
                    bodyStyle : 'width:100%',   
			        autoHeight : true,
			        viewConfig:{   
						forceFit:true  
					}, 
			        stateful: false,
			        stateId: 'grid'
			    });  
			    //��ʼ�����ص�һ��Tab��grid
			    grid1.getStore().load();
			    
			    //###################grid2
			    var proxy2 = new Ext.data.HttpProxy({
					url:'serverAjaxManager.ajax?action=ajaxHostInfolist'
				});
				var reader2 = new Ext.data.JsonReader({
						root : 'monitorNodeList'
					},[
						{name : 'id' ,type:'int'},
						{name : 'ipAddress'},
						{name : 'alias'},
						{name : 'status'},
						{name : 'category'},
						{name : 'type'},
						{name : 'subtype'},
						{name : 'pingValue' ,type:'int'},
						{name : 'cpuValue' ,type:'int'},
						{name : 'memoryValue' ,type:'int'},
						{name : 'cpuValueColor'},
						{name : 'memoryValueColor'},
						{name : 'inutilhdxValue' ,type:'int'},
						{name : 'oututilhdxValue' ,type:'int'},
						{name : 'eventListCount' ,type:'int'},
						{name : 'collectType'},
						{name : 'eventListSummary'},
						{name : 'entityNumber'}
				]);
			    var dataStore2= new Ext.data.Store({
			        autoLoad: false,
			        baseParams: {type: 'net'},
					proxy : proxy2,
					reader : reader2
				});
				var grid2 = new Ext.grid.GridPanel({
			        store: dataStore2,
			        title:'�����豸',
			        columns: [
			            {
			                id       :'id',
			                header   : '�豸ID', 
			                width :70,
			                sortable : true, 
			                dataIndex: 'id'
			            },
			            {
			                header   : 'ip��ַ', 
			                sortable : true, 
			                renderer : 'ipAddress', 
			                dataIndex: 'ipAddress'
			            },
			            {
			                header   : '����', 
			                sortable : true, 
			                renderer : function(alias,_this,_data){ 
			                	var status = _data.get('status');
			                	var statusImg = '';
					        	if("1" ==status){
					        		statusImg = "alarm_level_1.gif";
					        	}else if("2"==status){
					        		statusImg = "alarm_level_2.gif";
					        	}else if("3"==status){
					        		statusImg = "alert.gif";
					        	}else{
					        		statusImg = "status_ok.gif";
					        	};
					        	var nodeId = _data.get('id');
					        	var type = _data.get('type');
					        	var subtype = _data.get('subtype');
					        	var node_ip = _data.get('ipAddress');
			                	 var tabstr = "<img src='"+rootpath+"/resource/image/topo/"+statusImg+"'>  <a href='javascript:openTologyAndLocation(\""+rootpath+"\","+nodeId+",\""+type+"\",\""+subtype+"\",\""+node_ip+"\")'>"+alias+"</a>";
							     return tabstr;
							},    
			                dataIndex: 'alias'
			            },
			            {
			                header   : '������(%)', 
			                sortable : true, 
			                renderer : 'pingValue', 
			                dataIndex: 'pingValue'
			            },
			            {
			                header   : 'CPU(%)', 
			                sortable : true, 
			                renderer :  function(cpuValue,_this,_data){ 
			                	 var cpuUnUsed = 100-cpuValue;
			                	 //ȡ����ǰcpu����ɫ  ��ǰcolumn ��index������
			                	// var itemid = this.getItemId();
			                	var cpuValueColor = _data.get('cpuValueColor');
			                	 var tabstr =  '<table width="100%"><tr><td>'+cpuValue+'%</td>' +
			                	 									'<td>' +
			                	 										'<table border=1 height=15  bgcolor=#ffffff>' +
			                	 											'<tr><td width='+cpuValue+'% bgcolor='+cpuValueColor+'></td><td width='+cpuUnUsed+'% bgcolor=#ffffff></td></tr></table>' +
			                	 									'</td></tr></table>';
							     return tabstr;
							},   
			                dataIndex: 'cpuValue'
			            },
			            {
			                header   : '�ڴ�(%)', 
			                sortable : true, 
			                renderer : function(memoryValue,_this,_data){ 
			                	 var memoryUnUsed = 100-memoryValue;
			                	 //�ڴ����ɫ
			                	var memoryValueColor = _data.get('memoryValueColor');
			                	 var tabstr =  '<table width="100%"><tr><td>'+memoryValue+'%</td>' +
			                	 									'<td>' +
			                	 										'<table border=1 height=15  bgcolor=#ffffff>' +
			                	 											'<tr><td width='+memoryValue+'% bgcolor='+memoryValueColor+'></td><td width='+memoryUnUsed+'% bgcolor=#ffffff></td></tr></table>' +
			                	 									'</td></tr></table>';
							     return tabstr;
							},   
			                dataIndex: 'memoryValue'
			            },
			            {
			                header   : '�������(KB/S)', 
			                sortable : true, 
			                renderer : 'inutilhdxValue', 
			                dataIndex: 'inutilhdxValue'
			            },
			            {
			                header   : '��������(KB/S)', 
			                sortable : true, 
			                renderer : 'oututilhdxValue', 
			                dataIndex: 'oututilhdxValue'
			            }
			       	],
			        stripeRows: true,
			        autoExpandColumn: 'id',
                    bodyStyle : 'width:100%',   
			        autoHeight : true,
			        viewConfig:{   
						forceFit:true  
					}, 
			        stateful: false,
			        stateId: 'grid'
			    });  
			//######################grid3
			    var proxy3 = new Ext.data.HttpProxy({
					url:'serverAjaxManager.ajax?action=ajaxHostInfolist'
				});
				var reader3 = new Ext.data.JsonReader({
						root : 'monitorDBDTOList'
					},[
						{name : 'id' ,type:'int'},
						{name : 'ipAddress'},
						{name : 'alias'},
						{name : 'sid'},
						{name : 'status'},
						{name : 'dbtype'},
						{name : 'mon_flag'},
						{name : 'dbname'},
						{name : 'port' ,type:'int'},
						{name : 'pingValue'}
				]);
			    var dataStore3 = new Ext.data.Store({
			        autoLoad: false,
			        baseParams: {type: 'db'},
					proxy : proxy3,
					reader : reader3
				});
			    // manually load local data
			    var grid3 = new Ext.grid.GridPanel({
			        store: dataStore3,
			        title:'���ݿ�',
			        columns: [
			            {
			                id       :'id',
			                header   : '���ݿ�ID', 
			                width :70,
			                sortable : true, 
			                dataIndex: 'id'
			            },
			            {
			                header   : '����', 
			                sortable : true, 
			                renderer : 'dbtype', 
			                dataIndex: 'dbtype'
			            },
			            {
			                header   : '����', 
			                sortable : true, 
			                renderer : function(alias,_this,_data){ 
			                	var nodeId = _data.get('id');
					        	var type = _data.get('type');
					        	var subtype = _data.get('subtype');
					        	var node_ip = _data.get('ipAddress');
					        	 var tabstr = "<a href='javascript:openTologyAndLocation(\""+rootpath+"\","+nodeId+",\""+type+"\",\""+subtype+"\",\""+node_ip+"\")'>"+alias+"</a>";
			                },
			                dataIndex: 'alias'
			                
			            },
			            {
			                header   : 'ip��ַ', 
			                sortable : true, 
			                renderer : 'ipAddress', 
			                dataIndex: 'ipAddress'
			            },
			            {
			                header   : '�˿�', 
			                sortable : true, 
			                renderer : 'port', 
			                dataIndex: 'port'
			            },
			            {
			                header   : '����״̬', 
			                sortable : true, 
			                renderer : 'mon_flag', 
			                dataIndex: 'mon_flag'
			            },
			            {
			                header   : '״̬', 
			                sortable : true, 
			                renderer : function(status,_this,_data){ 
			                	var statusImg = '';
					        	if("1" ==status){
					        		statusImg = "alarm_level_1.gif";
					        	}else if("2"==status){
					        		statusImg = "alarm_level_2.gif";
					        	}else if("3"==status){
					        		statusImg = "alert.gif";
					        	}else{
					        		statusImg = "status_ok.gif";
					        	};
			                	 var tabstr = "<img src='"+rootpath+"/resource/image/topo/"+statusImg+"'>";
							     return tabstr;
							},   
			                dataIndex: 'status'
			            },
			            {
			                header   : '������', 
			                sortable : true, 
			                renderer : 'pingValue', 
			                dataIndex: 'pingValue'
			            }
			       	],
			        stripeRows: true,
			        autoExpandColumn: 'id',
                    bodyStyle : 'width:100%',   
			        autoHeight : true,
			        viewConfig:{   
						forceFit:true  
					}, 
			        stateful: false,
			        stateId: 'grid'
			    });  
			    
			//TabPanel width:616,
		    var tabs = new Ext.TabPanel({
		    	id:'devicexn',
		    	renderTo:"tab_list",
                width:Ext.get("tab_list").getWidth(),
		    	height:300,
		        activeTab: 0, 
		        plain:true,
		        defaults:{autoScroll: true},
		        items:[
      		  	 	grid1,
        		    grid2,
        		    grid3
   				]
		    }); 
		    
		    Ext.get('tab_list').on('resize',function(){
		    	tabs.setWidth(Ext.get('tab_list').getWidth());
		    	tabs.onLayout();
		    }); 
		    
		    //����Tabҳ���л����¼�
		    tabs.on("tabchange",function(currentTab, newTab ){
		    	//����store
		    	newTab.getStore().load();
		    } );
		};

//������ͼ���Ҷ�λ
function openTologyAndLocation(rootpath,nodeId,type,subtype,ipaddress) {
	TopoRemoteService.getNodeTologyXMLName(nodeId,type,subtype, {
		callback:function(xmlName) {
			var width = screen.width - 40;
			var height = screen.height - 40;
			if(xmlName=="network.jsp") {
				var topologyWindows = window.open(rootpath+"/topology/network/index.jsp","topologyWindows", "toolbar=no,height="+height+",width="+width+",scrollbars=yes,left=0,top=0,resizable=yes");
				//ֻ�ܴ򿪣���δ�ҵ���ε����Ӵ��ڷ���
				//alert(topologyWindows.self.document.getElementById("mainFrame").contentWindow.topFrame1.searchNodeById(nodeId));
				
				var bbb = setInterval(function() {
					if (locationAAA(topologyWindows, ipaddress)) {
						clearInterval(bbb);
					}
				},500);
				//window.setTimeout(function() {
					//locationAAA(topologyWindows, ipaddress);
				//}, 2000);
			} else {
				var topologyWindows = window.open(rootpath+"/topology/submap/index.jsp?submapXml="+xmlName,"topologyWindows", "toolbar=no,height="+height+",width="+width+",scrollbars=yes,screenX=0,screenY=0,resizable=yes");
				//topologyWindows.frames["mainFrame"].frames["topFrame"].windows.searchNodeById(nodeId);
			
				var bbb = setInterval(function() {
					if (locationAAA(topologyWindows, ipaddress)) {
						clearInterval(bbb);
					}
				},500);
			}
		}
	});
}

function locationAAA(topologyWindows, ipaddress) {
	if (!topologyWindows.self.document.getElementById("mainFrame")) {
		return false;
	}
	if (!topologyWindows.self.document.getElementById("mainFrame").contentWindow) {
		return false;
	}
	if (!topologyWindows.self.document.getElementById("mainFrame").contentWindow.topFrame) {
		return false;
	}
	if (!topologyWindows.self.document.getElementById("mainFrame").contentWindow.topFrame.searchNodeByIp) {
		return false;
	}
	if (!topologyWindows.self.document.getElementById("mainFrame").contentWindow.mainFrame) {
		return false;
	}
	if (!topologyWindows.self.document.getElementById("mainFrame").contentWindow.mainFrame.isloadFlag) {
		return false;
	}
	topologyWindows.self.document.getElementById("mainFrame").contentWindow.topFrame.searchNodeByIp(ipaddress);
	return true;
	//alert(topologyWindows.self.document.getElementById("mainFrame").getElementById("parentFrame"));
}