/**
 * <p>Description:��Ҫ����UPS�Ϳյ�</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project ��³ʯ��
 * @date 2007-1-25
 */

package com.afunms.security.util;

public class MachineProtectHelper
{
	/**
	 * �ڵ�״̬����
	 */
    public static String getUPSStatusDescr(int status)
    {
        String descr = null; 
    	if(status==1)
    	   descr = "����";
		else if(status==2)	//0,1,2,3����һ��Ľڵ㱣��һ��		
		   descr = "�豸æ";
		else if(status==3)			
		   descr = "Ping��ͨ";  
		else if(status==4)
		   descr = "UPS�й���";
		else if(status==5)
		   descr = "�е�ϵ�,�������";
		else //0
		   descr = "��������";
    	return descr;
    }
    
	/**
	 * �ڵ�״̬��־
	 */
    public static String getStatusImage(int status)
    {
        String image = null; 
    	if(status==1)
    	   image = "status5.png";
		else if(status==2)
		   image = "status2.png";     	
		else if(status==3)
		   image = "status4.png"; 
		else if(status==4)
		   image = "status1.png";
		else if(status==5)
		   image = "status3.png";
		else
		   image = "status6.png";	
    	return "image/topo/" + image;
    }
}