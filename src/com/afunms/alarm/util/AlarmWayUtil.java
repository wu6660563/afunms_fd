package com.afunms.alarm.util;

import java.util.ArrayList;
import java.util.List;

import com.afunms.alarm.dao.AlarmWayDao;
import com.afunms.alarm.dao.AlarmWayDetailDao;
import com.afunms.alarm.model.AlarmWay;
import com.afunms.alarm.model.AlarmWayDetail;

public class AlarmWayUtil {
	
	/**
	 * ��������Ϊ ��
	 */
	public static String DATE_TYPE_MONTH = "month";
	
	/**
	 * ��������Ϊ ��
	 */
	public static String DATE_TYPE_WEEK = "week";
	
	/**
	 * �澯���� ҳ��
	 */
	public static String ALARM_WAY_CATEGORY_PAGE = "page";
	
	/**
	 * �澯���� ����
	 */
	public static String ALARM_WAY_CATEGORY_SOUND = "sound";
	
	/**
	 * �澯���� �ʼ�
	 */
	public static String ALARM_WAY_CATEGORY_MAIL = "mail";
	
	/**
	 * �澯���� ����
	 */
	public static String ALARM_WAY_CATEGORY_SMS = "sms";
	
	/**
	 * �澯���� �绰
	 */
	public static String ALARM_WAY_CATEGORY_PHONE = "phone";
	
	/**
	 * �澯���� ����
	 */
	public static String ALARM_WAY_CATEGORY_DESKTOP = "desktop";

	/**
     * �澯���� ����
     */
    public static String ALARM_WAY_CATEGORY_FVSD = "fvsd";

	/**
	 * ���澯��ʽ����ϸ���ô������ݿ���
	 * �˷����� alarmWayDetailList �� alarmWayDetail �� alarmWayId ���úú� 
	 * �ȸ��� alarmWayId ɾ�� alarmWayDetail
	 * �����saveAlarmWayDetail(List alarmWayDetailList)����
	 * @param alarmWay
	 * @param alarmWayDetailList
	 */
	public void saveAlarmWayDetail(AlarmWay alarmWay , List alarmWayDetailList){
		List list = new ArrayList();
		if(alarmWayDetailList != null){
			for(int i = 0 ; i < alarmWayDetailList.size() ; i++){
				AlarmWayDetail alarmWayDetail = (AlarmWayDetail)alarmWayDetailList.get(i);
				alarmWayDetail.setAlarmWayId(alarmWay.getId()+"");
				list.add(alarmWayDetail);
			}
			
			AlarmWayDetailDao alarmWayDetailDao = new AlarmWayDetailDao();
			try {
				alarmWayDetailDao.deleteByAlarmWayId(alarmWay.getId()+"");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				alarmWayDetailDao.close();
			}
			
			saveAlarmWayDetail(list);
		}
		
	}
	
	/**
	 * ���澯��ʽ����ϸ���ô������ݿ���
	 * @param alarmWayDetailList
	 */
	public void saveAlarmWayDetail(List alarmWayDetailList){
		AlarmWayDetailDao alarmWayDetailDao = new AlarmWayDetailDao();
		try {
			alarmWayDetailDao.save(alarmWayDetailList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			alarmWayDetailDao.close();
		}
		
	}
	
	/**
	 * ������ alarmWayId ɾ�����ݿ��и澯��ʽ����ϸ����
	 * @param alarmWayId
	 */
	public void deleteAlarmWayDetail(String[] ids){
		if(ids !=null){
			for(int i = 0 ; i < ids.length ; i++){
				deleteAlarmWayDetail(ids[i]);
			}
		}
		
	}
	
	/**
	 * ������ alarmWayId ɾ�����ݿ��и澯��ʽ����ϸ����
	 * @param alarmWayId
	 */
	public void deleteAlarmWayDetail(String alarmWayId){
		AlarmWayDetailDao alarmWayDetailDao = new AlarmWayDetailDao();
		try {
			alarmWayDetailDao.deleteByAlarmWayId(alarmWayId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			alarmWayDetailDao.close();
		}
		
	}
	
	
	/**
	 * ���� AlarmWay ���
	 * @param alarmWayId
	 */
	public boolean saveAlarmWay(AlarmWay alarmWay){
		
		if("1".equals(alarmWay.getIsDefault())){
			updateIsDefault("0", "1");
		}
		
		boolean result = false;
		AlarmWayDao alarmWayDao = new AlarmWayDao();
		try {
			result = alarmWayDao.save(alarmWay);
		} catch (RuntimeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			alarmWayDao.close();
		}
		
		return result;
		
	}
	
	/**
	 * ���� AlarmWay ���
	 * @param alarmWayId
	 */
	public boolean updateAlarmWay(AlarmWay alarmWay){
		
		if("1".equals(alarmWay.getIsDefault())){
			updateIsDefault("0", "1");
		}
		
		boolean result = false;
		AlarmWayDao alarmWayDao = new AlarmWayDao();
		try {
			result = alarmWayDao.update(alarmWay);
		} catch (RuntimeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			alarmWayDao.close();
		}
		
		return result;
		
	}
	
	
	/**
	 * ���� isDefault ���
	 * @param alarmWayId
	 */
	public boolean updateIsDefault(String newIsDefault , String oldIsDefault){
		boolean result = false;
		AlarmWayDao alarmWayDao = new AlarmWayDao();
		try {
			result = alarmWayDao.updateIsDefault(newIsDefault , oldIsDefault);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			alarmWayDao.close();
		}
		
		return result;
		
	}
	
	
	
}
