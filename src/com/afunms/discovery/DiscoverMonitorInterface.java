/**
 * <p>Description:discovery process monitor</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-13
 */

package com.afunms.discovery;

/**
 * �������˷���,����������
 */
public interface DiscoverMonitorInterface
{	
	/**
	 * ��ʼʱ��
	 */
	public String getStartTime();	

	/**
	 * ����ʱ��
	 */
	public String getEndTime();
	
	/**
	 * ����ǰ,�ܺ�ʱ
	 */
	public String getElapseTime();
	
	/**
	 * ����������
	 */
	public int getSubNetTotal();
	
	/**
	 * �豸������
	 */
	public int getHostTotal();

	/**
	 * �ѷ������豸������
	 */
	public int getDiscoveredNodeTotal();
			
	/**
	 * ��ǰ���ֽ����ͳ�Ʊ�
	 */
	public String getResultTable();			
	
	/**
	 * �����������?
	 */
	public boolean isCompleted();
}