/**
 * @author sunqichang/������
 * Created on 2011-5-12 ����02:05:32
 */
package com.afunms.capreport.mail;

/**
 * �ʼ���Ϣbean
 * 
 * @author sunqichang/������
 * 
 */
public class MailInfo {
	/**
	 * �ռ��˵�ַ
	 */
	private String receiver;

	/**
	 * �����˵�ַ
	 */
	private String copyReceiver;

	/**
	 * �����˵�ַ
	 */
	private String hiddenReceiver;

	/**
	 * mail����
	 */
	private String subject;

	/**
	 * mail����
	 */
	private String content;

	/**
	 * mail������·�����������֮���÷ֺŷָ�
	 */
	private String affixPath;

	/**
	 * 
	 */
	public MailInfo() {

	}

	/**
	 * �����ռ��˵�ַ������ռ��˵�ַ֮���÷ֺŷָ� ʱ��
	 * 
	 * @author sunqichang/������
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	/**
	 * @author sunqichang/������
	 * @return
	 */
	public String getReceiver() {
		return receiver;
	}

	/**
	 * ���ó����˵�ַ����������˵�ַ֮���÷ֺŷָ� ʱ��
	 * 
	 * @author sunqichang/������
	 * @return String
	 */
	public void setCopyReceiver(String receiver) {
		this.copyReceiver = receiver;
	}

	/**
	 * 
	 * @return �����˵�ַ
	 */
	public String getCopyReceiver() {
		return copyReceiver;
	}

	/**
	 * ���ð����˵�ַ����������˵�ַ֮���÷ֺŷָ� ʱ��
	 * 
	 * @author sunqichang/������
	 * @return String
	 */
	public void setHiddenReceiver(String receiver) {
		this.hiddenReceiver = receiver;
	}

	/**
	 * @return �����˵�ַ
	 */
	public String getHiddenReceiver() {
		return hiddenReceiver;
	}

	/**
	 * ����mail����
	 * 
	 * @param subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return mail����
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * ����mail����
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return mail����
	 */
	public String getContent() {
		return content;
	}

	/**
	 * ����mail������·�����������֮���÷ֺŷָ�
	 * 
	 * @author sunqichang/������
	 * @return String
	 */
	public void setAffixPath(String affixPath) {
		this.affixPath = affixPath;
	}

	/**
	 * @return mail������·�����������֮���÷ֺŷָ�
	 */
	public String getAffixPath() {
		return affixPath;
	}

}
