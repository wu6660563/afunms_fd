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
 * MQ���ͱ��Ķ�
 * 
 * @author ��Ʒ��
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
	 * connectionFactory:���ӹ�����JMS ������������
	 * <p>
	 * 
	 * @since v1.01
	 */
	private ConnectionFactory connectionFactory = null;

	/**
	 * connection:JMS �ͻ��˵�JMS Provider ������
	 * <p>
	 * 
	 * @since v1.01
	 */
	private Connection connection = null;

	/**
	 * session:һ�����ͻ������Ϣ���߳�
	 * <p>
	 * 
	 * @since v1.01
	 */
	private Session session = null;

	/**
	 * destination:��Ϣ��Ŀ�ĵ�;��Ϣ���͸�˭.
	 * <p>
	 * 
	 * @since v1.01
	 */
	private Destination destination = null;

	/**
	 * producer:��Ϣ������
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
		// ����ConnectionFactoryʵ�����󣬴˴�����ActiveMq��ʵ��jar
		connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD, "tcp://" + ipaddress + ":"
						+ port);

		try {
			// ����ӹ����õ����Ӷ���
			connection = connectionFactory.createConnection();
			// ����
			connection.start();
			// ��ȡ��������
			session = connection.createSession(Boolean.TRUE,
					Session.AUTO_ACKNOWLEDGE);

			destination = session.createTopic(topicName);
			// �õ���Ϣ�����ߡ�������
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
		session.commit(); // һ��Ҫ�ύ�����ύ������ղ�����Ϣ
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
		String str = "��";
		System.out.println(str.getBytes()[0] + "======" + str.getBytes()[1]);
		message.writeBytes(str.getBytes());
		// ������Ϣ��Ŀ�ĵط�
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
				System.out.println("�ѷ���һ�Σ�");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
