<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.system.model.*"%>
<%@page import="com.afunms.config.model.Portconfig"%>
<%@ include file="/include/globe.inc"%>
<%
    String rootPath = request.getContextPath();
    List list = (List) request.getAttribute("list");
    String id = (String) request.getAttribute("id");
    String ipaddress = (String) request.getAttribute("ipaddress");
%>
<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
        <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
        <script language="JavaScript" type="text/javascript">

            function doNodeFromlastoconfig() {  
                mainForm.action = "<%=rootPath%>/portconfig.do?action=fromnodelasttoconfig";
                mainForm.submit();
            }  
  
            function doEmptyNode() {  
                mainForm.action = "<%=rootPath%>/portconfig.do?action=emptyNode";
                mainForm.submit();
            } 

            function CreateWindow(url) {
                msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
            }    

        </script>
        <script language="JavaScript" type="text/JavaScript">
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

    </head>
    <body id="body" class="body" onload="initmenu();">
        <form name="mainForm" method="post">
            <input type=hidden name="id" value="<%=id%>">
            <input type=hidden name="ipaddress" value="<%=ipaddress%>">
            <table id="body-container" class="body-container">
                <tr>

                    <td class="td-container-main">
                        <table id="container-main" class="container-main">
                            <tr>
                                <td class="td-container-main-add">
                                    <table id="container-main-add" class="container-main-add">
                                        <tr>
                                            <td>
                                                <table id="add-content" class="add-content">
                                                    <tr>
                                                        <td>
                                                            <table id="add-content-header" class="add-content-header">
                                                                <tr>
                                                                    <td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
                                                                    <td class="add-content-title"> �豸�˿�����Ϣ >> <%=ipaddress%></td>
                                                                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <table id="add-content-header" class="add-content-header" width="100%">
                                                                <tr>
                                                                    <td align="right" width="">
                                                                        <INPUT type="button" class="formStyle" value="ˢ ��" onclick="doNodeFromlastoconfig()" />
                                                                        <INPUT type="button" class="formStyle" value="�� ��" onclick="doEmptyNode()" />
                                                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <table id="detail-content-body" class="detail-content-body">
                                                                <tr>
                                                                    <td>

                                                                        <table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%">
                                                                            <tr>
                               													<td align="center" class="body-data-title"><INPUT type="checkbox" id="checkall" name="checkall" onclick="javascript:chkall()">���</td>
                               													<td align="center" class="body-data-title">�豸����(ip)</td>
                               													<td align="center" class="body-data-title">�˿�����</td>
                               													<td align="center" class="body-data-title">�˿�����</td>
                               													<td align="center" class="body-data-title">�˿�Ӧ��</td>
                               													<td align="center" class="body-data-title">�˿�Down�Ƿ�澯</td>
                               													<td align="center" class="body-data-title">�ؼ��˿�</td>
                                                                                <td align="center" class="body-data-title">����</td>
                        		        									</tr>
                                                                            <%
                                                                                Portconfig vo = null;
                                                                                for (int i = 0; i < list.size(); i++) {
                                                                                    vo = (Portconfig) list.get(i);
                                                                                    String isSMS = "��";
                                                                                    if (vo.getSms() == 0) {
                                                                                        isSMS = "��";
                                                                                    }
                                                                                    String isImportant = "��";
                                                                                    if (vo.getImportant() == 0) {
                                                                                        isImportant = "��";
                                                                                    }
                                                                            %>
                                                                            <tr <%=onmouseoverstyle%>>
                            													<td align="center" class="body-data-list"><INPUT type="checkbox" value="<%=vo.getId()%>" name="checkbox"><%=1 + i%></td>
                            													<td align="center" class="body-data-list"><%=vo.getIpaddress()%></td>
                            													<td align="center" class="body-data-list"><%=vo.getPortindex()%></td>
                            													<td align="center" class="body-data-list"><%=vo.getName()%></td>
                            													<td align="center" class="body-data-list" id="linkUse<%=vo.getId()%>"><%=vo.getLinkuse()%></td>
                            													<td align="center" class="body-data-list" id="isSMS<%=vo.getId()%>"><%=isSMS%></td>
                                                                                <td align="center" class="body-data-list" id="isImportant<%=vo.getId()%>"><%=isImportant%></td>
                            													<td align="center" class="body-data-list"><span style='cursor:hand' onClick='window.open("<%=rootPath%>/portconfig.do?action=showedit&id=<%=vo.getId()%>","_blank", "height=400, width= 500, top=200, left= 200")'>
                        											<img src="<%=rootPath%>/resource/image/editicon.gif" border="0"/></td>
                        		        									</tr>
                                                                            <% 
                        	        									    }
                                                                            %>
                                                                        </table>
                                                                        <br>
                                                                    </TD>
                                                                </tr>
                                                                <tr>
                                                                    <TD nowrap colspan="4" align=center>

                                                                        <input type="reset" style="width: 50" value="�ر�" onclick="javascript:window.close()">
                                                                    </TD>
                                                                </tr>

                                                            </table>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <table id="detail-content-footer" class="detail-content-footer">
                                                                <tr>
                                                                    <td>
                                                                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                                            <tr>
                                                                                <td align="left" valign="bottom">
                                                                                    <img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" />
                                                                                </td>
                                                                                <td></td>
                                                                                <td align="right" valign="bottom">
                                                                                    <img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" />
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
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>

        </form>
    </BODY>


</HTML>