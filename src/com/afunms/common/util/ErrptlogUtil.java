package com.afunms.common.util;

/**
 * 
 * @author HONGLI  Feb 28, 2011
 *
 */
public class ErrptlogUtil {
	
	/**
	 * @param type  ���󼶱���
	 * @return      ���󼶱�������
	 */
	public static String getTypename(String type){
		String typename = "";
		if("pend".equalsIgnoreCase(type)){
			typename = "�豸����������ܶ�";
		}else if("perf".equalsIgnoreCase(type)){
			typename = "���������½�";
		}else if("perm".equalsIgnoreCase(type)){
			typename = "Ӳ���豸�����ģ����";
		}else if("temp".equalsIgnoreCase(type)){
			typename = "��ʱ�Դ��󣬾������Ժ��Ѿ��ָ�����";
		}else if("info".equalsIgnoreCase(type)){
			typename = "һ����Ϣ�����Ǵ���";
		}else if("unkn".equalsIgnoreCase(type)){
			typename = "����ȷ�������������";
		}else {
			typename = "�豸����������ܶ�";
		}
		return typename;
	}
	
	/**
	 * @param errptclass  �����������
	 * @return			  ��������������
	 */
	public static String getClassname(String errptclass){
		String errptclassname = "";
		if("h".equalsIgnoreCase(errptclass)){
			errptclassname = "Ӳ������ʹ���";
		}else if("s".equalsIgnoreCase(errptclass)){
			errptclassname = "�������";
		}else if("o".equalsIgnoreCase(errptclass)){
			errptclassname = "��Ϊ����";
		}else if("u".equalsIgnoreCase(errptclass)){
			errptclassname = "����ȷ��";
		}else{
			errptclassname = "Ӳ������ʹ���";
		}
		return errptclassname;
	}
	
}
