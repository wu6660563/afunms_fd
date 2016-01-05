/**
 * <p>Description:Tomcat Manager</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-12-07
 */

package com.afunms.application.manage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.apache.commons.beanutils.BeanUtils;

import com.afunms.alarm.dao.AlarmIndicatorsNodeDao;
import com.afunms.alarm.service.NodeAlarmService;
import com.afunms.alarm.util.AlarmConstant;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.application.dao.DBDao;
import com.afunms.application.dao.DBTypeDao;
import com.afunms.application.dao.Db2spaceconfigDao;
import com.afunms.application.dao.InformixspaceconfigDao;
import com.afunms.application.dao.MySqlSpaceConfigDao;
import com.afunms.application.dao.OraclePartsDao;
import com.afunms.application.dao.OraspaceconfigDao;
import com.afunms.application.dao.SqldbconfigDao;
import com.afunms.application.dao.SybspaceconfigDao;
import com.afunms.application.model.DBTypeVo;
import com.afunms.application.model.DBVo;
import com.afunms.application.model.Db2spaceconfig;
import com.afunms.application.model.Informixspaceconfig;
import com.afunms.application.model.MonitorDBDTO;
import com.afunms.application.model.OracleEntity;
import com.afunms.application.model.Oracle_sessiondata;
import com.afunms.application.model.Oraspaceconfig;
import com.afunms.application.model.Sqldbconfig;
import com.afunms.application.model.Sqlserver_processdata;
import com.afunms.application.model.SybaseVO;
import com.afunms.application.model.Sybspaceconfig;
import com.afunms.application.model.TablesVO;
import com.afunms.application.util.DBPool;
import com.afunms.application.util.IpTranslation;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.ChartXml;
import com.afunms.common.util.EncryptUtil;
import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SystemConstant;
import com.afunms.config.dao.SupperDao;
import com.afunms.config.model.Supper;
import com.afunms.event.dao.AlarmInfoDao;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.AlarmInfo;
import com.afunms.event.model.Smscontent;
import com.afunms.indicators.dao.NodeGatherIndicatorsDao;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.indicators.util.NodeGatherIndicatorsUtil;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.loader.DBLoader;
import com.afunms.polling.node.DBNode;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.polling.snmp.LoadDB2File;
import com.afunms.polling.snmp.LoadInformixFile;
import com.afunms.polling.snmp.LoadMySqlFile;
import com.afunms.polling.snmp.LoadOracleFile;
import com.afunms.polling.snmp.LoadSQLServerFile;
import com.afunms.polling.snmp.LoadSysbaseFile;
import com.afunms.polling.snmp.db.DB2DataCollector;
import com.afunms.polling.snmp.db.InformixDataCollector;
import com.afunms.polling.snmp.db.MySqlDataCollector;
import com.afunms.polling.snmp.db.OracleDataCollector;
import com.afunms.polling.snmp.db.SQLServerDataCollector;
import com.afunms.polling.snmp.db.SybaseDataCollector;
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

public class DataBaseManager extends BaseManager implements ManagerInterface {
	
	private Hashtable sendeddata = ShareData.getSendeddata();

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	
//	private String list() {
//		User current_user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
//		String curentbids = current_user.getBusinessids();
//		DBDao dao = new DBDao();
//		List list = new ArrayList();
//		List selbids = new ArrayList();
//		String selectbids = getParaValue("selectbids");
//		try {
//			if(selectbids != null && selectbids.trim().length()>0){
//				//�������Ĳ�ѯ
//				//list = dao.getDbByBID(selectbids);
//				list = dao.getDbByBIDUnderCurrentUser(selectbids, curentbids);
//			}else{
//				//�������������,���ѯ��ǰ�û�������ҵ���µ����ݿ�
//				System.out.println(curentbids);
//				//list = dao.getDbByBID(curentbids);
//				list = dao.getDbByBIDUnderCurrentUser(selectbids, curentbids);
//			}
//			//list = dao.loadAll();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			dao.close();
//		}
//		/*---------------- modify  zhao--------*/
//		DBTypeDao typedao = new DBTypeDao();
//		DBTypeVo oraVo = null;
//		try {
//			oraVo = (DBTypeVo) typedao.findByDbtype("oracle");
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			typedao.close();
//		}
//		/* nielin add  at 2010-08-13 =========================================strat
//		 */
//		Hashtable hashtable = new Hashtable();
//		
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		String date = simpleDateFormat.format(new Date());
//		
//		String starttime = date + " 00:00:00";
//		String totime = date + " 23:59:59";
//		/* nielin add  at 2010-08-13 =========================================end
//		 */
//
//
//		/*---------------modify  end-------------*/
//		List addtion = new LinkedList();
//		for (int i = 0; i < list.size(); i++) {
//			DBVo vo = (DBVo) list.get(i);
//			Node DBNode = PollingEngine.getInstance().getDbByID(vo.getId());
//			if (DBNode == null) {
//				vo.setStatus(1);
//			} else {
//				vo.setStatus(DBNode.getStatus());
//			}
//			/*---------------mofify  zhao*/
//			
//			/* nielin add  at 2010-08-13 =========================================strat
//			 */
//			try {
//				Hashtable eventListSummary = new Hashtable();
//				String generalAlarm = "0"; 				// ��ͨ�澯��  Ĭ��Ϊ 0
//				String urgentAlarm = "0"; 				// ���ظ澯��  Ĭ��Ϊ 0
//				String seriousAlarm = "0";				// �����澯��  Ĭ��Ϊ 0
//				
//				EventListDao eventListDao = new EventListDao();
//				try {
//					generalAlarm = eventListDao.getCountByWhere(" where nodeid='" + vo.getId() + "'" + " and level1='1' and recordtime>='" + starttime + "' and recordtime<='" + totime + "'");
//					urgentAlarm = eventListDao.getCountByWhere(" where nodeid='" + vo.getId() + "'" + " and level1='2' and recordtime>='" + starttime + "' and recordtime<='" + totime + "'");
//					seriousAlarm = eventListDao.getCountByWhere(" where nodeid='" + vo.getId() + "'" + " and level1='3' and recordtime>='" + starttime + "' and recordtime<='" + totime + "'");
//				} catch (RuntimeException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} finally{
//					eventListDao.close();
//				}
//				eventListSummary.put("generalAlarm", generalAlarm);
//				eventListSummary.put("urgentAlarm", urgentAlarm);
//				eventListSummary.put("seriousAlarm", seriousAlarm);
//				
//				hashtable.put(vo.getId()+"", eventListSummary);
//			} catch (RuntimeException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			
//			/* nielin add  at 2010-08-13 =========================================end
//			 */
//
//			if (vo.getDbtype() == oraVo.getId()) {
//				OraclePartsDao odao = null;
//				List oracles = new ArrayList();
//				try {
//					odao = new OraclePartsDao();
//					oracles = odao.findOracleParts(vo.getId());
//				} catch (Exception e) {
//					e.printStackTrace();
//				} finally {
//					odao.close();
//				}
//
//				boolean flag = false;
//				for (int j = 0; j < oracles.size(); j++) {
//					OracleEntity ora = (OracleEntity) oracles.get(j);
//					DBVo ovo = new DBVo();
//					ovo.setAlias(vo.getAlias());
//					ovo.setBid(vo.getBid());
//					ovo.setCategory(vo.getCategory());
//					ovo.setDbName(ora.getSid());
//					ovo.setDbtype(vo.getDbtype());
//					ovo.setDbuse(vo.getDbuse());
//					ovo.setIpAddress(vo.getIpAddress() + ":" + ora.getId());
//					//ovo.setManaged(vo.getManaged());
//					ovo.setPassword(ora.getPassword());
//					ovo.setPort(vo.getPort());
//					ovo.setSendemail(ora.getGzerid());
//					ovo.setSendmobiles(vo.getSendmobiles());
//					ovo.setStatus(vo.getStatus());
//					ovo.setUser(ora.getUser());
//					ovo.setId(vo.getId());
//					ovo.setAlias(ora.getAlias());
//					ovo.setManaged(ora.getManaged());
//
//					addtion.add(ovo);
//				}
//			} else {
//				addtion.add(vo);
//			}
//			/*----------------------modify   end -----------*/
//		}
//		/*for(int i=0;i<addtion.size();i++){
//			list.add(addtion.get(i));
//		}*/
//		request.setAttribute("list", addtion);
//		List selectbuslist = new ArrayList();
//		Hashtable allbuss = new Hashtable();
//		BusinessDao bussdao = new BusinessDao();
//		List allbusslist = new ArrayList();
//		try{
//			allbusslist = bussdao.loadAll();
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally{
//			bussdao.close();
//		}
//		if(allbusslist != null && allbusslist.size()>0){
//			for(int i=0;i<allbusslist.size();i++){
//				Business buss = (Business)allbusslist.get(i);
//				allbuss.put(buss.getId()+"", buss);
//			}
//			
//		}
//
//		String[]curbids = null;
//		if(curentbids != null && curentbids.trim().length()>0){
//			curbids = curentbids.split(",");
//			if(curbids != null && curbids.length > 0){
//				for(int i=0;i<curbids.length;i++){
//					if(curbids[i] != null && curbids[i].trim().length()>0){
//						//��ӵ��б���
//						if(allbuss.containsKey(curbids[i])){
//							selectbuslist.add((Business)allbuss.get(curbids[i]));
//						}
//					}
//				}
//			}
//		}
//		request.setAttribute("selectbuslist", selectbuslist);
//		request.setAttribute("selectbids", selectbids);
//		request.setAttribute("allbusslist", allbusslist);
//		
//		request.setAttribute("eventlistHashtable", hashtable);
//		
//		
//		return "/application/db/list.jsp";
//	}
	
	private String list() {
		
		List list = getList();
		
		String flag = (String)request.getAttribute("flag");
		String treeBid = (String)request.getAttribute("treeBid");
		DBTypeVo oraVo = null;
		DBTypeDao typedao = new DBTypeDao();
		try {
			oraVo = (DBTypeVo) typedao.findByDbtype("Oracle");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			typedao.close();
		}
		
		List monitorDBDTOList = new ArrayList();

		for (int i = 0; i < list.size(); i++) {
			DBVo vo = (DBVo) list.get(i);
			
			MonitorDBDTO monitorDBDTO = null;
			
			if (vo.getDbtype() == oraVo.getId()) {
				OraclePartsDao odao = null;
				List oracles = new ArrayList();
				try {
					odao = new OraclePartsDao();
					if("1".equals(flag)){
						oracles = odao.findOracleParts(vo.getId(),1);
					}else{
						oracles = odao.findOracleParts(vo.getId());
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					odao.close();
				}
				for (int j = 0; j < oracles.size(); j++) {
					OracleEntity ora = (OracleEntity) oracles.get(j);
					
					String ipAddress = vo.getIpAddress();
					
					monitorDBDTO = getMonitorDBDTOByDBVo(vo,ora.getId());
					
					monitorDBDTO.setSid(String.valueOf(ora.getId()));
					
					monitorDBDTO.setIpAddress(ipAddress);
					
					monitorDBDTO.setDbname(ora.getSid());
					
					monitorDBDTO.setAlias(ora.getAlias());
					
					if(ora.getManaged() == 1){
						monitorDBDTO.setMon_flag("��");
					}else{
						monitorDBDTO.setMon_flag("��");
					}
										
					monitorDBDTOList.add(monitorDBDTO);
				}
			}else {
				monitorDBDTO = getMonitorDBDTOByDBVo(vo,0);
				monitorDBDTOList.add(monitorDBDTO);
			}
			
		}
		if(monitorDBDTOList == null){
			monitorDBDTOList = new ArrayList();
		}
		request.setAttribute("list", monitorDBDTOList);
		
		return "/application/db/dblist.jsp";
	}
	
	/**
     * ��ȡ list
     * @author nielin
     * @date 2010-08-13
     * @param <code>DBVo</code>
     * @return
     */
	public List getList(){
		
		List list = new ArrayList();
		
		String sql = getBidSql();
		
		if(sql != null){
			//sql = sql + getMonFlagSql();
		}
		
		DBDao dao = new DBDao();
		try {
			list = dao.findByCriteria(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		
		return list;
	}
	
	
	/**
     * ���ҵ��Ȩ�޵� SQL ���
     * @author nielin
     * @date 2010-08-13
     * @return
     */
    public String getBidSql(){
    	
    	User current_user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
		String curentbids = current_user.getBusinessids();
		
    	String selectbids = getParaValue("selectbids");
    	
    	StringBuffer sql1 = new StringBuffer();
		StringBuffer s1 = new StringBuffer();
		StringBuffer sql2 = new StringBuffer();
		StringBuffer s2 = new StringBuffer();
		int flag = 0;
		
		
		if (selectbids != null) {
			if (selectbids != "-1") {
				String[] bids = selectbids.split(",");
				if (bids.length > 0) {
					for (int i = 0; i < bids.length; i++) {
						if (bids[i].trim().length() > 0) {
							if (flag == 0) {
								//s.append(" and ( bid like '%," + bids[i].trim() + ",%' ");
								s2.append(" and ( bid like '%" + bids[i].trim() + "%' ");
								flag = 1;
							} else {
								//flag = 1;
								//s.append(" or bid like '%," + bids[i].trim() + ",%' ");
								s2.append(" or bid like '%" + bids[i].trim() + "%' ");
							}
						}
					}
					s2.append(") ");
				}

			}
		}
		sql2.append("select * from app_db_node where 1=1 " + s2.toString());
		
		flag = 0;
		if (current_user.getRole() != 0 &&  curentbids != null) {
			System.out.println("%%%%%%%%%%%%%%%%%%"+curentbids);
			if (curentbids != "-1") {
				String[] bids = curentbids.split(",");
				if (bids.length > 0) {
					for (int i = 0; i < bids.length; i++) {
						if (bids[i].trim().length() > 0) {
							if (flag == 0) {
								//s.append(" and ( bid like '%," + bids[i].trim() + ",%' ");
								s1.append(" and ( bid like '%" + bids[i].trim() + "%' ");
								flag = 1;
							} else {
								//flag = 1;
								//s.append(" or bid like '%," + bids[i].trim() + ",%' ");
								s1.append(" or bid like '%" + bids[i].trim() + "%' ");
							}
						}
					}
					s1.append(") ");
				}

			}
		}
		sql1.append("select * from ("+ sql2.toString() +") as t where 1=1 " + s1.toString());
		
		String treeBid = request.getParameter("treeBid");
		if(treeBid != null && treeBid.trim().length() > 0){
			treeBid = treeBid.trim();
			treeBid = "," + treeBid + ",";
			String[] treeBids = treeBid.split(",");
			if(treeBids != null){
				for(int i = 0; i < treeBids.length; i++){
					if(treeBids[i].trim().length() > 0){
						sql1 = sql1.append(" and " + "bid" + " like '%," + treeBids[i].trim() + ",%'");
					}
				}
			}
		}
//		SysLogger.info(sql1.toString());
		return sql1.toString();
		
    }
    
    public String getMonFlagSql(){
    	String mon_flag = getParaValue("flag");
    	
    	String sql = "";
    	if(mon_flag !=null && "1".equals(mon_flag)){
    		sql = " and managed='1'";
    	}
    	
    	request.setAttribute("flag", mon_flag);
    	
    	return sql;
    }
	
	
    /**
     * ͨ�� DBVo ����װ MonitorDBDTO
     * @author nielin
     * @date 2010-08-13
     * @param <code>DBVo</code>
     * @return
     */
    public MonitorDBDTO getMonitorDBDTOByDBVo(DBVo vo , int sid){
    	
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = simpleDateFormat.format(new Date());
		
		String starttime = date + " 00:00:00";
		String totime = date + " 23:59:59";
		
		int id = vo.getId(); 									// id
		String ipAddress = vo.getIpAddress();					// ipaddress			
		String alias = vo.getAlias();							// ����
		String dbname = vo.getDbName();							// ���ݿ�����
		String port = vo.getPort();								// �˿�	
		String mon_flag = "��";
		
		String dbtype = "";										// ���ݿ�����
		
		String status = "";										// ״̬
		
		String pingValue = "";									// ������

		Hashtable eventListSummary = new Hashtable();			// �澯
		
		if(vo.getManaged() == 1){
			mon_flag = "��";
		}
		
		/*===========for dbtype start==================*/
		DBTypeDao typedao = new DBTypeDao();
		DBTypeVo dbTypeVo = null;
		try {
			dbTypeVo = (DBTypeVo) typedao.findByID(String.valueOf(vo.getDbtype()));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			typedao.close();
		}
		if(dbTypeVo!=null){
			dbtype = dbTypeVo.getDbtype();
		}else {
			dbtype = "δ֪";
		}
		
		if("Oracle".equalsIgnoreCase(dbtype)){
			id = sid;
		}
		
		/*===========for dbtype end==================*/
		
		
		/*===========for status start==================*/
//      Node DBNode = PollingEngine.getInstance().getDbByID(vo.getId());
//      if (DBNode == null) {
//          status = "0";
//      } else {
//          status = DBNode.getStatus() + "";
//      }
        NodeUtil nodeUtil = new NodeUtil();
        NodeAlarmService nodeAlarmService = new NodeAlarmService();
        if("Oracle".equalsIgnoreCase(dbtype)){
            vo.setId(sid);
        }
        int maxAlarmLevel = nodeAlarmService.getMaxAlarmLevel(nodeUtil.conversionToNodeDTO(vo));
        status = String.valueOf(maxAlarmLevel);
        /*===========for status start==================*/
		
		/*===========for pingValue start==================*/
		Hashtable dbdata = null;
		
		pingValue = "����ֹͣ";
		
		if ("Oracle".equalsIgnoreCase(dbtype)){
			//2010-HONGLI
			Hashtable memPerfValue = new Hashtable();
			DBDao dao = new DBDao();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(ipAddress);
			String serverip = hex+":"+sid;
			Hashtable statusHashtable;
			try {
				statusHashtable = dao.getOracle_nmsorastatus(serverip);//ȡ״̬��Ϣ
				memPerfValue = dao.getOracle_nmsoramemperfvalue(serverip);
				String statusStr = String.valueOf(statusHashtable.get("status"));
				if("1".equals(statusStr)){
					pingValue = "��������";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				dao.close();
			}
//			Hashtable alloracledata = ShareData.getAlloracledata();
//			if(alloracledata != null && alloracledata.size()>0){
//				if(alloracledata.containsKey(vo.getIpAddress())){
//					Hashtable iporacledata = (Hashtable)alloracledata.get(vo.getIpAddress());
//					if(iporacledata.containsKey("status")){
//						String sta=(String)iporacledata.get("status");
//						if("1".equals(sta)){
//							pingValue = "��������";
//						}
//					}
//				}
//			}
		}else if ("SQLServer".equalsIgnoreCase(dbtype)){
//			Hashtable allsqlserverdata = ShareData.getSqlserverdata();
//			if(allsqlserverdata != null && allsqlserverdata.size()>0){
//				if(allsqlserverdata.containsKey(vo.getIpAddress())){
//					Hashtable ipsqlserverdata = (Hashtable)allsqlserverdata.get(vo.getIpAddress());
//					if(ipsqlserverdata.containsKey("status")){
//						String p_status = (String)ipsqlserverdata.get("status");
//						if(p_status != null && p_status.length()>0){
//							if("1".equalsIgnoreCase(p_status)){
//								pingValue = "��������";
//							}
//						}
//					}
//				}
//			}
			DBDao dbDao = new DBDao();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+vo.getAlias();
			Hashtable sqlValue = new Hashtable();
			Hashtable statusHash;
			try {
				statusHash = dbDao.getSqlserver_nmsstatus(serverip);
				String p_status = (String)statusHash.get("status");
				if(p_status != null && p_status.length()>0){
					if("1".equalsIgnoreCase(p_status)){
						pingValue = "��������";
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				dbDao.close();
			}
		}else if ("MySql".equalsIgnoreCase(dbtype)){
//			dbdata = ShareData.getMySqlmonitordata();
//			if(dbdata != null && dbdata.size()>0){
//				Hashtable ipData = (Hashtable)dbdata.get(vo.getIpAddress());
//				if(ipData != null && ipData.size()>0){
//					pingValue = (String)ipData.get("runningflag");
//				}
//			}
			DBDao dao = new DBDao();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+vo.getId();
			Hashtable ipData = dao.getMysqlDataByServerip(serverip);
			Hashtable statusHashtable;
			try {
				statusHashtable = dao.getMysql_nmsstatus(serverip);
				String statusStr = (String)statusHashtable.get("status");
				if("1".equals(statusStr)){
					pingValue = "��������";
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if(dao != null){
					dao.close();
				}
			}
		}else if ("DB2".equalsIgnoreCase(dbtype)){
//			Hashtable allDb2data = ShareData.getAlldb2data();
//			Hashtable ipDb2data = new Hashtable();
//			if(allDb2data != null && allDb2data.size()>0){
//				if(allDb2data.containsKey(vo.getIpAddress())){
//					ipDb2data = (Hashtable)allDb2data.get(vo.getIpAddress());
//					if(ipDb2data.containsKey("status")){
//						String p_status = (String)ipDb2data.get("status");
//						if(p_status != null && p_status.length()>0){
//							if("1".equalsIgnoreCase(p_status)){
//								pingValue = "��������";
//							}
//						}
//					}
//				}
//			}
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			DBDao dao = new DBDao();
			String serverip = hex+":"+vo.getId();
			String statusStr = "0";
			Hashtable tempStatusHashtable = null;
			try {
				tempStatusHashtable = dao.getDB2_nmsstatus(serverip);
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				if(dao != null){
					dao.close();
				}
			}
			if(tempStatusHashtable != null && tempStatusHashtable.containsKey("status")){
				statusStr = (String)tempStatusHashtable.get("status");
			}
			if(statusStr.equals("1")){
				pingValue = "��������";
			}
		}else if ("Sybase".equalsIgnoreCase(dbtype)){
//			Hashtable sysValue = new Hashtable();
//			Hashtable sValue = new Hashtable();
//			sysValue = ShareData.getSysbasedata();
			//sValue = (Hashtable)sysValue.get(vo.getIpAddress());
//			if(sysValue!=null&&sysValue.size()>0){
//				if(sysValue.containsKey(vo.getIpAddress())){
//					sValue = (Hashtable)sysValue.get(vo.getIpAddress());
//					if(sValue.containsKey("status")){
//						String p_status = (String)sValue.get("status");
//						if(p_status != null && p_status.length()>0){
//							if("1".equalsIgnoreCase(p_status)){
//								pingValue = "��������";
//							}
//						}
//					}
//				}
//			}
			//��ȡsybase��Ϣ
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			DBDao dao = new DBDao();
			String serverip = hex+":"+vo.getId();
			String statusStr = "0";
			Hashtable tempStatusHashtable = null;
			try {
				tempStatusHashtable = dao.getSybase_nmsstatus(serverip);
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				if(dao != null){
					dao.close();
				}
			}
			if(tempStatusHashtable != null && tempStatusHashtable.containsKey("status")){
				statusStr = (String)tempStatusHashtable.get("status");
			}
			if(statusStr.equals("1")){
				pingValue = "��������";
			}
		}else if ("Informix".equalsIgnoreCase(dbtype)){
//			Hashtable sysValue = new Hashtable();
//			Hashtable sValue = new Hashtable();
//			Hashtable informixData=new Hashtable();
//			Hashtable mino=new Hashtable();
//			sysValue = ShareData.getInformixmonitordata();
//			if(sysValue!=null&&sysValue.size()>0){
//				if(sysValue.containsKey(vo.getIpAddress())){
//					mino=(Hashtable)sysValue.get(vo.getIpAddress());
//					informixData=(Hashtable)mino.get(vo.getDbName());
//					if(informixData == null)informixData = new Hashtable();
//					if(informixData.containsKey("status")){
//						String p_status = (String)informixData.get("status");
//						if(p_status != null && p_status.length()>0){
//							if("1".equalsIgnoreCase(p_status)){
//								pingValue = "��������";
//							}
//						}
//					}
//				}
//			}
			DBDao dao = new DBDao();
			IpTranslation tranfer = new IpTranslation();
			String hex = tranfer.formIpToHex(vo.getIpAddress());
			String serverip = hex+":"+vo.getDbName();
			String statusStr;
			try {
				statusStr = String.valueOf(((Hashtable)dao.getInformix_nmsstatus(serverip)).get("status"));
				if("1".equalsIgnoreCase(statusStr)){
					pingValue = "��������";
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				dao.close();
			}
		}
		/*===========for pingValue end==================*/
		
		
		
		/*===========for eventListSummary start==================*/
       	String generalAlarm = "0"; 				// ��ͨ�澯��  Ĭ��Ϊ 0
		String urgentAlarm = "0"; 				// ���ظ澯��  Ĭ��Ϊ 0
		String seriousAlarm = "0";				// �����澯��  Ĭ��Ϊ 0
		
//		EventListDao eventListDao = new EventListDao();
//		try {
//			generalAlarm = eventListDao.getCountByWhere(" where nodeid='" + id + "'" + " and level1='1' and recordtime>='" + starttime + "' and recordtime<='" + totime + "'");
//			urgentAlarm = eventListDao.getCountByWhere(" where nodeid='" + id + "'" + " and level1='2' and recordtime>='" + starttime + "' and recordtime<='" + totime + "'");
//			seriousAlarm = eventListDao.getCountByWhere(" where nodeid='" + id + "'" + " and level1='3' and recordtime>='" + starttime + "' and recordtime<='" + totime + "'");
//		} catch (RuntimeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally{
//			eventListDao.close();
//		}
		eventListSummary.put("generalAlarm", generalAlarm);
		eventListSummary.put("urgentAlarm", urgentAlarm);
		eventListSummary.put("seriousAlarm", seriousAlarm);
		/*===========for eventListSummary end==================*/
		
		
		MonitorDBDTO monitorDBDTO = new MonitorDBDTO();
		monitorDBDTO.setId(id);
		monitorDBDTO.setAlias(alias);
		monitorDBDTO.setDbname(dbname);
		monitorDBDTO.setDbtype(dbtype);
		monitorDBDTO.setPingValue(pingValue);
		monitorDBDTO.setEventListSummary(eventListSummary);
		monitorDBDTO.setIpAddress(ipAddress);
		monitorDBDTO.setPort(port);
		monitorDBDTO.setStatus(status);
		monitorDBDTO.setMon_flag(mon_flag);
		
    	return monitorDBDTO;
    }
	
	private String add() {
		DBVo vo = new DBVo();

		vo.setUser(getParaValue("user"));
		String password = getParaValue("password");
		String flag = getParaValue("flag");
		
		String enpassword = "";
		try{
			enpassword = EncryptUtil.encode(password);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//vo.setPassword(getParaValue("password"));
		vo.setPassword(enpassword);
		vo.setAlias(getParaValue("alias"));
		vo.setIpAddress(getParaValue("ip_address"));
		vo.setPort(getParaValue("port"));
		vo.setDbName(getParaValue("db_name"));
		vo.setCategory(getParaIntValue("category"));
		vo.setDbuse(getParaValue("dbuse"));
		vo.setSendmobiles(getParaValue("sendmobiles"));
		vo.setSendemail(getParaValue("sendemail"));
		vo.setSendphone(getParaValue("sendphone"));
		String allbid = this.getParaValue("bid");
		vo.setBid(allbid);
		vo.setManaged(getParaIntValue("managed"));
		vo.setDbtype(getParaIntValue("dbtype"));
		vo.setCollecttype(getParaIntValue("collecttype"));
		vo.setSupperid(getParaIntValue("supperid"));//snow add 2010-05-20
		/* modify   zhao     -----------------*/
		List<OracleEntity> addoracles = new LinkedList<OracleEntity>();
		DBTypeDao typedao = new DBTypeDao();
		DBTypeVo oracletypevo = null;
		try {
			oracletypevo = (DBTypeVo) typedao.findByDbtype("oracle");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			typedao.close();
		}

		if (getParaIntValue("dbtype") == oracletypevo.getId()) {
			List list = null;
			DBDao bdao = null;
			try {
				bdao = new DBDao();
				list = bdao.getDbByTypeAndIpaddress(oracletypevo.getId(), vo.getIpAddress());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				bdao.close();
			}
			if (list == null || list.size() == 0) {
				vo.setId(KeyGenerator.getInstance().getNextKey());
				DBDao dao = new DBDao();
				try {
					dao.save(vo);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					dao.close();
				}
			} else {
				DBVo first = (DBVo) list.get(0);
				vo.setId(first.getId());
			}
			OraclePartsDao odao = new OraclePartsDao();
			OracleEntity oracle = new OracleEntity();
			oracle.setDbid(vo.getId());
			oracle.setPassword(vo.getPassword());
			oracle.setSid(vo.getDbName());
			oracle.setUser(vo.getUser());
			oracle.setGzerid(vo.getSendemail());
			oracle.setId(KeyGenerator.getInstance().getNextKey());
			oracle.setAlias(vo.getAlias());
			oracle.setCollectType(vo.getCollecttype());
			oracle.setManaged(vo.getManaged());
			oracle.setBid(vo.getBid());
			try {
				odao.save(oracle);
				//��ʼ���ɼ�ָ��
   				try {
						NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
						nodeGatherIndicatorsUtil.addGatherIndicatorsForNode(oracle.getId()+"", AlarmConstant.TYPE_DB, "oracle","1");
   				} catch (RuntimeException e) {
   					// TODO Auto-generated catch block
   					e.printStackTrace();
   				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (odao != null)
					odao.close();
			}

			DBLoader dbloader = new DBLoader();
			int tid=vo.getId();
			vo.setId(oracle.getId());
			dbloader.loadOne(vo);
			vo.setId(tid);
			try {
				// nielin add for time-sharing at 2009-01-04
				TimeShareConfigUtil timeShareConfigUtil = new TimeShareConfigUtil();
				try {
					boolean resutl = timeShareConfigUtil.saveTimeShareConfigList(request,
							vo.getId() + ":oracle" + oracle.getId(), timeShareConfigUtil.getObjectType("1"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try{
				execute(vo);
			}catch(Exception e){
				
			}
			
			DBTypeVo dbTypeVo = null;
			try {
				typedao = new DBTypeDao();
				dbTypeVo = (DBTypeVo) typedao.findByID(getParaValue("dbtype"));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				typedao.close();
			}
			//��ʼ���澯ָ��
			try {
				AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
				alarmIndicatorsUtil.saveAlarmInicatorsThresholdForNode(String.valueOf(oracle.getId()), AlarmConstant.TYPE_DB, dbTypeVo.getDbtype());
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//��ORACLE���ݿ�������ݲɼ�
			NodeGatherIndicatorsDao indicatorsdao = new NodeGatherIndicatorsDao();
	    	List<NodeGatherIndicators> monitorItemList = new ArrayList<NodeGatherIndicators>(); 	
	    	Hashtable<String,Hashtable<String,NodeGatherIndicators>> oracleHash = new Hashtable<String,Hashtable<String,NodeGatherIndicators>>();//�����Ҫ���ӵ�DB2ָ��  <dbid:Hashtable<name:NodeGatherIndicators>>
	    	try{
	    		//��ȡ�����õ�ORACLE���б�����ָ��
	    		monitorItemList = indicatorsdao.getByNodeId(oracle.getId()+"",1,"db","oracle");
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}finally{
	    		indicatorsdao.close();
	    	}
	    	if(monitorItemList == null)monitorItemList = new ArrayList<NodeGatherIndicators>();
	    	Hashtable gatherHash = new Hashtable();
	    	for(int i=0;i<monitorItemList.size();i++){
	    		NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators)monitorItemList.get(i);
	    		//ORACLE���ݿ�
	    		gatherHash.put(nodeGatherIndicators.getName(), nodeGatherIndicators);
	    	}
        	//OracleDataCollector oraclecollector = new OracleDataCollector();
        	//SysLogger.info("##############################");
        	//SysLogger.info("### ��ʼ�ɼ�IDΪ"+oracle.getId()+"��ORACLE���� ");
        	//SysLogger.info("##############################");
        	//oraclecollector.collect_data(oracle.getId()+"", gatherHash);	
			
			return list();
		} else {
			/* modify  end -----------------------------------*/

			//�����ݿ������ӱ����ָ��
			//DiscoverCompleteDao dcDao = new DiscoverCompleteDao();
			//dcDao.addDBMonitor(vo.getId(),vo.getIpAddress(),"mysql");
			//����ѯ�߳������ӱ����ӽڵ�
			//DBLoader loader = new DBLoader();
			//loader.loadOne(vo);
			//loader.close();
			vo.setId(KeyGenerator.getInstance().getNextKey());
			DBLoader dbloader = new DBLoader();
			dbloader.loadOne(vo);
			DBDao dao = new DBDao();
			try {
				dao.save(vo);
				// nielin add for time-sharing at 2009-01-04
				TimeShareConfigUtil timeShareConfigUtil = new TimeShareConfigUtil();
				try {
					boolean resutl = timeShareConfigUtil.saveTimeShareConfigList(request, String.valueOf(vo.getId()),
							timeShareConfigUtil.getObjectType("1"));
				} catch (Exception e) {
					e.printStackTrace();
				}
	            /* snow add at 2010-5-19 */
	            TimeGratherConfigUtil timeGratherConfigUtil = new TimeGratherConfigUtil();
	            try {
					boolean result2 = timeGratherConfigUtil.saveTimeGratherConfigList(request, String.valueOf(vo.getId()), timeGratherConfigUtil.getObjectType("1"));
				} catch (Exception e) {
					e.printStackTrace();
				}
	      		/* snow add end*/
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dao.close();
			}
//			try{
//				execute(vo);
//			}catch(Exception e){
//				
//			}
			//DBTask dbtask = new DBTask();
			//dbtask.run();
			
			
			DBTypeVo dbTypeVo = null;
			try {
				typedao = new DBTypeDao();
				dbTypeVo = (DBTypeVo) typedao.findByID(getParaValue("dbtype"));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				typedao.close();
			}
			
			//��ʼ���澯ָ��
			try {
				AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
				alarmIndicatorsUtil.saveAlarmInicatorsThresholdForNode(String.valueOf(vo.getId()), AlarmConstant.TYPE_DB, dbTypeVo.getDbtype());
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//��ʼ���ɼ�ָ��
			try {
				NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
				nodeGatherIndicatorsUtil.addGatherIndicatorsForNode(vo.getId()+"", AlarmConstant.TYPE_DB, dbTypeVo.getDbtype(),"1");
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//�����ݿ�������ݲɼ�
			NodeGatherIndicatorsDao indicatorsdao = new NodeGatherIndicatorsDao();
	    	List<NodeGatherIndicators> monitorItemList = new ArrayList<NodeGatherIndicators>(); 	
	    	Hashtable<String,Hashtable<String,NodeGatherIndicators>> oracleHash = new Hashtable<String,Hashtable<String,NodeGatherIndicators>>();//�����Ҫ���ӵ�DB2ָ��  <dbid:Hashtable<name:NodeGatherIndicators>>
	    	try{
	    		//��ȡ�����õ����ݿ����б�����ָ��
	    		monitorItemList = indicatorsdao.getByNodeId(vo.getId()+"",1,"db",dbTypeVo.getDbtype());
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}finally{
	    		indicatorsdao.close();
	    	}
	    	if(monitorItemList == null)monitorItemList = new ArrayList<NodeGatherIndicators>();
	    	Hashtable gatherHash = new Hashtable();
	    	for(int i=0;i<monitorItemList.size();i++){
	    		NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators)monitorItemList.get(i);
	    		//���ݿ�ɼ�ָ��
	    		gatherHash.put(nodeGatherIndicators.getName(), nodeGatherIndicators);
	    	}
	    	
	    	/*
	    	if("sqlserver".equalsIgnoreCase(dbTypeVo.getDbtype()) ){
	    		SQLServerDataCollector sqlservercollector = new SQLServerDataCollector();
	        	SysLogger.info("##############################");
	        	SysLogger.info("### ��ʼ�ɼ�IDΪ"+vo.getId()+"��SQLSERVER���� ");
	        	SysLogger.info("##############################");
	        	sqlservercollector.collect_data(vo.getId()+"", gatherHash);
	    	}else if("sybase".equalsIgnoreCase(dbTypeVo.getDbtype()) ){
	    		SybaseDataCollector dbcollector = new SybaseDataCollector();
	        	SysLogger.info("##############################");
	        	SysLogger.info("### ��ʼ�ɼ�IDΪ"+vo.getId()+"��Sybase���� ");
	        	SysLogger.info("##############################");
	        	dbcollector.collect_data(vo.getId()+"", gatherHash);
	    	}else if("db2".equalsIgnoreCase(dbTypeVo.getDbtype()) ){
	    		DB2DataCollector dbcollector = new DB2DataCollector();
	        	SysLogger.info("##############################");
	        	SysLogger.info("### ��ʼ�ɼ�IDΪ"+vo.getId()+"��DB2���� ");
	        	SysLogger.info("##############################");
	        	dbcollector.collect_data(vo.getId()+"", gatherHash);
	    	}else if("informix".equalsIgnoreCase(dbTypeVo.getDbtype()) ){
	    		InformixDataCollector dbcollector = new InformixDataCollector();
	        	SysLogger.info("##############################");
	        	SysLogger.info("### ��ʼ�ɼ�IDΪ"+vo.getId()+"��Informix���� ");
	        	SysLogger.info("##############################");
	        	dbcollector.collect_data(vo.getId()+"", gatherHash);
	    	}else if("mysql".equalsIgnoreCase(dbTypeVo.getDbtype()) ){
	    		MySqlDataCollector dbcollector = new MySqlDataCollector();
	        	SysLogger.info("##############################");
	        	SysLogger.info("### ��ʼ�ɼ�IDΪ"+vo.getId()+"��MySql���� ");
	        	SysLogger.info("##############################");
	        	dbcollector.collect_data(vo.getId()+"", gatherHash);
	    	}
	    	*/
        	
			
			return list();
		}
	}

	public String delete() {
		String id = getParaValue("radio");
		String[] arr = id.split(":");
		boolean flag = true;
		DBTypeVo sqlservertypevo = null;
		DBTypeVo db2typevo = null;
		DBTypeVo sybasetypevo = null;
		DBTypeVo infomixtypevo = null;
		DBTypeVo mysqltypevo = null;
		DBTypeDao typedao = new DBTypeDao();
		try {
			sqlservertypevo = (DBTypeVo) typedao.findByDbtype("sqlserver");
			db2typevo = (DBTypeVo) typedao.findByDbtype("db2");
			sybasetypevo = (DBTypeVo) typedao.findByDbtype("sybase");
			infomixtypevo = (DBTypeVo) typedao.findByDbtype("informix");
			mysqltypevo = (DBTypeVo) typedao.findByDbtype("mysql");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			typedao.close();
		}
		if (arr != null && arr.length > 1) {
			String id1 = arr[0];
			String id2 = arr[1];
			if("null".equalsIgnoreCase(id2)){
				id = id1;
			}else{
				//id = id1;
				OraclePartsDao oracleDao = new OraclePartsDao();
				OraspaceconfigDao configDao =null;
				List list = new ArrayList();
				DBDao bdao = new DBDao();
				try {
					configDao = new OraspaceconfigDao();
					OracleEntity oracle = (OracleEntity)oracleDao.findByID(id2);
					oracleDao.delete(new String[] { id2 });
					IpTranslation transfer = new IpTranslation();
					DBVo vo = (DBVo) bdao.findByID(oracle.getDbid()+"");
					id = vo.getId()+"";
					if (oracleDao != null)
						oracleDao.close();
					oracleDao = new OraclePartsDao();
					String hexIp = transfer.formIpToHex(vo.getIpAddress());
					configDao.deleteByIP(hexIp + ":" + id2);
					list = oracleDao.findOracleParts(Integer.parseInt(id1));
					
					//ɾ�������ݿ�Ĳɼ�ָ��
					NodeGatherIndicatorsDao dao = new NodeGatherIndicatorsDao();
					try {
						dao.deleteByNodeIdAndTypeAndSubtype(oracle.getId()+"", "db", "oracle");
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						dao.close();
					}
					//ɾ�������ݿ�ĸ澯��ֵ
					AlarmIndicatorsNodeDao indidao = new AlarmIndicatorsNodeDao();
					try {
						indidao.deleteByNodeId(oracle.getId()+"", "db", "oracle");
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						indidao.close();
					}
					
					//����serverip�ͱ���ɾ���洢�ɼ����ݱ������
					DBDao dbDao = new DBDao();
					IpTranslation tranfer = new IpTranslation();
					String hex = tranfer.formIpToHex(vo.getIpAddress());
					String serverip = hex+":"+id2;
					String[] tableNames = {"nms_oracontrfile","nms_oracursors","nms_oradbio",
							"nms_oraextent","nms_oraisarchive","nms_orakeepobj","nms_oralock",
							"nms_oralogfile","nms_oramemperfvalue","nms_oramemvalue","nms_orarollback",
							"nms_orasessiondata","nms_oraspaces","nms_orastatus","nms_orasys","nms_oratables",
							"nms_oratopsql","nms_orauserinfo","nms_orawait"};
					try {
						dbDao.clearTablesData(tableNames, serverip);
					} catch (RuntimeException e) {
						e.printStackTrace();
					}finally{
						dbDao.close();
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					oracleDao.close();
					configDao.close();
					bdao.close();
				}

				if (list.size() == 0) {
					flag = true;
					//id = id1;
				} else {
					//id = id1;
					flag = false;
				}
			}
			
		}
		//SysLogger.info("id==============="+id);
		DBDao dbdao=null;
		try{
			 dbdao = new DBDao();
			DBVo dbvo=(DBVo) dbdao.findByID(id+"");
			
			
			

			/*
			 * niein add  for  alarm start 2010-09-02==================== 
			 * 
			 */
			DBTypeVo dbTypeVo = null;
			try {
				typedao = new DBTypeDao();
				dbTypeVo = (DBTypeVo) typedao.findByID(dbvo.getDbtype()+"");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				typedao.close();
			}
			String nodeid = arr[0];
			if("Oracle".equals(dbTypeVo.getDbtype())){
				nodeid = arr[1];
			}
			
			
			
			if(dbvo.getDbtype()==sybasetypevo.getId()){
				SybspaceconfigDao spacedao=null;
				try{
					spacedao=new SybspaceconfigDao();
					spacedao.deleteByIP(dbvo.getIpAddress());
					//ɾ�������ݿ�Ĳɼ�ָ��
					NodeGatherIndicatorsDao dao = new NodeGatherIndicatorsDao();
					try {
						dao.deleteByNodeIdAndTypeAndSubtype(dbvo.getId()+"", "db", "sybase");
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						dao.close();
					}
					//ɾ�������ݿ�ĸ澯��ֵ
					AlarmIndicatorsNodeDao indidao = new AlarmIndicatorsNodeDao();
					try {
						indidao.deleteByNodeId(dbvo.getId()+"", "db", "sybase");
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						indidao.close();
					}
					
					//����serverip�����ݿ�idɾ���洢�ɼ����ݱ������
					DBDao dbDao = new DBDao();
					IpTranslation tranfer = new IpTranslation();
					String hex = tranfer.formIpToHex(dbvo.getIpAddress());
					String serverip = hex+":"+dbvo.getId();
					String[] tableNames = {"nms_sybasestatus","nms_sybaseperformance","nms_sybasedbinfo",
							"nms_sybasedeviceinfo","nms_sybaseuserinfo","nms_sybaseserversinfo"};
					try {
						dbDao.clearTablesData(tableNames, serverip);
					} catch (RuntimeException e) {
						e.printStackTrace();
					}finally{
						dbDao.close();
					}
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					if(spacedao!=null)
						spacedao.close();
				}
				
			}else  if(dbvo.getDbtype()==db2typevo.getId()){
				Db2spaceconfigDao db2dao=null;
				try{
					db2dao=new Db2spaceconfigDao();
					db2dao.deleteByIP(dbvo.getIpAddress());
					//ɾ�������ݿ�Ĳɼ�ָ��
					NodeGatherIndicatorsDao dao = new NodeGatherIndicatorsDao();
					try {
						dao.deleteByNodeIdAndTypeAndSubtype(dbvo.getId()+"", "db", "db2");
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						dao.close();
					}
					//ɾ�������ݿ�ĸ澯��ֵ
					AlarmIndicatorsNodeDao indidao = new AlarmIndicatorsNodeDao();
					try {
						indidao.deleteByNodeId(dbvo.getId()+"", "db", "db2");
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						indidao.close();
					}
					//����serverip�����ݿ�idɾ���洢�ɼ����ݱ������
					DBDao dbDao = new DBDao();
					IpTranslation tranfer = new IpTranslation();
					String hex = tranfer.formIpToHex(dbvo.getIpAddress());
					String serverip = hex+":"+dbvo.getId();
					String[] tableNames = {"nms_db2tablespace","nms_db2common","nms_db2conn","nms_db2variable",
							"nms_db2spaceinfo","nms_db2log","nms_db2write","nms_db2pool","nms_db2lock",
							"nms_db2read","nms_db2session"};
					try {
						dbDao.clearTablesData(tableNames, serverip);
					} catch (RuntimeException e) {
						e.printStackTrace();
					}finally{
						dbDao.close();
					}
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					if(db2dao!=null)
						db2dao.close();
				}
			}else if(dbvo.getDbtype()==infomixtypevo.getId()){
				InformixspaceconfigDao informixdao=null;
				try{
					informixdao =new InformixspaceconfigDao();
					informixdao.deleteByIP(dbvo.getIpAddress());
					//ɾ�������ݿ�Ĳɼ�ָ��
					NodeGatherIndicatorsDao dao = new NodeGatherIndicatorsDao();
					try {
						dao.deleteByNodeIdAndTypeAndSubtype(dbvo.getId()+"", "db", "informix");
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						dao.close();
					}
					//ɾ�������ݿ�ĸ澯��ֵ
					AlarmIndicatorsNodeDao indidao = new AlarmIndicatorsNodeDao();
					try {
						indidao.deleteByNodeId(dbvo.getId()+"", "db", "informix");
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						indidao.close();
					}
					
					//����serverip�ͱ���ɾ���洢�ɼ����ݱ������
					DBDao dbDao = new DBDao();
					IpTranslation tranfer = new IpTranslation();
					String hex = tranfer.formIpToHex(dbvo.getIpAddress());
					String serverip = hex+":"+dbvo.getDbName();
					String[] tableNames = {"nms_informixabout","nms_informixconfig","nms_informixdatabase",
							"nms_informixio","nms_informixlock","nms_informixlog",
							"nms_informixsession","nms_informixspace","nms_informixstatus"};
					try {
						dbDao.clearTablesData(tableNames, serverip);
					} catch (RuntimeException e) {
						e.printStackTrace();
					}finally{
						dbDao.close();
					}
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					informixdao.close();
				}
			}else if(sqlservertypevo.getId()==dbvo.getDbtype()){
				SqldbconfigDao sqldao=null;
				try{
					sqldao=new SqldbconfigDao();
					sqldao.deleteByIP(dbvo.getIpAddress());
					//ɾ�������ݿ�Ĳɼ�ָ��
					NodeGatherIndicatorsDao dao = new NodeGatherIndicatorsDao();
					try {
						dao.deleteByNodeIdAndTypeAndSubtype(dbvo.getId()+"", "db", "sqlserver");
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						dao.close();
					}
					//ɾ�������ݿ�ĸ澯��ֵ
					AlarmIndicatorsNodeDao indidao = new AlarmIndicatorsNodeDao();
					try {
						indidao.deleteByNodeId(dbvo.getId()+"", "db", "sqlserver");
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						indidao.close();
					}
					
					//����serverip�ͱ���ɾ���洢�ɼ����ݱ������
					DBDao dbDao = new DBDao();
					IpTranslation tranfer = new IpTranslation();
					String hex = tranfer.formIpToHex(dbvo.getIpAddress());
					String serverip = hex+":"+dbvo.getAlias();
					String[] tableNames = {"nms_sqlservercaches","nms_sqlserverconns",
							"nms_sqlserverdbvalue","nms_sqlserverinfo_v","nms_sqlserverlockinfo_v","nms_sqlserverlocks",
							"nms_sqlservermems","nms_sqlserverpages","nms_sqlserverscans","nms_sqlserversqls",
							"nms_sqlserverstatisticshash","nms_sqlserverstatus","nms_sqlserversysvalue"};
					try {
						dbDao.clearTablesData(tableNames, serverip);
					} catch (RuntimeException e) {
						e.printStackTrace();
					}finally{
						dbDao.close();
					}
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					sqldao.close();
				}
			}else if(mysqltypevo.getId()==dbvo.getDbtype()){
				MySqlSpaceConfigDao sqldao=null;
				try{
					sqldao=new MySqlSpaceConfigDao();
					sqldao.deleteByIP(dbvo.getIpAddress());
					//ɾ�������ݿ�Ĳɼ�ָ��
					NodeGatherIndicatorsDao dao = new NodeGatherIndicatorsDao();
					try {
						dao.deleteByNodeIdAndTypeAndSubtype(dbvo.getId()+"", "db", "mysql");
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						dao.close();
					}
					//ɾ�������ݿ�ĸ澯��ֵ
					AlarmIndicatorsNodeDao indidao = new AlarmIndicatorsNodeDao();
					try {
						indidao.deleteByNodeId(dbvo.getId()+"", "db", "mysql");
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						indidao.close();
					}
					
					//����serverip�ͱ���ɾ���洢�ɼ����ݱ������
					DBDao dbDao = new DBDao();
					IpTranslation tranfer = new IpTranslation();
					String hex = tranfer.formIpToHex(dbvo.getIpAddress());
					String serverip = hex+":"+dbvo.getId();
					try {
						dbDao.clearTableData("nms_mysqlinfo", serverip);
					} catch (RuntimeException e) {
						e.printStackTrace();
					}finally{
						dbDao.close();
					}
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					sqldao.close();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(dbdao!=null)
				dbdao.close();
		}
		
		if (flag) {
			DBDao dao = new DBDao();
			try {
				dao.delete(id);
				TimeShareConfigUtil timeShareConfigUtil = new TimeShareConfigUtil();
				timeShareConfigUtil.deleteTimeShareConfig(id, timeShareConfigUtil.getObjectType("1"));
				/* snow add at 2010-5-19 ɾ�����ݿ�ɼ�ʱ�� */
				TimeGratherConfigUtil timeGratherConfigUtil = new TimeGratherConfigUtil();
				timeGratherConfigUtil.deleteTimeGratherConfig(id, timeGratherConfigUtil.getObjectType("1"));
				/* snow end */
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dao.close();
			}

			int nodeId = Integer.parseInt(id);
			//PollingEngine.getInstance().deleteNodeByID(nodeId);
			PollingEngine.getInstance().deleteDbByID(nodeId);//yangjun
			DBPool.getInstance().removeConnect(nodeId);
		}

		//����ҵ����ͼ
		NodeDependDao nodedependao = new NodeDependDao();
		List list = nodedependao.findByNode("dbs"+id);
		if(list!=null&&list.size()>0){
			for(int j = 0; j < list.size(); j++){
				NodeDepend vo = (NodeDepend)list.get(j);
				if(vo!=null){
					LineDao lineDao = new LineDao();
	    			lineDao.deleteByidXml("dbs"+id, vo.getXmlfile());
	    			NodeDependDao nodeDependDao = new NodeDependDao();
	    			if(nodeDependDao.isNodeExist("dbs"+id, vo.getXmlfile())){
	            		nodeDependDao.deleteByIdXml("dbs"+id, vo.getXmlfile());
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
	    			ManageXml manageXml = (ManageXml) subMapDao.findByXml(vo.getXmlfile());
	    			if(manageXml!=null){
	    				NodeDependDao nodeDepenDao = new NodeDependDao();
	    				try{
	    				    List lists = nodeDepenDao.findByXml(vo.getXmlfile());
	    				    ChartXml chartxml;
	    					chartxml = new ChartXml("NetworkMonitor","/"+vo.getXmlfile().replace("jsp", "xml"));
	    					chartxml.addBussinessXML(manageXml.getTopoName(),lists);
	    					ChartXml chartxmlList;
	    					chartxmlList = new ChartXml("NetworkMonitor","/"+vo.getXmlfile().replace("jsp", "xml").replace("businessmap", "list"));
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
		return "/db.do?action=list";
	}

	private String update() {
		DBVo vo = new DBVo();
		vo.setId(getParaIntValue("id"));
		vo.setUser(getParaValue("user"));
		//String flag = getParaValue("flag");
		
		//��Ҫ�ж��û��������������ݿ���������Ƿ�һ��,��һ��,˵��û�޸�����,����һ��,��Ҫ����������ܺ��滻ԭ���ļ�������
		String password = getParaValue("password");
		DBDao _dao = new DBDao();
		DBVo _vo = new DBVo();
		try{
			_vo = (DBVo)_dao.findByID(vo.getId()+"");
		}catch(Exception e){
			
		}finally{
			_dao.close();
		}
		if(_vo != null){
			if(_vo.getPassword().equals(password)){
				vo.setPassword(password);
			}else{
				//��Ҫ���ܺ��滻ԭ������
				String newPassword = "";
				try{
					newPassword = EncryptUtil.encode(password);
				}catch(Exception e){
					
				}
				vo.setPassword(newPassword);
			}
		}
		//vo.setPassword(getParaValue("password"));
		vo.setAlias(getParaValue("alias"));
		vo.setIpAddress(getParaValue("ip_address"));
		vo.setPort(getParaValue("port"));
		vo.setDbName(getParaValue("db_name"));
		vo.setCategory(getParaIntValue("category"));
		vo.setDbuse(getParaValue("dbuse"));
		vo.setSendmobiles(getParaValue("sendmobiles"));
		vo.setSendemail(getParaValue("sendemail"));
		vo.setSendphone(getParaValue("sendphone"));
		String sid = getParaValue("sid");
		int tsid = -1;
		String allbid = getParaValue("bid");
		vo.setBid(allbid);
		vo.setManaged(getParaIntValue("managed"));
		vo.setDbtype(getParaIntValue("dbtype"));
		vo.setCollecttype(getParaIntValue("collecttype"));
		vo.setSupperid(getParaIntValue("supperid"));//snow add 2010-05-20
		boolean flag = false;
		try {
			tsid = Integer.parseInt(sid);
		} catch (Exception e) {
			flag = true;
		}
		OraclePartsDao oraDao = null;
		OraspaceconfigDao configDao = null;
		//SysLogger.info(tsid+"===="+flag);
		if (tsid != -1 && !flag) {
			DBDao dao = null;
			try {
				oraDao = new OraclePartsDao();
				configDao = new OraspaceconfigDao();
				IpTranslation transfer = new IpTranslation();
				String hexIp = transfer.formIpToHex(vo.getIpAddress());
				configDao.deleteByIP(hexIp + ":" + tsid);
				OracleEntity oracle = new OracleEntity();
				oracle.setAlias(vo.getAlias());
				oracle.setCollectType(vo.getCollecttype());
				oracle.setDbid(vo.getId());
				oracle.setGzerid(vo.getSendemail());
				oracle.setId(Integer.parseInt(sid));
				oracle.setManaged(vo.getManaged());
				oracle.setPassword(vo.getPassword());
				oracle.setSid(vo.getDbName());
				oracle.setUser(vo.getUser());
				oracle.setBid(vo.getBid());

				oraDao.update(oracle);
				
				dao = new DBDao();
				dao.update(vo);
					
				if (PollingEngine.getInstance().getNodeByID(oracle.getId()) != null) {
					DBNode dbNode = (DBNode) PollingEngine.getInstance().getNodeByID(vo.getId());
					dbNode.setUser(vo.getUser());
					dbNode.setPassword(vo.getPassword());
					dbNode.setPort(vo.getPort());
					dbNode.setIpAddress(vo.getIpAddress());
					dbNode.setAlias(vo.getAlias());
					dbNode.setDbName(vo.getDbName());
					dbNode.setCollecttype(vo.getCollecttype());
					dbNode.setBid(vo.getBid());
				}
				TimeShareConfigUtil timeShareConfigUtiligUtil = new TimeShareConfigUtil(); // nielin add for time-sharing at 2010-01-03
				boolean result = timeShareConfigUtiligUtil.saveTimeShareConfigList(request, vo.getId() + ":oracle"
						+ oracle.getId(), timeShareConfigUtiligUtil.getObjectType("1"));
				
	            /* snow add at 2010-5-19 */
	            TimeGratherConfigUtil timeGratherConfigUtil = new TimeGratherConfigUtil();
	            boolean result2 = timeGratherConfigUtil.saveTimeGratherConfigList(request, String.valueOf(vo.getId()), timeGratherConfigUtil.getObjectType("1"));
	      		/* snow add end*/
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (oraDao != null){
					oraDao.close();
				}
				if (configDao != null){
					configDao.close();
				}
				if(dao != null){
					dao.close();
				}
			}
			/*------------modify end-------------*/
			return list();
		}

		/*
		DBPool.getInstance().removeConnect(vo.getId());
		 */
		DBDao dao = null;
		try {
			dao = new DBDao();
			dao.update(vo);
			TimeShareConfigUtil timeShareConfigUtiligUtil = new TimeShareConfigUtil(); // nielin add for time-sharing at 2010-01-03
			boolean result = timeShareConfigUtiligUtil.saveTimeShareConfigList(request, String.valueOf(vo.getId()),
					timeShareConfigUtiligUtil.getObjectType("1"));
			/* snow add at 2010-5-19 */
            TimeGratherConfigUtil timeGratherConfigUtil = new TimeGratherConfigUtil();
            //SysLogger.info("$$$$$$$$$$$$$$$$$$$$$");
            boolean result2 = timeGratherConfigUtil.saveTimeGratherConfigList(request, String.valueOf(vo.getId()), timeGratherConfigUtil.getObjectType("1"));
      		/* snow add end*/
			
			if (PollingEngine.getInstance().getNodeByID(vo.getId()) != null) {
				DBNode dbNode = (DBNode) PollingEngine.getInstance().getNodeByID(vo.getId());
				dbNode.setUser(vo.getUser());
				dbNode.setPassword(vo.getPassword());
				dbNode.setPort(vo.getPort());
				dbNode.setIpAddress(vo.getIpAddress());
				dbNode.setAlias(vo.getAlias());
				dbNode.setDbName(vo.getDbName());
				dbNode.setCollecttype(vo.getCollecttype());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dao != null)
				dao.close();
		}

		return list();
	}

	private String cancelmanage() {
		String sid = getParaValue("sid");
		int tsid = -1;
		boolean flag = false;
		try {
			tsid = Integer.parseInt(sid);
		} catch (Exception e) {
			flag = true;
		}
		if (tsid != -1 && !flag) {
			OracleEntity vo = new OracleEntity();
			//DBDao dao = new DBDao();
			OraclePartsDao dao = new OraclePartsDao();
			//int sid=getParaIntValue("sid");
			try {
				vo = (OracleEntity) dao.getOracleById(tsid);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dao.close();
			}
			vo.setManaged(0);

			dao = new OraclePartsDao();
			try {
				dao.update(vo);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dao.close();
			}
			
			/* if(PollingEngine.getInstance().getNodeByID(vo.getId())!=null)
			    {   
			    	PollingEngine.getInstance().deleteDbByID(vo.getId());       	
			    }*/
		/*	if (PollingEngine.getInstance().getDbByID(vo.getId()) != null) {
				PollingEngine.getInstance().deleteDbByID(vo.getId());
			}*/
			
			
			
		} else {
			DBVo vo = new DBVo();
			DBDao dao = new DBDao();
			try {
				vo = (DBVo) dao.findByID(getParaValue("id"));
				vo.setManaged(0);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dao.close();
			}
			dao = new DBDao();
			try {
				dao.update(vo);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dao.close();
			}
		/*	if (PollingEngine.getInstance().getDbByID(vo.getId()) != null) {
				PollingEngine.getInstance().deleteDbByID(vo.getId());
			}*/
		}

		return "/db.do?action=list";
	}

	private String check() {
		DBVo vo = new DBVo();
		String strid=getParaValue("id");
		String sid=getParaValue("sid");
		//SysLogger.info(strid+"=========="+sid);
		int id=0;
		try{
			id=Integer.parseInt(strid);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(sid != null && sid.length()>0 && !"null".equals(sid)){
			OraclePartsDao partdao=new OraclePartsDao();
			DBDao dbdao=null;
			try{
				//OracleEntity oracle=(OracleEntity)partdao.findByID(String.valueOf(0-id));
				OracleEntity oracle=(OracleEntity)partdao.findByID(sid);
				dbdao=new DBDao();
				vo=(DBVo)dbdao.findByID(oracle.getDbid()+"");
				//sid=String.valueOf(0-id);
				strid=vo.getId()+"";
				//oracle.getd
				
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(partdao!=null)
				  partdao.close();
				if(dbdao!=null)
					dbdao.close();
			}
		}else{
		    DBDao dao = new DBDao();
			try {
				vo = (DBVo) dao.findByID(strid);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dao.close();
			}
		}
		DBTypeDao typedao = new DBTypeDao();
		DBTypeVo typevo = null;
		try {
			typevo = (DBTypeVo) typedao.findByID(vo.getDbtype() + "");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			typedao.close();
		}
		//HONGLI ADD 
		session.setAttribute("id", strid);
		session.setAttribute("sid", sid);
		if (typevo.getDbtype().equalsIgnoreCase("oracle")) {
			return "/oracle.do?action=oracleping&id=" +strid + "&sid=" + sid;
		} else if (typevo.getDbtype().equalsIgnoreCase("sqlserver")) {
			return "/sqlserver.do?action=sqlserverping&id=" + getParaValue("id");
		} else if (typevo.getDbtype().equalsIgnoreCase("mysql")) {
			return "/mysql.do?action=mysqlping&id=" + getParaValue("id");
		} else if (typevo.getDbtype().equalsIgnoreCase("db2")) {
			return "/db2.do?action=db2ping&id=" + getParaValue("id");
		} else if (typevo.getDbtype().equalsIgnoreCase("sybase")) {
			return "/sybase.do?action=sybaseping&id=" + getParaValue("id");
		} else if (typevo.getDbtype().equalsIgnoreCase("informix")) {
			return "/informix.do?action=informixping&id=" + getParaValue("id");
		} else {
			return "/db.do?action=list";
		}
	}

	private String addmanage() {
		String sid = getParaValue("sid");
		int tsid = -1;
		boolean flag = false;
		try {
			tsid = Integer.parseInt(sid);
		} catch (Exception e) {
			flag = true;
		}
		DBVo tvo = new DBVo();
		if (tsid != -1 && !flag) {
			OracleEntity vo = new OracleEntity();
			//DBDao dao = new DBDao();
			OraclePartsDao dao = new OraclePartsDao();
			//int sid=getParaIntValue("sid");
			try {
				vo = (OracleEntity) dao.getOracleById(tsid);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dao.close();
			}
			vo.setManaged(1);

			dao = new OraclePartsDao();
			try {
				dao.update(vo);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dao.close();
			}
			DBDao bdao = new DBDao();
			//bdao.getDbByTypeAndIpaddress(1, ipaddress)
			DBVo bvo = null;
			try {
				bvo = (DBVo) bdao.findByID(vo.getDbid() + "");
				bvo.setAlias(vo.getAlias());
				bvo.setBid(vo.getBid());
				bvo.setCollecttype(vo.getCollectType());
				bvo.setId(vo.getId());
				bvo.setIpAddress(bvo.getIpAddress() + ":" + vo.getSid());
				bvo.setManaged(vo.getManaged());
				bvo.setPassword(vo.getPassword());
				bvo.setUser(vo.getUser());
				bvo.setSendemail(vo.getGzerid());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				bdao.close();
			}
			tvo = bvo;
		} else {

			DBVo vo = new DBVo();
			DBDao dao = new DBDao();
			try {
				vo = (DBVo) dao.findByID(getParaValue("id"));
				vo.setManaged(1);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dao.close();
			}
			dao = new DBDao();
			try {
				dao.update(vo);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dao.close();
			}
			tvo = vo;
		}
		if (PollingEngine.getInstance().getDbByID(tvo.getId()) != null) {
			DBNode dbNode = (DBNode) PollingEngine.getInstance().getDbByID(tvo.getId());
			dbNode.setUser(tvo.getUser());
			dbNode.setPassword(tvo.getPassword());
			dbNode.setPort(tvo.getPort());
			dbNode.setIpAddress(tvo.getIpAddress());
			dbNode.setAlias(tvo.getAlias());
			dbNode.setDbName(tvo.getDbName());
		} else {
			DBNode dbNode = new DBNode();
			dbNode.setUser(tvo.getUser());
			dbNode.setPassword(tvo.getPassword());
			dbNode.setPort(tvo.getPort());
			dbNode.setIpAddress(tvo.getIpAddress());
			dbNode.setAlias(tvo.getAlias());
			dbNode.setDbName(tvo.getDbName());
			dbNode.setId(tvo.getId());
			PollingEngine.getInstance().addDb(dbNode);
			//PollingEngine.getInstance().addDb(dbNode);
		}
		//DBPool.getInstance().removeConnect(vo.getId());     
		return "/db.do?action=list";
	}

	/**
	 * @nielin modify
	 * send edit.jsp
	 */
	private String ready_edit() {
		DBDao dao = new DBDao();
		String jsp = "";
		try {
			

			// find  time-sharing list of the db
			boolean flag = false;
			int sid = -1;
			try {
				sid = Integer.parseInt(getParaValue("sid"));
			} catch (Exception e) {
				//e.printStackTrace();
				flag = true;
			}
			List timeShareConfigList = null;
			if (!flag && (sid != -1)) {
				jsp = readyEdit(dao);
				OraclePartsDao oracleDao = null;
				try {
					oracleDao = new OraclePartsDao();
					OracleEntity oracle = (OracleEntity) oracleDao.getOracleById(sid);
					DBVo vo = (DBVo)dao.findByID(oracle.getDbid()+"");
					//DBVo vo = (DBVo) request.getAttribute("vo");
					vo.setAlias(oracle.getAlias());
					vo.setCollecttype(oracle.getCollectType());
					vo.setDbName(oracle.getSid());
					vo.setManaged(oracle.getManaged());
					vo.setPassword(oracle.getPassword());
					vo.setUser(oracle.getUser());
					vo.setSendemail(oracle.getGzerid());
					vo.setBid(oracle.getBid());
					request.setAttribute("vo", vo);
					request.setAttribute("sid", sid+"");
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					oracleDao.close();
				}
				TimeShareConfigUtil timeShareConfigUtil = new TimeShareConfigUtil();
				timeShareConfigList = timeShareConfigUtil.getTimeShareConfigList(getParaValue("id") + ":oracle" + sid,
						timeShareConfigUtil.getObjectType("1"));
				
			    /* snow add at 2010-05-18 */
			    //�ṩ��Ӧ����Ϣ
		    	SupperDao supperdao = new SupperDao();
		    	List<Supper> allSupper = supperdao.loadAll();
		    	request.setAttribute("allSupper", allSupper);
		    	//�ṩ�����õĲɼ�ʱ����Ϣ
		    	TimeGratherConfigUtil tg  = new TimeGratherConfigUtil();
		    	List<TimeGratherConfig> timeGratherConfigList = tg.getTimeGratherConfig(getParaValue("id"), tg.getObjectType("1"));
		    	for (TimeGratherConfig timeGratherConfig : timeGratherConfigList) {
		    		timeGratherConfig.setHourAndMin();
				}
		    	request.setAttribute("timeGratherConfigList", timeGratherConfigList);  
		    	/* snow end */
		    	
			} else {
				request.setAttribute("sid", "");
				jsp = readyEdit(dao);
				TimeShareConfigUtil timeShareConfigUtil = new TimeShareConfigUtil(); // nielin add for time-sharing 2010-01-04
				timeShareConfigList = timeShareConfigUtil.getTimeShareConfigList(getParaValue("id"), timeShareConfigUtil
						.getObjectType("1"));
				
			    /* snow add at 2010-05-18 */
				//�ṩ��Ӧ����Ϣ
		    	SupperDao supperdao = new SupperDao();
		    	List<Supper> allSupper = supperdao.loadAll();
		    	request.setAttribute("allSupper", allSupper);
		    	//�ṩ�����õĲɼ�ʱ����Ϣ
		    	TimeGratherConfigUtil tg  = new TimeGratherConfigUtil();
		    	List<TimeGratherConfig> timeGratherConfigList = tg.getTimeGratherConfig(getParaValue("id"), tg.getObjectType("1"));
		    	for (TimeGratherConfig timeGratherConfig : timeGratherConfigList) {
		    		timeGratherConfig.setHourAndMin();
				}
		    	request.setAttribute("timeGratherConfigList", timeGratherConfigList);  
		    	/* snow end */
				
			}

			request.setAttribute("timeShareConfigList", timeShareConfigList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		//setTarget("/application/db/edit.jsp");
		//SysLogger.info("sid======7====999==="+jsp);
		return "/application/db/edit.jsp";
	}
	
	private String ready_add(){
    	SupperDao supperdao = new SupperDao();
    	List<Supper> allSupper = supperdao.loadAll();
    	String flag = getParaValue("flag");
    	request.setAttribute("allSupper", allSupper);
    	request.setAttribute("flag", flag);
    	return "/application/db/add.jsp";
	}

	public String execute(String action) {
		if (action.equals("list"))
			return list();
		if (action.equals("ready_add"))
			return ready_add();
		if (action.equals("add"))
			return add();
		if (action.equals("delete"))
			return delete();
		if (action.equals("ready_edit")) {
			return ready_edit();
		}
		if (action.equals("update"))
			return update();
		if (action.equals("cancelmanage"))
			return cancelmanage();
		if (action.equals("addmanage"))
			return addmanage();
		if (action.equals("check"))
			return check();
		setErrorCode(ErrorMessage.ACTION_NO_FOUND);
		return null;
	}
	
	/**
	 * nielin add 2010-08-05
	 */
	public void execute(DBVo vo){
		//System.out.println("=====================��ʼ�ɼ�");
		DBDao dbdao = null;
		try {

			List mslist = null;
			
			List oclist = null;
			
			List sysbaselist = null;
			
			List informixlist = null;
			
			List db2list = null;

			List mysqllist = null;
			
			if(vo != null){
				dbdao = new DBDao();
				try {
					//vo = (DBVo)dbdao.findByID(String.valueOf(vo.getId()));
					String password = EncryptUtil.decode(vo.getPassword());
					vo.setPassword(password);
				} catch (RuntimeException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}finally{
					dbdao.close();
				}
				if(vo != null ){
					DBTypeDao typeDao = new DBTypeDao();
					DBTypeVo type = null;
					try {
						type = (DBTypeVo)typeDao.findByID(String.valueOf(vo.getDbtype()));
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						typeDao.close();
					}
					//SysLogger.info("type================="+type.getDbtype());
					if("MySql".equals(type.getDbtype())){
						mysqllist = new ArrayList();
						mysqllist.add(vo);
					}else if("SQLServer".equals(type.getDbtype())){
						mslist = new ArrayList();
						mslist.add(vo);
					}
					else if("Oracle".equals(type.getDbtype())){
						oclist = new ArrayList();
						oclist.add(vo);
					}
					else if("Sybase".equals(type.getDbtype())){
						sysbaselist = new ArrayList();
						sysbaselist.add(vo);
					}
					else if("Informix".equals(type.getDbtype())){
						informixlist = new ArrayList();
						informixlist.add(vo);
					}
					else if("DB2".equals(type.getDbtype())){
						db2list = new ArrayList();
						db2list.add(vo);
					}
				}
			}
			
			//sqlserver�ɼ�
			if (mslist != null) {
				for (int i = 0; i < mslist.size(); i++) {
					Hashtable sqlserverdata = new Hashtable();
					DBVo dbmonitorlist = (DBVo) mslist.get(i);
					DBNode dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
					//��ʼ�����ݿ�ڵ�״̬
					dbnode.setAlarm(false);
					dbnode.setStatus(0);
					Calendar _tempCal = Calendar.getInstance();
					Date _cc = _tempCal.getTime();
					String _time = sdf.format(_cc);
					dbnode.setLastTime(_time);
					dbnode.getAlarmMessage().clear();

					String serverip = dbmonitorlist.getIpAddress();
					String username = dbmonitorlist.getUser();
					String passwords = dbmonitorlist.getPassword();

					Calendar date = Calendar.getInstance();
					Date d = new Date();
					//�жϸ����ݿ��Ƿ���������
					boolean sqlserverIsOK = false;

					if (dbnode.getCollecttype() == SystemConstant.DBCOLLECTTYPE_SHELL) {
						//�ű��ɼ���ʽ
						String filename = ResourceCenter.getInstance().getSysPath() + "/linuxserver/" + serverip
								+ ".sqlserver.log";
						File file = new File(filename);
						if (!file.exists()) {
							//�ļ�������,������澯
							try {
								createFileNotExistSMS(serverip);
							} catch (Exception e) {
								e.printStackTrace();
							}
							continue;
						}
						SysLogger.info("###��ʼ����SQLSERVER:" + serverip + "�����ļ�###");
						LoadSQLServerFile loadsqlserver = new LoadSQLServerFile(filename);

						try {
							sqlserverdata = loadsqlserver.getSQLInital();
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (sqlserverdata != null && sqlserverdata.size() > 0) {
							//�ж����ݿ�����״̬
							if (sqlserverdata.containsKey("status")) {
								int status = Integer.parseInt((String) sqlserverdata.get("status"));
								if (status == 1)
									sqlserverIsOK = true;
								if (!sqlserverIsOK) {
									//��Ҫ�������ݿ����ڵķ������Ƿ�����ͨ
									Host host = (Host) PollingEngine.getInstance().getNodeByIP(serverip);
									Vector ipPingData = (Vector) ShareData.getPingdata().get(serverip);
									if (ipPingData != null) {
										Pingcollectdata pingdata = (Pingcollectdata) ipPingData.get(0);
										Calendar tempCal = (Calendar) pingdata.getCollecttime();
										Date cc = tempCal.getTime();
										String time = sdf.format(cc);//.format(cc);		
										String lastTime = time;
										String pingvalue = pingdata.getThevalue();
										if (pingvalue == null || pingvalue.trim().length() == 0)
											pingvalue = "0";
										double pvalue = new Double(pingvalue);
										if (pvalue == 0) {
											//�������������Ӳ���***********************************************
											dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
											dbnode.setAlarm(true);
											dbnode.setLastTime(lastTime);
											dbnode.setStatus(3);
											List alarmList = dbnode.getAlarmMessage();
											if (alarmList == null)
												alarmList = new ArrayList();
											dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
											String sysLocation = "";
											try {
												SmscontentDao eventdao = new SmscontentDao();
												String eventdesc = "SQLSERVER(" + dbmonitorlist.getDbName() + " IP:"
														+ dbmonitorlist.getIpAddress() + ")" + "�����ݿ����ֹͣ";
												eventdao.createEventWithReasion("poll", dbmonitorlist.getId() + "", dbmonitorlist
														.getAlias()
														+ "(" + dbmonitorlist.getIpAddress() + ")", eventdesc, 3, "db", "ping",
														"���ڵķ��������Ӳ���");
											} catch (Exception e) {
												e.printStackTrace();
											}
										} else {
											Pingcollectdata hostdata = null;
											hostdata = new Pingcollectdata();
											hostdata.setIpaddress(serverip);
											hostdata.setCollecttime(date);
											hostdata.setCategory("SQLPing");
											hostdata.setEntity("Utilization");
											hostdata.setSubentity("ConnectUtilization");
											hostdata.setRestype("dynamic");
											hostdata.setUnit("%");
											hostdata.setThevalue("0");
											try {
												dbdao.createHostData(hostdata);
												//���Ͷ���	
												dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
												dbnode.setAlarm(true);
												List alarmList = dbnode.getAlarmMessage();
												if (alarmList == null)
													alarmList = new ArrayList();
												dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
												dbnode.setStatus(3);
												createSMS("sqlserver", dbmonitorlist);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}

									} else {
										Pingcollectdata hostdata = null;
										hostdata = new Pingcollectdata();
										hostdata.setIpaddress(serverip);
										hostdata.setCollecttime(date);
										hostdata.setCategory("SQLPing");
										hostdata.setEntity("Utilization");
										hostdata.setSubentity("ConnectUtilization");
										hostdata.setRestype("dynamic");
										hostdata.setUnit("%");
										hostdata.setThevalue("0");
										try {
											dbdao.createHostData(hostdata);
											//���Ͷ���	
											dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
											dbnode.setAlarm(true);
											List alarmList = dbnode.getAlarmMessage();
											if (alarmList == null)
												alarmList = new ArrayList();
											dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
											dbnode.setStatus(3);
											createSMS("sqlserver", dbmonitorlist);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								} else {
									//��ͨ�������,����ͨ�����ݲ������
									Pingcollectdata hostdata = null;
									hostdata = new Pingcollectdata();
									hostdata.setIpaddress(serverip);
									hostdata.setCollecttime(date);
									hostdata.setCategory("SQLPing");
									hostdata.setEntity("Utilization");
									hostdata.setSubentity("ConnectUtilization");
									hostdata.setRestype("dynamic");
									hostdata.setUnit("%");
									hostdata.setThevalue("100");
									try {
										dbdao.createHostData(hostdata);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								if (sqlserverIsOK) {
									//�����ݿ��������ϣ���������ݿ����ݵĲɼ�
									Vector info_v = new Vector();
									Hashtable sysValue = new Hashtable();
									Vector altfiles_v = new Vector();
									Vector process_v = new Vector();
									Vector sysuser_v = new Vector();
									Vector lockinfo_v = new Vector();
									Hashtable sqlservervalue = new Hashtable();
									try {
										if (sqlserverdata.containsKey("info_v")) {
											info_v = (Vector) sqlserverdata.get("info_v");
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
									try {
										if (sqlserverdata.containsKey("sysValue")) {
											sysValue = (Hashtable) sqlserverdata.get("sysValue");
										}
									} catch (Exception e) {
										e.printStackTrace();
									}

									if (info_v == null)
										info_v = new Vector();

									for (int j = 0; j < info_v.size(); j++) {
										Sqlserver_processdata sp = new Sqlserver_processdata();
										Hashtable ht = (Hashtable) info_v.get(j);
										String spid = ht.get("spid").toString();
										String dbname = ht.get("dbname").toString();
										String usernames = ht.get("username").toString();
										String cpu = ht.get("cpu").toString();
										String memusage = ht.get("memusage").toString();
										String physical_io = ht.get("physical_io").toString();

										String p_status = ht.get("status").toString();
										String hostname = ht.get("hostname").toString();
										String program_name = ht.get("program_name").toString();
										String login_time = ht.get("login_time").toString();
										sp.setCpu(Integer.parseInt(cpu));
										sp.setDbname(dbname);
										sp.setHostname(hostname);
										sp.setMemusage(Integer.parseInt(memusage));
										sp.setMon_time(d);
										sp.setPhysical_io(Long.parseLong(physical_io));
										sp.setProgram_name(program_name);
										sp.setSpid(spid);
										sp.setStatus(p_status);
										sp.setUsername(usernames);
										sp.setLogin_time(sdf.parse(login_time));
										sp.setServerip(serverip);
										try {
											dbdao.addSqlserver_processdata(sp);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}

									//								Hashtable retValue = new Hashtable();				
									//								retValue = dbdao.collectSQLServerMonitItemsDetail(serverip, "", username, passwords);
									//								sqlserverdata.put("retValue", retValue);

									Hashtable dbValue = new Hashtable();
									//�õ����ݿ�����Ϣ
									try {
										if (sqlserverdata.containsKey("dbValue")) {
											dbValue = (Hashtable) sqlserverdata.get("dbValue");
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
									//dbValue = dbdao.getSqlserverDB(serverip, username, passwords);
									//ShareData.setSqldbdata(serverip, dbValue);

									//�����ݿ�ռ���и澯���
									if (dbValue != null && dbValue.size() > 0) {
										SqldbconfigDao sqldbconfigManager = new SqldbconfigDao();
										Hashtable alarmdbs = sqldbconfigManager.getByAlarmflag(1);
										sqldbconfigManager.close();
										Hashtable database = (Hashtable) dbValue.get("database");
										//SysLogger.info("database size===="+database.size());
										Hashtable logfile = (Hashtable) dbValue.get("logfile");
										Vector names = (Vector) dbValue.get("names");
										if (alarmdbs == null)
											alarmdbs = new Hashtable();
										if (database == null)
											database = new Hashtable();
										if (logfile == null)
											logfile = new Hashtable();
										if (names != null && names.size() > 0) {
											for (int k = 0; k < names.size(); k++) {
												String dbname = (String) names.get(k);
												if (database.get(dbname) != null) {
													Hashtable db = (Hashtable) database.get(dbname);
													String usedperc = (String) db.get("usedperc");
													if (alarmdbs.containsKey(serverip + ":" + dbname + ":0")) {
														Sqldbconfig sqldbconfig = (Sqldbconfig) alarmdbs.get(serverip + ":"
																+ dbname + ":0");
														if (sqldbconfig == null)
															continue;
														if (usedperc == null)
															continue;
														if (sqldbconfig.getAlarmvalue() < new Integer(usedperc)) {
															//�澯
															SysLogger.info("### ��ʼ�澯 ###");
															dbnode = (DBNode) PollingEngine.getInstance().getDbByID(
																	dbmonitorlist.getId());
															dbnode.setAlarm(true);
															dbnode.setStatus(3);
															List alarmList = dbnode.getAlarmMessage();
															if (alarmList == null)
																alarmList = new ArrayList();
															dbnode.getAlarmMessage().add(
																	sqldbconfig.getDbname() + "��ռ䳬����ֵ"
																			+ sqldbconfig.getAlarmvalue());
															createSqldbSMS(dbmonitorlist, sqldbconfig);
														}
													}
												}
												if (logfile.get(dbname) != null) {
													Hashtable db = (Hashtable) logfile.get(dbname);
													String usedperc = (String) db.get("usedperc");
													if (alarmdbs.containsKey(serverip + ":" + dbname + ":0")) {
														Sqldbconfig sqldbconfig = (Sqldbconfig) alarmdbs.get(serverip + ":"
																+ dbname + ":1");
														if (sqldbconfig == null)
															continue;
														if (sqldbconfig.getAlarmvalue() < new Integer(usedperc)) {
															//�澯
															SysLogger.info("$$$ ��ʼ�澯 $$$");
															dbnode = (DBNode) PollingEngine.getInstance().getDbByID(
																	dbmonitorlist.getId());
															dbnode.setAlarm(true);
															dbnode.setStatus(3);
															List alarmList = dbnode.getAlarmMessage();
															if (alarmList == null)
																alarmList = new ArrayList();
															dbnode.getAlarmMessage().add(
																	sqldbconfig.getDbname() + "��ռ䳬����ֵ"
																			+ sqldbconfig.getAlarmvalue());
															createSqldbSMS(dbmonitorlist, sqldbconfig);
														}
													}
												}

											}
										}
									}
									ShareData.setSqlserverdata(serverip, sqlserverdata);
								}
							}
						}
					} else {
						//JDBC�ɼ���ʽ
						try {
							//SysLogger.info("password:==="+passwords);
							sqlserverIsOK = dbdao.getSqlserverIsOk(serverip, username, passwords);
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							//dbdao.close();
						}
						if (!sqlserverIsOK) {
							sqlserverdata.put("status", "0");
							//��Ҫ�������ݿ����ڵķ������Ƿ�����ͨ
							Host host = (Host) PollingEngine.getInstance().getNodeByIP(serverip);
							Vector ipPingData = (Vector) ShareData.getPingdata().get(serverip);
							if (ipPingData != null) {
								Pingcollectdata pingdata = (Pingcollectdata) ipPingData.get(0);
								Calendar tempCal = (Calendar) pingdata.getCollecttime();
								Date cc = tempCal.getTime();
								String time = sdf.format(cc);//.format(cc);		
								String lastTime = time;
								String pingvalue = pingdata.getThevalue();
								if (pingvalue == null || pingvalue.trim().length() == 0)
									pingvalue = "0";
								double pvalue = new Double(pingvalue);
								if (pvalue == 0) {
									//�������������Ӳ���***********************************************
									dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
									dbnode.setAlarm(true);
									dbnode.setLastTime(lastTime);
									dbnode.setStatus(3);
									List alarmList = dbnode.getAlarmMessage();
									if (alarmList == null)
										alarmList = new ArrayList();
									dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
									String sysLocation = "";
									try {
										SmscontentDao eventdao = new SmscontentDao();
										String eventdesc = "SQLSERVER(" + dbmonitorlist.getDbName() + " IP:"
												+ dbmonitorlist.getIpAddress() + ")" + "�����ݿ����ֹͣ";
										eventdao.createEventWithReasion("poll", dbmonitorlist.getId() + "", dbmonitorlist
												.getAlias()
												+ "(" + dbmonitorlist.getIpAddress() + ")", eventdesc, 3, "db", "ping",
												"���ڵķ��������Ӳ���");
									} catch (Exception e) {
										e.printStackTrace();
									}
								} else {
									Pingcollectdata hostdata = null;
									hostdata = new Pingcollectdata();
									hostdata.setIpaddress(serverip);
									hostdata.setCollecttime(date);
									hostdata.setCategory("SQLPing");
									hostdata.setEntity("Utilization");
									hostdata.setSubentity("ConnectUtilization");
									hostdata.setRestype("dynamic");
									hostdata.setUnit("%");
									hostdata.setThevalue("0");
									try {
										dbdao.createHostData(hostdata);
										//���Ͷ���	
										dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
										dbnode.setAlarm(true);
										List alarmList = dbnode.getAlarmMessage();
										if (alarmList == null)
											alarmList = new ArrayList();
										dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
										dbnode.setStatus(3);
										createSMS("sqlserver", dbmonitorlist);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}

							} else {
								Pingcollectdata hostdata = null;
								hostdata = new Pingcollectdata();
								hostdata.setIpaddress(serverip);
								hostdata.setCollecttime(date);
								hostdata.setCategory("SQLPing");
								hostdata.setEntity("Utilization");
								hostdata.setSubentity("ConnectUtilization");
								hostdata.setRestype("dynamic");
								hostdata.setUnit("%");
								hostdata.setThevalue("0");
								try {
									dbdao.createHostData(hostdata);
									//���Ͷ���	
									dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
									dbnode.setAlarm(true);
									List alarmList = dbnode.getAlarmMessage();
									if (alarmList == null)
										alarmList = new ArrayList();
									dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
									dbnode.setStatus(3);
									createSMS("sqlserver", dbmonitorlist);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						} else {
							//��ͨ�������,����ͨ�����ݲ������
							Pingcollectdata hostdata = null;
							hostdata = new Pingcollectdata();
							hostdata.setIpaddress(serverip);
							hostdata.setCollecttime(date);
							hostdata.setCategory("SQLPing");
							hostdata.setEntity("Utilization");
							hostdata.setSubentity("ConnectUtilization");
							hostdata.setRestype("dynamic");
							hostdata.setUnit("%");
							hostdata.setThevalue("100");
							try {
								dbdao.createHostData(hostdata);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						if (sqlserverIsOK) {//�����ݿ��������ϣ���������ݿ����ݵĲɼ�

							Vector info_v = new Vector();
							Hashtable sysValue = new Hashtable();
							//Vector altfiles_v = new Vector();
							Vector process_v = new Vector();
							Vector sysuser_v = new Vector();
							Vector lockinfo_v = new Vector();
							//�õ�ϵͳ��Ϣ
							try {
								sysValue = dbdao.getSqlServerSys(serverip, username, passwords);
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (sysValue == null)
								sysValue = new Hashtable();

							//��ȡ����Ϣ
							try {
								lockinfo_v = dbdao.getSqlserverLockinfo(serverip, username, passwords);
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (lockinfo_v == null)
								lockinfo_v = new Vector();

							//��ȡ������Ϣ
							try {
								info_v = dbdao.getSqlserverProcesses(serverip, username, passwords);
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (info_v == null)
								info_v = new Vector();

							//����ȡ��������ӵķ���ֵ��
							sqlserverdata.put("sysValue", sysValue);
							sqlserverdata.put("lockinfo_v", lockinfo_v);
							sqlserverdata.put("info_v", info_v);
							sqlserverdata.put("status", "1");

							for (int j = 0; j < info_v.size(); j++) {
								Sqlserver_processdata sp = new Sqlserver_processdata();
								Hashtable ht = (Hashtable) info_v.get(j);
								String spid = ht.get("spid").toString();
								String dbname = ht.get("dbname").toString();
								String usernames = ht.get("username").toString();
								String cpu = ht.get("cpu").toString();
								String memusage = ht.get("memusage").toString();
								String physical_io = ht.get("physical_io").toString();

								String status = ht.get("status").toString();
								String hostname = ht.get("hostname").toString();
								String program_name = ht.get("program_name").toString();
								String login_time = ht.get("login_time").toString();

								sp.setCpu(Integer.parseInt(cpu));
								sp.setDbname(dbname);
								sp.setHostname(hostname);
								sp.setMemusage(Integer.parseInt(memusage));
								sp.setMon_time(d);
								sp.setPhysical_io(Long.parseLong(physical_io));
								sp.setProgram_name(program_name);
								sp.setSpid(spid);
								sp.setStatus(status);
								sp.setUsername(usernames);
								sp.setLogin_time(sdf.parse(login_time));
								sp.setServerip(serverip);
								try {
									dbdao.addSqlserver_processdata(sp);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							Hashtable retValue = new Hashtable();
							retValue = dbdao.collectSQLServerMonitItemsDetail(serverip, "", username, passwords);
							sqlserverdata.put("retValue", retValue);

							Hashtable dbValue = new Hashtable();
							//�õ����ݿ�����Ϣ
							dbValue = dbdao.getSqlserverDB(serverip, username, passwords);
							sqlserverdata.put("dbValue", dbValue);

							ShareData.setSqldbdata(serverip, dbValue);
							//�����ݿ�ռ���и澯���
							if (dbValue != null && dbValue.size() > 0) {
								SqldbconfigDao sqldbconfigManager = new SqldbconfigDao();
								Hashtable alarmdbs = sqldbconfigManager.getByAlarmflag(1);
								sqldbconfigManager.close();
								Hashtable database = (Hashtable) dbValue.get("database");
								Hashtable logfile = (Hashtable) dbValue.get("logfile");
								Vector names = (Vector) dbValue.get("names");
								if (alarmdbs == null)
									alarmdbs = new Hashtable();
								if (database == null)
									database = new Hashtable();
								if (logfile == null)
									logfile = new Hashtable();
								if (names != null && names.size() > 0) {
									for (int k = 0; k < names.size(); k++) {
										String dbname = (String) names.get(k);
										if (database.get(dbname) != null) {
											Hashtable db = (Hashtable) database.get(dbname);
											String usedperc = (String) db.get("usedperc");
											if (alarmdbs.containsKey(serverip + ":" + dbname + ":0")) {
												Sqldbconfig sqldbconfig = (Sqldbconfig) alarmdbs.get(serverip + ":" + dbname
														+ ":0");
												if (sqldbconfig == null)
													continue;
												if (usedperc == null)
													continue;
												if (sqldbconfig.getAlarmvalue() < new Integer(usedperc)) {
													//�澯
													SysLogger.info("### ��ʼ�澯 ###");
													dbnode = (DBNode) PollingEngine.getInstance()
															.getDbByID(dbmonitorlist.getId());
													dbnode.setAlarm(true);
													dbnode.setStatus(3);
													List alarmList = dbnode.getAlarmMessage();
													if (alarmList == null)
														alarmList = new ArrayList();
													dbnode.getAlarmMessage().add(
															sqldbconfig.getDbname() + "��ռ䳬����ֵ" + sqldbconfig.getAlarmvalue());
													createSqldbSMS(dbmonitorlist, sqldbconfig);
												}
											}
										}
										if (logfile.get(dbname) != null) {
											Hashtable db = (Hashtable) logfile.get(dbname);
											String usedperc = (String) db.get("usedperc");
											if (alarmdbs.containsKey(serverip + ":" + dbname + ":0")) {
												Sqldbconfig sqldbconfig = (Sqldbconfig) alarmdbs.get(serverip + ":" + dbname
														+ ":1");
												if (sqldbconfig == null)
													continue;
												if (sqldbconfig.getAlarmvalue() < new Integer(usedperc)) {
													//�澯
													SysLogger.info("$$$ ��ʼ�澯 $$$");
													dbnode = (DBNode) PollingEngine.getInstance()
															.getDbByID(dbmonitorlist.getId());
													dbnode.setAlarm(true);
													dbnode.setStatus(3);
													List alarmList = dbnode.getAlarmMessage();
													if (alarmList == null)
														alarmList = new ArrayList();
													dbnode.getAlarmMessage().add(
															sqldbconfig.getDbname() + "��ռ䳬����ֵ" + sqldbconfig.getAlarmvalue());
													createSqldbSMS(dbmonitorlist, sqldbconfig);
												}
											}
										}

									}
								}
							}

							ShareData.setSqlserverdata(serverip, sqlserverdata);
						}
					}
				}
			}

			//ȡ��oracle�ɼ�
			if (oclist != null) {
				for (int i = 0; i < oclist.size(); i++) {
					Object obj = oclist.get(i);
					DBVo dbmonitorlist = new DBVo();
					BeanUtils.copyProperties(dbmonitorlist, obj);
					DBNode dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
					if (dbnode != null) {
						
						dbnode.setStatus(0);
						dbnode.setAlarm(false);
						dbnode.getAlarmMessage().clear();
						Calendar _tempCal = Calendar.getInstance();
						Date _cc = _tempCal.getTime();
						String _time = sdf.format(_cc);
						dbnode.setLastTime(_time);
						
						//�ж��豸�Ƿ��ڲɼ�ʱ����� 0:���ڲɼ�ʱ�����,���˳�;1:��ʱ�����,���вɼ�;2:�����ڲɼ�ʱ�������,��ȫ��ɼ�
	        			TimeGratherConfigUtil timeconfig = new TimeGratherConfigUtil();
	        			int result = 0;
	        			result = timeconfig.isBetween(dbnode.getId()+"", "db");
						if(result == 0){
							SysLogger.info("###### "+dbnode.getIpAddress()+" ���ڲɼ�ʱ�����,����######");
							continue;
						}
						
					}else 
						continue;

					/*
					 * modify
					 * zhao-------------------------------------------------------
					 */
					OraclePartsDao partdao = new OraclePartsDao();
					List<OracleEntity> oracleparts = new ArrayList<OracleEntity>();
					try {
						oracleparts = partdao.findOracleParts(dbmonitorlist.getId(), 1);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (partdao != null)
							partdao.close();
					}

					/*------------------------------modify end---------------------------------*/
					String serverip = dbmonitorlist.getIpAddress();
					String username = dbmonitorlist.getUser();
					String passwords = dbmonitorlist.getPassword();
					int port = Integer.parseInt(dbmonitorlist.getPort());
					String dbnames = dbmonitorlist.getDbName();
					int allFlag = 0;
					Date d1 = new Date();
					for (OracleEntity oracle : oracleparts) {
						// �жϸ����ݿ��Ƿ���������
						boolean oracleIsOK = false;
						dbnode = (DBNode) PollingEngine.getInstance().getDbByID(oracle.getId());

						if (dbnode != null) {
							dbnode.setStatus(0);
							dbnode.setAlarm(false);
							dbnode.getAlarmMessage().clear();
							Calendar _tempCal = Calendar.getInstance();
							Date _cc = _tempCal.getTime();
							String _time = sdf.format(_cc);
							dbnode.setLastTime(_time);
						}
						if (oracle.getCollectType() == SystemConstant.DBCOLLECTTYPE_SHELL) {
							// �ű��ɼ���ʽ
							String filename = ResourceCenter.getInstance().getSysPath() + "/linuxserver/" + serverip + ".oracle."
									+ oracle.getSid() + ".log";
							File file = new File(filename);
							if (!file.exists()) {
								// �ļ�������,������澯
								try {
									createFileNotExistSMS(serverip);
								} catch (Exception e) {
									e.printStackTrace();
								}
								continue;
							}
							SysLogger.info("###��ʼ����ORACLE:" + serverip + "�����ļ�###");
							LoadOracleFile loadoracle = new LoadOracleFile(filename);
							Hashtable oracledata = new Hashtable();
							try {
								oracledata = loadoracle.getOracleInit();
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (oracledata != null && oracledata.size() > 0) {
								// �ж����ݿ�����״̬
								if (oracledata.containsKey("status")) {
									int status = Integer.parseInt((String) oracledata.get("status"));
									if (status == 1)
										oracleIsOK = true;
									if (!oracleIsOK) {
										// ��Ҫ�������ݿ����ڵķ������Ƿ�����ͨ
										Host host = (Host) PollingEngine.getInstance().getNodeByIP(serverip);
										Vector ipPingData = (Vector) ShareData.getPingdata().get(serverip);
										if (ipPingData != null) {
											Pingcollectdata pingdata = (Pingcollectdata) ipPingData.get(0);
											Calendar tempCal = (Calendar) pingdata.getCollecttime();
											Date cc = tempCal.getTime();
											String time = sdf.format(cc);
											String lastTime = time;
											String pingvalue = pingdata.getThevalue();
											if (pingvalue == null || pingvalue.trim().length() == 0)
												pingvalue = "0";
											double pvalue = new Double(pingvalue);
											if (pvalue == 0) {
												// �������������Ӳ���***********************************************
												String sysLocation = "";
												// eventdao=null;
												try {
													SmscontentDao eventdao = new SmscontentDao();

													dbnode = (DBNode) PollingEngine.getInstance().getDbByID(oracle.getId());
													dbnode.setAlarm(true);
													dbnode.setStatus(3);
													List alarmList = dbnode.getAlarmMessage();
													if (alarmList == null)
														alarmList = new ArrayList();
													dbnode.getAlarmMessage().add("���ݿ����ȫ��ֹͣ");
													String eventdesc = "ORACLE(" + " IP:" + dbmonitorlist.getIpAddress() + ")"
															+ "�����ݿ����ֹͣ";
													eventdao.createEventWithReasion("poll", dbmonitorlist.getId() + ":"
															+ oracle.getId(), dbmonitorlist.getAlias() + "("
															+ dbmonitorlist.getIpAddress() + ":" + oracle.getSid() + ")",
															eventdesc, 3, "db", "ping", "���ڵķ��������Ӳ���");
												} catch (Exception e) {
													e.printStackTrace();
												} finally {
													// eventdao.close();
												}
											} else {
												try {
													Pingcollectdata hostdata = null;
													hostdata = new Pingcollectdata();
													hostdata.setIpaddress(serverip + ":" + oracle.getId());
													Calendar date = Calendar.getInstance();
													hostdata.setCollecttime(date);
													hostdata.setCategory("ORAPing");
													hostdata.setEntity("Utilization");
													hostdata.setSubentity("ConnectUtilization");
													hostdata.setRestype("dynamic");
													hostdata.setUnit("%");
													hostdata.setThevalue("0");
													dbdao.createHostData(hostdata);
													dbnode = (DBNode) PollingEngine.getInstance().getDbByID(oracle.getId());
													dbnode.setAlarm(true);
													dbnode.setStatus(3);
													List alarmList = dbnode.getAlarmMessage();
													if (alarmList == null)
														alarmList = new ArrayList();
													dbnode.getAlarmMessage().add("���ݿ����ֹͣ");

													// String
													// ip=hostdata.getIpaddress();
													// hostdata.setIpaddress(ip+":"+)

													// ���Ͷ���

													createSMS("oracle", dbmonitorlist, oracle);
//													Hashtable hash = new Hashtable();
//													hash.elements()
//													while(hash.)
												} catch (Exception e) {
													e.printStackTrace();
												}
											}
										} else {
											try {
												// for (OracleEntity or : stops)
												// {
												Pingcollectdata hostdata = null;
												hostdata = new Pingcollectdata();
												hostdata.setIpaddress(serverip + ":" + oracle.getId());
												Calendar date = Calendar.getInstance();
												hostdata.setCollecttime(date);
												hostdata.setCategory("ORAPing");
												hostdata.setEntity("Utilization");
												hostdata.setSubentity("ConnectUtilization");
												hostdata.setRestype("dynamic");
												hostdata.setUnit("%");
												hostdata.setThevalue("0");
												dbdao.createHostData(hostdata);
												dbnode = (DBNode) PollingEngine.getInstance().getDbByID(oracle.getId());
												dbnode.setAlarm(true);
												List alarmList = dbnode.getAlarmMessage();
												if (alarmList == null)
													alarmList = new ArrayList();
												dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
												dbnode.setStatus(3);
												// }
												// Set set=allConnec.entrySet();
												// ���Ͷ���
												createSMS("oracle", dbmonitorlist, oracle);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}

									} else {
										try {
											// for (OracleEntity or :
											// oracleparts) {
											Pingcollectdata hostdata = null;
											hostdata = new Pingcollectdata();
											hostdata.setIpaddress(serverip + ":" + oracle.getId());
											Calendar date = Calendar.getInstance();
											hostdata.setCollecttime(date);
											hostdata.setCategory("ORAPing");
											hostdata.setEntity("Utilization");
											hostdata.setSubentity("ConnectUtilization");
											hostdata.setRestype("dynamic");
											hostdata.setUnit("%");
											hostdata.setThevalue("100");
											dbdao.createHostData(hostdata);
											if (sendeddata.containsKey("oracle" + ":" + serverip + ":" + oracle.getSid()))
												sendeddata.remove("oracle" + ":" + serverip + ":" + oracle.getSid());

										} catch (Exception e) {
											e.printStackTrace();
										}
										
									}
									if(oracleIsOK){
										//�����ݿ�������,�������ص����ݲɼ�
//										ShareData.setAlloracledata(serverip + ":" + oracle.getId(), oracledata);
//										Hashtable alldatas = (Hashtable) ShareData.getAlloracledata();
										Vector tableinfo_v = null;
//										Hashtable datas = (Hashtable) alldatas.get(serverip + ":" + oracle.getId());
										IpTranslation tranfer = new IpTranslation();
										String hex = tranfer.formIpToHex(vo.getIpAddress());
										String serveripstr = hex+":"+oracle.getSid();
										try {

//											tableinfo_v = (Vector) datas.get("tableinfo_v");
											tableinfo_v = dbdao.getOracle_nmsoraspaces(serveripstr);
										} catch (Exception e) {
											e.printStackTrace();
										}
										if (tableinfo_v != null && tableinfo_v.size() > 0) {
											// �滻ԭ����SESSSION����
											ShareData.setOraspacedata(serverip + ":" + oracle.getId(), tableinfo_v);
											OraspaceconfigDao oraspaceconfigManager = new OraspaceconfigDao();
											Hashtable oraspaces = oraspaceconfigManager.getByAlarmflag(1);
											oraspaceconfigManager.close();
											Vector spaces = new Vector();
											for (int k = 0; k < tableinfo_v.size(); k++) {
												Hashtable ht = (Hashtable) tableinfo_v.get(k);
												String tablespace = ht.get("tablespace").toString();
												if (spaces.contains(tablespace))
													continue;
												spaces.add(tablespace);
												String percent = ht.get("percent_free").toString();
												if (oraspaces.containsKey(serverip + ":" + oracle.getId() + ":" + tablespace)) {
													// ������Ҫ�澯�ı�ռ�
													Integer free = 0;
													try {
														free = new Float(percent).intValue();
													} catch (Exception e) {
														e.printStackTrace();
													}
													// ���ݱ�ռ�澯�����ж��Ƿ�澯
													Oraspaceconfig oraspaceconfig = (Oraspaceconfig) oraspaces.get(serverip + ":"
															+ oracle.getId() + ":" + tablespace);
													if (oraspaceconfig.getAlarmvalue() < (100 - free)) {
														// �澯
														dbnode = (DBNode) PollingEngine.getInstance().getDbByID(oracle.getId());
														dbnode.setAlarm(true);
														List alarmList = dbnode.getAlarmMessage();
														if (alarmList == null)
															alarmList = new ArrayList();
														dbnode.getAlarmMessage().add(
																oraspaceconfig.getSpacename() + "��ռ䳬����ֵ"
																		+ oraspaceconfig.getAlarmvalue());
														dbnode.setStatus(3);
														createSpaceSMS(dbmonitorlist, oraspaceconfig, oracle);
													}
												}

											}
										}
										if (tableinfo_v != null && tableinfo_v.size() > 0)
											ShareData.setOraspacedata(serverip + ":" + oracle.getId(), tableinfo_v);
										Vector info_v = null;
//										info_v = (Vector) datas.get("info_v");
										info_v = dbdao.getOracle_nmsorarollback(serveripstr);
										for (int j = 0; j < info_v.size(); j++) {
											Oracle_sessiondata os = new Oracle_sessiondata();
											Hashtable ht = (Hashtable) info_v.get(j);
											String machine = ht.get("machine").toString();
											String usernames = ht.get("username").toString();
											String program = ht.get("program").toString();
											String status1 = ht.get("status").toString();
											String sessiontype = ht.get("sessiontype").toString();
											String command = ht.get("command").toString();
											String logontime = ht.get("logontime").toString();
											os.setCommand(command);
											os.setLogontime(sdf1.parse(logontime));
											os.setMachine(machine);
											os.setMon_time(d1);
											os.setProgram(program);
											os.setSessiontype(sessiontype);
											os.setStatus(status1);
											os.setUsername(usernames);
//											IpTranslation tranfer = new IpTranslation();
											hex = tranfer.formIpToHex(serverip);
											os.setServerip(hex + ":" + oracle.getId());
											os.setDbname(dbnames);
											try {
												dbdao.addOracle_sessiondata(os);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									}
									// Hashtable oracledata = new Hashtable();

								}	
							}
							if(oracledata!=null)
								 ShareData.setAlloracledata(serverip,
								 oracledata);

						} else {
							// JDBC�ɼ���ʽ
							Hashtable oracledata = new Hashtable();
							allFlag = 0;
							/* modify zhao--------------------- */
							// List<OracleEntity> stops = new
							// LinkedList<OracleEntity>();
							// Hashtable<String, OracleEntity> allConnec = new
							// Hashtable<String, OracleEntity>();
							// for (OracleEntity ora : oracleparts) {
							try {
								//SysLogger.info(serverip+"============"+oracle.getSid());
								oracleIsOK = dbdao.getOracleIsOK(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle
									.getPassword()));
							}catch(Exception e){
								e.printStackTrace();
							}

							if (!oracleIsOK) {
								// allstatus.put(ora.getSid(),ora);
								allFlag = 1;
								// stops.add(ora);
								//dbnode = (DBNode) PollingEngine.getInstance().getDbByID(oracle.getId());
								dbnode = (DBNode) PollingEngine.getInstance().getDbByID(oracle.getDbid());
								dbnode.setAlarm(true);
								dbnode.setStatus(3);
							}
							// }
							//				
							/* modify end --------------- */
							if (allFlag == 1) {
								// ��Ҫ�������ݿ����ڵķ������Ƿ�����ͨ
								Host host = (Host) PollingEngine.getInstance().getNodeByIP(serverip);
								Vector ipPingData = (Vector) ShareData.getPingdata().get(serverip);
								if (ipPingData != null) {
									Pingcollectdata pingdata = (Pingcollectdata) ipPingData.get(0);
									Calendar tempCal = (Calendar) pingdata.getCollecttime();
									Date cc = tempCal.getTime();
									String time = sdf.format(cc);
									String lastTime = time;
									String pingvalue = pingdata.getThevalue();
									if (pingvalue == null || pingvalue.trim().length() == 0)
										pingvalue = "0";
									double pvalue = new Double(pingvalue);
									if (pvalue == 0) {
										// �������������Ӳ���***********************************************
										String sysLocation = "";
										// eventdao=null;
										try {
											SmscontentDao eventdao = new SmscontentDao();

											dbnode = (DBNode) PollingEngine.getInstance().getDbByID(oracle.getId());
											dbnode.setAlarm(true);
											dbnode.setStatus(3);
											List alarmList = dbnode.getAlarmMessage();
											if (alarmList == null)
												alarmList = new ArrayList();
											dbnode.getAlarmMessage().add("���ݿ����ȫ��ֹͣ");
											String eventdesc = "ORACLE(" + " IP:" + dbmonitorlist.getIpAddress() + ")"
													+ "�����ݿ����ֹͣ";
											eventdao.createEventWithReasion("poll", dbmonitorlist.getId() + ":" + oracle.getId(),
													dbmonitorlist.getAlias() + "(" + dbmonitorlist.getIpAddress() + ":"
															+ oracle.getSid() + ")", eventdesc, 3, "db", "ping", "���ڵķ��������Ӳ���");
										} catch (Exception e) {
											e.printStackTrace();
										} finally {
											// eventdao.close();
										}
									} else {
										try {
											Pingcollectdata hostdata = null;
											hostdata = new Pingcollectdata();
											hostdata.setIpaddress(serverip + ":" + oracle.getId());
											Calendar date = Calendar.getInstance();
											hostdata.setCollecttime(date);
											hostdata.setCategory("ORAPing");
											hostdata.setEntity("Utilization");
											hostdata.setSubentity("ConnectUtilization");
											hostdata.setRestype("dynamic");
											hostdata.setUnit("%");
											hostdata.setThevalue("0");
											dbdao.createHostData(hostdata);
											dbnode = (DBNode) PollingEngine.getInstance().getDbByID(oracle.getId());
											dbnode.setAlarm(true);
											dbnode.setStatus(3);
											List alarmList = dbnode.getAlarmMessage();
											if (alarmList == null)
												alarmList = new ArrayList();
											dbnode.getAlarmMessage().add("���ݿ����ֹͣ");

											// String
											// ip=hostdata.getIpaddress();
											// hostdata.setIpaddress(ip+":"+)

											// ���Ͷ���

											createSMS("oracle", dbmonitorlist, oracle);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}

								} else {
									try {
										// for (OracleEntity or : stops) {
										Pingcollectdata hostdata = null;
										hostdata = new Pingcollectdata();
										hostdata.setIpaddress(serverip + ":" + oracle.getId());
										Calendar date = Calendar.getInstance();
										hostdata.setCollecttime(date);
										hostdata.setCategory("ORAPing");
										hostdata.setEntity("Utilization");
										hostdata.setSubentity("ConnectUtilization");
										hostdata.setRestype("dynamic");
										hostdata.setUnit("%");
										hostdata.setThevalue("0");
										dbdao.createHostData(hostdata);
										dbnode = (DBNode) PollingEngine.getInstance().getDbByID(oracle.getDbid());
										dbnode.setAlarm(true);
										List alarmList = dbnode.getAlarmMessage();
										if (alarmList == null)
											alarmList = new ArrayList();
										dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
										dbnode.setStatus(3);
										// }
										// Set set=allConnec.entrySet();
										// ���Ͷ���
										createSMS("oracle", dbmonitorlist, oracle);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}

							} else {
								try {
									// for (OracleEntity or : oracleparts) {
									Pingcollectdata hostdata = null;
									hostdata = new Pingcollectdata();
									hostdata.setIpaddress(serverip + ":" + oracle.getId());
									Calendar date = Calendar.getInstance();
									hostdata.setCollecttime(date);
									hostdata.setCategory("ORAPing");
									hostdata.setEntity("Utilization");
									hostdata.setSubentity("ConnectUtilization");
									hostdata.setRestype("dynamic");
									hostdata.setUnit("%");
									hostdata.setThevalue("100");
									dbdao.createHostData(hostdata);
									if (sendeddata.containsKey("oracle" + ":" + serverip + ":" + oracle.getSid()))
										sendeddata.remove("oracle" + ":" + serverip + ":" + oracle.getSid());
									// }
								} catch (Exception e) {
									e.printStackTrace();
								}
								/* modify zhao ---------------------------- */
							}
							if(allFlag == 0){
								passwords = EncryptUtil.decode(oracle.getPassword());
								Hashtable allOraData =  dbdao.getAllOracleData(serverip, port, oracle.getSid(), oracle.getUser(), passwords);
								Vector info = (Vector) allOraData.get("session");
								Vector tableinfo = (Vector) allOraData.get("tablespace");
								Vector rollbackinfo_v = (Vector) allOraData.get("rollback");
								Hashtable sysValue = (Hashtable) allOraData.get("sysinfo");
								Hashtable memValue = (Hashtable) allOraData.get("ga_hash");
								Vector lockinfo_v = (Vector) allOraData.get("lock");
								Hashtable memPerfValue = (Hashtable) allOraData.get("memoryPerf");
								Vector table_v = (Vector) allOraData.get("table");
								Vector sql_v = (Vector) allOraData.get("topsql");
								Vector contrFile_v = (Vector) allOraData.get("controlfile");
								Hashtable isArchive_h = (Hashtable) allOraData.get("sy_hash");
								Vector logFile_v = (Vector) allOraData.get("log");
								Vector keepObj_v = (Vector) allOraData.get("keepobj");  
								String lstrnStatu = (String) allOraData.get("open_mode");
								Vector extent_v = (Vector) allOraData.get("extent");
								Hashtable userinfo_h = (Hashtable) allOraData.get("user");
								
//								Vector info = dbdao.getOracleSession(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle.getPassword()));
//								Vector tableinfo = dbdao.getOracleTableinfo(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle
//										.getPassword()));
//								Vector rollbackinfo_v = dbdao.getOracleRollbackinfo(serverip, port, oracle.getSid(),
//										oracle.getUser(), EncryptUtil.decode(oracle.getPassword()));
//								Hashtable sysValue = dbdao.getOracleSys(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle
//										.getPassword()));
//								Hashtable memValue = dbdao.getOracleMem(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle
//										.getPassword()));
//								Hashtable memPerfValue = dbdao.getOracleMemPerf(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle
//										.getPassword()));
//								Vector lockinfo_v = dbdao.getOracleLockinfo(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle
//										.getPassword()));
//								Vector sql_v = dbdao.getOracleTop10Sql(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle
//										.getPassword()));
//								Vector table_v = dbdao.getOracleTable(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle
//										.getPassword()));
//								Vector contrFile_v = dbdao.getOracleControlFile(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle
//										.getPassword()));
//								Hashtable isArchive_h = dbdao.getOracleIsArchive(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle
//										.getPassword()));
//								Vector logFile_v = dbdao.getOracleLogFile(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle
//										.getPassword()));
//								Vector keepObj_v = dbdao.getOracleKeepObj(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle
//										.getPassword()));
//								String lstrnStatu = dbdao.getOracleLstn(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle
//										.getPassword()));
//								Vector extent_v = dbdao.getOracleExtent(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle
//										.getPassword()));
//								Hashtable userinfo_h = dbdao.getOracleUserinfo(serverip, port, oracle.getSid(), oracle.getUser(), EncryptUtil.decode(oracle
//										.getPassword()));
								
								if (info == null)
									info = new Vector();
								if (sysValue == null)
									sysValue = new Hashtable();
								if (memValue == null)
									memValue = new Hashtable();
								if (memPerfValue == null)
									memPerfValue = new Hashtable();
								if (tableinfo == null)
									tableinfo = new Vector();
								if (rollbackinfo_v == null)
									rollbackinfo_v = new Vector();
								if (lockinfo_v == null)
									lockinfo_v = new Vector();
								if (table_v == null)
									table_v = new Vector();
								if (sql_v == null)
									sql_v = new Vector();
								if (contrFile_v == null)
									contrFile_v = new Vector();
								if (logFile_v == null)
									logFile_v = new Vector();
								if (keepObj_v == null)
									keepObj_v = new Vector();
								if (isArchive_h == null)
									isArchive_h = new Hashtable();
								if(lstrnStatu == null)
									lstrnStatu = "";
								if(extent_v == null)
									extent_v = new Vector();
								if(userinfo_h == null)
									userinfo_h = new Hashtable();
								oracledata.put("sysValue", sysValue);
								oracledata.put("memValue", memValue);
								oracledata.put("memPerfValue", memPerfValue);
								oracledata.put("tableinfo_v", tableinfo);
								oracledata.put("rollbackinfo_v", rollbackinfo_v);
								oracledata.put("lockinfo_v", lockinfo_v);
								oracledata.put("info_v", info);
								oracledata.put("table_v", table_v);
								oracledata.put("sql_v", sql_v);
								oracledata.put("contrFile_v", contrFile_v);
								oracledata.put("isArchive_h", isArchive_h);
								oracledata.put("keepObj_v", keepObj_v);
								oracledata.put("lstrnStatu", lstrnStatu);
								oracledata.put("extent_v", extent_v);
								oracledata.put("logFile_v", logFile_v);
								oracledata.put("userinfo_h", userinfo_h);
								oracledata.put("status", "1");
								ShareData.setAlloracledata(serverip + ":" + oracle.getId(), oracledata);
								// }
								/*-----------modify  end  ------------------------------*/

//								Hashtable alldatas = (Hashtable) ShareData.getAlloracledata();
								IpTranslation tranfer = new IpTranslation();
								String hex = tranfer.formIpToHex(vo.getIpAddress());
								String serveripstr = hex+":"+oracle.getSid();
								Vector tableinfo_v = null;
								Hashtable datas=oracledata;
								try {

//									tableinfo_v = (Vector) datas.get("tableinfo_v");
									tableinfo_v = dbdao.getOracle_nmsoraspaces(serveripstr);
								} catch (Exception e) {
									e.printStackTrace();
								}
								if (tableinfo_v != null && tableinfo_v.size() > 0) {
									// �滻ԭ����SESSSION����
									ShareData.setOraspacedata(serverip + ":" + oracle.getId(), tableinfo_v);
									OraspaceconfigDao oraspaceconfigManager = new OraspaceconfigDao();
									Hashtable oraspaces = oraspaceconfigManager.getByAlarmflag(1);
									oraspaceconfigManager.close();
									Vector spaces = new Vector();
									for (int k = 0; k < tableinfo_v.size(); k++) {
										Hashtable ht = (Hashtable) tableinfo_v.get(k);
										String tablespace = ht.get("tablespace").toString();
										if (spaces.contains(tablespace))
											continue;
										spaces.add(tablespace);
										String percent = ht.get("percent_free").toString();
										if (oraspaces.containsKey(serverip + ":" + oracle.getId() + ":" + tablespace)) {
											// ������Ҫ�澯�ı�ռ�
											Integer free = 0;
											try {
												free = new Float(percent).intValue();
											} catch (Exception e) {
												e.printStackTrace();
											}
											// ���ݱ�ռ�澯�����ж��Ƿ�澯
											Oraspaceconfig oraspaceconfig = (Oraspaceconfig) oraspaces.get(serverip + ":"
													+ oracle.getId() + ":" + tablespace);
											if (oraspaceconfig.getAlarmvalue() < (100 - free)) {
												// �澯
												dbnode = (DBNode) PollingEngine.getInstance().getDbByID(oracle.getId());
												dbnode.setAlarm(true);
												List alarmList = dbnode.getAlarmMessage();
												if (alarmList == null)
													alarmList = new ArrayList();
												dbnode.getAlarmMessage().add(
														oraspaceconfig.getSpacename() + "��ռ䳬����ֵ" + oraspaceconfig.getAlarmvalue());
												dbnode.setStatus(3);
												createSpaceSMS(dbmonitorlist, oraspaceconfig, oracle);
											}
										}

									}
								}
								if (tableinfo_v != null && tableinfo_v.size() > 0)
									ShareData.setOraspacedata(serverip + ":" + oracle.getId(), tableinfo_v);
								Vector info_v = null;
								info_v = (Vector) datas.get("info_v");
								for (int j = 0; j < info_v.size(); j++) {
									Oracle_sessiondata os = new Oracle_sessiondata();
									Hashtable ht = (Hashtable) info_v.get(j);
									String machine = ht.get("machine").toString();
									String usernames = ht.get("username").toString();
									String program = ht.get("program").toString();
									String status = ht.get("status").toString();
									String sessiontype = ht.get("sessiontype").toString();
									String command = ht.get("command").toString();
									String logontime = ht.get("logontime").toString();
									os.setCommand(command);
									os.setLogontime(sdf1.parse(logontime));
									os.setMachine(machine);
									os.setMon_time(d1);
									os.setProgram(program);
									os.setSessiontype(sessiontype);
									os.setStatus(status);
									os.setUsername(usernames);
//									IpTranslation tranfer = new IpTranslation();
									hex = tranfer.formIpToHex(serverip);
									os.setServerip(hex + ":" + oracle.getId());
									os.setDbname(dbnames);
									try {
										dbdao.addOracle_sessiondata(os);
									} catch (Exception e) {
										e.printStackTrace();
									}
							}
						}
							//allFlag == 1
							if(oracledata!=null)
							   ShareData.setAlloracledata(serverip + ":" + oracle.getId(), oracledata);
						}
					}
				}

			}

			//ȡ��sysbase�ɼ�
			if (sysbaselist != null) {
				SybspaceconfigDao sybspaceconfigManager = new SybspaceconfigDao();
				Hashtable sybspaceconfig = new Hashtable();
				try {
					sybspaceconfig = sybspaceconfigManager.getByAlarmflag(1);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					sybspaceconfigManager.close();
				}

				for (int i = 0; i < sysbaselist.size(); i++) {
					Object obj = sysbaselist.get(i);

					DBVo dbmonitorlist = new DBVo();
					BeanUtils.copyProperties(dbmonitorlist, obj);
					DBNode dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
					dbnode.setAlarm(false);
					dbnode.setStatus(0);
					Calendar _tempCal = Calendar.getInstance();
					Date _cc = _tempCal.getTime();
					String _time = sdf.format(_cc);
					dbnode.setLastTime(_time);
					dbnode.getAlarmMessage().clear();

					String serverip = dbmonitorlist.getIpAddress();
					String username = dbmonitorlist.getUser();
					String passwords = dbmonitorlist.getPassword();
					int port = Integer.parseInt(dbmonitorlist.getPort());
					String dbnames = dbmonitorlist.getDbName();
					Date d1 = new Date();
					//�жϸ����ݿ��Ƿ���������
					boolean sysbaseIsOK = false;
					if (dbnode.getCollecttype() == SystemConstant.DBCOLLECTTYPE_SHELL) {
						// �ű��ɼ���ʽ
						String filename = ResourceCenter.getInstance().getSysPath() + "/linuxserver/" + serverip + ".sysbase.log";
						File file = new File(filename);
						if (!file.exists()) {
							// �ļ�������,������澯
							try {
								createFileNotExistSMS(serverip);
							} catch (Exception e) {
								e.printStackTrace();
							}
							continue;
						}
						SysLogger.info("###��ʼ����SysBase:" + serverip + "�����ļ�###");

						LoadSysbaseFile loadsysbase = new LoadSysbaseFile(filename);
						Hashtable allSysbaseDatas = new Hashtable();
						try {
							allSysbaseDatas = loadsysbase.getSysbaseConfig();
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (allSysbaseDatas != null && allSysbaseDatas.size() > 0) {
							if (allSysbaseDatas.containsKey("status")) {
								int status = Integer.parseInt(allSysbaseDatas.get("status").toString());
								if (status == 1)
									sysbaseIsOK = true;
								if (!sysbaseIsOK) {
									// ��Ҫ�������ݿ����ڵķ������Ƿ�����ͨ
									Host host = (Host) PollingEngine.getInstance().getNodeByIP(serverip);
									Vector ipPingData = (Vector) ShareData.getPingdata().get(serverip);
									if (ipPingData != null) {
										Pingcollectdata pingdata = (Pingcollectdata) ipPingData.get(0);
										Calendar tempCal = (Calendar) pingdata.getCollecttime();
										Date cc = tempCal.getTime();
										String time = sdf.format(cc);
										String lastTime = time;
										String pingvalue = pingdata.getThevalue();
										if (pingvalue == null || pingvalue.trim().length() == 0)
											pingvalue = "0";
										double pvalue = new Double(pingvalue);
										if (pvalue == 0) {
											// �������������Ӳ���***********************************************
											dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
											dbnode.setAlarm(true);
											dbnode.setStatus(3);
											List alarmList = dbnode.getAlarmMessage();
											if (alarmList == null)
												alarmList = new ArrayList();
											dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
											String sysLocation = "";
											try {
												SmscontentDao eventdao = new SmscontentDao();
												String eventdesc = "SYBASE(" + dbmonitorlist.getDbName() + " IP:"
														+ dbmonitorlist.getIpAddress() + ")" + "�����ݿ����ֹͣ";
												eventdao.createEventWithReasion("poll", dbmonitorlist.getId() + "", dbmonitorlist
														.getAlias()
														+ "(" + dbmonitorlist.getIpAddress() + ")", eventdesc, 3, "db", "ping",
														"���ڵķ��������Ӳ���");
											} catch (Exception e) {
												e.printStackTrace();
											}
										} else {
											Pingcollectdata hostdata = null;
											hostdata = new Pingcollectdata();
											hostdata.setIpaddress(serverip);
											Calendar date = Calendar.getInstance();
											hostdata.setCollecttime(date);
											hostdata.setCategory("SYSPing");
											hostdata.setEntity("Utilization");
											hostdata.setSubentity("ConnectUtilization");
											hostdata.setRestype("dynamic");
											hostdata.setUnit("%");
											hostdata.setThevalue("0");
											try {
												dbdao.createHostData(hostdata);
												// ���Ͷ���
												dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
												dbnode.setAlarm(true);
												List alarmList = dbnode.getAlarmMessage();
												if (alarmList == null)
													alarmList = new ArrayList();
												dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
												dbnode.setStatus(3);
												createSMS("sybase", dbmonitorlist);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}

									} else {
										Pingcollectdata hostdata = null;
										hostdata = new Pingcollectdata();
										hostdata.setIpaddress(serverip);
										Calendar date = Calendar.getInstance();
										hostdata.setCollecttime(date);
										hostdata.setCategory("SYSPing");
										hostdata.setEntity("Utilization");
										hostdata.setSubentity("ConnectUtilization");
										hostdata.setRestype("dynamic");
										hostdata.setUnit("%");
										hostdata.setThevalue("0");
										try {
											dbdao.createHostData(hostdata);
											// ���Ͷ���
											dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
											dbnode.setAlarm(true);
											List alarmList = dbnode.getAlarmMessage();
											if (alarmList == null)
												alarmList = new ArrayList();
											dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
											dbnode.setStatus(3);
											createSMS("sybase", dbmonitorlist);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								} else {
									Pingcollectdata hostdata = null;
									hostdata = new Pingcollectdata();
									hostdata.setIpaddress(serverip);
									Calendar date = Calendar.getInstance();
									hostdata.setCollecttime(date);
									hostdata.setCategory("SYSPing");
									hostdata.setEntity("Utilization");
									hostdata.setSubentity("ConnectUtilization");
									hostdata.setRestype("dynamic");
									hostdata.setUnit("%");
									hostdata.setThevalue("100");
									try {
										dbdao.createHostData(hostdata);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								Hashtable retValue = new Hashtable();
								if (sysbaseIsOK) {// �����ݿ��������ϣ���������ݿ����ݵĲɼ�
									SybaseVO sysbaseVO = new SybaseVO();

									try {
										// sysbaseVO =
										// dbdao.getSysbaseInfo(serverip,
										// port, username, passwords);
										sysbaseVO = (SybaseVO) allSysbaseDatas.get("sysbase");
									} catch (Exception e) {
										e.printStackTrace();
									}
									if (sysbaseVO == null)
										sysbaseVO = new SybaseVO();
									
									retValue.put("status", "1");
									retValue.put("sysbaseVO", sysbaseVO);
									
									List allspace = sysbaseVO.getDbInfo();
									if (allspace != null && allspace.size() > 0) {
										for (int k = 0; k < allspace.size(); k++) {
											TablesVO tvo = (TablesVO) allspace.get(k);
											if (sybspaceconfig.containsKey(serverip + ":" + tvo.getDb_name())) {
												// �澯�ж�
												Sybspaceconfig sybconfig = (Sybspaceconfig) sybspaceconfig.get(serverip + ":"
														+ tvo.getDb_name());
												Integer usedperc = Integer.parseInt(tvo.getDb_usedperc());
												if (usedperc > sybconfig.getAlarmvalue()) {
													// ������ֵ�澯
													dbnode = (DBNode) PollingEngine.getInstance()
															.getDbByID(dbmonitorlist.getId());
													dbnode.setAlarm(true);
													List alarmList = dbnode.getAlarmMessage();
													if (alarmList == null)
														alarmList = new ArrayList();
													dbnode.getAlarmMessage().add(
															sybconfig.getSpacename() + "��ռ䳬����ֵ" + sybconfig.getAlarmvalue());
													dbnode.setStatus(3);
													createSybSpaceSMS(dbmonitorlist, sybconfig);
												}
											}
										}
									}
								}
								if(retValue!=null)
								   ShareData.setSysbasedata(serverip, retValue);
							}
						}
						// ////////////////////////////////////////////////////////////
					} else {
						//JDBC�ɼ���ʽ
						try {
							sysbaseIsOK = dbdao.getSysbaseIsOk(serverip, username, passwords, port);
						} catch (Exception e) {
							e.printStackTrace();
							sysbaseIsOK = false;
						}
						if (!sysbaseIsOK) {
							//��Ҫ�������ݿ����ڵķ������Ƿ�����ͨ
							Host host = (Host) PollingEngine.getInstance().getNodeByIP(serverip);
							Vector ipPingData = (Vector) ShareData.getPingdata().get(serverip);
							if (ipPingData != null) {
								Pingcollectdata pingdata = (Pingcollectdata) ipPingData.get(0);
								Calendar tempCal = (Calendar) pingdata.getCollecttime();
								Date cc = tempCal.getTime();
								String time = sdf.format(cc);
								String lastTime = time;
								String pingvalue = pingdata.getThevalue();
								if (pingvalue == null || pingvalue.trim().length() == 0)
									pingvalue = "0";
								double pvalue = new Double(pingvalue);
								if (pvalue == 0) {
									//�������������Ӳ���***********************************************
									dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
									dbnode.setAlarm(true);
									dbnode.setStatus(3);
									List alarmList = dbnode.getAlarmMessage();
									if (alarmList == null)
										alarmList = new ArrayList();
									dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
									String sysLocation = "";
									try {
										SmscontentDao eventdao = new SmscontentDao();
										String eventdesc = "SYBASE(" + dbmonitorlist.getDbName() + " IP:"
												+ dbmonitorlist.getIpAddress() + ")" + "�����ݿ����ֹͣ";
										eventdao.createEventWithReasion("poll", dbmonitorlist.getId() + "", dbmonitorlist
												.getAlias()
												+ "(" + dbmonitorlist.getIpAddress() + ")", eventdesc, 3, "db", "ping",
												"���ڵķ��������Ӳ���");
									} catch (Exception e) {
										e.printStackTrace();
									}
								} else {
									Pingcollectdata hostdata = null;
									hostdata = new Pingcollectdata();
									hostdata.setIpaddress(serverip);
									Calendar date = Calendar.getInstance();
									hostdata.setCollecttime(date);
									hostdata.setCategory("SYSPing");
									hostdata.setEntity("Utilization");
									hostdata.setSubentity("ConnectUtilization");
									hostdata.setRestype("dynamic");
									hostdata.setUnit("%");
									hostdata.setThevalue("0");
									try {
										dbdao.createHostData(hostdata);
										//���Ͷ���	
										dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
										dbnode.setAlarm(true);
										List alarmList = dbnode.getAlarmMessage();
										if (alarmList == null)
											alarmList = new ArrayList();
										dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
										dbnode.setStatus(3);
										createSMS("sybase", dbmonitorlist);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}

							} else {
								Pingcollectdata hostdata = null;
								hostdata = new Pingcollectdata();
								hostdata.setIpaddress(serverip);
								Calendar date = Calendar.getInstance();
								hostdata.setCollecttime(date);
								hostdata.setCategory("SYSPing");
								hostdata.setEntity("Utilization");
								hostdata.setSubentity("ConnectUtilization");
								hostdata.setRestype("dynamic");
								hostdata.setUnit("%");
								hostdata.setThevalue("0");
								try {
									dbdao.createHostData(hostdata);
									//���Ͷ���	
									dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
									dbnode.setAlarm(true);
									List alarmList = dbnode.getAlarmMessage();
									if (alarmList == null)
										alarmList = new ArrayList();
									dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
									dbnode.setStatus(3);
									createSMS("sybase", dbmonitorlist);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						} else {
							Pingcollectdata hostdata = null;
							hostdata = new Pingcollectdata();
							hostdata.setIpaddress(serverip);
							Calendar date = Calendar.getInstance();
							hostdata.setCollecttime(date);
							hostdata.setCategory("SYSPing");
							hostdata.setEntity("Utilization");
							hostdata.setSubentity("ConnectUtilization");
							hostdata.setRestype("dynamic");
							hostdata.setUnit("%");
							hostdata.setThevalue("100");
							try {
								dbdao.createHostData(hostdata);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						Hashtable retValue = new Hashtable();
						if (sysbaseIsOK) {//�����ݿ��������ϣ���������ݿ����ݵĲɼ�
							SybaseVO sysbaseVO = new SybaseVO();
							try {
								sysbaseVO = dbdao.getSysbaseInfo(serverip, port, username, passwords);
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (sysbaseVO == null)
								sysbaseVO = new SybaseVO();
						
							retValue.put("sysbaseVO", sysbaseVO);
							retValue.put("status", "1");
							
							List allspace = sysbaseVO.getDbInfo();
							if (allspace != null && allspace.size() > 0) {
								for (int k = 0; k < allspace.size(); k++) {
									TablesVO tvo = (TablesVO) allspace.get(k);
									if (sybspaceconfig.containsKey(serverip + ":" + tvo.getDb_name())) {
										//�澯�ж�
										Sybspaceconfig sybconfig = (Sybspaceconfig) sybspaceconfig.get(serverip + ":"
												+ tvo.getDb_name());
										Integer usedperc = Integer.parseInt(tvo.getDb_usedperc());
										if (usedperc > sybconfig.getAlarmvalue()) {
											//������ֵ�澯
											dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
											dbnode.setAlarm(true);
											List alarmList = dbnode.getAlarmMessage();
											if (alarmList == null)
												alarmList = new ArrayList();
											dbnode.getAlarmMessage().add(
													sybconfig.getSpacename() + "��ռ䳬����ֵ" + sybconfig.getAlarmvalue());
											dbnode.setStatus(3);
											createSybSpaceSMS(dbmonitorlist, sybconfig);
										}
									}
								}
							}
						}
						if(retValue!=null){
							ShareData.setSysbasedata(serverip, retValue);
						}
					}

					SysLogger.info("end collect sysbase --------- " + serverip);
				}
			}

			//ȡ��informix�ɼ�
			if (informixlist != null) {
				for (int i = 0; i < informixlist.size(); i++) {
					Hashtable monitorValue = new Hashtable();

					Object obj = informixlist.get(i);

					DBVo dbmonitorlist = new DBVo();
					BeanUtils.copyProperties(dbmonitorlist, obj);
					DBNode dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
					dbnode.setAlarm(false);
					dbnode.setStatus(0);
					Calendar _tempCal = Calendar.getInstance();
					Date _cc = _tempCal.getTime();
					String _time = sdf.format(_cc);
					dbnode.setLastTime(_time);
					dbnode.getAlarmMessage().clear();

					String serverip = dbmonitorlist.getIpAddress();
					String username = dbmonitorlist.getUser();
					String passwords = dbmonitorlist.getPassword();
					int port = Integer.parseInt(dbmonitorlist.getPort());
					String dbnames = dbmonitorlist.getDbName();
					String dbservername = dbmonitorlist.getAlias();//��ʱ�ķ�������
					Date d1 = new Date();
					//�жϸ����ݿ��Ƿ���������
					int allFlag = 0;
					SysLogger.info("begin collect informix--" + dbnames + " --------- " + serverip);
					boolean informixIsOK = false;
					if (dbnode.getCollecttype() == SystemConstant.DBCOLLECTTYPE_SHELL) {
						// �ű��ɼ���ʽ
						String filename = ResourceCenter.getInstance().getSysPath() + "/linuxserver/" + serverip
								+ ".informix.log";
						File file = new File(filename);
						if (!file.exists()) {
							// �ļ�������,������澯
							try {
								createFileNotExistSMS(serverip);
							} catch (Exception e) {
								e.printStackTrace();
							}
							continue;
						}
						SysLogger.info("###��ʼ����Informix:" + serverip + "�����ļ�###");
						LoadInformixFile load = new LoadInformixFile(filename);
						Hashtable informixData = new Hashtable();
						try {
							informixData = load.getInformixFile();
						} catch (Exception e) {
							e.printStackTrace();
						}

						if (informixData != null && informixData.size() > 0) {
							if (informixData.containsKey("status")) {
								int status = Integer.parseInt(informixData.get("status").toString());
								if (status == 1)
									informixIsOK = true;
								if (!informixIsOK) {
									dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
									dbnode.setAlarm(true);
									createSMS("informix", dbmonitorlist);
									allFlag = 1;
								} else {
									// �������ϣ���������ݲɼ�
									Hashtable returnValue = new Hashtable();
									try {
										// returnValue =
										// dbdao.getInformixDBConfig(
										// serverip, port + "", username,
										// passwords, dbnames, dbservername);
										returnValue = (Hashtable) informixData.get("informix");
									} catch (Exception e) {
										e.printStackTrace();
									}
									// �Ա�ռ�ֵ���и澯�ж�
									if (returnValue != null && returnValue.size() > 0) {
										if (returnValue.containsKey("informixspaces")) {
											List spaceList = (List) returnValue.get("informixspaces");// �ռ���Ϣ
											if (spaceList != null && spaceList.size() > 0) {
												// �滻ԭ���ı�ռ�����
												SysLogger.info("add infromix space size=====" + spaceList.size());
												ShareData.setInformixspacedata(serverip, spaceList);
												InformixspaceconfigDao informixspaceconfigdao = new InformixspaceconfigDao();
												Hashtable informixspaces = new Hashtable();
												try {
													informixspaces = informixspaceconfigdao.getByAlarmflag(1);
												} catch (Exception e) {
													e.printStackTrace();
												} finally {
													informixspaceconfigdao.close();
												}
												Vector spaces = new Vector();
												for (int k = 0; k < spaceList.size(); k++) {
													Hashtable ht = (Hashtable) spaceList.get(k);
													String tablespace = ht.get("dbspace").toString();
													if (spaces.contains(tablespace))
														continue;
													spaces.add(tablespace);
													String percent = ht.get("percent_free").toString();
													if (informixspaces.containsKey(serverip + ":" + tablespace)) {
														// ������Ҫ�澯�ı�ռ�
														Integer free = 0;
														try {
															free = new Float(percent).intValue();
														} catch (Exception e) {
															e.printStackTrace();
														}
														// ���ݱ�ռ�澯�����ж��Ƿ�澯
														Informixspaceconfig informixspaceconfig = (Informixspaceconfig) informixspaces
																.get(serverip + ":" + tablespace);
														if (informixspaceconfig.getAlarmvalue() < (100 - free)) {
															// �澯
															dbnode = (DBNode) PollingEngine.getInstance().getDbByID(
																	dbmonitorlist.getId());
															dbnode.setAlarm(true);
															List alarmList = dbnode.getAlarmMessage();
															if (alarmList == null)
																alarmList = new ArrayList();
															dbnode.getAlarmMessage().add(
																	informixspaceconfig.getSpacename() + "��ռ䳬����ֵ"
																			+ informixspaceconfig.getAlarmvalue());
															dbnode.setStatus(3);
															try {
																createInformixSpaceSMS(dbmonitorlist, informixspaceconfig);
															} catch (Exception e) {
																SysLogger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
																e.printStackTrace();
															}
														}
													}

												}
											}
										}
									}
									monitorValue.put(dbnames, informixData);

								}
								SysLogger.info("end collect informix--" + dbnames + " --------- " + serverip);
								if (allFlag == 1) {
									// ��һ�����ݿ��ǲ�ͨ��
									// ��Ҫ�������ݿ����ڵķ������Ƿ�����ͨ
									Host host = (Host) PollingEngine.getInstance().getNodeByIP(serverip);
									Vector ipPingData = (Vector) ShareData.getPingdata().get(serverip);
									if (ipPingData != null) {
										Pingcollectdata pingdata = (Pingcollectdata) ipPingData.get(0);
										Calendar tempCal = (Calendar) pingdata.getCollecttime();
										Date cc = tempCal.getTime();
										String time = sdf.format(cc);
										String lastTime = time;
										String pingvalue = pingdata.getThevalue();
										if (pingvalue == null || pingvalue.trim().length() == 0)
											pingvalue = "0";
										double pvalue = new Double(pingvalue);
										if (pvalue == 0) {
											// �������������Ӳ���***********************************************
											dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
											dbnode.setAlarm(true);
											dbnode.setStatus(3);
											List alarmList = dbnode.getAlarmMessage();
											if (alarmList == null)
												alarmList = new ArrayList();
											dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
											String sysLocation = "";
											try {
												SmscontentDao eventdao = new SmscontentDao();
												String eventdesc = "Informix(" + dbmonitorlist.getDbName() + " IP:"
														+ dbmonitorlist.getIpAddress() + ")" + "�����ݿ����ֹͣ";
												eventdao.createEventWithReasion("poll", dbmonitorlist.getId() + "", dbmonitorlist
														.getAlias()
														+ "(" + dbmonitorlist.getIpAddress() + ")", eventdesc, 3, "db", "ping",
														"���ڵķ��������Ӳ���");
											} catch (Exception e) {
												e.printStackTrace();
											}
										} else {
											Pingcollectdata hostdata = null;
											hostdata = new Pingcollectdata();
											hostdata.setIpaddress(serverip);
											Calendar date = Calendar.getInstance();
											hostdata.setCollecttime(date);
											hostdata.setCategory("INFORMIXPing");
											hostdata.setEntity("Utilization");
											hostdata.setSubentity("ConnectUtilization");
											hostdata.setRestype("dynamic");
											hostdata.setUnit("%");
											hostdata.setThevalue("0");
											try {
												dbdao.createHostData(hostdata);
												// ���Ͷ���
												dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
												dbnode.setAlarm(true);
												List alarmList = dbnode.getAlarmMessage();
												if (alarmList == null)
													alarmList = new ArrayList();
												dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
												dbnode.setStatus(3);
												createSMS("informix", dbmonitorlist);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}

									} else {
										Pingcollectdata hostdata = null;
										hostdata = new Pingcollectdata();
										hostdata.setIpaddress(serverip);
										Calendar date = Calendar.getInstance();
										hostdata.setCollecttime(date);
										hostdata.setCategory("INFORMIXPing");
										hostdata.setEntity("Utilization");
										hostdata.setSubentity("ConnectUtilization");
										hostdata.setRestype("dynamic");
										hostdata.setUnit("%");
										hostdata.setThevalue("0");
										try {
											dbdao.createHostData(hostdata);
											// ���Ͷ���
											dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
											dbnode.setAlarm(true);
											List alarmList = dbnode.getAlarmMessage();
											if (alarmList == null)
												alarmList = new ArrayList();
											dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
											dbnode.setStatus(3);
											createSMS("informix", dbmonitorlist);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								} else {
									Pingcollectdata hostdata = null;
									hostdata = new Pingcollectdata();
									hostdata.setIpaddress(serverip);
									Calendar date = Calendar.getInstance();
									hostdata.setCollecttime(date);
									hostdata.setCategory("INFORMIXPing");
									hostdata.setEntity("Utilization");
									hostdata.setSubentity("ConnectUtilization");
									hostdata.setRestype("dynamic");
									hostdata.setUnit("%");
									hostdata.setThevalue("100");
									try {
										dbdao.createHostData(hostdata);
									} catch (Exception e) {
										e.printStackTrace();
									}

								}
							}
							if (allFlag == 0) {
								monitorValue.put("runningflag", "��������");
							} else {
								monitorValue.put("runningflag", "<font color=red>����ֹͣ</font>");
							}

							if (monitorValue != null && monitorValue.size() > 0) {
								ShareData.setInfomixmonitordata(serverip, monitorValue);
							}
						}
						// ///////////////////////////////////////////////////////////////
					} else {
						//JDBC�ɼ���ʽ
						try {
							informixIsOK = dbdao.getInformixIsOk(serverip, port + "", username, passwords, dbnames, dbservername);
						} catch (Exception e) {
							e.printStackTrace();
							informixIsOK = false;
						}
						if (!informixIsOK) {
							dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
							dbnode.setAlarm(true);
							createSMS("informix", dbmonitorlist);
							allFlag = 1;
						} else {
							//�������ϣ���������ݲɼ�
							Hashtable returnValue = new Hashtable();
							try {
								returnValue = dbdao.getInformixDBConfig(serverip, port + "", username, passwords, dbnames,
										dbservername);
							} catch (Exception e) {
								e.printStackTrace();
							}
							//�Ա�ռ�ֵ���и澯�ж�
							if (returnValue != null && returnValue.size() > 0) {
								if (returnValue.containsKey("informixspaces")) {
									List spaceList = (List) returnValue.get("informixspaces");//�ռ���Ϣ
									if (spaceList != null && spaceList.size() > 0) {
										//�滻ԭ���ı�ռ�����		
										//SysLogger.info("add infromix space size====="+spaceList.size());
										ShareData.setInformixspacedata(serverip, spaceList);
										InformixspaceconfigDao informixspaceconfigdao = new InformixspaceconfigDao();
										Hashtable informixspaces = new Hashtable();
										try {
											informixspaces = informixspaceconfigdao.getByAlarmflag(1);
										} catch (Exception e) {
											e.printStackTrace();
										} finally {
											informixspaceconfigdao.close();
										}
										Vector spaces = new Vector();
										for (int k = 0; k < spaceList.size(); k++) {
											Hashtable ht = (Hashtable) spaceList.get(k);
											String tablespace = ht.get("dbspace").toString();
											if (spaces.contains(tablespace))
												continue;
											spaces.add(tablespace);
											String percent = ht.get("percent_free").toString();
											if (informixspaces.containsKey(serverip + ":" + tablespace)) {
												//������Ҫ�澯�ı�ռ�								
												Integer free = 0;
												try {
													free = new Float(percent).intValue();
												} catch (Exception e) {
													e.printStackTrace();
												}
												//���ݱ�ռ�澯�����ж��Ƿ�澯
												Informixspaceconfig informixspaceconfig = (Informixspaceconfig) informixspaces
														.get(serverip + ":" + tablespace);
												if (informixspaceconfig.getAlarmvalue() < (100 - free)) {
													//�澯
													dbnode = (DBNode) PollingEngine.getInstance()
															.getDbByID(dbmonitorlist.getId());
													dbnode.setAlarm(true);
													List alarmList = dbnode.getAlarmMessage();
													if (alarmList == null)
														alarmList = new ArrayList();
													dbnode.getAlarmMessage().add(
															informixspaceconfig.getSpacename() + "��ռ䳬����ֵ"
																	+ informixspaceconfig.getAlarmvalue());
													dbnode.setStatus(3);
													try {
														createInformixSpaceSMS(dbmonitorlist, informixspaceconfig);
													} catch (Exception e) {
														//SysLogger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
														e.printStackTrace();
													}
												}
											}

										}
									}
								}
							}
							Hashtable informixData = new Hashtable();
							informixData.put("status", "1");
							informixData.put("informix", returnValue);
							monitorValue.put(dbnames, informixData);

						}
						SysLogger.info("end collect informix--" + dbnames + " --------- " + serverip);
						if (allFlag == 1) {
							//��һ�����ݿ��ǲ�ͨ��
							//��Ҫ�������ݿ����ڵķ������Ƿ�����ͨ
							Host host = (Host) PollingEngine.getInstance().getNodeByIP(serverip);
							Vector ipPingData = (Vector) ShareData.getPingdata().get(serverip);
							if (ipPingData != null) {
								Pingcollectdata pingdata = (Pingcollectdata) ipPingData.get(0);
								Calendar tempCal = (Calendar) pingdata.getCollecttime();
								Date cc = tempCal.getTime();
								String time = sdf.format(cc);
								String lastTime = time;
								String pingvalue = pingdata.getThevalue();
								if (pingvalue == null || pingvalue.trim().length() == 0)
									pingvalue = "0";
								double pvalue = new Double(pingvalue);
								if (pvalue == 0) {
									//�������������Ӳ���***********************************************
									dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
									dbnode.setAlarm(true);
									dbnode.setStatus(3);
									List alarmList = dbnode.getAlarmMessage();
									if (alarmList == null)
										alarmList = new ArrayList();
									dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
									String sysLocation = "";
									try {
										SmscontentDao eventdao = new SmscontentDao();
										String eventdesc = "Informix(" + dbmonitorlist.getDbName() + " IP:"
												+ dbmonitorlist.getIpAddress() + ")" + "�����ݿ����ֹͣ";
										eventdao.createEventWithReasion("poll", dbmonitorlist.getId() + "", dbmonitorlist
												.getAlias()
												+ "(" + dbmonitorlist.getIpAddress() + ")", eventdesc, 3, "db", "ping",
												"���ڵķ��������Ӳ���");
									} catch (Exception e) {
										e.printStackTrace();
									}
								} else {
									Pingcollectdata hostdata = null;
									hostdata = new Pingcollectdata();
									hostdata.setIpaddress(serverip);
									Calendar date = Calendar.getInstance();
									hostdata.setCollecttime(date);
									hostdata.setCategory("INFORMIXPing");
									hostdata.setEntity("Utilization");
									hostdata.setSubentity("ConnectUtilization");
									hostdata.setRestype("dynamic");
									hostdata.setUnit("%");
									hostdata.setThevalue("0");
									try {
										dbdao.createHostData(hostdata);
										//���Ͷ���	
										dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
										dbnode.setAlarm(true);
										List alarmList = dbnode.getAlarmMessage();
										if (alarmList == null)
											alarmList = new ArrayList();
										dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
										dbnode.setStatus(3);
										createSMS("informix", dbmonitorlist);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}

							} else {
								Pingcollectdata hostdata = null;
								hostdata = new Pingcollectdata();
								hostdata.setIpaddress(serverip);
								Calendar date = Calendar.getInstance();
								hostdata.setCollecttime(date);
								hostdata.setCategory("INFORMIXPing");
								hostdata.setEntity("Utilization");
								hostdata.setSubentity("ConnectUtilization");
								hostdata.setRestype("dynamic");
								hostdata.setUnit("%");
								hostdata.setThevalue("0");
								try {
									dbdao.createHostData(hostdata);
									//���Ͷ���	
									dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
									dbnode.setAlarm(true);
									List alarmList = dbnode.getAlarmMessage();
									if (alarmList == null)
										alarmList = new ArrayList();
									dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
									dbnode.setStatus(3);
									createSMS("informix", dbmonitorlist);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						} else {
							Pingcollectdata hostdata = null;
							hostdata = new Pingcollectdata();
							hostdata.setIpaddress(serverip);
							Calendar date = Calendar.getInstance();
							hostdata.setCollecttime(date);
							hostdata.setCategory("INFORMIXPing");
							hostdata.setEntity("Utilization");
							hostdata.setSubentity("ConnectUtilization");
							hostdata.setRestype("dynamic");
							hostdata.setUnit("%");
							hostdata.setThevalue("100");
							try {
								dbdao.createHostData(hostdata);
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
						if (allFlag == 0) {
							monitorValue.put("runningflag", "��������");
						} else {
							monitorValue.put("runningflag", "<font color=red>����ֹͣ</font>");
						}

						if (monitorValue != null && monitorValue.size() > 0) {
							ShareData.setInfomixmonitordata(serverip, monitorValue);
						}
					}

					SysLogger.info("end collect informix --------- " + serverip);
				}
			}

			//ȡ��db2�ɼ�
			if (db2list != null) {
				for (int i = 0; i < db2list.size(); i++) {
					Object obj = db2list.get(i);
					DBVo dbmonitorlist = new DBVo();
					BeanUtils.copyProperties(dbmonitorlist, obj);
					DBNode dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
					dbnode.setAlarm(false);
					dbnode.setStatus(0);
					Calendar _tempCal = Calendar.getInstance();
					Date _cc = _tempCal.getTime();
					String _time = sdf.format(_cc);
					dbnode.setLastTime(_time);
					dbnode.getAlarmMessage().clear();

					String serverip = dbmonitorlist.getIpAddress();
					String username = dbmonitorlist.getUser();
					String passwords = dbmonitorlist.getPassword();
					int port = Integer.parseInt(dbmonitorlist.getPort());
					String dbnames = dbmonitorlist.getDbName();
					Date d1 = new Date();
					//�жϸ����ݿ��Ƿ���������
					String[] dbs = dbnames.split(",");
					//SysLogger.info("process db2 ====== "+serverip);
					int allFlag = 0;
					if (dbnode.getCollecttype() == SystemConstant.DBCOLLECTTYPE_SHELL) {
						//	// �ű��ɼ���ʽ
						String filename = ResourceCenter.getInstance().getSysPath() + "/linuxserver/" + serverip + ".db2.log";
						File file = new File(filename);
						if (!file.exists()) {
							// �ļ�������,������澯
							System.out.println("�ļ�������");
							try {
								createFileNotExistSMS(serverip);
							} catch (Exception e) {
								e.printStackTrace();
							}
							continue;
						}
						SysLogger.info("###��ʼ����DB2:" + serverip + "�����ļ�###");
						LoadDB2File load = new LoadDB2File(filename);
						Hashtable db2Data = new Hashtable();
						try {
							db2Data = load.getDB2Init();
						} catch (Exception e) {
							e.printStackTrace();
						}
						boolean db2IsOK = false;
						if (db2Data != null && db2Data.size() > 0) {
							if (db2Data.containsKey("status")) {
								System.out.println(db2Data.get("status"));
								int status = Integer.parseInt((String) db2Data.get("status"));
								if (status == 1)
									db2IsOK = true;
								if (!db2IsOK) {
									dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
									dbnode.setAlarm(true);
									dbnode.setStatus(3);
									// createSMS("db2",dbmonitorlist);
									allFlag = 1;
								}
								if (allFlag == 1) {
									// ��һ�����ݿ��ǲ�ͨ��
									// ��Ҫ�������ݿ����ڵķ������Ƿ�����ͨ
									Host host = (Host) PollingEngine.getInstance().getNodeByIP(serverip);
									Vector ipPingData = (Vector) ShareData.getPingdata().get(serverip);
									if (ipPingData != null) {
										Pingcollectdata pingdata = (Pingcollectdata) ipPingData.get(0);
										Calendar tempCal = (Calendar) pingdata.getCollecttime();
										Date cc = tempCal.getTime();
										String time = sdf.format(cc);
										String lastTime = time;
										String pingvalue = pingdata.getThevalue();
										if (pingvalue == null || pingvalue.trim().length() == 0)
											pingvalue = "0";
										double pvalue = new Double(pingvalue);
										if (pvalue == 0) {
											// �������������Ӳ���***********************************************
											dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
											dbnode.setAlarm(true);
											dbnode.setStatus(3);
											List alarmList = dbnode.getAlarmMessage();
											if (alarmList == null)
												alarmList = new ArrayList();
											dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
											String sysLocation = "";
											try {
												SmscontentDao eventdao = new SmscontentDao();
												String eventdesc = "DB2(" + dbmonitorlist.getDbName() + " IP:"
														+ dbmonitorlist.getIpAddress() + ")" + "�����ݿ����ֹͣ";
												eventdao.createEventWithReasion("poll", dbmonitorlist.getId() + "", dbmonitorlist
														.getAlias()
														+ "(" + dbmonitorlist.getIpAddress() + ")", eventdesc, 3, "db", "ping",
														"���ڵķ��������Ӳ���");
											} catch (Exception e) {
												e.printStackTrace();
											}
										} else {
											Pingcollectdata hostdata = null;
											hostdata = new Pingcollectdata();
											hostdata.setIpaddress(serverip);
											Calendar date = Calendar.getInstance();
											hostdata.setCollecttime(date);
											hostdata.setCategory("DB2Ping");
											hostdata.setEntity("Utilization");
											hostdata.setSubentity("ConnectUtilization");
											hostdata.setRestype("dynamic");
											hostdata.setUnit("%");
											hostdata.setThevalue("0");
											try {
												dbdao.createHostData(hostdata);
												// ���Ͷ���
												dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
												dbnode.setAlarm(true);
												List alarmList = dbnode.getAlarmMessage();
												if (alarmList == null)
													alarmList = new ArrayList();
												dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
												dbnode.setStatus(3);
												createSMS("db2", dbmonitorlist);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}

									} else {
										Pingcollectdata hostdata = null;
										hostdata = new Pingcollectdata();
										hostdata.setIpaddress(serverip);
										Calendar date = Calendar.getInstance();
										hostdata.setCollecttime(date);
										hostdata.setCategory("DB2Ping");
										hostdata.setEntity("Utilization");
										hostdata.setSubentity("ConnectUtilization");
										hostdata.setRestype("dynamic");
										hostdata.setUnit("%");
										hostdata.setThevalue("0");
										try {
											dbdao.createHostData(hostdata);
											// ���Ͷ���
											dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
											dbnode.setAlarm(true);
											List alarmList = dbnode.getAlarmMessage();
											if (alarmList == null)
												alarmList = new ArrayList();
											dbnode.getAlarmMessage().add("���ݿ����ֹͣ");

											dbnode.setStatus(3);
											createSMS("db2", dbmonitorlist);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}

								} else {
									// �������ݿ�����ͨ��
									Pingcollectdata hostdata = null;
									hostdata = new Pingcollectdata();
									hostdata.setIpaddress(serverip);
									Calendar date = Calendar.getInstance();
									hostdata.setCollecttime(date);
									hostdata.setCategory("DB2Ping");
									hostdata.setEntity("Utilization");
									hostdata.setSubentity("ConnectUtilization");
									hostdata.setRestype("dynamic");
									hostdata.setUnit("%");
									hostdata.setThevalue("100");
									try {
										dbdao.createHostData(hostdata);
										if (sendeddata.containsKey("db2" + ":" + serverip))
											sendeddata.remove("db2" + ":" + serverip);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								Hashtable spaceHash = (Hashtable) db2Data.get("spaceInfo");
								String[] alldbs = dbnames.split(",");
								Hashtable alltype6spaceHash = new Hashtable();
								Hashtable type6spaceHash = new Hashtable();
								for (int k = 0; k < alldbs.length; k++) {
									String dbStr = alldbs[k];
									List type6space = new ArrayList();
									if (spaceHash.containsKey(dbStr)) {
										List retList = (List) spaceHash.get(dbStr);
										if (retList != null && retList.size() > 0) {
											for (int j = 0; j < retList.size(); j++) {
												Hashtable sys_hash = (Hashtable) retList.get(j);
												if (sys_hash != null && sys_hash.size() > 0) {
													// if(sys_hash.get("container_type")!=null){
													// ֻ�е�container_type=6��ʱ������ݿ�����û�����ı��С
													// if(sys_hash.get("container_type").toString().equals("6")){
													type6space.add(sys_hash);
													// �жϸ澯
													Db2spaceconfigDao db2spaceconfigManager = new Db2spaceconfigDao();
													Hashtable db2alarm = db2spaceconfigManager.getByAlarmflag(1);
													db2spaceconfigManager.close();
													if (db2alarm != null && db2alarm.size() > 0) {
														if (db2alarm.containsKey(serverip + ":" + dbStr + ":"
																+ sys_hash.get("tablespace_name").toString())) {
															// �ж�ֵ�Ƿ�Խ��
															Db2spaceconfig db2spaceconfig = (Db2spaceconfig) db2alarm
																	.get(serverip + ":" + dbStr + ":"
																			+ sys_hash.get("tablespace_name").toString());
															String usableper = (String) sys_hash.get("usableper");
															if (usableper.trim().length() == 0)
																usableper = "0";
															float usablefloatper = new Float(usableper);

															if (db2spaceconfig.getAlarmvalue() < (100 - new Float(usablefloatper)
																	.intValue())) {
																// �澯
																dbnode = (DBNode) PollingEngine.getInstance().getDbByID(
																		dbmonitorlist.getId());
																dbnode.setAlarm(true);
																dbnode.setStatus(3);
																List alarmList = dbnode.getAlarmMessage();
																if (alarmList == null)
																	alarmList = new ArrayList();
																dbnode.getAlarmMessage().add(
																		db2spaceconfig.getSpacename() + "��ռ䳬����ֵ"
																				+ db2spaceconfig.getAlarmvalue());
																createDb2SpaceSMS(dbmonitorlist, db2spaceconfig);
															}
														}
													}
												}
											}
										}
									}
									if (type6space != null && type6space.size() > 0) {
										// ��typeΪ6�ı�ռ�ӽ�����
										type6spaceHash.put(dbStr, type6space);
									}
								}
								if (type6spaceHash != null && type6spaceHash.size() > 0) {
									alltype6spaceHash.put(serverip, type6spaceHash);
									ShareData.setDb2type6spacedata(serverip, alltype6spaceHash);
								}
								ShareData.setAlldb2data(serverip, db2Data);
							}
						}
						// ////////////////////////////////////////////
					} else {
						//JDBC�ɼ���ʽ
						Hashtable allDb2Data = new Hashtable();
						for (int k = 0; k < dbs.length; k++) {
							//SysLogger.info("begin collect db2--"+dbs[k]+" --------- "+serverip);
							String dbStr = dbs[k];
							boolean db2IsOK = false;
							try {
								db2IsOK = dbdao.getDB2IsOK(serverip, port, dbStr, username, passwords);
							} catch (Exception e) {
								e.printStackTrace();
								db2IsOK = false;
							}
							if (!db2IsOK) {
								dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
								dbnode.setAlarm(true);
								dbnode.setStatus(3);
								//createSMS("db2",dbmonitorlist);
								allFlag = 1;
							}
							//SysLogger.info("end collect db2--"+dbs[k]+" --------- "+serverip);
						}
						if (allFlag == 1) {
							//��һ�����ݿ��ǲ�ͨ��
							//��Ҫ�������ݿ����ڵķ������Ƿ�����ͨ
							Host host = (Host) PollingEngine.getInstance().getNodeByIP(serverip);
							Vector ipPingData = (Vector) ShareData.getPingdata().get(serverip);
							if (ipPingData != null) {
								Pingcollectdata pingdata = (Pingcollectdata) ipPingData.get(0);
								Calendar tempCal = (Calendar) pingdata.getCollecttime();
								Date cc = tempCal.getTime();
								String time = sdf.format(cc);
								String lastTime = time;
								String pingvalue = pingdata.getThevalue();
								if (pingvalue == null || pingvalue.trim().length() == 0)
									pingvalue = "0";
								double pvalue = new Double(pingvalue);
								if (pvalue == 0) {
									//�������������Ӳ���***********************************************
									dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
									dbnode.setAlarm(true);
									dbnode.setStatus(3);
									List alarmList = dbnode.getAlarmMessage();
									if (alarmList == null)
										alarmList = new ArrayList();
									dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
									String sysLocation = "";
									try {
										SmscontentDao eventdao = new SmscontentDao();
										String eventdesc = "DB2(" + dbmonitorlist.getDbName() + " IP:"
												+ dbmonitorlist.getIpAddress() + ")" + "�����ݿ����ֹͣ";
										eventdao.createEventWithReasion("poll", dbmonitorlist.getId() + "", dbmonitorlist
												.getAlias()
												+ "(" + dbmonitorlist.getIpAddress() + ")", eventdesc, 3, "db", "ping",
												"���ڵķ��������Ӳ���");
									} catch (Exception e) {
										e.printStackTrace();
									}
								} else {
									Pingcollectdata hostdata = null;
									hostdata = new Pingcollectdata();
									hostdata.setIpaddress(serverip);
									Calendar date = Calendar.getInstance();
									hostdata.setCollecttime(date);
									hostdata.setCategory("DB2Ping");
									hostdata.setEntity("Utilization");
									hostdata.setSubentity("ConnectUtilization");
									hostdata.setRestype("dynamic");
									hostdata.setUnit("%");
									hostdata.setThevalue("0");
									try {
										dbdao.createHostData(hostdata);
										//���Ͷ���	
										dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
										dbnode.setAlarm(true);
										List alarmList = dbnode.getAlarmMessage();
										if (alarmList == null)
											alarmList = new ArrayList();
										dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
										dbnode.setStatus(3);
										createSMS("db2", dbmonitorlist);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}

							} else {
								Pingcollectdata hostdata = null;
								hostdata = new Pingcollectdata();
								hostdata.setIpaddress(serverip);
								Calendar date = Calendar.getInstance();
								hostdata.setCollecttime(date);
								hostdata.setCategory("DB2Ping");
								hostdata.setEntity("Utilization");
								hostdata.setSubentity("ConnectUtilization");
								hostdata.setRestype("dynamic");
								hostdata.setUnit("%");
								hostdata.setThevalue("0");
								try {
									dbdao.createHostData(hostdata);
									//���Ͷ���	
									dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
									dbnode.setAlarm(true);
									List alarmList = dbnode.getAlarmMessage();
									if (alarmList == null)
										alarmList = new ArrayList();
									dbnode.getAlarmMessage().add("���ݿ����ֹͣ");

									dbnode.setStatus(3);
									createSMS("db2", dbmonitorlist);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

						} else {
							//�������ݿ�����ͨ��
							Pingcollectdata hostdata = null;
							hostdata = new Pingcollectdata();
							hostdata.setIpaddress(serverip);
							Calendar date = Calendar.getInstance();
							hostdata.setCollecttime(date);
							hostdata.setCategory("DB2Ping");
							hostdata.setEntity("Utilization");
							hostdata.setSubentity("ConnectUtilization");
							hostdata.setRestype("dynamic");
							hostdata.setUnit("%");
							hostdata.setThevalue("100");
							try {
								dbdao.createHostData(hostdata);
								if (sendeddata.containsKey("db2" + ":" + serverip))
									sendeddata.remove("db2" + ":" + serverip);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						allDb2Data.put("status", "1");
						// ��DB2���ݽ��вɼ�

						// ��space��Ϣ���вɼ�
						Hashtable spaceHash = dbdao.getDB2Space(serverip, port, dbnames, username, passwords);
						allDb2Data.put("spaceInfo", spaceHash);

						// ��sys��Ϣ���вɼ�
						Hashtable sysHash = dbdao.getDB2Sys(serverip, port, dbnames, username, passwords);
						allDb2Data.put("sysInfo", sysHash);

						// ��pool��Ϣ���вɼ�
						Hashtable poolHash = dbdao.getDB2Pool(serverip, port, dbnames, username, passwords);
						allDb2Data.put("poolInfo", poolHash);

						// ��session��Ϣ���вɼ�
						Hashtable sessionHash = dbdao.getDB2Session(serverip, port, dbnames, username, passwords);
						allDb2Data.put("session", sessionHash);

						String[] alldbs = dbnames.split(",");

						// ��lock��Ϣ���вɼ�
						Hashtable lockInfo = new Hashtable<String, List>();
						for (String db : alldbs) {
							List lockHash = dbdao.getDB2Lock(serverip, port, dbnames, username, passwords);
							lockInfo.put(db, lockHash);
						}
						allDb2Data.put("lock", lockInfo);
						Hashtable alltype6spaceHash = new Hashtable();
						Hashtable type6spaceHash = new Hashtable();
						for (int k = 0; k < alldbs.length; k++) {
							String dbStr = alldbs[k];
							List type6space = new ArrayList();
							if (spaceHash.containsKey(dbStr)) {
								List retList = (List) spaceHash.get(dbStr);
								if (retList != null && retList.size() > 0) {
									for (int j = 0; j < retList.size(); j++) {
										Hashtable sys_hash = (Hashtable) retList.get(j);
										if (sys_hash != null && sys_hash.size() > 0) {
											//if(sys_hash.get("container_type")!=null){
											//ֻ�е�container_type=6��ʱ������ݿ�����û�����ı��С
											//if(sys_hash.get("container_type").toString().equals("6")){
											type6space.add(sys_hash);
											//�жϸ澯
											Db2spaceconfigDao db2spaceconfigManager = new Db2spaceconfigDao();
											Hashtable db2alarm = db2spaceconfigManager.getByAlarmflag(1);
											db2spaceconfigManager.close();
											if (db2alarm != null && db2alarm.size() > 0) {
												if (db2alarm.containsKey(serverip + ":" + dbStr + ":"
														+ sys_hash.get("tablespace_name").toString())) {
													//�ж�ֵ�Ƿ�Խ��
													Db2spaceconfig db2spaceconfig = (Db2spaceconfig) db2alarm.get(serverip + ":"
															+ dbStr + ":" + sys_hash.get("tablespace_name").toString());
													String usableper = (String) sys_hash.get("usableper");
													if (usableper.trim().length() == 0)
														usableper = "0";
													float usablefloatper = new Float(usableper);

													if (db2spaceconfig.getAlarmvalue() < (100 - new Float(usablefloatper)
															.intValue())) {
														//�澯
														dbnode = (DBNode) PollingEngine.getInstance().getDbByID(
																dbmonitorlist.getId());
														dbnode.setAlarm(true);
														dbnode.setStatus(3);
														List alarmList = dbnode.getAlarmMessage();
														if (alarmList == null)
															alarmList = new ArrayList();
														dbnode.getAlarmMessage().add(
																db2spaceconfig.getSpacename() + "��ռ䳬����ֵ"
																		+ db2spaceconfig.getAlarmvalue());
														createDb2SpaceSMS(dbmonitorlist, db2spaceconfig);
													}
												}
											}

											//}
											//}
										}
									}
								}
							}
							if (type6space != null && type6space.size() > 0) {
								//��typeΪ6�ı�ռ�ӽ�����
								type6spaceHash.put(dbStr, type6space);
							}
						}
						if (type6spaceHash != null && type6spaceHash.size() > 0) {
							alltype6spaceHash.put(serverip, type6spaceHash);
							ShareData.setDb2type6spacedata(serverip, alltype6spaceHash);
						}
						ShareData.setAlldb2data(serverip, allDb2Data);
					}

					SysLogger.info("end process db2 ====== " + serverip);
				}
			}

			//ȡ��mysql�ɼ�
			if (mysqllist != null) {
				//SybspaceconfigDao sybspaceconfigManager=new SybspaceconfigDao();
				//Hashtable sybspaceconfig = sybspaceconfigManager.getByAlarmflag(1);
				//sybspaceconfigManager.close();
				for (int i = 0; i < mysqllist.size(); i++) {
					Hashtable monitorValue = new Hashtable();

					Object obj = mysqllist.get(i);

					DBVo dbmonitorlist = new DBVo();
					BeanUtils.copyProperties(dbmonitorlist, obj);
					DBNode dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
					dbnode.setAlarm(false);
					dbnode.setStatus(0);
					Calendar _tempCal = Calendar.getInstance();
					Date _cc = _tempCal.getTime();
					String _time = sdf.format(_cc);
					dbnode.setLastTime(_time);
					dbnode.getAlarmMessage().clear();

					String serverip = dbmonitorlist.getIpAddress();
					String username = dbmonitorlist.getUser();
					String passwords = dbmonitorlist.getPassword();
					int port = Integer.parseInt(dbmonitorlist.getPort());
					String dbnames = dbmonitorlist.getDbName();
					Date d1 = new Date();
					//�жϸ����ݿ��Ƿ���������
					String[] dbs = dbnames.split(",");
					//�жϸ����ݿ��Ƿ���������
					int allFlag = 0;
					boolean mysqlIsOK = false;
					if (dbnode.getCollecttype() == SystemConstant.DBCOLLECTTYPE_SHELL) {
						// �ű��ɼ���ʽ
						System.out.println("-------mysql���ýű���ʽ�ɼ�-----");
						String filename = ResourceCenter.getInstance().getSysPath() + "/linuxserver/" + serverip + ".mysql.log";
						File file = new File(filename);
						if (!file.exists()) {
							// �ļ�������,������澯
							try {
								createFileNotExistSMS(serverip);
							} catch (Exception e) {
								e.printStackTrace();
							}
							continue;
						}
						SysLogger.info("#######################��ʼ����Mysql:" + serverip + "�����ļ�###########");
						LoadMySqlFile loadmysql = new LoadMySqlFile(filename);
						Hashtable mysqlData = new Hashtable();
						try {
							// sqlserverdata = loadsqlserver.getSQLInital();
							mysqlData = loadmysql.getMySqlCongfig();
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (mysqlData != null && mysqlData.size() > 0) {
							System.out.println(mysqlData.containsKey("status"));
							if (mysqlData.containsKey("status")) {
								int status = Integer.parseInt((String) mysqlData.get("status"));
								if (status == 1)
									mysqlIsOK = true;
								if (!mysqlIsOK) {
									dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
									dbnode.setAlarm(true);
									dbnode.setStatus(3);
									createSMS("mysql", dbmonitorlist);
									allFlag = 1;
								} else {
									// �������ϣ���������ݲɼ�
									for (int k = 0; k < dbs.length; k++) {
										String dbStr = dbs[k];
										Hashtable returnValue = new Hashtable();
										returnValue = (Hashtable) mysqlData.get(dbStr);
										if(returnValue!=null)
										   monitorValue.put(dbStr, returnValue);
										System.out.println("------jjjjjjjj---------");
									}
									if (allFlag == 1) {
										// ��һ�����ݿ��ǲ�ͨ��
										// ��Ҫ�������ݿ����ڵķ������Ƿ�����ͨ
										Host host = (Host) PollingEngine.getInstance().getNodeByIP(serverip);
										Vector ipPingData = (Vector) ShareData.getPingdata().get(serverip);
										if (ipPingData != null) {
											Pingcollectdata pingdata = (Pingcollectdata) ipPingData.get(0);
											Calendar tempCal = (Calendar) pingdata.getCollecttime();
											Date cc = tempCal.getTime();
											String time = sdf.format(cc);
											String lastTime = time;
											String pingvalue = pingdata.getThevalue();
											if (pingvalue == null || pingvalue.trim().length() == 0)
												pingvalue = "0";
											double pvalue = new Double(pingvalue);
											if (pvalue == 0) {
												// �������������Ӳ���***********************************************
												dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
												dbnode.setAlarm(true);
												dbnode.setStatus(3);
												List alarmList = dbnode.getAlarmMessage();
												if (alarmList == null)
													alarmList = new ArrayList();
												dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
												String sysLocation = "";
												try {
													SmscontentDao eventdao = new SmscontentDao();
													String eventdesc = "MYSQL(" + dbmonitorlist.getDbName() + " IP:"
															+ dbmonitorlist.getIpAddress() + ")" + "�����ݿ����ֹͣ";
													eventdao.createEventWithReasion("poll", dbmonitorlist.getId() + "",
															dbmonitorlist.getAlias() + "(" + dbmonitorlist.getIpAddress() + ")",
															eventdesc, 3, "db", "ping", "���ڵķ��������Ӳ���");
												} catch (Exception e) {
													e.printStackTrace();
												}
											} else {
												Pingcollectdata hostdata = null;
												hostdata = new Pingcollectdata();
												hostdata.setIpaddress(serverip);
												Calendar date = Calendar.getInstance();
												hostdata.setCollecttime(date);
												hostdata.setCategory("MYPing");
												hostdata.setEntity("Utilization");
												hostdata.setSubentity("ConnectUtilization");
												hostdata.setRestype("dynamic");
												hostdata.setUnit("%");
												hostdata.setThevalue("0");
												try {
													dbdao.createHostData(hostdata);
													// ���Ͷ���
													dbnode = (DBNode) PollingEngine.getInstance()
															.getDbByID(dbmonitorlist.getId());
													dbnode.setAlarm(true);
													List alarmList = dbnode.getAlarmMessage();
													if (alarmList == null)
														alarmList = new ArrayList();
													dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
													dbnode.setStatus(3);
													createSMS("mysql", dbmonitorlist);
												} catch (Exception e) {
													e.printStackTrace();
												}
											}

										} else {
											Pingcollectdata hostdata = null;
											hostdata = new Pingcollectdata();
											hostdata.setIpaddress(serverip);
											Calendar date = Calendar.getInstance();
											hostdata.setCollecttime(date);
											hostdata.setCategory("MYPing");
											hostdata.setEntity("Utilization");
											hostdata.setSubentity("ConnectUtilization");
											hostdata.setRestype("dynamic");
											hostdata.setUnit("%");
											hostdata.setThevalue("0");
											try {
												dbdao.createHostData(hostdata);
												// ���Ͷ���
												dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
												dbnode.setAlarm(true);
												List alarmList = dbnode.getAlarmMessage();
												if (alarmList == null)
													alarmList = new ArrayList();
												dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
												dbnode.setStatus(3);
												createSMS("mysql", dbmonitorlist);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									} else {
										Pingcollectdata hostdata = null;
										hostdata = new Pingcollectdata();
										hostdata.setIpaddress(serverip);
										Calendar date = Calendar.getInstance();
										hostdata.setCollecttime(date);
										hostdata.setCategory("MYPing");
										hostdata.setEntity("Utilization");
										hostdata.setSubentity("ConnectUtilization");
										hostdata.setRestype("dynamic");
										hostdata.setUnit("%");
										hostdata.setThevalue("100");
										try {
											dbdao.createHostData(hostdata);
										} catch (Exception e) {
											e.printStackTrace();
										}

									}
									if (allFlag == 0) {
										// �����ݿ��������ϣ���������ݿ����ݵĲɼ�
										/*
										 * SybaseVO sysbaseVO = new SybaseVO();
										 * try{ sysbaseVO =
										 * dbdao.getSysbaseInfo(serverip,
										 * port,username, passwords);
										 * }catch(Exception e){
										 * e.printStackTrace(); } if (sysbaseVO ==
										 * null)sysbaseVO = new SybaseVO();
										 * Hashtable retValue = new Hashtable();
										 * retValue.put("sysbaseVO", sysbaseVO);
										 * ShareData.setSysbasedata(serverip,
										 * retValue); List allspace =
										 * sysbaseVO.getDbInfo(); if(allspace !=
										 * null && allspace.size()>0){ for(int
										 * k=0;k<allspace.size();k++){ TablesVO
										 * tvo = (TablesVO)allspace.get(k);
										 * if(sybspaceconfig
										 * .containsKey(serverip
										 * +":"+tvo.getDb_name())){ //�澯�ж�
										 * Sybspaceconfig sybconfig =
										 * (Sybspaceconfig
										 * )sybspaceconfig.get(serverip
										 * +":"+tvo.getDb_name()); Integer
										 * usedperc =
										 * Integer.parseInt(tvo.getDb_usedperc
										 * ());
										 * if(usedperc>sybconfig.getAlarmvalue
										 * ()){ //������ֵ�澯 dbnode =
										 * (DBNode)PollingEngine
										 * .getInstance().getDbByID
										 * (dbmonitorlist.getId());
										 * dbnode.setAlarm(true); List alarmList =
										 * dbnode.getAlarmMessage();
										 * if(alarmList == null)alarmList = new
										 * ArrayList();
										 * dbnode.getAlarmMessage().
										 * add(sybconfig
										 * .getSpacename()+"��ռ䳬����ֵ"
										 * +sybconfig.getAlarmvalue());
										 * //dbnode.setStatus(3);
										 * createSybSpaceSMS
										 * (dbmonitorlist,sybconfig); } } } }
										 */
									}
									if (allFlag == 0) {
										monitorValue.put("runningflag", "��������");
									} else {
										monitorValue.put("runningflag", "<font color=red>����ֹͣ</font>");
									}

									if (monitorValue != null && monitorValue.size() > 0) {
										ShareData.setMySqlmonitordata(serverip, monitorValue);
									}
								}
							}
						}
						// //////////////////////////////////////////
					} else {
						//JDBC�ɼ���ʽ
						for (int k = 0; k < dbs.length; k++) {
							SysLogger.info("begin collect mysql--" + dbs[k] + " --------- " + serverip);
							String dbStr = dbs[k];

							try {
								mysqlIsOK = dbdao.getMySqlIsOk(serverip, username, passwords, dbStr);
							} catch (Exception e) {
								e.printStackTrace();
								mysqlIsOK = false;
							}
							if (!mysqlIsOK) {
								dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
								dbnode.setAlarm(true);
								dbnode.setStatus(3);
								createSMS("mysql", dbmonitorlist);
								allFlag = 1;
							} else {
								//�������ϣ���������ݲɼ�
								Hashtable returnValue = new Hashtable();
								try {
									returnValue = dbdao.getMySqlDBConfig(serverip, username, passwords, dbStr);
									Vector vector = dbdao.getStatus(serverip, username, passwords, dbStr);
									Vector vector1 = dbdao.getVariables(serverip, username, passwords, dbStr);
									returnValue.put("variables", vector);
									returnValue.put("global_status", vector1);
									Vector dispose = dbdao.getDispose(serverip, username, passwords, dbStr);
									Vector dispose1 = dbdao.getDispose1(serverip, username, passwords, dbStr);
									Vector dispose2 = dbdao.getDispose2(serverip, username, passwords, dbStr);
									Vector dispose3 = dbdao.getDispose3(serverip, username, passwords, dbStr);
									returnValue.put("dispose", dispose);
									returnValue.put("dispose1", dispose1);
									returnValue.put("dispose2", dispose2);
									returnValue.put("dispose3", dispose3);
								} catch (Exception e) {
									e.printStackTrace();
								}
								monitorValue.put(dbStr, returnValue);

							}
							SysLogger.info("end collect mysql--" + dbs[k] + " --------- " + serverip);
						}
						if (allFlag == 1) {
							//��һ�����ݿ��ǲ�ͨ��
							//��Ҫ�������ݿ����ڵķ������Ƿ�����ͨ
							Host host = (Host) PollingEngine.getInstance().getNodeByIP(serverip);
							Vector ipPingData = (Vector) ShareData.getPingdata().get(serverip);
							if (ipPingData != null) {
								Pingcollectdata pingdata = (Pingcollectdata) ipPingData.get(0);
								Calendar tempCal = (Calendar) pingdata.getCollecttime();
								Date cc = tempCal.getTime();
								String time = sdf.format(cc);
								String lastTime = time;
								String pingvalue = pingdata.getThevalue();
								if (pingvalue == null || pingvalue.trim().length() == 0)
									pingvalue = "0";
								double pvalue = new Double(pingvalue);
								if (pvalue == 0) {
									//�������������Ӳ���***********************************************
									dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
									dbnode.setAlarm(true);
									dbnode.setStatus(3);
									List alarmList = dbnode.getAlarmMessage();
									if (alarmList == null)
										alarmList = new ArrayList();
									dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
									String sysLocation = "";
									try {
										SmscontentDao eventdao = new SmscontentDao();
										String eventdesc = "MYSQL(" + dbmonitorlist.getDbName() + " IP:"
												+ dbmonitorlist.getIpAddress() + ")" + "�����ݿ����ֹͣ";
										eventdao.createEventWithReasion("poll", dbmonitorlist.getId() + "", dbmonitorlist
												.getAlias()
												+ "(" + dbmonitorlist.getIpAddress() + ")", eventdesc, 3, "db", "ping",
												"���ڵķ��������Ӳ���");
									} catch (Exception e) {
										e.printStackTrace();
									}
								} else {
									Pingcollectdata hostdata = null;
									hostdata = new Pingcollectdata();
									hostdata.setIpaddress(serverip);
									Calendar date = Calendar.getInstance();
									hostdata.setCollecttime(date);
									hostdata.setCategory("MYPing");
									hostdata.setEntity("Utilization");
									hostdata.setSubentity("ConnectUtilization");
									hostdata.setRestype("dynamic");
									hostdata.setUnit("%");
									hostdata.setThevalue("0");
									try {
										dbdao.createHostData(hostdata);
										//���Ͷ���	
										dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
										dbnode.setAlarm(true);
										List alarmList = dbnode.getAlarmMessage();
										if (alarmList == null)
											alarmList = new ArrayList();
										dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
										dbnode.setStatus(3);
										createSMS("mysql", dbmonitorlist);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}

							} else {
								Pingcollectdata hostdata = null;
								hostdata = new Pingcollectdata();
								hostdata.setIpaddress(serverip);
								Calendar date = Calendar.getInstance();
								hostdata.setCollecttime(date);
								hostdata.setCategory("MYPing");
								hostdata.setEntity("Utilization");
								hostdata.setSubentity("ConnectUtilization");
								hostdata.setRestype("dynamic");
								hostdata.setUnit("%");
								hostdata.setThevalue("0");
								try {
									dbdao.createHostData(hostdata);
									//���Ͷ���	
									dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
									dbnode.setAlarm(true);
									List alarmList = dbnode.getAlarmMessage();
									if (alarmList == null)
										alarmList = new ArrayList();
									dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
									dbnode.setStatus(3);
									createSMS("mysql", dbmonitorlist);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						} else {
							Pingcollectdata hostdata = null;
							hostdata = new Pingcollectdata();
							hostdata.setIpaddress(serverip);
							Calendar date = Calendar.getInstance();
							hostdata.setCollecttime(date);
							hostdata.setCategory("MYPing");
							hostdata.setEntity("Utilization");
							hostdata.setSubentity("ConnectUtilization");
							hostdata.setRestype("dynamic");
							hostdata.setUnit("%");
							hostdata.setThevalue("100");
							try {
								dbdao.createHostData(hostdata);
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
						if (allFlag == 0) {
							//�����ݿ��������ϣ���������ݿ����ݵĲɼ�
							/*
							SybaseVO sysbaseVO = new SybaseVO();
							try{
								sysbaseVO = dbdao.getSysbaseInfo(serverip, port,username, passwords);
							}catch(Exception e){
								e.printStackTrace();
							}
							if (sysbaseVO == null)sysbaseVO = new SybaseVO();
							Hashtable retValue = new Hashtable();
							retValue.put("sysbaseVO", sysbaseVO);
							ShareData.setSysbasedata(serverip, retValue);
							List allspace = sysbaseVO.getDbInfo();
							if(allspace != null && allspace.size()>0){						
								for(int k=0;k<allspace.size();k++){
									TablesVO tvo = (TablesVO)allspace.get(k); 
									if(sybspaceconfig.containsKey(serverip+":"+tvo.getDb_name())){
										//�澯�ж�
										Sybspaceconfig sybconfig = (Sybspaceconfig)sybspaceconfig.get(serverip+":"+tvo.getDb_name());
										Integer usedperc = Integer.parseInt(tvo.getDb_usedperc());
										if(usedperc>sybconfig.getAlarmvalue()){
											//������ֵ�澯
											dbnode = (DBNode)PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
											dbnode.setAlarm(true);
											List alarmList = dbnode.getAlarmMessage();
											if(alarmList == null)alarmList = new ArrayList();
											dbnode.getAlarmMessage().add(sybconfig.getSpacename()+"��ռ䳬����ֵ"+sybconfig.getAlarmvalue());
											//dbnode.setStatus(3);
											createSybSpaceSMS(dbmonitorlist,sybconfig);
										}
									}
								}
							}
							 */
						}
						if (allFlag == 0) {
							monitorValue.put("runningflag", "��������");
						} else {
							monitorValue.put("runningflag", "<font color=red>����ֹͣ</font>");
						}

						if (monitorValue != null && monitorValue.size() > 0) {
							ShareData.setMySqlmonitordata(serverip, monitorValue);
						}
					}

					SysLogger.info("end collect mysql --------- " + serverip);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbdao.close();
			System.out.println("********DB Thread Count : " + Thread.activeCount());
		}
	}
	
	/*--------modify   zhao -------------------------*/
	public void createSMS(String db, DBVo dbmonitorlist, OracleEntity oracle) {
		// ��������
		// ���ڴ����õ�ǰ���IP��PING��ֵ
		Calendar date = Calendar.getInstance();
		// for (OracleEntity oracle : oracles) {
		try {
			if (!sendeddata.containsKey(db + ":" + dbmonitorlist.getIpAddress() + ":" + oracle.getSid())) {
				// �����ڣ��������ţ�������ӵ������б���
				Smscontent smscontent = new Smscontent();
				String time = sdf.format(date.getTime());
				smscontent.setLevel("2");
				smscontent.setObjid(dbmonitorlist.getId() + ":oracle" + oracle.getId());
				smscontent.setMessage(db + "(" + oracle.getSid() + " IP:" + dbmonitorlist.getIpAddress() + ")" + "�����ݿ����ֹͣ");
				smscontent.setRecordtime(time);
				smscontent.setSubtype("db");
				smscontent.setSubentity("ping");
				smscontent.setIp(dbmonitorlist.getIpAddress() + ":" + oracle.getSid());
				// ���Ͷ���
				SmscontentDao smsmanager = new SmscontentDao();
				try {
					smsmanager.sendDatabaseSmscontent(smscontent);
				} catch (Exception e) {
					e.printStackTrace();
				}
				sendeddata.put(db + ":" + dbmonitorlist.getIpAddress() + ":" + oracle.getSid(), date);
			} else {
				// ���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
				Calendar formerdate = (Calendar) sendeddata.get(db + ":" + dbmonitorlist.getIpAddress() + ":" + oracle.getSid());
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date last = null;
				Date current = null;
				Calendar sendcalen = formerdate;
				Date cc = sendcalen.getTime();
				String tempsenddate = formatter.format(cc);

				Calendar currentcalen = date;
				cc = currentcalen.getTime();
				last = formatter.parse(tempsenddate);
				String currentsenddate = formatter.format(cc);
				current = formatter.parse(currentsenddate);

				long subvalue = current.getTime() - last.getTime();
				if (subvalue / (1000 * 60 * 60 * 24) >= 1) {
					// ����һ�죬���ٷ���Ϣ
					Smscontent smscontent = new Smscontent();
					String time = sdf.format(date.getTime());
					smscontent.setLevel("2");
					smscontent.setObjid(dbmonitorlist.getId() + "");
					smscontent.setMessage(db + "(" + dbmonitorlist.getDbName() + " IP:" + dbmonitorlist.getIpAddress() + ")"
							+ "�����ݿ����ֹͣ");
					smscontent.setRecordtime(time);
					smscontent.setSubtype("db");
					smscontent.setSubentity("ping");
					smscontent.setIp(dbmonitorlist.getIpAddress() + ":" + oracle.getSid());
					// smscontent.setMessage("db&"+time+"&"+dbmonitorlist.getId()+"&"+db+"("+dbmonitorlist.getDbName()+"
					// IP:"+dbmonitorlist.getIpAddress()+")"+"�����ݿ����ֹͣ");
					// ���Ͷ���
					SmscontentDao smsmanager = new SmscontentDao();
					try {
						smsmanager.sendDatabaseSmscontent(smscontent);
					} catch (Throwable e) {

					}
					// �޸��Ѿ����͵Ķ��ż�¼
					sendeddata.put(db + ":" + dbmonitorlist.getIpAddress() + ":" + oracle.getSid(), date);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// }

	}

	public void createSMS(String db, DBVo dbmonitorlist) {
		//��������		 	
		//���ڴ����õ�ǰ���IP��PING��ֵ
		Calendar date = Calendar.getInstance();
		try {
			if (!sendeddata.containsKey(db + ":" + dbmonitorlist.getIpAddress())) {
				//�����ڣ��������ţ�������ӵ������б���
				Smscontent smscontent = new Smscontent();
				String time = sdf.format(date.getTime());
				smscontent.setLevel("2");
				smscontent.setObjid(dbmonitorlist.getId() + "");
				smscontent.setMessage(db + "(" + dbmonitorlist.getDbName() + " IP:" + dbmonitorlist.getIpAddress() + ")"
						+ "�����ݿ����ֹͣ");
				smscontent.setRecordtime(time);
				smscontent.setSubtype("db");
				smscontent.setSubentity("ping");
				smscontent.setIp(dbmonitorlist.getIpAddress());
				//���Ͷ���
				SmscontentDao smsmanager = new SmscontentDao();
				try {
					smsmanager.sendDatabaseSmscontent(smscontent);
				} catch (Exception e) {

				}
				sendeddata.put(db + ":" + dbmonitorlist.getIpAddress(), date);
			} else {
				//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
				Calendar formerdate = (Calendar) sendeddata.get(db + ":" + dbmonitorlist.getIpAddress());
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date last = null;
				Date current = null;
				Calendar sendcalen = formerdate;
				Date cc = sendcalen.getTime();
				String tempsenddate = formatter.format(cc);

				Calendar currentcalen = date;
				cc = currentcalen.getTime();
				last = formatter.parse(tempsenddate);
				String currentsenddate = formatter.format(cc);
				current = formatter.parse(currentsenddate);

				long subvalue = current.getTime() - last.getTime();
				if (subvalue / (1000 * 60 * 60 * 24) >= 1) {
					//����һ�죬���ٷ���Ϣ
					Smscontent smscontent = new Smscontent();
					String time = sdf.format(date.getTime());
					smscontent.setLevel("2");
					smscontent.setObjid(dbmonitorlist.getId() + "");
					smscontent.setMessage(db + "(" + dbmonitorlist.getDbName() + " IP:" + dbmonitorlist.getIpAddress() + ")"
							+ "�����ݿ����ֹͣ");
					smscontent.setRecordtime(time);
					smscontent.setSubtype("db");
					smscontent.setSubentity("ping");
					smscontent.setIp(dbmonitorlist.getIpAddress());
					//smscontent.setMessage("db&"+time+"&"+dbmonitorlist.getId()+"&"+db+"("+dbmonitorlist.getDbName()+" IP:"+dbmonitorlist.getIpAddress()+")"+"�����ݿ����ֹͣ");
					//���Ͷ���
					SmscontentDao smsmanager = new SmscontentDao();
					try {
						smsmanager.sendDatabaseSmscontent(smscontent);
					} catch (Exception e) {

					}
					//�޸��Ѿ����͵Ķ��ż�¼	
					sendeddata.put(db + ":" + dbmonitorlist.getIpAddress(), date);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createSpaceSMS(DBVo dbmonitorlist, Oraspaceconfig oraspaceconfig, OracleEntity oracle) {
		// ��������
		// ���ڴ����õ�ǰ���IP��PING��ֵ
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SmscontentDao smsmanager = new SmscontentDao();
		AlarmInfoDao alarminfomanager = new AlarmInfoDao();

		String ipaddress = dbmonitorlist.getIpAddress();
		Hashtable sendeddata = ShareData.getSendeddata();
		Calendar date = Calendar.getInstance();
		String time = sdf.format(date.getTime());
		// String
		// errorcontent="oraspace&"+time+"&"+dbmonitorlist.getId()+"&"+oraspaceconfig.getSpacename()+"("+dbmonitorlist.getDbName()+"
		// IP:"+dbmonitorlist.getIpAddress()+")"+"�ı�ռ䳬����ֵ";
		try {
			if (!sendeddata.containsKey(ipaddress + ":" + oracle.getId() + ":" + oraspaceconfig.getSpacename())) {
				// �����ڣ��������ţ�������ӵ������б���
				Smscontent smscontent = new Smscontent();
				smscontent.setLevel("2");
				smscontent.setObjid(dbmonitorlist.getId() + ":oracle" + oracle.getId());
				smscontent.setMessage(dbmonitorlist.getIpAddress() + ":" + oracle.getSid() + "�����ݿ��"
						+ oraspaceconfig.getSpacename() + "��ռ䳬��" + oraspaceconfig.getAlarmvalue() + "%�ķ�ֵ");
				smscontent.setRecordtime(time);
				smscontent.setSubtype("db");
				smscontent.setSubentity("oraspace");
				smscontent.setIp(dbmonitorlist.getIpAddress() + ":" + oracle.getSid());
				// smscontent.setMessage(errorcontent);
				// ���Ͷ���
				try {
					smsmanager.sendDatabaseSmscontent(smscontent);
				} catch (Exception e) {

				}
				sendeddata.put(ipaddress + ":" + oracle.getId() + ":" + oraspaceconfig.getSpacename(), date);
			} else {
				// ���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
				Calendar formerdate = (Calendar) sendeddata.get(ipaddress + ":" + oracle.getId() + ":"
						+ oraspaceconfig.getSpacename());
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date last = null;
				Date current = null;
				Calendar sendcalen = formerdate;
				Date cc = sendcalen.getTime();
				String tempsenddate = formatter.format(cc);

				Calendar currentcalen = date;
				cc = currentcalen.getTime();
				last = formatter.parse(tempsenddate);
				String currentsenddate = formatter.format(cc);
				current = formatter.parse(currentsenddate);

				long subvalue = current.getTime() - last.getTime();
				if (subvalue / (1000 * 60 * 60 * 24) >= 1) {
					// ����һ�죬���ٷ���Ϣ
					Smscontent smscontent = new Smscontent();
					smscontent.setLevel("2");
					smscontent.setObjid(dbmonitorlist.getId() + ":oracle" + oracle.getId());
					smscontent.setMessage(dbmonitorlist.getIpAddress() + "�����ݿ��" + oraspaceconfig.getSpacename() + "��ռ䳬��"
							+ oraspaceconfig.getAlarmvalue() + "%�ķ�ֵ");
					smscontent.setRecordtime(time);
					smscontent.setSubtype("db");
					smscontent.setSubentity("oraspace");
					smscontent.setIp(dbmonitorlist.getIpAddress() + ":" + oracle.getId());
					// ���Ͷ���
					try {
						smsmanager.sendDatabaseSmscontent(smscontent);
					} catch (Exception e) {

					}
					sendeddata.put(ipaddress + ":" + oracle.getId() + ":" + oraspaceconfig.getSpacename(), date);
				} else {
					// ��д�����澯����
					// �������澯����д����
					AlarmInfo alarminfo = new AlarmInfo();
					alarminfo.setContent(dbmonitorlist.getIpAddress() + "�����ݿ��" + oraspaceconfig.getSpacename() + "��ռ䳬��"
							+ oraspaceconfig.getAlarmvalue() + "%�ķ�ֵ");
					alarminfo.setIpaddress(ipaddress);
					alarminfo.setLevel1(new Integer(2));
					alarminfo.setRecordtime(Calendar.getInstance());
					alarminfomanager.save(alarminfo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			alarminfomanager.close();
		}
	}

	public static void createInformixSpaceSMS(DBVo dbmonitorlist, Informixspaceconfig informixspaceconfig) {
		//��������		 	
		//���ڴ����õ�ǰ���IP��PING��ֵ
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SmscontentDao smsmanager = new SmscontentDao();
		AlarmInfoDao alarminfomanager = new AlarmInfoDao();

		String ipaddress = dbmonitorlist.getIpAddress();
		Hashtable sendeddata = ShareData.getSendeddata();
		Calendar date = Calendar.getInstance();
		String time = sdf.format(date.getTime());
		try {
			if (!sendeddata.containsKey(ipaddress + ":" + informixspaceconfig.getSpacename())) {
				//�����ڣ��������ţ�������ӵ������б���
				Smscontent smscontent = new Smscontent();
				smscontent.setLevel("2");
				smscontent.setObjid(dbmonitorlist.getId() + "");
				smscontent.setMessage(dbmonitorlist.getIpAddress() + "�����ݿ��" + informixspaceconfig.getSpacename() + "��ռ䳬��"
						+ informixspaceconfig.getAlarmvalue() + "%�ķ�ֵ");
				smscontent.setRecordtime(time);
				smscontent.setSubtype("db");
				smscontent.setSubentity("informixspace");
				smscontent.setIp(dbmonitorlist.getIpAddress());
				//���Ͷ���
				try {
					SysLogger.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
					smsmanager.sendDatabaseSmscontent(smscontent);
				} catch (Exception e) {

				}
				sendeddata.put(ipaddress + ":" + informixspaceconfig.getSpacename(), date);
			} else {
				//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
				Calendar formerdate = (Calendar) sendeddata.get(ipaddress + ":" + informixspaceconfig.getSpacename());
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date last = null;
				Date current = null;
				Calendar sendcalen = formerdate;
				Date cc = sendcalen.getTime();
				String tempsenddate = formatter.format(cc);

				Calendar currentcalen = date;
				cc = currentcalen.getTime();
				last = formatter.parse(tempsenddate);
				String currentsenddate = formatter.format(cc);
				current = formatter.parse(currentsenddate);

				long subvalue = current.getTime() - last.getTime();
				if (subvalue / (1000 * 60 * 60 * 24) >= 1) {
					//����һ�죬���ٷ���Ϣ
					Smscontent smscontent = new Smscontent();
					smscontent.setLevel("2");
					smscontent.setObjid(dbmonitorlist.getId() + "");
					smscontent.setMessage(dbmonitorlist.getIpAddress() + "�����ݿ��" + informixspaceconfig.getSpacename() + "��ռ䳬��"
							+ informixspaceconfig.getAlarmvalue() + "%�ķ�ֵ");
					smscontent.setRecordtime(time);
					smscontent.setSubtype("db");
					smscontent.setSubentity("informixspace");
					smscontent.setIp(dbmonitorlist.getIpAddress());
					//���Ͷ���
					try {
						smsmanager.sendDatabaseSmscontent(smscontent);
					} catch (Exception e) {

					}
					sendeddata.put(ipaddress + ":" + informixspaceconfig.getSpacename(), date);
				} else {
					//��д�����澯����
					//�������澯����д����
					AlarmInfo alarminfo = new AlarmInfo();
					alarminfo.setContent(dbmonitorlist.getIpAddress() + "�����ݿ��" + informixspaceconfig.getSpacename() + "��ռ䳬��"
							+ informixspaceconfig.getAlarmvalue() + "%�ķ�ֵ");
					alarminfo.setIpaddress(ipaddress);
					alarminfo.setLevel1(new Integer(2));
					alarminfo.setRecordtime(Calendar.getInstance());
					alarminfomanager.save(alarminfo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			alarminfomanager.close();
		}
	}

	public static void createSqldbSMS(DBVo dbmonitorlist, Sqldbconfig sqldbconfig) {
		//��������		 	
		//���ڴ����õ�ǰ���IP��PING��ֵ
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SmscontentDao smsmanager = new SmscontentDao();
		AlarmInfoDao alarminfomanager = new AlarmInfoDao();

		String ipaddress = dbmonitorlist.getIpAddress();
		Hashtable sendeddata = ShareData.getSendeddata();
		Calendar date = Calendar.getInstance();
		String time = sdf.format(date.getTime());
		String errorcontent = "";
		if (sqldbconfig.getLogflag() == 0) {
			//���ļ�
			errorcontent = dbmonitorlist.getIpAddress() + "��" + dbmonitorlist.getDbName() + "��" + sqldbconfig.getDbname()
					+ "�Ŀ�ռ䳬��" + sqldbconfig.getAlarmvalue() + "%�ķ�ֵ";
		} else {
			//��־�ļ�
			errorcontent = dbmonitorlist.getIpAddress() + "��" + dbmonitorlist.getDbName() + "��" + sqldbconfig.getDbname()
					+ "����־����" + sqldbconfig.getAlarmvalue() + "%�ķ�ֵ";
		}

		try {
			if (!sendeddata.containsKey(ipaddress + ":" + sqldbconfig.getDbname() + ":" + sqldbconfig.getLogflag())) {
				//�����ڣ��������ţ�������ӵ������б���
				Smscontent smscontent = new Smscontent();
				//String time1 = sdf.format(date.getTime());
				smscontent.setLevel("2");
				smscontent.setObjid(dbmonitorlist.getId() + "");
				smscontent.setMessage(errorcontent);
				smscontent.setRecordtime(time);
				smscontent.setSubtype("db");
				smscontent.setSubentity("sqldb");
				smscontent.setIp(dbmonitorlist.getIpAddress());
				//���Ͷ���
				try {
					smsmanager.sendURLSmscontent(smscontent);
				} catch (Exception e) {

				}
				sendeddata.put(ipaddress + ":" + sqldbconfig.getDbname() + ":" + sqldbconfig.getLogflag(), date);
			} else {
				//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
				Calendar formerdate = (Calendar) sendeddata.get(ipaddress + ":" + sqldbconfig.getDbname() + ":"
						+ sqldbconfig.getLogflag());
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date last = null;
				Date current = null;
				Calendar sendcalen = formerdate;
				Date cc = sendcalen.getTime();
				String tempsenddate = formatter.format(cc);

				Calendar currentcalen = date;
				cc = currentcalen.getTime();
				last = formatter.parse(tempsenddate);
				String currentsenddate = formatter.format(cc);
				current = formatter.parse(currentsenddate);

				long subvalue = current.getTime() - last.getTime();
				if (subvalue / (1000 * 60 * 60 * 24) >= 1) {
					//����һ�죬���ٷ���Ϣ
					Smscontent smscontent = new Smscontent();
					//String time1 = sdf.format(date.getTime());
					smscontent.setLevel("2");
					smscontent.setObjid(dbmonitorlist.getId() + "");
					smscontent.setMessage(errorcontent);
					smscontent.setRecordtime(time);
					smscontent.setSubtype("db");
					smscontent.setSubentity("sqldb");
					smscontent.setIp(dbmonitorlist.getIpAddress());
					//���Ͷ���
					try {
						smsmanager.sendURLSmscontent(smscontent);
					} catch (Exception e) {

					}
					//�޸��Ѿ����͵Ķ��ż�¼	
					sendeddata.put(ipaddress + ":" + sqldbconfig.getDbname() + ":" + sqldbconfig.getLogflag(), date);
				} else {
					//��д�����澯����
					//�������澯����д����
					AlarmInfo alarminfo = new AlarmInfo();
					alarminfo.setContent(errorcontent);
					alarminfo.setIpaddress(ipaddress);
					alarminfo.setLevel1(new Integer(2));
					alarminfo.setRecordtime(Calendar.getInstance());
					alarminfomanager.save(alarminfo);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createDb2SpaceSMS(DBVo dbmonitorlist, Db2spaceconfig db2spaceconfig) {
		//��������		 	
		//���ڴ����õ�ǰ���IP��PING��ֵ
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SmscontentDao smsmanager = new SmscontentDao();
		AlarmInfoDao alarminfomanager = new AlarmInfoDao();

		String ipaddress = dbmonitorlist.getIpAddress();
		Hashtable sendeddata = ShareData.getSendeddata();
		//EventListDao eventmanager=new EventListDao();
		Calendar date = Calendar.getInstance();
		String time = sdf.format(date.getTime());
		String errorcontent = dbmonitorlist.getIpAddress() + "��" + db2spaceconfig.getDbname() + "��"
				+ db2spaceconfig.getSpacename() + "�ı�ռ䳬��" + db2spaceconfig.getAlarmvalue() + "%��ֵ";
		try {
			if (!sendeddata.containsKey(ipaddress + ":" + db2spaceconfig.getDbname() + ":" + db2spaceconfig.getSpacename())) {
				//�����ڣ��������ţ�������ӵ������б���
				Smscontent smscontent = new Smscontent();
				smscontent.setLevel("2");
				smscontent.setObjid(dbmonitorlist.getId() + "");
				smscontent.setMessage(errorcontent);
				smscontent.setRecordtime(time);
				smscontent.setSubtype("db");
				smscontent.setSubentity("db2space");
				smscontent.setIp(dbmonitorlist.getIpAddress());
				//���Ͷ���
				try {
					smsmanager.sendURLSmscontent(smscontent);
				} catch (Exception e) {

				}
				sendeddata.put(ipaddress + ":" + db2spaceconfig.getDbname() + ":" + db2spaceconfig.getSpacename(), date);
			} else {
				//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
				Calendar formerdate = (Calendar) sendeddata.get(ipaddress + ":" + db2spaceconfig.getDbname() + ":"
						+ db2spaceconfig.getSpacename());
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date last = null;
				Date current = null;
				Calendar sendcalen = formerdate;
				Date cc = sendcalen.getTime();
				String tempsenddate = formatter.format(cc);

				Calendar currentcalen = date;
				cc = currentcalen.getTime();
				last = formatter.parse(tempsenddate);
				String currentsenddate = formatter.format(cc);
				current = formatter.parse(currentsenddate);

				long subvalue = current.getTime() - last.getTime();
				if (subvalue / (1000 * 60 * 60 * 24) >= 1) {
					//����һ�죬���ٷ���Ϣ
					Smscontent smscontent = new Smscontent();
					smscontent.setLevel("2");
					smscontent.setObjid(dbmonitorlist.getId() + "");
					smscontent.setMessage(errorcontent);
					smscontent.setRecordtime(time);
					smscontent.setSubtype("db");
					smscontent.setSubentity("db2space");
					smscontent.setIp(dbmonitorlist.getIpAddress());
					//���Ͷ���
					try {
						smsmanager.sendURLSmscontent(smscontent);
					} catch (Exception e) {

					}
					//�޸��Ѿ����͵Ķ��ż�¼	
					sendeddata.put(ipaddress + ":" + db2spaceconfig.getDbname() + ":" + db2spaceconfig.getSpacename(), date);
				} else {
					//��д�����澯����
					//�������澯����д����
					AlarmInfo alarminfo = new AlarmInfo();
					alarminfo.setContent(errorcontent);
					alarminfo.setIpaddress(ipaddress);
					alarminfo.setLevel1(new Integer(2));
					alarminfo.setRecordtime(Calendar.getInstance());
					alarminfomanager.save(alarminfo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	public static void createSybSpaceSMS(DBVo dbvo, Sybspaceconfig sybconfig) {
		//��������		 	
		//���ڴ����õ�ǰ���IP��PING��ֵ
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SmscontentDao smsmanager = new SmscontentDao();
		AlarmInfoDao alarminfomanager = new AlarmInfoDao();

		String ipaddress = dbvo.getIpAddress();
		Hashtable sendeddata = ShareData.getSendeddata();
		//I_Eventlist eventmanager=new EventlistManager();
		Calendar date = Calendar.getInstance();
		String time = sdf.format(date.getTime());
		String errorcontent = dbvo.getIpAddress() + "��" + dbvo.getDbName() + "��" + sybconfig.getSpacename() + "�ı�ռ䳬��"
				+ sybconfig.getAlarmvalue() + "%��ֵ";
		try {
			if (!sendeddata.containsKey(ipaddress + ":" + sybconfig.getSpacename())) {
				//�����ڣ��������ţ�������ӵ������б���
				Smscontent smscontent = new Smscontent();
				//String time1 = sdf.format(date.getTime());
				smscontent.setLevel("2");
				smscontent.setObjid(dbvo.getId() + "");
				smscontent.setMessage(errorcontent);
				smscontent.setRecordtime(time);
				smscontent.setSubtype("db");
				smscontent.setSubentity("sybspace");
				smscontent.setIp(dbvo.getIpAddress());
				//���Ͷ���
				try {
					smsmanager.sendURLSmscontent(smscontent);
				} catch (Exception e) {

				}
				sendeddata.put(ipaddress + ":" + sybconfig.getSpacename(), date);
			} else {
				//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
				Calendar formerdate = (Calendar) sendeddata.get(ipaddress + ":" + sybconfig.getSpacename());
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date last = null;
				Date current = null;
				Calendar sendcalen = formerdate;
				Date cc = sendcalen.getTime();
				String tempsenddate = formatter.format(cc);

				Calendar currentcalen = date;
				cc = currentcalen.getTime();
				last = formatter.parse(tempsenddate);
				String currentsenddate = formatter.format(cc);
				current = formatter.parse(currentsenddate);

				long subvalue = current.getTime() - last.getTime();
				if (subvalue / (1000 * 60 * 60 * 24) >= 1) {
					//����һ�죬���ٷ���Ϣ
					Smscontent smscontent = new Smscontent();
					smscontent.setLevel("2");
					smscontent.setObjid(dbvo.getId() + "");
					smscontent.setMessage(errorcontent);
					smscontent.setRecordtime(time);
					smscontent.setSubtype("db");
					smscontent.setSubentity("sybspace");
					smscontent.setIp(dbvo.getIpAddress());
					//���Ͷ���
					try {
						smsmanager.sendURLSmscontent(smscontent);
					} catch (Exception e) {

					}
					//�޸��Ѿ����͵Ķ��ż�¼	
					sendeddata.put(ipaddress + ":" + sybconfig.getSpacename(), date);
				} else {
					//��д�����澯����
					//�������澯����д����
					AlarmInfo alarminfo = new AlarmInfo();
					alarminfo.setContent(errorcontent);
					alarminfo.setIpaddress(ipaddress);
					alarminfo.setLevel1(new Integer(2));
					alarminfo.setRecordtime(Calendar.getInstance());
					alarminfomanager.save(alarminfo);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createFileNotExistSMS(String ipaddress) {
		//��������		 	
		//���ڴ����õ�ǰ���IP��PING��ֵ
		Calendar date = Calendar.getInstance();
		try {
			Host host = (Host) PollingEngine.getInstance().getNodeByIP(ipaddress);
			if (host == null)
				return;

			if (!sendeddata.containsKey(ipaddress + ":file:" + host.getId())) {
				//�����ڣ��������ţ�������ӵ������б���
				Smscontent smscontent = new Smscontent();
				String time = sdf.format(date.getTime());
				smscontent.setLevel("3");
				smscontent.setObjid(host.getId() + "");
				smscontent.setMessage(host.getAlias() + " (" + host.getIpAddress() + ")" + "����־�ļ��޷���ȷ�ϴ������ܷ�����");
				smscontent.setRecordtime(time);
				smscontent.setSubtype("host");
				smscontent.setSubentity("ftp");
				smscontent.setIp(host.getIpAddress());//���Ͷ���
				SmscontentDao smsmanager = new SmscontentDao();
				smsmanager.sendURLSmscontent(smscontent);
				sendeddata.put(ipaddress + ":file" + host.getId(), date);
			} else {
				//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
				Calendar formerdate = (Calendar) sendeddata.get(ipaddress + ":file:" + host.getId());
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date last = null;
				Date current = null;
				Calendar sendcalen = formerdate;
				Date cc = sendcalen.getTime();
				String tempsenddate = formatter.format(cc);

				Calendar currentcalen = date;
				cc = currentcalen.getTime();
				last = formatter.parse(tempsenddate);
				String currentsenddate = formatter.format(cc);
				current = formatter.parse(currentsenddate);

				long subvalue = current.getTime() - last.getTime();
				if (subvalue / (1000 * 60 * 60 * 24) >= 1) {
					//����һ�죬���ٷ���Ϣ
					Smscontent smscontent = new Smscontent();
					String time = sdf.format(date.getTime());
					smscontent.setLevel("3");
					smscontent.setObjid(host.getId() + "");
					smscontent.setMessage(host.getAlias() + " (" + host.getIpAddress() + ")" + "����־�ļ��޷���ȷ�ϴ������ܷ�����");
					smscontent.setRecordtime(time);
					smscontent.setSubtype("host");
					smscontent.setSubentity("ftp");
					smscontent.setIp(host.getIpAddress());//���Ͷ���
					SmscontentDao smsmanager = new SmscontentDao();
					smsmanager.sendURLSmscontent(smscontent);
					//�޸��Ѿ����͵Ķ��ż�¼	
					sendeddata.put(ipaddress + ":file:" + host.getId(), date);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}