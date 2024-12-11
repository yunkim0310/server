let storeList = [];

$(function () {

    const mode = $("input[name='mode']:hidden").val();

    console.log(mode);
    
    // 좋아요 추가, 취소
    $("a#like").on("click", function () {

        const storeId = $("input[name='storeId']:hidden").val();
        const likeId = $(this).data("likeid");
        const $this = $(this);
        
        // 좋아요 추가
        if ($this.attr("class") === "btn-like-inactive") {

            $.ajax({
                url: "/api-store/addStoreLike?relationNo="+storeId,
                method: "GET",
                dataType: "json",
                success: function (result) {

                    if (result !== 0) {

                        $this.data("likeid", result);
                        $this.attr("class", "btn-like-active");

                    } else {

                        if (confirm("좋아요는 회원만 가능한 기능입니다.\n로그인 하시겠습니까?")) {
                            window.location.href = "/user/login";
                        }

                    }
                }
            })
        
        // 좋아요 취소
        } else if ($this.attr("class") === "btn-like-active") {

            $.ajax({
                url: "/api-store/removeStoreLike?likeId="+likeId,
                method: "GET",
                dataType: "json",
                success: function (result) {

                    if (result) {
                        $this.data("likeid", null);
                        $this.attr("class", "btn-like-inactive");
                    }

                }

            });

        }

    });


    // TODO 채팅 버튼 처리 추가 필요
    $("a#chat").on("click", function () {

    });


    if (mode ==="info") {

        $("button[name='operationToggle']").on("click", function () {

            var $operationMore = $("#operationMore");

            if ($operationMore.css("display") === "none") {
                $operationMore.css("display", "block");
                $(this).text("∧")
            } else {
                $operationMore.css("display", "none");
                $(this).text("∨");
            }

        });

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

    }

});

// 지도 초기화 함수
function initMap() {

    const map = new google.maps.Map(document.getElementById("map"), {
        center: {lat: 37.5400456, lng: 126.9921017},
        zoom: 12,
    });

    const bounds = new google.maps.LatLngBounds();
    const infoWindow = new google.maps.InfoWindow();

    storeList.forEach(({storeId, storeName, storeLocation}) => {

        const [lat, lng] = storeLocation.split(',').map(Number);

        const marker = new google.maps.Marker({
            position: {lat, lng},
            storeId,
            map,
        });

        bounds.extend(marker.position);

        marker.addListener("click", () => {
            infoWindow.setContent(`<a href="/getStore?storeId=${storeId}">${storeName}</a>`);
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

