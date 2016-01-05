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
 * @author      吴品龙
 * @version     v1.01
 * @since       v1.01
 * @Date        Oct 22, 2013 3:31:43 PM
 * @mail		wupinlong@dhcc.com.cn
 */
public class SenderDemo {

	private static final int SEND_NUMBER = 10;

	public static void main(String[] args) {
		// ConnectionFactory ：连接工厂，JMS 用它创建连接
		ConnectionFactory connectionFactory;
		// Connection ：JMS 客户端到JMS Provider 的连接
		Connection connection = null;
		// Session： 一个发送或接收消息的线程
		Session session;
		// Destination ：消息的目的地;消息发送给谁.
		Destination destination;
		// MessageProducer：消息发送者
		MessageProducer producer;
		// TextMessage message;
		// 构造ConnectionFactory实例对象，此处采用ActiveMq的实现jar
		connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:51616");
		try {
			// 构造从工厂得到连接对象
			connection = connectionFactory.createConnection();
			// 启动
			connection.start();
			// 获取操作连接
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
			message.setStringProperty("SrcFlag", "192.168.0.1$10.1.1.1"); // 发起方标识
			message.setStringProperty("DestFlag", "192.168.0.1$10.1.1.2");	//目标地址标识
			String str = new String(
					"在平台管理后台为每个系统提供两条通信队列(queue)及多条数据传输topic，其中：两条通信队列中一条用于平台向系统发送控制/命令报文，另一条接收系统发送的响应报文；数据传输topic用于系统向平台发送数据报文，按照数据实时性要求不同，采用不同的通道传输，防止数据间处理互相影响。");
			message.writeBytes(str.getBytes());
			// 发送消息到目的地方
			System.out.println(str);
			producer.send(message);
		}
	}
}
