/**
 * <p>Description:mapping app_db_node</p>
 * <p>Company: dhcc.com</p>
 * @author miiwill
 * @project afunms
 * @date 2007-1-7
 */

package com.afunms.application.model;

import com.afunms.common.base.BaseVo;

public class DominoHttp extends BaseVo{
	private String httpAccept = "";//HTTP������������
	private String httpRefused = "";//���ܾ���HTTP����������
	private String httpCurrentCon = "";//��ǰHTTP������
	private String httpMaxCon = "";//���HTTP������
	private String httpPeakCon = "";//��ֵHTTP������
	private String httpWorkerRequest = "";//HTTP������
	private String httpWorkerRequestTime = "";//HTTP����ʱ��
	private String httpWorkerBytesRead = "";//Worker�ܼƶ��ֽ�
	private String httpWorkerBytesWritten = "";//Worker�ܼ�д�ֽ�
	private String httpWorkerRequestProcess = "";//Worker�ܼƴ������������
	private String httpWorkerTotalRequest = "";//Worker�ܼ�����ʱ��
	private String httpErrorUrl = "";//Worker�ܼ�����ʱ��
	
	public DominoHttp(){
		httpAccept = "";//HTTP������������
		httpRefused = "";//���ܾ���HTTP����������
		httpCurrentCon = "";//��ǰHTTP������
		httpMaxCon = "";//���HTTP������
		httpPeakCon = "";//��ֵHTTP������
		httpWorkerRequest = "";//HTTP������
		httpWorkerRequestTime = "";//HTTP����ʱ��
		httpWorkerBytesRead = "";//Worker�ܼƶ��ֽ�
		httpWorkerBytesWritten = "";//Worker�ܼ�д�ֽ�
		httpWorkerRequestProcess = "";//Worker�ܼƴ������������
		httpWorkerTotalRequest = "";//Worker�ܼ�����ʱ��		
	}
	
	public String getHttpAccept() {
		return httpAccept;
	}
	public void setHttpAccept(String httpAccept) {
		this.httpAccept = httpAccept;
	}
	public String getHttpCurrentCon() {
		return httpCurrentCon;
	}
	public void setHttpCurrentCon(String httpCurrentCon) {
		this.httpCurrentCon = httpCurrentCon;
	}
	public String getHttpMaxCon() {
		return httpMaxCon;
	}
	public void setHttpMaxCon(String httpMaxCon) {
		this.httpMaxCon = httpMaxCon;
	}
	public String getHttpPeakCon() {
		return httpPeakCon;
	}
	public void setHttpPeakCon(String httpPeakCon) {
		this.httpPeakCon = httpPeakCon;
	}
	public String getHttpRefused() {
		return httpRefused;
	}
	public void setHttpRefused(String httpRefused) {
		this.httpRefused = httpRefused;
	}
	public String getHttpWorkerBytesRead() {
		return httpWorkerBytesRead;
	}
	public void setHttpWorkerBytesRead(String httpWorkerBytesRead) {
		this.httpWorkerBytesRead = httpWorkerBytesRead;
	}
	public String getHttpWorkerBytesWritten() {
		return httpWorkerBytesWritten;
	}
	public void setHttpWorkerBytesWritten(String httpWorkerBytesWritten) {
		this.httpWorkerBytesWritten = httpWorkerBytesWritten;
	}
	public String getHttpWorkerRequest() {
		return httpWorkerRequest;
	}
	public void setHttpWorkerRequest(String httpWorkerRequest) {
		this.httpWorkerRequest = httpWorkerRequest;
	}
	public String getHttpWorkerRequestProcess() {
		return httpWorkerRequestProcess;
	}
	public void setHttpWorkerRequestProcess(String httpWorkerRequestProcess) {
		this.httpWorkerRequestProcess = httpWorkerRequestProcess;
	}
	public String getHttpWorkerRequestTime() {
		return httpWorkerRequestTime;
	}
	public void setHttpWorkerRequestTime(String httpWorkerRequestTime) {
		this.httpWorkerRequestTime = httpWorkerRequestTime;
	}
	public String getHttpWorkerTotalRequest() {
		return httpWorkerTotalRequest;
	}
	public void setHttpWorkerTotalRequest(String httpWorkerTotalRequest) {
		this.httpWorkerTotalRequest = httpWorkerTotalRequest;
	}

	public String getHttpErrorUrl() {
		return httpErrorUrl;
	}

	public void setHttpErrorUrl(String httpErrorUrl) {
		this.httpErrorUrl = httpErrorUrl;
	}

}