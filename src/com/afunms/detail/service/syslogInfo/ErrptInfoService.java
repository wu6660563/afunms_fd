package com.afunms.detail.service.syslogInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.afunms.config.model.Errptlog;
import com.afunms.event.dao.SyslogDao;
import com.afunms.event.model.Syslog;


public class ErrptInfoService {
	
	private String type;
	
	private String subtype;
	
	private String nodeid;
	

	/**
	 * @param type
	 * @param subtype
	 * @param nodeid
	 */
	public ErrptInfoService(String nodeid, String type, String subtype) {
		this.nodeid = nodeid;
		this.type = type;
		this.subtype = subtype;
	}
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the subtype
	 */
	public String getSubtype() {
		return subtype;
	}

	/**
	 * @param subtype the subtype to set
	 */
	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	/**
	 * @return the nodeid
	 */
	public String getNodeid() {
		return nodeid;
	}

	/**
	 * @param nodeid the nodeid to set
	 */
	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}




	/**
	 * ��ȡErrpt��Ϣ
	 * �÷���Ϊ����ӿڷ���
	 * @param startdate		��ʼ����
	 * @param todate		��������
	 * @param priorityname	��־�ȼ�
	 * @return
	 */
	public List<Errptlog> getErrptInfo(String ipaddress, String startdate, String todate, String errpttype, String errptclass){
		return getErrptSQLCondition(ipaddress, startdate, todate, errpttype, errptclass);
	}
	 
	/**
	 * ��ȡSyslog��Ϣ
	 * �÷���Ϊ˽�з���
	 * @param startdate		��ʼ����
	 * @param todate		��������
	 * @param priorityname	��־�ȼ�
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Errptlog> getErrptSQLCondition(String ipaddress, String startdate, String todate, String errpttype, String errptclass){
		String startTime = "";		// ��ʼʱ��
		String toTime = "";			// ����ʱ��
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if(startdate == null || "".equals(startdate.trim())){
			startdate = simpleDateFormat.format(date);
		}
		startTime = startdate + " 00:00:00";
		if(todate == null || "".equals(todate.trim())){
			todate = simpleDateFormat.format(date);
		}
		toTime = todate + " 23:59:59";
		List<Errptlog> list = null;
		SyslogDao syslogDao = new SyslogDao();
		try {
			//list = syslogDao.getQuery(ipaddress, startTime, toTime, errpttype, errptclass);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			syslogDao.close();
		}
		return list;
	}
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
