/*
 * @(#)GeneralPingIndicatorGather.java     v1.01, 2014 1 2
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.general;

import java.util.Calendar;
import java.util.Vector;

import com.afunms.common.util.ping.PingUtil;
import com.afunms.gather.indicator.base.BaseIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.Pingcollectdata;

/**
 * ClassName:   GeneralPingIndicatorGather.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 2 22:30:17
 */
public class GeneralPingIndicatorGather extends BaseIndicatorGather {

    /**
     * afterGetSimpleIndicatorValue:
     *
     *
     * @since   v1.01
     * @see com.afunms.gather.indicator.base.BaseIndicatorGather#afterGetSimpleIndicatorValue()
     */
    @Override
    public void afterGetSimpleIndicatorValue() {

    }

    /**
     * beforeGetSimpleIndicatorValue:
     *
     *
     * @since   v1.01
     * @see com.afunms.gather.indicator.base.BaseIndicatorGather#beforeGetSimpleIndicatorValue()
     */
    @Override
    public void beforeGetSimpleIndicatorValue() {

    }

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
        int[] result = getResult(getIpAddress());
        int connect = result[0];
        int respoonseTime = result[1];
        
        if (connect > 0 && respoonseTime == 0) {
            respoonseTime = new Double(Math.random() * 5).intValue();
        }
        
        if (connect > 0 && respoonseTime == 0) {
            respoonseTime = new Double(Math.random() * 5).intValue();
        }
        
        Vector<Pingcollectdata> vector = new Vector<Pingcollectdata>();
        Calendar date = getCalendar();
        vector.add(createPingcollectdata(date, "Utilization", "ConnectUtilization", String.valueOf(connect), RESTYPE_DYNAMIC, "%", ""));
        vector.add(createPingcollectdata(date, "ResponseTime", "ResponseTime", String.valueOf(respoonseTime), RESTYPE_DYNAMIC, "∫¡√Î", ""));
        return createSimpleIndicatorValue(vector);
    }

    public Pingcollectdata createPingcollectdata(Calendar date, String entity, String subentity, String thevalue, String restype, String unit, String chname) {
        Pingcollectdata pingcollectdata = new Pingcollectdata();
        pingcollectdata.setIpaddress(getIpAddress());
        pingcollectdata.setCollecttime(date);
        pingcollectdata.setCategory("Ping");
        pingcollectdata.setEntity(entity);
        pingcollectdata.setSubentity(subentity);
        pingcollectdata.setThevalue(thevalue);
        pingcollectdata.setRestype(restype);
        pingcollectdata.setUnit(unit);
        pingcollectdata.setChname(chname);
        return pingcollectdata;
    }

    public int[] getResult(String ipAddress) {
        return PingUtil.ping(ipAddress);
    }
}

