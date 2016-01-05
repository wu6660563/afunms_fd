package com.afunms.topology.manage;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.afunms.application.dao.DBDao;
import com.afunms.application.dao.Emailmonitor_historyDao;
import com.afunms.application.dao.Ftpmonitor_historyDao;
import com.afunms.application.dao.JBossmonitor_historyDao;
import com.afunms.application.dao.TemperatureHumidityDao;
import com.afunms.application.dao.Urlmonitor_historyDao;
import com.afunms.application.manage.PortServiceTypeManager;
import com.afunms.application.manage.TemperatureHumidityManager;
import com.afunms.application.manage.TomcatManager;
import com.afunms.application.model.DBVo;
import com.afunms.application.model.DominoDisk;
import com.afunms.application.model.SybaseVO;
import com.afunms.application.model.TablesVO;
import com.afunms.application.model.TemperatureHumidityConfig;
import com.afunms.application.util.IpTranslation;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.CreateTableManager;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.config.dao.BusinessDao;
import com.afunms.config.model.Business;
import com.afunms.detail.reomte.model.DiskInfo;
import com.afunms.detail.service.diskInfo.DiskInfoService;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.model.EventList;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.api.I_HostCollectData;
import com.afunms.polling.api.I_HostCollectDataDay;
import com.afunms.polling.api.I_HostLastCollectData;
import com.afunms.polling.base.Node;
import com.afunms.polling.impl.HostCollectDataDayManager;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.polling.loader.HostLoader;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.polling.om.Diskcollectdata;
import com.afunms.polling.om.Flashcollectdata;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.Memorycollectdata;
import com.afunms.system.model.User;
import com.afunms.system.vo.AlarmVo;
import com.afunms.system.vo.CombVo;
import com.afunms.system.vo.Db2Vo;
import com.afunms.system.vo.EventVo;
import com.afunms.system.vo.FlexVo;
import com.afunms.system.vo.FluxVo;
import com.afunms.system.vo.InformixVo;
import com.afunms.system.vo.InformxVo;
import com.afunms.system.vo.LinkFluxVo;
import com.afunms.system.vo.MemoryVo;
import com.afunms.system.vo.Vo;
import com.afunms.system.vo.Vo3;
import com.afunms.system.vo.Vos;
import com.afunms.system.vo.oraVo;
import com.afunms.temp.dao.OthersTempDao;
import com.afunms.topology.dao.DiscoverCompleteDao;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.dao.IpMacBaseDao;
import com.afunms.topology.model.HostNode;
import com.afunms.topology.util.NodeHelper;
import com.afunms.topology.util.XmlOperator;

import flex.messaging.FlexContext;
import flex.messaging.FlexSession;

public class ServiceForFlex extends BaseManager implements ManagerInterface{
	
	public ServiceForFlex(){ 
		
	}

	public FlexSession session;
	public List<Host> getNodeListByPerms(){
		session = FlexContext.getFlexSession();
		User current_user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
		HostNodeDao hostNodeDao =new HostNodeDao();
		
		List list = new ArrayList();
		if(current_user.getRole() == 0){
			list = hostNodeDao.loadByPerAll("-1");
		}else 
			list = hostNodeDao.loadByPerAll(current_user.getBusinessids());
		return list;
	}
	
	public List<Host> getNodeList(){
		List alist = new ArrayList();
		session = FlexContext.getFlexSession();
		User current_user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
		String bids[] = current_user.getBusinessids().split(",");
		List list = PollingEngine.getInstance().getNodeList();
		HostNodeDao hostNodeDao =new HostNodeDao();
		for(int i=0;i<list.size();i++){
			Host host=(Host)list.get(i);
			HostNode node = (HostNode) hostNodeDao.findByID(host.getId()+"");
			
			if(current_user.getRole() == 0){
				String alarmmessage="";
				if(host.isAlarm()){
					List alarmList = host.getAlarmMessage();
					if (alarmList != null && alarmList.size() > 0) {
						for (int k = 0; k < alarmList.size(); k++) {
							alarmmessage = alarmmessage + alarmList.get(k) + "<br>";
						}
					}
					host.setShowmessage(host.getShowMessage()+"<br>"+alarmmessage);
				} else {
					host.setShowmessage(host.getShowMessage());
				}
				alist.add(host);
			}else{
				int tag = 0;
				if(bids!=null&&bids.length>0){
				    for(int j=0;j<bids.length;j++){
				        if(node.getBid()!=null&&!"".equals(node.getBid())&&!"".equals(bids[j])&&node.getBid().indexOf(bids[j])!=-1){
				            tag++;
				        }
				    }
				}
				if(tag>0){
					String alarmmessage="";
					if(host.isAlarm()){
						List alarmList = host.getAlarmMessage();
						if (alarmList != null && alarmList.size() > 0) {
							for (int k = 0; k < alarmList.size(); k++) {
								alarmmessage = alarmmessage + alarmList.get(k) + "<br>";
							}
						}
						host.setShowmessage(host.getShowMessage()+"<br>"+alarmmessage);
					} else {
						host.setShowmessage(host.getShowMessage());
					}
					alist.add(host);
				}
			}
			

		}
		hostNodeDao.close();
		return alist;
	}
	
	public String menuaddendpoint(String id)
    {
    	try{
    			HostNodeDao dao = new HostNodeDao();
                HostNode host = (HostNode)dao.findByID(id);
                host.setEndpoint(1);
                dao = new HostNodeDao();
                dao.update(host);
                //�����ڴ�
         	    Host _host = (Host)PollingEngine.getInstance().getNodeByID(host.getId()); 	   
         	    if(_host != null){
         	    	_host.setEndpoint(0);
         	    }
    	}catch(Exception e){
    		e.printStackTrace();
    	}
        
        return "sucess";
    }
	
	public String menucancelendpoint(String id)
    {
    	try{
    			HostNodeDao dao = new HostNodeDao();
                HostNode host = (HostNode)dao.findByID(id);
                host.setEndpoint(0);
                dao = new HostNodeDao();
                dao.update(host);
                //PollingEngine.getInstance().deleteNodeByID(Integer.parseInt(id));
                //�����ڴ�
         	    Host _host = (Host)PollingEngine.getInstance().getNodeByID(host.getId()); 	   
         	    if(_host != null){
         	    	_host.setEndpoint(0);
         	    }
    	}catch(Exception e){
    		e.printStackTrace();
    	}
        
        return "sucess";
    }
	
	public String menucancelmanage(String id)// yangjun add flex
	{
		try {
			HostNodeDao dao = new HostNodeDao();
			HostNode host = (HostNode) dao.findByID(id);
			host.setManaged(false);
			dao = new HostNodeDao();
			dao.update(host);
			PollingEngine.getInstance().deleteNodeByID(Integer.parseInt(id));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "sucess";
	}
	
	public String menuaddmanage(String id)//yangjun add flex
    {
    	try{
    			HostNodeDao dao = new HostNodeDao();
                HostNode host = (HostNode)dao.findByID(id);
                host.setManaged(true);
                dao = new HostNodeDao();
                dao.update(host);
                HostLoader hl = new HostLoader();
                hl.loadOne(host);
                //PollingEngine.getInstance().deleteNodeByID(Integer.parseInt(id));
                //PollingEngine.getInstance().addNode(node)(Integer.parseInt(id));
    	}catch(Exception e){
    		e.printStackTrace();
    	}
        
        return "sucess";
    }

	public String Delete(String id)// yangjun add flex
	{
		PollingEngine.getInstance().deleteNodeByID(Integer.parseInt(id));
		HostNodeDao dao = new HostNodeDao();
		HostNode host = (HostNode) dao.findByID(id);
		dao.delete(id);

		String ip = host.getIpAddress();
//		String ip1 = "", ip2 = "", ip3 = "", ip4 = "";
//		//String[] ipdot = ip.split(".");
//		String tempStr = "";
//		String allipstr = "";
//		if (ip.indexOf(".") > 0) {
//			ip1 = ip.substring(0, ip.indexOf("."));
//			ip4 = ip.substring(ip.lastIndexOf(".") + 1, ip.length());
//			tempStr = ip.substring(ip.indexOf(".") + 1, ip.lastIndexOf("."));
//		}
//		ip2 = tempStr.substring(0, tempStr.indexOf("."));
//		ip3 = tempStr.substring(tempStr.indexOf(".") + 1, tempStr.length());
//		allipstr = ip1 + ip2 + ip3 + ip4;
		String allipstr = SysUtil.doip(ip);

		CreateTableManager ctable = new CreateTableManager();
		DBManager conn = new DBManager();
		try {
			if (host.getCategory() < 4 || host.getCategory() == 7) {
				// ��ɾ�������豸��
				ctable.deleteTable(conn, "ping", allipstr, "ping");// Ping
				ctable.deleteTable(conn, "pinghour", allipstr, "pinghour");// Ping
				ctable.deleteTable(conn, "pingday", allipstr, "pingday");// Ping

				ctable.deleteTable(conn, "memory", allipstr, "mem");// �ڴ�
				ctable.deleteTable(conn, "memoryhour", allipstr, "memhour");// �ڴ�
				ctable.deleteTable(conn, "memoryday", allipstr, "memday");// �ڴ�

				ctable.deleteTable(conn, "cpu", allipstr, "cpu");// CPU
				ctable.deleteTable(conn, "cpuhour", allipstr, "cpuhour");// CPU
				ctable.deleteTable(conn, "cpuday", allipstr, "cpuday");// CPU

				ctable.deleteTable(conn, "utilhdxperc", allipstr, "hdperc");
				ctable.deleteTable(conn, "utilhdx", allipstr, "hdx");
				ctable.deleteTable(conn, "hdxperchour", allipstr, "hdperchour");
				ctable.deleteTable(conn, "hdxpercday", allipstr, "hdpercday");
				ctable.deleteTable(conn, "utilhdxhour", allipstr, "hdxhour");
				ctable.deleteTable(conn, "utilhdxday", allipstr, "hdxday");

				ctable.deleteTable(conn, "discardsperc", allipstr, "dcardperc");
				ctable.deleteTable(conn, "dcardperchour", allipstr,
						"dcardperchour");
				ctable.deleteTable(conn, "dcardpercday", allipstr,
						"dcardpercday");

				ctable.deleteTable(conn, "errorsperc", allipstr, "errperc");
				ctable
						.deleteTable(conn, "errperchour", allipstr,
								"errperchour");
				ctable.deleteTable(conn, "errpercday", allipstr, "errpercday");

				ctable.deleteTable(conn, "packs", allipstr, "packs");
				ctable.deleteTable(conn, "packshour", allipstr, "packshour");
				ctable.deleteTable(conn, "packsday", allipstr, "packsday");
				ctable.deleteTable(conn, "inpacks", allipstr, "inpacks");
				ctable
						.deleteTable(conn, "inpackshour", allipstr,
								"inpackshour");
				ctable.deleteTable(conn, "inpacksday", allipstr, "inpacksday");
				ctable.deleteTable(conn, "outpacks", allipstr, "outpacks");
				ctable.deleteTable(conn, "outpackshour", allipstr,
						"outpackshour");
				ctable
						.deleteTable(conn, "outpacksday", allipstr,
								"outpacksday");
				ctable.deleteTable(conn, "temper", allipstr, "temper");
				ctable.deleteTable(conn, "temperhour", allipstr, "temperhour");
				ctable.deleteTable(conn, "temperday", allipstr, "temperday");

				DiscoverCompleteDao dcDao = new DiscoverCompleteDao();
				dcDao.deleteMonitor(host.getId(), host.getIpAddress());
				conn.close();
				try {
					// ͬʱɾ���¼�������������
					EventListDao eventdao = new EventListDao();
					eventdao.delete(host.getId(), "network");
				} catch (Exception e) {
					e.printStackTrace();
				}
				// ɾ��IP-MAC-BASE����Ķ�Ӧ������
				IpMacBaseDao macbasedao = new IpMacBaseDao();
				try {
					// delte��,conn�Ѿ��ر�
					macbasedao.deleteByHostIp(host.getIpAddress());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (host.getCategory() == 4) {
				// ɾ������������
				ctable.deleteTable(conn, "pro", allipstr, "pro");// ����
				ctable.deleteTable(conn, "prohour", allipstr, "prohour");// ����Сʱ
				ctable.deleteTable(conn, "proday", allipstr, "proday");// ������

				ctable.deleteTable(conn, "log", allipstr, "log");// ������

				ctable.deleteTable(conn, "memory", allipstr, "mem");// �ڴ�
				ctable.deleteTable(conn, "memoryhour", allipstr, "memhour");// �ڴ�
				ctable.deleteTable(conn, "memoryday", allipstr, "memday");// �ڴ�

				ctable.deleteTable(conn, "cpu", allipstr, "cpu");// CPU
				ctable.deleteTable(conn, "cpuhour", allipstr, "cpuhour");// CPU
				ctable.deleteTable(conn, "cpuday", allipstr, "cpuday");// CPU
				/*
				 * ctable.createTable("disk",allipstr,"disk");
				 * ctable.createTable("diskhour",allipstr,"diskhour");
				 * ctable.createTable("diskday",allipstr,"diskday");
				 */
				ctable.deleteTable(conn, "ping", allipstr, "ping");
				ctable.deleteTable(conn, "pinghour", allipstr, "pinghour");
				ctable.deleteTable(conn, "pingday", allipstr, "pingday");

				ctable.deleteTable(conn, "utilhdxperc", allipstr, "hdperc");
				ctable.deleteTable(conn, "utilhdx", allipstr, "hdx");
				ctable.deleteTable(conn, "hdxperchour", allipstr, "hdperchour");
				ctable.deleteTable(conn, "hdxpercday", allipstr, "hdpercday");
				ctable.deleteTable(conn, "utilhdxhour", allipstr, "hdxhour");
				ctable.deleteTable(conn, "utilhdxday", allipstr, "hdxday");

				try {
					// ͬʱɾ���¼�������������
					EventListDao eventdao = new EventListDao();
					eventdao.delete(host.getId(), "host");
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			conn.close();
		}
		// 2.����xml
		if (host.getCategory() < 4) {
			// �����豸
			XmlOperator opr = new XmlOperator();
			opr.setFile("network.jsp");
			opr.init4updateXml();
			opr.deleteNodeByID(host.getId() + "");
			// opr.addNode(helper.getHost());
			opr.writeXml();
		} else if (host.getCategory() == 4) {
			// ����������
			XmlOperator opr = new XmlOperator();
			opr.setFile("server.jsp");
			opr.init4updateXml();
			opr.deleteNodeByID(host.getId() + "");
			// opr.addNode(helper.getHost());
			opr.writeXml();
		}
		return "success";
	}
	
	/*
	 * ��ȡ�����豸��ָ��ʱ��ε��ڴ������� 20100531(δ��)
	 * */
	public ArrayList<FlexVo> getNetMemoryByDate_(String ipAddress,String dateStr,String time){
		I_HostCollectData hostManager = new HostCollectDataManager();
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		String date[] = null;
		String startTime = "";
		String endTime = "";
		if("".equals(dateStr)){
			startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
			endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		} else {
			date = dateStr.split("=");
			startTime = date[0] + " 00:00:00";
			endTime = date[1] + " 23:59:59";
		}
		DecimalFormat df=new DecimalFormat("#.##");
//		if(java.sql.Date.valueOf(startTime).after(java.sql.Date.valueOf(endTime))){ 
//			System.out.println("��ʼ���ڴ��ڽ�ֹ����!");
//	    } else {
//	    	��ȡ�ڴ���������ʷ����ƽ��ֵ
			Hashtable virtualHash = new Hashtable();
			try {
				virtualHash = hostManager.getMemory(ipAddress, "Utilization", startTime, endTime, time);
			} catch (Exception e) {
				e.printStackTrace();
			}
			List virtualList = new ArrayList();
			virtualList = (List) virtualHash.get("list");
			if(virtualList!=null&&virtualList.size()>0){
				for(int i=0;i<virtualList.size();i++){
					FlexVo mVo = new FlexVo();
					Vector virtualVector = new Vector();
					virtualVector = (Vector) virtualList.get(i);
					if(virtualVector!=null){
						mVo.setObjectName((String) virtualVector.get(1));
						mVo.setObjectNumber(df.format(Double.parseDouble((String) virtualVector.get(0))));
						flexDataList.add(mVo);
					}
				}
			}
//	    }
		return flexDataList;
	}
	/*
	 * ��ȡһ̨�豸��ָ��ʱ���ƽ���ڴ������� 20100531
	 * */
	public String getAvgNetMemoryByDate(String ipAddress,String dateStr){
		I_HostCollectData hostManager = new HostCollectDataManager();
		String date[] = dateStr.split("=");
		String startTime = date[0] + " 00:00:00";
		String endTime = date[1] + " 23:59:59";
		String virtualAvgutil = "";
		String pAvgutil = "";
		String pMaxutil = "";
		String pMinutil = "";
		String pTemputil = "";
//		��Ҫ���ֲ�ʽ�ж�
		String runmodel = PollingEngine.getCollectwebflag(); 
		DecimalFormat df=new DecimalFormat("#.##");
		if(java.sql.Date.valueOf(date[0]).after(java.sql.Date.valueOf(date[1]))){ 
			System.out.println("��ʼ���ڴ��ڽ�ֹ����!");
	    } else {
	    	Hashtable ipAllData = new Hashtable();
	    	if("0".equals(runmodel)){
				//�ɼ�������Ǽ���ģʽ 
				ipAllData = (Hashtable)ShareData.getSharedata().get(ipAddress);
			}else{
				//�ɼ�������Ƿ���ģʽ 
				ipAllData = (Hashtable)ShareData.getAllNetworkData().get(ipAddress);
			}
			String name = "";
			if (ipAllData != null) {
				Vector memoryVector = (Vector) ipAllData.get("memory");
				if (memoryVector != null && memoryVector.size() > 0) {
					for (int si = 0; si < memoryVector.size(); si++) {
						Memorycollectdata memodata = (Memorycollectdata) memoryVector.elementAt(si);
						if(memodata!=null){
							if (memodata.getEntity().equalsIgnoreCase("Utilization")) {
								name = name + memodata.getSubentity() + ";";
							}
						}
					}
				}
			}
			String arrName[] = name.split(";");
	    	Hashtable virtualHash[] = new Hashtable[arrName.length];
	    	if(arrName!=null&&arrName.length>0){
	    		for(int i=0;i<arrName.length;i++){
					try {
				        virtualHash[i] = hostManager.getMemory(ipAddress, arrName[i], startTime, endTime, "");
					} catch (Exception e) {
						e.printStackTrace();
					}
					pAvgutil = df.format(Double.parseDouble((String) virtualHash[i].get("avgput")));
					pMaxutil = df.format(Double.parseDouble((String) virtualHash[i].get("max")));
					pMinutil = df.format(Double.parseDouble((String) virtualHash[i].get("min")));
					pTemputil = df.format(Double.parseDouble((String) virtualHash[i].get("temp")));
					virtualAvgutil = virtualAvgutil + "�ڴ�"+ arrName[i]+":"+pTemputil+"%:"+pMaxutil+"%:"+pMinutil+"%:"+pAvgutil+"%";
					if(i!=arrName.length-1){
						virtualAvgutil = virtualAvgutil + ",";
					}
		    	}
	    	}
	    }
	    return virtualAvgutil;
	} 
//	/*
//	 * ��ȡһ̨�豸��ȥʮ����ÿ��CPU�����ʵ�ƽ��ֵ
//	 * */
//	public ArrayList<FlexVo> getCPUByWeek(String ipAddress){
//		I_HostCollectData hostManager = new HostCollectDataManager();
//		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
//		for(int j=9;j>=0;j--){
//			Calendar curDate_s = Calendar.getInstance();
//	        curDate_s.add(Calendar.DAY_OF_MONTH, -j);
//	        String date = new SimpleDateFormat("yyyy-MM-dd").format(curDate_s.getTime());
//	        String starttime = date + " 00:00:00";
//	        String endTime = date + " 23:59:59";
//	        String avgcpucon = "0%";
//	        Hashtable cpuHash = new Hashtable();
//	        try {
//				cpuHash = hostManager.getCategory(ipAddress, "CPU", "Utilization", starttime, endTime);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			if(cpuHash.get("avgcpucon")!=null){
//				avgcpucon = (String) cpuHash.get("avgcpucon");
//			}
//			//System.out.println(date+":"+avgcpucon);
//			FlexVo fVo = new FlexVo();
//			fVo.setObjectName(date);
//			fVo.setObjectNumber(avgcpucon.replace("%", ""));
//			//SysLogger.info(fVo.getObjectName()+"==8888=="+fVo.getObjectNumber());
//			flexDataList.add(fVo);
//		}
//		
//		
//		return flexDataList;
//		
//	}
	/**
	 * 
	 */
	public ArrayList<FlexVo> getCPUByWeek(String ipAddress ){ 
		//��ȡ10�������
		int  num=9; 
		SysLogger.info("ooooooooooooooo"+ipAddress);
		return getCPUAvgByNum(ipAddress,num);
	}
	/**
	 * ����IP �ʹ��������num
	 * ͳ�� ��num��ǰ�������ÿ��CPUƽ��������
	 * @param ipAddress
	 * @param num
	 * @return
	 * @author makewen
	 * @date   Apr 25, 2011
	 */
	public  ArrayList<FlexVo> getCPUAvgByNum(String ipAddress,int num ){
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		HostCollectDataManager hostManager = new HostCollectDataManager();
		try{ 
			flexDataList=hostManager.getCPUDataAvgArray(ipAddress,num);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return flexDataList;
	} 
	/**
	 * ��ȡ10�������
	 * @param ipAddress
	 * @return
	 */
	public ArrayList<FlexVo> getDomCPUByTenDay(String ipAddress ){ 
		//��ȡ10�������
		int  num=9; 
		return getDomCPUAvg(ipAddress,num);
	}
	//domino
	public  ArrayList<FlexVo> getDomCPUAvg(String ipAddress,int num ){
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		HostCollectDataManager hostManager = new HostCollectDataManager();
		try{ 
			ipAddress=ipAddress.replace('.', '_');
 	        String table="dominocpu"+ipAddress; 
			flexDataList=hostManager.getCPUDataAvg(table,num);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return flexDataList;
	} 
	/*
	 * ��ȡһ̨�豸��ָ��ʱ���CPU�����ʵ�ƽ��ֵ
	 * */
	public ArrayList<FlexVo> getCPUByDate(String ipAddress,String dateStr){
		I_HostCollectData hostManager = new HostCollectDataManager();
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		String datestr[] = dateStr.split("=");
		if(datestr!=null&&datestr.length==2){
			if(java.sql.Date.valueOf(datestr[0]).after(java.sql.Date.valueOf(datestr[1]))){ 
				System.out.println("��ʼ���ڴ��ڽ�ֹ����!");
		    } else {
		    	String date = datestr[0];
		    	SimpleDateFormat   dateFormat=new   SimpleDateFormat("yyyy-MM-dd");   
		    	Date a = null;
				Date b = null;
				Date currentDate = null;
				try {
					a = dateFormat.parse(datestr[0]);     
			        b = dateFormat.parse(datestr[1]);
				} catch (ParseException e) {
					e.printStackTrace();
				}   
                int days=(int) ((b.getTime()-a.getTime())/86400000);
                for(int j=0;j<=days;j++){
                	String starttime = date + " 00:00:00";
			        String endTime = date + " 23:59:59";
			        String avgcpucon = "0%";
			        Hashtable cpuHash = new Hashtable();
			        try {
						cpuHash = hostManager.getCategory(ipAddress, "CPU", "Utilization", starttime, endTime);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(cpuHash.get("avgcpucon")!=null){
						avgcpucon = (String) cpuHash.get("avgcpucon");
					}
					//System.out.println(date+":"+avgcpucon);
					FlexVo fVo = new FlexVo();
					fVo.setObjectName(date);
					fVo.setObjectNumber(avgcpucon.replace("%", ""));
					//SysLogger.info(fVo.getObjectName()+"===###===="+fVo.getObjectNumber());
					flexDataList.add(fVo);
					try {
						currentDate = dateFormat.parse(date);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					b = new Date(currentDate.getTime() + 86400000);
					date = dateFormat.format(b);
        		}
		    }
		}
		return flexDataList;
		
	}
	
	/**
	 * ��ȡ��Ӧip��ַ��cpu�ڵ������ʱ��ε�ֵ(flex����)
	 * 
	 * @Date 2009-2-9
	 */
	public ArrayList<Vos> getCPUByDay(String date,String ipAddress) {
		I_HostCollectData hostManager = new HostCollectDataManager();
		String startTime = date + " 00:00:00";
		String endTime = date + " 23:59:59";;
		// ��collectdataȡcpu����ʷ����,����ڱ���
		Hashtable cpuHash = new Hashtable();
		try {
			cpuHash = hostManager.getCategory(ipAddress, "CPU", "Utilization", startTime, endTime);
		} catch (Exception e) {
			e.printStackTrace();
		}

		List cpuList = new ArrayList();
		if (cpuHash.get("list") != null) {
			ArrayList<Vos> flexDataList = new ArrayList<Vos>();
			cpuList = (ArrayList) cpuHash.get("list");
			Vos fVo;
			for (int i = 0; i < cpuList.size(); i++) {
				Vector cpuVector = new Vector();
				fVo = new Vos();
				cpuVector = (Vector) cpuList.get(i);
				if (cpuVector != null || cpuVector.size() > 0) {
					fVo.setObjectNumber((String) cpuVector.get(0));
					fVo.setObjectName2((String) cpuVector.get(1));
					fVo.setObjectName1(ipAddress);
					//SysLogger.info(fVo.getObjectName1()+"===="+fVo.getObjectNumber());
					flexDataList.add(fVo);
				}
			}
			return flexDataList;
		}
		return null;
	}	
	/*
	 * ��ȡһ̨�豸�����и�ʱ��ε��ۺ�����ֵ
	 * */
	public ArrayList<FluxVo> getFluxByDay(String ipAddress){
//		System.out.println("==================="+ipAddress);
		I_HostCollectData hostManager = new HostCollectDataManager();
		ArrayList<FluxVo> flexDataList = new ArrayList<FluxVo>();
		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //��ȡ������ʷ����ƽ��ֵ
		Hashtable inutilHash = new Hashtable();
		try {
			inutilHash = hostManager.getAllutilhdx(ipAddress, "AllInBandwidthUtilHdx", startTime, endTime, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Hashtable oututilHash = new Hashtable();
		try {
			oututilHash = hostManager.getAllutilhdx(ipAddress, "AllOutBandwidthUtilHdx", startTime, endTime, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		List inutilList = new ArrayList();
		inutilList = (List) inutilHash.get("list");
		List oututilList = new ArrayList();
		oututilList = (List) oututilHash.get("list");
//		if(inutilList!=null&&inutilList.size()>0&&oututilList!=null&&oututilList.size()>0){
//			int num = (inutilList.size()>oututilList.size()?oututilList.size():inutilList.size());
//			for(int i=0;i<num;i++){
//				FluxVo fVo = new FluxVo();
//				Vector inHdxVector = new Vector();
//				inHdxVector = (Vector) inutilList.get(i);
//				Vector outHdxVector = new Vector();
//				outHdxVector = (Vector) oututilList.get(i);
//				if(inHdxVector!=null&&outHdxVector!=null){
//					fVo.setDate((String) inHdxVector.get(1));
//					fVo.setFluxin((String) inHdxVector.get(0));
//					fVo.setFluxout((String) outHdxVector.get(0));
////					System.out.println("==================="+(String) inHdxVector.get(1));
////					System.out.println("==================="+(String) inHdxVector.get(0));
////					System.out.println("==================="+(String) outHdxVector.get(0));
//					flexDataList.add(fVo);
//				}
//			}
//		}
		if(inutilList!=null||oututilList!=null){
			if(inutilList.size()>oututilList.size()){
				int num_min = oututilList.size();
				int num_max = inutilList.size();
				if(num_min==0){
					for(int i=0;i<num_max;i++){
						FluxVo fVo = new FluxVo();
						Vector inHdxVector = new Vector();
						inHdxVector = (Vector) inutilList.get(i);
						if(inHdxVector!=null){
							fVo.setDate((String) inHdxVector.get(1));
							fVo.setFluxin((String) inHdxVector.get(0));
							fVo.setFluxout("0");
							flexDataList.add(fVo);
						}
					}
				} else {
					for(int i=0;i<num_max;i++){
						FluxVo fVo = new FluxVo();
						Vector inHdxVector = new Vector();
						inHdxVector = (Vector) inutilList.get(i);
						String time0 = "";
						String fluxout = "";
						if(inHdxVector!=null){
							time0 = (String) inHdxVector.get(1);
							fVo.setDate(time0);
							fVo.setFluxin((String) inHdxVector.get(0));
						}
						for(int j = 0; j < num_min; j++){
							Vector outHdxVector = new Vector();
							outHdxVector = (Vector) oututilList.get(j);
							if(outHdxVector!=null){
								String time1 = (String) outHdxVector.get(1);
								if(time1.equals(time0)){
									fluxout = (String) outHdxVector.get(0);
									break;
								}
							}
						}
						if(!"".equals(fluxout)&&fluxout.indexOf("-")==-1){
							fVo.setFluxout(fluxout);
						} else {
							fVo.setFluxout("0");
						}
						flexDataList.add(fVo);
					}
				}
			} else if(inutilList.size()<oututilList.size()) {
				int num_max = oututilList.size();
				int num_min = inutilList.size();
				if(num_min==0){
					for(int i=0;i<num_max;i++){
						FluxVo fVo = new FluxVo();
						Vector outHdxVector = new Vector();
						outHdxVector = (Vector) oututilList.get(i);
						if(outHdxVector!=null){
							fVo.setDate((String) outHdxVector.get(1));
							fVo.setFluxin("0");
							fVo.setFluxout((String) outHdxVector.get(0));
							flexDataList.add(fVo);
						}
					}
				} else {
					for(int i=0;i<num_max;i++){
						FluxVo fVo = new FluxVo();
						Vector outHdxVector = new Vector();
						outHdxVector = (Vector) oututilList.get(i);
						String time0 = "";
						String fluxin = "";
						if(outHdxVector!=null){
							time0 = (String) outHdxVector.get(1);
							fVo.setDate(time0);
							fVo.setFluxout((String) outHdxVector.get(0));
						}
						for(int j = 0; j < num_min; j++){
							Vector inHdxVector = new Vector();
							inHdxVector = (Vector) inutilList.get(j);
							if(inHdxVector!=null){
								String time1 = (String) inHdxVector.get(1);
								if(time1.equals(time0)){
									fluxin = (String) inHdxVector.get(0);
									break;
								}
							}
						}
						if(!"".equals(fluxin)&&fluxin.indexOf("-")==-1){
							fVo.setFluxin(fluxin);
						} else {
							fVo.setFluxin("0");
						}
						flexDataList.add(fVo);
					}
				}
			} else {
				int num = (inutilList.size()>oututilList.size()?oututilList.size():inutilList.size());
				for(int i=0;i<num;i++){
					FluxVo fVo = new FluxVo();
					Vector inHdxVector = new Vector();
					inHdxVector = (Vector) inutilList.get(i);
					Vector outHdxVector = new Vector();
					outHdxVector = (Vector) oututilList.get(i);
					if(inHdxVector!=null&&outHdxVector!=null){
						fVo.setDate((String) inHdxVector.get(1));
						fVo.setFluxin((String) inHdxVector.get(0));
						fVo.setFluxout((String) outHdxVector.get(0));
						flexDataList.add(fVo);
					}
				}
			}
		}
//		System.out.println("==================="+flexDataList.size());
		return flexDataList;
		
	}
	
	/*
	 * ��ȡһ̨�豸��ȥʮ����ۺ�����ֵ
	 * */
	public ArrayList<FluxVo> getFluxByDays(String ipAddress,String time){
//		System.out.println("==================="+ipAddress);
		I_HostCollectData hostManager = new HostCollectDataManager();
		ArrayList<FluxVo> flexDataList = new ArrayList<FluxVo>();
		Calendar curDate_s = Calendar.getInstance();
        curDate_s.add(Calendar.DAY_OF_MONTH, -9);
        String date = new SimpleDateFormat("yyyy-MM-dd").format(curDate_s.getTime());
        String starttime = date + " 00:00:00";
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //��ȡ������ʷ����ƽ��ֵ
		Hashtable inutilHash = new Hashtable();
		try {
			inutilHash = hostManager.getAllutilhdx(ipAddress, "AllInBandwidthUtilHdx", starttime, endTime, "", time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Hashtable oututilHash = new Hashtable();
		try {
			oututilHash = hostManager.getAllutilhdx(ipAddress, "AllOutBandwidthUtilHdx", starttime, endTime, "", time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List inutilList = new ArrayList();
		inutilList = (List) inutilHash.get("list");
		List oututilList = new ArrayList();
		oututilList = (List) oututilHash.get("list");
//		if(inutilList!=null&&inutilList.size()>0&&oututilList!=null&&oututilList.size()>0){
//			int num = (inutilList.size()>oututilList.size()?oututilList.size():inutilList.size());
//			for(int i=0;i<num;i++){
//				FluxVo fVo = new FluxVo();
//				Vector inHdxVector = new Vector();
//				inHdxVector = (Vector) inutilList.get(i);
//				Vector outHdxVector = new Vector();
//				outHdxVector = (Vector) oututilList.get(i);
//				if(inHdxVector!=null&&outHdxVector!=null){
//					fVo.setDate((String) inHdxVector.get(1));
//					fVo.setFluxin((String) inHdxVector.get(0));
//					fVo.setFluxout((String) outHdxVector.get(0));
//					flexDataList.add(fVo);
//				}
//			}
//		}
		if(inutilList!=null||oututilList!=null){
			if(inutilList.size()>oututilList.size()){
				int num_min = oututilList.size();
				int num_max = inutilList.size();
				if(num_min==0){
					for(int i=0;i<num_max;i++){
						FluxVo fVo = new FluxVo();
						Vector inHdxVector = new Vector();
						inHdxVector = (Vector) inutilList.get(i);
						if(inHdxVector!=null){
							fVo.setDate((String) inHdxVector.get(1));
							fVo.setFluxin((String) inHdxVector.get(0));
							fVo.setFluxout("0");
							flexDataList.add(fVo);
						}
					}
				} else {
					for(int i=0;i<num_max;i++){
						FluxVo fVo = new FluxVo();
						Vector inHdxVector = new Vector();
						inHdxVector = (Vector) inutilList.get(i);
						String time0 = "";
						String fluxout = "";
						if(inHdxVector!=null){
							time0 = (String) inHdxVector.get(1);
							fVo.setDate(time0);
							fVo.setFluxin((String) inHdxVector.get(0));
						}
						for(int j = 0; j < num_min; j++){
							Vector outHdxVector = new Vector();
							outHdxVector = (Vector) oututilList.get(j);
							if(outHdxVector!=null){
								String time1 = (String) outHdxVector.get(1);
								if(time1.equals(time0)){
									fluxout = (String) outHdxVector.get(0);
									break;
								}
							}
						}
						if(!"".equals(fluxout)&&fluxout.indexOf("-")==-1){
							fVo.setFluxout(fluxout);
						} else {
							fVo.setFluxout("0");
						}
						flexDataList.add(fVo);
					}
				}
			} else if(inutilList.size()<oututilList.size()) {
				int num_max = oututilList.size();
				int num_min = inutilList.size();
				if(num_min==0){
					for(int i=0;i<num_max;i++){
						FluxVo fVo = new FluxVo();
						Vector outHdxVector = new Vector();
						outHdxVector = (Vector) oututilList.get(i);
						if(outHdxVector!=null){
							fVo.setDate((String) outHdxVector.get(1));
							fVo.setFluxin("0");
							fVo.setFluxout((String) outHdxVector.get(0));
							flexDataList.add(fVo);
						}
					}
				} else {
					for(int i=0;i<num_max;i++){
						FluxVo fVo = new FluxVo();
						Vector outHdxVector = new Vector();
						outHdxVector = (Vector) oututilList.get(i);
						String time0 = "";
						String fluxin = "";
						if(outHdxVector!=null){
							time0 = (String) outHdxVector.get(1);
							fVo.setDate(time0);
							fVo.setFluxout((String) outHdxVector.get(0));
						}
						for(int j = 0; j < num_min; j++){
							Vector inHdxVector = new Vector();
							inHdxVector = (Vector) inutilList.get(j);
							if(inHdxVector!=null){
								String time1 = (String) inHdxVector.get(1);
								if(time1.equals(time0)){
									fluxin = (String) inHdxVector.get(0);
									break;
								}
							}
						}
						if(!"".equals(fluxin)&&fluxin.indexOf("-")==-1){
							fVo.setFluxin(fluxin);
						} else {
							fVo.setFluxin("0");
						}
						flexDataList.add(fVo);
					}
				}
			} else {
				int num = (inutilList.size()>oututilList.size()?oututilList.size():inutilList.size());
				for(int i=0;i<num;i++){
					FluxVo fVo = new FluxVo();
					Vector inHdxVector = new Vector();
					inHdxVector = (Vector) inutilList.get(i);
					Vector outHdxVector = new Vector();
					outHdxVector = (Vector) oututilList.get(i);
					if(inHdxVector!=null&&outHdxVector!=null){
						fVo.setDate((String) inHdxVector.get(1));
						fVo.setFluxin((String) inHdxVector.get(0));
						fVo.setFluxout((String) outHdxVector.get(0));
						flexDataList.add(fVo);
					}
				}
			}
		}
		//System.out.println("==================="+flexDataList.size());
		return flexDataList;
	}
	/*
	 * ��ȡһ̨�豸��ָ��ʱ����ۺ�����ֵ
	 * */
	public ArrayList<FluxVo> getFluxByDate(String ipAddress,String dateStr,String time){
		I_HostCollectData hostManager = new HostCollectDataManager();
		ArrayList<FluxVo> flexDataList = new ArrayList<FluxVo>();
		String datestr[] = dateStr.split("=");
		//System.out.println("----------"+datestr.length);
		if(datestr!=null&&datestr.length==2){
			if(java.sql.Date.valueOf(datestr[0]).after(java.sql.Date.valueOf(datestr[1]))){ 
				System.out.println("��ʼ���ڴ��ڽ�ֹ����!");
		    } else { 
            	String starttime = datestr[0] + " 00:00:00";
		        String endTime = datestr[1] + " 23:59:59";
                //��ȡ������ʷ����ƽ��ֵ
				Hashtable inutilHash = new Hashtable();
				try {
					inutilHash = hostManager.getAllutilhdx(ipAddress, "AllInBandwidthUtilHdx", starttime, endTime, "", time);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable oututilHash = new Hashtable();
				try {
					oututilHash = hostManager.getAllutilhdx(ipAddress, "AllOutBandwidthUtilHdx", starttime, endTime, "", time);
				} catch (Exception e) {
					e.printStackTrace();
				}
				List inutilList = new ArrayList();
				inutilList = (List) inutilHash.get("list");
				List oututilList = new ArrayList();
				oututilList = (List) oututilHash.get("list");
//				if(inutilList!=null&&inutilList.size()>0&&oututilList!=null&&oututilList.size()>0){
//					int num = (inutilList.size()>oututilList.size()?oututilList.size():inutilList.size());
//					for(int i=0;i<num;i++){
//						FluxVo fVo = new FluxVo();
//						Vector inHdxVector = new Vector();
//						inHdxVector = (Vector) inutilList.get(i);
//						Vector outHdxVector = new Vector();
//						outHdxVector = (Vector) oututilList.get(i);
//						if(inHdxVector!=null&&outHdxVector!=null){
//							fVo.setDate((String) inHdxVector.get(1));
//							fVo.setFluxin((String) inHdxVector.get(0));
//							fVo.setFluxout((String) outHdxVector.get(0));
//							flexDataList.add(fVo);
//						}
//					}
//				}
				if(inutilList!=null||oututilList!=null){
					if(inutilList.size()>oututilList.size()){
						int num_min = oututilList.size();
						int num_max = inutilList.size();
						if(num_min==0){
							for(int i=0;i<num_max;i++){
								FluxVo fVo = new FluxVo();
								Vector inHdxVector = new Vector();
								inHdxVector = (Vector) inutilList.get(i);
								if(inHdxVector!=null){
									fVo.setDate((String) inHdxVector.get(1));
									fVo.setFluxin((String) inHdxVector.get(0));
									fVo.setFluxout("0");
									flexDataList.add(fVo);
								}
							}
						} else {
							for(int i=0;i<num_max;i++){
								FluxVo fVo = new FluxVo();
								Vector inHdxVector = new Vector();
								inHdxVector = (Vector) inutilList.get(i);
								String time0 = "";
								String fluxout = "";
								if(inHdxVector!=null){
									time0 = (String) inHdxVector.get(1);
									fVo.setDate(time0);
									fVo.setFluxin((String) inHdxVector.get(0));
								}
								for(int j = 0; j < num_min; j++){
									Vector outHdxVector = new Vector();
									outHdxVector = (Vector) oututilList.get(j);
									if(outHdxVector!=null){
										String time1 = (String) outHdxVector.get(1);
										if(time1.equals(time0)){
											fluxout = (String) outHdxVector.get(0);
											break;
										}
									}
								}
								if(!"".equals(fluxout)&&fluxout.indexOf("-")==-1){
									fVo.setFluxout(fluxout);
								} else {
									fVo.setFluxout("0");
								}
								flexDataList.add(fVo);
							}
						}
					} else if(inutilList.size()<oututilList.size()) {
						int num_max = oututilList.size();
						int num_min = inutilList.size();
						if(num_min==0){
							for(int i=0;i<num_max;i++){
								FluxVo fVo = new FluxVo();
								Vector outHdxVector = new Vector();
								outHdxVector = (Vector) oututilList.get(i);
								if(outHdxVector!=null){
									fVo.setDate((String) outHdxVector.get(1));
									fVo.setFluxin("0");
									fVo.setFluxout((String) outHdxVector.get(0));
									flexDataList.add(fVo);
								}
							}
						} else {
							for(int i=0;i<num_max;i++){
								FluxVo fVo = new FluxVo();
								Vector outHdxVector = new Vector();
								outHdxVector = (Vector) oututilList.get(i);
								String time0 = "";
								String fluxin = "";
								if(outHdxVector!=null){
									time0 = (String) outHdxVector.get(1);
									fVo.setDate(time0);
									fVo.setFluxout((String) outHdxVector.get(0));
								}
								for(int j = 0; j < num_min; j++){
									Vector inHdxVector = new Vector();
									inHdxVector = (Vector) inutilList.get(j);
									if(inHdxVector!=null){
										String time1 = (String) inHdxVector.get(1);
										if(time1.equals(time0)){
											fluxin = (String) inHdxVector.get(0);
											break;
										}
									}
								}
								if(!"".equals(fluxin)&&fluxin.indexOf("-")==-1){
									fVo.setFluxin(fluxin);
								} else {
									fVo.setFluxin("0");
								}
								flexDataList.add(fVo);
							}
						}
					} else {
						int num = (inutilList.size()>oututilList.size()?oututilList.size():inutilList.size());
						for(int i=0;i<num;i++){
							FluxVo fVo = new FluxVo();
							Vector inHdxVector = new Vector();
							inHdxVector = (Vector) inutilList.get(i);
							Vector outHdxVector = new Vector();
							outHdxVector = (Vector) oututilList.get(i);
							if(inHdxVector!=null&&outHdxVector!=null){
								fVo.setDate((String) inHdxVector.get(1));
								fVo.setFluxin((String) inHdxVector.get(0));
								fVo.setFluxout((String) outHdxVector.get(0));
								flexDataList.add(fVo);
							}
						}
					}
				}
		    } 
		}
		return flexDataList;
	}
	
	/*
	 * ��ȡһ̨�豸�ڵ�����ͨ�ʵ�ֵ
	 * */
	public ArrayList<FlexVo> getPingByDay(String ipAddress,String time){
		I_HostCollectData hostManager = new HostCollectDataManager();
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Hashtable pingHash = new Hashtable();
        try {
        	pingHash = hostManager.getPingData(ipAddress, "ConnectUtilization", startTime, endTime, "", time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List pingList = new ArrayList();
		pingList = (List) pingHash.get("list");
		if(pingList!=null&&pingList.size()>0){
			int num = pingList.size();
			for(int i=0;i<num;i++){
				FlexVo fVo = new FlexVo();
				Vector pingVector = new Vector();
				pingVector = (Vector) pingList.get(i);
				if(pingVector!=null){
					fVo.setObjectName((String) pingVector.get(1));
					fVo.setObjectNumber((String) pingVector.get(0));
					flexDataList.add(fVo);
				}
			}
		}
		//System.out.println("--------"+flexDataList.size());
		return flexDataList;
		
	}
	/*
	 * ��ȡһ̨Domino�ڵ�����ͨ�ʵ�ֵ
	 * @param type �Ժ���
	 * */
	public ArrayList<FlexVo> getDomPing(String ipAddress,String time,String type){
		I_HostCollectData hostManager = new HostCollectDataManager();
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Hashtable pingHash = new Hashtable();
        try {
        	
        	pingHash = hostManager.getCategory(ipAddress, "DomPing", "ConnectUtilization", startTime, endTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List pingList = new ArrayList();
		pingList = (List) pingHash.get("list");
		if(pingList!=null&&pingList.size()>0){
			int num = pingList.size();
			for(int i=0;i<num;i++){
				FlexVo fVo = new FlexVo();
				Vector pingVector = new Vector();
				pingVector = (Vector) pingList.get(i);
				if(pingVector!=null){
					fVo.setObjectName((String) pingVector.get(1));
					fVo.setObjectNumber((String) pingVector.get(0));
					flexDataList.add(fVo);
				}
			}
		}
		//System.out.println("--------"+flexDataList.size());
		return flexDataList;
		
	}
	/**
	 * @CPU
	 * @param date
	 * @param ipAddress
	 * @param type
	 * @return
	 */
	public ArrayList<Vos> getDomCPUByDay(String date,String ipAddress,String type) {
		I_HostCollectData hostManager = new HostCollectDataManager();
		String startTime = date + " 00:00:00";
		String endTime = date + " 23:59:59";;
		// ��collectdataȡcpu����ʷ����,����ڱ���
		Hashtable cpuHash = new Hashtable();
		try {
			cpuHash = hostManager.getCategory(ipAddress, "dominoCpu", "Utilization", startTime, endTime);
		} catch (Exception e) {
			e.printStackTrace();
		}

		List cpuList = new ArrayList();
		if (cpuHash.get("list") != null) {
			ArrayList<Vos> flexDataList = new ArrayList<Vos>();
			cpuList = (ArrayList) cpuHash.get("list");
			Vos fVo;
			for (int i = 0; i < cpuList.size(); i++) {
				Vector cpuVector = new Vector();
				fVo = new Vos();
				cpuVector = (Vector) cpuList.get(i);
				if (cpuVector != null || cpuVector.size() > 0) {
					fVo.setObjectNumber((String) cpuVector.get(0));
					fVo.setObjectName2((String) cpuVector.get(1));
					fVo.setObjectName1(ipAddress);
					//SysLogger.info(fVo.getObjectName1()+"===="+fVo.getObjectNumber());
					flexDataList.add(fVo);
				}
			}
			return flexDataList;
		}
		return null;
	}	
	public ArrayList<MemoryVo> getDomMemoryByDay(String ipAddress,String sortType,String type){
		I_HostCollectData hostManager = new HostCollectDataManager();
		ArrayList<MemoryVo> flexDataList = new ArrayList<MemoryVo>();
		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		DecimalFormat df=new DecimalFormat("#.##");
        //��ȡ�ڴ���������ʷ����ƽ��ֵ
	
		Hashtable servmemHash = new Hashtable();
		try {
			servmemHash = hostManager.getCategory(ipAddress, sortType,"Utilization" ,startTime , endTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List physicalList =  new ArrayList();
		
		if(servmemHash!=null&&servmemHash.size()>0){
			physicalList = (List) servmemHash.get("list");
			
		}
		
		if(physicalList!=null&&physicalList.size()>0){
			
			int p_size = physicalList!=null?physicalList.size():0;
			
				int num = p_size;
				for(int i=0;i<num;i++){
					MemoryVo mVo = new MemoryVo();
					Vector physicalVector = new Vector();
					physicalVector = (Vector) physicalList.get(i);
					mVo.setDate((String) physicalVector.get(1));
					mVo.setPhysicalMemory(df.format(Double.parseDouble((String) physicalVector.get(0))));
				
					flexDataList.add(mVo);
				
			}
		}
		
		return flexDataList;
	}
	/**
	 * domino ����
	 * @param ipAddress
	 * @param dateStr
	 * @param time
	 * @param type
	 * @return
	 */
	public ArrayList getDomDiskByDate(String ipAddress,String dateStr,String time,String type){
	//	SysLogger.info("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^1");
		HostCollectDataManager hostManager = new HostCollectDataManager();
		ArrayList flexDataList = new ArrayList();
		ArrayList list = new ArrayList();
		String date[] = null;
		String startTime = "";
		String endTime = "";
		if("".equals(dateStr)){
			startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
			endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		} else {
			date = dateStr.split("=");
			startTime = date[0] + " 00:00:00";
			endTime = date[1] + " 23:59:59";
		}
		DecimalFormat df=new DecimalFormat("#.##");
		Hashtable ipAllData = (Hashtable) ShareData.getDominodata().get(ipAddress);
		String[] arrName = null;
		String diskname = "";
		String runmodel = PollingEngine.getCollectwebflag();//ϵͳ����ģʽ
		if("0".equals(runmodel)){
			//�ɼ�������Ǽ���ģʽ
			String name = "";
			if (ipAllData != null) {
				List diskList = (List) ipAllData.get("Disk");
				if (diskList != null && diskList.size() > 0) {
					for (int si = 0; si < diskList.size(); si++) {
						DominoDisk diskdata = (DominoDisk)diskList.get(si);
						if(diskdata!=null){
							if (!diskdata.getDiskname().equals("")) {
								name = name + diskdata.getDiskname() + ";";
							}
						}
					}
				}
			}
			arrName = name.split(";");
		}else{
			//�ɼ�������Ƿ���ģʽ
			//arrName = new String[]{"C:/","D:/","E:/","F:/","G:/","H:/"};
			String allipstr = SysUtil.doip(ipAddress);
			String tablename = "domdisk"+time+allipstr;
			List<String> subentityList = hostManager.getSubentitysByTableName(tablename);
			if(subentityList != null){
				String[] temps = new String[subentityList.size()];
				for(int i=0; i<subentityList.size();i++){
					temps[i] = subentityList.get(i);
				}
				arrName = temps;
			}
		}
        //��ȡӲ����������ʷ����ֵ
		if(arrName!=null&&arrName.length>0){
			Hashtable virtualHash[] = new Hashtable[arrName.length];
			for(int i=0;i<arrName.length;i++){
				diskname = diskname + subString(arrName[i]);
				if(i!=arrName.length-1){
					diskname = diskname + ",";
				}
				try {
					virtualHash[i] = hostManager.getDisk(ipAddress,"",arrName[i], startTime, endTime, time);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			flexDataList.add(diskname);
			if(virtualHash[0]!=null){
				List dlist=new ArrayList();
				dlist = (List) virtualHash[0].get("list");
				int dlist_len = dlist.size();
				for(int i=0;i<dlist_len;i++){
					Vector v = (Vector) dlist.get(i);
					Hashtable h =new Hashtable();
					h.put("objectName", v.get(3));
					//SysLogger.info("======"+v.get(3));
					for(int j = 0;j<virtualHash.length;j++){
						List virtualList = new ArrayList();
						virtualList = (List) virtualHash[j].get("list");
						for(int k =0;k<virtualList.size();k++){
							Vector vector = new Vector();
							vector = (Vector) virtualList.get(k);
							if(vector.get(3).equals(v.get(3))){
								String tempstr = df.format(Double.parseDouble((String)vector.get(0)));
//								SysLogger.info(subString(arrName[j])+"======"+tempstr);
								h.put(subString(arrName[j]), tempstr);
								break;
							}
						}
					}
					list.add(h);
				}
			}
			flexDataList.add(list);
		}
		return flexDataList;
	}
	/*
	 * ��ȡһ̨�豸�ڵ��컻ҳ�ʵ�ֵ
	 * */
	public ArrayList<FlexVo> getLineByDay(String ipAddress,String time){
		I_HostCollectData hostManager = new HostCollectDataManager();
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Hashtable pgusedHash = new Hashtable();
        try {
        	pgusedHash = hostManager.getPgusedData(ipAddress, startTime, endTime, "", time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List pingList = new ArrayList();
		pingList = (List) pgusedHash.get("list");
		if(pingList!=null&&pingList.size()>0){
			int num = pingList.size();
			for(int i=0;i<num;i++){
				FlexVo fVo = new FlexVo();
				Vector pingVector = new Vector();
				pingVector = (Vector) pingList.get(i);
				if(pingVector!=null){
					fVo.setObjectName((String) pingVector.get(1));
					fVo.setObjectNumber((String) pingVector.get(0));
					flexDataList.add(fVo);
				}
			}
		}
		//System.out.println("--------"+flexDataList.size());
		return flexDataList;
		
	}
	
	/*
	 * ��ȡһ̨�豸��ָ��ʱ�����ͨ�ʵ�ֵ
	 * */
	public ArrayList<FlexVo> getPingByDate(String ipAddress,String dateStr,String time){
		I_HostCollectData hostManager = new HostCollectDataManager();
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		String datestr[] = dateStr.split("=");
		//System.out.println("---------"+dateStr);
		if(datestr.length==2){
			if(java.sql.Date.valueOf(datestr[0]).after(java.sql.Date.valueOf(datestr[1]))){ 
				System.out.println("��ʼ���ڴ��ڽ�ֹ����!");
		    } else {
            	String starttime = datestr[0] + " 00:00:00";
		        String endTime = datestr[1] + " 23:59:59";
		        Hashtable pingHash = new Hashtable();
		        try {
		        	pingHash = hostManager.getPingData(ipAddress, "ConnectUtilization", starttime, endTime, "", time);
				} catch (Exception e) {
					e.printStackTrace();
				}
				List pingList = new ArrayList();
				pingList = (List) pingHash.get("list");
				if(pingList!=null&&pingList.size()>0){
					int num = pingList.size();
					for(int i=0;i<num;i++){
						FlexVo fVo = new FlexVo();
						Vector pingVector = new Vector();
						pingVector = (Vector) pingList.get(i);
						if(pingVector!=null){
							fVo.setObjectName((String) pingVector.get(1));
							fVo.setObjectNumber((String) pingVector.get(0));
							flexDataList.add(fVo);
						}
					}
				}
		    }
		}
		return flexDataList;
	}
	
	/*
	 * ��ȡһ̨��������ָ��ʱ��λ�ҳ�ʵ�ֵ
	 * */
	public ArrayList<FlexVo> getLineByDate(String ipAddress,String dateStr,String time){
		I_HostCollectData hostManager = new HostCollectDataManager();
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		String datestr[] = dateStr.split("=");
		//System.out.println("---------"+dateStr);
		if(datestr.length==2){
			if(java.sql.Date.valueOf(datestr[0]).after(java.sql.Date.valueOf(datestr[1]))){ 
				System.out.println("��ʼ���ڴ��ڽ�ֹ����!");
		    } else {
            	String starttime = datestr[0] + " 00:00:00";
		        String endTime = datestr[1] + " 23:59:59";
		        Hashtable pingHash = new Hashtable();
		        try {
		        	pingHash = hostManager.getPgusedData(ipAddress, starttime, endTime, "", time);
				} catch (Exception e) {
					e.printStackTrace();
				}
				List pingList = new ArrayList();
				pingList = (List) pingHash.get("list");
				if(pingList!=null&&pingList.size()>0){
					int num = pingList.size();
					for(int i=0;i<num;i++){
						FlexVo fVo = new FlexVo();
						Vector pingVector = new Vector();
						pingVector = (Vector) pingList.get(i);
						if(pingVector!=null){
							fVo.setObjectName((String) pingVector.get(1));
							fVo.setObjectNumber((String) pingVector.get(0));
							flexDataList.add(fVo);
						}
					}
				}
		    }
		}
		return flexDataList;
	}
	
	/*
	 * ��ȡһ̨�豸��ָ��ʱ�����Ӧʱ���ֵ
	 * */
	public ArrayList<FlexVo> getResponseTimeByDate(String ipAddress,String dateStr,String time){
		I_HostCollectData hostManager = new HostCollectDataManager();
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		String datestr[] = dateStr.split("=");
		//System.out.println("---------"+dateStr);
		if(datestr.length==2){
			if(java.sql.Date.valueOf(datestr[0]).after(java.sql.Date.valueOf(datestr[1]))){ 
				System.out.println("��ʼ���ڴ��ڽ�ֹ����!");
		    } else {
            	String starttime = datestr[0] + " 00:00:00";
		        String endTime = datestr[1] + " 23:59:59";
		        Hashtable responseHash = new Hashtable();
		        try {
		        	responseHash = hostManager.getPingData(ipAddress, "ResponseTime", starttime, endTime, "", time);
				} catch (Exception e) {
					e.printStackTrace();
				}
				List responseList = new ArrayList();
				responseList = (List) responseHash.get("list");
				if(responseList!=null&&responseList.size()>0){
					int num = responseList.size();
					for(int i=0;i<num;i++){
						FlexVo fVo = new FlexVo();
						Vector responseVector = new Vector();
						responseVector = (Vector) responseList.get(i);
						if(responseVector!=null){
							fVo.setObjectName((String) responseVector.get(1));
							fVo.setObjectNumber((String) responseVector.get(0));
							flexDataList.add(fVo);
						}
					}
				}
		    }
		}
		return flexDataList;
		
	}
	
	/*
	 * ��ȡһ̨�豸�ڵ�����Ӧʱ���ֵ
	 * */
	public ArrayList<FlexVo> getResponseTimeByDay(String ipAddress){
		I_HostCollectData hostManager = new HostCollectDataManager();
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Hashtable responseHash = new Hashtable();
        try {
        	responseHash = hostManager.getPingData(ipAddress, "ResponseTime", startTime, endTime, "", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		List responseList = new ArrayList();
		responseList = (List) responseHash.get("list");
		if(responseList!=null&&responseList.size()>0){
			int num = responseList.size();
			for(int i=0;i<num;i++){
				FlexVo fVo = new FlexVo();
				Vector responseVector = new Vector();
				responseVector = (Vector) responseList.get(i);
				if(responseVector!=null){
					fVo.setObjectName((String) responseVector.get(1));
					fVo.setObjectNumber((String) responseVector.get(0));
					flexDataList.add(fVo);
				}
			}
		}
		//System.out.println("--------"+flexDataList.size());
		return flexDataList;
		
	}
	
	/*
	 * ��ȡһ̨�豸�ڵ���Ŀ�����
	 * */
	public ArrayList<FlexVo> getAvgPingByDay(String ipAddress){
		I_HostCollectData hostManager = new HostCollectDataManager();
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Hashtable pingHash = new Hashtable();
        try {
        	pingHash = hostManager.getPingData(ipAddress, "ConnectUtilization", startTime, endTime, "", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String pingValue = (String)pingHash.get("avgput");
		if(pingValue!=null&&!"".equals(pingValue)){
			FlexVo fVo1 = new FlexVo();
			fVo1.setObjectName("������");
			fVo1.setObjectNumber(Float.parseFloat(pingValue)+"");
			flexDataList.add(fVo1);
			FlexVo fVo2 = new FlexVo();
			fVo2.setObjectName("��������");
			fVo2.setObjectNumber((100-Float.parseFloat(pingValue))+"");
			flexDataList.add(fVo2);
		}
//		System.out.println("--------"+flexDataList.size());
		return flexDataList;
	}
	/*
	 * ��ȡһ̨�豸��ָ��ʱ���ƽ����ͨ��
	 * */
	public String getAvgPingByDate(String ipAddress,String dateStr){
		I_HostCollectData hostManager = new HostCollectDataManager();
		String date[] = dateStr.split("=");
//		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
//		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String startTime = date[0] + " 00:00:00";
		String endTime = date[1] + " 23:59:59";
        Hashtable pingHash = new Hashtable();
        try {
        	pingHash = hostManager.getPingData(ipAddress, "ConnectUtilization", startTime, endTime, "", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String pingValue = (String)pingHash.get("avgput");
//		System.out.println("--------"+flexDataList.size());
		return pingValue;
	}
	
	/*
	 * ��ȡһ̨��������ָ��ʱ���ƽ����ҳ��
	 * */
	public String getAvgLineByDate(String ipAddress,String dateStr){
		I_HostCollectData hostManager = new HostCollectDataManager();
		String date[] = dateStr.split("=");
//		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
//		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String startTime = date[0] + " 00:00:00";
		String endTime = date[1] + " 23:59:59";
        Hashtable pingHash = new Hashtable();
        try {
        	pingHash = hostManager.getPgusedData(ipAddress, startTime, endTime, "", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String pingValue = (String)pingHash.get("avgput");
//		System.out.println("--------"+flexDataList.size());
		return pingValue;
	}
	
	/*
	 * ��ȡһ̨�豸��ָ��ʱ���ƽ����Ӧʱ��
	 * */
	public String getAvgResponseByDate(String ipAddress,String dateStr){
		I_HostCollectData hostManager = new HostCollectDataManager();
		String date[] = dateStr.split("=");
//		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
//		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String startTime = date[0] + " 00:00:00";
		String endTime = date[1] + " 23:59:59";
        Hashtable pingHash = new Hashtable();
        try {
        	pingHash = hostManager.getPingData(ipAddress, "ResponseTime", startTime, endTime, "", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String responseValue = (String)pingHash.get("avgput");
//		System.out.println("--------"+flexDataList.size());
		return responseValue;
	}
	/*
	 * ��ȡһ̨�豸��ָ��ʱ���ƽ���ۺ�����ֵ
	 * */
	public String getAvgFluxByDate(String ipAddress,String dateStr){
		I_HostCollectData hostManager = new HostCollectDataManager();
		String date[] = dateStr.split("=");
		String startTime = date[0] + " 00:00:00";
		String endTime = date[1] + " 23:59:59";
        //��ȡ������ʷ����ƽ��ֵ
		Hashtable inutilHash = new Hashtable();
		try {
			inutilHash = hostManager.getAllutilhdx(ipAddress, "AllInBandwidthUtilHdx", startTime, endTime, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Hashtable oututilHash = new Hashtable();
		try {
			oututilHash = hostManager.getAllutilhdx(ipAddress, "AllOutBandwidthUtilHdx", startTime, endTime, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String inAvgutil = (String) inutilHash.get("avgput");
		String outAvgutil = (String) oututilHash.get("avgput");
		String inMaxutil = (String) inutilHash.get("max");
		String outMaxutil = (String) oututilHash.get("max");
		String inMinutil = (String) inutilHash.get("min");
		String outMinutil = (String) oututilHash.get("min");
		String inTemputil = (String) inutilHash.get("temp");
		String outTemputil = (String) oututilHash.get("temp");
//		hash.put("in", inTemputil+":"+inMaxutil+":"+inMinutil+":"+inAvgutil);
//		hash.put("out", outTemputil+":"+outMaxutil+":"+outMinutil+":"+outAvgutil);
//		System.out.println("inAvgutil:"+inAvgutil+"===================outAvgutil:"+outAvgutil);
		return "���"+":"+inTemputil+"kb/s:"+inMaxutil+"kb/s:"+inMinutil+"kb/s:"+inAvgutil+"kb/s,����"+":"+outTemputil+"kb/s:"+outMaxutil+"kb/s:"+outMinutil+"kb/s:"+outAvgutil+"kb/s";
		
	}
	/*
	 * ��ȡ��������¼�ͳ��
	 * */
	public ArrayList<FlexVo> getEventByDay(){
		System.out.println("-----------------------------");
		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		ArrayList<FlexVo> dataList = new ArrayList<FlexVo>();
		EventListDao eventListDao = new EventListDao();
		session = FlexContext.getFlexSession();
		User current_user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
		String bids = current_user.getBusinessids();
		if (current_user.getRole() == 0 || current_user.getRole() == 1) {
		    bids = "-1";
	    }
		try {
			flexDataList = (ArrayList) eventListDao.getEventList(startTime, endTime, bids);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			eventListDao.close();
		}
		if(flexDataList.size()<4){
			for(int i=3;i>=0;i--){
				boolean tag = false;
				String num = "";
				for(int j=0;j<flexDataList.size();j++){
					FlexVo flexVo = flexDataList.get(j);
					if(flexVo.getObjectName().equals(""+i)){
						tag = true;
						num = flexVo.getObjectNumber();
					}
				}
				if(!tag){
					FlexVo flexvo = new FlexVo();
					if(i==3){
						flexvo.setObjectName("�����¼�");
						flexvo.setObjectNumber("0");
					} else if(i==2){
						flexvo.setObjectName("�����¼�");
						flexvo.setObjectNumber("0");
					} else if(i==1){
						flexvo.setObjectName("��ͨ�¼�");
						flexvo.setObjectNumber("0");
					} else if(i==0){
						flexvo.setObjectName("��ʾ��Ϣ");
						flexvo.setObjectNumber("0");
					}
					dataList.add(flexvo);
				} else {
					FlexVo flexvo = new FlexVo();
					if(i==3){
						flexvo.setObjectName("�����¼�");
						flexvo.setObjectNumber(num);
					} else if(i==2){
						flexvo.setObjectName("�����¼�");
						flexvo.setObjectNumber(num);
					} else if(i==1){
						flexvo.setObjectName("��ͨ�¼�");
						flexvo.setObjectNumber(num);
					} else if(i==0){
						flexvo.setObjectName("��ʾ��Ϣ");
						flexvo.setObjectNumber("0");
					}
					dataList.add(flexvo);
				}
			}
			
		} else {
			for(int j=0;j<flexDataList.size();j++){
				FlexVo flexVo = flexDataList.get(j);
				FlexVo flexvo = new FlexVo();
				if(flexVo.getObjectName().equals("3")){
					flexvo.setObjectName("�����¼�");
					flexvo.setObjectNumber(flexVo.getObjectNumber());
				} else if(flexVo.getObjectName().equals("2")){
					flexvo.setObjectName("�����¼�");
					flexvo.setObjectNumber(flexVo.getObjectNumber());
				} else if(flexVo.getObjectName().equals("1")){
					flexvo.setObjectName("��ͨ�¼�");
					flexvo.setObjectNumber(flexVo.getObjectNumber());
				} else if(flexVo.getObjectName().equals("0")){
					flexvo.setObjectName("��ʾ��Ϣ");
					flexvo.setObjectNumber(flexVo.getObjectNumber());
				}
				dataList.add(flexvo);
			}
		}
		return dataList;
		
	}
	
	/*
	 * ��ȡ�����������и�ʱ��ε��ڴ�������
	 * */
	public ArrayList<MemoryVo> getMemoryByDay(String ipAddress){
		I_HostCollectData hostManager = new HostCollectDataManager();
		ArrayList<MemoryVo> flexDataList = new ArrayList<MemoryVo>();
		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		DecimalFormat df=new DecimalFormat("#.##");
        //��ȡ�ڴ���������ʷ����ƽ��ֵ
		Hashtable virtualHash = new Hashtable();
		try {
			virtualHash = hostManager.getMemory(ipAddress, "VirtualMemory", startTime, endTime, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Hashtable physicalHash = new Hashtable();
		try {
			physicalHash = hostManager.getMemory(ipAddress, "PhysicalMemory", startTime, endTime, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		List virtualList = new ArrayList();
		if(virtualHash!=null&&virtualHash.size()>0){
			virtualList = (List) virtualHash.get("list");
			try {
				if(virtualList==null||virtualList.size()==0){
					virtualHash = hostManager.getMemory(ipAddress, "SwapMemory", startTime, endTime, "");
					virtualList = (List) virtualHash.get("list");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		List physicalList =  new ArrayList();
		if(physicalHash!=null&&physicalHash.size()>0){
			physicalList = (List) physicalHash.get("list");
		}
		if((virtualList!=null&&virtualList.size()>0)||(physicalList!=null&&physicalList.size()>0)){
			int v_size = virtualList!=null?virtualList.size():0;
			int p_size = physicalList!=null?physicalList.size():0;
			if(v_size>p_size){
				int num = v_size;
				for(int i=0;i<num;i++){
					MemoryVo mVo = new MemoryVo();
					Vector virtualVector = new Vector();
					virtualVector = (Vector) virtualList.get(i);
					mVo.setDate((String) virtualVector.get(1));
					mVo.setVirtualMemory(df.format(Double.parseDouble((String) virtualVector.get(0))));
					if(physicalList!=null&&physicalList.size()>0){
						for(int j=0;j<physicalList.size();j++){
							Vector physicalVector = (Vector) physicalList.get(j);
							if(physicalVector!=null&&physicalVector.size()>0){
								if(physicalVector.get(1)!=null&&physicalVector.get(1).toString().equals((String) virtualVector.get(1))&&physicalVector.get(0)!=null){
									mVo.setPhysicalMemory(df.format(Double.parseDouble((String) physicalVector.get(0))));
									break;
								} else {
									mVo.setPhysicalMemory("0");
								}
							} else {
								mVo.setPhysicalMemory("0");
							}
						}
					}
					flexDataList.add(mVo);
				}
			} else {
				int num = p_size;
				for(int i=0;i<num;i++){
					MemoryVo mVo = new MemoryVo();
					Vector physicalVector = new Vector();
					physicalVector = (Vector) physicalList.get(i);
					mVo.setDate((String) physicalVector.get(1));
					mVo.setPhysicalMemory(df.format(Double.parseDouble((String) physicalVector.get(0))));
					if(virtualList!=null&&virtualList.size()>0){
						for(int j=0;j<virtualList.size();j++){
							Vector virtualVector = (Vector) virtualList.get(j);
							if(virtualVector!=null&&virtualVector.size()>0){
								if(virtualVector.get(1)!=null&&virtualVector.get(1).toString().equals((String) physicalVector.get(1))&&virtualVector.get(0)!=null){
									mVo.setVirtualMemory(df.format(Double.parseDouble((String) virtualVector.get(0))));
									break;
								} else {
									mVo.setVirtualMemory("0");
								}
								
							} else {
								mVo.setVirtualMemory("0");
							}
						}
					}
					flexDataList.add(mVo);
				}
			}
		}
		return flexDataList;
	}
	
	/*
	 * ��ȡ��������ָ��ʱ��ε��ڴ�������
	 * */
	public ArrayList<MemoryVo> getMemoryByDate(String ipAddress,String dateStr,String time){
		I_HostCollectData hostManager = new HostCollectDataManager();
		ArrayList<MemoryVo> flexDataList = new ArrayList<MemoryVo>();
		String date[] = dateStr.split("=");
		String startTime = date[0] + " 00:00:00";
		String endTime = date[1] + " 23:59:59";
		DecimalFormat df=new DecimalFormat("#.##");
		if(java.sql.Date.valueOf(date[0]).after(java.sql.Date.valueOf(date[1]))){ 
			//System.out.println("��ʼ���ڴ��ڽ�ֹ����!");
	    } else {
//	    	��ȡ�ڴ���������ʷ����ƽ��ֵ
			Hashtable virtualHash = new Hashtable();
			try {
				virtualHash = hostManager.getMemory(ipAddress, "VirtualMemory", startTime, endTime, time);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Hashtable physicalHash = new Hashtable();
			try {
				physicalHash = hostManager.getMemory(ipAddress, "PhysicalMemory", startTime, endTime, time);
			} catch (Exception e) {
				e.printStackTrace();
			}
			List virtualList = new ArrayList();
			if(virtualHash!=null&&virtualHash.size()>0){
				virtualList = (List) virtualHash.get("list");
				try {
					if(virtualList==null||virtualList.size()==0){
						virtualHash = hostManager.getMemory(ipAddress, "SwapMemory", startTime, endTime, "");
						virtualList = (List) virtualHash.get("list");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			List physicalList = new ArrayList();
			if(physicalHash!=null&&physicalHash.size()>0){
				physicalList = (List) physicalHash.get("list");
			}
			if((virtualList!=null&&virtualList.size()>0)||(physicalList!=null&&physicalList.size()>0)){
				int v_size = virtualList!=null?virtualList.size():0;
				int p_size = physicalList!=null?physicalList.size():0;
				if(v_size>p_size){
					int num = v_size;
					for(int i=0;i<num;i++){
						MemoryVo mVo = new MemoryVo();
						Vector virtualVector = new Vector();
						virtualVector = (Vector) virtualList.get(i);
						mVo.setDate((String) virtualVector.get(1));
						mVo.setVirtualMemory(df.format(Double.parseDouble((String) virtualVector.get(0))));
						if(physicalList!=null&&physicalList.size()>0){
							for(int j=0;j<physicalList.size();j++){
								Vector physicalVector = (Vector) physicalList.get(j);
								if(physicalVector!=null&&physicalVector.size()>0){
									if(physicalVector.get(1)!=null&&physicalVector.get(1).toString().equals((String) virtualVector.get(1))&&physicalVector.get(0)!=null){
										mVo.setPhysicalMemory(df.format(Double.parseDouble((String) physicalVector.get(0))));
										break;
									} else {
										mVo.setPhysicalMemory("0");
									}
								} else {
									mVo.setPhysicalMemory("0");
								}
							}
						}
						flexDataList.add(mVo);
					}
				} else {
					int num = p_size;
					for(int i=0;i<num;i++){
						MemoryVo mVo = new MemoryVo();
						Vector physicalVector = new Vector();
						physicalVector = (Vector) physicalList.get(i);
						mVo.setDate((String) physicalVector.get(1));
						mVo.setPhysicalMemory(df.format(Double.parseDouble((String) physicalVector.get(0))));
						if(virtualList!=null&&virtualList.size()>0){
							for(int j=0;j<virtualList.size();j++){
								Vector virtualVector = (Vector) virtualList.get(j);
								if(virtualVector!=null&&virtualVector.size()>0){
									if(virtualVector.get(1)!=null&&virtualVector.get(1).toString().equals((String) physicalVector.get(1))&&virtualVector.get(0)!=null){
										mVo.setVirtualMemory(df.format(Double.parseDouble((String) virtualVector.get(0))));
										break;
									} else {
										mVo.setVirtualMemory("0");
									}
									
								} else {
									mVo.setVirtualMemory("0");
								}
							}
						}
						flexDataList.add(mVo);
					}
				}
			}
	    }
		return flexDataList;
	}
	/*
	 * ��ȡһ̨�豸��ָ��ʱ���ƽ���ڴ�������
	 * */
	public String getAvgMemoryByDate(String ipAddress,String dateStr){
		I_HostCollectData hostManager = new HostCollectDataManager();
		String date[] = dateStr.split("=");
		String startTime = date[0] + " 00:00:00";
		String endTime = date[1] + " 23:59:59";
		String vAvgutil = "";
		String pAvgutil = "";
		String vMaxutil = "";
		String pMaxutil = "";
		String vMinutil = "";
		String pMinutil = "";
		String vTemputil = "";
		String pTemputil = "";
		try {
			DecimalFormat df=new DecimalFormat("#.##");
			if(java.sql.Date.valueOf(date[0]).after(java.sql.Date.valueOf(date[1]))){ 
				//System.out.println("��ʼ���ڴ��ڽ�ֹ����!");
		    } else {
		    	Hashtable virtualHash = new Hashtable();
				try {
					virtualHash = hostManager.getMemory(ipAddress, "VirtualMemory", startTime, endTime, "");
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					if(virtualHash.get("avgput")==null||"".equalsIgnoreCase(virtualHash.get("avgput").toString())){
						virtualHash = hostManager.getMemory(ipAddress, "SwapMemory", startTime, endTime, "");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable physicalHash = new Hashtable();
				try {
					physicalHash = hostManager.getMemory(ipAddress, "PhysicalMemory", startTime, endTime, "");
				} catch (Exception e) {
					e.printStackTrace();
				}
				vAvgutil = df.format(Double.parseDouble((String) virtualHash.get("avgput")));
				pAvgutil = df.format(Double.parseDouble((String) physicalHash.get("avgput")));
				vMaxutil = df.format(Double.parseDouble((String) virtualHash.get("max")));
				pMaxutil = df.format(Double.parseDouble((String) physicalHash.get("max")));
				vMinutil = df.format(Double.parseDouble((String) virtualHash.get("min")));
				pMinutil = df.format(Double.parseDouble((String) physicalHash.get("min")));
				vTemputil = df.format(Double.parseDouble((String) virtualHash.get("temp")));
				pTemputil = df.format(Double.parseDouble((String) physicalHash.get("temp")));
//				hash.put("in", inTemputil+":"+inMaxutil+":"+inMinutil+":"+inAvgutil);
//				hash.put("out", outTemputil+":"+outMaxutil+":"+outMinutil+":"+outAvgutil);
//				System.out.println("inAvgutil:"+inAvgutil+"===================outAvgutil:"+outAvgutil);
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "�����ڴ�"+":"+pTemputil+"%:"+pMaxutil+"%:"+pMinutil+"%:"+pAvgutil+"%,�����ڴ�"+":"+vTemputil+"%:"+vMaxutil+"%:"+vMinutil+"%:"+vAvgutil+"%";
		
	}
	
	private String subString(String str){
		int len = str.length();
		if(len>0&&str.indexOf(":")!=-1){
			return str.substring(0, 3);
		} else {
			return str;
		}
	}
	public ArrayList<Vo> getDiskByDay(String ipAddress){
		ArrayList<Vo> flexDataList = new ArrayList<Vo>();
		Vector diskVector = null;
		String runmodel = PollingEngine.getCollectwebflag();
		if("0".equals(runmodel)){
			//�ɼ�������Ǽ���ģʽ
			Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(ipAddress);
			if (ipAllData != null) {
				diskVector = (Vector) ipAllData.get("disk");
			}
			//String[] diskItem={"AllSize","UsedSize","Utilization","INodeUsedSize","INodeUtilization"};
			String util = "";
			String name = "";
			String allSize = "";
			String usedSize = "";
			DecimalFormat df1=new DecimalFormat("#");
			DecimalFormat df2=new DecimalFormat("#.#");
			if (diskVector != null && diskVector.size() > 0) {
				for (int si = 0; si < diskVector.size(); si++) {
					Diskcollectdata diskdata = (Diskcollectdata) diskVector.elementAt(si);
					if(diskdata!=null){
						if (diskdata.getEntity().equalsIgnoreCase("Utilization")) {
							util = util + df1.format(Double.parseDouble(diskdata.getThevalue())) + ";";
						} else if (diskdata.getEntity().equalsIgnoreCase("AllSize")) {
							name = name + subString(diskdata.getSubentity()) + ";";
							allSize = allSize + df2.format(Double.parseDouble(diskdata.getThevalue())) + ";";
						} else if (diskdata.getEntity().equalsIgnoreCase("UsedSize")) {
							usedSize = usedSize + df2.format(Double.parseDouble(diskdata.getThevalue())) + ";";
						}
					}
				}
			}
			String arrName[] = name.split(";");
			String arrUtil[] = util.split(";");
			String arrAll[] = allSize.split(";");
			String arrUsed[] = usedSize.split(";");
			for(int i=0;i<arrName.length;i++){
				Vo vo = new Vo();
				vo.setObjectName(arrName[i]+"("+arrUtil[i]+"%)");
				vo.setObjectNumber1(arrAll[i]);
				vo.setObjectNumber2(arrUsed[i]);
				//SysLogger.info(vo.getObjectName()+"===="+vo.getObjectNumber2());
				flexDataList.add(vo);
			}
		}else{
			//�ɼ�������Ƿ���ģʽ
			I_HostCollectData hostCollectData = new HostCollectDataManager();
			//������Ϣ 
			Host host = (Host)PollingEngine.getInstance().getNodeByIP(ipAddress); 
			NodeUtil nodeUtil = new NodeUtil();
	       	NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(host);
			DiskInfoService diskInfoService = new DiskInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype());
			List<DiskInfo> alldiskInfo = diskInfoService.getCurrDiskInfo();
			for(int i=0; i<alldiskInfo.size(); i++){
				DiskInfo diskInfo = alldiskInfo.get(i);
				String sindex = diskInfo.getSindex();//��������
				String utilization = diskInfo.getUtilization();//������
				String allSize = diskInfo.getAllSize();//�ܴ�С
				String usedSize = diskInfo.getUsedSize();//��ʹ�õĴ�С
				Vo vo = new Vo();
				vo.setObjectName(sindex+"("+utilization+"%)");
				vo.setObjectNumber1(allSize);
				vo.setObjectNumber2(usedSize);
				flexDataList.add(vo);
			}
//			System.out.println(alldiskInfo);
		}
		return flexDataList;
	}
	/*
	 * ��ȡһ̨�豸��ĳ�����ڵ�����������
	 * */
	public ArrayList<FluxVo> getPortFluxByDay(String ipAddress, String subentity){
		SysLogger.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&2");
		System.out.println("==================="+ipAddress);
		I_HostCollectDataDay hostManager = new HostCollectDataDayManager();
		ArrayList<FluxVo> flexDataList = new ArrayList<FluxVo>();
		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //��ȡ������ʷ����ֵ
		Hashtable inutilHash = new Hashtable();
		try {
			inutilHash = hostManager.getmultiHisHdx(ipAddress, subentity, "InBandwidthUtilHdx", startTime, endTime, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Hashtable oututilHash = new Hashtable(); 
		try {
			oututilHash = hostManager.getmultiHisHdx(ipAddress, subentity, "OutBandwidthUtilHdx", startTime, endTime, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		List inutilList = new ArrayList();
		inutilList = (List) inutilHash.get("list");
		List oututilList = new ArrayList();
		oututilList = (List) oututilHash.get("list");
		if(inutilList!=null&&inutilList.size()>0&&oututilList!=null&&oututilList.size()>0){
			if(inutilList.size()>oututilList.size()){
				int num_min = oututilList.size();
				int num_max = inutilList.size();
				if(num_min==0){
					for(int i=0;i<num_max;i++){
						FluxVo fVo = new FluxVo();
						Vector inHdxVector = new Vector();
						inHdxVector = (Vector) inutilList.get(i);
						if(inHdxVector!=null){
							fVo.setDate((String) inHdxVector.get(1));
							fVo.setFluxin((String) inHdxVector.get(0));
							fVo.setFluxout("0");
							SysLogger.info(fVo.getDate()+"===="+fVo.getFluxin()+"==="+fVo.getFluxout());
							flexDataList.add(fVo);
						}
					}
				} else {
					for(int i=0;i<num_max;i++){
						FluxVo fVo = new FluxVo();
						Vector inHdxVector = new Vector();
						inHdxVector = (Vector) inutilList.get(i);
						String time = "";
						String fluxout = "";
						if(inHdxVector!=null){
							time = (String) inHdxVector.get(1);
							fVo.setDate(time);
							fVo.setFluxin((String) inHdxVector.get(0));
						}
						for(int j = 0; j < num_min; j++){
							Vector outHdxVector = new Vector();
							outHdxVector = (Vector) oututilList.get(j);
							if(outHdxVector!=null){
								String time1 = (String) outHdxVector.get(1);
								if(time1.equals(time)){
									fluxout = (String) outHdxVector.get(0);
									break;
								}
							}
						}
						if(!"".equals(fluxout)&&fluxout.indexOf("-")==-1){
							fVo.setFluxout(fluxout);
						} else {
							fVo.setFluxout("0");
						}
						SysLogger.info(fVo.getDate()+"===="+fVo.getFluxin()+"==="+fVo.getFluxout());
						flexDataList.add(fVo);
					}
				}
			} else if(inutilList.size()<oututilList.size()) {
				int num_max = oututilList.size();
				int num_min = inutilList.size();
				if(num_min==0){
					for(int i=0;i<num_max;i++){
						FluxVo fVo = new FluxVo();
						Vector outHdxVector = new Vector();
						outHdxVector = (Vector) oututilList.get(i);
						if(outHdxVector!=null){
							fVo.setDate((String) outHdxVector.get(1));
							fVo.setFluxin("0");
							fVo.setFluxout((String) outHdxVector.get(0));
							SysLogger.info(fVo.getDate()+"===="+fVo.getFluxin()+"==="+fVo.getFluxout());
							flexDataList.add(fVo);
						}
					}
				} else {
					for(int i=0;i<num_max;i++){
						FluxVo fVo = new FluxVo();
						Vector outHdxVector = new Vector();
						outHdxVector = (Vector) oututilList.get(i);
						String time = "";
						String fluxin = "";
						if(outHdxVector!=null){
							time = (String) outHdxVector.get(1);
							fVo.setDate(time);
							fVo.setFluxout((String) outHdxVector.get(0));
						}
						for(int j = 0; j < num_min; j++){
							Vector inHdxVector = new Vector();
							inHdxVector = (Vector) inutilList.get(j);
							if(inHdxVector!=null){
								String time1 = (String) inHdxVector.get(1);
								if(time1.equals(time)){
									fluxin = (String) inHdxVector.get(0);
									break;
								}
							}
						}
						if(!"".equals(fluxin)&&fluxin.indexOf("-")==-1){
							fVo.setFluxin(fluxin);
						} else {
							fVo.setFluxin("0");
						}
						SysLogger.info(fVo.getDate()+"===="+fVo.getFluxin()+"==="+fVo.getFluxout());
						flexDataList.add(fVo);
					}
				}
			} else {
				int num = (inutilList.size()>oututilList.size()?oututilList.size():inutilList.size());
				for(int i=0;i<num;i++){
					FluxVo fVo = new FluxVo();
					Vector inHdxVector = new Vector();
					inHdxVector = (Vector) inutilList.get(i);
					Vector outHdxVector = new Vector();
					outHdxVector = (Vector) oututilList.get(i);
					if(inHdxVector!=null&&outHdxVector!=null){
						fVo.setDate((String) inHdxVector.get(1));
						fVo.setFluxin((String) inHdxVector.get(0));
						fVo.setFluxout((String) outHdxVector.get(0));
						SysLogger.info(fVo.getDate()+"===="+fVo.getFluxin()+"==="+fVo.getFluxout());
						flexDataList.add(fVo);
					}
				}
			}
			
		}
		System.out.println("==================="+flexDataList.size());
		return flexDataList;
		
	}
	/*
	 * ��ȡһ̨�豸��ĳ������ָ��ʱ��ε��������
	 * */
	public ArrayList<FluxVo> getPortFluxByDate(
			String ipAddress,
			String subentity,
			String dateStr,
			String time){
		I_HostCollectDataDay hostManager = new HostCollectDataDayManager();
		ArrayList<FluxVo> flexDataList = new ArrayList<FluxVo>();
		String datestr[] = dateStr.split("=");
		SysLogger.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&3");
		//System.out.println("----------"+datestr.length);
		if(datestr!=null&&datestr.length==2){
			if(java.sql.Date.valueOf(datestr[0]).after(java.sql.Date.valueOf(datestr[1]))){ 
				System.out.println("��ʼ���ڴ��ڽ�ֹ����!");
		    } else { 
            	String starttime = datestr[0] + " 00:00:00";
		        String endTime = datestr[1] + " 23:59:59";
                //��ȡ������ʷ����ƽ��ֵ
				Hashtable inutilHash = new Hashtable();
				try {
					inutilHash = hostManager.getmultiHisHdx(ipAddress, subentity, "InBandwidthUtilHdx", starttime, endTime, time);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable oututilHash = new Hashtable();
				try {
					oututilHash = hostManager.getmultiHisHdx(ipAddress, subentity, "OutBandwidthUtilHdx", starttime, endTime, time);
				} catch (Exception e) {
					e.printStackTrace();
				}
				List inutilList = new ArrayList();
				inutilList = (List) inutilHash.get("list");
				List oututilList = new ArrayList();
				oututilList = (List) oututilHash.get("list");
				if(inutilList!=null&&inutilList.size()>0&&oututilList!=null&&oututilList.size()>0){
					if(inutilList.size()>oututilList.size()){
						int num_min = oututilList.size();
						int num_max = inutilList.size();
						if(num_min==0){
							for(int i=0;i<num_max;i++){
								FluxVo fVo = new FluxVo();
								Vector inHdxVector = new Vector();
								inHdxVector = (Vector) inutilList.get(i);
								if(inHdxVector!=null){
									fVo.setDate((String) inHdxVector.get(1));
									fVo.setFluxin((String) inHdxVector.get(0));
									fVo.setFluxout("0");
									flexDataList.add(fVo);
								}
							}
						} else {
							for(int i=0;i<num_max;i++){
								FluxVo fVo = new FluxVo();
								Vector inHdxVector = new Vector();
								inHdxVector = (Vector) inutilList.get(i);
								String time0 = "";
								String fluxout = "";
								if(inHdxVector!=null){
									time0 = (String) inHdxVector.get(1);
									fVo.setDate(time0);
									fVo.setFluxin((String) inHdxVector.get(0));
								}
								for(int j = 0; j < num_min; j++){
									Vector outHdxVector = new Vector();
									outHdxVector = (Vector) oututilList.get(j);
									if(outHdxVector!=null){
										String time1 = (String) outHdxVector.get(1);
										if(time1.equals(time0)){
											fluxout = (String) outHdxVector.get(0);
											break;
										}
									}
								}
								if(!"".equals(fluxout)&&fluxout.indexOf("-")==-1){
									fVo.setFluxout(fluxout);
								} else {
									fVo.setFluxout("0");
								}
								flexDataList.add(fVo);
							}
						}
					} else if(inutilList.size()<oututilList.size()) {
						int num_max = oututilList.size();
						int num_min = inutilList.size();
						if(num_min==0){
							for(int i=0;i<num_max;i++){
								FluxVo fVo = new FluxVo();
								Vector outHdxVector = new Vector();
								outHdxVector = (Vector) oututilList.get(i);
								if(outHdxVector!=null){
									fVo.setDate((String) outHdxVector.get(1));
									fVo.setFluxin("0");
									fVo.setFluxout((String) outHdxVector.get(0));
									flexDataList.add(fVo);
								}
							}
						} else {
							for(int i=0;i<num_max;i++){
								FluxVo fVo = new FluxVo();
								Vector outHdxVector = new Vector();
								outHdxVector = (Vector) oututilList.get(i);
								String time0 = "";
								String fluxin = "";
								if(outHdxVector!=null){
									time0 = (String) outHdxVector.get(1);
									fVo.setDate(time0);
									fVo.setFluxout((String) outHdxVector.get(0));
								}
								for(int j = 0; j < num_min; j++){
									Vector inHdxVector = new Vector();
									inHdxVector = (Vector) inutilList.get(j);
									if(inHdxVector!=null){
										String time1 = (String) inHdxVector.get(1);
										if(time1.equals(time0)){
											fluxin = (String) inHdxVector.get(0);
											break;
										}
									}
								}
								if(!"".equals(fluxin)&&fluxin.indexOf("-")==-1){
									fVo.setFluxin(fluxin);
								} else {
									fVo.setFluxin("0");
								}
								flexDataList.add(fVo);
							}
						}
					} else {
						int num = (inutilList.size()>oututilList.size()?oututilList.size():inutilList.size());
						for(int i=0;i<num;i++){
							FluxVo fVo = new FluxVo();
							Vector inHdxVector = new Vector();
							inHdxVector = (Vector) inutilList.get(i);
							Vector outHdxVector = new Vector();
							outHdxVector = (Vector) oututilList.get(i);
							if(inHdxVector!=null&&outHdxVector!=null){
								fVo.setDate((String) inHdxVector.get(1));
								fVo.setFluxin((String) inHdxVector.get(0));
								fVo.setFluxout((String) outHdxVector.get(0));
								flexDataList.add(fVo);
							}
						}
					}
					
				}
		    } 
		}
		return flexDataList;
	}
	/*
	 * ��ȡһ̨�豸��ĳ������ָ��ʱ��ε������С����ǰ��ƽ���������
	 * */
	public String getAvgPortFluxByDate(
			String ipAddress,
			String subentity,
			String dateStr,
			String time){
		I_HostCollectDataDay hostManager = new HostCollectDataDayManager();
		String date[] = dateStr.split("=");
		String startTime = date[0] + " 00:00:00";
		String endTime = date[1] + " 23:59:59";
        //��ȡ������ʷ����ƽ��ֵ
//		SysLogger.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&4");
		Hashtable inutilHash = new Hashtable();
		try {
			inutilHash = hostManager.getmultiHisHdx(ipAddress, subentity, "InBandwidthUtilHdx", startTime, endTime, time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Hashtable oututilHash = new Hashtable();
		try {
			oututilHash = hostManager.getmultiHisHdx(ipAddress, subentity, "OutBandwidthUtilHdx", startTime, endTime, time);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		Hashtable hash = new Hashtable();
		String inAvgutil = (String) inutilHash.get("avgput");
		String outAvgutil = (String) oututilHash.get("avgput");
		String inMaxutil = (String) inutilHash.get("max");
		String outMaxutil = (String) oututilHash.get("max");
		String inMinutil = (String) inutilHash.get("min");
		String outMinutil = (String) oututilHash.get("min");
		String inTemputil = (String) inutilHash.get("temp");
		String outTemputil = (String) oututilHash.get("temp");
//		hash.put("in", inTemputil+":"+inMaxutil+":"+inMinutil+":"+inAvgutil);
//		hash.put("out", outTemputil+":"+outMaxutil+":"+outMinutil+":"+outAvgutil);
//		System.out.println("inAvgutil:"+inAvgutil+"===================outAvgutil:"+outAvgutil);
		return "���"+":"+inTemputil+"kb/s:"+inMaxutil+"kb/s:"+inMinutil+"kb/s:"+inAvgutil+"kb/s,����"+":"+outTemputil+"kb/s:"+outMaxutil+"kb/s:"+outMinutil+"kb/s:"+outAvgutil+"kb/s";
		
	}
	//��ȡ�˿���ϸ��Ϣ
	public String getPortInfo(String ip,String index){
		I_HostLastCollectData hostlastmanager=new HostLastCollectDataManager();	
		Hashtable hash = new Hashtable();
		String[] netIfdetail={"index","ifDescr","ifname","ifType","ifMtu","ifSpeed","ifPhysAddress","ifOperStatus"};
		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		try {
			hash = hostlastmanager.getIfdetail_share(ip,index,netIfdetail,startTime,endTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String str = "";
		for(int i=0 ; i<netIfdetail.length ; i++){
			String value = "";
			if(hash.get(netIfdetail[i]) != null) {
				value = (String)hash.get(netIfdetail[i]);
				if(i==7){
				    String url = "";
                    if(value.equals("up")){
                        url = "/afunms/resource/image/topo/up.gif";
                    } else if(value.equals("down")){
                        url = "/afunms/resource/image/topo/down.gif";
                    } else {
                        url = "/afunms/resource/image/topo/testing.gif";
                    }
                    value = url;
				}
				str = str + value +";";
			}
		}
		//System.out.println("======================"+str);
		return str;
		
	}
    //������ݰ�
	public ArrayList<Vo> getInPacks(String ip,String index,String dateStr,String time){
		I_HostCollectData hostmanager=new HostCollectDataManager();
		ArrayList<Vo> flexDataList = new ArrayList<Vo>();
		String date[] = dateStr.split("=");
		String startTime = date[0] + " 00:00:00";
		String endTime = date[1] + " 23:59:59";
		String[] banden2 = {"ifInBroadcastPkts","ifInMulticastPkts"};
		String[] bandch2 = {"��ڹ㲥���ݰ�","��ڶಥ���ݰ�"};	
		try {
			Hashtable[] bandhashtable2 = hostmanager.getIfBand_InPacks(ip,index,banden2,bandch2,startTime,endTime,time);
			String[] keys = (String[])bandhashtable2[0].get("key");
			if(keys != null && keys.length==2){
				Vector vector0=(Vector)(bandhashtable2[0].get(keys[0]));
				Vector vector1=(Vector)(bandhashtable2[0].get(keys[1]));
				if(vector0.size()>vector1.size()){
					int num_max = vector0.size();
					int num_min = vector1.size();
					if(num_min==0){
						for(int i = 0; i < num_max; i++){
							Vo fVo = new Vo();
							Vector obj0 = (Vector)vector0.get(i);
							String time0 = (String)obj0.get(1);
							fVo.setObjectName(time0);
							fVo.setObjectNumber1((String)obj0.get(0));
							fVo.setObjectNumber2("0");
							flexDataList.add(fVo);
						}
					} else {
						for(int i = 0; i < num_max; i++){
							String objectNumber2 = "";
							Vo fVo = new Vo();
							Vector obj0 = (Vector)vector0.get(i);
							String time0 = (String)obj0.get(1);
							fVo.setObjectName(time0);
							fVo.setObjectNumber1((String)obj0.get(0));
							for(int j = 0; j < num_min; j++){
								Vector obj1 = (Vector)vector1.get(j);
								String time1 = (String)obj1.get(1);
								if(time1.equals(time0)){
									objectNumber2 = (String)obj1.get(0);
									break;
								}
							}
							if(!"".equals(objectNumber2)){
								if(objectNumber2.indexOf("-")!=-1){
									fVo.setObjectNumber2("0");
								} else {
									fVo.setObjectNumber2(objectNumber2);
								}
							} else {
								fVo.setObjectNumber2("0");
							}
							flexDataList.add(fVo);
						}
					}
					
				} else if(vector0.size()<vector1.size()){
					int num_max = vector1.size();
					int num_min = vector0.size();
					if(num_min==0){
						for(int i = 0; i < num_max; i++){
							Vo fVo = new Vo();
							Vector obj1 = (Vector)vector1.get(i);
							String time1 = (String)obj1.get(1);
							fVo.setObjectName(time1);
							fVo.setObjectNumber1("0");
							fVo.setObjectNumber2((String)obj1.get(0));
							flexDataList.add(fVo);
						}
					} else {
						for(int i = 0; i < num_max; i++){
							String objectNumber1 = "";
							Vo fVo = new Vo();
							Vector obj1 = (Vector)vector1.get(i);
							String time1 = (String)obj1.get(1);
							fVo.setObjectName(time1);
							fVo.setObjectNumber2((String)obj1.get(0));
							for(int j = 0; j < num_min; j++){
								Vector obj0 = (Vector)vector0.get(j);
								String time0 = (String)obj0.get(1);
								if(time1.equals(time0)){
									objectNumber1 = (String)obj0.get(0);
									break;
								}
							}
							if(!"".equals(objectNumber1)){
								if(objectNumber1.indexOf("-")!=-1){
									fVo.setObjectNumber1("0");
								} else {
									fVo.setObjectNumber1(objectNumber1);
								}
							} else {
								fVo.setObjectNumber1("0");
							}
							flexDataList.add(fVo);
						}
					}
				} else {
					int num = vector0.size()>vector1.size()?vector1.size():vector0.size();
					for(int j=0; j<num; j++){
						Vo fVo = new Vo();
						Vector obj0 = (Vector)vector0.get(j);
						Vector obj1 = (Vector)vector1.get(j);
						fVo.setObjectName((String)obj0.get(1));
						fVo.setObjectNumber1((String)obj0.get(0));
						fVo.setObjectNumber2((String)obj1.get(0));
						flexDataList.add(fVo);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return flexDataList;
		
	}
    //�������ݰ�
	public ArrayList<Vo> getOutPacks(String ip,String index,String dateStr,String time){
		I_HostCollectData hostmanager=new HostCollectDataManager();
		ArrayList<Vo> flexDataList = new ArrayList<Vo>();
		String date[] = dateStr.split("=");
		String startTime = date[0] + " 00:00:00";
		String endTime = date[1] + " 23:59:59";
		String[] banden2 = {"ifOutBroadcastPkts","ifOutMulticastPkts"};
		String[] bandch2 = {"���ڹ㲥���ݰ�","���ڶಥ���ݰ�"};
		try {
			Hashtable[] bandhashtable2 = hostmanager.getIfBand_OutPacks(ip,index,banden2,bandch2,startTime,endTime,time);
			String[] keys = (String[])bandhashtable2[0].get("key");
			if(keys != null && keys.length==2){
				Vector vector0=(Vector)(bandhashtable2[0].get(keys[0]));
				Vector vector1=(Vector)(bandhashtable2[0].get(keys[1]));
				if(vector0.size()>vector1.size()){
					int num_max = vector0.size();
					int num_min = vector1.size();
					
					if(num_min==0){
						for(int i = 0; i < num_max; i++){
							Vo fVo = new Vo();
							Vector obj0 = (Vector)vector0.get(i);
							String time0 = (String)obj0.get(1);
							fVo.setObjectName(time0);
							fVo.setObjectNumber1((String)obj0.get(0));
							fVo.setObjectNumber2("0");
							flexDataList.add(fVo);
						}
					} else {
						for(int i = 0; i < num_max; i++){
							String objectNumber2 = "";
							Vo fVo = new Vo();
							Vector obj0 = (Vector)vector0.get(i);
							String time0 = (String)obj0.get(1);
							fVo.setObjectName(time0);
							fVo.setObjectNumber1((String)obj0.get(0));
							for(int j = 0; j < num_min; j++){
								Vector obj1 = (Vector)vector1.get(j);
								String time1 = (String)obj1.get(1);
								if(time1.equals(time0)){
									objectNumber2 = (String)obj1.get(0);
									break;
								}
							}
							if(!"".equals(objectNumber2)){
								if(objectNumber2.indexOf("-")!=-1){
									fVo.setObjectNumber2("0");
								} else {
									fVo.setObjectNumber2(objectNumber2);
								}
							} else {
								fVo.setObjectNumber2("0");
							}
							flexDataList.add(fVo);
						}
					}
					
				} else if(vector0.size()<vector1.size()){
					int num_max = vector1.size();
					int num_min = vector0.size();
					if(num_min==0){
						for(int i = 0; i < num_max; i++){
							Vo fVo = new Vo();
							Vector obj1 = (Vector)vector1.get(i);
							String time1 = (String)obj1.get(1);
							fVo.setObjectName(time1);
							fVo.setObjectNumber1("0");
							fVo.setObjectNumber2((String)obj1.get(0));
							flexDataList.add(fVo);
						}
					} else {
						for(int i = 0; i < num_max; i++){
							String objectNumber1 = "";
							Vo fVo = new Vo();
							Vector obj1 = (Vector)vector1.get(i);
							String time1 = (String)obj1.get(1);
							fVo.setObjectName(time1);
							fVo.setObjectNumber2((String)obj1.get(0));
							for(int j = 0; j < num_min; j++){
								Vector obj0 = (Vector)vector0.get(j);
								String time0 = (String)obj0.get(1);
								if(time1.equals(time0)){
									objectNumber1 = (String)obj0.get(0);
									break;
								}
							}
							if(!"".equals(objectNumber1)){
								if(objectNumber1.indexOf("-")!=-1){
									fVo.setObjectNumber1("0");
								} else {
									fVo.setObjectNumber1(objectNumber1);
								}
							} else {
								fVo.setObjectNumber1("0");
							}
							flexDataList.add(fVo);
						}
					}
				} else {
					int num = vector0.size()>vector1.size()?vector1.size():vector0.size();
					for(int j=0; j<num; j++){
						Vo fVo = new Vo();
						Vector obj0 = (Vector)vector0.get(j);
						Vector obj1 = (Vector)vector1.get(j);
						fVo.setObjectName((String)obj0.get(1));
						fVo.setObjectNumber1((String)obj0.get(0));
						fVo.setObjectNumber2((String)obj1.get(0));
						flexDataList.add(fVo);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flexDataList;
		
	}
	//�˿ڶ�����
	public ArrayList<Vo> getDiscardsPerc(String ip,String index,String dateStr,String time){
		I_HostCollectData hostmanager=new HostCollectDataManager();
		ArrayList<Vo> flexDataList = new ArrayList<Vo>();
		String date[] = dateStr.split("=");
		String startTime = date[0] + " 00:00:00";
		String endTime = date[1] + " 23:59:59";
		String[] banden2 = {"InDiscardsPerc","OutDiscardsPerc"};
		String[] bandch2 = {"��ڶ�����","���ڶ�����"};
		String table = "";
		if(time.equals("day")){
			table = "dcardpercday";
		} else if(time.equals("hour")){
			table = "dcardperchour";
		} else {
			table = "discardsperc";
		}
		try {
			Hashtable[] bandhashtable = hostmanager.getDiscardsPerc(ip,index,banden2,bandch2,startTime,endTime,table);
			String[] keys = (String[])bandhashtable[0].get("key");
			if(keys != null && keys.length==2){
				Vector vector0=(Vector)(bandhashtable[0].get(keys[0]));
				Vector vector1=(Vector)(bandhashtable[0].get(keys[1]));
				if(vector0.size()>vector1.size()){
					int num_max = vector0.size();
					int num_min = vector1.size();
					if(num_min==0){
						for(int i = 0; i < num_max; i++){
							Vo fVo = new Vo();
							Vector obj0 = (Vector)vector0.get(i);
							String time0 = (String)obj0.get(1);
							fVo.setObjectName(time0);
							fVo.setObjectNumber1((String)obj0.get(0));
							fVo.setObjectNumber2("0.0");
							flexDataList.add(fVo);
						}
					} else {
						for(int i = 0; i < num_max; i++){
							String objectNumber2 = "";
							Vo fVo = new Vo();
							Vector obj0 = (Vector)vector0.get(i);
							String time0 = (String)obj0.get(1);
							fVo.setObjectName(time0);
							fVo.setObjectNumber1((String)obj0.get(0));
							for(int j = 0; j < num_min; j++){
								Vector obj1 = (Vector)vector1.get(j);
								String time1 = (String)obj1.get(1);
								if(time1.equals(time0)){
									objectNumber2 = (String)obj1.get(0);
									break;
								}
							}
							if(!"".equals(objectNumber2)){
								if(objectNumber2.indexOf("-")!=-1){
									fVo.setObjectNumber2("0.0");
								} else {
									fVo.setObjectNumber2(objectNumber2);
								}
							} else {
								fVo.setObjectNumber2("0.0");
							}
							flexDataList.add(fVo);
						}
					}
					
				} else if(vector0.size()<vector1.size()){
					int num_max = vector1.size();
					int num_min = vector0.size();
					if(num_min==0){
						for(int i = 0; i < num_max; i++){
							Vo fVo = new Vo();
							Vector obj1 = (Vector)vector1.get(i);
							String time1 = (String)obj1.get(1);
							fVo.setObjectName(time1);
							fVo.setObjectNumber1("0.0");
							fVo.setObjectNumber2((String)obj1.get(0));
							flexDataList.add(fVo);
						}
					} else {
						for(int i = 0; i < num_max; i++){
							String objectNumber1 = "";
							Vo fVo = new Vo();
							Vector obj1 = (Vector)vector1.get(i);
							String time1 = (String)obj1.get(1);
							fVo.setObjectName(time1);
							fVo.setObjectNumber2((String)obj1.get(0));
							for(int j = 0; j < num_min; j++){
								Vector obj0 = (Vector)vector0.get(j);
								String time0 = (String)obj0.get(1);
								if(time1.equals(time0)){
									objectNumber1 = (String)obj0.get(0);
									break;
								}
							}
							if(!"".equals(objectNumber1)){
								if(objectNumber1.indexOf("-")!=-1){
									fVo.setObjectNumber1("0.0");
								} else {
									fVo.setObjectNumber1(objectNumber1);
								}
							} else {
								fVo.setObjectNumber1("0.0");
							}
							flexDataList.add(fVo);
						}
					}
				} else {
					int num = vector0.size()>vector1.size()?vector1.size():vector0.size();
					for(int j=0; j<num; j++){
						Vo fVo = new Vo();
						Vector obj0 = (Vector)vector0.get(j);
						Vector obj1 = (Vector)vector1.get(j);
						fVo.setObjectName((String)obj0.get(1));
						fVo.setObjectNumber1((String)obj0.get(0));
						fVo.setObjectNumber2((String)obj1.get(0));
						flexDataList.add(fVo);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flexDataList;
		
	}
    //�˿ڴ�����
	public ArrayList<Vo> getErrorsperc(String ip,String index,String dateStr,String time){
		I_HostCollectData hostmanager=new HostCollectDataManager();
		ArrayList<Vo> flexDataList = new ArrayList<Vo>();
		String date[] = dateStr.split("=");
		String startTime = date[0] + " 00:00:00";
		String endTime = date[1] + " 23:59:59";
		String[] banden1 = {"InErrorsPerc","OutErrorsPerc"};
		String[] bandch1 = {"��ڴ�����","���ڴ�����"};
		String table = "";
		if(time.equals("day")){
			table = "errpercday";
		} else if(time.equals("hour")){
			table = "errperchour";
		} else {
			table = "errorsperc";
		}
		try {
			Hashtable[] bandhashtable1 = hostmanager.getErrorsPerc(ip,index,banden1,bandch1,startTime,endTime,table);
			String[] keys = (String[])bandhashtable1[0].get("key");
			if(keys != null && keys.length==2){
				Vector vector0=(Vector)(bandhashtable1[0].get(keys[0]));
				Vector vector1=(Vector)(bandhashtable1[0].get(keys[1]));
				if(vector0.size()>vector1.size()){
					int num_max = vector0.size();
					int num_min = vector1.size();
					if(num_min==0){
						for(int i = 0; i < num_max; i++){
							Vo fVo = new Vo();
							Vector obj0 = (Vector)vector0.get(i);
							String time0 = (String)obj0.get(1);
							fVo.setObjectName(time0);
							fVo.setObjectNumber1((String)obj0.get(0));
							fVo.setObjectNumber2("0.0");
							flexDataList.add(fVo);
						}
					} else {
						for(int i = 0; i < num_max; i++){
							String objectNumber2 = "";
							Vo fVo = new Vo();
							Vector obj0 = (Vector)vector0.get(i);
							String time0 = (String)obj0.get(1);
							fVo.setObjectName(time0);
							fVo.setObjectNumber1((String)obj0.get(0));
							for(int j = 0; j < num_min; j++){
								Vector obj1 = (Vector)vector1.get(j);
								String time1 = (String)obj1.get(1);
								if(time1.equals(time0)){
									objectNumber2 = (String)obj1.get(0);
									break;
								}
							}
							if(!"".equals(objectNumber2)){
								if(objectNumber2.indexOf("-")!=-1){
									fVo.setObjectNumber2("0.0");
								} else {
									fVo.setObjectNumber2(objectNumber2);
								}
							} else {
								fVo.setObjectNumber2("0.0");
							}
							flexDataList.add(fVo);
						}
					}
				} else if(vector0.size()<vector1.size()){
					int num_max = vector1.size();
					int num_min = vector0.size();
					if(num_min==0){
						for(int i = 0; i < num_max; i++){
							Vo fVo = new Vo();
							Vector obj1 = (Vector)vector1.get(i);
							String time1 = (String)obj1.get(1);
							fVo.setObjectName(time1);
							fVo.setObjectNumber1("0.0");
							fVo.setObjectNumber2((String)obj1.get(0));
							flexDataList.add(fVo);
						}
					} else {
						for(int i = 0; i < num_max; i++){
							String objectNumber1 = "";
							Vo fVo = new Vo();
							Vector obj1 = (Vector)vector1.get(i);
							String time1 = (String)obj1.get(1);
							fVo.setObjectName(time1);
							fVo.setObjectNumber2((String)obj1.get(0));
							for(int j = 0; j < num_min; j++){
								Vector obj0 = (Vector)vector0.get(j);
								String time0 = (String)obj0.get(1);
								if(time1.equals(time0)){
									objectNumber1 = (String)obj0.get(0);
									break;
								}
							}
							if(!"".equals(objectNumber1)){
								if(objectNumber1.indexOf("-")!=-1){
									fVo.setObjectNumber1("0.0");
								} else {
									fVo.setObjectNumber1(objectNumber1);
								}
							} else {
								fVo.setObjectNumber1("0.0");
							}
							flexDataList.add(fVo);
						}	
					}
				} else {
					int num = vector0.size()>vector1.size()?vector1.size():vector0.size();
					for(int j=0; j<num; j++){
						Vo fVo = new Vo();
						Vector obj0 = (Vector)vector0.get(j);
						Vector obj1 = (Vector)vector1.get(j);
						fVo.setObjectName((String)obj0.get(1));
						fVo.setObjectNumber1((String)obj0.get(0));
						fVo.setObjectNumber2((String)obj1.get(0));
						flexDataList.add(fVo);
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flexDataList;
	}
	//�˿��շ���Ϣ��
	public ArrayList<Vo> getIfBand_Packs(String ip,String index,String dateStr,String time){
		I_HostCollectData hostmanager=new HostCollectDataManager();
		ArrayList<Vo> flexDataList = new ArrayList<Vo>();
		String date[] = dateStr.split("=");
		String startTime = date[0] + " 00:00:00";
		String endTime = date[1] + " 23:59:59";
		String[] banden2 = {"InCastPkts","OutCastPkts"};
		String[] bandch2 = {"������ݰ�","�������ݰ�"};	
		try {
			Hashtable[] bandhashtable = hostmanager.getIfBand_Packs(ip,index,banden2,bandch2,startTime,endTime,time);
			String[] keys = (String[])bandhashtable[0].get("key");
			if(keys != null && keys.length==2){
				Vector vector0=(Vector)(bandhashtable[0].get(keys[0]));
				Vector vector1=(Vector)(bandhashtable[0].get(keys[1]));
				if(vector0.size()>vector1.size()){
					int num_max = vector0.size();
					int num_min = vector1.size();
					if(num_min==0){
						for(int i = 0; i < num_max; i++){
							Vo fVo = new Vo();
							Vector obj0 = (Vector)vector0.get(i);
							String time0 = (String)obj0.get(1);
							fVo.setObjectName(time0);
							fVo.setObjectNumber1((String)obj0.get(0));
							fVo.setObjectNumber2("0");
							flexDataList.add(fVo);
						}
					} else {
						for(int i = 0; i < num_max; i++){
							String objectNumber2 = "";
							Vo fVo = new Vo();
							Vector obj0 = (Vector)vector0.get(i);
							String time0 = (String)obj0.get(1);
							fVo.setObjectName(time0);
							fVo.setObjectNumber1((String)obj0.get(0));
							for(int j = 0; j < num_min; j++){
								Vector obj1 = (Vector)vector1.get(j);
								String time1 = (String)obj1.get(1);
								if(time1.equals(time0)){
									objectNumber2 = (String)obj1.get(0);
									break;
								}
							}
							if(!"".equals(objectNumber2)){
								if(objectNumber2.indexOf("-")!=-1){
									fVo.setObjectNumber2("0");
								} else {
									fVo.setObjectNumber2(objectNumber2);
								}
							} else {
								fVo.setObjectNumber2("0");
							}
							flexDataList.add(fVo);
						}
					}
					
				} else if(vector0.size()<vector1.size()){
					int num_max = vector1.size();
					int num_min = vector0.size();
					if(num_min==0){
						for(int i = 0; i < num_max; i++){
							Vo fVo = new Vo();
							Vector obj1 = (Vector)vector1.get(i);
							String time1 = (String)obj1.get(1);
							fVo.setObjectName(time1);
							fVo.setObjectNumber1("0");
							fVo.setObjectNumber2((String)obj1.get(0));
							flexDataList.add(fVo);
						}
					} else {
						for(int i = 0; i < num_max; i++){
							String objectNumber1 = "";
							Vo fVo = new Vo();
							Vector obj1 = (Vector)vector1.get(i);
							String time1 = (String)obj1.get(1);
							fVo.setObjectName(time1);
							fVo.setObjectNumber2((String)obj1.get(0));
							for(int j = 0; j < num_min; j++){
								Vector obj0 = (Vector)vector0.get(j);
								String time0 = (String)obj0.get(1);
								if(time1.equals(time0)){
									objectNumber1 = (String)obj0.get(0);
									break;
								}
							}
							if(!"".equals(objectNumber1)){
								if(objectNumber1.indexOf("-")!=-1){
									fVo.setObjectNumber1("0");
								} else {
									fVo.setObjectNumber1(objectNumber1);
								}
							} else {
								fVo.setObjectNumber1("0");
							}
							flexDataList.add(fVo);
						}
					}
				} else {
					int num = vector0.size()>vector1.size()?vector1.size():vector0.size();
					for(int j=0; j<num; j++){
						Vo fVo = new Vo();
						Vector obj0 = (Vector)vector0.get(j);
						Vector obj1 = (Vector)vector1.get(j);
						fVo.setObjectName((String)obj0.get(1));
						fVo.setObjectNumber1((String)obj0.get(0));
						fVo.setObjectNumber2((String)obj1.get(0));
						flexDataList.add(fVo);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println("============"+flexDataList.size());
		return flexDataList;
	}
	
	//��ȡ�¼��б�
	public ArrayList<EventVo> getEventList(){
		ArrayList<EventVo> flexDataList = new ArrayList<EventVo>();
		List rpceventlist = new ArrayList();
		session = FlexContext.getFlexSession();
		User current_user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
		String bids = current_user.getBusinessids();
		if(current_user.getRole()==0||current_user.getRole()==1){
			bids = "-1";
		}
		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		EventListDao eventdao = new EventListDao();
		String timeFormat = "MM-dd HH:mm:ss";
		java.text.SimpleDateFormat timeFormatter = new java.text.SimpleDateFormat(timeFormat);
		try {
			rpceventlist = eventdao.getQuery(startTime, endTime, "99", "99", bids, -1);
			if(rpceventlist != null && rpceventlist.size()>0){
				for(int i=0;i<rpceventlist.size();i++){
					EventVo Vo = new EventVo();
					EventList event = (EventList)rpceventlist.get(i);
					Vo.setContent(event.getContent());
					Vo.setEventlocation(event.getEventlocation());
					Date d2 = event.getRecordtime().getTime();
  			  		String time = timeFormatter.format(d2);
					Vo.setRecordtime(time);
					String level = String.valueOf(event.getLevel1());
					if("0".equals(level)){
				  		level="��ʾ��Ϣ";
				  	}
					if("1".equals(level)){
				  		level="��ͨ�澯";
				  	}
				  	if("2".equals(level)){
				  		level="���ظ澯";
				  	}
				  	if("3".equals(level)){
				  		level="�����澯";
				  	}
					Vo.setLevel1(level);
					flexDataList.add(Vo);
					if(i==7)break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			eventdao.close();
		}
		return flexDataList;
	}
	
	//��ȡtomcat��ͨ������
	public ArrayList<FlexVo> getTomcatPingByDay(String ipAddress,String time){
		TomcatManager tomcatManager = new TomcatManager();
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Hashtable pingHash = new Hashtable();
        try {
        	pingHash = tomcatManager.getCategory(ipAddress,"TomcatPing","ConnectUtilization",startTime,endTime,time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List pingList = new ArrayList();
		pingList = (List) pingHash.get("list");
		if(pingList!=null&&pingList.size()>0){
			int num = pingList.size();
			for(int i=0;i<num;i++){
				FlexVo fVo = new FlexVo();
				Vector pingVector = new Vector();
				pingVector = (Vector) pingList.get(i);
				if(pingVector!=null){
					fVo.setObjectName((String) pingVector.get(1));
					fVo.setObjectNumber((String) pingVector.get(0));
					flexDataList.add(fVo);
				}
			}
		}
		return flexDataList;
		
	}
	
	public ArrayList<FlexVo> getTomcatPingByDate(String ipAddress,String dateStr,String time){
		TomcatManager tomcatManager = new TomcatManager();
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		String datestr[] = dateStr.split("=");
		if(datestr.length==2){
			if(java.sql.Date.valueOf(datestr[0]).after(java.sql.Date.valueOf(datestr[1]))){ 
				System.out.println("��ʼ���ڴ��ڽ�ֹ����!");
		    } else {
            	String starttime = datestr[0] + " 00:00:00";
		        String endTime = datestr[1] + " 23:59:59";
		        Hashtable pingHash = new Hashtable();
		        try {
		        	pingHash = tomcatManager.getCategory(ipAddress,"TomcatPing","ConnectUtilization",starttime,endTime,time);
				} catch (Exception e) {
					e.printStackTrace();
				}
				List pingList = new ArrayList();
				pingList = (List) pingHash.get("list");
				if(pingList!=null&&pingList.size()>0){
					int num = pingList.size();
					for(int i=0;i<num;i++){
						FlexVo fVo = new FlexVo();
						Vector pingVector = new Vector();
						pingVector = (Vector) pingList.get(i);
						if(pingVector!=null){
							fVo.setObjectName((String) pingVector.get(1));
							fVo.setObjectNumber((String) pingVector.get(0));
							flexDataList.add(fVo);
						}
					}
				}
		    }
		}
		return flexDataList;
	}
	
	public String getAvgTomcatPingByDate(String ipAddress,String dateStr,String time){
		TomcatManager tomcatManager = new TomcatManager();
		String date[] = dateStr.split("=");
		String startTime = date[0] + " 00:00:00";
		String endTime = date[1] + " 23:59:59";
        Hashtable pingHash = new Hashtable();
        try {
        	pingHash = tomcatManager.getCategory(ipAddress,"TomcatPing","ConnectUtilization",startTime,endTime,time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String pingValue = (String)pingHash.get("avgpingcon");
		return pingValue;
	}
//	��ȡtomcatJVM�ڴ�����������
	public ArrayList<FlexVo> getTomcatJvmByDay(String ipAddress,String time){
		TomcatManager tomcatManager = new TomcatManager();
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Hashtable pingHash = new Hashtable();
        try {
        	pingHash = tomcatManager.getCategory(ipAddress,"tomcat_jvm","jvm_utilization",startTime,endTime,time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List pingList = new ArrayList();
		pingList = (List) pingHash.get("list");
		if(pingList!=null&&pingList.size()>0){
			int num = pingList.size();
			for(int i=0;i<num;i++){
				FlexVo fVo = new FlexVo();
				Vector pingVector = new Vector();
				pingVector = (Vector) pingList.get(i);
				if(pingVector!=null){
					fVo.setObjectName((String) pingVector.get(1));
					fVo.setObjectNumber((String) pingVector.get(0));
					flexDataList.add(fVo);
				}
			}
		}
		return flexDataList;
		
	}
	
	public ArrayList<FlexVo> getTomcatJvmByDate(String ipAddress,String dateStr,String time){
		TomcatManager tomcatManager = new TomcatManager();
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		String datestr[] = dateStr.split("=");
		if(datestr.length==2){
			if(java.sql.Date.valueOf(datestr[0]).after(java.sql.Date.valueOf(datestr[1]))){ 
				System.out.println("��ʼ���ڴ��ڽ�ֹ����!");
		    } else {
            	String starttime = datestr[0] + " 00:00:00";
		        String endTime = datestr[1] + " 23:59:59";
		        Hashtable pingHash = new Hashtable();
		        try {
		        	pingHash = tomcatManager.getCategory(ipAddress,"tomcat_jvm","jvm_utilization",starttime,endTime,time);
				} catch (Exception e) {
					e.printStackTrace();
				}
				List pingList = new ArrayList();
				pingList = (List) pingHash.get("list");
				if(pingList!=null&&pingList.size()>0){
					int num = pingList.size();
					for(int i=0;i<num;i++){
						FlexVo fVo = new FlexVo();
						Vector pingVector = new Vector();
						pingVector = (Vector) pingList.get(i);
						if(pingVector!=null){
							fVo.setObjectName((String) pingVector.get(1));
							fVo.setObjectNumber((String) pingVector.get(0));
							flexDataList.add(fVo);
						}
					}
				}
		    }
		}
		return flexDataList;
	}
	
	public String getAvgTomcatJvmByDate(String ipAddress,String dateStr,String time){
		TomcatManager tomcatManager = new TomcatManager();
		String date[] = dateStr.split("=");
		String startTime = date[0] + " 00:00:00";
		String endTime = date[1] + " 23:59:59";
        Hashtable pingHash = new Hashtable();
        try {
        	pingHash = tomcatManager.getCategory(ipAddress,"tomcat_jvm","jvm_utilization",startTime,endTime,time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String pingValue = (String)pingHash.get("avg_tomcat_jvm");
		return pingValue;
	}
	
	//��ȡ���ݿ���ͨ����Ϣ
	public ArrayList<FlexVo> getDbPingByDay(String ipAddress,String time,String category){
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
      Hashtable ConnectUtilizationhash = new Hashtable();
        Pattern pattern=Pattern.compile("([^:]+):(\\d*)");
        Matcher m=pattern.matcher(ipAddress);
        String sid="";
        String ip="";
        if(m.find()){
        	ipAddress=m.group(1);
        	sid=m.group(2);
        	ip=ipAddress+":"+sid;
        }else{
        	ip=ipAddress;
        }		
        I_HostCollectData hostmanager=new HostCollectDataManager();
		try{
			ConnectUtilizationhash = hostmanager.getCategory(ip,category,"ConnectUtilization",startTime,endTime,time);
			List pingList = new ArrayList();
			pingList = (List) ConnectUtilizationhash.get("list");
			if(pingList!=null&&pingList.size()>0){
				int num = pingList.size();
				for(int i=0;i<num;i++){
					FlexVo fVo = new FlexVo();
					Vector pingVector = new Vector();
					pingVector = (Vector) pingList.get(i);
					if(pingVector!=null){
						fVo.setObjectName((String) pingVector.get(1));
						fVo.setObjectNumber((String) pingVector.get(0));
						flexDataList.add(fVo);
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
        
		return flexDataList;
	}
	public ArrayList<FlexVo> getDbPingByDate(String ipAddress,String dateStr,String time,String category){
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		String datestr[] = dateStr.split("=");
		if(datestr.length==2){
			if(java.sql.Date.valueOf(datestr[0]).after(java.sql.Date.valueOf(datestr[1]))){ 
				System.out.println("��ʼ���ڴ��ڽ�ֹ����!");
		    } else {
            	String starttime = datestr[0] + " 00:00:00";
		        String endTime = datestr[1] + " 23:59:59";
		        Hashtable ConnectUtilizationhash = new Hashtable();
				I_HostCollectData hostmanager=new HostCollectDataManager();
				try{
					ConnectUtilizationhash = hostmanager.getCategory(ipAddress,category,"ConnectUtilization",starttime,endTime,time);
					List pingList = new ArrayList();
					pingList = (List) ConnectUtilizationhash.get("list");
					if(pingList!=null&&pingList.size()>0){
						int num = pingList.size();
						for(int i=0;i<num;i++){
							FlexVo fVo = new FlexVo();
							Vector pingVector = new Vector();
							pingVector = (Vector) pingList.get(i);
							if(pingVector!=null){
								fVo.setObjectName((String) pingVector.get(1));
								fVo.setObjectNumber((String) pingVector.get(0));
								flexDataList.add(fVo);
							}
						}
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
		    }
		}
		return flexDataList;
	}
	public String getAvgDbPingByDate(String ipAddress,String dateStr,String time,String category){
		String date[] = dateStr.split("=");
		String startTime = date[0] + " 00:00:00";
		String endTime = date[1] + " 23:59:59";
		Hashtable ConnectUtilizationhash = new Hashtable();
		I_HostCollectData hostmanager=new HostCollectDataManager();
		try{
			ConnectUtilizationhash = hostmanager.getCategory(ipAddress,category,"ConnectUtilization",startTime,endTime,time);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		String avgping = (String) ConnectUtilizationhash.get("avgpingcon");
		return avgping;
	}
	
	//��ȡoracle��ռ���Ϣ
	public ArrayList<oraVo> getOraTabSpaceInfo(String ipAddress,String port,String name,String user,String pwd){
		ArrayList<oraVo> flexDataList = new ArrayList<oraVo>();
		Vector tableinfo_v = new Vector();
//		DBDao dao = new DBDao();
//		try{
//			tableinfo_v = dao.getOracleTableinfo(ipAddress,Integer.parseInt(port),name,user,pwd);
//		} catch(Exception e) {
//			e.printStackTrace();
//		} finally {
//			dao.close();
//		}
//		Hashtable alloracledata = ShareData.getAlloracledata();
//		Hashtable iporacledata = new Hashtable();
//		if(alloracledata != null && alloracledata.size()>0){
//			if(alloracledata.containsKey(ipAddress)){
//				iporacledata = (Hashtable)alloracledata.get(ipAddress);
//				if(iporacledata.containsKey("tableinfo_v")){
//					tableinfo_v = (Vector)iporacledata.get("tableinfo_v");
//				}
//			}
//			
//		}		
		//2010-HONGLI
		DBDao dao = new DBDao();
		IpTranslation tranfer = new IpTranslation();
		String hex = null;
		String ip = null;
		String sid = null;
		if(ipAddress.contains(":")){
			ip = ipAddress.split(":")[0];
			sid = ipAddress.split(":")[1];
			hex = tranfer.formIpToHex(ip);
		}
		String serverip = hex+":"+sid;
		try {
			tableinfo_v = dao.getOracle_nmsoraspaces(serverip);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		for(int i=0;i<tableinfo_v.size();i++){
			oraVo oravo = new oraVo();
			Hashtable ht = (Hashtable)tableinfo_v.get(i);
			String filename = ht.get("file_name").toString();
			String tablespace = ht.get("tablespace").toString();
			String size = ht.get("size_mb").toString();
			String free = ht.get("free_mb").toString();
			String percent = ht.get("percent_free").toString();
			String status = ht.get("status").toString();
			oravo.setFile_name(filename);
			oravo.setTablespace(tablespace);
			oravo.setSize(size);
			oravo.setFree(free);
			oravo.setPercent(percent);
			oravo.setStatus(status);
			flexDataList.add(oravo);
		}
		return flexDataList;
		
	}
	
	public ArrayList<FlexVo> getOraMem(String ipAddress,String port,String name,String user,String pwd,String category){
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		Hashtable memValue = new Hashtable();
		DecimalFormat df=new DecimalFormat("#.##");
		String[] sysItem1={"shared_pool","large_pool","DEFAULT_buffer_cache","java_pool"};
		String[] sysItemch1={"�����","���ͳ�","���������ٻ���","Java��"};
		String[] sysItem2={"aggregate_PGA_target_parameter","total_PGA_allocated","maximum_PGA_allocated"};
		String[] sysItemch2={"�ܼ�PGAĿ��","����ĵ�ǰPGA","��������PGA"};
		String[] sysItem = null;
		String[] sysItemch = null;
		if("SGA".equals(category)){
			sysItem = sysItem1;
			sysItemch = sysItemch1;
		} else if("PGA".equals(category)){
			sysItem = sysItem2;
			sysItemch = sysItemch2;
		}

//		Hashtable alloracledata = ShareData.getAlloracledata();
//		Hashtable iporacledata = new Hashtable();
//		if(alloracledata != null && alloracledata.size()>0){
//			if(alloracledata.containsKey(ipAddress)){
//				iporacledata = (Hashtable)alloracledata.get(ipAddress);
//				if(iporacledata.containsKey("memValue")){
//					memValue = (Hashtable)iporacledata.get("memValue");
//				}
//			}
//			
//		}
		//2010-HONGLI
		DBDao dao = new DBDao();
		IpTranslation tranfer = new IpTranslation();
		String hex = null;
		String ip = null;
		String sid = null;
		if(ipAddress.contains(":")){
			ip = ipAddress.split(":")[0];
			sid = ipAddress.split(":")[1];
			hex = tranfer.formIpToHex(ip);
		}
		String serverip = hex+":"+sid;
		try {
			memValue = dao.getOracle_nmsoramemvalue(serverip);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try{
			if(memValue!=null){
                for(int i=0; i<sysItem.length; i++){
                	FlexVo flexVo = new FlexVo();
             	    String key = sysItemch[i];
                    String value = "";
                    flexVo.setObjectName(key);
                    if(memValue.get(sysItem[i])!=null){
                       value = (String)memValue.get(sysItem[i]);
                    }
                    if(value != null && !value.equals("") && !value.equals("null")){
                    	if(value.indexOf("MB")!=-1){
                    		value = value.replace("MB", "");
                    	}
                    	if(value.indexOf("KB")!=-1){
                    		value = value.replace("KB", "");
                    	}
                    	flexVo.setObjectNumber(df.format(Double.parseDouble(value)));
                    } else {
                    	flexVo.setObjectNumber("0");
                    }
                    flexDataList.add(flexVo);
                }
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//dao.close();
		}
		return flexDataList;
	}
	
	/*
	 * ��ȡweb������ָ��ʱ��ε���ͨ��
	 * */
	public ArrayList<FlexVo> getWebPing(String id,String dateStr,String timeStr){
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		String date[] = dateStr.split("=");
		String time[] = timeStr.split("=");
		String startTime = date[0] + " "+time[0]+":00:00";
		String endTime = date[1] + " "+time[1]+":59:59";
		String conn[] = new String[2];
		Urlmonitor_historyDao historydao = new Urlmonitor_historyDao();
		try {
			conn = historydao.getAvailability(Integer.parseInt(id),startTime,endTime,"is_canconnected");
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			historydao.close();
		}
		if(conn[0]!=null&&!"".equals(conn[0])&&conn[1]!=null&&!"".equals(conn[1])){
			FlexVo fVo1 = new FlexVo();
			fVo1.setObjectName("��ͨ");
			fVo1.setObjectNumber(conn[0]);
			flexDataList.add(fVo1);
			FlexVo fVo2 = new FlexVo();
			fVo2.setObjectName("δ��ͨ");
			fVo2.setObjectNumber(conn[1]);
			flexDataList.add(fVo2);
		}
		return flexDataList;
	}
	public ArrayList<FlexVo> getWebPingByDate(String id,String dateStr,String timeStr,int x){
		Urlmonitor_historyDao historydao = new Urlmonitor_historyDao();
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		String datestr[] = dateStr.split("=");
		if(datestr.length==2){
			if(java.sql.Date.valueOf(datestr[0]).after(java.sql.Date.valueOf(datestr[1]))){ 
				System.out.println("��ʼ���ڴ��ڽ�ֹ����!");
		    } else {
            	String starttime = datestr[0] + " 00:00:00";
		        String endTime = datestr[1] + " 23:59:59";
		        Hashtable pingHash = new Hashtable();
		        try {
		        	pingHash = historydao.getPingData(Integer.parseInt(id), starttime, endTime, "");
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					historydao.close();
				}
				List pingList = new ArrayList();
				pingList = (List) pingHash.get("list");
				if(pingList!=null&&pingList.size()>0){
					int num = pingList.size();
					for(int i=0;i<num;i++){
						FlexVo fVo = new FlexVo();
						Vector pingVector = new Vector();
						pingVector = (Vector) pingList.get(i);
						if(pingVector!=null){
							fVo.setObjectName((String) pingVector.get(1));
							fVo.setObjectDesc(pingVector.get(3).toString().substring(pingVector.get(3).toString().lastIndexOf(")")+1));
							if(x==0){
								fVo.setObjectNumber((String) pingVector.get(x)+"00");
							} else {
								fVo.setObjectNumber((String) pingVector.get(x));
							}
							flexDataList.add(fVo);
						}
					}
				}
		    }
		}
		return flexDataList;
	}
	//��ȡsql server ���ݿ���Ϣ
	public ArrayList<Vo> getSqlServerDb(String ip,String user,String pwd,String category,String name){
		Hashtable dbValue = new Hashtable();
		Hashtable alldatabase = new Hashtable();
		ArrayList<Vo> flexDataList = new ArrayList<Vo>();
		//DBDao dao = new DBDao();
		try {
			//�õ����ݿ�����Ϣ
			//dbValue = dao.getSqlserverDB(ip,user,pwd);
//			Hashtable allsqlserverdata = ShareData.getSqlserverdata();
//			Hashtable ipsqlserverdata = new Hashtable();
//			if(allsqlserverdata != null && allsqlserverdata.size()>0){
//				if(allsqlserverdata.containsKey(ip)){
//					ipsqlserverdata = (Hashtable)allsqlserverdata.get(ip);
//					if(ipsqlserverdata.containsKey("dbValue")){
//						dbValue = (Hashtable)ipsqlserverdata.get("dbValue");
//					}
//				}
//				
//			}
			DBDao dbDao = new DBDao();
			DBVo dbVo = (DBVo) dbDao.findByCondition("ip_address", ip, 2).get(0);
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(ip);
			String serverip = hex+":"+dbVo.getAlias();
			dbValue = dbDao.getSqlserver_nmsdbvalue(serverip);
			if(dbDao != null){
				dbDao.close();
			}
			alldatabase = (Hashtable) dbValue.get(category);
			if(alldatabase != null && alldatabase.size()>0){
				
				Vector names = new Vector();
				names = (Vector)dbValue.get("names");
				for(int i=0;i<names.size();i++){
					Vo vo = new Vo();
					String key = (String)names.get(i);
					if(alldatabase.get(key) == null)continue; 
                    Hashtable data=(Hashtable)alldatabase.get(key);  
                    if(data == null){
                    	continue;
                    }
                    if(data.get(name) == null){
                    	continue;
                    }
					String dbname = data.get(name).toString();
					String size = data.get("size").toString();						
					String usedsize = "";
					if (data.get("usedsize") != null){
						usedsize= data.get("usedsize").toString();
					}
					String usedperc = "";
					if(data.get("usedperc")!=null){
						usedperc= data.get("usedperc").toString();
			        }
					vo.setObjectName(dbname+"("+usedperc+"%)");
					vo.setObjectNumber1(size);
					vo.setObjectNumber2(usedsize);
					flexDataList.add(vo);
				}
			}
	    } catch(Exception e) {
			e.printStackTrace();
		} finally {
			//dao.close();
		}
		return flexDataList;
	}
	/*
	 * ��ȡFtp������ָ��ʱ��ε���ͨ��
	 * */
	public ArrayList<FlexVo> getFtpPing(String id,String dateStr,String timeStr){
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		String date[] = dateStr.split("=");
		String time[] = timeStr.split("=");
		String startTime = date[0] + " "+time[0]+":00:00";
		String endTime = date[1] + " "+time[1]+":59:59";
		String conn[] = new String[2];
		Ftpmonitor_historyDao historydao = new Ftpmonitor_historyDao();
		try {
			conn = historydao.getAvailability(Integer.parseInt(id),startTime,endTime,"is_canconnected");
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			historydao.close();
		}
		if(conn[0]!=null&&!"".equals(conn[0])&&conn[1]!=null&&!"".equals(conn[1])){
			FlexVo fVo1 = new FlexVo();
			fVo1.setObjectName("��ͨ");
			fVo1.setObjectNumber(conn[0]);
			flexDataList.add(fVo1);
			FlexVo fVo2 = new FlexVo();
			fVo2.setObjectName("δ��ͨ");
			fVo2.setObjectNumber(conn[1]);
			flexDataList.add(fVo2);
		}
		return flexDataList;
	}
	public ArrayList<FlexVo> getFtpPingByDate(String id,String dateStr,String timeStr,int x){
		Ftpmonitor_historyDao historydao = new Ftpmonitor_historyDao();
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		String datestr[] = dateStr.split("=");
		if(datestr.length==2){
			if(java.sql.Date.valueOf(datestr[0]).after(java.sql.Date.valueOf(datestr[1]))){ 
				System.out.println("��ʼ���ڴ��ڽ�ֹ����!");
		    } else {
            	String starttime = datestr[0] + " 00:00:00";
		        String endTime = datestr[1] + " 23:59:59";
		        Hashtable pingHash = new Hashtable();
		        try {
		        	pingHash = historydao.getPingData(Integer.parseInt(id), starttime, endTime);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					historydao.close();
				}
				List pingList = new ArrayList();
				pingList = (List) pingHash.get("list");
				if(pingList!=null&&pingList.size()>0){
					int num = pingList.size();
					for(int i=0;i<num;i++){
						FlexVo fVo = new FlexVo();
						Vector pingVector = new Vector();
						pingVector = (Vector) pingList.get(i);
						if(pingVector!=null){
							fVo.setObjectName((String) pingVector.get(1));
							if(x==0){
								fVo.setObjectNumber((String) pingVector.get(x)+"00");
							} else {
								fVo.setObjectNumber((String) pingVector.get(x));
							}
							flexDataList.add(fVo);
						}
					}
				}
		    }
		}
		return flexDataList;
	}
	/*
	 * ��ȡEmail������ָ��ʱ��ε���ͨ��
	 * */
	public ArrayList<FlexVo> getEmailPing(String id,String dateStr,String timeStr){
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		String date[] = dateStr.split("=");
		String time[] = timeStr.split("=");
		String startTime = date[0] + " "+time[0]+":00:00";
		String endTime = date[1] + " "+time[1]+":59:59";
		String conn[] = new String[2];
		Emailmonitor_historyDao historydao = new Emailmonitor_historyDao();
		try {
			conn = historydao.getAvailability(Integer.parseInt(id),startTime,endTime,"is_canconnected");
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			historydao.close();
		}
		if(conn[0]!=null&&!"".equals(conn[0])&&conn[1]!=null&&!"".equals(conn[1])){
			FlexVo fVo1 = new FlexVo();
			fVo1.setObjectName("��ͨ");
			fVo1.setObjectNumber(conn[0]);
			flexDataList.add(fVo1);
			FlexVo fVo2 = new FlexVo();
			fVo2.setObjectName("δ��ͨ");
			fVo2.setObjectNumber(conn[1]);
			flexDataList.add(fVo2);
		}
		return flexDataList;
	}
	public ArrayList<FlexVo> getEmailPingByDate(String id,String dateStr,String timeStr,int x){
		Emailmonitor_historyDao historydao = new Emailmonitor_historyDao();
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		String datestr[] = dateStr.split("=");
		if(datestr.length==2){
			if(java.sql.Date.valueOf(datestr[0]).after(java.sql.Date.valueOf(datestr[1]))){ 
				System.out.println("��ʼ���ڴ��ڽ�ֹ����!");
		    } else {
            	String starttime = datestr[0] + " 00:00:00";
		        String endTime = datestr[1] + " 23:59:59";
		        Hashtable pingHash = new Hashtable();
		        try {
		        	pingHash = historydao.getPingData(Integer.parseInt(id), starttime, endTime);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					historydao.close();
				}
				List pingList = new ArrayList();
				pingList = (List) pingHash.get("list");
				if(pingList!=null&&pingList.size()>0){
					int num = pingList.size();
					for(int i=0;i<num;i++){
						FlexVo fVo = new FlexVo();
						Vector pingVector = new Vector();
						pingVector = (Vector) pingList.get(i);
						if(pingVector!=null){
							fVo.setObjectName((String) pingVector.get(1));
							if(x==0){
								fVo.setObjectNumber((String) pingVector.get(x)+"00");
							} else {
								fVo.setObjectNumber((String) pingVector.get(x));
							}
							flexDataList.add(fVo);
						}
					}
				}
		    }
		}
		return flexDataList;
	}
	/*
	 * ��ȡ�˿ڷ�����ָ��ʱ��ε���ͨ��
	 * */
	public ArrayList<FlexVo> getPortPing(String ip,String dateStr,String port){
		PortServiceTypeManager portServiceTypeManager = new PortServiceTypeManager();
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		String date[] = dateStr.split("=");
		String starttime = date[0] + " 00:00:00";
        String endTime = date[1] + " 23:59:59";
        Hashtable pingHash = new Hashtable();
		try {
			pingHash = portServiceTypeManager.getCategory(ip,"SOCKETPing","ConnectUtilization",starttime,endTime,port);
		} catch(Exception e) {
			e.printStackTrace();
		} 
		String avg = (String) pingHash.get("avgpingcon");
		if(avg!=null&&!"".equals(avg)){
			float ping = Float.parseFloat(avg.replace("%", ""));
			FlexVo fVo1 = new FlexVo();
			fVo1.setObjectName("��ͨ");
			fVo1.setObjectNumber(ping+"");
			flexDataList.add(fVo1);
			FlexVo fVo2 = new FlexVo();
			fVo2.setObjectName("δ��ͨ");
			fVo2.setObjectNumber((100.0-ping)+"");
			flexDataList.add(fVo2);
		}
		return flexDataList;
	}
	public ArrayList<FlexVo> getPortPingByDate(String ip,String dateStr,String port){
		PortServiceTypeManager portServiceTypeManager = new PortServiceTypeManager();
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		String datestr[] = dateStr.split("=");
		if(datestr.length==2){
			if(java.sql.Date.valueOf(datestr[0]).after(java.sql.Date.valueOf(datestr[1]))){ 
				System.out.println("��ʼ���ڴ��ڽ�ֹ����!");
		    } else {
            	String starttime = datestr[0] + " 00:00:00";
		        String endTime = datestr[1] + " 23:59:59";
		        Hashtable pingHash = new Hashtable();
		        try {
		        	pingHash = portServiceTypeManager.getCategory(ip,"SOCKETPing","ConnectUtilization",starttime,endTime,port);
				} catch (Exception e) {
					e.printStackTrace();
				} 
				List pingList = new ArrayList();
				pingList = (List) pingHash.get("list");
				if(pingList!=null&&pingList.size()>0){
					int num = pingList.size();
					for(int i=0;i<num;i++){
						FlexVo fVo = new FlexVo();
						Vector pingVector = new Vector();
						pingVector = (Vector) pingList.get(i);
						if(pingVector!=null){
							fVo.setObjectName((String) pingVector.get(1));
							fVo.setObjectNumber((String) pingVector.get(0));
							flexDataList.add(fVo);
						}
					}
				}
		    }
		}
		return flexDataList;
	}
	//���sybase���ݿ���Ϣ
	public ArrayList<Vo> getSybaseInfo(String ipAddress){
		ArrayList<Vo> flexDataList = new ArrayList<Vo>();
//		Hashtable ipAllData = (Hashtable) ShareData.getSysbasedata().get(ipAddress);
		//Hashtable sysbaseData=ipAllData.get("");
//		if(ipAllData!=null){
		//��ȡsybase��Ϣ
		SybaseVO sysbaseVO = new SybaseVO();
		IpTranslation tranfer = new IpTranslation();
		String hex = tranfer.formIpToHex(ipAddress);
		DBDao dao = new DBDao();
		DBVo dbvo = (DBVo) dao.findByCondition("ip_address", ipAddress, 6).get(0);
		String serverip = hex+":"+dbvo.getId();
		sysbaseVO = dao.getSybaseDataByServerip(serverip); 
		dao.close();
//			SybaseVO sysbaseVO = (SybaseVO)ipAllData.get("sysbaseVO");
		if (sysbaseVO != null) {
			List dbsizelist = (List)sysbaseVO.getDbInfo();
			if (dbsizelist != null && dbsizelist.size()>0){
            	for(int i=0;i<dbsizelist.size();i++){
            		TablesVO tablesVO = (TablesVO)dbsizelist.get(i);
            		Vo vo = new Vo();
    				vo.setObjectName(tablesVO.getDb_name()+"("+tablesVO.getDb_usedperc()+"%)");
    				vo.setObjectNumber1(tablesVO.getDb_size());
    				vo.setObjectNumber2(tablesVO.getDb_freesize());
    				flexDataList.add(vo);
            	}
			}
		}
//		}
		return flexDataList;
	}
	
//	���Informix���ݿ���Ϣ
	public ArrayList<InformixVo> getInformixInfo(String ipAddress,String dbname){
		ArrayList<InformixVo> flexDataList = new ArrayList<InformixVo>();
//		Hashtable ipAllData = (Hashtable) ShareData.getInformixmonitordata().get(ipAddress);
//		Hashtable dbValue = new Hashtable();
//		if(ipAllData!=null){
//			dbValue = (Hashtable)ipAllData.get(dbname);
//			ArrayList dbspaces = new ArrayList();
//			Hashtable informixData=(Hashtable)dbValue.get("informix");
		DBDao dao = new DBDao();
		IpTranslation tranfer = new IpTranslation();
		String hex = tranfer.formIpToHex(ipAddress);
		String serverip = hex+":"+dbname;
		String status = "0";
		try {
			status = String.valueOf(((Hashtable)dao.getInformix_nmsstatus(serverip)).get("status"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//			if(informixData!=null){
//				dbspaces = (ArrayList)informixData.get("informixspaces");//���ݿ�ռ���Ϣ
		if("1".equals(status)){
				List dbspaces = new ArrayList();
				try {
					dbspaces = dao.getInformix_nmsspace(serverip);
				} catch (Exception e) {
					e.printStackTrace();
				}
				DecimalFormat df=new DecimalFormat("#.##");
				if (dbspaces != null) {
					if (dbspaces.size()>0){
		            	for(int i=0;i<dbspaces.size();i++){
		            		Hashtable tablesVO = (Hashtable)dbspaces.get(i);
		            		InformixVo vo = new InformixVo();
		    				vo.setDbspace(tablesVO.get("dbspace")+"("+df.format(100-Float.parseFloat(tablesVO.get("percent_free")+""))+"%)");
		    				vo.setFname(tablesVO.get("fname")+"");
		    				vo.setOwner(tablesVO.get("owner")+"");
		    				vo.setPages_free(tablesVO.get("pages_free")+"");
		    				vo.setPages_size(tablesVO.get("pages_size")+"");
		    				vo.setPages_used(tablesVO.get("pages_used")+"");
		    				flexDataList.add(vo);
		        	}
				}
			}
		}
		return flexDataList;
	}
	//���ݿ⵱��Ŀ�����
	public ArrayList<FlexVo> getAvgDbPingByDay(String ipAddress,String category){
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Hashtable ConnectUtilizationhash = new Hashtable();
		I_HostCollectData hostmanager=new HostCollectDataManager();
		try{
			ConnectUtilizationhash = hostmanager.getCategory(ipAddress,category,"ConnectUtilization",startTime,endTime,"");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		String avgping = (String) ConnectUtilizationhash.get("avgpingcon");
		if(avgping!=null&&!"".equals(avgping)){
			FlexVo fVo1 = new FlexVo();
			fVo1.setObjectName("������");
			fVo1.setObjectNumber(Float.parseFloat(avgping.replace("%", ""))+"");
			flexDataList.add(fVo1);
			FlexVo fVo2 = new FlexVo();
			fVo2.setObjectName("��������");
			fVo2.setObjectNumber((100-Float.parseFloat(avgping.replace("%", "")))+"");
			flexDataList.add(fVo2);
		}
//		System.out.println("--------"+flexDataList.size());
		return flexDataList;
	}
	
	public ArrayList<InformxVo> getInformixList(String ipAddress,String dbname){
		ArrayList<InformxVo> flexDataList = new ArrayList<InformxVo>();
//		Hashtable ipAllData = (Hashtable) ShareData.getInformixmonitordata().get(ipAddress);
//		Hashtable dbValue = new Hashtable();
//		dbValue = (Hashtable)ipAllData.get(dbname);
//		Hashtable informixData=(Hashtable)dbValue.get("informix");
//		ArrayList dbconfig = new ArrayList();
//		dbconfig = (ArrayList)informixData.get("configList");//���ݿ�ռ���Ϣ
		DBDao dao = new DBDao();
		IpTranslation tranfer = new IpTranslation();
		String hex = tranfer.formIpToHex(ipAddress);
		String serverip = hex+":"+dbname;
		List dbconfig = new ArrayList();
		try {
			dbconfig = dao.getInformix_nmsconfig(serverip);
		} catch (Exception e) {
			e.printStackTrace();
		}
		DecimalFormat df=new DecimalFormat("#.##");
		if (dbconfig != null && dbconfig.size()>0){
        	for(int i=0;i<dbconfig.size();i++){
        		Hashtable tablesVO = (Hashtable)dbconfig.get(i);
        		InformxVo vo = new InformxVo();
				vo.setCf_default(tablesVO.get("cf_default")+"");
				vo.setCf_effective(tablesVO.get("cf_effective")+"");
				vo.setCf_name(tablesVO.get("cf_name")+"");
				vo.setCf_original(tablesVO.get("cf_original")+"");
				flexDataList.add(vo);
        	}
		}
		return flexDataList;
	}
	
    //�����ڻ�ȡ��ʪ����Ϣ
	public ArrayList<Vo> getTCByDate(String id,String dateStr){
		ArrayList<Vo> flexDataList = new ArrayList<Vo>();
		String datestr[] = dateStr.split("=");
		if(datestr.length==2){
			if(java.sql.Date.valueOf(datestr[0]).after(java.sql.Date.valueOf(datestr[1]))){ 
				System.out.println("��ʼ���ڴ��ڽ�ֹ����!");
		    } else {
            	String starttime = datestr[0] + " 00:00:00";
		        String endTime = datestr[1] + " 23:59:59";
		        TemperatureHumidityDao temperatureHumidityDao = new TemperatureHumidityDao();
		        List list = null;
				try {
					list = temperatureHumidityDao.findByNodeIdAndTime(id, starttime, endTime);
				} catch (RuntimeException e) {
					e.printStackTrace();
				} finally {
					temperatureHumidityDao.close();
				}
				if(list!=null && list.size()>0){
					for(int i=0;i<list.size();i++){
						Vo vo = new Vo();
						TemperatureHumidityConfig tvo = (TemperatureHumidityConfig)list.get(i);
						vo.setObjectName(tvo.getTime());
						vo.setObjectNumber1(tvo.getTemperature());
						vo.setObjectNumber2(tvo.getHumidity());
						flexDataList.add(vo);
					}
				}
		    }
		}
		return flexDataList;
	}
	public String getAvgTCByDate(String id,String dateStr){
		String TH = "";
		TemperatureHumidityManager temperatureHumidityManager = new TemperatureHumidityManager();
		String datestr[] = dateStr.split("=");
		String starttime = datestr[0] + " 00:00:00";
        String endTime = datestr[1] + " 23:59:59";
        Hashtable hs = new Hashtable();
        hs = temperatureHumidityManager.getStatistics(id, starttime, endTime);
        if(hs!=null){
        	TH = hs.get("avgTemperature")+","+hs.get("avgHumidity");
        }
		return TH;
		
	}
	
	//��ȡ����������Ϣ
	public ArrayList<FlexVo> getProByDate(String ip,String pid,String dateStr,String time){
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		I_HostCollectData hostmanager=new HostCollectDataManager();
		String datestr[] = dateStr.split("=");
		String starttime = datestr[0] + " 00:00:00";
        String endTime = datestr[1] + " 23:59:59";
        DecimalFormat df=new DecimalFormat("#.##");
		try {
			Hashtable hash = hostmanager.getCategory(ip,"Process",pid,starttime,endTime,time);
			List memList = new ArrayList();
			memList = (List) hash.get("list");
			if(memList!=null&&memList.size()>0){
				int num = memList.size();
				for(int i=0;i<num;i++){
					FlexVo fVo = new FlexVo();
					Vector pingVector = new Vector();
					pingVector = (Vector) memList.get(i);
					if(pingVector!=null){
						fVo.setObjectName((String) pingVector.get(1));
						fVo.setObjectNumber(df.format(Double.parseDouble((String)pingVector.get(0))));
						flexDataList.add(fVo);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return flexDataList;
	}
	public String getAvgProByDate(String ip,String pid,String dateStr,String time){
		String avg = "";
		I_HostCollectData hostmanager=new HostCollectDataManager();
		String datestr[] = dateStr.split("=");
		String starttime = datestr[0] + " 00:00:00";
        String endTime = datestr[1] + " 23:59:59";
		try {
			Hashtable hash1 = hostmanager.getCategory(ip,"Process",pid,starttime,endTime,time);
			if(hash1!=null){
			    avg = (String) hash1.get("avgmemo");	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println("------1111------"+avg);
		return avg;
	}
	
	public ArrayList<Db2Vo> getDB2TableSpace(String ipAddress,String port,String name,String user,String pwd){
		Hashtable returnhash = new Hashtable();
		ArrayList<Db2Vo> flexDataList = new ArrayList<Db2Vo>();
		DBDao dao = new DBDao();
		try{
			//returnhash = dao.getDB2Space(ipAddress, Integer.parseInt(port), name, user, pwd);
//			Hashtable allDb2Data=(Hashtable)ShareData.getAlldb2data();
			DBVo dbvo = (DBVo) dao.findByCondition("ip_address", ipAddress, 5).get(0);
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(dbvo.getIpAddress());
			String sip = hex+":"+dbvo.getId();
			Hashtable monitorValue = dao.getDB2DataByServerip(sip);
			Hashtable allDb2Data = (Hashtable)monitorValue.get("allDb2Data");
			Hashtable ipData=(Hashtable)allDb2Data.get(ipAddress); 
			returnhash=(Hashtable)ipData.get("spaceInfo");
			Enumeration dbs = returnhash.keys();
			List retList = new ArrayList();
			while(dbs.hasMoreElements()){
				String obj = (String)dbs.nextElement();
				retList = (List)returnhash.get(obj);
				for(int i=0;i<retList.size();i++){
					Hashtable ht = (Hashtable)retList.get(i);
					if(ht == null)continue;					
					String spacename = "";
					if (ht.get("tablespace_name")!=null)spacename=ht.get("tablespace_name").toString();
					String size = "";
					if(ht.get("totalspac")!=null)size=ht.get("totalspac").toString();
					String free = "";
					if(ht.get("usablespac")!=null)free=ht.get("usablespac").toString();
					String percent = "";
					if(ht.get("usableper")!= null)percent=ht.get("usableper").toString();
					if("".equals(percent)){
						percent="0.0";
					}
					if(size.equals(free)){
						percent="100.0";
					}
					
					Db2Vo vo = new Db2Vo();
					vo.setTablespace(spacename+"("+percent+"%)");
					vo.setSize(size);
					vo.setPercent(percent);
					vo.setFree(free);
					flexDataList.add(vo);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dao.close();
		}
		return flexDataList;
		
	}
	
	public ArrayList getDiskByDate(String ipAddress,String dateStr,String time,String diskName){
		I_HostCollectData hostManager = new HostCollectDataManager();
		ArrayList flexDataList = new ArrayList();
		ArrayList list = new ArrayList();
		String date[] = null;
		String startTime = "";
		String endTime = "";
		try {
			if("".equals(dateStr)){
				startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
				endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			} else {
				date = dateStr.split("=");
				startTime = date[0] + " 00:00:00";
				endTime = date[1] + " 23:59:59";
			}
			DecimalFormat df=new DecimalFormat("#.##");
			Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(ipAddress); 
			String[] arrName = null;
			String diskname = "";
			String runmodel = PollingEngine.getCollectwebflag();//ϵͳ����ģʽ
			if("0".equals(runmodel)){
				//�ɼ�������Ǽ���ģʽ
				String name = "";
				if(diskName!=null&&!"".equals(diskName)){
					name = diskName;
				}else {
					if (ipAllData != null) {
						Vector diskVector = (Vector) ipAllData.get("disk");
						if (diskVector != null && diskVector.size() > 0) {
							for (int si = 0; si < diskVector.size(); si++) {
								Diskcollectdata diskdata = (Diskcollectdata) diskVector.elementAt(si);
								if(diskdata!=null){
									if (diskdata.getEntity().equalsIgnoreCase("AllSize")) {
										name = name + diskdata.getSubentity() + ";";
									}
								}
							}
						}
					}
				}
				arrName = name.split(";");
			}else{
				//�ɼ�������Ƿ���ģʽ
				//arrName = new String[]{"C:/","D:/","E:/","F:/","G:/","H:/"};
				String allipstr = SysUtil.doip(ipAddress);
				String tablename = "disk"+time+allipstr;
				List<String> subentityList = hostManager.getSubentitysByTableName(tablename);
				if(subentityList != null){
					String[] temps = new String[subentityList.size()];
					for(int i=0; i<subentityList.size();i++){
						temps[i] = subentityList.get(i);
					}
					arrName = temps;
				}
			}
	        //��ȡӲ����������ʷ����ֵ
			if(arrName!=null&&arrName.length>0){
				Hashtable virtualHash[] = new Hashtable[arrName.length];
				for(int i=0;i<arrName.length;i++){
					diskname = diskname + subString(arrName[i]);
					if(i!=arrName.length-1){
						diskname = diskname + ",";
					}
					try {
						virtualHash[i] = hostManager.getDisk(ipAddress,arrName[i], startTime, endTime, time);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				flexDataList.add(diskname);
				if(virtualHash[0]!=null){
					List dlist=new ArrayList();
					dlist = (List) virtualHash[0].get("list");
					int dlist_len = dlist.size();
					for(int i=0;i<dlist_len;i++){
						Vector v = (Vector) dlist.get(i);
						Hashtable h =new Hashtable();
						h.put("objectName", v.get(3));
						for(int j = 0;j<virtualHash.length;j++){
							List virtualList = new ArrayList();
							virtualList = (List) virtualHash[j].get("list");
							for(int k =0;k<virtualList.size();k++){
								Vector vector = new Vector();
								vector = (Vector) virtualList.get(k);
								if(vector.get(3).equals(v.get(3))){
									String tempstr = df.format(Double.parseDouble((String)vector.get(0)));
									h.put(subString(arrName[j]), tempstr);
									break;
								}
							}
						}
						list.add(h);
					}
				}
				flexDataList.add(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flexDataList;
	}
	
	public ArrayList getDisk(String ipAddress){
		ArrayList flexDataList = new ArrayList();
		Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(ipAddress);
		try {
			if (ipAllData != null) {
				Vector diskVector = (Vector) ipAllData.get("disk");
				if (diskVector != null && diskVector.size() > 0) {
					for (int si = 0; si < diskVector.size(); si++) {
						Diskcollectdata diskdata = (Diskcollectdata) diskVector.elementAt(si);
						if(diskdata!=null){
							if (diskdata.getEntity().equalsIgnoreCase("AllSize")) {
//								name = name + diskdata.getSubentity() + ";";
								CombVo combVo = new CombVo();
								combVo.setLabel(diskdata.getSubentity());
								combVo.setData(diskdata.getSubentity());
								flexDataList.add(combVo);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flexDataList;
	}
	
	public ArrayList getCpuDetailByDate(String ipAddress,String dateStr,String time){
		I_HostCollectData hostManager = new HostCollectDataManager();
		ArrayList flexDataList = new ArrayList();
		ArrayList list = new ArrayList();
		String date[] = null;
		String startTime = "";
		String endTime = "";
		if("".equals(dateStr)){
			startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
			endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		} else {
			date = dateStr.split("=");
			startTime = date[0] + " 00:00:00";
			endTime = date[1] + " 23:59:59";
		}
		DecimalFormat df=new DecimalFormat("#.###");
		String runmodel = PollingEngine.getCollectwebflag();
		String name = "";
		if("0".equals(runmodel)){
        	//�ɼ�������Ǽ���ģʽ
			Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(ipAddress);		
			if (ipAllData != null) {
				List cpuperflist = (List) ipAllData.get("cpuperflist");
				if (cpuperflist != null && cpuperflist.size() > 0) {
					//for (int si = 0; si < cpuperflist.size(); si++) {
						Hashtable cpuperfhash = (Hashtable) cpuperflist.get(0);
						if(cpuperfhash!=null){
							Iterator keys=cpuperfhash.keySet().iterator();
					 		 String key="";
							 while(keys.hasNext()){
								key=keys.next().toString();
								String value = (String)cpuperfhash.get(key);
								name = name + key.replaceAll("%", "") + ";";
							 }
						}
					//}
				}
			}
		}else{
			//�ɼ�������Ƿ���ģʽ
			OthersTempDao tempdao = new OthersTempDao();
			List cpuperflist = new ArrayList();
			try{
				cpuperflist = tempdao.getCpuPerfInfoList(ipAddress);
			}catch(Exception e){
				
			}finally{
				tempdao.close();
			}
			if(cpuperflist != null && cpuperflist.size()>0){
				for (int si = 0; si < cpuperflist.size(); si++) {
					CPUcollectdata cpudata = (CPUcollectdata)cpuperflist.get(si);
					name = name + cpudata.getSubentity().replaceAll("%", "") + ";";
				}
			}
		}
		String cpudetailname = "";
		String arrName[] = name.split(";");
        //��ȡCPU����ָ����������ʷ����ֵ
		if(arrName!=null&&arrName.length>0){
			Hashtable virtualHash[] = new Hashtable[arrName.length];
			DBManager dbmanager = new DBManager();
			try{
				for(int i=0;i<arrName.length;i++){
					cpudetailname = cpudetailname + subString(arrName[i]);
					if(i!=arrName.length-1){
						cpudetailname = cpudetailname + ",";
					}
					try {
						virtualHash[i] = hostManager.getCpuDetails(dbmanager,ipAddress,arrName[i].replaceAll("%", ""), startTime, endTime, time);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				dbmanager.close();
			}
			flexDataList.add(cpudetailname);
			if(virtualHash[1]!=null){
				List dlist=new ArrayList();
				dlist = (List) virtualHash[1].get("list");
				int dlist_len = dlist.size();
				for(int i=0;i<dlist_len;i++){
					Vector v = (Vector) dlist.get(i);
					Hashtable h =new Hashtable();
					h.put("objectName", v.get(3));
					//SysLogger.info(v.get(3)+"================");
					for(int j = 0;j<virtualHash.length;j++){
						List virtualList = new ArrayList();
						virtualList = (List) virtualHash[j].get("list");
						for(int k =0;k<virtualList.size();k++){
							Vector vector = new Vector();
							vector = (Vector) virtualList.get(k);
							if(vector.get(3).equals(v.get(3))){
								//SysLogger.info(arrName[j].replaceAll("%", "")+"================"+vector.get(0));
								h.put(subString(arrName[j].replaceAll("%", "")), vector.get(0));
								break;
							}
						}
					}
					list.add(h);
				}
			}
			flexDataList.add(list);
		}
		return flexDataList;
	}
	
	public ArrayList getNetMemoryByDate(String ipAddress,String dateStr,String time){
		I_HostCollectData hostManager = new HostCollectDataManager();
		ArrayList flexDataList = new ArrayList();
		ArrayList list = new ArrayList();
		String date[] = null;
		String startTime = "";
		String endTime = "";
		if("".equals(dateStr)){
			startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
			endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		} else {
			date = dateStr.split("=");
			startTime = date[0] + " 00:00:00";
			endTime = date[1] + " 23:59:59";
		}
		String memoname = "";
		String[] arrName = null;
		String runmodel = PollingEngine.getCollectwebflag();//ϵͳ����ģʽ
		if("0".equals(runmodel)){
			//�ɼ�������Ǽ���ģʽ
			Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(ipAddress);
			String name = "";
			if (ipAllData != null) {
				Vector memoryVector = (Vector) ipAllData.get("memory");
				if (memoryVector != null && memoryVector.size() > 0) {
					for (int si = 0; si < memoryVector.size(); si++) {
						Memorycollectdata memodata = (Memorycollectdata) memoryVector.elementAt(si);
						if(memodata!=null){
							if (memodata.getEntity().equalsIgnoreCase("Utilization")) {
								name = name + memodata.getSubentity() + ";";
							}
						}
					}
				}
			}
			arrName = name.split(";");
		}else{
			//�ɼ�������Ƿ���ģʽ
			String allipstr = SysUtil.doip(ipAddress);
			String tablename = "memory"+time+allipstr;
			List<String> subentityList = hostManager.getSubentitysByTableName(tablename);
			if(subentityList != null){
				String[] temps = new String[subentityList.size()];
				for(int i=0; i<subentityList.size();i++){
					temps[i] = subentityList.get(i);
				}
				arrName = temps;
			}
		}
        //��ȡ���ڴ���������ʷ����ֵ
		if(arrName!=null&&arrName.length>0){
			Hashtable virtualHash[] = new Hashtable[arrName.length];
			for(int i=0;i<arrName.length;i++){
				memoname = memoname + "�ڴ�" + arrName[i];
				if(i!=arrName.length-1){
					memoname = memoname + ",";
				}
				try {
					virtualHash[i] = hostManager.getMemory(ipAddress,arrName[i], startTime, endTime, time);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			flexDataList.add(memoname);
			if(virtualHash[0]!=null){
				List dlist=new ArrayList();
				dlist = (List) virtualHash[0].get("list");
				int dlist_len = dlist.size();
				for(int i=0;i<dlist_len;i++){
					Vector v = (Vector) dlist.get(i);
					Hashtable h =new Hashtable();
					h.put("objectName", v.get(1));
					for(int j = 0;j<virtualHash.length;j++){
						List virtualList = new ArrayList();
						virtualList = (List) virtualHash[j].get("list");
						for(int k =0;k<virtualList.size();k++){
							Vector vector = new Vector();
							vector = (Vector) virtualList.get(i);
							if(vector.get(1).equals(v.get(1))){
								h.put("�ڴ�"+arrName[j], vector.get(0));
								break;
							}
						}
					}
					list.add(h);
				}
			}
			flexDataList.add(list);
		}
		return flexDataList;
	}
	
	public ArrayList getUpsByDate(String ipAddress,String dateStr,String time,String obj1,String obj2,String obj3,String table){
		I_HostCollectData hostManager = new HostCollectDataManager();
		ArrayList flexDataList = new ArrayList();
		String date[] = null;
		String startTime = "";
		String endTime = "";
		if("".equals(dateStr)){
			startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
			endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		} else {
			date = dateStr.split("=");
			startTime = date[0] + " 00:00:00";
			endTime = date[1] + " 23:59:59";
		}
		String arrName[] = {obj1,obj2,obj3};
        //��ȡ���ڴ���������ʷ����ֵ
		if(arrName!=null&&arrName.length>0){
			Hashtable returnHash[] = new Hashtable[arrName.length];
			for(int i=0;i<arrName.length;i++){
				try {
					returnHash[i] = hostManager.getUpsData(ipAddress,arrName[i], startTime, endTime, time, table);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(returnHash!=null){
				List returnlist1=new ArrayList();
				returnlist1 = (List) returnHash[0].get("list");
				List returnlist2=new ArrayList();
				returnlist2 = (List) returnHash[1].get("list");
				List returnlist3=new ArrayList();
				returnlist3 = (List) returnHash[2].get("list");
				int list_len = returnlist1.size();
				for(int i=0;i<list_len;i++){
					Vector v = (Vector) returnlist1.get(i);
					Vector v1 = (Vector) returnlist2.get(i);
					Vector v2 = (Vector) returnlist3.get(i);
					Vo3 vo3 = new Vo3();
					vo3.setObjectName((String) v.get(1));
					vo3.setObjectNumber1((String) v.get(0));
					vo3.setObjectNumber2((String) v1.get(0));
					vo3.setObjectNumber3((String) v2.get(0));
					flexDataList.add(vo3);
				}
			}
		}
		return flexDataList;
	}
	
	public ArrayList getNetFlashMemoryByDate(String ipAddress,String dateStr,String time){
		I_HostCollectData hostManager = new HostCollectDataManager();
		ArrayList flexDataList = new ArrayList();
		ArrayList list = new ArrayList();
		String date[] = null;
		String startTime = "";
		String endTime = "";
		if("".equals(dateStr)){
			startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
			endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		} else {
			date = dateStr.split("=");
			startTime = date[0] + " 00:00:00";
			endTime = date[1] + " 23:59:59";
		}
		String memoname = "";
		String[] arrName = null;
		String runmodel = PollingEngine.getCollectwebflag();//ϵͳ����ģʽ
		if("0".equals(runmodel)){
			//�ɼ�������Ǽ���ģʽ
			Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(ipAddress);
			String name = "";
			if (ipAllData != null) {
				Vector memoryVector = (Vector) ipAllData.get("flash");
				if (memoryVector != null && memoryVector.size() > 0) {
					for (int si = 0; si < memoryVector.size(); si++) {
						Flashcollectdata memodata = (Flashcollectdata) memoryVector.elementAt(si);
						if(memodata!=null){
							if (memodata.getEntity().equalsIgnoreCase("Utilization")) {
								name = name + memodata.getSubentity() + ";";
							}
						}
					}
				}
			}
			arrName = name.split(";");
		}else{
			//�ɼ�������Ƿ���ģʽ
			String allipstr = SysUtil.doip(ipAddress);
			String tablename = "flash"+time+allipstr;
			List<String> subentityList = hostManager.getSubentitysByTableName(tablename);
			if(subentityList != null){
				String[] temps = new String[subentityList.size()];
				for(int i=0; i<subentityList.size();i++){
					temps[i] = subentityList.get(i);
				}
				arrName = temps;
			}
		}
        //��ȡ��������������ʷ����ֵ
		if(arrName!=null&&arrName.length>0){
			Hashtable virtualHash[] = new Hashtable[arrName.length];
			for(int i=0;i<arrName.length;i++){
				memoname = memoname + "����" + arrName[i];
				if(i!=arrName.length-1){
					memoname = memoname + ",";
				}
				try {
					virtualHash[i] = hostManager.getFlash(ipAddress,arrName[i], startTime, endTime, time);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			flexDataList.add(memoname);
			if(virtualHash[0]!=null){
				List dlist=new ArrayList();
				dlist = (List) virtualHash[0].get("list");
				int dlist_len = dlist.size();
				for(int i=0;i<dlist_len;i++){
					Vector v = (Vector) dlist.get(i);
					Hashtable h =new Hashtable();
					h.put("objectName", v.get(1));
					for(int j = 0;j<virtualHash.length;j++){
						List virtualList = new ArrayList();
						virtualList = (List) virtualHash[j].get("list");
						for(int k =0;k<virtualList.size();k++){
							Vector vector = new Vector();
							vector = (Vector) virtualList.get(i);
							if(vector.get(1).equals(v.get(1))){
								h.put("����"+arrName[j], vector.get(0));
								break;
							}
						}
					}
					list.add(h);
				}
			}
			flexDataList.add(list);
		}
		return flexDataList;
	}
	/*
	 * ��ȡһ̨�豸��ָ��ʱ���ƽ������������
	 * */
	public String getAvgNetFlashMemoryByDate(String ipAddress,String dateStr){
		I_HostCollectData hostManager = new HostCollectDataManager();
		String date[] = dateStr.split("=");
		String startTime = date[0] + " 00:00:00";
		String endTime = date[1] + " 23:59:59";
		String virtualAvgutil = "";
		DecimalFormat df=new DecimalFormat("#.##");
//		��Ҫ���ֲ�ʽ�ж�
		String runmodel = PollingEngine.getCollectwebflag(); 
		if(java.sql.Date.valueOf(date[0]).after(java.sql.Date.valueOf(date[1]))){ 
			System.out.println("��ʼ���ڴ��ڽ�ֹ����!");
	    } else {
	    	Hashtable ipAllData = new Hashtable();
	    	if("0".equals(runmodel)){
				//�ɼ�������Ǽ���ģʽ 
				ipAllData = (Hashtable)ShareData.getSharedata().get(ipAddress);
			}else{
				//�ɼ�������Ƿ���ģʽ 
				ipAllData = (Hashtable)ShareData.getAllNetworkData().get(ipAddress);
			}
			String name = "";
			if (ipAllData != null) {
				Vector memoryVector = (Vector) ipAllData.get("flash");
				if (memoryVector != null && memoryVector.size() > 0) {
					for (int si = 0; si < memoryVector.size(); si++) {
						Memorycollectdata memodata = (Memorycollectdata) memoryVector.elementAt(si);
						if(memodata!=null){
							if (memodata.getEntity().equalsIgnoreCase("Utilization")) {
								name = name + memodata.getSubentity() + ";";
							}
						}
					}
				}
			}
			String arrName[] = name.split(";");
	    	Hashtable virtualHash[] = new Hashtable[arrName.length];
	    	if(arrName!=null&&arrName.length>0){
	    		for(int i=0;i<arrName.length;i++){
					try {
				        virtualHash[i] = hostManager.getFlash(ipAddress, arrName[i], startTime, endTime, "");
					} catch (Exception e) {
						e.printStackTrace();
					}
					virtualAvgutil = virtualAvgutil + "����"+ arrName[i] + "ƽ��������:" +df.format(Double.parseDouble((String) virtualHash[i].get("avgput"))) +"%  ";
		    	}
	    	}
	    }
		return virtualAvgutil;
		
	}
	public ArrayList getEnviromentByDate(String ipAddress,String dateStr,String time,String table,String key){
		I_HostCollectData hostManager = new HostCollectDataManager();
		ArrayList flexDataList = new ArrayList();
		ArrayList list = new ArrayList();
		String date[] = null;
		String startTime = "";
		String endTime = "";
		if("".equals(dateStr)){
			startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
			endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		} else {
			date = dateStr.split("=");
			startTime = date[0] + " 00:00:00";
			endTime = date[1] + " 23:59:59";
		}
		String dataname = "";
		String[] arrName = null;
		String runmodel = PollingEngine.getCollectwebflag();//ϵͳ����ģʽ
		if("0".equals(runmodel)){
			//�ɼ�������Ǽ���ģʽ
			Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(ipAddress);
			String name = "";
			if (ipAllData != null) {
				Vector dataVector = (Vector) ipAllData.get(key);
				if (dataVector != null && dataVector.size() > 0) {
					for (int si = 0; si < dataVector.size(); si++) {
						Interfacecollectdata data = (Interfacecollectdata) dataVector.elementAt(si);
						if(data!=null){
							name = name + data.getSubentity() + ";";
						}
					}
				}
			}
			arrName = name.split(";");
		}else{
			//�ɼ�������Ƿ���ģʽ
			String allipstr = SysUtil.doip(ipAddress);
			String tablename = table+time+allipstr;
			List<String> subentityList = hostManager.getSubentitysByTableName(tablename);
			if(subentityList != null){
				String[] temps = new String[subentityList.size()];
				for(int i=0; i<subentityList.size();i++){
					temps[i] = subentityList.get(i);
				}
				arrName = temps;
			}
		}
		if(arrName!=null&&arrName.length>0){
			Hashtable virtualHash[] = new Hashtable[arrName.length];
			for(int i=0;i<arrName.length;i++){
				dataname = dataname + "ģ��" + arrName[i];
				if(i!=arrName.length-1){
					dataname = dataname + ",";
				}
				try {
					virtualHash[i] = hostManager.getEnviroment(ipAddress,arrName[i], startTime, endTime, time,table);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			flexDataList.add(dataname);
			if(virtualHash[0]!=null){
				List dlist=new ArrayList();
				dlist = (List) virtualHash[0].get("list");
				int dlist_len = dlist.size();
				for(int i=0;i<dlist_len;i++){
					Vector v = (Vector) dlist.get(i);
					Hashtable h =new Hashtable();
					h.put("objectName", v.get(1));
					for(int j = 0;j<virtualHash.length;j++){
						List virtualList = new ArrayList();
						virtualList = (List) virtualHash[j].get("list");
						for(int k =0;k<virtualList.size();k++){
							Vector vector = new Vector();
							vector = (Vector) virtualList.get(i);
							if(vector.get(1).equals(v.get(1))){
								h.put("ģ��"+arrName[j], vector.get(0));
								break;
							}
						}
					}
					list.add(h);
				}
			}
			flexDataList.add(list);
		}
		return flexDataList;
	}
	
	public ArrayList<AlarmVo> getAlarmEquipment(){
        ArrayList<AlarmVo> flexDataList = new ArrayList<AlarmVo>();
        List nodeList = PollingEngine.getInstance().getNodeList();	
		List dbList = PollingEngine.getInstance().getDbList();	
		List tomcatList = PollingEngine.getInstance().getTomcatList();	
		List weblogicList = PollingEngine.getInstance().getApacheList();
		List mqList = PollingEngine.getInstance().getMqList();	
		List dominoList = PollingEngine.getInstance().getDominoList();	
		List wasList = PollingEngine.getInstance().getWasList();	
		List cicsList = PollingEngine.getInstance().getCicsList();	
//		List firewallList = PollingEngine.getInstance().getAllTypeMap().get("net_firewall");	
		List webList = PollingEngine.getInstance().getWebList();	
		List mailList = PollingEngine.getInstance().getMailList();	
		List ftpList = PollingEngine.getInstance().getFtpList();	
		List iisList = PollingEngine.getInstance().getIisList();	
		List socketList = PollingEngine.getInstance().getSocketList();	
		BusinessDao bussdao = new BusinessDao();
	    List allbuss = bussdao.loadAll(); 
	    
		if(dbList!=null&&dbList.size()>0){
            for(int i=0;i<dbList.size();i++){
            	Node node = (Node)dbList.get(i);  
			    if(node.isAlarm()){
			    	AlarmVo alarmVo = new AlarmVo();
			    	alarmVo.setType_("���ݿ�");
//			    	MYSQL
			        if(node.getCategory()==52){
			        	alarmVo.setType("MYSQL");
			      	}
			       	//Oracle
			       	else if(node.getCategory()==53){
			       		alarmVo.setType("Oracle");
			      	}
			       	//Sqlserver
			       	else if(node.getCategory()==54){
			       		alarmVo.setType("Sqlserver");
			      	}
			       	//Sybase
			       	else if(node.getCategory()==55){
			       		alarmVo.setType("Sybase");
			      	}
			       	//DB2
			       	else if(node.getCategory()==59){
			       		alarmVo.setType("DB2");
			      	}
			       	//Informix
			       	else if(node.getCategory()==60){
			       		alarmVo.setType("Informix");
			      	}
			        alarmVo.setIp(node.getIpAddress());
			        alarmVo.setLocation("");
			        alarmVo.setName(node.getAlias());
			        String bus = "";
			        if(allbuss.size()>0){
						for(int j=0;j<allbuss.size();j++){
							Business buss = (Business)allbuss.get(j);
							if(node.getBid()!=null&&node.getBid().indexOf(buss.getId())!=-1){
								bus = bus + buss.getName() + ";";
							}
						}
					}
			        alarmVo.setBussiness("".equals(bus)?"":bus.substring(0, bus.lastIndexOf(";")));
			        alarmVo.setLevel1("../resource/"+NodeHelper.getStatusImage(node.getStatus()));
			        flexDataList.add(alarmVo);
			    }
			    
		     }
		}
//        if(firewallList!=null&&firewallList.size()>0){
//	        for(int i=0;i<firewallList.size();i++){
//		        Node node = (Node)firewallList.get(i);  
//				//����ǽ
//		        if(node.getCategory()==8&&node.isAlarm()){
//		        	AlarmVo alarmVo = new AlarmVo();
//		        	alarmVo.setType_("����ǽ");
//		        	alarmVo.setType(node.getType());
//		        	alarmVo.setIp(node.getIpAddress());
//			        alarmVo.setLocation("");
//			        alarmVo.setName(node.getAlias());
//			        String bus = "";
//			        if(allbuss.size()>0){
//						for(int j=0;j<allbuss.size();j++){
//							Business buss = (Business)allbuss.get(j);
//							if(node.getBid()!=null&&node.getBid().contains(buss.getId())){
//								bus = bus + buss.getName() + ";";
//							}
//						}
//					}
//			        alarmVo.setBussiness("".equals(bus)?"":bus.substring(0, bus.lastIndexOf(";")));
//			        alarmVo.setLevel1("../resource/"+NodeHelper.getStatusImage(node.getStatus()));
//			        flexDataList.add(alarmVo);
//		        }
//	        }
//		}
		if(nodeList!=null&&nodeList.size()>0){
	        for(int i=0;i<nodeList.size();i++){
	       	    Node node = (Node)nodeList.get(i);  
	       	    if(node.isAlarm()){
	       	    	AlarmVo alarmVo = new AlarmVo();
		       	    //·����
		       	    if(node.getCategory()==1){
		       	    	alarmVo.setType_("·����");
	           	    }
		       	    //·�ɽ�����
			       	else if(node.getCategory()==2){
			       		alarmVo.setType_("·�ɽ�����");
			       	}
			       	//������
			       	else if(node.getCategory()==3){
			       		alarmVo.setType_("������");
			      	}
			       	//������
			       	else if(node.getCategory()==4){
			       		alarmVo.setType_("������");
			       	}
//		       	�ʼ���ȫ����
			       	else if(node.getCategory()==10){
			       		alarmVo.setType_("�ʼ���ȫ����");
			       	}
//			       	ATM
			       	else if(node.getCategory()==9){
			       		alarmVo.setType_("ATM");
			       	}
//			       	����ǽ
			       	else if(node.getCategory()==8){
			       		alarmVo.setType_("����ǽ");
			       	}
//		       	���ؾ���
			       	else if(node.getCategory()==11){
			       		alarmVo.setType_("F5");
			       	}
		       	    alarmVo.setType(node.getType());
		       	    alarmVo.setIp(node.getIpAddress());
			        alarmVo.setLocation("");
			        alarmVo.setName(node.getAlias());
			        String bus = "";
			        if(allbuss.size()>0){
						for(int j=0;j<allbuss.size();j++){
							Business buss = (Business)allbuss.get(j);
							if(node.getBid()!=null&&node.getBid().contains(buss.getId())){
								bus = bus + buss.getName() + ";";
							}
						}
					}
			        alarmVo.setBussiness("".equals(bus)?"":bus.substring(0, bus.lastIndexOf(";")));
			        alarmVo.setLevel1("../resource/"+NodeHelper.getStatusImage(node.getStatus()));
			        flexDataList.add(alarmVo);
		        }
	       	}
		}
		
		if(mailList!=null&&mailList.size()>0){
			for(int i=0;i<mailList.size();i++){
				Node node = (Node)mailList.get(i);  
				//Mail
				if(node.getCategory()==56&&node.isAlarm()){
					AlarmVo alarmVo = new AlarmVo();
					alarmVo.setType_("Mail");
		        	alarmVo.setType(node.getType());
		        	alarmVo.setIp(node.getIpAddress());
			        alarmVo.setLocation("");
			        alarmVo.setName(node.getAlias());
			        String bus = "";
			        if(allbuss.size()>0){
						for(int j=0;j<allbuss.size();j++){
							Business buss = (Business)allbuss.get(j);
							if(node.getBid()!=null&&node.getBid().contains(buss.getId())){
								bus = bus + buss.getName() + ";";
							}
						}
					}
			        alarmVo.setBussiness("".equals(bus)?"":bus.substring(0, bus.lastIndexOf(";")));
			        alarmVo.setLevel1("../resource/"+NodeHelper.getStatusImage(node.getStatus()));
			        flexDataList.add(alarmVo);
				}
			}
		}
       
		if(webList!=null&&webList.size()>0){
			for(int i=0;i<webList.size();i++){
				Node node = (Node)webList.get(i);  
				//WEB
		       	if(node.getCategory()==57&&node.isAlarm()){
		       		AlarmVo alarmVo = new AlarmVo();
		        	alarmVo.setType_("WEB����");
		        	alarmVo.setType(node.getType());
		        	alarmVo.setIp(node.getIpAddress());
			        alarmVo.setLocation("");
			        alarmVo.setName(node.getAlias());
			        String bus = "";
			        if(allbuss.size()>0){
						for(int j=0;j<allbuss.size();j++){
							Business buss = (Business)allbuss.get(j);
							if(node.getBid()!=null&&node.getBid().contains(buss.getId())){
								bus = bus + buss.getName() + ";";
							}
						}
					}
			        alarmVo.setBussiness("".equals(bus)?"":bus.substring(0, bus.lastIndexOf(";")));
			        alarmVo.setLevel1("../resource/"+NodeHelper.getStatusImage(node.getStatus()));
			        flexDataList.add(alarmVo);
		      	}
			}
		} 
		 
		if(ftpList!=null&&ftpList.size()>0){
			for(int i=0;i<ftpList.size();i++){
				Node node = (Node)ftpList.get(i);  
				 //FTP
		       	if(node.getCategory()==58&&node.isAlarm()){
		       		AlarmVo alarmVo = new AlarmVo();
		        	alarmVo.setType_("FTP����");
		        	alarmVo.setType(node.getType());
		        	alarmVo.setIp(node.getIpAddress());
			        alarmVo.setLocation("");
			        alarmVo.setName(node.getAlias());
			        String bus = "";
			        if(allbuss.size()>0){
						for(int j=0;j<allbuss.size();j++){
							Business buss = (Business)allbuss.get(j);
							if(node.getBid()!=null&&node.getBid().contains(buss.getId())){
								bus = bus + buss.getName() + ";";
							}
						}
					}
			        alarmVo.setBussiness("".equals(bus)?"":bus.substring(0, bus.lastIndexOf(";")));
			        alarmVo.setLevel1("../resource/"+NodeHelper.getStatusImage(node.getStatus()));
			        flexDataList.add(alarmVo);
		      	}
			}
		}  
		
		if(mqList!=null&&mqList.size()>0){
			for(int i=0;i<mqList.size();i++){
				Node node = (Node)mqList.get(i);  
				 //MQ
		       if(node.getCategory()==61&&node.isAlarm()){
		           AlarmVo alarmVo = new AlarmVo();
		        	alarmVo.setType_("MQ");
		        	alarmVo.setType(node.getType());
		        	alarmVo.setIp(node.getIpAddress());
			        alarmVo.setLocation("");
			        alarmVo.setName(node.getAlias());
			        String bus = "";
			        if(allbuss.size()>0){
						for(int j=0;j<allbuss.size();j++){
							Business buss = (Business)allbuss.get(j);
							if(node.getBid()!=null&&node.getBid().contains(buss.getId())){
								bus = bus + buss.getName() + ";";
							}
						}
					}
			        alarmVo.setBussiness("".equals(bus)?"":bus.substring(0, bus.lastIndexOf(";")));
			        alarmVo.setLevel1("../resource/"+NodeHelper.getStatusImage(node.getStatus()));
			        flexDataList.add(alarmVo);
		       }
			}
		}  
       	
		if(dominoList!=null&&dominoList.size()>0){
			for(int i=0;i<dominoList.size();i++){
				Node node = (Node)dominoList.get(i);  
				 //Domino
		       	if(node.getCategory()==62&&node.isAlarm()){
		       		AlarmVo alarmVo = new AlarmVo();
		        	alarmVo.setType_("Domino");
		        	alarmVo.setType(node.getType());
		        	alarmVo.setIp(node.getIpAddress());
			        alarmVo.setLocation("");
			        alarmVo.setName(node.getAlias());
			        String bus = "";
			        if(allbuss.size()>0){
						for(int j=0;j<allbuss.size();j++){
							Business buss = (Business)allbuss.get(j);
							if(node.getBid()!=null&&node.getBid().contains(buss.getId())){
								bus = bus + buss.getName() + ";";
							}
						}
					}
			        alarmVo.setBussiness("".equals(bus)?"":bus.substring(0, bus.lastIndexOf(";")));
			        alarmVo.setLevel1("../resource/"+NodeHelper.getStatusImage(node.getStatus()));
			        flexDataList.add(alarmVo);
		      	}
		    }
	    } 
		
		if(wasList!=null&&wasList.size()>0){
			for(int i=0;i<wasList.size();i++){
				Node node = (Node)wasList.get(i);  
				 //WAS
		       	if(node.getCategory()==63&&node.isAlarm()){
		       		AlarmVo alarmVo = new AlarmVo();
		        	alarmVo.setType_("WAS");
		        	alarmVo.setType(node.getType());
		        	alarmVo.setIp(node.getIpAddress());
			        alarmVo.setLocation("");
			        alarmVo.setName(node.getAlias());
			        String bus = "";
			        if(allbuss.size()>0){
						for(int j=0;j<allbuss.size();j++){
							Business buss = (Business)allbuss.get(j);
							if(node.getBid()!=null&&node.getBid().contains(buss.getId())){
								bus = bus + buss.getName() + ";";
							}
						}
					}
			        alarmVo.setBussiness("".equals(bus)?"":bus.substring(0, bus.lastIndexOf(";")));
			        alarmVo.setLevel1("../resource/"+NodeHelper.getStatusImage(node.getStatus()));
			        flexDataList.add(alarmVo);
		      	}
		    }
	    }
 
		if(weblogicList!=null&&weblogicList.size()>0){
			for(int i=0;i<weblogicList.size();i++){
				Node node = (Node)weblogicList.get(i);  
				 //WEBLOGIC
		       	if(node.getCategory()==64&&node.isAlarm()){
		       		AlarmVo alarmVo = new AlarmVo();
		        	alarmVo.setType_("WEBLOGIC");
		        	alarmVo.setType(node.getType());
		        	alarmVo.setIp(node.getIpAddress());
			        alarmVo.setLocation("");
			        alarmVo.setName(node.getAlias());
			        String bus = "";
			        if(allbuss.size()>0){
						for(int j=0;j<allbuss.size();j++){
							Business buss = (Business)allbuss.get(j);
							if(node.getBid()!=null&&node.getBid().contains(buss.getId())){
								bus = bus + buss.getName() + ";";
							}
						}
					}
			        alarmVo.setBussiness("".equals(bus)?"":bus.substring(0, bus.lastIndexOf(";")));
			        alarmVo.setLevel1("../resource/"+NodeHelper.getStatusImage(node.getStatus()));
			        flexDataList.add(alarmVo);
		      	}
		    }
		}
		
       	
		if(cicsList!=null&&cicsList.size()>0){
			for(int i=0;i<cicsList.size();i++){
				Node node = (Node)cicsList.get(i);  
				 //CICS
		       	if(node.getCategory()==65&&node.isAlarm()){
		       		AlarmVo alarmVo = new AlarmVo();
		        	alarmVo.setType_("CICS");
		        	alarmVo.setIp(node.getIpAddress());
			        alarmVo.setLocation("");
			        alarmVo.setName(node.getAlias());
			        String bus = "";
			        if(allbuss.size()>0){
						for(int j=0;j<allbuss.size();j++){
							Business buss = (Business)allbuss.get(j);
							if(node.getBid()!=null&&node.getBid().contains(buss.getId())){
								bus = bus + buss.getName() + ";";
							}
						}
					}
			        alarmVo.setBussiness("".equals(bus)?"":bus.substring(0, bus.lastIndexOf(";")));
			        alarmVo.setType(node.getType());
			        alarmVo.setLevel1("../resource/"+NodeHelper.getStatusImage(node.getStatus()));
			        flexDataList.add(alarmVo);
		      	}
		    }
		}
       	
		if(iisList!=null&&iisList.size()>0){
			for(int i=0;i<iisList.size();i++){
				Node node = (Node)iisList.get(i);  
				 //IIS
		       	if(node.getCategory()==67&&node.isAlarm()){
		       		AlarmVo alarmVo = new AlarmVo();
		        	alarmVo.setType_("IIS");
		        	alarmVo.setIp(node.getIpAddress());
			        alarmVo.setLocation("");
			        alarmVo.setName(node.getAlias());
			        String bus = "";
			        if(allbuss.size()>0){
						for(int j=0;j<allbuss.size();j++){
							Business buss = (Business)allbuss.get(j);
							if(node.getBid()!=null&&node.getBid().contains(buss.getId())){
								bus = bus + buss.getName() + ";";
							}
						}
					}
			        alarmVo.setBussiness("".equals(bus)?"":bus.substring(0, bus.lastIndexOf(";")));
			        alarmVo.setType(node.getType());
			        alarmVo.setLevel1("../resource/"+NodeHelper.getStatusImage(node.getStatus()));
			        flexDataList.add(alarmVo);
		      	}
		    }
		}
       	
		if(socketList!=null&&socketList.size()>0){
			for(int i=0;i<socketList.size();i++){
				Node node = (Node)socketList.get(i);  
				 //Socket
		       	if(node.getCategory()==68&&node.isAlarm()){
		       		AlarmVo alarmVo = new AlarmVo();
		        	alarmVo.setType_("Socket");
		        	alarmVo.setIp(node.getIpAddress());
			        alarmVo.setLocation("");
			        alarmVo.setName(node.getAlias());
			        String bus = "";
			        if(allbuss.size()>0){
						for(int j=0;j<allbuss.size();j++){
							Business buss = (Business)allbuss.get(j);
							if(node.getBid()!=null&&node.getBid().contains(buss.getId())){
								bus = bus + buss.getName() + ";";
							}
						}
					}
			        alarmVo.setBussiness("".equals(bus)?"":bus.substring(0, bus.lastIndexOf(";")));
			        alarmVo.setType(node.getType());
			        alarmVo.setLevel1("../resource/"+NodeHelper.getStatusImage(node.getStatus()));
			        flexDataList.add(alarmVo);
		      	}
		    }
		}
       	
		if(tomcatList!=null&&tomcatList.size()>0){
			for(int i=0;i<tomcatList.size();i++){
				Node node = (Node)tomcatList.get(i);  
				 //Tomcat
		       	if(node.getCategory()==51&&node.isAlarm()){
		       		AlarmVo alarmVo = new AlarmVo();
		        	alarmVo.setType_("Tomcat");
		        	alarmVo.setIp(node.getIpAddress());
			        alarmVo.setLocation("");
			        alarmVo.setName(node.getAlias());
			        String bus = "";
			        if(allbuss.size()>0){
						for(int j=0;j<allbuss.size();j++){
							Business buss = (Business)allbuss.get(j);
							if(node.getBid()!=null&&node.getBid().contains(buss.getId())){
								bus = bus + buss.getName() + ";";
							}
						}
					}
			        alarmVo.setBussiness("".equals(bus)?"":bus.substring(0, bus.lastIndexOf(";")));
			        alarmVo.setType(node.getType());
			        alarmVo.setLevel1("../resource/"+NodeHelper.getStatusImage(node.getStatus()));
			        flexDataList.add(alarmVo);
		      	}
		    }
		}
        
  	    return flexDataList;
	}
	
    public ArrayList getLinkFluxByDate(String id,String dateStr,String time){
		I_HostCollectData hostManager = new HostCollectDataManager();
		ArrayList<LinkFluxVo> flexDataList = new ArrayList<LinkFluxVo>();
		String datestr[] = dateStr.split("=");
		if(datestr.length==2){
			if(java.sql.Date.valueOf(datestr[0]).after(java.sql.Date.valueOf(datestr[1]))){ 
				System.out.println("��ʼ���ڴ��ڽ�ֹ����!");
		    } else {
            	String starttime = datestr[0] + " 00:00:00";
		        String endTime = datestr[1] + " 23:59:59";
		        Hashtable uputilHash = new Hashtable();
				try {
					uputilHash = hostManager.getLkuhdx(id, "UP", starttime, endTime, time);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable downutilHash = new Hashtable();
				try {
					downutilHash = hostManager.getLkuhdx(id, "DOWN", starttime, endTime, time);
				} catch (Exception e) {
					e.printStackTrace();
				}
				List upList = new ArrayList();
				upList = (List) uputilHash.get("list");
				List downList = new ArrayList();
				downList = (List) downutilHash.get("list");
				if(upList!=null&&upList.size()>0&&downList!=null&&downList.size()>0){
					int num = upList.size()>downList.size()?downList.size():upList.size();
					for(int i=0;i<num;i++){
						LinkFluxVo fVo = new LinkFluxVo();
						Vector upfluxVector = new Vector();
						upfluxVector = (Vector) upList.get(i);
						Vector downfluxVector = new Vector();
						downfluxVector = (Vector) downList.get(i);
						if(upfluxVector!=null&&downfluxVector!=null){
							fVo.setDate((String) upfluxVector.get(1));
							fVo.setFluxdown((String) downfluxVector.get(0));
							fVo.setFluxup((String) upfluxVector.get(0));
							flexDataList.add(fVo);
						}
					}
				}
		    }
		}
		return flexDataList;
		
	}
    
    public String getAvgLinkFluxByDate(String id,String dateStr,String time){
		I_HostCollectData hostManager = new HostCollectDataManager();
		String upAvgutil = "";
		String downAvgutil = "";
		String datestr[] = dateStr.split("=");
		if(datestr.length==2){
			if(java.sql.Date.valueOf(datestr[0]).after(java.sql.Date.valueOf(datestr[1]))){ 
				System.out.println("��ʼ���ڴ��ڽ�ֹ����!");
		    } else {
				String date[] = dateStr.split("=");
				String startTime = date[0] + " 00:00:00";
				String endTime = date[1] + " 23:59:59";
				Hashtable uputilHash = new Hashtable();
				try {
					uputilHash = hostManager.getLkuhdx(id, "UP", startTime, endTime, time);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Hashtable downutilHash = new Hashtable();
				try {
					downutilHash = hostManager.getLkuhdx(id, "DOWN", startTime, endTime, time);
				} catch (Exception e) {
					e.printStackTrace();
				}
				upAvgutil = (String) uputilHash.get("avgput");
				downAvgutil = (String) downutilHash.get("avgput");
		    }
		}
		return upAvgutil+","+downAvgutil;
	}
    
    public ArrayList getLinkPingByDate(String id,String dateStr,String time){
		I_HostCollectData hostManager = new HostCollectDataManager();
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		String datestr[] = dateStr.split("=");
		if(datestr.length==2){
			if(java.sql.Date.valueOf(datestr[0]).after(java.sql.Date.valueOf(datestr[1]))){ 
				System.out.println("��ʼ���ڴ��ڽ�ֹ����!");
		    } else {
            	String starttime = datestr[0] + " 00:00:00";
		        String endTime = datestr[1] + " 23:59:59";
		        Hashtable pingHash = new Hashtable();
		        try {
		        	pingHash = hostManager.getLkPing(id, starttime, endTime, time);
				} catch (Exception e) {
					e.printStackTrace();
				}
				List responseList = new ArrayList();
				responseList = (List) pingHash.get("list");
				if(responseList!=null&&responseList.size()>0){
					int num = responseList.size();
					for(int i=0;i<num;i++){
						FlexVo fVo = new FlexVo();
						Vector responseVector = new Vector();
						responseVector = (Vector) responseList.get(i);
						if(responseVector!=null){
							fVo.setObjectName((String) responseVector.get(1));
							fVo.setObjectNumber((String) responseVector.get(0));
							flexDataList.add(fVo);
						}
					}
				}
		    }
		}
		return flexDataList;
	}
    
    public ArrayList getFluxUtilByDate(String id,String dateStr,String time){
    	SysLogger.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&1");
		I_HostCollectData hostManager = new HostCollectDataManager();
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		String datestr[] = dateStr.split("=");
		if(datestr.length==2){
			if(java.sql.Date.valueOf(datestr[0]).after(java.sql.Date.valueOf(datestr[1]))){ 
				System.out.println("��ʼ���ڴ��ڽ�ֹ����!");
		    } else {
            	String starttime = datestr[0] + " 00:00:00";
		        String endTime = datestr[1] + " 23:59:59";
		        Hashtable responseHash = new Hashtable();
		        try {
		        	responseHash = hostManager.getLkuhdxp(id, starttime, endTime, time);
				} catch (Exception e) {
					e.printStackTrace();
				}
				List responseList = new ArrayList();
				responseList = (List) responseHash.get("list");
				if(responseList!=null&&responseList.size()>0){
					int num = responseList.size();
					for(int i=0;i<num;i++){
						FlexVo fVo = new FlexVo();
						Vector responseVector = new Vector();
						responseVector = (Vector) responseList.get(i);
						if(responseVector!=null){
							fVo.setObjectName((String) responseVector.get(1));
							fVo.setObjectNumber((String) responseVector.get(0));
							flexDataList.add(fVo);
						}
					}
				}
		    }
		}
		return flexDataList;
	}
    
    public String getAvgFluxUtilByDate(String id,String dateStr,String time){
		I_HostCollectData hostManager = new HostCollectDataManager();
		String avgUtil = "";
		String datestr[] = dateStr.split("=");
		if(datestr.length==2){
			if(java.sql.Date.valueOf(datestr[0]).after(java.sql.Date.valueOf(datestr[1]))){ 
				System.out.println("��ʼ���ڴ��ڽ�ֹ����!");
		    } else {
            	String starttime = datestr[0] + " 00:00:00";
		        String endTime = datestr[1] + " 23:59:59";
		        Hashtable responseHash = new Hashtable();
		        try {
		        	responseHash = hostManager.getLkuhdxp(id, starttime, endTime, time);
				} catch (Exception e) {
					e.printStackTrace();
				}
				String responseStr = (String) responseHash.get("avgput");
				if(responseStr!=null&&!"".equals(responseStr)){
					avgUtil = responseStr;
				}
		    }
		}
		return avgUtil;
	}
    
  //quzhi add
	public ArrayList<FlexVo> getJbossPingByDate(String id,String dateStr,String timeStr,int x){
		JBossmonitor_historyDao historydao = new JBossmonitor_historyDao();
		ArrayList<FlexVo> flexDataList = new ArrayList<FlexVo>();
		String datestr[] = dateStr.split("=");
		if(datestr.length==2){
			if(java.sql.Date.valueOf(datestr[0]).after(java.sql.Date.valueOf(datestr[1]))){ 
				System.out.println("��ʼ���ڴ��ڽ�ֹ����!");
		    } else {
            	String starttime = datestr[0] + " 00:00:00";
		        String endTime = datestr[1] + " 23:59:59";
		        Hashtable pingHash = new Hashtable();
		        try {
		        	pingHash = historydao.getPingData(Integer.parseInt(id), starttime, endTime, "");
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					historydao.close();
				}
				List pingList = new ArrayList();
				pingList = (List) pingHash.get("list");
				if(pingList!=null&&pingList.size()>0){
				
					int num = pingList.size();
					for(int i=0;i<num;i++){
						FlexVo fVo = new FlexVo();
						Vector pingVector = new Vector();
						pingVector = (Vector) pingList.get(i);
						
						if(pingVector!=null){
							fVo.setObjectName((String) pingVector.get(1));
							if(x==0){
								fVo.setObjectNumber((String) pingVector.get(x)+"00");
							} else {
								fVo.setObjectNumber((String) pingVector.get(x));
							}
							flexDataList.add(fVo);
						}
					}
				}
		    }
		}
		return flexDataList;
	}
	
	public ArrayList getTestAreaByDate(String ipAddress, String dateStr,
			String time) {
		//SysLogger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@1");
		I_HostCollectData hostManager = new HostCollectDataManager();
		ArrayList flexDataList = new ArrayList();
		ArrayList list = new ArrayList();
		String date[] = null;
		String startTime = "";
		String endTime = "";
		if ("".equals(dateStr)) {
			startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date())
					+ " 00:00:00";
			endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date());
		} else {
			date = dateStr.split("=");
			startTime = date[0] + " 00:00:00";
			endTime = date[1] + " 23:59:59";
		}
		DecimalFormat df = new DecimalFormat("#.###");
		Hashtable ipAllData = new Hashtable();
//		��Ҫ���ֲ�ʽ�ж�
		String runmodel = PollingEngine.getCollectwebflag(); 
    	if("0".equals(runmodel)){
			//�ɼ�������Ǽ���ģʽ 
			ipAllData = (Hashtable)ShareData.getSharedata().get(ipAddress);
		}else{
			//�ɼ�������Ƿ���ģʽ 
			ipAllData = (Hashtable)ShareData.getAllNetworkData().get(ipAddress);
		}
		String name = "";
		if (ipAllData != null) {
			Vector diskVector = (Vector) ipAllData.get("disk");
			if (diskVector != null && diskVector.size() > 0) {
				for (int si = 0; si < diskVector.size(); si++) {
					Diskcollectdata diskdata = (Diskcollectdata) diskVector
							.elementAt(si);
					if (diskdata != null) {
						if (diskdata.getEntity().equalsIgnoreCase("AllSize")) {
							name = name + diskdata.getSubentity() + ";";
						}
					}
				}
			}
		}
		String diskname = "";
		String arrName[] = name.split(";");
		// ��ȡӲ����������ʷ����ֵ
		if (arrName != null && arrName.length > 0) {
			//SysLogger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2");
			Hashtable virtualHash[] = new Hashtable[arrName.length];
			for (int i = 0; i < arrName.length; i++) {
				diskname = diskname + subString(arrName[i]);
				if (i != arrName.length - 1) {
					diskname = diskname + ",";
				}
				try {
					virtualHash[i] = hostManager.getDisk(ipAddress, arrName[i],
							startTime, endTime, time);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			flexDataList.add(diskname);
			if (virtualHash[0] != null) {
				List dlist = new ArrayList();
				dlist = (List) virtualHash[0].get("list");
				int dlist_len = dlist.size();
				for (int i = 0; i < dlist_len; i++) {
					Vector v = (Vector) dlist.get(i);
					Hashtable h = new Hashtable();
					h.put("objectName", v.get(3));
					for (int j = 0; j < virtualHash.length; j++) {
						List virtualList = new ArrayList();
						virtualList = (List) virtualHash[j].get("list");
						for (int k = 0; k < virtualList.size(); k++) {
							Vector vector = new Vector();
							vector = (Vector) virtualList.get(k);
							if (vector.get(3).equals(v.get(3))) {
								h.put(subString(arrName[j]), vector.get(0));
								break;
							}
						}
					}
					list.add(h);
				}
			}
			flexDataList.add(list);
		}
		return flexDataList;
	}
	
	public String execute(String action) {
		return null;
	}
//	public ArrayList getCpuDetailByDate(String ipAddress,String dateStr,String time){
//		I_HostCollectData hostManager = new HostCollectDataManager();
//		ArrayList flexDataList = new ArrayList();
//		ArrayList list = new ArrayList();
//		String date[] = null;
//		String startTime = "";
//		String endTime = "";
//		if("".equals(dateStr)){
//			startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
//			endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//		} else {
//			date = dateStr.split("=");
//			startTime = date[0] + " 00:00:00";
//			endTime = date[1] + " 23:59:59";
//		}
//		DecimalFormat df=new DecimalFormat("#.###");
//		Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(ipAddress);
//		String name = "";
//		if (ipAllData != null) {
//			List cpuperflist = (List) ipAllData.get("cpuperflist");
//			if (cpuperflist != null && cpuperflist.size() > 0) {
//				for (int si = 0; si < cpuperflist.size(); si++) {
//					Hashtable cpuhash = (Hashtable) cpuperflist.get(si);
//					Enumeration en = cpuhash.keys();
//					while(en.hasMoreElements()){
//						String key = (String) en.nextElement();
//						if (key!=null&&!"".equals(key)) {
//							name = name + key + ";";
//						}
//					}
//				}
//			}
//		}
//		String cpuname = "";
//		String arrName[] = name.split(";");
//        //��ȡӲ����������ʷ����ֵ
//		if(arrName!=null&&arrName.length>0){
//			Hashtable virtualHash[] = new Hashtable[arrName.length];
//			for(int i=0;i<arrName.length;i++){
//				cpuname = cpuname + toCpuString(arrName[i]);
//				if(i!=arrName.length-1){
//					cpuname = cpuname + ",";
//				}
//				try {
//					virtualHash[i] = hostManager.getCpuPerf(ipAddress,arrName[i], startTime, endTime, time);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			flexDataList.add(cpuname);
//			if(virtualHash[0]!=null){
//				List dlist=new ArrayList();
//				dlist = (List) virtualHash[0].get("list");
//				int dlist_len = dlist.size();
//				for(int i=0;i<dlist_len;i++){
//					Vector v = (Vector) dlist.get(i);
//					Hashtable h =new Hashtable();
//					h.put("objectName", v.get(3));
//					for(int j = 0;j<virtualHash.length;j++){
//						List virtualList = new ArrayList();
//						virtualList = (List) virtualHash[j].get("list");
//						for(int k =0;k<virtualList.size();k++){
//							Vector vector = new Vector();
//							vector = (Vector) virtualList.get(k);
//							if(vector.get(3).equals(v.get(3))){
//								h.put(toCpuString(arrName[j]), vector.get(0));
//								break;
//							}
//						}
//					}
//					list.add(h);
//				}
//			}
//			flexDataList.add(list);
//		}
//		return flexDataList;
//	}
	
	private String toCpuString(String str){
		if("physc".equalsIgnoreCase(str)){
    		str = "����";
    	}else if("%idle".equalsIgnoreCase(str)){
    	    str = "%����";
    	}else if("%iowait".equalsIgnoreCase(str)||"%wio".equalsIgnoreCase(str)){
    	    str = "%io�ȴ�";
    	}else if("%system".equalsIgnoreCase(str)||"%sys".equalsIgnoreCase(str)){
    	    str = "%ϵͳ";
    	}else if("%user".equalsIgnoreCase(str)||"%usr".equalsIgnoreCase(str)){
    	    str = "%�û�";
    	}else if("%nice".equalsIgnoreCase(str)){
    	    str = str;
    	}
		return str;
	}

}
