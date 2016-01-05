package com.afunms.common.util.logging;


/**
 * ���޷�ͨ����־���� <code>LogFactory</code> ����Ӧ�Ĺ�������������һ����־ 
 * <code>Log</code> ��ʵ��ʱ���׳����쳣</p>
 *
 * @author ����
 * @version $Revision: 1.0 $ $Date: 2011-03-02 09:11:11 +0200 (Wed, 02 Mar 2011) $
 */

public class LogConfigurationException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6976186569111431357L;


	/**
     * ���췽��
     * ����һ����ϸ��ϢΪ <code>null</code> ���쳣
     */
    public LogConfigurationException() {
        super();
    }


    /**
     * ���췽��
     * ����һ��ָ����ϸ��ϢΪ <code>String</code> ���͵��쳣
     *
     * @param message The detail message
     */
    public LogConfigurationException(String message) {
        super(message);
    }


    /**
     * ���췽��
     * ͨ��ָ��ָ������Ϊ <code>Throwable</code> ���͵� 
     * <code>cause</code> ��ͨ����������������ϸ��Ϣ������һ�� 
     * <code>LogConfigurationException</code> �쳣
     *
     * @param cause The underlying cause
     */
    public LogConfigurationException(Throwable cause) {
        this( ((cause == null) ? null : cause.toString()), cause);
    }


    /**
     * ���췽��
     * ͨ��ָ����ϸ��ϢΪ <code>String</code> ���͵� <code>message</code> 
     * ��ָ������Ϊ <code>Throwable</code> ���͵� <code>cause</code>
     * ������һ�� <code>LogConfigurationException</code> �쳣
     *
     * @param message The detail message
     * @param cause The underlying cause
     */
    public LogConfigurationException(String message, Throwable cause) {

        super(message);
        this.cause = cause; // Two-argument version requires JDK 1.4 or later

    }


    /**
     * �쳣�ĵײ�ԭ�� {@link Throwable}
     */
    protected Throwable cause = null;


    /**
     * ��������쳣�ĵײ�ԭ�� {@link Throwable}
     */
    public Throwable getCause() {

        return (this.cause);

    }


}
