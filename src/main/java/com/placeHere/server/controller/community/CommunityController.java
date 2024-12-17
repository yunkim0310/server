package com.placeHere.server.controller.community;

import com.placeHere.server.domain.*;
import com.placeHere.server.service.community.CommunityService;
import com.placeHere.server.service.community.FriendService;
import com.placeHere.server.service.like.LikeService;
import com.placeHere.server.service.reservation.ReservationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/review/*")
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

    @Autowired
    private LikeService likeService;

    @Value("${page_size}")
    private int pageSize;

    @Value("${list_size}")
    private int listSize;

    @Value("${cloud.aws.s3.bucket-url}")
    private String bucketUrl;


    // Constructor
    public CommunityController() {
        System.out.println(this.getClass());
    }


    // Method
    //  @RequestMapping (value = "/addReview.do" , method = RequestMethod.GET)
    @GetMapping("/addReview")
    public String addReview(@SessionAttribute("user") User user, Model model) throws Exception {

        System.out.println("/addReview.do : Get");

        Search search = new Search();

        // 사용자 이름을 가져옴
        String userName = user.getUsername();

        search.setSearchKeyword("이용 완료");
        search.setOrder("desc");

        List<Reservation> reservations = reservationService.getRsrvUserList(userName, search);

        Review review = new Review();

        model.addAttribute("url", bucketUrl);
        model.addAttribute("reservations", reservations);
        model.addAttribute("review", review);
        model.addAttribute("currentUser", user);

        return "test/community/addReview";
    }

    @PostMapping("/addReview")
    public String addReview(@RequestParam("rsrvNo") int rsrvNo, @ModelAttribute("review") Review review) throws Exception {

        System.out.println("/addReview.do : Post" + review.toString());
        // B/L
        communityService.addReview(review);

        reservationService.updateRsrvStatus(rsrvNo,"리뷰 작성");

        return "redirect:/review/getReviewList?type=myFeed";
    }


    // getReview 리뷰 상세 조회
    @GetMapping("/getReview")
    public String getReview(@RequestParam("reviewNo") int reviewNo,
                            @ModelAttribute Search search,
                            Model model,
                            @SessionAttribute("user") User user) throws Exception {

        System.out.println("/review/getReview : GET");

        search.setPageSize(pageSize);
        search.setListSize(listSize);

        Review review = communityService.getReview(reviewNo, search);

        if (review == null) {
            throw new Exception("리뷰를 찾을 수 없습니다. reviewNo: " + reviewNo);
        } else {
            
            // 좋아요 판별
            Like chkLike = new Like();
            chkLike.setRelationNo(review.getReviewNo());
            chkLike.setTarget("review");
            chkLike.setUserName(user.getUsername());

            Like chkedLike = likeService.chkLike(chkLike);

            review.setLike(chkedLike);
        }

        // 댓글 리스트 불러오는 거
        List<Comment> commentList = communityService.getCommentList(reviewNo, search);
        int commentTotalCnt = (commentList.isEmpty()) ? 0 : commentList.get(0).getCommentTotalCnt();

        Paging paging = new Paging(commentTotalCnt, search.getPage(), search.getPageSize(), search.getListSize());

        model.addAttribute("url", bucketUrl);
        model.addAttribute("paging", paging);
        model.addAttribute("review", review);
        model.addAttribute("commentList", commentList);
        model.addAttribute("user", user);

        System.out.println("getReview컨트롤러 user == "+ user);
        System.out.println("cont" + review);

        return "community/getReview";
    }

//    @PostMapping("/getComment")
//    public String getCommentList(@RequestParam("reviewNo") int reviewNo,
//                                 @ModelAttribute Search search, Model model) throws Exception {
//
//        System.out.println("/review/getComment : POST");
//        System.out.println("getComment" + reviewNo);
//
//        search.setPageSize(pageSize);
//        search.setListSize(listSize);
//
//        // 댓글 리스트 가져오기
//        List<Comment> comments = communityService.getCommentList(reviewNo, search);
//
//        //로그에서 확인된 count 값을 사용
//        int totalCnt = comments.isEmpty() ? 0 : comments.get(0).getCommentTotalCnt();
//
//        // Paging 객체 생성
//        Paging paging = new Paging(totalCnt, search.getPage(), search.getPageSize(), search.getListSize());
//        model.addAttribute("paging", paging);
//
//        model.addAttribute("commentList", comments);
//
//        Review review = communityService.getReview(reviewNo);
//        model.addAttribute("review", review);
//
//        return "test/community/getReview";
//    }


    //updateReview 리뷰 수정
    @GetMapping("/updateReview")
    public String updateReview(@RequestParam("reviewNo") int reviewNo, Model model) throws Exception {

        System.out.println("/review/updateReview : GET");
        System.out.println("updateReview 페이지로 이동: reviewNo = " + reviewNo);

        Search search = new Search(pageSize, listSize);
        Review review = communityService.getReview(reviewNo, search);

        model.addAttribute("review", review);
        model.addAttribute("url", bucketUrl);

        return "test/community/updateReview";
    }

    @PostMapping("/updateReview")
    public String updateReview(@ModelAttribute("review") Review review) throws Exception {

        System.out.println("/review/updateReview : POST");

        communityService.updateReview(review);

        return "redirect:/review/getReviewList?type=myFeed";
    }


    // 리뷰 삭제
    @PostMapping("/removeReview")
    public String removeReivew(@RequestParam("reviewNo") int reviewNo) throws Exception {

        Review review = new Review();
        review.setReviewNo(reviewNo);

        communityService.removeReview(review);
        return "redirect:/review/getReviewList?type=myFeed";

    }

    // TODO 2
    //getReviewList ( 리뷰 전체 목록 보기(ok) + 친구 리뷰 불러오기 + 다른사람의 리뷰리스트 , My피드 리뷰리스트(ok) ) ==> todo 현재 user정보가 없어 하드코딩 함 )
    // getReviewList.html +  getMyReviewList.html + getFriendReviewList.html + getOtherFeedView.html
    @GetMapping("/getReviewList")
    public String getReviewList(
            @ModelAttribute Search search,
            //friendUsername = friendReq + friendRes / 즉 나와 친구인 username
            @RequestParam(value = "friendUsername", required = false) String friendUsername,
            @SessionAttribute("user") User user,
            //type의 따라 조회하는 데이터 형태를 결정 ex) type=my : 나 / type=friend : 친구
            @RequestParam(value = "type", required = false, defaultValue = "") String type,
            Model model) {

        System.out.println("/review/getReviewList : GET");

        try {
            System.out.println("요청된 페이지: " + search.getPage());
            System.out.println("친구 사용자 이름: " + friendUsername);
            System.out.println("요청 타입: " + type);

            // TODO Search 에 page 가 바인딩 되어오지 않으면 추가 코드 필요

            // Search 객체를 생성하고 페이지 번호 및 리스트 사이즈를 설정
            search.setListSize(listSize); // 리스트 사이즈 설정
            search.setPageSize(pageSize);

            // 리뷰 리스트 초기화
            List<Review> reviewList = new ArrayList<Review>();

            String currentUser = user.getUsername();
            System.out.println("currentUser : "+currentUser);

            String result = "";
            Paging paging = new Paging();
            int totalCnt = 0;

            switch (type) {

                case "allList":
                    // 전체 리뷰 리스트 가져오기
                    reviewList = communityService.getReviewList(search);

                    //페이징을 위한거
                    totalCnt = (reviewList.isEmpty())? 0 : reviewList.get(0).getReviewTotalCnt() ;
                    paging = new Paging(totalCnt, search.getPage(), search.getPageSize(), search.getListSize());

                    result = "test/community/getReviewList";

                    break;

                case "friendList":
                    if (friendUsername != null && !friendUsername.isEmpty()) {
                        Friend friend = new Friend();
                        friend.setFriendReq(currentUser);
                        friend.setFriendRes(friendUsername);
                        System.out.println("friendUsername : "+friendUsername);


                        // 친구 여부 확인
                        Friend friendCheck = friendService.chkFriend(friend);

                        // friendCheck가 null이 아닐 경우 친구
                        boolean isFriend = (friendCheck != null && friendCheck.isFriendStatus());

                        boolean friendStatus;
                        if (isFriend) {
                            friendStatus = true;
                            model.addAttribute("friendNo", friendCheck.getFriendNo()); // 친구 번호 추가
                        } else {
                            friendStatus = false;
                        }

                        model.addAttribute("isFriend", isFriend);
                        model.addAttribute("friendStatus", friendStatus);

                        // 친구가 아닐 경우 메시지 처리
                        if (isFriend) {
                            // 친구의 리뷰 리스트 가져오기
                            reviewList = communityService.getReviewList(List.of(friendUsername), search);
                        } else {
                            reviewList = communityService.getReviewList(List.of(friendUsername), search);
                            model.addAttribute("message", "친구가 아닙니다.");
                            System.out.println("friendNo" + friend.getFriendNo());
                        }

                        //페이징을 위한거
                        totalCnt = (reviewList.isEmpty())? 0 : reviewList.get(0).getReviewTotalCnt() ;
                        paging = new Paging(totalCnt, search.getPage(), search.getPageSize(), search.getListSize());

                        //친구 신청 시 res 값 넣어줌
                        model.addAttribute("friendUsername", friendUsername);
                        model.addAttribute("paging", paging);


                        if (friendUsername.equals(currentUser)) {
                            result = "test/community/getMyReviewList"; // 사용자의 리뷰 리스트로 이동
                        } else {
                            result = "test/community/feedTest"; // 친구의 피드로 이동
                        }
                    }
                    break;

                case "myFeed":

                    search.setSearchKeyword("이용 완료");
                    search.setOrder("desc");

                    System.out.println("test1111 " + currentUser);

                    String userName = currentUser;

                    // 나의 리뷰 리스트 가져오기
                    reviewList = communityService.getReviewList(List.of(currentUser), search);

                    //리뷰 작성 할 수 있는 예약 정보
                    List<Reservation> reservations = reservationService.getRsrvUserList(userName, search);
                    for (Reservation reservation :reservations){
                        int rsrvNo = reservation.getRsrvNo();
                        System.out.println(rsrvNo);
                    }

//                    model.addAttribute("reviewList", reviewList);
                    model.addAttribute("reservations", reservations);

                    //페이징을 위한 totalCnt
                    totalCnt = (reviewList.isEmpty())? 0 : reviewList.get(0).getReviewTotalCnt() ;

                    //Constroller에서 Paging을 Model로 보내야한다
                    paging = new Paging(totalCnt, search.getPage(), search.getPageSize(), search.getListSize());

//                    return "test/community/getMyReviewList";
                    result =  "test/community/getMyReviewList";

                    break;

                case "otherFeed":
                    // 다른 사람 리뷰 리스트 가져오기
                    if (friendUsername != null && !friendUsername.isEmpty()) {
                        reviewList = communityService.getReviewList(List.of(friendUsername), search);
//                        model.addAttribute("reviewList", reviewList);
//                        return "test/community/getOtherFeedView";
                        result = "test/community/getOtherFeedView";
                    }
                    break;

                default:
                    // 전체 리뷰 리스트 가져오기 (디폴트 행동)
                    reviewList = communityService.getReviewList(search);
//                    model.addAttribute("reviewList", reviewList);
//                    return "test/community/testGetReviewList";

                    totalCnt = (reviewList.isEmpty())? 0 : reviewList.get(0).getReviewTotalCnt() ;
                    paging = new Paging(totalCnt, search.getPage(), search.getPageSize(), search.getListSize());

                    result = "test/community/getReviewList";
            }

            for (int i = 0; i < reviewList.size(); i++) {

                Review review = reviewList.get(i);

                Like chkLike = new Like();
                chkLike.setRelationNo(review.getReviewNo());
                chkLike.setTarget("review");
                chkLike.setUserName(user.getUsername());

                Like chkedLike = likeService.chkLike(chkLike);

                review.setLike(chkedLike);

                reviewList.set(i, review);

            }

            System.out.println(reviewList);

            model.addAttribute("url", bucketUrl);
            model.addAttribute("reviewList", reviewList);
            model.addAttribute("paging", paging);

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

    }


    // 댓글 삭제
    @PostMapping("/removeComment")
    public String removeComment(@RequestParam("commentNo") int commentNo,
                                @RequestParam("reviewNo") int reviewNo) throws Exception {

        Comment comment = new Comment();
        comment.setCommentNo(commentNo);

        System.out.println("reviewNo : " + reviewNo);

        communityService.removeComment(comment);

        return "redirect:/review/getReview?reviewNo=" + reviewNo;

    }


    // 친구신청
    @PostMapping("/sendFriendReq")
    public String sendFriendReq(@RequestParam(value = "friendRes") String friendRes,
                                @SessionAttribute("user") User user) throws  Exception {

        System.out.println("/review/sendFriendReq : POST 친구 신청");
        System.out.println("friendRes : " + friendRes);
        System.out.println("친구 신청 요청: " + user.getUsername() + " -> " + friendRes);

        Friend friend = new Friend();
        friend.setFriendReq(user.getUsername());
        friend.setFriendRes(friendRes);

        // 친구 상태 확인
        Friend existingFriend = friendService.chkFriend(friend);
        if (existingFriend != null) {
            System.out.println("이미 친구 상태입니다: " + user.getUsername() + "와 " + friendRes + "는 이미 친구입니다.");
            return "redirect:/review/alreadyFriends"; // 친구인 경우 다른 페이지로 리다이렉트
        }

        friendService.sendFriendReq(friend);

        return "redirect:/review/getFriendReqStatus";
    }



    //친구삭제
    @PostMapping("/remove")
    public String removeFriend(@ModelAttribute Friend friend) {

        System.out.println("/review/remove : POST 친구 삭제");
        System.out.println("삭제할 친구 : " + friend);

        try {
            // 친구 삭제 로직 (서비스 호출)
            friendService.removeFriend(friend);

            return "redirect:/review/getFriendList";
        } catch (Exception e) {

            e.printStackTrace();

            return "error";
        }
    }


    //친구 요청 목록을 확인하다 (getFriendReq 합침)
    @GetMapping(value = "/getFriendReqStatus")
    public String getFriendReqList( @SessionAttribute("user") User user, Model model) throws Exception {

        System.out.println("/review/getFriendReqStatus : GET");

        String userName = user.getUsername();
        System.out.println("userName : "+userName);

        Search search = new Search();
        search.setListSize(10);
        search.setStartRowNum(0);

        try {

            System.out.println("친구 요청 목록을 가져옴. 사용자: " + userName);

            // 친구 요청 목록
            List<Friend> friendRequests = friendService.getFriendReqList(userName, search);
            // 수신된 친구 요청 목록
            List<Friend> receivedRequests = friendService.getFriendResList(userName, search);

            model.addAttribute("friendRequests", friendRequests);
            model.addAttribute("receivedRequests", receivedRequests);

        } catch (Exception e) {

            e.printStackTrace();

            model.addAttribute("message", "친구 요청을 가져오는 데 오류가 발생했습니다.");
        }

        return "test/community/getFriendReqStatus";
    }


    //친구 신청을 수락하다
    @PostMapping("/addFriend")
    public String addFriend(@RequestParam int friendNo, RedirectAttributes redirectAttributes) {

        System.out.println("/review/addFriend : POST 친구 신청 수락");

        try {
            // 친구 추가 메서드 호출
            boolean success = friendService.addFriend(friendNo);

            if (success) {
                redirectAttributes.addFlashAttribute("message", "친구 요청이 수락되었습니다.");
            } else {
                redirectAttributes.addFlashAttribute("message", "친구 요청 수락에 실패했습니다.");
            }

            return "redirect:/review/getFriendList";

        } catch (Exception e) {

            e.printStackTrace();

            // 오류 발생 시 에러 페이지로 이동
            return "error";
        }
    }


    // 친구 요청 거절
    @PostMapping("/removeFriendReq")
    public String removeFriendReq(@RequestParam int friendNo, RedirectAttributes redirectAttributes) {

        System.out.println("/review/removeFriendReq : POST 친구 요청 거절");

        try {

            Friend friend = new Friend();
            friend.setFriendNo(friendNo);

            boolean success = friendService.removeFriendReq(friend);

            if (success) {
                redirectAttributes.addFlashAttribute("message", "친구 요청이 거절되었습니다.");
            } else {
                redirectAttributes.addFlashAttribute("message", "친구 요청 거절에 실패했습니다.");
            }

            return "redirect:/review/getFriendReqStatus";

        } catch (Exception e) {

            e.printStackTrace();

            return "error";
        }

    }


    // 친구 목록 조회
    @GetMapping("/getFriendList")
    public String getFriendList(@SessionAttribute("user") User user, Search search,
                                @RequestParam(value = "keyword", required = false) String keyword, Model model) {

        System.out.println("/review/getFriendList : GET");

        try {
            String username = user.getUsername();
            System.out.println("username : "+username);

            // 친구 목록 가져오기
            List<Friend> friends = friendService.getFriendList(username, new Search(pageSize, 10), keyword);

            model.addAttribute("friends", friends);
            model.addAttribute("keyword", keyword);

            return "test/community/getFriendList";

        } catch (Exception e) {

            e.printStackTrace();

            return "error"; // 오류 발생 시 에러 페이지로 이동
        }
    }

    //aaa.html을 위한..
    @GetMapping("/test")
    public String test() {
        return "test/community/aaa";
    }
}






//    @PostMapping("/remove")
//    public String removeFriend(@RequestParam(required = false) Integer friendNo, Model model) {
//        if (friendNo == null || friendNo <= 0) {
//            // friendNo가 null이거나 유효하지 않은 경우 어떤 동작을 취할지 처리
//            System.out.println("friendNo가 비었거나 잘못되었습니다.");
//            return "redirect:/error"; // 또는 적절한 경로로 리다이렉트
//        }
//
//        try {
//            System.out.println("2222222  ::: " + friendNo);
//            // 친구 삭제 로직 (서비스 호출)
//            friendService.removeFriend(friendNo);
//            model.addAttribute("friendNo", friendNo);
//            System.out.println("3333333 ::" + friendNo);
//
//            return "redirect:/getOtherFeedView";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "error"; // 오류 발생 시 에러 페이지로 이동
//        }
//    }



//친구 목록 조회

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

//    //친구 신청
//    @PostMapping("/friend/request")
//    public String requestFriend(@RequestParam String friendUsername) {
//        try {
//            Friend friend = new Friend();
//            friend.setFriendReq("user02");   // todo 현재 하드 코딩
//            friend.setFriendRes(friendUsername);
//
//            //친구 신청 메서드 호출
//            friendService.sendFriendReq(friend);
//            return "redirect:/review/getReviewList?page=1&size=10&friendUsername=" + friendUsername; // 리뷰 목록으로 리다이렉트
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "error"; // 오류 발생 시 에러 페이지로 이동
//        }
//    }

//    //getOtherFeedView에서 친구 상태값을 위한 컨트롤러
//    @GetMapping("/getOtherFeedView")
//    public String getOtherFeedView(Model model) {
//
//        // 하드코딩된 사용자
//        String currentUser = "user01"; // 현재 나
//        String friendUser = "user06"; // 친구 요청 받을 사람
//
//        // 친구 상태 설정 (1: 친구, 0: 친구 x)
//        int friendStatus = (currentUser.equals("user01") && friendUser.equals("user06")) ? 1 : 0; // 하드코딩 예시
//
////        model.addAttribute("friendNo", friendNo);
//        model.addAttribute("friendStatus", friendStatus);
//        model.addAttribute("friendUsername", friendUser);
//        System.out.println("친구 상태: " + friendStatus);
//
//        return "test/community/getOtherFeedView";
//    }

//
//            //type = friend 이고 friendUsername 이 유효한 경우 친구의 리뷰를 조회
//            if (type.equals("friend") && friendUsername != null && !friendUsername.isEmpty()) {
//                //친구 여부 확인
//                //현재 로그인한 사용자 이름
//                String currentUser = "user01";
//
//                // 친구 여부 확인
//                boolean isFriend = friendService.chkFriend(currentUser, friendUsername);
//                model.addAttribute("isFriend", isFriend);
//
//                if (isFriend) {
//                    //친구의 리뷰 리스트 가져오기
//                    reviewList = communityService.getReviewList(List.of(friendUsername), search);
//                } else {
////                  //일반 사용자의 리뷰를 가져오기
//                    reviewList = communityService.getReviewList(List.of(friendUsername), search);
//                    model.addAttribute("message", "지정된 사용자는 친구가 아님.");
//                }
//                model.addAttribute("reviewList", reviewList);
//                return "test/community/getOtherFeedView";
//
//            } else if ("my".equals(type)) {
//                // 타입이 "my"인 경우 내 리뷰 리스트 가져오기
//                String username = "user01";
//                reviewList = communityService.getReviewList(List.of(username), search);
//                model.addAttribute("reviewList", reviewList);
//                return "test/community/getMyReviewList";
//
//            } else {
//                //전체 리뷰 가져오기
//                reviewList = communityService.getReviewList(search);
//                //리뷰 리스트와 친구 사용자 이름 추가
//                model.addAttribute("reviewList", reviewList);
//                return "test/community/getReviewList";
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "error";
//        }
//    }



