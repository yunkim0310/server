$(function() {
    // 조회 버튼 클릭
    $("button.btn-primary.view").on("click", function () {
        var prodNo = $(this).data("prod-no");
        self.location = "/product/getProduct?prodNo=" + prodNo;
    });

    $("button[name='confirmBtn']").on("click", function () {
        self.location = "/product/listProduct";
    });

    $("div.product-card").on("click", function () {
        var prodNo = $(this).data("prod-no");
        self.location = "/product/getProduct?prodNo=" + prodNo;
    });

    $("button.btn-remove-wish").on("click", function () {
        var username = $(this).data("user-name");
        var prodNo = $(this).data("prod-no");
        $.ajax({
            type: "DELETE",
            url: "/api-purchase/removeWish",
            data: { username: username, prodNo: prodNo },
            success: function(response) {
                alert("찜 목록에서 상품이 삭제되었습니다.");
                location.reload();
            },
            error: function(xhr, status, error) {
                alert("찜 목록 삭제 실패함: " + error);
            }
        });
    });

    $("button.btn-clear-wish").on("click", function () {
        var userName = $(this).data("user-name");
        $.ajax({
            type: "DELETE",
            url: "/api-purchase/clearWish",
            data: { userName: userName },
            success: function(response) {
                alert("찜 목록에서 전체 상품이 삭제되었습니다.");
                location.reload();
            },
            error: function(xhr, status, error) {
                alert("찜 목록 삭제 실패함: " + error);
            }
        });
    });

});

// function removeWish(){
//
//     const query = 'input[name="chk"]:checked'
//     const selectedElements = document.querySelectorAll(query)
//
//     const selectedElementsCnt = selectedElements.length;
//
//     if(selectedElementsCnt == 0){
//         alert("삭제할 상품을 선택해주세요.")
//         return false;
//     }else{
//         const arr = new Array(selectedElementsCnt);
//
//         document.querySelectorAll('input[name="chk"]:checked').forEach(function(v,i){
//             arr[i] = v.value;
//         });
//
//         const form = document.createElement('form');
//         form.setAttribute('method', 'DELETE');
//         form.setAttribute('action', '/purchase/removeWish');
//
//         var input1 = document.createElement('input');
//         input1.setAttribute("type", "hidden");
//         input1.setAttribute("name", "wishCartNo");
//         input1.setAttribute("value", arr);
//         form.appendChild(input1);
//         console.log(form);
//         document.body.appendChild(form);
//         form.submit();
//
//     }
//
// }

function removeWish() {
    // 체크된 상품들
    const selectedElements = document.querySelectorAll('input[name="chk"]:checked');
    const selectedElementsCnt = selectedElements.length;

    // 체크된 항목이 없으면 삭제 불가 메시지 표시
    if (selectedElementsCnt === 0) {
        alert("삭제할 상품을 선택해주세요.");
        return false;
    } else {
        const arr = [];
        selectedElements.forEach(function(v, i) {
            arr[i] = v.value;  // 체크된 항목의 wishCartNo 값을 배열에 저장
        });

        // AJAX 요청을 보내서 서버에서 해당 상품들을 삭제하도록 함
        $.ajax({
            url: '/purchase/removeWish',  // 서버의 삭제 처리 URL
            type: 'DELETE',  // HTTP DELETE 요청
            data: {
                wishCartNo: arr  // 삭제할 상품들의 wishCartNo를 배열로 전달
            },
            success: function(response) {
                alert("찜 목록에서 선택된 상품이 삭제되었습니다.");
                location.reload();  // 삭제 후 페이지 새로 고침
            },
            error: function(xhr, status, error) {
                alert("찜 목록 삭제 실패: " + error);  // 오류 발생 시 메시지 표시
            }
        });
    }
}