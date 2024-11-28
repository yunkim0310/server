package com.placeHere.server.controller.reservation;

import com.placeHere.server.domain.Reservation;
import com.placeHere.server.domain.Search;
import com.placeHere.server.domain.Store;
import com.placeHere.server.service.reservation.ReservationService;
import com.placeHere.server.service.store.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;


@Controller
@RequestMapping("test/reservation/*")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private StoreService storeService;


    public ReservationController(){
        System.out.println(this.getClass());
    }

    @RequestMapping( value="getRsrv", method=RequestMethod.GET )
    public String getRsrv(@RequestParam("rsrvNo") int rsrvNo, Model model) throws Exception {

        Reservation reservation = reservationService.getRsrv(rsrvNo);

        System.out.println("/reservation/getRsrv : GET");

        model.addAttribute("reservation", reservation);

        return "test/reservation/getrsrv";
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
        return "test/reservation/listrsrvstore";
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

        return "test/reservation/listrsrvstore";
    }



    @RequestMapping(value = "getRsrvUserList", method = RequestMethod.GET)
    public String getRsrvUserList(
            @RequestParam("userName") String userName, // 가게 ID는 필수
            Model model) throws Exception {

        // 초기 Search 객체 설정 (기본값)
        Search search = new Search();
        search.setOrder(null); // 기본값 없음
        search.setSearchKeyword(null); // 모든 상태 포함

        // 서비스 호출
        List<Reservation> reservations = reservationService.getRsrvUserList(userName, search);

        // 모델에 데이터 추가
        model.addAttribute("reservations", reservations);

        // 뷰 반환
        return "test/reservation/listrsrvuser";
    }


    @RequestMapping(value = "getRsrvUserList", method = RequestMethod.POST)
    public String getRsrvUserList(
            @RequestParam("userName") String userName,
            @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
            @RequestParam(value = "order", required = false) String order,
            Model model) throws Exception {

        // Search 객체 생성
        Search search = new Search();

        // 조건 설정: 값이 있을 때만 설정
        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            search.setSearchKeyword(searchKeyword);
        }
        if (order != null && !order.isEmpty()) {
            search.setOrder(order);
        }

        // 서비스 호출
        List<Reservation> reservations = reservationService.getRsrvUserList(userName, search);

        // 모델에 데이터 추가
        model.addAttribute("reservations", reservations);

        return "test/reservation/listrsrvuser";
    }


    @RequestMapping(value = "addRsrv", method = RequestMethod.GET)
    public String addRsrv(
            @RequestParam("storeId") int storeId,
            @RequestParam(value = "effectDt", required = false) Date effectDt,
            Model model) {

        if (effectDt == null) {
            effectDt = new Date(System.currentTimeMillis()); // 현재 날짜
        }

        Store store = storeService.getStore(storeId, effectDt);

        model.addAttribute("store", store);

        return "test/reservation/addrsrv";
    }

}