package com.jhs.exam.exam2.app;

import com.jhs.exam.exam2.container.Container;
import com.jhs.exam.exam2.container.ContainerComponent;
import com.jhs.exam.exam2.util.Ut;
import com.jhs.mysqliutil.MysqlUtil;

import lombok.Getter;

public class App implements ContainerComponent {
	@Getter
	private boolean ready = false;
	private String smtpGmailPw;
	
	@Override
	public void init() {
		// 필수적으로 로딩되어야 하는 내용 불러오기
		smtpGmailPw = Ut.getFileContents("c:/work/jhj8664/SmtpGmailPw.txt");
				
		if (smtpGmailPw != null && smtpGmailPw.trim().length() > 0) {
			ready = true;
		}
	}
	
	public static boolean isDevMode() {
		// 이 부분을 false로 바꾸면 production 모드 이다.
		return true;
	}
	
	// 서버에서 돌아가는 모드
	private static boolean isProductMode() {
		return isDevMode() == false;
	}
	
	// 정적 요소 세팅
	public static void start() {
		// DB 세팅
		MysqlUtil.setDBInfo("localhost", "sbsst", "sbs123414", "jsp_board");
		MysqlUtil.setDevMode(isDevMode());
				
		// 공용 객체 세팅
		Container.init();
				
	}
	
	// 이메일 아이디
	public String getSmtpGmailId() {
		return "jhj8664@gmail.com";
	}
	
	// 이메일 비밀번호
	public String getSmtpGmailPw() {
		return smtpGmailPw;
	}

	// 사이트 이름
	public String getSiteName() {
		return "준이의 코딩천국";
	}

	// 아래 코드를 실행시키기 위한 기본 코드(프로토콜,도메인,포트 등)
	public String getBaseUri() {
		String appUri = getSiteProtocol() + "://" + getSiteDomain();

		if (getSitePort() != 80 && getSitePort() != 443) {
			appUri += ":" + getSitePort();
		}

		if (getContextName().length() > 0) {
			appUri += "/" + getContextName();
		}

		return appUri;
	}

	// 현재가 서비스 모드면 "", 그렇지 않으면 앱 이름(jsp_community_2021)
	private String getContextName() {
		if (isProductMode()) {
			return "";
		}

		return "jsp_community_2021";
	}
	
	// 사이트 포트
	private int getSitePort() {
		return 8080;
	}

	// 도메인
	private String getSiteDomain() {
		return "localhost";
	}

	// 프로토콜
	private String getSiteProtocol() {
		return "http";
	}

	// 로그인 페이지
	public String getLoginUri() {
		return getBaseUri() + "/usr/member/login";				
	}

	// 이메일 알림 이름
	public String getNotifyEmailFromName() {
		return "준이의 코딩천국 알림";
	}
}
