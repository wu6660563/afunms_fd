package com.afunms.config.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.config.model.CheckResult;
import com.afunms.config.model.CompCheckResultModel;
import com.ibm.db2.jcc.c.r;

public class CheckResultDao extends BaseDao implements DaoInterface {
	public CheckResultDao() {
		super("nms_comp_check_results");
	}

	@Override
	public BaseVo loadFromRS(ResultSet rs) {
		CompCheckResultModel vo=new CompCheckResultModel();
		try {
			vo.setId(rs.getInt("ID"));
			vo.setStrategyId(rs.getInt("STRATEGY_ID"));
			vo.setStrategyName(rs.getString("STRATEGY_NAME"));
			vo.setGroupId(rs.getInt("GROUP_ID"));
			vo.setGroupName(rs.getString("GROUP_NAME"));
			vo.setRuleId(rs.getInt("RULE_ID"));
			vo.setRuleName(rs.getString("RULE_NAME"));
			vo.setIp(rs.getString("IP"));
			vo.setIsViolation(rs.getInt("ISVIOLATION"));
			vo.setViolationSeverity(rs.getInt("VIOLATION_SEVERITY"));
			vo.setDescription(rs.getString("DESCRIPTION"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return vo;
	}

	public boolean save(BaseVo vo) {
		
		return false;
	}

	public boolean update(BaseVo vo) {
		// TODO Auto-generated method stub
		return false;
	}
  public List<CompCheckResultModel> getReslutByIdAndIp(String id,String ip) {
	  String sql = "select * from nms_comp_check_results  where STRATEGY_ID="
			+ id + " and ip='" + ip + "'";
	List<CompCheckResultModel> list=new ArrayList<CompCheckResultModel>();
	try {
		rs = conn.executeQuery(sql);
		while (rs.next()) {
			list.add((CompCheckResultModel) loadFromRS(rs));
		}
	} catch (Exception e) {
		e.printStackTrace();
	}finally{
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (conn != null) {
			conn.close();
		}
	}
	return list;
}
	public CheckResult getExtraCount(String id, String ip) {
		
		int count = 0;
		String time="";
		Vector<String> vector=new Vector<String>();
		CheckResult result=new CheckResult();
		try {
			String sql="select count(ISVIOLATION) as countX ,CHECK_TIME from nms_comp_check_results  where ISVIOLATION=0 and STRATEGY_ID="
				+ id + " and ip='" + ip + "' group by ISVIOLATION";
			rs = conn.executeQuery(sql);
			while(rs.next()){
				vector.add(rs.getString("CHECK_TIME"));
			}
			if (vector.size()>0) {
				result.setExactCount(0);
			}else {
				sql = "select count(ISVIOLATION) as countX ,CHECK_TIME from nms_comp_check_results  where ISVIOLATION=1 and STRATEGY_ID="
					+ id + " and ip='" + ip + "' group by ISVIOLATION";
				
				rs = conn.executeQuery(sql);
				while (rs.next()) {
					
					count = rs.getInt("countX");
					time=rs.getString("CHECK_TIME");
					result.setExactCount(count);
					result.setCheckTime(time);
				}	
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				conn.close();
			}
		}
		return result;
	}
	public CheckResult getExtraCount1(String id, String ip) {
		
		int count = 0;
		String time="";
		Vector<String> vector=new Vector<String>();
		CheckResult result=new CheckResult();
		try {
			String 
				sql = "select count(ISVIOLATION) as countX ,CHECK_TIME from nms_comp_check_results  where ISVIOLATION=1 and STRATEGY_ID="
					+ id + " and ip='" + ip + "' group by ISVIOLATION";
				
				rs = conn.executeQuery(sql);
				while (rs.next()) {
					
					count = rs.getInt("countX");
					time=rs.getString("CHECK_TIME");
					result.setExactCount(count);
					result.setCheckTime(time);
					
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				conn.close();
			}
		}
		return result;
	}
	
	public List getExtraCountList() {
		int count = 0;
		String time="";
		List resultList=new ArrayList();
		Vector<String> ipVector=new Vector<String>();
		CheckResult result=new CheckResult();
		try {
			String sql = "select STRATEGY_ID,STRATEGY_NAME,IP,count( IP) as count ,CHECK_TIME from nms_comp_check_results  where ISVIOLATION=0 group by IP" ;
			 rs = conn.executeQuery(sql);
			 while(rs.next()){
           	  ipVector.add(rs.getString("IP"));
             }
			 rs=null;
			 
			 sql = "select STRATEGY_ID,STRATEGY_NAME,IP,count( IP) as count ,CHECK_TIME from nms_comp_check_results  where ISVIOLATION=1 group by IP" ;
             
			rs = conn.executeQuery(sql);
			while (rs.next()) {
				 int id=rs.getInt("STRATEGY_ID");
				 String name=rs.getString("STRATEGY_NAME");
				 String ip=rs.getString("IP");
				count = rs.getInt("count");
				time=rs.getString("CHECK_TIME");
				result.setId(id);//����ID
				result.setExactCount(count);
				result.setName(rs.getString("STRATEGY_NAME"));
				result.setIp(rs.getString("IP"));
				result.setCheckTime(time);
				if (!ipVector.contains(ip)) {
					resultList.add(result);
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				conn.close();
			}
		}
		return resultList;
	}
//	public List getExtraCountList1() {
//		int count = 0;
//		String time="";
//		List resultList=new ArrayList();
//		CheckResult result=new CheckResult();
//		try {
//			
//			
//			String sql = "select STRATEGY_ID,STRATEGY_NAME,IP,count( IP) as count ,CHECK_TIME from nms_comp_check_results  where ISVIOLATION=1 group by IP" ;
//			
//			rs = conn.executeQuery(sql);
//			while (rs.next()) {
//				int id=rs.getInt("STRATEGY_ID");
//				String name=rs.getString("STRATEGY_NAME");
//				String ip=rs.getString("IP");
//				count = rs.getInt("count");
//				time=rs.getString("CHECK_TIME");
//				result.setId(id);//����ID
//				result.setExactCount(count);
//				result.setName(rs.getString("STRATEGY_NAME"));
//				result.setIp(rs.getString("IP"));
//				result.setCheckTime(time);
//					resultList.add(result);
//				
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			if (rs != null) {
//				try {
//					rs.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			if (conn != null) {
//				conn.close();
//			}
//		}
//		return resultList;
//	}

	public List<CheckResult> getResultById(String id) {
		List<CheckResult> resultList = new ArrayList<CheckResult>();

		try {
			String sql = "select a0.ip,count(a0.VIOLATION_SEVERITY) as countX ,a0.VIOLATION_SEVERITY as countY ,a0.check_time from nms_comp_check_results a0 where a0.ISVIOLATION=0 and STRATEGY_ID="
					+ id + " group by a0.ip,a0.VIOLATION_SEVERITY ";
			//System.out.println(sql);
			rs = conn.executeQuery(sql);
			CheckResult result = null;
			String tempIp = "";
			while (rs.next()) {
				String ip = rs.getString("ip");
				String time = rs.getString("check_time");
				if (tempIp.equals("")) {
					result = new CheckResult();

				}
				if (!tempIp.equals("") && !tempIp.equals(ip)) {

					if (!tempIp.equals(ip)) {

						resultList.add(result);
					}
					result = new CheckResult();
				}
				int countX = rs.getInt("countX");
				int countY = rs.getInt("countY");
				if (countY == 0) {
					result.setCount0(countX);
				} else if (countY == 1) {
					result.setCount1(countX);
				} else if (countY == 2) {
					result.setCount2(countX);
				}
				result.setIp(ip);
				result.setCheckTime(time);
				tempIp = ip;

			}
			if (result != null)
				resultList.add(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				conn.close();
			}
		}
		return resultList;
	}
	public List<CheckResult> getAllResult() {
		List<CheckResult> resultList = new ArrayList<CheckResult>();
		
		try {
			String sql = "select a0.STRATEGY_ID, a0.ip,a0.STRATEGY_NAME,count(a0.VIOLATION_SEVERITY) as countX ,a0.VIOLATION_SEVERITY as countY ,a0.check_time from nms_comp_check_results a0 where a0.ISVIOLATION=0  group by a0.ip,a0.VIOLATION_SEVERITY ";
			System.out.println(sql);
			rs = conn.executeQuery(sql);
			CheckResult result = null;
			String tempIp = "";
			while (rs.next()) {
				int id=rs.getInt("STRATEGY_ID");
				String ip = rs.getString("ip");
				String name=rs.getString("STRATEGY_NAME");
				String time = rs.getString("check_time");
				if (tempIp.equals("")) {
					result = new CheckResult();
					
				}
				if (!tempIp.equals("") && !tempIp.equals(ip)) {
					
					if (!tempIp.equals(ip)) {
						
						resultList.add(result);
					}
					result = new CheckResult();
				}
				int countX = rs.getInt("countX");
				int countY = rs.getInt("countY");
				if (countY == 0) {
					result.setCount0(countX);
				} else if (countY == 1) {
					result.setCount1(countX);
				} else if (countY == 2) {
					result.setCount2(countX);
				}
				result.setId(id);
				result.setIp(ip);
				result.setCheckTime(time);
				result.setName(name);
				tempIp = ip;
				
			}
			if (result != null)
				resultList.add(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				conn.close();
			}
		}
		return resultList;
	}

	
}
