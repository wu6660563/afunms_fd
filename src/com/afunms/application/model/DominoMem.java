/**
 * <p>Description:mapping app_db_node</p>
 * <p>Company: dhcc.com</p>
 * @author miiwill
 * @project afunms
 * @date 2007-1-7
 */

package com.afunms.application.model;

import com.afunms.common.base.BaseVo;

public class DominoMem extends BaseVo{
	private String memAllocate = "";//�ڴ����� wxy edit
	private String memAllocateProcess = "";//����ʹ�õ��ڴ�
	private String memAllocateShare = "";//�����ڴ�
	private String memPhysical = "";//�����ڴ�
	private String memFree = "";//���������ڴ�
	private String platformMemPhyPctUtil = "";//ƽ̨�����ڴ�������
	private String platformMemPhysical = "";//ƽ̨�����ڴ��С
	private String mempctutil = "";//�����ڴ�ʹ���� wxy edit
	
	
	public DominoMem(){
		memAllocate = "";//��ʹ���ڴ�
		memAllocateProcess = "";//����ʹ�õ��ڴ�
		memAllocateShare = "";//�����ڴ�
		memPhysical = "";//�����ڴ�		
		memFree = "";
		mempctutil= "0";
	}
	public String getMemAllocate() {
		return memAllocate;
	}
	public void setMemAllocate(String memAllocate) {
		this.memAllocate = memAllocate;
	}
	public String getMemAllocateProcess() {
		return memAllocateProcess;
	}
	public void setMemAllocateProcess(String memAllocateProcess) {
		this.memAllocateProcess = memAllocateProcess;
	}
	public String getMemAllocateShare() {
		return memAllocateShare;
	}
	public void setMemAllocateShare(String memAllocateShare) {
		this.memAllocateShare = memAllocateShare;
	}
	public String getMemPhysical() {
		return memPhysical;
	}
	public void setMemPhysical(String memPhysical) {
		this.memPhysical = memPhysical;
	}
	public String getMemFree() {
		return memFree;
	}
	public void setMemFree(String memFree) {
		this.memFree = memFree;
	}
	public String getPlatformMemPhyPctUtil() {
		return platformMemPhyPctUtil;
	}
	public void setPlatformMemPhyPctUtil(String platformMemPhyPctUtil) {
		this.platformMemPhyPctUtil = platformMemPhyPctUtil;
	}
	public String getPlatformMemPhysical() {
		return platformMemPhysical;
	}
	public void setPlatformMemPhysical(String platformMemPhysical) {
		this.platformMemPhysical = platformMemPhysical;
	}
	public String getMempctutil() {
		return mempctutil;
	}
	public void setMempctutil(String mempctutil) {
		this.mempctutil = mempctutil;
	}

}