/*
 * @(#)NodeService.java     v1.01, Dec 22, 2012
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.afunms.common.base.BaseVo;
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.node.model.Category;

/**
 * ClassName:   NodeService.java
 * <p>
 *
 * @author      ����
 * @version     v1.01
 * @since       v1.01
 * @Date        Dec 22, 2012 6:04:34 PM
 */
public class NodeService {

    /**
     * logger:
     * <p>��־
     *
     * @since   v1.01
     */
    private static SysLogger logger = SysLogger.getLogger(NodeService.class);

    /**
     * addNode:
     * <p>����豸��������Ϣ
     *
     * @param   baseVo
     *          - �豸
     *
     * @since   v1.01
     */
    public void addNode(BaseVo baseVo) {
        NodeUtil nodeUtil = new NodeUtil();
        NodeDTO node = nodeUtil.conversionToNodeDTO(baseVo);
        addNode(node);
    }

    /**
     * addNode:
     * <p>����豸��������Ϣ
     *
     * @param   node
     *          - �豸
     *
     * @since   v1.01
     */
    public void addNode(NodeDTO node) {
        CategoryService categoryService = new CategoryService();
        Category category = categoryService.getCategory(node);
        if (category == null) {
            logger.info("δ֪���豸����:" + node.getNodeid() + ":" + node.getName() + ":" + node.getIpaddress() + ":" + node.getType() + ":" + node.getSubtype());
            return;
        } else {
            logger.info("��֪���豸����:" + node.getNodeid() + ":" + node.getName() + ":" + node.getIpaddress() + ":" + node.getType() + ":" + node.getSubtype());
        }

        ConfigInfoService service = new ConfigInfoService();
        service.addNodeConfigInfo(node, category);
    }

    /**
     * deleteNode:
     * <p>ɾ���豸��������Ϣ
     *
     * @param   baseVo
     *          - �豸
     *
     * @since   v1.01
     */
    public void deleteNode(BaseVo baseVo) {
        NodeUtil nodeUtil = new NodeUtil();
        NodeDTO node = nodeUtil.conversionToNodeDTO(baseVo);
        deleteNode(node);
    }

    /**
     * deleteNode:
     * <p>ɾ���豸��������Ϣ
     *
     * @param   node
     *          - �豸
     *
     * @since   v1.01
     */
    public void deleteNode(NodeDTO node) {
        CategoryService categoryService = new CategoryService();
        Category category = categoryService.getCategory(node);
        if (category == null) {
            logger.info("δ֪���豸����:" + node.getNodeid() + ":" + node.getName() + ":" + node.getIpaddress() + ":" + node.getType() + ":" + node.getSubtype());
            return;
        } else {
            logger.info("��֪���豸����:" + node.getNodeid() + ":" + node.getName() + ":" + node.getIpaddress() + ":" + node.getType() + ":" + node.getSubtype());
        }
        
        ConfigInfoService service = new ConfigInfoService();
        service.deleteNodeConfigInfo(node, category);
    }

    public void sendNode(BaseVo baseVo) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("aaa"));
            objectOutputStream.writeObject(baseVo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

