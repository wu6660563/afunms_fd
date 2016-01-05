package com.afunms.rmi.service;

import java.io.Serializable;

public class RMIClientAction implements Serializable {

    /**
     * –Ú¡–ªØID
     */
    private static final long serialVersionUID = 7552559235627141419L;

    private String action;
    
    private RMIParameter parameter;
    
    private RMIClientCallBackAction clientCallBackAction;

    private RMIClientService clientService;

    /**
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * @return the parameter
     */
    public RMIParameter getParameter() {
        return parameter;
    }

    /**
     * @return the clientCallBackAction
     */
    public RMIClientCallBackAction getClientCallBackAction() {
        return clientCallBackAction;
    }

    /**
     * @return the clientService
     */
    public RMIClientService getClientService() {
        return clientService;
    }

    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * @param parameter the parameter to set
     */
    public void setParameter(RMIParameter parameter) {
        this.parameter = parameter;
    }

    /**
     * @param clientCallBackAction the clientCallBackAction to set
     */
    public void setClientCallBackAction(RMIClientCallBackAction clientCallBackAction) {
        this.clientCallBackAction = clientCallBackAction;
    }

    /**
     * @param clientService the clientService to set
     */
    public void setClientService(RMIClientService clientService) {
        this.clientService = clientService;
    }
    
    
}
