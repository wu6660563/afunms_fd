//-----�Ҽ��˵�����ʽ---------------------------------------
function pingMenuOut() {
	window.event.srcElement.className="ping_menu_out";
}
function pingMenuOver() {
	window.event.srcElement.className="ping_menu_over";
}

function detailMenuOut() {
	window.event.srcElement.className="detail_menu_out";
}
function detailMenuOver() {
	window.event.srcElement.className="detail_menu_over";
}

function cloudMenuOut() {
	window.event.srcElement.className="cloud_menu_out";
}
function cloudMenuOver() {
	window.event.srcElement.className="cloud_menu_over";
}
function showalert(id) {
	//window.parent.parent.opener.location="/afunms/detail/dispatcher.jsp?id="+id;
	window.parent.parent.opener.parent.window.document.getElementById('mainFrame').src="/afunms/detail/dispatcher.jsp?id="+id+"&fromtopo=true";
}
//----����������ͼ�Ҽ��˵�����ʽ����ʽ�Ķ�����CSS�ļ��У��˵���XML�е�MENU��������