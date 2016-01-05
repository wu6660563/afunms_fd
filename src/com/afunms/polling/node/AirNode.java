/**
 * <p>Description:ups node</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2011-05-04
 */

package com.afunms.polling.node;

import com.afunms.inform.model.Alarm;
import com.afunms.polling.base.Node;

public class AirNode extends Node
{
    public AirNode()
    {    
    	category = 101;
    }
    
	private String community;  //�������Ը��඼��
    private String Location; 
    private String subtype; 

    public String getCommunity() {
		return community;
	}
	public void setCommunity(String community) {
		this.community = community;
	}
	public String getLocation() {
		return Location;
	}
	public void setLocation(String location) {
		Location = location;
	}  
	
	/**
	 * ���Ǹ���ͬ������
	 */
	public String getShowMessage()
	{
		StringBuffer msg = new StringBuffer(200);
		msg.append("<font color='green'>����:"+type);
		msg.append("</font><br>����:");
		msg.append(alias);
		msg.append("<br>");
		msg.append("IP��ַ:");
		msg.append(ipAddress);
		msg.append("<br>");
//		System.out.println("managed========"+managed);
		if(!managed)
		{			
			msg.append("<font color='red'>");
			msg.append("��������</font>");
			return msg.toString();
		}	
		if(moidList!=null&&moidList.size()>0){
			
		}
				
		if(alarm) //status=4,5
		{	
		    msg.append("<font color='red'>--������Ϣ:--</font><br>");
		    for(int i=0;i<alarmMessage.size();i++)
		    {
		        msg.append(((Alarm)alarmMessage.get(i)).getMessage());
		        msg.append("<br>");
		    }
		}
		msg.append("����ʱ��:" + lastTime);
		return msg.toString();
	}
	public String getSubtype() {
		return subtype;
	}
	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}	
}