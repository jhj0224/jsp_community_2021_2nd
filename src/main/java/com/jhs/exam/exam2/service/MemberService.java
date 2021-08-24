package com.jhs.exam.exam2.service;

import com.jhs.exam.exam2.app.App;
import com.jhs.exam.exam2.container.Container;
import com.jhs.exam.exam2.container.ContainerComponent;
import com.jhs.exam.exam2.dto.Member;
import com.jhs.exam.exam2.dto.ResultData;
import com.jhs.exam.exam2.repository.MemberRepository;
import com.jhs.exam.exam2.util.Ut;

public class MemberService implements ContainerComponent {
	private MemberRepository memberRepository;
	private EmailService emailService;

	public void init() {
		memberRepository = Container.memberRepository;
		emailService = Container.emailService;
	}

	// 로그인 여부를 확인하는 메서드
	public ResultData login(String loginId, String loginPw) {
		// loginId를 이용하여 member값을 구함
		Member member = getMemberByLoginId(loginId);

		// member값이 존재하지 않을 시 F-1 리턴
		if (member == null) {
			return ResultData.from("F-1", "존재하지 않는 회원의 로그인아이디 입니다.");
		}

		// 찾은 member의 loginPw값과 입력받은 loginPw와 비교하여 틀릴시 F-2 리턴
		if (member.getLoginPw().equals(loginPw) == false) {
			return ResultData.from("F-2", "비밀번호가 일치하지 않습니다.");
		}

		// 구한 member값을 저장하고 S-1 리턴
		return ResultData.from("S-1", "환영합니다.", "member", member);
	}

	// 회원가입 함수
	public ResultData join(String loginId, String loginPw, String name, String nickname, String cellphoneNo,
			String email) {

		// loginId를 이용하여 member값을 구함
		Member oldMember = getMemberByLoginId(loginId);

		// member값이 존재 하면 F-1 저장후 리턴
		if (oldMember != null) {
			return ResultData.from("F-1", Ut.f("%s(은)는 이미 사용중인 로그인아이디입니다.", loginId));
		}

		// name과 email를 통하여 member값을 구해옴
		oldMember = getMemberByNameAndEmail(name, email);

		// member값이 존재 하면 F-2 저장후 리턴
		if (oldMember != null) {
			return ResultData.from("F-2", Ut.f("%s님은 이메일 주소 `%s`(으)로 이미 가입하셨습니다.", name, email));
		}

		// 해당 변수를 이용하여 member를 만들어주는 함수
		int id = memberRepository.join(loginId, loginPw, name, nickname, cellphoneNo, email);

		// S-1저장후 리턴
		return ResultData.from("S-1", "가입성공", "id", id);
	}

	// name, email로 해당 member값을 불러와 리턴하는 함수
	public Member getMemberByNameAndEmail(String name, String email) {
		return memberRepository.getMemberByNameAndEmail(name, email);
	}

	// loginId로 해당 member값을 불러와 리턴하는 함수
	public Member getMemberByLoginId(String loginId) {
		return memberRepository.getMemberByLoginId(loginId);
	}

	// 해당 member가 관리자인지 아닌지 판별하는 함수
	public boolean isAdmin(Member member) {
		return member.getAuthLevel() >= 7;
	}

	// 메일을 발송하는 함수
	public ResultData sendTempLoginPwToEmail(Member actor) {
		App app = Container.app;
		// 메일 제목과 내용 만들기
		String siteName = app.getSiteName();
		String siteLoginUrl = app.getLoginUri();
		String title = "[" + siteName + "] 임시 패스워드 발송";
		String tempPassword = Ut.getTempPassword(6);
		String body = "<h1>임시 패스워드 : " + tempPassword + "</h1>";
		// 내용 + 해당 사이트 로그인페이지로 이동하는 태그 생성
		body += "<a href=\"" + siteLoginUrl + "\" target=\"_blank\">로그인 하러가기</a>";

		// 해당 member의 이메일이 존재하지 않을시 F-0 저장후 리턴
		if (actor.getEmail().length() == 0) {
			return ResultData.from("F-0", "해당 회원의 이메일이 없습니다.");
		}

		// 메일을 발송해주는 함수, 만약 notifyRs 값이 1이 아니면 메일 발송에 실패 한 것
		int notifyRs = emailService.notify(actor.getEmail(), title, body);

		// notifyRs가 1이 아니면 F-1 저장후 리턴
		if (notifyRs != 1) {
			return ResultData.from("F-1", "메일발송에 실패하였습니다.");
		}

		// 해당 member의 비밀번호를 변경하는 메서드
		setTempLoginPw(actor, tempPassword);

		String resultMsg = String.format("고객님의 새 임시 패스워드가 %s (으)로 발송되었습니다.", actor.getEmail());
		// 메일발송, 비밀번호 변경이 완료 되면 S-1, 완료 메세지 저장후 리턴
		return ResultData.from("S-1", resultMsg, "email", actor.getEmail());
	}

	// DB에 접근하여 해당 멤버 비밀번호 변경하는 함수
	private void setTempLoginPw(Member actor, String tempLoginPw) {
		memberRepository.modifyPassword(actor.getId(), tempLoginPw);
	}
}
