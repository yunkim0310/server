$(function () {

    $("form").attr("action", "/store/addOperation").attr("method", "post");

    $("#breakTimeToggle").on("change", function () {

        let isEnabled = this.checked;

        console.log(isEnabled);
        $("#breakTimeStart, #breakTimeEnd").prop("disabled", !isEnabled);

    });

    $("#submit").on("click", function () {

        $("form").submit();

    });

});