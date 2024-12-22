// 위도 경도 얻는 함수
export function getLocation(key, storeAddr) {
    return new Promise((resolve, reject) => {

        $.ajax({
            url: `https://maps.googleapis.com/maps/api/geocode/json?address=${storeAddr}&key=${key}`,
            type: "GET",
            dataType: "json",
            success: function (result) {
                const lat = result.results[0].geometry.location.lat; // results로 수정
                const lng = result.results[0].geometry.location.lng; // results로 수정
                const location = lat + ',' + lng;
                resolve(location);  // 성공 시 위치값 반환
            },
            error: function (error) {
                reject(error);  // 실패 시 에러 반환
            }
        });

    });

}

export function getApiKey() {
    return new Promise((resolve, reject) => {

        $.ajax({
            url: "/api-store/getApiKey",
            method: "GET",
            dataType: "json",
            success: function (result) {
                resolve(result);

                console.log("API Key Return");
            }
        });

    });

}
