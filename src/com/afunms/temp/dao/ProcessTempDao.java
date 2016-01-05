package com.afunms.temp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.CommonUtil;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.SysLogger;
import com.afunms.detail.reomte.model.ProcessInfo;
import com.afunms.temp.model.NodeTemp;
import com.afunms.temp.model.ServiceNodeTemp;

public class ProcessTempDao extends BaseDao implements DaoInterface {

	public ProcessTempDao(String ipaddress) {
		super("nms_process_data_temp" + CommonUtil.doip(ipaddress));	   	  
	}

	/**
	 * ɾ��һ����¼
	 */
	public boolean deleteByNodeIdSindex(String nodeid,String sid,String subentity) {
		boolean result = false;  
	    try   
	    {
	    	conn.executeUpdate("delete from " + table + " where nodeid='" + nodeid + "' and sindex='" + sid  + "' and subentity='" + subentity + "'");
	    	result = true;
	    }
	    catch(Exception ex)
	    {
	        SysLogger.error("Error in ProcessTempDao.deleteByNodeId(String nodeid)",ex);
	    }
	    finally
	    {
	        conn.close();
	    }
	    return result;
	}
	public boolean deleteByIp(String ip) {
		boolean result = false;
	    try
	    {
	    	conn.executeUpdate("delete from " + table + " where ip='" + ip + "'");
	    	result = true;
	    }
	    catch(Exception ex)
	    {
	        SysLogger.error("Error in ProcessTempDao.deleteByIp(String ip)",ex);
	    }
	    finally
	    {
	        conn.close();
	    }
	    return result;
	}
	public BaseVo loadFromRS(ResultSet rs) {
		NodeTemp vo = new NodeTemp();
		try
		{
			vo.setNodeid(rs.getString("nodeid"));
			vo.setIp(rs.getString("ip"));
			vo.setType(rs.getString("type"));
			vo.setSubtype(rs.getString("subtype"));
			vo.setEntity(rs.getString("entity"));
			vo.setSubentity(rs.getString("subentity"));
			vo.setThevalue(rs.getString("thevalue"));
			vo.setChname(rs.getString("chname"));
			vo.setRestype(rs.getString("restype"));
			vo.setSindex(rs.getString("sindex"));
			vo.setCollecttime(rs.getString("collecttime"));
			vo.setUnit(rs.getString("unit"));
			vo.setBak(rs.getString("bak"));
		}
		catch(Exception e)
		{
			SysLogger.error("ProcessTempDao.loadFromRS()",e);
		}
	    return vo;
	}

	public boolean save(BaseVo baseVo) {
		NodeTemp vo = (NodeTemp)baseVo;		
	    StringBuffer sql = new StringBuffer(500);
	    sql.append("insert into " + table + "(nodeid,ip,type,subtype,entity,subentity,sindex,thevalue,chname,restype,collecttime,unit,bak)values('");
	    sql.append(vo.getNodeid());
	    sql.append("','");
	    sql.append(vo.getIp());
	    sql.append("','");
	    sql.append(vo.getType());
	    sql.append("','");
	    sql.append(vo.getSubtype());
	    sql.append("','");
	    sql.append(vo.getEntity());
	    sql.append("','");
	    sql.append(vo.getSubentity());
	    sql.append("','");
	    sql.append(vo.getSindex());
	    sql.append("','");
	    sql.append(vo.getThevalue());
	    sql.append("','");
	    sql.append(vo.getChname());
	    sql.append("','");
	    sql.append(vo.getRestype());
	    sql.append("','");
	    sql.append(vo.getCollecttime());
	    sql.append("','");
	    sql.append(vo.getUnit());
	    sql.append("','");
	    sql.append(vo.getBak());
	    sql.append("')");
	   
	    return saveOrUpdate(sql.toString());
    }

	public boolean update(BaseVo vo) {
		return false;
	}
	public List<ProcessInfo> countProcessInfoByName(String nodeid, String type, String subtype){
		List<ProcessInfo> list = new ArrayList<ProcessInfo>();
		StringBuffer sql = new StringBuffer();
		sql.append("select thevalue,count(thevalue) from " + table + " where nodeid='"); 
		sql.append(nodeid);
		sql.append("' and type='");
		sql.append(type);
		sql.append("' and subtype='");
		sql.append(subtype);
		sql.append("' and subentity='Name' group by thevalue");
		rs = conn.executeQuery(sql.toString());
		DBManager manager = new DBManager();
		try {
			Pattern p1 = Pattern.compile("(\\d+):(\\d+)��");
			while (rs.next()) {
				try {
					String name = rs.getString(1);		// ��������
					String count = rs.getString(2);		// ���̸���
					StringBuffer sql2 = new StringBuffer();
					sql2.append("select * from " + table + " where sindex in (select sindex from " + table + " where ");
					sql2.append(" nodeid='" + nodeid + "' ");
					sql2.append(" and type='" + type + "' ");
					sql2.append(" and subtype='" + subtype + "'");
					sql2.append(" and thevalue='" + name + "')");
					String status = "";
					String proType = "";
					double cpuTime = 0;
					double cpuUtilization = 0;
					double memoryUtilization = 0;
					double memory = 0;
					ResultSet resultSet = manager.executeQuery(sql2.toString());
					while (resultSet.next()) {
						String subentity = resultSet.getString("subentity");
						String thevalue = resultSet.getString("thevalue");
						if("CpuTime".equals(subentity)){
							float sumOfCPU = 0;
							if(thevalue!=null){
								if(thevalue.indexOf(":")!=-1){
									Matcher matcher2 = p1.matcher(thevalue);
									if(matcher2.find())
									{
										String t1 = matcher2.group(1);
										String t2 = matcher2.group(2);
										sumOfCPU = Float.parseFloat(t1)*60 + Float.parseFloat(t2);
									}
								} else {
									sumOfCPU = Float.parseFloat(thevalue.replace("��", ""));
								}
							}
							cpuTime = cpuTime + sumOfCPU;
						}  else if("Status".equals(subentity)){
							status = thevalue;
						} else if("CpuUtilization".equals(subentity)){
							cpuUtilization = cpuUtilization + Double.parseDouble(thevalue);
						} else if("Type".equals(subentity)){
							proType = thevalue;
						} else if("MemoryUtilization".equals(subentity)){
							memoryUtilization = memoryUtilization + Double.parseDouble(thevalue);
						} else if("Memory".equals(subentity)){
							memory = memory + Double.parseDouble(thevalue);
						}  
					}
					
					ProcessInfo processInfo = new ProcessInfo();
					processInfo.setName(name);
					processInfo.setCount(count);
					processInfo.setCpuTime(cpuTime + "");
					processInfo.setMemory(memory + "");
					processInfo.setMemoryUtilization(memoryUtilization + "");
					processInfo.setStatus(status);
					processInfo.setType(proType);
					list.add(processInfo);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			manager.close();
			conn.close();
		}
		return list;
	}
	
	
	public List<ProcessInfo> getProcessDetailInfoByName(String nodeid, String type, String subtype, String name){
		List<ProcessInfo> list = new ArrayList<ProcessInfo>();
		StringBuffer sql = new StringBuffer();
		sql.append("select sindex from " + table + " where nodeid='"); 
		sql.append(nodeid);
		sql.append("' and type='");
		sql.append(type);
		sql.append("' and subtype='");
		sql.append(subtype);
		sql.append("' and thevalue='" + name + "'");
		rs = conn.executeQuery(sql.toString());
		DBManager manager = new DBManager();
		try {
			while (rs.next()) {
				try {
					String sindex = rs.getString(1);		// ����id
					StringBuffer sql2 = new StringBuffer();
					sql2.append("select * from " + table + " where");
					sql2.append(" nodeid='" + nodeid + "' ");
					sql2.append(" and type='" + type + "' ");
					sql2.append(" and subtype='" + subtype + "'");
					sql2.append(" and sindex='" + sindex + "'");
					String status = "";
					String proType = "";
					String cpuTime = "";
					String cpuUtilization = "";
					String memoryUtilization = "";
					String memory = "";
					ResultSet resultSet = manager.executeQuery(sql2.toString());
					while (resultSet.next()) {
						String subentity = resultSet.getString("subentity");
						String thevalue = resultSet.getString("thevalue");
						if("CpuTime".equals(subentity)){
							cpuTime = thevalue;
						}  else if("Status".equals(subentity)){
							status = thevalue;
						} else if("CpuUtilization".equals(subentity)){
							cpuUtilization = cpuUtilization + Double.parseDouble(thevalue);
						} else if("Type".equals(subentity)){
							proType = thevalue;
						} else if("MemoryUtilization".equals(subentity)){
							memoryUtilization = thevalue;
						} else if("Memory".equals(subentity)){
							memory = memory + thevalue;
						}  
					}
					
					ProcessInfo processInfo = new ProcessInfo();
					processInfo.setPid(sindex);
					processInfo.setName(name);
					processInfo.setCpuTime(cpuTime + "");
					processInfo.setMemory(memory + "");
					processInfo.setMemoryUtilization(memoryUtilization + "");
					processInfo.setStatus(status);
					processInfo.setType(proType);
					list.add(processInfo);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			manager.close();
			conn.close();
		}
		return list;
	}
	
	
	public List<ProcessInfo> getProcessInfo(String nodeid, String type, String subtype){
		List<ProcessInfo> list = new ArrayList<ProcessInfo>();
		StringBuffer sql = new StringBuffer();
		sql.append("select sindex from " + table + " where nodeid='"); 
		sql.append(nodeid);
		sql.append("' and type='");
		sql.append(type);
		sql.append("' and subtype='");
		sql.append(subtype);
		sql.append("' group by sindex");
		rs = conn.executeQuery(sql.toString());
		DBManager manager = new DBManager();
		try {
			while (rs.next()) {
				try {
					String sindex = rs.getString(1);	// ����id
					StringBuffer sql2 = new StringBuffer();
					sql2.append("select * from " + table + " where");
					sql2.append(" nodeid='" + nodeid + "' ");
					sql2.append(" and type='" + type + "' ");
					sql2.append(" and subtype='" + subtype + "'");
					sql2.append(" and sindex='" + sindex + "'");
					String name = ""; 					// ��������
					//String count = "";				// ���̸���
					String proType = "";				// ��������
					String cpuTime = "";				// CPUʱ��
					String memoryUtilization = "";		// �ڴ�ռ����
					String memory = "";					// �ڴ�ռ����
					String status = "";					// ��ǰ״̬
					String averageUSecs = "";			// ƽ������CPU����ʱ��(���˽�����)
					String extPriorityRev = "";			// ���ȼ�(���˽�����)
					String runtime = "";				// ����ʱ��(���˽�����)
					ResultSet resultSet = manager.executeQuery(sql2.toString());
					while (resultSet.next()) {
						String subentity = resultSet.getString("subentity");
						String thevalue = resultSet.getString("thevalue");
						if("Name".equals(subentity)){
							name = thevalue;
						}  else if("CpuTime".equals(subentity)){
							cpuTime = thevalue;
						}  else if("Status".equals(subentity)){
							status = thevalue;
						} else if("Type".equals(subentity)){
							proType = thevalue;
						} else if("MemoryUtilization".equals(subentity)){
							memoryUtilization = thevalue;
						} else if("Memory".equals(subentity)){
							memory = memory + thevalue;
						} else if("Status".equalsIgnoreCase(subentity)){
							status = thevalue;
						} else if("AverageUSecs".equalsIgnoreCase(subentity)){
							averageUSecs = thevalue;
						} else if("ExtPriorityRev".equalsIgnoreCase(subentity)){
							extPriorityRev = thevalue;
						} else if("Runtime".equalsIgnoreCase(subentity)){
							runtime = thevalue;
						}
					}
					
					ProcessInfo processInfo = new ProcessInfo();
					processInfo.setPid(sindex);
					processInfo.setName(name);
					processInfo.setCpuTime(cpuTime + "");
					processInfo.setMemory(memory + "");
					processInfo.setMemoryUtilization(memoryUtilization + "");
					processInfo.setStatus(status);
					processInfo.setType(proType);
					processInfo.setAverageUSecs(averageUSecs);
					processInfo.setExtPriorityRev(extPriorityRev);
					processInfo.setRuntime(runtime);
					list.add(processInfo);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			manager.close();
			conn.close();
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<NodeTemp> getNodeTempList(String nodeid, String type, String subtype){
		StringBuffer sql = new StringBuffer();
		sql.append(" where nodeid='" + nodeid + "' and type='" + type + "' and subtype='" + subtype + "'");
		//System.out.println(sql.toString());
		return findByCondition(sql.toString());
	}

	/**
	 * �õ�������Ϣ
	 * @param nodeid
	 * @param type
	 * @param subtype
	 * @param order
	 * @return
	 */
	public Hashtable getProcessInfo(String nodeid, String type, String subtype,
			String order) {
		Hashtable retHashtable = new Hashtable();
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("select distinct sindex from " + table + " where nodeid = '");
		sqlBuffer.append(nodeid);
		sqlBuffer.append("' and type = '");
		sqlBuffer.append(type);
		sqlBuffer.append("' and subtype = '");
		sqlBuffer.append(subtype);
		sqlBuffer.append("'");
		DBManager dbManager = new DBManager();
		rs1 = dbManager.executeQuery(sqlBuffer.toString());
		List sindexList = new ArrayList();
		try {
			while(rs1.next()){
				String sindex = rs1.getString("sindex");
				sindexList.add(sindex);
			}
			rs1.close();
			for(int i = 0;i < sindexList.size(); i++){
				Hashtable itemHashtable = new Hashtable();
				String sindex = (String)sindexList.get(i);
				StringBuffer sql = new StringBuffer();
				sql.append(" select * from " + table + " where nodeid='");
				sql.append(nodeid);
				sql.append("' and type='");
				sql.append(type);
				sql.append("' and subtype='");
				sql.append(subtype);
				sql.append("' and sindex='");
				sql.append(sindex);
				sql.append("' order by '");
				sql.append(order);
				sql.append("'");
				System.out.println(sql.toString());
				rs2 = dbManager.executeQuery(sql.toString());
				while (rs2.next()) {
					String subentity = rs2.getString("subentity");
					String thevalue = rs2.getString("thevalue");
					itemHashtable.put(subentity, thevalue);
				}
				retHashtable.put(i, itemHashtable);
				rs2.close();
			}
		}catch (SQLException e) {
				e.printStackTrace();
		} finally{
			try {
				if(rs1 != null){
					rs1.close();
				}
				if(rs2 != null){
					rs2.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbManager.close();
		}
		return retHashtable;
	}
}
