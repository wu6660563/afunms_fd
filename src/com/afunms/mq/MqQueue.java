package com.afunms.mq;

public class MqQueue implements java.io.Serializable{
	    
	    /**
	     * 
	     */
	    private static final long serialVersionUID = -1004302532706853719L;
	    private String qname;
	    private String qtype;
	    private String persistent;
	    private String usage;
	    private String qdepth;
	    
	    private String remoteQName;
	    private String remoteQM;
	    private String xmitQName;
	    
	    public String getQdepth() {
	        return qdepth;
	    }
	    public void setQdepth(String qdepth) {
	        this.qdepth = qdepth;
	    }
	    public String getQname() {
	        return qname;
	    }
	    public void setQname(String qname) {
	        this.qname = qname;
	    }
	    public String getQtype() {
	        return qtype;
	    }
	    public void setQtype(String qtype) {
	        this.qtype = qtype;
	    }
	    public String getPersistent() {
	        return persistent;
	    }
	    public void setPersistent(String persistent) {
	        this.persistent = persistent;
	    }
	    public String getUsage() {
	        return usage;
	    }
	    public void setUsage(String usage) {
	        this.usage = usage;
	    }
	    public String getRemoteQM() {
	        return remoteQM;
	    }
	    public void setRemoteQM(String remoteQM) {
	        this.remoteQM = remoteQM;
	    }
	    public String getRemoteQName() {
	        return remoteQName;
	    }
	    public void setRemoteQName(String remoteQName) {
	        this.remoteQName = remoteQName;
	    }
	    public String getXmitQName() {
	        return xmitQName;
	    }
	    public void setXmitQName(String xmitQName) {
	        this.xmitQName = xmitQName;
	    }

	}
