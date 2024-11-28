package com.placeHere.server.controller.reservation;

import com.placeHere.server.service.reservation.ReservationService;
import com.placeHere.server.service.store.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("test/reservation/*")
public class ReservationRestController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private StoreService storeService;


    public ReservationRestController(){
        System.out.println(this.getClass());
    }


    @RequestMapping(value = "updateRsrvStatus", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseEntity<String> updateRsrvStatus(@RequestBody Map<String, Object> payload) {
        try {
            int rsrvNo = (int) payload.get("rsrvNo");
            String rsrvStatus = (String) payload.get("rsrvStatus");

            // 서비스 호출하여 예약 상태 업데이트
            reservationService.updateRsrvStatus(rsrvNo, rsrvStatus);

            return ResponseEntity.ok("Reservation status updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating reservation status: " + e.getMessage());
        }
    }





}