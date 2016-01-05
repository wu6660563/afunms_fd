

/*
 * ����Ϊִ������ķ���������ִ�н�� , ���ִ��ʧ���򷵻�false . �ɹ�����true,
 * ͬʱ��ִ�еĽ�����н��������� temperature �� humidity
 */









package com.afunms.polling.task;

import com.afunms.serial.util.Parameters;
import com.afunms.serial.util.SerialBean;

public class TemperatureHumidityUtil {
	
	private Parameters parameters;
	
	private String command;
	
	private String result;
	
	/**
	 * the serialPortId of the serial to be used. COM1, COM2, etc.
	 */
	private String serialPortId;
	
	/**
	 * the address of the serial node to be used. 01 , 02 etc.
	 */
	private String address;
	
	/**
	 * the number of Baud Rate(For Exemaple: 9600)
	 */
	private int baudRate;
	
	/**
	 * the number of data bit 
	 */
	private int databits;
	
	/**
	 * the number of STOP bits
	 */
	private int stopbits;
	
	/**
	 * the number of Parity 
	 */
	private int parity;


	public TemperatureHumidityUtil() {
		parameters = new Parameters();
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * @param serialPortId
	 * @param address
	 * @param baudRate
	 * @param databits
	 * @param stopbits
	 * @param parity
	 */
	public TemperatureHumidityUtil(String serialPortId, String address,
			int baudRate, int databits, int stopbits, int parity) {
		initialize(serialPortId, address, baudRate, databits, stopbits, parity);
	}

	public static void main(String[] args){
		TemperatureHumidityUtil temperatureHumidityUtil =new TemperatureHumidityUtil();
		temperatureHumidityUtil.initialize("COM1","01",9600,7,2,0);
		temperatureHumidityUtil.execute();
	}
	
	/**
	 * Initialization, configuration parameters of temperature and humidity sensors: 
	 * @param serialPortId
	 * @param address
	 * @param baudRate
	 * @param databits
	 * @param stopbits
	 * @param parity
	 */
	public void initialize(String serialPortId, String address,
			int baudRate, int databits, int stopbits, int parity){
		this.serialPortId = serialPortId;
		this.address = address;
		this.baudRate = baudRate;
		this.databits = databits;
		this.stopbits = stopbits;
		this.parity = parity;
		parameters.setSerialPortId(serialPortId);
		parameters.setBaudRate(baudRate);
		parameters.setDatabits(databits);
		parameters.setStopbits(stopbits);
		parameters.setParity(parity);
		result="";
	}
	
	/**
	 * 
	 * This function is to execute the command, and returns the execute of results,
	 * If it fails, return false.
	 * Returns true if successful
	 * The same time the implementation of the results of analysis, 
	 * and assigned to this.temperature and this.humidity 
	 * 
	 * 
	 * execute
	 * @return
	 */
	public boolean execute(){
		
		// ʵ��������ͨ��
		SerialBean serialBean = null;
		try {
			serialBean = new SerialBean(parameters);
			
			// ��ʼ������ͨ�� �������-1��˵��ʧ�� 
			// Initialization serial communication , if return -1 is failed 
			int init = serialBean.initialize();
			if(init == -1){
				System.out.println("Initialization Error.Check initialization parameters.");
				return false;
			}
			
			// ���ݵ�ַ , �Լ���С��ַ������ַ ��������
			// According to address, and the minimum address and maximum address to create a command
			command = createCommand(address, "01", "05");
			
			for(int i = 0; i < 5 ; i++){
				// ִ��д���� ѭ��ִ�� 5 ��
				// execute write command , Loop 5 times
				serialBean.writeMsg(command);
				try {
					// �ȴ� 500ms �ô��ڻ�������ȡ���ڷ��ص�����
					// To wait for 500 milliseconds to get data from serial port into buffer
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
				// �ӻ������ж�ȡ���е�����
				// read from the buffer all data
				String tempResult = serialBean.readMsg();
				
				if(tempResult!=null&&result.length()<=tempResult.length()){
					// ȡ����ַ���Ϊ�����
					// The final result is  the longest string 
					result = tempResult;
				}
				try {
					// �ȴ� 500ms ִ���´�
					// wait 500 ms
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			// �رմ���
			// close port
			serialBean.closePort();
		}
		
		if(result != null && result.length() >=15){
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * To get Temperature
	 * ��ȡ�¶�
	 * @return
	 */
	public String getTemperature(){
		if(result != null && result.length() >= 19){
			// �����¶�
			int temp1 = Integer.parseInt(result.substring(11, 13), 16);
			int temp2 = Integer.parseInt(result.substring(13, 15), 16);
			double temperature= (( temp1&(0x07) )* 256 + temp2 ) * 0.0625;
			return temperature + "";
		}
		return null ;
		
	}
	
	/**
	 * 
	 * To get Humidity
	 * ��ȡʪ��
	 * @return
	 */
	public String getHumidity(){
		if(result != null && result.length() >= 19){
			// ����ʪ��
			int hum1 = Integer.parseInt(result.substring(15, 17), 16);
			int hum2 = Integer.parseInt(result.substring(17, 19), 16);
			double humidity= (hum1* 256 + hum2 ) / 10.0;
			return humidity + "";
		}
		return null;
	}
	
	/**
	 * 
	 * create command
	 * 
	 * ��������
	 * 
	 * @param address
	 * @param minAddress
	 * @param maxAddress
	 * @return
	 */
	private String createCommand(String address ,String minAddress ,String maxAddress){
		String tempCommand = ":" + address + "040080" + minAddress + maxAddress;
		int lrc = 0;
		// ����lrc
		lrc = Integer.parseInt(address , 16) + Integer.parseInt("04" , 16) 
			+ Integer.parseInt("80" , 16) + Integer.parseInt(minAddress , 16) 
			+ Integer.parseInt(maxAddress , 16) ;
		// ȡ����ת����16���Ƶ��ַ���
		String templrc = Integer.toHexString( ~lrc );
		// ȥ�����λΪ��Ч���
		templrc = templrc.substring(templrc.length()-2, templrc.length());
		tempCommand = tempCommand +Integer.toHexString( Integer.parseInt(templrc,16) + 1 );
		return tempCommand + '\r' + '\n';
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
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}


	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
}
