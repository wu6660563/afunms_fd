/**
 * <p>Description:probe the router table</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-12
 */

package com.afunms.discovery;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import com.afunms.common.util.SnmpUtil;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SystemConstant;
import com.afunms.polling.task.ThreadPool;
import com.afunms.topology.dao.DiscoverConfigDao;
import com.afunms.topology.model.DiscoverConfig;

public class CiscoCDPProbeThread extends BaseThread
//public class CiscoCDPProbeThread implements Runnable
{
   private Host node;
   private static SnmpUtil snmp = SnmpUtil.getInstance();
   
   public CiscoCDPProbeThread(Host node)
   {
      this.node = node;
   }
   
   public void run()
   { 
	   if(DiscoverEngine.getInstance().getStopStatus() == 1)return;
	   List netshieldList = DiscoverResource.getInstance().getNetshieldList();
	   List netincludeList = DiscoverResource.getInstance().getNetincludeList();
	   
	      //�ҳ��Ϸ���CDP�б�
	      List cdpList = node.getCdpList(); 
	      
	  	  if(cdpList==null || cdpList.size()==0)   
	      {
	  		  DiscoverEngine.getInstance().addDiscoverdcount();
	  		  node.updateCount(1);      
	  		  setCompleted(true);
	 	      return; 	
	      }
	  	  
	    //�ӿ�
  		Date startdate = new Date();
		// �����̳߳�
		ThreadPool threadPool = new ThreadPool(cdpList.size());														
		// ��������
		
	      for(int i=0;i<cdpList.size();i++)
	      {
	    	  try{		  
	    		  CdpCachEntryInterface cdp = (CdpCachEntryInterface)cdpList.get(i);   		  
	    		  threadPool.runTask(CDPSubThread.createTask(cdp, node)); 
	    	  }catch(Exception e){
	    		  e.printStackTrace();
	    	  }
	      }
  		// �ر��̳߳ز��ȴ������������
		threadPool.join();
		threadPool.close();
		threadPool = null;
		Date enddate = new Date();
		SysLogger.info("##############################");
		SysLogger.info("### "+node.getIpAddress()+" ����ʱ�� "+(enddate.getTime()-startdate.getTime())+"####");
		SysLogger.info("##############################");
		DiscoverEngine.getInstance().addDiscoverdcount();
		node.updateCount(1);      
		setCompleted(true);
		return;
   }//end_run
}