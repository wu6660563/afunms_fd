/**
 * 
 */
package com.afunms.middle.util;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.afunms.common.util.SysLogger;
import com.afunms.initialize.ResourceCenter;

/**
 * 
 * ClassName: JMSReceiveFactory.java
 * <p>
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Dec 23, 2013 11:09:21 AM
 * @mail wupinlong@dhcc.com.cn
 */
public class JMSReceiveFactory implements Runnable {

	private static String JMS_SERVER_XML = ResourceCenter.getInstance()
			.getSysPath()
			+ "/WEB-INF/classes/jmsserver.xml";

	private static JMSReceiveFactory jmsReceiveFactory;

	private static ConnectionFactory connectionFactory;

	private static Connection connection = null;

	private static Session session = null;

	private static HashMap<String, Destination> destMap = new HashMap<String, Destination>();

	private static boolean isConnect = false; // 是否连接JMS

	private static String ipaddress = "localhost";

	private static String port = "51616";

	private static Long reConnectTime = 20L;

	private JMSReceiveFactory() {
		init();
	}

	public static JMSReceiveFactory getInstance() {
		if (jmsReceiveFactory == null) {
			jmsReceiveFactory = new JMSReceiveFactory();
		}
		return jmsReceiveFactory;
	}

	private void init() {
		Document document = null;
		try {
			SAXBuilder builder = new SAXBuilder();
			document = builder.build(JMS_SERVER_XML);
			ipaddress = document.getRootElement().getChildText("ipaddress");
			port = document.getRootElement().getChildText("port");
			String jms_reConnectTime = document.getRootElement().getChildText(
					"reConnectTime");
			if (jms_reConnectTime != null && !"".equals(jms_reConnectTime)) {
				reConnectTime = Long.parseLong(jms_reConnectTime);
			}

		} catch (Exception e) {
			e.printStackTrace();
			isConnect = false;
		}

		try {
			connectionFactory = new ActiveMQConnectionFactory(
					ActiveMQConnection.DEFAULT_USER,
					ActiveMQConnection.DEFAULT_PASSWORD, "tcp://" + ipaddress
							+ ":" + port);
			// 构造从工厂得到连接对象
			connection = connectionFactory.createConnection();
			connection.start();
			// 获取操作连接
			session = connection.createSession(Boolean.FALSE,
					Session.AUTO_ACKNOWLEDGE);
			SysLogger.info("JMS连接成功，新建对象！");

			Element topicNames = document.getRootElement().getChild(
					"topicNames");
			List<Element> topicNameList = topicNames.getChildren("topicName");
			for (Element element : topicNameList) {
				String enable = element.getAttributeValue("enable");
				String topicNameValue = element.getText();
				if ("true".equals(enable)) {
					createTopic(topicNameValue);
				}
			}

			isConnect = true;
		} catch (Exception e) {
			e.printStackTrace();
			isConnect = false;
		}
	}

	public void startReceive() {
		Set<String> set = destMap.keySet();
		for (String string : set) {
			MQReceiver receiver = new MQReceiver(string);
			new Thread(receiver).start();
		}
	}

	public Session getSession() {
		return session;
	}

	public synchronized void notifyThread() {
		this.notify();
	}

	public Destination createTopic(String topicName) throws Exception {
		Destination destination = null;
		if (destMap.containsKey(topicName)) {
			destination = destMap.get(topicName);
		} else {
			destination = session.createTopic(topicName);
			destMap.put(topicName, destination);
		}
		return destination;
	}

	public void reConnect() {
		while (true) {
			try {
				SysLogger.info("JMS正在重连...");
				init();
				Thread.sleep(reConnectTime * 1000);
			} catch (Exception e) {
				e.printStackTrace();
				SysLogger.error("JMS正在重连...重连失败！", e);
				isConnect = false;
			}
			if (isConnect) {
				SysLogger.info("JMS重连成功！");
				break;
			}
		}
	}

	public void close() throws JMSException {
		if (session != null)
			session.close();
		if (connection != null)
			connection.close();
	}

	public static boolean isConnect() {
		return isConnect;
	}

	public static void setConnect(boolean isConnect) {
		JMSReceiveFactory.isConnect = isConnect;
	}

	public void run() {
		while (true) {
			try {
				if (isConnect) {
					synchronized (this) {
						this.wait();
					}
				}
				SysLogger.info("JMS正在重连...");
				init();
				Thread.sleep(reConnectTime);
			} catch (Exception e) {
				e.printStackTrace();
				SysLogger.error("JMS正在重连...重连失败！", e);
				isConnect = false;
			}
			if (isConnect) {
				SysLogger.info("JMS重连成功！");
				startReceive(); // 重新建立一个新线程
			}

		}
	}

}
