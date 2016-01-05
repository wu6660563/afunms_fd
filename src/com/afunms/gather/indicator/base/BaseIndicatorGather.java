/*
 * @(#)BaseIndicatorGather.java     v1.01, 2013 12 26
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.base;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.afunms.gather.indicator.IndicatorGather;
import com.afunms.gather.model.IndicatorInfo;
import com.afunms.gather.model.IndicatorValue;
import com.afunms.gather.model.SimpleIndicatorValue;

/**
 * ClassName:   BaseIndicatorGather.java
 * <p>
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 26 23:46:01
 */
public abstract class BaseIndicatorGather implements IndicatorGather {

    /**
     * SCALE:
     * <p>������λС��λ
     *
     * @since   v1.01
     */
    public final static int SCALE = 2;

    /**
     * indicatorInfo:
     * <p>ָ����Ϣ��
     *
     * @since   v1.01
     */
    private IndicatorInfo indicatorInfo;

    /**
     * ipAddress:
     * <p>Ip ��ַ
     *
     * @since   v1.01
     */
    protected String ipAddress = null;

    /**
     * nodeId:
     * <p>�豸 ID
     *
     * @since   v1.01
     */
    protected String nodeId = null;

    /**
     * sdf:
     * <p>��ʽ������
     *
     * @since   v1.01
     */
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * RESTYPE_STATIC:
     * <p>��ֵ̬
     *
     * @since   v1.01
     */
    public static final String RESTYPE_STATIC = "static";
    
    /**
     * RESTYPE_DYNAMIC:
     * <p>��ֵ̬
     *
     * @since   v1.01
     */
    public static final String RESTYPE_DYNAMIC = "dynamic";

    public static final String DESC_IFINDEX = "index";
    
    public static final String DESC_IFDESCR = "ifDescr";
    
    public static final String DESC_IFTYPE = "ifType";
    
    public static final String DESC_IFMTU = "ifMtu";
    
    public static final String DESC_IFSPEED = "ifSpeed";
    
    public static final String DESC_IFPHYSADDRESS = "ifPhysAddress";

    public static final String DESC_IFADMINSTATUS = "ifAdminStatus";
    
    public static final String DESC_IFOPERSTATUS = "ifOperStatus";
    
    public static final String DESC_IFLASTCHANGE = "ifLastChange";

    public static final String DESC_IFINOCTETS = "ifInOctets";

    public static final String DESC_IFOUTOCTETS = "ifOutOctets";

    public static final String DESC_IFINMULTICASTPKTS = "ifInMulticastPkts";

    public static final String DESC_IFOUTMULTICASTPKTS = "ifOutMulticastPkts";

    public static final String DESC_IFINBROADCASTPKTS = "ifInBroadcastPkts";

    public static final String DESC_IFOUTBROADCASTPKTS = "ifOutBroadcastPkts";

    public static final String DESC_IFINPKTS = "ifInPkts";

    public static final String DESC_IFOUTPKTS = "ifOutPkts";

    public static final String DESC_IFINPKTSPERS = "ifInPktsPers";

    public static final String DESC_IFOUTPKTSPERS = "ifOutPktsPers";

    public static final String DESC_IFINDISCARDS = "ifInDiscards";

    public static final String DESC_IFOUTDISCARDS = "ifOutDiscards";

    public static final String DESC_IFINERRORS = "ifInErrors";

    public static final String DESC_IFOUTERRORS = "ifOutErrors";
    
    public static final String DESC_IFINBANDWIDTHUTILHDX = "InBandwidthUtilHdx";

    public static final String DESC_IFOUTBANDWIDTHUTILHDX = "OutBandwidthUtilHdx";

    public static final String DESC_IFINBANDWIDTHUTILHDXPERC = "InBandwidthUtilHdxPerc";

    public static final String DESC_IFOUTBANDWIDTHUTILHDXPERC = "OutBandwidthUtilHdxPerc";

    public static final String DESC_IFINDISCARDSPERC = "InDiscardsPerc";

    public static final String DESC_IFOUTDISCARDSPERC = "OutDiscardsPerc";

    public static final String DESC_IFINERRORSPERC = "InErrorsPerc";

    public static final String DESC_IFOUTERRORSPERC = "OutErrorsPerc";

    public static final String CHNAME_IFINDEX = "�˿�����";
    
    public static final String CHNAME_IFDESCR = "����";
    
    public static final String CHNAME_IFTYPE = "����";
    
    public static final String CHNAME_IFMTU = "������ݰ�";
    
    public static final String CHNAME_IFSPEED = "�˿��������(bit)";
    
    public static final String CHNAME_IFPHYSADDRESS = "�˿�Mac��ַ";

    public static final String CHNAME_IFADMINSTATUS = "����״̬";
    
    public static final String CHNAME_IFOPERSTATUS = "��ǰ״̬";
    
    public static final String CHNAME_IFLASTCHANGE = "ϵͳsysUpTime����";

    public static final String CHNAME_IFINOCTETS = "���յ��ֽ�";
    
    public static final String CHNAME_IFOUTOCTETS = "������ֽ�";

    public static final String CHNAME_IFINMULTICASTPKTS = "��ڶಥ���ݰ�";
    
    public static final String CHNAME_IFOUTMULTICASTPKTS = "���ڶಥ���ݰ�";

    public static final String CHNAME_IFINBROADCASTPKTS = "��ڹ㲥���ݰ�";
    
    public static final String CHNAME_IFOUTBROADCASTPKTS = "���ڹ㲥���ݰ�";

    public static final String CHNAME_IFINPKTS = "������ݰ�";

    public static final String CHNAME_IFOUTPKTS = "�������ݰ�";

    public static final String CHNAME_IFINPKTSPERS = "ÿ��������ݰ�";

    public static final String CHNAME_IFOUTPKTSPERS = "ÿ��������ݰ�";

    public static final String CHNAME_IFINDISCARDS = "��ڶ�����";
    
    public static final String CHNAME_IFOUTDISCARDS = "���ڶ�����";

    public static final String CHNAME_IFINERRORS = "��ڴ������";
    
    public static final String CHNAME_IFOUTERRORS = "���ڴ������";

    public static final String CHNAME_IFINBANDWIDTHUTILHDX = "�˿��������";

    public static final String CHNAME_IFOUTBANDWIDTHUTILHDX = "�˿ڳ�������";

    public static final String CHNAME_IFINBANDWIDTHUTILHDXPERC = "�˿���ڴ���������";

    public static final String CHNAME_IFOUTBANDWIDTHUTILHDXPERC = "�˿ڳ��ڴ���������";

    public static final String CHNAME_IFINDISCARDSPERC = "��ڶ�����";
    
    public static final String CHNAME_IFOUTDISCARDSPERC = "���ڶ�����";

    public static final String CHNAME_IFINERRORSPERC = "��ڴ����";
    
    public static final String CHNAME_IFOUTERRORSPERC = "���ڴ����";
    /**
     * indicatorValue:
     * <p>
     *
     * @since   v1.01
     */
    private IndicatorValue lastIndicatorValue;

    /**
     * simpleIndicatorValue:
     * <p>
     *
     * @since   v1.01
     */
    private SimpleIndicatorValue lastSimpleIndicatorValue;

    /**
     * getIndicatorValue:
     * <p>ʵ�ֽӿ��� ��ȡ�ɼ�ָ��ֵ �����������¶������Ļ�ȡ�򵥲ɼ�ָ��ֵ�ķ��������������ʵ�֡�
     *
     * @return
     *
     * @since   v1.01
     * @see com.afunms.gather.indicator.IndicatorGather#getIndicatorValue()
     */
    public final IndicatorValue getIndicatorValue() {
        Object value = null;
        int errorCode = 0;
        SimpleIndicatorValue simpleIndicatorValue = null;
        try {
            beforeGetSimpleIndicatorValue();
            simpleIndicatorValue = getSimpleIndicatorValue();
            afterGetSimpleIndicatorValue();
            errorCode = simpleIndicatorValue.getErrorCode();
            value = simpleIndicatorValue.getValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        IndicatorValue indicatorValue = new IndicatorValue();
        indicatorValue.setValue(value);
        indicatorValue.setErrorCode(errorCode);
        indicatorValue.setTime(new Date().getTime());
        indicatorValue.setIndicatorInfo(getIndicatorInfo());
        setLastIndicatorValue(indicatorValue);
        setLastSimpleIndicatorValue(simpleIndicatorValue);
        return indicatorValue;
    }

    public abstract void beforeGetSimpleIndicatorValue();

    public abstract SimpleIndicatorValue getSimpleIndicatorValue();

    public abstract void afterGetSimpleIndicatorValue();

    /**
     * createSimpleIndicatorValue:
     * <p>�����򵥵Ĳɼ�ֵ
     *
     * @return  {@link SimpleIndicatorValue}
     *          - ���ش����ļ򵥲ɼ�ֵ
     *
     * @since   v1.01
     */
    public SimpleIndicatorValue createSimpleIndicatorValue() {
        return createSimpleIndicatorValue(null);
    }

    /**
     * createSimpleIndicatorValue:
     * <p>�����򵥵Ĳɼ�ֵ
     *
     * @param   value
     *          - �ɼ�ֵ
     * @return  {@link SimpleIndicatorValue}
     *          - ���ش����ļ򵥲ɼ�ֵ
     *
     * @since   v1.01
     */
    public SimpleIndicatorValue createSimpleIndicatorValue(Object value) {
        return createSimpleIndicatorValue(value, 0);
    }

    /**
     * createSimpleIndicatorValue:
     * <p>�����򵥵Ĳɼ�ֵ
     *
     * @param   value
     *          - �ɼ�ֵ
     * @param   errorCode
     *          - ������
     * @return  {@link SimpleIndicatorValue}
     *          - ���ش����ļ򵥲ɼ�ֵ
     *
     * @since   v1.01
     */
    public SimpleIndicatorValue createSimpleIndicatorValue(Object value, int errorCode) {
        return new SimpleIndicatorValue(value, errorCode);
    }

    /**
     * getIndicatorInfo:
     * <p>
     *
     * @return  IndicatorInfo
     *          -
     * @since   v1.01
     */
    public IndicatorInfo getIndicatorInfo() {
        return indicatorInfo;
    }

    /**
     * setIndicatorInfo:
     * <p>
     *
     * @param   indicatorInfo
     *          -
     * @since   v1.01
     */
    public void setIndicatorInfo(IndicatorInfo indicatorInfo) {
        this.indicatorInfo = indicatorInfo;
    }

    /**
     * getLastIndicatorValue:
     * <p>
     *
     * @return  IndicatorValue
     *          -
     * @since   v1.01
     */
    public IndicatorValue getLastIndicatorValue() {
        return lastIndicatorValue;
    }

    /**
     * setLastIndicatorValue:
     * <p>
     *
     * @param   lastIndicatorValue
     *          -
     * @since   v1.01
     */
    public void setLastIndicatorValue(IndicatorValue lastIndicatorValue) {
        this.lastIndicatorValue = lastIndicatorValue;
    }

    /**
     * getLastSimpleIndicatorValue:
     * <p>
     *
     * @return  SimpleIndicatorValue
     *          -
     * @since   v1.01
     */
    public SimpleIndicatorValue getLastSimpleIndicatorValue() {
        return lastSimpleIndicatorValue;
    }

    /**
     * setLastSimpleIndicatorValue:
     * <p>
     *
     * @param   lastSimpleIndicatorValue
     *          -
     * @since   v1.01
     */
    public void setLastSimpleIndicatorValue(
                    SimpleIndicatorValue lastSimpleIndicatorValue) {
        this.lastSimpleIndicatorValue = lastSimpleIndicatorValue;
    }

    /**
     * getIpAddress:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getIpAddress() {
        if (ipAddress == null) {
            setIpAddress(getIndicatorInfo().getNodeDTO().getIpaddress());
        }
        return ipAddress;
    }

    /**
     * setIpAddress:
     * <p>
     *
     * @param   ipAddress
     *          -
     * @since   v1.01
     */
    private void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    
    /**
     * getNodeId:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getNodeId() {
        if (nodeId == null) {
            setNodeId(getIndicatorInfo().getNodeDTO().getNodeid());
        }
        return nodeId;
    }

    /**
     * setNodeId:
     * <p>
     *
     * @param   nodeId
     *          -
     * @since   v1.01
     */
    private void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    /**
     * getCalendar:
     * <p>��ȡĬ��ʱ�䣨��ǰʱ�䣩 {@link Calendar} ��ʵ��
     *
     * @return
     *
     * @since   v1.01
     */
    protected Calendar getCalendar() {
        return getCalendar(new Date());
    }

    /**
     * getCalendar:
     * <p>��ȡָ��ʱ��� {@link Calendar} ��ʵ��
     *
     * @param   date
     *          - ָ����ʱ��
     * @return  {@link Calendar}
     *          - ָ��ʱ��� {@link Calendar} ��ʵ��
     *
     * @since   v1.01
     */
    protected Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * format:
     * <p>��ʽ�� double ��������λС��
     *
     * @param d
     * @return
     *
     * @since   v1.01
     */
    protected double format(Double d) {
        return format(d, SCALE);
    }

    /**
     * format:
     * <p>
     *
     * @param d
     * @param scale
     * @return
     *
     * @since   v1.01
     */
    protected double format(Double d, int scale) {
        BigDecimal b = new BigDecimal(d);  
        double d1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  
        return d1;
    }

    /**
     * format:
     * <p>��ʽ������ʱ��
     *
     * @param   date
     *          - ����ʱ��
     * @return  {@link String}
     *          - ���� "yyyy-MM-dd HH:mm:ss" ��ʽ�����ڸ�ʽ
     *
     * @since   v1.01
     */
    protected String format(Date date) {
        return sdf.format(date);
    }

}

