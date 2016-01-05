package com.afunms.detail.host.service;

import java.util.List;

import com.afunms.common.util.SysLogger;
import com.afunms.detail.reomte.model.DetailTabRemote;
import com.afunms.detail.service.diskInfo.DiskInfoService;
import com.afunms.detail.service.sysInfo.JobInfoService;
import com.afunms.detail.service.sysInfo.SubsystemInfoService;
import com.afunms.detail.service.sysInfo.SystemPoolInfoService;
import com.afunms.detail.service.sysInfo.SystemValueInfoService;
import com.afunms.topology.model.DiskForAS400;
import com.afunms.topology.model.JobForAS400;
import com.afunms.topology.model.SubsystemForAS400;
import com.afunms.topology.model.SystemPoolForAS400;
import com.afunms.topology.model.SystemValueForAS400;




public class AS400Service extends HostService{

	public AS400Service(String nodeid, String type, String subtype) {
		super(nodeid, type, subtype);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * ��ȡ AS400 �������豸�� tab ҳ��Ϣ
	 * @return
	 */
	public List<DetailTabRemote> getTabInfo(){
		String file = "/detail/host/as400detailtab.xml";
		// ���ø���Ľ��� tab Ҳ��Ϣ
		SysLogger.info(file);
		return praseDetailTabXML(file);
	}
	
	/**
	 * ��ȡ AS400 �������豸��ϵͳ״̬��Ϣ
	 * @return
	 */
	public List<SystemValueForAS400> getSystemValueInfo(){
		SystemValueInfoService systemValueInfoService = new SystemValueInfoService(this.nodeid, this.type, this.subtype);
		return systemValueInfoService.getCurrSystemValueForAS400Info();
	}
	
	
	/**
	 * ��ȡ AS400 �������豸��ϵͳ״̬��Ϣ
	 * @return
	 */
	public List<SystemPoolForAS400> getSystemPoolInfo(){
		SystemPoolInfoService systemPoolInfoService = new SystemPoolInfoService(this.nodeid, this.type, this.subtype);
		return systemPoolInfoService.getCurrSystemPoolForAS400Info();
	}
	
	/**
	 * ��ȡ AS400 �������豸��ϵͳ״̬��Ϣ
	 * @return
	 */
	public List<DiskForAS400> getDiskInfo(){
		DiskInfoService diskInfoService = new DiskInfoService(this.nodeid, this.type, this.subtype);
		return diskInfoService.getCurrDiskForAS400Info();
	}
	
	/**
	 * ��ȡ AS400 �������豸����ҵ��Ϣ
	 * @return
	 */
	public List<JobForAS400> getJobInfo(String jobType, String jobSubtype, String jobActivestatus, String jobSubsystem){
		JobInfoService jobInfoService = new JobInfoService(this.nodeid, this.type, this.subtype);
		return jobInfoService.getCurrJobForAS400Info(jobType, jobSubtype, jobActivestatus, jobSubsystem);
	}
	
	/**
	 * ��ȡ AS400 �������豸����ҵ������Ϣ
	 * @return
	 */
	public List<String[]> getJobTypeInfo(){
		JobInfoService jobInfoService = new JobInfoService(this.nodeid, this.type, this.subtype);
		return jobInfoService.getAllJobTypeInfo();
	}
	
	/**
	 * ��ȡ AS400 �������豸����ҵ��������Ϣ
	 * @return
	 */
	public List<String[]> getJobSubtypeInfo(){
		JobInfoService jobInfoService = new JobInfoService(this.nodeid, this.type, this.subtype);
		return jobInfoService.getAllJobSubtypeInfo();
	}
	
	/**
	 * ��ȡ AS400 �������豸����ҵ�״̬��Ϣ
	 * @return
	 */
	public List<String[]> getJobActiveStatusInfo(){
		JobInfoService jobInfoService = new JobInfoService(this.nodeid, this.type, this.subtype);
		return jobInfoService.getAllJobActiveStatusInfo();
	}
	
	/**
	 * ��ȡ AS400 �������豸����ϵͳ��Ϣ
	 * @return
	 */
	public List<SubsystemForAS400> getSubsystemInfo(){
		SubsystemInfoService subsystemInfoService = new SubsystemInfoService(this.nodeid, this.type, this.subtype);
		return subsystemInfoService.getCurrSubsystemForAS400Info();
	}
	
	
	
}
