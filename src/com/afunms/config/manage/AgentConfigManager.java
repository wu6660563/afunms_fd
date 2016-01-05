package com.afunms.config.manage;

import java.util.List;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.config.dao.AgentConfigDao;
import com.afunms.config.dao.MacconfigDao;
import com.afunms.config.model.AgentConfig;
import com.afunms.config.model.Macconfig;

/**
 * ����agent��������
 * @author LiNan
 *
 */

public class AgentConfigManager extends BaseManager implements ManagerInterface {
	public String execute(String action) {
		if (action.equals("list")) {
			return list();
		}
		if (action.equals("ready_add")) {
			return "/config/agentconfig/add.jsp";
		}
		if (action.equals("add")) {
			return add();
		}
		if (action.equals("ready_edit")) {
			return ready_edit();
		}
		if (action.equals("update")) {
			return update();
		}
		if (action.equals("delete")) {
			return delete();
		}
		setErrorCode(ErrorMessage.ACTION_NO_FOUND);
		return null;
	}
/**
 * ��ҳ���н����б�չʾ
 * @return
 */
	public String list() {
		AgentConfigDao dao = new AgentConfigDao();
		setTarget("/config/agentconfig/list.jsp");
		return list(dao);
	}
/**
 * ����µ�agent
 * @return
 */
	public String add() {
		AgentConfig vo = new AgentConfig();
		AgentConfigDao dao = new AgentConfigDao();

		vo.setAgentname(getParaValue("agentname"));
		vo.setIpaddress(getParaValue("ipaddress"));
		vo.setAgentdesc(getParaValue("agentdesc"));

		try {
			dao.save(vo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		return "/agent.do?action=list";
	}
/**
 * ��agent�����޸�
 * ��ת���޸�ҳ��
 * @return
 */
	public String ready_edit() {
		AgentConfigDao dao = new AgentConfigDao();
		BaseVo vo = null;
		try {
			vo = dao.findByID(getParaValue("id"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		request.setAttribute("vo", vo);
		return "/config/agentconfig/edit.jsp";
	}
/**
 * ��agent�޸�
 * @return
 */
	public String update() {
		AgentConfig vo = new AgentConfig();
		vo.setAgentid(getParaIntValue("id"));
		vo.setAgentname(getParaValue("agentname"));
		vo.setIpaddress(getParaValue("ipaddress"));
		vo.setAgentdesc(getParaValue("agentdesc"));
		AgentConfigDao dao = new AgentConfigDao();
		try {
			dao.update(vo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		return "/agent.do?action=list";
	}
	/**
	 * ����ɾ��agent
	 * @return
	 */
	public String delete(){
		String[] ids = getParaArrayValue("checkbox");
		AgentConfigDao dao = new AgentConfigDao();
		try
		{
			if(ids!=null && ids.length>0){
				dao.deleteall(ids);
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			dao.close();
		}
			
        return "/agent.do?action=list"; 
	}
}
