package com.afunms.system.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.afunms.system.model.OperationPermission;

public class OperationPermissionService {

    private static OperationPermissionService instance = null;

    private Map<String, OperationPermission> operationPermissionMap = new HashMap<String, OperationPermission>();

    private OperationPermissionService() {
    }

    public static OperationPermissionService getInstance() {
        if (instance == null) {
            instance = new OperationPermissionService();
        }
        return instance;
    }

    public void addOperationPermission(OperationPermission operationPermission) {
        operationPermissionMap.put(operationPermission.getCode(),
                operationPermission);
    }

    public OperationPermission getOperationPermission(String code) {
        return operationPermissionMap.get(code);
    }

    public List<OperationPermission> getOperationPermissionList() {
        List<OperationPermission> list = new ArrayList<OperationPermission>();
        Iterator<String> iterator = operationPermissionMap.keySet().iterator();
        while (iterator.hasNext()) {
            String code = iterator.next();
            OperationPermission operationPermission = operationPermissionMap.get(code);
            list.add(operationPermission);
        }
        return list;
    }

    public static OperationPermission getNodeOperationPermission() {
        return getInstance().getOperationPermission("node");
    }
    
    public static OperationPermission getGatherOperationPermission() {
        return getInstance().getOperationPermission("gather");
    }

    public static OperationPermission getThresholdOperationPermission() {
        return getInstance().getOperationPermission("threshold");
    }
    
    public static OperationPermission getProcessOperationPermission() {
        return getInstance().getOperationPermission("Process");
    }
    
    public static OperationPermission getHostServiceOperationPermission() {
        return getInstance().getOperationPermission("HostService");
    }

    public static OperationPermission getDBOperationPermission() {
        return getInstance().getOperationPermission("DB");
    }

    public static OperationPermission getAlarmWayOperationPermission() {
        return getInstance().getOperationPermission("alarmWay");
    }

    public static OperationPermission getDepartmentOperationPermission() {
        return getInstance().getOperationPermission("department");
    }

    public static OperationPermission getPositionOperationPermission() {
        return getInstance().getOperationPermission("position");
    }

    public static OperationPermission getSupperOperationPermission() {
        return getInstance().getOperationPermission("supper");
    }

    public static OperationPermission getKnowledgebaseOperationPermission() {
        return getInstance().getOperationPermission("knowledgebase");
    }

    public static OperationPermission getKnowledgesOperationPermission() {
        return getInstance().getOperationPermission("knowledges");
    }

    public static OperationPermission getUserOperationPermission() {
        return getInstance().getOperationPermission("user");
    }

    public static OperationPermission getUserManagerOperationPermission() {
        return getInstance().getOperationPermission("userManager");
    }

    public static OperationPermission getRoleOperationPermission() {
        return getInstance().getOperationPermission("role");
    }

    public static OperationPermission getAdminOperationPermission() {
        return getInstance().getOperationPermission("admin");
    }

    public static OperationPermission getTopologyOperationPermission() {
        return getInstance().getOperationPermission("topology");
    }

    public static OperationPermission getTopologyNodeOperationPermission() {
        return getInstance().getOperationPermission("topologyNode");
    }

    public static OperationPermission getTopologyHinNodeOperationPermission() {
        return getInstance().getOperationPermission("topologyHinNode");
    }

    public static OperationPermission getTopologyLineOperationPermission() {
        return getInstance().getOperationPermission("topologyLine");
    }

    public static OperationPermission getTopologyHinLineOperationPermission() {
        return getInstance().getOperationPermission("topologyHinLine");
    }  
}