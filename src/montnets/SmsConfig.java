package montnets;

import java.util.Calendar;

import com.afunms.common.base.BaseVo;

public class SmsConfig extends BaseVo{
	
	private int id;
	private String name;//�û���
	private String mobilenum;//�ֻ�����
	private String eventlist;//�澯��Ϣ
	private Calendar eventtime;//ʱ��
	public String getEventlist() {
		return eventlist;
	}
	public void setEventlist(String eventlist) {
		this.eventlist = eventlist;
	}

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMobilenum() {
		return mobilenum;
	}
	public void setMobilenum(String mobilenum) {
		this.mobilenum = mobilenum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Calendar getEventtime() {
		return eventtime;
	}
	public void setEventtime(Calendar eventtime) {
		this.eventtime = eventtime;
	}

}
