/*
 * @(#)H3CCpuSnmpIndicatorGather.java     v1.01, 2013 12 27
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.h3c;

import java.util.List;
import java.util.Vector;

import com.afunms.gather.indicator.general.GeneralCpuSnmpIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;

/**
 * ClassName:   H3CCpuSnmpIndicatorGather.java
 * <p>
 *
 * @author      ƒÙ¡÷
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 27 01:34:17
 */
public class H3CCpuSnmpIndicatorGather extends GeneralCpuSnmpIndicatorGather {

    private final static String[] OIDS = new String[] { "1.3.6.1.4.1.2011.6.1.1.1.4" };
    
    private final static String[] OIDS_2 = new String[] { "1.3.6.1.4.1.2011.10.2.6.1.1.1.1.6" };

    private final static String[] OIDS_3 = new String[] { "1.3.6.1.4.1.2011.2.17.4.4.1.7" };

    private final static String[] OIDS_4 = new String[] { "1.3.6.1.4.1.2011.5.25.31.1.1.1.1.5" };

    private final static String[] OIDS_5 = new String[] { "1.3.6.1.4.1.2011.6.3.4.1.2" };

    private final static String[] OIDS_6 = new String[] { "1.3.6.1.4.1.25506.2.6.1.1.1.1.6" };

    private final static String SYS_OID = "1.3.6.1.4.1.2011.2.31";

    private final static String SYS_OID_2 = "1.3.6.1.4.1.2011.2.62.2.5";
    
    private final static String SYS_OID_3 = "1.3.6.1.4.1.2011.2.88.2";

    private final static String SYS_OID_4 = "1.3.6.1.4.1.2011.2.62.2.3";

    private final static String SYS_OID_5 = "1.3.6.1.4.1.2011.2.62.2.9";

    private final static String SYS_OID_6 = "1.3.6.1.4.1.2011.2.23.97";

    private final static String SYS_OID_7 = "1.3.6.1.4.1.2011.10.1.88";

    private final static String SYS_OID_8 = "1.3.6.1.4.1.2011.2.170.2";

    private final static String SYS_OID_9 = "1.3.6.1.4.1.2011.2.170.3";
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
        String sysOid = getNodeSysOid();
        String[] oids = null;
        if (sysOid.startsWith("1.3.6.1.4.1.2011.")) {
            if (sysOid.equals(SYS_OID)) {
                oids = OIDS_3;
            } else if (sysOid.equals(SYS_OID_2) || sysOid.equals(SYS_OID_3)) {
                oids = OIDS_4;
            } else if (sysOid.equals(SYS_OID_4)) {
                oids = OIDS_4;
            } else if (sysOid.trim().equals(SYS_OID_5)) {
                oids = OIDS_4;
            } else if (sysOid.equals(SYS_OID_6)) {
                oids = OIDS_4;
            } else if (sysOid.equals(SYS_OID_7)) {
                oids = OIDS_4;
            } else if (sysOid.equals(SYS_OID_8)) {
                oids = OIDS_5;
            } else if (sysOid.equals(SYS_OID_9)) {
                oids = OIDS_5;
            } else {
                oids = OIDS;
            }
        } else if (sysOid.startsWith("1.3.6.1.4.1.25506.")) {
            oids = OIDS_6;
        }
        List<String[]> oidsList = new Vector<String[]>();
        oidsList.add(oids);
        oidsList.add(OIDS_2);
        oidsList.add(OIDS_5);
        return getSimpleIndicatorValue(oidsList);
    }
}

