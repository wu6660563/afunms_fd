/*
 * @(#)WindowsSoftwareSnmpIndicatorGather.java     v1.01, 2013 12 29
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.windows;

import java.util.Vector;

import com.afunms.gather.indicator.base.SnmpIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.Softwarecollectdata;

/**
 * ClassName:   WindowsSoftwareSnmpIndicatorGather.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 29 14:29:29
 */
public class WindowsSoftwareSnmpIndicatorGather extends SnmpIndicatorGather {

    private final static String[] OIDS = new String[] {
        "1.3.6.1.2.1.25.6.3.1.2",   // 名称
        "1.3.6.1.2.1.25.6.3.1.3",   // id
        "1.3.6.1.2.1.25.6.3.1.4",   // 类别
        "1.3.6.1.2.1.25.6.3.1.5" }; // 安装日期

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
        Vector<Softwarecollectdata> softwareVector = new Vector<Softwarecollectdata>();
        if (valueArray != null) {
            for (int i = 0; i < valueArray.length; i++) {
                String name = valueArray[i][0];
                String swid = valueArray[i][1];
                String type = valueArray[i][2];
                String insdate = valueArray[i][3];

                if ("4".equalsIgnoreCase(type)) {
                    type = "应用软件";
                } else {
                    type = "系统软件";
                }

                if (insdate == null) {
                    continue;
                }
                String swdate = getDate(insdate);

                softwareVector.addElement(createSoftwarecollectdata(name, swid, type, swdate));
            }
        }
        return createSimpleIndicatorValue(softwareVector);
    }

    public Softwarecollectdata createSoftwarecollectdata(String name, String swid, String type, String swdate) {
        Softwarecollectdata softwarecollectdata = new Softwarecollectdata();
        softwarecollectdata.setIpaddress(getIpAddress());
        softwarecollectdata.setName(name);
        softwarecollectdata.setSwid(swid);
        softwarecollectdata.setType(type);
        softwarecollectdata.setInsdate(swdate);
        return softwarecollectdata;
    }

    public String getDate(String swdate) {
        String[] num = swdate.split(":");
        String num1 = Integer.valueOf(num[0], 16).toString();
        String num2 = Integer.valueOf(num[1], 16).toString();
        String num3 = Integer.valueOf(num[2], 16).toString();
        String num4 = Integer.valueOf(num[3], 16).toString();
        String num5 = Integer.valueOf(num[4], 16).toString();
        String num6 = Integer.valueOf(num[5], 16).toString();
        String num7 = Integer.valueOf(num[6], 16).toString();
        String num8 = Integer.valueOf(num[7], 16).toString();
        String swyear = Integer.parseInt(num1) * 256 + Integer.parseInt(num2)
                + "";
        String swnewdate = swyear + "-" + num3 + "-" + num4 + " " + num5 + ":"
                + num6 + ":" + num7 + ":" + num8;
        return swnewdate;

    }
}

