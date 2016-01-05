package com.afunms.rmi.service;

import java.io.Serializable;
import java.rmi.RemoteException;


public class RMIClientService implements Serializable {

    /**
     * 序列化 ID
     */
    private static final long serialVersionUID = 6716584797767260028L;

    private RMIClientImpl_Stub clientImpl = null;

    public boolean connect(String host, String port, String name) {
        boolean result = false;
        clientImpl = (RMIClientImpl_Stub) RMIConnectService.connect(host, port, name);
        if (clientImpl != null) {
            result = true;
        }
        return result;
    }

    public boolean connect(String url) {
        boolean result = false;
        clientImpl = (RMIClientImpl_Stub) RMIConnectService.connect(url);
        if (clientImpl != null) {
            result = true;
        }
        return result;
    }

    public void sendMessage(String action, RMIParameter parameter, RMIClientCallBackAction clientCallBackAction) throws RemoteException {
        if (clientImpl != null) {
            RMIClientAction clientAction = new RMIClientAction();
            clientAction.setAction(action);
            clientAction.setParameter(parameter);
            clientAction.setClientCallBackAction(clientCallBackAction);
            clientAction.setClientService(this);
            excuteMessage(clientAction);
        }
    }
    
    public void addMessage(String action, RMIParameter parameter, RMIClientCallBackAction clientCallBackAction) throws RemoteException {
        if (clientImpl != null) {
            RMIClientAction clientAction = new RMIClientAction();
            clientAction.setAction(action);
            clientAction.setParameter(parameter);
            clientAction.setClientCallBackAction(clientCallBackAction);
            clientAction.setClientService(this);
            RMIClientActionThread.getInstance().add(clientAction); 
        }
    }
    
    void excuteMessage(RMIClientAction clientAction) throws RemoteException {
        RMIClientCallBackAction clientCallBackAction = clientAction.getClientCallBackAction();
        clientAction.setClientCallBackAction(null);
        RMIClientImpl_Stub clientImpl = clientAction.getClientService().getRMIClientImpl();
        if (clientImpl == null) {
            throw new NullPointerException("当前客户端连接服务未连接！");
        }
        RMIAttribute attribute = clientImpl.sendMessage(clientAction);
        if (clientCallBackAction != null) {
            clientCallBackAction.callBack(clientAction.getParameter(), attribute);
        }
    }

    RMIClientImpl_Stub getRMIClientImpl() {
        return clientImpl;
    }
}
