package com.placeHere.server.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Closeday {

    // Field
    // PK
    private int closedayId;
    // 가게 Id
    private int storeId;
    // 휴무일
    private String closeday;
    // 휴무일 총 개수
    private int totalCnt;

}
