package com.afunms.temp.dao;

import java.sql.ResultSet;
import java.util.List;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.SysLogger;
import com.afunms.temp.model.DeviceNodeTemp;

public class DeviceTempDao extends BaseDao implements DaoInterface {

	public DeviceTempDao() {
		super("nms_device_data_temp");	   	  
	}

	public boolean deleteByIp(String ip) {
		boolean result = false;
	    try
	    {
	    	conn.executeUpdate("delete from nms_device_data_temp where ip='" + ip + "'");
	    	result = true;
	    }
	    catch(Exception ex)
	    {
	        SysLogger.error("Error in DeviceTempDao.deleteByIp(String ip)",ex);
	    }
	    finally
	    {
	        conn.close();
	    }
	    return result;
	}
	public BaseVo loadFromRS(ResultSet rs) {
		DeviceNodeTemp vo = new DeviceNodeTemp();
		try
		{
			vo.setIp(rs.getString("ip"));
			vo.setNodeid(rs.getString("nodeid"));
			vo.setCollecttime(rs.getString("collecttime"));
			vo.setSubtype(rs.getString("subtype"));
			vo.setType(rs.getString("type"));
			vo.setName(rs.getString("name"));
			vo.setDeviceindex(rs.getString("deviceindex"));
			vo.setDtype(rs.getString("dtype"));
			vo.setStatus(rs.getString("status"));
		}
		catch(Exception e)
		{
			SysLogger.error("DeviceTempDao.loadFromRS()",e);
		}
	    return vo;
	}

	public boolean save(BaseVo baseVo) {
		DeviceNodeTemp vo = (DeviceNodeTemp)baseVo;		
	    StringBuffer sql = new StringBuffer(500);
	    sql.append("insert into nms_device_data_temp(nodeid,ip,type,subtype,name,deviceindex,dtype,status,collecttime)values('");
	    sql.append(vo.getNodeid());
	    sql.append("','");
	    sql.append(vo.getIp());
	    sql.append("','");
	    sql.append(vo.getType());
	    sql.append("','");
	    sql.append(vo.getSubtype());
	    sql.append("','");
	    sql.append(vo.getName().replace("\\", "/"));
	    sql.append("','");
	    sql.append(vo.getDeviceindex());
	    sql.append("','");
	    sql.append(vo.getDtype());
	    sql.append("','");
	    sql.append(vo.getStatus());
	    sql.append("','");
	    sql.append(vo.getCollecttime());
	    sql.append("')");
//	    System.out.println("sql.toString()=="+sql.toString());
	    return saveOrUpdate(sql.toString());
    }

	public boolean update(BaseVo vo) {
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public List<DeviceNodeTemp> getDeviceNodeTemp(String nodeid, String type, String subtype){
		StringBuffer sql = new StringBuffer();
		sql.append(" where nodeid='" + nodeid + "' and type='" + type + "' and subtype='" + subtype + "'");
//		System.out.println(sql.toString());
		return findByCondition(sql.toString());
	}

}
