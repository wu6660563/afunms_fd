package com.afunms.temp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.SysLogger;
import com.afunms.detail.reomte.model.FibreCapabilityInfo;
import com.afunms.temp.model.NodeTemp;

public class ChannelTempDao extends BaseDao implements DaoInterface {

	public ChannelTempDao() {
		super("nms_fibrecapability_data_temp");	   	  
	}

	/**
	 * ɾ��һ����¼
	 */
	public boolean deleteByNodeIdSindex(String nodeid,String sid,String subentity) {
		boolean result = false;
	    try
	    {
	    	conn.executeUpdate("delete from nms_fibrecapability_data_temp where nodeid='" + nodeid + "' and sindex='" + sid + "' and subentity='" + subentity + "'");
	    	result = true;
	    }
	    catch(Exception ex)
	    {
	        SysLogger.error("Error in ChannelTempDao.deleteByNodeId(String nodeid)",ex);
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
	    	conn.executeUpdate("delete from nms_fibrecapability_data_temp where ip='" + ip + "'");
	    	result = true;
	    }
	    catch(Exception ex)
	    {
	        SysLogger.error("Error in ChannelTempDao.deleteByIp(String ip)",ex);
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
			SysLogger.error("ChannelTempDao.loadFromRS()",e);
		}
	    return vo;
	}

	public boolean save(BaseVo baseVo) {
		NodeTemp vo = (NodeTemp)baseVo;		
	    StringBuffer sql = new StringBuffer(500);
	    sql.append("insert into nms_fibrecapability_data_temp(nodeid,ip,type,subtype,entity,subentity,sindex,thevalue,chname,restype,collecttime,unit,bak)values('");
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
//	    System.out.println("##################"+sql.toString());
	    return saveOrUpdate(sql.toString());
    }

	public boolean update(BaseVo vo) {
		return false;
	}
	
	public List<FibreCapabilityInfo> getFibreCapabilityInfoList(String nodeid, String type, String subtype, String[] subentities){
		List<FibreCapabilityInfo> fibreCapabilityInfoList = new ArrayList<FibreCapabilityInfo>(); 
		StringBuffer sql = new StringBuffer();
		sql.append("select sindex from nms_fibrecapability_data_temp where nodeid='"); 
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
					String sindex = rs.getString(1);	// �����������
					StringBuffer sql2 = new StringBuffer();
					sql2.append("select * from nms_fibrecapability_data_temp where ");
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
					String portName = "";						// �������
				    String portPhysOperStatus = "";				// ��ǰ״̬
				    String portC3InFrames = "";					// ���֡��
				    String portC3OutFrames = "";				// ����֡��
				    String portC3InOctets = "";					// ���֡�ֽ���
				    String portC3OutOctets = "";				// ����֡�ֽ���
				    String portC3Discards = "";					// ������
					//SysLogger.info(sql2.toString());
					ResultSet resultSet = manager.executeQuery(sql2.toString());
					while (resultSet.next()) {
						String subentity = resultSet.getString("subentity");
						String thevalue = resultSet.getString("thevalue");
						String unit = resultSet.getString("unit");
						if("PortName".equalsIgnoreCase(subentity)){
							portName = thevalue;
	             	    }
	             	    if("PortPhysOperStatus".equalsIgnoreCase(subentity)){
	             	    	portPhysOperStatus = thevalue;
	             	    }
	             	    if("PortC3InFrames".equalsIgnoreCase(subentity)){
	             	    	portC3InFrames = thevalue;
	             	    }
	             	    if("PortC3OutFrames".equalsIgnoreCase(subentity)){
	             	    	portC3OutFrames = thevalue;
	             	    }
	             	    if("PortC3InOctets".equalsIgnoreCase(subentity)){
	             	    	portC3InOctets = thevalue;
	             	    }
	             	    if("PortC3OutOctets".equalsIgnoreCase(subentity)){
	             	        portC3OutOctets = thevalue;
	             	    }
	             	    if("PortC3Discards".equalsIgnoreCase(subentity)){
	             	        portC3Discards = thevalue;
	             	    }
					}
					
					FibreCapabilityInfo fibreCapabilityInfo = new FibreCapabilityInfo();
					fibreCapabilityInfo.setSindex(sindex);
					fibreCapabilityInfo.setPortName(portName);
					fibreCapabilityInfo.setPortPhysOperStatus(portPhysOperStatus);
					fibreCapabilityInfo.setPortC3InFrames(portC3InFrames);
					fibreCapabilityInfo.setPortC3OutFrames(portC3OutFrames);
					fibreCapabilityInfo.setPortC3InOctets(portC3InOctets);
					fibreCapabilityInfo.setPortC3OutOctets(portC3OutOctets);
					fibreCapabilityInfo.setPortC3Discards(portC3Discards);
					
					fibreCapabilityInfoList.add(fibreCapabilityInfo);
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
		return fibreCapabilityInfoList;
	}

}
