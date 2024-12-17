package com.placeHere.server.controller.reservation;

import com.placeHere.server.dao.store.StoreDao;
import com.placeHere.server.domain.StoreOperation;
import com.placeHere.server.domain.StoreReservation;
import com.placeHere.server.service.reservation.ReservationService;
import com.placeHere.server.service.store.StoreService;
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




    @RequestMapping(value = "countReservations", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> countReservations(@RequestBody Map<String, Object> payload) {
        try {
            // role과 userName을 payload에서 가져옴
            String role = (String) payload.get("role");
            String userName = (String) payload.get("userName");

            // 반환 결과를 담을 Map
            Map<String, Object> result = new HashMap<>();

            if ("ROLE_USER".equalsIgnoreCase(role)) {
                // ROLE_USER 처리
                if (userName == null || userName.trim().isEmpty()) {
                    result.put("error", "userName is required for ROLE_USER");
                    return ResponseEntity.ok(result);
                }

                // 탈퇴할 일반 회원의 예약 권수 카운팅
                Integer rsrvCount = reservationService.getCountRsrvUser(userName);
                int finalRsrvCount = rsrvCount != null ? rsrvCount : 0;

                result.put("reservationCount", finalRsrvCount);

            } else if ("ROLE_STORE".equalsIgnoreCase(role)) {
                // ROLE_STORE 처리
                if (userName == null || userName.trim().isEmpty()) {
                    result.put("error", "userName is required for ROLE_STORE");
                    return ResponseEntity.ok(result);
                }

                // userName을 통해 storeId를 조회
                Integer storeId = storeService.getStoreId(userName);

                int finalRsrvCount = 0;
                int finalPhoneRsrvCount = 0;

                if (storeId != null && storeId > 0) {
                    // storeId가 유효할 경우 예약 건수와 전화 예약 건수 조회
                    Integer rsrvCount = reservationService.getCountRsrvStore(storeId);
                    Integer phoneRsrvCount = reservationService.getCountRsrvNumber(storeId);

                    finalRsrvCount = rsrvCount != null ? rsrvCount : 0;
                    finalPhoneRsrvCount = phoneRsrvCount != null ? phoneRsrvCount : 0;
                }

                result.put("reservationCount", finalRsrvCount);
                result.put("phoneReservationCount", finalPhoneRsrvCount);

            } else {
                // role이 유효하지 않은 경우
                result.put("error", "Invalid role. Supported roles: ROLE_USER, ROLE_STORE");
                return ResponseEntity.ok(result);
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // 예외 발생 시 에러 응답 반환
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("error", "Error occurred while counting reservations: " + e.getMessage());
            return ResponseEntity.ok(errorResult);
        }
    }


}