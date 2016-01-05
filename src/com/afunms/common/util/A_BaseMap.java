/**
 * <p>Description:network utilities</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-11
 */

package com.afunms.common.util;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class A_BaseMap {

	/**
	 * 
	 */
	public A_BaseMap() {
		super();
		// TODO Auto-generated constructor stub
	}

	private Session session;
		// ���ݿ���������
		private Transaction transaction;

		/**
		 * ��ʼ�����ݿ���������
		 * @return ��ʼ����ɵ����ݿ�����
		 * @throws HibernateException
		 */
		public Session beginTransaction() throws HibernateException {
			session = HibernateUtil.currentSession();
			transaction = session.beginTransaction();
			return session;
		}

		/**
		 * ���һ�����ݿ�����
		 * @param commit �Ƿ��ύ����trueʱ�ύ��falseʱ�����ݿⷢ��ع���rollback��
		 * @throws HibernateException
		 */
		public void endTransaction(boolean commit) throws HibernateException {
			try{
			
			if (commit) {
				transaction.commit();
			} else {
				transaction.rollback();
			}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			finally{
				HibernateUtil.closeSession();
			}
			
		}

}
