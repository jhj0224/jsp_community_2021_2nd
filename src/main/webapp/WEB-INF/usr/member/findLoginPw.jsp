<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="로그인비밀번호 찾기" />
<%@ include file="../part/head.jspf"%>

<section class="section section-member-login flex-grow flex justify-center items-center">
	<div class="w-full max-w-md card-wrap">

		<div class="card bordered shadow-lg">
			<div class="card-title">
				<span>
					<i class="fas fa-search"></i>
				</span>
				<span>로그인비밀번호 찾기</span>
			</div>

			<div class="px-4 py-4">
				<script>
					let MemberFindLoginPw__submitDone = false;
					function MemberFindLoginPw__submit(form) {
						if (MemberFindLoginPw__submitDone) {
							return;
						}
						
						form.loginId.value = form.loginId.value.trim();
						if (form.loginId.value.length == 0) {
							alert('이름을 입력해주세요.');
							form.loginId.focus();
							return;
						}
						
						form.email.value = form.email.value.trim();
						if (form.email.value.length == 0) {
							alert('이메일을 입력해주세요.');
							form.email.focus();
							return;
						}
						form.submit();
						MemberFindLoginPw__submitDone = true;
					}
				</script>
				<form action="../member/doFindLoginPw" method="POST"
					onsubmit="MemberFindLoginPw__submit(this); return false;">
					<div class="form-control">
						<label class="label">
							<span class="label-text">로그인아이디</span>
						</label>
						<div>
							<input class="input input-bordered w-full" maxlength="100"
								name="loginId" type="text" placeholder="로그인아이디를 입력해주세요." value="${param.loginId}" />
						</div>
					</div>

					<div class="form-control">
						<label class="label">
							<span class="label-text">이메일</span>
						</label>
						<div>
							<input class="input input-bordered w-full" maxlength="100"
								name="email" type="email" placeholder="이메일을 입력해주세요." />
						</div>
					</div>

					<div class="btns">
						<button type="submit" class="btn btn-link">비밀번호 찾기</button>
						<a href="../member/findLoginPw" class="btn btn-link">아이디 찾기</a>
						<a href="../member/login" class="btn btn-link">로그인</a>
						<a href="../member/join" class="btn btn-link">가입</a>
					</div>
				</form>
			</div>
		</div>
	</div>
</section>
<%@ include file="../part/foot.jspf"%> 