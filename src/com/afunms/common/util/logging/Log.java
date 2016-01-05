package com.afunms.common.util.logging;

/**
 * <p>һ���򵥵���־��¼ API �ĳ���ӿ�.  Ϊ���ܹ�����־���� {@ LogFactory }
 * ���ɹ�ʵ������־, ������־�����ʵ�ָýӿڣ����ұ����д�һ���ַ��� {@ String}
 * ��Ϊ�����Ĺ��췽�������ַ������ڸ������־��ʵ��������</p>
 *
 * <p> ����Ϊ <code>Log</code> ������ȼ�:
 * <ol>
 * <li>���� DEBUG (the least serious)</li>
 * <li>��Ϣ INFO</li>
 * <li>���� WARN</li>
 * <li>���� ERROR</li>
 * <li>���� FATAL (the most serious)</li>
 * </ol>
 *
 * <ol>
 * <li>DEBUG Levelָ��ϸ������Ϣ�¼��Ե���Ӧ�ó����Ƿǳ��а����ġ�</li>
 * <li>INFO level���� ��Ϣ�ڴ����ȼ�����ͻ��ǿ��Ӧ�ó�������й��̡�</li>
 * <li>WARN level���������Ǳ�ڴ�������Ρ�</li>
 * <li>ERROR levelָ����Ȼ���������¼�������Ȼ��Ӱ��ϵͳ�ļ������С�</li>
 * <li>FATAL levelָ��ÿ�����صĴ����¼����ᵼ��Ӧ�ó�����˳���</li>
 * </ol>
 *
 * ��Щ��־�ȼ��ĸ����Ƿ��Ӧ����Ҫ�����ڵײ���־ϵͳ��ʵ�֡�
 * ��ȻԤ������������ģ����ǻ�����Ҫʵ������֤.</p>
 *
 * <p>���������Ǽ�¼�ȽϹ�ע��.
 * ͨ������ʵ�������, һ����������ܱ�����ʡ����࿪��(�����¼������Ϣ�Ļ�).</p>
 *
 * <p> ����,
 * <code><pre>
 *    if (log.isDebugEnabled()) {
 *        ... do something expensive ...
 *        log.debug(theResult);
 *    }
 * </pre></code>
 * </p>
 *
 * <p>�ײ����־ϵͳһ�㶼�����ⲿ����־ API ����������, Ȼ��ͨ��ĳ�ֻ�����֧�ָ�ϵͳ</p>
 *
 * @author <a href="mailto:nielin@dhcc.com.cn">����</a>
 * @version $Revision: 1.0 $
 * $Date: 2011-03-01 15:48:25 +0100 (Tue, 01 Mar 2011) $
 */
public interface Log {


    // ----------------------------------------------------- Logging Properties


    /**
     * <p>��鵱ǰ��־�����Ƿ����� DEBUG ����</p>
     *
     * <p>���������Ϊ�˼�����־δ���� DEBUG �������俪�����磺</p>
     * <p>
     * if({@link #isDebugEnabled()}) {
     *      {@link #debug(Object)}
     * }
     * </p>
     * <p>���������Ƿ��¼����־�������һ���жϵĿ�����΢������ģ�����
     * ��������м�¼��ȴ����ʡ����࿪����</p>
     *
     * @return {@link Boolean}
     *          -- ��ǰ��־�������� DEBUG ���𷵻� true �����򷵻� false
     */
    boolean isDebugEnabled();


    /**
     * <p>��鵱ǰ��־�����Ƿ����� ERROR ����</p>
     *
     * <p>���������Ϊ�˼�����־δ���� ERROR �������俪�����磺</p>
     * <p>
     * if({@link #isErrorEnabled()}) {
     *      {@link #error(Object)}
     * }
     * </p>
     * <p>���������Ƿ��¼����־�������һ���жϵĿ�����΢������ģ�����
     * ��������м�¼��ȴ����ʡ����࿪����</p>
     *
     * @return {@link Boolean}
     *          -- ��ǰ��־�������� ERROR ���𷵻� true �����򷵻� false
     */
    boolean isErrorEnabled();


    /**
     * <p>��鵱ǰ��־�����Ƿ����� FATAL ����</p>
     *
     * <p>���������Ϊ�˼�����־δ���� FATAL �������俪�����磺</p>
     * <p>
     * if({@link #isFatalEnabled()}) {
     *      {@link #fatal(Object)}
     * }
     * </p>
     * <p>���������Ƿ��¼����־�������һ���жϵĿ�����΢������ģ�����
     * ��������м�¼��ȴ����ʡ����࿪����</p>
     *
     * @return {@link Boolean}
     *          -- ��ǰ��־�������� FATAL ���𷵻� true �����򷵻� false
     */
    boolean isFatalEnabled();


    /**
     * <p>��鵱ǰ��־�����Ƿ����� INFO ����</p>
     *
     * <p>���������Ϊ�˼�����־δ���� INFO �������俪�����磺</p>
     * <p>
     * if({@link #isInfoEnabled()}) {
     *      {@link #info(Object)}
     * }
     * </p>
     * <p>���������Ƿ��¼����־�������һ���жϵĿ�����΢������ģ�����
     * ��������м�¼��ȴ����ʡ����࿪����</p>
     *
     * @return {@link Boolean}
     *          -- ��ǰ��־�������� INFO ���𷵻� true �����򷵻� false
     */
    boolean isInfoEnabled();


    /**
     * <p>��鵱ǰ��־�����Ƿ����� WARN ����</p>
     *
     * <p>���������Ϊ�˼�����־δ���� WARN �������俪�����磺</p>
     * <p>
     * if({@link #isWarnEnabled()}) {
     *      {@link #warn(Object)}
     * }
     * </p>
     * <p>���������Ƿ��¼����־�������һ���жϵĿ�����΢������ģ�����
     * ��������м�¼��ȴ����ʡ����࿪����</p>
     *
     * @return {@link Boolean}
     *          -- ��ǰ��־�������� WARN ���𷵻� true �����򷵻� false
     */
    boolean isWarnEnabled();

    /**
     *
     * <p>��鵱ǰ��־���öԸ����� level �����Ƿ����á�</p>
     *
     * <p>���������Ϊ�˼�����־�Ը����� level ������δ����ʱ����俪�����磺</p>
     * <p>
     * if({@link #isEnabledFor(Level)}) {
     *      {@link #warn(Object)}
     * }
     * </p>
     * <p>���������Ƿ��¼����־�������һ���жϵĿ�����΢������ģ�����
     * ��������м�¼��ȴ����ʡ����࿪����</p>
     *
     * @param level
     *          �����ļ���
     * @return {@link Boolean}
     *          -- ��ǰ��־�������ø����� level ���𷵻� true �����򷵻� false
     */
    boolean isEnabledFor(Level level);


    // -------------------------------------------------------- Logging Methods


    /**
     * <p>����־�� DEBUG �ȼ�����¼��Ϣ��</p>
     *
     * <p>�÷��������ȵ��� {@link #isDebugEnabled()} ������
     * �жϺͱȽ���־�� DEBUG �ȼ��Ƿ����á�������� DEBUG �ȼ�
     * ����¼��־����Ὣ��Ϊ�����������Ϣ����ת��Ϊ�ַ�����Ȼ��
     * ���������</p>
     *
     * <p>ע�⣺�÷���ֻ���ӡ���� {@link Throwable} �����ƣ�
     * ��û�� {@link Throwable} ��ջ������Ϣ������Ҫ��ջ����
     * ��Ϣ��Ӧ��ʹ�� {@link #debug(Object, Throwable)} ����
     * �����档
     *
     * @param message
     *          ��־��Ϣ����
     */
    void debug(Object message);


    /**
     * <p>����־�� DEBUG �ȼ�����¼��Ϣ��������Ϊ���������
     * {@link Throwable} �Ķ�ջ������Ϣ��</p>
     *
     * @see #debug(Object)
     * @param message
     *          ��־��Ϣ����
     * @param t
     *          ��־�쳣���������ջ����
     */
    void debug(Object message, Throwable t);


    /**
     * <p>����־�� INFO �ȼ�����¼��Ϣ��</p>
     *
     * <p>�÷��������ȵ��� {@link #isFatalEnabled()} ������
     * �жϺͱȽ���־�� INFO �ȼ��Ƿ����á�������� INFO �ȼ�
     * ����¼��־����Ὣ��Ϊ�����������Ϣ����ת��Ϊ�ַ�����Ȼ��
     * ���������</p>
     *
     * <p>ע�⣺�÷���ֻ���ӡ���� {@link Throwable} �����ƣ�
     * ��û�� {@link Throwable} ��ջ������Ϣ������Ҫ��ջ����
     * ��Ϣ��Ӧ��ʹ�� {@link #info(Object, Throwable)} ����
     * �����档
     *
     * @param message
     *          ��־��Ϣ����
     */
    void info(Object message);


    /**
     * <p>����־�� INFO �ȼ�����¼��Ϣ��������Ϊ���������
     * {@link Throwable} �Ķ�ջ������Ϣ��</p>
     *
     * @see #info(Object)
     * @param message
     *          ��־��Ϣ����
     * @param t
     *          ��־�쳣���������ջ����
     */
    void info(Object message, Throwable t);


    /**
     * <p>����־�� WARN �ȼ�����¼��Ϣ��</p>
     *
     * <p>�÷��������ȵ��� {@link #isWarnEnabled()} ������
     * �жϺͱȽ���־�� WARN �ȼ��Ƿ����á�������� WARN �ȼ�
     * ����¼��־����Ὣ��Ϊ�����������Ϣ����ת��Ϊ�ַ�����Ȼ��
     * ���������</p>
     *
     * <p>ע�⣺�÷���ֻ���ӡ���� {@link Throwable} �����ƣ�
     * ��û�� {@link Throwable} ��ջ������Ϣ������Ҫ��ջ����
     * ��Ϣ��Ӧ��ʹ�� {@link #warn(Object, Throwable)} ����
     * �����档
     *
     * @param message
     *          ��־��Ϣ����
     */
    void warn(Object message);


    /**
     * <p>����־�� WARN �ȼ�����¼��Ϣ��������Ϊ���������
     * {@link Throwable} �Ķ�ջ������Ϣ��</p>
     *
     * @see #warn(Object)
     * @param message
     *          ��־��Ϣ����
     * @param t
     *          ��־�쳣���������ջ����
     */
    void warn(Object message, Throwable t);


    /**
     * <p>����־�� ERROR �ȼ�����¼��Ϣ��</p>
     *
     * <p>�÷��������ȵ��� {@link #isErrorEnabled()} ������
     * �жϺͱȽ���־�� ERROR �ȼ��Ƿ����á�������� ERROR �ȼ�
     * ����¼��־����Ὣ��Ϊ�����������Ϣ����ת��Ϊ�ַ�����Ȼ��
     * ���������</p>
     *
     * <p>ע�⣺�÷���ֻ���ӡ���� {@link Throwable} �����ƣ�
     * ��û�� {@link Throwable} ��ջ������Ϣ������Ҫ��ջ����
     * ��Ϣ��Ӧ��ʹ�� {@link #error(Object, Throwable)} ����
     * �����档
     *
     * @param message
     *          ��־��Ϣ����
     */
    void error(Object message);


    /**
     * <p>����־�� ERROR �ȼ�����¼��Ϣ��������Ϊ���������
     * {@link Throwable} �Ķ�ջ������Ϣ��</p>
     *
     * @see #error(Object)
     * @param message
     *          ��־��Ϣ����
     * @param t
     *          ��־�쳣���������ջ����
     */
    void error(Object message, Throwable t);


    /**
     * <p>����־�� FATAL �ȼ�����¼��Ϣ��</p>
     *
     * <p>�÷��������ȵ��� {@link #isFatalEnabled()} ������
     * �жϺͱȽ���־�� FATAL �ȼ��Ƿ����á�������� FATAL �ȼ�
     * ����¼��־����Ὣ��Ϊ�����������Ϣ����ת��Ϊ�ַ�����Ȼ��
     * ���������</p>
     *
     * <p>ע�⣺�÷���ֻ���ӡ���� {@link Throwable} �����ƣ�
     * ��û�� {@link Throwable} ��ջ������Ϣ������Ҫ��ջ����
     * ��Ϣ��Ӧ��ʹ�� {@link #fatal(Object, Throwable)} ����
     * �����档
     *
     * @param message
     *          ��־��Ϣ����
     */
    void fatal(Object message);


    /**
     * <p>����־�� FATAL �ȼ�����¼��Ϣ��������Ϊ���������
     * {@link Throwable} �Ķ�ջ������Ϣ��</p>
     *
     * @see #fatal(Object)
     * @param message
     *          ��־��Ϣ����
     * @param t
     *          ��־�쳣���������ջ����
     */
    void fatal(Object message, Throwable t);

    /**
     * <p>�ø��� level �ȼ�����¼��־��Ϣ��</p>
     *
     * <p>�÷��������ȵ��� {@link #isEnabledFor(Level)} ������
     * �жϺͱȽϸ�������־ level �ȼ��Ƿ����á�������ø����� level
     * �ȼ�����¼��־����Ὣ��Ϊ�����������Ϣ����ת��Ϊ�ַ�����Ȼ��
     * ���������</p>
     *
     * <p>ע�⣺�÷���ֻ���ӡ���� {@link Throwable} �����ƣ�
     * ��û�� {@link Throwable} ��ջ������Ϣ������Ҫ��ջ����
     * ��Ϣ��Ӧ��ʹ�� {@link #log(Level, Object, Throwable)}
     * ���������档
     *
     * @param level
     *          ��������־����
     * @param message
     *          ��־��Ϣ����
     */
    void log(Level level, Object message);

    /**
     * <p>�ø�������־ level �ȼ�����¼��Ϣ��������Ϊ���������
     * {@link Throwable} �Ķ�ջ������Ϣ��</p>
     *
     * @see #log(Level, Object)
     * @param level
     *          ��������־����
     * @param message
     *          ��־��Ϣ����
     * @param t
     *          ��־�쳣���������ջ����
     */
    void log(Level level, Object message, Throwable t);
}
