package com.jhs.exam.exam2.interceptor;

import com.jhs.exam.exam2.http.Rq;

public class NeedLoginInterceptor extends Interceptor {
	
	public void init() {
		
	}	
	
	// ControllerTypeName 값이 "usr"가 아닐경우 true를 리턴
	@Override
	public boolean runBeforeAction(Rq rq) {
		if ( rq.getControllerTypeName().equals("usr") == false ) {
			return true;
		}
		
		// getActionPath 값이 해당 case값일 경우 true를 리턴
		switch (rq.getActionPath()) {
		case "/usr/article/list":
		case "/usr/article/detail":
		case "/usr/home/main":
		case "/usr/home/doSendMail":
		case "/usr/member/login":
		case "/usr/member/doLogout":
		case "/usr/member/doLogin":
		case "/usr/member/join":
		case "/usr/member/doJoin":
		case "/usr/member/getCheckValidLoginId":
		case "/usr/member/findLoginId":
		case "/usr/member/doFindLoginId":
		case "/usr/member/findLoginPw":
		case "/usr/member/doFindLoginPw":
			return true;
		}
		
		// 로그인 요구 메세지 출력후 로그인 페이지 이동 + 로그인페이지로 이동전 페이지 정보를 담아 이동하는 메서드
		if ( rq.isNotLogined() ) {
			rq.replace("로그인 후 이용해주세요.", "../member/login?afterLoginUri=" + rq.getEncodedAfterLoginUri());
			
			return false;
		}

		return true;
	}

}
