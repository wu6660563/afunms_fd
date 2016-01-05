/**
 * <p>Description:node loader</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-11-28
 */

package com.afunms.polling.base;

import com.afunms.common.base.BaseVo;
import com.afunms.topology.dao.NodeMonitorDao;

public abstract class NodeLoader
{
    //private NodeMonitorDao nmDao;  
    
	public NodeLoader()
	{
		//nmDao = new NodeMonitorDao();
	}
	
	/**
	 * �������нڵ�
	 */
    public abstract void loading();
    
	/**
	 * ����һ���ڵ�
	 */
    public abstract void loadOne(BaseVo vo); 
    
    public NodeMonitorDao getNmDao()
    {
    	NodeMonitorDao nmDao = new NodeMonitorDao();
    	return nmDao;
    }
    
    public void close()
    {
    	//nmDao.close();
    }
}