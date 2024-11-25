package com.placeHere.server.domain;

import lombok.Data;
import java.util.Date;

@Data
public class Reservation {
    private int rsrvNo;
    private int storeId;
    private String userName;
    private String rsrvStatus;
    private Date rsrvDt;
    private int rsrvPerson;
    private int amount;
    private String paymentId;
    private String rsrvReq;
    private String reason;
    private Date rsrvCreateTime;
    private String storeName;
    private String storeAddr;
    private String rsrvNumber;
}
