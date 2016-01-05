package com.afunms.serial.util;

//3. ReadSerial
//ReadSerial��һ���̣߳������ϵĴ�ָ���Ĵ��ڶ�ȡ���ݲ������ŵ��������С� 
//
//public ReadSerial(SerialBuffer SB, InputStream Port)
//����������һ��ReadSerial���̣�����SBָ����Ŵ������ݵĻ�����������Portָ���Ӵ��������յ���������
//
//public void run()
//ReadSerial���̵��������������ϵĴ�ָ���Ĵ��ڶ�ȡ���ݲ������ŵ��������С� 

import java.io.*;

/**
 * 
 * This class reads message from the specific serial port and save the message
 * to the serial buffer.
 * 
 */
public class SerialRead extends Thread {
	private SerialBuffer ComBuffer;
	private InputStream ComPort;

	/**
	 * 
	 * Constructor
	 * 
	 * @param SB
	 *            The buffer to save the incoming messages.
	 * @param Port
	 *            The InputStream from the specific serial port.
	 * 
	 */
	public SerialRead(SerialBuffer SB, InputStream Port) {
		ComBuffer = SB;
		ComPort = Port;
	}

	public void run() {
		int c;
		try {
			while (true) {
				c = ComPort.read();
				if(c==-1){
					break;
				}
				ComBuffer.PutChar(c);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
