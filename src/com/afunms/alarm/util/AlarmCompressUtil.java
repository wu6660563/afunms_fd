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
 * 		告警压制功能
 * 
 * 		描述：
 *         本功能结合告警链路组告警机制对告警进行压制，主要解决所监测设备系统之间
 *      的检测指标项具有优先级和必要依赖关系时，压制了不必要产生的告警信息。从而使得
 *      监测对产生故障的设备的定位更加准确清晰。
 *      
 *      设计思路：
 *         本功能是在系统之前告警处理的基础上进行，提供的公开处理方法为
 *         public void compressCheckEvent(AlarmIndicatorsNode nm, CheckEvent event);
 *         需要传递AlarmIndicatorsNode 和 CheckEvent 参数，根据这两个参数，查看当前告警是否具备了
 *         压缩条件，具备则进行压缩处理，不具备则查询比它优先级高的指标进行检查，如果依旧不具备压缩条件则
 *         查询它所在设备之上的设备的依赖设备进行压缩。
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
     * 日志
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
		event.setContent("测试999");
		
		//AlarmLinkNodeServices ser = new AlarmLinkNodeServices();
		//ser.compressCheckEvent(nm, event);
		//ser.compressInsertCheckEvent(event);
		//ser.compressUpdateCheckEvent(event);
		//ser.getHighPriorityLevelIndicators(nm);
		System.out.println("\n测试完毕！");
	}
	
	
	/**
	 * 压缩告警方法
	 * 
	 * @param nm
	 * 
	 * @param event
	 * 
	 */
	public int compressCheckEvent(AlarmIndicatorsNode nm, CheckEvent event) {	
		int returnFlag = 0;
		/**
		 * 	检查该类型的告警是否被压缩，压缩则更新，
		 * 	否则，查询优先级高的指标告警
		 */
		int num = 0;
		
		// 查询比当前告警优先级较高的告警项进行压缩处理
		compressInsertCheckEvent(event);
		num = isCompressCheckEvent(
				getHighPriorityLevelIndicators(nm),nm.getNodeid(),event.getAlarmlevel());
		if(num > 0) {
			// 在压缩表中存在比当前告警优先级高的告警信息，则进入压制程序
			// 将当前告警信息新增到压缩表中
			returnFlag = 0;
		} else {
			// 当前压缩表中不存在压缩信息 
			// 则进行设备依赖关系查找
			
			//联关系存在nms_alarm_device_dependence表里，需要自己设定基准设备
			//隐藏该功能，目的防止失误修改基准设备
			//拓扑图创建完毕，关联之后，该功能则无需使用
			//设定地方为：告警方式设定-->list.jsp(258 line)--><a href="#" onclick="relatedNode()" style="display:inline">关联设备</a>修改为可见，然后再设定
			AlarmDeviceDependence previousAlarmDevice = 
				getPreviousDevice(Integer.parseInt(nm.getNodeid()));
			// 查看上行设备网络是否联动
			num  = isCompressCheckEvent(previousAlarmDevice.getType(),previousAlarmDevice.getSubtype(),
					"ping", previousAlarmDevice.getNodeid(), -1);
			if(num > 0) {
				returnFlag = 0;
			} else {
				// 产生告警
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
		
		// 按照指标依赖关系判定其是否告警
		for(int i=0; i<alarm.size(); i++) {
			if(alarm.get(i).getName().equals(nm.getName())) {
				alarmIndicators = alarm.get(i);
			}
		}
		
		//PriorityLevel优先级越高，数字越小
		for(int i=0; i<alarm.size(); i++) {
			if(alarm.get(i).getPriorityLevel() < alarmIndicators.getPriorityLevel()) {
				newAlarm.add(alarm.get(i));
			}
		}
		return newAlarm;
	}
	
	/**
	 * 更新压缩告警表
	 * 
	 * @param event
	 */
	private void compressUpdateCheckEvent(CheckEvent event) {
		CheckEventCompress compress = eventToCompress(event);
		CheckEventCompressDao checkEventCompressDao = new CheckEventCompressDao();
		try{
			// 更新时不更新firsttime字段
			checkEventCompressDao.updateExceptFirsttime(compress);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			checkEventCompressDao.close();
		}
		logger.info("更新Compress:" + compress.toString());
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
	 * 根据类型和子类型获取其告警指标
	 * 
	 * @param type
	 * 			
	 * @param subType
	 * 
	 * @param enable
	 * 			1 表示指标可用 0 表示指标不可用
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
	 * CheckEvent转为CheckEventCompress
	 * 	
	 * 	firsttime字段与collecttime相同
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
	 * 是否压缩告警
	 * 
	 * 先从压缩表中查找，查找到则更新压缩表
	 * 否则，则从当前告警表中查找
	 * 
	 * @param alarmIndicators
	 * 						
	 * @param nodeid
	 * 
	 * @param checkEventLevel
	 * @return
	 * 			true:  可以压缩
	 * 			false: 不压缩，可以发告警
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
	 * 保存在当前告警表中
	 * 
	 * @param event
	 */
	private void saveCheckEvent(CheckEvent event) {
		logger.info("保存在当前告警表中。。");
		CheckEventDao dao = new CheckEventDao();
		try{
			dao.save(event);
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
	//	compressInsertCheckEvent(event);	// 保存在压缩表中
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
