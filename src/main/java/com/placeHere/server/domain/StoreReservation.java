package com.placeHere.server.domain;

import lombok.Data;

import java.util.List;

@Data
public class StoreReservation {
    private StoreOperation storeOperation;
    private List<ReservationTimeStatus> reservationTimeStatusList;
}
