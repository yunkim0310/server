$(function() {

//    alert("getReview.js");

    // 댓글 수정
    $("button.btn-primary.edit").on("click", function() {

        var commentNo = $(this).data("comment-no");
        var currentContent = $(this).data("current-comment");
        console.log("commentNo chk :: ", commentNo);

        const newContent = prompt("댓글 내용을 수정하세요:", currentContent);

        if(newContent === null) return; // 사용자가 취소 버튼 누른경우

        const commentData = {
            "commentNo": commentNo ,
            "commentsContent": newContent
        };

        fetch('/api-review/updateComment', {
            method: 'PUT',
            headers: {
             'Content-Type': 'application/json'
            },
            body: JSON.stringify(commentData)
        })
        .then(commentData => {
            console.log("222222222"+commentData);

            alert("수정이 완료되었습니다.");
            location.reload();
        })
        .catch(error => {
            alert(error.message);
        });

    });


    // 댓글 추가
    function addComment() {

        alert("댓글을 등록하시겠습니까");
        // 기본 폼 제출 동작 방지
        const reviewNo = document.getElementById('reviewNo').value;
        const commentsContent = document.getElementById('commentsContent').value;

        // 댓글 길이 체크
        if (commentsContent.trim().length < 1 || commentsContent.length > 100) {
            alert("댓글은 최소 1자, 최대 100자까지 입력 가능합니다.");
            return;
        }

        console.log("reviewNo chk :: ", reviewNo);
        console.log("commentsContent chk :: ", commentsContent);
        console.log("username chk :: ", username);

        fetch('/api-review/addComment', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                reviewNo: reviewNo,
                username: username,
                commentsContent: commentsContent
            }),
        })
        .then(response => {

            if (!response.ok) {
                throw new Error('댓글 추가 실패');
            }

            return response.json(); // 댓글 추가 후 서버의 JSON 응답 파싱
        })
        .then(data => {

            // 댓글 목록 업데이트
            const commentList = document.querySelector('.comment-list');
            const newComment = document.createElement('li');
            newComment.textContent = data.commentsContent; // 서버에서 응답받은 댓글 내용
            commentList.appendChild(newComment);

            location.reload();

            document.getElementById('commentsContent').value = ''; // 입력란 초기화

            const commentCountElement = document.querySelector('.review-container .comment-count');
            let currentCount = parseInt(commentCountElement.textContent);
            commentCountElement.textContent = currentCount + 1;
        })
        .catch(error => {
            document.querySelector('.error-message').textContent = error.message;
        });


    }


    // 댓글 삭제 함수
    function removeComment(commentNo, buttonElement) {

        if (confirm("정말로 댓글을 삭제하시겠습니까?")) {

           fetch('/removeComment/' + commentNo, {
               method: 'DELETE',
           })
           .then(response => {

               if (!response.ok) {
                   throw new Error('댓글 삭제 실패');
               }

               const commentItem = buttonElement.parentElement; // 삭제할 댓글 항목을 선택
               commentItem.remove();
           })
           .catch(error => {
               alert(error.message); // 오류 발생 시 경고 메시지 표시
           });
        }
    }


    // submit 함수
    function getReview(page) {

       $("input[name='page']").val(page);

       $("form[name='pagingForm']").attr("action", "/review/getReview").attr("method", "GET").submit();
    }


    // 펑션 이름이랑 같게
    pageNavigator(getReview);


   // 댓글 추가 버튼
   $("button[name='addCommentBtn']").on("click", function() {

//        alert("addComment Clicked");

        addComment();
   });


});
