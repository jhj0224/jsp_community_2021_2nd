package com.jhs.exam.exam2.interceptor;

import com.jhs.exam.exam2.http.Rq;

public class NeedAdminInterceptor extends Interceptor {

	public void init() {
		
	}
	
	@Override
	public boolean runBeforeAction(Rq rq) {
		if (rq.getControllerTypeName().equals("adm") == false) {
			return true;
		}

		switch (rq.getActionPath()) {
		case "/adm/member/login":
		case "/adm/member/doLogin":
			return true;
		}

		if ( rq.isNotLogined() || rq.isNotAdmin() ) {
			rq.replace("관리자 로그인 후 이용해주세요.", "../member/login");

			return false;
		}

		return true;
	}

}