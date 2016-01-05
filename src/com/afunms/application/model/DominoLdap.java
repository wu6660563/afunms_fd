/**
 * <p>Description:mapping app_db_node</p>
 * <p>Company: dhcc.com</p>
 * @author miiwill
 * @project afunms
 * @date 2007-1-7
 */

package com.afunms.application.model;

import com.afunms.common.base.BaseVo;

public class DominoLdap extends BaseVo{
	private String ldapRunning = "";//�����Ƿ�������
	private String ldapInboundQue = "";//�ȴ��������������
	private String ldapInboundActive = "";//��ǰ������������
	private String ldapInboundActiveSSL = "";//��ǰ����SSL������
	private String ldapInboundBytesReseived = "";//�������ӽ����ֽ�������Bytes��
	private String ldapInboundBytesSent = "";//�������ӷ����ֽ�������Bytes��
	private String ldapInboundPeak = "";//�����������������
	private String ldapInboundPeakSSL = "";//����������� SSL ������
	private String ldapInboundTotal = "";//������������
	private String ldapInboundTotalSSL = "";//����SSL��������
	private String ldapBadHandShake = "";//ʧ�ܵ�����SSL������
	private String ldapThreadsBusy = "";//��ǰ��æ�߳���
	private String ldapThreadsIdle = "";//��ǰ�����߳���
	private String ldapThreadsInPool = "";//��ǰ�̳߳��е��߳���
	private String ldapTHreadsPeak = "";//��󲢷��߳���
	public String getLdapBadHandShake() {
		return ldapBadHandShake;
	}
	public void setLdapBadHandShake(String ldapBadHandShake) {
		this.ldapBadHandShake = ldapBadHandShake;
	}
	public String getLdapInboundActive() {
		return ldapInboundActive;
	}
	public void setLdapInboundActive(String ldapInboundActive) {
		this.ldapInboundActive = ldapInboundActive;
	}
	public String getLdapInboundActiveSSL() {
		return ldapInboundActiveSSL;
	}
	public void setLdapInboundActiveSSL(String ldapInboundActiveSSL) {
		this.ldapInboundActiveSSL = ldapInboundActiveSSL;
	}
	public String getLdapInboundBytesReseived() {
		return ldapInboundBytesReseived;
	}
	public void setLdapInboundBytesReseived(String ldapInboundBytesReseived) {
		this.ldapInboundBytesReseived = ldapInboundBytesReseived;
	}
	public String getLdapInboundBytesSent() {
		return ldapInboundBytesSent;
	}
	public void setLdapInboundBytesSent(String ldapInboundBytesSent) {
		this.ldapInboundBytesSent = ldapInboundBytesSent;
	}
	public String getLdapInboundPeak() {
		return ldapInboundPeak;
	}
	public void setLdapInboundPeak(String ldapInboundPeak) {
		this.ldapInboundPeak = ldapInboundPeak;
	}
	public String getLdapInboundPeakSSL() {
		return ldapInboundPeakSSL;
	}
	public void setLdapInboundPeakSSL(String ldapInboundPeakSSL) {
		this.ldapInboundPeakSSL = ldapInboundPeakSSL;
	}
	public String getLdapInboundQue() {
		return ldapInboundQue;
	}
	public void setLdapInboundQue(String ldapInboundQue) {
		this.ldapInboundQue = ldapInboundQue;
	}
	public String getLdapInboundTotal() {
		return ldapInboundTotal;
	}
	public void setLdapInboundTotal(String ldapInboundTotal) {
		this.ldapInboundTotal = ldapInboundTotal;
	}
	public String getLdapInboundTotalSSL() {
		return ldapInboundTotalSSL;
	}
	public void setLdapInboundTotalSSL(String ldapInboundTotalSSL) {
		this.ldapInboundTotalSSL = ldapInboundTotalSSL;
	}
	public String getLdapRunning() {
		return ldapRunning;
	}
	public void setLdapRunning(String ldapRunning) {
		this.ldapRunning = ldapRunning;
	}
	public String getLdapThreadsBusy() {
		return ldapThreadsBusy;
	}
	public void setLdapThreadsBusy(String ldapThreadsBusy) {
		this.ldapThreadsBusy = ldapThreadsBusy;
	}
	public String getLdapThreadsIdle() {
		return ldapThreadsIdle;
	}
	public void setLdapThreadsIdle(String ldapThreadsIdle) {
		this.ldapThreadsIdle = ldapThreadsIdle;
	}
	public String getLdapThreadsInPool() {
		return ldapThreadsInPool;
	}
	public void setLdapThreadsInPool(String ldapThreadsInPool) {
		this.ldapThreadsInPool = ldapThreadsInPool;
	}
	public String getLdapTHreadsPeak() {
		return ldapTHreadsPeak;
	}
	public void setLdapTHreadsPeak(String ldapTHreadsPeak) {
		this.ldapTHreadsPeak = ldapTHreadsPeak;
	}

}