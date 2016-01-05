package com.afunms.polling.snmp.ftp;

import java.util.Hashtable;
import java.util.List;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.FTPUtil;
import com.afunms.common.util.SnmpUtils;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.initialize.ResourceCenter;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.polling.base.Node;
import com.afunms.polling.loader.FtpLoader;
import com.afunms.polling.node.Ftp;
import com.afunms.polling.node.Result;
import com.afunms.polling.om.FTPcollectdata;

public class Upload extends SnmpMonitor {

    private static final String localPath = ResourceCenter.getInstance().getSysPath() + "WEB-INF/ftpTest/";

    public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
        if (nodeGatherIndicators == null) {
            return null;
        }
        FtpLoader loader = new FtpLoader();
        Ftp ftp = loader.loadOneById(nodeGatherIndicators.getNodeid());
        if (ftp == null) {
            return null;
        }
        if (!ftp.isManaged()) {
            return null;
        }
        NodeUtil nodeUtil = new NodeUtil();
        NodeDTO node = nodeUtil.conversionToNodeDTO(ftp);

        try {
            Result result = getValue(ftp, nodeGatherIndicators);
            int errorCode = result.getErrorCode();
            if (errorCode <= 0) {
                AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                List<AlarmIndicatorsNode> list = alarmIndicatorsUtil.getAlarmIndicatorsForNode(node.getNodeid(), node.getType(), node.getSubtype());
                CheckEventUtil checkEventUtil = new CheckEventUtil();
                for (AlarmIndicatorsNode alarmIndicatorsNode : list) {
                    if ("upload".equals(alarmIndicatorsNode.getName())) {
                        //checkEventUtil.checkFTPEvent(node, alarmIndicatorsNode, result);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        Ftp ftp = (Ftp) node;
        String ip = ftp.getIpAddress();
        int port = ftp.getPort();
        String userName = ftp.getUsername();
        String password = ftp.getPassword();
        String fileName = ftp.getFilename();
        String remotePath = ftp.getRemotePath();
        int errorCode = FTPUtil.upload(ip, port, userName, password, fileName, localPath, remotePath);
        String errorInfo = FTPUtil.getErrorInfo(errorCode);
        
        FTPcollectdata collectdata = new FTPcollectdata();
        collectdata.setCategory(nodeGatherIndicators.getName());
        collectdata.setCollecttime(getCalendar());
        collectdata.setThevalue(String.valueOf(errorCode));
        
        Result result = new Result();
        result.setErrorCode(errorCode);
        result.setErrorInfo(errorInfo);
        result.setCollectTime(collectdata.getCollecttime().getTime());
        result.setResult(collectdata);
        return result;
    }

    public static void main(String[] args) {
    }
}
