/*
 * @(#)RouterPersistenceIndicatorValueAction.java     v1.01, 2014 1 2
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.indicator.action.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.afunms.node.indicator.action.PersistenceIndicatorValueAction;
import com.afunms.polling.om.IpRouter;

/**
 * ClassName:   RouterPersistenceIndicatorValueAction.java
 * <p>{@link RouterPersistenceIndicatorValueAction} Router 指标的持久化处理
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 2 22:01:47
 */
public class RouterPersistenceIndicatorValueAction extends
                PersistenceIndicatorValueAction {

    private final static String TABLE_NAME_DATA_TEMP = "nms_route_data_temp";

    private final static String[] iproutertype = { "", "", "", "direct(3)", "indirect(4)" };

    private final static String[] iprouterproto = {
        "", "other(1)", "local(2)", "netmgmt(3)", "icmp(4)",
        "egp(5)", "ggp(6)", "hello(7)", "rip(8)",
        "is-is(9)", "es-is(10)", "ciscoIgrp(11)", "bbnSpfIgp(12)",
        "ospf(13)", "bgp(14)"
    };

/**
     * executeToDB:
     *
     *
     * @since   v1.01
     * @see com.afunms.node.indicator.action.PersistenceIndicatorValueAction#executeToDB()
     */
    @SuppressWarnings("unchecked")
    @Override
    public void executeToDB() {
        Vector<IpRouter> vector = (Vector<IpRouter>) getIndicatorValue().getValue();
        if (vector == null) {
            return;
        }
        List<String[]> dataTempList = new ArrayList<String[]>();
        for (IpRouter ipRouter : vector) {
            dataTempList.add(createDataTempArray(ipRouter));
        }

        // 删除临时数据
        String deleteDataTempSQL = "delete from " + TABLE_NAME_DATA_TEMP + " where nodeid='" + getNodeId() + "'";
        executeSQL(deleteDataTempSQL);

        // 保存临时数据
        String dataTempSQL = "insert into " + TABLE_NAME_DATA_TEMP + "(nodeid,ip,type,subtype,ifindex,nexthop,proto,rtype,mask,collecttime,physaddress,dest)"
        + " values(?,?,?,?,?,?,?,?,?,?,?,?)";
        executePreparedSQL(dataTempSQL, dataTempList);

    }

    /**
     * createDataTempArray:
     * <p>创建 临时数据 的数组
     *
     * @param   ipRouter
     *          - {@link IpRouter}
     * @return  {@link String[]}
     *          - 临时数据 的数组
     *
     * @since   v1.01
     */
    public String[] createDataTempArray(IpRouter ipRouter) {
        String[] array = new String[12];
        array[0] = getNodeId();
        array[1] = getIpAddress();
        array[2] = getNodeType();
        array[3] = getNodeSubtype();
        array[4] = ipRouter.getIfindex();
        array[5] = ipRouter.getNexthop();
        array[6] = iprouterproto[Integer.parseInt(ipRouter
                        .getProto().longValue()
                        + "")];
        array[7] = iproutertype[Integer.parseInt(ipRouter
                        .getType().longValue()
                        + "")];
        array[8] = ipRouter.getMask();
        array[9] = format(ipRouter.getCollecttime());
        array[10] = ipRouter.getPhysaddress();
        array[11] = ipRouter.getDest();
        return array;
    }
}

