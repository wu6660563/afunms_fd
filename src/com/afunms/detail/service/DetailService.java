package com.afunms.detail.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.afunms.common.base.BaseVo;
import com.afunms.common.util.SysLogger;
import com.afunms.detail.reomte.model.DetailTabRemote;
import com.afunms.detail.reomte.model.DetailTitleRemote;
import com.afunms.detail.reomte.model.InterfaceInfo;
import com.afunms.detail.service.cpuInfo.CpuInfoService;
import com.afunms.detail.service.envInfo.ENVInfoService;
import com.afunms.detail.service.eventListInfo.EventListInfoService;
import com.afunms.detail.service.interfaceInfo.InterfaceInfoService;
import com.afunms.detail.service.memoryInfo.FlashInfoService;
import com.afunms.detail.service.memoryInfo.MemoryInfoService;
import com.afunms.detail.service.pingInfo.PingInfoService;
import com.afunms.detail.service.syslogInfo.SyslogInfoService;
import com.afunms.detail.service.systemInfo.SystemInfoService;
import com.afunms.event.model.EventList;
import com.afunms.event.model.Syslog;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.initialize.ResourceCenter;
import com.afunms.temp.model.NodeTemp;

public class DetailService {

	protected NodeDTO nodeDTO;
	
	protected String nodeid;
	
	protected String type;
	
	protected String subtype;
	
	

	public DetailService(String nodeid, String type, String subtype) {
		this.nodeid = nodeid;
		this.type = type;
		this.subtype = subtype;
		init();
	}
	
	/**
	 * @return the nodeDTO
	 */
	public NodeDTO getNodeDTO() {
		return nodeDTO;
	}

	/**
	 * @param nodeDTO the nodeDTO to set
	 */
	public void setNodeDTO(NodeDTO nodeDTO) {
		this.nodeDTO = nodeDTO;
	}
	
	protected void init(){
		init(null);
	}

	protected void init(BaseVo baseVo) {
		NodeUtil nodeUtil = new NodeUtil();
		if(baseVo == null){
			List<BaseVo> list = nodeUtil.getNodeByTyeAndSubtype(type, subtype);
			List<NodeDTO> nodeDTOList = nodeUtil.conversionToNodeDTO(list);
			Iterator<NodeDTO> iterator = nodeDTOList.iterator();
			while (iterator.hasNext()) {
				NodeDTO elem = (NodeDTO) iterator.next();
				if (String.valueOf(elem.getId()).equals(nodeid)) {
					setNodeDTO(elem);
					break;
				}
			}
		} else {
			setNodeDTO(nodeUtil.conversionToNodeDTO(baseVo));
		}
	}

	/**
	 * ���ڽ��� ��ϸ��Ϣ�� tab ҳ
	 * �÷���Ӧ������������
	 * @param file   tab.xml �ļ� ��ʽΪ Ӧ��·���� /.../...
	 * @return
	 */
	protected List<DetailTabRemote> praseDetailTabXML(String file) {
		List<DetailTabRemote> tabList = new ArrayList<DetailTabRemote>();
		SAXBuilder builder = new SAXBuilder();
		try {
			// ��Ŀ·�� /afunms
			String sysPath = ResourceCenter.getInstance().getSysPath();
			Document doc = builder.build(new File(sysPath + file));
			List allTab = doc.getRootElement().getChildren("tab");
			Iterator tabIterator = allTab.iterator();
			while (tabIterator.hasNext()) {
				// ѭ��ÿ����ǩҳ����ȡÿ����ǩҳ�ı���
				Element tabElement = (Element) tabIterator.next();
				// ��ǩҳ id
				String tabId = (String)tabElement.getAttributeValue("id");
				// ��ǩҳ name
				String tabName =(String)tabElement.getAttributeValue("title");
				// ��ǩҳ action
				String tabAction = (String)tabElement.getAttributeValue("action");
				
				// ��ǩҳ �����б�
				List<DetailTitleRemote> titleList = new ArrayList<DetailTitleRemote>();
				List allTitleTr = tabElement.getChildren();
				Iterator<Element> titleTrIterator = allTitleTr.iterator();
				int rowNum = 0;			// �����к�
				while (titleTrIterator.hasNext()) {
					Element titleTrElement = (Element) titleTrIterator.next();
					List titleTr_TitleList = titleTrElement.getChildren();
					Iterator<Element>  titleTr_TitleIterator = titleTr_TitleList.iterator();
					while (titleTr_TitleIterator.hasNext()) {
						// ѭ��ÿһ�� title-tr
						Element titleElement = (Element) titleTr_TitleIterator.next();
						
						String titleName = titleElement.getText();
						String titleId = titleElement.getAttributeValue("id");
						String cols = titleElement.getAttributeValue("cols");
						if(cols == null || "".equals(cols.trim())){
							cols = "1";
						}
						
						DetailTitleRemote detailTitleRemote = new DetailTitleRemote();
						detailTitleRemote.setId(titleId);
						detailTitleRemote.setContent(titleName);
						detailTitleRemote.setCols(cols);
						detailTitleRemote.setRowNum(rowNum+"");
						titleList.add(detailTitleRemote);
					}
					rowNum++;
				}
				DetailTabRemote detailTabRemote = new DetailTabRemote();
				detailTabRemote.setId(tabId);
				detailTabRemote.setName(tabName);
				detailTabRemote.setAction(tabAction);
				detailTabRemote.setTitleList(titleList);
				tabList.add(detailTabRemote);
			}
		} catch (Exception e) {
			SysLogger.error("DetailService.praseDetailTabXML()", e);
		}
		
		for(int i = 0 ; i< tabList.size(); i++){
			System.out.println(tabList.get(i).getName());
		}
		return tabList;
	}
	
	/**
	 * ��ȡ�豸ϵͳ��Ϣ
	 * @return ���� NodeTemp �б�
	 */
	public List<NodeTemp> getSystemInfo(){
		SystemInfoService systemInfoService = new SystemInfoService(this.nodeid, this.type, this.subtype);
		return systemInfoService.getCurrAllSystemInfo();
	}
	
	/**
	 * ��ȡ�豸PING��Ϣ
	 * @return ���� NodeTemp �б�
	 */
	public List<NodeTemp> getCurrPingInfo(){
		PingInfoService pingInfoService = new PingInfoService(this.nodeid, this.type, this.subtype);
		return pingInfoService.getCurrPingInfo(null);
	}
	
	/**
	 * ��ȡ������ͨ��ƽ��ֵ����Ϣ
	 * @return	���� ��ͨ��ƽ��ֵ�� String ��ʽ
	 */
	public String getCurrDayPingAvgInfo(){
		PingInfoService pingInfoService = new PingInfoService(this.nodeid, this.type, this.subtype);
		return pingInfoService.getCurrDayPingAvgInfo(this.nodeDTO.getIpaddress());
	}
	
	/**
	 * ��ȡ��ǰ�ڴ�ֵ����Ϣ
	 * @return	���� NodeTemp �б�
	 */
	public List<NodeTemp> getCurrMemoryInfo(){
		MemoryInfoService memoryInfoService = new MemoryInfoService(this.nodeid, this.type, this.subtype);
		return memoryInfoService.getCurrPerMemoryListInfo();
	}
	
	/**
	 * ��ȡ��ǰ����ֵ����Ϣ
	 * @return	���� NodeTemp �б�
	 */
	public List<NodeTemp> getCurrFlashInfo(){
		FlashInfoService flashInfoService = new FlashInfoService(this.nodeid, this.type, this.subtype);
		return flashInfoService.getCurrPerFlashListInfo();
	}
	
	/**
	 * ��ȡ��ǰ��������ֵ����Ϣ
	 * @return	���� NodeTemp �б�
	 */
	public List<NodeTemp> getCurrENVInfo(){
		ENVInfoService envInfoService = new ENVInfoService(this.nodeid, this.type, this.subtype);
		return envInfoService.getCurrPerENVListInfo();
	}
	
	/**
	 * ��ȡ��ǰ���� CPU ƽ��ֵ����Ϣ
	 * @return	���� ��ǰ���� CPU ƽ��ֵ�� String ��ʽ
	 */
	public String getCurrCpuAvgInfo(){
		CpuInfoService cpuInfoService = new CpuInfoService(this.nodeid, this.type, this.subtype);
		return cpuInfoService.getCurrCpuAvgInfo();
	}
	
	/**
	 * ��ȡ��ǰ״̬����Ϣ
	 * @return	���� ״̬�� String ��ʽ
	 */
	public String getStautsInfo(){
		SystemInfoService systemInfoService = new SystemInfoService(this.nodeid, this.type, this.subtype);
		return systemInfoService.getStautsInfo();
	}
	
	/**
	 * �����豸������Ϣ���÷���ֻ�������豸�ͷ�������Ҫ��д
	 * @param category	--- �豸���
	 * @return
	 */
	public String getCategoryInfo(String category){
		SystemInfoService systemInfoService = new SystemInfoService(this.nodeid, this.type, this.subtype);
		return systemInfoService.getCategoryInfo(Integer.valueOf(category));
	}
	
	/**
	 * ��ȡ��Ӧ����Ϣ
	 * @param supperId --- ��Ӧ��id
	 * @return ���ع�Ӧ������
	 */
	public String getSupperInfo(String supperId){
		SystemInfoService systemInfoService = new SystemInfoService(this.nodeid, this.type, this.subtype);
		return systemInfoService.getSupperInfo(supperId);
	}
	
	/**
	 * ��ȡ�豸�ӿڵ���Ϣ
	 * @param subentities	--- �ӿڵ��������飬���δ�գ���ȡȫ����Ϣ
	 * @return
	 */
	public List<InterfaceInfo> getInterfaceInfo(String[] subentities){
		InterfaceInfoService interfaceInfoService = new InterfaceInfoService(this.nodeid, this.type, this.subtype);
		return interfaceInfoService.getCurrAllInterfaceInfo(subentities);
	}
	
	/**
	 * * ��ȡ�豸�ӿڵ���Ϣ
	 * @param subentities	--- �ӿڵ��������飬���δ�գ���ȡȫ����Ϣ
	 * @param sindexs       --- ������
	 * @param starttime     --- ��ʼ����
	 * @param totime        --- ��������
	 * @return
	 */
	public List<InterfaceInfo> getInterfaceInfo(String[] subentities,String[] sindexs,String starttime,String totime){
		InterfaceInfoService interfaceInfoService = new InterfaceInfoService(this.nodeid, this.type, this.subtype);
		return interfaceInfoService.getCurrAllInterfaceInfo(subentities,sindexs,starttime,totime);
	}
	
	/**
	 * �õ���֪�����Ľӿ���Ϣ
	 * @param sindexs       --- ������
	 * @param starttime     --- ��ʼ����
	 * @param totime        --- ��������
	 * @return
	 */
	public List<InterfaceInfo> getInterfaceInfoBySindes(String[] sindexs,String starttime, String totime){
		InterfaceInfoService interfaceInfoService = new InterfaceInfoService(this.nodeid, this.type, this.subtype);
		return interfaceInfoService.getInterfaceInfoBySindes(sindexs,starttime,totime);
	}
	
	/**
	 * ��ȡSyslog��Ϣ
	 * @param startdate --- ��ʼ����
	 * @param todate    --- ��������
	 * @param level1	--- ��־�ȼ�
	 * @return ����Syslog�б� Syslog
	 */
	public List<Syslog> getSyslogInfo(String startdate, String todate, String priorityname){
		return new SyslogInfoService(this.nodeid, this.type, this.subtype).getSyslogInfo(this.nodeDTO.getIpaddress(), startdate, todate, priorityname);
	}
	
	/**
	 * ��ȡ�澯��Ϣ
	 * @param startdate --- ��ʼ����
	 * @param todate    --- ��������
	 * @param level1	--- �澯�ȼ�
	 * @param status	--- ����״̬
	 * @return ���ظ澯�б� EventList
	 */
	public List<Object> getAlarmInfo(String startdate, String todate, String level1, String status){
		return new EventListInfoService(this.nodeid, this.type, this.subtype).getCurrSummaryEventListInfo(startdate, todate, level1, status);
	}
	
	/**
	 * ��ȡ�澯��ϸ��Ϣ
	 * @param startdate --- ��ʼ����
	 * @param todate    --- ��������
	 * @param level1	--- �澯�ȼ�
	 * @param status	--- ����״̬
	 * @return ���ظ澯�б� EventList
	 */
	public List<EventList> getAlarmDetailInfo(String startdate, String todate, String level1, String eventlocation, String subentity, String status){
		return new EventListInfoService(this.nodeid, this.type, this.subtype).getEventListInfo(startdate, todate, level1, eventlocation, subentity, status);
	}
	
	

}
