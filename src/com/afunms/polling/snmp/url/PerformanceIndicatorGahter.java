/*
 * @(#)PerformanceIndicatorGahter.java     v1.01, Mar 18, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.polling.snmp.url;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.application.urlmonitor.URLDataCollector;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.node.model.URLPerformanceInfo;
import com.afunms.polling.api.IndicatorGather;
import com.afunms.polling.base.Node;
import com.afunms.polling.loader.URLConfigLoader;
import com.afunms.polling.node.Result;
import com.afunms.polling.node.URLConfigNode;
import com.gatherResulttosql.URLResultTosql;

/**
 * ClassName:   PerformanceIndicatorGahter.java
 * <p>性能信息采集类
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        Mar 18, 2013 4:06:19 PM
 */
public class PerformanceIndicatorGahter extends SnmpMonitor implements
        IndicatorGather {

    /**
     * format:
     * <p>时间格式化
     *
     * @since   v1.01
     */
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    /**
     * collect_data:
     * <p>
     *
     * @return
     *
     * @since   v1.01
     */
    public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
        String nodeid = nodeGatherIndicators.getNodeid();
        URLConfigLoader loader = new URLConfigLoader();
        URLConfigNode node = loader.loadOne(nodeid);
        if (node == null || node.isManaged()) {
            return null;
        }
        Result result = getValue(node, nodeGatherIndicators);
        URLPerformanceInfo info = (URLPerformanceInfo) result.getResult();
        
        AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
        try {
            List<AlarmIndicatorsNode> list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(nodeGatherIndicators.getNodeid(), nodeGatherIndicators.getType(), nodeGatherIndicators.getSubtype());
            CheckEventUtil checkEventUtil = new CheckEventUtil();
            for (AlarmIndicatorsNode alarmIndicatorsNode : list) {
                if ("ping".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
                    checkEventUtil.checkEvent(node, alarmIndicatorsNode, String.valueOf(info.getConnectUtilization()));
                } else if ("pageSize".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
                    checkEventUtil.checkEvent(node, alarmIndicatorsNode, String.valueOf(info.getPageSize()));
                } else if ("responseTime".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
                    checkEventUtil.checkEvent(node, alarmIndicatorsNode, String.valueOf(info.getResponseTime()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        URLResultTosql resultTosql = new URLResultTosql();
        Hashtable dataresult = new Hashtable();
        dataresult.put("info", result.getResult());
        resultTosql.CreateResultTosql(dataresult, node);
        return null;
    }

    /**
     * getValue:
     *
     * @param node
     * @param nodeGatherIndicators
     * @return
     *
     * @since   v1.01
     * @see com.afunms.polling.api.IndicatorGather#getValue(com.afunms.polling.base.Node, com.afunms.indicators.model.NodeGatherIndicators)
     */
    public Result getValue(Node node, NodeGatherIndicators nodeGatherIndicators) {
        URLConfigNode configNode = (URLConfigNode) node;
        String url = configNode.getUrl();
        int timeOut = configNode.getTimeout();
        String data = URLDataCollector.getData(url, timeOut);
        int connectUtilization = 0;
        int pageSize = 0;
        long responseTime = 0;
        if (data != null && data.length() > 0) {
            connectUtilization = 100;
            pageSize = data.length() / 1024;
            responseTime = URLDataCollector.getResponseTime(url, timeOut);
        }
        System.out.println(connectUtilization + "====" + responseTime + "=====" + pageSize);
        URLPerformanceInfo info = new URLPerformanceInfo();
        info.setConnectUtilization(connectUtilization);
        info.setResponseTime(responseTime);
        info.setPageSize(pageSize);
        info.setChangeRate(0);
        info.setCollecttime(format.format(new Date()));
        Result result = new Result();
        result.setResult(info);
        return result;
    }

}

