package com.afunms.polling.snmp.linux;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.SysLogger;
import com.afunms.polling.base.ErrorCode;
import com.afunms.polling.base.ObjectValue;
import com.afunms.polling.om.Processcollectdata;

/**
 * Linux Process ��־������
 * 
 * @author ����
 */
public class LinuxProcessByLogFile extends LinuxByLogFile {

	/**
	 * ��־
	 */
	private static SysLogger logger = SysLogger
			.getLogger(LinuxProcessByLogFile.class.getName());

	private static final String LINUX_PROCESS_BEGIN_KEYWORD = LinuxLogFileKeywordConstant.LINUX_PROCESS_BEGIN_KEYWORD;

	private static final String LINUX_PROCESS_END_KEYWORD = LinuxLogFileKeywordConstant.LINUX_PROCESS_END_KEYWORD;

	@Override
	public ObjectValue getObjectValue() {
		String beginStr = LINUX_PROCESS_BEGIN_KEYWORD;
		String endStr = LINUX_PROCESS_END_KEYWORD;
		String processContent = getLogFileContent(beginStr, endStr);
		String ipaddress = getNodeDTO().getIpaddress();
		Calendar date = getCalendarInstance();

		String[] processLineArr = processContent.split("\n");
		Vector<Processcollectdata> processVector = new Vector<Processcollectdata>();
		Hashtable<String, Processcollectdata> processCountHash = new Hashtable<String, Processcollectdata>();
		int commond_start = processLineArr[0].trim().indexOf("COMMAND");
		for (int i = 1; i < processLineArr.length; i++) {
			String tmpCommondStr = processLineArr[i].substring(commond_start);
			if(' ' != processLineArr[i].charAt(commond_start-1)){
				//�պ�Commond����
				tmpCommondStr = processLineArr[i].substring(commond_start + tmpCommondStr.indexOf(' ')).trim();
			}
			
			if(tmpCommondStr == null || tmpCommondStr.trim().length() == 0) {
				continue;
			}
			
			String[] tmpData = null;
			try {
				tmpData = processLineArr[i].trim().substring(0, commond_start + tmpCommondStr.indexOf(' ')).split("\\s++");
			} catch (Exception e) {
				e.printStackTrace();
			}
			if ((tmpData != null)) {
				if ("USER".equalsIgnoreCase(tmpData[0])) {
					// ������
					continue;
				}
				String USER = tmpData[0]; // USER
				String PID = tmpData[1]; // PID
				String CPU = tmpData[2]; // %CPU
				String MEM = tmpData[3]; // %MEM
				String VSZ = tmpData[4]; // VSZ
				String RSS = tmpData[5]; // RSS
				String TTY = tmpData[6]; // TTY
				String STAT = tmpData[7]; // STAT
				String START = tmpData[8]; // START
				String TIME = tmpData[9]; // TIME
				String COMMAND = tmpCommondStr; // COMMAND
//				System.out.println(USER+"==="+PID+"===="+CPU+"===="+MEM+"===="+VSZ+"===="+TTY+"===="+STAT+"===="+START+"===="+TIME+"===="+COMMAND);

				// D �����ж� uninterruptible sleep (usually IO)
				// R ���� runnable (on run queue)
				// S �ж� sleeping
				// T ֹͣ traced or stopped
				// Z ���� a defunct (��zombie��) process
				if ("Z".equalsIgnoreCase(STAT)) {
					STAT = "�����ж�";
				} else if ("R".equalsIgnoreCase(STAT)) {
					STAT = "��������";
				} else if ("S".equalsIgnoreCase(STAT)) {
					STAT = "�����ж�";
				} else if ("T".equalsIgnoreCase(STAT)) {
					STAT = "����ֹͣ";
				} else if ("Z".equalsIgnoreCase(STAT)) {
					STAT = "��������";
				} else {
					STAT = "��������";
				}

				Processcollectdata processdata = new Processcollectdata();
				processdata.setIpaddress(ipaddress);
				processdata.setCollecttime(date);
				processdata.setCategory("Process");
				processdata.setEntity("process_id");
				processdata.setSubentity(PID);
				processdata.setRestype("dynamic");
				processdata.setUnit(" ");
				processdata.setThevalue(PID);
				processVector.addElement(processdata);

				processdata = new Processcollectdata();
				processdata.setIpaddress(ipaddress);
				processdata.setCollecttime(date);
				processdata.setCategory("Process");
				processdata.setEntity("USER");
				processdata.setSubentity(PID);
				processdata.setRestype("dynamic");
				processdata.setUnit(" ");
				processdata.setThevalue(USER);
				processVector.addElement(processdata);

				processdata = new Processcollectdata();
				processdata.setIpaddress(ipaddress);
				processdata.setCollecttime(date);
				processdata.setCategory("Process");
				processdata.setEntity("MemoryUtilization");
				processdata.setSubentity(PID);
				processdata.setRestype("dynamic");
				processdata.setUnit("%");
				processdata.setThevalue(MEM);
				processVector.addElement(processdata);

				processdata = new Processcollectdata();
				processdata.setIpaddress(ipaddress);
				processdata.setCollecttime(date);
				processdata.setCategory("Process");
				processdata.setEntity("Memory");
				processdata.setSubentity(PID);
				processdata.setRestype("static");
				processdata.setUnit("K");
				processdata.setThevalue(VSZ);
				processVector.addElement(processdata);

				processdata = new Processcollectdata();
				processdata.setIpaddress(ipaddress);
				processdata.setCollecttime(date);
				processdata.setCategory("Process");
				processdata.setEntity("Type");
				processdata.setSubentity(PID);
				processdata.setRestype("static");
				processdata.setUnit(" ");
				processdata.setThevalue("Ӧ�ó���");
				processVector.addElement(processdata);

				processdata = new Processcollectdata();
				processdata.setIpaddress(ipaddress);
				processdata.setCollecttime(date);
				processdata.setCategory("Process");
				processdata.setEntity("Status");
				processdata.setSubentity(PID);
				processdata.setRestype("static");
				processdata.setUnit(" ");
				processdata.setThevalue(STAT);
				processVector.addElement(processdata);

				processdata = new Processcollectdata();
				processdata.setIpaddress(ipaddress);
				processdata.setCollecttime(date);
				processdata.setCategory("Process");
				processdata.setEntity("Name");
				processdata.setSubentity(PID);
				processdata.setRestype("static");
				processdata.setUnit(" ");
				processdata.setThevalue(COMMAND);
				processVector.addElement(processdata);

				processdata = new Processcollectdata();
				processdata.setIpaddress(ipaddress);
				processdata.setCollecttime(date);
				processdata.setCategory("Process");
				processdata.setEntity("CpuTime");
				processdata.setSubentity(PID);
				processdata.setRestype("static");
				processdata.setUnit("��");
				processdata.setThevalue(TIME);
				processVector.addElement(processdata);

				processdata = new Processcollectdata();
				processdata.setIpaddress(ipaddress);
				processdata.setCollecttime(date);
				processdata.setCategory("Process");
				processdata.setEntity("StartTime");
				processdata.setSubentity(PID);
				processdata.setRestype("static");
				processdata.setUnit(" ");
				processdata.setThevalue(START);
				processVector.addElement(processdata);

				processdata = new Processcollectdata();
				processdata.setIpaddress(ipaddress);
				processdata.setCollecttime(date);
				processdata.setCategory("Process");
				processdata.setEntity("CpuUtilization");
				processdata.setSubentity(PID);
				processdata.setRestype("dynamic");
				processdata.setUnit("%");
				processdata.setThevalue(CPU);
				processVector.addElement(processdata);

				// ��ӹؼ�����
				processdata = processCountHash.get(COMMAND);
				if (processdata != null) {
					Integer count = Integer.parseInt(processdata.getThevalue());
					count++;
					processdata.setThevalue(String.valueOf(count));
					processCountHash.put(COMMAND, processdata);
				} else {
					processdata = new Processcollectdata();
					processdata.setIpaddress(ipaddress);
					processdata.setCollecttime(date);
					processdata.setCategory("Process");
					processdata.setEntity("Num");
					processdata.setSubentity(PID);
					processdata.setRestype("dynamic");
					processdata.setUnit("��");
					processdata.setThevalue("1");
					processdata.setChname(COMMAND);
					processCountHash.put(COMMAND, processdata);
				}
			}
		}
		ObjectValue objectValue = new ObjectValue();
		Hashtable<String, Object> returnHash = new Hashtable<String, Object>();
		returnHash.put("processVector", processVector);
		returnHash.put("processCountHash", processCountHash);
		objectValue.setObjectValue(returnHash);
		objectValue.setErrorCode(ErrorCode.CODE_SUCESS);
		return objectValue;
	}
}
