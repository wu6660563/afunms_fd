package com.afunms.polling.base;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

public class ErrorCode implements Serializable {

    /**
     * ���л� id
     */
    private static final long serialVersionUID = -7439083049149415867L;

    /**
     * �ɹ� �������������
     */
    public static final int CODE_SUCCESS_INT = 0;

    /**
     * �޷����� �������������
     */
    public static final int CODE_ERROR_NO_CONNECT_INT = 1;

    /**
     * ���ӳ�ʱ �������������
     */
    public static final int CODE_ERROR_CONNECT_TIMEOUT_INT = 2;

    /**
     * �û���������� �������������
     */
    public static final int CODE_ERROR_USERNAME_PASSWORD_INCORRECT_INT = 3;

    /**
     * ���������������ӳ�� MAP
     */
    public static final Map<Integer, String> CODE_DESCRIPTION_CN_MAP = new Hashtable<Integer, String>();

    static {
        CODE_DESCRIPTION_CN_MAP.put(CODE_SUCCESS_INT, "�ɹ�");
        CODE_DESCRIPTION_CN_MAP.put(CODE_ERROR_NO_CONNECT_INT, "�޷�����");
        CODE_DESCRIPTION_CN_MAP.put(CODE_ERROR_CONNECT_TIMEOUT_INT, "���ӳ�ʱ");
        CODE_DESCRIPTION_CN_MAP.put(CODE_ERROR_USERNAME_PASSWORD_INCORRECT_INT, "�û����������");
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
     * ����������������
     */
    private int errorCode = 0;

    /**
     * ������������
     */
    private String description = CODE_DESCRIPTION_CN_MAP.get(errorCode);

    /**
     * ����һ����ָ���Ĵ����������������� {@link IndicatorErrorCode} ʵ��
     * @param errorCode
     * @param description
     */
    public ErrorCode(int errorCode, String description) {
        setErrorCode(errorCode);
        setDescription(description);
    }

    /**
     * ���ش���������������
     * @return {@link Integer}
     *          ����������������
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * ����ָ���Ĵ���������������
     * @param errorCode
     *          ָ���Ĵ���������������
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * ���ش�����������
     * @return the description
     *          ������������
     */
    public String getDescription() {
        return description;
    }

    /**
     * ����ָ���Ĵ�����������
     * @param description
     *          ������������
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
}
