<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.topology.dao.ManageXmlDao"%>
<%@page import="com.afunms.topology.model.ManageXml"%>
<%@page import="com.afunms.system.model.User"%>
<%
  String rootPath = request.getContextPath();  
  
  String menu = request.getParameter("menu");
  if(menu!=null){
     session.setAttribute(SessionConstant.CURRENT_MENU,menu); 
  }
  User current_user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
  String bids[] = current_user.getBusinessids().split(",");
  String xml_current="";
  int tt=0;
  ManageXmlDao dao = new ManageXmlDao();
	List list = dao.loadAll();
	for(int i=0; i<list.size(); i++)
	{
		ManageXml vo = (ManageXml)list.get(i);
		int tag = 0;
		//System.out.println("vo.getBid()======"+vo.getBid());
		if(bids!=null&&bids.length>0){
		    for(int j=0;j<bids.length;j++){
		        if(vo.getBid()!=null&&!"".equals(vo.getBid())&&!"".equals(bids[j])&&vo.getBid().indexOf(bids[j])!=-1){
		            tag++;
		        }
		    }
		}
		//System.out.println("tag======"+tag);
		xml_current=vo.getXmlName();
		if(tag>0){
		    ++tt;
		    if("network.jsp".equals(xml_current)){
		        session.setAttribute(SessionConstant.CURRENT_TOPO_VIEW,xml_current); 
		        break;
		    } else {
		        response.sendRedirect(request.getContextPath() + "/topology/submap/index.jsp?submapXml="+xml_current);
		    }
		} 
	} 
	//System.out.println(tt);
	if(tt==0){
	    response.sendRedirect(request.getContextPath() + "/topology/network/blank.jsp");
	} else {
	    session.setAttribute("fatherXML","network.jsp");    //yangjun add
	}
	//System.out.println("xml_current======"+xml_current);
  
%>
<html>
<head>
<title>ÍøÂçÉè±¸ÍØÆËÍ¼</title>
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
</head>  
<frameset rows="30,*" cols="*" frameborder="NO" border="0" framespacing="0" noresize>
  <frame name="topFrame" scrolling="NO" noresize src="top.jsp?xml=<%=xml_current%>" marginheight="0" marginwidth="0">
  <frameset name=search cols="0,*" frameborder="NO" border="0" framespacing="0" rows="*">
    <frame name="leftFrame" src="tree.jsp?treeflag=0&fromtopo=true">
    <frame id="mainFrame" name="mainFrame" src="network.jsp">
  </frameset>
</frameset>
</html>
