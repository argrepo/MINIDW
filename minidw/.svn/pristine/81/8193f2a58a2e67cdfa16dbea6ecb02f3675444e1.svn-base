package com.anvizent.client.scheduler.listner;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class ClientSchedulerListner
 *
 */
//@WebListener
public class ClientSchedulerListner implements ServletContextListener {

	/**
	 * Default constructor.
	 */
	public ClientSchedulerListner() throws Exception {
		//ClientSchedulerMain.getInstance().init();
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			//ClientSchedulerMain.getInstance().start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}

}
