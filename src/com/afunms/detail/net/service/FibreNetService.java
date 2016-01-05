package com.afunms.detail.net.service;

import java.util.List;

import com.afunms.detail.reomte.model.DetailTabRemote;
import com.afunms.detail.reomte.model.FibreCapabilityInfo;
import com.afunms.detail.reomte.model.FibreConfigInfo;
import com.afunms.detail.reomte.model.LightInfo;
import com.afunms.detail.reomte.model.ProcessInfo;
import com.afunms.detail.service.fibreInfo.FibreCapabilityInfoService;
import com.afunms.detail.service.fibreInfo.FibreConfigInfoService;
import com.afunms.detail.service.fibreInfo.LightInfoService;
import com.afunms.detail.service.processInfo.ProcessInfoService;


public class FibreNetService extends NetService{
	
	public FibreNetService(String nodeid, String type, String subtype) {
		super(nodeid, type, subtype);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * ��ȡ�����豸�� tab ҳ��Ϣ
	 * @return
	 */
	public List<DetailTabRemote> getTabInfo(){
		String file = "/detail/net/fibrenetdetailtab.xml";
		// ���ø���Ľ��� tab Ҳ��Ϣ
		return praseDetailTabXML(file);
	}
	
	
	/**
	 * ��ȡ������Ϣ
	 * @return ���� ��ǰ���� ����Ϣ
	 */
	public List<ProcessInfo> getProcessInfo(){
		ProcessInfoService processInfoService = new ProcessInfoService(this.nodeid, this.type, this.subtype);
		return processInfoService.getCurrProcessInfo();
	}
	
	
	/**
	 * ��ȡ�źŵ���Ϣ
	 * @return ���� ��ǰ�źŵ��б� ����Ϣ
	 */
	public List<LightInfo> getLightInfo(){
		LightInfoService lightInfoService = new LightInfoService(this.nodeid, this.type, this.subtype);
		return lightInfoService.getCurrLightInfo();
	}
	
	
	/**
	 * ��ȡ���������Ϣ
	 * @return ���� ��ǰ��������б� ����Ϣ
	 */
	public List<FibreConfigInfo> getFibreConfigInfo(){
		FibreConfigInfoService fibreConfigInfoService = new FibreConfigInfoService(this.nodeid, this.type, this.subtype);
		return fibreConfigInfoService.getFibreConfigInfo();
	}
	
	/**
	 * ��ȡ���������Ϣ
	 * @return ���� ��ǰ��������б� ����Ϣ
	 */
	public List<FibreCapabilityInfo> getFibreCapabilityInfo(){
		FibreCapabilityInfoService fibrecCapabilityInfoService = new FibreCapabilityInfoService(this.nodeid, this.type, this.subtype);
		return fibrecCapabilityInfoService.getFibreCapabilityInfo();
	}


}
