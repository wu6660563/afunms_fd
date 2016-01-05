package com.afunms.realtime;
import java.util.Date;
import java.util.LinkedList;
/********************************************************
 *Title:DoubleDataQueue
 *Description: �ڴ����� ģ����� amcharts
 *Company  dhcc
 *@author zhangcw
 * Mar 10, 2011 11:03:00 AM
 ********************************************************
 */
public class DoubleDataQueue {
	private int LENGTH=30;
	private LinkedList<DoubleDataModel> list=new LinkedList<DoubleDataModel>();
	private boolean isInited=false;//�Ƿ��ʼ����
	private boolean isDataList=false;//list���ֵ�Ƿ�����ʵ����ֵ
	/**
	 * �������
	 * @param dataModel
	 */
	public void enqueue(DoubleDataModel doubleDataModel)
	 {
		if(list.size()==0){
			 init();
			 enqueue(doubleDataModel);
		 }else if(list.size()<LENGTH){
			 list.addLast(doubleDataModel);
		 }else if(list.size()==LENGTH){
			list.removeFirst();
			list.addLast(doubleDataModel);
		 }
	 }
	/**
	 * ��ʼ������
	 */
	 @SuppressWarnings("deprecation")
	public void init(){
		 this.list.clear();
		 for(int i=0;i<LENGTH;i++){
			 DoubleDataModel dm=new DoubleDataModel();
			 dm.setFirstData(null);
			 dm.setSecondData(null);
			 //����ֱ�� dm.setDate(new Date());
			 Date date=new Date();
			 date.setSeconds((i*5)%60);
			 dm.setDate(date);
			 list.addLast(dm);
		 }
		 this.setInited(true);
	 }
	 /**
	  * ��ʼ������,�����һ������ʼ��Ϊ d
	 */
	 @SuppressWarnings("deprecation")
	public void initWithLastData(Double d){
		 this.list.clear();
		 for(int i=0;i<LENGTH-1;i++){
				DoubleDataModel dm=new DoubleDataModel();
				dm.setFirstData(null);
				dm.setSecondData(null);
				Date date=new Date();
				date.setSeconds((i*5)%60);
				dm.setDate(date);
				list.add(dm);//ǰn-1������Ϊnull
			}
			DoubleDataModel dm=new DoubleDataModel();
			dm.setFirstData(d);
			dm.setSecondData(d);
			dm.setDate(new Date());
			list.add(dm);
		    this.setInited(true);
	 }
	public int getLENGTH() {
		return LENGTH;
	}
	public void setLENGTH(int length) {
		LENGTH = length;
	}
	public LinkedList<DoubleDataModel> getList() {
		return list;
	}
	public void setList(LinkedList<DoubleDataModel> list) {
		this.list = list;
	}
	public boolean isInited() {
		return isInited;
	}
	public void setInited(boolean isInited) {
		this.isInited = isInited;
	}
	public boolean isDataList() {
		return isDataList;
	}
	public void setDataList(boolean isDataList) {
		this.isDataList = isDataList;
	}
	
}
