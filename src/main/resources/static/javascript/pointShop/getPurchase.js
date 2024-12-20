$(function() {

    $("button[name='buyBtn']").on("click", function () {
        var prodNo = $(this).data("prod-no");
        var prodStatus = $(this).data("prod-status");

        if(!prodStatus){
            alert("판매 중지된 상품입니다.");
            return;
        }
        self.location = "/purchase/addPurchase?prodNo=" + prodNo;
    });

    // 확인 버튼 클릭
    $("button[name='confirmBtn']").on("click", function () {
        // var username = $(this).data("user-name");
        self.location = "/purchase/listPurchase";
    });
});

function toggleContent() {
    var content = event.target.nextElementSibling; // 클릭된 버튼 바로 다음의 div.content를 찾음
    var button = event.target;
    var arrow = button.querySelector('.arrow');

    if (content.style.display === "none" || content.style.display === "") {
        content.style.display = "block"; // 내용 펼침
        button.innerText = "설명 숨기기 ^"; // 버튼 텍스트 변경
    } else {
        content.style.display = "none"; // 내용 접기
        button.innerText = "자세히 보기 v"; // 버튼 텍스트 변경
    }

    // 화살표 회전 효과
    button.closest('.expandable-section').classList.toggle('open');
}