$(function () {

    $("header.header-wrap").hide();

    $(document).on("click", "#like",function () {

        const likeId = $(this).data("likeid");

        $.ajax({
            url: "/api-store/removeStoreLike?likeId="+likeId,
            method: "GET",
            dataType: "json",
            success: function (result) {

                if (result) {
                    window.location.href = "/getStoreLikeList";
                }

            }

        });

    });

    function submitFnc(page) {

        $("input[name='page']:hidden").val(page);

        $("form").attr("action", "/getStoreLikeList").attr("method", "GET").submit();
    }

    pageNavigator(submitFnc);

})