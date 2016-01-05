function addWordConfigRow() {
	$("#addWordConfigRow").click(function () {
		var num = $("#wordConfigTable tr").length-1;
		var t = $("<tr>").css({"border":"1"});
		t.append($("<td>").html("*¹Ø¼ü×Ö").css({"border":"1 solid #000000","width":"50px"}));
		t.append($("<td>").html('<input class="input-text" id="words'
			+ num
			+ '" name="words'
			+ num
			+ '" type="text"  size="50"/>').css({"border":"1 solid #000000","width":"60px"}));
		var del = $("<span>").html("<a href='javascript:del();'>É¾³ý</a>").click(function(){$(this).parent().parent().remove();});
		t.append($("<td>").append(del).css({"border":"1 solid #000000","width":"30px"}).attr("align","center"));
		t.insertBefore("#wordConfigTable tr:last");
		document.getElementById("wordNum").value = num;
	});
}
function delRow(timeShareConfigDivId, rowNo) {
	var str = "" + rowNo;
	while (str.length < 2) {
		str = "0" + str;
	}
	var i = 0;
	while (timeShareConfigDivId.rows[i].firstChild.firstChild.id != "table" + str) {
		i++;
	}
	timeShareConfigDivId.deleteRow(i);
}

function wordShareConfig(store) {
	for (var i = 0; i < store.length; i++) {
		var item = store[i];
		
		var t = $("<tr>").css({"border":"1"});
		t.append($("<td>").html("*¹Ø¼ü×Ö").css({"border":"1 solid #000000","width":"50px"}));
		t.append($("<td>").html('<input class="input-text" id="words'
			+ i
			+ '" name="words'
			+ i
			+ '" type="text"  size="50"/>').css({"border":"1 solid #000000","width":"60px"}));
		var del = $("<span>").html("<a href='javascript:del();'>É¾³ý</a>").click(function(){$(this).parent().parent().remove();});
		t.append($("<td>").append(del).css({"border":"1 solid #000000","width":"30px"}).attr("align","center"));
		t.insertBefore("#wordConfigTable tr:last");
		document.getElementById("wordNum").value = i;
		
		var str = "" + i;
		document.getElementById("words" + str).value = item.word;
	}
}
function del(){
	var num = $("#wordConfigTable tr").length-1;
	num--;
	alert(num);
	document.getElementById("wordNum").value = num;
}
