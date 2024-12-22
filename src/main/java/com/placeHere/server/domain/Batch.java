package com.placeHere.server.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Batch implements Serializable {

    private Long id;
    private String batchName;
    // SUCCESS, FAIL
    private String status;
    private Date execDt;
}
