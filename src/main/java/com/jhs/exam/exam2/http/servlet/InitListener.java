package com.jhs.exam.exam2.http.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.jhs.exam.exam2.app.App;

@WebListener
public class InitListener implements ServletContextListener {
	// 웹어플리케이션 시작시 호출되며, 웹 어플리케이션의 모든 필터 또는 서블릿이 초기화 되기 전에 호출되는 메서드
    @Override
    public void contextInitialized(ServletContextEvent sce) {
    	App.start();
    }

    // 웹어플리케이션 종료시 호출되며, 웹 어플리케이션의 모든 필터 또는 서블릿이 종료된 이후 호출되는 메서드
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        
    }
}
