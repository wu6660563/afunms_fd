package com.afunms.application.model;

import com.afunms.common.base.BaseVo;

/**
 * @author HONGLI E-mail: ty564457881@163.com
 * @version ����ʱ�䣺Aug 16, 2011 3:26:49 PM
 * ��˵�� :��������ָ���м���ģ����
 */
public class PerformancePanelIndicatorsModel extends BaseVo{

	private String id;
	/**
	 * �����������
	 */
	private String panelName;
	
	/**
	 * ָ�������
	 */
	private String indicatorName;
	
	/**
	 * ָ�������
	 */
	private String indicatorDesc;
	
	public String getIndicatorName() {
		return indicatorName;
	}

	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
	}

	public String getIndicatorDesc() {
		return indicatorDesc;
	}

	public void setIndicatorDesc(String indicatorDesc) {
		this.indicatorDesc = indicatorDesc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPanelName() {
		return panelName;
	}

	public void setPanelName(String panelName) {
		this.panelName = panelName;
	}
}

