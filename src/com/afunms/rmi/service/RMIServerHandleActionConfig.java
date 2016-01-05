package com.afunms.rmi.service;

public class RMIServerHandleActionConfig {

    private String name;
    
    private String RMIServerHandleActionClassName;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the rMIServerHandleActionClassName
     */
    public String getRMIServerHandleActionClassName() {
        return RMIServerHandleActionClassName;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param serverHandleActionClassName the rMIServerHandleActionClassName to set
     */
    public void setRMIServerHandleActionClassName(String serverHandleActionClassName) {
        RMIServerHandleActionClassName = serverHandleActionClassName;
    }
    
}
