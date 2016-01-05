package com.afunms.biosreport.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.afunms.biosreport.BiosConstant;
import com.afunms.biosreport.model.AixServerReportModel;
import com.afunms.biosreport.model.WindowsServerReportModel;
import com.afunms.indicators.model.NodeDTO;

/**
 * Aix 服务器持久层数据操作方法
 * 
 * @author Administrator
 *
 */
public class AixServerReportDao extends BiosReportBaseDao{

	public List<AixServerReportModel> getDevice(String start, String end){
		ArrayList<AixServerReportModel> device = new ArrayList<AixServerReportModel>();
		List<NodeDTO> list = super.getNodesByKind(BiosConstant.getInstance().AIX_SERVAER);
		int nx=0;
		for(NodeDTO n : list){
			device.add(createNetDevice(n, start, end));
//			nx++;
//			if(nx >2){
//				break;
//			}
		}
//		device.add(create());
		return device;
	}
	
	public AixServerReportModel create(){
		AixServerReportModel vo = new AixServerReportModel("192.1.1.1","AIX测试添加","");
		vo.setAlarmCommon("1");
		vo.setAlarmSerious("12");
		vo.setAlarmUrgency("1233");
		//vo.setCollectTime(createTime());
		vo.setCpuAvgVal("56.3");
		vo.setNodeid("111");
		vo.setCpuMaxVal("12.2312");
		vo.setFileSysUsingAvgVal("66.553");
		vo.setPhyMemAvgVal("32.1");
		vo.setPhyMemMaxVal("32.1");
		return vo;
	}
	
	/**
	 * 构造报表中的单个网络设备的数据信息
	 * 
	 * @param node
	 * @param start
	 * @param end
	 * @return
	 */
	public AixServerReportModel createNetDevice(NodeDTO node, String start, String end ){
		AixServerReportModel netDevice = new AixServerReportModel(node.getIpaddress(),node.getName(),"");
		
	//	resetNodeId(node);	// 改变nodeid 为了告警查询
		
		String ip = node.getIpaddress();
		String _ip = ip.replace('.', '_');
		String cpuTable = "cpu" + _ip;
		
		netDevice.setNodeid(node.getNodeid());
		netDevice.setCollectTime(createTime(start,end));
		
		// 获取CPU统计信息
		Map<String,String> cpuMap = getCommonVal(getCommonSQL(cpuTable, start, end));
	    netDevice.setCpuMaxVal(cpuMap.get("max"));
	    netDevice.setCpuAvgVal(cpuMap.get("avg"));
	    
	    // 获取平均连通率
	    String pingTable = "ping" + _ip;
	    Map<String,String> pingMap = getCommonVal(getPingSQL(pingTable, start, end));
	    netDevice.setAvergeConnectivity(pingMap.get("avg"));
	    
	    // 物理内存利用率
	    String netMemTable = "memory" + get_IP(node.getIpaddress());
	    Map<String,String> phyMemMap = getCommonVal(getHostPhyMemSQL(netMemTable, start, end));
	    netDevice.setPhyMemAvgVal(phyMemMap.get("avg"));
	    netDevice.setPhyMemMaxVal(phyMemMap.get("max"));

	    // 交换内存利用率
	    Map<String,String> swapMemMap = getCommonVal(getSwapMemSQL(netMemTable, start, end));
	    netDevice.setPageSpaceAvgVal(swapMemMap.get("avg"));
	    netDevice.setPageSpaceMaxVal(swapMemMap.get("max"));
	    
	    // 文件系统利用率
	    String fileSysTable = "disk" + get_IP(node.getIpaddress());
	    Map<String,String> fileSysMap = getCommonVal(getCommonSQL(fileSysTable, start, end));
	    netDevice.setFileSysUsingAvgVal(fileSysMap.get("avg"));
	    
	    System.out.println("IP"+node.getIpaddress() + " nodeid " + node.getNodeid()
	    		+ " subtype:" + node.getType());
	    
	    // 告警数
	    Map<String,String> alarmMap = getAlarmValue(Integer.parseInt(node.getNodeid()), node.getType(), start, end);
	    netDevice.setAlarmCommon(alarmMap.get("1")); // 普通告警数
	    netDevice.setAlarmSerious(alarmMap.get("2")); // 严重告警数
	    netDevice.setAlarmUrgency(alarmMap.get("3")); // 紧急告警数
		
	    System.out.println(netDevice.toString());
	    
		return netDevice;
	}

	/**
	 * 插入数据 system_bios_aix_server
	 * 
	 * @param device
	 */
	public boolean insertData(AixServerReportModel vo){
		
		boolean flag = true;
		StringBuffer sql = new StringBuffer();
		sql.append("insert into system_bios_aix_server (" +
				"nodeid, collecttime, ip_address, alias, ping, file_sys_using_avg, cpu_avg, cpu_max, phy_mem_avg, phy_mem_max, "
			  + "page_space_avg, page_space_max, alarm_common, alarm_serious, alarm_urgency) values('");
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
		sql.append(vo.getFileSysUsingAvgVal());
		sql.append("','");
		sql.append(vo.getCpuAvgVal());
		sql.append("','");
		sql.append(vo.getCpuMaxVal());
		sql.append("','");
		sql.append(vo.getPhyMemAvgVal());
		sql.append("','");
		sql.append(vo.getPhyMemMaxVal());
		sql.append("','");
		sql.append(vo.getPageSpaceAvgVal());
		sql.append("','");
		sql.append(vo.getPageSpaceMaxVal());
		sql.append("','");
		sql.append(vo.getAlarmCommon());
		sql.append("','");
		sql.append(vo.getAlarmSerious());
		sql.append("','");
		sql.append(vo.getAlarmUrgency());
		sql.append("')");
		System.out.println(sql);
	   try{
		   saveOrUpdate(sql.toString());
	   }catch(Exception e){
		   flag = false;
	   }
	   return flag;
	
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
		System.out.println(sql);
		return sql;
	}
	/**
	 * aix服务器交换内存SQL
	 * @param table
	 * @param start
	 * @param end
	 * @return
	 */
	private String getSwapMemSQL(String table,String start,String end){
		String sql = "select max(cast(thevalue as signed)) as max_val, avg(cast(thevalue as signed)) as avg_val, " +
		" min(cast(thevalue as signed)) as min_val from " + table +
		" where collecttime >= '"+ start + "'" +
		" and collecttime <= '" + end + "' " +
		" and subentity='SwapMemory';";
		System.out.println(sql);
		return sql;
	}
	
	
}
