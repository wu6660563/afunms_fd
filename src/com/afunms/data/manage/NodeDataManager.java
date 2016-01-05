/**
 * <p>Description:DiscoverManager</p>
 * <p>Company: dhcc.com</p>
 * @author nielin
 * @project afunms
 * @date 2010-12-08
 */

package com.afunms.data.manage;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.data.service.NodeDataService;
import com.afunms.indicators.util.Constant;

/**
 * �� manager ���ڽ����豸��ϸ��Ϣҳ����ת
 */
public abstract class NodeDataManager extends BaseManager implements ManagerInterface {

	protected NodeDataService nodeDataService;
	
	protected String getType() {
	    return getParaValue("type");
	}

	protected String getSubtype() {
	    return getParaValue("subtype");
	}

	protected String getNodeid() {
	    return getParaValue("nodeid");
	}

	protected NodeDataService getNodeDataService() {
	    return nodeDataService;
	}

}
