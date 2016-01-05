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

public class DB2Manager extends BaseManager implements ManagerInterface
{
	public static Hashtable rsc_type_ht = new Hashtable();
	public static Hashtable req_mode_ht = new Hashtable();
	public static Hashtable mode_ht = new Hashtable();
	public static Hashtable req_status_ht = new Hashtable();
	public static Hashtable req_ownertype_ht = new Hashtable();
	private String list()
	{
		DBDao dao = new DBDao();
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
	
	private String db2ping()
    {    	   
		DBVo vo = new DBVo();
		
		
		Hashtable sysValue = new Hashtable();
		Hashtable commonValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
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
			String runstr = "<font color=red>�����ݿ����ֹͣ</font>";
			String[] dbs = vo.getDbName().split(",");
			int allFlag = 0;
//			Hashtable allDb2data = ShareData.getAlldb2data();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String sip = hex+":"+vo.getId();
			Hashtable monitorValue = dao.getDB2DataByServerip(sip);
			Hashtable allDb2data = (Hashtable)monitorValue.get("allDb2Data");
			Hashtable ipDb2data = new Hashtable();
			if(allDb2data != null && allDb2data.size()>0){
				if(allDb2data.containsKey(vo.getIpAddress())){
					ipDb2data = (Hashtable)allDb2data.get(vo.getIpAddress());
					if(ipDb2data.containsKey("status")){
						String p_status = (String)ipDb2data.get("status");
						if(p_status != null && p_status.length()>0){
							//SysLogger.info("p_status=================="+p_status);
							if("1".equalsIgnoreCase(p_status)){
								runstr = "��������";
							}
						}
					}
				}
				if(ipDb2data.containsKey("sysInfo")) sysValue=(Hashtable)ipDb2data.get("sysInfo");	
				if(ipDb2data.containsKey("commonhash")) commonValue=(Hashtable)ipDb2data.get("commonhash");	
			}
//			for(int k=0;k<dbs.length;k++){
//				String dbStr = dbs[k];					
//				boolean db2IsOK = false;
//				dao = new DBDao();
//				try{
//					db2IsOK = dao.getDB2IsOK(vo.getIpAddress(), Integer.parseInt(vo.getPort()), vo.getDbName(), vo.getUser(), vo.getPassword());
//				}catch(Exception e){
//					e.printStackTrace();
//					db2IsOK = false;
//				}finally{
//					dao.close();
//				}
//				if (!db2IsOK){
//					allFlag =1;
//				}
//			}
//			if (allFlag == 0){
//				runstr = "��������";
//			}

			request.setAttribute("runstr", runstr);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"DB2Ping","ConnectUtilization",starttime1,totime1);
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
			p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);

			//pingƽ��ֵ
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);

			//imgurlhash
			imgurlhash.put("ConnectUtilization","resource/image/jfreechart/"+newip+"ConnectUtilization"+".png");    
			
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			request.setAttribute("sqlsys",sysValue);
			request.setAttribute("commonValue",commonValue);
			 
			
		        
		}catch(Exception e){
			e.printStackTrace();
		}

		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/db2ping.jsp";
    }
	
	private String db2space()
    {    	   
		DBVo vo = new DBVo();
		Hashtable returnhash = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		Hashtable commonValue = new Hashtable();
		String pingconavg ="0";
		double avgpingcon = 0;
		try{
			DBDao dao = new DBDao();
			try{
				vo = (DBVo)dao.findByID(getParaValue("id"));
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
			String runstr = "<font color=red>�����ݿ����ֹͣ</font>";
			String[] dbs = vo.getDbName().split(",");
			int allFlag = 0;
//			Hashtable allDb2data = ShareData.getAlldb2data();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String sip = hex+":"+vo.getId();
			Hashtable monitorValue = dao.getDB2DataByServerip(sip);
			Hashtable allDb2data = (Hashtable)monitorValue.get("allDb2Data");
			Hashtable ipDb2data = new Hashtable();
			if(allDb2data != null && allDb2data.size()>0){
				if(allDb2data.containsKey(vo.getIpAddress())){
					ipDb2data = (Hashtable)allDb2data.get(vo.getIpAddress());
					if(ipDb2data.containsKey("status")){
						String p_status = (String)ipDb2data.get("status");
						if(p_status != null && p_status.length()>0){
							if("1".equalsIgnoreCase(p_status)){
								runstr = "��������";
							}
						}
					}
				}
				if(ipDb2data.containsKey("spaceInfo"))
					returnhash=(Hashtable)ipDb2data.get("spaceInfo");
				if(ipDb2data.containsKey("commonhash")) commonValue=(Hashtable)ipDb2data.get("commonhash");	
				
			}

			request.setAttribute("runstr", runstr);
			//dao = new DBDao();
			
				//returnhash = dao.getDB2Space(vo.getIpAddress(), Integer.parseInt(vo.getPort()), vo.getDbName(), vo.getUser(), vo.getPassword());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"DB2Ping","ConnectUtilization",starttime1,totime1);
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
			p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);

			//pingƽ��ֵ
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);

			//imgurlhash
			imgurlhash.put("ConnectUtilization","resource/image/jfreechart/"+newip+"ConnectUtilization"+".png");    
			
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			request.setAttribute("returnhash",returnhash);
			request.setAttribute("commonValue",commonValue);
		        
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/db2space.jsp";
    }
	
	private String db2pool()
    {    	   
		DBVo vo = new DBVo();
		
		
		Hashtable returnhash = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		Hashtable commonValue = new Hashtable();
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
			String runstr = "<font color=red>�����ݿ����ֹͣ</font>";
			String[] dbs = vo.getDbName().split(",");
//			Hashtable allDb2data = ShareData.getAlldb2data();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String sip = hex+":"+vo.getId();
			Hashtable monitorValue = dao.getDB2DataByServerip(sip);
			Hashtable allDb2data = (Hashtable)monitorValue.get("allDb2Data");
			Hashtable ipDb2data = new Hashtable();
			if(allDb2data != null && allDb2data.size()>0){
				if(allDb2data.containsKey(vo.getIpAddress())){
					ipDb2data = (Hashtable)allDb2data.get(vo.getIpAddress());
					if(ipDb2data.containsKey("status")){
						String p_status = (String)ipDb2data.get("status");
						if(p_status != null && p_status.length()>0){
							if("1".equalsIgnoreCase(p_status)){
								runstr = "��������";
							}
						}
					}
				}
				if(ipDb2data.containsKey("poolInfo")){
					returnhash=(Hashtable)ipDb2data.get("poolInfo");
				}
				if(ipDb2data.containsKey("commonhash")) commonValue=(Hashtable)ipDb2data.get("commonhash");	
			
			}
			/*int allFlag = 0;
			for(int k=0;k<dbs.length;k++){
				String dbStr = dbs[k];					
				boolean db2IsOK = false;
				dao = new DBDao();
				try{
					db2IsOK = dao.getDB2IsOK(vo.getIpAddress(), Integer.parseInt(vo.getPort()), vo.getDbName(), vo.getUser(), vo.getPassword());
				}catch(Exception e){
					e.printStackTrace();
					db2IsOK = false;
				}finally{
					dao.close();
				}
				if (!db2IsOK){
					allFlag =1;
				}
			}
			if (allFlag == 0){
				runstr = "��������";
			}*/

			request.setAttribute("runstr", runstr);
			

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"DB2Ping","ConnectUtilization",starttime1,totime1);
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
			p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);

			//ping?��???
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);

			//imgurlhash
			imgurlhash.put("ConnectUtilization","resource/image/jfreechart/"+newip+"ConnectUtilization"+".png");    
			
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			request.setAttribute("returnhash",returnhash);
			request.setAttribute("commonValue",commonValue);
			Hashtable cach=new Hashtable();
			if(ipDb2data.containsKey("cach")) 
			{
				cach=(Hashtable)ipDb2data.get("cach");	
			}
			request.setAttribute("cach",cach);
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/db2pool.jsp";
    }
	
	private String db2table()
    {    	   
		DBVo vo = new DBVo();
		
		
		Hashtable returnhash = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		Hashtable commonValue = new Hashtable();
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
			String runstr = "<font color=red>�����ݿ����ֹͣ</font>";
			String[] dbs = vo.getDbName().split(",");
//			Hashtable allDb2data = ShareData.getAlldb2data();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String sip = hex+":"+vo.getId();
			Hashtable monitorValue = dao.getDB2DataByServerip(sip);
			Hashtable allDb2data = (Hashtable)monitorValue.get("allDb2Data");
			Hashtable ipDb2data = new Hashtable();
			if(allDb2data != null && allDb2data.size()>0){
				if(allDb2data.containsKey(vo.getIpAddress())){
					ipDb2data = (Hashtable)allDb2data.get(vo.getIpAddress());
					if(ipDb2data.containsKey("status")){
						String p_status = (String)ipDb2data.get("status");
						if(p_status != null && p_status.length()>0){
							if("1".equalsIgnoreCase(p_status)){
								runstr = "��������";
							}
						}
					}
				}
				if(ipDb2data.containsKey("poolInfo")){
					returnhash=(Hashtable)ipDb2data.get("poolInfo");
				}
				if(ipDb2data.containsKey("commonhash")) commonValue=(Hashtable)ipDb2data.get("commonhash");	
				
			}

			request.setAttribute("runstr", runstr);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"DB2Ping","ConnectUtilization",starttime1,totime1);
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

			//ping
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);

			//imgurlhash
			//imgurlhash.put("ConnectUtilization","resource/image/jfreechart/"+newip+"ConnectUtilization"+".png");    
			
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			request.setAttribute("returnhash",returnhash);
			request.setAttribute("commonValue",commonValue);
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/db2table.jsp";
    }
	
	private String db2conn()
    {    	   
		DBVo vo = new DBVo();
		
		
		Hashtable returnhash = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		Hashtable commonValue = new Hashtable();
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
			String runstr = "<font color=red>�����ݿ����ֹͣ</font>";
			String[] dbs = vo.getDbName().split(",");
//			Hashtable allDb2data = ShareData.getAlldb2data();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String sip = hex+":"+vo.getId();
			Hashtable monitorValue = dao.getDB2DataByServerip(sip);
			Hashtable allDb2data = (Hashtable)monitorValue.get("allDb2Data");
			Hashtable ipDb2data = new Hashtable();
			if(allDb2data != null && allDb2data.size()>0){
				if(allDb2data.containsKey(vo.getIpAddress())){
					ipDb2data = (Hashtable)allDb2data.get(vo.getIpAddress());
					if(ipDb2data.containsKey("status")){
						String p_status = (String)ipDb2data.get("status");
						if(p_status != null && p_status.length()>0){
							if("1".equalsIgnoreCase(p_status)){
								runstr = "��������";
							}
						}
					}
				}
				if(ipDb2data.containsKey("conn")){ 
					returnhash=(Hashtable)ipDb2data.get("conn");
				}
				if(ipDb2data.containsKey("commonhash")) commonValue=(Hashtable)ipDb2data.get("commonhash");	
			
			}
			/*int allFlag = 0;
			for(int k=0;k<dbs.length;k++){
				String dbStr = dbs[k];					
				boolean db2IsOK = false;
				dao = new DBDao();
				try{
					db2IsOK = dao.getDB2IsOK(vo.getIpAddress(), Integer.parseInt(vo.getPort()), vo.getDbName(), vo.getUser(), vo.getPassword());
				}catch(Exception e){
					e.printStackTrace();
					db2IsOK = false;
				}finally{
					dao.close();
				}
				if (!db2IsOK){
					allFlag =1;
				}
			}
			if (allFlag == 0){
				runstr = "��������";
			}*/

			request.setAttribute("runstr", runstr);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"DB2Ping","ConnectUtilization",starttime1,totime1);
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
			p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);

			//ping?��???
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);

			//imgurlhash
			imgurlhash.put("ConnectUtilization","resource/image/jfreechart/"+newip+"ConnectUtilization"+".png");    
			
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			request.setAttribute("returnhash",returnhash);
			request.setAttribute("commonValue",commonValue);
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/db2conn.jsp";
    }
	
	private String db2log()
    {    	   
		DBVo vo = new DBVo();
		
		
		Hashtable returnhash = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		Hashtable commonValue = new Hashtable();
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
			String runstr = "<font color=red>�����ݿ����ֹͣ</font>";
			String[] dbs = vo.getDbName().split(",");
//			Hashtable allDb2data = ShareData.getAlldb2data();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String sip = hex+":"+vo.getId();
			Hashtable monitorValue = dao.getDB2DataByServerip(sip);
			Hashtable allDb2data = (Hashtable)monitorValue.get("allDb2Data");
			Hashtable ipDb2data = new Hashtable();
			if(allDb2data != null && allDb2data.size()>0){
				if(allDb2data.containsKey(vo.getIpAddress())){
					ipDb2data = (Hashtable)allDb2data.get(vo.getIpAddress());
					if(ipDb2data.containsKey("status")){
						String p_status = (String)ipDb2data.get("status");
						if(p_status != null && p_status.length()>0){
							if("1".equalsIgnoreCase(p_status)){
								runstr = "��������";
							}
						}
					}
				}
				if(ipDb2data.containsKey("log")){
					returnhash=(Hashtable)ipDb2data.get("log");
				}
				if(ipDb2data.containsKey("commonhash")) commonValue=(Hashtable)ipDb2data.get("commonhash");	
			
			}
			/*int allFlag = 0;
			for(int k=0;k<dbs.length;k++){
				String dbStr = dbs[k];					
				boolean db2IsOK = false;
				dao = new DBDao();
				try{
					db2IsOK = dao.getDB2IsOK(vo.getIpAddress(), Integer.parseInt(vo.getPort()), vo.getDbName(), vo.getUser(), vo.getPassword());
				}catch(Exception e){
					e.printStackTrace();
					db2IsOK = false;
				}finally{
					dao.close();
				}
				if (!db2IsOK){
					allFlag =1;
				}
			}
			if (allFlag == 0){
				runstr = "��������";
			}*/

			request.setAttribute("runstr", runstr);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"DB2Ping","ConnectUtilization",starttime1,totime1);
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
			p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);

			//ping?��???
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);

			//imgurlhash
			imgurlhash.put("ConnectUtilization","resource/image/jfreechart/"+newip+"ConnectUtilization"+".png");    
			
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			request.setAttribute("returnhash",returnhash);
			request.setAttribute("commonValue",commonValue); 
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/db2log.jsp";
    }
	
	private String db2lock()
    {    	   
		DBVo vo = new DBVo();
		
		
		Hashtable returnhash = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		Hashtable commonValue = new Hashtable();
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
			String runstr = "<font color=red>�����ݿ����ֹͣ</font>";
			String[] dbs = vo.getDbName().split(",");
//			Hashtable allDb2data = ShareData.getAlldb2data();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String sip = hex+":"+vo.getId();
			Hashtable monitorValue = dao.getDB2DataByServerip(sip);
			Hashtable allDb2data = (Hashtable)monitorValue.get("allDb2Data");
			Hashtable ipDb2data = new Hashtable();
			if(allDb2data != null && allDb2data.size()>0){
				if(allDb2data.containsKey(vo.getIpAddress())){
					ipDb2data = (Hashtable)allDb2data.get(vo.getIpAddress());
					if(ipDb2data.containsKey("status")){
						String p_status = (String)ipDb2data.get("status");
						if(p_status != null && p_status.length()>0){
							if("1".equalsIgnoreCase(p_status)){
								runstr = "��������";
							}
						}
					}
				}
				if(ipDb2data.containsKey("poolInfo")){
					returnhash=(Hashtable)ipDb2data.get("poolInfo");
				}
				if(ipDb2data.containsKey("commonhash")) commonValue=(Hashtable)ipDb2data.get("commonhash");	
			
			}


			request.setAttribute("runstr", runstr);
			

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"DB2Ping","ConnectUtilization",starttime1,totime1);
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

			//ping
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);

			//imgurlhash
			//imgurlhash.put("ConnectUtilization","resource/image/jfreechart/"+newip+"ConnectUtilization"+".png");    
			
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			request.setAttribute("returnhash",returnhash);
			request.setAttribute("commonValue",commonValue); 
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/db2lock.jsp";
    }
	

	private String db2session()
    {    	   
		DBVo vo = new DBVo();
		
		
		Hashtable returnhash = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		Hashtable commonValue = new Hashtable();
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
			String runstr = "<font color=red>�����ݿ����ֹͣ</font>";
			String[] dbs = vo.getDbName().split(",");
//			Hashtable allDb2data = ShareData.getAlldb2data();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String sip = hex+":"+vo.getId();
			Hashtable monitorValue = dao.getDB2DataByServerip(sip);
			Hashtable allDb2data = (Hashtable)monitorValue.get("allDb2Data");
			Hashtable ipDb2data = new Hashtable();
			if(allDb2data != null && allDb2data.size()>0){
				if(allDb2data.containsKey(vo.getIpAddress())){
					ipDb2data = (Hashtable)allDb2data.get(vo.getIpAddress());
					if(ipDb2data.containsKey("status")){
						String p_status = (String)ipDb2data.get("status");
						if(p_status != null && p_status.length()>0){
							if("1".equalsIgnoreCase(p_status)){
								runstr = "��������";
							}
						}
					}
				}
				if(ipDb2data.containsKey("session")){
					returnhash=(Hashtable)ipDb2data.get("session");
				}
				if(ipDb2data.containsKey("commonhash")) commonValue=(Hashtable)ipDb2data.get("commonhash");	
				
			}
			/*int allFlag = 0;
			for(int k=0;k<dbs.length;k++){
				String dbStr = dbs[k];					
				boolean db2IsOK = false;
				dao = new DBDao();
				try{
					db2IsOK = dao.getDB2IsOK(vo.getIpAddress(), Integer.parseInt(vo.getPort()), vo.getDbName(), vo.getUser(), vo.getPassword());
				}catch(Exception e){
					e.printStackTrace();
					db2IsOK = false;
				}finally{
					dao.close();
				}
				if (!db2IsOK){
					allFlag =1;
				}
			}
			if (allFlag == 0){
				runstr = "��������";
			}
*/
			request.setAttribute("runstr", runstr);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"DB2Ping","ConnectUtilization",starttime1,totime1);
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
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);

			//imgurlhash
			//imgurlhash.put("ConnectUtilization","resource/image/jfreechart/"+newip+"ConnectUtilization"+".png");    
			
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			request.setAttribute("returnhash",returnhash);
			request.setAttribute("commonValue",commonValue);
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/db2session.jsp";
    }

	private String db2event()
    {    	   
		DBVo vo = new DBVo();
		
		
		Hashtable returnhash = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		Hashtable commonValue = new Hashtable();
		String pingconavg ="0";
		double avgpingcon = 0;
		String flag = getParaValue("flag");
		request.setAttribute("flag",flag);
		List list = new ArrayList();
		int status =99;
		int level1 = 99;
		String b_time ="";
		String t_time = "";
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
			String runstr = "<font color=red>�����ݿ����ֹͣ</font>";
			String[] dbs = vo.getDbName().split(",");
	//			int allFlag = 0;
//			for(int k=0;k<dbs.length;k++){
//				String dbStr = dbs[k];					
//				boolean db2IsOK = false;
//				dao = new DBDao();
//				try{
//					db2IsOK = dao.getDB2IsOK(vo.getIpAddress(), Integer.parseInt(vo.getPort()), vo.getDbName(), vo.getUser(), vo.getPassword());
//				}catch(Exception e){
//					e.printStackTrace();
//					db2IsOK = false;
//				}finally{
//					dao.close();
//				}
//				if (!db2IsOK){
//					allFlag =1;
//				}
//			}
//			if (allFlag == 0){
//				runstr = "��������";
//			}
//			Hashtable allDb2data = ShareData.getAlldb2data();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String sip = hex+":"+vo.getId();
			Hashtable monitorValue = dao.getDB2DataByServerip(sip);
			Hashtable allDb2data = (Hashtable)monitorValue.get("allDb2Data");
			Hashtable ipDb2data = new Hashtable();
			if(allDb2data != null && allDb2data.size()>0){
				if(allDb2data.containsKey(vo.getIpAddress())){
					ipDb2data = (Hashtable)allDb2data.get(vo.getIpAddress());
					if(ipDb2data.containsKey("status")){
						String p_status = (String)ipDb2data.get("status");
						if(p_status != null && p_status.length()>0){
							if("1".equalsIgnoreCase(p_status)){
								runstr = "��������";
							}
						}
					}
				}
				if(ipDb2data.containsKey("commonhash")) commonValue=(Hashtable)ipDb2data.get("commonhash");	
			}
			
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
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"DB2Ping","ConnectUtilization",starttime1,totime1);
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
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);

			//imgurlhash
			//imgurlhash.put("ConnectUtilization","resource/image/jfreechart/"+newip+"ConnectUtilization"+".png");    
			
			  avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130);    
				
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
					try{
						list = eventdao.getQuery(starttime1,totime1,"db",status+"",level1+"",user.getBusinessids(),vo.getId());
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						eventdao.close();
					}
							
				}catch(Exception ex){
					ex.printStackTrace();
				}
				request.setAttribute("list", list);
				request.setAttribute("startdate", b_time);
				request.setAttribute("todate", t_time);
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			request.setAttribute("commonValue",commonValue);
		        
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/db2event.jsp";
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
        if(action.equals("db2ping"))
            return db2ping();
        if(action.equals("db2space"))
            return db2space();
        if(action.equals("db2pool"))
            return db2pool();
        if(action.equals("db2lock"))
            return db2lock();
        if(action.equals("db2log"))
            return db2log();
        if(action.equals("db2conn"))
            return db2conn();
        if(action.equals("db2table"))
            return db2table();
        if(action.equals("db2session"))
            return db2session();
        if(action.equals("db2event"))
            return db2event();
        if(action.equals("sychronizeData"))
        {
        	return sychronizeData();
        }
        if(action.equals("isDatabaseOK"))
        	return isDatabaseOK();
        //HONGLI START 
        if(action.equals("db2ManagerPingReport")){
        	return db2ManagerPingReport();
        }
        if(action.equals("db2ManagerPingReportQuery")){
        	return db2ManagerPingReportQuery();
        }
        if(action.equals("db2ManagerNatureReport")){
        	return db2ManagerNatureReport();
        }
        if(action.equals("db2ManagerNatureReportQuery")){
        	return db2ManagerNatureReportQuery();
        }
        if(action.equals("db2ManagerCldReport")){
        	return db2ManagerCldReport();
        }
        if(action.equals("db2ManagerCldReportQuery")){
        	return db2ManagerCldReportQuery();
        }
        if(action.equals("db2ManagerEventReport")){
        	return db2ManagerEventReport();
        }
        if(action.equals("db2ManagerEventReportQuery")){
        	return db2ManagerEventReportQuery();
        }
        //HONGLI END
		setErrorCode(ErrorMessage.ACTION_NO_FOUND);
		return null;
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
			if(dbPage.equals("db2ping"))
			{
				return db2ping();
			}
			if(dbPage.equals("db2pool"))
			{
				return db2pool();
			}
			if(dbPage.equals("db2session"))
			{
				return db2session();
			}
			if(dbPage.equals("db2event"))
			{
				return db2event();
			}
			return "/application/db/db2ping";
		}
		private String isDatabaseOK()
		{
			DBDao dbdao = null;
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
			boolean db2IsOK = false;
			try {
				//ddddddd
				dbdao = new DBDao();
				vo = (DBVo)dbdao.findByID(id);
				String dbname = vo.getDbName();
				db2IsOK = dbdao.getDB2IsOK(myip, port, dbname, myUser, myPassword);
				
				request.setAttribute("dbname", dbname);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (dbdao != null)
					dbdao.close();
			}
			request.setAttribute("oracleIsOK", db2IsOK);
			return "/tool/dbisok.jsp";
		}
		
		/**
		 * @author HONGLI
		 * date 2010-11-11
		 * DB2�����Ա���ҳ
		 * @return
		 */
		public String db2ManagerPingReport(){
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
//				Hashtable allsqlserverdata = ShareData.getAlldb2data();
				IpTranslation tranfer = new IpTranslation();
				String hex = tranfer.formIpToHex(vo.getIpAddress());
				String sip = hex+":"+vo.getId();
				Hashtable monitorValue = dao.getDB2DataByServerip(sip);
				Hashtable allDb2data = (Hashtable)monitorValue.get("allDb2Data");
				Hashtable ipdb2data = new Hashtable();
				if(allDb2data != null && allDb2data.size()>0){
					if(allDb2data.containsKey(vo.getIpAddress())){
						ipdb2data = (Hashtable)allDb2data.get(vo.getIpAddress());
						if(ipdb2data.containsKey("status")){
							String p_status = (String)ipdb2data.get("status");
							if(p_status != null && p_status.length()>0){
								if("1".equalsIgnoreCase(p_status)){
									pingnow = "100.0";
								}
							}
						}
					}
				}
				String newip=SysUtil.doip(vo.getIpAddress());	
				request.setAttribute("newIp", newip);
				Hashtable ConnectUtilizationhash = new Hashtable();
				I_HostCollectData hostmanager=new HostCollectDataManager();
				try{
					ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"DB2Ping","ConnectUtilization",starttime,totime);
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
				if(pingmin != null){
					pingmin = pingmin.replace("%", "");//��С��ͨ��
				}
				avgpingcon = new Double(pingconavg+"").doubleValue();
				
				p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);//��ͼ
			}catch(Exception e){
				e.printStackTrace();
			}
			request.setAttribute("avgpingcon", avgpingcon);
			request.setAttribute("pingmax", pingmax);
			request.setAttribute("pingmin", pingmin);
			request.setAttribute("pingnow",pingnow); 
			return "/capreport/db/showDbDB2Report.jsp";
		}
		
		/**
		 * @author HONGLI 
		 * date 2010-11-11
		 * �����Ա��� ��ʱ���ѯ
		 * @return
		 */
		public String db2ManagerPingReportQuery(){
			return db2ManagerPingReport();
		}
		
		/**
		 *@author HONGLI 
		 * date 2010-11-11
		 * ���ܱ��� 
		 * @return
		 */
		public String db2ManagerNatureReport(){
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
			Hashtable spaceInfo = new Hashtable();
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
//				Hashtable alldb2data = ShareData.getAlldb2data();
				IpTranslation tranfer = new IpTranslation();
				String hex = tranfer.formIpToHex(vo.getIpAddress());
				String sip = hex+":"+vo.getId();
				dao = new DBDao();
				Hashtable monitorValue = null;
				Hashtable alldb2data = null;
				Hashtable ipdb2data = new Hashtable(); 
				try {
					monitorValue = dao.getDB2DataByServerip(sip);
				} catch (Exception e) {
					e.printStackTrace();
				} finally{
					dao.close();
				}
				if(monitorValue != null && monitorValue.containsKey("allDb2Data")){
					alldb2data = (Hashtable)monitorValue.get("allDb2Data");
				}
				if(alldb2data != null && alldb2data.size()>0){
					if(alldb2data.containsKey(vo.getIpAddress())){
						ipdb2data = (Hashtable)alldb2data.get(vo.getIpAddress());
						if(ipdb2data.containsKey("status")){
							String p_status = (String)ipdb2data.get("status");
							if(p_status != null && p_status.length()>0){
								if("1".equalsIgnoreCase(p_status)){
									pingnow = "100.0";
								}
							}
						}
						if(ipdb2data.containsKey("spaceInfo")){
							spaceInfo = (Hashtable)ipdb2data.get("spaceInfo");
						}
					}
				}
				String newip=SysUtil.doip(vo.getIpAddress());	
				request.setAttribute("newIp", newip);
				Hashtable ConnectUtilizationhash = new Hashtable();
				I_HostCollectData hostmanager=new HostCollectDataManager();
				try{
					ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"DB2Ping","ConnectUtilization",starttime,totime);
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
				if(pingmax == null || !"".equals(pingmax)){
					pingmax = pingmax.replace("%", "");//ƽ����ͨ��
				}
				if(pingmin != null || !"".equals(pingmin)){
					pingmin = pingmin.replace("%", "");//ƽ����ͨ��
				}
				avgpingcon = new Double(pingconavg+"").doubleValue();
				
				p_draw_line(ConnectUtilizationhash,"��ͨ��",newip+"ConnectUtilization",740,150);//��ͼ
			}catch(Exception e){
				e.printStackTrace();
			}
			request.setAttribute("spaceInfo", spaceInfo);
			request.setAttribute("avgpingcon", avgpingcon+"");
			request.setAttribute("pingmax", pingmax);
			request.setAttribute("pingmin", pingmin);
			request.setAttribute("pingnow",pingnow); 
			return "/capreport/db/showDbDB2NatureReport.jsp";
		}
		
		/**
		 *@author HONGLI 
		 * date 2010-11-11
		 * ���ܱ���  �����ڲ�ѯ
		 * @return
		 */
		public String db2ManagerNatureReportQuery(){
			return db2ManagerNatureReport();
		}
		
		/**
		 *@author HONGLI 
		 * date 2010-11-11
		 * �ۺϱ���
		 * @return
		 */
		public String db2ManagerCldReport(){
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
			Hashtable spaceInfo = new Hashtable();
			String downnum = "";
//			���ݿ����еȼ�=====================
			String grade = "��";
			Hashtable mems = new Hashtable();//�ڴ���Ϣ
			Hashtable sysValue = new Hashtable();
			Hashtable conn =  new Hashtable();//������Ϣ
			Hashtable poolInfo =  new Hashtable();	
			Hashtable log =  new Hashtable();	
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
//				Hashtable allsqlserverdata = ShareData.getAlldb2data();
				IpTranslation tranfer = new IpTranslation();
				String hex = tranfer.formIpToHex(vo.getIpAddress());
				String sip = hex+":"+vo.getId();
				dao = new DBDao();
				Hashtable monitorValue = null;
				Hashtable allDb2data = null;
				Hashtable ipDb2data = new Hashtable(); 
				try {
					monitorValue = dao.getDB2DataByServerip(sip);
				} catch (Exception e) {
					e.printStackTrace();
				} finally{
					dao.close();
				}
				if(monitorValue != null && monitorValue.containsKey("allDb2Data")){
					allDb2data = (Hashtable)monitorValue.get("allDb2Data");
				}
				if(allDb2data != null && allDb2data.size()>0){
					if(allDb2data.containsKey(vo.getIpAddress())){
						ipDb2data = (Hashtable)allDb2data.get(vo.getIpAddress());
						if(ipDb2data.containsKey("status")){
							String p_status = (String)ipDb2data.get("status");
							if(p_status != null && p_status.length()>0){
								if("1".equalsIgnoreCase(p_status)){
									runstr = "��������";
									pingnow = "100.0";
								}
							}
						}
						if(ipDb2data.containsKey("spaceInfo")){
							spaceInfo = (Hashtable)ipDb2data.get("spaceInfo");
						}
						if(ipDb2data.containsKey("retValue")){
							mems = (Hashtable)((Hashtable)ipDb2data.get("retValue")).get("mems");
						}
						if(ipDb2data.containsKey("sysValue")){
							sysValue = (Hashtable)ipDb2data.get("sysValue");
						}
						if(ipDb2data.containsKey("conn")){
							conn = (Hashtable)ipDb2data.get("conn");
						}
						if(ipDb2data.containsKey("poolInfo")){
							poolInfo=(Hashtable)ipDb2data.get("poolInfo");
						}
						if(ipDb2data.containsKey("log")){
							log =(Hashtable)ipDb2data.get("log");
						}
					}
				}
				String newip=SysUtil.doip(vo.getIpAddress());	
				request.setAttribute("newip", newip);
				Hashtable ConnectUtilizationhash = new Hashtable();
				I_HostCollectData hostmanager=new HostCollectDataManager();
				try{
					ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"DB2Ping","ConnectUtilization",starttime,totime);
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
			request.setAttribute("log", log);
			request.setAttribute("conn", conn);
			request.setAttribute("poolInfo", poolInfo);
			request.setAttribute("sysValue", sysValue);
			request.setAttribute("downnum", downnum);
			request.setAttribute("mems", mems);
			request.setAttribute("grade", grade);
			request.setAttribute("vo", vo);
			request.setAttribute("runstr", runstr);
			request.setAttribute("typevo", typevo);
			request.setAttribute("spaceInfo", spaceInfo);
			request.setAttribute("avgpingcon", avgpingcon+"");
			request.setAttribute("pingmax", pingmax);
			request.setAttribute("pingmin", pingmin);
			request.setAttribute("pingnow",pingnow); 
			return "/capreport/db/showDbDB2CldReport.jsp";
		}
		
		/**
		 *@author HONGLI 
		 * date 2010-11-11
		 * �ۺϱ���  �����ڲ�ѯ
		 * @return
		 */
		public String db2ManagerCldReportQuery(){
			return db2ManagerCldReport();
		}
		
		/**
		 *@author HONGLI 
		 * date 2010-11-11
		 * �¼�����   
		 * @return
		 */
		public String db2ManagerEventReport(){
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
					ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"DB2Ping","ConnectUtilization",starttime,totime);
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
			return "/capreport/db/showDbDB2EventReport.jsp";
		}
		
		/**
		 *@author HONGLI 
		 * date 2010-11-11
		 * �¼�����   �����ڲ�ѯ
		 * @return
		 */
		public String db2ManagerEventReportQuery(){
			return db2ManagerEventReport();
		}
}