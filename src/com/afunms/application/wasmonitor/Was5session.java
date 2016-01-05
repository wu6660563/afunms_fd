package com.afunms.application.wasmonitor;

import com.afunms.common.base.BaseVo;

public class Was5session extends BaseVo{
	String ipaddress;
    java.util.Calendar recordtime;
	long liveCount;
	long createCount;
	long invalidateCount;
	long lifeTime;
	long activeCount;
	long timeoutInvalidationCount;
	public String getIpaddress() {
		return ipaddress;
	}
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	public java.util.Calendar getRecordtime() {
		return recordtime;
	}
	public void setRecordtime(java.util.Calendar recordtime) {
		this.recordtime = recordtime;
	}
	public long getLiveCount() {
		return liveCount;
	}
	public void setLiveCount(long liveCount) {
		this.liveCount = liveCount;
	}
	public long getCreateCount() {
		return createCount;
	}
	public void setCreateCount(long createCount) {
		this.createCount = createCount;
	}
	public long getInvalidateCount() {
		return invalidateCount;
	}
	public void setInvalidateCount(long invalidateCount) {
		this.invalidateCount = invalidateCount;
	}
	public long getLifeTime() {
		return lifeTime;
	}
	public void setLifeTime(long lifeTime) {
		this.lifeTime = lifeTime;
	}
	public long getActiveCount() {
		return activeCount;
	}
	public void setActiveCount(long activeCount) {
		this.activeCount = activeCount;
	}
	public long getTimeoutInvalidationCount() {
		return timeoutInvalidationCount;
	}
	public void setTimeoutInvalidationCount(long timeoutInvalidationCount) {
		this.timeoutInvalidationCount = timeoutInvalidationCount;
	}
	
	
}
