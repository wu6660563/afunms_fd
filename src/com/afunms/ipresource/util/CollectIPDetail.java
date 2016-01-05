package com.afunms.ipresource.util;

import java.util.*;

import com.afunms.common.util.*;
import com.afunms.discovery.IpAddress;
import com.afunms.ipresource.model.IpResource;
import com.afunms.ipresource.dao.IpResourceDao;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Host;
import com.afunms.polling.node.IfEntity;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.dao.LinkDao;
import com.afunms.topology.model.HostNode;
import com.afunms.topology.model.Link;

public class CollectIPDetail
{
    private List<IpResource> allIps;
    private HashMap<Integer,List> fdbItems;
    
    /**
     * find dircet connect devices 
     */
    public void findDirectDevices()
    {
    	collectIps();
    	collectFdb();
    	analyse();
    	IpResourceDao dao = new IpResourceDao();
    	try{
    		dao.update(allIps);
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		dao.close();
    	}
    }
    
	private void collectIps()
    {
		Set<String> ipSet = new HashSet<String>();
		
		
		allIps = new ArrayList<IpResource>();    
        List nodeList = new ArrayList();
        HostNodeDao hnDao = new HostNodeDao();
        try{
        	nodeList = hnDao.loadNetwork(0);//�õ����������豸��IP    
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	hnDao.close();
        }
        for(int i=0;i<nodeList.size();i++)
		{			
        	HostNode node = (HostNode)nodeList.get(i);
        	//��ARP���еõ��������豸������IP-MAC��ַ��Ϣ
        	List ipList = SnmpUtil.getInstance().getIpNetToMediaTable(node.getIpAddress(),node.getCommunity());
//System.out.println("------ipList size : "+ipList.size());        	
			for(int j=0;j<ipList.size();j++)
			{
				IpAddress ipa = (IpAddress)ipList.get(j);
				if(!ipSet.contains(ipa.getIpAddress()))
				{
					ipSet.add(ipa.getIpAddress());
					IpResource ipr = new IpResource();
					ipr.setNodeId(node.getId());
					ipr.setIpAddress(ipa.getIpAddress());
					ipr.setIpLong(NetworkUtil.ip2long(ipa.getIpAddress()));
					ipr.setNode(node.getAlias());
					ipr.setIfIndex(ipa.getIfIndex());
					ipr.setMac(ipa.getPhysAddress());
				    Host host = (Host)PollingEngine.getInstance().getNodeByID(node.getId());
				    IfEntity ifObj = host.getIfEntityByIndex(ipa.getIfIndex());
                    ipr.setIfDescr(ifObj.getDescr());
//System.out.println("---ip : "+ipr.getIpAddress()+" node : "+ipr.getNode()+" ifIndex : "+ipr.getIfIndex()+" MAC : "+ipr.getMac());                    
					allIps.add(ipr);
				}
			}
		}
    }
	
	private void collectFdb()
    {
		SysLogger.info("########��ʼ��ִ��FDB�ɼ�����#########");
		fdbItems = new HashMap<Integer,List>();
		
		List<HostNode> nodeList = new ArrayList();
		HostNodeDao hnDao = new HostNodeDao();
		try{
			nodeList = hnDao.loadSwitch(); 
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			hnDao.close();
		}
		/*test begin*/
		/*test end*/
		
		for(HostNode node:nodeList)
		{
			List fdbList = SnmpUtil.getInstance().getFdbTable(node.getIpAddress(),node.getCommunity());
			if(fdbList!=null && fdbList.size()!=0)
			   fdbItems.put(node.getId(), fdbList);			
		}		
    }
	
	private void analyse()
	{
    	LinkDao dao = new LinkDao();
    	List<Link> linkList = new ArrayList();
    	try{
    		linkList = dao.loadAll();
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		dao.close();
    	}

        boolean hasFind = false;
        while(true)
        {
        	hasFind = false;
        	for(IpResource ipr:allIps)
            {
    		    for(int j=0;j<linkList.size();j++)
    		    {
    			    Link link = (Link)linkList.get(j);
//System.out.println("NodeId : "+ipr.getNodeId()+" Link --- getStartId : "+link.getStartId()+" getIfIndex() :"+ipr.getIfIndex()+" sartIndex : "+link.getStartIndex()+" endId : "+link.getEndId()+" endIndex : "+link.getEndIndex());    			    
    			    if(ipr.getNodeId()==link.getStartId()&& ipr.getIfIndex().equals(link.getStartIndex()))    			   
    			    {    			    	
    			    	String index = findMacInFdbTable(link.getEndId(),ipr.getMac());
    				    if(index!=null)
    				    {
    				    	ipr.setNodeId(link.getEndId());    				    	
    				    	ipr.setIfIndex(index);
    					    Host host = (Host)PollingEngine.getInstance().getNodeByID(ipr.getNodeId());
    					    IfEntity ifObj = host.getIfEntityByIndex(index);
    				    	
    				    	ipr.setNode(host.getAlias());
    				    	ipr.setIfDescr(ifObj.getDescr());
    				    	hasFind = true;
    				    	break;
    				    }    				    
    			    }
    			    else if(ipr.getNodeId() ==link.getEndId() && ipr.getIfIndex().equals(link.getEndIndex()))
    			    {
    			    	String index = findMacInFdbTable(link.getStartId(),ipr.getMac());
    				    if(index!=null)
    				    {
    				    	ipr.setNodeId(link.getStartId());    				    	
    				    	ipr.setIfIndex(index);
    					    Host host = (Host)PollingEngine.getInstance().getNodeByID(ipr.getNodeId());
    					    IfEntity ifObj = host.getIfEntityByIndex(index);
    				    	
    				    	ipr.setNode(host.getAlias());
    				    	ipr.setIfDescr(ifObj.getDescr());
    				    	hasFind = true;
    				    	break;
    				    }    				    
    			    }
    		    }    		    
    		}
            if(!hasFind) break;
        }
	}
	
    private String findMacInFdbTable(int hostId,String mac)
    {    	
    	List fdbTable = fdbItems.get(hostId);
        if(fdbTable==null) return null;
        
        String index = null;
        for(int i=0;i<fdbTable.size();i++)
        {
            String[] item = (String[])fdbTable.get(i);	
            if(mac.equals(item[1]))
            {
            	index = item[0];
            	break;
            }
        }
    	return index;
    }
}