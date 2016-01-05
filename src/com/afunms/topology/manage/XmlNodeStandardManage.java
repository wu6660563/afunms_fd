/*
 * @(#)XmlNodeStandardManage.java     v1.01, Oct 12, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.topology.manage;

import java.util.ArrayList;
import java.util.List;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.Constant;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.topology.dao.ManageXmlDao;
import com.afunms.topology.dao.XmlNodeStandardDao;
import com.afunms.topology.model.ManageXml;
import com.afunms.topology.model.XmlNodeStandardVo;

/**
 * ClassName: XmlNodeStandardManage.java
 * <p>
 * 
 * @author 吴品龙
 * @version v1.01
 * @since v1.01
 * @Date Oct 12, 2013 11:05:24 AM
 * @mail wupinlong@dhcc.com.cn
 */
public class XmlNodeStandardManage extends BaseManager implements
		ManagerInterface {

	public String execute(String action) {
		if ("list".equals(action)) {
			return list();
		}
		if ("getTopoList".equals(action)) {
			return getTopoList();
		}
		if ("saveNodeDefaultTopo".equals(action)) {
			return saveNodeDefaultTopo();
		}
		setErrorCode(ErrorMessage.ACTION_NO_FOUND);
		return null;
	}

	public String list() {
		// 查出所有类型的设备列表
		String ipaddress = request.getParameter("ipaddress");
		
		NodeUtil nodeUtil = new NodeUtil();
		List<BaseVo> list = nodeUtil.getNodeByTyeAndSubtype(Constant.ALL_TYPE,null);
		List<NodeDTO> dtoList = nodeUtil.conversionToNodeDTO(list);
		
		List<NodeDTO> list2 = new ArrayList<NodeDTO>();
		if(ipaddress != null && !"".equals(ipaddress)) {
			for (int i = 0; i < dtoList.size(); i++) {
				if(dtoList.get(i).getIpaddress().equals(ipaddress)){
					list2.add(dtoList.get(i));
				}
			}
		} else {
			list2.addAll(dtoList);
		}

		XmlNodeStandardDao dao = null;
		try {
			dao = new XmlNodeStandardDao();
			List<XmlNodeStandardVo> xmlNodeList = dao.loadAll();
			request.setAttribute("xmlNodeList", xmlNodeList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		request.setAttribute("list", list2);
		return "/topology/network/locationtopology/list.jsp";
	}

	public String getTopoList() {
		String nodeIds = request.getParameter("nodeIds");
		String types = request.getParameter("types");
		String subtypes = request.getParameter("subtypes");
		
		ManageXmlDao dao = new ManageXmlDao();
		List<ManageXml> list = null;
		try {
			list = dao.loadAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		System.out.println(nodeIds);
		request.setAttribute("list", list);
		request.setAttribute("nodeIds", nodeIds);
		request.setAttribute("types", types);
		request.setAttribute("subtypes", subtypes);
		
		return "/topology/network/locationtopology/chooseTopo.jsp";
	}

	public String saveNodeDefaultTopo() {
		String nodeIds = request.getParameter("nodeIds");
		String types = request.getParameter("types");
		String subtypes = request.getParameter("subtypes");
		String[] node_Ids = nodeIds.split(",");
		String[] node_types = types.split(",");
		String[] node_subtypes = subtypes.split(",");

		String xmlId = request.getParameter("xmlId");
		ManageXmlDao dao = new ManageXmlDao();
		ManageXml xmlVo = null;
		try {
			xmlVo = (ManageXml) dao.findById1(Integer.parseInt(xmlId));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}

		XmlNodeStandardDao XmlNodeDao = null;
		XmlNodeStandardVo vo = null;

		NodeUtil nodeUtil = new NodeUtil();
		for (int i = 0; i < node_subtypes.length; i++) {
			List<BaseVo> list = nodeUtil.getNodeByTyeAndSubtype(node_types[i],
					node_subtypes[i]);
			List<NodeDTO> dtoList = nodeUtil.conversionToNodeDTO(list);
			for (int j = 0; j < dtoList.size(); j++) {
				NodeDTO nodeDto = dtoList.get(j);
				if (nodeDto.getNodeid().equals(node_Ids[i])) {
					try {
						XmlNodeDao = new XmlNodeStandardDao();
						vo = XmlNodeDao.getXmlNodeStandardVo(nodeDto
								.getNodeid(), nodeDto.getType(), nodeDto
								.getSubtype());
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						XmlNodeDao.close();
					}

					if (vo != null) {
						// 已经存在，修改即可
						System.out.println("修改。。。。");
						vo.setXmlName(xmlVo.getXmlName());
						vo.setTopoId(String.valueOf(xmlVo.getId()));
						vo.setTopoName(xmlVo.getTopoName());

						XmlNodeDao = new XmlNodeStandardDao();
						try {
							XmlNodeDao.update(vo);
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							XmlNodeDao.close();
						}
					} else {
						// 不存在，直接入库
						System.out.println("直接入库");
						vo = new XmlNodeStandardVo();
						vo.setNodeId(nodeDto.getNodeid());
						vo.setNodeName(nodeDto.getName());
						vo.setType(nodeDto.getType());
						vo.setSubtype(nodeDto.getSubtype());
						vo.setIpaddress(nodeDto.getSubtype());
						vo.setXmlName(xmlVo.getXmlName());
						vo.setTopoId(String.valueOf(xmlVo.getId()));
						vo.setTopoName(xmlVo.getTopoName());

						XmlNodeDao = new XmlNodeStandardDao();
						try {
							XmlNodeDao.save(vo);
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							XmlNodeDao.close();
						}
					}
					
					break;
				}
			}
		}
		return list();
	}

}
