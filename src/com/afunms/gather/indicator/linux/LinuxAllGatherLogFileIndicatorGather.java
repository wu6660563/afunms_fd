/*
 * @(#)LinuxAllGatherLogFileIndicatorGather.java     v1.01, 2014 1 14
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.gather.indicator.linux;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.afunms.common.util.SysLogger;
import com.afunms.config.model.Nodeconfig;
import com.afunms.config.model.Nodecpuconfig;
import com.afunms.gather.indicator.base.LogFileIndicatorGather;
import com.afunms.gather.model.SimpleIndicatorValue;
import com.afunms.polling.om.Servicecollectdata;
import com.afunms.polling.om.Systemcollectdata;

/**
 * ClassName:   LinuxAllGatherLogFileIndicatorGather.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 14 15:14:39
 */
public class LinuxAllGatherLogFileIndicatorGather extends
                LogFileIndicatorGather {

    /**
     * logger:
     * <p>日志
     *
     * @since   v1.01
     */
    private final static SysLogger logger = SysLogger.getLogger(LinuxAllGatherLogFileIndicatorGather.class);

    protected Hashtable<String, LogFileIndicatorGather> allIndicatorGatherHashtable = null;
    /**
     * getSimpleIndicatorValue:
     *
     * @return
     *
     * @since   v1.01
     * @see com.afunms.gather.indicator.base.BaseIndicatorGather#getSimpleIndicatorValue()
     */
    @SuppressWarnings({ "unchecked", "static-access" })
    @Override
    public SimpleIndicatorValue getSimpleIndicatorValue() {
        String logFileContent = loadLogFileContent();
        setLogFileContent(logFileContent);
        Hashtable<String, Object> hashtable = new Hashtable<String, Object>();

        if (logFileContent != null && logFileContent.length() > 0) {
            // 文件内容不为空，则进行采集
            Hashtable<String, LogFileIndicatorGather> allIndicatorGatherHashtable = getAllIndicatorGatherHashtable();
            Iterator<String> iterator = allIndicatorGatherHashtable.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                try {
                    LogFileIndicatorGather logFileIndicatorGather = allIndicatorGatherHashtable.get(key);
                    logFileIndicatorGather.setIndicatorInfo(getIndicatorInfo());
                    logFileIndicatorGather.setLogFileContent(logFileContent);
                    SimpleIndicatorValue simpleIndicatorValue = logFileIndicatorGather.getSimpleIndicatorValue();
                    logFileIndicatorGather.setLastSimpleIndicatorValue(simpleIndicatorValue);
                    
                    hashtable.put(key, simpleIndicatorValue.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("Linux 设备 " + getIpAddress() + " 采集 key 为：" + key + " 出错", e);
                }
            }
            Vector<Systemcollectdata> vector = new Vector<Systemcollectdata>();
            Vector<Systemcollectdata> macVector = (Vector<Systemcollectdata>) hashtable.get("mac");
            vector.addAll(macVector);
            vector.addAll((Vector<Systemcollectdata>) hashtable.get("uname"));
            vector.addAll((Vector<Systemcollectdata>) hashtable.get("date"));
            hashtable.put("system", vector);
    
            Hashtable<String, Object> CPUConfigHashtable = (Hashtable<String, Object>) hashtable.get("CPUConfig");
            String procesorsnum = (String) CPUConfigHashtable.get("procesorsnum");
            List<Nodecpuconfig> cpuconfiglist = (List<Nodecpuconfig>) CPUConfigHashtable.get("cpuconfiglist");
            Nodeconfig nodeconfig = new Nodeconfig();
            nodeconfig.setNodeid(Integer.valueOf(getNodeId()));
            nodeconfig.setHostname(getIndicatorInfo().getNodeDTO().getName());
            nodeconfig.setCSDVersion((String) hashtable.get("version"));
            nodeconfig.setNumberOfProcessors(procesorsnum);
    
            for (Systemcollectdata systemcollectdata : macVector) {
                nodeconfig.setMac(systemcollectdata.getThevalue());
            }
            hashtable.put("nodeconfig", nodeconfig);
            hashtable.put("CPUConfig", cpuconfiglist);
            
            List<Hashtable<String, String>> serviceList = (List<Hashtable<String,String>>) hashtable.get("service");
            Vector<Servicecollectdata> serviceVector = new Vector<Servicecollectdata>();
            for (Hashtable<String, String> servicehashtable : serviceList) {
                serviceVector.add(getServicecollectdataByHashtable(servicehashtable));
            }
            hashtable.put("service", serviceVector);
            
            Hashtable<String, Object> cpuHashtable = (Hashtable<String, Object>) hashtable.get("cpu");
            hashtable.put("cpu", cpuHashtable.get("cpuVector"));
            hashtable.put("cpuperflist", cpuHashtable.get("cpuperflist"));
        }
        return createSimpleIndicatorValue(hashtable);
    }

    public Hashtable<String, LogFileIndicatorGather> createAllIndicatorGatherHashtable() {
        Hashtable<String, LogFileIndicatorGather> hashtable = new Hashtable<String, LogFileIndicatorGather>();
        // 采集时间
        hashtable.put("collecttime", new LinuxCollectTimeLogFileIndicatorGather());
        // Version
        hashtable.put("version", new LinuxVersionLogFileIndicatorGather());
        // CPUConfig
        hashtable.put("CPUConfig", new LinuxCPUConfigLogFileIndicatorGather());
        // 磁盘
        hashtable.put("disk", new LinuxDiskLogFileIndicatorGather());
        // 磁盘性能
        hashtable.put("diskperf", new LinuxDiskperfLogFileIndicatorGather());
        // CPU
        hashtable.put("cpu", new LinuxCpuLogFileIndicatorGather());
        // Memory
        hashtable.put("memory", new LinuxMemoryLogFileIndicatorGather());
        // Process
        hashtable.put("process", new LinuxProcessLogFileIndicatorGather());
        // MAC
        hashtable.put("mac", new LinuxMacLogFileIndicatorGather());
        // Interface
        hashtable.put("interface", new LinuxInterfaceLogFileIndicatorGather());
        // UName
        hashtable.put("uname", new LinuxUNameLogFileIndicatorGather());
        // User
        hashtable.put("user", new LinuxUserLogFileIndicatorGather());
        // Date
        hashtable.put("date", new LinuxDateLogFileIndicatorGather());
        // uptime
        hashtable.put("uptime", new LinuxUptimeLogFileIndicatorGather());
        // Service
        hashtable.put("service", new LinuxServiceLogFileIndicatorGather());
        return hashtable;
    }
    
    /**
     * getAllIndicatorGatherHashtable:
     * <p>
     *
     * @return  Hashtable<String,LogFileIndicatorGather>
     *          -
     * @since   v1.01
     */
    public Hashtable<String, LogFileIndicatorGather> getAllIndicatorGatherHashtable() {
        if (allIndicatorGatherHashtable == null) {
            allIndicatorGatherHashtable = createAllIndicatorGatherHashtable();
        }
        return allIndicatorGatherHashtable;
    }


    /**
     * setAllIndicatorGatherHashtable:
     * <p>
     *
     * @param   allIndicatorGatherHashtable
     *          -
     * @since   v1.01
     */
    public void setAllIndicatorGatherHashtable(
                    Hashtable<String, LogFileIndicatorGather> allIndicatorGatherHashtable) {
        this.allIndicatorGatherHashtable = allIndicatorGatherHashtable;
    }

    /**
     * 根据service的 Hashtable 得到 {@link Servicecollectdata} 对象
     * 
     * @param serviceItemHash
     * @return
     */
    public Servicecollectdata getServicecollectdataByHashtable(Hashtable<String, String> serviceItemHash) {
        Servicecollectdata servicecollectdata = new Servicecollectdata();
        Iterator<String> iterator = serviceItemHash.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = serviceItemHash.get(key);
            if (value != null && value.indexOf("\\") != -1) {
                value = value.replaceAll("\\\\", "/");
            }
            if ("DisplayName".equalsIgnoreCase(key)
                    || "name".equalsIgnoreCase(key)) {
                servicecollectdata.setName(value);
            }
            if ("instate".equalsIgnoreCase(key)
                    || "State".equalsIgnoreCase(key)
                    || "status".equalsIgnoreCase(key)) {
                servicecollectdata.setInstate(value);
            }
            if ("opstate".equalsIgnoreCase(key)) {
                servicecollectdata.setOpstate(value);
            }
            if ("uninst".equalsIgnoreCase(key)) {
                servicecollectdata.setUninst(value);
            }
            if ("paused".equalsIgnoreCase(key)) {
                servicecollectdata.setPaused(value);
            }
            if ("StartMode".equalsIgnoreCase(key)) {
                servicecollectdata.setStartMode(value);
            }
            if ("PathName".equalsIgnoreCase(key)) {
                servicecollectdata.setPathName(value);
            }
            if ("Description".equalsIgnoreCase(key)) {
                servicecollectdata.setDescription(value);
            }
            if ("ServiceType".equalsIgnoreCase(key)) {
                servicecollectdata.setServiceType(value);
            }
            if ("pid".equalsIgnoreCase(key)) {
                servicecollectdata.setPid(value);
            }
            if ("groupstr".equalsIgnoreCase(key)
                    || "group".equalsIgnoreCase(key)) {
                servicecollectdata.setGroupstr(value);
            }
        }
        return servicecollectdata;
    }
}

