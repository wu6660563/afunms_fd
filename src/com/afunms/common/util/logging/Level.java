package com.afunms.common.util.logging;

/**
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
 * @author ����
 * @version $Revision: 1.0 $
 * $Date: 2011-06-04 11:22:22 +0100 (Sat, 04 June 2011) $
 *
 */
@SuppressWarnings("unchecked")
public class Level implements Comparable {

    /**
     * ALL Level �������������
     */
    public static final int ALL_INT = Integer.MIN_VALUE;

    /**
     * DEBUG Level �������������
     */
    public static final int DEBUG_INT = 10000;

    /**
     * INFO Level �������������
     */
    public static final int INFO_INT = 20000;

    /**
     * WARN Level �������������
     */
    public static final int WARN_INT = 30000;

    /**
     * ERROR Level �������������
     */
    public static final int ERROR_INT = 40000;

    /**
     * FATAL Level �������������
     */
    public static final int FATAL_INT = 50000;

    /**
     * OFF Level �������������
     */
    public static final int OFF_INT = Integer.MAX_VALUE;

    /**
     * ALL Level ָ�������ܵͼ���Ϊ�˴����е���־��¼
     */
    public static final Level ALL = new Level(ALL_INT, "ALL");

    /**
     * DEBUG Level ָ��ϸ������Ϣ�¼��Ե���Ӧ�ó����Ƿǳ��а����ġ�
     */
    public static final Level DEBUG = new Level(DEBUG_INT, "DEBUG");

    /**
     * INFO level ������Ϣ�ڴ����ȼ�����ͻ��ǿ��Ӧ�ó�������й��̡�
     */
    public static final Level INFO = new Level(INFO_INT, "INFO");

    /**
     * WARN level ���������Ǳ�ڴ�������Ρ�
     */
    public static final Level WARN = new Level(WARN_INT, "WARN");

    /**
     * ERROR level ָ����Ȼ���������¼�������Ȼ��Ӱ��ϵͳ�ļ������С�
     */
    public static final Level ERROR = new Level(ERROR_INT, "ERROR");

    /**
     * FATAL level ָ��ÿ�����صĴ����¼����ᵼ��Ӧ�ó�����˳���
     */
    public static final Level FATAL = new Level(FATAL_INT, "FATAL");

    /**
     * OFF level ָ�������ܸ߼��𲢽���־�ر�
     */
    public static final Level OFF = new Level(OFF_INT, "OFF");

    /**
     * ��������
     */
    private int level;

    /**
     * �����ַ�������
     */
    private String levelStr;

    /**
     * ʵ������־�������
     * @param level
     *          ��־�������������
     * @param levelStr
     *          ��־������ַ�������
     */
    protected Level(int level, String levelStr) {
        this.level = level;
        this.levelStr = levelStr;
    }

    /**
     * ������־��������
     * @return the level
     *          ��������
     */
    public int getLevel() {
        return level;
    }

    /**
     * ������־�����ַ�������
     * @return the levelStr
     *          �����ַ�������
     */
    public String getLevelStr() {
        return levelStr;
    }

    /**
     * ͨ���������ַ�������ת��Ϊһ����־����
     * ���ת��ʧ�ܣ��򷵻� {@link #DEBUG} ����
     * @param levelStr
     *          ��־�����ַ�������
     * @return {@link Level}
     *          -- ����ת�������־�������ת��ʧ�ܣ��򷵻� {@link #DEBUG} ����
     */
    public static Level toLevel(String levelStr) {
        return toLevel(levelStr, Level.DEBUG);
    }

    /**
     * ͨ����������������ת��Ϊһ����־����
     * ���ת��ʧ�ܣ��򷵻� {@link #DEBUG} ����
     * @param level
     *          ��־��������
     * @return {@link Level}
     *          -- ����ת�������־�������ת��ʧ�ܣ��򷵻� {@link #DEBUG} ����
     */
    public static Level toLevel(int level) {
        return toLevel(level, Level.DEBUG);
    }

    /**
     * ͨ����������������ת��Ϊһ����־����
     * ���ת��ʧ�ܣ��򷵻ظ�����Ĭ�ϼ���
     * @param level
     *          ��־��������
     * @param defaultLevel
     *          Ĭ�ϵ���־����
     * @return {@link Level}
     *          -- ����ת�������־�������ת��ʧ�ܣ��򷵻ظ�����Ĭ�ϼ���
     */
    public static Level toLevel(int level, Level defaultLevel) {
        switch(level) {
            case ALL_INT: return ALL;
            case DEBUG_INT: return DEBUG;
            case INFO_INT: return INFO;
            case WARN_INT: return WARN;
            case ERROR_INT: return ERROR;
            case FATAL_INT: return FATAL;
            case OFF_INT: return OFF;
            default: return defaultLevel;
        }
    }

    /**
     * ͨ���������ַ�������ת��Ϊһ����־����
     * ���ת��ʧ�ܣ��򷵻ظ�����Ĭ�ϼ���
     * @param levelStr
     *          ��־�����ַ�������
     * @param defaultLevel
     *          Ĭ�ϵ���־����
     * @return {@link Level}
     *          -- ����ת�������־�������ת��ʧ�ܣ��򷵻ظ�����Ĭ�ϼ���
     */
    public static Level toLevel(String levelStr, Level defaultLevel) {
        if (levelStr == null) {
            return defaultLevel;
        }

        String s = levelStr.toUpperCase();

        if (s.equals("ALL")) {
            return Level.ALL;
        }
        if (s.equals("DEBUG")) {
            return Level.DEBUG;
        }
        if (s.equals("INFO"))  {
            return Level.INFO;
        }
        if (s.equals("WARN"))  {
            return Level.WARN;
        }
        if (s.equals("ERROR")) {
            return Level.ERROR;
        }
        if (s.equals("FATAL")) {
            return Level.FATAL;
        }
        if (s.equals("OFF")) {
            return Level.OFF;
        }

//        For Turkish i problem, see bug 40937

        if (s.equals("\u0130NFO")) {
            return Level.INFO;
        }
        return defaultLevel;
    }

    /**
     * �Ƚϴ���־�ȼ���ָ���ȼ������˳��
     * @param o
     *          ָ������־�ȼ����󣬸ö������ͱ���Ϊ Level ���� level ���ࡣ
     * @return {@link Integer}
     *          ����ö���С�ڡ����ڻ����ָ��������ֱ𷵻ظ������������������
     */
    public int compareTo(Object o) {
        Level anotherLevel = null;
        if (o == null) {
            throw new NullPointerException("ָ���ĵȼ�����Ϊ��");
        }
        if (o instanceof Level) {
            anotherLevel = (Level) o;
        } else {
            throw new IllegalArgumentException(
                    "ָ���Ķ�������Ϊ��" + o.getClass().getName()
                    + "�������ͱ���Ϊ��" + Level.class.getName());
        }
        if (this.getLevel() > anotherLevel.getLevel()) {
            return 1;
        } else if (this.getLevel() == anotherLevel.getLevel()) {
            return 0;
        } else {
            return -1;
        }
    }

}
