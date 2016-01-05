package com.afunms.application.manage;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import com.afunms.application.dao.ApacheConfigDao;
import com.afunms.application.dao.Apachemonitor_historyDao;
import com.afunms.application.dao.Apachemonitor_realtimeDao;
import com.afunms.application.dao.DBDao;
import com.afunms.application.jbossmonitor.HttpClientJBoss;
import com.afunms.application.model.ApacheConfig;
import com.afunms.application.model.Apachemonitor_realtime;
import com.afunms.application.model.DBVo;
import com.afunms.application.model.JBossConfig;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.ChartGraph;
import com.afunms.common.util.ChartXml;
import com.afunms.common.util.CreateTableManager;
import com.afunms.common.util.DateE;
import com.afunms.common.util.InitCoordinate;
import com.afunms.common.util.SessionConstant;
import com.afunms.polling.om.Task;
import com.afunms.polling.task.TaskXml;
import com.afunms.system.model.User;
import com.afunms.topology.dao.LineDao;
import com.afunms.topology.dao.ManageXmlDao;
import com.afunms.topology.dao.NodeDependDao;
import com.afunms.topology.model.ManageXml;
import com.afunms.topology.model.NodeDepend;
import com.afunms.topology.util.KeyGenerator;
import com.ibm.ctg.server.TServer;

public class ApacheManager extends BaseManager implements ManagerInterface{
	
	/**
	 * 查询apache信息
	 * @return
	 */
	private String list()
	{
		List ips = new ArrayList();
		ApacheConfig vo = null;
		ApacheConfigDao configdao = null;
		User operator = (User) session.getAttribute(SessionConstant.CURRENT_USER);
		String bids = operator.getBusinessids();
		if (bids == null){
			bids = "";
		}
		String bid[] = bids.split(",");
		Vector rbids = new Vector();
		if (bid != null && bid.length > 0) {
			for (int i = 0; i < bid.length; i++) {
				if (bid[i] != null && bid[i].trim().length() > 0){
					rbids.add(bid[i].trim());
				}
			}
		}
		List list = null;
		try {
			configdao = new ApacheConfigDao();
			if (operator.getRole() == 0) {
				list = configdao.loadAll();
			} else {
				list = configdao.getApacheByBID(rbids);
			}
			request.setAttribute("list", list);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			configdao.close();
		}
		setTarget("/application/apache/list.jsp");
		return "/application/apache/list.jsp";
		//return list(configdao);
	}
   /**
    * 添加一条apache信息
    * @return
    */
	private String add()
    {   
		ApacheConfigDao dao=null;
		ApacheConfig vo = new ApacheConfig();
		vo.setId(KeyGenerator.getInstance().getNextKey());
		vo.setAlias(getParaValue("alias"));
		vo.setUsername(getParaValue("username"));
    	vo.setPassword(getParaValue("password"));
    	vo.setIpaddress(getParaValue("ipaddress"));
    	vo.setPort(getParaIntValue("port"));
		vo.setFlag(getParaIntValue("flag"));
		vo.setSendmobiles(getParaValue("sendmobiles"));
		vo.setSendemail(getParaValue("sendemail"));
		vo.setSendemail(getParaValue("sendphone")); 
        vo.setNetid(getParaValue("bid"));  
    	try{
	         dao = new ApacheConfigDao();
	         dao.save(vo);
    	}catch(Exception e){
				
		}finally{
			dao.close();
		} 
        
        return "/apache.do?action=list&jp=1";
		//return list();
    }    
	/**
	 * 删除一条apache信息记录
	 * @return
	 */
	public String delete()
	{
		String[] ids = getParaArrayValue("checkbox");
		ApacheConfig vo = new ApacheConfig();
		List list = new ArrayList();
		ApacheConfigDao configdao = new ApacheConfigDao();
    	if(ids != null && ids.length > 0){		
    		configdao.delete(ids);
    		for(int i=0;i<ids.length;i++){
    			//更新业务视图
    			String id = ids[i];
    			NodeDependDao nodedependao = new NodeDependDao();
    			List weslist = nodedependao.findByNode("apa"+id);
    			if(weslist!=null&&weslist.size()>0){
    				for(int j = 0; j < weslist.size(); j++){
    					NodeDepend wesvo = (NodeDepend)weslist.get(j);
    					if(wesvo!=null){
    						LineDao lineDao = new LineDao();
    		    			lineDao.deleteByidXml("apa"+id, wesvo.getXmlfile());
    		    			NodeDependDao nodeDependDao = new NodeDependDao();
    		    			if(nodeDependDao.isNodeExist("apa"+id, wesvo.getXmlfile())){
    		            		nodeDependDao.deleteByIdXml("apa"+id, wesvo.getXmlfile());
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
    		//删除Apache Http Server在临时表里中存储的数据
	        String[] nmsTempDataTables = {"nms_apache_temp"};
	        CreateTableManager createTableManager = new CreateTableManager();
	        createTableManager.clearNmsTempDatas(nmsTempDataTables, ids);
    	}
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
			configdao = new ApacheConfigDao();
			list = configdao.getApacheByBID(rbids);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			configdao.close();
		}
		request.setAttribute("list",list);
		return list();
	}
	/**
	 * 修改apache信息
	 * @return
	 */
	private String update()
    {    	   
		ApacheConfig vo = new ApacheConfig();
    	vo.setId(getParaIntValue("id"));
    	vo.setAlias(getParaValue("alias"));
    	vo.setUsername(getParaValue("username"));
    	vo.setPassword(getParaValue("password"));
    	vo.setIpaddress(getParaValue("ipaddress"));
    	vo.setPort(getParaIntValue("port"));
		vo.setFlag(getParaIntValue("flag"));
		vo.setSendmobiles(getParaValue("sendmobiles"));
		vo.setSendemail(getParaValue("sendemail"));
		vo.setSendemail(getParaValue("sendphone"));
		vo.setNetid(getParaValue("bid"));  
//		vo.setNetid(getParaValue("netid"));
//        String allbid = ",";
//        String[] businessids = getParaArrayValue("checkbox");
//        if(businessids != null && businessids.length>0){
//        	for(int i=0;i<businessids.length;i++){
//        		
//        		String bid = businessids[i];
//        		allbid= allbid+bid+",";
//        	}
//        } 
//        vo.setNetid(allbid);
        ApacheConfigDao configdao = null;
        try{
        	configdao = new ApacheConfigDao();
        	configdao.update(vo);	
			
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	if (configdao != null) {
				configdao.close();
			}
        }
             
		return list();
    }
	
	/**
	 * 添加apache监视信息
	 * @return
	 */
	private String addalert()
    {    
		ApacheConfig vo = new ApacheConfig();
		ApacheConfigDao configdao = null;
		
		List list = new ArrayList();

		try{
			configdao = new ApacheConfigDao();
			vo = (ApacheConfig)configdao.findByID(getParaValue("id"));
			vo.setFlag(1);
			configdao = new ApacheConfigDao();
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
//			configdao = new ApacheConfigDao();
			list = configdao.getApacheByBID(rbids);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(configdao != null){
				configdao.close();
			}
		}
		request.setAttribute("list",list);
		return list();
    }
	/**
	 * 取消Apache监视信息
	 * @return
	 */
	private String cancelalert()
    {    
		ApacheConfig vo = new ApacheConfig();
		ApacheConfigDao configdao = null;
		DBVo dbvo = new DBVo();
		DBDao dao = null;
		List list = new ArrayList();
		List conflist = new ArrayList();
		List ips = new ArrayList();
		String ipaddress ="";
		try{
			configdao = new ApacheConfigDao();
			dao = new DBDao();
			vo = (ApacheConfig)configdao.findByID(getParaValue("id"));
			vo.setFlag(0);
			configdao = new ApacheConfigDao();
			configdao.update(vo);	
			User operator = (User)session.getAttribute(SessionConstant.CURRENT_USER);
			String bids = operator.getBusinessids();
			if(bids == null)bids="";
			String bid[] = bids.split(",");
			Vector rbids = new Vector();
			if(bid != null && bid.length>0){
				for(int i=0;i<bid.length;i++){
					if(bid[i] != null && bid[i].trim().length()>0){
						rbids.add(bid[i].trim());
					}
				}
			}
//			configdao = new ApacheConfigDao();
			list = configdao.getApacheByBID(rbids);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dao != null) {
				dao.close();
			}
			if (configdao != null) {
				configdao.close();
			}
		}
		request.setAttribute("list",list);
		return list();
    }
	
	
	public Hashtable collectapachedata(ApacheConfig apacheConfig){
		HttpClientJBoss apache = new HttpClientJBoss();
		Hashtable<String, String> apache_ht = new Hashtable<String, String>();
	   	   String src=apache.getGetResponseWithHttpClient("http://"+apacheConfig.getIpaddress()+":"+apacheConfig.getPort()+"/server-status", "GBK");
//	   	   System.out.println(src);
	   	   int status=0;
	   	   if(src.contains("Version"))
	   	   {    
	   		   status=0;
	   		   String version=src.substring(src.indexOf("Version"));
	   		   String apa_version=version.substring(version.indexOf("Version")+9,version.indexOf("/dt")-1);
	   		   //版本号
	   		   String built=src.substring(src.indexOf("Built"));
	   		   String apa_built=built.substring(built.indexOf("Built")+7,built.indexOf("/dt")-1);
	   		   //编译安装时间
	   		   String current=src.substring(src.indexOf("Current"));
	   		   String apa_current=current.substring(current.indexOf("Current")+14,current.indexOf("/dt")-1);
	   		   //目前系统时间
	   		   String restart=src.substring(src.indexOf("Restart"));
	   		   String apa_restart=restart.substring(restart.indexOf("Restart")+14,restart.indexOf("/dt")-1);
	   		   //重新启动时间
	   		   String parent =src.substring(src.indexOf("Parent"));
	   		   String apa_parent=parent.substring(parent.indexOf("Parent")+26,parent.indexOf("/dt")-1);
	   		   //父程序的世代编号
	   		   String uptime=src.substring(src.indexOf("uptime"));
	   		   String apa_uptime=uptime.substring(uptime.indexOf("uptime")+9,uptime.indexOf("/dt")-1);
	   		   //服务器运行时间
	   		   String accesses=src.substring(src.indexOf("accesses"));
	   		   String apa_accesses=accesses.substring(accesses.indexOf("accesses")+10,accesses.indexOf("-"));
	   		   //接受的联机数量
	   		   String traffic=src.substring(src.indexOf("Traffic"));
	   		   String apa_traffic=traffic.substring(traffic.indexOf("Traffic")+9,traffic.indexOf("/dt")-1);
	   		   //传输的数据量
	   		   String swss=traffic.substring(traffic.indexOf("<dt>"));
	   		   String apa_swss1=swss.substring(swss.indexOf("<dt>")+4,swss.indexOf("</dt>"));
	   		   String swss1=swss.substring(swss.indexOf("</dt>")+5);
	   		   String apa_swss2=swss1.substring(swss1.indexOf("<dt>")+4,swss1.indexOf("</dt>"));
	   		   //apaswss1和apaswss2为Apache process目前的状态

	   		   String pre=src.substring(src.indexOf("pre")+4,src.indexOf("/pre")-1);
	   		   
	   		   String vhost=src.substring(src.indexOf("VHost"));
	   		   String vhost1=vhost.substring(vhost.indexOf("</tr>")+6);
	   		   String td=vhost1.substring(vhost1.indexOf("<tr>"),vhost1.indexOf("</table>"));
	   		   
		   		/**
		   		 * *  注释：  _：等待连结中  S：启动中  R：正在读取要求  W：正在送出回应  K：处于保持联机的状态   D：正在查找DNS  C：正在关闭连结   
		            * L：正在写入记录文件  G：进入正常结束程序中  I：处理闲置   .：尚无此程序 
		   		 */
	   		   	apa_built = apa_built.replaceAll("\n", "");//过滤掉换行符
	   		    pre = pre.replaceAll("\\n", "");
		   		if(pre.indexOf("_") != -1){
		   			pre = pre.replaceAll("_++", "等待连结中-->");
		   		}
		   		if(pre.indexOf(".") != -1){
		   			pre = pre.replaceAll("\\.++", "尚无此程序-->");
		   		}
		   		if(pre.indexOf("S") != -1){
		   			pre = pre.replaceAll("S", "启动中-->");
		   		}
		   		if(pre.indexOf("R") != -1){
		   			pre = pre.replaceAll("R", "正在读取要求-->");
		   		}
		   		if(pre.indexOf("W") != -1){
		   			pre = pre.replaceAll("W", "正在送出回应-->");
		   		}
		   		if(pre.indexOf("K") != -1){
		   			pre = pre.replaceAll("K", "处于保持联机的状态-->");
		   		}
		   		if(pre.indexOf("D") != -1){
		   			pre = pre.replaceAll("D", "正在查找DNS-->");
		   		}
		   		if(pre.indexOf("C") != -1){
		   			pre = pre.replaceAll("C", "正在关闭连结-->");
		   		}
		   		if(pre.indexOf("L") != -1){
		   			pre = pre.replaceAll("L", "正在写入记录文件-->");
		   		}
		   		if(pre.indexOf("G") != -1){
		   			pre = pre.replaceAll("G", "进入正常结束程序中-->");
		   		}
		   		if(pre.indexOf("I") != -1){
		   			pre = pre.replaceAll("I", "处理闲置-->");
		   		}
	   		   String req_sec=apa_swss1.substring(apa_swss1.indexOf(">")+1,apa_swss1.indexOf("-"));
	   		   //request/sec的数值
	   		   String b_sec=apa_swss1.substring(apa_swss1.indexOf("-")+1,apa_swss1.indexOf("d")+1);
	   		   //B/second的数值
	   		   String b_sec1=apa_swss1.substring(apa_swss1.indexOf("second"));
	   		   String b_req=b_sec1.substring(b_sec1.indexOf("-")+1,b_sec1.indexOf("t")+1);
	   		   //B/request的数值
	   		   String process=apa_swss2.substring(apa_swss2.indexOf(">")+1,apa_swss2.indexOf("req"));
	   		   //process
	   		   String workers=apa_swss2.substring(apa_swss2.indexOf(",")+2,apa_swss2.indexOf("idle"));
	   		   //workers
	   		   
		        apache_ht.put("version", apa_version);
		        apache_ht.put("built", apa_built);
		        apache_ht.put("current", apa_current);
		        apache_ht.put("restart", apa_restart);
		        apache_ht.put("parent", apa_parent);
		        apache_ht.put("uptime", apa_uptime);
		        apache_ht.put("accesses", apa_accesses);
		        apache_ht.put("traffic", apa_traffic);
		        apache_ht.put("swss", apa_swss1);
		        apache_ht.put("swss1", apa_swss2);
	            apache_ht.put("pre", pre);
//	            apache_ht.put("td", td);
		        apache_ht.put("req_sec", req_sec);
		        apache_ht.put("b_sec", b_sec);
		        apache_ht.put("b_req", b_req);
		        apache_ht.put("process", process);
		        apache_ht.put("workers", workers);
	            apache_ht.put("status", String.valueOf(status));   	
	   	   }else{
	   		   status=1;
	   		   apache_ht.put("status", String.valueOf(status));
	   	   }
	   	   return apache_ht;
	}
	
	/**
	 * 
	 * apache服务流程
	 * @return
	 */

	private String apacheServer()
    {    
		Integer id = getParaIntValue("id");
	    ApacheConfig  vo= null;
		ApacheConfigDao dao = null;
		List arr = null;
		try {
			dao = new ApacheConfigDao();
			arr = dao.getApacheById(id);
		} catch (RuntimeException e1) {
			e1.printStackTrace();
		} finally{
			if(dao != null){
				dao.close();
			}
		}
		if(arr != null && arr.size()>0){
			vo=(ApacheConfig)arr.get(0);
		}
//		HttpClientJBoss apache = new HttpClientJBoss();
//		Hashtable<String, String> apache_ht = new Hashtable<String, String>();
//	   	   String src=apache.getGetResponseWithHttpClient("http://"+vo.getIpaddress()+":"+vo.getPort()+"/server-status", "GBK");
//	   	   System.out.println(src);
//	   	   int status=0;
//	   	   if(src.contains("Version"))
//	   	   {    
//	   		   status=0;
//	   		   String version=src.substring(src.indexOf("Version"));
//	   		   String apa_version=version.substring(version.indexOf("Version")+9,version.indexOf("/dt")-1);
//	   		   //版本号
//	   		   String built=src.substring(src.indexOf("Built"));
//	   		   String apa_built=built.substring(built.indexOf("Built")+7,built.indexOf("/dt")-1);
//	   		   //编译安装时间
//	   		   String current=src.substring(src.indexOf("Current"));
//	   		   String apa_current=current.substring(current.indexOf("Current")+14,current.indexOf("/dt")-1);
//	   		   //目前系统时间
//	   		   String restart=src.substring(src.indexOf("Restart"));
//	   		   String apa_restart=restart.substring(restart.indexOf("Restart")+14,restart.indexOf("/dt")-1);
//	   		   //重新启动时间
//	   		   String parent =src.substring(src.indexOf("Parent"));
//	   		   String apa_parent=parent.substring(parent.indexOf("Parent")+26,parent.indexOf("/dt")-1);
//	   		   //父程序的世代编号
//	   		   String uptime=src.substring(src.indexOf("uptime"));
//	   		   String apa_uptime=uptime.substring(uptime.indexOf("uptime")+9,uptime.indexOf("/dt")-1);
//	   		   //服务器运行时间
//	   		   String accesses=src.substring(src.indexOf("accesses"));
//	   		   String apa_accesses=accesses.substring(accesses.indexOf("accesses")+10,accesses.indexOf("-"));
//	   		   //接受的联机数量
//	   		   String traffic=src.substring(src.indexOf("Traffic"));
//	   		   String apa_traffic=traffic.substring(traffic.indexOf("Traffic")+9,traffic.indexOf("/dt")-1);
//	   		   //传输的数据量
//	   		   String swss=traffic.substring(traffic.indexOf("<dt>"));
//	   		   String apa_swss1=swss.substring(swss.indexOf("<dt>")+4,swss.indexOf("</dt>"));
//	   		   String swss1=swss.substring(swss.indexOf("</dt>")+5);
//	   		   String apa_swss2=swss1.substring(swss1.indexOf("<dt>")+4,swss1.indexOf("</dt>"));
//	   		   //apaswss1和apaswss2为Apache process目前的状态
//
//	   		   String pre=src.substring(src.indexOf("pre")+4,src.indexOf("/pre")-1);
//	   		   
//	   		   String vhost=src.substring(src.indexOf("VHost"));
//	   		   String vhost1=vhost.substring(vhost.indexOf("</tr>")+6);
//	   		   String td=vhost1.substring(vhost1.indexOf("<tr>"),vhost1.indexOf("</table>"));
//	   		   
//	        apache_ht.put("version", apa_version);
//	        apache_ht.put("built", apa_built);
//	        apache_ht.put("current", apa_current);
//	        apache_ht.put("restart", apa_restart);
//	        apache_ht.put("parent", apa_parent);
//	        apache_ht.put("uptime", apa_uptime);
//	        apache_ht.put("accesses", apa_accesses);
//	        apache_ht.put("traffic", apa_traffic);
//	        apache_ht.put("swss", apa_swss1);
//	        apache_ht.put("swss1", apa_swss2);
//            apache_ht.put("pre", pre);
//            apache_ht.put("td", td);
//	   		   //System.out.println(src);   	
//	   	   }else
//	   	   {
//	   		status=1;
//	   	   }
//	   	request.setAttribute("apache_ht", apache_ht);
  // 		request.setAttribute("status", status);
   		request.setAttribute("vo", vo);
	    java.text.SimpleDateFormat timeFormatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ApacheConfigDao configdao = new ApacheConfigDao();
		Apachemonitor_realtimeDao realtimedao = new Apachemonitor_realtimeDao();
		Apachemonitor_historyDao historydao = new Apachemonitor_historyDao();
		List urllist = new ArrayList();      //用做条件选择列表
		ApacheConfig initconf = new ApacheConfig();    //当前的对象
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
				urllist = configdao.getApacheByBID(rbids);
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
        		configdao = new ApacheConfigDao();
        		try{
        			initconf = (ApacheConfig)configdao.findByID(queryid+"");
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
            	 urlList = realtimedao.getByApacheId(queryid);
             }catch(Exception e){
            	 e.printStackTrace();
             }finally{
            	 realtimedao.close();
             }
             
        	Calendar last = null;
        	if(urlList != null && urlList.size()>0){
        		last = ((Apachemonitor_realtime)urlList.get(0)).getMon_time();
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
        		wave_v = historydao.getByApacheid(queryid, starttime, totime,0);
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
        	cg.timewave(s,"时间","连通","",wave_name,600,120);
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
        request.setAttribute("wave_name",wave_name);;
        request.setAttribute("connrate",connrate);
        request.setAttribute("from_date1",from_date1);
        request.setAttribute("to_date1",to_date1);
        request.setAttribute("from_hour",from_hour);
        request.setAttribute("to_hour",to_hour);
		return "/application/apache/apacheserver.jsp";
    }
	/**
	 * 性能信息
	 * @return
	 */
	private String apacheStatus()
    {    
		Integer id = getParaIntValue("id");
	    ApacheConfig  vo= null;
		ApacheConfigDao dao = new ApacheConfigDao();
		List arr = dao.getApacheById(id);
		for(int i=0;i<arr.size();i++)
		{
			  vo=(ApacheConfig)arr.get(i);
		}
//		HttpClientJBoss apache = new HttpClientJBoss();
//		Hashtable<String, String> apache_ht = new Hashtable<String, String>();
//	   	   String src=apache.getGetResponseWithHttpClient("http://"+vo.getIpaddress()+":"+vo.getPort()+"/server-status", "GBK");
//	   	   //System.out.println(src);
//	   	   int status=0;
//	   	   if(src.contains("Version"))
//	   	   {    
//	   		   status=0;
//	   		   String version=src.substring(src.indexOf("Version"));
//	   		   String apa_version=version.substring(version.indexOf("Version")+9,version.indexOf("/dt")-1);
//	   		   //版本号
//	   		   String built=src.substring(src.indexOf("Built"));
//	   		   String apa_built=built.substring(built.indexOf("Built")+7,built.indexOf("/dt")-1);
//	   		   //编译安装时间
//	   		   String current=src.substring(src.indexOf("Current"));
//	   		   String apa_current=current.substring(current.indexOf("Current")+14,current.indexOf("/dt")-1);
//	   		   //目前系统时间
//	   		   String restart=src.substring(src.indexOf("Restart"));
//	   		   String apa_restart=restart.substring(restart.indexOf("Restart")+14,restart.indexOf("/dt")-1);
//	   		   //重新启动时间
//	   		   String parent =src.substring(src.indexOf("Parent"));
//	   		   String apa_parent=parent.substring(parent.indexOf("Parent")+26,parent.indexOf("/dt")-1);
//	   		   //父程序的世代编号
//	   		   String uptime=src.substring(src.indexOf("uptime"));
//	   		   String apa_uptime=uptime.substring(uptime.indexOf("uptime")+9,uptime.indexOf("/dt")-1);
//	   		   //服务器运行时间
//	   		   String accesses=src.substring(src.indexOf("accesses"));
//	   		   String apa_accesses=accesses.substring(accesses.indexOf("accesses")+10,accesses.indexOf("-"));
//	   		   //接受的联机数量
//	   		   String traffic=src.substring(src.indexOf("Traffic"));
//	   		   String apa_traffic=traffic.substring(traffic.indexOf("Traffic")+9,traffic.indexOf("/dt")-1);
//	   		   //传输的数据量
//	   		   String swss=traffic.substring(traffic.indexOf("<dt>"));
//	   		   String apa_swss1=swss.substring(swss.indexOf("<dt>")+4,swss.indexOf("</dt>"));
//	   		   String swss1=swss.substring(swss.indexOf("</dt>")+5);
//	   		   String apa_swss2=swss1.substring(swss1.indexOf("<dt>")+4,swss1.indexOf("</dt>"));
//	   		   //apaswss1和apaswss2为Apache process目前的状态
//
//	   		   String pre=src.substring(src.indexOf("pre")+4,src.indexOf("/pre")-1);
//	   		   
//	   		   String vhost=src.substring(src.indexOf("VHost"));
//	   		   String vhost1=vhost.substring(vhost.indexOf("</tr>")+6);
//	   		   String td=vhost1.substring(vhost1.indexOf("<tr>"),vhost1.indexOf("</table>"));
//	   		   
//	        apache_ht.put("version", apa_version);
//	        apache_ht.put("built", apa_built);
//	        apache_ht.put("current", apa_current);
//	        apache_ht.put("restart", apa_restart);
//	        apache_ht.put("parent", apa_parent);
//	        apache_ht.put("uptime", apa_uptime);
//	        apache_ht.put("accesses", apa_accesses);
//	        apache_ht.put("traffic", apa_traffic);
//	        apache_ht.put("swss", apa_swss1);
//	        apache_ht.put("swss1", apa_swss2);
//            apache_ht.put("pre", pre);
//            apache_ht.put("td", td);
//	   		   //System.out.println(src);   	
//	   	   }else
//	   	   {
//	   		status=1;
//	   	   }
//	   	request.setAttribute("apache_ht", apache_ht);
//   		request.setAttribute("status", status);
   		request.setAttribute("vo", vo);
	    java.text.SimpleDateFormat timeFormatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ApacheConfigDao configdao = new ApacheConfigDao();
		Apachemonitor_realtimeDao realtimedao = new Apachemonitor_realtimeDao();
		Apachemonitor_historyDao historydao = new Apachemonitor_historyDao();
		List urllist = new ArrayList();      //用做条件选择列表
		ApacheConfig initconf = new ApacheConfig();    //当前的对象
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
				urllist = configdao.getApacheByBID(rbids);
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
        		configdao = new ApacheConfigDao();
        		try{
        			initconf = (ApacheConfig)configdao.findByID(queryid+"");
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
            	 urlList = realtimedao.getByApacheId(queryid);
             }catch(Exception e){
            	 e.printStackTrace();
             }finally{
            	 realtimedao.close();
             }
             
        	Calendar last = null;
        	if(urlList != null && urlList.size()>0){
        		last = ((Apachemonitor_realtime)urlList.get(0)).getMon_time();
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
        		wave_v = historydao.getByApacheid(queryid, starttime, totime,0);
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
        	cg.timewave(s,"时间","连通","",wave_name,600,120);
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
        request.setAttribute("wave_name",wave_name);;
        request.setAttribute("connrate",connrate);
        request.setAttribute("from_date1",from_date1);
        request.setAttribute("to_date1",to_date1);
        request.setAttribute("from_hour",from_hour);
        request.setAttribute("to_hour",to_hour);
		return "/application/apache/apachestatus.jsp";
    }
	
	/**
	 * 进程状态
	 * @return
	 */
	private String apacheProcess()
    {    
		Integer id = getParaIntValue("id");
	    ApacheConfig  vo= null;
		ApacheConfigDao dao = new ApacheConfigDao();
		List arr = dao.getApacheById(id);
		for(int i=0;i<arr.size();i++)
		{
			  vo=(ApacheConfig)arr.get(i);
		}
//		HttpClientJBoss apache = new HttpClientJBoss();
//		Hashtable<String, String> apache_ht = new Hashtable<String, String>();
//	   	   String src=apache.getGetResponseWithHttpClient("http://"+vo.getIpaddress()+":"+vo.getPort()+"/server-status", "GBK");
//	   	   //System.out.println(src);
//	   	   int status=0;
//	   	   if(src.contains("Version"))
//	   	   {    
//	   		   status=0;
//	   		String version=src.substring(src.indexOf("Version"));
//		   	 String apa_version=version.substring(version.indexOf("Version")+9,version.indexOf("/dt")-1);
//	 		   //版本号
//	 		   String built=src.substring(src.indexOf("Built"));
//	 		   String apa_built=built.substring(built.indexOf("Built")+7,built.indexOf("/dt")-1);
//	 		   //编译安装时间
//	 		   String current=src.substring(src.indexOf("Current"));
//	 		   String apa_current=current.substring(current.indexOf("Current")+14,current.indexOf("/dt")-1);
//	 		   //目前系统时间
//	 		   String restart=src.substring(src.indexOf("Restart"));
//	 		   String apa_restart=restart.substring(restart.indexOf("Restart")+14,restart.indexOf("/dt")-1);
//	 		   //重新启动时间
//	 		   String parent =src.substring(src.indexOf("Parent"));
//	 		   String apa_parent=parent.substring(parent.indexOf("Parent")+26,parent.indexOf("/dt")-1);
//	 		   //父程序的世代编号
//	 		   String uptime=src.substring(src.indexOf("uptime"));
//	 		   String apa_uptime=uptime.substring(uptime.indexOf("uptime")+9,uptime.indexOf("/dt")-1);
//	 		   //服务器运行时间
//	 		   String accesses=src.substring(src.indexOf("accesses"));
//	 		   String apa_accesses=accesses.substring(accesses.indexOf("accesses")+10,accesses.indexOf("-"));
//	 		   //接受的联机数量
//	 		   String traffic=src.substring(src.indexOf("Traffic"));
//	 		   String apa_traffic=traffic.substring(traffic.indexOf("Traffic")+9,traffic.indexOf("/dt")-1);
//	 		   //传输的数据量
//	 		   String swss=traffic.substring(traffic.indexOf("<dt>"));
//	 		   String apa_swss1=swss.substring(swss.indexOf("<dt>")+4,swss.indexOf("</dt>"));
//	 		   String swss1=swss.substring(swss.indexOf("</dt>")+5);
//	 		   String apa_swss2=swss1.substring(swss1.indexOf("<dt>")+4,swss1.indexOf("</dt>"));
//	   		   
//	   		   String req_sec=apa_swss1.substring(apa_swss1.indexOf(">")+1,apa_swss1.indexOf("-"));
//	   		   //request/sec的数值
//	   		   String b_sec=apa_swss1.substring(apa_swss1.indexOf("-")+1,apa_swss1.indexOf("d")+1);
//	   		   //B/second的数值
//	   		   String b_sec1=apa_swss1.substring(apa_swss1.indexOf("second"));
//	   		   String b_req=b_sec1.substring(b_sec1.indexOf("-")+1,b_sec1.indexOf("t")+1);
//	   		   //B/request的数值
//	   		   String process=apa_swss2.substring(apa_swss2.indexOf(">")+1,apa_swss2.indexOf("req"));
//	   		   //process
//	   		   String workers=apa_swss2.substring(apa_swss2.indexOf(",")+2,apa_swss2.indexOf("idle"));
//	   		   //workers
//	   		apache_ht.put("version", apa_version);
//	        apache_ht.put("built", apa_built);
//	        apache_ht.put("current", apa_current);
//	        apache_ht.put("restart", apa_restart);
//	        apache_ht.put("parent", apa_parent);
//	        apache_ht.put("uptime", apa_uptime);
//	        apache_ht.put("accesses", apa_accesses);
//	        apache_ht.put("traffic", apa_traffic);
//	        apache_ht.put("req_sec", req_sec);
//	        apache_ht.put("b_sec", b_sec);
//	        apache_ht.put("b_req", b_req);
//	        apache_ht.put("process", process);
//	        apache_ht.put("workers", workers);
//	   		   //System.out.println(src);   	
//	   	   }else
//	   	   {
//	   		status=1;
//	   	   }
//	   	request.setAttribute("apache_ht", apache_ht);
//   		request.setAttribute("status", status);
   		request.setAttribute("vo", vo);
	    java.text.SimpleDateFormat timeFormatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ApacheConfigDao configdao = new ApacheConfigDao();
		Apachemonitor_realtimeDao realtimedao = new Apachemonitor_realtimeDao();
		Apachemonitor_historyDao historydao = new Apachemonitor_historyDao();
		List urllist = new ArrayList();      //用做条件选择列表
		ApacheConfig initconf = new ApacheConfig();    //当前的对象
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
				urllist = configdao.getApacheByBID(rbids);
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
        		configdao = new ApacheConfigDao();
        		try{
        			initconf = (ApacheConfig)configdao.findByID(queryid+"");
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
            	 urlList = realtimedao.getByApacheId(queryid);
             }catch(Exception e){
            	 e.printStackTrace();
             }finally{
            	 realtimedao.close();
             }
             
        	Calendar last = null;
        	if(urlList != null && urlList.size()>0){
        		last = ((Apachemonitor_realtime)urlList.get(0)).getMon_time();
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
        		wave_v = historydao.getByApacheid(queryid, starttime, totime,0);
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
        	cg.timewave(s,"时间","连通","",wave_name,600,120);
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
        request.setAttribute("wave_name",wave_name);;
        request.setAttribute("connrate",connrate);
        request.setAttribute("from_date1",from_date1);
        request.setAttribute("to_date1",to_date1);
        request.setAttribute("from_hour",from_hour);
        request.setAttribute("to_hour",to_hour);
		return "/application/apache/apacheprocess.jsp";
    }
	/**
	 *连通图
	 * @return
	 */
	private String apachePing()
    {    
		Integer id = getParaIntValue("id");
	    ApacheConfig  vo= null;
		ApacheConfigDao dao = new ApacheConfigDao();
		List arr = dao.getApacheById(id);
		for(int i=0;i<arr.size();i++)
		{
			  vo=(ApacheConfig)arr.get(i);
		}
//		HttpClientJBoss apache = new HttpClientJBoss();
//		Hashtable<String, String> apache_ht = new Hashtable<String, String>();
//	   	   String src=apache.getGetResponseWithHttpClient("http://"+vo.getIpaddress()+":"+vo.getPort()+"/server-status", "GBK");
//	   	   //System.out.println(src);
//	   	   int status=0;
//	   	   if(src.contains("Version"))
//	   	   {    
//	   		   status=0;
//	   		   String version=src.substring(src.indexOf("Version"));
//	   		   String apa_version=version.substring(version.indexOf("Version")+9,version.indexOf("/dt")-1);
//	   		   //版本号
//	   		   String built=src.substring(src.indexOf("Built"));
//	   		   String apa_built=built.substring(built.indexOf("Built")+7,built.indexOf("/dt")-1);
//	   		   //编译安装时间
//	   		   String current=src.substring(src.indexOf("Current"));
//	   		   String apa_current=current.substring(current.indexOf("Current")+14,current.indexOf("/dt")-1);
//	   		   //目前系统时间
//	   		   String restart=src.substring(src.indexOf("Restart"));
//	   		   String apa_restart=restart.substring(restart.indexOf("Restart")+14,restart.indexOf("/dt")-1);
//	   		   //重新启动时间
//	   		   String parent =src.substring(src.indexOf("Parent"));
//	   		   String apa_parent=parent.substring(parent.indexOf("Parent")+26,parent.indexOf("/dt")-1);
//	   		   //父程序的世代编号
//	   		   String uptime=src.substring(src.indexOf("uptime"));
//	   		   String apa_uptime=uptime.substring(uptime.indexOf("uptime")+9,uptime.indexOf("/dt")-1);
//	   		   //服务器运行时间
//	   		   String accesses=src.substring(src.indexOf("accesses"));
//	   		   String apa_accesses=accesses.substring(accesses.indexOf("accesses")+10,accesses.indexOf("-"));
//	   		   //接受的联机数量
//	   		   String traffic=src.substring(src.indexOf("Traffic"));
//	   		   String apa_traffic=traffic.substring(traffic.indexOf("Traffic")+9,traffic.indexOf("/dt")-1);
//	   		   //传输的数据量
//	   		   String swss=traffic.substring(traffic.indexOf("<dt>"));
//	   		   String apa_swss1=swss.substring(swss.indexOf("<dt>")+4,swss.indexOf("</dt>"));
//	   		   String swss1=swss.substring(swss.indexOf("</dt>")+5);
//	   		   String apa_swss2=swss1.substring(swss1.indexOf("<dt>")+4,swss1.indexOf("</dt>"));
//	   		   //apaswss1和apaswss2为Apache process目前的状态
//
//	   		   String pre=src.substring(src.indexOf("pre")+4,src.indexOf("/pre")-1);
//	   		   
//	   		   String vhost=src.substring(src.indexOf("VHost"));
//	   		   String vhost1=vhost.substring(vhost.indexOf("</tr>")+6);
//	   		   String td=vhost1.substring(vhost1.indexOf("<tr>"),vhost1.indexOf("</table>"));
//	   		   
//	        apache_ht.put("version", apa_version);
//	        apache_ht.put("built", apa_built);
//	        apache_ht.put("current", apa_current);
//	        apache_ht.put("restart", apa_restart);
//	        apache_ht.put("parent", apa_parent);
//	        apache_ht.put("uptime", apa_uptime);
//	        apache_ht.put("accesses", apa_accesses);
//	        apache_ht.put("traffic", apa_traffic);
//	        apache_ht.put("swss", apa_swss1);
//	        apache_ht.put("swss1", apa_swss2);
//            apache_ht.put("pre", pre);
//            apache_ht.put("td", td);
//	   		   //System.out.println(src);   	
//	   	   }else
//	   	   {
//	   		status=1;
//	   	   }
//	   	request.setAttribute("apache_ht", apache_ht);
//   		request.setAttribute("status", status);
   		request.setAttribute("vo", vo);
	    java.text.SimpleDateFormat timeFormatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ApacheConfigDao configdao = new ApacheConfigDao();
		Apachemonitor_realtimeDao realtimedao = new Apachemonitor_realtimeDao();
		Apachemonitor_historyDao historydao = new Apachemonitor_historyDao();
		List urllist = new ArrayList();      //用做条件选择列表
		ApacheConfig initconf = new ApacheConfig();    //当前的对象
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
				urllist = configdao.getApacheByBID(rbids);
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
        		configdao = new ApacheConfigDao();
        		try{
        			initconf = (ApacheConfig)configdao.findByID(queryid+"");
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
            	 urlList = realtimedao.getByApacheId(queryid);
             }catch(Exception e){
            	 e.printStackTrace();
             }finally{
            	 realtimedao.close();
             }
             
        	Calendar last = null;
        	if(urlList != null && urlList.size()>0){
        		last = ((Apachemonitor_realtime)urlList.get(0)).getMon_time();
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
        		wave_v = historydao.getByApacheid(queryid, starttime, totime,0);
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
        	cg.timewave(s,"时间","连通","",wave_name,600,120);
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
        request.setAttribute("wave_name",wave_name);;
        request.setAttribute("connrate",connrate);
        request.setAttribute("from_date1",from_date1);
        request.setAttribute("to_date1",to_date1);
        request.setAttribute("from_hour",from_hour);
        request.setAttribute("to_hour",to_hour);
		return "/application/apache/apacheping.jsp";
    }
	public String execute(String action) 
	{	
        if(action.equals("list"))
            return list();    
        if(action.equals("ready_add"))
        	return "/application/apache/add.jsp";
        if(action.equals("add"))
        	return add();
        if(action.equals("delete"))
            return delete();
        if(action.equals("ready_edit"))
        {	  
 		    DaoInterface dao = new ApacheConfigDao();
    	    setTarget("/application/apache/edit.jsp");
            return readyEdit(dao);
        }
        if(action.equals("update"))
            return update();
        if(action.equals("addalert"))
            return addalert();
        if(action.equals("cancelalert"))
            return cancelalert();
        if(action.equals("server"))
            return apacheServer();
        if(action.equals("status"))
            return apacheStatus();
        if(action.equals("process"))
            return apacheProcess();
        if(action.equals("ping"))
            return apachePing();
		setErrorCode(ErrorMessage.ACTION_NO_FOUND);
		return null;
	}
	
	private void drawPiechart(String[] keys,String[] values,String chname,String enname){
		ChartGraph cg = new ChartGraph();
		DefaultPieDataset piedata = new DefaultPieDataset();
		for(int i=0;i<keys.length;i++){
		  piedata.setValue(keys[i], new Double(values[i]).doubleValue());
		}
		cg.pie(chname,piedata,enname,200,120);    	
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
	
	public List<ApacheConfig> getApacheConfigListByMonflag(Integer flag){
		ApacheConfigDao apacheConfigDao = null;
		List<ApacheConfig> apacheConfigList = null;
		try{
			apacheConfigDao = new ApacheConfigDao();
			apacheConfigList = (List<ApacheConfig>)apacheConfigDao.getApacheConfigListByMonFlag(flag);
		}catch(Exception e){
			
		}finally{
			apacheConfigDao.close();
		} 
		return apacheConfigList;
	}

}
