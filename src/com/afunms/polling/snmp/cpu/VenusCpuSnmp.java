/*
 * @(#)VenusCpuSnmp.java     v1.01, Mar 25, 2013
 * 
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.polling.snmp.cpu;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.polling.api.IndicatorGather;
import com.afunms.polling.base.Node;
import com.afunms.polling.loader.HostLoader;
import com.afunms.polling.node.Host;
import com.afunms.polling.node.Result;
import com.afunms.polling.om.CPUcollectdata;
import com.gatherResulttosql.NetHostDatatempCpuRTosql;
import com.gatherResulttosql.NetcpuResultTosql;

/**
 * 
 * ClassName: VenusCpuSnmp.java
 * <p>
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Mar 25, 2013 8:38:20 PM
 */

public class VenusCpuSnmp extends SnmpMonitor implements IndicatorGather {

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

		Hashtable<String, Vector<CPUcollectdata>> collectHash = (Hashtable<String, Vector<CPUcollectdata>>) result
				.getResult();
		String value = "0";
		Vector<CPUcollectdata> cpuVector = collectHash.get("cpu");
		if (cpuVector != null && cpuVector.size() > 0) {
			CPUcollectdata cpudata = cpuVector.get(0);
			value = cpudata.getThevalue();
		}
		CheckEventUtil checkutil = new CheckEventUtil();
		AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
		List<AlarmIndicatorsNode> list = alarmIndicatorsUtil
				.getAlarmInicatorsThresholdForNode(
						String.valueOf(node.getId()), nodeGatherIndicators
								.getType(), nodeGatherIndicators.getSubtype());
		for (AlarmIndicatorsNode alarmIndicatorsNode : list) {
			if ("cpu".equals(alarmIndicatorsNode.getName())) {
				checkutil.checkEvent(node, alarmIndicatorsNode, value);
			}
		}

		NetcpuResultTosql restosql = new NetcpuResultTosql();
		restosql.CreateResultTosql(collectHash, node.getIpAddress());

		// 把结果转换成sql
		NetHostDatatempCpuRTosql totempsql = new NetHostDatatempCpuRTosql();
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
	 * @since v1.01
	 * @see com.afunms.polling.api.IndicatorGather#getValue(com.afunms.polling.base.Node,
	 *      com.afunms.indicators.model.NodeGatherIndicators)
	 */
	public Result getValue(Node node, NodeGatherIndicators nodeGatherIndicators) {
		Calendar date = getCalendar();
		Hashtable<String, Vector<CPUcollectdata>> collectHash = new Hashtable<String, Vector<CPUcollectdata>>();
		Vector<CPUcollectdata> cpuVector = new Vector<CPUcollectdata>();
		int errorCode = 0;
		String errorInfo = "";
		try {
			Host host = (Host) node;
			String[] oids = new String[] { "1.3.6.1.4.1.15227.1.3.1.1.1" }; // 此ID为启明星辰天清汉马USG防火墙设备
			String[][] valueArray = snmp.getCpuTableData(host.getIpAddress(),
					host.getCommunity(), oids);
			double valueDouble = 0;
			int flag = 0;
			if (valueArray != null) {
				for (int i = 0; i < valueArray.length; i++) {
					String _value = valueArray[i][0];
					String index = valueArray[i][1];

					try {
						if (_value != null) {
							_value=_value.replaceAll("%", "");
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
				valueStr = String.valueOf(df.parse(String.valueOf(valueDouble
						/ flag)));
			}

			CPUcollectdata cpudata = new CPUcollectdata();
			cpudata.setIpaddress(node.getIpAddress());
			cpudata.setCollecttime(date);
			cpudata.setCategory("CPU");
			cpudata.setEntity("Utilization");
			cpudata.setSubentity("Utilization");
			cpudata.setRestype("dynamic");
			cpudata.setUnit("%");
			cpudata.setThevalue(valueStr);
			cpuVector.addElement(cpudata);

			collectHash.put("cpu", cpuVector);
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
