/**
 * <p>Description:Tomcat Manager</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-12-07
 */

package com.afunms.application.manage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;


import com.afunms.application.dao.TomcatDao;
import com.afunms.application.model.Tomcat;
import com.afunms.application.tomcatmonitor.TomcatInfo;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.CEIString;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.config.dao.BusinessDao;
import com.afunms.config.dao.SupperDao;
import com.afunms.config.model.Business;
import com.afunms.config.model.Supper;
import com.afunms.detail.service.tomcatInfo.TomcatInfoService;
import com.afunms.node.service.NodeService;
import com.afunms.topology.util.KeyGenerator;

public class TomcatManager extends BaseManager implements ManagerInterface {

    /**
     * list:
     * <p>处理 {@link Tomcat} 列表请求
     *
     * @return  {@link String}
     *          - 列表页面
     *
     * @since   v1.01
     */
    private String list() {
        String bidSql = getBidSql("bid");
        TomcatDao dao = new TomcatDao();
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
        return "/application/tomcat/list.jsp";
    }

    /**
     * add:
     * <p>处理添加 {@link Tomcat} 请求
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
        return "/application/tomcat/add.jsp";
    }

    /**
     * save:
     * <p>处理保存 {@link Tomcat} 请求
     *
     * @return  {@link String}
     *          - 返回保存后的列表页面
     *
     * @since   v1.01
     */
    private String save() {
        Tomcat tomcat = createTomcat();
        tomcat.setId(KeyGenerator.getInstance().getNextKey());

        TomcatDao dao = new TomcatDao();
        try {
            boolean result = dao.save(tomcat);
            if (result) {
                NodeService service = new NodeService();
                service.addNode(tomcat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return list();
    }

    /**
     * delete:
     * <p>处理删除 {@link Tomcat} 请求
     *
     * @return  {@link String}
     *          - 删除后的列表页面
     *
     * @since   v1.01
     */
    public String delete() {
        String[] ids = getParaArrayValue("checkbox");
        TomcatDao dao = new TomcatDao();
        try {
            if (ids != null && ids.length > 0) {
                NodeService nodeService = new NodeService();
                for (String id : ids) {
                    Tomcat node = (Tomcat) dao.findByID(id);
                    if (node != null) {
                        nodeService.deleteNode(node);
                    }
                }
                dao.delete(ids);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return list();
    }

    /**
     * edit:
     * <p>处理编辑 {@link Tomcat} 请求
     *
     * @return  {@link String}
     *          - 编辑页面
     *
     * @since   v1.01
     */
    private String edit() {
        String jsp = "/application/tomcat/edit.jsp";
        TomcatDao dao = new TomcatDao();
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
        Tomcat vo = (Tomcat) request.getAttribute("vo");
        BusinessDao bussdao = new BusinessDao();
        List<Business> allbuss = null;
        try {
           allbuss = (List<Business>) bussdao.loadAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bussdao.close();
        }
        String bid = vo.getBid();
        if (bid == null) {
            bid = "";
        }
        String id[] = bid.split(",");
        List bidlist = new ArrayList();
        if (id != null && id.length > 0) {
            for (int i = 0; i < id.length; i++) {
                bidlist.add(id[i]);
            }
        }
        request.setAttribute("supperList", supperList);
        request.setAttribute("bidlist", bidlist);
        request.setAttribute("allbuss", allbuss);
        return jsp;
    }

    /**
     * update:
     * <p>处理更新 {@link Tomcat} 请求
     *
     * @return  {@link String}
     *          - 返回更新后的列表页面
     *
     * @since   v1.01
     */
    private String update() {
        Tomcat vo = createTomcat();
        vo.setId(getParaIntValue("id"));
        TomcatDao dao = new TomcatDao();
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
     * createTomcat:
     * <p>根据页面内容创建一个新的 {@link Tomcat} 实例
     *
     * @return  {@link Tomcat}
     *          - {@link Tomcat} 实例
     *
     * @since   v1.01
     */
    public Tomcat createTomcat() {
        Tomcat vo = new Tomcat();
        vo.setUser(getParaValue("user"));
        vo.setPassword(getParaValue("password"));
        vo.setName(getParaValue("name"));
        vo.setIpAddress(getParaValue("ip_address"));
        vo.setPort(getParaValue("port"));
        vo.setMonflag(getParaIntValue("monflag"));
        vo.setSupperid(getParaIntValue("supperid"));// snow add 2010-5-20
        vo.setVersion("");
        vo.setJvmversion("");
        vo.setJvmvender("");
        vo.setOs("");
        vo.setOsversion("");
        vo.setBid(getParaValue("bid"));
        return vo;
    }

    public Hashtable getCategory(String ip, String category, String subentity,
            String starttime, String endtime) {
        return getCategory(ip, category, subentity, starttime, endtime, "");
    }
    
    public Hashtable getCategory(String ip, String category, String subentity,
            String starttime, String endtime, String time) {
        if ("TomcatPing".equals(category)) {
            TomcatInfoService infoService = new TomcatInfoService();
            return infoService.getPingInfo(ip, starttime, endtime);
        } else if ("tomcat_jvm".equals(category)) {
            TomcatInfoService infoService = new TomcatInfoService();
            return infoService.getJVMMemoryInfo(ip, starttime, endtime);
        }
        return null;
    }

    public String execute(String action) {
        if ("list".equals(action)) {
            return list();
        } else if ("add".equals(action)) {
            return add();
        } else if ("save".equals(action)) {
            return save();
        } else if ("delete".equals(action)) {
            return delete();
        } else if ("edit".equals(action)) {
            return edit();
        } else if ("update".equals(action)) {
            return update();
        }
        setErrorCode(ErrorMessage.ACTION_NO_FOUND);
        return null;
    }
}