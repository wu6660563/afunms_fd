package com.afunms.common.util.logging;

/**
 * <p> 以下为 <code>Log</code> 的五个等级:
 * <ol>
 * <li>调试 DEBUG (the least serious)</li>
 * <li>信息 INFO</li>
 * <li>警告 WARN</li>
 * <li>错误 ERROR</li>
 * <li>致命 FATAL (the most serious)</li>
 * </ol>
 *
 * <ol>
 * <li>DEBUG Level指出细粒度信息事件对调试应用程序是非常有帮助的。</li>
 * <li>INFO level表明 消息在粗粒度级别上突出强调应用程序的运行过程。</li>
 * <li>WARN level表明会出现潜在错误的情形。</li>
 * <li>ERROR level指出虽然发生错误事件，但仍然不影响系统的继续运行。</li>
 * <li>FATAL level指出每个严重的错误事件将会导致应用程序的退出。</li>
 * </ol>
 * @author 聂林
 * @version $Revision: 1.0 $
 * $Date: 2011-06-04 11:22:22 +0100 (Sat, 04 June 2011) $
 *
 */
@SuppressWarnings("unchecked")
public class Level implements Comparable {

    /**
     * ALL Level 级别的整数常量
     */
    public static final int ALL_INT = Integer.MIN_VALUE;

    /**
     * DEBUG Level 级别的整数常量
     */
    public static final int DEBUG_INT = 10000;

    /**
     * INFO Level 级别的整数常量
     */
    public static final int INFO_INT = 20000;

    /**
     * WARN Level 级别的整数常量
     */
    public static final int WARN_INT = 30000;

    /**
     * ERROR Level 级别的整数常量
     */
    public static final int ERROR_INT = 40000;

    /**
     * FATAL Level 级别的整数常量
     */
    public static final int FATAL_INT = 50000;

    /**
     * OFF Level 级别的整数常量
     */
    public static final int OFF_INT = Integer.MAX_VALUE;

    /**
     * ALL Level 指出尽可能低级别为了打开所有的日志记录
     */
    public static final Level ALL = new Level(ALL_INT, "ALL");

    /**
     * DEBUG Level 指出细粒度信息事件对调试应用程序是非常有帮助的。
     */
    public static final Level DEBUG = new Level(DEBUG_INT, "DEBUG");

    /**
     * INFO level 表明消息在粗粒度级别上突出强调应用程序的运行过程。
     */
    public static final Level INFO = new Level(INFO_INT, "INFO");

    /**
     * WARN level 表明会出现潜在错误的情形。
     */
    public static final Level WARN = new Level(WARN_INT, "WARN");

    /**
     * ERROR level 指出虽然发生错误事件，但仍然不影响系统的继续运行。
     */
    public static final Level ERROR = new Level(ERROR_INT, "ERROR");

    /**
     * FATAL level 指出每个严重的错误事件将会导致应用程序的退出。
     */
    public static final Level FATAL = new Level(FATAL_INT, "FATAL");

    /**
     * OFF level 指出尽可能高级别并将日志关闭
     */
    public static final Level OFF = new Level(OFF_INT, "OFF");

    /**
     * 级别整数
     */
    private int level;

    /**
     * 级别字符串描述
     */
    private String levelStr;

    /**
     * 实例化日志级别对象
     * @param level
     *          日志级别的整数常量
     * @param levelStr
     *          日志级别的字符串描述
     */
    protected Level(int level, String levelStr) {
        this.level = level;
        this.levelStr = levelStr;
    }

    /**
     * 返回日志级别整数
     * @return the level
     *          级别整数
     */
    public int getLevel() {
        return level;
    }

    /**
     * 返回日志级别字符串描述
     * @return the levelStr
     *          级别字符串描述
     */
    public String getLevelStr() {
        return levelStr;
    }

    /**
     * 通过给定的字符串参数转换为一个日志级别。
     * 如果转换失败，则返回 {@link #DEBUG} 级别。
     * @param levelStr
     *          日志级别字符串描述
     * @return {@link Level}
     *          -- 返回转换后的日志级别，如果转换失败，则返回 {@link #DEBUG} 级别
     */
    public static Level toLevel(String levelStr) {
        return toLevel(levelStr, Level.DEBUG);
    }

    /**
     * 通过给定的整数参数转换为一个日志级别。
     * 如果转换失败，则返回 {@link #DEBUG} 级别。
     * @param level
     *          日志级别整数
     * @return {@link Level}
     *          -- 返回转换后的日志级别，如果转换失败，则返回 {@link #DEBUG} 级别
     */
    public static Level toLevel(int level) {
        return toLevel(level, Level.DEBUG);
    }

    /**
     * 通过给定的整数参数转换为一个日志级别。
     * 如果转换失败，则返回给定的默认级别。
     * @param level
     *          日志级别整数
     * @param defaultLevel
     *          默认的日志级别
     * @return {@link Level}
     *          -- 返回转换后的日志级别，如果转换失败，则返回给定的默认级别。
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
     * 通过给定的字符串参数转换为一个日志级别。
     * 如果转换失败，则返回给定的默认级别。
     * @param levelStr
     *          日志级别字符串描述
     * @param defaultLevel
     *          默认的日志级别
     * @return {@link Level}
     *          -- 返回转换后的日志级别，如果转换失败，则返回给定的默认级别。
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
     * 比较此日志等级与指定等级对象的顺序。
     * @param o
     *          指定的日志等级对象，该对象类型必须为 Level 或者 level 子类。
     * @return {@link Integer}
     *          如果该对象小于、等于或大于指定对象，则分别返回负整数、零或正整数。
     */
    public int compareTo(Object o) {
        Level anotherLevel = null;
        if (o == null) {
            throw new NullPointerException("指定的等级对象为空");
        }
        if (o instanceof Level) {
            anotherLevel = (Level) o;
        } else {
            throw new IllegalArgumentException(
                    "指定的对象类型为：" + o.getClass().getName()
                    + "，其类型必须为：" + Level.class.getName());
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
