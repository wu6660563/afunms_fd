package com.afunms.temp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.SysLogger;
import com.afunms.detail.reomte.model.DiskPerfInfo;
import com.afunms.temp.model.NodeTemp;

public class DiskPerfTempDao extends BaseDao implements DaoInterface {

	public DiskPerfTempDao() {
		super("nms_diskperf_data_temp");	   	  
	}

	/**
	 * ɾ��һ����¼
	 */
	public boolean deleteByNodeIdSindex(String nodeid,String sid,String subentity) {
		boolean result = false;  
	    try   
	    {
	    	conn.executeUpdate("delete from nms_diskperf_data_temp where nodeid='" + nodeid + "' and sindex='" + sid  + "' and subentity='" + subentity + "'");
	    	result = true;
	    }
	    catch(Exception ex)
	    {
	        SysLogger.error("Error in DiskPerfTempDao.deleteByNodeId(String nodeid)",ex);
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
	    	conn.executeUpdate("delete from nms_diskperf_data_temp where ip='" + ip + "'");
	    	result = true;
	    }
	    catch(Exception ex)
	    {
	        SysLogger.error("Error in DiskPerfTempDao.deleteByIp(String ip) ",ex);
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
			SysLogger.error("DiskPerfTempDao.loadFromRS()",e);
		}
	    return vo;
	}

	public boolean save(BaseVo baseVo) {
		NodeTemp vo = (NodeTemp)baseVo;		
	    StringBuffer sql = new StringBuffer(500);
	    sql.append("insert into nms_diskperf_data_temp(nodeid,ip,type,subtype,entity,subentity,sindex,thevalue,chname,restype,collecttime,unit,bak)values('");
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
	
	@SuppressWarnings("unchecked")
	public List<NodeTemp> getNodeTemp(String nodeid, String type, String subtype){
		StringBuffer sql = new StringBuffer();
		sql.append(" where nodeid='" + nodeid + "' and type='" + type + "' and subtype='" + subtype + "'");
		System.out.println(sql.toString());
		return findByCondition(sql.toString());
	}
	
	
	@SuppressWarnings("unchecked")
	public List<DiskPerfInfo> getDiskInfoList(String nodeid, String type, String subtype, String[] subentities){
		List<DiskPerfInfo> diskPerfInfoList = new ArrayList<DiskPerfInfo>();
		StringBuffer sql = new StringBuffer();
		sql.append("select sindex from nms_diskperf_data_temp where nodeid='"); 
		sql.append(nodeid);
		sql.append("' and type='");
		sql.append(type);
		sql.append("' and subtype='");
		sql.append(subtype);
		sql.append("' group by sindex");
		//System.out.println(sql.toString());
		rs = conn.executeQuery(sql.toString());
		DBManager manager = new DBManager();
		try {
			while (rs.next()) {
				try {
					String sindex = rs.getString(1);	// ��������(����)
					StringBuffer sql2 = new StringBuffer();
					sql2.append("select * from nms_diskperf_data_temp where ");
					sql2.append(" nodeid='" + nodeid + "' ");
					sql2.append(" and type='" + type + "' ");
					sql2.append(" and subtype='" + subtype + "'");
					sql2.append(" and sindex='" + sindex + "'");
					if(subentities != null && subentities.length > 0){
						for(int i = 0; i < subentities.length; i++) {
							if(i == 0){
								sql2.append(" and (");
							} else {
								sql2.append(" or");
							}
							sql2.append(" subentity='" + subentities[i] + "'");
						}
						sql2.append(" )");
					}
					//SysLogger.info(sql2.toString());
					String disklebel = "";						// ��������
					String busyRate = "";						// ��æ(%)
					String avque = "";							// ƽ�����
					String readAndWriteBlockPerSecond = "";		// ��д����/��
					String readAndWriteBytePerSecond = "";		// ��д�ֽڣ�K��/��
					String avwait = "";							// ƽ���ȴ�ʱ��(ms)
					String avserv = "";							// ƽ��ִ��ʱ��(ms)
					ResultSet resultSet = manager.executeQuery(sql2.toString());
					while (resultSet.next()) {
						String subentity = resultSet.getString("subentity");
						String thevalue = resultSet.getString("thevalue");
						if("disklebel".equalsIgnoreCase(subentity)) {
							disklebel = thevalue;
						} else if("%busy".equalsIgnoreCase(subentity)) {
							busyRate = thevalue;
						} else if("avque".equalsIgnoreCase(subentity)) {
							avque = thevalue;
						} else if("r+w/s".equalsIgnoreCase(subentity)) {
							readAndWriteBlockPerSecond = thevalue;
						} else if("Kbs/s".equalsIgnoreCase(subentity)) {
							readAndWriteBytePerSecond = thevalue;
						} else if("avwait".equalsIgnoreCase(subentity)) {
							avwait = thevalue;
						} else if("avserv".equalsIgnoreCase(subentity)) {
							avserv = thevalue;
						}
					}
					DiskPerfInfo diskPerfInfo = new DiskPerfInfo();
					diskPerfInfo.setDisklebel(disklebel);
					diskPerfInfo.setBusyRate(busyRate);
					diskPerfInfo.setAvque(avque);
					diskPerfInfo.setReadAndWriteBlockPerSecond(readAndWriteBlockPerSecond);
					diskPerfInfo.setReadAndWriteBytePerSecond(readAndWriteBytePerSecond);
					diskPerfInfo.setAvwait(avwait);
					diskPerfInfo.setAvserv(avserv);
					
					diskPerfInfoList.add(diskPerfInfo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			manager.close();
			conn.close();
		}
		return diskPerfInfoList;
	}

	/**
	 * �õ�diskperf ����������Ϣ
	 * @param nodeid
	 * @return
	 */
	public List getDiskperflistInfo(String nodeid, String type, String subtype) {
		List diskperfList = new ArrayList();
		DBManager dbManager = new DBManager();
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("select distinct sindex from nms_diskperf_data_temp where nodeid = '");
		sqlBuffer.append(nodeid);
		sqlBuffer.append("' and type = '");
		sqlBuffer.append(type);
		sqlBuffer.append("' and subtype = '");
		sqlBuffer.append(subtype);
		sqlBuffer.append("'");
		ResultSet rs1 = null;
		rs1 = dbManager.executeQuery(sqlBuffer.toString());
		List sindexList = new ArrayList();
		try {
			while(rs1.next()){   
				String sindex = rs1.getString("sindex");
				sindexList.add(sindex);
			}
			rs1.close();
			for(int i = 0;i < sindexList.size(); i++){
				String sindex = (String)sindexList.get(i);
				sqlBuffer = new StringBuffer();
				sqlBuffer.append("select distinct * from nms_diskperf_data_temp where nodeid = '");
				sqlBuffer.append(nodeid);
				sqlBuffer.append("' and sindex = '");
				sqlBuffer.append(sindex);
				sqlBuffer.append("'");
				rs = dbManager.executeQuery(sqlBuffer.toString());
				Hashtable diskperflistHashtable = new Hashtable();
				while (rs.next()) {
					String subentity = rs.getString("subentity");
					String thevalue = rs.getString("thevalue");
					diskperflistHashtable.put(subentity, thevalue);
				}
				rs.close();
				diskperfList.add(diskperflistHashtable);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(rs1 != null){
				try {
					rs1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			dbManager.close();
		}
		return diskperfList;
	}
}
