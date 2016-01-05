<%@page contentType="text/html; charset=GB2312"%>
<?xml version="1.0" encoding="GB2312"?>
<root>
	<nodes>
		<node>
			<id category="switch_router">45</id>
			<img>image/topo/switch_with_route.png</img>
			<x>348px</x>
			<y>115px</y>
			<ip>10.10.1.1</ip>
			<alias>HX_7506A</alias>
			<info>&lt;font color='green'&gt;类型:路由交换机&lt;/font&gt;&lt;br&gt;设备标签:HX_7506A&lt;br&gt;IP地址:10.10.1.1&lt;br&gt;CPU利用率:8.0 %&lt;br&gt;入口流速:0 bit/秒&lt;br&gt;出口流速:0 bit/秒&lt;br&gt;网络连通性:100 %&lt;br&gt;更新时间:2009-06-29 08:22:18</info>
			<menu>&lt;a class="ping_menu_out" onmouseover="pingMenuOver();" onmouseout="pingMenuOut();" onclick="javascript:resetProcDlg();window.showModelessDialog('/afunms/detail/ping.jsp?ip=10.10.1.1', window, 'dialogHeight:250px;dialogWidth:500px;status:0;help:0;edge:sunken;center:yes;scroll:0');timingCloseProDlg(8000);" title="ping 10.10.1.1"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;Ping &lt;/a&gt;&lt;br/&gt;&lt;a class="trace_menu_out" onmouseover="traceMenuOver();" onmouseout="traceMenuOut();" onclick="javascript:window.open('/afunms/detail/tracerouter.jsp?ipaddress=10.10.1.1','window', 'toolbar=no,height=400,width=500,scrollbars=yes,center=yes,screenY=0')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;Traceroute&lt;/a&gt;&lt;br/&gt;&lt;a class="detail_menu_out" onmouseover="detailMenuOver();" onmouseout="detailMenuOut();" onclick="javascript:window.open('/afunms/detail/dispatcher.jsp?id=45','window', 'toolbar=no,height=620,width=820,scrollbars=yes,center=yes,screenY=0')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;查看信息 &lt;/a&gt;&lt;br/&gt;&lt;a class="download_menu_out" onmouseover="downloadMenuOver();" onmouseout="downloadMenuOut();" onclick="javascript:window.open('/afunms/topology/network/download.jsp?flag=0','window', 'toolbar=no,height=620,width=820,scrollbars=yes,center=yes,screenY=0')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;生成别名拓扑图 &lt;/a&gt;&lt;br/&gt;&lt;a class="download_menu_out" onmouseover="downloadMenuOver();" onmouseout="downloadMenuOut();" onclick="javascript:window.open('/afunms/topology/network/download.jsp?flag=1','window', 'toolbar=no,height=620,width=820,scrollbars=yes,center=yes,screenY=0')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;生成IP拓扑图 &lt;/a&gt;&lt;br/&gt;</menu>
		</node>
		<node>
			<id category="switch">53</id>
			<img>image/topo/switch.png</img>
			<x>488px</x>
			<y>127px</y>
			<ip>10.10.1.4</ip>
			<alias>14B_S3652P</alias>
			<info>&lt;font color='green'&gt;类型:交换机&lt;/font&gt;&lt;br&gt;设备标签:14B_S3652P&lt;br&gt;IP地址:10.10.1.4&lt;br&gt;CPU利用率:38.0 %&lt;br&gt;入口流速:0 bit/秒&lt;br&gt;出口流速:0 bit/秒&lt;br&gt;网络连通性:100 %&lt;br&gt;更新时间:2009-06-29 08:22:18</info>
			<menu>&lt;a class="ping_menu_out" onmouseover="pingMenuOver();" onmouseout="pingMenuOut();" onclick="javascript:resetProcDlg();window.showModelessDialog('/afunms/detail/ping.jsp?ip=10.10.1.4', window, 'dialogHeight:250px;dialogWidth:500px;status:0;help:0;edge:sunken;center:yes;scroll:0');timingCloseProDlg(8000);" title="ping 10.10.1.4"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;Ping &lt;/a&gt;&lt;br/&gt;&lt;a class="trace_menu_out" onmouseover="traceMenuOver();" onmouseout="traceMenuOut();" onclick="javascript:window.open('/afunms/detail/tracerouter.jsp?ipaddress=10.10.1.4','window', 'toolbar=no,height=400,width=500,scrollbars=yes,center=yes,screenY=0')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;Traceroute&lt;/a&gt;&lt;br/&gt;&lt;a class="detail_menu_out" onmouseover="detailMenuOver();" onmouseout="detailMenuOut();" onclick="javascript:window.open('/afunms/detail/dispatcher.jsp?id=53','window', 'toolbar=no,height=620,width=820,scrollbars=yes,center=yes,screenY=0')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;查看信息 &lt;/a&gt;&lt;br/&gt;&lt;a class="download_menu_out" onmouseover="downloadMenuOver();" onmouseout="downloadMenuOut();" onclick="javascript:window.open('/afunms/topology/network/download.jsp?flag=0','window', 'toolbar=no,height=620,width=820,scrollbars=yes,center=yes,screenY=0')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;生成别名拓扑图 &lt;/a&gt;&lt;br/&gt;&lt;a class="download_menu_out" onmouseover="downloadMenuOver();" onmouseout="downloadMenuOut();" onclick="javascript:window.open('/afunms/topology/network/download.jsp?flag=1','window', 'toolbar=no,height=620,width=820,scrollbars=yes,center=yes,screenY=0')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;生成IP拓扑图 &lt;/a&gt;&lt;br/&gt;</menu>
		</node>
	</nodes>
	<assistant_lines />
	<lines>
		<line>
			<a>45</a>
			<b>53</b>
			<color>blue</color>
			<dash>Solid</dash>
		</line>
	</lines>
</root>

