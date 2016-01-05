<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.alarm.model.AlarmIndicators"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.alarm.model.AlarmThreshold"%>
<%@ include file="/include/globe.inc"%>

<%
	String rootPath = request.getContextPath();
  	String menuTable = (String)request.getAttribute("menuTable");
	List list = (List)request.getAttribute("list");
  
	String indicatorsId = (String)request.getAttribute("indicatorsId");
  	AlarmIndicators alarmIndicators = (AlarmIndicators)request.getAttribute("alarmIndicators");
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
			//��Ӳ˵�	
			function initmenu()
			{
				var idpattern=new RegExp("^menu");
				var menupattern=new RegExp("child$");
				var tds = document.getElementsByTagName("div");
				for(var i=0,j=tds.length;i<j;i++){
					var td = tds[i];
					if(idpattern.test(td.id)&&!menupattern.test(td.id)){					
						menu =new Menu(td.id,td.id+"child",'dtu','100',show,my_on,my_off);
						menu.init();		
					}
				}
				
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
			
			
			function add(){
				mainForm.action = "<%=rootPath%>/alarmIndicators.do?action=add";
				mainForm.submit();
			}
			
			function edit(){
				mainForm.action = "<%=rootPath%>/alarmIndicators.do?action=edit&id=" + node;
				mainForm.submit();
			}
			
			function save(){
				mainForm.action = "<%=rootPath%>/alarmIndicators.do?action=saveAlarmThreshold&indicatorsId=" + "<%=indicatorsId%>";
				mainForm.submit();
			}
			
			
			function setDefaultValue(){
				mainForm.action = "<%=rootPath%>/alarmIndicators.do?action=setDefaultValue&indicatorsId=" + "<%=indicatorsId%>";
				mainForm.submit();
			}
			
			
			  
			
		</script>
		
		<script type="text/javascript">
		
			var rowNum = 0;
		
			function initAlarmThresholdList(){
				var alarmThresholds = new Array();
				<%
					if(list !=null && list.size() != 0){
						System.out.println(list.size());
						for(int i = 0; i < list.size() ; i++){
							AlarmThreshold alarmThreshold = (AlarmThreshold)list.get(i);
				%>
							alarmThresholds.push({
								id:'<%=alarmThreshold.getId()%>',
								indicatorsId:'<%=alarmThreshold.getIndicatorsId()%>',
								datatype:'<%=alarmThreshold.getDatatype()%>',
								level:'<%=alarmThreshold.getLevel()%>',
								alarmTimes:'<%=alarmThreshold.getAlarmTimes()%>',
								value:'<%=alarmThreshold.getValue()%>',
								unit:'<%=alarmThreshold.getUnit()%>',
								isAlarm:'<%=alarmThreshold.getIsAlarm()%>',
								type:'<%=alarmThreshold.getType()%>',
								isSendSMS:'<%=alarmThreshold.getIsSendSMS()%>',
								bak:'<%=alarmThreshold.getBak()%>'
							});
				<%
						}
					}
				%>
				
				var alarmThresholdTable = document.getElementById("alarmThresholdTable");
				alarmThresholdTable.innerHTML = "";
				rowNum = 0;
				for(var i = 0;i < alarmThresholds.length;i++){
					addAlarmThresholdTable();
					var alarmThreshold = alarmThresholds[i];
					
					var str = "" + rowNum;
					if (str.length < 2) {
						str = "0" + str;
					}
					//document.getElementById("timeShareConfigId" + str).value = alarmThreshold.id;
					//document.getElementById("indicatorsId" + str).value = alarmThreshold.indicatorsId;
					document.getElementById("datatype" + str).value = alarmThreshold.datatype;
					document.getElementById("level" + str).value = alarmThreshold.level;
					document.getElementById("alarmTimes" + str).value = alarmThreshold.alarmTimes;
					document.getElementById("value" + str).value = alarmThreshold.value;
					document.getElementById("unit" + str).value = alarmThreshold.unit;
					document.getElementById("isAlarm" + str).value = alarmThreshold.isAlarm;
					document.getElementById("type" + str).value = alarmThreshold.type;
					document.getElementById("isSendSMS" + str).value = alarmThreshold.isSendSMS;
					document.getElementById("bak" + str).value = alarmThreshold.bak;
				}
				
			}
			
			
			
			function addAlarmThresholdTable(){
			
				rowNum = rowNum + 1;
				var str = "" + rowNum;
				while (str.length < 2) {
					str = "0" + str;
				}
				var table = '<table id="container-main-content-' + str + '"  class="container-main-content">'+
				'<tr><td><table class="content-header">'+
				'<tr>'+
				'<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>'+
				'<td class="content-title"> �澯ָ�귧ֵ���� ' + str  +' </td>'+
				'<td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>'+
				'</tr>'+
				'</table>'+
		        '</td>'+
		        '</tr>'+
		        '<tr>'+
		        '<td>'+
		        '<table class="content-body">'+
				'<tr>'+
				'<td>'+
				'<table>'+
				'<tr>'+
				'<td class="body-data-title" >��������</td>'+
				'<td class="body-data-title">�ȼ�</td>'+
				'<td class="body-data-title">�澯����</td>'+
				'<td class="body-data-title">�澯��ֵ</td>'+
				'<td class="body-data-title">��ֵ��λ</td>'+
				'<td class="body-data-title">�Ƿ�澯</td>'+
				'<td class="body-data-title">�Ƿ��Ͷ���</td>'+
				'<td class="body-data-title">����</td>'+
				'<td class="body-data-title">�澯����</td>'+
				'<td class="body-data-title">����</td>'+
				'</tr>'+
				'<tr>'+
				'<td class="body-data-list"><input style="width:80px" type="text" id="datatype'+ str +'"  name="datatype'+ str +'"></td>'+
				'<td class="body-data-list"><input style="width:80px" type="text" id="level'+ str +'" name="level'+ str +'"></td>'+
				'<td class="body-data-list"><input style="width:80px" type="text" id="alarmTimes'+ str +'" name="alarmTimes'+ str +'"></td>'+
				'<td class="body-data-list"><input style="width:80px" type="text" id="value'+ str +'" name="value'+ str +'"></td>'+
				'<td class="body-data-list"><input style="width:80px" type="text" id="unit'+ str +'" name="unit'+ str +'"></td>'+
				'<td class="body-data-list"><input style="width:80px" type="text" id="isAlarm'+ str +'" name="isAlarm'+ str +'"></td>'+
				'<td class="body-data-list"><input style="width:80px" type="text" id="isSendSMS'+ str +'" name="isSendSMS'+ str +'"></td>'+
				'<td class="body-data-list"><input style="width:80px" type="text" id="type'+ str +'" name="type'+ str +'"></td>'+
				'<td class="body-data-list"><input type="text" id="bak'+ str +'" name="bak'+ str +'"></td>'+
				'<td class="body-data-list"><a href=javascript:delRow1('+ str +')>ɾ��</a></td>'+
				'</tr>'+
				'</table>'+
				'</td>'+
				'</tr>'+
		        '</table>'+
		        '</td>'+
		        '</tr>'+
		        '<tr>'+
		        '<td>'+
		        '<table class="content-footer">'+
		        '<tr>'+
		        '<td>'+
		        '<table width="100%" border="0" cellspacing="0" cellpadding="0">'+
				'<tr>'+
				'<td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>'+
				'<td></td>'+
				'<td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>'+
				'</tr>'+
				'</table>'+
		        '</td>'+
		        '</tr>'+
		        '</table>'+
		        '</td>'+
		        '</tr>'+
		        '</table>';
		        var alarmThresholdTable = document.getElementById("alarmThresholdTable");
		        alarmThresholdTable.insertAdjacentHTML("beforeEnd" , table);
		        document.getElementById("rowNum").value = rowNum;
			}
			
			function delRow1(row) {
				var str = "" + row;
				while (str.length < 2) {
					str = "0" + str;
				}
				var alarmThresholdTable = document.getElementById("alarmThresholdTable");
				var alarmThresholdTable1 = document.getElementById("container-main-content-" + str);
		        alarmThresholdTable.removeChild(alarmThresholdTable1);
			}
			
			
		</script>
	</head>
	<body id="body" class="body" onload="initmenu();initAlarmThresholdList();">
		<form id="mainForm" method="post" name="mainForm">
			<table id="body-container" class="body-container">
				<tr>
					<td class="td-container-menu-bar">
						<table id="container-menu-bar" class="container-menu-bar">
							<tr>
								<td>
									<%=menuTable%>
								</td>	
							</tr>
						</table>
					</td>
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
									                	<td class="content-title"> �澯 >> �澯���� >> �澯ָ�귧ֵ���� </td>
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
															<table>
																<tr>
													    			<td  class="body-data-title" style="text-align: left;">
													    				����ָ��
							        								</td>
							        								<td  class="body-data-title" style="text-align: left;">
													    				<a onclick="addAlarmThresholdTable();" href="#">���ӷ�ֵ��ϸ��Ϣ����</a>&nbsp;&nbsp;&nbsp;
													    				<input type="hidden" id="rowNum" name="rowNum">
													    				<a onclick="save();" href="#">����</a>&nbsp;&nbsp;&nbsp;
													    				<a onclick="addAlarmThresholdTable();" href="#">����</a>&nbsp;&nbsp;&nbsp;
													    				<a onclick="initAlarmThresholdList();" href="#">����</a>&nbsp;&nbsp;&nbsp;
																		<a onclick="initAlarmThresholdList();" href="#">����</a>&nbsp;&nbsp;&nbsp;
																		<a onclick="setDefaultValue();" href="#">Ӧ�����豸Ĭ��ֵ</a>&nbsp;&nbsp;&nbsp;
							        								</td>
							        							</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<table>
																<tr>
													    			<td class="body-data-title">����</td>
													    			<td class="body-data-title">����</td>
													    			<td class="body-data-title">������</td>
													    			<td class="body-data-title">����</td>
							        							</tr>
				        										<tr>
													    			<td class="body-data-list"><%=alarmIndicators.getName()%></td>
													    			<td class="body-data-list"><%=alarmIndicators.getType()%></td>
													    			<td class="body-data-list"><%=alarmIndicators.getSubtype()%></td>
													    			<td class="body-data-list"><%=alarmIndicators.getDescription()%></td>
							        							</tr>
															</table>
														</td>
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
							
							<tr>
								<td>
									<div id="alarmThresholdTable" class="td-container-main-content"></div>
								</td>
							</tr>
							
						</table>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
