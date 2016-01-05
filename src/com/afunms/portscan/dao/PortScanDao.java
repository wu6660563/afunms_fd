package com.afunms.portscan.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.CommonUtil;
import com.afunms.common.util.SysLogger;
import com.afunms.portscan.model.PortConfig;
import com.afunms.sysset.model.Service;
import com.afunms.system.model.AccreditAdmin;
import com.afunms.system.vo.PortTypeVo;

public class PortScanDao extends BaseDao implements DaoInterface {

	public PortScanDao() {
		super("nms_portscan_config");
		// TODO Auto-generated constructor stub
	}

	@Override
	public BaseVo loadFromRS(ResultSet rs) {
		PortConfig vo = new PortConfig();
		try {
			vo.setId(rs.getInt("id"));
			vo.setIpaddress(rs.getString("ipaddress"));
			vo.setPort(rs.getString("port"));
			vo.setPortName(rs.getString("portName"));
			vo.setDescription(rs.getString("description"));
			vo.setType(rs.getString("type"));
			vo.setTimeout(rs.getString("timeout"));
			vo.setStatus(rs.getString("status"));
			vo.setIsScanned(rs.getString("isScanned"));

			vo.setScantime(rs.getString("scantime"));

		} catch (Exception e) {
			SysLogger.error("porttypeDao.loadFromRS()", e);
			vo = null;
		}
		return vo;
	}

	public boolean deleteByHostIp(String hostip) {
		String sql = "delete from nms_portscan_config where ipaddress='"
				+ hostip + "'";
		return saveOrUpdate(sql);
	}

	public boolean save(BaseVo vo) {
		// Ӧ�ñ�֤�˿ڲ����ظ�
		PortConfig portConfig = (PortConfig) vo;
		StringBuffer sb = new StringBuffer(200);
		sb
				.append("insert into nms_portscan_config(ipaddress,port,portName,desc,type,timeout,status,isScanned,scantime)values(");
		sb.append(portConfig.getIpaddress());
		sb.append(",'");
		sb.append(portConfig.getPort());
		sb.append("',");
		sb.append(portConfig.getPortName());
		sb.append(",");
		sb.append(portConfig.getDescription());
		sb.append(",");
		sb.append(portConfig.getType());
		sb.append(",");
		sb.append(portConfig.getTimeout());
		sb.append(",");
		sb.append(portConfig.getStatus());
		sb.append(",");
		sb.append(portConfig.getIsScanned());
		sb.append(",");
		sb.append(portConfig.getScantime());
		sb.append(")");

		return saveOrUpdate(sb.toString());
	}

	public boolean saveBatch(List list) {
		boolean result = false;
		try {
			for (int i = 0; i < list.size(); i++) {
				PortConfig portConfig = (PortConfig) list.get(i);
				StringBuffer sb = new StringBuffer(200);
				sb
						.append("insert into nms_portscan_config(ipaddress,port,portName,description,type,timeout,status,isScanned,scantime)values('");
				sb.append(portConfig.getIpaddress());
				sb.append("','");
				sb.append(portConfig.getPort());
				sb.append("','");
				sb.append(portConfig.getPortName());
				sb.append("','");
				sb.append(portConfig.getDescription());
				sb.append("','");
				sb.append(portConfig.getType());
				sb.append("','");
				sb.append(portConfig.getTimeout());
				sb.append("','");
				sb.append(portConfig.getStatus());
				sb.append("','");
				sb.append(portConfig.getIsScanned());
				sb.append("','");
				sb.append(portConfig.getScantime());
				sb.append("')");
				conn.addBatch(sb.toString());
			}
			conn.executeBatch();
			result = true;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public boolean update(BaseVo vo) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteByIpaddress(List ipaddresslist) {
		boolean result = false;
		try {
			for (int i = 0; i < ipaddresslist.size(); i++) {
				String ipaddress = (String) ipaddresslist.get(i);
				String sql = "delete from nms_portscan_config where ipaddress='"
						+ ipaddress + "'";
				conn.addBatch(sql);
			}
			conn.executeBatch();
			result = true;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public boolean deleteByIpaddress(String ipaddress) {
		String sql = "delete from nms_portscan_config where ipaddress='"
				+ ipaddress + "'";
		return saveOrUpdate(sql);
	}

	public List findByIpaddress(String ipaddress) {
		String sql = "select * from nms_portscan_config where ipaddress='"
				+ ipaddress + "'";
		return findByCriteria(sql);
	}

	public Hashtable getPortStatusByIp(String ip, String starttime,
			String endtime) {
		Hashtable hash = new Hashtable();
		if (!starttime.equals("") && !endtime.equals("")) {
			List list1 = new ArrayList();
			String allip = ip;
			String sql = "select a.THEVALUE,a.COLLECTTIME from portstatus"
					+ allip + " a where  a.COLLECTTIME >= '" + starttime
					+ "' and  a.COLLECTTIME <= '" + endtime + "' order by id";
			rs = conn.executeQuery(sql);
			try {
				while (rs.next()) {
					Vector v = new Vector();
					String thevalue = rs.getString("THEVALUE");
					String collecttime = rs.getString("COLLECTTIME");
					if (thevalue.equalsIgnoreCase("up")) {
						thevalue = "100";
					} else {
						thevalue = "0";
					}
					v.add(0, thevalue);
					v.add(1, collecttime);
					v.add(2, "%");
					list1.add(v);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (rs != null)
						rs.close();
					if (conn != null)
						conn.close();

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			hash.put("list", list1);
		}
		return hash;
	}

	public String getHourData(String allip, String index, String starttime,
			String totime) {
		int value = 0;
		Map<Integer, Integer> map = new TreeMap<Integer, Integer>();
		for (int i = 0; i < 24; i++) {
			map.put(i, 2);

		}
		try {
			int temp = 0;
			boolean flag = false;
			rs = conn
					.executeQuery("select HOUR(COLLECTTIME)  as h,THEVALUE as value from portstatus"
							+ allip
							+ " where  COLLECTTIME >= '"
							+ starttime
							+ "' and  COLLECTTIME <= '"
							+ totime
							+ "'  and SUBENTITY='" + index + "' group by h");

			while (rs.next()) {
				int time = rs.getInt("h");
				String thevalue = rs.getString("value");

				if (flag && temp == time && value == 0) {
					continue;
				}

				if (thevalue.equalsIgnoreCase("up")) {
					value = 1;

				} else if(thevalue.equalsIgnoreCase("down")){
					value = 0;
				}

				map.put(time, value);
				temp = time;
				flag = true;
			}
		} catch (Exception e) {
			SysLogger.error(" port size null :");
		}finally{
			try {
			if (rs!=null)
					rs.close();
			if (conn!=null) {
				conn.close();
			}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		StringBuffer dataStr = new StringBuffer();
		boolean flag1=false;
		for (int i = 0; i < 24; i++) {
			if (map.get(i)==2) {
				dataStr.append(i).append(";").append("").append("\\n");
			}else{
			dataStr.append(i).append(";").append(map.get(i)).append("\\n");
			flag1=true;
			}
		}
		if (!flag1) {
			return "0";
		}
		return dataStr.toString();
	}
	public Vector getCurrentStatus(String ip) {
		
		Vector vector=new Vector();
		try {
			
			rs = conn
					.executeQuery("select p.thevalue value , s.name n,p.collecttime from nms_interface_data_temp" + CommonUtil.doip(ip) + "  p ,system_portconfig s where p.ip=s.ipaddress and p.sindex=s.portindex and s.sms=1 and  p.subentity='ifOperStatus' and p.ip='"+ip+"'");

			while (rs.next()) {
				String name = rs.getString("n");
				String thevalue = rs.getString("value");
//				if (thevalue.equalsIgnoreCase("up")) {
//					value = 1;
//
//				} else if(thevalue.equalsIgnoreCase("down")){
//					value = 0;
//				}
				String[] strs=new String[2];
				strs[0]=name;
				strs[1]=thevalue;
				vector.add(strs);
			}
		} catch (Exception e) {
			SysLogger.error(" port size null :");
		}finally{
			try {
			if (rs!=null)
					rs.close();
			if (conn!=null) {
				conn.close();
			}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		return vector;
	}
}
