import { getLocation, getApiKey } from "./geoLocation.mjs";

$(function() {

    // form 속성 설정
    $("form[name='addStore']").attr("action", "/store/addStore").attr("method", "post").attr("enctype", "multipart/form-data");

    // 중복체크, 사업자번호 확인 결과 텍스트
    let duplicateResult = "";

    // API KEY
    let businessNoApiKey = ""
    let googleApiKey = ""

    getApiKey().then(apiKey => {
        businessNoApiKey = apiKey.businessNo;
        googleApiKey = apiKey.google;
    });

    // 다음 단계 버튼 함수
    $("button[name='submitBtn']").on("click", function (e) {

        if (chkValidation()) {

            // 음식 카테고리 합치기
            var category1 = $("input[name='foodCategory1']:checked").val();
            var category2 = $("input[name='foodCategory2']:checked").val();
            var category3 = $("input[name='foodCategory3']:checked").val();
            var category4 = $("input[name='foodCategory4']").val();
            $("input[name='foodCategoryId']").val(category1 + "/" + (category2.replace("-", ", ")) + "/" + category3 + "/" + category4);

            // 매장 주소 합치기
            $("input[name='storeAddr']").val($("input[name='storeAddr1']").val() + ', ' + $.trim(("input[name='storeAddr2']").val()));

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

            $("form[name='addStore']").attr("action", "/store/addStore").attr("method", "post").attr("enctype", "multipart/form-data").submit();

        } else {
            e.preventDefault();
        }

    });


    // 사업자 번호 확인 API (공공 API)
    function checkDuplicateBusinessNo(businessNo) {

        var bNo = {"b_no": [businessNo]};

        $.ajax({
            url: "https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey="+businessNoApiKey,
            type: "POST",
            data: JSON.stringify(bNo),
            dataType: "JSON",
            contentType: "application/json",
            accept: "application/json",
            success: function(result) {
                console.log(result);

                let btnActive = false;

                let data = result.data[0];

                let bState = data.b_stt_cd;
                let taxType = data.tax_type_cd;

                console.log((bState === "") ? "null" : bState);
                console.log((taxType === "") ? "null" : taxType);

                if (bState !== "01") {
                    duplicateResult = "휴업 또는 폐업상태입니다.";
                    btnActive = false;
                }

                if (taxType === "") {
                    duplicateResult = data.tax_type;
                    btnActive = false;
                }

                if (bState === "01" && taxType !== "") {2
                    duplicateResult = "사용가능한 사업자 번호입니다.";
                    btnActive = true;
                }

                if (btnActive) {
                    $("#useBusinessNo").prop("disabled", false);
                } else {
                    $("#useBusinessNo").prop("disabled", true);
                }

                $("#duplicateResult").text(duplicateResult);
            },
            error: function(result) {
                console.log(result.responseText);
            }
        });

    }


    // 사업자 정보 중복확인 및 상태 확인 메서드
    $("input[name='businessNo']").on('input', function () {

        // let businessNo = $("input[name='businessNo']").val();

        // 숫자만 입력하도록 필터링 (숫자가 아닌 값 제거후 업데이트)
        let businessNo = $(this).val().replace(/[^0-9]/g, '');
        $(this).val(businessNo);

        if (businessNo.length === 10) {

            $.ajax({
                url: "/api-store/chkDuplicateBusinessNo",
                method: "GET",
                data: {businessNo: businessNo},
                dataType: "json",
                headers: {
                    "Accept": "application/json"
                },
                success: function (response) {
                    if (response) {

                        checkDuplicateBusinessNo(businessNo);

                    } else {
                        duplicateResult = "중복된 사업자 번호입니다.";
                        $("#duplicateResult").text(duplicateResult);
                    }
                },
                error: function (xhr, status, error) {
                    console.error("Request failed: " + status + ", " + error);
                }
            });

        } else {
            $("#useBusinessNo").prop("disabled", true);
            duplicateResult = "사업자 번호는 하이픈(-) 제외 10글자 숫자입니다.";
            $("#duplicateResult").text(duplicateResult);
        }

    });


    // 사업자 번호 사용하기 버튼
    $(document).on('click', "#useBusinessNo",function () {
       $("input[name='businessNo']")
           .prop("readonly", true)
           .css("background-color", "#f0f0f0");

       $("#useBusinessNo span").text("변경하기");
       $("#useBusinessNo").attr("id", "changeBusinessNo");

    });


    // 사업자 번호 변경하기 버튼
    $(document).on('click', "#changeBusinessNo",function () {
        $("input[name='businessNo']")
            .prop("readonly", false)
            .css("background-color", "");

        $("#changeBusinessNo span").text("사용하기");
        $("#changeBusinessNo").attr("id", "useBusinessNo");

    });

    // 1차 분류 선택이 변경되면 2차, 3차 분류의 체크박스 해제 및 분류 표시
    $("input[name='foodCategory1']").on("change", function() {
        var selectedCategory1 = $("input[name='foodCategory1']:checked").val();

        $("#foodCategory4").css("display", "none");

        // 2차 분류 표시
        if (selectedCategory1) {
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
    });


    // 2차 분류 선택이 변경되면 3차 분류의 체크박스 해제 및 해당하는 3차 분류 표시
    $("input[name='foodCategory2']").on("change", function() {
        var selectedCategory2 = $("input[name='foodCategory2']:checked").val();
        updateDetailCategory(selectedCategory2);

        // 3차 분류의 체크 해제
        $("input[name='foodCategory3']").prop("checked", false);
        $("#foodCategory4").css("display", "none");
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


    // 상세 분류 표시 함수
    $("input[name='foodCategory3']").on("change", function () {

        var selectedCategory3 = $("input[name='foodCategory3']:checked").val();

        if (selectedCategory3 != "전체") {
            $("#foodCategory4").css("display", "block");
            $("input[name='foodCategory4']").focus();
        } else {
            $("#foodCategory4").css("display", "none");
        }
    });


    // 다음 주소 검색
    $("#daumPostCode, input[name='storeAddr1']").on("click", function () {

        new daum.Postcode({
            oncomplete: function(data) {
                var addr = data.address; // 최종 주소 변수

                // 주소 정보를 해당 필드에 넣는다.
                $("input[name='storeAddr1']").val(addr);
                $("input[name='storeAddr2']").focus();

                getLocation(googleApiKey, addr).then(location => {

                    $("input[name='storeLocation']").val(location);

                });

            }
        }).open();

    });

    var hashtagCnt = 1;

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
        $(this).closest('.addedHashtagInput').remove();
        hashtagCnt--;

        if (hashtagCnt < 5) {
            $('button[name="addHashtagInput"]').prop("disabled", false);
        } else {
            $('button[name="addHashtagInput"]').prop("disabled", true);
        }
    });


    // 메뉴 입력창 생성함수
    var menuCnt = 1;

    $('button[name="addMenuInput"]').on('click', function () {

        // 컨테이너 생성
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
                                    `<input type="text" class="css-1bkd15f e1uzxhvi2" placeholder="메뉴 이름을 입력해주세요." name="menuList[${menuCnt}].menuName" minlength="1" maxlength="30" required>` +
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

        $(this).closest('.addedMenuInput').remove();
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
        
        // 사업자 번호
        if ($("input[name='businessNo']").prop("readonly")) {
            result = true;
        } else {
            alert("사업자 번호를 입력후 사용하기를 눌러주세요");
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


    // 편의시설 중복 방지 함수 (콜키지 프리, 콜키지 가능/키즈존, 노키즈존)
    $("input[name='amenitiesNoList']").on("change", function () {

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
