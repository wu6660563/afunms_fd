<!-- hostrepoetday.jsp -->
<%@ page contentType="text/html;charset=gb2312" %>
<%@ page language="java" import="java.util.*" %>
<%@ include file="../include/globe.inc"%>

<jsp:useBean id="itm" scope="page" class="cn.com.dhcc.poweritsm.itm.ITMResource"/>
<jsp:useBean id="equipiddic" scope="page" class="cn.com.dhcc.poweritsm.util.EquipIdDic"/> 
<jsp:useBean id="dateinfo" scope="page" class="cn.com.dhcc.poweritsm.common.DateInformation"/>
<%String timeFormat = "yyyy-MM-dd";String action="init";
java.text.SimpleDateFormat timeFormatter = new java.text.SimpleDateFormat(timeFormat);
String sDate="";
sDate=request.getParameter("currentdate");
if(sDate==null){sDate = timeFormatter.format(new java.util.Date());}
String hosttype="";												//flash传过来的参数 确定主机类型
hosttype=request.getParameter("hosttype");
String fromtype="";												//flash传过来的参数
fromtype=request.getParameter("fromtype");
if(fromtype==null){fromtype="link";}      //fromtype判断是link打开还是flash打开
String mibname="";												
mibname=request.getParameter("mibname");  //mibname flash传过来的参数 确定是cpu还是内存监控
if(mibname==null){mibname="";}
String getiid="";
String getmid="";
String geteid="";
String getunit="";

if(fromtype.equals("host")){
	equipiddic.setXmlid(request.getParameter("xmlid"));		//flash传过来的参数
	equipiddic.setType("0");
	String currentip=((Hashtable)equipiddic.selectOne().elementAt(0)).get("alias").toString();
	
	try{  
	Hashtable hash_model =new Hashtable();
		Hashtable hash_resource =new Hashtable();
		Hashtable hash_Instance =new Hashtable();
		Hashtable hash_Metrics =new Hashtable();
		
	geteid=itm.selectEidPointByAlias(currentip);
	//System.out.println("eid  "+geteid);
	if(geteid!=null){
			if(mibname.equals("cpuuse")){                   //如果为检测cpu利用率  
			
				String cpu_model=""; 		//cpu 资源模型
				String cpu_resouce=""; 	//cpu 相关资源
				String cpu_instance="";	//cpu 实例名称
				String cpu_metrics="";  //cpu 取得测量值名称		
						
 				if("Windows".equals(hosttype)){               //根据条件判断主机类型和应当取的cpu参数                
 					cpu_model="TMW_Processor"; 		
 					cpu_resouce="CPU Usage"; 	
 					cpu_instance="Processor=0;";	
 					cpu_metrics="PercentProcessorTime"; 	
					}
				if("UNIX_Linux".equals(hosttype)){
				   	cpu_model="DMXCpu"; 		
 						cpu_resouce="Percent usage"; 	
 						cpu_instance="name=total;";	
 						cpu_metrics="prcSysTime"; 
				 	}
				 					
		    		hash_model=itm.selectModelByEidModelName(geteid,cpu_model);
		    		if(!hash_model.isEmpty()){
		    			String id=hash_model.get("id").toString();
		    			hash_resource=itm.selectResouceByIDEID(geteid,id,cpu_resouce);
		    			if(!hash_resource.isEmpty()){
			    			String rcid=hash_resource.get("rcid").toString();
			    			hash_Instance=itm.selectInstanceByRCIDEID(rcid,geteid,cpu_instance);
			    			hash_Metrics=itm.selectMetricsByRCIDEID(rcid,geteid,cpu_metrics,"AVG");
		    				if(!hash_Instance.isEmpty()){
		    					getiid=hash_Instance.get("iid").toString();
		    				}
		    				if(!hash_Metrics.isEmpty()){
		    					getmid=hash_Metrics.get("mid").toString();
		    				}
		    				getunit="%";
		    				
		    			}
		    		}
	    		}
	    		if(mibname.equals("memouse")){
	    	
	    			
						String memory_model=""; 		//内存 资源模型
						String memory_resouce=""; 	//内存 相关资源
						String memory_instance="";	//内存 实例名称
						String memory_metrics="";   //内存 取得测量值名称		
	    			
 				if("Windows".equals(hosttype)){               //根据条件判断主机类型和应当取的cpu参数                
 					memory_model="TMW_MemoryModel"; 	
 					memory_resouce="Memory Usage"; 
 					memory_instance="Memory=Memory;";
 					memory_metrics="TotalAvail"; 	
					}
				if("UNIX_Linux".equals(hosttype)){
					memory_model="DMXMemory"; 	
 					memory_resouce="Memory Availability"; 
 					memory_instance="name=total;";
 					memory_metrics="PrcAvailStorage"; 
				 	}	    			
	    		hash_model=itm.selectModelByEidModelName(geteid,memory_model);
		    		if(!hash_model.isEmpty()){
		    			String id=hash_model.get("id").toString();
		    			hash_resource=itm.selectResouceByIDEID(geteid,id,memory_resouce);
		    			if(!hash_resource.isEmpty()){
			    			String rcid=hash_resource.get("rcid").toString();
			    			hash_Instance=itm.selectInstanceByRCIDEID(rcid,geteid,memory_instance);
			    			hash_Metrics=itm.selectMetricsByRCIDEID(rcid,geteid,memory_metrics,"AVG");
		    				if(!hash_Instance.isEmpty()){
		    					getiid=hash_Instance.get("iid").toString();
		    					//System.out.println("iid is"+getiid);
		    				}
		    				if(!hash_Metrics.isEmpty()){
		    					getmid=hash_Metrics.get("mid").toString();
		    					//System.out.println("memomid is"+getmid);
		    				}
		    				
		    				getunit="字节/秒";
		    			}
		    		}
	    		}
	    		if(mibname.equals("netinuse")){

						String netin_model=""; 		//输入 资源模型
						String netin_resouce=""; 	//输入 相关资源
						String netin_instance="";	//输入 实例名称
						String netin_metrics="";  //输入 取得测量值名称	 
						   			 				
	    		if("Windows".equals(hosttype)){               //根据条件判断主机类型和应当取的cpu参数                
					 netin_model="TMW_NetworkIntCard"; 		
					 netin_resouce="Output Queue Length"; 	
					 netin_instance="NetworkInterfaceCard=Compaq Ethernet_Fast Ethernet Adapter_Module;";	
					 netin_metrics="OutputQueueLength"; 
					}
				if("UNIX_Linux".equals(hosttype)){
					 netin_model="DMXNetworkInterface"; 		
					 netin_resouce="Interface Card"; 	
					 netin_instance="InterfaceName=en0;";	
					 netin_metrics="InPacks";  
				 	}	
		    		hash_model=itm.selectModelByEidModelName(geteid,netin_model);
		    		if(!hash_model.isEmpty()){
		    			String id=hash_model.get("id").toString();
		    			hash_resource=itm.selectResouceByIDEID(geteid,id,netin_resouce);
		    			if(!hash_resource.isEmpty()){
			    			String rcid=hash_resource.get("rcid").toString();
			    			hash_Instance=itm.selectInstanceByRCIDEID(rcid,geteid,netin_instance);
			    			hash_Metrics=itm.selectMetricsByRCIDEID(rcid,geteid,netin_metrics,"AVG");
		    				if(!hash_Instance.isEmpty()){
		    					getiid=hash_Instance.get("iid").toString();
		    				}
		    				if(!hash_Metrics.isEmpty()){
		    					getmid=hash_Metrics.get("mid").toString();
		    				}
		    				getunit="字节/秒";
		    				
		    			}
		    		}
	    		}
	    		//UNIX
	    		if(mibname.equals("netoutuse")){
		    		hash_model=itm.selectModelByEidModelName(geteid,"DMXNetworkInterface");
		    		if(!hash_model.isEmpty()){
		    			String id=hash_model.get("id").toString();
		    			hash_resource=itm.selectResouceByIDEID(geteid,id,"Interface Card");
		    			if(!hash_resource.isEmpty()){
			    			String rcid=hash_resource.get("rcid").toString();
			    			hash_Instance=itm.selectInstanceByRCIDEID(rcid,geteid,"InterfaceName=en0;");
			    			hash_Metrics=itm.selectMetricsByRCIDEID(rcid,geteid,"OutPacks","AVG");
		    				if(!hash_Instance.isEmpty()){
		    					getiid=hash_Instance.get("iid").toString();
		    				}
		    				if(!hash_Metrics.isEmpty()){
		    					getmid=hash_Metrics.get("mid").toString();
		    				}
		    				getunit="字节/秒";
		    				
		    			}
		    		}
	    		}
	    	}
	 action="show";
	}
	catch(Exception e){System.out.println(e);}
	
}


String eid="";
eid=request.getParameter("eid");
if(eid==null){eid="-1";}
String iid="";
iid=request.getParameter("iid");
if(iid==null){iid="-1";}
String mid="";
mid=request.getParameter("mid");
if(mid==null){mid="-1";}

//out.println("eid is "+ eid +"   iid is  "+iid+"    mid is  "+mid);
String currentname="";
currentname=request.getParameter("currentname");
if(currentname==null){currentname="";}
String parame="";
parame=request.getParameter("parame");
if(parame==null){parame="";}
String unit="";
unit=request.getParameter("unit");
if(unit==null){unit="";}
if(action.equals("show")){
eid=geteid;
iid=getiid;
mid=getmid;
unit=getunit;
}
String currentname1=currentname+"  "+parame+"日报表";
%>
<html>
<head>
<title><%=alltitle%></title>
<link href="<%=csshref%>" rel="stylesheet" type="text/css">
<script language="javascript">
var tabImageBase = "../img/report/tabs";
</script> <script language="javascript" src="../js/report/dhtml.js"></script> 
<script language="javascript" src="../js/report/graph.js"></script> 
<script language="JavaScript" src="date.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
</head>

<body>
<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="calendar.htm" style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
<OBJECT classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 height=0 id=WB width=0>
  </OBJECT>
  <table width="100%" border="0">
  <tr>
    <td align="center">
      <br>
           </td>
  </tr>
  </table>
  <table width="100%" border="0">
  <tr> 
    <td align="right"><br> 
      <INPUT type="button" value="打印设置" id=button1 name=button1 onclick="setPrint();" class="noprint"> 
      <INPUT type="button" value="预览" id=button2 name=button2 onclick="previewPrint();" class="noprint"> 
      <INPUT type="button" value="打印" id=button4 name=button4 onclick="print1();" class="noprint">
      <INPUT type="button" value="保存网页" id=button3 name=button3 onclick="save();" class="noprint"> 
      <INPUT type="button" value="刷新" id=button5 name=button5 onclick="reload();" class="noprint"> 
      <INPUT type="button" value="关闭窗口" id=button6 name=button6 onclick="window.close();" class="noprint">  
    </td>
  </tr>
 <tr> 
    <td align="right"><br> <form action="hostreportday.jsp" method="post" name="subform"  class="noprint">
	<input type= "hidden" name=eid value=<%=eid%>>
	<input type= "hidden" name=iid value=<%=iid%>>
	<input type= "hidden" name=mid value=<%=mid%>>
	 <input type= "hidden" name=currentname value=<%=currentname%>>
	<input type= "hidden" name=parame value=<%=parame%>>
	<input type= "hidden" name=unit value=<%=unit%>>
        <input name="currentdate" type="text"  class=input2 value="<%=sDate%>" size="10" maxlength="10"> 
        <a onClick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.subform.imageCalendar1,document.subform.currentdate,null,0,330)"> 
        <img id=imageCalendar1 align=absmiddle width=34 height=21 src=calendar/button.gif border=0></a>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input name="submit" type="submit" value="生成报表">
      </form>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    </td>
  </tr>
  </table>
<%
try{

       double[] dou_allmuti =new double[24];
       dou_allmuti=itm.selectMetricsData(iid,mid,eid,sDate);
%>
<table width="100%" border="0">
  <br>
  <tr> 
    <td align="center" valign="middle"><%=currentname1%> </td>
  </tr>
  <tr> 
    <td align="center">
      <%if(dou_allmuti.length>0){%>
      <table border="0" cellspacing="0" cellpadding="0">
        <tr>  <td align=left>单位(<%=unit%>)</td>
          <td align="right"><a href="#" onClick="javascript: executeStore(document.applets.topinternalprotocols0, 3); " onMouseOver='status="Change graph type to Line"; return true;' onMouseOut='status=""; return true;' onFocus='status="Change graph type to Line"; return false;' onBlur='status=""; return false;'><img border="0" name="line0" src="../img/report/tabs/line_unselected.gif" width="36" height="16" align="absbottom" vspace="0"  alt="Line"  title="线形图"/></a> 
            <a href="#" onClick="javascript: executeStore(document.applets.topinternalprotocols0, 1); " onMouseOver='status="Change graph type to Bar"; return true;' onMouseOut='status=""; return true;' onFocus='status="Change graph type to Bar"; return false;' onBlur='status=""; return false;'><img border="0" name="bar0" src="../img/report/tabs/bar_unselected.gif" width="36" height="16" align="absbottom" vspace="0"  alt="Bar"  title="柱图"/></a> 
            <a href="#" onClick="javascript: executeStore(document.applets.topinternalprotocols0, 2); " onMouseOver='status="Change graph type to Stacked"; return true;' onMouseOut='status=""; return true;' onFocus='status="Change graph type to Stacked"; return false;' onBlur='status=""; return false;'><img border="0" name="stacked0" src="../img/report/tabs/stacked_unselected.gif" width="36" height="16" align="absbottom" vspace="0"  alt="Stacked"  title="堆叠图"/></a> 
            <a href="#" onClick="javascript: executeStore(document.applets.topinternalprotocols0, 4); " onMouseOver='status="Change graph type to Area"; return true;' onMouseOut='status=""; return true;' onFocus='status="Change graph type to Area"; return false;' onBlur='status=""; return false;'><img border="0" name="area0" src="../img/report/tabs/area_unselected.gif" width="36" height="16" align="absbottom" vspace="0"  alt="Area"  title="面积条线图"/></a> 
          </td>
        </tr>
        <tr> 
          <td align=right colspan=2><applet name="topinternalprotocols0" codebase="../popchart" code="PopChart.class" archive="PopChartA.jar" width="575" height="230" mayscript>
              <param name="bgcolor" value="ffffff">
              <param name="file" value="multiline">
              <param name="pcscript" value="main.beginStore(0)graph.categories(

<%for(int n=0;n<24;n++){
			
        		out.print("0");
        		
     				out.print(",");
     				}
     				
     				%>

)graph.series(

<%
String timeFormat1 = "HH:mm";
java.text.SimpleDateFormat timeFormatter1 = new java.text.SimpleDateFormat(timeFormat1);
for(int n=0;n<24;n++){
out.print(timeFormatter1.format(timeFormatter1.parseObject(n+":00"))); 
out.print(",");
	out.print(dou_allmuti[n]);
out.print(";");
   }			%>

)title.setText(<%=currentname1%>)title.show()main.endStore()main.execute(0)main.beginStore(1)main.newapfile(multibar)main.execute(0)main.endStore()
main.beginStore(2)main.newapfile(multistacked)main.execute(0)main.endStore()main.beginStore(3)main.newapfile(multiline)main.execute(0)main.endStore()main.beginStore(4)main.newapfile(multiarea)main.execute(0)main.endStore()">
              <param name="initializingGraphMessage" value="初始化图像中......">
              <param name="corruptAppearanceFileError" value="ERROR: corrupt appearance file">
              <param name="appearanceFileLoadError" value="ERROR: could not load appearance file">
              <param name="dataFileLoadError" value="ERROR: could not load data file">
              <param name="securityExceptionPostfix" value="- Security Exception">
              <param name="badPCScriptPrefix" value="ERROR: bad pcscript - ">
            </applet>
            </td>
        </tr>
        
          
      </table>
      <%}%>
    </td>
  </tr>

  
</table>


 <%	
        }catch(Exception e){out.println(e.getMessage());}
     %>　 　 
</body>
</html>

