
$(function () {
    
    // 지도 스크롤 이벤트
    $(window).on('scroll', function() {

        const scrollThreshold = 300; // 스크롤 위치 기준
        const mapContainer = $('#map-container');

        if ($(window).scrollTop() >= scrollThreshold) {
            mapContainer.css({
                'position': 'fixed',  // 지도 고정
                'bottom': '0',  // 화면 하단에 고정
                'top': 'auto'  // 상단 위치는 제거
            });
        } else {
            mapContainer.css({
                'position': 'absolute',  // 원래 위치로 돌아감
                'bottom': 'auto',  // 하단 고정 해제
                'top': '300px'  // 원래 위치로 설정
            });
        }
    });
    
})

