package com.afunms.system.model;

/**
 * ����
 * @author ����
 *
 */
public class Operation {

    /**
     * ��������
     */
    private String name;
    
    /**
     * ��������
     */
    private String code;
    
    public static final Operation create = new Operation("����", "create");

    public Operation(String name, String code) {
        setName(name);
        setCode(code);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }
    
}
