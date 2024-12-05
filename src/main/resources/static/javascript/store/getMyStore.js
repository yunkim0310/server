$(function() {

    const mode = $("#mode").val();

    console.log(mode);
    
    // mode 에 따라서 다른 js 적용

    // 가게 리뷰
    if (mode === 'review') {

    }


    // 매장 소식
    if (mode === 'news') {

        $("form").attr("action", "/store/getMyStore").attr("method", "post").attr("enctype", "multipart/form-data");
        
        // 소식 등록
        $("#addStoreNews").on("click", function () {

            $("form[name='addStoreNews']").submit();

        })

        let previousTextarea = null; // 이전에 활성화된 textarea
        let previousValue = "";      // 이전 textarea의 초기 값
        let $previousCancelBtn = null; // 이전 취소 버튼

        // 수정 버튼 클릭 이벤트 처리
        $(document).on('click', 'button[name="updateStoreNewsForm"]',function () {

            const $reservationItem = $(this).closest('.reservation-item');
            const $currentTextarea = $reservationItem.find('textarea[name="newsContents"]');
            const $deleteBtn = $reservationItem.find('button[name="removeStoreNews"]');
            const $updateBtn = $(this); // 현재 수정 버튼
            const newsId = $(this).data("newsid");

            // 이전 textarea 가 있고 다른 버튼을 눌렀다면, 복구 로직 실행
            if (previousTextarea && previousTextarea[0] !== $currentTextarea[0]) {
                previousTextarea.val(previousValue).prop('disabled', true); // 복구 및 비활성화
                $previousCancelBtn.replaceWith(
                    `<button name="removeStoreNews" type="button" class="css-ufulao e4nu7ef3 checkBtn" data-newsid="${newsId}">삭제</button>`
                ); // 취소 버튼을 삭제 버튼으로 복구
                $previousCancelBtn = null;
            }

            // 현재 textarea 를 활성화
            if ($currentTextarea.prop('disabled')) {
                previousValue = $currentTextarea.val(); // 초기 값 저장
                $currentTextarea.prop('disabled', false); // 활성화
                const textLength = $currentTextarea.val().length;
                $currentTextarea.focus().get(0).setSelectionRange(textLength, textLength); // 커서 이동

                // 삭제 버튼을 취소 버튼으로 변경
                $deleteBtn.replaceWith(
                    `<button name="cancelEdit" type="button" class="css-ufulao e4nu7ef3 checkBtn" data-newsid="${newsId}">취소</button>`
                );

                // 수정 버튼을 새로운 updateStoreNews로 교체
                $updateBtn.replaceWith(
                    `<button name="updateStoreNews" type="button" class="css-ufulao e4nu7ef3 checkBtn" data-newsid="${newsId}">수정</button>`
                );

                // 현재 요소를 이전 요소로 저장
                previousTextarea = $currentTextarea;
                $previousCancelBtn = $reservationItem.find('button[name="cancelEdit"]');
            }
        });


        // 취소 버튼 클릭 이벤트 처리
        $(document).on('click', 'button[name="cancelEdit"]', function () {
            const $reservationItem = $(this).closest('.reservation-item');
            const $currentTextarea = $reservationItem.find('textarea[name="newsContents"]');
            const $cancelBtn = $(this);
            const $updateBtn = $reservationItem.find('button[name="updateStoreNews"]'); // 수정 버튼
            const newsId = $(this).data("newsid");

            // textarea 비활성화 및 초기 값 복구
            $currentTextarea.val(previousValue).prop('disabled', true);

            // 취소 버튼을 삭제 버튼으로 변경
            $cancelBtn.replaceWith(
                `<button name="removeStoreNews" type="button" class="css-ufulao e4nu7ef3 checkBtn" data-newsid="${newsId}">삭제</button>`
            );

            // 수정 버튼을 updateStoreNewsForm으로 복구
            $updateBtn.replaceWith(
                `<button name="updateStoreNewsForm" type="button" class="css-ufulao e4nu7ef3 checkBtn" data-newsid="${newsId}">수정</button>`
            );

            // 초기화
            previousTextarea = null;
            previousValue = "";
            $previousCancelBtn = null;
        });


        // 소식 수정 이벤트 처리
        $(document).on("click", "button[name='updateStoreNews']", function () {

            var newsId = $(this).data("newsid");

            alert(newsId);

            $("input[name='newsId']:hidden").prop("disabled", null).val(newsId);
            $("input[name='fnc']:hidden").val("update");

            console.log("updateStoreNews " + newsId);

            $("form[name='updateAndRemoveStoreNews']").submit();
        });


        // 소식 삭제 이벤트 처리
        $(document).on("click", "button[name='removeStoreNews']",function () {
            var newsId = $(this).data("newsid");

            alert(newsId);

            $("input[name='newsId']:hidden").prop("disabled", null).val(newsId);
            $("input[name='fnc']:hidden").val("remove");

            $("form[name='updateAndRemoveStoreNews']").submit();
        });

    }


    // 휴무일
    if (mode === 'closeday') {


    }

    
});