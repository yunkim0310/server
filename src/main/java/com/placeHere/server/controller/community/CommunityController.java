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

import java.sql.Date;
import java.util.ArrayList;
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

        System.out.println("cont" + review);

        return "test/community/getReview";
    }

    //getComment
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
        return "redirect:/review/getReviewList?type=my";
    }


    // 리뷰 삭제
    @PostMapping("/removeReview")
    public String removeReivew(@RequestParam("reviewNo") int reviewNo) throws Exception {

        Review review = new Review();
        review.setReviewNo(reviewNo);

        communityService.removeReview(review);
        return "redirect:/review/getReviewList?type=my";

    }


    //getReviewList ( 리뷰 전체 목록 보기 + 친구 리뷰 불러오기 + 다른사람의 리뷰리스트 , My피드 리뷰리스트 ) ==> todo 현재 user정보가 없어 하드코딩 함 )
    // getReviewList.html +  getMyReviewList.html + getFriendReviewList.html + getOtherFeedView.html
    @GetMapping("/getReviewList")
    public String getReviewList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int listSize,
            //friendUsername = friendReq + friendRes / 즉 나와 친구인 username
            @RequestParam(value = "friendUsername", required = false) String friendUsername,
            //type의 따라 조회하는 데이터 형태를 결정 ex) type=my : 나 / type=friend : 친구
            @RequestParam(value = "type", required = false) String type,
            Model model
    ) {
        try {
            System.out.println("요청된 페이지: " + page);
            System.out.println("요청된 리스트 사이즈: " + listSize);
            System.out.println("친구 사용자 이름: " + friendUsername);
            System.out.println("요청 타입: " + type);

            // Search 객체를 생성하고 페이지 번호 및 리스트 사이즈를 설정
            Search search = new Search();
            search.setPage(page); // 현재 페이지 설정
            search.setListSize(listSize); // 리스트 사이즈 설정

            // 리뷰 리스트 초기화
            List<Review> reviewList = new ArrayList<>();

            //type = friend 이고 friendUsername 이 유효한 경우 친구의 리뷰를 조회
            if ("friend".equals(type) && friendUsername != null && !friendUsername.isEmpty()) {
                //친구 여부 확인
                //현재 로그인한 사용자 이름
                String currentUser = "user02";
                boolean isFriend = friendService.chkFriend(currentUser, friendUsername);
                model.addAttribute("isFriend", isFriend);
                if (isFriend) {
//                    // 친구인 경우 친구의 리뷰 리스트 가져오기
                    reviewList = communityService.getReviewList(List.of(friendUsername), search);
                } else {
                    model.addAttribute("message", "해당 사용자는 친구가 아닙니다.");
                }
                model.addAttribute("reviewList", reviewList);
                System.out.println("111111111111" + reviewList);
                return "test/community/getFriendReviewList";

            } else if ("my".equals(type)) {
                // 타입이 "my"인 경우 내 리뷰 리스트 가져오기
                String username = "user01";
                reviewList = communityService.getReviewList(List.of(username), search);
                model.addAttribute("reviewList", reviewList);
                System.out.println("22222222" + reviewList);
                return "test/community/getMyReviewList";

            } else {
                // 친구가 아닌 일반 리뷰 조회
                reviewList = communityService.getReviewList(search);
                //리뷰 리스트와 친구 사용자 이름 추가
                model.addAttribute("reviewList", reviewList);
                model.addAttribute("friendUsername", friendUsername);
                System.out.println("333333333" + reviewList);
                return "test/community/getReviewList";

            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    //친구 신청
    @PostMapping("/friend/request")
    public String requestFriend(@RequestParam String friendUsername) {
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

    //댓글 수정
//    @PutMapping("/updatecomment")
//    public ResponseEntity<Comment> updateComment(@RequestBody Comment comment) throws Exception{
//        // 댓글 수정
//        communityService.updateComment(comment);
//        System.out.println("11111" + comment);
//
//        // 수정된 댓글을 반환
//        return ResponseEntity.ok(comment);
//
//    }

//    // 댓글 수정 1
//    @GetMapping("/updatecomment")
//    public String updateComment(@RequestParam("comment") Comment comment, Model model) throws Exception {
//        System.out.println("1111111111" + comment);
//
//        communityService.updateComment(comment);
//        model.addAttribute("comment", comment);
//        return "test/community/getReview";
//    }


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


//    @PostMapping("/addComment")
//    public String addComment(@ModelAttribute Comment comment, RedirectAttributes redirectAttributes){
//        comment.setUserName("user01");
//
//        try{
//            communityService.addComment(comment);
//            comment.setCommentsDt(new Date(System.currentTimeMillis()));
//
//            redirectAttributes.addFlashAttribute("message", "댓글이 등록되었습니다.");
//            return "redirect:/getReview"; // 리뷰 상세 페이지로 리다이렉트
//        } catch (Exception e) {
//            e.printStackTrace();
//            redirectAttributes.addFlashAttribute("error", "댓글 등록에 실패했습니다.");
//            return "redirect:/getReview";
//        }
//    }

//            boolean chkFriend = false;
//
//            if (friendUsername != null && !friendUsername.isEmpty()) {
//            boolean isFriend = friendService.chkFriend("user2", friendUsername);
//
//                model.addAttribute("isFriend", isFriend);
//                reviewList = communityService.getReviewList(List.of(friendUsername), search);
//            } else {
//                reviewList = communityService.getReviewList(search);
//            }
//            model.addAttribute("reviewList", reviewList);
//            model.addAttribute("friendUsername", friendUsername); // 버튼에서 사용할 username 추가
//            return "test/community/getReviewList"; // 리뷰 리스트 화면
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "error"; // 에러 발생 시 error.html로 이동
//        }
//    }


    // 내가 작성한 리뷰 리스트 보기
//    @GetMapping("/getMyReviewList")
//    public String getMyReviewList(Model model) {
//        String username = "user01"; // todo user정보 로그인 되기 전까지 하드코딩
//        Search search = new Search();
//        List<Review> myReview = communityService.getReviewList(List.of(username), search);
//
//        System.out.println("사용자: " + username);
//        System.out.println("가져온 리뷰 개수: " + myReview.size());
//        myReview.forEach(review -> {
//        System.out.println("리뷰 번호: " + review.getReviewNo());
//        System.out.println("리뷰 내용: " + review.getReviewContent());
//        System.out.println("작성자: " + review.getUserName());
//        System.out.println("별점: " + review.getReviewScore());
//        });
//
//        model.addAttribute("reviewList", myReview);
//        return "test/community/getMyReviewList";
//    }


    //aaa.html을 위한..
    @GetMapping("/test")
    public String test() {
        return "test/community/aaa";
    }
}





