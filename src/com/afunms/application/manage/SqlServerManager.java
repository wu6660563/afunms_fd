/**
 * <p>Description:Tomcat Manager</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-12-07
 */

package com.afunms.application.manage;

import java.util.*;
import java.text.*;

import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;

import com.afunms.common.base.*;
import com.afunms.common.*;
import com.afunms.common.util.*;
import com.afunms.config.dao.NodeToBusinessDao;
import com.afunms.config.model.NodeToBusiness;
import com.afunms.event.dao.EventListDao;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.node.DBNode;
import com.afunms.polling.loader.*;
import com.afunms.application.model.DBTypeVo;
import com.afunms.application.model.DBVo;
import com.afunms.application.model.OracleEntity;
import com.afunms.system.model.User;
import com.afunms.topology.util.*;
import com.afunms.application.dao.DBDao;
import com.afunms.application.dao.DBTypeDao;
import com.afunms.application.dao.OraclePartsDao;
import com.afunms.application.util.DBPool;
import com.afunms.application.util.DBRefreshHelper;
import com.afunms.application.util.IpTranslation;
import com.afunms.topology.dao.DiscoverCompleteDao;
import com.afunms.polling.impl.*;
import com.afunms.polling.api.*;

import com.afunms.report.jfree.ChartCreator;

import org.jfree.data.general.DefaultPieDataset;

public class SqlServerManager extends BaseManager implements ManagerInterface
{
	public static Hashtable rsc_type_ht = new Hashtable();
	public static Hashtable req_mode_ht = new Hashtable();
	public static Hashtable mode_ht = new Hashtable();
	public static Hashtable req_status_ht = new Hashtable();
	public static Hashtable req_ownertype_ht = new Hashtable();
	private String list()
	{
		DBDao dao = new DBDao();
		List list = new ArrayList();
		try{
			list = dao.loadAll();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dao.close();
		}
		for(int i=0;i<list.size();i++)
		{
			DBVo vo = (DBVo)list.get(i);
			Node DBNode = PollingEngine.getInstance().getNodeByID(vo.getId());
			if(DBNode==null)
			   vo.setStatus(0);
			else
			   vo.setStatus(DBNode.getStatus());	
		}
		request.setAttribute("list",list);				
		return "/application/db/list.jsp";
	}

	private String add()
    {    	   
		DBVo vo = new DBVo();
    	vo.setId(KeyGenerator.getInstance().getNextKey());
    	vo.setUser(getParaValue("user"));
    	vo.setPassword(getParaValue("password"));        
        vo.setAlias(getParaValue("alias"));
        vo.setIpAddress(getParaValue("ip_address"));
        vo.setPort(getParaValue("port"));
        vo.setDbName(getParaValue("db_name"));
        vo.setCategory(getParaIntValue("category"));
        vo.setDbuse(getParaValue("dbuse"));
        vo.setSendmobiles(getParaValue("sendmobiles"));
        vo.setSendemail(getParaValue("sendemail"));
        String allbid = "";
        String[] businessids = getParaArrayValue("checkbox");
        if(businessids != null && businessids.length>0){
        	for(int i=0;i<businessids.length;i++){
        		
        		String bid = businessids[i];
        		allbid= allbid+bid+",";
        	}
        } 
        vo.setBid(allbid);
        vo.setManaged(getParaIntValue("managed"));
        vo.setDbtype(getParaIntValue("dbtype"));
        //�����ݿ������ӱ����ָ��
        //DiscoverCompleteDao dcDao = new DiscoverCompleteDao();
        //dcDao.addDBMonitor(vo.getId(),vo.getIpAddress(),"mysql");
        
        //����ѯ�߳������ӱ����ӽڵ�
        //DBLoader loader = new DBLoader();
        //loader.loadOne(vo);
        //loader.close();
        
        DBDao dao = new DBDao();
        try{
        	dao.save(vo);	    
        }catch(Exception e){
			e.printStackTrace();
		}finally{
			dao.close();
		}
        return "/db.do?action=list";
    }    
	
	public String delete()
	{
		String id = getParaValue("radio"); 
		DBDao dao = new DBDao();
		try{
			dao.delete(id);		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dao.close();
		}
		int nodeId = Integer.parseInt(id);
        PollingEngine.getInstance().deleteNodeByID(nodeId);
        DBPool.getInstance().removeConnect(nodeId);
        
        return "/db.do?action=list";
	}
	
	private String update()
    {    	   
		DBVo vo = new DBVo();
		
		
    	vo.setId(getParaIntValue("id"));
    	vo.setUser(getParaValue("user"));
    	vo.setPassword(getParaValue("password"));        
        vo.setAlias(getParaValue("alias"));
        vo.setIpAddress(getParaValue("ip_address"));
        vo.setPort(getParaValue("port"));
        vo.setDbName(getParaValue("db_name"));
        vo.setCategory(getParaIntValue("category"));
        vo.setDbuse(getParaValue("dbuse"));
        vo.setSendmobiles(getParaValue("sendmobiles"));
        vo.setSendemail(getParaValue("sendemail"));
        String allbid = "";
        String[] businessids = getParaArrayValue("checkbox");
        if(businessids != null && businessids.length>0){
        	for(int i=0;i<businessids.length;i++){
        		
        		String bid = businessids[i];
        		allbid= allbid+bid+",";
        	}
        } 
        vo.setBid(allbid);
        vo.setManaged(getParaIntValue("managed"));
        vo.setDbtype(getParaIntValue("dbtype"));
        /*
        DBPool.getInstance().removeConnect(vo.getId());
        
        if(PollingEngine.getInstance().getNodeByID(vo.getId())!=null)
        {        
        	DBNode dbNode = (DBNode)PollingEngine.getInstance().getNodeByID(vo.getId());
        	dbNode.setUser(vo.getUser());
        	dbNode.setPassword(vo.getPassword());
        	dbNode.setPort(vo.getPort());
        	dbNode.setIpAddress(vo.getIpAddress());
        	dbNode.setAlias(vo.getAlias());
        	dbNode.setDbName(vo.getDbName());        	
        } 
        */
        DBDao dao = new DBDao();
        try{
        	dao.update(vo);	    
        }catch(Exception e){
			e.printStackTrace();
		}finally{
			dao.close();
		}
        return "/db.do?action=list";
    }
	
	private String cancelmanage()
    {    	   
		DBVo vo = new DBVo();
		DBDao dao = new DBDao();
		try{
			vo = (DBVo)dao.findByID(getParaValue("id"));
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dao.close();
		}
		vo.setManaged(0);
        /*
        DBPool.getInstance().removeConnect(vo.getId());
        
        if(PollingEngine.getInstance().getNodeByID(vo.getId())!=null)
        {        
        	DBNode dbNode = (DBNode)PollingEngine.getInstance().getNodeByID(vo.getId());
        	dbNode.setUser(vo.getUser());
        	dbNode.setPassword(vo.getPassword());
        	dbNode.setPort(vo.getPort());
        	dbNode.setIpAddress(vo.getIpAddress());
        	dbNode.setAlias(vo.getAlias());
        	dbNode.setDbName(vo.getDbName());        	
        } 
        */
        dao = new DBDao();
        try{
        	dao.update(vo);	    
        }catch(Exception e){
			e.printStackTrace();
		}finally{
			dao.close();
		}
        return "/db.do?action=list";
    }
	
	private String sqlserverping()
    {    	   
		DBVo vo = new DBVo();
		double avgpingcon = 0;
		
		String bufferCacheHitRatio = null;
		String planCacheHitRatio = null;
		String cursorManagerByTypeHitRatio = null;
		String catalogMetadataHitRatio = null;
		
		String dbOfflineErrors = null;
		String killConnectionErrors = null;
		String userErrors = null;
		String infoErrors = null;
		String sqlServerErrors_total = null;
		
		String cachedCursorCounts = null; //cachedCursorCounts_total
		String cursorCacheUseCounts = null;//cursorCacheUseCounts_total
		String cursorRequests_total = null;
		String activeCursors = null;//activeCursors_total
		String cursorMemoryUsage = null;//cursorMemoryUsage_total
		String cursorWorktableUsage = null;//cursorWorktableUsage_total
		String activeOfCursorPlans = null;//Number of active cursor plans_total
		
		Hashtable sysValue = new Hashtable();
		Hashtable database = new Hashtable();
		String id = getParaValue("id");
		request.setAttribute("id", id);
		try{
			DBDao dao = new DBDao();
			try{
				vo = (DBVo)dao.findByID(id);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}
			
			DBTypeDao typedao = new DBTypeDao();
			DBTypeVo typevo = null;
			try{
				typevo = (DBTypeVo)typedao.findByID(vo.getDbtype()+"");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				typedao.close();
			}
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "<font color=red>����ֹͣ</font>";

			
//			Hashtable allsqlserverdata = ShareData.getSqlserverdata();
//			Hashtable ipsqlserverdata = new Hashtable();
//			if(allsqlserverdata != null && allsqlserverdata.size()>0){
//				if(allsqlserverdata.containsKey(vo.getIpAddress())){
//					ipsqlserverdata = (Hashtable)allsqlserverdata.get(vo.getIpAddress());
//					if(ipsqlserverdata.containsKey("status")){
//						String p_status = (String)ipsqlserverdata.get("status");
//						if(p_status != null && p_status.length()>0){
//							if("1".equalsIgnoreCase(p_status)){
//								runstr = "��������";
//							}
//						}
//					}
//					if(ipsqlserverdata.containsKey("sysValue")){
//						sysValue = (Hashtable)ipsqlserverdata.get("sysValue");
//					}
//					Hashtable retValue = (Hashtable)ipsqlserverdata.get("retValue");
//					Hashtable pages = (Hashtable)retValue.get("pages");
			DBDao dbDao = new DBDao();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+vo.getAlias();
			Hashtable status = dbDao.getSqlserver_nmsstatus(serverip);
			Hashtable pages =  dbDao.getSqlserver_nmspages(serverip);
			sysValue = dbDao.getSqlserver_nmssysvalue(serverip);
			database=dbDao.getSqlserver_nmsdbvalue(serverip);
			String p_status = (String)status.get("status");
			if(p_status != null && p_status.length()>0){
				if("1".equalsIgnoreCase(p_status)){
					runstr = "��������";
				}
			}
			dao.close();
			if(pages != null && pages.size() > 0){
				bufferCacheHitRatio = (String)pages.get("bufferCacheHitRatio");
				planCacheHitRatio = (String)pages.get("planCacheHitRatio");
				cursorManagerByTypeHitRatio = (String)pages.get("cursorManagerByTypeHitRatio");
				catalogMetadataHitRatio = (String)pages.get("catalogMetadataHitRatio");
	
				dbOfflineErrors = (String)pages.get("dbOfflineErrors");
				killConnectionErrors = (String)pages.get("killConnectionErrors");
				userErrors = (String)pages.get("userErrors");
				infoErrors = (String)pages.get("infoErrors");
				sqlServerErrors_total = (String)pages.get("sqlServerErrors_total");
				
				cachedCursorCounts = (String)pages.get("cachedCursorCounts"); 
				cursorCacheUseCounts = (String)pages.get("cursorCacheUseCounts");
				cursorRequests_total = (String)pages.get("cursorRequests_total");
				activeCursors = (String)pages.get("activeCursors");
				cursorMemoryUsage = (String)pages.get("cursorMemoryUsage");
				cursorWorktableUsage = (String)pages.get("cursorWorktableUsage");
				activeOfCursorPlans = (String)pages.get("activeOfCursorPlans");
			}
			request.setAttribute("bufferCacheHitRatio", bufferCacheHitRatio);
			request.setAttribute("planCacheHitRatio", planCacheHitRatio);
			request.setAttribute("cursorManagerByTypeHitRatio", cursorManagerByTypeHitRatio);
			request.setAttribute("catalogMetadataHitRatio", catalogMetadataHitRatio);
			
			request.setAttribute("dbOfflineErrors", dbOfflineErrors);
			request.setAttribute("killConnectionErrors", killConnectionErrors);
			request.setAttribute("userErrors", userErrors);
			request.setAttribute("infoErrors", infoErrors);
			request.setAttribute("sqlServerErrors_total", sqlServerErrors_total);
			request.setAttribute("cachedCursorCounts", cachedCursorCounts);
			request.setAttribute("cursorCacheUseCounts", cursorCacheUseCounts);
			request.setAttribute("cursorRequests_total", cursorRequests_total);
			request.setAttribute("activeCursors", activeCursors);
			request.setAttribute("cursorMemoryUsage", cursorMemoryUsage);
			request.setAttribute("cursorWorktableUsage", cursorWorktableUsage);
			request.setAttribute("activeOfCursorPlans", activeOfCursorPlans);
			
			
			request.setAttribute("runstr", runstr);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());					
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			String newip=SysUtil.doip(vo.getIpAddress());						
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"SQLPing","ConnectUtilization",starttime1,totime1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			String pingconavg = "";
			if (ConnectUtilizationhash.get("avgpingcon")!=null)
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
			}
	
			  avgpingcon = new Double(pingconavg+"").doubleValue();
//			String starttime1 = time1 + " 00:00:00";
//			String totime1 = time1 + " 23:59:59";
//			
//			Hashtable ConnectUtilizationhash = new Hashtable();
//			I_HostCollectData hostmanager=new HostCollectDataManager();
//			try{
//				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"SQLPing","ConnectUtilization",starttime1,totime1);
//			}catch(Exception ex){
//				ex.printStackTrace();
//			}
//			if (ConnectUtilizationhash.get("avgpingcon")!=null)
//				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
//			if(pingconavg != null){
//				pingconavg = pingconavg.replace("%", "");
//			}
			//��ͼ
			//p_draw_line(responsehash,"��ͨ��",newip+"response",750,200);
			//p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);

			//pingƽ��ֵ
			//maxhash = new Hashtable();
			//maxhash.put("avgpingcon",pingconavg);

			//imgurlhash
			//imgurlhash.put("ConnectUtilization","resource/image/jfreechart/"+newip+"ConnectUtilization"+".png");    
			
//			  double avgpingcon = new Double(pingconavg+"").doubleValue();
//			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
//			  DefaultPieDataset dpd = new DefaultPieDataset();
//			  dpd.setValue("������",avgpingcon);
//			  dpd.setValue("��������",100 - avgpingcon);
//			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			
			//request.setAttribute("imgurl",imgurlhash);
			//request.setAttribute("max",maxhash);
			//request.setAttribute("chart1",chart1);
			request.setAttribute("sqlsys",sysValue);
			request.setAttribute("dbValue",database);
		        
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/sqlserverping.jsp";
    }
	
	private String sqlserversys()
    {    	   
		DBVo vo = new DBVo();
		Hashtable sysValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		String pingconavg ="0";
		double avgpingcon = 0;
		String id = getParaValue("id");
		request.setAttribute("id", id);
		
		String bufferCacheHitRatio = null;
		String planCacheHitRatio = null;
		String cursorManagerByTypeHitRatio = null;
		String catalogMetadataHitRatio = null;
		
		String dbOfflineErrors = null;
		String killConnectionErrors = null;
		String userErrors = null;
		String infoErrors = null;
		String sqlServerErrors_total = null;
		
		String cachedCursorCounts = null; //cachedCursorCounts_total
		String cursorCacheUseCounts = null;//cursorCacheUseCounts_total
		String cursorRequests_total = null;
		String activeCursors = null;//activeCursors_total
		String cursorMemoryUsage = null;//cursorMemoryUsage_total
		String cursorWorktableUsage = null;//cursorWorktableUsage_total
		String activeOfCursorPlans = null;//Number of active cursor plans_total
		
		String pingjun_lockWaits = null;
		String pingjun_memoryGrantQueueWaits = null;
		String pingjun_threadSafeMemoryObjectWaits = null;
		String pingjun_logWriteWaits = null;
		String pingjun_logBufferWaits = null;
		String pingjun_networkIOWaits = null;
		String pingjun_pageIOLatchWaits = null;
		String pingjun_pageLatchWaits = null;
		String pingjun_nonPageLatchWaits = null;
		String pingjun_waitForTheWorker = null;
		String pingjun_workspaceSynchronizationWaits = null;
		String pingjun_transactionOwnershipWaits = null;
		
		String jingxing_lockWaits = null;
		String jingxing_memoryGrantQueueWaits = null;
		String jingxing_threadSafeMemoryObjectWaits = null;
		String jingxing_logWriteWaits = null;
		String jingxing_logBufferWaits = null;
		String jingxing_networkIOWaits = null;
		String jingxing_pageIOLatchWaits = null;
		String jingxing_pageLatchWaits = null;
		String jingxing_nonPageLatchWaits = null;
		String jingxing_waitForTheWorker = null;
		String jingxing_workspaceSynchronizationWaits = null;
		String jingxing_transactionOwnershipWaits = null;
		
		String qidong_lockWaits = null;
		String qidong_memoryGrantQueueWaits = null;
		String qidong_threadSafeMemoryObjectWaits = null;
		String qidong_logWriteWaits = null;
		String qidong_logBufferWaits = null;
		String qidong_networkIOWaits = null;
		String qidong_pageIOLatchWaits = null;
		String qidong_pageLatchWaits = null;
		String qidong_nonPageLatchWaits = null;
		String qidong_waitForTheWorker = null;
		String qidong_workspaceSynchronizationWaits = null;
		String qidong_transactionOwnershipWaits = null;
		
		String leiji_lockWaits = null;
		String leiji_memoryGrantQueueWaits = null;
		String leiji_threadSafeMemoryObjectWaits = null;
		String leiji_logWriteWaits = null;
		String leiji_logBufferWaits = null;
		String leiji_networkIOWaits = null;
		String leiji_pageIOLatchWaits = null;
		String leiji_pageLatchWaits = null;
		String leiji_nonPageLatchWaits = null;
		String leiji_waitForTheWorker = null;
		String leiji_workspaceSynchronizationWaits = null;
		String leiji_transactionOwnershipWaits = null;
		
		try{
			DBDao dao = new DBDao();
			try{
				vo = (DBVo)dao.findByID(id);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}
			
			DBTypeDao typedao = new DBTypeDao();
			DBTypeVo typevo = null;
			try{
				typevo = (DBTypeVo)typedao.findByID(vo.getDbtype()+"");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				typedao.close();
			}
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "<font color=red>����ֹͣ</font>";
			
//			Hashtable allsqlserverdata = ShareData.getSqlserverdata();
//			Hashtable ipsqlserverdata = new Hashtable();
//			if(allsqlserverdata != null && allsqlserverdata.size()>0){
//				if(allsqlserverdata.containsKey(vo.getIpAddress())){
//					ipsqlserverdata = (Hashtable)allsqlserverdata.get(vo.getIpAddress());
//					if(ipsqlserverdata.containsKey("status")){
//						String p_status = (String)ipsqlserverdata.get("status");
//						if(p_status != null && p_status.length()>0){
//							if("1".equalsIgnoreCase(p_status)){
//								runstr = "��������";
//							}
//						}
//					}
//					if(ipsqlserverdata.containsKey("sysValue")){
//						sysValue = (Hashtable)ipsqlserverdata.get("sysValue");
//					}
//					if(ipsqlserverdata.containsKey("retValue")){
//						Hashtable retValue = (Hashtable)ipsqlserverdata.get("retValue");
//						request.setAttribute("sqlValue", retValue);
//					}
//					Hashtable retValue = (Hashtable)ipsqlserverdata.get("retValue");
//					Hashtable pages = (Hashtable)retValue.get("pages");
			DBDao dbDao = new DBDao();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+vo.getAlias();
			Hashtable sqlValue = new Hashtable();
			Hashtable status = dbDao.getSqlserver_nmsstatus(serverip);
			Hashtable pages =  dbDao.getSqlserver_nmspages(serverip);
			Hashtable statisticsHash = dbDao.getSqlserver_nmsstatisticsHash(serverip);
			sysValue = dbDao.getSqlserver_nmssysvalue(serverip);
			Hashtable mems = dbDao.getSqlserver_nmsmems(serverip);
			sqlValue.put("pages", pages);
			sqlValue.put("mems", mems);
			request.setAttribute("sqlValue", sqlValue);
			request.setAttribute("mems", mems);
			String p_status = (String)status.get("status");
			if(p_status != null && p_status.length()>0){
				if("1".equalsIgnoreCase(p_status)){
					runstr = "��������";
				}
			}
			dao.close();
			if(pages != null && pages.size() > 0){
				bufferCacheHitRatio = (String)pages.get("bufferCacheHitRatio");
				planCacheHitRatio = (String)pages.get("planCacheHitRatio");
				cursorManagerByTypeHitRatio = (String)pages.get("cursorManagerByTypeHitRatio");
				catalogMetadataHitRatio = (String)pages.get("catalogMetadataHitRatio");

				dbOfflineErrors = (String)pages.get("dbOfflineErrors");
				killConnectionErrors = (String)pages.get("killConnectionErrors");
				userErrors = (String)pages.get("userErrors");
				infoErrors = (String)pages.get("infoErrors");
				sqlServerErrors_total = (String)pages.get("sqlServerErrors_total");
				
				cachedCursorCounts = (String)pages.get("cachedCursorCounts"); 
				cursorCacheUseCounts = (String)pages.get("cursorCacheUseCounts");
				cursorRequests_total = (String)pages.get("cursorRequests_total");
				activeCursors = (String)pages.get("activeCursors");
				cursorMemoryUsage = (String)pages.get("cursorMemoryUsage");
				cursorWorktableUsage = (String)pages.get("cursorWorktableUsage");
				activeOfCursorPlans = (String)pages.get("activeOfCursorPlans");
			}
			if(statisticsHash != null && statisticsHash.size() > 0){
//					Hashtable statisticsHash = (Hashtable)retValue.get("statisticsHash");
				pingjun_lockWaits = (String)statisticsHash.get("pingjun_lockWaits");
				pingjun_memoryGrantQueueWaits = (String)statisticsHash.get("pingjun_memoryGrantQueueWaits");
				pingjun_threadSafeMemoryObjectWaits = (String)statisticsHash.get("pingjun_threadSafeMemoryObjectWaits");
				pingjun_logWriteWaits = (String)statisticsHash.get("pingjun_logWriteWaits");
				pingjun_logBufferWaits = (String)statisticsHash.get("pingjun_logBufferWaits");
				pingjun_networkIOWaits = (String)statisticsHash.get("pingjun_networkIOWaits");
				pingjun_pageIOLatchWaits = (String)statisticsHash.get("pingjun_pageIOLatchWaits");
				pingjun_pageLatchWaits = (String)statisticsHash.get("pingjun_pageLatchWaits");
				pingjun_nonPageLatchWaits = (String)statisticsHash.get("pingjun_nonPageLatchWaits");
				pingjun_waitForTheWorker = (String)statisticsHash.get("pingjun_waitForTheWorker");
				pingjun_workspaceSynchronizationWaits = (String)statisticsHash.get("pingjun_workspaceSynchronizationWaits");
				pingjun_transactionOwnershipWaits = (String)statisticsHash.get("pingjun_transactionOwnershipWaits");
				
				jingxing_lockWaits= (String)statisticsHash.get("jingxing_lockWaits");
				jingxing_memoryGrantQueueWaits = (String)statisticsHash.get("jingxing_memoryGrantQueueWaits");
				jingxing_threadSafeMemoryObjectWaits = (String)statisticsHash.get("jingxing_threadSafeMemoryObjectWaits");
				jingxing_logWriteWaits = (String)statisticsHash.get("jingxing_logWriteWaits");
				jingxing_logBufferWaits = (String)statisticsHash.get("jingxing_logBufferWaits");
				jingxing_networkIOWaits = (String)statisticsHash.get("jingxing_networkIOWaits");
				jingxing_pageIOLatchWaits = (String)statisticsHash.get("jingxing_pageIOLatchWaits");
				jingxing_pageLatchWaits =(String)statisticsHash.get("jingxing_pageLatchWaits");
				jingxing_nonPageLatchWaits = (String)statisticsHash.get("jingxing_nonPageLatchWaits");
				jingxing_waitForTheWorker = (String)statisticsHash.get("jingxing_waitForTheWorker");
				jingxing_workspaceSynchronizationWaits = (String)statisticsHash.get("jingxing_workspaceSynchronizationWaits");
				jingxing_transactionOwnershipWaits = (String)statisticsHash.get("jingxing_transactionOwnershipWaits");
				
				qidong_lockWaits = (String)statisticsHash.get("qidong_lockWaits");
				qidong_memoryGrantQueueWaits = (String)statisticsHash.get("qidong_memoryGrantQueueWaits");
				qidong_threadSafeMemoryObjectWaits = (String)statisticsHash.get("qidong_threadSafeMemoryObjectWaits");
				qidong_logWriteWaits = (String)statisticsHash.get("qidong_logWriteWaits");
				qidong_logBufferWaits = (String)statisticsHash.get("qidong_logBufferWaits");
				qidong_networkIOWaits = (String)statisticsHash.get("qidong_networkIOWaits");
				qidong_pageIOLatchWaits= (String)statisticsHash.get("qidong_pageIOLatchWaits");
				qidong_pageLatchWaits= (String)statisticsHash.get("qidong_pageLatchWaits");
				qidong_nonPageLatchWaits = (String)statisticsHash.get("qidong_nonPageLatchWaits");
				qidong_waitForTheWorker = (String)statisticsHash.get("qidong_waitForTheWorker");
				qidong_workspaceSynchronizationWaits = (String)statisticsHash.get("qidong_workspaceSynchronizationWaits");
				qidong_transactionOwnershipWaits = (String)statisticsHash.get("qidong_transactionOwnershipWaits");
				
				leiji_lockWaits = (String)statisticsHash.get("leiji_lockWaits");
				leiji_memoryGrantQueueWaits = (String)statisticsHash.get("leiji_memoryGrantQueueWaits");
				leiji_threadSafeMemoryObjectWaits = (String)statisticsHash.get("leiji_threadSafeMemoryObjectWaits");
				leiji_logWriteWaits = (String)statisticsHash.get("leiji_logWriteWaits");
				leiji_logBufferWaits = (String)statisticsHash.get("leiji_logBufferWaits");
				leiji_networkIOWaits = (String)statisticsHash.get("leiji_networkIOWaits");
				leiji_pageIOLatchWaits = (String)statisticsHash.get("leiji_pageIOLatchWaits");
				leiji_pageLatchWaits = (String)statisticsHash.get("leiji_pageLatchWaits");
				leiji_nonPageLatchWaits = (String)statisticsHash.get("leiji_nonPageLatchWaits");
				leiji_waitForTheWorker = (String)statisticsHash.get("leiji_waitForTheWorker");
				leiji_workspaceSynchronizationWaits = (String)statisticsHash.get("leiji_workspaceSynchronizationWaits");
				leiji_transactionOwnershipWaits = (String)statisticsHash.get("leiji_transactionOwnershipWaits");
			}
			request.setAttribute("bufferCacheHitRatio", bufferCacheHitRatio);
			request.setAttribute("planCacheHitRatio", planCacheHitRatio);
			request.setAttribute("cursorManagerByTypeHitRatio", cursorManagerByTypeHitRatio);
			request.setAttribute("catalogMetadataHitRatio", catalogMetadataHitRatio);
			
			request.setAttribute("dbOfflineErrors", dbOfflineErrors);
			request.setAttribute("killConnectionErrors", killConnectionErrors);
			request.setAttribute("userErrors", userErrors);
			request.setAttribute("infoErrors", infoErrors);
			request.setAttribute("sqlServerErrors_total", sqlServerErrors_total);
			request.setAttribute("cachedCursorCounts", cachedCursorCounts);
			request.setAttribute("cursorCacheUseCounts", cursorCacheUseCounts);
			request.setAttribute("cursorRequests_total", cursorRequests_total);
			request.setAttribute("activeCursors", activeCursors);
			request.setAttribute("cursorMemoryUsage", cursorMemoryUsage);
			request.setAttribute("cursorWorktableUsage", cursorWorktableUsage);
			request.setAttribute("activeOfCursorPlans", activeOfCursorPlans);
			
			request.setAttribute("runstr", runstr);
			
			request.setAttribute("pingjun_lockWaits", pingjun_lockWaits);
			request.setAttribute("pingjun_memoryGrantQueueWaits", pingjun_memoryGrantQueueWaits);
			request.setAttribute("pingjun_threadSafeMemoryObjectWaits", pingjun_threadSafeMemoryObjectWaits);
			request.setAttribute("pingjun_logWriteWaits", pingjun_logWriteWaits);
			request.setAttribute("pingjun_logBufferWaits", pingjun_logBufferWaits);
			request.setAttribute("pingjun_networkIOWaits", pingjun_networkIOWaits);
			request.setAttribute("pingjun_pageIOLatchWaits", pingjun_pageIOLatchWaits);
			request.setAttribute("pingjun_pageLatchWaits", pingjun_pageLatchWaits);
			request.setAttribute("pingjun_nonPageLatchWaits", pingjun_nonPageLatchWaits);
			request.setAttribute("pingjun_waitForTheWorker", pingjun_waitForTheWorker);
			request.setAttribute("pingjun_workspaceSynchronizationWaits", pingjun_workspaceSynchronizationWaits);
			request.setAttribute("pingjun_transactionOwnershipWaits", pingjun_transactionOwnershipWaits);
			
			request.setAttribute("jingxing_lockWaits", jingxing_lockWaits);
			request.setAttribute("jingxing_memoryGrantQueueWaits", jingxing_memoryGrantQueueWaits);
			request.setAttribute("jingxing_threadSafeMemoryObjectWaits", jingxing_threadSafeMemoryObjectWaits);
			request.setAttribute("jingxing_logWriteWaits", jingxing_logWriteWaits);
			request.setAttribute("jingxing_logBufferWaits", jingxing_logBufferWaits);
			request.setAttribute("jingxing_networkIOWaits", jingxing_networkIOWaits);
			request.setAttribute("jingxing_pageIOLatchWaits", jingxing_pageIOLatchWaits);
			request.setAttribute("jingxing_pageLatchWaits", jingxing_pageLatchWaits);
			request.setAttribute("jingxing_nonPageLatchWaits", jingxing_nonPageLatchWaits);
			request.setAttribute("jingxing_waitForTheWorker", jingxing_waitForTheWorker);
			request.setAttribute("jingxing_workspaceSynchronizationWaits", jingxing_workspaceSynchronizationWaits);
			request.setAttribute("jingxing_transactionOwnershipWaits", jingxing_transactionOwnershipWaits);

			request.setAttribute("qidong_lockWaits", qidong_lockWaits);
			request.setAttribute("qidong_memoryGrantQueueWaits", qidong_memoryGrantQueueWaits);
			request.setAttribute("qidong_threadSafeMemoryObjectWaits", qidong_threadSafeMemoryObjectWaits);
			request.setAttribute("qidong_logWriteWaits", qidong_logWriteWaits);
			request.setAttribute("qidong_logBufferWaits", qidong_logBufferWaits);
			request.setAttribute("qidong_networkIOWaits", qidong_networkIOWaits);
			request.setAttribute("qidong_pageIOLatchWaits", qidong_pageIOLatchWaits);
			request.setAttribute("qidong_pageLatchWaits", qidong_pageLatchWaits);
			request.setAttribute("qidong_nonPageLatchWaits", qidong_nonPageLatchWaits);
			request.setAttribute("qidong_waitForTheWorker", qidong_waitForTheWorker);
			request.setAttribute("qidong_workspaceSynchronizationWaits", qidong_workspaceSynchronizationWaits);
			request.setAttribute("qidong_transactionOwnershipWaits", qidong_transactionOwnershipWaits);
			
			request.setAttribute("leiji_lockWaits", leiji_lockWaits);
			request.setAttribute("leiji_memoryGrantQueueWaits", leiji_memoryGrantQueueWaits);
			request.setAttribute("leiji_threadSafeMemoryObjectWaits", leiji_threadSafeMemoryObjectWaits);
			request.setAttribute("leiji_logWriteWaits", leiji_logWriteWaits);
			request.setAttribute("leiji_logBufferWaits", leiji_logBufferWaits);
			request.setAttribute("leiji_networkIOWaits", leiji_networkIOWaits);
			request.setAttribute("leiji_pageIOLatchWaits", leiji_pageIOLatchWaits);
			request.setAttribute("leiji_pageLatchWaits", leiji_pageLatchWaits);
			request.setAttribute("leiji_nonPageLatchWaits", leiji_nonPageLatchWaits);
			request.setAttribute("leiji_waitForTheWorker", leiji_waitForTheWorker);
			request.setAttribute("leiji_workspaceSynchronizationWaits", leiji_workspaceSynchronizationWaits);
			request.setAttribute("leiji_transactionOwnershipWaits", leiji_transactionOwnershipWaits);
			
//			dao = new DBDao();
//			try{
//				sysValue = dao.getSqlServerSys(vo.getIpAddress(),vo.getUser(),vo.getPassword());
//			}catch(Exception e){
//				e.printStackTrace();
//			}finally{
//				dao.close();
//			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"SQLPing","ConnectUtilization",starttime1,totime1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if (ConnectUtilizationhash.get("avgpingcon")!=null)
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
			}
	
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
//				Hashtable allValue = ShareData.getSqlserverdata();			
//				if(allValue != null){
//					Hashtable retValue = (Hashtable)allValue.get(vo.getIpAddress());
//					request.setAttribute("sqlValue", retValue);
//				}
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			request.setAttribute("sqlsys",sysValue);
		        
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/sqlserversys.jsp";
    }

	private String sqlserverdb()
    {    	   
		DBVo vo = new DBVo();
		
		String bufferCacheHitRatio = null;
		String planCacheHitRatio = null;
		String cursorManagerByTypeHitRatio = null;
		String catalogMetadataHitRatio = null;
		
		String dbOfflineErrors = null;
		String killConnectionErrors = null;
		String userErrors = null;
		String infoErrors = null;
		String sqlServerErrors_total = null;
		
		String cachedCursorCounts = null; //cachedCursorCounts_total
		String cursorCacheUseCounts = null;//cursorCacheUseCounts_total
		String cursorRequests_total = null;
		String activeCursors = null;//activeCursors_total
		String cursorMemoryUsage = null;//cursorMemoryUsage_total
		String cursorWorktableUsage = null;//cursorWorktableUsage_total
		String activeOfCursorPlans = null;//Number of active cursor plans_total
		
		Hashtable sysValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		Vector tableinfo_v = new Vector();
		Hashtable dbValue = new Hashtable();
		Hashtable retValue = new Hashtable();
		String pingconavg ="0";
		double avgpingcon = 0;
		String id = getParaValue("id");
		request.setAttribute("id", id);
		try{
			DBDao dao = new DBDao();
			try{
				vo = (DBVo)dao.findByID(id);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}
			
			DBTypeDao typedao = new DBTypeDao();
			DBTypeVo typevo = null;
			try{
				typevo = (DBTypeVo)typedao.findByID(vo.getDbtype()+"");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				typedao.close();
			}
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "<font color=red>����ֹͣ</font>";
//			Hashtable allsqlserverdata = ShareData.getSqlserverdata();
//			Hashtable ipsqlserverdata = new Hashtable();
//			if(allsqlserverdata != null && allsqlserverdata.size()>0){
//				if(allsqlserverdata.containsKey(vo.getIpAddress())){
//					ipsqlserverdata = (Hashtable)allsqlserverdata.get(vo.getIpAddress());
//					if(ipsqlserverdata.containsKey("status")){
//						String p_status = (String)ipsqlserverdata.get("status");
//						if(p_status != null && p_status.length()>0){
//							if("1".equalsIgnoreCase(p_status)){
//								runstr = "��������";
//							}
//						}
//					}
//					if(ipsqlserverdata.containsKey("sysValue")){
//						sysValue = (Hashtable)ipsqlserverdata.get("sysValue");
//					}
//					retValue = (Hashtable)ipsqlserverdata.get("retValue");
//					Hashtable pages = (Hashtable)retValue.get("pages");
			DBDao dbDao = new DBDao();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+vo.getAlias();
			Hashtable sqlValue = new Hashtable();
			Hashtable status = dbDao.getSqlserver_nmsstatus(serverip);
			Hashtable pages =  dbDao.getSqlserver_nmspages(serverip);
			Hashtable statisticsHash = dbDao.getSqlserver_nmsstatisticsHash(serverip);
			sysValue = dbDao.getSqlserver_nmssysvalue(serverip);
			Hashtable mems = dbDao.getSqlserver_nmsmems(serverip);
			Hashtable conns = dao.getSqlserver_nmsconns(serverip);
			Hashtable caches = dao.getSqlserver_nmscaches(serverip);
			Hashtable sqls = dao.getSqlserver_nmssqls(serverip);
			Hashtable scans = dao.getSqlserver_nmsscans(serverip);
			sqlValue.put("pages", pages);
			sqlValue.put("mems", mems);
			sqlValue.put("conns", conns);
			sqlValue.put("caches", caches);
			sqlValue.put("sqls", sqls);
			sqlValue.put("scans", scans);
			
			request.setAttribute("sqlValue", sqlValue);
			request.setAttribute("mems", mems);
			String p_status = (String)status.get("status");
			if(p_status != null && p_status.length()>0){
				if("1".equalsIgnoreCase(p_status)){
					runstr = "��������";
				}
			}
			dao.close();
			if(pages != null && pages.size() > 0){
				bufferCacheHitRatio = (String)pages.get("bufferCacheHitRatio");
				planCacheHitRatio = (String)pages.get("planCacheHitRatio");
				cursorManagerByTypeHitRatio = (String)pages.get("cursorManagerByTypeHitRatio");
				catalogMetadataHitRatio = (String)pages.get("catalogMetadataHitRatio");

				dbOfflineErrors = (String)pages.get("dbOfflineErrors");
				killConnectionErrors = (String)pages.get("killConnectionErrors");
				userErrors = (String)pages.get("userErrors");
				infoErrors = (String)pages.get("infoErrors");
				sqlServerErrors_total = (String)pages.get("sqlServerErrors_total");
				
				cachedCursorCounts = (String)pages.get("cachedCursorCounts"); 
				cursorCacheUseCounts = (String)pages.get("cursorCacheUseCounts");
				cursorRequests_total = (String)pages.get("cursorRequests_total");
				activeCursors = (String)pages.get("activeCursors");
				cursorMemoryUsage = (String)pages.get("cursorMemoryUsage");
				cursorWorktableUsage = (String)pages.get("cursorWorktableUsage");
				activeOfCursorPlans = (String)pages.get("activeOfCursorPlans");
			}
			request.setAttribute("bufferCacheHitRatio", bufferCacheHitRatio);
			request.setAttribute("planCacheHitRatio", planCacheHitRatio);
			request.setAttribute("cursorManagerByTypeHitRatio", cursorManagerByTypeHitRatio);
			request.setAttribute("catalogMetadataHitRatio", catalogMetadataHitRatio);
			
			request.setAttribute("dbOfflineErrors", dbOfflineErrors);
			request.setAttribute("killConnectionErrors", killConnectionErrors);
			request.setAttribute("userErrors", userErrors);
			request.setAttribute("infoErrors", infoErrors);
			request.setAttribute("sqlServerErrors_total", sqlServerErrors_total);
			request.setAttribute("cachedCursorCounts", cachedCursorCounts);
			request.setAttribute("cursorCacheUseCounts", cursorCacheUseCounts);
			request.setAttribute("cursorRequests_total", cursorRequests_total);
			request.setAttribute("activeCursors", activeCursors);
			request.setAttribute("cursorMemoryUsage", cursorMemoryUsage);
			request.setAttribute("cursorWorktableUsage", cursorWorktableUsage);
			request.setAttribute("activeOfCursorPlans", activeOfCursorPlans);
			
			request.setAttribute("runstr", runstr);

			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"SQLPing","ConnectUtilization",starttime1,totime1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if (ConnectUtilizationhash.get("avgpingcon")!=null)
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
			}  
			
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);

				
				
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
//			  Hashtable allValue = ShareData.getSqlserverdata();			
//				if(allValue != null){
//					Hashtable retValue = (Hashtable)allValue.get(vo.getIpAddress());
//					request.setAttribute("sqlValue", retValue);
//				}
				
//				request.setAttribute("sqlValue", retValue);
			request.setAttribute("dbValue",dbValue);
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			request.setAttribute("sqlsys",sysValue);
		        
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/sqlserverdb.jsp";
    }
	
	private String sqlserverproc()
    {    	   
		DBVo vo = new DBVo();
		
		String bufferCacheHitRatio = null;
		String planCacheHitRatio = null;
		String cursorManagerByTypeHitRatio = null;
		String catalogMetadataHitRatio = null;
		
		String dbOfflineErrors = null;
		String killConnectionErrors = null;
		String userErrors = null;
		String infoErrors = null;
		String sqlServerErrors_total = null;
		
		String cachedCursorCounts = null; //cachedCursorCounts_total
		String cursorCacheUseCounts = null;//cursorCacheUseCounts_total
		String cursorRequests_total = null;
		String activeCursors = null;//activeCursors_total
		String cursorMemoryUsage = null;//cursorMemoryUsage_total
		String cursorWorktableUsage = null;//cursorWorktableUsage_total
		String activeOfCursorPlans = null;//Number of active cursor plans_total
		
		Hashtable sysValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		Vector tableinfo_v = new Vector();
		Vector process_v = new Vector();
		String pingconavg ="0";
		double avgpingcon = 0;
		String id = getParaValue("id");
		request.setAttribute("id", id);
		try{
			DBDao dao = new DBDao();
			try{
				vo = (DBVo)dao.findByID(id);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}
			
			DBTypeDao typedao = new DBTypeDao();
			DBTypeVo typevo = null;
			try{
				typevo = (DBTypeVo)typedao.findByID(vo.getDbtype()+"");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				typedao.close();
			}
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "<font color=red>����ֹͣ</font>";
//			Hashtable allsqlserverdata = ShareData.getSqlserverdata();
//			Hashtable ipsqlserverdata = new Hashtable();
//			if(allsqlserverdata != null && allsqlserverdata.size()>0){
//				if(allsqlserverdata.containsKey(vo.getIpAddress())){
//					ipsqlserverdata = (Hashtable)allsqlserverdata.get(vo.getIpAddress());
//					if(ipsqlserverdata.containsKey("status")){
//						String p_status = (String)ipsqlserverdata.get("status");
//						if(p_status != null && p_status.length()>0){
//							if("1".equalsIgnoreCase(p_status)){
//								runstr = "��������";
//							}
//						}
//					}
//					if(ipsqlserverdata.containsKey("sysValue")){
//						sysValue = (Hashtable)ipsqlserverdata.get("sysValue");
//					}
//					if(ipsqlserverdata.containsKey("info_v")){
//						process_v = (Vector)ipsqlserverdata.get("info_v");
//					}
//					Hashtable retValue = (Hashtable)ipsqlserverdata.get("retValue");
//					Hashtable pages = (Hashtable)retValue.get("pages");
			DBDao dbDao = new DBDao();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+vo.getAlias();
			Hashtable sqlValue = new Hashtable();
			Hashtable status = dbDao.getSqlserver_nmsstatus(serverip);
			Hashtable pages =  dbDao.getSqlserver_nmspages(serverip);
			Hashtable statisticsHash = dbDao.getSqlserver_nmsstatisticsHash(serverip);
			sysValue = dbDao.getSqlserver_nmssysvalue(serverip);
//			Hashtable mems = dbDao.getSqlserver_nmsmems(serverip);
//			Hashtable conns = dao.getSqlserver_nmsconns(serverip);
//			Hashtable caches = dao.getSqlserver_nmscaches(serverip);
//			Hashtable sqls = dao.getSqlserver_nmssqls(serverip);
//			Hashtable scans = dao.getSqlserver_nmsscans(serverip);
			process_v = dao.getSqlserver_nmsinfo_v(serverip);
//			sqlValue.put("pages", pages);
//			sqlValue.put("mems", mems);
//			sqlValue.put("conns", conns);
//			sqlValue.put("caches", caches);
//			sqlValue.put("sqls", sqls);
//			sqlValue.put("scans", scans);
			request.setAttribute("sqlValue", sqlValue);
//			request.setAttribute("mems", mems);
			String p_status = (String)status.get("status");
			if(p_status != null && p_status.length()>0){
				if("1".equalsIgnoreCase(p_status)){
					runstr = "��������";
				}
			}
			dao.close();
			if(pages != null && pages.size() > 0){
				bufferCacheHitRatio = (String)pages.get("bufferCacheHitRatio");
				planCacheHitRatio = (String)pages.get("planCacheHitRatio");
				cursorManagerByTypeHitRatio = (String)pages.get("cursorManagerByTypeHitRatio");
				catalogMetadataHitRatio = (String)pages.get("catalogMetadataHitRatio");

				dbOfflineErrors = (String)pages.get("dbOfflineErrors");
				killConnectionErrors = (String)pages.get("killConnectionErrors");
				userErrors = (String)pages.get("userErrors");
				infoErrors = (String)pages.get("infoErrors");
				sqlServerErrors_total = (String)pages.get("sqlServerErrors_total");
				
				cachedCursorCounts = (String)pages.get("cachedCursorCounts"); 
				cursorCacheUseCounts = (String)pages.get("cursorCacheUseCounts");
				cursorRequests_total = (String)pages.get("cursorRequests_total");
				activeCursors = (String)pages.get("activeCursors");
				cursorMemoryUsage = (String)pages.get("cursorMemoryUsage");
				cursorWorktableUsage = (String)pages.get("cursorWorktableUsage");
				activeOfCursorPlans = (String)pages.get("activeOfCursorPlans");
			}
			request.setAttribute("bufferCacheHitRatio", bufferCacheHitRatio);
			request.setAttribute("planCacheHitRatio", planCacheHitRatio);
			request.setAttribute("cursorManagerByTypeHitRatio", cursorManagerByTypeHitRatio);
			request.setAttribute("catalogMetadataHitRatio", catalogMetadataHitRatio);
			
			request.setAttribute("dbOfflineErrors", dbOfflineErrors);
			request.setAttribute("killConnectionErrors", killConnectionErrors);
			request.setAttribute("userErrors", userErrors);
			request.setAttribute("infoErrors", infoErrors);
			request.setAttribute("sqlServerErrors_total", sqlServerErrors_total);
			request.setAttribute("cachedCursorCounts", cachedCursorCounts);
			request.setAttribute("cursorCacheUseCounts", cursorCacheUseCounts);
			request.setAttribute("cursorRequests_total", cursorRequests_total);
			request.setAttribute("activeCursors", activeCursors);
			request.setAttribute("cursorMemoryUsage", cursorMemoryUsage);
			request.setAttribute("cursorWorktableUsage", cursorWorktableUsage);
			request.setAttribute("activeOfCursorPlans", activeOfCursorPlans);
			
			request.setAttribute("runstr", runstr);
			
//			dao = new DBDao();
//			try{
//				sysValue = dao.getSqlServerSys(vo.getIpAddress(),vo.getUser(),vo.getPassword());
//			}catch(Exception e){
//				e.printStackTrace();
//			}finally{
//				dao.close();
//			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"SQLPing","ConnectUtilization",starttime1,totime1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
			if (ConnectUtilizationhash.get("avgpingcon")!=null)
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
			}
			//��ͼ
			//p_draw_line(responsehash,"��ͨ��",newip+"response",750,200);
			//p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);

			//pingƽ��ֵ
			//maxhash = new Hashtable();
			//maxhash.put("avgpingcon",pingconavg);

			//imgurlhash
			//imgurlhash.put("ConnectUtilization","resource/image/jfreechart/"+newip+"ConnectUtilization"+".png");    
			
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  
//			  dao = new DBDao();
//			  try{
//				//�õ����ݿ�Ľ�����Ϣ
//				  process_v = dao.getSqlserverProcesses(vo.getIpAddress(),vo.getUser(),vo.getPassword());
//			  }catch(Exception e){
//				  e.printStackTrace();
//			  }finally{
//					dao.close();
//				}
			  
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
//				Hashtable allValue = ShareData.getSqlserverdata();			
//				if(allValue != null){
//					Hashtable retValue = (Hashtable)allValue.get(vo.getIpAddress());
//					request.setAttribute("sqlValue", retValue);
//				}
			request.setAttribute("process_v",process_v);
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			request.setAttribute("sqlsys",sysValue);
		        
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/sqlserverproc.jsp";
    }
	
	private String sqlserverlock()
    {    	   
		DBVo vo = new DBVo();
		
		String bufferCacheHitRatio = null;
		String planCacheHitRatio = null;
		String cursorManagerByTypeHitRatio = null;
		String catalogMetadataHitRatio = null;
		
		String dbOfflineErrors = null;
		String killConnectionErrors = null;
		String userErrors = null;
		String infoErrors = null;
		String sqlServerErrors_total = null;
		
		String cachedCursorCounts = null; //cachedCursorCounts_total
		String cursorCacheUseCounts = null;//cursorCacheUseCounts_total
		String cursorRequests_total = null;
		String activeCursors = null;//activeCursors_total
		String cursorMemoryUsage = null;//cursorMemoryUsage_total
		String cursorWorktableUsage = null;//cursorWorktableUsage_total
		String activeOfCursorPlans = null;//Number of active cursor plans_total
		
		Hashtable sysValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		Vector tableinfo_v = new Vector();
		Vector lockinfo_v = new Vector();
		Hashtable retValue = new Hashtable();
		String pingconavg ="0";
		double avgpingcon = 0;
		String id = getParaValue("id");
		request.setAttribute("id", id);
		try{
			DBDao dao = new DBDao();
			try{
				vo = (DBVo)dao.findByID(id);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}
			
			DBTypeDao typedao = new DBTypeDao();
			DBTypeVo typevo = null;
			try{
				typevo = (DBTypeVo)typedao.findByID(vo.getDbtype()+"");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				typedao.close();
			}
			
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "<font color=red>����ֹͣ</font>";
			
//			Hashtable allsqlserverdata = ShareData.getSqlserverdata();
//			Hashtable ipsqlserverdata = new Hashtable();
//			if(allsqlserverdata != null && allsqlserverdata.size()>0){
//				if(allsqlserverdata.containsKey(vo.getIpAddress())){
//					ipsqlserverdata = (Hashtable)allsqlserverdata.get(vo.getIpAddress());
//					if(ipsqlserverdata.containsKey("status")){
//						String p_status = (String)ipsqlserverdata.get("status");
//						if(p_status != null && p_status.length()>0){
//							if("1".equalsIgnoreCase(p_status)){
//								runstr = "��������";
//							}
//						}
//					}
//					if(ipsqlserverdata.containsKey("sysValue")){
//						sysValue = (Hashtable)ipsqlserverdata.get("sysValue");
//					}
//					if(ipsqlserverdata.containsKey("lockinfo_v")){
//						lockinfo_v = (Vector)ipsqlserverdata.get("lockinfo_v");
//					}
//					retValue = (Hashtable)ipsqlserverdata.get("retValue");
//					Hashtable pages = (Hashtable)retValue.get("pages");
			DBDao dbDao = new DBDao();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+vo.getAlias();
			Hashtable sqlValue = new Hashtable();
			Hashtable status = dbDao.getSqlserver_nmsstatus(serverip);
			Hashtable pages =  dbDao.getSqlserver_nmspages(serverip);
			Hashtable statisticsHash = dbDao.getSqlserver_nmsstatisticsHash(serverip);
			sysValue = dbDao.getSqlserver_nmssysvalue(serverip);
			lockinfo_v = dbDao.getSqlserver_nmslockinfo_v(serverip);
			Hashtable locks = dbDao.getSqlserver_nmslocks(serverip);
//			Hashtable mems = dbDao.getSqlserver_nmsmems(serverip);
//			Hashtable conns = dao.getSqlserver_nmsconns(serverip);
			Hashtable caches = dao.getSqlserver_nmscaches(serverip);
//			Hashtable sqls = dao.getSqlserver_nmssqls(serverip);
//			Hashtable scans = dao.getSqlserver_nmsscans(serverip);
//			process_v = dao.getSqlserver_nmsinfo_v(serverip);
//			sqlValue.put("pages", pages);
//			sqlValue.put("mems", mems);
//			sqlValue.put("conns", conns);
			sqlValue.put("caches", caches);
			sqlValue.put("locks", locks);
//			sqlValue.put("sqls", sqls);
//			sqlValue.put("scans", scans);
			request.setAttribute("sqlValue", sqlValue);
//			request.setAttribute("mems", mems);
			String p_status = (String)status.get("status");
			if(p_status != null && p_status.length()>0){
				if("1".equalsIgnoreCase(p_status)){
					runstr = "��������";
				}
			}
			dao.close();
			if(pages != null && pages.size() > 0){
				bufferCacheHitRatio = (String)pages.get("bufferCacheHitRatio");
				planCacheHitRatio = (String)pages.get("planCacheHitRatio");
				cursorManagerByTypeHitRatio = (String)pages.get("cursorManagerByTypeHitRatio");
				catalogMetadataHitRatio = (String)pages.get("catalogMetadataHitRatio");

				dbOfflineErrors = (String)pages.get("dbOfflineErrors");
				killConnectionErrors = (String)pages.get("killConnectionErrors");
				userErrors = (String)pages.get("userErrors");
				infoErrors = (String)pages.get("infoErrors");
				sqlServerErrors_total = (String)pages.get("sqlServerErrors_total");
				
				cachedCursorCounts = (String)pages.get("cachedCursorCounts"); 
				cursorCacheUseCounts = (String)pages.get("cursorCacheUseCounts");
				cursorRequests_total = (String)pages.get("cursorRequests_total");
				activeCursors = (String)pages.get("activeCursors");
				cursorMemoryUsage = (String)pages.get("cursorMemoryUsage");
				cursorWorktableUsage = (String)pages.get("cursorWorktableUsage");
				activeOfCursorPlans = (String)pages.get("activeOfCursorPlans");
			}
			request.setAttribute("bufferCacheHitRatio", bufferCacheHitRatio);
			request.setAttribute("planCacheHitRatio", planCacheHitRatio);
			request.setAttribute("cursorManagerByTypeHitRatio", cursorManagerByTypeHitRatio);
			request.setAttribute("catalogMetadataHitRatio", catalogMetadataHitRatio);
			
			request.setAttribute("dbOfflineErrors", dbOfflineErrors);
			request.setAttribute("killConnectionErrors", killConnectionErrors);
			request.setAttribute("userErrors", userErrors);
			request.setAttribute("infoErrors", infoErrors);
			request.setAttribute("sqlServerErrors_total", sqlServerErrors_total);
			request.setAttribute("cachedCursorCounts", cachedCursorCounts);
			request.setAttribute("cursorCacheUseCounts", cursorCacheUseCounts);
			request.setAttribute("cursorRequests_total", cursorRequests_total);
			request.setAttribute("activeCursors", activeCursors);
			request.setAttribute("cursorMemoryUsage", cursorMemoryUsage);
			request.setAttribute("cursorWorktableUsage", cursorWorktableUsage);
			request.setAttribute("activeOfCursorPlans", activeOfCursorPlans);
			
			
			request.setAttribute("runstr", runstr);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"SQLPing","ConnectUtilization",starttime1,totime1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if (ConnectUtilizationhash.get("avgpingcon")!=null)
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
			}
			//��ͼ
			//p_draw_line(responsehash,"��ͨ��",newip+"response",750,200);
			//p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);

			//pingƽ��ֵ
			//maxhash = new Hashtable();
			//maxhash.put("avgpingcon",pingconavg);

			//imgurlhash
			//imgurlhash.put("ConnectUtilization","resource/image/jfreechart/"+newip+"ConnectUtilization"+".png");    
			
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 

//				request.setAttribute("sqlValue", retValue);
				request.setAttribute("lockinfo_v",lockinfo_v);
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			request.setAttribute("sqlsys",sysValue);
			request.setAttribute("rsc_type_ht",rsc_type_ht); 
			request.setAttribute("req_mode_ht",req_mode_ht);
			request.setAttribute("mode_ht",mode_ht);
			request.setAttribute("req_status_ht",req_status_ht);
			request.setAttribute("req_ownertype_ht",req_ownertype_ht);
		        
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/sqlserverlock.jsp";
    }
	
	private String sqlserverwait()
    {    	   
		DBVo vo = new DBVo();
		
		String bufferCacheHitRatio = null;
		String planCacheHitRatio = null;
		String cursorManagerByTypeHitRatio = null;
		String catalogMetadataHitRatio = null;
		
		String dbOfflineErrors = null;
		String killConnectionErrors = null;
		String userErrors = null;
		String infoErrors = null;
		String sqlServerErrors_total = null;
		
		String cachedCursorCounts = null; //cachedCursorCounts_total
		String cursorCacheUseCounts = null;//cursorCacheUseCounts_total
		String cursorRequests_total = null;
		String activeCursors = null;//activeCursors_total
		String cursorMemoryUsage = null;//cursorMemoryUsage_total
		String cursorWorktableUsage = null;//cursorWorktableUsage_total
		String activeOfCursorPlans = null;//Number of active cursor plans_total
		
		Hashtable sysValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		Vector tableinfo_v = new Vector();
		Vector lockinfo_v = new Vector();
		Hashtable retValue = new Hashtable();
		String pingconavg ="0";
		double avgpingcon = 0;
		String id = getParaValue("id");
		request.setAttribute("id", id);
		
		String pingjun_lockWaits = "";
		String pingjun_memoryGrantQueueWaits = "";
		String pingjun_threadSafeMemoryObjectWaits = "";
		String pingjun_logWriteWaits = "";
		String pingjun_logBufferWaits = "";
		String pingjun_networkIOWaits = "";
		String pingjun_pageIOLatchWaits = "";
		String pingjun_pageLatchWaits = "";
		String pingjun_nonPageLatchWaits = "";
		String pingjun_waitForTheWorker = "";
		String pingjun_workspaceSynchronizationWaits = "";
		String pingjun_transactionOwnershipWaits = "";
				
		String jingxing_lockWaits = "";
		String jingxing_memoryGrantQueueWaits = "";
		String jingxing_threadSafeMemoryObjectWaits = "";
		String jingxing_logWriteWaits = "";
		String jingxing_logBufferWaits = "";
		String jingxing_networkIOWaits = "";
		String jingxing_pageIOLatchWaits = "";
		String jingxing_pageLatchWaits = "";
		String jingxing_nonPageLatchWaits = "";
		String jingxing_waitForTheWorker = "";
		String jingxing_workspaceSynchronizationWaits = "";
		String jingxing_transactionOwnershipWaits = "";

		String qidong_lockWaits = "";
		String qidong_memoryGrantQueueWaits = "";
		String qidong_threadSafeMemoryObjectWaits = "";
		String qidong_logWriteWaits = "";
		String qidong_logBufferWaits = "";
		String qidong_networkIOWaits = "";
		String qidong_pageIOLatchWaits = "";
		String qidong_pageLatchWaits = "";
		String qidong_nonPageLatchWaits = "";
		String qidong_waitForTheWorker = "";
		String qidong_workspaceSynchronizationWaits = "";
		String qidong_transactionOwnershipWaits = "";
				
		String leiji_lockWaits = "";
		String leiji_memoryGrantQueueWaits = "";
		String leiji_threadSafeMemoryObjectWaits = "";
		String leiji_logWriteWaits = "";
		String leiji_logBufferWaits = "";
		String leiji_networkIOWaits = "";
		String leiji_pageIOLatchWaits = "";
		String leiji_pageLatchWaits = "";
		String leiji_nonPageLatchWaits = "";
		String leiji_waitForTheWorker = "";
		String leiji_workspaceSynchronizationWaits = "";
		String leiji_transactionOwnershipWaits = "";		
		
		
		
		
		try{
			DBDao dao = new DBDao();
			try{
				vo = (DBVo)dao.findByID(id);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}
			
			DBTypeDao typedao = new DBTypeDao();
			DBTypeVo typevo = null;
			try{
				typevo = (DBTypeVo)typedao.findByID(vo.getDbtype()+"");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				typedao.close();
			}
			
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "<font color=red>����ֹͣ</font>";
			
//			Hashtable allsqlserverdata = ShareData.getSqlserverdata();
//			Hashtable ipsqlserverdata = new Hashtable();
//			if(allsqlserverdata != null && allsqlserverdata.size()>0){
//				if(allsqlserverdata.containsKey(vo.getIpAddress())){
//					ipsqlserverdata = (Hashtable)allsqlserverdata.get(vo.getIpAddress());
//					if(ipsqlserverdata.containsKey("status")){
//						String p_status = (String)ipsqlserverdata.get("status");
//						if(p_status != null && p_status.length()>0){
//							if("1".equalsIgnoreCase(p_status)){
//								runstr = "��������";
//							}
//						}
//					}
//					if(ipsqlserverdata.containsKey("sysValue")){
//						sysValue = (Hashtable)ipsqlserverdata.get("sysValue");
//					}
//					retValue = (Hashtable)ipsqlserverdata.get("retValue");
//					Hashtable statisticsHash = (Hashtable)retValue.get("statisticsHash");
//					if(statisticsHash == null)statisticsHash = new Hashtable();
			DBDao dbDao = new DBDao();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+vo.getAlias();
			Hashtable sqlValue = new Hashtable();
			Hashtable status = dbDao.getSqlserver_nmsstatus(serverip);
			Hashtable pages =  dbDao.getSqlserver_nmspages(serverip);
			Hashtable statisticsHash = dbDao.getSqlserver_nmsstatisticsHash(serverip);
			sysValue = dbDao.getSqlserver_nmssysvalue(serverip);
			lockinfo_v = dbDao.getSqlserver_nmslockinfo_v(serverip);
//			Hashtable locks = dbDao.getSqlserver_nmslocks(serverip);
//			Hashtable mems = dbDao.getSqlserver_nmsmems(serverip);
//			Hashtable conns = dao.getSqlserver_nmsconns(serverip);
//			Hashtable caches = dao.getSqlserver_nmscaches(serverip);
//			Hashtable sqls = dao.getSqlserver_nmssqls(serverip);
//			Hashtable scans = dao.getSqlserver_nmsscans(serverip);
//			process_v = dao.getSqlserver_nmsinfo_v(serverip);
//			sqlValue.put("pages", pages);
//			sqlValue.put("mems", mems);
//			sqlValue.put("conns", conns);
//			sqlValue.put("caches", caches);
//			sqlValue.put("locks", locks);
//			sqlValue.put("sqls", sqls);
//			sqlValue.put("scans", scans);
			request.setAttribute("sqlValue", sqlValue);
//			request.setAttribute("mems", mems);
			String p_status = (String)status.get("status");
			if(p_status != null && p_status.length()>0){
				if("1".equalsIgnoreCase(p_status)){
					runstr = "��������";
				}
			}
			dao.close();
			if(statisticsHash != null && statisticsHash.size() > 0){
					pingjun_lockWaits = (String)statisticsHash.get("pingjun_lockWaits");
					pingjun_memoryGrantQueueWaits = (String)statisticsHash.get("pingjun_memoryGrantQueueWaits");
					pingjun_threadSafeMemoryObjectWaits = (String)statisticsHash.get("pingjun_threadSafeMemoryObjectWaits");
					pingjun_logWriteWaits = (String)statisticsHash.get("pingjun_logWriteWaits");
					pingjun_logBufferWaits = (String)statisticsHash.get("pingjun_logBufferWaits");
					pingjun_networkIOWaits = (String)statisticsHash.get("pingjun_networkIOWaits");
					pingjun_pageIOLatchWaits = (String)statisticsHash.get("pingjun_pageIOLatchWaits");
					pingjun_pageLatchWaits = (String)statisticsHash.get("pingjun_pageLatchWaits");
					pingjun_nonPageLatchWaits = (String)statisticsHash.get("pingjun_nonPageLatchWaits");
					pingjun_waitForTheWorker = (String)statisticsHash.get("pingjun_waitForTheWorker");
					pingjun_workspaceSynchronizationWaits = (String)statisticsHash.get("pingjun_workspaceSynchronizationWaits");
					pingjun_transactionOwnershipWaits = (String)statisticsHash.get("pingjun_transactionOwnershipWaits");
					
					jingxing_lockWaits= (String)statisticsHash.get("jingxing_lockWaits");
					jingxing_memoryGrantQueueWaits = (String)statisticsHash.get("jingxing_memoryGrantQueueWaits");
					jingxing_threadSafeMemoryObjectWaits = (String)statisticsHash.get("jingxing_threadSafeMemoryObjectWaits");
					jingxing_logWriteWaits = (String)statisticsHash.get("jingxing_logWriteWaits");
					jingxing_logBufferWaits = (String)statisticsHash.get("jingxing_logBufferWaits");
					jingxing_networkIOWaits = (String)statisticsHash.get("jingxing_networkIOWaits");
					jingxing_pageIOLatchWaits = (String)statisticsHash.get("jingxing_pageIOLatchWaits");
					jingxing_pageLatchWaits =(String)statisticsHash.get("jingxing_pageLatchWaits");
					jingxing_nonPageLatchWaits = (String)statisticsHash.get("jingxing_nonPageLatchWaits");
					jingxing_waitForTheWorker = (String)statisticsHash.get("jingxing_waitForTheWorker");
					jingxing_workspaceSynchronizationWaits = (String)statisticsHash.get("jingxing_workspaceSynchronizationWaits");
					jingxing_transactionOwnershipWaits = (String)statisticsHash.get("jingxing_transactionOwnershipWaits");
					
					qidong_lockWaits = (String)statisticsHash.get("qidong_lockWaits");
					qidong_memoryGrantQueueWaits = (String)statisticsHash.get("qidong_memoryGrantQueueWaits");
					qidong_threadSafeMemoryObjectWaits = (String)statisticsHash.get("qidong_threadSafeMemoryObjectWaits");
					qidong_logWriteWaits = (String)statisticsHash.get("qidong_logWriteWaits");
					qidong_logBufferWaits = (String)statisticsHash.get("qidong_logBufferWaits");
					qidong_networkIOWaits = (String)statisticsHash.get("qidong_networkIOWaits");
					qidong_pageIOLatchWaits= (String)statisticsHash.get("qidong_pageIOLatchWaits");
					qidong_pageLatchWaits= (String)statisticsHash.get("qidong_pageLatchWaits");
					qidong_nonPageLatchWaits = (String)statisticsHash.get("qidong_nonPageLatchWaits");
					qidong_waitForTheWorker = (String)statisticsHash.get("qidong_waitForTheWorker");
					qidong_workspaceSynchronizationWaits = (String)statisticsHash.get("qidong_workspaceSynchronizationWaits");
					qidong_transactionOwnershipWaits = (String)statisticsHash.get("qidong_transactionOwnershipWaits");
					
					leiji_lockWaits = (String)statisticsHash.get("leiji_lockWaits");
					leiji_memoryGrantQueueWaits = (String)statisticsHash.get("leiji_memoryGrantQueueWaits");
					leiji_threadSafeMemoryObjectWaits = (String)statisticsHash.get("leiji_threadSafeMemoryObjectWaits");
					leiji_logWriteWaits = (String)statisticsHash.get("leiji_logWriteWaits");
					leiji_logBufferWaits = (String)statisticsHash.get("leiji_logBufferWaits");
					leiji_networkIOWaits = (String)statisticsHash.get("leiji_networkIOWaits");
					leiji_pageIOLatchWaits = (String)statisticsHash.get("leiji_pageIOLatchWaits");
					leiji_pageLatchWaits = (String)statisticsHash.get("leiji_pageLatchWaits");
					leiji_nonPageLatchWaits = (String)statisticsHash.get("leiji_nonPageLatchWaits");
					leiji_waitForTheWorker = (String)statisticsHash.get("leiji_waitForTheWorker");
					leiji_workspaceSynchronizationWaits = (String)statisticsHash.get("leiji_workspaceSynchronizationWaits");
					leiji_transactionOwnershipWaits = (String)statisticsHash.get("leiji_transactionOwnershipWaits");
			}		
					
//					Hashtable pages = (Hashtable)retValue.get("pages");
			if(pages != null && pages.size() > 0){
					bufferCacheHitRatio = (String)pages.get("bufferCacheHitRatio");
					planCacheHitRatio = (String)pages.get("planCacheHitRatio");
					cursorManagerByTypeHitRatio = (String)pages.get("cursorManagerByTypeHitRatio");
					catalogMetadataHitRatio = (String)pages.get("catalogMetadataHitRatio");

					dbOfflineErrors = (String)pages.get("dbOfflineErrors");
					killConnectionErrors = (String)pages.get("killConnectionErrors");
					userErrors = (String)pages.get("userErrors");
					infoErrors = (String)pages.get("infoErrors");
					sqlServerErrors_total = (String)pages.get("sqlServerErrors_total");
					
					cachedCursorCounts = (String)pages.get("cachedCursorCounts"); 
					cursorCacheUseCounts = (String)pages.get("cursorCacheUseCounts");
					cursorRequests_total = (String)pages.get("cursorRequests_total");
					activeCursors = (String)pages.get("activeCursors");
					cursorMemoryUsage = (String)pages.get("cursorMemoryUsage");
					cursorWorktableUsage = (String)pages.get("cursorWorktableUsage");
					activeOfCursorPlans = (String)pages.get("activeOfCursorPlans");
			}
			request.setAttribute("bufferCacheHitRatio", bufferCacheHitRatio);
			request.setAttribute("planCacheHitRatio", planCacheHitRatio);
			request.setAttribute("cursorManagerByTypeHitRatio", cursorManagerByTypeHitRatio);
			request.setAttribute("catalogMetadataHitRatio", catalogMetadataHitRatio);
			
			request.setAttribute("dbOfflineErrors", dbOfflineErrors);
			request.setAttribute("killConnectionErrors", killConnectionErrors);
			request.setAttribute("userErrors", userErrors);
			request.setAttribute("infoErrors", infoErrors);
			request.setAttribute("sqlServerErrors_total", sqlServerErrors_total);
			request.setAttribute("cachedCursorCounts", cachedCursorCounts);
			request.setAttribute("cursorCacheUseCounts", cursorCacheUseCounts);
			request.setAttribute("cursorRequests_total", cursorRequests_total);
			request.setAttribute("activeCursors", activeCursors);
			request.setAttribute("cursorMemoryUsage", cursorMemoryUsage);
			request.setAttribute("cursorWorktableUsage", cursorWorktableUsage);
			request.setAttribute("activeOfCursorPlans", activeOfCursorPlans);
			
			
			request.setAttribute("runstr", runstr);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"SQLPing","ConnectUtilization",starttime1,totime1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if (ConnectUtilizationhash.get("avgpingcon")!=null)
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
			}
			//��ͼ
			//p_draw_line(responsehash,"��ͨ��",newip+"response",750,200);
			//p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);

			//pingƽ��ֵ
			//maxhash = new Hashtable();
			//maxhash.put("avgpingcon",pingconavg);

			//imgurlhash
			//imgurlhash.put("ConnectUtilization","resource/image/jfreechart/"+newip+"ConnectUtilization"+".png");    
			
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 

//				request.setAttribute("sqlValue", retValue);
				request.setAttribute("lockinfo_v",lockinfo_v);
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			request.setAttribute("sqlsys",sysValue);
			request.setAttribute("rsc_type_ht",rsc_type_ht); 
			request.setAttribute("req_mode_ht",req_mode_ht);
			request.setAttribute("mode_ht",mode_ht);
			request.setAttribute("req_status_ht",req_status_ht);
			request.setAttribute("req_ownertype_ht",req_ownertype_ht);
			
			request.setAttribute("pingjun_lockWaits", pingjun_lockWaits);
			request.setAttribute("pingjun_memoryGrantQueueWaits", pingjun_memoryGrantQueueWaits);
			request.setAttribute("pingjun_threadSafeMemoryObjectWaits", pingjun_threadSafeMemoryObjectWaits);
			request.setAttribute("pingjun_logWriteWaits", pingjun_logWriteWaits);
			request.setAttribute("pingjun_logBufferWaits", pingjun_logBufferWaits);
			request.setAttribute("pingjun_networkIOWaits", pingjun_networkIOWaits);
			request.setAttribute("pingjun_pageIOLatchWaits", pingjun_pageIOLatchWaits);
			request.setAttribute("pingjun_pageLatchWaits", pingjun_pageLatchWaits);
			request.setAttribute("pingjun_nonPageLatchWaits", pingjun_nonPageLatchWaits);
			request.setAttribute("pingjun_waitForTheWorker", pingjun_waitForTheWorker);
			request.setAttribute("pingjun_workspaceSynchronizationWaits", pingjun_workspaceSynchronizationWaits);
			request.setAttribute("pingjun_transactionOwnershipWaits", pingjun_transactionOwnershipWaits);
			
			request.setAttribute("jingxing_lockWaits", jingxing_lockWaits);
			request.setAttribute("jingxing_memoryGrantQueueWaits", jingxing_memoryGrantQueueWaits);
			request.setAttribute("jingxing_threadSafeMemoryObjectWaits", jingxing_threadSafeMemoryObjectWaits);
			request.setAttribute("jingxing_logWriteWaits", jingxing_logWriteWaits);
			request.setAttribute("jingxing_logBufferWaits", jingxing_logBufferWaits);
			request.setAttribute("jingxing_networkIOWaits", jingxing_networkIOWaits);
			request.setAttribute("jingxing_pageIOLatchWaits", jingxing_pageIOLatchWaits);
			request.setAttribute("jingxing_pageLatchWaits", jingxing_pageLatchWaits);
			request.setAttribute("jingxing_nonPageLatchWaits", jingxing_nonPageLatchWaits);
			request.setAttribute("jingxing_waitForTheWorker", jingxing_waitForTheWorker);
			request.setAttribute("jingxing_workspaceSynchronizationWaits", jingxing_workspaceSynchronizationWaits);
			request.setAttribute("jingxing_transactionOwnershipWaits", jingxing_transactionOwnershipWaits);

			request.setAttribute("qidong_lockWaits", qidong_lockWaits);
			request.setAttribute("qidong_memoryGrantQueueWaits", qidong_memoryGrantQueueWaits);
			request.setAttribute("qidong_threadSafeMemoryObjectWaits", qidong_threadSafeMemoryObjectWaits);
			request.setAttribute("qidong_logWriteWaits", qidong_logWriteWaits);
			request.setAttribute("qidong_logBufferWaits", qidong_logBufferWaits);
			request.setAttribute("qidong_networkIOWaits", qidong_networkIOWaits);
			request.setAttribute("qidong_pageIOLatchWaits", qidong_pageIOLatchWaits);
			request.setAttribute("qidong_pageLatchWaits", qidong_pageLatchWaits);
			request.setAttribute("qidong_nonPageLatchWaits", qidong_nonPageLatchWaits);
			request.setAttribute("qidong_waitForTheWorker", qidong_waitForTheWorker);
			request.setAttribute("qidong_workspaceSynchronizationWaits", qidong_workspaceSynchronizationWaits);
			request.setAttribute("qidong_transactionOwnershipWaits", qidong_transactionOwnershipWaits);
			
			request.setAttribute("leiji_lockWaits", leiji_lockWaits);
			request.setAttribute("leiji_memoryGrantQueueWaits", leiji_memoryGrantQueueWaits);
			request.setAttribute("leiji_threadSafeMemoryObjectWaits", leiji_threadSafeMemoryObjectWaits);
			request.setAttribute("leiji_logWriteWaits", leiji_logWriteWaits);
			request.setAttribute("leiji_logBufferWaits", leiji_logBufferWaits);
			request.setAttribute("leiji_networkIOWaits", leiji_networkIOWaits);
			request.setAttribute("leiji_pageIOLatchWaits", leiji_pageIOLatchWaits);
			request.setAttribute("leiji_pageLatchWaits", leiji_pageLatchWaits);
			request.setAttribute("leiji_nonPageLatchWaits", leiji_nonPageLatchWaits);
			request.setAttribute("leiji_waitForTheWorker", leiji_waitForTheWorker);
			request.setAttribute("leiji_workspaceSynchronizationWaits", leiji_workspaceSynchronizationWaits);
			request.setAttribute("leiji_transactionOwnershipWaits", leiji_transactionOwnershipWaits);		
		        
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/sqlserverwait.jsp";
    }

	private String sqlserverevent()
    {    	   
		DBVo vo = new DBVo();
		
		String bufferCacheHitRatio = null;
		String planCacheHitRatio = null;
		String cursorManagerByTypeHitRatio = null;
		String catalogMetadataHitRatio = null;
		
		String dbOfflineErrors = null;
		String killConnectionErrors = null;
		String userErrors = null;
		String infoErrors = null;
		String sqlServerErrors_total = null;
		
		String cachedCursorCounts = null; //cachedCursorCounts_total
		String cursorCacheUseCounts = null;//cursorCacheUseCounts_total
		String cursorRequests_total = null;
		String activeCursors = null;//activeCursors_total
		String cursorMemoryUsage = null;//cursorMemoryUsage_total
		String cursorWorktableUsage = null;//cursorWorktableUsage_total
		String activeOfCursorPlans = null;//Number of active cursor plans_total
		
		Hashtable sysValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		String pingconavg ="0";
		double avgpingcon = 0;
		String id = getParaValue("id");
		request.setAttribute("id", id);
		List list = new ArrayList();
		int status =99;
		int level1 = 99;
		String b_time ="";
		String t_time = "";
		String flag = getParaValue("flag");
		request.setAttribute("flag",flag);
		try{
			DBDao dao = new DBDao();
			try{
				vo = (DBVo)dao.findByID(id);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}
			
			DBTypeDao typedao = new DBTypeDao();
			DBTypeVo typevo = null;
			try{
				typevo = (DBTypeVo)typedao.findByID(vo.getDbtype()+"");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				typedao.close();
			}
			
			request.setAttribute("db", vo);
			request.setAttribute("dbtye", typevo.getDbdesc());
			String managed = "������";
			if(vo.getManaged() == 0)managed = "δ����";
			request.setAttribute("managed", managed);
			String runstr = "<font color=red>����ֹͣ</font>";
			
//			Hashtable allsqlserverdata = ShareData.getSqlserverdata();
//			Hashtable ipsqlserverdata = new Hashtable();
//			if(allsqlserverdata != null && allsqlserverdata.size()>0){
//				if(allsqlserverdata.containsKey(vo.getIpAddress())){
//					ipsqlserverdata = (Hashtable)allsqlserverdata.get(vo.getIpAddress());
//					if(ipsqlserverdata.containsKey("status")){
//						String p_status = (String)ipsqlserverdata.get("status");
//						if(p_status != null && p_status.length()>0){
//							if("1".equalsIgnoreCase(p_status)){
//								runstr = "��������";
//							}
//						}
//					}
//					if(ipsqlserverdata.containsKey("sysValue")){
//						sysValue = (Hashtable)ipsqlserverdata.get("sysValue");
//					}
//					Hashtable retValue = (Hashtable)ipsqlserverdata.get("retValue");
//					Hashtable pages = (Hashtable)retValue.get("pages");
			DBDao dbDao = new DBDao();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+vo.getAlias();
			Hashtable sqlValue = new Hashtable();
			Hashtable statusHash = dbDao.getSqlserver_nmsstatus(serverip);
			Hashtable pages =  dbDao.getSqlserver_nmspages(serverip);
			Hashtable statisticsHash = dbDao.getSqlserver_nmsstatisticsHash(serverip);
			sysValue = dbDao.getSqlserver_nmssysvalue(serverip);
//			lockinfo_v = dbDao.getSqlserver_nmslockinfo_v(serverip);
//			Hashtable locks = dbDao.getSqlserver_nmslocks(serverip);
//			Hashtable mems = dbDao.getSqlserver_nmsmems(serverip);
//			Hashtable conns = dao.getSqlserver_nmsconns(serverip);
//			Hashtable caches = dao.getSqlserver_nmscaches(serverip);
//			Hashtable sqls = dao.getSqlserver_nmssqls(serverip);
//			Hashtable scans = dao.getSqlserver_nmsscans(serverip);
//			process_v = dao.getSqlserver_nmsinfo_v(serverip);
//			sqlValue.put("pages", pages);
//			sqlValue.put("mems", mems);
//			sqlValue.put("conns", conns);
//			sqlValue.put("caches", caches);
//			sqlValue.put("locks", locks);
//			sqlValue.put("sqls", sqls);
//			sqlValue.put("scans", scans);
			request.setAttribute("sqlValue", sqlValue);
//			request.setAttribute("mems", mems);
			String p_status = (String)statusHash.get("status");
			if(p_status != null && p_status.length()>0){
				if("1".equalsIgnoreCase(p_status)){
					runstr = "��������";
				}
			}
			dao.close();
			if(pages != null && pages.size() > 0){
				bufferCacheHitRatio = (String)pages.get("bufferCacheHitRatio");
				planCacheHitRatio = (String)pages.get("planCacheHitRatio");
				cursorManagerByTypeHitRatio = (String)pages.get("cursorManagerByTypeHitRatio");
				catalogMetadataHitRatio = (String)pages.get("catalogMetadataHitRatio");

				dbOfflineErrors = (String)pages.get("dbOfflineErrors");
				killConnectionErrors = (String)pages.get("killConnectionErrors");
				userErrors = (String)pages.get("userErrors");
				infoErrors = (String)pages.get("infoErrors");
				sqlServerErrors_total = (String)pages.get("sqlServerErrors_total");
				
				cachedCursorCounts = (String)pages.get("cachedCursorCounts"); 
				cursorCacheUseCounts = (String)pages.get("cursorCacheUseCounts");
				cursorRequests_total = (String)pages.get("cursorRequests_total");
				activeCursors = (String)pages.get("activeCursors");
				cursorMemoryUsage = (String)pages.get("cursorMemoryUsage");
				cursorWorktableUsage = (String)pages.get("cursorWorktableUsage");
				activeOfCursorPlans = (String)pages.get("activeOfCursorPlans");
			}
			request.setAttribute("bufferCacheHitRatio", bufferCacheHitRatio);
			request.setAttribute("planCacheHitRatio", planCacheHitRatio);
			request.setAttribute("cursorManagerByTypeHitRatio", cursorManagerByTypeHitRatio);
			request.setAttribute("catalogMetadataHitRatio", catalogMetadataHitRatio);
			
			request.setAttribute("dbOfflineErrors", dbOfflineErrors);
			request.setAttribute("killConnectionErrors", killConnectionErrors);
			request.setAttribute("userErrors", userErrors);
			request.setAttribute("infoErrors", infoErrors);
			request.setAttribute("sqlServerErrors_total", sqlServerErrors_total);
			request.setAttribute("cachedCursorCounts", cachedCursorCounts);
			request.setAttribute("cursorCacheUseCounts", cursorCacheUseCounts);
			request.setAttribute("cursorRequests_total", cursorRequests_total);
			request.setAttribute("activeCursors", activeCursors);
			request.setAttribute("cursorMemoryUsage", cursorMemoryUsage);
			request.setAttribute("cursorWorktableUsage", cursorWorktableUsage);
			request.setAttribute("activeOfCursorPlans", activeOfCursorPlans);
			
			request.setAttribute("runstr", runstr);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			String strStartDay = getParaValue("startdate");
			String strToDay = getParaValue("todate");
			if(strStartDay!=null && !"".equals(strStartDay)){
			starttime1 = strStartDay+" 00:00:00";
			}
			if(strToDay!=null && !"".equals(strToDay)){
			totime1 = strToDay+" 23:59:59";
			}

			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"SQLPing","ConnectUtilization",starttime1,totime1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if (ConnectUtilizationhash.get("avgpingcon")!=null)
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
			}
			//��ͼ
			//p_draw_line(responsehash,"��ͨ��",newip+"response",750,200);
			//p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);

			//pingƽ��ֵ
			//maxhash = new Hashtable();
			//maxhash.put("avgpingcon",pingconavg);

			//imgurlhash
			//imgurlhash.put("ConnectUtilization","resource/image/jfreechart/"+newip+"ConnectUtilization"+".png");    
			
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
		    	status = getParaIntValue("status");
		    	level1 = getParaIntValue("level1");
		    	if(status == -1)status=99;
		    	if(level1 == -1)level1=99;
		    	request.setAttribute("status", status);
		    	request.setAttribute("level1", level1);
		    	
		    	b_time = getParaValue("startdate");
				t_time = getParaValue("todate");
		    	
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				if (b_time == null){
					b_time = sdf1.format(new Date());
				}
				if (t_time == null){
					t_time = sdf1.format(new Date());
				}
				try{
					User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);      //�û�����
					EventListDao eventdao = new EventListDao();
					list = eventdao.getQuery(starttime1,totime1,"db",status+"",level1+"",
							user.getBusinessids(),vo.getId());
				}catch(Exception ex){
					ex.printStackTrace();
				}
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 

			request.setAttribute("list", list);
			request.setAttribute("startdate", b_time);
			request.setAttribute("todate", t_time);
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			request.setAttribute("sqlsys",sysValue);
			request.setAttribute("rsc_type_ht",rsc_type_ht); 
			request.setAttribute("req_mode_ht",req_mode_ht);
			request.setAttribute("mode_ht",mode_ht);
			request.setAttribute("req_status_ht",req_status_ht);
			request.setAttribute("req_ownertype_ht",req_ownertype_ht);
		        
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/sqlserverevent.jsp";
    }
	
	private String addmanage()
    {    	   
		DBVo vo = new DBVo();
		DBDao dao = new DBDao();
		try{
			vo = (DBVo)dao.findByID(getParaValue("id"));
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dao.close();
		}
		vo.setManaged(1);
        /*
        DBPool.getInstance().removeConnect(vo.getId());
        
        if(PollingEngine.getInstance().getNodeByID(vo.getId())!=null)
        {        
        	DBNode dbNode = (DBNode)PollingEngine.getInstance().getNodeByID(vo.getId());
        	dbNode.setUser(vo.getUser());
        	dbNode.setPassword(vo.getPassword());
        	dbNode.setPort(vo.getPort());
        	dbNode.setIpAddress(vo.getIpAddress());
        	dbNode.setAlias(vo.getAlias());
        	dbNode.setDbName(vo.getDbName());        	
        } 
        */
        dao = new DBDao();
        try{
        	dao.update(vo);	    
        }catch(Exception e){
			e.printStackTrace();
		}finally{
			dao.close();
		}
        return "/db.do?action=list";
    }
	
	public String execute(String action) 
	{	
        if(action.equals("list"))
            return list();    
        if(action.equals("ready_add"))
        	return "/application/db/add.jsp";
        if(action.equals("add"))
        	return add();
        if(action.equals("delete"))
            return delete();
        if(action.equals("ready_edit"))
        {	  
 		    DaoInterface dao = new DBDao();
    	    setTarget("/application/db/edit.jsp");
            return readyEdit(dao);
        }
        if(action.equals("update"))
            return update();
        if(action.equals("cancelmanage"))
            return cancelmanage();
        if(action.equals("addmanage"))
            return addmanage();
        if(action.equals("sqlserverping"))
            return sqlserverping();
        if(action.equals("sqlserversys"))
            return sqlserversys();
        if(action.equals("sqlserverproc"))
            return sqlserverproc();
        if(action.equals("sqlserverdb"))
            return sqlserverdb();
        if(action.equals("sqlserverlock"))
            return sqlserverlock();
        if(action.equals("sqlserverwait"))
            return sqlserverwait();
        if(action.equals("sqlserverevent"))
            return sqlserverevent();
        if(action.equals("sqlserverwaittime"))
        {
        	return sqlserverAvgWatitime();
        }
        if(action.equals("sychronizeData"))
        {
        	return sychronizeData();
        }
        if(action.equals("isDatabaseOK"))
        {
        	return isDatabaseOK();
        }
        //HONGLI START1
        if(action.equals("SqlServerManagerRep")){
        	return sqlServerManagerRep();
        }
        if(action.equals("SqlServerManagerRepQuery")){
        	return sqlServerManagerRepQuery();
        }
        if(action.equals("SqlServerManagerNatureRep")){
        	return sqlServerManagerNatureRep();
        }
        if(action.equals("SqlServerManagerNatureRepQuery")){
        	return sqlServerManagerNatureRepQuery();
        }
        if(action.equals("SqlServerManagerCldRep")){
        	return sqlServerManagerCldRep();
        }
        if(action.equals("SqlServerManagerCldRepQuery")){
        	return sqlServerManagerCldRepQuery();
        }
        if(action.equals("SqlServerManagerEventReport")){
        	return sqlServerManagerEventReport();
        }
        if(action.equals("SqlServerManagerEventReportQuery")){
        	return sqlServerManagerEventReportQuery();
        }
        //HONGLI END1
		setErrorCode(ErrorMessage.ACTION_NO_FOUND);
		return null;
	}
	private String sqlserverAvgWatitime()
	{
		return "/application/db/sqlserveravgwatitime.jsp";
	}
	   private void p_draw_line(Hashtable hash,String title1,String title2,int w,int h){
	    	List list = (List)hash.get("list");
	    	try{
	    	if(list==null || list.size()==0){
	    		draw_blank(title1,title2,w,h);
	    	}
	    	else{
	    	String unit = (String)hash.get("unit");
	    	if (unit == null)unit="%";
	    	ChartGraph cg = new ChartGraph();
	    	
	    	TimeSeries ss = new TimeSeries(title1,Minute.class);
	    	TimeSeries[] s = {ss};
	    	for(int j=0; j<list.size(); j++){
	    			Vector v = (Vector)list.get(j);
	    			Double	d=new Double((String)v.get(0));			
	    			String dt = (String)v.get(1);
	    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    			Date time1 = sdf.parse(dt);				
	    			Calendar temp = Calendar.getInstance();
	    			temp.setTime(time1);
	    			Minute minute=new Minute(temp.get(Calendar.MINUTE),temp.get(Calendar.HOUR_OF_DAY),temp.get(Calendar.DAY_OF_MONTH),temp.get(Calendar.MONTH)+1,temp.get(Calendar.YEAR));
	    			ss.addOrUpdate(minute,d);
	    	}
	    	cg.timewave(s,"x(ʱ��)","y("+unit+")",title1,title2,w,h);
	    	
	    	
	    	
	    	
	    	}
	    	hash = null;
	    	}
	    	catch(Exception e){e.printStackTrace();}
	    	}
	    
	    private void draw_blank(String title1,String title2,int w,int h){
	    	ChartGraph cg = new ChartGraph();
	    	TimeSeries ss = new TimeSeries(title1,Minute.class);
	    	TimeSeries[] s = {ss};
	    	try{
	    		Calendar temp = Calendar.getInstance();
	    		Minute minute=new Minute(temp.get(Calendar.MINUTE),temp.get(Calendar.HOUR_OF_DAY),temp.get(Calendar.DAY_OF_MONTH),temp.get(Calendar.MONTH)+1,temp.get(Calendar.YEAR));
	    		ss.addOrUpdate(minute,null);
	    		cg.timewave(s,"x(ʱ��)","y",title1,title2,w,h);
	    	}
	    	catch(Exception e){e.printStackTrace();}
	    }
		static{
			rsc_type_ht.put("1","NULL ��Դ��δʹ�ã�");
			rsc_type_ht.put("2","���ݿ�");
			rsc_type_ht.put("3","�ļ�");
			rsc_type_ht.put("4","����");
			rsc_type_ht.put("5","��");
			rsc_type_ht.put("6","ҳ");
			rsc_type_ht.put("7","��");
			rsc_type_ht.put("8","��չ����");
			rsc_type_ht.put("9","RID���� ID)");
			rsc_type_ht.put("10","Ӧ�ó���");

			req_mode_ht.put("0","NULL");
			req_mode_ht.put("1","Sch-S");
			req_mode_ht.put("2","Sch-M");
			req_mode_ht.put("3","S");
			req_mode_ht.put("4","U");
			req_mode_ht.put("5","X");
			req_mode_ht.put("6","IS");
			req_mode_ht.put("7","IU");
			req_mode_ht.put("8","IX");
			req_mode_ht.put("9","SIU");
			req_mode_ht.put("10","SIX");
			req_mode_ht.put("11","UIX");
			req_mode_ht.put("12","BU");
			req_mode_ht.put("13","RangeS_S");
			req_mode_ht.put("14","RangeS_U");
			req_mode_ht.put("15","RangeI_N");
			req_mode_ht.put("16","RangeI_S");
			req_mode_ht.put("17","RangeI_U");
			req_mode_ht.put("18","RangeI_X");
			req_mode_ht.put("19","RangeX_S");
			req_mode_ht.put("20","RangeX_U");
			req_mode_ht.put("21","RangeX_X");
			
			req_status_ht.put("1","������	");
			req_status_ht.put("2","����ת��");
			req_status_ht.put("3","���ڵȴ�");
			
			req_ownertype_ht.put("1","����");
			req_ownertype_ht.put("2","�α�");
			req_ownertype_ht.put("3","�Ự");
			req_ownertype_ht.put("4","ExSession");
			
			mode_ht.put("0","����Ȩ������Դ");
			mode_ht.put("1","�ܹ��ȶ��ԣ�ȷ�������κλỰ���Ƽܹ�Ԫ���ϵļܹ��ȶ�����ʱ��ȥ�ܹ�Ԫ�أ�����������");
			mode_ht.put("2","�ܹ��޸ģ��������κ�Ҫ����ָ����Դ�ܹ��ĻỰ���п��ơ�ȷ��û�������ĻỰ��������ָ���Ķ���");
			mode_ht.put("3","������Ȩ���ƻỰ����Դ���й�����ʡ�");
			mode_ht.put("4","���£���ʾ�����տ��ܸ��µ���Դ�ϻ�ȡ�����������ڷ�ֹ������ʽ�����������������ڶ���Ự������Դ�����Ժ���ܸ�����Դʱ������");
			mode_ht.put("5","��������Ȩ���ƻỰ����Դ�����������ʡ�");
			mode_ht.put("6","��������ʾ���⽫ S ������������νṹ�ڵ�ĳ��������Դ�ϡ�");
			mode_ht.put("7","������£���ʾ���⽫ U ������������νṹ�ڵ�ĳ��������Դ�ϡ�");
			mode_ht.put("8","������������ʾ���⽫ X ������������νṹ�ڵ�ĳ��������Դ�ϡ�");
			mode_ht.put("9","����������£���ʾ������������νṹ�ڵĴ�����Դ�ϻ�ȡ����������Դ���й�����ʡ�");
			mode_ht.put("10","����������������ʾ������������νṹ�ڵĴ�����Դ�ϻ�ȡ����������Դ���й�����ʡ�");
			mode_ht.put("11","����������������ʾ��������������������νṹ�ڵĴ�����Դ�ϻ�ȡ����������Դ��");
			mode_ht.put("12","����������");
			mode_ht.put("13","�������Χ�͹�����Դ������ʾ�ɴ��з�Χɨ�衣");
			mode_ht.put("14","�������Χ�͸�����Դ������ʾ�ɴ��и���ɨ�衣");
			mode_ht.put("15","�������Χ�Ϳ���Դ���������������в����¼�֮ǰ���Է�Χ��");
			mode_ht.put("16","ͨ�� RangeI_N �� S �����ص������ļ���Χת����");
			mode_ht.put("17","ͨ�� RangeI_N �� U �����ص������ļ���Χת����");
			mode_ht.put("18","ͨ�� RangeI_N �� X �����ص������ļ���Χת����");
			mode_ht.put("19","ͨ�� RangeI_N �� RangeS_S �����ص������ļ���Χת����");
			mode_ht.put("20","ͨ�� RangeI_N �� RangeS_U �����ص������ļ���Χת����");
			mode_ht.put("21","��������Χ��������Դ������ת�����ڸ��·�Χ�еļ�ʱʹ�á�");
		}
		private String sychronizeData()
		{
			DBRefreshHelper dbRefreshHelper = new DBRefreshHelper();
			
			String dbvoId = request.getParameter("id");
			String dbPage = request.getParameter("dbPage");
			DBDao dbDao = new DBDao();
			DBVo dbVo = (DBVo)dbDao.findByID(dbvoId);
			dbRefreshHelper.execute(dbVo);
			if(dbPage.equals("sqlserverping"))
			{
				return sqlserverping();
			}
			if(dbPage.equals("sqlserversys"))
			{
				return sqlserversys();
			}
			if(dbPage.equals("sqlserverdb"))
			{
				return sqlserverdb();
			}
			if(dbPage.equals("sqlserverproc"))
			{
				return sqlserverproc();
			}
			if(dbPage.equals("sqlserverlock"))
			{
				return sqlserverlock();
			}
			if(dbPage.equals("sqlserverevent"))
			{
				return sqlserverevent();
			}
			if(dbPage.equals("sqlserverevent"))
			{
				return sqlserverevent();
			}
			return "/application/db/sqlserverping.jsp";
		}
		private String isDatabaseOK()
		{
			DBDao dbdao = null;
			dbdao = new DBDao();
			OraclePartsDao oraclePartsDao = null;
			String myip = request.getParameter("myip");
			String myport = request.getParameter("myport");
			int port = Integer.parseInt(myport);
			String myUser = request.getParameter("myUser");
			String myPassword = request.getParameter("myPassword");
			String id = request.getParameter("id");
			String dbType = request.getParameter("dbType");
			request.setAttribute("dbType", dbType);
			DBVo vo =  null;
			boolean sqlServerIsOK = false;
			try {
				sqlServerIsOK = dbdao.getSqlserverIsOk(myip, myUser, myPassword);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (dbdao != null)
					dbdao.close();
			}
			request.setAttribute("oracleIsOK", sqlServerIsOK);
			return "/tool/dbisok.jsp";
		}
		
		
		/**
		 * @author HONGLI 
		 * date 2010-11-09
		 * �����Ա��� 
		 */
		
		public String sqlServerManagerRep(){
			SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd"); 
			Date d = new Date();
			String startdate = getParaValue("startdate");
			if(startdate==null){
				startdate = sdf0.format(d);
			}else {
				try {
					startdate = sdf0.format(sdf0.parse(getParaValue("startdate")));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
			String todate = getParaValue("todate");
			if(todate==null){
				todate = sdf0.format(d);
			}else {
				try {
					todate = sdf0.format(sdf0.parse(getParaValue("todate")));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
			String starttime = startdate + " 00:00:00";
			String totime = todate + " 23:59:59";
			request.setAttribute("startdate", starttime);
			request.setAttribute("todate", totime);
			DBVo vo = new DBVo();
			String id = (String)session.getAttribute("id");
			double avgpingcon = 0;
			String pingnow = "0.0";//��ǰ��ͨ��
			String pingmin = "0.0";//��С��ͨ��
			String pingmax = "0.0";//�����ͨ��
			try{
				DBDao dao = new DBDao();
				try{
					vo = (DBVo)dao.findByID(id);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					dao.close();
				}
				DBTypeDao typedao = new DBTypeDao();
				DBTypeVo typevo = null;
				try{
					typevo = (DBTypeVo)typedao.findByID(vo.getDbtype()+"");
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					typedao.close();
				}
				request.setAttribute("db", vo);
				request.setAttribute("IpAddress", vo.getIpAddress());
				request.setAttribute("dbtye", typevo.getDbdesc());
//				Hashtable allsqlserverdata = ShareData.getSqlserverdata();
//				Hashtable ipsqlserverdata = new Hashtable();
//				if(allsqlserverdata != null && allsqlserverdata.size()>0){
//					if(allsqlserverdata.containsKey(vo.getIpAddress())){
//						ipsqlserverdata = (Hashtable)allsqlserverdata.get(vo.getIpAddress());
//						if(ipsqlserverdata.containsKey("status")){
//							String p_status = (String)ipsqlserverdata.get("status");
//							if(p_status != null && p_status.length()>0){
//								if("1".equalsIgnoreCase(p_status)){
//									pingnow = "100.0";
//								}
//							}
//						}
//					}
//				}
				DBDao dbDao = new DBDao();
				IpTranslation tranfer = new IpTranslation();
				String hex = tranfer.formIpToHex(vo.getIpAddress());
				String serverip = hex+":"+vo.getAlias();
				Hashtable statusHash = dbDao.getSqlserver_nmsstatus(serverip);
				String p_status = (String)statusHash.get("status");
				if(p_status != null && p_status.length()>0){
					if("1".equalsIgnoreCase(p_status)){
						pingnow = "100.0";
					}
				}
				dao.close();
				String newip=SysUtil.doip(vo.getIpAddress());	
				request.setAttribute("newIp", newip);
				Hashtable ConnectUtilizationhash = new Hashtable();
				I_HostCollectData hostmanager=new HostCollectDataManager();
				try{
					ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"SQLPing","ConnectUtilization",starttime,totime);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				String pingconavg = "";
				if (ConnectUtilizationhash.get("avgpingcon")!=null){
					pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
				}
				if(pingconavg != null){
					pingconavg = pingconavg.replace("%", "");//ƽ����ͨ��
				}
				pingmax = (String)ConnectUtilizationhash.get("pingMax");//�����ͨ��
				pingmin = (String)ConnectUtilizationhash.get("pingmax");//��С��ͨ��
				avgpingcon = new Double(pingconavg+"").doubleValue();
				
				p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);//��ͼ
			}catch(Exception e){
				e.printStackTrace();
			}
			request.setAttribute("avgpingcon", avgpingcon);
			request.setAttribute("pingmax", pingmax);
			request.setAttribute("pingmin", pingmin);
			request.setAttribute("pingnow",pingnow); 
			return "/capreport/db/showDbSqlServerReport.jsp";
		}
		
		
		/**
		 * @author HONGLI 
		 * date 2010-11-09
		 * �����Ա��� ��ʱ���ѯ
		 * @return
		 */
		public String sqlServerManagerRepQuery(){
			return sqlServerManagerRep();
		}
		
		/**
		 *@author HONGLI 
		 * date 2010-11-09
		 * ���ܱ��� 
		 * @return
		 */
		public String sqlServerManagerNatureRep(){
			SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd"); 
			Date d = new Date();
			String startdate = getParaValue("startdate");
			if(startdate==null){
				startdate = sdf0.format(d);
			}else {
				try {
					startdate = sdf0.format(sdf0.parse(getParaValue("startdate")));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
			String todate = getParaValue("todate");
			if(todate==null){
				todate = sdf0.format(d);
			}else {
				try {
					todate = sdf0.format(sdf0.parse(getParaValue("todate")));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
			String starttime = startdate + " 00:00:00";
			String totime = todate + " 23:59:59";
			request.setAttribute("startdate", starttime);
			request.setAttribute("todate", totime);
			DBVo vo = new DBVo();
			String id = (String)session.getAttribute("id");
			double avgpingcon = 0;
			String pingnow = "0.0";//��ǰ��ͨ��
			String pingmin = "0.0";//��С��ͨ��
			String pingmax = "0.0";//�����ͨ��
			Hashtable dbValue = new Hashtable();
			try{
				DBDao dao = new DBDao();
				try{
					vo = (DBVo)dao.findByID(id);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					dao.close();
				}
				DBTypeDao typedao = new DBTypeDao();
				DBTypeVo typevo = null;
				try{
					typevo = (DBTypeVo)typedao.findByID(vo.getDbtype()+"");
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					typedao.close();
				}
				request.setAttribute("db", vo);
				request.setAttribute("IpAddress", vo.getIpAddress());
				request.setAttribute("dbtye", typevo.getDbdesc());
//				Hashtable allsqlserverdata = ShareData.getSqlserverdata();
//				Hashtable ipsqlserverdata = new Hashtable();
//				
//				if(allsqlserverdata != null && allsqlserverdata.size()>0){
//					if(allsqlserverdata.containsKey(vo.getIpAddress())){
//						ipsqlserverdata = (Hashtable)allsqlserverdata.get(vo.getIpAddress());
//						if(ipsqlserverdata.containsKey("status")){
//							String p_status = (String)ipsqlserverdata.get("status");
//							if(p_status != null && p_status.length()>0){
//								if("1".equalsIgnoreCase(p_status)){
//									pingnow = "100.0";
//								}
//							}
//						}
//						if(ipsqlserverdata.containsKey("dbValue")){
//							dbValue = (Hashtable)ipsqlserverdata.get("dbValue");
//						}
//					}
//				}
				DBDao dbDao = new DBDao();
				IpTranslation tranfer = new IpTranslation();
				String hex = tranfer.formIpToHex(vo.getIpAddress());
				String serverip = hex+":"+vo.getAlias();
				Hashtable sqlValue = new Hashtable();
				Hashtable statusHash = dbDao.getSqlserver_nmsstatus(serverip);
				dbValue = dbDao.getSqlserver_nmsdbvalue(serverip);
				String p_status = (String)statusHash.get("status");
				if(p_status != null && p_status.length()>0){
					if("1".equalsIgnoreCase(p_status)){
						pingnow = "100.0";
					}
				}
				dao.close();
				String newip=SysUtil.doip(vo.getIpAddress());	
				request.setAttribute("newIp", newip);
				Hashtable ConnectUtilizationhash = new Hashtable();
				I_HostCollectData hostmanager=new HostCollectDataManager();
				try{
					ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"SQLPing","ConnectUtilization",starttime,totime);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				String pingconavg = "";
				if (ConnectUtilizationhash.get("avgpingcon")!=null){
					pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
				}
				if(pingconavg != null){
					pingconavg = pingconavg.replace("%", "");//ƽ����ͨ��
				}
				pingmax = (String)ConnectUtilizationhash.get("pingMax");//�����ͨ��
				pingmin = (String)ConnectUtilizationhash.get("pingmax");//��С��ͨ��
				avgpingcon = new Double(pingconavg+"").doubleValue();
				
				p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);//��ͼ
			}catch(Exception e){
				e.printStackTrace();
			}
			request.setAttribute("dbValue", dbValue);
			request.setAttribute("avgpingcon", avgpingcon+"");
			request.setAttribute("pingmax", pingmax);
			request.setAttribute("pingmin", pingmin);
			request.setAttribute("pingnow",pingnow); 
			return "/capreport/db/showDbSqlServerNatureReport.jsp";
		}
		
		
		/**
		 *@author HONGLI 
		 * date 2010-11-09
		 * ���ܱ���  �����ڲ�ѯ
		 * @return
		 */
		public String sqlServerManagerNatureRepQuery(){
			return sqlServerManagerNatureRep();
		}
		
		/**
		 *@author HONGLI 
		 * date 2010-11-09
		 * �ۺϱ���  
		 * @return
		 */
		public String sqlServerManagerCldRep(){
			SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd"); 
			Date d = new Date();
			String startdate = getParaValue("startdate");
			if(startdate==null){
				startdate = sdf0.format(d);
			}else {
				try {
					startdate = sdf0.format(sdf0.parse(getParaValue("startdate")));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
			String todate = getParaValue("todate");
			if(todate==null){
				todate = sdf0.format(d);
			}else {
				try {
					todate = sdf0.format(sdf0.parse(getParaValue("todate")));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
			String starttime = startdate + " 00:00:00";
			String totime = todate + " 23:59:59";
			request.setAttribute("startdate", starttime);
			request.setAttribute("todate", totime);
			DBVo vo = new DBVo();
			DBTypeVo typevo = null;
			String id = (String)session.getAttribute("id");
			double avgpingcon = 0;
			String pingnow = "0.0";//��ǰ��ͨ��
			String pingmin = "0.0";//��С��ͨ��
			String pingmax = "0.0";//�����ͨ��
			String runstr = "<font color=red>����ֹͣ</font>";
			Hashtable dbValue = new Hashtable();
			String downnum = "";
//			���ݿ����еȼ�=====================
			String grade = "��";
			Hashtable mems = new Hashtable();//�ڴ���Ϣ
			Hashtable sysValue = new Hashtable();
			List eventList = new ArrayList();//�¼��б�
			try{
				DBDao dao = new DBDao();
				try{
					vo = (DBVo)dao.findByID(id);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					dao.close();
				}
				DBTypeDao typedao = new DBTypeDao();
				try{
					typevo = (DBTypeVo)typedao.findByID(vo.getDbtype()+"");
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					typedao.close();
				}
				request.setAttribute("db", vo);
				request.setAttribute("IpAddress", vo.getIpAddress());
				request.setAttribute("dbtye", typevo.getDbdesc());
//				Hashtable allsqlserverdata = ShareData.getSqlserverdata();
//				Hashtable ipsqlserverdata = new Hashtable();
//				
//				if(allsqlserverdata != null && allsqlserverdata.size()>0){
//					if(allsqlserverdata.containsKey(vo.getIpAddress())){
//						ipsqlserverdata = (Hashtable)allsqlserverdata.get(vo.getIpAddress());
//						if(ipsqlserverdata.containsKey("status")){
//							String p_status = (String)ipsqlserverdata.get("status");
//							if(p_status != null && p_status.length()>0){
//								if("1".equalsIgnoreCase(p_status)){
//									runstr = "��������";
//									pingnow = "100.0";
//								}
//							}
//						}
//						if(ipsqlserverdata.containsKey("dbValue")){
//							dbValue = (Hashtable)ipsqlserverdata.get("dbValue");
//						}
//						if(ipsqlserverdata.containsKey("retValue")){
//							mems = (Hashtable)((Hashtable)ipsqlserverdata.get("retValue")).get("mems");
//						}
//						if(ipsqlserverdata.containsKey("sysValue")){
//							sysValue = (Hashtable)ipsqlserverdata.get("sysValue");
//						}
//					}
//				}
				
				DBDao dbDao = new DBDao();
				IpTranslation tranfer = new IpTranslation();
				String hex = tranfer.formIpToHex(vo.getIpAddress());
				String serverip = hex+":"+vo.getAlias();
				Hashtable statusHash = dbDao.getSqlserver_nmsstatus(serverip);
				dbValue = dbDao.getSqlserver_nmsdbvalue(serverip);
				sysValue = dbDao.getSqlserver_nmssysvalue(serverip);
				mems = dbDao.getSqlserver_nmsmems(serverip);
				String p_status = (String)statusHash.get("status");
				if(p_status != null && p_status.length()>0){
					if("1".equalsIgnoreCase(p_status)){
						runstr = "��������";
						pingnow = "100.0";
					}
				}
				dao.close();
				String newip=SysUtil.doip(vo.getIpAddress());	
				request.setAttribute("newip", newip);
				Hashtable ConnectUtilizationhash = new Hashtable();
				I_HostCollectData hostmanager=new HostCollectDataManager();
				try{
					ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"SQLPing","ConnectUtilization",starttime,totime);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				String pingconavg = "";
				if (ConnectUtilizationhash.get("avgpingcon")!=null){
					pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
				}
				if(pingconavg != null){
					pingconavg = pingconavg.replace("%", "");//ƽ����ͨ��
				}
				if (ConnectUtilizationhash.get("downnum") != null){
					downnum = (String) ConnectUtilizationhash.get("downnum");
				}
				pingmax = (String)ConnectUtilizationhash.get("pingMax");//�����ͨ��
				pingmin = (String)ConnectUtilizationhash.get("pingmax");//��С��ͨ��
				avgpingcon = new Double(pingconavg+"").doubleValue();
				
				p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);//��ͼ
				
				//�õ����еȼ�
				DBTypeDao dbTypeDao = new DBTypeDao();
				int count = 0;
				try {
					count = dbTypeDao.finddbcountbyip(vo.getIpAddress());
					request.setAttribute("count", count);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					dbTypeDao.close();
				}
				
				if (count>0) {
					grade = "��";
				}
				if (!"0".equals(downnum)) {
					grade = "��";
				}
				
//				�¼��б�
				int status = getParaIntValue("status");
		    	int level1 = getParaIntValue("level1");
		    	if(status == -1)status=99;
		    	if(level1 == -1)level1=99;
		    	request.setAttribute("status", status);
		    	request.setAttribute("level1", level1);
				try{
					User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);      //�û�����
					//SysLogger.info("user businessid===="+vo.getBusinessids());
					EventListDao eventdao = new EventListDao();
					try{
						eventList = eventdao.getQuery(starttime,totime,"db",status+"",level1+"",user.getBusinessids(),vo.getId());
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						eventdao.close();
					}
					
					//ConnectUtilizationhash = hostmanager.getCategory(host.getIpAddress(),"Ping","ConnectUtilization",starttime1,totime1);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			request.setAttribute("list", eventList);
			request.setAttribute("sysValue", sysValue);
			request.setAttribute("downnum", downnum);
			request.setAttribute("mems", mems);
			request.setAttribute("grade", grade);
			request.setAttribute("vo", vo);
			request.setAttribute("runstr", runstr);
			request.setAttribute("typevo", typevo);
			request.setAttribute("dbValue", dbValue);
			request.setAttribute("avgpingcon", avgpingcon+"");
			request.setAttribute("pingmax", pingmax);
			request.setAttribute("pingmin", pingmin);
			request.setAttribute("pingnow",pingnow); 
			return "/capreport/db/showDbSqlServerCldReport.jsp";
		}
		
		/**
		 *@author HONGLI 
		 * date 2010-11-09
		 * �ۺϱ���   �����ڲ�ѯ
		 * @return
		 */
		public String sqlServerManagerCldRepQuery(){
			return sqlServerManagerCldRep();
		}
		
		/**
		 *@author HONGLI 
		 * date 2010-11-11
		 * �¼�����   
		 * @return
		 */
		public String sqlServerManagerEventReport(){
			SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd"); 
			Date d = new Date();
			String startdate = getParaValue("startdate");
			if(startdate==null){
				startdate = sdf0.format(d);
			}else {
				try {
					startdate = sdf0.format(sdf0.parse(getParaValue("startdate")));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
			String todate = getParaValue("todate");
			if(todate==null){
				todate = sdf0.format(d);
			}else {
				try {
					todate = sdf0.format(sdf0.parse(getParaValue("todate")));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
			String starttime = startdate + " 00:00:00";
			String totime = todate + " 23:59:59";
			request.setAttribute("startdate", starttime);
			request.setAttribute("todate", totime);
			DBVo vo = new DBVo();
			DBTypeVo typevo = null;
			String id = (String)session.getAttribute("id");
			String downnum = "";
			List eventList = new ArrayList();//�¼��б�
			try{
				DBDao dao = new DBDao();
				try{
					vo = (DBVo)dao.findByID(id);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					dao.close();
				}
				DBTypeDao typedao = new DBTypeDao();
				try{
					typevo = (DBTypeVo)typedao.findByID(vo.getDbtype()+"");
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					typedao.close();
				}
				String newip=SysUtil.doip(vo.getIpAddress());	
				request.setAttribute("newip", newip);
				Hashtable ConnectUtilizationhash = new Hashtable();
				I_HostCollectData hostmanager=new HostCollectDataManager();
				try{
					ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"SQLPing","ConnectUtilization",starttime,totime);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				
				if (ConnectUtilizationhash.get("downnum") != null){
					downnum = (String) ConnectUtilizationhash.get("downnum");
				}
				
				//�õ����еȼ�
				DBTypeDao dbTypeDao = new DBTypeDao();
				int count = 0;
				try {
					count = dbTypeDao.finddbcountbyip(vo.getIpAddress());
					request.setAttribute("count", count);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					dbTypeDao.close();
				}
				
//				�¼��б�
				int status = getParaIntValue("status");
		    	int level1 = getParaIntValue("level1");
		    	if(status == -1)status=99;
		    	if(level1 == -1)level1=99;
		    	request.setAttribute("status", status);
		    	request.setAttribute("level1", level1);
				try{
					User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);      //�û�����
					//SysLogger.info("user businessid===="+vo.getBusinessids());
					EventListDao eventdao = new EventListDao();
					try{
						eventList = eventdao.getQuery(starttime,totime,"db",status+"",level1+"",user.getBusinessids(),vo.getId());
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						eventdao.close();
					}
					//ConnectUtilizationhash = hostmanager.getCategory(host.getIpAddress(),"Ping","ConnectUtilization",starttime1,totime1);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			request.setAttribute("list", eventList);
			request.setAttribute("downnum", downnum);
			request.setAttribute("vo", vo);
			request.setAttribute("typevo", typevo);
			return "/capreport/db/showDbSqlServerEventReport.jsp";
		}
		
		/**
		 *@author HONGLI 
		 * date 2010-11-11
		 * �¼�����   �����ڲ�ѯ
		 * @return
		 */
		public String sqlServerManagerEventReportQuery(){
			return sqlServerManagerEventReport();
		}
		
}