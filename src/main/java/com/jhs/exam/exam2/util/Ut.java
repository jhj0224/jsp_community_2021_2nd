package com.jhs.exam.exam2.util;

import java.io.FileInputStream;
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

import com.fasterxml.jackson.core.JsonGenerator;
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

	// 베이스 타입만 리턴
	public static boolean isPrimitiveType(Object source) {
		return WRAPPER_TYPE_MAP.containsKey(source.getClass());
	}

	// 해당 변수가 베이스 타입인지 여부 확인하는 메서드
	public static boolean isBaseType(Object source) {
		if (isPrimitiveType(source)) {
			return true;
		}

		if (source instanceof String) {
			return true;
		}

		return false;
	}

	// 지정된 문자열의 형식 항목을 지정된 배열에 있는 해당 개체의 문자열 표현으로 바꿈
	public static String f(String format, Object... args) {
		return String.format(format, args);
	}

	// map을 편하게 만들어 주는 메서드
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

	// ObjectMapper 클래스를 이용하여 java 오브젝트를 json형식으로 변환시켜주는 메서드
	public static String toJson(Object obj, String defaultValue) {
		ObjectMapper om = new ObjectMapper();

		try {
			return om.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			return defaultValue;
		}
	}

	// java오브젝트를 json 형식으로 보기 편한게 보여주는 메서드
	public static String toPrettyJson(Object obj, String defaultValue) {
		ObjectMapper om = new ObjectMapper();
		om.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);

		try {
			return om.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			return defaultValue;
		}
	}

	// json 형태의 문자를 해당유형으로 바꿔주는 메서드
	public static <T> T toObjFromJson(String jsonStr, TypeReference<T> typeReference) {
		ObjectMapper om = new ObjectMapper();

		try {
			return (T) om.readValue(jsonStr, typeReference);
		} catch (JsonProcessingException e) {
			return null;
		}
	}

	// json 형태의 문자를 class형식으로 바꿔주는 메서드
	public static <T> T toObjFromJson(String jsonStr, Class<T> cls) {
		ObjectMapper om = new ObjectMapper();

		try {
			return (T) om.readValue(jsonStr, cls);
		} catch (JsonProcessingException e) {
			return null;
		}
	}

	// 해당 변수를 인코딩해주는 메서드
	public static String getUriEncoded(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (Exception e) {
			return str;
		}
	}

	// 메일을 보내주는 메서드
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

	// 파일내용 읽어오기
	public static String getFileContents(String filePath) {
		String rs = null;
		try {
			// 바이트 단위로 파일읽기
			FileInputStream fileStream = null; // 파일 스트림

			fileStream = new FileInputStream(filePath);// 파일 스트림 생성
			// 버퍼 선언
			byte[] readBuffer = new byte[fileStream.available()];
			while (fileStream.read(readBuffer) != -1) {
			}

			rs = new String(readBuffer);

			fileStream.close(); // 스트림 닫기
		} catch (Exception e) {
			e.getStackTrace();
		}

		return rs;
	}

	// 임시 비밀번호를 만들어주는 메서드
	public static String getTempPassword(int length) {
		int index = 0;
		char[] charArr = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
				'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < length; i++) {
			index = (int) (charArr.length * Math.random());
			sb.append(charArr[index]);
		}

		return sb.toString();
	}
}
