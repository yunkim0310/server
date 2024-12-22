$(function () {


    $("#searchKeyword").autocomplete({
        source: function (request, response) {
            $.ajax({
                url: "/api-product/json/getProductAutocomplete/" + encodeURIComponent(request.term),
                method: "GET",
                dataType: "json",
                success: function (JSONData) {
                    response(JSONData);
                }
            });
        }
    });

    function paging(page) {

        $("form")[0].reset();
        $("input[name='page']").val(page);

        $("form").attr("action", "/purchase/listPurchase").attr("method", "post").submit()

    }

    pageNavigator(paging);

    $("button.btn-confirm").on("click", function () {
        self.location = "/purchase/listPurchase";
    });

    // $("button.btn-primary.list").on("click", function () {
    //     self.location = "/product/listProduct";
    // });
    $("button[name='confirmBtn']").on("click", function () {
        self.location = "/product/listProduct";
    });

    $("div.product-card").on("click", function () {
        var tranNo = $(this).data("tran-no");
        self.location = "/purchase/getPurchase?tranNo=" + tranNo;
    });

    $("button.btn-primary.confirm").on("click", function () {
        self.location = "/purchase/listPurchase";
    });

    $("input[name='order']").on("change", function () {
        let order = $(this).val();
        fncGetList(order);
    });
});

function fncGetList(order) {
    $("form").attr("method", "POST").attr("action", "/purchase/listPurchase").submit();
}

// var price = /*[${product.prodPrice}]*/ 1234567; // 가격 값 가져오기 (템플릿에서 렌더링된 값)
// var formattedPrice = price.toLocaleString(); // 천 단위 콤마 추가
// document.getElementById('prodPrice').innerText = formattedPrice + 'P';

function updateSearchKeyword() {
    // 카테고리 선택 값을 searchKeyword input에 자동으로 채워넣음
    var selectedCategory = document.getElementById('prodCateNo').value;
    if (selectedCategory !== "0") { // 선택된 값이 '0'이 아닐 때만
        // 쉼표 제거 및 값을 입력
        document.getElementById('searchKeyword').value = selectedCategory.replace(',', '');
    }
    $("form").attr("method", "POST").attr("action", "/purchase/listPurchase").submit();
}

function fncGetList(order) {
    $("form").attr("method" , "POST").attr("action" , "/purchase/listPurchase").submit();
}

$(function() {
    // 조회 버튼 클릭
    $("button.btn-primary.view").on("click", function () {
        var tranNo = $(this).data("tran-no");
        self.location = "/purchase/getPurchase?tranNo=" + tranNo;
    });
});