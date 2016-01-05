package com.afunms.config.manage;

import java.util.List;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.config.dao.AgentNodeDao;
import com.afunms.topology.dao.HostNodeDao;

/**
 * ����ĳ��agent��������豸�ڵ�����
 * @author LiNan
 *
 */

public class AgentNodeManager extends BaseManager implements ManagerInterface {

	public String execute(String action) {

		if (action.equals("list")) {
			return list();
		}
		if (action.equals("add")) {
			return add();
		}
		if(action.equals("save")){
			return save();
		}
		if(action.equals("delete")){
			return delete();
		}
		setErrorCode(ErrorMessage.ACTION_NO_FOUND);
		return null;
	}
/**
 *���Ѿ���Ӻõ��豸�ڵ����ҳ��չʾ
 */
	public String list() {
		int agentid = getParaIntValue("id");
		String jsp = "/config/agentnode/list.jsp";
		List list = null;
		AgentNodeDao dao = new AgentNodeDao();
		try {
			list = dao.findbyid(agentid);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}

		request.setAttribute("list", list);
		request.setAttribute("agentid", agentid);

		return jsp;
	}

/**
 * ��ת����δ�����豸�ڵ�ҳ��
 * @return
 */	
	public String add() {
		String jsp = "/config/agentnode/add.jsp";
		int agentid=getParaIntValue("agentid");
		List list = null;
		AgentNodeDao dao = new AgentNodeDao();
		try {
			list = dao.findfornode();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		request.setAttribute("list", list);
		request.setAttribute("agentid", agentid);
		return jsp;
	}
	
/**
 * ����ѡ����豸�ڵ�����������
 * @return
 */
	public String save(){
		int agentid=getParaIntValue("agentid");
		String[] ids=getParaArrayValue("checkbox");
		AgentNodeDao dao=new AgentNodeDao();
		try {
			dao.save(ids, agentid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/agentnode.do?action=list";
	}

/**
 * ����ѡ����豸�ڵ��������ɾ��
 * @return
 */
	public String delete(){
		String[] ids=getParaArrayValue("checkbox");
		AgentNodeDao dao=new AgentNodeDao();
		try {
			dao.delete(ids);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/agentnode.do?action=list";
	}

}
