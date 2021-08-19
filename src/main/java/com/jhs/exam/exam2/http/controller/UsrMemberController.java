package com.jhs.exam.exam2.http.controller;

import com.jhs.exam.exam2.container.Container;
import com.jhs.exam.exam2.dto.Member;
import com.jhs.exam.exam2.dto.ResultData;
import com.jhs.exam.exam2.http.Rq;
import com.jhs.exam.exam2.service.MemberService;
import com.jhs.exam.exam2.util.Ut;

public class UsrMemberController extends Controller {
	// memberService객체를 Container에서 받아온다
	private MemberService memberService; 

	public void init() {
		memberService = Container.memberService;
	}	
	// ActionMethodName이 아래 case와 일치하면 해당 함수로 이동
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
		case "doFindLoginPw":
			actionDoFindLoginPw(rq);
			break;
		// 일치하지 않을시 오류메세지 출력후 break;	
		default:
			rq.println("존재하지 않는 페이지 입니다.");
			break;
		}
	}	
	
	// 로그인아이디 찾기 페이지로 이동
	private void actionShowFindLoginId(Rq rq) {
		rq.jsp("usr/member/findLoginId");
	}

	// 로그인아이디 찾기 함수(로그인아이디 찾기 페이지에서 이동)
	private void actionDoFindLoginId(Rq rq) {
		// 로그인 아이디 찾기 페이지에서 받은 파라미터 값을 변수에 저장
		String name = rq.getParam("name", "");
		String email = rq.getParam("email", "");
		
		// 파라미터 값이 없을 경우 메세지 출력 후 뒤로가기
		if (name.length() == 0) {
			rq.historyBack("name(을)를 입력해주세요.");
			return;
		}

		if (email.length() == 0) {
			rq.historyBack("email(을)를 입력해주세요.");
			return;
		}
		
		// 해당 변수에 일치하는 회원을 찾아주는 메서드
		Member oldMember = memberService.getMemberByNameAndEmail(name, email);
		
		// 일치하는 회원이 없을 경우 메세지 출력 후 뒤로가기
		if ( oldMember == null ) {
			rq.historyBack("일치하는 회원이 존재하지 않습니다.");
			return;
		}
		
		// 이동 할 페이지를 로그인아이디를 포함해 저장
		String replaceUri = "../member/login?loginId=" + oldMember.getLoginId();
		// 찾은 로그인 아이디를 보여주고 페이지 이동 후 리턴
		rq.replace(Ut.f("해당 회원의 로그인아이디는 `%s` 입니다.", oldMember.getLoginId()), replaceUri);
		return;
		
	}
	
	// 비밀번호 찾기 페이지로 이동
	private void actionShowFindLoginPw(Rq rq) {
		rq.jsp("usr/member/findLoginPw");
	}
	
	// 비밀번호 찾기 메서드(비밀번호 찾기 페이지에서 이동)
	private void actionDoFindLoginPw(Rq rq) {
		// 입력한 loginId와 email을 받아 변수에 저장
		String loginId = rq.getParam("loginId", "");
		String email = rq.getParam("email", "");

		// 비정상적으로 loginId와 email이 없을 경우 메세지 출력 후 뒤로가기
		if (loginId.length() == 0) {
			rq.historyBack("loginId(을)를 입력해주세요.");
			return;
		}

		if (email.length() == 0) {
			rq.historyBack("email(을)를 입력해주세요.");
			return;
		}

		// loginId로 해당 member 찾는 메서드
		Member oldMember = memberService.getMemberByLoginId(loginId);

		// member가 존재하지 않을시 메세지 출력 후 뒤로가기
		if (oldMember == null) {
			rq.historyBack("일치하는 회원이 존재하지 않습니다.");
			return;
		}
		
		// member의 이메일과 입력한 이메일이 틀릴시 메세지 출력 후 뒤로가기
		if (oldMember.getEmail().equals(email) == false) {
			rq.historyBack("일치하는 회원이 존재하지 않습니다.");
			return;
		}

		// 해당 member의 비밀번호를 임시비밀번호로 바꾸고 해당 이메일로 보내주는 메서드
		ResultData sendTempLoginPwToEmailRd = memberService.sendTempLoginPwToEmail(oldMember);

		// sendTempLoginPwToEmailRd 변수가 F-로 시작할시 메세지 출력후 뒤로가기
		if ( sendTempLoginPwToEmailRd.isFail() ) {
			rq.historyBack(sendTempLoginPwToEmailRd.getMsg());
			return;
		}

		// sendTempLoginPwToEmailRd 변수가 S-로 시작할시 메세지 출력후 메인 페이지로 이동하는 메서드
		rq.replace(sendTempLoginPwToEmailRd.getMsg(), "../home/main");
		return;
		
	}
	
	// 회원가입 함수(회원가입 페이지에서 연결)
	private void actionDoJoin(Rq rq) {
		// 회원가입 페이지에서 받아온 파라미터를 변수에 저장
		String loginId = rq.getParam("loginId", "");
		String loginPw = rq.getParam("loginPw", "");
		String name = rq.getParam("name", "");
		String nickname = rq.getParam("nickname", "");
		String cellphoneNo = rq.getParam("cellphoneNo", "");
		String email = rq.getParam("email", "");
		
		// 파라미터 값이 없을 경우 메세지 출력 후 뒤로가기
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
			
		// 저장한 변수를 이용하여 회원가입하는 메서드
		ResultData joinRd = memberService.join(loginId, loginPw, name, nickname, cellphoneNo, email);

		// joinRd값이 f-로 시작시 메세지 출력 후 뒤로가기
		if (joinRd.isFail()) {
			rq.historyBack(joinRd.getMsg());
			return;
		}
		
		// 메세지 출력 후 해당 페이지로 이동
		rq.replace(joinRd.getMsg(), "../member/login");
	}

	// 로그아웃 메서드
	private void actionDoLogout(Rq rq) {
		// 로그인된 json 형식의 로그인멤버를 삭제
		rq.removeSessionAttr("loginedMemberJson");
		rq.replace(null, "../../");
	}

	// 로그인 가능 여부를 판별하는 메서드(로그인 페이지에서 연결)
	private void actionDoLogin(Rq rq) {
		// 로그인 페이지에서 받아온 파라미터를 해당 변수에 저장
		String loginId = rq.getParam("loginId", "");
		String loginPw = rq.getParam("loginPw", "");

		// 파라미터 값이 없을 경우 메세지 출력 후 뒤로가기
		if (loginId.length() == 0) {
			rq.historyBack("loginId를 입력해주세요.");
			return;
		}

		if (loginPw.length() == 0) {
			rq.historyBack("loginPw를 입력해주세요.");
			return;
		}

		// 해당 변수를 이용하여 로그인 여부 확인하는 메서드, Rd = ResultData
		ResultData loginRd = memberService.login(loginId, loginPw);

		// loginId값이 F-로 시작시 메세지 출력 후 뒤로가기
		if (loginRd.isFail()) {
			rq.historyBack(loginRd.getMsg());
			return;
		}

		// loginRd의 body에 있는 member를 가져와 저장
		Member member = (Member) loginRd.getBody().get("member");

		// member값을 loginedMemberJson라는 이름으로 json형식으로 저장
		rq.setSessionAttr("loginedMemberJson", Ut.toJson(member, ""));
		
		String redirectUri = rq.getParam("redirectUri", "../article/list");

		// 성공 메세지 출력 후 해당 페이지로 이동
		rq.replace(loginRd.getMsg(), redirectUri);
	}

	// 로그인 페이지로 이동 하는 함수
	private void actionShowLogin(Rq rq) {
		rq.jsp("usr/member/login");
	}
	
	// 회원가입 페이지로 이동 하는 함수
	private void actionShowJoin(Rq rq) {
		rq.jsp("usr/member/join");
	}
}
