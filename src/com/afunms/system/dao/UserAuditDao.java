/**
 * <p>Description:operate table NMS_USER</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-07
 */

package com.afunms.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.afunms.application.model.Emailmonitor_realtime;
import com.afunms.common.base.*;
import com.afunms.system.model.UserAudit;


public class UserAuditDao extends BaseDao implements DaoInterface
{
   public UserAuditDao()
   {
	   super("nms_user_audit");
   }

	@Override
	public BaseVo loadFromRS(ResultSet rs) {
		// TODO Auto-generated method stub
		UserAudit userAudit = new UserAudit();
		
		
		try {
			userAudit.setId(rs.getInt("id"));
			
			userAudit.setUserId(rs.getInt("userid"));
			
			userAudit.setAction(rs.getString("action"));
			
			userAudit.setTime(rs.getString("time"));;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return userAudit;
	}
	
	public boolean save(BaseVo vo) {
		// TODO Auto-generated method stub
		UserAudit userAudit = (UserAudit)vo;	  							
	    StringBuffer sql = new StringBuffer();
	    sql.append("insert into nms_user_audit(userid,action,time)values(");
	    sql.append("'");
	    sql.append(userAudit.getUserId());
	    sql.append("','");
	    sql.append(userAudit.getAction());
	    sql.append("','");
	    sql.append(userAudit.getTime());
	    sql.append("'");
	    sql.append(")");
	    return saveOrUpdate(sql.toString());
		
	}
	
	public boolean update(BaseVo vo) {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean deleteByUserId(String userId){
		String sql = "delete from nms_user_audit where userid='" + userId+"'";
		try {
			conn.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
