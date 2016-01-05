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
 * @author      ����
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
        device_Status.put("1", "δ֪");
        device_Status.put("2", "����");
        device_Status.put("3", "�澯");
        device_Status.put("4", "����");
        device_Status.put("5", "ֹͣ");
    };

    private static Hashtable<String, String> device_Type = null;

    static {
        device_Type = new Hashtable<String, String>();
        device_Type.put("1.3.6.1.2.1.25.3.1.1", "����");
        device_Type.put("1.3.6.1.2.1.25.3.1.2", "δ֪");
        device_Type.put("1.3.6.1.2.1.25.3.1.3", "CPU");
        device_Type.put("1.3.6.1.2.1.25.3.1.4", "����");
        device_Type.put("1.3.6.1.2.1.25.3.1.5", "��ӡ��");
        device_Type.put("1.3.6.1.2.1.25.3.1.6", "����");
        device_Type.put("1.3.6.1.2.1.25.3.1.10", "�Կ�");
        device_Type.put("1.3.6.1.2.1.25.3.1.11", "����");
        device_Type.put("1.3.6.1.2.1.25.3.1.12", "Э������");
        device_Type.put("1.3.6.1.2.1.25.3.1.13", "����");
        device_Type.put("1.3.6.1.2.1.25.3.1.14", "���ƽ����");
        device_Type.put("1.3.6.1.2.1.25.3.1.15", "����");
        device_Type.put("1.3.6.1.2.1.25.3.1.16", "��ӡ��");
        device_Type.put("1.3.6.1.2.1.25.3.1.17", "����");
        device_Type.put("1.3.6.1.2.1.25.3.1.18", "�Ŵ�");
        device_Type.put("1.3.6.1.2.1.25.3.1.19", "ʱ��");
        device_Type.put("1.3.6.1.2.1.25.3.1.20", "��̬�ڴ�");
        device_Type.put("1.3.6.1.2.1.25.3.1.21", "�̶��ڴ�");
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

