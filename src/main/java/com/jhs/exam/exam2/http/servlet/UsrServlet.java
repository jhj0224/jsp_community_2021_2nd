package com.jhs.exam.exam2.http.servlet;

import javax.servlet.annotation.WebServlet;

///usr로 시작하는 모든 요청
@WebServlet("/usr/*")
public class UsrServlet extends DispatcherServlet {
}