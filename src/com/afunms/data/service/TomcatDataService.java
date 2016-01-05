/*
 * @(#)TomcatDataService.java     v1.01, Jan 9, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.data.service;

import com.afunms.alarm.service.NodeAlarmService;
import com.afunms.application.dao.TomcatDao;
import com.afunms.application.model.Tomcat;
import com.afunms.common.base.BaseVo;
import com.afunms.common.util.CommonUtil;
import com.afunms.common.util.CreateMetersPic;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.initialize.ResourceCenter;
import com.afunms.temp.dao.TomcatTempDao;
import com.afunms.temp.model.TomcatNodeTemp;

/**
 * ClassName:   TomcatDataService.java
 * <p> {@link TomcatDataService} 用于提供 {@link Tomcat} 详细信息页面中的信息
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        Jan 9, 2013 2:47:42 PM
 */
public class TomcatDataService extends NodeDataService {

    private static CreateMetersPic cmp = new CreateMetersPic();

    private static final String JVM_IMAGE_PATH = "resource/image/jfreechart/reportimg/";

    private static final String JVM_IMAGE_PATH_NAME = "tomcat_jvm";

    private static final String JVM_IMAGE_BG_PATH = ResourceCenter.getInstance().getSysPath()
                                                + "resource\\image\\dashBoard1.png";

    private static final String JVM_IMAGE_TITLE = "JVM利用率";

    /**
     * 
     */
    public TomcatDataService() {
        super();
    }

    /**
     * @param baseVo
     */
    public TomcatDataService(BaseVo baseVo) {
        super(baseVo);
    }

    /**
     * @param nodeDTO
     */
    public TomcatDataService(NodeDTO nodeDTO) {
        super(nodeDTO);
    }

    /**
     * @param nodeid
     * @param type
     * @param subtype
     */
    public TomcatDataService(String nodeid, String type, String subtype) {
        super(nodeid, type, subtype);
    }

    /**
     * 重写父类的方法用于提高性能
     * 
     * @see com.afunms.data.service.NodeDataService#init(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public void init(String nodeid, String type, String subtype) {
        BaseVo baseVo = null;
        TomcatDao tomcatDao = new TomcatDao();
        try {
            baseVo = tomcatDao.findByID(nodeid);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            tomcatDao.close();
        }
        super.init(baseVo);
    }

    /**
     * getMaxAlarmLevel:
     * <p>获取最大告警等级
     *
     * @return  {@link String}
     *          - 最大告警等级
     *
     * @since   v1.01
     */
    public String getMaxAlarmLevel() {
        NodeAlarmService nodeAlarmService = new NodeAlarmService();
        int maxAlarmLevel = nodeAlarmService.getMaxAlarmLevel(nodeDTO);
        return String.valueOf(maxAlarmLevel);
    }

    /**
     * getTomcatNodeTemp:
     * <p>获取 Tomcat 当前信息
     *
     * @return  {@link TomcatNodeTemp}
     *          - Tomcat 当前信息
     *
     * @since   v1.01
     */
    public TomcatNodeTemp getTomcatNodeTemp() {
        TomcatNodeTemp nodeTemp = null;
        TomcatTempDao dao = new TomcatTempDao();
        try {
            nodeTemp = dao.findByNodeid(getNodeid());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        if (nodeTemp == null) {
            nodeTemp = new TomcatNodeTemp();
        }
        return nodeTemp;
    }

    
    public String getCurJVMImg(String curJVMUtilization) {
        if (curJVMUtilization == null) {
            curJVMUtilization = "0";
        }
        String bgImagePath = JVM_IMAGE_BG_PATH;
        String title = JVM_IMAGE_TITLE;
        String name = JVM_IMAGE_PATH_NAME;
        cmp.createChartByParam(getNodeid(), curJVMUtilization, bgImagePath, title, name);
        String pingImageInfo = JVM_IMAGE_PATH + getNodeid() + name + ".png";
        return pingImageInfo;
    }

}

