/**
 * <p>Description:mapping app_db_node</p>
 * <p>Company: dhcc.com</p>
 * @author miiwill
 * @project afunms
 * @date 2007-1-7
 */

package com.afunms.application.model;

import com.afunms.common.base.BaseVo;

public class DominoMail extends BaseVo{
	private String mailDead = "";//�޷��ʵݵ��ʼ���Ŀ
	private String mailWaiting = "";//�ȴ����͸��������������ʼ���Ŀ
	private String mailWaitingRecipients = "";//�ȴ����͸��������������ʼ���Ŀ
	private String mailDeliverRate = "";//��ǰ�ʵ����ʣ�Bytes/s��
	private String mailTransferRate = "";//��ǰ�������ʣ�Bytes/s��
	private String mailDeliverThreadsMax = "";//����ʵ��߳���
	private String mailDeliverThreadsTotal = "";//�ʵ��߳�����
	private String mailTransferThreadsMax = "";//������߳���
	private String mailTransferThreadsTotal = "";//�����߳�����
	private String mailAvgSize = "";//�ʼ�ƽ����С
	private String mailAvgTime = "";//�ʼ�ƽ���ַ�ʱ��
	
	public String getMailDead() {
		return mailDead;
	}
	public void setMailDead(String mailDead) {
		this.mailDead = mailDead;
	}
	public String getMailDeliverRate() {
		return mailDeliverRate;
	}
	public void setMailDeliverRate(String mailDeliverRate) {
		this.mailDeliverRate = mailDeliverRate;
	}
	public String getMailDeliverThreadsMax() {
		return mailDeliverThreadsMax;
	}
	public void setMailDeliverThreadsMax(String mailDeliverThreadsMax) {
		this.mailDeliverThreadsMax = mailDeliverThreadsMax;
	}
	public String getMailDeliverThreadsTotal() {
		return mailDeliverThreadsTotal;
	}
	public void setMailDeliverThreadsTotal(String mailDeliverThreadsTotal) {
		this.mailDeliverThreadsTotal = mailDeliverThreadsTotal;
	}
	public String getMailTransferRate() {
		return mailTransferRate;
	}
	public void setMailTransferRate(String mailTransferRate) {
		this.mailTransferRate = mailTransferRate;
	}
	public String getMailTransferThreadsMax() {
		return mailTransferThreadsMax;
	}
	public void setMailTransferThreadsMax(String mailTransferThreadsMax) {
		this.mailTransferThreadsMax = mailTransferThreadsMax;
	}
	public String getMailTransferThreadsTotal() {
		return mailTransferThreadsTotal;
	}
	public void setMailTransferThreadsTotal(String mailTransferThreadsTotal) {
		this.mailTransferThreadsTotal = mailTransferThreadsTotal;
	}
	public String getMailWaiting() {
		return mailWaiting;
	}
	public void setMailWaiting(String mailWaiting) {
		this.mailWaiting = mailWaiting;
	}
	public String getMailWaitingRecipients() {
		return mailWaitingRecipients;
	}
	public void setMailWaitingRecipients(String mailWaitingRecipients) {
		this.mailWaitingRecipients = mailWaitingRecipients;
	}
	public String getMailAvgSize() {
		return mailAvgSize;
	}
	public void setMailAvgSize(String mailAvgSize) {
		this.mailAvgSize = mailAvgSize;
	}
	public String getMailAvgTime() {
		return mailAvgTime;
	}
	public void setMailAvgTime(String mailAvgTime) {
		this.mailAvgTime = mailAvgTime;
	}

}