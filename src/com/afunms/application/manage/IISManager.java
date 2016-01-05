/**
 * <p>Description:Tomcat Manager</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-12-07
 */

package com.afunms.application.manage;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;

import com.afunms.application.dao.IISConfigDao;
import com.afunms.application.model.IISConfig;
import com.afunms.application.model.MQConfig;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.CEIString;
import com.afunms.common.util.ChartGraph;
import com.afunms.common.util.ChartXml;
import com.afunms.common.util.CreateTableManager;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.DateE;
import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.config.dao.SupperDao;
import com.afunms.config.model.Supper;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.api.I_HostCollectData;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.loader.IISLoader;
import com.afunms.system.model.TimeGratherConfig;
import com.afunms.system.model.User;
import com.afunms.system.util.TimeGratherConfigUtil;
import com.afunms.system.util.TimeShareConfigUtil;
import com.afunms.topology.dao.LineDao;
import com.afunms.topology.dao.ManageXmlDao;
import com.afunms.topology.dao.NodeDependDao;
import com.afunms.topology.model.ManageXml;
import com.afunms.topology.model.NodeDepend;
import com.afunms.topology.util.KeyGenerator;

public class IISManager extends BaseManager implements ManagerInterface
{
	DateE datemanager = new DateE();
	private String list()
	{
		List ips = new ArrayList();
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
		
		IISConfigDao configdao = new IISConfigDao();	
		List list = new ArrayList();
		try{
			if(operator.getRole() == 0){
				list = configdao.loadAll();
			}else{
				list = configdao.getIISByBID(rbids);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			configdao.close();
		}
		request.setAttribute("list",list);
		return "/application/iis/list.jsp";	
	}
	
	/**
	 * snow 增加前将供应商查找到
	 * @return
	 */
	private String ready_add(){
		SupperDao supperdao = new SupperDao();
    	List<Supper> allSupper = supperdao.loadAll();
    	request.setAttribute("allSupper", allSupper);
		return "/application/iis/add.jsp";
	}
	

	private String add()
    {    	  	
		
		IISConfig vo=new IISConfig();
		
		String name=getParaValue("name");
		String ipaddress=getParaValue("ipaddress");
		String community=getParaValue("community");
		int mon_flag=getParaIntValue("mon_flag");
		int netid=getParaIntValue("netid");
		String sendmobiles=getParaValue("sendmobiles");
		String sendemail=getParaValue("sendemail");
		
		vo.setId(KeyGenerator.getInstance().getNextKey());
		vo.setName(name);
		vo.setIpaddress(ipaddress);
		vo.setCommunity(community);
		vo.setMon_flag(mon_flag);
		//vo.setNetid(netid);
		vo.setSendmobiles(sendmobiles);
		vo.setSendemail(sendemail);
		vo.setSendphone(getParaValue("sendphone"));
		vo.setSupperid(getParaIntValue("supperid"));//snow add 2010-5-20
		
        vo.setNetid(getParaValue("bid"));
        IISConfigDao dao=new IISConfigDao();
        try{
        	dao.save(vo);
        	TimeShareConfigUtil timeShareConfigUtil = new TimeShareConfigUtil();  // nielin add for time-sharing at 2010-01-05
        	timeShareConfigUtil.saveTimeShareConfigList(request, String.valueOf(vo.getId()), timeShareConfigUtil.getObjectType("14"));
        	/* 增加采集时间设置 snow add at 2010-5-20 */
        	TimeGratherConfigUtil timeGratherConfigUtil = new TimeGratherConfigUtil();
        	timeGratherConfigUtil.saveTimeGratherConfigList(request, String.valueOf(vo.getId()), timeGratherConfigUtil.getObjectType("14"));
        	/* snow add end*/
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	dao.close();
        }
        //在轮询线程中增加被监视节点
        IISLoader loader = new IISLoader();
        try{
        	loader.loadOne(vo);
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	loader.close();
        }
        return "/iis.do?action=list";
    }    
	
	public String delete()
	{
		String[] ids = getParaArrayValue("checkbox");
		MQConfig vo = new MQConfig();
		List list = new ArrayList();
		
    	if(ids != null && ids.length > 0){
    		// nielin modify 2010-01-05
    		TimeShareConfigUtil timeShareConfigUtil = new TimeShareConfigUtil();  // nielin add for time-sharing at 2010-01-05
    		TimeGratherConfigUtil timeGratherConfigUtil = new TimeGratherConfigUtil(); // snow add at 2010-5-20 删除数据库采集时间
    		for(int i=0;i<ids.length;i++){
    			PollingEngine.getInstance().deleteIisByID(Integer.parseInt(ids[i]));
    			timeShareConfigUtil.deleteTimeShareConfig(ids[i], timeShareConfigUtil.getObjectType("14"));
    			timeGratherConfigUtil.deleteTimeGratherConfig(ids[i], timeGratherConfigUtil.getObjectType("14")); // snow add at 2010-5-20
    			//更新业务视图
    			String id = ids[i];
    			NodeDependDao nodedependao = new NodeDependDao();
    			List weslist = nodedependao.findByNode("iis"+id);
    			if(weslist!=null&&weslist.size()>0){
    				for(int j = 0; j < weslist.size(); j++){
    					NodeDepend wesvo = (NodeDepend)weslist.get(j);
    					if(wesvo!=null){
    						LineDao lineDao = new LineDao();
    		    			lineDao.deleteByidXml("iis"+id, wesvo.getXmlfile());
    		    			NodeDependDao nodeDependDao = new NodeDependDao();
    		    			if(nodeDependDao.isNodeExist("iis"+id, wesvo.getXmlfile())){
    		            		nodeDependDao.deleteByIdXml("iis"+id, wesvo.getXmlfile());
    		            	} else {
    		            		nodeDependDao.close();
    		            	}
    		    			
    		    			//yangjun
    		    			User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
    		    			ManageXmlDao mXmlDao =new ManageXmlDao();
    		    			List xmlList = new ArrayList();
    		    			try{
    		    				xmlList = mXmlDao.loadByPerAll(user.getBusinessids());
    		    			}catch(Exception e){
    		    				e.printStackTrace();
    		    			}finally{
    		    				mXmlDao.close();
    		    			}
    		    			try{
    		    				ChartXml chartxml;
    		    			    chartxml = new ChartXml("tree");
    		    			    chartxml.addViewTree(xmlList);
    		    		    }catch(Exception e){
    		    			    e.printStackTrace();   	
    		    		    }
    		                
    		                ManageXmlDao subMapDao = new ManageXmlDao();
    		    			ManageXml manageXml = (ManageXml) subMapDao.findByXml(wesvo.getXmlfile());
    		    			if(manageXml!=null){
    		    				NodeDependDao nodeDepenDao = new NodeDependDao();
    		    				try{
    		    				    List lists = nodeDepenDao.findByXml(wesvo.getXmlfile());
    		    				    ChartXml chartxml;
    		    					chartxml = new ChartXml("NetworkMonitor","/"+wesvo.getXmlfile().replace("jsp", "xml"));
    		    					chartxml.addBussinessXML(manageXml.getTopoName(),lists);
    		    					ChartXml chartxmlList;
    		    					chartxmlList = new ChartXml("NetworkMonitor","/"+wesvo.getXmlfile().replace("jsp", "xml").replace("businessmap", "list"));
    		    					chartxmlList.addListXML(manageXml.getTopoName(),lists);
    		    				}catch(Exception e){
    		    				    e.printStackTrace();   	
    		    				}finally{
    		    					nodeDepenDao.close();
    		                    }
    		    			}
    					}
    				}
    			}
    		}
			//删除IIS在临时表里中存储的数据
	        String[] nmsTempDataTables = {"nms_iis_temp"};
	        CreateTableManager createTableManager = new CreateTableManager();
	        createTableManager.clearNmsTempDatas(nmsTempDataTables, ids);
	        
    		IISConfigDao configdao = new IISConfigDao();
    		try{
    			configdao.delete(ids);
    		}catch(Exception e){
    			e.printStackTrace();
    		}finally{
    			configdao.close();
    		}
    	}
		return "/iis.do?action=list";
	}
	
	/**
	 * @author nielin add for time-sharing at 2010-01-05
	 * @return
	 */
	private String ready_edit(){
		String jsp = "/application/iis/edit.jsp";
		List timeShareConfigList = new ArrayList();
		IISConfigDao dao = new IISConfigDao();
		try{
			setTarget(jsp);
			jsp = readyEdit(dao);
			TimeShareConfigUtil timeShareConfigUtil = new TimeShareConfigUtil(); 
			timeShareConfigList = timeShareConfigUtil.getTimeShareConfigList(getParaValue("id"), timeShareConfigUtil.getObjectType("14"));
			
			/* 获得设备的采集时间 snow add at 2010-05-20 */
			//提供供应商信息
			SupperDao supperdao = new SupperDao();
	    	List<Supper> allSupper = supperdao.loadAll();
	    	request.setAttribute("allSupper", allSupper);
	    	//提供已设置的采集时间信息
	    	TimeGratherConfigUtil tg  = new TimeGratherConfigUtil();
	    	List<TimeGratherConfig> timeGratherConfigList = tg.getTimeGratherConfig(getParaValue("id"), tg.getObjectType("14"));
	    	for (TimeGratherConfig timeGratherConfig : timeGratherConfigList) {
	    		timeGratherConfig.setHourAndMin();
	    	}
	    	request.setAttribute("timeGratherConfigList", timeGratherConfigList);  
	    	/* snow end */
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dao.close();
		}
		request.setAttribute("timeShareConfigList", timeShareConfigList);
		return jsp;
	}
	
	private String update()
    {    	   
		IISConfig vo = new IISConfig();
		
		List list = new ArrayList();
		List conflist = new ArrayList();
		List ips = new ArrayList();
		int id=getParaIntValue("id");
		String name=getParaValue("name");
		String ipaddress=getParaValue("ipaddress");
		String community=getParaValue("community");
		int mon_flag=getParaIntValue("mon_flag");
		int netid=getParaIntValue("netid");
		String sendmobiles=getParaValue("sendmobiles");
		String sendemail=getParaValue("sendemail");
		
		vo.setId(id);
		vo.setName(name);
		vo.setIpaddress(ipaddress);
		vo.setCommunity(community);
		vo.setMon_flag(mon_flag);
		//vo.setNetid(netid);
		vo.setSendmobiles(sendmobiles);
		vo.setSendemail(sendemail);
		vo.setSendphone(getParaValue("sendphone"));
		vo.setSupperid(getParaIntValue("supperid"));//snow add 2010-5-20

        vo.setNetid(getParaValue("bid"));
        IISConfigDao configdao = new IISConfigDao();
        try{
        	configdao.update(vo);	
        	TimeShareConfigUtil timeShareConfigUtil = new TimeShareConfigUtil(); // nielin add for time-sharing at 2010-01-05
        	timeShareConfigUtil.saveTimeShareConfigList(request, String.valueOf(vo.getId()), timeShareConfigUtil.getObjectType("14"));
        	/* 增加采集时间设置 snow add at 2010-5-20 */
        	TimeGratherConfigUtil timeGratherConfigUtil = new TimeGratherConfigUtil();
        	timeGratherConfigUtil.saveTimeGratherConfigList(request, String.valueOf(vo.getId()), timeGratherConfigUtil.getObjectType("14"));
        	/* snow add end*/
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	configdao.close();
        }
        if(PollingEngine.getInstance().getIisByID(vo.getId())!=null)
        {        
           com.afunms.polling.node.IIS iis = (com.afunms.polling.node.IIS)PollingEngine.getInstance().getIisByID(vo.getId());
           iis.setId(vo.getId());
           iis.setAlias(vo.getName());
           iis.setName(vo.getName());
           iis.setIpAddress(vo.getIpaddress());
           iis.setCommunity(vo.getCommunity());
           iis.setSendemail(vo.getSendemail());
           iis.setSendmobiles(vo.getSendmobiles());
           iis.setSendphone(vo.getSendphone());
           iis.setBid(vo.getNetid());
           iis.setMon_flag(vo.getMon_flag());
        }
        return "/iis.do?action=list";
    }
	
	private String search()
    {    	   
//		DBVo vo = new DBVo();
//		DBDao dao = new DBDao();
//		SybspaceconfigDao configdao = new SybspaceconfigDao();
//		List list = new ArrayList();
//		List conflist = new ArrayList();
//		List ips = new ArrayList();
//		String ipaddress ="";
//
//		try{
//			ipaddress = getParaValue("ipaddress");
//			User operator = (User)session.getAttribute(SessionConstant.CURRENT_USER);
//			String bids = operator.getBusinessids();
//			String bid[] = bids.split(",");
//			Vector rbids = new Vector();
//			if(bid != null && bid.length>0){
//				for(int i=0;i<bid.length;i++){
//					if(bid[i] != null && bid[i].trim().length()>0)
//						rbids.add(bid[i].trim());
//				}
//			}
//
//			List oraList = new ArrayList();
//			DBTypeDao typedao = new DBTypeDao();
//			DBTypeVo typevo = (DBTypeVo)typedao.findByDbtype("sybase");
//			try{
//				oraList = dao.getDbByTypeAndBID(typevo.getId(),rbids);
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//			
//			if(oraList != null && oraList.size()>0){
//				for(int i=0;i<oraList.size();i++){
//					DBVo dbmonitorlist = (DBVo)oraList.get(i);
//					ips.add(dbmonitorlist.getIpAddress());
//				}
//			}
//			
//			
//			configdao = new SybspaceconfigDao();
//			//configdao.fromLastToOraspaceconfig();
//			
//			//ipaddress = (String)session.getAttribute("ipaddress");			
//			if (ipaddress != null && ipaddress.trim().length()>0){
//				configdao = new SybspaceconfigDao();
//				list =configdao.getByIp(ipaddress);
//				if (list == null || list.size() == 0){
//					list = configdao.loadAll();
//				}
//			}else{
//				configdao = new SybspaceconfigDao();
//				list = configdao.loadAll();		
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		//request.setAttribute("Oraspaceconfiglist", conflist);
//		request.setAttribute("iplist",ips);
//		request.setAttribute("ipaddress",ipaddress);
//		configdao = new SybspaceconfigDao();
//		list = configdao.getByIp(ipaddress);
//		request.setAttribute("list",list);
		return "/application/db/sybaseconfigsearchlist.jsp";
    }
	
	private String addalert()
    {    
		IISConfig vo = new IISConfig();
		IISConfigDao configdao = new IISConfigDao();
		List list = new ArrayList();
		List conflist = new ArrayList();
		List ips = new ArrayList();
		String ipaddress ="";
		try{
			vo = (IISConfig)configdao.findByID(getParaValue("id"));
			vo.setMon_flag(1);	
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			configdao.close();
		}
		configdao = new IISConfigDao();
		try{
			configdao.update(vo);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			configdao.close();
		}
		
		return "/iis.do?action=list";
    }
	
	private String cancelalert()
    {    
		IISConfig vo = new IISConfig();
		IISConfigDao configdao = new IISConfigDao();
		List list = new ArrayList();
		List conflist = new ArrayList();
		List ips = new ArrayList();
		String ipaddress ="";
		try{
			vo = (IISConfig)configdao.findByID(getParaValue("id"));
			vo.setMon_flag(0);	
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			configdao.close();
		}
		configdao = new IISConfigDao();
		try{
			configdao.update(vo);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			configdao.close();
		}
		return "/iis.do?action=list";
    }
	
	public String detail(){
		 
		 String id= request.getParameter("id");
		   com.afunms.polling.node.IIS iis = (com.afunms.polling.node.IIS)PollingEngine.getInstance().getIisByID(Integer.parseInt(id)); 
		   String ip=iis.getIpAddress();
		   	Hashtable imgurlhash=new Hashtable();
			try {
				String newip=doip(ip);
				java.text.SimpleDateFormat timeFormatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        String lasttime="";
		        String nexttime="";
		        String conn_name="";
		        String valid_name = "";
		        String fresh_name = "";
		        String wave_name = "";
		        String delay_name = "";
		        String connrate="0";
		        String validrate="0";
		        String freshrate="0";
		        Calendar now = Calendar.getInstance();
		        now.setTime(new Date());
		        Date nowdate = new Date();
		        nowdate.getHours();
		        String from_date1 = getParaValue("from_date1");
		        if (from_date1 == null){
		        	from_date1 = timeFormatter.format(new Date());
		        	request.setAttribute("from_date1", from_date1);
		        }
		        String to_date1 = getParaValue("to_date1");
		        if (to_date1 == null){
		        	to_date1 = timeFormatter.format(new Date());
		        	request.setAttribute("to_date1", to_date1);
		        }
		        String from_hour = getParaValue("from_hour");
		        if (from_hour == null){
		        	from_hour = "00";
		        	request.setAttribute("from_hour", from_hour);
		        }
		        String to_hour = getParaValue("to_hour");
		        if(to_hour == null){
		        	to_hour = nowdate.getHours()+"";            	            
		        	request.setAttribute("to_hour", to_hour);
		        }
		        String starttime = from_date1+" "+from_hour+":00:00";
		        String totime = to_date1+" "+to_hour+":59:59";   
				request.setAttribute("starttime1", from_date1);
				request.setAttribute("totime1", to_date1);										
				try{
					Hashtable hash1 = getCategory(ip,"IISPing","ConnectUtilization",starttime,totime);						
					p_draw_line(hash1,"连通率",newip+"IISPing",740,150);
					//Hashtable hash = getCategory(ip,"tomcat_jvm","jvm_utilization",starttime1,totime1);						
					//p_draw_line(hash,"JVM内存利用率",newip+"tomcat_jvm",740,150);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				//imgurlhash
				//imgurlhash.put("tomcat_jvm","resource\\image\\jfreechart\\"+newip+"tomca_jvm"+".png");
				imgurlhash.put("IISPing","resource/image/jfreechart/"+newip+"IISPing"+".png");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			request.setAttribute("imgurlhash",imgurlhash);
		   return  "/application/iis/iis_detail.jsp";
	   }
	
	public String execute(String action) 
	{	
        if(action.equals("list"))
            return list();    
        if(action.equals("ready_add"))
        	return ready_add();
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
        if(action.equals("detail"))
            return detail();
        if(action.equals("search"))
            return search();
		setErrorCode(ErrorMessage.ACTION_NO_FOUND);
		return null;
	}
	
public Hashtable getCollecttime(String ip) throws Exception{
    	
    	String collecttime = null;
    	String nexttime = null;
    	Hashtable pollingtime_ht= new Hashtable();
    	DBManager dbmanager = new DBManager();
    	ResultSet rs = null;
//    	String ip1 ="",ip2="",ip3="",ip4="";	
//		String tempStr = "";
//		String allipstr = "";
//		if (ip.indexOf(".")>0){
//			ip1=ip.substring(0,ip.indexOf("."));
//			ip4=ip.substring(ip.lastIndexOf(".")+1,ip.length());			
//			tempStr = ip.substring(ip.indexOf(".")+1,ip.lastIndexOf("."));
//		}
//		ip2=tempStr.substring(0,tempStr.indexOf("."));
//		ip3=tempStr.substring(tempStr.indexOf(".")+1,tempStr.length());
//		allipstr=ip1+ip2+ip3+ip4;
    	String allipstr = SysUtil.doip(ip);
		
		String sql = "";
		StringBuffer sb = new StringBuffer();
		sb.append(" select max(DATE_FORMAT(h.collecttime,'%Y-%m-%d %H:%i:%s')) as collecttime from iisping"+allipstr+" h  ");
		sql = sb.toString();
		SysLogger.info(sql);
		rs = dbmanager.executeQuery(sql);
		if(rs.next()){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			collecttime =rs.getString("collecttime");
			
			if(collecttime == null){
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(new Date().getTime());
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				collecttime = dateFormat.format(c.getTime()).toString();
			}
			Date date=format.parse(collecttime);
			int mins=date.getMinutes()+5;
			date.setMinutes(mins);
			nexttime=format.format(date);
			pollingtime_ht.put("lasttime", collecttime);
			pollingtime_ht.put("nexttime", nexttime);
		}	
		dbmanager.close();
		return  pollingtime_ht;
    }
	
    public double iisping(int id)
    {    	   
    	String strid = String.valueOf(id);
		IISConfig vo = new IISConfig();
		
		double avgpingcon=0;
		Hashtable sysValue = new Hashtable();
		Hashtable imgurlhash=new Hashtable();
		Hashtable maxhash = new Hashtable();
		String pingconavg ="0";
		try{
			IISConfigDao dao = new IISConfigDao();
			try{
				vo = (IISConfig)dao.findByID(strid);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dao.close();
			}

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time1 = sdf.format(new Date());
			String newip=SysUtil.doip(vo.getIpaddress());						
		
			String starttime1 = time1 + " 00:00:00";
			String totime1 = time1 + " 23:59:59";
			
			Hashtable ConnectUtilizationhash = new Hashtable();
			I_HostCollectData hostmanager=new HostCollectDataManager();
			try{
				ConnectUtilizationhash = getCategory(vo.getIpaddress(),"IISPing","ConnectUtilization",starttime1,totime1);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			if (ConnectUtilizationhash.get("avgpingcon")!=null)
				pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
			if(pingconavg != null){
				pingconavg = pingconavg.replace("%", "");
			}
			  avgpingcon = new Double(pingconavg+"").doubleValue();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//dao.close();
		}
		return avgpingcon;
    }
	private void drawPiechart(String[] keys,String[] values,String chname,String enname){
		ChartGraph cg = new ChartGraph();
		DefaultPieDataset piedata = new DefaultPieDataset();
		for(int i=0;i<keys.length;i++){
		  piedata.setValue(keys[i], new Double(values[i]).doubleValue());
		}
		cg.pie(chname,piedata,enname,300,120);    	
	}
	
	private void drawchart(Minute[] minutes,String keys,String[][] values,String chname,String enname){
		try{
		//int size = keys.length;
		TimeSeries[] s2 = new TimeSeries[1];
		String[] keymemory = new String[1];
		String[] unitMemory = new String[1];
		//for(int i = 0 ; i < size ; i++){
			String key = keys;
		//	System.out.println("in drawchart -------------- i="+i+" key="+key+" ");
			TimeSeries ss2 = new TimeSeries(key,Minute.class);
			String[] hmemory = values[0];
			arrayTochart(ss2,hmemory,minutes);
			keymemory[0] = key;
			s2[0] = ss2;
	//		}
		ChartGraph cg = new ChartGraph();
		cg.timewave(s2,"x","y(MB)",chname,enname,300,150);    	
		}catch(Exception e){
		System.out.println("drawchart error:" + e);
	}
	}
	
	private void arrayTochart(TimeSeries s,String[] h,Minute[] minutes){
		try{
		for(int j=0;j<h.length;j++){
			//System.out.println("h[i]: " + h[j]);
			String value=h[j];
			Double v=new Double(0);
			if(value!=null)	 {
				v=new Double(value);
			}
			s.addOrUpdate(minutes[j],v);
			}
		}catch(Exception e){
			System.out.println("arraytochart error:" + e);
		}
	}
	public String getF(String s){
		if(s.length()>5)
			s=s.substring(0,5);
		return s;
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
			//if (title1.equals("Cpu利用率")){
				Vector v = (Vector)list.get(j);
				//CPUcollectdata obj = (CPUcollectdata)list.get(j);
				Double	d=new Double((String)v.get(0));			
				String dt = (String)v.get(1);			
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date time1 = sdf.parse(dt);				
				Calendar temp = Calendar.getInstance();
				temp.setTime(time1);
				Minute minute=new Minute(temp.get(Calendar.MINUTE),temp.get(Calendar.HOUR_OF_DAY),temp.get(Calendar.DAY_OF_MONTH),temp.get(Calendar.MONTH)+1,temp.get(Calendar.YEAR));
				ss.addOrUpdate(minute,d);
			//}
		}
		cg.timewave(s,"x(时间)","y("+unit+")",title1,title2,w,h);
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
			cg.timewave(s,"x(时间)","y",title1,title2,w,h);
		}
		catch(Exception e){e.printStackTrace();}
	}
	 private void getTime(HttpServletRequest request,String[] time){		
		  Calendar current = new GregorianCalendar();
		  String key = getParaValue("beginhour");
		  if(getParaValue("beginhour") == null){
			  Integer hour = new Integer(current.get(Calendar.HOUR_OF_DAY));
			  request.setAttribute("beginhour", new Integer(hour.intValue()-1));
			  request.setAttribute("endhour", hour);
			  //mForm.setBeginhour(new Integer(hour.intValue()-1));
			  //mForm.setEndhour(hour);
		  }
		  if(getParaValue("begindate") == null){
			  current.set(Calendar.MINUTE,59);
			  current.set(Calendar.SECOND,59);
			  time[1] = datemanager.getDateDetail(current);
			  current.add(Calendar.HOUR_OF_DAY,-1);
			  current.set(Calendar.MINUTE,0);
			  current.set(Calendar.SECOND,0);
			  time[0] = datemanager.getDateDetail(current);

			  java.text.SimpleDateFormat timeFormatter = new java.text.SimpleDateFormat("yyyy-M-d");
			  String begindate = "";
			  begindate = timeFormatter.format(new java.util.Date());
			  request.setAttribute("begindate", begindate);
			  request.setAttribute("enddate", begindate);
			  //mForm.setBegindate(begindate);
			  //mForm.setEnddate(begindate);
		 }
		 else{
			  String temp = getParaValue("begindate");
			  time[0] = temp+" "+getParaValue("beginhour")+":00:00";
			  temp = getParaValue("enddate");
			  time[1] = temp+" "+getParaValue("endhour")+":59:59";
		 }
		  if(getParaValue("startdate") == null){
			  current.set(Calendar.MINUTE,59);
			  current.set(Calendar.SECOND,59);
			  time[1] = datemanager.getDateDetail(current);
			  current.add(Calendar.HOUR_OF_DAY,-1);
			  current.set(Calendar.MINUTE,0);
			  current.set(Calendar.SECOND,0);
			  time[0] = datemanager.getDateDetail(current);

			  java.text.SimpleDateFormat timeFormatter = new java.text.SimpleDateFormat("yyyy-M-d");
			  String startdate = "";
			  startdate = timeFormatter.format(new java.util.Date());
			  request.setAttribute("startdate", startdate);
			  request.setAttribute("todate", startdate);
			  //mForm.setStartdate(startdate);
			  //mForm.setTodate(startdate);
		 }
		 else{
			  String temp = getParaValue("startdate");
			  time[0] = temp+" "+getParaValue("beginhour")+":00:00";
			  temp = getParaValue("todate");
			  time[1] = temp+" "+getParaValue("endhour")+":59:59";
		 }
		  
	}
	 
	 private String doip(String ip){
//		  String newip="";
//		  for(int i=0;i<3;i++){
//			int p=ip.indexOf(".");
//			newip+=ip.substring(0,p);
//			ip=ip.substring(p+1);
//		  }
//		 newip+=ip;
		 ip =  SysUtil.doip(ip);
		 //System.out.println("newip="+newip);
		 return ip;
	}
	    public Hashtable getCategory(
				String ip,
				String category,
				String subentity,
				String starttime,
				String endtime)
				throws Exception {
				Hashtable hash = new Hashtable();
			 	//Connection con = null;
			 	//PreparedStatement stmt = null;
			 	DBManager dbmanager = new DBManager();
			 	ResultSet rs = null;
				try{
					//con=DataGate.getCon();
					if (!starttime.equals("") && !endtime.equals("")) {
						//con=DataGate.getCon();
//						String ip1 ="",ip2="",ip3="",ip4="";	
//						String tempStr = "";
//						String allipstr = "";
//						if (ip.indexOf(".")>0){
//							ip1=ip.substring(0,ip.indexOf("."));
//							ip4=ip.substring(ip.lastIndexOf(".")+1,ip.length());			
//							tempStr = ip.substring(ip.indexOf(".")+1,ip.lastIndexOf("."));
//						}
//						ip2=tempStr.substring(0,tempStr.indexOf("."));
//						ip3=tempStr.substring(tempStr.indexOf(".")+1,tempStr.length());
//						allipstr=ip1+ip2+ip3+ip4;
						String allipstr = SysUtil.doip(ip);
//						
						String sql = "";
						StringBuffer sb = new StringBuffer();
						 if (category.equals("IISPing")){
							sb.append(" select h.thevalue,DATE_FORMAT(h.collecttime,'%Y-%m-%d %H:%i:%s') as collecttime,h.unit from iisping"+allipstr+" h where ");
						 }
						sb.append(" h.category='");
						sb.append(category);
						sb.append("' and h.subentity='");
						sb.append(subentity);
						sb.append("' and h.collecttime >= '");
						sb.append(starttime);
						sb.append("' and h.collecttime <= '");
						sb.append(endtime);
						sb.append("' order by h.collecttime");
						sql = sb.toString();
						SysLogger.info(sql);
						
						rs = dbmanager.executeQuery(sql);
						List list1 =new ArrayList();
						String unit = "";
						String max = "";
						double tempfloat=0;
						double pingcon = 0;
						double tomcat_jvm_con = 0;
						int downnum = 0;
						int i=0;
				        while (rs.next()) {
				        	i=i+1;
				        	Vector v =new Vector();		        	
				            String thevalue=rs.getString("thevalue");
				            String collecttime = rs.getString("collecttime");		            
				            v.add(0,emitStr(thevalue));
				            v.add(1,collecttime);
				            v.add(2,rs.getString("unit"));
				            if (category.equals("IISPing")&&subentity.equalsIgnoreCase("ConnectUtilization")){
				            	pingcon=pingcon+getfloat(thevalue);
				            	if(thevalue.equals("0")){
				            		downnum = downnum + 1;
				            	}	
				            }
//				            if (category.equals("tomcat_jvm")&&subentity.equalsIgnoreCase("ConnectUtilization")){
//				            	pingcon=pingcon+getfloat(thevalue);
//				            	if(thevalue.equals("0")){
//				            		downnum = downnum + 1;
//				            	}	
//				            }
				            if (subentity.equalsIgnoreCase("ConnectUtilization")) {
				            	if (i==1)tempfloat = getfloat(thevalue);
				            	if (tempfloat > getfloat(thevalue))tempfloat = getfloat(thevalue);
				            }else{
				            	if (tempfloat < getfloat(thevalue))tempfloat = getfloat(thevalue);
				            }	
				            list1.add(v);	
				    }	
				        rs.close();
				        //stmt.close();
				        
						Integer size = new Integer(0);
						hash.put("list", list1);
						if (list1.size() != 0) {
							size = new Integer(list1.size());
							if (list1.get(0) != null) {
								Vector tempV = (Vector)list1.get(0);
								unit = (String)tempV.get(2);
							}
						}
						if (category.equals("IISPing")&&subentity.equalsIgnoreCase("ConnectUtilization")){
							if (list1 !=null && list1.size()>0){
								hash.put("avgpingcon", CEIString.round(pingcon/list1.size(),2)+unit);						
								hash.put("pingmax", tempfloat+"");
								hash.put("downnum", downnum+"");
							}else{ 
								hash.put("avgpingcon", "0.0%");	
								hash.put("pingmax", "0.0%");
								hash.put("downnum", "0");
							}
						}
						hash.put("size", size);			
						hash.put("max", CEIString.round(tempfloat,2) + unit);
						hash.put("unit", unit);
				        }
					} catch (Exception e) {
					e.printStackTrace();
				}finally{
					if (rs != null)
					rs.close();
					dbmanager.close();
				}
				
				return hash;
			}
	    
		private double getfloat(String num) {
			double snum = 0.0;
			if (num != null) {
				if (num.indexOf(".")>=0){				
					if (num.substring(num.indexOf(".")+1).length()>7){
						String tempStr = num.substring(num.indexOf(".")+1);
						num = num.substring(0,num.indexOf(".")+1)+tempStr.substring(0,7);					
					}
				}
				int inum = (int) (Float.parseFloat(num) * 100);
				snum = new Double(inum/100.0).doubleValue();
			}
			return snum;
		}
		private String emitStr(String num) {
			if (num != null) {
				if (num.indexOf(".")>=0){				
					if (num.substring(num.indexOf(".")+1).length()>7){
						String tempStr = num.substring(num.indexOf(".")+1);
						num = num.substring(0,num.indexOf(".")+1)+tempStr.substring(0,7);					
					}
				}
			}
			return num;
}
}