package com.afunms.config.model;

import com.afunms.common.base.BaseVo;

public class CompStrategy extends BaseVo{
private int id;        //����ID
private String name;   //��������
private String description;//����
private int type;       //����
private int  violateType;//����Υ������
private String  groupId;    //����������ID����
private String createBy;    //������
private String createTime;  //����ʱ��
private String lastModifiedBy;//���һ���޸���
private String lastModifiedTime;//�һ���޸�ʱ��
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public int getType() {
	return type;
}
public void setType(int type) {
	this.type = type;
}
public int getViolateType() {
	return violateType;
}
public void setViolateType(int violateType) {
	this.violateType = violateType;
}
public String getGroupId() {
	return groupId;
}
public void setGroupId(String groupId) {
	this.groupId = groupId;
}
public String getCreateBy() {
	return createBy;
}
public void setCreateBy(String createBy) {
	this.createBy = createBy;
}
public String getCreateTime() {
	return createTime;
}
public void setCreateTime(String createTime) {
	this.createTime = createTime;
}
public String getLastModifiedBy() {
	return lastModifiedBy;
}
public void setLastModifiedBy(String lastModifiedBy) {
	this.lastModifiedBy = lastModifiedBy;
}
public String getLastModifiedTime() {
	return lastModifiedTime;
}
public void setLastModifiedTime(String lastModifiedTime) {
	this.lastModifiedTime = lastModifiedTime;
}

}
