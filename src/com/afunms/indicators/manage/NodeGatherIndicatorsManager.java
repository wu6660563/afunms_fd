/**
 * <p>Description:DiscoverManager</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-12
 */

package com.afunms.indicators.manage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.dao.GatherIndicatorsDao;
import com.afunms.indicators.dao.NodeGatherIndicatorsDao;
import com.afunms.indicators.model.GatherIndicators;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.indicators.util.Constant;
import com.afunms.indicators.util.GatherIndicatorsUtil;
import com.afunms.indicators.util.NodeGatherIndicatorsUtil;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Host;
import com.afunms.system.model.User;

/**
 * 
 * �豸ָ�� manage
 * 
 */
public class NodeGatherIndicatorsManager extends BaseManager implements
        ManagerInterface {

    private static SysLogger logger = SysLogger.getLogger(NodeGatherIndicatorsManager.class.getName());

    public String execute(String action) {
        if ("list".equals(action)) {
            return list();
        } else if ("add".equals(action)) {
            return add();
        } else if ("save".equals(action)) {
            return save();
        } else if ("edit".equals(action)) {
            return edit();
        } else if ("update".equals(action)) {
            return update();
        } else if ("delete".equals(action)) {
            return delete();
        } else if ("showtomultiaddlist".equals(action)) {
            return showtomultiaddlist();
        } else if ("showlist".equals(action)) {
            return showlist();
        } else if ("showAdd".equals(action)) {
            return showAdd();
        } else if ("showChooseNodeList".equals(action)) {
            return showChooseNodeList();
        } else if ("showsave".equals(action)) {
            return showsave();
        } else if ("multiadd".equals(action)) {
            return multichange();
        } else if ("showEdit".equals(action)) {
            return showEdit();
        } else if ("showUpdate".equals(action)) {
            return showUpdate();
        } else if ("showDelete".equals(action)) {
            return showDelete();
        } else if ("multisupplement".equals(action)) {// ����Ӧ��
            return multisupplement();

        }
        setErrorCode(ErrorMessage.ACTION_NO_FOUND);
        return null;
    }

    public String list() {
        String menuflag = getParaValue("menuflag");
        String jsp = "/topology/nodegatherindicators/list.jsp?menuflag="
                + menuflag;
        String nodeid = getParaValue("nodeid");
        String type = getParaValue("type");
        String subtype = getParaValue("subtype");
        List list = null;
        NodeGatherIndicatorsDao dao = new NodeGatherIndicatorsDao();
        try {
            Host host = (Host) PollingEngine.getInstance().getNodeByID(
                    Integer.parseInt(nodeid));
            String isDefalut = "0";
            if (null == host || host.getCollecttype() == 1) {
                isDefalut = "1";
            }
            list = dao.findByNodeIdAndTypeAndSubtype(nodeid, type, subtype,
                    isDefalut);
        } catch (RuntimeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        request.setAttribute("list", list);
        request.setAttribute("nodeid", nodeid);
        request.setAttribute("type", type);
        request.setAttribute("subtype", subtype);

        return jsp;
    }

    public String add() {

        String jsp = "/topology/nodegatherindicators/add.jsp";

        String nodeid = getParaValue("nodeid");
        String type = getParaValue("type");
        String subtype = getParaValue("subtype");
        Hashtable moidhash = new Hashtable();
        Host host = (Host) PollingEngine.getInstance().getNodeByID(
                Integer.parseInt(nodeid));
        String isDefalut = "0";
        if (null == host || host.getCollecttype() == 1) {
            isDefalut = "1";
        }
        try {
            GatherIndicatorsUtil gatherIndicatorsUtil = new GatherIndicatorsUtil();
            List gatherIndicatorsList = gatherIndicatorsUtil
                    .getGatherIndicatorsByTypeAndSubtype(type, subtype,
                            isDefalut);
            request.setAttribute("gatherIndicatorsList", gatherIndicatorsList);

            NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
            List<NodeGatherIndicators> nodeGatherIndicatorsList = nodeGatherIndicatorsUtil
                    .getGatherIndicatorsForNode(nodeid, type, subtype,
                            isDefalut);

            if (nodeGatherIndicatorsList != null) {
                for (int i = 0; i < nodeGatherIndicatorsList.size(); i++) {
                    NodeGatherIndicators nodeGatherIndicators = nodeGatherIndicatorsList
                            .get(i);
                    moidhash.put(nodeGatherIndicators.getName(),
                            nodeGatherIndicators);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        request.setAttribute("moidhash", moidhash);
        request.setAttribute("nodeid", nodeid);
        request.setAttribute("type", type);
        request.setAttribute("subtype", subtype);
        return jsp;
    }

    public String save() {
        // NodeGatherIndicators nodeGatherIndicators = createGatherIndicators();

        String[] ids = getParaArrayValue("checkbox");
        String nodeid = getParaValue("nodeid");

        NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
        nodeGatherIndicatorsUtil.addGatherIndicatorsForNode(nodeid, ids);
        // NodeGatherIndicatorsUtil gatherutil = new NodeGatherIndicatorsUtil();
        nodeGatherIndicatorsUtil.refreshShareDataGather();
        nodeGatherIndicatorsUtil = null;
        return list();
    }

    public String edit() {
        String jsp = "/topology/nodegatherindicators/edit.jsp";

        String nodeid = getParaValue("nodeid");
        String type = getParaValue("type");
        String subtype = getParaValue("subtype");
        String id = getParaValue("id");
        NodeGatherIndicators nodeGatherIndicators = null;
        NodeGatherIndicatorsDao gatherIndicatorsDao = new NodeGatherIndicatorsDao();
        try {
            nodeGatherIndicators = (NodeGatherIndicators) gatherIndicatorsDao
                    .findByID(id);
        } catch (RuntimeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        request.setAttribute("nodeGatherIndicators", nodeGatherIndicators);
        request.setAttribute("nodeid", nodeid);
        request.setAttribute("type", type);
        request.setAttribute("subtype", subtype);
        return jsp;
    }

    public String update() {
        NodeGatherIndicators nodeGatherIndicators = createGatherIndicators();
        int id = getParaIntValue("id");
        nodeGatherIndicators.setId(id);
        NodeGatherIndicatorsDao nodeGatherIndicatorsDao = new NodeGatherIndicatorsDao();
        try {
            nodeGatherIndicatorsDao.update(nodeGatherIndicators);
            NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
            nodeGatherIndicatorsUtil.refreshShareDataGather();
            nodeGatherIndicatorsUtil = null;
        } catch (RuntimeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            nodeGatherIndicatorsDao.close();
        }
        return list();
    }

    public String delete() {
        String[] ids = getParaArrayValue("checkbox");
        NodeGatherIndicatorsDao nodeGatherIndicatorsDao = new NodeGatherIndicatorsDao();
        try {
            nodeGatherIndicatorsDao.delete(ids);
            NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
            nodeGatherIndicatorsUtil.refreshShareDataGather();
            nodeGatherIndicatorsUtil = null;
        } catch (RuntimeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            nodeGatherIndicatorsDao.close();
        }

        return list();

    }

    public String showtomultiaddlist() {

        String jsp = "/topology/nodegatherindicators/showtomultiaddlist.jsp";

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

            // System.out.println(nodeid + "====" + type + "=======" + subtype);

            NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();

            List nodeDTOlist = nodeGatherIndicatorsUtil
                    .getNodeListByTypeAndSubtype(type, subtype);

            if (nodeDTOlist != null) {
                for (int i = 0; i < nodeDTOlist.size(); i++) {
                    NodeDTO nodeDTO = (NodeDTO) nodeDTOlist.get(i);
                    if (String.valueOf(nodeDTO.getId()).equals(nodeid)) {
                        nodeDTOlist.remove(i);
                    }
                }
            }
            request.setAttribute("nodeDTOlist", nodeDTOlist);
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

    /**
     * 
     * ����ɼ�ָ�����÷��� �Ȱ�ѡ�������Ҫ�޸ĵ�ָ��ɾ����Ȼ�����ѡ��Ķ����ٴ���Ӳɼ�ָ��
     * 
     * @return
     */
    public String multisupplement() {

        String[] nodeids = getParaArrayValue("checkbox");
        String type = getParaValue("type");
        String subtype = getParaValue("subtype");
        String nodeid = getParaValue("nodeid");

        // ��Ҫ�����޸ĵķ�ֵָ��
        String ids = getParaValue("ids");

        List idlist = new ArrayList();// ��������ָ���id
        List addindilist = new ArrayList();// �����ɼ���ǰ����ָ�����

        // �Բɼ�ָ��id���з���
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

        NodeGatherIndicatorsDao nodeGatherIndicatorsDao = null;
        //
        if (idlist != null && idlist.size() > 0) {
            nodeGatherIndicatorsDao = new NodeGatherIndicatorsDao();
            try {// ��ѯ��ǰ�Ĳɼ�ָ���¼
                for (int i = 0; i < idlist.size(); i++) {
                    String indicatorid = (String) idlist.get(i);
                    NodeGatherIndicators innode = (NodeGatherIndicators) nodeGatherIndicatorsDao
                            .findByID(indicatorid);
                    addindilist.add(innode);

                }
            } catch (Exception e) {

            } finally {
                nodeGatherIndicatorsDao.close();
            }

        }
        // ָ�괦�����
        NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
        // ��ʼ�����Բɼ�ָ����д���

        List nghilist = new ArrayList();
        for (int i = 0; i < addindilist.size(); i++) {
            NodeGatherIndicators nodeg = (NodeGatherIndicators) addindilist
                    .get(i);
            // ɾ���ڵ��id
            nodeGatherIndicatorsUtil.deleteGatherIndicatorsFortype(nodeg
                    .getName(), nodeg.getType(), nodeg.getSubtype(), nodeg
                    .getNodeid(), nodeids);
            //
            if (nodeids != null && nodeids.length > 0) {

                for (int n = 0; n < nodeids.length; n++) {
                    // if(!nodeids[n].equals(nodeg.getNodeid()))
                    // {//���˵�ԭ����ָ��
                    NodeGatherIndicators nodeglist = new NodeGatherIndicators();
                    nodeglist.setAlias(nodeg.getAlias());
                    nodeglist.setCategory(nodeg.getCategory());
                    nodeglist.setDescription(nodeg.getDescription());
                    nodeglist.setInterval_unit(nodeg.getInterval_unit());
                    nodeglist.setIsCollection(nodeg.getIsCollection());
                    nodeglist.setIsDefault(nodeg.getIsDefault());
                    nodeglist.setName(nodeg.getName());
                    nodeglist.setNodeid(nodeids[n]);
                    nodeglist.setPoll_interval(nodeg.getPoll_interval());
                    nodeglist.setSubtype(nodeg.getSubtype());
                    nodeglist.setType(nodeg.getType());
                    nodeglist.setClasspath(nodeg.getClasspath());

                    nghilist.add(nodeglist);
                    // }
                }

                // ������ӷ�ֵָ��

            }

        }

        nodeGatherIndicatorsUtil.addGatherIndicatorsFornode(nghilist);
        nghilist = null;

        String jspFlag = getParaValue("jspFlag");
        // System.out.println(jspFlag);
        if ("multi".equals(jspFlag)) {
            return showlist();
        }
        // NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new
        // NodeGatherIndicatorsUtil();
        nodeGatherIndicatorsUtil.refreshShareDataGather();
        nodeGatherIndicatorsUtil = null;
        return list();
    }

    /**
     * ����Ӧ�õ�ǰָ�� �Ȱ�ѡ��������еĲɼ�ָ��ɾ����Ȼ�����ѡ��Ķ����ٴ���Ӳɼ�ָ��
     * 
     * @return
     */
    public String multichange() {
        String[] nodeids = getParaArrayValue("checkbox");
        String type = getParaValue("type");
        String subtype = getParaValue("subtype");
        String nodeid = getParaValue("nodeid");
        // ��Ҫ�����޸ĵķ�ֵָ��
        String ids = getParaValue("ids");

        List idlist = new ArrayList();// ��������ָ���id
        List addindilist = new ArrayList();// �����ɼ���ǰ����ָ�����

        // �Բɼ�ָ��id���з���
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

        NodeGatherIndicatorsDao nodeGatherIndicatorsDao = null;
        // ����ָ���Ĳɼ�Ԫ����ѯ��Ԫ�ɼ�ָ����Ŀ�����Ա����ڵ������б���
        if (idlist != null && idlist.size() > 0) {
            nodeGatherIndicatorsDao = new NodeGatherIndicatorsDao();
            try {// ��ѯ��ǰ�Ĳɼ�ָ���¼
                for (int i = 0; i < idlist.size(); i++) {
                    String indicatorid = (String) idlist.get(i);
                    NodeGatherIndicators innode = (NodeGatherIndicators) nodeGatherIndicatorsDao
                            .findByID(indicatorid);
                    addindilist.add(innode);

                }
            } catch (Exception e) {
                logger.error("����Ӧ�òɼ�ָ�����", e);
            } finally {
                nodeGatherIndicatorsDao.close();
            }

        }
        // ָ�괦�����
        NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
        // �Ȱ�ԭ������Ĳɼ�ָ��ȫ��ɾ��
        
        if (nodeids != null && nodeids.length > 0) {
            List updatelist = new ArrayList();
            List savelist = new ArrayList();
            Hashtable nodeindihash = new Hashtable();
            try {
                List list2 = new ArrayList();
                for (int i = 0; i < nodeids.length; i++) {
                    nodeGatherIndicatorsDao = new NodeGatherIndicatorsDao();
                    try {
                        list2 = nodeGatherIndicatorsDao
                                .findByNodeIdAndTypeAndSubtype(nodeids[i],
                                        type, subtype);
                    } catch (Exception e) {
                        logger.info("����豸ָ��ʱ��������", e);
                    } finally {
                        nodeGatherIndicatorsDao.close();
                    }

                    if (list2 != null && list2.size() > 0) {
                        for (int j = 0; j < list2.size(); j++) {
                            NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators) list2
                                    .get(j);
                            nodeindihash.put(nodeGatherIndicators.getName()
                                    + ":" + nodeGatherIndicators.getType()
                                    + ":" + nodeGatherIndicators.getSubtype(),
                                    nodeGatherIndicators);
                        }
                        if (addindilist != null && addindilist.size() > 0) {
                            try {
                                for (int k = 0; k < addindilist.size(); k++) {
                                    NodeGatherIndicators gatherIndicators = (NodeGatherIndicators) addindilist
                                            .get(k);
                                    if (nodeindihash
                                            .containsKey(gatherIndicators
                                                    .getName()
                                                    + ":"
                                                    + gatherIndicators
                                                            .getType()
                                                    + ":"
                                                    + gatherIndicators
                                                            .getSubtype())) {
                                        // ������,���޸�
                                        NodeGatherIndicators nodeGatherIndicators_update = (NodeGatherIndicators) nodeindihash
                                                .get(gatherIndicators.getName()
                                                        + ":"
                                                        + gatherIndicators
                                                                .getType()
                                                        + ":"
                                                        + gatherIndicators
                                                                .getSubtype());
                                        NodeGatherIndicators nodeGatherIndicators_copy = nodeGatherIndicatorsUtil
                                                .createGatherIndicatorsForNode(gatherIndicators);
                                        nodeGatherIndicators_copy
                                                .setId(nodeGatherIndicators_update
                                                        .getId());
                                        nodeGatherIndicators_copy
                                                .setNodeid(nodeGatherIndicators_update
                                                        .getNodeid());
                                        updatelist
                                                .add(nodeGatherIndicators_copy);
                                    } else {
                                        // ������,����Ҫ��ӽ�ȥ
                                        NodeGatherIndicators nodeGatherIndicators_copy = nodeGatherIndicatorsUtil
                                                .createGatherIndicatorsForNode(gatherIndicators);

                                        nodeGatherIndicators_copy
                                                .setNodeid(nodeids[i]);

                                        savelist.add(nodeGatherIndicators_copy);
                                    }
                                }
                            } catch (Exception e) {

                            }
                        }
                    } else {
                        // û�����κβɼ�ָ��,�����ȫ��
                        // SysLogger.info("û�����κβɼ�ָ��,�����ȫ��###");
                        if (addindilist != null && addindilist.size() > 0) {
                            try {
                                for (int k = 0; k < addindilist.size(); k++) {
                                    // ������,����Ҫ��ӽ�ȥ
                                    NodeGatherIndicators gatherIndicators = (NodeGatherIndicators) addindilist
                                            .get(k);
                                    NodeGatherIndicators nodeGatherIndicators_copy = nodeGatherIndicatorsUtil
                                            .createGatherIndicatorsForNode(gatherIndicators);

                                    nodeGatherIndicators_copy
                                            .setNodeid(nodeids[i]);

                                    savelist.add(nodeGatherIndicators_copy);
                                }
                            } catch (Exception e) {

                            }
                        }
                    }
                }
                if (updatelist != null && updatelist.size() > 0) {
                    try {
                        nodeGatherIndicatorsDao = new NodeGatherIndicatorsDao();
                        nodeGatherIndicatorsDao.updateBatch(updatelist);
                    } catch (Exception e) {

                    } finally {
                        nodeGatherIndicatorsDao.close();
                    }
                }
                if (savelist != null && savelist.size() > 0) {
                    try {
                        nodeGatherIndicatorsDao = new NodeGatherIndicatorsDao();
                        nodeGatherIndicatorsDao.saveBatch(savelist);
                    } catch (Exception e) {

                    } finally {
                        nodeGatherIndicatorsDao.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        NodeGatherIndicatorsUtil gatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
        gatherIndicatorsUtil.refreshShareDataGather();
        return showlist();
    }

    public String multiadd() {

        String[] nodeids = getParaArrayValue("checkbox");
        String type = getParaValue("type");
        String subtype = getParaValue("subtype");
        String nodeid = getParaValue("nodeid");

        // ��Ҫ����Ӧ�õ�ָ��
        String ids = getParaValue("ids");

        List idlist = new ArrayList();// ��������ӽ����ϻ�ȡ�ɼ�ָ��id
        List addindilist = new ArrayList();// ������������ݿ��в�ѯ�Ĳɼ�ָ��
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
        // System.out.println("===="+ids);//

        NodeGatherIndicatorsDao nodeGatherIndicatorsDao = null;

        if (idlist != null && idlist.size() > 0) {
            nodeGatherIndicatorsDao = new NodeGatherIndicatorsDao();
            try {
                for (int i = 0; i < idlist.size(); i++) {
                    String indicatorid = (String) idlist.get(i);
                    NodeGatherIndicators innode = (NodeGatherIndicators) nodeGatherIndicatorsDao
                            .findByID(indicatorid);
                    addindilist.add(innode);
                }
            } catch (Exception e) {

            } finally {
                nodeGatherIndicatorsDao.close();
            }

        }

        NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();

        if (nodeids != null && nodeids.length > 0) {
            List updatelist = new ArrayList();
            List savelist = new ArrayList();
            Hashtable nodeindihash = new Hashtable();
            try {
                List list2 = new ArrayList();
                for (int i = 0; i < nodeids.length; i++) {
                    System.out.println(nodeids[i]);
                    nodeGatherIndicatorsDao = new NodeGatherIndicatorsDao();
                    try {
                        list2 = nodeGatherIndicatorsDao
                                .findByNodeIdAndTypeAndSubtype(nodeids[i],
                                        type, subtype);
                    } catch (Exception e) {

                    } finally {
                        nodeGatherIndicatorsDao.close();
                    }

                    if (list2 != null && list2.size() > 0) {
                        for (int j = 0; j < list2.size(); j++) {
                            NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators) list2
                                    .get(j);
                            nodeindihash.put(nodeGatherIndicators.getName()
                                    + ":" + nodeGatherIndicators.getType()
                                    + ":" + nodeGatherIndicators.getSubtype(),
                                    nodeGatherIndicators);
                        }
                        if (addindilist != null && addindilist.size() > 0) {
                            try {
                                for (int k = 0; k < addindilist.size(); k++) {
                                    NodeGatherIndicators _nodeGatherIndicators = (NodeGatherIndicators) addindilist
                                            .get(k);
                                    if (nodeindihash
                                            .containsKey(_nodeGatherIndicators
                                                    .getName()
                                                    + ":"
                                                    + _nodeGatherIndicators
                                                            .getType()
                                                    + ":"
                                                    + _nodeGatherIndicators
                                                            .getSubtype())) {
                                        // ������,���޸�
                                        NodeGatherIndicators nodeGatherIndicators_update = (NodeGatherIndicators) nodeindihash
                                                .get(_nodeGatherIndicators
                                                        .getName()
                                                        + ":"
                                                        + _nodeGatherIndicators
                                                                .getType()
                                                        + ":"
                                                        + _nodeGatherIndicators
                                                                .getSubtype());
                                        NodeGatherIndicators nodeGatherIndicators_copy = nodeGatherIndicatorsUtil
                                                .createGatherIndicatorsForNode(_nodeGatherIndicators);
                                        nodeGatherIndicators_copy
                                                .setId(nodeGatherIndicators_update
                                                        .getId());
                                        nodeGatherIndicators_copy
                                                .setNodeid(nodeGatherIndicators_update
                                                        .getNodeid());
                                        // System.out.println(nodeGatherIndicators_copy.getNodeid()+"====update==========nodeGatherIndicators_copy.getNodeid()=================="
                                        // );
                                        updatelist
                                                .add(nodeGatherIndicators_copy);
                                    } else {
                                        // ������,����Ҫ��ӽ�ȥ
                                        NodeGatherIndicators nodeGatherIndicators_copy = nodeGatherIndicatorsUtil
                                                .createGatherIndicatorsForNode(_nodeGatherIndicators);

                                        nodeGatherIndicators_copy
                                                .setNodeid(nodeids[i]);

                                        // System.out.println(nodeGatherIndicators_copy.getNodeid()+"====add==========nodeGatherIndicators_copy.getNodeid()=================="
                                        // );
                                        savelist.add(nodeGatherIndicators_copy);
                                    }
                                }
                            } catch (Exception e) {

                            }
                        }
                    } else {
                        // û�����κβɼ�ָ��,�����ȫ��
                        // SysLogger.info("û�����κβɼ�ָ��,�����ȫ��###");
                        if (addindilist != null && addindilist.size() > 0) {
                            try {
                                for (int k = 0; k < addindilist.size(); k++) {
                                    // ������,����Ҫ��ӽ�ȥ
                                    NodeGatherIndicators _nodeGatherIndicators = (NodeGatherIndicators) addindilist
                                            .get(k);
                                    NodeGatherIndicators nodeGatherIndicators_copy = nodeGatherIndicatorsUtil
                                            .createGatherIndicatorsForNode(_nodeGatherIndicators);

                                    nodeGatherIndicators_copy
                                            .setNodeid(nodeids[i]);

                                    // System.out.println(nodeGatherIndicators_copy.getNodeid()+"====add==========nodeGatherIndicators_copy.getNodeid()=================="
                                    // );
                                    savelist.add(nodeGatherIndicators_copy);
                                }
                            } catch (Exception e) {

                            }
                        }
                    }
                }
                if (updatelist != null && updatelist.size() > 0) {
                    try {
                        nodeGatherIndicatorsDao = new NodeGatherIndicatorsDao();
                        nodeGatherIndicatorsDao.updateBatch(updatelist);
                    } catch (Exception e) {

                    } finally {
                        nodeGatherIndicatorsDao.close();
                    }
                }
                if (savelist != null && savelist.size() > 0) {
                    try {
                        nodeGatherIndicatorsDao = new NodeGatherIndicatorsDao();
                        nodeGatherIndicatorsDao.saveBatch(savelist);
                    } catch (Exception e) {

                    } finally {
                        nodeGatherIndicatorsDao.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String jspFlag = getParaValue("jspFlag");
        System.out.println(jspFlag);
        if ("multi".equals(jspFlag)) {
            return showlist();
        }
        // NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new
        // NodeGatherIndicatorsUtil();
        nodeGatherIndicatorsUtil.refreshShareDataGather();
        nodeGatherIndicatorsUtil = null;
        return list();
    }

    public NodeGatherIndicators createGatherIndicators() {
        String nodeid = getParaValue("nodeid");
        String name = getParaValue("name");
        String type = getParaValue("type");
        String subtype = getParaValue("subtype");
        String alias = getParaValue("alias");
        String description = getParaValue("description");
        String category = getParaValue("category");
        String isDefault = getParaValue("isDefault");
        String isCollection = getParaValue("isCollection");
        String poll_interval = getParaValue("poll_interval");
        String classpath = getParaValue("classpath");
        String[] interstr = poll_interval.split("-");

        NodeGatherIndicators nodeGatherIndicators = new NodeGatherIndicators();

        nodeGatherIndicators.setName(name);
        nodeGatherIndicators.setType(type);
        nodeGatherIndicators.setSubtype(subtype);
        nodeGatherIndicators.setAlias(alias);
        nodeGatherIndicators.setDescription(description);
        nodeGatherIndicators.setCategory(category);
        nodeGatherIndicators.setIsDefault(isDefault);
        nodeGatherIndicators.setIsCollection(isCollection);
        nodeGatherIndicators.setNodeid(nodeid);
        nodeGatherIndicators.setPoll_interval(interstr[0]);
        nodeGatherIndicators.setInterval_unit(interstr[1]);
        nodeGatherIndicators.setClasspath(classpath);

        return nodeGatherIndicators;
    }

    public String showlist() {
        String jsp = "/topology/nodegatherindicators/showlist.jsp";
        String nodeid = getParaValue("nodeid");
        String type = getParaValue("type");
        String subtype = getParaValue("subtype");
        
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

        // �ڲ�ѯ������Ҫ�����ͺ������͵��豸
//        List<NodeDTO> nodeDTOlist = nodeGatherIndicatorsUtil.getNodeListByTypeAndSubtype(
//                type, subtype, user.getBusinessids());
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

        // Ϊ�˷�ҳ
        NodeGatherIndicatorsDao nodeGatherIndicatorsDao = new NodeGatherIndicatorsDao();
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
            list(nodeGatherIndicatorsDao, wherestr);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            nodeGatherIndicatorsDao.close();
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

    /**
     * �� �б������
     * 
     * @return
     */
    public String showAdd() {

        String jsp = "/topology/nodegatherindicators/showadd.jsp";

        String nodeid = getParaValue("nodeid");
        String type = getParaValue("type");
        String subtype = getParaValue("subtype");

        String jspFlag = getParaValue("jspFlag");

        // ����Ӧ�õ�
        Hashtable moidhash = new Hashtable();
        List<NodeGatherIndicators> nodeGatherIndicatorsList = null;
        List gatherIndicatorsList = null;
        try {
            if ("multi".equals(jspFlag)) {
                // ����Ӧ��, ʹ���豸��ָ��
                NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();
                nodeGatherIndicatorsList = nodeGatherIndicatorsUtil
                        .getGatherIndicatorsForNode(nodeid, type, subtype);
                if (nodeGatherIndicatorsList != null) {
                    for (int i = 0; i < nodeGatherIndicatorsList.size(); i++) {
                        NodeGatherIndicators nodeGatherIndicators = nodeGatherIndicatorsList
                                .get(i);
                        moidhash.put(nodeGatherIndicators.getName(),
                                nodeGatherIndicators);
                    }
                }
            } else {
                // ���, ʹ��Ĭ�ϵ�ָ��
                GatherIndicatorsUtil gatherIndicatorsUtil = new GatherIndicatorsUtil();
                gatherIndicatorsList = gatherIndicatorsUtil
                        .getGatherIndicatorsByTypeAndSubtype(type, subtype);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setAttribute("moidhash", moidhash);
        request.setAttribute("nodeid", nodeid);
        request.setAttribute("type", type);
        request.setAttribute("subtype", subtype);

        request.setAttribute("jspFlag", jspFlag);

        request.setAttribute("nodeGatherIndicatorsList",
                nodeGatherIndicatorsList);
        request.setAttribute("gatherIndicatorsList", gatherIndicatorsList);
        return jsp;
    }

    public String showChooseNodeList() {

        String jsp = "/topology/nodegatherindicators/showchoosenodelist.jsp";

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
            NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();

            List nodeDTOlist = nodeGatherIndicatorsUtil
                    .getNodeListByTypeAndSubtype(type, subtype, user
                            .getBusinessids());

            List nodeList = new ArrayList();

            if (nodeDTOlist == null) {
                nodeDTOlist = nodeList;
            }

            if ("multi".equals(jspFlag)) {
                // ����Ӧ��ʱ��ȥ��ԭ�����豸
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
            e.printStackTrace();
        }

        return jsp;
    }

    public String showsave() {

        String[] nodeids = getParaArrayValue("checkbox");
        String type = getParaValue("type");
        String subtype = getParaValue("subtype");
        String nodeid = getParaValue("nodeid");

        // ��Ҫ����Ӧ�õ�ָ��
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

        // System.out.println(ids+"=================");

        GatherIndicatorsDao gatherIndicatorsDao = null;

        if (idlist != null && idlist.size() > 0) {
            gatherIndicatorsDao = new GatherIndicatorsDao();
            try {
                for (int i = 0; i < idlist.size(); i++) {
                    String indicatorid = (String) idlist.get(i);
                    GatherIndicators innode = (GatherIndicators) gatherIndicatorsDao.findByID(indicatorid);
                    addindilist.add(innode);
                }
            } catch (Exception e) {
                logger.info("����豸ָ��ʱ��������", e);
            } finally {
                gatherIndicatorsDao.close();
            }

        }

        NodeGatherIndicatorsUtil nodeGatherIndicatorsUtil = new NodeGatherIndicatorsUtil();

        if (nodeids != null && nodeids.length > 0) {
            NodeGatherIndicatorsDao nodeGatherIndicatorsDao = null;
            List updatelist = new ArrayList();
            List savelist = new ArrayList();
            Hashtable nodeindihash = new Hashtable();
            try {
                List list2 = new ArrayList();
                for (int i = 0; i < nodeids.length; i++) {
                    nodeGatherIndicatorsDao = new NodeGatherIndicatorsDao();
                    try {
                        list2 = nodeGatherIndicatorsDao
                                .findByNodeIdAndTypeAndSubtype(nodeids[i],
                                        type, subtype);
                    } catch (Exception e) {
                        logger.info("����豸ָ��ʱ��������", e);
                    } finally {
                        nodeGatherIndicatorsDao.close();
                    }

                    if (list2 != null && list2.size() > 0) {
                        for (int j = 0; j < list2.size(); j++) {
                            NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators) list2
                                    .get(j);
                            nodeindihash.put(nodeGatherIndicators.getName()
                                    + ":" + nodeGatherIndicators.getType()
                                    + ":" + nodeGatherIndicators.getSubtype(),
                                    nodeGatherIndicators);
                        }
                        if (addindilist != null && addindilist.size() > 0) {
                            try {
                                for (int k = 0; k < addindilist.size(); k++) {
                                    GatherIndicators gatherIndicators = (GatherIndicators) addindilist
                                            .get(k);
                                    if (nodeindihash
                                            .containsKey(gatherIndicators
                                                    .getName()
                                                    + ":"
                                                    + gatherIndicators
                                                            .getType()
                                                    + ":"
                                                    + gatherIndicators
                                                            .getSubtype())) {
                                        // ������,���޸�
                                        NodeGatherIndicators nodeGatherIndicators_update = (NodeGatherIndicators) nodeindihash
                                                .get(gatherIndicators.getName()
                                                        + ":"
                                                        + gatherIndicators
                                                                .getType()
                                                        + ":"
                                                        + gatherIndicators
                                                                .getSubtype());
                                        NodeGatherIndicators nodeGatherIndicators_copy = nodeGatherIndicatorsUtil
                                                .createGatherIndicatorsForNode(gatherIndicators);
                                        nodeGatherIndicators_copy
                                                .setId(nodeGatherIndicators_update
                                                        .getId());
                                        nodeGatherIndicators_copy
                                                .setNodeid(nodeGatherIndicators_update
                                                        .getNodeid());
                                        updatelist
                                                .add(nodeGatherIndicators_copy);
                                    } else {
                                        // ������,����Ҫ��ӽ�ȥ
                                        NodeGatherIndicators nodeGatherIndicators_copy = nodeGatherIndicatorsUtil
                                                .createGatherIndicatorsForNode(gatherIndicators);

                                        nodeGatherIndicators_copy
                                                .setNodeid(nodeids[i]);

                                        savelist.add(nodeGatherIndicators_copy);
                                    }
                                }
                            } catch (Exception e) {

                            }
                        }
                    } else {
                        // û�����κβɼ�ָ��,�����ȫ��
                        // SysLogger.info("û�����κβɼ�ָ��,�����ȫ��###");
                        if (addindilist != null && addindilist.size() > 0) {
                            try {
                                for (int k = 0; k < addindilist.size(); k++) {
                                    // ������,����Ҫ��ӽ�ȥ
                                    GatherIndicators gatherIndicators = (GatherIndicators) addindilist
                                            .get(k);
                                    NodeGatherIndicators nodeGatherIndicators_copy = nodeGatherIndicatorsUtil
                                            .createGatherIndicatorsForNode(gatherIndicators);

                                    nodeGatherIndicators_copy
                                            .setNodeid(nodeids[i]);

                                    savelist.add(nodeGatherIndicators_copy);
                                }
                            } catch (Exception e) {

                            }
                        }
                    }
                }
                if (updatelist != null && updatelist.size() > 0) {
                    try {
                        nodeGatherIndicatorsDao = new NodeGatherIndicatorsDao();
                        nodeGatherIndicatorsDao.updateBatch(updatelist);
                    } catch (Exception e) {

                    } finally {
                        nodeGatherIndicatorsDao.close();
                    }
                }
                if (savelist != null && savelist.size() > 0) {
                    try {
                        nodeGatherIndicatorsDao = new NodeGatherIndicatorsDao();
                        nodeGatherIndicatorsDao.saveBatch(savelist);
                    } catch (Exception e) {

                    } finally {
                        nodeGatherIndicatorsDao.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return showlist();
    }

    public String showEdit() {
        String jsp = "/topology/nodegatherindicators/showedit.jsp";

        String nodeid = getParaValue("nodeid");
        String type = getParaValue("type");
        String subtype = getParaValue("subtype");
        String id = getParaValue("id");
        NodeGatherIndicators nodeGatherIndicators = null;
        NodeGatherIndicatorsDao gatherIndicatorsDao = new NodeGatherIndicatorsDao();
        try {
            nodeGatherIndicators = (NodeGatherIndicators) gatherIndicatorsDao
                    .findByID(id);
        } catch (RuntimeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            gatherIndicatorsDao.close();
        }

        if (nodeGatherIndicators != null) {
            nodeid = nodeGatherIndicators.getNodeid();
            type = nodeGatherIndicators.getType();
            subtype = nodeGatherIndicators.getSubtype();
        }

        request.setAttribute("nodeGatherIndicators", nodeGatherIndicators);
        request.setAttribute("nodeid", nodeid);
        request.setAttribute("type", type);
        request.setAttribute("subtype", subtype);
        return jsp;
    }

    public String showUpdate() {
        NodeGatherIndicators nodeGatherIndicators = createGatherIndicators();
        int id = getParaIntValue("id");
        nodeGatherIndicators.setId(id);
        NodeGatherIndicatorsDao nodeGatherIndicatorsDao = new NodeGatherIndicatorsDao();
        try {
            nodeGatherIndicatorsDao.update(nodeGatherIndicators);
        } catch (RuntimeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            nodeGatherIndicatorsDao.close();
        }
        return showlist();
    }

    public String showDelete() {
        String[] ids = getParaArrayValue("checkbox");
        NodeGatherIndicatorsDao nodeGatherIndicatorsDao = new NodeGatherIndicatorsDao();
        try {
            nodeGatherIndicatorsDao.delete(ids);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            nodeGatherIndicatorsDao.close();
        }
        return showlist();
    }

}
