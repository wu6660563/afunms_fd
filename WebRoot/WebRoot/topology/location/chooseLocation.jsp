<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.topology.model.RemotePingHost"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.topology.model.IpDistrictMatch"%>
<%@page import="com.afunms.topology.model.IpDistrictMatchConfig"%>
<%@page import="com.afunms.config.model.DistrictConfig"%>
<%@page import="com.afunms.config.dao.DistrictDao"%>
<%@page import="com.afunms.gather.model.GatherNode"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.location.model.FvsdReportLocationNode"%>
<%@page import="com.afunms.location.service.FvsdReportLocationService"%>
<%@page import="com.afunms.location.model.FvsdReportLocation"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>

<%
    String rootPath = request.getContextPath();
    String menuTable = (String)request.getAttribute("menuTable");
    List<FvsdReportLocation> list = (List<FvsdReportLocation>)request.getAttribute("list");
%>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
        <link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
        <script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
        <script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
        
        <link rel="stylesheet" type="text/css"  href="<%=rootPath%>/application/environment/resource/ext3.1/resources/css/ext-all.css" />
        <script type="text/javascript"  src="<%=rootPath%>/application/environment/resource/ext3.1/adapter/ext/ext-base.js"></script>
        <script type="text/javascript" src="<%=rootPath%>/application/environment/resource/ext3.1/ext-all.js"></script>
        <script type="text/javascript" src="<%=rootPath%>/application/environment/resource/ext3.1/ext-all-debug.js"></script>
        
        <script type="text/javascript">
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
        <script>
            function update() {
                var radioes = document.getElementsByName("radio");
                var length = 0;
                var isChoose = false;
                var fvsdLocationId = "";
                if(radioes) {
                    length = radioes.length;
                    var i = 0;
                    while(i < length) {
                        var radio = radioes[i];
                        if (radio.checked) {
                            isChoose = true;
                            fvsdLocationId = radio.value;
                        }
                        i++;
                    }
                }
                if (isChoose) {
                    window.opener.update(fvsdLocationId);
                    window.close();
                } else {
                    alert("请选择区域！！！");
                }
            }
        </script>
    </head>
    <body id="body" class="body" onload="initmenu();">
        <form id="mainForm" method="post" name="mainForm">
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
                                                        <td class="content-title"> 资源 >> 区域列表 </td>
                                                        <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <table id="content-body" class="content-body">
                                                    <tr align="right" >
                                                        <td class="body-data-title" colspan="6">
                                                            <table >
                                                                <tr>
                                                                    <td style="text-align: right;" class="body-data-title"><input type="button" value="确定" onclick="update()">&nbsp;&nbsp;&nbsp;</td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td align="center" class="body-data-title"><INPUT type="radio" id="radio" name="checkall">序号</td>
                                                        <td align="center" class="body-data-title">区域</td>
                                                    </tr>
                                                    <%
                                                        if(list!=null&& list.size()>0){
                                                            for(FvsdReportLocation fvsdReportLocation : list){
                                                                    String id = fvsdReportLocation.getId();
                                                                    String descr = fvsdReportLocation.getDescr();
                                                                %>
                                                                <tr <%=onmouseoverstyle%>>
                                                                    <td align="center" class="body-data-list"><INPUT type="radio" value="<%=id%>" name="radio" onclick="javascript:chkall()"></td>
                                                                    <td align="center" class="body-data-list"><%=descr%></td>
                                                                </tr>
                                                                <% 
                                                            }
                                                            
                                                        }
                                                     %>
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
