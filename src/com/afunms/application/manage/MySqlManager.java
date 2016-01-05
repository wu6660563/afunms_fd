package com.afunms.application.manage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;

import com.afunms.application.dao.DBDao;
import com.afunms.application.dao.DBTypeDao;
import com.afunms.application.dao.OraclePartsDao;
import com.afunms.application.model.DBTypeVo;
import com.afunms.application.model.DBVo;
import com.afunms.application.model.OracleEntity;
import com.afunms.application.util.DBPool;
import com.afunms.application.util.DBRefreshHelper;
import com.afunms.application.util.IpTranslation;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.ChartGraph;
import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.event.dao.EventListDao;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.api.I_HostCollectData;
import com.afunms.polling.base.Node;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.report.jfree.ChartCreator;
import com.afunms.system.model.User;
import com.afunms.topology.util.KeyGenerator;

public class MySqlManager extends BaseManager implements ManagerInterface{
	
	public static Hashtable rsc_type_ht = new Hashtable();
	public static Hashtable req_mode_ht = new Hashtable();
	public static Hashtable mode_ht = new Hashtable();
	public static Hashtable req_status_ht = new Hashtable();
	public static Hashtable req_ownertype_ht = new Hashtable();
	
	/**
	 * ��ѯ���ݿ������м�¼
	 * @return
	 */
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
    
	/**
	 * ��ӷ���
	 * @return
	 */
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
	/**
	 * ɾ������
	 * @return
	 */
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
	/**
	 * �޸ķ���
	 * @return
	 */
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
	/**
	 * ��ͨ��
	 * @return
	 */
	private String mysqlping()
    {    	   
		DBVo vo = new DBVo();
		
		Vector sysValue = new Vector();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		String pingconavg ="0";
		String basePath=""; //����·��
		String dataPath=""; //����·��
		String logerrorPath=""; //����·��
		String version="";  //���ݿ�汾
		String hostOS="";   //����������ϵͳ
		int doneFlag = 0;
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
//			Hashtable allData = ShareData.getMySqlmonitordata();
//			Hashtable ipData = ShareData.getMySqlmonitordata();
//			if(allData != null && allData.size()>0){
//				ipData = (Hashtable)allData.get(vo.getIpAddress());
//				if(ipData != null && ipData.size()>0){
//					runstr = (String)ipData.get("runningflag");
//					String dbnames = vo.getDbName();
//					String[] dbs = dbnames.split(",");
//					for(int k=0;k<dbs.length;k++){
//						//�ж��Ƿ��Ѿ���ȡ�˵�ǰ��������Ϣ
//						if(doneFlag == 1)break;
//						String dbStr = dbs[k];
//						if(ipData.containsKey(dbStr)){
//							Hashtable returnValue = new Hashtable();
//							returnValue = (Hashtable)ipData.get(dbStr);
//							if(returnValue != null && returnValue.size()>0){
//								if(returnValue.containsKey("configVal")){
//									Hashtable configVal = (Hashtable)returnValue.get("configVal");
//									if(configVal.containsKey("basePath"))basePath=(String)configVal.get("basePath");
//									if(configVal.containsKey("dataPath"))dataPath=(String)configVal.get("dataPath");
//									if(configVal.containsKey("logerrorPath"))logerrorPath=(String)configVal.get("logerrorPath");
//									if(configVal.containsKey("version"))version=(String)configVal.get("version");
//									if(configVal.containsKey("hostOS"))hostOS=(String)configVal.get("hostOS");
//									doneFlag = 1;
//								}
//							}
//						}
//					}
//					
//				}
//			}
//			Hashtable allData = ShareData.getMySqlmonitordata();
//			Hashtable ipData = ShareData.getMySqlmonitordata();
//			if(allData != null && allData.size()>0){
//				ipData = (Hashtable)allData.get(vo.getIpAddress());
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+vo.getId();
			DBDao dbDao = null;
			Hashtable ipData = null;
			try {
				dbDao = new DBDao();
				ipData = dbDao.getMysqlDataByServerip(serverip);
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				if(dbDao != null){
					dbDao.close();
				}
			}
			if(ipData != null && ipData.size()>0){
				runstr = (String)ipData.get("runningflag");
				String dbnames = vo.getDbName();
				String[] dbs = dbnames.split(",");
				for(int k=0;k<dbs.length;k++){
					//�ж��Ƿ��Ѿ���ȡ�˵�ǰ��������Ϣ
					if(doneFlag == 1)break;
					String dbStr = dbs[k];
					if(ipData.containsKey(dbStr)){
						Hashtable returnValue = new Hashtable();
						returnValue = (Hashtable)ipData.get(dbStr);
						if(returnValue != null && returnValue.size()>0){
							if(returnValue.containsKey("configVal")){
								Hashtable configVal = (Hashtable)returnValue.get("configVal");
								if(configVal.containsKey("basePath"))basePath=(String)configVal.get("basePath");
								if(configVal.containsKey("dataPath"))dataPath=(String)configVal.get("dataPath");
								if(configVal.containsKey("logerrorPath"))logerrorPath=(String)configVal.get("logerrorPath");
								if(configVal.containsKey("version"))version=(String)configVal.get("version");
								if(configVal.containsKey("hostOS"))hostOS=(String)configVal.get("hostOS");
								doneFlag = 1;
							}
						}
					}
				}
				
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
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"MYPing","ConnectUtilization",starttime1,totime1);
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
			imgurlhash.put("ConnectUtilization","resource/image/jfreechart/"+newip+"ConnectUtilization"+".png");    
			
			  double avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			
			request.setAttribute("basePath",basePath);
			request.setAttribute("dataPath",dataPath);
			request.setAttribute("logerrorPath",logerrorPath);
			request.setAttribute("version",version);
			request.setAttribute("hostOS",hostOS);
			request.setAttribute("avgpingcon", avgpingcon);
		        
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//dao.close();
		}
		return "/application/db/mysqlping.jsp";
    }
	
	/**
	 * MYSQL���ݿ�������Ϣ
	 * @return
	 */
	private String mysqlconnect()
    {    	   
		DBVo vo = new DBVo();
		
		String id = getParaValue("id");
		request.setAttribute("id", id);
		Vector sysValue = new Vector();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		List sessionlist = new ArrayList();
		String pingconavg ="0";
		String basePath=""; //����·��
		String dataPath=""; //����·��
		String logerrorPath=""; //����·��
		String version="";  //���ݿ�汾
		String hostOS="";   //����������ϵͳ
		int doneFlag = 0;
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
//			Hashtable allData = ShareData.getMySqlmonitordata();
//			Hashtable ipData = ShareData.getMySqlmonitordata();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+vo.getId();
			DBDao dbDao = null;
			Hashtable ipData = null;
			try {
				dbDao = new DBDao();
				ipData = dbDao.getMysqlDataByServerip(serverip);
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				if(dbDao != null){
					dbDao.close();
				}
			}
//			if(allData != null && allData.size()>0){
//			ipData = (Hashtable)allData.get(vo.getIpAddress());
			if(ipData != null && ipData.size()>0){
				runstr = (String)ipData.get("runningflag");
				String dbnames = vo.getDbName();
				String[] dbs = dbnames.split(",");
				for(int k=0;k<dbs.length;k++){
					//�ж��Ƿ��Ѿ���ȡ�˵�ǰ��������Ϣ
					if(doneFlag == 1)break;
					String dbStr = dbs[k];
					if(ipData.containsKey(dbStr)){
						Hashtable returnValue = new Hashtable();
						returnValue = (Hashtable)ipData.get(dbStr);
						if(returnValue != null && returnValue.size()>0){
							if(doneFlag == 0){
								//�ж��Ƿ��Ѿ���ȡ�˵�ǰ��������Ϣ
								if(returnValue.containsKey("configVal")){
									Hashtable configVal = (Hashtable)returnValue.get("configVal");
									if(configVal.containsKey("basePath"))basePath=(String)configVal.get("basePath");
									if(configVal.containsKey("dataPath"))dataPath=(String)configVal.get("dataPath");
									if(configVal.containsKey("logerrorPath"))logerrorPath=(String)configVal.get("logerrorPath");
									if(configVal.containsKey("version"))version=(String)configVal.get("version");
									if(configVal.containsKey("hostOS"))hostOS=(String)configVal.get("hostOS");
									doneFlag = 1;
								}
							}
							if(returnValue.containsKey("sessionsDetail")){
								//�������ݿ�������Ϣ
								sessionlist.add((List)returnValue.get("sessionsDetail"));
							}
						}
					}
				}
				
			}
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
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"MYPing","ConnectUtilization",starttime1,totime1);
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
			
			  double avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			
			request.setAttribute("basePath",basePath);
			request.setAttribute("dataPath",dataPath);
			request.setAttribute("logerrorPath",logerrorPath);
			request.setAttribute("version",version);
			request.setAttribute("hostOS",hostOS);
			request.setAttribute("sessionlist",sessionlist);
			request.setAttribute("avgpingcon", avgpingcon);
		        
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//dao.close();
		}
		return "/application/db/mysqlconnect.jsp";
    }
	
	/**
	 * MYSQL���ݿ����Ϣ
	 * @return
	 */
	private String mysqltables()
    {    	   
		DBVo vo = new DBVo();
		String id = getParaValue("id");
		request.setAttribute("id", id);
		
		Vector sysValue = new Vector();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		Hashtable tablesHash = new Hashtable();
		String pingconavg ="0";
		String basePath=""; //����·��
		String dataPath=""; //����·��
		String logerrorPath=""; //����·��
		String version="";  //���ݿ�汾
		String hostOS="";   //����������ϵͳ
		int doneFlag = 0;
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
//			Hashtable allData = ShareData.getMySqlmonitordata();
//			Hashtable ipData = ShareData.getMySqlmonitordata();
//			if(allData != null && allData.size()>0){
//				ipData = (Hashtable)allData.get(vo.getIpAddress());
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+vo.getId();
			DBDao dbDao = null;
			Hashtable ipData = null;
			try {
				dbDao = new DBDao();
				ipData = dbDao.getMysqlDataByServerip(serverip);
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				if(dbDao != null){
					dbDao.close();
				}
			}
			if(ipData != null && ipData.size()>0){
				
				runstr = (String)ipData.get("runningflag");
				String dbnames = vo.getDbName();
				String[] dbs = dbnames.split(",");
				for(int k=0;k<dbs.length;k++){
					//�ж��Ƿ��Ѿ���ȡ�˵�ǰ��������Ϣ
					//if(doneFlag == 1)break;
					String dbStr = dbs[k];
					if(ipData.containsKey(dbStr)){
						Hashtable returnValue = new Hashtable();
						returnValue = (Hashtable)ipData.get(dbStr);
						if(returnValue != null && returnValue.size()>0){

							if(returnValue.containsKey("tablesDetail")){
								//�������ݿ�������Ϣ
								tablesHash.put(dbStr,(List)returnValue.get("tablesDetail"));
							}

							if(doneFlag == 0){
								//�ж��Ƿ��Ѿ���ȡ�˵�ǰ��������Ϣ
								if(returnValue.containsKey("configVal")){
									Hashtable configVal = (Hashtable)returnValue.get("configVal");
									if(configVal.containsKey("basePath"))basePath=(String)configVal.get("basePath");
									if(configVal.containsKey("dataPath"))dataPath=(String)configVal.get("dataPath");
									if(configVal.containsKey("logerrorPath"))logerrorPath=(String)configVal.get("logerrorPath");
									if(configVal.containsKey("version"))version=(String)configVal.get("version");
									if(configVal.containsKey("hostOS"))hostOS=(String)configVal.get("hostOS");
									doneFlag = 1;
								}
							}
						}
					}
				}
				
			}
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
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"MYPing","ConnectUtilization",starttime1,totime1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if (ConnectUtilizationhash.get("avgpingcon")!=null)
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
			}

			//pingƽ��ֵ
			maxhash = new Hashtable();
			maxhash.put("avgpingcon",pingconavg);

			//imgurlhash
			//imgurlhash.put("ConnectUtilization","resource/image/jfreechart/"+newip+"ConnectUtilization"+".png");    
			
			  double avgpingcon = new Double(pingconavg+"").doubleValue();
			  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			
			request.setAttribute("imgurl",imgurlhash);
			request.setAttribute("max",maxhash);
			request.setAttribute("chart1",chart1);
			
			request.setAttribute("basePath",basePath);
			request.setAttribute("dataPath",dataPath);
			request.setAttribute("logerrorPath",logerrorPath);
			request.setAttribute("version",version);
			request.setAttribute("hostOS",hostOS);
			request.setAttribute("tablesHash",tablesHash);
			request.setAttribute("avgpingcon", avgpingcon);
		        
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//dao.close();
		}
		return "/application/db/mysqltable.jsp";
    }
	
	/**
	 * ������Ϣ
	 * @return
	 */
	private String mysqlsys()
    {    	   
		DBVo vo = new DBVo();
		
		
		Vector variables = new Vector();
		Vector status = new Vector();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		Vector tableinfo_v = new Vector();
		Vector tableinfo_v1 = new Vector();
		Vector tableinfo_v2 = new Vector();
		Vector tableinfo_v3 = new Vector();
		String pingconavg ="0";
		String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
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
			String runstr = "����ֹͣ";
			 
//			Hashtable allData = ShareData.getMySqlmonitordata();
//			Hashtable ipData = ShareData.getMySqlmonitordata();
//			if(allData != null && allData.size()>0){
//				ipData = (Hashtable)allData.get(vo.getIpAddress());
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+vo.getId();
			DBDao dbDao = null;
			Hashtable ipData = null;
			try {
				dbDao = new DBDao();
				ipData = dbDao.getMysqlDataByServerip(serverip);
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				if(dbDao != null){
					dbDao.close();
				}
			}
			if(ipData != null && ipData.size()>0){
				runstr = (String)ipData.get("runningflag");
			}
			if(ipData.containsKey("status")){
				String p_status = (String)ipData.get("status");
				if(p_status != null && p_status.length()>0){
					if("1".equalsIgnoreCase(p_status)){
						runstr = "��������";
					}
				}
			}
//			}
			Hashtable returnVal=(Hashtable)ipData.get(vo.getDbName());
			/*dao = new DBDao();
			try{
				if(dao.getMySqlIsOk(vo.getIpAddress(), vo.getUser(), vo.getPassword(),vo.getDbName())){
					runstr = "��������";
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
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
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"MYPing","ConnectUtilization",starttime1,totime1);
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
			dao = new DBDao();
			try{
	variables=(Vector)returnVal.get("variables");
				//status=dao.getStatus(vo.getIpAddress(), vo.getUser(), vo.getPassword(), vo.getDbName());
				status=(Vector)returnVal.get("global_status");
				//tableinfo_v = dao.getDispose(vo.getIpAddress(), vo.getUser(), vo.getPassword(),vo.getDbName());
				tableinfo_v=(Vector)returnVal.get("dispose");
				//tableinfo_v1 = dao.getDispose1(vo.getIpAddress(), vo.getUser(), vo.getPassword(),vo.getDbName());
				tableinfo_v1=(Vector)returnVal.get("dispose1");
				//tableinfo_v2 = dao.getDispose2(vo.getIpAddress(), vo.getUser(), vo.getPassword(),vo.getDbName());
				tableinfo_v2=(Vector)returnVal.get("dispose2");
				//tableinfo_v3 = dao.getDispose3(vo.getIpAddress(), vo.getUser(), vo.getPassword(),vo.getDbName()); 
				tableinfo_v3=(Vector)returnVal.get("dispose3");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}
			  double avgpingcon = new Double(pingconavg+"").doubleValue();
			  
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  request.setAttribute("avgpingcon", avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130);   
		}catch(Exception e){
			e.printStackTrace();
		}
		for(int i =0;i<tableinfo_v.size();i++)
		{
			Hashtable ht = (Hashtable)tableinfo_v.get(i);
			String tablespace = ht.get("variable_name").toString();
		}
		request.setAttribute("imgurl",imgurlhash);
		request.setAttribute("max",maxhash);
		request.setAttribute("chart1",chart1);
		request.setAttribute("tableinfo_v",tableinfo_v);
		request.setAttribute("tableinfo_v1",tableinfo_v1);
		request.setAttribute("tableinfo_v2",tableinfo_v2);
		request.setAttribute("tableinfo_v3",tableinfo_v3);
		request.setAttribute("variables",variables);
		request.setAttribute("status", status);
		//request.setAttribute("avgpingcon", avgpingcon);
		return "/application/db/mysqlsys.jsp";
    }
  /**
   * ������Ϣ
   * @return
   */
	private String mysqlspace()
    {    	   
		DBVo vo = new DBVo();
		
		
		Vector sysValue = new Vector();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		Vector tableinfo_v = new Vector();
		Vector tableinfo_v1 = new Vector();
		Vector tableinfo_v2 = new Vector();
		Vector tableinfo_v3 = new Vector();
		Vector tableinfo_v4 = new Vector();
		Vector tableinfo_v5 = new Vector();
		Vector tableinfo_v6 = new Vector();
		String maxconnections="";   //���������
		String buffersize="";   //�ڴ�
		
		String basePath=""; //����·��
		String dataPath=""; //����·��
		String logerrorPath=""; //����·��
		String version="";  //���ݿ�汾
		String hostOS="";   //����������ϵͳ
		
		int doneFlag = 0;
		Vector Val = new Vector();
		String pingconavg ="0";
		String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
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
			String runstr = "����ֹͣ";
//			Hashtable allData = ShareData.getMySqlmonitordata();
//			Hashtable ipData = ShareData.getMySqlmonitordata();
//			if(allData != null && allData.size()>0){
//				ipData = (Hashtable)allData.get(vo.getIpAddress());
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+vo.getId();
			DBDao dbDao = null;
			Hashtable ipData = null;
			try {
				dbDao = new DBDao();
				ipData = dbDao.getMysqlDataByServerip(serverip);
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				if(dbDao != null){
					dbDao.close();
				}
			}
			if(ipData != null && ipData.size()>0){
				runstr = (String)ipData.get("runningflag");
				String dbnames = vo.getDbName();
				String[] dbs = dbnames.split(",");
				for(int k=0;k<dbs.length;k++){
					//�ж��Ƿ��Ѿ���ȡ�˵�ǰ��������Ϣ
					//if(doneFlag == 1)break;
					String dbStr = dbs[k];
					if(ipData.containsKey(dbStr)){
						Hashtable returnValue = new Hashtable();
						returnValue = (Hashtable)ipData.get(dbStr);
						if(returnValue != null && returnValue.size()>0){
							if(doneFlag == 0){
								//�ж��Ƿ��Ѿ���ȡ�˵�ǰ��������Ϣ
								if(returnValue.containsKey("configVal")){
									Hashtable configVal = (Hashtable)returnValue.get("configVal");
									if(configVal.containsKey("maxconnections"))maxconnections=(String)configVal.get("maxconnections");
									if(configVal.containsKey("buffersize"))buffersize=(String)configVal.get("buffersize");
									if(configVal.containsKey("basePath"))basePath=(String)configVal.get("basePath");
									if(configVal.containsKey("dataPath"))dataPath=(String)configVal.get("dataPath");
									if(configVal.containsKey("logerrorPath"))logerrorPath=(String)configVal.get("logerrorPath");
									if(configVal.containsKey("version"))version=(String)configVal.get("version");
									if(configVal.containsKey("hostOS"))hostOS=(String)configVal.get("hostOS");
									doneFlag = 1;
								}
								if(returnValue.containsKey("Val")){
									Val = (Vector)returnValue.get("Val");
								}
							}
						}
					}
				}
			}
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
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"MYPing","ConnectUtilization",starttime1,totime1);
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

//			tableinfo_v = dao.getMySqlPerfom(vo.getIpAddress(), vo.getUser(), vo.getPassword(),vo.getDbName());
//			tableinfo_v1 = dao.getMySqlPerfom1(vo.getIpAddress(), vo.getUser(), vo.getPassword(),vo.getDbName());
//			tableinfo_v2 = dao.getMySqlPerfom2(vo.getIpAddress(), vo.getUser(), vo.getPassword(),vo.getDbName());
//			tableinfo_v3 = dao.getMySqlPerfom3(vo.getIpAddress(), vo.getUser(), vo.getPassword(),vo.getDbName());
//			tableinfo_v4 = dao.getMySqlPerfom4(vo.getIpAddress(), vo.getUser(), vo.getPassword(),vo.getDbName());
//			tableinfo_v5 = dao.getMySqlPerfom5(vo.getIpAddress(), vo.getUser(), vo.getPassword(),vo.getDbName());
//			tableinfo_v6 = dao.getMySqlPerfom6(vo.getIpAddress(), vo.getUser(), vo.getPassword(),vo.getDbName());
			double avgpingcon = new Double(pingconavg+"").doubleValue();
			  
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			  request.setAttribute("avgpingcon", avgpingcon);
		}catch(Exception e){
			e.printStackTrace();
		}
		for(int i =0;i<tableinfo_v.size();i++)
		{
			Hashtable ht = (Hashtable)tableinfo_v.get(i);
			String tablespace = ht.get("variable_name").toString();
		}
		request.setAttribute("imgurl",imgurlhash);
		request.setAttribute("max",maxhash);
		request.setAttribute("chart1",chart1);
//		request.setAttribute("tableinfo_v",tableinfo_v);
//		request.setAttribute("tableinfo_v1",tableinfo_v1);
//		request.setAttribute("tableinfo_v2",tableinfo_v2);
//		request.setAttribute("tableinfo_v3",tableinfo_v3);
//		request.setAttribute("tableinfo_v4",tableinfo_v4);
//		request.setAttribute("tableinfo_v5",tableinfo_v5);
//		request.setAttribute("tableinfo_v6",tableinfo_v6);
		request.setAttribute("maxconnections",maxconnections);
		request.setAttribute("buffersize",buffersize);
		request.setAttribute("Val",Val);
		request.setAttribute("basePath",basePath);
		request.setAttribute("dataPath",dataPath);
		request.setAttribute("logerrorPath",logerrorPath);
		request.setAttribute("version",version);
		request.setAttribute("hostOS",hostOS);
		
		return "/application/db/mysqlspace.jsp";
    }
	/**
	 * Status��Ϣ
	 * @return
	 */
	private String mysqlstatus()
    {    	   
		DBVo vo = new DBVo();	
		Vector sysValue = new Vector();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		Vector tableinfo_v = new Vector();
		String pingconavg ="0";
		String basePath=""; //����·��
		String dataPath=""; //����·��
		String logerrorPath=""; //����·��
		String version="";  //���ݿ�汾
		String hostOS="";   //����������ϵͳ
		int doneFlag = 0;
		String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
	Hashtable returnValue = new Hashtable();
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
//			Hashtable allData = ShareData.getMySqlmonitordata();
//			Hashtable ipData = ShareData.getMySqlmonitordata();
//			if(allData != null && allData.size()>0){
//				ipData = (Hashtable)allData.get(vo.getIpAddress());
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+vo.getId();
			DBDao dbDao = null;
			Hashtable ipData = null;
			try {
				dbDao = new DBDao();
				ipData = dbDao.getMysqlDataByServerip(serverip);
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				if(dbDao != null){
					dbDao.close();
				}
			}
			if(ipData != null && ipData.size()>0){
				runstr = (String)ipData.get("runningflag");
				String dbnames = vo.getDbName();
				String[] dbs = dbnames.split(",");
				for(int k=0;k<dbs.length;k++){
					//�ж��Ƿ��Ѿ���ȡ�˵�ǰ��������Ϣ
					if(doneFlag == 1)break;
					String dbStr = dbs[k];
					if(ipData.containsKey(dbStr)){
					
						returnValue = (Hashtable)ipData.get(dbStr);
						if(returnValue != null && returnValue.size()>0){
							if(doneFlag == 0){
								//�ж��Ƿ��Ѿ���ȡ�˵�ǰ��������Ϣ
								if(returnValue.containsKey("configVal")){
									Hashtable configVal = (Hashtable)returnValue.get("configVal");
									if(configVal.containsKey("basePath"))basePath=(String)configVal.get("basePath");
									if(configVal.containsKey("dataPath"))dataPath=(String)configVal.get("dataPath");
									if(configVal.containsKey("logerrorPath"))logerrorPath=(String)configVal.get("logerrorPath");
									if(configVal.containsKey("version"))version=(String)configVal.get("version");
									if(configVal.containsKey("hostOS"))hostOS=(String)configVal.get("hostOS");
									doneFlag = 1;
								}
							}
						}
					}
				}
				
			}
//			}
			
			request.setAttribute("runstr", runstr);
			try{
					//dao = new DBDao();
				try{
					//sysValue = dao.getVariables(vo.getIpAddress(), vo.getUser(), vo.getPassword(),vo.getDbName());
					sysValue=(Vector)returnValue.get("variables");
					//System.out.println(sysValue.size());
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					//dao.close();
				}
				String versionvalue = "";
				String vercommaval = "";
				String vercomosval = "";
				String verwaittimeval = "";
				String myverwaittimeval = "";
				if(sysValue != null)
				{
					for(int i=0;i<sysValue.size();i++)
					{
						Hashtable hs=(Hashtable)sysValue.get(i);
						if(hs.get("variable_name").equals("version"))
						{   
							versionvalue=(String)hs.get("value");
						}
						if(hs.get("variable_name").equals("version_compile_machine"))
						{   
							vercommaval=(String)hs.get("value");
						}
						if(hs.get("variable_name").equals("version_compile_os"))
						{
							vercomosval=(String)hs.get("value");
						}
						if(hs.get("variable_name").equals("wait_timeout"))
						{   
							myverwaittimeval=(String)hs.get("value");
						}
				
					}
				}
				request.setAttribute("versionvalue", versionvalue);
				request.setAttribute("vercommaval", vercommaval);
				request.setAttribute("vercomosval", vercomosval);
				request.setAttribute("verwaittimeval", verwaittimeval);
			}catch(Exception e){
				e.printStackTrace();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"MYPing","ConnectUtilization",starttime1,totime1);
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
			//dao = new DBDao();
			try{
				//tableinfo_v = dao.getStatus(vo.getIpAddress(), vo.getUser(), vo.getPassword(),vo.getDbName());
				tableinfo_v=(Vector)returnValue.get("global_status");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				//dao.close();
			}
			//sessioninfo_v = oraclemonManager.getOracleSession(serverip,serverport,servername,username,userpw);
			//rollbackinfo_v = oraclemonManager.getOracleRollbackinfo(serverip,serverport,servername,username,userpw);    
			
			  double avgpingcon = new Double(pingconavg+"").doubleValue();
			  
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  request.setAttribute("avgpingcon", avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130);   
		}catch(Exception e){
			e.printStackTrace();
		}
		/*if(tableinfo_v != null)
		{
			for(int i =0;i<tableinfo_v.size();i++)
			{
				Hashtable ht = (Hashtable)tableinfo_v.get(i);
				String tablespace = ht.get("variable_name").toString();
			}
		}*/
		request.setAttribute("imgurl",imgurlhash);
		request.setAttribute("max",maxhash);
		request.setAttribute("chart1",chart1);
		request.setAttribute("tableinfo_v",tableinfo_v);
		request.setAttribute("basePath",basePath);
		request.setAttribute("dataPath",dataPath);
		request.setAttribute("logerrorPath",logerrorPath);
		request.setAttribute("version",version);
		request.setAttribute("hostOS",hostOS);
		return "/application/db/mysqlstatus.jsp";
    }
	/**
	 * Variables��Ϣ
	 * @return
	 */
	private String mysqlvariables()
    {    	   
		DBVo vo = new DBVo();
		
		
		Vector sysValue = new Vector();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		Vector tableinfo_v = new Vector();
		String pingconavg ="0";
		String basePath=""; //����·��
		String dataPath=""; //����·��
		String logerrorPath=""; //����·��
		String version="";  //���ݿ�汾
		String hostOS="";   //����������ϵͳ
		int doneFlag = 0;
		String id = getParaValue("id");
		request.setAttribute("id", id);
		String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
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
//			Hashtable allData = ShareData.getMySqlmonitordata();
//			Hashtable ipData = ShareData.getMySqlmonitordata();
			Hashtable returnValue = new Hashtable();
//			if(allData != null && allData.size()>0){
//				ipData = (Hashtable)allData.get(vo.getIpAddress());
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+vo.getId();
			DBDao dbDao = null;
			Hashtable ipData = null;
			try {
				dbDao = new DBDao();
				ipData = dbDao.getMysqlDataByServerip(serverip);
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				if(dbDao != null){
					dbDao.close();
				}
			}
			if(ipData != null && ipData.size()>0){
				runstr = (String)ipData.get("runningflag");
				String dbnames = vo.getDbName();
				String[] dbs = dbnames.split(",");
				for(int k=0;k<dbs.length;k++){
					//�ж��Ƿ��Ѿ���ȡ�˵�ǰ��������Ϣ
					if(doneFlag == 1)break;
					String dbStr = dbs[k];
					if(ipData.containsKey(dbStr)){
					
						returnValue = (Hashtable)ipData.get(dbStr);
						if(returnValue != null && returnValue.size()>0){
							if(doneFlag == 0){
								//�ж��Ƿ��Ѿ���ȡ�˵�ǰ��������Ϣ
								if(returnValue.containsKey("configVal")){
									Hashtable configVal = (Hashtable)returnValue.get("configVal");
									if(configVal.containsKey("basePath"))basePath=(String)configVal.get("basePath");
									if(configVal.containsKey("dataPath"))dataPath=(String)configVal.get("dataPath");
									if(configVal.containsKey("logerrorPath"))logerrorPath=(String)configVal.get("logerrorPath");
									if(configVal.containsKey("version"))version=(String)configVal.get("version");
									if(configVal.containsKey("hostOS"))hostOS=(String)configVal.get("hostOS");
									doneFlag = 1;
								}
							}
						}
					}
				}
				
			}
//			}
			request.setAttribute("runstr", runstr);
			
			try{
			//	dao = new DBDao();
				try{
					//sysValue = dao.getVariables(vo.getIpAddress(), vo.getUser(), vo.getPassword(),vo.getDbName());
					sysValue=(Vector)returnValue.get("variables");
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					//dao.close();
				}
			
				if(sysValue != null)
				{
					for(int i=0;i<sysValue.size();i++)
					{
						Hashtable hs=(Hashtable)sysValue.get(i);
						if(hs.get("variable_name").equals("version"))
						{   
							String versionvalue=(String)hs.get("value");
							request.setAttribute("versionvalue", versionvalue);
						}
						if(hs.get("variable_name").equals("version_compile_machine"))
						{   
							String vercommaval=(String)hs.get("value");
							request.setAttribute("vercommaval", vercommaval);
						}
						if(hs.get("variable_name").equals("version_compile_os"))
						{
							String vercomosval=(String)hs.get("value");
							request.setAttribute("vercomosval", vercomosval);
						}
						if(hs.get("variable_name").equals("wait_timeout"))
						{   
							String verwaittimeval=(String)hs.get("value");
							request.setAttribute("verwaittimeval", verwaittimeval);
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpAddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"MYPing","ConnectUtilization",starttime1,totime1);
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
			//dao = new DBDao();
			try{
				//tableinfo_v = dao.getVariables(vo.getIpAddress(), vo.getUser(), vo.getPassword(),vo.getDbName());  
				tableinfo_v=(Vector)returnValue.get("variables");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				//dao.close();
			}
			
			  double avgpingcon = new Double(pingconavg+"").doubleValue();
			  
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  request.setAttribute("avgpingcon", avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130);   
		}catch(Exception e){
			e.printStackTrace();
		}
		/*for(int i =0;i<tableinfo_v.size();i++)
		{
			Hashtable ht = (Hashtable)tableinfo_v.get(i);
			String tablespace = ht.get("variable_name").toString();
		}*/
		request.setAttribute("imgurl",imgurlhash);
		request.setAttribute("max",maxhash);
		request.setAttribute("chart1",chart1);
		request.setAttribute("tableinfo_v",tableinfo_v);
		request.setAttribute("basePath",basePath);
		request.setAttribute("dataPath",dataPath);
		request.setAttribute("logerrorPath",logerrorPath);
		request.setAttribute("version",version);
		request.setAttribute("hostOS",hostOS);
		return "/application/db/mysqlvariables.jsp";
    }
	/**
	 * �澯��Ϣ
	 * @return
	 */
	private String mysqlevent()
    {    	   
		DBVo vo = new DBVo();
		
		String flag = getParaValue("flag");
		request.setAttribute("flag",flag);
		Vector sysValue = new Vector();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		Hashtable memValue = new Hashtable();
		String pingconavg ="0";
		String basePath=""; //����·��
		String dataPath=""; //����·��
		String logerrorPath=""; //����·��
		String version="";  //���ݿ�汾
		String hostOS="";   //����������ϵͳ
		int doneFlag = 0;
		String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
		String time = null;
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
			String runstr = "<font color=red>����ֹͣ</font>";
//			Hashtable allData = ShareData.getMySqlmonitordata();
//			Hashtable ipData = ShareData.getMySqlmonitordata();
			Hashtable returnValue = new Hashtable();
//			if(allData != null && allData.size()>0){
//				ipData = (Hashtable)allData.get(vo.getIpAddress());
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+vo.getId();
			DBDao dbDao = null;
			Hashtable ipData = null;
			try {
				dbDao = new DBDao();
				ipData = dbDao.getMysqlDataByServerip(serverip);
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				if(dbDao != null){
					dbDao.close();
				}
			}
			if(ipData != null && ipData.size()>0){
				runstr = (String)ipData.get("runningflag");
				String dbnames = vo.getDbName();
				String[] dbs = dbnames.split(",");
				for(int k=0;k<dbs.length;k++){
					//�ж��Ƿ��Ѿ���ȡ�˵�ǰ��������Ϣ
					if(doneFlag == 1)break;
					String dbStr = dbs[k];
					if(ipData.containsKey(dbStr)){
						
						returnValue = (Hashtable)ipData.get(dbStr);
						if(returnValue != null && returnValue.size()>0){
							if(doneFlag == 0){
								//�ж��Ƿ��Ѿ���ȡ�˵�ǰ��������Ϣ
								if(returnValue.containsKey("configVal")){
									Hashtable configVal = (Hashtable)returnValue.get("configVal");
									if(configVal.containsKey("basePath"))basePath=(String)configVal.get("basePath");
									if(configVal.containsKey("dataPath"))dataPath=(String)configVal.get("dataPath");
									if(configVal.containsKey("logerrorPath"))logerrorPath=(String)configVal.get("logerrorPath");
									if(configVal.containsKey("version"))version=(String)configVal.get("version");
									if(configVal.containsKey("hostOS"))hostOS=(String)configVal.get("hostOS");
									doneFlag = 1;
								}
							}
						}
					}
				}
				
			}
//			}
			request.setAttribute("runstr", runstr);
			
			try{
				//dao = new DBDao();
				try{
					//sysValue = dao.getVariables(vo.getIpAddress(), vo.getUser(), vo.getPassword(),vo.getDbName());
					sysValue=(Vector)returnValue.get("variables");
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					//dao.close();
				}
				for(int i=0;i<sysValue.size();i++)
				{
				Hashtable hs=(Hashtable)sysValue.get(i);
				if(hs.get("variable_name").equals("version"))
				{   
					String versionvalue=(String)hs.get("value");
					request.setAttribute("versionvalue", versionvalue);
				}
				if(hs.get("variable_name").equals("version_compile_machine"))
				{   
					String vercommaval=(String)hs.get("value");
					request.setAttribute("vercommaval", vercommaval);
				}
				if(hs.get("variable_name").equals("version_compile_os"))
				{
					String vercomosval=(String)hs.get("value");
					request.setAttribute("vercomosval", vercomosval);
				}
				if(hs.get("variable_name").equals("wait_timeout"))
				{   
					String verwaittimeval=(String)hs.get("value");
					request.setAttribute("verwaittimeval", verwaittimeval);
				}
			
				}
			}catch(Exception e){
				e.printStackTrace();
			}
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
				ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"MYPing","ConnectUtilization",starttime1,totime1);
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
			
			  double avgpingcon = new Double(pingconavg+"").doubleValue();
			  
			  DefaultPieDataset dpd = new DefaultPieDataset();
			  dpd.setValue("������",avgpingcon);
			  dpd.setValue("��������",100 - avgpingcon);
			  chart1 = ChartCreator.createPieChart(dpd,"",130,130); 
			  request.setAttribute("avgpingcon", avgpingcon);
		    	//tmp = request.getParameter("id");
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
				//String starttime1 = b_time + " 00:00:00";
				//String totime1 = t_time + " 23:59:59";
				try{
					User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);      //�û�����
					//SysLogger.info("user businessid===="+vo.getBusinessids());
					EventListDao eventdao = new EventListDao();
					try{
						list = eventdao.getQuery(starttime1,totime1,"db",status+"",level1+"",user.getBusinessids(),vo.getId());
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
		request.setAttribute("imgurl",imgurlhash);
		request.setAttribute("max",maxhash);
		request.setAttribute("chart1",chart1);
		request.setAttribute("oramem",memValue);
		request.setAttribute("list", list);
		request.setAttribute("startdate", b_time);
		request.setAttribute("todate", t_time);
		request.setAttribute("basePath",basePath);
		request.setAttribute("dataPath",dataPath);
		request.setAttribute("logerrorPath",logerrorPath);
		request.setAttribute("version",version);
		request.setAttribute("hostOS",hostOS);
		return "/application/db/mysqlevent.jsp";
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
		if(action.equals("mysqltables"))
            return mysqltables();
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
        if(action.equals("mysqlping"))
            return mysqlping();
        if(action.equals("mysqlsys"))
            return mysqlsys();
        if(action.equals("mysqlspace"))
            return mysqlspace();
        if(action.equals("mysqlstatus"))
            return mysqlstatus();
        if(action.equals("mysqlvariables"))
            return mysqlvariables();
        if(action.equals("mysqlevent"))
            return mysqlevent();
        if(action.equals("mysqlconnect"))
            return mysqlconnect();
        if(action.equals("sychronizeData"))
            return sychronizeData();
        if(action.equals("isDatabaseOK"))
        	return isDatabaseOK();
        //HONGLI ADD START
        if(action.equals("mysqlManagerPingReport"))
        	return mysqlManagerPingReport();
        if(action.equals("mysqlManagerPingReportQuery"))
        	return mysqlManagerPingReportQuery();
        if(action.equals("mysqlManagerNatureReport"))
        	return mysqlManagerNatureReport();
        if(action.equals("mysqlManagerNatureReportQuery"))
        	return mysqlManagerNatureReportQuery();
        if(action.equals("mysqlManagerCldReport"))
        	return mysqlManagerCldReport();
        if(action.equals("mysqlManagerCldReportQuery"))
        	return mysqlManagerCldReportQuery();
        if(action.equals("mysqlManagerEventReport"))
        	return mysqlManagerEventReport();
        if(action.equals("mysqlManagerEventReportQuery"))
        	return mysqlManagerEventReportQuery();
        //HONGLI ADD END
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
			DBDao dbDao = null;
			DBVo dbVo = null;
			try {
				dbDao = new DBDao();
				dbVo = (DBVo)dbDao.findByID(dbvoId);
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				if(dbDao != null){
					dbDao.close();
				}
			}
			dbRefreshHelper.execute(dbVo);
			if(dbPage.equals("mysqlping"))
			{
				return mysqlping();
			}
			if(dbPage.equals("mysqlconnect"))
			{
				return mysqlconnect();
			}
			if(dbPage.equals("mysqltable"))
			{
				return mysqltables();
			}
			if(dbPage.equals("mysqlspace"))
			{
				return mysqlspace();
			}
			if(dbPage.equals("mysqlstatus"))
			{
				return mysqlstatus();
			}
			if(dbPage.equals("mysqlvariables"))
			{
				return mysqlvariables();
			}
			if(dbPage.equals("mysqlevent"))
			{
				return mysqlevent();
			}
			return "/application/db/mysqlping.jsp";
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
			boolean oracleIsOK = false;
			try {
				//ddddddd
				dbdao = new DBDao();
				vo = (DBVo)dbdao.findByID(id);
				String dbname = vo.getDbName();
				oracleIsOK = dbdao.getMySqlIsOk(myip, myUser, myPassword, dbname);
				request.setAttribute("dbname", dbname);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (dbdao != null){
					dbdao.close();
				}
			}
			request.setAttribute("oracleIsOK", oracleIsOK);
			return "/tool/dbisok.jsp";
		}
		/**
		 * @author HONGLI
		 * date 2010-11-16
		 * �����Ա���ҳ
		 * @return
		 */
		public String mysqlManagerPingReport(){
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
			Vector Val = new Vector();
			int doneFlag = 0;
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
//				Hashtable allData = ShareData.getMySqlmonitordata();
//				Hashtable ipData = ShareData.getMySqlmonitordata();
//				if(allData != null && allData.size()>0){
//					ipData = (Hashtable)allData.get(vo.getIpAddress());
				IpTranslation tranfer = new IpTranslation();
				String hex = tranfer.formIpToHex(vo.getIpAddress());
				String serverip = hex+":"+vo.getId();
				Hashtable statusHashtable = null;
				DBDao dbDao = null;
				try {
					dbDao = new DBDao();
					Hashtable ipData = dbDao.getMysqlDataByServerip(serverip);
					statusHashtable = dbDao.getMysql_nmsstatus(serverip);
				} catch (Exception e) {
					e.printStackTrace();
				} finally{
					if(dbDao != null){
						dbDao.close();
					}
				}
				String runstr = "����ֹͣ";
				if(statusHashtable != null && statusHashtable.containsKey("status")){
					String status = (String)statusHashtable.get("status");
					if("1".equals(status)){
						runstr = "��������";
						pingnow = "100";
					}
				}
//				if(ipData != null && ipData.size()>0){
//					String dbnames = vo.getDbName();
//					String[] dbs = dbnames.split(",");
//					for(int k=0;k<dbs.length;k++){
//						//�ж��Ƿ��Ѿ���ȡ�˵�ǰ��������Ϣ
//						//if(doneFlag == 1)break;
//						String dbStr = dbs[k];
//						if(ipData.containsKey(dbStr)){
//							Hashtable returnValue = new Hashtable();
//							returnValue = (Hashtable)ipData.get(dbStr);
//							if(returnValue != null && returnValue.size()>0){
//								if(doneFlag == 0){
//									//�ж��Ƿ��Ѿ���ȡ�˵�ǰ��������Ϣ
//									if(returnValue.containsKey("configVal")){
//										doneFlag = 1;
//									}
//									if(returnValue.containsKey("Val")){
//										Val = (Vector)returnValue.get("Val");
//									}
//								}
//							}
//						}
//					}

//					String runstr = (String)ipData.get("runningflag");
//					if("��������".equals(runstr)){
//						pingnow = "100";
//					}
//				}
//				}
				String newip=SysUtil.doip(vo.getIpAddress());	
				request.setAttribute("newIp", newip);
				Hashtable ConnectUtilizationhash = new Hashtable();
				I_HostCollectData hostmanager=new HostCollectDataManager();
				try{
					ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"MYPing","ConnectUtilization",starttime,totime);
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
			request.setAttribute("Val", Val);
			request.setAttribute("avgpingcon", avgpingcon);
			request.setAttribute("pingmax", pingmax);
			request.setAttribute("pingmin", pingmin);
			request.setAttribute("pingnow",pingnow); 
			return "/capreport/db/showDbMySQLReport.jsp";
		}
		
		/**
		 * @author HONGLI 
		 * date 2010-11-16
		 * �����Ա��� ��ʱ���ѯ
		 * @return
		 */
		public String mysqlManagerPingReportQuery(){
			return mysqlManagerPingReport();
		}
		
		/**
		 *@author HONGLI 
		 * date 2010-11-16
		 * ���ܱ��� 
		 * @return
		 */
		public String mysqlManagerNatureReport(){
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
			Vector Val = new Vector();
			int doneFlag = 0;
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
//				Hashtable allData = ShareData.getMySqlmonitordata();
//				Hashtable ipData = ShareData.getMySqlmonitordata();
//				if(allData != null && allData.size()>0){
//					ipData = (Hashtable)allData.get(vo.getIpAddress());
				IpTranslation tranfer = new IpTranslation();
				String hex = tranfer.formIpToHex(vo.getIpAddress());
				String serverip = hex+":"+vo.getId();
				Hashtable ipData = null;
				DBDao dbDao = null;
				try {
					dbDao = new DBDao();
					ipData = dbDao.getMysqlDataByServerip(serverip);
				} catch (Exception e) {
					e.printStackTrace();
				} finally{
					if(dbDao != null){
						dbDao.close();
					}
				}
				if(ipData != null && ipData.size()>0){
//						runstr = (String)ipData.get("runningflag");
					String dbnames = vo.getDbName();
					String[] dbs = dbnames.split(",");
					for(int k=0;k<dbs.length;k++){
						//�ж��Ƿ��Ѿ���ȡ�˵�ǰ��������Ϣ
						//if(doneFlag == 1)break;
						String dbStr = dbs[k];
						if(ipData.containsKey(dbStr)){
							Hashtable returnValue = new Hashtable();
							returnValue = (Hashtable)ipData.get(dbStr);
							if(returnValue != null && returnValue.size()>0){
								if(doneFlag == 0){
									//�ж��Ƿ��Ѿ���ȡ�˵�ǰ��������Ϣ
									if(returnValue.containsKey("configVal")){
										doneFlag = 1;
									}
									if(returnValue.containsKey("Val")){
										Val = (Vector)returnValue.get("Val");
									}
								}
							}
						}
					}

					String runstr = (String)ipData.get("runningflag");
					if("��������".equals(runstr)){
						pingnow = "100";
					}
				}
//				}
				String newip=SysUtil.doip(vo.getIpAddress());	
				request.setAttribute("newIp", newip);
				Hashtable ConnectUtilizationhash = new Hashtable();
				I_HostCollectData hostmanager=new HostCollectDataManager();
				try{
					ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"MYPing","ConnectUtilization",starttime,totime);
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
			request.setAttribute("Val", Val);
			request.setAttribute("spaceInfo", spaceInfo);
			request.setAttribute("avgpingcon", avgpingcon+"");
			request.setAttribute("pingmax", pingmax);
			request.setAttribute("pingmin", pingmin);
			request.setAttribute("pingnow",pingnow); 
			return "/capreport/db/showDbMySQLNatureReport.jsp";
		}
		
		/**
		 *@author HONGLI 
		 * date 2010-11-16
		 * ���ܱ���  �����ڲ�ѯ
		 * @return
		 */
		public String mysqlManagerNatureReportQuery(){
			return mysqlManagerNatureReport();
		}
		
		/**
		 *@author HONGLI 
		 * date 2010-11-11
		 * �ۺϱ���
		 * @return
		 */
		public String mysqlManagerCldReport(){
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
			Vector Val = new Vector();
			int doneFlag = 0;
			List sessionlist = new ArrayList();
			Hashtable tablesHash = new Hashtable();
			Vector tableinfo_v = new Vector();
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
//				Hashtable allData = ShareData.getMySqlmonitordata();
//				Hashtable ipData = ShareData.getMySqlmonitordata();
//				if(allData != null && allData.size()>0){
//					ipData = (Hashtable)allData.get(vo.getIpAddress());
				IpTranslation tranfer = new IpTranslation();
				String hex = tranfer.formIpToHex(vo.getIpAddress());
				String serverip = hex+":"+vo.getId();
				Hashtable ipData = null;
				DBDao dbDao = null;
				try {
					dbDao = new DBDao();
					ipData = dbDao.getMysqlDataByServerip(serverip);
				} catch (Exception e) {
					e.printStackTrace();
				} finally{
					if(dbDao != null){
						dbDao.close();
					}
				}
				if(ipData != null && ipData.size()>0){
//						runstr = (String)ipData.get("runningflag");
					String dbnames = vo.getDbName();
					String[] dbs = dbnames.split(",");
					for(int k=0;k<dbs.length;k++){
						//�ж��Ƿ��Ѿ���ȡ�˵�ǰ��������Ϣ
						//if(doneFlag == 1)break;
						String dbStr = dbs[k];
						if(ipData.containsKey(dbStr)){
							Hashtable returnValue = new Hashtable();
							returnValue = (Hashtable)ipData.get(dbStr);
							if(returnValue != null && returnValue.size()>0){
								if(doneFlag == 0){
									//�ж��Ƿ��Ѿ���ȡ�˵�ǰ��������Ϣ
									if(returnValue.containsKey("configVal")){
										doneFlag = 1;
									}
									if(returnValue.containsKey("Val")){
										Val = (Vector)returnValue.get("Val");
									}
								}
								if(returnValue.containsKey("sessionsDetail")){
									//�������ݿ�������Ϣ
									sessionlist.add((List)returnValue.get("sessionsDetail"));
								}
								if(returnValue.containsKey("tablesDetail")){
									//�������ݿ����Ϣ
									tablesHash.put(dbStr,(List)returnValue.get("tablesDetail"));
								}
								if(returnValue.containsKey("tablesDetail")){
									//�������ݿ����Ϣ
									tableinfo_v=(Vector)returnValue.get("variables");
								}
							}
						}
					}

					runstr = (String)ipData.get("runningflag");
					if("��������".equals(runstr)){
						pingnow = "100";
					}
				}
//				}
				String newip=SysUtil.doip(vo.getIpAddress());	
				request.setAttribute("newIp", newip);
				Hashtable ConnectUtilizationhash = new Hashtable();
				I_HostCollectData hostmanager=new HostCollectDataManager();
				try{
					ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"MYPing","ConnectUtilization",starttime,totime);
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
			request.setAttribute("tableinfo_v",tableinfo_v);
			request.setAttribute("tablesHash",tablesHash);
			request.setAttribute("sessionlist",sessionlist);
			request.setAttribute("Val", Val);
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
			return "/capreport/db/showDbMySQLCldReport.jsp";
		}
		
		/**
		 *@author HONGLI 
		 * date 2010-11-16
		 * �ۺϱ���  �����ڲ�ѯ
		 * @return
		 */
		public String mysqlManagerCldReportQuery(){
			return mysqlManagerCldReport();
		}
		
		/**
		 *@author HONGLI 
		 * date 2010-11-16
		 * �¼�����   
		 * @return
		 */
		public String mysqlManagerEventReport(){
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
			List list = new ArrayList();
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
					ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"MYPing","ConnectUtilization",starttime,totime);
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
				//�¼��б�
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
						list = eventdao.getQuery(starttime,totime,"db",status+"",level1+"",user.getBusinessids(),vo.getId());
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
			request.setAttribute("list", list);
			request.setAttribute("downnum", downnum);
			request.setAttribute("vo", vo);
			request.setAttribute("typevo", typevo);
			return "/capreport/db/showDbMySQLEventReport.jsp";
		}
		
		/**
		 *@author HONGLI 
		 * date 2010-11-16
		 * �¼�����   �����ڲ�ѯ
		 * @return
		 */
		public String mysqlManagerEventReportQuery(){
			return mysqlManagerEventReport();
		}
}
