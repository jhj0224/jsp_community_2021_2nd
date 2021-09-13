package com.jhs.exam.exam2.http.controller;

import java.util.List;

import com.jhs.exam.exam2.container.Container;
import com.jhs.exam.exam2.dto.Article;
import com.jhs.exam.exam2.dto.Board;
import com.jhs.exam.exam2.dto.ResultData;
import com.jhs.exam.exam2.http.Rq;
import com.jhs.exam.exam2.service.ArticleService;
import com.jhs.exam.exam2.service.BoardService;
import com.jhs.exam.exam2.util.Ut;

public class UsrArticleController extends Controller {
	// articleService와 boardService를 사용하기 위해 Container에 생성된 해당 객체 불러오기
	private ArticleService articleService;
	private BoardService boardService;
		
	public void init() {
		articleService = Container.articleService;
		boardService = Container.boardService;		
	}

	@Override
	public void performAction(Rq rq) {
		// ActionMethodName이 아래 case와 일치하면 해당 함수로 이동
		switch (rq.getActionMethodName()) {
		case "list":
			actionShowList(rq);
			break;
		case "detail":
			actionShowDetail(rq);
			break;
		case "write":
			actionShowWrite(rq);
			break;
		case "doWrite":
			actionDoWrite(rq);
			break;
		case "modify":
			actionShowModify(rq);
			break;
		case "doModify":
			actionDoModify(rq);
			break;
		case "doDelete":
			actionDoDelete(rq);
			break;
		// 일치하지 않을시 오류메세지 출력후 break;
		default:
			rq.println("존재하지 않는 페이지 입니다.");
			break;
		}
	}

	
	// 페이지에서 게시물을 삭제 하는 메서드
	// rq.replace(Ut.f("%d번 게시물을 삭제하였습니다.", id), redirectUri);
	private void actionDoDelete(Rq rq) {
		// 해당 게시물의 id값을 rq.getIntParam()를 사용하여 불러온다.
		int id = rq.getIntParam("id", 0);
		int boardId = rq.getIntParam("boardId", 0);

		// id가 0일 경우 메세지 출력 후 뒤로가기
		if (id == 0) {
			rq.historyBack("id를 입력해주세요.");
			return;
		}
		// 해당 id번 게시물을 불러오는 메서드
		Article article = articleService.getForPrintArticleById(rq.getLoginedMember(), id);

		// id번 게시물이 존재 하지 않을시 메세지 출력후 뒤로가기
		if (article == null) {
			rq.historyBack(Ut.f("`%d`번 게시물은 존재하지 않습니다.", id));
			return;
		}

		// 로그인 회원 정보와 게시물의 작성자 정보를 비교해 권한여부를 파악하고 해당 메세지를 리턴받는 메서드
		ResultData actorCanDeleteRd = articleService.actorCanDelete(rq.getLoginedMember(), article);

		// actorCanDelete가 F-로 시작할시 오류 메세지 출력후 리턴
		// F-로 시작할시 권한이 없다는것을 의미
		if (actorCanDeleteRd.isFail()) {
			rq.historyBack(actorCanDeleteRd.getMsg());
			return;
		}

		// 해당 id의 게시물을 삭제하는 메서드
		ResultData deleteRd = articleService.delete(id);

		// 삭제 완료 메세지 출력후 해당 주소로 이동
		rq.replace(deleteRd.getMsg(), "../article/list?boardId=" + boardId);

	}

	
	// 게시물 상세보기 메서드
	private void actionShowDetail(Rq rq) {
		// 해당 게시물의 id값을 rq.getIntParam()를 사용하여 불러온다.
		int id = rq.getIntParam("id", 0);

		// id값이 0이면 메세지 출력후 뒤로가기
		if (id == 0) {
			rq.historyBack("id를 입력해주세요.");
			return;
		}
		// 해당 id번 게시물 찾기
		Article article = articleService.getForPrintArticleById(rq.getLoginedMember(), id);

		// id번 게시물이 존재 하지 않을시 메세지 출력후 뒤로가기
		if (article == null) {
			rq.historyBack(Ut.f("`%d`번 게시물은 존재하지 않습니다.", id));
			return;
		}
						
		// 페이지에 article값을 사용하기 위해 쓰는 메서드
		rq.setAttr("article", article);			

		// 해당 페이지 이동하는 메서드
		rq.jsp("usr/article/detail");
	}

	
	// 게시물 리스트를 보여주는 메서드
	private void actionShowList(Rq rq) {
		// 게시판 번호를 받아 저장 없을시 0(전체 게시판) 저장
		int boardId = rq.getIntParam("boardId", 0);

		// 검색 타입 받아 저장 없을시 title,body 세팅
		String searchKeywordTypeCode = rq.getParam("searchKeywordTypeCode", "title,body");
		// 검색 값 받아 저장
		String searchKeyword = rq.getParam("searchKeyword", "");
		// 페이지값 받아 저장 없을시 첫번째 페이지 1
		int page = rq.getIntParam("page", 1);
		// 페이지별 보여줄 게시물의 수
		int itemsCountInAPage = 5;

		// 검색타입, 검색값, 게시판번호를 변수로 총 보여줄 게시물수를 받아 저장
		int totalItemsCount = articleService.getArticlesCount(searchKeywordTypeCode, searchKeyword, boardId);
		// 로그인회원 정보로 해당 게시물의 수정,삭제 권한 확인 나머지 변수에 맞는 게시물리스트 받아 저장
		List<Article> articles = articleService.getForPrintArticles(rq.getLoginedMember(), itemsCountInAPage, page,
				searchKeywordTypeCode, searchKeyword, boardId);

		int totalPage = (int) Math.ceil((double) totalItemsCount / itemsCountInAPage);
		// 게시판 변수 사용을 위해 받아 저장
		Board board = boardService.getBoardById(boardId);

		// 페이지에 필요가 변수를 setAttr에 저장해 해당 페이지에서 사용
		rq.setAttr("totalItemsCount", totalItemsCount);
		rq.setAttr("board", board);
		rq.setAttr("boardId", boardId);
		rq.setAttr("searchKeywordTypeCode", searchKeywordTypeCode);
		rq.setAttr("searchKeyword", searchKeyword);
		rq.setAttr("totalPage", totalPage);
		rq.setAttr("page", page);
		rq.setAttr("articles", articles);

		// 해당 페이지로 이동
		rq.jsp("usr/article/list");
	}

	
	// 게시물작성 jsp에서 연결(작성된 내용을 DB에 저장하는 함수)
	private void actionDoWrite(Rq rq) {
		// 게시물 작성페이지에서 해당 변수를 받아 저장
		int boardId = rq.getIntParam("boardId", 0);
		String title = rq.getParam("title", "");
		String body = rq.getParam("body", "");

		// 로그인한 회원의 id값을 받아 저장
		int memberId = rq.getLoginedMemberId();

		// 게시물 작성페이지에서 해당 변수를 받아 저장 없을시 ../article/list 저장
		String redirectUri = rq.getParam("redirectUri", "../article/list");

		// 필요 변수 없이 비정상적으로 접근시 리턴
		if (boardId == 0) {
			rq.historyBack("boardId를 입력해주세요.");
			return;
		}

		if (title.length() == 0) {
			rq.historyBack("title를 입력해주세요.");
			return;
		}

		if (body.length() == 0) {
			rq.historyBack("body를 입력해주세요.");
			return;
		}

		// 해당 변수를 이용하여 게시물 작성
		ResultData writeRd = articleService.write(boardId, memberId, title, body);

		if (writeRd.isFail()) {
			rq.historyBack(writeRd.getMsg());
			return;
		}

		// 해당 게시물의 id값 받아 저장
		int id = (int) writeRd.getBody().get("id");

		// 이동할 페이지의 [NEW_ID]값을 id값으로 변경
		redirectUri = redirectUri.replace("[NEW_ID]", id + "");

		// 성공메세지 출력후 해당 페이지로 이동
		rq.replace(writeRd.getMsg(), redirectUri);

	}

	// 해당 페이지로 이동하는 함수
	private void actionShowWrite(Rq rq) {
		rq.jsp("usr/article/write");
	}

	
	// 게시물 수정 페이지에서 연결(변경될 게시물 내용을 DB에 저장하는 함수)
	private void actionDoModify(Rq rq) {
		// 게시물 수정 페이지에서 변수 받아 저장
		int id = rq.getIntParam("id", 0);
		String title = rq.getParam("title", "");
		String body = rq.getParam("body", "");

		// 게시물 수정 페이지에서 redirectUri 받아 저장 없을시 "../article/detail?id=" + id 저장
		String redirectUri = rq.getParam("redirectUri", "../article/detail?id=" + id);

		// 비정상적으로 필요 변수 없이 접근시 오류메세지 출력후 뒤로가기
		if (id == 0) {
			rq.historyBack("id를 입력해주세요.");
			return;
		}

		if (title.length() == 0) {
			rq.historyBack("title를 입력해주세요.");
			return;
		}

		if (body.length() == 0) {
			rq.historyBack("body를 입력해주세요.");
			return;
		}

		// id에 맞는 게시물을 받아와 저장 하는 메서드
		// 로그인 member 정보로 수정,삭제 권한 세팅
		Article article = articleService.getForPrintArticleById(rq.getLoginedMember(), id);

		// id번 게시물이 없을시 메세지 출력후 뒤로가기
		if (article == null) {
			rq.historyBack(Ut.f("`%d`번 게시물은 존재하지 않습니다.", id));
			return;
		}

		// 찾은 게시물에 작성자와 로그인한 멤버의 정보로 수정,삭제 권한 여부 확인하는 메서드
		ResultData actorCanModifyRd = articleService.actorCanModify(rq.getLoginedMember(), article);

		// 권한이 없을경우 메세지 출력후 뒤로가기
		if (actorCanModifyRd.isFail()) {
			rq.historyBack(actorCanModifyRd.getMsg());
			return;
		}
		// id번 게시물의 title과 body를 수정하는 메서드
		ResultData modifyRd = articleService.modify(id, title, body);

		// 성공메세지 출력후 redirectUri 페이지로 이동하는 메서드
		rq.replace(modifyRd.getMsg(), redirectUri);
	}

	
	// 수정할 게시물이 존재하는 확인하는 함수
	private void actionShowModify(Rq rq) {
		// 수정페이지로 이동시 해당 게시물 번호를 받아 저장
		int id = rq.getIntParam("id", 0);

		// 비정상적으로 id가 없이 수정페이지 이동시 오류메세지 출력후 뒤로가기
		if (id == 0) {
			rq.historyBack("id를 입력해주세요.");
			return;
		}

		// id번 게시물은 찾는 메서드
		// 로그인 멤버 정보로 수정,삭제 권한 세팅
		Article article = articleService.getForPrintArticleById(rq.getLoginedMember(), id);

		// id번 게시물이 존재하지 않을시 오류메세지 출력후 뒤로가기
		if (article == null) {
			rq.historyBack(Ut.f("`%d`번 게시물은 존재하지 않습니다.", id));
			return;
		}

		// 로그인 멤버의 정보와 게시물의 정보로 수정,삭제 권한 있는지 확인 하는 메서드
		ResultData actorCanModifyRd = articleService.actorCanModify(rq.getLoginedMember(), article);

		// 권한이 없을시 메세지 출력후 뒤로가기
		if (actorCanModifyRd.isFail()) {
			rq.historyBack(actorCanModifyRd.getMsg());
			return;
		}

		// 수정 페이지에서 필요한 변수를 보내는 메서드
		rq.setAttr("article", article);

		// 해당 페이지로 이동하는 메서드
		rq.jsp("usr/article/modify");
	}
}
