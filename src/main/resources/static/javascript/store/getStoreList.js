let storeList = [];

// AJAX 요청으로 storeList 데이터 가져오기
$(function () {
    
    // 가게 위치 가져오기
    $.ajax({
        url: "/api-store/getStoreLocation" + window.location.search,
        method: "GET",
        dataType: "json",
        headers: {
            "Accept": "application/json"
        },
        success: function (result) {

            storeList = result;

            // storeList가 로드된 후에 지도를 초기화
            if (typeof google !== 'undefined' && google.maps) {
                initMap();
            } else {
                console.error("Google Maps API가 로드되지 않았습니다.");
            }

        },
        error: function (xhr, status, error) {
            console.error("AJAX 요청 실패: ", error);
        }

    });

});


// 지도 초기화 함수
function initMap() {

    const map = new google.maps.Map(document.getElementById("map"), {
        center: { lat: 37.5400456, lng: 126.9921017 },
        zoom: 12,
    });

    const bounds = new google.maps.LatLngBounds();
    const infoWindow = new google.maps.InfoWindow();

    if (storeList.length > 0) {

        storeList.forEach(({storeId, storeName, storeLocation}) => {

            const [lat, lng] = storeLocation.split(',').map(Number);

            const marker = new google.maps.Marker({
                position: {lat, lng},
                storeId,
                map,
            });

            bounds.extend(marker.position);

            marker.addListener("click", () => {
                infoWindow.setContent(`<a href="/getStore?storeId=${storeId}" target="_blank">${storeName}</a>`);
                infoWindow.open({
                    anchor: marker,
                    map,
                });
            });
        });

        map.fitBounds(bounds);

        if (storeList.length === 1) {

            google.maps.event.addListenerOnce(map, 'idle', function () {
                map.setZoom(18); // 원하는 줌 레벨로 설정
            });

        }

    }

}