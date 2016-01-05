/**
 * <p>Description:monitor with snmp</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-18
 */

package com.afunms.monitor.executor.base;

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import com.afunms.common.util.SnmpService;

public class SnmpMonitor extends BaseMonitor
{
	public static Hashtable Interface_IfType = null;
	static {
		Interface_IfType = new Hashtable();
		Interface_IfType.put("1", "other(1)");
		Interface_IfType.put("6", "ethernetCsmacd(6)");
		Interface_IfType.put("23", "ppp(23)");
		Interface_IfType.put("28", "slip(28)");
		Interface_IfType.put("33", "Console port");
		Interface_IfType.put("53", "propVirtual(53)");
		Interface_IfType.put("117", "gigabitEthernet(117)");

		Interface_IfType.put("131", "tunnel(131)");

		Interface_IfType.put("135", "others(135)");
		Interface_IfType.put("136", "others(136)");
		Interface_IfType.put("142", "others(142)");
		Interface_IfType.put("54", "others(54)");
		Interface_IfType.put("5", "others(5)");

	}
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
	public static Hashtable HOST_Type_Producter = null;
	static {
		HOST_Type_Producter = new Hashtable();
		HOST_Type_Producter.put("1.3.6.1.4.1.9.1.248", "cisco");
		HOST_Type_Producter.put("2", "�ȴ�");
		HOST_Type_Producter.put("3", "���еȴ����");
		HOST_Type_Producter.put("4", "������");
	}
    protected static SnmpService snmp = new SnmpService(); 
    
	public SnmpMonitor()
    {
    }      

	protected Calendar getCalendar() {
	    return getCalendar(new Date());
	}

	protected Calendar getCalendar(Date date) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
        return calendar;
    }
}