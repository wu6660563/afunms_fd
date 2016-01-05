// info ��Ϣ����ʽ
var infoBgColor     = "#F5F5F5";
var infoBgImg       = "";           // path to background image;
var infoBorder      = "solid black 1px";
var infoBorderColor = "#003399";
var infoBorderWidth = 1;
var infoDelay       = 500;          // time span until tooltip shows up [milliseconds]
var infoFontColor   = "#000000";
var infoFontFace    = "����,arial,helvetica,sans-serif";
var infoFontSize    = "12px";
var infoFontWeight  = "normal";     // alternative: "bold";
var infoPadding     = 3;            // spacing between border and content
var infoShadowColor = "";
var infoShadowWidth = 0;
var infoTextAlign   = "left";
var infoTitleColor  = "#ffffff";    // color of caption text
var infoWidth       = 180;

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
}

//ȫ�ֱ���
var xmldoc;						// xmlȫ�ֶ���
var xx;							// ��굱ǰ���㣭ƫ����
var yy;							// ��굱ǰ���㣭ƫ����
var obj = null;				// �����¼��Ŀؼ�����
var objStyle = null;  // �����¼��Ŀؼ�����,���ڸı�ѡ��ͼƬ����ʽ��
var strSearch = null;			// �������ַ���
var line_ids = new Array();	// ��ŵ�ǰ�¼��ؼ����������
var tempArray = new Array();	// ����޸Ĺ��Ķ���id������
var clickObj = null;
var maxWidth = 0;				// �����
var maxHeight = 0;				// ���߶�
var minWidth = 0;				// ��С���
var minHeight = 0;				// ��С�߶�
var strArray = new Array();
var beginx = 0;
var beginy = 0;
var selectStatus = false; // �ж�ѡ��
var objMoveAry = new Array();
var objLeftAry = new Array();
var objTopAry = new Array();
var lineMoveAry = new Array();
var relLineAry = new Array();
var isRemoved = false;
var movedObjFlag = false;		// FIXME: �ñ���δ����
//Ϊ�������ܶ��������------------��1---
var nodeCoorAry = new Array();	// ÿ���ڵ�����꣺ip,x,y
//---------------------------------
window.document.onselectstart = selectStart;

// ʵ��xmlװ�ع���
// url: xml�ļ�
function loadXML(url)
{
	var http = new ActiveXObject("Microsoft.XMLHTTP");
	xmldoc = new ActiveXObject("Microsoft.XMLDOM");
	http.open("POST", url, false);
	http.send();
	xmldoc.async = false;
	xmldoc.loadXML(http.responseText);
	maxWidth = maxHeight = minWidth = minHeight = 0;

	parseData();
}

// ����xml������������չ��
function parseData()
{	
	// ����root�ڵ�
	var dateInfo = xmldoc.getElementsByTagName("root")[0].getAttribute("time");

	// ����nodes�ڵ�
	var nodes = xmldoc.getElementsByTagName("node");	
	for (var i = 0; i < nodes.length; i += 1)
	{
		var node = nodes[i];
		var id = node.getElementsByTagName("id")[0].text;
		var imgValue = "../../resource/"+node.getElementsByTagName("img")[0].text;
		//var imgValue = node.getElementsByTagName("img")[0].text;
		var x = node.getElementsByTagName("x")[0].text;
		var y = node.getElementsByTagName("y")[0].text;
		var ip = node.getElementsByTagName("ip")[0].text
		var alias = node.getElementsByTagName("alias")[0].text;
		var info = node.getElementsByTagName("info")[0].text;
		var menu = node.getElementsByTagName("menu")[0].text;
		//���������õ����飬�������ʼ��---------��2---
		nodeCoorAry.push(ip + "," + x + "," + y);
		//---------------------------------------
		
		getConfine(x, y);		// ��ȡ���߽磬�� maxWidth��maxHeight ��ֵ

		// ��ʾ�豸
		//var img = document.createElement("<v:image lines=''></v:image>");
		var img = document.createElement("v:image");
		img.id = "node_" + id;
		img.style.width = 65;
		img.style.height = 26;
		img.style.position = "absolute";
		img.style.cursor = "hand";
		img.style.left = x;
		img.style.top = y;
		img.src = imgValue;
		document.all.divLayer.appendChild(img);

		aliasHSpace = 10;
	    aliasVSpace = 26;	
	    
		// ��ʾ�豸�ı�
		var divText = document.createElement("div");
		divText.id = "text_" + id;
		divText.style.position = "absolute";
		divText.style.width = "65";
		divText.style.height = "26";
		divText.style.left = parseInt(x, 10) - aliasHSpace;
		divText.style.top = parseInt(y, 10) + aliasVSpace;
		divText.style.fontSize = "12px";
		divText.align = "center";
		if (g_viewFlag == 0)
			divText.innerHTML = alias;
		else
			divText.innerHTML = ip;
		document.all.divLayer.appendChild(divText);
		// ��ʾ�豸��Ϣ
		var divInfo = document.createElement("div");
		divInfo.id = "info_" + id;
		divInfo.style.position = "absolute";
		divInfo.style.border = infoBorder;
		divInfo.style.width = infoWidth;
		divInfo.style.height = "auto";
		divInfo.style.color = infoFontColor;
		divInfo.style.padding = infoPadding;
		divInfo.style.lineHeight = "120%";
		divInfo.style.zIndex = 2;
		divInfo.style.backgroundColor = infoBgColor;
		divInfo.style.left = parseInt(x, 10) + 32;
		divInfo.style.top = parseInt(y, 10);
		divInfo.style.visibility = "hidden";
		divInfo.style.fontSize = "12px";
		divInfo.innerHTML = info;
		document.all.divLayer.appendChild(divInfo);

		// ����"�豸��Ϣ��ʾ"�¼�
		document.all("node_" + id).onmouseover = function() { document.all(this.id.replace("node", "info")).style.visibility = "visible"; };
		document.all("node_" + id).onmouseout = function() { document.all(this.id.replace("node", "info")).style.visibility = "hidden"; };

		//#########���ӡ��豸��Ϣ��ʾ��ʱ��Խ�紦���������߽������Զ���ʾ�����ӷ�Χ֮��   HONGLI ADD ########
		if(divInfo.clientHeight != 0){
			if(parseInt(x, 10) > document.body.clientWidth-infoWidth){
				divInfo.style.left =parseInt(x, 10)-infoWidth;
			} 
			if(parseInt(y, 10) > document.body.clientHeight-divInfo.clientHeight){
				divInfo.style.top = parseInt(y, 10)-divInfo.clientHeight;  
		    }
		}
	    //#############HONG ADD END##########
		
		// ����������ʾ�˵�
		var divMenu = document.createElement("div");
		divMenu.id = "menu_" + id;
		divMenu.style.position = "absolute";
		divMenu.style.width = "auto";
		divMenu.style.height = "auto";
		divMenu.style.zIndex = 2;
		divMenu.style.left = parseInt(x, 10) + 28;
		divMenu.style.top = parseInt(y, 10);
		divMenu.style.visibility = "hidden";
		divMenu.style.border = "solid #000066 1px";
		divMenu.style.backgroundColor = "#F5F5F5";
		divMenu.style.padding = "1px";
		divMenu.style.lineHeight = "100%";
		divMenu.style.fontSize = "12px";
		divMenu.innerHTML = menu;
		document.all.divLayer.appendChild(divMenu);

		//���Ӳ˵��Ĵ����¼�
		document.all("node_" + id).oncontextmenu = function()
		{ 
			if(clickObj != null)
			{
				document.all(clickObj.id.replace("node", "menu")).style.visibility = "hidden";
				clickObj = null;
			}	
			if(objStyle != null)
			{
			  	unSelectImg(objStyle);
			    objStyle = null;
			}	
			clickObj = this;
			objStyle = this;
			selectImg(objStyle);
			document.all(this.id.replace("node", "info")).style.visibility = "hidden";
			document.all(this.id.replace("node", "menu")).style.visibility = "visible"; 			
	   };
	   
		document.all("menu_" + id).onclick = function() { this.style.visibility = "hidden"; };	
		//����"�϶�"�¼�
		document.all("node_" + id).onmousedown = down;
		document.onmousemove = null;
		document.all("node_" + id).onmouseup = up;
	}

	// ����lines�ڵ�
	var lines = xmldoc.getElementsByTagName("line");
	for (var j = 0; j < lines.length; j += 1)
	{
		var lineObj = lines[j];
		var a = lineObj.getElementsByTagName("a")[0].text;
		var b = lineObj.getElementsByTagName("b")[0].text;
		var color = lineObj.getElementsByTagName("color")[0].text;
		var dash = lineObj.getElementsByTagName("dash")[0].text;

		var line = document.createElement("v:line");
		line.id = "line_" + a + "_"+ b;
		line.style.position = "absolute";
		line.style.zIndex = -1;
		line.from = (parseInt(document.all("node_" + a).style.left) + 15) + "," + (parseInt(document.all("node_" + a).style.top) + 8);
		line.to = (parseInt(document.all("node_" + b).style.left) + 15) + "," + (parseInt(document.all("node_" + b).style.top) + 8);
		line.strokecolor = color;
		line.strokeweight = 1;

		line.onmouseover = function() { window.event.srcElement.strokeweight = 2; window.event.srcElement.style.cursor = "hand"; };
		line.onmouseout = function() { window.event.srcElement.strokeweight = 1; window.event.srcElement.style.cursor = "default"; };
		line.onclick = function() { showLineInfo() };

		document.all.divLayer.appendChild(line);
		document.all("node_" + a).lines += '&' + line.id;
		document.all("node_" + b).lines += '&' + line.id;
		
		// ���� stroke ��ǩ
		//window.event.srcElement.stroke.dashstyle = "Solid";
		var stroke = document.createElement("v:stroke");
		stroke.dashstyle = dash;
		document.all(line.id).appendChild(stroke);
	}
		
	var nodeNum = nodes.length;
	if (nodeNum == 0)
	{
		document.write("<br/><br/><br/><br/><br/>");
		document.write("<center>��û��ѡ��۲������ͼ����ѡ����ȷ������ͼ</center>");
	}
	zoomProcDlg("out");//�ر�����
}
//end of ParseData();

// ����ѡ��
function selectStart()
{
	if (window.event.srcElement.id == "divLayer" || 
		window.event.srcElement.tagName == "BODY")
	{
		obj = null;
		selectStatus = true;
		imgTop.style.height = imgTop.style.width = imgLeft.style.height = imgLeft.style.width = imgBottom.style.height = imgBottom.style.width = imgRight.style.height = imgRight.style.width = 1;
		imgTop.style.visibility = imgLeft.style.visibility = imgBottom.style.visibility = imgRight.style.visibility = "visible";
		imgTop.style.zIndex = imgLeft.style.zIndex = imgBottom.style.zIndex = imgRight.style.zIndex = 9999;
		beginx = event.x;
		beginy = event.y;
		document.onmousemove = move;
		document.onmouseup = up;
	}
}


function bodyDown()
{
	if (window.event.srcElement.tagName == "BODY") 
	{
		if (clickObj != null )
		{
			document.all(clickObj.id.replace("node", "menu")).style.visibility = "hidden"; 
			unSelectImg(clickObj);
			clickObj = null;
		}
		if (objStyle != null)
		{
		   unSelectImg(objStyle);
		   objStyle = null;
		}
		if (document.all("containImgDiv"))
		{
			if(isRemoved == false)
			{
				rmvContainedImg();	
			}
			isRemoved = true;
		}
	}
	else if (window.event.srcElement.tagName == "DIV" && 
			 window.event.srcElement.id == "containImgDiv")
	{
		if(isRemoved == false)
		{
			rmvContainedImg();	
		}
		isRemoved = true;
	}
}

// ����divLayer�հ״�ʱ
function divLayerDown()
{
	if (window.event.srcElement.id == "divLayer")
	{
		if (clickObj != null )
		{
			document.all(clickObj.id.replace("node", "menu")).style.visibility = "hidden"; 
			unSelectImg(clickObj);
			clickObj = null;
		}
		if (objStyle != null)
		{
		   unSelectImg(objStyle);
		   objStyle = null;
		}
		if (document.all("containImgDiv"))
		{
			if(isRemoved == false)
			{
				rmvContainedImg();	
			}
			isRemoved = true;
		}
	}
	else if (window.event.srcElement.tagName == "DIV" && 
			 window.event.srcElement.id == "containImgDiv")
	{
		if(isRemoved == false)
		{
			rmvContainedImg();	
		}
		isRemoved = true;
	}
}

// �������������¼�
function down()
{
	if (event.button != 1)
	{  // �����Ҽ��϶�
		return false;
	}
	if (clickObj != null)
	{ // �����Ҽ�����ͼ���Ĳ˵�
		document.all(clickObj.id.replace("node", "menu")).style.visibility = "hidden";
		clickObj = null;
	}
	this.setCapture();
	if (document.all("containImgDiv") && objMoveAry.contains(this))
	{
		xx = event.x - document.all("containImgDiv").offsetLeft;
		yy = event.y - document.all("containImgDiv").offsetTop;
		this.setCapture();
		obj = this;
	}
	else
	{
		if(document.all("containImgDiv"))
		{
			rmvContainedImg();
		}
		xx = event.x - this.offsetLeft;
		yy = event.y - this.offsetTop;
		if (objStyle != null)
		{
			unSelectImg(objStyle);
			objStyle = null;
		}
		obj = this;
		objStyle = this;
		selectImg(objStyle);
		if (obj.lines != null)
		{
			line_ids = obj.lines.split("&");
		}
		strSearch = obj.id.replace(/^[^_]*/, '') + '_';
	}
	document.onmousemove = move;
}

// ��������ƶ��¼�
function move()
{
	var eventX = window.event.x;
	var eventY = window.event.y;
	if (eventX > 0 & eventY > 0 & eventX < window.screen.width & eventY < window.screen.height)
	{
		if(obj != null)
		{
			var tempX = eventX - xx;
			var tempY = eventY - yy;
			if (document.all("containImgDiv"))
			{
				document.all("containImgDiv").style.left = tempX;
				document.all("containImgDiv").style.top = tempY;
				if (line_ids.length > 0)
				{
					for (var i = 0; i < line_ids.length; i++)
					{
						for(var j = 0; j < line_ids[i].length-1; j++)
						{
							var iElem = document.all(line_ids[i][j]);
							if (iElem == null)
							{
								continue;
							}
							var lth = line_ids[i].length-1;
							var tmpStr = document.all(line_ids[i][lth]).id.replace(/[^_]*/, '') + '_';
							var iLeft = parseInt(document.all(line_ids[i][lth]).style.left) + parseInt(document.all("containImgDiv").style.left) + 15;
							var iTop = parseInt(document.all(line_ids[i][lth]).style.top) + parseInt(document.all("containImgDiv").style.top) + 8;
							if (line_ids[i][j].search(tmpStr) != -1)
							{
								iElem.from = iLeft + "," + iTop;
							}
							else
							{
								iElem.to = iLeft + "," + iTop;
							}
						}
					}
				}
			}
			else
			{
				var textStyle = document.all(obj.id.replace("node", "text")).style;
				var infoStyle = document.all(obj.id.replace("node", "info")).style;
				var menuStyle = document.all(obj.id.replace("node", "menu")).style;
				obj.style.left = tempX;
				obj.style.top = tempY;
				textStyle.left = tempX - aliasHSpace;
				textStyle.top = tempY + aliasVSpace;
				infoStyle.left = tempX + 32;
				infoStyle.top = tempY;
				//#########������ק�豸ʱ���豸��Ϣ��ʾ����Խ�紦���������߽������Զ���ʾ�����ӷ�Χ֮��   HONGLI ADD #########
				var divInfo = document.getElementById(obj.id.replace("node", "info"));
				if(parseInt(tempX, 10) > document.body.clientWidth-infoWidth-24){
					infoStyle.left =parseInt(tempX, 10)-infoWidth;
				}
				if(parseInt(tempY, 10) > document.body.clientHeight-divInfo.clientHeight){  
					infoStyle.top = parseInt(tempY, 10)-divInfo.clientHeight;  
			    }
		   		//#############HONG ADD END##########
				menuStyle.left = tempX + 28;
				menuStyle.top = tempY;
				var iLeft = parseInt(obj.style.left) + 15;
				var iTop = parseInt(obj.style.top) + 8;
				for (var i = 1; i < line_ids.length; i += 1)
				{
					var iElem = document.all(line_ids[i]);
					if (line_ids[i].search(strSearch) != -1)
					{
						iElem.from = iLeft + "," + iTop;
					}
					else
					{
						iElem.to = iLeft + "," + iTop;
					}
				}
			}
			movedObjFlag = true;
		}
		if (selectStatus)
		{  // ����ѡ��
			var tmpx = window.event.x;
			var tmpy = window.event.y;
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
		}
	}
}

// ���������ͷ��¼�
function up()
{
	if (obj != null)
	{
		this.releaseCapture();
		if (movedObjFlag == true)
		{
			if (document.all("containImgDiv"))
			{
				for (var i = 0; i < objMoveAry.length; i++)
				{
					if (!tempArray.contains(objMoveAry[i]))
					{
						tempArray.push(objMoveAry[i]);
					}
				}
			}
			else if (!tempArray.contains(this))
			{
				tempArray.push(this);
			}
		}
		obj = null;
		if(!document.all("containImgDiv"))
		{
			line_ids.length = 0;
		}		
		strSearch = null;
	}
	if (selectStatus)
	{
		imgTopX = parseInt(imgTop.style.left);   // ȡ�òʿ�Ĵ�Сλ�ã�������λ��������ڵĽڵ���ı���ǩ����objMoveAry����
		imgTopY = parseInt(imgTop.style.top);
		imgBottomX = parseInt(imgTop.style.left) + parseInt(imgTop.style.width);
		imgBottomY = parseInt(imgTop.style.top) + parseInt(imgLeft.style.height);
		var allImgNodes = document.getElementsByTagName("image");
		var j = 0;
		objMoveAry.length = 0;
		for (var i = 0; i < allImgNodes.length; i++)
		{
			if (parseInt(allImgNodes[i].style.left) + parseInt(document.all.divLayer.style.left) >= imgTopX 
			   && parseInt(allImgNodes[i].style.left) + parseInt(document.all.divLayer.style.left) <= imgBottomX 
			   && parseInt(allImgNodes[i].style.top) + parseInt(document.all.divLayer.style.top) >= imgTopY 
			   && parseInt(allImgNodes[i].style.top) + parseInt(document.all.divLayer.style.top) <=imgBottomY
			   && parseInt(allImgNodes[i].style.left) + parseInt(allImgNodes[i].style.width) + parseInt(document.all.divLayer.style.left) <= imgBottomX 
			   && parseInt(allImgNodes[i].style.top) + parseInt(allImgNodes[i].style.height) + parseInt(document.all.divLayer.style.top) <= imgBottomY)
			{
				objMoveAry[j] = allImgNodes[i];
				selectImg(objMoveAry[j]);
				j++;
			}
		}
		if (objMoveAry.length > 0)
		{
			var containImgDiv = document.createElement("div");
			containImgDiv.id = "containImgDiv";
			containImgDiv.style.position = "absolute";
			containImgDiv.style.left = imgTopX;
			containImgDiv.style.top = imgTopY;
			containImgDiv.style.width = imgTop.style.width;
			containImgDiv.style.height = imgLeft.style.height;
			document.all.divLayer.appendChild(containImgDiv);
			for (var i = 0; i < objMoveAry.length; i++)
			{
				var txtObj = document.all(objMoveAry[i].id.replace("node","text"));
				var infoObj = document.all(objMoveAry[i].id.replace("node","info"));
				var menuObj = document.all(objMoveAry[i].id.replace("node","menu"));
				document.all("containImgDiv").appendChild(objMoveAry[i]);
				document.all("containImgDiv").appendChild(txtObj);
				document.all("containImgDiv").appendChild(infoObj);
				document.all("containImgDiv").appendChild(menuObj);
				objMoveAry[i].style.position = "absolute";
				objMoveAry[i].style.left = parseInt(objMoveAry[i].style.left) - parseInt(containImgDiv.style.left);
				objMoveAry[i].style.top = parseInt(objMoveAry[i].style.top) - parseInt(containImgDiv.style.top);
				txtObj.style.position = "absolute";
				txtObj.style.left = parseInt(txtObj.style.left) - parseInt(containImgDiv.style.left);
				txtObj.style.top = parseInt(txtObj.style.top) - parseInt(containImgDiv.style.top);
				infoObj.style.position = "absolute";
				infoObj.style.left = parseInt(infoObj.style.left) - parseInt(containImgDiv.style.left);
				infoObj.style.top = parseInt(infoObj.style.top) - parseInt(containImgDiv.style.top);
				menuObj.style.position = "absolute";
				menuObj.style.left = parseInt(menuObj.style.left) - parseInt(containImgDiv.style.left);
				menuObj.style.top = parseInt(menuObj.style.top) - parseInt(containImgDiv.style.top);
			}
			isRemoved = false;
			line_ids.length = 0;
			lineMoveAry.length = 0; // ��ż��ص�containImgDiv��������߶���
			for (var i = 0; i < objMoveAry.length; i++)
			{
				if (objMoveAry[i].lines == null)
				{
					// ����豸û������
					continue;
				}
				if (objMoveAry[i].lines.split("&").length > 0)
				{
					var twoDemAry = new Array();
					twoDemAry = objMoveAry[i].lines.split("&");
					var tmpRelAry = new Array();
					for (var j = 1; j < twoDemAry.length; j++)
					{
						if (! document.all(twoDemAry[j]))
						{
							continue;
						}
						var conObjAry = twoDemAry[j].replace("line_","").split("_");
						if ((document.all("containImgDiv").all("node_" + conObjAry[0])) && (document.all("containImgDiv").all("node_" + conObjAry[1]))) {
							var addLineObj = document.all(twoDemAry[j]);
							lineMoveAry.push(addLineObj);		
							addLineObj.style.visibility = "hidden";
							addLineObj.style.position = "absolute";
							addLineObj.from = (parseInt(document.all("node_" + conObjAry[0]).style.left) + 15) + "," + (parseInt(document.all("node_" + conObjAry[0]).style.top) + 8);
							addLineObj.to = (parseInt(document.all("node_" + conObjAry[1]).style.left) + 15) + "," + (parseInt(document.all("node_" + conObjAry[1]).style.top) + 8);
						    document.all("containImgDiv").appendChild(addLineObj);
							addLineObj.style.visibility = "visible";
						}
						else
						{
							relLineAry.push(twoDemAry[j]);  // ���������div���������ߣ��ǰ��������ŵ���������
							tmpRelAry.push(twoDemAry[j]);   // ���뵱ǰ�ڵ���ص����ߣ�û�����ڵ�ǰdiv�У����ŵ���������
						}
					}
					if (tmpRelAry.length > 0)
					{
						tmpRelAry.push(objMoveAry[i].id);
						line_ids.push(tmpRelAry);	// line_ids�д�����������div�ڵ����������߶���(û�����ڵ�ǰdiv��)id
					}
				}
			}
		}
		selectStatus = false;
		imgTop.style.visibility = "hidden";
		imgLeft.style.visibility = "hidden";
		imgBottom.style.visibility = "hidden";
		imgRight.style.visibility = "hidden";
	}
	document.onmousemove = null;
}

function rmvContainedImg()
{
	var tmpDivLeft = document.all("containImgDiv").style.left;
	var tmpDivTop = document.all("containImgDiv").style.top;
	for (var i = 0; i < objMoveAry.length; i++)
	{
		unSelectImg(objMoveAry[i]);	
		var txtObj = document.all(objMoveAry[i].id.replace("node","text"));
		var infoObj = document.all(objMoveAry[i].id.replace("node","info"));
		var menuObj = document.all(objMoveAry[i].id.replace("node","menu"));
		document.all.divLayer.appendChild(objMoveAry[i]);
		document.all.divLayer.appendChild(txtObj);
		document.all.divLayer.appendChild(infoObj);
		document.all.divLayer.appendChild(menuObj);
		objMoveAry[i].style.left = parseInt(objMoveAry[i].style.left) + parseInt(tmpDivLeft);
		objMoveAry[i].style.top = parseInt(objMoveAry[i].style.top) + parseInt(tmpDivTop);
		txtObj.style.left = parseInt(txtObj.style.left) + parseInt(tmpDivLeft);
		txtObj.style.top = parseInt(txtObj.style.top) + parseInt(tmpDivTop);
		infoObj.style.left = parseInt(infoObj.style.left) + parseInt(tmpDivLeft);
		infoObj.style.top = parseInt(infoObj.style.top) + parseInt(tmpDivTop);
		menuObj.style.left = parseInt(menuObj.style.left) + parseInt(tmpDivLeft);
		menuObj.style.top = parseInt(menuObj.style.top) + parseInt(tmpDivTop);
	}			
	if (lineMoveAry.length > 0)
	{
		for (var i = 0; i < lineMoveAry.length; i++)
		{
			document.all.divLayer.appendChild(lineMoveAry[i]);
			lineMoveAry[i].style.visibility = "hidden";
			var conObjAry = lineMoveAry[i].id.replace("line_","").split("_");
			var fromObj = document.all("node_" + conObjAry[0]);
			var toObj = document.all("node_" + conObjAry[1]);
			lineMoveAry[i].from = (parseInt(fromObj.style.left) + 15) + "," + (parseInt(fromObj.style.top) + 8);
			lineMoveAry[i].to =  (parseInt(toObj.style.left) + 15) + "," + (parseInt(toObj.style.top) + 8);
			lineMoveAry[i].style.visibility = "visible";
		}
	}
	lineMoveAry.length = 0;
	objMoveAry.length = 0;
	line_ids.length = 0;
	document.all.divLayer.removeChild(document.all("containImgDiv"));
}

// ѡ�нڵ�����ʽ
function selectImg(objSty)
{
	document.all(objSty.id.replace("node_","text_")).style.background = "#335EA8";
	document.all(objSty.id.replace("node_","text_")).style.color = "#FFFFFF";
	document.all(objSty.id.replace("node_","text_")).style.height = "11px";
	//document.all(objSty.id.replace("node_","text_")).style.border = "#555555 1px dotted";
	/*if(objSty.src.indexOf("_col.png") == -1){
		objSty.src = objSty.src.replace(".png","_col.png");
		document.all(objSty.id.replace("node_","text_")).style.background = "#94B4DD";
	}*/
}
// ȡ��ѡ�нڵ����ʽ
function unSelectImg(objSty)
{
	document.all(objSty.id.replace("node_","text_")).style.background = "";
	document.all(objSty.id.replace("node_","text_")).style.color = "#000000";
	//document.all(objSty.id.replace("node_","text_")).style.border = "";
    /*if(objSty.src.indexOf("_col.png") != -1){
		objSty.src = objSty.src.replace("_col.png",".png");
		document.all(objSty.id.replace("node_","text_")).style.background = "";
	}*/	
}

// ����
function save()
{
	if(document.all("containImgDiv"))
	{
		rmvContainedImg();
	}
	var nodes = xmldoc.getElementsByTagName("node");
	for (var i = 0; i < tempArray.length; i += 1)
	{
		for (var j = 0; j < nodes.length; j += 1)
		{
			var node = nodes[j];
			var id = "node_" + node.getElementsByTagName("id")[0].text;

			if (tempArray[i].id == id)
			{
				node.getElementsByTagName("x")[0].text = tempArray[i].style.left;
				node.getElementsByTagName("y")[0].text = tempArray[i].style.top;
			}
		}
	}
	document.all("hidXml").value = xmldoc.xml;
	document.frmMap.submit();
}

// �ж�X, Y�Ƿ������нڵ���������ֵ
function getConfine(x, y)
{
	var coorX;
	var coorY;
	
	if (x.indexOf("px") >= 0)
	{
		coorX = x.substring(0, x.length-2);
		coorX = parseInt(coorX);
	}
	else {
		coorX = parseInt(x);
	}
	
	if (y.indexOf("px") >= 0)
	{
		coorY = y.substring(0, y.lenght-2);
		coorY = parseInt(coorY);
	}
	else
	{
		coorY = parseInt(y);
	}
	
	if (coorX > maxWidth)
		maxWidth = coorX;
	if (coorY > maxHeight)
		maxHeight = coorY;
		
	if (coorX < minWidth)
		minWidth = coorX;
	if (coorY < minHeight)
		minHeight = coorY;
}

// ͨ�����̵ġ��������ҡ������������ͼλ��
function document.onkeydown()
{
//-----�Ȳ���--------��3-------
/*	try
	{
		if (document.all.blind.style.visibility == "visible")
			return;
	}
	catch (exception)
	{
	}
	*/
//----------------------
	var left = divLayer.style.left;
	var top = divLayer.style.top;
	
	//left = left.substring(0, left.length - 2);
	//top = top.substring(0, top.length - 2);

	left = parseInt(left);
	top = parseInt(top);
	//alert(event.keyCode); return;
	switch (event.keyCode)
	{
		/*
		case 81:		// Q �����л���ͼ
			if (g_viewFlag == 0) {
				g_viewFlag = 1;
				var target = "showMap.jsp?filename=" + filename + "&viewflag=1";
				parent.mainFrame.location = target;
			}
			else if (g_viewFlag == 1) {
				g_viewFlag = 0;
				var target = "showMap.jsp?filename=" + filename + "&viewflag=0";
				parent.mainFrame.location = target;
			}
			break;
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
			
		case 69:		// E ����/��ʾ�������
			showController(!controllerState);
			break;

		default:
			break;
	}
	
	/*if (event.keyCode == 37) {
		// ����
		//divLayer.style.left = (left - 50);
		moveAction('left');
	}
	else if (event.keyCode == 38) {
		// ����
		//divLayer.style.top = (top - 50);
		moveAction('up');
	}
	else if (event.keyCode == 39) {
		// ����
		//divLayer.style.left = (left + 50);
		moveAction('right');
	}
	else if (event.keyCode == 40) {
		// ����
		//divLayer.style.top = (top + 50);
		moveAction('down');
	}*/
}


/**** �����ƶ�������ĸ���ť�¼������Ű�ť�¼� -- ��ʼ ****/

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

///-----------------��4----
	closeAnchor();	// �ر�׼��
//	hideLineTip();	// ������·��ʾ
//------------------------

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
	divLayer.style.left = 0;//parseInt(mainX);--���Ǹĺ�ģ����ڻָ�ԭ����λ��-----��5--
	divLayer.style.top = 0;//parseInt(mainY);
}

function updatePosition() 
{
	var divLeft = parseInt(divLayer.style.left);
	var divTop = parseInt(divLayer.style.top);
	
	//divLeft = divLeft.substring(0, divLeft.length - 2);
	//divTop = divTop.substring(0, divTop.length - 2);

	left = parseInt(divLeft);
	top = parseInt(divTop);
}

function zoomAll(state) 
{
	closeAnchor();//---------�ĺ�������-
	if (divLayer.style.zoom == "") 
	{
		divLayer.style.zoom = 1.0;
	}
	
	if (state == "out") 
	{
		// ��С
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
		// �Ŵ�
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
		// ��ԭ
		if (divLayer.style.zoom != "") 
		{
			divLayer.style.zoom = 1.0;
			zoom = 1.0;
		}
	}
}

function showController(show) 
{
    document.all.moveController.style.visibility = "visible";
    document.all.sizeController.style.visibility = "visible";
	controllerState = true;
	return true;
}

// ------ ͼƬ����Ч������ - ��ʼ ------

function swapImage(imageID, imageSrc) 
{
	document.all(imageID).src = imgPath+imageSrc;
}

// ------ ͼƬ����Ч������ - ���� ------

function loadMoveController() 
{
	document.write('<div id="moveController" style="position:absolute;top:5px;left:5px;z-index:999;background-image:url(image/controller_bg.gif);">');
	document.write('<table width="66" border="0" cellspacing="0" cellpadding="0" style="font-size:9pt;"><tr><td height="19" width="19"></td><td width="28">');
	document.write('<img src="'+imgPath+'image/topo/arrow_up.gif" onmouseout="javascript:swapImage(\'image_up\', \'image/topo/arrow_up.gif\');" onmouseover="javascript:swapImage(\'image_up\', \'image/topo/arrow_up_over.gif\');" alt="�����ƶ� | W" width="28" height="19" onclick="javascript:moveAction(\'up\');" style="cursor:hand;" name="image_up" id="image_up" />');
	document.write('</td><td height="19" width="19"></td></tr><tr><td>');
	document.write('<img src="'+imgPath+'image/topo/arrow_left.gif" onmouseout="javascript:swapImage(\'image_left\', \'image/topo/arrow_left.gif\');" onmouseover="javascript:swapImage(\'image_left\', \'image/topo/arrow_left_over.gif\');" alt="�����ƶ� | A" width="19" height="28" onclick="javascript:moveAction(\'left\');" style="cursor:hand;" name="image_left" id="image_left" />');
	document.write('</td><td align="center" valign="middle" style="text-align:center;">');
	document.write('<img src="'+imgPath+'image/topo/arrow_center.gif" onmouseout="javascript:swapImage(\'image_center\', \'image/topo/arrow_center.gif\');" onmouseover="javascript:swapImage(\'image_center\', \'image/topo/arrow_center_over.gif\');" alt="��λ | R" width="19" height="19" onclick="javascript:moveAction(\'origin\');" style="cursor:hand;" name="image_center" id="image_center" />');
	document.write('</td><td>');
	document.write('<img src="'+imgPath+'image/topo/arrow_right.gif" onmouseout="javascript:swapImage(\'image_right\', \'image/topo/arrow_right.gif\');" onmouseover="javascript:swapImage(\'image_right\', \'image/topo/arrow_right_over.gif\');" alt="�����ƶ� | D" width="19" height="28" onclick="javascript:moveAction(\'right\');" style="cursor:hand;" name="image_right" id="image_right" />');
	document.write('</td></tr><tr><td></td><td>');
	document.write('<img src="'+imgPath+'image/topo/arrow_down.gif" onmouseout="javascript:swapImage(\'image_down\', \'image/topo/arrow_down.gif\');" onmouseover="javascript:swapImage(\'image_down\', \'image/topo/arrow_down_over.gif\');" alt="�����ƶ� | S" width="28" height="19" onclick="javascript:moveAction(\'down\');" style="cursor:hand;" name="image_down" id="image_down" />');
	document.write('</td><td></td></tr></table></div>');
}

function loadSizeController() 
{
	document.write('<div id="sizeController" style="position:absolute;top:80px;left:8px;z-index:998;background-image:url(image/controller_bg2.gif);width:60px;">');
	document.write('<table width="58" height="20" border="0" cellspacing="0" cellpadding="0"><tr><td width="18" height="20" style="padding-left:2px;">');
	document.write('<img src="'+imgPath+'image/topo/zoom_out.gif" onmouseout="javascript:swapImage(\'image_out\', \'image/topo/zoom_out.gif\');" onmouseover="javascript:swapImage(\'image_out\', \'image/topo/zoom_out_over.gif\');" alt="��С��ͼ | Z" width="18" height="16" onclick="javascript:zoomAll(\'out\');" style="cursor:hand;" name="image_out" id="image_out" />');
	document.write('</td><td align="center" style="text-align:center;">');
	document.write('<img src="'+imgPath+'image/topo/zoom_reset.gif" onmouseout="javascript:swapImage(\'image_reset\', \'image/topo/zoom_reset.gif\');" onmouseover="javascript:swapImage(\'image_reset\', \'image/topo/zoom_reset_over.gif\');" alt="��ԭ | X" width="18" height="16" onclick="javascript:zoomAll();" style="cursor:hand;" name="image_reset" id="image_reset" />');
	document.write('</td><td width="18" height="20">');
	document.write('<img src="'+imgPath+'image/topo/zoom_in.gif" onmouseout="javascript:swapImage(\'image_in\', \'image/topo/zoom_in.gif\');" onmouseover="javascript:swapImage(\'image_in\', \'image/topo/zoom_in_over.gif\');" alt="�Ŵ���ͼ | C" width="18" height="16" onclick="javascript:zoomAll(\'in\');" style="cursor:hand;" name="image_in" id="image_in" />');
	document.write('</td></tr></table></div>');
}

/**** �����ƶ�������ĸ���ť�¼������Ű�ť�¼� -- ���� ****/

function displayInfoFrame() 
{
	var divTop = document.all.linkedline.style.top;
	//divTop = divTop.substring(0, divTop.length - 2);
	var top = parseInt(divTop);
	if (top > (defaultY + parentScrollTop)) 
	{
		document.all.linkedline.style.top = (top - 4) + "px";
	}
	else {
		document.all.linkedline.style.top = (defaultY + parentScrollTop) + "px";
		clearTimer();
	}
}

/**** �༭��� -- ��ʼ ****/

var offsetX;	// ƫ��λ��X
var offsetY;	// ƫ��λ��Y

function loadEditPanel()
{
	document.write('<div class="edit_panel" id="editPanel" title="�༭���" onmousedown="editMouseDown()" onmouseup="editMouseUp()" onmousemove="">');
	document.write('<table width="100%" border="0" cellspacing="0" cellpadding="0" style="padding:1px;background-color:#F5F5F5;"><tr><td>');
	document.write('<table width="100%" border="0" cellspacing="0" cellpadding="0"><tr>');
	document.write('<td align="center" style="padding:4px;padding-bottom:2px;border-bottom:#666666 1px solid;background-color:#3979B9;cursor:move;"><font color="#F5F5F5"><b>�༭���</b></font></td>');//background-color:#9FBDE2;
	document.write('</tr></table>');
	document.write('</td></tr>');
	document.write('<tr><td>');
	document.write('<table width="100%" border="0" cellspacing="0" cellpadding="0"><tr>');
	document.write('<td align="center" style="padding:2px;"><input type="button" name="adddevice" class="button_add_device_out" onmouseover="javascript:buttonAddDeviceOver();" onmouseout="javascript:buttonAddDeviceOut();" onclick="javascript:addDevice();" value="����豸" title="����ͼ������豸"/></td>');
	document.write('</tr><tr>');
	document.write('<td align="center" style="padding:2px;"><input type="button" name="deldevice" class="button_del_device_out" onmouseover="javascript:buttonDelDeviceOver();" onmouseout="javascript:buttonDelDeviceOut();" onclick="javascript:delDevice();" value="ɾ���豸" title="����ͼ��ɾ���豸"/></td>');
	document.write('</tr><tr>');
	//document.write('<td align="center" style="padding:2px;"><input type="button" name="editdevice" class="button_edit_device_out" onmouseover="javascript:buttonEditDeviceOver();" onmouseout="javascript:buttonEditDeviceOut();" onclick="javascript:editDevice();" value="�༭�豸" title="�༭��ͼ���Ѵ��ڵ��豸"/></td>');
	//document.write('</tr><tr>');
	document.write('<td align="center" style="padding:2px;"><input type="button" name="addline" class="button_add_line_out" onmouseover="javascript:buttonAddLineOver();" onmouseout="javascript:buttonAddLineOut();" onclick="javascript:addLine();" value="�����·" title="����ͼ�������·"/></td>');
	document.write('</tr><tr>');
	document.write('<td align="center" style="padding:2px;"><input type="button" name="delline" class="button_del_line_out" onmouseover="javascript:buttonDelLineOver();" onmouseout="javascript:buttonDelLineOut();" onclick="javascript:delLine();" value="ɾ����·" title="����ͼ��ɾ����·"/></td>');
	document.write('</tr></table>');
	document.write('</td></tr></table>');
	document.write('</div>');
}

function updateEditPanel(x, y, display)
{
	document.all.editPanel.style.left = x + "px";
	document.all.editPanel.style.top = y + "px";
	if (display)
	{
		document.all.editPanel.style.visibility = "visible";
	}
	else
	{
		document.all.editPanel.style.visibility = "hidden";
	}
}

function queryEditPanel()
{
	var divLeft = document.all.editPanel.style.left;
	//divLeft = divLeft.substring(0, divLeft.length - 2);
	var x = parseInt(divLeft);
	
	var divTop = document.all.editPanel.style.top;
	//divTop = divTop.substring(0, divTop.length - 2);
	var y = parseInt(divTop);
	
	var display = false;
	if (document.all.editPanel.style.visibility == "visible")
	{
		display = true;
	}
	else
	{
		display = false;
	}
	
	var res = new Array();
	res[0] = x;
	res[1] = y;
	res[2] = display;
	return res;
}

// �������������¼�
function editMouseDown()
{
	document.all.editPanel.setCapture();
	offsetX = event.x - document.all.editPanel.offsetLeft;
	offsetY = event.y - document.all.editPanel.offsetTop;
	document.all.editPanel.onmousemove = editMouseMove;
}


// ��������ƶ��¼�
function editMouseMove()
{
	var eventX = window.event.x;
	var eventY = window.event.y;

	var tempX = eventX - offsetX;
	var tempY = eventY - offsetY;
	
	updateEditPanel(tempX, tempY, true);
}


// ���������ͷ��¼�
function editMouseUp()
{
	document.all.editPanel.releaseCapture();
	document.all.editPanel.onmousemove = null;
}

function addDevice()
{
	openProcDlg();  //��ʾ����
	//status����������ʾ����ҳ�Ի���
	var status = "dialogHeight:200px;dialogWidth:400px;status:0;help:0;edge:sunken;scroll:0;center:yes;resizable:no";
	var value = window.showModalDialog("addDevice.jsp", window, status);
	if (value == null)
	{
		return;
	}
	
	var tmpArray = value.split("#");
	var page = "addDevice.jsp";
	if (tmpArray.length == 2)
	{
		page += "?ipaddress=" + tmpArray[0];
		page += "&linked=" + tmpArray[1];
	}
	else
	{
		alert("ҳ�������������");
		return;
	}

	openProcDlg();

	status = "dialogHeight:170px;dialogWidth:400px;status:0;help:0;edge:sunken;scroll:0;center:yes;resizable:no";
	window.showModalDialog(page, window, status);
}

function delDevice()
{
	openProcDlg(); //��ʾ����
	var status = "dialogHeight:280px;dialogWidth:400px;status:0;help:0;edge:sunken;scroll:0;center:yes;resizable:no";
	var value = window.showModalDialog("delDevice.jsp", window, status);
	if (value == null)
	{
		return;
	}

	var page = "delDevice.jsp";
	page += "?deleted=" + value;
	
	openProcDlg();
	status = "dialogHeight:170px;dialogWidth:400px;status:0;help:0;edge:sunken;scroll:0;center:yes;resizable:no";
	window.showModalDialog(page, window, status);
}

function addLine()
{
	openProcDlg();
	var status = "dialogHeight:350px;dialogWidth:600px;status:0;help:0;edge:sunken;scroll:0;center:yes;resizable:no";
	var value = window.showModalDialog("addLink.jsp", window, status);
	if (value == null) 
	{
		return;
	}
	
	var tmp = value.split(",");
	var page = "addLink.jsp";
	page += "?line=" + tmp[0];
	page += "&ifIndex=" + tmp[1];
	
	openProcDlg();
	status = "dialogHeight:170px;dialogWidth:400px;status:0;help:0;edge:sunken;scroll:0;center:yes;resizable:no";
	window.showModalDialog(page, window, status);
}

function delLine()
{
	openProcDlg();
	var status = "dialogHeight:400px;dialogWidth:400px;status:0;help:0;edge:sunken;scroll:0;center:yes;resizable:no";
	var value = window.showModalDialog("delLink.jsp", window, status);
	if (value == null) 
	{
		return;
	}
	
	var page = "delLink.jsp";
	page += "?deleted=" + value;
	
	openProcDlg();
	status = "dialogHeight:170px;dialogWidth:400px;status:0;help:0;edge:sunken;scroll:0;center:yes;resizable:no";
	window.showModalDialog(page, window, status);
}

function buttonAddDeviceOut() 
{
	window.event.srcElement.className="button_add_device_out";
}
function buttonAddDeviceOver() 
{
	window.event.srcElement.className="button_add_device_over";
}

function buttonDelDeviceOut() 
{
	window.event.srcElement.className="button_del_device_out";
}
function buttonDelDeviceOver() 
{
	window.event.srcElement.className="button_del_device_over";
}

function buttonEditDeviceOut() 
{
	window.event.srcElement.className="button_edit_device_out";
}
function buttonEditDeviceOver() 
{
	window.event.srcElement.className="button_edit_device_over";
}

function buttonAddLineOut() 
{
	window.event.srcElement.className="button_add_line_out";
}
function buttonAddLineOver() 
{
	window.event.srcElement.className="button_add_line_over";
}
function buttonDelLineOut() 
{
	window.event.srcElement.className="button_del_line_out";
}
function buttonDelLineOver() 
{
	window.event.srcElement.className="button_del_line_over";
}


/**** �༭��� -- ���� ****/


/**** ������� -- ��ʼ ****/
//����Ҫ��һ�������ƶ�����---��μ����ƶ��ķ���ʹ�С

var anchorPos = new Array();
anchorPos[0] = (viewWidth - 88) / 2;
anchorPos[1] = 200;
var flashTimer;
var closeAncTimer;

//var mappingIP = window.parent.bottomFrame.getMappingIP();

document.write("<div id=\"postLayer\" style=\"position:absolute;left:"+ anchorPos[0] +"px;top:"+ anchorPos[1] +"px;visibility:hidden;z-index:993;width:88px;height:100px;\">");
document.write("<table width=\"100%\" height=\"100\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr>");
document.write("<td style=\"border:#FF0000 2px dotted\">&nbsp;</td>");
document.write("</tr></table>");
document.write("</div>");


function getNodeCoor(ip)
{
	for (var i = 0; i < nodeCoorAry.length; ++i)
	{
		var tmp = nodeCoorAry[i].split(",");
		if (tmp[0] == ip)
		{
			var coor = new Array();
			coor[0] = tmp[1];
			coor[1] = tmp[2];
			return coor;
		}
	}
/*	
	var key = null;
	// �� nodeIPMap �в��������������IP
	for (var i = 0; i < exDevIPAry.length; ++i)
	{
		if (mappingIP.containsKey(exDevIPAry[i]))
		{
			var element = mappingIP.get(exDevIPAry[i]);
			var value = element.value;
			for (var j = 0; j < value.length; ++j)
			{
				if (value[j] == ip)
				{
					key = element.key;
				}
			}
		}
	}
	
	if (key != null)
	{
		for (var i = 0; i < nodeCoorAry.length; ++i)
		{
			var tmp = nodeCoorAry[i].split(",");
			if (tmp[0] == key)
			{
				var coor = new Array();
				coor[0] = tmp[1];
				coor[1] = tmp[2];
				return coor;
			}
		}
		return "�������� "+ ip +" ��Ӧ���豸�������豸δ��������ͼ�С�\r\n�뽫��ͼ��ȫչ�����ٽ��в��ҡ�";
	}
	*/
	return null;
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

// ���ݽڵ�������ƶ�����
// ������������ϵ�һ���ڵ����꣬�ƶ�֮��ʹ�ڵ�λ�� Anchor ��
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

/**** ������� -- ���� ****/