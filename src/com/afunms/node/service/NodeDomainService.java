/*
 * @(#)NodeDomainService.java     v1.01, 2013 9 28
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.node.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.afunms.indicators.model.NodeDTO;
import com.afunms.initialize.ResourceCenter;
import com.afunms.node.dao.NodeDomainDao;
import com.afunms.node.model.Domain;
import com.afunms.node.model.NodeDomain;

/**
 * ClassName:   NodeDomainService.java
 * <p>
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2013 9 28 23:22:35
 */
public class NodeDomainService {

    private static Hashtable<String, NodeDomain> NODE_DOMAIN_HASHTABLE = new Hashtable<String, NodeDomain>();

    public static Domain DEFAULT_DOMAIN;

    private static List<Domain> DOMAIN_LIST = new ArrayList<Domain>();

    private static Hashtable<String, Domain> DOMAIN_HASHTABLE = new Hashtable<String, Domain>();

    static {
        try {
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(new File(ResourceCenter.getInstance().getSysPath() + "WEB-INF/classes/node-domain.xml"));
            List list = doc.getRootElement().getChildren("domain");
            for (Object object : list) {
                Element element = (Element) object;
                int id = Integer.valueOf(element.getChildText("id"));
                String descr = element.getChildText("descr");
                boolean isDefault = Boolean.valueOf(element.getChildText("default"));
                Domain domain = new Domain();
                domain.setId(id);
                domain.setDescr(descr);
                domain.setDefault(isDefault);
                DOMAIN_LIST.add(domain);
                if (isDefault) {
                    DEFAULT_DOMAIN = domain;
                }
                DOMAIN_HASHTABLE.put("" + id, domain);
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }  
    }
    public static void setNodeDomain(NodeDomain nodeDomain) {
        NODE_DOMAIN_HASHTABLE.put(nodeDomain.getNodeId() + ":" + nodeDomain.getNodeType() + ":" + nodeDomain.getNodeSubtype(), nodeDomain);
    }

    public NodeDomain getNodeDomain(NodeDTO nodeDTO) {
        NodeDomain domain = null;
        String id = nodeDTO.getNodeid() + ":" + nodeDTO.getType() + ":" + nodeDTO.getSubtype();
        domain = NODE_DOMAIN_HASHTABLE.get(id);
        if (domain != null) {
            return domain;
        }
        domain = findDomain(nodeDTO);
        if (domain != null) {
            setNodeDomain(domain);
            return domain;
        }
        domain = createDomain(nodeDTO);
        add(domain);
        setNodeDomain(domain);
        return domain;
    }

    public NodeDomain findDomain(NodeDTO nodeDTO) {
        NodeDomainDao nodeDomainDao = new NodeDomainDao();
        List<NodeDomain> list = nodeDomainDao.find(nodeDTO);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public NodeDomain createDomain(NodeDTO nodeDTO) {
        NodeDomain domain = new NodeDomain();
        domain.setDomain(String.valueOf(DEFAULT_DOMAIN.getId()));
        domain.setNodeId(nodeDTO.getNodeid());
        domain.setNodeType(nodeDTO.getType());
        domain.setNodeSubtype(nodeDTO.getSubtype());
        return domain;
    }

    public List<NodeDomain> getAllNodeDomain() {
        List<NodeDomain> list = null;
        NodeDomainDao dao = new NodeDomainDao();
        try {
            list = dao.loadAll();
        } catch (RuntimeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            dao.close();
        }
        if (list == null) {
            list = new ArrayList<NodeDomain>();
        }
        return list;
    }
    /**
     * add:
     * <p>添加设备所属区域
     *
     * @param domain
     *
     * @since   v1.01
     */
    public void add(NodeDomain domain) {
        NodeDomainDao dao = new NodeDomainDao();
        try {
            dao.save(domain);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
    }

    /**
     * delete:
     * <p>删除设备所属区域
     *
     * @param   id
     *
     * @since   v1.01
     */
    public void delete(String id) {
        delete(new String[] {id});
    }

    /**
     * delete:
     * <p>删除设备所属区域
     *
     * @param id
     *
     * @since   v1.01
     */
    public void delete(String[] id) {
        NodeDomainDao dao = new NodeDomainDao();
        try {
            dao.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
    }

    public void delete(NodeDTO nodeDTO) {
        NodeDomainDao dao = new NodeDomainDao();
        try {
            dao.delete(nodeDTO);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
    }

    public void delete(String nodeId, String nodeType, String nodeSubtype) {
        NodeDomainDao dao = new NodeDomainDao();
        try {
            dao.delete(nodeId, nodeType, nodeSubtype);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
    }

    public static Domain getDomain(String domain) {
        return DOMAIN_HASHTABLE.get(domain);
    }

    public static List<Domain> getDomain() {
        return DOMAIN_LIST;
    }
}

