package com.afunms.config.manage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.application.dao.DBDao;
import com.afunms.application.model.OracleEntity;
import com.afunms.application.util.IpTranslation;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.ManagerInterface;
import com.afunms.config.dao.OracleTableSpaceDao;
import com.afunms.config.model.OracleTableSpaceConfig;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.Constant;
import com.afunms.indicators.util.NodeUtil;

public class OracleTableSpaceManager extends BaseManager implements
        ManagerInterface {
    /**
     * execute: 处理请求
     *
     * @param   action
     *          - 请求
     * @return  {@link String}
     *          - 返回处理后的页面
     *
     * @since   v1.01
     * @see com.afunms.common.base.ManagerInterface#execute(java.lang.String)
     */
    public String execute(String action) {
		if("list".equals(action)){
			return list();
		} else if ("empty".equals(action)) {
		    return empty();
		} else if ("refresh".equals(action)) {
		    return refresh();
		} else if("delete".equals(action)){
			return delete();
		} if("showedit".endsWith(action)){
			return readyEdit();
		} if("update".equals(action)){
			return update();
		}
		return null;
	}

    /**
     * list:
     * <p>处理列表
     *
     * @return  {@link String}
     *          - 列表页面
     *
     * @since   v1.01
     */
    private String list() {
        NodeUtil nodeUtil = new NodeUtil();
        nodeUtil.setBid(getBid());
        List<BaseVo> oracleList = nodeUtil.getNodeByTyeAndSubtype(Constant.TYPE_DB, Constant.TYPE_DB_SUBTYPE_ORACLE);
        List<NodeDTO> nodeList = nodeUtil.conversionToNodeDTO(oracleList);
        String nodeid = getParaValue("nodeid");
        if (nodeid == null || nodeid.trim().length() == 0) {
            if (nodeList != null && nodeList.size() > 0) {
                nodeid = nodeList.get(0).getNodeid();
            }
        }
        List<OracleTableSpaceConfig> list = null;
        if (nodeid != null && nodeid.trim().length() > 0) {
            OracleTableSpaceDao dao = new OracleTableSpaceDao();
            try {
                list = dao.findByNodeId(nodeid);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dao.close();
            }
        }
        if (list == null) {
            list = new ArrayList<OracleTableSpaceConfig>();
        }
        System.out.println(list.size());
        request.setAttribute("list", list);
        request.setAttribute("nodeid", nodeid);
        request.setAttribute("nodeList", nodeList);
        return "/config/oratableconfig/list.jsp";
    }

    /**
     * empty:
     * <p>处理清空
     * 
     * @return  {@link String}
     *          - 返回列表页面
     * 
     * @since   v1.01
     */
    private String empty() {
        OracleTableSpaceDao dao = new OracleTableSpaceDao();
        try {
            dao.empty();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return list();
    }

    /**
     * refresh:
     * <p>处理刷新
     *
     * @return  {@link String}
     *          - 处理刷新后返回列表页面
     *
     * @since   v1.01
     */
    private String refresh() {
        String nodeid = getParaValue("nodeid");
        NodeUtil nodeUtil = new NodeUtil();
        System.out.println("=========" + nodeid);
        List<BaseVo> oracleList = nodeUtil.getNodeByTyeAndSubtype(Constant.TYPE_DB, Constant.TYPE_DB_SUBTYPE_ORACLE);
        List<OracleTableSpaceConfig> list = new ArrayList<OracleTableSpaceConfig>();
        if (nodeid != null) {
            for (BaseVo vo : oracleList) {
                OracleEntity entity = (OracleEntity) vo;
                if (entity.getId() == Integer.valueOf(nodeid)) {
                    NodeDTO node = nodeUtil.conversionToNodeDTO(entity);
                    list.addAll(getOracleTableSpaceConfigFromGather(entity, node));
                }
            }
        } else {
            for (BaseVo vo : oracleList) {
                OracleEntity entity = (OracleEntity) vo;
                NodeDTO node = nodeUtil.conversionToNodeDTO(entity);
                list.addAll(getOracleTableSpaceConfigFromGather(entity, node));
            }
        }
        OracleTableSpaceDao dao = new OracleTableSpaceDao();
        try {
            dao.save(list);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return list();
    }

    private List<OracleTableSpaceConfig> getOracleTableSpaceConfigFromGather(OracleEntity entity, NodeDTO node) {
        String hex = IpTranslation.formIpToHex(node.getIpaddress());
        String serverip = hex + ":" + node.getNodeid();
        Vector<Hashtable<String, String>> tableinfo_v = null;
        DBDao dao = new DBDao();
        try {
            tableinfo_v = dao.getOracle_nmsoraspaces(serverip);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        List<OracleTableSpaceConfig> list = new ArrayList<OracleTableSpaceConfig>();
        if (tableinfo_v != null) {
            System.out.println(tableinfo_v.size());
            List<OracleTableSpaceConfig> newList = new ArrayList<OracleTableSpaceConfig>();
            for (Hashtable<String, String> hashtable : tableinfo_v) {
                String file_name = hashtable.get("file_name");
                String tablespace = hashtable.get("tablespace");
                String status = hashtable.get("status");
                String chunks = hashtable.get("chunks");
                
                OracleTableSpaceConfig config = new OracleTableSpaceConfig();
                config.setNodeid(node.getNodeid());
                config.setIpaddress(node.getIpaddress()); 
                config.setDbType(node.getSubtype());
                config.setDbName(entity.getSid());
                config.setLinkuse("");
                config.setTableSpace(tablespace);
                config.setFileName(file_name);
                config.setTableIndex(0);
                config.setBak("利用率阀值");
                config.setReportflag(1);
                config.setLimenvalue(85);
                config.setLimenvalue1(90);
                config.setLimenvalue2(95);
                config.setSms(1);
                config.setSms1(1);
                config.setSms2(1);
                config.setSms3(1);
                config.setMonflag(1);
                newList.add(config);
            }
            List<OracleTableSpaceConfig> oldList = null;
            OracleTableSpaceDao oracleTableSpaceDao = new OracleTableSpaceDao();
            try {
                oldList = oracleTableSpaceDao.findByNodeId(node.getNodeid());
            } catch (RuntimeException e) {
                e.printStackTrace();
            } finally {
                oracleTableSpaceDao.close();
            }
            Hashtable<String, OracleTableSpaceConfig> oldHashtable = new Hashtable<String, OracleTableSpaceConfig>();
            if (oldList != null && oldList.size() > 0) {
                for (OracleTableSpaceConfig oracleTableSpaceConfig : oldList) {
                    oldHashtable.put(oracleTableSpaceConfig.getTableSpace(), oracleTableSpaceConfig);
                }
            }
            for (OracleTableSpaceConfig oracleTableSpaceConfig : newList) {
                if (oldHashtable.containsKey(oracleTableSpaceConfig.getTableSpace())) {
                    list.add(oldHashtable.get(oracleTableSpaceConfig.getTableSpace()));
                } else {
                    list.add(oracleTableSpaceConfig);
                }
            }
            oracleTableSpaceDao = new OracleTableSpaceDao();
            try {
                oracleTableSpaceDao.delete(node.getNodeid());
            } catch (RuntimeException e) {
                e.printStackTrace();
            } finally {
                oracleTableSpaceDao.close();
            }
        }
        return list;
    }
    /**
     * 准备编辑
     * 
     * @return
     */
    private String readyEdit() {
        String id = getParaValue("id");
        OracleTableSpaceConfig vo = null;
        OracleTableSpaceDao dao = new OracleTableSpaceDao();
        try {
            vo = (OracleTableSpaceConfig) dao.findByID(id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        request.setAttribute("vo", vo);
        return "/config/oratableconfig/edit.jsp";
    }

    /**
     * 更新
     * 
     * @return
     */
    private String update() {
        OracleTableSpaceConfig vo = null;
        String id = getParaValue("id");
        OracleTableSpaceDao dao = new OracleTableSpaceDao();
        try {
            vo = (OracleTableSpaceConfig) dao.findByID(id);
        } catch (RuntimeException e1) {
            e1.printStackTrace();
        } finally {
            dao.close();
        }

        int monflag = getParaIntValue("monflag");
        int limenvalue = getParaIntValue("limenvalue");
        int sms = getParaIntValue("sms");
        int limenvalue1 = getParaIntValue("limenvalue1");
        int sms1 = getParaIntValue("sms1");
        int limenvalue2 = getParaIntValue("limenvalue2");
        int sms2 = getParaIntValue("sms2");
        int reportflag = getParaIntValue("reportflag");
        vo.setMonflag(monflag);
        vo.setLimenvalue(limenvalue);
        vo.setLimenvalue1(limenvalue1);
        vo.setSms1(sms1);
        vo.setLimenvalue2(limenvalue2);
        vo.setSms2(sms2);

        String linkuse = getParaValue("linkuse");
        vo.setLinkuse(linkuse);
        if (sms > -1)
            vo.setSms(sms);
        if (reportflag > -1)
            vo.setReportflag(reportflag);

        dao = new OracleTableSpaceDao();
        try {
            dao.update(vo);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return list();
    }

    /**
     * delete:
     * <p>处理删除
     *
     * @return  {@link String}
     *          - 处理删除后返回列表页面 
     *
     * @since   v1.01
     */
    private String delete() {
        String[] id = getParaArrayValue("checkbox");
        System.out.println(id);
        if (id != null && id.length > 0) {
            OracleTableSpaceDao dao = new OracleTableSpaceDao();
            try {
                dao.delete(id);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dao.close();
            }
        }
        return list();
    }
}
