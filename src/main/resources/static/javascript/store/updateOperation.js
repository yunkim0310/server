$(function () {

    $("form").attr("action", "/store/updateOperation").attr("method", "post");

    $("#breakTimeToggle").on("change", function () {

        let isEnabled = this.checked;
        $("#breakStart, #breakEnd").prop("disabled", !isEnabled);

    })

    $("#submit").on("click", function () {

        $("form").submit();

    })

});