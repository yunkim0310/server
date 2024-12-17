$(function() {

    // 좋아요 처리
    $('a#like').on('click', function() {

        const $this = $(this);
        const reviewNo = $this.data('review-no');
        const userName = $this.data('username');

        console.log(reviewNo);
        console.log(userName);

        const currentLikeCount = parseInt($('.like-count').text());
        const isActive = $this.attr("class") === "btn-like-active";

        let likeData = {
            userName: userName,
            relationNo: reviewNo,
            target: 'review',
        };

        $.ajax({
            url: "/api-review/addLike",
            method: "POST",
            data: JSON.stringify(likeData),
            dataType: "json",
            headers: {
                "Content-Type" : "application/json"
            },
            success: function(result) {

//                alert(result);

                // 좋아요 취소
//                if (result === "-1") {

//                    alert("좋아요 취소 성공");

//                } else if (result === "1") {

//                    alert("좋아요 등록 성공");

//                } else {
//                    alert("오류");
//                }

                location.reload();

            }

        });
        // ajax End

    });

})