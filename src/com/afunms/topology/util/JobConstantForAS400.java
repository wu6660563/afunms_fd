/*
 * 
 * 
 * 
 * 	
		Hashtable typehashtable = new Hashtable();
		typehashtable.put("A" , "�Զ�������ҵ");
		typehashtable.put("B" , "����ҵ");
		typehashtable.put("H" , "ˮƽ�����ڲ�����(����������)");
		typehashtable.put("I" , "����ʽ��ҵ");
		typehashtable.put("M" , "��ϵͳ������ҵ");
		typehashtable.put("R" , "���ѻ���������ҵ");
		typehashtable.put("S" , "ϵͳ��ҵ");
		typehashtable.put("V" , "�����ڲ���������");
		typehashtable.put("W" , "���ѻ�д������ҵ");
		typehashtable.put("X" , "�������Ƴ�����ϵͳ��ҵ");
		
		Hashtable subtypehashtable = new Hashtable();
		subtypehashtable.put("N" , "������������");
		subtypehashtable.put("D" , "����");
		subtypehashtable.put("E" , "������ҵ����ͨ��");
		subtypehashtable.put("J" , "Ԥ������ҵ");
		subtypehashtable.put("P" , "��ӡ����������ҵ");
		subtypehashtable.put("T" , "�������ն�(MRT)��ҵ(��ϵͳ/36 ����)");
		subtypehashtable.put("U" , "����ļ��ѻ��û�");
		
		Hashtable statushashtable = new Hashtable();
		statushashtable.put(Job.ACTIVE_JOB_STATUS_WAIT_DEQUEUE , "���ڵȴ�����");
		statushashtable.put(Job.ACTIVE_JOB_STATUS_WAIT_SELECTION , "���ڵȴ�ѡ��");
		statushashtable.put(Job.ACTIVE_JOB_STATUS_WAIT_CONDITION , "���ڵȴ�����");
		statushashtable.put(Job.ACTIVE_JOB_STATUS_WAIT_EVENT , "���ڵȴ��¼�");
		statushashtable.put(Job.ACTIVE_JOB_STATUS_WAIT_DELAY , "���ڵȴ��ӳ�");
		statushashtable.put(Job.ACTIVE_JOB_STATUS_WAIT_DELAYED , "���ڵȴ����ӳ�");
		statushashtable.put(Job.ACTIVE_JOB_STATUS_WAIT_PRESTART , "���ڵȴ�����");
		statushashtable.put(Job.ACTIVE_JOB_STATUS_WAIT_SIGNAL , "���ڵȴ��ź�");
		statushashtable.put(Job.ACTIVE_JOB_STATUS_SUSPENDED_SYSTEM_REQUEST , "���ݹ� - ϵͳ����");
		statushashtable.put(Job.ACTIVE_JOB_STATUS_WAIT_DISPLAY , "���ڵȴ�����վ I/O");
		statushashtable.put(Job.ACTIVE_JOB_STATUS_WAIT_TIME_INTERVAL , "���ڵȴ��¼����");
		statushashtable.put(Job.ACTIVE_JOB_STATUS_WAIT_TIME_INTERVAL_AND_ACTIVE , "���ڵȴ��¼����");
		statushashtable.put(Job.ACTIVE_JOB_STATUS_WAIT_JAVA , "���ڵȴ� Java ����");
		statushashtable.put(Job.ACTIVE_JOB_STATUS_WAIT_THREAD , "���ڵȴ��߳�");
		statushashtable.put(Job.ACTIVE_JOB_STATUS_RUNNING , "��������");
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */



package com.afunms.topology.util;

import java.util.Enumeration;
import java.util.Hashtable;

public class JobConstantForAS400 {
	
	private static final String JOB_TYPE_AUTOSTART = "A";
	
	private static final String JOB_TYPE_BATCH = "B";
	
	private static final String JOB_TYPE_INTERACTIVE = "I";
	
	private static final String JOB_TYPE_SUBSYSTEM_MONITOR = "M";
	
	private static final String JOB_TYPE_SPOOLED_READER = "R";
	
	private static final String JOB_TYPE_SYSTEM = "S";
	
	private static final String JOB_TYPE_SPOOLED_WRITER = "W";
	
	private static final String JOB_TYPE_SCPF_SYSTEM = "X";
	
	private static final String JOB_TYPE_NOT_VALID = "";
	
	private static final String JOB_TYPE_ENHANCED = "1016";
	
	private static Hashtable<String, String> typehashtable = null;
	
	static {
		typehashtable = new Hashtable<String, String>();
		typehashtable.put(JOB_TYPE_AUTOSTART , "�Զ�������ҵ");
		typehashtable.put(JOB_TYPE_BATCH , "����ҵ");
		typehashtable.put("H" , "ˮƽ�����ڲ�����(����������)");
		typehashtable.put(JOB_TYPE_INTERACTIVE , "����ʽ��ҵ");
		typehashtable.put(JOB_TYPE_SUBSYSTEM_MONITOR , "��ϵͳ������ҵ");
		typehashtable.put(JOB_TYPE_SPOOLED_READER , "���ѻ���������ҵ");
		typehashtable.put(JOB_TYPE_SYSTEM , "ϵͳ��ҵ");
		typehashtable.put("V" , "�����ڲ���������");
		typehashtable.put(JOB_TYPE_SPOOLED_WRITER , "���ѻ�д������ҵ");
		typehashtable.put(JOB_TYPE_SCPF_SYSTEM , "�������Ƴ�����ϵͳ��ҵ");
		typehashtable.put(JOB_TYPE_NOT_VALID , "");
		typehashtable.put(JOB_TYPE_ENHANCED , "1016");
	};
	
	
	private static final String JOB_SUBTYPE_IMMEDIATE = "D";
	
	private static final String JOB_SUBTYPE_PROCEDURE_START_REQUEST = "E";
	
	private static final String JOB_SUBTYPE_PRESTART = "J";
	
	private static final String JOB_SUBTYPE_PRINT_DRIVER = "P";
	
	private static final String JOB_SUBTYPE_MRT = "T";
	
	private static final String JOB_SUBTYPE_ALTERNATE_SPOOL_USER = "U";
	
	private static final String JOB_SUBTYPE_MACHINE_SERVER_JOB = "F";
	
	private static final String JOB_SUBTYPE_BLANK = "";
	
	private static Hashtable<String, String> subtypehashtable = null;
	
	static {
		subtypehashtable = new Hashtable<String, String>();
		subtypehashtable.put("N" , "������������");
		subtypehashtable.put( JOB_SUBTYPE_IMMEDIATE , "����");
		subtypehashtable.put( JOB_SUBTYPE_PROCEDURE_START_REQUEST , "������ҵ����ͨ��");
		subtypehashtable.put( JOB_SUBTYPE_PRESTART , "Ԥ������ҵ");
		subtypehashtable.put( JOB_SUBTYPE_PRINT_DRIVER , "��ӡ����������ҵ");
		subtypehashtable.put( JOB_SUBTYPE_MRT , "�������ն�(MRT)��ҵ(��ϵͳ/36 ����)");
		subtypehashtable.put( JOB_SUBTYPE_ALTERNATE_SPOOL_USER , "����ļ��ѻ��û�");
		subtypehashtable.put( JOB_SUBTYPE_BLANK , "");
		subtypehashtable.put( JOB_SUBTYPE_MACHINE_SERVER_JOB , "F");
	};
	/**
	 * ���� ����Ϊ IBM Toolbox for java ������������� �� ������
	 * 
	 * ���� IBM PCOMM ���ǽ� ���ͺ������� �����һ�� �γ��� ����
	 * 
	 * ASJ: �Զ����� 
	 * BCH: ���� 
	 * BCI: ������ʱ 
	 * EVK: �ɳ��������������� 
	 * INT: ���� o M36: �߼�36��������ҵ 
	 * MRT: ��������ն� 
	 * PJ: ����ǰ��ҵ 
	 * PDJ: ��ӡ������ҵ 
	 * RDR: ��ȡ���� 
	 * SBS: ��ϵͳ������ 
	 * SYS: ϵͳ 
	 * WTR: д�����״̬ 
	 * 
	 * ��������Ϊ IBM PCOMM �������������
	 */

	/**
	 * Constant indicating that a job was disconnected form a work station display.
	 */
    public static final String ACTIVE_JOB_STATUS_DISCONNECTED = "DSC"; 
    
    /**
     * Constant indicating that a job has been ended with the *IMMED option, 
     * or its delay time has ended with the *CNTRLD option.
     */
    public static final String ACTIVE_JOB_STATUS_ENDED = "END";
    
    /**
     * Constant indicating that a job is ending for a reason other than 
     * running the End Job (ENDJOB) or End Subsystem (ENDSBS) commands, 
     * such as a SIGNOFF command, End Group Job (ENDGRPJOB) command, 
     * or an exception that is not handled.
     */
    public static final String ACTIVE_JOB_STATUS_ENDING = "EOJ"; 
    
    /**
     * Job attribute representing the status of what the initial thread 
     * of a job is currently doing, when the active job status is 
     * ACTIVE_JOB_STATUS_ENDED or ACTIVE_JOB_STATUS_ENDING. 
     */
    public static final int ACTIVE_JOB_STATUS_FOR_JOBS_ENDING = 103;
    
    /**
     * Constant indicating that a job is held.
     */
    public static final String ACTIVE_JOB_STATUS_HELD = "HLD"; 
    
    /**
     * Constant indicating that a job is held due to a suspended thread.
     */
    public static final String ACTIVE_JOB_STATUS_HELD_THREAD = "HLDT"; 
    
    /**
     * Constant indicating that a job is ineligible and not currently 
     * in a pool activity level.
     */
    public static final String ACTIVE_JOB_STATUS_INELIGIBLE = "INEL";
    
    /**
     * Constant indicating that a job is either in transition or not active.
     */
    public static final String ACTIVE_JOB_STATUS_NONE = ""; 
    
    /**
     * ��������
     * 
     * Constant indicating that a job is currently running in a pool activity level.
     */
    public static final String ACTIVE_JOB_STATUS_RUNNING = "RUN"; 
    
    /**
     * Constant indicating that a job has stopped as the result of a signal.
     */
    public static final String ACTIVE_JOB_STATUS_STOPPED = "SIGS"; 
    
    /**
     * Constant indicating that a job is suspended by a Transfer Group Job (TFRGRPJOB) command
     */
    public static final String ACTIVE_JOB_STATUS_SUSPENDED = "GRP"; 
    
    /**
     * ���ݹ� - ϵͳ����
     * 
     * Constant indicating that a job is the suspended half of a system request job pair.
     */
    public static final String ACTIVE_JOB_STATUS_SUSPENDED_SYSTEM_REQUEST = "SRQ";
    
    
    /**
     * Constant indicating that a job is waiting for the completion of 
     * an I/O operation to a binary synchronous device.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_BIN_SYNCH_DEVICE = "BSCW"; 
    
    /**
     * Constant indicating that a job is waiting in a pool activity level for 
     * the completion of an I/O operation to a binary synchronous device.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_BIN_SYNCH_DEVICE_AND_ACTIVE = "BSCA"; 
    
    /**
     * Constant indicating that a job is waiting for the completion of save-while-active 
     * checkpoint processing in another job.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_CHECKPOINT = "CMTW";
    
    /**
     * Constant indicating that a job is waiting for the completion of 
     * an I/O operation to a communications device.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_COMM_DEVICE = "CMNW"; 
    
    /**
     * Constant indicating that a job is waiting in a pool activity level for 
     * the completion of an I/O operation to a communications device.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_COMM_DEVICE_AND_ACTIVE = "CMNA";
    
    /** 
     * ���ڵȴ����� 
     * 
     * Constant indicating that a job is waiting on a handle-based condition.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_CONDITION = "CNDW"; 
    
    /**
     * Constant indicating that a job is waiting for the completion of a CPI communications call.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_CPI_COMM = "CPCW"; 
    
    /**
     * Constant indicating that a job is waiting to try a read operation again 
     * on a database file after the end-of-file (EOF) has been reached.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_DATABASE_EOF = "EOFW"; 
    
    /**
     * Constant indicating that a job is waiting in a pool activity level to 
     * try a read operation again on a database file after the end-of-file (EOF) has been reached.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_DATABASE_EOF_AND_ACTIVE = "EOFA";
    
    /**
     * ���ڵȴ��ӳ�
     * 
     * Constant indicating that a job is delayed for a time interval to end, 
     * or for a specific delay end time, by the Delay Job (DLYJOB) command. 
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_DELAY = "DLYW"; 
    
    /**
     * ���ڵȴ����ӳ�
     * 
     * Constant indicating that a job is waiting for a specified time interval to end,
     * or for a specific delay end time, as specified on the Delay Job (DLYJOB) command. 
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_DELAYED = "DLYW";
    
    /** 
     * ���ڵȴ����� 
     * 
     * Constant indicating that a job is waiting for the completion of a dequeue operation.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_DEQUEUE = "DEQW";
    
    /**
     * Constant indicating that a job is waiting in a pool activity level for 
     * the completion of a dequeue operation.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_DEQUEUE_AND_ACTIVE = "DEQA"; 
    
    /**
     * Constant indicating that a job is waiting for the completion of 
     * an I/O operation to a diskette unit.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_DISKETTE = "DKTW"; 
    
    /**
     * Constant indicating that a job is waiting in a pool activity level for 
     * the completion of an I/O operation to a diskette unit.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_DISKETTE_AND_ACTIVE = "DKTA"; 
    
    /**
     * ���ڵȴ�����վ I/O
     *
     * Constant indicating that a job is waiting for input from a work station display.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_DISPLAY = "DSPW"; 
    
    /**
     * Constant indicating that a job is waiting in a pool activity level for 
     * input from a work station display.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_DISPLAY_AND_ACTIVE = "DSPA";
    
    /** 
     * ���ڵȴ��¼� 
     * 
     * Constant indicating that a job is waiting for an event. 
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_EVENT = "EVTW"; 
    
    /**
     * Constant indicating that a job is waiting for the completion of 
     * an I/O operation to an intersystem communications function (ICF) file.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_ICF_FILE = "ICFW"; 
    
    /**
     * Constant indicating that a job is waiting in a pool activity level for 
     * the completion of an I/O operation to an intersystem communications function (ICF) file.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_ICF_FILE_AND_ACTIVE = "ICFA"; 
    
    /**
     * ���ڵȴ� Java ����
     * 
     * Constant indicating that a job is waiting for the completion of a Java program.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_JAVA = "JVAW"; 
    
    /**
     * Constant indicating that a job is waiting in a pool activity level for 
     * the completion of a Java program.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_JAVA_AND_ACTIVE = "JVAA"; 
    
    /**
     * Constant indicating that a job is waiting for a lock.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_LOCK = "LCKW";
    
    /**
     * Constant indicating that a job is waiting for a message from a message queue.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_MESSAGE = "MSGW"; 
    
    /**
     * Constant indicating that a job is waiting for the completion of 
     * an I/O operation to a mixed device file.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_MIXED_DEVICE_FILE = "MXDW"; 
    
    /**
     * Constant indicating that a job is waiting for the completion of 
     * an I/O operation to multiple files.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_MULTIPLE_FILES = "MLTW"; 
    
    /**
     * Constant indicating that a job is waiting in a pool activity level for 
     * the completion of an I/O operation to multiple files.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_MULTIPLE_FILES_AND_ACTIVE = "MLTA";
    
    /**
     * Constant indicating that a job is waiting for a mutex. 
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_MUTEX = "MTXW";
    
    /**
     * Constant indicating that a job is waiting for the completion of 
     * an I/O operation to an optical device.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_OPTICAL_DEVICE = "OPTW"; 
    
    /**
     * Constant indicating that a job is waiting in a pool activity level for 
     * the completion of an I/O operation to an optical device.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_OPTICAL_DEVICE_AND_ACTIVE = "OPTA"; 
    
    /**
     * Constant indicating that a job is waiting for the completion of 
     * an OSI Communications Subsystem for OS/400 operation.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_OSI = "OSIW"; 
    
    /**
     * ���ڵȴ�����
     * 
     * Constant indicating that a prestart job is waiting for a program start request.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_PRESTART = "PSRW";
    
    /**
     * Constant indicating that a job is waiting for the completion of printer output.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_PRINT = "PRTW";
    
    /**
     * Constant indicating that a job is waiting in a pool activity level for 
     * the completion of printer output.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_PRINT_AND_ACTIVE = "PRTA";
    
    /**
     * Constant indicating that a job is waiting for the completion of a save file operation.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_SAVE_FILE = "SVFW";
    
    /**
     * Constant indicating that a job is waiting in a pool activity level for 
     * the completion of a save file operation.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_SAVE_FILE_AND_ACTIVE = "SVFA"; 
    
    /** 
     * ���ڵȴ�ѡ�� 
     * 
     * Constant indicating that a job is waiting for the completion of a selection.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_SELECTION = "SELW"; 
    
    /**
     * Constant indicating that a job is waiting for a semaphore. 
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_SEMAPHORE = "SEMW"; 
    
    /**
     * ���ڵȴ��ź�
     * 
     * Constant indicating that a job is waiting for a signal.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_SIGNAL = "SIGW";
    
    /**
     * Constant indicating that a job is waiting for the completion of 
     * an I/O operation to a tape device.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_TAPE_DEVICE = "TAPW";
    
    /**
     * Constant indicating that a job is waiting in a pool activity level for 
     * the completion of an I/O operation to a tape device.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_TAPE_DEVICE_AND_ACTIVE = "TAPA";
    
    /**
     * ���ڵȴ��߳�
     * 
     * Constant indicating that a job is waiting for another thread to complete an operation.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_THREAD = "THDW"; 
    
    /**
     * ���ڵȴ��¼����
     * 
     * Constant indicating that a job is waiting for a time interval to end.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_TIME_INTERVAL = "TIMW"; 
    
    /**
     * ���ڵȴ��¼����
     * 
     * Constant indicating that a job is waiting in a pool activity level for a time interval to end.
     */
    public static final String ACTIVE_JOB_STATUS_WAIT_TIME_INTERVAL_AND_ACTIVE = "TIMA"; 

	
	private static Hashtable<String, String> activeStatushashtable = null;
	static {
		activeStatushashtable = new Hashtable<String, String>();
		activeStatushashtable.put(ACTIVE_JOB_STATUS_WAIT_DEQUEUE , "���ڵȴ�����");
		activeStatushashtable.put(ACTIVE_JOB_STATUS_WAIT_SELECTION , "���ڵȴ�ѡ��");
		activeStatushashtable.put(ACTIVE_JOB_STATUS_WAIT_CONDITION , "���ڵȴ�����");
		activeStatushashtable.put(ACTIVE_JOB_STATUS_WAIT_EVENT , "���ڵȴ��¼�");
		activeStatushashtable.put(ACTIVE_JOB_STATUS_WAIT_DELAY , "���ڵȴ��ӳ�");
		activeStatushashtable.put(ACTIVE_JOB_STATUS_WAIT_DELAYED , "���ڵȴ����ӳ�");
		activeStatushashtable.put(ACTIVE_JOB_STATUS_WAIT_PRESTART , "���ڵȴ�����");
		activeStatushashtable.put(ACTIVE_JOB_STATUS_WAIT_SIGNAL , "���ڵȴ��ź�");
		activeStatushashtable.put(ACTIVE_JOB_STATUS_SUSPENDED_SYSTEM_REQUEST , "���ݹ� - ϵͳ����");
		activeStatushashtable.put(ACTIVE_JOB_STATUS_WAIT_DISPLAY , "���ڵȴ�����վ I/O");
		activeStatushashtable.put(ACTIVE_JOB_STATUS_WAIT_TIME_INTERVAL , "���ڵȴ��¼����");
		activeStatushashtable.put(ACTIVE_JOB_STATUS_WAIT_TIME_INTERVAL_AND_ACTIVE , "���ڵȴ��¼����");
		activeStatushashtable.put(ACTIVE_JOB_STATUS_WAIT_JAVA , "���ڵȴ� Java ����");
		activeStatushashtable.put(ACTIVE_JOB_STATUS_WAIT_THREAD , "���ڵȴ��߳�");
		activeStatushashtable.put(ACTIVE_JOB_STATUS_RUNNING, "��������");
	};
	
	/**
	 * ���� Ϊ IBM Toolbox for java ������������״̬ ֻ�����һ����
	 * 
	 */

	
	/**
	 * ͨ�����ͻ�ȡ������������
	 * 
	 * @param type
	 * @return
	 */
	public static String getTypeDescription(String type){
		return typehashtable.get(type);
	}
	
	/**
	 * ͨ�������ͻ�ȡ��������������
	 * 
	 * @param subtype
	 * @return
	 */
	public static String getSubtypeDescription(String subtype){
		return subtypehashtable.get(subtype);
	}
	
	/**
	 * ͨ��״̬��ȡ״̬��������
	 * 
	 * @param activeStatus
	 * @return
	 */
	public static String getActiveStatusDescription(String activeStatus){
		return activeStatushashtable.get(activeStatus);
	}

	/**
	 * ��ȡ�������͵� hashtable
	 * 
	 * @return the typehashtable
	 */
	public static Hashtable<String, String> getTypehashtable() {
		return typehashtable;
	}

	/**
	 * �����������͵� hashtable
	 * 
	 * @param typehashtable the typehashtable to set
	 */
	public static void setTypehashtable(Hashtable<String, String> typehashtable) {
		JobConstantForAS400.typehashtable = typehashtable;
	}

	/**
	 * ��ȡ���������͵� hashtable
	 * 
	 * @return the subtypehashtable
	 */
	public static Hashtable<String, String> getSubtypehashtable() {
		return subtypehashtable;
	}

	/**
	 * �������������͵� hashtable
	 * 
	 * @param subtypehashtable the subtypehashtable to set
	 */
	public static void setSubtypehashtable(
			Hashtable<String, String> subtypehashtable) {
		JobConstantForAS400.subtypehashtable = subtypehashtable;
	}

	/**
	 * ��ȡ����״̬�� hashtable
	 * 
	 * @return the activeStatushashtable
	 */
	public static Hashtable<String, String> getActiveStatushashtable() {
		return activeStatushashtable;
	}

	/**
	 * ��ȡ����״̬�� hashtable
	 * 
	 * @param activeStatushashtable the activeStatushashtable to set
	 */
	public static void setActiveStatushashtable(Hashtable<String, String> activeStatushashtable) {
		JobConstantForAS400.activeStatushashtable = activeStatushashtable;
	}
	
	/**
	 * ��ȡ�������͵�ö��
	 * 
	 * @return typeEnumeration
	 */
	public static Enumeration<String> getTypeEnumeration(){
		return typehashtable.keys();
	}
	
	/**
	 * ��ȡ���������͵�ö��
	 * 
	 * @return subtypeEnumeration
	 */
	public static Enumeration<String> getSubtypeEnumeration(){
		return subtypehashtable.keys();
	}
	
	/**
	 * ��ȡ����״̬��ö��
	 * 
	 * @return activeStatusEnumeration
	 */
	public static Enumeration<String> getActiveStatusEnumeration(){
		return activeStatushashtable.keys();
	}
	
		
}
