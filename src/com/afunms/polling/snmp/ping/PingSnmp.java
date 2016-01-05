package com.afunms.polling.snmp.ping;

/*
 * @author hukelei@dhcc.com.cn
 *
 */

import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.ping.PingUtil;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.polling.loader.HostLoader;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Pingcollectdata;
import com.gatherResulttosql.HostnetPingResultTosql;
import com.gatherResulttosql.NetHostPingdatatempRtosql;

public class PingSnmp extends SnmpMonitor {

    public PingSnmp() {
    }

    public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
        Hashtable returnhash = new Hashtable();
        HostLoader hostLoader = new HostLoader();
        Host node = hostLoader.loadOneByID(nodeGatherIndicators.getNodeid());
        if (node == null) {
            return null;
        }
        if (!node.isManaged()) {
            return null;
        }

        Calendar calendar = getCalendar();

        int[] result = getResult(node.getIpAddress());
        int connect = result[0];
        int respoonseTime = result[1];
        
        if (connect > 0 && respoonseTime == 0) {
            respoonseTime = new Double(Math.random() * 5).intValue();
        }

        Pingcollectdata pingcollectdata = new Pingcollectdata();
        pingcollectdata.setIpaddress(node.getIpAddress());
        pingcollectdata.setCollecttime(calendar);
        pingcollectdata.setCategory("Ping");
        pingcollectdata.setEntity("Utilization");
        pingcollectdata.setSubentity("ConnectUtilization");
        pingcollectdata.setRestype("dynamic");
        pingcollectdata.setUnit("%");
        pingcollectdata.setThevalue(String.valueOf(connect));

        Pingcollectdata responseTimecollectdata = new Pingcollectdata();
        responseTimecollectdata.setIpaddress(node.getIpAddress());
        responseTimecollectdata.setCollecttime(calendar);
        responseTimecollectdata.setCategory("Ping");
        responseTimecollectdata.setEntity("ResponseTime");
        responseTimecollectdata.setSubentity("ResponseTime");
        responseTimecollectdata.setRestype("dynamic");
        responseTimecollectdata.setUnit("毫秒");
        responseTimecollectdata.setThevalue(String.valueOf(respoonseTime));

        // 对PING值进行告警检测
        CheckEventUtil checkeventutil = new CheckEventUtil();
        AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
        List<AlarmIndicatorsNode> list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(nodeGatherIndicators.getNodeid(), nodeGatherIndicators.getType(), nodeGatherIndicators.getSubtype());
        for (AlarmIndicatorsNode alarmIndicatorsNode : list) {
            if ("ping".equals(alarmIndicatorsNode.getName())) {
                checkeventutil.checkEvent(node, alarmIndicatorsNode, pingcollectdata.getThevalue());
            }
        }

        Vector vector = new Vector();
        vector.add(pingcollectdata);
        vector.add(responseTimecollectdata);
        returnhash.put("ping", vector);

        // 把数据转换成sql
        HostnetPingResultTosql tosql = new HostnetPingResultTosql();
        tosql.CreateResultTosql(returnhash, node.getIpAddress());
        NetHostPingdatatempRtosql totempsql = new NetHostPingdatatempRtosql();
        totempsql.CreateResultTosql(returnhash, node);

        return returnhash;
    }

    public int[] getResult(String ipAddress) {
        return PingUtil.ping(ipAddress);
    }


}
