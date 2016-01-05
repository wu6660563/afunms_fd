<%@page contentType="text/html"%>
<%@page pageEncoding="GBK"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.system.model.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
    <%
    	String rootPath = request.getContextPath(); 
    %>
        <meta http-equiv="Content-Type" content="text/html; charset=GBK">
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
        <script type="text/javascript">    
            var objectId = "<%=(String) request.getAttribute("objectId")%>";
            var objectType = "<%=(String) request.getAttribute("objectType")%>";
            var action = "<%=(String) request.getAttribute("action")%>";
            var firewallconfid = "<%=(String) request.getAttribute("firewallconfid")%>";
            var confid = "<%=(String) request.getAttribute("confid")%>";
            var currentName="";
            function setMobile(name){
            	
                currentName=name;
                alert(currentName);
                var url="<%=rootPath%>/user.do?action=setnetsmsmobiles&equipmentid="+objectId+"&firewallconfid="+firewallconfid+"&confid="+confid+"";
                msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no");
            }     
            function setMobileValue(sendmobiles){
            	document.getElementById(currentName).value = sendmobiles
            }              
            function addSmsConfig(){
            	smsConfigForm.rowNum.value=rowNum;
            	smsConfigForm.objectId.value=objectId;
            	smsConfigForm.objectType.value=objectType;
                    smsConfigForm.submit();
            }
            
            var rowNum=0;
            function addRow(){
                var tr = document.getElementById("smsConfigTable1").insertRow(0); 
                rowNum=rowNum+1;
                var str = ""+rowNum;
                while(str.length<2){
                    str = "0"+str;
                }
                var hours="";
                for(i=0;i<=24;i++){
                    hours=hours+'<OPTION value="'+i+'">'+i+'</OPTION>';
                }
                var td = tr.insertCell(tr.cells.length);

                var elemTable = document.createElement("table");
                var elemTBody = document.createElement("tBody");
                
                elemTable.id = "table"+str;
                elemTable.width="100%";
                elemTable.align = "left"; 
                elemTable.cellPadding = "1"; 
                elemTable.cellSpacing = "1"; 
                elemTable.bgColor = "black"; 
                elemTBody.bgColor = "white";
                elemTable.appendChild(elemTBody);
                td.appendChild(elemTable);
        
                var tr0=document.createElement("tr");
                elemTBody.appendChild(tr0);
                var td00 = tr0.insertCell(tr0.cells.length);
                td00.className="lab";
                td00.innerHTML='<input name="smsConfigId'+str+'" type="hidden"  /><span class="must">*</span>��ʼʱ��';
                var td01 = tr0.insertCell(tr0.cells.length);
                td01.align="left";
                td01.innerHTML='<select name="beginTime'+str+'" style="width:100px">'+hours+'</select>';
                var td02 = tr0.insertCell(tr0.cells.length);
                td02.className="lab";
                td02.innerHTML='<span class="must">*</span>����ʱ��';
                var td03 = tr0.insertCell(tr0.cells.length);
                td03.align="left";
                td03.innerHTML='<select name="endTime'+str+'" style="width:100px">'+hours+'</select>';
                var td04 = tr0.insertCell(tr0.cells.length);
                td04.className="lab";
                td04.innerHTML='<span class="must">*</span>���Ž�����';
                var td05 = tr0.insertCell(tr0.cells.length);
                td05.align="left";
                td05.innerHTML='<input class="input-text" name="userIds'+str+'" type="text"  size="40"  onclick="setMobile(this.name)" />';
                var td06 = tr0.insertCell(tr0.cells.length); 
                td06.className="lab";
                td06.innerHTML='<a href="javascript:delRow('+rowNum+')">ɾ��</a>';
            }
            
            function delRow(rowNo){
                var str = ""+rowNo;
                while(str.length<2){
                    str = "0"+str;
                }
                var i=0;
                
                document.getElementById("smsConfigTable1").rows[i]
                
                
                //while($("smsConfigTable1").rows[i].firstChild.firstChild.id!="table"+str){
                while(document.getElementById("smsConfigTable1").rows[i].firstChild.firstChild.id!="table"+str){
                    i++;
                }
                document.getElementById("smsConfigTable1").deleteRow(i);
                //$("smsConfigTable1").deleteRow(i);
            }
            
            function init(){
                var smsConfigs = new Array();
        <%


            List smsConfigArrayList = (List) request.getAttribute("SmsConfigArrayList");
            Iterator iterator = smsConfigArrayList.iterator();
            while (iterator.hasNext()) {
                SmsConfig smsConfig = (SmsConfig) iterator.next();
                int id = smsConfig.getId();
                String beginTime = smsConfig.getBeginTime();
                String endTime = smsConfig.getEndTime();
                String userIds = smsConfig.getUserIds();
        %>
                smsConfigs.push({
                    smsConfigId:"<%=id%>",
                    beginTime:"<%=beginTime%>",
                    endTime:"<%=endTime%>",
                    userIds:"<%=userIds%>"
                });
        <%
            }
        %>   
                for(var i=0;i<smsConfigs.length;i++){
                    var item=smsConfigs[i];
                    addRow();
                    var str = ""+rowNum;
                    if(str.length<2){
                        str = "0"+str;
                    }
                    alert(item.smsConfigId+"==="+item.beginTime+"==="+item.endTime+"==="+item.userIds);
     
                    //smsConfigForm."smsConfigId'"+str"'".value = item.smsConfigId;
                    //alert("%%%%%%%%%%%%%%%%%%");
                   // document.getElementByName("smsConfigId"+str).value=item.smsConfigId;
                    //document.getElementByName("beginTime"+str).value=item.beginTime;
                    //document.getElementByName("endTime"+str).value=item.endTime;
                    //document.getElementByName("userIds"+str).value=item.userIds;
                    alert("================================");
                    //smsConfigForm.smsConfigId
                    //$("smsConfigId"+str).value=item.smsConfigId;
                    //$("beginTime"+str).value=item.beginTime;
                    //$("endTime"+str).value=item.endTime;
                    //$("userIds"+str).value=item.userIds;
                }
            }
        </script>
    </head>  
    <body onLoad="init();">
        <form action="<%=rootPath%>/smsconfig.do?action=add" name="smsConfigForm" method="post">  
            <div id="formDiv" style="border: 0px 1px 1px 0px solid #e8f2ff;">         
                <table class="tab3">
                    <tbody>
                        <tr>
                            <td style="padding:0px 0px 0px 5px">���ŷ�����ϸ����</td>
                        </tr>
                        <tr>
                            <td>           
                                <table id="smsConfigTable1"  style="width:100%; padding:0;  background-color:#FFFFFF;" >
                                    <tbody>
                                        <tr>
                                            <td colspan="0" height="50" align="center"> 
                                                <span  onClick="addRow();" style="border: 1px solid black;margin:10px;padding:0px 3px 0px 3px;cursor: hand;">��&nbsp;&nbsp;��</span><span  onClick="addSmsConfig();" style="border: 1px solid black;margin:10px;padding:0px 3px 0px 3px;cursor: hand;">��&nbsp;&nbsp;��</span>
                                            </td>
                                        </tr>
                                    </tbody> 
                                </table>
                            </td>
                        </tr>
                    </tbody> 
                </table>
            </div> 
            <input type="hidden" name="sendmobiles" value="0" />
            <input type="hidden" name="objectId" value="0" />
            <input type="hidden" name="objectType" value="" /> 
            <input type="hidden" name="operate" value="" /> 
            <input type="hidden" name="firewallconfid" value="" /> 
            <input type="hidden" name="confid" value="" />             
            <input type="hidden" name="rowNum" value=""/>
            <input type="hidden" name="action" value="add"/>
        </form>
    </body>
</html>
