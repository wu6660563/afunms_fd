<%@page contentType="text/html; charset=GB2312"%>
<?xml version="1.0" encoding="GB2312"?>
<root>
	<nodes>
		<node>
			<id category="server">137</id>
			<img>image/topo/win_2000.gif</img>
			<x>352px</x>
			<y>108px</y>
			<ip>10.110.19.168</ip>
			<alias>DHC-SERVER</alias>
			<info>&lt;font color='green'&gt;类型:服务器&lt;/font&gt;&lt;br&gt;名称:DHC-SERVER&lt;br&gt;IP地址:10.110.19.168&lt;br&gt;CPU利用率(1):0.0%&lt;br&gt;CPU利用率(2):1.0%&lt;br&gt;内存利用率:63.0%&lt;br&gt;硬盘利用率(C):10.0%&lt;br&gt;硬盘利用率(D):29.0%&lt;br&gt;硬盘利用率(E):18.0%&lt;br&gt;更新时间:2007-01-22 14:43:02</info>
			<menu>&lt;a class="ping_menu_out" onmouseover="pingMenuOver();" onmouseout="pingMenuOut();" onclick="javascript:resetProcDlg();window.showModelessDialog('/afunms/detail/ping.jsp?ip=10.110.19.168', window, 'dialogHeight:250px;dialogWidth:500px;status:0;help:0;edge:sunken;center:yes;scroll:0');timingCloseProDlg(8000);" title="ping 10.110.19.168"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;Ping &lt;/a&gt;&lt;br/&gt;&lt;a class="detail_menu_out" onmouseover="detailMenuOver();" onmouseout="detailMenuOut();" onclick="javascript:window.open('/afunms/detail/dispatcher.jsp?id=137','window', 'toolbar=no,height=620,width=820,scrollbars=yes,center=yes,screenY=0')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;查看信息 &lt;/a&gt;&lt;br/&gt;</menu>
		</node>
		<node>
			<id category="tomcat">220</id>
			<img>image/topo/tomcat.gif</img>
			<x>566px</x>
			<y>106px</y>
			<ip>10.110.19.168</ip>
			<alias>afunms</alias>
			<info>&lt;font color='green'&gt;类型:Tomcat&lt;/font&gt;&lt;br&gt;名称:afunms&lt;br&gt;IP地址:10.110.19.168&lt;br&gt;JVM内存利用率(jvm):88.99%&lt;br&gt;更新时间:2007-01-22 14:39:42</info>
			<menu>&lt;a class="ping_menu_out" onmouseover="pingMenuOver();" onmouseout="pingMenuOut();" onclick="javascript:resetProcDlg();window.showModelessDialog('/afunms/detail/ping.jsp?ip=10.110.19.168', window, 'dialogHeight:250px;dialogWidth:500px;status:0;help:0;edge:sunken;center:yes;scroll:0');timingCloseProDlg(8000);" title="ping 10.110.19.168"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;Ping &lt;/a&gt;&lt;br/&gt;&lt;a class="detail_menu_out" onmouseover="detailMenuOver();" onmouseout="detailMenuOut();" onclick="javascript:window.open('/afunms/detail/dispatcher.jsp?id=220','window', 'toolbar=no,height=620,width=820,scrollbars=yes,center=yes,screenY=0')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;查看信息 &lt;/a&gt;&lt;br/&gt;</menu>
		</node>
		<node>
			<id category="mysql">218</id>
			<img>image/topo/mysql.gif</img>
			<x>171px</x>
			<y>106px</y>
			<ip>10.110.19.168</ip>
			<alias>东华网管DB</alias>
			<info>&lt;font color='green'&gt;类型:MySQL&lt;/font&gt;&lt;br&gt;名称:东华网管DB&lt;br&gt;IP地址:10.110.19.168&lt;br&gt;当前连接数:21.0个&lt;br&gt;更新时间:2007-01-22 14:39:42</info>
			<menu>&lt;a class="ping_menu_out" onmouseover="pingMenuOver();" onmouseout="pingMenuOut();" onclick="javascript:resetProcDlg();window.showModelessDialog('/afunms/detail/ping.jsp?ip=10.110.19.168', window, 'dialogHeight:250px;dialogWidth:500px;status:0;help:0;edge:sunken;center:yes;scroll:0');timingCloseProDlg(8000);" title="ping 10.110.19.168"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;Ping &lt;/a&gt;&lt;br/&gt;&lt;a class="detail_menu_out" onmouseover="detailMenuOver();" onmouseout="detailMenuOut();" onclick="javascript:window.open('/afunms/detail/dispatcher.jsp?id=218','window', 'toolbar=no,height=620,width=820,scrollbars=yes,center=yes,screenY=0')"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;查看信息 &lt;/a&gt;&lt;br/&gt;</menu>
		</node>
	</nodes>
	<lines>
		<line>
			<a>137</a>
			<b>220</b>
			<color>blue</color>
			<dash>Solid</dash>
		</line>
		<line>
			<a>137</a>
			<b>218</b>
			<color>blue</color>
			<dash>Solid</dash>
		</line>
	</lines>
</root>

