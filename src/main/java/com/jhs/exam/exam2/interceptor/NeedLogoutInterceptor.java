package com.jhs.exam.exam2.interceptor;

import com.jhs.exam.exam2.http.Rq;

public class NeedLogoutInterceptor extends Interceptor {

	public void init() {
		
	}
	
	@Override
	public boolean runBeforeAction(Rq rq) {
		switch (rq.getActionPath()) {
		case "/usr/member/login":
		case "/usr/member/doLogin":
		case "/usr/member/join":
		case "/usr/member/doJoin":
		case "/usr/member/findLoginId":
		case "/usr/member/doFindLoginId":
		case "/usr/member/findLoginPw":
		case "/usr/member/doFindLoginPw":
			if (rq.isLogined()) {
				rq.replace("로그아웃 후 이용해주세요.", "../home/main");

				return false;
			}
		}
		
		return true;
	}

}
