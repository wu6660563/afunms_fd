/**
 * <p>Description:DiscoverManager</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-12
 */

package com.afunms.event.manage;

import com.afunms.common.base.*;
import com.afunms.common.util.CreateTableManager;
import com.afunms.common.util.SessionConstant;
import com.afunms.polling.*;
import com.afunms.polling.node.*;
import com.afunms.report.abstraction.ExcelReport1;
import com.afunms.report.base.AbstractionReport1;
import com.afunms.system.model.User;
import com.afunms.topology.model.HostNode;
import com.afunms.topology.dao.DiscoverCompleteDao;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.util.*;
import com.afunms.common.util.DBManager;
import com.afunms.config.model.*;
import com.afunms.config.dao.*;
import com.afunms.event.dao.*;
import com.afunms.common.util.*;
import com.afunms.event.model.NetSyslogRule;
import com.afunms.event.model.Syslog;
import com.afunms.event.model.NetSyslog;

import java.text.SimpleDateFormat;
import java.util.*;

import com.afunms.polling.impl.IpResourceReport;
import com.afunms.polling.loader.HostLoader;

/**
 * 增加一个节点，要在server.jsp增加一个节点;
 * 删除或更新一个节点信息，在这则不直接操作server.jsp,而是让任务updateXml来更新xml.
 */
public class NetSyslogManager extends BaseManager implements ManagerInterface
{
	private String list()
	{
		int status =99;
		String priority = "99";
		int bid = 0;
		String ip = "";
		String b_time ="";
		String t_time = "";
		String content = "";
		String strclass = "-1";
		strclass = getParaValue("strclass");
		request.setAttribute("strclass", strclass);
		NetSyslogDao dao = new NetSyslogDao();
    	status = getParaIntValue("status");
    	priority = getParaValue("priority");
    	ip = getParaValue("ipaddress");
    	if(status == -1)status=99;
    	//if(priority == -1)priority=99;
    	request.setAttribute("status", status);
    	request.setAttribute("priority", priority);
    	
    	bid = getParaIntValue("businessid");
    	request.setAttribute("businessid", bid);
    	BusinessDao bdao = new BusinessDao();
    	List businesslist = bdao.loadAll();
    	request.setAttribute("businesslist", businesslist);
    	content = getParaValue("content");
    	if(content == null)content = "";
    	request.setAttribute("content", content);
    	
    	b_time = getParaValue("startdate");
		t_time = getParaValue("todate");
	
		if (b_time == null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			b_time = sdf.format(new Date());
		}
		if (t_time == null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			t_time = sdf.format(new Date());
		}
		String starttime1 = b_time + " 00:00:00";
		String totime1 = t_time + " 23:59:59";
		String sql="";
		try{
			User vo = (User)session.getAttribute(SessionConstant.CURRENT_USER);      //用户姓名
			StringBuffer s = new StringBuffer();
			if(!"-1".equals(strclass) && strclass != null && !"".equals(strclass) && !"null".equals(strclass)){
				if("1".equals(strclass)){
					s.append(" where category = 4 and recordtime>= '"+starttime1+"' " +"and recordtime<='"+totime1+"'");
				}else if("2".equals(strclass)){
					s.append(" where category <> 4 and recordtime>= '"+starttime1+"' " +"and recordtime<='"+totime1+"'");
				}
			}else{
				s.append("where recordtime>= '"+starttime1+"' " +"and recordtime<='"+totime1+"'");
			}
			if(!"-1".equals(ip) && ip != null){
				s.append(" and ipaddress = '"+ip+"'");
			}
			if(priority != null && !"null".equals(priority) && !"".equals(priority) && !"8,1,2,3,4,5,6,7".equals(priority)){
				if(priority.indexOf('8')!=-1){
					priority = priority.replace('8', '0');
				}
				s.append(" and priority in ("+priority+")");
			}
			String businessid = vo.getBusinessids();
			int flag = 0;
			if(bid > -1){
				s.append(" and businessid like '%,"+bid+",%'");
			}
			if(content != null && content.trim().length()>0){
				s.append(" and message like '%"+content+"%'");
			}
			sql = s.toString()+" order by id desc";
		}catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("startdate", b_time);
		request.setAttribute("todate", t_time);
		setTarget("/alarm/syslog/list.jsp");
        return list(dao,sql);
	}
	
	private String filterlist()
	{
		
		NetSyslogRuleDao ruledao = new NetSyslogRuleDao();
		List rulelist = ruledao.loadAll();
		if(rulelist != null && rulelist.size()>0){
			NetSyslogRule logrule = (NetSyslogRule)rulelist.get(0);
			String facility = logrule.getFacility();
			String[] facilitys = facility.split(",");
			List flist = new ArrayList();
			if(facilitys != null && facilitys.length>0){
				for(int i = 0;i<facilitys.length;i++){
					flist.add(facilitys[i]);
				}
			}
			request.setAttribute("facilitys", flist);
			
			String priority = logrule.getPriority();
			String[] prioritys = priority.split(",");
			List plist = new ArrayList();
			if(prioritys != null && prioritys.length>0){
				for(int i = 0;i<prioritys.length;i++){
					plist.add(prioritys[i]);
				}
			}
			request.setAttribute("prioritys", plist);
		}
        return "/alarm/syslog/filterlist.jsp";
	}
	
	private String monitornodelist()
	{
		HostNodeDao dao = new HostNodeDao();	 
		setTarget("/topology/network/monitornodelist.jsp");
        return list(dao," where managed=1");
	}
	
    private String read()
    {
	    DaoInterface dao = new HostNodeDao();
	    setTarget("/topology/network/read.jsp");
        return readyEdit(dao);
    }
    
    private String telnet()
    {
    	request.setAttribute("ipaddress",getParaValue("ipaddress"));
	    
        return "/tool/telnet.jsp";
    }
    
	private String readyEdit()
	{
	    DaoInterface dao = new HostNodeDao();
	    setTarget("/topology/network/edit.jsp");
        return readyEdit(dao);
	}
	private String readyEditAlias()
	{
	    DaoInterface dao = new HostNodeDao();
	    setTarget("/topology/network/editalias.jsp");
        return readyEdit(dao);
	}
	private String readyEditSysGroup()
	{
	    DaoInterface dao = new HostNodeDao();
	    setTarget("/topology/network/editsysgroup.jsp");
        return readyEdit(dao);
	}
	
	private String readyEditSnmp()
	{
	    DaoInterface dao = new HostNodeDao();
	    setTarget("/topology/network/editsnmp.jsp");
        return readyEdit(dao);
	}
	
	private String update()
	{         	  
		HostNode vo = new HostNode();
        vo.setId(getParaIntValue("id"));
        vo.setAlias(getParaValue("alias"));
        vo.setManaged(getParaIntValue("managed")==1?true:false);
        
        NodeToBusinessDao ntbdao = new NodeToBusinessDao();
        ntbdao.deleteallbyNE(vo.getId(), "equipment");
        ntbdao.close();
        String[] businessids = getParaArrayValue("checkbox");
        if(businessids != null && businessids.length>0){
        	for(int i=0;i<businessids.length;i++){
        		
        		String bid = businessids[i];
        		//SysLogger.info(bid+"====");
        		NodeToBusiness ntb = new NodeToBusiness();
        		ntb.setBusinessid(Integer.parseInt(bid));
        		ntb.setNodeid(vo.getId());
        		ntb.setElementtype("equipment");
        		ntbdao = new NodeToBusinessDao();
        		ntbdao.save(ntb);
        		ntbdao.close();
        	}
        }        
        //更新内存
 	    Host host = (Host)PollingEngine.getInstance().getNodeByID(vo.getId()); 	
 	    if(host != null){
 	    	host.setAlias(vo.getAlias());
 	    	host.setManaged(vo.isManaged());
 	    }else{
 	    	if(getParaIntValue("managed")==1){
 	    		HostNodeDao dao = new HostNodeDao();
 	    		HostNode hostnode = dao.loadHost(vo.getId());
 	    		hostnode.setAlias(getParaValue("alias"));
 	    		hostnode.setManaged(getParaIntValue("managed")==1?true:false);
 	    		HostLoader loader = new HostLoader();
 	    		loader.loadOne(hostnode);
 	    		//PollingEngine.getInstance().addNode(node)
 	    	}
 	    }
 	     	    
        //更新数据库
 	    DaoInterface dao = new HostNodeDao(); 	    
	    setTarget("/network.do?action=list");
        return update(dao,vo);
    }
	
	private String updatealias()
	{        
		HostNode vo = new HostNode();
        vo.setId(getParaIntValue("id"));
        vo.setAlias(getParaValue("alias"));
        vo.setManaged(getParaIntValue("managed")==1?true:false);

        
        //更新内存
 	    Host host = (Host)PollingEngine.getInstance().getNodeByID(vo.getId()); 	   
 	    host.setAlias(vo.getAlias());
 	    host.setManaged(vo.isManaged());
 	     	    
        //更新数据库
 	    DaoInterface dao = new HostNodeDao(); 	    
	    setTarget("/topology/network/networkview.jsp?id="+vo.getId()+"&ipaddress="+vo.getIpAddress());
        return update(dao,vo);
    }
	
	private String updatesysgroup()
	{         	  
		HostNode vo = new HostNode();
		HostNodeDao dao = new HostNodeDao();
		
		vo = dao.loadHost(getParaIntValue("id"));
        vo.setId(getParaIntValue("id"));
        vo.setSysName(getParaValue("sysname"));
        vo.setSysContact(getParaValue("syscontact"));
        vo.setSysLocation(getParaValue("syslocation"));
        
        Hashtable mibvalues = new Hashtable();
        mibvalues.put("sysContact", getParaValue("syscontact"));
        mibvalues.put("sysName", getParaValue("sysname"));
        mibvalues.put("sysLocation", getParaValue("syslocation"));
        
        //更新数据库
        dao.close();
        dao = new HostNodeDao();
 	    boolean flag = false;
 	    flag = dao.updatesysgroup(vo,mibvalues);
 	    if(flag){
 	    	//更新内存
 	    	Host host = (Host)PollingEngine.getInstance().getNodeByID(vo.getId()); 	  
 	    	host.setSysName(getParaValue("sysname"));
 	    	host.setSysContact(getParaValue("syscontact"));
 	    	host.setSysLocation(getParaValue("syslocation"));
 	    }	   
        return "/topology/network/networkview.jsp?id="+vo.getId()+"&ipaddress="+vo.getIpAddress();
    }
	
	private String updatesnmp()
	{         	  
		HostNode vo = new HostNode();
		HostNodeDao dao = new HostNodeDao();
		vo = dao.loadHost(getParaIntValue("id"));
        vo.setId(getParaIntValue("id"));
        vo.setCommunity(getParaValue("readcommunity"));
        vo.setWriteCommunity(getParaValue("writecommunity"));
        vo.setSnmpversion(getParaIntValue("snmpversion"));
        
        //更新数据库
        dao.close();
        dao = new HostNodeDao();
	    //更新内存
	    Host host = (Host)PollingEngine.getInstance().getNodeByID(vo.getId()); 	 
	    host.setCommunity(getParaValue("readcommunity"));
	    host.setWritecommunity(getParaValue("writecommunity"));
	    host.setSnmpversion(getParaIntValue("snmpversion"));
	    dao.updatesnmp(vo);
    	//setTarget("/monitor.do?action=netif&id="+vo.getId()+"&ipaddress="+vo.getIpAddress());
    	return "/topology/network/networkview.jsp?id="+vo.getId()+"&ipaddress="+vo.getIpAddress();
    }
	
	private String refreshsysname()
	{         	  
		HostNodeDao dao = new HostNodeDao();
		String sysName = "";
		sysName = dao.refreshSysName(getParaIntValue("id"));
 	    
        //更新内存
 	    Host host = (Host)PollingEngine.getInstance().getNodeByID(getParaIntValue("id")); 	   
 	    if(host != null){
 	    	host.setSysName(sysName);
 	    	host.setAlias(sysName);
 	    }

 	   return "/network.do?action=list";
    }
	
    private String delete()
    {
    	
    	String[] ids = getParaArrayValue("checkbox");
    	if(ids != null && ids.length > 0){
    		//进行修改
    		for(int i=0;i<ids.length;i++){
    		       //String id = getParaValue("radio"); 
    		        String id = ids[i]; 
    		        
    		      
  			
    		}
            
    	}
    	
        
        return "/network.do?action=list";
    }
    
    private String cancelmanage()
    {
    	String[] ids = getParaArrayValue("checkbox");
    	if(ids != null && ids.length > 0){
    		//进行修改
    		for(int i=0;i<ids.length;i++){
    			HostNodeDao dao = new HostNodeDao();
                HostNode host = (HostNode)dao.findByID(ids[i]);
                host.setManaged(false);
                dao = new HostNodeDao();
                dao.update(host);
                PollingEngine.getInstance().deleteNodeByID(Integer.parseInt(ids[i]));
    		}
            
    	}
        
        return "/network.do?action=monitornodelist";
    }

    private String add() 
    {  	 	  
	    String ipAddress = getParaValue("ip_address");
	    String alias = getParaValue("alias");
	    String community = getParaValue("community");
	    String writecommunity = getParaValue("writecommunity");
	    int type = getParaIntValue("type");
	    
	    TopoHelper helper = new TopoHelper(); //包括更新数据库和更新内存
	    int addResult = helper.addHost(ipAddress,alias,community,writecommunity,type); //加入一台服务器
	    if(addResult==0)
	    {	  
	        setErrorCode(ErrorMessage.ADD_HOST_FAILURE);
	        return null;      
	    }   
	    if(addResult==-1)
	    {	  
	        setErrorCode(ErrorMessage.IP_ADDRESS_EXIST);
	        return null;      
	    }   	    
	    if(addResult==-2)
	    {	  
	        setErrorCode(ErrorMessage.PING_FAILURE);
	        return null;      
	    }   	    
	    if(addResult==-3)
	    {	  
	        setErrorCode(ErrorMessage.SNMP_FAILURE);
	        return null;      
	    }      
	    
 	    //2.更新xml
	    XmlOperator opr = new XmlOperator();
	    opr.setFile("network.jsp");
	    opr.init4updateXml();
	    opr.addNode(helper.getHost());   
        opr.writeXml();
        
        return "/network.do?action=list";
    }  

    private String find()
    {
	   String key = getParaValue("key");
	   String value = getParaValue("value");	 
	   HostNodeDao dao = new HostNodeDao();
       request.setAttribute("list",dao.findByCondition(key,value));
     
       return "/topology/network/find.jsp";
   }

   private String save()
   {
       String[] fs = getParaArrayValue("fcheckbox");
       String faci_str="";
       if(fs != null && fs.length>0){
       		for(int i=0;i<fs.length;i++){
       		
       			String fa = fs[i];
       			faci_str=faci_str+fa+",";
       		}
       }
       String[] ps = getParaArrayValue("checkbox");
       String pri_str="";
       if(ps != null && ps.length>0){
       		for(int i=0;i<ps.length;i++){
       		
       			String pa = ps[i];
       			pri_str=pri_str+pa+",";
       		}
       }
       NetSyslogRuleDao ruledao = new NetSyslogRuleDao();
		List rulelist = ruledao.loadAll();
		NetSyslogRule rule = null;
		ruledao = new NetSyslogRuleDao();
		if(rulelist != null && rulelist.size()>0){
			rule = (NetSyslogRule)rulelist.get(0);
		}
		if(rule == null){
			rule = new NetSyslogRule();
			rule.setFacility(faci_str);
			rule.setPriority(pri_str);
			ruledao.save(rule);
		}else{
			rule.setFacility(faci_str);
			rule.setPriority(pri_str);
			ruledao.update(rule);
		}
	   
	   return "/netsyslog.do?action=filter&jp=1";   
   }
   
   private String savevlan()
   {
	   String xmlString = request.getParameter("hidXml");			
	   xmlString = xmlString.replace("<?xml version=\"1.0\"?>", "<?xml version=\"1.0\" encoding=\"GB2312\"?>");		
	   XmlOperator xmlOpr = new XmlOperator();
	   xmlOpr.setFile("networkvlan.jsp");
	   xmlOpr.saveImage(xmlString);
	   
	   return "/topology/network/save.jsp";   
   }
   
   private String netsyslogdetail()
   {
		Hashtable imgurlhash=new Hashtable();
		Hashtable hash = new Hashtable();//"Cpu"--current
		Hashtable userhash = new Hashtable();//"user"--current
		Hashtable maxhash = new Hashtable();//"Cpu"--max
		List list = new ArrayList();
		try{
			//String ip=getParaValue("ipaddress");
			int id = getParaIntValue("id");
			NetSyslog syslog = new NetSyslog();
			NetSyslogDao dao = new NetSyslogDao();
			syslog = (NetSyslog)dao.findByID(id+"");							
			request.setAttribute("syslog", syslog);	
		}catch(Exception e){
			e.printStackTrace();
		}
		return "/alarm/syslog/net_syslogdetail.jsp";
	    //return "/detail/host_syslogdetail.jsp";
   }
    
   public String execute(String action)
   {
	  if(action.equals("list"))
	     return list();
	  if(action.equals("filter"))
		     return filterlist();
	  if(action.equals("netsyslogdetail"))
		 return netsyslogdetail(); 
      if(action.equals("read"))
         return read();      
	  if(action.equals("ready_edit"))
         return readyEdit();
	  if(action.equals("ready_editalias"))
		  return readyEditAlias();
	  if(action.equals("ready_editsysgroup"))
		  return readyEditSysGroup();
	  if(action.equals("ready_editsnmp"))
	         return readyEditSnmp();
      if(action.equals("update"))
         return update(); 
      if(action.equals("cancelmanage"))
          return cancelmanage(); 
      if(action.equals("updatealias"))
          return updatealias();
      if(action.equals("updatesysgroup"))
          return updatesysgroup();
      if(action.equals("updatesnmp"))
          return updatesnmp();
      if(action.equals("refreshsysname"))
          return refreshsysname();
	  if(action.equals("delete"))
	     return delete();     
      if(action.equals("find"))
         return find();           
	  if(action.equals("ready_add"))
	     return "/topology/network/add.jsp";
      if(action.equals("add"))
         return add();
      if(action.equals("telnet"))
          return telnet(); 
      if(action.equals("save"))
         return save();
      if(action.equals("downloadsyslogreport"))
          return downloadsyslogreport();
      if(action.equals("downloadsyslogreportall"))
          return downloadsyslogreportall();
      setErrorCode(ErrorMessage.ACTION_NO_FOUND);
      return null;
   }
   
   public String downloadsyslogreport()
	{
		//Hashtable allcpuhash = new Hashtable();
		List list = (List) session.getAttribute("list");
		int startRow = ((Integer)session.getAttribute("startRow")).intValue();
		Hashtable reporthash = new Hashtable();
		if (list!=null) {
			reporthash.put("list", list);
		}
		else {
			list = new ArrayList();
		}
		reporthash.put("startRow", startRow);
		AbstractionReport1 report = new ExcelReport1(new IpResourceReport(),
				reporthash);
		
		report.createReport_syslog("/temp/syslog_report.xls");
		request.setAttribute("filename", report.getFileName());
		return "/alarm/syslog/download.jsp";
	}
   public String downloadsyslogreportall()
	{
		//Hashtable allcpuhash = new Hashtable();
	   NetSyslogDao netSyslogDao = new NetSyslogDao();
		List list = netSyslogDao.loadAll();
		//int startRow = ((Integer)session.getAttribute("startRow")).intValue();
		Hashtable reporthash = new Hashtable();
		if (list!=null) {
			reporthash.put("list", list);
		}
		else {
			list = new ArrayList();
		}
		//reporthash.put("startRow", startRow);
		AbstractionReport1 report = new ExcelReport1(new IpResourceReport(),
				reporthash);
		
		report.createReport_syslogall("/temp/syslogall_report.xls");
		request.setAttribute("filename", report.getFileName());
		return "/alarm/syslog/download.jsp";
	}
   
}
