package com.afunms.system.model;

import com.afunms.common.base.BaseVo;

public class RoleOperation extends BaseVo {
    
    private int id;
    
    private String roleId;
    
    private String operation;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the roleId
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * @return the operation
     */
    public String getOperation() {
        return operation;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param roleId the roleId to set
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    /**
     * @param operation the operation to set
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }
    
}
