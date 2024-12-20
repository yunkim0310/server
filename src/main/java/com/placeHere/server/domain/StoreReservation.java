package com.placeHere.server.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class StoreReservation {

    // Field
    // 가게 운영 정보
    private StoreOperation storeOperation;
    // 예약 시간 상태
    private List<ReservationTimeStatus> reservationTimeStatusList;

}
