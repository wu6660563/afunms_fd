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
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 26 23:46:01
 */
public abstract class BaseIndicatorGather implements IndicatorGather {

    /**
     * SCALE:
     * <p>保留两位小数位
     *
     * @since   v1.01
     */
    public final static int SCALE = 2;

    /**
     * indicatorInfo:
     * <p>指标信息类
     *
     * @since   v1.01
     */
    private IndicatorInfo indicatorInfo;

    /**
     * ipAddress:
     * <p>Ip 地址
     *
     * @since   v1.01
     */
    protected String ipAddress = null;

    /**
     * nodeId:
     * <p>设备 ID
     *
     * @since   v1.01
     */
    protected String nodeId = null;

    /**
     * sdf:
     * <p>格式化日期
     *
     * @since   v1.01
     */
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * RESTYPE_STATIC:
     * <p>静态值
     *
     * @since   v1.01
     */
    public static final String RESTYPE_STATIC = "static";
    
    /**
     * RESTYPE_DYNAMIC:
     * <p>动态值
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

    public static final String CHNAME_IFINDEX = "端口索引";
    
    public static final String CHNAME_IFDESCR = "描述";
    
    public static final String CHNAME_IFTYPE = "类型";
    
    public static final String CHNAME_IFMTU = "最大数据包";
    
    public static final String CHNAME_IFSPEED = "端口最大速率(bit)";
    
    public static final String CHNAME_IFPHYSADDRESS = "端口Mac地址";

    public static final String CHNAME_IFADMINSTATUS = "管理状态";
    
    public static final String CHNAME_IFOPERSTATUS = "当前状态";
    
    public static final String CHNAME_IFLASTCHANGE = "系统sysUpTime评估";

    public static final String CHNAME_IFINOCTETS = "接收的字节";
    
    public static final String CHNAME_IFOUTOCTETS = "传输的字节";

    public static final String CHNAME_IFINMULTICASTPKTS = "入口多播数据包";
    
    public static final String CHNAME_IFOUTMULTICASTPKTS = "出口多播数据包";

    public static final String CHNAME_IFINBROADCASTPKTS = "入口广播数据包";
    
    public static final String CHNAME_IFOUTBROADCASTPKTS = "出口广播数据包";

    public static final String CHNAME_IFINPKTS = "入口数据包";

    public static final String CHNAME_IFOUTPKTS = "出口数据包";

    public static final String CHNAME_IFINPKTSPERS = "每秒入口数据包";

    public static final String CHNAME_IFOUTPKTSPERS = "每秒出口数据包";

    public static final String CHNAME_IFINDISCARDS = "入口丢包数";
    
    public static final String CHNAME_IFOUTDISCARDS = "出口丢包数";

    public static final String CHNAME_IFINERRORS = "入口错误包数";
    
    public static final String CHNAME_IFOUTERRORS = "出口错误包数";

    public static final String CHNAME_IFINBANDWIDTHUTILHDX = "端口入口流速";

    public static final String CHNAME_IFOUTBANDWIDTHUTILHDX = "端口出口流速";

    public static final String CHNAME_IFINBANDWIDTHUTILHDXPERC = "端口入口带宽利用率";

    public static final String CHNAME_IFOUTBANDWIDTHUTILHDXPERC = "端口出口带宽利用率";

    public static final String CHNAME_IFINDISCARDSPERC = "入口丢包率";
    
    public static final String CHNAME_IFOUTDISCARDSPERC = "出口丢包率";

    public static final String CHNAME_IFINERRORSPERC = "入口错包率";
    
    public static final String CHNAME_IFOUTERRORSPERC = "出口错包率";
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
     * <p>实现接口中 获取采集指标值 方法，并重新定义抽象的获取简单采集指标值的方法，由子类进行实现。
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
     * <p>创建简单的采集值
     *
     * @return  {@link SimpleIndicatorValue}
     *          - 返回创建的简单采集值
     *
     * @since   v1.01
     */
    public SimpleIndicatorValue createSimpleIndicatorValue() {
        return createSimpleIndicatorValue(null);
    }

    /**
     * createSimpleIndicatorValue:
     * <p>创建简单的采集值
     *
     * @param   value
     *          - 采集值
     * @return  {@link SimpleIndicatorValue}
     *          - 返回创建的简单采集值
     *
     * @since   v1.01
     */
    public SimpleIndicatorValue createSimpleIndicatorValue(Object value) {
        return createSimpleIndicatorValue(value, 0);
    }

    /**
     * createSimpleIndicatorValue:
     * <p>创建简单的采集值
     *
     * @param   value
     *          - 采集值
     * @param   errorCode
     *          - 错误码
     * @return  {@link SimpleIndicatorValue}
     *          - 返回创建的简单采集值
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
     * <p>获取默认时间（当前时间） {@link Calendar} 的实例
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
     * <p>获取指定时间的 {@link Calendar} 的实例
     *
     * @param   date
     *          - 指定的时间
     * @return  {@link Calendar}
     *          - 指定时间的 {@link Calendar} 的实例
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
     * <p>格式化 double ，保留两位小数
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
     * <p>格式化日期时间
     *
     * @param   date
     *          - 日期时间
     * @return  {@link String}
     *          - 返回 "yyyy-MM-dd HH:mm:ss" 形式的日期格式
     *
     * @since   v1.01
     */
    protected String format(Date date) {
        return sdf.format(date);
    }

}

