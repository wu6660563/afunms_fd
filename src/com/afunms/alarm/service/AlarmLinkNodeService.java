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
 * 告警联动分析功能数据服务层
 * 
 * 	设计思路：
 * 
 * 	根据传入的告警信息，查找该告警信息所在的链路，得到对应的链路信息，
 * 
 * 	原始告警如果不监测的话 
 * 	
 * 	进行告警判断，如果产生告警信息，那么将该告警信息 加入到链路告警信息存储器中。
 * 
 * 	查询链路告警信息器，查看当前链路中是否开始告警，如果整个链路的告警信息都触发了，那么查找告警规则，
 * 
 * 	看是否具备告警条件，是则产生告警，并清空当前告警信息存储器，否则不做任何操作。
 * 
 * 	SendAlarmUtil
 * 
 * 	被覆盖 与 整个链条节点产生告警使得其他也告警？
 * 
 * 	新告警节点屏蔽：分为完全屏蔽原来告警指标（）
 * 				 本节点屏蔽
 * 	
 * 	设备间的依赖性
 * 	指标间的优先级
 *  
 * 
 * 	add 阀值比较方式、 告警描述 
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
	 * 告警联动分析处理 
	 * 	根据传入的原来告警指标，获取其关联到的告警链路中所有的告警节点，分别对每个告警节点进行处理
	 * 
	 * @param nm
	 * 			产生告警的原来告警指标
	 * @param value
	 * 			告警值
	 */
	public void deal(NodeDTO node, AlarmIndicatorsNode nm, String value) {
		logger.info("开始进行告警组处理...");
		List<AlarmLinkNode> nodeList = getAlarmLinkByAlarmID(nm.getId());	// 获取原来告警所关联到的所有告警链路节点
		//System.out.println("queryByAlarmID:" + nodeList.size());
		// 处理每一个告警节点
		for (int i=0; i<nodeList.size(); i++){
			//System.out.println(nodeList.get(i));
			dealAlarmNode(node, nm, nodeList.get(i),value);
		}
		logger.info("告警组处理结束...");
	}
	
	private List<AlarmLinkNode> getAlarmLinkByAlarmID(int id) {
		List<AlarmLinkNode> nodeList = new ArrayList<AlarmLinkNode>();
		AlarmLinkNodeDao dao = new AlarmLinkNodeDao();
		try{
			nodeList  = dao.queryByAlarmID(id);	// 获取原来告警所关联到的所有告警链路节点
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		
		return nodeList;
	}
	
	public void dealAlarmIndicatorsNode(AlarmIndicatorsNode nm, String value) {
		// 监测该告警是否被联动中的节点屏蔽
		if(checkAlarmIsCovered(nm)){
			
		}
	}
	
	public boolean compareWithAllValue(AlarmIndicatorsNode nm, String value) {
		return false;
	}
	
	/**
	 * 按照链路组进行告警处理
	 * 
	 * @param linkList
	 */
	public void dealAlarmNode(NodeDTO node, AlarmIndicatorsNode nm,AlarmLinkNode ln, String value) {
		if(!checkAlarmIsAvailable(ln)) {
			return ;
		}
		logger.info("开始处理节点："+ln);
		// 如果当前节点对应的链路完成告警或未被使用，则更新其链路信息，主要是应对在系统运行过程中，客户修改链路信息的情形
		AlarmLinkStorer store = linkStorer.get(ln.getLinkid());
		if(store == null || !store.isUsed()){
			logger.info("use ...............");
			linkStorer.put(ln.getLinkid(), initAlarmLinkStorer(ln.getLinkid()));
		}
		// 判断告警等级
		int alarmLevel = getAlarmLevel(ln, Double.parseDouble(value));
		if(checkEventTimes(ln,alarmLevel) > 0) {
			// 获得告警次数后达到可以告警的条件后，再去查看链路中的所有告警指标是否都具备了整条链路告警的条件
			// 把当前告警的次数升级后查看所在链路具不具备告警条件
			store = linkStorer.get(ln.getLinkid());
			if(store.isAvaliable()) {
				/**
				 *  整个告警链产生告警
				 */
				// 查看告警依赖项
				// 查看设备依赖项
				// 如果整个链路的节点都产生告警了，则进入压制程序
				// 构造告警信息
				// 
				// 开始告警压缩处理
				logger.info("开始告警压缩处理。。。");
				CheckEventUtil util = new CheckEventUtil();
				util.sendAlarm(node, nm, value, alarmLevel,"");
				
				store.clearValue();	// 清空标记
			}else {
				// 新增告警信息标记到告警链存储中
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
		 * 1. 先添加到标识中
		 */
		
		return false;
	}
	
	/**
	 * 检查当前告警是否覆盖原来的告警
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
	 * 检查原来告警是否被联动中的告警覆盖
	 * 
	 * @param nm
	 * 			原来的告警信息
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
	 * 判断新告警是否允许产生告警
	 * 
	 * 	是否产生告警的条件是： 自身是否允许产生告警；是否被上级告警所覆盖。
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
//				// 查询当前节点的上级节点
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
		// 告警阀值
		double limenvalues[] = new double[3];
		limenvalues[0] = Double.parseDouble(ln.getLimenvalue0());	
		limenvalues[1] = Double.parseDouble(ln.getLimenvalue1());
		limenvalues[2] = Double.parseDouble(ln.getLimenvalue2());
		
		// 获取当前告警等级
		alarmLevel = checkAlarmLevel(value, limenvalues, ln.getCompareType());
		
		logger.info(ln.getDescr()+" 告警等级：" + alarmLevel);
		
		return alarmLevel;
	}
	
	/**
	 * 根据监测值，告警阀值，以及排序方式 返回告警等级
	 * 
	 * @param value
	 * 				监测值
	 * @param values
	 * 				阀值数据values[2]表示三级阀值
	 * 					  values[1] 表示二级阀值
	 * 					  values[0] 表示一级阀值
	 * @param compare_type
	 * 				比较方式 0 表示降序，其它表升序
	 * @return
	 * 			level: 等级分别为 3,2,1,0
	 */
	public int checkAlarmLevel(double value,double values[],int compare_type) {
		int level = 0;
		if(compare_type == 0) {
			// 降序方式
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
			// 升序方式
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
	 * 获取告警次数
	 * 
	 * @param ln
	 * @param alarmLevel
	 * @return
	 */
	public int checkEventTimes(AlarmLinkNode ln, int alarmLevel) {
		int eventTimes = 0;
		// 用当前指标的id号和原来告警指标id号联合作为告警次数的标识
		String name = ln.getId() + "" + ln.getAlarmid(); 
		if(alarmLevel == 0) {
			setEventTimes(name, 0);	// 将有等级的告警次数清空
			eventTimes = 0;
		}

		// 获得定义的告警次数
		int defineTime = getDefineEventTime(ln, alarmLevel);
		int lastEventTime = getEventTimes(name, alarmLevel);	// 得到先前的告警次数
		eventTimes = lastEventTime + 1;
		setEventTimes(name, lastEventTime+1);
		if((lastEventTime+1) < defineTime) {
			eventTimes = 0;
		}
		return eventTimes;
	}
	
	/**
	 * 返回指定等级对应的告警次数
	 * 
	 * @param ln
	 * 			告警指标项
	 * @param alarmLevel
	 * 			告警等级
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
     * 设置告警次数
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
     * 得到之前的告警次数
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
	 * 根据目前传入的AlarmIndicatorsNode id号,查询该id号所在的告警链
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
 * 只存储不被覆盖的节点
 * 
 * @author yag
 *
 */
class AlarmLinkStorer {
	
	/**
	 * 是否被使用
	 */
	private boolean isUsed = false;
	
	private static SysLogger logger = SysLogger.getLogger(AlarmLinkStorer.class);
		
	/**
	 * link链路号
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
		this.setUsed(true);	// 使用中
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
	 * 返回当前链路是否可用，即所有告警节点是否都产生告警信息了
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
	 * 将当前告警节点项置空，回复默认设置为0 
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