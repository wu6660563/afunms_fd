/**
 * <p>Description:constant class</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-06
 */

package com.afunms.common.util;

import java.util.Hashtable;

public class SystemConstant
{
   private SystemConstant()
   {
   }

   public static final int ISRouter = 1;  //·��
   public static final int ISBridge = 2;  //STP   
   public static final int ISCDP = 3;  //CDP
   public static final int ISNDP = 4;  //NDP
   public static final int ISMac = 5;  //MAC

   public static final Integer ISByHand = 10;  //�ֹ�
   
   public static final int PHYSICALLINK = 0;  //��������
   public static final int LOGICALLINK = 1;  //�߼�����
   
   public static final int NONEPHYSICALLINK = 1;  //��ʼ�˺ͽ������Ƕ�������������
   public static final int STARTPHYSICALLINK = 2;  //��ʼ������������,���������߼�����
   public static final int ENDPHYSICALLINK = 3;  //��ʼ�����߼�����,����������������
   public static final int BOTHPHYSICALLINK = 4;  //��ʼ�˺ͽ������Ƕ�����������
   
   public static final int SNMP_IF_TYPE_ETHERNET = 6;

   public static final int SNMP_IF_TYPE_PROP_VIRTUAL = 53;

   public static final int SNMP_IF_TYPE_L2_VLAN = 135;

   public static final int SNMP_IF_TYPE_L3_VLAN = 136;
   
   public static final int RUNNING2NET = 3; //3 running2Net
   public static final int STARTUP2NET = 6; //6 startup2Net
   
   public static final int STARTUP2CONFIG = 3; //3 startup2config
   public static final int RUNNING2CONFIG = 4; //4 running2config
   
   public static final int COLLECTTYPE_SNMP = 1; //SNMP�ɼ���ʽ
   public static final int COLLECTTYPE_SHELL = 2; //SHELL�ɼ���ʽ
   public static final int COLLECTTYPE_PING = 3; //PING�ɼ���ʽ
   public static final int COLLECTTYPE_REMOTEPING = 4; //Զ��PING�ɼ���ʽ
   public static final int COLLECTTYPE_WMI = 5; //WMI�ɼ���ʽ
   public static final int COLLECTTYPE_TELNET = 6;  // Telnet �ɼ���ʽ
   public static final int COLLECTTYPE_SSH = 7;     // SSH �ɼ���ʽ
   public static final int COLLECTTYPE_TELNETCONNECT = 8;     // ֻTELNET��������
   public static final int COLLECTTYPE_SSHCONNECT = 9;     // ֻSSH��������
   public static final int COLLECTTYPE_DATAINTERFACE = 10;     // ���ݽӿ�
   
   public static final int DBCOLLECTTYPE_JDBC = 1; //JDBC�ɼ���ʽ
   public static final int DBCOLLECTTYPE_SHELL = 2; //�ű��ɼ���ʽ
   
   public static final int OSTYPE_WINDOWS = 5; //WINDOWS
   public static final int OSTYPE_AIX = 6; //AIX
   public static final int OSTYPE_HPUNIX = 7; //HPUNIX
   public static final int OSTYPE_SOLARIS = 8; //SOLARIS
   public static final int OSTYPE_LINUX = 9; //LINUX
   
   public static final int MACBAND_BASE = 0; //����
   public static final int MACBAND_IPMAC = 1; //IP-MAC��
   public static final int MACBAND_PORTMAC = 2; //�˿�-MAC��
   public static final int MACBAND_IPPORTMAC = 3; //IP-�˿�-MAC��
   
	  public static Hashtable COLLECTTYPE = null;
	  static {
		  COLLECTTYPE = new Hashtable();
		  COLLECTTYPE.put("1", "SNMP");
		  COLLECTTYPE.put("2", "����");
		  COLLECTTYPE.put("3", "PING");
		  COLLECTTYPE.put("4", "Զ��PING");
		  COLLECTTYPE.put("5", "WMI");
		  COLLECTTYPE.put("6", "Telnet");
		  COLLECTTYPE.put("7", "SSH");
		  COLLECTTYPE.put("8", "TELNET��������");
		  COLLECTTYPE.put("9", "SSH��������");

	  }
	  
	  public static Hashtable OSTYPE = null;
	  static {
		  OSTYPE = new Hashtable();
		  OSTYPE.put("1", "Cisco");
		  OSTYPE.put("2", "H3C");
		  OSTYPE.put("3", "Entrasys");
		  OSTYPE.put("4", "Radware");
		  OSTYPE.put("5", "WINDOWS");
		  OSTYPE.put("6", "AIX");
		  OSTYPE.put("7", "HPUNIX");
		  OSTYPE.put("8", "SOLARIS");
		  OSTYPE.put("9", "LINUX");
		  OSTYPE.put("10", "MAIPU");
	  }
	  
	  public static Hashtable TYPE = null;
	  static {
		  TYPE = new Hashtable();
		  TYPE.put("1", "·����");
		  TYPE.put("2", "·�ɽ�����");
		  TYPE.put("3", "������");
		  TYPE.put("4", "������");
		  TYPE.put("8", "����ǽ");
	  }
   
   
   
   
   
   
}
