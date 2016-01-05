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
import com.afunms.application.model.Sybspaceconfig;
import com.afunms.system.model.User;
import com.afunms.topology.util.*;
import com.afunms.application.dao.*;
import com.afunms.application.util.DBPool;
import com.afunms.topology.dao.DiscoverCompleteDao;
import com.afunms.polling.impl.*;
import com.afunms.polling.api.*;

import com.afunms.report.jfree.ChartCreator;

import org.jfree.data.general.DefaultPieDataset;

public class SybaseConfigManager extends BaseManager implements ManagerInterface
{
	private String list()
	{
		List ips = new ArrayList();
		User operator = (User)session.getAttribute(SessionConstant.CURRENT_USER);
		String bids = operator.getBusinessids();
		DBVo vo = new DBVo();
		
		String bid[] = bids.split(",");
		Vector rbids = new Vector();
		if(bid != null && bid.length>0){
			for(int i=0;i<bid.length;i++){
				if(bid[i] != null && bid[i].trim().length()>0)
					rbids.add(bid[i].trim());
			}
		}

		List oraList = new ArrayList();
		DBTypeDao typedao = new DBTypeDao();
		DBTypeVo typevo = null;
		try{
			typevo = (DBTypeVo)typedao.findByDbtype("sybase");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			typedao.close();
		}
		
		DBDao dao = new DBDao();
		try{
			oraList = dao.getDbByTypeAndBID(typevo.getId(),rbids);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dao.close();
		}
		
		if(oraList != null && oraList.size()>0){
			for(int i=0;i<oraList.size();i++){
				DBVo dbmonitorlist = (DBVo)oraList.get(i);
				ips.add(dbmonitorlist.getIpAddress());
			}
		}
		request.setAttribute("iplist",ips);
		SybspaceconfigDao configdao = new SybspaceconfigDao();	
		setTarget("/application/db/sybaseconfiglist.jsp");
		return list(configdao);
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
		Sybspaceconfig vo = new Sybspaceconfig();
		DBVo dbvo = new DBVo();
		
		List list = new ArrayList();
		List conflist = new ArrayList();
		List ips = new ArrayList();
		String ipaddress ="";		
		
    	vo.setId(getParaIntValue("id"));
        vo.setIpaddress(getParaValue("ipaddress"));
        vo.setSpacename(getParaValue("spacename"));
        vo.setLinkuse(getParaValue("linkuse"));
        vo.setAlarmvalue(getParaIntValue("alarmvalue"));
        vo.setBak(getParaValue("bak"));
        vo.setReportflag(getParaIntValue("reportflag"));
        vo.setSms(getParaIntValue("sms"));
        
        try{
        	SybspaceconfigDao configdao = new SybspaceconfigDao();
        	try{
        		configdao.update(vo);	
        	}catch(Exception e){
				e.printStackTrace();
			}finally{
				configdao.close();
			}
			User operator = (User)session.getAttribute(SessionConstant.CURRENT_USER);
			String bids = operator.getBusinessids();
			String bid[] = bids.split(",");
			Vector rbids = new Vector();
			if(bid != null && bid.length>0){
				for(int i=0;i<bid.length;i++){
					if(bid[i] != null && bid[i].trim().length()>0)
						rbids.add(bid[i].trim());
				}
			}

			List oraList = new ArrayList();
			DBTypeDao typedao = new DBTypeDao();
			DBTypeVo typevo = (DBTypeVo)typedao.findByDbtype("sybase");
			
			DBDao dao = new DBDao();
			try{
				oraList = dao.getDbByTypeAndBID(typevo.getId(),rbids);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}
			
			if(oraList != null && oraList.size()>0){
				for(int i=0;i<oraList.size();i++){
					DBVo dbmonitorlist = (DBVo)oraList.get(i);
					ips.add(dbmonitorlist.getIpAddress());
				}
			}
			
			
			//configdao = new SybspaceconfigDao();
			//configdao.fromLastToOraspaceconfig();			
			if (ipaddress != null && ipaddress.trim().length()>0){
				configdao = new SybspaceconfigDao();
				try{
					list =configdao.getByIp(ipaddress);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					configdao.close();
				}
				
				if (list == null || list.size() == 0){
					configdao = new SybspaceconfigDao();
					try{
						list = configdao.loadAll();
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						configdao.close();
					}
				}
			}else{
				configdao = new SybspaceconfigDao();
				try{
					list = configdao.loadAll();		
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					configdao.close();
				}
			}
			
//			if(list != null && list.size()>0){
//				for(int k=0;k<list.size();k++){
//					Sybspaceconfig oraspaceconfig = (Sybspaceconfig)list.get(k);
//					if(ips.contains(oraspaceconfig.getIpaddress()))conflist.add(oraspaceconfig);
//				}
//			}
        }catch(Exception e){
        	e.printStackTrace();
        }
        
        //request.setAttribute("Oraspaceconfiglist", conflist);
		request.setAttribute("iplist",ips);
		request.setAttribute("ipaddress",ipaddress);
		request.setAttribute("list",list);
        
        
		return "/application/db/sybaseconfigsearchlist.jsp";
    }

	
	private String createSpaceConfig()
    {    	   
		DBVo vo = new DBVo();
		
		
		List list = new ArrayList();
		List conflist = new ArrayList();
		List ips = new ArrayList();
		String ipaddress ="";

		try{
			ipaddress = getParaValue("ip");
			User operator = (User)session.getAttribute(SessionConstant.CURRENT_USER);
			String bids = operator.getBusinessids();
			String bid[] = bids.split(",");
			Vector rbids = new Vector();
			if(bid != null && bid.length>0){
				for(int i=0;i<bid.length;i++){
					if(bid[i] != null && bid[i].trim().length()>0)
						rbids.add(bid[i].trim());
				}
			}

			List oraList = new ArrayList();
			DBTypeDao typedao = new DBTypeDao();
			DBTypeVo typevo = null;
			try{
				typevo = (DBTypeVo)typedao.findByDbtype("sybase");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				typedao.close();
			}
			
			DBDao dao = new DBDao();
			try{
				oraList = dao.getDbByTypeAndBID(typevo.getId(),rbids);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}
			
			if(oraList != null && oraList.size()>0){
				for(int i=0;i<oraList.size();i++){
					DBVo dbmonitorlist = (DBVo)oraList.get(i);
					ips.add(dbmonitorlist.getIpAddress());
				}
			}
			
			
			SybspaceconfigDao configdao = new SybspaceconfigDao();
			try{
				configdao.fromLastToSybspaceconfig();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				configdao.close();
			}
			
			ipaddress = (String)session.getAttribute("ipaddress");			
			if (ipaddress != null && ipaddress.trim().length()>0){
				configdao = new SybspaceconfigDao();
				try{
					list =configdao.getByIp(ipaddress);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					configdao.close();
				}
				
				if (list == null || list.size() == 0){
					configdao = new SybspaceconfigDao();
					try{
						list = configdao.loadAll();
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						configdao.close();
					}
				}
			}else{
				configdao = new SybspaceconfigDao();
				try{
					list = configdao.loadAll();		
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					configdao.close();
				}
			}
			
//			if(list != null && list.size()>0){
//				for(int k=0;k<list.size();k++){
//					Sybspaceconfig oraspaceconfig = (Sybspaceconfig)list.get(k);
//					if(ips.contains(oraspaceconfig.getIpaddress()))conflist.add(oraspaceconfig);
//				}
//			}
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("iplist",ips);
		SybspaceconfigDao configdao = new SybspaceconfigDao();	
		setTarget("/application/db/sybaseconfiglist.jsp");
		return list(configdao);
    }
	
	private String search()
    {    	   
		DBVo vo = new DBVo();
		
		
		List list = new ArrayList();
		List conflist = new ArrayList();
		List ips = new ArrayList();
		String ipaddress ="";

		try{
			ipaddress = getParaValue("ipaddress");
			User operator = (User)session.getAttribute(SessionConstant.CURRENT_USER);
			String bids = operator.getBusinessids();
			String bid[] = bids.split(",");
			Vector rbids = new Vector();
			if(bid != null && bid.length>0){
				for(int i=0;i<bid.length;i++){
					if(bid[i] != null && bid[i].trim().length()>0)
						rbids.add(bid[i].trim());
				}
			}

			List oraList = new ArrayList();
			DBTypeDao typedao = new DBTypeDao();
			DBTypeVo typevo = null;
			try{
				typevo = (DBTypeVo)typedao.findByDbtype("sybase");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				typedao.close();
			}
			
			DBDao dao = new DBDao();
			try{
				oraList = dao.getDbByTypeAndBID(typevo.getId(),rbids);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}
			
			if(oraList != null && oraList.size()>0){
				for(int i=0;i<oraList.size();i++){
					DBVo dbmonitorlist = (DBVo)oraList.get(i);
					ips.add(dbmonitorlist.getIpAddress());
				}
			}
			
			
			SybspaceconfigDao configdao = null;
			
			//ipaddress = (String)session.getAttribute("ipaddress");			
			if (ipaddress != null && ipaddress.trim().length()>0){
				configdao = new SybspaceconfigDao();
				try{
					list =configdao.getByIp(ipaddress);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					configdao.close();
				}
				
				if (list == null || list.size() == 0){
					configdao = new SybspaceconfigDao();
					try{
						list = configdao.loadAll();
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						configdao.close();
					}
				}
			}else{
				configdao = new SybspaceconfigDao();
				try{
					list = configdao.loadAll();		
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					configdao.close();
				}
			}
			
//			if(list != null && list.size()>0){
//				for(int k=0;k<list.size();k++){
//					Sybspaceconfig oraspaceconfig = (Sybspaceconfig)list.get(k);
//					if(ips.contains(oraspaceconfig.getIpaddress()))conflist.add(oraspaceconfig);
//				}
//			}
		}catch(Exception e){
			e.printStackTrace();
		}
		//request.setAttribute("Oraspaceconfiglist", conflist);
		request.setAttribute("iplist",ips);
		request.setAttribute("ipaddress",ipaddress);
//		SybspaceconfigDao configdao = new SybspaceconfigDao();
//		list = configdao.getByIp(ipaddress);
		request.setAttribute("list",list);
		return "/application/db/sybaseconfigsearchlist.jsp";
    }
	
	private String addalert()
    {    
		Sybspaceconfig vo = new Sybspaceconfig();
		
		DBVo dbvo = new DBVo();
		
		List list = new ArrayList();
		List conflist = new ArrayList();
		List ips = new ArrayList();
		String ipaddress ="";
		try{
			SybspaceconfigDao configdao = new SybspaceconfigDao();
			try{
				vo = (Sybspaceconfig)configdao.findByID(getParaValue("id"));
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				configdao.close();
			}
			
			ipaddress = vo.getIpaddress();
			vo.setSms(1);
			configdao = new SybspaceconfigDao();
			try{
				configdao.update(vo);	
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				configdao.close();
			}
			
			User operator = (User)session.getAttribute(SessionConstant.CURRENT_USER);
			String bids = operator.getBusinessids();
			String bid[] = bids.split(",");
			Vector rbids = new Vector();
			if(bid != null && bid.length>0){
				for(int i=0;i<bid.length;i++){
					if(bid[i] != null && bid[i].trim().length()>0)
						rbids.add(bid[i].trim());
				}
			}

			List oraList = new ArrayList();
			DBTypeDao typedao = new DBTypeDao();
			DBTypeVo typevo = null;
			try{
				typevo = (DBTypeVo)typedao.findByDbtype("sybase");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				typedao.close();
			}
			
			DBDao dao = new DBDao();
			try{
				oraList = dao.getDbByTypeAndBID(typevo.getId(),rbids);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}
			
			if(oraList != null && oraList.size()>0){
				for(int i=0;i<oraList.size();i++){
					DBVo dbmonitorlist = (DBVo)oraList.get(i);
					ips.add(dbmonitorlist.getIpAddress());
				}
			}
			
			if (ipaddress != null && ipaddress.trim().length()>0){
				configdao = new SybspaceconfigDao();
				try{
					list =configdao.getByIp(ipaddress);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					configdao.close();
				}
				if (list == null || list.size() == 0){
					configdao = new SybspaceconfigDao();
					try{
						list = configdao.loadAll();
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						configdao.close();
					}
				}
			}else{
				configdao = new SybspaceconfigDao();
				try{
					list = configdao.loadAll();		
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					configdao.close();
				}
			}
			
//			if(list != null && list.size()>0){
//				for(int k=0;k<list.size();k++){
//					Sybspaceconfig oraspaceconfig = (Sybspaceconfig)list.get(k);
//					if(ips.contains(oraspaceconfig.getIpaddress()))conflist.add(oraspaceconfig);
//				}
//			}
		}catch(Exception e){
			e.printStackTrace();
		}
		//request.setAttribute("Oraspaceconfiglist", conflist);
		request.setAttribute("iplist",ips);
		request.setAttribute("ipaddress",ipaddress);
		request.setAttribute("list",list);
		return "/application/db/sybaseconfigsearchlist.jsp";
    }
	
	private String cancelalert()
    {    
		Sybspaceconfig vo = new Sybspaceconfig();
		
		DBVo dbvo = new DBVo();
		
		List list = new ArrayList();
		List conflist = new ArrayList();
		List ips = new ArrayList();
		String ipaddress ="";
		try{
			SybspaceconfigDao configdao = new SybspaceconfigDao();
			try{
				vo = (Sybspaceconfig)configdao.findByID(getParaValue("id"));
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				configdao.close();
			}
			
			ipaddress = vo.getIpaddress();
			vo.setSms(0);
			
			configdao = new SybspaceconfigDao();
			try{
				configdao.update(vo);	
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				configdao.close();
			}
			
			User operator = (User)session.getAttribute(SessionConstant.CURRENT_USER);
			String bids = operator.getBusinessids();
			String bid[] = bids.split(",");
			Vector rbids = new Vector();
			if(bid != null && bid.length>0){
				for(int i=0;i<bid.length;i++){
					if(bid[i] != null && bid[i].trim().length()>0)
						rbids.add(bid[i].trim());
				}
			}

			List oraList = new ArrayList();
			DBTypeDao typedao = new DBTypeDao();
			DBTypeVo typevo = null;
			try{
				typevo = (DBTypeVo)typedao.findByDbtype("sybase");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				typedao.close();
			}
			
			DBDao dao = new DBDao();
			try{
				oraList = dao.getDbByTypeAndBID(typevo.getId(),rbids);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}
			
			if(oraList != null && oraList.size()>0){
				for(int i=0;i<oraList.size();i++){
					DBVo dbmonitorlist = (DBVo)oraList.get(i);
					ips.add(dbmonitorlist.getIpAddress());
				}
			}
					
			if (ipaddress != null && ipaddress.trim().length()>0){
				configdao = new SybspaceconfigDao();
				try{
					list =configdao.getByIp(ipaddress);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					configdao.close();
				}
				
				if (list == null || list.size() == 0){
					configdao = new SybspaceconfigDao();
					try{
						list = configdao.loadAll();
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						configdao.close();
					}
				}
			}else{
				configdao = new SybspaceconfigDao();
				try{
					list = configdao.loadAll();		
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					configdao.close();
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("iplist",ips);
		request.setAttribute("ipaddress",ipaddress);
		request.setAttribute("list",list);
		return "/application/db/sybaseconfigsearchlist.jsp";
    }
	/**
	 * @author hukelei add for 
	 * @since 2010-01-21
	 * @return
	 */
	private String ready_edit(){
		String jsp = "/application/db/sybaseconfigedit.jsp";
		SybspaceconfigDao dao = new SybspaceconfigDao();
		try{
			setTarget(jsp);
			jsp = readyEdit(dao);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dao.close();
		}
	    return jsp;
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
        	return ready_edit();
        if(action.equals("update"))
            return update();
        if(action.equals("addalert"))
            return addalert();
        if(action.equals("cancelalert"))
            return cancelalert();
        if(action.equals("search"))
            return search();
        if(action.equals("createspaceconfig"))
            return createSpaceConfig();
		setErrorCode(ErrorMessage.ACTION_NO_FOUND);
		return null;
	}
	
}