$(function () {

    $("form").attr("action", "/store/addOperation").attr("method", "post");

    // 브레이크타임 토글 이벤트처리
    $("#breakTimeToggle").on("change", function () {

        let isEnabled = this.checked;

        console.log(isEnabled);
        $("#breakTimeStart, #breakTimeEnd").prop("disabled", !isEnabled);
        $("#breakTimeStart, #breakTimeEnd").prop("required", isEnabled);

    });


    // 정기 휴무요일 최대 3개 선택
    $('input[name="regularClosedayList"]').on('change', function () {
        const checkedCount = $('input[name="regularClosedayList"]:checked').length;
        if (checkedCount > 3) {
            alert('최대 3개까지만 선택 가능합니다.');
            $(this).prop('checked', false); // 선택을 취소
        }
    });


    $("#submit").on("click", function () {

        $("form").submit();

    });

});