package com.afunms.config.model;

import com.afunms.common.base.BaseVo;

public class DetailCompRule extends BaseVo{
private int id;
private int ruleId;
private int relation;//��ϵ�����
private int isContain;//�Ƿ����
private String expression;//��Ҫ�ȶԵ����ݣ�������ʽ�������ݣ�
private String beginBlock;//��ʼ��
private String endBlock;//������
private int isExtraContain;//�Ƿ�������ӿ�(0:��������1��������-1����)
private String extraBlock;//���ӿ�

public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public int getRuleId() {
	return ruleId;
}
public void setRuleId(int ruleId) {
	this.ruleId = ruleId;
}

public int getRelation() {
	return relation;
}
public void setRelation(int relation) {
	this.relation = relation;
}
public int getIsContain() {
	return isContain;
}
public void setIsContain(int isContain) {
	this.isContain = isContain;
}
public void setIsExtraContain(int isExtraContain) {
	this.isExtraContain = isExtraContain;
}
public String getExpression() {
	return expression;
}
public void setExpression(String expression) {
	this.expression = expression;
}
public String getBeginBlock() {
	return beginBlock;
}
public void setBeginBlock(String beginBlock) {
	this.beginBlock = beginBlock;
}
public String getEndBlock() {
	return endBlock;
}
public void setEndBlock(String endBlock) {
	this.endBlock = endBlock;
}

public int getIsExtraContain() {
	return isExtraContain;
}
public String getExtraBlock() {
	return extraBlock;
}
public void setExtraBlock(String extraBlock) {
	this.extraBlock = extraBlock;
}

}
