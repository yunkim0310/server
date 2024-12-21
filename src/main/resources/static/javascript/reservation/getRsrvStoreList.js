// 이벤트 전파 중지
function stopPropagation(event) {
    event.stopPropagation();
}

// AJAX로 예약 상태 업데이트
function updateRsrvStatus(rsrvNo, buttonElement) {
    console.log("Reservation Number: ", rsrvNo); // 디버깅용 로그
    $.ajax({
        url: '/api-reservation/updateRsrvStatus',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            rsrvNo: rsrvNo,
            rsrvStatus: '예약 확정'
        }),
        success: function (response) {
            const row = $(buttonElement).closest('tr');
            row.find('.status-cell').text('예약 확정');
            row.find('.action-cell').empty();
        },
        error: function () {
            alert('Failed to update reservation status.');
        }
    });
}

// 전화번호 마스킹 처리
document.addEventListener("DOMContentLoaded", function () {
    const phoneElements = document.querySelectorAll("[data-phone]");
    phoneElements.forEach(element => {
        const originalNumber = element.textContent.trim(); // 원래 전화번호 가져오기
        if (originalNumber.length >= 7) {
            const maskedNumber = originalNumber.substring(0, 3) + "xxxx" + originalNumber.substring(7);
            element.textContent = maskedNumber; // 마스킹된 번호로 교체
        }
    });
});

// 페이지 이동 및 검색 실행
function search(page) {
    $("input[name='page']").val(page);
    $("form[name='searchForm']").attr("action", "/reservation/getRsrvStoreList").attr("method", "post").submit();
}

// 자동으로 검색 폼 제출
function autoSubmitForm() {
    document.getElementById("searchForm").submit();
}

// 페이지 네비게이터 초기화
pageNavigator(search);