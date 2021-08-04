package com.jhs.exam.exam2.http.controller;

import com.jhs.exam.exam2.container.Container;
import com.jhs.exam.exam2.dto.Member;
import com.jhs.exam.exam2.dto.ResultData;
import com.jhs.exam.exam2.http.Rq;
import com.jhs.exam.exam2.service.MemberService;
import com.jhs.exam.exam2.util.Ut;

public class UsrMemberController extends Controller {
	private MemberService memberService; 

	public void init() {
		memberService = Container.memberService;
	}	
	
	@Override
	public void performAction(Rq rq) {
		switch (rq.getActionMethodName()) {
		case "login":
			actionShowLogin(rq);
			break;
		case "doLogin":
			actionDoLogin(rq);
			break;
		case "doLogout":
			actionDoLogout(rq);
			break;
		case "join":
			actionShowJoin(rq);
			break;
		case "doJoin":
			actionDoJoin(rq);
			break;
		case "findLoginId":
			actionShowFindLoginId(rq);
			break;
		case "doFindLoginId":
			actionDoFindLoginId(rq);
			break;
		case "findLoginPw":
			actionShowFindLoginPw(rq);
			break;
		default:
			rq.println("존재하지 않는 페이지 입니다.");
			break;
		}
	}
		
	private void actionShowFindLoginId(Rq rq) {
		rq.jsp("usr/member/findLoginId");
	}

	private void actionDoFindLoginId(Rq rq) {
		String name = rq.getParam("name", "");
		String email = rq.getParam("email", "");
		
		if (name.length() == 0) {
			rq.historyBack("name(을)를 입력해주세요.");
			return;
		}

		if (email.length() == 0) {
			rq.historyBack("email(을)를 입력해주세요.");
			return;
		}
		
		Member oldMember = memberService.getMemberByNameAndEmail(name, email);
		
		if ( oldMember == null ) {
			rq.historyBack("일치하는 회원이 존재하지 않습니다.");
			return;
		}
		
		String replaceUri = "../member/login?loginId=" + oldMember.getLoginId();
		rq.replace(Ut.f("해당 회원의 로그인아이디는 `%s` 입니다.", oldMember.getLoginId()), replaceUri);
		return;
		
	}
	
	private void actionShowFindLoginPw(Rq rq) {
		rq.jsp("usr/member/findLoginPw");
	}
	
	private void actionDoJoin(Rq rq) {
		String loginId = rq.getParam("loginId", "");
		String loginPw = rq.getParam("loginPw", "");
		String name = rq.getParam("name", "");
		String nickname = rq.getParam("nickname", "");
		String cellphoneNo = rq.getParam("cellphoneNo", "");
		String email = rq.getParam("email", "");
		
		if (loginId.length() == 0) {
			rq.historyBack("loginId(을)를 입력해주세요.");
			return;
		}

		if (loginPw.length() == 0) {
			rq.historyBack("loginPw(을)를 입력해주세요.");
			return;
		}

		if (name.length() == 0) {
			rq.historyBack("name(을)를 입력해주세요.");
			return;
		}

		if (nickname.length() == 0) {
			rq.historyBack("nickname(을)를 입력해주세요.");
			return;
		}

		if (cellphoneNo.length() == 0) {
			rq.historyBack("cellphoneNo(을)를 입력해주세요.");
			return;
		}

		if (email.length() == 0) {
			rq.historyBack("email(을)를 입력해주세요.");
			return;
		}
						
		ResultData joinRd = memberService.join(loginId, loginPw, name, nickname, cellphoneNo, email);

		if (joinRd.isFail()) {
			rq.historyBack(joinRd.getMsg());
			return;
		}
		
		rq.replace(joinRd.getMsg(), "../member/login");
	}

	private void actionDoLogout(Rq rq) {
		rq.removeSessionAttr("loginedMemberJson");
		rq.replace(null, "../../");
	}

	private void actionDoLogin(Rq rq) {
		String loginId = rq.getParam("loginId", "");
		String loginPw = rq.getParam("loginPw", "");

		if (loginId.length() == 0) {
			rq.historyBack("loginId를 입력해주세요.");
			return;
		}

		if (loginPw.length() == 0) {
			rq.historyBack("loginPw를 입력해주세요.");
			return;
		}

		ResultData loginRd = memberService.login(loginId, loginPw);

		if (loginRd.isFail()) {
			rq.historyBack(loginRd.getMsg());
			return;
		}

		Member member = (Member) loginRd.getBody().get("member");

		rq.setSessionAttr("loginedMemberJson", Ut.toJson(member, ""));
		
		String redirectUri = rq.getParam("redirectUri", "../article/list");

		rq.replace(loginRd.getMsg(), redirectUri);
	}

	private void actionShowLogin(Rq rq) {
		rq.jsp("usr/member/login");
	}
	
	private void actionShowJoin(Rq rq) {
		rq.jsp("usr/member/join");
	}
}
