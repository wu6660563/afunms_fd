package com.afunms.polling.base;

public class ObjectValue {

    /**
     * 错误代码
     */
    private ErrorCode errorCode;

    /**
     * 采集到的value
     */
    private Object objectValue;

    /**
     * 返回错误代码
     * @return {@link ErrorCode}
     *          错误代码
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    /**
     * 设置指定的错误代码
     * @param errorCode
     *          指定的错误代码
     */
    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * 返回数据对象
     * @return {@link Object}
     *          数据对象
     */
    public Object getObjectValue() {
        return objectValue;
    }

    /**
     * 设置指定的数据对象
     * @param objectValue
     *          指定的数据对象
     */
    public void setObjectValue(Object objectValue) {
        this.objectValue = objectValue;
    }

    
}
