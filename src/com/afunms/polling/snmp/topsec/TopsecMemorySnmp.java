/*
 * @(#)TopsecMemorySnmp.java     v1.01, Jan 15, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.polling.snmp.topsec;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.SnmpUtils;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.polling.api.IndicatorGather;
import com.afunms.polling.base.Node;
import com.afunms.polling.loader.HostLoader;
import com.afunms.polling.node.Host;
import com.afunms.polling.node.Result;
import com.afunms.polling.om.Memorycollectdata;
import com.gatherResulttosql.NetHostMemoryRtsql;
import com.gatherResulttosql.NetmemoryResultTosql;

/**
 * ClassName:   TopsecMemorySnmp.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        Jan 15, 2013 5:33:48 PM
 */
public class TopsecMemorySnmp extends SnmpMonitor implements IndicatorGather {

    private static final DecimalFormat df = new DecimalFormat("#.##");

    public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
        HostLoader hostLoader = new HostLoader();
        Host node = hostLoader.loadOneByID(nodeGatherIndicators.getNodeid());
        if (node == null) {
            return null;
        }
        if (!node.isManaged()) {
            return null;
        }
        Result result = getValue(node, nodeGatherIndicators);
        
        Hashtable<String, Vector<Memorycollectdata>> collectHash = (Hashtable<String, Vector<Memorycollectdata>>) result.getResult();
        String value = "0";
        Vector<Memorycollectdata> memeoryVector = collectHash.get("memory");
        if (memeoryVector != null && memeoryVector.size() > 0) {
            Memorycollectdata data = memeoryVector.get(0);
            value = data.getThevalue();
        }
        CheckEventUtil checkutil = new CheckEventUtil();
        AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
        List<AlarmIndicatorsNode> list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String
                        .valueOf(node.getId()),
                        nodeGatherIndicators.getType(), nodeGatherIndicators.getSubtype());
        for (AlarmIndicatorsNode alarmIndicatorsNode : list) {
            if ("memory".equals(alarmIndicatorsNode.getName())) {
                checkutil.checkEvent(node, alarmIndicatorsNode, value);
            }
        }
        
        NetmemoryResultTosql tosql=new NetmemoryResultTosql();
        tosql.CreateResultTosql(collectHash, node.getIpAddress());
        NetHostMemoryRtsql  totempsql=new NetHostMemoryRtsql();
        totempsql.CreateResultTosql(collectHash, node);
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
        Calendar date = getCalendar();
        Hashtable<String, Vector<Memorycollectdata>> collectHash = new Hashtable<String, Vector<Memorycollectdata>>();
        Vector<Memorycollectdata> memoryVector = new Vector<Memorycollectdata>();
        int errorCode = 0;
        String errorInfo = "";
        try {
            Host host = (Host) node;
            String[] oids = new String[] { "1.3.6.1.4.1.14331.5.5.1.4.6" };
            String[][] valueArray = SnmpUtils.getTemperatureTableData(node
                    .getIpAddress(), host.getCommunity(), oids, host
                    .getSnmpversion(), 3, 1000 * 30);
            double valueDouble = 0;
            int flag = 0;
            if (valueArray != null) {
                for (int i = 0; i < valueArray.length; i++) {
                    String _value = valueArray[i][0];
                    String index = valueArray[i][1];

                    try {
                        if (_value != null) {
                            valueDouble += Double.valueOf(_value);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    flag += 1;
                }
            }

            String valueStr = "0";
            if (flag > 0) {
                valueStr = String.valueOf(df.parse(String.valueOf(valueDouble/flag)));
            }

            
            
            Memorycollectdata data = new Memorycollectdata();
            data.setIpaddress(node.getIpAddress());
            data.setCollecttime(date);
            data.setCategory("Memory");
            data.setEntity("Utilization");
            data.setSubentity("Avg");
            data.setRestype("dynamic");
            data.setUnit("%");
            data.setThevalue(valueStr);
            memoryVector.addElement(data);

            collectHash.put("memory", memoryVector);
        } catch (ParseException e) {
            e.printStackTrace();
            errorCode = -1;
            errorInfo = "数据解析错误";
        } catch (Exception e) {
            e.printStackTrace();
            errorCode = -2;
            errorInfo = "未知错误";
        }
        Result result = new Result();
        result.setCollectTime(date.getTime());
        result.setErrorCode(errorCode);
        result.setErrorInfo(errorInfo);
        result.setResult(collectHash);
        return result;
    }

}

