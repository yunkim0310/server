import { getLocation, getApiKey } from "./geoLocation.mjs";

$(function() {

    // action 맵핑
    $("form[name='updateStore']").attr("action", "/store/updateStore").attr("method", "post").attr("enctype", "multipart/form-data");

    // API Key
    let googleApiKey = ""

    getApiKey().then(apiKey => {
        googleApiKey = apiKey.google;
    });

    // 편의시설 중복방지 함수 (콜키지, 키즈존 관련)
    function chkAmenitiesValidation() {

        const $corkageFree = $("#amenitiesNo3");
        const $corkageAble = $("#amenitiesNo4");

        const $kidsZone = $("#amenitiesNo5");
        const $noKidsZone = $("#amenitiesNo6");

        // 코르키지 관련 체크 상태 처리
        $corkageAble.prop("disabled", $corkageFree.is(":checked"));
        $corkageFree.prop("disabled", $corkageAble.is(":checked"));

        // 키즈존 관련 체크 상태 처리
        $noKidsZone.prop("disabled", $kidsZone.is(":checked"));
        $kidsZone.prop("disabled", $noKidsZone.is(":checked"));
    }

    chkAmenitiesValidation();

    
    // submit 함수
    $("button[name='submitBtn']").on("click", function (e) {

        if (chkValidation()) {

            // 음식 카테고리 합치기
            var category1 = $("input[name='foodCategory1']:checked").val();
            var category2 = $("input[name='foodCategory2']:checked").val();
            var category3 = $("input[name='foodCategory3']:checked").val();
            var category4 = $("input[name='foodCategory4']").val();

            $("input[name='foodCategoryId']").val(category1 + "/" + (category2.replace("-", ", ")) + "/" + category3 + "/" + category4);

            // 매장 주소 합치기
            $("input[name='storeAddr']").val($("input[name='storeAddr1']").val() + ', ' + $.trim($("input[name='storeAddr2']").val()));

            // 매장 전화번호 합치기
            var phone1 = $("input[name='storePhone1']").val().replace(" ", "");
            if (phone1 != null && phone1 !== "") {
                var phone2 = $("input[name='storePhone2']").val().replace(" ", "");
                var phone3 = $("input[name='storePhone3']").val().replace(" ", "");

                $("input[name='storePhone']:hidden").val(phone1 + '-' + phone2 + '-' + phone3);
            }

            // 해시태그 변경 (공백, # 제거)
            $("input[name='hashtagList']").each(function () {

                let hashtag = $(this).val();

                hashtag = hashtag.replace("#", "").replace(" ", "");

                $(this).val(hashtag);

            });

            $("form[name='updateStore']").attr("action", "/store/updateStore").attr("method", "post").attr("enctype", "multipart/form-data").submit();

        } else {
            e.preventDefault();
        }

    });


    // 초기 상태에서 선택된 1차, 2차, 3차 분류에 따라 표시 설정
    initCategoryDisplay();


    // 1차 분류 변경 이벤트
    $("input[name='foodCategory1']").on("change", function() {
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
    });


    // 2차 분류 변경 이벤트
    $("input[name='foodCategory2']").on("change", function() {
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
    });


    // 3차 분류 변경 이벤트
    $("input[name='foodCategory3']").on("change", function() {
        var selectedCategory3 = $("input[name='foodCategory3']:checked").val();

        $("#foodCategory4").find("input, select, textarea").val("")

        // 3차 분류 선택에 따라 상세 분류 표시
        if (selectedCategory3 && selectedCategory3 !== "전체") {
            $("#foodCategory4").css("display", "block").prop("disabled", false)
        } else {
            $("#foodCategory4").css("display", "none").prop("disabled", true);
        }
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

                if (selectedCategory3 && selectedCategory3 !== "전체") {
                    $("#foodCategory4").css("display", "block");
                }
            }
        }
    }


    // 다음 주소 검색
    $("#daumPostCode, input[name='storeAddr1']").on("click", function () {

        new daum.Postcode({
            oncomplete: function(data) {
                var addr = data.address; // 최종 주소 변수

                // 주소 정보를 해당 필드에 넣는다.
                $("input[name='storeAddr1']").val(addr);
                $("input[name='storeAddr2']").focus();
                
                // Geocode 생성
                getLocation(googleApiKey, addr).then(location => {

                    $("input[name='storeLocation']").val(location);

                });

            }
        }).open();

    });



    var hashtagCnt = Number($("input[name='hashtagCnt']:hidden").val());
    hashtagCnt = (hashtagCnt > 1)? hashtagCnt : 1;

    // 해시태그 입력창 추가 함수
    $('button[name="addHashtagInput"]').on('click', function () {

        // 컨테이너 생성
        const container = $(
            '<div class="css-1pjgd36 e744wfw6 addedHashtagInput">' +
                '<div class="css-1y8737n e744wfw5">' +
                    '<label class="css-1obgjqh e744wfw4">&nbsp;</label>' +
                '</div>' +
                '<div class="css-82a6rk e744wfw3">' +
                    '<div class="css-jmalg e1uzxhvi6">' +
                        '<div class="css-176lya2 e1uzxhvi3 under-name-block">' +
                            '<input type="text" class="css-1bkd15f e1uzxhvi2" ' +
                            'placeholder="#을 제외한 해시태그를 입력해주세요." name="hashtagList" maxlength="10">' +
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

    // 해시태그 입력창 삭제 함수
    $(document).on('click', "button[name='removeHashtagInput']", function() {
        $(this).closest('.addedHashtagInput').remove();  // 해당 div 삭제
        hashtagCnt--;

        if (hashtagCnt < 5) {
            $('button[name="addHashtagInput"]').prop("disabled", false);
        } else {
            $('button[name="addHashtagInput"]').prop("disabled", true);
        }
    });


    // 메뉴 입력창 생성함수
    var menuCnt = Number($("input[name='menuCnt']:hidden").val());

    $('button[name="addMenuInput"]').on('click', function () {

        const container = $(
            '<tbody class="addedMenuInput">' +
            '<tr>' +
            '<td>&nbsp;</td>' +
            '</tr>' +
            '<tr>' +
            '<td>&nbsp;</td>' +
            '</tr>' +
            '<tr>' +
            '<td rowspan="7">' +
            `<input type="radio" name="specialMenuNo" value="${menuCnt + 1}">` +
            `<input type="hidden" name="menuList[${menuCnt}].menuNo" value="${menuCnt+1}">` +
            '</td>' +
            '<td>' +
            '<div class="css-1pjgd36 e744wfw6">' +
            '<div class="css-82a6rk e744wfw3" style="display: inline-flex">' +
            `<input type="file" accept="image/*" class="menuImg${menuCnt+1}">` +
            `<input type="hidden" class="menuImg${menuCnt+1}" name="menuList[${menuCnt}].menuImg"` +
            '</div>' +
            '<div style="display: flex; justify-content: flex-end;padding-left: 34%;">' +
            '<div class="css-1w0ksfz e744wfw2">' +
            '<button class="css-ufulao e4nu7ef3 checkBtn" name="removeMenuInput" type="button">' +
            '<span class="css-ymwvow e4nu7ef1 checked_nick_btn">삭제</span>' +
            '</button>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</td>' +
            '</tr>' +
            '<tr>' +
            '<td>' +
            `<div id="menuImg${menuCnt+1}" style="display: none;padding-bottom: 10px;">` +
            `<img class="menuImg${menuCnt+1}" alt="메뉴 사진" width="100" height="100">` +
            '</div>' +
            '</td>' +
            '</tr>' +
            '<tr>' +
            '<td>' +
            '<div class="css-82a6rk e744wfw3">' +
            '<label class="css-1obgjqh e744wfw4">' +
            '메뉴 이름' +
            '<span class="css-qq9ke6 e744wfw0">*</span>' +
            '</label>' +
            '</div>' +
            '<div class="css-82a6rk e744wfw3">' +
            '<div class="css-jmalg e1uzxhvi6">' +
            '<div class="css-176lya2 e1uzxhvi3 under-name-block">' +
            `<input type="text" class="css-1bkd15f e1uzxhvi2" placeholder="메뉴 이름을 입력해주세요." name="menuList[${menuCnt}].menuName" min="0" required>` +
            '</div>' +
            '</div>' +
            '</div>' +
            '</td>' +
            '</tr>' +
            '<tr>' +
            '<td>&nbsp;</td>' +
            '</tr>' +
            '<tr>' +
            '<td>' +
            '<div class="css-82a6rk e744wfw3">' +
            '<label class="css-1obgjqh e744wfw4">' +
            '메뉴 가격' +
            '<span class="css-qq9ke6 e744wfw0">*</span>' +
            '</label>' +
            '</div>' +
            '<div class="css-82a6rk e744wfw3">' +
            '<div class="css-jmalg e1uzxhvi6">' +
            '<div class="css-176lya2 e1uzxhvi3 under-name-block">' +
            `<input type="number" class="css-1bkd15f e1uzxhvi2" placeholder="메뉴 가격을 입력해주세요." name="menuList[${menuCnt}].menuPrice" min="0" required>` +
            '</div>' +
            '</div>' +
            '</div>' +
            '</td>' +
            '</tr>' +
            '<tr>' +
            '<td>&nbsp;</td>' +
            '</tr>' +
            '<tr>' +
            '<td>' +
            '<div class="css-82a6rk e744wfw3">' +
            '<label class="css-1obgjqh e744wfw4">' +
            '메뉴 설명' +
            '</label>' +
            '</div>' +
            '<div class="css-82a6rk e744wfw3">' +
            '<div class="css-jmalg e1uzxhvi6">' +
            '<div class="css-176lya2 e1uzxhvi3 under-name-block">' +
            `<input type="text" class="css-1bkd15f e1uzxhvi2" placeholder="메뉴 설명을 입력해주세요." name="menuList[${menuCnt}].menuInfo" maxlength="100">` +
            '</div>' +
            '</div>' +
            '</div>' +
            '</td>' +
            '</tr>' +
            '</tbody>'
        );

        // hashtagInputs 에 컨테이너 추가
        $('#menuInputs').append(container);

        menuCnt++;

        if (menuCnt < 20) {
            $('button[name="addMenuInput"]').prop("disabled", false);
        } else {
            $('button[name="addMenuInput"]').prop("disabled", true);
        }

    });


    // TODO 기존 선택한 대표메뉴 삭제시 #defaultMenu로 체크되게 변경
    // 메뉴 입력창 삭제 함수
    $(document).on('click', "button[name='removeMenuInput']", function() {

        $(this).closest('.addedMenuInput').remove();  // 해당 div 삭제
        menuCnt--;

        if (menuCnt < 20) {
            $('button[name="addMenuInput"]').prop("disabled", false);
        } else {
            $('button[name="addMenuInput"]').prop("disabled", true);
        }
    });

    
    // 파일 업로드 모듈
    fileUploadEvent("storeImg", "store/store/");
    fileUploadEvent("menuImg", "store/menu/");


    // 편의시설 중복 방지 함수 (콜키지 프리, 콜키지 가능/키즈존, 노키즈존)
    $("input[name='amenitiesNoList']").on("change", function () {

        chkAmenitiesValidation();

    });

    // 해시태그 입력 필터링 함수
    function hashtagInputFilter(input) {

        // 한글, 영어, 숫자만 허용하는 정규식
        const pattern = /^[가-힣a-zA-Z0-9]*$/;

        return input.split('').filter(char => pattern.test(char)).join('');
    }

    // 해시태그 입력 이벤트처리
    $(document).on("input", "input[name='hashtagList']", function () {

        var input = $(this).val();

        // 필터링된 값으로 업데이트
        var filteredValue = hashtagInputFilter(input);

        if (input !== filteredValue) {
            $(this).val(filteredValue);
            alert("한글, 영어, 숫자만 입력 가능합니다. 공백은 허용되지 않습니다.");
        }
    });

});

// 유효성 검사
function chkValidation () {

    // 매장 사진
    const storeImg1 = $("input[name='storeImg1']:hidden").val();
    const storeImg2 = $("input[name='storeImg2']:hidden").val();
    const storeImg3 = $("input[name='storeImg3']:hidden").val();

    let result = true;

    if (storeImg1 == null || storeImg2 == null || storeImg3 == null) {
        alert("매장 사진이 부족합니다.");
        return false;
    }

    // 매장명
    var storeName = $.trim($("input[name='storeName']").val());

    if (storeName === "") {
        alert("매장명은 공백만 입력할 수 없습니다.");
        return false;
    }

    // 주소
    if ($("input[name='storeAddr1']").val() == null) {
        alert("주소는 필수 입력사항입니다.")
        return false;
    }

    // 매장 소개
    var storeInfo = $.trim($("#storeInfo").val());

    // 공백만 있거나 개행만 있는 경우
    if (storeInfo === "" || storeInfo.replace(/\n/g, "") === "") {
        alert("매장 소개는 공백만 있을 수 없습니다. 내용을 입력해주세요.");
        return false;
    }

    // 음식 카테고리
    if ($("input[name='foodCategory1']").is(":checked") && $("input[name='foodCategory2']").is(":checked") && $("input[name='foodCategory3']").is(":checked")) {
        result = true;
    } else {
        alert("음식 카테고리를 3차까지 선택해주세요");
        return false;
    }

    // 메뉴
    if ($("input[name='menuList[0].menuName']").val() == null || $("input[name='menuList[0].menuPrice']") == null) {
        alert("메뉴 이름과 가격은 필수 입력사항입니다.")
        return false;
    }

    return result;
}