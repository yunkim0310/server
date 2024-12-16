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
            System.out.println("11");
            System.out.println(storeReservation);

            if (storeReservation != null) {
                return ResponseEntity.ok(storeReservation);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @RequestMapping(value = "countRsrvStore", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Map<String, Integer>> countRsrvStore(@RequestBody Map<String, Object> payload) {
        try {
            int storeId = (int) payload.get("storeId");

            // 탈퇴할 점주 회원의 예약 권수 카운팅 (전화 예약 제외)
            Integer rsrvCount = reservationService.getCountRsrvStore(storeId);
            Integer phoneRsrvCount = reservationService.getCountRsrvNumber(storeId);

            // null 값 처리를 위해 기본값 0으로 설정
            int finalRsrvCount = rsrvCount != null ? rsrvCount : 0;
            int finalPhoneRsrvCount = phoneRsrvCount != null ? phoneRsrvCount : 0;

            // 결과를 Map에 담아 반환
            Map<String, Integer> result = new HashMap<>();
            result.put("reservationCount", finalRsrvCount); // 전화 예약 제외한 예약 권수
            result.put("phoneReservationCount", finalPhoneRsrvCount); // 전화 예약 권수

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // 예외 발생 시 에러 응답 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }








    @RequestMapping(value = "countRsrvUser", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Map<String, Integer>> countRsrvUser(@RequestBody Map<String, Object> payload) throws Exception{
            // userName을 payload에서 가져옴
            String userName = (String) payload.get("userName");
            // 탈퇴할 일반 회원의 예약 권수 카운팅
            Integer rsrvCount = reservationService.getCountRsrvUser(userName);

            // null 값 처리를 위해 기본값 0으로 설정
            int finalRsrvCount = rsrvCount != null ? rsrvCount : 0;

            // 결과를 Map에 담아 반환
            Map<String, Integer> result = new HashMap<>();
            result.put("reservationCount", finalRsrvCount);

            return ResponseEntity.ok(result);
    }

}