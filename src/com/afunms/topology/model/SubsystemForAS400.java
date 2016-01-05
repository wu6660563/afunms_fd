/**
 * <p>Description:mapping table NMS_TOPO_NODE</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-12
 */

package com.afunms.topology.model;

import com.afunms.common.base.BaseVo;
import com.ibm.as400.access.ObjectDescription;

public class SubsystemForAS400 extends BaseVo {
	
	/**
	 * id
	 */
	private int id;
	
	/**
	 * ��ϵͳ�����豸id
	 */
	private String nodeid;
	
	/**
	 * ��ϵͳ�����豸ip
	 */
	private String ipaddress;
	
	/**
	 * ��ϵͳ����
	 */
	private String name;
	
	/**
	 * ��ϵͳ�����豸id
	 */
	private String currentActiveJobs;
	
	/**
	 * �Ƿ����
	 */
	private String exists;
	
	/**
	 * ·��
	 */
	private String path;
	
	/**
	 * ��ҵ��
	 */
	private String jobNum;
	
	/**
	 * ��������
	 */
	private String objectDescription;
	
	/**
	 * �ɼ�ʱ��
	 */
	private String collectTime;

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
	 * @return the nodeid
	 */
	public String getNodeid() {
		return nodeid;
	}

	/**
	 * @param nodeid the nodeid to set
	 */
	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}

	/**
	 * @return the ipaddress
	 */
	public String getIpaddress() {
		return ipaddress;
	}

	/**
	 * @param ipaddress the ipaddress to set
	 */
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the currentActiveJobs
	 */
	public String getCurrentActiveJobs() {
		return currentActiveJobs;
	}

	/**
	 * @param currentActiveJobs the currentActiveJobs to set
	 */
	public void setCurrentActiveJobs(String currentActiveJobs) {
		this.currentActiveJobs = currentActiveJobs;
	}

	/**
	 * @return the exists
	 */
	public String getExists() {
		return exists;
	}

	/**
	 * @param exists the exists to set
	 */
	public void setExists(String exists) {
		this.exists = exists;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the jobNum
	 */
	public String getJobNum() {
		return jobNum;
	}

	/**
	 * @param jobNum the jobNum to set
	 */
	public void setJobNum(String jobNum) {
		this.jobNum = jobNum;
	}

	/**
	 * @return the objectDescription
	 */
	public String getObjectDescription() {
		return objectDescription;
	}

	/**
	 * @param objectDescription the objectDescription to set
	 */
	public void setObjectDescription(String objectDescription) {
		this.objectDescription = objectDescription;
	}

	/**
	 * @return the collectTime
	 */
	public String getCollectTime() {
		return collectTime;
	}

	/**
	 * @param collectTime the collectTime to set
	 */
	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}

	
}