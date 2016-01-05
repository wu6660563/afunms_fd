// JavaScript Document
var existingIP = new Array();


/**************** Util Function - Begin ****************/

// �ж��������Ƿ��и�Ԫ��
Array.prototype.contains = function() {
	for (var i = 0; i < this.length; i++) {
		if (arguments[0] == this[i]) {
			return true;
		}
	}
	return false;
}

// ȥ���ַ����еĿո�
String.prototype.trim = function() {													
    return this.replace(/(^\s*)|(\s*$)/g, "");
}

/**************** Util Function - End ****************/

function checkIPAddress(ip) {
	// ���IP��ַ��ʽ�Ƿ�Ϸ�
    if (ip.trim() == "") {
		alert("�������豸IP��ַ��");
	    return false;
	}
	else {
		var tmpIpList = ip.split(".");
		if (tmpIpList.length != 4) {
			alert("������������IP��ַ��");
			return false;
		}
		else {
			if (isNaN(tmpIpList[0]) || isNaN(tmpIpList[1]) || isNaN(tmpIpList[2]) || isNaN(tmpIpList[3])) {
				alert("IP��ַ����Ϊ���֣�");
				return false;
			}
			else if (tmpIpList[0]>255 || tmpIpList[0]<0 || tmpIpList[1]>255 || tmpIpList[1]<0 || tmpIpList[2]>255 || tmpIpList[2]<0 || tmpIpList[3]>255 || tmpIpList[3]<0) {
				alert("�Ƿ���IP��ַ��");
				return false;
			}
		}
	}
	
	// ����Ƿ�Ϊ�Ѵ��ڵ�IP
	for (var i = 0; i < existingIP.length; i++) {
		if (ip == existingIP[i]) {
			alert("IP��ַ�Ѵ��ڣ�\r\n����������IP��ַ��");
			return false;
		}
	}
	
	return true;
}

function checkMask(mask) {
	mask = mask.trim();
	if (mask == "") {
		alert("�������������룡");
		return false;
	}
	else {
		var tmpMaskList = mask.split(".");
		if (tmpMaskList.length != 4) {
			alert("���������ʽ����ȷ��");
			return false;
		}
		else {
			if(isNaN(tmpMaskList[0]) || isNaN(tmpMaskList[1]) || isNaN(tmpMaskList[2]) || isNaN(tmpMaskList[3])) {
				alert("�����������Ϊ���֣�");
				return false;
			}
			else if (tmpMaskList[0]>255 || tmpMaskList[0]<0 || tmpMaskList[1]>255 || tmpMaskList[1]<0 || tmpMaskList[2]>255 || tmpMaskList[2]<0 || tmpMaskList[3]>255 || tmpMaskList[3]<0) {
				alert("�Ƿ����������룡");
				return false;
			}
		}
	}
	return true;
}


function submitAddDeviceForm() {
	// ���IP��ַ
	if (checkIPAddress(addDeviceForm.ipaddress.value)) {
		var ipaddress = addDeviceForm.ipaddress.value;
	}
	else {
		return false;
	}

	var linked = addDeviceForm.linked.value;
	
	var tmpLinked = linked.split("$");
	var msg = "��ȷ������豸��Ϣ��\r\n";
	msg += "* �豸IP��ַ��" + ipaddress + "\t\r\n";
	/*msg += "* �����������豸��\r\n";
	if (tmpLinked[0] == "127.0.0.1") {
		msg += "  - ��\r\n"
	}
	else {
		for (var i = 0; i < tmpLinked.length; i++) {
			msg += "  - " + tmpLinked[i] + "\r\n";
		}
	}*/
	msg += "\r\n�Ƿ�ȷ��������Ϣ��"
	if (!window.confirm(msg)) {
		return false;
	}
	
	var allValue = ipaddress + "#" + linked;
	
	window.returnValue = allValue;
	window.close();
	return false;
}


function submitDelDeviceForm() {
	var deleted = delDeviceForm.deleted.value;
	if (deleted == null || deleted == "127.0.0.1" || deleted == "") {
		return false;
	}
	
	var tmp = deleted.split("$");
	var msg = "��ȷ��ɾ���豸��Ϣ��\r\n";
	msg += "* ��ָ��ɾ�����豸�У�\r\n";
	for (var i = 0; i < tmp.length; i++) {
		msg += "  - " + tmp[i] + "\t\r\n";
	}
	msg += "\r\n�Ƿ�ȷ��������Ϣ��";
	if (!window.confirm(msg)) {
		return false;
	}

	window.returnValue = deleted;
	window.close();
	return false;
}


function submitAddLinkForm() {
	var line = addLinkForm.linkData.value;
	if (line == null || line == "127.0.0.1$127.0.0.1" || line == "") {
		return false;
	}
	var interface = addLinkForm.interfaceData.value;
	//if (interface == null || interface == "null$null" || interface == "") {
	//	return false;
	//}
	
	var tmpLine = line.split("#");
	var tmpIf = interface.split("#");
	if (tmpLine.length != tmpIf.length) {
		window.alert("��ӵ���·��Ϣ���豸��Ϣ�������޷���ӣ�");
		return false;
	}
	var msg = "��ȷ�������·��Ϣ��\r\n";
	msg += "* ��ָ����ӵ���·�У�\r\n";
	for (var i = 0; i < tmpLine.length; i++) {
		var ipTeam = tmpLine[i].split("$");
		var ifTeam = tmpIf[i].split("$");
		msg += "  - " + ipTeam[0] + "(" + ifTeam[0] + ") �� " + ipTeam[1] + "(" + ifTeam[1] + ") ��������·\t\r\n";
	}
	msg += "\r\n�Ƿ�ȷ��������Ϣ��";
	if (!window.confirm(msg)) {
		return false;
	}
	
	window.returnValue = line + "," + interface;
	window.close();
	return false;
}


function submitDelLinkForm() {
	var deleted = delLinkForm.deleted.value;
	if (deleted == null || deleted == "127.0.0.1$127.0.0.1" || deleted == "") {
		return false;
	}
	
	var tmp;
	var line = deleted.split("#");
	var msg = "��ȷ��ɾ����·��Ϣ��\r\n";
	msg += "* ��ָ��ɾ������·�У�\r\n";
	for (var i = 0; i < line.length; i++) {
		tmp = line[i].split("$");
		msg += "  - " + tmp[0] + " �� " + tmp[1] + " ��������·\t\r\n";
	}
	msg += "\r\n�Ƿ�ȷ��������Ϣ��";
	if (!window.confirm(msg)) {
		return false;
	}

	window.returnValue = deleted;
	window.close();
	return false;
}
