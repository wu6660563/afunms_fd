package com.afunms.alarm.model;

import com.afunms.common.base.BaseVo;

/**
 * �澯��·�ڵ㣬���ڸ澯��������
 * 
 * @author yag
 *
 */
public class AlarmLinkNode extends BaseVo{

	/**
	 * �ڵ��������
	 * 	�ڵ��ǻ����豸�ĸ澯ָ����ģ�����
	 * �ο���·���
	 */
	private int id;
	private int fid;
	private int linkid;		// �����
	private int nodeid;		// �豸���
	private int alarmid;	// ���
	private String name;		// ָ������
	private String	alarmDescr;
	private int isCover;	// �Ƿ񸲸�
	private int isEnabled;	// �Ƿ�ʹ��
	private int compareType;	// �ȽϷ�ʽ
	//private int number;		// ���
	//private int isCoverNext;	// �Ƿ񸲸��¼�
	
	
	private int sms0;
	private int sms1;
	private int sms2;
	
	// �澯��ֵ
	private String limenvalue0;
	private String limenvalue1;
	private String limenvalue2;
	
	// ��������
	private int time0;
	private int time1;
	private int time2;
	
	// �澯��ʽ
	private String way0;
	private String way1;
	private String way2;
	
	private String descr;	// ����

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getClass().getName() + ":[id=" + this.getId()
			+ ",fid =" + this.getFid() + ", linkid=" + this.getLinkid()
			+ ",nodeid=" + this.getNodeid() +", name=" + this.getName()+",alarmDesrc=" + this.getAlarmDescr()
			+ ",alarmid=" + this.getAlarmid() + ",isCover=" + this.getIsCover()
			+ ",isEnabled=" + this.getIsEnabled() + ",compareType=" + this.getCompareType()
			+ ",sms0=" + this.getSms0() +",sms1=" + this.getSms1() + ",sms2=" +this.getSms2()
			+ ",limen0=" + this.getLimenvalue0() + ",limen1=" + this.getLimenvalue1() + ",limen2=" + this.getLimenvalue2()
			+ ",time0=" + this.getTime0() + ",time1=" + this.getTime1() + ",time2=" + this.getTime2()
			+ ",way0=" + this.getWay0() + ",way1=" + this.getWay1() + ",way2=" + this.getWay2()
			+ ",descr=" + this.getDescr()
			+ "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	
	public int getLinkid() {
		return linkid;
	}

	public void setLinkid(int linkid) {
		this.linkid = linkid;
	}

	public int getNodeid() {
		return nodeid;
	}

	public void setNodeid(int nodeid) {
		this.nodeid = nodeid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlarmDescr() {
		return alarmDescr;
	}

	public void setAlarmDescr(String alarmDescr) {
		this.alarmDescr = alarmDescr;
	}

	public int getSms0() {
		return sms0;
	}

	public void setSms0(int sms0) {
		this.sms0 = sms0;
	}

	public int getSms1() {
		return sms1;
	}

	public void setSms1(int sms1) {
		this.sms1 = sms1;
	}

	public int getSms2() {
		return sms2;
	}

	public void setSms2(int sms2) {
		this.sms2 = sms2;
	}

	public int getAlarmid() {
		return alarmid;
	}

	public void setAlarmid(int alarmid) {
		this.alarmid = alarmid;
	}

	public int getIsCover() {
		return isCover;
	}

	public void setIsCover(int isCover) {
		this.isCover = isCover;
	}

	public int getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(int isEnabled) {
		this.isEnabled = isEnabled;
	}

	public int getCompareType() {
		return compareType;
	}

	public void setCompareType(int compareType) {
		this.compareType = compareType;
	}

	public String getLimenvalue0() {
		return limenvalue0;
	}

	public void setLimenvalue0(String limenvalue0) {
		this.limenvalue0 = limenvalue0;
	}

	public String getLimenvalue1() {
		return limenvalue1;
	}

	public void setLimenvalue1(String limenvalue1) {
		this.limenvalue1 = limenvalue1;
	}

	public String getLimenvalue2() {
		return limenvalue2;
	}

	public void setLimenvalue2(String limenvalue2) {
		this.limenvalue2 = limenvalue2;
	}

	public int getTime0() {
		return time0;
	}

	public void setTime0(int time0) {
		this.time0 = time0;
	}

	public int getTime1() {
		return time1;
	}

	public void setTime1(int time1) {
		this.time1 = time1;
	}

	public int getTime2() {
		return time2;
	}

	public void setTime2(int time2) {
		this.time2 = time2;
	}

	public String getWay0() {
		return way0;
	}

	public void setWay0(String way0) {
		this.way0 = way0;
	}

	public String getWay1() {
		return way1;
	}

	public void setWay1(String way1) {
		this.way1 = way1;
	}

	public String getWay2() {
		return way2;
	}

	public void setWay2(String way2) {
		this.way2 = way2;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}
}
