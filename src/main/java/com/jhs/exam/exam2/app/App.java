package com.jhs.exam.exam2.app;

import com.jhs.exam.exam2.container.Container;
import com.jhs.exam.exam2.util.Ut;
import com.jhs.mysqliutil.MysqlUtil;

import lombok.Getter;

public class App {
	@Getter
	private static boolean ready = false;
	private static String smtpGmailPw;
	
	public static boolean isDevMode() {
		// 이 부분을 false로 바꾸면 production 모드 이다.
		return true;
	}
	
	public static void init() {
		// DB 세팅
		MysqlUtil.setDBInfo("localhost", "sbsst", "sbs123414", "jsp_board");
		MysqlUtil.setDevMode(isDevMode());
		
		// 필수적으로 로딩되어야 하는 내용 불러오기
		smtpGmailPw = Ut.getFileContents("c:/work/jhj8664/SmtpGmailPw.txt");
		
		// 공용 객체 세팅
		Container.init();
		
		if (smtpGmailPw != null && smtpGmailPw.trim().length() > 0) {
			ready = true;
		}
	}
	
	public static String getSmtpGmailId() {
		return "jhj8664@gmail.com";
	}
	
	public static String getSmtpGmailPw() {
		return smtpGmailPw;
	}
}
