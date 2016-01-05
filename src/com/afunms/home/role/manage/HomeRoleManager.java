package com.afunms.home.role.manage;

import java.text.SimpleDateFormat;
import java.util.List;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.SysLogger;
import com.afunms.home.module.dao.ModuleDao;
import com.afunms.home.module.model.ModuleModel;
import com.afunms.home.role.dao.HomeRoleDao;
import com.afunms.home.role.model.HomeRoleModel;
import com.afunms.system.dao.RoleDao;
import com.afunms.system.model.Role;
import com.afunms.system.model.User;

public class HomeRoleManager extends BaseManager implements ManagerInterface {

    public String execute(String action) { 
	if ("update".equals(action)) {
	    return update();
	} else if ("save".equals(action)) {
	    return save();
	} else if ("list".equals(action)) {
	    return list();
	}
	setErrorCode(ErrorMessage.ACTION_NO_FOUND);
	return null;
    }

    private String list() {
	String url = "/system/home/role/list.jsp";
	RoleDao rd = null;
	try {
	    rd = new RoleDao();
	    List<Role> Rolelist = (List<Role>) rd.loadAll();
	    //this.insert(Rolelist);
	    request.setAttribute("Rolelist", Rolelist);
	} catch (Exception e) {
	    SysLogger.error("Error in HomeRoleManager.roleList()", e);
	    e.printStackTrace();
	} finally {
	    rd.close();
	}
	return url;
    }

    public void insert(List<Role> roleList) {
	DBManager db = new DBManager();
	String sql = "";
	try {
	    ModuleDao moduleDao = new ModuleDao();
	    List moduleList = moduleDao.findByCondition("");
	    db.addBatch(" delete from nms_home_module_role ");
	    for (int i = 0; i < roleList.size(); i++) {
		Role role = (Role) roleList.get(i);
		for (int k = 0; k < moduleList.size(); k++) {
		    ModuleModel moduleModel = (ModuleModel) moduleList.get(k);
		    StringBuffer sb = new StringBuffer(100);
		    sb.append("insert into nms_home_module_role(enName, chName, role_id, dept_id, visible, note,type)values('");
		    sb.append(moduleModel.getEnName());
		    sb.append("','");
		    sb.append(moduleModel.getChName());
		    sb.append("','");
		    sb.append(role.getId());// ��ɫid
		    sb.append("','");
		    sb.append("0");// ����Ĭ��Ϊ��
		    sb.append("','");
		    sb.append(1);// Ĭ�϶�Ϊ�ɼ�
		    sb.append("','");
		    java.util.Date currentTime = new java.util.Date();
		    SimpleDateFormat formatter = new SimpleDateFormat(
			    "yyyy-MM-dd HH:mm:ss");
		    String dateString = formatter.format(currentTime);
		    sb.append(dateString);
		    sb.append("','");
		    sb.append(moduleModel.getType());
		    sb.append("')"); 
		    db.addBatch(sb.toString());
		}
	    }
	    db.executeBatch(); 
	} catch (Exception ex) {
	    SysLogger.error(ex.getMessage());
	} finally {
	    db.close();
	}
    } 
    private String save() {
	String roleId = getParaValue("roleId");
	String[] checkboxStrArray = request.getParameterValues("checkbox");// �õ�ҳ���ϱ�ѡ�е�checkbox,δѡ�еĽ��ò���
	HomeRoleDao dao = new HomeRoleDao();
	List list = dao.findByCondition(" where role_id='" + roleId + "' ");
	DBManager db = new DBManager();
	String sql = "";
	for (int j = 0; j < list.size(); j++) {
	    HomeRoleModel model = (HomeRoleModel) list.get(j);
	    model.setVisible(0); // ��Ҫ�����е�����0
	    if (checkboxStrArray != null) {// ��Ҫ�ж�checkboxStrArray����Ϊ��
		for (int i = 0; i < checkboxStrArray.length; i++) {
		    if (model.getChName().equals(checkboxStrArray[i])) { // �Ƿ�ѡ�� 
			model.setVisible(1);
		    }
		}
	    }
	    // �޸��û������ʾ
	    sql = "update nms_home_module_user set visible='"
		    + model.getVisible() + "' where role_id='" + roleId
		    + "' and chName='" + model.getChName() + "'";
	    db.addBatch(sql);

	    sql = dao.updateSql(model);
	    db.addBatch(sql);
	}
	db.executeBatch();
	db.close();
	return "/user.do?action=home";
    } 
    private String update() {
	String roleId = getParaValue("roleId");
	String url = "/system/home/role/update.jsp";
	User uservo = (User) session.getAttribute(SessionConstant.CURRENT_USER);
	String sqlCondition = " where role_id='" + roleId + "'";
	HomeRoleDao dao = new HomeRoleDao();
	List homeRoleList = dao.findByCondition(sqlCondition);
	RoleDao rd = new RoleDao();
	Role role = null;
    try {
        role = (Role) rd.findByID(roleId);
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        rd.close();
    }
	request.setAttribute("role", role);
	request.setAttribute("homeRoleList", homeRoleList);
	ModuleDao moduleDao = new ModuleDao();
	List moduleList = moduleDao.findByCondition("");
	request.setAttribute("moduleList", moduleList);
	return url;
    }
}
