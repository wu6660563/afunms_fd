/*
 * @(#)SnmpIndicatorGather.java     v1.01, 2013 12 27
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.base;

import java.util.List;

import com.afunms.common.util.SnmpService;
import com.afunms.topology.model.HostNode;

/**
 * ClassName:   SnmpIndicatorGather.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 27 00:16:52
 */
public abstract class SnmpIndicatorGather extends BaseIndicatorGather {

    /**
     * snmp:
     * <p>SNMP 服务类
     *
     * @since   v1.01
     */
    protected static SnmpService snmp = new SnmpService(); 

    /**
     * defaultOids:
     * <p>默认的 OIDS
     *
     * @since   v1.01
     */
    protected String[] defaultOids = null;

    /**
     * community:
     * <p>读团体
     *
     * @since   v1.01
     */
    protected String community = null;

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
        this.ipAddress = getIndicatorInfo().getNodeDTO().getIpaddress();
        this.community = ((HostNode) getIndicatorInfo().getBaseVo()).getCommunity();
    }

    /**
     * getTableValuesByOids:
     * <p>通过 oids 获取 列表值
     *
     * @param   oids
     *          - oids
     * @return  {@link String[][]}
     *          - 值
     *
     * @since   v1.01
     */
    public String[][] getTableValuesByOids(String[] oids) {
        String[][] valueArray = null;
        try {
            valueArray = snmp.getCpuTableData(getIpAddress(), getCommunity(), oids);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return valueArray;
    }

    /**
     * getTableValuesByOids:
     * <p>从 OID 列表中选择一个 OID 进行执行
     *
     * @param oidsList
     * @return
     *
     * @since   v1.01
     */
    public String[][] getTableValuesByOids(List<String[]> oidsList) {
        String[][] valueArray = null;
        if (defaultOids == null) {
            for (String[] oids : oidsList) {
                valueArray = getTableValuesByOids(oids);
                if (valueArray != null)  {
                    for (String[] string : valueArray) {
                        if (string != null) {
                            defaultOids = oids;
                            break;
                        }
                    }
                }
                if (defaultOids != null) {
                    break;
                }
            }
        } else {
            valueArray = getTableValuesByOids(defaultOids);
        }
        return valueArray;
    }
    /**
     * getCommunity:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getCommunity() {
        return community;
    }

    /**
     * setCommunity:
     * <p>
     *
     * @param   community
     *          -
     * @since   v1.01
     */
    public void setCommunity(String community) {
        this.community = community;
    }

    /**
     * getDefaultOids:
     * <p>
     *
     * @return  String[]
     *          -
     * @since   v1.01
     */
    public String[] getDefaultOids() {
        return defaultOids;
    }

    /**
     * setDefaultOids:
     * <p>
     *
     * @param   defaultOids
     *          -
     * @since   v1.01
     */
    public void setDefaultOids(String[] defaultOids) {
        this.defaultOids = defaultOids;
    }

    /**
     * getNodeSysOid:
     * <p>获取设备的 sysOid
     * 
     * @return  {@link String}
     *          - 设备的 sysOid
     *
     * @since   v1.01
     */
    public String getNodeSysOid() {
        return getIndicatorInfo().getNodeDTO().getSysOid().trim();
    }
}

