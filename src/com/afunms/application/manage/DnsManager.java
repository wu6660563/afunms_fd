package com.afunms.application.manage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.commons.beanutils.BeanUtils;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;

import com.afunms.application.dao.DBDao;
import com.afunms.application.dao.DnsConfigDao;
import com.afunms.application.dao.Dnsmonitor_historyDao;
import com.afunms.application.dao.Dnsmonitor_realtimeDao;
import com.afunms.application.model.DBVo;
import com.afunms.application.model.DnsConfig;
import com.afunms.application.model.Dnsmonitor_realtime;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.ChartGraph;
import com.afunms.common.util.CreateTableManager;
import com.afunms.common.util.DateE;
import com.afunms.common.util.InitCoordinate;
import com.afunms.common.util.SessionConstant;
import com.afunms.config.dao.SupperDao;
import com.afunms.config.model.Supper;
import com.afunms.polling.om.Task;
import com.afunms.polling.task.TaskXml;
import com.afunms.system.model.TimeGratherConfig;
import com.afunms.system.model.User;
import com.afunms.system.util.TimeGratherConfigUtil;
import com.afunms.topology.util.KeyGenerator;



public class DnsManager extends BaseManager implements ManagerInterface{
	/**
	 * 查询dns信息
	 * @return
	 */
	private String list()
	{    
		int status=0;
		List ips = new ArrayList();
		User operator = (User)session.getAttribute(SessionConstant.CURRENT_USER);
		String bids = operator.getBusinessids();
		if(bids == null)bids = "";
		String bid[] = bids.split(",");
		Vector rbids = new Vector();
		if(bid != null && bid.length>0){
			for(int i=0;i<bid.length;i++){
				if(bid[i] != null && bid[i].trim().length()>0)
					rbids.add(bid[i].trim());
			}
		}
		List list = null;
		DnsConfigDao configdao = new DnsConfigDao();
	   try
	   {	
		   if(operator.getRole() == 0){
			   list = configdao.loadAll();
		   }else{
			   list = configdao.getDnsByBID(rbids);
		   }
		
		
	   }catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	       finally
	       {
	    	   configdao.close();
	       }
	       
	       request.setAttribute("list",list);
	      // System.out.println(status);
    	   request.setAttribute("status", status);
		setTarget("/application/dns/dnsconfiglist.jsp");
		return "/application/dns/dnsconfiglist.jsp";
		//return list(configdao);
	}
	
	/**
	 * snow 增加前将供应商查找到
	 * @return
	 */
	private String ready_add(){
		SupperDao supperdao = new SupperDao();
    	List<Supper> allSupper = supperdao.loadAll();
    	request.setAttribute("allSupper", allSupper);
		return "/application/dns/add.jsp";
	}
	
	/**
	    * 添加一条dns信息
	    * @return
	    */
		private String add()
	    {    	   
			DnsConfig vo = new DnsConfig();
			vo.setId(KeyGenerator.getInstance().getNextKey());
			vo.setUsername(getParaValue("username"));
	    	vo.setPassword(getParaValue("password"));
	    	vo.setHostip(getParaValue("hostip"));
	    	vo.setHostinter(getParaIntValue("hostinter"));
	    	vo.setDns(getParaValue("dns"));
	    	vo.setDnsip(getParaValue("dnsip"));
			vo.setFlag(getParaIntValue("_flag"));
			vo.setSendmobiles(getParaValue("sendmobiles"));
			vo.setSendemail(getParaValue("sendemail"));
			vo.setSendemail(getParaValue("sendphone"));
			vo.setSupperid(getParaIntValue("supperid"));//snow add 2010-5-20
			
			
	        vo.setNetid(getParaValue("bid"));       
	        DnsConfigDao dao = new DnsConfigDao();
	        try{
	        	dao.save(vo);	
	        	/* 增加采集时间设置 snow add at 2010-5-20 */
	        	TimeGratherConfigUtil timeGratherConfigUtil = new TimeGratherConfigUtil();
	        	timeGratherConfigUtil.saveTimeGratherConfigList(request, String.valueOf(vo.getId()), timeGratherConfigUtil.getObjectType("17"));
	        	/* snow add end*/
	        }catch(Exception e){
	        	e.printStackTrace();
	        }finally{
	        	dao.close();
	        }
	        
	        return "/dns.do?action=list&jp=1";
	    }    
		/**
		 * 删除一条dns信息记录
		 * @return
		 */
		public String delete()
		{
			String[] ids = getParaArrayValue("checkbox");
			DnsConfig vo = new DnsConfig();
			List list = new ArrayList();
			DnsConfigDao configdao = null;
	    	if(ids != null && ids.length > 0){	
	    		configdao = new DnsConfigDao();
	    		try{
	    			configdao.delete(ids);
	    		}catch(Exception e){
	    			e.printStackTrace();
	    		}finally{
	    			configdao.close();
	    		}
				/* snow add 2010-5-20 */
				TimeGratherConfigUtil tg = new TimeGratherConfigUtil();
				for (String str : ids) {
					tg.deleteTimeGratherConfig(str, tg.getObjectType("17"));
				}
				/* snow end*/
	    		
	    	}
	    	//删除DNS在临时表里中存储的数据
	        String[] nmsTempDataTables = {"nms_dns_temp"};
	        CreateTableManager createTableManager = new CreateTableManager();
	        createTableManager.clearNmsTempDatas(nmsTempDataTables, ids);
	    	
			try{
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
				configdao = new DnsConfigDao();
				try{
					list = configdao.getDnsByBID(rbids);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					configdao.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			request.setAttribute("list",list);
			return list();
		}
		/**
		 * snow
		 * 修改cics前获得其数据库中的关联数据，采集时间
		 * @return url
		 */
		private String ready_edit(){
			 DaoInterface dao = new DnsConfigDao();
	 	    setTarget("/application/dns/edit.jsp");
			String jsp = "";
	    	try {
	    		jsp = readyEdit(dao);
				//提供供应商信息
				SupperDao supperdao = new SupperDao();
		    	List<Supper> allSupper = supperdao.loadAll();
		    	request.setAttribute("allSupper", allSupper);
		    	//采集时间信息
				TimeGratherConfigUtil tg  = new TimeGratherConfigUtil();
				List<TimeGratherConfig> timeGratherConfigList = tg.getTimeGratherConfig(getParaValue("id"), tg.getObjectType("17"));
				for (TimeGratherConfig timeGratherConfig : timeGratherConfigList) {
					timeGratherConfig.setHourAndMin();
				}
				request.setAttribute("timeGratherConfigList", timeGratherConfigList);
			} catch (Exception e) {
				e.printStackTrace();
			}  
	    	return jsp;
	    }
		/**
		 * 修改DNS信息
		 * @return
		 */
		private String update()
	    {    	   
			DnsConfig vo = new DnsConfig();
			List list = new ArrayList();
			List conflist = new ArrayList();
			List ips = new ArrayList();
			String ipaddress ="";		
	    	vo.setId(getParaIntValue("id"));
	    	vo.setUsername(getParaValue("username"));
	    	vo.setPassword(getParaValue("password"));
	    	vo.setHostip(getParaValue("hostip"));
	    	vo.setHostinter(getParaIntValue("hostinter"));
	    	vo.setDns(getParaValue("dns"));
	    	vo.setDnsip(getParaValue("dnsip"));
			vo.setFlag(getParaIntValue("_flag"));
			vo.setSendmobiles(getParaValue("sendmobiles"));
			vo.setSendemail(getParaValue("sendemail"));
			vo.setSendemail(getParaValue("sendphone"));
			vo.setNetid(getParaValue("netid"));
			vo.setSupperid(getParaIntValue("supperid"));//snow add 2010-5-20
			
			
	        vo.setNetid(getParaValue("bid"));
	        DnsConfigDao configdao = new DnsConfigDao();
	        try{
	        	try{
	        		configdao.update(vo);	
	        		/* 增加采集时间设置 snow add at 2010-5-20 */
	        		TimeGratherConfigUtil timeGratherConfigUtil = new TimeGratherConfigUtil();
	        		timeGratherConfigUtil.saveTimeGratherConfigList(request, String.valueOf(vo.getId()), timeGratherConfigUtil.getObjectType("17"));
	        		/* snow add end*/
	        	}catch(Exception e){
	        		e.printStackTrace();
	        	}finally{
	        		configdao.close();
	        	}
				User operator = (User)session.getAttribute(SessionConstant.CURRENT_USER);
				String bids = operator.getBusinessids();
				if(bids == null)bids = "";
				String bid[] = bids.split(",");
				Vector rbids = new Vector();
				if(bid != null && bid.length>0){
					for(int i=0;i<bid.length;i++){
						if(bid[i] != null && bid[i].trim().length()>0)
							rbids.add(bid[i].trim());
					}
				}
				configdao = new DnsConfigDao();
				try{
					list = configdao.getDnsByBID(rbids);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					configdao.close();
				}
				
	        }catch(Exception e){
	        	e.printStackTrace();
	        }
			request.setAttribute("list",list);
	             
			return list();
	    }
	
	/**
	 * 添加DNS监视信息
	 * @return
	 */
	private String addalert()
    {    
		DnsConfig vo = new DnsConfig();
		DnsConfigDao configdao = new DnsConfigDao();
		
		List list = new ArrayList();

		try{
			vo = (DnsConfig)configdao.findByID(getParaValue("id"));
			vo.setFlag(1);
			configdao = new DnsConfigDao();
			configdao.update(vo);	
			User operator = (User)session.getAttribute(SessionConstant.CURRENT_USER);
			String bids = operator.getBusinessids();
			if(bids == null)bids="";
			String bid[] = bids.split(",");
			Vector rbids = new Vector();
			if(bid != null && bid.length>0){
				for(int i=0;i<bid.length;i++){
					if(bid[i] != null && bid[i].trim().length()>0)
						rbids.add(bid[i].trim());
				}
			}
			configdao = new DnsConfigDao();
			
			if(operator.getRole() == 0){
				list = configdao.loadAll();
			}else{
				list = configdao.getDnsByBID(rbids);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally
		{
			configdao.close();
		}
		request.setAttribute("list",list);
		return "/application/dns/dnsconfigsearchlist.jsp";
    }
	/**
	 * 取消DNS监视信息
	 * @return
	 */
	private String cancelalert()
    {    
		DnsConfig vo = new DnsConfig();
		DnsConfigDao configdao = new DnsConfigDao();
		DBVo dbvo = new DBVo();
		DBDao dao = new DBDao();
		List list = new ArrayList();
		List conflist = new ArrayList();
		List ips = new ArrayList();
		String ipaddress ="";
		try{
			vo = (DnsConfig)configdao.findByID(getParaValue("id"));
			vo.setFlag(0);
			configdao = new DnsConfigDao();
			configdao.update(vo);	
			User operator = (User)session.getAttribute(SessionConstant.CURRENT_USER);
			String bids = operator.getBusinessids();
			if(bids == null)bids="";
			String bid[] = bids.split(",");
			Vector rbids = new Vector();
			if(bid != null && bid.length>0){
				for(int i=0;i<bid.length;i++){
					if(bid[i] != null && bid[i].trim().length()>0)
						rbids.add(bid[i].trim());
				}
			}
			configdao = new DnsConfigDao();
			list = configdao.getDnsByBID(rbids);
		}catch(Exception e){
			e.printStackTrace();
		}
		finally
		{
			dao.close();
			configdao.close();
		}
		request.setAttribute("list",list);
		return "/application/dns/dnsconfigsearchlist.jsp";
    }
	
	/**
	 * 
	 * 监控信息
	 * @return
	 */

	private String detail()
    {    
		Integer id = getParaIntValue("id");
		System.out.println(id);
	    DnsConfig dc = null;
		DnsConfigDao dao = new DnsConfigDao();
		List arr = dao.getDnsById(id);
		for(int i=0;i<arr.size();i++)
		{
			  dc=(DnsConfig)arr.get(i);
		}
		String dnsip = "";
		if(dc != null){
			dnsip = dc.getDnsip();
		}
		request.setAttribute("id", id);
		request.setAttribute("dnsip", dnsip);
		java.text.SimpleDateFormat timeFormatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DnsConfigDao configdao = new DnsConfigDao();
		Dnsmonitor_realtimeDao realtimedao = new Dnsmonitor_realtimeDao();
		Dnsmonitor_historyDao historydao = new Dnsmonitor_historyDao();
		List urllist = new ArrayList();      //用做条件选择列表
		DnsConfig initconf = new DnsConfig();    //当前的对象
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
        int flag = 0;
        try {
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
			//configdao = new WebConfigDao();
			try{
				urllist = configdao.getDnsByBID(rbids);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				configdao.close();
			}
			
        	Integer queryid = getParaIntValue("id");//.getUrl_id();

        	if(urllist.size()>0&&queryid==null){
        		Object obj = urllist.get(0);           		
        	}
        	if(queryid!=null){					
        		//如果是链接过来则取用查询条件
        		configdao = new DnsConfigDao();
        		try{
        			initconf = (DnsConfig)configdao.findByID(queryid+"");
        		}catch(Exception e){
        			e.printStackTrace();
        		}finally{
        			configdao.close();
        		}
        	}
        	queryid = initconf.getId();
        	 conn_name=queryid+"urlmonitor-conn";
             valid_name =queryid+"urlmonitor-valid";
             fresh_name =queryid+"urlmonitor-refresh";
             wave_name = queryid+"urlmonitor-rec";
             delay_name = queryid+"urlmonitor-delay";
        	
             List urlList = null;
             try{
            	 urlList = realtimedao.getByDNSId(queryid);
             }catch(Exception e){
            	 e.printStackTrace();
             }finally{
            	 realtimedao.close();
             }
             
        	Calendar last = null;
        	if(urlList != null && urlList.size()>0){
        		last = ((Dnsmonitor_realtime)urlList.get(0)).getMon_time();
        	}
        	int interval = 0;
        	//TaskXmlUtil taskutil = new TaskXmlUtil("urltask");
        	try {
    			//Session session = this.beginTransaction();
    			List numList = new ArrayList();
    			TaskXml taskxml = new TaskXml();
    			numList = taskxml.ListXml();
    			for (int i = 0; i < numList.size(); i++) {
    				Task task = new Task();
    				BeanUtils.copyProperties(task, numList.get(i));
    				if (task.getTaskname().equals("urltask")){
    					interval = task.getPolltime().intValue();
    					//numThreads = task.getPolltime().intValue();
    				}
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        	DateE de=new DateE();
        	if (last == null){
        		last = new GregorianCalendar();
        		flag=1;
        	}
        	lasttime = de.getDateDetail(last);
        	last.add(Calendar.MINUTE,interval);
        	nexttime = de.getDateDetail(last);
        	
        	int hour=1;
        	if(getParaValue("hour")!=null){
        		 hour = Integer.parseInt(getParaValue("hour"));
        	}else{
        		request.setAttribute("hour", "1");
        		//urlconfForm.setHour("1");
        	}
        	
        	InitCoordinate initer=new InitCoordinate(new GregorianCalendar(),hour,1);
        	//Minute[] minutes=initer.getMinutes();
        	TimeSeries ss1 = new TimeSeries("", Minute.class); 
        	TimeSeries ss2 = new TimeSeries("", Minute.class); 
        	
        	//ss.add()
        	TimeSeries[] s = new TimeSeries[1];
        	TimeSeries[] s_ = new TimeSeries[1];
        	//Vector wave_v = historyManager.getInfo(queryid,initer);
        	Vector wave_v = null;
        	try{
        		wave_v = historydao.getByDnsid(queryid, starttime, totime,0);
        	}catch(Exception e){
        		e.printStackTrace();
        	}finally{
        		//historydao.close();
        	}
        	if(wave_v == null)wave_v = new Vector();
        	for(int i=0;i<wave_v.size();i++){
        		Hashtable ht = (Hashtable)wave_v.get(i);
        		double conn = Double.parseDouble(ht.get("conn").toString());
        		String time = ht.get("mon_time").toString();
          		ss1.addOrUpdate(new Minute(sdf1.parse(time)),conn);
        	}
        	s[0] = ss1;
        	s_[0] = ss2;
        	ChartGraph cg = new ChartGraph();
        	//cg.timewave(s,"时间","连通","",wave_name,600,120);
        	//cg.timewave(s_,"时间","时延(ms)","",delay_name,600,120);
        	//p_draw_line(s,"","url"+queryid+"ConnectUtilization",740,120);
        	
        	//是否连通
        	String conn[] = new String[2];
        	if (flag == 0)
        		conn = historydao.getAvailability(queryid,starttime,totime,"is_canconnected");
        	else{
        		conn = historydao.getAvailability(queryid,starttime,totime,"is_canconnected");
        	}
        	String[] key1 = {"连通","未连通"};
        	drawPiechart(key1,conn,"",conn_name);
        	connrate = getF(String.valueOf(Float.parseFloat(conn[0])/(Float.parseFloat(conn[0])+Float.parseFloat(conn[1]))*100))+"%";	
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	historydao.close();
        	configdao.close();
        	realtimedao.close();
        }
        request.setAttribute("urllist",urllist);
        request.setAttribute("initconf",initconf);
        request.setAttribute("lasttime",lasttime);
        request.setAttribute("nexttime",nexttime);
        request.setAttribute("conn_name",conn_name);
        request.setAttribute("valid_name",valid_name);
        request.setAttribute("fresh_name",fresh_name);
        request.setAttribute("wave_name",wave_name);
        request.setAttribute("delay_name",delay_name);
        request.setAttribute("connrate",connrate);
        request.setAttribute("validrate",validrate);
        request.setAttribute("freshrate",freshrate);
        request.setAttribute("from_date1",from_date1);
        request.setAttribute("to_date1",to_date1);
        request.setAttribute("from_hour",from_hour);
        request.setAttribute("to_hour",to_hour);
		return "/application/dns/detail.jsp";
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
	
	public List<DnsConfig> getDnsConfigListByMonflag(Integer flag){
		DnsConfigDao dnsConfigDao = null;
		List<DnsConfig> dnsConfigList = null;
		try{
			dnsConfigDao = new DnsConfigDao();
			dnsConfigList = (List<DnsConfig>)dnsConfigDao.getDNSConfigListByMonFlag(flag);
		}catch(Exception e){
			
		}finally{
			dnsConfigDao.close();
		} 
		return dnsConfigList;
	}

}
