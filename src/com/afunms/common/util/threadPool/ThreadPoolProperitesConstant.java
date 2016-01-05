package com.afunms.common.util.threadPool;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于线程池属性配置的常量，这里定义了线程池配置文件里关于
 * 每个可配置属性的名称。
 *
 * @author 聂林
 * @version 1.0 $Date: 2011-03-07 16:34:30 +0100 (Mon, Mar 7, 2011) $
 */
public class ThreadPoolProperitesConstant {

    /**
     * 线程池名称
     */
    protected static final String NAME = "name";

    /**
     * 最大线程数
     */
    protected static final String MAX_THREADS = "max-threads";

    /**
     * 最大空闲线程数
     */
    protected static final String MAX_SPARE_THREADS = "max-spare-threads";

    /**
     * 最小空闲线程数
     */
    protected static final String MIN_SPARE_THREADS = "min-spare-threads";

    /**
     * 线程空闲超时时间
     */
    protected static final String WORK_WAIT_TIMEOUT = "work-wait-timeout";

    /**
     * 线程默认优先级
     */
    protected static final String THREAD_PRIORITIY = "thread-priority";

    /**
     * 是否启用线程池日志
     */
    protected static final String IS_LOG = "isLog";

    /**
     * 是否启用线程池日志
     */
    protected static final String[] PROPERITIES_KEYS = {
                                NAME,
                                MAX_THREADS,
                                MAX_SPARE_THREADS,
                                MIN_SPARE_THREADS,
                                WORK_WAIT_TIMEOUT,
                                THREAD_PRIORITIY,
                                IS_LOG
    };
    
    public static String[] getProperitesKeys() {
        return PROPERITIES_KEYS;
    }
}
