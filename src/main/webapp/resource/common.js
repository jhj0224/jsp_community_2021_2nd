console.log("common.js 로딩");

/* 모바일 탑바 시작 */
function MobileTopBar__init() {
  $('.mobile-top-bar .btn-show-mobile-side-bar').click(function() {
    MobileSideBar__show();
  });
}
/* 모바일 탑바 끝 */

/* 모바일 사이드바 시작 */
function MobileSideBar__init() {
  $('.mobile-side-bar .btn-close-mobile-side-bar').click(function() {
    MobileSideBar__hide();
  })
}

function MobileSideBar__show() {
  $('.mobile-side-bar').addClass('active');
  $('html').addClass('mobile-side-bar-actived');
}

function MobileSideBar__hide() {
  $('.mobile-side-bar').removeClass('active');
  $('html').removeClass('mobile-side-bar-actived');
}
/* 모바일 사이드바 끝 */

/* 탑바 시작 */
function TopBar__init() {
  $('.top-bar .btn-show-search-bar').click(function() {
    SearchBar__show();
  });
}
/* 탑바 끝 */

/* 검색바 시작 */
function SearchBar__init() {
  $('.search-bar .btn-hide-search-bar').click(function() {
    SearchBar__hide();
  });
}

function SearchBar__show() {
  $('html').addClass('search-bar-actived');
  $('.search-bar').addClass('active');
  $('.search-bar form input[name="searchKeyword"]').focus();
}

function SearchBar__hide() {
  $('html').removeClass('search-bar-actived');
  $('.search-bar').removeClass('active');
  $('.search-bar form input[name="searchKeyword"]').val('');
}
/* 검색바 끝 */

MobileTopBar__init();
MobileSideBar__init();
TopBar__init();
SearchBar__init();