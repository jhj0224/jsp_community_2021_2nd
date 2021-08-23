package com.jhs.exam.exam2.http.servlet;

import javax.servlet.annotation.WebServlet;

///adm로 시작하는 모든 요청
@WebServlet("/adm/*")
public class AdmServlet extends DispatcherServlet {
}