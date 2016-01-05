/**
 * <p>Description:Alarm Manager</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-10-13
 */

package com.afunms.location.manage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.afunms.common.base.*;
import com.afunms.common.util.SessionConstant;
import com.afunms.config.model.Nodediskconfig;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.location.dao.FvsdReportLocationNodeDao;
import com.afunms.location.model.FvsdReportLocation;
import com.afunms.location.model.FvsdReportLocationNode;
import com.afunms.location.service.FvsdReportLocationService;
import com.afunms.system.model.User;

public class FvsdReportLocationManager extends BaseManager implements ManagerInterface {

    public String execute(String action) {
        if ("list".equals(action)) {
            return list();
        } else if ("chooseLocation".equals(action)) {
            return chooseLocation();
        }
        return null;
    }
    
    public String list() {
        String type = "-1";
        String subtype="-1";
        String ipaddress = getParaValue("ipaddress");
        User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
        String bid = user.getBusinessids();
        NodeUtil nodeUtil = new NodeUtil();
        /**
         * 判断是超级用户还是正常用户
         */
        boolean result = false;
        if (user.getRole() == 0) {
            result = true;
        }
        else if (bid == null || bid.trim().length() == 0) {
            result = false;
        }
        else {
            /*
             * 设定正常用户在权限范围内可访问的设备
             */
            nodeUtil.setBid(bid);
            result = true;
        }
        List<BaseVo> baseVolist =  nodeUtil.getNodeByTyeAndSubtype(type, subtype);
        List<NodeDTO> list = nodeUtil.conversionToNodeDTO(baseVolist);
        List<NodeDTO> list2 = new ArrayList<NodeDTO>();
        if (list != null) {
            if (ipaddress != null && ipaddress.trim().length() > 0) {
                for (NodeDTO nodeDTO : list) {
                    if (nodeDTO.getIpaddress().indexOf(ipaddress) >= 0) {
                        list2.add(nodeDTO);
                    }
                }
                list = list2;
            }
        } else {
            list = list2;
        }
        FvsdReportLocationNodeDao dao = new FvsdReportLocationNodeDao();
        List<FvsdReportLocationNode> fvsdReportLocationNodeList = null;
        try {
            fvsdReportLocationNodeList = (List<FvsdReportLocationNode>)dao.loadAll();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        Hashtable<String, FvsdReportLocationNode> fvsdReportLocationNodeHashtable = new Hashtable<String, FvsdReportLocationNode>();
        if (fvsdReportLocationNodeList != null) {
            for (FvsdReportLocationNode fvsdReportLocationNode : fvsdReportLocationNodeList) {
                String key = fvsdReportLocationNode.getNodeid() + ":" + fvsdReportLocationNode.getType() + ":" + fvsdReportLocationNode.getSubtype();
                fvsdReportLocationNodeHashtable.put(key, fvsdReportLocationNode);
            }
        }
        request.setAttribute("list", list);
        request.setAttribute("fvsdReportLocationNodeHashtable", fvsdReportLocationNodeHashtable);
        return "/topology/location/list.jsp";
    }

    public String chooseLocation() {
        List<FvsdReportLocation> list = FvsdReportLocationService.getInstance().getAllFvsdReportLoaction();
        request.setAttribute("list", list);
        return "/topology/location/chooseLocation.jsp";
    }
 
    public String update() {
        return list();
    }
    
    public List<FvsdReportLocationNode> update(String fvsdLocationId, FvsdReportLocationNode[] fvsdReportLocationNodes) {
        List<FvsdReportLocationNode> addList = new ArrayList<FvsdReportLocationNode>();
        List<FvsdReportLocationNode> updateList = new ArrayList<FvsdReportLocationNode>();
        List<FvsdReportLocationNode> allList = new ArrayList<FvsdReportLocationNode>();
        if (fvsdReportLocationNodes != null) {
            for (FvsdReportLocationNode fvsdReportLocationNode : fvsdReportLocationNodes) {
                if ("-1".equals(fvsdReportLocationNode.getId())) {
                    addList.add(fvsdReportLocationNode);
                } else {
                    updateList.add(fvsdReportLocationNode);
                }
                fvsdReportLocationNode.setFvsdReportLocationId(fvsdLocationId);
                FvsdReportLocation fvsdReportLocation = FvsdReportLocationService.getInstance().getFvsdReportLoaction(fvsdLocationId);
                fvsdReportLocationNode.setDescr(fvsdReportLocation.getDescr());
                allList.add(fvsdReportLocationNode);
            }
        }
        FvsdReportLocationNodeDao dao = new FvsdReportLocationNodeDao();
        try {
            dao.save(addList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        dao = new FvsdReportLocationNodeDao();
        try {
            dao.update(updateList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        return allList;
    }
}