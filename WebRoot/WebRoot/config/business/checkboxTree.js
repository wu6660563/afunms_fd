// Node object
function Node(id, name, pid , isSelected) {
	
	// id
	this.id = id;
	
	// name
	this.name = name;

	// �� id
	this.pid = pid;
	
	// �Ƿ�����ӽڵ�
	this.isHadChild = false;
	
	// �Ƿ�չ��
	this.isOpen = false;
	
	// �Ƿ�ѡ��
	this.isSelected = isSelected;
	
	// ���е��ӽڵ�
	this.childNodes = null;
	
	// �ڵ�ĵȼ�
	this.level = null;
}

// Tree object
function Tree(){
	// ��������
	this.dataArray = new Array();

	// ���нڵ�
	this.nodeArray = new Array();
	
	// 

	// ������DIV
	this.treeDiv = null;
	
	// ���ڵ�
	this.rootNode = null;
	

}

Tree.prototype = {
	// ��ʼ������
	init:function(dataArray){
		
		this.dataArray = dataArray;
		
		
	
		for(var i = 0 ; i < this.dataArray.length ; i++){
		
			var data = this.dataArray[i];
			
			var node = new Node(data[0] , data[1] , data[2] , data[3]);
			
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
		this.rootNode = this.getRootNode();
		this.recursiveCreateTree(this.rootNode , null);
	},
	
	// ʹ��  id , �Լ� tagName ������һ��element
	creaateElement: function (id , tagName){
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
	
	// ��ȡ�����ӽڵ�
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
		var rootNode = null;
		for(var i = 0 ; i < this.nodeArray.length ; i++){
			var node = this.nodeArray[i];
			if(node.pid == '0'){   // ��� PID Ϊ null ��Ϊ�սڵ�
				rootNode = node;
				break;
			}
		}
		
		return rootNode;
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
		var nodeDiv = this.creaateElement('nodeDiv-' + node.id , 'DIV');
		var nodeDataDiv = this.createNodeDataDiv(nodeDiv , node);
		var nodeChildDiv = this.createNodeChildDiv(nodeDiv, node);
		nodeDiv.appendChild(nodeDataDiv);
		nodeDiv.appendChild(nodeChildDiv);
		return nodeDiv;
	},
	
	// �����ڵ����� DIV
	createNodeDataDiv:function(nodeDiv , node){
		var nodeDataDiv = this.creaateElement('nodeDataDiv-' + node.id , 'DIV');
		
		if(node.isHadChild){
			// ������ӽڵ�  ������ʾ�ӽڵ��ͼ��
			var showNodeChildDivIcon = this.createShowNodeChildDivIcon(node,this.showOrHideNodeChildDiv);
			nodeDataDiv.appendChild(showNodeChildDivIcon);
			
			// ���� �ļ���ͼ�� 
			var nodeFolderIcon = this.createNodeFolderIcon(node);
			nodeDataDiv.appendChild(nodeFolderIcon);
		}else{
			// ���û���ӽڵ� ����   checkbox 
			var nodeCheckbox = this.createNodeCheckboxIcon(node);
			nodeDataDiv.appendChild(nodeCheckbox);
		}
		
		// �����ڵ��Text
		var nodeDataName = this.creaateElement('nodeDataDiv-name-' + node.id, 'A');
		nodeDataName.innerText = node.name;
		nodeDataDiv.appendChild(nodeDataName);
		
		
		return nodeDataDiv;
	},
	
	// �����ڵ���ӽڵ� DIV
	createNodeChildDiv:function(nodeDiv , node){
		var nodeChildDiv = this.creaateElement('nodeChildDiv-' + node.id , 'DIV');
		nodeChildDiv.className = "nodeChildDiv";
		if(node.isOpen){
			nodeChildDiv.style.display = "inline";
		}else{
			nodeChildDiv.style.display = "none";
		}
		return nodeChildDiv;
	},
	
	// �����ڵ����ʾ�ӽڵ�ͼ��
	createShowNodeChildDivIcon:function(node,callback){
		// �����ڵ��ͼ��
		var img = this.creaateElement('showNodeChildDivIcon-' + node.id, 'img');
		if(node.isOpen){
			img.src = "/afunms/config/business/img/minus5.gif";
		}else{
			img.src = "/afunms/config/business/img/plus5.gif";
		}
		img.onclick = function(){
			callback.call(img,node);
		};
		return img;
	},
	
	// ��ʾ�������ӽڵ�DIV
	showOrHideNodeChildDiv:function(node){
		var nodeChildDiv = document.getElementById("nodeChildDiv-" + node.id);
		var nodeFolderIcon = document.getElementById("nodeFolderIcon-" + node.id);
		if(node.isOpen){
			// ����� ������
			node.isOpen = false;
			if(nodeChildDiv){
				nodeChildDiv.style.display = "none";
			}
			if(nodeFolderIcon){
				nodeFolderIcon.src = "/afunms/config/business/img/folderClosed.gif";
			}
			this.src = "/afunms/config/business/img/plus5.gif";
		}else{
			// ������� ���
			node.isOpen = true;
			if(nodeChildDiv){
				nodeChildDiv.style.display = "inline";
			}
			if(nodeFolderIcon){
				nodeFolderIcon.src = "/afunms/config/business/img/folderOpen.gif";
			}
			this.src = "/afunms/config/business/img/minus5.gif";
		}
	},
	
	// �����ļ���ͼ��
	createNodeFolderIcon:function(node){
		var nodeFolderIcon = this.creaateElement("nodeFolderIcon-" + node.id, "img");
		if(node.isOpen){
			nodeFolderIcon.src = "/afunms/config/business/img/folderOpen.gif";
		}else{
			nodeFolderIcon.src = "/afunms/config/business/img/folderClosed.gif";
		}
		return nodeFolderIcon;
	},
	
	// �����ڵ�  checkbox ͼ��
	createNodeCheckboxIcon:function(node){
		var img = this.creaateElement('nodeCheckboxIcon-' + node.id, 'img');
		if(node.isSelected){
			img.src = "/afunms/config/business/img/iconCheckDis.gif";
		}else{
			img.src = "/afunms/config/business/img/iconUncheckDis.gif";
		}
		img.onclick = function(){
			if(node.isSelected){
				// ��'ѡ��' ���Ϊ 'δѡ��'
				node.isSelected = false;
				img.src = "/afunms/config/business/img/iconUncheckDis.gif";
			}else{
				// ��� 'δѡ��'  ���Ϊ 'ѡ��'
				node.isSelected = true;
				img.src = "/afunms/config/business/img/iconCheckDis.gif";
			}
		};
		return img;
	},
	
	// ִ���Ƿ�ѡ��
	isSelected:function(node){
		if(node.isSelected){
			this.choose(node);
		}else{
			this.noChoose(node);
		}
	},
	
	// ѡ��
	choose:function(node){
		var checkimage = document.getElementById("nodeCheckboxIcon-" + node.id);
		checkimage.src = "/afunms/config/business/img/iconCheckDis.gif";
		node.isSelected = true;
	},
	
	noChoose:function(node){
		var checkimage = document.getElementById("nodeCheckboxIcon-" + node.id);
		checkimage.src = "/afunms/config/business/img/iconUncheckDis.gif";
		node.isSelected = false;
	},
	
	// ȫѡ
	allChoose:function(){
		for(var i = 0 ; i<this.nodeArray.length;i++){
			node = this.nodeArray[i];
			if(!node.isHadChild){
				this.choose(node);
			}
			
		}
		
	},
	
	// ȫ��ѡ
	allNotChoose:function(){
		var node;
		var checkimage;
		for(var i = 0 ; i<this.nodeArray.length;i++){
			node = this.nodeArray[i];
			if(!node.isHadChild){
				this.noChoose(node);
			}
			
		}
		
	},
	
	antiChoose:function(){
		for(var i = 0 ; i<this.nodeArray.length;i++){
			node = this.nodeArray[i];
			if(!node.isHadChild){
				if(node.isSelected){
					this.noChoose(node);
				}else{
					this.choose(node);
				}
			}
		}
	},
	
	// ���� isSelected ��ȡ����ѡ�еĽڵ�
	getIsSelectedNode:function(isSelected){
		var isSelectedNodeArray = new Array();
		for(var i = 0 ; i < this.nodeArray.length ; i ++){
			var pernode = this.nodeArray[i];
			if(!pernode.isHadChild && pernode.isSelected == isSelected){
				isSelectedNodeArray.push(pernode);
			}
		}
		
		return isSelectedNodeArray;
	},
	
	// ����
	reset:function(){
		for(var i = 0 ; i < this.nodeArray.length ; i++){
			var pernode = this.nodeArray[i];
			if(!pernode.isHadChild){
				pernode.isSelected = this.dataArray[i][3];
				this.isSelected(pernode);
			}
		
		}
	}
};





