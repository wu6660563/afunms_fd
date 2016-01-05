/*
 * @(#)FileSendService.java     v1.01, 2013 12 23
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.synchronize.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import com.afunms.common.util.SerializeUtil;
import com.afunms.common.util.SysLogger;
import com.afunms.initialize.ResourceCenter;
import com.afunms.rmi.service.RMIParameter;

/**
 * ClassName:   FileSendService.java
 * <p>文件发送服务
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 12 23 10:23:49
 */
public class FileSendService extends BaseSendService {

    /**
     * sdf:
     * <p>日期转换
     *
     * @since   v1.01
     */
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); 

    private final static SysLogger logger = SysLogger.getLogger(FileSendService.class);
    /**
     * path:
     * <p>路径
     *
     * @since   v1.01
     */
    private static String SYS_PATH = ResourceCenter.getInstance().getSysPath();

    private static String PATH = "send_file";
    /**
     * send:
     * <p>发送
     *
     * @param parameter
     *
     * @since   v1.01
     * @see com.afunms.synchronize.service.SendService#send(com.afunms.rmi.service.RMIParameter)
     */
    @SuppressWarnings("static-access")
    public void send(RMIParameter parameter) {
        try {
            File file = new File(SYS_PATH + PATH + "/");
            if (!file.exists()) {
                // 如果不存在，则创建该路径
                file.createNewFile();
                logger.info("创建文件同步传输服务的文件夹：" + SYS_PATH + PATH + "/");
            }
            String action = (String) parameter.getParameter("action");
            String fileName = action + "_" + sdf.format(new Date()) + "_" + UUID.randomUUID() + ".obj";
            SerializeUtil.serialize(parameter, SYS_PATH + PATH + "/" + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        Properties properties = getProperties();
        if (properties != null) {
            String name = properties.getProperty("name");
            if (name != null && name.trim().length() > 0) {
                setName(name);
            }
            String path = properties.getProperty("path");
            if (path != null && path.trim().length() > 0) {
                setPATH(path);
            }
            
        }
    }

    /**
     * getPATH:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public static String getPATH() {
        return PATH;
    }

    /**
     * setPATH:
     * <p>
     *
     * @param   path
     *          -
     * @since   v1.01
     */
    public static void setPATH(String path) {
        PATH = path;
    }

    
}

