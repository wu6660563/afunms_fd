/*
 * @(#)SendModelTask.java     v1.01, Jul 16, 2014
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.task;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import java.util.Vector;

import org.jdom.Element;

import com.afunms.common.util.DBManager;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.indicators.util.NodeGatherIndicatorsUtil;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.middle.service.CreateModelObjectService;
import com.afunms.middle.util.SendDataConstant;
import com.afunms.temp.dao.InterfaceTempDao;
import com.afunms.temp.model.NodeTemp;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.dao.LinkDao;
import com.afunms.topology.model.HostNode;
import com.afunms.topology.model.Link;

/**
 * ClassName:   SendModelObjectTask.java
 * <p>模型对象
 *
 * @author      吴品龙
 * @version     v1.01
 * @since       v1.01
 * @Date        Jul 16, 2014 2:40:17 PM
 * @mail		wupinlong@dhcc.com.cn
 */
public class SendModelObjectTask extends TimerTask {
	
//	private static java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		List<NodeDTO> list = null;
		CreateModelObjectService service = CreateModelObjectService.getInstance();
		service.clearAllData();
		try {
			list = service.getDevicesList();
			
			//组装数据deviceVector
			HostNodeDao hostNodeDao = null;
			DBManager dbManager = null;
			if(list != null && list.size() > 0) {
				for (NodeDTO nodeDTO : list) {
					Hashtable<String, Object> deviceHash = new Hashtable<String, Object>();
					hostNodeDao = new HostNodeDao();
					HostNode hostnode = (HostNode) hostNodeDao.findByID(nodeDTO.getNodeid());
					hostNodeDao.close();
					
					int category = hostnode.getCategory();
					String sql = "SELECT * FROM nms_manage_nodetype WHERE category='"+category+"'";
					dbManager = new DBManager();
					ResultSet resultset = dbManager.executeQuery(sql);
					String type = SendDataConstant.TYPE_UNKNOWN;
					if(resultset.next()) {
						String name = resultset.getString("name");
						if("net_router".equals(name)) {
							type = SendDataConstant.TYPE_NETWORK_ROUTE_DEVICE;
						} else if("net_switch".equals(name)) {
							type = SendDataConstant.TYPE_NETWORK_SWITCH_DEVICE;
						} else if("net_firewall".equals(name)) {
							type = SendDataConstant.TYPE_NETWORK_FIREWALL_DEVICE;
						} else if("net_server".equals(name)) {
							if("windows".equals(nodeDTO.getSubtype())) {
								type = SendDataConstant.TYPE_SERVER_WINDOWS_DEVICE;
							} else if("linux".equals(nodeDTO.getSubtype())) {
								type = SendDataConstant.TYPE_SERVER_LINUX_DEVICE;
							} else if("aix".equals(nodeDTO.getSubtype())) {
								type = SendDataConstant.TYPE_SERVER_AIX_DEVICE;
							} else {
								type = SendDataConstant.TYPE_SERVER;
							}
						} else {
							type = SendDataConstant.TYPE_NETWORK_DEVICE;
						}
					}
					resultset.close();
					dbManager.close();
					
					deviceHash.put("lid", nodeDTO.getNodeid());
					deviceHash.put("name", nodeDTO.getName());
					deviceHash.put("desc", nodeDTO.getName());
					deviceHash.put("ip", nodeDTO.getIpaddress());
					deviceHash.put("type", type);
					
					InterfaceTempDao interfacedao = new InterfaceTempDao(nodeDTO.getIpaddress());
					List allinterfacelist = interfacedao.getNodeTempList(nodeDTO.getId()+"", nodeDTO.getType(), nodeDTO.getSubtype(), null);
					//networks
					Vector<Hashtable<String, Object>> networkVector = new Vector<Hashtable<String, Object>>();
					Map<String, Hashtable<String, Object>> networkportMap = new HashMap<String, Hashtable<String, Object>>();
					
					if(allinterfacelist != null && allinterfacelist.size() > 0) {
						for (int i = 0; i < allinterfacelist.size(); i++) {
							NodeTemp vo = (NodeTemp)allinterfacelist.get(i);
							
							if("ifDescr".equals(vo.getSubentity()) || "ifPhysAddress".equals(vo.getSubentity())) {
								String thevalue = vo.getThevalue();
								if(thevalue == null || "null".equalsIgnoreCase(thevalue) || "".equals(thevalue)) {
									thevalue = "null";
								}
								if(networkportMap.containsKey(vo.getSindex())) {
									if("ifDescr".equals(vo.getSubentity())) {
										networkportMap.get(vo.getSindex()).put("name", thevalue);
										networkportMap.get(vo.getSindex()).put("desc", thevalue);
									}
									if("ifPhysAddress".equals(vo.getSubentity())) {
										networkportMap.get(vo.getSindex()).put("mac", thevalue);
									}
								} else {
									Hashtable<String, Object> networkHash = new Hashtable<String, Object>();
									networkHash.put("lid", vo.getSindex());
									networkHash.put("name", "");
									networkHash.put("desc", "");
									networkHash.put("mac", "null");
									if("ifDescr".equals(vo.getSubentity())) {
										networkHash.put("name", thevalue);
										networkHash.put("desc", thevalue);
									}
									if("ifPhysAddress".equals(vo.getSubentity())) {
										networkHash.put("mac", thevalue);
									}
									networkHash.put("perf_group", "interface");
									networkportMap.put(vo.getSindex(), networkHash);
									networkVector.add(networkHash);
								}
							}
						}
					}
					if(networkVector != null && networkVector.size() > 0) {
						deviceHash.put("network_ports", networkVector);
					}
					
					//components
					NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
					List<NodeGatherIndicators> NodeGatherIndicatorsList = nodeGatherIndicatorsUtil.getGatherIndicatorsForNode(nodeDTO.getNodeid(), nodeDTO.getType(), nodeDTO.getSubtype(), "1");
					Vector<Hashtable<String, String>> components = new Vector<Hashtable<String, String>>();
					for (NodeGatherIndicators nodeGatherIndicators : NodeGatherIndicatorsList) {
						Hashtable<String, String> component = new Hashtable<String, String>();
						component.put("lid", String.valueOf(nodeGatherIndicators.getId()));
						component.put("perf_group", String.valueOf(nodeGatherIndicators.getName()));
						component.put("name", String.valueOf(nodeGatherIndicators.getName()));
						component.put("desc", String.valueOf(nodeGatherIndicators.getDescription()));
						components.add(component);
					}
					if(components != null && components.size() > 0) {
						deviceHash.put("components", components);
					}
					service.getDeviceVector().add(deviceHash);
				}
			}
			
			//组装relations
			LinkDao dao = new LinkDao();
			List<Link> linkList = dao.loadAll();
			for (Link link : linkList) {
				Hashtable<String, String> relationHash = new Hashtable<String, String>();
				relationHash.put("object_a", link.getStartDescr());
				relationHash.put("name", "topo_link");
				relationHash.put("object_b", link.getEndDescr());
				service.getRelationsVector().add(relationHash);
			}
			Element model = service.createDocument();
			Element header = service.createHeader(SendDataConstant.HEADER_SYSTYPE, SendDataConstant.HEADER_SYSNAME, SendDataConstant.HEADER_SYSDOMAIN, SendDataConstant.HEADER_MSGTYPE_1);
			model.addContent(header);
			Element body = service.createBody();
			model.addContent(body);
			Element safezone = service.buildSafeZone(SendDataConstant.SAFETYZONE_1);
			body.addContent(safezone);
			Element devices = service.buildDevices();
			if(devices != null) {
				body.addContent(devices);
			}
			Element ip_subnets = service.buildIp_subnets();
			if(ip_subnets != null) {
				body.addContent(ip_subnets);
			}
			Element vlans = service.buildVlans();
			if(vlans != null) {
				body.addContent(vlans);
			}
			Element relations = service.buildRelations();
			if(relations != null) {
				body.addContent(relations);
			}
			Element delete_objects = service.buildDelete_objects();
			if(delete_objects != null) {
				body.addContent(delete_objects);
			}
			Element delete_relations = service.buildDelete_relations();
			if(delete_relations != null) {
				body.addContent(delete_relations);
			}
			String xmlStr = service.createModelObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

