/*******************************************************************************
 * netdetail.js
 * 
 * @Author: ����
 * 
 * @Date: 2010-12-10
 * 
 * @Function:
 * �� js ����Ҫ��������ɷ������豸��ϸ��Ϣҳ�������չʾ��
 * ��ҳ������� js �󣬴� js ����ҳ�������ɺ��Զ�ִ�� init() ������
 * ��ʼ��ҳ������ݡ�
 *
 ******************************************************************************/


if (window.addEventListener) { 
	window.addEventListener("load", init, false); 
} else if (window.attachEvent) { 
	window.attachEvent("onload", init); 
} else { 
	window.onload = init; 
}


// ҳ���ʼ��
function init() { 
	useLoadingMessage("Loading"); 
	var nodeid = $("nodeid").value;
	var type = $("type").value;
	var subtype = $("subtype").value;
	var hostDetail= new HostDetail(nodeid, type, subtype);
	hostDetail.show();
}


function HostDetail(nodeid, type, subtype){ 
	
	// �豸id
	this.nodeid = nodeid;
	
	// �豸����
	this.type = type;
	
	// �豸������
	this.subtype = subtype;
	
	// �豸
	this.hostNode = null;
	
	// ��ǩҳ�б�
	this.tabList = null;
	
	// ��ǰѡ�еı�ǩҳ(��ҳ������ʾ�ı�ǩҳ)
	this.currTab = null;
	
	// ҳ���б�ǩҳ���ڵ� DIV
	this.detailTabDIV = null;
	
	// ��ǰѡ�еı�ǩҳ�б���
	this.titleList = null;
	
	// ҳ�����������ڵ� DIV
	this.detailDataDIV = null;
	
	// ҳ�����������ڵ� table
	this.detailDataTABLE = null;
	
	// tab ҳ ѡ�� ʱ�� CSS
	this.tabSelectedClassName = "detail-data-title";
	
	// tab ҳ δѡ�� ʱ�� CSS
	this.tabUnSelectedClassName = "detail-data-title-out";
	
	// tab ҳ ������� ʱ�� CSS
	this.tabOnmouseoverClassName = "detail-data-title";
	
	// tab ҳ ����Ƴ� ʱ�� CSS
	this.tabOnmouseoutClassName = "detail-data-title-out";
	
	// ��ȡ��Ŀ����·��
	this.rootPath = $('rootPath').value;
	
	// SWF �ļ��İ汾
	this.SWFVersion = 8;
	
	// SWF �ļ��ĵ�ɫ
	this.SWFbackgroundclor = "#ffffff";
	
	// SWF ·��
	this.SWFPath = this.rootPath + "/flex/";
	
	// ��ǰ������һ�� SWF ��
	this.currCreateSWF = null;
	
	// ϵͳ��Ϣ���� �������� SWF �ļ���
	this.Pie_ComponentSWFName = "Pie_Component";
	
	this.pingSWFName = "Oracle_Ping";
	
	// ��ϸ��Ϣ�� �����б��� ���е�˳�� 
	// ���� -- asc
	// �ݼ� -- desc
	this.dataTROrder = null;
	
	// �Ӻ�̨�첽��ȡ������
	this.data = null;
		
	// HostDetail �౾�� �ñ���Ϊ�����ȫ�ֱ������ڻص���תʱ����
	ObjectSelf = this;
	
	
}

HostDetail.prototype = {
	
	
	// ʹ��  id , �Լ� tagName ������һ��element
	createElement: function (id , tagName){
		var element = document.createElement(tagName);
		element.id = id;
		return element;
	},
	
	// ����չʾ��ϸ��Ϣҳ��ĺ���
	show: function (){
		
		// ��ȡ��ϸ��Ϣҳ���ϱ�ǩҳ���ڵ� div
		this.detailTabDIV = $('detailTabDIV');
		
		if(!this.detailTabDIV){
			//alert("����ҳ���д���һ�� id=detailTabDIV �� div ���ڴ�ű�ǩҳ");
			return;
		}

		this.detailDataDIV = $('detailDataDIV');
		
		if(!this.detailDataDIV){
			alert("����ҳ���д���һ�� id=detailDataDIV �� div ���ڴ�ŵ�ǰ��ǩҳ����ϸ����");
			return;
		}
		
		if(!this.detailDataDIV){
			alert("����ҳ���д���һ�� id=detailDataDIV �� div ���ڴ�ŵ�ǰ��ǩҳ����ϸ����");
			return;
		}
		
		var freshSystemInfo = $('freshSystemInfo');
		// ��� ˢ��ϵͳ��Ϣ�ķ���
		if(freshSystemInfo){
			freshSystemInfo.onclick = function (){
				StorageRemoteService.getStorageInfo(ObjectSelf.nodeid, ObjectSelf.type, ObjectSelf.subtype, {
					callback:function(data){
						ObjectSelf.hostNode =  data;
						ObjectSelf.getSystemInfo();
					}
				});
			}
		}
		
		StorageRemoteService.getStorageInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				ObjectSelf.hostNode =  data;
				ObjectSelf.getSystemInfo();
			}
		});
		
		this.getTabInfo();
		
	},
	
	// ��ȡ�豸ϵͳ��Ϣ
	getSystemInfo: function (){
//		StorageRemoteService.getSystemInfo(this.nodeid, this.type, this.subtype, {
//			callback:function(data){
//				ObjectSelf.callbackgetSystemInfo(data);
//			}
//		});
		ObjectSelf.callbackgetSystemInfo(null);
	},
	
	// ��ȡ�豸ϵͳ��Ϣ����Ļص�����
	callbackgetSystemInfo: function (data){ 
		this.data = data;
		this.createSystemInfo();
	},
	
	// ��ȡ�豸ϵͳ��Ϣ�� �����豸ϵͳ��Ϣ
	createSystemInfo: function (){ 
		StorageRemoteService.getCurrDayPingAvgInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				var currDayPingAvgInfo_str = data;
				var currDayPingAvgInfo_float = parseFloat(currDayPingAvgInfo_str);
				if(isNaN(currDayPingAvgInfo_float)){
					currDayPingAvgInfo_float = 0;
				}
				var currDayUnPingAvgInfo_float = 100 - currDayPingAvgInfo_float;
				var Pie_ComponentSWFPath = ObjectSelf.SWFPath + ObjectSelf.Pie_ComponentSWFName + ".swf?" +
				"percent1=" + new String(currDayPingAvgInfo_str) + 
				"&percentStr1=����" +
				"&percent2=" + new String(currDayUnPingAvgInfo_float) + 
				"&percentStr2=������";
				var currDayPingAvgInfoTD = $('currDayPingAvgInfo');
				ObjectSelf.removeAllChild(currDayPingAvgInfoTD);
				ObjectSelf.createSWFTABLE(currDayPingAvgInfoTD, Pie_ComponentSWFPath, ObjectSelf.Pie_ComponentSWFName, "160", "160", "8", "#ffffff");
			}
		});
		
		var mon_flag = this.hostNode.mon_flag;
		if(mon_flag == 0){
			mon_flag = "δ���";
		}else {
			mon_flag = "�Ѽ��";
		}
		
		var status = this.hostNode.status;
		if(status == 0){
			status = "δ����";
		}else {
			status = "����";
		}
		
		$('systemInfo_name').innerHTML = this.hostNode.name;
		$('systemInfo_ipaddress').innerHTML = this.hostNode.ipaddress;
		$('systemInfo_mon_flag').innerHTML = mon_flag;
		$('systemInfo_status').innerHTML = status;
		$('systemInfo_serialNumber').innerHTML = this.hostNode.serialNumber;
		
		StorageRemoteService.getProducerInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				$('systemInfo_producerStr').innerHTML = data;
			}
		});
	},
	
	// ��ȡ��ϸ��Ϣҳ���ϱ�ǩҳ����Ϣ
	getTabInfo: function (){
		StorageRemoteService.getTabInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				ObjectSelf.callbackgetTabInfo(data);
			}
		});
	},
	
	// ��ȡ��ϸ��Ϣҳ���ϱ�ǩҳ����Ϣ����Ļص�����
	callbackgetTabInfo: function (data){
		if(data && data.length > 0){
			this.tabList = data;
			// ������ϸ��Ϣҳ���ϱ�ǩҳ
			this.createTabInfo();
		}
	},
	
	// ������ϸ��Ϣҳ���ϱ�ǩҳ
	createTabInfo:function (){
		
		var tabTable = this.createElement("", "table");
		this.detailTabDIV.appendChild(tabTable);
		
		var tabTbody = this.createElement("", "tbody");
		tabTable.appendChild(tabTbody);
		
		var tr = this.createElement("", "tr")
		tabTbody.appendChild(tr);
		
		var td = this.createElement("", "td");
		td.width = this.tabList.length * 72;
		tr.appendChild(td);
		
		var table = this.createElement("", "table");
		td.appendChild(table);
		
		var tbody = this.createElement("", "tbody");
		table.appendChild(tbody);
		
		tr = this.createElement("", "tr")
		tbody.appendChild(tr);
		
		for(var i = 0; i < this.tabList.length; i++){
			// ѭ�����ɱ�ǩҳ
			var tab = this.tabList[i];
			var tabTd = this.createElement(tab.id, "td");
			tabTd.className = this.getTabUnSelectedClassName();
			tabTd.onmouseover = function () {
				this.className = ObjectSelf.getTabOnmouseoverClassName();
			};
			tabTd.onmouseout = function () {
				this.className= ObjectSelf.getTabOnmouseoutClassName();
			};
			tabTd.onclick = (function (tab){
				return function(){
					ObjectSelf.setCurrTab(tab);
				};
			})(tab);
			tabTd.insertAdjacentHTML("afterBegin", tab.name);
			tabTd.id = tab.id;
			tr.appendChild(tabTd);
			
			if(i == 0){
				this.setCurrTab(tab);
			}
		}
	},
	
	// ����ǰ��ǩҳ�ĺ���
	setCurrTab: function(tab){
		if(this.currTab){
			$(this.currTab.id).className = this.getTabUnSelectedClassName();
			$(this.currTab.id).onmouseover = function () {
				this.className = ObjectSelf.getTabOnmouseoverClassName();
			};
			$(this.currTab.id).onmouseout = function () {
				this.className = ObjectSelf.getTabOnmouseoutClassName();
			};
		}
		this.currTab = tab;
		this.titleList = this.currTab.titleList;
		$(this.currTab.id).className = this.getTabSelectedClassName();
		$(this.currTab.id).onmouseover = function () {
			this.className = ObjectSelf.getTabOnmouseoverClassName();
		};
		$(this.currTab.id).onmouseout = function () {
			this.className = ObjectSelf.getTabOnmouseoverClassName();
		};
		this.createDetailDataTABLE();
		this.createTitleInfo();
		eval("this." + this.currTab.action + "()");
	},
	
	// ��ȡ tab ҳ ѡ�� ʱ�� CSS
	getTabSelectedClassName: function(){
		return this.tabSelectedClassName;
	},
	
	// ��ȡ tab ҳ δѡ�� ʱ�� CSS
	getTabUnSelectedClassName: function(){
		return this.tabUnSelectedClassName;
	},
	
	// ��ȡ tab ҳ ������� ʱ�� CSS
	getTabOnmouseoverClassName: function(){
		return this.tabOnmouseoverClassName;
	},
	
	// ��ȡ tab ҳ ����Ƴ� ʱ�� CSS
	getTabOnmouseoutClassName: function(){
		return this.tabOnmouseoutClassName;
	},
	
	// ɾ��һ�� HTMLElement �Ľڵ�������ӽڵ�
	removeAllChild: function(HTMLElement){
		if(HTMLElement){
			// ��� HTMLElement �ڵ㲻Ϊ��
			while (HTMLElement.hasChildNodes()) {
				// ��� HTMLElement �ڵ㻹���ӽڵ���ɾ�����һ���ӽڵ�
				HTMLElement.removeChild(HTMLElement.firstChild);
			}
		}
	},
	
	// ������ϸ��Ϣҳ�����ݵı��
	createDetailDataTABLE: function(){
		if(!this.detailDataTABLE){
			// �����ϸ��Ϣҳ�����ݵı��Ϊ null
			this.detailDataTABLE = this.createElement("detailDataTABLE", "table");
			this.detailDataDIV.appendChild(this.detailDataTABLE);
		}
		this.removeAllChild(this.detailDataTABLE);
		
		var titleBODY = this.createElement("detailDataTABLE-TBODY-Title", "tbody");
		this.detailDataTABLE.appendChild(titleBODY);
		
		var listBODY = this.createElement("detailDataTABLE-TBODY-List", "tbody");
		this.detailDataTABLE.appendChild(listBODY);
	},
	
	
	// ������ϸ��Ϣҳ�����ݵı�����
	createTitleInfo: function (titleList){
		if(titleList){
			this.titleList = titleList;
		}
		
		var titleTBODY = this.detailDataTABLE.tBodies[0];
		if(!titleTBODY){
			this.createDetailDataTABLE();
		}
		this.removeAllChild(titleTBODY);
		
		if(this.titleList && this.titleList.length > 0){
			// �����ȡ�ı��ⲻΪ�ղ��Ҹ������� 0
			
			var titleTRRow = 0;
			
			var titleTR = this.createElement("titleTR-" + titleTRRow, "tr")
			titleTBODY.appendChild(titleTR);
			
			for(var i = 0; i < this.titleList.length; i++){
				// ѭ��ÿһ��������
				var title = this.titleList[i];
				
				if(title.rowNum > titleTRRow){
					titleTRRow = titleTRRow + 1;
					titleTR = this.createElement(this.currTab.id + "titleTR-" + titleTRRow, "tr")
					titleTBODY.appendChild(titleTR);
				}
				
				var titleId = "";
				if(title.id){
					titleId = this.currTab.id + "-titleTD-" + title.id;
				}
				var titleTD = this.createElement(titleId, "td");
				titleTD.className = "body-data-title";
				if(title.cols){
					titleTD.colSpan = title.cols;
				}
				titleTD.style.height = 25;
				titleTD.insertAdjacentHTML("afterBegin", title.content);
				titleTR.appendChild(titleTD);
			}
			
		}
	},
	
	// ��ȡ�豸��ϸ��Ϣ���ݺ� �����豸��ϸ��Ϣ�����б�ÿһ�е� td �� ��������Щ td �����鼯��
	// length  		--- ��Ҫ���� td �ĸ��� 
	// TRContain	--- ÿһ�� td ������ �� tr �� ���δ�գ� ����Щ td ��������뵽 tr �� ��
	// 					��Ҫ�������Լ����뵽 tr �У����򽫲�����ʾ��ҳ����
	createDetailDataTD : function(length, TRContain){
		var detailDataTDArray = new Array();
		if(!length || length == 0){ 
			return detailDataTDArray;
		}
		for(var i = 0; i < length; i++){
			var td = this.createElement("", "td");
			td.className = 'detail-data-body-list';
			detailDataTDArray.push(td);
			if(TRContain){
				TRContain.appendChild(td);
			}
		}
		return detailDataTDArray;
	},
	
	// -------------------------------------------------------------------------------
	// ------------			����Ϊ���� tab ҳ����Ҫ����Ϣ			----------------------
	// -------------------------------------------------------------------------------
	
	// -------------------------------------------------------------------------------
	// ------------			�豸��ͨ����Ϣ    START				----------------------
	// ��ȡ�豸��ͨ����Ϣ
	getPingInfo: function(){
//		StorageRemoteService.getPingInfo(this.nodeid, this.type, this.subtype, {
//			callback:function(data){
//				ObjectSelf.callbackgetPingInfo(data);
//			}
//		});
		this.callbackgetPingInfo(null);
	},
	
	// ��ȡ�豸������Ϣ����Ļص�����
	callbackgetPingInfo: function(data){
		this.data = data;
		this.createPingInfo();
	},
	
	// ��ȡ�豸��ͨ����Ϣ�� �����豸��ͨ����Ϣ
	createPingInfo: function(){
		if(!this.detailDataTABLE){
			// �����ϸ��Ϣҳ�����ݵı��Ϊ null
			this.detailDataTABLE = this.createElement("detailDataTABLE", "table");
			this.detailDataDIV.appendChild(this.detailDataTABLE);
		}

		var tbody = this.createElement("", "tbody");
		this.detailDataTABLE.appendChild(tbody);

		var tr = this.createElement("", "tr");
		tbody.appendChild(tr);
		
//		// ���� responseTime ���� td
//		var td = this.createElement("", "td");
//		tr.appendChild(td);
//		// ���� responseTime ���� table
//		var responseTimePath = this.SWFPath + this.responseTimeSWFName + ".swf?ipadress=" + this.hostNode.ipAddress; 
//		var responseTimeSWFTABLE = this.createSWFTABLE(td, responseTimePath, this.responseTimeSWFName, "346", "250", this.SWFVersion, this.SWFbackgroundclor);

		// ���� Ping ���� td
		var td = this.createElement("", "td");
		tr.appendChild(td);
		// ���� Ping ���� table
		//alert(this.hostNode.ipaddress);
		var pingSWFPath = this.SWFPath + this.pingSWFName + ".swf?ipadress=" + this.hostNode.ipaddress+"&category=STORPing"; 
		var pingSWFTABLE = this.createSWFTABLE(td, pingSWFPath, this.pingSWFName, "98%", "250", this.SWFVersion, this.SWFbackgroundclor);
		
		
	},
	
	// ------------			�豸��ͨ����Ϣ    END					----------------------
	// -------------------------------------------------------------------------------
	
	
	// -------------------------------------------------------------------------------
	// ------------			�豸����λ����Ϣ   START				----------------------
	// ��ȡ�豸����λ����Ϣ
	getArraySiteInfo: function(){
		StorageRemoteService.getArraySiteInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				ObjectSelf.callbackgetArraySiteInfo(data);
			}
		});
	},
	
	// ��ȡ�豸����λ����Ϣ����Ļص�����
	callbackgetArraySiteInfo: function(data){
		this.data = data;
		this.createArraySiteInfo();
	},
	
	// ��ȡ�豸����λ����Ϣ�� �����豸����λ����Ϣ
	createArraySiteInfo: function(){
		var listTBODY = this.detailDataTABLE.tBodies[1];
		if(!listTBODY){
			// �����ϸ��Ϣҳ�����ݱ���� �����б�� TBODY Ϊ null �����´��� (һ�㲻�����)
			this.createDetailDataTABLE();
		}
		// ɾ���б������е�Ԫ��
		this.removeAllChild(listTBODY);
		var arraySiteInfoList = this.data;
		if(!arraySiteInfoList || !arraySiteInfoList.length){
			return;
		}
		
		var currTabId = this.currTab.id;
		var index_cellIndex = $(currTabId + "-titleTD-" + "index").cellIndex;
		var arsite_cellIndex = $(currTabId + "-titleTD-" + "arsite").cellIndex;
		var da_pair_cellIndex = $(currTabId + "-titleTD-" + "da_pair").cellIndex;
		var dkcap_cellIndex = $(currTabId + "-titleTD-" + "dkcap").cellIndex;
		var state_cellIndex = $(currTabId + "-titleTD-" + "state").cellIndex;
		var array_cellIndex = $(currTabId + "-titleTD-" + "array").cellIndex;
		
		var length = arraySiteInfoList.length;
		var i = 0
		while(i < length){
			var arraySiteInfo = arraySiteInfoList[i];
			i++;
			var index = i;									// ���
			var arsite = arraySiteInfo.arsite;				// arsite
			var da_pair = arraySiteInfo.da_pair;			// DA Pair
			var dkcap = arraySiteInfo.dkcap;				// dkcap (10^9B)
			var state = arraySiteInfo.state;				// State
			var array = arraySiteInfo.array;				// Array
			
			var tr = this.createElement(this.currTab.id + "-dataTR-" + index, "tr");
			tr.height = 25;
			listTBODY.appendChild(tr);
			var detailDataTDArray = this.createDetailDataTD(6, tr);
			
			// ���
			var td = detailDataTDArray[index_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "index";
			td.insertAdjacentHTML("afterBegin", index);
			
			// arsite
			var td = detailDataTDArray[arsite_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "arsite";
			td.insertAdjacentHTML("afterBegin", arsite);
			
			// DA Pair
			var td = detailDataTDArray[da_pair_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "da_pair";
			td.insertAdjacentHTML("afterBegin", da_pair);
			
			// dkcap (10^9B)
			var td = detailDataTDArray[dkcap_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "dkcap";
			td.insertAdjacentHTML("afterBegin", dkcap);
			
			// State
			var td = detailDataTDArray[state_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "state";
			td.insertAdjacentHTML("afterBegin", state);
			
			// Array
			var td = detailDataTDArray[array_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "array";
			td.insertAdjacentHTML("afterBegin", array);
			
		}
	},
	
	// ------------			�豸����λ����Ϣ    END					----------------------
	// -------------------------------------------------------------------------------
	
	
	// -------------------------------------------------------------------------------
	// ------------			�豸������Ϣ    START					----------------------
	// ��ȡ�豸������Ϣ 
	getArrayInfo: function(){
		StorageRemoteService.getArrayInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				ObjectSelf.callbackgetArrayInfo(data);
			}
		});
	},
	
	// ��ȡ�豸������Ϣ����Ļص�����
	callbackgetArrayInfo: function(data){
		this.data = data;
		this.createArrayInfo();
	},
	
	// ��ȡ�豸������Ϣ�� �����豸������Ϣ
	createArrayInfo: function(){
		var listTBODY = this.detailDataTABLE.tBodies[1];
		if(!listTBODY){
			// �����ϸ��Ϣҳ�����ݱ���� �����б�� TBODY Ϊ null �����´��� (һ�㲻�����)
			this.createDetailDataTABLE();
		}
		// ɾ���б������е�Ԫ��
		this.removeAllChild(listTBODY);
		var arrayInfoList = this.data;
		if(!arrayInfoList || !arrayInfoList.length){
			return;
		}
		
		var currTabId = this.currTab.id;
		var index_cellIndex = $(currTabId + "-titleTD-" + "index").cellIndex;
		var array_cellIndex = $(currTabId + "-titleTD-" + "array").cellIndex;
		var state_cellIndex = $(currTabId + "-titleTD-" + "state").cellIndex;
		var data_cellIndex = $(currTabId + "-titleTD-" + "data").cellIndex;
		var raidtype_cellIndex = $(currTabId + "-titleTD-" + "raidtype").cellIndex;
		var arsite_cellIndex = $(currTabId + "-titleTD-" + "arsite").cellIndex;
		var rank_cellIndex = $(currTabId + "-titleTD-" + "rank").cellIndex;
		var da_pair_cellIndex= $(currTabId + "-titleTD-" + "da_pair").cellIndex;
		var ddmcap_cellIndex = $(currTabId + "-titleTD-" + "ddmcap").cellIndex;
		
		var length = arrayInfoList.length;
		var i = 0;
		while(i < length){
			var arrayInfo = arrayInfoList[i];
			i++;
			var index = i;								// ���
			var array = arrayInfo.array;				// Array
			var state = arrayInfo.state;				// State
			var data = arrayInfo.data;					// Data
			var raidtype = arrayInfo.raidtype;			// RAIDtype
			var arsite = arrayInfo.arsite;				// arsite
			var rank = arrayInfo.rank;					// Rank
			var da_pair = arrayInfo.da_pair;			// DA Pair
			var ddmcap = arrayInfo.ddmcap;				// DDMcap (10^9B)
			
			var tr = this.createElement("", "tr");
			tr.height = 25;
			listTBODY.appendChild(tr);
			var detailDataTDArray = this.createDetailDataTD(9, tr);
			
			// ���
			var td = detailDataTDArray[index_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "index";
			td.insertAdjacentHTML("afterBegin", index);
			
			// Array
			var td = detailDataTDArray[array_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "array";
			td.insertAdjacentHTML("afterBegin", array);
			
			// State
			var td = detailDataTDArray[state_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "state";
			td.insertAdjacentHTML("afterBegin", state);
			
			// Data
			var td = detailDataTDArray[data_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "data";
			td.insertAdjacentHTML("afterBegin", data);
			
			// RAIDtype
			var td = detailDataTDArray[raidtype_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "raidtype";
			td.insertAdjacentHTML("afterBegin", raidtype);
			
			// arsite
			var td = detailDataTDArray[arsite_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "arsite";
			td.insertAdjacentHTML("afterBegin", arsite);
			
			// Rank
			var td = detailDataTDArray[rank_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "rank";
			td.insertAdjacentHTML("afterBegin", rank);
			
			// DA Pair
			var td = detailDataTDArray[da_pair_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "da_pair";
			td.insertAdjacentHTML("afterBegin", da_pair);
			
			// DDMcap (10^9B)
			var td = detailDataTDArray[ddmcap_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "ddmcap";
			td.insertAdjacentHTML("afterBegin", ddmcap);
		}
		
	},
	
	
	// ------------			�豸������Ϣ    END					----------------------
	// -------------------------------------------------------------------------------
	
	
	// -------------------------------------------------------------------------------
	// ------------			�豸����Ϣ    START					----------------------
	// ��ȡ�豸����Ϣ
	getRankInfo: function (){
		StorageRemoteService.getRankInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				ObjectSelf.callbackgetRankInfo(data);
			}
		});
	},
	
	// ��ȡ�豸����Ϣ����Ļص�����
	callbackgetRankInfo: function(data){
		this.data = data;
		this.createRankInfo();
	},
	
	// ��ȡ�豸����Ϣ�� �����豸����Ϣ
	createRankInfo: function(){
		var listTBODY = this.detailDataTABLE.tBodies[1];
		if(!listTBODY){
			// �����ϸ��Ϣҳ�����ݱ���� �����б�� TBODY Ϊ null �����´��� (һ�㲻�����)
			this.createDetailDataTABLE();
		}
		// ɾ���б������е�Ԫ��
		this.removeAllChild(listTBODY);
		
		var rankInfoList = this.data;
		if(!rankInfoList || !rankInfoList.length){
			return;
		}
		
		var currTabId = this.currTab.id;
		var index_cellIndex = $(currTabId + "-titleTD-" + "index").cellIndex;
		var rank_id_cellIndex = $(currTabId + "-titleTD-" + "rank_id").cellIndex;
		var group_cellIndex = $(currTabId + "-titleTD-" + "group").cellIndex;
		var state_cellIndex = $(currTabId + "-titleTD-" + "state").cellIndex;
		var datastate_cellIndex = $(currTabId + "-titleTD-" + "datastate").cellIndex;
		var array_cellIndex = $(currTabId + "-titleTD-" + "array").cellIndex;
		var raidtype_cellIndex = $(currTabId + "-titleTD-" + "raidtype").cellIndex;
		var extpoolid_cellIndex = $(currTabId + "-titleTD-" + "extpoolid").cellIndex;
		var stgtype_cellIndex = $(currTabId + "-titleTD-" + "stgtype").cellIndex;
		
		var length = rankInfoList.length;
		var i = 0;
		while(i < length){
			var rankInfo = rankInfoList[i];
			i++;
			var index = i;								// ���
		    var rank_id = rankInfo.rank_id;				// ID
		    var group = rankInfo.group;					// Group
		    var state = rankInfo.state;					// State
		    var datastate = rankInfo.datastate;			// datastate
		    var array = rankInfo.array;					// Array
		    var raidtype = rankInfo.raidtype;			// RAIDtype
		    var extpoolid = rankInfo.extpoolid;			// extpoolID
		    var stgtype = rankInfo.stgtype;				// stgtype
		    
		    var tr = this.createElement(this.currTab.id + "-dataTR-" + index, "tr");
			tr.height = 25;
			listTBODY.appendChild(tr);
			var detailDataTDArray = this.createDetailDataTD(9, tr);
			
			// ���
			var td = detailDataTDArray[index_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "index";
			td.insertAdjacentHTML("afterBegin", index);
			
			// ID
			var td = detailDataTDArray[rank_id_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "rank_id";
			td.insertAdjacentHTML("afterBegin", rank_id);
			
			// Group
			var td = detailDataTDArray[group_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "group";
			td.insertAdjacentHTML("afterBegin", group);
			
			// State
			var td = detailDataTDArray[state_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "state";
			td.insertAdjacentHTML("afterBegin", state);
			
			// datastate
			var td = detailDataTDArray[datastate_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "datastate";
			td.insertAdjacentHTML("afterBegin", datastate);
			
			// Array
			var td = detailDataTDArray[array_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "array";
			td.insertAdjacentHTML("afterBegin", array);
			
			// RAIDtype
			var td = detailDataTDArray[raidtype_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "raidtype";
			td.insertAdjacentHTML("afterBegin", raidtype);
			
			// extpoolID
			var td = detailDataTDArray[extpoolid_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "extpoolid";
			td.insertAdjacentHTML("afterBegin", extpoolid);
			
			// stgtype
			var td = detailDataTDArray[stgtype_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "stgtype";
			td.insertAdjacentHTML("afterBegin", stgtype);
		}
	},
	
	// ------------			�豸����Ϣ    END						----------------------
	// -------------------------------------------------------------------------------
	
	// -------------------------------------------------------------------------------
	// ------------			�豸��չ����Ϣ    START				----------------------
	// ��ȡ�豸��չ����Ϣ
	getExtpoolInfo: function (){
		StorageRemoteService.getExtpoolInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				ObjectSelf.callbackgetExtpoolInfo(data);
			}
		});
	},
	
	// ��ȡ�豸��չ����Ϣ����Ļص�����
	callbackgetExtpoolInfo: function(data){
		this.data = data;
		this.createExtpoolInfo();
	},
	
	// ��ȡ�豸��չ����Ϣ�� �����豸��չ����Ϣ
	createExtpoolInfo: function(){
		var listTBODY = this.detailDataTABLE.tBodies[1];
		if(!listTBODY){
			// �����ϸ��Ϣҳ�����ݱ���� �����б�� TBODY Ϊ null �����´��� (һ�㲻�����)
			this.createDetailDataTABLE();
		}
		// ɾ���б������е�Ԫ��
		this.removeAllChild(listTBODY);
		
		var extpoolInfoList = this.data;
		if(!extpoolInfoList || !extpoolInfoList.length){
			return;
		}
		
		var currTabId = this.currTab.id;
		var index_cellIndex = $(currTabId + "-titleTD-" + "index").cellIndex;
		var name_cellIndex = $(currTabId + "-titleTD-" + "name").cellIndex;
		var extpool_id_cellIndex = $(currTabId + "-titleTD-" + "extpool_id").cellIndex;
		var stgtype_cellIndex = $(currTabId + "-titleTD-" + "stgtype").cellIndex;
		var rankgrp_cellIndex = $(currTabId + "-titleTD-" + "rankgrp").cellIndex;
		var status_cellIndex = $(currTabId + "-titleTD-" + "status").cellIndex;
		var availstor_cellIndex = $(currTabId + "-titleTD-" + "availstor").cellIndex;
		var allocated_cellIndex = $(currTabId + "-titleTD-" + "allocated").cellIndex;
		var available_cellIndex = $(currTabId + "-titleTD-" + "available").cellIndex;
		var reserved_cellIndex = $(currTabId + "-titleTD-" + "reserved").cellIndex;
		var numvols_cellIndex = $(currTabId + "-titleTD-" + "numvols").cellIndex;
		
		var length = extpoolInfoList.length;
		var i = 0;
		while(i < length){
			var extpoolInfo = extpoolInfoList[i];
			i++;
			var index = i;									// ���
			var name = extpoolInfo.name;					// Name
		 	var extpool_id = extpoolInfo.extpool_id;		// ID
			var stgtype = extpoolInfo.stgtype;				// stgtype
			var rankgrp = extpoolInfo.rankgrp;				// rankgrp
			var status = extpoolInfo.status;				// status
			var availstor = extpoolInfo.availstor;			// availstor (2^30B)
			var allocated = extpoolInfo.allocated;			// %allocated
			var available = extpoolInfo.available;			// available
			var reserved = extpoolInfo.reserved;			// reserved
			var numvols = extpoolInfo.numvols;				// numvols
		    
		    var tr = this.createElement(this.currTab.id + "-dataTR-" + index, "tr");
			tr.height = 25;
			listTBODY.appendChild(tr);
			var detailDataTDArray = this.createDetailDataTD(11, tr);
			
			// ���
			var td = detailDataTDArray[index_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "index";
			td.insertAdjacentHTML("afterBegin", index);
			
			// Name
			var td = detailDataTDArray[name_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "name";
			td.insertAdjacentHTML("afterBegin", name);
			
			// ID
			var td = detailDataTDArray[extpool_id_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "extpool_id";
			td.insertAdjacentHTML("afterBegin", extpool_id);
			
			// stgtype
			var td = detailDataTDArray[stgtype_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "stgtype";
			td.insertAdjacentHTML("afterBegin", stgtype);
			
			// rankgrp
			var td = detailDataTDArray[rankgrp_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "rankgrp";
			td.insertAdjacentHTML("afterBegin", rankgrp);
			
			// status
			var td = detailDataTDArray[status_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "status";
			td.insertAdjacentHTML("afterBegin", status);
			
			// availstor (2^30B)
			var td = detailDataTDArray[availstor_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "availstor";
			td.insertAdjacentHTML("afterBegin", availstor);
			
			// %allocated
			var td = detailDataTDArray[allocated_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "allocated";
			td.insertAdjacentHTML("afterBegin", allocated);
			
			// reserved
			var td = detailDataTDArray[reserved_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "reserved";
			td.insertAdjacentHTML("afterBegin", reserved);
			
			// numvols
			var td = detailDataTDArray[numvols_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "numvols";
			td.insertAdjacentHTML("afterBegin", numvols);
		}
	},
	
	// ------------			�豸��չ����Ϣ    END					----------------------
	// -------------------------------------------------------------------------------
	
	// -------------------------------------------------------------------------------
	// ------------			�豸�߼�����Ϣ    START				----------------------
	// ��ȡ�豸�߼�����Ϣ
	getFbvolInfo: function (){
		StorageRemoteService.getFbvolInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				ObjectSelf.callbackgetFbvolInfo(data);
			}
		});
	},
	
	// ��ȡ�豸�߼�����Ϣ����Ļص�����
	callbackgetFbvolInfo: function(data){
		this.data = data;
		this.createFbvolInfo();
	},
	
	// ��ȡ�豸�߼�����Ϣ�� �����豸�߼�����Ϣ
	createFbvolInfo: function(){
		var listTBODY = this.detailDataTABLE.tBodies[1];
		if(!listTBODY){
			// �����ϸ��Ϣҳ�����ݱ���� �����б�� TBODY Ϊ null �����´��� (һ�㲻�����)
			this.createDetailDataTABLE();
		}
		// ɾ���б������е�Ԫ��
		this.removeAllChild(listTBODY);
		
		var fbvolInfoList = this.data;
		if(!fbvolInfoList || !fbvolInfoList.length){
			return;
		}
		
		var currTabId = this.currTab.id;
		var index_cellIndex = $(currTabId + "-titleTD-" + "index").cellIndex;
		var name_cellIndex = $(currTabId + "-titleTD-" + "name").cellIndex;
		var fbvol_id_cellIndex = $(currTabId + "-titleTD-" + "fbvol_id").cellIndex;
		var accstate_cellIndex = $(currTabId + "-titleTD-" + "accstate").cellIndex;
		var datastate_cellIndex = $(currTabId + "-titleTD-" + "datastate").cellIndex;
		var configstate_cellIndex = $(currTabId + "-titleTD-" + "configstate").cellIndex;
		var devicemtm_cellIndex = $(currTabId + "-titleTD-" + "devicemtm").cellIndex;
		var datatype_cellIndex = $(currTabId + "-titleTD-" + "datatype").cellIndex;
		var extpool_cellIndex = $(currTabId + "-titleTD-" + "extpool").cellIndex;
		var cap_2_30B_cellIndex = $(currTabId + "-titleTD-" + "cap_2_30B").cellIndex;
		var cap_10_9B_cellIndex = $(currTabId + "-titleTD-" + "cap_10_9B").cellIndex;
		var cap_blocks_cellIndex = $(currTabId + "-titleTD-" + "cap_blocks").cellIndex;
		
		var length = fbvolInfoList.length;
		var i = 0;
		while(i < length){
			var fbvolInfo = fbvolInfoList[i];
			i++;
			var index = i;									// ���
			var name = fbvolInfo.name;						// Name
			var fbvol_id = fbvolInfo.fbvol_id;				// ID
			var accstate = fbvolInfo.accstate;				// accstate
			var datastate = fbvolInfo.datastate;			// datastate
			var configstate = fbvolInfo.configstate;		// configstate
			var devicemtm = fbvolInfo.devicemtm;			// deviceMTM
			var datatype = fbvolInfo.datatype;				// datatype
			var extpool = fbvolInfo.extpool;				// extpool
			var cap_2_30B = fbvolInfo.cap_2_30B;			// cap (2^30B)
			var cap_10_9B = fbvolInfo.cap_10_9B;			// cap (10^9B)
			var cap_blocks = fbvolInfo.cap_blocks;			// cap (blocks)
		    
		    var tr = this.createElement(this.currTab.id + "-dataTR-" + index, "tr");
			tr.height = 25;
			listTBODY.appendChild(tr);
			var detailDataTDArray = this.createDetailDataTD(12, tr);
			
			// ���
			var td = detailDataTDArray[index_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "index";
			td.insertAdjacentHTML("afterBegin", index);
			
			// Name
			var td = detailDataTDArray[name_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "name";
			td.insertAdjacentHTML("afterBegin", name);
			
			// ID
			var td = detailDataTDArray[fbvol_id_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "fbvol_id";
			td.insertAdjacentHTML("afterBegin", fbvol_id);
			
			// accstate
			var td = detailDataTDArray[accstate_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "accstate";
			td.insertAdjacentHTML("afterBegin", accstate);
			
			// datastate
			var td = detailDataTDArray[datastate_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "datastate";
			td.insertAdjacentHTML("afterBegin", datastate);
			
			// configstate
			var td = detailDataTDArray[configstate_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "configstate";
			td.insertAdjacentHTML("afterBegin", configstate);
			
			// deviceMTM
			var td = detailDataTDArray[devicemtm_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "devicemtm";
			td.insertAdjacentHTML("afterBegin", devicemtm);
			
			// datatype
			var td = detailDataTDArray[datatype_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "datatype";
			td.insertAdjacentHTML("afterBegin", datatype);
			
			// extpool
			var td = detailDataTDArray[extpool_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "extpool";
			td.insertAdjacentHTML("afterBegin", extpool);
			
			// cap (2^30B)
			var td = detailDataTDArray[cap_2_30B_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "cap_2_30B";
			td.insertAdjacentHTML("afterBegin", cap_2_30B);
			
			// cap (10^9B)
			var td = detailDataTDArray[cap_10_9B_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "cap_10_9B";
			td.insertAdjacentHTML("afterBegin", cap_10_9B);
			
			// cap (blocks)
			var td = detailDataTDArray[cap_blocks_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "cap_blocks";
			td.insertAdjacentHTML("afterBegin", cap_blocks);
		}
	},
	
	// ------------			�豸�߼�����Ϣ    END					----------------------
	// -------------------------------------------------------------------------------
	
	// -------------------------------------------------------------------------------
	// ------------			�豸������Ϣ    START				----------------------
	// ��ȡ�豸������Ϣ
	getVolgrpInfo: function (){
		StorageRemoteService.getVolgrpInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				ObjectSelf.callbackgetVolgrpInfo(data);
			}
		});
	},
	
	// ��ȡ�豸������Ϣ����Ļص�����
	callbackgetVolgrpInfo: function(data){
		this.data = data;
		this.createVolgrpInfo();
	},
	
	// ��ȡ�豸������Ϣ�� �����豸������Ϣ
	createVolgrpInfo: function(){
		var listTBODY = this.detailDataTABLE.tBodies[1];
		if(!listTBODY){
			// �����ϸ��Ϣҳ�����ݱ���� �����б�� TBODY Ϊ null �����´��� (һ�㲻�����)
			this.createDetailDataTABLE();
		}
		// ɾ���б������е�Ԫ��
		this.removeAllChild(listTBODY);
		
		var volgrpInfoList = this.data;
		if(!volgrpInfoList || !volgrpInfoList.length){
			return;
		}
		
		var currTabId = this.currTab.id;
		var index_cellIndex = $(currTabId + "-titleTD-" + "index").cellIndex;
		var name_cellIndex = $(currTabId + "-titleTD-" + "name").cellIndex;
		var volgrp_id_cellIndex = $(currTabId + "-titleTD-" + "volgrp_id").cellIndex;
		var type_cellIndex = $(currTabId + "-titleTD-" + "type").cellIndex;
		
		var length = volgrpInfoList.length;
		var i = 0;
		while(i < length){
			var volgrpInfo = volgrpInfoList[i];
			i++;
			var index = i;									// ���
			var name = volgrpInfo.name;						// Name
			var volgrp_id = volgrpInfo.volgrp_id;			// ID
			var type = volgrpInfo.type;						// Type
		    
		    var tr = this.createElement(this.currTab.id + "-dataTR-" + index, "tr");
			tr.height = 25;
			listTBODY.appendChild(tr);
			var detailDataTDArray = this.createDetailDataTD(4, tr);
			
			// ���
			var td = detailDataTDArray[index_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "index";
			//td.insertAdjacentHTML("afterBegin", index);
			
			var img = this.createElement("", "img");
			img.src = this.rootPath + "/img/nolines_plus.gif";
			td.appendChild(img);
			td.onclick = function(volgrp_id,listTBODY,tr,img){
				var display = false;
				return function(){
					//alert(display);
					if(display){
						var volgrpFbvolInfoTR = $("volgrpFbvolInfo-TR" + volgrp_id);
						if(volgrpFbvolInfoTR){
							listTBODY.removeChild(volgrpFbvolInfoTR);
						}
						img.src = ObjectSelf.rootPath + "/img/nolines_plus.gif";
						display = false;
					} else {
						img.src = ObjectSelf.rootPath + "/img/nolines_minus.gif";
						StorageRemoteService.getVolgrpFbvolInfo(ObjectSelf.nodeid, ObjectSelf.type, ObjectSelf.subtype, volgrp_id, {
							callback:function(data){
								ObjectSelf.callbackgetVolgrpFbvolInfo(data,volgrp_id,listTBODY,tr,img);
							}
						})
						display = true;
					}
					
				}
			}(volgrp_id,listTBODY,tr,img)
			
			// Name
			var td = detailDataTDArray[name_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "name";
			td.insertAdjacentHTML("afterBegin", name);
			
			// ID
			var td = detailDataTDArray[volgrp_id_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "volgrp_id";
			td.insertAdjacentHTML("afterBegin", volgrp_id);
			
			// Type
			var td = detailDataTDArray[type_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "type";
			td.insertAdjacentHTML("afterBegin", type);
			
		}
	},
	
	callbackgetVolgrpFbvolInfo: function(data,volgrp_id,listTBODY,tr,img){
//		var volgrpFbvolInfoTR = $("volgrpFbvolInfo-TR");
//		if(volgrpFbvolInfoTR){
//			listTBODY.removeChild(volgrpFbvolInfoTR);
//		}
//		
		var fbvolInfoList = data;
		if(!fbvolInfoList || !fbvolInfoList.length){
			return;
		}
		
		var trIndex = tr.rowIndex;
		var colSpan = tr.cells.length;
		
		volgrpFbvolInfoTR = listTBODY.insertRow(trIndex);
		volgrpFbvolInfoTR.id = "volgrpFbvolInfo-TR" + volgrp_id;
		
		
		var volgrpFbvolInfoTD = this.createElement("", "td");
		volgrpFbvolInfoTR.appendChild(volgrpFbvolInfoTD);
		volgrpFbvolInfoTD.colSpan = colSpan;
		
		var volgrpFbvolInfoTABLE = this.createElement("", "table");
		volgrpFbvolInfoTD.appendChild(volgrpFbvolInfoTABLE);
		
		var volgrpFbvolInfoTBODY = this.createElement("", "tbody");
		volgrpFbvolInfoTABLE.appendChild(volgrpFbvolInfoTBODY);
		
		var length = fbvolInfoList.length;
		var i = 0;
		while(i < length){
			var fbvolInfo = fbvolInfoList[i];
			i++;
			var index = i;									// ���
			var name = fbvolInfo.name;						// Name
			var fbvol_id = fbvolInfo.fbvol_id;				// ID
			var accstate = fbvolInfo.accstate;				// accstate
			var datastate = fbvolInfo.datastate;			// datastate
			var configstate = fbvolInfo.configstate;		// configstate
			var devicemtm = fbvolInfo.devicemtm;			// deviceMTM
			var datatype = fbvolInfo.datatype;				// datatype
			var extpool = fbvolInfo.extpool;				// extpool
			var cap_2_30B = fbvolInfo.cap_2_30B;			// cap (2^30B)
			var cap_10_9B = fbvolInfo.cap_10_9B;			// cap (10^9B)
			var cap_blocks = fbvolInfo.cap_blocks;			// cap (blocks)
			
			var fbvolInfoTR = this.createElement("","tr");
			volgrpFbvolInfoTBODY.appendChild(fbvolInfoTR);
			
			var td = this.createElement("","td");
			td.insertAdjacentHTML("afterBegin", "�߼���" + index);
			td.className = 'detail-data-body-list';
			td.style.textAlign = "right";
			fbvolInfoTR.appendChild(td);
			
			// Name
			var td = this.createElement("","td");
			td.insertAdjacentHTML("afterBegin", name);
			td.className = 'detail-data-body-list';
			td.style.textAlign = "center";
			fbvolInfoTR.appendChild(td);
			
			// ID
			var td = this.createElement("","td");
			td.insertAdjacentHTML("afterBegin", fbvol_id);
			td.className = 'detail-data-body-list';
			td.style.textAlign = "center";
			fbvolInfoTR.appendChild(td);
		}
	},
	
	// ------------			�豸������Ϣ    END					----------------------
	// -------------------------------------------------------------------------------
	
	// -------------------------------------------------------------------------------
	// ------------			�豸 I/O �˿���Ϣ    START				----------------------
	// ��ȡ�豸 I/O �˿���Ϣ
	getIOPortInfo: function (){
		StorageRemoteService.getIOPortInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				ObjectSelf.callbackgetIOPortInfo(data);
			}
		});
	},
	
	// ��ȡ�豸 I/O �˿���Ϣ����Ļص�����
	callbackgetIOPortInfo: function(data){
		this.data = data;
		this.createIOPortInfo();
	},
	
	// ��ȡ�豸 I/O �˿���Ϣ�� �����豸 I/O �˿���Ϣ
	createIOPortInfo: function(){
		var listTBODY = this.detailDataTABLE.tBodies[1];
		if(!listTBODY){
			// �����ϸ��Ϣҳ�����ݱ���� �����б�� TBODY Ϊ null �����´��� (һ�㲻�����)
			this.createDetailDataTABLE();
		}
		// ɾ���б������е�Ԫ��
		this.removeAllChild(listTBODY);
		
		var IOPortInfoList = this.data;
		if(!IOPortInfoList || !IOPortInfoList.length){
			return;
		}
		
		var currTabId = this.currTab.id;
		var index_cellIndex = $(currTabId + "-titleTD-" + "index").cellIndex;
		var ioport_id_cellIndex = $(currTabId + "-titleTD-" + "ioport_id").cellIndex;
		var wwpn_cellIndex = $(currTabId + "-titleTD-" + "wwpn").cellIndex;
		var state_cellIndex = $(currTabId + "-titleTD-" + "state").cellIndex;
		var type_cellIndex = $(currTabId + "-titleTD-" + "type").cellIndex;
		var topo_cellIndex = $(currTabId + "-titleTD-" + "topo").cellIndex;
		var portgrp_cellIndex = $(currTabId + "-titleTD-" + "portgrp").cellIndex;
		
		var length = IOPortInfoList.length;
		var i = 0;
		while(i < length){
			var IOPortInfo = IOPortInfoList[i];
			i++;
			var index = i;									// ���
			var ioport_id = IOPortInfo.ioport_id;			// ID
		 	var wwpn = IOPortInfo.wwpn;						// WWPN
			var state = IOPortInfo.state;					// State
			var type = IOPortInfo.type;						// Type
		 	var topo = IOPortInfo.topo;						// topo
			var portgrp = IOPortInfo.portgrp;				// portgrp
		    
		    var tr = this.createElement(this.currTab.id + "-dataTR-" + index, "tr");
			tr.height = 25;
			listTBODY.appendChild(tr);
			var detailDataTDArray = this.createDetailDataTD(7, tr);
			
			// ���
			var td = detailDataTDArray[index_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "index";
			td.insertAdjacentHTML("afterBegin", index);
			
			// ID
			var td = detailDataTDArray[ioport_id_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "ioport_id";
			td.insertAdjacentHTML("afterBegin", ioport_id);
			
			// WWPN
			var td = detailDataTDArray[wwpn_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "wwpn";
			td.insertAdjacentHTML("afterBegin", wwpn);
			
			// State
			var td = detailDataTDArray[state_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "state";
			td.insertAdjacentHTML("afterBegin", state);
			
			// Type
			var td = detailDataTDArray[type_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "type";
			td.insertAdjacentHTML("afterBegin", type);
			
			// topo
			var td = detailDataTDArray[topo_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "topo";
			td.insertAdjacentHTML("afterBegin", topo);
			
			// portgrp
			var td = detailDataTDArray[portgrp_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "portgrp";
			td.insertAdjacentHTML("afterBegin", portgrp);
			
		}
	},
	
	// ------------			�豸 I/O �˿���Ϣ    END				----------------------
	// -------------------------------------------------------------------------------
	
	// -------------------------------------------------------------------------------
	// ------------			�豸����������Ϣ    START				----------------------
	// ��ȡ�豸����������Ϣ
	getHostConnectInfo: function (){
		StorageRemoteService.getHostConnectInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				ObjectSelf.callbackgetHostConnectInfo(data);
			}
		});
	},
	
	// ��ȡ�豸����������Ϣ����Ļص�����
	callbackgetHostConnectInfo: function(data){
		this.data = data;
		this.createHostConnectInfo();
	},
	
	// ��ȡ�豸����������Ϣ�� �����豸����������Ϣ
	createHostConnectInfo: function(){
		var listTBODY = this.detailDataTABLE.tBodies[1];
		if(!listTBODY){
			// �����ϸ��Ϣҳ�����ݱ���� �����б�� TBODY Ϊ null �����´��� (һ�㲻�����)
			this.createDetailDataTABLE();
		}
		// ɾ���б������е�Ԫ��
		this.removeAllChild(listTBODY);
		
		var hostConnectInfoList = this.data;
		if(!hostConnectInfoList || !hostConnectInfoList.length){
			return;
		}
		
		var currTabId = this.currTab.id;
		var index_cellIndex = $(currTabId + "-titleTD-" + "index").cellIndex;
		var name_cellIndex = $(currTabId + "-titleTD-" + "name").cellIndex;
		var hostconnect_id_cellIndex = $(currTabId + "-titleTD-" + "hostconnect_id").cellIndex;
		var wwpn_cellIndex = $(currTabId + "-titleTD-" + "wwpn").cellIndex;
		var hostType_cellIndex = $(currTabId + "-titleTD-" + "hostType").cellIndex;
		var profile_cellIndex = $(currTabId + "-titleTD-" + "profile").cellIndex;
		var portgrp_cellIndex = $(currTabId + "-titleTD-" + "portgrp").cellIndex;
		var volgrpID_cellIndex = $(currTabId + "-titleTD-" + "volgrpID").cellIndex;
		var essIOport_cellIndex = $(currTabId + "-titleTD-" + "essIOport").cellIndex;
		
		var length = hostConnectInfoList.length;
		var i = 0;
		while(i < length){
			var hostConnectInfo = hostConnectInfoList[i];
			i++;
			var index = i;											// ���
			var name = hostConnectInfo.name;						// Name
		 	var hostconnect_id = hostConnectInfo.hostconnect_id;	// ID	
			var wwpn = hostConnectInfo.wwpn;						// WWPN
			var hostType = hostConnectInfo.hostType;				// HostType
		 	var profile = hostConnectInfo.profile;					// Profile
			var portgrp = hostConnectInfo.portgrp;					// portgrp
			var volgrpID = hostConnectInfo.volgrpID;				// volgrpID
			var essIOport = hostConnectInfo.essIOport;				// ESSIOport
		    
		    var tr = this.createElement(this.currTab.id + "-dataTR-" + index, "tr");
			tr.height = 25;
			listTBODY.appendChild(tr);
			var detailDataTDArray = this.createDetailDataTD(9, tr);
			
			// ���
			var td = detailDataTDArray[index_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "index";
			td.insertAdjacentHTML("afterBegin", index);
			
			// Name
			var td = detailDataTDArray[name_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "name";
			td.insertAdjacentHTML("afterBegin", name);
			
			// ID
			var td = detailDataTDArray[hostconnect_id_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "hostconnect_id";
			td.insertAdjacentHTML("afterBegin", hostconnect_id);
			
			// WWPN
			var td = detailDataTDArray[wwpn_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "wwpn";
			td.insertAdjacentHTML("afterBegin", wwpn);
			
			// HostType
			var td = detailDataTDArray[hostType_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "hostType";
			td.insertAdjacentHTML("afterBegin", hostType);
			
			// Profile
			var td = detailDataTDArray[profile_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "profile";
			td.insertAdjacentHTML("afterBegin", profile);
			
			// portgrp
			var td = detailDataTDArray[portgrp_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "portgrp";
			td.insertAdjacentHTML("afterBegin", portgrp);
			
			// volgrpID
			var td = detailDataTDArray[volgrpID_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "volgrpID";
			td.insertAdjacentHTML("afterBegin", volgrpID);
			
			// ESSIOport
			var td = detailDataTDArray[essIOport_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "essIOport";
			td.insertAdjacentHTML("afterBegin", essIOport);
			
		}
	},
	
	// ------------			�豸����������Ϣ    END				----------------------
	// -------------------------------------------------------------------------------
	
	
	// -------------------------------------------------------------------------------
	// ------------			����Ϊ���� tab ҳ����Ҫ����Ϣ			----------------------
	// -------------------------------------------------------------------------------
	
	// ���� SWF �����뵽 table �У�Ȼ�󽫸� table ���뵽���� SWFContain �ڣ����ظ� table
	// SWFContain       -- SWF ���ļ���������������Ҫ�����ȷ��� table �� Ȼ����� SWF �� 
	// ע�⣺�����������Ѿ���ʾ��ҳ�����뵽ҳ���У�������ʾ SWF ���׳� �Ҳ��������쳣
	// SWFPath          -- SWF ���ļ�·������·��Ӧ�ð����� SWF ���ļ�����Ĳ���
	// name             -- SWF ���ļ����ƣ������ƽ����� SWF �ļ�����ҳ��� div �� id������ҪΨһ;
	// width            -- SWF ���ļ���ȣ�
	// height           -- SWF ���ļ��汾�����ŵ�falsh����İ汾��
	// backgroundclor   -- SWF ���ļ���ɫ��
	createSWFTABLE: function(SWFContain, SWFPath, name, width, height, version, backgroundclor){
		var SWFTABLE = this.createElement("", "table");
		SWFTABLE.style.textAlign = "center";
		SWFContain.appendChild(SWFTABLE);

		var tbody = this.createElement("", "tbody");
		SWFTABLE.appendChild(tbody);

		var tr = this.createElement("", "tr");
		tbody.appendChild(tr);

		var td = this.createElement("", "td");
		tr.appendChild(td);

		var SWFDIV = this.createElement(name, "div");
		td.appendChild(SWFDIV);
		
		SWFDIV.insertAdjacentHTML("afterBegin", "<strong>�밲װ�汾8���ϵ� Flash ���</strong>");
		
		var so = new SWFObject(SWFPath, name, width, height, version, backgroundclor);
		so.write(name);

		return SWFTABLE;
	},
	
	
	// ��ȡ�豸����Ϣ�� ���豸��Ϣ�����ֶ�����
	// titleTD -- ����� td
	sortDataTR: function(titleTD){
		var dataTRRows = this.detailDataTABLE.tBodies[1].rows;
		var dataTRArray = new Array();
		var title_cellIndex = titleTD.cellIndex;
		for(var i = 0; i < dataTRRows.length; i++){
			dataTRArray.push(dataTRRows[i]);
		}
		
		if( !this.dataTROrder || this.dataTROrder == "desc"){
			// �����ǰ ����˳�� this.dataTROrder Ϊ null  
			// ����ǰ ����˳�� this.dataTROrder Ϊ desc (�ݼ�) �򽫸�Ϊ asc (����)
			this.dataTROrder = "asc";
		} else if (this.dataTROrder == "asc"){
			// �����ǰ ����˳�� this.dataTROrder Ϊ asc (����) �򽫸�Ϊ desc (�ݼ�)
			this.dataTROrder = "desc";
		}
		
		var new_dataTRArray = dataTRArray.sort(function(previousDataTR, dataTR){
			
			var previousDataTR_value = previousDataTR.cells(title_cellIndex).innerText;
			var dataTR_value = dataTR.cells(title_cellIndex).innerText;
			
			var previousDataTR_value_int = parseInt(previousDataTR_value);
			if(isNaN(previousDataTR_value_int)){
				previousDataTR_value_int = 0;
			}
			
			var dataTR_value_int = parseInt(dataTR_value);
			if(isNaN(dataTR_value_int)){
				dataTR_value_int = 0;
			}
			
			if(!ObjectSelf.dataTROrder || ObjectSelf.dataTROrder == "asc"){
				// �����ǰ ����˳�� this.dataTROrder Ϊ null  
				// ����ǰ ����˳�� this.dataTROrder Ϊ asc Ϊ (����)
				return previousDataTR_value_int - dataTR_value_int;
			} else if (ObjectSelf.dataTROrder == "desc"){
				// �����ǰ ����˳�� this.dataTROrder Ϊ desc (�ݼ�)
				return dataTR_value_int - previousDataTR_value_int;
			}
		});
		this.removeAllChild(this.detailDataTABLE.tBodies[1]);
		
		for(var i = 0; i < new_dataTRArray.length; i++){
			this.detailDataTABLE.tBodies[1].appendChild(new_dataTRArray[i]);
		}
	}
}

function useLoadingMessage(message) { 
	var loadingMessage; 
	if (message) loadingMessage = message; 
	else loadingMessage = "Loading"; 
	DWREngine.setPreHook(function() {
		var disabledZone = $('disabledZone'); 
		if (!disabledZone) { 
			disabledZone = document.createElement('div'); 
			disabledZone.setAttribute('id', 'disabledZone'); 
			disabledZone.style.position = "absolute"; 
			disabledZone.style.zIndex = "1000"; 
			disabledZone.style.left = "0px"; 
			disabledZone.style.top = "0px"; 
			disabledZone.style.width = "100%"; 
			disabledZone.style.height = "100%"; 
			document.body.appendChild(disabledZone); 
			var messageZone = document.createElement('div'); 
			messageZone.setAttribute('id', 'messageZone'); 
			messageZone.style.position = "absolute"; 
			messageZone.style.top = "0px"; 
			messageZone.style.right = "0px"; 
			messageZone.style.background = "red"; 
			messageZone.style.color = "white"; 
			messageZone.style.fontFamily = "Arial,Helvetica,sans-serif"; 
			messageZone.style.padding = "4px"; 
			disabledZone.appendChild(messageZone); 
			var text = document.createTextNode(loadingMessage); 
			messageZone.appendChild(text); 
		} else { 
			$('messageZone').innerHTML = loadingMessage; 
			disabledZone.style.visibility = 'visible'; 
		} 
	}); 
	DWREngine.setPostHook(function() { 
		$('disabledZone').style.visibility = 'hidden';
	}); 
}