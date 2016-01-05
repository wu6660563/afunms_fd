/**
 * <p>Description:Tomcat Manager</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-12-07
 */

package com.afunms.application.manage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;

import com.afunms.application.dao.DBDao;
import com.afunms.application.dao.MQConfigDao;
import com.afunms.application.model.MQConfig;
import com.afunms.application.util.IpTranslation;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.ChartGraph;
import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.ShareData;
import com.afunms.config.dao.SupperDao;
import com.afunms.config.model.Supper;
import com.afunms.detail.service.mqInfo.MQInfoService;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.api.I_HostCollectData;
import com.afunms.polling.base.Node;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.loader.MqLoader;
import com.afunms.polling.node.MQ;
import com.afunms.system.model.TimeGratherConfig;
import com.afunms.system.model.User;
import com.afunms.system.util.TimeGratherConfigUtil;
import com.afunms.system.util.TimeShareConfigUtil;
import com.afunms.topology.util.KeyGenerator;

public class MQManager extends BaseManager implements ManagerInterface
{
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
		List list = null;
		MQConfigDao configdao = new MQConfigDao();
		try{
			if(operator.getRole()==0){
				list = configdao.loadAll();
			}else
				list = configdao.getMQByBID(rbids);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			configdao.close();
		}
		if(list == null)list = new ArrayList();
		for(int i=0;i<list.size();i++)
		{
			MQConfig vo = (MQConfig)list.get(i);
			Node mqNode = PollingEngine.getInstance().getMqByID(vo.getId());
			if(mqNode==null)
			   vo.setStatus(0);
			else
			   vo.setStatus(mqNode.getStatus());	
		}
		request.setAttribute("list",list);	
		return "/application/mq/list.jsp";
	}
	/**
	 * snow 增加前将供应商查找到
	 * @return
	 */
	private String ready_add(){
		SupperDao supperdao = new SupperDao();
    	List<Supper> allSupper = supperdao.loadAll();
    	request.setAttribute("allSupper", allSupper);
		return "/application/mq/add.jsp";
	}

	private String add()
    {    	   
		MQConfig vo = new MQConfig();
		
		vo.setId(KeyGenerator.getInstance().getNextKey());
		vo.setName(getParaValue("name"));
		vo.setIpaddress(getParaValue("ipaddress"));
		vo.setManagername(getParaValue("managername"));
		vo.setPortnum(getParaIntValue("portnum"));
		vo.setSendmobiles(getParaValue("sendmobiles"));
		vo.setMon_flag(getParaIntValue("mon_flag"));
		vo.setSendemail(getParaValue("sendemail"));
		vo.setSendphone(getParaValue("sendphone"));
		vo.setSupperid(getParaIntValue("supperid"));

        
        vo.setNetid(getParaValue("bid"));
        
        MQConfigDao dao = new MQConfigDao();
        try{
        	dao.save(vo);
        	TimeShareConfigUtil timeShareConfigUtil = new TimeShareConfigUtil(); // nielin add for time-sharing 2010-01-04
        	timeShareConfigUtil.saveTimeShareConfigList(request, String.valueOf(vo.getId()), timeShareConfigUtil.getObjectType("9"));
            /* 增加采集时间设置 snow add at 2010-5-20 */
            TimeGratherConfigUtil timeGratherConfigUtil = new TimeGratherConfigUtil();
            timeGratherConfigUtil.saveTimeGratherConfigList(request, String.valueOf(vo.getId()), timeGratherConfigUtil.getObjectType("9"));
      		/* snow add end*/
        
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	dao.close();
        }
        //在轮询线程中增加被监视节点
        MqLoader loader = new MqLoader();
        try{
        	loader.loadOne(vo);
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	loader.close();
        }
        return "/mq.do?action=list&jp=1";
    }    
	
	public String delete()
	{
		String[] ids = getParaArrayValue("checkbox");
		MQConfig vo = new MQConfig();
		List list = new ArrayList();
		MQConfigDao configdao = new MQConfigDao();
		try{
			if(ids != null && ids.length > 0){	
				TimeGratherConfigUtil timeGratherConfigUtil = new TimeGratherConfigUtil(); // snow add at 2010-5-20 删除数据库采集时间
	    		TimeShareConfigUtil timeShareConfigUtil = new TimeShareConfigUtil();   // nielin add for time-sharing at 2010-01-04
	    		for(int i=0;i<ids.length;i++){
	    			timeGratherConfigUtil.deleteTimeGratherConfig(ids[i], timeGratherConfigUtil.getObjectType("9")); // snow add at 2010-5-20 
	    			timeShareConfigUtil.deleteTimeShareConfig(ids[i], timeShareConfigUtil.getObjectType("9"));
	    			PollingEngine.getInstance().deleteMqByID(Integer.parseInt(ids[i]));
	    		}
	    		configdao.delete(ids);
	    		
	    		//删除MQ临时表信息
	    		//根据nodeid和数据库id删除存储采集数据表的数据
				DBDao dbDao = new DBDao();
				String[] tableNames = {"nms_mq_temp"};
				try {
					dbDao.clearTablesDataByNodeIds(tableNames, ids);
				} catch (RuntimeException e) {
					e.printStackTrace();
				}finally{
					dbDao.close();
				}
	    	}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			configdao.close();
		}
		return "/mq.do?action=list";
		
	}
	
	/**
	 * @author nielin add for sms
	 * @since 2009-12-31
	 * @return
	 */
	private String ready_edit(){
		String jsp = "/application/mq/edit.jsp";
		List  timeShareConfigList = new ArrayList();
		MQConfigDao dao = new MQConfigDao();
		try{
			setTarget(jsp);
			jsp = readyEdit(dao);
			TimeShareConfigUtil timeShareConfigUtil = new TimeShareConfigUtil();
			timeShareConfigList = timeShareConfigUtil.getTimeShareConfigList(getParaValue("id"), timeShareConfigUtil.getObjectType("9"));
		    /* 获得设备的采集时间 snow add at 2010-05-18 */
			//提供供应商信息
			SupperDao supperdao = new SupperDao();
	    	List<Supper> allSupper = supperdao.loadAll();
	    	request.setAttribute("allSupper", allSupper);
	    	//提供已设置的采集时间信息
	    	TimeGratherConfigUtil tg  = new TimeGratherConfigUtil();
	    	List<TimeGratherConfig> timeGratherConfigList = tg.getTimeGratherConfig(getParaValue("id"), tg.getObjectType("9"));
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
		MQConfig vo = new MQConfig();
		
		List list = new ArrayList();
		List conflist = new ArrayList();
		List ips = new ArrayList();
		String ipaddress ="";		
		
    	vo.setId(getParaIntValue("id"));
		vo.setName(getParaValue("name"));
		vo.setIpaddress(getParaValue("ipaddress"));
		vo.setManagername(getParaValue("managername"));
		vo.setPortnum(getParaIntValue("portnum"));
		vo.setSendmobiles(getParaValue("sendmobiles"));
		vo.setMon_flag(getParaIntValue("mon_flag"));
		//vo.setNetid(rs.getString("netid"));
		vo.setSendemail(getParaValue("sendemail"));
		vo.setSendphone(getParaValue("sendphone"));
		vo.setSupperid(getParaIntValue("supperid"));

        
        vo.setNetid(getParaValue("bid"));
        MQConfigDao configdao = new MQConfigDao();
        try{
        	configdao.update(vo);	
        	TimeShareConfigUtil timeShareConfigUtil = new TimeShareConfigUtil();
        	timeShareConfigUtil.saveTimeShareConfigList(request, String.valueOf(vo.getId()), timeShareConfigUtil.getObjectType("9"));
            /* 增加采集时间设置 snow add at 2010-5-20 */
            TimeGratherConfigUtil timeGratherConfigUtil = new TimeGratherConfigUtil();
            timeGratherConfigUtil.saveTimeGratherConfigList(request, String.valueOf(vo.getId()), timeGratherConfigUtil.getObjectType("9"));
      		/* snow add end*/
        
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	configdao.close();
        }
        if(PollingEngine.getInstance().getMqByID(vo.getId())!=null)
        {        
           com.afunms.polling.node.MQ mq = (com.afunms.polling.node.MQ)PollingEngine.getInstance().getMqByID(vo.getId());
           mq.setName(vo.getName());
       		mq.setIpaddress(vo.getIpaddress());
       		mq.setManagername(vo.getManagername());
       		mq.setPortnum(vo.getPortnum());
       		mq.setSendemail(vo.getSendemail());
       		mq.setSendmobiles(vo.getSendmobiles());
       		mq.setSendphone(vo.getSendphone());
       		mq.setBid(vo.getNetid());
       		mq.setMon_flag(vo.getMon_flag());
        }
        return "/mq.do?action=list";
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
		MQConfig vo = new MQConfig();
		MQConfigDao configdao = new MQConfigDao();
		List list = new ArrayList();
		List conflist = new ArrayList();
		List ips = new ArrayList();
		String ipaddress ="";
		try{
			vo = (MQConfig)configdao.findByID(getParaValue("id"));
			vo.setMon_flag(1);
			MQ mq = (MQ)PollingEngine.getInstance().getMqByID(vo.getId());
			mq.setMon_flag(1);	
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			configdao.close();
		}
		configdao = new MQConfigDao();
		try{
			configdao.update(vo);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			configdao.close();
		}
		return "/mq.do?action=list";
    }
	
	private String cancelalert()
    {    
		MQConfig vo = new MQConfig();
		MQConfigDao configdao = new MQConfigDao();
		List list = new ArrayList();
		List conflist = new ArrayList();
		List ips = new ArrayList();
		String ipaddress ="";
		try{
			vo = (MQConfig)configdao.findByID(getParaValue("id"));
			vo.setMon_flag(0);
			MQ mq = (MQ)PollingEngine.getInstance().getMqByID(vo.getId());
			mq.setMon_flag(0);
	
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			configdao.close();
		}
		configdao = new MQConfigDao();
		try{
			configdao.update(vo);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			configdao.close();
		}
		return "/mq.do?action=list";
    }
	
	private String detail()
    {    
        List remotelist = new ArrayList();
        List locallist = new ArrayList();
        Vector mqValue = new Vector();
        Hashtable rValue = new Hashtable();
        MQConfig vo = new MQConfig();
        String runmodel = PollingEngine.getCollectwebflag(); 
		MQConfigDao configdao = new MQConfigDao();
		
        try{
        	vo = (MQConfig)configdao.findByID(getParaValue("id"));
        	String ip = vo.getIpaddress();
        	if("0".equals(runmodel)){
               	//采集与访问是集成模式	
	        	Hashtable allMqValues = ShareData.getMqdata();
	        	if (allMqValues != null && allMqValues.size()>0){
					rValue = (Hashtable)allMqValues.get(ip+":"+vo.getManagername());
	        	}
        	}else{
        	 	//采集与访问是分离模式
        		MQInfoService mqInfoService = new MQInfoService();
        		rValue = mqInfoService.getMQDataHashtable(vo.getId()+"");
        	}
        	double mqAvgPing = getMqAvgPing(vo);
        	request.setAttribute("mqAvgPing", mqAvgPing);
            request.setAttribute("vo", vo);
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	configdao.close();
        }
		mqValue = (Vector)rValue.get("mqValue");
		remotelist = (List)rValue.get("remote");
		locallist = (List)rValue.get("local");
		if(mqValue == null)mqValue = new Vector();
		if(remotelist == null)remotelist = new ArrayList();
		if(locallist == null)locallist = new ArrayList();
        request.setAttribute("mqValue", mqValue);
        request.setAttribute("remote", remotelist);
        request.setAttribute("local", locallist);
		return "/application/mq/detail.jsp";
    }
	
	/**
	 * 得到mq的平均利用率
	 * @param vo
	 * @return
	 */
	private double getMqAvgPing(MQConfig vo) {
		double avgpingcon=0;
		if(vo == null){
			return avgpingcon;
		}
		String pingconavg ="0";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String time1 = sdf.format(new Date());
		String starttime1 = time1 + " 00:00:00";
		String totime1 = time1 + " 23:59:59";
		
		Hashtable ConnectUtilizationhash = new Hashtable();
		I_HostCollectData hostmanager=new HostCollectDataManager();
		try{
			ConnectUtilizationhash = hostmanager.getCategory(vo.getIpaddress(),"MqPing","ConnectUtilization",starttime1,totime1);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		if (ConnectUtilizationhash.get("avgpingcon")!=null)
			pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
		if(pingconavg != null){
			pingconavg = pingconavg.replace("%", "");
		}
		  avgpingcon = new Double(pingconavg+"").doubleValue();
		return avgpingcon;
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
	
}