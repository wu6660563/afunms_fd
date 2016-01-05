/*
 * @(#)MQReceiver.java     v1.01, Sep 26, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.middle.util;

import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;

import com.afunms.common.util.SysLogger;
import com.afunms.middle.service.RemoteViewService;

/**
 * ClassName: MQReceiver.java
 * <p>
 * MQ接收报文段
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Sep 26, 2013 11:32:57 AM
 * @mail wupinlong@dhcc.com.cn
 */
public class MQReceiver implements Runnable {

	private static Destination destination = null;

	private MessageConsumer consumer = null;

	private String topicName;

	public MQReceiver(String topicName) {
		try {
			this.topicName = topicName;
			destination = JMSReceiveFactory.getInstance()
					.createTopic(topicName);
			consumer = JMSReceiveFactory.getInstance().getSession()
					.createConsumer(destination);
			SysLogger.info("----topicName:" + topicName);
		} catch (Exception e) {
			e.printStackTrace();
			JMSReceiveFactory.setConnect(false);
		}
	}

	private void receiveData() {
		BytesMessage byteMessage = null;
		String mess = null;
		try {
			byteMessage = (BytesMessage) consumer.receive();
			if (byteMessage != null) {
				byte[] temp = new byte[(int) byteMessage.getBodyLength()];
				byteMessage.readBytes(temp);
				mess = new String(temp);

				// *****************远程调阅测试--start--************************
				RemoteViewService service = new RemoteViewService();
				// System.out.println("topicName:" + topicName + "接收到信息长度：" + mess.length());
				for (byte b : temp) {
					System.out.print(b + " ");
				}
				System.out.println();
				DL476Packets packets = new DL476Packets();
				packets.addRequestPackets(temp);
				service.execute(packets);
				// *****************远程调阅测试--end--************************
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLogger.error("consumer已经被关闭...topicName为" + topicName, e);
			try {
				if (consumer != null) {
					consumer.close();
				}
				JMSReceiveFactory.getInstance().close();
				JMSReceiveFactory.setConnect(false);
			} catch (JMSException e1) {
				e1.printStackTrace();
				JMSReceiveFactory.setConnect(false);
			}
		}
	}

	public void run() {
		while (true) {
			if (consumer == null) {
				// 处理初始化程序连接不上MQ
				try {
					destination = JMSReceiveFactory.getInstance().createTopic(
							topicName);
					consumer = JMSReceiveFactory.getInstance().getSession()
							.createConsumer(destination);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (consumer == null) {
					break;
				}
			}
			receiveData();
			if (!JMSReceiveFactory.isConnect()) {
				SysLogger.info("JMS-->topicName为：" + topicName
						+ "----已经退出接收等待！");
				JMSReceiveFactory.setConnect(false);
				JMSReceiveFactory.getInstance().notifyThread();
				break;
			}
		}
	}

}
