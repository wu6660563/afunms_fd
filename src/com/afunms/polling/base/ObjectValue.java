package com.afunms.polling.base;

public class ObjectValue {

    /**
     * �������
     */
    private ErrorCode errorCode;

    /**
     * �ɼ�����value
     */
    private Object objectValue;

    /**
     * ���ش������
     * @return {@link ErrorCode}
     *          �������
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    /**
     * ����ָ���Ĵ������
     * @param errorCode
     *          ָ���Ĵ������
     */
    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * �������ݶ���
     * @return {@link Object}
     *          ���ݶ���
     */
    public Object getObjectValue() {
        return objectValue;
    }

    /**
     * ����ָ�������ݶ���
     * @param objectValue
     *          ָ�������ݶ���
     */
    public void setObjectValue(Object objectValue) {
        this.objectValue = objectValue;
    }

    
}
