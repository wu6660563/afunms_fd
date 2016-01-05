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
	
	// ϵͳ��Ϣ���� CPU SWF �ļ���
	this.DHCCGaugeSWFName = "DHCCGauge";
	
	// ������Ϣ���� ��Ӧʱ�� Response_time SWF �ļ���
	this.responseTimeSWFName = "Response_time";
	
	// ������Ϣ���� ��ͨ�� Ping SWF �ļ���
	this.pingSWFName = "Ping";
	
	// ������Ϣ���� CPU Line_CPU SWF �ļ���
	this.Line_CPUSWFName = "Line_CPU";
	
	// ������Ϣ���� �ڴ� Area_Memory SWF �ļ���
	this.Area_MemorySWFName = "Area_Memory";
	
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
		if(freshSystemInfo){
			freshSystemInfo.onclick = function (){
				AS400RemoteService.getHostNodeInfo(ObjectSelf.nodeid, ObjectSelf.type, ObjectSelf.subtype, {
					callback:function(data){
						ObjectSelf.hostNode =  data;
						ObjectSelf.getSystemInfo();
					}
				});
			}
		}
		
		
		AS400RemoteService.getHostNodeInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				ObjectSelf.hostNode =  data;
				ObjectSelf.getSystemInfo();
			}
		});
		
		this.getTabInfo();
		
	},
	
	// ��ȡ�豸ϵͳ��Ϣ
	getSystemInfo: function (){
		AS400RemoteService.getSystemInfo(this.nodeid, this.type, this.subtype, {
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
		
		AS400RemoteService.getCurrDayPingAvgInfo(this.nodeid, this.type, this.subtype, {
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
		
		AS400RemoteService.getCurrCpuAvgInfo(this.nodeid, this.type, this.subtype, {
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
		AS400RemoteService.getStautsInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				var statusImg = "";
				if(data){
					statusImg = data;
				}
				$('systemInfo_stauts').innerHTML = "<img src=\"" + ObjectSelf.rootPath + "/resource/" + statusImg+ "\">";
			}
		});
		AS400RemoteService.getCategoryInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				var category = "";
				if(data){
					category = data;
				}
				$('systemInfo_category').innerHTML = category;
			}
		});
		
		AS400RemoteService.getSupperInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				var supperInfo = "";
				if(data){
					supperInfo = data;
				}
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
		AS400RemoteService.getTabInfo(this.nodeid, this.type, this.subtype, {
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
					titleTR = this.createElement(this.currTab.id + "-titleTR-" + titleTRRow, "tr")
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
	
	// ��ȡ�豸��ϸ��Ϣ����� �����豸��ϸ��Ϣ���ݱ����е� td �� ��������Щ td �����鼯��
	// length  		--- ��Ҫ���� td �ĸ��� 
	// TRContain	--- ÿһ�� td ������ �� tr �� ���δ�գ� ����Щ td ��������뵽 tr �� ��
	// 					��Ҫ�������Լ����뵽 tr �У����򽫲�����ʾ��ҳ����
	createDetailTitleTD : function(length, TRContain){
		var detailTitileTDArray = new Array();
		if(!length || length == 0){ 
			return detailTitileTDArray;
		}
		for(var i = 0; i < length; i++){
			var td = this.createElement("", "td");
			td.className = 'detail-data-body-title';
			detailTitileTDArray.push(td);
			if(TRContain){
				TRContain.appendChild(td);
			}
		}
		return detailTitileTDArray;
	},
	
	// -------------------------------------------------------------------------------
	// ------------			����Ϊ���� tab ҳ����Ҫ����Ϣ			----------------------
	// -------------------------------------------------------------------------------
	
	// -------------------------------------------------------------------------------
	// ------------			�豸ϵͳ״̬��Ϣ    START					----------------------
	// ��ȡ�豸ϵͳ״̬��Ϣ
	getSystemStatusInfo: function(){
		AS400RemoteService.getSystemValueInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				ObjectSelf.callbackgetSystemValueInfo(data);
			}
		});
	},
	
	// ��ȡ�豸ϵͳ״̬��Ϣ����Ļص�����
	callbackgetSystemValueInfo: function(data){
		this.data = data;
		this.createSystemValueInfo();
	},
	
	// ��ȡ�豸ϵͳ״̬��Ϣ�� �����豸��ͨ����Ϣ
	createSystemValueInfo: function(){
		var listTBODY = this.detailDataTABLE.tBodies[1];
		if(!listTBODY){
			// �����ϸ��Ϣҳ�����ݱ���� �����б�� TBODY Ϊ null �����´��� (һ�㲻������������)
			this.createDetailDataTABLE();
		}
		// ɾ���б������е�Ԫ��
		this.removeAllChild(listTBODY);
		
		var systemValueInfoList = this.data;
		var length = 0;
		if(systemValueInfoList){
			length = systemValueInfoList.length;
		}
		var i = 0;
		var cpu = "";
		var DBCapability = "";
		var JobsInSystem = "";
		var SystemASP = "";
		var PercentSystemASPUsed = "";
		var CurrentUnprotectedStorageUsed = "";
		var MaximumUnprotectedStorageUsed = "";
		var PercentPermanentAddresses = "";
		var PercentTemporaryAddresses = "";
		while(i < length){
			var systemValueInfo = systemValueInfoList[i];
			i++;
			var category = systemValueInfo.category;
			var value = systemValueInfo.value;
			if(category == "cpu") {
				cpu = value;
			} else if(category == "DBCapability") {
				DBCapability = value;
			} else if(category == "PercentSystemASPUsed") {
				PercentSystemASPUsed = value;
			} else if(category == "JobsInSystem") {
				JobsInSystem = value;
			} else if(category == "SystemASP") {
				SystemASP = value;
			} else if(category == "CurrentUnprotectedStorageUsed") {
				CurrentUnprotectedStorageUsed = value;
			} else if(category == "MaximumUnprotectedStorageUsed") {
				MaximumUnprotectedStorageUsed = value;
			} else if(category == "PercentPermanentAddresses") {
				PercentPermanentAddresses = value;
			} else if(category == "PercentTemporaryAddresses") {
				PercentTemporaryAddresses = value;
			} 
		}
		var tr = this.createElement("", "tr");
		listTBODY.appendChild(tr);
		var DetailDataTDArray = this.createDetailDataTD(4, tr);
		DetailDataTDArray[0].innerHTML = "cpuʹ����";
		DetailDataTDArray[1].innerHTML = cpu;
		DetailDataTDArray[2].innerHTML = "���ݿ�����";
		DetailDataTDArray[3].innerHTML = DBCapability;
		
		var tr = this.createElement("", "tr");
		listTBODY.appendChild(tr);
		var DetailDataTDArray = this.createDetailDataTD(4, tr);
		DetailDataTDArray[0].innerHTML = "ϵͳ�е�����";
		DetailDataTDArray[1].innerHTML = JobsInSystem;
		DetailDataTDArray[2].innerHTML = "ϵͳASP";
		DetailDataTDArray[3].innerHTML = SystemASP;
		
		var tr = this.createElement("", "tr");
		listTBODY.appendChild(tr);
		var DetailDataTDArray = this.createDetailDataTD(4, tr);
		DetailDataTDArray[0].innerHTML = "ϵͳASP�ٷֱ�";
		DetailDataTDArray[1].innerHTML = PercentSystemASPUsed;
		DetailDataTDArray[2].innerHTML = "��ǰ�ޱ���״̬�µ�ʹ�����";
		DetailDataTDArray[3].innerHTML = CurrentUnprotectedStorageUsed;
		
		var tr = this.createElement("", "tr");
		listTBODY.appendChild(tr);
		var DetailDataTDArray = this.createDetailDataTD(4, tr);
		DetailDataTDArray[0].innerHTML = "���Ǳ���״̬";
		DetailDataTDArray[1].innerHTML = MaximumUnprotectedStorageUsed;
		DetailDataTDArray[2].innerHTML = "���õ�ַ�ٷֱ�";
		DetailDataTDArray[3].innerHTML = PercentPermanentAddresses;
		
		var tr = this.createElement("", "tr");
		listTBODY.appendChild(tr);
		var DetailDataTDArray = this.createDetailDataTD(4, tr);
		DetailDataTDArray[0].innerHTML = "��ʱ��ַ�ٷֱ�";
		DetailDataTDArray[1].innerHTML = PercentTemporaryAddresses;
		DetailDataTDArray[2].innerHTML = "";
		DetailDataTDArray[3].innerHTML = "";
		
	},
	
	// ------------			�豸ϵͳ״̬��Ϣ    END				----------------------
	// -------------------------------------------------------------------------------
	
	
	// -------------------------------------------------------------------------------
	// ------------			�豸������Ϣ    START					----------------------
	// ��ȡ�豸������Ϣ
	getPerformaceInfo: function(){
		AS400RemoteService.getHostNodeInfo(this.nodeid, this.type, this.subtype, {
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
		
		// ���� responseTime ���� td
		var td = this.createElement("", "td");
		tr.appendChild(td);
		// ���� responseTime ���� table
		var responseTimePath = this.SWFPath + this.responseTimeSWFName + ".swf?ipadress=" + this.hostNode.ipAddress; 
		var responseTimeSWFTABLE = this.createSWFTABLE(td, responseTimePath, this.responseTimeSWFName, "346", "250", this.SWFVersion, this.SWFbackgroundclor);
		// ���� Ping ���� td
		var td = this.createElement("", "td");
		tr.appendChild(td);
		// ���� Ping ���� table
		var pingSWFPath = this.SWFPath + this.pingSWFName + ".swf?ipadress=" + this.hostNode.ipAddress; 
		var pingSWFTABLE = this.createSWFTABLE(td, pingSWFPath, this.pingSWFName, "346", "250", this.SWFVersion, this.SWFbackgroundclor);
		
		if(this.hostNode.collecttype == 3 || this.hostNode.collecttype == 4 
		|| this.hostNode.collecttype == 8 || this.hostNode.collecttype == 9){
			return;			
		}
		
		// -------------------��һ��-------------------------
		
		var tr = this.createElement("", "tr");
		tbody.appendChild(tr);
		
		// ���� Line_CPU ���� td
		var td = this.createElement("", "td");
		tr.appendChild(td);
		// ���� Line_CPU ���� table
		var Line_CPUPath = this.SWFPath + this.Line_CPUSWFName + ".swf?ipadress=" + this.hostNode.ipAddress; 
		var Line_CPUSWFTABLE = this.createSWFTABLE(td, Line_CPUPath, this.Line_CPUSWFName, "346", "250", this.SWFVersion, this.SWFbackgroundclor);
		
		// ���� Area_Memory ���� td
		var td = this.createElement("", "td");
		tr.appendChild(td);
		// ���� Area_Memory ���� table
		var Area_MemoryPath = this.SWFPath + this.Area_MemorySWFName + ".swf?ipadress=" + this.hostNode.ipAddress; 
		var Area_MemorySWFTABLE = this.createSWFTABLE(td, Area_MemoryPath, this.Area_MemorySWFName, "346", "250", this.SWFVersion, this.SWFbackgroundclor);
	},
	
	// ------------			�豸������Ϣ    END					----------------------
	// -------------------------------------------------------------------------------
	
	
	// -------------------------------------------------------------------------------
	// ------------			�豸ϵͳ��Ϣ    START					----------------------
	// ��ȡ�豸ϵͳ��Ϣ
	getSysInfo: function (){
		// ��Ϊ�ж������ ��ʱû�з���������
		// ������ createSysInfo ���������
		this.callbackgetSysInfo(null);
	},
	
	// ��ȡ�豸ϵͳ��Ϣ����Ļص�����
	callbackgetSysInfo: function(data){
		this.data = data;
		this.createSysInfo();
	},
	
	// ��ȡ�豸ϵͳ��Ϣ�� �����豸ϵͳ��Ϣ
	createSysInfo: function(){
		var listTBODY = this.detailDataTABLE.tBodies[1];
		if(!listTBODY){
			// �����ϸ��Ϣҳ�����ݱ���� �����б�� TBODY Ϊ null �����´��� (һ�㲻�����)
			this.createDetailDataTABLE();
		}
		// ɾ���б������е�Ԫ��
		this.removeAllChild(listTBODY);
		
		// cpu����
		var tr = this.createElement("", "tr");
		listTBODY.appendChild(tr);
		
		var td = this.createElement("", "td");
		tr.appendChild(td);
		
		var table = this.createElement(this.currTab.id + "-cpuPerf-dataTABLE", "table");
		td.appendChild(table);
		
		var cpuPerfTBODY = this.createElement(this.currTab.id + "-cpuPerf-dataTBODY", "tbody");
		table.appendChild(cpuPerfTBODY);
		
		var detailTitleArray = ["%�û�", "%ϵͳ", "%io�ȴ�", "%����", "����"];
		var length = detailTitleArray.length;
		var tr = this.createElement(this.currTab.id + "-cpuPerf-titleTR-0", "tr");
		tr.className = "detail-data-body-title";
		cpuPerfTBODY.appendChild(tr);
		
		var td = this.createElement("", "td");
		tr.appendChild(td);
		td.colSpan = length;
		td.insertAdjacentHTML("afterBegin", "<table><tr><td style='height:25px;text-align:left;' class='detail-data-body-title'>&nbsp;&nbsp;cpu����</td></tr></table>");
		
		var tr = this.createElement(this.currTab.id + "-cpuPerf-titleTR-1", "tr");
		cpuPerfTBODY.appendChild(tr);
		
		
		var detailTitleTDArray = this.createDetailTitleTD(length, tr);
		for(var i = 0 ; i < length; i++){
			var detailTitleTD = detailTitleTDArray[i];
			detailTitleTD.style.height = '25px';
			detailTitleTD.insertAdjacentHTML("afterBegin", detailTitleArray[i]);
		}
		
				
		// ��������
		var tr = this.createElement("", "tr");
		listTBODY.appendChild(tr);
		
		var td = this.createElement("", "td");
		tr.appendChild(td);
		
		var table = this.createElement(this.currTab.id + "-diskPerf-dataTABLE", "table");
		td.appendChild(table);
		
		var diskPerfTBODY = this.createElement(this.currTab.id + "-diskPerf-dataTBODY", "tbody");
		table.appendChild(diskPerfTBODY);
		
		var detailTitleArray = ["������", "��æ(%)", "ƽ�����", "��д����/��", "��д�ֽڣ�K��/��", "ƽ���ȴ�ʱ��(ms)", "ƽ��ִ��ʱ��(ms)"];
		var tr = this.createElement(this.currTab.id + "-diskPerf-titleTR-0", "tr");
		tr.className = "detail-data-body-title";
		diskPerfTBODY.appendChild(tr);
		
		var length = detailTitleArray.length;
		var td = this.createElement("", "td");
		tr.appendChild(td);
		td.colSpan = length;
		td.insertAdjacentHTML("afterBegin", "<table><tr><td style='height:25px;text-align:left;' class='detail-data-body-title'>&nbsp;&nbsp;��������</td></tr></table>");
		
		var tr = this.createElement(this.currTab.id + "-diskPerf-titleTR-1", "tr");
		diskPerfTBODY.appendChild(tr);
		
		
		var detailTitleTDArray = this.createDetailTitleTD(length, tr);
		for(var i = 0 ; i < length; i++){
			var detailTitleTD = detailTitleTDArray[i];
			detailTitleTD.style.height = '25px';
			detailTitleTD.insertAdjacentHTML("afterBegin", detailTitleArray[i]);
		}
		
		// ҳ������ 
		var tr = this.createElement("", "tr");
		listTBODY.appendChild(tr);
		
		var td = this.createElement("", "td");
		tr.appendChild(td);
		
		var table = this.createElement(this.currTab.id + "-pageperf-dataTABLE", "table");
		td.appendChild(table);
		
		var pagePerfTBODY = this.createElement(this.currTab.id + "-pagePerf-dataTBODY", "tbody");
		table.appendChild(pagePerfTBODY);
		
		var detailTitleArray = ["ҳ����ȳ�������/����б�", "�ڴ�ҳ�������", 
								"�ڴ�ҳ�������", "�ͷŵ�ҳ��", "ɨ���ҳ", "ʱ������"];
		var tr = this.createElement(this.currTab.id + "-pagePerf-titleTR-0", "tr");
		tr.className = "detail-data-body-title";
		pagePerfTBODY.appendChild(tr);
		
		var length = detailTitleArray.length;
		var td = this.createElement("", "td");
		tr.appendChild(td);
		td.colSpan = length;
		td.insertAdjacentHTML("afterBegin", "<table><tr><td style='height:25px;text-align:left;' class='detail-data-body-title'>&nbsp;&nbsp;ҳ������</td></tr></table>");
		
		var tr = this.createElement(this.currTab.id + "-pagePerf-titleTR-1", "tr");
		pagePerfTBODY.appendChild(tr);
		
		
		var detailTitleTDArray = this.createDetailTitleTD(length, tr);
		for(var i = 0 ; i < length; i++){
			var detailTitleTD = detailTitleTDArray[i];
			detailTitleTD.style.height = '25px';
			detailTitleTD.insertAdjacentHTML("afterBegin", detailTitleArray[i]);
		}
		
		// ҳ�潻��
		var tr = this.createElement("", "tr");
		listTBODY.appendChild(tr);
		
		var td = this.createElement("", "td");
		tr.appendChild(td);
		
		var table = this.createElement(this.currTab.id + "-pageSpace-dataTABLE", "table");
		td.appendChild(table);
		
		var pageSpaceTBODY = this.createElement(this.currTab.id + "-pageSpace-dataTBODY", "tbody");
		table.appendChild(pageSpaceTBODY);
		
		var detailTitleArray = ["Total_Paging_Space", "Percent_Used"];
		var tr = this.createElement(this.currTab.id + "-pageSpace-titleTR-0", "tr");
		tr.className = "detail-data-body-title";
		pageSpaceTBODY.appendChild(tr);
		
		var length = detailTitleArray.length;
		var td = this.createElement("", "td");
		tr.appendChild(td);
		td.colSpan = length;
		td.insertAdjacentHTML("afterBegin", "<table><tr><td style='height:25px;text-align:left;' class='detail-data-body-title'>&nbsp;&nbsp;Paging Space������</td></tr></table>");
		
		var tr = this.createElement(this.currTab.id + "-pageSpace-titleTR-1", "tr");
		pageSpaceTBODY.appendChild(tr);
		
		var detailTitleTDArray = this.createDetailTitleTD(length, tr);
		for(var i = 0 ; i < length; i++){
			var detailTitleTD = detailTitleTDArray[i];
			detailTitleTD.style.height = '25px';
			detailTitleTD.insertAdjacentHTML("afterBegin", detailTitleArray[i]);
		}
		
		// ��ȡcpu������Ϣ
		AS400RemoteService.getCpuPerfInfo(this.nodeid, this.type, this.subtype,{
			callback: function(data){
				ObjectSelf.createCpuPerfInfoData(cpuPerfTBODY, data);
			}
		});
		
		// ��ȡ����������Ϣ
		AS400RemoteService.getDiskPerfInfo(this.nodeid, this.type, this.subtype,{
			callback: function(data){
				ObjectSelf.createDiskPerfInfoData(diskPerfTBODY, data);
			}
		});
		
		// ��ȡҳ��������Ϣ
		AS400RemoteService.getPagePerfInfo(this.nodeid, this.type, this.subtype,{
			callback: function(data){
				ObjectSelf.createPagePerfInfoData(pagePerfTBODY, data);
			}
		});
		
		// ��ȡҳ�潻����Ϣ
		AS400RemoteService.getPageSpaceInfo(this.nodeid, this.type, this.subtype,{
			callback: function(data){
				ObjectSelf.createPageSpaceInfoData(pageSpaceTBODY, data);
			}
		});
	},
	
	// ��ȡcpu������Ϣ�� ����cpu������Ϣ����
	createCpuPerfInfoData: function(cpuPerfTBODY, data){
		if(!cpuPerfTBODY){
			return;
		}
		var cpuPerfInfoList = data;
		if(!cpuPerfInfoList){
			return;
		}
		var length = cpuPerfInfoList.length;
		var i = 0;
		while(i < length){
			var cpuPerfInfo = cpuPerfInfoList[i];
			i++;
			var user = cpuPerfInfo.user;					// %�û�
			var sysRate = cpuPerfInfo.sysRate;				// %ϵͳ
			var wioRate = cpuPerfInfo.wioRate;				// %io�ȴ�
			var idleRate = cpuPerfInfo.idleRate;			// %����
			var physc = cpuPerfInfo.physc;					// ���� 
			
			
			var tr = this.createElement("", "tr");
			tr.height = 25;
			cpuPerfTBODY.appendChild(tr);
			
			var detailDataArray = [user, sysRate, wioRate, idleRate, physc];
			var detailDataArrayLength = detailDataArray.length;
			var detailDataTDArray = this.createDetailDataTD(detailDataArrayLength, tr);
			
			for(var j = 0; j < detailDataArrayLength; j++){
				detailDataTDArray[j].insertAdjacentHTML("afterBegin", detailDataArray[j]);
			}
		}
		
	},
	
	// ��ȡ����������Ϣ�� ��������������Ϣ����
	createDiskPerfInfoData: function(diskPerfTBODY, data){
		if(!diskPerfTBODY){
			return;
		}
		var diskPerfInfoList = data;
		if(!diskPerfInfoList){
			return;
		}
		var length = diskPerfInfoList.length;
		var i = 0;
		while(i < length){
			var diskPerfInfo = diskPerfInfoList[i];
			i++;
			var disklebel = diskPerfInfo.disklebel;			// ��������
			var busyRate = diskPerfInfo.busyRate;			// ��æ(%)
			var avque = diskPerfInfo.avque;					// ƽ�����
			var readAndWriteBlockPerSecond = diskPerfInfo.readAndWriteBlockPerSecond;	// ��д����/��
			var readAndWriteBytePerSecond = diskPerfInfo.readAndWriteBytePerSecond;		// ��д�ֽڣ�K��/��
			var avwait = diskPerfInfo.avwait;				// ƽ���ȴ�ʱ��(ms)
			var avserv = diskPerfInfo.avserv;				// ƽ��ִ��ʱ��(ms)
			
			
			var tr = this.createElement("", "tr");
			tr.height = 25;
			diskPerfTBODY.appendChild(tr);
			
			var detailDataArray = [disklebel, busyRate, avque, readAndWriteBlockPerSecond, 
									readAndWriteBytePerSecond, avwait, avserv];
			var detailDataArrayLength = detailDataArray.length;
			var detailDataTDArray = this.createDetailDataTD(detailDataArrayLength, tr);
			
			for(var j = 0; j < detailDataArrayLength; j++){
				detailDataTDArray[j].insertAdjacentHTML("afterBegin", detailDataArray[j]);
			}
		}
		
	},
	
	// ��ȡҳ��������Ϣ�� ����ҳ��������Ϣ����
	createPagePerfInfoData: function(pagePerfTBODY, data){
		if(!pagePerfTBODY){
			return;
		}
		var pagePerfInfoList = data;
		if(!pagePerfInfoList){
			return;
		}
		var length = pagePerfInfoList.length;
		var i = 0;
		while(i < length){
			var pagePerfInfo = pagePerfInfoList[i];
			i++;
			
			var re = pagePerfInfo.re;				// ҳ����ȳ�������/����б�
			var pi = pagePerfInfo.pi;				// �ڴ�ҳ�������
			var po = pagePerfInfo.po;				// �ڴ�ҳ�������
			var fr = pagePerfInfo.fr;				// �ͷŵ�ҳ��
			var sr = pagePerfInfo.sr;				// ɨ���ҳ
			var cy = pagePerfInfo.cy;				// ʱ������
			
			
			var tr = this.createElement("", "tr");
			tr.height = 25;
			pagePerfTBODY.appendChild(tr);
			
			var detailDataArray = [re, pi, po, fr, sr, cy];
			var detailDataArrayLength = detailDataArray.length;
			var detailDataTDArray = this.createDetailDataTD(detailDataArrayLength, tr);
			
			for(var j = 0; j < detailDataArrayLength; j++){
				detailDataTDArray[j].insertAdjacentHTML("afterBegin", detailDataArray[j]);
			}
		}
		
	},
	
	// ��ȡҳ�潻����Ϣ�� ����ҳ�潻����Ϣ����
	createPageSpaceInfoData: function(pageSpaceTBODY, data){
		if(!pageSpaceTBODY){
			return;
		}
		var pageSpaceInfoList = data;
		if(!pageSpaceInfoList){
			return;
		}
		var length = pageSpaceInfoList.length;
		var i = 0;
		while(i < length){
			var pageSpaceInfo = pageSpaceInfoList[i];
			i++;
			var total_Paging_Space = pageSpaceInfo.total_Paging_Space;	// Total Paging Space
			var percent_Used = pageSpaceInfo.percent_Used;				// Percent Used
			
			
			var tr = this.createElement("", "tr");
			tr.height = 25;
			pageSpaceTBODY.appendChild(tr);
			
			var detailDataArray = [total_Paging_Space, percent_Used];
			var detailDataArrayLength = detailDataArray.length;
			var detailDataTDArray = this.createDetailDataTD(detailDataArrayLength, tr);
			
			for(var j = 0; j < detailDataArrayLength; j++){
				detailDataTDArray[j].insertAdjacentHTML("afterBegin", detailDataArray[j]);
			}
		}
		
	},
	
	// ------------			�豸ϵͳ��Ϣ    END					----------------------
	// -------------------------------------------------------------------------------
	
	
	// -------------------------------------------------------------------------------
	// ------------			�豸������Ϣ    START					----------------------
	// ��ȡ�豸������Ϣ
	getConfigInfo: function (){
		// ��Ϊ�ж������ ��ʱû�з��������� 
		// ������ createConfigInfo �����ﵥ�����
		this.callbackgetConfigInfo(null);
	},
	
	// ��ȡ�豸������Ϣ����Ļص�����
	callbackgetConfigInfo: function(data){
		this.data = data;
		this.createConfigInfo();
	},
	
	// ��ȡ�豸������Ϣ�� �����豸������Ϣ
	createConfigInfo: function(){
		var listTBODY = this.detailDataTABLE.tBodies[1];
		if(!listTBODY){
			// �����ϸ��Ϣҳ�����ݱ���� �����б�� TBODY Ϊ null �����´��� (һ�㲻�����)
			this.createDetailDataTABLE();
		}
		// ɾ���б������е�Ԫ��
		this.removeAllChild(listTBODY);
		
		// �ڴ�����
		var tr = this.createElement("", "tr");
		listTBODY.appendChild(tr);
		
		var td = this.createElement("", "td");
		tr.appendChild(td);
		
		var table = this.createElement(this.currTab.id + "-memoryConfig-dataTABLE", "table");
		td.appendChild(table);
		
		var memoryConfigTBODY = this.createElement(this.currTab.id + "-memoryConfig-dataTBODY", "tbody");
		table.appendChild(memoryConfigTBODY);
		
		var detailTitleArray = ["����", "��С"];
		var length = detailTitleArray.length;
		var tr = this.createElement(this.currTab.id + "-memoryConfig-titleTR-0", "tr");
		tr.className = "detail-data-body-title";
		memoryConfigTBODY.appendChild(tr);
		
		var td = this.createElement("", "td");
		tr.appendChild(td);
		td.colSpan = length;
		td.insertAdjacentHTML("afterBegin", "<table><tr><td style='height:25px;text-align:left;' class='detail-data-body-title'>&nbsp;&nbsp;�ڴ�����</td></tr></table>");
		
		var tr = this.createElement(this.currTab.id + "-memoryConfig-titleTR-1", "tr");
		memoryConfigTBODY.appendChild(tr);
		
		
		var detailTitleTDArray = this.createDetailTitleTD(length, tr);
		for(var i = 0 ; i < length; i++){
			var detailTitleTD = detailTitleTDArray[i];
			detailTitleTD.style.height = '25px';
			detailTitleTD.insertAdjacentHTML("afterBegin", detailTitleArray[i]);
		}
		
				
		// ��������
		var tr = this.createElement("", "tr");
		listTBODY.appendChild(tr);
		
		var td = this.createElement("", "td");
		tr.appendChild(td);
		
		var table = this.createElement(this.currTab.id + "-netmediaConfig-dataTABLE", "table");
		td.appendChild(table);
		
		var netmediaConfigTBODY = this.createElement(this.currTab.id + "-netmediaConfig-dataTBODY", "tbody");
		table.appendChild(netmediaConfigTBODY);
		
		var detailTitleArray = ["����", "MAC", "����", "״̬"];
		var tr = this.createElement(this.currTab.id + "-netmediaConfig-titleTR-0", "tr");
		tr.className = "detail-data-body-title";
		netmediaConfigTBODY.appendChild(tr);
		
		var length = detailTitleArray.length;
		var td = this.createElement("", "td");
		tr.appendChild(td);
		td.colSpan = length;
		td.insertAdjacentHTML("afterBegin", "<table><tr><td style='height:25px;text-align:left;' class='detail-data-body-title'>&nbsp;&nbsp;��������</td></tr></table>");
		
		var tr = this.createElement(this.currTab.id + "-netmediaConfig-titleTR-1", "tr");
		netmediaConfigTBODY.appendChild(tr);
		
		
		var detailTitleTDArray = this.createDetailTitleTD(length, tr);
		for(var i = 0 ; i < length; i++){
			var detailTitleTD = detailTitleTDArray[i];
			detailTitleTD.style.height = '25px';
			detailTitleTD.insertAdjacentHTML("afterBegin", detailTitleArray[i]);
		}
		
		// �û�����
		var tr = this.createElement("", "tr");
		listTBODY.appendChild(tr);
		
		var td = this.createElement("", "td");
		tr.appendChild(td);
		
		var table = this.createElement(this.currTab.id + "-userConfig-dataTABLE", "table");
		td.appendChild(table);
		
		var userConfigTBODY = this.createElement(this.currTab.id + "-userConfig-dataTBODY", "tbody");
		table.appendChild(userConfigTBODY);

		var detailTitleArray = ["����", "�û���"];
		var tr = this.createElement(this.currTab.id + "-userConfig-titleTR-0", "tr");
		tr.className = "detail-data-body-title";
		userConfigTBODY.appendChild(tr);
		
		var length = detailTitleArray.length;
		var td = this.createElement("", "td");
		tr.appendChild(td);
		td.colSpan = length;
		td.insertAdjacentHTML("afterBegin", "<table><tr><td style='height:25px;text-align:left;' class='detail-data-body-title'>&nbsp;&nbsp;ҳ������</td></tr></table>");
		
		var tr = this.createElement(this.currTab.id + "-userConfig-titleTR-1", "tr");
		userConfigTBODY.appendChild(tr);
		
		
		var detailTitleTDArray = this.createDetailTitleTD(length, tr);
		for(var i = 0 ; i < length; i++){
			var detailTitleTD = detailTitleTDArray[i];
			detailTitleTD.style.height = '25px';
			detailTitleTD.insertAdjacentHTML("afterBegin", detailTitleArray[i]);
		}
		
		
		// ��ȡ�ڴ�������Ϣ
		AS400RemoteService.getMemoryConfigInfo(this.nodeid, this.type, this.subtype,{
			callback: function(data){
				ObjectSelf.createMemoryConfigInfoData(memoryConfigTBODY, data);
			}
		});
		
		// ��ȡ����������Ϣ
		AS400RemoteService.getNetmediaConfigInfo(this.nodeid, this.type, this.subtype,{
			callback: function(data){
				ObjectSelf.createNetmediaConfigInfoData(netmediaConfigTBODY, data);
			}
		});
		
		// ��ȡ�û�������Ϣ
		AS400RemoteService.getUserConfigInfo(this.nodeid, this.type, this.subtype,{
			callback: function(data){
				ObjectSelf.createUserConfigInfoData(userConfigTBODY, data);
			}
		});
		
	},
	
	// ��ȡ�ڴ�������Ϣ�� �����ڴ�������Ϣ����
	createMemoryConfigInfoData: function(memoryConfigTBODY, data){
		if(!memoryConfigTBODY){
			return;
		}
		var memoryConfigInfoList = data;
		if(!memoryConfigInfoList){
			return;
		}
		var length = memoryConfigInfoList.length;
		var i = 0;
		while(i < length){
			var memoryConfigInfo = memoryConfigInfoList[i];
			i++;
			var descr_cn = memoryConfigInfo.descr_cn;			// ��������
			var size = memoryConfigInfo.size;					// ��С
			var unit = memoryConfigInfo.unit;					// ��С�ĵ�λ
			//var type = memoryConfigInfo.type;					// ����
			//var sindex = memoryConfigInfo.sindex;				// ����
			
			
			var tr = this.createElement("", "tr");
			tr.height = 25;
			memoryConfigTBODY.appendChild(tr);
			
			//var detailDataArray = [descr_cn, size];
			//var detailDataArrayLength = detailDataArray.length;
			var detailDataTDArray = this.createDetailDataTD(2, tr);
			
			// ����
			var td = detailDataTDArray[0];
			td.insertAdjacentHTML("afterBegin", descr_cn);
			
			// ��С
			var td = detailDataTDArray[1];
			td.insertAdjacentHTML("afterBegin", size + unit);
		}
		
	},
	
	// ��ȡ����������Ϣ�� ��������������Ϣ����
	createNetmediaConfigInfoData: function(netmediaConfigTBODY, data){
		if(!netmediaConfigTBODY){
			return;
		}
		var NetmediaConfigInfoList = data;
		if(!NetmediaConfigInfoList){
			return;
		}
		var length = NetmediaConfigInfoList.length;
		var i = 0;
		while(i < length){
			var netmediaConfig = NetmediaConfigInfoList[i];
			i++;
			var desc = netmediaConfig.desc;				// ����
			var mac = netmediaConfig.mac;				// MAC
			var speed = netmediaConfig.speed;			// ����
			var status = netmediaConfig.status;			// ״̬
			
			
			var tr = this.createElement("", "tr");
			tr.height = 25;
			netmediaConfigTBODY.appendChild(tr);
			
			var detailDataArray = [desc, mac, speed, status];
			var detailDataArrayLength = detailDataArray.length;
			var detailDataTDArray = this.createDetailDataTD(detailDataArrayLength, tr);
			
			for(var j = 0; j < detailDataArrayLength; j++){
				detailDataTDArray[j].insertAdjacentHTML("afterBegin", detailDataArray[j]);
			}
		}
		
	},
	
	// ��ȡ�û�������Ϣ�� �����û�������Ϣ����
	createUserConfigInfoData: function(userConfigTBODY, data){
		if(!userConfigTBODY){
			return;
		}
		var userConfigInfoList = data;
		if(!userConfigInfoList){
			return;
		}
		var length = userConfigInfoList.length;
		var i = 0;
		while(i < length){
			var userConfig = userConfigInfoList[i];
			i++;
			var name = userConfig.name;				// ����
			var userGroup = userConfig.userGroup;	// �û���
			
			
			var tr = this.createElement("", "tr");
			tr.height = 25;
			userConfigTBODY.appendChild(tr);
			
			var detailDataArray = [name, userGroup];
			var detailDataArrayLength = detailDataArray.length;
			var detailDataTDArray = this.createDetailDataTD(detailDataArrayLength, tr);
			
			for(var j = 0; j < detailDataArrayLength; j++){
				detailDataTDArray[j].insertAdjacentHTML("afterBegin", detailDataArray[j]);
			}
		}
		
	},
	
	// ------------			�豸������Ϣ    END					----------------------
	// -------------------------------------------------------------------------------
	
	
	// -------------------------------------------------------------------------------
	// ------------			�豸������Ϣ    START					----------------------
	// ��ȡ�豸������Ϣ 
	getProcessInfo: function(){
		AS400RemoteService.getProcessInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				ObjectSelf.callbackgetProcessInfo(data);
			}
		});
	},
	
	// ��ȡ�豸������Ϣ����Ļص�����
	callbackgetProcessInfo: function(data){
		this.data = data;
		this.createProcessInfo();
	},
	
	// ��ȡ�豸������Ϣ�� �����豸������Ϣ
	createProcessInfo: function(){
		var listTBODY = this.detailDataTABLE.tBodies[1];
		if(!listTBODY){
			// �����ϸ��Ϣҳ�����ݱ���� �����б�� TBODY Ϊ null �����´��� (һ�㲻�����)
			this.createDetailDataTABLE();
		}
		// ɾ���б������е�Ԫ��
		this.removeAllChild(listTBODY);
		var processInfoList = this.data;
		if(processInfoList == null){
			return;
		}
		
		var currTabId = this.currTab.id;
		var name_cellIndex = $(currTabId + "-titleTD-" + "name").cellIndex;
		var count_cellIndex = $(currTabId + "-titleTD-" + "count").cellIndex;
		var type_cellIndex = $(currTabId + "-titleTD-" + "type").cellIndex;
		var cpuTime_cellIndex = $(currTabId + "-titleTD-" + "cpuTime").cellIndex;
		var memoryUtilization_cellIndex = $(currTabId + "-titleTD-" + "memoryUtilization").cellIndex;
		var memory_cellIndex = $(currTabId + "-titleTD-" + "memory").cellIndex;
		var status_cellIndex= $(currTabId + "-titleTD-" + "status").cellIndex;
		var showDetail_cellIndex = $(currTabId + "-titleTD-" + "showDetail").cellIndex;
		
		var length = processInfoList.length;
		var i = 0;
		while(i < length){
			var processInfo = processInfoList[i];
			i++;
			var index = i;								// ���
			var name = processInfo.name;				// ��������
			var count = processInfo.count;				// ���̸���
			var type = processInfo.type;				// ��������
			var cpuTime = processInfo.cpuTime;			// CPUʱ��
			var memoryUtilization = processInfo.memoryUtilization;			// �ڴ�ռ����
			var memory = processInfo.memory;			// �ڴ�ռ����
			var status = processInfo.status;			// ��ǰ״̬
			
			var tr = this.createElement("", "tr");
			tr.height = 25;
			listTBODY.appendChild(tr);
			var detailDataTDArray = this.createDetailDataTD(8, tr);
			
			// ��������
			var td = detailDataTDArray[name_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "name";
			td.insertAdjacentHTML("afterBegin", name);
			
			// ���̸���
			var td = detailDataTDArray[count_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "count";
			td.insertAdjacentHTML("afterBegin", count);
			
			// ��������
			var td = detailDataTDArray[type_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "type";
			td.insertAdjacentHTML("afterBegin", type);
			
			// CPUʱ��
			var td = detailDataTDArray[cpuTime_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "cpuTime";
			td.insertAdjacentHTML("afterBegin", cpuTime);
			
			// �ڴ�ռ����
			var td = detailDataTDArray[memoryUtilization_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "memoryUtilization";
			td.insertAdjacentHTML("afterBegin", memoryUtilization);
			
			// �ڴ�ռ����
			var td = detailDataTDArray[memory_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "memory";
			td.insertAdjacentHTML("afterBegin", memory);
			
			// ��ǰ״̬
			var td = detailDataTDArray[status_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "status";
			td.insertAdjacentHTML("afterBegin", status);
			
			// �鿴����
			var td = detailDataTDArray[showDetail_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "showDetail";
			var button = this.createElement("", "input");
			button.type = "button";
			button.value = "�鿴����";
			td.appendChild(button);
			button.onclick = function(name){
					return function(){
						ObjectSelf.showProcessInfoDetail(name);
					};
			}(name);
		}
		
	},
	
	// ��ȡ�豸������Ϣ�� �����豸������Ϣ�� ����鿴��������
	showProcessInfoDetail: function(name){
		AS400RemoteService.getProcessDetailInfo(this.nodeid, this.type, this.subtype, name, {
			callback:function(data){
				ObjectSelf.createProcessDetailInfo(data);
			}
		});
		var titleTBODY = this.detailDataTABLE.tBodies[0];
		if(!titleTBODY){
			this.createDetailDataTABLE();
		}
		this.removeAllChild(titleTBODY);
		
		var titleTR = this.createElement(this.currTab.id + "titleTR-0", "tr")
		titleTBODY.appendChild(titleTR);
		var titleArray = ["����ID", "��������", "��������", "cpuʱ��", "�ڴ�ռ����", "�ڴ�ռ����", "��ǰ״̬"];
		var titleIDArray = ["pid", "name", "type", "cpuTime", "memoryUtilization", "memory", "status"];
		var length = titleArray.length;
		var i = 0;
		while(i < length){
			var titleTD = this.createElement(titleIDArray[i], "td");
			titleTD.style.height = 25;
			titleTD.className = "body-data-title";
			titleTD.insertAdjacentHTML("afterBegin", titleArray[i]);   // ����ID
			titleTR.appendChild(titleTD);
			i++;
		}
		
	},
	
	// ��ȡ�豸������Ϣ�� �����豸������Ϣ�� ��������������Ϣ
	createProcessDetailInfo: function(data){
		var listTBODY = this.detailDataTABLE.tBodies[1];
		if(!listTBODY){
			// �����ϸ��Ϣҳ�����ݱ���� �����б�� TBODY Ϊ null �����´��� (һ�㲻�����)
			this.createDetailDataTABLE();
		}
		// ɾ���б������е�Ԫ��
		this.removeAllChild(listTBODY);
		
		var processDetailInfoList = data;
		var length = processDetailInfoList.length;
		if(processDetailInfoList == null || length == 0){
			return;
		}
		var i = 0;
		while(i < length){
			var processInfo = processDetailInfoList[i];
			i++;
			var index = i + 1;							// ���
			var pid = processInfo.pid;					// ����ID
			var name = processInfo.name;				// ��������
			var type = processInfo.type;				// ��������
			var cpuTime = processInfo.cpuTime;			// CPUʱ��
			var memoryUtilization = processInfo.memoryUtilization;			// �ڴ�ռ����
			var memory = processInfo.memory;			// �ڴ�ռ����
			var status = processInfo.status;			// ��ǰ״̬
			
			var tr = this.createElement("", "tr");
			tr.height = 25;
			listTBODY.appendChild(tr);
			
			// ����ID
			var td = this.createElement("", "td");
			td.className = 'detail-data-body-list';
			td.insertAdjacentHTML("afterBegin", pid);
			tr.appendChild(td);
			
			// ��������
			var td = this.createElement("", "td");
			td.className = 'detail-data-body-list';
			td.insertAdjacentHTML("afterBegin", name);
			tr.appendChild(td);
			
			// ��������
			var td = this.createElement("", "td");
			td.className = 'detail-data-body-list';
			td.insertAdjacentHTML("afterBegin", type);
			tr.appendChild(td);
			
			// CPUʱ��
			var td = this.createElement("", "td");
			td.className = 'detail-data-body-list';
			td.insertAdjacentHTML("afterBegin", cpuTime);
			tr.appendChild(td);
			
			// �ڴ�ռ����
			var td = this.createElement("", "td");
			td.className = 'detail-data-body-list';
			td.insertAdjacentHTML("afterBegin", memoryUtilization);
			tr.appendChild(td);
			
			// �ڴ�ռ����
			var td = this.createElement("", "td");
			td.className = 'detail-data-body-list';
			td.insertAdjacentHTML("afterBegin", memory);
			tr.appendChild(td);
			
			// ��ǰ״̬
			var td = this.createElement("", "td");
			td.className = 'detail-data-body-list';
			td.insertAdjacentHTML("afterBegin", status);
			tr.appendChild(td);
		}
		
	},
	
	// ------------			�豸������Ϣ    END					----------------------
	// -------------------------------------------------------------------------------
	
	
	// -------------------------------------------------------------------------------
	// ------------			�豸ϵͳ����Ϣ    START					----------------------
	// ��ȡ�豸ϵͳ����Ϣ
	getSystemPoolInfo: function (){
		AS400RemoteService.getSystemPoolInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				ObjectSelf.callbackgetSystemPoolInfo(data);
			}
		});
	},
	
	// ��ȡ�豸ϵͳ����Ϣ����Ļص�����
	callbackgetSystemPoolInfo: function(data){
		this.data = data;
		this.createSystemPoolInfo();
	},
	
	// ��ȡ�豸ϵͳ����Ϣ�� �����豸ϵͳ����Ϣ
	createSystemPoolInfo: function(){
		var listTBODY = this.detailDataTABLE.tBodies[1];
		if(!listTBODY){
			// �����ϸ��Ϣҳ�����ݱ���� �����б�� TBODY Ϊ null �����´��� (һ�㲻�����)
			this.createDetailDataTABLE();
		}
		// ɾ���б������е�Ԫ��
		this.removeAllChild(listTBODY);
		var systemPoolInfoList = this.data;
		
		var length = 0;
		if(systemPoolInfoList){
			length = systemPoolInfoList.length;
		}
		
		var currTabId = this.currTab.id;
		var systemPool_cellIndex = $(currTabId + "-titleTD-" + "systemPool").cellIndex;
		var name_cellIndex = $(currTabId + "-titleTD-" + "name").cellIndex;
		var size_cellIndex = $(currTabId + "-titleTD-" + "size").cellIndex;
		var reservedSize_cellIndex = $(currTabId + "-titleTD-" + "reservedSize").cellIndex;
		var maximumActiveThreads_cellIndex = $(currTabId + "-titleTD-" + "maximumActiveThreads").cellIndex;
		
		
		var i = 0
		while(i < length){
			var systemPoolInfo = systemPoolInfoList[i];
			i++;
			var index = i;														// ���
			var systemPool = systemPoolInfo.systemPool;							// ϵͳ������Id
			var name = systemPoolInfo.name;										// ϵͳ������
			var size = systemPoolInfo.size;										// ϵͳ�ش�С
			var reservedSize = systemPoolInfo.reservedSize;						// ϵͳ�صı����ش�С
			var maximumActiveThreads = systemPoolInfo.maximumActiveThreads;		// ϵͳ�������
			
			var tr = this.createElement(this.currTab.id + "-dataTR-" + index, "tr");
			tr.height = 25;
			listTBODY.appendChild(tr);
			var detailDataTDArray = this.createDetailDataTD(5, tr);
			
			// ϵͳ������Id
			var td = detailDataTDArray[systemPool_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "systemPool";
			td.insertAdjacentHTML("afterBegin", systemPool);
			
			// ϵͳ������
			var td = detailDataTDArray[name_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "name";
			td.insertAdjacentHTML("afterBegin", name);
			
			// ϵͳ�ش�С
			var td = detailDataTDArray[size_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "size";
			td.insertAdjacentHTML("afterBegin", size);
			
			// ϵͳ�صı����ش�С
			var td = detailDataTDArray[reservedSize_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "reservedSize";
			td.insertAdjacentHTML("afterBegin", reservedSize);
			
			// ϵͳ�������
			var td = detailDataTDArray[maximumActiveThreads_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "maximumActiveThreads";
			td.insertAdjacentHTML("afterBegin", maximumActiveThreads);
			
			
			
		}
		
	},
	
	// ------------			�豸ϵͳ����Ϣ    END					----------------------
	// -------------------------------------------------------------------------------
	
	
	// -------------------------------------------------------------------------------
	// ------------			�豸������Ϣ    START				----------------------
	// ��ȡ�豸������Ϣ 
	getDiskInfo: function (){
		AS400RemoteService.getDiskInfo(this.nodeid, this.type, this.subtype, {
				callback:function(data){
					ObjectSelf.callbackgetDiskInfo(data);
				}
		});
	},
	
	// ��ȡ�豸������Ϣ����Ļص�����
	callbackgetDiskInfo: function(data){
		this.data = data;
		this.createDiskInfo();
	},
	
	// ��ȡ�豸������Ϣ�� �����豸������Ϣ
	createDiskInfo: function(){
		var listTBODY = this.detailDataTABLE.tBodies[1];
		if(!listTBODY){
			// �����ϸ��Ϣҳ�����ݱ���� �����б�� TBODY Ϊ null �����´��� (һ�㲻�����)
			this.createDetailDataTABLE();
		}
		// ɾ���б������е�Ԫ��
		this.removeAllChild(listTBODY);
		
		var diskInfoList = this.data;
		var length = 0;
		if(diskInfoList){
			length = diskInfoList.length;
		}
		
		var currTabId = this.currTab.id;
		var unit_cellIndex = $(currTabId + "-titleTD-" + "unit").cellIndex;
		var type_cellIndex = $(currTabId + "-titleTD-" + "type").cellIndex;
		var size_cellIndex = $(currTabId + "-titleTD-" + "size").cellIndex;
		var used_cellIndex = $(currTabId + "-titleTD-" + "used").cellIndex;
		var ioRqs_cellIndex = $(currTabId + "-titleTD-" + "ioRqs").cellIndex;
		var requestSize_cellIndex = $(currTabId + "-titleTD-" + "requestSize").cellIndex;
		var readRqs_cellIndex = $(currTabId + "-titleTD-" + "readRqs").cellIndex;
		var writeRqs_cellIndex = $(currTabId + "-titleTD-" + "writeRqs").cellIndex;
		var read_cellIndex = $(currTabId + "-titleTD-" + "read").cellIndex;
		var write_cellIndex = $(currTabId + "-titleTD-" + "write").cellIndex;
		var busy_cellIndex = $(currTabId + "-titleTD-" + "busy").cellIndex;
		
		var i = 0
		while(i < length){
			var diskInfo = diskInfoList[i];
			i++;
			var index = i;
			var unit = diskInfo.unit;					// ��Ԫ
			var diskInfoType = diskInfo.type;			// ����
		 	var size = diskInfo.size;					// ��С
			var used = diskInfo.used;					// �������ðٷֱ�
			var ioRqs = diskInfo.ioRqs;					// IO Rqs
			var requestSize = diskInfo.requestSize;		// ������Ĵ�С
			var readRqs = diskInfo.readRqs;				// �� Rqs
			var writeRqs = diskInfo.writeRqs;				// д Rqs
			var read = diskInfo.read;					// ��(K)
			var write = diskInfo.write;					// д(K)
			var busy = diskInfo.busy;					// æµ
			
			var tr = this.createElement(this.currTab.id + "-dataTR-" + index, "tr");
			tr.height = 25;
			listTBODY.appendChild(tr);
			var detailDataTDArray = this.createDetailDataTD(11, tr);
			
			// ��Ԫ
			var td = detailDataTDArray[unit_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "unit";
			td.insertAdjacentHTML("afterBegin", unit);
			
			// ����
			var td = detailDataTDArray[type_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "type";
			td.insertAdjacentHTML("afterBegin", diskInfoType);
			
			// ��С
			var td = detailDataTDArray[size_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "size";
			td.insertAdjacentHTML("afterBegin", size);
			
			// �������ðٷֱ�
			var td = detailDataTDArray[used_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "used";
			td.insertAdjacentHTML("afterBegin", used);
			
			// IO Rqs
			var td = detailDataTDArray[ioRqs_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "ioRqs";
			td.insertAdjacentHTML("afterBegin", ioRqs);
			
			// ������Ĵ�С
			var td = detailDataTDArray[requestSize_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "requestSize";
			td.insertAdjacentHTML("afterBegin", requestSize);
			
			// �� Rqs
			var td = detailDataTDArray[readRqs_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "readRqs";
			td.insertAdjacentHTML("afterBegin", readRqs);
			
			// д Rqs
			var td = detailDataTDArray[writeRqs_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "writeRqs";
			td.insertAdjacentHTML("afterBegin", writeRqs);
			
			// ��(K)
			var td = detailDataTDArray[read_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "read";
			td.insertAdjacentHTML("afterBegin", read);
			
			// д(K)
			var td = detailDataTDArray[write_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "write";
			td.insertAdjacentHTML("afterBegin", write);
			
			// æµ
			var td = detailDataTDArray[busy_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "busy";
			td.insertAdjacentHTML("afterBegin", busy);
		}
		
	},
	
	// ------------			�豸������Ϣ    END					----------------------
	// -------------------------------------------------------------------------------
	
	
	// -------------------------------------------------------------------------------
	// ------------			�豸��ҵ��Ϣ    START					----------------------
	// ��ȡ�豸��ҵ��Ϣ
	getJobInfo: function (){
		
		var jobTypeElement = $('jobType');
		var jobSubtypeElement = $('jobSubtype');
		var jobActivestatusElement = $('jobActivestatus');
		var jobSubsystemElement = $('jobSubsystem');
		var searchJobInfoElement = $('searchJobInfo');
		
		if(!searchJobInfoElement.onclick){
			searchJobInfoElement.onclick = function(){
				ObjectSelf.getJobInfo();
			}
		}
		
		if(jobTypeElement.options.length == 0){
			var getJobTypeInfo = function (){
				jobTypeElement.options[0] = new Option("����", "-1");
				AS400RemoteService.getJobTypeInfo(this.nodeid, this.type, this.subtype, {
					callback:function(data){
						var jobTypeInfoList = data;
						var length = 0;
						if(jobTypeInfoList){
							length = jobTypeInfoList.length;
						}
						var i = 0;
						while(i < length){
							var jobTypeInfo = jobTypeInfoList[i];
							i++;
							jobTypeElement.options[i] = new Option(jobTypeInfo[1], jobTypeInfo[0]);
						}
					}
				});
			};
			getJobTypeInfo();
			jobTypeElement.value = "-1";
		}
		
		if(jobSubtypeElement.options.length == 0){
			var getJobSubtypeInfo = function (){
				jobSubtypeElement.options[0] = new Option("����", "-1");
				AS400RemoteService.getJobSubtypeInfo(this.nodeid, this.type, this.subtype, {
					callback:function(data){
						var jobSubtypeInfoList = data;
						var length = 0;
						if(jobSubtypeInfoList){
							length = jobSubtypeInfoList.length;
						}
						var i = 0;
						while(i < length){
							var jobSubtypeInfo = jobSubtypeInfoList[i];
							i++;
							jobSubtypeElement.options[i] = new Option(jobSubtypeInfo[1], jobSubtypeInfo[0]);
						}
					}
				});
			};
			getJobSubtypeInfo();
			jobSubtypeElement.value = "-1";
		}
		
		if(jobActivestatusElement.options.length == 0){
			var getJobActivestatusInfo = function (){
				jobActivestatusElement.options[0] = new Option("����", "-1");
				AS400RemoteService.getJobActiveStatusInfo(this.nodeid, this.type, this.subtype, {
					callback:function(data){
						var jobActiveStatusInfoList = data;
						var length = 0;
						if(jobActiveStatusInfoList){
							length = jobActiveStatusInfoList.length;
						}
						var i = 0;
						while(i < length){
							var jobActiveStatusInfo = jobActiveStatusInfoList[i];
							i++;
							jobActivestatusElement.options[i] = new Option(jobActiveStatusInfo[1], jobActiveStatusInfo[0]);
						}
					}
				});
			};
			getJobActivestatusInfo();
			jobActivestatusElement.value = "-1";
		}
		
		var jobTypeValue = jobTypeElement.value;
		var jobSubtypeValue = jobSubtypeElement.value;
		var jobActivestatusValue = jobActivestatusElement.value;
		var jobSubsystemValue = "";
		if(jobSubsystemElement){
			jobSubsystemValue = jobSubsystemElement.value;
			jobSubsystemElement.value = null;
		}
		
		AS400RemoteService.getJobInfo(this.nodeid, this.type, this.subtype, jobTypeValue, jobSubtypeValue, jobActivestatusValue, jobSubsystemValue, {
			callback:function(data){
				ObjectSelf.callbackgetJobInfo(data);
			}
		});
	},
	
	// ��ȡ�豸��ҵ��Ϣ����Ļص�����
	callbackgetJobInfo: function(data){
		this.data = data;
		this.createJobInfo();
	},
	
	// ��ȡ�豸��ҵ��Ϣ�� �����豸��ҵ��Ϣ
	createJobInfo: function(){
		var listTBODY = this.detailDataTABLE.tBodies[1];
		if(!listTBODY){
			// �����ϸ��Ϣҳ�����ݱ���� �����б�� TBODY Ϊ null �����´��� (һ�㲻�����)
			this.createDetailDataTABLE();
		}
		// ɾ���б������е�Ԫ��
		this.removeAllChild(listTBODY);
		
		var jobInfoList = this.data;
		var length = 0;
		if(jobInfoList){
			length = jobInfoList.length;
		}
		
		var currTabId = this.currTab.id;
		
		
		var index_cellIndex = $(currTabId + "-titleTD-" + "index").cellIndex;
		var name_cellIndex = $(currTabId + "-titleTD-" + "name").cellIndex;
		var user_cellIndex = $(currTabId + "-titleTD-" + "user").cellIndex;
		var type_cellIndex = $(currTabId + "-titleTD-" + "type").cellIndex;
		var subtype_cellIndex = $(currTabId + "-titleTD-" + "subtype").cellIndex;
		var CPUUsedTime_cellIndex = $(currTabId + "-titleTD-" + "CPUUsedTime").cellIndex;
		var activeStatus_cellIndex = $(currTabId + "-titleTD-" + "activeStatus").cellIndex;
		
		var i = 0;
		while(i < length){
			var jobInfo = jobInfoList[i];
			i++;
			var index = i;									// ���
			var name = jobInfo.name;						// ��ϵͳ/����
			var user = jobInfo.user;						// �û�
			var jobType = jobInfo.type;						// ����
			var jobSubtype = jobInfo.subtype;				// ������
			var CPUUsedTime = jobInfo.CPUUsedTime;			// ռ��CPUʱ��(����)
			var activeStatus = jobInfo.activeStatus;		// �״̬
			
			var tr = this.createElement(this.currTab.id + "-dataTR-" + index, "tr");
			tr.height = 25;
			listTBODY.appendChild(tr);
			var detailDataTDArray = this.createDetailDataTD(7, tr);
			
			// ���
			var td = detailDataTDArray[index_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "index";
			td.insertAdjacentHTML("afterBegin", index);
			
			// ��ϵͳ/����
			var td = detailDataTDArray[name_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "name";
			td.insertAdjacentHTML("afterBegin", name);
			
			// �û�
			var td = detailDataTDArray[user_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "user";
			td.insertAdjacentHTML("afterBegin", user);
			
			// ����
			var td = detailDataTDArray[type_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "type";
			td.insertAdjacentHTML("afterBegin", jobType);
			
			// ������
			var td = detailDataTDArray[subtype_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "subtype";
			td.insertAdjacentHTML("afterBegin", jobSubtype);
			
			// ռ��CPUʱ��(����)
			var td = detailDataTDArray[CPUUsedTime_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "CPUUsedTime";
			td.insertAdjacentHTML("afterBegin", CPUUsedTime);
			
			// �״̬
			var td = detailDataTDArray[activeStatus_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "activeStatus";
			td.insertAdjacentHTML("afterBegin", activeStatus);
			
		}
		
	},
	
	// ------------			�豸��ҵ��Ϣ    END					----------------------
	// -------------------------------------------------------------------------------
	
	
	// -------------------------------------------------------------------------------
	// ------------			�豸��ϵͳ��Ϣ    START				----------------------
	// ��ȡ�豸��ϵͳ��Ϣ
	getSubsystemInfo: function (){
		AS400RemoteService.getSubsystemInfo(this.nodeid, this.type, this.subtype, {
			callback:function(data){
				ObjectSelf.callbackgetSubsystemInfo(data);
			}
		});
	},
	
	// ��ȡ�豸��ϵͳ��Ϣ����Ļص�����
	callbackgetSubsystemInfo: function(data){
		this.data = data;
		this.createSubsystemInfo();
	},
	
	// ��ȡ�豸��ϵͳ��Ϣ�� �����豸��ϵͳ��Ϣ
	createSubsystemInfo: function(){
		var listTBODY = this.detailDataTABLE.tBodies[1];
		if(!listTBODY){
			// �����ϸ��Ϣҳ�����ݱ���� �����б�� TBODY Ϊ null �����´��� (һ�㲻�����)
			this.createDetailDataTABLE();
		}
		// ɾ���б������е�Ԫ��
		this.removeAllChild(listTBODY);
		
		var subsystemInfoList = this.data;
		var length = 0;
		if(subsystemInfoList){
			length = subsystemInfoList.length;
		}
		
		var currTabId = this.currTab.id;
		
		
		var index_cellIndex = $(currTabId + "-titleTD-" + "index").cellIndex;
		var name_cellIndex = $(currTabId + "-titleTD-" + "name").cellIndex;
		var jobNum_cellIndex = $(currTabId + "-titleTD-" + "jobNum").cellIndex;
		var path_cellIndex = $(currTabId + "-titleTD-" + "path").cellIndex;
		
		var i = 0;
		while(i < length){
			var subsystemInfo = subsystemInfoList[i];
			i++;
			var index = i;									// ���
			var name = subsystemInfo.name;					// ����
			var jobNum = subsystemInfo.jobNum;				// ��ҵ��
			var path = subsystemInfo.path;					// ·��
			
			var tr = this.createElement(this.currTab.id + "-dataTR-" + index, "tr");
			tr.height = 25;
			listTBODY.appendChild(tr);
			var detailDataTDArray = this.createDetailDataTD(4, tr);
			
			// ���
			var td = detailDataTDArray[index_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "index";
			td.insertAdjacentHTML("afterBegin", index);
			
			// ����
			var td = detailDataTDArray[name_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "name";
			td.style.cursor = "hand";
			td.insertAdjacentHTML("afterBegin", "<a>" + name + "</a>");
			td.onclick = function(path){
				return function(){
					var titleList = null;
					for(var i = 0 ; i < ObjectSelf.tabList.length; i++){
						var tab = ObjectSelf.tabList[i];
						if(tab.id == "JobInfo"){
							titleList = tab.titleList;
						}
					}
					var jobSubsystem = $("jobSubsystem");
					if(!jobSubsystem){
						jobSubsystem = ObjectSelf.createElement("jobSubsystem", "input");
						jobSubsystem.type = "hidden";
					}
					jobSubsystem.value = path;
					ObjectSelf.detailDataTABLE.appendChild(jobSubsystem);
					ObjectSelf.createTitleInfo(titleList);
					ObjectSelf.getJobInfo();
				}
			}(path)
			
			// ��ҵ��
			var td = detailDataTDArray[jobNum_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "jobNum";
			td.insertAdjacentHTML("afterBegin", jobNum);
			
			// ·��
			var td = detailDataTDArray[path_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "path";
			td.insertAdjacentHTML("afterBegin", path);
		}
		
	},
	
	// ------------			�豸�豸��ϵͳ��Ϣ  END					----------------------
	// -------------------------------------------------------------------------------
	
	// -------------------------------------------------------------------------------
	// ------------			�豸Syslog��Ϣ    START				----------------------
	// ��ȡ�豸Syslog��Ϣ
	getSyslogInfo: function (){
		
		var startdateElement = $('startdate');				// ��ȡ��ʼ����
		var todateElement = $('todate');					// ��ȡ��ֹ����	
		var prioritynameElement = $('priorityname');		// ��ȡ��־�ȼ�
		var searchSyslogInfoElement = $('searchSyslogInfo');
		
		if(!searchSyslogInfoElement.onclick){
			searchSyslogInfoElement.onclick = function(){
				ObjectSelf.getSyslogInfo();
			}
		}
		
		var date = new Date();
		if(!startdateElement.value){
			startdateElement.value = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
		}
		
		if(!todateElement.value){
			 todateElement.value = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
		}
		
		var startdateValue = startdateElement.value;
		var todateValue = todateElement.value;
		var prioritynameValue = null;
		
		if(prioritynameElement){
			 prioritynameValue = prioritynameElement.value;
		}
		
		AS400RemoteService.getSyslogInfo(this.nodeid, this.type, this.subtype, 
			startdateValue, todateValue, prioritynameValue, {
				callback:function(data){
					ObjectSelf.callbackgetSyslogInfo(data);
				}
		});
	},
	
	// ��ȡ�豸Syslog��Ϣ����Ļص�����
	callbackgetSyslogInfo: function(data){
		this.data = data;
		this.createSyslogInfo();
	},
	
	// ��ȡ�豸Syslog��Ϣ�� �����豸Syslog��Ϣ
	createSyslogInfo: function(){
		var listTBODY = this.detailDataTABLE.tBodies[1];
		if(!listTBODY){
			// �����ϸ��Ϣҳ�����ݱ���� �����б�� TBODY Ϊ null �����´��� (һ�㲻�����)
			this.createDetailDataTABLE();
		}
		// ɾ���б������е�Ԫ��
		this.removeAllChild(listTBODY);
		var syslogInfoList = this.data;
		
		if(!syslogInfoList || !syslogInfoList.length){
			return;
		}
		
		var currTabId = this.currTab.id;
		var index_cellIndex = $(currTabId + "-titleTD-" + "index").cellIndex;
		var priorityName_cellIndex = $(currTabId + "-titleTD-" + "priorityName").cellIndex;
		var recordtime_cellIndex = $(currTabId + "-titleTD-" + "recordtime").cellIndex;
		var facility_cellIndex = $(currTabId + "-titleTD-" + "facility").cellIndex;
		var processname_cellIndex = $(currTabId + "-titleTD-" + "processname").cellIndex;
		var eventid_cellIndex = $(currTabId + "-titleTD-" + "eventid").cellIndex;
		var username_cellIndex = $(currTabId + "-titleTD-" + "username").cellIndex;
		var hostname_cellIndex = $(currTabId + "-titleTD-" + "hostname").cellIndex;
		var showDetail_cellIndex = $(currTabId + "-titleTD-" + "showDetail").cellIndex;
		
		var length = syslogInfoList.length;
		var i = 0
		while(i < length){
			var syslogInfo = syslogInfoList[i];
			i++;
			var id = syslogInfo.id;									// ID
			var ipaddress = syslogInfo.ipaddress;					// IP
			var index = i;											// ���
			var priorityName = syslogInfo.priorityName;				// ����
			var recordtime = syslogInfo.recordtime;					// ����
			var facility = syslogInfo.facility;						// ��Դ
			var processname = syslogInfo.processname;				// ����
			var eventid = syslogInfo.eventid;						// �¼�
			var username = syslogInfo.username;						// �û�
			var hostname = syslogInfo.hostname;						// �����
			var showDetail = null;									// �鿴
			
			var tr = this.createElement(this.currTab.id + "-dataTR-" + index, "tr");
			tr.height = 25;
			listTBODY.appendChild(tr);
			var detailDataTDArray = this.createDetailDataTD(9, tr);
			
			// ���
			var td = detailDataTDArray[index_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "index";
			td.insertAdjacentHTML("afterBegin", index);
			
			// ����
			var td = detailDataTDArray[priorityName_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "priorityName";
			td.insertAdjacentHTML("afterBegin", priorityName);
			
			// ����
			var recordtime = new Date(recordtime);
			var td = detailDataTDArray[recordtime_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "recordtime";
			td.insertAdjacentHTML("afterBegin", recordtime.getFullYear() + '-' + (recordtime.getMonth() + 1) + '-' + recordtime.getDate()+ 
				' ' + recordtime.getHours() + ':' + recordtime.getMinutes() + ':' + recordtime.getSeconds());
			
			// ��Դ
			var td = detailDataTDArray[facility_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "facility";
			td.insertAdjacentHTML("afterBegin", facility);
			
			// ����
			var td = detailDataTDArray[processname_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "processname";
			td.insertAdjacentHTML("afterBegin", processname);
			
			// �¼�
			var td = detailDataTDArray[eventid_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "eventid";
			td.insertAdjacentHTML("afterBegin", eventid);
			
			// �û�
			var td = detailDataTDArray[username_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "username";
			td.insertAdjacentHTML("afterBegin", username);
			
			// �����
			var td = detailDataTDArray[hostname_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "hostname";
			td.insertAdjacentHTML("afterBegin", hostname);
			
			// �鿴����
			var td = detailDataTDArray[showDetail_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "showDetail";
			var button = this.createElement("", "input");
			button.type = "button";
			button.value = "�鿴����";
			td.appendChild(button);
			button.onclick = function(id, ipaddress){
					return function(){
						ObjectSelf.showSyslogInfoDetail(id, ipaddress);
					};
			}(id, ipaddress);
		}
		
	},
	
	showSyslogInfoDetail: function(id, ipaddress){
		window.open(this.rootPath + '/monitor.do?action=hostsyslogdetail' +
				'&id=' + id + '&ipaddress=' + ipaddress,
				"protypeWindow",
				"toolbar=no,width=500,height=400,directories=no,status=no,scrollbars=yes,menubar=no");
	},
	
	// ------------			�豸Syslog��Ϣ    END					----------------------
	// -------------------------------------------------------------------------------
	
	
	// -------------------------------------------------------------------------------
	// ------------			�豸�豸��Ϣ    START				----------------------
	// ��ȡ�豸�豸��Ϣ
	getDeviceInfo: function (){
		AS400RemoteService.getDeviceInfo(this.nodeid, this.type, this.subtype, {
				callback:function(data){
					ObjectSelf.callbackgetDeviceInfo(data);
				}
		});
	},
	
	// ��ȡ�豸�豸��Ϣ����Ļص�����
	callbackgetDeviceInfo: function(data){
		this.data = data;
		this.createDeviceInfo();
	},
	
	// ��ȡ�豸�豸��Ϣ�� �����豸�豸��Ϣ
	createDeviceInfo: function(){
		var listTBODY = this.detailDataTABLE.tBodies[1];
		if(!listTBODY){
			// �����ϸ��Ϣҳ�����ݱ���� �����б�� TBODY Ϊ null �����´��� (һ�㲻�����)
			this.createDetailDataTABLE();
		}
		// ɾ���б������е�Ԫ��
		this.removeAllChild(listTBODY);
		var deviceInfoList = this.data;
		if(!deviceInfoList || !deviceInfoList.length){
			return;
		}
		var currTabId = this.currTab.id;
		var index_cellIndex = $(currTabId + "-titleTD-" + "index").cellIndex;
		var dtype_cellIndex = $(currTabId + "-titleTD-" + "dtype").cellIndex;
		var name_cellIndex = $(currTabId + "-titleTD-" + "name").cellIndex;
		var status_cellIndex = $(currTabId + "-titleTD-" + "status").cellIndex;
		
		var length = deviceInfoList.length;
		var i = 0
		while(i < length){
			var deviceInfo = deviceInfoList[i];
			i++;
			var index = i;									// ���
			//var deviceindex = deviceInfo.deviceindex;		//
			var dtype = deviceInfo.dtype;					// �豸����
			var name = deviceInfo.name;						// ����
			var status = deviceInfo.status;					// ״̬
			
			var tr = this.createElement(this.currTab.id + "-dataTR-" + index, "tr");
			tr.height = 25;
			listTBODY.appendChild(tr);
			var detailDataTDArray = this.createDetailDataTD(4, tr);
			
			// ���
			var td = detailDataTDArray[index_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "index";
			td.insertAdjacentHTML("afterBegin", index);
			
			// �豸����
			var td = detailDataTDArray[dtype_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "dtype";
			td.insertAdjacentHTML("afterBegin", dtype);
			
			// ����
			var td = detailDataTDArray[name_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "name";
			td.insertAdjacentHTML("afterBegin", name);
			
			// ״̬
			var td = detailDataTDArray[status_cellIndex];
			td.id = this.currTab.id + "-dataTD-" + index + "-" + "status";
			td.insertAdjacentHTML("afterBegin", status);
			
		}
		
	},
	
	// ------------			�豸�豸��Ϣ    END					----------------------
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
		
		AS400RemoteService.getAlarmInfo(this.nodeid, this.type, this.subtype, 
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
		
		AS400RemoteService.getAlarmDetailInfo(this.nodeid, this.type, this.subtype, 
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