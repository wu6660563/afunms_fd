package com.gatherdb;

import java.util.Hashtable;
import java.util.Timer;

import com.afunms.indicators.model.NodeGatherIndicators;



/**
 * ����Ϊ��̬������������Ҫ���ڴ��е����ݶ������ڴ����ж���
 * ���Ҷ���ı�����Ҫ����ϸ��ע�ͣ�����key��ʲô�����汣�����ʲô���͵ı���
 * 
 * 
 * @author konglq
 *
 */
public class nmsmemorydate {
	
	//�������涨ʱ���������IDΪKEY��VALUE��TIMER����
	public static Hashtable <String,Timer> TaskList= new Hashtable();
	/** 
	 *���еĲɼ���������б�key��nodeid��value Ϊnms_host_node ���ݿ��
	 *��hashtable�ķ�ʽ����
	 */	
	
	/**
	 * 
	 * �ڴ��б��ֲɼ���������Ϣ
	 * nms_gather_indicators_node
	 * idΪkey
	 * value Ϊ���ݿ��¼
	 * 
	 */
     public static Hashtable<String,NodeGatherIndicators> RunGatherLinst=new Hashtable();
     
     //��ʱά��������
     public static boolean MaintainTaskStatus=false;
     //ά���������
     public static Timer MaintainTasktimer= null;
     
     //��ʱ�������
     public static boolean GathersqlTaskStatus=false;
     //��ʱ����������
     public static Timer GathersqlTasktimer= null;
     //���ݷ���ģʽ�������
     public static Timer GatherDatatempsqlTasktimer= null;
     
     
     
    
     
}
