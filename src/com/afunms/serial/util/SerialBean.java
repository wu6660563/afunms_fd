//
// Created on 2010-01-28
//
//  Javaʵ�ִ��ڶ�дͨѶ- -                                     
//
//1. SerialBean
//SerialBean�Ǳ����������Ӧ�ó���Ľӿڡ�������ж�����SerialBean�Ĺ��췽���Լ���ʼ�����ڣ��Ӵ��ڶ�ȡ���ݣ�������д�������Լ��رմ��ڵĺ���������������£� 
//
//
//public int Initialize()
//��������ʼ����ָ���Ĵ��ڲ����س�ʼ������������ʼ���ɹ�����1�����򷵻�-1��
//��ʼ���Ľ���Ǹô��ڱ�SerialBean��ռ��ʹ�ã�
//������ڱ��ɹ���ʼ�������һ���̶߳�ȡ�Ӵ��ڴ�������ݲ����䱣���ڻ������С�
//
//public String ReadPort(int Length)
//�������Ӵ���(������)�ж�ȡָ�����ȵ�һ���ַ���������Lengthָ���������ַ����ĳ��ȡ�
//
//public String ReadPort()
//�������Ӵ���(������)�ж�ȡ���е��ַ�����
//
//
//public void WritePort(String Msg)
//�������򴮿ڷ���һ���ַ���������Msg����Ҫ���͵��ַ�����
//
//public void ClosePort()
//������ֹͣ���ڼ����̲��رմ��ڡ� 
//SerialBean��Դ�������£� 

package com.afunms.serial.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.comm.CommPortIdentifier;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.UnsupportedCommOperationException;
/**
 * 
 * @author nielin
 * 
 * created on 2010-01-28
 * 
 * This bean provides some basic functions to implement full dulplex information
 * exchange through the srial port.
 * 
 */
public class SerialBean
{
	/**
	 * the serialPortId of the serial to be used. COM1, COM2, etc.
	 */
	private String serialPortId;
	
	/**
	 * the number of Baud Rate(For Exemaple: 9600)
	 */
	private int baudRate;
	
	/**
	 * the number of data bit 
	 */
	private int databits;
	
	/**
	 * the number of stop bits
	 */
	private int stopbits;
	
	/**
	 * the number of Parity 
	 */
	private int parity;
	
	/**
	 * the parameters of this SerialBean. The parameters contains serialPortId , baudRate ,
	 * databits , stopbits , parity.
	 */
	private Parameters parameters;
	
	/**
	 * Communications port management.
	 * This property is for internal properties, 
	 * temporary does not provide get () and set () method, if there is a need to increase
	 */
	private CommPortIdentifier commPortIdentifier;
	
	/**
	 * the Serial port of RS-232 
	 */
	private SerialPort serialPort;
	
	/**
	 * input stream from the communications port
	 */
	private InputStream inputStream;
	
	/**
	 * output stream from the communications port
	 */
	private OutputStream outputStream;
	
	/**
	 * Serial data buffer for storing read from serial port of the data
	 */
	private SerialBuffer serialBuffer;
	
	private SerialRead serailRead;
	/**
	 * 
	 * Constructor
	 * 
	 */
	public SerialBean(){
		
	}
	
	/**
	 * 
	 * Constructor
	 * 
	 * @param serialPortId ,serialPortId, databits, stopbits, parity
	 * 
	 * 
	 */
	public SerialBean(String serialPortId , int baudRate , int databits, int stopbits, int parity){
		setSerialPortId(serialPortId);
		setBaudRate(baudRate);
		setDatabits(databits);
		setStopbits(stopbits);
		setParity(parity);
		serialBuffer = new SerialBuffer();
	}
	
	/**
	 * 
	 * Constructor
	 * 
	 * @param parameters
	 * 			the parameters of this SerialBean. The parameters contains serialPortId , baudRate ,
	 * 			databits , stopbits , parity.
	 * 
	 */
	public SerialBean(Parameters parameters){
		this.parameters = parameters;
		setSerialPortId(parameters.getSerialPortId());
		setBaudRate(parameters.getBaudRate());
		setDatabits(parameters.getDatabits());
		setStopbits(parameters.getStopbits());
		setParity(parameters.getParity());
		serialBuffer = new SerialBuffer();
	}
	
	/**
	 * @return the serialPortId
	 */
	public String getSerialPortId() {
		return serialPortId;
	}
	
	/**
	 * @param serialPortId the serialPortId to set
	 */
	public void setSerialPortId(String serialPortId) {
		this.serialPortId = serialPortId;
	}
	
	/**
	 * @return the baudRate
	 */
	public int getBaudRate() {
		return baudRate;
	}
	
	/**
	 * @param baudRate the baudRate to set
	 */
	public void setBaudRate(int baudRate) {
		this.baudRate = baudRate;
	}
	
	/**
	 * @return the databits
	 */
	public int getDatabits() {
		return databits;
	}
	
	/**
	 * @param databits the databits to set
	 */
	public void setDatabits(int databits) {
		this.databits = databits;
	}
	
	/**
	 * @return the stopbits
	 */
	public int getStopbits() {
		return stopbits;
	}
	
	/**
	 * @param stopbits the stopbits to set
	 */
	public void setStopbits(int stopbits) {
		this.stopbits = stopbits;
	}
	
	/**
	 * @return the parity
	 */
	public int getParity() {
		return parity;
	}
	
	/**
	 * @param parity the parity to set
	 */
	public void setParity(int parity) {
		this.parity = parity;
	}
	
	/**
	 * @return the parameters
	 */
	public Parameters getParameters() {
		return parameters;
	}
	
	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * @return the serialBuffer
	 */
	public SerialBuffer getSerialBuffer() {
		return serialBuffer;
	}

	/**
	 * @param serialBuffer the serialBuffer to set
	 */
	public void setSerialBuffer(SerialBuffer serialBuffer) {
		this.serialBuffer = serialBuffer;
	}
	
	/**
	 * 
	 * This function initializes the specified serial port and returns the result to initialize. 
	 * If the initialization successful return 1, otherwise returns -1.
	 * The result is that the serial port initialization has been SerialBean exclusive use,
	 * If the serial port is successfully initialized, 
	 * then open a thread to read incoming data from the serial port and save it in the buffer.
	 * 
	 * @author nielin
	 * create on 2010-01-28
	 * 
	 * @return
	 */
	public int initialize(){
		int InitSuccess = 1;
		int InitFail    = -1;
		try {
			commPortIdentifier = CommPortIdentifier.getPortIdentifier(serialPortId);
			try {
				try{
					//serialPort.close();
				}catch(Exception e){
					e.printStackTrace();
				}
				serialPort = (SerialPort)commPortIdentifier.open("Serial_Sensors", 2000);
			} catch (PortInUseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return InitFail;
			}
			// Initialize the communication parameters to 
			// baudRate(For example 9600), 
			// databits(For example serialPort.DATABITS_8 . --5,6,7,8--), 
			// stopbits(For example serialPort.STOPBITS_1.  --1,1.5,2),
			// parity  (For example serialPort.PARITY_NONE  --PARITY_NONE , PARITY_EVEN  , PARITY_MARK
			//                                                PARITY_ODD  , PARITY_SPACE  ).
			try {
				serialPort.setSerialPortParams(baudRate, databits, stopbits, parity);
				
			}catch (UnsupportedCommOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return InitFail;
			}
			// Use InputStream in to read from the serial port, and
			// OutputStream
			// out to write to the serial port.
			try {
				inputStream  =  serialPort.getInputStream();
				outputStream =  serialPort.getOutputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return InitFail;
			}
			serailRead = new SerialRead(serialBuffer, inputStream);
			serailRead.start();
		} catch (NoSuchPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return InitFail;
		}
		return InitSuccess;
	}
	
	/**
	 * 
	 * This function sends a string to the serial port. 
	 * Msg parameter is the string that need to be sent.
	 * 
	 * @author nielin
	 * create on 2010-01-28
	 * 
	 * @param msg
	 * @return
	 */
	public int writeMsg(String msg){
		int writeSuccess = -1;
		int writeFail    = -1;
		try{
			for (int i = 0; i < msg.length(); i++){
				outputStream.write(msg.charAt(i));
			}
		} catch (IOException e)  {
			e.printStackTrace();
			return writeFail;
		}
		return writeSuccess;
	}
	
	/**
	 * This function is from the serial port (buffer) to read the string.
	 * 
	 * @author nielin
	 * create on 2010-01-28
	 * 
	 * 
	 * @return
	 */
	public String readMsg(){
		String msg = "";
		msg = serialBuffer.GetMsg();
		return msg;
	}
	
	/**
	 * This function is from the serial port (buffer) to read a specified length of a string. 
	 * Length parameter specifies the length of the string returned.
	 * 
	 * @author nielin
	 * create on 2010-01-28
	 * 
	 * @param length
	 * @return
	 */
	public String readMsg(int length){
		String msg = "";
		msg = serialBuffer.GetMsg(length);
		return msg;
	}
	
	/**
	 * 
	 * This function closes the serial port in use.
	 * 
	 * @author nielin
	 * create on 2010-01-28
	 * 
	 */
	@SuppressWarnings("deprecation")
	public void closePort(){
		try {
//			try {
//				inputStream.close();
//				try {
//				} catch (Error e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			//�жϴ��߳�
//			try{
//				serailRead.interrupt();
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//			inputStream.close();
//			outputStream.close();
			serialPort.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}








 
