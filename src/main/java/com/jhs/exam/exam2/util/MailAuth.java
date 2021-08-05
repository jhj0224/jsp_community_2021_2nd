package com.jhs.exam.exam2.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

class MailAuth extends Authenticator {

	PasswordAuthentication pa;

	public MailAuth(String mailId, String mailPw) {

		pa = new PasswordAuthentication(mailId, mailPw);
	}

	public PasswordAuthentication getPasswordAuthentication() {
		return pa;
	}
}
