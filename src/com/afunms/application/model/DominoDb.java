/**
 * <p>Description:mapping app_db_node</p>
 * <p>Company: dhcc.com</p>
 * @author miiwill
 * @project afunms
 * @date 2007-1-7
 */

package com.afunms.application.model;

import com.afunms.common.base.BaseVo;

public class DominoDb extends BaseVo{
	private String dbBuffPoolMax = "";//����ص����ֵ��MB��
	private String dbBuffPoolPeak = "";//����صĸ߷�ֵ��MB��
	private String dbBuffPoolReads = "";//����ض�ȡ����
	private String dbBuffPoolWrites = "";//�����д�����
	private String dbBuffPoolReadHit = "";//����ض����������ʣ�����
	private String dbCacheEntry = "";//���ٻ����е���Ŀ��
	private String dbCacheWaterMark = "";//���ٻ���ĸ�ˮӡ
	private String dbCacheHit = "";//���ٻ������д���
	private String dbCacheDbOpen = "";//���ٻ�����ɵ����ݿ�򿪲�������
	private String dbNifPoolPeak = "";//NIF�ط�ֵ��Ŀ
	private String dbNifPoolUse = "";//NIF����ʹ����Ŀ
	private String dbNsfPoolPeak = "";//NSF�ط�ֵ��Ŀ
	private String dbNsfPoolUse = "";//NSF����ʹ����Ŀ
	
	public DominoDb(){
		dbBuffPoolMax = "";//����ص����ֵ��MB��
		dbBuffPoolPeak = "";//����صĸ߷�ֵ��MB��
		dbBuffPoolReads = "";//����ض�ȡ����
		dbBuffPoolWrites = "";//�����д�����
		dbBuffPoolReadHit = "";//����ض����������ʣ�����
		dbCacheEntry = "";//���ٻ����е���Ŀ��
		dbCacheWaterMark = "";//���ٻ���ĸ�ˮӡ
		dbCacheHit = "";//���ٻ������д���
		dbCacheDbOpen = "";//���ٻ�����ɵ����ݿ�򿪲�������
		dbNifPoolPeak = "";//NIF�ط�ֵ��Ŀ
		dbNifPoolUse = "";//NIF����ʹ����Ŀ
		dbNsfPoolPeak = "";//NSF�ط�ֵ��Ŀ
		dbNsfPoolUse = "";//NSF����ʹ����Ŀ		
	}
	public String getDbBuffPoolMax() {
		return dbBuffPoolMax;
	}
	public void setDbBuffPoolMax(String dbBuffPoolMax) {
		this.dbBuffPoolMax = dbBuffPoolMax;
	}
	public String getDbBuffPoolPeak() {
		return dbBuffPoolPeak;
	}
	public void setDbBuffPoolPeak(String dbBuffPoolPeak) {
		this.dbBuffPoolPeak = dbBuffPoolPeak;
	}
	public String getDbBuffPoolReadHit() {
		return dbBuffPoolReadHit;
	}
	public void setDbBuffPoolReadHit(String dbBuffPoolReadHit) {
		this.dbBuffPoolReadHit = dbBuffPoolReadHit;
	}
	public String getDbBuffPoolReads() {
		return dbBuffPoolReads;
	}
	public void setDbBuffPoolReads(String dbBuffPoolReads) {
		this.dbBuffPoolReads = dbBuffPoolReads;
	}
	public String getDbBuffPoolWrites() {
		return dbBuffPoolWrites;
	}
	public void setDbBuffPoolWrites(String dbBuffPoolWrites) {
		this.dbBuffPoolWrites = dbBuffPoolWrites;
	}
	public String getDbCacheDbOpen() {
		return dbCacheDbOpen;
	}
	public void setDbCacheDbOpen(String dbCacheDbOpen) {
		this.dbCacheDbOpen = dbCacheDbOpen;
	}
	public String getDbCacheEntry() {
		return dbCacheEntry;
	}
	public void setDbCacheEntry(String dbCacheEntry) {
		this.dbCacheEntry = dbCacheEntry;
	}
	public String getDbCacheHit() {
		return dbCacheHit;
	}
	public void setDbCacheHit(String dbCacheHit) {
		this.dbCacheHit = dbCacheHit;
	}
	public String getDbCacheWaterMark() {
		return dbCacheWaterMark;
	}
	public void setDbCacheWaterMark(String dbCacheWaterMark) {
		this.dbCacheWaterMark = dbCacheWaterMark;
	}
	public String getDbNifPoolPeak() {
		return dbNifPoolPeak;
	}
	public void setDbNifPoolPeak(String dbNifPoolPeak) {
		this.dbNifPoolPeak = dbNifPoolPeak;
	}
	public String getDbNifPoolUse() {
		return dbNifPoolUse;
	}
	public void setDbNifPoolUse(String dbNifPoolUse) {
		this.dbNifPoolUse = dbNifPoolUse;
	}
	public String getDbNsfPoolPeak() {
		return dbNsfPoolPeak;
	}
	public void setDbNsfPoolPeak(String dbNsfPoolPeak) {
		this.dbNsfPoolPeak = dbNsfPoolPeak;
	}
	public String getDbNsfPoolUse() {
		return dbNsfPoolUse;
	}
	public void setDbNsfPoolUse(String dbNsfPoolUse) {
		this.dbNsfPoolUse = dbNsfPoolUse;
	}

}