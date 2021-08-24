package com.jhs.exam.exam2.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

// 메일 발송하는 클래스
class MailAuth extends Authenticator {

	PasswordAuthentication pa;
	
	public MailAuth(String mailId, String mailPw) {

		pa = new PasswordAuthentication(mailId, mailPw);
	}

	public PasswordAuthentication getPasswordAuthentication() {
		return pa;
	}
}
