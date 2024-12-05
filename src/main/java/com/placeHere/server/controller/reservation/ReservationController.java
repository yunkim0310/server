package com.placeHere.server.controller.reservation;

import com.placeHere.server.domain.Reservation;
import com.placeHere.server.domain.Search;
import com.placeHere.server.domain.Store;
import com.placeHere.server.domain.StoreOperation;
import com.placeHere.server.service.reservation.PaymentService;
import com.placeHere.server.service.reservation.ReservationService;
import com.placeHere.server.service.store.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;


@Controller
@RequestMapping("reservation/*")
public class ReservationController {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }


    @Autowired
    private ReservationService reservationService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private PaymentService paymentService;


    public ReservationController(){
        System.out.println("ReservationController Start");
    }

    @RequestMapping( value="getRsrv", method=RequestMethod.GET )
    public String getRsrv(@RequestParam("rsrvNo") int rsrvNo, Model model) throws Exception {

        Reservation reservation = reservationService.getRsrv(rsrvNo);

        System.out.println("/reservation/getRsrv : GET");

        model.addAttribute("reservation", reservation);

        return "reservation/getRsrv";
    }


    @RequestMapping(value = "getRsrvStoreList", method = RequestMethod.GET)
    public String getRsrvStoreList(
            @RequestParam("storeId") int storeId, // 가게 ID는 필수
            Model model) throws Exception {

        // 초기 Search 객체 설정 (기본값)
        Search search = new Search();
        search.setStartDate(null); // 기본값 없음
        search.setEndDate(null); // 기본값 없음
        search.setSearchStatuses(null); // 모든 상태 포함

        // 서비스 호출
        List<Reservation> reservations = reservationService.getRsrvStoreList(storeId, search);


        // 모델에 데이터 추가
        model.addAttribute("reservations", reservations);

        // 뷰 반환
        return "reservation/getRsrvStoreList";
    }


    @RequestMapping(value = "getRsrvStoreList", method = RequestMethod.POST)
    public String getRsrvStoreList(
            @RequestParam("storeId") int storeId,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "searchStatuses", required = false) List<String> searchStatuses,
            Model model) throws Exception {

        // Search 객체 생성
        Search search = new Search();

        // 조건 설정: 값이 있을 때만 설정
        if (startDate != null && !startDate.isEmpty()) {
            search.setStartDate(startDate);
        }

        if (endDate != null && !endDate.isEmpty()) {
            search.setEndDate(endDate);
        }

        if (searchStatuses != null && !searchStatuses.isEmpty()) {
            search.setSearchStatuses(searchStatuses);
        }

        // 서비스 호출
        List<Reservation> reservations = reservationService.getRsrvStoreList(storeId, search);

        model.addAttribute("reservations", reservations);

        return "reservation/getRsrvStoreList";
    }



    @RequestMapping(value = "getRsrvUserList", method = RequestMethod.GET)
    public String getRsrvUserList(
            @ModelAttribute Search search,
            @RequestParam("userName") String userName, // 가게 ID는 필수
            Model model) throws Exception {


        // 초기 Search 객체 설정 (기본값)
        search.setOrder("desc"); // 기본 정렬
        search.setSearchKeyword(null); // 모든 상태 포함

        // 서비스 호출
        List<Reservation> reservations = reservationService.getRsrvUserList(userName, search);

        // 모델에 데이터 추가
        model.addAttribute("reservations", reservations);
        model.addAttribute("search", search);

        // 뷰 반환
        return "reservation/getRsrvUserList";
    }


    @RequestMapping(value = "getRsrvUserList", method = RequestMethod.POST)
    public String getRsrvUserList(
            @ModelAttribute Search search,
            @RequestParam("userName") String userName,
            @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
            @RequestParam(value = "order", required = false) String order,
            Model model) throws Exception {


        // 조건 설정: 값이 있을 때만 설정

        search.setSearchKeyword(searchKeyword);

        search.setOrder(order);

        // 서비스 호출
        List<Reservation> reservations = reservationService.getRsrvUserList(userName, search);

        // 모델에 데이터 추가
        model.addAttribute("reservations", reservations);
        model.addAttribute("search", search);

        return "reservation/getRsrvUserList";
    }


    @RequestMapping(value = "addRsrv", method = RequestMethod.GET)
    public String addRsrv(
            @RequestParam("storeId") int storeId,
            @RequestParam(value = "effectDt", required = false) String effectDtStr, // String으로 받기
            Model model) {

        System.out.println("/reservation/addReservation : GET");

        java.util.Date effectDt;

        try {
            // effectDt가 null이면 현재 날짜 사용
            if (effectDtStr == null || effectDtStr.isEmpty()) {
                effectDt = new java.util.Date();
            } else {
                // "yyyy-MM-dd" 형식의 날짜를 java.util.Date로 변환
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                effectDt = dateFormat.parse(effectDtStr);
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid date format for effectDt");
        }

        // java.sql.Date로 변환
        java.sql.Date sqlEffectDt = new java.sql.Date(effectDt.getTime());

        Store store = storeService.getStore(storeId, sqlEffectDt);

        model.addAttribute("store", store);

        return "reservation/addRsrv";
    }


    @RequestMapping(value = "addRsrv", method = RequestMethod.POST)
    public String addRsrv(
            @RequestParam("rsrvDt") String rsrvDtStr, // String으로 받기
            @ModelAttribute("reservation") Reservation reservation,
            Model model) throws Exception {

        System.out.println("/reservation/addReservation : POST");

        // "yyyy-MM-dd'T'HH:mm" 형식의 rsrvDt를 java.util.Date로 변환
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        java.util.Date parsedDate = dateTimeFormat.parse(rsrvDtStr);

        // java.sql.Date 또는 java.sql.Timestamp로 변환 필요
        reservation.setRsrvDt(new java.sql.Date(parsedDate.getTime())); // java.sql.Date 사용 시

        // Business Logic
        reservationService.addRsrv(reservation);

        // 저장 후 예약 번호 가져오기 (MyBatis나 JPA의 경우, reservation 객체에 rsrvNo가 자동으로 설정됨)
        int rsrvNo = reservation.getRsrvNo(); // MyBatis나 JPA가 예약 번호를 설정했다고 가정

        // 예약 번호를 pay 페이지로 전달
        model.addAttribute("rsrvNo", rsrvNo);

        return "reservation/chkRsrv";
    }

    @RequestMapping(value = "chkRsrv", method = RequestMethod.POST)
    public String chkRsrv(@RequestParam("rsrvNo") int rsrvNo, Model model) throws Exception {

        System.out.println("/reservation/Paycheck : POST");
        // 1. 예약 정보 조회
        Reservation reservation = reservationService.getRsrv(rsrvNo);

        model.addAttribute("reservation", reservation);

        return "reservation/sendRsrv";
    }


    @RequestMapping(value = "sendRsrv", method = RequestMethod.GET)
    public String confirmPayGet(
            @RequestParam("orderId") String orderId,
            @RequestParam("paymentKey") String paymentKey,
            @RequestParam int amount,
            Model model) throws Exception {

        // 필요한 데이터 모델에 추가
        System.out.println("/reservation/sendRsrv : GET");
        model.addAttribute("orderId", orderId);
        model.addAttribute("paymentKey", paymentKey);
        model.addAttribute("amount", amount);


        return "reservation/sendRsrv";
    }


    @RequestMapping(value = "addRsrvResult", method = RequestMethod.POST)
    public String confirmPay(@RequestParam("orderId") String orderId,
                             @RequestParam("paymentKey") String paymentKey,
                             @RequestParam int amount,
                             Model model) throws Exception {

        String[] parts = orderId.split("_");

        int rsrvNo = Integer.parseInt(parts[0]); // 예약 번호 추출

        paymentService.confirmPayment(paymentKey, orderId, amount);

        String paymentId = paymentKey;


        reservationService.updateRsrvpay(rsrvNo, paymentId);


        // 1. 예약 정보 조회
        Reservation reservation = reservationService.getRsrv(rsrvNo);

        java.sql.Date sqlRsrvDt = new java.sql.Date(reservation.getRsrvDt().getTime());

        // 2. 가게 정보 조회 (예약 최대 인수)
        Store store = storeService.getStore(reservation.getStoreId(), sqlRsrvDt);
        StoreOperation storeOperation = store.getStoreOperation();
        int maxCapacity = store.getStoreOperation().getRsrvLimit(); // 가게의 예약 최대 인수

        // 3. 휴무일 확인
        List<String> closedayList = storeOperation.getClosedayList(); // 가게의 휴무일 목록
        String reservationDate = sqlRsrvDt.toString(); // 예약 날짜를 문자열로 변환

        if (closedayList != null && closedayList.contains(reservationDate)) {
            // 예약 일자가 휴무일인 경우

            paymentService.refundPayment(reservation.getPaymentId(), "예약이 마감되었습니다.",null, amount);
            reservationService.updateRsrvStatus(rsrvNo, "예약 취소");

            // fail.html로 이동
            model.addAttribute("message", "예약이 불가능한 날짜(휴무일)입니다.");
            return "reservation/failRsrv";
        }

        // 4. 현재 예약 일시의 예약 인수 계산
        int currentReservationCount = reservationService.getCountRsrv(reservation.getRsrvDt(), reservation.getStoreId());
        int totalReservationCount = currentReservationCount + reservation.getRsrvPerson();

        // 5. 예약 최대 인수 비교 및 처리
        if (totalReservationCount > maxCapacity) {
            // 예약 상태를 "예약 취소"로 변경

            paymentService.refundPayment(reservation.getPaymentId(), "예약이 마감되었습니다." ,null, amount);
            reservationService.updateRsrvStatus(rsrvNo, "예약 취소");

            // fail.html로 이동
            model.addAttribute("message", "예약 인원이 가게의 최대 예약 인원을 초과했습니다.");
            return "reservation/failRsrv";
        } else {
            // 예약 상태를 "예약 요청"으로 변경
            reservationService.updateRsrvStatus(rsrvNo, "예약 요청");

            // success.html로 이동
            return "reservation/successRsrv";
        }
    }


//    @RequestMapping(value = "paycheck", method = RequestMethod.GET)
//    public String Paycheck(@RequestParam("orderId") String orderId,
//                           @RequestParam("paymentKey") String paymentKey,
//                           @RequestParam int amount,
//                           Model model) throws Exception {
//
//        System.out.println("/reservation/Paycheck : GET");
//
//        String[] parts = orderId.split("_");
//
//        int rsrvNo = Integer.parseInt(parts[0]); // 예약 번호 추출
//
//        paymentService.confirmPayment(paymentKey, orderId, amount);
//
//        String paymentId = paymentKey;
//
//
//        reservationService.updateRsrvpay(rsrvNo, paymentId);
//
//
//
//        model.addAttribute("rsrvNo", rsrvNo);
//
//        return "test/reservation/pay";
//    }


}