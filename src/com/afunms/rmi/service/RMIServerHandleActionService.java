package com.afunms.rmi.service;

import java.util.HashMap;
import java.util.List;

public class RMIServerHandleActionService {

    /**
     * 单例模式
     */
    private static RMIServerHandleActionService instance;

    /**
     * 私有的构造方法
     */
    private RMIServerHandleActionService() {
        
    }

    public static RMIServerHandleActionService getInstance() {
        if (instance == null) {
            instance = new RMIServerHandleActionService(); 
        } 
        return instance;
    }

    private HashMap<String, RMIServerHandleActionConfig> RMIServerHandleActionMap = new HashMap<String, RMIServerHandleActionConfig>();

    @SuppressWarnings("unchecked")
    public RMIAttribute handleAction(RMIClientAction clientAction) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        RMIAttribute attribute = null;
        try {
            String action = clientAction.getAction();
            RMIServerHandleActionConfig serverHandleActionConfig = RMIServerHandleActionMap.get(action);
            if (serverHandleActionConfig == null) {
                return null;
            }
            Class<RMIServerHandleAction>  RMIServerHandleActionClass = (Class<RMIServerHandleAction>) Class.forName(serverHandleActionConfig.getRMIServerHandleActionClassName());
            attribute = RMIServerHandleActionClass.newInstance().execute(clientAction.getParameter());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attribute;
    }

    public void addRMIServerHandleAction(List<RMIServerHandleActionConfig> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        for (RMIServerHandleActionConfig serverHandleActionConfig : list) {
            RMIServerHandleActionMap.put(serverHandleActionConfig.getName(), serverHandleActionConfig);
        }
    }

    public RMIServerHandleActionConfig remove(String serverHandleActionName) {
        return RMIServerHandleActionMap.remove(serverHandleActionName);
    }
}
