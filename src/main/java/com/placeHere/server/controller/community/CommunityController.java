package com.placeHere.server.controller.community;

import com.placeHere.server.dao.community.CommunityDao;
import com.placeHere.server.domain.*;
import com.placeHere.server.service.community.CommunityService;
import com.placeHere.server.service.community.FriendService;
import com.placeHere.server.service.like.LikeService;
import com.placeHere.server.service.reservation.ReservationService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/review")
public class CommunityController {

    //Field
    @Autowired
    @Qualifier("communityServiceImpl")
    private CommunityService communityService;

    @Autowired
    @Qualifier("reservationServiceImpl")
    private ReservationService reservationService;

    @Autowired
    @Qualifier("FriendServiceImpl")
    private FriendService friendService;


    public CommunityController() {
        System.out.println(this.getClass());
    }

    //  @RequestMapping (value = "/addReview.do" , method = RequestMethod.GET)
    @GetMapping("/addReview")
    public String addReview(@RequestParam("rsrvNo") int rsrvNo,
                            @RequestParam("userName") String userName,
                            @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
                            @RequestParam(value = "order", required = false) String order,
                            Model model) throws Exception {

        System.out.println("/addReview.do");

        Search search = new Search();

        // 조건 설정: 값이 있을 때만 설정
        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            search.setSearchKeyword(searchKeyword);
        }
        if (order != null && !order.isEmpty()) {
            search.setOrder(order);
        }

        //예약 정보 가져옴
        Reservation reservation = reservationService.getRsrv(rsrvNo);
        List<Reservation> reservations = reservationService.getRsrvUserList(userName, search);

        Review review = new Review();
        model.addAttribute("reservations", reservations);
        model.addAttribute("reservation", reservation);
        model.addAttribute("review", review);

        return "test/community/addReview";
    }

    @PostMapping("/addReview")
    public String addReview(@ModelAttribute("review") Review review) throws Exception {

        System.out.println("/addReview.do" + review.toString());
        // B/L
        communityService.addReview(review);

        return "redirect:/review/getReviewList";
    }


    // getReview
    @GetMapping("/getReview")
    public String getReview(@RequestParam("reviewNo") int reviewNo, Model model) throws Exception {

        Review review = communityService.getReview(reviewNo);
        if (review == null) {
            throw new Exception("리뷰를 찾을 수 없습니다. reviewNo: " + reviewNo);
        }



        model.addAttribute("review", review);
        // 댓글 리스트 불러오는 거
        List<Comment> commentList = communityService.getCommentList(reviewNo);
        model.addAttribute("commentList", commentList);

        return "test/community/getReview";
    }


//    //리뷰 삭제
//    @PostMapping("/removeReview")
//    public String removeReview(@RequestParam("reviewNo") int reviewNo) throws Exception{
//        Review review = new Review();
//        review.setReviewNo(reviewNo);
//
//        //리뷰 삭제 메서드 => 리뷰 상테 값 변경
//        communityService.removeReview(review);
//
//        return "redirect:/review/getReviewList";
//    }


    //   //getComment
    @PostMapping("/getComment")
    public String getCommentList(@RequestParam("reviewNo") int reviewNo, Model model) throws Exception {

        System.out.println("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
        System.out.println("getComment" + reviewNo);

        List<Comment> comments = communityService.getCommentList(reviewNo);

        model.addAttribute("commentList", comments);

        Review review = communityService.getReview(reviewNo);
        model.addAttribute("review", review);

        return "test/community/getReview";
    }

    //updateReview
    @GetMapping("/updateReview")
    public String updateReview(@RequestParam("reviewNo") int reviewNo, Model model) throws Exception {
        System.out.println("updateReview 페이지로 이동: reviewNo = " + reviewNo);

        Review review = communityService.getReview(reviewNo);
        model.addAttribute("review", review);
        return "test/community/updateReview";
    }

    @PostMapping("/updateReview")
    public String updateReview(@ModelAttribute("review") Review review) throws Exception {
        System.out.println("/review/updateReview : POST");

        communityService.updateReview(review);
        return "redirect:/review/getReview?reviewNo=" + review.getReviewNo();
    }


    //    // 리뷰 삭제
//    @PostMapping("/removeReview")
//    public String removeReview(@RequestParam("reviewNo") int reviewNo) throws Exception {
//
//        Review review = new Review();
//        review.setReviewNo(reviewNo);
//
//        communityService.removeReview(review);
//        return "redirect:/review/getReviewList"; // 리뷰 목록으로 리다이렉션
//
//    }
//
//
//   //getReviewList
////    @GetMapping("/getReviewList")
////    public String getReviewList(Model model) throws Exception {
////    System.out.println("/review/getReviewList : GET");
////
////
////
////        List<Review> getReviewList = communityService.;
////        model.addAttribute("getReviewList", getReviewList);
////        return "test/community/getReviewList";
////
////        }
//
    //getReviewList ( 리뷰 전체 목록 보기 + 친구 리뷰 불러오기  ==> todo 현재 user정보가 없어 하드코딩 함 )
    @GetMapping("/getReviewList")
    public String getReviewList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int listSize,
            @RequestParam(value = "friendUsername", required = false) String friendUsername,
            Model model
    ) {
        try {
            // Search 객체를 생성하고 페이지 번호 및 리스트 사이즈를 설정
            Search search = new Search();
            search.setPage(page); // 현재 페이지 설정
            search.setListSize(listSize); // 리스트 사이즈 설정

            List<Review> reviewList;
            boolean chkFriend = false;

            if (friendUsername != null && !friendUsername.isEmpty()) {
            boolean isFriend = friendService.chkFriend("user02", friendUsername);

                model.addAttribute("isFriend", isFriend);
                reviewList = communityService.getReviewList(List.of(friendUsername), search);
            } else {
                reviewList = communityService.getReviewList(search);
            }
            model.addAttribute("reviewList", reviewList);
            model.addAttribute("friendUsername", friendUsername); // 버튼에서 사용할 username 추가
            return "test/community/getReviewList"; // 리뷰 리스트 화면
        } catch (Exception e) {
            e.printStackTrace();
            return "error"; // 에러 발생 시 error.html로 이동
        }
    }


    // 내가 작성한 리뷰 리스트 보기
    @GetMapping("/getMyReviewList")
    public String getMyReviewList(Model model) {
        String username = "user01"; // todo user정보 로그인 되기 전까지 하드코딩
        Search search = new Search();
        List<Review> myReview = communityService.getReviewList(List.of(username), search);
        model.addAttribute("reviewList", myReview);
        return "test/community/myReviewList";
    }


    //친구 신청
    @PostMapping("/friend/request")
    public String requestFriend(@RequestParam String  friendUsername){
        try {
            Friend friend = new Friend();
            friend.setFriendReq("user02");   // todo 현재 하드 코딩
            friend.setFriendRes(friendUsername);

            //친구 신청 메서드 호출
            friendService.sendFriendReq(friend);
            return "redirect:/review/getReviewList?page=1&size=10&friendUsername=" + friendUsername; // 리뷰 목록으로 리다이렉트
        } catch (Exception e) {
            e.printStackTrace();
            return "error"; // 오류 발생 시 에러 페이지로 이동
        }
    }

    // 친구 삭제 ( 친구삭제 버튼 안되는 거 )
//    @PostMapping("/friend/remove")
//    public String removeFriend(@RequestParam String friendUsername){
//        try {
//            //친구 정보 가져오는 객체 생성
//            Friend friend = new Friend();
//            //현재 사용자 이름 todo 나중에 하드코딩인거 수정
//            friend.setFriendReq("user02");
//            friend.setFriendRes(friendUsername);
//
//            //친구 삭제 요청 호출
//            if (friendService.removeFriendReq(friend.getFriendNo())) {
//            }
//
//            return "redirect:/review/getReviewList?page=1&size=10&friendUsername=" + friendUsername; // 리뷰 목록으로 리다이렉트
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "error"; // 오류 발생 시 에러 페이지로 이동
//        }
//    }


    //친구 삭제 도전 1 => 실패 // Rest 로 다시 도전 )

//        @PostMapping("/remove")
//        public String removeFriend(@RequestParam("friendNo")  String friendNoStr, RedirectAttributes redirectAttributes) {
//            try {
//                //문자열 int로 변환
//                int friendNo = Integer.parseInt(friendNoStr);
//                boolean success = friendService.removeFriendReq(friendNo);
//                if (success) {
//                    redirectAttributes.addFlashAttribute("message", "친구가 삭제되었습니다.");
//                } else {
//                    redirectAttributes.addFlashAttribute("message", "친구 삭제에 실패했습니다.");
//                }
//            } catch (NumberFormatException e) {
//                redirectAttributes.addFlashAttribute("message", "유효하지 않은 친구 번호입니다.");
//            } catch (Exception e) {
//                redirectAttributes.addFlashAttribute("message", "오류가 발생했습니다: " + e.getMessage());
//            }
//            return "redirect:/review/getReviewList";
//    }


    @GetMapping("/test")
    public String test(){
        return "test/community/aaa";
    }
}





