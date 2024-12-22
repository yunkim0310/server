$(function () {

    function paging(page) {

        $("form")[0].reset();
        $("input[name='page']").val(page);

        $("form").attr("action", "/purchase/listPointHistory").attr("method", "post").submit()

    }

    pageNavigator(paging);

});


function updateSearchKeyword() {
    // 카테고리 선택 값을 searchKeyword input에 자동으로 채워넣음
    var selecteddepType = document.getElementById('depType').value;
    console.log(selecteddepType);
    if (selecteddepType !== "0") { // 선택된 값이 '0'이 아닐 때만
        // 쉼표 제거 및 값을 입력
        document.getElementById('searchKeyword').value = selectedCategory.replace(',', '');
    }else{
        self.location = "/purchase/listPointHistory";
    }
    $("form").attr("method", "POST").attr("action", "/purchase/listPurchase").submit();
}