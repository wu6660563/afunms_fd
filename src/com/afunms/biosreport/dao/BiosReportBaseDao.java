package com.afunms.biosreport.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.biosreport.BiosConstant;
import com.afunms.biosreport.model.BiosReportBaseModel;
import com.afunms.capreport.dao.BaseDaoImp;
import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;

/**
 * �԰z�ܱ���ģ�����ݻ�ȡ�Ļ��� dao
 * 
 * @author Administrator
 *
 */
public abstract class BiosReportBaseDao extends BaseDao implements DaoInterface{

	public static List<NodeDTO> nodeList = new ArrayList<NodeDTO>();
	
//	private static Logger log = Logger.getLogger(BiosReportBaseDao.class);
	private static SysLogger logger = SysLogger.getLogger(BiosReportBaseDao.class);

	public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	static {
		nodeList = getAllNodes();
	}
	
	/**
	 * 	�����豸���ͻ�ȡ�豸��Ϣ
	 * 	�豸���Ͱ�����windows_server\aix_server\db_oracle\db_sqlserver\net
	 * 
	 */
	public List<NodeDTO> getNodesByKind(String kind){
		
		if("".equals(kind.trim()) || kind == null){
			return null;
		}
	
		List<BaseVo> list = new ArrayList<BaseVo>();
		NodeUtil util = new NodeUtil();
		if(kind.equals(BiosConstant.getInstance().WINDOWS_SERVER)){
			list =  util.getHost_WindowsList();
		}else if(kind.equals(BiosConstant.getInstance().AIX_SERVAER)){
			list = util.getHost_AixList();
		}else if(kind.equals(BiosConstant.getInstance().DB_ORACLE)){
			list = util.getDB_OracleList();
		}else if(kind.equals(BiosConstant.getInstance().DB_SQLSERVER)){
			list = util.getDB_SQLServerList();
		}else if(kind.equals(BiosConstant.getInstance().NET_DEVICE)){
			list = util.getNetList();			
		}else{
			list = null;	// ���Ҵ���
			logger.info(getClass().getName()+"�z�ܱ����Զ������У�" + kind + "�����豸��Ϊnull!!!");
		}
		//print("list(getNodesByKinds) size:" + list.size());
		if(list != null){
			return util.conversionToNodeDTO(list);
		}else{
			return null;
		}
	}
	
	/**
	 * ��ȡϵͳ�����豸��Ϣ
	 * ʹ��NodeUtil���еķ���
	 * 
	 * @return List<NodeDTO>
	 */
	public static List<NodeDTO> getAllNodes(){
		List<BaseVo> list = new ArrayList<BaseVo>();
		NodeUtil util = new NodeUtil();
		list = util.getNodeByTyeAndSubtype("-1", "-1");
		if(list != null || list.size() > 0) {
			return util.conversionToNodeDTO(list);
		} else {
			return null;
		}
	}
	
	public String getNodeAlias(int id, String ip){
		if(id < 1 || ip == null || ip.trim().length() < 1){
			return "";
		}
		List<NodeDTO> nodes = getAllNodes();
		for(NodeDTO node : nodes) {
			if(node.getId() == id && node.getIpaddress().equals(ip)){
				return node.getName();
			}
		}
		return "";
	}
	
	/**
	 * ��ip�е�.��Ϊ_
	 * @param ip
	 * @return String
	 */
	public String get_IP(String ip){
		return ip.replace('.', '_');
	}
	
	/**
	 * ����ָ������ �� ip ��ñ���
	 * 
	 * @param type
	 * @param ip
	 * @return String
	 */
	public String getTable(String type, String ip){
		return type + get_IP(ip);
	}
	
	/**
	 * ��������Ϣ��ip\alias\alarm �ȣ���װ���豸ģ����
	 * 
	 * @param nodeDTO
	 * @return
	 */
	public void createBaseDeviceInfo(BiosReportBaseModel model, NodeDTO nodeDTO,
			String start, String end){
		if(nodeDTO != null){
			model.setIpAddress(nodeDTO.getIpaddress()); // ����IP��Ϣ
			model.setAliasName(nodeDTO.getName());		// ����
			Map<String,String> map = getCommonVal(		// ��ͨ��
					getCommonSQL(
						getTable(BiosConstant.getInstance().DATA_PING,nodeDTO.getIpaddress()),
						start, end));
			model.setAvergeConnectivity(map.get("avg")); // ƽ����ͨ��
			map = getAlarmValue(Integer.parseInt(nodeDTO.getNodeid()), nodeDTO.getType(), start, end);
			model.setAlarmCommon(map.get("1"));	// ��ͨ�澯��
			model.setAlarmSerious(map.get("2")); // ���ظ澯��
			model.setAlarmUrgency(map.get("3")); // �����澯��
		}
	}
	
	

	/**
	 * ��ѯ�����е�����ʱ��
	 * 
	 *  �˱��е��ֶ�Ϊ collecttime 
	 * @param table 
	 * @return
	 */
	public String getLatestTime(String table){
		if("".equals(table)){
			return "";
		}
		String sql = "select max(collecttime) as collecttime from " + table;
		
		Date newdate = new Date();
		try{
			logger.info(sql);
    		rs = conn.executeQuery(sql);
    		while(rs.next()){
    			newdate.setTime(rs.getTimestamp("collecttime").getTime());
    		}
    	}catch(SQLException e){
    		e.printStackTrace();
    		return "";
    	}
    	return sdf.format(newdate);
//    	
//    	Calendar cal = Calendar.getInstance();
//    	cal.setTime(newdate);
//    	logger.info(table + "����ʱ���ǣ�" 
//    			+ sdf.format(new Date(cal.getTimeInMillis() + BiosConstant.WEEK)));
//    	return sdf.format(new Date(cal.getTimeInMillis() + BiosConstant.WEEK));
	}
	
	public String createTime(String start,String end){
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		Calendar calStart = Calendar.getInstance();
		Calendar calEnd = Calendar.getInstance();
		try {
			calStart.setTime(sdf.parse(start));
			calEnd.setTime(sdf.parse(end));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long cha = calEnd.getTimeInMillis() - calStart.getTimeInMillis();
		cal.setTime(now);
		return sdf.format(new Date(calStart.getTimeInMillis()+(cha/2)));
	}
	
	/**
	 * ��ȡͨ�����ݵķ���
	 * 
	 * @param table
	 * @param start
	 * @param end
	 * @return
	 */
	public Map<String,String> getCommonVal(String sql){
		Map<String,String> map = new HashMap<String, String>();
		
		try {
			rs = conn.executeQuery(sql);
			while(rs.next()){
				String max = rs.getString("max_val");
				map.put("max", max);
				String avg = rs.getString("avg_val");
				map.put("avg", avg);
				String min = rs.getString("min_val");
				map.put("min", min);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map = null;
			return map;
		}
		return map;
	}
	
	public String getCommonSQL(String table,String start,String end){
		String sql = "select max(cast(thevalue as signed)) as max_val, avg(cast(thevalue as signed)) as avg_val, " +
		" min(cast(thevalue as signed)) as min_val from " + table +
		" where collecttime >= '"+ start + "'" +
		" and collecttime <= '" + end + "';";
		logger.info(sql);
		return sql;
	}
	
	
	/**
	 * 
	 * @param table
	 * @param start
	 * @param end
	 * @return
	 */
	public String getPingSQL(String table,String start,String end){
		String sql = "select max(cast(thevalue as signed)) as max_val, avg(cast(thevalue as signed)) as avg_val, " +
		" min(cast(thevalue as signed)) as min_val from " + table +
		" where collecttime >= '"+ start + "'" +
		" and collecttime <= '" + end + "' and entity='Utilization' AND subentity='ConnectUtilization';";
		logger.info(sql);
		return sql;
	}
	
	
	
	/**
	 * ��ȡ�澯����
	 * 
	 * @param nodeid
	 * @param start
	 * @param end
	 * @return
	 */
	public Map<String, String> getAlarmValue(int nodeid, String subtype, String start,String end){
		Map<String,String> map = new HashMap<String, String>();
		
		try {
			rs = conn.executeQuery(getAlarmSQL(nodeid, subtype, start, end));
			while(rs.next()){
				String level = rs.getString("level1");
				String sum = String.valueOf(rs.getInt("sums"));
						
				if("1".equals(level)){
					map.put("1", sum);
				}else if("2".equals(level)){
					map.put("2", sum);
				}else if("3".equals(level)){
					map.put("3", sum);
				}else{
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map = null;
			return map;
		}
		return map;
	}
	
	private String getAlarmSQL(int nodeid, String subtype, String start, String end){
			
		String sql =  "select sum(count) as sums , level1 from " + BiosConstant.getInstance().TABLE_ALARM_HOUR
				+ " where nodeid="+ nodeid  
				+ " and subtype='" + subtype
				+ "' and recordtime >= '"+ start 
				+ "' and recordtime <= '" + end + "' group by level1;";
		logger.info(sql);
		return sql;
	}
	
	
	/**
     * 	���Դ���������ݿ����Ƿ���ڣ���Ҫ�������ݹ鵵����
     * @param tableName
     * @return 
     */
    public boolean tableIsExist(String tableName){
    	if(tableName != null && !"".equals(tableName.trim())){
    		rs = conn.executeQuery("select * from information_schema.tables WHERE table_name = '"+ tableName+"';");
    		try {
				while(rs.next()){
					return true;
				}
				return false;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				SysLogger.error("ϵͳ���Ե�ǰ��"+tableName +"�Ƿ���ڣ�in DataArchivingNodeDao.tableIsExist()", e);
			} 
    	}else {
    		return false;
    	}
    	return false;
    }
	
	public void print(String msg){
		logger.info(msg);
	}
	
	
	@Override
	public BaseVo loadFromRS(ResultSet rs) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean save(BaseVo vo) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean update(BaseVo vo) {
		// TODO Auto-generated method stub
		return false;
	}

}
