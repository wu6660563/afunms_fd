/**
 * <p>Description:Error Mapping</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-06
 */

package com.afunms.common.base;

public class ErrorMessage
{
  private ErrorMessage()
  {
  }

  public static final int NO_LOGIN = 1001;
  public static final int NO_RIGHT = 1002;
  public static final int INCORRECT_PASSWORD = 1003;
  public static final int USER_EXIST = 1004;
  public static final int NO_ROLE = 1005;
  public static final int NO_DEPARTMENT = 1006;
  public static final int NO_POSITION = 1007;
  
  public static final int ACTION_NO_FOUND = 2001;
  public static final int CAN_NOT_CONNECT_DB = 2002;
  
  public static final int SYS_OID_EXIST = 3001;
  public static final int MOID_EXIST = 3002;
  public static final int SERVICE_EXIST = 3003;
  public static final int PING_FAILURE = 3004;
  public static final int SNMP_FAILURE = 3005;
  public static final int ADD_HOST_FAILURE = 3006;
  public static final int LINK_EXIST = 3007;  
  public static final int IP_ADDRESS_EXIST = 3008;
  public static final int IS_NOT_MGE_UPS = 3009;
  public static final int DOUBLE_LINKS = 3010;
  public static final int DEVICES_SAME = 3011;
  
  public static String getErrorMessage(int messageCode)
  {
     switch(messageCode)
     {
        case NO_LOGIN: return "�Բ���,��û�е�¼,���ѳ�ʱ,�����µ�¼!";
        case NO_RIGHT: return "�Բ���,��û��Ȩ��ִ�иò���,����ϵͳ����Ա��ϵ!";
        case INCORRECT_PASSWORD: return "�Բ���,�û��������벻��ȷ!";
        case USER_EXIST: return "�Բ���,���û����Ѿ�����,����������!";
        case NO_ROLE: return "'��ɫ'Ϊ��,������[ϵͳ����->��ɫ]������'��ɫ'!";
        case NO_DEPARTMENT: return "'����'Ϊ��,������[ϵͳ����->����]������'����'!";
        case NO_POSITION: return "'ְ��'Ϊ��,������[ϵͳ����->ְ��]������'ְ��'!";
        
        case ACTION_NO_FOUND: return "û����Ӧ�Ĳ���!";
        
        case SYS_OID_EXIST: return "���ϵͳOID�Ѿ�����!";
        case MOID_EXIST: return "���������ID�Ѿ�����!";
        case SERVICE_EXIST: return "��������Ѿ�����!";
        case IP_ADDRESS_EXIST: return "���IP��ַ�Ѿ�����!";
        case PING_FAILURE: return "�豸Ping��ͨ,����ʧ��!";
        case SNMP_FAILURE: return "�豸��֧��SNMP���߹�ͬ�岻��ȷ,����ʧ��!";
        case ADD_HOST_FAILURE: return "����ʧ��,δ֪����!";
        case LINK_EXIST: return "��·�Ѿ�����,����ʧ��!";
        case DOUBLE_LINKS:  return "����̨�豸�Ѿ�����˫��·��������������·!";
        case DEVICES_SAME:  return "����̨�豸Ϊͬһ�豸������������·!";
        case CAN_NOT_CONNECT_DB: return "�Բ��𣬲����������ݿ�!";        
        case IS_NOT_MGE_UPS: return "���豸����÷������UPS!";   
        
        default:return "δ֪����";
     }
  }
}
