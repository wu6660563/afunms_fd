/*
 * @(#)CreateModelService.java     v1.01, Jul 23, 2014
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.afunms.common.base.BaseVo;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.Constant;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.initialize.ResourceCenter;


/**
 * ClassName:   CreateModelObjectService.java
 * <p> 创建模型对象数据报文（创建XML的Service类）
 *
 * @author      吴品龙
 * @version     v1.01
 * @since       v1.01
 * @Date        Jul 24, 2014 9:58:43 AM
 * @mail		wupinlong@dhcc.com.cn
 */
public class CreateModelObjectService {
	
	/**
	 * deviceHashtable:
	 * <p> devices
	 *
	 * @since   v1.01
	 */
	private Vector<Hashtable<String, Object>> deviceVector = new Vector<Hashtable<String, Object>>();
	
	/**
	 * ipSubnetVector:
	 * <p> ip_subnets
	 *
	 * @since   v1.01
	 */
	private Vector<Hashtable<String, Object>> ipSubnetVector = new Vector<Hashtable<String, Object>>();
	
	/**
	 * vlansVector:
	 * <p>　vlans
	 *
	 * @since   v1.01
	 */
	private Vector<Hashtable<String, Object>> vlansVector = new Vector<Hashtable<String, Object>>();
	
	/**
	 * relationsVector:
	 * <p> relations
	 *
	 * @since   v1.01
	 */
	private Vector<Hashtable<String, String>> relationsVector = new Vector<Hashtable<String, String>>();
	
	/**
	 * deleterelationsVector:
	 * <p> deleterelations
	 *
	 * @since   v1.01
	 */
	private Vector<Hashtable<String, String>> deleterelationsVector = new Vector<Hashtable<String, String>>();
	
	/**
	 * deleteObjectsVector:
	 * <p> deleteObjects
	 *
	 * @since   v1.01
	 */
	private Vector<Hashtable<String, String>> deleteObjectsVector = new Vector<Hashtable<String, String>>();
	
	private String fileName = ResourceCenter.getInstance().getSysPath() + File.separator + "middle_file" + File.separator + "model_object.xml";
	
	public static CreateModelObjectService service;
	
	public Document document;
	
	private CreateModelObjectService() {
		
	}
	
	public static CreateModelObjectService getInstance() {
		if(service == null) {
			return new CreateModelObjectService();
		}
		return service;
	}
	
	public String createModelObject() {
		Format f = Format.getPrettyFormat();
		f.setEncoding("utf-8");
		XMLOutputter out = new XMLOutputter(f);
		String xmlString = "";
		try {
			out.output(document, new FileOutputStream(fileName));
			xmlString = docToString(document);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(out != null) out.clone();
		}
		return xmlString;
	}
	
	public Element createDocument() {
		document = new Document();
		Element model = new Element("model");
		
		//得到当前时间
		Calendar cal = Calendar.getInstance();
		String time_str = "";
		String month = (cal.get(Calendar.MONTH)+1) > 10 ? (""+(cal.get(Calendar.MONTH)+1)):("0"+(cal.get(Calendar.MONTH)+1));
		String date = (cal.get(Calendar.DATE) > 10 ? (""+cal.get(Calendar.DATE)):("0"+cal.get(Calendar.DATE)));
		String hour = (cal.get(Calendar.HOUR_OF_DAY) > 10 ? (""+cal.get(Calendar.HOUR_OF_DAY)):("0"+cal.get(Calendar.HOUR_OF_DAY)));
		String minute = (cal.get(Calendar.MINUTE) > 10 ? (""+cal.get(Calendar.MINUTE)):("0"+cal.get(Calendar.MINUTE)));
		String second = (cal.get(Calendar.SECOND) > 10 ? (""+cal.get(Calendar.SECOND)):("0"+cal.get(Calendar.SECOND)));
		String millisecond = "";
		if(cal.get(Calendar.MILLISECOND) > 100) {
			millisecond = ""+cal.get(Calendar.MILLISECOND);
		} else if(cal.get(Calendar.MILLISECOND) < 100 && cal.get(Calendar.MILLISECOND) > 10) {
			millisecond = "0"+cal.get(Calendar.MILLISECOND);
		} else if(cal.get(Calendar.MILLISECOND) < 10) {
			millisecond = "00"+cal.get(Calendar.MILLISECOND);
		}
		time_str = cal.get(Calendar.YEAR) + month + date
			+ hour + minute + second + millisecond;
		model.setAttribute("timestmp", time_str);
		document.addContent(model);
		return model;
	}
	
	public Element createHeader(String systype, String sysname, String sysdomain, String msgtype) {
		Element header = new Element("header");
		
		Element header_systype = new Element("systype");
		header_systype.setText(systype);
		header.addContent(header_systype);
		Element header_sysname = new Element("sysname");
		header_sysname.setText(sysname);
		header.addContent(header_sysname);
		Element header_sysdomain = new Element("sysdomain");
		header_sysdomain.setText(sysdomain);
		header.addContent(header_sysdomain);
		Element header_msgtype = new Element("msgtype");
		header_msgtype.setText(msgtype);
		header.addContent(header_msgtype);
		return header;
	}
	
	public Element createBody() {
		Element body = new Element("body");
		return body;
	}
	
	public List<NodeDTO> getDevicesList(){
		NodeUtil nodeUtil = new NodeUtil();
		List<BaseVo> basevolist = new ArrayList<BaseVo>();

		// 暂时只推送网络设备、服务器、防火墙
		List<BaseVo> netList = nodeUtil.getNodeByTyeAndSubtype(
				Constant.TYPE_NET, Constant.ALL_SUBTYPE);
		basevolist.addAll(netList);
		List<BaseVo> hostList = nodeUtil.getNodeByTyeAndSubtype(
				Constant.TYPE_HOST, Constant.ALL_SUBTYPE);
		basevolist.addAll(hostList);
		List<BaseVo> firewallList = nodeUtil.getNodeByTyeAndSubtype(
				Constant.TYPE_FIREWALL, Constant.ALL_SUBTYPE);
		basevolist.addAll(firewallList);

		List<NodeDTO> dtoList = nodeUtil.conversionToNodeDTO(basevolist);
		return dtoList;
	}
	
	/**
	 * buildDevices:
	 * <p>
	 *
	 * @return
	 *
	 * @since   v1.01
	 */
	@SuppressWarnings("unchecked")
	public Element buildDevices() {
		Element devices = null;
		if(deviceVector != null && deviceVector.size() > 0) {
			devices = new Element("devices");
			for (Hashtable<String, Object> deviceHashtable : deviceVector) {
				Element device = new Element("device");
				devices.addContent(device);

				Element device_lid = new Element("lid");
				device_lid.setText(String.valueOf(deviceHashtable.get("lid")));
				device.addContent(device_lid);
				Element device_name = new Element("name");
				device_name.setText(String.valueOf(deviceHashtable.get("name")));
				device.addContent(device_name);
				Element device_desc = new Element("desc");
				device_desc.setText(String.valueOf(deviceHashtable.get("desc")));
				device.addContent(device_desc);
				Element device_ip = new Element("ip");
				device_ip.setText(String.valueOf(deviceHashtable.get("ip")));
				device.addContent(device_ip);
				Element device_type = new Element("type");
				device_type.setText(String.valueOf(deviceHashtable.get("type"))); // 设备类型
				device.addContent(device_type);

				//build network_ports
				Vector<Hashtable<String, Object>> networkPortsVector = (Vector<Hashtable<String, Object>>) deviceHashtable.get("network_ports");
				if(networkPortsVector != null && networkPortsVector.size() > 0) {
					Element network_ports = new Element("network_ports");
					device.addContent(network_ports);
					for (Hashtable<String, Object> network_portHash : networkPortsVector) {
						Element network_port = new Element("network_port");
						network_ports.addContent(network_port);
						
						Element network_port_lid = new Element("lid");
						network_port_lid.setText(String.valueOf(network_portHash.get("lid")));
						network_port.addContent(network_port_lid);
						Element network_port_perf_group = new Element("perf_group");
						network_port_perf_group.setText(String.valueOf(network_portHash.get("perf_group")));
						network_port.addContent(network_port_perf_group);
						Element network_port_name = new Element("name");
						network_port_name.setText(String.valueOf(network_portHash.get("name")));
						network_port.addContent(network_port_name);
						Element network_port_desc = new Element("desc");
						network_port_desc.setText(String.valueOf(network_portHash.get("desc")));
						network_port.addContent(network_port_desc);
						Element network_port_mac = new Element("mac");
						String macvalue = String.valueOf(network_portHash.get("mac"));
						if(macvalue == null || "".equalsIgnoreCase(macvalue)) {
							macvalue = "null";
						}
						network_port_mac.setText(macvalue);
						network_port.addContent(network_port_mac);
						
						Vector<Hashtable<String, Object>> ipsVector = (Vector<Hashtable<String, Object>>) network_portHash.get("ips");
						if(ipsVector != null && ipsVector.size() > 0) {
							Element network_port_ips = new Element("ips");
							network_port.addContent(network_port_ips);
							//循环IPS标签，多N个IP标签
							for (Hashtable<String, Object> ipHash : ipsVector) {
								Element ips_ip = new Element("ip");
								network_port_ips.addContent(ips_ip);
								Element ips_address = new Element("address");
								ips_address.setText(String.valueOf(ipHash.get("address")));
								ips_ip.addContent(ips_address);
								Element ips_subnet_mask = new Element("subnet_mask");
								ips_subnet_mask.setText(String.valueOf(ipHash.get("subnet_mask")));
								ips_ip.addContent(ips_subnet_mask);
							}
						}
					}
				}
				
				
				//build components
				Vector<Hashtable<String, Object>> componentsVector = (Vector<Hashtable<String, Object>>) deviceHashtable.get("components");
				if(componentsVector != null && componentsVector.size() > 0) {
					Element components = new Element("components");
					device.addContent(components);
					for (Hashtable<String, Object> componetHash : componentsVector) {
						Element component = new Element("component");
						components.addContent(component);
						
						Element component_lid = new Element("lid");
						component_lid.setText(String.valueOf(componetHash.get("lid")));
						component.addContent(component_lid);
						Element component_perf_group = new Element("perf_group");
						component_perf_group.setText(String.valueOf(componetHash.get("perf_group")));
						component.addContent(component_perf_group);
						Element component_name = new Element("name");
						component_name.setText(String.valueOf(componetHash.get("name")));
						component.addContent(component_name);
						Element component_desc = new Element("desc");
						component_desc.setText(String.valueOf(componetHash.get("desc")));
						component.addContent(component_desc);
					}
				}
			}
		}
		return devices;
	}
	
	/**
	 * buildIp_subnets:
	 * <p> 构建ip_subnets
	 *
	 * @return
	 *
	 * @since   v1.01
	 */
	@SuppressWarnings("unchecked")
	public Element buildIp_subnets() {
		Element ip_subnets = null;
		if(ipSubnetVector != null && ipSubnetVector.size() > 0) {
			ip_subnets = new Element("ip_subnets");
			for (Hashtable<String, Object> ip_subnetHash : ipSubnetVector) {
				Element ip_subnet = new Element("ip_subnet");
				ip_subnets.addContent(ip_subnet);
				
				Element lid = new Element("lid");
				lid.setText(String.valueOf(ip_subnetHash.get("lid")));
				ip_subnet.addContent(lid);
				Element name = new Element("name");
				name.setText(String.valueOf(ip_subnetHash.get("name")));
				ip_subnet.addContent(name);
				Element address = new Element("address");
				address.setText(String.valueOf(ip_subnetHash.get("address")));
				ip_subnet.addContent(address);
				
				Vector<Hashtable<String, Object>> memberVector = (Vector<Hashtable<String, Object>>) ip_subnetHash.get("members");
				if(memberVector != null && memberVector.size() > 0) {
					Element members = new Element("members");
					ip_subnet.addContent(members);
					for (Hashtable<String, Object> memberHash : memberVector) {
						Element member = new Element("member");
						members.addContent(member);
						Element device = new Element("device");
						device.setText(String.valueOf(memberHash.get("device")));
						member.addContent(device);
						Element port = new Element("port");
						port.setText(String.valueOf(memberHash.get("port")));
						member.addContent(port);
					}
				}
			}
		}
		
		return ip_subnets;
	}
	
	/**
	 * buildVlans: 
	 * <p> 构建vlans
	 *
	 * @return
	 *
	 * @since   v1.01
	 */
	@SuppressWarnings("unchecked")
	public Element buildVlans() {
		Element vlans = null;
		if(vlansVector != null && vlansVector.size() > 0) {
			vlans = new Element("vlans");
			for (Hashtable<String, Object> vlanHash : vlansVector) {
				Element vlan = new Element("vlan");
				vlans.addContent(vlan);
				
				Element lid = new Element("lid");
				lid.setText(String.valueOf(vlanHash.get("lid")));
				vlan.addContent(lid);
				Element name = new Element("name");
				name.setText(String.valueOf(vlanHash.get("name")));
				vlan.addContent(name);
				Element address = new Element("address");
				address.setText(String.valueOf(vlanHash.get("address")));
				vlan.addContent(address);
				
				
				Vector<Hashtable<String, Object>> memberVector = (Vector<Hashtable<String, Object>>) vlanHash.get("members");
				if(memberVector != null && memberVector.size() > 0) {
					Element members = new Element("members");
					vlan.addContent(members);
					for (Hashtable<String, Object> memberHash : memberVector) {
						Element member = new Element("member");
						members.addContent(member);
						Element device = new Element("device");
						device.setText(String.valueOf(memberHash.get("device")));
						member.addContent(device);
						Element port = new Element("port");
						port.setText(String.valueOf(memberHash.get("port")));
						member.addContent(port);
					}
				}
			}
		}
		return vlans;
	}
	
	/**
	 * buildRelations:
	 * <p> 构建ralations
	 *
	 * @return
	 *
	 * @since   v1.01
	 */
	public Element buildRelations() {
		Element relations = null;
		if(relationsVector != null && relationsVector.size() > 0) {
			relations = new Element("relations");
			for (Hashtable<String, String> relationHash : relationsVector) {
				Element relation = new Element("relation");
				relations.addContent(relation);
				
				Element object_a = new Element("object_a");
				object_a.setText(relationHash.get("object_a"));
				relation.addContent(object_a);
				Element name = new Element("name");
				name.setText(relationHash.get("name"));
				relation.addContent(name);
				Element object_b = new Element("object_b");
				object_b.setText(relationHash.get("object_b"));
				relation.addContent(object_b);
			}
		}
		return relations;
	}
	
	/**
	 * buildDelete_relations:
	 * <p> 构建delete_ralations
	 *
	 * @return
	 *
	 * @since   v1.01
	 */
	public Element buildDelete_relations() {
		Element delete_relations = null;
		if(deleterelationsVector != null && deleterelationsVector.size() > 0) {
			delete_relations = new Element("delete_relations");
			for (Hashtable<String, String> relationHash : deleterelationsVector) {
				Element delete_relation = new Element("delete_relation");
				delete_relations.addContent(delete_relation);
				
				Element object_a = new Element("object_a");
				object_a.setText(relationHash.get("object_a"));
				delete_relation.addContent(object_a);
				Element name = new Element("name");
				name.setText(relationHash.get("name"));
				delete_relation.addContent(name);
				Element object_b = new Element("object_b");
				object_b.setText(relationHash.get("object_b"));
				delete_relation.addContent(object_b);
			}
		}
		return delete_relations;
	}
	
	/**
	 * buildDelete_objects:
	 * <p> 构建delete_objects
	 *
	 * @return
	 *
	 * @since   v1.01
	 */
	public Element buildDelete_objects() {
		Element delete_objects = null;
		if(deleteObjectsVector != null && deleteObjectsVector.size() > 0) {
			delete_objects = new Element("delete_objects");
			for (Hashtable<String, String> deleteObjectHash : deleteObjectsVector) {
				Element delete_object = new Element("delete_object");
				delete_objects.addContent(delete_object);
				
				Element gid = new Element("gid");
				gid.setText(deleteObjectHash.get("object_a"));
				delete_object.addContent(gid);
			}
		}
		return delete_objects;
	}
	
	public Element buildSafeZone(String safeZone) {
		Element safetyzone = new Element("safetyzone");
		safetyzone.setText(safeZone);
		return safetyzone;
	}
	
    /**
     * stringToDoc:
     * <p> 字符串转换为DOCUMENT
     *
     * @param xmlStr
     * @return
     * @throws Exception
     *
     * @since   v1.01
     */
    public Document stringToDoc(String xmlStr) throws Exception {     
        java.io.Reader in = new StringReader(xmlStr);     
        Document doc = (new SAXBuilder()).build(in);
        in.close();
        return doc;     
    }     
    
    /**
     * docToString:
     * <p> Document转换为字符串
     *
     * @param doc
     * @return
     * @throws Exception
     *
     * @since   v1.01
     */
    public String docToString(Document doc) throws Exception {     
        Format format = Format.getPrettyFormat();     
        format.setEncoding("UTF-8");// 设置xml文件的字符为UTF-8，解决中文问题     
        XMLOutputter xmlout = new XMLOutputter(format);     
        ByteArrayOutputStream bo = new ByteArrayOutputStream();     
        xmlout.output(doc, bo);
        bo.close();
        xmlout.clone();
        return bo.toString("utf-8"); //此处也设置为utf-8，保持一致，中文才不会乱码
    }     
	

	public Vector<Hashtable<String, Object>> getDeviceVector() {
		return deviceVector;
	}

	public Vector<Hashtable<String, Object>> getIpSubnetVector() {
		return ipSubnetVector;
	}

	public Vector<Hashtable<String, Object>> getVlansVector() {
		return vlansVector;
	}

	public Vector<Hashtable<String, String>> getRelationsVector() {
		return relationsVector;
	}

	public Vector<Hashtable<String, String>> getDeleterelationsVector() {
		return deleterelationsVector;
	}

	public Vector<Hashtable<String, String>> getDeleteObjectsVector() {
		return deleteObjectsVector;
	}
	
	public void clearAllData() {
		deviceVector.clear();
		ipSubnetVector.clear();
		vlansVector.clear();
		relationsVector.clear();
		deleterelationsVector.clear();
		deleteObjectsVector.clear();
	}
	
}

