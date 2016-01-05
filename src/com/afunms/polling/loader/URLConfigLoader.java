/**
 * <p>Description:loading db nodes</p>
 * <p>Company: dhcc.com</p>
 * @author miiwill
 * @project afunms
 * @date 2007-1-8
 */

package com.afunms.polling.loader;

import com.afunms.polling.base.Node;
import java.util.List;

import com.afunms.polling.PollingEngine;
import com.afunms.application.dao.URLConfigDao;
import com.afunms.application.dao.WebConfigDao;
import com.afunms.application.model.URLConfig;
import com.afunms.application.model.WebConfig;
import com.afunms.polling.base.NodeLoader;
import com.afunms.polling.node.URLConfigNode;
import com.afunms.polling.node.Web;
import com.afunms.common.base.BaseVo;

public class URLConfigLoader extends NodeLoader {
	public void loading() {
		WebConfigDao dao = new WebConfigDao();
		List list = dao.loadAll();
		clearRubbish(list);
		for (int i = 0; i < list.size(); i++) {
			WebConfig vo = (WebConfig) list.get(i);
			loadOne(vo);
		}
	}

	public void clearRubbish(List baseVoList) {
		List nodeList = PollingEngine.getInstance().getWebList(); // 得到内存中的list
		for (int index = 0; index < nodeList.size(); index++) {
			if (nodeList.get(index) instanceof Web) {
				Web node = (Web) nodeList.get(index);
				if (baseVoList == null) {
					nodeList.remove(node);
				} else {
					boolean flag = false;
					for (int j = 0; j < baseVoList.size(); j++) {
						WebConfig hostNode = (WebConfig) baseVoList.get(j);
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
		URLConfig vo = (URLConfig) baseVo;
		URLConfigNode node = new URLConfigNode();
		node.setId(vo.getId());
		node.setTimeout(vo.getTimeout());
		node.setCategory(57);
		node.setStatus(0);
		node.setType("WEB访问服务");

		// ---------------加载被监视对象-------------------
		// PollingEngine.getInstance().addWeb(web);

		Node node2 = PollingEngine.getInstance().getWebByID(node.getId());
		if (node2 != null) {
			PollingEngine.getInstance().getWebList().remove(node2);
		}
		PollingEngine.getInstance().addWeb(node2);
	}

	public URLConfigNode loadOne(String id) {
	    URLConfig vo = null;
	    URLConfigDao dao = new URLConfigDao();
	    try {
	        vo = (URLConfig) dao.findByID(id);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        if (vo == null) {
            return null;
        }
        URLConfigNode node = new URLConfigNode();
        node.setId(vo.getId());
        node.setUrl(vo.getUrl());
        node.setAlias(vo.getName());
        node.setTimeout(vo.getTimeout());
        node.setCategory(57);
        node.setStatus(0);
        node.setType("URL访问服务");
        node.setIpAddress(vo.getIpaddress());

        // ---------------加载被监视对象-------------------
        // PollingEngine.getInstance().addWeb(web);

        Node node2 = PollingEngine.getInstance().getWebByID(node.getId());
        if (node2 != null) {
            PollingEngine.getInstance().getWebList().remove(node2);
        }
        PollingEngine.getInstance().addWeb(node2);
        return node;
	}
}