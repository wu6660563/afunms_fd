/*
 * @(#)GeneralRouterSnmpIndicatorGather.java     v1.01, 2013 12 30
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.general;

import java.util.Calendar;
import java.util.Vector;

import com.afunms.gather.indicator.base.SnmpIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.IpRouter;

/**
 * ClassName:   GeneralRouterSnmpIndicatorGather.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 30 09:40:55
 */
public class GeneralRouterSnmpIndicatorGather extends SnmpIndicatorGather {

    private final static String[] OIDS = new String[] {
        "1.3.6.1.2.1.4.21.1.2",     // 0.if
        // index
        "1.3.6.1.2.1.4.21.1.1",     // 1.ipRouterDest
        "1.3.6.1.2.1.4.21.1.7",     // 7.ipRouterNextHop
        "1.3.6.1.2.1.4.21.1.8",     // 8.ipRouterType
        "1.3.6.1.2.1.4.21.1.9",     // 9.ipRouterProto
        "1.3.6.1.2.1.4.21.1.11"     // 11.ipRouterMask
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
        Vector<IpRouter> ipRouterVector = new Vector<IpRouter>();
        try {
            if (valueArray != null) {
                Calendar date = getCalendar();
                for (int i = 0; i < valueArray.length; i++) {
                    String ifIndex = valueArray[i][0];
                    String dest = valueArray[i][1];
                    String nexthop = valueArray[i][2];
                    String type = valueArray[i][3];
                    String proto = valueArray[i][4];
                    String mask = valueArray[i][5];
                    if (ifIndex == null) {
                        continue;
                    }
                    ipRouterVector.add(createIpRouter(date, ifIndex, dest, nexthop, type, proto, mask));
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return createSimpleIndicatorValue(ipRouterVector);
    }

    public IpRouter createIpRouter(Calendar date, String ifIndex, String dest, String nexthop, String type, String proto, String mask) {
        IpRouter ipRouter = new IpRouter();
        ipRouter.setCollecttime(date);
        ipRouter.setIfindex(ifIndex);
        ipRouter.setDest(dest);
        ipRouter.setNexthop(nexthop);
        ipRouter.setType(new Long(type));
        ipRouter.setProto(new Long(proto));
        ipRouter.setMask(mask);
        return ipRouter;
    }
}

