package com.afunms.common.util;
import java.util.ArrayList;
import java.util.List;

public class MeterModel {

    private int picx;//ͼƬ����
    private int picy;//ͼƬ���
    private  int meterX;//�Ǳ��̿��
    private int meterY;//�Ǳ��̸߶�
    
    private String picName;//ͼƬ����
    
    private int innerRoundColor;//��Բ��ɫ
    private int outRingColor;//�⻷��ɫ
    private int bgColor;//����ɫ
    
    private int meterSize;//�Ǳ��̴�С
    
    private String title;//ͼ��
    private double value;//ֵ
    
    private int fontSize;//�����С
    
    private int outPointerColor;//ָ���ⲿ��ɫ
    private int inPointerColor;//ָ���ڲ���ɫ
    
    private int titleY;//��������ߵľ���
    private int titleTop;//�������ϱߵľ���
    
    private int valueY;//ֵ����ߵľ���
    private int valueTop;//ֵ���ϱߵľ���
        
    private List<StageColor> stagelist = new ArrayList<StageColor>();
    public List<StageColor> getList() {
        return stagelist;
    }
    public void setList(List<StageColor> stagelist) {
        this.stagelist = stagelist;
    }
    public int getPicx() {
        return picx;
    }
    public void setPicx(int picx) {
        this.picx = picx;
    }
    public int getPicy() {
        return picy;
    }
    public void setPicy(int picy) {
        this.picy = picy;
    }
    public int getMeterX() {
        return meterX;
    }
    public void setMeterX(int meterX) {
        this.meterX = meterX;
    }
    public int getMeterY() {
        return meterY;
    }
    public void setMeterY(int meterY) {
        this.meterY = meterY;
    }
    public int getInnerRoundColor() {
        return innerRoundColor;
    }
    public void setInnerRoundColor(int innerRoundColor) {
        this.innerRoundColor = innerRoundColor;
    }
    public int getOutRingColor() {
        return outRingColor;
    }
    public void setOutRingColor(int outRingColor) {
        this.outRingColor = outRingColor;
    }
    public int getBgColor() {
        return bgColor;
    }
    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public double getValue() {
        return value;
    }
    public void setValue(double value) {
        this.value = value;
    }
    public int getMeterSize() {
        return meterSize;
    }
    public void setMeterSize(int meterSize) {
        this.meterSize = meterSize;
    }
    public int getFontSize() {
        return fontSize;
    }
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }
    public int getOutPointerColor() {
        return outPointerColor;
    }
    public void setOutPointerColor(int outPointerColor) {
        this.outPointerColor = outPointerColor;
    }
    public int getInPointerColor() {
        return inPointerColor;
    }
    public void setInPointerColor(int inPointerColor) {
        this.inPointerColor = inPointerColor;
    }
    public int getTitleY() {
        return titleY;
    }
    public void setTitleY(int titleY) {
        this.titleY = titleY;
    }
    public int getTitleTop() {
        return titleTop;
    }
    public void setTitleTop(int titleTop) {
        this.titleTop = titleTop;
    }
    public int getValueY() {
        return valueY;
    }
    public void setValueY(int valueY) {
        this.valueY = valueY;
    }
    public int getValueTop() {
        return valueTop;
    }
    public void setValueTop(int valueTop) {
        this.valueTop = valueTop;
    }
    public String getPicName() {
        return picName;
    }
    public void setPicName(String picName) {
        this.picName = picName;
    }
    
    
    
    
}
