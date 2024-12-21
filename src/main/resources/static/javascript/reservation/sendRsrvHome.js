// 확인 버튼 클릭 시 리다이렉트
function redirect() {
    window.location.href = "/"; // '/' 경로로 리다이렉트
}

// 페이지 로드 시 팝업 표시
window.onload = function () {
    document.getElementById('customAlert').style.display = 'block';
    document.getElementById('overlay').style.display = 'block';
};