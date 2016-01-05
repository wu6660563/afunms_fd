/**
 * <p>Description:base task</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-09-24
 */

package com.afunms.polling.base;

import com.afunms.common.util.SysLogger;

public abstract class BaseTask implements Runnable
{	
	protected String descr;	
	public BaseTask()
	{		
	}	

	public void setDescr(String descr)
	{
	    this.descr = descr;	
	}

	public void run()
	{
		if(timeRestricted())
		{
		    SysLogger.info("[" + descr + "]����ʼ");
		    executeTask();
		    SysLogger.info("[" + descr + "]�������");
		}
	}
		
	/**
	 * ʱ������,����ɸ����������
	 */
	public boolean timeRestricted()
	{
		return true;
	}
	
	public abstract void executeTask();
}