package com.gathertask.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;

import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.node.model.NodeDomain;
import com.afunms.node.service.NodeDomainService;
import com.database.DBManager;
import com.database.config.SystemConfig;

public class Taskdao {

    Logger logger = Logger.getLogger(Taskdao.class);

    /**
     * ��ȡ��Ҫ�ɼ��Ĳɼ�ָ�� ��agentid ��idΪ��Ȼ����ʱ�򣬾�������agent��ģʽ�ɼ��� ��agent��idΪ-1 ��ʱ��
     * 
     * @return �ɼ��������б�
     */
    public Hashtable GetRunTaskList() {

        String sql = "select b.* from topo_host_node a ,nms_gather_indicators_node b where a.id=b.nodeid and a.managed=1 and b.classpath like 'com%'";

        // Agentģʽ
        int agentid = -1;
        String Systemtype = SystemConfig.getConfigInfomation("Agentconfig",
                        "Systemtype");

        if (Systemtype.trim().equals("agent")) {// agent �ɼ�����
            try {
                agentid = Integer.parseInt(SystemConfig.getConfigInfomation(
                                "Agentconfig", "AGENTID"));

            } catch (Exception e) {
                // agentid=-1;
            }
            sql = "select b.* from topo_host_node a ,nms_gather_indicators_node b ,nms_node_agent c where a.id=b.nodeid and a.managed=1 and b.classpath like 'com%' and c.nodeid=b.nodeid and c.agentid='"
                            + agentid + "'";
        } else if (Systemtype.trim().equals("standalone")) {// standalone �����汾

            sql = "select b.* from topo_host_node a ,nms_gather_indicators_node b where a.id=b.nodeid and a.managed=1 and b.classpath like 'com%'";
        } else if (Systemtype.trim().equals("webserver")) {// webserver
                                                            // �������������ɼ�

            sql = "";
        }

        sql = "select * from nms_gather_indicators_node b where b.classpath like 'com%'";
        DBManager manager = null;
        List<NodeGatherIndicators> list = new ArrayList<NodeGatherIndicators>();
        Hashtable<String, NodeGatherIndicators> hashtable = new Hashtable<String, NodeGatherIndicators>();
        if (sql.trim().length() > 0) {
            try {
                manager = new DBManager();

                ResultSet rs = manager.executeQuery(sql);
                // list=manager.executeQuerykeyoneListHashMap(sql, "id");
                while (rs.next()) {
                    NodeGatherIndicators nodeGatherIndicators = new NodeGatherIndicators();
                    nodeGatherIndicators.setId(rs.getInt("id"));
                    nodeGatherIndicators.setNodeid(rs.getString("nodeid"));
                    nodeGatherIndicators.setName(rs.getString("name"));
                    nodeGatherIndicators.setType(rs.getString("type"));
                    nodeGatherIndicators.setSubtype(rs.getString("subtype"));
                    nodeGatherIndicators.setAlias(rs.getString("alias"));
                    nodeGatherIndicators.setDescription(rs
                                    .getString("description"));
                    nodeGatherIndicators.setCategory(rs.getString("category"));
                    nodeGatherIndicators
                                    .setIsDefault(rs.getString("isDefault"));
                    nodeGatherIndicators.setIsCollection(rs
                                    .getString("isCollection"));
                    nodeGatherIndicators.setPoll_interval(rs
                                    .getString("poll_interval"));
                    nodeGatherIndicators.setInterval_unit(rs
                                    .getString("interval_unit"));
                    nodeGatherIndicators
                                    .setClasspath(rs.getString("classpath"));
                    list.add(nodeGatherIndicators);
                    hashtable.put(String.valueOf(nodeGatherIndicators.getId()), nodeGatherIndicators);
                }

            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e);
            } finally {

                if (manager != null)
                    manager.close();
            }
        }
        // logger.info(list.toString());

        NodeDomainService nodeDomainService = new NodeDomainService();
        List<NodeDomain> nodeDomainList = nodeDomainService.getAllNodeDomain();
        Hashtable<String, NodeDomain> nodeDomainHashtable = new Hashtable<String, NodeDomain>();
        if (nodeDomainList != null) {
            for (NodeDomain nodeDomain : nodeDomainList) {
                nodeDomainHashtable.put(nodeDomain.getNodeId() + ":"
                                + nodeDomain.getNodeType() + ":"
                                + nodeDomain.getNodeSubtype(), nodeDomain);
            }
        }
        
        
        for (NodeGatherIndicators nodeGatherIndicators : list) {
            String key = nodeGatherIndicators.getNodeid() + ":" + nodeGatherIndicators.getType() + ":" + nodeGatherIndicators.getSubtype();
            NodeDomain nodeDomain = nodeDomainHashtable.get(key);
            if (nodeDomain != null) {
                if (Integer.valueOf(nodeDomain.getDomain()) != NodeDomainService.DEFAULT_DOMAIN.getId()) {
                    hashtable.remove(String.valueOf(nodeGatherIndicators.getId()));
                }
            } 
        }
        return hashtable;
    }

    public static void main(String[] arg) {

        Taskdao dao = new Taskdao();
        Hashtable table = new Hashtable();
        table = dao.GetRunTaskList();
        dao.logger.info(table);

    }

}
