package com.placeHere.server.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class StoreReservation {
    private StoreOperation storeOperation;
    private List<ReservationTimeStatus> reservationTimeStatusList;

}
