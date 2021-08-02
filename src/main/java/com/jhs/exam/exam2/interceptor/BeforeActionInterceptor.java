package com.jhs.exam.exam2.interceptor;

import com.jhs.exam.exam2.container.Container;
import com.jhs.exam.exam2.dto.Member;
import com.jhs.exam.exam2.http.Rq;
import com.jhs.exam.exam2.service.MemberService;
import com.jhs.exam.exam2.util.Ut;

public class BeforeActionInterceptor extends Interceptor {
	
	private MemberService memberService;

	public void init() {
		memberService = Container.memberService;
	}

	@Override
	public boolean runBeforeAction(Rq rq) {
		String loginedMemberJson = rq.getSessionAttr("loginedMemberJson", "");

		if (loginedMemberJson.length() > 0) {
			rq.setLogined(true);
			rq.setLoginedMember(Ut.toObjFromJson(loginedMemberJson, Member.class));
			rq.setLoginedMemberId(rq.getLoginedMember().getId());
			rq.setAdmin(memberService.isAdmin(rq.getLoginedMember()));
		}
		
		rq.setAttr("rq", rq);

		return true;
	}

}
