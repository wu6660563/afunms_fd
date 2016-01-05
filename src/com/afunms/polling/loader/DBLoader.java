/**
 * <p>Description:loading db nodes</p>
 * <p>Company: dhcc.com</p>
 * @author miiwill
 * @project afunms
 * @date 2007-1-8
 */

package com.afunms.polling.loader;

import com.afunms.polling.base.Node;
import java.util.ArrayList;
import java.util.List;

import com.afunms.application.dao.DBDao;
import com.afunms.application.dao.DBTypeDao;
import com.afunms.application.dao.OraclePartsDao;
import com.afunms.application.model.ApacheConfig;
import com.afunms.application.model.DBTypeVo;
import com.afunms.application.model.DBVo;
import com.afunms.application.model.OracleEntity;
import com.afunms.common.base.BaseVo;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.NodeLoader;
import com.afunms.polling.node.ApachConfig;
import com.afunms.polling.node.DBNode;
import com.afunms.topology.dao.NodeMonitorDao;

public class DBLoader extends NodeLoader {
	public void loading() {
		DBDao dao = new DBDao();
		List list = dao.getDbByMonFlag(-1);
		clearRubbish(list);
		DBTypeDao typedao = new DBTypeDao();
		DBTypeVo oracleType = null;
		try {
			oracleType = (DBTypeVo) typedao.findByDbtype("oracle");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			typedao.close();
		}
		// SysLogger.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		for (int i = 0; i < list.size(); i++) {
			DBVo vo = (DBVo) list.get(i);
			if (vo.getDbtype() == oracleType.getId()) {
				OraclePartsDao oracleDao = new OraclePartsDao();
				List<OracleEntity> oracles = oracleDao.findOracleParts(vo
						.getId());
				for (OracleEntity oracle : oracles) {
					DBVo nvo = new DBVo();
					nvo.setDbName(vo.getDbName());
					nvo.setAlias(vo.getAlias());
					nvo.setBid(oracle.getBid());
					nvo.setCategory(vo.getCategory());
					nvo.setCollecttype(vo.getCollecttype());
					nvo.setDbtype(vo.getDbtype());
					nvo.setDbuse(vo.getDbuse());
					nvo.setId(oracle.getId());
					nvo.setIpAddress(vo.getIpAddress());
					nvo.setManaged(vo.getManaged());
					nvo.setPassword(oracle.getPassword());
					nvo.setPort(vo.getPort());
					nvo.setSendemail(oracle.getGzerid());
					nvo.setSendmobiles(vo.getSendmobiles());
					nvo.setSendphone(vo.getSendphone());
					nvo.setStatus(vo.getStatus());
					nvo.setUser(oracle.getUser());
					// vo.setPassword(oracle.getPassword());
					// vo.setId(oracle.getId());
					// vo.setSendemail(oracle.getGzerid());
					// vo.setUser(oracle.getUser());
					loadOne(nvo);
				}
			} else
				loadOne(vo);
		}

	}

	public void clearRubbish(List baseVoList) {
		List nodeList = PollingEngine.getInstance().getDbList(); // 得到内存中的list
		for (int index = 0; index < nodeList.size(); index++) {
			if (nodeList.get(index) instanceof DBNode) {
				DBNode node = (DBNode) nodeList.get(index);
				if (baseVoList == null) {
					nodeList.remove(node);
				} else {
					boolean flag = false;
					for (int j = 0; j < baseVoList.size(); j++) {
						DBVo hostNode = (DBVo) baseVoList.get(j);
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
		DBVo vo = (DBVo) baseVo;
		DBNode dbNode = new DBNode();
		dbNode.setManaged(true);
		dbNode.setId(vo.getId());
		dbNode.setAlias(vo.getAlias());
		dbNode.setIpAddress(vo.getIpAddress());
		dbNode.setCategory(vo.getCategory());
		dbNode.setBid(vo.getBid());
		dbNode.setDbtype(vo.getDbtype());
		dbNode.setUser(vo.getUser());
		dbNode.setPassword(vo.getPassword());
		dbNode.setPort(vo.getPort());
		dbNode.setDbName(vo.getDbName());
		dbNode.setStatus(0);
		dbNode.setCollecttype(vo.getCollecttype());
		dbNode.setType("数据库");
		// SysLogger.info("############"+dbNode.getCollecttype());

		// ---------------加载被监视对象-------------------
		List moidList = new ArrayList(5);
		NodeMonitorDao nodeMonitorDao = null;
		List nmList = new ArrayList();
		try {
			nodeMonitorDao = getNmDao();
			nmList = nodeMonitorDao.loadByNodeID(dbNode.getId());
		} catch (Exception e) {

		} finally {
			if (nodeMonitorDao != null)
				nodeMonitorDao.close();
		}
		for (int j = 0; j < nmList.size(); j++) {
			/*
			 * NodeMonitor nm = (NodeMonitor)nmList.get(j); MonitoredItem item =
			 * MonitorFactory.createItem(nm.getMoid()) ; item.loadSelf(nm);
			 * moidList.add(item);
			 */
		} // all monitor items are loaded
		dbNode.setMoidList(moidList);
		// PollingEngine.getInstance().addNode(dbNode);
		// SysLogger.info("添加数据库====="+dbNode.getIpAddress()+"===id:"+dbNode.getId());
		// PollingEngine.getInstance().addDb(dbNode);

		 Node node=PollingEngine.getInstance().getDbByID(dbNode.getId());
		 if(node!=null){
		 PollingEngine.getInstance().getDbList().remove(node);
		 }
		PollingEngine.getInstance().addDb(dbNode);
	}
}