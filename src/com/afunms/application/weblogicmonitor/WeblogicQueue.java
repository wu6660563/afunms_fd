package com.afunms.application.weblogicmonitor;

public class WeblogicQueue {
	String executeQueueRuntimeName = null; //����
	String threadPoolRuntimeExecuteThreadIdleCount = null; //  ִ���̵߳�ǰ������ .1.3.6.1.4.1.140.625.180.1.25
	String executeQueueRuntimePendingRequestOldestTime = null; // ��������ȴ�����Ĵ���ʱ�� .1.3.6.1.4.1.140.625.180.1.30
	String executeQueueRuntimePendingRequestCurrentCount = null;
	String executeQueueRuntimePendingRequestTotalCount = null;
	public String getExecuteQueueRuntimeName() {
		return executeQueueRuntimeName;
	}
	public void setExecuteQueueRuntimeName(String executeQueueRuntimeName) {
		this.executeQueueRuntimeName = executeQueueRuntimeName;
	}
	public String getThreadPoolRuntimeExecuteThreadIdleCount() {
		return threadPoolRuntimeExecuteThreadIdleCount;
	}
	public void setThreadPoolRuntimeExecuteThreadIdleCount(
			String threadPoolRuntimeExecuteThreadIdleCount) {
		this.threadPoolRuntimeExecuteThreadIdleCount = threadPoolRuntimeExecuteThreadIdleCount;
	}
	public String getExecuteQueueRuntimePendingRequestOldestTime() {
		return executeQueueRuntimePendingRequestOldestTime;
	}
	public void setExecuteQueueRuntimePendingRequestOldestTime(
			String executeQueueRuntimePendingRequestOldestTime) {
		this.executeQueueRuntimePendingRequestOldestTime = executeQueueRuntimePendingRequestOldestTime;
	}
	public String getExecuteQueueRuntimePendingRequestCurrentCount() {
		return executeQueueRuntimePendingRequestCurrentCount;
	}
	public void setExecuteQueueRuntimePendingRequestCurrentCount(
			String executeQueueRuntimePendingRequestCurrentCount) {
		this.executeQueueRuntimePendingRequestCurrentCount = executeQueueRuntimePendingRequestCurrentCount;
	}
	public String getExecuteQueueRuntimePendingRequestTotalCount() {
		return executeQueueRuntimePendingRequestTotalCount;
	}
	public void setExecuteQueueRuntimePendingRequestTotalCount(
			String executeQueueRuntimePendingRequestTotalCount) {
		this.executeQueueRuntimePendingRequestTotalCount = executeQueueRuntimePendingRequestTotalCount;
	}

	
}
