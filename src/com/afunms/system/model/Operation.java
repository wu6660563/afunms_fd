package com.afunms.system.model;

/**
 * 操作
 * @author 聂林
 *
 */
public class Operation {

    /**
     * 操作名称
     */
    private String name;
    
    /**
     * 操作代号
     */
    private String code;
    
    public static final Operation create = new Operation("增加", "create");

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
