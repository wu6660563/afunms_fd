package com.gathertask;

import java.util.Hashtable;

import com.afunms.indicators.model.NodeGatherIndicators;


/**
 * 
 * ��ʱ�����߳̽ӿ�
 * @author konglq
 *
 */
public interface BaskRunerInterface {
	/**
	 * 
	 * ���ж�ʱ����ķ�������ʵ�ִ˷���
	 * �ڴ˷�����ʵ������Ҫ��������
	 * 
	 */
	public Hashtable collect_Data(NodeGatherIndicators alarmIndicatorsNode);
	
	
	

}
