package com.afunms.common.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.application.model.MQConfig;
import com.afunms.application.util.RemoteClientInfo;
import com.afunms.polling.task.MonitorTimer;


public class ShareData {

  //public static int classCount = 0;  // shared by all instances
  public static int count = 0;              // separate for each servlet
  public static int firstipmac =0;
  //public static Hashtable instances = new Hashtable();  // also shared
  public static Hashtable sharedata = new Hashtable();  // also shared
  public static Hashtable pingdata = new Hashtable();  // also shared
  public static Hashtable iispingdata = new Hashtable();  // also shared
  public static Hashtable portdata = new Hashtable();  // all port data
  public static Hashtable hostdata = new Hashtable();  // all host and network
  public static Hashtable GSNdata = new Hashtable();  //�㽭�ƶ�ר���豸
  //��������Ϣ
  public static Hashtable memdata = new Hashtable();  // �������ڴ桢������Ϣ�Ķ����¼�
  public static Hashtable sendeddata = new Hashtable();  // all host and network
  public static Hashtable createeventdata = new Hashtable();  // all host and network
  public static Hashtable portsendeddata = new Hashtable();  // all host and network
  public static Hashtable memsendeddata = new Hashtable();  // all host and network
  public static Hashtable procsendeddata = new Hashtable();  // ������PROCS���̶�ʧ�Ķ����¼�
  public static Hashtable filesizedata = new Hashtable();  // all host and network
  public static Hashtable allpingdata = new Hashtable();  // all host and network
  public static Hashtable alldbdata = new Hashtable();  // all database
  public static Hashtable allportdata = new Hashtable();  // all host and network
  public static Hashtable packsdata = new Hashtable();  // all host and network
  public static Hashtable discardsdata = new Hashtable();  // all host and network
  public static Hashtable errorsdata = new Hashtable();  // all host and network
  public static Hashtable octetsdata = new Hashtable();  // �����ֽ�
  public static Hashtable realoctetsdata = new Hashtable();  // �����ֽ�
  public static Hashtable pksdata = new Hashtable();  // all host and network
  public static Hashtable eventdata = new Hashtable();  // all host and network
  public static Hashtable sameequipsmsdata = new Hashtable();  // all host and network
  //�ڵ��ַPINGֵ
  public static Hashtable<String ,Vector> relateippingdata = new Hashtable<String ,Vector>();  // all host and network
  //ipmac
  public static Hashtable relateipmacdata = new Hashtable(); 
  public static Hashtable relatefdbipmacdata = new Hashtable(); 
  public static Hashtable ipmacbanddata = new Hashtable();  
  public static Hashtable fdbipmacbanddata = new Hashtable();
  public static Hashtable iprouterdata = new Hashtable();  // all host and network
  public static Hashtable policydata = new Hashtable();  // all host and network
  public static Hashtable tosroutedata = new Hashtable();  // all host and network
  public static Hashtable dominodata = new Hashtable();  // all host and network
  public static Hashtable iisdata = new Hashtable();  // �洢IIS�ɼ���������Ϣ
  public static Hashtable sysbasedata = new Hashtable();  // �洢SYSBASE�ɼ���������Ϣ
  public static Hashtable lostprocdata = new Hashtable();  // �洢�Ѿ���ʧ�Ľ�����Ϣ
  public static Hashtable sqlserverdata = new Hashtable();  // �洢sqlserver�ɼ���������Ϣ
  public static Hashtable mqdata = new Hashtable();  // �洢mq�ɼ���������Ϣ
  public static Hashtable weblogicdata = new Hashtable();  // �洢weblogic�ɼ���������Ϣ
  public static Hashtable tomcatdata = new Hashtable();  // �洢tomcat�ɼ���������Ϣ
  public static Hashtable ibmstoragedata = new Hashtable();  // �洢IBMStorage�ɼ���������Ϣ
  public static Hashtable alldiskalarmdata = new Hashtable();  // �洢DISK�Ƿ�澯������Ϣ
  public static Hashtable allmqalarmdata = new Hashtable();  // �洢MQ�Ƿ�澯������Ϣ
  public static Hashtable allwlserveralarmdata = new Hashtable();  // �洢websphere�Ƿ�澯������Ϣ
  public static Hashtable alldb2data = new Hashtable();  // �洢DB2�ɼ���������Ϣ
  public static Hashtable alloracledata = new Hashtable();  // �洢ORACLE�ɼ���������Ϣ
  public static Hashtable oraspacedata = new Hashtable();  // �洢ORACLE��ռ�ɼ���������Ϣ
  public static Hashtable alloraspacealarmdata = new Hashtable();  // �洢ORASPACE�Ƿ�澯������Ϣ
  
  public static Hashtable informixspacedata = new Hashtable();  // �洢INFORMIX��ռ�ɼ���������Ϣ
  public static Hashtable allinformixspacealarmdata = new Hashtable();  // �洢INFORMIXSPACE�Ƿ�澯������Ϣ
  
  public static Hashtable db2type6spacedata = new Hashtable();  // �洢ORASPACE�Ƿ�澯������Ϣ
  public static Hashtable wasdata = new Hashtable();  // �洢Was�ɼ���������Ϣ
  public static Hashtable sqldbdata = new Hashtable();  // �洢sqldb�ɼ���������Ϣ
  //public static Hashtable sqldbdata = new Hashtable();  // �洢sqldb�ɼ���������Ϣ
  public static Hashtable emersondata = new Hashtable();  // �洢�յ��ɼ���������Ϣ
  public static Hashtable allsyslogconfdata = new Hashtable();  // �洢�յ��ɼ���������Ϣ
  public static Hashtable alllegatodata = new Hashtable();  // �洢�յ��ɼ���������Ϣ
  public static Hashtable alltuxedodata = new Hashtable();  // �洢�յ��ɼ���������Ϣ
  
  public static Hashtable mysqldata = new Hashtable();  // �洢mysql�ɼ���������Ϣ
  public static Hashtable mysqlmonitordata = new Hashtable();  // �洢mysql�ɼ���������Ϣ
  
  
  public static Hashtable allstoragedata = new Hashtable();  // �洢�ɼ���������Ϣ   nielin add 2010-06-28
  
  public static Hashtable checkeventdata = new Hashtable(); //�洢ָ��ԱȺ�澯����Ϣ
  public static Hashtable subscribeReportHash = new Hashtable();//�洢���������õ�TimerTask��keyΪnms_cycle_report_config��id��valueΪ�洢TimerTask��List
  
  private static Hashtable allLinkData = new Hashtable();//��·��Ϣ  HONGLI 2011-04-13
  private static Hashtable allLinknodeInterfaceData = new Hashtable();//��·�ڵ�ӿ����ݼ��� HONGLI 2011-04-13
  
  public static Hashtable nodereportData = new Hashtable();	//wupinlong for fsgdj 2013-05-24
  public static Hashtable tru64netstatData = new Hashtable();	//wupinlong for tru64netstat ���ڴ洢��һ�ζ˿���������С���Ա�������٣�Tru64NetstatByLogFile��
  
  /**
   * �洢���д����ݿ���ȡ�ģ������豸���߷��������Ĳ�������  
   * <p>�ɲμ�NetworkDao.java collectAllNetworkData���� ���ڸ�������ͼUpdateXmlTaskʱ �洢���ݿ���ʵʱ����</p>
   * @author HONGLI 2011-04-19
   */
  private static Hashtable allNetworkData = new Hashtable();  
  
  /**
   * �洢���д����ݿ���ȡ�ģ������豸���߷�����������ͨ������  
   */
  private static Hashtable allNetworkPingData = new Hashtable();
  
  public static Hashtable connectConfigHashtable = new Hashtable(); //pingsnmp  
  
  public static Hashtable portConfigHash = new Hashtable(); //��Ŷ˿�����
  
  public static Hashtable gatherHash = new Hashtable(); //��Ŷ˿�����
  
  public static Hashtable checkEventHash = new Hashtable(); //��Ŷ˿�����
  
  public static Hashtable paramsHash = new Hashtable(); //TELNET����
  
  /**
   * ���вɼ���DNS������Ϣ
   */
  private static Hashtable allDnsData = new Hashtable();
  

  /**
   * ���ÿ5���Ӳɼ���Timer����
   * String:nodeid
   * MonitorTimer:��node��Ӧ��Timer
   */
  private static HashMap<String,MonitorTimer> m5TimerMap = new HashMap<String, MonitorTimer>();
  
  /**
   * ���ÿ5���Ӳɼ��������ݼ���
   */
  private static Hashtable m5Alldata = new Hashtable();
  
  /**
   * ���ÿ5���Ӳɼ��������У��Ѿ��ɼ�������
   */
  private static int m5CollectedSize;
  
  /**
   * ���ÿ10���Ӳɼ���Timer����
   * String:nodeid
   * MonitorTimer:��node��Ӧ��Timer
   */
  private static HashMap<String,MonitorTimer> m10TimerMap = new HashMap<String, MonitorTimer>();
  
  /**
   * ���ÿ10���Ӳɼ��������ݼ���
   */
  private static Hashtable m10Alldata = new Hashtable();
  
  /**
   * ���ÿ10���Ӳɼ��������У��Ѿ��ɼ�������
   */
  private static int m10CollectedSize;
  
  /**
   * ���ÿ30���Ӳɼ���Timer����
   * String:nodeid
   * MonitorTimer:��node��Ӧ��Timer
   */
  private static HashMap<String,MonitorTimer> m30TimerMap = new HashMap<String, MonitorTimer>();
  
  /**
   * ���ÿ30���Ӳɼ��������ݼ���
   */
  private static Hashtable m30Alldata = new Hashtable();
  
  /**
   * ���ÿ30���Ӳɼ��������У��Ѿ��ɼ�������
   */
  private static int m30CollectedSize;
  
  /**
   * ���ÿ1Сʱ�ɼ���Timer����
   * String:nodeid
   * MonitorTimer:��node��Ӧ��Timer
   */
  private static HashMap<String,MonitorTimer> h1TimerMap = new HashMap<String, MonitorTimer>();
  
  /**
   * ���ÿСʱ�ɼ��������ݼ���
   */
  private static Hashtable h1Alldata = new Hashtable();
  
  /**
   * ���ÿСʱ�ɼ��������У��Ѿ��ɼ�������
   */
  private static int h1CollectedSize;
  
  /**
   * ���ÿ��ɼ���Timer����
   * String:nodeid
   * MonitorTimer:��node��Ӧ��Timer
   */
  private static HashMap<String,MonitorTimer> d1TimerMap = new HashMap<String, MonitorTimer>();
  
  /**
   * ���ÿ��ɼ��������ݼ���
   */
  private static Hashtable d1Alldata = new Hashtable();
  
  /**
   * ���ÿ��ɼ��������У��Ѿ��ɼ�������
   */
  private static int d1CollectedSize;
  
  
  
  /**
   * ���ÿ5������SNMP�ɼ���ʽ�������ɼ���Timer����
   * String:nodeid
   * MonitorTimer:��node��Ӧ��Timer
   */
  private static HashMap<String,MonitorTimer> m5HostTimerMap = new HashMap<String, MonitorTimer>();
  
  /**
   * ��ŷ�����ÿ5���ӷ������ɼ��������ݼ���
   */
  private static Hashtable m5HostAlldata = new Hashtable();
  
  /**
   * ���ÿ5���ӷ������ɼ��������У��Ѿ��ɼ�������
   */
  private static int m5HostCollectedSize; 
  
  
  /**
   * ���ÿ10������SNMP�ɼ���ʽ�������ɼ���Timer����
   * String:nodeid
   * MonitorTimer:��node��Ӧ��Timer
   */
  private static HashMap<String,MonitorTimer> m10HostTimerMap = new HashMap<String, MonitorTimer>();
  
  /**
   * ��ŷ�����ÿ10���ӷ������ɼ��������ݼ���
   */
  private static Hashtable m10HostAlldata = new Hashtable();
  
  /**
   * ���ÿ10���ӷ������ɼ��������У��Ѿ��ɼ�������
   */
  private static int m10HostCollectedSize; 
  
  /**
   * ���ÿ30������SNMP�ɼ���ʽ�������ɼ���Timer����
   * String:nodeid
   * MonitorTimer:��node��Ӧ��Timer
   */
  private static HashMap<String,MonitorTimer> m30HostTimerMap = new HashMap<String, MonitorTimer>();
  
  /**
   * ��ŷ�����ÿ30���ӷ������ɼ��������ݼ���
   */
  private static Hashtable m30HostAlldata = new Hashtable();
  
  /**
   * ���ÿ30���ӷ������ɼ��������У��Ѿ��ɼ�������
   */
  private static int m30HostCollectedSize; 
  
  /**
   * ���ÿ1Сʱ��SNMP�ɼ���ʽ�������ɼ���Timer����
   * String:nodeid
   * MonitorTimer:��node��Ӧ��Timer
   */
  private static HashMap<String,MonitorTimer> h1HostTimerMap = new HashMap<String, MonitorTimer>();
  
  /**
   * ��ŷ�����ÿ1Сʱ�������ɼ��������ݼ���
   */
  private static Hashtable h1HostAlldata = new Hashtable();
  
  /**
   * ���ÿСʱ�������ɼ��������У��Ѿ��ɼ�������
   */
  private static int h1HostCollectedSize; 
  
  /**
   * ���ÿ1����SNMP�ɼ���ʽ�������ɼ���Timer����
   * String:nodeid
   * MonitorTimer:��node��Ӧ��Timer
   */
  private static HashMap<String,MonitorTimer> d1HostTimerMap = new HashMap<String, MonitorTimer>();
  
  /**
   * ��ŷ�����ÿ1��������ɼ��������ݼ���
   */
  private static Hashtable d1HostAlldata = new Hashtable();
  
  /**
   * ���ÿ��������ɼ��������У��Ѿ��ɼ�������
   */
  private static int d1HostCollectedSize; 
  
  /**
   * ��ż����·�ɼ���Timer����
   * String:linkid
   * MonitorTimer:��node��Ӧ��Timer
   */
  private static HashMap<String,MonitorTimer> linkTimerMap = new HashMap<String, MonitorTimer>();
  
  /**
   * ���ÿ��·�ɼ��������ݼ���
   */
  private static Hashtable linkAlldata = new Hashtable();
  
  /**
   * �����·�ɼ��������У��Ѿ��ɼ�������
   */
  private static int linkCollectedSize; 
  
  /**
   * ������ɼ���Timer����
   * String:linkid
   * MonitorTimer:��node��Ӧ��Timer
   */
  private static HashMap<String,MonitorTimer> panelTimerMap = new HashMap<String, MonitorTimer>();
  /**
   * ������ɼ��������У��Ѿ��ɼ�������
   */
  private static int panelCollectedSize;
  
  /**
   * ����豸���µ�Timer����
   * Host 
   * MonitorTimer:��node��Ӧ��Timer
   */
  private static HashMap<String,MonitorTimer> pollTimerMap = new HashMap<String, MonitorTimer>();
  /**
   * ����豸���µ������У��Ѿ��ɼ�������
   */
  private static int pollCollectedSize; 
  
  /**
   * ���XML���µ�Timer����
   * Host 
   * MonitorTimer:��node��Ӧ��Timer
   */
  private static HashMap<String,MonitorTimer> xmlTimerMap = new HashMap<String, MonitorTimer>();
  /**
   * ���XML���µ������У��Ѿ��ɼ�������
   */
  private static int xmlCollectedSize; 
  
  /**
   * ���ÿ5�����ô���ɼ���ʽ�������ɼ���Timer����
   * String:nodeid
   * MonitorTimer:��node��Ӧ��Timer
   */
  private static HashMap<String,MonitorTimer> m5AgentHostTimerMap = new HashMap<String, MonitorTimer>();
  
  /**
   * ��ŷ�����ÿ5����AGENT�������ɼ��������ݼ���
   */
  private static Hashtable m5AgentHostAlldata = new Hashtable();
  
  /**
   * ���ÿ5���ӷ������ɼ��������У��Ѿ��ɼ�������
   */
  private static int m5AgentHostCollectedSize; 
  private static Hashtable<String,RemoteClientInfo> ip_clientInfoHash = new Hashtable<String,RemoteClientInfo>();
  
  /**
   * ������
   */
  private static Object obj = null;
  
  public synchronized static Object getInstanceOfObject(){
	  if(obj == null){
		  obj = new Object();
	  }
	  return obj;
  }
  
  public static int getM5AgentHostCollectedSize() {
		return m5AgentHostCollectedSize;
	}

	public static void setM5AgentHostCollectedSize(Integer collectedSize) {
		synchronized(getInstanceOfObject()){
			m5AgentHostCollectedSize = collectedSize;
		}
	}

	/**
	 * m5CollectedSize����1
	 */
	public static int addM5AgentHostCollectedSize(){
		synchronized(getInstanceOfObject()){
			m5AgentHostCollectedSize = m5AgentHostCollectedSize + 1;
		}
		return m5AgentHostCollectedSize;
	}

	public static Hashtable getM5AgentHostAlldata() { 
		return m5AgentHostAlldata;
	}

	public static void setM5AgentHostAlldata(Hashtable alldata) {
		m5AgentHostAlldata = alldata;
	}

	public static HashMap<String, MonitorTimer> getM5AgentHostTimerMap() {
			return m5AgentHostTimerMap;
	  }

		public static void setM5AgentHostTimerMap(HashMap<String, MonitorTimer> timerMap) {
			m5AgentHostTimerMap = timerMap;
		}
  
  public static int getXmlCollectedSize() {
		return xmlCollectedSize;
	}

	public static void setXmlCollectedSize(Integer collectedSize) {
		synchronized(getInstanceOfObject()){
			xmlCollectedSize = collectedSize;
		}
	}

	/**
	 * xmlCollectedSize����1
	 */
	public static int addXmlCollectedSize(){
		synchronized(getInstanceOfObject()){
			xmlCollectedSize = xmlCollectedSize + 1;
		}
		return xmlCollectedSize;
	}
	
	public static HashMap<String, MonitorTimer> getXmlTimerMap() {
		return xmlTimerMap;
}

	public static void setXmlTimerMap(HashMap<String, MonitorTimer> timerMap) {
		xmlTimerMap = timerMap;
	}
	
  
  public static int getPollCollectedSize() {
		return pollCollectedSize;
	}

	public static void setPollCollectedSize(Integer collectedSize) {
		synchronized(getInstanceOfObject()){
			pollCollectedSize = collectedSize;
		}
	}

	/**
	 * PollCollectedSize����1
	 */
	public static int addPollCollectedSize(){
		synchronized(getInstanceOfObject()){
			pollCollectedSize = pollCollectedSize + 1;
		}
		return pollCollectedSize;
	}
	
	public static HashMap<String, MonitorTimer> getPollTimerMap() {
		return pollTimerMap;
}

	public static void setPollTimerMap(HashMap<String, MonitorTimer> timerMap) {
		pollTimerMap = timerMap;
	}
  
  public static int getPanelCollectedSize() {
		return panelCollectedSize;
	}

	public static void setPanelCollectedSize(Integer collectedSize) {
		synchronized(getInstanceOfObject()){
			panelCollectedSize = collectedSize;
		}
	}

	/**
	 * panelCollectedSize����1
	 */
	public static int addPanelCollectedSize(){
		synchronized(getInstanceOfObject()){
			panelCollectedSize = panelCollectedSize + 1;
		}
		return panelCollectedSize;
	}
	
	public static HashMap<String, MonitorTimer> getPanelTimerMap() {
		return panelTimerMap;
  }

	public static void setPanelTimerMap(HashMap<String, MonitorTimer> timerMap) {
		panelTimerMap = timerMap;
	}
	
  
  public static int getLinkCollectedSize() {
		return linkCollectedSize;
	}

	public static void setLinkCollectedSize(Integer collectedSize) {
		synchronized(getInstanceOfObject()){
			linkCollectedSize = collectedSize;
		}
	}

	/**
	 * linkCollectedSize����1
	 */
	public static int addLinkCollectedSize(){
		synchronized(getInstanceOfObject()){
			linkCollectedSize = linkCollectedSize + 1;
		}
		return linkCollectedSize;
	}

	public static Hashtable getLinkAlldata() { 
		return linkAlldata;
	}

	public static void setLinkAlldata(Hashtable alldata) {
		linkAlldata = alldata;
	}

	public static HashMap<String, MonitorTimer> getLinkTimerMap() {
			return linkTimerMap;
	  }

		public static void setLinkTimerMap(HashMap<String, MonitorTimer> timerMap) {
			linkTimerMap = timerMap;
		}  
  
public static int getM5CollectedSize() {
	return m5CollectedSize;
}

public static void setM5CollectedSize(Integer collectedSize) {
	synchronized(getInstanceOfObject()){
		m5CollectedSize = collectedSize;
	}
}

/**
 * m5CollectedSize����1
 */
public static int addM5CollectedSize(){
	synchronized(getInstanceOfObject()){
		m5CollectedSize = m5CollectedSize + 1;
	}
	return m5CollectedSize;
}

public static Hashtable getM5Alldata() { 
	return m5Alldata;
}

public static void setM5Alldata(Hashtable alldata) {
	m5Alldata = alldata;
}

public static HashMap<String, MonitorTimer> getM5TimerMap() {
		return m5TimerMap;
  }

	public static void setM5TimerMap(HashMap<String, MonitorTimer> timerMap) {
		m5TimerMap = timerMap;
	}
	
	/**
	 * 10���Ӳɼ�
	 * @return
	 */
	public static int getM10CollectedSize() {
		return m10CollectedSize;
	}

	public static void setM10CollectedSize(Integer collectedSize) {
		synchronized(getInstanceOfObject()){
			m10CollectedSize = collectedSize;
		}
	}

	/**
	 * m10CollectedSize����1
	 */
	public static int addM10CollectedSize(){
		synchronized(getInstanceOfObject()){
			m10CollectedSize = m10CollectedSize + 1;
		}
		return m10CollectedSize;
	}

	public static Hashtable getM10Alldata() { 
		return m10Alldata;
	}

	public static void setM10Alldata(Hashtable alldata) {
		m10Alldata = alldata;
	}

	public static HashMap<String, MonitorTimer> getM10TimerMap() {
			return m10TimerMap;
	  }

		public static void setM10TimerMap(HashMap<String, MonitorTimer> timerMap) {
			m10TimerMap = timerMap;
		}
		
		/**
		 * 30���Ӳɼ�
		 * @return
		 */
		public static int getM30CollectedSize() {
			return m30CollectedSize;
		}

		public static void setM30CollectedSize(Integer collectedSize) {
			synchronized(getInstanceOfObject()){
				m30CollectedSize = collectedSize;
			}
		}

		/**
		 * m10CollectedSize����1
		 */
		public static int addM30CollectedSize(){
			synchronized(getInstanceOfObject()){
				m30CollectedSize = m30CollectedSize + 1;
			}
			return m30CollectedSize;
		}

		public static Hashtable getM30Alldata() { 
			return m30Alldata;
		}

		public static void setM30Alldata(Hashtable alldata) {
			m30Alldata = alldata;
		}

		public static HashMap<String, MonitorTimer> getM30TimerMap() {
				return m30TimerMap;
		  }

			public static void setM30TimerMap(HashMap<String, MonitorTimer> timerMap) {
				m30TimerMap = timerMap;
			}	
			
			
			/**
			 * 1Сʱ�ɼ�
			 * @return
			 */
			public static int getH1CollectedSize() {
				return h1CollectedSize;
			}

			public static void setH1CollectedSize(Integer collectedSize) {
				synchronized(getInstanceOfObject()){
					h1CollectedSize = collectedSize;
				}
			}

			/**
			 * h1CollectedSize����1
			 */
			public static int addH1CollectedSize(){
				synchronized(getInstanceOfObject()){
					h1CollectedSize = h1CollectedSize + 1;
				}
				return h1CollectedSize;
			}

			public static Hashtable getH1Alldata() { 
				return h1Alldata;
			}

			public static void setH1Alldata(Hashtable alldata) {
				h1Alldata = alldata;
			}

			public static HashMap<String, MonitorTimer> getH1TimerMap() {
					return h1TimerMap;
			  }

				public static void setH1TimerMap(HashMap<String, MonitorTimer> timerMap) {
					h1TimerMap = timerMap;
				}
				
		/**
		* 1��ɼ�
		* @return
		*/
		public static int getD1CollectedSize() {
			return d1CollectedSize;
		}

		public static void setD1CollectedSize(Integer collectedSize) {
			synchronized(getInstanceOfObject()){
				d1CollectedSize = collectedSize;
			}
		}

		/**
		* d1CollectedSize����1
		*/
		public static int addD1CollectedSize(){
			synchronized(getInstanceOfObject()){
				d1CollectedSize = d1CollectedSize + 1;
			}
			return d1CollectedSize;
		}

		public static Hashtable getD1Alldata() { 
			return d1Alldata;
		}

		public static void setD1Alldata(Hashtable alldata) {
			d1Alldata = alldata;
		}

		public static HashMap<String, MonitorTimer> getD1TimerMap() {
			return d1TimerMap;
		}

		public static void setD1TimerMap(HashMap<String, MonitorTimer> timerMap) {
			d1TimerMap = timerMap;
		}
		
		/**
		* ������5���Ӳɼ�
		* @return
		*/
		public static int getM5HostCollectedSize() {
			return m5HostCollectedSize;
		}

		public static void setM5HostCollectedSize(Integer collectedSize) {
			synchronized(getInstanceOfObject()){
				m5HostCollectedSize = collectedSize;
			}
		}

		/**
		* m5HostCollectedSize����1
		*/
		public static int addM5HostCollectedSize(){
			synchronized(getInstanceOfObject()){
				m5HostCollectedSize = m5HostCollectedSize + 1;
			}
			return m5HostCollectedSize;
		}

		public static Hashtable getM5HostAlldata() { 
			return m5HostAlldata;
		}

		public static void setM5HostAlldata(Hashtable alldata) {
			m5HostAlldata = alldata;
		}

		public static HashMap<String, MonitorTimer> getM5HostTimerMap() {
			return m5HostTimerMap;
		}

		public static void setM5HostTimerMap(HashMap<String, MonitorTimer> timerMap) {
			m5HostTimerMap = timerMap;
		}		
		
		/**
		* ������10���Ӳɼ�
		* @return
		*/
		public static int getM10HostCollectedSize() {
			return m10HostCollectedSize;
		}

		public static void setM10HostCollectedSize(Integer collectedSize) {
			synchronized(getInstanceOfObject()){
				m10HostCollectedSize = collectedSize;
			}
		}

		/**
		* m10HostCollectedSize����1
		*/
		public static int addM10HostCollectedSize(){
			synchronized(getInstanceOfObject()){
				m10HostCollectedSize = m10HostCollectedSize + 1;
			}
			return m10HostCollectedSize;
		}

		public static Hashtable getM10HostAlldata() { 
			return m10HostAlldata;
		}

		public static void setM10HostAlldata(Hashtable alldata) {
			m10HostAlldata = alldata;
		}

		public static HashMap<String, MonitorTimer> getM10HostTimerMap() {
			return m10HostTimerMap;
		}

		public static void setM10HostTimerMap(HashMap<String, MonitorTimer> timerMap) {
			m10HostTimerMap = timerMap;
		}
		
		/**
		* ������30���Ӳɼ�
		* @return
		*/
		public static int getM30HostCollectedSize() {
			return m30HostCollectedSize;
		}

		public static void setM30HostCollectedSize(Integer collectedSize) {
			synchronized(getInstanceOfObject()){
				m30HostCollectedSize = collectedSize;
			}
		}

		/**
		* m30HostCollectedSize����1
		*/
		public static int addM30HostCollectedSize(){
			synchronized(getInstanceOfObject()){
				m30HostCollectedSize = m30HostCollectedSize + 1;
			}
			return m5HostCollectedSize;
		}

		public static Hashtable getM30HostAlldata() { 
			return m30HostAlldata;
		}

		public static void setM30HostAlldata(Hashtable alldata) {
			m30HostAlldata = alldata;
		}

		public static HashMap<String, MonitorTimer> getM30HostTimerMap() {
			return m30HostTimerMap;
		}

		public static void setM30HostTimerMap(HashMap<String, MonitorTimer> timerMap) {
			m30HostTimerMap = timerMap;
		}
		
		/**
		* ������1Сʱ�ɼ�
		* @return
		*/
		public static int getH1HostCollectedSize() {
			return h1HostCollectedSize;
		}

		public static void setH1HostCollectedSize(Integer collectedSize) {
			synchronized(getInstanceOfObject()){
				h1HostCollectedSize = collectedSize;
			}
		}

		/**
		* h1HostCollectedSize����1
		*/
		public static int addH1HostCollectedSize(){
			synchronized(getInstanceOfObject()){
				h1HostCollectedSize = h1HostCollectedSize + 1;
			}
			return h1HostCollectedSize;
		}

		public static Hashtable getH1HostAlldata() { 
			return h1HostAlldata;
		}

		public static void setH1HostAlldata(Hashtable alldata) {
			h1HostAlldata = alldata;
		}

		public static HashMap<String, MonitorTimer> getH1HostTimerMap() {
			return h1HostTimerMap;
		}

		public static void setH1HostTimerMap(HashMap<String, MonitorTimer> timerMap) {
			h1HostTimerMap = timerMap;
		}
		
		/**
		* ������1��ɼ�
		* @return
		*/
		public static int getD1HostCollectedSize() {
			return d1HostCollectedSize;
		}

		public static void setD1HostCollectedSize(Integer collectedSize) {
			synchronized(getInstanceOfObject()){
				d1HostCollectedSize = collectedSize;
			}
		}

		/**
		* d1HostCollectedSize����1
		*/
		public static int addD1HostCollectedSize(){
			synchronized(getInstanceOfObject()){
				d1HostCollectedSize = d1HostCollectedSize + 1;
			}
			return d1HostCollectedSize;
		}

		public static Hashtable getD1HostAlldata() { 
			return d1HostAlldata;
		}

		public static void setD1HostAlldata(Hashtable alldata) {
			d1HostAlldata = alldata;
		}

		public static HashMap<String, MonitorTimer> getD1HostTimerMap() {
			return d1HostTimerMap;
		}

		public static void setD1HostTimerMap(HashMap<String, MonitorTimer> timerMap) {
			d1HostTimerMap = timerMap;
		}
		

		
  
  public static Hashtable getAllNetworkData() {
	return allNetworkData;
}


public static Hashtable getAllNetworkPingData() {
	return allNetworkPingData;
}


public static void setAllNetworkPingData(Hashtable allNetworkPingData) {
	ShareData.allNetworkPingData = allNetworkPingData;
}


public static void setAllNetworkData(Hashtable allNetworkData) {
	ShareData.allNetworkData = allNetworkData;
}


public static Hashtable getSubscribeReportHash() {
		return subscribeReportHash;
	}


	public static Hashtable getAllLinknodeInterfaceData() {
	return allLinknodeInterfaceData;
}


public static void setAllLinknodeInterfaceData(
		Hashtable allLinknodeInterfaceData) {
	ShareData.allLinknodeInterfaceData = allLinknodeInterfaceData;
}


	public static void setSubscribeReportHash(Hashtable subscribeReportHash) {
		ShareData.subscribeReportHash = subscribeReportHash;
	}
  
  public static Hashtable getAllLinkData() {
		return allLinkData;
	}


	public static void setAllLinkData(Hashtable allLinkData) {
		ShareData.allLinkData = allLinkData;
	}


public static void setCheckeventdata(String key,String value){  
	  checkeventdata.put(key,value);
	  }


  public static Hashtable getCheckeventdata(){  
	  return checkeventdata;
	  }
  
  
  public static int runflag =0;
  public static int runserviceflag =0;
  
  public static Hashtable apachedata = new Hashtable();
  public static void setApachedata(String ip,Hashtable hash){  
	  apachedata.put(ip,hash);
	  }


  public static Hashtable getApachedata(){  
	  return apachedata;
	  }
  
  public static Hashtable jbossdata = new Hashtable();
  public static void setJbossdata(String ip,Hashtable hash){  
	  jbossdata.put(ip,hash);
	  }


public static Hashtable getAllDnsData() {
	return allDnsData;
}


public static void setAllDnsData(Hashtable allDnsData) {
	ShareData.allDnsData = allDnsData;
}


public static Hashtable getJbossdata(){  
	  return jbossdata;
	  }
  
  public static int getRunflag(){
	  return runflag;
  }
  public static void setRunflag(int _runflag){
	  runflag = _runflag;
  }
  
  public static int getRunserviceflag(){
	  return runserviceflag;
  }
  public static void setRunserviceflag(int _runserviceflag){
	  runserviceflag = _runserviceflag;
  }
  
  
  //nielin add 2010-06-28
  public static void setStoragedata(String ip,Hashtable hash){  
	  allstoragedata.put(ip,hash);
	  }
  
  //nielin add 2010-06-28
  public static Hashtable getStoragedata(){  
	  return allstoragedata;
	  }
  
  public static void setMySqlmonitordata(String ip,Hashtable hash){  
	  mysqlmonitordata.put(ip,hash);
	  }
 public static Hashtable getMySqlmonitordata(){
	  	return mysqlmonitordata;
	  }
  public static void setMySqldata(String ip,Hashtable hash){  
	  sqlserverdata.put(ip,hash);
	  }
 public static Hashtable getMySqldata(){
	  	return sqlserverdata;
	  }
  
  public static void setAlltuxedodata(String ip,Hashtable hash){  
	  alltuxedodata.put(ip,hash);
	  }
  public static Hashtable getAlltuxedodata(){
	  	return alltuxedodata;
	  }
  
  public static void setAlllegatodata(String ip,HashMap hash){  
	  alllegatodata.put(ip,hash);
	  }
  public static Hashtable getAlllegatodata(){
	  	return alllegatodata;
	  }
  
  public static void setAllsyslogconfdata(Hashtable hash){
	  allsyslogconfdata = hash;
  }
  public static Hashtable getAllsyslogconfdata(){
	  return allsyslogconfdata;
  }
  
  public static void setEmersondata(String ip,Hashtable hash){  
	  emersondata.put(ip,hash);
	  }
  public static Hashtable getEmersondata(){
	  	return emersondata;
	  }


  public static void setSqldbdata(String ip,Hashtable hash){  
	  sqldbdata.put(ip,hash);
	  }
  public static Hashtable getSqldbdata(){
	  	return sqldbdata;
	  }
  
  public static void setWasdata(String ip,Hashtable hash){  
	  wasdata.put(ip,hash);
	  }
  public static Hashtable getWasdata(){
	  	return wasdata;
	  }
  
  
  public static void setDb2type6spacedata(String ip,Hashtable hash){  
	  db2type6spacedata.put(ip,hash);
	  }
  public static Hashtable getDb2type6spacedata(){
	  	return db2type6spacedata;
	  }
  
  public static void setAlloraspacealarmdata(Hashtable hash){  
	  alloraspacealarmdata = hash;
	  }
  public static Hashtable getAlloraspacealarmdata(){
	  	return alloraspacealarmdata;
	  }

  public static void setInformixspacedata(String ip,List list){  
	  informixspacedata.put(ip,list);
	  }
  public static Hashtable getInformixspacedata(){
	  	return informixspacedata;
	  }
 
  public static void setAllinformixspacealarmdata(Hashtable hash){  
	  allinformixspacealarmdata = hash;
	  }
  public static Hashtable getAllinformixspacealarmdata(){
	  	return allinformixspacealarmdata;
	  }
  
  

  public static void setOraspacedata(String ip,Vector vec){  
	  oraspacedata.put(ip,vec);
	  }
  public static Hashtable getOraspacedata(){
	  	return oraspacedata;
	  }

  public static void setAlloracledata(String ip,Hashtable hash){  
	  alloracledata.put(ip,hash);
	  }
  public static Hashtable getAlloracledata(){
	  	return alloracledata;
	  }

  public static void setAlldb2data(String ip,Hashtable hash){  
	  alldb2data.put(ip,hash);
	  }
  public static Hashtable getAlldb2data(){
	  	return alldb2data;
	  }

  public static void setAllwlserveralarmdata(Hashtable hash){  
	  allwlserveralarmdata = hash;
	  }
  public static Hashtable getAllwlserveralarmdata(){
	  	return allwlserveralarmdata;
	  }

  
  public static void setAllmqalarmdata(Hashtable hash){  
	  allmqalarmdata = hash;
	  }
  public static Hashtable getAllmqalarmdata(){
	  	return allmqalarmdata;
	  }
  
  
  public static void setAlldiskalarmdata(Hashtable hash){  
	  alldiskalarmdata = hash;
	  }
  public static Hashtable getAlldiskalarmdata(){
	  	return alldiskalarmdata;
	  }
  
  
  public static void setIBMStoragedata(String ip,Hashtable hash){  
	  ibmstoragedata.put(ip,hash);
	  }
  public static Hashtable getIBMStoragedata(){
	  	return ibmstoragedata;
	  }
  
  
  public static void setWeblogicdata(String ip,Hashtable hash){  
	  weblogicdata.put(ip,hash);
	  }
  public static Hashtable getWeblogicdata(){
	  	return weblogicdata;
	  }
  public static void setTomcatdata(String ip,Hashtable hash){  
	  tomcatdata.put(ip,hash);
	  }
  public static Hashtable getTomcatdata(){
	  	return tomcatdata;
	  }
  public static void addMqdata(MQConfig mqconf,Hashtable mqValue){
	  //��IP:��������ΪKEY
//System.out.println(mqconf.getIpaddress()+":"+mqconf.getManagername()+"-----");	
	  if(mqconf != null && mqconf.getIpaddress()!=null && mqconf.getManagername()!=null && mqValue != null)
	  mqdata.put(mqconf.getIpaddress()+":"+mqconf.getManagername(),mqValue);
	  }
  public static Hashtable getMqdata(){
	  	return mqdata;
	  }  
  
  public static Hashtable getProcsendeddata(){
	  	return procsendeddata;
	  }
  
  
  public static void setLostprocdata(String ip,Hashtable hash){  
	  lostprocdata.put(ip,hash);
	  }
  public static Hashtable getLostprocdata(String ip){
	  	return (Hashtable)lostprocdata.get(ip);
	  }

  
  public static void setSysbasedata(String ip,Hashtable hash){  
	  sysbasedata.put(ip,hash);
	  }
  public static Hashtable getSysbasedata(){
	  	return sysbasedata;
	  }

  public static void setSqlserverdata(String ip,Hashtable hash){  
	  sqlserverdata.put(ip,hash);
	  }
  public static Hashtable getSqlserverdata(){
	  	return sqlserverdata;
	  }
  
  
  public static Date formerUpdateTime = new Date();
  
  
  
  
  public static void setFormerUpdateTime(Date date){
	  formerUpdateTime = date;
  }
  public static Date getFormerUpdateTime(){
	  if (formerUpdateTime == null) formerUpdateTime = new Date();
	  return formerUpdateTime;
  }
  
  public static void setPolicydata(String ip,Hashtable hash){
	  policydata.put(ip,hash);
	  }
  public static Hashtable getPolicydata(){
	  	return policydata;
  }
  

  public static void setTosroutedata(String ip,List hash){
	  tosroutedata.put(ip,hash);
	  }
  public static Hashtable getTosroutedata(){
	  	return tosroutedata;
	  }
  
  public static void setDominodata(String ip,Hashtable hash){
	  dominodata.put(ip,hash);
	  }
  public static Hashtable getDominodata(){
	  	return dominodata;
	  }
  public static void setIisdata(String ip,List list){
	  iisdata.put(ip,list);
	  }
  public static Hashtable getIisdata(){
	  	return iisdata;
	  }
  
  public static void setIprouterdata(String ip,Vector v){
  	iprouterdata.put(ip,v);
  }
  public static Hashtable getIprouterdata(){
  	return iprouterdata;
  }
  
  public static Hashtable getIpmacbanddata(){
  	return ipmacbanddata;
  }
  public static Hashtable getFdbipmacbanddata(){
	  	return fdbipmacbanddata;
	  }

  public static void setRelateipmacdata(String ip,Vector v){
  	relateipmacdata.put(ip,v);
  }
  public static Hashtable getRelateipmacdata(){
  	return relateipmacdata;
  }
  
  public static void setRelatefdbipmacdata(String ip,Vector v){
	  	relatefdbipmacdata.put(ip,v);
	  }
	  public static Hashtable getRelatefdbipmacdata(){
	  	return relatefdbipmacdata;
	  }

  public static void setRelateippingdata(String ip,Vector value){
  	relateippingdata.put(ip,value);
  }
  public static Hashtable getRelateippingdata(){
  	return relateippingdata;
  }

  public static void setSameequipsmsdata(String ip,Vector v){  	
  	sameequipsmsdata.put(ip,v);  	
  }
  public static void refreshSameequipsmsdata(){  	
  	sameequipsmsdata = new Hashtable();  	
  }
  public static Hashtable getSameequipsms(){
  	return sameequipsmsdata;
  }
  public static Vector getSameequipsmsdata(String ip){
  	return (Vector)sameequipsmsdata.get(ip);
  }

  
  
  
  public static void setEventdata(String ip,Vector v){  	
  	eventdata.put(ip,v);  	
  }
  public static Vector getEventdata(String ip){
  	return (Vector)eventdata.get(ip);
  }
  
  public static void setOctetsdata(String ip,Hashtable hash){  	
  	octetsdata.put(ip,hash);  	
  }
  public static Hashtable getOctetsdata(String ip){
  	return (Hashtable)octetsdata.get(ip);
  }
  
  public static void setRealOctetsdata(String ip,Hashtable hash){  	
	  	realoctetsdata.put(ip,hash);  	
	  }
	  public static Hashtable getRealOctetsdata(String ip){
	  	return (Hashtable)realoctetsdata.get(ip);
	  }
  
  public static void setPksdata(String ip,Hashtable hash){  	
	  	pksdata.put(ip,hash);  	
	  }
	  public static Hashtable getPksdata(String ip){
	  	return (Hashtable)pksdata.get(ip);
	  }
  
  
  public static void setPacksdata(String ip,Hashtable hash){  	
  	packsdata.put(ip,hash);  	
  }
  public static Hashtable getPacksdata(String ip){
  	return (Hashtable)packsdata.get(ip);
  }
  
  public static void setDiscardsdata(String ip,Hashtable hash){
  	discardsdata.put(ip,hash);
  }
  public static Hashtable getDiscardsdata(String ip){
  	return (Hashtable)discardsdata.get(ip);
  }
  public static void setErrorsdata(String ip,Hashtable hash){
  	errorsdata.put(ip,hash);
  }
  public static Hashtable getErrorsdata(String ip){
  	return (Hashtable)errorsdata.get(ip);
  }

  
  public static void setCount(int acount){
  	count = acount;
  }
  public static int getCount(){
  	return count;
  }

  public static void setFirstipmac(int afirstipmac){
  	firstipmac = afirstipmac;
  }
  public static int getFirstipmac(){
  	return firstipmac;
  }
  
  
  public static void setPingdat(Hashtable pdata){
  	pingdata = pdata;
  }
  public static void setPingdata(String ipaddress,Vector v){
  	if (pingdata.containsKey(ipaddress))
  		pingdata.remove(ipaddress);
  		pingdata.put(ipaddress,v);
  }
  
  public static void setIISPingdata(Hashtable pdata){
	  	iispingdata = pdata;
	  }
	  public static void setIISPingdata(String ipaddress,Vector v){
	  	if (iispingdata.containsKey(ipaddress))
	  		iispingdata.remove(ipaddress);
	  	iispingdata.put(ipaddress,v);
	  }
  /*
  public static void setAllpingdata(String ipaddress,Pingcollectdata pingcollectdata){
  	if (allpingdata.containsKey(ipaddress))
  		allpingdata.remove(ipaddress);
  	allpingdata.put(ipaddress,pingcollectdata);
  }
  */

  public static void setAlldbdata(String ipaddress,Calendar date){
  	if (alldbdata.containsKey(ipaddress))
  		alldbdata.remove(ipaddress);
  	alldbdata.put(ipaddress,date);
  }

  public static Hashtable getPingdata(){ 
  	return pingdata;
  }
  public static Hashtable getIISPingdata(){
	  	return iispingdata;
	  }
  public static void setSharedata(Hashtable sdata){
  	sharedata = sdata;
  }
  public static void setSharedata(String ipaddress,Vector v){
  	//sharedata = sdata;
  	if (sharedata.containsKey(ipaddress))
  		sharedata.remove(ipaddress);
  	sharedata.put(ipaddress,v);
  }
  
  public static void setLastPortdata(Hostlastcollectdata lastdata){
  	portdata.put(lastdata.getIpaddress()+"."+lastdata.getSubentity(),lastdata);
  }
  /*
  public static void addSendeddata(SendedSMS sendedsms){
  	sendeddata.put(sendedsms.getIpaddress(),sendedsms);
  }
  public static void addMemsendeddata(SendedSMS sendedsms){
  	memsendeddata.put(sendedsms.getIpaddress()+":"+sendedsms.getCategory()+":"+sendedsms.getSubentity(),sendedsms);
  }
  
  public static void addPortSendeddata(SendedSMS sendedsms){
  	portsendeddata.put(sendedsms.getIpaddress(),sendedsms);
  }
  */
  public static void addFilesizedata(String ipandfilename,String filesize){
  	filesizedata.put(ipandfilename,filesize);
  }

  public static Hashtable getSendeddata(){
  	return sendeddata;
  }
  public static Hashtable getCreateEventdata(){
	  	return createeventdata;
	  }
  public static Hashtable getMemsendeddata(){
  	return memsendeddata;
  }

  public static Hashtable getPortsendeddata(){
  	return portsendeddata;
  }
  public static Hashtable getFilesizedata(){
  	return filesizedata;
  }
  public static Hashtable getAllpingdata(){
  	return allpingdata;
  }
  public static Hashtable getAlldbdata(){
  	return alldbdata;
  }

  public static Hashtable getAllportdata(){
  	return allportdata;
  }

  public static Hashtable getPortdata(){
  	return portdata;
  }
  
  public static void setLastHostdata(Hostlastcollectdata lastdata){
  	hostdata.put(lastdata.getIpaddress(),lastdata);
  }
  
  public static Hashtable getHostdata(){
  	return hostdata;
  }
  public static void setLastMemdata(String index,String value){
  	hostdata.put(index,value);
  }
  public static Hashtable getMemdata(){
  	return memdata;
  }
  
  
  public static Hashtable getSharedata(){
  	if (sharedata == null )sharedata=new Hashtable();
  	return sharedata;
  }
  
  public static Hashtable informixmonitordata = new Hashtable(); // �洢informix�ɼ���������Ϣ

	public static void setInfomixmonitordata(String ip, Hashtable hash) {
		informixmonitordata.put(ip, hash);
	}
public static Hashtable getInformixmonitordata() {
		return informixmonitordata;
	}

public static Hashtable getGSNdata() {
	return GSNdata;
}
public static void setGSNdata(String key,Hashtable ndata) {
	GSNdata.put(key, ndata);
}




public static Hashtable getConnectConfigHashtable() {
	return connectConfigHashtable;
}


public static void setConnectConfigHashtable(Hashtable connectConfigHashtable) {
	ShareData.connectConfigHashtable = connectConfigHashtable;
}


public static Hashtable getPortConfigHash() {
	return portConfigHash;
}


public static void setPortConfigHash(Hashtable portConfigHash) {
	ShareData.portConfigHash = portConfigHash;
}

public static Hashtable getGatherHash() {
	return gatherHash;
}


public static void setGatherHash(Hashtable gatherHash) {
	ShareData.gatherHash = gatherHash;
}


public static Hashtable getCheckEventHash() {
	return checkEventHash;
}


public static void setCheckEventHash(Hashtable checkEventHash) {
	ShareData.checkEventHash = checkEventHash;
}

public static Hashtable getParamsHash() {
	return paramsHash;
}

public static void setParamsHash(Hashtable paramsHash) {
	ShareData.paramsHash = paramsHash;
}
public static Hashtable<String, RemoteClientInfo> getIp_clientInfoHash() {
	return ip_clientInfoHash;
}

public static void setIp_clientInfoHash(
		Hashtable<String, RemoteClientInfo> ip_clientInfoHash) {
	ShareData.ip_clientInfoHash = ip_clientInfoHash;
}

public static Hashtable getNodereportData() {
	return nodereportData;
}

public static void setNodereportData(Hashtable nodereportData) {
	ShareData.nodereportData = nodereportData;
}

/**
 * getTru64netstatData:
 * <p>
 *
 * @return  Hashtable
 *          -
 * @since   v1.01
 */
public static Hashtable getTru64netstatData() {
	return tru64netstatData;
}
}
