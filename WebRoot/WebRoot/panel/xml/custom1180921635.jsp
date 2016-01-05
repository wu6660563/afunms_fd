<%@page contentType="text/html; charset=GB2312"%>
<?xml version="1.0" encoding="GB2312"?>
<root>
	<nodes>
		<node>
			<id category="switch_router">2</id>
			<img>image/topo/switch_with_route.png</img>
			<x>660px</x>
			<y>138px</y>
			<ip>192.168.55.61</ip>
			<alias>6509_B</alias>
			<info>&lt;font color='green'&gt;类型:路由交换机&lt;/font&gt;&lt;br&gt;名称:6509_B&lt;br&gt;IP地址:192.168.55.61&lt;br&gt;&lt;font color='red'&gt;Ping不通&lt;/font&gt;</info>
			<menu>&lt;a class="ping_menu_out" onmouseover="pingMenuOver();" onmouseout="pingMenuOut();" onclick="javascript:resetProcDlg();window.showModelessDialog('/afunms/detail/ping.jsp?ip=192.168.55.61', window, 'dialogHeight:250px;dialogWidth:500px;status:0;help:0;edge:sunken;center:yes;scroll:0');timingCloseProDlg(8000);" title="ping 192.168.55.61"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;Ping &lt;/a&gt;&lt;br/&gt;&lt;a class="detail_menu_out" onmouseover="detailMenuOver();" onmouseout="detailMenuOut();" onclick="javascript:window.open('/afunms/detail/dispatcher.jsp?id=2','window', 'toolbar=no,height=620,width=820,scrollbars=yes,center=yes,screenY=0')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;查看信息 &lt;/a&gt;&lt;br/&gt;</menu>
		</node>
		<node>
			<id category="switch_router">1</id>
			<img>image/topo/switch_with_route.png</img>
			<x>671px</x>
			<y>41px</y>
			<ip>192.168.48.252</ip>
			<alias>6509_A</alias>
			<info>&lt;font color='green'&gt;类型:路由交换机&lt;/font&gt;&lt;br&gt;名称:6509_A&lt;br&gt;IP地址:192.168.48.252&lt;br&gt;&lt;font color='red'&gt;Ping不通&lt;/font&gt;</info>
			<menu>&lt;a class="ping_menu_out" onmouseover="pingMenuOver();" onmouseout="pingMenuOut();" onclick="javascript:resetProcDlg();window.showModelessDialog('/afunms/detail/ping.jsp?ip=192.168.48.252', window, 'dialogHeight:250px;dialogWidth:500px;status:0;help:0;edge:sunken;center:yes;scroll:0');timingCloseProDlg(8000);" title="ping 192.168.48.252"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;Ping &lt;/a&gt;&lt;br/&gt;&lt;a class="detail_menu_out" onmouseover="detailMenuOver();" onmouseout="detailMenuOut();" onclick="javascript:window.open('/afunms/detail/dispatcher.jsp?id=1','window', 'toolbar=no,height=620,width=820,scrollbars=yes,center=yes,screenY=0')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;查看信息 &lt;/a&gt;&lt;br/&gt;</menu>
		</node>
		<node>
			<id category="switch">14</id>
			<img>image/topo/switch_alarm3.gif</img>
			<x>786px</x>
			<y>114px</y>
			<ip>192.168.48.221</ip>
			<alias>bangonglou_1</alias>
			<info>&lt;font color='green'&gt;类型:交换机&lt;/font&gt;&lt;br&gt;名称:bangonglou_1&lt;br&gt;IP地址:192.168.48.221&lt;br&gt;&lt;font color='red'&gt;Ping不通&lt;/font&gt;</info>
			<menu>&lt;a class="ping_menu_out" onmouseover="pingMenuOver();" onmouseout="pingMenuOut();" onclick="javascript:resetProcDlg();window.showModelessDialog('/afunms/detail/ping.jsp?ip=192.168.48.221', window, 'dialogHeight:250px;dialogWidth:500px;status:0;help:0;edge:sunken;center:yes;scroll:0');timingCloseProDlg(8000);" title="ping 192.168.48.221"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;Ping &lt;/a&gt;&lt;br/&gt;&lt;a class="detail_menu_out" onmouseover="detailMenuOver();" onmouseout="detailMenuOut();" onclick="javascript:window.open('/afunms/detail/dispatcher.jsp?id=14','window', 'toolbar=no,height=620,width=820,scrollbars=yes,center=yes,screenY=0')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;查看信息 &lt;/a&gt;&lt;br/&gt;</menu>
		</node>
		<node>
			<id category="switch">13</id>
			<img>image/topo/switch_alarm3.gif</img>
			<x>661px</x>
			<y>227px</y>
			<ip>192.168.48.222</ip>
			<alias>bangonglou_2</alias>
			<info>&lt;font color='green'&gt;类型:交换机&lt;/font&gt;&lt;br&gt;名称:bangonglou_2&lt;br&gt;IP地址:192.168.48.222&lt;br&gt;&lt;font color='red'&gt;Ping不通&lt;/font&gt;</info>
			<menu>&lt;a class="ping_menu_out" onmouseover="pingMenuOver();" onmouseout="pingMenuOut();" onclick="javascript:resetProcDlg();window.showModelessDialog('/afunms/detail/ping.jsp?ip=192.168.48.222', window, 'dialogHeight:250px;dialogWidth:500px;status:0;help:0;edge:sunken;center:yes;scroll:0');timingCloseProDlg(8000);" title="ping 192.168.48.222"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;Ping &lt;/a&gt;&lt;br/&gt;&lt;a class="detail_menu_out" onmouseover="detailMenuOver();" onmouseout="detailMenuOut();" onclick="javascript:window.open('/afunms/detail/dispatcher.jsp?id=13','window', 'toolbar=no,height=620,width=820,scrollbars=yes,center=yes,screenY=0')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;查看信息 &lt;/a&gt;&lt;br/&gt;</menu>
		</node>
	</nodes>
	<assistant_lines />
	<lines>
		<line>
			<a>1</a>
			<b>2</b>
			<color>blue</color>
			<dash>Solid</dash>
		</line>
		<line>
			<a>14</a>
			<b>2</b>
			<color>blue</color>
			<dash>Solid</dash>
		</line>
		<line>
			<a>13</a>
			<b>2</b>
			<color>blue</color>
			<dash>Solid</dash>
		</line>
	</lines>
</root>

