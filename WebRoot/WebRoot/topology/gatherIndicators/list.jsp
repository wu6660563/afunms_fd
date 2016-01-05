<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.indicators.model.GatherIndicators;"%>

<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  List list = (List)request.getAttribute("list");
  
  JspPage jp = (JspPage)request.getAttribute("page");
  String gatherfindselect=(String)session.getAttribute("gatherfindselect");
%>


<html>
	<head>
	
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		
		<!-- �����������˵� -->
		<script type="text/javascript">
			SPT="--��ѡ������--";
			SCT="--��ѡ�����--";
			SAT="--��ѡ��ָ��--";
			ShowT=1;		//��ʾ���� 0:����ʾ 1:��ʾ
			PCAD="<%=gatherfindselect%>";
			if(ShowT)PCAD=SPT+"$"+SCT+","+SAT+"#"+PCAD;
			PCAArea=[];
			PCAP=[];
			PCAC=[];
			PCAA=[];
			PCAN=PCAD.split("#");
			for(i=0;i<PCAN.length;i++)
			{
				PCAA[i]=[];TArea=PCAN[i].split("$")[1].split("|");
				for(j=0;j<TArea.length;j++)
				{
					PCAA[i][j]=TArea[j].split(",");
					if(PCAA[i][j].length==1)
					PCAA[i][j][1]=SAT;TArea[j]=TArea[j].split(",")[0]
				}
					PCAArea[i]=PCAN[i].split("$")[0]+","+TArea.join(",");
					PCAP[i]=PCAArea[i].split(",")[0];
					PCAC[i]=PCAArea[i].split(',')
			}
	
			function PCAS()
			{
				this.SelP=document.getElementsByName(arguments[0])[0];
				this.SelC=document.getElementsByName(arguments[1])[0];
				this.SelA=document.getElementsByName(arguments[2])[0];
				this.DefP=this.SelA?arguments[3]:arguments[2];
				this.DefC=this.SelA?arguments[4]:arguments[3];
				this.DefA=this.SelA?arguments[5]:arguments[4];
				this.SelP.PCA=this;
				this.SelC.PCA=this;
				this.SelP.onchange=function(){PCAS.SetC(this.PCA)};
				if(this.SelA)
				this.SelC.onchange=function(){PCAS.SetA(this.PCA)};
				PCAS.SetP(this)
			};
				
				PCAS.SetP=function(PCA){
				for(i=0;i<PCAP.length;i++){PCAPT=PCAPV=PCAP[i];
				if(PCAPT==SPT)PCAPV="";
				PCA.SelP.options.add(new Option(PCAPT,PCAPV));
				if(PCA.DefP==PCAPV)PCA.SelP[i].selected=true}PCAS.SetC(PCA)
				};
				
				PCAS.SetC=function(PCA){
				PI=PCA.SelP.selectedIndex;
				PCA.SelC.length=0;
				for(i=1;i<PCAC[PI].length;i++){PCACT=PCACV=PCAC[PI][i];
				if(PCACT==SCT)PCACV="";
				PCA.SelC.options.add(new Option(PCACT,PCACV));
				if(PCA.DefC==PCACV)
				PCA.SelC[i-1].selected=true}
				if(PCA.SelA)PCAS.SetA(PCA)
				};
				
				PCAS.SetA=function(PCA){
				PI=PCA.SelP.selectedIndex;
				CI=PCA.SelC.selectedIndex;
				PCA.SelA.length=0;
				for(i=1;i<PCAA[PI][CI].length;i++){
				PCAAT=PCAAV=PCAA[PI][CI][i];
				if(PCAAT==SAT)
				PCAAV="";
				PCA.SelA.options.add(new Option(PCAAT,PCAAV));
				if(PCA.DefA==PCAAV)PCA.SelA[i-1].selected=true}
				}
			</script>	
		
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
				mainForm.action = "<%=rootPath%>/gatherIndicators.do?action=add";
				mainForm.submit();
			}
			
			function edit(){
				mainForm.action = "<%=rootPath%>/gatherIndicators.do?action=edit&id=" + node;
				mainForm.submit();
			}
			function toFind()
			  {
			     mainForm.action = "<%=rootPath%>/gatherIndicators.do?action=find";
			     mainForm.submit();
			  }
			
			
			  
			
		</script>
		<script type="text/javascript">
		
			var curpage= <%=jp.getCurrentPage()%>;
  			var totalpages = <%=jp.getPageTotal()%>;
  			var delAction = "<%=rootPath%>/gatherIndicators.do?action=delete";
  			var listAction = "<%=rootPath%>/gatherIndicators.do?action=list";
		</script>
	</head>
	<body id="body" class="body" onload="initmenu();">
		<!-- ��������������Ҫ��ʾ���Ҽ��˵� -->
		<div id="itemMenu" style="display: none";>
		<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
			style="border: thin;font-size: 12px" cellspacing="0">
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.edit()">�༭</td>
			</tr>
		</table>
		</div>
		<!-- �Ҽ��˵�����-->
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
									                	<td class="content-title">&nbsp;��Դ >> ���ܼ��� >> ����ָ��һ���� </td>
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
													    			<td  class="body-data-title">
							    										<jsp:include page="../../common/page.jsp">
																			<jsp:param name="curpage" value="<%=jp.getCurrentPage()%>"/>
																			<jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>"/>
							  										 	</jsp:include>
							        								</td>
							        							</tr>
															</table>
														</td>
													</tr> 
													<tr>
														<td>
															<table>
																<tr>
																<%
																	String select3="--��ѡ������--";
																	String select4="--��ѡ������--";
																	select3=(String)request.getAttribute("con3");
																	select4=(String)request.getAttribute("con4");
																 %>
																	<td class="body-data-title" style="text-align: right;">
																		���:<select size="1" name="categorycon">
																			</select>
																	</td>
																	<td class="body-data-title" style="text-align: right;">
																		���ͣ�<select size="1" name="entitycon">
																			</select>
																	</td>
																	<script language="javascript" defer>
																		new PCAS("categorycon","entitycon","<%=select3%>","<%=select4%>");
																	</script>
																	<td class="body-data-title" style="text-align: right;">
																	<input type="button" name="find" value="��ѯ" onclick="toFind()">
																	</td>
													    			<td  class="body-data-title" style="text-align: right;">
													    				<a href="#" onclick="window.open('<%=rootPath%>/nodeGatherIndicators.do?action=list&nodeid=173&type=db&subtype=Oracle')">�豸�ɼ�ָ���б�</a>&nbsp;&nbsp;&nbsp;
							    										<a href="#" onclick="add()">���</a>&nbsp;&nbsp;&nbsp;
							    										<a href="#" onclick="toDelete()">ɾ��</a>&nbsp;&nbsp;&nbsp;
							        								</td>
							        							</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<table>
																<tr>
													    			<td class="body-data-title"><INPUT type="checkbox" name="checkall" onclick="javascript:chkall()">���</td>
													    			<td class="body-data-title">����</td>
													    			<td class="body-data-title">����</td>
													    			<td class="body-data-title">����</td>
													    			<td class="body-data-title">������</td>
													    			<td class="body-data-title">�Ƿ�ɼ�</td>
													    			<td class="body-data-title">�Ƿ�����Ĭ������</td>
													    			<td class="body-data-title">�ɼ����</td>
													    			<td class="body-data-title">����</td>
							        							</tr>
							        							<%
							        							
							        								if(list != null && list.size() > 0) {
							        									for(int i = 0; i < list.size(); i++){
							        										GatherIndicators gatherIndicators = (GatherIndicators)list.get(i);
							        										
							        										String unit = "";
							        										if("m".equals(gatherIndicators.getInterval_unit())){
							        											unit = "����";
							        										}else if("h".equals(gatherIndicators.getInterval_unit())){
							        											unit = "Сʱ";
							        										}else if("d".equals(gatherIndicators.getInterval_unit())){
							        											unit = "��";
							        										}else if("w".equals(gatherIndicators.getInterval_unit())){
							        											unit = "��";
							        										}else if("mt".equals(gatherIndicators.getInterval_unit())){
							        											unit = "��";
							        										}else if("y".equals(gatherIndicators.getInterval_unit())){
							        											unit = "��";
							        										}
							        										String isCollection = "��";
							        										if("1".equals(gatherIndicators.getIsCollection())){
							        											isCollection = "��";
							        										}
							        										String isDefault = "��";
							        										if("1".equals(gatherIndicators.getIsDefault())){
							        											isDefault = "��";
							        										}
							        										%>
							        											<tr <%=onmouseoverstyle%>>
																	    			<td class="body-data-list"><INPUT type="checkbox" name="checkbox" value="<%=gatherIndicators.getId()%>"><%=jp.getStartRow()+i%></td>
																	    			<td class="body-data-list"><%=gatherIndicators.getName()%></td>
																	    			<td class="body-data-list"><%=gatherIndicators.getAlias()%></td>
																	    			<td class="body-data-list"><%=gatherIndicators.getType()%></td>
																	    			<td class="body-data-list"><%=gatherIndicators.getSubtype()%></td>
																	    			<td class="body-data-list"><%=isCollection%></td>
																	    			<td class="body-data-list"><%=isDefault%></td>
																	    			<td class="body-data-list"><%=gatherIndicators.getPoll_interval() + " " + unit%></td>
																	    			<!-- 
																	    			<td class="body-data-list"><img src="<//%=rootPath%>/resource/image/status.gif"
																						border="0" width=15 oncontextmenu=showMenu('2','<//%=gatherIndicators.getId()%>','') alt="�Ҽ�����">
																					</td>
																					 -->
																					<td  align='center'  class="body-data-list"><a href="<%=rootPath%>/gatherIndicators.do?action=edit&id=<%=gatherIndicators.getId()%>">
							       														<img src="<%=rootPath%>/resource/image/editicon.gif" border="0" alt="�༭"/></a>
							       													</td>
											        							</tr>
							        										<%
							        									}
							        								}%>
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
						</table>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
