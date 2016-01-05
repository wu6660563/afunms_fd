/*
 * @(#)MQSend.java     v1.01, Sep 30, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.util;

import java.util.Hashtable;
import java.util.List;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.afunms.initialize.ResourceCenter;

/**
 * ClassName: MQSend.java
 * <p>
 * MQ发送报文端
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Sep 30, 2013 12:20:19 PM
 * @mail wupinlong@dhcc.com.cn
 */
public class MQSend {

	/**
	 * Jms_Server_File:
	 * <p>
	 * 
	 * @since v1.01
	 */
	private static String Jms_Server_File = ResourceCenter.getInstance()
			.getSysPath()
			+ "/WEB-INF/classes/jmsserver.xml";

	/**
	 * connectionFactory:连接工厂，JMS 用它创建连接
	 * <p>
	 * 
	 * @since v1.01
	 */
	private ConnectionFactory connectionFactory = null;

	/**
	 * connection:JMS 客户端到JMS Provider 的连接
	 * <p>
	 * 
	 * @since v1.01
	 */
	private Connection connection = null;

	/**
	 * session:一个发送或接收消息的线程
	 * <p>
	 * 
	 * @since v1.01
	 */
	private Session session = null;

	/**
	 * destination:消息的目的地;消息发送给谁.
	 * <p>
	 * 
	 * @since v1.01
	 */
	private Destination destination = null;

	/**
	 * producer:消息发送者
	 * <p>
	 * 
	 * @since v1.01
	 */
	private MessageProducer producer = null;

	/**
	 * ipaddress:
	 * <p>
	 * 
	 * @since v1.01
	 */
	private static String ipaddress = "localhost";

	/**
	 * port:
	 * <p>
	 * 
	 * @since v1.01
	 */
	private static String port = "61616";

	private String topicName = "topic_fs_donghua_all_nr";

	public static Hashtable<String, String> topicHash = new Hashtable<String, String>();

	static {
		try {
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(Jms_Server_File);
			ipaddress = document.getRootElement().getChildText("ipaddress");
			port = document.getRootElement().getChildText("port");

			List<Element> topicSendNames = document.getRootElement().getChild(
					"topicSendNames").getChildren("topicName");
			for (Element element : topicSendNames) {
				String topicName = element.getText();
				String type = element.getAttributeValue("type");
				topicHash.put(type, topicName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MQSend(String type) {
		this.topicName = topicHash.get(type);
		init();
	}

	/**
	 * 
	 * init:
	 * <p>
	 * 
	 * @since v1.01
	 */
	private void init() {
		// TextMessage message;
		// 构造ConnectionFactory实例对象，此处采用ActiveMq的实现jar
		connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD, "tcp://" + ipaddress + ":"
						+ port);

		try {
			// 构造从工厂得到连接对象
			connection = connectionFactory.createConnection();
			// 启动
			connection.start();
			// 获取操作连接
			session = connection.createSession(Boolean.TRUE,
					Session.AUTO_ACKNOWLEDGE);

			destination = session.createTopic(topicName);
			// 得到消息生成者、发送者
			producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * sendMessage:
	 * <p>
	 * 
	 * @param bytes
	 * @throws Exception
	 * 
	 * @since v1.01
	 */
	public void sendMessage(byte[] bytes) throws Exception {
		BytesMessage message = session.createBytesMessage();
		message.setJMSType("BytesMessage");
		message.writeBytes(bytes);
		producer.send(message);
		session.commit(); // 一定要提交，不提交会造成收不到信息
	}

	/**
	 * sendMessageTest:
	 * <p>
	 * 
	 * @throws Exception
	 * 
	 * @since v1.01
	 */
	private void sendMessageTest() throws Exception {
		BytesMessage message = session.createBytesMessage();
		message.setJMSType("BytesMessage");
		String str = "在";
		System.out.println(str.getBytes()[0] + "======" + str.getBytes()[1]);
		message.writeBytes(str.getBytes());
		// 发送消息到目的地方
		producer.send(message);

		session.commit();
	}

	/**
	 * close:
	 * <p>
	 * 
	 * 
	 * @since v1.01
	 */
	public void close() {
		try {
			if (session != null)
				session.close();
			if (session != null)
				connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

	/**
	 * main:
	 * <p>
	 * 
	 * @param args
	 * 
	 * @since v1.01
	 */
	public static void main(String[] args) {
		while (true) {
			MQSend send = new MQSend("topic_fs_donghua_all_nr");
			try {
				send.sendMessageTest();
				Thread.sleep(1000 * 2);
				System.out.println("已发送一次！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
