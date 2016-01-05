package com.afunms.serial.util;

//2. SerialBuffer 
//
//SerialBuffer��������Ĵ��ڻ������������������û�������д�����ݺʹӸû������ж�ȡ��������Ҫ�ĺ����� 
//
//public synchronized String GetMsg(int Length)
//�������Ӵ���(������)�ж�ȡʣ�����ݵ�ָ�����ȵ�һ���ַ���������Lengthָ���������ַ����ĳ��ȡ�
//
//public synchronized String GetMsg()
//�������Ӵ���(������)�ж�ȡ���е��ַ�����
//
//public synchronized void PutChar(int c)
//�����������ڻ�������д��һ���ַ�������c ����Ҫд����ַ���
//
//����������д�����ݻ����Ǵӻ�������ȡ���ݵ�ʱ�򣬱��뱣֤���ݵ�ͬ����
//���GetMsg��PutChar������������Ϊsynchronized���ھ���ʵ���в�ȡ��ʩʵ�ֵ����ݵ�ͬ���� 

/**
 * 
 * @author nielin
 * 
 * created on 2010-01-28
 * 
 * This class implements the buffer area to store incoming data from the serial
 * port.
 * 
 */
public class SerialBuffer {
	/**
	 * Store the current need to return the data.
	 */
	private String currentMsg;
	
	/**
	 * Store all incoming data from the serial port.
	 */
	private String contentMsg;
	
	/**
	 * Store the data that have not yet been returned.
	 */
	private String lastMsg;
	
	/**
	 * Store temporary data.
	 */
	private String tempContentMsg;
	
	/**
	 * Length of the data users need to. Default lengthNeeded = 1.
	 */
	private int lengthNeeded = 1;
	
	/**
	 * Whether to return data. False is not return data , then go to the thread to wait.
	 */
	private boolean available = false;
	
	

	/**
	 * 
	 * Constructors
	 * 
	 */
	public SerialBuffer() {
		setContentMsg("");
		setCurrentMsg("");
		setLastMsg("");
		setTempContentMsg("");
		setLengthNeeded(1);
		setAvailable(false);
	}

	/**
	 * 
	 * Constructors
	 * 
	 * @param currentMsg
	 * @param contentMsg
	 * @param lastMsg
	 * @param tempContentMsg
	 * @param lengthNeeded
	 * @param available
	 */
	public SerialBuffer(String currentMsg, String contentMsg, String lastMsg,
			String tempContentMsg, int lengthNeeded, boolean available) {
		this.currentMsg = currentMsg;
		this.contentMsg = contentMsg;
		this.lastMsg = lastMsg;
		this.tempContentMsg = tempContentMsg;
		this.lengthNeeded = lengthNeeded;
		this.available = available;
	}

	/**
	 * @return the currentMsg
	 */
	public String getCurrentMsg() {
		return currentMsg;
	}

	/**
	 * @param currentMsg the currentMsg to set
	 */
	public void setCurrentMsg(String currentMsg) {
		this.currentMsg = currentMsg;
	}

	/**
	 * @return the contentMsg
	 */
	public String getContentMsg() {
		return contentMsg;
	}

	/**
	 * @param contentMsg the contentMsg to set
	 */
	public void setContentMsg(String contentMsg) {
		this.contentMsg = contentMsg;
	}

	/**
	 * @return the lastMsg
	 */
	public String getLastMsg() {
		return lastMsg;
	}

	/**
	 * @param lastMsg the lastMsg to set
	 */
	public void setLastMsg(String lastMsg) {
		this.lastMsg = lastMsg;
	}

	/**
	 * @return the tempContentMsg
	 */
	public String getTempContentMsg() {
		return tempContentMsg;
	}

	/**
	 * @param tempContentMsg the tempContentMsg to set
	 */
	public void setTempContentMsg(String tempContentMsg) {
		this.tempContentMsg = tempContentMsg;
	}

	/**
	 * @return the lengthNeeded
	 */
	public int getLengthNeeded() {
		return lengthNeeded;
	}

	/**
	 * @param lengthNeeded the lengthNeeded to set
	 */
	public void setLengthNeeded(int lengthNeeded) {
		this.lengthNeeded = lengthNeeded;
	}

	/**
	 * @return the available
	 */
	public boolean isAvailable() {
		return available;
	}

	/**
	 * @param available the available to set
	 */
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	/**
	 * 
	 * This function returns a string with a certain length from the incomin
	 * messages.
	 * 
	 * @param Length
	 *            The length of the string to be returned.
	 * 
	 */
	public synchronized String GetMsg(int Length) {
		lengthNeeded = Length;
		notifyAll();
		if (lengthNeeded > lastMsg.length()) {
			available = false;
			while (available == false) {
				try {
					wait();
				} catch (InterruptedException e) {
				}
			}
		}
		currentMsg = lastMsg.substring(0, lengthNeeded);
		tempContentMsg = lastMsg.substring(lengthNeeded);
		lastMsg = tempContentMsg;
		lengthNeeded = 1;
		notifyAll();
		return currentMsg;
	}
	
	/**
	 * 
	 * This function returns a string with all data from the incomin
	 * messages.
	 * 
	 */
	public synchronized String GetMsg() {
		currentMsg = "";
		currentMsg = contentMsg ; 
		contentMsg = "";
		notifyAll();
		return currentMsg;
	}

	/**
	 * 
	 * This function stores a character captured from the serial port to the
	 * buffer area.
	 * 
	 * @param t
	 *            The char value of the character to be stored.
	 * 
	 */
	public synchronized void PutChar(int c) {
		Character d = new Character((char) c);
		contentMsg = contentMsg.concat(d.toString());
		lastMsg = lastMsg.concat(d.toString());
		if (lengthNeeded < lastMsg.length()) {
			available = true;
		}
		notifyAll();
	}
	
	/**
	 * clear Buffer
	 * @return
	 */
	public synchronized boolean clearBuffer() {
			setContentMsg("");
			setCurrentMsg("");
			setLastMsg("");
			setTempContentMsg("");
		return true;
	}

	
}
