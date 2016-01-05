/*
 * @(#)WindowsServiceSnmpIndicatorGather.java     v1.01, 2013 12 29
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.windows;

import java.util.Vector;

import com.afunms.gather.indicator.base.SnmpIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.Servicecollectdata;

/**
 * ClassName:   WindowsServiceSnmpIndicatorGather.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 29 14:06:33
 */
public class WindowsServiceSnmpIndicatorGather extends SnmpIndicatorGather {

    private final static String[] OIDS = new String[] {
        "1.3.6.1.4.1.77.1.2.3.1.1", // 名称
        "1.3.6.1.4.1.77.1.2.3.1.2",
        "1.3.6.1.4.1.77.1.2.3.1.3",
        "1.3.6.1.4.1.77.1.2.3.1.4",
        "1.3.6.1.4.1.77.1.2.3.1.5" };

    /**
     * getSimpleIndicatorValue:
     *
     * @return
     *
     * @since   v1.01
     * @see com.afunms.gather.indicator.base.BaseIndicatorGather#getSimpleIndicatorValue()
     */
    @Override
    public SimpleIndicatorValue getSimpleIndicatorValue() {
        String[][] valueArray = getTableValuesByOids(OIDS);
        Vector<Servicecollectdata> serviceVector = new Vector<Servicecollectdata>();
        if (valueArray != null) {
            for (int i = 0; i < valueArray.length; i++) {
                String name = valueArray[i][0];
                String instate = valueArray[i][1];
                String opstate = valueArray[i][2];
                String uninst = valueArray[i][3];
                String paused = valueArray[i][4];

                if ("1".equalsIgnoreCase(instate)) {
                    instate = "已卸载";
                } else if ("2".equalsIgnoreCase(instate)) {
                    instate = "安装待批";
                } else if ("3".equalsIgnoreCase(instate)) {
                    instate = "卸载待批";
                } else {
                    instate = "已安装";
                }
                
                if ("1".equalsIgnoreCase(opstate)) {
                    opstate = "活动的";
                } else if ("2".equalsIgnoreCase(opstate)) {
                    opstate = "活动待批";
                } else if ("3".equalsIgnoreCase(opstate)) {
                    opstate = "暂停待批";
                } else {
                    opstate = "暂停的";
                }
                
                if ("1".equalsIgnoreCase(uninst)) {
                    uninst = "不能卸载";
                } else {
                    uninst = "允许卸载";
                }
                
                if ("1".equalsIgnoreCase(paused)) {
                    paused = "不能暂停";
                } else {
                    paused = "允许暂停";
                }

                serviceVector.add(createServicecollectdata(name, instate, opstate, uninst, paused));
            }
        }
        return createSimpleIndicatorValue(serviceVector);
    }

    public Servicecollectdata createServicecollectdata(String name, String instate, String opstate, String uninst, String paused) {
        Servicecollectdata servicedata = new Servicecollectdata();
        servicedata.setIpaddress(getIpAddress());
        servicedata.setName(name);
        servicedata.setInstate(instate);
        servicedata.setOpstate(opstate);
        servicedata.setUninst(uninst);
        servicedata.setPaused(paused);
        return servicedata;
    }
}

