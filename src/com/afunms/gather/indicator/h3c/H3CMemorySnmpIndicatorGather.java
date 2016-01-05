/*
 * @(#)H3CMemorySnmpIndicatorGather.java     v1.01, 2014 1 6
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.h3c;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.gather.indicator.base.SnmpIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.Memorycollectdata;

/**
 * ClassName:   H3CMemorySnmpIndicatorGather.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 6 22:37:14
 */
public class H3CMemorySnmpIndicatorGather extends SnmpIndicatorGather {

    private final static String[] OIDS = new String[] {
        "1.3.6.1.4.1.2011.6.1.2.1.1.2",         // hwMemSize
        "1.3.6.1.4.1.2011.6.1.2.1.1.3"          // hwMemFree
    };

    private final static String[] OIDS_2 = new String[] {
        "1.3.6.1.4.1.2011.2.2.5.1",             // 已用内存
        "1.3.6.1.4.1.2011.2.2.5.2"              // 空闲内存
    };

    private final static String[] OIDS_3 = new String[] {
        "1.3.6.1.4.1.2011.6.3.5.1.1.2",         // 已用内存
        "1.3.6.1.4.1.2011.6.3.5.1.1.3"          // 空闲内存
    };

    private final static String[] OIDS_4 = new String[] {
        "1.3.6.1.4.1.2011.10.2.6.1.1.1.1.8"     // 内存利用率
    };

    private final static String[] OIDS_5 = new String[] {
        "1.3.6.1.4.1.2011.2.17.6.9.1.2"         // 内存利用率
    };

    private final static String[] OIDS_6 = new String[] {
        "1.3.6.1.4.1.2011.5.25.31.1.1.1.1.7"    // 内存利用率
    };

    private final static String[] OIDS_7 = new String[] {
        "1.3.6.1.4.1.25506.2.6.1.1.1.1.8"       // 内存利用率
    };

    private final static String SYS_OID = "1.3.6.1.4.1.2011.1.1.1.12811";

    private final static String SYS_OID_2 = "1.3.6.1.4.1.2011.10.1.89";

    private final static String SYS_OID_3 = "1.3.6.1.4.1.2011.2.170.2";

    private final static String SYS_OID_4 = "1.3.6.1.4.1.2011.2.170.3";

    private final static String SYS_OID_5 = "1.3.6.1.4.1.2011.2.45";

    private final static String SYS_OID_6 = "1.3.6.1.4.1.2011.2.31";

    private final static String SYS_OID_7 = "1.3.6.1.4.1.2011.2.62.2.5";

    private final static String SYS_OID_8 = "1.3.6.1.4.1.2011.2.88.2";

    private final static String SYS_OID_9 = "1.3.6.1.4.1.2011.2.62.2.9";

    private final static String SYS_OID_10 = "1.3.6.1.4.1.2011.2.62.2.3";

    private final static String SYS_OID_11 = "1.3.6.1.4.1.2011.2.23.97";
    
    private final static String CALUCLATION_SIZE = "size";
    
    private final static String CALUCLATION_PERCENTAGE = "percentage";

    /**
     * calculation:
     * <p>计算方式
     *
     * @since   v1.01
     */
    private String calculation;
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
        List<String[]> oidsList = new ArrayList<String[]>();
        Hashtable<String[], String> calculationHashtable = new Hashtable<String[], String>();
        if (sysOid.startsWith("1.3.6.1.4.1.2011.")) {
            oids = OIDS;
            calculationHashtable.put(oids, CALUCLATION_SIZE);
            if (sysOid.equals(SYS_OID) || sysOid.equals(SYS_OID_2)) {
                oids = OIDS_2;
                calculationHashtable.put(oids, CALUCLATION_SIZE);
            } else if (sysOid.equals(SYS_OID_3)) {
                oids = OIDS_3;
                calculationHashtable.put(oids, CALUCLATION_SIZE);
            } else if (sysOid.equals(SYS_OID_4)) {
                oids = OIDS_3;
                calculationHashtable.put(oids, CALUCLATION_SIZE);
            } else if (sysOid.equals(SYS_OID_5)) {
                oids = OIDS_4;
                calculation = CALUCLATION_PERCENTAGE;
                calculationHashtable.put(oids, calculation);
            } else if (sysOid.equals(SYS_OID_6)) {
                oids = OIDS_5;
                calculationHashtable.put(oids, CALUCLATION_PERCENTAGE);
            } else if (sysOid.equals(SYS_OID_7) || sysOid.equals(SYS_OID_8)) {
                oids = OIDS_6;
                calculationHashtable.put(oids, CALUCLATION_PERCENTAGE);
            } else if (sysOid.equals(SYS_OID_9)) {
                oids = OIDS_6;
                calculationHashtable.put(oids, CALUCLATION_PERCENTAGE);
            } else if (sysOid.equals(SYS_OID_10)) {
                oids = OIDS_6;
                calculationHashtable.put(oids, CALUCLATION_PERCENTAGE);
            } else if (sysOid.equals(SYS_OID_11)) {
                oids = OIDS_6;
                calculationHashtable.put(oids, CALUCLATION_PERCENTAGE);
            } else {
                oidsList.add(OIDS_3);
                calculationHashtable.put(OIDS_3, CALUCLATION_SIZE);
            }
        } else if (sysOid.startsWith("1.3.6.1.4.1.25506.")) {
            oids = OIDS_7;
            calculationHashtable.put(oids, CALUCLATION_PERCENTAGE);
        }

        oidsList.add(oids);
        String[][] valueArray = getTableValuesByOids(oidsList);
        calculation = calculationHashtable.get(defaultOids);
        Vector<Memorycollectdata> memoryVector = new Vector<Memorycollectdata>();
        if (valueArray != null) {
            Calendar date = getCalendar();
            Double allValue = 0D;
            if (CALUCLATION_SIZE.equals(calculation)) {
                for (int i = 0; i < valueArray.length; i++) {
                    try {
                        if (valueArray[i][0] == null || valueArray[i][1] == null) {
                            continue;
                        }
                        Double usedValue = Double.valueOf(valueArray[i][0]);
                        Double freeValue = Double.valueOf(valueArray[i][1]);
                        if (usedValue + freeValue > 0D) {
                            allValue += format(usedValue * 100D / (usedValue + freeValue));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                for (int i = 0; i < valueArray.length; i++) {
                    try {
                        String value = valueArray[i][0];
                        allValue += Double.valueOf(value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            String value = "0";
            if (valueArray.length > 0) {
                value = String.valueOf(format(allValue / (valueArray.length)));
            }
            memoryVector.add(createMemorycollectdata(date, "Utilization", "Utilization", value, "%"));
        }
        return createSimpleIndicatorValue(memoryVector);
    }

    public Memorycollectdata createMemorycollectdata(Calendar date, String entity, String subentity, String thevalue, String unit) {
        Memorycollectdata memorydata = new Memorycollectdata();
        memorydata.setIpaddress(getIpAddress());
        memorydata.setCollecttime(date);
        memorydata.setCategory("Memory");
        memorydata.setEntity(entity);
        memorydata.setSubentity(subentity);
        memorydata.setRestype("dynamic");
        memorydata.setThevalue(thevalue);
        memorydata.setUnit(unit);
        return memorydata;
    }
}

