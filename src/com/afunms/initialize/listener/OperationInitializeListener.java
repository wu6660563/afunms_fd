/*
 * @(#)OperationInitializeListener.java     v1.01, 2014 1 9
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.initialize.listener;

import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import com.afunms.common.util.SysLogger;
import com.afunms.system.model.Operation;
import com.afunms.system.model.OperationPermission;
import com.afunms.system.service.OperationPermissionService;
import com.afunms.system.service.OperationService;

/**
 * ClassName:   OperationInitializeListener.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 9 10:41:46
 */
public class OperationInitializeListener extends AbstractInitializeListener {

    /**
     * logger:
     * <p>日志
     *
     * @since   v1.01
     */
    private static SysLogger logger = SysLogger.getLogger(OperationInitializeListener.class.getName());

    private final static String operationPermissionFile = "WEB-INF/classes/operationPermission.xml";

    /**
     * init:
     *
     * @param configFile
     * @return
     *
     * @since   v1.01
     * @see com.afunms.initialize.SysInitializeListener#init(java.lang.String)
     */
    @SuppressWarnings({ "unchecked", "static-access" })
    public boolean init(String configFile) {
        try {
            Document doc = getDocument(configFile);
            List<Element> list = (List<Element>) doc.getRootElement().getChildren("operation");
            Iterator<Element> it = list.iterator();
            while(it.hasNext()) {
                Element element = (Element)it.next();
                String name = element.getChild("name").getText();
                String code = element.getChild("code").getText();
                Operation operation = new Operation(name, code);
                OperationService.getInstance().addOperation(operation);
            }
        } catch(Exception e) {
            logger.error("初始化 操作 配置信息出错！！！", e);
        }
        try {
        	System.out.println(getResourceCenter().getSysPath() + "    " + operationPermissionFile);
            Document doc = getDocument(getResourceCenter().getSysPath() + operationPermissionFile);
            List list = doc.getRootElement().getChildren("operation-permission");
            Iterator it = list.iterator();
            while(it.hasNext()) {
                Element element = (Element)it.next();
                String name = element.getChild("name").getText();
                String code = element.getChild("code").getText();
                List codeList = element.getChild("operation-codes").getChildren("code");
                
                OperationPermission operationPermission = new OperationPermission();
                operationPermission.setName(name);
                operationPermission.setCode(code);
                for (Object object : codeList) {
                    Element codeElement = (Element) object;
                    String operationCode = codeElement.getText();
                    Operation operation = OperationService.getInstance().getOperation(operationCode);
                    operationPermission.addOperation(operation);
                }
                OperationPermissionService.getInstance().addOperationPermission(operationPermission);
            }
        } catch(Exception e) {
            logger.error("初始化 操作 配置信息出错！！！", e);
        }
        return false;
    }

}

