import { getApiKey } from "./geoLocation.mjs";

let storeList = [];

$(function () {

    const mode = $("input[name='mode']:hidden").val();

    console.log(mode);

    let googleApiKey = getApiKey().then(apiKey => {
        googleApiKey = apiKey.google;
    });

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
    
    // 좋아요 추가, 취소
    $("a#likeStore").on("click", function () {

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
    // $("a#chat").on("click", function () {
    //
    // });


    if (mode === "info") {

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


    if (mode === "statistics") {

        $.ajax({
            url: "/api-store/getStatistics" + window.location.search,
            method: "GET",
            dataType: "json",
            headers: {
                "Accept": "application/json"
            },
            success: function (result) {
                
                // 본인 가게인지 여부 확인 (본인 가게만 금주의 예약 볼 수 있음)
                const isMyStore = $("input[name='isMyStore']").val();
                
                // 통계 데이터
                const week = result.week;
                const avg = result.avg;
                const per = result.per;

                // 금주 + 평균 예약 통계 그래프
                const rsrvChart = document.getElementById('rsrvChart');

                const rsrvDataset = (isMyStore === "true")?
                    [{
                        label: "예약 평균",
                        data: [avg["월"], avg["화"], avg["수"], avg["목"], avg["금"], avg["토"], avg["일"]],
                        type: 'line'
                    },
                        {
                        label: '금주 예약',
                        data: [week["월"], week["화"], week["수"], week["목"], week["금"], week["토"], week["일"]],
                        borderWidth: 1,
                        type: 'bar'
                    }]
                    :
                    [{
                        label: "예약 평균",
                        data: [avg["월"], avg["화"], avg["수"], avg["목"], avg["금"], avg["토"], avg["일"]],
                        type: 'line'
                    }];

                new Chart (rsrvChart, {
                    type: 'bar',
                    data: {
                        labels: ['월', '화', '수', '목', '금', '토', '일'],
                        datasets: rsrvDataset
                    },
                    options: {
                        plugins: {
                            legend: {
                                display: true
                            },
                            layout: {
                                padding: {
                                    top: 30
                                }
                            },
                            title: {
                                display: true,
                                text: '예약 통계',
                                font: {
                                    size: 20
                                }
                            },
                            datalabels: {
                                anchor: 'end',
                                align: 'top',
                                formatter: (value) => value,
                                color: '#000',
                                font: {
                                    size: 12
                                }
                            }
                        },
                        scales: {
                            y: {
                                beginAtZero: true,
                                max: Math.max(...rsrvDataset[0].data.concat(rsrvDataset[0].data)) * 1.5
                            }
                        },
                    },
                    plugins: [ChartDataLabels]
                });


                // 성별, 나이대별 예약 비율
                const percentChart = document.getElementById('percentChart');

                const percentDataset =
                    [{
                        label: '남성',
                        data: [per["10대 남성"], per["20대 남성"], per["30대 남성"], per["40대 남성"], per["50대 남성"], per["60대이상 남성"]],
                        borderWidth: 1,
                        type: 'bar'
                    },
                    {
                        label: "여성",
                        data: [per["10대 여성"], per["20대 여성"], per["30대 여성"], per["40대 여성"], per["50대 여성"], per["60대이상 여성"]],
                        borderWidth: 1,
                        type: 'bar'
                    }];

                new Chart (percentChart, {
                    type: 'bar',
                    data: {
                        labels: ["10대", "20대", "30대", "40대", "50대", "60대이상"],
                        datasets: percentDataset
                    },
                    options: {
                        plugins: {
                            legend: {
                                display: true
                            },
                            layout: {
                                padding: {
                                    top: 30
                                }
                            },
                            title: {
                                display: true,
                                text: '성별, 나이대별 예약 비율',
                                font: {
                                    size: 20
                                }
                            },
                            datalabels: {
                                anchor: 'end',
                                align: 'top',
                                formatter: (value) => value+' %',
                                color: '#000',
                                font: {
                                    size: 12
                                }
                            }
                        },
                        scales: {
                            y: {
                                beginAtZero: true,
                                max: Math.max(...percentDataset[0].data.concat(percentDataset[1].data)) * 1.5
                            }
                        },
                    },
                    plugins: [ChartDataLabels]
                });

            }

        });

    }
    
    
    // 주변시설 추천
    if (mode === "nearby") {

        let map;

        async function initMap() {

            const { Map } = await google.maps.importLibrary('maps');
            const infoWindow = new google.maps.InfoWindow();

            const [lat, lng] = storeList[0].storeLocation.split(',').map(Number);

            let center = new google.maps.LatLng(lat, lng);

            map = new Map(document.getElementById('nearbyMap'), {
                center: center,
                zoom: 16,
                mapId: 'DEMO_MAP_ID',
            });

            new google.maps.Marker({
                position: center,
                map: map,
                title: 'Center Location',
                icon: {
                    path: google.maps.SymbolPath.CIRCLE,
                    fillColor: '#4880FF',
                    fillOpacity: 1,
                    scale: 10,
                    strokeColor: '#FFFFFF',
                    strokeWeight: 5,
                },
            });

            getNearbyPlace(lat, lng);

        }

        async function getNearbyPlace(lat, lng) {
            const { Place, SearchNearbyRankPreference } = await google.maps.importLibrary('places');
            const { AdvancedMarkerElement } = await google.maps.importLibrary('marker');

            // 500m 이내 공원, 카페, 술집 1개 설정
            const categories = ['park', 'cafe', 'bar'];
            const center = new google.maps.LatLng(lat, lng);
            const radius = 500;

            for (const category of categories) {
                const request = {
                    fields: ['displayName', 'location', 'businessStatus', 'formattedAddress',
                            'photos', 'types', 'rating', 'nationalPhoneNumber'],
                    locationRestriction: {
                        center: center,
                        radius: radius,
                    },
                    includedPrimaryTypes: [category],
                    maxResultCount: 1,
                    rankPreference: SearchNearbyRankPreference.POPULARITY,
                    language: 'ko',
                    region: 'kr',
                };

                try {
                    
                    const { places } = await Place.searchNearby(request);
                    const infoWindow = new google.maps.InfoWindow();

                    if (places && places.length) {

                        const place = places[0];

                        console.log(`Recommended ${category}:`, place);

                        // 마크 추가
                        const marker = new AdvancedMarkerElement({
                            map,
                            position: place.location,
                            title: place.displayName,
                        });


                        // 마크 클릭 정보 추가
                        marker.addListener("click", () => {
                            infoWindow.setContent(`
                                <a href="https://www.google.com/search?q=${place.displayName}" target="_blank">${place.displayName}</a>
                                <p>${place.formattedAddress}</p>
                                `);
                            infoWindow.open({
                                anchor: marker,
                                map,
                            });
                        });

                        if (place) {

                            console.log(place.photos);

                            if (place.photos.length > 0) {

                                $(`#${category}Img`).attr("src", `https://places.googleapis.com/v1/${place.photos[0].name}/media?maxHeightPx=400&maxWidthPx=400&key=${googleApiKey}`)
                                    .attr("alt", place.displayName);
                            } else {

                                $(`#${category}Img`).attr("src", "/images/store/icon_no_image.png")
                                    .attr("style", "background-color: #FFFFFF;")
                                    .attr("alt", place.displayName);
                            }

                            $(`#${category}Name`).attr("style","font-size: 25px;").text(`${place.displayName}`);
                            $(`#${category}Addr`).attr("style","font-size: 20px;").text(`${place.formattedAddress}`);
                            $(`#${category}Rating`).attr("style","font-size: 20px;").text(`${(place.rating == null)? '등록된 평점이 없습니다' : '★ ' + place.rating}`);
                            $(`#${category}Phone`).attr("style","font-size: 20px;").text(`${(place.nationalPhoneNumber == null)? '등록된 전화번호가 없습니다' : place.nationalPhoneNumber}`);

                        } else {

                            $(`#${category}InfoNone`).attr("style","display: block;").text("주변에 해당 시설이 없습니다.");
                            $(`#${category}Info table`).css("display", "none");

                        }

                    } else {
                        console.log(`No ${category} found within ${radius}m.`);
                    }
                } catch (error) {
                    console.error(`Error fetching ${category}:`, error);
                }
            }
        }

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

                // storeList 가 로드된 후에 지도를 초기화
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

    // 메뉴 사진 이벤트 처리
    $(".menuImgLink").on("click", function () {

        const url = $(this).data("url");
        const title = $(this).data("title");

        openImagePopup(url, title);
    })

});

// 메뉴 사진 팝업 메서드
function openImagePopup(imageUrl, title) {
    const popupOptions = 'width=300,height=300,resizable=no,scrollbars=no';
    const popup = window.open('', '_blank', popupOptions);

    // 팝업창 내용 작성
    if (popup) {
        popup.document.write(`
            <!DOCTYPE html>
            <html>
            <head>
                <title>${title}</title>
                <style>
                    body { margin: 0; display: flex; justify-content: center; align-items: center; height: 100vh; background-color: #000; }
                    img { max-width: 100%; max-height: 100%; background-color: #FFFFFF }
                </style>
            </head>
            <body>
                <img src="${imageUrl}" alt="Popup Image">
            </body>
            </html>
        `);
        popup.document.close();
    }
}


