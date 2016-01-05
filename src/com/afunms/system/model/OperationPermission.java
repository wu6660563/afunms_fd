package com.afunms.system.model;

import java.util.ArrayList;
import java.util.List;

public class OperationPermission {

    /**
     * 操作权限名称
     */
    private String name;
    
    /**
     * 操作权限代号
     */
    private String code;

    /**
     * 操作
     */
    private List<Operation> operationlist = new ArrayList<Operation>();

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    public void addOperation(Operation operation) {
        getOperationlist().add(operation);
    }

    /**
     * @return the operationlist
     */
    public List<Operation> getOperationlist() {
        return operationlist;
    }

    /**
     * @param operationlist the operationlist to set
     */
    public void setOperationlist(List<Operation> operationlist) {
        this.operationlist = operationlist;
    }
    
}
