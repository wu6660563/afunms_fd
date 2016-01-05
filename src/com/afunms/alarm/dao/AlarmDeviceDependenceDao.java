package com.afunms.alarm.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.afunms.alarm.model.AlarmDeviceDependence;
import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.DBManager;

/**
 * 告警联动分析功能数据持久层
 * 
 * 
 * @author yag
 *
 */
public class AlarmDeviceDependenceDao extends BaseDao implements DaoInterface{

	public AlarmDeviceDependenceDao(){
		super("nms_alarm_device_dependence");
	}
	
	@Override
	public BaseVo loadFromRS(ResultSet rs) {
		AlarmDeviceDependence node = new AlarmDeviceDependence();
		try{
				node.setId(rs.getInt("id"));
				node.setFid(rs.getInt("fid"));
				node.setType(rs.getString("type"));
				node.setSubtype(rs.getString("subtype"));
				node.setNodeid(rs.getInt("nodeid"));
				node.setLinkPort(rs.getString("link_port"));
				node.setIpAddress(rs.getString("ip_address"));
				node.setDeviceName(rs.getString("device_name"));
				node.setNodeLevel(rs.getInt("node_level"));
		
		} catch(Exception e) {
			e.printStackTrace();
		}
		return node;
	}
	
	public boolean save(BaseVo vo) {
		AlarmDeviceDependence node = (AlarmDeviceDependence)vo;
		StringBuffer sql = new StringBuffer();
		sql.append("insert into nms_alarm_device_dependence(fid,type,subtype,nodeid,link_port,ip_address,device_name,node_level)"
				+ " values('");
		sql.append(node.getFid());
		sql.append("','" + node.getType());
		sql.append("','" + node.getSubtype() + "','");
		sql.append(node.getNodeid() + "','");
		sql.append(node.getLinkPort() + "','");
		sql.append(node.getIpAddress() + "','");
		sql.append(node.getDeviceName() + "','");
		sql.append(node.getNodeLevel());
		sql.append("')");
		return super.saveOrUpdate(sql.toString());
	}

	public boolean update(BaseVo vo) {
		AlarmDeviceDependence node = (AlarmDeviceDependence)vo;
		StringBuffer sql = new StringBuffer();
		sql.append("update nms_alarm_device_dependence set fid="); 
		sql.append(node.getFid());
		sql.append(",type='" + node.getType() + "',");
		sql.append(" subtype='" + node.getSubtype() + "',");
		sql.append(" nodeid=" + node.getNodeid() + ",");
		sql.append(" link_port='" + node.getLinkPort() + "',");
		sql.append(" ip_address='" + node.getIpAddress() + "',");
		sql.append(" device_name='" + node.getDeviceName() + "',");
		sql.append(" node_level=" + node.getNodeLevel() + "");
		sql.append(" where id=" + node.getId());
		return super.saveOrUpdate(sql.toString());
	}
	
	public boolean empty(){
		String sql = "truncate table nms_alarm_device_dependence";
		return super.saveOrUpdate(sql);
	}

	public boolean delete(String[] id) {
		return super.delete(id);
	}
	
	/**
	 * 根据原来的告警id进行查询
	 * 
	 * @param id
	 * 			alarmid
	 * @return
	 * 			List<AlarmDeviceDependence>
	 */
	public List<AlarmDeviceDependence> queryByAlarmID(int id) {
		List<AlarmDeviceDependence> list = new ArrayList<AlarmDeviceDependence>();
		if (id < 0) {
			return list;
		}
		String condition = " where alarmid=" + id;
		return super.findByCondition(condition);
	}
	
	public List<Integer> queryLinkIdByAlarmID(int id) throws SQLException {

		List<Integer> linkIdList = new ArrayList<Integer>();
		DBManager db = new DBManager();
		String sql = "select distinct(linkid) as linkid from "
				+ " nms_alarm_link_node where alarmid=" + id;
		ResultSet rs = null;
		try {
			rs = db.executeQuery(sql);
			while(rs.next()){
				linkIdList.add(rs.getInt("linkid"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} finally {
				db.close();
			}
		}
		
		return linkIdList;
	}
	
	
	public static void main(String[] args) {
		AlarmDeviceDependence vo = new AlarmDeviceDependence();
		vo.setId(1);
		vo.setFid(3);
		vo.setType("db");
		vo.setSubtype("oracle");
		vo.setNodeid(123);
		vo.setIpAddress("192.168.1.12");
		vo.setLinkPort("224");
		vo.setDeviceName("监测数据库测试");
		AlarmDeviceDependenceDao dao = new AlarmDeviceDependenceDao();
		try{
			//dao.update(vo);
			List<AlarmDeviceDependence> list = dao.loadAll();
			for(int i=0; i<list.size(); i++){
			}
		} finally {
			dao.close();
		}
		//new AlarmDeviceDependenceDao().test(",2,,4,,5,,6,");
	}

	

	
}
