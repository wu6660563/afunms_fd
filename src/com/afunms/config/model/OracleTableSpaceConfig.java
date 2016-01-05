package com.afunms.config.model;

import com.afunms.common.base.BaseVo;

/**
 * 	Oracle数据库表空间利用率配置
 * 	 2012-07-10 
 * @author yag
 *
 */
public class OracleTableSpaceConfig extends BaseVo{

	private Integer id;
	private String nodeid;
	private String ipaddress;
	private String dbType;		// 数据库类型
	private String dbName;		// 数据库名称
	private Integer tableIndex;	
	private String tableSpace;	// 表空间名
	private String fileName;	// 文件名
	private String linkuse;
	private Integer sms;
	private String bak;
	private Integer reportflag;
	private int limenvalue;
	private int limenvalue1;
	private int limenvalue2;
	private Integer sms1;
	private Integer sms2;
	private Integer sms3;
	private Integer monflag;
	
	public OracleTableSpaceConfig(){
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	/**
     * getNodeid:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getNodeid() {
        return nodeid;
    }


    /**
     * setNodeid:
     * <p>
     *
     * @param   nodeid
     *          -
     * @since   v1.01
     */
    public void setNodeid(String nodeid) {
        this.nodeid = nodeid;
    }


    public String getIpaddress() {
		return ipaddress;
	}
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public Integer getTableIndex() {
		return tableIndex;
	}
	public void setTableIndex(Integer tableIndex) {
		this.tableIndex = tableIndex;
	}

	public String getTableSpace() {
		return tableSpace;
	}


	public void setTableSpace(String tableSpace) {
		this.tableSpace = tableSpace;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public String getLinkuse() {
		return linkuse;
	}
	public void setLinkuse(String linkuse) {
		this.linkuse = linkuse;
	}
	public Integer getSms() {
		return sms;
	}
	public void setSms(Integer sms) {
		this.sms = sms;
	}
	public String getBak() {
		return bak;
	}
	public void setBak(String bak) {
		this.bak = bak;
	}
	public Integer getReportflag() {
		return reportflag;
	}
	public void setReportflag(Integer reportflag) {
		this.reportflag = reportflag;
	}
	public int getLimenvalue() {
		return limenvalue;
	}
	public void setLimenvalue(int limenvalue) {
		this.limenvalue = limenvalue;
	}
	public int getLimenvalue1() {
		return limenvalue1;
	}
	public void setLimenvalue1(int limenvalue1) {
		this.limenvalue1 = limenvalue1;
	}
	public int getLimenvalue2() {
		return limenvalue2;
	}
	public void setLimenvalue2(int limenvalue2) {
		this.limenvalue2 = limenvalue2;
	}
	public Integer getSms1() {
		return sms1;
	}
	public void setSms1(Integer sms1) {
		this.sms1 = sms1;
	}
	public Integer getSms2() {
		return sms2;
	}
	public void setSms2(Integer sms2) {
		this.sms2 = sms2;
	}
	public Integer getSms3() {
		return sms3;
	}
	public void setSms3(Integer sms3) {
		this.sms3 = sms3;
	}
	public Integer getMonflag() {
		return monflag;
	}
	public void setMonflag(Integer monflag) {
		this.monflag = monflag;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getClass().getName() +"[ ip=" + this.getIpaddress()
				+ ", dbName=" + this.getDbName()
				+ ", tablespace=" + this.getTableSpace()
				+ ", filename=" + this.getFileName()
				+ "]" ;
	}
	
	
}
