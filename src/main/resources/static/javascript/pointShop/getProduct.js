$(function() {
    // 구매 버튼 클릭
    $("button[name='buyBtn']").on("click", function () {
        var prodNo = $(this).data("prod-no");
        self.location = "/purchase/addPurchase?prodNo=" + prodNo;
    });

    $("button[name='editBtn']").on("click", function () {
        var prodNo = $(this).data("prod-no");
        self.location = "/product/updateProduct?prodNo=" + prodNo;
    });

    $("div.__desc").on("click", function () {
        var prodNo = $(this).data("prod-no");
        self.location = "/product/getProduct?prodNo=" + prodNo;
    });

    // 확인 버튼 클릭
    $("button[name='confirmBtn']").on("click", function () {
        self.location = "/product/listProduct";
    });

    // 장바구니 아이콘 클릭
    $("a#cart").on("click", function () {
        var prodNo = $(this).data("prod-no");
        var username = $(this).data("user-name");
        var prodStatus = $(this).data("prod-status");
        console.log(prodStatus);

        if(prodStatus === false){
            alert("판매 중지된 상품입니다.");
            return;
        }

        $.ajax({
            type: "POST",
            url: "/api-purchase/addCart",
            contentType: "application/json",
            data: JSON.stringify({ username : username, prodNo: prodNo, cntProd: 1 }),
            success: function(response) {

                alert(response);
                // if(response === "이미 추가된 상품입니다."){
                //
                //     alert(response);
                //
                // }else if(response === "장바구니가 꽉찼습니다."){
                //
                //     alert(response);
                //
                // }else{
                //
                //     alert(response);
                //
                // }
            },
            error: function(xhr, status, error) {
                alert("장바구니 추가 실패: " + error);
            }
        });
    });

    // 찜하기 아이콘 클릭
    $("a#like").on("click", function() {
        var prodNo = $(this).data("prod-no");
        var username = $(this).data("user-name");
        const $this = $(this);

        var wishCartData = {
            prodNo: prodNo,
            username: username
        };

        // AJAX 요청을 보내고 색상 변경
        $.ajax({
            url: "/api-purchase/addWish",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(wishCartData),
            success: function(response) {

                if(response === "찜 목록이 꽉찼습니다."){
                    alert(response);  // 서버에서 받은 응답을 알림으로 표시
                    $this.attr("class", "btn-like-inactive");
                    // location.reload();
                }else{

                    if ($this.attr("class") === "btn-like-inactive") {
                        $this.attr("class", "btn-like-active");
                    } else {
                        $this.attr("class", "btn-like-inactive");
                    }
                }
            },
            error: function(error) {
                alert("오류가 발생했습니다.");
            }
        });

        // 하트 아이콘의 클래스를 토글하여 빨간색 <-> 빈 하트로 변경
        // $(this).toggleClass('fas far');

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