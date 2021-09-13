package com.jhs.exam.exam2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

// Article class 생성 
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Article {
	private int id;
	private String regDate;
	private String updateDate;
	private int boardId;
	private int memberId;
	private String title;
	private String body;
	private int dislikeCount;
	private int likeCount;
	private int hitCount;
	
	// 추가로 만들어진 것 담기, extra는 기존 것과 추가한 것이라는 걸 구분하기 위해 사용함
	private String extra__writerName;
	private String extra__boardName;
	private Boolean extra__actorCanModify;
	private Boolean extra__actorCanDelete;
	
	private Boolean extra__actorCanLike;
	private Boolean extra__actorCanCancelLike;
	private Boolean extra__actorCanDisLike;
	private Boolean extra__actorCanCancelDisLike;
	
	private int extra__likePoint;
	private int extra__likeOnlyPoint;
	private int extra__dislikeOnlyPoint;
	
	// 게시물 리스트 제목 출력
	public String getTitleForPrint() {
		return title;
	}
	
	// 게시물 리스트 본문 요약 출력
	public String getBodySummaryForPrint() {
		return body;
	}
}
