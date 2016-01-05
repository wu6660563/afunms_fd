/**
 * <p>Description:DiscoverManager</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-12
 */

package com.afunms.application.manage;

import java.util.*;

import com.afunms.common.base.*;
import com.afunms.common.util.SnmpService;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpUtil;
import com.afunms.common.util.SysConfigFileUtil;
import com.afunms.common.util.SysLogger;
import com.afunms.application.model.IPNode;
import com.afunms.application.dao.IPNodeDao;
import com.afunms.topology.model.HostNode;
import com.afunms.topology.util.*;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.loader.IPNodeLoader;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.IpMac;
import com.afunms.polling.snmp.CiscoSnmp;
import com.afunms.polling.snmp.H3CSnmp;
import com.afunms.polling.task.ThreadPool;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.api.I_HostCollectData;
import com.afunms.polling.base.Node;

public class MacManager extends BaseManager implements ManagerInterface
{
	private String list()
	{
		IPNodeDao dao = new IPNodeDao();
		List list = null;
		try{
			list = dao.loadAll(); 
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dao.close();
		}
		for(int i=0;i<list.size();i++)
		{
			IPNode vo = (IPNode)list.get(i);
			Node ipNode = PollingEngine.getInstance().getNodeByID(vo.getId());
			if(ipNode==null)
			   vo.setStatus(0);
			else
			   vo.setStatus(ipNode.getStatus());	
		}
		request.setAttribute("list",list);				
		return "/application/ip_node/list.jsp";
	}
	
	private String update()
	{
		IPNode vo = new IPNode();
		vo.setIpAddress(getParaValue("ip"));
        vo.setId(getParaIntValue("id"));        
        vo.setAlias(getParaValue("alias"));

        Node node = PollingEngine.getInstance().getNodeByID(vo.getId());
        node.setIpAddress(vo.getIpAddress());
        node.setAlias(vo.getAlias());
        
 	    DaoInterface dao = new IPNodeDao();    	   
	    setTarget("/ipnode.do?action=list");
        return update(dao,vo);
	}
	
	private String delete()
	{		
		Vector ipmacVector = new Vector();
		String id = getParaValue("id"); 
		String macip = getParaValue("macip");
		String index = getParaValue("index");
		String type = getParaValue("type");
		Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(id));
		
		SysConfigFileUtil snmputil = new SysConfigFileUtil();
		try{
			snmputil.deleteArp(host.getIpAddress(), host.getWritecommunity(), host.getSnmpversion(), ".1.3.6.1.2.1.4.22.1.4."+index+"."+macip);
		}catch(Exception e){
			e.printStackTrace();
		}
		SnmpService snmp = new SnmpService();
		
//      ---------------------------------------------------�õ�����IpNetToMedia,��ֱ������豸���ӵ�ip start
	     try
	     {
	        String[] oids = new String[]
	                    {"1.3.6.1.2.1.4.22.1.1",   //1.ifIndex
	        		     "1.3.6.1.2.1.4.22.1.2",   //2.mac
	                     "1.3.6.1.2.1.4.22.1.3",   //3.ip
	                     "1.3.6.1.2.1.4.22.1.4"};  //4.type
			String[][] valueArray = null;   	  
			try {
				valueArray = snmp.getTableData(host.getIpAddress(),host.getCommunity(),oids);
			} catch(Exception e){
				valueArray = null;
				e.printStackTrace();
				//SysLogger.error(host.getIpAddress() + "_H3CSnmp");
			}
			if(valueArray != null){
		   	  for(int i=0;i<valueArray.length;i++)
		   	  {
		   		  if(valueArray[i][3]!=null && "2".equalsIgnoreCase(valueArray[i][3]))continue;
		   		  IpMac ipmac = new IpMac();
		   		  for(int j=0;j<4;j++){
		   			String sValue = valueArray[i][j];
		   			if(sValue == null)continue;
					if(j==0){
						ipmac.setIfindex(sValue);
					}else if (j==1){
						ipmac.setMac(sValue);
					}else if (j==2){
						ipmac.setIpaddress(sValue);									
					}
		   		 }
		   		//SysLogger.info(ipmac.getIpaddress()+"==="+ipmac.getMac()+"==="+ipmac.getIfindex());
		   		ipmac.setIfband("0");
		   		ipmac.setIfsms("0");
				ipmac.setCollecttime(new GregorianCalendar());
				ipmac.setRelateipaddr(host.getIpAddress());
				SysLogger.info(ipmac.getIpaddress());
				ipmacVector.addElement(ipmac);
		   	  }	
			}
	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }
	    Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
	    ipAllData.remove("ipmac");
	    ipAllData.put("ipmac", ipmacVector);
	    Hashtable sharedata = ShareData.getSharedata();
	    
		if (ipAllData != null && ipAllData.size()>0)
			sharedata.put(host.getIpAddress(),ipAllData);
	  
		//  ---------------------------------------------------�õ�����IpNetToMedia,��ֱ������豸���ӵ�ip end			
		if("firewall".equals(type)){
			return "/monitor.do?action=firewallarp&id="+id+"&ipaddress="+host.getIpAddress();
		}
		return "/monitor.do?action=netarp&id="+id+"&ipaddress="+host.getIpAddress();
	}
	
	private void deletesingle()
	{		
		Vector ipmacVector = new Vector();
		String flag = getParaValue("flag"); 
		request.setAttribute("flag", flag);
		String id = getParaValue("id"); 
		String macip = getParaValue("macip");
		String index = getParaValue("index");
		Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(id));
		
		SysConfigFileUtil snmputil = new SysConfigFileUtil();
		try{
			snmputil.deleteArp(host.getIpAddress(), host.getWritecommunity(), host.getSnmpversion(), ".1.3.6.1.2.1.4.22.1.4."+index+"."+macip);
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	private String deleteall()
	{		
		Vector ipmacVector = new Vector();
		String flag = getParaValue("flag"); 
		request.setAttribute("flag", flag);
		String id = getParaValue("id"); 
		String macip = getParaValue("macip");
		String index = getParaValue("index");
		Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(id));
		String[] oids = null;
		int[] intvalues = null;
		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
		if(ipAllData != null && ipAllData.size()>0){
			ipmacVector = (Vector)ipAllData.get("ipmac");
			if(ipmacVector != null && ipmacVector.size()>0){
				oids = new String[ipmacVector.size()];
				intvalues = new int[ipmacVector.size()];
				// �����̳߳�
        		ThreadPool threadPool = new ThreadPool(20);														
        		// ��������
        		for (int i=0; i<ipmacVector.size(); i++) {
					IpMac ipmac = (IpMac)ipmacVector.get(i);
					oids[i] = ".1.3.6.1.2.1.4.22.1.4."+ipmac.getIfindex()+"."+ipmac.getIpaddress();
					intvalues[i]=2;
					try{
            			threadPool.runTask(createTask(host.getIpAddress(), host.getWritecommunity(), host.getSnmpversion(), oids[i]));
					}catch(Exception e){
						e.printStackTrace();
					}
        		}
        		// �ر��̳߳ز��ȴ������������
        		threadPool.join();
        		threadPool.close();
        		threadPool = null;
			}
		}
		SnmpService snmp = new SnmpService();
		ipmacVector = new Vector();
		
		//      ---------------------------------------------------�õ�����IpNetToMedia,��ֱ������豸���ӵ�ip start
	     try
	     {
	        oids = new String[]
	                    {"1.3.6.1.2.1.4.22.1.1",   //1.ifIndex
	        		     "1.3.6.1.2.1.4.22.1.2",   //2.mac
	                     "1.3.6.1.2.1.4.22.1.3",   //3.ip
	                     "1.3.6.1.2.1.4.22.1.4"};  //4.type
			String[][] valueArray = null;   	  
			try {
				valueArray = snmp.getTableData(host.getIpAddress(),host.getCommunity(),oids);
			} catch(Exception e){
				valueArray = null;
				e.printStackTrace();
				SysLogger.error(host.getIpAddress() + "_H3CSnmp");
			}
			if(valueArray != null){
		   	  for(int i=0;i<valueArray.length;i++)
		   	  {
		   		  IpMac ipmac = new IpMac();
		   		  for(int j=0;j<4;j++){
		   			String sValue = valueArray[i][j];
		   			if(sValue == null)continue;
					if(j==0){
						ipmac.setIfindex(sValue);
					}else if (j==1){
						ipmac.setMac(sValue);
					}else if (j==2){
						ipmac.setIpaddress(sValue);									
					}
		   		 }
		   		//SysLogger.info(ipmac.getIpaddress()+"==="+ipmac.getMac()+"==="+ipmac.getIfindex());
		   		ipmac.setIfband("0");
		   		ipmac.setIfsms("0");
				ipmac.setCollecttime(new GregorianCalendar());
				ipmac.setRelateipaddr(host.getIpAddress());
				ipmacVector.addElement(ipmac);
		   	  }	
			}
	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }
	    ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
	    ipAllData.put("ipmac", ipmacVector);
	    ShareData.getSharedata().put(host.getIpAddress(), ipAllData);
	  
	    //  ---------------------------------------------------�õ�����IpNetToMedia,��ֱ������豸���ӵ�ip end		
				
		return "/monitor.do?action=netarp&id="+id+"&ipaddress="+host.getIpAddress();
	}
	
	private String refresh()
	{		
		Vector ipmacVector = new Vector();
		String id = getParaValue("id"); 
		String flag = getParaValue("flag"); 
		String type = getParaValue("type");
		request.setAttribute("flag", flag);
		Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(id));
		String[] oids = null;
		int[] intvalues = null;
		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());

		SnmpService snmp = new SnmpService();
		ipmacVector = new Vector();
		
//      ---------------------------------------------------�õ�����IpNetToMedia,��ֱ������豸���ӵ�ip start
	     try
	     {
	        oids = new String[]
	                    {"1.3.6.1.2.1.4.22.1.1",   //1.ifIndex
	        		     "1.3.6.1.2.1.4.22.1.2",   //2.mac
	                     "1.3.6.1.2.1.4.22.1.3",   //3.ip
	                     "1.3.6.1.2.1.4.22.1.4"};  //4.type
			String[][] valueArray = null;   	  
			try {
				valueArray = snmp.getTableData(host.getIpAddress(),host.getCommunity(),oids);
			} catch(Exception e){
				valueArray = null;
				e.printStackTrace();
				SysLogger.error(host.getIpAddress() + "_H3CSnmp");
			}
			if(valueArray != null){
		   	  for(int i=0;i<valueArray.length;i++)
		   	  {
		   		  IpMac ipmac = new IpMac();
		   		  for(int j=0;j<4;j++){
		   			String sValue = valueArray[i][j];
		   			if(sValue == null)continue;
					if(j==0){
						ipmac.setIfindex(sValue);
					}else if (j==1){
						ipmac.setMac(sValue);
					}else if (j==2){
						ipmac.setIpaddress(sValue);									
					}
		   		 }
		   		//SysLogger.info(ipmac.getIpaddress()+"==="+ipmac.getMac()+"==="+ipmac.getIfindex());
		   		ipmac.setIfband("0");
		   		ipmac.setIfsms("0");
				ipmac.setCollecttime(new GregorianCalendar());
				ipmac.setRelateipaddr(host.getIpAddress());
				ipmacVector.addElement(ipmac);
		   	  }	
			}
	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }
	    ipAllData.put("ipmac", ipmacVector);
	    ShareData.getSharedata().put(host.getIpAddress(), ipAllData);
	  
//  ---------------------------------------------------�õ�����IpNetToMedia,��ֱ������豸���ӵ�ip end		
		if("firewall".equals(type)){
			return "/monitor.do?action=firewallarp&id="+id+"&ipaddress="+host.getIpAddress();
		}
		return "/monitor.do?action=netarp&id="+id+"&ipaddress="+host.getIpAddress();
	}
	
	private String refreshfdb()
	{		
		Vector fdbVector = new Vector();
		String id = getParaValue("id"); 
		String flag = getParaValue("flag"); 
		request.setAttribute("flag", flag);
		Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(id));
		String[] oids = null;
		Hashtable MACVSIP = new Hashtable();
		int[] intvalues = null;
		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());

		SnmpService snmp = new SnmpService();
		//ipmacVector = new Vector();
		
		//---------------------------------------------------�õ�����IpNetToMedia,��ֱ������豸���ӵ�ip start
	     try
	     {
	        oids = new String[]
	                    {"1.3.6.1.2.1.4.22.1.1",   //1.ifIndex
	        		     "1.3.6.1.2.1.4.22.1.2",   //2.mac
	                     "1.3.6.1.2.1.4.22.1.3",   //3.ip
	                     "1.3.6.1.2.1.4.22.1.4"};  //4.type
			String[][] valueArray = null;   	  
			try {
				valueArray = snmp.getTableData(host.getIpAddress(),host.getCommunity(),oids);
			} catch(Exception e){
				valueArray = null;
				e.printStackTrace();
				SysLogger.error(host.getIpAddress() + "_CiscoSnmp");
			}
		   	  for(int i=0;i<valueArray.length;i++)
		   	  {
		   		  IpMac ipmac = new IpMac();
		   		  for(int j=0;j<4;j++){
		   			String sValue = valueArray[i][j];
		   			//SysLogger.info("MAC===="+sValue);
		   			if(sValue == null)continue;
					if(j==0){
						ipmac.setIfindex(sValue);
					}else if (j==1){
						ipmac.setMac(sValue);
					}else if (j==2){
						ipmac.setIpaddress(sValue);									
					}
		   		 }
		   		ipmac.setIfband("0");
		   		ipmac.setIfsms("0");
				ipmac.setCollecttime(new GregorianCalendar());
				ipmac.setRelateipaddr(host.getIpAddress());
				//ipmacVector.addElement(ipmac);
				//SysLogger.info("ARP hostip==>"+host.getIpAddress()+"=="+ipmac.getMac()+"====="+ipmac.getIpaddress());
				if(ipmac != null && ipmac.getMac() != null && ipmac.getIpaddress() != null)
					MACVSIP.put(ipmac.getMac(), ipmac.getIpaddress());
		   	  }	
	    }
	    catch (Exception e)
	    {
	    	//SysLogger.error("getIpNetToMediaTable(),ip=" + address + ",community=" + community);
	        //tableValues = null;
	        e.printStackTrace();
	    }
		
		//---------------------------------------------------�õ�����FDB,��ֱ������豸���ӵ�ip start
	     try
	     {
	    	 List fdbList = SnmpUtil.getInstance().getFdbTable(host.getIpAddress(),host.getCommunity());
	    	 if(fdbList!=null && fdbList.size()!=0){
	    		 for(int i=0;i<fdbList.size();i++){
	    			 String[] item = new String[2];
	    			 item = (String[])fdbList.get(i);
	    			 IpMac ipmac = new IpMac();
	    			 ipmac.setIfindex(item[0]);
	    			 ipmac.setMac(item[1]);
	    			 ipmac.setIfband("0");
				   	 ipmac.setIfsms("0");
					 ipmac.setCollecttime(new GregorianCalendar());
					 ipmac.setRelateipaddr(host.getIpAddress());
					 if(MACVSIP != null && MACVSIP.containsKey(ipmac.getMac())){
						 ipmac.setIpaddress((String)MACVSIP.get(ipmac.getMac()));
					 }else{
						 continue;
						 //ipmac.setIpaddress("");
					 }
					 ipmac.setRelateipaddr(host.getIpAddress());//���ý�����IP
					 //SysLogger.info("FDB hostip : "+host.getIpAddress()+"  ip :"+ipmac.getIpaddress()+" ifIndex : "+ipmac.getIfindex()+" MAC : "+ipmac.getMac()) ;
					 fdbVector.add(ipmac);
	    		 }
	    	 }
	    		 //fdbVector.add(fdbList);

	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }
	    ipAllData.put("fdb", fdbVector);
	    ShareData.getSharedata().put(host.getIpAddress(), ipAllData);
	  
//  ---------------------------------------------------�õ�����IpNetToMedia,��ֱ������豸���ӵ�ip end		
				
		return "/monitor.do?action=netfdb&id="+id+"&ipaddress="+host.getIpAddress();
	}
	
	private String add()
    {    	   
    	IPNode vo = new IPNode();
    	vo.setId(KeyGenerator.getInstance().getNextKey());
        vo.setIpAddress(getParaValue("ip"));
        vo.setAlias(getParaValue("alias"));
        
        IPNodeLoader loader = new IPNodeLoader();
        loader.loadOne(vo);
	    DaoInterface dao = new IPNodeDao();    	   
	    setTarget("/ipnode.do?action=list");
        return save(dao,vo);

    }    
	
	public String execute(String action) 
	{	
        if (action.equals("list"))
            return list();    
		if(action.equals("ready_add"))
			return "/application/ip_node/add.jsp";
        if (action.equals("add"))
        	return add();
  	    if (action.equals("delete"))
            return delete();
	  	  if (action.equals("deleteall"))
	          return deleteall();
	  	if (action.equals("refresh"))
	        return refresh();
	  	if (action.equals("refreshfdb"))
	        return refreshfdb();
        if (action.equals("update"))
        	return update();  
        if(action.equals("ready_edit"))
        {	  
 		    DaoInterface dao = new IPNodeDao();
    	    setTarget("/application/ip_node/edit.jsp");
            return readyEdit(dao);
        }    
		setErrorCode(ErrorMessage.ACTION_NO_FOUND);
		return null;
	}
	
    /**
    ��������
     */	
	private static Runnable createTask(final String ipaddress,final String writecommunity,final int version,final String oid) {
		
		return new Runnable() {
			public void run() {
				try {                	
					SysConfigFileUtil snmputil = new SysConfigFileUtil();
					try{
						snmputil.deleteArp(ipaddress,writecommunity,version,oid);
					}catch(Exception e){
						e.printStackTrace();
					}
            }catch(Exception exc){
            	
            }

            //System.out.println("Task " + taskID + ": end");
        }
		};
	}
}
