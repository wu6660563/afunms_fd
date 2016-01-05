<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.report.abstraction.JspReport"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.base.Node"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="java.util.List"%>
<%
  JspReport report = (JspReport)request.getAttribute("report");
  String day = (String)request.getAttribute("day");
  if(day==null) day = SysUtil.getCurrentDate();

  String rc = (String)request.getAttribute("report_category");
  if(rc==null) rc = "";
    
  int nodeId = 0;
  Integer temp = (Integer)request.getAttribute("node_id");
  if(temp!=null) nodeId = temp.intValue();
  
  String rootPath = request.getContextPath();
  
  String radio = (String)request.getAttribute("radio");
  if(radio==null) radio = "1";
  
  List nodeList = PollingEngine.getInstance().getNodeList();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<script language="javascript">
  function selectDate(obj)
  {
     result = window.showModalDialog('<%=rootPath%>/common/calendar.htm',obj.value,'dialogWidth=185px;dialogHeight=210px;status=0;help=0');
     if (result!=null) obj.value = result;
  }

  function toReport()
  {
     mainForm.action = "<%=rootPath%>/report.do?action=report_jsp";
     mainForm.submit();
  }
  
  function loadServer()
  {
     while(mainForm.node_id.options.length!=0)
       mainForm.node_id.options.remove(0);
<%
     for(int i=0;i<nodeList.size();i++)
     {
         Node host = (Node)nodeList.get(i);
         if(host.getCategory()==4)
             out.print("mainForm.node_id.options.add(new Option('" + host.getAlias() + "','" + host.getId() + "'))\n");
     }    
%>       
  }
  
  function loadNetwork()
  {
     while(mainForm.node_id.options.length!=0)
       mainForm.node_id.options.remove(0);
<%
     for(int i=0;i<nodeList.size();i++)
     {
         Node host = (Node)nodeList.get(i);
         if(host.getCategory()<4)
             out.print("mainForm.node_id.options.add(new Option('" + host.getAlias() + "','" + host.getId() + "'))\n");
     }    
%>       
  }
  
  function go_change(tag)
  {
     if(tag==1)
        loadServer();
     else   
        loadNetwork();
  }  
</script>
</HEAD>

<BODY leftmargin="0" topmargin="0" bgcolor="#9FB0C4">
<form method="post" name="mainForm">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td width="16"></td>
		<td bgcolor="#9FB0C4" align="center">
		<br>
		<table width="95%" border=0 cellpadding=0 cellspacing=0 width='700'>
			<tr>
				<td><img src="<%=rootPath%>/resource/image/main_frame_ltcorner.gif" width=12 height=34></td>
				<td background = "<%=rootPath%>/resource/image/main_frame_tbg.gif" align="right">
				<img border="0" src="<%=rootPath%>/resource/image/main_frame_tflag.gif" width="66" height="34"></td>
				<td><img src="<%=rootPath%>/resource/image/main_frame_rtcorner.gif" width=13 height=34></td>
			</tr>			
			<tr>
				<td background="<%=rootPath%>/resource/image/main_frame_lbg.gif" width=12></td>
				<td height=300 bgcolor="#FFFFFF" valign="top"><br>				
<!--=============================table begin=====================-->
<table width="750" border="0" align='center'>
<tr>
    <td height="10">
     <table width="100%" border="0" cellspacing="0" cellpadding="0">
       <tr class="othertr"><td align="center" width="100%">
        日期<input name="day" type="text" class="formStyle" value="<%=day%>" size="10" readonly><img name="selDay1" style="CURSOR:hand" src="<%=rootPath%>/resource/image/cal_btn.gif" onclick="selectDate(mainForm.day)">&nbsp;
        报表类型<select size=1 name='report_category' style='width:130px;'>
           <option value='cpu' <% if(rc.equals("cpu")) out.print("selected"); %>>CPU利用率报表</option>
           <option value='memory' <% if(rc.equals("memory")) out.print("selected"); %>>内存利用率报表</option>
           <option value='disk' <% if(rc.equals("disk")) out.print("selected"); %>>硬盘利用率报表</option>
           <option value='rx_util' <% if(rc.equals("rx_util")) out.print("selected"); %>>接口入口利用率报表</option>
           <option value='tx_util' <% if(rc.equals("tx_util")) out.print("selected"); %>>接口出口利用率报表</option>           
           <option value='rx' <% if(rc.equals("rx")) out.print("selected"); %>>接口入口流量报表</option>
           <option value='tx' <% if(rc.equals("tx")) out.print("selected"); %>>接口出口流量报表</option>
           <option value='error' <% if(rc.equals("error")) out.print("selected"); %>>接口错误率报表</option>
           <option value='discard' <% if(rc.equals("discard")) out.print("selected"); %>>接口丢包率报表</option>
           </select>设备<select size=1 name='node_id' style='width:150px;'>
<%
     for(int i=0;i<nodeList.size();i++)
     {
         Node host = (Node)nodeList.get(i);
         if(radio.equals("1")&&host.getCategory()!=4) continue;
         if(radio.equals("2")&&host.getCategory()>3) continue;
                  
         out.print("<option value='");
         out.print(host.getId());
         if(nodeId==host.getId())
            out.print("' selected>");
         else
            out.print("'>"); 
         out.print(host.getAlias());
         out.print("</option>");
     }     
%>                       
           </select>
      <INPUT type="radio" class=noborder name="radio" value="1" <%if(radio.equals("1")) out.print("checked");%> onclick="go_change(1)">服务器&nbsp;
      <INPUT type="radio" class=noborder name="radio" value="2" <%if(radio.equals("2")) out.print("checked");%> onclick="go_change(2)">网络设备
      <input type="button" name="Submit" value="生成报表" class="button" onclick="toReport()"></td>
      </tr>
      <tr><td height="10"></td></tr>
    </table>
   </td>
  </tr>
</table><br>
<!--**************************************感染病毒机器报表******************************************-->
<%if(report!=null){%>
<table width="750" border="0" cellspacing="0" cellpadding="0" align="center">
<tr><td align="right">
    <a href="<%=rootPath%>/report.do?action=report_pdf&day=<%=day%>&node_id=<%=nodeId%>&report_category=<%=rc%>" target="_blank"><img name="selDay1" alt='导出PDF' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_pdf.gif" width=30 height=32 border="0"></a>
    &nbsp;&nbsp;&nbsp;
    <a href="<%=rootPath%>/report.do?action=report_excel&day=<%=day%>&node_id=<%=nodeId%>&report_category=<%=rc%>" target="_blank"><img name="selDay1" alt='导出EXCEL' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=30 height=32 border="0"></a>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 </td></tr>
<tr><td align="center" height='30' valign='middle'><b><%=report.getHead()%></b></td></tr>
<%
if((report.getChart()!=null)&&(!report.getChart().equals("")))
{
%>
<tr><td width="100%" align="center"><br><img src="<%=rootPath%>/artist?series_key=<%=report.getChart()%>"></td></tr>
<%}%>
<tr><td width="100%" align="center"><%=report.getTable()%></td></tr>
</table>
<%}%>
			</td>
				<td background="<%=rootPath%>/resource/image/main_frame_rbg.gif" width=13></td>
			</tr>
			<tr>
				<td valign="top"><img src="<%=rootPath%>/resource/image/main_frame_lbcorner.gif" width=12 height=15></td>
				<td background="<%=rootPath%>/resource/image/main_frame_bbg.gif" height=15></td>
				<td valign="top"><img src="<%=rootPath%>/resource/image/main_frame_rbcorner.gif" width=13 height=15></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</form>
</BODY>
</HTML>