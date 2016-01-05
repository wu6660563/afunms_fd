package com.afunms.application.manage;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.afunms.alarm.dao.AlarmIndicatorsDao;
import com.afunms.application.dao.DBDao;
import com.afunms.application.dao.NodeIndicatorAlarmDao;
import com.afunms.application.dao.PerformancePanelDao;
import com.afunms.application.dao.PerformancePanelIndicatorsDao;
import com.afunms.application.model.DBVo;
import com.afunms.application.model.NodeIndicatorAlarm;
import com.afunms.application.model.PerformancePanelIndicatorsModel;
import com.afunms.application.model.PerformancePanelModel;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.SessionConstant;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.system.model.User;
import com.afunms.topology.dao.HostNodeDao;

/**
 * @author HONGLI E-mail: ty564457881@163.com
 * @version ����ʱ�䣺Aug 15, 2011 7:22:03 PM
 * ��˵��:������������
 */
public class PerformancePanelManager extends BaseManager implements ManagerInterface {
	private static PerformancePanelManager performancePanelManager;
	
	public static synchronized PerformancePanelManager getInstance(){
		if(performancePanelManager == null){
			performancePanelManager = new PerformancePanelManager();
		}
		return performancePanelManager;
	}
	
	/**
	 * ��ʼ����������еĸ澯����
	 */
	public void init(){
		NodeIndicatorAlarmDao nodeIndicatorAlarmDao = new NodeIndicatorAlarmDao();
		try {
			nodeIndicatorAlarmDao.clearData();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			nodeIndicatorAlarmDao.close();
		}
	}
	
	/**
	 * ������
	 */
	private static String rootPath;

	
	public String execute(String action) {
		if("addperformancePanel".equals(action)){
			return addperformancePanel();
		}
		if("secondStep".equals(action)){
			return secondStep();
		}
		if("thirdStep".equals(action)){
			return thirdStep();
		}
		if("createNewPanel".equals(action)){
			return createNewPanel();
		}
		if("delete".equals(action)){
			return delete();
		}
		if("toEditPanelDevice".equals(action)){
			return toEditPanelDevice();
		}
		if("toEditPanelIndicators".equals(action)){
			return toEditPanelIndicators();
		}
		if("panelList".equals(action)){
			return panelList();
		}
		setErrorCode(ErrorMessage.ACTION_NO_FOUND);
		return null;
	}
	
	
	/**
	 * ��������б�
	 * @return
	 */
	private String panelList(){
		String freshTimeMinute = getParaValue("freshTimeMinute");
		if(freshTimeMinute == null){
			freshTimeMinute = "300";//ҳ��Ĭ��Ϊ60��ˢ��һ��
		}
		//��ع����б�
		//���ܼ������table���
		rootPath = request.getContextPath();
		List<String> tableList = getPerformanceList();
		//���ؼ�ع���Ľ���
		request.setAttribute("tableList", tableList);
		request.setAttribute("freshTimeMinute", freshTimeMinute);
		return "/performance/xnmb.jsp";
	}
	
	/**
	 * �༭����ָ��
	 * @return
	 */
	private String toEditPanelIndicators(){
		String deviceType = "";
		String panelName = "";
		try {
			deviceType = new String(request.getParameter("deviceType").getBytes("iso8859-1"),"gbk");
			panelName = new String(request.getParameter("panelName").getBytes("iso8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		//���ݽڵ��idȡ������ָ��ļ���
		List alarmIndicatorsList = null;
		AlarmIndicatorsDao alarmIndicatorsDao = new AlarmIndicatorsDao();
		try {
			alarmIndicatorsList = alarmIndicatorsDao.getByType(deviceType);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			alarmIndicatorsDao.close();
		}
		//ָ�꼯�ϴ���request��
		PerformancePanelIndicatorsDao panelIndicatorsDao = null;
		List perList = null;
		try{
			panelIndicatorsDao = new PerformancePanelIndicatorsDao();
			perList = panelIndicatorsDao.findByCondition(" where panelName = '"+panelName.trim()+"'");
		} catch(Exception e) {
			e.printStackTrace();
		} finally{
			if(panelIndicatorsDao != null){
				panelIndicatorsDao.close();
			}
		}
		//����������ƣ��õ�������е�����ָ��
		StringBuffer indicatorsIdsBuffer = new StringBuffer();
		if(perList != null){
			for(int i=0; i<perList.size(); i++){
				PerformancePanelIndicatorsModel panelModel = (PerformancePanelIndicatorsModel)perList.get(i);
				indicatorsIdsBuffer.append(",");
				indicatorsIdsBuffer.append(panelModel.getIndicatorName());
				if(i == perList.size()-1){
					indicatorsIdsBuffer.append(",");
				}
			}
		}
		request.setAttribute("indicatorsIds", indicatorsIdsBuffer.toString());
		request.setAttribute("alarmIndicatorsList", alarmIndicatorsList);
		request.setAttribute("panelName", panelName);
		request.setAttribute("deviceType", deviceType);
		return "/performance/performancePanelChoseAlarmIndicators.jsp";
	}
	
	/**
	 * ���༭�����豸��ҳ��
	 * @return
	 */
	private String toEditPanelDevice(){
		String deviceType = "";
		String panelName = "";
		try {
			deviceType = new String(request.getParameter("deviceType").getBytes("iso8859-1"),"gbk");
			panelName = new String(request.getParameter("panelName").getBytes("iso8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		//���ݵ�ǰ�û���Ȩ�޵õ��豸��Դ��
		//Ĭ�ϵ��豸��Դ����Ϊ�����豸  deviceType = 'net'
		List deviceList = null;
		int typeInt = 1;
		if(deviceType == null || deviceType.equals("null")){
			deviceType = "net";
		}
		if(deviceType.equals("net")){
			typeInt = 1;
		}else if(deviceType.equals("host")){
			typeInt = 4;
		}else if(deviceType.equals("db")){
			typeInt = 5;//���ݿ�
		}
		String[] bids = getBids();
		deviceList = getDeviceList(1,typeInt,bids);
		//����������ƣ��õ�������е��豸Ԫ��
		PerformancePanelDao performancePanelDao = null;
		List perList = null;
		try{
			performancePanelDao = new PerformancePanelDao();
			perList = performancePanelDao.findByCondition(" where name = '"+panelName.trim()+"'");
		} catch(Exception e) {
			e.printStackTrace();
		} finally{
			if(performancePanelDao != null){
				performancePanelDao.close();
			}
		}
		//�ڵ�id�ַ���
		StringBuffer deviceIds = new StringBuffer();
		if(perList != null){
			for(int i=0; i<perList.size(); i++){
				PerformancePanelModel panelModel = (PerformancePanelModel)perList.get(i);
				deviceIds.append(",");
				deviceIds.append(panelModel.getDeviceId());
				if(i == perList.size()-1){
					deviceIds.append(",");
				}
			}
		}
		request.setAttribute("deviceIds", deviceIds.toString());
		request.setAttribute("panelName", panelName);
		request.setAttribute("deviceType", deviceType);
		request.setAttribute("deviceList", deviceList);
		return "/performance/performancePanelChoseDevice.jsp";
	}
	
	private String delete(){
		String panelName = "";
		try {
			panelName = new String(request.getParameter("panelName").getBytes("iso8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		//ɾ����ǰѡ�е����
		PerformancePanelDao performancePanelDao = new PerformancePanelDao();
		try {
			performancePanelDao.deleteByName(panelName);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			performancePanelDao.close();
		}
		//ɾ����ǰ����ָ���е�����
		PerformancePanelIndicatorsDao performancePanelIndicatorsDao = new PerformancePanelIndicatorsDao();
		try {
			performancePanelIndicatorsDao.deleteByPanelName(panelName);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			performancePanelIndicatorsDao.close();
		}
		
		//��ع����б�
		//���ܼ������table���
		List<String> tableList = getPerformanceList();
		//���ؼ�ع���Ľ���
		request.setAttribute("tableList", tableList);
		return "/performance/xnmb.jsp";
	}
	
	/**
	 * ���Ĳ�����������ָ�����
	 * @return
	 */
	private String createNewPanel(){
		//�����������
		String panelName = getParaValue("panelName");
		//�õ��豸����
		String deviceType = getParaValue("deviceType");
		//�õ������豸id
		String deviceIds = getParaValue("deviceIds");
		//�õ����и澯ָ������
		String indicatorNames = getParaValue("indicatorNames");
		if(indicatorNames != null && !"null".equals(indicatorNames)){
			indicatorNames = indicatorNames.replaceAll("root,", "");
		}
		//�����Ϣ���
		List<PerformancePanelModel> performancePanelList = getPerformancePanelModelList(panelName, deviceType, deviceIds);
		PerformancePanelDao performancePanelDao = new PerformancePanelDao();
		try {
			performancePanelDao.savePreformance(performancePanelList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			performancePanelDao.close();
		}
		//����Ӧ��ָ���м����Ϣ��� 
		PerformancePanelIndicatorsDao performancePanelIndicatorsDao = new PerformancePanelIndicatorsDao();
		try {
			performancePanelIndicatorsDao.savePreformancePanelIndicators(panelName,indicatorNames);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			performancePanelIndicatorsDao.close();
		}
		
		//��ع����б�
		//���ܼ������table���
		rootPath = request.getContextPath();
		List<String> tableList = getPerformanceList();
		//���ؼ�ع���Ľ���
		request.setAttribute("tableList", tableList);
		return "/performance/xnmb.jsp";
	}
	
	/**
	 * ��ȡ��������б�
	 * @return
	 */
	public List<String> getPerformanceList(){
		List<PerformancePanelModel> allPaneList = null;
		PerformancePanelDao performancePanelDao = new PerformancePanelDao();
		try {
			allPaneList = performancePanelDao.loadAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			performancePanelDao.close();
		}
		//����Ӧ��ָ���м����Ϣ��� 
		List<PerformancePanelIndicatorsModel> allPaneIndicatorsList = null;
		PerformancePanelIndicatorsDao performancePanelIndicatorsDao = new PerformancePanelIndicatorsDao();
		try {
			allPaneIndicatorsList = performancePanelIndicatorsDao.loadAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			performancePanelIndicatorsDao.close();
		}
		//ȡ���澯��Ϣ�б�
		Hashtable<String, List<NodeIndicatorAlarm>> nodeIndicatorAlarmHash = null;
		NodeIndicatorAlarmDao nodeIndicatorAlarmDao = new NodeIndicatorAlarmDao();
		try {
			nodeIndicatorAlarmHash = nodeIndicatorAlarmDao.getNodeIndicaorAlarmHash();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			performancePanelIndicatorsDao.close();
		}
		
		//��ع����б�
		//���ܼ������table���
		List<String> tableList = new ArrayList<String>();
		//��Ϲ�������е��������-���������Ϊ��λ  
		Hashtable<String, List<PerformancePanelModel>> allPanelHash = getPanelHash(allPaneList);
		//��Ϲ������������Ӧ��ָ�꼯��-����������Ϊ��λ
		Hashtable<String, List<PerformancePanelIndicatorsModel>> allPanelIndicatorsHash = getPanelIndicatorsHash(allPaneIndicatorsList);
		Iterator<String> panelNamesIterator = allPanelHash.keySet().iterator();
		while(panelNamesIterator.hasNext()){
			String pName = panelNamesIterator.next();//�������
			List<PerformancePanelModel> performancePanelModelList = allPanelHash.get(pName);//����豸id����
			List<PerformancePanelIndicatorsModel> performancePanelIndicatorsModelList = allPanelIndicatorsHash.get(pName);//���ָ��id����
			//����������ݼ��Ϲ����ҳ��չ�ֵĶ�ά���
			//�������ݶ�̬�����table���
			StringBuffer tableBuffer = new StringBuffer();
			tableBuffer.append("<br><!--��ط�������--!><table><tr><td><a href='#' onclick=\"display(\'table_");//������ص�table����
			tableBuffer.append(pName);
			tableBuffer.append("\',\'table_img_");
			tableBuffer.append(pName);
			tableBuffer.append("\')\"><img id='table_img_");
			tableBuffer.append(pName);
			tableBuffer.append("' src='");
			tableBuffer.append(rootPath);
			tableBuffer.append("/resource/image/tree/minus.gif'></a>  �������ƣ�");
			tableBuffer.append(pName);
			tableBuffer.append("&nbsp;&nbsp;&nbsp;&nbsp;�豸���ͣ�");
			String deviceType = "net";
			if(performancePanelModelList != null && performancePanelModelList.size() != 0){
				deviceType = performancePanelModelList.get(0).getDeviceType();
				tableBuffer.append(getTypeDescByTypeStr(deviceType));
			}
			tableBuffer.append("</td><td align='right'>");
			tableBuffer.append("<a href='#' onclick=\"editPanelDevice(\'");
			tableBuffer.append(deviceType);
			tableBuffer.append("\',\'");
			tableBuffer.append(pName);
			tableBuffer.append("\')\"");
			tableBuffer.append("'>���ü���豸</a>&nbsp;&nbsp;|&nbsp;&nbsp;");
			tableBuffer.append("<a href='#' onclick=\"editPanelIndicators(\'");
			tableBuffer.append(deviceType);
			tableBuffer.append("\',\'");
			tableBuffer.append(pName);
			tableBuffer.append("\')\">���ü��ָ��</a>&nbsp;&nbsp;|&nbsp;&nbsp;");
			tableBuffer.append("<a onclick=\"confrimDeletePanel('");
			tableBuffer.append(pName);
			tableBuffer.append("')\" href='#'>ɾ����ط���</a></td></tr></table>");
			tableBuffer.append("<table class=\"data\" cellpadding=\"0\" cellspacing=\"0\" id='table_");
			tableBuffer.append(pName);
			tableBuffer.append("'><tr style='background-color: #ECECEC' >");
			tableBuffer.append("<td align=\"center\" class=\"body-data-title\">�豸</td>");
			if(performancePanelIndicatorsModelList != null){
				for(int i=0; i<performancePanelIndicatorsModelList.size(); i++){//���ӱ�ͷ
					PerformancePanelIndicatorsModel pIndicators = performancePanelIndicatorsModelList.get(i);
					tableBuffer.append("<td align=\"center\" class=\"body-data-title\">");
					tableBuffer.append(pIndicators.getIndicatorDesc());//����ָ������� �õ�ָ�������
					tableBuffer.append("</td>");
				}
			}
			tableBuffer.append("</tr>");
			for(int i=0; i<performancePanelModelList.size(); i++){//���ӱ���
				PerformancePanelModel pModel = performancePanelModelList.get(i);
				//�����豸id���豸������  �õ��豸�ĳ�����
				String url = getDeviceUrl(pModel.getDeviceId(), pModel.getDeviceType());
				tableBuffer.append("<tr onmouseout=\"this.style.background='#FFFFFF'\"  onmouseover=\"this.style.background='#AACCFF'\" id='tr_");
				tableBuffer.append(pModel.getName()+"_"+pModel.getDeviceId());
				tableBuffer.append("'>");
				tableBuffer.append("<td class=\"data_content center\"><a href='#' onclick=\"toDetailPage('");
				tableBuffer.append(url);
				tableBuffer.append("')\">");
				//�����豸id���豸������  �õ��豸����
				tableBuffer.append(getDeviceName(pModel.getDeviceId(),pModel.getDeviceType()));
				tableBuffer.append("</a></td>");
				//�澯��Ϣ��״̬��Ϣ
				if(performancePanelIndicatorsModelList != null){
					for(int j=0; j<performancePanelIndicatorsModelList.size(); j++){//���ѭ��:��������ָ���б�
						PerformancePanelIndicatorsModel panelIndicatorsModel = performancePanelIndicatorsModelList.get(j);
						String panelIndicatorsName = panelIndicatorsModel.getIndicatorName().trim();//����е�ĳ���澯ָ������
						//�õ��ýڵ������ָ��ĸ澯��Ϣ�ļ���
						List<NodeIndicatorAlarm> nodeIndicatorAlarmList = nodeIndicatorAlarmHash.get(pModel.getDeviceId()+":"+pModel.getDeviceType());
						if(nodeIndicatorAlarmList == null){//��һ������: ���豸�޸澯��Ϣ 
							tableBuffer.append("<td align='center' class=\"data_content center\"><img src='");
							tableBuffer.append(getCurrentStatusImage(0));
							tableBuffer.append("' alt='����'/>&nbsp;");
							tableBuffer.append("</td>");
							continue;
						}
						//���豸���ڸ澯��Ϣʱ���Ƚϸ澯��Ϣ��ָ�����Ƹ���ǰ���ָ������
						boolean hashAlarm = false;
						for(int k=0; k < nodeIndicatorAlarmList.size(); k++){//�ڲ�ѭ�� �澯�б�
							NodeIndicatorAlarm nodeIndicatorAlarm = nodeIndicatorAlarmList.get(k);
							String nodeIndicatorAlarmName = nodeIndicatorAlarm.getIndicatorName().trim();//�澯��Ϣ�б��еĸ澯ָ������
							if(panelIndicatorsName.equalsIgnoreCase(nodeIndicatorAlarmName)){//�澯ָ������ͬʱ   ���ڶ������Σ����豸�и澯��Ϣ�����Ҹ澯ָ����������е�ָ���ͷͬ��ʱ��
								tableBuffer.append("<td align='center' class=\"data_content center\"><img src='");
								tableBuffer.append(getCurrentStatusImage(Integer.parseInt(nodeIndicatorAlarm.getAlarmLevel())));
								tableBuffer.append("' alt='");
								tableBuffer.append(nodeIndicatorAlarm.getAlarmDesc());
								tableBuffer.append("'/>");
								tableBuffer.append("</td>");
								hashAlarm = true;
								continue;//�˳�ѭ����
							}
						}
						//�����������Σ����豸�и澯��Ϣ���澯ָ����������е�ָ���ͷ ��ͬ��ʱ��
						if(!hashAlarm){
							tableBuffer.append("<td align='center' class=\"data_content center\"><img src='");
							tableBuffer.append(getCurrentStatusImage(0));
							tableBuffer.append("' alt='����");
							tableBuffer.append("'/>&nbsp;");
							tableBuffer.append("</td>");
						}
					}
				}
				tableBuffer.append("</tr>");
			}
			tableBuffer.append("</table>");
			tableList.add(tableBuffer.toString());
		}
		return tableList;
	}
	
	/**
	 * <p>�˴������������⣬����ȷ����������</p>
	 * �����豸���ͺ��豸��id�õ��ĳ�����->��ת��������ϸҳ  
	 * @param deviceId
	 * @param deviceType
	 * @return
	 */
	public static String getDeviceUrl(String deviceId, String deviceType){
		String url = null;
		if(deviceType == null){
			url = "";
		}else if(deviceType.equals("net") || deviceType.equals("host")){
			url = "/detail/dispatcher.jsp?id=net"+deviceId+"&flag=1&fromtopo=true";
		}else if(deviceType.equals("db")){
			url = "/detail/dispatcher.jsp?id=dbs"+deviceId+"&flag=1&fromtopo=true";
		}  
		return rootPath+url;
	}
	
	/**
	 * �����豸���͵�Ӣ�����õ���������
	 * @param deviceType
	 * @return
	 */
	public static String getTypeDescByTypeStr(String deviceType){
		String retString = null;
		if(deviceType == null){
			retString = "";
		}else if(deviceType.equals("net")){
			retString = "�����豸";
		}else if(deviceType.equals("host")){
			retString = "������";
		}else if(deviceType.equals("db")){
			retString = "���ݿ�";
		}
		return retString;
	}
	
	/**
	 * �ڵ�״̬��־
	 */
	public static String getCurrentStatusImage(int status) {
		String image = null;
		if (status == 0)
			image = "a_level_0.gif";
		else if (status == 1)
			image = "a_level_1.gif";
		else if (status == 2)
			image = "a_level_2.gif";
		else if (status == 3)
			 image = "a_level_3.gif";
		else
			image = "unmanaged.gif";
		return rootPath+"/resource/image/topo/" + image;
	}
	
	/**
	 * �����豸��id �Լ��豸���� �õ��豸������
	 * @param id
	 * @param deviceType
	 * @return
	 */
	private String getDeviceName(String id, String deviceType){
		String name = null;
		if(deviceType.equals("net") || deviceType.equals("host")){
			Node node = (Node)PollingEngine.getInstance().getNodeByID(Integer.parseInt(id));
			if(node == null){
				name = id;
				return name;
			}
			name = node.getAlias()+"("+node.getIpAddress()+")";
		}else if(deviceType.equals("db")){
			DBDao dbDao = new DBDao();
			DBVo dbvo = null;
			try {
				dbvo = (DBVo)dbDao.findByID(id);
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				dbDao.close();
			}
			if(dbvo != null){
				name = dbvo.getAlias()+"("+dbvo.getIpAddress()+")";
			}
		}
		return name;
	}
	
	/**
	 * ��Ϲ�������е��������ָ�꼯�� key���������  value������ָ�꼯��
	 * @param allPaneList  ���е��������
	 * @return
	 */
	private Hashtable<String, List<PerformancePanelIndicatorsModel>> getPanelIndicatorsHash(
			List<PerformancePanelIndicatorsModel> allPaneIndicatorsList) {
		Hashtable<String, List<PerformancePanelIndicatorsModel>> allPanelHash = new Hashtable<String, List<PerformancePanelIndicatorsModel>>();
		//Set<id>
		Set<String> panelNameSet = new HashSet<String>();
		for(int i=0; i<allPaneIndicatorsList.size(); i++){
			panelNameSet.add(allPaneIndicatorsList.get(i).getPanelName());
		}
		Iterator<String> panelNameIterator = panelNameSet.iterator();
		while(panelNameIterator.hasNext()){
			String panelName = panelNameIterator.next();
			if(panelName == null){
				continue;
			}
			List<PerformancePanelIndicatorsModel> panelList = new ArrayList<PerformancePanelIndicatorsModel>();
			for(int i=0; i<allPaneIndicatorsList.size(); i++){
				if(panelName.equals(allPaneIndicatorsList.get(i).getPanelName())){
					panelList.add(allPaneIndicatorsList.get(i));
				}
			}
			allPanelHash.put(panelName, panelList);
		}
		return allPanelHash;
	}

	/**
	 * ��Ϲ�������е�������� key���������  value�����ļ�¼
	 * @param allPaneList  ���е��������
	 * @return
	 */
	private Hashtable<String, List<PerformancePanelModel>> getPanelHash(List<PerformancePanelModel> allPaneList){
		Hashtable<String, List<PerformancePanelModel>> allPanelHash = new Hashtable<String, List<PerformancePanelModel>>();
		//Set<id>
		Set<String> panelNameSet = new HashSet<String>();
		for(int i=0; i<allPaneList.size(); i++){
			panelNameSet.add(allPaneList.get(i).getName());
		}
		Iterator<String> panelNameIterator = panelNameSet.iterator();
		while(panelNameIterator.hasNext()){
			String panelName = panelNameIterator.next();
			if(panelName == null){
				continue;
			}
			List<PerformancePanelModel> panelList = new ArrayList<PerformancePanelModel>();
			for(int i=0; i<allPaneList.size(); i++){
				if(panelName.equals(allPaneList.get(i).getName())){
					panelList.add(allPaneList.get(i));
				}
			}
			allPanelHash.put(panelName, panelList);
		}
		return allPanelHash;
	}
	
	/**
	 * @param panelName      �������
	 * @param deviceType     ����е��豸���
	 * @param deviceIds      ����е������豸��id
	 * @return
	 */
	private List<PerformancePanelModel> getPerformancePanelModelList(String panelName, String deviceType, String deviceIds){
		if(panelName == null){
			return null;
		}
		List<PerformancePanelModel> performancePanelList = new ArrayList<PerformancePanelModel>();
		String[] deviceIdsArry = deviceIds.split(",");
		for(String deviceId:deviceIdsArry){
			PerformancePanelModel panelModel = new PerformancePanelModel();
			panelModel.setDeviceId(deviceId);
			panelModel.setDeviceType(deviceType);
			panelModel.setName(panelName);
			performancePanelList.add(panelModel);
		}
		return performancePanelList;
	}
	
	/**
	 * ��������ѡ��ָ��
	 * @return
	 */
	private String thirdStep(){
		//�õ����еĽڵ�id
		String[] nodeids = getParaArrayValue("selectedDevice");
		String deviceType = getParaValue("deviceType");
		String panelName = getParaValue("panelName");
		if(deviceType == null || deviceType.equals("deviceType")){
			deviceType = "net";
		}
		String deviceIds = getParaValue("deviceIds");
		if(deviceIds != null && !deviceIds.equals("null")){
			deviceIds = deviceIds.replaceAll("root,", "");
		}
		//���ݽڵ��idȡ������ָ��ļ���
		List alarmIndicatorsList = null;
		AlarmIndicatorsDao alarmIndicatorsDao = new AlarmIndicatorsDao();
		try {
			alarmIndicatorsList = alarmIndicatorsDao.getByType(deviceType);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			alarmIndicatorsDao.close();
		}
		//ָ�꼯�ϴ���request��
		request.setAttribute("alarmIndicatorsList", alarmIndicatorsList);
		request.setAttribute("nodeids", nodeids);
		request.setAttribute("panelName", panelName);
		request.setAttribute("deviceIds", deviceIds);
		request.setAttribute("deviceType", deviceType);
		return "/performance/addPerformancePanelChoseAlarmIndicators.jsp";
	}
	
	/**
	 * �ڶ�����ѡ���豸
	 * @return
	 */
	private String secondStep(){
		//���ݵ�ǰ�û���Ȩ�޵õ��豸��Դ��
		//Ĭ�ϵ��豸��Դ����Ϊ�����豸  deviceType = 'net'
		List deviceList = null;
		String deviceType= getParaValue("deviceType");
		String panelName = getParaValue("panelName");
		int typeInt = 1;
		if(deviceType == null || deviceType.equals("null")){
			deviceType = "net";
		}
		if(deviceType.equals("net")){
			typeInt = 1;
		}else if(deviceType.equals("host")){
			typeInt = 4;
		}else if(deviceType.equals("db")){
			typeInt = 5;//���ݿ�
		}
		String[] bids = getBids();
		deviceList = getDeviceList(1,typeInt,bids);
		request.setAttribute("panelName", panelName);
		request.setAttribute("deviceType", deviceType);
		request.setAttribute("deviceList", deviceList);
		return "/performance/addPerformancePanelChoseDevice.jsp";
	}
	
	/**
	 * ��ȡ list
	 * @param <code>DBVo</code>
	 * @return
	 */
	public List getDBList(String[] bids){
		List list = new ArrayList();
		StringBuffer sql2 = new StringBuffer();
		StringBuffer s2 = new StringBuffer();
		int flag = 0;
		if (bids != null && bids.length > 0) {
			for (int i = 0; i < bids.length; i++) {
				if (bids[i].trim().length() > 0) {
					if (flag == 0) {
						//s.append(" and ( bid like '%," + bids[i].trim() + ",%' ");
						s2.append(" and ( bid like '%" + bids[i].trim() + "%' ");
						flag = 1;
					} else {
						//flag = 1;
						//s.append(" or bid like '%," + bids[i].trim() + ",%' ");
						s2.append(" or bid like '%" + bids[i].trim() + "%' ");
					}
				}
			}
			s2.append(") ");
		}
		sql2.append("select * from app_db_node where 1=1 " + s2.toString());
		
		DBDao dao = new DBDao();
		try {
			list = dao.findByCriteria(sql2.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		return list;
	}
	
	/**
	 * @param managed   �������״̬
	 * @param category  ���
	 * @param bids      ҵ��id
	 * @return
	 */
	private List getDeviceList(int managed,int category,String[] bids){
		List deviceList = null;
		if(category == 1 || category == 4){//�����豸���߷�����
			HostNodeDao hostNodeDao = new HostNodeDao();
			try {
				deviceList = hostNodeDao.loadMonitorByMonCategory(managed,category,bids);
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				hostNodeDao.close();
			}
		}else{//���ݿ�
			deviceList = getDBList(bids);
		}
		return deviceList;
	}
	
	/**
	 * ��һ��������ҳ��
	 * @return
	 */
	private String addperformancePanel(){
		return "/performance/addPerformancePanel.jsp";
	}
	
	/**
	 * ���ҵ��Ȩ������
	 * @return
	 */
	public String[] getBids(){
		String[] bids = null;
		User current_user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
		if (current_user.getBusinessids() != null){
			if(current_user.getBusinessids() !="-1"){
				bids = current_user.getBusinessids().split(",");
			}
		}
		return bids; 
	}
}
