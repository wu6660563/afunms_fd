package com.afunms.config.model;

import com.afunms.common.base.BaseVo;

//����
public class CompRule extends BaseVo{
private int id;//����ID
private String device_type;//�豸����(h3c,cisco)
private String  comprule_name;//��������
private String description;//��������
private int violation_severity;//Υ�����ض�(0-����,1-��Ҫ,2-����
private int select_type;//ѡ���׼(0-�򵥣�1-�߼���2-�߼��Զ���)
private String remediation_descr;//��������
private String created_by;//������
private String create_time;//����ʱ��
private String last_modified_by;//���һ���޸���
private String last_modified_time;//���һ���޸�ʱ��
private String rule_content;//��������
//private String beiyong1;
//private String beiyong2;

public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}

public String getComprule_name() {
	return comprule_name;
}
public void setComprule_name(String comprule_name) {
	this.comprule_name = comprule_name;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}


public int getViolation_severity() {
	return violation_severity;
}
public void setViolation_severity(int violation_severity) {
	this.violation_severity = violation_severity;
}
public int getSelect_type() {
	return select_type;
}
public void setSelect_type(int select_type) {
	this.select_type = select_type;
}
public String getRemediation_descr() {
	return remediation_descr;
}
public void setRemediation_descr(String remediation_descr) {
	this.remediation_descr = remediation_descr;
}
public String getDevice_type() {
	return device_type;
}
public void setDevice_type(String device_type) {
	this.device_type = device_type;
}

public String getCreated_by() {
	return created_by;
}
public void setCreated_by(String created_by) {
	this.created_by = created_by;
}
public String getCreate_time() {
	return create_time;
}
public void setCreate_time(String create_time) {
	this.create_time = create_time;
}
public String getLast_modified_by() {
	return last_modified_by;
}
public void setLast_modified_by(String last_modified_by) {
	this.last_modified_by = last_modified_by;
}
public String getLast_modified_time() {
	return last_modified_time;
}
public void setLast_modified_time(String last_modified_time) {
	this.last_modified_time = last_modified_time;
}
public String getRule_content() {
	return rule_content;
}
public void setRule_content(String rule_content) {
	this.rule_content = rule_content;
}

}
