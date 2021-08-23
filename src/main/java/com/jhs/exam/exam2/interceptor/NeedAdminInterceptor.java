package com.jhs.exam.exam2.interceptor;

import com.jhs.exam.exam2.http.Rq;

public class NeedAdminInterceptor extends Interceptor {

	public void init() {
		
	}
	
	// ControllerTypeName 값이 "adm"가 아닐경우 true를 리턴
	@Override
	public boolean runBeforeAction(Rq rq) {
		if (rq.getControllerTypeName().equals("adm") == false) {
			return true;
		}

		// getActionPath 값이 해당 case값과 같은 경우 true를 리턴
		switch (rq.getActionPath()) {
		case "/adm/member/login":
		case "/adm/member/doLogin":
			return true;
		}

		// 아닐 경우 메세지 출력 후 로그인 페이지로 이동
		if ( rq.isNotLogined() || rq.isNotAdmin() ) {
			rq.replace("관리자 로그인 후 이용해주세요.", "../member/login");

			return false;
		}

		return true;
	}

}