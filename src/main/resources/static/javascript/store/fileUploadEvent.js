function fileUploadEvent(fieldName, path) {

    $(document).on("change", `input[class^='${fieldName}']:file`,function () {

        const $this = $(this);
        var className = $this.attr('class');

        console.log(className)

        const file = $this[0].files[0];

        var $storeImg = $(`input[class='${className}']:hidden`);

        console.log($storeImg.val() === "");

        if ($storeImg.val() == null || $storeImg.val() === "" || $storeImg.val().includes("_")) {

            console.log("uploadFile");
            console.log($storeImg.val());

            uploadFile(file, path).then(result => {

                console.log(result);
                $storeImg.val(result.filePath);
                $(`img.${className}`).attr("src", result.url)
                $(`#${className}`).css("display", "flex");
            });

        } else {

            console.log("updateFile");
            console.log($storeImg.val());

            updateFile($storeImg.val(), file, path).then(result => {

                console.log(result);
                $storeImg.val(result.filePath);
                $(`img.${className}`).attr("src", result.url)
            });
        }

    });

}