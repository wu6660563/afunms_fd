package com.afunms.biosreport.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.afunms.biosreport.BiosConstant;
import com.afunms.biosreport.model.BiosReportBaseModel;
import com.afunms.biosreport.model.NetDeviceReport;
import com.afunms.biosreport.model.WindowsServerReportModel;
import com.afunms.indicators.model.NodeDTO;

/**
 * windows����������־û����ݲ�����
 * 
 * @author Administrator
 *
 */
public class WindowsServerReportDao extends BiosReportBaseDao{

	/**
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public List<WindowsServerReportModel> getDevice(String start, String end){
		ArrayList<WindowsServerReportModel> device = new ArrayList<WindowsServerReportModel>();
		List<NodeDTO> list = super.getNodesByKind(BiosConstant.getInstance().WINDOWS_SERVER);
		int nx = 0;
		for(NodeDTO n : list){
			device.add(createNetDevice(n, start, end));
//			nx++;
//			if(nx>2){
//				break;
//			}
		}
//		device.add(create());
		return device;
	}
	
	public WindowsServerReportModel create(){
		WindowsServerReportModel netDevice = new WindowsServerReportModel("192.168.201.5","��Ա����","");
		netDevice.setNodeid("123");
		netDevice.setAlarmCommon("23");
		netDevice.setAlarmSerious("12");
		netDevice.setAlarmUrgency("111");
		netDevice.setAvergeConnectivity("12.1");
		//netDevice.setCollectTime(createTime());
		netDevice.setCpuAvgVal("1.2");
		netDevice.setCpuMaxVal("2.3");
		netDevice.setDiskSpaceUsingAvgVal("23.4");
		netDevice.setPhyMemAvgVal("25.5");
		netDevice.setPhyMemMaxVal("25.521212");
		netDevice.setVirMemAvgVal("1.1");
		netDevice.setVirMemMaxVal("1.1");
		return netDevice;
		
	}
	
	/**
	 * ���챨���еĵ��������豸��������Ϣ
	 * 
	 * @param node
	 * @param start
	 * @param end
	 * @return
	 */
	public WindowsServerReportModel createNetDevice(NodeDTO node, String start, String end ){
		WindowsServerReportModel netDevice = new WindowsServerReportModel(node.getIpaddress(),node.getName(),"");
		
		String ip = node.getIpaddress();
		String _ip = ip.replace('.', '_');
		String cpuTable = "cpu" + _ip;
		
		netDevice.setNodeid(node.getNodeid());
		netDevice.setCollectTime(createTime(start,end));
		
		// ��ȡCPUͳ����Ϣ
		Map<String,String> cpuMap = getCommonVal(getCommonSQL(cpuTable, start, end));
	    netDevice.setCpuMaxVal(cpuMap.get("max"));
	    netDevice.setCpuAvgVal(cpuMap.get("avg"));
	    
	    // ��ȡƽ����ͨ��
	    String pingTable = "ping" + _ip;
	    Map<String,String> pingMap = getCommonVal(getPingSQL(pingTable, start, end));
	    netDevice.setAvergeConnectivity(pingMap.get("avg"));
	    
	    // �����ڴ�������
	    String netMemTable = "memory" + get_IP(node.getIpaddress());
	    Map<String,String> phyMemMap = getCommonVal(getHostPhyMemSQL(netMemTable, start, end));
	    netDevice.setPhyMemAvgVal(phyMemMap.get("avg"));
	    netDevice.setPhyMemMaxVal(phyMemMap.get("max"));
	    
	    // �����ڴ�������
	    Map<String,String> virMemMap = getCommonVal(getHostVirMemSQL(netMemTable, start, end));
	    netDevice.setVirMemAvgVal(virMemMap.get("avg"));
	    netDevice.setVirMemMaxVal(virMemMap.get("max"));

	    // ���̿ռ�������
	    String diskTable = "disk" + get_IP(node.getIpaddress());
	    if(tableIsExist(diskTable)){
	    	Map<String,String> diskMap = getCommonVal(getCommonSQL(diskTable, start, end));
	    	netDevice.setDiskSpaceUsingAvgVal(diskMap.get("avg"));
	    }	    
	    System.out.println("IP"+node.getIpaddress() + " nodeid " + node.getNodeid()
	    		+ " subtype:" + node.getType());
	    
	    // �澯��
	    Map<String,String> alarmMap = getAlarmValue(Integer.parseInt(node.getNodeid()), node.getType(), start, end);
	    netDevice.setAlarmCommon(alarmMap.get("1")); // ��ͨ�澯��
	    netDevice.setAlarmSerious(alarmMap.get("2")); // ���ظ澯��
	    netDevice.setAlarmUrgency(alarmMap.get("3")); // �����澯��
		
	    System.out.println(netDevice.toString());
	    
		return netDevice;
	}

	/**
	 * �������� system_bios_windows_server
	 * 
	 * @param device
	 */
	public boolean insertData(WindowsServerReportModel vo){
		
		boolean flag = true;
		StringBuffer sql = new StringBuffer();
		sql.append("insert into system_bios_windows_server (" +
				"nodeid, collecttime, ip_address, alias, ping, disk_using_avg,cpu_avg, cpu_max, phy_mem_avg, phy_mem_max, "
			  + "vir_mem_avg, vir_mem_max, alarm_common, alarm_serious, alarm_urgency) values('");
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
		sql.append(vo.getDiskSpaceUsingAvgVal());
		sql.append("','");
		sql.append(vo.getCpuAvgVal());
		sql.append("','");
		sql.append(vo.getCpuMaxVal());
		sql.append("','");
		sql.append(vo.getPhyMemAvgVal());
		sql.append("','");
		sql.append(vo.getPhyMemMaxVal());
		sql.append("','");
		sql.append(vo.getVirMemAvgVal());
		sql.append("','");
		sql.append(vo.getVirMemMaxVal());
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
	 * �����������ڴ�SQL
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
	 * �����������ڴ�SQL
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
		System.out.println(sql);
		return sql;
	}
}
