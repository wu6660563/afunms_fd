var rowNum = 0;
var action = "";

/*
 * ��������
 * �£�month
 * �ܣ�week
 * */
var dateType = [["month" , "��"] , ["week" , "��"]];

/**
 * һ�ܵ���
 */
var weekday = [ ["1" , "����"] ,
				["2" , "��һ"] , 
				["3" , "�ܶ�"] , 
				["4" , "����"] ,
				["5" , "����"] , 
				["6" , "����"] ,
				["7" , "����"] 
				];
				
var monthday = new Array();
for(var i = 1; i <= 31; i++){
	monthday[i-1] = ["" + i , "" + i];
}

function setMobile(name) {

	currentName = name;

	var currentName = document.getElementById(name);

	var url = action + "&event=" + currentName.id + "&value="
			+ currentName.value + "";
	msgWindow = window
			.open(
					url,
					"protypeWindow",
					"toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no");
}

/**
 * ����һ��
 */
function addRow(alarmConfigDivId, category) {
	var tr = document.getElementById(alarmConfigDivId).insertRow(0);
	rowNum = rowNum + 1;
	var str = "" + rowNum;
	while (str.length < 2) {
		str = "0" + str;
	}
	var hours = "";
	for (var i = 0; i < 24; i++) {
		hours = hours + '<OPTION value="' + i + '">' + i + '</OPTION>';
	}
	
	var types = "";
	for (var i = 0 ; i < dateType.length; i++){
		types = types + '<OPTION value="' + dateType[i][0] + '">' + dateType[i][1] + '</OPTION>';
	}
	
	var td = tr.insertCell(tr.cells.length);

	var elemTable = document.createElement("table");
	var elemTBody = document.createElement("tBody");
	elemTable.style.marginTop = "10px";
	elemTable.id = "table" + str;
	elemTable.width = "100%";
	elemTable.align = "left";
	elemTable.cellPadding = "1";
	elemTable.cellSpacing = "1";
	elemTable.bgColor = "black";
	elemTBody.bgColor = "white";
	elemTable.appendChild(elemTBody);
	td.appendChild(elemTable);

	var tr0 = document.createElement("tr");
	elemTBody.appendChild(tr0);
	var td00 = tr0.insertCell(tr0.cells.length);
	td00.className = "lab";
	td00.innerHTML = '<input id="id-' + str
			+ '" name="id-' + str + '" type="hidden"  />'
			+ '<input id="category-' + str + '" name="category-' + str
			+ '" type="hidden"  /><span class="must">*</span>���ڷ�ʽ';
	var td01 = tr0.insertCell(tr0.cells.length);
	td01.align = "left";
	td01.innerHTML = '<select id="dateType-' + str + '" name="dateType-' + str
			+ '" style="width:40px" onchange="changeDateSelect(' + str+ ',this.value)">' + types + '</select>';
	var td13 = tr0.insertCell(tr0.cells.length);
	td13.className = "lab";
	td13.innerHTML = '<span class="must">*</span>�������ʹ���';
	var td14 = tr0.insertCell(tr0.cells.length);
	td14.align = "left";
	td14.innerHTML = '<input class="input-text" id="sendTimes-'
			+ str
			+ '" name="sendTimes-'
			+ str
			+ '" type="text"  size="2"/>';
	var td15 = tr0.insertCell(tr0.cells.length);
	td15.className = "lab";
	td15.innerHTML = '<span class="must">*</span>���ʱ��(��)';
	var td16 = tr0.insertCell(tr0.cells.length);
	td16.align = "left";
	td16.innerHTML = '<input class="input-text" id="sendIntervalTimes-'
			+ str
			+ '" name="sendIntervalTimes-'
			+ str
			+ '" type="text"  size="2"/>';
	var td02 = tr0.insertCell(tr0.cells.length);
	td02.innerHTML = '<span class="must">*</span>��ʼ����';
	var td03 = tr0.insertCell(tr0.cells.length);
	td03.align = "left";
	td03.innerHTML = '<select id="startDate-' + str + '" name="startDate-' + str
			+ '" style="width:40px"></select>';
	var td04 = tr0.insertCell(tr0.cells.length);
	td04.innerHTML = '<span class="must">*</span>��������';
	var td05 = tr0.insertCell(tr0.cells.length);
	td05.align = "left";
	td05.innerHTML = '<select id="endDate-' + str + '" name="endDate-' + str
			+ '" style="width:40px"></select>';
	var td06 = tr0.insertCell(tr0.cells.length);
	td06.innerHTML = '<span class="must">*</span>��ʼʱ��';
	var td07 = tr0.insertCell(tr0.cells.length);
	td07.align = "left";
	td07.innerHTML = '<select id="startTime-' + str + '" name="startTime-' + str
			+ '" style="width:40px">' + hours + '</select>';
	var td08 = tr0.insertCell(tr0.cells.length);
	td08.innerHTML = '<span class="must">*</span>����ʱ��';
	var td09 = tr0.insertCell(tr0.cells.length);
	td09.align = "left";
	td09.innerHTML = '<select id="endTime-' + str + '" name="endTime-' + str
			+ '" style="width:40px">' + hours + '</select>';
	var td10 = tr0.insertCell(tr0.cells.length);
	td10.className = "lab";
	td10.innerHTML = '<span class="must">*</span>���ý�����';
	var td11 = tr0.insertCell(tr0.cells.length);
	td11.align = "left";
	td11.innerHTML = '<input class="input-text" readonly="readonly" id="userIds-'
			+ str
			+ '" name="userIds-'
			+ str
			+ '" type="text"  size="15"  onclick="setReceiver(this.id)" />';
	var td12 = tr0.insertCell(tr0.cells.length);
	td12.className = "lab";
	td12.innerHTML = '<a href="javascript:delRow(' + alarmConfigDivId + ','
			+ rowNum + ')">ɾ��</a>';
	changeDateSelect(str,document.getElementById("dateType-" + str).value);
	document.getElementById("rowNum").value = rowNum;
	document.getElementById("category-" + str).value = category;
}

function changeDateSelect(num , value){
	var daysArray = "";
	var str = num + "";
	while (str.length < 2) {
		str = "0" + str;
	}
	
	if ("month" == value){
		daysArray = monthday;
	}else {
		daysArray = weekday;
	}
	
	var startDateSelect = document.getElementById("startDate-" + str);
	startDateSelect.length = 0;
	for (var i = 0 ; i < daysArray.length; i++){
		startDateSelect.options[i] = new Option(daysArray[i][1] , daysArray[i][0]);
	}
	var endDateSelect = document.getElementById("endDate-" + str);
	endDateSelect.length = 0;
	for (var i = 0 ; i < daysArray.length; i++){
		endDateSelect.options[i] = new Option(daysArray[i][1] , daysArray[i][0]);
	}
}

function delRow(alarmConfigDivId, rowNo) {
	
	var str = "" + rowNo;
	while (str.length < 2) {
		str = "0" + str;
	}
	var i = 0;
	
	while (alarmConfigDivId.rows[i].firstChild.firstChild.id != "table"
			+ str) {
		i++;
	}
	
	alarmConfigDivId.deleteRow(i);
}

function initAlarmWayByArray(alarmConfigDivId, store) {
	for (var i = 0; i < store.length; i++) {
		var item = store[i];
		initAlarmWay(alarmConfigDivId , item);
	}
}

function initAlarmWay(alarmConfigDivId , item){
	addRow(alarmConfigDivId, item.category);
	var str = "" + rowNum;
	if (str.length < 2) {
		str = "0" + str;
	}
	document.getElementById("id-" + str).value = item.id;
	document.getElementById("dateType-" + str).value = item.dateType;
	changeDateSelect(str,document.getElementById("dateType-" + str).value);
	document.getElementById("sendTimes-" + str).value = item.sendTimes;
	document.getElementById("sendIntervalTimes-" + str).value = item.sendIntervalTimes;
	document.getElementById("startDate-" + str).value = item.startDate;
	document.getElementById("endDate-" + str).value = item.endDate;
	document.getElementById("startTime-" + str).value = item.startTime;
	document.getElementById("endTime-" + str).value = item.endTime;
	document.getElementById("userIds-" + str).value = item.userIds;
	
}
