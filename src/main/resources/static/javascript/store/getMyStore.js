$(function() {

    const mode = $("#mode").val();

    console.log(mode);
    
    // mode 에 따라서 다른 js 적용

    // 가게 리뷰
    if (mode === 'review') {
        alert("review.js start");
    }


    // 매장 소식
    if (mode === 'news') {

        alert('news.js start');

        $("form").attr("action", "/store/getMyStore").attr("method", "post").attr("enctype", "multipart/form-data");

        $("#addStoreNews").on("click", function () {

            $("form[name='addStoreNews']").submit();

        })


        // 수정 폼 버튼
        $(document).on("click", "button[name='updateStoreNewsForm']", function () {
            var $row = $(this).closest("tr"); // 클릭한 버튼의 행을 가져옴
            var newsId = $(this).data("newsid"); // 버튼의 data-newsid 값 가져옴

            console.log("updateStoreNewsForm " + newsId);

            // 기존에 열려 있던 수정 폼 복원
            $("tr").each(function () {
                var $currentRow = $(this);
                if ($currentRow.data("originalImg")) {
                    // 복원할 데이터가 있으면 복원
                    var originalImg = $currentRow.data("originalImg");
                    var originalContents = $currentRow.data("originalContents");
                    var originalDt = $currentRow.data("originalDt");
                    var originalNewsId = $currentRow.data("originalNewsId");

                    $currentRow.html(`
                            <td>${originalNewsId}</td>
                            <td>${originalImg}</td>
                            <td>${originalContents}</td>
                            <td>${originalDt}</td>
                            <td>
                                <button name="updateStoreNewsForm" data-newsid="${originalNewsId}" type="button">수정</button>
                                <button name="removeStoreNews" data-newsid="${originalNewsId}" type="button">삭제</button>
                            </td>
                        `);

                    $currentRow.removeData("originalImg");
                    $currentRow.removeData("originalContents");
                    $currentRow.removeData("originalDt");
                }
            });

            // 현재 행의 기존 데이터 저장
            var originalNewsId = $row.find("td:eq(0)").text().trim();
            var originalImg = $row.find("td:eq(1)").text().trim(); // 첫 번째 열 (이미지)
            var originalContents = $row.find("td:eq(2)").text().trim(); // 두 번째 열 (내용)
            var originalDt = $row.find("td:eq(3)").text().trim();

            $row.data("originalImg", originalImg);
            $row.data("originalContents", originalContents);
            $row.data("originalDt", originalDt);
            $row.data("originalNewsId", originalNewsId);

            // 행 내용을 수정 폼으로 변경
            $row.html(`
                    <td>${newsId}</td>
                    <td>
                        <input type="file" name="newsImgFile" accept="image/*">
                    </td>
                    <td>
                        <textarea name="newsContents" rows="3" cols="30">${originalContents}</textarea>
                    </td>
                    <td>
                        ${originalDt}
                    </td>
                    <td>
                        <button name="updateStoreNews" data-newsid="${originalNewsId}" type="button">저장</button>
                        <button name="cancelEdit" type="button">취소</button>
                    </td>
                `);

            console.log(`Editing row with newsId: ${originalNewsId}`);
        });


        // 수정 취소 버튼
        $(document).on("click", "button[name='cancelEdit']", function () {

            // 현재 행 가져오기
            var $row = $(this).closest("tr");

            // 저장된 원래 데이터를 읽어옴
            var originalImg = $row.data("originalImg");
            var originalContents = $row.data("originalContents");
            var originalDt = $row.data("originalDt");
            var originalNewsId = $row.data("originalNewsId");

            console.log("cancelEdit " + originalNewsId);

            // 행 내용을 원래 데이터로 복원
            $row.html(`
                    <td>${originalNewsId}</td>
                    <td>${originalImg}</td>
                    <td>${originalContents}</td>
                    <td>${originalDt}</td>
                    <td>
                        <button name="updateStoreNewsForm" data-newsid="${originalNewsId}" type="button">수정</button>
                        <button name="removeStoreNews" data-newsid="${originalNewsId}" type="button">삭제</button>
                    </td>
                `);
        });


        // 소식 수정
        $(document).on("click", "button[name='updateStoreNews']", function () {

            var newsId = $(this).data("newsid");

            $("input[name='newsId']:hidden").prop("disabled", null).val(newsId);
            $("input[name='fnc']:hidden").val("update");

            console.log("updateStoreNews "+newsId);

            $("form[name='updateAndRemoveStoreNews']").submit();

        })


        // 소식 삭제
        $("button[name='removeStoreNews']").on("click", function () {

            var newsId = $(this).data("newsid");

            console.log(newsId);

            $("input[name='newsId']:hidden").prop("disabled", null).val(newsId);
            $("input[name='fnc']:hidden").val("remove");

            $("form[name='updateAndRemoveStoreNews']").submit();
        });

    }


    // 휴무일
    if (mode === 'closeday') {

        alert('closeday.js start');

    }

    
});