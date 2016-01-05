package com.afunms.application.model;
/**
 * @author HONGLI E-mail: ty564457881@163.com
 * @version ����ʱ�䣺May 27, 2011 1:57:06 PM
 * ��˵����oracle������ϢModel
 */
public class OracleLockInfo {
	private String id;
	
	/**
	 * ���ȴ���
	 */
	private String lockwaitcount;
	
	/**
	 * ������
	 */
	private String deadlockcount;
	
	/**
	 * ��ǰ������
	 */
	private String processcount;
	
	/**
	 * ���������
	 */
	private String maxprocesscount;

	
	/**
	 * ��ǰ�Ự����
	 */
	private String currentsessioncount;
	
	/**
	 * �������Ự����
	 */
	private String useablesessioncount;
	
	/**
	 * ���ûỰ���ٷֱ�
	 */
	private String useablesessionpercent;
	
	/**
	 * �ȴ������ĻỰ����
	 */
	private String lockdsessioncount;
	
	
	/**
	 * �ύ�ڻع��ٷֱ�
	 */
	private String rollbackcommitpercent;
	
	/**
	 * �ع�����
	 */
	private String rollbacks;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRollbacks() {
		return rollbacks;
	}

	public void setRollbacks(String rollbacks) {
		this.rollbacks = rollbacks;
	}

	public String getRollbackcommitpercent() {
		return rollbackcommitpercent;
	}

	public void setRollbackcommitpercent(String rollbackcommitpercent) {
		this.rollbackcommitpercent = rollbackcommitpercent;
	}

	public String getCurrentsessioncount() {
		return currentsessioncount;
	}

	public void setCurrentsessioncount(String currentsessioncount) {
		this.currentsessioncount = currentsessioncount;
	}

	public String getUseablesessioncount() {
		return useablesessioncount;
	}

	public void setUseablesessioncount(String useablesessioncount) {
		this.useablesessioncount = useablesessioncount;
	}

	public String getLockdsessioncount() {
		return lockdsessioncount;
	}

	public void setLockdsessioncount(String lockdsessioncount) {
		this.lockdsessioncount = lockdsessioncount;
	}

	public String getUseablesessionpercent() {
		return useablesessionpercent;
	}

	public void setUseablesessionpercent(String useablesessionpercent) {
		this.useablesessionpercent = useablesessionpercent;
	}


	public String getLockwaitcount() {
		return lockwaitcount;
	}

	public void setLockwaitcount(String lockwaitcount) {
		this.lockwaitcount = lockwaitcount;
	}

	public String getDeadlockcount() {
		return deadlockcount;
	}

	public void setDeadlockcount(String deadlockcount) {
		this.deadlockcount = deadlockcount;
	}

	public String getProcesscount() {
		return processcount;
	}

	public void setProcesscount(String processcount) {
		this.processcount = processcount;
	}

	public String getMaxprocesscount() {
		return maxprocesscount;
	}

	public void setMaxprocesscount(String maxprocesscount) {
		this.maxprocesscount = maxprocesscount;
	}
}
