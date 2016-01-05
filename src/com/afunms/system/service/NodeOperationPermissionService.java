package com.afunms.system.service;

import java.util.List;

import com.afunms.system.dao.RoleOperationDao;
import com.afunms.system.model.RoleOperation;
import com.afunms.system.model.User;


public class NodeOperationPermissionService {

    private List<RoleOperation> list;

    public NodeOperationPermissionService(User user) {
        String roleId = String.valueOf(user.getRole());
        this.init(roleId);
    }
    
    public NodeOperationPermissionService(String roleId) {
        this.init(roleId);
    }
    
    private void init(String roleId) {
        RoleOperationDao roleOperationDao = new RoleOperationDao();
        try {
            list = roleOperationDao.findByRoleId(roleId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            roleOperationDao.close();
        }
    }
}
