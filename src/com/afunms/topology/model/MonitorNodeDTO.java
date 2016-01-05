package com.afunms.topology.model;

import java.util.Hashtable;
import java.util.List;

import com.afunms.common.base.BaseVo;

public class MonitorNodeDTO extends BaseVo{
	
	private int id; 
	
	private String ipAddress;
	
	private String alias;
	
	private String status;
	
	private String category;
	
	private String type;
	
	private String subtype;
	
	private String pingValue;  
	
	private String cpuValue;  
	
	private String memoryValue;
	
	private String cpuValueColor;  
	
	private String memoryValueColor;
	
	private String inutilhdxValue;
	
	private String oututilhdxValue;
	
	private String eventListCount;
	
	private String collectType;
	
	private Hashtable eventListSummary;
	
	//硬盘，key盘符、value利用率	wupinlong add 2013/11/1
	private List hardDisk;
	
	private int entityNumber;//接口数量

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	public int getEntityNumber() {
		return entityNumber;
	}

	public void setEntityNumber(int entityNumber) {
		this.entityNumber = entityNumber;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the pingValue
	 */
	public String getPingValue() {
		return pingValue;
	}

	/**
	 * @param pingValue the pingValue to set
	 */
	public void setPingValue(String pingValue) {
		this.pingValue = pingValue;
	}

	/**
	 * @return the cpuValue
	 */
	public String getCpuValue() {
		return cpuValue;
	}

	/**
	 * @param cpuValue the cpuValue to set
	 */
	public void setCpuValue(String cpuValue) {
		this.cpuValue = cpuValue;
	}

	/**
	 * @return the memoryValue
	 */
	public String getMemoryValue() {
		return memoryValue;
	}

	/**
	 * @param memoryValue the memoryValue to set
	 */
	public void setMemoryValue(String memoryValue) {
		this.memoryValue = memoryValue;
	}

	/**
	 * @return the cpuValueColor
	 */
	public String getCpuValueColor() {
		return cpuValueColor;
	}

	/**
	 * @param cpuValueColor the cpuValueColor to set
	 */
	public void setCpuValueColor(String cpuValueColor) {
		this.cpuValueColor = cpuValueColor;
	}

	/**
	 * @return the memoryValueColor
	 */
	public String getMemoryValueColor() {
		return memoryValueColor;
	}

	/**
	 * @param memoryValueColor the memoryValueColor to set
	 */
	public void setMemoryValueColor(String memoryValueColor) {
		this.memoryValueColor = memoryValueColor;
	}

	/**
	 * @return the inutilhdxValue
	 */
	public String getInutilhdxValue() {
		return inutilhdxValue;
	}

	/**
	 * @param inutilhdxValue the inutilhdxValue to set
	 */
	public void setInutilhdxValue(String inutilhdxValue) {
		this.inutilhdxValue = inutilhdxValue;
	}

	/**
	 * @return the oututilhdxValue
	 */
	public String getOututilhdxValue() {
		return oututilhdxValue;
	}

	/**
	 * @param oututilhdxValue the oututilhdxValue to set
	 */
	public void setOututilhdxValue(String oututilhdxValue) {
		this.oututilhdxValue = oututilhdxValue;
	}

	/**
	 * @return the eventListCount
	 */
	public String getEventListCount() {
		return eventListCount;
	}

	/**
	 * @param eventListCount the eventListCount to set
	 */
	public void setEventListCount(String eventListCount) {
		this.eventListCount = eventListCount;
	}

	/**
	 * @return the collectType
	 */
	public String getCollectType() {
		return collectType;
	}

	/**
	 * @param collectType the collectType to set
	 */
	public void setCollectType(String collectType) {
		this.collectType = collectType;
	}

	/**
	 * @return the eventListSummary
	 */
	public Hashtable getEventListSummary() {
		return eventListSummary;
	}

	/**
	 * @param eventListSummary the eventListSummary to set
	 */
	public void setEventListSummary(Hashtable eventListSummary) {
		this.eventListSummary = eventListSummary;
	}

	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * getHardDisk:
	 * <p>
	 *
	 * @return  List
	 *          -
	 * @since   v1.01
	 */
	public List getHardDisk() {
		return hardDisk;
	}

	/**
	 * setHardDisk:
	 * <p>
	 *
	 * @param   hardDisk
	 *          -
	 * @since   v1.01
	 */
	public void setHardDisk(List hardDisk) {
		this.hardDisk = hardDisk;
	}
	

}
