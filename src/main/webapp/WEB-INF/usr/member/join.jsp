<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="회원가입" />
<%@ include file="../part/head.jspf"%>

<section class="section section-member-login flex-grow flex justify-center items-center">
	<div class="w-full max-w-md card-wrap">
		<div class="card bordered shadow-lg">
			<div class="card-title">
				<span>
					<i class="fas fa-user-plus"></i>
				</span>
				<span>회원가입</span>
			</div>

			<div class="px-4 py-4">
				<script>
					let MemberJoin__submitDone = false;
					function MemberJoin__submit(form) {
						if (MemberJoin__submitDone) {
							return;
						}
						
						if ( form.agreementTermsOfService.checked == false ) {
							alert('이용약관에 동의해야 진행할 수 있습니다.');
							form.agreementTermsOfService.focus();
							
							return;
						}
						
						if ( form.agreementPrivacyPolicy.checked == false ) {
							alert('개인정보취급방침에 동의해야 진행할 수 있습니다.');
							form.agreementPrivacyPolicy.focus();
							
							return;
						}
						// 좌우 공백제거
						form.loginId.value = form.loginId.value.trim();
						if (form.loginId.value.length == 0) {
							alert('로그인아이디를 입력해주세요.');
							form.loginId.focus();
							return;
						}
						form.loginPw.value = form.loginPw.value.trim();
						if (form.loginPw.value.length == 0) {
							alert('로그인비밀번호를 입력해주세요.');
							form.loginPw.focus();
							return;
						}
						form.loginPwConfirm.value = form.loginPwConfirm.value
								.trim();
						if (form.loginPwConfirm.value.length == 0) {
							alert('로그인비밀번호 확인을 입력해주세요.');
							form.loginPwConfirm.focus();
							return;
						}
						if (form.loginPw.value != form.loginPwConfirm.value) {
							alert('로그인비밀번호 확인이 일치하지 않습니다.');
							form.loginPwConfirm.focus();
							return;
						}
						form.name.value = form.name.value.trim();
						if (form.name.value.length == 0) {
							alert('이름을 입력해주세요.');
							form.name.focus();
							return;
						}
						form.nickname.value = form.nickname.value.trim();
						if (form.nickname.value.length == 0) {
							alert('별명을 입력해주세요.');
							form.nickname.focus();
							return;
						}
						form.cellphoneNo.value = form.cellphoneNo.value.trim();
						if (form.cellphoneNo.value.length == 0) {
							alert('휴대전화번호를 입력해주세요.');
							form.cellphoneNo.focus();
							return;
						}
						form.email.value = form.email.value.trim();
						if (form.email.value.length == 0) {
							alert('이메일을 입력해주세요.');
							form.email.focus();
							return;
						}
						form.submit();
						MemberJoin__submitDone = true;
					}
				</script>
				<form action="../member/doJoin" method="POST" onsubmit="MemberJoin__submit(this); return false;">
					<input type="hidden" name="redirectUri" value="${param.afterLoginUri}" />

					<div class="form-control">
						<label class="label">
							<span class="label-text">
								<span>이용약관</span>
								<a href="#" class="s-link">다시 확인하기</a>
							</span>
						</label>
						<div>
							<label class="flex">
								<input type="checkbox" class="checkbox" name="agreementTermsOfService">
								<span class="ml-2">이용약관에 동의합니다.</span>
							</label>
						</div>
					</div>

					<div class="form-control">
						<label class="label">
							<span class="label-text">
								<span>개인정보취급방침</span>
								<a href="#" class="s-link">다시 확인하기</a>
							</span>
						</label>
						<div>
							<label class="flex">
								<input type="checkbox" class="checkbox" name="agreementPrivacyPolicy">
								<span class="ml-2">개인정보취급방침에 동의합니다.</span>
							</label>
						</div>
					</div>

					<div class="form-control">
						<label class="label">
							<span class="label-text">로그인아이디</span>
						</label>
						<div>
							<input class="input input-bordered w-full" maxlength="100" name="loginId" type="text" placeholder="로그인아이디를 입력해주세요." />
						</div>
					</div>

					<div class="form-control">
						<label class="label">
							<span class="label-text">로그인비밀번호</span>
						</label>
						<div>
							<input class="input input-bordered w-full" maxlength="100" name="loginPw" type="password" placeholder="로그인비밀번호를 입력해주세요." />
						</div>
					</div>

					<div class="form-control">
						<label class="label">
							<span class="label-text">로그인비밀번호 확인</span>
						</label>
						<div>
							<input class="input input-bordered w-full" maxlength="100" name="loginPwConfirm" type="password" placeholder="로그인비밀번호 확인을 입력해주세요." />
						</div>
					</div>

					<div class="form-control">
						<label class="label">
							<span class="label-text">이름</span>
						</label>
						<div>
							<input class="input input-bordered w-full" maxlength="100" name="name" type="text" placeholder="이름을 입력해주세요." />
						</div>
					</div>

					<div class="form-control">
						<label class="label">
							<span class="label-text">별명</span>
						</label>
						<div>
							<input class="input input-bordered w-full" maxlength="100" name="nickname" type="text" placeholder="별명을 입력해주세요." />
						</div>
					</div>

					<div class="form-control">
						<label class="label">
							<span class="label-text">휴대전화번호</span>
						</label>
						<div>
							<input class="input input-bordered w-full" maxlength="100" name="cellphoneNo" type="tel" placeholder="휴대전화번호를 입력해주세요." />
						</div>
					</div>

					<div class="form-control">
						<label class="label">
							<span class="label-text">이메일</span>
						</label>
						<div>
							<input class="input input-bordered w-full" maxlength="100" name="email" type="email" placeholder="이메일을 입력해주세요." />
						</div>
					</div>

					<div class="btns">
						<button type="submit" class="btn btn-link">회원가입</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</section>
<%@ include file="../part/foot.jspf"%> 