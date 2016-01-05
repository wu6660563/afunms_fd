/*
 * @(#)JDomTest.java     v1.01, Dec 2, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.NodeList;

/**
 * ClassName: JDomTest.java
 * <p>
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Dec 2, 2013 5:18:28 PM
 * @mail wupinlong@dhcc.com.cn
 */
public class JDomTest {

	public static void parseSVG() throws Exception {
		SAXBuilder builder = new SAXBuilder();
		Document document = builder.build("src/svg_template.xml");// 获得文档对象
		Element root = document.getRootElement();// 获得根节点
		Element element = root.getChild("defs");
		System.out.println("element:" + element);
		List<Element> list2 = element.getChildren("symbol");
		System.out.println("list2.size():" + list2.size());
		for (Element ele : list2) {
			Element image = ele.getChild("image");
			System.out.println("image-->x:" + image.getAttributeValue("x"));
			System.out.println("image-->href:"
					+ image.getAttributeValue("href"));
		}
	}

	// 解析xml文件
	public static void XmlParse() throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		InputStream file = new FileInputStream("src/po.xml");
		Document document = builder.build(file);// 获得文档对象
		Element root = document.getRootElement();// 获得根节点
		Namespace ns = Namespace.getNamespace("m", "http://tempuri.org/");
		Namespace ns1 = Namespace.getNamespace("xmlns", "http://tempuri.org/");
		root.setNamespace(ns1);
		List<Element> list = root.getChildren();
		for (Element e : list) {
			e.setNamespace(ns);
			System.out.println("ID=" + e.getAttributeValue("id", ns));
			System.out.println("username=" + e.getChildText("username", ns));
			System.out.println("password=" + e.getChildText("password", ns));
			System.out.println("type:xlink="
					+ e.getChild("password").getAttributeValue("xlink", ns,
							"type"));
		}
	}

	public static void test() throws Exception {
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build("src/po.xml");// 获得文档对象

		Namespace ns_SOAP_ENV = Namespace.getNamespace("SOAP-ENV",
				"http://schemas.xmlsoap.org/soap/envelope/");
		Namespace ns_SOAP_ENC = Namespace.getNamespace("SOAP-ENC",
				"http://schemas.xmlsoap.org/soap/encoding/");
		Namespace ns_xsi = Namespace.getNamespace("xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
		Namespace ns_xsd = Namespace.getNamespace("xsd",
				"http://www.w3.org/2001/XMLSchema");
		Namespace ns_m = Namespace.getNamespace("m", "http://tempuri.org/");

		Element Envelope = new Element("Envelope");
		Envelope.setNamespace(ns_SOAP_ENV);
		Envelope.addNamespaceDeclaration(ns_SOAP_ENC);
		Envelope.addNamespaceDeclaration(ns_xsi);
		Envelope.addNamespaceDeclaration(ns_xsd);

		Element Body = new Element("Body");
		Body.setNamespace(ns_SOAP_ENV);

		Element Login = new Element("Login");
		Login.setNamespace(ns_m);

		Element UserName = new Element("UserName").addContent("username");
		UserName.setNamespace(ns_m);

		Element PassWord = new Element("PassWord").addContent("password");
		PassWord.setNamespace(ns_m);

		Login.addContent(UserName).addContent(PassWord);
		Body.addContent(Login);
		Envelope.addContent(Body);

		doc.setRootElement(Envelope);

		// 文件处理
		Format format = Format.getCompactFormat();
		format.setIndent("	");
		XMLOutputter out = new XMLOutputter(format);
		out.output(doc, new FileOutputStream("src/po.xml"));
	}

	public static void parseXmlWithNamespace() throws Exception {
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build("src/po.xml");// 获得文档对象

		Namespace ns_SOAP_ENV = Namespace.getNamespace("SOAP-ENV",
				"http://schemas.xmlsoap.org/soap/envelope/");
		Namespace ns_SOAP_ENC = Namespace.getNamespace("SOAP-ENC",
				"http://schemas.xmlsoap.org/soap/encoding/");
		Namespace ns_xsi = Namespace.getNamespace("xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
		Namespace ns_xsd = Namespace.getNamespace("xsd",
				"http://www.w3.org/2001/XMLSchema");
		Namespace ns_m = Namespace.getNamespace("m", "http://tempuri.org/");
		Element Envelope = doc.getRootElement();
		Envelope.setNamespace(ns_SOAP_ENV);
		Envelope.addNamespaceDeclaration(ns_SOAP_ENC);
		Envelope.addNamespaceDeclaration(ns_xsi);
		Envelope.addNamespaceDeclaration(ns_xsd);

		Element ele1 = Envelope.getChild("Body", ns_SOAP_ENV);
		ele1.getChild("Login", ns_m);
		// System.out.println(ele1.get);
	}

	// 增
	public static void addXml() throws JDOMException, FileNotFoundException,
			IOException {
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build("src/po.xml");// 获得文档对象
		Element root = doc.getRootElement();// 获得根节点

		// 添加新元素
		Element element = new Element("person");
		element.setAttribute("id", "3");
		Element element1 = new Element("username");
		element1.setText("zhangdaihao");
		Element element2 = new Element("password");
		element2.setText("mima");
		element.addContent(element1);
		element.addContent(element2);
		root.addContent(element);
		doc.setRootElement(root);

		// 文件处理
		Format format = Format.getCompactFormat();
		format.setIndent("	");
		XMLOutputter out = new XMLOutputter(format);
		out.output(doc, new FileOutputStream("src/po.xml"));
	}

	// 根据ID值删除一个节点
	public static void deletePerson(int id) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		InputStream file = new FileInputStream("src/po.xml");
		Document doc = builder.build(file);// 获得文档对象
		Element root = doc.getRootElement();// 获得根节点
		List<Element> list = root.getChildren();
		for (Element e : list) {
			// 获取ID值
			if (Integer.parseInt(e.getAttributeValue("id")) == id) {
				root.removeContent(e);
				break;// ??
			}
		}

		// 文件处理
		Format format = Format.getCompactFormat();
		format.setIndent("	");
		XMLOutputter out = new XMLOutputter(format);
		out.output(doc, new FileOutputStream("src/po.xml"));
	}

	// 根据ID值修改一个节点
	public static void updatePerson(int id) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		InputStream file = new FileInputStream("src/po.xml");
		Document doc = builder.build(file);// 获得文档对象
		Element root = doc.getRootElement();// 获得根节点
		List<Element> list = root.getChildren(); // 得到所有的子节点
		// List<Element> list = rootEl.getChildren("disk");
		for (Element e : list) {
			// 获取ID值
			if (Integer.parseInt(e.getAttributeValue("id")) == id) {
				System.out.println("--------------------");
				e.getChild("username").setText("111111111");
				e.getChild("password").setText("password");
			}
		}

		// 文件处理
		Format format = Format.getCompactFormat();
		format.setIndent("	");
		XMLOutputter out = new XMLOutputter(format);
		out.output(doc, new FileOutputStream("src/po.xml"));
	}

	public static void xpathParseXml() throws Exception {
		DocumentBuilder db = null;
		DocumentBuilderFactory dbf = null;
		File file = new File("src/svg_template.xml");
		dbf = DocumentBuilderFactory.newInstance();
		db = dbf.newDocumentBuilder();
		org.w3c.dom.Document doc = db.parse(file);
		XPath xpath = XPathFactory.newInstance().newXPath();
		NodeList nodeList = (NodeList) xpath.evaluate("/svg/defs/symbol[1]", doc,
				XPathConstants.NODESET);
		System.out.println(nodeList.getLength());
		for (int i = 0; i < nodeList.getLength(); i++) {
			org.w3c.dom.Element symbol = (org.w3c.dom.Element) nodeList.item(i);

			org.w3c.dom.Element image = (org.w3c.dom.Element) symbol
					.getElementsByTagName("image").item(0);
			System.out.println(" xlink:href :"
					+ image.getAttribute("xlink:href"));
		}
	}
	
	public static void parseTest(){
		SAXBuilder builder = new SAXBuilder();
		try {
			InputStream file = new FileInputStream("src/jmsserver.xml");
			Document doc = builder.build(file);// 获得文档对象
			Element root = doc.getRootElement();// 获得根节点
			System.out.println(root.getChildText("ipaddress"));
			System.out.println(root.getChildText("port"));
			System.out.println(root.getChildText("reConnectTime"));
			Element topicNames = root.getChild("topicNames");
			List<Element> topicNameList = topicNames.getChildren("topicName");
			for (Element element : topicNameList) {
				System.out.println(element.getText()+">>>>>>>>>"+element.getAttributeValue("enable"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String ars[]) throws Exception {
		// addXml();//增加XML
		// deletePerson(3);//删除XML
		// updatePerson(2);//修改XML
		// XmlParse();// 解析XML
		// parseXmlWithNamespace();
		// parseSVG();

//		parseTest();
		
		//时间格式转化
//		Calendar cal = Calendar.getInstance();
//		String str = "";
//		String month = (cal.get(Calendar.MONTH)+1) > 10 ? (""+(cal.get(Calendar.MONTH)+1)):("0"+(cal.get(Calendar.MONTH)+1));
//		String date = (cal.get(Calendar.DATE) > 10 ? (""+cal.get(Calendar.DATE)):("0"+cal.get(Calendar.DATE)));
//		String hour = (cal.get(Calendar.HOUR_OF_DAY) > 10 ? (""+cal.get(Calendar.HOUR_OF_DAY)):("0"+cal.get(Calendar.HOUR_OF_DAY)));
//		String minute = (cal.get(Calendar.MINUTE) > 10 ? (""+cal.get(Calendar.MINUTE)):("0"+cal.get(Calendar.MINUTE)));
//		String second = (cal.get(Calendar.SECOND) > 10 ? (""+cal.get(Calendar.SECOND)):("0"+cal.get(Calendar.SECOND)));
//		String millisecond = "";
//		if(cal.get(Calendar.MILLISECOND) > 100) {
//			millisecond = ""+cal.get(Calendar.MILLISECOND);
//		} else if(cal.get(Calendar.MILLISECOND) < 100 && cal.get(Calendar.MILLISECOND) > 10) {
//			millisecond = "0"+cal.get(Calendar.MILLISECOND);
//		} else if(cal.get(Calendar.MILLISECOND) < 10) {
//			millisecond = "00"+cal.get(Calendar.MILLISECOND);
//		}
//		str = cal.get(Calendar.YEAR) + month + date
//			+ hour + minute + second + millisecond;
//		System.out.println(str);
		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> map1 = new HashMap<String, Object>();
		List<String> list = new ArrayList<String>();
		list.add("test1");
		list.add("test2");
		list.add("test3");
		map.put("group", "Test");
		map.put("perf", list);
		map1.put("list", list);
		map.put("list", map1);
		String str = net.sf.json.JSONObject.fromObject(map).toString();
		
//		System.out.println(str);
		
//		Map<String, Object> map1 = new HashMap<String, Object>();
//		List<HashMap<String, Object>> set = new LinkedList<HashMap<String, Object>>();
//		map1.put("test1", new Double(100.0));
//		map1.put("test2", 102.000000f);
//		
//		for (int i = 0; i < 4; i++) {
//			HashMap<String, Object> map2 = new HashMap<String, Object>();
//			map2.put("a"+i, 100.0);
//			set.add(map2);
//		}
//		map1.put("set", set);
//		System.out.println(JSONObject.toJSONString(map1));
	}

}
