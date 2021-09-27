package com.jhs.exam.exam2.http.controller;

import com.jhs.exam.exam2.app.App;
import com.jhs.exam.exam2.container.Container;
import com.jhs.exam.exam2.http.Rq;
import com.jhs.exam.exam2.util.Ut;

public class UsrHomeController extends Controller {
	
	public void init() {
		
	}
	
	// ActionMethodName이 아래 case와 일치하면 해당 함수로 이동
	@Override
	public void performAction(Rq rq) {
		switch (rq.getActionMethodName()) {
		case "main":
			actionShowMain(rq);
			break;
		case "doSendMail":
			actionDoSendMail(rq);
			break;
		// 일치하지 않을시 오류메세지 출력후 break;
		default:
			rq.println("존재하지 않는 페이지 입니다.");
			break;
		}
	}

	// main 페이지 보여주는 함수
	private void actionShowMain(Rq rq) {
		rq.jsp("usr/home/main");
	}
	
	// Gmail 외부에서 불러서 이메일 보내는 함수
	private void actionDoSendMail(Rq rq) {
		App app = Container.app;
		Ut.sendMail(app.getSmtpGmailId(), app.getSmtpGmailPw(), "no-reply@hyungjoon.site", "Brillante 사이트에서 알림", "jhj8664@gmail.com", "제목", "내용");
	}
	
}