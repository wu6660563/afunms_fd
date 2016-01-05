package com.afunms.home.user.manage;

import java.util.ArrayList;
import java.util.List;

import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ErrorMessage;
import com.afunms.common.base.ManagerInterface;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.SysLogger;
import com.afunms.home.module.dao.ModuleDao;
import com.afunms.home.role.dao.HomeRoleDao;
import com.afunms.home.role.model.HomeRoleModel;
import com.afunms.home.user.dao.HomeUserDao;
import com.afunms.home.user.model.HomeUserModel;
import com.afunms.system.dao.RoleDao;
import com.afunms.system.model.Role;
import com.afunms.system.model.User;

public class HomeUserManager extends BaseManager implements ManagerInterface {

    public String execute(String action) {
	// TODO Auto-generated method stub
	if ("update".equals(action)) {
	    return update();
	} else if ("save".equals(action)) {
	    return save();
	}
	setErrorCode(ErrorMessage.ACTION_NO_FOUND);
	return null;
    }

    @SuppressWarnings("unchecked")
    private String save() {
	String[] checkboxStrArray = request.getParameterValues("checkbox");// �õ�ҳ���ϱ�ѡ�е�checkboxδѡ�еĽ��ò���
	User uservo = (User) session.getAttribute(SessionConstant.CURRENT_USER);
	HomeUserDao homeUserDao = new HomeUserDao();
	DBManager db;
	try {
	    db = new DBManager();
	    String sql = "";
	    HomeRoleDao roleDao = new HomeRoleDao();
	    List homeRoleList = roleDao.findByCondition(" where role_id='"
		    + uservo.getRole() + "'");// ���ҽ�ɫ��������ģ�� �����˲���ʾ�Ĺ���
	    sql = "delete from nms_home_module_user where user_id='"
		    + uservo.getUserid() + "'";
	    db.addBatch(sql);
	    for (int i = 0; i < homeRoleList.size(); i++) {
		HomeRoleModel roleModel = (HomeRoleModel) homeRoleList.get(i);
		HomeUserModel userModel = new HomeUserModel();
		userModel.setBusinessids(uservo.getBusinessids());
		userModel.setChName(roleModel.getChName());
		userModel.setDept_id(roleModel.getDept_id());
		userModel.setEnName(roleModel.getEnName());
		userModel.setName(uservo.getName());
		userModel.setNote(roleModel.getNote());
		userModel.setRole_id(uservo.getRole());
		userModel.setUser_id(uservo.getUserid());
		userModel.setType(roleModel.getType());// ��������
		userModel.setVisible(0); // ��Ҫ�����е�����0
		if (checkboxStrArray != null) {// ��Ҫ�ж�checkboxStrArray����Ϊ��
		    for (int j = 0; j < checkboxStrArray.length; j++) {
			if (userModel.getChName().equals(checkboxStrArray[j])) { // �����Ƿ�ѡ��
			    // ���ж��Ƿ��޸�Ϊ1
			    userModel.setVisible(1);
			}
		    }
		}
		sql = homeUserDao.saveSql(userModel);
		db.addBatch(sql);
	    }
	    db.executeBatch();
	    db.close();
	} catch (Exception ex) {
	    SysLogger.error(ex.getMessage());
	} finally {
	}
	return "/user.do?action=home";
    }

    private String update() {
	String url = "/system/home/user/update.jsp";
	User uservo = (User) session.getAttribute(SessionConstant.CURRENT_USER);
	List homeUserModelList = new ArrayList();
	HomeUserDao homeUserDao = new HomeUserDao();
	homeUserModelList = homeUserDao.findByCondition(" where user_id='"
		+ uservo.getUserid() + "'");// �ܿ��Ƶ�ģ��
	HomeRoleDao homeRoleDao = new HomeRoleDao();
	List homeRoleList = homeRoleDao.findByCondition(" where role_id='"
		+ uservo.getRole() + "' and visible='1'");// �ܿ��Ƶ�ģ��
	if (homeUserModelList.isEmpty() || homeUserModelList == null) {
	    System.out.println("�û�:" + uservo.getUserid() + "��δ������ҳģ�������ʾ���ã�");
	    if (homeRoleList.isEmpty() || homeRoleList == null) {
		System.out.println("��ɫ��" + uservo.getRole() + "ģ�����(��ɫ)"); // ��ת����ҳ
		return "/user.do?action=home";
	    } else {
		for (int i = 0; i < homeRoleList.size(); i++) {
		    HomeRoleModel roleModel = (HomeRoleModel) homeRoleList
			    .get(i);
		    HomeUserModel userModel = new HomeUserModel();
		    userModel.setBusinessids(uservo.getBusinessids());
		    userModel.setChName(roleModel.getChName());
		    userModel.setDept_id(roleModel.getDept_id());
		    userModel.setEnName(roleModel.getEnName());
		    userModel.setName(uservo.getName());
		    userModel.setNote(roleModel.getNote());
		    userModel.setRole_id(uservo.getRole());
		    userModel.setUser_id(uservo.getUserid());
		    userModel.setVisible(roleModel.getVisible());
		    userModel.setType(roleModel.getType());// ��������
		    homeUserModelList.add(userModel);
		}
	    }
	}
	// �ڲ�ѯһ�����ݿ�||����ֱ�ӳ�����ķ�������֯����
	RoleDao rd = new RoleDao();
	Role role = (Role) rd.findByID("" + uservo.getRole());
	request.setAttribute("role", role);
	request.setAttribute("homeUserModelList", homeUserModelList);
	ModuleDao moduleDao = new ModuleDao();
	List moduleList = moduleDao.findByCondition("");
	request.setAttribute("homeRoleList", homeRoleList);
	return url;
    }
}
