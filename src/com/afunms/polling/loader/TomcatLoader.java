/**
 * <p>Description:loading host node</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-11-28
 */

package com.afunms.polling.loader;

import com.afunms.polling.base.Node;
import java.util.ArrayList;
import java.util.List;

import com.afunms.common.base.BaseVo;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.monitor.executor.base.MonitorFactory;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.NodeLoader;
import com.afunms.polling.node.Host;
import com.afunms.application.dao.TomcatDao;
import com.afunms.application.model.Tomcat;

import com.afunms.topology.dao.NodeMonitorDao;
import com.afunms.topology.model.HostNode;
import com.afunms.topology.model.NodeMonitor;

public class TomcatLoader extends NodeLoader {
	public void loading() {
		TomcatDao dao = new TomcatDao();
		List list = dao.loadAll();
		clearRubbish(list);
		for (int i = 0; i < list.size(); i++) {
			Tomcat vo = (Tomcat) list.get(i);
			loadOne(vo);
		}
		close();
	}

	public void clearRubbish(List baseVoList) {
		List nodeList = PollingEngine.getInstance().getTomcatList(); // �õ��ڴ��е�list
		for (int index = 0; index < nodeList.size(); index++) {
			if (nodeList.get(index) instanceof com.afunms.polling.node.Tomcat) {
				com.afunms.polling.node.Tomcat node = (com.afunms.polling.node.Tomcat) nodeList.get(index);
				if (baseVoList == null) {
					nodeList.remove(node);
				} else {
					boolean flag = false;
					for (int j = 0; j < baseVoList.size(); j++) {
						com.afunms.application.model.Tomcat hostNode = (com.afunms.application.model.Tomcat) baseVoList.get(j);
						if (node.getId() == hostNode.getId()) {
							flag = true;
						}
					}
					if (!flag) {
						nodeList.remove(node);
					}
				}
			}
		}
	}

	public void loadOne(BaseVo baseVo) {
		com.afunms.application.model.Tomcat vo = (com.afunms.application.model.Tomcat) baseVo;
		com.afunms.polling.node.Tomcat tomcat = new com.afunms.polling.node.Tomcat();
		tomcat.setId(vo.getId());
		tomcat.setUser(vo.getUser());
		tomcat.setPassword(vo.getPassword());
		tomcat.setAlias(vo.getName());
		tomcat.setIpAddress(vo.getIpAddress());
		tomcat.setPort(vo.getPort());
		tomcat.setManaged(true);
		tomcat.setVersion(vo.getVersion());
		tomcat.setJvmversion(vo.getJvmversion());
		tomcat.setJvmvender(vo.getJvmvender());
		tomcat.setOs(vo.getOs());
		tomcat.setOsversion(vo.getOsversion());
		tomcat.setStatus(0);
		tomcat.setBid(vo.getBid());
		tomcat.setType("Tomcat�м��");
		// ---------------(3)���ر����Ӷ���-------------------
		List moidList = new ArrayList(5);

		List nmList = new ArrayList();
		NodeMonitorDao nodeMonitorDao = getNmDao();
		try {
			nmList = nodeMonitorDao.loadByNodeID(tomcat.getId());
		} catch (Exception e) {

		} finally {
			nodeMonitorDao.close();
		}
		for (int j = 0; j < nmList.size(); j++) {
			NodeMonitor nm = (NodeMonitor) nmList.get(j);
			MonitoredItem item = MonitorFactory.createItem(nm.getMoid());
			item.loadSelf(nm);
			moidList.add(item);
		} // all monitor items are loaded
		tomcat.setMoidList(moidList);
		// PollingEngine.getInstance().addTomcat(tomcat);

		Node node = PollingEngine.getInstance().getTomcatByID(tomcat.getId());
		if (node != null) {
			PollingEngine.getInstance().getTomcatList().remove(node);
		}
		PollingEngine.getInstance().addTomcat(tomcat);
	}

	public com.afunms.polling.node.Tomcat loadOneByID(String id) {
	    com.afunms.application.model.Tomcat vo = null;
	    TomcatDao dao = new TomcatDao();
	    try {
	        vo = (com.afunms.application.model.Tomcat) dao.findByID(id);
        } catch (RuntimeException e1) {
            e1.printStackTrace();
        } finally {
            dao.close();
        }
        if (vo == null) {
            return null;
        }
        com.afunms.polling.node.Tomcat tomcat = new com.afunms.polling.node.Tomcat();
        tomcat.setId(vo.getId());
        tomcat.setUser(vo.getUser());
        tomcat.setPassword(vo.getPassword());
        tomcat.setAlias(vo.getName());
        tomcat.setIpAddress(vo.getIpAddress());
        tomcat.setPort(vo.getPort());
        tomcat.setManaged(true);
        tomcat.setVersion(vo.getVersion());
        tomcat.setJvmversion(vo.getJvmversion());
        tomcat.setJvmvender(vo.getJvmvender());
        tomcat.setOs(vo.getOs());
        tomcat.setOsversion(vo.getOsversion());
        tomcat.setStatus(0);
        tomcat.setBid(vo.getBid());
        tomcat.setType("Tomcat�м��");
        // ---------------(3)���ر����Ӷ���-------------------
        List moidList = new ArrayList(5);

        List nmList = new ArrayList();
        NodeMonitorDao nodeMonitorDao = getNmDao();
        try {
            nmList = nodeMonitorDao.loadByNodeID(tomcat.getId());
        } catch (Exception e) {

        } finally {
            nodeMonitorDao.close();
        }
        for (int j = 0; j < nmList.size(); j++) {
            NodeMonitor nm = (NodeMonitor) nmList.get(j);
            MonitoredItem item = MonitorFactory.createItem(nm.getMoid());
            item.loadSelf(nm);
            moidList.add(item);
        } // all monitor items are loaded
        tomcat.setMoidList(moidList);
        // PollingEngine.getInstance().addTomcat(tomcat);

        Node node = PollingEngine.getInstance().getTomcatByID(tomcat.getId());
        if (node != null) {
            PollingEngine.getInstance().getTomcatList().remove(node);
        }
        PollingEngine.getInstance().addTomcat(tomcat);
        return tomcat;
    }
}