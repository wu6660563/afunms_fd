package com.afunms.realtime;
import java.util.Date;
import java.util.LinkedList;
/********************************************************
 *Title:Queue
 *Description:CPU ������_����ģ��
 *Company  dhcc
 *@author zhangcw
 * 2011-3-4 ����12:47:29
 ********************************************************
 */
public class Queue {
	private int LENGTH=30;
	private LinkedList<DataModel> list=new LinkedList<DataModel>();
	private boolean isInited=false;//�Ƿ��ʼ����
	private boolean isDataList=false;//list���ֵ�Ƿ�����ʵ����ֵ
	/**
	 * �������
	 * @param dataModel
	 */
	public void enqueue(DataModel dataModel)
	 {
		if(list.size()==0){
			 init();
			 enqueue(dataModel);
		 }else if(list.size()<LENGTH){
			 list.addLast(dataModel);
		 }else if(list.size()==LENGTH){
			list.removeFirst();
			list.addLast(dataModel);
		 }
	 }
	/**
	 * ��ʼ������
	 */
	 @SuppressWarnings("deprecation")
	public void init(){
		 this.list.clear();
		 for(int i=0;i<LENGTH;i++){
			 DataModel dm=new DataModel();
			 dm.setData(null); 
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
			 DataModel dm=new DataModel();
			 dm.setData(null); 
			 //����ֱ�� dm.setDate(new Date());
			 Date date=new Date();
			 date.setSeconds((i*5)%60);
			 dm.setDate(date);
			 list.addLast(dm);
		 }
		 DataModel dm=new DataModel();
		 dm.setData(d); 
		 //����ֱ�� dm.setDate(new Date());
		 Date date=new Date();
		 dm.setDate(date);
		 list.addLast(dm);
		 this.setInited(true);
	 }
	public int getLENGTH() {
		return LENGTH;
	}
	public void setLENGTH(int length) {
		LENGTH = length;
	}
	public LinkedList<DataModel> getList() {
		return list;
	}
	public void setList(LinkedList<DataModel> list) {
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
