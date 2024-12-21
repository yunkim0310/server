$(function () {

    $("form").attr("action", "/store/updateOperation").attr("method", "post");

    $("#breakTimeToggle").on("change", function () {

        let isEnabled = this.checked;
        $("#breakTimeStart, #breakTimeEnd").prop("disabled", !isEnabled);
        $("#breakTimeStart, #breakTimeEnd").prop("required", isEnabled);

    })

    $("#submit").on("click", function () {

        $("form").submit();

    })

});