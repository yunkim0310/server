// 팝업 표시 함수
function showCustomPopup() {
    document.getElementById("customPopup").style.display = "block";
}

// 확인 버튼 클릭 시 리다이렉트
function redirectToReservation() {
    window.location.href = "/reservation/addRsrv?storeId=1"; // 원하는 storeId 값으로 설정
}

// 페이지 로드 시 팝업 표시
window.onload = showCustomPopup;