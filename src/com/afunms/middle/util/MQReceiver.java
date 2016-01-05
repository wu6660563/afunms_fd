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
 * MQ���ձ��Ķ�
 * 
 * @author ��Ʒ��
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

				// *****************Զ�̵��Ĳ���--start--************************
				RemoteViewService service = new RemoteViewService();
				// System.out.println("topicName:" + topicName + "���յ���Ϣ���ȣ�" + mess.length());
				for (byte b : temp) {
					System.out.print(b + " ");
				}
				System.out.println();
				DL476Packets packets = new DL476Packets();
				packets.addRequestPackets(temp);
				service.execute(packets);
				// *****************Զ�̵��Ĳ���--end--************************
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLogger.error("consumer�Ѿ����ر�...topicNameΪ" + topicName, e);
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
				// �����ʼ���������Ӳ���MQ
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
				SysLogger.info("JMS-->topicNameΪ��" + topicName
						+ "----�Ѿ��˳����յȴ���");
				JMSReceiveFactory.setConnect(false);
				JMSReceiveFactory.getInstance().notifyThread();
				break;
			}
		}
	}

}
