package com.placeHere.server.controller.reservation;

import com.placeHere.server.domain.Reservation;
import com.placeHere.server.domain.Search;
import com.placeHere.server.service.pointShop.ProductService;
import com.placeHere.server.service.reservation.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.List;


@Controller
@RequestMapping("/reservation/*")
public class ReservationController {

    @Autowired
    @Qualifier("productServiceImpl")
    private ProductService productService;
    @Autowired
    private ReservationService reservationService;

    public ReservationController(){
        System.out.println(this.getClass());
    }

    @RequestMapping( value="getRsrv", method=RequestMethod.GET )
    public String getRsrv(@RequestParam("rsrvNo") int rsrvNo, Model model) throws Exception {

        Reservation reservation = reservationService.getRsrv(rsrvNo);

        System.out.println("/reservation/getRsrv : GET");

        model.addAttribute("reservation", reservation);

        return "reservation/getrsrv";
    }


    @RequestMapping(value = "getRsrvStoreList", method = RequestMethod.GET)
    public String getRsrvStoreList(
            @RequestParam("storeId") int storeId, // 가게 ID는 필수
            @RequestParam(value = "startDate", required = false) String startDate, // 시작 날짜
            @RequestParam(value = "endDate", required = false) String endDate, // 종료 날짜
            @RequestParam(value = "searchStatuses", required = false) List<String> searchStatuses, // 상태 목록
            Model model) throws Exception {

        // Search 객체 생성
        Search search = new Search();

        // 조건 설정
        if (startDate != null) {
            search.setStartDate(startDate); // "yyyy-MM-dd" 형식 필요
        }
        if (endDate != null) {
            search.setEndDate(endDate); // "yyyy-MM-dd" 형식 필요
        }
        search.setSearchStatuses(searchStatuses); // 예약 상태 목록

        // 서비스 호출
        List<Reservation> reservations = reservationService.getRsrvStoreList(storeId, search);

        // 모델에 데이터 추가
        model.addAttribute("reservations", reservations);

        // 뷰 반환
        return "reservation/listrsrvstore";
    }


    @RequestMapping(value = "getRsrvUserList", method = RequestMethod.GET)
    public String getRsrvUserList(
            @RequestParam("userName") String userName, // 유저 네임은 필수
            @RequestParam(value = "searchKeyword", required = false) String searchKeyword, // 예약 상태 검색
            @RequestParam(value = "order", required = false) String order, // 차순
            Model model) throws Exception {

        // Search 객체 생성
        Search search = new Search();

        if (searchKeyword != null) {
            search.setSearchKeyword(searchKeyword);
        }
        if (order != null) {
            search.setOrder(order);
        }

        // 서비스 호출
       List<Reservation> reservations = reservationService.getRsrvUserList(userName, search);

        // 모델에 데이터 추가
        model.addAttribute("reservations", reservations);

        // 뷰 반환
        return "reservation/listrsrvuser";
    }

}