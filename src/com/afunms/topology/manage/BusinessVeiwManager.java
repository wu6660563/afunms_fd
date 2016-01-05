/**
 * <p>Description:DiscoverManager</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-12
 */

package com.afunms.topology.manage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.afunms.alarm.service.NodeAlarmService;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.SessionConstant;
import com.afunms.flex.networkTopology.NetworkMonitor;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.system.model.User;
import com.afunms.topology.dao.ManageXmlDao;
import com.afunms.topology.dao.NodeDependDao;
import com.afunms.topology.dao.TreeNodeDao;
import com.afunms.topology.model.ManageXml;
import com.afunms.topology.model.NodeDepend;
import com.afunms.topology.model.TreeNode;


/**
 * 增加一个节点，要在server.jsp增加一个节点;
 * 删除或更新一个节点信息，在这则不直接操作server.jsp,而是让任务updateXml来更新xml.
 */
public class BusinessVeiwManager extends BaseManager implements ManagerInterface {

	public String execute(String action) {
		// TODO Auto-generated method stub
		
		if("list".equals(action)){
			return list();
		} else if("showViewNode".equals(action)){
			return showViewNode();
		}
		
		return null;
	}
	
	private String list(){
		User user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
		NetworkMonitor networkMonitor = new NetworkMonitor();
		Hashtable<ManageXml, List<NodeDTO>> nodeDependListHashtable = networkMonitor.getBussinessviewHash(user);
		List<ManageXml> list = new ArrayList<ManageXml>();
		Iterator<ManageXml> iterator = nodeDependListHashtable.keySet().iterator();
		while (iterator.hasNext()) {
            ManageXml manageXml = iterator.next();
            list.add(manageXml);
        }
		request.setAttribute("nodeDependListHashtable", nodeDependListHashtable);
		request.setAttribute("list", list);
		request.setAttribute("bid", user.getBusinessids());
		return "/performance/businessview/list.jsp";
	}
	
	private String showViewNode(){
		String jsp = "/performance/businessview/showviewnode.jsp";
		// 视图ID
		String viewId = request.getParameter("viewId");
		ManageXml manageXml = null;
		ManageXmlDao manageXmlDao = new ManageXmlDao();
		try {
			manageXml = (ManageXml)manageXmlDao.findByID(viewId);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			manageXmlDao.close();
		}
		if(manageXml == null){
			return list();
		}
		
		
		NodeDependDao nodeDependDao = new NodeDependDao(); 
		List<NodeDepend> list = null;
		try {
			list = nodeDependDao.findByXml(manageXml.getXmlName());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			nodeDependDao.close();
		}
		
		NodeAlarmService nodeAlarmService = new NodeAlarmService();
		List<NodeDTO> nodeDTOList = new ArrayList<NodeDTO>();
		List<Node> nodeList = new ArrayList<Node>();
		Hashtable<NodeDTO, String> nodeTagHash = new Hashtable<NodeDTO, String>();
		Hashtable<NodeDTO, String> nodeAlarmLevelHash = new Hashtable<NodeDTO, String>();
		if(list!=null){
			for(int i = 0 ; i < list.size(); i++){
				NodeDepend nodeDepend = list.get(i);
				String nodeId = nodeDepend.getNodeId();
				String nodeTag = nodeId.substring(0, 3);
				String node_id = nodeId.substring(3);
				NodeUtil nodeUtil = new NodeUtil();
				List<BaseVo> baseVolist = nodeUtil.getByNodeTag(nodeTag, null);
				List<NodeDTO> AllNodeDTOList = nodeUtil.conversionToNodeDTO(baseVolist);
				if(AllNodeDTOList != null){
					for (NodeDTO nodeDTO : AllNodeDTOList) {
						if(nodeDTO.getNodeid().equalsIgnoreCase(node_id)){
						    nodeTagHash.put(nodeDTO, nodeId);
						    nodeAlarmLevelHash.put(nodeDTO, String.valueOf(nodeAlarmService.getMaxAlarmLevel(nodeDTO)));
							nodeDTOList.add(nodeDTO);
						}
					}
				}
			}
		}
		
		request.setAttribute("manageXml", manageXml);
		request.setAttribute("nodeList", nodeList);
		request.setAttribute("list", nodeDTOList);
		request.setAttribute("nodeTagHash", nodeTagHash);
		request.setAttribute("nodeAlarmLevelHash", nodeAlarmLevelHash);
		return jsp;
	}
    
}
