package com.afunms.detail.host.service;

import java.util.List;

import com.afunms.common.util.CommonUtil;
import com.afunms.common.util.SysLogger;
import com.afunms.detail.reomte.model.CpuPerfInfo;
import com.afunms.detail.reomte.model.DetailTabRemote;
import com.afunms.detail.reomte.model.DiskInfo;
import com.afunms.detail.reomte.model.DiskPerfInfo;
import com.afunms.detail.reomte.model.InterfaceInfo;
import com.afunms.detail.reomte.model.MemoryConfigInfo;
import com.afunms.detail.reomte.model.NetmediaConfigInfo;
import com.afunms.detail.reomte.model.PagePerfInfo;
import com.afunms.detail.reomte.model.PageSpaceInfo;
import com.afunms.detail.reomte.model.UserConfigInfo;
import com.afunms.detail.service.configInfo.MemoryConfigInfoService;
import com.afunms.detail.service.configInfo.NetmediaConfigInfoService;
import com.afunms.detail.service.configInfo.UserConfigInfoService;
import com.afunms.detail.service.diskInfo.DiskInfoService;
import com.afunms.detail.service.sysInfo.CpuPerfInfoService;
import com.afunms.detail.service.sysInfo.DiskPerfInfoService;
import com.afunms.detail.service.sysInfo.PagePerfInfoService;
import com.afunms.detail.service.sysInfo.PageSpaceInfoService;




public class LinuxService extends HostService{

	public LinuxService(String nodeid, String type, String subtype) {
		super(nodeid, type, subtype);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * ��ȡ Linux �������豸�� tab ҳ��Ϣ
	 * @return
	 */
	public List<DetailTabRemote> getTabInfo(){
		String file = "/detail/host/linuxdetailtab.xml";
		// ���ø���Ľ��� tab ҳ��Ϣ
		SysLogger.info(file);
		return praseDetailTabXML(file);
	}

	/**
	 * ��ȡLinux�������豸�Ľӿڵ���Ϣ
	 * @return
	 */
	public List<InterfaceInfo> getInterfaceInfo(){
		String[] subentities = {"index", "ifDescr", "ifSpeed", "ifOperStatus", 
				"OutBandwidthUtilHdx", "InBandwidthUtilHdx",};
		// ���ø��෽��
		return getInterfaceInfo(subentities);
	}
	
	
	/**
	 * ��ȡLinux�������豸�Ĵ��̵���Ϣ
	 * @return
	 */
	public List<DiskInfo> getDiskInfo(){
		DiskInfoService diskInfoService = new DiskInfoService(this.nodeid, this.type, this.subtype);
		return diskInfoService.getCurrDiskInfo();
	}
	
	/**
	 * ��ȡLinux�������豸�Ĵ���������ͼ
	 * @return
	 */
	public String getDiskInfoUtilizationImg(){
		DiskInfoService diskInfoService = new DiskInfoService(this.nodeid, this.type, this.subtype);
		List<DiskInfo> diskInfoList = diskInfoService.getCurrDiskInfo();
		String imgName = CommonUtil.ip2long(this.hostNode.getIpAddress()) + "disk";
		return diskInfoService.getCurrDiskInfoUtilizationImg(diskInfoList, "", imgName, 750, 150);
	}
	
	/**
	 * ��ȡLinux�������豸CPU������Ϣ
	 * @return
	 */
	public List<CpuPerfInfo> getCpuPerfInfo(){
		CpuPerfInfoService cpuPerfInfoService = new CpuPerfInfoService(this.nodeid, this.type, this.subtype);
		return cpuPerfInfoService.getCurrCpuPerfInfo();
	}
	
	/**
	 * ��ȡLinux�������豸����������Ϣ
	 * @return
	 */
	public List<DiskPerfInfo> getDiskPerfInfo(){
		DiskPerfInfoService diskPerfInfoService = new DiskPerfInfoService(this.nodeid, this.type, this.subtype);
		return diskPerfInfoService.getCurrDiskPerfInfo();
	}
	
	/**
	 * ��ȡLinux�������豸ҳ��������Ϣ
	 * @return
	 */
	public List<PagePerfInfo> getPagePerfInfo(){
		PagePerfInfoService pagePerfInfoService = new PagePerfInfoService(this.nodeid, this.type, this.subtype);
		return pagePerfInfoService.getCurrPagePerfInfo();
	}
	
	/**
	 * ��ȡLinux�������豸����ҳ�潻����Ϣ
	 * @return
	 */
	public List<PageSpaceInfo> getPageSpaceInfo(){
		PageSpaceInfoService pageSpaceInfoService = new PageSpaceInfoService(this.nodeid, this.type, this.subtype);
		return pageSpaceInfoService.getCurrPageSpaceInfo();
	}
	
	/**
	 * ��ȡLinux�������豸�ڴ�������Ϣ
	 * @return
	 */
	public List<MemoryConfigInfo> getMemoryConfigInfo(){
		MemoryConfigInfoService memoryConfigInfoService = new MemoryConfigInfoService(this.nodeid, this.type, this.subtype);
		return memoryConfigInfoService.getCurrMemoryConfigInfo();
	}
	
	/**
	 * ��ȡLinux�������豸����������Ϣ
	 * @return
	 */
	public List<NetmediaConfigInfo> getNetmediaConfigInfo(){
		NetmediaConfigInfoService netmediaConfigInfoService = new NetmediaConfigInfoService(this.nodeid, this.type, this.subtype);
		return netmediaConfigInfoService.getCurrNetmediaConfigInfo();
	}
	
	
	/**
	 * ��ȡLinux�������豸�û�������Ϣ
	 * @return
	 */
	public List<UserConfigInfo> getUserConfigInfo(){
		UserConfigInfoService userConfigInfoService = new UserConfigInfoService(this.nodeid, this.type, this.subtype);
		return userConfigInfoService.getCurrUserConfigInfo();
	}
	
	
	
}
