package com.afunms.alarm.util;

import java.util.ArrayList;
import java.util.List;

import com.afunms.alarm.dao.AlarmDeviceDependenceDao;
import com.afunms.alarm.dao.AlarmIndicatorsDao;
import com.afunms.alarm.model.AlarmDeviceDependence;
import com.afunms.alarm.model.AlarmIndicators;
import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.common.util.SysLogger;
import com.afunms.event.dao.CheckEventCompressDao;
import com.afunms.event.dao.CheckEventDao;
import com.afunms.event.model.CheckEvent;
import com.afunms.event.model.CheckEventCompress;

/**
 * 
 * ClassName:   AlarmCompressUtil.java
 * <p>
 * 		�澯ѹ�ƹ���
 * 
 * 		������
 *         �����ܽ�ϸ澯��·��澯���ƶԸ澯����ѹ�ƣ���Ҫ���������豸ϵͳ֮��
 *      �ļ��ָ����������ȼ��ͱ�Ҫ������ϵʱ��ѹ���˲���Ҫ�����ĸ澯��Ϣ���Ӷ�ʹ��
 *      ���Բ������ϵ��豸�Ķ�λ����׼ȷ������
 *      
 *      ���˼·��
 *         ����������ϵͳ֮ǰ�澯����Ļ����Ͻ��У��ṩ�Ĺ���������Ϊ
 *         public void compressCheckEvent(AlarmIndicatorsNode nm, CheckEvent event);
 *         ��Ҫ����AlarmIndicatorsNode �� CheckEvent �����������������������鿴��ǰ�澯�Ƿ�߱���
 *         ѹ���������߱������ѹ���������߱����ѯ�������ȼ��ߵ�ָ����м�飬������ɲ��߱�ѹ��������
 *         ��ѯ�������豸֮�ϵ��豸�������豸����ѹ����
 *       
 * </p>
 *
 * @author      yaoanguo@dhcc.com.cn

 * @version     v1.01
 * 
 * @since       v1.01
 * 
 * @Date        Dec 10, 2012 2:01:08 PM
 */
public class AlarmCompressUtil {

	 /**
     * ��־
     */
    private static SysLogger logger = SysLogger.getLogger(AlarmCompressUtil.class.getName());
	
	public static void main(String[] args) {
		AlarmIndicatorsNode nm = new AlarmIndicatorsNode();
		nm.setType("db");
		nm.setSubtype("oracle");
		nm.setName("buffercache");
		nm.setNodeid("123");
		CheckEvent event = new CheckEvent();
		event.setAlarmlevel(2);
		event.setNodeId("123");
		event.setType("db");
		event.setSubtype("oracle");
		event.setName("buffercache");
		
		event.setCollecttime("2012-11-20 11:20:11");
		event.setContent("����999");
		
		//AlarmLinkNodeServices ser = new AlarmLinkNodeServices();
		//ser.compressCheckEvent(nm, event);
		//ser.compressInsertCheckEvent(event);
		//ser.compressUpdateCheckEvent(event);
		//ser.getHighPriorityLevelIndicators(nm);
		System.out.println("\n������ϣ�");
	}
	
	
	/**
	 * ѹ���澯����
	 * 
	 * @param nm
	 * 
	 * @param event
	 * 
	 */
	public int compressCheckEvent(AlarmIndicatorsNode nm, CheckEvent event) {	
		int returnFlag = 0;
		/**
		 * 	�������͵ĸ澯�Ƿ�ѹ����ѹ������£�
		 * 	���򣬲�ѯ���ȼ��ߵ�ָ��澯
		 */
		int num = 0;
		
		// ��ѯ�ȵ�ǰ�澯���ȼ��ϸߵĸ澯�����ѹ������
		compressInsertCheckEvent(event);
		num = isCompressCheckEvent(
				getHighPriorityLevelIndicators(nm),nm.getNodeid(),event.getAlarmlevel());
		if(num > 0) {
			// ��ѹ�����д��ڱȵ�ǰ�澯���ȼ��ߵĸ澯��Ϣ�������ѹ�Ƴ���
			// ����ǰ�澯��Ϣ������ѹ������
			returnFlag = 0;
		} else {
			// ��ǰѹ�����в�����ѹ����Ϣ 
			// ������豸������ϵ����
			
			//����ϵ����nms_alarm_device_dependence�����Ҫ�Լ��趨��׼�豸
			//���ظù��ܣ�Ŀ�ķ�ֹʧ���޸Ļ�׼�豸
			//����ͼ������ϣ�����֮�󣬸ù���������ʹ��
			//�趨�ط�Ϊ���澯��ʽ�趨-->list.jsp(258 line)--><a href="#" onclick="relatedNode()" style="display:inline">�����豸</a>�޸�Ϊ�ɼ���Ȼ�����趨
			AlarmDeviceDependence previousAlarmDevice = 
				getPreviousDevice(Integer.parseInt(nm.getNodeid()));
			// �鿴�����豸�����Ƿ�����
			num  = isCompressCheckEvent(previousAlarmDevice.getType(),previousAlarmDevice.getSubtype(),
					"ping", previousAlarmDevice.getNodeid(), -1);
			if(num > 0) {
				returnFlag = 0;
			} else {
				// �����澯
				returnFlag = 1;
			}
			
		}
		return returnFlag;
	}
	
	
	private List<AlarmIndicators> getHighPriorityLevelIndicators(AlarmIndicatorsNode nm) {
		List<AlarmIndicators> newAlarm = new ArrayList<AlarmIndicators>();
		
		List<AlarmIndicators> alarm = getAlarmIndicatorsByNodeType(
						nm.getType(),nm.getSubtype(),1);
		AlarmIndicators alarmIndicators = null;
		
		// ����ָ��������ϵ�ж����Ƿ�澯
		for(int i=0; i<alarm.size(); i++) {
			if(alarm.get(i).getName().equals(nm.getName())) {
				alarmIndicators = alarm.get(i);
			}
		}
		
		//PriorityLevel���ȼ�Խ�ߣ�����ԽС
		for(int i=0; i<alarm.size(); i++) {
			if(alarm.get(i).getPriorityLevel() < alarmIndicators.getPriorityLevel()) {
				newAlarm.add(alarm.get(i));
			}
		}
		return newAlarm;
	}
	
	/**
	 * ����ѹ���澯��
	 * 
	 * @param event
	 */
	private void compressUpdateCheckEvent(CheckEvent event) {
		CheckEventCompress compress = eventToCompress(event);
		CheckEventCompressDao checkEventCompressDao = new CheckEventCompressDao();
		try{
			// ����ʱ������firsttime�ֶ�
			checkEventCompressDao.updateExceptFirsttime(compress);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			checkEventCompressDao.close();
		}
		logger.info("����Compress:" + compress.toString());
	}

	private void compressInsertCheckEvent(CheckEvent event) {
		CheckEventCompress compress = eventToCompress(event);
		CheckEventCompressDao checkEventCompressDao = new CheckEventCompressDao();
		try{
			checkEventCompressDao.save(compress);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			checkEventCompressDao.close();
		}
	}
	
	/**
	 * �������ͺ������ͻ�ȡ��澯ָ��
	 * 
	 * @param type
	 * 			
	 * @param subType
	 * 
	 * @param enable
	 * 			1 ��ʾָ����� 0 ��ʾָ�겻����
	 * @return
	 */
	private List<AlarmIndicators> getAlarmIndicatorsByNodeType(String type, 
			String subType,int enable) {
		String condition = " where type='" + type 
				+"' and subtype='" + subType+"' and enabled='" + enable +"'";
		List<AlarmIndicators> alarm = new ArrayList<AlarmIndicators>();
		AlarmIndicatorsDao dao = new AlarmIndicatorsDao();
		try{
			alarm = dao.findByCondition(condition);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		return alarm;
	}
	
	/**
	 * CheckEventתΪCheckEventCompress
	 * 	
	 * 	firsttime�ֶ���collecttime��ͬ
	 * 	
	 * @param event
	 * @return
	 */
	private CheckEventCompress eventToCompress(CheckEvent event) {
		CheckEventCompress compress = new CheckEventCompress();
		compress.setAlarmId(event.getAlarmId());
		compress.setAlarmlevel(event.getAlarmlevel());
		compress.setCollecttime(event.getCollecttime());
		compress.setContent(event.getContent());
		compress.setFirsttime(event.getCollecttime());	//
		compress.setName(event.getName());
		compress.setNodeId(event.getNodeId());
		compress.setSindex(event.getSindex());
		compress.setSubtype(event.getSubtype());
		compress.setType(event.getType());
		
		return compress;
	}

	/**
	 * �Ƿ�ѹ���澯
	 * 
	 * �ȴ�ѹ�����в��ң����ҵ������ѹ����
	 * ������ӵ�ǰ�澯���в���
	 * 
	 * @param alarmIndicators
	 * 						
	 * @param nodeid
	 * 
	 * @param checkEventLevel
	 * @return
	 * 			true:  ����ѹ��
	 * 			false: ��ѹ�������Է��澯
	 */
	private int isCompressCheckEvent(List<AlarmIndicators> alarmIndicators,
			String nodeid, int checkEventLevel) {
		int eventNum = 0;
		if(alarmIndicators.isEmpty()){
			return eventNum;
		}
		AlarmIndicators indicators = alarmIndicators.get(0);
		String sql= "";
		StringBuffer sb = new StringBuffer();
		sb.append(" where node_id='" + nodeid + "' ");
		sb.append(" and type='" + indicators.getType() + "' ");
		sb.append(" and subtype='" + indicators.getSubtype() + "'");
		if(checkEventLevel >= 0) {
			sb.append(" and level=" + checkEventLevel );
		}
		sb.append(" and (name='");
		for(int i=0; i<alarmIndicators.size(); i++) {
			sb.append(alarmIndicators.get(i).getName());
			sb.append("' or name='");
		}
		
		sql = sb.substring(0, sb.length()-11);
		sql += "')";
		
		CheckEventCompressDao dao = new CheckEventCompressDao();
		
		try {
			String cnt = dao.getCountByWhere(sql.toString());
			if(cnt != null && !"".equals(cnt)) {
				eventNum = Integer.parseInt(cnt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		return eventNum;
	}
	
	private int isCompressCheckEvent(AlarmIndicatorsNode alarmIndicatorsNode, 
					int checkEventLevel){
		StringBuffer sb = new StringBuffer();
		
		sb.append(" where node_id=" + alarmIndicatorsNode.getNodeid() + " ");
		sb.append(" and type='" + alarmIndicatorsNode.getType() + "' ");
		sb.append(" and subtype='" + alarmIndicatorsNode.getSubtype() + "'");
		if(checkEventLevel >= 0) {
			sb.append(" and level=" + checkEventLevel );
		}
		sb.append(" and name='" + alarmIndicatorsNode.getName() + "'");
		
		CheckEventCompressDao dao = new CheckEventCompressDao();
		int eventNum = 0;
		try{
			List list = dao.findByCondition(sb.toString());
			if(!list.isEmpty()) {
				eventNum = list.size();
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
	
		return eventNum;
	}
	
	private int isCompressCheckEvent(String type,String subtype,String name,int nodeid, 
			int checkEventLevel){
		if(null == type || type.trim().length()==0
				|| null == subtype || subtype.trim().length() == 0) {
			return 0;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(" where node_id='" + nodeid + "' ");
		if(null != type && type.trim().length()>0){
			sb.append(" and type='" + type + "' ");
		}
		if(null != subtype && type.trim().length()>0){
			sb.append(" and subtype='" + subtype + "'");
		}
		if(checkEventLevel >= 0) {
			sb.append(" and level=" + checkEventLevel );
		}
		sb.append(" and name='" + name + "'");
		
		CheckEventCompressDao dao = new CheckEventCompressDao();
		int eventNum = 0;
		try{
			List list = dao.findByCondition(sb.toString());
			if(list != null && list.size() > 0) {
				eventNum = list.size();
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		return eventNum;
	}
	
	/**
	 * 
	 * @param nodeid
	 * @return
	 */
	private AlarmDeviceDependence getPreviousDevice(int nodeid) {
		AlarmDeviceDependence alarmDeviceDependence = new AlarmDeviceDependence();
		AlarmDeviceDependenceDao dao = new AlarmDeviceDependenceDao();
		try{
			String condition = " where nodeid=(select fid FROM "
				+ " nms_alarm_device_dependence WHERE nodeid=" + nodeid + ")";
			List<AlarmDeviceDependence> list = dao.findByCondition(condition);
			if(list != null && !list.isEmpty()){
				alarmDeviceDependence = list.get(0);
			}
			//salarmDeviceDependence = (AlarmDeviceDependence) dao.findByCondition(condition).get(0);
		}catch (Exception e) {
			e.printStackTrace();
			return alarmDeviceDependence;
		} finally {
			dao.close();
		}
		return alarmDeviceDependence;
	}
	
	/**
	 * �����ڵ�ǰ�澯����
	 * 
	 * @param event
	 */
	private void saveCheckEvent(CheckEvent event) {
		logger.info("�����ڵ�ǰ�澯���С���");
		CheckEventDao dao = new CheckEventDao();
		try{
			dao.save(event);
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
	//	compressInsertCheckEvent(event);	// ������ѹ������
	}
	
//	public static void main(String[] args) {
//		AlarmLinkNodeServices ser = new AlarmLinkNodeServices();
//		List<AlarmIndicators> alarmIndicators = new ArrayList<AlarmIndicators>();
//		for(int i=0; i<4; i++){
//			AlarmIndicators alarm = new AlarmIndicators();
//			alarm.setName("tablespace");
//			alarm.setType("db");
//			alarm.setSubtype("oracle");
//			alarmIndicators.add(alarm);
//		}
//		ser.isCompressCheckEvent(alarmIndicators, "123", -1);
//	}
}
