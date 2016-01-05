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
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 27 00:16:52
 */
public abstract class SnmpIndicatorGather extends BaseIndicatorGather {

    /**
     * snmp:
     * <p>SNMP ������
     *
     * @since   v1.01
     */
    protected static SnmpService snmp = new SnmpService(); 

    /**
     * defaultOids:
     * <p>Ĭ�ϵ� OIDS
     *
     * @since   v1.01
     */
    protected String[] defaultOids = null;

    /**
     * community:
     * <p>������
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
     * <p>ͨ�� oids ��ȡ �б�ֵ
     *
     * @param   oids
     *          - oids
     * @return  {@link String[][]}
     *          - ֵ
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
     * <p>�� OID �б���ѡ��һ�� OID ����ִ��
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
     * <p>��ȡ�豸�� sysOid
     * 
     * @return  {@link String}
     *          - �豸�� sysOid
     *
     * @since   v1.01
     */
    public String getNodeSysOid() {
        return getIndicatorInfo().getNodeDTO().getSysOid().trim();
    }
}

