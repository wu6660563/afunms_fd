/*******************************************************************************
 * netdetail.js
 * 
 * @Author: ����
 * 
 * @Date: 2010-12-10
 * 
 * @Function:
 * �� js ����Ҫ��������������豸��ϸ��Ϣҳ�������չʾ��
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
	var netDetail= new NetDetail(nodeid, type, subtype);
	netDetail.show();
}


function NetDetail(nodeid, type, subtype){ 
	
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
	
	// ϵͳ��Ϣ���� CPU SWF �ļ���
	this.DHCCGaugeSWFName = "DHCCGauge";
	
	// ������Ϣ���� ��Ӧʱ�� Response_time SWF �ļ���
	this.responseTimeSWFName = "Response_time";
	
	// ������Ϣ���� ��ͨ�� Ping SWF �ļ���
	this.pingSWFName = "Ping";
	
	// ������Ϣ���� CPU Line_CPU SWF �ļ���
	this.Line_CPUSWFName = "Line_CPU";
	
	// ������Ϣ���� ���� Area_flux SWF �ļ���
	this.Area_fluxSWFName = "Area_flux";
	
	// ������Ϣ���� �ڴ� Net_Memory SWF �ļ���
	this.Net_MemorySWFName = "Net_Memory";
	
	// ������Ϣ���� ���� Net_flash_Memory SWF �ļ���
	this.Net_flash_MemorySWFName = "Net_flash_Memory";
	
	// ����������Ϣ�������� SWF �ļ�����һ�� Envoriment_component SWF �ļ�����ֻ�ǲ�����ͬ
	this.Envoriment_componentSWFName = "Envoriment_component";
	
	// ����������Ϣ���� ��Դ SWF �ļ� �ؼ��� power
	this.Envoriment_componentPowerSWFkey = "power";
	
	// ����������Ϣ���� ��ѹ SWF �ļ� �ؼ��� vol
	this.Envoriment_componentVolSWFkey = "vol";
	
	// ����������Ϣ���� ���� SWF �ļ� �ؼ��� fan
	this.Envoriment_componentFanSWFkey = "fan";
	
	// ����������Ϣ���� �¶� SWF �ļ� �ؼ��� temper
	this.Envoriment_componentTemperSWFkey = "temperature";
	
	this.Envoriment_componentTemperSWFTable = "temper";
	
	// ��ϸ��Ϣ�� �����б��� ���е�˳�� 
	// ���� -- asc
	// �ݼ� -- desc
	this.dataTROrder = null;
	
	// �Ӻ�̨�첽��ȡ������
	this.data = null;
		
	// NetDetail �౾�� �ñ���Ϊ�����ȫ�ֱ������ڻص���תʱ����
	ObjectSelf = this;
	
	
}

NetDetail.prototype = {
	
	
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
				NetRemoteService.getHostNodeInfo(ObjectSelf.nodeid, ObjectSelf.type, ObjectSelf.subtype, {
					callback:function(data){
						ObjectSelf.hostNode =  data;
						ObjectSelf.getSystemInfo();
					}
				});
			}
		}
		
		NetRemoteService.getHostNodeInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				ObjectSelf.hostNode =  data;
				ObjectSelf.getSystemInfo();
			}
		});
		
		this.getTabInfo();
		
	},
	
	// ��ȡ�豸ϵͳ��Ϣ
	getSystemInfo: function (){
		NetRemoteService.getSystemInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				ObjectSelf.callbackgetSystemInfo(data);
			}
		});
	},
	
	// ��ȡ�豸ϵͳ��Ϣ����Ļص�����
	callbackgetSystemInfo: function (data){ 
		this.data = data;
		this.createSystemInfo();
	},
	
	// ��ȡ�豸������Ϣ�� �����豸������Ϣ
	createSystemInfo: function (){ 
		
		var systemInfoData = this.data;
		
		var sysDescr = "";
		var sysUpTime = "";
		var MacAddr = "";
		var sysCollectTime = "";
		for(var i = 0; i < systemInfoData.length; i++){
			var systemInfo = systemInfoData[i];
			if( systemInfo.sindex && "sysDescr" == systemInfo.sindex){
					sysDescr = systemInfo.thevalue;
			} else if( systemInfo.sindex && "sysUpTime" == systemInfo.sindex){
					sysUpTime = systemInfo.thevalue;
					sysCollectTime = systemInfo.collecttime;
			} else if( systemInfo.sindex && "MacAddr" == systemInfo.sindex){
					MacAddr = systemInfo.thevalue;
			}
		}
		
		NetRemoteService.getCurrDayPingAvgInfo(this.nodeid, this.type, this.subtype, {
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
		
		NetRemoteService.getCurrCpuAvgInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				var currCpuAvgInfo_str = data;
				var currCpuAvgInfo_float = parseFloat(currCpuAvgInfo_str);
				if(isNaN(currCpuAvgInfo_float)){
					currCpuAvgInfo_float = 0;
				}
				var DHCCGaugeSWFPath = ObjectSelf.SWFPath + ObjectSelf.DHCCGaugeSWFName + ".swf?" +
				"percent=" + new String(currCpuAvgInfo_float);
				
				var currCpuAvgInfoTD = $('currCpuAvgInfo');
				ObjectSelf.removeAllChild(currCpuAvgInfoTD);
				ObjectSelf.createSWFTABLE(currCpuAvgInfoTD, DHCCGaugeSWFPath, ObjectSelf.DHCCGaugeSWFName, "160", "160", "8", "#ffffff");
			}
		});
		NetRemoteService.getStautsInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				var statusImg = "";
				if(data){
					statusImg = data;
				}
				$('systemInfo_stauts').innerHTML = "<img src=\"" + ObjectSelf.rootPath + "/resource/" + statusImg+ "\">";
			}
		});
		NetRemoteService.getCategoryInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				var category = "";
				if(data){
					category = data;
				}
				$('systemInfo_category').innerHTML = category;
			}
		});
		
		NetRemoteService.getSupperInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				var supperInfo = "";
				if(data){
					supperInfo = data;
				}
				//alert(supperInfo);
				$('systemInfo_supper').innerHTML = supperInfo;
			}
		});
		
		$('systemInfo_alias').innerHTML = this.hostNode.alias;
		$('systemInfo_sysName').innerHTML = this.hostNode.sysName;
		$('systemInfo_ipaddress').innerHTML = this.hostNode.ipAddress;
		$('systemInfo_netMask').innerHTML = this.hostNode.netMask;
		$('systemInfo_type').innerHTML = this.hostNode.type;
		$('systemInfo_sysdescr').innerHTML = sysDescr;
		$('systemInfo_sysuptime').innerHTML = sysUpTime;
		$('systemInfo_collecttime').innerHTML = sysCollectTime;
		$('systemInfo_mac').innerHTML = MacAddr;
		$('systemInfo_sysOid').innerHTML = this.hostNode.sysOid;
		
	},
	
	// ��ȡ��ϸ��Ϣҳ���ϱ�ǩҳ����Ϣ
	getTabInfo: function (){
		NetRemoteService.getTabInfo(this.nodeid, this.type, this.subtype, {
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
	
	// ��������ǩҳ�ĺ���
	setCurrTab: function (tab){
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
	// ------------			�豸������Ϣ    START					----------------------
	// ��ȡ�豸������Ϣ
	getInterfaceInfo: function(){
		NetRemoteService.getInterfaceInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				ObjectSelf.callbackgetInterfaceInfo(data);
			}
		});
	},
	
	// ��ȡ�豸������Ϣ����Ļص�����
	callbackgetInterfaceInfo: function(data){
		this.data = data;
		this.createInterfaceInfo();
	},
	
	// ��ȡ�豸������Ϣ�� �����豸������Ϣ
	createInterfaceInfo: function(){
		var listTBODY = this.detailDataTABLE.tBodies[1];
		if(!listTBODY){
			// �����ϸ��Ϣҳ�����ݱ���� �����б�� TBODY Ϊ null �����´��� (һ�㲻������������)
			this.createDetailDataTABLE();
		}
		// ɾ���б������е�Ԫ��
		this.removeAllChild(listTBODY);
		
		var interfaceInfoList = this.data;
		var length = interfaceInfoList.length;
		if(!interfaceInfoList || interfaceInfoList.length == 0){
			return;
		}
		var currTabId = this.currTab.id;
		var index_cellIndex = $(currTabId + "-titleTD-" + "index").cellIndex;
		var ifDescr_cellIndex = $(currTabId + "-titleTD-" + "ifDescr").cellIndex;
		var ifSpeed_cellIndex = $(currTabId + "-titleTD-" + "ifSpeed").cellIndex;
		var ifOperStatus_cellIndex = $(currTabId + "-titleTD-" + "ifOperStatus").cellIndex;
		var ifOutBroadcastPkts_cellIndex = $(currTabId + "-titleTD-" + "ifOutBroadcastPkts").cellIndex;
		var ifInBroadcastPkts_cellIndex = $(currTabId + "-titleTD-" + "ifInBroadcastPkts").cellIndex;
		var ifOutMulticastPkts_cellIndex= $(currTabId + "-titleTD-" + "ifOutMulticastPkts").cellIndex;
		var ifInMulticastPkts_cellIndex = $(currTabId + "-titleTD-" + "ifInMulticastPkts").cellIndex;
		var OutBandwidthUtilHdx_titleTD = $(currTabId + "-titleTD-" + "OutBandwidthUtilHdx");
		var OutBandwidthUtilHdx_cellIndex = OutBandwidthUtilHdx_titleTD.cellIndex;
		var InBandwidthUtilHdx_titleTD = $(currTabId + "-titleTD-" + "InBandwidthUtilHdx");
		var InBandwidthUtilHdx_cellIndex = InBandwidthUtilHdx_titleTD.cellIndex;
		var showDetail_cellIndex = $(currTabId + "-titleTD-" + "showDetail").cellIndex;
		
		OutBandwidthUtilHdx_titleTD.style.cursor = "hand";
		OutBandwidthUtilHdx_titleTD.onclick = function(){
			// ��� ������� �¼�
			ObjectSelf.sortDataTR(this);
		};
		
		InBandwidthUtilHdx_titleTD.style.cursor = "hand";
		InBandwidthUtilHdx_titleTD.onclick = function(){
			// ��� ������� �¼�
			ObjectSelf.sortDataTR(this);
		}
		var i = 0;
		while(i < length){
			var interfaceInfo = interfaceInfoList[i];
			i++;
			var index = i;													// ���
			var sindex = interfaceInfo.sindex;								// ����
			var ifDescr = interfaceInfo.ifDescr;							// ����
			var ifSpeed = interfaceInfo.ifSpeed;							// ÿ���ֽ���(M)
			var ifOperStatus = interfaceInfo.ifOperStatus;					// ״̬
			var ifOutBroadcastPkts = interfaceInfo.ifOutBroadcastPkts;		// ���ڹ㲥���ݰ�
			var ifInBroadcastPkts = interfaceInfo.ifInBroadcastPkts;		// ��ڹ㲥���ݰ�
			var ifOutMulticastPkts = interfaceInfo.ifOutMulticastPkts;		// ���ڶಥ���ݰ�
			var ifInMulticastPkts = interfaceInfo.ifInMulticastPkts;		// ��ڶಥ���ݰ�
			var OutBandwidthUtilHdx = interfaceInfo.outBandwidthUtilHdx;	// ��������
			var InBandwidthUtilHdx = interfaceInfo.inBandwidthUtilHdx;		// �������		
			
			var tr = this.createElement(this.currTab.id + "-dataTR-" + index, "tr");
			tr.height = 25;
			listTBODY.appendChild(tr);
			var detailDataTDArray = this.createDetailDataTD(11, tr);
			
			// ����
			var td = detailDataTDArray[index_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "sindex";
			td.insertAdjacentHTML("afterBegin", sindex);
			
			// ����
			var td = detailDataTDArray[ifDescr_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "ifDescr";
			td.insertAdjacentHTML("afterBegin", ifDescr);
			
			// ÿ���ֽ���(M)
			var td = detailDataTDArray[ifSpeed_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "ifSpeed";
			td.insertAdjacentHTML("afterBegin", ifSpeed);
			
			// ״̬
			var td = detailDataTDArray[ifOperStatus_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "ifOperStatus";
			td.insertAdjacentHTML("afterBegin", ifOperStatus);
			
			// ���ڹ㲥���ݰ�
			var td = detailDataTDArray[ifOutBroadcastPkts_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "ifOutBroadcastPkts";
			td.insertAdjacentHTML("afterBegin", ifOutBroadcastPkts);
			
			// ��ڹ㲥���ݰ�
			var td = detailDataTDArray[ifInBroadcastPkts_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "ifInBroadcastPkts";
			td.insertAdjacentHTML("afterBegin", ifInBroadcastPkts);
			
			// ���ڶಥ���ݰ�
			var td = detailDataTDArray[ifOutMulticastPkts_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "ifOutMulticastPkts";
			td.insertAdjacentHTML("afterBegin", ifOutMulticastPkts);
			
			// ��ڶಥ���ݰ�
			var td = detailDataTDArray[ifInMulticastPkts_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "ifInMulticastPkts";
			td.insertAdjacentHTML("afterBegin", ifInMulticastPkts);
			
			// ��������
			var td = detailDataTDArray[OutBandwidthUtilHdx_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "OutBandwidthUtilHdx";
			td.insertAdjacentHTML("afterBegin", OutBandwidthUtilHdx);
			
			// �������
			var td = detailDataTDArray[InBandwidthUtilHdx_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "InBandwidthUtilHdx";
			td.insertAdjacentHTML("afterBegin", InBandwidthUtilHdx);
			
			// �鿴����
			var td = detailDataTDArray[showDetail_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "showDetail";
			var img = this.createElement("", "img");
			td.appendChild(img);
			img.src = this.rootPath + "/resource/image/status.gif";
			img.oncontextmenu = function(sindex, ifDescr){
					return function(){
						ObjectSelf.showInterfaceInfoMenu(sindex, ifDescr);
					};
				}(sindex, ifDescr);
		}
		
		
	},
	
	// ��ȡ�豸������Ϣ�� �Ҽ�����
	// rowControlString			-- �п����ַ�����0��ʾ����ʾ��1��ʾ��ʾ���硰101�������ʾ��1��3����ʾ����2�в���ʾ ��ʱд��
	// InterfaceInfoIndex 		-- �˿�����
	// InterfaceInfoIfDescr 	-- �˿�����
	showInterfaceInfoMenu: function(InterfaceInfoIndex, InterfaceInfoIfDescr){
		var showInterfaceInfoDetail = $('showInterfaceInfoDetail');
		if(showInterfaceInfoDetail){
			showInterfaceInfoDetail.onclick = function(){
				window.open(ObjectSelf.rootPath + "/monitor.do?action=show_utilhdx&" +
						"id=" + ObjectSelf.nodeid + 
						"&ipaddress=" + ObjectSelf.hostNode.ipAddress + 
						"&ifindex=" + InterfaceInfoIndex + "&ifname=" + InterfaceInfoIfDescr, 
						"newwindow", "height=350, width=840, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes");
			};
		}
		var showInterfaceInfoportset = $('showInterfaceInfoportset');
		if(showInterfaceInfoportset){
			$('showInterfaceInfoportset').onclick = function(){
				window.open(ObjectSelf.rootPath + "/panel.do?action=show_portreset&" +
						"id=" + ObjectSelf.nodeid + 
						"&ipaddress=" + ObjectSelf.hostNode.ipAddress + 
						"&ifindex=" + InterfaceInfoIndex + "&ifname="+ InterfaceInfoIfDescr, 
						"newwindow", "height=200, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes");
			};
		}
		popMenu("InterfaceInfo_itemMenu", 100, "11");
		return false;
	},
	
	// ------------			�豸������Ϣ    END					----------------------
	// -------------------------------------------------------------------------------
	
	// -------------------------------------------------------------------------------
	// ------------			�豸������Ϣ    START					----------------------
	// ��ȡ�豸������Ϣ
	getPerformaceInfo: function(){
		NetRemoteService.getHostNodeInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				ObjectSelf.callbackgetPerformaceInfo(data);
			}
		});
	},
	
	// ��ȡ�豸������Ϣ����Ļص�����
	callbackgetPerformaceInfo: function(data){
		this.hostNode = data;
		this.createPerformaceInfo();
	},
	
	// ��ȡ�豸������Ϣ�� �����豸������Ϣ
	createPerformaceInfo: function(){
		if(!this.detailDataTABLE){
			// �����ϸ��Ϣҳ�����ݵı��Ϊ null
			this.detailDataTABLE = this.createElement("detailDataTABLE", "table");
			this.detailDataDIV.appendChild(this.detailDataTABLE);
		}

		var tbody = this.createElement("", "tbody");
		this.detailDataTABLE.appendChild(tbody);

		var tr = this.createElement("", "tr");
		tbody.appendChild(tr);
		
		var td = this.createElement("", "td");
		tr.appendChild(td);
		// ���� responseTime ���� table
		var responseTimePath = this.SWFPath + this.responseTimeSWFName + ".swf?ipadress=" + this.hostNode.ipAddress; 
		var responseTimeSWFTABLE = this.createSWFTABLE(td, responseTimePath, this.responseTimeSWFName, "346", "250", this.SWFVersion, this.SWFbackgroundclor);

		var td = this.createElement("", "td");
		tr.appendChild(td);
		// ���� Ping ���� table
		var pingSWFPath = this.SWFPath + this.pingSWFName + ".swf?ipadress=" + this.hostNode.ipAddress; 
		var pingSWFTABLE = this.createSWFTABLE(td, pingSWFPath, this.pingSWFName, "346", "250", this.SWFVersion, this.SWFbackgroundclor);
		
		if(this.hostNode.collecttype == 3 || this.hostNode.collecttype ==4 
		|| this.hostNode.collecttype == 8 || this.hostNode.collecttype == 9){
			return;			
		}
		
		// -------------------��һ��-------------------------
		var tr = this.createElement("", "tr");
		tbody.appendChild(tr);
		
		var td = this.createElement("", "td");
		tr.appendChild(td);
		// ���� Line_CPU ���� table
		var Line_CPUPath = this.SWFPath + this.Line_CPUSWFName + ".swf?ipadress=" + this.hostNode.ipAddress; 
		var Line_CPUSWFTABLE = this.createSWFTABLE(td, Line_CPUPath, this.Line_CPUSWFName, "346", "250", this.SWFVersion, this.SWFbackgroundclor);
		
		var td = this.createElement("", "td");
		tr.appendChild(td);
		// ���� Area_flux ���� table
		var Area_fluxSWFPath = this.SWFPath + this.Area_fluxSWFName + ".swf?ipadress=" + this.hostNode.ipAddress; 
		var Area_fluxSWFTABLE = this.createSWFTABLE(td, Area_fluxSWFPath, this.Area_fluxSWFName, "346", "250", this.SWFVersion, this.SWFbackgroundclor);
		
		// -------------------��һ��-------------------------
		var tr = this.createElement("", "tr");
		tbody.appendChild(tr);
		
		var td = this.createElement("", "td");
		tr.appendChild(td);
		// ���� Net_Memory ���� table
		var Net_MemoryPath = this.SWFPath + this.Net_MemorySWFName + ".swf?ipadress=" + this.hostNode.ipAddress; 
		var Net_MemorySWFTABLE = this.createSWFTABLE(td, Net_MemoryPath, this.Net_MemorySWFName, "346", "250", this.SWFVersion, this.SWFbackgroundclor);
		
		var td = this.createElement("", "td");
		tr.appendChild(td);
		// ���� Net_flash_Memory ���� table
		var Net_flash_MemorySWFPath = this.SWFPath + this.Net_flash_MemorySWFName + ".swf?ipadress=" + this.hostNode.ipAddress; 
		var Net_flash_MemorySWFTABLE = this.createSWFTABLE(td, Net_flash_MemorySWFPath, this.Net_flash_MemorySWFName, "346", "250", this.SWFVersion, this.SWFbackgroundclor);
		
		
	},
	
	// ------------			�豸������Ϣ    END					----------------------
	// -------------------------------------------------------------------------------
	
	
	// -------------------------------------------------------------------------------
	// ------------			�豸ARP��Ϣ    START					----------------------
	// ��ȡ�豸 ARP ��Ϣ
	getARPInfo: function (){
		NetRemoteService.getARPInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				ObjectSelf.callbackgetARPInfo(data);
			}
		});
	},
	
	// ��ȡ�豸 ARP ��Ϣ����Ļص�����
	callbackgetARPInfo: function(data){
		this.data = data;
		this.createARPInfo();
	},
	
	// ��ȡ�豸 ARP ��Ϣ�� �����豸 ARP ��Ϣ
	createARPInfo: function(){
		var listTBODY = this.detailDataTABLE.tBodies[1];
		if(!listTBODY){
			// �����ϸ��Ϣҳ�����ݱ���� �����б�� TBODY Ϊ null �����´��� (һ�㲻�����)
			this.createDetailDataTABLE();
		}
		// ɾ���б������е�Ԫ��
		this.removeAllChild(listTBODY);
		
		var ipMacInfoList = this.data;
		var length = ipMacInfoList.length;
		if(!ipMacInfoList || length == 0){
			return;
		}
		
		var currTabId = this.currTab.id;
		var index_cellIndex = $(currTabId + "-titleTD-" + "index").cellIndex;
		var ifindex_cellIndex = $(currTabId + "-titleTD-" + "ifindex").cellIndex;
		var ipaddress_cellIndex = $(currTabId + "-titleTD-" + "ipaddress").cellIndex;
		var mac_cellIndex = $(currTabId + "-titleTD-" + "mac").cellIndex;
		var collecttime_cellIndex = $(currTabId + "-titleTD-" + "collecttime").cellIndex;
		var ifband_cellIndex = $(currTabId + "-titleTD-" + "ifband").cellIndex;
		
		var i = 0;
		while(i < length){
			var ipMacInfo = ipMacInfoList[i];
			i++;
			var index = i;
		    var id = ipMacInfo.id;							// id
		    var relateipaddr = ipMacInfo.relateipaddr;		// �����豸 ip
		    var ifindex = ipMacInfo.ifindex;				// ����
		    var ipaddress = ipMacInfo.ipaddress;			// ipaddress
		    var mac = ipMacInfo.mac;						// MAC
		    var collecttime = ipMacInfo.collecttime;		// ɨ��ʱ��
		    var ifband = ipMacInfo.ifband;					// ����
		    var ifsms = ipMacInfo.ifsms;					// ifsms
		    var bak = ipMacInfo.bak;						// bak
		    
		    var tr = this.createElement(this.currTab.id + "-dataTR-" + index, "tr");
			tr.height = 25;
			listTBODY.appendChild(tr);
			var detailDataTDArray = this.createDetailDataTD(7, tr);
			
			// ���
			var td = detailDataTDArray[index_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "index";
			td.insertAdjacentHTML("afterBegin", index);
			
			// ����
			var td = detailDataTDArray[ifindex_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "ifindex";
			td.insertAdjacentHTML("afterBegin", ifindex);
			
			// ipaddress
			var td = detailDataTDArray[ipaddress_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "ipaddress";
			td.insertAdjacentHTML("afterBegin", ipaddress);
			
			// MAC
			var td = detailDataTDArray[mac_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "mac";
			td.insertAdjacentHTML("afterBegin", mac);
			
			// ɨ��ʱ��
			var recordtime = new Date(collecttime);
			var td = detailDataTDArray[collecttime_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "collecttime";
			td.insertAdjacentHTML("afterBegin", recordtime.getFullYear() + '-' + (recordtime.getMonth() + 1) + '-' + recordtime.getDate()+ 
				' ' + recordtime.getHours() + ':' + recordtime.getMinutes() + ':' + recordtime.getSeconds());
		}
	},
	
	// ------------			�豸ARP��Ϣ    END					----------------------
	// -------------------------------------------------------------------------------
	
	// -------------------------------------------------------------------------------
	// ------------			�豸FDB��Ϣ    START					----------------------
	// ��ȡ�豸 FDB ��Ϣ
	getFDBInfo: function (){
		NetRemoteService.getFDBInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				ObjectSelf.callbackgetFDBInfo(data);
			}
		});
	},
	
	// ��ȡ�豸 FDB ��Ϣ����Ļص�����
	callbackgetFDBInfo: function(data){
		this.data = data;
		this.createFDBInfo();
	},
	
	// ��ȡ�豸 FDB ��Ϣ�� �����豸 FDB ��Ϣ
	createFDBInfo: function(){
		var listTBODY = this.detailDataTABLE.tBodies[1];
		if(!listTBODY){
			// �����ϸ��Ϣҳ�����ݱ���� �����б�� TBODY Ϊ null �����´��� (һ�㲻�����)
			this.createDetailDataTABLE();
		}
		// ɾ���б������е�Ԫ��
		this.removeAllChild(listTBODY);
		
		var fdbInfoList = this.data;
		var length = fdbInfoList.length;
		if(!fdbInfoList || length == 0){
			return;
		}
		
		var currTabId = this.currTab.id;
		var index_cellIndex = $(currTabId + "-titleTD-" + "index").cellIndex;
		var ifindex_cellIndex = $(currTabId + "-titleTD-" + "ifindex").cellIndex;
		var ipaddress_cellIndex = $(currTabId + "-titleTD-" + "ipaddress").cellIndex;
		var mac_cellIndex = $(currTabId + "-titleTD-" + "mac").cellIndex;
		var collecttime_cellIndex = $(currTabId + "-titleTD-" + "collecttime").cellIndex;
		
		var i = 0;
		while(i < length){
			var fdbInfo = fdbInfoList[i];
			i++;
			//var id = FdbNodeTemp.id;						// ID
			var index = i;									// ���
			var ifindex = fdbInfo.ifindex;					// ����
			var ipaddress = fdbInfo.ipaddress;				// ipaddress
			var mac = fdbInfo.mac;							// mac
			var collecttime = fdbInfo.collecttime;			// �ɼ�ʱ��
			
			var tr = this.createElement(this.currTab.id + "-dataTR-" + index, "tr");
			tr.height = 25;
			listTBODY.appendChild(tr);
			var detailDataTDArray = this.createDetailDataTD(9, tr);
			
			// ���
			var td = detailDataTDArray[index_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "index";
			td.insertAdjacentHTML("afterBegin", index);
			
			// ����
			var td = detailDataTDArray[ifindex_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "ifindex";
			td.insertAdjacentHTML("afterBegin", ifindex);
			
			// ipaddress
			var td = detailDataTDArray[ipaddress_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "ipaddress";
			td.insertAdjacentHTML("afterBegin", ipaddress);
			
			// MAC
			var td = detailDataTDArray[mac_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "mac";
			td.insertAdjacentHTML("afterBegin", mac);
			
			// �ɼ�ʱ��
			//var recordtime = new Date(collecttime);
			var td = detailDataTDArray[collecttime_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "collecttime";
			td.insertAdjacentHTML("afterBegin", collecttime);
			
		}
		
	},
	
	// ------------			�豸FDB��Ϣ    END					----------------------
	// -------------------------------------------------------------------------------
	
	// -------------------------------------------------------------------------------
	// ------------			�豸·����Ϣ    START					----------------------
	// ��ȡ�豸·����Ϣ
	getRouterInfo: function (){
		NetRemoteService.getRouterInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				ObjectSelf.callbackgetRouterInfo(data);
			}
		});
	},
	
	// ��ȡ�豸·����Ϣ����Ļص�����
	callbackgetRouterInfo: function(data){
		this.data = data;
		this.createRouterInfo();
	},
	
	// ��ȡ�豸·����Ϣ�� �����豸·����Ϣ
	createRouterInfo: function(){
		var listTBODY = this.detailDataTABLE.tBodies[1];
		if(!listTBODY){
			// �����ϸ��Ϣҳ�����ݱ���� �����б�� TBODY Ϊ null �����´��� (һ�㲻�����)
			this.createDetailDataTABLE();
		}
		// ɾ���б������е�Ԫ��
		this.removeAllChild(listTBODY);
		
		var routerInfoList = this.data;
		var length = routerInfoList.length;
		if(!routerInfoList || length == 0){
			return;
		}
		var currTabId = this.currTab.id;
		var ifindex_cellIndex = $(currTabId + "-titleTD-" + "ifindex").cellIndex;
		var dest_cellIndex = $(currTabId + "-titleTD-" + "dest").cellIndex;
		var nexthop_cellIndex = $(currTabId + "-titleTD-" + "nexthop").cellIndex;
		var rtype_cellIndex = $(currTabId + "-titleTD-" + "rtype").cellIndex;
		var proto_cellIndex = $(currTabId + "-titleTD-" + "proto").cellIndex;
		var mask_cellIndex = $(currTabId + "-titleTD-" + "mask").cellIndex;
		
		var i = 0;
		while(i < length){
			var routerInfo = routerInfoList[i];
			i++;
			var index = i;									// ���
			var nodeid = routerInfo.nodeid;					// �豸id
			var ip = routerInfo.ip;							// �豸ip
			var type = routerInfo.type;						// �豸����
			var subtype = routerInfo.subtype;				// �豸������
			var ifindex = routerInfo.ifindex;				// ����
			var nexthop = routerInfo.nexthop;				// ��һ��
			var proto = routerInfo.proto;					// ·��Э��
			var rtype = routerInfo.rtype;					// ·������
			var mask = routerInfo.mask;						// ��������
			var dest = routerInfo.dest;						// Ŀ���ַ
			var collecttime = routerInfo.collecttime;		// �ɼ�ʱ��
			
			var tr = this.createElement("", "tr");
			tr.height = 25;
			listTBODY.appendChild(tr);
			var detailDataTDArray = this.createDetailDataTD(6, tr);
			
			// ����
			var td = detailDataTDArray[ifindex_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "ifindex";
			td.insertAdjacentHTML("afterBegin", ifindex);
			
			// Ŀ���ַ
			var td = detailDataTDArray[dest_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "dest";
			td.insertAdjacentHTML("afterBegin", dest);
			
			// ��һ��
			var td = detailDataTDArray[nexthop_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "nexthop";
			td.insertAdjacentHTML("afterBegin", nexthop);
			
			// ·������
			var td = detailDataTDArray[rtype_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "rtype";
			td.insertAdjacentHTML("afterBegin", rtype);
			
			// ·��Э��
			var td = detailDataTDArray[proto_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "proto";
			td.insertAdjacentHTML("afterBegin", proto);
			
			// ��������
			var td = detailDataTDArray[mask_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "mask";
			td.insertAdjacentHTML("afterBegin", mask);
		}
		
	},
	
	// ------------			�豸·����Ϣ    END					----------------------
	// -------------------------------------------------------------------------------
	
	// -------------------------------------------------------------------------------
	// ------------			�豸IP�б���Ϣ    START				----------------------
	// ��ȡ�豸 IP�б� ��Ϣ
	getIpListInfo: function (){
		NetRemoteService.getIpListInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				ObjectSelf.callbackgetIpListInfo(data);
			}
		});
	},
	
	// ��ȡ�豸 FDB ��Ϣ����Ļص�����
	callbackgetIpListInfo: function(data){
		this.data = data;
		this.createIpListInfo();
	},
	
	// ��ȡ�豸 FDB ��Ϣ�� �����豸 FDB ��Ϣ
	createIpListInfo: function(){
		var listTBODY = this.detailDataTABLE.tBodies[1];
		if(!listTBODY){
			// �����ϸ��Ϣҳ�����ݱ���� �����б�� TBODY Ϊ null �����´��� (һ�㲻�����)
			this.createDetailDataTABLE();
		}
		// ɾ���б������е�Ԫ��
		this.removeAllChild(listTBODY);
		
		var ipListInfoList = this.data;
		var length = ipListInfoList.length;
		if(!ipListInfoList || length == 0){
			return;
		}
		
		var currTabId = this.currTab.id;
		var indexs_cellIndex = $(currTabId + "-titleTD-" + "indexs").cellIndex;
		var descr_cellIndex = $(currTabId + "-titleTD-" + "descr").cellIndex;
		var speed_cellIndex = $(currTabId + "-titleTD-" + "speed").cellIndex;
		var aliasip_cellIndex = $(currTabId + "-titleTD-" + "aliasip").cellIndex;
		var status_cellIndex = $(currTabId + "-titleTD-" + "status").cellIndex;
		var types_cellIndex = $(currTabId + "-titleTD-" + "types").cellIndex;
		var showDetail_cellIndex = $(currTabId + "-titleTD-" + "showDetail").cellIndex;
		
		var i = 0;
		while(i < length){
			var ipListInfo = ipListInfoList[i];
			i++;
			var index = i;									// ���
			var indexs = ipListInfo.indexs;					// ����
			var descr = ipListInfo.descr;					// ����
			var speed = ipListInfo.speed;					// ÿ���ֽ���
			var aliasip = ipListInfo.aliasip;				// aliasip
			//var status = null;							// ״̬
			var types = ipListInfo.types;					// ����
			
			var tr = this.createElement("", "tr");
			tr.height = 25;
			listTBODY.appendChild(tr);
			var detailDataTDArray = this.createDetailDataTD(7, tr);
			
			// ����
			var td = detailDataTDArray[indexs_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "indexs";
			td.insertAdjacentHTML("afterBegin", indexs);
			
			// ����
			var td = detailDataTDArray[descr_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "descr";
			td.insertAdjacentHTML("afterBegin", descr);
			
			// ÿ���ֽ���
			var td = detailDataTDArray[speed_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "speed";
			td.insertAdjacentHTML("afterBegin", speed);
			tr.appendChild(td);
			
			// aliasip
			var td = detailDataTDArray[aliasip_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "aliasip";
			td.insertAdjacentHTML("afterBegin", aliasip);
			
			// ״̬
			var td = detailDataTDArray[aliasip_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "status";
			
			// ����
			var td = detailDataTDArray[types_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "types";
			td.insertAdjacentHTML("afterBegin", types);
			
			// �鿴
			var td = detailDataTDArray[showDetail_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "showDetail";
		}
		
	},
	
	// ------------			�豸IP�б���Ϣ    END					----------------------
	// -------------------------------------------------------------------------------
	
	// -------------------------------------------------------------------------------
	// ------------			�豸����������Ϣ     START				----------------------
	// ��ȡ�豸����������Ϣ 
	getPowerEnvironmentInfo: function(){
		NetRemoteService.getHostNodeInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				ObjectSelf.callbackgetPowerEnvironmentInfo(data);
			}
		});
	},
	
	// ��ȡ�豸����������Ϣ����Ļص�����
	callbackgetPowerEnvironmentInfo: function(data){
		this.hostNode = data;
		this.createPowerEnvironmentInfo();
	},
	
	
	// ��ȡ�豸����������Ϣ�� �����豸����������Ϣ
	createPowerEnvironmentInfo: function(){
		if(!this.detailDataTABLE){
			// �����ϸ��Ϣҳ�����ݵı��Ϊ null
			this.detailDataTABLE = this.createElement("detailDataTABLE", "table");
			this.detailDataDIV.appendChild(this.detailDataTABLE);
		}

		var tbody = this.createElement("", "tbody");
		this.detailDataTABLE.appendChild(tbody);

		var tr = this.createElement("", "tr");
		tbody.appendChild(tr);
		
		// ���� ��Դ power ���� td
		var td = this.createElement("", "td");
		tr.appendChild(td);
		// ���� ��Դ power ���� table
		var Envoriment_componentPowerPath = this.SWFPath + this.Envoriment_componentSWFName + ".swf?" +
				"ipadress=" + this.hostNode.ipAddress + 
				"&key=" + this.Envoriment_componentPowerSWFkey + 
				"&table=" + this.Envoriment_componentPowerSWFkey +  
				"&type=" + this.subtype+
				"&title=��Դ";  
		var Envoriment_componentPowerSWFName = this.Envoriment_componentSWFName +  this.Envoriment_componentPowerSWFkey;
		// ���� ��Դ power SWF
		var Envoriment_componentPowerSWFTABLE = this.createSWFTABLE(td, Envoriment_componentPowerPath, Envoriment_componentPowerSWFName, "346", "250", this.SWFVersion, this.SWFbackgroundclor);
		
		// ���� ��ѹ vol ���� td
		var td = this.createElement("", "td");
		tr.appendChild(td);
		// ���� ��ѹ vol ���� table
		var Envoriment_componentVolSWFPath = this.SWFPath + this.Envoriment_componentSWFName + ".swf?" +
				"ipadress=" + this.hostNode.ipAddress + 
				"&key=" + this.Envoriment_componentVolSWFkey + 
				"&table=" + this.Envoriment_componentVolSWFkey +  
				"&type=" + this.subtype+
				"&title=��ѹ"; 
		var Envoriment_componentVolSWFName = this.Envoriment_componentSWFName + this.Envoriment_componentVolSWFkey;
		// ���� ��ѹ vol SWF
		var Envoriment_componentVolSWFTABLE = this.createSWFTABLE(td, Envoriment_componentVolSWFPath, Envoriment_componentVolSWFName, "346", "250", this.SWFVersion, this.SWFbackgroundclor);
		
		if(this.hostNode.collecttype == 3 || this.hostNode.collecttype ==4 
		|| this.hostNode.collecttype == 8 || this.hostNode.collecttype == 9){
			return;			
		}
		
		// -------------------��һ��-------------------------
		var tr = this.createElement("", "tr");
		tbody.appendChild(tr);
		
		// ���� ���� fan ���� td
		var td = this.createElement("", "td");
		tr.appendChild(td);
		// ���� ���� fan ���� table
		var Envoriment_componentFanrPath = this.SWFPath + this.Envoriment_componentSWFName + ".swf?" +
				"ipadress=" + this.hostNode.ipAddress + 
				"&key=" + this.Envoriment_componentFanSWFkey + 
				"&table=" + this.Envoriment_componentFanSWFkey +  
				"&type=" + this.subtype+
				"&title=����"; 
		var Envoriment_componentFanSWFName = this.Envoriment_componentSWFName +  this.Envoriment_componentFanSWFkey;
		// ���� ���� fan SWF
		var Envoriment_componentFanSWFTABLE = this.createSWFTABLE(td, Envoriment_componentFanrPath, Envoriment_componentFanSWFName, "346", "250", this.SWFVersion, this.SWFbackgroundclor);
		
		// ���� �¶� temper ���� td
		var td = this.createElement("", "td");
		tr.appendChild(td);
		// ���� �¶� temper ���� table
		var Envoriment_componentTemperSWFPath = this.SWFPath + this.Envoriment_componentSWFName + ".swf?" +
				"ipadress=" + this.hostNode.ipAddress + 
				"&key=" + this.Envoriment_componentTemperSWFkey + 
				"&table=" + this.Envoriment_componentTemperSWFTable +  
				"&type=" + this.subtype +
				"&title=�¶�"; 
		var Envoriment_componentTemperSWFName = this.Envoriment_componentSWFName + this.Envoriment_componentTemperSWFkey;
		// ���� ��ѹ vol SWF
		var Envoriment_componentTemperSWFTABLE = this.createSWFTABLE(td, Envoriment_componentTemperSWFPath, Envoriment_componentTemperSWFName, "346", "250", this.SWFVersion, this.SWFbackgroundclor);
		
		
	},
	
	// ------------			�豸����������Ϣ    END				----------------------
	// -------------------------------------------------------------------------------
	
	// -------------------------------------------------------------------------------
	// ------------			�豸�澯��Ϣ     START					----------------------
	// ��ȡ�豸�澯��Ϣ
	getAlarmInfo: function(){
		var startdateElement = $('startdate');
		var todateElement = $('todate');
		var level1Element = $('level1');
		var stautsElement = $('event_status');
		var searchAlarmInfoElement = $('searchAlarmInfo');
		
		if(!searchAlarmInfoElement.onclick){
			searchAlarmInfoElement.onclick = function(){
				ObjectSelf.getAlarmInfo();
			}
			
		}
		
		var date = new Date();
		if(!startdateElement.value){
			startdateElement.value = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
		}
		
		if(!todateElement.value){
			 todateElement.value = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
		}
		
		var level1Value = null;
		var stautsValue = null;
		var startdateValue = startdateElement.value;
		var todateValue = todateElement.value
		
		if(level1Element){
			 level1Value = level1Element.value;
		}
		
		if(stautsElement){
			stautsElement.value = "-1";
			stautsValue = stautsElement.value;
		}
		
		NetRemoteService.getAlarmInfo(this.nodeid, this.type, this.subtype, 
			startdateValue, todateValue, level1Value, null, {
				callback:function(data){
					ObjectSelf.callbackgetAlarmInfo(data);
				}
		});
	},
	
	// ��ȡ�豸�澯��Ϣ����Ļص�����
	callbackgetAlarmInfo: function(data){
		this.data = data;
		this.createAlarmInfo();
		
	},
	
	// ��ȡ�豸�澯��Ϣ�󴴽��澯��Ϣ
	createAlarmInfo: function(){
		var listTBODY = this.detailDataTABLE.tBodies[1];
		if(!listTBODY){
			// �����ϸ��Ϣҳ�����ݱ���� �����б�� TBODY Ϊ null �����´��� (һ�㲻�����)
			this.createDetailDataTABLE();
		}
		// ɾ���б������е�Ԫ��
		this.removeAllChild(listTBODY);
		
		
		var eventList = this.data;
		if(!eventList || !eventList.length){
			return;
		}
		var currTabId = this.currTab.id;
		var index_cellIndex = $(currTabId + "-titleTD-" + "index").cellIndex;
		var level1_cellIndex = $(currTabId + "-titleTD-" + "level1").cellIndex;
		var content_cellIndex = $(currTabId + "-titleTD-" + "content").cellIndex;
		//var recordtime_cellIndex = $(currTabId + "-titleTD-" + "recordtime").cellIndex;
		//var eventtype_cellIndex = $(currTabId + "-titleTD-" + "eventtype").cellIndex;
		//var managesign_cellIndex = $(currTabId + "-titleTD-" + "managesign").cellIndex;
		var maxtime_cellIndex = $(currTabId + "-titleTD-" + "maxtime").cellIndex;
		var count_cellIndex = $(currTabId + "-titleTD-" + "count").cellIndex;
		var operation_cellIndex = $(currTabId + "-titleTD-" + "operation").cellIndex;
		
		var length = eventList.length;
		var i = 0;
		while(i < length){
			var eventListInfo = eventList[i];
			i++;
			
			var event = eventListInfo[0];
			var maxtime = eventListInfo[2];				// ����ʱ��
			var count = eventListInfo[1];				// ����
			var eventId = event.id;						// �¼�ID
			var index = i;								// ���
			var level1 = event.level1;					// �¼��ȼ�
			var content = event.content;				// �¼�����
			var eventlocation = event.eventlocation;	// �¼���Դ
			var subentity = event.subentity;			// �¼�ָ��
			var operation = null;						// ���ݴ���״̬����ȡ����
			
			var tr = this.createElement("", "tr");
			tr.height = 25;
			listTBODY.appendChild(tr);
			var detailDataTDArray = this.createDetailDataTD(6, tr);
			
			// ���
			var td = detailDataTDArray[index_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "index";
			td.insertAdjacentHTML("afterBegin", index);
			
			// �¼��ȼ�
			var td = detailDataTDArray[level1_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "level1";
			var level_str = "";
			if("3" == level1){
				level_str = "�����¼�";
				td.style.backgroundColor = "red"
			} else if("2" == level1){
				level_str = "�����¼�";
				td.style.backgroundColor = "orange";
			} else if("1" == level1){
				level_str = "��ͨ�¼�";
				td.style.backgroundColor = "yellow";
			}
			td.insertAdjacentHTML("afterBegin", level_str);
			
			// �¼�����
			var td = detailDataTDArray[content_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "content";
			td.insertAdjacentHTML("afterBegin", content);
			
			// ����ʱ��
			var td = detailDataTDArray[maxtime_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "maxtime";
			td.insertAdjacentHTML("afterBegin", maxtime);
			
			// ����
			var td = detailDataTDArray[count_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "count";
			td.insertAdjacentHTML("afterBegin", count);
			
			// ����
			var td = detailDataTDArray[operation_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "operation";
			var button = this.createElement("", "input");
			button.type = "button";
			td.appendChild(button);
			button.value = "�鿴����";
			button.onclick = function(eventlocation, level1, subentity){
				return function(){
					var searchAlarmInfoElement = $('searchAlarmInfo');
					var level1Element = $('level1');
					level1Element.value = level1;
					if(searchAlarmInfoElement){
						searchAlarmInfoElement.onclick = function(){
							ObjectSelf.showAlarmDetailInfo(eventlocation, subentity);
						}
					}
					ObjectSelf.showAlarmDetailInfo(eventlocation, subentity);
				};
			}(eventlocation, level1, subentity);
		}
	},
	
	showAlarmDetailInfo: function(eventlocation, subentity){
		var startdateElement = $('startdate');
		var todateElement = $('todate');
		var level1Element = $('level1');
		var stautsElement = $('event_status');
		var startdateValue = startdateElement.value;
		var todateValue = todateElement.value;
		var level1Value = level1Element.value;
		var stautsValue = stautsElement.value;
		
		var titleTBODY = this.detailDataTABLE.tBodies[0];
		if(!titleTBODY){
			this.createDetailDataTABLE();
		}
		titleTBODY.deleteRow(1);
		var titleTR = this.createElement(this.currTab.id + "titleTR-1", "tr")
		titleTBODY.appendChild(titleTR);
		var titleArray = ["���", "�¼��ȼ�", "�¼�����", "�Ǽ�����", "�Ǽ���", "����״̬", "����"];
		var titleIDArray = ["index", "level1", "content", "recordtime", "eventtype", "managesign", "operation"];
		var length = titleArray.length;
		titleTBODY.rows[0].cells[0].colSpan = length;
		var i = 0;
		while(i < length){
			var titleTD = this.createElement(this.currTab.id + "-titleTD-" + titleIDArray[i], "td");
			titleTD.style.height = 25;
			titleTD.className = "body-data-title";
			titleTD.insertAdjacentHTML("afterBegin", titleArray[i]);  
			titleTR.appendChild(titleTD);
			i++;
		}
		
		NetRemoteService.getAlarmDetailInfo(this.nodeid, this.type, this.subtype, 
			startdateValue, todateValue, level1Value, eventlocation, subentity, stautsValue, {
				callback:function(data){
					ObjectSelf.data = data;
					ObjectSelf.createAlarmDetailInfo();
				}
		});
	},
	
	
	createAlarmDetailInfo: function(){
		var listTBODY = this.detailDataTABLE.tBodies[1];
		if(!listTBODY){
			// �����ϸ��Ϣҳ�����ݱ���� �����б�� TBODY Ϊ null �����´��� (һ�㲻�����)
			this.createDetailDataTABLE();
		}
		// ɾ���б������е�Ԫ��
		this.removeAllChild(listTBODY);
		
		
		var eventList = this.data;
		if(!eventList || !eventList.length){
			return;
		}
		var currTabId = this.currTab.id;
		var index_cellIndex = $(currTabId + "-titleTD-" + "index").cellIndex;
		var level1_cellIndex = $(currTabId + "-titleTD-" + "level1").cellIndex;
		var content_cellIndex = $(currTabId + "-titleTD-" + "content").cellIndex;
		var recordtime_cellIndex = $(currTabId + "-titleTD-" + "recordtime").cellIndex;
		var eventtype_cellIndex = $(currTabId + "-titleTD-" + "eventtype").cellIndex;
		var managesign_cellIndex = $(currTabId + "-titleTD-" + "managesign").cellIndex;
		var operation_cellIndex = $(currTabId + "-titleTD-" + "operation").cellIndex;
		
		var length = eventList.length;
		var i = 0;
		while(i < length){
			var event = eventList[i];
			i++;
			var eventId = event.id;						// �¼�ID
			var index = i;								// ���
			var level1 = event.level1;					// �¼��ȼ�
			var content = event.content;				// �¼�����
			var recordtime = event.recordtime;			// �Ǽ�����
			var eventtype = event.eventtype;			// �Ǽ���
			var managesign = event.managesign;			// ����״̬
			var operation = null;						// ���ݴ���״̬����ȡ����
			
			var tr = this.createElement("", "tr");
			tr.height = 25;
			listTBODY.appendChild(tr);
			var detailDataTDArray = this.createDetailDataTD(7, tr);
			
			// ���
			var td = detailDataTDArray[index_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "index";
			td.insertAdjacentHTML("afterBegin", index);
			
			// �¼��ȼ�
			var td = detailDataTDArray[level1_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "level1";
			if("3" == level1){
				level1 = "�����¼�";
				td.style.backgroundColor = "red"
			} else if("2" == level1){
				level1 = "�����¼�";
				td.style.backgroundColor = "orange";
			} else if("1" == level1){
				level1 = "��ͨ�¼�";
				td.style.backgroundColor = "yellow";
			}
			td.insertAdjacentHTML("afterBegin", level1);
			
			// �¼�����
			var td = detailDataTDArray[content_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "content";
			td.insertAdjacentHTML("afterBegin", content);
			
			// �Ǽ�����
			var recordtime = new Date(recordtime);
			var td = detailDataTDArray[recordtime_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "recordtime";
			td.insertAdjacentHTML("afterBegin", 
				recordtime.getFullYear() + '-' + (recordtime.getMonth() + 1) + '-' + recordtime.getDate()+ 
				' ' + recordtime.getHours() + ':' + recordtime.getMinutes() + ':' + recordtime.getSeconds());
			
			// �Ǽ���
			var td = detailDataTDArray[eventtype_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "eventtype";
			td.insertAdjacentHTML("afterBegin", eventtype);
			
			// ����״̬
			var operationStatus = "";
			if("0" == managesign){
		  		operationStatus = "δ����";
		  	}else if("1" == managesign){
		  		operationStatus = "������";  	
		  	}else if("2" == eventStatus){
		  	  	operationStatus = "�������";
		  	}
			var td = detailDataTDArray[managesign_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "managesign";
			td.insertAdjacentHTML("afterBegin", operationStatus);
			
			// ����
			var td = detailDataTDArray[operation_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "operation";
			var button = this.createElement("", "input");
			button.type = "button";
			
			if("0" == managesign){
				button.value = "���ܴ���";
				button.onclick = function(eventId){
					return function(){
						alert("���ܴ���==eventId" + eventId)
						};
				}(eventId);
		  	}else if("1" == managesign){
		  		button.value = "��д����";
		  		button.onclick = function(eventId){
					return function(){
						alert("��д����==eventId" + eventId)
						};
				}(eventId);
		  	}else if("2" == managesign){
		  		button.value = "�鿴����";
		  		button.onclick = function(eventId){
					return function(){
						alert("�鿴����==eventId" + eventId)
						};
				}(eventId);
		  	}
		}
	},
	
	// ------------			�豸�澯��Ϣ    END					----------------------
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