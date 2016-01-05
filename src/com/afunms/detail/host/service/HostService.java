package com.afunms.detail.host.service;

import java.util.List;


import com.afunms.common.util.SysLogger;
import com.afunms.detail.reomte.model.DetailTabRemote;
import com.afunms.detail.reomte.model.InterfaceInfo;
import com.afunms.detail.reomte.model.ProcessInfo;
import com.afunms.detail.service.DetailService;
import com.afunms.detail.service.cpuInfo.CpuInfoService;
import com.afunms.detail.service.deviceInfo.DeviceInfoService;
import com.afunms.detail.service.ipMacInfo.IpMacInfoService;
import com.afunms.detail.service.processInfo.ProcessInfoService;
import com.afunms.detail.service.serviceInfo.ServiceInfoService;
import com.afunms.detail.service.sofwareInfo.SoftwareInfoService;
import com.afunms.detail.service.storageInfo.StorageInfoService;
import com.afunms.polling.om.IpMac;
import com.afunms.temp.model.DeviceNodeTemp;
import com.afunms.temp.model.NodeTemp;
import com.afunms.temp.model.ServiceNodeTemp;
import com.afunms.temp.model.SoftwareNodeTemp;
import com.afunms.temp.model.StorageNodeTemp;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;

public class HostService extends DetailService{
	
	protected HostNode hostNode;
	
	public HostService(String nodeid, String type, String subtype) {
		super(nodeid, type, subtype);
	}

	protected void init(){
		HostNodeDao hostNodeDao = new HostNodeDao();
		try {
			setHostNode((HostNode)hostNodeDao.findByID(nodeid));
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			hostNodeDao.close();
		}
		super.init(this.hostNode);
	}
	
	
	/**
	 * @return the hostNode
	 */
	public HostNode getHostNode() {
		return hostNode;
	}

	/**
	 * @param hostNode the hostNode to set
	 */
	public void setHostNode(HostNode hostNode) {
		this.hostNode = hostNode;
	}
	
	/**
	 * ��ȡ������������ tab ҳ��Ϣ
	 * @return
	 */
	public List<DetailTabRemote> getTabInfo(){
		String file = "/detail/host/hostdetailtab.xml";
		// ���ø���Ľ��� tab Ҳ��Ϣ
		SysLogger.info(file);
		return praseDetailTabXML(file);
	}
	
	
	/**
	 * ��ȡ�����豸��������Ϣ
	 * @param category	--- �豸���
	 * @return
	 */
	public String getCategoryInfo(){
		// ���ø��෽��
		return getCategoryInfo(this.hostNode.getCategory()+"");
	}
	
	/**
	 * ��ȡ�����豸�Ĺ�Ӧ�̵���Ϣ
	 * @return
	 */
	public String getSupperInfo(){
		// ���ø��෽��
		return getSupperInfo(this.hostNode.getSupperid() + "");
	}
	
	/**
	 * ��ȡ�������豸�Ľӿڵ���Ϣ
	 * @return
	 */
	public List<InterfaceInfo> getInterfaceInfo(){
		String[] subentities = {"index", "ifDescr", "ifSpeed", "ifOperStatus", 
				"ifOutBroadcastPkts", "ifInBroadcastPkts", "ifOutMulticastPkts",
				"ifInMulticastPkts", "OutBandwidthUtilHdx", "InBandwidthUtilHdx",};
		// ���ø��෽��
		return getInterfaceInfo(subentities);
	}
	
	/**
	 * ��ȡÿһ�� cpu ����Ϣ
	 * ��Ϊֻ�з��������ж�� cpu ����� ���Ը÷���û�зŵ�������
	 * @return ���� ÿ�� cpu ����Ϣ
	 */
	public List<NodeTemp> getCurrPerCpuListInfo(){
		CpuInfoService cpuInfoService = new CpuInfoService(this.nodeid, this.type, this.subtype);
		return cpuInfoService.getCurrPerCpuListInfo();
	}
	
	/**
	 * ��ȡ������Ϣ
	 * @return ���� ��ͬ���ƵĽ���ͳ�ƺ���б� ����Ϣ
	 */
	public List<ProcessInfo> getProcessInfo(){
		ProcessInfoService processInfoService = new ProcessInfoService(this.nodeid, this.type, this.subtype);
		return processInfoService.getCountProcessInfoByName();
	}
	
	/**
	 * ���ؽ��̵���ϸ��Ϣ
	 * @return
	 */
	public List<ProcessInfo> getProcessDetailInfo(String processName){
		ProcessInfoService processInfoService = new ProcessInfoService(this.nodeid, this.type, this.subtype);
		return processInfoService.getCurrProcessDetailInfo(processName);
	}
	
	/**
	 * ��ȡ ARP ��Ϣ
	 */
	public List<IpMac> getARPInfo(){
		IpMacInfoService ipMacInfoService = new IpMacInfoService(this.nodeid, this.type, this.subtype);
		return ipMacInfoService.getCurrAllIpMacInfo(this.hostNode.getIpAddress());
	}
	
	/**
	 * ��ȡ�����Ϣ
	 * @return
	 */
	public List<SoftwareNodeTemp> getSoftwareInfo(){
		SoftwareInfoService softwareInfoService = new SoftwareInfoService(this.nodeid, this.type, this.subtype);
		return softwareInfoService.getCurrSoftwareInfo();
	}
	
	/**
	 * ��ȡ�����Ϣ
	 * @return
	 */
	public List<ServiceNodeTemp> getServiceInfo(){
		ServiceInfoService serviceInfoService = new ServiceInfoService(this.nodeid, this.type, this.subtype);
		return serviceInfoService.getCurrServiceInfo();
	}
	
	/**
	 * ��ȡ�豸��Ϣ
	 * @return
	 */
	public List<DeviceNodeTemp> getDeviceInfo(){
		DeviceInfoService deviceInfoService = new DeviceInfoService(this.nodeid, this.type, this.subtype);
		return deviceInfoService.getCurrDeviceInfo();
	}
	
	/**
	 * ��ȡ�洢��Ϣ
	 * @return
	 */
	public List<StorageNodeTemp> getStorageInfo(){
		StorageInfoService storageInfoService = new StorageInfoService(this.nodeid, this.type, this.subtype);
		return storageInfoService.getCurrStorageInfo();
	}
	
	
	
}
