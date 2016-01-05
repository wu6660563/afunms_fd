package com.afunms.system.service;

import java.util.HashMap;
import java.util.Map;

import com.afunms.system.model.Operation;

public class OperationService {

    private static OperationService instance = null;

    private Map<String, Operation> operationMap = new HashMap<String, Operation>();

    public static final String CreateOperationCode = "create";
    
    public static final String UpdateOperationCode = "update";
    
    public static final String DeleteOperationCode = "delete";

    public static final String RetrieveOperationCode = "retrieve";

    private OperationService() {
    }

    public static OperationService getInstance() {
        if (instance == null) {
            instance = new OperationService();
        }
        return instance;
    }

    public void addOperation(Operation operation) {
        operationMap.put(operation.getCode(), operation);
    }

    public Operation getOperation(String code) {
        return operationMap.get(code);
    }

    public static Operation getCreateOperation() {
        return getInstance().getOperation(CreateOperationCode);
    }
    
    public static Operation getUpdateOperation() {
        return getInstance().getOperation(UpdateOperationCode);
    }
    
    public static Operation getDeleteOperation() {
        return getInstance().getOperation(DeleteOperationCode);
    }

    public static Operation getRetrieveOperation() {
        return getInstance().getOperation(RetrieveOperationCode);
    }
}
