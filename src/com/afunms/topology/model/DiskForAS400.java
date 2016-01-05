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

public class DiskForAS400 extends BaseVo {
	
	/**
	 * id
	 */
	private int id;
	
	/**
	 * ���������豸id
	 */
	private String nodeid;
	
	/**
	 * ���������豸ip
	 */
	private String ipaddress;
	
	/**
	 * ���̵�Ԫ
	 */
	private String unit;
	
	/**
	 * ��������
	 */
	private String type;
	
	/**
	 * ���̴�С(M)
	 */
	private String size;
	
	/**
	 * %�������ðٷֱ�
	 */
	private String used;
	
	/**
	 * IO Rqs
	 */
	private String ioRqs;
	
	/**
	 * ������Ĵ�С(K)
	 */
	private String requestSize;
	
	/**
	 * �� Rqs
	 */
	private String readRqs;
	
	/**
	 * д Rqs
	 */
	private String writeRqs;
	
	/**
	 * ��(K)
	 */
	private String read;
	
	/**
	 * д(K)
	 */
	private String write;
	
	/**
	 * %æµ�ٷֱ�
	 */
	private String busy;
	
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
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the size
	 */
	public String getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * @return the used
	 */
	public String getUsed() {
		return used;
	}

	/**
	 * @param used the used to set
	 */
	public void setUsed(String used) {
		this.used = used;
	}

	/**
	 * @return the ioRqs
	 */
	public String getIoRqs() {
		return ioRqs;
	}

	/**
	 * @param ioRqs the ioRqs to set
	 */
	public void setIoRqs(String ioRqs) {
		this.ioRqs = ioRqs;
	}

	/**
	 * @return the requestSize
	 */
	public String getRequestSize() {
		return requestSize;
	}

	/**
	 * @param requestSize the requestSize to set
	 */
	public void setRequestSize(String requestSize) {
		this.requestSize = requestSize;
	}

	/**
	 * @return the readRqs
	 */
	public String getReadRqs() {
		return readRqs;
	}

	/**
	 * @param readRqs the readRqs to set
	 */
	public void setReadRqs(String readRqs) {
		this.readRqs = readRqs;
	}

	/**
	 * @return the writeRqs
	 */
	public String getWriteRqs() {
		return writeRqs;
	}

	/**
	 * @param writeRqs the writeRqs to set
	 */
	public void setWriteRqs(String writeRqs) {
		this.writeRqs = writeRqs;
	}

	/**
	 * @return the read
	 */
	public String getRead() {
		return read;
	}

	/**
	 * @param read the read to set
	 */
	public void setRead(String read) {
		this.read = read;
	}

	/**
	 * @return the write
	 */
	public String getWrite() {
		return write;
	}

	/**
	 * @param write the write to set
	 */
	public void setWrite(String write) {
		this.write = write;
	}

	/**
	 * @return the busy
	 */
	public String getBusy() {
		return busy;
	}

	/**
	 * @param busy the busy to set
	 */
	public void setBusy(String busy) {
		this.busy = busy;
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