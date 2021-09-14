package com.jhs.exam.exam2.container;

import java.util.ArrayList;
import java.util.List;

import com.jhs.exam.exam2.app.App;
import com.jhs.exam.exam2.http.controller.AdmHomeController;
import com.jhs.exam.exam2.http.controller.UsrArticleController;
import com.jhs.exam.exam2.http.controller.UsrHomeController;
import com.jhs.exam.exam2.http.controller.UsrMemberController;
import com.jhs.exam.exam2.interceptor.BeforeActionInterceptor;
import com.jhs.exam.exam2.interceptor.NeedAdminInterceptor;
import com.jhs.exam.exam2.interceptor.NeedLoginInterceptor;
import com.jhs.exam.exam2.interceptor.NeedLogoutInterceptor;
import com.jhs.exam.exam2.repository.ArticleRepository;
import com.jhs.exam.exam2.repository.BoardRepository;
import com.jhs.exam.exam2.repository.LikeRepository;
import com.jhs.exam.exam2.repository.MemberRepository;
import com.jhs.exam.exam2.service.ArticleService;
import com.jhs.exam.exam2.service.BoardService;
import com.jhs.exam.exam2.service.EmailService;
import com.jhs.exam.exam2.service.LikeService;
import com.jhs.exam.exam2.service.MemberService;

// 객체를 저장하는 곳, 크기를 지정할 필요없이 요소가 추가될 때마다 크기를 동적으로 증가시킴 
public class Container {
	private static List<ContainerComponent> containerComponents;
	
	public static App app;
	
	public static BeforeActionInterceptor beforeActionInterceptor;
	public static NeedLoginInterceptor needLoginInterceptor;
	public static NeedLogoutInterceptor needLogoutInterceptor;
	public static NeedAdminInterceptor needAdminInterceptor;

	public static ArticleRepository articleRepository;
	public static ArticleService articleService;
	public static UsrArticleController usrArticleController;

	public static MemberRepository memberRepository;
	public static MemberService memberService;
	public static UsrMemberController usrMemberController;

	public static UsrHomeController usrHomeController;

	public static BoardRepository boardRepository;
	public static BoardService boardService;
	
	public static AdmHomeController admHomeController;

	public static EmailService emailService;

	public static LikeService likeService;

	public static LikeRepository likeRepository;

	public static void init() {
		containerComponents = new ArrayList<>();
				
		// 의존성 세팅 시작
		app = addContainerComponent(new App());
		memberRepository = addContainerComponent(new MemberRepository());
		boardRepository = addContainerComponent(new BoardRepository());
		articleRepository = addContainerComponent(new ArticleRepository());
		likeRepository = addContainerComponent(new LikeRepository());
		
		memberService = addContainerComponent(new MemberService());
		boardService = addContainerComponent(new BoardService());
		articleService = addContainerComponent(new ArticleService());
		likeService = addContainerComponent(new LikeService());

		beforeActionInterceptor = addContainerComponent(new BeforeActionInterceptor());
		needLoginInterceptor = addContainerComponent(new NeedLoginInterceptor());
		needLogoutInterceptor = addContainerComponent(new NeedLogoutInterceptor());
		needAdminInterceptor = addContainerComponent(new NeedAdminInterceptor());

		usrMemberController = addContainerComponent(new UsrMemberController());
		usrArticleController = addContainerComponent(new UsrArticleController());
		usrHomeController = addContainerComponent(new UsrHomeController());
		
		admHomeController = addContainerComponent(new AdmHomeController());
		
		emailService = addContainerComponent(new EmailService());
		
		// 객체 초기화
		// 초기화
		for (ContainerComponent containerComponent : containerComponents) {
			containerComponent.init();
		}
		
	}
		
	// 위의 과정을 한번에 넣기 위해 만든 것, 객체 생성 -> 객체 담기 -> return , 인터넷 참조
	private static <T> T addContainerComponent(ContainerComponent containerComponent) {
		containerComponents.add(containerComponent);

		return (T) containerComponent;
}
}