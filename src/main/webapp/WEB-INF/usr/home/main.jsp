<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle" value="홈" />
<%@ include file="../part/head.jspf"%>

<section class="section section-home-main">	
	<div class="container mx-auto card-wrap px-4">
    <div class="card bordered shadow-lg">
      <div class="card-title">
        <a>
          <i class="fas fa-home"></i>
        </a>
        <span>최신 게시물</span>
      </div>

      <div class="px-4 py-4">
        <h1>최신 게시물</h1>
      </div>
    </div>
  </div>
</section>

<%@ include file="../part/foot.jspf"%>