/*
 * @(#)CustomReportManager.java     v1.01, Jun 18, 2013
 *
 * Copyright (c) 2011, TNT All Rights Reserved.
 */

package com.afunms.capreport.manage;

import java.util.List;

import com.afunms.capreport.dao.CustomReportDao;
import com.afunms.capreport.model.CustomReportVo;
import com.afunms.common.base.BaseManager;
import com.afunms.common.base.ManagerInterface;
import com.afunms.system.dao.UserDao;
import com.afunms.system.model.User;

/**
 * 
 * ClassName:   CustomReportManager.java
 * <p>
 *
 * @author      吴品龙
 * @version     v1.01
 * @since       v1.01
 * @Date        Jun 18, 2013 3:37:15 PM
 */
public class CustomReportManager  extends BaseManager implements ManagerInterface{

	public String execute(String action) {
		if("list".equals(action)) {
			return list();
		} else if("add".equals(action)) {
			return add();
		} else if("delete".equals(action)) {
			return delete();
		} else if("save".equals(action)) {
			return save();
		} else if("edit".equals(action)) {
			return edit();
		} else if("update".equals(action)){
			return update();
		}
		return null;
	}
	
	private String list(){
		CustomReportDao dao = new CustomReportDao();
        setTarget("/capreport/customReportList.jsp");
        UserDao userDao = new UserDao();
        List<User> userList = null;
        try {
        	userList = userDao.loadAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			userDao.close();
		}
        request.setAttribute("userList", userList);
		return list(dao);
	}
	
	public CustomReportVo getCustomVo() {
		String name = getParaValue("name");
		String type = getParaValue("type");
		String code = getParaValue("code");
		String userId = getParaValue("userId");
		String isCreate = getParaValue("isCreate");
		String isSend = getParaValue("isSend");
		String mailTitle = getParaValue("mailTitle");
		String mailDesc = getParaValue("mailDesc");
		String fileType = getParaValue("fileType");
		String sendDate = "";
		String sendTime = "";
		if("day".equals(type)) {
			//日报
			sendTime = getParaValue("sendtimehou");
		} else if("week".equals(type)) {
			//周报
			sendDate = getParaValue("sendtimeweek");
			sendTime = getParaValue("sendtimehou");
		} else if("month".equals(type)) {
			//月报
			sendDate = getParaValue("sendtimeday");
			sendTime = getParaValue("sendtimehou");
		}
		
		CustomReportVo vo = new CustomReportVo();
		vo.setName(name);
		vo.setType(type);
		vo.setCode(code);
		vo.setUserId(userId);
		vo.setIsCreate(isCreate);
		vo.setIsSend(isSend);
		vo.setMailTitle(mailTitle);
		vo.setMailDesc(mailDesc);
		vo.setFileType(fileType);
		vo.setSendDate(sendDate);
		vo.setSendTime(sendTime);
		return vo;
	}
	
	private String save() {
		CustomReportVo vo = getCustomVo();
		CustomReportDao dao = new CustomReportDao();
		try {
			dao.save(vo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		return list();
	}
	
	private String add(){
		return "/capreport/custom_add.jsp";
	}
	
	private String delete(){
		String ids[] = getParaArrayValue("checkbox");
		CustomReportDao dao = new CustomReportDao();
		try {
			dao.delete(ids);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			dao.close();
		}
		return list();
	}
	
	private String edit(){
		String id = getParaValue("id");
		CustomReportDao dao = new CustomReportDao();
		try {
			CustomReportVo vo = (CustomReportVo) dao.findByID(id);
			request.setAttribute("vo", vo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		return "/capreport/custom_edit.jsp";
	}
	
	private String update() {
		CustomReportVo vo = getCustomVo();
		int id = getParaIntValue("id");
		vo.setId(id);
		CustomReportDao dao = new CustomReportDao();
		try {
			dao.update(vo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		return list();
	}
	
}

