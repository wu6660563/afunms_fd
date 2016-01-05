/*
 * @(#)DBAttributeAction.java     v1.01, 2013 12 20
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.synchronize.service;

import com.afunms.rmi.service.RMIParameter;
import com.gatherdb.DBAttribute;
import com.gatherdb.GathersqlListManager;
import com.gatherdb.ResultToDB;
import com.gatherdb.ResultTosql;

/**
 * ClassName:   DBAttributeAction.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 20 10:52:18
 */
public class DBAttributeAction implements DataAction {

    /**
     * action:
     *
     * @param parameter
     *
     * @since   v1.01
     * @see com.afunms.synchronize.service.DataAction#action(com.afunms.rmi.service.RMIParameter)
     */
    public void action(RMIParameter parameter) {
        try {
            DBAttribute attribute = (DBAttribute) parameter.getParameter("DBAttribute");
            if (attribute != null) {
                String className = (String) attribute
                        .getAttribute("className");
                ResultToDB resultToDB = new ResultToDB();
                resultToDB.setAttribute(attribute);
                resultToDB.setResultTosql((ResultTosql) Class.forName(
                        className).newInstance());
                GathersqlListManager.getInstance().addToQueue(
                        resultToDB);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}

