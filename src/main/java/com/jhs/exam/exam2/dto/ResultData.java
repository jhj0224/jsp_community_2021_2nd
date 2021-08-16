package com.jhs.exam.exam2.dto;

import java.util.Map;

import com.jhs.exam.exam2.util.Ut;

import lombok.Getter;
import lombok.ToString;

// 리턴 받은 값이 true,false 여부를 확인하고 메세지와 필요 데이터를 반환 하기위해 사용하는 클래스
@ToString
public class ResultData {
	@Getter
	private String msg;
	@Getter
	private String resultCode;
	@Getter
	private Map<String, Object> body;
	
	private ResultData() {
		
	}
	
	public boolean isSuccess() {
		return resultCode.startsWith("S-");
	}
	
	public boolean isFail() {
		return !isSuccess();
	}

	public static ResultData from(String resultCode, String msg, Object... bodyArgs) {
		ResultData rd = new ResultData();

		rd.resultCode = resultCode;
		rd.msg = msg;
		rd.body = Ut.mapOf(bodyArgs);

		return rd;
	}
}
