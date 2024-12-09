package com.placeHere.server.controller.reservation;

import com.placeHere.server.dao.store.StoreDao;
import com.placeHere.server.domain.StoreOperation;
import com.placeHere.server.domain.StoreReservation;
import com.placeHere.server.service.reservation.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api-reservation/*")
public class ReservationRestController {

    @Autowired
    private ReservationService reservationService;

//    @Autowired
//    private StoreDao storeDao;


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


    // 가게 운영 정보 조회 API
    @GetMapping("/getOperationByDate")
    public ResponseEntity<StoreReservation> getOperationByDate(@RequestParam("storeId") int storeId,
                                                             @RequestParam("effectDt") String effectDt) {
        try {
            /*Date sqlEffectDt = Date.valueOf(effectDt); // String -> java.sql.Date 변환
            StoreOperation operation = storeDao.getOperationByDt(storeId, sqlEffectDt);*/

            Map<String, Object> map = new HashMap<>();
            map.put("storeId", storeId);
            map.put("effectDt", effectDt);
            StoreReservation storeReservation = reservationService.getStoreReservation(map);

            if (storeReservation != null) {
                return ResponseEntity.ok(storeReservation);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }







}