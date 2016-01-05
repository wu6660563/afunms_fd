package com.afunms.config.manage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.SysLogger;
import com.afunms.config.dao.KnowledgeDAO;
import com.afunms.config.dao.KnowledgebaseDao;
import com.afunms.config.dao.SupperDao;
import com.afunms.config.model.Knowledgebase;
import com.afunms.system.model.User;

public class KnowledgebaseManager extends BaseManager implements
		ManagerInterface {

	public String list() {
		List list = null;
		KnowledgebaseDao dao = new KnowledgebaseDao();
		try {
            setTarget("/config/knowledgebase/list.jsp");
            list(dao);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            dao.close();
        }
		return "/config/knowledgebase/list.jsp";
	}
	
	public String hostfind() {
		String eventid = (String) session.getAttribute("idforknowledge");
		KnowledgebaseDao dao = new KnowledgebaseDao();
		List attachfiles_event_list = null;
        try {
            attachfiles_event_list = dao.findforevent(eventid);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
		session.setAttribute("attachfiles_event_list", attachfiles_event_list);
		setTarget("/config/knowledgebase/event_show.jsp");
		KnowledgebaseDao dao2=new KnowledgebaseDao();
		try {
            list(dao2);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao2.close();
        }
		return "/config/knowledgebase/event_show.jsp";
	}
	
	/**
	*����������ѯ
	*/
	public String find(){
		String con1=getParaValue("categorycon");
		request.setAttribute("con1",con1 );
		String con2=getParaValue("entitycon");
		request.setAttribute("con2",con2 );
		String con3=getParaValue("subentitycon");
		request.setAttribute("con3",con3 );
		String key=getParaValue("wordkey");
		request.setAttribute("wordkey",key );
		KnowledgebaseDao dao = new KnowledgebaseDao();
		try {
            if(key.equals("")){
            List findlist=dao.find(con1, con2, con3);
            session.setAttribute("mylist", findlist);
            }else{
            List findlist=dao.findByKey(key);
            session.setAttribute("mylist", findlist);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
		setTarget("/config/knowledgebase/find.jsp");
		KnowledgebaseDao dao2=new KnowledgebaseDao();
		try {
           list(dao2);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            dao2.close();
        }
        return "/config/knowledgebase/find.jsp";
	}
	
	/**
	*�޸�
	*/
	public String update(){
		Knowledgebase vo = new Knowledgebase();
		vo.setId(getParaIntValue("id"));
		vo.setCategory(getParaValue("category"));
		vo.setEntity(getParaValue("entity"));
		vo.setSubentity(getParaValue("subentity"));
		vo.setTitles(getParaValue("titles"));
		vo.setContents(getParaValue("contents"));
		vo.setAttachfiles(getParaValue("attachfiles"));
		vo.setBak(getParaValue("bak"));

		KnowledgebaseDao dao = new KnowledgebaseDao();
		String target = "";
        try {
            target = null;
            if(dao.update(vo))
            	   target = "/knowledgebase.do?action=list";
        } catch (RuntimeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            dao.close();
        }
		return target;
	}
	
	/**
	*����
	*/
	public String add() {
			Knowledgebase vo = new Knowledgebase();
			KnowledgebaseDao dao = new KnowledgebaseDao();
			String fname=(String)session.getAttribute("upfname");
			
			vo.setCategory(getParaValue("category"));
			vo.setEntity(getParaValue("entity"));
			vo.setSubentity(getParaValue("subentity"));
			vo.setTitles(getParaValue("titles"));
			vo.setContents(getParaValue("contents"));
			vo.setAttachfiles(fname);
			vo.setBak(getParaValue("bak"));
			vo.setUserid(getParaValue("userid"));
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			Date date = new Date();
			vo.setKtime(simpleDateFormat.format(date));

			try {
				dao.save(vo);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				dao.close();
				String mynull="null";
				session.setAttribute("upfname",mynull);
			}
			return "/knowledgebase.do?action=list";
		}
	
	
	private String readyAdd() {
		return "/config/knowledgebase/add.jsp";
	}
	
	private String upload(){
		return "/config/knowledgebase/upload.jsp";
	}
	
	
	public String execute(String action) {
		if (action.equals("list")) {
			KnowledgebaseDao dao=new KnowledgebaseDao();
			String findselect = "";
            try {
                findselect = dao.selectcontent();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dao.close();
            }
			session.setAttribute("findselect", findselect);
			return list();
		}
		if (action.equals("ready_add")) {
			KnowledgeDAO dao=new KnowledgeDAO();
			String select = "";
            try {
                select = dao.selectcontent();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dao.close();
            }
			session.setAttribute("select", select);
			return readyAdd();
		}
		if (action.equals("add")) {
			return add();
		}
		if (action.equals("delete")) {

			DaoInterface dao = new KnowledgebaseDao();
			try {
                setTarget("/knowledgebase.do?action=list&jp=1");
                delete(dao);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                ((KnowledgebaseDao)dao).close();
            }
            return "/knowledgebase.do?action=list&jp=1";
		}
		if (action.equals("update")) {
			return update();
		}
		if (action.equals("read")) {
			DaoInterface dao = new KnowledgebaseDao();
			try {
                setTarget("/config/knowledgebase/read.jsp");
                readyEdit(dao);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                ((KnowledgebaseDao)dao).close();
            }
            return "/config/knowledgebase/read.jsp";
		}
		if (action.equals("ready_edit")) {
			KnowledgeDAO dao2=new KnowledgeDAO();
			String select = "";
            try {
                select = dao2.selectcontent();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dao2.close();
            }
			session.setAttribute("select", select);
			DaoInterface dao = new KnowledgebaseDao();
			try {
                setTarget("/config/knowledgebase/edit.jsp");
                readyEdit(dao);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                ((KnowledgebaseDao)dao).close();
            }
            return "/config/knowledgebase/edit.jsp";
		}
		if(action.equals("upload")){
			return upload();
		}
		if(action.equals("find")){
			KnowledgebaseDao dao=new KnowledgebaseDao();
			String findselect = "";
            try {
                findselect = dao.selectcontent();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dao.close();
            }
			session.setAttribute("findselect", findselect);
			return find();
		}
		if (action.equals("hostfind")) {
			return hostfind();
		}
		if (action.equals("show")) {
			DaoInterface dao = new KnowledgebaseDao();
			try {
                setTarget("/config/knowledgebase/show_content.jsp");
                readyEdit(dao);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                ((KnowledgebaseDao)dao).close();
            }
            return "/config/knowledgebase/show_content.jsp";
		}
		setErrorCode(ErrorMessage.ACTION_NO_FOUND);
		return null;
	}

}
