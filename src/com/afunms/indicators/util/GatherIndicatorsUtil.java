package com.afunms.indicators.util;

import java.util.ArrayList;
import java.util.List;

import com.afunms.indicators.dao.GatherIndicatorsDao;
import com.afunms.indicators.model.GatherIndicators;


/**
 * ����Ϊ ���豸���ʱ ���豸�������Ĭ�ϵؼ��ָ��
 * @author Administrator
 *
 */

public class GatherIndicatorsUtil {
	
//	/**
//	 * ͨ�� �豸���� �� �����ͺ��豸id ���Ĭ�ϼ��ָ��
//	 * @param nodeid
//	 * @param type
//	 * @param subtype
//	 */
//	public void addIndicatorsForNode(String nodeid , String type , String subtype){
//		
//		
//	}
	
	/**
	 * ͨ������ �� ������ ����ȡĬ�ϼ��ָ���б�
	 * @param type
	 * @param subtype
	 * @return
	 */
	public List<GatherIndicators> getGatherIndicatorsByTypeAndSubtype(String type , String subtype){
		List<GatherIndicators> list = null;
		GatherIndicatorsDao gatherIndicatorsDao = new GatherIndicatorsDao();
		try {
			list = gatherIndicatorsDao.getByTypeAndSubtype(type , subtype);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			gatherIndicatorsDao.close();
		}
		return list;
	}
	
	
	
	/**
	 * ͨ������ �� ������ ����ȡĬ�ϼ��ָ���б�
	 * @param type ����
	 * @param subtype ������
	 * @param Collecttype �ɼ���ʽ
	 * @return �ɼ�ָ���б�
	 */
	public List<GatherIndicators> getGatherIndicatorsByTypeAndSubtype(String type , String subtype,String flag,int Collecttype){
		List<GatherIndicators> list = null;
		GatherIndicatorsDao gatherIndicatorsDao = new GatherIndicatorsDao();
		try {
			list = gatherIndicatorsDao.getByTypeAndSubtypeAndcollecttype(type , subtype,flag,Collecttype);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			gatherIndicatorsDao.close();
		}
		return list;
	}
	
	
	/**
	 * ͨ������ �� ������ ����ȡĬ�ϼ��ָ���б�
	 * @param type
	 * @param subtype
	 * @return
	 */
	public List<GatherIndicators> getGatherIndicatorsByTypeAndSubtype(String type , String subtype,String flag){
		List<GatherIndicators> list = null;
		GatherIndicatorsDao gatherIndicatorsDao = new GatherIndicatorsDao();
		try {
			list = gatherIndicatorsDao.getByTypeAndSubtype(type , subtype,flag);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			gatherIndicatorsDao.close();
		}
		return list;
	}
	
	/**
	 * ͨ������ �� ������,�Ƿ�Ĭ��,ָ������ ����ȡĬ�ϼ��ָ���б�
	 * @param type
	 * @param subtype
	 * @param flag
	 * @param indename
	 * @return
	 */
	public List<GatherIndicators> getGatherIndicatorsByTypeAndSubtype(String type , String subtype,String flag,String indename){
		List<GatherIndicators> list = null;
		GatherIndicatorsDao gatherIndicatorsDao = new GatherIndicatorsDao();
		try {
			list = gatherIndicatorsDao.getByTypeAndSubtype(type , subtype,flag,indename);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			gatherIndicatorsDao.close();
		}
		return list;
	}
	
	
	/**
	 * ͨ������ �� ������ ����ȡĬ�ϼ��ָ���б�
	 * @param type
	 * @param subtype
	 * @return
	 */
	public List<GatherIndicators> getGatherIndicatorsByIds(String[] ids){
		List<GatherIndicators> list = new ArrayList<GatherIndicators>();
		GatherIndicatorsDao gatherIndicatorsDao = new GatherIndicatorsDao();
		try {
			for(int i = 0 ; i < ids.length ; i ++){
				GatherIndicators gatherIndicators = (GatherIndicators)gatherIndicatorsDao.findByID(ids[i]);
				list.add(gatherIndicators);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			gatherIndicatorsDao.close();
		}
		return list;
	}
	
}
