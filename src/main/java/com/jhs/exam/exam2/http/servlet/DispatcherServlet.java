package com.jhs.exam.exam2.http.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jhs.exam.exam2.app.App;
import com.jhs.exam.exam2.container.Container;
import com.jhs.exam.exam2.http.Rq;
import com.jhs.exam.exam2.http.controller.Controller;
import com.jhs.mysqliutil.MysqlUtil;

abstract public class DispatcherServlet extends HttpServlet {
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		App app = Container.app;
		
		Rq rq = new Rq(req, resp);

		// 설정된 길이보다 짧으면 리턴
		if (rq.isInvalid()) {
			rq.print("올바른 요청이 아닙니다.");
		}
		
		// isReady()가 false면 실행X
		if (app.isReady() == false) {
			rq.print("앱이 실행준비가 아닙니다.");
			rq.print("<br>");
			rq.print("필수적으로 만들어야 하는 파일을 만들었는지 체크 후 다시 실행시켜주세요.");
			return;
		}
		
		// 인터셉터에서 false 리턴 할 경우 리턴
		if (runInterceptors(rq) == false) {
			return;
		}	

		// 해당 함수에 맞지 않아 null을 반환 했을시 "올바른 요청 아닙니다" 출력
		Controller controller = getControllerByRq(rq);
		
		// null이 아닐경우 해당 컨트롤러 performAction 실행
		if (controller != null) {
			controller.performAction(rq);

			MysqlUtil.closeConnection();
		} else {
			rq.print("올바른 요청이 아닙니다.");
		}
	}

	// ControllerTypeName이 usr이고 해당 ControllerName로 나누어 컨트롤로 실행
	private Controller getControllerByRq(Rq rq) {
		// uri에 ControllerTypeName 불러오기
		switch (rq.getControllerTypeName()) {
		// usr가 맞는지 확인
		case "usr":
			// uri에 ControllerName 불러와 아래 case에 해당하는지 확인 후 해당 컨트롤러 실행
			switch (rq.getControllerName()) {
			case "article":
				return Container.usrArticleController;
			case "member":
				return Container.usrMemberController;
			case "home":
				return Container.usrHomeController;
			case "like":
				return Container.usrLikeController;
			}

			break;
		case "adm":
			// uri에 ControllerName 불러와 아래 case에 해당하는지 확인 후 해당 컨트롤러 실행
			switch (rq.getControllerName()) {
			case "home":
				return Container.admHomeController;
			}
		}
		// 없을시 null 리턴
		return null;
	}

	private boolean runInterceptors(Rq rq) {

		// 로그인 했을시 세션을 저장(바로 사용할 수 있게 json 형식의 member값을 녹여서 변수에 저장)
		if (Container.beforeActionInterceptor.runBeforeAction(rq) == false) {
			return false;
		}

		// 이동하는곳 로그인 유무 걸러주는 인터셉터(로그인 필요한곳 이동시 로그인 안하면 false리턴)
		if (Container.needLoginInterceptor.runBeforeAction(rq) == false) {
			return false;
		}

		// 이동하는곳 로그아웃 유무 걸러주는 인터셉터(로그인 후 이동시 로그인 되어있으면 false리턴)
		if (Container.needLogoutInterceptor.runBeforeAction(rq) == false) {
			return false;
		}
		
		if (Container.needAdminInterceptor.runBeforeAction(rq) == false) {
			return false;
		}

		return true;
	}

	// 실행하면 doGet함수 실행되는 함수
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
