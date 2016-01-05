package com.afunms.common.util.logging.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * 默认的Log4j日志的配置属性
 * @author <a href="mailto:nielin@dhcc.com.cn">聂林</a>
 * @version $Revision: 1.0 $Date 2011-06-02 23:10:15 +0100 (Thu, 02 June 2011) $
 */
public class Log4jPorperties {

    /**
     * Log4j 日志配置属性
     */
    private static Properties logProperties;

    static {
        logProperties = new Properties();
        try {
            logProperties.load(
                    Log4jPorperties.class.getClassLoader().
                    getResourceAsStream(
                            "com/nlsystem/common/logging/impl/"
                            + "log4j.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取默认的 Log4j 日志配置属性
     * @return
     *          默认的 Log4j 日志配置属性
     */
    public static Properties getProperties() {
        return logProperties;
    }

}
