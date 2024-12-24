$(function() {

    // 좋아요 처리
    $('a#like').on('click', function() {

        const $this = $(this);
        const reviewNo = $this.data('review-no');
        console.log(reviewNo);

        let likeData = {
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

                console.log(result);

                // 로그인 안 한 경우
                if (result === 0) {

                    if (confirm("좋아요는 회원만 가능한 기능입니다.\n로그인 하시겠습니까?")) {
                        window.location.href = "/user/login";
                    }

                // 좋아요 추가가 성공한 경우
                } else if (result === 1) {
                    location.reload();
                // 좋아요 취소가 된 경우
                } else if (result === -1) {
                    location.reload();
                // 오류 발생
                } else if (result === -2) {
                    alert("알수 없는 오류가 발생했습니다.");
                }
            }

        });
        // ajax End

    });

})