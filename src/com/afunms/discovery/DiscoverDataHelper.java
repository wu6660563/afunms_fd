/**
 * <p>Description:������֮��,�����ݽ��д���</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-13
 */

package com.afunms.discovery;

import java.util.*;
import java.sql.ResultSet;

import com.afunms.topology.dao.*;
import com.afunms.topology.model.*;
import com.afunms.common.util.*;
import com.afunms.topology.util.NodeHelper; 
import com.afunms.topology.util.XmlOperator;


public class DiscoverDataHelper
{
   public void memory2DB()
   {
	   
	   
	   //������֮��������ԭ�������˷��ֱ�,��ֹ���ֵ�һ���ʱ������������ֹ���ֳ������ʧ����
	   //DiscoverConfigDao cleandao = new DiscoverConfigDao();
	   //cleandao.cleanTOPOTable() new DiscoverCompleteDao();
	   DiscoverCompleteDao nodeDao = new DiscoverCompleteDao();
	   if(DiscoverEngine.getInstance().getDiscovermodel() == 1){
		   //���䷢��
		   //�õ����ֵ���ʷ�豸,�����Ѿ�ɾ��������Ѿ����ֵ��豸
		   List formerNodeList = DiscoverEngine.getInstance().getFormerNodeList();
		   if(formerNodeList != null && formerNodeList.size()>0){
			   for(int k=0;k<formerNodeList.size();k++){
				   Host formernode = (Host)formerNodeList.get(k);
				   if(formernode.getDiscoverstatus()>-1){
					   formernode.setDiscoverstatus(formernode.getStatus()+1);
				   }else{
					   formernode.setDiscoverstatus(1);
				   }
				   SysLogger.info("��ǰ���û���ֵ��豸"+formernode.getIpAddress());
				   //������Ҫ�������ýڵ��IDֵ
				   formernode.setId(KeyGenerator.getInstance().getHostKey());
				   DiscoverEngine.getInstance().getHostList().add(formernode);
			   }
		   }
		   if(formerNodeList != null && formerNodeList.size()>0){
			   for(int k=0;k<formerNodeList.size();k++){
				   Host formernode = (Host)formerNodeList.get(k);
				   //�������ӹ�ϵ
				   List formerNodeLinkList = DiscoverEngine.getInstance().getFormerNodeLinkList();
				   if(formerNodeLinkList != null && formerNodeLinkList.size()>0){
					   for(int m=0;m<formerNodeLinkList.size();m++){
						   com.afunms.topology.model.Link modellink = (com.afunms.topology.model.Link)formerNodeLinkList.get(m);

						   //Link link = (Link)formerNodeLinkList.get(m);
						   String ip = "";
						   int isstartip = 0;
						   if(modellink.getStartIp().equalsIgnoreCase(formernode.getIpAddress())){
							   ip = modellink.getEndIp();
						   }else if (modellink.getEndIp().equalsIgnoreCase(formernode.getIpAddress())){
							   ip = modellink.getStartIp();
							   isstartip = 1; 
						   }
						   if(ip != null && ip.length()>0){
							   //ԭ���������ӹ�ϵ
							   //�ж϶Զ˽ڵ��Ƿ����
							   Host host = DiscoverEngine.getInstance().getHostByIP(ip);
							   Host formerhost = DiscoverEngine.getInstance().getHostByIP(formernode.getIpAddress());
							   if(host != null){
								   //�Զ����Ѿ����ֵĽڵ��б���,���ԭ�������ӹ�ϵ��ӽ�ȥ
								   Link link = new Link();
								   link.setStartIp(formerhost.getIpAddress());
								   link.setStartAlias(formerhost.getAlias());
								   link.setStartDescr(modellink.getStartDescr());
								   link.setStartIndex(modellink.getStartIndex());
								   link.setStartPhysAddress(host.getBridgeAddress());
								   link.setStartPort(modellink.getStartPort());
								   
								   link.setEndIp(modellink.getEndIp());
								   link.setEndAlias(host.getAlias());
								   link.setEndDescr(modellink.getEndDescr());
								   link.setEndIndex(modellink.getEndIndex());
								   link.setEndPhysAddress(host.getBridgeAddress());
								   link.setEndPort(modellink.getEndPort());
								   
								   
								   
								   if(isstartip == 0){
									   link.setEndId(host.getId());
									   link.setEndIp(host.getIpAddress());
									   link.setEndAlias(host.getAlias());
									   link.setEndDescr(modellink.getEndDescr());
									   link.setEndIndex(modellink.getEndIndex());
									   link.setEndPhysAddress(host.getBridgeAddress());
									   link.setEndPort(modellink.getEndPort());
									   
									   link.setStartId(formerhost.getId());
									   link.setStartIp(modellink.getStartIp());
									   link.setStartAlias(modellink.getStartAlias());
									   link.setStartDescr(modellink.getStartDescr());
									   link.setStartIndex(modellink.getStartIndex());
									   link.setStartPhysAddress(formerhost.getBridgeAddress());
									   link.setStartPort(modellink.getStartPort());
									   
									   link.setFindtype(modellink.getFindtype());
									   link.setLinktype(modellink.getLinktype());
									   link.setAssistant(modellink.getAssistant());
									   
									   
								   }else{
									   link.setStartId(host.getId());
									   link.setStartIp(host.getIpAddress());
									   link.setStartAlias(modellink.getEndAlias());
									   link.setStartDescr(modellink.getEndDescr());
									   link.setStartIndex(modellink.getEndIndex());
									   link.setStartPhysAddress(host.getBridgeAddress());
									   link.setStartPort(modellink.getEndPort());
									   
									   link.setEndId(formerhost.getId());
									   link.setEndIp(formerhost.getIpAddress());
									   link.setEndAlias(formerhost.getAlias());
									   link.setEndDescr(modellink.getStartDescr());
									   link.setEndIndex(modellink.getStartIndex());
									   link.setEndPhysAddress(formerhost.getBridgeAddress());
									   link.setEndPort(modellink.getStartPort());

									   link.setFindtype(modellink.getFindtype());
									   link.setLinktype(modellink.getLinktype());
									   link.setAssistant(modellink.getAssistant());
								   }
								   DiscoverEngine.getInstance().getLinkList().add(link);
							   }else{
								   //�Զ˲����Ѿ����ֵĽڵ��б���,��Ҫ�ж��Ƿ��ڵ�ǰ��û���ֵ��б���,����,��ֱ�����
							   }
						   }
					   }
				   }
			   }
		   }
	   }
	   
	   //�ô�ȡ��ɾ�������豸,�ڷ���ʱ�������ʱֹͣ,����ɾ��ԭ���豸.�����ڷ���ǰ,��Ҫ��������豸
	   //nodeDao.clear();	      	  
	   nodeDao.addID();
	   nodeDao.addLinkData(DiscoverEngine.getInstance().getLinkList());
	   //nodeDao.addHostData(DiscoverEngine.getInstance().getHostList());
	   nodeDao.addInterfaceData(DiscoverEngine.getInstance().getHostList());
	   nodeDao.addSubNetData(DiscoverEngine.getInstance().getSubNetList());
	   nodeDao.addMonitor(DiscoverEngine.getInstance().getHostList());
	   nodeDao.close();
	   
       DiscoverStatDao dao = new DiscoverStatDao();
       DiscoverStat vo = new DiscoverStat();
       vo.setStartTime(DiscoverMonitor.getInstance().getStartTime());
       vo.setEndTime(DiscoverMonitor.getInstance().getEndTime());
       vo.setElapseTime(SysUtil.diffTwoTime(vo.getStartTime(),vo.getEndTime()));     
       vo.setHostTotal(DiscoverEngine.getInstance().getHostList().size());
       vo.setSubnetTotal(DiscoverEngine.getInstance().getSubNetList().size());       
       dao.save(vo);
   }
      
//   public void memoryLinks2DB()
//   {
//	   DiscoverCompleteDao nodeDao = new DiscoverCompleteDao();
//	   nodeDao.addLinkData(DiscoverEngine.getInstance().getLinkList());
//   }
      
   public void DB2NetworkXml()
   {	
	   XmlOperator xmlOpr = new XmlOperator();
	   xmlOpr.setFile("network.jsp");
	   xmlOpr.init4createXml();
	
       DBManager db = null;
       ResultSet rs = null;
       int widthSpace = 30;
       int heightSpace = 60;
       int margin = 80; 
      int r=210;
	   try
	   {	
		  db = new DBManager();
//          String subSql = "from topo_host_node where (category<4 or category = 7) group by layer";
//          rs = db.executeQuery("select layer,count(layer) total " + subSql + " order by total desc");
//          int maxWidth = 0; //�����
//          if(rs.next())
//          	 maxWidth = rs.getInt(2) * widthSpace;
//          
//          int[] arrLayer = null; 
//          rs = db.executeQuery("select count(*) from (select layer " + subSql + ") distinctiptable");
//          if(rs.next())
//          	 arrLayer = new int[rs.getInt(1)];
//          
//          rs = db.executeQuery("select layer,count(layer) total " + subSql + " order by layer");
//          while(rs.next()){
//        	  try{
//        		  if(rs.getInt(1) > 0)
//        		  arrLayer[rs.getInt(1)-1] = rs.getInt(2);
//        	  }catch(Exception ex){
//        		  ex.printStackTrace();
//        	  }
//          }
          //��ѯ��ÿ����·
          rs = db.executeQuery("select n.ip_address ip,l.end_ip from topo_host_node n,topo_network_link l where n.ip_address=l.start_ip and l.end_ip in(select n1.ip_address from  topo_host_node n1 where  n1.layer=n.layer+1) and l.linktype != -1  union select n.ip_address,l.start_ip from topo_host_node n,topo_network_link l where n.ip_address=l.end_ip and l.start_ip in(select n1.ip_address from  topo_host_node n1 where  n1.layer=n.layer+1) and l.linktype != -1");
          HashMap<String, List<String>> t = new HashMap<String, List<String>>();
  		 List<String> ipList = new ArrayList<String>();
  		 int j=0;
  		boolean flag = true;
  		String tempIp = "";
          while(rs.next()){
                j++;
        		String ip = rs.getString(1);
        		String ipValue = rs.getString(2);
        		
        		if (ip.equals(tempIp)) {
        			ipList.add(ipValue);
        			if (rs.getRow()==j) {
        				t.put(ip, ipList);
					}
        		} else {
        			List<String> existList = t.get(ip);
        			if (existList != null && existList.size() > 0) {

        				existList.add(ipValue);
        			
            				t.put(ip, existList);
    					
        			} else {
        				if (flag) {
        					ipList.add(ipValue);
        					
        					flag = false;
        				}else {
        					t.put(tempIp, ipList);
            				ipList = new ArrayList<String>();
            				ipList.add(ipValue);	
						}
        				t.put(ip, ipList);
        				
        			}
        			
        		}
        		tempIp = ip;
          }
          	 
          int startX =0;
          int startY=0;
          int noLinkX=0;
          int noLinkY=0;
          int tempLayer=1;
          HashMap<String, LinkPoint> map=new HashMap<String, LinkPoint>();
          rs = db.executeQuery("select * from topo_host_node where (category<4 or category = 7) order by layer,super_node,id");
	      
          while(rs.next())
	      {
	    	  String rsId = rs.getString("id");
	          String rsIpAddress = rs.getString("ip_address");
	          String rsAlias = rs.getString("alias");
	          int rsCategory = rs.getInt("category");
	          int rsLayer = rs.getInt("layer");
	          int discoverstatus = rs.getInt("discoverstatus");
	          
//	          if(layer == rsLayer&&rsLayer!=1) 
//	          {
//	        	  layer = rsLayer;
//	        	  startX = (int)((Math.sin(2*Math.PI*i/arrLayer[layer-1]))*r+startX);
//	        	  startY = (int)((Math.cos(2*Math.PI*i/arrLayer[layer-1]))*r+startY);
//			      i++;
//	          }else if (layer!= rsLayer&&rsLayer!=1) {
//	        	  layer = rsLayer;
//	        	  i=1;
//	        	  startX = (int)((Math.sin(2*Math.PI*i/arrLayer[layer-1]))*r+startX);
//	        	  startY = (int)((Math.cos(2*Math.PI*i/arrLayer[layer-1]))*r+startY);
//			     i++;
	          if (rsLayer!=1) {
	        	  List<String> list = t.get(rsIpAddress);
	        	   LinkPoint point1=map.get(rsIpAddress);
	        	   if (point1!=null) {
	        		   startX=point1.getX();
						startY=point1.getY();
				   }else {
					   if (tempLayer==rsLayer) {
						   noLinkX+=50;
					}else {
						noLinkX=0;
					}
					   
					   
					startX=noLinkX;
					startY=rsLayer*100+noLinkY;
				   }
	        	   int layerR=r-rsLayer*30;
	        	   if (rsLayer>5) {
	        		   layerR=50;
				   }
					
					if (list!=null&&list.size()>0) {
						
						for (int k = 1; k <=list.size(); k++) {
							String linkIp=list.get(k-1);
							
							int x = (int)((Math.sin(2*Math.PI*k/list.size()))*layerR+startX);
				        	int y = (int)((Math.cos(2*Math.PI*k/list.size()))*layerR+startY);
				        	LinkPoint point=new LinkPoint(x,y);
				        	map.put(linkIp, point);
				        	
						}
					}
					tempLayer=rsLayer;
					
			}else if (rsLayer==1) {
				startX+=420;
				startY=300;
				List<String> list = t.get(rsIpAddress);
				if (list!=null&&list.size()>0) {
					for (int k = 1; k <=list.size(); k++) {
						String linkIp=list.get(k-1);
						int x = (int)((Math.sin(2*Math.PI*k/list.size()))*r+startX);
			        	int y = (int)((Math.cos(2*Math.PI*k/list.size()))*r+startY);
			        	LinkPoint point=new LinkPoint(x,y);
			        	map.put(linkIp, point);
					}
				}
				 LinkPoint point=new LinkPoint(startX,startY);
		          map.put(rsIpAddress, point);
		         // noLinkX=startX;
		          noLinkY=startY;
			}
	          String x = String.valueOf(startX); 
	          String y = String.valueOf(startY);
			 
	         
//	    	  if(layer != rsLayer)
//	      	  {	
//	      	  	  layer = rsLayer;
//	      	  	  startX = maxWidth/2 - (arrLayer[layer-1] / 2) * widthSpace + margin;
//	      	  	  col = 0;
//	      	  }
//		      String x = String.valueOf(startX + col * widthSpace); 
//		      String y = String.valueOf(layer*heightSpace + margin);
//		      col++;
		      if(discoverstatus > 0){
		    	  xmlOpr.addNode("net"+rsId,rsCategory,NodeHelper.getLostImage(rsCategory),rsIpAddress,rsAlias, x, y);//yangjun xiugai 
		      }else{
		    	  xmlOpr.addNode("net"+rsId,rsCategory,NodeHelper.getTopoImage(rsCategory),rsIpAddress,rsAlias, x, y);//yangjun xiugai 
		      }
	    	  		      
	      }//end_while
	      
//		  HostNodeDao dao = new HostNodeDao();	  
//		  List list = dao.loadServer();	
//	   	  widthSpace = 100;
//		  int hightSpace = 120;
//		  int marginX = 20;
//		  int marginY = 10;
//		  int perRow = 8;
//		  int row = -1; 
//
//		  for(int i=0; i<list.size(); i++)
//		  {
//			 HostNode host = (HostNode)list.get(i);							
//			 
//			 String nodeId = String.valueOf(host.getId());
//			 String img = NodeHelper.getServerTopoImage(host.getSysOid());
//			 if( i % perRow == 0 ) row++;
//			 String x = String.valueOf(( i % perRow ) * widthSpace + marginX); 
//			 String y = String.valueOf(row * hightSpace + marginY);		 
//			 xmlOpr.addNode(nodeId,4,img,host.getIpAddress(),host.getAlias(), x, y);		 
//		  }	
	      
		  //lines���
    	  rs = db.executeQuery("select * from topo_network_link where assistant=0 and linktype != -1 order by id");
          while(rs.next())
        	 xmlOpr.addLine(rs.getString("link_name"),rs.getString("id"),"net"+rs.getString("start_id"), "net"+rs.getString("end_id"));//yangjun xiugai
    	  rs = db.executeQuery("select * from topo_network_link where assistant=1 and linktype != -1 order by id");
          while(rs.next())
        	 xmlOpr.addAssistantLine(rs.getString("link_name"),rs.getString("id"),"net"+rs.getString("start_id"), "net"+rs.getString("end_id"));//yangjun xiugai                    
          xmlOpr.createXml();                  
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace();
		   SysLogger.error("DiscoverDataHelper.DB2NetworkXml()", e);
	   }    
	   finally
	   {
		   db.close();
	   }
   } 
   
   public void DB2NetworkVlanXml()
   {	
	   XmlOperator xmlOpr = new XmlOperator();
	   xmlOpr.setFile("networkvlan.jsp");
	   xmlOpr.init4createXml();
	
       DBManager db = null;
       ResultSet rs = null;
       int widthSpace = 30;
       int heightSpace = 60;
       int margin = 80; 
      
	   try
	   {	
		  db = new DBManager();
          String subSql = "from topo_host_node where (category<4 or category = 7) group by layer";
          rs = db.executeQuery("select layer,count(layer) total " + subSql + " order by total desc");
          int maxWidth = 0; //�����
          if(rs.next())
          	 maxWidth = rs.getInt(2) * widthSpace;
          
          int[] arrLayer = null; 
          rs = db.executeQuery("select count(*) from (select layer " + subSql + ") distinctiptable");
          if(rs.next())
          	 arrLayer = new int[rs.getInt(1)];
          
          rs = db.executeQuery("select layer,count(layer) total " + subSql + " order by layer");
          while(rs.next()){
        	  try{
        		  arrLayer[rs.getInt(1)-1] = rs.getInt(2);
        	  }catch(Exception ex){
        		  ex.printStackTrace();
        	  }
          }
          	 
	  
          int layer = 0;
          int col = 0;
          int startX = 10; 
          rs = db.executeQuery("select * from topo_host_node where (category<4 or category = 7) order by layer,super_node,id");
	      while(rs.next())
	      {
	    	  String rsId = rs.getString("id");
	          String rsIpAddress = rs.getString("ip_address");
	          String rsAlias = rs.getString("alias");
	          int rsCategory = rs.getInt("category");
	          int rsLayer = rs.getInt("layer");
	          
	    	  if(layer != rsLayer)
	      	  {	
	      	  	  layer = rsLayer;
	      	  	  startX = maxWidth/2 - (arrLayer[layer-1] / 2) * widthSpace + margin;
	      	  	  col = 0;
	      	  }
		      String x = String.valueOf(startX + col * widthSpace); 
		      String y = String.valueOf(layer*heightSpace + margin);
		      col++;

	    	  xmlOpr.addNode("net"+rsId,rsCategory,NodeHelper.getTopoImage(rsCategory),rsIpAddress,rsAlias, x, y);//yangjun xiugai 		      
	      }//end_while
	      
		  //lines���
	      rs = null;
    	  rs = db.executeQuery("select * from topo_network_link where assistant=0 and linktype = -1 order by id");
          while(rs.next())
        	 xmlOpr.addLine(rs.getString("link_name"),rs.getString("id"),rs.getString("start_id"), rs.getString("end_id"));
          rs = null;
    	  rs = db.executeQuery("select * from topo_network_link where assistant=1 and linktype = -1 order by id");
          while(rs.next())
        	 xmlOpr.addAssistantLine(rs.getString("link_name"),rs.getString("id"),rs.getString("start_id"), rs.getString("end_id"));   
                         
          xmlOpr.createXml();
          
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace();
		   SysLogger.error("DiscoverDataHelper.DB2NetworkVlanXml()", e);
	   }    
	   finally
	   {
		   db.close();
	   }
   } 
	  
   public void DB2ServerXml()
   {	
   	  int widthSpace = 100;
	  int hightSpace = 120;
	  int marginX = 20;
	  int marginY = 10;
	  int perRow = 8;
	  int row = -1; 
	  
	  XmlOperator xmlOpr = new XmlOperator();
	  xmlOpr.setFile("server.jsp");
	  xmlOpr.init4createXml();
	  
	  HostNodeDao dao = new HostNodeDao();	  
	  List list = dao.loadServer();		  
	  for(int i=0; i<list.size(); i++)
	  {
		 HostNode host = (HostNode)list.get(i);							
		 
		 String nodeId = String.valueOf(host.getId());
		 String img = NodeHelper.getServerTopoImage(host.getSysOid());
		 if( i % perRow == 0 ) row++;
		 String x = String.valueOf(( i % perRow ) * widthSpace + marginX); 
		 String y = String.valueOf(row * hightSpace + marginY);		 
		 xmlOpr.addNode("net"+nodeId,4,img,host.getIpAddress(),host.getAlias(), x, y);//yangjun xiugai 				 
	  }	
	  xmlOpr.createXml();
   }

   public void createLinkXml(){
       DBManager db = null;
       ResultSet rs = null;
       db = new DBManager();
       try {
            ManageXmlDao mdao = new ManageXmlDao();
            List<ManageXml> list = mdao.loadAll();
            if(list!=null&&list.size()>0){
                for(int i = 0;i < list.size(); i++){
                    ManageXml manageXml = list.get(i);
                    XmlOperator xopr = new XmlOperator();
                    xopr.setFile(manageXml.getXmlName());
                    xopr.init4updateXml();
                    rs = db.executeQuery("select * from topo_network_link where assistant=0 and linktype != -1 order by id");
                    while(rs.next()){
                        if(xopr.isNodeExist("net"+rs.getString("start_id"))&&xopr.isNodeExist("net"+rs.getString("end_id"))&&!xopr.isLinkExist(rs.getString("id"))){
                            xopr.addLine(rs.getString("link_name"),rs.getString("id"),"net"+rs.getString("start_id"), "net"+rs.getString("end_id"));    
                        }
                    }
                    rs = db.executeQuery("select * from topo_network_link where assistant=1 and linktype != -1 order by id");
                    while(rs.next()){
                        if(xopr.isNodeExist("net"+rs.getString("start_id"))&&xopr.isNodeExist("net"+rs.getString("end_id"))&&!xopr.isAssLinkExist(rs.getString("id"))){
                            xopr.addAssistantLine(rs.getString("link_name"),rs.getString("id"),"net"+rs.getString("start_id"), "net"+rs.getString("end_id"));
                        }
                    }
                    xopr.writeXml();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
   }
}