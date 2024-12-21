$(function () {

    $("form").attr("action", "/store/addOperation").attr("method", "post");

    // 브레이크타임 토글 이벤트처리
    $("#breakTimeToggle").on("change", function () {

        let isEnabled = this.checked;

        console.log(isEnabled);
        $("#breakTimeStart, #breakTimeEnd").prop("disabled", !isEnabled);
        $("#breakTimeStart, #breakTimeEnd").prop("required", isEnabled);

    });

    $("#submit").on("click", function () {

        $("form").submit();

    });

});