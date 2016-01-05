//-----右键菜单的样式---------------------------------------
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
function downloadMenuOut() {
	window.event.srcElement.className="download_menu_out";
}
function downloadMenuOver() {
	window.event.srcElement.className="download_menu_over";
}
function propertyMenuOut() {
	window.event.srcElement.className="property_menu_out";
}
function propertyMenuOver() {
	window.event.srcElement.className="property_menu_over";
}
function deleteMenuOut() {
	window.event.srcElement.className="deleteline_menu_out";
}
function deleteMenuOver() {
	window.event.srcElement.className="deleteline_menu_over";
}
function deleteEquipMenuOut() {
	window.event.srcElement.className="deleteEquip_menu_out";
}
function deleteEquipMenuOver() {
	window.event.srcElement.className="deleteEquip_menu_over";
}
//设备关联应用  HONGLI ADD
function deleteEquipRelatedApplicationsMenuOut() {
	window.event.srcElement.className="equipRelatedApplications_menu_out";
}
function deleteEquipRelatedApplicationsMenuOver() {
	window.event.srcElement.className="equipRelatedApplications_menu_over";
}
function relationMapMenuOut() {
	window.event.srcElement.className="relationmap_menu_out";
}
function relationMapMenuOver() {
	window.event.srcElement.className="relationmap_menu_over";
}
function traceMenuOut() {
	window.event.srcElement.className="trace_menu_out";
}
function traceMenuOver() {
	window.event.srcElement.className="trace_menu_over";
}
//设备面板
function sbmbMenuOut() {
	window.event.srcElement.className="sbmb_menu_out";
}
function sbmbMenuOver() {
	window.event.srcElement.className="sbmb_menu_over";
}
function telnetMenuOut() {
	window.event.srcElement.className="telnet_menu_out";
}

function telnetMenuOver() {
	window.event.srcElement.className="telnet_menu_over";
}
function showalert(id) {
	window.parent.parent.opener.parent.window.document.getElementById('mainFrame').src="/afunms/detail/dispatcher.jsp?id="+id;

}
function deleteLineMenuOut() {
	window.event.srcElement.className="deleteline_menu_out";
}
function deleteLineMenuOver() {
	window.event.srcElement.className="deleteline_menu_over";
}
function editLineMenuOut() {
	window.event.srcElement.className="editline_menu_out";
}
function editLineMenuOver() {
	window.event.srcElement.className="editline_menu_over";
}
//----以上是拓扑图右键菜单的样式，样式的定义在CSS文件中，菜单在XML中的MENU项中设置