package com.jhs.exam.exam2.http.controller;

import com.jhs.exam.exam2.http.Rq;
import com.jhs.exam.exam2.util.Ut;

public class AdmHomeController extends Controller {
	
	public void init() {
		
	}
	
	// adm/home/main으로 제대로 들어가면 actionShowMain으로, 그렇지 않다면 존재하지 않는 페이지로
	// ActionMethodName이 아래 case와 일치하면 해당 함수로 이동
	@Override
	public void performAction(Rq rq) {
		switch (rq.getActionMethodName()) {
		case "main":
			actionShowMain(rq);
			break;
		// 일치하지 않을시 오류메세지 출력후 break;	
		default:
			rq.println("존재하지 않는 페이지 입니다.");
			break;
		}
	}

	// 관리자 페이지 보여주기
	private void actionShowMain(Rq rq) {
		rq.print("관리자 페이지 입니다.");
	}
}