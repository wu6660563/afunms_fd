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
 * Aix �������־ò����ݲ�������
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
		AixServerReportModel vo = new AixServerReportModel("192.1.1.1","AIX�������","");
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
	 * ���챨���еĵ��������豸��������Ϣ
	 * 
	 * @param node
	 * @param start
	 * @param end
	 * @return
	 */
	public AixServerReportModel createNetDevice(NodeDTO node, String start, String end ){
		AixServerReportModel netDevice = new AixServerReportModel(node.getIpaddress(),node.getName(),"");
		
	//	resetNodeId(node);	// �ı�nodeid Ϊ�˸澯��ѯ
		
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
	    Map<String,String> swapMemMap = getCommonVal(getSwapMemSQL(netMemTable, start, end));
	    netDevice.setPageSpaceAvgVal(swapMemMap.get("avg"));
	    netDevice.setPageSpaceMaxVal(swapMemMap.get("max"));
	    
	    // �ļ�ϵͳ������
	    String fileSysTable = "disk" + get_IP(node.getIpaddress());
	    Map<String,String> fileSysMap = getCommonVal(getCommonSQL(fileSysTable, start, end));
	    netDevice.setFileSysUsingAvgVal(fileSysMap.get("avg"));
	    
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
	 * �������� system_bios_aix_server
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
	 * aix�����������ڴ�SQL
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
