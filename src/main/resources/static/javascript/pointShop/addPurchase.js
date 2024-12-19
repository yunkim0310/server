$(function() {

    $("button[name='buyBtn']").on("click", function () {
        fncAddPurchase();
    });
});
$(function() {
    $("button[name='confirmBtn']").on("click", function () {
        // self.location = "/product/getProduct";
        history.go(-1);
    });
});
function fncAddPurchase() {
    var tranPoint = Number($("input[name='tranPoint']").val());
    var currPoint = Number($("input[name='currPoint']").val());
    var prodStatus = Boolean($("input[name='prodStatus']").val());

    // prodStatus = Boolean(prodStatus);

    if (tranPoint > currPoint) {
        alert("보유하신 포인트가 부족합니다.");
        return;
    }else if(!prodStatus){
        alert("판매 중지된 상품입니다.");
        return;
    }else{
        alert("구매가 완료되었습니다.");
        console.log(prodStatus);
    }
    $("form").attr("method", "POST")
        .attr("action", "/purchase/addPurchase")
        .attr("enctype", "multipart/form-data")
        .submit();
}