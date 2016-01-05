/**
 * 
 */
package com.afunms.middle.util;

import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;




/**
 * 
 * ClassName:   ReceiverDemo.java
 * <p>
 *
 * @author      ��Ʒ��
 * @version     v1.01
 * @since       v1.01
 * @Date        Dec 19, 2013 11:01:41 AM
 * @mail		wupinlong@dhcc.com.cn
 */
public class ReceiverDemo {
	
	private static Destination destination = null;
	
	private MessageConsumer consumer = null;
	
	public ReceiverDemo(String topicName) {
		try {
			destination = JMSReceiveFactory.getInstance().createTopic(topicName);
			consumer = JMSReceiveFactory.getInstance().getSession().createConsumer(destination);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void receive(){
		while (true) {
			while (true) {
				if (consumer == null) {
					// �����ʼ���������Ӳ���MQ
					break;
				}
				receiveData();
				if (!JMSReceiveFactory.isConnect()) {
					System.out.println("�Ѿ��˳����յȴ���");
					break;
				}
			}
			JMSReceiveFactory.getInstance().reConnect();
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
				System.out.println(mess);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("consumer�Ѿ����ر�...");
			try {
				if (consumer != null){
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
	
	public static void main(String[] args) {
		ReceiverDemo demo = new ReceiverDemo("topic_fs_donghua_all_nr");
		demo.receive();
	}
	
}
