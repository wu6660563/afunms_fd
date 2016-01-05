// info 信息框样式
var infoBgColor     = "#F5F5F5";
var infoBgImg       = "";           // path to background image;
var infoBorder      = "solid black 1px";
var infoBorderColor = "#003399";
var infoBorderWidth = 1;
var infoDelay       = 500;          // time span until tooltip shows up
									// [milliseconds]
var infoFontColor   = "#000000";
var infoFontFace    = "宋体,arial,helvetica,sans-serif";
var infoFontSize    = "12px";
var infoFontWeight  = "normal";     // alternative: "bold";
var infoPadding     = 3;            // spacing between border and content
var infoShadowColor = "";
var infoShadowWidth = 0;   
var infoTextAlign   = "left";
var infoTitleColor  = "#ffffff";    // color of caption text
var infoWidth       = 180;

// 右键菜单对象 以后可能直接用 divMenuElement
var clickObj = null;
// 右键菜单对象 以后可能直接用 divMenuElement
var clickLineObj = null;
// 画框选中的图元数组
var objMoveAry = new Array();
// 触发事件的控件对象
var obj = null;
// 触发事件的控件对象,用于改变选中图片后样式表		
var objStyle = null;
// 搜索的字符串
var strSearch = null;
// yangjun addCtrl键选中的图元
var objEntityAry = new Array();
// 存放当前事件控件的相关连线
var line_ids = new Array();
// 判断选框
var selectStatus = false;
// FIXME: 该变量未定义
var movedObjFlag = false;
// 判断ctrl
var ctrlStatus = false;

var isRemoved = false;

var lineMoveAry = new Array();
// 辅助链路存放数组yangjun add
var assLineMoveAry = new Array();
// 示意链路存放数组 yangjun add
var demoLineMoveAry = new Array();
// 存放修改过的对象id的数组
var tempArray = new Array();

window.document.onselectstart = selectStart;

var topologyMapObject = null;

Array.prototype.contains = function()
{ 
	for (var i = 0; i < this.length; i++)
	{
		if (arguments[0] == this[i])
		{
			return true;
		}
	}
	return false;
};

var nodeobj = function(id,image,category,xmlname,relationMap){
	this.id = id;
    this.category = category;
    this.image = image;
    this.xmlname = xmlname;
    this.relationMap = relationMap;
}
var linkobj = function(id,lineid,color,xmlname,type){
	this.id = id;
    this.color = color;
    this.lineid = lineid;
    this.xmlname = xmlname;
    this.type = type;
}

function TopologyMapNode() {

	// id
	this.id = null;

	// 类型
	this.nodeType = null;

	// 图片
	this.imgValue = null;

	// X 位置
	this.x = 0;

	// Y 位置
	this.y = 0;

	// IP
	this.ip = null;

	// 别名
	this.alias = null;

	// 设备信息
	this.info = null;

	// 设备右键菜单
	this.menu = null;

	// 设备关联子图
	this.relationMap = null;
	
	// 设备图片对象
	this.imageElement = null;
	
	// 设备文本对象
	this.divTextElement = null;

	// 设备信息对象
	this.divInfoElement = null;

	// 设备别名上下空白
	this.aliasHSpace = 0;
	
	// 设备别名左右空白
	this.aliasVSpace = 0;
	
	// 是否被选中
	this.isSelected = false;
	
	// 设备移动之前的 X 位置
	this.beforeMoveX = 0;

	// 设备移动之前的 Y 位置
	this.beforeMoveY = 0;
	
	// 设备被鼠标按住状态
	this.mouseDownStatus = false;
};


function TopologyMapLine() {
	
	// XML链路 id
	this.lineid = null;

	// 链路 a
	this.a = null;
	
	// 链路 b
	this.b = null;
	
	// 链路颜色
	this.color = null;
	
	// 链路
	this.dash = null;
	
	// 链路宽度
	this.lineWidth = null;
	
	// 链路信息
	this.lineInfo = null;
	
	// 链路菜单
	this.lineMenu = null;

	// 链路id
	this.id = null;
	
	// 链路对象
	this.lineElement = null;
	
	// 鼠标状态
    this.mouseDownStatus = false;
};

function TopologyMapOperation() {

	// 删除设备
	this.deleteTopolgyMapNode = false;

	// 删除示意设备
	this.deleteTopolgyMapHinNode = false;
	
	// 删除链路
	this.deleteTopolgyMapLine = false;

	// 删除示意链路
	this.deleteTopolgyMapHinLine = false;
	
	// 修改链路
	this.updateTopolgyMapLine = false;

	// 修改示意链路
	this.updateTopolgyMapHinLine = false;
};

function TopologyMapObject() {
	
	// TopologyMapObject 类本身 该变量为此类的全局变量用于回调反转时使用
	ObjectSelf = this;

	// 拓扑图的 XML 文件
	this.xmlName = null;

	// 拓扑图的 XML 对象
	this.xmldoc = null;
	
	// 拓扑图最大宽度
	this.maxWidth = 0;
	
	// 拓扑图最大高度
	this.maxHeight = 0;
	
	// 拓扑图最小高度
	this.minWidth = 0;
	
	// 拓扑图最小高度
	this.minHeight = 0;
	
	// 拓扑图 XML 设备节点数据
	this.nodes = null;
	
	// 拓扑图 XML 设备链路数据
	this.lines = null;
	
	// 拓扑图 XML 设备双链路数据
	this.assistantlines = null;
	
	// 拓扑图 XML 设备示意数据
	this.demoLines = null;
	
	// 拓扑图设备节点对象数组
	this.topologyMapNodes = new Array();
	
	// 拓扑图设备链路对象数组
	this.topologyMapLines = new Array();

	// 拓扑图双链路对象数组
	this.topologyMapAssistantLines = new Array();
	
	// 拓扑图示意链路对象数组
	this.topologyMapDemoLines = new Array();
	
	// 拓扑图设备节点数组用于更新
	this.nodeobjArray = new Array();
	
	// 拓扑图链路节点数组用于更新
	this.linkobjArray = new Array();
	
	// 设备信息对象
	this.divInfoElement = null;
	
	// 设备菜单对象
	this.divMenuElement = null;
	
	// 选中的设备数组中
	this.selectedTopologyMapNodes = new Array();
	
	// 鼠标初始位置 X 用于计算移动
	this.mouseOriX = null;
	
	// 鼠标初始位置 Y 用于计算移动
	this.mouseOriY = null;
	
	// 画框选择时，用的上下左右四根彩线
	this.imgTop = document.getElementById("imgTop");

	// 画框选择时，用的上下左右四根彩线
	this.imgLeft = document.getElementById("imgLeft");

	// 画框选择时，用的上下左右四根彩线
	this.imgBottom = document.getElementById("imgBottom");

	// 画框选择时，用的上下左右四根彩线
	this.imgRight = document.getElementById("imgRight");

	// 拓扑图展现的 DIV
	this.divLayer = document.getElementById("divLayer");

	// 拓扑图操作权限
	this.topologyMapOperation = new TopologyMapOperation();
}

TopologyMapObject.prototype = {
	
	// 使用  id , 以及 tagName 来创建一个element
	createElement: function (id , tagName){
		var element = document.createElement(tagName);
		element.id = id;
		return element;
	},
	
	// 加载 XML 方法
	loadXML:function(url) {
		this.initXML(url);
		this.parseData();
	},
	
	initXML: function(url) {
		var http = new ActiveXObject("Microsoft.XMLHTTP");
		this.xmldoc = new ActiveXObject("Microsoft.XMLDOM");
		http.open("POST", url, false);
		http.send();
		this.xmldoc.async = false;
		this.xmldoc.loadXML(http.responseText);
	},
	
	parseData: function() {
		// 分析nodes节点
		this.nodes = this.xmldoc.getElementsByTagName("node");
		var nodeNum = this.nodes.length;
		var i = 0;
		while (i < nodeNum) {
			var node = this.nodes[i];
			var topologyMapNode = this.createTopologyMapNode(node);
			this.topologyMapNodes.push(topologyMapNode);
			this.addTopologyMapNode(topologyMapNode);
			this.nodeobjArray.push(new nodeobj(topologyMapNode.imageElement.id,topologyMapNode.imageElement.src,topologyMapNode.nodeType,fatherXML, topologyMapNode.relationMap));
			i++;
		}
		// 分析lines节点
		this.lines = this.xmldoc.getElementsByTagName("line");
		var lineNum = this.lines.length;
		var j = 0;
		while (j < lineNum) {
			var lineObj = this.lines[j];
			var topologyMapLine = this.createTopologyMapLine(lineObj);
			this.topologyMapLines.push(topologyMapLine);
			this.addTopologyMapLine(topologyMapLine);
			this.linkobjArray.push(new linkobj(topologyMapLine.id,topologyMapLine.lineid,topologyMapLine.color,fatherXML,"line"));
			j++;
		}
		
		// 分析Assistantlines节点
		this.assistantlines = this.xmldoc.getElementsByTagName("assistant_line");
		var assistantlineNum = this.assistantlines.length;
		var k = 0;
		while (k < assistantlineNum) {
			var line = this.assistantlines[k];
			var topologyMapAssistantLine = this.createTopologyMapAssistantLine(line);
			this.topologyMapAssistantLines.push(topologyMapAssistantLine);
			this.addTopologyMapAssistantLine(topologyMapAssistantLine);
			this.linkobjArray.push(new linkobj(topologyMapAssistantLine.id,topologyMapAssistantLine.lineid,topologyMapAssistantLine.color,fatherXML,"assistant"));
			k++;
		}
		
		// 分析demoLines节点
		this.demoLines = this.xmldoc.getElementsByTagName("demoLine");
		var demolineNum = this.demoLines.length;
		var m = 0;
		while (m < demolineNum) {
			var line = this.demoLines[m];
			var topologyMapDemoLine = this.createTopologyMapDemoLine(line);
			this.topologyMapDemoLines.push(topologyMapDemoLine);
			this.addTopologyMapDemoLine(topologyMapDemoLine);
			this.linkobjArray.push(new linkobj(topologyMapDemoLine.id,topologyMapDemoLine.lineid,topologyMapDemoLine.color,fatherXML,"demo"));
			m++;
		}
		// zoomProcDlg("out");// 关闭闪屏
	},
	
	createTopologyMapNode: function(node) {
		var topologyMapNode = new TopologyMapNode();
		topologyMapNode.id = node.getElementsByTagName("id")[0].text;
		topologyMapNode.nodeType = node.getElementsByTagName("id")[0].getAttribute("category");
		topologyMapNode.imgValue = "../../resource/"+node.getElementsByTagName("img")[0].text;
		topologyMapNode.x = node.getElementsByTagName("x")[0].text;
		topologyMapNode.y = node.getElementsByTagName("y")[0].text;
		topologyMapNode.ip = node.getElementsByTagName("ip")[0].text;
		topologyMapNode.alias = node.getElementsByTagName("alias")[0].text;
		topologyMapNode.info = "正在获取数据";//node.getElementsByTagName("info")[0].text;
		//topologyMapNode.menu = node.getElementsByTagName("menu")[0].text;
		this.getTopologyMapNodeMenuData(topologyMapNode);
		topologyMapNode.relationMap = node.getElementsByTagName("relationMap")[0].text;
		
		return topologyMapNode
	},
	
	createTopologyMapLine: function(lineObj) {
		var topologyMapLine = new TopologyMapLine();
		topologyMapLine.lineid = lineObj.getAttribute("id");
		topologyMapLine.a = lineObj.getElementsByTagName("a")[0].text;
		topologyMapLine.b = lineObj.getElementsByTagName("b")[0].text;
		topologyMapLine.color = "green";
		topologyMapLine.dash = lineObj.getElementsByTagName("dash")[0].text;
		topologyMapLine.lineWidth = lineObj.getElementsByTagName("lineWidth")[0].text;
		topologyMapLine.lineInfo = "";//lineObj.getElementsByTagName("lineInfo")[0].text;
		this.getTopologyMapLineMenuData(topologyMapLine);
		topologyMapLine.id = "line_" + topologyMapLine.a + "_"+ topologyMapLine.b;
		return topologyMapLine;
	},

	createTopologyMapAssistantLine: function(lineObj) {
		var topologyMapLine = new TopologyMapLine();
		topologyMapLine.lineid = lineObj.getAttribute("id");
		topologyMapLine.a = lineObj.getElementsByTagName("a")[0].text;
		topologyMapLine.b = lineObj.getElementsByTagName("b")[0].text;
		topologyMapLine.color = "blue";
		topologyMapLine.dash = lineObj.getElementsByTagName("dash")[0].text;
		topologyMapLine.lineWidth = lineObj.getElementsByTagName("lineWidth")[0].text;
		topologyMapLine.lineInfo = "正在获取数据";//lineObj.getElementsByTagName("lineInfo")[0].text;
		this.getTopologyMapLineMenuData(topologyMapLine);
		topologyMapLine.id = "line_" + topologyMapLine.a + "_"+ topologyMapLine.b + "#assistant";
		return topologyMapLine;
	},
	
	createTopologyMapDemoLine: function(lineObj) {
		var topologyMapLine = new TopologyMapLine();
		topologyMapLine.lineid = lineObj.getAttribute("id");
		topologyMapLine.a = lineObj.getElementsByTagName("a")[0].text;
		topologyMapLine.b = lineObj.getElementsByTagName("b")[0].text;
		topologyMapLine.color = lineObj.getElementsByTagName("color")[0].text;
		topologyMapLine.dash = lineObj.getElementsByTagName("dash")[0].text;
		topologyMapLine.lineWidth = lineObj.getElementsByTagName("lineWidth")[0].text;
		topologyMapLine.lineInfo = "";//lineObj.getElementsByTagName("lineInfo")[0].text;
		this.getTopologyMapLineMenuData(topologyMapLine);
		topologyMapLine.id = "line_" + topologyMapLine.a + "_"+ topologyMapLine.b + "#demoline";
		return topologyMapLine;
	},
	
	// 创建拓扑图节点图片对象
	createTopologyMapNodeImg: function (topologyMapNode){
		var id = topologyMapNode.id;
		var nodeType = topologyMapNode.nodeType;
		var imgValue = topologyMapNode.imgValue;
		var x = topologyMapNode.x;
		var y = topologyMapNode.y;
		var info = topologyMapNode.info;
		var relationMap = topologyMapNode.relationMap;
		
		// 创建图片对象
		var imageElement = this.createElement("node_" + id, "v:image");
		if(info == "示意设备") { // yangjun add 区分示意设备
			imageElement.name = relationMap + ",1";// 节点关联的子图文件名和示意设备判断标记
		} else {
		    imageElement.name = relationMap + ",0";;// 节点关联的子图文件名和示意设备判断标记
		}
		imageElement.style.position = "absolute";
		imageElement.style.cursor = "hand";
		imageElement.style.left = x;
		imageElement.style.top = y;
		imageElement.src = imgValue;
		
		imageElement = this.setTopologyMapNodeSize(topologyMapNode, imageElement);
		return imageElement;
	},

	// 设置设备显示的大小
	setTopologyMapNodeSize: function(topologyMapNode, imageElement) {
		var nodeType = topologyMapNode.nodeType;
		var aliasHSpace = 0;
		var	aliasVSpace = 0;
		if(nodeType == "net_server") {
			imageElement.style.width = 65;
			imageElement.style.height = 26;
			aliasHSpace = 10;
			aliasVSpace = 26;			
		} else if(nodeType == "机柜") {
			imageElement.style.width = 95;
			imageElement.style.height = 371;
			aliasHSpace = 10;
			aliasVSpace = 371;
		} else if(nodeType == "标题") {
			imageElement.style.width = 10;
			imageElement.style.height = 10;
			aliasHSpace = 5;
			aliasVSpace = 5
		} else if(nodeType == "服务器") {
			imageElement.style.width = 65;
			imageElement.style.height = 26;
			aliasHSpace = 5;
			aliasVSpace = 20
		} else if(nodeType == "ups") {
			imageElement.style.width = 68;
			imageElement.style.height = 76;
			aliasHSpace = 10;
			aliasVSpace = 70
		} else if(nodeType == "weblogic") {
			imageElement.style.width = 32;
			imageElement.style.height = 21;
			aliasHSpace = 20;
			aliasVSpace = 22
		} else if(nodeType == "ftp") {
			imageElement.style.width = 32;
			imageElement.style.height = 32;
			aliasHSpace = 20;
			aliasVSpace = 33
		} else if(nodeType == "web") {
			imageElement.style.width = 32;
			imageElement.style.height = 32;
			aliasHSpace = 20;
			aliasVSpace = 34
		} else if(nodeType == "iis") {
			imageElement.style.width = 30;
			imageElement.style.height = 14;
			aliasHSpace = 30;
			aliasVSpace = 16
		} else if(nodeType == "mail") {
			imageElement.style.width = 30;
			imageElement.style.height = 20;
			aliasHSpace = 15;
			aliasVSpace = 21
		} else if(nodeType == "机柜") {
			imageElement.style.width = 95;
			imageElement.style.height = 371;
			aliasHSpace = 10;
			aliasVSpace = 371;
		} else if(nodeType == "标题") {
			imageElement.style.width = 10;
			imageElement.style.height = 10;
			aliasHSpace = 5;
			aliasVSpace = 5;
		} else if(nodeType == "子图") {
            imageElement.style.width = 64;
            imageElement.style.height = 64;
            aliasHSpace = 10;
            aliasVSpace = 64;
        } else {
		    imageElement.style.width = 30;
			imageElement.style.height = 30;
			aliasHSpace = 24;
			aliasVSpace = 28;
		}
		topologyMapNode.aliasHSpace = aliasHSpace;
		topologyMapNode.aliasVSpace = aliasVSpace;
		return imageElement;
	},
	
	// 显示设备信息
	showTopologyMapNodeInfo: function(topologyMapNode) {
		var id = topologyMapNode.id;
		var nodeType = topologyMapNode.nodeType;
		if (this.mouseDownStatus) {
			return false;
		}
		var divInfoElement = this.createTopologyMapNodeInfo(topologyMapNode);
		this.divInfoElement = divInfoElement;
		TopoRemoteService.getShowMessage(id, nodeType, fatherXML, {
			callback:function(data){
				if(data){
				    ObjectSelf.divInfoElement.innerHTML = data;
				    var divInfoElement = ObjectSelf.divInfoElement;
				    //#########增加“设备信息显示”时的越界处理，即超过边界的情况自动显示到可视范围之内   HONGLI ADD ########
			        if(divInfoElement.clientHeight != 0){
			        	var x = topologyMapNode.imageElement.style.left;
                        var y = topologyMapNode.imageElement.style.top;
			            if(parseInt(x, 10) > document.body.clientWidth - divInfoElement.clientWidth){
			                divInfoElement.style.left =parseInt(x, 10) - divInfoElement.clientWidth;
			            }
			            //alert(parseInt(y, 10));
			            //alert(document.body.clientHeight);
			            //alert(divInfoElement.clientHeight);
			            if(parseInt(y, 10) > document.body.clientHeight-divInfoElement.clientHeight){
			                divInfoElement.style.top = parseInt(y, 10)-divInfoElement.clientHeight;  
			            }
			        }
			        //#############HONG ADD END##########
				}
			}
		});
		this.divInfoElement.style.visibility = "visible"; 
	},
	
	// 隐藏设备信息
	hideTopologyMapNodeInfo: function(topologyMapNode) {
		this.divInfoElement.style.visibility = "hidden"; 
	},

	// 显示设备详细信息或跳转到关联子图
	showTopologyMapNodeDetail: function(topologyMapNode) {
		var id = topologyMapNode.id;
		var imageElement = topologyMapNode.imageElement;
		if(imageElement.name.substring(0, imageElement.name.lastIndexOf(",")) !="" ){
         	 if(imageElement.name.substring(0, imageElement.name.lastIndexOf(","))=="network.jsp"){
         	     window.parent.parent.location = "../network/index.jsp";
         	 }else{
         	     window.parent.parent.location = "../submap/index.jsp?submapXml=" + imageElement.name.substring(0, imageElement.name.lastIndexOf(","));
         	 }
         }else{
             showalert(id);
             window.parent.parent.opener.focus();
         }
	},

	// 创建拓扑图设备文本对象
	createTopologyMapNodeText: function(topologyMapNode) {
		var id = topologyMapNode.id;
		var x = topologyMapNode.x;
		var y = topologyMapNode.y;
		var ip = topologyMapNode.ip;
		var alias = topologyMapNode.alias;
		var aliasHSpace = topologyMapNode.aliasHSpace;
		var aliasVSpace = topologyMapNode.aliasVSpace;
		
		// 显示设备文本
		var divTextElement = this.createElement("text_" + id, "div");
		
		divTextElement.style.position = "absolute";
		divTextElement.style.width = parseInt(topologyMapNode.imageElement.style.width) + 2 * aliasHSpace;
		divTextElement.style.height = "20";
		divTextElement.style.left = parseInt(x, 10) - aliasHSpace;
		divTextElement.style.top = parseInt(y, 10) + aliasVSpace;
		divTextElement.style.fontSize = "12px";
		divTextElement.align = "center";
		if (g_viewFlag == 0) {
            divTextElement.innerHTML = alias;// 显示设备别名
        } else {
            divTextElement.innerHTML = ip;// 显示设备IP
        }
		return divTextElement;
	},
	
	// 创建拓扑图设备信息对象
	createTopologyMapNodeInfo: function(topologyMapNode) {
		var id = topologyMapNode.id;
		var x = topologyMapNode.imageElement.style.left;
		var y = topologyMapNode.imageElement.style.top;
		var info = topologyMapNode.info;
		// 鼠标移上显示设备信息
		if (this.divInfoElement == null) {
			this.divInfoElement = this.createElement("info_" + id, "div");
			this.divInfoElement.style.visibility = "hidden";
			this.divLayer.appendChild(this.divInfoElement);
		}
		var divInfoElement = this.divInfoElement;

        
		divInfoElement.name = id;// alias+"("+ip+")";//yangjun add
		divInfoElement.style.position = "absolute";
		divInfoElement.style.border = infoBorder;
		divInfoElement.style.width = infoWidth;
		divInfoElement.style.height = "auto";
		divInfoElement.style.color = infoFontColor;
		divInfoElement.style.padding = infoPadding;
		divInfoElement.style.lineHeight = "120%";
		divInfoElement.style.zIndex = 2;
		divInfoElement.style.backgroundColor = infoBgColor;
		divInfoElement.style.left = parseInt(x, 10) + parseInt(topologyMapNode.imageElement.style.width);
		divInfoElement.style.top = parseInt(y, 10);
		divInfoElement.style.visibility = "hidden";
		divInfoElement.style.fontSize = "12px";
		divInfoElement.innerHTML = info;
		
		return divInfoElement;
	},

	// 创建拓扑图设备菜单对象
	showTopologyMapNodeMenu: function(topologyMapNode) {
		var divMenuElement = this.createTopologyMapNodeMenu(topologyMapNode);
		this.divMenuElement = divMenuElement;
		if(clickObj != null) {
			this.divMenuElement.style.visibility = "hidden";
			clickObj = null;
		}	
		if(objStyle != null) {
		  	unSelectImg(objStyle);
		    objStyle = null;
		}
		clickObj = this.divMenuElement;
		objStyle = topologyMapNode.imageElement;
		selectImg(objStyle);
		if (this.divInfoElement != null) {
			this.divInfoElement.style.visibility = "hidden";
		}
		this.divMenuElement.style.visibility = "visible";
	},

	// 创建拓扑图设备菜单对象
	createTopologyMapNodeMenu: function(topologyMapNode) {
		var id = topologyMapNode.id;
		var x = topologyMapNode.imageElement.style.left;
		var y = topologyMapNode.imageElement.style.top;
		var menu = topologyMapNode.menu;
		// 拓扑图设备右键菜单对象
		if (this.divMenuElement == null) {
			this.divMenuElement = this.createElement("menu_" + id, "div");
			this.divMenuElement.style.visibility = "hidden";
			this.divMenuElement.onclick = (function () {
				return function() {
					ObjectSelf.divMenuElement.style.visibility = "hidden"; };
			})();
			this.divLayer.appendChild(this.divMenuElement);
		}
		var divMenuElement = this.divMenuElement;

		divMenuElement.style.position = "absolute";
		divMenuElement.style.width = 120;
		divMenuElement.style.height = "auto";
		divMenuElement.style.zIndex = 2;
		divMenuElement.style.left = parseInt(x, 10) + parseInt(topologyMapNode.imageElement.style.width);
        divMenuElement.style.top = parseInt(y, 10);
		divMenuElement.style.visibility = "hidden";
		divMenuElement.style.border = "solid #000066 1px";
		divMenuElement.style.backgroundColor = "#F5F5F5";
		divMenuElement.style.padding = "1px";
		divMenuElement.style.lineHeight = "100%";
		divMenuElement.style.fontSize = "12px";
		divMenuElement.innerHTML = menu;
		return divMenuElement;
	},

	// 隐藏拓扑图设备菜单对象
	hideTopologyMapNodeMenu: function() {
		if (this.divMenuElement) {
			this.divMenuElement.style.visibility = "hidden";
		}
	},

	// 添加拓扑图设备节点
	addTopologyMapNode: function(topologyMapNode) {
		var id = topologyMapNode.id;
		var nodeType = topologyMapNode.nodeType;
		var imgValue = topologyMapNode.imgValue;
		var x = topologyMapNode.x;
		var y = topologyMapNode.y;
		var ip = topologyMapNode.ip;
		var alias = topologyMapNode.alias;
		var info = topologyMapNode.info;
		var menu = topologyMapNode.menu;
		var relationMap = topologyMapNode.relationMap;

		//getConfine(x, y);		// 获取最大边界，给 maxWidth，maxHeight 赋值
		
		// 创建图片对象
		var imageElement = this.createTopologyMapNodeImg(topologyMapNode);
		topologyMapNode.imageElement = imageElement;
		
		this.divLayer.appendChild(imageElement);
	
		// nodeobjArray[nodeobjArray.length] = new nodeobj(img.id,img.src,nodeType,fatherXML);
		// 创建文本对象
		var divTextElement = this.createTopologyMapNodeText(topologyMapNode);
		topologyMapNode.divTextElement = divTextElement;

		this.divLayer.appendChild(divTextElement);
		
		// 创建信息对象
		// 注释原因：不在为每个设备创建Info信息 而是改成只创建一个，每次获取Info信息后对其信息修改
//		var divInfoElement = this.createTopologyMapNodeInfo(topologyMapNode);
//		topologyMapNode.divInfoElement = divInfoElement;
//		
//		document.all.divLayer.appendChild(divInfoElement);
		
		// 增加"设备信息显示"事件
		topologyMapNode.imageElement.onmouseover = (function(topologyMapNode) {
			return function () {
				ObjectSelf.showTopologyMapNodeInfo(topologyMapNode);
			};
		})(topologyMapNode);
		
		// 增加"设备信息隐藏"事件
		topologyMapNode.imageElement.onmouseout = (function(topologyMapNode) { 
			return function () {
				ObjectSelf.hideTopologyMapNodeInfo(topologyMapNode);
			};
		})(topologyMapNode);

		// 双击图元事件yangjun
		topologyMapNode.imageElement.ondblclick = (function(topologyMapNode) {
			return function () {
				ObjectSelf.showTopologyMapNodeDetail(topologyMapNode);
			};
		})(topologyMapNode);
		
		// 增加菜单的触发事件
		topologyMapNode.imageElement.oncontextmenu = (function(topologyMapNode) {
			return function () {
				ObjectSelf.showTopologyMapNodeMenu(topologyMapNode);
			};
		})(topologyMapNode);
		
		// 增加"拖动"事件
		topologyMapNode.imageElement.onmousedown = (function(topologyMapNode) {
			return function () {
				ObjectSelf.nodeDown(event, topologyMapNode);
			};
		})(topologyMapNode);
		document.onmousemove = null;
		topologyMapNode.imageElement.onmouseup = (function(topologyMapNode) {
			return function () {
				ObjectSelf.nodeUp(event, topologyMapNode);
			};
		})(topologyMapNode);
	},
	
	// 显示拓扑图链路信息对象
	showTopologyMapLineInfo: function(e, topologyMapLine) {
		var lineid = topologyMapLine.lineid;
		var divInfoElement = this.createTopologyMapLineInfo(topologyMapLine);
		this.divInfoElement = divInfoElement;
	   	LinkRemoteService.getShowMessage(lineid, {
			callback:function(data){
				if(data){
				    ObjectSelf.divInfoElement.innerHTML = data;
				}
			}
		});
		
		var getCoordInDocument = function(e) {// 获取鼠标当前位置
             e = e || window.event; 
             var x = e.pageX || (e.clientX + (document.documentElement.scrollLeft|| document.body.scrollLeft)); 
             var y= e.pageY || (e.clientY + (document.documentElement.scrollTop || document.body.scrollTop));   
             return {'x':x,'y':y}; 
        }
        
       	var pos = getCoordInDocument();
       	window.event.srcElement.style.cursor = "hand";
       	this.divInfoElement.style.left = parseInt(pos['x'])-parseInt(this.divLayer.style.left.replace("px",""));
       	this.divInfoElement.style.top = parseInt(pos['y'])-parseInt(this.divLayer.style.top.replace("px",""));
       	this.divInfoElement.style.visibility = "visible";
	},

	// 隐藏拓扑图链路信息对象
	hideTopologyMapLineInfo: function(topologyMapLine) {
		window.event.srcElement.style.cursor = "default"; 
		this.divInfoElement.style.visibility = "hidden";
	},

	// 创建拓扑图链路信息对象
	createTopologyMapLineInfo: function(topologyMapLine) {
		var id = topologyMapLine.id;
		var x = topologyMapLine.x;
		var y = topologyMapLine.y;
		var info = topologyMapLine.info;
		// 鼠标移上显示设备信息
		if (this.divInfoElement == null) {
			this.divInfoElement = this.createElement("info_" + id, "div");
			this.divInfoElement.style.visibility = "hidden";
			this.divLayer.appendChild(this.divInfoElement);
		}
		var divInfoElement = this.divInfoElement;

		divInfoElement.id = id
		divInfoElement.style.position = "absolute";
		divInfoElement.style.border = infoBorder;
		divInfoElement.style.width = 220;
		divInfoElement.style.height = "auto";
		divInfoElement.style.color = infoFontColor;
		divInfoElement.style.padding = infoPadding;
		divInfoElement.style.display = "block";
		divInfoElement.style.lineHeight = "120%";
		divInfoElement.style.zIndex = 2;
		divInfoElement.style.backgroundColor = infoBgColor;
		divInfoElement.style.visibility = "hidden";
		divInfoElement.style.fontSize = "12px";
		divInfoElement.innerHTML = info;
		
		//#########增加“设备信息显示”时的越界处理，即超过边界的情况自动显示到可视范围之内   HONGLI ADD ########
		if(divInfoElement.clientHeight != 0){
			if(parseInt(x, 10) > document.body.clientWidth - infoWidth){
				divInfoElement.style.left =parseInt(x, 10) - infoWidth;
			} 
			if(parseInt(y, 10) > document.body.clientHeight-divInfoElement.clientHeight){
				divInfoElement.style.top = parseInt(y, 10)-divInfoElement.clientHeight;  
		    }
		}
	    //#############HONG ADD END##########
		return divInfoElement;
	},

	// 显示拓扑图设备菜单对象
	showTopologyMapLineMenu: function(e, topologyMapLine) {
		var divMenuElement = this.createTopologyMapLineMenu(topologyMapLine);
		this.divMenuElement = divMenuElement;
		var getCoordInDocument = function(e) {// 获取鼠标当前位置
             e = e || window.event; 
             var x = e.pageX || (e.clientX + (document.documentElement.scrollLeft|| document.body.scrollLeft)); 
             var y= e.pageY || (e.clientY + (document.documentElement.scrollTop || document.body.scrollTop));   
             return {'x':x,'y':y}; 
        }
		var pos = getCoordInDocument();
		this.divMenuElement.style.left = parseInt(pos['x'])-parseInt(this.divLayer.style.left.replace("px",""));
	    this.divMenuElement.style.top = parseInt(pos['y'])-parseInt(this.divLayer.style.top.replace("px",""));
		if(clickLineObj != null)
		{
			this.divMenuElement.style.visibility = "hidden";
			clickLineObj = null;
		}
		clickLineObj = this.divMenuElement;
		this.divInfoElement.style.visibility = "hidden";
		this.divMenuElement.style.visibility = "visible";
	},
	
	// 创建拓扑图链路菜单对象
	createTopologyMapLineMenu: function(topologyMapLine) {
		var a = topologyMapLine.a;
		var b = topologyMapLine.b;
		var color = topologyMapLine.color;
		var dash = topologyMapLine.dash;
		var lineWidth = topologyMapLine.lineWidth;
		var lineInfo = topologyMapLine.lineInfo;
		var lineMenu = topologyMapLine.lineMenu;
		var id = topologyMapLine.id;
		
		// 拓扑图设备右键菜单对象
		if (this.divMenuElement == null) {
			this.divMenuElement = this.createElement("menu_" + id, "div");
			this.divMenuElement.style.visibility = "hidden";
			this.divMenuElement.onclick = (function () {
				return function() { ObjectSelf.divMenuElement.style.visibility = "hidden"; };
			})();
			this.divLayer.appendChild(this.divMenuElement);
		}
		var divMenuElement = this.divMenuElement;

		divMenuElement.style.position = "absolute";
		divMenuElement.style.width = 120;
		divMenuElement.style.height = "auto";
		divMenuElement.style.zIndex = 2;
		divMenuElement.style.visibility = "hidden";
		divMenuElement.style.border = "solid #000066 1px";
		divMenuElement.style.backgroundColor = "#F5F5F5";
		divMenuElement.style.padding = "1px";
		divMenuElement.style.lineHeight = "100%";
		divMenuElement.style.fontSize = "12px";
		divMenuElement.innerHTML = lineMenu;

		return divMenuElement;
	},

	// 添加拓扑图链路节点
	addTopologyMapLine: function(topologyMapLine) {
		var a = topologyMapLine.a;
		var b = topologyMapLine.b;
		var color = topologyMapLine.color;
		var dash = topologyMapLine.dash;
		var lineWidth = topologyMapLine.lineWidth;
		var lineInfo = topologyMapLine.lineInfo;
		var lineMenu = topologyMapLine.lineMenu;
		var id = topologyMapLine.id;
         
		var lineElement = document.createElement("v:line");
		lineElement.id = id;
		lineElement.style.position = "absolute";
		lineElement.style.zIndex = -1;
		lineElement.from = (parseInt(document.all("node_" + a).style.left) + parseInt(document.all("node_" + a).style.width)/2) + "," + (parseInt(document.all("node_" + a).style.top) + parseInt(document.all("node_" + a).style.height)/2);
		lineElement.to = (parseInt(document.all("node_" + b).style.left) + parseInt(document.all("node_" + b).style.width)/2) + "," + (parseInt(document.all("node_" + b).style.top) + parseInt(document.all("node_" + b).style.height)/2);
		lineElement.strokecolor = color;
		lineElement.strokeweight = lineWidth;// 1;
		
		topologyMapLine.lineElement = lineElement;
		// 显示链路信息
		topologyMapLine.lineElement.onmousemove = (function(topologyMapLine){
				return function(e) {
					ObjectSelf.showTopologyMapLineInfo(e, topologyMapLine);
       			}; 
		})(topologyMapLine);
		topologyMapLine.lineElement.onmouseout = (function(topologyMapLine){
				return function(e) {
					ObjectSelf.hideTopologyMapLineInfo(topologyMapLine);
       			}; 
		})(topologyMapLine);

		this.divLayer.appendChild(topologyMapLine.lineElement);
		
		document.all("node_" + a).lines += '&' + lineElement.id;
		document.all("node_" + b).lines += '&' + lineElement.id;
		
		// 增加链路菜单的触发事件
		topologyMapLine.lineElement.oncontextmenu = (function(topologyMapLine){
				return function(e) {
					ObjectSelf.showTopologyMapLineMenu(e, topologyMapLine);
       			};
		})(topologyMapLine);
		
		// 加入 stroke 标签
		// window.event.srcElement.stroke.dashstyle = "Solid";
		var stroke = document.createElement("v:stroke");
		stroke.dashstyle = dash;
		topologyMapLine.lineElement.appendChild(stroke);
	},
	
	/**
	 * ========================= 双链路 ===========================================
	 */
	
	// 显示拓扑图链路信息对象
	showTopologyMapAssistantLineInfo: function(e, topologyMapLine) {
		var lineid = topologyMapLine.lineid;
		var divInfoElement = this.createTopologyMapLineInfo(topologyMapLine);
		this.divInfoElement = divInfoElement;
	   	LinkRemoteService.getShowMessage(lineid, {
			callback:function(data){
				if(data){
				    divInfoElement.innerHTML = data;
				}
			}
		});
		
		var getCoordInDocument = function(e) {// 获取鼠标当前位置
             e = e || window.event; 
             var x = e.pageX || (e.clientX + (document.documentElement.scrollLeft|| document.body.scrollLeft)); 
             var y= e.pageY || (e.clientY + (document.documentElement.scrollTop || document.body.scrollTop));   
             return {'x':x,'y':y}; 
        }
        
       	var pos = getCoordInDocument();
       	window.event.srcElement.style.cursor = "hand";
       	this.divInfoElement.style.left = parseInt(pos['x'])-parseInt(document.all.divLayer.style.left.replace("px",""));
       	this.divInfoElement.style.top = parseInt(pos['y'])-parseInt(document.all.divLayer.style.top.replace("px",""));
       	this.divInfoElement.style.visibility = "visible";
	},

	// 隐藏拓扑图链路信息对象
	hideTopologyMapAssistantLineInfo: function(topologyMapLine) {
		window.event.srcElement.style.cursor = "default"; 
		this.divInfoElement.style.visibility = "hidden";
	},

	// 创建拓扑图链路信息对象
	createTopologyMapAssistantLineInfo: function(topologyMapLine) {
		var id = topologyMapLine.id;
		var x = topologyMapLine.x;
		var y = topologyMapLine.y;
		var info = topologyMapLine.info;
		// 鼠标移上显示设备信息
		if (this.divInfoElement == null) {
			this.divInfoElement = this.createElement("info_" + id, "div");
			this.divInfoElement.style.visibility = "hidden";
			document.all.divLayer.appendChild(this.divInfoElement);
		}
		var divInfoElement = this.divInfoElement;

		divInfoElement.style.position = "absolute";
		divInfoElement.style.border = infoBorder;
		divInfoElement.style.width = 200;
		divInfoElement.style.height = "auto";
		divInfoElement.style.color = infoFontColor;
		divInfoElement.style.padding = infoPadding;
		divInfoElement.style.display = "block";
		divInfoElement.style.lineHeight = "120%";
		divInfoElement.style.zIndex = 2;
		divInfoElement.style.backgroundColor = infoBgColor;
		divInfoElement.style.visibility = "hidden";
		divInfoElement.style.fontSize = "12px";
		divInfoElement.innerHTML = lineInfo;
		
		//#########增加“设备信息显示”时的越界处理，即超过边界的情况自动显示到可视范围之内   HONGLI ADD ########
		if(divInfoElement.clientHeight != 0){
			if(parseInt(x, 10) > document.body.clientWidth - infoWidth){
				divInfoElement.style.left =parseInt(x, 10) - infoWidth;
			} 
			if(parseInt(y, 10) > document.body.clientHeight-divInfoElement.clientHeight){
				divInfoElement.style.top = parseInt(y, 10)-divInfoElement.clientHeight;  
		    }
		}
	    //#############HONG ADD END##########
		return divInfoElement;
	},
	
	// 添加拓扑图链路节点
	addTopologyMapAssistantLine: function(topologyMapLine) {
		//linkobjArray[linkobjArray.length] = new linkobj(line.id,line.lineid,color,fatherXML,"assistant");
		// ///////////////yangjun add begin
		var a = topologyMapLine.a;
		var b = topologyMapLine.b;
		var color = topologyMapLine.color;
		var dash = topologyMapLine.dash;
		var lineWidth = topologyMapLine.lineWidth;
		var lineInfo = topologyMapLine.lineInfo;
		var lineMenu = topologyMapLine.lineMenu;
		var id = topologyMapLine.id;
         
		var lineElement = document.createElement("v:line");
		lineElement.id = id;
		lineElement.style.position = "absolute";
		lineElement.style.zIndex = -1;
		//line.from = (parseInt(document.all("node_" + a).style.left) + 20) + "," + (parseInt(document.all("node_" + a).style.top) + 10);
		//line.to = (parseInt(document.all("node_" + b).style.left) + 20) + "," + (parseInt(document.all("node_" + b).style.top) + 10);
		
		var leftA = 0;
		var topA = 0;
		
		if (this.topologyMapAssistantLines) {
			var topologyMapAssistantLinesLength = this.topologyMapAssistantLines.length;
			for (var i = 0; i < topologyMapAssistantLinesLength; i++) {
				var topologyMapAssistantLine = this.topologyMapAssistantLines[i];
				if (topologyMapAssistantLine.a == a && topologyMapAssistantLine.b == b) {
					leftA += 6;
					topA += 6;
				} else if (topologyMapAssistantLine.a == b && topologyMapAssistantLine.b == a) {
					leftA += 6;
					topA += 6;
				}
			}
		}
		lineElement.from = (parseInt(document.all("node_" + a).style.left) + parseInt(document.all("node_" + a).style.width)/2 + leftA) + "," + (parseInt(document.all("node_" + a).style.top) + parseInt(document.all("node_" + a).style.height)/2 + topA);
		lineElement.to = (parseInt(document.all("node_" + b).style.left) + parseInt(document.all("node_" + b).style.width)/2 + leftA) + "," + (parseInt(document.all("node_" + b).style.top) + parseInt(document.all("node_" + b).style.height)/2 + topA);
		
		lineElement.strokecolor = color;
		lineElement.strokeweight = lineWidth;
		
		topologyMapLine.lineElement = lineElement;
		
		// 显示链路信息
		topologyMapLine.lineElement.onmousemove = (function(topologyMapLine){
				return function(e) {
					ObjectSelf.showTopologyMapLineInfo(e, topologyMapLine);
       			}; 
		})(topologyMapLine);
		topologyMapLine.lineElement.onmouseout = (function(topologyMapLine){
				return function(e) {
					ObjectSelf.hideTopologyMapLineInfo(topologyMapLine);
       			}; 
		})(topologyMapLine);

		document.all.divLayer.appendChild(topologyMapLine.lineElement);
		
		document.all("node_" + a).lines += '&' + lineElement.id;
		document.all("node_" + b).lines += '&' + lineElement.id;
		
		// 增加链路菜单的触发事件
		topologyMapLine.lineElement.oncontextmenu = (function(topologyMapLine){
				return function(e) {
					ObjectSelf.showTopologyMapLineMenu(e, topologyMapLine);
       			};
		})(topologyMapLine);
		
		// 加入 stroke 标签
		// window.event.srcElement.stroke.dashstyle = "Solid";
		var stroke = document.createElement("v:stroke");
		stroke.dashstyle = dash;
		topologyMapLine.lineElement.appendChild(stroke);
	},
	
	
	/**
	 * ========================= 示意链路 ===========================================
	 */
	
	// 添加拓扑图示意链路节点
	addTopologyMapDemoLine: function(topologyMapLine) {
		//linkobjArray[linkobjArray.length] = new linkobj(line.id,line.lineid,color,fatherXML,"assistant");
		// ///////////////yangjun add begin
		var a = topologyMapLine.a;
		var b = topologyMapLine.b;
		var color = topologyMapLine.color;
		var dash = topologyMapLine.dash;
		var lineWidth = topologyMapLine.lineWidth;
		var lineInfo = topologyMapLine.lineInfo;
		var lineMenu = topologyMapLine.lineMenu;
		var id = topologyMapLine.id;
         
		var lineElement = document.createElement("v:line");
		lineElement.id = id;
		lineElement.style.position = "absolute";
		lineElement.style.zIndex = -1;
		lineElement.from = (parseInt(document.all("node_" + a).style.left) + parseInt(document.all("node_" + a).style.width)/2) + "," + (parseInt(document.all("node_" + a).style.top) + parseInt(document.all("node_" + a).style.height)/2);
		lineElement.to = (parseInt(document.all("node_" + b).style.left) + parseInt(document.all("node_" + b).style.width)/2) + "," + (parseInt(document.all("node_" + b).style.top) + parseInt(document.all("node_" + b).style.height)/2);
		lineElement.strokecolor = color;
		lineElement.strokeweight = lineWidth;
		
		topologyMapLine.lineElement = lineElement;
		
		// 显示链路信息
		topologyMapLine.lineElement.onmousemove = (function(topologyMapLine){
				return function(e) {
					ObjectSelf.showTopologyMapLineInfo(e, topologyMapLine);
       			}; 
		})(topologyMapLine);
		topologyMapLine.lineElement.onmouseout = (function(topologyMapLine){
				return function(e) {
					ObjectSelf.hideTopologyMapLineInfo(topologyMapLine);
       			}; 
		})(topologyMapLine);

		document.all.divLayer.appendChild(topologyMapLine.lineElement);
		
		document.all("node_" + a).lines += '&' + lineElement.id;
		document.all("node_" + b).lines += '&' + lineElement.id;
		
		// 增加链路菜单的触发事件
		topologyMapLine.lineElement.oncontextmenu = (function(topologyMapLine){
				return function(e) {
					ObjectSelf.showTopologyMapLineMenu(e, topologyMapLine);
       			};
		})(topologyMapLine);
		
		// 加入 stroke 标签
		// window.event.srcElement.stroke.dashstyle = "Solid";
		var stroke = document.createElement("v:stroke");
		stroke.dashstyle = dash;
		topologyMapLine.lineElement.appendChild(stroke);
	},
	
	//yangjun add 按照id查找设备节点
	getNodeId: function(id) {
		var nodeNum = this.topologyMapNodes.length;
		var i = 0;
		while (i < nodeNum) {
			var topologyMapNode = this.topologyMapNodes[i];
			if (topologyMapNode.id == id) {
				var coor = new Array();
				coor[0] = topologyMapNode.imageElement.style.left;
				coor[1] = topologyMapNode.imageElement.style.top;
				return coor;
			}
			i = i + 1;
		}
		return null;
	},
	
	//yangjun add 按照ip查找设备节点
	getNodeIP: function(ip) {
		var nodeNum = this.topologyMapNodes.length;
		var i = 0;
		while (i < nodeNum) {
			var topologyMapNode = this.topologyMapNodes[i];
			if (topologyMapNode.ip == ip) {
				var coor = new Array();
				coor[0] = topologyMapNode.imageElement.style.left;
				coor[1] = topologyMapNode.imageElement.style.top;
				return coor;
			}
			i = i + 1;
		}
		return null;
	},

	addNode: function (nodeid, url) {
		this.initXML(url);
		this.nodes = this.xmldoc.getElementsByTagName("node");
		var nodeNum = this.nodes.length;
		var i = 0;
		while (i < nodeNum) {
			var node = this.nodes[i];
			var id = node.getElementsByTagName("id")[0].text;
			if (id == nodeid) {
				var topologyMapNode = this.createTopologyMapNode(node);
				this.topologyMapNodes.push(topologyMapNode);
				this.addTopologyMapNode(topologyMapNode);
				this.nodeobjArray.push(new nodeobj(topologyMapNode.imageElement.id,topologyMapNode.imageElement.src,topologyMapNode.nodeType,fatherXML));
			}
			i = i + 1;
		}
	},
	
	addlink: function (lineid, url) {
		this.initXML(url);
		this.lines = this.xmldoc.getElementsByTagName("line");
		var lineNum = this.lines.length;
		var i = 0;
		while (i < lineNum) {
			var line = this.lines[i];
			var line_id = line.getAttribute("id");
			if (lineid == line_id) {
				var topologyMapLine = this.createTopologyMapLine(line);
				this.topologyMapLines.push(topologyMapLine);
				this.addTopologyMapLine(topologyMapLine);
				this.linkobjArray.push(new linkobj(topologyMapLine.id,topologyMapLine.lineid,topologyMapLine.color,fatherXML,"line"));
			}
			i = i + 1;
		}
	},

	addAssLink: function (lineid, url) {
		this.initXML(url);
		this.assistantlines = this.xmldoc.getElementsByTagName("assistant_line");
		var lineNum = this.assistantlines.length;
		var i = 0;
		while (i < lineNum) {
			var assistantline = this.assistantlines[i];
			var line_id = assistantline.getAttribute("id");
			if (lineid == line_id) {
				var topologyMapAssistantLine = this.createTopologyMapAssistantLine(assistantline);
				this.topologyMapAssistantLines.push(topologyMapAssistantLine);
				this.addTopologyMapAssistantLine(topologyMapAssistantLine);
				this.linkobjArray.push(new linkobj(topologyMapAssistantLine.id,topologyMapAssistantLine.lineid,topologyMapAssistantLine.color,fatherXML,"assistant"));
			}
			i = i + 1;
		}
	},
	
	addLine: function (lineid, url) {
		this.initXML(url);
		this.demoLines = this.xmldoc.getElementsByTagName("demoLine");
		var lineNum = this.demoLines.length;
		var i = 0;
		while (i < lineNum) {
			var demoLine = this.demoLines[i];
			var line_id = demoLine.getAttribute("id");
			if (lineid == line_id) {
				var topologyMapDemoLine = this.createTopologyMapDemoLine(demoLine);
				this.topologyMapDemoLines.push(topologyMapDemoLine);
				this.addTopologyMapDemoLine(topologyMapDemoLine);
				this.linkobjArray.push(new linkobj(topologyMapDemoLine.id,topologyMapDemoLine.lineid,topologyMapDemoLine.color,fatherXML,"demo"));
			}
			i = i + 1;
		}
	},
	
	save: function() {
		if(document.all("containImgDiv")) {
			rmvContainedImg();
		}
		var nodes = this.xmldoc.getElementsByTagName("node");
		for (var i = 0; i < this.topologyMapNodes.length; i += 1)
		{
			for (var j = 0; j < nodes.length; j += 1)
			{
				var node = nodes[j];
				var id = "node_" + node.getElementsByTagName("id")[0].text;
				if (this.topologyMapNodes[i].imageElement.id == id)
				{
					node.getElementsByTagName("x")[0].text = this.topologyMapNodes[i].imageElement.style.left;
					node.getElementsByTagName("y")[0].text = this.topologyMapNodes[i].imageElement.style.top;
				}
			}
		}
		document.all("hidXml").value = this.xmldoc.xml;
		document.frmMap.submit();
	},
	
	replaceNodeobj: function(data){
	    if(data!=null&&data.length>0){
	    	for(var i = 0; i < data.length; i++){
	    		for(var j = 0; j < this.topologyMapNodes.length; j++) {
	    			if (data[i].id == this.topologyMapNodes[j].imageElement.id) {
	    				this.topologyMapNodes[j].imageElement.src = "../../resource/"+data[i].image;
	    			}
	    		}
	    	}
	    }
	},

	replaceLinkobj: function(data){
	    if(data!=null){
	    	for(var i = 0; i < data.length; i++){
	    		for(var j = 0; j < this.topologyMapLines.length; j++) {
	    			if (data[i].id == this.topologyMapLines[j].lineElement.id) {
	    				this.topologyMapLines[j].lineElement.strokecolor = data[i].color;
	    			}
	    		}
	    		for(var j = 0; j < this.topologyMapAssistantLines.length; j++) {
	    			if (data[i].id == this.topologyMapAssistantLines[j].lineElement.id) {
	    				this.topologyMapAssistantLines[j].lineElement.strokecolor = data[i].color;
	    			}
	    		}
	    		for(var j = 0; j < this.topologyMapDemoLines.length; j++) {
	    			if (data[i].id == this.topologyMapDemoLines[j].lineElement.id) {
	    				this.topologyMapDemoLines[j].lineElement.strokecolor = data[i].color;
	    			}
	    		}
	    	}
	    }
	},
	
	// handle node onmousedown event, 处理设备按住鼠标事件
	nodeDown: function(evnet, topologyMapNode) {
		if (event.button != 1) {
			// 禁用右键拖动
			return false;
		}
		if (this.divMenuElement) {
			this.divMenuElement.style.visibility = "hidden";
		}
		if (this.divInfoElement) {
            this.divInfoElement.style.visibility = "hidden";
        }
		if (!this.selectedTopologyMapNodes) {
			this.selectedTopologyMapNodes = new Array();
		}
		var isSelected = topologyMapNode.isSelected;
		if (!isSelected && !event.ctrlKey) {
			this.cancelAllSelectedNode();
		}
		this.addSelectedNode(topologyMapNode);
		topologyMapNode.mouseDownStatus = true;
        this.mouseDownStatus = true;
		this.mouseOriX = event.x;
		this.mouseOriY = event.y;
		
		// 移动设备之前重新设置在移动之前的 X Y 位置
		var isSelectedNum = this.selectedTopologyMapNodes.length;
		var i = 0;
		while (i < isSelectedNum) {
			var isSelectedTopologyMapNode = this.selectedTopologyMapNodes[i];
			var beforeMoveX = isSelectedTopologyMapNode.imageElement.offsetLeft;
			var beforeMoveY = isSelectedTopologyMapNode.imageElement.offsetTop;
			isSelectedTopologyMapNode.beforeMoveX = beforeMoveX;
			isSelectedTopologyMapNode.beforeMoveY = beforeMoveY;
			i++;
		}
		// 增加链路菜单的触发事件
		document.onmousemove = (function(){
				return function(e) {
					var isSelectedNum = ObjectSelf.selectedTopologyMapNodes.length;
					var j = 0;
					while (j < isSelectedNum) {
						var isSelectedTopologyMapNode = ObjectSelf.selectedTopologyMapNodes[j];
						ObjectSelf.nodeMove(e, isSelectedTopologyMapNode);
						j++;
					}
       			};
		})();
	},

	// handle node onmousemove event, 处理设备按住鼠标移动事件
	nodeMove: function(event, topologyMapNode) {
		var eventX = window.event.x;
		var eventY = window.event.y;
		if (eventX > 0 & eventY > 0 & eventX < window.screen.width & eventY < window.screen.height) {
			var imageStyle = topologyMapNode.imageElement.style;
			var textStyle = topologyMapNode.divTextElement.style;
			var beforeMoveX = topologyMapNode.beforeMoveX;
			var beforeMoveY = topologyMapNode.beforeMoveY;
			var tempX = eventX - this.mouseOriX + beforeMoveX;
			var tempY = eventY - this.mouseOriY + beforeMoveY;
			imageStyle.left = tempX ;
			imageStyle.top = tempY ;
			textStyle.left = tempX - topologyMapNode.aliasHSpace;
			textStyle.top = tempY + topologyMapNode.aliasVSpace;
			
	   		// #############HONG ADD END##########
	   		// ############# 调整链路位置 ########
	   		var iLeft = parseInt(imageStyle.left) + parseInt(imageStyle.width)/2;
			var iTop = parseInt(imageStyle.top) + parseInt(imageStyle.height)/2;
			var lineNum = this.topologyMapLines.length;
			var i = 0;
			while (i < lineNum) {
				var topologyMapLine = this.topologyMapLines[i];
				if (topologyMapLine.a == topologyMapNode.id) {
					topologyMapLine.lineElement.from = iLeft + "," + iTop;
				}
				if (topologyMapLine.b == topologyMapNode.id) {
					topologyMapLine.lineElement.to = iLeft + "," + iTop;
				}
				i = i + 1;
			}
			
			var assistantlineNum = this.topologyMapAssistantLines.length;
			var j = 0;
			while (j < assistantlineNum) {
				var topologyMapAssistantLine = this.topologyMapAssistantLines[j];
				if (topologyMapAssistantLine.a == topologyMapNode.id) {
					topologyMapAssistantLine.lineElement.from = (parseInt(iLeft)+5) + "," + iTop;
				}
				if (topologyMapAssistantLine.b == topologyMapNode.id) {
					topologyMapAssistantLine.lineElement.to = (parseInt(iLeft)+5) + "," + iTop;
				}
				j = j + 1;
			}
			
			var demoLineNum = this.topologyMapDemoLines.length;
			var k = 0;
			while (k < demoLineNum) {
				var topologyMapDemoLine = this.topologyMapDemoLines[k];
				if (topologyMapDemoLine.a == topologyMapNode.id) {
					topologyMapDemoLine.lineElement.from = (parseInt(iLeft)) + "," + iTop;
				}
				if (topologyMapDemoLine.b == topologyMapNode.id) {
					topologyMapDemoLine.lineElement.to = (parseInt(iLeft)) + "," + iTop;
				}
				k = k + 1;
			}
		}
	},
	
	// handle node onmouseup event, 处理设备放开鼠标事件
	nodeUp: function(event, topologyMapNode) {
		topologyMapNode.mouseDownStatus = false;
		this.mouseDownStatus = false;
		topologyMapNode.imageElement.onmousemove = null;
		this.handleOnMouseUp();
		this.hideTopologyMapNodeMenu();
		return false;
	},
	
	// handle body onmousedown event, 处理页面按住鼠标事件
	bodyDown: function () {
		if (window.event.srcElement.id == "divLayer") {
			this.cancelAllSelectedNode();
			this.hideTopologyMapNodeMenu();
		}
	},
	
	// handle body onmousedown event, 处理页面按住鼠标事件
	divLayerDown: function () {
		if (window.event.srcElement.tagName == "BODY") {
			this.cancelAllSelectedNode();
			this.hideTopologyMapNodeMenu();
		}
	},
	
	// 处理选框
	selectStart: function() {
		if (window.event.srcElement.id == "divLayer" || 
			window.event.srcElement.tagName == "BODY") {
			this.hideTopologyMapNodeMenu();
			// 画框选择时，用的上下左右四根彩线
			var imgTop = this.imgTop;
			var imgLeft = this.imgLeft;
			var imgBottom = this.imgBottom;
			var imgRight = this.imgRight;
			
			imgTop.style.height = imgTop.style.width = imgLeft.style.height = imgLeft.style.width = imgBottom.style.height = imgBottom.style.width = imgRight.style.height = imgRight.style.width = 1;
			imgTop.style.visibility = imgLeft.style.visibility = imgBottom.style.visibility = imgRight.style.visibility = "visible";
			imgTop.style.zIndex = imgLeft.style.zIndex = imgBottom.style.zIndex = imgRight.style.zIndex = 9999;
			this.beforeMoveX = event.x;
			this.beforeMoveY = event.y;
			document.onmousemove = (function () {
				return function(e){
					ObjectSelf.selectMove(e);
				};
			})();
			document.onmouseup = (function () {
				return function(e){
					ObjectSelf.selectUp(e);
				};
			})();
		}
	},
	
	// 处理选框移动事件
	selectMove: function(event) {
		// 画框选择时，用的上下左右四根彩线
		var imgTop = this.imgTop;
		var imgLeft = this.imgLeft;
		var imgBottom = this.imgBottom;
		var imgRight = this.imgRight;
		var tmpx = window.event.x;
		var tmpy = window.event.y;
		var beginx = this.beforeMoveX;
		var beginy = this.beforeMoveY;
		imgTop.style.left = Math.min(beginx,tmpx);
		imgTop.style.top = Math.min(beginy,tmpy);
		imgTop.style.width = Math.abs(beginx - tmpx);
		imgLeft.style.left = Math.min(beginx,tmpx);
		imgLeft.style.top = Math.min(beginy,tmpy);
		imgLeft.style.height = Math.abs(beginy - tmpy);
		imgBottom.style.left = Math.min(beginx,tmpx);
		imgBottom.style.top = Math.max(beginy,tmpy);
		imgBottom.style.width = Math.abs(beginx - tmpx);
		imgRight.style.left = Math.max(beginx,tmpx);
		imgRight.style.top = Math.min(beginy,tmpy);
		imgRight.style.height = Math.abs(beginy - tmpy);
	},
	
	selectUp: function(event) {
		this.cancelAllSelectedNode();
		var nodeNum = this.topologyMapNodes.length;
		var i = 0;
		while (i < nodeNum) {
			var topologyMapNode = this.topologyMapNodes[i];
			var isInSelect = this.checkNodeIsInSelect(topologyMapNode);
			if (isInSelect) {
				this.addSelectedNode(topologyMapNode);
			}
			i++;
		}
		var imgTop = this.imgTop;
		var imgLeft = this.imgLeft;
		var imgBottom = this.imgBottom;
		var imgRight = this.imgRight;
		imgTop.style.visibility = "hidden";
		imgLeft.style.visibility = "hidden";
		imgBottom.style.visibility = "hidden";
		imgRight.style.visibility = "hidden";
		this.handleOnMouseUp();
	},
	
	// handle onmouseup event 处理放开鼠标事件
	handleOnMouseUp: function() {
		document.onmouseup = null;
		document.onmousemove = null;
	},
	
	// 判断设备是否在选框之中
	checkNodeIsInSelect: function(topologyMapNode) {
		var divLayStyleLeft = this.divLayer.style.left;
		var divLayStyleTop = this.divLayer.style.top;
		var nodeStyleLeft = topologyMapNode.imageElement.style.left;
		var nodeStyleTop = topologyMapNode.imageElement.style.top;
		var nodeStyleWith = topologyMapNode.imageElement.style.width;
		var nodeStyleHeight = topologyMapNode.imageElement.style.height;
		// 画框选择时，用的上下左右四根彩线
		var imgTop = this.imgTop;
		var imgLeft = this.imgLeft;
		var imgBottom = this.imgBottom;
		var imgRight = this.imgRight;
		imgTopX = parseInt(imgTop.style.left);
		imgTopY = parseInt(imgTop.style.top);
		imgBottomX = parseInt(imgTop.style.left) + parseInt(imgTop.style.width);
		imgBottomY = parseInt(imgTop.style.top) + parseInt(imgLeft.style.height);
		
		var result = parseInt(nodeStyleLeft) + parseInt(divLayStyleLeft) >= imgTopX;
		var result = result && (parseInt(nodeStyleLeft) + parseInt(divLayStyleLeft) <= imgBottomX);
		var result = result && (parseInt(nodeStyleTop) + parseInt(divLayStyleTop) >= imgTopY);
		var result = result && (parseInt(nodeStyleTop) + parseInt(divLayStyleTop) <= imgBottomY);
		var result = result && (parseInt(nodeStyleLeft) + parseInt(nodeStyleWith) + parseInt(divLayStyleLeft) <= imgBottomX);
		var result = result && (parseInt(nodeStyleTop) + parseInt(nodeStyleHeight) + parseInt(divLayStyleTop) <= imgBottomY);
		return result;
	},

	getAllSelectedNodes: function() {
		return this.selectedTopologyMapNodes;
	},
	
	// 添加选择的设备
	addSelectedNode: function (topologyMapNode) {
		var isSelected = topologyMapNode.isSelected;
		if (isSelected) {
			return false;
		}
		this.setSelectedNodeStyle(topologyMapNode);
		topologyMapNode.isSelected = true;
		this.setSelectedNodeStyle(topologyMapNode);
		this.selectedTopologyMapNodes.push(topologyMapNode);
		return true;
	},

	// 取消所有选择的设备
	cancelAllSelectedNode: function () {
		while (this.selectedTopologyMapNodes.length > 0) {
			var isSelectedTopologyMapNode = this.selectedTopologyMapNodes.pop();
			isSelectedTopologyMapNode.isSelected = false;
			this.setUnSelectedNodeStyle(isSelectedTopologyMapNode);
		}
	},

	// 设置选中节点后的样式
	setSelectedNodeStyle: function (topologyMapNode) {
		topologyMapNode.imageElement.style.background = "#9BFFAC";
		topologyMapNode.imageElement.style.border="1px solid #007314";
		topologyMapNode.imageElement.style.filter="Alpha(Opacity=60);";
	},

	// 取消选中节点的样式
	setUnSelectedNodeStyle: function (topologyMapNode) {
		topologyMapNode.imageElement.style.background = "";
		topologyMapNode.imageElement.style.border = "0px solid #007314";
		topologyMapNode.imageElement.style.color = "#000000";
		topologyMapNode.imageElement.style.filter= "";
	},
	
	//右键菜单
	getTopologyMapNodeMenuData: function (topologyMapNode) {
		topologyMapNode.menu = "";
		if (topologyMapNode.id.indexOf("hin") < 0) {
			//实体图元
			topologyMapNode.menu = '<a class="ping_menu_out" onmouseover="pingMenuOver();" onmouseout="pingMenuOut();" onclick="javascript:resetProcDlg();window.showModelessDialog(\'/afunms/tool/ping.jsp?ipaddress=' + topologyMapNode.ip + '\', window, \'dialogHeight:500px;dialogWidth:500px;status:0;help:0;edge:sunken;center:yes;scroll:0\');timingCloseProDlg(8000);" title="ping ' + topologyMapNode.ip + '">&nbsp;&nbsp;&nbsp;&nbsp;Ping </a><br/>';
			topologyMapNode.menu +='<a class="download_menu_out" onmouseover="downloadMenuOver();" onmouseout="downloadMenuOut();" onclick="javascript:window.open(\'/afunms/topology/network/download.jsp?flag=0&xml=' + this.xmlName + '\',\'window\', \'toolbar=no,height=620,width=820,scrollbars=yes,center=yes,screenY=0\')">&nbsp;&nbsp;&nbsp;&nbsp;生成别名拓扑图 </a><br/>';
			topologyMapNode.menu +='<a class="download_menu_out" onmouseover="downloadMenuOver();" onmouseout="downloadMenuOut();" onclick="javascript:window.open(\'/afunms/topology/network/download.jsp?flag=1&xml=' + this.xmlName + '\',\'window\', \'toolbar=no,height=620,width=820,scrollbars=yes,center=yes,screenY=0\')">&nbsp;&nbsp;&nbsp;&nbsp;生成IP拓扑图 </a><br/>';
			//topologyMapNode.menu +='<a class="download_menu_out" onmouseover="downloadMenuOver();" onmouseout="downloadMenuOut();" onclick="saveTopologyXmlByNode(\''+topologyMapNode.id+'\',\''+this.xmlName+'\');">&nbsp;&nbsp;&nbsp;&nbsp;设为默认拓扑图 </a><br/>';
			// topologyMapNode.menu +='<a class="property_menu_out" onmouseover="propertyMenuOver();" onmouseout="propertyMenuOut();" onclick="javascript:window.showModalDialog(\'/afunms/submap.do?action=equipProperty&type=net_router&nodeId=' + topologyMapNode.id + '\', window, \'dialogwidth:500px; dialogheight:300px; status:no; help:no;resizable:0\');">&nbsp;&nbsp;&nbsp;&nbsp;图元属性 </a><br/>';
			if (this.topologyMapOperation.deleteTopolgyMapNode) {
				topologyMapNode.menu +='<a class="deleteEquip_menu_out" onmouseover="deleteEquipMenuOver();" onmouseout="deleteEquipMenuOut();" onclick="removeEquip(\'' + topologyMapNode.id + '\')">&nbsp;&nbsp;&nbsp;&nbsp;拓扑图删除设备 </a><br/>';
			}
		} else if (this.topologyMapOperation.deleteTopolgyMapHinNode) {
			//示意图元
			topologyMapNode.menu +='<a class="deleteEquip_menu_out" onmouseover="deleteEquipMenuOver();" onmouseout="deleteEquipMenuOut();" onclick="removeEquip(\'' + topologyMapNode.id + '\')">&nbsp;&nbsp;&nbsp;&nbsp;拓扑图删除设备 </a><br/>';
			topologyMapNode.menu +='<a class="relationmap_menu_out" onmouseover="relationMapMenuOver();" onmouseout="relationMapMenuOut();" onclick="javascript:window.showModalDialog(\'/afunms/submap.do?action=relationList&nodeId='+topologyMapNode.id+'&category='+topologyMapNode.nodeType+'\', window, \'dialogwidth:500px; dialogheight:300px; status:no; help:no;resizable:0\');">&nbsp;&nbsp;&nbsp;&nbsp;关联拓扑图 </a><br/>';
		}
		
	},

	getTopologyMapLineMenuData: function (topologyMapLine) {
		topologyMapLine.lineMenu = "";
		if (topologyMapLine.lineid.indexOf("hl") < 0) {
			var propertyLineMenu = '<a class="property_menu_out" onmouseover="propertyMenuOver();" onmouseout="propertyMenuOut();" onclick="javascript:window.showModalDialog(\'/afunms/submap.do?action=linkProperty&lineId=' + topologyMapLine.lineid + '\', window, \'dialogwidth:350px; dialogheight:250px; status:no; help:no;resizable:0\');">&nbsp;&nbsp;&nbsp;&nbsp;链路属性 </a><br/>';
			var updateLineMenu = '<a class="editline_menu_out" onmouseover="editLineMenuOver();" onmouseout="editLineMenuOut();" onclick="editLink(' + topologyMapLine.lineid + ')">&nbsp;&nbsp;&nbsp;&nbsp;修改链路 </a><br/>';
			var deleteLineMenu = '<a class="deleteline_menu_out" onmouseover="deleteLineMenuOver();" onmouseout="deleteLineMenuOut();" onclick="deleteLink(\'' + topologyMapLine.lineid + '\')">&nbsp;&nbsp;&nbsp;&nbsp;删除链路 </a><br/>';
			if (this.topologyMapOperation.updateTopolgyMapLine) {
				topologyMapLine.lineMenu += propertyLineMenu;
				topologyMapLine.lineMenu += updateLineMenu;
			}
			if (this.topologyMapOperation.deleteTopolgyMapLine) {
				topologyMapLine.lineMenu += deleteLineMenu;
			}
		} else {
			var deleteLineMenu = '<a class="deleteline_menu_out" onmouseover="deleteLineMenuOver();" onmouseout="deleteLineMenuOut();" onclick="deleteLine(\'' + topologyMapLine.lineid + '\')">&nbsp;&nbsp;&nbsp;&nbsp;删除链路 </a><br/>';
			var updateDemoLineMenu = '<a class="property_menu_out" onmouseover="propertyMenuOver();" onmouseout="propertyMenuOut();" onclick="javascript:window.showModalDialog(\'/afunms/link.do?action=readyEditLine&lineid=' + topologyMapLine.lineid + '&xml=' + this.xmlName + '\', window, \'dialogwidth:500px; dialogheight:300px; status:no; help:no;resizable:0\');">&nbsp;&nbsp;&nbsp;&nbsp;链路属性 </a><br/>';
			if (this.topologyMapOperation.updateTopolgyMapHinLine) {
				topologyMapLine.lineMenu += updateDemoLineMenu;
			}
			if (this.topologyMapOperation.deleteTopolgyMapHinLine) {
				topologyMapLine.lineMenu += deleteLineMenu;
			}
		}
	}
}

function getAllSelectedNodes() {
	return topologyMapObject.getAllSelectedNodes();
}


	/**
	 * =========================== 处理节点选中的样式 ==========================
	 */

// 选中节点后的样式
function selectImg(objSty)
{	
	document.all(objSty.id).style.background = "#9BFFAC";
	document.all(objSty.id).style.border="1px solid #007314";
	document.all(objSty.id).style.filter="Alpha(Opacity=60);";
	
// document.all(objSty.id.replace("node_","text_")).style.background =
// "#9BFFAC";
// document.all(objSty.id.replace("node_","text_")).style.border="1px solid
// #007314";
// document.all(objSty.id.replace("node_","text_")).style.filter="Alpha(Opacity=60);";
	// document.all(objSty.id.replace("node_","text_")).style.color = "#FFFFFF";
	// document.all(objSty.id.replace("node_","text_")).style.height = "12px";
}

// 取消选中节点的样式
function unSelectImg(objSty)
{
	document.all(objSty.id).style.background = "";
	document.all(objSty.id).style.border="0px solid #007314";
	document.all(objSty.id).style.color = "#000000";
	document.all(objSty.id).style.filter="";
	
// document.all(objSty.id.replace("node_","text_")).style.background = "";
// document.all(objSty.id.replace("node_","text_")).style.border="0px solid
// #007314";
// document.all(objSty.id.replace("node_","text_")).style.color = "#000000";
}

//// 处理选框
//function selectStart()
//{
//	if (window.event.srcElement.id == "divLayer" || 
//		window.event.srcElement.tagName == "BODY")
//	{
//		obj = null;
//		selectStatus = true;
//		imgTop.style.height = imgTop.style.width = imgLeft.style.height = imgLeft.style.width = imgBottom.style.height = imgBottom.style.width = imgRight.style.height = imgRight.style.width = 1;
//		imgTop.style.visibility = imgLeft.style.visibility = imgBottom.style.visibility = imgRight.style.visibility = "visible";
//		imgTop.style.zIndex = imgLeft.style.zIndex = imgBottom.style.zIndex = imgRight.style.zIndex = 9999;
//		beginx = event.x;
//		beginy = event.y;
//		document.onmousemove = move;
//		document.onmouseup = up;
//	}
//}


/** ** 搜索面板 -- 开始 *** */
// 最重要的一步就是移动主层---如何计算移动的方向和大小

var anchorPos = new Array();
anchorPos[0] = (viewWidth - 88) / 2;
anchorPos[1] = 200;
var flashTimer;
var closeAncTimer;

// var mappingIP = window.parent.bottomFrame.getMappingIP();

document.write("<div id=\"postLayer\" style=\"position:absolute;left:"+ anchorPos[0] +"px;top:"+ anchorPos[1] +"px;visibility:hidden;z-index:993;width:88px;height:60px;\">");
document.write("<table width=\"100%\" height=\"60\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr>");
document.write("<td style=\"border:#FF0000 2px dotted\">&nbsp;</td>");
document.write("</tr></table>");
document.write("</div>");


function getNodeCoor(ip)
{
	return topologyMapObject.getNodeIP(ip);
}
//yangjun add 按照id查找设备节点
function getNodeId(id)
{
	return topologyMapObject.getNodeId(id);
}
function setMainLayerPos(x, y)
{
	document.all.divLayer.style.left = x;
	document.all.divLayer.style.top = y;
}

function showAnchor()
{
	clearInterval(flashTimer);
	clearTimeout(closeAncTimer);
	document.all.postLayer.style.visibility = "visible";
	flashTimer = setInterval("flashAnchor()", 500);
	closeAncTimer = setTimeout("closeAnchor()", 30*1000);
}

function flashAnchor()
{
	var visi = document.all.postLayer.style.visibility;
	if (visi == "visible")
	{
		document.all.postLayer.style.visibility = "hidden";
	}
	else
	{
		document.all.postLayer.style.visibility = "visible";
	}
}

function closeAnchor()
{
	clearInterval(flashTimer);
	clearTimeout(closeAncTimer);
	document.all.postLayer.style.visibility = "hidden";
}

// 根据节点的坐标移动主层
// 输入变量是其上的一个节点坐标，移动之后使节点位于 Anchor 内
function moveMainLayer(coor)
{
	var x = y = 0;
	coor[0] = parseInt(coor[0]);
	coor[1] = parseInt(coor[1]);
	if (coor[0] > 0)
	{
		x = coor[0] - anchorPos[0];
		x = 0 - x;
	}
	else
	{
		x = anchorPos[0] - coor[0];
	}
	
	if (coor[1] > 0)
	{
		y = coor[1] - anchorPos[1];
		y = 0 - y;
	}
	else
	{
		y = anchorPos[1] - coor[1];
	}

	setMainLayerPos(x+23, y+10);
	
	showAnchor();
}
/** ** 搜索面板 -- 结束 *** */


// 通过键盘的“上下左右”方向键控制视图位置
function document.onkeydown()
{
// -----先不用--------改3-------
/*
 * try { if (document.all.blind.style.visibility == "visible") return; } catch
 * (exception) { }
 */
// ----------------------
	var left = divLayer.style.left;
	var top = divLayer.style.top;
	
	// left = left.substring(0, left.length - 2);
	// top = top.substring(0, top.length - 2);

	left = parseInt(left);
	top = parseInt(top);
	switch (event.keyCode)
	{
		/*
		 * case 81: // Q 键，切换视图 if (g_viewFlag == 0) { g_viewFlag = 1; var
		 * target = "showMap.jsp?filename=" + filename + "&viewflag=1";
		 * parent.mainFrame.location = target; } else if (g_viewFlag == 1) {
		 * g_viewFlag = 0; var target = "showMap.jsp?filename=" + filename +
		 * "&viewflag=0"; parent.mainFrame.location = target; } break;
		 */
		case 35:		// End
		case 82:		// R
			moveAction();
			break;
		case 37:
		case 65:		// A
			moveAction('left');
			break;
		case 38:
		case 87:		// W
			moveAction('up');
			break;
		case 39:
		case 68:		// D
			moveAction('right');
			break;
		case 40:
		case 83:		// S
			moveAction('down');
			break;
			
		case 90:		// Z
			zoomAll('out');
			break;
		case 88:		// X
			zoomAll();
			break;
		case 67:		// C
			zoomAll('in');
			break;
			
		case 69:		// E 隐藏/显示控制面板
			// showController(!controllerState);
			break;

		default:
			break;
	}

}


/** ** 控制移动方向的四个按钮事件和缩放按钮事件 -- 开始 *** */

var distance = 80;
var speed = 12;
var position;
var left;
var top;
var timer;

var zoom = 1.0;
var scale = 0.1;

var controllerState = true;

function moveAction(dir)
{

// /-----------------改4----
	closeAnchor();	// 关闭准星
// hideLineTip(); // 隐藏链路提示
// ------------------------

	clearTimer();
	updatePosition();
	
	if (dir == "left")
	{
		position = left + distance;
		timer = setInterval("moveLeft()", speed);
	}
	else if (dir == "up") 
	{
		position = top + distance;
		timer = setInterval("moveUp()", speed);
	}
	else if (dir == "right") 
	{
		position = left - distance;
		timer = setInterval("moveRight()", speed);
	}
	else if (dir == "down") 
	{
		position = top - distance;
		timer = setInterval("moveDown()", speed);
	}
	else
	{
		moveOrigin();
	}
}

function clearTimer()
{
	clearInterval(timer);
}

function moveLeft() 
{
	updatePosition();
	if (left >= position) 
	{
		clearTimer();
		return;
	}
	divLayer.style.left = (left + speed);
}

function moveUp() 
{
	updatePosition();
	if (top >= position)
	{
		clearTimer();
		return;
	}
	divLayer.style.top = (top + speed);
}

function moveRight()
{
	updatePosition();
	if (left <= position) 
	{
		clearTimer();
		return;
	}
	divLayer.style.left = (left - speed);
}

function moveDown() 
{
	updatePosition();
	if (top <= position) 
	{
		clearTimer();
		return;
	}
	divLayer.style.top = (top - speed);
}

function moveOrigin() 
{
	divLayer.style.left = 0;// parseInt(mainX);--这是改后的，用于恢复原来的位置-----改5--
	divLayer.style.top = 0;// parseInt(mainY);
}

function updatePosition() 
{
	var divLeft = parseInt(divLayer.style.left);
	var divTop = parseInt(divLayer.style.top);
	
	// divLeft = divLeft.substring(0, divLeft.length - 2);
	// divTop = divTop.substring(0, divTop.length - 2);

	left = parseInt(divLeft);
	top = parseInt(divTop);
}

function zoomAll(state) 
{
	closeAnchor();// ---------改后添加这个-
	if (divLayer.style.zoom == "") 
	{
		divLayer.style.zoom = 1.0;
	}
	
	if (state == "out") 
	{
		// 缩小
		if (divLayer.style.zoom != "") 
		{
			zoom = parseFloat(zoom) - scale;
			if (zoom <= 0) 
			{
				zoom = 0.9;
			}
			else if (zoom > 0 && zoom < 0.5) 
			{
				zoom = 0.5;
				return;
			}
			divLayer.style.zoom = parseFloat(zoom);
		}
	}
	else if (state == "in") 
	{
		// 放大
		if (divLayer.style.zoom != "") 
		{
			zoom = parseFloat(zoom) + scale;
			if (zoom > 2.0) 
			{
				zoom = 2.0;
				return;
			}
			else if (zoom == 0.2) 
			{
				zoom = 1.1;
			}
			divLayer.style.zoom = parseFloat(zoom);
		}
	}
	else {
		// 复原
		if (divLayer.style.zoom != "") 
		{
			divLayer.style.zoom = 1.0;
			zoom = 1.0;
		}
	}
}

function showController(show) 
{
	if (show) 
	{
		document.all.moveController.style.visibility = "visible";
		document.all.sizeController.style.visibility = "visible";
		controllerState = true;
	}
	else 
	{
		document.all.moveController.style.visibility = "hidden";
		document.all.sizeController.style.visibility = "hidden";
		// controllerState = false;
		controllerState = true;
	}
	return true;
}

// ------ 图片交换效果函数 - 开始 ------

function swapImage(imageID, imageSrc) 
{
	document.all(imageID).src = imgPath+imageSrc;
}

// ------ 图片交换效果函数 - 结束 ------

function loadMoveController() 
{
	document.write('<div id="moveController" style="position:absolute;top:5px;left:5px;z-index:999;background-image:url(image/controller_bg.gif);">');
	document.write('<table width="66" border="0" cellspacing="0" cellpadding="0" style="font-size:9pt;"><tr><td height="19" width="19"></td><td width="28">');
	document.write('<img src="'+imgPath+'image/topo/arrow_up.gif" onmouseout="javascript:swapImage(\'image_up\', \'image/topo/arrow_up.gif\');" onmouseover="javascript:swapImage(\'image_up\', \'image/topo/arrow_up_over.gif\');" alt="向上移动 | W" width="28" height="19" onclick="javascript:moveAction(\'up\');" style="cursor:hand;" name="image_up" id="image_up" />');
	document.write('</td><td height="19" width="19"></td></tr><tr><td>');
	document.write('<img src="'+imgPath+'image/topo/arrow_left.gif" onmouseout="javascript:swapImage(\'image_left\', \'image/topo/arrow_left.gif\');" onmouseover="javascript:swapImage(\'image_left\', \'image/topo/arrow_left_over.gif\');" alt="向左移动 | A" width="19" height="28" onclick="javascript:moveAction(\'left\');" style="cursor:hand;" name="image_left" id="image_left" />');
	document.write('</td><td align="center" valign="middle" style="text-align:center;">');
	document.write('<img src="'+imgPath+'image/topo/arrow_center.gif" onmouseout="javascript:swapImage(\'image_center\', \'image/topo/arrow_center.gif\');" onmouseover="javascript:swapImage(\'image_center\', \'image/topo/arrow_center_over.gif\');" alt="复位 | R" width="19" height="19" onclick="javascript:moveAction(\'origin\');" style="cursor:hand;" name="image_center" id="image_center" />');
	document.write('</td><td>');
	document.write('<img src="'+imgPath+'image/topo/arrow_right.gif" onmouseout="javascript:swapImage(\'image_right\', \'image/topo/arrow_right.gif\');" onmouseover="javascript:swapImage(\'image_right\', \'image/topo/arrow_right_over.gif\');" alt="向右移动 | D" width="19" height="28" onclick="javascript:moveAction(\'right\');" style="cursor:hand;" name="image_right" id="image_right" />');
	document.write('</td></tr><tr><td></td><td>');
	document.write('<img src="'+imgPath+'image/topo/arrow_down.gif" onmouseout="javascript:swapImage(\'image_down\', \'image/topo/arrow_down.gif\');" onmouseover="javascript:swapImage(\'image_down\', \'image/topo/arrow_down_over.gif\');" alt="向下移动 | S" width="28" height="19" onclick="javascript:moveAction(\'down\');" style="cursor:hand;" name="image_down" id="image_down" />');
	document.write('</td><td></td></tr></table></div>');
}

function loadSizeController() 
{
	document.write('<div id="sizeController" style="position:absolute;top:80px;left:8px;z-index:998;background-image:url(image/controller_bg2.gif);width:60px;">');
	document.write('<table width="58" height="20" border="0" cellspacing="0" cellpadding="0"><tr><td width="18" height="20" style="padding-left:2px;">');
	document.write('<img src="'+imgPath+'image/topo/zoom_out.gif" onmouseout="javascript:swapImage(\'image_out\', \'image/topo/zoom_out.gif\');" onmouseover="javascript:swapImage(\'image_out\', \'image/topo/zoom_out_over.gif\');" alt="缩小视图 | Z" width="18" height="16" onclick="javascript:zoomAll(\'out\');" style="cursor:hand;" name="image_out" id="image_out" />');
	document.write('</td><td align="center" style="text-align:center;">');
	document.write('<img src="'+imgPath+'image/topo/zoom_reset.gif" onmouseout="javascript:swapImage(\'image_reset\', \'image/topo/zoom_reset.gif\');" onmouseover="javascript:swapImage(\'image_reset\', \'image/topo/zoom_reset_over.gif\');" alt="还原 | X" width="18" height="16" onclick="javascript:zoomAll();" style="cursor:hand;" name="image_reset" id="image_reset" />');
	document.write('</td><td width="18" height="20">');
	document.write('<img src="'+imgPath+'image/topo/zoom_in.gif" onmouseout="javascript:swapImage(\'image_in\', \'image/topo/zoom_in.gif\');" onmouseover="javascript:swapImage(\'image_in\', \'image/topo/zoom_in_over.gif\');" alt="放大视图 | C" width="18" height="16" onclick="javascript:zoomAll(\'in\');" style="cursor:hand;" name="image_in" id="image_in" />');
	document.write('</td></tr></table></div>');
}
loadMoveController();		// 加载移动控制器
loadSizeController();		// 加载大小控制器
showController(false);
/** ** 控制移动方向的四个按钮事件和缩放按钮事件 -- 结束 *** */

		/**
		 * =============================== 辅助功能 ============================================
		 */
// 处理选框
function selectStart()
{
	topologyMapObject.selectStart();
}


function bodyDown()
{
	topologyMapObject.bodyDown();
}

// 按下divLayer空白处时
function divLayerDown()
{
	topologyMapObject.bodyDown();
}
// 右键某个设备，设置为当前拓扑图为默认告警拓扑图 wupinlong add/2013/10/8
function saveTopologyXmlByNode(nodeId, xmlName) {
	TopoRemoteService.saveTopologyXmlByNode(nodeId,xmlName,{
		callback:function(data) {
			if(data=="success") {
				alert("设置成功！");
			} else {
				alert("设置失败！");
			}
		}
	});
}
