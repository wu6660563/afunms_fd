/*
 * @(#)URLManager.java     v1.01, Mar 5, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.application.manage;

import java.util.ArrayList;
import java.util.List;

import com.afunms.application.dao.URLConfigDao;
import com.afunms.application.model.URLConfig;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ManagerInterface;
import com.afunms.config.dao.BusinessDao;
import com.afunms.config.dao.SupperDao;
import com.afunms.config.model.Business;
import com.afunms.config.model.Supper;
import com.afunms.node.service.NodeService;
import com.afunms.topology.util.KeyGenerator;

/**
 * ClassName:   URLManager.java
 * <p>{@link URLManager} URL 服务逻辑控制类
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        Mar 5, 2013 10:45:05 PM
 */
public class URLManager extends BaseManager implements ManagerInterface {

    /**
     * execute:
     *
     * @param action
     * @return
     *
     * @since   v1.01
     * @see com.afunms.common.base.ManagerInterface#execute(java.lang.String)
     */
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
        }
        return null;
    }

    /**
     * list:
     * <p>处理 {@link Url} 列表请求
     *
     * @return  {@link String}
     *          - 列表页面
     *
     * @since   v1.01
     */
    private String list() {
        String bidSql = getBidSql("bid");
        URLConfigDao dao = new URLConfigDao();
        try {
            if (bidSql == null) {
                // 如果为 null 则为超级用户，不需要检查 bid
                list(dao);
            } else if (bidSql.trim().length() > 0) {
                list(dao, "where " + bidSql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return "/application/url/list.jsp";
    }

    /**
     * add:
     * <p>处理添加 {@link URLConfig} 请求
     *
     * @return  {@link String}
     *          - 添加页面
     *
     * @since   v1.01
     */
    private String add() {
        SupperDao supperdao = new SupperDao();
        List<Supper> supperList = null;
        try {
            supperList = (List<Supper>) supperdao.loadAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            supperdao.close();
        }
        if (supperList == null) {
            supperList = new ArrayList<Supper>();
        }
        request.setAttribute("supperList", supperList);
        return "/application/url/add.jsp";
    }

    /**
     * add:
     * <p>处理保存 {@link URLConfig} 请求
     *
     * @return  {@link String}
     *          - 列表页面
     *
     * @since   v1.01
     */
    private String save() {
        URLConfig config = createURLConfig();
        config.setId(KeyGenerator.getInstance().getNextKey());
        URLConfigDao dao = new URLConfigDao();
        try {
            dao.save(config);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        NodeService service = new NodeService();
        service.addNode(config);
        return list();
    }

    /**
     * edit:
     * <p>处理编辑 {@link URLConfig} 请求
     *
     * @return  {@link String}
     *          - 编辑页面
     *
     * @since   v1.01
     */
    public String edit() {
        String jsp = "/application/url/edit.jsp";
        URLConfigDao dao = new URLConfigDao();
        try {
            setTarget(jsp);
            jsp = readyEdit(dao);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        SupperDao supperdao = new SupperDao();
        List<Supper> supperList = null;
        try {
            supperList =  (List<Supper>) supperdao.loadAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            supperdao.close();
        }
        if (supperList == null) {
            supperList = new ArrayList<Supper>();
        }
        URLConfig vo = (URLConfig) request.getAttribute("vo");
        String bidNames = getBidNames(vo.getBid());
        request.setAttribute("bidNames", bidNames);
        return jsp; 
    }

    /**
     * update:
     * <p>
     *
     * @return
     *
     * @since   v1.01
     */
    private String update() {
        URLConfig config = createURLConfig();
        config.setId(getParaIntValue("id"));
        URLConfigDao dao = new URLConfigDao();
        try {
            dao.update(config);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return list();
    }

    /**
     * delete:
     * <p>处理删除 {@link URLConfig} 请求
     *
     * @return  {@link String}
     *          - 列表页面
     *
     * @since   v1.01
     */
    public String delete() {
        String[] ids = getParaArrayValue("checkbox");
        if (ids != null && ids.length > 0) {
            NodeService service = new NodeService();
            URLConfigDao dao = new URLConfigDao();
            try {
                for (String id : ids) {
                    URLConfig config = null;
                    config = (URLConfig) dao.findByID(id);
                    service.deleteNode(config);
                }
                dao.delete(ids);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dao.close();
            }
        }
        return list();
    }

    private URLConfig createURLConfig() {
        URLConfig config = new URLConfig();
        config.setName(getParaValue("name"));
        config.setUrl(getParaValue("url"));
        config.setTimeout(getParaIntValue("timeout"));
        config.setMonFlag(getParaIntValue("monFlag"));
        config.setPageSize(getParaIntValue("pageSize"));
        config.setIpaddress(getParaValue("ipaddress"));
        config.setSupperid(getParaIntValue("supperid"));
        config.setBid(getParaValue("bid"));
        return config;
    }
}

