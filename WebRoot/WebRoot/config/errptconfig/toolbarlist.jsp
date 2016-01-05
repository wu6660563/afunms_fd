<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.indicators.model.NodeGatherIndicators"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.config.model.Errptconfig"%>
<%@page import="com.afunms.alarm.model.*"%>
<%@page import="com.afunms.alarm.dao.*"%>

<%
	String rootPath = request.getContextPath();
  	Errptconfig errptconfig = (Errptconfig)request.getAttribute("errptconfig");
  	if(errptconfig == null)errptconfig = new Errptconfig();
		String tstr0="";
		String tstr1="";
		String tstr2="";
		String tstr3="";
		String tstr4="";
		String tstr5="";
  		String nodeid = (String)request.getAttribute("nodeid");
		String errpttpye = errptconfig.getErrpttype();
		String alarmwayid = errptconfig.getAlarmwayid();
		String alarmwayname="";
		if(alarmwayid != null && alarmwayid.trim().length()>0){
			AlarmWayDao alarmWayDao = new AlarmWayDao();
			try {
				AlarmWay alarmWay0 = (AlarmWay)alarmWayDao.findByID(alarmwayid);
				if(alarmWay0!=null){
					alarmwayname = alarmWay0.getName();
					//alarmWayHashtable.put("way0", alarmWay0);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				alarmWayDao.close();
			}				
		}
		
		
		if(errpttpye == null)errpttpye = "";
		String[] errpts = errpttpye.split(",");
		Hashtable errpthash = new Hashtable();
		try{
		if(errpts != null && errpts.length>0){
			for(int i=0;i<errpts.length;i++){
				if(errpts[i]!= null && errpts[i].trim().length()>0){
					errpthash.put(errpts[i], errpts[i]);
				}
			}
		}
		if(errpthash != null && errpthash.size()>0){
			if(errpthash.containsKey("pend"))tstr0="checked";
			if(errpthash.containsKey("perf"))tstr1="checked";
			if(errpthash.containsKey("perm"))tstr2="checked";
			if(errpthash.containsKey("temp"))tstr3="checked";
			if(errpthash.containsKey("info"))tstr4="checked";
			if(errpthash.containsKey("unkn"))tstr5="checked";
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		String cstr0="";
		String cstr1="";
		String cstr2="";
		String cstr3="";
		String errptclass = errptconfig.getErrptclass();
		if(errptclass == null)errptclass = "";
		String[] errptclasses = errptclass.split(",");
		Hashtable errptclasshash = new Hashtable();
		try{
		if(errptclasses != null && errptclasses.length>0){
			for(int i=0;i<errptclasses.length;i++){
				if(errptclasses[i]!= null && errptclasses[i].trim().length()>0){
					errptclasshash.put(errptclasses[i], errptclasses[i]);
					//System.out.println(errptclasses[i]+"---------------");
				}
			}
		}
		if(errptclasshash != null && errptclasshash.size()>0){
			if(errptclasshash.containsKey("h"))cstr0="checked";
			if(errptclasshash.containsKey("s"))cstr1="checked";
			if(errptclasshash.containsKey("o"))cstr2="checked";
			if(errptclasshash.containsKey("u"))cstr3="checked";
		}
		}catch(Exception e){
			e.printStackTrace();
		}
%>


<html>
	<head>
	
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script type="text/javascript">
		 	
			var show = true;
			var hide = false;
			//�޸Ĳ˵������¼�ͷ����
			function my_on(head,body)
			{
				var tag_a;
				for(var i=0;i<head.childNodes.length;i++)
				{
					if (head.childNodes[i].nodeName=="A")
					{
						tag_a=head.childNodes[i];
						break;
					}
				}
				tag_a.className="on";
			}
			function my_off(head,body)
			{
				var tag_a;
				for(var i=0;i<head.childNodes.length;i++)
				{
					if (head.childNodes[i].nodeName=="A")
					{
						tag_a=head.childNodes[i];
						break;
					}
				}
				tag_a.className="off";
			}
		</script>
		<script language="JavaScript">

			//��������
			var node="";
			var ipaddress="";
			var operate="";
			/**
			*���ݴ����id��ʾ�Ҽ��˵�
			*/
			function showMenu(id,nodeid,ip)
			{	
				ipaddress=ip;
				node=nodeid;
				//operate=oper;
			    if("" == id)
			    {
			        popMenu(itemMenu,100,"100");
			    }
			    else
			    {
			        popMenu(itemMenu,100,"1111");
			    }
			    event.returnValue=false;
			    event.cancelBubble=true;
			    return false;
			}
			/**
			*��ʾ�����˵�
			*menuDiv:�Ҽ��˵�������
			*width:����ʾ�Ŀ��
			*rowControlString:�п����ַ�����0��ʾ����ʾ��1��ʾ��ʾ���硰101�������ʾ��1��3����ʾ����2�в���ʾ
			*/
			function popMenu(menuDiv,width,rowControlString)
			{
			    //���������˵�
			    var pop=window.createPopup();
			    //���õ����˵�������
			    pop.document.body.innerHTML=menuDiv.innerHTML;
			    var rowObjs=pop.document.body.all[0].rows;
			    //��õ����˵�������
			    var rowCount=rowObjs.length;
			    //alert("rowCount==>"+rowCount+",rowControlString==>"+rowControlString);
			    //ѭ������ÿ�е�����
			    for(var i=0;i<rowObjs.length;i++)
			    {
			        //������ø��в���ʾ����������һ
			        var hide=rowControlString.charAt(i)!='1';
			        if(hide){
			            rowCount--;
			        }
			        //�����Ƿ���ʾ����
			        rowObjs[i].style.display=(hide)?"none":"";
			        //������껬�����ʱ��Ч��
			        rowObjs[i].cells[0].onmouseover=function()
			        {
			            this.style.background="#397DBD";
			            this.style.color="white";
			        }
			        //������껬������ʱ��Ч��
			        rowObjs[i].cells[0].onmouseout=function(){
			            this.style.background="#F1F1F1";
			            this.style.color="black";
			        }
			    }
			    //���β˵��Ĳ˵�
			    pop.document.oncontextmenu=function()
			    {
			            return false; 
			    }
			    //ѡ���Ҽ��˵���һ��󣬲˵�����
			    pop.document.onclick=function()
			    {
			        pop.hide();
			    }
			    //��ʾ�˵�
			    pop.show(event.clientX-1,event.clientY,width,rowCount*25,document.body);
			    return true;
			}
			
  function toAdd()
  {
      mainForm.action = "<%=rootPath%>/errpt.do?action=save";
      mainForm.submit();
  }
  
	//-- nielin modify at 2010-01-04 start ----------------
	function CreateWindow(url)
	{
		msgWindow=window.open(url,"protypeWindow","toolbar=no,width=1000,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
	}   
			
	function chooseAlarmWay(value,alarmWayIdEvent,alarmWayNameEvent){
	//alert(alarmWayIdEvent+"============"+alarmWayNameEvent);
		CreateWindow("/afunms/alarmWay.do?action=chooselist&jp=1&alarmWayNameEvent=" + alarmWayNameEvent + "&alarmWayIdEvent=" + alarmWayIdEvent);
	}
			
			  
			
		</script>
		<script type="text/javascript">
		
  			var delAction = "<%=rootPath%>/nodeGatherIndicators.do?action=delete";
  			var listAction = "<%=rootPath%>/nodeGatherIndicators.do?action=list";
		</script>
	</head>
	<body id="body" class="body"">
		<form id="mainForm" method="post" name="mainForm">
			<input type="hidden" name="nodeid" id="nodeid" value="<%=nodeid%>">
			<table id="body-container" class="body-container">
				<tr>
					<td class="td-container-main">
						<table id="container-main" class="container-main">
							<tr>
								<td class="td-container-main-content">
									<table id="container-main-content" class="container-main-content">
										<tr>
											<td>
												<table id="content-header" class="content-header">
								                	<tr>
									                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
									                	<td class="content-title"> <b>�澯 >> ERRPT���� >> ERRPT���˹���</b> </td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									       			</tr>
									        	</table>
		        							</td>
		        						</tr>
		        						<tr>
		        							<td>
		        								<table id="content-body" class="content-body">
													<tr>
														<td>
															<table width="100%" cellPadding=0 cellspacing="1" algin="center">
									                    					<tr algin="left" valign="center">                      														
									                      						<td height="28" bgcolor="#ECECEC" colspan="8">&nbsp;&nbsp;&nbsp;&nbsp;<b>���󼶱�</b></td>
									                    					</tr>
									                    					<tr align="left" valign="center" bgcolor=#ffffff> 
									                    						<td height="28" align="right" width=5%>&nbsp;&nbsp;<INPUT type="checkbox" class=noborder value="pend" <%=tstr0%> name="checkbox"></td>
									                    						<td height="28" align="left" width=25%>&nbsp;&nbsp;�豸����������ܶ�ʧ</td>
									                    						<td height="28" align="right" width=5%%><INPUT type="checkbox" class=noborder value="perf" <%=tstr1%> name="checkbox"></td>
									                    						<td height="28" align="left" width=25%>&nbsp;&nbsp;���������½�</td> 
									                    						<td height="28" align="right" width=5%><INPUT type="checkbox" class=noborder value="perm" <%=tstr2%> name="checkbox"></td>
									                    						<td height="28" align="left" width=25%>&nbsp;&nbsp;Ӳ���豸�����ģ����</td> 
									                    						
									                    					</tr> 
									                    					<tr align="left" valign="center" bgcolor=#ffffff> 
									                    						<td height="28" align="right" width=5%>&nbsp;&nbsp;<INPUT type="checkbox" class=noborder value="info" <%=tstr4%> name="checkbox"></td>
									                    						<td height="28" align="left" width=25%>&nbsp;&nbsp;һ����Ϣ�����Ǵ���</td>
									                    						<td height="28" align="right" width=5%><INPUT type="checkbox" class=noborder value="unkn" <%=tstr5%> name="checkbox"></td>
									                    						<td height="28" align="left" width=25%>&nbsp;&nbsp;����ȷ�������������</td>
									                    						<td height="28" align="right" width=5%><INPUT type="checkbox" class=noborder value="temp" <%=tstr3%> name="checkbox"></td>
									                    						<td height="28" align="left" width=25%>&nbsp;&nbsp;��ʱ�Դ��󣬾������Ժ��Ѿ��ָ�����</td>
									                    					</tr>                     					                    					                    					                     		                   										                 										                      								
									            					</table>
									            					<table width="100%" cellPadding=0 cellspacing="1" algin="center">
									                    					<tr algin="left" valign="center">                      														
									                      						<td height="28" bgcolor="#ECECEC" colspan="8">&nbsp;&nbsp;&nbsp;&nbsp;<b>��������</b></td>
									                    					</tr>
									                    					<tr align="left" valign="center" bgcolor=#ffffff> 
									                    						<td height="28" align="right" width=5%>&nbsp;&nbsp;&nbsp;&nbsp;<INPUT type="checkbox" class=noborder value="h" <%=cstr0%> name="ccheckbox"></td>
									                    						<td height="28" align="left" width=20%>&nbsp;&nbsp;Ӳ������ʹ���</td>
									                    						<td height="28" align="right" width=5%%><INPUT type="checkbox" class=noborder value="s" <%=cstr1%> name="ccheckbox"></td>
									                    						<td height="28" align="left" width=20%>&nbsp;&nbsp;�������</td> 
									                    						<td height="28" align="right" width=5%><INPUT type="checkbox" class=noborder value="o" <%=cstr2%> name="ccheckbox"></td>
									                    						<td height="28" align="left" width=20%>&nbsp;&nbsp;��Ϊ����</td> 
									                    						<td height="28" align="right" width=5%><INPUT type="checkbox" class=noborder value="u" <%=cstr3%> name="ccheckbox"></td>
									                    						<td height="28" align="left" width=20%>&nbsp;&nbsp;����ȷ��</td>
									                    						
									                    					</tr>                     					                    					                    					                     		                   										                 										                      								
									            					</table>
	            													<table width="100%" cellPadding=0 cellspacing="1" algin="center">
									                    					<tr algin="left" valign="center">                      														
									                      						<td height="28" bgcolor="#ECECEC" colspan="8">&nbsp;&nbsp;&nbsp;&nbsp;<b>�澯��ʽ����</b></td>
									                    					</tr>
									                    					<tr align="left" valign="center" bgcolor=#ffffff> 
									                    						<td height="28" align="left" width=5%>&nbsp;&nbsp;&nbsp;&nbsp;
									                    									<input type="text" disabled="disabled" value="<%=alarmwayname%>" name="way0-name" id="way0-name">
																				<input type="hidden" value="<%=alarmwayid%>" name="way0-id" id="way0-id">
																				<a href="#" onclick="chooseAlarmWay('','way0-id','way0-name')">���</a>

									                    						</td>
									                    						
									                    					</tr>                     					                    					                    					                     		                   										                 										                      								
									            					</table>									            					
														</td>
													</tr>
													<tr>
																			<TD nowrap colspan="20" align=center><br>
																			<input type="button" value="ȷ ��" style="width:50" class="formStylebutton" onclick="toAdd()">&nbsp;&nbsp;
																			<input type="button" value="�� ��" style="width:50" onclick="window.close()">
																			</TD>	
																		</tr>
		        								</table>
		        							</td>
		        						</tr>
		        						<tr>
		        							<td>
		        								<table id="content-footer" class="content-footer">
		        									<tr>
		        										<td>
		        											<table width="100%" border="0" cellspacing="0" cellpadding="0">
									                  			<tr>
									                    			<td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
									                    			<td></td>
									                    			<td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
									                  			</tr>
									              			</table>
		        										</td>
		        									</tr>
		        								</table>
		        							</td>
		        						</tr>
		        					</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
