/**
 * <p>Description: �Ž�ϵͳ��¼</p>
 * <p>Company: dhcc.com</p>
 * @author miiwill
 * @project ��³ʯ��
 * @date 2007-01-18
 */

package com.afunms.security.model;

import com.afunms.common.base.BaseVo;

public class GateRecord extends BaseVo
{
	private String person; //����
	private String event;  //�¼�
    private String logTime; //����ʱ��(��������)
    private String io;  //1=����,0=����
    
	public String getEvent() 
	{
		return event;
	}
	
	public void setEvent(String event) 
	{
		this.event = event;
	}
	
	public String getIo() 
	{
		return io;
	}
	
	public void setIo(String io) 
	{
		this.io = io;
	}
	
	public String getLogTime() 
	{
		return logTime;
	}
	
	public void setLogTime(String logTime) 
	{
		this.logTime = logTime;
	}
	
	public String getPerson() 
	{
		return person;
	}
	
	public void setPerson(String person) 
	{
		this.person = person;
	}
}    
    
   