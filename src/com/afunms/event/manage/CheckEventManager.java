package com.afunms.event.manage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import com.afunms.alarm.dao.AlarmIndicatorsNodeDao;
import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.SessionConstant;
import com.afunms.config.dao.BusinessDao;
import com.afunms.config.model.Business;
import com.afunms.event.dao.CheckEventDao;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.model.CheckEvent;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.Constant;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.system.model.User;
import com.afunms.topology.model.MonitorNodeDTO;

/**
 * @author admin email:
 * @version time: Dec 16, 201111:08:20 AM description:
 */
public class CheckEventManager extends BaseManager implements ManagerInterface {

    public String execute(String action) {

        // TODO Auto-generated method stub
        if ("list".equals(action)) {
            return list();
        }
        return null;
    }

    /**
     * ��ȡ��ǰ�澯ҳ��
     * 
     * @return
     */
    private String list() {
        String type = getParaValue("type");
        String subtype = getParaValue("subtype");
        String nodeid = getParaValue("nodeid");

        // Ĭ�ϲ�ѯ���е����ͺ����е������͵��豸
        if (type == null) {
            type = Constant.ALL_TYPE;
        }

        if (subtype == null) {
            subtype = Constant.ALL_SUBTYPE;
        }

        if (nodeid == null) {
            nodeid = "-1";
        }

        // �Ȳ�ѯ�����û����ܲ鿴���������ͺ������͵��豸
        List<NodeDTO> allNodeDTOlist = null;
        User user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
        NodeUtil nodeUtil = new NodeUtil();
        String bid = user.getBusinessids();
        boolean isCheck = false;
        if (user.getRole() == 0) {
            isCheck = true;
        } else if (bid == null || bid.trim().length() == 0) {
            isCheck = false;
        } else {
            String[] bids = bid.trim().split(",");
            if (bids == null || bids.length == 0) {
                isCheck = false;
            } else {
                nodeUtil.setBid(bid);
                isCheck = true;
            }
        }
        if (isCheck) {
            List<BaseVo> nodelist = nodeUtil.getNodeByTyeAndSubtype(
                            Constant.ALL_TYPE, Constant.ALL_SUBTYPE);
            allNodeDTOlist = nodeUtil.conversionToNodeDTO(nodelist);
        }
        if (allNodeDTOlist == null) {
            allNodeDTOlist = new ArrayList<NodeDTO>();
        }
        Hashtable<String, NodeDTO> allNodeDTOHashtable = new Hashtable<String, NodeDTO>();
        for (NodeDTO nodeDTO : allNodeDTOlist) {
            allNodeDTOHashtable.put(String.valueOf(nodeDTO.getId()) + ":"
                            + nodeDTO.getType() + ":" + nodeDTO.getSubtype(),
                            nodeDTO);
        }

        // ��ѯ�����û����ܲ鿴���������ͺ������͵��豸��ǰ�澯
        // Ϊ�˷�ҳ
        CheckEventDao checkEventDao = new CheckEventDao();
        try {
            StringBuffer sqlBuffer = new StringBuffer();
            sqlBuffer.append(" where 1=1 ");

            if (!"-1".equalsIgnoreCase(type)) {
                sqlBuffer.append(" and type='" + type + "'");
            }
            if (!"-1".equalsIgnoreCase(subtype)) {
                sqlBuffer.append(" and subtype='" + subtype + "'");
            }
            if (!"-1".equalsIgnoreCase(nodeid)) {
                sqlBuffer.append(" and node_id='" + nodeid + "'");
            }
            sqlBuffer.append(" and node_id in("); // �����������в���
            if (allNodeDTOlist != null && allNodeDTOlist.size() > 0) {
                for (NodeDTO nodeDTO : allNodeDTOlist) {
                    sqlBuffer.append(nodeDTO.getNodeid() + ",");
                }
            }
            sqlBuffer.append("-1);");
            list(checkEventDao, sqlBuffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            checkEventDao.close();
        }
        // ��ѯ�ĵ�ǰ�澯�б�
        List list = (List) request.getAttribute("list");
        
        //�Ը澯�������򣬽��澯����ߵ�����ǰ�� wupinlong add 2013/10/9
        sort(list);

        request.setAttribute("list", list);
        request.setAttribute("allNodeDTOlist", allNodeDTOlist);
        request.setAttribute("allNodeDTOHashtable", allNodeDTOHashtable);
        request.setAttribute("nodeid", nodeid);
        request.setAttribute("type", type);
        request.setAttribute("subtype", subtype);

        return "/alarm/event/curalarm.jsp";
    }
    
    /**
	 * 2013/10/09 wupinlong
	 * sort:
	 * <p>
	 *
	 * @param list
	 * @return ����Status���澯��������֮���List
	 * @since   v1.01
	 */
	public List sort(List list){
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				CheckEvent vo1 = (CheckEvent)o1;
				CheckEvent vo2 = (CheckEvent)o2;
				
            	int result = 0;
            	int alarmLevel1 = vo1.getAlarmlevel();
            	int alarmLevel2 = vo2.getAlarmlevel();
                if (alarmLevel1 < alarmLevel2){
                	result = 1;
                } else if(alarmLevel1 == alarmLevel2){
                	result = 0;
                }else{
                	result = -1;
                }
                return result;
            }
		});
		return list;
	}

}
