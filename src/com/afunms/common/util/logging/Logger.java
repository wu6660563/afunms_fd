package com.afunms.common.util.logging;

/**
 * ��־��չ�ӿ�
 * �����Ҫʹ���Զ������־�������ʵ�ָýӿڣ�
 * �Դﵽ����־������ʵ����
 * @author ����
 * @version 1.0 $Date 2011-05-31 23:12:19 +0100 (Tue, Match 31, 2011)$
 */
public interface Logger extends Log {

    /**
     * ͨ����Ҫʹ����־�������ȫ�޶���������ȡһ���µ���־ʵ����
     * @param className
     *          ��Ҫʹ����־�������ȫ�޶���
     * @return {@link Logger}
     *          �����µ���־ʵ��
     */
    Logger getNewInstance(String className);

}
