package com.afunms.biosreport.model;

/**
 * Oracle 数据库模板
 * 
 * @author Administrator
 *
 */
public class DbOracleReportModel  extends BiosReportBaseModel{

	public DbOracleReportModel(){
	}
	
	public DbOracleReportModel(String ip, String name, String connectivty){
		super(ip, name, connectivty);
	}
	
	// 缓冲区命中率
	private String bufferCache;
	
	// 数据字典命中率
	private String dictionaryCache;
	
	// PGA
	private String pga;
	
	// sga
	private String sga;
	
	// 打开的游标数
	private String openCur;
	
	// 内存中的排序
	private String memorySort;

	public String toString(){
		return getClass().getName()
			+ "[IP=" + super.getIpAddress()
			+ ", aliasName=" + super.getAliasName()
			+ ", nodeid=" + super.getNodeid()
			+ ", collecttime=" + super.getCollectTime()
			+ ", dept=" + super.getDeptName()
			+ ", ping=" + super.getAvergeConnectivity()
			+ ", bufferCache=" + getBufferCache()
			+ ", dictionaryCache=" + getDictionaryCache()
			+ ", pga=" + getPga()
			+ ", sga=" + getSga()
			+ ", openCur=" + getOpenCur()
			+ ", memorySort=" + getMemorySort()
			+ ", alarmCommon=" + getAlarmCommon()
			+ ", alarmSerious=" + getAlarmSerious()
			+ ", alarmUrgency=" + getAlarmUrgency();
	}
	
	public String getBufferCache() {
		return bufferCache;
	}

	public void setBufferCache(String bufferCache) {
		this.bufferCache = get2(bufferCache);
	}

	public String getDictionaryCache() {
		return dictionaryCache;
	}

	public void setDictionaryCache(String dictionaryCache) {
		this.dictionaryCache = get2(dictionaryCache);
	}

	public String getPga() {
		return pga;
	}

	public void setPga(String pga) {
		this.pga = get2(pga);
	}

	public String getSga() {
		return sga;
	}

	public void setSga(String sga) {
		this.sga = get2(sga);
	}

	public String getOpenCur() {
		return openCur;
	}

	public void setOpenCur(String openCur) {
		this.openCur = get2(openCur);
	}

	public String getMemorySort() {
		return memorySort;
	}

	public void setMemorySort(String memorySort) {
		this.memorySort = get2(memorySort);
	}
	
}
