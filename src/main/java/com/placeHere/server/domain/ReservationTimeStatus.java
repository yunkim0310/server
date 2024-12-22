package com.placeHere.server.domain;

import lombok.Data;

@Data
public class ReservationTimeStatus {

    //Field
    //예약 일시
    private String rsrvDt;
    //예약 인수
    private int rsrvPerson;
}
