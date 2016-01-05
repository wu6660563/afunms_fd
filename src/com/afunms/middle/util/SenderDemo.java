/**
 * 
 */
package com.afunms.middle.util;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 
 * ClassName:   SenderDemo.java
 * <p>
 *
 * @author      ��Ʒ��
 * @version     v1.01
 * @since       v1.01
 * @Date        Oct 22, 2013 3:31:43 PM
 * @mail		wupinlong@dhcc.com.cn
 */
public class SenderDemo {

	private static final int SEND_NUMBER = 10;

	public static void main(String[] args) {
		// ConnectionFactory �����ӹ�����JMS ������������
		ConnectionFactory connectionFactory;
		// Connection ��JMS �ͻ��˵�JMS Provider ������
		Connection connection = null;
		// Session�� һ�����ͻ������Ϣ���߳�
		Session session;
		// Destination ����Ϣ��Ŀ�ĵ�;��Ϣ���͸�˭.
		Destination destination;
		// MessageProducer����Ϣ������
		MessageProducer producer;
		// TextMessage message;
		// ����ConnectionFactoryʵ�����󣬴˴�����ActiveMq��ʵ��jar
		connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:51616");
		try {
			// ����ӹ����õ����Ӷ���
			connection = connectionFactory.createConnection();
			// ����
			connection.start();
			// ��ȡ��������
			session = connection.createSession(Boolean.TRUE,
					Session.AUTO_ACKNOWLEDGE);
			destination = session.createTopic("topic_fs_donghua_all_nr");
			producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			sendMessage(session, producer);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		finally {
//			try {
//				if (null != connection)
//					connection.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
	}

	public static void sendMessage(Session session, MessageProducer producer)
			throws Exception {
		for (int i = 1; i <= SEND_NUMBER; i++) {
			BytesMessage message = session.createBytesMessage();
			message.setJMSType("BytesMessage");
			message.setStringProperty("SrcFlag", "192.168.0.1$10.1.1.1"); // ���𷽱�ʶ
			message.setStringProperty("DestFlag", "192.168.0.1$10.1.1.2");	//Ŀ���ַ��ʶ
			String str = new String(
					"��ƽ̨�����̨Ϊÿ��ϵͳ�ṩ����ͨ�Ŷ���(queue)���������ݴ���topic�����У�����ͨ�Ŷ�����һ������ƽ̨��ϵͳ���Ϳ���/����ģ���һ������ϵͳ���͵���Ӧ���ģ����ݴ���topic����ϵͳ��ƽ̨�������ݱ��ģ���������ʵʱ��Ҫ��ͬ�����ò�ͬ��ͨ�����䣬��ֹ���ݼ䴦����Ӱ�졣");
			message.writeBytes(str.getBytes());
			// ������Ϣ��Ŀ�ĵط�
			System.out.println(str);
			producer.send(message);
		}
	}
}
