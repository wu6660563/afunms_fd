package com.afunms.common.util.threadPool;

import java.util.ArrayList;
import java.util.List;

/**
 * �����̳߳��������õĳ��������ﶨ�����̳߳������ļ������
 * ÿ�����������Ե����ơ�
 *
 * @author ����
 * @version 1.0 $Date: 2011-03-07 16:34:30 +0100 (Mon, Mar 7, 2011) $
 */
public class ThreadPoolProperitesConstant {

    /**
     * �̳߳�����
     */
    protected static final String NAME = "name";

    /**
     * ����߳���
     */
    protected static final String MAX_THREADS = "max-threads";

    /**
     * �������߳���
     */
    protected static final String MAX_SPARE_THREADS = "max-spare-threads";

    /**
     * ��С�����߳���
     */
    protected static final String MIN_SPARE_THREADS = "min-spare-threads";

    /**
     * �߳̿��г�ʱʱ��
     */
    protected static final String WORK_WAIT_TIMEOUT = "work-wait-timeout";

    /**
     * �߳�Ĭ�����ȼ�
     */
    protected static final String THREAD_PRIORITIY = "thread-priority";

    /**
     * �Ƿ������̳߳���־
     */
    protected static final String IS_LOG = "isLog";

    /**
     * �Ƿ������̳߳���־
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
