package com.afunms.system.service;

import java.util.List;

import com.afunms.system.dao.RoleOperationDao;
import com.afunms.system.model.OperationPermission;
import com.afunms.system.model.RoleOperation;
import com.afunms.system.model.User;


public class RoleOperationPermissionService {

    protected List<RoleOperation> list;
    
    protected OperationPermission operationPermission = null;
    
    protected String roleId = null;

    private RoleOperationPermissionService(String roleId) {
        this.init(roleId);
    }

    public static RoleOperationPermissionService getInstance(String roleId, OperationPermission operationPermission) {
        RoleOperationPermissionService operationPermissionService = new RoleOperationPermissionService(roleId);
        operationPermissionService.operationPermission = operationPermission;
        return operationPermissionService;
    }

    public static RoleOperationPermissionService getInstance(User user, OperationPermission operationPermission) {
        RoleOperationPermissionService operationPermissionService = new RoleOperationPermissionService(String.valueOf(user.getRole()));
        operationPermissionService.operationPermission = operationPermission;
        return operationPermissionService;
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
        this.roleId = roleId;
    }
    
    public boolean isCreateOperationPermission() {
        if ("0".equals(this.roleId)) {
            return true;
        }
        String operation = operationPermission.getCode() + "-" + OperationService.CreateOperationCode;
        return getOperationPermission(operation);
    }

    public boolean isUpdateOperationPermission() {
        if ("0".equals(this.roleId)) {
            return true;
        }
        String operation = operationPermission.getCode() + "-" + OperationService.UpdateOperationCode;
        return getOperationPermission(operation);
    }

    public boolean isDeleteOperationPermission() {
        if ("0".equals(this.roleId)) {
            return true;
        }
        String operation = operationPermission.getCode() + "-" + OperationService.DeleteOperationCode;
        return getOperationPermission(operation);
    }

    public boolean isRetrieveOperationPermission() {
        if ("0".equals(this.roleId)) {
            return true;
        }
        String operation = operationPermission.getCode() + "-" + OperationService.RetrieveOperationCode;
        return getOperationPermission(operation);
    }

    private boolean getOperationPermission(String operation) {
        boolean result = false;
        if ("0".equals(this.roleId)) {
            result = true;
        } else if (list != null) {
            for (RoleOperation roleOperation : list) {
                if (roleOperation.getOperation().equals(operation)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}
