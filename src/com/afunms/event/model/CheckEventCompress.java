/**
 * <p>Description:mapping table NMS_POSITION</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-07
 */

package com.afunms.event.model;

import com.afunms.common.base.BaseVo;

/**
 * 告警压缩实体
 * 
 * @author yag
 *
 */
public class CheckEventCompress extends BaseVo implements java.io.Serializable {

    // 告警指标Id
    private String alarmId;

    // 告警指标名称
    private String name;

    // 告警指标设备ID
    private String nodeId;

    private String type;

    private String subtype;

    private String sindex;

    private int alarmlevel;

    private String content;

    private String firsttime;	// 标记告警第一次产生的时间
    
    private String collecttime;

    public String toString() {
    	StringBuffer sb = new StringBuffer();
    	sb.append(this.getClass().getName() + ":[id=" + this.getAlarmId());
    	sb.append(",name=" + this.getName() + ",nodeId=" + this.getNodeId());
    	sb.append(",type=" + this.getType() + ",subtype=" + this.getSubtype());
    	sb.append(",sindex=" + this.getSindex() +",alarmlevel=" + this.getAlarmlevel());
    	sb.append(",content=" + this.getContent() + ",firsttime="+ this.getFirsttime());
    	sb.append(",collecttime"+this.getCollecttime() +"]");
    	return sb.toString();
    }
    
    /**
     * @return the alarmId
     */
    public String getAlarmId() {
        return alarmId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the nodeId
     */
    public String getNodeId() {
        return nodeId;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the subtype
     */
    public String getSubtype() {
        return subtype;
    }

    /**
     * @return the sindex
     */
    public String getSindex() {
        return sindex;
    }

    /**
     * @return the alarmlevel
     */
    public int getAlarmlevel() {
        return alarmlevel;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @return the collecttime
     */
    public String getCollecttime() {
        return collecttime;
    }

    /**
     * @param alarmId the alarmId to set
     */
    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param nodeId the nodeId to set
     */
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @param subtype the subtype to set
     */
    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    /**
     * @param sindex the sindex to set
     */
    public void setSindex(String sindex) {
        this.sindex = sindex;
    }

    /**
     * @param alarmlevel the alarmlevel to set
     */
    public void setAlarmlevel(int alarmlevel) {
        this.alarmlevel = alarmlevel;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @param collecttime the collecttime to set
     */
    public void setCollecttime(String collecttime) {
        this.collecttime = collecttime;
    }

	public String getFirsttime() {
		return firsttime;
	}

	public void setFirsttime(String firsttime) {
		this.firsttime = firsttime;
	}
    
}
