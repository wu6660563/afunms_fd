<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.node.model.NodeDomain"%>
<%@page import="com.afunms.node.model.Domain"%>
<%@page import="com.afunms.node.service.NodeDomainService"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>

<%
    String rootPath = request.getContextPath();
    String menuTable = (String)request.getAttribute("menuTable");
    List<NodeDTO> list = (List<NodeDTO>)request.getAttribute("list");
    Hashtable<String, NodeDomain> nodeDomainHashtable = (Hashtable<String, NodeDomain>) request.getAttribute("nodeDomainHashtable");
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
        <script type="text/javascript" src="<%=rootPath%>/js/dwr/engine.js"></script> 
        <script type="text/javascript" src="<%=rootPath%>/js/dwr/util.js"></script> 
        <script type="text/javascript" src="<%=rootPath%>/dwr/interface/NodeDomainService.js"></script>
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
            
            function NodeDomain() {
                this.id = null;
                this.nodeId = null;
                this.nodeType = null;
                this.nodeSubtype = null;
                this.domain = null;
                this.domainDescr = null;
            };
        
            function chooseLocation(){
                window.open("<%=rootPath%>/domain.do?action=choose", '_bank', "toolbar=no,height=400, width= 500, top=200, left= 200,scrollbars=no"+"screenX=0,screenY=0")
            }
    
            function update(domainId) {
                var checkes = document.getElementsByName("checkbox");
                var length = 0;
                var checked = false;
                var nodeDomainArray = new Array();
                if (checkes) {
                    length = checkes.length;
                    var i = 0;
                    while(i < length) {
                        var check = checkes[i];
                        if(check.checked) {
                            checked = true;
                            var id = check.value;
                            var ids = id.split(":");
                            var nodeDomainId = ids[0];
                            var nodeid = ids[1];
                            var type = ids[2];
                            var subtype = ids[3];
                            var nodeDomain = new NodeDomain();
                            nodeDomain.id = nodeDomainId;
                            nodeDomain.nodeId = nodeid;
                            nodeDomain.nodeType = type;
                            nodeDomain.nodeSubtype = subtype;
                            nodeDomain.domain = domainId;
                            nodeDomainArray.push(nodeDomain);
                        }
                        i++;
                    }
                }
                if (checked) {
                    NodeDomainService.update(domainId, nodeDomainArray, function(data) {
                        var nodeDomainArray2 = data;
                        var length2 = nodeDomainArray2.length;
                        var j = 0;
                        while(j < length2) {
                            var nodeDomain = nodeDomainArray2[j];
                            j++;
                            var nodeid = nodeDomain.nodeId;
                            var type = nodeDomain.nodeType;
                            var subtype = nodeDomain.nodeSubtype;
                            var nodeDomainId = nodeDomain.id;
                            var descrCheckBox = document.getElementById(nodeid + ":" + type + ":" + subtype + "_id");
                            descrCheckBox.value = nodeDomainId + ":" + nodeid + ":" + type + ":" + subtype;
                            var descrTD = document.getElementById(nodeid + ":" + type + ":" + subtype + "_descr");
                            descrTD.innerHTML = nodeDomain.domainDescr;
                        }
                    });
                } else {
                    alert("未选择设备！")
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
                                                        <td class="content-title"> 资源 >> 设备所属列表 </td>
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
                                                                    <td style="text-align: right;" class="body-data-title"><input type="button" value="设置区域" onclick="chooseLocation()">&nbsp;&nbsp;&nbsp;</td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td align="center" class="body-data-title"><INPUT type="checkbox" id="checkall" name="checkall" onclick="javascript:chkall()">序号</td>
                                                        <td align="center" class="body-data-title">设备名称</td>
                                                        <td align="center" class="body-data-title">设备IP</td>
                                                        <td align="center" class="body-data-title">设备类型</td>
                                                        <td align="center" class="body-data-title">设备子类型</td>
                                                        <td align="center" class="body-data-title">设备所属区域</td>
                                                    </tr>
                                                    <%
                                                        if(list!=null&& list.size()>0){
                                                            for(NodeDTO nodeDTO : list){
                                                                String nodeid = nodeDTO.getNodeid();
                                                                String name = nodeDTO.getName();
                                                                String ipaddress = nodeDTO.getIpaddress();
                                                                String type = nodeDTO.getType();
                                                                String subtype = nodeDTO.getSubtype();
                                                                String key = nodeid + ":" + type + ":" + subtype;
                                                                System.out.println(key);
                                                                NodeDomain nodeDomain = nodeDomainHashtable.get(key);
                                                                String domainId = "-1";
                                                                String descr = "";
                                                                String id = "-1";
                                                                if (nodeDomain != null) {
                                                                    domainId = nodeDomain.getDomain();
                                                                    Domain domain = NodeDomainService.getDomain(domainId);
                                                                    descr = domain.getDescr();
                                                                    id = "" + nodeDomain.getId();
                                                                }
                                                                id += ":" + nodeid + ":" + type + ":" + subtype;
                                                                %>
                                                                <tr <%=onmouseoverstyle%>>
                                                                    <td align="center" class="body-data-list"><INPUT type="checkbox" value="<%=id%>" id="<%=key%>_id" name="checkbox"></td>
                                                                    <td align="center" class="body-data-list"><%=name%></td>
                                                                    <td align="center" class="body-data-list"><%=ipaddress%></td>
                                                                    <td align="center" class="body-data-list"><%=type%></td>
                                                                    <td align="center" class="body-data-list"><%=subtype%></td>
                                                                    <td align="center" class="body-data-list" id="<%=key%>_descr"><%=descr%></td>
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
