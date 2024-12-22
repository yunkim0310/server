$(function() {

    // submit 함수
    function feedTest(page) {

        $("input[name='page']").val(page);

        $("form[name='pagingForm']").attr("action", "/review/feedTest").attr("method", "GET").submit();
    }
    // 펑션 이름이랑 같게
    pageNavigator(feedTest);

});