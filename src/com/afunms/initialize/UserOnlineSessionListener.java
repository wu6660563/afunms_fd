package com.afunms.initialize;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.afunms.common.util.SessionConstant;
import com.afunms.common.util.SysUtil;
import com.afunms.system.dao.SysLogDao;
import com.afunms.system.model.User;

/**
 * 	yaoag add 2012-08-14 
 *  
 *  统计用户在线时长
 * 
 * 	@author yag
 *
 */
public class UserOnlineSessionListener implements HttpSessionListener{

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-hh HH:mm:ss");
	
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		// TODO Auto-generated method stub
//		HttpSession session = sessionEvent.getSession();
//		ServletContext application = sessionEvent.getSession().getServletContext();
//		User user = (User)session.getAttribute(SessionConstant.CURRENT_USER); //当前用户
//		if(user != null){
//			System.out.println("登录：" + user.getName() +" @ " ); 
//		}
//		System.out.println("login....");
	}

	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		// TODO Auto-generated method stub
		HttpSession session = sessionEvent.getSession();
		User user = (User)session.getAttribute(SessionConstant.CURRENT_USER); //当前用户
		ServletContext application = sessionEvent.getSession().getServletContext();
		if(user != null ) {
			System.out.println("退出：" + user.getUserid() +" @ "); 
			SysLogDao dao = new SysLogDao();
			int id = 0;
			try{
				id = dao.getIdByUsername(user.getName());
			} catch(Exception e){
				
			} finally {
				dao.close();
			}
			if(id > 0){
				try{
					dao = new SysLogDao();
					dao.saveQuitTime(SysUtil.getCurrentTime(), id);
				} catch (Exception e){
					
				} finally {
					dao.close();
				}
			}
		}
		//System.out.println("login out ...........");
	}

}
