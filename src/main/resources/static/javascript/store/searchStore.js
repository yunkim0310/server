$(function () {

    // 검색 함수
    function search(page) {

        var searchKeyword = $.trim($("input[name='searchKeyword']:text").val());

        var category1 = $("input[name='foodCategory1']:checked").val();
        var category2 = $("input[name='foodCategory2']:checked").val();
        var category3 = $("input[name='foodCategory3']:checked").val();

        $("input[name='searchKeyword']:hidden").val(searchKeyword);

        if (category1 !== undefined) {

            if (category2 === undefined) {
                category2 = "전체";
            }

            if (category3 === undefined) {
                category3 = "전체";
            }

            $("input[name='foodCategoryId']").val(category1+"/"+(category2.replace("-", ", "))+"/"+category3+"/");
        }

        // 해시태그 변경 (공백, # 제거)
        $("input[name='hashtagList']").each(function () {

            var hashtag = $(this).val();

            // 공백 및 # 제거
            hashtag = hashtag.replace("#", "").replace(/\s+/g, "");
            $(this).val(hashtag);
        });

        $("input[name='mode']:hidden").val("result");
        $("input[name='page']").val(page);

        $("form[name='searchStore']").attr("action", "/getStoreList").attr("method", "get").submit();
    }

    // 검색 버튼
    $("button[name='searchStore']").on("click", function () {

        search(1);
    });

    // 엔터 검색
    $(document).on('keydown', function (e) {

        // Enter 키 코드: 13
        if (e.keyCode === 13) {

            e.preventDefault();
            search(1);
        }
    });

    var mode = $("input[name='mode']:hidden").val();

    console.log(mode);

    // 필터 적용 후 제출 (모달 내의 폼 제출)
    $("button[name='saveFilter']").on("click", function (e) {

        e.preventDefault(); // 기본 제출 방지

        if (mode === "search") {

            // 필터 적용 후 모달 닫기
            $("#filterModal").hide();
        } else {
            search(1);
        }

    });

    // 페이징
    pageNavigator(search);

    $(".popularKeyword").on("click", function () {

        resetFilter();
        $("input[name='searchKeyword']:text").val($(this).text());
        search(1);
    })

});