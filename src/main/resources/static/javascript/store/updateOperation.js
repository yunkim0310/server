$(function () {

    alert("변경된 운영정보는 14일 후에 적용됩니다.");

    let checkedCount = $('input[name="regularClosedayList"]:checked').length;

    $("form").attr("action", "/store/updateOperation").attr("method", "post");

    $("#breakTimeToggle").on("change", function () {

        let isEnabled = this.checked;
        $("#breakTimeStart, #breakTimeEnd").prop("disabled", !isEnabled);
        $("#breakTimeStart, #breakTimeEnd").prop("required", isEnabled);

    });


    // 정기 휴무요일 최대 3개 선택
    $('input[name="regularClosedayList"]').on('change', function () {
        checkedCount = $('input[name="regularClosedayList"]:checked').length;
        if (checkedCount > 3) {
            alert('최대 3개까지만 선택 가능합니다.');
            $(this).prop('checked', false); // 선택을 취소
        }
    });


    $("#submit").on("click", function () {
        $("form").submit();
    })

});