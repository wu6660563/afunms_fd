var selectedItem = null;

var targetWin = "";
var shrinkToolsParm = 0;
var shrinkToolsParmTemp = 0;

document.onclick = handleClick;
document.onmouseover = handleOver;
document.onmouseout = handleOut;
document.onmousedown = handleDown;
document.onmouseup = handleUp;

document.write(writeSubPadding(10));  //write the stylesheet for the sub. Getting the indention right

function handleClick() {
	el = getReal(window.event.srcElement, "tagName", "DIV");
        if( shrinkToolsParm == 1 && targetWin != "") el = targetWin;
        if( shrinkToolsParmTemp == 1 && shrinkToolsParm == 0 && targetWin != "" ) 
        {	
        	el = targetWin;
        	shrinkToolsParmTemp = 0;
        	targetWin = "";
        }
        if( el.className == "topFolder" ) targetWin = el;
	if ((el.className == "topFolder") || (el.className == "subFolder")) {
		el.sub = eval(el.id + "Sub");
		if (el.sub.style.display == null) el.sub.style.display = "none";
		if (el.sub.style.display != "block" && shrinkToolsParm == 0) {
			if (el.parentElement.openedSub != null) {
				var opener = eval(el.parentElement.openedSub + ".opener");
				hide(el.parentElement.openedSub);
				if (opener.className == "topFolder")
					outTopItem(opener);
			}
			el.sub.style.display = "block";
			el.sub.parentElement.openedSub = el.sub.id;
			el.sub.opener = el;
		}
		else {
			if (el.sub.openedSub != null) hide(el.sub.openedSub);
			else hide(el.sub.id);
		}
	}
	
	if ((el.className == "subItem") || (el.className == "subFolder")) {
		if (selectedItem != null)
			restoreSubItem(selectedItem);
		highlightSubItem(el);
	}
	
	if ((el.className == "topItem") || (el.className == "topFolder")) {
		if (selectedItem != null)
			restoreSubItem(selectedItem);
	}

	if ((el.className == "topItem") || (el.className == "subItem")) {
		if ((el.href != null) && (el.href != "")) {
			if ((el.target == null) || (el.target == "")) {
				if (window.opener == null) {
					if (document.all.tags("BASE").item(0) != null)
						window.open(el.href, document.all.tags("BASE").item(0).target);
					else 
						window.location = el.href;
				}
				else {
					window.opener.location =  el.href;
				}
			}
			else {
				window.open(el.href, el.target);
			}
		}
	}
	var tmp  = getReal(el, "className", "favMenu");
	if (tmp.className == "favMenu") fixScroll(tmp);
}

function handleOver() {
	var fromEl = getReal(window.event.fromElement, "tagName", "DIV");
	var toEl = getReal(window.event.toElement, "tagName", "DIV");
	if (fromEl == toEl) return;
	
	el = toEl;
	
	if ((el.className == "topFolder") || (el.className == "topItem")) overTopItem(el);
	if ((el.className == "subFolder") || (el.className == "subItem")) overSubItem(el);
	
	if (el.className == "scrollButton") overscrollButton(el);
}

function handleOut() {
	var fromEl = getReal(window.event.fromElement, "tagName", "DIV");
	var toEl = getReal(window.event.toElement, "tagName", "DIV");
	if (fromEl == toEl) return;
	
	el = fromEl;

	if ((el.className == "topFolder") || (el.className == "topItem")) outTopItem(el);
	if ((el.className == "subFolder") || (el.className == "subItem")) outSubItem(el);
	if (el.className == "scrollButton") outscrollButton(el);
}

function handleDown() {
	el = getReal(window.event.srcElement, "tagName", "DIV");
		
	if (el.className == "scrollButton") {
		downscrollButton(el);
		var mark = Math.max(el.id.indexOf("Up"), el.id.indexOf("Down"));
		var type = el.id.substr(mark);
		var menuID = el.id.substring(0,mark);
		eval("scroll" + type + "(" + menuID + ")");
	}
}

function handleUp() {
	el = getReal(window.event.srcElement, "tagName", "DIV");
		
	if (el.className == "scrollButton") {
		upscrollButton(el);
		window.clearTimeout(scrolltimer);
	}
}

function hide(elID) {
	var el = eval(elID);
	el.style.display = "none";
	el.parentElement.openedSub = null;
	if (el.openedSub != null) hide(el.openedSub);
}

function writeSubPadding(depth) {
	var str, str2, val;

	var str = "<style type='text/css'>\n";
	
	for (var i=0; i < depth; i++) {
		str2 = "";
		val  = 0;
		for (var j=0; j < i; j++) {
			str2 += ".sub "
			val += 22;   //下一级菜单距离上一级的距离（横向）
		}
		str += str2 + ".subFolder {padding-left: " + val + "px;}\n";
		str += str2 + ".subItem   {padding-left: " + val + "px;}\n";
	}
	
	str += "</style>\n";
	return str;
}

function overTopItem(el) {
	with (el.style) {
		//background   = "buttonface";
		borderLeft   = "1px ";
		borderRight  = "1px ";
		borderTop    = "1px ";
		borderBottom = "1px ";
		paddingBottom = "2px";
		color         = "blue";   //控制父菜单颜色（移动或选中时）
	}
}

function outTopItem(el) {
	if ((el.sub != null) && (el.parentElement.openedSub == el.sub.id)) { //opened
		with(el.style) {
			borderTop = "1px ";
			borderLeft  = "1px ";
			borderRight    = "1px ";
			borderBottom = "1px ";
			paddingBottom = "2px";	
			color         = "black";		
			//background = "url(/images/tileback.gif) buttonface";
		}
	}
	else {
		with (el.style) {
			border = "0px  ";
			//background = "buttonface";
			padding = "2px";
			color         = "black";	
		}
	}
}
function overSubItem(el) {
	el.style.textDecoration = "underline";
}

function outSubItem(el) {
	el.style.textDecoration = "none";
}

function highlightSubItem(el) {
	//el.style.background = "buttonshadow";    //鼠标点击选中后显示的颜色
	el.style.color      = "blue";              //只控制子集菜单
	selectedItem = el;
	backToOld(targetWin);
}
function backToOld(el) {
	if( targetWin == "" ) return;
	el.style.color      = "black";              //将父菜单变黑
}
function restoreSubItem(el) {
	//el.style.background = "url(/images/tileback.gif) buttonface";
	//鼠标点击其他菜单时上一次点击的那个颜色恢复
	el.style.color      = "menutext";
	selectedItem = null;
}

function overscrollButton(el) {
	overTopItem(el);
	el.style.padding = "0px";
}

function outscrollButton(el) {
	outTopItem(el);
	el.style.padding = "0px";
}

function downscrollButton(el) {
	with (el.style) {
		borderRight   = "1px ";
		borderLeft  = "1px ";
		borderBottom    = "1px ";
		borderTop = "1px ";
	}
}

function upscrollButton(el) {
	overTopItem(el);
	el.style.padding = "0px";
}

function getReal(el, type, value) {
	temp = el;
	while ((temp != null) && (temp.tagName != "BODY")) {
		if (eval("temp." + type) == value) {
			el = temp;
			return el;
		}
		temp = temp.parentElement;
	}
	return el;
}

var scrolltimer;
var scrollAmount = 20;

function scrollDown(el) {
	if (el.offsetHeight > el.parentElement.offsetHeight) {
		var mt = parseInt(el.style.marginTop);
		mt -= scrollAmount;
		if (mt >= el.parentElement.offsetHeight - el.offsetHeight - 2) {
			el.style.marginTop = mt;
			scrolltimer = window.setTimeout("scrollDown(" + el.id + ")",100);
		}
		else {
			el.style.marginTop = el.parentElement.offsetHeight - el.offsetHeight - 2;
		}
	}
	fixScroll(el)
}

function scrollUp(el) {
	var mt = parseInt(el.style.marginTop);
	mt += scrollAmount;
	if (mt >= 0) {
		el.style.marginTop = 0;
	}
	else {
		el.style.marginTop = mt;
		scrolltimer = window.setTimeout("scrollUp(" + el.id + ")",100);
	}
	fixScroll(el);
}

function fixScroll(el) {
	if (el.style.marginTop == "") el.style.margin = "0px";
	mt = parseInt(el.style.marginTop);
	var downButton = eval(el.id + "Down");
	var upButton   = eval(el.id + "Up");

	upButton.style.left = leftPos(el.parentElement.parentElement) + 2;
	upButton.style.top = topPos(el.parentElement.parentElement) + 2;
	upButton.style.width = el.parentElement.offsetWidth - 2;
	downButton.style.left = leftPos(el.parentElement.parentElement) + 2;
	downButton.style.top = topPos(el.parentElement.parentElement) + el.parentElement.offsetHeight - 16;
	downButton.style.width = el.parentElement.offsetWidth - 2;

	upButton.style.display   = (mt < 0) ? "block" : "none";
	downButton.style.display = ((mt == el.parentElement.offsetHeight - el.offsetHeight - 2)
		 || (el.offsetHeight <= el.parentElement.offsetHeight)) ? "none" : "inline";
		 
	if (el.offsetHeight < el.parentElement.offsetHeight) {
		el.style.marginTop = 0;
		upButton.style.display = "none";
	}
}

function topPos(el) {
	return doPosLoop(el, "Top");
}

function leftPos(el) {
	return doPosLoop(el, "Left");
}

function doPosLoop(el, val) {
	var temp = el;
	var x = eval("temp.offset" + val);
	while ((temp.tagName!="BODY") && (temp.offsetParent.style.position != "absolute")) {
		temp = temp.offsetParent;
		x += eval("temp.offset" + val);
	}
	return x;
}
 