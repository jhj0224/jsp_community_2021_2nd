package com.jhs.exam.exam2.dto;

import lombok.Data;

@Data 
public class IsLike {
	private int id;
	private String regDate;
	private String updateDate;
	private String relTypeCode;
	private int relId;
	private int memberId;
	private int point;
}
	