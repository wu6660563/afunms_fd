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
			<info>&lt;font color='green'&gt;����:·�ɽ�����&lt;/font&gt;&lt;br&gt;�豸��ǩ:HX_7506A&lt;br&gt;IP��ַ:10.10.1.1&lt;br&gt;CPU������:8.0 %&lt;br&gt;�������:0 bit/��&lt;br&gt;��������:0 bit/��&lt;br&gt;������ͨ��:100 %&lt;br&gt;����ʱ��:2009-06-29 08:22:18</info>
			<menu>&lt;a class="ping_menu_out" onmouseover="pingMenuOver();" onmouseout="pingMenuOut();" onclick="javascript:resetProcDlg();window.showModelessDialog('/afunms/detail/ping.jsp?ip=10.10.1.1', window, 'dialogHeight:250px;dialogWidth:500px;status:0;help:0;edge:sunken;center:yes;scroll:0');timingCloseProDlg(8000);" title="ping 10.10.1.1"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;Ping &lt;/a&gt;&lt;br/&gt;&lt;a class="trace_menu_out" onmouseover="traceMenuOver();" onmouseout="traceMenuOut();" onclick="javascript:window.open('/afunms/detail/tracerouter.jsp?ipaddress=10.10.1.1','window', 'toolbar=no,height=400,width=500,scrollbars=yes,center=yes,screenY=0')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;Traceroute&lt;/a&gt;&lt;br/&gt;&lt;a class="detail_menu_out" onmouseover="detailMenuOver();" onmouseout="detailMenuOut();" onclick="javascript:window.open('/afunms/detail/dispatcher.jsp?id=45','window', 'toolbar=no,height=620,width=820,scrollbars=yes,center=yes,screenY=0')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;�鿴��Ϣ &lt;/a&gt;&lt;br/&gt;&lt;a class="download_menu_out" onmouseover="downloadMenuOver();" onmouseout="downloadMenuOut();" onclick="javascript:window.open('/afunms/topology/network/download.jsp?flag=0','window', 'toolbar=no,height=620,width=820,scrollbars=yes,center=yes,screenY=0')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;���ɱ�������ͼ &lt;/a&gt;&lt;br/&gt;&lt;a class="download_menu_out" onmouseover="downloadMenuOver();" onmouseout="downloadMenuOut();" onclick="javascript:window.open('/afunms/topology/network/download.jsp?flag=1','window', 'toolbar=no,height=620,width=820,scrollbars=yes,center=yes,screenY=0')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;����IP����ͼ &lt;/a&gt;&lt;br/&gt;</menu>
		</node>
		<node>
			<id category="switch">53</id>
			<img>image/topo/switch.png</img>
			<x>488px</x>
			<y>127px</y>
			<ip>10.10.1.4</ip>
			<alias>14B_S3652P</alias>
			<info>&lt;font color='green'&gt;����:������&lt;/font&gt;&lt;br&gt;�豸��ǩ:14B_S3652P&lt;br&gt;IP��ַ:10.10.1.4&lt;br&gt;CPU������:38.0 %&lt;br&gt;�������:0 bit/��&lt;br&gt;��������:0 bit/��&lt;br&gt;������ͨ��:100 %&lt;br&gt;����ʱ��:2009-06-29 08:22:18</info>
			<menu>&lt;a class="ping_menu_out" onmouseover="pingMenuOver();" onmouseout="pingMenuOut();" onclick="javascript:resetProcDlg();window.showModelessDialog('/afunms/detail/ping.jsp?ip=10.10.1.4', window, 'dialogHeight:250px;dialogWidth:500px;status:0;help:0;edge:sunken;center:yes;scroll:0');timingCloseProDlg(8000);" title="ping 10.10.1.4"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;Ping &lt;/a&gt;&lt;br/&gt;&lt;a class="trace_menu_out" onmouseover="traceMenuOver();" onmouseout="traceMenuOut();" onclick="javascript:window.open('/afunms/detail/tracerouter.jsp?ipaddress=10.10.1.4','window', 'toolbar=no,height=400,width=500,scrollbars=yes,center=yes,screenY=0')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;Traceroute&lt;/a&gt;&lt;br/&gt;&lt;a class="detail_menu_out" onmouseover="detailMenuOver();" onmouseout="detailMenuOut();" onclick="javascript:window.open('/afunms/detail/dispatcher.jsp?id=53','window', 'toolbar=no,height=620,width=820,scrollbars=yes,center=yes,screenY=0')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;�鿴��Ϣ &lt;/a&gt;&lt;br/&gt;&lt;a class="download_menu_out" onmouseover="downloadMenuOver();" onmouseout="downloadMenuOut();" onclick="javascript:window.open('/afunms/topology/network/download.jsp?flag=0','window', 'toolbar=no,height=620,width=820,scrollbars=yes,center=yes,screenY=0')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;���ɱ�������ͼ &lt;/a&gt;&lt;br/&gt;&lt;a class="download_menu_out" onmouseover="downloadMenuOver();" onmouseout="downloadMenuOut();" onclick="javascript:window.open('/afunms/topology/network/download.jsp?flag=1','window', 'toolbar=no,height=620,width=820,scrollbars=yes,center=yes,screenY=0')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;����IP����ͼ &lt;/a&gt;&lt;br/&gt;</menu>
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

