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
import java.util.Date;
import java.util.Hashtable;
import java.util.List;



import com.afunms.alarm.service.NodeAlarmService;
import com.afunms.application.dao.EmailConfigDao;
import com.afunms.application.dao.Emailmonitor_historyDao;
import com.afunms.application.dao.FTPConfigDao;
import com.afunms.application.dao.Ftpmonitor_historyDao;
import com.afunms.application.dao.PSTypeDao;
import com.afunms.application.dao.URLConfigDao;
import com.afunms.application.dao.WebConfigDao;
import com.afunms.application.model.DBVo;
import com.afunms.application.model.EmailMonitorConfig;
import com.afunms.application.model.FTPConfig;
import com.afunms.application.model.MonitorServiceDTO;
import com.afunms.application.model.PSTypeVo;
import com.afunms.application.model.URLConfig;
import com.afunms.application.model.WebConfig;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.SysLogger;
import com.afunms.event.dao.EventListDao;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.node.Ftp;
import com.afunms.polling.node.Mail;
import com.afunms.polling.node.Web;
import com.afunms.system.model.User;


public class ServiceManager extends BaseManager implements ManagerInterface {

	public String execute(String action) {
		// TODO Auto-generated method stub
		if("list".equals(action)){
			return list();
		}else if("changeManage".equals(action)){
			return changeManage();
		}
		
		return null;
	}
	
	public String list(){
		
		String category = getParaValue("category");
		
		String flag = getParaValue("flag");
		
		request.setAttribute("flag", flag);
		
		List monitorServiceDTOList = new ArrayList();
		MonitorServiceDTO monitorServiceDTO = null;
		
		if("mail".equals(category)){
			try {
				// mail
				List maiList = getMailList();
				for(int i = 0 ; i < maiList.size() ; i ++){
					EmailMonitorConfig emailMonitorConfig = (EmailMonitorConfig) maiList.get(i);
					
					monitorServiceDTO = getMonitorServiceDTOByMail(emailMonitorConfig);
					
					monitorServiceDTOList.add(monitorServiceDTO);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		else if("ftp".equals(category)){
			try {
				// FTP
				List ftpList = getFtpList();
				for(int i = 0 ; i < ftpList.size() ; i ++){
					FTPConfig ftpConfig = (FTPConfig) ftpList.get(i);
					
					monitorServiceDTO = getMonitorServiceDTOByFtp(ftpConfig);
					
					monitorServiceDTOList.add(monitorServiceDTO);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		else if("web".equals(category)){
			try {
				// WEB
				List webList = getWebList();
				for(int i = 0 ; i < webList.size() ; i ++){
					WebConfig webConfig = (WebConfig) webList.get(i);
					
					monitorServiceDTO = getMonitorServiceDTOByWeb(webConfig);
					
					monitorServiceDTOList.add(monitorServiceDTO);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		else if("url".equals(category)){
            try {
                // url
                List urlList = getURLList();
                for(int i = 0 ; i < urlList.size() ; i ++){
                    URLConfig urlConfig = (URLConfig) urlList.get(i);
                    
                    monitorServiceDTO = getMonitorServiceDTOByURL(urlConfig);
                    
                    monitorServiceDTOList.add(monitorServiceDTO);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

		else if("portservice".equals(category)){
			try {
				// portService
				List psTypeVoList = getPSTypeVoList();
				for(int i = 0 ; i < psTypeVoList.size() ; i ++){
					PSTypeVo pSTypeVo = (PSTypeVo) psTypeVoList.get(i);
					
					monitorServiceDTO = getMonitorServiceDTOByPSTypeVo(pSTypeVo);
					
					monitorServiceDTOList.add(monitorServiceDTO);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			monitorServiceDTOList = getList();
			
			category = "services";
		}
		
		request.setAttribute("category", category);
		
		request.setAttribute("list", monitorServiceDTOList);
		return "/application/service/list.jsp";
	}
	
	public List getList(){
		
		List monitorServiceDTOList = new ArrayList();
		MonitorServiceDTO monitorServiceDTO = null;
		
		try {
			// mail
			List maiList = getMailList();
			for(int i = 0 ; i < maiList.size() ; i ++){
				EmailMonitorConfig emailMonitorConfig = (EmailMonitorConfig) maiList.get(i);
				
				monitorServiceDTO = getMonitorServiceDTOByMail(emailMonitorConfig);
				
				monitorServiceDTOList.add(monitorServiceDTO);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			// FTP
			List ftpList = getFtpList();
			for(int i = 0 ; i < ftpList.size() ; i ++){
				FTPConfig ftpConfig = (FTPConfig) ftpList.get(i);
				
				monitorServiceDTO = getMonitorServiceDTOByFtp(ftpConfig);
				
				monitorServiceDTOList.add(monitorServiceDTO);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			// WEB
			List webList = getWebList();
			for(int i = 0 ; i < webList.size() ; i ++){
				WebConfig webConfig = (WebConfig) webList.get(i);
				
				monitorServiceDTO = getMonitorServiceDTOByWeb(webConfig);
				
				monitorServiceDTOList.add(monitorServiceDTO);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
            // URL
            List urlList = getURLList();
            for(int i = 0 ; i < urlList.size() ; i ++){
                URLConfig urlConfig = (URLConfig) urlList.get(i);
                
                monitorServiceDTO = getMonitorServiceDTOByURL(urlConfig);
                
                monitorServiceDTOList.add(monitorServiceDTO);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
			// portService
			List psTypeVoList = getPSTypeVoList();
			for(int i = 0 ; i < psTypeVoList.size() ; i ++){
				PSTypeVo pSTypeVo = (PSTypeVo) psTypeVoList.get(i);
				
				monitorServiceDTO = getMonitorServiceDTOByPSTypeVo(pSTypeVo);
				
				monitorServiceDTOList.add(monitorServiceDTO);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return monitorServiceDTOList;
	}
	
	/**
	 * ��ȡ Mail �б�
	 * @return
	 */
	public List getMailList(){
		// mail
		
		List maillist = null;
		
		EmailConfigDao emailConfigDao = new EmailConfigDao();
		try{
			maillist = emailConfigDao.findByCondition(getMailSql());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			emailConfigDao.close();
		}
		
		if(maillist == null){
			maillist = new ArrayList();
		}
		
		return maillist;
	}
	
	/**
	 * ��ȡ Mail Sql ���
	 * @return
	 */
	public String getMailSql(){
		
		String sql = " where 1=1";
		
		sql = sql + getMonFlagSql("monflag");
		
		sql = sql + getBidSql("bid");
		
		return sql;
	}
	
	
	/**
	 * ���� mailConfig ����װ �м��
	 * @param mailConfig
	 * @return
	 */
	public MonitorServiceDTO getMonitorServiceDTOByMail(EmailMonitorConfig emailMonitorConfig){
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = simpleDateFormat.format(new Date());
		
		String starttime = date + " 00:00:00";
		String totime = date + " 23:59:59";
		
		int id = emailMonitorConfig.getId();
		String alias = emailMonitorConfig.getName();
		String ipAddress = emailMonitorConfig.getIpaddress();
		String category = "mail";
		
		Node mailNode = PollingEngine.getInstance().getMailByID(id);
		
		int status = 0;
		try {
			status = mailNode.getStatus();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Hashtable eventListSummary = new Hashtable();
		
		String generalAlarm = "0"; 				// ��ͨ�澯��  Ĭ��Ϊ 0
		String urgentAlarm = "0"; 				// ���ظ澯��  Ĭ��Ϊ 0
		String seriousAlarm = "0";				// �����澯��  Ĭ��Ϊ 0
		
		Emailmonitor_historyDao emailmonitor_historyDao = new Emailmonitor_historyDao();
		try {
			seriousAlarm = emailmonitor_historyDao.getCountByWhere("  where email_id='" + id + "'" + " and is_canconnected='0' and mon_time>='" + starttime + "' and mon_time<='" + totime + "'");
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			emailmonitor_historyDao.close();
		}
		eventListSummary.put("generalAlarm", generalAlarm);
		eventListSummary.put("urgentAlarm", urgentAlarm);
		eventListSummary.put("seriousAlarm", seriousAlarm);
		
		String monflag = "��";
		
		if(emailMonitorConfig.getMonflag() == 1){
			monflag = "��";
		}
		
		
		
		
		
		MonitorServiceDTO monitorServiceDTO = new MonitorServiceDTO();
		monitorServiceDTO.setId(id);
		monitorServiceDTO.setStatus(String.valueOf(status));
		monitorServiceDTO.setAlias(alias);
		monitorServiceDTO.setIpAddress(ipAddress);
		monitorServiceDTO.setPingValue("");
		monitorServiceDTO.setCategory(category);
		monitorServiceDTO.setEventListSummary(eventListSummary);
		monitorServiceDTO.setMonflag(monflag);
		return monitorServiceDTO;
	}
	
	
	
	
	/**
	 * ��ȡ FTP �б�
	 * @return
	 */
	public List getFtpList(){
		// FTP
		
		List ftplist = null;
		
		FTPConfigDao ftpConfigDao = new FTPConfigDao();
		try{
			ftplist = ftpConfigDao.findByCondition(getFtpSql());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ftpConfigDao.close();
		}
		
		if(ftplist == null){
			ftplist = new ArrayList();
		}
		
		return ftplist;
	}
	
	/**
	 * ��ȡ Ftp Sql ���
	 * @return
	 */
	public String getFtpSql(){
		
		String sql = " where 1=1";
		
		sql = sql + getMonFlagSql("monflag");
		
		sql = sql + getBidSql("bid");
		
		return sql;
	}
	
	
	/**
	 * ���� ftpConfig ����װ �м��
	 * @param ftpConfig
	 * @return
	 */
	public MonitorServiceDTO getMonitorServiceDTOByFtp(FTPConfig ftpConfig){
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = simpleDateFormat.format(new Date());
		
		String starttime = date + " 00:00:00";
		String totime = date + " 23:59:59";
		
		int id = ftpConfig.getId();
		String alias = ftpConfig.getName();
		String ipAddress = ftpConfig.getIpaddress();
		String category = "ftp";
		
		Node ftpNode = PollingEngine.getInstance().getFtpByID(id);
		
		int status = 0;
		try {
			status = ftpNode.getStatus();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Hashtable eventListSummary = new Hashtable();
		
		String generalAlarm = "0"; 				// ��ͨ�澯��  Ĭ��Ϊ 0
		String urgentAlarm = "0"; 				// ���ظ澯��  Ĭ��Ϊ 0
		String seriousAlarm = "0";				// �����澯��  Ĭ��Ϊ 0
		
		Ftpmonitor_historyDao ftpmonitor_historyDao = new Ftpmonitor_historyDao();
		try {
			seriousAlarm = ftpmonitor_historyDao.getCountByWhere(" where ftp_id='" + id + "'" + " and is_canconnected='0' and mon_time>='" + starttime + "' and mon_time<='" + totime + "'");
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			ftpmonitor_historyDao.close();
		}
		eventListSummary.put("generalAlarm", generalAlarm);
		eventListSummary.put("urgentAlarm", urgentAlarm);
		eventListSummary.put("seriousAlarm", seriousAlarm);
		
		
		String monflag = "��";
		if(ftpConfig.getMonflag() == 1){
			monflag = "��";
		}
		
		
		MonitorServiceDTO monitorServiceDTO = new MonitorServiceDTO();
		monitorServiceDTO.setId(id);
		monitorServiceDTO.setStatus(String.valueOf(status));
		monitorServiceDTO.setAlias(alias);
		monitorServiceDTO.setIpAddress(ipAddress);
		monitorServiceDTO.setPingValue("");
		monitorServiceDTO.setCategory(category);
		monitorServiceDTO.setEventListSummary(eventListSummary);
		monitorServiceDTO.setMonflag(monflag);
		return monitorServiceDTO;
	}
	
	
	
	/**
	 * ��ȡ WEB �б�
	 * @return
	 */
	public List getWebList(){
		// WEB
		
		List weblist = null;
		
		WebConfigDao webConfigDao = new WebConfigDao();
		try{
			weblist = webConfigDao.findByCondition(getWebSql());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			webConfigDao.close();
		}
		
		if(weblist == null){
			weblist = new ArrayList();
		}
		
		return weblist;
	}
	
	/**
	 * ��ȡ Web Sql ���
	 * @return
	 */
	public String getWebSql(){
		
		String sql = " where 1=1";
		
		sql = sql + getMonFlagSql("flag");
		
		sql = sql + getBidSql("netid");
		
		return sql;
	}
	
	
	/**
	 * ���� webConfig ����װ �м��
	 * @param webConfig
	 * @return
	 */
	public MonitorServiceDTO getMonitorServiceDTOByWeb(WebConfig webConfig){
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = simpleDateFormat.format(new Date());
		
		String starttime = date + " 00:00:00";
		String totime = date + " 23:59:59";
		
		int id = webConfig.getId();
		String alias = webConfig.getAlias();
		String ipAddress = webConfig.getIpAddress();
		String category = "web";
		
		Node webNode = PollingEngine.getInstance().getWebByID(id);
		
		int status = 0;
		try {
			status = webNode.getStatus();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Hashtable eventListSummary = new Hashtable();
		
		String generalAlarm = "0"; 				// ��ͨ�澯��  Ĭ��Ϊ 0
		String urgentAlarm = "0"; 				// ���ظ澯��  Ĭ��Ϊ 0
		String seriousAlarm = "0";				// �����澯��  Ĭ��Ϊ 0
		
		EventListDao eventListDao = new EventListDao();
		try {
			generalAlarm = eventListDao.getCountByWhere(" where nodeid='" + id + "'" + " and level1='1' and recordtime>='" + starttime + "' and recordtime<='" + totime + "'");
			urgentAlarm = eventListDao.getCountByWhere(" where nodeid='" + id + "'" + " and level1='2' and recordtime>='" + starttime + "' and recordtime<='" + totime + "'");
			seriousAlarm = eventListDao.getCountByWhere(" where nodeid='" + id + "'" + " and level1='3' and recordtime>='" + starttime + "' and recordtime<='" + totime + "'");
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			eventListDao.close();
		}
		eventListSummary.put("generalAlarm", generalAlarm);
		eventListSummary.put("urgentAlarm", urgentAlarm);
		eventListSummary.put("seriousAlarm", seriousAlarm);
		
		
		String monflag = "��";
		if(webConfig.getFlag() == 1){
			monflag = "��";
		}
		
		MonitorServiceDTO monitorServiceDTO = new MonitorServiceDTO();
		monitorServiceDTO.setId(id);
		monitorServiceDTO.setStatus(String.valueOf(status));
		monitorServiceDTO.setAlias(alias);
		monitorServiceDTO.setIpAddress(ipAddress);
		monitorServiceDTO.setPingValue("");
		monitorServiceDTO.setCategory(category);
		monitorServiceDTO.setEventListSummary(eventListSummary);
		monitorServiceDTO.setMonflag(monflag);
		return monitorServiceDTO;
	}
	
	
	/**
     * ��ȡ URL �б�
     * @return
     */
    public List getURLList(){
        // URL
        
        List urllist = null;
        
        URLConfigDao urlConfigDao = new URLConfigDao();
        try{
            urllist = urlConfigDao.findByCondition(getWebSql());
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            urlConfigDao.close();
        }
        
        if(urllist == null){
            urllist = new ArrayList();
        }
        
        return urllist;
    }
    
    /**
     * ��ȡ URL Sql ���
     * @return
     */
    public String getURLSql(){
        
        String sql = " where 1=1";
        
        sql = sql + getMonFlagSql("mon_flag");
        
        sql = sql + getBidSql("bid");
        
        return sql;
    }
    
    
    /**
     * ���� webConfig ����װ �м��
     * @param webConfig
     * @return
     */
    public MonitorServiceDTO getMonitorServiceDTOByURL(URLConfig urlConfig){
        
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String date = simpleDateFormat.format(new Date());
//        
//        String starttime = date + " 00:00:00";
//        String totime = date + " 23:59:59";
        
        int id = urlConfig.getId();
        String alias = urlConfig.getName();
        String ipAddress = urlConfig.getIpaddress();
        String category = "url";
        
//        Node webNode = PollingEngine.getInstance().getWebByID(id);
//        
//        int status = 0;
//        try {
//            status = webNode.getStatus();
//        } catch (RuntimeException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        NodeUtil util = new NodeUtil();
        NodeDTO node = util.conversionToNodeDTO(urlConfig);

        NodeAlarmService service = new NodeAlarmService();
        int status = service.getMaxAlarmLevel(node);
        
        Hashtable eventListSummary = new Hashtable();
        
        String generalAlarm = "0";              // ��ͨ�澯��  Ĭ��Ϊ 0
        String urgentAlarm = "0";               // ���ظ澯��  Ĭ��Ϊ 0
        String seriousAlarm = "0";              // �����澯��  Ĭ��Ϊ 0
//        
//        EventListDao eventListDao = new EventListDao();
//        try {
//            generalAlarm = eventListDao.getCountByWhere(" where nodeid='" + id + "'" + " and level1='1' and recordtime>='" + starttime + "' and recordtime<='" + totime + "'");
//            urgentAlarm = eventListDao.getCountByWhere(" where nodeid='" + id + "'" + " and level1='2' and recordtime>='" + starttime + "' and recordtime<='" + totime + "'");
//            seriousAlarm = eventListDao.getCountByWhere(" where nodeid='" + id + "'" + " and level1='3' and recordtime>='" + starttime + "' and recordtime<='" + totime + "'");
//        } catch (RuntimeException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } finally{
//            eventListDao.close();
//        }
        eventListSummary.put("generalAlarm", generalAlarm);
        eventListSummary.put("urgentAlarm", urgentAlarm);
        eventListSummary.put("seriousAlarm", seriousAlarm);
        
        
        String monflag = "��";
        if(urlConfig.getMonFlag() == 1){
            monflag = "��";
        }
        
        MonitorServiceDTO monitorServiceDTO = new MonitorServiceDTO();
        monitorServiceDTO.setId(id);
        monitorServiceDTO.setStatus(String.valueOf(status));
        monitorServiceDTO.setAlias(alias);
        monitorServiceDTO.setIpAddress(ipAddress);
        monitorServiceDTO.setPingValue("");
        monitorServiceDTO.setCategory(category);
        monitorServiceDTO.setEventListSummary(eventListSummary);
        monitorServiceDTO.setMonflag(monflag);
        return monitorServiceDTO;
    }
	
	/**
	 * ��ȡ PSTypeVo �б�
	 * @return
	 */
	public List getPSTypeVoList(){
		// WEB
		
		List psTypeVoList = null;
		
		PSTypeDao psTypeDao = new PSTypeDao();
		try{
			psTypeVoList = psTypeDao.findByCondition(getPSTypeVoSql());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			psTypeDao.close();
		}
		
		if(psTypeVoList == null){
			psTypeVoList = new ArrayList();
		}
		
		return psTypeVoList;
	}
	
	/**
	 * ��ȡ PSTypeVo Sql ���
	 * @return
	 */
	public String getPSTypeVoSql(){
		
		String sql = " where 1=1";
		
		sql = sql + getMonFlagSql("monflag");
		
		sql = sql + getBidSql("bid");
		
		return sql;
	}
	
	
	/**
	 * ���� psTypeVo ����װ �м��
	 * @param psTypeVo
	 * @return
	 */
	public MonitorServiceDTO getMonitorServiceDTOByPSTypeVo(PSTypeVo psTypeVo){
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = simpleDateFormat.format(new Date());
		
		String starttime = date + " 00:00:00";
		String totime = date + " 23:59:59";
		
		int id = psTypeVo.getId();
		String alias = psTypeVo.getPort();
		String ipAddress = psTypeVo.getIpaddress();
		String category = "portService";
		
		Node psTypeVoNode = PollingEngine.getInstance().getSocketByID(id);
		
		int status = 0;
		try {
			status = psTypeVoNode.getStatus();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Hashtable eventListSummary = new Hashtable();
		
		String generalAlarm = "0"; 				// ��ͨ�澯��  Ĭ��Ϊ 0
		String urgentAlarm = "0"; 				// ���ظ澯��  Ĭ��Ϊ 0
		String seriousAlarm = "0";				// �����澯��  Ĭ��Ϊ 0
		
		EventListDao eventListDao = new EventListDao();
		try {
			generalAlarm = eventListDao.getCountByWhere(" where nodeid='" + id + "'" + " and level1='1' and recordtime>='" + starttime + "' and recordtime<='" + totime + "'");
			urgentAlarm = eventListDao.getCountByWhere(" where nodeid='" + id + "'" + " and level1='2' and recordtime>='" + starttime + "' and recordtime<='" + totime + "'");
			seriousAlarm = eventListDao.getCountByWhere(" where nodeid='" + id + "'" + " and level1='3' and recordtime>='" + starttime + "' and recordtime<='" + totime + "'");
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			eventListDao.close();
		}
		eventListSummary.put("generalAlarm", generalAlarm);
		eventListSummary.put("urgentAlarm", urgentAlarm);
		eventListSummary.put("seriousAlarm", seriousAlarm);
		
		
		String monflag = "��";
		if(psTypeVo.getMonflag() == 1){
			monflag = "��";
		}
		
		
		MonitorServiceDTO monitorServiceDTO = new MonitorServiceDTO();
		monitorServiceDTO.setId(id);
		monitorServiceDTO.setStatus(String.valueOf(status));
		monitorServiceDTO.setAlias(alias);
		monitorServiceDTO.setIpAddress(ipAddress);
		monitorServiceDTO.setPingValue("");
		monitorServiceDTO.setCategory(category);
		monitorServiceDTO.setEventListSummary(eventListSummary);
		monitorServiceDTO.setMonflag(monflag);
		return monitorServiceDTO;
	}
	
	
	
	
	public String getMonFlagSql(String fieldName){
    	String mon_flag = getParaValue("mon_flag");
    	
    	String sql = "";
    	if(mon_flag !=null && "1".equals(mon_flag)){
    		sql = " and "+ fieldName + "='1'";
    	}
    	
    	request.setAttribute("mon_flag", mon_flag);
    	
    	return sql;
    }
	
	
	/**
     * ���ҵ��Ȩ�޵� SQL ���
     * @author nielin
     * @date 2010-08-09
     * @return
     */
    public String getBidSql(String fieldName){
    	User current_user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
    	
    	String sql = "";
    	StringBuffer s = new StringBuffer();
		if(current_user.getRole() != 0){
		int _flag = 0;
		if (current_user.getBusinessids() != null){
			if(current_user.getBusinessids() !="-1"){
				String[] bids = current_user.getBusinessids().split(",");
				if(bids.length>0){
					for(int i=0;i<bids.length;i++){
						if(bids[i].trim().length()>0){
							if(_flag==0){
									s.append(" and ( " + fieldName + " like '%,"+bids[i].trim()+",%' ");
								_flag = 1;
							}else{
								//flag = 1;
									s.append(" or " + fieldName + " like '%,"+bids[i].trim()+",%' ");
							}
						}
					}
					s.append(") ") ;
				}
				
			}	
		}
		}
		
		sql = s.toString();
		
		String treeBid = request.getParameter("treeBid");
		if(treeBid != null && treeBid.trim().length() > 0){
			treeBid = treeBid.trim();
			treeBid = "," + treeBid + ",";
			String[] treeBids = treeBid.split(",");
			if(treeBids != null){
				for(int i = 0; i < treeBids.length; i++){
					if(treeBids[i].trim().length() > 0){
						sql = sql + " and " + fieldName + " like '%," + treeBids[i].trim() + ",%'";
					}
				}
			}
		}
		//SysLogger.info("select * from topo_host_node where managed=1 "+sql);
		return sql;
    }
    
    
    public String changeManage(){
    	
    	String category = getParaValue("type");
    	
    	try {
			if("ftp".equals(category)){
				changeFtpManage();
			}else if("mail".equals(category)){
				changeMailManage();
			}else if("web".equals(category)){
				changeWebManage();
			}else if("portService".equals(category)){
				changePortServiceManage();
			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	return list();
    }
    
    /**
	 * �޸� FTPConfig �ļ�����Ϣ֮�󷵻�FTPConfig�б�ҳ��
	 * @return
	 */
	private void changeFtpManage()
    {   
		boolean result = false;
		FTPConfig ftpConfig=new FTPConfig();
        FTPConfigDao ftpConfigDao = null;
        try{
        	String id = getParaValue("id");
        	int monflag = getParaIntValue("value");
        	ftpConfigDao =new FTPConfigDao();
        	ftpConfig = (FTPConfig)ftpConfigDao.findByID(id);
        	ftpConfig.setMonflag(monflag);
        	result = ftpConfigDao.update(ftpConfig);
        	Ftp ftp = (Ftp)PollingEngine.getInstance().getFtpByID(Integer.parseInt(id));
			ftp.setMonflag(monflag);
        }catch(Exception e){
        	e.printStackTrace();
        	result = false;
        }finally{
        	ftpConfigDao.close();
        }
    }
	
	
	private void changeMailManage()
    {   
		boolean result = false;
		EmailMonitorConfig emailMonitorConfig=new EmailMonitorConfig();
		EmailConfigDao emailConfigDao = null;
        try{
        	String id = getParaValue("id");
        	int monflag = getParaIntValue("value");
        	emailConfigDao =new EmailConfigDao();
        	emailMonitorConfig = (EmailMonitorConfig)emailConfigDao.findByID(id);
        	emailMonitorConfig.setMonflag(monflag);
        	result = emailConfigDao.update(emailMonitorConfig);	   
			Mail mail = (Mail)PollingEngine.getInstance().getMailByID(Integer.parseInt(id));
			mail.setFlag(monflag);
        }catch(Exception e){
        	e.printStackTrace();
        	result = false;
        }finally{
        	emailConfigDao.close();
        }
    }
	
	
	
	private void changeWebManage()
    {    
		WebConfig vo = new WebConfig();
		try{
			int monflag = getParaIntValue("value");
			WebConfigDao configdao = new WebConfigDao();
			try{
				vo = (WebConfig)configdao.findByID(getParaValue("id"));
			}catch(Exception e){
    			e.printStackTrace();
    		}finally{
    			configdao.close();
    		}
    		
			vo.setFlag(monflag);
			configdao = new WebConfigDao();
			try{
				configdao.update(vo);
			}catch(Exception e){
    			e.printStackTrace();
    		}finally{
    			configdao.close();
    		}
    		
			Web web = (Web)PollingEngine.getInstance().getWebByID(vo.getId());
			web.setFlag(monflag);
		}catch(Exception e){
			e.printStackTrace();
		}
    }
	
	
	private void changePortServiceManage()
    {   
		boolean result = false;
		PSTypeVo pstyVo =new PSTypeVo();
		PSTypeDao pstypedao = null;
        try{
        	String id = getParaValue("id");
        	int monflag = getParaIntValue("value");
        	pstypedao =new PSTypeDao();
        	pstyVo = (PSTypeVo)pstypedao.findByID(id);
        	pstyVo.setMonflag(monflag);
        	result = pstypedao.update(pstyVo);	    
        }catch(Exception e){
        	e.printStackTrace();
        	result = false;
        }finally{
        	pstypedao.close();
        }
    }
    
	
	
	
	
}