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
    var prodPrice = Number($("input[name='prodPrice']").val());
    var prodCateNo = $("select[name='prodCateNo']").val();

    if (prodName == null || prodName.length < 1) {
        alert("상품명은 반드시 입력하여야 합니다.");
        event.preventDefault();
        return;
    }else if (prodName.length > 20) {
        alert("상품명은 20자까지 입력 가능합니다.");
        event.preventDefault();
        return;
    }else if (prodDetail == null || prodDetail.length < 1) {
        alert("상품상세정보는 반드시 입력하여야 합니다.");
        event.preventDefault();
        return;
    }else if (prodDetail.length > 200) {
        alert("상품 설명은 200자까지 입력 가능합니다.");
        event.preventDefault();
        return;
    }else if (prodImg1 == null || prodImg1.length < 1) {
        alert("상품 대표 이미지는 반드시 입력하셔야 합니다.");
        event.preventDefault();
        return;
    }else if (prodPrice == null || prodPrice.length < 1) {
        alert("가격은 반드시 입력하셔야 합니다.");
        event.preventDefault();
        return;
    }else if(isNaN(prodPrice)) {
        alert("가격은 숫자만 입력하셔야 합니다.");
        event.preventDefault();
        return;
    }else if (prodCateNo == null || prodCateNo.length < 1) {
        alert("상품 카테고리 번호는 반드시 선택하셔야 합니다.");
        event.preventDefault();
        return;
    }else{
        alert("수정이 완료되었습니다.")
    }

    $("form").attr("method", "POST")
        .attr("action", "/product/updateProduct")
        .attr("enctype", "multipart/form-data")
        .submit();
}

$(function (){

    $("input[class^='prodImg']:file").on("change", function() {

        const $this = $(this);
        var className = $this.attr('class');

        console.log(className)

        const file = $this[0].files[0];

        var $prodImg = $(`input[name='${className}']:hidden`);

        console.log($prodImg.val() === "");

        if ($prodImg.val() == null || $prodImg.val() === "") {

            console.log("uploadFile");
            console.log($prodImg.val());

            uploadFile(file, "point/product/").then(result => {

                console.log(result);
                $prodImg.val(result.filePath);
                $(`img.${className}`).attr("src", result.url)
                $(`#${className}`).css("display", "flex");
            });

        } else {

            console.log("updateFile");
            console.log($prodImg.val());

            updateFile($prodImg.val(), file, "point/product/").then(result => {

                console.log(result);
                $prodImg.val(result.filePath);
                $(`img.${className}`).attr("src", result.url)
            });
        }

    })

});