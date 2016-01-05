package com.afunms.detail.service;

import java.util.List;

import com.afunms.application.dao.StorageDao;
import com.afunms.application.model.Storage;
import com.afunms.detail.reomte.model.DetailTabRemote;
import com.afunms.detail.service.pingInfo.StoragePingInfoService;
import com.afunms.detail.service.storageInfo.StorageArrayInfoService;
import com.afunms.detail.service.storageInfo.StorageArraySiteInfoService;
import com.afunms.detail.service.storageInfo.StorageExtpoolInfoService;
import com.afunms.detail.service.storageInfo.StorageFbvolInfoService;
import com.afunms.detail.service.storageInfo.StorageHostConnectInfoService;
import com.afunms.detail.service.storageInfo.StorageIOPortInfoService;
import com.afunms.detail.service.storageInfo.StorageRankInfoService;
import com.afunms.detail.service.storageInfo.StorageVolgrpInfoService;
import com.afunms.detail.service.systemInfo.SystemInfoService;
import com.afunms.temp.model.NodeTemp;
import com.afunms.temp.model.StorageArrayNodeTemp;
import com.afunms.temp.model.StorageArraySiteNodeTemp;
import com.afunms.temp.model.StorageExtpoolNodeTemp;
import com.afunms.temp.model.StorageFbvolNodeTemp;
import com.afunms.temp.model.StorageHostConnectNodeTemp;
import com.afunms.temp.model.StorageIOPortNodeTemp;
import com.afunms.temp.model.StorageRankNodeTemp;
import com.afunms.temp.model.StorageVolgrpNodeTemp;

/**
 * ���ڴ洢�豸 ��û��  ��  type �� subtype ����
 * �̳��� detailService ����д init() ����
 */

public class StorageService extends DetailService {
	
	protected Storage storage;

	public StorageService(String nodeid, String type, String subtype) {
		super(nodeid, type, subtype);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return the storage
	 */
	public Storage getStorage() {
		return storage;
	}

	/**
	 * @param storage the storage to set
	 */
	public void setStorage(Storage storage) {
		this.storage = storage;
	}
	
	public void init(){
		StorageDao storageDao = new StorageDao();
		try {
			storage = (Storage)storageDao.findByID(nodeid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			storageDao.close();
		}
	}
	
	/**
	 * ��ȡ�����豸�� tab ҳ��Ϣ
	 * @return
	 */
	public List<DetailTabRemote> getTabInfo(){
		String file = "/detail/storage/storagedetailtab.xml";
		// ���ø���Ľ��� tab Ҳ��Ϣ
		return praseDetailTabXML(file);
	}
	
	/**
	 * ��ȡ�豸ϵͳ��Ϣ
	 * @return ���� NodeTemp �б�
	 */
	public List<NodeTemp> getSystemInfo(){
		return null;
	}
	
	/**
	 * ��ȡ�洢�豸���ͺ���Ϣ
	 * @return	���� �ͺ� �� String ��ʽ
	 */
	public String getProducerInfo(){
		SystemInfoService systemInfoService = new SystemInfoService(this.nodeid, this.type, this.subtype);
		return systemInfoService.getStorageProducerInfo(this.storage.getType());

	}
	
	/**
	 * ��ȡ������ͨ��ƽ��ֵ����Ϣ
	 * @return	���� ��ͨ��ƽ��ֵ�� String ��ʽ
	 */
	public String getCurrDayPingAvgInfo(){
		StoragePingInfoService storagePingInfoService = new StoragePingInfoService(this.nodeid, this.type, this.subtype);
		return storagePingInfoService.getCurrDayPingAvgInfo(this.storage.getIpaddress());
	}
	
	
	/**
	 * ��ȡ�洢�豸�� ArraySiteInfo ��Ϣ
	 * @return	���� StorageArraySiteNodeTemp ���б�
	 */
	public List<StorageArraySiteNodeTemp> getArraySiteInfo(){
		StorageArraySiteInfoService storageArraySiteInfoService = new StorageArraySiteInfoService(this.nodeid, this.type, this.subtype);
		return storageArraySiteInfoService.getCurrArraySiteInfo();
	}
	
	/**
	 * ��ȡ�洢�豸�� ArrayInfo ��Ϣ
	 * @return	���� StorageArrayNodeTemp ���б�
	 */
	public List<StorageArrayNodeTemp> getArrayInfo(){
		StorageArrayInfoService storageArrayInfoService = new StorageArrayInfoService(this.nodeid, this.type, this.subtype);
		return storageArrayInfoService.getCurrArrayInfo();
	}

	
	/**
	 * ��ȡ�洢�豸�� RankInfo ��Ϣ
	 * @return	���� StorageRankNodeTemp ���б�
	 */
	public List<StorageRankNodeTemp> getRankInfo(){
		StorageRankInfoService storageRankInfoService = new StorageRankInfoService(this.nodeid, this.type, this.subtype);
		return storageRankInfoService.getCurrRankInfo();
	}
	
	/**
	 * ��ȡ�洢�豸�� ExtpoolInfo ��Ϣ
	 * @return	���� StorageExtpoolNodeTemp ���б�
	 */
	public List<StorageExtpoolNodeTemp> getExtpoolInfo(){
		StorageExtpoolInfoService storageExtpoolInfoService = new StorageExtpoolInfoService(this.nodeid, this.type, this.subtype);
		return storageExtpoolInfoService.getCurrExtpoolInfo();
	}
	
	
	/**
	 * ��ȡ�洢�豸�� FbvolInfo ��Ϣ
	 * @return	���� StorageFbvolNodeTemp ���б�
	 */
	public List<StorageFbvolNodeTemp> getFbvolInfo(){
		StorageFbvolInfoService storageFbvolInfoService = new StorageFbvolInfoService(this.nodeid, this.type, this.subtype);
		return storageFbvolInfoService.getCurrFbvolInfo();
	}
	
	/**
	 * ��ȡ�洢�豸�� VolgrpInfo ��Ϣ
	 * @return	���� StorageVolgrpNodeTemp ���б�
	 */
	public List<StorageVolgrpNodeTemp> getVolgrpInfo(){
		StorageVolgrpInfoService storageVolgrpInfoService = new StorageVolgrpInfoService(this.nodeid, this.type, this.subtype);
		return storageVolgrpInfoService.getCurrVolgrpInfo();
	}
	
	/**
	 * ��ȡ�洢�豸�� IOPortInfo ��Ϣ
	 * @return	���� StorageIOPortNodeTemp ���б�
	 */
	public List<StorageIOPortNodeTemp> getIOPortInfo(){
		StorageIOPortInfoService storageIOPortInfoService = new StorageIOPortInfoService(this.nodeid, this.type, this.subtype);
		return storageIOPortInfoService.getCurrIOPortInfo();
	}
	
	/**
	 * ��ȡ�洢�豸�� HostConnectInfo ��Ϣ
	 * @return	���� StorageHostConnectNodeTemp ���б�
	 */
	public List<StorageHostConnectNodeTemp> getHostConnectInfo(){
		StorageHostConnectInfoService storageHostConnectInfoService = new StorageHostConnectInfoService(this.nodeid, this.type, this.subtype);
		return storageHostConnectInfoService.getCurrHostConnectInfo();
	}
	
	/**
	 * ��ȡ�洢�豸�� getVolgrpFbvolInfo ��Ϣ
	 * @return	���� StorageFbvolNodeTemp ���б�
	 */
	public List<StorageFbvolNodeTemp> getVolgrpFbvolInfo(String volgrp_id){
		StorageFbvolInfoService storageFbvolInfoService = new StorageFbvolInfoService(this.nodeid, this.type, this.subtype);
		return storageFbvolInfoService.getCurrVolgrpFbvolInfo(volgrp_id);
	}
	
}
