/**
 * <p>Description:DepartmentManager</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-07
 */

package com.afunms.system.manage;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.afunms.common.base.*;
import com.afunms.system.dao.RoleDao;
import com.afunms.system.dao.RoleOperationDao;
import com.afunms.system.model.OperationPermission;
import com.afunms.system.model.Role;
import com.afunms.system.model.RoleOperation;
import com.afunms.system.service.OperationPermissionService;


public class OperationPermissionManager extends BaseManager implements ManagerInterface {

    public String execute(String action) {
        if ("chooseRole".equals(action)) {
            return chooseRole();
        } else if ("chooseOperationPermission".equals(action)) {
            return chooseOperationPermission();
        } else if ("update".equals(action)) {
            return update();
        }
        setErrorCode(ErrorMessage.ACTION_NO_FOUND);
        return null;
    }

    private String chooseRole() {
        List<Role> rolelist = null;
        RoleDao roleDao = new RoleDao();
        try {
            rolelist = roleDao.loadAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            roleDao.close();
        }
        request.setAttribute("Rolelist", rolelist);
        return "/system/operationpermission/chooseRole.jsp";
    }
	
    private String chooseOperationPermission() {
        String roleId = getParaValue("RoleId");
        Role role = null;
        RoleDao roleDao = new RoleDao();
        try {
            role = (Role) roleDao.findByID(roleId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            roleDao.close();
        }
        List<OperationPermission> operationPermissionList = OperationPermissionService.getInstance().getOperationPermissionList();

        List<RoleOperation> roleOperationList = null;

        RoleOperationDao roleOperationDao = new RoleOperationDao();
        try {
            roleOperationList = roleOperationDao.findByRoleId(roleId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            roleOperationDao.close();
        }
        Map<String, RoleOperation> roleOperationMap = new HashMap<String, RoleOperation>();
        if (roleOperationList != null) {
            for (RoleOperation roleOperation : roleOperationList) {
                roleOperationMap.put(roleOperation.getOperation(), roleOperation);
            }
        }
        
        request.setAttribute("role", role);
        request.setAttribute("operationPermissionList", operationPermissionList);
        request.setAttribute("roleOperationMap", roleOperationMap);
        return "/system/operationpermission/chooseOperationPermission.jsp";
    }
    
    private String update() {
        String roleId = getParaValue("RoleId");
        String[] operationPermissions = getParaArrayValue("operationPermission");
        
        List<RoleOperation> list = new ArrayList<RoleOperation>();
        if (operationPermissions != null) {
            for (String operation : operationPermissions) {
                RoleOperation roleOperation = new RoleOperation();
                roleOperation.setRoleId(roleId);
                roleOperation.setOperation(operation);
                list.add(roleOperation);
            }
        }
        
        RoleOperationDao roleOperationDao = new RoleOperationDao();
        try {
            roleOperationDao.deleteByRoleId(roleId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            roleOperationDao.close();
        }
        roleOperationDao = new RoleOperationDao();
        try {
            roleOperationDao.save(list);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            roleOperationDao.close();
        }
        return chooseRole();
    }
    
    private String successful() {
        return "";
    }
}
