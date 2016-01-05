/*
 * @(#)WindowsDeviceSnmpIndicatorGather.java     v1.01, 2013 12 27
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.windows;

import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.CommonUtil;
import com.afunms.gather.indicator.base.SnmpIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.Devicecollectdata;

/**
 * ClassName:   WindowsDeviceSnmpIndicatorGather.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 27 15:40:49
 */
public class WindowsDeviceSnmpIndicatorGather extends SnmpIndicatorGather {

    private final static String[] OIDS = new String[] {
        "1.3.6.1.2.1.25.3.2.1.1", // hrDeviceIndex
        "1.3.6.1.2.1.25.3.2.1.2", // hrDeviceType
        "1.3.6.1.2.1.25.3.2.1.3", // hrDeviceDescr
        "1.3.6.1.2.1.25.3.2.1.5"  // hrDeviceStatus
        }; 

    private static Hashtable<String, String> device_Status = null;

    static {
        device_Status = new Hashtable<String, String>();
        device_Status.put("1", "未知");
        device_Status.put("2", "运行");
        device_Status.put("3", "告警");
        device_Status.put("4", "测试");
        device_Status.put("5", "停止");
    };

    private static Hashtable<String, String> device_Type = null;

    static {
        device_Type = new Hashtable<String, String>();
        device_Type.put("1.3.6.1.2.1.25.3.1.1", "其他");
        device_Type.put("1.3.6.1.2.1.25.3.1.2", "未知");
        device_Type.put("1.3.6.1.2.1.25.3.1.3", "CPU");
        device_Type.put("1.3.6.1.2.1.25.3.1.4", "网络");
        device_Type.put("1.3.6.1.2.1.25.3.1.5", "打印机");
        device_Type.put("1.3.6.1.2.1.25.3.1.6", "磁盘");
        device_Type.put("1.3.6.1.2.1.25.3.1.10", "显卡");
        device_Type.put("1.3.6.1.2.1.25.3.1.11", "声卡");
        device_Type.put("1.3.6.1.2.1.25.3.1.12", "协处理器");
        device_Type.put("1.3.6.1.2.1.25.3.1.13", "键盘");
        device_Type.put("1.3.6.1.2.1.25.3.1.14", "调制解调器");
        device_Type.put("1.3.6.1.2.1.25.3.1.15", "并口");
        device_Type.put("1.3.6.1.2.1.25.3.1.16", "打印口");
        device_Type.put("1.3.6.1.2.1.25.3.1.17", "串口");
        device_Type.put("1.3.6.1.2.1.25.3.1.18", "磁带");
        device_Type.put("1.3.6.1.2.1.25.3.1.19", "时钟");
        device_Type.put("1.3.6.1.2.1.25.3.1.20", "动态内存");
        device_Type.put("1.3.6.1.2.1.25.3.1.21", "固定内存");
    };

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
        Vector<Devicecollectdata> deviceVector = new Vector<Devicecollectdata>();
        if (valueArray != null) {
            for (int i = 0; i < valueArray.length; i++) {
                String devindex = valueArray[i][0];
                String type = valueArray[i][1];
                String name = valueArray[i][2];
                String status = valueArray[i][3];
                if (status == null) {
                    status = "";
                }
                if (device_Status.containsKey(status)) {
                    status = (String) device_Status.get(status);
                }

                Devicecollectdata devicedata = new Devicecollectdata();
                name = CommonUtil.removeIllegalStr(name);
                devicedata.setDeviceindex(devindex);
                devicedata.setIpaddress(getIpAddress());
                devicedata.setName(name);
                devicedata.setStatus(status);
                devicedata.setType((String) device_Type.get(type));
                deviceVector.addElement(devicedata);
            }
        }
        return createSimpleIndicatorValue(deviceVector);
    }

}

