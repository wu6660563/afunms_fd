package com.afunms.alarm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.python.modules.synchronize;

import com.afunms.alarm.dao.AlarmLinkNodeDao;
import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.model.AlarmLinkNode;
import com.afunms.alarm.util.AlarmResourceCenter;
import com.afunms.common.base.BaseVo;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.model.NodeDTO;

/**
 * �澯���������������ݷ����
 * 
 * 	���˼·��
 * 
 * 	���ݴ���ĸ澯��Ϣ�����Ҹø澯��Ϣ���ڵ���·���õ���Ӧ����·��Ϣ��
 * 
 * 	ԭʼ�澯��������Ļ� 
 * 	
 * 	���и澯�жϣ���������澯��Ϣ����ô���ø澯��Ϣ ���뵽��·�澯��Ϣ�洢���С�
 * 
 * 	��ѯ��·�澯��Ϣ�����鿴��ǰ��·���Ƿ�ʼ�澯�����������·�ĸ澯��Ϣ�������ˣ���ô���Ҹ澯����
 * 
 * 	���Ƿ�߱��澯��������������澯������յ�ǰ�澯��Ϣ�洢�����������κβ�����
 * 
 * 	SendAlarmUtil
 * 
 * 	������ �� ���������ڵ�����澯ʹ������Ҳ�澯��
 * 
 * 	�¸澯�ڵ����Σ���Ϊ��ȫ����ԭ���澯ָ�꣨��
 * 				 ���ڵ�����
 * 	
 * 	�豸���������
 * 	ָ�������ȼ�
 *  
 * 
 * 	add ��ֵ�ȽϷ�ʽ�� �澯���� 
 * 	
 * @author yag
 *
 */
public class AlarmLinkNodeService {
	
	private static Map<Integer,AlarmLinkStorer> linkStorer = 
					new HashMap<Integer,AlarmLinkStorer>();

	private static SysLogger logger = SysLogger.getLogger(AlarmLinkNodeService.class);
	
	public static void main(String[] args) {
		AlarmLinkNodeService service = new AlarmLinkNodeService();
		AlarmIndicatorsNode nm = new AlarmIndicatorsNode();
		AlarmIndicatorsNode nm1 = new AlarmIndicatorsNode();
		AlarmIndicatorsNode nm2 = new AlarmIndicatorsNode();
		AlarmIndicatorsNode nm3 = new AlarmIndicatorsNode();
		AlarmIndicatorsNode nm4 = new AlarmIndicatorsNode();
		AlarmIndicatorsNode nm5 = new AlarmIndicatorsNode();
		AlarmIndicatorsNode nm6 = new AlarmIndicatorsNode();
		AlarmIndicatorsNode nm7 = new AlarmIndicatorsNode();
		AlarmIndicatorsNode nm8 = new AlarmIndicatorsNode();
		AlarmIndicatorsNode nm9 = new AlarmIndicatorsNode();
		
		nm.setId(59);
		nm1.setId(51);
		nm2.setId(59);
		nm3.setId(59);
		nm4.setId(51);
		nm5.setId(51);
		nm6.setId(67);
		nm7.setId(67);
		nm8.setId(59);
		nm9.setId(67);
		//service.deal(nm, "97");
//		List list = service.getAlarmLinkByAlarmID(59);
//		for(int i=0; i<list.size(); i++){
//			System.out.println(list.get(i));
//		}
//		service.deal(nm, "97");
//		service.deal(nm1, "97");
//		service.deal(nm2, "97");
//		service.deal(nm3, "97");
//		service.deal(nm4, "97");
//		service.deal(nm5, "97");
//		service.deal(nm6, "97");
//		service.deal(nm7, "97");
//		service.deal(nm8, "97");
//		service.deal(nm9, "97");
	}
	
	private void testStore() {
		AlarmLinkStorer store = new AlarmLinkStorer(2);
		store.setLinkid(4);
		System.out.println(store.isAvaliable());
		store.setLinkid(5);
		System.out.println(store.isAvaliable());
		store.setLinkid(5);
		store.setLinkid(5);
		System.out.println(store.isAvaliable());
		store.setLinkid(4);
		System.out.println(store.isAvaliable());
	}
	
	/**
	 * �澯������������ 
	 * 	���ݴ����ԭ���澯ָ�꣬��ȡ��������ĸ澯��·�����еĸ澯�ڵ㣬�ֱ��ÿ���澯�ڵ���д���
	 * 
	 * @param nm
	 * 			�����澯��ԭ���澯ָ��
	 * @param value
	 * 			�澯ֵ
	 */
	public void deal(NodeDTO node, AlarmIndicatorsNode nm, String value) {
		logger.info("��ʼ���и澯�鴦��...");
		List<AlarmLinkNode> nodeList = getAlarmLinkByAlarmID(nm.getId());	// ��ȡԭ���澯�������������и澯��·�ڵ�
		//System.out.println("queryByAlarmID:" + nodeList.size());
		// ����ÿһ���澯�ڵ�
		for (int i=0; i<nodeList.size(); i++){
			//System.out.println(nodeList.get(i));
			dealAlarmNode(node, nm, nodeList.get(i),value);
		}
		logger.info("�澯�鴦�����...");
	}
	
	private List<AlarmLinkNode> getAlarmLinkByAlarmID(int id) {
		List<AlarmLinkNode> nodeList = new ArrayList<AlarmLinkNode>();
		AlarmLinkNodeDao dao = new AlarmLinkNodeDao();
		try{
			nodeList  = dao.queryByAlarmID(id);	// ��ȡԭ���澯�������������и澯��·�ڵ�
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		
		return nodeList;
	}
	
	public void dealAlarmIndicatorsNode(AlarmIndicatorsNode nm, String value) {
		// ���ø澯�Ƿ������еĽڵ�����
		if(checkAlarmIsCovered(nm)){
			
		}
	}
	
	public boolean compareWithAllValue(AlarmIndicatorsNode nm, String value) {
		return false;
	}
	
	/**
	 * ������·����и澯����
	 * 
	 * @param linkList
	 */
	public void dealAlarmNode(NodeDTO node, AlarmIndicatorsNode nm,AlarmLinkNode ln, String value) {
		if(!checkAlarmIsAvailable(ln)) {
			return ;
		}
		logger.info("��ʼ����ڵ㣺"+ln);
		// �����ǰ�ڵ��Ӧ����·��ɸ澯��δ��ʹ�ã����������·��Ϣ����Ҫ��Ӧ����ϵͳ���й����У��ͻ��޸���·��Ϣ������
		AlarmLinkStorer store = linkStorer.get(ln.getLinkid());
		if(store == null || !store.isUsed()){
			logger.info("use ...............");
			linkStorer.put(ln.getLinkid(), initAlarmLinkStorer(ln.getLinkid()));
		}
		// �жϸ澯�ȼ�
		int alarmLevel = getAlarmLevel(ln, Double.parseDouble(value));
		if(checkEventTimes(ln,alarmLevel) > 0) {
			// ��ø澯������ﵽ���Ը澯����������ȥ�鿴��·�е����и澯ָ���Ƿ񶼾߱���������·�澯������
			// �ѵ�ǰ�澯�Ĵ���������鿴������·�߲��߱��澯����
			store = linkStorer.get(ln.getLinkid());
			if(store.isAvaliable()) {
				/**
				 *  �����澯�������澯
				 */
				// �鿴�澯������
				// �鿴�豸������
				// ���������·�Ľڵ㶼�����澯�ˣ������ѹ�Ƴ���
				// ����澯��Ϣ
				// 
				// ��ʼ�澯ѹ������
				logger.info("��ʼ�澯ѹ����������");
				CheckEventUtil util = new CheckEventUtil();
				util.sendAlarm(node, nm, value, alarmLevel,"");
				
				store.clearValue();	// ��ձ��
			}else {
				// �����澯��Ϣ��ǵ��澯���洢��
				store.setStores(ln.getId());
			}
		}
	}
	
	/**
	 * 
	 * @param linkid
	 */
	private AlarmLinkStorer initAlarmLinkStorer(int linkid){
		logger.info("init alarmLinkStorer");
		return  new AlarmLinkStorer(linkid);
	}
	
	private boolean isAvaliableByLinkGroup(AlarmLinkNode ln, String value){
		/**
		 * 1. ����ӵ���ʶ��
		 */
		
		return false;
	}
	
	/**
	 * ��鵱ǰ�澯�Ƿ񸲸�ԭ���ĸ澯
	 * 
	 * @param node
	 * 			AlarmLinkNode
	 * @return
	 * 			boolean
	 */
	private boolean checkAlarmIsCovered(AlarmLinkNode node) {
		return (node.getIsCover() > 0 ? true : false);
	}
	/**
	 * ���ԭ���澯�Ƿ������еĸ澯����
	 * 
	 * @param nm
	 * 			ԭ���ĸ澯��Ϣ
	 * @return
	 * 			
	 */
	public boolean checkAlarmIsCovered(AlarmIndicatorsNode nm){
		AlarmLinkNodeDao dao = new AlarmLinkNodeDao();
		List<AlarmLinkNode> list = null;
		try{
			list = dao.queryByAlarmID(nm.getId());
			for(AlarmLinkNode n:list) {
				if(n.getIsCover() > 0) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * �ж��¸澯�Ƿ���������澯
	 * 
	 * 	�Ƿ�����澯�������ǣ� �����Ƿ���������澯���Ƿ��ϼ��澯�����ǡ�
	 * 
	 * @param ln
	 * @return
	 */
	public boolean checkAlarmIsAvailable(AlarmLinkNode ln) {
		boolean isEnabled = false;
		boolean isCovered = true;
//		if(ln.getId() != ln.getFid()) {
//			AlarmLinkNodeDao dao = new AlarmLinkNodeDao();
//			try {
//				// ��ѯ��ǰ�ڵ���ϼ��ڵ�
//				AlarmLinkNode priorNode = (AlarmLinkNode) dao.findByID(ln.getFid()+"");
//				isCovered = priorNode.getIsCoverNext() > 0 ? true : false ;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}		 
//		}
		isEnabled = ln.getIsEnabled() > 0 ? true : false ;
		return isEnabled ;//&& isCovered;
	}
	
	
	
	/**
	 * 
	 * @param ln
	 * @param value
	 * @return
	 */
	public int getAlarmLevel(AlarmLinkNode ln, double value) {
		int alarmLevel = 0;
		int eventLevel = 0;
		// �澯��ֵ
		double limenvalues[] = new double[3];
		limenvalues[0] = Double.parseDouble(ln.getLimenvalue0());	
		limenvalues[1] = Double.parseDouble(ln.getLimenvalue1());
		limenvalues[2] = Double.parseDouble(ln.getLimenvalue2());
		
		// ��ȡ��ǰ�澯�ȼ�
		alarmLevel = checkAlarmLevel(value, limenvalues, ln.getCompareType());
		
		logger.info(ln.getDescr()+" �澯�ȼ���" + alarmLevel);
		
		return alarmLevel;
	}
	
	/**
	 * ���ݼ��ֵ���澯��ֵ���Լ�����ʽ ���ظ澯�ȼ�
	 * 
	 * @param value
	 * 				���ֵ
	 * @param values
	 * 				��ֵ����values[2]��ʾ������ֵ
	 * 					  values[1] ��ʾ������ֵ
	 * 					  values[0] ��ʾһ����ֵ
	 * @param compare_type
	 * 				�ȽϷ�ʽ 0 ��ʾ��������������
	 * @return
	 * 			level: �ȼ��ֱ�Ϊ 3,2,1,0
	 */
	public int checkAlarmLevel(double value,double values[],int compare_type) {
		int level = 0;
		if(compare_type == 0) {
			// ����ʽ
			if (value <= values[2]) {
				level = 3;
			} else if(value <= values[1]) {
				level = 2;
			} else if(value <= values[0]) {
				level = 1;
			} else {
				level = 0;
			}
		} else {
			// ����ʽ
			if (value >= values[2]) {
				level = 3;
			} else if (value >= values[1]) {
				level = 2;
			} else if (value >= values[0]) {
				level = 1;
			} else {
				level = 0;
			} 
		}
		return level;
	}
	
	/**
	 * ��ȡ�澯����
	 * 
	 * @param ln
	 * @param alarmLevel
	 * @return
	 */
	public int checkEventTimes(AlarmLinkNode ln, int alarmLevel) {
		int eventTimes = 0;
		// �õ�ǰָ���id�ź�ԭ���澯ָ��id��������Ϊ�澯�����ı�ʶ
		String name = ln.getId() + "" + ln.getAlarmid(); 
		if(alarmLevel == 0) {
			setEventTimes(name, 0);	// ���еȼ��ĸ澯�������
			eventTimes = 0;
		}

		// ��ö���ĸ澯����
		int defineTime = getDefineEventTime(ln, alarmLevel);
		int lastEventTime = getEventTimes(name, alarmLevel);	// �õ���ǰ�ĸ澯����
		eventTimes = lastEventTime + 1;
		setEventTimes(name, lastEventTime+1);
		if((lastEventTime+1) < defineTime) {
			eventTimes = 0;
		}
		return eventTimes;
	}
	
	/**
	 * ����ָ���ȼ���Ӧ�ĸ澯����
	 * 
	 * @param ln
	 * 			�澯ָ����
	 * @param alarmLevel
	 * 			�澯�ȼ�
	 * @return
	 */
	public int getDefineEventTime(AlarmLinkNode ln, int alarmLevel) {
		int defineTime = 0;
		if(alarmLevel == 3) {
			defineTime = ln.getTime2();
		} else if (alarmLevel == 2) {
			defineTime = ln.getTime1();
		} else if (alarmLevel == 1) {
			defineTime = ln.getTime0();
		}
		return defineTime;
	}	
	
	 /**
     * ���ø澯����
     * 
     * @param name
     * @param times
     * @return
     */
    private int setEventTimes(String name, int times) {
        try {
            AlarmResourceCenter.getInstance().setAttribute(name,
                    String.valueOf(times));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }
	
    /**
     * �õ�֮ǰ�ĸ澯����
     * 
     * @param name
     * @param alarmLevel
     * @return
     */
    private int getEventTimes(String name, int alarmLevel) {
        int times = 0;
        try {
            String num = (String) AlarmResourceCenter.getInstance()
                    .getAttribute(name);
            if (num != null && num.length() > 0) {
                times = Integer.parseInt(num);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }
    
	
	/**
	 * ����Ŀǰ�����AlarmIndicatorsNode id��,��ѯ��id�����ڵĸ澯��
	 * 
	 */
	public Map<Integer,List<AlarmLinkNode>> getNodesByIndicatorsId(int id) {
		List<AlarmLinkNode> list = new ArrayList<AlarmLinkNode>();
		Map<Integer, List<AlarmLinkNode>> map = new HashMap<Integer, List<AlarmLinkNode>>();
		
		return map;
	}
	
//	public static void main(String[] args) {
//		AlarmLinkStorer store = new AlarmLinkStorer(2);
//		store.setStores(4);
//		logger.info(store.isAvaliable());
//		store.setStores(5);
//		logger.info(store.isAvaliable());
//		store.setStores(5);
//		store.clearValue();
//		store.setStores(5);
//		store.setStores(4);
//		logger.info(store.isAvaliable());
//		store.setStores(4);
//		logger.info(store.isAvaliable());
//	}
}



/**
 * ֻ�洢�������ǵĽڵ�
 * 
 * @author yag
 *
 */
class AlarmLinkStorer {
	
	/**
	 * �Ƿ�ʹ��
	 */
	private boolean isUsed = false;
	
	private static SysLogger logger = SysLogger.getLogger(AlarmLinkStorer.class);
		
	/**
	 * link��·��
	 */
	private int linkid;
	
	private Map<Integer,Integer> stores = new HashMap<Integer,Integer>();
	
	public AlarmLinkStorer(int linkid){
		this.linkid = linkid;
		init(linkid);
	}
	
	private void init(int linkid) {
		AlarmLinkNodeDao dao = new AlarmLinkNodeDao();
		try{
			List<AlarmLinkNode> list = dao.queryByLinkID(linkid);
			for(int i=0; i<list.size(); i++){
				stores.put(list.get(i).getId(), 0);
				logger.info(list.get(i));
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		this.setUsed(true);	// ʹ����
	}

	/**
	 * 
	 * @param name
	 */
	public void setStores(int id) {
		int value = stores.get(id);
		this.stores.put(id, ++value);
	}
	
	/**
	 * ���ص�ǰ��·�Ƿ���ã������и澯�ڵ��Ƿ񶼲����澯��Ϣ��
	 * 
	 * @return
	 */
	public boolean isAvaliable(){
		Set<Integer> set = stores.keySet();
		for(Integer s: set){
			logger.info(stores.get(s));
			if(stores.get(s) < 1){
				return false;
			}
		}
		return true;	
	}
	
	/**
	 * ����ǰ�澯�ڵ����ÿգ��ظ�Ĭ������Ϊ0 
	 */
	public void clearValue() {
		if(this.getLinkid()>0) {
			this.init(linkid);
			return;
		} else {
			Set<Integer> set = stores.keySet();
			for(Integer s: set){
				stores.put(s, 0);
			}
			this.isUsed = false;
		}
	}
	
	
	public int getLinkid() {
		return linkid;
	}
	public void setLinkid(int linkid) {
		this.linkid = linkid;
	}

	public boolean isUsed() {
		return isUsed;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}
	
}