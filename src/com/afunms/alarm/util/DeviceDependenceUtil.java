/*
 * @(#)DeviceDependenceUtil.java     v1.01, Jul 18, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.alarm.util;

import java.util.List;
import java.util.Vector;

import com.afunms.alarm.model.AlarmDeviceDependence;
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.dao.LinkDao;
import com.afunms.topology.model.HostNode;
import com.afunms.topology.model.Link;
import com.database.DBManager;

/**
 * 
 * ClassName: DeviceDependenceUtil.java
 * <p>
 * �����������ͼ���趨ĳ���豸Ϊ���㣬���µݹ飬�õ��豸֮���������ϵ
 * ������ϵ����nms_alarm_device_dependence�����Ҫ�Լ��趨��׼�豸
 * 
 * ����ͼ������ϣ�����֮�󣬸ù���������ʹ��
 * �����������ܣ�����ǰ����Ҫ���޸�alarm/way/list.jsp(257 line)����<a>�����豸</a>��display�����Ըĳ�inline��Ȼ����ҳ���ϵĹ����豸��������������ͼ�豸������ϵ
 * ��initNodeIdΪ��׼�豸ID���޸�֮��������������Ҫ��WebRoot�����task/relatedNode.properties�޸�
 * 
 * @author ��Ʒ��
 * @version v1.01
 * @since v1.01
 * @Date Jul 18, 2013 3:51:17 PM
 */

public class DeviceDependenceUtil {

	/**
	 * ��־
	 */
	private static SysLogger logger = SysLogger
			.getLogger(DeviceDependenceUtil.class.getName());

	private Vector<AlarmDeviceDependence> dependenceVector = new Vector<AlarmDeviceDependence>();

	private String initNodeId;
	
	private int num = 0;

	public DeviceDependenceUtil(String initNodeId) {
		this.initNodeId = initNodeId;
	}

	public boolean excute(int nodeLevel) {
		boolean flag = false;
		try {

			AlarmDeviceDependence alarmDeviceDependence = new AlarmDeviceDependence();
			HostNodeDao hostNodeDao = new HostNodeDao();
			alarmDeviceDependence.setFid(0);
			NodeUtil nodeUtil = new NodeUtil();
			HostNode hostNode = null;
			try {
				hostNode = (HostNode) hostNodeDao.findByID(String
						.valueOf(initNodeId));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				hostNodeDao.close();
			}
			NodeDTO nodeDTO = nodeUtil.creatNodeDTOByNode(hostNode);

			alarmDeviceDependence.setType(nodeDTO.getType());
			alarmDeviceDependence.setSubtype(nodeDTO.getSubtype());
			alarmDeviceDependence.setNodeid(Integer.parseInt(initNodeId));
			alarmDeviceDependence.setLinkPort("");
			alarmDeviceDependence.setIpAddress(nodeDTO.getIpaddress());
			alarmDeviceDependence.setDeviceName(nodeDTO.getName());
			alarmDeviceDependence.setNodeLevel(0);
			dependenceVector.add(alarmDeviceDependence);

			dependence(initNodeId, nodeLevel);
			if (saveDependenceList()) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * Dependence:���ݻ����豸�����µݹ飬�����豸֮���������ϵ
	 * <p>
	 * 
	 * @param nodeId
	 * 
	 * @since v1.01
	 */
	public void dependence(String nodeId, int nodeLevel) {
		System.out.println(num);
		num++;
		if (nodeId == null || "".equals(nodeId)) {
			logger.info("δ���û�׼�豸���޷������豸֮���������ϵ��");
			return;
		}
		LinkDao linkDao = new LinkDao();
		List<Link> linkList = null;
		try {
			linkList = linkDao.findByNodeId(nodeId);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			linkDao.close();
		}
		AlarmDeviceDependence alarmDeviceDependence = null;

		if (linkList == null || linkList.size() == 0) {
			return;
		}

		for (int i = 0; i < linkList.size(); i++) {
			Link link = linkList.get(i);
			String portIndex = null;
			String childId = null;
			if (link.getStartId() == Integer.parseInt(nodeId)) {
				// ���nodeId����startId����ô�����豸id����endId
				portIndex = link.getStartIndex();
				childId = String.valueOf(link.getEndId());
			} else if (link.getEndId() == Integer.parseInt(nodeId)) {
				// ���nodeId����endId����ô�����豸����startId
				portIndex = link.getEndIndex();
				childId = String.valueOf(link.getStartId());
			}
			if (isExistNodeId(childId)) {
				continue;
			}
			// �ж��Ƿ��Ѿ����ڣ��������Vector�У�����Ҫ�ӽ�ȥ�����Ҳ���Ҫ�ٻص�
			alarmDeviceDependence = setAlarmDeviceDependence(Integer
					.valueOf(nodeId), childId, portIndex, nodeLevel);
			dependenceVector.add(alarmDeviceDependence);
			dependence(childId, nodeLevel + 1);
		}

	}

	/**
	 * setAlarmDeviceDependence:
	 * <p>
	 * 
	 * @param link
	 * @param nodeId
	 * @param node_id
	 * @param portIndex
	 * @param ipAddress
	 * @return
	 * 
	 * @since v1.01
	 */
	public AlarmDeviceDependence setAlarmDeviceDependence(int fid,
			String nodeId, String portIndex, int nodeLevel) {
		HostNodeDao hostNodeDao = new HostNodeDao();
		AlarmDeviceDependence alarmDeviceDependence = new AlarmDeviceDependence();
		alarmDeviceDependence.setFid(fid);

		NodeUtil nodeUtil = new NodeUtil();
		HostNode hostNode = null;
		try {
			hostNode = (HostNode) hostNodeDao.findByID(String.valueOf(nodeId));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			hostNodeDao.close();
		}
		NodeDTO nodeDTO = nodeUtil.creatNodeDTOByNode(hostNode);

		alarmDeviceDependence.setType(nodeDTO.getType());
		alarmDeviceDependence.setSubtype(nodeDTO.getSubtype());
		alarmDeviceDependence.setNodeid(Integer.parseInt(nodeId));
		alarmDeviceDependence.setLinkPort(portIndex);
		alarmDeviceDependence.setIpAddress(nodeDTO.getIpaddress());
		alarmDeviceDependence.setDeviceName(nodeDTO.getName());
		alarmDeviceDependence.setNodeLevel(nodeLevel + 1);

		return alarmDeviceDependence;
	}

	/**
	 * isExistNodeId:�ж��Ƿ������dependenceVector
	 * <p>
	 * 
	 * @param nodeId
	 * @return
	 * 
	 * @since v1.01
	 */
	public boolean isExistNodeId(String nodeId) {
		boolean flag = false;
		for (int i = 0; i < dependenceVector.size(); i++) {
			AlarmDeviceDependence alarmDeviceDependence = dependenceVector
					.elementAt(i);
			String node_id = String.valueOf(alarmDeviceDependence.getNodeid());
			if (nodeId.equals(node_id)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	public boolean saveDependenceList() {
		boolean flag = false;
		StringBuffer stringBuffer = null;
		DBManager dbManager = new DBManager();
		if (dependenceVector.size() > 0) {
			for (int i = 0; i < dependenceVector.size(); i++) {
				AlarmDeviceDependence alarmDeviceDependence = dependenceVector
						.get(i);
				stringBuffer = new StringBuffer();
				stringBuffer.append("insert into nms_alarm_device_dependence(fid,type,subtype,nodeid,link_port,ip_address,device_name,node_level) values('");
				stringBuffer.append(alarmDeviceDependence.getFid()+"','");
				stringBuffer.append(alarmDeviceDependence.getType()+"','");
				stringBuffer.append(alarmDeviceDependence.getSubtype()+"','");
				stringBuffer.append(alarmDeviceDependence.getNodeid()+"','");
				stringBuffer.append(alarmDeviceDependence.getLinkPort()+"','");
				stringBuffer.append(alarmDeviceDependence.getIpAddress()+"','");
				stringBuffer.append(alarmDeviceDependence.getDeviceName()+"','");
				stringBuffer.append(alarmDeviceDependence.getNodeLevel()+"')");
				dbManager.addBatch(stringBuffer.toString());
			}
		}
		try {
			dbManager.executeBatch();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbManager.close();
		}
		return flag;
	}

	/**
	 * getInitNodeId:
	 * <p>
	 * 
	 * @return String -
	 * @since v1.01
	 */
	public String getInitNodeId() {
		return initNodeId;
	}

	/**
	 * setInitNodeId:
	 * <p>
	 * 
	 * @param initNodeId -
	 * @since v1.01
	 */
	public void setInitNodeId(String initNodeId) {
		this.initNodeId = initNodeId;
	}

}
