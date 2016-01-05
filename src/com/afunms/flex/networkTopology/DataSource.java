/*
 * Used as a datasource in lieu of a database.
 * Parses /data/devices.xml.
 */

package com.afunms.flex.networkTopology;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import flex.messaging.FlexContext;

import com.afunms.flex.networkTopology.vo.*;

public class DataSource {
	private Device[] _devices;
	private HashMap _deviceHashMap;

	public DataSource(String xml) {
		loadXML(xml);
	}

	public Device[] getDevices() {
		return _devices;
	}
	
	public Device getDeviceDetail(String id) {
		return (Device) _deviceHashMap.get(id);
	}

	private void loadXML(String xml) {
		FileInputStream inputStream = null;
		try {
			ServletContext context = FlexContext.getServletContext();
			//System.out.println(xml);
			String path = context.getRealPath("/flex/data/"+xml);
			if (path != null) {
				// end adjusted
				inputStream = new FileInputStream(path);
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				factory.setIgnoringComments(true);
				factory.setIgnoringElementContentWhitespace(true);

				DocumentBuilder documentBuilder = factory.newDocumentBuilder();
				Document document = documentBuilder.parse(inputStream);

				createDeviceArray(document);
			} else {
				System.out.println("Unable to open devices.xml");
			}
		} catch (ParserConfigurationException e) {
			System.out.println("ParserConfigurationException: " + e);
		} catch (IOException e) {
			System.out.println("IOException: " + e);
		} catch (SAXException e) {
			System.out.println("SAXException: " + e);
		} finally{
			if(inputStream != null){
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// Creates an Array and HashMap of the devices.
	private void createDeviceArray(Document document) {
		Element element = (Element) document.getDocumentElement();
		String title = (String)element.getAttribute("title");
		
		NodeList nodeList = element.getElementsByTagName("device");
		if (nodeList != null && nodeList.getLength() > 0) {
			// Parse through the <device> tags.
			_devices = new Device[nodeList.getLength()];
			_deviceHashMap = new HashMap();

			for (int i = 0; i < nodeList.getLength(); i++) {
				Element element1 = (Element) nodeList.item(i);
				NodeList deviceNodeList = element1.getChildNodes();
				Device device = new Device();
				for (int j = 0; j < deviceNodeList.getLength(); j++) {
					Node node = deviceNodeList.item(j);
					if (node.getNodeType() == 1 && !node.getFirstChild().getNodeValue().equals(null)) {
						device.setParentTitle(title);
						String value = node.getFirstChild().getNodeValue();
						String nodeName = node.getNodeName();
						if (nodeName.equalsIgnoreCase("id"))
							device.setId(value);
						else if (nodeName.equalsIgnoreCase("ip"))
							device.setIp(value);
						else if (nodeName.equalsIgnoreCase("name"))
							device.setName(value);
						else if (nodeName.equalsIgnoreCase("type"))
							device.setType(value);
						else if (nodeName.equalsIgnoreCase("typeName"))
							device.setTypeName(value);
						else if (nodeName.equalsIgnoreCase("iconUrl"))
							device.setIconUrl(value);
						else if (nodeName.equalsIgnoreCase("x"))
							device.setX(Integer.parseInt(value));
						else if (nodeName.equalsIgnoreCase("y"))
							device.setY(Integer.parseInt(value));
						else if (nodeName.equalsIgnoreCase("connections"))
							device.setConnections(createConnectionsArray(node));
						else if (nodeName.equalsIgnoreCase("cpu"))
							device.setCpu(Integer.parseInt(value));
						else if (nodeName.equalsIgnoreCase("memory"))
							device.setMemory(Integer.parseInt(value));
						else if (nodeName.equalsIgnoreCase("incoming"))
							device.setIncoming(Float.parseFloat(value));
						else if (nodeName.equalsIgnoreCase("outgoing"))
							device.setOutgoing(Float.parseFloat(value));
						else if (nodeName.equalsIgnoreCase("ping"))
							device.setPing(Integer.parseInt(value));
						else if (nodeName.equalsIgnoreCase("alarmCpu"))
							device.setAlarmCpu(Integer.parseInt(value));
						else if (nodeName.equalsIgnoreCase("alarmMemory"))
							device.setAlarmMemory(Integer.parseInt(value));
						else if (nodeName.equalsIgnoreCase("alarmIncoming"))
							device.setAlarmIncoming(Float.parseFloat(value));
						else if (nodeName.equalsIgnoreCase("alarmOutgoing"))
							device.setAlarmOutgoing(Float.parseFloat(value));
						else if (nodeName.equalsIgnoreCase("alarmFlag"))
							device.setAlarmFlag(value);
						else if (nodeName.equalsIgnoreCase("alarmDescs"))
							device.setAlarmDesc(createAlarmDescsArray(node));
						else if (nodeName.equalsIgnoreCase("alertTotals"))
							device.setAlertTotals(createAlertTotalsArray(node));
						else if (nodeName.equalsIgnoreCase("admin"))
							device.setAdmin(value);
						else if (nodeName.equalsIgnoreCase("location"))
							device.setLocation(value);
						else if (nodeName.equalsIgnoreCase("responseTime"))
							device.setResponseTime(value);
					}
				}

				_devices[i] = device;
				_deviceHashMap.put(device.getId(), device);
			}
		}
	}

	/*
	 * Creates an array from a <connections> node. <connections>
	 * <connection>XPA1576</connection> </connections>
	 */
	private String[] createConnectionsArray(Node node) {
		Element element = (Element) node;
		NodeList nodeList = element.getElementsByTagName("connection");
		String[] connections = new String[nodeList.getLength()];
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node conectionsNode = nodeList.item(i);
			if (conectionsNode.getNodeType() == 1 && !conectionsNode.getFirstChild().getNodeValue().equals(null))
				connections[i] = conectionsNode.getFirstChild().getNodeValue();
		}

		return connections;
	}

	/*
	 * Creates an array from a <connections> node. <alarmDescs> <alarmDesc>MMMM</alarmDesc>
	 * </alarmDescs>
	 */
	private String[] createAlarmDescsArray(Node node) {
		Element element = (Element) node;
		NodeList nodeList = element.getElementsByTagName("alarmDesc");
		String[] alarmDescs = new String[nodeList.getLength()];
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node alarmDescsNode = nodeList.item(i);
			if (!alarmDescsNode.getFirstChild().getNodeValue().equals(null))
				alarmDescs[i] = alarmDescsNode.getFirstChild().getNodeValue();
		}

		return alarmDescs;
	}

	/*
	 * Creates an array from an <alertTotals> node. <alertTotals> <alert>
	 * <type>Critical</type> <total>16</total> </alert> </alertTotals>
	 */
	private AlertTotal[] createAlertTotalsArray(Node node) {
		Element element = (Element) node;
		NodeList nodeList = element.getElementsByTagName("alert");
		AlertTotal[] alerts = new AlertTotal[nodeList.getLength()];
		for (int i = 0; i < nodeList.getLength(); i++) {
			AlertTotal alert = new AlertTotal();
			NodeList eventNodeList = nodeList.item(i).getChildNodes();
			for (int j = 0; j < eventNodeList.getLength(); j++) {
				Node eventNode = eventNodeList.item(j);
				if (eventNode.getNodeType() == 1 && !eventNode.getFirstChild().getNodeValue().equals(null)) {
					String value = eventNode.getFirstChild().getNodeValue();
					String nodeName = eventNode.getNodeName();
					if (nodeName.equalsIgnoreCase("type"))
						alert.setType(value);
					else if (nodeName.equalsIgnoreCase("total"))
						alert.setTotal(Integer.parseInt(value));
				}
			}
			alerts[i] = alert;
		}

		return alerts;
	}

	/*
	 * Creates an array from an <events> node. <events> <event> <date>1185946089</date>
	 * <description>The time service has not been able to synchronize the system
	 * time for 49152 seconds.</description> </event> </events>
	 */
	private DeviceEvent[] createDeviceEventsArray(Node node) {
		Element element = (Element) node;
		NodeList nodeList = element.getElementsByTagName("event");
		DeviceEvent[] deviceEvents = new DeviceEvent[nodeList.getLength()];
		for (int i = 0; i < nodeList.getLength(); i++) {
			DeviceEvent deviceEvent = new DeviceEvent();
			NodeList eventNodeList = nodeList.item(i).getChildNodes();
			for (int j = 0; j < eventNodeList.getLength(); j++) {
				Node eventNode = eventNodeList.item(j);
				if (eventNode.getNodeType() == 1 && !eventNode.getFirstChild().getNodeValue().equals(null)) {
					String value = eventNode.getFirstChild().getNodeValue();
					String nodeName = eventNode.getNodeName();
					if (nodeName.equalsIgnoreCase("date"))
						deviceEvent.setDate(Long.parseLong(value));
					else if (nodeName.equalsIgnoreCase("description"))
						deviceEvent.setDescription(value);
				}
			}
			deviceEvents[i] = deviceEvent;
		}

		return deviceEvents;
	}

	private int[] createAverageMemoryByMonthArray(Node node) {
		Element element = (Element) node;
		NodeList nodeList = element.getElementsByTagName("percent");
		int[] percents = new int[nodeList.getLength()];
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node percentsNode = nodeList.item(i);
			if (percentsNode.getNodeType() == 1 && !percentsNode.getFirstChild().getNodeValue().equals(null))
				percents[i] = Integer.parseInt(percentsNode.getFirstChild().getNodeValue());
		}

		return percents;
	}
	private int[] createAverageCpuByMonthArray(Node node) {
		Element element = (Element) node;
		NodeList nodeList = element.getElementsByTagName("percent");
		int[] percents = new int[nodeList.getLength()];
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node percentsNode = nodeList.item(i);
			if (percentsNode.getNodeType() == 1 && !percentsNode.getFirstChild().getNodeValue().equals(null))
				percents[i] = Integer.parseInt(percentsNode.getFirstChild().getNodeValue());
		}

		return percents;
	}
	private int[] createAveragePingByMonthArray(Node node) {
		Element element = (Element) node;
		NodeList nodeList = element.getElementsByTagName("percent");
		int[] percents = new int[nodeList.getLength()];
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node percentsNode = nodeList.item(i);
			if (percentsNode.getNodeType() == 1 && !percentsNode.getFirstChild().getNodeValue().equals(null))
				percents[i] = Integer.parseInt(percentsNode.getFirstChild().getNodeValue());
		}

		return percents;
	}
}