package com.jhs.exam.exam2.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jhs.exam.exam2.app.App;
import com.jhs.exam.exam2.container.Container;
import com.jhs.exam.exam2.dto.Member;
import com.jhs.exam.exam2.util.Ut;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Rq {
	private HttpServletRequest req;
	private HttpServletResponse resp;
	@Getter
	private boolean isInvalid = false;
	@Getter
	private String controllerTypeName;
	@Getter
	private String controllerName;
	@Getter
	private String actionMethodName;
	
	@Getter
	@Setter
	private boolean isAdmin = false;	
	
	@Getter
	@Setter
	private boolean isLogined = false;
	
	@Getter
	@Setter
	private int loginedMemberId = 0;
	
	@Getter
	@Setter
	private Member loginedMember = null;
	
	@Getter
	private App app = Container.app;
	
	public boolean isNotLogined() {
		return isLogined == false;
	}

	public Rq(HttpServletRequest req, HttpServletResponse resp) {
		// 들어오는 파리미터를 UTF-8로 해석
		try {
			req.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 서블릿이 HTML 파일을 만들때 UTF-8 로 쓰기
		resp.setCharacterEncoding("UTF-8");

		// HTML이 UTF-8 형식이라는 것을 브라우저에게 알린다.
		resp.setContentType("text/html; charset=UTF-8");

		this.req = req;
		this.resp = resp;

		String requestUri = req.getRequestURI();
		String[] requestUriBits = requestUri.split("/");

		int minBitsCount = 5;

		if (requestUriBits.length < minBitsCount) {
			isInvalid = true;
			return;
		}

		int controllerTypeNameIndex = 2;
		int controllerNameIndex = 3;
		int actionMethodNameIndex = 4;

		this.controllerTypeName = requestUriBits[controllerTypeNameIndex];
		this.controllerName = requestUriBits[controllerNameIndex];
		this.actionMethodName = requestUriBits[actionMethodNameIndex];
	}

	public void print(String str) {
		try {
			resp.getWriter().append(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void println(String str) {
		print(str + "\n");
	}

	// 해당 파일 위치를 변수에 담아 이동시켜주는 메서드
	public void jsp(String jspPath) {
		RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/" + jspPath + ".jsp");
		try {
			requestDispatcher.forward(req, resp);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}

	public String getParam(String paramName, String defaultValue) {
		String paramValue = req.getParameter(paramName);

		if (paramValue == null || paramValue.trim().length() == 0) {
			return defaultValue;
		}

		return paramValue;
	}

	// 사용자가 입력한 값(정수만 가능)을 읽어오는 메서드
	public int getIntParam(String paramName, int defaultValue) {
		// paramName 값을 읽어와 paramValue에 저장
		String paramValue = req.getParameter(paramName);

		// paramValue 값이 null일 경우 defaultValue값을 리턴
		if (paramValue == null) {
			return defaultValue;
		}

		// paramValue값이 있을경우 정수화 하여 리턴
		try {
			return Integer.parseInt(paramValue);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public void printf(String format, Object... args) {
		print(Ut.f(format, args));
	}

	// 입력한 변수 msg를 출력하고 페이지 뒤로가기 메서드
	public void historyBack(String msg) {
		println("<script>");
		if (msg != null && msg.trim().length() > 0) {
			printf("alert('%s');\n", msg.trim());
		}
		println("history.back();");
		println("</script>");
	}

	public void println(Object obj) {
		println(obj.toString());
	}

	public void setAttr(String attrName, Object attrValue) {
		req.setAttribute(attrName, attrValue);
	}

	public void replace(String msg, String redirectUri) {
		println("<script>");
		if (msg != null && msg.trim().length() > 0) {
			printf("alert('%s');\n", msg.trim());
		}
		printf("location.replace('%s');\n", redirectUri);
		println("</script>");
	}

	public void setSessionAttr(String attrName, String attrValue) {
		req.getSession().setAttribute(attrName, attrValue);
	}

	public void removeSessionAttr(String attrName) {
		req.getSession().removeAttribute(attrName);
	}

	public <T> T getSessionAttr(String attrName, T defaultValue) {
		if (req.getSession().getAttribute(attrName) == null) {
			return defaultValue;
		}

		return (T) req.getSession().getAttribute(attrName);
	}

	public String getActionPath() {
		return "/" + controllerTypeName + "/" + controllerName + "/" + actionMethodName;
	}
	
	public String getStringAttr(String attrName, String defaultValue) {
		String attrValue = (String)req.getAttribute(attrName);

		if (attrValue == null) {
			return defaultValue;
		}

		return attrValue;
	}

	public int getIntAttr(String attrName, int defaultValue) {
		Integer attrValue = (Integer)req.getAttribute(attrName);

		if (attrValue == null) {
			return defaultValue;
		}

		return attrValue;
	}
	
	private Map<String, Object> getParamMap() {
		Map<String, Object> params = new HashMap<>();

		Enumeration<String> parameterNames = req.getParameterNames();

		while (parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			Object paramValue = req.getParameter(paramName);

			params.put(paramName, paramValue);
		}

		return params;
	}

	public String getParamMapJsonStr() {
		return Ut.toJson(getParamMap(), "");
	}
	
	private Map<String, Object> getBaseTypeAttrMap() {
		Map<String, Object> attrs = new HashMap<>();

		Enumeration<String> attrNames = req.getAttributeNames();

		while (attrNames.hasMoreElements()) {
			String attrName = attrNames.nextElement();
			Object attrValue = req.getAttribute(attrName);

			if ( attrName.equals("rq") ) {
				continue;
			}

			if ( Ut.isBaseType(attrValue) == false ) {
				continue;
			}

			attrs.put(attrName, attrValue);
		}

		return attrs;
	}

	public String getBaseTypeAttrMapJsonStr() {
		return Ut.toJson(getBaseTypeAttrMap(), "");
	}
	
	public String getCurrentUri() {
		String uri = req.getRequestURI();
		String queryStr = req.getQueryString();

		if ( queryStr != null && queryStr.length() > 0 ) {
			uri += "?" + queryStr;
		}

		return uri;
	}

	public String getEncodedCurrentUri() {
		return Ut.getUriEncoded(getCurrentUri());
	}
	
	public String getEncodedAfterLoginUri() {
		return Ut.getUriEncoded(getAfterLoginUri());
	}

	public String getAfterLoginUri() {
		String afterLoginUri = getParam("afterLoginUri", "");

		if ( afterLoginUri.length() > 0 ) {
			return afterLoginUri;
		}

		return getCurrentUri();
	}

	public void debugParams() {
		print("<h1>debugParams</h1>");
		print("<pre>");
		print(Ut.toPrettyJson(getParamMap(), ""));
		print("</pre>");
		
	}
	
	public boolean isNotAdmin() {
		return !isAdmin;
	}
}
