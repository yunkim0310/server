$(function () {

    console.log("fileUpload");

   // 이미지 입력에 대한 이벤트처리
    $("input[class^='reviewImg']:file").on("change", function() {

        const $this = $(this);
        var className = $this.attr('class');

        console.log(className)

        const file = $this[0].files[0];

        var $reviewImg = $(`input[name='${className}']:hidden`);

        console.log($reviewImg.val() === "");

        if ($reviewImg.val() == null || $reviewImg.val() === "") {

            console.log("uploadFile");
            console.log($reviewImg.val());

            uploadFile(file, "community/").then(result => {

                console.log(result);
                $reviewImg.val(result.filePath);
                $(`img.${className}`).attr("src", result.url)
                $(`#${className}`).css("display", "flex");
            });

        } else {

            console.log("updateFile");
            console.log($reviewImg.val());

            updateFile($reviewImg.val(), file, "community/").then(result => {

                console.log(result);
                $reviewImg.val(result.filePath);
                $(`img.${className}`).attr("src", result.url)
            });
        }

    })

});