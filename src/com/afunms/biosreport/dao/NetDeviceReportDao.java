package com.afunms.biosreport.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.biosreport.BiosConstant;
import com.afunms.biosreport.model.NetDeviceReport;
import com.afunms.business.model.BusinessNode;
import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.base.JspPage;
import com.afunms.common.util.CreateTableManager;
import com.afunms.common.util.DBManager;
import com.afunms.indicators.model.NodeDTO;

public class NetDeviceReportDao extends BiosReportBaseDao{

	
	public List<NetDeviceReport> getDevice(String start, String end){
		List<NetDeviceReport> device = new ArrayList<NetDeviceReport>();
		List<NodeDTO> list = super.getNodesByKind(BiosConstant.getInstance().NET_DEVICE);
		int nx=0;
		for(NodeDTO n : list){
			device.add(createNetDevice(n, start, end));
		}
		return device;
	}
	
	
	/**
	 * 构造报表中的单个网络设备的数据信息
	 * 
	 * @param node
	 * @param start
	 * @param end
	 * @return
	 */
	public NetDeviceReport createNetDevice(NodeDTO node, String start, String end ){
		NetDeviceReport netDevice = new NetDeviceReport(node.getIpaddress(),node.getName(),"");
			
		String ip = node.getIpaddress();
		String _ip = ip.replace('.', '_');
		String cpuTable = "cpu" + _ip;
		
		Date now = new Date();
		netDevice.setCollectTime(createTime(start,end));
		netDevice.setNodeid(node.getNodeid());
		
		// 获取CPU统计信息
		Map<String,String> cpuMap = getCommonVal(getCommonSQL(cpuTable, start, end));
	    netDevice.setCpuMaxVal(cpuMap.get("max"));
	    netDevice.setCpuAvgVal(cpuMap.get("avg"));
	    
	    // 获取平均连通率
	    String pingTable = "ping" + _ip;
	    Map<String,String> pingMap = getCommonVal(getPingSQL(pingTable, start, end));
	    netDevice.setAvergeConnectivity(pingMap.get("avg"));
	    
	    // 物理内存利用率
	    String netMemTable = "memory" + _ip;
	    Map<String,String> phyMemMap = getCommonVal(getNetMemSQL(netMemTable, start, end));
	    netDevice.setPhysicsMemoryAvgVal(phyMemMap.get("avg"));
	    netDevice.setPhysicsMemoryMaxVal(phyMemMap.get("max"));
	    
	    // 入口带宽利用率
	    String bandWidthTable = "allutilhdx" + _ip;
	    Map<String,String> inBandMap = getCommonVal(getNetInBandSQL(bandWidthTable, start, end));
	    netDevice.setInFlowAvgVal(inBandMap.get("avg"));
	    netDevice.setInFlowMaxVal(inBandMap.get("max"));
	    
	    // 出口带宽利用率
	    Map<String,String> outBandMap = getCommonVal(getNetOutBandSQL(bandWidthTable, start, end));
	    netDevice.setOutFlowAvgVal(outBandMap.get("avg"));
	    netDevice.setOutFlowMaxVal(outBandMap.get("max"));
	    
	    // 带宽综合平均利用率
	    Map<String,String> avgBandMap = getCommonVal(getNetAvgBandSQL(bandWidthTable, start, end));
	    if(avgBandMap.get("avg") != null){
	    	double avgBandRate = Double.parseDouble(avgBandMap.get("avg"));
	    	double inBandAvgVal = Double.parseDouble(inBandMap.get("avg"));
	    	double outBandAvgVal = Double.parseDouble(outBandMap.get("avg"));
	    	netDevice.setInFlowBandWidthRate(String.valueOf((inBandAvgVal / avgBandRate)*100)); // 入口带宽利用率
	    	netDevice.setOutFlowBandWidthRate(String.valueOf((outBandAvgVal / avgBandRate)*100)); // 出口带宽利用率
	    }
	   // System.out.println("IP"+node.getIpaddress() + " nodeid " + node.getNodeid()
	   // 		+ " subtype:" + node.getType());
	    
	    // 告警数
	    Map<String,String> alarmMap = getAlarmValue(Integer.parseInt(node.getNodeid()), node.getType(), start, end);
	    netDevice.setAlarmCommon(alarmMap.get("1")); // 普通告警数
	    netDevice.setAlarmSerious(alarmMap.get("2")); // 严重告警数
	    netDevice.setAlarmUrgency(alarmMap.get("3")); // 紧急告警数
		
	    
	   // System.out.println(netDevice.toString());
	    
	    // 插入数据
	    //insertData(netDevice,node.getNodeid());
	    
		return netDevice;
	}
	
	/**
	 * 插入数据 system_bios_net
	 * 
	 * @param device
	 */
	public boolean insertData(NetDeviceReport vo){
		
		boolean flag = true;
		StringBuffer sql = new StringBuffer();
		sql.append("insert into system_bios_net (" +
				"nodeid, collecttime, ip_address, alias, ping, cpu_avg, cpu_max, phy_mem_avg, phy_mem_max, "
			  + "out_flow_avg, out_flow_max, out_flow_bandwidth, in_flow_avg, in_flow_max, "
			  + "in_flow_bandwidth, alarm_common, alarm_serious, alarm_urgency) values('");
	    sql.append(vo.getNodeid());
		sql.append("','");
		sql.append(vo.getCollectTime());
		sql.append("','");
		sql.append(vo.getIpAddress());
		sql.append("','");
		sql.append(vo.getAliasName());
		sql.append("','");
		sql.append(vo.getAvergeConnectivity());
		sql.append("','");
		sql.append(vo.getCpuAvgVal());
		sql.append("','");
		sql.append(vo.getCpuMaxVal());
		sql.append("','");
		sql.append(vo.getPhysicsMemoryAvgVal());
		sql.append("','");
		sql.append(vo.getPhysicsMemoryMaxVal());
		sql.append("','");
		sql.append(vo.getOutFlowAvgVal());
		sql.append("','");
		sql.append(vo.getOutFlowMaxVal());
		sql.append("','");
		sql.append(vo.getOutFlowBandWidthRate());
		sql.append("','");
		sql.append(vo.getInFlowAvgVal());
		sql.append("','");
		sql.append(vo.getInFlowMaxVal());
		sql.append("','");
		sql.append(vo.getInFlowBandWidthRate());
		sql.append("','");
		sql.append(vo.getAlarmCommon());
		sql.append("','");
		sql.append(vo.getAlarmSerious());
		sql.append("','");
		sql.append(vo.getAlarmUrgency());
		sql.append("')");
		//System.out.println(sql);
		//String allipstr = vo.getBid()+"_"+vo.getId();
	   try{
		   saveOrUpdate(sql.toString());
	   }catch(Exception e){
		   flag = false;
	   }
	   return flag;
	
	}
	
	
	/**
	 * 获取通用数据的方法
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
	
	/**
	 * 网络设备 内存SQL
	 * @param table
	 * @param start
	 * @param end
	 * @return
	 */
	private String getNetMemSQL(String table,String start,String end){
		String sql = "select max(cast(thevalue as signed)) as max_val, avg(cast(thevalue as signed)) as avg_val, " +
		" min(cast(thevalue as signed)) as min_val from " + table +
		" where collecttime >= '"+ start + "'" +
		" and collecttime <= '" + end + "';";
		//System.out.println(sql);
		return sql;
	}
	
	/**
	 * 服务器物理内存SQL
	 * @param table
	 * @param start
	 * @param end
	 * @return
	 */
	private String getHostPhyMemSQL(String table,String start,String end){
		String sql = "select max(cast(thevalue as signed)) as max_val, avg(cast(thevalue as signed)) as avg_val, " +
		" min(cast(thevalue as signed)) as min_val from " + table +
		" where collecttime >= '"+ start + "'" +
		" and collecttime <= '" + end + "' " +
		" and subentity='PhysicalMemory';";
		//System.out.println(sql);
		return sql;
	}
	
	/**
	 * 服务器虚拟内存SQL
	 * @param table
	 * @param start
	 * @param end
	 * @return
	 */
	private String getHostVirMemSQL(String table,String start,String end){
		String sql = "select max(cast(thevalue as signed)) as max_val, avg(cast(thevalue as signed)) as avg_val, " +
		" min(cast(thevalue as signed)) as min_val from " + table +
		" where collecttime >= '"+ start + "'" +
		" and collecttime <= '" + end + "' " +
		" and subentity='VirtualMemory';";
		//System.out.println(sql);
		return sql;
	}
	
	
	/**
	 * 网络设备：综合入口流速
	 */
	private String getNetInBandSQL(String table,String start,String end){
		String sql = "select max(cast(thevalue as signed)) as max_val, avg(cast(thevalue as signed)) as avg_val, " +
		" min(cast(thevalue as signed)) as min_val from " + table +
		" where collecttime >= '"+ start + "'" +
		" and collecttime <= '" + end + "' " +
		" and subentity='AllInBandwidthUtilHdx';";
		//System.out.println(sql);
		return sql;
	}
	
	/**
	 * 网络设备：综合出口流速
	 */
	private String getNetOutBandSQL(String table,String start,String end){
		String sql = "select max(cast(thevalue as signed)) as max_val, avg(cast(thevalue as signed)) as avg_val, " +
		" min(cast(thevalue as signed)) as min_val from " + table +
		" where collecttime >= '"+ start + "'" +
		" and collecttime <= '" + end + "' " +
		" and subentity='AllOutBandwidthUtilHdx';";
		//System.out.println(sql);
		return sql;
	}
	
	/**
	 * 网络设备：综合平均流速
	 */
	private String getNetAvgBandSQL(String table,String start,String end){
		String sql = "select max(cast(thevalue as signed)) as max_val, avg(cast(thevalue as signed)) as avg_val, " +
		" min(cast(thevalue as signed)) as min_val from " + table +
		" where collecttime >= '"+ start + "'" +
		" and collecttime <= '" + end + "' " +
		" and subentity='AllBandwidthUtilHdx';";
		//System.out.println(sql);
		return sql;
	}
	
	public boolean delete(String[] id) {
		// TODO Auto-generated method stub
		return false;
	}

	public BaseVo findByID(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public JspPage getPage() {
		// TODO Auto-generated method stub
		return null;
	}

	public List listByPage(int curpage, int perpage) {
		// TODO Auto-generated method stub
		return null;
	}

	public List listByPage(int curpage, String where, int perpage) {
		// TODO Auto-generated method stub
		return null;
	}

	public List loadAll() {
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

	@Override
	public BaseVo loadFromRS(ResultSet rs) {
		// TODO Auto-generated method stub
		return null;
	}

}
