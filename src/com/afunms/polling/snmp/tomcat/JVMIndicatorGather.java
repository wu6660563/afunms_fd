/*
 * @(#)JVMIndicatorGather.java     v1.01, Jan 7, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.polling.snmp.tomcat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.application.dao.TomcatDao;
import com.afunms.application.tomcatmonitor.ServerStream;
import com.afunms.application.tomcatmonitor.TomcatInfo;
import com.afunms.application.tomcatmonitor.TomcatServerConnector;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.ShareData;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.polling.api.IndicatorGather;
import com.afunms.polling.base.Node;
import com.afunms.polling.loader.HostLoader;
import com.afunms.polling.loader.TomcatLoader;
import com.afunms.polling.node.Host;
import com.afunms.polling.node.Result;
import com.afunms.polling.node.Tomcat;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.temp.dao.TomcatTempDao;
import com.afunms.temp.model.NodeTemp;
import com.afunms.temp.model.TomcatNodeTemp;
import com.gatherResulttosql.TomcatResulttosql;

/**
 * ClassName:   JVMIndicatorGather.java
 * <p>{@link JVMIndicatorGather} Tomcat 的 JVM 指标采集类
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        Jan 7, 2013 9:51:50 AM
 */
public class JVMIndicatorGather extends SnmpMonitor implements IndicatorGather {

    private static final DecimalFormat df = new DecimalFormat("#.##");

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
        TomcatLoader hostLoader = new TomcatLoader();
        Tomcat node = hostLoader.loadOneByID(nodeGatherIndicators.getNodeid());
        if (node == null) {
            return null;
        }
        if (!node.isManaged()) {
            return null;
        }
        NodeUtil nodeUtil = new NodeUtil();
        NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(node);
        Result result = getValue(node, nodeGatherIndicators);
        Hashtable hashtable = (Hashtable) result.getResult();
        TomcatNodeTemp tomcatNodeTemp = (TomcatNodeTemp) hashtable.get("nodeTemp");
        
        String ping = tomcatNodeTemp.getPing();
        String jvm = tomcatNodeTemp.getMemoryUtilization();

        AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
        try {
            List<AlarmIndicatorsNode> list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(nodeGatherIndicators.getNodeid(), nodeGatherIndicators.getType(), nodeGatherIndicators.getSubtype());
            CheckEventUtil checkEventUtil = new CheckEventUtil();
            for (AlarmIndicatorsNode alarmIndicatorsNode : list) {
                if ("ping".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
                    checkEventUtil.checkEvent(nodeDTO, alarmIndicatorsNode, ping);
                } else if ("jvm".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
                    checkEventUtil.checkEvent(nodeDTO, alarmIndicatorsNode, jvm);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TomcatResulttosql resulttosql = new TomcatResulttosql();
        resulttosql.CreateResultTosql(hashtable, node);
        
        
        return null;
    }

    /**
     * getValue:
     * <p>获取结果
     *
     * @param   node
     *          - 设备
     * @param   nodeGatherIndicators
     *          - 采集指标
     * @return  {@link Result}
     *          - 返回采集结果
     *
     * @since   v1.01
     * @see com.afunms.polling.api.IndicatorGather#getValue(com.afunms.polling.base.Node, com.afunms.indicators.model.NodeGatherIndicators)
     */
    public Result getValue(Node node, NodeGatherIndicators nodeGatherIndicators) {
        Result result = new Result();
        Hashtable<String, Object> hashtable = new Hashtable<String, Object>();
        try {
            Tomcat tomcat = (Tomcat) node;
            String ip = tomcat.getIpAddress();
            int port = Integer.valueOf(tomcat.getPort());
            String user = tomcat.getUser();
            String password = tomcat.getPassword();
            String nodeid = String.valueOf(tomcat.getId());
            TomcatServerConnector connector = new TomcatServerConnector();
            int errorCode = 0;
            String errorInfo = "未采集数据";
            String ping = "0";
            TomcatInfo info = null;
            if (connector.connect(ip, port, user, password)) {
                String content = connector.getContent();
                info = connector.parseContent(content);
                Double freeMemory = Double.valueOf(info.getFreeMemory());
                Double totalMemory = Double.valueOf(info.getTotalMemory());
                Double memoryUtilization = (totalMemory - freeMemory) * 100 / totalMemory;
                info.setMemoryUtilization(df.format(memoryUtilization));
                errorCode = 1;
                ping = "100";
            } else {
                errorCode = -1;
                errorInfo = "未连接成功";
                ping = "0";
            }
            Calendar calendar = getCalendar();
            Pingcollectdata pingcollectdata = new Pingcollectdata();
            pingcollectdata.setIpaddress(node.getIpAddress());
            pingcollectdata.setCollecttime(calendar);
            pingcollectdata.setCategory("Ping");
            pingcollectdata.setEntity("Utilization");
            pingcollectdata.setSubentity("ConnectUtilization");
            pingcollectdata.setThevalue(ping);
            pingcollectdata.setRestype("dynamic");
            pingcollectdata.setUnit("%");
            Vector<Pingcollectdata> vector = new Vector<Pingcollectdata>();
            vector.add(pingcollectdata);
            hashtable.put("ping", vector);

            TomcatNodeTemp nodeTemp = convertTomcatInfoToTomcatTemp(nodeid, info);
            nodeTemp.setPing(ping);
            nodeTemp.setCollectTime(sdf.format(calendar.getTime()));
            hashtable.put("nodeTemp", nodeTemp);

            result.setCollectTime(calendar.getTime());
            result.setErrorCode(errorCode);
            result.setErrorInfo(errorInfo);
            result.setResult(hashtable);
        } catch (Exception e) {
            e.printStackTrace();
            result.setErrorCode(-1);
        }
        return result;
    }

    private TomcatNodeTemp convertTomcatInfoToTomcatTemp(String nodeid, TomcatInfo info) {
        if (info == null) {
            info = new TomcatInfo();
            info.setPing("0");
            info.setTomcatVersion("");
            info.setJVMVersion("");
            info.setJVMVendor("");
            info.setOSName("");
            info.setOSVersion("");
            info.setOSArchitecture("");
            info.setFreeMemory("0");
            info.setTotalMemory("0");
            info.setMaxMemory("0");
            info.setMemoryUtilization("0");
        }
        TomcatNodeTemp nodeTemp = new TomcatNodeTemp();
        nodeTemp.setNodeid(nodeid);
        nodeTemp.setPing(info.getPing());
        nodeTemp.setTomcatVersion(info.getTomcatVersion());
        nodeTemp.setJVMVersion(info.getJVMVersion());
        nodeTemp.setJVMVendor(info.getJVMVendor());
        nodeTemp.setOSName(info.getOSName());
        nodeTemp.setOSVersion(info.getOSVersion());
        nodeTemp.setOSArchitecture(info.getOSArchitecture());
        nodeTemp.setFreeMemory(info.getFreeMemory());
        nodeTemp.setTotalMemory(info.getTotalMemory());
        nodeTemp.setMaxMemory(info.getMaxMemory());
        nodeTemp.setMemoryUtilization(info.getMemoryUtilization());
        return nodeTemp;
    }
    /**
     * main:
     * <p>
     *
     * @param args
     *
     * @since   v1.01
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}

