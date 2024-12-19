$(function() {
    // 구매 버튼 클릭
    $("button[name='buyBtn']").on("click", function () {
        fncAddPurchase();
    });

    // 확인 버튼 클릭
    $("button[name='confirmBtn']").on("click", function () {
        self.location = "/product/listProduct";
    });

    $("div.product-card").on("click", function () {
        var prodNo = $(this).data("prod-no");
        self.location = "/product/getProduct?prodNo=" + prodNo;
    });
});

function fncAddPurchase() {
    var tranPoint = Number($("input[name='tranPoint']").val());
    var currPoint = Number($("input[name='currPoint']").val());

    if (tranPoint > currPoint) {
        alert("보유하신 포인트가 부족합니다.");
        return;
    }else{
        alert("일괄 구매가 완료되었습니다.")
    }

    $("form").attr("method", "POST")
        .attr("action", "/purchase/addPurchaseCart")
        .attr("enctype", "multipart/form-data")
        .submit();
}

$(function() {
    // 조회 버튼 클릭
    $("button.btn-primary.view").on("click", function () {
        var prodNo = $(this).data("prod-no");
        self.location = "/product/getProduct?prodNo=" + prodNo;
    });

    $("button.btn-primary.list").on("click", function () {
        self.location = "/product/listProduct";
    });

    $("button.btn-remove-cart").on("click", function () {
        var userName = $(this).data("user-name");
        var prodNo = $(this).data("prod-no");
        $.ajax({
            type: "DELETE",
            url: "/api-purchase/removeCart",
            data: { userName: userName, prodNo: prodNo },
            success: function(response) {
                alert("장바구니에서 상품이 삭제되었습니다.");
                location.reload();
            },
            error: function(xhr, status, error) {
                alert("장바구니 삭제 실패함: " + error);
            }
        });
    });


    $("button.btn-clear-cart").on("click", function () {
        var userName = $(this).data("user-name");
        $.ajax({
            type: "DELETE",
            url: "/api-purchase/clearCart",
            data: { userName: userName },
            success: function(response) {
                alert("장바구니에서 전체 상품이 삭제되었습니다.");
                location.reload();
            },
            error: function(xhr, status, error) {
                alert("장바구니 삭제 실패함: " + error);
            }
        });
    });

});