/**
 * <p>Description:DiscoverManager</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-12
 */

package com.afunms.indicators.manage;


import java.util.List;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.JspPage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.config.dao.KnowledgebaseDao;
import com.afunms.indicators.dao.GatherIndicatorsDao;
import com.afunms.indicators.model.GatherIndicators;



/**
 * 
 * ��׼ָ�� manage
 * 
 */
public class GatherIndicatorsManager extends BaseManager implements ManagerInterface{

	public String execute(String action) {
		// TODO Auto-generated method stub
		if("list".equals(action)){
			KnowledgebaseDao dao=new KnowledgebaseDao();
			String gatherfindselect = "";
            try {
                gatherfindselect = dao.selectcontent2();
            } catch (RuntimeException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                dao.close();
            }
			session.setAttribute("gatherfindselect", gatherfindselect);
			return list();
		}else if("add".equals(action)){
			return add();
		}else if("save".equals(action)){
			return save();
		}else if("edit".equals(action)){
			return edit();
		}else if("update".equals(action)){
			return update();
		}else if("delete".equals(action)){
			return delete();
		}
		else if("find".equals(action)){
			return find();
		}
		setErrorCode(ErrorMessage.ACTION_NO_FOUND);
		return null;
	}
	
	public String list(){
		String jsp = "/topology/gatherIndicators/list.jsp";
		GatherIndicatorsDao dao = new GatherIndicatorsDao();
		try {
            setTarget(jsp);
            list(dao);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
		return jsp;
	}
	
	public String add(){
		String jsp = "/topology/gatherIndicators/add.jsp";
		return jsp;
	}
	
	public String save(){
		
		GatherIndicators gatherIndicators = createGatherIndicators();
		
		GatherIndicatorsDao dao = new GatherIndicatorsDao();
		try {
			dao.save(gatherIndicators);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		
		return list();
	}
	
	public String edit(){
		String jsp = "/topology/gatherIndicators/edit.jsp";
		
		String id = getParaValue("id");
		
		GatherIndicators gatherIndicators = null;
		
		GatherIndicatorsDao dao = new GatherIndicatorsDao();
		try {
			gatherIndicators = (GatherIndicators)dao.findByID(id);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		
		request.setAttribute("gatherIndicators", gatherIndicators);
		
		return jsp;
	}
	
	public String update(){
		
		GatherIndicators gatherIndicators = createGatherIndicators();
		
		int id = getParaIntValue("id");
		
		gatherIndicators.setId(id);
		
		GatherIndicatorsDao dao = new GatherIndicatorsDao();
		try {
			dao.update(gatherIndicators);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		
		GatherIndicatorsDao dao2=new GatherIndicatorsDao();
		List uplist=dao2.updatelist(id);
		String jsp=list();
		request.setAttribute("list", uplist);
		GatherIndicatorsDao typedao=new GatherIndicatorsDao();
		String type = "";
        try {
            type = typedao.type(id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            typedao.close();
        }
		GatherIndicatorsDao subtypedao=new GatherIndicatorsDao();
		String subtype = "";
        try {
            subtype = subtypedao.subtype(id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            subtypedao.close();
        }
		request.setAttribute("con3", type);
		request.setAttribute("con4", subtype);
		KnowledgebaseDao knowdao=new KnowledgebaseDao();
		String gatherfindselect = "";
        try {
            gatherfindselect = knowdao.selectcontent2();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            knowdao.close();
        }
		session.setAttribute("gatherfindselect", gatherfindselect);
		JspPage jp = (JspPage)request.getAttribute("page");
		jp.setTotalPage(1);
		jp.setCurrentPage(1);
		jp.setMinNum(1);
		request.setAttribute("page", jp);
		return jsp;
	}
	
	public String delete(){
		String[] ids = getParaArrayValue("checkbox");
		GatherIndicatorsDao dao = new GatherIndicatorsDao();
		try {
			dao.delete(ids);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		
		return list();
	}
	
	
public String find(){
		GatherIndicatorsDao dao=new GatherIndicatorsDao();
		String con1=getParaValue("categorycon");
		request.setAttribute("con3",con1 );
		String con2=getParaValue("entitycon");
		request.setAttribute("con4",con2 );
		List gatherfindlist = null;
        try {
            gatherfindlist = dao.gatherfind(con1, con2);
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            dao.close();
        }
		setTarget("/topology/gatherIndicators/list.jsp");
		KnowledgebaseDao dao2=new KnowledgebaseDao();
		String page= "";
        try {
            page = list(dao2);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao2.close();
        }
		request.setAttribute("list", gatherfindlist);
		JspPage jp = (JspPage)request.getAttribute("page");
		jp.setTotalPage(1);
		jp.setCurrentPage(1);
		jp.setMinNum(1);
		request.setAttribute("page", jp);
		return page;
	}
	
	
	public GatherIndicators createGatherIndicators(){
		
		String name = getParaValue("name");
		String alias = getParaValue("alias");
		String type = getParaValue("type");
		String subtype = getParaValue("subtype");
		String description = getParaValue("description");
		String category = getParaValue("category");
		String isDefault = getParaValue("isDefault");
		String isCollection = getParaValue("isCollection");
		String interval = getParaValue("poll_interval");
		String[] interstr = interval.split("-");
		
		
		
		GatherIndicators gatherIndicators = new GatherIndicators();
		gatherIndicators.setName(name);
		gatherIndicators.setAlias(alias);
		gatherIndicators.setType(type);
		gatherIndicators.setSubtype(subtype);
		gatherIndicators.setDescription(description);
		gatherIndicators.setCategory(category);
		gatherIndicators.setIsDefault(isDefault);
		gatherIndicators.setIsCollection(isCollection);
		
		gatherIndicators.setPoll_interval(interstr[0]);
		gatherIndicators.setInterval_unit(interstr[1]);
		return gatherIndicators;
	}
	
}
