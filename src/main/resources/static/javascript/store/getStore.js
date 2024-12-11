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


    if (mode === "info") {

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

