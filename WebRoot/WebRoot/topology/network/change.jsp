<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%
    String customXml = request.getParameter("customview");
    String fullscreen = request.getParameter("fullscreen");
    if ("network.jsp".equals(customXml)) {
        session.setAttribute(SessionConstant.CURRENT_TOPO_VIEW,customXml);
        response.sendRedirect(request.getContextPath() + "/topology/network/showMap.jsp?fullscreen=" + fullscreen);    
    } else {
        session.setAttribute(SessionConstant.CURRENT_SUBMAP_VIEW,customXml);
        response.sendRedirect(request.getContextPath() + "/topology/submap/showMap.jsp?fullscreen=" + fullscreen);    
    }
%>