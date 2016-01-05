package com.afunms.polling.snmp;

/*
 * Created on 2005-3-2
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

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
	 * �û�״̬������Ϣ
	 */
	public static Hashtable UserStatusConfig = null;
	static {
		UserStatusConfig = new Hashtable();
		UserStatusConfig.put("OK", "����");
		UserStatusConfig.put("Degraded", "�˻�");
	}
	
	/*
	 * �û�״̬������Ϣ
	 */
	public static Hashtable NetworkStatusConfig = null;
	static {
		NetworkStatusConfig = new Hashtable();
		NetworkStatusConfig.put("2", "����");
		NetworkStatusConfig.put("3", "����");
		NetworkStatusConfig.put("7", "����");
	}
	
	/*
	 * �û�״̬������Ϣ
	 */
	public static Hashtable UserLocalAccountConfig = null;
	static {
		UserLocalAccountConfig = new Hashtable();
		UserLocalAccountConfig.put("True", "��");
		UserLocalAccountConfig.put("False", "��");
	}
		/*
		 * Service ����ģʽ������Ϣ
		 */
		public static Hashtable ServiceStartModeConfig = null;
		static {
			ServiceStartModeConfig = new Hashtable();
			ServiceStartModeConfig.put("Disabled", "����");
			ServiceStartModeConfig.put("Manual", "�ֶ�");
			ServiceStartModeConfig.put("Auto", "�Զ�");
		}
		
		/*
		 * Service ״̬������Ϣ
		 */
		public static Hashtable ServiceStateConfig = null;
		static {
			ServiceStateConfig = new Hashtable();
			ServiceStateConfig.put("Running", "����");
			ServiceStateConfig.put("Stopped", "��ֹͣ");
		}
		
		/*
		 * Service ����������Ϣ
		 */
		public static Hashtable ServiceTypeConfig = null;
		static {
			ServiceTypeConfig = new Hashtable();
			ServiceTypeConfig.put("Share Process", "����");
			ServiceTypeConfig.put("Own Process", "��ռ");
		}
  
		/*
		 * WMI ����������Ϣ
		 */
	  public static Hashtable NetworkConfigName = null;
	  static {
		  NetworkConfigName = new Hashtable();
		  NetworkConfigName.put("IPAddress", "IP��ַ");
		  NetworkConfigName.put("Description", "����");
		  NetworkConfigName.put("Index", "����");
		  NetworkConfigName.put("IPSubnet", "��������");
		  NetworkConfigName.put("MACAddress", "MAC��ַ");
		  NetworkConfigName.put("DNSServerSearchOrder", "DNS");
		  NetworkConfigName.put("DefaultIPGateway", "����");
	  }
	  
	  /*
		 * WMI ����״̬��Ϣ
		 */
	  public static Hashtable NetworkStatusName = null;
	  static {
		  NetworkStatusName = new Hashtable();
		  NetworkStatusName.put("IPAddress", "IP��ַ");
		  NetworkStatusName.put("Index", "����");
		  NetworkStatusName.put("MACAddress", "MAC��ַ");
		  NetworkStatusName.put("Name", "����");
		  NetworkStatusName.put("NetConnectionStatus", "״̬");
	  }
	  
		/*
		 * WMI ����������Ϣ
		 */
	  public static Hashtable HostInfoName = null;
	  static {
		  HostInfoName = new Hashtable();
		  HostInfoName.put("TotalPhysicalMemory", "�����ڴ�");
		  HostInfoName.put("Hostname", "������");
		  HostInfoName.put("NumberOfProcessors", "����������");
		  HostInfoName.put("Sysname", "ϵͳ����");
		  HostInfoName.put("SerialNumber", "���к�");
		  HostInfoName.put("CSDVersion", "������");
		  HostInfoName.put("CPUname", "CPU");
	  }
	  
		/*
		 * WMI ����������Ϣ
		 */
	  public static Hashtable DiskConfigName = null;
	  static {
		  DiskConfigName = new Hashtable();
		  DiskConfigName.put("BytesPerSector", "ÿ�����ֽ���");
		  DiskConfigName.put("Caption", "����");
		  DiskConfigName.put("Index", "����");
		  DiskConfigName.put("InterfaceType", "����");
		  DiskConfigName.put("Size", "��С");
	  }
	  
		/*
		 * WMI CPU������Ϣ
		 */
	  public static Hashtable CPUName = null;
	  static {
		  CPUName = new Hashtable();
		  CPUName.put("ContextSwitchesPersec", "�����л���");
		  CPUName.put("ProcessorQueueLength", "���е��߳�����");
		  CPUName.put("SystemCallsPersec", "ϵͳ������");
	  }
	  
		/*
		 * WMI ������Ϣ
		 */
	  public static Hashtable ServiceName = null;
	  static {
		  ServiceName = new Hashtable();
		  ServiceName.put("DisplayName", "����");
		  ServiceName.put("State", "״̬");
		  ServiceName.put("ServiceType", "����");
		  ServiceName.put("Description", "����");
		  ServiceName.put("PathName", "·��");
		  ServiceName.put("StartMode", "����ģʽ");
	  }
	  
	  /*
		 * WMI ������Ϣ
		 */
	  public static Hashtable ProcessName = null;
	  static {
		  ProcessName = new Hashtable();
		  ProcessName.put("Caption", "��ʶ");
		  ProcessName.put("CreationDate", "����ʱ��");
		  ProcessName.put("Description", "����");
		  ProcessName.put("ExecutablePath", "ִ��·��");
		  ProcessName.put("Name", "����");
		  ProcessName.put("WorkingSetSize", "�ڴ��С");
		  ProcessName.put("Priority", "���ȼ�");
		  ProcessName.put("ProcessId", "����ID");
		  ProcessName.put("ThreadCount", "�߳�����");
		  ProcessName.put("KernelModeTime", "����̬ʱ��");
		  ProcessName.put("UserModeTime", "�û�̬ʱ��");
	  }
	  
	  /*
		 * WMI ���̷�����Ϣ
		 */
	  public static Hashtable DiskpartionName = null;
	  static {
		  DiskpartionName = new Hashtable();
		  DiskpartionName.put("Caption", "��ʶ");
		  DiskpartionName.put("VolumeName", "������");
		  DiskpartionName.put("FileSystem", "�ļ�ϵͳ");
		  DiskpartionName.put("FreeSpace", "���д�С");
		  DiskpartionName.put("Size", "��С");
	  }
	  
	  /*
		 * WMI ���̷�����Ϣ
		 */
	  public static Hashtable CPUConfigName = null;
	  static {
		  CPUConfigName = new Hashtable();
		  CPUConfigName.put("DataWidth", "����λ");
		  CPUConfigName.put("LoadPercentage", "������");
		  CPUConfigName.put("ProcessorId", "������ID");
		  CPUConfigName.put("Name", "����");
		  CPUConfigName.put("L2CacheSize", "���������С");
		  CPUConfigName.put("L2CacheSpeed", "���������ٶ�");
	  }
	  
	  /*
		 * WMI �ڴ�������Ϣ
		 */
	  public static Hashtable MemoryName = null;
	  static {
		  MemoryName = new Hashtable();
		  MemoryName.put("AvailableBytes", "���е������ڴ�����");
		  MemoryName.put("CommittedBytes", "������");
		  MemoryName.put("PageFaultsPersec", "�봦��Ĵ���ҳ/��");
		  MemoryName.put("PagesInputPersec", "����ҳ����/��");
		  MemoryName.put("PagesOutputPersec", "���ҳ����/�� ");
		  MemoryName.put("PagesPersec", "��ȡ��ҳ��/��");
	  }
	  
	  /*
		 * WMI �������������Ϣ
		 */
	  public static Hashtable PhysicalDiskinfoName = null;
	  static {
		  PhysicalDiskinfoName = new Hashtable();
		  PhysicalDiskinfoName.put("AvgDiskQueueLength", "ƽ�����г���");
		  PhysicalDiskinfoName.put("AvgDiskReadQueueLength", "ƽ�������г���");
		  PhysicalDiskinfoName.put("AvgDiskWriteQueueLength", "ƽ��д���г���");
		  PhysicalDiskinfoName.put("CurrentDiskQueueLength", "��ǰ���г���");
		  PhysicalDiskinfoName.put("DiskWriteBytesPersec", "����д�ֽ���/�� ");
		  PhysicalDiskinfoName.put("PercentDiskTime", "æʱ��");
	  }
	  
	  /*
		 * WMI ����ӿ�����������Ϣ
		 */
	  public static Hashtable NetworkInterfaceinfoName = null;
	  static {
		  NetworkInterfaceinfoName = new Hashtable();
		  NetworkInterfaceinfoName.put("BytesReceivedPersec", "�����ֽ���/��");
		  NetworkInterfaceinfoName.put("BytesSentPersec", "�����ֽ���/��");
		  NetworkInterfaceinfoName.put("BytesTotalPersec", "�ֽ�����/��");
		  NetworkInterfaceinfoName.put("CurrentBandwidth", "����");
		  NetworkInterfaceinfoName.put("Name", "����");
		  NetworkInterfaceinfoName.put("OutputQueueLength", "������г���");
		  NetworkInterfaceinfoName.put("PacketsOutboundErrors", "�������ݰ�");
		  NetworkInterfaceinfoName.put("PacketsPersec", "���ݰ�/��");
		  NetworkInterfaceinfoName.put("PacketsReceivedDiscarded", "�����Ľ��յ����ݰ�");
		  NetworkInterfaceinfoName.put("PacketsReceivedErrors", "���յĴ������ݰ�");
		  NetworkInterfaceinfoName.put("PacketsReceivedNonUnicastPersec", "���յķǵ������ݰ�/�� ");
		  NetworkInterfaceinfoName.put("PacketsReceivedPersec", "�������ݰ�/��");
		  NetworkInterfaceinfoName.put("PacketsReceivedUnicastPersec", "ƽ�������г���");
		  NetworkInterfaceinfoName.put("PacketsReceivedUnknown", "���յĲ�ʶ������ݰ�");
		  NetworkInterfaceinfoName.put("PacketsSentNonUnicastPersec", "���͵ķǵ������ݰ�/��");
		  NetworkInterfaceinfoName.put("PacketsSentPersec", "���͵����ݰ�/��");
		  NetworkInterfaceinfoName.put("PacketsSentUnicastPersec", "���͵ĵ������ݰ�/��");
	  }
	
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
	public static final String[]  NetWorkMibInterfaceUnit0={"","","","bit",
										"kb/s","","","",
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
	public static final String[] NetWorkMibInterfaceDesc1={"index","ifInOctets","ifInMulticastPkts","ifInBroadcastPkts",
											"ifInDiscards","ifInErrors","ifOutOctets",
											"ifOutMulticastPkts","ifOutBroadcastPkts","ifOutDiscards","ifOutErrors",
											"ifOutQLen","ifSpecific"
									};
	public static final String[] NetWorkMibInterfaceChname1={"�˿�����","���յ��ֽ�","���������ݰ�","�ǵ��������ݰ�",
											"�����������ݰ�","��վ�������ݰ�","��վ��֪�������ݰ�","������ֽ�",
											"���������ݰ�","�ǵ��������ݰ�","��վ�����������ݰ�","��վ����ʧ�ܵ����ݰ�",
											"�����Ϣ�����еĳ���","Mib�Զ˿ڵ�˵��"
									};
	public static final String[] NetWorkMibInterfaceDesc3={"index","ifInMulticastPkts","ifInBroadcastPkts","ifOutMulticastPkts",
												"ifOutBroadcastPkts"
									};
	public static final String[] NetWorkMibInterfaceChname3={"�˿�����","���յĶಥ���ݰ���Ŀ","���յĹ㲥���ݰ���Ŀ","���͵Ķಥ���ݰ���Ŀ",
											"���͵Ĺ㲥���ݰ���Ŀ"
									};
	public static final String[] NetWorkMibInterfaceDesc2={"ifType","ifInOctets","ifOutOctets"};
	public static final String[] NetWorkMibInterfaceChname2={"����","���յ��ֽ�","������ֽ�"
									};
	public static final String[] NetWorkMibInterfaceUnit1={"kb","","",
											"","","kb",
											"","","","",
											"",""
									};
	public static final int[] NetWorkMibInterfaceScale1={1024,0,1,
											0,0,1024,
											0,0,0,0,
											0,0
									};
	public static final String[] NetWorkMibInterfaceUnit2={"","KB","KB"};
public static final int[] NetWorkMibInterfaceScale2={1,1024,1024};

	public static final String[] NetWorkMibSystemDesc={"sysDescr","sysUpTime","sysContact","sysName","sysLocation","sysServices"
														};
	public static final String[] NetWorkMibSystemChname={"����","����ʱ��","��ϵ��","�豸����","�豸λ��","��������"
			
	};
	public static final String[] UpsMibStatueChname={"��������","����̨��","����ϵͳA��������й�����","����ϵͳB��������й�����","����ϵͳC��������й�����",
        "����ϵͳA����������ڹ���","����ϵͳA����������ڹ���","����ϵͳA����������ڹ���",
        "����ϵͳA��������޹�����","����ϵͳA��������޹�����","����ϵͳA��������޹�����"};
	public static final String[] UpsMibOutputChname={"����ߵ�ѹA","����ߵ�ѹB","����ߵ�ѹC","A���������","B���������",
        "C���������","���Ƶ��","A�������������","B�������������","C�������������",
        "A������й�����","B������й�����","C������й�����","A��������ڹ���","B��������ڹ���","C��������ڹ���",
        "A������޹�����","B������޹�����","C������޹�����",
         "A��������ذٷֱ�","B��������ذٷֱ�","C��������ذٷֱ�",
			"A�������ֵ��","B�������ֵ��","C�������ֵ��"};

	public static final String[] UpsMibSystemDesc={"sysmodel","sysname","sysfirmware","sysDate","sysSerial ","sysFactory"};
	public static final String[] UpsMibInputDesc={"SRXDYAB","SRXDYBC","SRXDYCA","AXSRDY","BXSRDY","CXSRDY","AXSRDL","BXSRDL","CXSRDL","SRPL","SRGLYSA","SRGLYSB","SRGLYSC"};
	public static final String[] UpsMibInputChname={"�����ߵ�ѹAB","�����ߵ�ѹBC","�����ߵ�ѹCA","A�������ѹ","B�������ѹ","C�������ѹ","A���������","B���������","C���������","����Ƶ��","���빦������A","���빦������B","���빦������C"};
	public static final String[] UpsMibAlarmDesc={"INDEX","ALARMTIME","ALARMID","NAME","LEVEL"};
	public static final String[] UpsMibAlarmChname={"����","�澯ʱ��","�澯�¼�ID","�¼�����","�ȼ�"};
	public static final String[] UpsMibBypassDesc={"AXPLDY","BXPLDY","CXPLDY","PLPL"};
    public static final String[] UpsMibBypassChname={"A����·��ѹ","B����·��ѹ","C����·��ѹ","��·Ƶ��"};
    public static final String[] UpsMibBypassUnit={"V","V","V","Hz"};
    public static final String[] UpsMibBatteryUnit={"V","A","min","���϶�","���϶�","",""};
    public static final String[] UpsMibStatueUnit={"","","kW","kW","kW","kVA","kVA","kVA","KVAR","KVAR","KVAR"};
    public static final String[] UpsMibInputUnit={"V","V","V","V","V","V","A","A","A","Hz","Hz","Hz","","",""};
    public static final String[] UpsMibOutputUnit={"V","V","V","A","A","A","HZ","","","","kW","kW","kW","kVA","kVA","kVA","KVAR","KVAR","KVAR","%","%","%","","",""};
    public static final String[] UpsMibOutputDesc={"SCXDYA","SCXDYB","SCXDYC","AXSCDL","BXSCDL",
		                                             "CXSCDL","SCPL","AXSCGLYS","BXSCGLYS","CXSCGLYS",
		                                             "AXSCYGGL","BXSCYGGL","CXSCYGGL","AXSCSZGL","BXSCSZGL","CXSCSZGL",
		                                             "AXSCWGGL","BXSCWGGL","CXSCWGGL",
		                                             "AXSCFZBFB","BXSCFZBFB","CXSCFZBFB",
														"AXSCFZB","BXSCFZB","CXSCFZB"};
	
	public static final String[] UpsMibStatueDesc={"JXRL","BJTH","BJXTAXSCZYGGL","BJXTBXSCZYGGL","BJXTCXSCZYGGL","BJXTAXSCZSZGL","BJXTBXSCZSZGL",
        "BJXTCXSCZSZGL","BJXTAXSCZWGGL","BJXTBXSCZWGGL","BJXTCXSCZWGGL"};
public static final String[] UpsMibBatteryChname={"��ص�ѹ","��ص���","���ʣ���ʱ��","����¶�","�����¶�","�豸����","�豸����"};
public static final String[] UpsMibBatteryDesc={"DCDY","DCDL","DCSYHBSJ","DCWD","HJWD","SBMS","SBMC"};
public static final String[] NetWorkMibHardwareDesc={"entPhysicalDescr1","entPhysicalDescr2","entPhysicalDescr3","entPhysicalDescr4"};
public static final String[] NetWorkMibHardwareChname={"����1","����2","����3","����4"};
public static final String[] NetWorkMibCapabilityDesc={"PortName","PortPhysAdminStatus","PortPhysOperStatus","PortC3InFrames","PortC3OutFrames","PortC3InOctets","PortC3OutOctets","PortC3Discards"};
public static final String[] NetWorkMibCapabilityChname={"�������","״̬","��ǰ״̬","���֡��","����֡��","���֡�ֽ���","����֡�ֽ���","������"};
public static final String[]  NetWorkMibCapabilityUnit0={"","","","֡","֡","KB/��","KB/��","��"};
public static final int[]  NetWorkMibCapabilityScale0={0,0,0,1,1,1024*8,1024*8,1};
public static final String[] NetWorkMibCapabilityDesc1={"PortName","PortPhysAdminStatus","PortPhysOperStatus","PortOlsIns","PortOlsOuts","PortC3InFrames","PortC3OutFrames","PortC3InOctets","PortC3OutOctets","PortC3Discards"};
public static final String[] NetWorkMibCapabilityChname1={"�������","״̬","��ǰ״̬","PortOlsIns","PortOlsOuts","���֡��","����֡��","���֡�ֽ���","����֡�ֽ���","������"};
public static final String[]  NetWorkMibCapabilityUnit1={"","","","","","֡","֡","KB/��","KB/��","��"};
public static final int[]  NetWorkMibCapabilityScale1={0,0,0,1,1,1,1,1024*8,1024*8,1};
public static final String[] NetWorkMibConfigDesc={"FabricName","ElementName","ModuleCapacity","ModuleDescr","FeModuleObjectID","ModuleOperStatus","UPTime","ModuleFxPortCapacity","ModuleName"};
public static final String[] NetWorkMibConfigChname={"����","����Ԫ������","����ģ����������","ģ������","ģ��OID","ģ��״̬","ģ����������ʱ��","�����","ģ������"};

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
	  
	  
	  
}
