/*
 * @(#)TopoService.java     v1.01, 2013 10 29
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.topology.service;


import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

import com.afunms.alarm.service.NodeAlarmService;
import com.afunms.common.base.BaseVo;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.topology.dao.ManageXmlDao;
import com.afunms.topology.model.ManageXml;
import com.afunms.topology.util.XmlOperator;

/**
 * ClassName:   TopoService.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 10 29 15:23:55
 */
public class TopoService {

    public boolean isAlarm(ManageXml manageXml) {
        String xmlName = manageXml.getXmlName();
        System.out.println(xmlName);
        XmlOperator xmlOpr = new XmlOperator();
        xmlOpr.setFile(xmlName);
        xmlOpr.init4updateXml();
        List<Element> list = xmlOpr.getAllNodes();
        NodeUtil nodeUtil = new NodeUtil();
        for (Element element : list) {
            String id = element.getChildText("id");
            String relationMap = element.getChildText("relationMap");
            if (id.indexOf("hin") >= 0) {
                // 示意设备 暂不考虑.
                if (relationMap != null) {
                    ManageXml xml = null;
                    ManageXmlDao dao = new ManageXmlDao();
                    try {
                        xml = (ManageXml) dao.findByXml(relationMap);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    } finally {
                        dao.close();
                    }
                    if (xml != null) {
                        if (isAlarm(xml)) {
                            return true;
                        }
                    }
                }
                continue;
            }
            
            String nodeId = id.replaceAll("node_", "");
            String nodeTag = nodeId.substring(0, 3);
            String nodeid = nodeId.substring(3);
            
            List<BaseVo> baseVolist = nodeUtil.getByNodeTag(nodeTag, null);

            for (BaseVo baseVo : baseVolist) {
                NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(baseVo);
                if (nodeid.equals(nodeDTO.getNodeid())) {
                    NodeAlarmService nodeAlarmService = new NodeAlarmService();
                    if (nodeAlarmService.getMaxAlarmLevel(nodeDTO) > 0) {
                        return true;
                    }
                    break;
                }
            }
        }
        return false;
    }
}

