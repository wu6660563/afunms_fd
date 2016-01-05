package com.afunms.event.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.SysLogger;
import com.afunms.event.model.CheckEvent;
import com.afunms.event.model.IManagerTrap;

/**
 * 华为SDH北向接口trap实体 数据持久层
 * 
 * @author yag
 *
 */
public class IManagerTrapDao extends BaseDao implements DaoInterface {

	public static void main(String[] args) {
		IManagerTrapDao dao = new IManagerTrapDao();
		IManagerTrap trap = new IManagerTrap();
		trap.setName("银川核心局");
		dao.save(trap);
		dao.close();
	}
	
	private static SysLogger logger = SysLogger.getLogger(IManagerTrapDao.class);
	
	public IManagerTrapDao(){
		super("nms_imanager_trap");
	}
	
	@Override
	public BaseVo loadFromRS(ResultSet rs) {
		
		IManagerTrap manager = new IManagerTrap();
		try {
				manager.setId(rs.getInt("id"));
				manager.setName(rs.getString("name"));
				manager.setEventName(rs.getString("eventname"));
				manager.setDeviceType(rs.getString("device_type"));
				manager.setDeviceIP(rs.getString("device_ip"));
				manager.setElementInstance(rs.getString("element_instance"));
				manager.setNetworkManagementType(rs.getString("network_management_type"));
				manager.setAlarmCreateTime(rs.getString("alarm_create_time"));
				manager.setReasonOfCausingAlarm(rs.getString("reason_of_causing_alarm"));
				manager.setAlarmLevel(rs.getString("alarm_level"));
				manager.setAlarmInfo(rs.getString("alarm_info"));
				manager.setAlarmAdditionalInfo(rs.getString("alarm_additional_info"));
				manager.setAlarmFlag(rs.getString("alarm_flag"));
				manager.setAlarmFunctionType(rs.getString("alarm_function_type"));
				manager.setAlarmNumber(rs.getString("alarm_number"));
				manager.setAdviceOfReparingAlarm(rs.getString("advice_of_reparing_alarm"));
				manager.setResourceID(rs.getString("resource_id"));
				manager.setCollecttime(rs.getString("collecttime"));
		} catch (SQLException e) {
			logger.error("IManagerTrapDao.loadFromRS()",e);
		}
		return manager;
	}

	public boolean save(BaseVo basevo) {
		IManagerTrap vo = (IManagerTrap) basevo;
        StringBuffer sql = new StringBuffer(100);
        sql.append("insert into nms_imanager_trap(name,eventname,device_type,device_ip,element_instance,network_management_type,alarm_create_time," +
        		" reason_of_causing_alarm,alarm_level,alarm_info,alarm_additional_info,alarm_flag,alarm_function_type,alarm_number," +
        		" advice_of_reparing_alarm,resource_id,collecttime)values(");
        sql.append("'");
        sql.append(vo.getName());
        sql.append("','");
        sql.append(vo.getEventName());
        sql.append("','");
        sql.append(vo.getDeviceType());
        sql.append("','");
        sql.append(vo.getDeviceIP());
        sql.append("','");
        sql.append(vo.getElementInstance());
        sql.append("','");
        sql.append(vo.getNetworkManagementType());
        sql.append("','");
        sql.append(vo.getAlarmCreateTime());
        sql.append("','");
        sql.append(vo.getReasonOfCausingAlarm());
        sql.append("','");
        sql.append(vo.getAlarmLevel());
        sql.append("','");
        sql.append(vo.getAlarmInfo());
        sql.append("','");
        sql.append(vo.getAlarmAdditionalInfo());
        sql.append("','");
        sql.append(vo.getAlarmFlag());
        sql.append("','");
        sql.append(vo.getAlarmFunctionType());
        sql.append("','");
        sql.append(vo.getAlarmNumber());
        sql.append("','");
        sql.append(vo.getAdviceOfReparingAlarm());
        sql.append("','");
        sql.append(vo.getResourceID());
        sql.append("','");
        sql.append(vo.getCollecttime());
        sql.append("')");
        logger.info(sql.toString());
        return saveOrUpdate(sql.toString());
	}

	public boolean update(BaseVo vo) {
		// TODO Auto-generated method stub
		return false;
	}

	
	
}
