$(function() {
    
    // mode 에 따라서 다르게 반응
    var mode = $("input[name='mode']:hidden").val();

    console.log(mode);

    // mode 가 결과창일때 (getStoreList)
    if (mode === "result") {

        // 초기 상태에서 선택된 1차, 2차, 3차 분류에 따라 표시 설정
        initCategoryDisplay();

        // 필터 텍스트 적용
        updateRegionText();
        updateFoodCategoryText();
        updatePriceText();
        updateHashtagText();
        updateAmenitiesText();

        // 1차 분류 변경 이벤트
        $("input[name='foodCategory1']").on("change", function () {

            var selectedCategory1 = $("input[name='foodCategory1']:checked").val();

            // 1차 분류가 선택되면 2차 분류 표시 및 3차 분류 초기화
            if (selectedCategory1) {

                $("div[id^='foodCategory2']").css("display", "block");
                $("div[id^='foodCategory3_']").css("display", "none");
                $("#foodCategory4").css("display", "none");

                // 2차, 3차 분류 체크 해제
                $("input[name='foodCategory2']").prop("checked", false);
                $("input[name='foodCategory3']").prop("checked", false);

            } else {

                // 1차 분류가 선택되지 않은 경우 하위 분류 숨기기
                $("div[id^='foodCategory2']").css("display", "none");
                $("div[id^='foodCategory3_']").css("display", "none");
                $("#foodCategory4").css("display", "none");

            }

            updateFoodCategoryText();
        });


        // 2차 분류 변경 이벤트
        $("input[name='foodCategory2']").on("change", function () {

            var selectedCategory2 = $("input[name='foodCategory2']:checked").val();

            // 2차 분류 선택에 따라 3차 분류 표시
            if (selectedCategory2) {

                $("div[id^='foodCategory3_']").css("display", "none");

                var detailCategoryDiv = $("#foodCategory3_" + selectedCategory2);

                if (detailCategoryDiv.length > 0) {

                    detailCategoryDiv.css("display", "block");

                }

                // 3차 분류 체크 해제 및 상세 분류 숨기기
                $("input[name='foodCategory3']").prop("checked", false);
                $("#foodCategory4").css("display", "none");

            } else {

                $("div[id^='foodCategory3_']").css("display", "none");
                $("#foodCategory4").css("display", "none");

            }

            updateFoodCategoryText();
        });


        // 3차 분류 이벤트 처리 함수
        $("input[name='foodCategory3']").on("change", function () {

            updateFoodCategoryText();
        });


        // 초기 상태 설정 함수
        function initCategoryDisplay() {

            var selectedCategory1 = $("input[name='foodCategory1']:checked").val();
            var selectedCategory2 = $("input[name='foodCategory2']:checked").val();
            var selectedCategory3 = $("input[name='foodCategory3']:checked").val();

            if (selectedCategory1) {

                $("div[id^='foodCategory2']").css("display", "block");

                if (selectedCategory2) {

                    $("div[id^='foodCategory3_']").css("display", "none");

                    var detailCategoryDiv = $("#foodCategory3_" + selectedCategory2);

                    if (detailCategoryDiv.length > 0) {

                        detailCategoryDiv.css("display", "block");

                        // 3차 분류에서 선택된 항목의 상태를 설정
                        if (selectedCategory3) {

                            detailCategoryDiv.find("input[name='foodCategory3'][value='" + selectedCategory3 + "']").prop("checked", true);

                        }
                    }
                }
            }

            var categoryText = "미선택";

            // 1차, 2차, 3차 선택이 있을 경우 텍스트 업데이트
            if (selectedCategory1 || selectedCategory2 || selectedCategory3) {

                var categoryParts = [];

                if (selectedCategory1) {
                    categoryParts.push(selectedCategory1);
                }
                if (selectedCategory2) {
                    categoryParts.push(selectedCategory2);
                }
                if (selectedCategory3) {
                    categoryParts.push(selectedCategory3);
                }

                categoryText = categoryParts.join("/"); // 선택된 카테고리를 /로 구분하여 표시
            }

            // 업데이트된 텍스트를 #foodCategory에 반영
            $("#foodCategory").text(categoryText);
        }

    }


    // mode 가 검색일때 (searchStore)
    if (mode === "search") {

        // 1차 분류 선택이 변경되면 2차, 3차 분류의 체크박스 해제 및 분류 표시
        $("input[name='foodCategory1']").on("change", function () {

            var selectedCategory1 = $("input[name='foodCategory1']:checked").val();

            $("#foodCategory4").css("display", "none");

            // 2차 분류 표시
            if (selectedCategory1) {

                // 2차 분류 보이기
                $("div[id^='foodCategory2']").css("display", "block");
                // 3차 분류 숨기기
                $("div[id^='foodCategory3_']").css("display", "none");

                // 2차 분류의 체크 해제
                $("input[name='foodCategory2']").prop("checked", false);

                // 3차 분류의 체크 해제
                $("input[name='foodCategory3']").prop("checked", false);

            } else {

                $("div[id^='foodCategory2']").css("display", "none");
                $("div[id^='foodCategory3_']").css("display", "none");

            }

            updateFoodCategoryText();
        });


        // 2차 분류 선택이 변경되면 3차 분류의 체크박스 해제 및 해당하는 3차 분류 표시
        $("input[name='foodCategory2']").on("change", function () {

            var selectedCategory2 = $("input[name='foodCategory2']:checked").val();

            updateDetailCategory(selectedCategory2);

            // 3차 분류의 체크 해제
            $("input[name='foodCategory3']").prop("checked", false);
            $("#foodCategory4").css("display", "none");

            updateFoodCategoryText();
        });


        // 3차 분류 이벤트 처리 함수
        $("input[name='foodCategory3']").on("change", function () {

            updateFoodCategoryText();
        });


        // 3차 분류 표시 함수
        function updateDetailCategory(selectedCategory2) {

            // 3차 분류 항목 모두 숨기기
            $("div[id^='foodCategory3_']").css("display", "none");
            $("#foodCategory4").css("display", "none");

            if (selectedCategory2) {

                // 선택된 2차 카테고리에 맞는 3차 카테고리만 보이도록 처리
                var detailCategoryDiv = $("#foodCategory3_" + selectedCategory2);

                if (detailCategoryDiv.length > 0) {
                    detailCategoryDiv.css("display", "block");
                }
            }
        }

    }


    // 음식 카테고리 텍스트 업데이트 함수
    function updateFoodCategoryText() {

        var selectedCategory1 = $("input[name='foodCategory1']:checked").val();
        var selectedCategory2 = $("input[name='foodCategory2']:checked").val();
        var selectedCategory3 = $("input[name='foodCategory3']:checked").val();

        var categoryText = "미선택";

        // 1차, 2차, 3차 선택이 있을 경우 텍스트 업데이트
        if (selectedCategory1 || selectedCategory2 || selectedCategory3) {

            var categoryParts = [];

            if (selectedCategory1) {
                categoryParts.push(selectedCategory1);
            }
            if (selectedCategory2) {
                categoryParts.push(selectedCategory2);
            }
            if (selectedCategory3) {
                categoryParts.push(selectedCategory3);
            }

            categoryText = categoryParts.join("/"); // 선택된 카테고리를 /로 구분하여 표시
        }

        // 업데이트된 텍스트를 #foodCategory에 반영
        $("#foodCategory").text(categoryText);
    }


    var hashtagCnt = Number($("input[name='hashtagCnt']:hidden").val());
    hashtagCnt = (hashtagCnt > 1)? hashtagCnt : 1;

    // 해시태그 입력창 추가 함수
    $('button[name="addHashtagInput"]').on('click', function () {

        // 컨테이너 생성
        const container = $(
            '<div class="css-1pjgd36 e744wfw6 addedHashtagInput">' +
                // '<div class="css-1y8737n e744wfw5">' +
                //     '<label class="css-1obgjqh e744wfw4">&nbsp;</label>' +
                // '</div>' +
                '<div class="css-82a6rk e744wfw3">' +
                    '<div class="css-jmalg e1uzxhvi6">' +
                        '<div class="css-176lya2 e1uzxhvi3 under-name-block">' +
                            '<input type="text" class="css-1bkd15f e1uzxhvi2" ' +
                            'placeholder="#을 제외한 해시태그를 입력해주세요." name="hashtagList">' +
                        '</div>' +
                    '</div>' +
                '</div>' +
                '<div class="css-1w0ksfz e744wfw2">' +
                    '<button class="css-ufulao e4nu7ef3 checkBtn" name="removeHashtagInput" type="button">' +
                        '<span class="css-ymwvow e4nu7ef1 checked_nick_btn">삭제</span>' +
                    '</button>' +
                '</div>' +
                '<br/>' +
            '</div>');

        // hashtagInputs에 컨테이너 추가
        $('#hashtagInputs').append(container);

        hashtagCnt++;

        if (hashtagCnt < 5) {
            $('button[name="addHashtagInput"]').prop("disabled", false);
        } else {
            $('button[name="addHashtagInput"]').prop("disabled", true);
        }

    });


    // 해시태그 입력창 제거 함수
    $(document).on('click', "button[name='removeHashtagInput']", function() {

        $(this).closest('.addedHashtagInput').remove();  // 해당 div 삭제
        hashtagCnt--;

        if (hashtagCnt < 5) {
            $('button[name="addHashtagInput"]').prop("disabled", false);
        } else {
            $('button[name="addHashtagInput"]').prop("disabled", true);
        }

        updateHashtagText();
    });


    function updateRegionText() {

        // 선택된 지역 목록을 담을 배열
        var selectedRegions = [];

        // 선택된 체크박스 값들을 배열에 추가
        $('input[name="regionList"]:checked').each(function() {
            selectedRegions.push($(this).next('label').text()); // 체크된 항목의 지역 이름을 배열에 추가
        });

        // 선택된 지역이 6개 이상일 경우 "외 몇개" 형식으로 표시
        if (selectedRegions.length > 5) {

            var displayedRegions = selectedRegions.slice(0, 5);
            var remainingCount = selectedRegions.length - 5;

            $('#region').text(displayedRegions.join(', ') + ` 외 ${remainingCount}개`);

        } else if (selectedRegions.length > 0) {
            $('#region').text(selectedRegions.join(', '));
        } else {
            $('#region').text('미선택');
        }
    }


    // 지역 선택에 대한 이벤트 처리
    $('input[name="regionList"]').on('change', function() {

        updateRegionText();
    });


    function updatePriceText() {

        var priceMin = Number($("input[name='priceMin']").val());
        var priceMax = Number($("input[name='priceMax']").val());

        $("input[name='priceMin']").attr("max", priceMax);
        $("input[name='priceMax']").attr("min", priceMin);

        $("#price").text(priceMin.toLocaleString() + ' ~ ' + priceMax.toLocaleString());
    }


    // 가격 범위에 대한 이벤트 처리
    $("input[name^='price']").on('input', function() {

        updatePriceText();
    });


    // 입력된 해시태그들의 리스트 업데이트 함수
    function updateHashtagText() {

        var hashtagList = [];

        // 모든 입력 필드의 값을 가져와 리스트에 추가
        $("input[name='hashtagList']").each(function () {

            var hashtag = $(this).val();

            // 공백 및 # 제거
            hashtag = hashtag.replace("#", "").replace(/\s+/g, "");
            $(this).val(hashtag);

            // 유효한 값만 리스트에 추가
            if (hashtag !== "") {
                hashtagList.push("#" + hashtag);
            }
        });

        // 해시태그 리스트를 표시
        if (hashtagList.length > 0) {
            $("#hashtag").text(hashtagList.join(", "));
        } else {
            $("#hashtag").text("미선택");
        }

    }


    // 해시태그 입력에 대한 이벤트 처리
    $(document).on('input', "input[name='hashtagList']",function() {

        updateHashtagText();
    });


    function updateAmenitiesText() {

        // 선택된 지역 목록을 담을 배열
        var selectedAmenities = [];

        // 선택된 체크박스 값들을 배열에 추가
        $('input[name="amenitiesNoList"]:checked').each(function() {
            selectedAmenities.push($(this).next('label').text()); // 체크된 항목의 지역 이름을 배열에 추가
        });

        // 선택된 편의시설이 6개 이상일 경우 "외 몇개" 형식으로 표시
        if (selectedAmenities.length > 5) {

            var displayedAmenities = selectedAmenities.slice(0, 5);
            var remainingCount = selectedAmenities.length - 5;

            $('#amenities').text(displayedAmenities.join(', ') + ` 외 ${remainingCount}개`);

        } else if (selectedAmenities.length > 0) {
            $('#amenities').text(selectedAmenities.join(', '));
        } else {
            $('#amenities').text('미선택');
        }
    }


    // 편의시설 선택에 대한 이벤트 처리
    $('input[name="amenitiesNoList"]').on('change', function() {

        updateAmenitiesText();
    });


    // 초기화 버튼 이벤트 처리
    $("button[name='resetFilter']").on('click', function () {

        resetFilter();
    });


    // 필터 버튼 클릭 시 모달 표시
    $("button[name='searchStoreFilter']").on("click", function () {

        $("#filterModal").show();
    });


    // 모달 닫기 버튼
    $("button[name='closeFilter']").on("click", function () {

        if (mode === "search") {
            resetFilter();
        }

        $("#filterModal").hide();  // 모달을 닫음
    });


    // 지역 필터 보이는 함수
    $("button[name='regionFilter']").on('click', function () {

        var switchs = $(this).data("switchs");

        if (switchs === "off") {

            $("#regionFilter").css("display", "block");
            $(this).data("switchs", "on");
        }

        else {

            $("#regionFilter").css("display", "none");
            $(this).data("switchs", "off");
        }

    });


    // 음식 카테고리 필터 보이는 함수
    $("button[name='foodCategoryFilter']").on('click', function () {

        var switchs = $(this).data("switchs");

        if (switchs === "off") {

            $("#foodCategoryFilter").css("display", "block");
            $(this).data("switchs", "on");
        }

        else {

            $("#foodCategoryFilter").css("display", "none");
            $(this).data("switchs", "off");
        }

    });


    // 가격 필터 보이는 함수
    $("button[name='priceFilter']").on('click', function () {

        var switchs = $(this).data("switchs");

        if (switchs === "off") {

            $("#priceFilter").css("display", "block");
            $(this).data("switchs", "on");
        }

        else {

            $("#priceFilter").css("display", "none");
            $(this).data("switchs", "off");
        }

    });


    // 해시태그 필터 보이는 함수
    $("button[name='hashtagFilter']").on('click', function () {

        var switchs = $(this).data("switchs");

        if (switchs === "off") {

            $("#hashtagFilter").css("display", "block");
            $(this).data("switchs", "on");
        }

        else {

            $("#hashtagFilter").css("display", "none");
            $(this).data("switchs", "off");
        }

    });


    // 편의시설 필터 보이는 함수
    $("button[name='amenitiesFilter']").on('click', function () {

        var switchs = $(this).data("switchs");

        if (switchs === "off") {

            $("#amenitiesFilter").css("display", "block");
            $(this).data("switchs", "on");
        }

        else {

            $("#amenitiesFilter").css("display", "none");
            $(this).data("switchs", "off");
        }

    });

});

// 필터 리셋 함수
function resetFilter() {

    // 모든 input, select, textarea 리셋
    $("form[name='searchStore'] input[type='text'], form[name='searchStore'] select, form[name='searchStore'] textarea").val('');

    // 가격 필터 값 리셋
    $("form[name='searchStore'] input[name='priceMin']").val(0);
    $("form[name='searchStore'] input[name='priceMax']").val(100000);

    // 채크박스, 라디오버튼 체크 리셋
    $("form[name='searchStore'] input[type='checkbox'], form[name='searchStore'] input[type='radio']").prop('checked', false);

    // 지역 필터 안 보이기
    $('#region').text('미선택');
    $("#regionFilter").css("display", "none");
    $("button[name='regionFilter']").data("switchs", "off");

    // 음식 카테고리 필터 안 보이기
    $('#foodCategory').text('미선택');
    $("#foodCategoryFilter").css("display", "none");
    $("button[name='foodCategoryFilter']").data("switchs", "off");

    // 음식 카테고리 2차 분류, 3차 분류 안 보이기
    $("div[id^='foodCategory2']").css("display", "none");
    $("div[id^='foodCategory3_']").css("display", "none");

    // 가격 필터 안 보이기
    $('#price').text('0 ~ 100,000');
    $("#priceFilter").css("display", "none");
    $("button[name='priceFilter']").data("switchs", "off");

    // 해시태그 필터 안 보이기
    $('#hashtag').text('미선택');
    $("#hashtagFilter").css("display", "none");
    $("button[name='hashtagFilter']").data("switchs", "off");

    // 해시태그 입력창 제거 후 추가버튼 리셋
    $(".addedHashtagInput").remove();
    hashtagCnt = 1;
    $('button[name="addHashtagInput"]').prop("disabled", false);

    // 편의시설 필터 안 보이기
    $('#amenities').text('미선택');
    $("#amenitiesFilter").css("display", "none");
    $("button[name='amenitiesFilter']").data("switchs", "off");

}