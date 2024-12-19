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

        $("form").attr("action", "/product/listProduct").attr("method", "post").submit()

    }

    pageNavigator(paging);

    $("button.btn-confirm").on("click", function () {
        self.location = "/product/listProduct";
    });

    $("div.product-card").on("click", function () {
        var prodNo = $(this).data("prod-no");
        self.location = "/product/getProduct?prodNo=" + prodNo;
    });

    $("button.btn-primary.edit").on("click", function () {
        var prodNo = $(this).data("prod-no");
        self.location = "/product/updateProduct?prodNo=" + prodNo;
    });

    $("button.btn-primary.confirm").on("click", function () {
        self.location = "/product/listProduct";
    });

    $("input[name='order']").on("change", function () {
        let order = $(this).val();
        fncGetList(order);
    });

    $("button[name='confirmBtn']").on("click", function () {
        self.location = "/product/listProduct";
    });
});

function fncGetList(order) {
    $("form").attr("method", "POST").attr("action", "/product/listProduct").submit();
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
    $("form").attr("method", "POST").attr("action", "/product/listProduct").submit();
}









// $(function () {
//     $(window).scroll(function() {
//         // 스크롤 맨 아래일 때
//         if ($(window).scrollTop() + $(window).height() >= $(document).height() - 50) {
//
//             // 타임리프 변수를 사용하여 현재 페이지와 최대 페이지 값 가져오기
//             var currentPage = parseInt([[${currentPage}]]);
//             var maxPage = parseInt([[${maxPage}]]);
//
//             if (currentPage <= maxPage) {
//                 // 현재 페이지 번호 증가
//                 $("#page").val(currentPage + 1);
//
//                 // 검색 조건을 JSON 객체로 준비
//                 var search = {
//                     searchKeyword: $("#searchKeyword").val(),
//                     searchSorting: $("#searchSorting").val(),
//                     currentPage: $("#currentPage").val()
//                 };
//
//                 $.ajax({
//                     url: "/product/json/listProduct",
//                     method: "POST",
//                     dataType: "json",
//                     headers: {
//                         "Accept": "application/json",
//                         "Content-Type": "application/json"
//                     },
//                     async: false,
//                     data: JSON.stringify(search),
//                     success: function(JSONData, status) {
//                         console.log(JSONData); // 응답 내용 확인
//
//                         var list = JSONData['list'];
//
//                         // 응답 받은 데이터로 화면 갱신
//                         display(list);
//                     },
//                     complete: function() {
//                         // 완료 후 처리할 코드
//                     }
//                 });
//             }
//         }
//     });
// });
//