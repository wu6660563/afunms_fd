/*
 * Created on 2005-3-2
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.afunms.common.util;

import java.util.Hashtable;

import org.snmp4j.smi.OID;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SnmpMibConstants {
	
	/*
	 * DB2����
	 */
	public static final String[] DB2_Appl_status={"SQLM_INIT","SQLM_CONNECTPEND","SQLM_CONNECTED",
						"SQLM_UOWEXEC","SQLM_UOWWAIT","SQLM_LOCKWAIT","SQLM_COMMIT_ACT","SQLM_ROLLBACK_ACT",
						"SQLM_RECOMP","SQLM_COMP","SQLM_INTR","SQLM_DISCONNECTPEND","SQLM_TPREP",
						"SQLM_THCOMT","SQLM_THABRT","SQLM_TEND","SQLM_CREATE_DB","SQLM_RESTART",
						"SQLM_RESTORE","SQLM_BACKUP","SQLM_LOAD","SQLM_UNLOAD","SQLM_IOERROR_WAIT",
						"SQLM_QUIESCE_TABLESPACE","SQLM_WAITFOR_REMOTE","SQLM_REMOTE_RQST","SQLM_DECOUPLED",
						"SQLM_ROLLBACK_TO_SAVEPOINT"
						};
	
	public static final String[] DB2_Client_platform={"SQLM_PLATFORM_UNKNOWN","SQLM_PLATFORM_OS2","SQLM_PLATFORM_DOS",
		"SQLM_PLATFORM_WINDOWS","SQLM_PLATFORM_AIX","SQLM_PLATFORM_NT","SQLM_PLATFORM_HP","SQLM_PLATFORM_SUN",
		"SQLM_PLATFORM_MVS_DRDA","SQLM_PLATFORM_AS400_DRDA","SQLM_PLATFORM_VM_DRDA","SQLM_PLATFORM_VSE_DRDA","SQLM_PLATFORM_UNKNOWN_DRDA",
		"SQLM_PLATFORM_SNI","SQLM_PLATFORM_MAC","SQLM_PLATFORM_WINDOWS95","SQLM_PLATFORM_SCO","SQLM_PLATFORM_SGI",
		"SQLM_PLATFORM_LINUX","SQLM_PLATFORM_DYNIX","SQLM_PLATFORM_AIX64","SQLM_PLATFORM_SUN64","SQLM_PLATFORM_HP64",
		"SQLM_PLATFORM_NT64","SQLM_PLATFORM_LINUX390","SQLM_PLATFORM_LINUXZ64","SQLM_PLATFORM_LINUXIA64",
		"SQLM_PLATFORM_LINUXPPC","SQLM_PLATFORM_LINUXPPC64","SQLM_PLATFORM_OS390","SQLM_PLATFORM_LINUXX8664",
		"SQLM_PLATFORM_HPIA","SQLM_PLATFORM_HPIA64","SQLM_PLATFORM_SUNX86","SQLM_PLATFORM_SUNX8664"
		};
	public static final String[] DB2_Client_protocol={"SQL_PROTOCOL_APPC","SQL_PROTOCOL_NETB","SQL_PROTOCOL_APPN",
		"SQL_PROTOCOL_TCPIP","SQL_PROTOCOL_CPIC","SQL_PROTOCOL_IPXSPX","SQL_PROTOCOL_LOCAL","SQL_PROTOCOL_NPIPE"
		};

	public static final String[] DB2_db_status={"SQLM_DB_ACTIVE","SQLM_DB_QUIESCE_PEND","SQLM_DB_QUIESCED",
		"SQLM_DB_ROLLFWD"
		};
	
	public static final String[] NetWorkMibInterfaceDesc0={"index","ifDescr","ifType","ifMtu",
											"ifSpeed","ifPhysAddress","ifAdminStatus","ifOperStatus",
											"ifLastChange","ifname"
											};
	public static final String[]  NetWorkMibInterfaceUnit0={"","","","�ֽ�",
										"KB/��","","","",
										"",""
									};
	public static final String[] NetWorkMibInterfaceChname0={"�˿�����","����","����","������ݰ�",
											"ÿ���ֽ���(M)","�˿�Mac��ַ","Ԥ��״̬","��ǰ״̬",
											"ϵͳsysUpTime����","�˿�����2"
									
													};
	public static final int[]  NetWorkMibInterfaceScale0={0,0,0,1,
											1000,0,0,0,
											0,0
										};
	public static final String[] NetWorkMibInterfaceDesc1={"index","ifInOctets","ifInUcastPkts","ifInNUcastPkts",
											"ifInDiscards","ifInErrors","ifInUnknownProtos","ifOutOctets",
											"ifOutUcastPkts","ifOutNUcastPkts","ifOutDiscards","ifOutErrors",
											"ifOutQLen","ifSpecific"
									};
	public static final String[] NetWorkMibInterfaceChname1={"�˿�����","���յ��ֽ�","���������ݰ�","�ǵ��������ݰ�",
											"�����������ݰ�","��վ�������ݰ�","��վ��֪�������ݰ�","������ֽ�",
											"���������ݰ�","�ǵ��������ݰ�","��վ�����������ݰ�","��վ����ʧ�ܵ����ݰ�",
											"�����Ϣ�����еĳ���","Mib�Զ˿ڵ�˵��"
									};
	public static final String[] NetWorkMibInterfaceUnit1={"KB","","",
											"","","KB",
											"","","","",
											"",""
									};
	public static final int[] NetWorkMibInterfaceScale1={1024,0,1,
											0,0,1024,
											0,0,0,0,
											0,0
									};
	public static final String[] NetWorkMibSystemDesc={"sysDescr","sysUpTime","sysContact","sysName","sysLocation","sysServices"
														};
	public static final String[] NetWorkMibSystemChname={"����","����ʱ��","��ϵ��","�豸����","�豸λ��","��������"
																		};

	public static final String[] UPSMibInDesc={"JLSRXDYAB","JLSRXDYBC","JLSRXDYCA","AXSRDL","BXSRDL",
												    "CXSRDL","SRPL","AXSRGLYS","BXSRGLYS","CXSRGLYS"};
	public static final String[] UPSMibInChname={"���������ߵ�ѹAB","���������ߵ�ѹBC","���������ߵ�ѹCA","A���������","B���������",
													"C���������","����Ƶ��","A�����빦������","B�����빦������","C�����빦������"};

	public static final String[] UPSMibSystemDesc={"ZLSRDY","DCDL","DCHBSJ","DCWD","HJWD"};
	public static final String[] UPSMibSystemChname={"ֱ�������ѹ","��ص���","��غ�ʱ��","����¶�","�����¶�"};
	
	public static final String[] UPSMibBranchDesc={"AXPLDY","BXPLDY","CXPLDY","PLPL"};
	public static final String[] UPSMibBranchChname={"A����·��ѹ","B����·��ѹ","C����·��ѹ","��·Ƶ��"};
	
	public static final String[] UPSMibOutDesc={"JLSCXDYA","JLSCXDYB","JLSCXDYC","AXSCDL","BXSCDL",
	    										"CXSCDL","SCPL","AXSCGLYS","BXSCGLYS","CXSCGLYS"};
	public static final String[] UPSMibOutChname={"��������ߵ�ѹA","��������ߵ�ѹB","��������ߵ�ѹC","A���������","B���������",
												"C���������","���Ƶ��","A�������������","B�������������","C�������������"};
	
	public static final String[] UPSMibSelfOutPowerDesc={"BJAXSCYGGL","BJBXSCYGGL","BJCXSCYGGL",
												"BJAXSCSZGL","BJBXSCSZGL","BJCXSCSZGL",
												"BJAXSCWGGL","BJBXSCWGGL","BJCXSCWGGL"};
	public static final String[] UPSMibSelfOutPowerChname={"����A������й�����","����B������й�����","����C������й�����",
												"����A��������ڹ���","����B��������ڹ���","����C��������ڹ���",
												"����A������޹�����","����B������޹�����","����C������޹�����"};
	
	public static final String[] UPSMibSelfOutPerDesc={"BJAXSCFZBFB","BJBXSCFZBFB","BJCXSCFZBFB",
												"BJAXSCFZB","BJBXSCFZB","BJCXSCFZB"};
	public static final String[] UPSMibSelfOutPerChname={"����A��������ذٷֱ�","����B��������ذٷֱ�","����C��������ذٷֱ�",
												"����A�������ֵ��","����B�������ֵ��","����C�������ֵ��"};
	public static final Hashtable allUps = new Hashtable();
					
	  static {
		  allUps.put("BJAXSCFZBFB", "����A��������ذٷֱ�");
		  allUps.put("BJBXSCFZBFB", "����B��������ذٷֱ�");
		  allUps.put("BJCXSCFZBFB", "����C��������ذٷֱ�");
		  allUps.put("BJAXSCFZB", "����A�������ֵ��");
		  allUps.put("BJBXSCFZB", "����B�������ֵ��");
		  allUps.put("BJCXSCFZB", "����C�������ֵ��");
		  allUps.put("ZLSRDY", "ֱ�������ѹ");
		  allUps.put("DCDL", "��ص���");
		  allUps.put("DCHBSJ", "��غ�ʱ��");
		  allUps.put("DCWD", "����¶�");
		  allUps.put("HJWD", "�����¶�");
		  
		  allUps.put("BJAXSCYGGL", "����A������й�����");
		  allUps.put("BJBXSCYGGL", "����B������й�����");
		  allUps.put("BJCXSCYGGL", "����C������й�����");
		  allUps.put("BJAXSCSZGL", "����A��������ڹ���");
		  allUps.put("BJBXSCSZGL", "����B��������ڹ���");
		  allUps.put("BJCXSCSZGL", "����C��������ڹ���");
		  allUps.put("BJAXSCWGGL", "����A������޹�����");
		  allUps.put("BJBXSCWGGL", "����B������޹�����");
		  allUps.put("BJCXSCWGGL", "����C������޹�����");

		  allUps.put("JLSCXDYA", "��������ߵ�ѹA");
		  allUps.put("JLSCXDYB", "��������ߵ�ѹB");
		  allUps.put("JLSCXDYC", "��������ߵ�ѹC");
		  allUps.put("AXSCDL", "A���������");
		  allUps.put("BXSCDL", "B���������");
		  allUps.put("CXSCDL", "C���������");
		  allUps.put("SCPL", "���Ƶ��");
		  allUps.put("AXSCGLYS", "A�������������");
		  allUps.put("BXSCGLYS", "B�������������");
		  allUps.put("CXSCGLYS", "C�������������");
		  
		  allUps.put("AXPLDY", "A����·��ѹ");
		  allUps.put("BXPLDY", "B����·��ѹ");
		  allUps.put("CXPLDY", "C����·��ѹ");
		  allUps.put("PLPL", "��·Ƶ��");		  
		  
		  allUps.put("JLSRXDYAB", "���������ߵ�ѹAB");
		  allUps.put("JLSRXDYBC", "���������ߵ�ѹBC");
		  allUps.put("JLSRXDYCA", "���������ߵ�ѹCA");
		  allUps.put("AXSRDL", "A���������");
		  allUps.put("BXSRDL", "B���������");
		  allUps.put("CXSRDL", "C���������");
		  allUps.put("SRPL", "����Ƶ��");
		  allUps.put("AXSRGLYS", "A�����빦������");
		  allUps.put("BXSRGLYS", "B�����빦������");
		  allUps.put("CXSRGLYS", "C�����빦������");

	  }
	
		public static final Hashtable allUpsEvent = new Hashtable();
		
		  static {			  			  			  			  
			  allUpsEvent.put("Mains Frequency Recovered", "��·Ƶ�ʻָ�����");
			  allUpsEvent.put("Input Disconnect Closed", "����տ��պ�");
			  allUpsEvent.put("Mains Voltage Recovered", "��·��ѹ�ָ�����");
			  allUpsEvent.put("UPS Inverter On", "UPS����");
			  allUpsEvent.put("UPS in Normal Mode", "UPS��·��乩��");
			  allUpsEvent.put("Battery Float Charging", "��ظ���");
			  allUpsEvent.put("System in Normal Mode", "ϵͳ��·��乩��");
			  allUpsEvent.put("Battery Not Charging", "���ֹͣ���");
			  allUpsEvent.put("UPS Battery Testing", "����Լ���");
			  allUpsEvent.put("Battery Self Test Completed", "����Լ����");
			  allUpsEvent.put("Battery Boost Charging", "��ؾ���");			  
			  allUpsEvent.put("Inverter Asynchronous", "�������ͬ��");			  	
			  allUpsEvent.put("Bypass Abnormal", "��·����");
			  allUpsEvent.put("UPS in Battery Mode", "UPS�����乩��");
			  allUpsEvent.put("Mains Voltage Abnormal", "��·����");
			  allUpsEvent.put("Mains Frequency Abnormal", "��·Ƶ�ʳ���");
			  allUpsEvent.put("System in Battery Mode", "ϵͳ�����乩��");
			  allUpsEvent.put("Bypass Recovered", "��·�ָ�����");
			  allUpsEvent.put("Inverter Resumed to be Synchr", "�����ͬ���ָ�");
			  allUpsEvent.put("Battery Low Pre-warning", "��ص�ѹԤ�澯");
			  allUpsEvent.put("UPS Shutdown", "UPS������");
			  allUpsEvent.put("UPS Inverter Off", "UPS�ػ�");
			  allUpsEvent.put("System in Bypass Mode", "ϵͳ��·����");
			  allUpsEvent.put("System Shutdown", "ϵͳ������");
			  allUpsEvent.put("Input Disconnect Open", "����տ��Ͽ�");			  			  
			  allUpsEvent.put("Output Disconnect Open", "����տ��Ͽ�");
			  allUpsEvent.put("Bypass Disconnect Open", "�Զ���·�տ��Ͽ�");
			  allUpsEvent.put("Bypass Disconnect Closed", "�Զ���·�տ��պ�");
			  allUpsEvent.put("Output Disconnect Closed", "����տ��պ�");
			  allUpsEvent.put("Parallel Communication Fail", "����ͨѶ����");			  
			  allUpsEvent.put("Parallel Communication Recove", "����ͨѶ��������");
			  allUpsEvent.put("UPS in Bypass Mode", "UPS��·����");
			  allUpsEvent.put("System Battery Low Fault", "ϵͳ���Ԥ�澯");
		  }
	
	public static final String[] TopsecMibSystemDesc=
			{"tosProductType","tosSysVersion","tosDeviceName","systemTime","currentConnections","cps"};
	public static final String[] TopsecMibSystemChname=
			{"��Ʒ����","�汾","�豸����","ϵͳʱ��","��ǰ������","ÿ����������"};
	
	
//	standard traps
   public static final OID coldStart =
	   new OID(new int[] { 1,3,6,1,6,3,1,1,5,1 });
   public static final OID warmStart =
	   new OID(new int[] { 1,3,6,1,6,3,1,1,5,2 });
   public static final OID authenticationFailure =
	   new OID(new int[] { 1,3,6,1,6,3,1,1,5,5 });
   public static final OID linkDown =
	 new OID(new int[] { 1,3,6,1,6,3,1,1,5,3 });
   public static final OID linkUp =
	 new OID(new int[] { 1,3,6,1,6,3,1,1,5,4 });
	 
//	mib������_mib����_mib����

	  //ϵͳʱ��
	  public static final String HOST_hrSystem_hrSystemDate =
		  "1.3.6.1.2.1.25.1.2.0";
	  //����ʱ��	
	  public static final String HOST_hrSystem_hrSystemUptime =
		  "1.3.6.1.2.1.25.1.1.0";
	  //ϵͳ�û���
	  public static final String HOST_hrSystem_hrSystemNumUsers =
		  "1.3.6.1.2.1.25.1.5.0";
	  //���н�����	
	  public static final String HOST_hrSystem_hrSystemProcesses =
		  "1.3.6.1.2.1.25.1.6.0";

	  //�г����ִ洢���ʹ�����
	  //����
	  public static final String TYPE_HOST_hrStorage_hrStorageOther =
		  "1.3.6.1.2.1.25.2.1.1";
	  //�ڴ�	
	  public static final String TYPE_HOST_hrStorage_hrStorageRam =
		  "1.3.6.1.2.1.25.2.1.2";
	  //�����ڴ�	
	  public static final String TYPE_HOST_hrStorage_hrStorageVirtualMemory =
		  "1.3.6.1.2.1.25.2.1.3";
	  //Ӳ��	
	  public static final String TYPE_HOST_hrStorage_hrStorageFixedDisk =
		  "1.3.6.1.2.1.25.2.1.4";
	  //���ƶ���
	  public static final String TYPE_HOST_hrStorage_hrStorageRemovableDisk =
		  "1.3.6.1.2.1.25.2.1.5";
	  //����	
	  public static final String TYPE_HOST_hrStorage_hrStorageFloppyDisk =
		  "1.3.6.1.2.1.25.2.1.6";
	  //����	
	  public static final String TYPE_HOST_hrStorage_hrStorageCompactDisc =
		  "1.3.6.1.2.1.25.2.1.7";
	  //RamDisk
	  public static final String TYPE_HOST_hrStorage_hrStorageRamDisk =
		  "1.3.6.1.2.1.25.2.1.8";

	  public static Hashtable TYPE_HOST_hrStorage = null;
	  static {
		  TYPE_HOST_hrStorage = new Hashtable();
		  TYPE_HOST_hrStorage.put(TYPE_HOST_hrStorage_hrStorageOther, "����");
		  TYPE_HOST_hrStorage.put(TYPE_HOST_hrStorage_hrStorageRam, "�ڴ�");
		  TYPE_HOST_hrStorage.put(
			  TYPE_HOST_hrStorage_hrStorageVirtualMemory,
			  "�����ڴ�");
		  TYPE_HOST_hrStorage.put(TYPE_HOST_hrStorage_hrStorageFixedDisk, "Ӳ��");
		  TYPE_HOST_hrStorage.put(
			  TYPE_HOST_hrStorage_hrStorageRemovableDisk,
			  "���ƶ���");
		  TYPE_HOST_hrStorage.put(TYPE_HOST_hrStorage_hrStorageFloppyDisk, "����");
		  TYPE_HOST_hrStorage.put(TYPE_HOST_hrStorage_hrStorageCompactDisc, "����");
		  TYPE_HOST_hrStorage.put(
			  TYPE_HOST_hrStorage_hrStorageRamDisk,
			  "RamDisk");
	  }
	  //�����ڴ�
	  public static final String HOST_hrStorage_hrMemorySize =
		  "1.3.6.1.2.1.25.2.2.0";

	  public static final String TABLE_HOST_hrStorage_hrStorageIndex =
		  "1.3.6.1.2.1.25.2.3.1.1";
	  public static final String TABLE_HOST_hrStorage_hrStorageType =
		  "1.3.6.1.2.1.25.2.3.1.2";
	  public static final String TABLE_HOST_hrStorage_hrStorageDescr =
		  "1.3.6.1.2.1.25.2.3.1.3";
	  //���䵥λ	
	  public static final String TABLE_HOST_hrStorage_hrStorageAllocationUnits =
		  "1.3.6.1.2.1.25.2.3.1.4";
	  //��С
	  public static final String TABLE_HOST_hrStorage_hrStorageSize =
		  "1.3.6.1.2.1.25.2.3.1.5";
	  //��ʹ��	
	  public static final String TABLE_HOST_hrStorage_hrStorageUsed =
		  "1.3.6.1.2.1.25.2.3.1.6";
	  //����ʧ��	
	  public static final String TABLE_HOST_hrStorage_hrStorageAllocationFailures =
		  "1.3.6.1.2.1.25.2.3.1.7";

	  //�����豸
	  public static final String TYPE_HOST_hrDevice_hrDeviceOther =
		  "1.3.6.1.2.1.25.3.1.1";
	  //δ֪�豸	
	  public static final String TYPE_HOST_hrDevice_hrDeviceUnknown =
		  "1.3.6.1.2.1.25.3.1.2";
	  //������	
	  public static final String TYPE_HOST_hrDevice_hrDeviceProcessor =
		  "1.3.6.1.2.1.25.3.1.3";
	  //����	
	  public static final String TYPE_HOST_hrDevice_hrDeviceNetwork =
		  "1.3.6.1.2.1.25.3.1.4";
	  //��ӡ��	
	  public static final String TYPE_HOST_hrDevice_hrDevicePrinter =
		  "1.3.6.1.2.1.25.3.1.5";
	  //�����豸	
	  public static final String TYPE_HOST_hrDevice_hrDeviceDiskStorage =
		  "1.3.6.1.2.1.25.3.1.6";
	  //��ʾ��	
	  public static final String TYPE_HOST_hrDevice_hrDeviceVideo =
		  "1.3.6.1.2.1.25.3.1.10";
	  //����	
	  public static final String TYPE_HOST_hrDevice_hrDeviceAudio =
		  "1.3.6.1.2.1.25.3.1.11";
	  //Э������
	  public static final String TYPE_HOST_hrDevice_hrDeviceCoprocessor =
		  "1.3.6.1.2.1.25.3.1.12";
	  //����	
	  public static final String TYPE_HOST_hrDevice_hrDeviceKeyboard =
		  "1.3.6.1.2.1.25.3.1.13";
	  //���ƽ����	
	  public static final String TYPE_HOST_hrDevice_hrDeviceModem =
		  "1.3.6.1.2.1.25.3.1.14";
	  //����	
	  public static final String TYPE_HOST_hrDevice_hrDeviceParallelPort =
		  "1.3.6.1.2.1.25.3.1.15";
	  //���	
	  public static final String TYPE_HOST_hrDevice_hrDevicePointing =
		  "1.3.6.1.2.1.25.3.1.16";
	  //����
	  public static final String TYPE_HOST_hrDevice_hrDeviceSerialPort =
		  "1.3.6.1.2.1.25.3.1.17";
	  //�Ŵ���	
	  public static final String TYPE_HOST_hrDevice_hrDeviceTape =
		  "1.3.6.1.2.1.25.3.1.18";
	  //ʱ��	
	  public static final String TYPE_HOST_hrDevice_hrDeviceClock =
		  "1.3.6.1.2.1.25.3.1.19";
	  //�����ڴ�	
	  public static final String TYPE_HOST_hrDevice_hrDeviceVolatileMemory =
		  "1.3.6.1.2.1.25.3.1.20";
	  //�����ڴ�	
	  public static final String TYPE_HOST_hrDevice_hrDeviceNonVolatileMemory =
		  "1.3.6.1.2.1.25.3.1.21";

	  public static Hashtable TYPE_HOST_hrDevice = null;
	  static {
		  TYPE_HOST_hrDevice = new Hashtable();
		  TYPE_HOST_hrDevice.put(TYPE_HOST_hrDevice_hrDeviceOther, "�����豸");
		  TYPE_HOST_hrDevice.put(TYPE_HOST_hrDevice_hrDeviceUnknown, "δ֪�豸");
		  TYPE_HOST_hrDevice.put(TYPE_HOST_hrDevice_hrDeviceProcessor, "������");
		  TYPE_HOST_hrDevice.put(TYPE_HOST_hrDevice_hrDeviceNetwork, "����");
		  TYPE_HOST_hrDevice.put(TYPE_HOST_hrDevice_hrDevicePrinter, "��ӡ��");
		  TYPE_HOST_hrDevice.put(TYPE_HOST_hrDevice_hrDeviceDiskStorage, "�����豸");
		  TYPE_HOST_hrDevice.put(TYPE_HOST_hrDevice_hrDeviceVideo, "��ʾ��");
		  TYPE_HOST_hrDevice.put(TYPE_HOST_hrDevice_hrDeviceAudio, "����");
		  TYPE_HOST_hrDevice.put(TYPE_HOST_hrDevice_hrDeviceCoprocessor, "Э������");
		  TYPE_HOST_hrDevice.put(TYPE_HOST_hrDevice_hrDeviceKeyboard, "����");
		  TYPE_HOST_hrDevice.put(TYPE_HOST_hrDevice_hrDeviceModem, "���ƽ����");
		  TYPE_HOST_hrDevice.put(TYPE_HOST_hrDevice_hrDeviceParallelPort, "����");
		  TYPE_HOST_hrDevice.put(TYPE_HOST_hrDevice_hrDevicePointing, "���");
		  TYPE_HOST_hrDevice.put(TYPE_HOST_hrDevice_hrDeviceSerialPort, "����");
		  TYPE_HOST_hrDevice.put(TYPE_HOST_hrDevice_hrDeviceTape, "�Ŵ���");
		  TYPE_HOST_hrDevice.put(TYPE_HOST_hrDevice_hrDeviceClock, "ʱ��");
		  TYPE_HOST_hrDevice.put(
			  TYPE_HOST_hrDevice_hrDeviceVolatileMemory,
			  "�����ڴ�");
		  TYPE_HOST_hrDevice.put(
			  TYPE_HOST_hrDevice_hrDeviceNonVolatileMemory,
			  "�����ڴ�");

	  }
	  public static Hashtable HOST_hrDevice_hrDeviceStatus = null;
	  static {
		  HOST_hrDevice_hrDeviceStatus = new Hashtable();
		  HOST_hrDevice_hrDeviceStatus.put("1", "δ֪");
		  HOST_hrDevice_hrDeviceStatus.put("2", "����");
		  HOST_hrDevice_hrDeviceStatus.put("3", "����");
		  HOST_hrDevice_hrDeviceStatus.put("4", "����");
		  HOST_hrDevice_hrDeviceStatus.put("5", "ֹͣ");
	  }
	  


	  //�����豸���
	  public static final String TABLE_HOST_hrDevice_hrDeviceIndex =
		  "1.3.6.1.2.1.25.3.2.1.1";
	  public static final String TABLE_HOST_hrDevice_hrDeviceType =
		  "1.3.6.1.2.1.25.3.2.1.2";
	  public static final String TABLE_HOST_hrDevice_hrDeviceDescr =
		  "1.3.6.1.2.1.25.3.2.1.3";
	  public static final String TABLE_HOST_hrDevice_hrDeviceID =
		  "1.3.6.1.2.1.25.3.2.1.4";
	  public static final String TABLE_HOST_hrDevice_hrDeviceStatus =
		  "1.3.6.1.2.1.25.3.2.1.5";
	  public static final String TABLE_HOST_hrDevice_hrDeviceErrors =
		  "1.3.6.1.2.1.25.3.2.1.6";

	  public static final String TABLE_HOST_hrDevice_hrProcessorFrwID =
		  "1.3.6.1.2.1.25.3.3.1.1";
	  public static final String TABLE_HOST_hrDevice_hrProcessorLoad =
		  "1.3.6.1.2.1.25.3.3.1.2";

	  //�������еĽ����б�
	  public static final String TABLE_HOST_hrSWRun_hrSWRunIndex =
		  "1.3.6.1.2.1.25.4.2.1.1";
	  //����
	  public static final String TABLE_HOST_hrSWRun_hrSWRunName =
		  "1.3.6.1.2.1.25.4.2.1.2";
	  public static final String TABLE_HOST_hrSWRun_hrSWRunID =
		  "1.3.6.1.2.1.25.4.2.1.3";
	  //����Ŀ¼	
	  public static final String TABLE_HOST_hrSWRun_hrSWRunPath =
		  "1.3.6.1.2.1.25.4.2.1.4";
	  //���в���	
	  public static final String TABLE_HOST_hrSWRunParameters =
		  "1.3.6.1.2.1.25.4.2.1.5";
	  //���	
	  public static final String TABLE_HOST_hrSWRun_hrSWRunType =
		  "1.3.6.1.2.1.25.4.2.1.6";
	  //״̬	
	  public static final String TABLE_HOST_hrSWRun_hrSWRunStatus =
		  "1.3.6.1.2.1.25.4.2.1.7";

	  public static final String TABLE_HOST_hrSWRunPerf_hrSWRunPerfCPU =
		  "1.3.6.1.2.1.25.5.1.1.1";
	  public static final String TABLE_HOST_hrSWRunPerf_hrSWRunPerfMem =
		  "1.3.6.1.2.1.25.5.1.1.2";
	  public static Hashtable HOST_hrSWRun_hrSWRunType = null;
	  static {
		  HOST_hrSWRun_hrSWRunType = new Hashtable();
		  HOST_hrSWRun_hrSWRunType.put("1", "δ֪");
		  HOST_hrSWRun_hrSWRunType.put("2", "����ϵͳ");
		  HOST_hrSWRun_hrSWRunType.put("3", "�豸����");
		  HOST_hrSWRun_hrSWRunType.put("4", "Ӧ�ó���");

	  }
	  public static Hashtable HOST_hrSWRun_hrSWRunStatus = null;
	  static {
		  HOST_hrSWRun_hrSWRunStatus = new Hashtable();
		  HOST_hrSWRun_hrSWRunStatus.put("1", "��������");
		  HOST_hrSWRun_hrSWRunStatus.put("2", "�ȴ�");
		  HOST_hrSWRun_hrSWRunStatus.put("3", "���еȴ����");
		  HOST_hrSWRun_hrSWRunStatus.put("4", "������");
	  }
	  //��װ������б�
	  public static final String TABLE_HOST_hrSWInstalled_hrSWInstalledIndex =
		  "1.3.6.1.2.1.25.6.3.1.1";
	  //����	
	  public static final String TABLE_HOST_hrSWInstalled_hrSWInstalledName =
		  "1.3.6.1.2.1.25.6.3.1.2";
	  public static final String TABLE_HOST_hrSWInstalled_hrSWInstalledID =
		  "1.3.6.1.2.1.25.6.3.1.3";
	  //���	
	  public static final String TABLE_HOST_hrSWInstalled_hrSWInstalledType =
		  "1.3.6.1.2.1.25.6.3.1.4";
	  //��װ����	
	  public static final String TABLE_HOST_hrSWInstalled_hrSWInstalledDate =
		  "1.3.6.1.2.1.25.6.3.1.5";
	  public static Hashtable HOST_hrSWInstalled_hrSWInstalledType = null;
	  static {
		  HOST_hrSWInstalled_hrSWInstalledType = new Hashtable();
		  HOST_hrSWInstalled_hrSWInstalledType.put("4", "Ӧ�ó���");
	  }

	  //��ǰϵͳ��������з���
	  public static final String TABLE_LANMANAGER_server_svSvcName =
		  "1.3.6.1.4.1.77.1.2.3.1.1";
	  public static final String TABLE_LANMANAGER_server_svSvcInstalledState =
		  "1.3.6.1.4.1.77.1.2.3.1.2";
	  public static final String TABLE_LANMANAGER_server_svSvcOperatingState =
		  "1.3.6.1.4.1.77.1.2.3.1.3";
	  public static final String TABLE_LANMANAGER_server_svSvcCanBeUninstalled =
		  "1.3.6.1.4.1.77.1.2.3.1.4";
	  public static final String TABLE_LANMANAGER_server_svSvcCanBePaused =
		  "1.3.6.1.4.1.77.1.2.3.1.5";

	  //��ע���û�	
	  public static final String TABLE_LANMANAGER_server_svUserName =
		  "1.3.6.1.4.1.77.1.2.25.1.1";

	  //�����ļ��С��ļ��й���Ҳ���ڰ�ȫ���������������ո����Ҳ������Ӧ�Ĵ���	
	  public static final String TABLE_LANMANAGER_server_svShareName =
		  "1.3.6.1.4.1.77.1.2.27.1.1";
	  public static final String TABLE_LANMANAGER_server_svSharePath =
		  "1.3.6.1.4.1.77.1.2.27.1.2";
	  public static final String TABLE_LANMANAGER_server_svShareComment =
		  "1.3.6.1.4.1.77.1.2.27.1.3";

	  public static final String RFC1213_interfaces_ifNumber =
		  "1.3.6.1.2.1.2.1.0";

	  public static final String TABLE_RFC1213_interfaces_ifIndex =
		  "1.3.6.1.2.1.2.2.1.1";
	  public static final String TABLE_RFC1213_interfaces_ifDescr =
		  "1.3.6.1.2.1.2.2.1.2";
	  public static final String TABLE_RFC1213_interfaces_ifType =
		  "1.3.6.1.2.1.2.2.1.3";

	  //����������ݰ�������
	  public static final String TABLE_RFC1213_interfaces_ifOutErrors =
		  "1.3.6.1.2.1.2.2.1.20";
	  //������������ݰ�������	
	  public static final String TABLE_RFC1213_interfaces_ifOutDiscards =
		  "1.3.6.1.2.1.2.2.1.19";
	  //����������ݰ�������	
	  public static final String TABLE_RFC1213_interfaces_ifInErrors =
		  "1.3.6.1.2.1.2.2.1.14";
	  //����δ֪Э���������	
	  public static final String TABLE_RFC1213_interfaces_ifInUnknownProtos =
		  "1.3.6.1.2.1.2.2.1.15";

	  public static final String TABLE_RFC1213_interfaces_ifOutUcastPkts =
		  "1.3.6.1.2.1.2.2.1.17";
	  public static final String TABLE_RFC1213_interfaces_ifOutNUcastPkts =
		  "1.3.6.1.2.1.2.2.1.18";

	  public static final String TABLE_RFC1213_interfaces_ifInUcastPkts =
		  "1.3.6.1.2.1.2.2.1.11";
	  public static final String TABLE_RFC1213_interfaces_ifInNUcastPkts =
		  "1.3.6.1.2.1.2.2.1.12";
	  public static final String TABLE_RFC1213_interfaces_ifInOctets =
		  "1.3.6.1.2.1.2.2.1.10";
	  public static final String TABLE_RFC1213_interfaces_ifOutOctets =
		  "1.3.6.1.2.1.2.2.1.16";
	  public static final String TABLE_RFC1213_interfaces_ifSpeed =
		  "1.3.6.1.2.1.2.2.1.5";

	  // ------------- microsoft IIS Server ���	
	  //	
	  public static final String httpServer_httpStatistics_totalBytesSentHighWord =
		  "1.3.6.1.4.1.311.1.7.3.1.1.0";
	  //	
	  public static final String httpServer_httpStatistics_totalBytesSentLowWord =
		  "1.3.6.1.4.1.311.1.7.3.1.2.0";
	  //	
	  public static final String httpServer_httpStatistics_totalBytesReceivedHighWord =
		  "1.3.6.1.4.1.311.1.7.3.1.3.0";
	  //	
	  public static final String httpServer_httpStatistics_totalBytesReceivedLowWord =
		  "1.3.6.1.4.1.311.1.7.3.1.4.0";
	  //	
	  public static final String httpServer_httpStatistics_totalFilesSent =
		  "1.3.6.1.4.1.311.1.7.3.1.5.0";
	  //	
	  public static final String httpServer_httpStatistics_totalFilesReceived =
		  "1.3.6.1.4.1.311.1.7.3.1.6.0";
	  //	
	  public static final String httpServer_httpStatistics_currentAnonymousUsers =
		  "1.3.6.1.4.1.311.1.7.3.1.7.0";
	  //	
	  public static final String httpServer_httpStatistics_totalAnonymousUsers =
		  "1.3.6.1.4.1.311.1.7.3.1.9.0";
	  //	
	  public static final String httpServer_httpStatistics_maxAnonymousUsers =
		  "1.3.6.1.4.1.311.1.7.3.1.11.0";
	  //	
	  public static final String httpServer_httpStatistics_currentConnections =
		  "1.3.6.1.4.1.311.1.7.3.1.13.0";
	  //	
	  public static final String httpServer_httpStatistics_maxConnections =
		  "1.3.6.1.4.1.311.1.7.3.1.14.0";
	  //	
	  public static final String httpServer_httpStatistics_connectionAttempts =
		  "1.3.6.1.4.1.311.1.7.3.1.15.0";
	  //	
	  public static final String httpServer_httpStatistics_logonAttempts =
		  "1.3.6.1.4.1.311.1.7.3.1.16.0";
	  //	
	  public static final String httpServer_httpStatistics_totalGets =
		  "1.3.6.1.4.1.311.1.7.3.1.18.0";
	  //	
	  public static final String httpServer_httpStatistics_totalPosts =
		  "1.3.6.1.4.1.311.1.7.3.1.19.0";
	  //	
	  public static final String httpServer_httpStatistics_totalNotFoundErrors =
		  "1.3.6.1.4.1.311.1.7.3.1.43.0";
	  
	  public static final Hashtable  scStatus= new Hashtable();
		static {
			scStatus.put("100", "������������");
			scStatus.put("101", "��������������ת��HTTPЭ��汾");
			scStatus.put("200", "���׳ɹ�");
			scStatus.put("201", "���ļ���URL");
			scStatus.put("202", "���ܺʹ���������δ���");
			scStatus.put("203", "������Ϣ��ȷ��������");
			scStatus.put("204", "�����յ�����������ϢΪ��");
			scStatus.put("205", "����������������û�������븴λ��ǰ�Ѿ���������ļ�");
			scStatus.put("206", "�������Ѿ�����˲����û���GET����");
			scStatus.put("300", "�������Դ���ڶദ�õ�");
			scStatus.put("301", "ɾ����������");
			scStatus.put("302", "��������ַ��������������");
			scStatus.put("303", "����ͻ���������URL����ʷ�ʽ");
			scStatus.put("304", "�ͻ����Ѿ�ִ����GET�����ļ�δ�仯");
			scStatus.put("305", "�������Դ����ӷ�����ָ���ĵ�ַ�õ�");
			scStatus.put("306", "ǰһ�汾HTTP��ʹ�õĴ��룬���а汾�в���ʹ��");
			scStatus.put("307", "�����������Դ��ʱ��ɾ��");
			scStatus.put("400", "�����������﷨����");
			scStatus.put("401", "������Ȩʧ��");
			scStatus.put("402", "������ЧChargeToͷ��Ӧ");
			scStatus.put("403", "��������");
			scStatus.put("404", "û�з����ļ�����ѯ��URl");
			scStatus.put("405", "�û���Request-Line�ֶζ���ķ���������");
			scStatus.put("406", "�����û����͵�Accept�ϣ�������Դ���ɷ���");
			scStatus.put("407", "����401���û����������ڴ���������ϵõ���Ȩ");
			scStatus.put("408", "�ͻ���û�����û�ָ���Ķ�ʱ�����������");
			scStatus.put("409", "�Ե�ǰ��Դ״̬�����������");
			scStatus.put("410", "�������ϲ����д���Դ���޽�һ���Ĳο���ַ");
			scStatus.put("411", "�������ܾ��û������Content-Length��������");
			scStatus.put("412", "һ����������ͷ�ֶ��ڵ�ǰ�����д���");
			scStatus.put("413", "�������Դ���ڷ���������Ĵ�С");
			scStatus.put("414", "�������ԴURL���ڷ���������ĳ���");
			scStatus.put("415", "������Դ��֧��������Ŀ��ʽ");
			scStatus.put("416", "�����а���Range����ͷ�ֶΣ��ڵ�ǰ������Դ��Χ��û��rangeָʾֵ������Ҳ������If-Range����ͷ�ֶ�");
			scStatus.put("417", "����������������Expectͷ�ֶ�ָ��������ֵ������Ǵ������������������һ�������������������� ");
			scStatus.put("500", "�����������ڲ�����");
			scStatus.put("501", "��������֧������ĺ���");
			scStatus.put("502", "��������ʱ�����ã���ʱ��Ϊ�˷�ֹ����ϵͳ����");
			scStatus.put("503", "���������ػ���ͣά��");
			scStatus.put("504", "�ؿڹ��أ�������ʹ����һ���ؿڻ��������Ӧ�û����ȴ�ʱ���趨ֵ�ϳ�");
			scStatus.put("505", "��������֧�ֻ�ܾ�֧����ͷ��ָ����HTTP�汾");
			
			
		}
		
		public static final Hashtable SCWIN32STATUS = new Hashtable();

		static {
			SCWIN32STATUS.put("0", "�����ѳɹ����");
			SCWIN32STATUS.put("1", "����ĺ���");
			SCWIN32STATUS.put("2", "ϵͳ�Ҳ���ָ�����ļ�");
			SCWIN32STATUS.put("3", "ϵͳ�Ҳ���ָ����·��");
			SCWIN32STATUS.put("4", "ϵͳ�޷����ļ�");
			SCWIN32STATUS.put("5", "�ܾ�����");
			SCWIN32STATUS.put("6", "�����Ч");
			SCWIN32STATUS.put("7", "�洢�����ƿ�����");
			SCWIN32STATUS.put("8", "���õĴ洢�����㣬�޷�ִ�и�����");
			SCWIN32STATUS.put("9", "�洢�����ƿ��ַ��Ч");
			SCWIN32STATUS.put("10", "��������");
			SCWIN32STATUS.put("11", "��ͼʹ�ò���ȷ�ĸ�ʽ���س���");
			SCWIN32STATUS.put("12", "���ʴ�����Ч");
			SCWIN32STATUS.put("13", "������Ч");
			SCWIN32STATUS.put("14", "���õĴ洢�����㣬�޷���ɸò���");
			SCWIN32STATUS.put("15", "ϵͳ�Ҳ���ָ����������");
			SCWIN32STATUS.put("16", "�޷�ɾ����Ŀ¼");
			SCWIN32STATUS.put("17", "ϵͳ�޷����ļ��Ƶ�����������������");
			SCWIN32STATUS.put("18", "û�������ļ�");
			SCWIN32STATUS.put("19", "ý��д����");
			SCWIN32STATUS.put("20", "ϵͳ�Ҳ���ָ�����豸");
			SCWIN32STATUS.put("21", "�豸��δ׼����");
			SCWIN32STATUS.put("22", "�豸�޷�ʶ�������");
			SCWIN32STATUS.put("23", "���ݴ���ѭ�������飩");
			SCWIN32STATUS.put("24", "���򷢳�������Ǹ�����ĳ��ȴ���");
			SCWIN32STATUS.put("25", "�������ڴ������޷���λָ���������ŵ�");
			SCWIN32STATUS.put("26", "�޷�����ָ���Ĵ��̻�����");
			SCWIN32STATUS.put("27", "�������Ҳ��������������");
			SCWIN32STATUS.put("28", "��ӡ��ȱֽ");
			SCWIN32STATUS.put("29", "ϵͳ�޷�д��ָ�����豸");
			SCWIN32STATUS.put("30", "ϵͳ�޷���ȡָ�����豸");
			SCWIN32STATUS.put("31", "��ϵͳ���ӵ��豸����������ת");
			SCWIN32STATUS.put("32", "����������ʹ�ø��ļ�����������޷�����");
			SCWIN32STATUS.put("33", "��һ�������������ļ���ĳһ���֣���������޷�����");
			SCWIN32STATUS.put("34", "�������е����̲���ȷ���뽫   %2   ��������к�:   %3������������   %");
			SCWIN32STATUS.put("36", "�򿪹�����ļ�̫��");
			SCWIN32STATUS.put("38", "�ѵ����ļ���β");
			SCWIN32STATUS.put("39", "��������");
			SCWIN32STATUS.put("50", "��֧�ִ���������");
			SCWIN32STATUS.put("51", "Զ�̼�����޷�ʹ��");
			SCWIN32STATUS.put("52", "�����д�������");
			SCWIN32STATUS.put("53", "�Ҳ�������·��");
			SCWIN32STATUS.put("54", "������æ");
			SCWIN32STATUS.put("55", "ָ����������Դ���豸�Ѳ�����");
			SCWIN32STATUS.put("56", "�Ѿ��ﵽ����   BIOS   ����ļ���");
			SCWIN32STATUS.put("57", "�������������ִ���");
			SCWIN32STATUS.put("58", "ָ���ķ������޷�����������Ĳ���");
			SCWIN32STATUS.put("59", "��������������");
			SCWIN32STATUS.put("60", "Զ��������������");
			SCWIN32STATUS.put("61", "��ӡ����������");
			SCWIN32STATUS.put("62", "��������û�д洢�ȴ���ӡ���ļ��Ŀռ�");
			SCWIN32STATUS.put("63", "�Ѿ�ɾ���Ⱥ��ӡ���ļ�");
			SCWIN32STATUS.put("64", "ָ�����������޷�ʹ��");
			SCWIN32STATUS.put("65", "�ܾ���������");
			SCWIN32STATUS.put("66", "������Դ���ʹ���");
			SCWIN32STATUS.put("67", "�Ҳ���������");
			SCWIN32STATUS.put("68", "�ѳ������ؼ���������������������Ƽ���");
			SCWIN32STATUS.put("69", "�ѳ�������   BIOS   �Ự�ļ���");
			SCWIN32STATUS.put("70", "Զ�̷������Ѿ���ͣ������������������");
			SCWIN32STATUS.put("71", "���ڸü������������Ŀ�Ѵﵽ���ޣ���ʱ�޷������ӵ���Զ�̼����");
			SCWIN32STATUS.put("72", "ָ���Ĵ�ӡ��������豸�Ѿ���ͣ");
			SCWIN32STATUS.put("80", "���ļ�����");
			SCWIN32STATUS.put("82", "�޷�������Ŀ¼���ļ�");
			SCWIN32STATUS.put("83", "INT   24��ʧ��");
			SCWIN32STATUS.put("84", "���������Ĵ洢��������");
			SCWIN32STATUS.put("85", "����ʹ�øñ����豸��");
			SCWIN32STATUS.put("86", "ָ�����������벻��ȷ");
			SCWIN32STATUS.put("87", "��������");
			SCWIN32STATUS.put("88", "�������д�����");
			SCWIN32STATUS.put("89", "��ʱϵͳ�޷�������������");
			SCWIN32STATUS.put("100", "�޷���������ϵͳ��־");
			SCWIN32STATUS.put("101", "�������������ר�ñ�־");
			SCWIN32STATUS.put("102", "��־�Ѿ����ã��޷��ر�");
			SCWIN32STATUS.put("103", "�޷��ٴ����øñ�־");
			SCWIN32STATUS.put("104", "�ж�ʱ�޷�����ר�ñ�־");
			SCWIN32STATUS.put("105", "�˱�־��ǰ������Ȩ����ֹ");
			SCWIN32STATUS.put("106", "�뽫���̲���������   %1");
			SCWIN32STATUS.put("107", "����������δ���룬����ֹͣ");
			SCWIN32STATUS.put("108", "��������ʹ�û�����������������");
			SCWIN32STATUS.put("109", "�ܵ��Ѿ�����");
			SCWIN32STATUS.put("110", "ϵͳ�޷���ָ�����豸���ļ�");
			SCWIN32STATUS.put("111", "�ļ���̫��");
			SCWIN32STATUS.put("112", "���̿ռ䲻��");
			SCWIN32STATUS.put("113", "û���������õ��ڲ��ļ���ʶ��");
			SCWIN32STATUS.put("114", "Ŀ���ڲ��ļ���ʶ������ȷ");
			SCWIN32STATUS.put("117", "��Ӧ�ó��������е�   IOCTL   ���ò���ȷ");
			SCWIN32STATUS.put("118", "У��д��Ŀ��ز���ֵ����ȷ");
			SCWIN32STATUS.put("119", "ϵͳ��֧�������������");
			SCWIN32STATUS.put("120", "�˹��ܽ���   Win32   ģʽ����Ч");
			SCWIN32STATUS.put("121", "����ѳ�ʱ");
			SCWIN32STATUS.put("122", "����ϵͳ���õ���������̫С");
			SCWIN32STATUS.put("123", "�ļ�����Ŀ¼�������﷨����");
			SCWIN32STATUS.put("124", "ϵͳ���ò㲻��ȷ");
			SCWIN32STATUS.put("125", "����û�о��");
			SCWIN32STATUS.put("126", "�Ҳ���ָ����ģ��");
			SCWIN32STATUS.put("127", "�Ҳ���ָ���Ĺ���");
			SCWIN32STATUS.put("128", "û��Ҫ�Ⱥ���ӳ���");
			SCWIN32STATUS.put("129", "%1   Ӧ�ó����޷���   Win32   ģʽ������");
			SCWIN32STATUS.put("130", "��ͼʹ�ô򿪵Ĵ��̷������ļ������������ԭʼ����   I/O  ���в�");
			SCWIN32STATUS.put("131", "��ͼ���ļ�ָ�������ļ���ͷ֮ǰ");
			SCWIN32STATUS.put("132", "�޷���ָ�����豸���ļ��������ļ�ָ");
			SCWIN32STATUS.put("133", "�����Ѱ���������������������������ʹ��   JOIN   ��   SUBST  ����");
			SCWIN32STATUS.put("134", "��ͼ���Ѿ����ӵ���������ʹ��JOIN   ��   SUBST   ����");
			SCWIN32STATUS.put("135", "��ͼ���Ѿ��滻����������ʹ��   JOIN   ��   SUBST   ����");
			SCWIN32STATUS.put("136", "ϵͳ��ͼɾ����δ���ӵ���������   JOIN");
			SCWIN32STATUS.put("137", "ϵͳ��ͼɾ����δ�滻�����������滻��");
			SCWIN32STATUS.put("138", "ϵͳ��ͼ�����������ӵ������ӵ��������µ�Ŀ¼");
			SCWIN32STATUS.put("139", "ϵͳ��ͼ���������滻�����滻���������µ�Ŀ");
			SCWIN32STATUS.put("140", "ϵͳ��ͼ�����������ӵ����滻����������һ��Ŀ¼��");
			SCWIN32STATUS.put("141", "ϵͳ��ͼ��������   SUBST   ���ӵ������ӵ��������µ�Ŀ¼");
			SCWIN32STATUS.put("142", "��ʱϵͳ�޷�����   JOIN   ��   SUBST");
			SCWIN32STATUS.put("143", "ϵͳ�޷������������ӵ����滻��ͬһ�������µ�Ŀ¼");
			SCWIN32STATUS.put("144", "��Ŀ¼���Ǹø�Ŀ¼����Ŀ¼");
			SCWIN32STATUS.put("145", "��Ŀ¼δ���");
			SCWIN32STATUS.put("146", "ָ����·���Ѿ����滻��ʹ��");
			SCWIN32STATUS.put("147", "��Դ���㣬�޷�ִ�и�����");
			SCWIN32STATUS.put("148", "��ʱ�޷�ʹ��ָ����·��");
			SCWIN32STATUS.put("149", "��ͼ�ϲ����滻ĳ��������Ŀ¼�����Ǹ�������Ŀ¼��һ���ѱ��滻ΪĿ��Ŀ¼");
			SCWIN32STATUS.put("150", "CONFIG.SYS   �ļ�δָ��ϵͳ������Ϣ�����ֹ����");
			SCWIN32STATUS.put("151", "DosMuxSemWait   ��ָ���ź��¼�����Ŀ����ȷ");
			SCWIN32STATUS.put("152", "DosMuxSemWait   û�����У��Ѿ�����̫��ı�־");
			SCWIN32STATUS.put("153", "DosMuxSemWait   �б���ȷ");
			SCWIN32STATUS.put("154", "����ľ�곬��Ŀ���ļ�ϵͳ�ı���ַ����ȼ���");
			SCWIN32STATUS.put("155", "�޷����������߳�");
			SCWIN32STATUS.put("156", "���մ���ܾ����ź�");
			SCWIN32STATUS.put("157", "�Ѿ���������������޷�����");
			SCWIN32STATUS.put("158", "�������Ѿ��������");
			SCWIN32STATUS.put("159", "�̱߳�ʶ���ĵ�ַ����");
			SCWIN32STATUS.put("160", "����   DosExecPgm   �Ĳ����ַ�������");
			SCWIN32STATUS.put("161", "ָ����·����Ч");
			SCWIN32STATUS.put("162", "�ź��ѹ���");
			SCWIN32STATUS.put("164", "ϵͳ�޷����������߳�");
			SCWIN32STATUS.put("167", "�޷������ļ��ķ�Χ");
			SCWIN32STATUS.put("170", "��Ҫ�����Դ����ʹ����");
			SCWIN32STATUS.put("173", "������������ṩ��ȡ��������Ҫ");
			SCWIN32STATUS.put("174", "�ļ�ϵͳ��֧�ֵ��������͵��Զ�����");
			SCWIN32STATUS.put("180", "ϵͳ��⵽������������");
			SCWIN32STATUS.put("182", "����ϵͳ�޷�����   %1");
			SCWIN32STATUS.put("183", "���ܴ����Ѿ����ڵ��ļ�");
			SCWIN32STATUS.put("186", "���͵ı�־����ȷ");
			SCWIN32STATUS.put("187", "�Ҳ���ָ����ϵͳ�ź�����");
			SCWIN32STATUS.put("188", "����ϵͳ�޷�����   %1");
			SCWIN32STATUS.put("189", "����ϵͳ�޷�����   %");
			SCWIN32STATUS.put("190", "����ϵͳ�޷�����   %1");
			SCWIN32STATUS.put("191", "�޷���   Win32   ģʽ������   %1");
			SCWIN32STATUS.put("192", "����ϵͳ�޷�����   %1");
			SCWIN32STATUS.put("193", "%1   ������Ч��   Win32   Ӧ�ó���");
			SCWIN32STATUS.put("194", "����ϵͳ�޷�����   %1");
			SCWIN32STATUS.put("195", "����ϵͳ�޷�����   %1");
			SCWIN32STATUS.put("196", "����ϵͳ�޷����д�Ӧ�ó���");
			SCWIN32STATUS.put("197", "����ϵͳ��ǰ�޷����д�Ӧ�ó���");
			SCWIN32STATUS.put("198", "����ϵͳ�޷�����   %1");
			SCWIN32STATUS.put("199", "����ϵͳ�޷����д�Ӧ�ó���");
			SCWIN32STATUS.put("200", "�����ӦС��   64KB");
			SCWIN32STATUS.put("201", "����ϵͳ�޷�����   %1");
			SCWIN32STATUS.put("202", "����ϵͳ�޷�����   %1");
			SCWIN32STATUS.put("203", "ϵͳ�Ҳ�������Ļ���ѡ��");
			SCWIN32STATUS.put("205", "�����������еĽ���û���źž��");
			SCWIN32STATUS.put("206", "�ļ�������չ��̫��");
			SCWIN32STATUS.put("207", "��   2   ��ջ����ʹ����");
			SCWIN32STATUS.put("208", "�����ȫ���ļ����ַ�   *   ��   ?   ����ȷ����ָ����ȫ���ļ����ַ�̫��");
			SCWIN32STATUS.put("209", "�����͵��źŲ���ȷ");
			SCWIN32STATUS.put("210", "�޷������źŴ������");
			SCWIN32STATUS.put("212", "�������������޷����·���");
			SCWIN32STATUS.put("214", "���ӵ��˳����̬����ģ��Ķ�̬����ģ��̫��");
			SCWIN32STATUS.put("215", "�޷������ô�������ģ��");
			SCWIN32STATUS.put("230", "�ܵ�״̬��Ч");
			SCWIN32STATUS.put("231", "���еĹܵ�ʵ��������æ״̬");
			SCWIN32STATUS.put("232", "�ܵ����ڹر�");
			SCWIN32STATUS.put("233", "�ڹܵ�����һ��û�н���");
			SCWIN32STATUS.put("234", "�и�����õ�����");
			SCWIN32STATUS.put("240", "��ȡ���ûỰ");
			SCWIN32STATUS.put("254", "ָ������չ��������Ч");
			SCWIN32STATUS.put("255", "��չ���Բ�һ��");
			SCWIN32STATUS.put("259", "û��������������");
			SCWIN32STATUS.put("266", "�޷�ʹ��   Copy   API");
			SCWIN32STATUS.put("267", "Ŀ¼����Ч");
			SCWIN32STATUS.put("275", "��չ���Բ�ƥ�仺����");
			SCWIN32STATUS.put("276", "��װ�ص��ļ�ϵͳ�ϵ���չ�����ļ��ѱ���");
			SCWIN32STATUS.put("277", "��չ���Ա���ļ�����");
			SCWIN32STATUS.put("278", "ָ������չ���Ծ����Ч");
			SCWIN32STATUS.put("282", "��װ���ļ�ϵͳ��֧����չ����");
			SCWIN32STATUS.put("288", "��ͼ�ͷŲ����ڵ����ߵĶ�·ͬ���ź�");
			SCWIN32STATUS.put("298", "�ź�Ͷ�ݵĴ���̫��");
			SCWIN32STATUS.put("299", "����ɲ���   Read/WriteProcessMemory   ����");
			SCWIN32STATUS.put("317", "��   %2   ����Ϣ�ļ��У�ϵͳ�޷��ҵ���Ϣ��Ϊ   0x%1   ����Ϣ");
			SCWIN32STATUS.put("487", "��ͼ������Ч��ַ");
			SCWIN32STATUS.put("534", "����������   32   λ");
			SCWIN32STATUS.put("535", "�ùܵ�����һ����һ����");
			SCWIN32STATUS.put("536", "�Ⱥ����Դ򿪹ܵ�����һ��");
			SCWIN32STATUS.put("994", "�ܾ�����չ���Եķ���");
			SCWIN32STATUS.put("995", "�����߳��˳���Ӧ�ó����Ҫ��I/O   �����쳣��ֹ");
			SCWIN32STATUS.put("996", "�ص���   I/O   �¼��������ѱ��״̬");
			SCWIN32STATUS.put("997", "���ڴ����ص���   I/O   ����");
			SCWIN32STATUS.put("998", "���ڴ�λ�õ���Ч����");
			SCWIN32STATUS.put("999", "ִ��ҳ�ڲ�������");
			SCWIN32STATUS.put("1001", "ѭ��̫���ջ���");
			SCWIN32STATUS.put("1002", "�����޷�����������Ϣ");
			SCWIN32STATUS.put("1003", "�޷���ɴ����");
			SCWIN32STATUS.put("1004", "��־��Ч");
			SCWIN32STATUS.put("1005", "��������ʶ����ļ�ϵͳ��ȷ��������Ҫ���ļ�ϵͳ���������Ѿ�װ�أ����Ҿ�û���κ���");
			SCWIN32STATUS.put("1006", "ĳ�ļ��ľ������ⲿ�ı䣬����򿪵��ļ�������Ч");
			SCWIN32STATUS.put("1007", "Ҫ��Ĳ����޷���ȫ��Ļģʽִ��");
			SCWIN32STATUS.put("1017", "ϵͳ��ͼ���ļ�װ�ػ�ԭ��ע����У����ǣ�ָ�����ļ�����ע����ļ���ʽ");
			SCWIN32STATUS.put("1018", "��ͼ��ע�������Ѿ����Ϊɾ��������ɵĲ����Ƿ�");
			SCWIN32STATUS.put("1019", "ϵͳ�޷���ע�����־�ļ��з�������Ŀռ�");
			SCWIN32STATUS.put("1020", "�޷����Ѿ����ӹؼ��ֻ���ֵ��ע���ؼ����д�����������");
			SCWIN32STATUS.put("1021", "����ʧ�ĸ����²��ܴ����̶����Ӽ�");
			SCWIN32STATUS.put("1022", "֪ͨ�ĸ��������Ѿ���ɣ����ҷ�����Ϣ��û�б��͵������ߵĻ������е�������Ҫ�о������ļ����ҵ��Ķ�������");
			SCWIN32STATUS.put("1051", "�ѽ�ֹͣ���Ʒ��͸����������з�����صķ���");
			SCWIN32STATUS.put("1052", "��Ҫ��Ŀ��ƶԴ˷�����Ч");
			SCWIN32STATUS.put("1053", "����û�м�ʱ����Ӧ�������������");
			SCWIN32STATUS.put("1054", "�޷�Ϊ�÷��񴴽��߳�");
			SCWIN32STATUS.put("1055", "�������ݿ�������");
			SCWIN32STATUS.put("1056", "�÷����ʵ����������");
			SCWIN32STATUS.put("1057", "�ʺ�����Ч���߲�����");
			SCWIN32STATUS.put("1058", "ָ���ķ�����ã��޷�����");
			SCWIN32STATUS.put("1059", "�Ѿ�ָ����ѭ������Ĵ�����ϵ");
			SCWIN32STATUS.put("1060", "ָ���ķ���������װ�ķ���");
			SCWIN32STATUS.put("1061", "�÷����ʱ�޷����տ�����Ϣ");
			SCWIN32STATUS.put("1062", "������δ����");
			SCWIN32STATUS.put("1063", "�������޷����ӵ�������Ƴ���");
			SCWIN32STATUS.put("1064", "�����������ʱ����������������");
			SCWIN32STATUS.put("1065", "ָ�������ݿⲻ����");
			SCWIN32STATUS.put("1066", "���񷵻ط����ض��Ĵ�����");
			SCWIN32STATUS.put("1067", "�����������ֹ");
			SCWIN32STATUS.put("1068", "�޷����������������");
			SCWIN32STATUS.put("1069", "���ڵ�¼ʧ�ܣ�û����������");
			SCWIN32STATUS.put("1070", "�����󣬷��񱣳�����������״̬");
			SCWIN32STATUS.put("1071", "ָ���ķ������ݿ�������Ч");
			SCWIN32STATUS.put("1072", "ָ���ķ����Ѿ����Ϊɾ��");
			SCWIN32STATUS.put("1073", "ָ���ķ����Ѿ�����");
			SCWIN32STATUS.put("1074", "ϵͳ��ǰ������һ�����гɹ�����������");
			SCWIN32STATUS.put("1075", "�������񲻴��ڣ����Ѿ����Ϊɾ��");
			SCWIN32STATUS.put("1076", "�Ѿ����ܽ���ǰ������������һ�����гɹ��Ŀ�������");
			SCWIN32STATUS.put("1077", "�Դ���һ�������Ժ�û���ٴ��������÷���");
			SCWIN32STATUS.put("1078", "�������Ѿ������������������ʾ��");
			SCWIN32STATUS.put("1100", "�Ѿ�����Ŵ�������ͷ");
			SCWIN32STATUS.put("1101", "�Ŵ����ʵ��ļ����");
			SCWIN32STATUS.put("1102", "����Ŵ�������ײ�");
			SCWIN32STATUS.put("1103", "�Ŵ����ʵ��ļ����ĩβ");
			SCWIN32STATUS.put("1104", "�Ŵ���û����������");
			SCWIN32STATUS.put("1105", "�Ŵ��޷�����");
			SCWIN32STATUS.put("1106", "���ʶ��ؾ�������´Ŵ�ʱ����ǰ�������С����");
			SCWIN32STATUS.put("1107", "װ�شŴ�ʱ���Ҳ����Ŵ�������Ϣ");
			SCWIN32STATUS.put("1108", "�޷�����ý���˳�����");
			SCWIN32STATUS.put("1109", "�޷�ж��ý��");
			SCWIN32STATUS.put("1110", "�������е�ý���Ѿ�����");
			SCWIN32STATUS.put("1111", "�Ѿ���λI/O����");
			SCWIN32STATUS.put("1112", "��������û��ý��");
			SCWIN32STATUS.put("1113", "��Ŀ����ֽڴ���ҳ�в����ڶԵ����ַ���ӳ��");
			SCWIN32STATUS.put("1114", "��̬���ӿ�(DLL)��ʼ������ʧ��");
			SCWIN32STATUS.put("1115", "���ڹر�ϵͳ");
			SCWIN32STATUS.put("1116", "���ڹر�ϵͳ");
			SCWIN32STATUS.put("1117", "����I/O�豸���ִ����޷����и�����");
			SCWIN32STATUS.put("1118", "�����豸��ʼ��ʧ�ܽ�ж�ش�����������");
			SCWIN32STATUS.put("1119", "�޷������������豸�����ж�����(IRQ)���豸������һ��ʹ�ø�IRQ���豸�Ѿ���");
			SCWIN32STATUS.put("1120", "�����ٴ�д�봮�пڣ�����I/O�����ѽ���(IOCTL_SERIAL_XOFF_COUNTERΪ��)");
			SCWIN32STATUS.put("1121", "���ڳ�ʱ������I/O�����ѽ���(IOCTL_SERIAL_XOFF_COUNTERδ�ﵽ��)");
			SCWIN32STATUS.put("1122", "���������Ҳ�����ʶ����ַ���");
			SCWIN32STATUS.put("1123", "����������ʶ���ֶ������̿������ŵ���ַ��ƥ��");
			SCWIN32STATUS.put("1124", "���̿���������Ĵ����������������޷�ʶ����");
			SCWIN32STATUS.put("1125", "���̿��������صĽ����ע��Ĳ�һ��");
			SCWIN32STATUS.put("1126", "����Ӳ��ʱ����У׼����ʧ�ܣ�����һ�κ�Ҳ�޷�����");
			SCWIN32STATUS.put("1127", "����Ӳ��ʱ�����̲���ʧ�ܣ�����һ�κ���û������");
			SCWIN32STATUS.put("1128", "����Ӳ��ʱ����Ҫ���������̿�����������δ�ɹ�");
			SCWIN32STATUS.put("1129", "�Ŵ��Ѿ���ͷ");
			SCWIN32STATUS.put("1130", "���õķ������洢�����㣬�޷�ִ�и�����");
			SCWIN32STATUS.put("1131", "��⵽Ǳ�ڵ��������");
			SCWIN32STATUS.put("1132", "������ַ����ָ�����ļ�ƫ����û����ȷ����");
			SCWIN32STATUS.put("1140", "��ͼ����ϵͳ��Դ״̬ʱ��������һӦ�ó�������������ֹ");
			SCWIN32STATUS.put("1141", "ϵͳBIOS�޷�����ϵͳ��Դ״̬");
			SCWIN32STATUS.put("1150", "ָ���ĳ�����Ҫ�µ�Windows�汾");
			SCWIN32STATUS.put("1151", "ָ���ĳ�����Windows��MS-DOS����");
			SCWIN32STATUS.put("1152", "�޷�����ָ������Ķ��ʵ��");
			SCWIN32STATUS.put("1153", "ָ���ĳ�����Ϊ�ɰ��Windows��д��");
			SCWIN32STATUS.put("1154", "���д�Ӧ�ó��������ĳ�����ļ�����û��Ӧ�ó�����ò�������ָ�����ļ���");
			SCWIN32STATUS.put("1155", "û��Ӧ�ó�����ò�������ָ�����ļ�����");
			SCWIN32STATUS.put("1156", "������͵�Ӧ�ó���ʱ���ִ���");
			SCWIN32STATUS.put("1157", "�Ҳ������д�Ӧ�ó��������ĳ�����ļ�");
			SCWIN32STATUS.put("1200", "ָ�����豸����Ч");
			SCWIN32STATUS.put("1201", "�豸��ǰ��Ȼδ���ӣ������Ǽ�������");
			SCWIN32STATUS.put("1202", "��ͼ�����Ѿ���ס���豸");
			SCWIN32STATUS.put("1203", "���繩Ӧ�̲����ܸ���������·��");
			SCWIN32STATUS.put("1204", "ָ�������繩Ӧ������Ч");
			SCWIN32STATUS.put("1205", "�޷����������������ļ�");
			SCWIN32STATUS.put("1206", "�������������ļ���");
			SCWIN32STATUS.put("1207", "�޷��оٷǰ�����");
			SCWIN32STATUS.put("1208", "������չ����");
			SCWIN32STATUS.put("1209", "ָ�������ĸ�ʽ��Ч");
			SCWIN32STATUS.put("1210", "ָ����������ĸ�ʽ��Ч");
			SCWIN32STATUS.put("1211", "ָ���¼����ĸ�ʽ��Ч");
			SCWIN32STATUS.put("1212", "ָ�������ĸ�ʽ��Ч");
			SCWIN32STATUS.put("1213", "ָ���������ĸ�ʽ��Ч");
			SCWIN32STATUS.put("1214", "ָ���������ĸ�ʽ��Ч");
			SCWIN32STATUS.put("1215", "ָ���������ĸ�ʽ��Ч");
			SCWIN32STATUS.put("1216", "ָ������ĸ�ʽ��Ч");
			SCWIN32STATUS.put("1217", "ָ�����ʼ�����Ч");
			SCWIN32STATUS.put("1218", "ָ���ʼ�Ŀ�ĵصĸ�ʽ��Ч");
			SCWIN32STATUS.put("1219", "���ṩ��ƾ��������ƾ�����ó�ͻ");
			SCWIN32STATUS.put("1220", "��ͼ����������������Ự����Ŀǰ��÷����������ĻỰ̫��");
			SCWIN32STATUS.put("1221", "�����ϵ�����������Ѿ�ʹ�øù����������");
			SCWIN32STATUS.put("1222", "���粻���ڻ���û������");
			SCWIN32STATUS.put("1223", "�û��Ѿ�ȡ���ò���");
			SCWIN32STATUS.put("1224", "��Ҫ��Ĳ����޷����Ѿ����û�ӳ��������ļ�������");
			SCWIN32STATUS.put("1225", "Զ��ϵͳ�ܾ���������");
			SCWIN32STATUS.put("1226", "�Ѿ��ر���������");
			SCWIN32STATUS.put("1227", "���紫����յ��Ѿ���һ����ַ�������");
			SCWIN32STATUS.put("1228", "�����յ���δ���ַ����");
			SCWIN32STATUS.put("1229", "��ͼ�ڲ����ڵ����������в���");
			SCWIN32STATUS.put("1230", "��ͼ�ڻ�����������Ͻ�����Ч����");
			SCWIN32STATUS.put("1231", "�޷����䵽Զ������");
			SCWIN32STATUS.put("1232", "�޷����䵽Զ��ϵͳ");
			SCWIN32STATUS.put("1233", "Զ��ϵͳ��֧�ִ���Э��");
			SCWIN32STATUS.put("1234", "Զ��ϵͳ��Ŀ������˵�û�������κη���");
			SCWIN32STATUS.put("1235", "�������Ѿ���ֹ");
			SCWIN32STATUS.put("1236", "����ϵͳ�Ѿ���ֹ��������");
			SCWIN32STATUS.put("1237", "�޷���ɲ�����������һ��");
			SCWIN32STATUS.put("1238", "�޷��������÷����������ӣ���Ϊ�Ѿ������˸��ʺ�ͬʱ���ӵ������Ŀ");
			SCWIN32STATUS.put("1239", "��ͼ�ڸ��ʺ�δ��Ȩ��ʱ���ڵ�¼");
			SCWIN32STATUS.put("1240", "��δ��Ȩ���ʺŴӸ�վ��¼����");
			SCWIN32STATUS.put("1241", "�����ַ�޷�����Ҫ��Ĳ���");
			SCWIN32STATUS.put("1242", "�����Ѿ�ע��");
			SCWIN32STATUS.put("1243", "ָ���ķ��񲻴���");
			SCWIN32STATUS.put("1244", "������δ��֤�û���ݣ��޷�ִ��Ҫ��Ĳ���");
			SCWIN32STATUS.put("1245", "�����û���δ��¼���磬�޷�����Ҫ��Ĳ���ָ���ķ��񲻴���");
			SCWIN32STATUS.put("1246", "������Ҫ�����߼�����������Ϣ");
			SCWIN32STATUS.put("1247", "��ɳ�ʼ����������ͼ�ٴ����г�ʼ������");
			SCWIN32STATUS.put("1248", "û�����������豸");
			SCWIN32STATUS.put("1300", "���Ƕ����еĵ��ó������������Ȩ");
			SCWIN32STATUS.put("1301", "�ʺ����밲ȫ��ʶ��֮���ӳ��δ���");
			SCWIN32STATUS.put("1302", "û��Ϊ���ʺ���ȷ������ϵͳ�������");
			SCWIN32STATUS.put("1303", "û�п��õ���Կ������֪����Կ");
			SCWIN32STATUS.put("1304", "NT����̫���ӣ��޷�ת����LANManager���뷵�ص�LANManager�����ǿ��ַ���");
			SCWIN32STATUS.put("1305", "�޶�����δ֪");
			SCWIN32STATUS.put("1306", "��ʾ�����޶����𲻼���");
			SCWIN32STATUS.put("1307", "�޷����˰�ȫ��ʶ��ָ��Ϊ�ö����ӵ����");
			SCWIN32STATUS.put("1308", "�޷����˰�ȫ��ʶ��ָ��Ϊ��Ҫ�Ķ�����");
			SCWIN32STATUS.put("1309", "Ŀǰδģ�¿ͻ����߳���ͼ��ģ�·����ϲ���");
			SCWIN32STATUS.put("1310", "�����Խ��ø���");
			SCWIN32STATUS.put("1311", "Ŀǰû�п��õĵ�¼�����������¼����");
			SCWIN32STATUS.put("1312", "ָ���ĵ�¼�Ự�����ڸûỰ��������ֹ");
			SCWIN32STATUS.put("1313", "ָ����Ȩ�޲�����");
			SCWIN32STATUS.put("1314", "�ͻ��������������Ȩ");
			SCWIN32STATUS.put("1315", "�ṩ�����Ʋ�����ȷ���ʺ����Ƹ�ʽ");
			SCWIN32STATUS.put("1316", "ָ�����û��Ѿ�����");
			SCWIN32STATUS.put("1317", "ָ�����û�������");
			SCWIN32STATUS.put("1318", "ָ�������Ѿ�����");
			SCWIN32STATUS.put("1319", "ָ�����鲻����");
			SCWIN32STATUS.put("1320", "����ָ�����û��ʺ��Ѿ���ĳ���ض���ĳ�Ա������Ҳ����ָ������ǿն����ܱ�ɾ��");
			SCWIN32STATUS.put("1321", "ָ�����û��ʺŲ�����ָ�����ʺŵĳ�Ա");
			SCWIN32STATUS.put("1322", "�ϴα����Ĺ����ʺ��޷��رջ�ɾ��");
			SCWIN32STATUS.put("1323", "�޷�������������������벻��ȷ");
			SCWIN32STATUS.put("1324", "�޷��������������������������������������ֵ");
			SCWIN32STATUS.put("1325", "��ΪΥ��������¹��������޷���������");
			SCWIN32STATUS.put("1326", "��¼ʧ��:�û���δ֪���������");
			SCWIN32STATUS.put("1327", "��¼ʧ��:�û��ʺ�����");
			SCWIN32STATUS.put("1328", "��¼ʧ��:Υ���ʺŵ�¼ʱ������");
			SCWIN32STATUS.put("1329", "��¼ʧ��:��ֹ�û���¼���ü������");
			SCWIN32STATUS.put("1330", "��¼ʧ��:ָ�����ʺ������ѹ���");
			SCWIN32STATUS.put("1331", "��¼ʧ��:��ǰ�����ʺ�");
			SCWIN32STATUS.put("1332", "δ����ʺ����밲ȫ�Ա�ʶ��֮���ӳ��");
			SCWIN32STATUS.put("1333", "һ������ı����û���ʶ��(LUID)̫��");
			SCWIN32STATUS.put("1334", "û���������õı����û���ʶ��(LUID)");
			SCWIN32STATUS.put("1335", "������ض�ʹ����˵����ȫ��ʶ�����Ӳ�������Ч��");
			SCWIN32STATUS.put("1336", "���ʿ����嵥(ACL)�ṹ��Ч");
			SCWIN32STATUS.put("1337", "��ȫ��ʶ���ṹ��Ч");
			SCWIN32STATUS.put("1338", "��ȫ�������ṹ��Ч");
			SCWIN32STATUS.put("1340", "�޷������̳еķ��ʿ����б�(ACL)����ʿ�����Ŀ(ACE)");
			SCWIN32STATUS.put("1341", "��������ǰ������");
			SCWIN32STATUS.put("1342", "��������ǰ����ʹ��");
			SCWIN32STATUS.put("1343", "���ṩ��ֵ����Ч�ı�ʶ����Ȩֵ");
			SCWIN32STATUS.put("1344", "û�и�����ڴ����ڸ��°�ȫ��Ϣ");
			SCWIN32STATUS.put("1345", "ָ����������Ч����ָ���������������������");
			SCWIN32STATUS.put("1359", "��ȫ�ʺ����ݿ�����ڲ��Ĳ�һ���Դ���");
			SCWIN32STATUS.put("1360", "ͨ�õķ������Ͱ����ڷ��������У��������Ѿ�ӳ��Ϊ��ͨ������");
			SCWIN32STATUS.put("1361", "��ȫ���������ĸ�ʽ���󣨾��Ի�����أ�");
			SCWIN32STATUS.put("1362", "����Ĳ���ֻ׼��¼����ʹ�øõ��ù��̲�δ����¼Ϊ��¼����");
			SCWIN32STATUS.put("1363", "�޷����Ѿ�ʹ�õı�ʶ���������µĵ�¼�Ự");
			SCWIN32STATUS.put("1364", "ָ����ȷ�����ݰ�δ֪");
			SCWIN32STATUS.put("1365", "��¼�Ự��״̬������Ĳ�����һ��");
			SCWIN32STATUS.put("1366", "��¼�Ự��ʶ������ʹ����");
			SCWIN32STATUS.put("1367", "��¼���������Ч�ĵ�¼����ֵ");
			SCWIN32STATUS.put("1368", "ֱ�����ݴ������ܵ���ȡ֮ǰ������ͨ���ùܵ�ģ��");
			SCWIN32STATUS.put("1369", "ע�������������״̬��������Ĳ���������");
			SCWIN32STATUS.put("1370", "ͻ�����ڲ���ȫ�����ݿ����");
			SCWIN32STATUS.put("1371", "�޷����ڲ��ʺ������иò���");
			SCWIN32STATUS.put("1372", "�޷��ڸ��ڲ��ض��������иò���");
			SCWIN32STATUS.put("1373", "�޷��ڸ��ڲ��ض��û������иò���");
			SCWIN32STATUS.put("1374", "��Ϊ���鵱ǰ���û�����Ҫ�飬���Բ��ܴӴ�����ɾ���û�");
			SCWIN32STATUS.put("1375", "�÷�������Ϊ��Ҫ����ʹ��");
			SCWIN32STATUS.put("1376", "ָ���ı����鲻����");
			SCWIN32STATUS.put("1377", "ָ�����ʺ������Ǳ�����ĳ�Ա");
			SCWIN32STATUS.put("1378", "ָ�����ʺ����Ѿ��Ǳ�����ĳ�Ա");
			SCWIN32STATUS.put("1379", "ָ���ı������Ѿ�����");
			SCWIN32STATUS.put("1380", "��¼ʧ��:�û��ڱ��������û�б���������ע������");
			SCWIN32STATUS.put("1381", "�����˿��Դ洢�ڵ���ϵͳ�е�����������");
			SCWIN32STATUS.put("1382", "���ܵĳ��ȳ������������ֵ");
			SCWIN32STATUS.put("1383", "���ذ�ȫ��Ȩ���ݿ�����ڲ���һ�µĴ���");
			SCWIN32STATUS.put("1384", "��¼ʱ���û��İ�ȫ���������ۻ�̫��İ�ȫ��ʶ��");
			SCWIN32STATUS.put("1385", "��¼ʧ��:�û��ڱ��������û�б���������ע������");
			SCWIN32STATUS.put("1386", "��������ܵ������������û�����");
			SCWIN32STATUS.put("1387", "�³�Ա�����ڣ�����޷�������ӵ�������");
			SCWIN32STATUS.put("1388", "�³�Ա���ʺ�������������޷�������ӵ�������");
			SCWIN32STATUS.put("1389", "ָ���İ�ȫ��ʶ��̫��");
			SCWIN32STATUS.put("1390", "��������ܵ����������ĸ��û�����");
			SCWIN32STATUS.put("1391", "��ʾACLû�пɼ̳е����");
			SCWIN32STATUS.put("1392", "�ļ���Ŀ¼���𻵣��޷���ȡ����");
			SCWIN32STATUS.put("1393", "���̽ṹ���𻵣��޷���ȡ");
			SCWIN32STATUS.put("1394", "ָ���ĵ�¼�Ựû���û��Ự��Կ");
			SCWIN32STATUS.put("1395", "���ڷ��ʵķ��������ض���Ŀ��������Ϊ���ӵ���Ŀ�Ѵﵽ����ɽ��ܵ���Ŀ�����Դ�ʱ�޷������µķ�������");
			SCWIN32STATUS.put("1400", "���ھ����Ч");
			SCWIN32STATUS.put("1401", "�˵������Ч");
			SCWIN32STATUS.put("1402", "�������Ч");
			SCWIN32STATUS.put("1403", "���ټ���ľ����Ч");
			SCWIN32STATUS.put("1404", "�ҽӾ����Ч");
			SCWIN32STATUS.put("1405", "���ش���λ�ýṹ�����Ч");
			SCWIN32STATUS.put("1406", "�޷��������ϲ���Ӵ���");
			SCWIN32STATUS.put("1407", "�Ҳ���������");
			SCWIN32STATUS.put("1408", "������Ч�����������߳�");
			SCWIN32STATUS.put("1409", "�Ѿ�ע���ȼ�");
			SCWIN32STATUS.put("1410", "���Ѿ�����");
			SCWIN32STATUS.put("1411", "�಻����");
			SCWIN32STATUS.put("1412", "�ര���Դ���");
			SCWIN32STATUS.put("1413", "������Ч");
			SCWIN32STATUS.put("1414", "ͼ������Ч");
			SCWIN32STATUS.put("1415", "ʹ��˽�˶Ի��򴰿���");
			SCWIN32STATUS.put("1416", "�Ҳ����б���ʶ��");
			SCWIN32STATUS.put("1417", "�Ҳ����κ�ͨ���");
			SCWIN32STATUS.put("1418", "�߳�û�д򿪼�����");
			SCWIN32STATUS.put("1419", "��δע���ȼ�");
			SCWIN32STATUS.put("1420", "�ô��ڲ�����Ч�ĶԻ��򴰿�");
			SCWIN32STATUS.put("1421", "�Ҳ������Ʊ�ʶ��");
			SCWIN32STATUS.put("1422", "����û�б༭���ƣ���˸���Ͽ����Ϣ��Ч");
			SCWIN32STATUS.put("1423", "���ڲ�����Ͽ�");
			SCWIN32STATUS.put("1424", "�߶ȱ���С��256");
			SCWIN32STATUS.put("1425", "�豸������(DC)�����Ч");
			SCWIN32STATUS.put("1426", "�ҽӹ���������Ч");
			SCWIN32STATUS.put("1427", "�ҽӹ�����Ч");
			SCWIN32STATUS.put("1428", "��������ģ��������������÷Ǳ��صĹҽ�");
			SCWIN32STATUS.put("1429", "ֻ��ȫ�����øùҽӹ���");
			SCWIN32STATUS.put("1430", "�Ѱ�װ�ռǹҽӹ���");
			SCWIN32STATUS.put("1431", "δ��װ�ҽӹ���");
			SCWIN32STATUS.put("1432", "��ѡ�б�����Ϣ��Ч");
			SCWIN32STATUS.put("1433", "LB_SETCOUNT���͵�����б��");
			SCWIN32STATUS.put("1434", "���б��֧���Ʊ��");
			SCWIN32STATUS.put("1435", "�޷��ƻ��������߳��������Ķ���");
			SCWIN32STATUS.put("1436", "�Ӵ��ڲ����в˵�");
			SCWIN32STATUS.put("1437", "����û��ϵͳ�˵�");
			SCWIN32STATUS.put("1438", "��Ϣ����ʽ��Ч");
			SCWIN32STATUS.put("1439", "ϵͳ��Χ�ڵ�(SPI_*)�Ĳ�����Ч");
			SCWIN32STATUS.put("1440", "��Ļ�Ѿ�����");
			SCWIN32STATUS.put("1441", "���ش���λ�ýṹ�����д��ھ�����������ͬ�ĸ�����");
			SCWIN32STATUS.put("1442", "���ڲ����Ӵ���");
			SCWIN32STATUS.put("1443", "GW_*������Ч");
			SCWIN32STATUS.put("1444", "�̱߳�ʶ����Ч");
			SCWIN32STATUS.put("1445", "�޷�����Ƕ��ĵ��ӿ�(MDI)���ڵ���Ϣ");
			SCWIN32STATUS.put("1446", "����ʽ�˵��Ѽ���");
			SCWIN32STATUS.put("1447", "����û�й�����");
			SCWIN32STATUS.put("1448", "��������ΧӦС��0x7FFF");
			SCWIN32STATUS.put("1449", "�޷���ָ���ķ�ʽ��ʾ��رմ���");
			SCWIN32STATUS.put("1450", "ϵͳ��Դ���㣬�޷����������ķ���");
			SCWIN32STATUS.put("1451", "ϵͳ��Դ���㣬�޷����������ķ���");
			SCWIN32STATUS.put("1452", "ϵͳ��Դ���㣬�޷����������ķ���");
			SCWIN32STATUS.put("1453", "���㣬�޷����������ķ���");
			SCWIN32STATUS.put("1454", "ϵͳ�������޷��������ķ���");
			SCWIN32STATUS.put("1455", "ҳ�潻���ļ�̫С���޷���ɴ������");
			SCWIN32STATUS.put("1456", "�Ҳ����˵���");
			SCWIN32STATUS.put("1500", "�¼���־�ļ���");
			SCWIN32STATUS.put("1501", "�޷����¼���־�ļ�������޷������¼���¼����");
			SCWIN32STATUS.put("1502", "�¼���־�ļ�����");
			SCWIN32STATUS.put("1503", "�¼���־�ļ������ζ�ȡʱ�ѷ����仯");
			SCWIN32STATUS.put("1700", "������Ч");
			SCWIN32STATUS.put("1701", "�󶨾�������ʹ���");
			SCWIN32STATUS.put("1702", "�󶨾����Ч");
			SCWIN32STATUS.put("1703", "��֧��   RPC   Э��˳��");
			SCWIN32STATUS.put("1704", "RPC   Э��������Ч");
			SCWIN32STATUS.put("1705", "�ַ�����ȫ��Ψһ��ʶ��(UUID)��Ч");
			SCWIN32STATUS.put("1706", "�յ�ĸ�ʽ��Ч");
			SCWIN32STATUS.put("1707", "�����ַ��Ч");
			SCWIN32STATUS.put("1708", "δ�ҵ��յ�");
			SCWIN32STATUS.put("1709", "��ʱ����ֵ��Ч");
			SCWIN32STATUS.put("1710", "�Ҳ����ö����ȫ��Ψһ��ʶ��(UUID)");
			SCWIN32STATUS.put("1711", "�ö����ȫ��Ψһ��ʶ��(UUID)�Ѿ�ע��");
			SCWIN32STATUS.put("1712", "��һ���͵�ȫ��Ψһ��ʶ��(UUID)�Ѿ�ע��");
			SCWIN32STATUS.put("1713", "RPC   ���������ڼ���");
			SCWIN32STATUS.put("1714", "��δע��Э��˳��");
			SCWIN32STATUS.put("1715", "RPC   �����������ڼ���״̬");
			SCWIN32STATUS.put("1716", "������������δ֪");
			SCWIN32STATUS.put("1717", "�ӿ�δ֪");
			SCWIN32STATUS.put("1718", "û�а�");
			SCWIN32STATUS.put("1719", "û��Э������");
			SCWIN32STATUS.put("1720", "�޷������յ�");
			SCWIN32STATUS.put("1721", "��Դ���㣬�޷���ɸò���");
			SCWIN32STATUS.put("1722", "RPC   �������޷�ʹ��");
			SCWIN32STATUS.put("1723", "RPC   ������̫æ���޷���ɴ������");
			SCWIN32STATUS.put("1724", "����ѡ����Ч");
			SCWIN32STATUS.put("1725", "���߳��в����ڻ��Զ�̹��̵���");
			SCWIN32STATUS.put("1726", "Զ�̹��̵���ʧ��");
			SCWIN32STATUS.put("1727", "Զ�̹��̵���ʧ�ܲ����޷�ִ��");
			SCWIN32STATUS.put("1728", "Զ�̹��̵���(RPC)Э����ִ���");
			SCWIN32STATUS.put("1730", "RPC   ��������֧�ִ����﷨");
			SCWIN32STATUS.put("1732", "��֧���������͵�ȫ��Ψһ��ʶ��");
			SCWIN32STATUS.put("1733", "��ʶ��Ч");
			SCWIN32STATUS.put("1734", "����߽���Ч");
			SCWIN32STATUS.put("1735", "�������в�������Ŀ��");
			SCWIN32STATUS.put("1736", "�����﷨��Ч");
			SCWIN32STATUS.put("1737", "��֧�����������﷨");
			SCWIN32STATUS.put("1739", "û�п��õ������ַ���޷�����ȫ��Ψһ��ʶ��(UUID)");
			SCWIN32STATUS.put("1740", "�ս���ظ�");
			SCWIN32STATUS.put("1741", "�����֤����δ֪");
			SCWIN32STATUS.put("1742", "���ô���������̫С");
			SCWIN32STATUS.put("1743", "�ַ���̫��");
			SCWIN32STATUS.put("1744", "�Ҳ���   RPC   Э������");
			SCWIN32STATUS.put("1745", "���̺ų�����Χ");
			SCWIN32STATUS.put("1746", "�˴ΰ󶨲������κ������֤��Ϣ");
			SCWIN32STATUS.put("1747", "�����֤����δ֪");
			SCWIN32STATUS.put("1748", "�����֤����δ֪");
			SCWIN32STATUS.put("1749", "��ȫ��������Ч");
			SCWIN32STATUS.put("1750", "�����֤����δ֪");
			SCWIN32STATUS.put("1751", "��Ŀ��Ч");
			SCWIN32STATUS.put("1752", "���������ս���޷�ִ�д������");
			SCWIN32STATUS.put("1753", "�յ��ӳ����û�и�����յ����");
			SCWIN32STATUS.put("1754", "û�е����κνӿ�");
			SCWIN32STATUS.put("1755", "��Ŀ��������");
			SCWIN32STATUS.put("1756", "�汾ѡ����Ч");
			SCWIN32STATUS.put("1757", "û��������Ա");
			SCWIN32STATUS.put("1758", "���Ե���ȫ������");
			SCWIN32STATUS.put("1759", "δ�ҵ��ӿ�");
			SCWIN32STATUS.put("1760", "��Ŀ�Ѿ�����");
			SCWIN32STATUS.put("1761", "��Ŀ�Ҳ���");
			SCWIN32STATUS.put("1762", "���Ʒ��񲻿���");
			SCWIN32STATUS.put("1763", "�����ַ����Ч");
			SCWIN32STATUS.put("1764", "��֧������Ĳ���");
			SCWIN32STATUS.put("1765", "û�пɹ�ð�µİ�ȫ��������");
			SCWIN32STATUS.put("1766", "Զ�̹��̵���(RPC)�����ڲ�����");
			SCWIN32STATUS.put("1767", "RPC ��������ͼ��������������");
			SCWIN32STATUS.put("1768", "RPC ����������Ѱַ����");
			SCWIN32STATUS.put("1769", "RPC �������еĸ���������������");
			SCWIN32STATUS.put("1770", "RPC �����������˸����������");
			SCWIN32STATUS.put("1771", "RPC �����������˸����������");
			SCWIN32STATUS.put("1772", "�������Զ�����󶨵�   RPC   �������б��Ѿ�����");
			SCWIN32STATUS.put("1773", "�޷����ַ�ת�����ļ�");
			SCWIN32STATUS.put("1774", "�����ַ�ת������ļ�С��512   ���ֽ�");
			SCWIN32STATUS.put("1775", "��Զ�̹��̵����У��ͻ���������������һ���յ���������");
			SCWIN32STATUS.put("1777", "Զ�̹��̵����е��������������仯");
			SCWIN32STATUS.put("1778", "���͵�Զ�̹��̵��õİ󶨾����ƥ��");
			SCWIN32STATUS.put("1779", "ռλ�����޷����Զ�̹��̵��õľ��");
			SCWIN32STATUS.put("1780", "���յĲο�ָ�뷢�͸�ռλ����");
			SCWIN32STATUS.put("1781", "�о�ֵ������Χ");
			SCWIN32STATUS.put("1782", "�ֽ���Ŀ̫С");
			SCWIN32STATUS.put("1783", "ռλ������յ���������");
			SCWIN32STATUS.put("1784", "���ṩ���û���������������Ĳ�����Ч");
			SCWIN32STATUS.put("1785", "�޷�ʶ�����ý�塣�����ܻ�δ��ʽ��");
			SCWIN32STATUS.put("1786", "����վû��ί������");
			SCWIN32STATUS.put("1787", "Windows NT �������ϵ� SAM ���ݿ�û�иù���վί�й�ϵ�ļ�����ʺ�");
			SCWIN32STATUS.put("1788", "�����������������ί�й�ϵʧ��");
			SCWIN32STATUS.put("1788", "��������վ��������ί�й�ϵʧ��");
			SCWIN32STATUS.put("1790", "�����¼ʧ��");
			SCWIN32STATUS.put("1791", "���߳�ִ�й������Ѿ�������Զ�̹��̵���");
			SCWIN32STATUS.put("1792", "��ͼ��¼���磬�������¼������δ����");
			SCWIN32STATUS.put("1793", "�û��ʺ��ѵ���");
			SCWIN32STATUS.put("1794", "�ض����������ʹ�ã��޷�ж��");
			SCWIN32STATUS.put("1795", "�Ѿ���װ��ָ���Ĵ�ӡ����������");
			SCWIN32STATUS.put("1796", "ָ���Ķ˿�δ֪");
			SCWIN32STATUS.put("1797", "��ӡ����������δ֪");
			SCWIN32STATUS.put("1798", "��ӡ�������δ֪");
			SCWIN32STATUS.put("1799", "ָ���ķָ����ļ���Ч");
			SCWIN32STATUS.put("1800", "ָ�������ȼ���Ч");
			SCWIN32STATUS.put("1801", "��ӡ������Ч");
			SCWIN32STATUS.put("1802", "��ӡ���Ѿ�����");
			SCWIN32STATUS.put("1803", "��ӡ��������Ч");
			SCWIN32STATUS.put("1804", "ָ��������������Ч");
			SCWIN32STATUS.put("1805", "ָ���Ļ���");

		}
	  
}
