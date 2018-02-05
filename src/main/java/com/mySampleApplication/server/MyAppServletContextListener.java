package com.mySampleApplication.server;


import com.mySampleApplication.shared.factory.JPAFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class MyAppServletContextListener implements ServletContextListener{

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        try {
            JPAFactory.close();
        } catch (Exception e) {
            throw new RuntimeException("factory was not closed");
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
    }
}