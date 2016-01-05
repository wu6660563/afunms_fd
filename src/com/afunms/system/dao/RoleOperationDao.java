package com.afunms.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.SysLogger;
import com.afunms.system.model.RoleFunction;
import com.afunms.system.model.RoleOperation;

public class RoleOperationDao extends BaseDao implements DaoInterface{

	public RoleOperationDao() {
		super("system_role_operation");
	}


	@Override
	public BaseVo loadFromRS(ResultSet rs) {
	    RoleOperation roleOperation = new RoleOperation();
	    try {
            roleOperation.setId(rs.getInt("id"));
            roleOperation.setRoleId(rs.getString("role_id"));
            roleOperation.setOperation(rs.getString("operation"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return roleOperation;
	}
	
	public boolean deleteByRoleId(String roleId) {
		String sql = "delete from " + table + " where role_id='" + roleId + "'";
		return saveOrUpdate(sql);
	}
	
	public List<RoleOperation> findByRoleId(String roleId) {
	    String sql = " where role_id='" + roleId + "'";
	    return findByCondition(sql);
	}
	
	public boolean save(List<RoleOperation> list) {
	    boolean result = false;
	    try {
            for (RoleOperation roleOperation : list) {
                String sql = "insert into " + table + "(role_id, operation) values('";
                sql += roleOperation.getRoleId();
                sql += "','";
                sql += roleOperation.getOperation();
                sql += "');";
                conn.addBatch(sql);
            }
            conn.executeBatch();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
	}

	public boolean save(BaseVo vo) {
		return false;
	}


	public boolean update(BaseVo vo) {
		// TODO Auto-generated method stub
		return false;
	}



}
