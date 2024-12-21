getReviewList.js

$(function() {

    // submit 함수
    function getReviewList(page) {

        $("input[name='page']").val(page);

        $("form[name='pagingForm']").attr("action", "/review/getReviewList").attr("method", "GET").submit();
    }
    // 펑션 이름이랑 같게
    pageNavigator(getReviewList);

});