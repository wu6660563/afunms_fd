/**
 * <p>Description:network utilities</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-11
 */

package com.afunms.common.util;

import javax.naming.Context; 
import javax.naming.InitialContext; 
import javax.naming.NamingException; 

import net.sf.hibernate.HibernateException; 
import net.sf.hibernate.Session; 
import net.sf.hibernate.SessionFactory; 

/** 
* @author HD 
*/ 
public class HibernateUtil { 

	private static SessionFactory sessionFactory = null; 
	public static final ThreadLocal session = new ThreadLocal(); 

	public static Session currentSession() throws HibernateException { 
		if (sessionFactory == null) { 
			// ���sessionFactoryʵ��Ϊnull���JNDI�л�ȡ 
			if (getSystemSessionFactory() == false) { 
				throw new HibernateException("Exception geting SessionFactory from JNDI "); 
			} 
		} 
		Session s = (Session) session.get(); 
		// Open a new Session, if this Thread has none yet 
		if (s == null) { 
			s = sessionFactory.openSession(); 
			session.set(s); 
		} 
		return s; 
	} 

	public static void closeSession() throws HibernateException { 
		Session s = (Session) session.get(); 
		session.set(null); 
		if (s != null) 
			s.close(); 
	} 

	private static boolean getSystemSessionFactory() { 
		try { 
			//��JNDI��ȡ��SessionFactory��ʵ�������������false 
			Context inttex = new InitialContext(); 
			sessionFactory = 
				(SessionFactory) inttex.lookup("HibernateSessionFactory"); 
		} catch (NamingException e) { 
			return false; 
		} 
		return true; 
	} 
} 
