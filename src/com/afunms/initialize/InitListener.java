/**
 * <p>Description:system initialize listener</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-06
 */

package com.afunms.initialize;


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * ClassName:   InitListener.java
 * <p>{@link ServletContext} 
 *
 * @author      聂林
 * @version     v1.01
 * @since       v1.01
 * @Date        2014 1 8 11:14:06
 */
public class InitListener implements ServletContextListener {

	/**
	 * contextDestroyed:
	 *
	 * @param event
	 *
	 * @since   v1.01
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent event) {
	}

	/**
	 * contextInitialized:
	 *
	 * @param event
	 *
	 * @since   v1.01
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent event) {
		try {
			String sysPath = event.getServletContext().getRealPath("/");
			// 给log4j.properties中变量${appDir}赋值
			System.setProperty("appDir", sysPath);
			// wupinlong add 2013/4/18 解决Collections.sort对比报错
			System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
			ResourceCenter.getInstance().setSysPath(sysPath);
			SysInitialize sysInit = new SysInitialize();
			sysInit.init();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}