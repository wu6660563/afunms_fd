// Node object
function TreeNode(id, name , descr , pid) {
	
	// id
	this.id = id;
	
	// name
	this.name = name;
	
	// descr ����
	this.descr = descr;

	// �� id
	this.pid = pid;
	
	// �Ƿ�����ӽڵ�
	this.isHadChild = false;
	
	// �Ƿ�չ��
	this.isOpen = false;
	
	// �Ƿ�ѡ��
	this.isSelected = false;
	
	// ���е��ӽڵ�
	this.childNodes = null;
	
	// �ڵ�ĵȼ�
	this.level = null;
	
	// �Ƿ�Ϊ���ڵ������ӽڵ�
	this.isEndNode = true;
	
}

// �Ҽ��˵� object
function MenuItem(id , name , img , onclick){
	// id
	this.id = id;
	
	// name
	this.name = name;
	
	// ͼƬ
	this.img = img;
	
	// ����¼�
	this.onclick = onclick;
}


// Tree object
function Tree(){
	// ��������
	this.dataArray = new Array();

	// ���нڵ�
	this.nodeArray = new Array();
	
	// �����Ҽ��˵����б�
	this.menuItemArray = new Array();
	
	// ������DIV
	this.treeDiv = null;
	
	// ���ڵ�
	this.rootNode = null;
	
	// �ڵ�click�¼�
	this.nodeClick = null;
	
	// �ڵ�contextmenu�¼�
	this.nodeContextmenu = null;
	
	// �Ҽ��˵�
	this.contextMenuItemDiv = null;
	
	// ͼƬ·��
	this.imPath = '';

}

Tree.prototype = {
	// ��ʼ������
	init:function(dataArray){
		
		this.dataArray = dataArray;
	
		// �������нڵ�
		for(var i = 0 ; i < this.dataArray.length ; i++){
		
			var data = this.dataArray[i];
			
			var node = new TreeNode(data[0] , data[1] , data[2] , data[3]);
			
			this.nodeArray[i] = node;
			
		}
		
		for(var i = 0 ; i < this.nodeArray.length ; i++){
			var perNode = this.nodeArray[i];
			perNode.childNodes = this.getChildNodes(perNode);
			perNode.isHadChild = this.isHadChild(perNode);
		}
		
		
		
	},

	
	show: function(treeDivId){
		if(document.getElementById){
			this.treeDiv = document.getElementById(treeDivId);
		}else{
			alert('�������֧��');
			return;
		}
		
		if(!this.treeDiv){
			alert('��DIV������ , ���ṩһ��DIV');
			return;
		}
		this.treeDiv.className = 'businessTree';
		this.rootNode = this.getRootNode();
		this.recursiveCreateTree(this.rootNode , null);
	},
	
	// ʹ��  id , �Լ� tagName ������һ��element
	createElement: function (id , tagName){
		var element = document.createElement(tagName);
		element.id = id;
		return element;
	},
	
	

	// �Ƿ�����ӽڵ�
	isHadChild:function(node){
		var result = false;
		for(var i = 0 ; i< this.nodeArray.length ; i++){
			var perNode = this.nodeArray[i];
			if(node.id == perNode.pid) {
				result = true;
				break;
			}
		}
		return result;
	},
	
	// ��ȡ�ڵ�ĸ��ڵ�
	getFatherNode:function(node){
		var fatherNode = null;
		for(var i = 0 ; i < this.nodeArray.length ; i++){
			var perNode = this.nodeArray[i];
			if(node.pid == perNode.id){
				fatherNode = perNode;
			}
		}
		
		return fatherNode;
	},
	
	// ��ȡ�ڵ�������ӽڵ�
	getChildNodes: function(node){
		var childNodes = new Array();
		for(var i = 0 ; i< this.nodeArray.length ; i++){
			var perNode = this.nodeArray[i];
			if(node.id == perNode.pid) {
				childNodes.push(perNode);
			}
		}
		return childNodes;
	},
	
	
	// ��ȡ���ڵ�
	getRootNode: function(){
		var node = null;
		for(var i = 0 ; i < this.nodeArray.length ; i++){
			var perNode = this.nodeArray[i];
			if(perNode.pid == '0'){   // ��� PID Ϊ 0 ��Ϊ���ڵ�
				node = perNode;
				break;
			}
		}
		return node;
	},
	
	// �ݹ鴴���� ��Ҫ �ڵ� ��  ���ڵ�
	recursiveCreateTree:function(node , parentNode){
		
		if(node == null){
			return;
		}
		// �����ڵ� DIV
		var nodeDiv = this.createNodeDiv(node);
		// ��ȡ���ڵ�� nodeChildDiv
		var parentNodeChildDiv = this.getParentNodeChildDiv(parentNode);
		// ��ӽڵ� DIV
		this.addNodeDiv(nodeDiv, parentNodeChildDiv);
		var childNodes = node.childNodes;
		if(childNodes){
			for(var i = 0 ; i < childNodes.length ; i++){
				var perChildNode = childNodes[i];
				
				if( i < childNodes.length -1){
					perChildNode.isEndNode = false;
				}
				this.recursiveCreateTree(perChildNode , node);
			}
		}
	},
	
	// ��ȡ���ڵ�� nodeChildDiv
	getParentNodeChildDiv:function(parentNode){
		var parentNodeChildDiv = null;
		if(parentNode){
			parentNodeChildDiv = document.getElementById('nodeChildDiv-' + parentNode.id);
		}else{
			parentNodeChildDiv = this.treeDiv;
		}
		return parentNodeChildDiv;
	},
	
	// ��ӽڵ� DIV
	addNodeDiv:function(nodeDiv, parentDiv){
		if(parentDiv){
			// ������ڵ� DIV ����,�򽫽ڵ�� DIV ���뵽parentDiv
			parentDiv.appendChild(nodeDiv);
		}else{
			// ������� treeDiv
			this.treeDiv.appendChild(nodeDiv);
		}
		
		
	},
	
	// �����ڵ� DIV
	createNodeDiv:function(node){
		var nodeDiv = this.createElement('nodeDiv-' + node.id , 'DIV');
		var nodeDataDiv = this.createNodeDataDiv(nodeDiv , node);
		var nodeChildDiv = this.createNodeChildDiv(nodeDiv, node);
		nodeDiv.appendChild(nodeDataDiv);
		nodeDiv.appendChild(nodeChildDiv);
		
		nodeDiv.className = this.getNodeDivClassName();
		return nodeDiv;
	},
	
	
	
	// �����ڵ����� DIV
	createNodeDataDiv:function(nodeDiv , node){
		var nodeDataDiv = this.createElement('nodeDataDiv-' + node.id , 'DIV');
		
		var imgDiv = this.createNodeDataDivImgDiv(node);
		
		if(imgDiv){
			nodeDataDiv.appendChild(imgDiv);
		}
		
		if(node.isHadChild){
			// ������ӽڵ� �򴴽������ӽڵ�Ľڵ�ͼ��  ���а�����ʾ�������ӽڵ��ͼ��  �ļ��д򿪺͹رյ�ͼ��
			var imgDiv2 = this.createHadChildNodeDivImgDiv(node);
			if(imgDiv2){
				nodeDataDiv.appendChild(imgDiv2);
			}
		}else{
			// ���û���ӽڵ� �򴴽�û�к����ӽڵ��ͼ�� ���а������� �� �ļ�ͼ��
			var imgDiv2 =this.createHadNoChildNodeDivImgDiv(node);
			if(imgDiv2){
				nodeDataDiv.appendChild(imgDiv2);
			}
			
		}
		
		// �����ڵ��Name��Div
		var nodeNameDiv = this.createNodeNameDiv(node);
		nodeDataDiv.appendChild(nodeNameDiv);
		
		nodeDataDiv.className = this.getNodeDataDivClassName();
		
		return nodeDataDiv;
	},
	
	// �����ڵ���ӽڵ� DIV
	createNodeChildDiv:function(nodeDiv , node){
		var nodeChildDiv = this.createElement('nodeChildDiv-' + node.id , 'DIV');
		if(node.isOpen){
			nodeChildDiv.style.display = "inline";
		}else{
			nodeChildDiv.style.display = "none";
		}
		
		nodeChildDiv.className = this.getNodeChildDivClassName();
		return nodeChildDiv;
	},
	
	// �����ڵ�����Div
	createNodeNameDiv:function(node){
		var nodeNameDiv = this.createElement('nodeDataDiv-name-' + node.id, 'DIV');
		var nodeNameA = this.createElement('nodeDataDiv-name-A-' + node.id , 'A')
		nodeNameA.innerText = node.name;
		nodeNameA.name = "nodeDataDiv-name-A";
		
		
		var nodeClick = this.getNodeClick();
		
		var tree = this;
		
		if(nodeClick){
			nodeNameA.onclick = function(){
				nodeClick.call(tree , nodeNameA , node);
			};
		}
		
		var nodeContextmenu = this.getNodeContextmenu();
		
		var tree = this;
		
		if(nodeContextmenu){
			nodeNameA.oncontextmenu = function(){
				nodeContextmenu.call(nodeNameA , tree , node);
				return false;
			}
			
		}
		
		
		nodeNameDiv.appendChild(nodeNameA);
		
		
		
		return nodeNameDiv;
	},
	
	// �����ڵ�����ǰ���ߺͿհ׵�ͼƬdiv
	createNodeDataDivImgDiv:function(node){
		
		var fatherNode = this.getFatherNode(node);
		if(!fatherNode){
			// ��� fatherNode Ϊ�� ���Ǹ��ڵ� ����Ҫ����
			return null;
		}
		
		var imgDiv = this.createElement('nodeDataDiv-imgDiv-' + node.id , 'DIV');
		
		// ��ȡ���ڵ�����ߺͿհ�ͼƬ��div
		var fatherNodeImgDiv = document.getElementById('nodeDataDiv-imgDiv-' + node.pid);
		if(fatherNodeImgDiv){
			// �����ڵ�����ߺͿհ�ͼƬ��div ���и���
			imgDiv.innerHTML = fatherNodeImgDiv.innerHTML;
		}
		
		var img = this.createElement(null , 'img');
		if(!fatherNode.isEndNode){
			// ��� ���ڵ㲻�����һ�� ���˽ڵ�������ڵ� �򴴽� ����ͼƬ
			img.src = this.getLineImg();
		}else{
			// ��� ���ڵ������һ���ڵ� �򴴽� �հ�ͼƬ
			img.src = this.getBlankImg();
		}
		imgDiv.appendChild(img);
		
		imgDiv.className = this.getNodeDataDivImgDivClassName();
		return imgDiv;
		
		
	},
	
	// ���������ӽڵ�Ľڵ�ͼ��  ���а�����ʾ�������ӽڵ��ͼ��  �ļ��д򿪺͹رյ�ͼ��
	createHadChildNodeDivImgDiv:function(node){
		var imgDiv = this.createElement('nodeDataDiv-imgDiv-hadChildNode-' + node.id , 'DIV');
		
		// ������ӽڵ�  ������ʾ�ӽڵ��ͼ��
		var showOrHideNodeChildDivImg = this.createShowOrHideNodeChildDivImg(node,this.showOrHideNodeChildDiv);
		imgDiv.appendChild(showOrHideNodeChildDivImg);
		
		// ���� �ļ���ͼ�� 
		var nodeFolderImg = this.createNodeFolderImg(node);
		imgDiv.appendChild(nodeFolderImg);
		
		imgDiv.className = this.getHadChildNodeDivImgDivClassName();
		
		return imgDiv;
	},
	
	// �����ڵ����ʾ�ӽڵ�ͼ�� ������ node �ڵ�    onClickCallback ��ť����¼�����Ļص�����
	createShowOrHideNodeChildDivImg:function(node,onClickCallback){
		// ����ͼ��
		var img = this.createElement('showOrHideNodeChildDivImg-' + node.id, 'img');
		if(node.isOpen){
			if(node.id == this.rootNode.id){
				img.src = this.getRootNodeOpenImg();
			}else{
				if(node.isEndNode){
					img.src = this.getNotRootAndIsEndNodeOpenImg();
				}else{
					img.src = this.getNotRootAndNotEndNodeOpenImg();
				}
			}
		}else{
			if(node.id == this.rootNode.id){
				img.src = this.getRootNodeClosedImg();
			}else{
				if(node.isEndNode){
					img.src = this.getNotRootAndIsEndNodeClosedImg();
				}else{
					img.src = this.getNotRootAndNotEndNodeClosedImg();
				}
			}
		}
		var tree = this;
		img.onclick = function(){
			onClickCallback.call(img, node , tree);
		};
		return img;
	},
	
	// ��ʾ������ �ӽڵ�DIV
	showOrHideNodeChildDiv:function(node , tree){
		var nodeChildDiv = document.getElementById("nodeChildDiv-" + node.id);
		var nodeFolderImg = document.getElementById("nodeFolderImg-" + node.id);
		if(node.isOpen){
			// ����� ������
			node.isOpen = false;
			if(nodeChildDiv){
				nodeChildDiv.style.display = "none";
			}
			if(nodeFolderImg){
				nodeFolderImg.src = tree.getFolderClosedImg();
			}
			if(node.id == tree.rootNode.id){
				this.src = tree.getRootNodeClosedImg();
			}else{
				if(node.isEndNode){
					this.src = tree.getNotRootAndIsEndNodeClosedImg();
				}else{
					this.src = tree.getNotRootAndNotEndNodeClosedImg();
				}
			}
			
		}else{
			// ������� ���
			node.isOpen = true;
			if(nodeChildDiv){
				nodeChildDiv.style.display = "inline";
			}
			if(nodeFolderImg){
				nodeFolderImg.src = tree.getFolderOpenImg();
			}
			if(node.id == tree.rootNode.id){
				this.src = tree.getRootNodeOpenImg();
			}else{
				if(node.isEndNode){
					this.src = tree.getNotRootAndIsEndNodeOpenImg();
				}else{
					this.src = tree.getNotRootAndNotEndNodeOpenImg();
				}
			}
		}
	},
	
	// �����ļ���ͼ��
	createNodeFolderImg:function(node){
		var nodeFolderImg = this.createElement("nodeFolderImg-" + node.id, "img");
		if(node.isOpen){
			nodeFolderImg.src = this.getFolderOpenImg();
		}else{
			nodeFolderImg.src = this.getFolderClosedImg();
		}
		return nodeFolderImg;
	},
	
	
	createHadNoChildNodeDivImgDiv:function(node){
		var imgDiv = this.createElement('nodeDataDiv-imgDiv-noChildNode-' + node.id , 'DIV');
		
		// ���� ����ͼ��
		var brokenLineImg = this.createBrokeLineImg(node);
		imgDiv.appendChild(brokenLineImg);
		
		// ���� �ļ�ͼ��
		var leafImg = this.createLeafImg(node);
		imgDiv.appendChild(leafImg);
		
		imgDiv.className = this.getHadNoChildNodeDivImgDivClassName();
		
		return imgDiv;
	},
	
	// ���� ����ͼ��
	createBrokeLineImg:function(node){
		var brokenLineImg = this.createElement('brokenLineImg-' + node.id , 'img');
		if(node.isEndNode){
			// ���Ϊ���ڵ�
			brokenLineImg.src = this.getIsEndBrokenLineImg();
		}else{
			// ����
			brokenLineImg.src = this.getNotEndBrokenLineImg();
		}
		
		return brokenLineImg;
	},
	
	// ���� �ļ�ͼ��
	createLeafImg:function(node){
		var leafImg = this.createElement('leafImg-' + node.id , 'img');
		leafImg.src = this.getLeafImg();
		return leafImg;
	},
	
	setImgPath:function(imPath){
		this.imPath = imPath;
	},
	
	getImgPath:function(){
		return this.imPath;
	},
	
	// ��ȡ����ͼ��
	getLineImg:function(){
		return this.getImgPath() + "line1.gif";
	},
	
	// ��ȡ����ͼ��
	getBlankImg:function(){
		return this.getImgPath() + "blank.gif";
	},
	
	// ��ȡ���ڵ�򿪵�ͼ��
	getRootNodeOpenImg:function(){
		return  this.getImgPath() + "minus5.gif";
	},
	
	// ��ȡ���ڵ�رյ�ͼ��
	getRootNodeClosedImg:function(){
		return  this.getImgPath() + "plus5.gif";
	},
	
	// ��ȡ�������ڵ�ķǸ��ڵ�򿪵�ͼ��
	getNotRootAndNotEndNodeOpenImg:function(){
		return  this.getImgPath() + "minus3.gif";
	},
	
	// ��ȡ�����ڵ㵫���Ǹ��ڵ�򿪵�ͼ��
	getNotRootAndIsEndNodeOpenImg:function(){
		return  this.getImgPath() + "minus2.gif";
	},
	
	// ��ȡ�������ڵ�ķǸ��ڵ�رյ�ͼ��
	getNotRootAndNotEndNodeClosedImg:function(){
		return  this.getImgPath() + "plus3.gif";
	},
	
	// ��ȡ�����ڵ㵫���Ǹ��ڵ�رյ�ͼ��
	getNotRootAndIsEndNodeClosedImg:function(){
		return  this.getImgPath() + "plus2.gif";
	},
	
	// ��ȡ�ļ��д򿪵�ͼ��
	getFolderOpenImg:function(){
		return  this.getImgPath() + "folderOpen.gif";
	},
	
	// ��ȡ�ļ��йرյ�ͼ��
	getFolderClosedImg:function(){
		return  this.getImgPath() + "folderClosed.gif";
	},
	
	// ��ȡ�������ڵ�����ӽڵ������ͼ��
	getNotEndBrokenLineImg:function(){
		return  this.getImgPath() + "line3.gif";
	},
	
	// ��ȡ�����ڵ�����ӽڵ������ͼ��
	getIsEndBrokenLineImg:function(){
		return  this.getImgPath() + "line2.gif";
	},
	
	// ��ȡ�ļ�ͼ��
	getLeafImg:function(){
		return this.getImgPath() + "leaf.gif";
	},
	
	// ��ȡ�ļ�ͼ��
	getMenuItemBackgroundImage:function(){
		return this.getImgPath() + "menubg.jpg";
	},
	
	getImgSrcByImgName:function(name){
		return this.getImgPath() +��name;
	},
	
	// ��ȡ�ڵ�Div����ʽ  nodeDiv
	getNodeDivClassName:function(){
		return 'nodeDiv';
	},
	
	// ��ȡ�ڵ����ݵ�Div����ʽ
	getNodeDataDivClassName:function(){
		return 'nodeDataDiv';
	},
	
	// ��ȡ�ڵ���ӽڵ��Div����ʽ
	getNodeChildDivClassName:function(){
		return 'nodeChildDiv';
	},
	
	// ��ȡ�ڵ����ݽڵ��ͼƬDiv����ʽ
	getNodeDataDivImgDivClassName:function(){
		return 'floatLeft';
	},
	
	// ��ȡ���ӽڵ�Ľڵ�ͼƬDiv����ʽ
	getHadChildNodeDivImgDivClassName:function(){
		return 'floatLeft';
	},
	
	// ��ȡû�����ӽڵ�Ľڵ�ͼƬDiv����ʽ
	getHadNoChildNodeDivImgDivClassName:function(){
		return 'floatLeft';
	},
	
	// ��ȡ�ڵ�onclick�¼�
	getNodeClick:function(){
		return this.nodeClick;
	},
	
	// ���ýڵ�onclick�¼�
	setNodeClick:function(nodeClick){
		this.nodeClick = nodeClick;
	},
	
	// ��ȡ�ڵ�oncontextmenu�¼�
	getNodeContextmenu:function(){
		return this.nodeContextmenu;
	},
	
	// ���ýڵ�onclick�¼�
	setNodeContextmenu:function(nodeContextmenu){
		this.nodeContextmenu = nodeContextmenu;
	},
	
	addMenuItem:function(menuItem){
		this.menuItemArray.push(menuItem);
	},
	
	getMenuItemById:function(menuItemId){
		for(var i = 0 ; i < this.menuItemArray.length ; i++){
			var perMenuItem = this.menuItemArray[i];
			if(perMenuItem.id == menuItemId){
				return perMenuItem;
			}
		}
	},
	
	getAllMenuItem:function(){
		return this.menuItemArray;
	},
	
	createContextMenuItem:function(){
		this.contextMenuItemDiv = this.createElement("contextMenuItemDiv" , "DIV");
		this.contextMenuItemDiv.style.display = "none";
		this.contextMenuItemDiv.style.position = "absolute";
		this.contextMenuItemDiv.className = "contextMenuItem";
		if(this.menuItemArray){
			for(var i = 0 ; i < this.menuItemArray.length ; i ++){
				var perMenuItem = this.menuItemArray[i];
				var menuItemDiv = this.createElement("menuItemDiv-" + perMenuItem.id ,"DIV");
				var menuItemImg = this.createElement("menuItemDiv-img-" + perMenuItem.id, "img");
				menuItemImg.src = this.getImgSrcByImgName(perMenuItem.img);
				menuItemDiv.appendChild(menuItemImg);
				var menuItemA = this.createElement("menuItemDiv-A-" + perMenuItem.id, "A");
				menuItemA.innerText = perMenuItem.name;
				menuItemDiv.appendChild(menuItemA);
				menuItemDiv.className = "menuItem";
				
				var tree = this;
				menuItemDiv.onmouseover = function(){
					this.style.backgroundImage = 'url('+tree.getMenuItemBackgroundImage()+')';
				};
				
				menuItemDiv.onmouseout = function(){
					this.className = "menuItem";
					this.style.background='#F0F1F4';
				}
				this.contextMenuItemDiv.appendChild(menuItemDiv);
			}
		}
		
		if(this.treeDiv){
			this.treeDiv.appendChild(this.contextMenuItemDiv);
		}
		
		return this.contextMenuItemDiv;
	},
	
	getContextMenuItem:function(){
		return this.contextMenuItemDiv;
	},
	
	setOnClickForMenuItemById:function(menuItemId , onclick , node){
		var menuItem = this.getMenuItemById(menuItemId);
		
		var menuItemDiv = document.getElementById("menuItemDiv-" + menuItem.id);
		
		var tree = this;
		menuItemDiv.onclick = function(){
			onclick.call(tree , node);
		};
	},
	
	showContextMenuItem:function(pixelTop , pixelLeft){
		this.contextMenuItemDiv.style.pixelTop = pixelTop; 
		this.contextMenuItemDiv.style.pixelLeft = pixelLeft;
		this.contextMenuItemDiv.style.display = "inline";
		
	},
	
	closeContextMenuItem:function(){
		this.contextMenuItemDiv.style.display = "none";
	}
};





