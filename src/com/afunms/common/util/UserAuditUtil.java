package com.afunms.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.afunms.system.dao.UserAuditDao;
import com.afunms.system.model.User;
import com.afunms.system.model.UserAudit;

public class UserAuditUtil {
	/**
	 * ����һ���û������Ϣ ����Ϊ���û�user ʱ��Date ����String
	 * @param user
	 * @param time
	 * @param action
	 * @return
	 */
	public boolean saveUserAudit(User user, Date time , String action){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		return saveUserAudit(user,simpleDateFormat.format(time),action);
	}
	/**
	 * ����һ���û������Ϣ ����: �û�user , ʱ���ʽ("yyyy-MM-ss HH-mm-ss") String���� , 
	 * ����String action
	 * @param user
	 * @param time
	 * @param action
	 * @return
	 */
	public boolean saveUserAudit(User user, String time , String action){
		UserAuditDao userAuditDao = null ;
		boolean result = false;
		try{
			UserAudit userAudit = new UserAudit();
			userAudit.setUserId(user.getId());
			userAudit.setTime(time);
			userAudit.setAction(action);
			userAuditDao = new UserAuditDao();
			result = userAuditDao.save(userAudit);	
		}catch(Exception e){
			e.printStackTrace();
			result = false;
		}finally{
			userAuditDao.close();
		}
		return result;
	}
    /**
     * ����һ���û������Ϣ ���� �û�user , ����String action . ʹ�õ�ʱ��ΪĬ�ϵ�ϵͳʱ�� 
     * @param user
     * @param action
     * @return
     */
	public boolean saveUserAudit(User user , String action){
		
		return saveUserAudit(user,new Date(),action);
	}
}
