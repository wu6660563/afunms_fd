<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.config.model.OracleTableSpaceConfig"%>
<%
    String rootPath = request.getContextPath();
    String menuTable = (String)request.getAttribute("menuTable");
    List<OracleTableSpaceConfig> list = (List<OracleTableSpaceConfig>)request.getAttribute("list");
    List<NodeDTO> nodeList = (List<NodeDTO>)request.getAttribute("nodeList");
    String nodeid = (String)request.getAttribute("nodeid");
    //JspPage jp = (JspPage)request.getAttribute("page");
%>
<%%>
<html>
<head>
    <script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
    <link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
    <link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
    <script language="javascript">	
        
        function doQuery() {  
            mainForm.action = "<%=rootPath%>/oratablespaconfig.do?action=list";
            mainForm.submit();
        }

        function refresh() {
            mainForm.action = "<%=rootPath%>/oratablespaconfig.do?action=refresh";
            mainForm.submit();
        }  
        
        function doChange()
        {
         if(mainForm.view_type.value==1)
            window.location = "<%=rootPath%>/topology/network/index.jsp";
         else
            window.location = "<%=rootPath%>/topology/network/port.jsp";
        }
        
        function toAdd()
        {
          mainForm.action = "<%=rootPath%>/oratablespaconfig.do?action=ready_add";
          mainForm.submit();
        }
        function toEmpty() {
            mainForm.action = "<%=rootPath%>/oratablespaconfig.do?action=empty";
            mainForm.submit();
        }
        function toDelete()
        {
          mainForm.action = "<%=rootPath%>/oratablespaconfig.do?action=delete";
          mainForm.submit();
        }
    </script>
    <script language="JavaScript" type="text/JavaScript">
        var show = true;
        var hide = false;
        //修改菜单的上下箭头符号
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
        //添加菜单	
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
                                                        <td class="content-title"> 资源 >> 性能监视 >> Oracle表空间阀值一览</td>
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
                                                            <table width="100%" >
                                                                <tr>
                                                                    <td class="body-data-title" style="text-align: left">
            				                                            &nbsp;&nbsp;<B>查询:</B>
                    								                    <SELECT name="nodeid" style="width: 150">
                                    									<%
                                										if(nodeList != null){
                                											for(NodeDTO node : nodeList){
                                                                                String selected = "";
                                                                                if (node.getNodeid().equals(nodeid)) {
                                                                                    selected = "selected=\"selected\"";
                                                                                }
                                    									%> 
                      									                     <OPTION value="<%=node.getNodeid()%>" <%=selected%>><%=node.getName()%></OPTION>
                                      									<%
                                  											}
                                  										}
                                      									%>
                                                                        </SELECT>&nbsp;
                                                                        <INPUT type="button" value="查询" onclick=" return doQuery()">
                                                                    </td>
                                                                    <td class="body-data-title">
                                                                        <INPUT type="button" value="清 空" onclick=" return toEmpty()">
            									                        <INPUT type="button" value="刷 新" onclick=" return refresh()">
            							                                <INPUT type="button" value="删 除" onclick=" return toDelete()">&nbsp;&nbsp;
            		  						                        </td>
                                                                </tr>
                                                            </table>
                                                        </td>    
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <table>
                                                                <tr>
                                                                    <td class="body-data-title" width='5%'><INPUT type="checkbox" name="checkall" onclick="javascript:chkall()"></td>
                                                                    <td class="body-data-title" width="8%">IP地址</td>
                                                                    <td class="body-data-title" width="7%">数据库</td>  
                                                                    <td class="body-data-title" width="8%">表空间</td>  
                                                                    <td class="body-data-title" width="5%">是否告警</td>
                                                                    <td class="body-data-title" width="8%">一级阀值</td>
                                                                    <td class="body-data-title" width="5%">是否告警</td>
                                                                    <td class="body-data-title" width="8%">二级阀值</td>
                                                                    <td class="body-data-title" width="5%">是否告警</td>
                                                                    <td class="body-data-title" width="8%">三级阀值</td>
                                                                    <td class="body-data-title" width="5%">是否告警</td>
                                                                    <td class="body-data-title" width='8%'>备注</td>
                                                                    <td class="body-data-title" width='5%'>操作</td>
                                                                </tr>
                                                                <%
                                                                int i = 0;
                                                                for(OracleTableSpaceConfig vo : list) {
                                                                    i ++;
                                                                    String monflag = "是";
                                                                    if (vo.getMonflag() == 0) {
                                                                        monflag="否";
                                                                    }
                                                                    String sms = "是";
                                                                    if(vo.getSms() == 0) {
                                                                        sms="否";
                                                                    }
                                                                    String sms1 = "是";
                                                                    if(vo.getSms1() == 0) {
                                                                        sms1="否";
                                                                    }
                                                                    String sms2 = "是";
                                                                    if(vo.getSms2() == 0) {
                                                                        sms2="否";
                                                                    }
                                                                %>
                                                                <tr>
                                                                    <td><INPUT type="checkbox" name=checkbox value="<%=vo.getId()%>"><%=i%></td>
                                                                    <td class="body-data-list"><%=vo.getIpaddress()%></td>
                                                                    <td class="body-data-list"><%=vo.getDbName()%></td>
                                                                    <td class="body-data-list"><%=vo.getTableSpace()%></td>
                                                                    <td class="body-data-list"><%=monflag%></td>
                                                                    <td class="body-data-list"><%=vo.getLimenvalue()%></td>
                                                                    <td class="body-data-list"><%=sms%></td>
                                                                    <td class="body-data-list"><%=vo.getLimenvalue1()%></td>
                                                                    <td class="body-data-list"><%=sms1%></td>
                                                                    <td class="body-data-list"><%=vo.getLimenvalue2()%></td>
                                                                    <td class="body-data-list"><%=sms2%></td>
                                                                    <td class="body-data-list"><%=vo.getBak()%></td>
                                                                    <td class="body-data-list"><a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/oratablespaconfig.do?action=showedit&id=<%=vo.getId()%>","editdiskconfig", "height=400, width= 500, top=200, left= 200")'>
                                                                        <img src="<%=rootPath%>/resource/image/editicon.gif" border="0"/></a></td>
                                                                </tr>
                                                                <%
                                                                }
                                                                %>
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
</HTML>
