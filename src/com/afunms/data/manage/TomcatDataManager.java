/*
 * @(#)TomcatDataManager.java     v1.01, Jan 8, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.data.manage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.afunms.application.model.Tomcat;
import com.afunms.common.util.SessionConstant;
import com.afunms.data.service.TomcatDataService;
import com.afunms.event.dao.EventListDao;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.system.model.User;
import com.afunms.temp.model.TomcatNodeTemp;
import com.ibm.db2.jcc.b.ac;

/**
 * ClassName:   TomcatDataManager.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        Jan 8, 2013 4:29:09 PM
 */
public class TomcatDataManager extends NodeDataManager {

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
        if ("getPerformanceInfo".equals(action)) {
            return getPerformanceInfo();
        } else if ("getEventInfo".equals(action)) {
            return getEventInfo();
        }
        return null;
    }

    /**
     * getPerformanceInfo:
     * <p>获取性能信息
     *
     * @return
     *
     * @since   v1.01
     */
    public String getPerformanceInfo() {
        getBaseInfo();
        return "/detail/middleware/tomcat/performance.jsp";
    }

    public void getBaseInfo() {
        TomcatDataService dataService = getDataService();
        
        NodeDTO node = dataService.getNodeDTO();
        Tomcat tomcat = (Tomcat) dataService.getBaseVo();
        TomcatNodeTemp tomcatNodeTemp = dataService.getTomcatNodeTemp();
        String maxAlarmLevel = dataService.getMaxAlarmLevel();
        
        String curJVMImg = dataService.getCurJVMImg(tomcatNodeTemp.getMemoryUtilization());

        tomcat.setVersion(tomcatNodeTemp.getTomcatVersion());
        tomcat.setOs(tomcatNodeTemp.getOSName());
        tomcat.setOsversion(tomcatNodeTemp.getOSVersion());
        tomcat.setJvmversion(tomcatNodeTemp.getJVMVersion());
        tomcat.setJvmvender(tomcatNodeTemp.getJVMVendor());
        
        request.setAttribute("node", node);
        request.setAttribute("vo", tomcat);
        request.setAttribute("tomcatNodeTemp", tomcatNodeTemp);
        request.setAttribute("maxAlarmLevel", maxAlarmLevel);
        request.setAttribute("curJVMImg", curJVMImg);
    }

    public String getEventInfo() {
        getBaseInfo();
        String nodeid = getNodeid();
        String type = getType();
        String subtype = getSubtype();
        int status = getParaIntValue("status");
        int level1 = getParaIntValue("level1");
        if (status == -1) {
            status = 99;
        }
        if (level1 == -1) {
            level1 = 99;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startdate = request.getParameter("startdate");
        if (startdate == null) {
            startdate = sdf.format(new Date());
        }
        String todate = request.getParameter("todate");
        if (todate == null) {
            todate = sdf.format(new Date());
        }
        String starttime = startdate + " 00:00:00";
        String totime = todate + " 23:59:59";
        String bid = ((User) session.getAttribute(SessionConstant.CURRENT_USER))
                .getBusinessids();
        List list = null;
        EventListDao dao = new EventListDao();
        try {
            list = dao.getQuery(starttime, totime, status + "", level1 + "",
                    bid, Integer.valueOf(nodeid));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        request.setAttribute("status", status);
        request.setAttribute("level1", level1);
        request.setAttribute("list", list);
        request.setAttribute("startdate", startdate);
        request.setAttribute("todate", todate);
        return "/detail/middleware/tomcat/event.jsp";
    }

    /**
     * getDataService:
     * <p>获取数据服务类
     *
     * @return  {@link TomcatDataService}
     *          - Tomcat 数据库服务了
     *
     * @since   v1.01
     */
    public TomcatDataService getDataService() {
        String nodeid = getNodeid();
        String type = getType();
        String subtype = getSubtype();
        TomcatDataService dataService = new TomcatDataService(nodeid,
                type, subtype);
        return dataService;
    }
}

