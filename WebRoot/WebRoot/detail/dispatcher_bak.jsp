<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.polling.base.Node"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.topology.model.*"%>
<%@page import="com.afunms.application.dao.OraclePartsDao"%>
<%@page import="com.afunms.application.model.OracleEntity"%>
<%  
   String nodeId = "";
   String temp = null;   
   String flag = request.getParameter("flag");
   if(flag==null){flag="0";}
   temp = request.getParameter("id");
   if(temp!=null) nodeId = temp;
   Node node = null;
   TreeNodeDao treeNodeDao = new TreeNodeDao();
   TreeNode vo = (TreeNode) treeNodeDao.findByNodeTag(nodeId.substring(0,3));
   if(vo!=null&&vo.getName()!=null&&!"".equals(vo.getName())){
	   node = PollingEngine.getInstance().getNodeByCategory(vo.getName(), Integer.parseInt(nodeId.substring(3)));
   }
   if(node == null) 
   {
       out.print("没有该节点的信息!");   
       return;
   }
   String rootPath = request.getContextPath();
   String paraString = ".jsp?id=" + nodeId.substring(3)+"&flag="+flag;
   String paraStringTemp = "&id=" + nodeId.substring(3)+"&flag="+flag;
   if(node.getCategory()==4){
      if(node.getCollecttype() == 3 || node.getCollecttype()==4 ||node.getCollecttype() == 8 || node.getCollecttype()==9 ){
      	//PING、REMOTEPING、只TELNET或SSH方式检测连通性
      	response.sendRedirect(rootPath+"/monitor.do?action=hostcpu&id="+ nodeId.substring(3)+"&flag="+flag);
      }else{
      	response.sendRedirect(rootPath+"/monitor.do?action=hostping&id="+ nodeId.substring(3)+"&flag="+flag);
      }
      
   }else if(node.getCategory()<4){
      if(node.getCollecttype() == 3 || node.getCollecttype()==4 ||node.getCollecttype() == 8 || node.getCollecttype()==9 ){
      	//PING、REMOTEPING、只TELNET或SSH方式检测连通性
      	response.sendRedirect(rootPath+"/monitor.do?action=netcpu"+paraStringTemp);     
      }else{
      	response.sendRedirect(rootPath+"/topology/network/networkview"+paraString);    
      }   
      
   }
          
   else if(node.getCategory()==50)
      response.sendRedirect("ip_detail" + paraString);    
   else if(node.getCategory()==51)
      //response.sendRedirect("tomcat_jvm" + paraString); 
      response.sendRedirect(rootPath+"/tomcat.do?action=tomcat_jvm&id="+ nodeId.substring(3)+"&flag="+flag);
   else if(node.getCategory()==52)
      response.sendRedirect("mysql_detail" + paraString); 
   else if(node.getCategory()==57)
      response.sendRedirect(rootPath+"/web.do?action=detail&id="+ nodeId.substring(3)+"&flag="+flag); 
   else if(node.getCategory()==58)
      response.sendRedirect(rootPath+"/FTP.do?action=detail&id="+ nodeId.substring(3)+"&flag="+flag);
   else if(node.getCategory()==64)
      response.sendRedirect(rootPath+"/weblogic.do?action=detail&id="+ nodeId.substring(3)+"&flag="+flag); 
   else if(node.getCategory()==67)
      response.sendRedirect(rootPath+"/iis.do?action=detail&id="+ nodeId.substring(3)+"&flag="+flag); 
   else if(node.getCategory()==56)
      response.sendRedirect(rootPath+"/mail.do?action=detail&id="+ nodeId.substring(3)+"&flag="+flag);
   else if(node.getCategory()==53){
      OraclePartsDao oraclePartsDao = new OraclePartsDao();
	  OracleEntity oracleEntity = (OracleEntity)oraclePartsDao.getOracleById(Integer.parseInt(nodeId.substring(3)));
	  oraclePartsDao.close();
	  String sids="";
	  sids = oracleEntity.getDbid()+"";
      response.sendRedirect(rootPath+"/db.do?action=check&id="+ sids+"&flag="+flag+"&sid="+nodeId.substring(3));
   }
   else if(node.getCategory()==54||node.getCategory()==55||node.getCategory()==59||node.getCategory()==60)
      response.sendRedirect(rootPath+"/db.do?action=check&id="+ nodeId.substring(3)+"&flag="+flag);  
   else if(node.getCategory()==8){
   	HostNodeDao dao = new HostNodeDao();
   	HostNode host = null;
   	try{
   		host = (HostNode)dao.findByID(temp.substring(3));
   	}catch(Exception ex){
		ex.printStackTrace();
	}finally{
		dao.close();
	}
   	if(host.getSysOid().equalsIgnoreCase("1.3.6.1.4.1.14331.1.4")){
   		//显示天融信防火墙
   		response.sendRedirect(rootPath+"/topology/network/firewallview_tos"+paraString);
   	}else{
   		//显示NETSCREEN防火墙
   		response.sendRedirect(rootPath+"/topology/network/firewallview"+paraString);
   	}
   		
   }
                 
%>