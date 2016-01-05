package com.afunms.application.manage;

import java.util.ArrayList;
import java.util.List;

import com.afunms.application.dao.ApplicationNodeDao;
import com.afunms.application.model.ApplicationNode;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.SysLogger;
import com.afunms.node.service.NodeService;
import com.afunms.topology.util.KeyGenerator;

public class ApplicationNodeManager extends BaseManager implements
		ManagerInterface {

	private static SysLogger logger = SysLogger.getLogger(ApplicationNodeManager.class);
	
	public String execute(String action) {
		if ("list".equals(action)) {
			return list();
		} else if ("showAdd".equals(action)) {
			return showAdd();
		} else if ("add".equals(action)) {
			return add();
		} else if ("showUpdate".equals(action)) {
			return showUpdate();
		} else if ("update".equals(action)) {
			return update();
		} else if ("delete".equals(action)) {
			return delete();
		} else if ("showList".equals(action)){
			return showList();
		}
		return null;
	}

	public String list() {
		String jsp = "/application/application_node/list.jsp";
		
		//String type = getParaValue("type");
		String subtype = getParaValue("subtype");

	    if (subtype == null || subtype.trim().length() == 0) {
	    	subtype = "-1";
	    }
	    
	    String bidSql = getBidSql("bid");
	    // 为了分页
	    ApplicationNodeDao applicationNodeDao = new ApplicationNodeDao();
	    List pagelist = new ArrayList();
	    String wherestr = "where 1=1 ";
	    try {
            if (!"-1".equalsIgnoreCase(subtype)) {
                wherestr = wherestr + " and subtype = '" + subtype + "'";
            }
            if (bidSql != null && bidSql.trim().length() > 0) {
            	wherestr += bidSql;
            }
            list(applicationNodeDao, wherestr);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	applicationNodeDao.close();
        }
        pagelist = (List) request.getAttribute("list");
        
        request.setAttribute("list", pagelist);
        request.setAttribute("subtype", subtype);

        return jsp;
	}
	
	public String showList() {
		String jsp = "/application/application_node/showlist.jsp";
		
		//String type = getParaValue("type");
		String subtype = getParaValue("subtype");

	    if (subtype == null || subtype.trim().length() == 0) {
	    	subtype = "-1";
	    }
	    
	    String bidSql = getBidSql("bid");
	    // 为了分页
	    ApplicationNodeDao applicationNodeDao = new ApplicationNodeDao();
	    List pagelist = new ArrayList();
	    String wherestr = "where 1=1 ";
	    try {
            if (!"-1".equalsIgnoreCase(subtype)) {
                wherestr = wherestr + " and subtype = '" + subtype + "'";
            }
            if (bidSql != null && bidSql.trim().length() > 0) {
            	wherestr += bidSql;
            }
            list(applicationNodeDao, wherestr);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	applicationNodeDao.close();
        }
        pagelist = (List) request.getAttribute("list");
        
        request.setAttribute("list", pagelist);
        request.setAttribute("subtype", subtype);

        return jsp;
	}
	
	public String showAdd() {
		String jsp = "/application/application_node/add.jsp";
		return jsp;
	}
	
	public String showUpdate() {
		String jsp = "/application/application_node/edit.jsp";
		String id = getParaValue("id");
		ApplicationNode applicationNode = new ApplicationNode();
		ApplicationNodeDao nodeDao = new ApplicationNodeDao();
		try {
			applicationNode = (ApplicationNode) nodeDao.findByID(id);
		} catch (Exception e) {
			logger.error("查询ApplicationNode失败！", e);
		} finally {
			nodeDao.close();
		}
		request.setAttribute("applicationNode", applicationNode);
		return jsp;
	}
	
	public String add() {
		ApplicationNode applicationNode = createApplicationNode();
		applicationNode.setId(KeyGenerator.getInstance().getNextKey());
		ApplicationNodeDao nodeDao = new ApplicationNodeDao();
		try {
			nodeDao.save(applicationNode);
			
			//调用addNode方法，自动创建表结构
			NodeService service = new NodeService();
			service.addNode(applicationNode);
			
		} catch(Exception e) {
			logger.info("添加ApplicationNode失败！！！", e);
		} finally {
			nodeDao.close();
		}		
		return list();
	}
	public String update() {
		ApplicationNode applicationNode = createApplicationNode();
		ApplicationNodeDao nodeDao = new ApplicationNodeDao();
		try {
			nodeDao.update(applicationNode);
		} catch(Exception e) {
			logger.info("更新ApplicationNode失败！！！", e);
		} finally {
			nodeDao.close();
		}
		return "/applicationnode.do?action=list&jp=1&subtype=-1";
	}
	
	public String delete() {
		String[] ids = request.getParameterValues("checkbox");

        ApplicationNodeDao nodeDao = new ApplicationNodeDao();
        try {
        	//调用addNode方法，自动创建表结构
			NodeService service = new NodeService();
			for (int i = 0; i < ids.length; i++) {
				ApplicationNode node = (ApplicationNode)nodeDao.findByID(ids[i]);
				if (node != null) {
					service.deleteNode(node);
                }
			}
			nodeDao.delete(ids);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            nodeDao.close();
        }
        return list();
	}
	
	private ApplicationNode createApplicationNode() {
		ApplicationNode applicationNode = new ApplicationNode();
		applicationNode.setId(getParaIntValue("id"));
		applicationNode.setName(getParaValue("name"));
		applicationNode.setIpAddress(getParaValue("ipaddress"));
		applicationNode.setType(getParaValue("type"));
		applicationNode.setSubtype(getParaValue("subtype"));
		applicationNode.setUniqueKey(getParaValue("uniquekey"));
		applicationNode.setDescr(getParaValue("descr"));
		applicationNode.setName(getParaValue("name"));
		String managed = getParaValue("managed");
		if("1".equals(managed)) {
			applicationNode.setManaged(true);
		} else {
			applicationNode.setManaged(false);
		}
		applicationNode.setBid(getParaValue("bid"));
		return applicationNode;
	}
	
	
}
