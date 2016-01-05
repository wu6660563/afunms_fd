package com.afunms.polling.base;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

public class ErrorCode implements Serializable {

    /**
     * 序列化 id
     */
    private static final long serialVersionUID = -7439083049149415867L;

    /**
     * 成功 代码的整数常量
     */
    public static final int CODE_SUCCESS_INT = 0;

    /**
     * 无法连接 代码的整数常量
     */
    public static final int CODE_ERROR_NO_CONNECT_INT = 1;

    /**
     * 连接超时 代码的整数常量
     */
    public static final int CODE_ERROR_CONNECT_TIMEOUT_INT = 2;

    /**
     * 用户名密码错误 代码的整数常量
     */
    public static final int CODE_ERROR_USERNAME_PASSWORD_INCORRECT_INT = 3;

    /**
     * 代码与代码描述的映射 MAP
     */
    public static final Map<Integer, String> CODE_DESCRIPTION_CN_MAP = new Hashtable<Integer, String>();

    static {
        CODE_DESCRIPTION_CN_MAP.put(CODE_SUCCESS_INT, "成功");
        CODE_DESCRIPTION_CN_MAP.put(CODE_ERROR_NO_CONNECT_INT, "无法连接");
        CODE_DESCRIPTION_CN_MAP.put(CODE_ERROR_CONNECT_TIMEOUT_INT, "连接超时");
        CODE_DESCRIPTION_CN_MAP.put(CODE_ERROR_USERNAME_PASSWORD_INCORRECT_INT, "用户名密码错误");
    }

    public static final ErrorCode CODE_SUCESS
        = new ErrorCode(CODE_SUCCESS_INT, CODE_DESCRIPTION_CN_MAP.get(CODE_SUCCESS_INT));

    public static final ErrorCode CODE_ERROR_NO_CONNECT
        = new ErrorCode(CODE_ERROR_NO_CONNECT_INT, CODE_DESCRIPTION_CN_MAP.get(CODE_ERROR_NO_CONNECT_INT));

    public static final ErrorCode CODE_ERROR_CONNECT_TIMEOUT
        = new ErrorCode(CODE_ERROR_CONNECT_TIMEOUT_INT, CODE_DESCRIPTION_CN_MAP.get(CODE_ERROR_CONNECT_TIMEOUT_INT));

    public static final ErrorCode CODE_ERROR_USERNAME_PASSWORD_INCORRECT
        = new ErrorCode(CODE_ERROR_USERNAME_PASSWORD_INCORRECT_INT, CODE_DESCRIPTION_CN_MAP.get(CODE_ERROR_USERNAME_PASSWORD_INCORRECT_INT));

    /**
     * 错误代码的整数常量
     */
    private int errorCode = 0;

    /**
     * 错误代码的描述
     */
    private String description = CODE_DESCRIPTION_CN_MAP.get(errorCode);

    /**
     * 构造一个以指定的错误代码和整数常量的 {@link IndicatorErrorCode} 实例
     * @param errorCode
     * @param description
     */
    public ErrorCode(int errorCode, String description) {
        setErrorCode(errorCode);
        setDescription(description);
    }

    /**
     * 返回错误代码的整数常量
     * @return {@link Integer}
     *          错误代码的整数常量
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * 设置指定的错误代码的整数常量
     * @param errorCode
     *          指定的错误代码的整数常量
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * 返回错误代码的描述
     * @return the description
     *          错误代码的描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置指定的错误代码的描述
     * @param description
     *          错误代码的描述
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
}
