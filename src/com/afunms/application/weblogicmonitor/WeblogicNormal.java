package com.afunms.application.weblogicmonitor;

public class WeblogicNormal {
	String domainName = null;//�����
	String domainActive = null;	//WEBLOGIC��ǰ��Ļ״̬
	String domainAdministrationPort = null;	//����˿�
	String domainConfigurationVersion = null;	//	�汾

	
	
	
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public String getDomainActive() {
		return domainActive;
	}
	public void setDomainActive(String domainActive) {
		this.domainActive = domainActive;
	}
	public String getDomainAdministrationPort() {
		return domainAdministrationPort;
	}
	public void setDomainAdministrationPort(String domainAdministrationPort) {
		this.domainAdministrationPort = domainAdministrationPort;
	}
	public String getDomainConfigurationVersion() {
		return domainConfigurationVersion;
	}
	public void setDomainConfigurationVersion(String domainConfigurationVersion) {
		this.domainConfigurationVersion = domainConfigurationVersion;
	}
	
}
