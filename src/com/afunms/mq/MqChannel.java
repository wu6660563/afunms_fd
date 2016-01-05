package com.afunms.mq;

public class MqChannel implements java.io.Serializable{
	private static final long serialVersionUID = -4536597408346296966L;
    private String name;
    private String type;
    private String xmitQName;
    private String connName;
    private String status;
    private String operation;
    private String desc;
    private String host;
    private String port;
    
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public String getPort() {
        return port;
    }
    public void setPort(String port) {
        this.port = port;
    }
    public String getOperation() {
        return operation;
    }
    public void setOperation(String operation) {
        this.operation = operation;
    }
    public String getConnName() {
        return port;
    }
    public void setConnName(String connName) {
        this.connName = connName;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getXmitQName() {
        return xmitQName;
    }
    public void setXmitQName(String xmitQName) {
        this.xmitQName = xmitQName;
    }
}
