<%@page contentType="text/html; charset=GB2312"%>
<?xml version="1.0" encoding="GB2312"?>
<root>
	<nodes>
		<node>
			<id category="net_router">net65</id>
			<img>image/topo/router.png</img>
			<x>297px</x>
			<y>89px</y>
			<ip>10.10.1.1</ip>
			<alias>dhcc</alias>
			<info>&lt;font color='green'&gt;����:·����&lt;/font&gt;&lt;br&gt;�豸��ǩ:dhcc&lt;br&gt;IP��ַ:10.10.1.1&lt;br&gt;CPU������:8.0 %&lt;br&gt;�����Լ��:100 %&lt;br&gt;�������:0 kb/��&lt;br&gt;��������:0 kb/��&lt;br&gt;����ʱ��:2011-03-25 08:15:20&lt;br&gt;</info>
			<menu>&lt;a class="ping_menu_out" onmouseover="pingMenuOver();" onmouseout="pingMenuOut();" onclick="javascript:resetProcDlg();window.showModelessDialog('/afunms/tool/ping.jsp?ipaddress=10.10.1.1', window, 'dialogHeight:500px;dialogWidth:500px;status:0;help:0;edge:sunken;center:yes;scroll:0');timingCloseProDlg(8000);" title="ping 10.10.1.1"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;Ping &lt;/a&gt;&lt;br/&gt;&lt;a class="trace_menu_out" onmouseover="traceMenuOver();" onmouseout="traceMenuOut();" onclick="javascript:window.open('/afunms/network.do?action=telnet&amp;&amp;ipaddress=10.10.1.1','window', 'toolbar=no,height=400,width=500,scrollbars=yes,center=yes,screenY=0')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;Telnet&lt;/a&gt;&lt;br/&gt;&lt;a class="trace_menu_out" onmouseover="traceMenuOver();" onmouseout="traceMenuOut();" onclick="javascript:window.open('/afunms/tool/tracerouter.jsp?ipaddress=10.10.1.1','window', 'toolbar=no,height=400,width=500,scrollbars=yes,center=yes,screenY=0')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;Traceroute&lt;/a&gt;&lt;br/&gt;&lt;a class="detail_menu_out" onmouseover="detailMenuOver();" onmouseout="detailMenuOut();" onclick="showalert('net65')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;�鿴��Ϣ &lt;/a&gt;&lt;br/&gt;&lt;a class="detail_menu_out" onmouseover="detailMenuOver();" onmouseout="detailMenuOut();" onclick="showpanel('10.10.1.1','808','493')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;�豸��� &lt;/a&gt;&lt;br/&gt;&lt;a class="download_menu_out" onmouseover="downloadMenuOver();" onmouseout="downloadMenuOut();" onclick="javascript:window.open('/afunms/topology/network/download.jsp?flag=0','window', 'toolbar=no,height=620,width=820,scrollbars=yes,center=yes,screenY=0')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;���ɱ�������ͼ &lt;/a&gt;&lt;br/&gt;&lt;a class="download_menu_out" onmouseover="downloadMenuOver();" onmouseout="downloadMenuOut();" onclick="javascript:window.open('/afunms/topology/network/download.jsp?flag=1','window', 'toolbar=no,height=620,width=820,scrollbars=yes,center=yes,screenY=0')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;����IP����ͼ &lt;/a&gt;&lt;br/&gt;&lt;a class="relationmap_menu_out" onmouseover="relationMapMenuOver();" onmouseout="relationMapMenuOut();" onclick="javascript:window.showModalDialog('/afunms/submap.do?action=relationList&amp;nodeId=net65&amp;category=net_router', window, 'dialogwidth:500px; dialogheight:300px; status:no; help:no;resizable:0');"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;��������ͼ &lt;/a&gt;&lt;br/&gt;&lt;a class="property_menu_out" onmouseover="propertyMenuOver();" onmouseout="propertyMenuOut();" onclick="javascript:window.showModalDialog('/afunms/submap.do?action=equipProperty&amp;type=net_router&amp;nodeId=net65', window, 'dialogwidth:500px; dialogheight:300px; status:no; help:no;resizable:0');"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;ͼԪ���� &lt;/a&gt;&lt;br/&gt;&lt;a class="deleteEquip_menu_out" onmouseover="deleteEquipMenuOver();" onmouseout="deleteEquipMenuOut();" onclick="addApplication('net65','10.10.1.1')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;�豸���Ӧ��&lt;/a&gt;&lt;br/&gt;&lt;a class="deleteEquip_menu_out" onmouseover="deleteEquipMenuOver();" onmouseout="deleteEquipMenuOut();" onclick="deleteEquip('net65','net_router')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;ϵͳɾ���豸&lt;/a&gt;&lt;br/&gt;&lt;a class="deleteEquip_menu_out" onmouseover="deleteEquipMenuOver();" onmouseout="deleteEquipMenuOut();" onclick="removeEquip('net65')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;����ͼɾ���豸&lt;/a&gt;&lt;br/&gt;</menu>
			<relationMap />
		</node>
	</nodes>
	<lines />
	<assistant_lines />
	<demoLines />
</root>

