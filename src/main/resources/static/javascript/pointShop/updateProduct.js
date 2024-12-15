$(function() {
    $("button[name='cancelBtn']").on("click", function () {
        // history.go(-1);
        self.location = "/product/listProduct";
    });
});

$(function() {
    $("button[name='submitBtn']").on("click", function() {
        fncUpdateProduct();
    });

});

function fncUpdateProduct() {
    var prodName = $("input[name='prodName']").val();
    var prodDetail = $("textarea[name='prodDetail']").val();
    var prodImg1 = $("input[name='prodImg1']").val();
    var prodPrice = $("input[name='prodPrice']").val();
    var prodCateNo = $("select[name='prodCateNo']").val();

    if (prodName == null || prodName.length < 1) {
        alert("상품명은 반드시 입력하여야 합니다.");
        event.preventDefault();
        return;
    }

    if (prodName.length > 20) {
        alert("상품명은 20자까지 입력 가능합니다.");
        event.preventDefault();
        return;
    }

    if (prodDetail == null || prodDetail.length < 1) {
        alert("상품상세정보는 반드시 입력하여야 합니다.");
        event.preventDefault();
        return;
    }

    if (prodDetail.length > 100) {
        alert("상품 설명은 100자까지 입력 가능합니다.");
        event.preventDefault();
        return;
    }

    if (prodImg1 == null || prodImg1.length < 1) {
        alert("상품 대표 이미지는 반드시 입력하셔야 합니다.");
        event.preventDefault();
        return;
    }
    if (prodPrice == null || prodPrice.length < 1) {
        alert("가격은 반드시 입력하셔야 합니다.");
        event.preventDefault();
        return;
    }
    if (prodCateNo == null || prodCateNo.length < 1) {
        alert("상품 카테고리 번호는 반드시 선택하셔야 합니다.");
        event.preventDefault();
        return;
    }

    $("form").attr("method", "POST")
        .attr("action", "/product/updateProduct")
        .attr("enctype", "multipart/form-data")
        .submit();
}