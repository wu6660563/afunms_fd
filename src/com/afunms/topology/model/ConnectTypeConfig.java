/**
 * <p>Description:mapping table nms_remote_ping_host</p>
 * <p>Company: dhcc.com</p>
 * @author nielin
 * @project afunms
 * @date 2010-03-16
 */

package com.afunms.topology.model;

import com.afunms.common.base.BaseVo;


public class ConnectTypeConfig extends BaseVo
{
	/**
	 * ������id
	 */
	private int id; 
	
	/**
	 * �豸id
	 */
	private String node_id;
	
	/**
	 * ��������
	 */
	private String connecttype;
	
	/**
	 * �û���
	 */
	private String username;
	
	/**
	 * ����
	 */
	private String password;
	
	/**
	 * ��½��ʾ��
	 */
	private String loginPrompt;
	
	/**
	 * ������ʾ��
	 */
	private String passwordPrompt;
	
	/**
	 * shell��ʾ��
	 */
	private String shellPrompt;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the node_id
	 */
	public String getNode_id() {
		return node_id;
	}

	/**
	 * @param node_id the node_id to set
	 */
	public void setNode_id(String node_id) {
		this.node_id = node_id;
	}

	/**
	 * @return the connecttype
	 */
	public String getConnecttype() {
		return connecttype;
	}

	/**
	 * @param connecttype the connecttype to set
	 */
	public void setConnecttype(String connecttype) {
		this.connecttype = connecttype;
	}
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param passwrod the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the loginPrompt
	 */
	public String getLoginPrompt() {
		return loginPrompt;
	}

	/**
	 * @param loginPrompt the loginPrompt to set
	 */
	public void setLoginPrompt(String loginPrompt) {
		this.loginPrompt = loginPrompt;
	}

	/**
	 * @return the passwordPrompt
	 */
	public String getPasswordPrompt() {
		return passwordPrompt;
	}

	/**
	 * @param passwrodPrompt the passwordPrompt to set
	 */
	public void setPasswordPrompt(String passwordPrompt) {
		this.passwordPrompt = passwordPrompt;
	}

	/**
	 * @return the shellPrompt
	 */
	public String getShellPrompt() {
		return shellPrompt;
	}

	/**
	 * @param shellPrompt the shellPrompt to set
	 */
	public void setShellPrompt(String shellPrompt) {
		this.shellPrompt = shellPrompt;
	}
	
}