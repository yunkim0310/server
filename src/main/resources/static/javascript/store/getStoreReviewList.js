$(function () {

    // submit 함수
    function getStoreReviewList(page) {

        const storeId = $("input[name='storeId']:hidden").val();
        const path = window.location.pathname;
        const queryString = (path === "/getStore") ? `mode=review&storeId=${storeId}&page=${page}` : `mode=review&page=${page}`;

        window.location.href = path + '?' +queryString;
    }

    // 페이징 함수
    pageNavigator(getStoreReviewList);

})