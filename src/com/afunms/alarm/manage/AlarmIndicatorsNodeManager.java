/**
 * <p>Description:Tomcat Manager</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-12-07
 */

package com.afunms.alarm.manage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.afunms.alarm.dao.AlarmIndicatorsDao;
import com.afunms.alarm.dao.AlarmIndicatorsNodeDao;
import com.afunms.alarm.dao.AlarmWayDao;
import com.afunms.alarm.model.AlarmIndicators;
import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.model.AlarmWay;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.SessionConstant;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.Constant;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Host;
import com.afunms.system.model.User;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;

public class AlarmIndicatorsNodeManager extends BaseManager implements
        ManagerInterface {

    public String execute(String action) {
        // TODO Auto-generated method stub

        if ("list".equals(action)) {
            return list();
        } else if ("add".equals(action)) {
            return add();
        } else if ("edit".equals(action)) {
            return edit();
        } else if ("save".equals(action)) {
            return save();
        } else if ("update".equals(action)) {
            return update();
        } else if ("delete".equals(action)) {
            return delete();
        } else if ("changeManage".equals(action)) {
            return changeManage();
        } else if ("showtoaddlist".equals(action)) {
            return showtoaddlist();
        } else if ("addselected".equals(action)) {
            return addselected();
        } else if ("showtomultiaddlist".equals(action)) {
            return showtomultiaddlist();
        } else if ("multiadd".equals(action)) {// 阀值配置方法
            return multiadd();
        } else if ("showlist".equals(action)) {
            return showlist();
        } else if ("showAdd".equals(action)) {
            return showAdd();
        } else if ("showChooseNodeList".equals(action)) {
            return showChooseNodeList();
        } else if ("showsave".equals(action)) {
            return showsave();
        } else if ("showEdit".equals(action)) {
            return showEdit();
        } else if ("showUpdate".equals(action)) {
            return showUpdate();
        } else if ("showDelete".equals(action)) {
            return showDelete();
        } else if ("replenish".equals(action)) {// 补充配置阀值
            return multireplenishadd();
        } else if ("changeadd".equals(action)) {// 应用当前阀值
            return multichangeadd();
        }
        return null;
    }

    public String showUpdate() {
        AlarmIndicatorsNode alarmIndicatorsNode = createAlarmIndicatorsNode();
        int id = getParaIntValue("id");
        alarmIndicatorsNode.setId(id);
        AlarmIndicatorsNodeDao alarmIndicatorsNodeDao = new AlarmIndicatorsNodeDao();
        try {
            alarmIndicatorsNodeDao.update(alarmIndicatorsNode);
        } catch (RuntimeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            alarmIndicatorsNodeDao.close();
        }
        return showlist();
    }

    public String showEdit() {
        String jsp = "/topology/threshold/showedit.jsp";

        String nodeid = getParaValue("nodeid");
        String type = getParaValue("type");
        String subtype = getParaValue("subtype");
        String id = getParaValue("id");
        AlarmIndicatorsNode alarmIndicatorsNode = null;
        AlarmIndicatorsNodeDao alarmIndicatorsNodeDao = new AlarmIndicatorsNodeDao();
        try {
            alarmIndicatorsNode = (AlarmIndicatorsNode) alarmIndicatorsNodeDao
                    .findByID(id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            alarmIndicatorsNodeDao.close();
        }

        Hashtable alarmWayHashtable = new Hashtable();

        if (alarmIndicatorsNode != null) {
            nodeid = alarmIndicatorsNode.getNodeid();
            type = alarmIndicatorsNode.getType();
            subtype = alarmIndicatorsNode.getSubtype();
            AlarmWayDao alarmWayDao = new AlarmWayDao();
            try {
                AlarmWay alarmWay0 = (AlarmWay) alarmWayDao
                        .findByID(alarmIndicatorsNode.getWay0());
                if (alarmWay0 != null) {
                    alarmWayHashtable.put("way0", alarmWay0);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                alarmWayDao.close();
            }

            alarmWayDao = new AlarmWayDao();
            try {
                AlarmWay alarmWay1 = (AlarmWay) alarmWayDao
                        .findByID(alarmIndicatorsNode.getWay1());
                if (alarmWay1 != null) {
                    alarmWayHashtable.put("way1", alarmWay1);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                alarmWayDao.close();
            }

            alarmWayDao = new AlarmWayDao();
            try {
                AlarmWay alarmWay2 = (AlarmWay) alarmWayDao
                        .findByID(alarmIndicatorsNode.getWay2());
                if (alarmWay2 != null) {
                    alarmWayHashtable.put("way2", alarmWay2);
                }
            } catch (RuntimeException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                alarmWayDao.close();
            }

        }
        request.setAttribute("alarmWayHashtable", alarmWayHashtable);
        request.setAttribute("alarmIndicatorsNode", alarmIndicatorsNode);
        request.setAttribute("nodeid", nodeid);
        request.setAttribute("type", type);
        request.setAttribute("subtype", subtype);
        return jsp;
    }

    public String showDelete() {
        String[] ids = getParaArrayValue("checkbox");
        AlarmIndicatorsNodeDao alarmIndicatorsNodeDao = new AlarmIndicatorsNodeDao();
        try {
            alarmIndicatorsNodeDao.delete(ids);
        } catch (RuntimeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            alarmIndicatorsNodeDao.close();
        }

        return showlist();

    }

    public String showsave() {

        String[] nodeids = getParaArrayValue("checkbox");
        String type = getParaValue("type");
        String subtype = getParaValue("subtype");
        String nodeid = getParaValue("nodeid");

        // 需要批量应用的阀值指标
        String ids = getParaValue("ids");

        List idlist = new ArrayList();
        List addindilist = new ArrayList();
        if (ids != null && ids.trim().length() > 0) {
            String[] idsplit = ids.split(",");
            if (idsplit != null && idsplit.length > 0) {
                for (int i = 0; i < idsplit.length; i++) {
                    if (idsplit[i] != null && idsplit[i].trim().length() > 0) {
                        idlist.add(idsplit[i]);
                    }
                }
            }
        }

        AlarmIndicatorsDao alarmIndicatorsDao = null;

        if (idlist != null && idlist.size() > 0) {
            alarmIndicatorsDao = new AlarmIndicatorsDao();
            try {
                for (int i = 0; i < idlist.size(); i++) {
                    String indicatorid = (String) idlist.get(i);
                    AlarmIndicators innode = (AlarmIndicators) alarmIndicatorsDao
                            .findByID(indicatorid);
                    addindilist.add(innode);
                }
            } catch (Exception e) {

            } finally {
                alarmIndicatorsDao.close();
            }

        }

        AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();

        if (nodeids != null && nodeids.length > 0) {
            AlarmIndicatorsNodeDao alarmIndicatorsNodeDao = null;
            List updatelist = new ArrayList();
            List savelist = new ArrayList();
            Hashtable nodeindihash = new Hashtable();
            try {
                List list2 = new ArrayList();
                for (int i = 0; i < nodeids.length; i++) {
                    alarmIndicatorsNodeDao = new AlarmIndicatorsNodeDao();
                    try {
                        list2 = alarmIndicatorsNodeDao
                                .getByNodeIdAndTypeAndSubType(nodeids[i], type,
                                        subtype);
                    } catch (Exception e) {

                    } finally {
                        alarmIndicatorsNodeDao.close();
                    }

                    if (list2 != null && list2.size() > 0) {
                        // System.out.println(list2.size() +
                        // "=========list2.size()==============");
                        for (int j = 0; j < list2.size(); j++) {
                            AlarmIndicatorsNode alarmIndicatorsNode = (AlarmIndicatorsNode) list2
                                    .get(j);
                            nodeindihash.put(alarmIndicatorsNode.getName()
                                    + ":" + alarmIndicatorsNode.getType() + ":"
                                    + alarmIndicatorsNode.getSubtype(),
                                    alarmIndicatorsNode);
                        }
                        if (addindilist != null && addindilist.size() > 0) {
                            try {
                                for (int k = 0; k < addindilist.size(); k++) {
                                    AlarmIndicators alarmIndicators = (AlarmIndicators) addindilist
                                            .get(k);
                                    if (nodeindihash
                                            .containsKey(alarmIndicators
                                                    .getName()
                                                    + ":"
                                                    + alarmIndicators.getType()
                                                    + ":"
                                                    + alarmIndicators
                                                            .getSubtype())) {
                                        // 若存在,则修改
                                        AlarmIndicatorsNode alarmIndicatorsNode_update = (AlarmIndicatorsNode) nodeindihash
                                                .get(alarmIndicators.getName()
                                                        + ":"
                                                        + alarmIndicators
                                                                .getType()
                                                        + ":"
                                                        + alarmIndicators
                                                                .getSubtype());
                                        AlarmIndicatorsNode alarmIndicatorsNode_copy = alarmIndicatorsUtil
                                                .createAlarmIndicatorsNodeByAlarmIndicators(alarmIndicators);
                                        alarmIndicatorsNode_copy
                                                .setId(alarmIndicatorsNode_update
                                                        .getId());
                                        alarmIndicatorsNode_copy
                                                .setNodeid(alarmIndicatorsNode_update
                                                        .getNodeid());
                                        // System.out.println(nodeGatherIndicators_copy.getNodeid()+"====update==========nodeGatherIndicators_copy.getNodeid()=================="
                                        // );
                                        updatelist
                                                .add(alarmIndicatorsNode_copy);
                                    } else {
                                        // 不存在,则需要添加进去
                                        AlarmIndicatorsNode alarmIndicatorsNode_copy = alarmIndicatorsUtil
                                                .createAlarmIndicatorsNodeByAlarmIndicators(alarmIndicators);

                                        alarmIndicatorsNode_copy
                                                .setNodeid(nodeids[i]);

                                        // System.out.println(nodeGatherIndicators_copy.getNodeid()+"====add==========nodeGatherIndicators_copy.getNodeid()=================="
                                        // );
                                        savelist.add(alarmIndicatorsNode_copy);
                                    }
                                }
                            } catch (Exception e) {

                            }
                        }
                    } else {
                        // 没设置任何采集指标,则添加全部
                        // SysLogger.info("没设置任何采集指标,则添加全部###");
                        if (addindilist != null && addindilist.size() > 0) {
                            try {
                                for (int k = 0; k < addindilist.size(); k++) {
                                    // 不存在,则需要添加进去
                                    AlarmIndicators alarmIndicators = (AlarmIndicators) addindilist
                                            .get(k);
                                    AlarmIndicatorsNode alarmIndicatorsNode_copy = alarmIndicatorsUtil
                                            .createAlarmIndicatorsNodeByAlarmIndicators(alarmIndicators);

                                    alarmIndicatorsNode_copy
                                            .setNodeid(nodeids[i]);

                                    // System.out.println(nodeGatherIndicators_copy.getNodeid()+"====add==========nodeGatherIndicators_copy.getNodeid()=================="
                                    // );
                                    savelist.add(alarmIndicatorsNode_copy);
                                }
                            } catch (Exception e) {

                            }
                        }
                    }
                }
                if (updatelist != null && updatelist.size() > 0) {
                    try {
                        alarmIndicatorsNodeDao = new AlarmIndicatorsNodeDao();
                        alarmIndicatorsNodeDao.update(updatelist);

                    } catch (Exception e) {

                    } finally {
                        alarmIndicatorsNodeDao.close();
                    }
                }
                if (savelist != null && savelist.size() > 0) {
                    try {
                        alarmIndicatorsNodeDao = new AlarmIndicatorsNodeDao();
                        alarmIndicatorsNodeDao.saveBatch(savelist);
                    } catch (Exception e) {

                    } finally {
                        alarmIndicatorsNodeDao.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return showlist();
    }

    public String showChooseNodeList() {

        String jsp = "/topology/threshold/showchoosenodelist.jsp";

        try {

            String jspFlag = getParaValue("jspFlag");

            String nodeid = getParaValue("nodeid");

            String type = getParaValue("type");

            String subtype = getParaValue("subtype");

            String[] ids = getParaArrayValue("checkbox");
            String idstr = "";
            if (ids != null && ids.length > 0) {
                for (int i = 0; i < ids.length; i++) {
                    idstr = idstr + ids[i] + ",";
                }
            }
            User user = (User) session
                    .getAttribute(SessionConstant.CURRENT_USER);
            AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();

            List nodeDTOlist = alarmIndicatorsUtil.getNodeListByTypeAndSubtype(
                    type, subtype, user.getBusinessids());

            List nodeList = new ArrayList();

            if (nodeDTOlist == null) {
                nodeDTOlist = nodeList;
            }

            if ("multi".equals(jspFlag)) {
                // 批量应用时，去除原来的设备
                for (int i = 0; i < nodeDTOlist.size(); i++) {
                    NodeDTO nodeDTO = (NodeDTO) nodeDTOlist.get(i);
                    if (!String.valueOf(nodeDTO.getId()).equals(nodeid)) {
                        nodeList.add(nodeDTO);
                    }
                }
                nodeDTOlist = nodeList;
            }
            request.setAttribute("nodeDTOlist", nodeDTOlist);
            request.setAttribute("ids", idstr);
            request.setAttribute("nodeid", nodeid);
            request.setAttribute("type", type);
            request.setAttribute("subtype", subtype);
            request.setAttribute("jspFlag", jspFlag);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return jsp;
    }

    /**
     * 在 列表中添加
     * 
     * @return
     */
    public String showAdd() {

        String jsp = "/topology/threshold/showadd.jsp";

        String nodeid = getParaValue("nodeid");
        String type = getParaValue("type");
        String subtype = getParaValue("subtype");

        String jspFlag = getParaValue("jspFlag");

        Hashtable moidhash = new Hashtable();
        try {
            if ("multi".equals(jspFlag)) {
                AlarmIndicatorsUtil alarmindicatorsUtil = new AlarmIndicatorsUtil();
                List<AlarmIndicatorsNode> nodeGatherIndicatorsList = alarmindicatorsUtil
                        .getAlarmIndicatorsForNode(nodeid, type, subtype);
                request.setAttribute("nodeGatherIndicatorsList",
                        nodeGatherIndicatorsList);

                if (nodeGatherIndicatorsList != null) {
                    for (int i = 0; i < nodeGatherIndicatorsList.size(); i++) {
                        AlarmIndicatorsNode alarmIndicatorsNode = nodeGatherIndicatorsList
                                .get(i);
                        moidhash.put(alarmIndicatorsNode.getName(),
                                alarmIndicatorsNode);
                    }
                }
            } else {
                AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                List gatherIndicatorsList = alarmIndicatorsUtil
                        .getAlarmInicatorsThresholdForNode(type, subtype);
                request.setAttribute("gatherIndicatorsList",
                        gatherIndicatorsList);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        request.setAttribute("moidhash", moidhash);
        request.setAttribute("nodeid", nodeid);
        request.setAttribute("type", type);
        request.setAttribute("subtype", subtype);
        request.setAttribute("jspFlag", jspFlag);
        return jsp;
    }

    public String showlist() {
        String jsp = "/topology/threshold/showlist.jsp";
        String nodeid = getParaValue("nodeid");
        String type = getParaValue("type");
        String subtype = getParaValue("subtype");

        if (type == null) {
            type = Constant.ALL_TYPE;
        }

        if (subtype == null) {
            subtype = Constant.ALL_SUBTYPE;
        }

        if (nodeid == null) {
            nodeid = "-1";
        }

        // 先查询出该用户所能查看的所有类型和子类型的设备
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

        // 在查询出所需要出类型和子类型的设备
        List<NodeDTO> nodeDTOlistType = null;
        List<NodeDTO> nodeDTOlistSubtype = null;
        List<NodeDTO> nodeDTOlist = null;
        if (Constant.ALL_TYPE.equals(type)) {
            nodeDTOlistType = allNodeDTOlist;
        } else {
            nodeDTOlistType = new ArrayList<NodeDTO>();
            for (NodeDTO nodeDTO : allNodeDTOlist) {
                if (nodeDTO != null && type.equals(nodeDTO.getType())) {
                    nodeDTOlistType.add(nodeDTO);
                }
            }
        }
        if (Constant.ALL_SUBTYPE.equals(subtype)) {
            nodeDTOlistSubtype = nodeDTOlistType;
        } else {
            nodeDTOlistSubtype = new ArrayList<NodeDTO>();
            for (NodeDTO nodeDTO : nodeDTOlistType) {
                if (nodeDTO != null && subtype.equals(nodeDTO.getSubtype())) {
                    nodeDTOlistSubtype.add(nodeDTO);
                }
            }
        }
        if ("-1".equals(nodeid)) {
            nodeDTOlist = nodeDTOlistSubtype;
        } else {
            nodeDTOlist = new ArrayList<NodeDTO>();
            for (NodeDTO nodeDTO : nodeDTOlistSubtype) {
                if (nodeDTO != null && nodeid.equals(nodeDTO.getNodeid())) {
                    nodeDTOlist.add(nodeDTO);
                }
            }
        }

        Hashtable<String, NodeDTO> nodeDTOHashtable = new Hashtable<String, NodeDTO>();
        // 为了分页
        AlarmIndicatorsNodeDao alarmIndicatorsNodeDao = new AlarmIndicatorsNodeDao();
        List pagelist = new ArrayList();
        String wherestr = "where 1=1 ";
        try {
            if (!"-1".equalsIgnoreCase(type)) {
                wherestr = wherestr + " and type = '" + type + "'";
            }
            if (!"-1".equalsIgnoreCase(subtype)) {
                wherestr = wherestr + " and subtype = '" + subtype + "'";
            }
            if (!"-1".equalsIgnoreCase(nodeid)) {
                wherestr = wherestr + " and nodeid = '" + nodeid + "'";
            }
            wherestr += " and nodeid in(";
            if (nodeDTOlist != null && nodeDTOlist.size() > 0) {
                for (NodeDTO nodeDTO : nodeDTOlist) {
                    wherestr += nodeDTO.getNodeid() + ",";
                }
            }
            wherestr += "-1);";
            list(alarmIndicatorsNodeDao, wherestr);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            alarmIndicatorsNodeDao.close();
        }
        pagelist = (List) request.getAttribute("list");

        if (nodeDTOlist != null) {
            for (NodeDTO nodeDTO : nodeDTOlist) {
                nodeDTOHashtable.put(String.valueOf(nodeDTO.getId()) + ":"
                        + nodeDTO.getType() + ":" + nodeDTO.getSubtype(),
                        nodeDTO);
            }
        }

        request.setAttribute("list", pagelist);

        request.setAttribute("allNodeDTOlist", allNodeDTOlist);

        request.setAttribute("nodelist", nodeDTOlist);
        request.setAttribute("nodeDTOHashtable", nodeDTOHashtable);
        request.setAttribute("nodeid", nodeid);
        request.setAttribute("type", type);
        request.setAttribute("subtype", subtype);

        return jsp;
    }

    public String list() {

        String jsp = "/alarm/threshold/list.jsp";

        try {
            List list = getList();

            request.setAttribute("list", list);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return jsp;
    }

    public String showtoaddlist() {

        String jsp = "/alarm/threshold/showtoaddlist.jsp";

        try {
            String nodeid = getParaValue("nodeid");

            String type = getParaValue("type");

            String subtype = getParaValue("subtype");

            // 获取已经实例化的采集指标
            List moidlist = new ArrayList();
            Hashtable moidhash = new Hashtable();

            AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
            try {
                moidlist = alarmIndicatorsUtil
                        .getAlarmInicatorsThresholdForNode(nodeid, type,
                                subtype);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (moidlist != null && moidlist.size() > 0) {
                for (int i = 0; i < moidlist.size(); i++) {
                    AlarmIndicatorsNode alarmIndicatorsNode = (AlarmIndicatorsNode) moidlist
                            .get(i);
                    moidhash.put(alarmIndicatorsNode.getName(),
                            alarmIndicatorsNode);
                }
            }

            AlarmIndicatorsDao alarmIndicatorsDao = new AlarmIndicatorsDao();
            // 获取采集摸板里对应类型的数据
            List list = new ArrayList();
            try {
                list = alarmIndicatorsDao.getByTypeAndSubType(type, subtype);
            } catch (RuntimeException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                alarmIndicatorsDao.close();
            }
            request.setAttribute("list", list);
            request.setAttribute("moidhash", moidhash);
            request.setAttribute("nodeid", nodeid);
            request.setAttribute("type", type);
            request.setAttribute("subtype", subtype);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return jsp;
    }

    public String showtomultiaddlist() {

        String jsp = "/alarm/threshold/showtomultiaddlist.jsp";

        try {
            String nodeid = getParaValue("nodeid");

            String type = getParaValue("type");

            String subtype = getParaValue("subtype");

            String[] ids = getParaArrayValue("checkbox");
            String idstr = "";
            if (ids != null && ids.length > 0) {
                for (int i = 0; i < ids.length; i++) {
                    idstr = idstr + ids[i] + ",";
                }
            }

            List hostnodelist = new ArrayList();

            if (type.equalsIgnoreCase("host")) {
                HostNodeDao dao = new HostNodeDao();
                // 获取被监视的服务器列表
                List nodelist = new ArrayList();
                try {
                    nodelist = dao.loadMonitorByMonCategory(1, 4);
                } catch (Exception e) {
                } finally {
                    dao.close();
                }
                if (nodelist != null && nodelist.size() > 0) {
                    for (int i = 0; i < nodelist.size(); i++) {
                        HostNode hostnode = (HostNode) nodelist.get(i);
                        if (hostnode.getId() != Integer.parseInt(nodeid)) {
                            hostnodelist.add(hostnode);
                        }
                    }
                }
                System.out.println("hostnodelist=" + hostnodelist.size());
            }
            if (type.equalsIgnoreCase("net")) {
                HostNodeDao dao = new HostNodeDao();
                // 获取被监视的服务器列表
                List nodelist = new ArrayList();
                try {
                    nodelist = dao.loadNetwork(1);
                    System.out.println("nodelist=" + nodelist.size());
                } catch (Exception e) {
                } finally {
                    dao.close();
                }
                if (nodelist != null && nodelist.size() > 0) {
                    for (int i = 0; i < nodelist.size(); i++) {
                        HostNode hostnode = (HostNode) nodelist.get(i);
                        if (hostnode.getId() != Integer.parseInt(nodeid)) {
                            hostnodelist.add(hostnode);
                        }
                    }
                }
                System.out.println("hostnodelist=" + hostnodelist.size());
            }

            request.setAttribute("hostnodelist", hostnodelist);
            request.setAttribute("ids", idstr);
            request.setAttribute("nodeid", nodeid);
            request.setAttribute("type", type);
            request.setAttribute("subtype", subtype);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return jsp;
    }

    public List getList() {

        String nodeid = getParaValue("nodeid");

        String type = getParaValue("type");

        String subtype = getParaValue("subtype");
        List list = null;

        AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
        try {
            list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(
                    nodeid, type, subtype);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Host host = (Host) PollingEngine.getInstance().getNodeByID(
                Integer.parseInt(nodeid));
        NodeUtil nodeUtil = new NodeUtil();
        NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(host);
        subtype = nodedto.getSubtype();
        type = nodedto.getType();
        // if("host".equalsIgnoreCase(type)){
        // //服务器,需要判断操作系统
        // HostNodeDao dao = new HostNodeDao();
        // HostNode hostnode = null;
        // try{
        // hostnode = (HostNode)dao.findByID(nodeid);
        // }catch(Exception e){
        //				
        // }finally{
        // dao.close();
        // }
        // if(hostnode.getSysOid() != null &&
        // hostnode.getSysOid().trim().length()>0){
        // if(hostnode.getSysOid().startsWith("1.3.6.1.4.1.311"))subtype =
        // "windows";
        // if(hostnode.getSysOid().startsWith("1.3.6.1.4.1.2021"))subtype =
        // "linux";
        // if(hostnode.getSysOid().startsWith("1.3.6.1.4.1.2"))subtype = "aix";
        // if(hostnode.getSysOid().startsWith("1.3.6.1.4.1.11"))subtype =
        // "hpunix";
        // if(hostnode.getSysOid().startsWith("1.3.6.1.4.1.42"))subtype =
        // "solaris";
        // if(hostnode.getSysOid().startsWith("as400"))subtype = "as400";
        // }
        // }else if("net".equalsIgnoreCase(type)){
        // //网络设备,需要判断操作系统
        // HostNodeDao dao = new HostNodeDao();
        // HostNode hostnode = null;
        // try{
        // hostnode = (HostNode)dao.findByID(nodeid);
        // }catch(Exception e){
        //				
        // }finally{
        // dao.close();
        // }
        // if(hostnode.getSysOid() != null &&
        // hostnode.getSysOid().trim().length()>0){
        // if(hostnode.getSysOid().startsWith("1.3.6.1.4.1.9"))subtype =
        // "cisco";
        // if(hostnode.getSysOid().startsWith("1.3.6.1.4.1.2011"))subtype =
        // "h3c";
        // if(hostnode.getSysOid().startsWith("1.3.6.1.4.1.25506"))subtype =
        // "h3c";
        // if(hostnode.getSysOid().startsWith("1.3.6.1.4.1.9.2.1.57"))subtype =
        // "entrasys";
        // if(hostnode.getSysOid().startsWith("1.3.6.1.4.1.89"))subtype =
        // "radware";
        // if(hostnode.getSysOid().startsWith("1.3.6.1.4.1.5651"))subtype =
        // "maipu";
        // if(hostnode.getSysOid().startsWith("1.3.6.1.4.1.4881"))subtype =
        // "redgiant";
        // if(hostnode.getSysOid().startsWith("1.3.6.1.4.1.45"))subtype =
        // "northtel";
        // if(hostnode.getSysOid().startsWith("1.3.6.1.4.1.171"))subtype =
        // "dlink";
        // if(hostnode.getSysOid().startsWith("1.3.6.1.4.1.3320"))subtype =
        // "bdcom";
        // }
        // }

        request.setAttribute("nodeid", nodeid);

        request.setAttribute("type", type);

        request.setAttribute("subtype", subtype);
        return list;
    }

    // public String getSQLQueryForList(){
    //		
    // return "";
    // }

    public String add() {
        String jsp = "/alarm/indicators/add.jsp";
        return jsp;
    }

    public String edit() {
        String jsp = "/alarm/threshold/edit.jsp";
        String id = getParaValue("id");
        AlarmIndicatorsNodeDao alarmIndicatorsNodeDao = new AlarmIndicatorsNodeDao();
        AlarmIndicatorsNode alarmIndicatorsNode = null;
        try {
            alarmIndicatorsNode = (AlarmIndicatorsNode) alarmIndicatorsNodeDao
                    .findByID(id);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            alarmIndicatorsNodeDao.close();
        }

        String nodeid = getParaValue("nodeid");
        String type = getParaValue("type");
        String subtype = getParaValue("subtype");

        Hashtable alarmWayHashtable = new Hashtable();

        if (alarmIndicatorsNode != null) {
            nodeid = alarmIndicatorsNode.getNodeid();
            type = alarmIndicatorsNode.getType();
            subtype = alarmIndicatorsNode.getSubtype();
            AlarmWayDao alarmWayDao = null;
            if (alarmIndicatorsNode.getWay0() != null
                    && !alarmIndicatorsNode.getWay0().equals("")) {
                alarmWayDao = new AlarmWayDao();
                try {
                    AlarmWay alarmWay0 = (AlarmWay) alarmWayDao
                            .findByID(alarmIndicatorsNode.getWay0());
                    if (alarmWay0 != null) {
                        alarmWayHashtable.put("way0", alarmWay0);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    alarmWayDao.close();
                }
            }

            if (alarmIndicatorsNode.getWay1() != null
                    && !alarmIndicatorsNode.getWay1().equals("")) {
                alarmWayDao = new AlarmWayDao();
                try {
                    AlarmWay alarmWay1 = (AlarmWay) alarmWayDao
                            .findByID(alarmIndicatorsNode.getWay1());
                    if (alarmWay1 != null) {
                        alarmWayHashtable.put("way1", alarmWay1);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    alarmWayDao.close();
                }
            }

            if (alarmIndicatorsNode.getWay2() != null
                    && !alarmIndicatorsNode.getWay2().equals("")) {
                alarmWayDao = new AlarmWayDao();
                try {
                    AlarmWay alarmWay2 = (AlarmWay) alarmWayDao
                            .findByID(alarmIndicatorsNode.getWay2());
                    if (alarmWay2 != null) {
                        alarmWayHashtable.put("way2", alarmWay2);
                    }
                } catch (RuntimeException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    alarmWayDao.close();
                }
            }

        }
        request.setAttribute("alarmWayHashtable", alarmWayHashtable);

        request.setAttribute("alarmIndicatorsNode", alarmIndicatorsNode);
        return jsp;
    }

    public String update() {

        AlarmIndicatorsNode alarmIndicatorsNode = createAlarmIndicatorsNode();
        int id = getParaIntValue("id");
        alarmIndicatorsNode.setId(id);
        String nodeid = getParaValue("nodeid");
        alarmIndicatorsNode.setNodeid(nodeid);
        AlarmIndicatorsNodeDao alarmIndicatorsNodeDao = new AlarmIndicatorsNodeDao();
        try {
            alarmIndicatorsNodeDao.update(alarmIndicatorsNode);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            alarmIndicatorsNodeDao.close();
        }

        return list();
    }

    public String addselected() {

        String[] ids = getParaArrayValue("moid");
        String type = getParaValue("type");

        String subtype = getParaValue("subtype");

        String nodeid = getParaValue("nodeid");
        if (ids != null && ids.length > 0) {
            AlarmIndicatorsDao alarmIndicatorsDao = new AlarmIndicatorsDao();
            try {
                List list2 = new ArrayList();
                for (int i = 0; i < ids.length; i++) {
                    AlarmIndicators alarmIndicators = (AlarmIndicators) alarmIndicatorsDao
                            .findByID(ids[i]);
                    AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
                    AlarmIndicatorsNode alarmIndicatorsNode = alarmIndicatorsUtil
                            .createAlarmIndicatorsNodeByAlarmIndicators(nodeid,
                                    alarmIndicators);
                    list2.add(alarmIndicatorsNode);
                }
                AlarmIndicatorsNodeDao alarmIndicatorsNodeDao = new AlarmIndicatorsNodeDao();
                try {
                    alarmIndicatorsNodeDao.saveBatch(list2);
                } catch (RuntimeException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    alarmIndicatorsNodeDao.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                alarmIndicatorsDao.close();
            }
        }
        return list();
    }

    /**
     * 
     * 
     * 补充设置阀值
     * 
     * @return
     */
    public String multireplenishadd() {

        String[] nodeids = getParaArrayValue("checkbox");
        String type = getParaValue("type");
        String subtype = getParaValue("subtype");
        String nodeid = getParaValue("nodeid");

        // 需要批量应用的指标
        String ids = getParaValue("ids");

        if (nodeids != null && nodeids.length > 0) {

            List idlist = new ArrayList();
            List addindilist = new ArrayList();
            if (ids != null && ids.trim().length() > 0) {
                String[] idsplit = ids.split(",");
                if (idsplit != null && idsplit.length > 0) {
                    for (int i = 0; i < idsplit.length; i++) {
                        if (idsplit[i] != null
                                && idsplit[i].trim().length() > 0) {
                            idlist.add(idsplit[i]);
                        }
                    }
                }
            }
            // 从数据库中查询出需要需要修改的值对象
            if (idlist != null && idlist.size() > 0) {
                AlarmIndicatorsNodeDao alarmIndicatorsNodeDao = new AlarmIndicatorsNodeDao();
                try {
                    for (int i = 0; i < idlist.size(); i++) {
                        String indicatorid = (String) idlist.get(i);
                        AlarmIndicatorsNode innode = (AlarmIndicatorsNode) alarmIndicatorsNodeDao
                                .findByID(indicatorid);
                        addindilist.add(innode);
                    }
                } catch (Exception e) {

                } finally {
                    alarmIndicatorsNodeDao.close();
                }

            }

            // 建立阀值操作对象

            AlarmIndicatorsNodeDao alarmIndicatorsNodeDao = new AlarmIndicatorsNodeDao();

            // 根据nodeids 删除对象对应的指标
            alarmIndicatorsNodeDao.deletenametypenodeid(nodeids, addindilist);
            // 批量添加阀值指标
            alarmIndicatorsNodeDao.addBatch(nodeids, addindilist);
        }
        return list();
        // return list();
    }

    /**
     * 
     * 应用当前阀值配置
     * 
     * @return
     */
    public String multichangeadd() {

        String[] nodeids = getParaArrayValue("checkbox");
        String type = getParaValue("type");
        String subtype = getParaValue("subtype");
        String nodeid = getParaValue("nodeid");

        // 需要批量应用的指标
        String ids = getParaValue("ids");

        if (nodeids != null && nodeids.length > 0) {

            List idlist = new ArrayList();
            List addindilist = new ArrayList();
            if (ids != null && ids.trim().length() > 0) {
                String[] idsplit = ids.split(",");
                if (idsplit != null && idsplit.length > 0) {
                    for (int i = 0; i < idsplit.length; i++) {
                        if (idsplit[i] != null
                                && idsplit[i].trim().length() > 0) {
                            idlist.add(idsplit[i]);
                        }
                    }
                }
            }
            // 从数据库中查询出需要需要修改的值对象
            if (idlist != null && idlist.size() > 0) {
                AlarmIndicatorsNodeDao alarmIndicatorsNodeDao = new AlarmIndicatorsNodeDao();
                try {
                    for (int i = 0; i < idlist.size(); i++) {
                        String indicatorid = (String) idlist.get(i);
                        AlarmIndicatorsNode innode = (AlarmIndicatorsNode) alarmIndicatorsNodeDao
                                .findByID(indicatorid);
                        addindilist.add(innode);
                    }

                    // 根据nodeids 删除对象对应的指标
                    alarmIndicatorsNodeDao.deletenodeid(nodeids);
                    // 批量添加阀值指标
                    alarmIndicatorsNodeDao.addBatch(nodeids, addindilist);

                    alarmIndicatorsNodeDao.close();

                } catch (Exception e) {

                } finally {
                    alarmIndicatorsNodeDao.close();
                }

            }

        }
        return list();
    }

    /***************************************************************************
     * 
     * 配置阀值原方法
     * 
     * @return
     */
    public String multiadd() {

        String[] nodeids = getParaArrayValue("checkbox");
        String type = getParaValue("type");
        String subtype = getParaValue("subtype");
        String nodeid = getParaValue("nodeid");

        // 需要批量应用的指标
        String ids = getParaValue("ids");

        List idlist = new ArrayList();
        List addindilist = new ArrayList();
        if (nodeids != null && nodeids.length > 0) {
            if (ids != null && ids.trim().length() > 0) {
                String[] idsplit = ids.split(",");
                if (idsplit != null && idsplit.length > 0) {
                    for (int i = 0; i < idsplit.length; i++) {
                        if (idsplit[i] != null
                                && idsplit[i].trim().length() > 0) {
                            idlist.add(idsplit[i]);
                        }
                    }
                }
            }
            // 从数据库中查询出需要需要修改的值对象
            if (idlist != null && idlist.size() > 0) {
                AlarmIndicatorsNodeDao alarmIndicatorsNodeDao = new AlarmIndicatorsNodeDao();
                try {
                    for (int i = 0; i < idlist.size(); i++) {
                        String indicatorid = (String) idlist.get(i);
                        AlarmIndicatorsNode innode = (AlarmIndicatorsNode) alarmIndicatorsNodeDao
                                .findByID(indicatorid);
                        addindilist.add(innode);
                    }
                } catch (Exception e) {

                } finally {
                    alarmIndicatorsNodeDao.close();
                }

            }
        }
        AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();

        if (nodeids != null && nodeids.length > 0) {
            AlarmIndicatorsNodeDao alarmIndicatorsNodeDao = null;
            List updatelist = new ArrayList();
            List savelist = new ArrayList();
            Hashtable nodeindihash = new Hashtable();
            try {
                List list2 = new ArrayList();
                for (int i = 0; i < nodeids.length; i++) {
                    alarmIndicatorsNodeDao = new AlarmIndicatorsNodeDao();
                    try {
                        list2 = alarmIndicatorsNodeDao
                                .getByNodeIdAndTypeAndSubType(nodeids[i], type,
                                        subtype);
                    } catch (Exception e) {

                    } finally {
                        alarmIndicatorsNodeDao.close();
                    }

                    if (list2 != null && list2.size() > 0) {
                        // System.out.println(list2.size() +
                        // "=========list2.size()==============");
                        for (int j = 0; j < list2.size(); j++) {
                            AlarmIndicatorsNode alarmIndicatorsNode = (AlarmIndicatorsNode) list2
                                    .get(j);
                            nodeindihash.put(alarmIndicatorsNode.getName()
                                    + ":" + alarmIndicatorsNode.getType() + ":"
                                    + alarmIndicatorsNode.getSubtype(),
                                    alarmIndicatorsNode);
                        }
                        if (addindilist != null && addindilist.size() > 0) {
                            try {
                                for (int k = 0; k < addindilist.size(); k++) {
                                    AlarmIndicatorsNode alarmIndicators = (AlarmIndicatorsNode) addindilist
                                            .get(k);
                                    if (nodeindihash
                                            .containsKey(alarmIndicators
                                                    .getName()
                                                    + ":"
                                                    + alarmIndicators.getType()
                                                    + ":"
                                                    + alarmIndicators
                                                            .getSubtype())) {
                                        // 若存在,则修改
                                        AlarmIndicatorsNode alarmIndicatorsNode_update = (AlarmIndicatorsNode) nodeindihash
                                                .get(alarmIndicators.getName()
                                                        + ":"
                                                        + alarmIndicators
                                                                .getType()
                                                        + ":"
                                                        + alarmIndicators
                                                                .getSubtype());
                                        AlarmIndicatorsNode alarmIndicatorsNode_copy = alarmIndicatorsUtil
                                                .createAlarmIndicatorsNodeByAlarmIndicators(alarmIndicators);
                                        alarmIndicatorsNode_copy
                                                .setId(alarmIndicatorsNode_update
                                                        .getId());
                                        alarmIndicatorsNode_copy
                                                .setNodeid(alarmIndicatorsNode_update
                                                        .getNodeid());
                                        // System.out.println(nodeGatherIndicators_copy.getNodeid()+"====update==========nodeGatherIndicators_copy.getNodeid()=================="
                                        // );
                                        updatelist
                                                .add(alarmIndicatorsNode_copy);
                                    } else {
                                        // 不存在,则需要添加进去
                                        AlarmIndicatorsNode alarmIndicatorsNode_copy = alarmIndicatorsUtil
                                                .createAlarmIndicatorsNodeByAlarmIndicators(alarmIndicators);

                                        alarmIndicatorsNode_copy
                                                .setNodeid(nodeids[i]);

                                        // System.out.println(nodeGatherIndicators_copy.getNodeid()+"====add==========nodeGatherIndicators_copy.getNodeid()=================="
                                        // );
                                        savelist.add(alarmIndicatorsNode_copy);
                                    }
                                }
                            } catch (Exception e) {

                            }
                        }
                    } else {
                        // 没设置任何采集指标,则添加全部
                        // SysLogger.info("没设置任何采集指标,则添加全部###");
                        if (addindilist != null && addindilist.size() > 0) {
                            try {
                                for (int k = 0; k < addindilist.size(); k++) {
                                    // 不存在,则需要添加进去
                                    AlarmIndicators alarmIndicators = (AlarmIndicators) addindilist
                                            .get(k);
                                    AlarmIndicatorsNode alarmIndicatorsNode_copy = alarmIndicatorsUtil
                                            .createAlarmIndicatorsNodeByAlarmIndicators(alarmIndicators);

                                    alarmIndicatorsNode_copy
                                            .setNodeid(nodeids[i]);

                                    // System.out.println(nodeGatherIndicators_copy.getNodeid()+"====add==========nodeGatherIndicators_copy.getNodeid()=================="
                                    // );
                                    savelist.add(alarmIndicatorsNode_copy);
                                }
                            } catch (Exception e) {

                            }
                        }
                    }
                }
                if (updatelist != null && updatelist.size() > 0) {
                    try {
                        alarmIndicatorsNodeDao = new AlarmIndicatorsNodeDao();
                        alarmIndicatorsNodeDao.update(updatelist);

                    } catch (Exception e) {

                    } finally {
                        alarmIndicatorsNodeDao.close();
                    }
                }
                if (savelist != null && savelist.size() > 0) {
                    try {
                        alarmIndicatorsNodeDao = new AlarmIndicatorsNodeDao();
                        alarmIndicatorsNodeDao.saveBatch(savelist);
                    } catch (Exception e) {

                    } finally {
                        alarmIndicatorsNodeDao.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return showlist();

    }

    public String save() {

        AlarmIndicatorsNode alarmIndicatorsNode = createAlarmIndicatorsNode();

        AlarmIndicatorsNodeDao alarmIndicatorsNodeDao = new AlarmIndicatorsNodeDao();
        try {
            alarmIndicatorsNodeDao.save(alarmIndicatorsNode);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            alarmIndicatorsNodeDao.close();
        }
        return list();
    }

    public String delete() {

        String id = getParaValue("id");
        String[] ids = { id };
        String value = getParaValue("value");

        AlarmIndicatorsNodeDao alarmIndicatorsNodeDao = new AlarmIndicatorsNodeDao();
        try {
            alarmIndicatorsNodeDao.delete(ids);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            alarmIndicatorsNodeDao.close();
        }
        return list();
    }

    public AlarmIndicatorsNode createAlarmIndicatorsNode() {
        String name = getParaValue("name");
        String type = getParaValue("type");
        String subtype = getParaValue("subtype");
        String datatype = getParaValue("datatype");
        String nodeid = getParaValue("nodeid");
        String moid = getParaValue("moid");
        int threshold = getParaIntValue("threshold");
        String threshold_unit = getParaValue("threshold_unit");
        int compare = getParaIntValue("compare");
        int compare_type = getParaIntValue("compare_type");
        String alarm_times = getParaValue("alarm_times");
        String alarm_info = getParaValue("alarm_info");
        String alarm_level = getParaValue("alarm_level");
        String enabled = getParaValue("enabled");
        // String poll_interval = getParaValue("poll_interval");
        // String interval_unit = getParaValue("interval_unit");
        String subentity = getParaValue("subentity");
        String limenvalue0 = getParaValue("limenvalue0");
        String limenvalue1 = getParaValue("limenvalue1");
        String limenvalue2 = getParaValue("limenvalue2");
        String time0 = getParaValue("time0");
        String time1 = getParaValue("time1");
        String time2 = getParaValue("time2");
        String sms0 = getParaValue("sms0");
        String sms1 = getParaValue("sms1");
        String sms2 = getParaValue("sms2");
        String way0 = getParaValue("way0-id");
        String way1 = getParaValue("way1-id");
        String way2 = getParaValue("way2-id");
        String category = getParaValue("category");
        String descr = getParaValue("descr");
        String unit = getParaValue("unit");

        threshold = 1;
        // compare = 1;
        compare_type = 1;

        AlarmIndicatorsNode alarmIndicatorsNode = new AlarmIndicatorsNode();
        alarmIndicatorsNode.setName(name);
        alarmIndicatorsNode.setType(type);
        alarmIndicatorsNode.setSubtype(subtype);
        alarmIndicatorsNode.setDatatype(datatype);
        alarmIndicatorsNode.setNodeid(nodeid);
        alarmIndicatorsNode.setMoid(moid);
        alarmIndicatorsNode.setThreshlod(threshold);
        alarmIndicatorsNode.setThreshlod_unit(threshold_unit);
        alarmIndicatorsNode.setCompare(compare);
        alarmIndicatorsNode.setCompare_type(compare_type);
        alarmIndicatorsNode.setAlarm_times(alarm_times);
        alarmIndicatorsNode.setAlarm_info(alarm_info);
        alarmIndicatorsNode.setAlarm_level(alarm_level);
        alarmIndicatorsNode.setEnabled(enabled);
        // String[] interstr = poll_interval.split("-");
        // alarmIndicatorsNode.setPoll_interval(interstr[0]);
        // alarmIndicatorsNode.setInterval_unit(interstr[1]);
        alarmIndicatorsNode.setSubentity(subentity);
        alarmIndicatorsNode.setLimenvalue0(limenvalue0);
        alarmIndicatorsNode.setLimenvalue1(limenvalue1);
        alarmIndicatorsNode.setLimenvalue2(limenvalue2);
        alarmIndicatorsNode.setTime0(time0);
        alarmIndicatorsNode.setTime1(time1);
        alarmIndicatorsNode.setTime2(time2);
        alarmIndicatorsNode.setSms0(sms0);
        alarmIndicatorsNode.setSms1(sms1);
        alarmIndicatorsNode.setSms2(sms2);
        alarmIndicatorsNode.setWay0(way0);
        alarmIndicatorsNode.setWay1(way1);
        alarmIndicatorsNode.setWay2(way2);
        alarmIndicatorsNode.setCategory(category);
        alarmIndicatorsNode.setDescr(descr);
        alarmIndicatorsNode.setUnit(unit);
        return alarmIndicatorsNode;
    }

    public String changeManage() {

        String id = getParaValue("id");

        String value = getParaValue("value");

        AlarmIndicatorsNodeDao alarmIndicatorsNodeDao = new AlarmIndicatorsNodeDao();
        try {
            alarmIndicatorsNodeDao.changeMonfalgById(id, value);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            alarmIndicatorsNodeDao.close();
        }

        return list();
    }

    public String setDefaultValue() {

        return null;
    }

}