package com.afunms.capreport.model;
/**
 * ͳ���豸������ָ��
 * @author wxy
 *
 */
public class StatisNumer {
	private String ip;// ip��ַ
	private String type;// ����
	private String name;// ����
	private String current;// ��ǰֵ
	private String average;// ƽ��ֵ
	private String maximum;// ���ֵ
	private String mininum;// ��Сֵ

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
    
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCurrent() {
		return current;
	}

	public void setCurrent(String current) {
		this.current = current;
	}

	public String getAverage() {
		return average;
	}

	public void setAverage(String average) {
		this.average = average;
	}

	public String getMaximum() {
		return maximum;
	}

	public void setMaximum(String maximum) {
		this.maximum = maximum;
	}

	public String getMininum() {
		return mininum;
	}

	public void setMininum(String mininum) {
		this.mininum = mininum;
	}

}
