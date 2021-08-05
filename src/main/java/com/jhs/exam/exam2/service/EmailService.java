package com.jhs.exam.exam2.service;

import com.jhs.exam.exam2.app.App;
import com.jhs.exam.exam2.container.Container;
import com.jhs.exam.exam2.container.ContainerComponent;
import com.jhs.exam.exam2.util.Ut;

public class EmailService implements ContainerComponent {
	@Override
	public void init() {

	}

	public int notify(String to, String title, String body) {
		App app = Container.app;
		String smtpGmailId = app.getSmtpGmailId();
		String smtpGmailPw = app.getSmtpGmailPw();
		String from = "no-reply@no-reply.com";
		String fromName = app.getNotifyEmailFromName();

		return Ut.sendMail(smtpGmailId, smtpGmailPw, from, fromName, to, title, body);
	}
}
