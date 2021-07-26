package com.jhs.exam.exam2.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Ut {
	private static final Map<Class<?>, Class<?>> WRAPPER_TYPE_MAP;
	static {
		WRAPPER_TYPE_MAP = new HashMap<Class<?>, Class<?>>(16);
		WRAPPER_TYPE_MAP.put(Integer.class, int.class);
		WRAPPER_TYPE_MAP.put(Byte.class, byte.class);
		WRAPPER_TYPE_MAP.put(Character.class, char.class);
		WRAPPER_TYPE_MAP.put(Boolean.class, boolean.class);
		WRAPPER_TYPE_MAP.put(Double.class, double.class);
		WRAPPER_TYPE_MAP.put(Float.class, float.class);
		WRAPPER_TYPE_MAP.put(Long.class, long.class);
		WRAPPER_TYPE_MAP.put(Short.class, short.class);
		WRAPPER_TYPE_MAP.put(Void.class, void.class);
	}

	public static boolean isPrimitiveType(Object source) {
		return WRAPPER_TYPE_MAP.containsKey(source.getClass());
	}

	public static boolean isBaseType(Object source) {
		if (isPrimitiveType(source)) {
			return true;
		}

		if (source instanceof String) {
			return true;
		}

		return false;
	}

	public static String f(String format, Object... args) {
		return String.format(format, args);
	}

	public static Map<String, Object> mapOf(Object... args) {
		if (args.length % 2 != 0) {
			throw new IllegalArgumentException("인자를 짝수개 입력해주세요.");
		}

		int size = args.length / 2;

		Map<String, Object> map = new LinkedHashMap<>();

		for (int i = 0; i < size; i++) {
			int keyIndex = i * 2;
			int valueIndex = keyIndex + 1;

			String key;
			Object value;

			try {
				key = (String) args[keyIndex];
			} catch (ClassCastException e) {
				throw new IllegalArgumentException("키는 String으로 입력해야 합니다. " + e.getMessage());
			}

			value = args[valueIndex];

			map.put(key, value);
		}

		return map;
	}

	public static String toJson(Object obj, String defaultValue) {
		ObjectMapper om = new ObjectMapper();

		try {
			return om.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			return defaultValue;
		}
	}

	public static <T> T toObjFromJson(String jsonStr, TypeReference<T> typeReference) {
		ObjectMapper om = new ObjectMapper();

		try {
			return (T) om.readValue(jsonStr, typeReference);
		} catch (JsonProcessingException e) {
			return null;
		}
	}

	public static <T> T toObjFromJson(String jsonStr, Class<T> cls) {
		ObjectMapper om = new ObjectMapper();

		try {
			return (T) om.readValue(jsonStr, cls);
		} catch (JsonProcessingException e) {
			return null;
		}
	}

	public static String getUriEncoded(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (Exception e) {
			return str;
		}
	}

	public static int sendMail(String smtpServerId, String smtpServerPw, String from, String fromName, String to,
			String title, String body) {
		Properties prop = System.getProperties();
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.port", "587");

		Authenticator auth = new MailAuth(smtpServerId, smtpServerPw);

		Session session = Session.getDefaultInstance(prop, auth);

		MimeMessage msg = new MimeMessage(session);

		try {
			msg.setSentDate(new Date());

			msg.setFrom(new InternetAddress(from, fromName));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
			msg.setSubject(title, "UTF-8");
			msg.setContent(body, "text/html; charset=UTF-8");

			Transport.send(msg);

		} catch (AddressException ae) {
			System.out.println("AddressException : " + ae.getMessage());
			return -1;
		} catch (MessagingException me) {
			System.out.println("MessagingException : " + me.getMessage());
			return -2;
		} catch (UnsupportedEncodingException e) {
			System.out.println("UnsupportedEncodingException : " + e.getMessage());
			return -3;
		}

		return 1;
	}

}

class MailAuth extends Authenticator {

	PasswordAuthentication pa;

	public MailAuth(String mailId, String mailPw) {

		pa = new PasswordAuthentication(mailId, mailPw);
	}

	public PasswordAuthentication getPasswordAuthentication() {
		return pa;
	}
} 
