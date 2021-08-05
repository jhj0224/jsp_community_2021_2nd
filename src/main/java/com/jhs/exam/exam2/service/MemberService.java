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

	public ResultData login(String loginId, String loginPw) {
		Member member = getMemberByLoginId(loginId);

		if (member == null) {
			return ResultData.from("F-1", "존재하지 않는 회원의 로그인아이디 입니다.");
		}

		if (member.getLoginPw().equals(loginPw) == false) {
			return ResultData.from("F-2", "비밀번호가 일치하지 않습니다.");
		}

		return ResultData.from("S-1", "환영합니다.", "member", member);
	}

	public ResultData join(String loginId, String loginPw, String name, String nickname, String cellphoneNo,
			String email) {

		Member oldMember = getMemberByLoginId(loginId);

		if (oldMember != null) {
			return ResultData.from("F-1", Ut.f("%s(은)는 이미 사용중인 로그인아이디입니다.", loginId));
		}

		oldMember = getMemberByNameAndEmail(name, email);

		if (oldMember != null) {
			return ResultData.from("F-2", Ut.f("%s님은 이메일 주소 `%s`(으)로 이미 가입하셨습니다.", name, email));
		}

		int id = memberRepository.join(loginId, loginPw, name, nickname, cellphoneNo, email);

		return ResultData.from("S-1", "가입성공", "id", id);
	}

	public Member getMemberByNameAndEmail(String name, String email) {
		return memberRepository.getMemberByNameAndEmail(name, email);
	}

	public Member getMemberByLoginId(String loginId) {
		return memberRepository.getMemberByLoginId(loginId);
	}

	public boolean isAdmin(Member member) {
		return member.getAuthLevel() >= 7;
	}

	public ResultData sendTempLoginPwToEmail(Member actor) {
		App app = Container.app;

		String siteName = app.getSiteName();
		String siteLoginUrl = app.getLoginUri();
		String title = "[" + siteName + "] 임시 패스워드 발송";
		String tempPassword = Ut.getTempPassword(6);
		String body = "<h1>임시 패스워드 : " + tempPassword + "</h1>";
		body += "<a href=\"" + siteLoginUrl + "\" target=\"_blank\">로그인 하러가기</a>";

		if (actor.getEmail().length() == 0) {
			return ResultData.from("F-0", "해당 회원의 이메일이 없습니다.");
		}

		// 메일 발송
		int notifyRs = emailService.notify(actor.getEmail(), title, body);

		if (notifyRs != 1) {
			return ResultData.from("F-1", "메일발송에 실패하였습니다.");
		}

		setTempLoginPw(actor, tempPassword);

		String resultMsg = String.format("고객님의 새 임시 패스워드가 %s (으)로 발송되었습니다.", actor.getEmail());
		return ResultData.from("S-1", resultMsg, "email", actor.getEmail());
	}

	private void setTempLoginPw(Member actor, String tempLoginPw) {
		memberRepository.modifyPassword(actor.getId(), tempLoginPw);
	}
}
