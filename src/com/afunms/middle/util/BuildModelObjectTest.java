package com.afunms.middle.util;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.afunms.common.base.BaseVo;
import com.afunms.common.util.DateE;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.Constant;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.topology.dao.LinkDao;
import com.afunms.topology.model.Link;

/**
 * 
 * ClassName: BuildModelObject.java
 * <p>
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Oct 22, 2013 3:32:18 PM
 * @mail wupinlong@dhcc.com.cn
 */
public class BuildModelObjectTest {

	private String fileName;

	public BuildModelObjectTest(String fileName) {
		this.fileName = fileName;
	}
	
	public List<NodeDTO> getDevicesList(){
		NodeUtil nodeUtil = new NodeUtil();
		List<BaseVo> basevolist = new ArrayList<BaseVo>();

		// 暂时只推送网络设备、服务器、防火墙
		List<BaseVo> netList = nodeUtil.getNodeByTyeAndSubtype(
				Constant.TYPE_NET, null);
		basevolist.addAll(netList);
		List<BaseVo> hostList = nodeUtil.getNodeByTyeAndSubtype(
				Constant.TYPE_HOST, null);
		basevolist.addAll(hostList);
		List<BaseVo> firewallList = nodeUtil.getNodeByTyeAndSubtype(
				Constant.TYPE_FIREWALL, null);
		basevolist.addAll(firewallList);

		List<NodeDTO> dtoList = nodeUtil.conversionToNodeDTO(basevolist);
		return dtoList;
	}

	public Element buildDevices() {
		Element devices = new Element("devices");
		
		List<NodeDTO> dtoList = getDevicesList();
		for (int i = 0; i < dtoList.size(); i++) {
			NodeDTO dtovo = dtoList.get(i);

			Element device = new Element("device");
			devices.addContent(device);

			Element device_lid = new Element("lid");
			device_lid.setText(dtovo.getNodeid());
			device.addContent(device_lid);
			Element device_name = new Element("name");
			device_name.setText(dtovo.getName());
			device.addContent(device_name);
			Element device_desc = new Element("desc");
			device_desc.setText(dtovo.getName());
			device.addContent(device_desc);
			Element device_ip = new Element("ip");
			device_ip.setText(dtovo.getIpaddress());
			device.addContent(device_ip);
			Element device_type = new Element("type");
			device_ip.setText(dtovo.getName()); // 设备类型
			device.addContent(device_type);

			Element network_ports = buildNetworkPort(device, dtovo);
			device.addContent(network_ports);
		}

		return devices;
	}

	public Element buildIPSubnet() {
		// 无子网
		Element ip_subnets = new Element("ip_subnets");

		Element ip_subnet = new Element("ip_subnet");
		ip_subnets.addContent(ip_subnet);
		Element ip_subnet_lid = new Element("lid");
		ip_subnet_lid.setText("");
		ip_subnet.addContent(ip_subnet_lid);
		Element ip_subnet_name = new Element("name");
		ip_subnet_name.setText("");
		ip_subnet.addContent(ip_subnet_name);
		Element ip_subnet_address = new Element("address");
		ip_subnet_address.setText("");
		ip_subnet.addContent(ip_subnet_address);
		Element ip_subnet_members = new Element("members");
		ip_subnet_members.setText("");
		ip_subnet.addContent(ip_subnet_members);
		Element members_member = new Element("member");
		members_member.setText("");
		ip_subnet_members.addContent(members_member);
		Element member_device = new Element("device");
		member_device.setText("");
		members_member.addContent(member_device);
		Element member_port = new Element("port");
		member_port.setText("");
		members_member.addContent(member_port);
		return ip_subnets;
	}
	
	public Element buildModelVlan(){
		Element vlans = new Element("vlans");
		
		Element vlan = new Element("vlan");
		vlans.addContent(vlan);
		Element vlan_lid = new Element("lid");
		vlan_lid.setText("");
		vlan.addContent(vlan_lid);
		Element vlan_name = new Element("name");
		vlan_name.setText("");
		vlan.addContent(vlan_name);
		Element vlan_address = new Element("address");
		vlan_address.setText("");
		vlan.addContent(vlan_address);
		Element vlan_members = new Element("members");
		vlan_members.setText("");
		vlan.addContent(vlan_members);
		Element members_member = new Element("member");
		members_member.setText("");
		vlan.addContent(members_member);
		Element member_device = new Element("device");
		member_device.setText("");
		vlan.addContent(member_device);
		Element member_port = new Element("port");
		member_port.setText("");
		vlan.addContent(member_port);
		return vlans;
	}
	
	public Element buildModelRelations() {
		Element relations = new Element("relations");
		LinkDao linkDao = new LinkDao();
		List<Link> linkList = linkDao.loadAll();
		for (int i = 0; i < linkList.size(); i++) {
			Element relation = new Element("relation");
			relations.addContent(relation);
			
			Element object_a = new Element("object_a");
			object_a.setText(linkList.get(i).getStartDescr());
			relation.addContent(object_a);
			Element linkname = new Element("name");
			linkname.setText(linkList.get(i).getLinkName());
			relation.addContent(linkname);
			Element object_b = new Element("object_b");
			object_b.setText(linkList.get(i).getEndDescr());
			relation.addContent(object_b);
		}
		return relations;
	}
	
	public Element buildDeleteObjects() {
		Element delete_objects = new Element("delete_objects");
		
		Element delete_object = new Element("delete_object");
		delete_object.setText("");
		delete_objects.addContent(delete_object);
		return delete_objects;
	}
	
	public Element buildDeleteRelations(){
		Element delete_relations = new Element("delete_relations");
		Element delete_relation = new Element("delete_relation");
		delete_relations.addContent(delete_relation);
		return delete_relations;
	}

	public Element buildNetworkPort(Element device, NodeDTO dtovo) {
		Element network_ports = new Element("network_ports");

		// 得到所有的接口
		String orderFlag = "index";
		String[] time = { "", "" };
		DateE datemanager = new DateE();
		Calendar current = new GregorianCalendar();
		current.set(Calendar.MINUTE, 59);
		current.set(Calendar.SECOND, 59);
		time[1] = datemanager.getDateDetail(current);
		current.add(Calendar.HOUR_OF_DAY, -1);
		current.set(Calendar.MINUTE, 0);
		current.set(Calendar.SECOND, 0);
		time[0] = datemanager.getDateDetail(current);
		String starttime = time[0];
		String endtime = time[1];

		HostLastCollectDataManager hostlastmanager = new HostLastCollectDataManager();
		String[] netInterfaceItem = { "index", "ifDescr", "ifSpeed",
				"ifOperStatus", "OutBandwidthUtilHdxPerc",
				"InBandwidthUtilHdxPerc", "OutBandwidthUtilHdx",
				"InBandwidthUtilHdx", "ifPhysAddress" };
		Vector vector = null;
		try {
			vector = hostlastmanager.getInterface(dtovo.getIpaddress(),
					netInterfaceItem, orderFlag, starttime, endtime);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < vector.size(); i++) {
			String[] strs = (String[]) vector.get(i);
			Element network_port = new Element("network_port");
			network_ports.addContent(network_port);

			Element network_port_lid = new Element("lid");
			network_port_lid.setText(strs[0]); // index
			network_port.addContent(network_port_lid);
			Element network_port_name = new Element("name");
			network_port_name.setText(strs[1]);
			network_port.addContent(network_port_name);
			Element network_port_desc = new Element("desc");
			network_port_desc.setText(strs[1]);
			network_port.addContent(network_port_desc);
			Element network_port_mac = new Element("mac");
			network_port_mac.setText(strs[8]); // ifPhysAddress
			network_port.addContent(network_port_mac);

			// ips
			Element network_port_ips = new Element("ips");
			network_port.addContent(network_port_ips);
			Element network_port_ip = new Element("ip");
			network_port.addContent(network_port_ip);
			Element ip_address = new Element("address");
			ip_address.setText("");
			network_port_ip.addContent(ip_address);
			Element ip_subnet_mask = new Element("subnet_mask");
			ip_subnet_mask.setText("");
			network_port_ip.addContent(ip_subnet_mask);
		}

		return device;
	}

	public Element buildIP(Element element) {
		return element;
	}

	public Element buildHeader(Element model) {
		Element header = new Element("header");
		Element systype = new Element("systype"); // 系统类型
		systype.setText("4"); // 地调系统
		header.addContent(systype);
		Element sysname = new Element("sysname"); // 系统名称
		sysname.setText("afunms");
		header.addContent(sysname);
		Element sysdomain = new Element("sysdomain"); // 所属区域
		sysdomain.setText("4");
		header.addContent(sysdomain);
		Element msgtype = new Element("msgtype"); // 消息类型
		msgtype.setText("1"); // 模型对象数据报文
		header.addContent(msgtype);

		model.addContent(header);
		return model;
	}

	public Document buildXml() {
		// model
		Document doc = new Document();
		Element model = new Element("model");
		doc.addContent(model);

		// header
		Element header = buildHeader(model);
		model.addContent(header);

		// body
		Element body = new Element("body");
		model.addContent(body);
		Element devices = buildDevices();
		body.addContent(devices);
		Element ip_subnets = buildIPSubnet();
		body.addContent(ip_subnets);
		Element vlans = buildModelVlan();
		body.addContent(vlans);
		Element relations = buildModelRelations();
		body.addContent(relations);
		Element delete_objects = buildDeleteObjects();
		body.addContent(delete_objects);
		Element delete_relations = buildDeleteRelations();
		body.addContent(delete_relations);
		
		Format f = Format.getPrettyFormat();
		f.setEncoding("utf-8");
		XMLOutputter out = new XMLOutputter(f);
		try {
			out.output(doc, new FileOutputStream(fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}

	public Document buildDevicesTest() {
		Document document = new Document();
		Element ele = new Element("model");
		document.addContent(ele);

		Element doc_ele0 = new Element("header");
		ele.addContent(doc_ele0);

		Element header_ele0 = new Element("systype"); // 系统类型
		header_ele0.setText("4"); // 地调系统
		doc_ele0.addContent(header_ele0);
		Element header_ele1 = new Element("sysname"); // 系统名称
		header_ele1.setText("afunms");
		doc_ele0.addContent(header_ele1);
		Element header_ele2 = new Element("sysdomain"); // 所属区域
		header_ele2.setText("4"); // 佛山地调
		doc_ele0.addContent(header_ele2);
		Element header_ele3 = new Element("msgtype"); // 消息类型
		header_ele3.setText("1"); // 模型对象数据报文
		doc_ele0.addContent(header_ele3);

		Element doc_ele1 = new Element("body"); // body
		ele.addContent(doc_ele1);

		// devices
		Element devices = new Element("devices"); // devices
		doc_ele1.addContent(devices);
		Element device = new Element("device"); // device
		devices.addContent(device);
		Element lid = new Element("lid"); // lid
		lid.setText("MO-1976678699");
		device.addContent(lid);
		Element name = new Element("name"); // name
		name.setText("switch1");
		device.addContent(name);
		Element desc = new Element("desc"); // desc
		desc.setText("骨干交换机A");
		device.addContent(desc);
		Element ip = new Element("ip"); // ip
		ip.setText("192.168.10.250");
		device.addContent(ip);
		Element type = new Element("type"); // type
		type.setText("network_device");
		device.addContent(type);

		// network_ports
		Element network_ports = new Element("network_ports");
		device.addContent(network_ports);
		Element network_port = new Element("network_port"); // network_port
		network_ports.addContent(network_port);

		Element port_lid = new Element("lid"); // port lid
		port_lid.setText("/root/devices/switch/switch1/port/port1");
		network_port.addContent(port_lid);
		Element port_name = new Element("name"); // port name
		port_name.setText("Port1");
		network_port.addContent(port_name);
		Element port_desc = new Element("desc"); // port desc
		port_desc.setText("port1");
		network_port.addContent(port_desc);
		Element port_mac = new Element("mac"); // port mac
		port_mac.setText("00215e362ef4");
		network_port.addContent(port_mac);
		Element port_ips = new Element("ips"); // port ips
		network_port.addContent(port_ips);
		Element port_ip = new Element("ip"); // port ip
		port_ips.addContent(port_ip);
		Element port_ipaddress = new Element("ipaddress"); // port ipaddress
		port_ipaddress.setText("10.55.28.82");
		port_ip.addContent(port_ipaddress);
		Element port_subnet_mask = new Element("subnet_mask"); // port
		// subnet_mask
		port_subnet_mask.setText("255.255.255.248");
		port_ip.addContent(port_subnet_mask);

		// ip_subnets
		Element ip_subnets = new Element("ip_subnets"); // ip_subnets
		doc_ele1.addContent(ip_subnets);
		Element ip_subnet = new Element("ip_subnet"); // ip_subnet
		ip_subnets.addContent(ip_subnet);

		Element ip_subnet_lid = new Element("lid"); // lid
		ip_subnet_lid.setText("subnet-1433671319");
		ip_subnet.addContent(ip_subnet_lid);
		Element ip_subnet_name = new Element("name"); // name
		ip_subnet_name.setText("10.122.112.0/24");
		ip_subnet.addContent(ip_subnet_name);
		Element ip_subnet_address = new Element("address"); // address
		ip_subnet_address.setText("10.122.112.0/24");
		ip_subnet.addContent(ip_subnet_address);
		Element ip_subnet_members = new Element("members"); // members
		ip_subnet.addContent(ip_subnet_members);
		Element ip_subnet_member = new Element("member"); // member
		ip_subnet_members.addContent(ip_subnet_member);
		Element member_device = new Element("device"); // device
		member_device.setText("MO1118926642");
		ip_subnet_member.addContent(member_device);
		Element member_port = new Element("port"); // port
		member_port.setText("MO-7139776");
		ip_subnet_member.addContent(member_port);

		// valans
		Element vlans = new Element("vlans"); // vlans
		doc_ele1.addContent(vlans);
		Element vlan = new Element("vlan"); // vlan
		vlans.addContent(vlan);
		Element vlan_lid = new Element("lid"); // lid
		vlan_lid.setText("lid_value");
		vlan.addContent(vlan_lid);
		Element vlan_name = new Element("name"); // name
		vlan_name.setText("Private channel to GZ");
		vlan.addContent(vlan_name);
		Element vlan_address = new Element("address"); // address
		vlan_address.setText("10.75.46.128");
		vlan.addContent(vlan_address);
		Element vlan_members = new Element("members"); // members
		vlan.addContent(vlan_members);
		Element vlan_member = new Element("member"); // member
		vlan_members.addContent(vlan_member);
		Element vlan_member_device = new Element("device"); // device
		vlan_member_device.setText("MO1118926642");
		vlan_member.addContent(vlan_member_device);
		Element vlan_port = new Element("port"); // port
		vlan_port.setText("MO-7139776");
		vlan_member.addContent(vlan_port);

		// relations
		Element relations = new Element("relations"); // relations
		doc_ele1.addContent(relations);
		Element relation = new Element("relation"); // relation
		relations.addContent(relation);
		Element object_a = new Element("object_a"); // object_a
		object_a.setText("/roo/devices/Linux/db1");
		relation.addContent(object_a);
		Element relation_name = new Element("name"); // name
		relation_name.setText("topo_link");
		relation.addContent(relation_name);
		Element object_b = new Element("object_b"); // object_b
		object_b.setText("/roo/devices/Linux/db1/eth0");
		relation.addContent(object_b);

		// delete_objects
		Element delete_objects = new Element("delete_objects"); // delete_objects
		doc_ele1.addContent(delete_objects);
		Element delete_object1 = new Element("delete_object"); // delete_object
		delete_object1.setText("gid");
		delete_objects.addContent(delete_object1);
		Element delete_object2 = new Element("delete_object"); // delete_object
		delete_object2.setText("gid");
		delete_objects.addContent(delete_object2);
		Element delete_object3 = new Element("delete_object"); // delete_object
		delete_object3.setText("gid");
		delete_objects.addContent(delete_object3);

		// delete_relations
		Element delete_relations = new Element("delete_relations"); // delete_relations
		doc_ele1.addContent(delete_relations);
		Element delete_relation = new Element("delete_relation"); // delete_relation
		delete_relations.addContent(delete_relation);
		Element delete_relation_object_a = new Element("object_a"); // object_a
		delete_relation_object_a.setText("gid");
		delete_relation.addContent(delete_relation_object_a);
		Element delete_relation_name = new Element("name"); // name
		delete_relation_name.setText("Support");
		delete_relation.addContent(delete_relation_name);
		Element delete_relation_object_b = new Element("object_b"); // object_b
		delete_relation_object_b.setText("gid");
		delete_relation_object_b.setAttribute("test", "test");
		delete_relation.addContent(delete_relation_object_b);

		Format f = Format.getPrettyFormat();
		f.setEncoding("utf-8");
		XMLOutputter out = new XMLOutputter(f);
		try {
			out.output(document, new FileOutputStream(fileName));
			System.out.println(doc2String(document));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.clone();
		}
		return document;
	}

	public static void main(String[] args) {
		BuildModelObjectTest vo = new BuildModelObjectTest("test.xml");
		vo.buildDevicesTest();
		// vo.readDevices("test.xml");
	}
	
	/**   
     * Document转换为字符串   
     *    
     * @param xmlFilePath XML文件路径   
     * @return xmlStr 字符串   
	 * @throws IOException 
     * @throws Exception   
     */    
    public String doc2String(Document doc) throws IOException{     
        Format format = Format.getPrettyFormat();     
        format.setEncoding("UTF-8");// 设置xml文件的字符为UTF-8，解决中文问题     
        XMLOutputter xmlout = new XMLOutputter(format);     
        ByteArrayOutputStream bo = new ByteArrayOutputStream();     
        xmlout.output(doc, bo);
        bo.close();
        return bo.toString();     
    }    
}
