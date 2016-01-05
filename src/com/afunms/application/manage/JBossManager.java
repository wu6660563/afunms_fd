package com.afunms.application.manage;


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
import com.afunms.application.dao.JBossConfigDao;
import com.afunms.application.dao.JBossmonitor_historyDao;
import com.afunms.application.dao.JBossmonitor_realtimeDao;
import com.afunms.application.jbossmonitor.HttpClientJBoss;
import com.afunms.application.model.DBVo;
import com.afunms.application.model.JBossConfig;
import com.afunms.application.model.JBossmonitor_realtime;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.ChartGraph;
import com.afunms.common.util.ChartXml;
import com.afunms.common.util.DateE;
import com.afunms.common.util.InitCoordinate;
import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.SysLogger;
import com.afunms.polling.om.Task;
import com.afunms.polling.task.TaskXml;
import com.afunms.system.model.User;
import com.afunms.topology.dao.LineDao;
import com.afunms.topology.dao.ManageXmlDao;
import com.afunms.topology.dao.NodeDependDao;
import com.afunms.topology.model.ManageXml;
import com.afunms.topology.model.NodeDepend;
import com.afunms.topology.util.KeyGenerator;

public class JBossManager extends BaseManager implements ManagerInterface{
	
	/**
	 * 查询jboss信息
	 * @return
	 */
	private String list()
	{
		List ips = new ArrayList();
		JBossConfig vo=null;
		JBossConfigDao configdao=null;
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
		configdao = new JBossConfigDao();
		try
		{
			if(operator.getRole() == 0){
				list = configdao.loadAll();
			}else{
				list = configdao.getJBossByBID(rbids);
			}
		request.setAttribute("list",list);
		}catch(Exception e)
		{
			e.printStackTrace();
		} 
	    finally
	    {
	    	configdao.close();
	    }
		setTarget("/application/jboss/list.jsp");
		return "/application/jboss/list.jsp";
		//return list(configdao);
	}
   /**
    * 添加一条JBOSS信息
    * @return
    */
	private String add()
    {   
		JBossConfigDao dao=null;
		JBossConfig vo = new JBossConfig();
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
         dao = new JBossConfigDao();
        dao.save(vo);
     }catch(Exception e){
			
		}finally{
			dao.close();
		} 
        
        return "/jboss.do?action=list&jp=1";
    }    
	/**
	 * 删除一条JBOSS信息记录
	 * @return
	 */
	public String delete()
	{
		String[] ids = getParaArrayValue("checkbox");
		JBossConfig vo = new JBossConfig();
		List list = new ArrayList();
		JBossConfigDao configdao = new JBossConfigDao();
    	if(ids != null && ids.length > 0){		
    		configdao.delete(ids);
    		for(int i=0;i<ids.length;i++){
    			//更新业务视图
    			String id = ids[i];
    			NodeDependDao nodedependao = new NodeDependDao();
    			List weslist = nodedependao.findByNode("jbo"+id);
    			if(weslist!=null&&weslist.size()>0){
    				for(int j = 0; j < weslist.size(); j++){
    					NodeDepend wesvo = (NodeDepend)weslist.get(j);
    					if(wesvo!=null){
    						LineDao lineDao = new LineDao();
    		    			lineDao.deleteByidXml("jbo"+id, wesvo.getXmlfile());
    		    			NodeDependDao nodeDependDao = new NodeDependDao();
    		    			if(nodeDependDao.isNodeExist("jbo"+id, wesvo.getXmlfile())){
    		            		nodeDependDao.deleteByIdXml("jbo"+id, wesvo.getXmlfile());
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
			configdao = new JBossConfigDao();
			list = configdao.getJBossByBID(rbids);
		}catch(Exception e){
			e.printStackTrace();
		}
		finally
		{
			configdao.close();
		}
		request.setAttribute("list",list);
		return list();
	}
	/**
	 * 修改JBOSS信息
	 * @return
	 */
	private String update()
    {    	   
		JBossConfig vo = new JBossConfig();
		JBossConfigDao dao = new JBossConfigDao();
		List list = new ArrayList();
		List conflist = new ArrayList();
		List ips = new ArrayList();
		String ipaddress ="";		
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
        JBossConfigDao configdao = new JBossConfigDao();
        try{
        	configdao.update(vo);	
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
			list = dao.getJBossByBID(rbids);
			
        }catch(Exception e){
        	e.printStackTrace();
        }
        finally
        {
        	dao.close();
        	configdao.close();
        }
		request.setAttribute("list",list);
             
		return list();
    }
	
	/**
	 * 添加JBOSS监视信息
	 * @return
	 */
	private String addalert()
    {    
		JBossConfig vo = new JBossConfig();
		JBossConfigDao configdao = new JBossConfigDao();
		
		List list = new ArrayList();

		try{
			vo = (JBossConfig)configdao.findByID(getParaValue("id"));
			vo.setFlag(1);
			configdao = new JBossConfigDao();
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
			configdao = new JBossConfigDao();
			list = configdao.getJBossByBID(rbids);
		}catch(Exception e){
			e.printStackTrace();
		}
		finally
		{
			configdao.close();
		}
		request.setAttribute("list",list);
		return list();
    }
	/**
	 * 取消JBOSS监视信息
	 * @return
	 */
	private String cancelalert()
    {    
		JBossConfig vo = new JBossConfig();
		JBossConfigDao configdao = new JBossConfigDao();
		DBVo dbvo = new DBVo();
		DBDao dao = new DBDao();
		List list = new ArrayList();
		List conflist = new ArrayList();
		List ips = new ArrayList();
		String ipaddress ="";
		try{
			vo = (JBossConfig)configdao.findByID(getParaValue("id"));
			vo.setFlag(0);
			configdao = new JBossConfigDao();
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
			configdao = new JBossConfigDao();
			list = configdao.getJBossByBID(rbids);
		}catch(Exception e){
			e.printStackTrace();
		}
		finally
		{
			dao.close();
			configdao.close();
		}
		request.setAttribute("list",list);
		return list();
    }
	
	/**
	 * 
	 * 监控信息
	 * @return
	 */

	private String detail()
    {    
		Integer id = getParaIntValue("id");
	    JBossConfig  vo= new JBossConfig();;
		JBossConfigDao dao = null;
		List arr = new ArrayList();
		try {
			dao = new JBossConfigDao();
			arr = dao.getJBossById(id);
		} catch (RuntimeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally{
			if(dao != null){
				dao.close();
			}
		}
		for(int i=0;i<arr.size();i++)
		{
			  vo=(JBossConfig)arr.get(i);
		}
//		HttpClientJBoss jboss = new HttpClientJBoss();
//		Hashtable<String, String> jboss_ht = new Hashtable<String, String>();
//	   	   String src=jboss.getGetResponseWithHttpClient("http://"+vo.getIpaddress()+":"+vo.getPort()+"/web-console/ServerInfo.jsp", "GBK");
//	   	   //System.out.println(src);
//	   	   int status=0;
//	   	   if(src.contains("Version"))
//	   	   {    
//	   		   status=0;
//	   		   //System.out.println(src.indexOf("SVNTag")+"aaa");
//	   		   String str=src.substring(src.indexOf("SVNTag"));
//	   		   String version=str.substring(7, str.indexOf("date"));//JBoss_4_2_2_GA 
//	   		   String date=str.substring(str.indexOf("date")+5, str.indexOf(")"));//200710221140
//	   		   
//	   		   String str1=str.substring(str.indexOf("Version Name:"));
//	   		   String versionname=str1.substring(str1.indexOf("</b>")+4, str1.indexOf("</font>"));//Trinity
//	   		   
//	   		   String str2=str1.substring(str1.indexOf("Built on:"));
//	   		   String builton=str2.substring(str2.indexOf("</b>")+4,str2.indexOf("</font>")); //October 22 2007
//	   		   
//	   		   String str3=str2.substring(str2.indexOf("Start date:"));
//	   		   String startdate=str3.substring(str3.indexOf("</b>")+4,str3.indexOf("</font>"));//Fri Apr 02 09:19:17 CST 2010
//	   		   
//	   		   String str4=str3.substring(str3.indexOf("Host:"));
//	   		   String host=str4.substring(str4.indexOf("</b>")+4,str4.indexOf("</font>"));//lenovo-ececc8f2 (10.10.152.46)
//	   		   
//	   		   String str5=str4.substring(str4.indexOf("Base Location:"));
//	   		   String baselocation=str5.substring(str5.indexOf("file:")+6,str5.indexOf("</font>"));//F:/jboss-4.2.2.GA/jboss-4.2.2.GA/server/
//	   		   
//	   		   String str6=str5.substring(str5.indexOf("Base Location (local):"));
//	   		   String baselocationlocal=str6.substring(str6.indexOf("</b>")+4,str6.indexOf("</font>"));//F:\jboss-4.2.2.GA\jboss-4.2.2.GA\server
//	   		   
//	   		   String str7=str6.substring(str6.indexOf("Running config:"));
//	   		   String runconfig=str7.substring(str7.indexOf("</b>")+4,str7.indexOf("</font>"));//'default'
//	   		   
//	   		   String str8=str7.substring(str7.indexOf("CPU:"));
//	   		   String cpu=str8.substring(str8.indexOf("</b>")+4,str8.indexOf("</font>"));//2
//         
//	   		   String str9=str8.substring(str8.indexOf("OS:"));
//	   		   String os=str9.substring(str9.indexOf("</b>")+4,str9.indexOf("</font>"));//Windows XP 5.1 (x86)
//	   		   
//	   		   String str10=str9.substring(str9.indexOf("Free Memory:"));
//	   		   String freememory=str10.substring(str10.indexOf("</b>")+4,str10.indexOf(" MB"));//117 MB
//	   		   String str11=str10.substring(str10.indexOf("Max Memory:"));
//	   		   String maxmemory=str11.substring(str11.indexOf("</b>")+4,str11.indexOf("</font>"));//493 MB
//	   		   
//	   		   String str12=str11.substring(str11.indexOf("Total Memory:"));
//	   		   String totalmemory=str12.substring(str12.indexOf("</b>")+4,str12.indexOf(" MB"));//165 MB
//	   		   String str13=str12.substring(str12.indexOf("Threads:"));
//	   		   String threads=str13.substring(str13.indexOf("</b>")+4,str13.indexOf("</font>"));//50
//	   		   
//	   		   String str14=str13.substring(str13.indexOf("JVM Version:"));
//	   		   String jvmversion=str14.substring(str14.indexOf("</b>")+4,str14.indexOf("</font>"));//1.5.0_06-b05 (Sun Microsystems Inc.)
//	   		   
//	   		   String str15=str14.substring(str14.indexOf("JVM Name:"));
//	   		   String jvmname=str15.substring(str15.indexOf("</b>")+4,str15.indexOf("</font>"));//Java HotSpot(TM) Server VM
//	        jboss_ht.put("version", version);
//	   		jboss_ht.put("date", date);
//	   		jboss_ht.put("versionname", versionname);
//	   		jboss_ht.put("builton", builton);
//	   		jboss_ht.put("startdate", startdate);
//	   		jboss_ht.put("host", host);
//	   		jboss_ht.put("baselocation", baselocation);
//	   		jboss_ht.put("baselocationlocal", baselocationlocal);
//	   		jboss_ht.put("runconfig", runconfig);
//	   		jboss_ht.put("cpu", cpu);
//	   		jboss_ht.put("os", os);
//	   		jboss_ht.put("freememory", freememory);
//	   		jboss_ht.put("maxmemory", maxmemory);
//	   		jboss_ht.put("totalmemory", totalmemory);
//	   		jboss_ht.put("threads", threads);
//	   		jboss_ht.put("jvmversion", jvmversion);
//	   		jboss_ht.put("jvmname", jvmname);
//	   	 
//	   		
//	   	
//	   		/*request.setAttribute("version", version);
//	   		request.setAttribute("date", date);
//	   		request.setAttribute("versionname", versionname);
//	   		request.setAttribute("builton", builton);
//	   		request.setAttribute("startdate", startdate);
//	   		request.setAttribute("host", host);
//	   		request.setAttribute("baselocation", baselocation);
//	   		request.setAttribute("baselocationlocal", baselocationlocal);
//	   		request.setAttribute("runconfig", runconfig);
//	   		request.setAttribute("cpu", cpu);
//	   		request.setAttribute("os", os);
//	   		request.setAttribute("freememory", freememory);
//	   		request.setAttribute("maxmemory", maxmemory);
//	   		request.setAttribute("totalmemory", totalmemory);
//	   		request.setAttribute("threads", threads);
//	   		request.setAttribute("jvmversion", jvmversion);
//	   		request.setAttribute("jvmname", jvmname);*/
//	   		   //System.out.println(src);   	
//	   	   }else
//	   	   {
//	   		status=1;
//	   	   }
//	   	String str=jboss.getGetResponseWithHttpClient("http://"+vo.getIpaddress()+":"+vo.getPort()+"/web-console/status", "GBK");
//	   	if(str.contains("ajp"))
//	   	{ 
//	   		
//	   		String ajp_total=str.substring(str.indexOf("ajp"));
//	   		String ajp1=ajp_total.substring(ajp_total.indexOf("ajp"),ajp_total.indexOf("http"));
//	   		String ajp=ajp1.substring(0,ajp1.indexOf("</h1>")); //ajp-127.0.0.1-8009
//	   		String ajp_maxthreads=ajp1.substring(ajp1.indexOf("Max threads:")+13,ajp1.indexOf("Current thread count:"));
//	   		String ajp_thrcount=ajp1.substring(ajp1.indexOf("Current thread count:")+22,ajp1.indexOf("Current thread busy:"));
//	   		String ajp_thrbusy=ajp1.substring(ajp1.indexOf("Current thread busy:")+21,ajp1.indexOf("<br>"));
//	   		String ajp_maxtime=ajp1.substring(ajp1.indexOf("Max processing time:")+21,ajp1.indexOf("Processing time:"));
//	   		String ajp_processtime=ajp1.substring(ajp1.indexOf("Processing time:")+17,ajp1.indexOf("Request count:"));
//	   		String ajp_requestcount=ajp1.substring(ajp1.indexOf("Request count:")+15,ajp1.indexOf("Error count:"));
//	   		String ajp_errorcount=ajp1.substring(ajp1.indexOf("Error count:")+13,ajp1.indexOf("Bytes received:"));
//	   		String ajp_bytereceived=ajp1.substring(ajp1.indexOf("Bytes received:")+16,ajp1.indexOf("Bytes sent:"));
//	   		String ajp_bytessent=ajp1.substring(ajp1.indexOf("Bytes sent:")+12,ajp1.indexOf("</p>"));
//	   		String http_total=str.substring(str.lastIndexOf("http"));
//	   		String http=http_total.substring(0,http_total.indexOf("</h1>"));
//	   		String http_maxthreads=ajp1.substring(http_total.indexOf("Max threads:")+14,ajp1.indexOf("Current thread count:"));
//	   		String http_thrcount=http_total.substring(http_total.indexOf("Current thread count:")+22,http_total.indexOf("Current thread busy:"));
//	   		String http_thrbusy=http_total.substring(http_total.indexOf("Current thread busy:")+21,http_total.indexOf("<br>"));
//	   		String http_maxtime=http_total.substring(http_total.indexOf("Max processing time:")+21,http_total.indexOf("Processing time:"));
//	   		String http_processtime=http_total.substring(http_total.indexOf("Processing time:")+17,http_total.indexOf("Request count:"));
//	   		String http_requestcount=http_total.substring(http_total.indexOf("Request count:")+15,http_total.indexOf("Error count:"));
//	   		String http_errorcount=http_total.substring(http_total.indexOf("Error count:")+13,http_total.indexOf("Bytes received:"));
//	   		String http_bytereceived=http_total.substring(http_total.indexOf("Bytes received:")+16,http_total.indexOf("Bytes sent:"));
//	   		String http_bytessent=http_total.substring(http_total.indexOf("Bytes sent:")+12,http_total.indexOf("</p>"));  
//	   		jboss_ht.put("ajp_maxthreads", ajp_maxthreads);
//	   		jboss_ht.put("ajp_thrcount", ajp_thrcount);
//	   		jboss_ht.put("ajp_thrbusy", ajp_thrbusy);
//	   		jboss_ht.put("ajp_maxtime", ajp_maxtime);
//	   		jboss_ht.put("ajp_processtime", ajp_processtime);
//	   		jboss_ht.put("ajp_requestcount", ajp_requestcount);
//	   		jboss_ht.put("ajp_errorcount", ajp_errorcount);
//	   		jboss_ht.put("ajp_bytereceived", ajp_bytereceived);
//	   		jboss_ht.put("ajp_bytessent", ajp_bytessent);
//	   		jboss_ht.put("ajp", ajp);
//	   		
//	   		jboss_ht.put("http_maxthreads", http_maxthreads);
//	   		jboss_ht.put("http_thrcount", http_thrcount);
//	   		jboss_ht.put("http_thrbusy", http_thrbusy);
//	   		jboss_ht.put("http_maxtime", http_maxtime);
//	   		jboss_ht.put("http_processtime", http_processtime);
//	   		jboss_ht.put("http_requestcount", http_requestcount);
//	   		jboss_ht.put("http_errorcount", http_errorcount);
//	   		jboss_ht.put("http_bytereceived", http_bytereceived);
//	   		jboss_ht.put("http_bytessent", http_bytessent);
//	   		jboss_ht.put("http", http);
//	   	}
	   //	request.setAttribute("jboss_ht", jboss_ht);
   		//request.setAttribute("status", status);
   		request.setAttribute("vo", vo);
	    java.text.SimpleDateFormat timeFormatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JBossConfigDao configdao = new JBossConfigDao();
		JBossmonitor_realtimeDao realtimedao = new JBossmonitor_realtimeDao();
		JBossmonitor_historyDao historydao = new JBossmonitor_historyDao();
		List urllist = new ArrayList();      //用做条件选择列表
		JBossConfig initconf = new JBossConfig();    //当前的对象
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
				urllist = configdao.getJBossByBID(rbids);
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
        		configdao = new JBossConfigDao();
        		try{
        			initconf = (JBossConfig)configdao.findByID(queryid+"");
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
            	 urlList = realtimedao.getByJBossId(queryid);
             }catch(Exception e){
            	 e.printStackTrace();
             }finally{
            	 realtimedao.close();
             }
             
        	Calendar last = null;
        	if(urlList != null && urlList.size()>0){
        		last = ((JBossmonitor_realtime)urlList.get(0)).getMon_time();
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
        		wave_v = historydao.getByJBossid(queryid, starttime, totime,0);
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
       //System.out.println(lasttime+nexttime+"conn_name:"+conn_name+"!!!!"+"wave_name:"+wave_name+"!!!!"+"connrate:"+connrate+"!!!!"+from_date1+to_date1+from_hour+to_hour);
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
		return "/application/jboss/detail.jsp";
    }
	
	/**
	 * 
	 * 监控信息
	 * @return
	 */
    public Hashtable collectjbossdata(JBossConfig jbossConfig){
    	//System.out.println("========="+jbossConfig.getIpaddress()+"======"+jbossConfig.getPort());
    	HttpClientJBoss jboss = new HttpClientJBoss();
		Hashtable<String, String> jboss_ht = new Hashtable<String, String>();
	   	   String src=jboss.getGetResponseWithHttpClient("http://"+jbossConfig.getIpaddress()+":"+jbossConfig.getPort()+"/web-console/ServerInfo.jsp", "GBK");
	   	  // System.out.println("========="+src);
	   	   int status=0;
	   	   if(src.contains("Version"))
	   	   {    
	   		   status=0;
	   		   //System.out.println(src.indexOf("SVNTag")+"aaa");
	   		   String str=src.substring(src.indexOf("SVNTag"));
	   		   String version=str.substring(7, str.indexOf("date"));//JBoss_4_2_2_GA 
	   		   String date=str.substring(str.indexOf("date")+5, str.indexOf(")"));//200710221140
	   		   
	   		   String str1=str.substring(str.indexOf("Version Name:"));
	   		   String versionname=str1.substring(str1.indexOf("</b>")+4, str1.indexOf("</font>"));//Trinity
	   		   
	   		   String str2=str1.substring(str1.indexOf("Built on:"));
	   		   String builton=str2.substring(str2.indexOf("</b>")+4,str2.indexOf("</font>")); //October 22 2007
	   		   
	   		   String str3=str2.substring(str2.indexOf("Start date:"));
	   		   String startdate=str3.substring(str3.indexOf("</b>")+4,str3.indexOf("</font>"));//Fri Apr 02 09:19:17 CST 2010
	   		   
	   		   String str4=str3.substring(str3.indexOf("Host:"));
	   		   String host=str4.substring(str4.indexOf("</b>")+4,str4.indexOf("</font>"));//lenovo-ececc8f2 (10.10.152.46)
	   		   
	   		   String str5=str4.substring(str4.indexOf("Base Location:"));
	   		   String baselocation=str5.substring(str5.indexOf("file:")+6,str5.indexOf("</font>"));//F:/jboss-4.2.2.GA/jboss-4.2.2.GA/server/
	   		   
	   		   String str6=str5.substring(str5.indexOf("Base Location (local):"));
	   		   String baselocationlocal=str6.substring(str6.indexOf("</b>")+4,str6.indexOf("</font>"));//F:\jboss-4.2.2.GA\jboss-4.2.2.GA\server
	   		   
	   		   String str7=str6.substring(str6.indexOf("Running config:"));
	   		   String runconfig=str7.substring(str7.indexOf("</b>")+4,str7.indexOf("</font>"));//'default'
	   		   
	   		   String str8=str7.substring(str7.indexOf("CPU:"));
	   		   String cpu=str8.substring(str8.indexOf("</b>")+4,str8.indexOf("</font>"));//2
         
	   		   String str9=str8.substring(str8.indexOf("OS:"));
	   		   String os=str9.substring(str9.indexOf("</b>")+4,str9.indexOf("</font>"));//Windows XP 5.1 (x86)
	   		   
	   		   String str10=str9.substring(str9.indexOf("Free Memory:"));
	   		   String freememory=str10.substring(str10.indexOf("</b>")+4,str10.indexOf(" MB"));//117 MB
	   		   String str11=str10.substring(str10.indexOf("Max Memory:"));
	   		   String maxmemory=str11.substring(str11.indexOf("</b>")+4,str11.indexOf("</font>"));//493 MB
	   		   
	   		   String str12=str11.substring(str11.indexOf("Total Memory:"));
	   		   String totalmemory=str12.substring(str12.indexOf("</b>")+4,str12.indexOf(" MB"));//165 MB
	   		   String str13=str12.substring(str12.indexOf("Threads:"));
	   		   String threads=str13.substring(str13.indexOf("</b>")+4,str13.indexOf("</font>"));//50
	   		   
	   		   String str14=str13.substring(str13.indexOf("JVM Version:"));
	   		   String jvmversion=str14.substring(str14.indexOf("</b>")+4,str14.indexOf("</font>"));//1.5.0_06-b05 (Sun Microsystems Inc.)
	   		   
	   		   String str15=str14.substring(str14.indexOf("JVM Name:"));
	   		   String jvmname=str15.substring(str15.indexOf("</b>")+4,str15.indexOf("</font>"));//Java HotSpot(TM) Server VM
	        jboss_ht.put("version", version);
	   		jboss_ht.put("date", date);
	   		jboss_ht.put("versionname", versionname);
	   		jboss_ht.put("builton", builton);
	   		jboss_ht.put("startdate", startdate);
	   		jboss_ht.put("host", host);
	   		jboss_ht.put("baselocation", baselocation);
	   		jboss_ht.put("baselocationlocal", baselocationlocal);
	   		jboss_ht.put("runconfig", runconfig);
	   		jboss_ht.put("cpu", cpu);
	   		jboss_ht.put("os", os);
	   		jboss_ht.put("freememory", freememory);
	   		jboss_ht.put("maxmemory", maxmemory);
	   		jboss_ht.put("totalmemory", totalmemory);
	   		jboss_ht.put("threads", threads);
	   		jboss_ht.put("jvmversion", jvmversion);
	   		jboss_ht.put("jvmname", jvmname);
	   		jboss_ht.put("status", String.valueOf(status));
	   		
	   	
	   		/*request.setAttribute("version", version);
	   		request.setAttribute("date", date);
	   		request.setAttribute("versionname", versionname);
	   		request.setAttribute("builton", builton);
	   		request.setAttribute("startdate", startdate);
	   		request.setAttribute("host", host);
	   		request.setAttribute("baselocation", baselocation);
	   		request.setAttribute("baselocationlocal", baselocationlocal);
	   		request.setAttribute("runconfig", runconfig);
	   		request.setAttribute("cpu", cpu);
	   		request.setAttribute("os", os);
	   		request.setAttribute("freememory", freememory);
	   		request.setAttribute("maxmemory", maxmemory);
	   		request.setAttribute("totalmemory", totalmemory);
	   		request.setAttribute("threads", threads);
	   		request.setAttribute("jvmversion", jvmversion);
	   		request.setAttribute("jvmname", jvmname);*/
	   		   //System.out.println(src);   	
	   	   }else
	   	   {
	   		status=1;
	   		
	   		jboss_ht.put("status", String.valueOf(status));
	   	   }
	   	String str=jboss.getGetResponseWithHttpClient("http://"+jbossConfig.getIpaddress()+":"+jbossConfig.getPort()+"/web-console/status", "GBK");
	   	if(str.contains("ajp"))
	   	{ 
	   		
	   		String ajp_total=str.substring(str.indexOf("ajp"));
	   		String ajp1=ajp_total.substring(ajp_total.indexOf("ajp"),ajp_total.indexOf("http"));
	   		String ajp=ajp1.substring(0,ajp1.indexOf("</h1>")); //ajp-127.0.0.1-8009
	   		String ajp_maxthreads=ajp1.substring(ajp1.indexOf("Max threads:")+13,ajp1.indexOf("Current thread count:"));
	   		String ajp_thrcount=ajp1.substring(ajp1.indexOf("Current thread count:")+22,ajp1.indexOf("Current thread busy:"));
	   		String ajp_thrbusy=ajp1.substring(ajp1.indexOf("Current thread busy:")+21,ajp1.indexOf("<br>"));
	   		String ajp_maxtime=ajp1.substring(ajp1.indexOf("Max processing time:")+21,ajp1.indexOf("Processing time:"));
	   		String ajp_processtime=ajp1.substring(ajp1.indexOf("Processing time:")+17,ajp1.indexOf("Request count:"));
	   		String ajp_requestcount=ajp1.substring(ajp1.indexOf("Request count:")+15,ajp1.indexOf("Error count:"));
	   		String ajp_errorcount=ajp1.substring(ajp1.indexOf("Error count:")+13,ajp1.indexOf("Bytes received:"));
	   		String ajp_bytereceived=ajp1.substring(ajp1.indexOf("Bytes received:")+16,ajp1.indexOf("Bytes sent:"));
	   		String ajp_bytessent=ajp1.substring(ajp1.indexOf("Bytes sent:")+12,ajp1.indexOf("</p>"));
	   		String http_total=str.substring(str.lastIndexOf("http"));
	   		String http=http_total.substring(0,http_total.indexOf("</h1>"));
	   		String http_maxthreads=ajp1.substring(http_total.indexOf("Max threads:")+14,ajp1.indexOf("Current thread count:"));
	   		String http_thrcount=http_total.substring(http_total.indexOf("Current thread count:")+22,http_total.indexOf("Current thread busy:"));
	   		String http_thrbusy=http_total.substring(http_total.indexOf("Current thread busy:")+21,http_total.indexOf("<br>"));
	   		String http_maxtime=http_total.substring(http_total.indexOf("Max processing time:")+21,http_total.indexOf("Processing time:"));
	   		String http_processtime=http_total.substring(http_total.indexOf("Processing time:")+17,http_total.indexOf("Request count:"));
	   		String http_requestcount=http_total.substring(http_total.indexOf("Request count:")+15,http_total.indexOf("Error count:"));
	   		String http_errorcount=http_total.substring(http_total.indexOf("Error count:")+13,http_total.indexOf("Bytes received:"));
	   		String http_bytereceived=http_total.substring(http_total.indexOf("Bytes received:")+16,http_total.indexOf("Bytes sent:"));
	   		String http_bytessent=http_total.substring(http_total.indexOf("Bytes sent:")+12,http_total.indexOf("</p>"));  
	   		jboss_ht.put("ajp_maxthreads", ajp_maxthreads);
	   		jboss_ht.put("ajp_thrcount", ajp_thrcount);
	   		jboss_ht.put("ajp_thrbusy", ajp_thrbusy);
	   		jboss_ht.put("ajp_maxtime", ajp_maxtime);
	   		jboss_ht.put("ajp_processtime", ajp_processtime);
	   		jboss_ht.put("ajp_requestcount", ajp_requestcount);
	   		jboss_ht.put("ajp_errorcount", ajp_errorcount);
	   		jboss_ht.put("ajp_bytereceived", ajp_bytereceived);
	   		jboss_ht.put("ajp_bytessent", ajp_bytessent);
	   		jboss_ht.put("ajp", ajp);
	   		
	   		jboss_ht.put("http_maxthreads", http_maxthreads);
	   		jboss_ht.put("http_thrcount", http_thrcount);
	   		jboss_ht.put("http_thrbusy", http_thrbusy);
	   		jboss_ht.put("http_maxtime", http_maxtime);
	   		jboss_ht.put("http_processtime", http_processtime);
	   		jboss_ht.put("http_requestcount", http_requestcount);
	   		jboss_ht.put("http_errorcount", http_errorcount);
	   		jboss_ht.put("http_bytereceived", http_bytereceived);
	   		jboss_ht.put("http_bytessent", http_bytessent);
	   		jboss_ht.put("http", http);
	   		
	   	}
		return jboss_ht;
    	
    }
	
	public String execute(String action) 
	{	
        if(action.equals("list"))
            return list();    
        if(action.equals("ready_add"))
        	return "/application/jboss/add.jsp";
        if(action.equals("add"))
        	return add();
        if(action.equals("delete"))
            return delete();
        if(action.equals("ready_edit"))
        {	  
 		    DaoInterface dao = new JBossConfigDao();
    	    setTarget("/application/jboss/edit.jsp");
            return readyEdit(dao);
        }
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
	
	public List<JBossConfig> getJBossConfigListByMonflag(Integer flag){
		JBossConfigDao jbossConfigDao = null;
		List<JBossConfig> jbossConfigList = null;
		try{
			jbossConfigDao = new JBossConfigDao();
			jbossConfigList = (List<JBossConfig>)jbossConfigDao.getJBossConfigListByMonFlag(flag);
		}catch(Exception e){
			
		}finally{
			jbossConfigDao.close();
		} 
		return jbossConfigList;
	}

}
